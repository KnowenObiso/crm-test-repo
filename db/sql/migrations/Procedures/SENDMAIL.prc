--
-- SENDMAIL  (Procedure) 
--
CREATE OR REPLACE PROCEDURE TQ_CRM."SENDMAIL" (v_mail_from IN VARCHAR2,
                                    v_mail_to IN VARCHAR2,
                                    v_mail_subj IN VARCHAR2,
                                    v_mail_msg IN VARCHAR2,
                                    v_mailhost IN VARCHAR2,
                                    v_mail_cc  IN VARCHAR2) IS
    c utl_smtp.connection;
      mailhost    VARCHAR2(30) := v_mailhost;--'192.168.0.193';
      html_message VARCHAR2(1000) := '<html>
    <head>
        <title>some subject</title>
    </head>
    <body>
     <a href="http://">Yes</a>
      <a href="http://">No</a>
        Your <b>Html</b> email message here.
    </body>
</html>
';
    PROCEDURE send_header(NAME IN VARCHAR2, HEADER IN VARCHAR2) AS
    BEGIN
      utl_smtp.write_data(c, NAME || ': ' || HEADER || utl_tcp.CRLF);
    END;
     BEGIN
    -- raise_application_error(-20000,v_mail_from||' == '||
    --v_mail_to||' == '||
    --v_mail_subj||' == '||
    --v_mail_msg||' == '||
    --v_mailhost);
      c := utl_smtp.open_connection(mailhost);
       utl_smtp.helo(c, mailhost);
       utl_smtp.mail(c, v_mail_from);
       utl_smtp.rcpt(c, v_mail_to);
       utl_smtp.rcpt(c, v_mail_cc);
       utl_smtp.open_data(c);
       send_header('From',    ''||v_mail_from||' <'||v_mail_from||'>');
       send_header('To',      ''||v_mail_to||' <'||v_mail_to||'>');
       send_header('Cc',      ''||v_mail_cc||' <'||v_mail_cc||'>');
       send_header('Subject', v_mail_subj);
     
       utl_smtp.write_data(c, utl_tcp.CRLF || v_mail_msg);
      -- utl_smtp.write_data(c, 'CC: ' || v_mail_cc || utl_tcp.crlf);
     --  utl_smtp.write_data(c, 'BCC: ' || v_mail_bcc || utl_tcp.crlf);
       utl_smtp.close_data(c);
       utl_smtp.quit(c);
      /*  html_email('joseph@turnkeyafrica.com',
'TQ_HRMS',
'HTML',
'gata',
html_message,
mailhost,
null);*/
     EXCEPTION
       WHEN utl_smtp.transient_error OR utl_smtp.permanent_error THEN
         utl_smtp.quit(c);
         RAISE_APPLICATION_ERROR(-20000,
           'Failed to send mail due to the following error: ' || SQLERRM);
     END; 
 
/