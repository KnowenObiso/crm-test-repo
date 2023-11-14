--
-- TQC_SMS_PKG010416  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_sms_pkg010416
AS
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20033,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   PROCEDURE create_sms_msg (
      v_reciepient   IN       VARCHAR2,
      v_clnt_code    IN       NUMBER,
      v_agn_code     IN       NUMBER,
      v_quot_code    IN       NUMBER,
      v_quot_no      IN       VARCHAR2,
      v_pol_code     IN       NUMBER,
      v_pol_no       IN       VARCHAR2,
      v_clm_no       IN       VARCHAR2,
      v_msgt_code    IN       NUMBER,
      v_sys_code     IN       NUMBER,
      v_sys_mod      IN       VARCHAR2,
      v_user         IN       VARCHAR2,
      v_sms_code     IN OUT   NUMBER,
      v_app_lvl      IN       VARCHAR2,
      v_usr_code     IN       NUMBER
   )
   IS
      v_sms_tel    VARCHAR2 (35);
      v_sms_msg    VARCHAR2 (500);
      v_errmsg     VARCHAR2 (400);
      v_usr_type   VARCHAR2 (35);
   BEGIN
      IF v_reciepient = 'C'
      THEN
         BEGIN
            SELECT clnt_sms_tel
              INTO v_sms_tel
              FROM tqc_clients
             WHERE clnt_code = v_clnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error client not defined..');
         END;
      ELSIF v_reciepient = 'A'
      THEN
         BEGIN
            SELECT agn_sms_tel
              INTO v_sms_tel
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
            SELECT usr_type
              INTO v_usr_type
              FROM tqc_users
             WHERE usr_code = v_clnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_usr_type := 'U';
         END;

         IF v_usr_type = 'U'
         THEN
            SELECT usr_cell_phone_no
              INTO v_sms_tel
              FROM tqc_users
             WHERE usr_code = v_clnt_code;
         ELSE                                            -- HANDLE GROUP USERS
            NULL;
         END IF;
      END IF;

      BEGIN
         SELECT msgt_msg
           INTO v_sms_msg
           FROM tqc_msg_templates
          WHERE msgt_code = v_msgt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Template selected not found..');
      END;

      BEGIN
         v_sms_msg :=
            tqc_memos_dbpkg.process_gis_pol_memo (              --v_pol_code,
                                                  v_quot_code,
                                                  v_clm_no,
                                                  NULL,
                                                  v_sms_msg,
                                                  v_app_lvl
                                                 );
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      BEGIN
         send_sms_msg (v_clnt_code,
                       v_agn_code,
                       v_quot_code,
                       v_quot_no,
                       v_pol_code,
                       v_pol_no,
                       v_clm_no,
                       v_sms_tel,
                       v_sms_msg,
                       v_sys_code,
                       v_sys_mod,
                       v_user,
                       v_sms_code
                      );
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error creating sms message');
      END;
   END;

   PROCEDURE SEND_SMS_MSG (v_clnt_code   IN     NUMBER,
                           v_agn_code    IN     NUMBER,
                           v_quot_code   IN     NUMBER,
                           v_quot_no     IN     VARCHAR,
                           v_pol_code    IN     NUMBER,
                           v_pol_no      IN     VARCHAR2,
                           v_clm_no      IN     VARCHAR2,
                           v_tel_no      IN     VARCHAR2,
                           v_sms_text    IN     VARCHAR2,
                           v_sys_code    IN     NUMBER,
                           v_sys_mod     IN     VARCHAR2,
                           v_user        IN     VARCHAR2,
                           v_sms_code       OUT NUMBER)
   IS
      --v_user VARCHAR2(35):= Pkg_Global_Vars.get_pvarchar2 ('pkg_global_vars.pvg_username');

      v_usr_code         NUMBER;
      lv_temp_txt        VARCHAR2 (10000);
      v_client           VARCHAR2 (200);
      v_pol_renewal_dt   DATE;
      v_uw_year          NUMBER;
      v_address          VARCHAR2 (200);
      v_title            VARCHAR2 (20);
      v_clnt_name        VARCHAR2 (200);
   BEGIN
      lv_temp_txt := v_sms_text;

      BEGIN
         SELECT CLNT_NAME || ' ' || CLNT_OTHER_NAMES CLIENTNAME,
                CLNT_POSTAL_ADDRS,
                CLNT_TITLE,
                CLNT_NAME
           INTO v_client,
                v_address,
                v_title,
                v_clnt_name
           FROM TQC_CLIENTS
          WHERE CLNT_CODE = v_clnt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      BEGIN
         SELECT POL_RENEWAL_DT, POL_UW_YEAR
           INTO v_pol_renewal_dt, v_uw_year
           FROM GIN_POLICIES
          WHERE POL_BATCH_NO = v_pol_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
      lv_temp_txt :=
         REPLACE (lv_temp_txt,
                  '[RENDATE]',
                  TO_CHAR (v_pol_renewal_dt, 'DD/Mon/YYYY'));
      lv_temp_txt :=
         REPLACE (lv_temp_txt,
                  '[RENEWALDATE]',
                  TO_CHAR (v_pol_renewal_dt, 'DD/Mon/YYYY'));

      lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
      lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENTNAME]', v_client);
      lv_temp_txt := REPLACE (lv_temp_txt, '[UWYEAR]', v_uw_year);
      lv_temp_txt := REPLACE (lv_temp_txt, '[ADDRESS]', v_address);
      lv_temp_txt := REPLACE (lv_temp_txt, '[TITLE]', v_title);
      lv_temp_txt := REPLACE (lv_temp_txt, '[SURNAME]', v_clnt_name);
      lv_temp_txt := REPLACE (lv_temp_txt, '[QUOTNO]', v_quot_no);

      -- raise_error('v_sys_code ='||v_agn_code);
      SELECT TQC_SMS_CODE_SEQ.NEXTVAL INTO v_sms_code FROM DUAL;

      BEGIN
         SELECT USR_CODE
           INTO v_usr_code
           FROM TQC_USERS
          WHERE USR_USERNAME = v_user;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Unable to fetch user code for ' || v_user);
      END;

      INSERT INTO TQC_SMS_MESSAGES (SMS_CODE,
                                    SMS_SYS_CODE,
                                    SMS_SYS_MODULE,
                                    SMS_CLNT_CODE,
                                    SMS_AGN_CODE,
                                    SMS_QUOT_CODE,
                                    SMS_POL_CODE,
                                    SMS_POL_NO,
                                    SMS_CLM_NO,
                                    SMS_TEL_NO,
                                    SMS_MSG,
                                    SMS_STATUS,
                                    SMS_PREPARED_BY,
                                    SMS_PREPARED_DATE,
                                    SMS_USR_CODE)
           VALUES (v_sms_code,
                   v_sys_code,
                   v_sys_mod,
                   v_clnt_code,
                   v_agn_code,
                   v_quot_code,
                   v_pol_code,
                   v_pol_no,
                   v_clm_no,
                   v_tel_no,
                   lv_temp_txt,
                   'D',
                   v_user,
                   SYSDATE,
                   v_usr_code);
   END;

   PROCEDURE send_sms_msg (
      v_clnt_code   IN   NUMBER,
      v_agn_code    IN   NUMBER,
      v_quot_code   IN   NUMBER,
      v_pol_code    IN   NUMBER,
      v_pol_no      IN   VARCHAR2,
      v_clm_no      IN   VARCHAR2,
      v_tel_no      IN   VARCHAR2,
      v_sms_text    IN   VARCHAR2,
      v_sys_code    IN   NUMBER,
      v_sys_mod     IN   VARCHAR2,
      v_user        IN   VARCHAR2
   )
   IS
      --v_user VARCHAR2(35):= Pkg_Global_Vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
      v_usr_code         NUMBER;
      v_sms_code         NUMBER;
      lv_temp_txt        VARCHAR2 (10000);
      v_client           VARCHAR2 (200);
      v_pol_renewal_dt   DATE;
      v_uw_year          NUMBER;
      v_address          VARCHAR2 (200);
      v_title            VARCHAR2 (20);
      v_clnt_name        VARCHAR2 (200);
   BEGIN
      lv_temp_txt := v_sms_text;

      BEGIN
         SELECT clnt_name || ' ' || clnt_other_names clientname,
                clnt_postal_addrs, clnt_title, clnt_name
           INTO v_client,
                v_address, v_title, v_clnt_name
           FROM tqc_clients
          WHERE clnt_code = v_clnt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      BEGIN
         SELECT pol_renewal_dt, pol_uw_year
           INTO v_pol_renewal_dt, v_uw_year
           FROM gin_policies
          WHERE pol_batch_no = v_pol_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
      lv_temp_txt :=
         REPLACE (lv_temp_txt,
                  '[RENDATE]',
                  TO_CHAR (v_pol_renewal_dt, 'DD/Mon/YYYY')
                 );
      lv_temp_txt :=
         REPLACE (lv_temp_txt,
                  '[RENEWALDATE]',
                  TO_CHAR (v_pol_renewal_dt, 'DD/Mon/YYYY')
                 );
      lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
      lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENTNAME]', v_client);
      lv_temp_txt := REPLACE (lv_temp_txt, '[UWYEAR]', v_uw_year);
      lv_temp_txt := REPLACE (lv_temp_txt, '[ADDRESS]', v_address);
      lv_temp_txt := REPLACE (lv_temp_txt, '[TITLE]', v_title);
      lv_temp_txt := REPLACE (lv_temp_txt, '[SURNAME]', v_clnt_name);

      IF LENGTH (NVL (TRIM (v_tel_no), '')) > 0
      THEN
         SELECT tqc_sms_code_seq.NEXTVAL
           INTO v_sms_code
           FROM DUAL;

         BEGIN
            SELECT usr_code
              INTO v_usr_code
              FROM tqc_users
             WHERE usr_username = v_user;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Unable to fetch user code for ' || v_user);
         END;

         INSERT INTO tqc_sms_messages
                     (sms_code, sms_sys_code, sms_sys_module, sms_clnt_code,
                      sms_agn_code, sms_quot_code, sms_pol_code, sms_pol_no,
                      sms_clm_no, sms_tel_no, sms_msg, sms_status,
                      sms_prepared_by, sms_prepared_date, sms_usr_code
                     )
              VALUES (v_sms_code, v_sys_code, v_sys_mod, v_clnt_code,
                      v_agn_code, v_quot_code, v_pol_code, v_pol_no,
                      v_clm_no, v_tel_no, lv_temp_txt, 'D',
                      v_user, SYSDATE, v_usr_code
                     );
      END IF;
   END;

   PROCEDURE send_sms_msg (
      v_clnt_code   IN       NUMBER,
      v_agn_code    IN       NUMBER,
      v_quot_code   IN       NUMBER,
      v_pol_code    IN       NUMBER,
      v_pol_no      IN       VARCHAR2,
      v_clm_no      IN       VARCHAR2,
      v_tel_no      IN       VARCHAR2,
      v_sms_text    IN       VARCHAR2,
      v_sys_code    IN       NUMBER,
      v_sys_mod     IN       VARCHAR2,
      v_user        IN       VARCHAR2,
      v_sms_code    OUT      NUMBER
   )
   IS
      --v_user VARCHAR2(35):= Pkg_Global_Vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
      v_usr_code         NUMBER;
      lv_temp_txt        VARCHAR2 (10000);
      v_client           VARCHAR2 (200);
      v_pol_renewal_dt   DATE;
      v_uw_year          NUMBER;
      v_address          VARCHAR2 (200);
      v_title            VARCHAR2 (20);
      v_clnt_name        VARCHAR2 (200);
   BEGIN
      lv_temp_txt := v_sms_text;

      --RAISE_ERROR('TEST'||v_clnt_code);
      BEGIN
         SELECT clnt_name || ' ' || clnt_other_names clientname,
                clnt_postal_addrs, clnt_title, clnt_name
           INTO v_client,
                v_address, v_title, v_clnt_name
           FROM tqc_clients
          WHERE clnt_code = v_clnt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      BEGIN
         SELECT pol_renewal_dt, pol_uw_year
           INTO v_pol_renewal_dt, v_uw_year
           FROM gin_policies
          WHERE pol_batch_no = v_pol_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
      lv_temp_txt :=
         REPLACE (lv_temp_txt,
                  '[RENDATE]',
                  TO_CHAR (v_pol_renewal_dt, 'DD/Mon/YYYY')
                 );
      lv_temp_txt :=
         REPLACE (lv_temp_txt,
                  '[RENEWALDATE]',
                  TO_CHAR (v_pol_renewal_dt, 'DD/Mon/YYYY')
                 );
      lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
      lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENTNAME]', v_client);
      lv_temp_txt := REPLACE (lv_temp_txt, '[UWYEAR]', v_uw_year);
      lv_temp_txt := REPLACE (lv_temp_txt, '[ADDRESS]', v_address);
      lv_temp_txt := REPLACE (lv_temp_txt, '[TITLE]', v_title);
      lv_temp_txt := REPLACE (lv_temp_txt, '[SURNAME]', v_clnt_name);

      --raise_error('v_sys_code ='||v_sys_code);
      SELECT tqc_sms_code_seq.NEXTVAL
        INTO v_sms_code
        FROM DUAL;

      BEGIN
         SELECT usr_code
           INTO v_usr_code
           FROM tqc_users
          WHERE usr_username = v_user;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Unable to fetch user code for ' || v_user);
      END;

      INSERT INTO tqc_sms_messages
                  (sms_code, sms_sys_code, sms_sys_module, sms_clnt_code,
                   sms_agn_code, sms_quot_code, sms_pol_code, sms_pol_no,
                   sms_clm_no, sms_tel_no, sms_msg, sms_status,
                   sms_prepared_by, sms_prepared_date, sms_usr_code
                  )
           VALUES (v_sms_code, v_sys_code, v_sys_mod, v_clnt_code,
                   v_agn_code, v_quot_code, v_pol_code, v_pol_no,
                   v_clm_no, v_tel_no, lv_temp_txt, 'D',
                   v_user, SYSDATE, v_usr_code
                  );
   END;

   PROCEDURE receive_sms_msg (v_tel_no IN VARCHAR2, v_sms_text IN VARCHAR2)
   IS
      v_ibx_code   NUMBER;
   BEGIN
      SELECT tqc_ibx_code_seq.NEXTVAL
        INTO v_ibx_code
        FROM DUAL;

      INSERT INTO tqc_inbox_messages
                  (ibx_code, ibx_tel_no, ibx_msg, ibx_date
                  )
           VALUES (v_ibx_code, v_tel_no, v_sms_text, SYSDATE
                  );
   END;

   PROCEDURE prompt_inbox_usr
   IS
      v_count       NUMBER;
      v_mailhost    VARCHAR2 (100);
      v_sendor      VARCHAR2 (100);
      v_recipient   VARCHAR2 (100);
   BEGIN
      BEGIN
         SELECT COUNT (*)
           INTO v_count
           FROM tqc_inbox_messages
          WHERE ibx_status = 'D';
      END;

      BEGIN
         SELECT param_value
           INTO v_mailhost
           FROM tqc_parameters
          WHERE param_name = 'MAILHOST';

         SELECT usr_email
           INTO v_sendor
           FROM tqc_users
          WHERE usr_username LIKE 'SMS%';

         SELECT usr_email
           INTO v_recipient
           FROM tqc_users
          WHERE usr_code IN (SELECT usr_per_rank_id
                               FROM tqc_users
                              WHERE usr_username LIKE 'SMS%');
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error fetching parameter..:- ' || SQLERRM (SQLCODE));
      END;

      IF NVL (v_count, 0) > 0
      THEN
         BEGIN
            sendmail
               (v_sendor,
                v_recipient,
                'YOU HAVE ' || v_count || ' MESSAGE(S)',
                   'You have '
                || v_count
                || ' message(s) in the system inbox waiting for your attendition.',
                v_mailhost,
                NULL
               );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error sending mail..:- ' || SQLERRM (SQLCODE));
         END;

         BEGIN
            UPDATE tqc_inbox_messages
               SET ibx_usr_informd = 'Y'
             WHERE ibx_status = 'D' AND ibx_usr_informd = 'D';
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error updating inbox messages..:- '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END prompt_inbox_usr;

   PROCEDURE prompt_usr_esc
   IS
      v_count            NUMBER;
      v_mailhost         VARCHAR2 (100);
      v_sendor           VARCHAR2 (100);
      v_recipient        VARCHAR2 (100);
      v_recipient_code   NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (*)
           INTO v_count
           FROM tqc_inbox_messages
          WHERE ibx_usr_informd = 'Y' AND ibx_status = 'D';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error (   'Error fetching inbox pending messages..:- '
                         || SQLERRM (SQLCODE)
                        );
      END;

      BEGIN
         SELECT param_value
           INTO v_mailhost
           FROM tqc_parameters
          WHERE param_name = 'MAILHOST';

         SELECT usr_email
           INTO v_sendor
           FROM tqc_users
          WHERE usr_username LIKE 'SMS%';

         SELECT usr_code
           INTO v_recipient_code
           FROM tqc_users
          WHERE usr_code IN (SELECT usr_per_rank_id
                               FROM tqc_users
                              WHERE usr_username LIKE 'SMS%');

         SELECT usr_email
           INTO v_recipient
           FROM tqc_users
          WHERE usr_code IN (SELECT usr_per_rank_id
                               FROM tqc_users
                              WHERE usr_code = v_recipient_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error fetching parameter..:- ' || SQLERRM (SQLCODE));
      END;

      IF NVL (v_count, 0) > 0
      THEN
         -- DO THE ESCALATION BASED ON THE SETUP;
         BEGIN
            sendmail
               (v_sendor,
                v_recipient,
                'SYSTEM PENDING SHORT MESSAGES',
                   v_count
                || ' message(s) have been pending in the system inbox waiting for attendition.'
                || ' Please can you make follow up.',
                v_mailhost,
                NULL
               );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error sending mail..:- ' || SQLERRM (SQLCODE));
         END;

         BEGIN
            UPDATE tqc_inbox_messages
               SET ibx_usr_informd = 'E'
             WHERE ibx_status = 'D' AND ibx_usr_informd = 'Y';
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error updating inbox messages..:- '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END prompt_usr_esc;

   PROCEDURE create_sms_job
   IS
      v_interval   VARCHAR2 (30);
   BEGIN
      BEGIN
         SELECT param_value
           INTO v_interval
           FROM tqc_parameters
          WHERE param_name = 'SMS_PROMPT_PERIOD';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error
                       (   'Error fetching SMS_PROMPT_PERIOD parameter ..:- '
                        || SQLERRM (SQLCODE)
                       );
      END;

      DBMS_JOB.submit (v_job,
                       'TQC_SMS_PKG.prompt_inbox_usr;',
                       SYSDATE,
                       'SYSDATE + ' || v_interval
                      );
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error creating user prompt job..:- '
                      || SQLERRM (SQLCODE)
                     );
   END create_sms_job;

   PROCEDURE create_sms_esc_job
   IS
      v_interval   VARCHAR2 (30);
   BEGIN
      BEGIN
         SELECT param_value
           INTO v_interval
           FROM tqc_parameters
          WHERE param_name = 'SMS_ESCALATION_PERIOD';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error
                       (   'Error fetching SMS_PROMPT_PERIOD parameter ..:- '
                        || SQLERRM (SQLCODE)
                       );
      END;

      DBMS_JOB.submit (v_job,
                       'TQC_SMS_PKG.PROMPT_USR_ESC;',
                       SYSDATE,
                       'SYSDATE + ' || v_interval
                      );
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error creating user prompt job..:- '
                      || SQLERRM (SQLCODE)
                     );
   END create_sms_esc_job;

   PROCEDURE remove_sms_job
   IS
   BEGIN
      BEGIN
         SELECT job
           INTO v_job
           FROM all_jobs
          WHERE what = 'TQC_SMS_PKG.prompt_inbox_usr;';
      END;

      BEGIN
         DBMS_JOB.remove (v_job);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (   'Error removing user prompt job..:- '
                         || SQLERRM (SQLCODE)
                        );
      END;
   END remove_sms_job;

   PROCEDURE remove_sms_esc_job
   IS
   BEGIN
      BEGIN
         SELECT job
           INTO v_job
           FROM all_jobs
          WHERE what = 'TQC_SMS_PKG.PROMPT_USR_ESC;';
      END;

      BEGIN
         DBMS_JOB.remove (v_job);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (   'Error removing user escalation job..:- '
                         || SQLERRM (SQLCODE)
                        );
      END;
   END remove_sms_esc_job;

   PROCEDURE sms_contact_query (
      resultset       IN OUT   sms_list_ref,
      listtype                 VARCHAR2,
      v_prod_code              NUMBER,
      v_pol_no        IN       VARCHAR2,
      v_sys_code               NUMBER,
      sms_temp_text   IN       VARCHAR2
   )
   IS
   BEGIN
      IF NVL (v_sys_code, -2000) != -2000
      THEN
         NULL;
       /*IF listtype IN ('A') THEN
       BEGIN
         OPEN resultset FOR
          SELECT DISTINCT AGN_NAME,AGN_SMS_TEL,AGN_CODE, NULL POL_POLICY_NO,
         Lms_Ord_Misc.Process_Msg(AGN_CODE,NULL,sms_temp_text,listtype)MSG_TEXT
          FROM TQC_AGENCIES;
          --WHERE ;
         EXCEPTION
          WHEN OTHERS THEN
            RAISE_ERROR('ERROR: '||SQLERRM(SQLCODE));
       END;
      ELSIF listtype IN ('C') THEN
       BEGIN
        OPEN resultset FOR
         SELECT CLNT_SURNAME||' '||CLNT_OTHER_NAMES,CLNT_SMS_TEL,CLNT_CODE,NULL POL_POLICY_NO,
         Lms_Ord_Misc.Process_Msg(CLNT_CODE,NULL,sms_temp_text,listtype)MSG_TEXT
         FROM TQC_CLIENTS;
       EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('ERROR: '||SQLERRM(SQLCODE));
       END;
      END IF;*/
      ELSIF v_sys_code = '27'
      THEN
         NULL;
      /*IF listtype IN ('A') THEN
       BEGIN
         OPEN resultset FOR
          SELECT DISTINCT AGN_NAME,AGN_SMS_TEL,AGN_CODE,NULL POL_POLICY_NO,
          Lms_Ord_Misc.Process_Msg(AGN_CODE,NULL,sms_temp_text,listtype)MSG_TEXT
          FROM TQC_AGENCIES,LMS_POLICY_ENDORSEMENTS,LMS_POLICIES
          WHERE POL_PROD_CODE = DECODE(NVL(v_prod_code,-2000),-2000,POL_PROD_CODE,v_prod_code)
          AND POL_POLICY_NO = DECODE(NVL(v_pol_no,'-2000'),'-2000',POL_POLICY_NO,v_pol_no)
          AND AGN_CODE = ENDR_AGEN_CODE
          AND POL_CURRENT_ENDR_CODE = endr_code;
         EXCEPTION
          WHEN OTHERS THEN
            RAISE_ERROR('ERROR: '||SQLERRM(SQLCODE));
       END;
      ELSIF listtype IN ('C') THEN
       BEGIN
        OPEN resultset FOR
         SELECT CLNT_SURNAME||' '||CLNT_OTHER_NAMES,CLNT_SMS_TEL,CLNT_CODE,POL_POLICY_NO,
         Lms_Ord_Misc.Process_Msg(CLNT_CODE,NULL,sms_temp_text,listtype)MSG_TEXT
         FROM TQC_CLIENTS,LMS_POLICIES
         WHERE POL_PROD_CODE = DECODE(NVL(v_prod_code,-2000),-2000,POL_PROD_CODE,v_prod_code)
         AND POL_POLICY_NO = DECODE(NVL(v_pol_no,'-2000'),'-2000',POL_POLICY_NO,v_pol_no)
         AND POL_CLIENT_PRP_CODE = CLNT_CODE;
       EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('ERROR: '||SQLERRM(SQLCODE));
       END;
      END IF;*/
      ELSIF v_sys_code = '37'
      THEN
         NULL;
      /*IF listtype IN ('A') THEN
        BEGIN
          OPEN resultset FOR
           SELECT DISTINCT AGN_NAME,AGN_SMS_TEL,AGN_CODE,POL_BATCH_NO,
           Lms_Ord_Misc.Process_Msg(AGN_CODE,NULL,sms_temp_text,listtype)MSG_TEXT
           FROM TQC_AGENCIES,GIN_POLICIES
           WHERE POL_PRO_CODE = v_prod_code
           AND AGN_CODE = POL_AGNT_AGENT_CODE;
          EXCEPTION
           WHEN OTHERS THEN
             RAISE_ERROR('ERROR: '||SQLERRM(SQLCODE));
        END;
       ELSIF listtype IN ('C') THEN
        BEGIN
         OPEN resultset FOR
          SELECT CLNT_SURNAME||' '||CLNT_OTHER_NAMES,CLNT_SMS_TEL,CLNT_CODE,POL_BATCH_NO,
          Lms_Ord_Misc.Process_Msg(CLNT_CODE,NULL,sms_temp_text,listtype)MSG_TEXT
          FROM TQC_CLIENTS,GIN_POLICIES
          WHERE POL_PRO_CODE = v_prod_code
          AND POL_PRP_CODE = CLNT_CODE;
        EXCEPTION
         WHEN OTHERS THEN
           RAISE_ERROR('ERROR: '||SQLERRM(SQLCODE));
        END;
       END IF;
      ELSE
         RAISE_ERROR('ERROR: '||SQLERRM(SQLCODE));
         */
      END IF;
   END sms_contact_query;

   PROCEDURE create_sms_xml (v_sms_code IN NUMBER DEFAULT NULL)
   IS
      doc          xmldom.domdocument;
      main_node    xmldom.domnode;
      root_node    xmldom.domnode;
      user_node    xmldom.domnode;
      item_node    xmldom.domnode;
      item_node1   xmldom.domnode;
      item_node2   xmldom.domnode;
      item_node3   xmldom.domnode;
      root_elmt    xmldom.domelement;
      item_elmt    xmldom.domelement;
      item_elmt1   xmldom.domelement;
      item_text    xmldom.domtext;

      CURSOR get_smsmessages
      IS
         SELECT sms_tel_no, sms_msg, sms_code, ROWNUM
           FROM tqc_sms_messages
          WHERE sms_status = 'R';
   --AND SMS_CODE = DECODE(NVL(v_sms_code,-2000),-2000,SMS_CODE,v_sms_code);
   BEGIN
      -- get document
      doc := xmldom.newdomdocument;
      -- create root element
      main_node := xmldom.makenode (doc);
      root_elmt := xmldom.createelement (doc, 'db:InputParameters');
      xmldom.setattribute
                (root_elmt,
                 'xmlns:db',
                 'http://xmlns.oracle.com/pcbpel/adapter/db/AKI/ADDMESSAGES/'
                );
      -- xmldom.setAttribute(root_elmt, 'xmlns:xsi', 'http://www.w3.org/2001/XMLSchema-instance');
      -- xmldom.setAttribute(root_elmt, 'xsi:schemaLocation', 'http://www.akadia.com/xml/soug/xmldom/emp.xsd');
      root_node := xmldom.appendchild (main_node, xmldom.makenode (root_elmt));
      item_elmt := xmldom.createelement (doc, 'SMSP');
      user_node := xmldom.appendchild (root_node, xmldom.makenode (item_elmt));

      FOR get_users_rec IN get_smsmessages ()
      LOOP
         -- create user element with rownum as attribute
         item_elmt := xmldom.createelement (doc, 'SMSP_ITEM');
         item_node :=
                  xmldom.appendchild (user_node, xmldom.makenode (item_elmt));
         -- create user element: EMP_NO
         item_elmt := xmldom.createelement (doc, 'TRN_NUM');
         item_node1 :=
                  xmldom.appendchild (item_node, xmldom.makenode (item_elmt));
         item_text := xmldom.createtextnode (doc, get_users_rec.sms_code);
         item_node2 :=
                 xmldom.appendchild (item_node1, xmldom.makenode (item_text));
------------------------------------------
/*
item_elmt := xmldom.createElement(
       doc
     , 'MSG'
   );
   item_node2 := xmldom.appendChild(
       item_node
     , xmldom.makeNode(item_elmt)
   );

*/
-- create user element: NAME
         item_elmt := xmldom.createelement (doc, 'MESSAGE');
         item_node2 :=
                  xmldom.appendchild (item_node, xmldom.makenode (item_elmt));
         item_text := xmldom.createtextnode (doc, get_users_rec.sms_msg);
         item_node1 :=
                 xmldom.appendchild (item_node2, xmldom.makenode (item_text));
         -- create user element: DEPT_NO
         item_elmt := xmldom.createelement (doc, 'TELNUM');
         item_node3 :=
                  xmldom.appendchild (item_node, xmldom.makenode (item_elmt));
         item_text := xmldom.createtextnode (doc, get_users_rec.sms_tel_no);
         item_node :=
                 xmldom.appendchild (item_node3, xmldom.makenode (item_text));
      END LOOP;

      -- write document to file using default character set from database
      xmldom.writetofile (doc, '/tmp/docSample.xml');
      -- free resources
      xmldom.freedocument (doc);
   -- END;

   -- deal with exceptions
   EXCEPTION
      WHEN xmldom.index_size_err
      THEN
         raise_application_error (-20120, 'Index Size error');
      WHEN xmldom.domstring_size_err
      THEN
         raise_application_error (-20120, 'String Size error');
      WHEN xmldom.hierarchy_request_err
      THEN
         raise_application_error (-20120, 'Hierarchy request error');
      WHEN xmldom.wrong_document_err
      THEN
         raise_application_error (-20120, 'Wrong doc error');
      WHEN xmldom.invalid_character_err
      THEN
         raise_application_error (-20120, 'Invalid Char error');
      WHEN xmldom.no_data_allowed_err
      THEN
         raise_application_error (-20120, 'Nod data allowed error');
      WHEN xmldom.no_modification_allowed_err
      THEN
         raise_application_error (-20120, 'No mod allowed error');
      WHEN xmldom.not_found_err
      THEN
         raise_application_error (-20120, 'Not found error');
      WHEN xmldom.not_supported_err
      THEN
         raise_application_error (-20120, 'Not supported error');
      WHEN xmldom.inuse_attribute_err
      THEN
         raise_application_error (-20120, 'In use attr error');
   END;

   PROCEDURE ftp_file
   IS
      p_localpath           VARCHAR2 (250) := '/tmp';
      p_filename            VARCHAR2 (250) := 'docSample.xml';
      p_hostname            VARCHAR2 (250) := 'arusha.turnkeyafrica.com';
      p_username            VARCHAR2 (250) := 'smsuser';
-- We probably should change this so that it's fed in from the procedure variables
      p_password            VARCHAR2 (250) := 'smsuser123';
      -- Same to this...
      p_remotepath          VARCHAR2 (250) := '/xmlinbound';
      p_remotename          VARCHAR2 (250) := 'docxml.xml';
      p_status              VARCHAR2 (250);
      -- TODO If status is successful delete the file
      p_error_message       VARCHAR2 (250);
      -- Find away of showing this error on the screen
      p_bytes_transmitted   NUMBER;
      p_trans_start         DATE;    --- Time stamp of when the data was send
      p_trans_end           DATE;
      p_filetype            VARCHAR2 (250) := 'BINARY';
      p_port                PLS_INTEGER    := 21;
   BEGIN
      utl_ftp.put (p_localpath,
                   p_filename,
                   p_hostname,
                   p_username,
                   p_password,
                   p_remotepath,
                   p_remotename,
                   p_status,
                   p_error_message,
                   p_bytes_transmitted,
                   p_trans_start,
                   p_trans_end,
                   p_filetype,
                   p_port
                  );
      DBMS_OUTPUT.put_line (p_status);
      DBMS_OUTPUT.put_line (p_error_message);
      DBMS_OUTPUT.put_line (p_bytes_transmitted);
      DBMS_OUTPUT.put_line (p_trans_start);
      DBMS_OUTPUT.put_line (p_trans_end);
   END;

   PROCEDURE account_tpye (
      resultset    IN OUT   account_type_ref,
      v_act_code   IN       NUMBER
   )
   IS
   BEGIN
      IF v_act_code = -1
      THEN
         BEGIN
            OPEN resultset FOR
               SELECT DISTINCT agn_code, agn_name, agn_email_address,
                               agn_sms_tel
                          FROM tqc_agencies;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR: ' || SQLERRM (SQLCODE));
         END;
      ELSE
         BEGIN
            OPEN resultset FOR
               SELECT DISTINCT agn_code, agn_name, agn_email_address,
                               agn_sms_tel
                          FROM tqc_agencies
                         WHERE agn_act_code = v_act_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR: ' || SQLERRM (SQLCODE));
         END;
      END IF;
   END account_tpye;

   FUNCTION get_sms (
      v_sms_type    IN   VARCHAR2,
      v_clnt_code   IN   NUMBER DEFAULT -2000,
      v_agn_code    IN   NUMBER DEFAULT -2000,
      v_pol_code    IN   NUMBER DEFAULT -2000,
      v_quot_code   IN   NUMBER DEFAULT -2000,
      v_clm_no      IN   VARCHAR2 DEFAULT 'ALL',
      v_sys_code    IN   NUMBER DEFAULT -2000
   )
      RETURN sms_ref
   IS
      v_retcur   sms_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT sms_code, sms_sys_code, sys_name, sms_sys_module,
                sms_clnt_code,
                LTRIM (RTRIM (clnt_name || ' ' || clnt_other_names)),
                sms_agn_code, agn_name, sms_pol_code, sms_pol_no, sms_clm_no,
                sms_tel_no, sms_msg, sms_status, sms_prepared_by,
                sms_prepared_date, sms_send_date, sms_quot_code, sms_quot_no,
                sms_usr_code
           FROM tqc_sms_messages, tqc_systems, tqc_clients, tqc_agencies
          WHERE sys_code(+) = sms_sys_code
            AND clnt_code(+) = sms_clnt_code
            AND agn_code(+) = sms_agn_code
            AND sms_status = v_sms_type
            AND sms_clnt_code =
                   DECODE (NVL (v_clnt_code, -2000),
                           -2000, sms_clnt_code,
                           v_clnt_code
                          )
            AND sms_agn_code =
                   DECODE (NVL (v_agn_code, -2000),
                           -2000, sms_agn_code,
                           v_agn_code
                          )
            AND sms_pol_code =
                   DECODE (NVL (v_pol_code, -2000),
                           -2000, sms_pol_code,
                           v_pol_code
                          )
            AND sms_quot_code =
                   DECODE (NVL (v_quot_code, -2000),
                           -2000, sms_quot_code,
                           v_quot_code
                          )
            AND sms_clm_no =
                     DECODE (NVL (v_clm_no, -2000),
                             -2000, sms_clm_no,
                             v_clm_no
                            )
            AND sms_sys_code =
                   DECODE (NVL (v_sys_code, -2000),
                           -2000, sms_sys_code,
                           v_sys_code
                          );

      RETURN (v_retcur);
   END;

   PROCEDURE getsmsquotation (v_sms_quotation OUT smsquotationrec)
   IS
   BEGIN
      OPEN v_sms_quotation FOR
         SELECT   quot_code, quot_no
             FROM gin_quotations
         ORDER BY 1;
   END;

   FUNCTION get_sms_templates (
      v_sys_code     IN   NUMBER,
      v_sys_module   IN   VARCHAR2,
      v_msgt_type    IN   VARCHAR2
   )
      RETURN sms_template_ref
   IS
      v_retcur   sms_template_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg
           FROM tqc_msg_templates
          WHERE msgt_sys_code = v_sys_code
            AND msgt_sys_module = v_sys_module
            AND msgt_type = v_msgt_type;

      RETURN (v_retcur);
   END;

   PROCEDURE alerttype_prc (
      v_addedit                  VARCHAR2,
      v_alerttype_tab   IN       tqc_alert_types_tab,
      v_err             OUT      VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_alerttype_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No alert Type Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_alerttype_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_alert_types
             WHERE alrt_code = v_alerttype_tab (i).alrt_code;

            IF v_count > 0
            THEN
               raise_error ('Record with Id Exists!');
            END IF;

            INSERT INTO tqc_alert_types
                        (alrt_code,
                         alrt_type,
                         alrt_sys_code
                        )
                 VALUES (tqc_alrt_code_seq.NEXTVAL,
                         v_alerttype_tab (i).alrt_type,
                         v_alerttype_tab (i).alrt_sys_code
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_alert_types
               SET alrt_type = v_alerttype_tab (i).alrt_type,
                   alrt_sys_code = v_alerttype_tab (i).alrt_sys_code
             WHERE alrt_code = v_alerttype_tab (i).alrt_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_alert_types
                  WHERE alrt_code = v_alerttype_tab (i).alrt_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Alert Types!');
   END alerttype_prc;

   PROCEDURE get_alerttype (
      v_refcur          OUT      alert_type_ref,
      v_alrt_sys_code   IN       NUMBER,
      v_shtdesc         IN       VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      --Raise_error('org code '||v_alrt_sys_code);
      OPEN v_refcur FOR
         SELECT alrt_code, alrt_type, alrt_sys_code, alrt_email, alrt_sms,
                sys_name, alrt_screen, alrt_email_msgt_code,
                alrt_sms_msgt_code, alrt_grp_usr_code, a.msgt_msg email,
                b.msgt_msg sms, usr_name grp_user, alrt_check_alert,
                alrt_sht_desc
           FROM tqc_alert_types,
                tqc_systems,
                tqc_msg_templates a,
                tqc_msg_templates b,
                tqc_users
          WHERE alrt_sys_code = sys_code
            AND alrt_email_msgt_code = a.msgt_code(+)
            AND alrt_sms_msgt_code = b.msgt_code(+)
            AND alrt_grp_usr_code = usr_code(+)
            AND alrt_sht_desc = NVL (v_shtdesc, alrt_sht_desc)
            AND alrt_sys_code = v_alrt_sys_code;
   END;

   PROCEDURE alert_prc (
      v_addedit              VARCHAR2,
      v_alert_tab   IN       tqc_alerts_tab,
      v_err         OUT      VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_alert_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No alert Type Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_alert_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            INSERT INTO tqc_alerts
                        (alrts_code,
                         alrts_alrt_code,
                         alrts_sys_code,
                         alrts_agn_code,
                         alrts_clnt_code,
                         alrts_description,
                         alrts_date,
                         alrts_period,
                         alrts_user_code,
                         alrts_dest_type,
                         alrts_dest_code,
                         alrts_msgt_code,
                         alrts_status,
                         alrts_sht_desc
                        )
                 VALUES (tqc_alrt_code_seq.NEXTVAL,
                         v_alert_tab (i).alrts_alrt_code,
                         v_alert_tab (i).alrts_sys_code,
                         v_alert_tab (i).alrts_agn_code,
                         v_alert_tab (i).alrts_clnt_code,
                         v_alert_tab (i).alrts_description,
                         v_alert_tab (i).alrts_date,
                         v_alert_tab (i).alrts_period,
                         v_alert_tab (i).alrts_user_code,
                         v_alert_tab (i).alrts_dest_type,
                         v_alert_tab (i).alrts_dest_code,
                         v_alert_tab (i).alrts_msgt_code,
                         v_alert_tab (i).alrts_status,
                         v_alert_tab (i).alrts_sht_desc
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_alerts
               SET alrts_code = v_alert_tab (i).alrts_code,
                   alrts_alrt_code = v_alert_tab (i).alrts_alrt_code,
                   alrts_sys_code = v_alert_tab (i).alrts_sys_code,
                   alrts_agn_code = v_alert_tab (i).alrts_agn_code,
                   alrts_clnt_code = v_alert_tab (i).alrts_clnt_code,
                   alrts_description = v_alert_tab (i).alrts_description,
                   alrts_date = v_alert_tab (i).alrts_date,
                   alrts_period = v_alert_tab (i).alrts_period,
                   alrts_user_code = v_alert_tab (i).alrts_user_code,
                   alrts_dest_type = v_alert_tab (i).alrts_dest_type,
                   alrts_dest_code = v_alert_tab (i).alrts_dest_code,
                   alrts_msgt_code = v_alert_tab (i).alrts_msgt_code,
                   alrts_status = v_alert_tab (i).alrts_status,
                   alrts_sht_desc = v_alert_tab (i).alrts_sht_desc
             WHERE alrts_code = v_alert_tab (i).alrts_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_alerts
                  WHERE alrts_code = v_alert_tab (i).alrts_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Alert Types!');
   END alert_prc;

   PROCEDURE get_alert (v_alert_code IN NUMBER, v_refcur OUT alert_ref)
   IS
   BEGIN
      -- Raise_error('org code '||v_org_code);
      OPEN v_refcur FOR
         SELECT alrts_code, alrts_alrt_code, alrts_sys_code, alrts_agn_code,
                alrts_clnt_code, alrts_description, alrts_date, alrts_period,
                alrts_user_code, alrts_dest_type, alrts_dest_code,
                alrts_msgt_code,
                getagency_name (alrts_dest_code, alrts_dest_type)
                                                                 agency_name,
                msgt_sht_desc,
                account_type_name (alrts_dest_type) acc_type_name,
                alrts_status, alrts_sht_desc
           FROM tqc_alerts, tqc_msg_templates
          WHERE alrts_alrt_code = v_alert_code
                AND alrts_msgt_code = msgt_code;
   END;

   FUNCTION getagency_name (v_agn_code NUMBER, v_acc_type VARCHAR2)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      IF v_acc_type = 'CL'
      THEN
         SELECT clnt_name
           INTO v_ret_val
           FROM tqc_clients
          WHERE clnt_code = v_agn_code;

         RETURN v_ret_val;
      ELSIF v_acc_type = 'SP'
      THEN
         SELECT spr_name
           INTO v_ret_val
           FROM tqc_service_providers
          WHERE spr_code = v_agn_code;

         RETURN v_ret_val;
      ELSIF v_acc_type = 'U'
      THEN
         SELECT usr_name
           INTO v_ret_val
           FROM tqc_users
          WHERE usr_code = v_agn_code;

         RETURN v_ret_val;
      ELSE
         SELECT agn_name
           INTO v_ret_val
           FROM tqc_agencies
          WHERE agn_code = v_agn_code;

         RETURN v_ret_val;
      END IF;
   END getagency_name;

   FUNCTION account_type_name (v_sht_desc VARCHAR2)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      IF v_sht_desc = 'SP'
      THEN
         v_ret_val := 'SERVICE PROVIDER';
         RETURN v_ret_val;
      ELSIF v_sht_desc = 'CL'
      THEN
         v_ret_val := 'CLIENT';
         RETURN v_ret_val;
      ELSIF v_sht_desc = 'U'
      THEN
         v_ret_val := 'USER';
         RETURN v_ret_val;
      ELSE
         SELECT act_account_type
           INTO v_ret_val
           FROM tqc_account_types
          WHERE act_type_sht_desc = v_sht_desc;

         RETURN v_ret_val;
      END IF;
   END account_type_name;

   FUNCTION getbirthdayalerts (v_sht_desc tqc_alerts.alrts_sht_desc%TYPE)
      RETURN alertmsg_ref
   IS
      v_cursor   alertmsg_ref;
      v_type     VARCHAR2 (4);
      param      tqc_parameters.param_value%TYPE;
   BEGIN
      SELECT tqc_parameters_pkg.get_param_varchar ('EMAILS_FROM')
        INTO param
        FROM DUAL;

      SELECT alrts_dest_type
        INTO v_type
        FROM tqc_alerts
       WHERE alrts_sht_desc = v_sht_desc;

      IF v_type = 'AC'
      THEN
         OPEN v_cursor FOR
            SELECT clnt_sht_desc,
                   TO_CHAR (clnt_name || ' ' || clnt_other_names),
                      TO_CHAR (clnt_dob, 'DD/MM')
                   || '/'
                   || TO_CHAR (SYSDATE, 'YYYY'),
                   clnt_email_addrs, msgt_msg, alrt_type, usr_name, usr_email,
                   param
              FROM tqc_clients,
                   tqc_alerts,
                   tqc_alert_types,
                   tqc_msg_templates,
                   tqc_users
             WHERE clnt_acc_officer = usr_code
               AND alrts_alrt_code = alrt_code
               AND alrts_msgt_code = msgt_code
               AND alrts_sht_desc = v_sht_desc
               AND alrts_status = 'A'
               AND TO_CHAR (clnt_dob - alrts_period, 'DD/MM') =
                                                    TO_CHAR (SYSDATE, 'DD/MM');
      ELSE
         OPEN v_cursor FOR
            SELECT clnt_sht_desc,
                   TO_CHAR (clnt_name || ' ' || clnt_other_names),
                      TO_CHAR (clnt_dob, 'DD/MM')
                   || '/'
                   || TO_CHAR (SYSDATE, 'YYYY'),
                   clnt_email_addrs, msgt_msg, alrt_type, usr_name, usr_email,
                   param
              FROM tqc_clients,
                   tqc_alerts,
                   tqc_alert_types,
                   tqc_msg_templates,
                   tqc_users
             WHERE usr_code = alrts_dest_code
               AND alrts_alrt_code = alrt_code
               AND alrts_msgt_code = msgt_code
               AND alrts_sht_desc = v_sht_desc
               AND alrts_status = 'A'
               AND TO_CHAR (clnt_dob - alrts_period, 'DD/MM') =
                                                    TO_CHAR (SYSDATE, 'DD/MM');
      END IF;

      RETURN v_cursor;
   END;

   FUNCTION getstockalerts (
      v_sht_desc    tqc_alerts.alrts_sht_desc%TYPE,
      v_item_code   NUMBER
   )
      RETURN stockalertmsg_ref
   IS
      v_cursor   stockalertmsg_ref;
      v_type     VARCHAR2 (4);
      param      tqc_parameters.param_value%TYPE;
   BEGIN
      ---rAISE_error('v_item_code--'||v_item_code||'v_sht_desc--'||v_sht_desc);
      SELECT tqc_parameters_pkg.get_param_varchar ('EMAILS_FROM')
        INTO param
        FROM DUAL;

      SELECT alrts_dest_type
        INTO v_type
        FROM tqc_alerts
       WHERE alrts_sht_desc = v_sht_desc;

      IF v_type = 'U'
      THEN
         OPEN v_cursor FOR
            SELECT   itm_description, itm_max_level, itm_reodr_level,
                     SUM (stc_quantity), unt_description, msgt_msg,
                     alrt_type, usr_name, usr_email, param
                FROM siv_items,
                     siv_units,
                     siv_stock,
                     tqc_alerts,
                     tqc_alert_types,
                     tqc_msg_templates,
                     tqc_users
               WHERE unt_code = itm_unt_code
                 AND usr_code = alrts_dest_code
                 AND alrts_alrt_code = alrt_code
                 AND alrts_msgt_code = msgt_code
                 AND alrts_sht_desc = v_sht_desc
                 AND alrts_status = 'A'
                 AND itm_quantity <= itm_reodr_level
                 AND itm_code = stc_itm_code
                 AND itm_code = v_item_code
            GROUP BY itm_description,
                     itm_max_level,
                     itm_reodr_level,
                     unt_description,
                     msgt_msg,
                     alrt_type,
                     usr_name,
                     usr_email,
                     param;

         RETURN v_cursor;
      END IF;
   END;

   PROCEDURE updatesmsmessage (
      v_smscode      NUMBER,
      v_clientcode   NUMBER,
      v_telno        VARCHAR2,
      v_message      VARCHAR2,
      v_usercode     NUMBER
   )
   IS
   BEGIN
      UPDATE tqc_sms_messages
         SET sms_clnt_code = v_clientcode,
             sms_tel_no = v_telno,
             sms_msg = v_message
       WHERE sms_code = v_smscode;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Message!');
   END updatesmsmessage;

   PROCEDURE sendprovsms (
      v_sms_sys_code      IN       NUMBER,
      v_sms_sys_module    IN       VARCHAR2,
      v_sms_clnt_code     IN       NUMBER,
      v_sms_agn_code      IN       NUMBER,
      v_sms_quot_code     IN       NUMBER,
      v_sms_pol_code      IN       NUMBER,
      v_sms_pol_no        IN       VARCHAR2,
      v_sms_clm_no        IN       VARCHAR2,
      v_sms_tel_no        IN       VARCHAR2,
      v_sms_msg           IN       VARCHAR2,
      v_sms_status        IN       VARCHAR2,
      v_sms_prepared_by   IN       VARCHAR2,
      v_sms_quot_no       IN       VARCHAR2,
      v_smscode           OUT      NUMBER
   )
   IS
      --v_user VARCHAR2(35):= Pkg_Global_Vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
      v_usr_code   NUMBER;
   BEGIN
      -- raise_error('v_sys_code ='||v_agn_code);
      SELECT tqc_sms_code_seq.NEXTVAL
        INTO v_smscode
        FROM DUAL;

      BEGIN
         SELECT usr_code
           INTO v_usr_code
           FROM tqc_users
          WHERE usr_username = v_sms_prepared_by;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Unable to fetch user code for ' || v_sms_prepared_by
                        );
      END;

      INSERT INTO tqc_sms_messages
                  (sms_code, sms_sys_code, sms_sys_module,
                   sms_clnt_code, sms_agn_code, sms_quot_code,
                   sms_pol_code, sms_pol_no, sms_clm_no, sms_tel_no,
                   sms_msg, sms_status, sms_prepared_by, sms_prepared_date,
                   sms_usr_code, sms_quot_no
                  )
           VALUES (v_smscode, v_sms_sys_code, NVL (v_sms_sys_module, 'R'),
                   v_sms_clnt_code, NVL (v_sms_agn_code, 0), v_sms_quot_code,
                   v_sms_pol_code, v_sms_pol_no, v_sms_clm_no, v_sms_tel_no,
                   v_sms_msg, v_sms_status, v_sms_prepared_by, SYSDATE,
                   v_usr_code, v_sms_quot_no
                  );
   END;

   PROCEDURE sendserviceprov (
      v_add_edit               IN   VARCHAR2,
      v_alrt_code              IN   NUMBER,
      v_alrt_type              IN   VARCHAR,
      v_alrt_sys_code          IN   NUMBER,
      v_alrt_email             IN   VARCHAR,
      v_alrt_sms               IN   VARCHAR,
      v_alrt_screen            IN   VARCHAR,
      v_alrt_email_msgt_code   IN   NUMBER,
      v_alrt_sms_msgt_code     IN   NUMBER,
      v_alrt_grp_usr_code      IN   NUMBER,
      v_alrt_check_alert       IN   VARCHAR2,
      v_shtdesc                IN   VARCHAR2
   )
   IS
   BEGIN
--RAISE_ERROR('v_add_edit'||v_add_edit||'v_alrt_code'||v_alrt_code);
      IF NVL (v_add_edit, 'S') = 'A'
      THEN
         INSERT INTO tqc_alert_types
                     (alrt_code, alrt_type,
                      alrt_sys_code, alrt_email, alrt_sms,
                      alrt_screen, alrt_email_msgt_code,
                      alrt_sms_msgt_code, alrt_grp_usr_code,
                      alrt_check_alert, alrt_sht_desc
                     )
              VALUES (tqc_alrt_code_seq.NEXTVAL, v_alrt_type,
                      v_alrt_sys_code, v_alrt_email, v_alrt_sms,
                      v_alrt_screen, v_alrt_email_msgt_code,
                      v_alrt_sms_msgt_code, v_alrt_grp_usr_code,
                      v_alrt_check_alert, v_shtdesc
                     );
      ELSIF NVL (v_add_edit, 'S') = 'E'
      THEN
         UPDATE tqc_alert_types
            SET alrt_type = v_alrt_type,
                alrt_sys_code = v_alrt_sys_code,
                alrt_email = v_alrt_email,
                alrt_sms = v_alrt_sms,
                alrt_screen = v_alrt_screen,
                alrt_email_msgt_code = v_alrt_email_msgt_code,
                alrt_sms_msgt_code = v_alrt_sms_msgt_code,
                alrt_grp_usr_code = v_alrt_grp_usr_code,
                alrt_check_alert = v_alrt_check_alert,
                alrt_sht_desc = v_shtdesc
          WHERE alrt_code = v_alrt_code;
      ELSIF NVL (v_add_edit, 'S') = 'D'
      THEN
         DELETE      tqc_alert_types
               WHERE alrt_code = v_alrt_code;
      END IF;
   END;

   FUNCTION getnewalerts (v_alrts_prepared_by IN VARCHAR2, v_cnt OUT NUMBER)
      RETURN alerts_ref
   IS
      v_cursor   alerts_ref;
   BEGIN
      SELECT COUNT (*)
        INTO v_cnt
        FROM tqc_alerts
       WHERE alrts_prepared_by = v_alrts_prepared_by AND alrts_status = 'N';

      OPEN v_cursor FOR
         SELECT alrts_description
           FROM tqc_alerts
          WHERE alrts_prepared_by = v_alrts_prepared_by AND alrts_status = 'N';

      RETURN v_cursor;
   END;

   PROCEDURE updatealerts (v_alrts_prepared_by IN VARCHAR2)
   IS
   BEGIN
      UPDATE tqc_alerts
         SET alrts_status = 'R'
       WHERE alrts_prepared_by = v_alrts_prepared_by;

      COMMIT;
   END;

   PROCEDURE updateviewedalerts (
      v_alrts_prepared_by   IN   VARCHAR2,
      v_status              IN   VARCHAR2,
      v_alrts_code          IN   NUMBER
   )
   IS
   BEGIN
      UPDATE tqc_alerts
         SET alrts_status = v_status
       WHERE alrts_code = v_alrts_code;

      COMMIT;
   END;

   PROCEDURE updateviewedalerts (
      v_alrts_prepared_by   IN   VARCHAR2,
      v_status              IN   VARCHAR2
   )
   IS
   BEGIN
      UPDATE tqc_alerts
         SET alrts_status = v_status
       WHERE alrts_prepared_by = v_alrts_prepared_by AND alrts_status != 'R';

      COMMIT;
   END;

   FUNCTION getalluseralerts (v_alrts_prepared_by IN VARCHAR2)
      RETURN alluseralerts_ref
   IS
      v_cursor   alluseralerts_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT alrts_code, alrts_alrt_code, agn_name, clnt_name,
                alrts_description, alrts_date, alrts_period,
                DECODE (alrts_status,
                        'N', 'Not Read',
                        'V', 'Not Read',
                        'R', 'Read'
                       ) alrts_status,
                msgt_msg, alrts_screen, alrts_prepared_by,
                alrts_prepare_date, alrts_quot_no, alrts_pol_policy_no,
                alrts_claim_no
           FROM tqc_alerts, tqc_agencies, tqc_clients, tqc_msg_templates
          WHERE clnt_code(+) = alrts_clnt_code
            AND agn_code(+) = alrts_agn_code
            AND msgt_code(+) = alrts_msgt_code
            AND alrts_prepared_by = v_alrts_prepared_by;

      RETURN v_cursor;
   END;

   PROCEDURE sendserviceprovsms (
      v_sms_sys_code      IN   NUMBER,
      v_sms_sys_module    IN   VARCHAR2,
      v_sms_clnt_code     IN   NUMBER,
      v_sms_agn_code      IN   NUMBER,
      v_sms_quot_code     IN   NUMBER,
      v_sms_pol_code      IN   NUMBER,
      v_sms_pol_no        IN   VARCHAR2,
      v_sms_clm_no        IN   VARCHAR2,
      v_sms_tel_no        IN   VARCHAR2,
      v_sms_msg           IN   VARCHAR2,
      v_sms_status        IN   VARCHAR2,
      v_sms_prepared_by   IN   VARCHAR2,
      v_sms_quot_no       IN   VARCHAR2
   )
   IS
      --v_user VARCHAR2(35):= Pkg_Global_Vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
      v_sms_code   NUMBER;
      v_usr_code   NUMBER;
   BEGIN
      -- raise_error('v_sys_code ='||v_agn_code);
      SELECT tqc_sms_code_seq.NEXTVAL
        INTO v_sms_code
        FROM DUAL;

      BEGIN
         SELECT usr_code
           INTO v_usr_code
           FROM tqc_users
          WHERE usr_username = v_sms_prepared_by;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Unable to fetch user code for ' || v_sms_prepared_by
                        );
      END;

      INSERT INTO tqc_sms_messages
                  (sms_code, sms_sys_code, sms_sys_module,
                   sms_clnt_code, sms_agn_code, sms_quot_code,
                   sms_pol_code, sms_pol_no, sms_clm_no, sms_tel_no,
                   sms_msg, sms_status, sms_prepared_by, sms_prepared_date,
                   sms_usr_code, sms_quot_no
                  )
           VALUES (v_sms_code, v_sms_sys_code, v_sms_sys_module,
                   v_sms_clnt_code, v_sms_agn_code, v_sms_quot_code,
                   v_sms_pol_code, v_sms_pol_no, v_sms_clm_no, v_sms_tel_no,
                   v_sms_msg, v_sms_status, v_sms_prepared_by, SYSDATE,
                   v_usr_code, v_sms_quot_no
                  );
   END;
   PROCEDURE send_instant_sms(v_sms_code NUMBER)
IS
   esendex_username   CONSTANT VARCHAR2 (40)   := 'EagleAfrica';
   -- 'your_username';
   esendex_password   CONSTANT VARCHAR2 (40)   := 'Efide@gleAfr1';
   -- 'your_password';
   esendex_account    CONSTANT VARCHAR2 (40)   := '+254724090528';
   -- 'your_account';--
   vrequest                    UTL_HTTP.req;
   vposttext                   VARCHAR2 (500);
   vresponse                   UTL_HTTP.resp;
   vresponsetext               VARCHAR2 (2000);
   verrortext                  VARCHAR2 (2000);
   v_url                       VARCHAR2 (2000);
   v_username                  VARCHAR2 (100);
   v_password                  VARCHAR2 (100);
   v_source                    VARCHAR2 (100);
   v_cnt                       NUMBER;
   v_success_msg               VARCHAR2(50);

   CURSOR cur_sms
   IS
      SELECT *
        FROM tqc_sms_messages
       WHERE sms_code = v_sms_code;
BEGIN
       --raise_error('v_url='||v_url||'v_username='||'v_password ='||v_password||'v_source='||v_source);
   SELECT SUBSTR (tss_url, 1, 2008), tss_username, tss_password, tss_source
    INTO v_url, v_username, v_password, v_source
     FROM tqc_system_sms;

   FOR cur_sms_rec IN cur_sms
   LOOP
    begin
   
----------------------------------------------------------------------------
   -- Build text for the post action.
   -- For a field description, see
   -- http://www.esendex.com/secure/messenger/formpost/SendSMS.aspx
   ----------------------------------------------------------------------------
      vposttext :=
            'messageID=00001'
         || CHR (38)
         || 'user='
         --|| utl_url.ESCAPE (esendex_username, TRUE)
         || utl_url.ESCAPE (v_username, TRUE)
         || CHR (38)
         || 'pass='
         || utl_url.ESCAPE (v_password, TRUE)
         --|| utl_url.ESCAPE (esendex_password, TRUE)
         || CHR (38)
         || 'shortCode='
         || utl_url.ESCAPE (v_source, TRUE)
         --|| utl_url.ESCAPE (esendex_account, TRUE)
         || CHR (38)
         || 'MSISDN='
         --|| utl_url.ESCAPE (precipient, TRUE)
         || utl_url.ESCAPE (cur_sms_rec.sms_tel_no, TRUE)
         || CHR (38)
         || 'MESSAGE='
         || utl_url.ESCAPE (cur_sms_rec.sms_msg, TRUE);
      -- || utl_url.ESCAPE (pbody, TRUE);
      DBMS_OUTPUT.put_line ('vPostText=' || vposttext);
      
      --raise_error(vposttext);
----------------------------------------------------------------------------

      -- if you need to set a proxy, uncomment next line.
----------------------------------------------------------------------------

      /* Utl_Http.set_proxy('proxy.it.my-company.com', 'my-company.com'); */
----------------------------------------------------------------------------
-- Send SMS through the Esendex SMS service.
----------------------------------------------------------------------------
--    vRequest := Utl_Http.begin_request
--                  ( url    => 'http://www.esendex.com/secure/messenger/formpost/SendSMS.aspx'
--                  , method => 'POST'
--                  );

      --   vrequest :=
--      UTL_HTTP.begin_request
--         (url         => 'http://172.16.4.84:9501/api?action=sendmessage&username=c2buser&password=c2buser123&recipient=0725954981&messagetype=SMS:TEXT&messagedata=HIISMSINAWORK',
--          method      => 'POST'
--         );
--v_url:=replace('http://172.16.4.84:9501/api?action=sendmessage&username=c2buser&password=c2buser123&recipient='|| cur_sms_rec.sms_tel_no ||'&messagetype=SMS:TEXT&messagedata='||cur_sms_rec.sms_msg,' ','%20');
--raise_error(v_url);
    v_url := REPLACE(v_url, '[USERNAME]', v_username);
    v_url := REPLACE(v_url, '[PASSWORD]', v_password);
    v_url := REPLACE(v_url, '[SOURCE]', v_source);
    v_url := REPLACE(v_url, '[DESTINATION]', replace(cur_sms_rec.sms_tel_no,'+',''));
    v_url := REPLACE(v_url, '[MESSAGE]', cur_sms_rec.sms_msg);
    v_url := REPLACE(v_url,' ','%20');
--raise_error(v_url);
      vrequest :=
         UTL_HTTP.begin_request
            (
              v_url,
             method      => 'POST'
            );
            
      UTL_HTTP.set_header (r          => vrequest,
                           NAME       => 'Content-Type',
                           VALUE      => 'application/x-www-form-urlencoded'
                          );
      UTL_HTTP.set_header (r          => vrequest,
                           NAME       => 'Content-Length',
                           VALUE      => LENGTH (vposttext)
                          );
      UTL_HTTP.write_text (r => vrequest, DATA => vposttext);
      vresponse := UTL_HTTP.get_response (vrequest);
      
     
      
      DBMS_OUTPUT.put_line ('status_code=' || vresponse.status_code);

      IF vresponse.status_code = '200'
      THEN
         UTL_HTTP.read_text (vresponse, vresponsetext);
         
         UPDATE tqc_sms_messages
            SET sms_status = 'OK',SMS_SEND_DATE = SYSDATE--,SMS_SENT_RESPONSE=verrortext
          WHERE sms_code = cur_sms_rec.sms_code;
      ELSE
         verrortext :=
               'HTTP status: '
            || vresponse.status_code
            || '-'
            || vresponse.reason_phrase;
            
            
             UPDATE tqc_sms_messages
            SET  sms_sent_response=verrortext
          WHERE sms_code = cur_sms_rec.sms_code;
         raise_application_error (-20001,
                                  'Sending SMS failed with ' || verrortext
                                 );
      END IF;

      --
      UTL_HTTP.end_response (vresponse);
      --
   --   DBMS_OUTPUT.put_line ('vErrorText=' || verrortext);
 
       -- IF verrortext IS NOT NULL
      
       
      
        
     
      v_cnt:=NVL(v_cnt,0)+1;
--      exception 
--      when others then
--       raise_error('error sending sms...'||SQLERRM(SQLCODE)); 
      
      end;
   END LOOP;
   RETURN;
  IF NVL(v_cnt,0)=0 THEN
    RETURN;
  END IF;
  
-- exception
--  when others then
--    raise_error('error sending sms...'||SQLERRM(SQLCODE));  
   
END;
END tqc_sms_pkg010416; 
/