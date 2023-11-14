--
-- TQC_EMAIL_PKG030215  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.Tqc_Email_Pkg030215 as

    TYPE email_files_rec IS RECORD
        (file_path VARCHAR2(150),
        file_name VARCHAR2(50));

      TYPE email_files_tab IS TABLE OF email_files_rec INDEX BY BINARY_INTEGER;

      v_attchment_rec CESCLOB;
      v_attchment_tab CESCLOBS;
       PROCEDURE CREATE_EMAIL_MSG(v_reciepient IN VARCHAR2,
                        v_clnt_code IN NUMBER,
                        v_agn_code IN NUMBER,
                        v_quot_code IN NUMBER,
                        v_quot_no IN VARCHAR2,
                        v_pol_code IN NUMBER,
                        v_pol_no IN VARCHAR2,
                        v_clm_no IN VARCHAR2,
                        v_msg_subj IN VARCHAR2,
                        v_msgt_code IN NUMBER,
                        v_files_tab IN     email_files_tab,
                        v_sys_code IN NUMBER,
                        v_sys_mod    IN VARCHAR2,
                        v_user IN VARCHAR2,
                        v_email_code IN OUT NUMBER,
                        v_app_lvl IN VARCHAR2,
                        v_usr_code IN NUMBER);
          PROCEDURE SEND_EMAIL_MSG(v_clnt_code IN NUMBER,
                        v_agn_code IN NUMBER,
                        v_quot_code IN NUMBER,
                        v_pol_code IN NUMBER,
                        v_pol_no IN VARCHAR2,
                        v_clm_no IN VARCHAR2,
                        v_email_addr IN VARCHAR2,
                        v_msg_subj IN VARCHAR2,
                        v_msg_text IN VARCHAR2,
                        v_sys_code IN NUMBER,
                        v_sys_mod    IN VARCHAR2,
                        v_files_tab IN     email_files_tab,
                        v_email_code OUT NUMBER,
                        v_email_sender_addr IN VARCHAR2);
   PROCEDURE load_attchmnt_from_url (v_eatt_code IN NUMBER,p_url  IN  VARCHAR2);
   PROCEDURE send_mail(v_email_code IN NUMBER);
   v_user VARCHAR2(35):= Pkg_Global_Vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesFiles,
                      clob_attach cesCLOBs default null,
                      blob_attach cesBLOBs default null);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesFiles,
                      clob_attach cesCLOBs default null,
                      blob_attach cesBLOBs default null);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesFiles,
                      clob_attach cesCLOBs default null,
                      blob_attach cesBLOBs default null);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesFiles,
                      clob_attach cesCLOBs default null,
                      blob_attach cesBLOBs default null);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesCLOBs);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesCLOBs);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesCLOBs);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesCLOBs);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesBLOBs);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesBLOBs);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesBLOBs);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesBLOBs);

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      filename1 varchar2 default null,
                      filetype1 varchar2 default 'text/plain',
                      filename2 varchar2 default null,
                      filetype2 varchar2 default 'text/plain',
                      filename3 varchar2 default null,
                      filetype3 varchar2 default 'text/plain');
                      
FUNCTION send_email (v_sendor      IN  VARCHAR2,
                      v_recipient 	IN  VARCHAR2,
                      v_cc          IN  VARCHAR2,
                      v_subject     IN  VARCHAR2,
                      v_message     IN  VARCHAR2)RETURN NUMBER;

end Tqc_Email_Pkg030215; 
 
/