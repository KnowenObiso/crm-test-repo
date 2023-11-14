--
-- TQC_EMAIL_PKG030215  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.Tqc_Email_Pkg030215 as
PROCEDURE RAISE_ERROR(v_msg IN VARCHAR2,v_err_no IN NUMBER:=NULL) IS
BEGIN
    IF v_err_no IS NULL THEN
        RAISE_APPLICATION_ERROR(-20037, v_msg||' - '||SQLERRM(SQLCODE));
    ELSE
        RAISE_APPLICATION_ERROR(v_err_no, v_msg||' - '||SQLERRM(SQLCODE));
    END IF;
END RAISE_ERROR;
PROCEDURE load_attchmnt_from_url (v_eatt_code IN NUMBER,p_url  IN  VARCHAR2) AS
  l_http_request   UTL_HTTP.req;
  l_http_response  UTL_HTTP.resp;
  l_blob           BLOB;
  l_raw            RAW(32767);
BEGIN
--raise_error(p_url);
  -- Initialize the BLOB.
  DBMS_LOB.createtemporary(l_blob, FALSE);

  -- Make a HTTP request and get the response.
  l_http_request  := UTL_HTTP.begin_request(p_url);
  l_http_response := UTL_HTTP.get_response(l_http_request);

  -- Copy the response into the BLOB.
  BEGIN
    LOOP
      UTL_HTTP.read_raw(l_http_response, l_raw, 32767);
      DBMS_LOB.writeappend (l_blob, UTL_RAW.length(l_raw), l_raw);
    END LOOP;
  EXCEPTION
    WHEN UTL_HTTP.end_of_body THEN
      UTL_HTTP.end_response(l_http_response);
  END;

  BEGIN
    UPDATE TQC_EMAIL_ATTACHMENTS SET EATT_FILE = l_blob WHERE EATT_CODE = v_eatt_code;
  EXCEPTION
    WHEN OTHERS THEN
        RAISE_ERROR('Error uploading email attachment');
  END;
  /*-- Insert the data into the table.
  INSERT INTO http_blob_test (id, url, data)
  VALUES (http_blob_test_seq.NEXTVAL, p_url, l_blob);
   */
  -- Relase the resources associated with the temporary LOB.
  DBMS_LOB.freetemporary(l_blob);
EXCEPTION
  WHEN OTHERS THEN
    UTL_HTTP.end_response(l_http_response);
    DBMS_LOB.freetemporary(l_blob);
    RAISE;
END load_attchmnt_from_url;
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
                        v_usr_code IN NUMBER) IS
                        
    v_email_addr varchar2(35);
    v_email_body varchar2(500);
	v_errmsg VARCHAR2(400);
    v_usr_type  varchar2(35);
    v_email_sender_addr varchar2(35);
       
BEGIN

    IF v_reciepient = 'C' THEN 
        BEGIN
        	SELECT CLNT_EMAIL_ADDRS
        	INTO v_email_addr
        	FROM TQC_CLIENTS
        	WHERE CLNT_CODE = v_clnt_code;
        EXCEPTION
        	WHEN OTHERS THEN 
        		RAISE_ERROR('Error client not defined..');
        END ;
        
        BEGIN
        SELECT param_value
        INTO v_email_sender_addr
        FROM TQC_PARAMETERS
        WHERE PARAM_NAME = 'EMAILS_FROM';
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_ERROR('Error Getting Defualt Email Sender ');
            WHEN OTHERS THEN
                RAISE_error('Error Getting Defualt Email Sender ');
        END;
        
    ELSIF v_reciepient = 'A' THEN 
        BEGIN
        	SELECT agn_email_address
        	INTO v_email_addr
        	FROM TQC_AGENCIES
        	WHERE AGN_CODE = v_agn_code;
        EXCEPTION
        	WHEN OTHERS THEN 
        		RAISE_ERROR('Error agent not defined..');
        END ;
     ELSIF v_reciepient = 'U' THEN
        BEGIN
        SELECT usr_type,usr_email
        INTO v_usr_type,v_email_sender_addr
        FROM TQC_USERS
        WHERE USR_CODE =v_usr_code;
        EXCEPTION
            WHEN OTHERS THEN
              v_usr_type :='U';
        END;

        IF v_usr_type = 'U' THEN 
        BEGIN
            SELECT USR_EMAIL
            INTO v_email_addr
            FROM TQC_USERS
            WHERE USR_CODE =v_usr_code;
        EXCEPTION
        WHEN OTHERS THEN
         RAISE_ERROR('v_usr_code='||v_usr_code);
        END;
        ELSE  -- HANDLE GROUP USERS
         NULL;
        END IF;
        BEGIN
        SELECT usr_email
        INTO v_email_sender_addr
        FROM TQC_USERS
        WHERE UPPER(USR_USERNAME) =UPPER(V_USER);
        EXCEPTION
            WHEN OTHERS THEN
              v_usr_type :='U';
        END;
      END IF;
    BEGIN
        SELECT MSGT_MSG
        INTO v_email_body
        FROM TQC_MSG_TEMPLATES
        WHERE MSGT_CODE = v_msgt_code;
    EXCEPTION
        WHEN OTHERS THEN 
            RAISE_ERROR('Template selected not found..');
    END;
   --RAISE_ERROR('v_usr_type='||v_email_addr||'='||v_email_sender_addr);
    BEGIN
		v_email_body :=  Tqc_Memos_Dbpkg.PROCESS_GIS_POL_MEMO(v_pol_code,
															v_clm_no,
															null,
															v_email_body,
															v_app_lvl);
    EXCEPTION
        WHEN OTHERS THEN 
            NULL;
    END;
    BEGIN
        SEND_EMAIL_MSG(v_clnt_code,
                        v_agn_code,
                        v_quot_code,
                        v_pol_code,
                        v_pol_no,
                        v_clm_no,
                        v_email_addr,
                        v_msg_subj,
                        v_email_body,
                        v_sys_code,
                        v_sys_mod,
                        v_files_tab,
                        v_email_code,
                        v_email_sender_addr);
    EXCEPTION
        WHEN OTHERS THEN 
            RAISE_ERROR('Error creating Email message');
    END;
        
END;
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
                        v_email_sender_addr IN VARCHAR2) IS
   v_user VARCHAR2(35):= Pkg_Global_Vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
   p_url VARCHAR2(250);
   v_web_server VARCHAR2(100);
   v_eatt_code number;
   --v_email_code NUMBER;
BEGIN
--RAISE_ERROR('testing');
    BEGIN
        SELECT PARAM_VALUE
        INTO v_web_server
        FROM TQC_PARAMETERS
        WHERE PARAM_NAME = 'WEB_SERVER';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_ERROR('Parameter for the Path '||'WEB_SERVER'||' for web server not specified in CRM');
        WHEN OTHERS THEN
            RAISE_error('Error getting Parameter for the Path '||'WEB_SERVER'||' for web server');
    END;
  SELECT TQC_SMS_CODE_SEQ.NEXTVAL INTO v_email_code FROM DUAL;
  INSERT INTO TQC_EMAIL_MESSAGES(EMAIL_CODE, EMAIL_SYS_CODE, EMAIL_SYS_MODULE, EMAIL_CLNT_CODE,
                                EMAIL_AGN_CODE, EMAIL_POL_CODE, EMAIL_POL_NO,
                                EMAIL_CLM_NO, EMAIL_QUOT_CODE,EMAIL_ADDRESS, EMAIL_SUBJ,EMAIL_MSG, EMAIL_STATUS,
                                EMAIL_PREPARED_BY, EMAIL_PREPARED_DATE,EMAIL_SENDER_ADDRES)
                      VALUES(v_email_code,v_sys_code,v_sys_mod,v_clnt_code,
                            v_agn_code, v_pol_code, v_pol_no,
                            v_clm_no, v_quot_code, v_email_addr,v_msg_subj,v_msg_text,'R',
                            v_user, SYSDATE,v_email_sender_addr);
  FOR x IN 1..v_files_tab.COUNT LOOP

    SELECT TQC_EATT_CODE_SEQ.NEXTVAL INTO v_eatt_code FROM DUAL;
    BEGIN
        INSERT INTO TQC_EMAIL_ATTACHMENTS(EATT_CODE, EATT_EMAIL_CODE, EATT_PATH, EATT_FILENAME)
        VALUES(v_eatt_code,v_email_code,v_files_tab(x).file_path,v_files_tab(x).file_name);
    EXCEPTION
        WHEN OTHERS THEN
            RAISE_ERROR('Error attaching email files..');
    END;
    BEGIN
        p_url :='/pdfdocs/'||REPLACE(v_files_tab(x).file_path,'\','/')||'/'||v_files_tab(x).file_name;
        --raise_error('url '||p_url);
        --p_url := v_web_server||'/pdfdocs/'||REPLACE(v_files_tab(x).file_path,'\','/')||'/'||v_files_tab(x).file_name;
         --p_url := 'D:\app\pdf\QUOTATIONS'||'\QUOTE2009314.PDF';--'/pdfdocs/'||REPLACE(v_files_tab(x).file_path,'\','/')||'/'||v_files_tab(x).file_name;
        load_attchmnt_from_url (v_eatt_code,p_url);
    EXCEPTION
        WHEN OTHERS THEN
            RAISE_ERROR('Error uploading file '||p_url);
    END;
  END LOOP;
END;
PROCEDURE send_mail(v_email_code IN NUMBER) IS
    v_body VARCHAR2(4000);
    v_html_body VARCHAR2(4000);
    v_cc    VARCHAR2(250);
    v_bcc   VARCHAR2(250);
    v_att_tab cesBLOBs := cesBLOBs();
    CURSOR cur_email IS SELECT * FROM TQC_EMAIL_MESSAGES WHERE EMAIL_CODE = v_email_code;
    CURSOR attfiles is SELECT * FROM TQC_EMAIL_ATTACHMENTS WHERE EATT_EMAIL_CODE =v_email_code;
    x number;
    v_email_from VARCHAR2(250);
BEGIN
    FOR cur_email_rec IN cur_email LOOP
        IF cur_email_rec.EMAIL_ADDRESS IS NULL THEN
            RAISE_ERROR('Please specify the Email Address to send to..');
        END IF;
        BEGIN
            SELECT PARAM_VALUE
            INTO v_email_from
            FROM TQC_PARAMETERS
            WHERE PARAM_NAME = 'EMAILS_FROM';
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_ERROR('EMAILS_FROM parameter not defined. Contact Turnkey Africa Limited...');
            WHEN OTHERS THEN
                RAISE_ERROR('Error getting email address from...');
        END;
        IF NVL(v_email_from,'NONE') = 'NONE' THEN
            RAISE_ERROR('Please define the email address from parameter [EMAILS_FROM]...');
        END IF;
        for cur_blob IN attfiles LOOP
            x:= NVL(x,0) + 1;
            v_att_tab.EXTEND;
            v_att_tab(x) := cesBLOB(cur_blob.EATT_FILE,cur_blob.EATT_FILENAME,'application/pdf');
        END LOOP;
--raise_error(v_email_from||'='|| cur_email_rec.EMAIL_ADDRESS||'='||
--cur_email_rec.EMAIL_SUBJ||'='||cur_email_rec.EMAIL_MSG);
        TQC_EMAIL_PKG.email_files(v_email_from,
                          cur_email_rec.EMAIL_ADDRESS,
                          cur_email_rec.EMAIL_SUBJ,
                          cur_email_rec.EMAIL_MSG,
                          v_html_body,--html_message VARCHAR2 DEFAULT NULL,
                         v_cc,--cc_names VARCHAR2 DEFAULT NULL,
                         v_Bcc,--bcc_names VARCHAR2 DEFAULT NULL,
                         v_att_tab);

        update TQC_EMAIL_MESSAGES 
        set EMAIL_status = 'S',
        EMAIL_SEND_DATE=TRUNC(SYSDATE)
         WHERE EMAIL_CODE = cur_email_rec.EMAIL_CODE;
    END LOOP;
end;

procedure do_email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2,
                      clob_message clob,
                      html_message varchar2,
                      clob_html_message clob,
                      cc_names varchar2,
                      bcc_names varchar2,
                      file_attach cesFiles,
                      clob_attach cesCLOBs,
                      blob_attach cesBLOBs)
is

   -- Change the SMTP host name and port number below to your own values,
   -- if not localhost on port 25:

   smtp_host          varchar2(256) := '10.176.18.56';
   smtp_port          number := 25;

   -- Change the boundary string, if needed, which demarcates boundaries of
   -- parts in a multi-part email, and should not appear inside the body of
   -- any part of the e-mail:

   boundary           constant varchar2(256) := 'CES.Boundary.DACA587499938898';

   recipients         varchar2(32767);
   directory_path     varchar2(256);
   file_path          varchar2(256);
   mime_type          varchar2(256);
   file_name          varchar2(256);
   cr                 varchar2(1) := chr(13);
   lf                 varchar2(1) := chr(10);
   crlf               varchar2(2) := cr || lf;
   mesg               varchar2(32767);
   conn               UTL_SMTP.CONNECTION;
   i                  binary_integer;
   my_code            number;
   my_errm            varchar2(32767);

   -- Function to return the next email address in the list of email addresses,
   -- separated by either a "," or a ";".  From Oracle's demo_mail.  The format
   -- of mailbox may be in one of these:
   --    someone@some-domain
   --    "Someone at some domain" <someone@some-domain>
   --    Someone at some domain <someone@some-domain>

   FUNCTION get_address(addr_list IN OUT VARCHAR2) RETURN VARCHAR2 IS

      addr VARCHAR2(256);
      i    pls_integer;

      FUNCTION lookup_unquoted_char(str  IN VARCHAR2,
                                    chrs IN VARCHAR2) RETURN pls_integer IS
         c            VARCHAR2(5);
         i            pls_integer;
         len          pls_integer;
         inside_quote BOOLEAN;

      BEGIN

         inside_quote := false;
         i := 1;
         len := length(str);
         WHILE (i <= len) LOOP
            c := substr(str, i, 1);
            IF (inside_quote) THEN
               IF (c = '"') THEN
                  inside_quote := false;
               ELSIF (c = '\') THEN
                  i := i + 1; -- Skip the quote character
               END IF;
               GOTO next_char;
            END IF;
            IF (c = '"') THEN
               inside_quote := true;
               GOTO next_char;
            END IF;
            IF (instr(chrs, c) >= 1) THEN
               RETURN i;
            END IF;
            <<next_char>>
            i := i + 1;
         END LOOP;
         RETURN 0;
      END;

   BEGIN
      BEGIN
        SELECT PARAM_VALUE
        INTO smtp_host
        FROM TQC_PARAMETERS
        WHERE PARAM_NAME = 'MAIL_SERVER';
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_ERROR('Parameter '||'MAIL_SERVER'||' for mail server not specified in CRM');
        WHEN OTHERS THEN
            RAISE_error('Error getting Parameter '||'MAIL_SERVER'||' for mail server');
      END;

      BEGIN
        SELECT PARAM_VALUE
        INTO smtp_port
        FROM TQC_PARAMETERS
        WHERE PARAM_NAME = 'MAIL_PORT';
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_ERROR('Parameter '||'MAIL_PORT'||' for mail server port not specified in CRM');
        WHEN OTHERS THEN
            RAISE_error('Error getting Parameter '||'MAIL_PORT'||' for mail server port');
      END;

      addr_list := ltrim(addr_list);
      i := lookup_unquoted_char(addr_list, ',;');
      IF (i >= 1) THEN
         addr := substr(addr_list, 1, i - 1);
         addr_list := substr(addr_list, i + 1);
      ELSE
         addr := addr_list;
         addr_list := '';
      END IF;
      i := lookup_unquoted_char(addr, '<');
      IF (i >= 1) THEN
         addr := substr(addr, i + 1);
         i := instr(addr, '>');
         IF (i >= 1) THEN
            addr := substr(addr, 1, i - 1);
         END IF;
      END IF;
      i := lookup_unquoted_char(addr, '@');
      IF (i = 0 and smtp_host != 'localhost') THEN
         i := instr(smtp_host, '.', -1, 2);
         addr := addr || '@' || substr(smtp_host, i + 1);
      END IF;
      addr := '<' || addr || '>';
      RETURN addr;
   END;

   -- Procedure to split a file pathname into its directory path and file name
   -- components.

   PROCEDURE split_path_name(file_path IN VARCHAR2, directory_path OUT VARCHAR2,
      file_name OUT VARCHAR2) IS

      pos number;

   begin

      -- Separate the filename from the directory name

      pos := instr(file_path,'/',-1);
      if pos = 0 then
         pos := instr(file_path,'\',-1);
      end if;
      if pos = 0 then
         directory_path := null;
      else
         directory_path := substr(file_path,1,pos - 1);
      end if;
      file_name := substr(file_path,pos + 1);

   end;

   -- Procedure to append the contents of a file, character LOB, or binary LOB
   -- to the e-mail

   PROCEDURE append_file(
      directory_path IN VARCHAR2 default null,
      file_name IN VARCHAR2 default null,
      mime_type IN VARCHAR2,
      conn IN OUT UTL_SMTP.CONNECTION,
      clob_attach IN CLOB default null,
      blob_attach IN BLOB default null) IS

      generated_name  varchar2(30) := 'CESDIR' || to_char(sysdate,'HH24MISS');
      directory_name  varchar2(256) := null;
      file_handle     utl_file.file_type;
      bfile_handle    bfile;
      lob_len         number(38) := 0;
      lob_pos         number(38) := 1;
      read_bytes      number(38);
      lf_at           number(38);
      line            varchar2(32767);
      data            raw(32767);
      my_code         number;
      my_errm         varchar2(32767);

   begin

      begin

         -- If this is a file to attach, grant access to the directory, unless
         -- already defined, and open the file (as a bfile for a binary file,
         -- otherwise as a text file)

         if directory_path is not null then
            begin
               line := directory_path;
               if instr(directory_path,':\') = 2 then
                 select dd.directory_name into directory_name from dba_directories dd
                    where lower(dd.directory_path) = lower(line) and rownum = 1;
               else
                 select dd.directory_name into directory_name from dba_directories dd
                    where dd.directory_path = line and rownum = 1;
               end if;
            exception
               when no_data_found then
                  directory_name := generated_name;
            end;
            if directory_name = generated_name then
               execute immediate 'create or replace directory ' || directory_name ||
                  ' as ''' || directory_path || '''';
               execute immediate 'grant read on directory ' || directory_name ||
                  ' to public';
            end if;
            if substr(mime_type,1,4) = 'text' then
              file_handle := utl_file.fopen(directory_name,file_name,'r');
            else
               bfile_handle := bfilename(directory_name,file_name);
               lob_len := dbms_lob.getlength(bfile_handle);
               dbms_lob.open(bfile_handle,dbms_lob.lob_readonly);
            end if;

         -- If this is a CLOB or BLOB to attach, just get the length of the LOB

         elsif clob_attach is not null then
            lob_len := dbms_lob.getlength(clob_attach);
         elsif blob_attach is not null then
            lob_len := dbms_lob.getlength(blob_attach);
         end if;

         -- Append the file's or LOB's contents to the end of the message

         loop

            -- If this is a text file, append the next line to the message,
            -- along with a carriage return / line feed

            if directory_path is not null and substr(mime_type,1,4) = 'text' then
               utl_file.get_line(file_handle,line);
               utl_smtp.write_data(conn,line || crlf);

            else

               -- If it is a character LOB, find the next line feed, get the
               -- the next line of text, and write it out, followed by a
               -- carriage return / line feed

               if clob_attach is not null then
                  lf_at := dbms_lob.instr(clob_attach,lf,lob_pos);
                  if lf_at = 0 then
                     lf_at := lob_len + 1;
                  end if;
                  read_bytes := lf_at - lob_pos;
                  if read_bytes > 0
                     and dbms_lob.substr(clob_attach,1,lf_at-1) = cr then
                     read_bytes := read_bytes - 1;
                  end if;
                  if read_bytes > 0 then
                     dbms_lob.read(clob_attach,read_bytes,lob_pos,line);
                     utl_smtp.write_data(conn,line);
                  end if;
                  utl_smtp.write_data(conn,crlf);
                  lob_pos := lf_at + 1;

               -- If it is a binary file or binary LOB, process it 57 bytes
               -- at a time, reading them in with a LOB read, encoding them
               -- in BASE64, and writing out the encoded binary string as raw
               -- data

               else
                  if lob_pos + 57 - 1 > lob_len then
                     read_bytes := lob_len - lob_pos + 1;
                  else
                     read_bytes := 57;
                  end if;
                  if blob_attach is not null then
                     dbms_lob.read(blob_attach,read_bytes,lob_pos,data);
                  else
                     dbms_lob.read(bfile_handle,read_bytes,lob_pos,data);
                  end if;
                  utl_smtp.write_raw_data(conn,utl_encode.base64_encode(data));
                  lob_pos := lob_pos + read_bytes;
               end if;

               -- Exit if we've processed all of the LOB or binary file

               if lob_pos > lob_len then
                  exit;
               end if;

            end if;

         end loop;

      -- Output any errors, except at end when no more data is found

      exception
         when no_data_found then
            null;
         when others then
            my_code := SQLCODE;
            my_errm := SQLERRM;
            raise_application_error(-20000,
               'Failed to send mail: Error code ' || my_code || ': ' || my_errm);
      end;

      -- Close the file (binary or text) and drop the generated directory,
      -- if any

      if directory_path is not null then
         if substr(mime_type,1,4) != 'text' then
            dbms_lob.close(bfile_handle);
         else
            utl_file.fclose(file_handle);
         end if;
      end if;
      if directory_name = generated_name then
         execute immediate 'drop directory ' || directory_name;
      end if;

   end;

begin

   -- Open the SMTP connection and set the From and To e-mail addresses

   conn := utl_smtp.open_connection(smtp_host,smtp_port);
   utl_smtp.helo(conn,smtp_host);
   recipients := from_name;
   utl_smtp.mail(conn,get_address(recipients));
   recipients := to_names;
   while recipients is not null loop
      utl_smtp.rcpt(conn,get_address(recipients));
   end loop;
   if cc_names is not null and length(cc_names) > 0 then
      recipients := cc_names;
      while recipients is not null loop
         utl_smtp.rcpt(conn,get_address(recipients));
      end loop;
   end if;
   if bcc_names is not null and length(bcc_names) > 0 then
      recipients := bcc_names;
      while recipients is not null loop
         utl_smtp.rcpt(conn,get_address(recipients));
      end loop;
   end if;
   utl_smtp.open_data(conn);

   -- Build the start of the mail message

   mesg := 'Date: ' || TO_CHAR(SYSDATE,'dd Mon yy hh24:mi:ss') || crlf ||
      'From: ' || from_name || crlf ||
      'Subject: ' || subject || crlf ||
      'To: ' || to_names || crlf;
   if cc_names is not null and length(cc_names) > 0 then
      mesg := mesg || 'Cc: ' || cc_names || crlf;
   end if;
   if bcc_names is not null and length(bcc_names) > 0 then
      mesg := mesg || 'Bcc: ' || bcc_names || crlf;
   end if;
   mesg := mesg || 'Mime-Version: 1.0' || crlf ||
      'Content-Type: multipart/mixed; boundary="' || boundary || '"' ||
      crlf || crlf ||
      'This is a Mime message, which your current mail reader may not' || crlf ||
      'understand. Parts of the message will appear as text. If the remainder' || crlf ||
      'appears as random characters in the message body, instead of as' || crlf ||
      'attachments, then you''ll have to extract these parts and decode them' || crlf ||
      'manually.' || crlf || crlf;
   utl_smtp.write_data(conn,mesg);

   -- Write the text message or message file or message CLOB, if any

   if (message is not null and length(message) > 0) or
      clob_message is not null then
      mesg := '--' || boundary || crlf ||
         'Content-Type: text/plain; name="message.txt"; charset=US-ASCII' ||
          crlf ||
         'Content-Disposition: inline; filename="message.txt"' || crlf ||
         'Content-Transfer-Encoding: 7bit' || crlf || crlf;
      utl_smtp.write_data(conn,mesg);
      if instr(message,'/') = 1 or instr(message,':\') = 2 or
         instr(message,'\\') = 1 then
         split_path_name(message,directory_path,file_name);
         append_file(directory_path,file_name,'text',conn);
         utl_smtp.write_data(conn,crlf);
      elsif message is not null and length(message) > 0 then
         utl_smtp.write_data(conn,message);
         if length(message) = 1 or
            substr(message,length(message)-1) != crlf then
            utl_smtp.write_data(conn,crlf);
         end if;
      elsif clob_message is not null then
         append_file(null,'message.txt','text/plain',conn,clob_message);
      end if;

   end if;

   -- Write the HTML message or message file or message CLOB, if any

   if (html_message is not null and length(html_message) > 0) or
      clob_html_message is not null then
      mesg := '--' || boundary || crlf ||
         'Content-Type: text/html; name="message.html"; charset=US-ASCII' ||
         crlf ||
         'Content-Disposition: inline; filename="message.html"' || crlf ||
         'Content-Transfer-Encoding: 7bit' || crlf || crlf;
      utl_smtp.write_data(conn,mesg);
      if instr(html_message,'/') = 1 or instr(html_message,':\') = 2 or
         instr(html_message,'\\') = 1 then
         split_path_name(html_message,directory_path,file_name);
         append_file(directory_path,file_name,'text',conn);
         utl_smtp.write_data(conn,crlf);
      elsif html_message is not null and length(html_message) > 0 then
         utl_smtp.write_data(conn,html_message);
         if length(html_message) = 1 or
            substr(html_message,length(html_message)-1) != crlf then
            utl_smtp.write_data(conn,crlf);
         end if;
      elsif clob_html_message is not null then
         append_file(null,'message.html','text/html',conn,clob_html_message);
      end if;
   end if;

   -- Attach the files, if any

   if file_attach is not null then

      for i in 1 .. file_attach.count loop
         file_path := null;
         mime_type := null;

         -- If this is a file path parameter, get the file path and check the
         -- next parameter to see if it is a file type parameter (else default
         -- to text/plain).

         if file_attach(i) is null or length(file_attach(i)) = 0 then
            exit;
         end if;
         if instr(file_attach(i),'/') = 1 or instr(file_attach(i),':\') = 2 or
            instr(file_attach(i),'\\') = 1 then
            file_path := file_attach(i);
            if i = file_attach.count then
               mime_type := 'text/plain';
            else
               if instr(file_attach(i+1),'/') > 1 and instr(file_attach(i+1),'/',1,2) = 0 then
                  mime_type := file_attach(i+1);
               else
                  mime_type := 'text/plain';
               end if;
            end if;
         end if;

         -- If this is a file path parameter ...

         if file_path is not null then

            split_path_name(file_path,directory_path,file_name);

            -- Generate the MIME boundary line according to the file (mime) type
            -- specified.

            mesg := crlf || '--' || boundary || crlf;
            if substr(mime_type,1,4) != 'text' then
               mesg := mesg || 'Content-Type: ' || mime_type ||
                  '; name="' || file_name || '"' || crlf ||
                  'Content-Disposition: attachment; filename="' ||
                  file_name || '"' || crlf ||
                  'Content-Transfer-Encoding: base64' || crlf || crlf ;
            else
               mesg := mesg || 'Content-Type: application/octet-stream; name="' ||
                  file_name || '"' || crlf ||
                  'Content-Disposition: attachment; filename="' ||
                  file_name || '"' || crlf ||
                  'Content-Transfer-Encoding: 7bit' || crlf || crlf ;
            end if;
            utl_smtp.write_data(conn,mesg);

            -- Append the file contents to the end of the message

            append_file(directory_path,file_name,mime_type,conn);

         end if;
      end loop;
   end if;

   -- Attach the character LOB's, if any

   if clob_attach is not null then
      for i in 1 .. clob_attach.count loop

         -- Get the name and mime type, if given, else use default values

         if clob_attach(i).vclob is null then
            exit;
         end if;
         file_name := clob_attach(i).fileName;
         if file_name is null then
            file_name := 'clob' || i || '.txt';
         end if;
         mime_type := clob_attach(i).mimeType;
         if mime_type is null then
            mime_type := 'text/plain';
         end if;

         -- Generate the MIME boundary line for this character file attachment

         mesg := crlf || '--' || boundary || crlf;
         mesg := mesg || 'Content-Type: application/octet-stream; name="' ||
            file_name || '"' || crlf ||
            'Content-Disposition: attachment; filename="' ||
            file_name || '"' || crlf ||
            'Content-Transfer-Encoding: 7bit' || crlf || crlf ;
         utl_smtp.write_data(conn,mesg);

         -- Append the CLOB contents to the end of the message

         append_file(null,file_name,mime_type,conn,clob_attach=>clob_attach(i).vclob);

      end loop;
   end if;

   -- Attach the binary LOB's, if any

   if blob_attach is not null then
      for i in 1 .. blob_attach.count loop

         -- Get the name and mime type, if given, else use default values

         if blob_attach(i).vblob is null then
            exit;
         end if;
         file_name := blob_attach(i).fileName;
         if file_name is null or length(file_name) = 0 then
            file_name := 'blob' || i;
         end if;
         mime_type := blob_attach(i).mimeType;
         if mime_type is null then
            mime_type := 'text/plain';  -- but, this is a strange default!
         end if;

         -- Generate the MIME boundary line for this BASE64 binary attachment

         mesg := crlf || '--' || boundary || crlf;
         mesg := mesg || 'Content-Type: ' || mime_type ||
            '; name="' || file_name || '"' || crlf ||
            'Content-Disposition: attachment; filename="' ||
            file_name || '"' || crlf ||
            'Content-Transfer-Encoding: base64' || crlf || crlf ;
         utl_smtp.write_data(conn,mesg);

         -- Append the BLOB contents to the end of the message

         append_file(null,file_name,mime_type,conn,blob_attach=>blob_attach(i).vblob);

      end loop;
   end if;

   -- Append the final boundary line

   mesg := crlf || '--' || boundary || '--' || crlf;
   utl_smtp.write_data(conn,mesg);

   -- Close the SMTP connection

   utl_smtp.close_data(conn);
   utl_smtp.quit(conn);

exception
   when utl_smtp.transient_error or utl_smtp.permanent_error then
      my_code := SQLCODE;
      my_errm := SQLERRM;
      begin
         utl_smtp.quit(conn);
      exception
         when utl_smtp.transient_error or utl_smtp.permanent_error then
            null;
      end;
      raise_application_error(-20000,
         'Failed to send mail - SMTP server down or unavailable: Error code ' ||
            my_code || ': ' || my_errm);
   when others then
      my_code := SQLCODE;
      my_errm := SQLERRM;
      raise_application_error(-20000,
         'Failed to send mail: Error code ' || my_code || ': ' || my_errm);

end;

-- Define the various overloaded definitions (interfaces) to email_files

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesFiles,
                      clob_attach cesCLOBs default null,
                      blob_attach cesBLOBs default null)
is
begin
   do_email_files(from_name,to_names,subject,message,null,html_message,null,
      cc_names,bcc_names,attach,clob_attach,blob_attach);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesFiles,
                      clob_attach cesCLOBs default null,
                      blob_attach cesBLOBs default null)
is
begin
   do_email_files(from_name,to_names,subject,null,message,html_message,null,
      cc_names,bcc_names,attach,clob_attach,blob_attach);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesFiles,
                      clob_attach cesCLOBs default null,
                      blob_attach cesBLOBs default null)
is
begin
   do_email_files(from_name,to_names,subject,message,null,null,html_message,
      cc_names,bcc_names,attach,clob_attach,blob_attach);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesFiles,
                      clob_attach cesCLOBs default null,
                      blob_attach cesBLOBs default null)
is
begin
   do_email_files(from_name,to_names,subject,null,message,null,html_message,
      cc_names,bcc_names,attach,clob_attach,blob_attach);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesCLOBs)
is
begin
   do_email_files(from_name,to_names,subject,message,null,html_message,null,
      cc_names,bcc_names,cesFiles(null),attach,null);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesCLOBs)
is
begin
   do_email_files(from_name,to_names,subject,null,message,html_message,null,
      cc_names,bcc_names,cesFiles(null),attach,null);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesCLOBs)
is
begin
   do_email_files(from_name,to_names,subject,message,null,null,html_message,
      cc_names,bcc_names,cesFiles(null),attach,null);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesCLOBs)
is
begin
   do_email_files(from_name,to_names,subject,null,message,null,html_message,
      cc_names,bcc_names,cesFiles(null),attach,null);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesBLOBs)
is
begin
   do_email_files(from_name,to_names,subject,message,null,html_message,null,
      cc_names,bcc_names,cesFiles(null),null,attach);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message varchar2 default '',
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesBLOBs)
is
begin
   do_email_files(from_name,to_names,subject,null,message,html_message,null,
      cc_names,bcc_names,cesFiles(null),null,attach);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message varchar2 default '',
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesBLOBs)
is
begin
   do_email_files(from_name,to_names,subject,message,null,null,html_message,
      cc_names,bcc_names,cesFiles(null),null,attach);
end;

procedure email_files(from_name varchar2,
                      to_names varchar2,
                      subject varchar2,
                      message clob,
                      html_message clob,
                      cc_names varchar2 default null,
                      bcc_names varchar2 default null,
                      attach cesBLOBs)
is
begin
   do_email_files(from_name,to_names,subject,null,message,null,html_message,
      cc_names,bcc_names,cesFiles(null),null,attach);
end;

-- This overloaded version supports legacy code using the "filename/filetype"
-- parameter pairs instead of the current "attach" parameters.  It is also used
-- when no file attachments are specified (since there is not a default value
-- for the "attach" parameters in the interfaces above).

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
                      filetype3 varchar2 default 'text/plain')
is
begin
   do_email_files(from_name,to_names,subject,message,null,html_message,null,
      cc_names,bcc_names,cesFiles(filename1,filetype1,filename2,filetype2,
      filename3,filetype3),null,null);
end;
 
 FUNCTION send_email (v_sendor      IN  VARCHAR2,
                      v_recipient 	IN  VARCHAR2,
                      v_cc          IN  VARCHAR2,
                      v_subject     IN  VARCHAR2,
                      v_message     IN  VARCHAR2)RETURN NUMBER IS
    v_mailhost VARCHAR2(200);
    v_status_desc VARCHAR2(200);
    v_sendorEmail       TQC_PARAMETERS.PARAM_VALUE%TYPE;
    BEGIN
			SELECT PARAM_VALUE INTO v_mailhost FROM TQC_PARAMETERS WHERE PARAM_NAME = 'MAILHOST';
            IF v_sendor IS NULL THEN
            SELECT PARAM_VALUE INTO v_sendorEmail FROM TQC_PARAMETERS WHERE PARAM_NAME = 'EMAILS_FROM';
                sendmail (v_sendorEmail,v_recipient,
							v_subject,
							v_message,
							v_mailhost,                            
                            v_cc);
                RETURN (1);
            
			ELSIF v_sendor IS NOT NULL AND v_recipient IS NOT NULL THEN
				sendmail (v_sendor,v_recipient,
							v_subject,
							v_message,
							v_mailhost,                            
                            v_cc);
                RETURN (1);
			END IF;
    EXCEPTION
		WHEN OTHERS THEN
				RAISE_ERROR('Error sending incident email to '||v_recipient);
    RETURN (1);
	END;

end Tqc_Email_Pkg030215; 
/