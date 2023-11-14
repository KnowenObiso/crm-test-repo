--
-- TQC_EMAIL_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_email_pkg
AS
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20037,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   PROCEDURE load_attchmnt_from_url (v_eatt_code IN NUMBER, p_url IN VARCHAR2)
   AS
      l_http_request    UTL_HTTP.req;
      l_http_response   UTL_HTTP.resp;
      l_blob            BLOB;
      l_raw             RAW (32767);
   BEGIN
--raise_error(p_url);
  -- Initialize the BLOB.
      DBMS_LOB.createtemporary (l_blob, FALSE);
      -- Make a HTTP request and get the response.
      l_http_request := UTL_HTTP.begin_request (p_url);
      l_http_response := UTL_HTTP.get_response (l_http_request);

      -- Copy the response into the BLOB.
      BEGIN
         LOOP
            UTL_HTTP.read_raw (l_http_response, l_raw, 32767);
            DBMS_LOB.writeappend (l_blob, UTL_RAW.LENGTH (l_raw), l_raw);
         END LOOP;
      EXCEPTION
         WHEN UTL_HTTP.end_of_body
         THEN
            UTL_HTTP.end_response (l_http_response);
      END;

      BEGIN
         UPDATE tqc_email_attachments
            SET eatt_file = l_blob
          WHERE eatt_code = v_eatt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error uploading email attachment');
      END;

      /*-- Insert the data into the table.
      INSERT INTO http_blob_test (id, url, data)
      VALUES (http_blob_test_seq.NEXTVAL, p_url, l_blob);
       */
      -- Relase the resources associated with the temporary LOB.
      DBMS_LOB.freetemporary (l_blob);
   EXCEPTION
      WHEN OTHERS
      THEN
         UTL_HTTP.end_response (l_http_response);
         DBMS_LOB.freetemporary (l_blob);
         RAISE;
   END load_attchmnt_from_url;

   PROCEDURE create_email_msg (
      v_reciepient     IN       VARCHAR2,
      v_clnt_code      IN       NUMBER,
      v_agn_code       IN       NUMBER,
      v_quot_code      IN       NUMBER,
      v_quot_no        IN       VARCHAR2,
      v_pol_code       IN       NUMBER,
      v_pol_no         IN       VARCHAR2,
      v_clm_no         IN       VARCHAR2,
      v_msg_subj       IN       VARCHAR2,
      v_msgt_code      IN       NUMBER,
      v_files_tab      IN       email_files_tab,
      v_sys_code       IN       NUMBER,
      v_sys_mod        IN       VARCHAR2,
      v_user           IN       VARCHAR2,
      v_email_code     IN OUT   NUMBER,
      v_app_lvl        IN       VARCHAR2,
      v_usr_code       IN       NUMBER,
      v_to_send_date   IN       DATE
   )
   IS
      v_email_addr          VARCHAR2 (35);
      v_email_body          VARCHAR2 (500);
      v_errmsg              VARCHAR2 (400);
      v_usr_type            VARCHAR2 (35);
      v_email_sender_addr   VARCHAR2 (35);
   BEGIN
      IF v_reciepient = 'C'
      THEN
         BEGIN
            SELECT clnt_email_addrs
              INTO v_email_addr
              FROM tqc_clients
             WHERE clnt_code = v_clnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error client not defined..');
         END;

         BEGIN
            SELECT param_value
              INTO v_email_sender_addr
              FROM tqc_parameters
             WHERE param_name = 'EMAILS_FROM';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error ('Error Getting Defualt Email Sender ');
            WHEN OTHERS
            THEN
               raise_error ('Error Getting Defualt Email Sender ');
         END;
      ELSIF v_reciepient = 'A'
      THEN
         BEGIN
            SELECT agn_email_address
              INTO v_email_addr
              FROM tqc_agencies
             WHERE agn_code = v_agn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error agent not defined..');
         END;
      ELSIF v_reciepient = 'U'
      THEN
         BEGIN
            SELECT usr_type, usr_email
              INTO v_usr_type, v_email_sender_addr
              FROM tqc_users
             WHERE usr_code = v_usr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_usr_type := 'U';
         END;

--raise_Error(v_usr_type||'='||v_usr_code);
         IF v_usr_type = 'U'
         THEN
            BEGIN
               SELECT usr_email
                 INTO v_email_addr
                 FROM tqc_users
                WHERE usr_code = v_usr_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error Getting User..' || v_usr_code);
            END;
         ELSE                                            -- HANDLE GROUP USERS
            NULL;
         END IF;

         BEGIN
            SELECT usr_email
              INTO v_email_sender_addr
              FROM tqc_users
             WHERE UPPER (usr_username) = UPPER (v_user);
         EXCEPTION
            WHEN OTHERS
            THEN
               v_usr_type := 'U';
         END;
      ELSIF v_reciepient = 'DR'
      THEN
         BEGIN
            SELECT sla_email
              INTO v_email_addr
              FROM TQ_FMS.FMS_SL_ACCOUNTS
             WHERE SLA_CODE = v_agn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Getting Debtor email..');
         END;   
      END IF;

      BEGIN
         SELECT msgt_msg
           INTO v_email_body
           FROM tqc_msg_templates
          WHERE msgt_code = v_msgt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Template selected not found..');
      END;

      --RAISE_ERROR('v_usr_type='||v_email_addr||'='||v_email_sender_addr);
      BEGIN
         v_email_body :=
            tqc_memos_dbpkg.process_gis_pol_memo (v_pol_code,
                                                  v_clm_no,
                                                  NULL,
                                                  v_email_body,
                                                  v_app_lvl
                                                 );
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      BEGIN
         send_email_msg (v_clnt_code,
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
                         v_email_sender_addr,
                         v_to_send_date
                        );
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error creating Email message');
      END;
   END;

   PROCEDURE send_email_msg (
      v_clnt_code           IN       NUMBER,
      v_agn_code            IN       NUMBER,
      v_quot_code           IN       NUMBER,
      v_pol_code            IN       NUMBER,
      v_pol_no              IN       VARCHAR2,
      v_clm_no              IN       VARCHAR2,
      v_email_addr          IN       VARCHAR2,
      v_msg_subj            IN       VARCHAR2,
      v_msg_text            IN       VARCHAR2,
      v_sys_code            IN       NUMBER,
      v_sys_mod             IN       VARCHAR2,
      v_files_tab           IN       email_files_tab,
      v_email_code          OUT      NUMBER,
      v_email_sender_addr   IN       VARCHAR2,
      v_to_send_date        IN       DATE
   )
   IS
      v_user         VARCHAR2 (35)
            := pkg_global_vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
      p_url          VARCHAR2 (250);
      v_web_server   VARCHAR2 (100);
      v_eatt_code    NUMBER;
   --v_email_code NUMBER;
   BEGIN
--RAISE_ERROR('testing');
      BEGIN
         SELECT param_value
           INTO v_web_server
           FROM tqc_parameters
          WHERE param_name = 'WEB_SERVER';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error (   'Parameter for the Path '
                         || 'WEB_SERVER'
                         || ' for web server not specified in CRM'
                        );
         WHEN OTHERS
         THEN
            raise_error (   'Error getting Parameter for the Path '
                         || 'WEB_SERVER'
                         || ' for web server'
                        );
      END;

      SELECT tqc_sms_code_seq.NEXTVAL
        INTO v_email_code
        FROM DUAL;

      INSERT INTO tqc_email_messages
                  (email_code, email_sys_code, email_sys_module,
                   email_clnt_code, email_agn_code, email_pol_code,
                   email_pol_no, email_clm_no, email_quot_code,
                   email_address, email_subj, email_msg, email_status,
                   email_prepared_by, email_prepared_date, email_sender_addres
                  )
           VALUES (v_email_code, v_sys_code, v_sys_mod,
                   v_clnt_code, v_agn_code, v_pol_code,
                   v_pol_no, v_clm_no, v_quot_code,
                   v_email_addr, v_msg_subj, v_msg_text, 'R',
                   v_user, SYSDATE, v_email_sender_addr
                  );

      FOR x IN 1 .. v_files_tab.COUNT
      LOOP
         SELECT tqc_eatt_code_seq.NEXTVAL
           INTO v_eatt_code
           FROM DUAL;

         BEGIN
            INSERT INTO tqc_email_attachments
                        (eatt_code, eatt_email_code,
                         eatt_path, eatt_filename
                        )
                 VALUES (v_eatt_code, v_email_code,
                         v_files_tab (x).file_path, v_files_tab (x).file_name
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error attaching email files..');
         END;

         BEGIN
            p_url :=
                  '/pdfdocs/'
               || REPLACE (v_files_tab (x).file_path, '\', '/')
               || '/'
               || v_files_tab (x).file_name;
            --raise_error('url '||p_url);
            --p_url := v_web_server||'/pdfdocs/'||REPLACE(v_files_tab(x).file_path,'\','/')||'/'||v_files_tab(x).file_name;
             --p_url := 'D:\app\pdf\QUOTATIONS'||'\QUOTE2009314.PDF';--'/pdfdocs/'||REPLACE(v_files_tab(x).file_path,'\','/')||'/'||v_files_tab(x).file_name;
            load_attchmnt_from_url (v_eatt_code, p_url);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error uploading file ' || p_url);
         END;
      END LOOP;
   END;

   PROCEDURE send_mail (v_email_code IN NUMBER)
   IS
      v_body         VARCHAR2 (4000);
      v_html_body    VARCHAR2 (4000);
      v_cc           VARCHAR2 (250);
      v_bcc          VARCHAR2 (250);
      v_att_tab      cesblobs        := cesblobs ();

      CURSOR cur_email
      IS
         SELECT *
           FROM tqc_email_messages
          WHERE email_code = v_email_code;

      CURSOR attfiles
      IS
         SELECT *
           FROM tqc_email_attachments
          WHERE eatt_email_code = v_email_code;

      x              NUMBER;
      v_email_from   VARCHAR2 (250);
   BEGIN
      FOR cur_email_rec IN cur_email
      LOOP
         IF cur_email_rec.email_address IS NULL
         THEN
            raise_error ('Please specify the Email Address to send to..');
         END IF;

         BEGIN
            SELECT param_value
              INTO v_email_from
              FROM tqc_parameters
             WHERE param_name = 'EMAILS_FROM';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error
                  ('EMAILS_FROM parameter not defined. Contact Turnkey Africa Limited...'
                  );
            WHEN OTHERS
            THEN
               raise_error ('Error getting email address from...');
         END;

         IF NVL (v_email_from, 'NONE') = 'NONE'
         THEN
            raise_error
               ('Please define the email address from parameter [EMAILS_FROM]...'
               );
         END IF;

         FOR cur_blob IN attfiles
         LOOP
            x := NVL (x, 0) + 1;
            v_att_tab.EXTEND;
            v_att_tab (x) :=
               cesblob (cur_blob.eatt_file,
                        cur_blob.eatt_filename,
                        'application/pdf'
                       );
         END LOOP;

--raise_error(v_email_from||'='|| cur_email_rec.EMAIL_ADDRESS||'='||
--cur_email_rec.EMAIL_SUBJ||'='||cur_email_rec.EMAIL_MSG);
         tqc_email_pkg.email_files
                            (v_email_from,
                             cur_email_rec.email_address,
                             cur_email_rec.email_subj,
                             cur_email_rec.email_msg,
                             v_html_body,
                                         --html_message VARCHAR2 DEFAULT NULL,
                             v_cc,           --cc_names VARCHAR2 DEFAULT NULL,
                             v_bcc,         --bcc_names VARCHAR2 DEFAULT NULL,
                             v_att_tab
                            );

         UPDATE tqc_email_messages
            SET email_status = 'S',
                email_send_date = TRUNC (SYSDATE)
          WHERE email_code = cur_email_rec.email_code;
      END LOOP;
   END;
   
   PROCEDURE send_serv_req_mail (v_email_code IN NUMBER)
   IS
      v_body         VARCHAR2 (4000);
      v_html_body    VARCHAR2 (4000);
      v_cc           VARCHAR2 (250);
      v_bcc          VARCHAR2 (250);
      v_att_tab      cesblobs        := cesblobs ();

      CURSOR cur_email
      IS
         SELECT *
           FROM tqc_email_messages
          WHERE email_code = v_email_code;

      CURSOR attfiles
      IS
         SELECT *
           FROM tqc_email_attachments
          WHERE eatt_email_code = v_email_code;

      x              NUMBER;
      v_email_from   VARCHAR2 (250);
   BEGIN
      FOR cur_email_rec IN cur_email
      LOOP
         IF cur_email_rec.email_address IS NULL
         THEN
            raise_error ('Please specify the Email Address to send to..');
         END IF;

         BEGIN
            SELECT param_value
              INTO v_email_from
              FROM tqc_parameters
             WHERE param_name = 'SERV_REQ_EMAILS_FROM';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error
                  ('EMAILS_FROM parameter not defined. Contact Turnkey Africa Limited...'
                  );
            WHEN OTHERS
            THEN
               raise_error ('Error getting email address from...');
         END;

         IF NVL (v_email_from, 'NONE') = 'NONE'
         THEN
            raise_error
               ('Please define the email address from parameter [EMAILS_FROM]...'
               );
         END IF;

         FOR cur_blob IN attfiles
         LOOP
            x := NVL (x, 0) + 1;
            v_att_tab.EXTEND;
            v_att_tab (x) :=
               cesblob (cur_blob.eatt_file,
                        cur_blob.eatt_filename,
                        'application/pdf'
                       );
         END LOOP;
         
        COMMIT;
--     raise_error(
--     ' v_email_from: '||v_email_from||
--     ' email_address: '|| cur_email_rec.email_address||
--     ' email_subj: '||cur_email_rec.email_subj||
--     ' email_msg: '||cur_email_rec.email_msg
--     );
         tqc_email_pkg.email_files
                            (v_email_from,
                             cur_email_rec.email_address,
                             cur_email_rec.email_subj,
                             cur_email_rec.email_msg,
                             v_html_body,
                                         --html_message VARCHAR2 DEFAULT NULL,
                             v_cc,           --cc_names VARCHAR2 DEFAULT NULL,
                             v_bcc,         --bcc_names VARCHAR2 DEFAULT NULL,
                             v_att_tab
                            );

         UPDATE tqc_email_messages
            SET email_status = 'S',
                email_send_date = TRUNC (SYSDATE)
          WHERE email_code = cur_email_rec.email_code;
      END LOOP;
   END;

   PROCEDURE do_email_files (
      from_name           VARCHAR2,
      to_names            VARCHAR2,
      subject             VARCHAR2,
      MESSAGE             VARCHAR2,
      clob_message        CLOB,
      html_message        VARCHAR2,
      clob_html_message   CLOB,
      cc_names            VARCHAR2,
      bcc_names           VARCHAR2,
      file_attach         cesfiles,
      clob_attach         cesclobs,
      blob_attach         cesblobs
   )
   IS
      -- Change the SMTP host name and port number below to your own values,
      -- if not localhost on port 25:
      smtp_host           VARCHAR2 (256)
                      := tqc_parameters_pkg.get_param_varchar ('MAIL_SERVER');
                                              --'localhost';--'10.176.18.56';
      smtp_port           NUMBER
                         := tqc_parameters_pkg.get_param_number ('MAIL_PORT');
                                                                        --25;
      -- Change the boundary string, if needed, which demarcates boundaries of
      -- parts in a multi-part email, and should not appear inside the body of
      -- any part of the e-mail:
      boundary   CONSTANT VARCHAR2 (256)   := 'CES.Boundary.DACA587499938898';
      recipients          VARCHAR2 (32767);
      directory_path      VARCHAR2 (256);
      file_path           VARCHAR2 (256);
      mime_type           VARCHAR2 (256);
      file_name           VARCHAR2 (256);
      cr                  VARCHAR2 (1)        := CHR (13);
      lf                  VARCHAR2 (1)        := CHR (10);
      crlf                VARCHAR2 (2)        := cr || lf;
      mesg                VARCHAR2 (32767);
      conn                UTL_SMTP.connection;
      i                   BINARY_INTEGER;
      my_code             NUMBER;
      my_errm             VARCHAR2 (32767);

      -- Function to return the next email address in the list of email addresses,
      -- separated by either a "," or a ";".  From Oracle's demo_mail.  The format
      -- of mailbox may be in one of these:
      --    someone@some-domain
      --    "Someone at some domain" <someone@some-domain>
      --    Someone at some domain <someone@some-domain>
      FUNCTION get_address (addr_list IN OUT VARCHAR2)
         RETURN VARCHAR2
      IS
         addr   VARCHAR2 (256);
         i      PLS_INTEGER;

         FUNCTION lookup_unquoted_char (str IN VARCHAR2, chrs IN VARCHAR2)
            RETURN PLS_INTEGER
         IS
            c              VARCHAR2 (5);
            i              PLS_INTEGER;
            len            PLS_INTEGER;
            inside_quote   BOOLEAN;
         BEGIN
            inside_quote := FALSE;
            i := 1;
            len := LENGTH (str);

            WHILE (i <= len)
            LOOP
               c := SUBSTR (str, i, 1);

               IF (inside_quote)
               THEN
                  IF (c = '"')
                  THEN
                     inside_quote := FALSE;
                  ELSIF (c = '\')
                  THEN
                     i := i + 1;                  -- Skip the quote character
                  END IF;

                  GOTO next_char;
               END IF;

               IF (c = '"')
               THEN
                  inside_quote := TRUE;
                  GOTO next_char;
               END IF;

               IF (INSTR (chrs, c) >= 1)
               THEN
                  RETURN i;
               END IF;

               <<next_char>>
               i := i + 1;
            END LOOP;

            RETURN 0;
         END;
      BEGIN
         BEGIN
            SELECT param_value
              INTO smtp_host
              FROM tqc_parameters
             WHERE param_name = 'MAIL_SERVER';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error (   'Parameter '
                            || 'MAIL_SERVER'
                            || ' for mail server not specified in CRM'
                           );
            WHEN OTHERS
            THEN
               raise_error (   'Error getting Parameter '
                            || 'MAIL_SERVER'
                            || ' for mail server'
                           );
         END;

         BEGIN
            SELECT param_value
              INTO smtp_port
              FROM tqc_parameters
             WHERE param_name = 'MAIL_PORT';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error (   'Parameter '
                            || 'MAIL_PORT'
                            || ' for mail server port not specified in CRM'
                           );
            WHEN OTHERS
            THEN
               raise_error (   'Error getting Parameter '
                            || 'MAIL_PORT'
                            || ' for mail server port'
                           );
         END;

         addr_list := LTRIM (addr_list);
         i := lookup_unquoted_char (addr_list, ',;');

         IF (i >= 1)
         THEN
            addr := SUBSTR (addr_list, 1, i - 1);
            addr_list := SUBSTR (addr_list, i + 1);
         ELSE
            addr := addr_list;
            addr_list := '';
         END IF;

         i := lookup_unquoted_char (addr, '<');

         IF (i >= 1)
         THEN
            addr := SUBSTR (addr, i + 1);
            i := INSTR (addr, '>');

            IF (i >= 1)
            THEN
               addr := SUBSTR (addr, 1, i - 1);
            END IF;
         END IF;

         i := lookup_unquoted_char (addr, '@');

         --raise_error(i||';'||smtp_host);
         IF (i = 0 AND smtp_host != 'localhost')
         THEN
            i := INSTR (smtp_host, '.', -1, 2);
            addr := addr || '@' || SUBSTR (smtp_host, i + 1);
         END IF;

         addr := '<' || addr || '>';
         RETURN addr;
      END;

      -- Procedure to split a file pathname into its directory path and file name
      -- components.
      PROCEDURE split_path_name (
         file_path        IN       VARCHAR2,
         directory_path   OUT      VARCHAR2,
         file_name        OUT      VARCHAR2
      )
      IS
         pos   NUMBER;
      BEGIN
         -- Separate the filename from the directory name
         pos := INSTR (file_path, '/', -1);

         IF pos = 0
         THEN
            pos := INSTR (file_path, '\', -1);
         END IF;

         IF pos = 0
         THEN
            directory_path := NULL;
         ELSE
            directory_path := SUBSTR (file_path, 1, pos - 1);
         END IF;

         file_name := SUBSTR (file_path, pos + 1);
      END;

      -- Procedure to append the contents of a file, character LOB, or binary LOB
      -- to the e-mail
      PROCEDURE append_file (
         directory_path   IN       VARCHAR2 DEFAULT NULL,
         file_name        IN       VARCHAR2 DEFAULT NULL,
         mime_type        IN       VARCHAR2,
         conn             IN OUT   UTL_SMTP.connection,
         clob_attach      IN       CLOB DEFAULT NULL,
         blob_attach      IN       BLOB DEFAULT NULL
      )
      IS
         generated_name   VARCHAR2 (30)
                                 := 'CESDIR' || TO_CHAR (SYSDATE, 'HH24MISS');
         directory_name   VARCHAR2 (256)     := NULL;
         file_handle      UTL_FILE.file_type;
         bfile_handle     BFILE;
         lob_len          NUMBER (38)        := 0;
         lob_pos          NUMBER (38)        := 1;
         read_bytes       NUMBER (38);
         lf_at            NUMBER (38);
         line             VARCHAR2 (32767);
         DATA             RAW (32767);
         my_code          NUMBER;
         my_errm          VARCHAR2 (32767);
      BEGIN
         BEGIN
            -- If this is a file to attach, grant access to the directory, unless
            -- already defined, and open the file (as a bfile for a binary file,
            -- otherwise as a text file)
            IF directory_path IS NOT NULL
            THEN
               BEGIN
                  line := directory_path;

                  IF INSTR (directory_path, ':\') = 2
                  THEN
                     SELECT dd.directory_name
                       INTO directory_name
                       FROM dba_directories dd
                      WHERE LOWER (dd.directory_path) = LOWER (line)
                        AND ROWNUM = 1;
                  ELSE
                     SELECT dd.directory_name
                       INTO directory_name
                       FROM dba_directories dd
                      WHERE dd.directory_path = line AND ROWNUM = 1;
                  END IF;
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     directory_name := generated_name;
               END;

               IF directory_name = generated_name
               THEN
                  EXECUTE IMMEDIATE    'create or replace directory '
                                    || directory_name
                                    || ' as '''
                                    || directory_path
                                    || '''';

                  EXECUTE IMMEDIATE    'grant read on directory '
                                    || directory_name
                                    || ' to public';
               END IF;

               IF SUBSTR (mime_type, 1, 4) = 'text'
               THEN
                  file_handle :=
                              UTL_FILE.fopen (directory_name, file_name, 'r');
               ELSE
                  bfile_handle := BFILENAME (directory_name, file_name);
                  lob_len := DBMS_LOB.getlength (bfile_handle);
                  DBMS_LOB.OPEN (bfile_handle, DBMS_LOB.lob_readonly);
               END IF;
            -- If this is a CLOB or BLOB to attach, just get the length of the LOB
            ELSIF clob_attach IS NOT NULL
            THEN
               lob_len := DBMS_LOB.getlength (clob_attach);
            ELSIF blob_attach IS NOT NULL
            THEN
               lob_len := DBMS_LOB.getlength (blob_attach);
            END IF;

            -- Append the file's or LOB's contents to the end of the message
            LOOP
               -- If this is a text file, append the next line to the message,
               -- along with a carriage return / line feed
               IF     directory_path IS NOT NULL
                  AND SUBSTR (mime_type, 1, 4) = 'text'
               THEN
                  UTL_FILE.get_line (file_handle, line);
                  UTL_SMTP.write_data (conn, line || crlf);
               ELSE
                  -- If it is a character LOB, find the next line feed, get the
                  -- the next line of text, and write it out, followed by a
                  -- carriage return / line feed
                  IF clob_attach IS NOT NULL
                  THEN
                     lf_at := DBMS_LOB.INSTR (clob_attach, lf, lob_pos);

                     IF lf_at = 0
                     THEN
                        lf_at := lob_len + 1;
                     END IF;

                     read_bytes := lf_at - lob_pos;

                     IF     read_bytes > 0
                        AND DBMS_LOB.SUBSTR (clob_attach, 1, lf_at - 1) = cr
                     THEN
                        read_bytes := read_bytes - 1;
                     END IF;

                     IF read_bytes > 0
                     THEN
                        DBMS_LOB.READ (clob_attach, read_bytes, lob_pos,
                                       line);
                        UTL_SMTP.write_data (conn, line);
                     END IF;

                     UTL_SMTP.write_data (conn, crlf);
                     lob_pos := lf_at + 1;
                  -- If it is a binary file or binary LOB, process it 57 bytes
                  -- at a time, reading them in with a LOB read, encoding them
                  -- in BASE64, and writing out the encoded binary string as raw
                  -- data
                  ELSE
                     IF lob_pos + 57 - 1 > lob_len
                     THEN
                        read_bytes := lob_len - lob_pos + 1;
                     ELSE
                        read_bytes := 57;
                     END IF;

                     IF blob_attach IS NOT NULL
                     THEN
                        DBMS_LOB.READ (blob_attach, read_bytes, lob_pos,
                                       DATA);
                     ELSE
                        DBMS_LOB.READ (bfile_handle,
                                       read_bytes,
                                       lob_pos,
                                       DATA
                                      );
                     END IF;

                     UTL_SMTP.write_raw_data (conn,
                                              UTL_ENCODE.base64_encode (DATA)
                                             );
                     lob_pos := lob_pos + read_bytes;
                  END IF;

                  -- Exit if we've processed all of the LOB or binary file
                  IF lob_pos > lob_len
                  THEN
                     EXIT;
                  END IF;
               END IF;
            END LOOP;
         -- Output any errors, except at end when no more data is found

         --      exception
--         when no_data_found then
--            null;
--         when others then
--            my_code := SQLCODE;
--            my_errm := SQLERRM;
--            raise_application_error(-20000,
--               'Failed to send mail: Error code ' || my_code || ': ' || my_errm);
         END;

         -- Close the file (binary or text) and drop the generated directory,
         -- if any
         IF directory_path IS NOT NULL
         THEN
            IF SUBSTR (mime_type, 1, 4) != 'text'
            THEN
               DBMS_LOB.CLOSE (bfile_handle);
            ELSE
               UTL_FILE.fclose (file_handle);
            END IF;
         END IF;

         IF directory_name = generated_name
         THEN
            EXECUTE IMMEDIATE 'drop directory ' || directory_name;
         END IF;
      END;
   BEGIN
   -- Open the SMTP connection and set the From and To e-mail addresses
--raise_error(smtp_host||'='||smtp_port);
      conn := UTL_SMTP.open_connection (smtp_host, smtp_port);
      UTL_SMTP.helo (conn, smtp_host);
      recipients := from_name;
      UTL_SMTP.mail (conn, get_address (recipients));
      recipients := to_names;

      WHILE recipients IS NOT NULL
      LOOP
         UTL_SMTP.rcpt (conn, get_address (recipients));
      END LOOP;

      IF cc_names IS NOT NULL AND LENGTH (cc_names) > 0
      THEN
         recipients := cc_names;

         WHILE recipients IS NOT NULL
         LOOP
            UTL_SMTP.rcpt (conn, get_address (recipients));
         END LOOP;
      END IF;

      IF bcc_names IS NOT NULL AND LENGTH (bcc_names) > 0
      THEN
         recipients := bcc_names;

         WHILE recipients IS NOT NULL
         LOOP
            UTL_SMTP.rcpt (conn, get_address (recipients));
         END LOOP;
      END IF;

      UTL_SMTP.open_data (conn);
      -- Build the start of the mail message
      mesg :=
            'Date: '
         || TO_CHAR (SYSDATE, 'dd Mon yy hh24:mi:ss')
         || crlf
         || 'From: '
         || from_name
         || crlf
         || 'Subject: '
         || subject
         || crlf
         || 'To: '
         || to_names
         || crlf;

      IF cc_names IS NOT NULL AND LENGTH (cc_names) > 0
      THEN
         mesg := mesg || 'Cc: ' || cc_names || crlf;
      END IF;

      IF bcc_names IS NOT NULL AND LENGTH (bcc_names) > 0
      THEN
         mesg := mesg || 'Bcc: ' || bcc_names || crlf;
      END IF;

      mesg :=
            mesg
         || 'Mime-Version: 1.0'
         || crlf
         || 'Content-Type: multipart/mixed; boundary="'
         || boundary
         || '"'
         || crlf
         || crlf
         || 'This is a Mime message, which your current mail reader may not'
         || crlf
         || 'understand. Parts of the message will appear as text. If the remainder'
         || crlf
         || 'appears as random characters in the message body, instead of as'
         || crlf
         || 'attachments, then you''ll have to extract these parts and decode them'
         || crlf
         || 'manually.'
         || crlf
         || crlf;
      UTL_SMTP.write_data (conn, mesg);

      -- Write the text message or message file or message CLOB, if any
      IF    (MESSAGE IS NOT NULL AND LENGTH (MESSAGE) > 0)
         OR clob_message IS NOT NULL
      THEN
         mesg :=
               '--'
            || boundary
            || crlf
            || 'Content-Type: text/plain; name="message.txt"; charset=US-ASCII'
            || crlf
            || 'Content-Disposition: inline; filename="message.txt"'
            || crlf
            || 'Content-Transfer-Encoding: 7bit'
            || crlf
            || crlf;
         UTL_SMTP.write_data (conn, mesg);

         IF    INSTR (MESSAGE, '/') = 1
            OR INSTR (MESSAGE, ':\') = 2
            OR INSTR (MESSAGE, '\\') = 1
         THEN
            split_path_name (MESSAGE, directory_path, file_name);
            append_file (directory_path, file_name, 'text', conn);
            UTL_SMTP.write_data (conn, crlf);
         ELSIF MESSAGE IS NOT NULL AND LENGTH (MESSAGE) > 0
         THEN
            UTL_SMTP.write_data (conn, MESSAGE);

            IF    LENGTH (MESSAGE) = 1
               OR SUBSTR (MESSAGE, LENGTH (MESSAGE) - 1) != crlf
            THEN
               UTL_SMTP.write_data (conn, crlf);
            END IF;
         ELSIF clob_message IS NOT NULL
         THEN
            append_file (NULL,
                         'message.txt',
                         'text/plain',
                         conn,
                         clob_message
                        );
         END IF;
      END IF;

      -- Write the HTML message or message file or message CLOB, if any
      IF    (html_message IS NOT NULL AND LENGTH (html_message) > 0)
         OR clob_html_message IS NOT NULL
      THEN
         mesg :=
               '--'
            || boundary
            || crlf
            || 'Content-Type: text/html; name="message.html"; charset=US-ASCII'
            || crlf
            || 'Content-Disposition: inline; filename="message.html"'
            || crlf
            || 'Content-Transfer-Encoding: 7bit'
            || crlf
            || crlf;
         UTL_SMTP.write_data (conn, mesg);

         IF    INSTR (html_message, '/') = 1
            OR INSTR (html_message, ':\') = 2
            OR INSTR (html_message, '\\') = 1
         THEN
            split_path_name (html_message, directory_path, file_name);
            append_file (directory_path, file_name, 'text', conn);
            UTL_SMTP.write_data (conn, crlf);
         ELSIF html_message IS NOT NULL AND LENGTH (html_message) > 0
         THEN
            UTL_SMTP.write_data (conn, html_message);

            IF    LENGTH (html_message) = 1
               OR SUBSTR (html_message, LENGTH (html_message) - 1) != crlf
            THEN
               UTL_SMTP.write_data (conn, crlf);
            END IF;
         ELSIF clob_html_message IS NOT NULL
         THEN
            append_file (NULL,
                         'message.html',
                         'text/html',
                         conn,
                         clob_html_message
                        );
         END IF;
      END IF;

      -- Attach the files, if any
      IF file_attach IS NOT NULL
      THEN
         FOR i IN 1 .. file_attach.COUNT
         LOOP
            file_path := NULL;
            mime_type := NULL;

            -- If this is a file path parameter, get the file path and check the
            -- next parameter to see if it is a file type parameter (else default
            -- to text/plain).
            IF file_attach (i) IS NULL OR LENGTH (file_attach (i)) = 0
            THEN
               EXIT;
            END IF;

            IF    INSTR (file_attach (i), '/') = 1
               OR INSTR (file_attach (i), ':\') = 2
               OR INSTR (file_attach (i), '\\') = 1
            THEN
               file_path := file_attach (i);

               IF i = file_attach.COUNT
               THEN
                  mime_type := 'text/plain';
               ELSE
                  IF     INSTR (file_attach (i + 1), '/') > 1
                     AND INSTR (file_attach (i + 1), '/', 1, 2) = 0
                  THEN
                     mime_type := file_attach (i + 1);
                  ELSE
                     mime_type := 'text/plain';
                  END IF;
               END IF;
            END IF;

            -- If this is a file path parameter ...
            IF file_path IS NOT NULL
            THEN
               split_path_name (file_path, directory_path, file_name);
               -- Generate the MIME boundary line according to the file (mime) type
               -- specified.
               mesg := crlf || '--' || boundary || crlf;

               IF SUBSTR (mime_type, 1, 4) != 'text'
               THEN
                  mesg :=
                        mesg
                     || 'Content-Type: '
                     || mime_type
                     || '; name="'
                     || file_name
                     || '"'
                     || crlf
                     || 'Content-Disposition: attachment; filename="'
                     || file_name
                     || '"'
                     || crlf
                     || 'Content-Transfer-Encoding: base64'
                     || crlf
                     || crlf;
               ELSE
                  mesg :=
                        mesg
                     || 'Content-Type: application/octet-stream; name="'
                     || file_name
                     || '"'
                     || crlf
                     || 'Content-Disposition: attachment; filename="'
                     || file_name
                     || '"'
                     || crlf
                     || 'Content-Transfer-Encoding: 7bit'
                     || crlf
                     || crlf;
               END IF;

               UTL_SMTP.write_data (conn, mesg);
               -- Append the file contents to the end of the message
               append_file (directory_path, file_name, mime_type, conn);
            END IF;
         END LOOP;
      END IF;

      -- Attach the character LOB's, if any
      IF clob_attach IS NOT NULL
      THEN
         FOR i IN 1 .. clob_attach.COUNT
         LOOP
            -- Get the name and mime type, if given, else use default values
            IF clob_attach (i).vclob IS NULL
            THEN
               EXIT;
            END IF;

            file_name := clob_attach (i).filename;

            IF file_name IS NULL
            THEN
               file_name := 'clob' || i || '.txt';
            END IF;

            mime_type := clob_attach (i).mimetype;

            IF mime_type IS NULL
            THEN
               mime_type := 'text/plain';
            END IF;

            -- Generate the MIME boundary line for this character file attachment
            mesg := crlf || '--' || boundary || crlf;
            mesg :=
                  mesg
               || 'Content-Type: application/octet-stream; name="'
               || file_name
               || '"'
               || crlf
               || 'Content-Disposition: attachment; filename="'
               || file_name
               || '"'
               || crlf
               || 'Content-Transfer-Encoding: 7bit'
               || crlf
               || crlf;
            UTL_SMTP.write_data (conn, mesg);
            -- Append the CLOB contents to the end of the message
            append_file (NULL,
                         file_name,
                         mime_type,
                         conn,
                         clob_attach      => clob_attach (i).vclob
                        );
         END LOOP;
      END IF;

      -- Attach the binary LOB's, if any
      IF blob_attach IS NOT NULL
      THEN
         FOR i IN 1 .. blob_attach.COUNT
         LOOP
            -- Get the name and mime type, if given, else use default values
            IF blob_attach (i).vblob IS NULL
            THEN
               EXIT;
            END IF;

            file_name := blob_attach (i).filename;

            IF file_name IS NULL OR LENGTH (file_name) = 0
            THEN
               file_name := 'blob' || i;
            END IF;

            mime_type := blob_attach (i).mimetype;

            IF mime_type IS NULL
            THEN
               mime_type := 'text/plain';  -- but, this is a strange default!
            END IF;

            -- Generate the MIME boundary line for this BASE64 binary attachment
            mesg := crlf || '--' || boundary || crlf;
            mesg :=
                  mesg
               || 'Content-Type: '
               || mime_type
               || '; name="'
               || file_name
               || '"'
               || crlf
               || 'Content-Disposition: attachment; filename="'
               || file_name
               || '"'
               || crlf
               || 'Content-Transfer-Encoding: base64'
               || crlf
               || crlf;
            UTL_SMTP.write_data (conn, mesg);
            -- Append the BLOB contents to the end of the message
            append_file (NULL,
                         file_name,
                         mime_type,
                         conn,
                         blob_attach      => blob_attach (i).vblob
                        );
         END LOOP;
      END IF;

      -- Append the final boundary line
      mesg := crlf || '--' || boundary || '--' || crlf;
      UTL_SMTP.write_data (conn, mesg);
      -- Close the SMTP connection
      UTL_SMTP.close_data (conn);
      UTL_SMTP.quit (conn);
--exception
--   when utl_smtp.transient_error or utl_smtp.permanent_error then
--      my_code := SQLCODE;
--      my_errm := SQLERRM;
--      begin
--         utl_smtp.quit(conn);
--      exception
--         when utl_smtp.transient_error or utl_smtp.permanent_error then
--            null;
--      end;
--      raise_application_error(-20000,
--         'Failed to send mail - SMTP server down or unavailable: Error code ' ||
--            my_code || ': ' || my_errm);
--   when others then
--      my_code := SQLCODE;
--      my_errm := SQLERRM;
--      raise_application_error(-20000,
--         'Failed to send mail: Error code ' || my_code || ': ' || my_errm);
   END;

-- Define the various overloaded definitions (interfaces) to email_files
   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesfiles,
      clob_attach    cesclobs DEFAULT NULL,
      blob_attach    cesblobs DEFAULT NULL
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      MESSAGE,
                      NULL,
                      html_message,
                      NULL,
                      cc_names,
                      bcc_names,
                      attach,
                      clob_attach,
                      blob_attach
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesfiles,
      clob_attach    cesclobs DEFAULT NULL,
      blob_attach    cesblobs DEFAULT NULL
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      NULL,
                      MESSAGE,
                      html_message,
                      NULL,
                      cc_names,
                      bcc_names,
                      attach,
                      clob_attach,
                      blob_attach
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesfiles,
      clob_attach    cesclobs DEFAULT NULL,
      blob_attach    cesblobs DEFAULT NULL
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      MESSAGE,
                      NULL,
                      NULL,
                      html_message,
                      cc_names,
                      bcc_names,
                      attach,
                      clob_attach,
                      blob_attach
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesfiles,
      clob_attach    cesclobs DEFAULT NULL,
      blob_attach    cesblobs DEFAULT NULL
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      NULL,
                      MESSAGE,
                      NULL,
                      html_message,
                      cc_names,
                      bcc_names,
                      attach,
                      clob_attach,
                      blob_attach
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesclobs
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      MESSAGE,
                      NULL,
                      html_message,
                      NULL,
                      cc_names,
                      bcc_names,
                      cesfiles (NULL),
                      attach,
                      NULL
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesclobs
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      NULL,
                      MESSAGE,
                      html_message,
                      NULL,
                      cc_names,
                      bcc_names,
                      cesfiles (NULL),
                      attach,
                      NULL
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesclobs
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      MESSAGE,
                      NULL,
                      NULL,
                      html_message,
                      cc_names,
                      bcc_names,
                      cesfiles (NULL),
                      attach,
                      NULL
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesclobs
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      NULL,
                      MESSAGE,
                      NULL,
                      html_message,
                      cc_names,
                      bcc_names,
                      cesfiles (NULL),
                      attach,
                      NULL
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesblobs
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      MESSAGE,
                      NULL,
                      html_message,
                      NULL,
                      cc_names,
                      bcc_names,
                      cesfiles (NULL),
                      NULL,
                      attach
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesblobs
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      NULL,
                      MESSAGE,
                      html_message,
                      NULL,
                      cc_names,
                      bcc_names,
                      cesfiles (NULL),
                      NULL,
                      attach
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesblobs
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      MESSAGE,
                      NULL,
                      NULL,
                      html_message,
                      cc_names,
                      bcc_names,
                      cesfiles (NULL),
                      NULL,
                      attach
                     );
   END;

   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        CLOB,
      html_message   CLOB,
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      attach         cesblobs
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      NULL,
                      MESSAGE,
                      NULL,
                      html_message,
                      cc_names,
                      bcc_names,
                      cesfiles (NULL),
                      NULL,
                      attach
                     );
   END;

-- This overloaded version supports legacy code using the "filename/filetype"
-- parameter pairs instead of the current "attach" parameters.  It is also used
-- when no file attachments are specified (since there is not a default value
-- for the "attach" parameters in the interfaces above).
   PROCEDURE email_files (
      from_name      VARCHAR2,
      to_names       VARCHAR2,
      subject        VARCHAR2,
      MESSAGE        VARCHAR2 DEFAULT '',
      html_message   VARCHAR2 DEFAULT '',
      cc_names       VARCHAR2 DEFAULT NULL,
      bcc_names      VARCHAR2 DEFAULT NULL,
      filename1      VARCHAR2 DEFAULT NULL,
      filetype1      VARCHAR2 DEFAULT 'text/plain',
      filename2      VARCHAR2 DEFAULT NULL,
      filetype2      VARCHAR2 DEFAULT 'text/plain',
      filename3      VARCHAR2 DEFAULT NULL,
      filetype3      VARCHAR2 DEFAULT 'text/plain'
   )
   IS
   BEGIN
      do_email_files (from_name,
                      to_names,
                      subject,
                      MESSAGE,
                      NULL,
                      html_message,
                      NULL,
                      cc_names,
                      bcc_names,
                      cesfiles (filename1,
                                filetype1,
                                filename2,
                                filetype2,
                                filename3,
                                filetype3
                               ),
                      NULL,
                      NULL
                     );
   END;

--   FUNCTION send_email (
--      v_sendor      IN   VARCHAR2,
--      v_recipient   IN   VARCHAR2,
--      v_cc          IN   VARCHAR2,
--      v_subject     IN   VARCHAR2,
--      v_message     IN   VARCHAR2
--   )
--      RETURN NUMBER
--   IS
--      v_mailhost      VARCHAR2 (200);
--      v_status_desc   VARCHAR2 (200);
--      v_sendoremail   tqc_parameters.param_value%TYPE;
--   BEGIN
--      SELECT param_value
--        INTO v_mailhost
--        FROM tqc_parameters
--       WHERE param_name = 'MAILHOST';

--      IF v_sendor IS NULL
--      THEN
--         SELECT param_value
--           INTO v_sendoremail
--           FROM tqc_parameters
--          WHERE param_name = 'EMAILS_FROM';

--         sendmail (v_sendoremail,
--                   v_recipient,
--                   v_subject,
--                   v_message,
--                   v_mailhost,
--                   v_cc
--                  );
--         RETURN (1);
--      ELSIF v_sendor IS NOT NULL AND v_recipient IS NOT NULL
--      THEN
--         sendmail (v_sendor,
--                   v_recipient,
--                   v_subject,
--                   v_message,
--                   v_mailhost,
--                   v_cc
--                  );
--         RETURN (1);
--      END IF;
--   EXCEPTION
--      WHEN OTHERS
--      THEN
--         raise_error ('Error sending incident email to ' || v_recipient);
--         RETURN (1);
--   END;

FUNCTION send_email (
      v_sendor      IN   VARCHAR2,
      v_recipient   IN   VARCHAR2,
      v_cc          IN   VARCHAR2,
      v_subject     IN   VARCHAR2,
      v_message     IN   VARCHAR2
   )
      RETURN NUMBER
   IS
      v_att_tab       cesblobs       := cesblobs (NULL);
      v_status_desc   VARCHAR2 (200) := NULL;
      v_from          VARCHAR2 (200)
                      := tqc_parameters_pkg.get_param_varchar ('EMAILS_FROM');
   BEGIN
      v_from := NVL (v_sendor, v_from);

      IF v_recipient IS NOT NULL
      THEN
         tqc_email_pkg.do_email_files
                                 (v_from,      --from_name           VARCHAR2,
                                  v_recipient, --to_names            VARCHAR2,
                                  v_subject,   --subject             VARCHAR2,
                                  v_message,   --MESSAGE             VARCHAR2,
                                  v_message,       --clob_message        CLOB,
                                  NULL,       -- html_message        VARCHAR2,
                                  NULL,           -- clob_html_message   CLOB,
                                  v_cc,       -- cc_names            VARCHAR2,
                                  NULL,        --bcc_names           VARCHAR2,
                                  NULL,        --file_attach         cesfiles,
                                  NULL,      --- clob_attach         cesclobs,
                                  NULL          --blob_attach         cesblobs
                                 );
         RETURN (1);                                               -- SUCCESS!
      END IF;
      RETURN (0);
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error sending incident email to ' || v_recipient||' From: '||v_from||  ' v_message: '||v_message);
         RETURN (0);
   END;



PROCEDURE send_html_mail (v_email_code IN NUMBER)
   IS
      v_body         VARCHAR2 (4000);
      v_html_body    VARCHAR2 (4000);
      v_cc           VARCHAR2 (250);
      v_bcc          VARCHAR2 (250);
      v_att_tab      cesblobs        := cesblobs ();

      CURSOR cur_email
      IS
         SELECT *
           FROM tqc_email_messages
          WHERE email_code = v_email_code;

      CURSOR attfiles
      IS
         SELECT *
           FROM tqc_email_attachments
          WHERE eatt_email_code = v_email_code;

      x              NUMBER;
      v_email_from   VARCHAR2 (250);
   BEGIN
      FOR cur_email_rec IN cur_email
      LOOP
         IF cur_email_rec.email_address IS NULL
         THEN
            raise_error ('Please specify the Email Address to send to..');
         END IF;

         BEGIN
            SELECT param_value
              INTO v_email_from
              FROM tqc_parameters
             WHERE param_name = 'EMAILS_FROM';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error
                  ('EMAILS_FROM parameter not defined. Contact Turnkey Africa Limited...'
                  );
            WHEN OTHERS
            THEN
               raise_error ('Error getting email address from...');
         END;

         IF NVL (v_email_from, 'NONE') = 'NONE'
         THEN
            raise_error
               ('Please define the email address from parameter [EMAILS_FROM]...'
               );
         END IF;

--raise_error(v_email_from||'='|| cur_email_rec.EMAIL_ADDRESS||'='||
--cur_email_rec.EMAIL_SUBJ||'='||cur_email_rec.EMAIL_MSG);
           tqc_email_pkg.do_email_files
                                 (v_email_from,      --from_name           VARCHAR2,
                                  cur_email_rec.email_address, --to_names            VARCHAR2,
                                  cur_email_rec.email_subj,   --subject             VARCHAR2,
                                  NULL,--cur_email_rec.email_msg,   --MESSAGE             VARCHAR2,
                                  NULL,--cur_email_rec.email_msg,       --clob_message        CLOB,
                                  cur_email_rec.email_msg,       -- html_message        VARCHAR2,
                                  cur_email_rec.email_msg,           -- clob_html_message   CLOB,
                                  v_cc,       -- cc_names            VARCHAR2,
                                  v_bcc,        --bcc_names           VARCHAR2,
                                  NULL,        --file_attach         cesfiles,
                                  NULL,      --- clob_attach         cesclobs,
                                  NULL          --blob_attach         cesblobs
                                 );                        

         UPDATE tqc_email_messages
            SET email_status = 'S',
                email_send_date = TRUNC (SYSDATE)
          WHERE email_code = cur_email_rec.email_code;
      END LOOP;
   END send_html_mail; 

PROCEDURE get_email_details (
   v_email_details   OUT      email_details_cursor,
   v_claim_no        IN       VARCHAR2,
   v_email_user      IN       VARCHAR2,
   v_trans_no        IN       NUMBER DEFAULT NULL,
   v_type            IN       NUMBER DEFAULT NULL,
   v_from_dt       IN DATE DEFAULT NULL,
   v_to_dt          IN DATE DEFAULT NULL,
   v_asof_dt           IN DATE DEFAULT NULL
)
IS
   v_app_lvl   VARCHAR2 (20);
BEGIN
   IF v_email_user = 'SB'
   THEN
      v_app_lvl := 'C';
      OPEN v_email_details FOR
         SELECT REPLACE (msgt_msg_subject,
                         '[CLAIMNO]',
                         v_claim_no
                        ) emailsubject,
                tqc_memos_dbpkg.process_gis_pol_memo
                                                   (NULL,
                                                    v_claim_no,
                                                    msgt_msg,
                                                    v_app_lvl,
                                                    NULL
                                                   ) emailbody
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'SALVTAGGING';
   ELSIF v_email_user = 'RJ'
   THEN
      v_app_lvl := 'AC';
      OPEN v_email_details FOR
         SELECT REPLACE (msgt_msg_subject,
                         '[TRANSNO]',
                         v_trans_no
                        ) emailsubject,
                tqc_memos_dbpkg.process_gis_pol_memo
                                                   (NULL,
                                                    NULL,
                                                    msgt_msg,
                                                    v_app_lvl,
                                                    NULL,
                                                    NULL,
                                                    v_trans_no
                                                   ) emailbody
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'REJECTTRANS';
   ELSIF v_email_user = 'ST'
   THEN
      v_app_lvl := 'ST';
      IF v_from_dt IS NOT NULL AND v_to_dt IS NOT NULL
      THEN
      OPEN v_email_details FOR
         SELECT REPLACE((REPLACE (msgt_msg_subject,
                         '[FROM_DATE]',
                         v_from_dt
                        )), '[TO_DATE]',  v_to_dt) emailsubject,
                tqc_memos_dbpkg.process_gis_pol_memo
                                                   (NULL,
                                                    NULL,
                                                    msgt_msg,
                                                    v_app_lvl,
                                                    NULL,
                                                    v_type,
                                                    NULL,
                                                    v_trans_no
                                                   ) emailbody
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'STMTTEMPLBF';
     ELSIF v_asof_dt IS NOT NULL THEN
     OPEN v_email_details FOR
         SELECT REPLACE (msgt_msg_subject,
                         '[SEND_DATE]',
                         v_asof_dt
                        ) emailsubject,
                tqc_memos_dbpkg.process_gis_pol_memo
                                                   (NULL,
                                                    NULL,
                                                    msgt_msg,
                                                    v_app_lvl,
                                                    NULL,
                                                    v_type,
                                                    NULL,
                                                    v_trans_no
                                                   ) emailbody
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'STMTTEMPL';     
     END IF;
   ELSIF v_email_user = 'BDM'
   THEN
      v_app_lvl := 'BDM';
      IF v_from_dt IS NOT NULL AND v_to_dt IS NOT NULL
      THEN
      OPEN v_email_details FOR
         SELECT REPLACE((REPLACE (msgt_msg_subject,
                         '[FROM_DATE]',
                         v_from_dt
                        )), '[TO_DATE]',  v_to_dt) emailsubject,
                tqc_memos_dbpkg.process_gis_pol_memo
                                                   (NULL,
                                                    NULL,
                                                    msgt_msg,
                                                    v_app_lvl,
                                                    NULL,
                                                    v_type,
                                                    NULL,
                                                    v_trans_no
                                                   ) emailbody
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'BDMTEMPL';
     ELSIF v_asof_dt IS NOT NULL THEN
     OPEN v_email_details FOR
         SELECT REPLACE (msgt_msg_subject,
                         '[SEND_DATE]',
                         v_asof_dt
                        ) emailsubject,
                tqc_memos_dbpkg.process_gis_pol_memo
                                                   (NULL,
                                                    NULL,
                                                    msgt_msg,
                                                    v_app_lvl,
                                                    NULL,
                                                    v_type,
                                                    NULL,
                                                    v_trans_no
                                                   ) emailbody
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'BDMTEMPL';     
     END IF; 
   ELSIF v_email_user = 'DLD'
   THEN
      v_app_lvl := 'DLD';
      IF v_from_dt IS NOT NULL AND v_to_dt IS NOT NULL
      THEN
      OPEN v_email_details FOR
         SELECT REPLACE((REPLACE (msgt_msg_subject,
                         '[FROM_DATE]',
                         v_from_dt
                        )), '[TO_DATE]',  v_to_dt) emailsubject,
                tqc_memos_dbpkg.process_gis_pol_memo
                                                   (NULL,
                                                    NULL,
                                                    msgt_msg,
                                                    v_app_lvl,
                                                    NULL,
                                                    v_type,
                                                    NULL,
                                                    v_trans_no
                                                   ) emailbody
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'DLDTEMPL';
     ELSIF v_asof_dt IS NOT NULL THEN
     OPEN v_email_details FOR
         SELECT REPLACE (msgt_msg_subject,
                         '[SEND_DATE]',
                         v_asof_dt
                        ) emailsubject,
                tqc_memos_dbpkg.process_gis_pol_memo
                                                   (NULL,
                                                    NULL,
                                                    msgt_msg,
                                                    v_app_lvl,
                                                    NULL,
                                                    v_type,
                                                    NULL,
                                                    v_trans_no
                                                   ) emailbody
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'DLDTEMPL';     
     END IF;          
   END IF;
END;

PROCEDURE storesentstatementlogs (
   v_email_to        IN   VARCHAR2,
   v_email_subject   IN   VARCHAR2,
   v_report_name     IN   VARCHAR2,
   v_email_from      IN   VARCHAR2,
   v_email_message   IN   VARCHAR2,
   v_email_code      IN   NUMBER,
   v_email_type      IN   NUMBER
)
IS
v_agn_code    NUMBER;
v_client_code NUMBER;
BEGIN
    IF v_email_type = 1 THEN
        v_agn_code := v_email_code;
    ELSIF v_email_type = 2 THEN
        v_client_code := v_email_code;
    END IF;

   INSERT INTO tqc_email_statement_logs
               (eml_stmt_code, eml_stmt_email_to, eml_stmt_subject,
                eml_stmt_rpt_name, eml_stmt_email_from, eml_stmt_message, eml_stmt_client_code, eml_stmt_agn_code, eml_stmt_type, eml_stmt_send_dt
               )
        VALUES (eml_stmt_code_seq.NEXTVAL, v_email_to, v_email_subject,
                v_report_name, v_email_from, v_email_message, v_client_code, v_agn_code, v_email_type, SYSDATE
               );
END;    
PROCEDURE create_telematics_email_msg (
      v_reciepient     IN       VARCHAR2,
      v_clnt_code      IN       NUMBER,
      v_agn_code       IN       NUMBER,
      v_quot_code      IN       NUMBER,
      v_quot_no        IN       VARCHAR2,
      v_pol_code       IN       NUMBER,
      v_pol_no         IN       VARCHAR2,
      v_clm_no         IN       VARCHAR2,
      v_files_tab      IN       email_files_tab,
      v_sys_code       IN       NUMBER,
      v_sys_mod        IN       VARCHAR2,
      v_user           IN       VARCHAR2,
      v_email_code     IN OUT   NUMBER,
      v_app_lvl        IN       VARCHAR2,
      v_usr_code       IN       NUMBER,
      v_msg_subj       IN       VARCHAR2,
      v_email_body     IN       VARCHAR2,
      v_to_send_date   IN       DATE
   )
   IS
      v_email_addr          VARCHAR2 (35);
      v_errmsg              VARCHAR2 (400);
      v_usr_type            VARCHAR2 (35);
      v_email_sender_addr   VARCHAR2 (35);
   BEGIN
      IF v_reciepient = 'C'
      THEN
         BEGIN
            SELECT clnt_email_addrs
              INTO v_email_addr
              FROM tqc_clients
             WHERE clnt_code = v_clnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error client not defined..');
         END;

         BEGIN
            SELECT param_value
              INTO v_email_sender_addr
              FROM tqc_parameters
             WHERE param_name = 'EMAILS_FROM';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error ('Error Getting Defualt Email Sender ');
            WHEN OTHERS
            THEN
               raise_error ('Error Getting Defualt Email Sender ');
         END;
      ELSIF v_reciepient = 'A'
      THEN
         BEGIN
            SELECT agn_email_address
              INTO v_email_addr
              FROM tqc_agencies
             WHERE agn_code = v_agn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error agent not defined..');
         END;
      ELSIF v_reciepient = 'U'
      THEN
         BEGIN
            SELECT usr_type, usr_email
              INTO v_usr_type, v_email_sender_addr
              FROM tqc_users
             WHERE usr_code = v_usr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_usr_type := 'U';
         END;

--raise_Error(v_usr_type||'='||v_usr_code);
         IF v_usr_type = 'U'
         THEN
            BEGIN
               SELECT usr_email
                 INTO v_email_addr
                 FROM tqc_users
                WHERE usr_code = v_usr_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error Getting User..' || v_usr_code);
            END;
         ELSE                                            -- HANDLE GROUP USERS
            NULL;
         END IF;

         BEGIN
            SELECT usr_email
              INTO v_email_sender_addr
              FROM tqc_users
             WHERE UPPER (usr_username) = UPPER (v_user);
         EXCEPTION
            WHEN OTHERS
            THEN
               v_usr_type := 'U';
         END;
      ELSIF v_reciepient = 'DR'
      THEN
         BEGIN
            SELECT sla_email
              INTO v_email_addr
              FROM TQ_FMS.FMS_SL_ACCOUNTS
             WHERE SLA_CODE = v_agn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Getting Debtor email..');
         END;   
      END IF;

      BEGIN
         send_email_msg (v_clnt_code,
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
                         v_email_sender_addr,
                         v_to_send_date
                        );
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error creating Email message');
      END;
   END;
END tqc_email_pkg;
/