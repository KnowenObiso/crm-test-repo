--
-- TQC_SMS_PKG030215  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.Tqc_Sms_Pkg030215
AS
   PROCEDURE RAISE_ERROR (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         RAISE_APPLICATION_ERROR (-20033,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         RAISE_APPLICATION_ERROR (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      END IF;
   END RAISE_ERROR;

   PROCEDURE CREATE_SMS_MSG (v_reciepient   IN     VARCHAR2,
                             v_clnt_code    IN     NUMBER,
                             v_agn_code     IN     NUMBER,
                             v_quot_code    IN     NUMBER,
                             v_quot_no      IN     VARCHAR2,
                             v_pol_code     IN     NUMBER,
                             v_pol_no       IN     VARCHAR2,
                             v_clm_no       IN     VARCHAR2,
                             v_msgt_code    IN     NUMBER,
                             v_sys_code     IN     NUMBER,
                             v_sys_mod      IN     VARCHAR2,
                             v_user         IN     VARCHAR2,
                             v_sms_code     IN OUT NUMBER,
                             v_app_lvl      IN     VARCHAR2,
                            v_usr_code IN NUMBER)
   IS
      v_sms_tel   VARCHAR2 (35);
      v_sms_msg   VARCHAR2 (500);
      v_errmsg    VARCHAR2 (400);
      v_usr_type VARCHAR2 (35);
   BEGIN
      IF v_reciepient = 'C'
      THEN
         BEGIN
            SELECT CLNT_SMS_TEL
              INTO v_sms_tel
              FROM TQC_CLIENTS
             WHERE CLNT_CODE = v_clnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               RAISE_ERROR ('Error client not defined..');
         END;
      ELSIF v_reciepient = 'A'
      THEN
         BEGIN
            SELECT AGN_SMS_TEL
              INTO v_sms_tel
              FROM TQC_AGENCIES
             WHERE AGN_CODE = v_agn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               RAISE_ERROR ('Error agent not defined..');
         END;
      ELSIF v_reciepient = 'U' THEN
        BEGIN
        SELECT USR_TYPE
        INTO v_usr_type
        FROM TQC_USERS
        WHERE USR_CODE =v_clnt_code;
        EXCEPTION
            WHEN OTHERS THEN
              v_usr_type :='U';
        END;
        IF v_usr_type = 'U' THEN 
            SELECT USR_CELL_PHONE_NO
            INTO v_sms_tel
            FROM TQC_USERS
            WHERE USR_CODE =v_clnt_code;
        ELSE  -- HANDLE GROUP USERS
         NULL;
        END IF;
      END IF;

      BEGIN
         SELECT MSGT_MSG
           INTO v_sms_msg
           FROM TQC_MSG_TEMPLATES
          WHERE MSGT_CODE = v_msgt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE_ERROR ('Template selected not found..');
      END;

      BEGIN
         v_sms_msg :=  Tqc_Memos_Dbpkg.PROCESS_GIS_POL_MEMO (               --v_pol_code,
                                                                                                  v_quot_code,
                                                                                                  v_clm_no,
                                                                                                  NULL,
                                                                                                  v_sms_msg,
                                                                                                  v_app_lvl);
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      BEGIN
         SEND_SMS_MSG (v_clnt_code,
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
                       v_sms_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE_ERROR ('Error creating sms message');
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

   PROCEDURE SEND_SMS_MSG (v_clnt_code   IN NUMBER,
                           v_agn_code    IN NUMBER,
                           v_quot_code   IN NUMBER,
                           v_pol_code    IN NUMBER,
                           v_pol_no      IN VARCHAR2,
                           v_clm_no      IN VARCHAR2,
                           v_tel_no      IN VARCHAR2,
                           v_sms_text    IN VARCHAR2,
                           v_sys_code    IN NUMBER,
                           v_sys_mod     IN VARCHAR2,
                           v_user        IN VARCHAR2)
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

      IF LENGTH (NVL (TRIM (v_tel_no), '')) > 0
      THEN
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
      END IF;
   END;

   PROCEDURE SEND_SMS_MSG (v_clnt_code   IN     NUMBER,
                           v_agn_code    IN     NUMBER,
                           v_quot_code   IN     NUMBER,
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

      --RAISE_ERROR('TEST'||v_clnt_code);

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

      --raise_error('v_sys_code ='||v_sys_code);
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

   PROCEDURE Receive_Sms_Msg (v_tel_no IN VARCHAR2, v_sms_text IN VARCHAR2)
   IS
      v_ibx_code   NUMBER;
   BEGIN
      SELECT TQC_IBX_CODE_SEQ.NEXTVAL INTO v_ibx_code FROM DUAL;

      INSERT INTO TQC_INBOX_MESSAGES (IBX_CODE,
                                      IBX_TEL_NO,
                                      IBX_MSG,
                                      IBX_DATE)
           VALUES (v_ibx_code,
                   v_tel_no,
                   v_sms_text,
                   SYSDATE);
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
           FROM TQC_INBOX_MESSAGES
          WHERE IBX_STATUS = 'D';
      END;

      BEGIN
         SELECT param_value
           INTO v_mailhost
           FROM TQC_PARAMETERS
          WHERE param_name = 'MAILHOST';

         SELECT usr_email
           INTO v_sendor
           FROM TQC_USERS
          WHERE usr_username LIKE 'SMS%';

         SELECT usr_email
           INTO v_recipient
           FROM TQC_USERS
          WHERE USR_CODE IN (SELECT USR_PER_RANK_ID
                               FROM TQC_USERS
                              WHERE usr_username LIKE 'SMS%');
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE_ERROR (
               'Error fetching parameter..:- ' || SQLERRM (SQLCODE));
      END;

      IF NVL (v_count, 0) > 0
      THEN
         BEGIN
            Sendmail (
               v_sendor,
               v_recipient,
               'YOU HAVE ' || v_count || ' MESSAGE(S)',
               'You have ' || v_count
               || ' message(s) in the system inbox waiting for your attendition.',
               v_mailhost,
               NULL);
         EXCEPTION
            WHEN OTHERS
            THEN
               RAISE_ERROR ('Error sending mail..:- ' || SQLERRM (SQLCODE));
         END;

         BEGIN
            UPDATE TQC_INBOX_MESSAGES
               SET IBX_USR_INFORMD = 'Y'
             WHERE IBX_STATUS = 'D' AND IBX_USR_INFORMD = 'D';
         EXCEPTION
            WHEN OTHERS
            THEN
               RAISE_ERROR (
                  'Error updating inbox messages..:- ' || SQLERRM (SQLCODE));
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
           FROM TQC_INBOX_MESSAGES
          WHERE IBX_USR_INFORMD = 'Y' AND IBX_STATUS = 'D';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            RAISE_ERROR (
               'Error fetching inbox pending messages..:- '
               || SQLERRM (SQLCODE));
      END;

      BEGIN
         SELECT param_value
           INTO v_mailhost
           FROM TQC_PARAMETERS
          WHERE param_name = 'MAILHOST';

         SELECT usr_email
           INTO v_sendor
           FROM TQC_USERS
          WHERE usr_username LIKE 'SMS%';

         SELECT USR_CODE
           INTO v_recipient_code
           FROM TQC_USERS
          WHERE USR_CODE IN (SELECT USR_PER_RANK_ID
                               FROM TQC_USERS
                              WHERE usr_username LIKE 'SMS%');

         SELECT usr_email
           INTO v_recipient
           FROM TQC_USERS
          WHERE USR_CODE IN (SELECT USR_PER_RANK_ID
                               FROM TQC_USERS
                              WHERE USR_CODE = v_recipient_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE_ERROR (
               'Error fetching parameter..:- ' || SQLERRM (SQLCODE));
      END;

      IF NVL (v_count, 0) > 0
      THEN
         -- DO THE ESCALATION BASED ON THE SETUP;
         BEGIN
            Sendmail (
               v_sendor,
               v_recipient,
               'SYSTEM PENDING SHORT MESSAGES',
               v_count
               || ' message(s) have been pending in the system inbox waiting for attendition.'
               || ' Please can you make follow up.',
               v_mailhost,
               NULL);
         EXCEPTION
            WHEN OTHERS
            THEN
               RAISE_ERROR ('Error sending mail..:- ' || SQLERRM (SQLCODE));
         END;

         BEGIN
            UPDATE TQC_INBOX_MESSAGES
               SET IBX_USR_INFORMD = 'E'
             WHERE IBX_STATUS = 'D' AND IBX_USR_INFORMD = 'Y';
         EXCEPTION
            WHEN OTHERS
            THEN
               RAISE_ERROR (
                  'Error updating inbox messages..:- ' || SQLERRM (SQLCODE));
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
           FROM TQC_PARAMETERS
          WHERE param_name = 'SMS_PROMPT_PERIOD';
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE_ERROR (
               'Error fetching SMS_PROMPT_PERIOD parameter ..:- '
               || SQLERRM (SQLCODE));
      END;

      DBMS_JOB.SUBMIT (v_job,
                       'TQC_SMS_PKG.prompt_inbox_usr;',
                       SYSDATE,
                       'SYSDATE + ' || v_interval);
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE_ERROR (
            'Error creating user prompt job..:- ' || SQLERRM (SQLCODE));
   END create_sms_job;

   PROCEDURE create_sms_esc_job
   IS
      v_interval   VARCHAR2 (30);
   BEGIN
      BEGIN
         SELECT param_value
           INTO v_interval
           FROM TQC_PARAMETERS
          WHERE param_name = 'SMS_ESCALATION_PERIOD';
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE_ERROR (
               'Error fetching SMS_PROMPT_PERIOD parameter ..:- '
               || SQLERRM (SQLCODE));
      END;

      DBMS_JOB.SUBMIT (v_job,
                       'TQC_SMS_PKG.PROMPT_USR_ESC;',
                       SYSDATE,
                       'SYSDATE + ' || v_interval);
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE_ERROR (
            'Error creating user prompt job..:- ' || SQLERRM (SQLCODE));
   END create_sms_esc_job;

   PROCEDURE remove_sms_job
   IS
   BEGIN
      BEGIN
         SELECT job
           INTO v_job
           FROM ALL_JOBS
          WHERE what = 'TQC_SMS_PKG.prompt_inbox_usr;';
      END;

      BEGIN
         DBMS_JOB.REMOVE (v_job);
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE_ERROR (
               'Error removing user prompt job..:- ' || SQLERRM (SQLCODE));
      END;
   END remove_sms_job;

   PROCEDURE remove_sms_esc_job
   IS
   BEGIN
      BEGIN
         SELECT job
           INTO v_job
           FROM ALL_JOBS
          WHERE what = 'TQC_SMS_PKG.PROMPT_USR_ESC;';
      END;

      BEGIN
         DBMS_JOB.REMOVE (v_job);
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE_ERROR (
               'Error removing user escalation job..:- ' || SQLERRM (SQLCODE));
      END;
   END remove_sms_esc_job;

   PROCEDURE sms_contact_query (resultset       IN OUT sms_list_ref,
                                listtype               VARCHAR2,
                                v_prod_code            NUMBER,
                                v_pol_no        IN     VARCHAR2,
                                v_sys_code             NUMBER,
                                sms_temp_text   IN     VARCHAR2)
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
      doc          xmldom.DOMDocument;
      main_node    xmldom.DOMNode;
      root_node    xmldom.DOMNode;
      user_node    xmldom.DOMNode;
      item_node    xmldom.DOMNode;
      item_node1   xmldom.DOMNode;
      item_node2   xmldom.DOMNode;
      item_node3   xmldom.DOMNode;
      root_elmt    xmldom.DOMElement;
      item_elmt    xmldom.DOMElement;
      item_elmt1   xmldom.DOMElement;
      item_text    xmldom.DOMText;

      CURSOR get_smsmessages
      IS
         SELECT SMS_TEL_NO,
                SMS_MSG,
                SMS_CODE,
                ROWNUM
           FROM TQC_SMS_MESSAGES
          WHERE SMS_STATUS = 'R';
   --AND SMS_CODE = DECODE(NVL(v_sms_code,-2000),-2000,SMS_CODE,v_sms_code);
   BEGIN
      -- get document
      doc := xmldom.newDOMDocument;
      -- create root element
      main_node := xmldom.makeNode (doc);
      root_elmt := xmldom.createElement (doc, 'db:InputParameters');
      xmldom.setAttribute (
         root_elmt,
         'xmlns:db',
         'http://xmlns.oracle.com/pcbpel/adapter/db/AKI/ADDMESSAGES/');
      -- xmldom.setAttribute(root_elmt, 'xmlns:xsi', 'http://www.w3.org/2001/XMLSchema-instance');
      -- xmldom.setAttribute(root_elmt, 'xsi:schemaLocation', 'http://www.akadia.com/xml/soug/xmldom/emp.xsd');
      root_node := xmldom.appendChild (main_node, xmldom.makeNode (root_elmt));
      item_elmt := xmldom.createElement (doc, 'SMSP');
      user_node := xmldom.appendChild (root_node, xmldom.makeNode (item_elmt));

      FOR get_users_rec IN get_smsmessages ()
      LOOP
         -- create user element with rownum as attribute
         item_elmt := xmldom.createElement (doc, 'SMSP_ITEM');
         item_node :=
            xmldom.appendChild (user_node, xmldom.makeNode (item_elmt));
         -- create user element: EMP_NO
         item_elmt := xmldom.createElement (doc, 'TRN_NUM');
         item_node1 :=
            xmldom.appendChild (item_node, xmldom.makeNode (item_elmt));
         item_text := xmldom.createTextNode (doc, get_users_rec.SMS_CODE);
         item_node2 :=
            xmldom.appendChild (item_node1, xmldom.makeNode (item_text));
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

         item_elmt := xmldom.createElement (doc, 'MESSAGE');
         item_node2 :=
            xmldom.appendChild (item_node, xmldom.makeNode (item_elmt));
         item_text := xmldom.createTextNode (doc, get_users_rec.SMS_MSG);
         item_node1 :=
            xmldom.appendChild (item_node2, xmldom.makeNode (item_text));


         -- create user element: DEPT_NO
         item_elmt := xmldom.createElement (doc, 'TELNUM');
         item_node3 :=
            xmldom.appendChild (item_node, xmldom.makeNode (item_elmt));
         item_text := xmldom.createTextNode (doc, get_users_rec.SMS_TEL_NO);
         item_node :=
            xmldom.appendChild (item_node3, xmldom.makeNode (item_text));
      END LOOP;

      -- write document to file using default character set from database
      xmldom.writeToFile (doc, '/tmp/docSample.xml');
      -- free resources
      xmldom.freeDocument (doc);
   -- END;



   -- deal with exceptions
   EXCEPTION
      WHEN xmldom.INDEX_SIZE_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'Index Size error');
      WHEN xmldom.DOMSTRING_SIZE_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'String Size error');
      WHEN xmldom.HIERARCHY_REQUEST_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'Hierarchy request error');
      WHEN xmldom.WRONG_DOCUMENT_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'Wrong doc error');
      WHEN xmldom.INVALID_CHARACTER_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'Invalid Char error');
      WHEN xmldom.NO_DATA_ALLOWED_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'Nod data allowed error');
      WHEN xmldom.NO_MODIFICATION_ALLOWED_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'No mod allowed error');
      WHEN xmldom.NOT_FOUND_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'Not found error');
      WHEN xmldom.NOT_SUPPORTED_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'Not supported error');
      WHEN xmldom.INUSE_ATTRIBUTE_ERR
      THEN
         RAISE_APPLICATION_ERROR (-20120, 'In use attr error');
   END;

   PROCEDURE ftp_file
   IS
      p_localpath           VARCHAR2 (250) := '/tmp';
      p_filename            VARCHAR2 (250) := 'docSample.xml';
      p_hostname            VARCHAR2 (250) := 'arusha.turnkeyafrica.com';
      p_username            VARCHAR2 (250) := 'smsuser'; -- We probably should change this so that it's fed in from the procedure variables
      p_password            VARCHAR2 (250) := 'smsuser123'; -- Same to this...
      p_remotepath          VARCHAR2 (250) := '/xmlinbound';
      p_remotename          VARCHAR2 (250) := 'docxml.xml';
      p_status              VARCHAR2 (250); -- TODO If status is successful delete the file
      p_error_message       VARCHAR2 (250); -- Find away of showing this error on the screen
      p_bytes_transmitted   NUMBER;
      p_trans_start         DATE;     --- Time stamp of when the data was send
      p_trans_end           DATE;
      p_filetype            VARCHAR2 (250) := 'BINARY';
      p_port                PLS_INTEGER := 21;
   BEGIN
      Utl_Ftp.PUT (p_localpath,
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
                   p_port);

      DBMS_OUTPUT.PUT_LINE (p_status);
      DBMS_OUTPUT.PUT_LINE (p_error_message);
      DBMS_OUTPUT.PUT_LINE (p_bytes_transmitted);
      DBMS_OUTPUT.PUT_LINE (p_trans_start);
      DBMS_OUTPUT.PUT_LINE (p_trans_end);
   END;

   PROCEDURE account_tpye (resultset    IN OUT account_type_ref,
                           v_act_code   IN     NUMBER)
   IS
   BEGIN
      IF v_act_code = -1
      THEN
         BEGIN
            OPEN resultset FOR
               SELECT DISTINCT AGN_CODE,
                               AGN_NAME,
                               AGN_EMAIL_ADDRESS,
                               AGN_SMS_TEL
                 FROM TQC_AGENCIES;
         EXCEPTION
            WHEN OTHERS
            THEN
               RAISE_ERROR ('ERROR: ' || SQLERRM (SQLCODE));
         END;
      ELSE
         BEGIN
            OPEN resultset FOR
               SELECT DISTINCT AGN_CODE,
                               AGN_NAME,
                               AGN_EMAIL_ADDRESS,
                               AGN_SMS_TEL
                 FROM TQC_AGENCIES
                WHERE AGN_ACT_CODE = v_act_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               RAISE_ERROR ('ERROR: ' || SQLERRM (SQLCODE));
         END;
      END IF;
   END account_tpye;

   FUNCTION get_sms (v_sms_type    IN VARCHAR2,
                     v_clnt_code   IN NUMBER DEFAULT -2000,
                     v_agn_code    IN NUMBER DEFAULT -2000,
                     v_pol_code    IN NUMBER DEFAULT -2000,
                     v_quot_code   IN NUMBER DEFAULT -2000,
                     v_clm_no      IN VARCHAR2 DEFAULT 'ALL',
                     v_sys_code    IN NUMBER DEFAULT -2000)
      RETURN sms_ref
   IS
      v_retcur   sms_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT SMS_CODE,
                SMS_SYS_CODE,
                SYS_NAME,
                SMS_SYS_MODULE,
                SMS_CLNT_CODE,
                LTRIM (RTRIM (clnt_name || ' ' || clnt_other_names)),
                SMS_AGN_CODE,
                agn_name,
                SMS_POL_CODE,
                SMS_POL_NO,
                SMS_CLM_NO,
                SMS_TEL_NO,
                SMS_MSG,
                SMS_STATUS,
                SMS_PREPARED_BY,
                SMS_PREPARED_DATE,
                SMS_SEND_DATE,
                SMS_QUOT_CODE,
                SMS_QUOT_NO,
                SMS_USR_CODE
           FROM TQC_SMS_MESSAGES,
                TQC_SYSTEMS,
                TQC_CLIENTS,
                TQC_AGENCIES
          WHERE     SYS_CODE(+) = SMS_SYS_CODE
                AND CLNT_CODE(+) = SMS_CLNT_CODE
                AND agn_CODE(+) = SMS_agn_CODE
                AND SMS_STATUS = v_sms_type
                AND SMS_CLNT_CODE =
                       DECODE (NVL (v_clnt_code, -2000),
                               -2000, SMS_CLNT_CODE,
                               v_clnt_code)
                AND SMS_AGN_CODE =
                       DECODE (NVL (v_agn_code, -2000),
                               -2000, SMS_AGN_CODE,
                               v_agn_code)
                AND SMS_POL_CODE =
                       DECODE (NVL (v_pol_code, -2000),
                               -2000, SMS_POL_CODE,
                               v_pol_code)
                AND SMS_QUOT_CODE =
                       DECODE (NVL (v_quot_code, -2000),
                               -2000, SMS_QUOT_CODE,
                               v_quot_code)
                AND SMS_CLM_NO =
                       DECODE (NVL (v_clm_no, -2000),
                               -2000, SMS_CLM_NO,
                               v_clm_no)
                AND SMS_SYS_CODE =
                       DECODE (NVL (v_sys_code, -2000),
                               -2000, SMS_SYS_CODE,
                               v_sys_code);

      RETURN (v_retcur);
   END;

   PROCEDURE getSmsQuotation (v_sms_quotation OUT smsQuotationRec)
   IS
   BEGIN
      OPEN v_sms_quotation FOR
           SELECT QUOT_CODE, QUOT_NO
             FROM GIN_QUOTATIONS
         ORDER BY 1;
   END;

   FUNCTION get_sms_templates (v_sys_code     IN NUMBER,
                               v_sys_module   IN VARCHAR2,
                               v_msgt_type    IN VARCHAR2)
      RETURN sms_template_ref
   IS
      v_retcur   sms_template_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT MSGT_CODE, MSGT_SHT_DESC, MSGT_MSG
           FROM TQC_MSG_TEMPLATES
          WHERE     MSGT_SYS_CODE = v_sys_code
                AND MSGT_SYS_MODULE = v_sys_module
                AND MSGT_TYPE = v_msgt_type;

      RETURN (v_retcur);
   END;

   PROCEDURE alertType_prc (v_addEdit                VARCHAR2,
                            v_alertType_tab   IN     TQC_ALERT_TYPES_TAB,
                            v_err                OUT VARCHAR2)
   IS
      v_count   NUMBER;
   BEGIN
      IF v_alertType_tab.COUNT = 0
      THEN
         raise_error (
            'Error occured. No alert Type Provided : ' || SQLERRM (SQLCODE));
      END IF;

      FOR I IN 1 .. v_alertType_tab.COUNT
      LOOP
         IF v_addEdit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM TQC_ALERT_TYPES
             WHERE ALRT_CODE = v_alertType_tab (I).ALRT_CODE;

            IF v_count > 0
            THEN
               raise_error ('Record with Id Exists!');
            END IF;


            INSERT INTO TQC_ALERT_TYPES (ALRT_CODE, ALRT_TYPE, ALRT_SYS_CODE)
                 VALUES (
                           TQC_ALRT_CODE_SEQ.NEXTVAL,
                           v_alertType_tab (I).ALRT_TYPE,
                           v_alertType_tab (I).ALRT_SYS_CODE);
         ELSIF v_addEdit = 'E'
         THEN
            UPDATE TQC_ALERT_TYPES
               SET ALRT_TYPE = v_alertType_tab (I).ALRT_TYPE,
                   ALRT_SYS_CODE = v_alertType_tab (I).ALRT_SYS_CODE
             WHERE ALRT_CODE = v_alertType_tab (I).ALRT_CODE;
         ELSIF v_addEdit = 'D'
         THEN
            DELETE TQC_ALERT_TYPES
             WHERE ALRT_CODE = v_alertType_tab (I).ALRT_CODE;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Alert Types!');
   END alertType_prc;

    PROCEDURE get_AlertType(    
                                v_refcur  OUT  alert_type_ref,
                                v_alrt_sys_code in NUMBER
                                ) IS
 BEGIN
 --Raise_error('org code '||v_alrt_sys_code);
    OPEN v_refcur FOR
     SELECT ALRT_CODE, ALRT_TYPE, ALRT_SYS_CODE, ALRT_EMAIL, ALRT_SMS,SYS_NAME ,
     ALRT_SCREEN,ALRT_EMAIL_MSGT_CODE,ALRT_SMS_MSGT_CODE,ALRT_GRP_USR_CODE,
     A.MSGT_MSG EMAIL,B.MSGT_MSG SMS,USR_NAME GRP_USER,
     ALRT_CHECK_ALERT
    FROM TQC_ALERT_TYPES,TQC_SYSTEMS ,
    TQC_MSG_TEMPLATES A,TQC_MSG_TEMPLATES B,TQC_USERS
    WHERE ALRT_SYS_CODE=SYS_CODE
    AND ALRT_EMAIL_MSGT_CODE=A.MSGT_CODE(+)
    AND ALRT_SMS_MSGT_CODE=B.MSGT_CODE(+) 
    AND ALRT_GRP_USR_CODE = USR_CODE(+)
    AND ALRT_SYS_CODE=v_alrt_sys_code;
    
 END ;

   PROCEDURE alert_prc (v_addEdit            VARCHAR2,
                        v_alert_tab   IN     TQC_ALERTS_TAB,
                        v_err            OUT VARCHAR2)
   IS
      v_count   NUMBER;
   BEGIN
      IF v_alert_tab.COUNT = 0
      THEN
         raise_error (
            'Error occured. No alert Type Provided : ' || SQLERRM (SQLCODE));
      END IF;

      FOR I IN 1 .. v_alert_tab.COUNT
      LOOP
         IF v_addEdit = 'A'
         THEN
            INSERT INTO TQC_ALERTS (ALRTS_CODE,
                                    ALRTS_ALRT_CODE,
                                    ALRTS_SYS_CODE,
                                    ALRTS_AGN_CODE,
                                    ALRTS_CLNT_CODE,
                                    ALRTS_DESCRIPTION,
                                    ALRTS_DATE,
                                    ALRTS_PERIOD,
                                    ALRTS_USER_CODE,
                                    ALRTS_DEST_TYPE,
                                    ALRTS_DEST_CODE,
                                    ALRTS_MSGT_CODE,
                                    ALRTS_STATUS,
                                    ALRTS_SHT_DESC)
                 VALUES (TQC_ALRT_CODE_SEQ.NEXTVAL,
                         v_alert_tab (I).ALRTS_ALRT_CODE,
                         v_alert_tab (I).ALRTS_SYS_CODE,
                         v_alert_tab (I).ALRTS_AGN_CODE,
                         v_alert_tab (I).ALRTS_CLNT_CODE,
                         v_alert_tab (I).ALRTS_DESCRIPTION,
                         v_alert_tab (I).ALRTS_DATE,
                         v_alert_tab (I).ALRTS_PERIOD,
                         v_alert_tab (I).ALRTS_USER_CODE,
                         v_alert_tab (I).ALRTS_DEST_TYPE,
                         v_alert_tab (I).ALRTS_DEST_CODE,
                         v_alert_tab (I).ALRTS_MSGT_CODE,
                         v_alert_tab (I).ALRTS_STATUS,
                         v_alert_tab (I).ALRTS_SHT_DESC);
         ELSIF v_addEdit = 'E'
         THEN
            UPDATE TQC_ALERTS
               SET ALRTS_CODE = v_alert_tab (I).ALRTS_CODE,
                   ALRTS_ALRT_CODE = v_alert_tab (I).ALRTS_ALRT_CODE,
                   ALRTS_SYS_CODE = v_alert_tab (I).ALRTS_SYS_CODE,
                   ALRTS_AGN_CODE = v_alert_tab (I).ALRTS_AGN_CODE,
                   ALRTS_CLNT_CODE = v_alert_tab (I).ALRTS_CLNT_CODE,
                   ALRTS_DESCRIPTION = v_alert_tab (I).ALRTS_DESCRIPTION,
                   ALRTS_DATE = v_alert_tab (I).ALRTS_DATE,
                   ALRTS_PERIOD = v_alert_tab (I).ALRTS_PERIOD,
                   ALRTS_USER_CODE = v_alert_tab (I).ALRTS_USER_CODE,
                   ALRTS_DEST_TYPE = v_alert_tab (I).ALRTS_DEST_TYPE,
                   ALRTS_DEST_CODE = v_alert_tab (I).ALRTS_DEST_CODE,
                   ALRTS_MSGT_CODE = v_alert_tab (I).ALRTS_MSGT_CODE,
                   ALRTS_STATUS = v_alert_tab (I).ALRTS_STATUS,
                   ALRTS_SHT_DESC = v_alert_tab (I).ALRTS_SHT_DESC
             WHERE ALRTS_CODE = v_alert_tab (I).ALRTS_CODE;
         ELSIF v_addEdit = 'D'
         THEN
            DELETE TQC_ALERTS
             WHERE ALRTS_CODE = v_alert_tab (I).ALRTS_CODE;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Alert Types!');
   END alert_prc;

   PROCEDURE get_Alert (v_alert_code IN NUMBER, v_refcur OUT alert_ref)
   IS
   BEGIN
      -- Raise_error('org code '||v_org_code);
      OPEN v_refcur FOR
         SELECT ALRTS_CODE,
                ALRTS_ALRT_CODE,
                ALRTS_SYS_CODE,
                ALRTS_AGN_CODE,
                ALRTS_CLNT_CODE,
                ALRTS_DESCRIPTION,
                ALRTS_DATE,
                ALRTS_PERIOD,
                ALRTS_USER_CODE,
                ALRTS_DEST_TYPE,
                ALRTS_DEST_CODE,
                ALRTS_MSGT_CODE,
                GETAGENCY_NAME (ALRTS_DEST_CODE, ALRTS_DEST_TYPE) AGENCY_NAME,
                MSGT_SHT_DESC,
                ACCOUNT_TYPE_NAME (ALRTS_DEST_TYPE) ACC_TYPE_NAME,
                ALRTS_STATUS,
                ALRTS_SHT_DESC
           FROM TQC_ALERTS, TQC_MSG_TEMPLATES
          WHERE ALRTS_ALRT_CODE = v_alert_code
                AND ALRTS_MSGT_CODE = MSGT_CODE;
   END;

   FUNCTION GETAGENCY_NAME (v_agn_code NUMBER, v_acc_type VARCHAR2)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      IF v_acc_type = 'CL'
      THEN
         SELECT CLNT_NAME
           INTO v_ret_val
           FROM TQC_CLIENTS
          WHERE CLNT_CODE = v_agn_code;

         RETURN v_ret_val;
      ELSIF v_acc_type = 'SP'
      THEN
         SELECT SPR_NAME
           INTO v_ret_val
           FROM TQC_SERVICE_PROVIDERS
          WHERE SPR_CODE = v_agn_code;

         RETURN v_ret_val;
      ELSIF v_acc_type = 'U'
      THEN
         SELECT USR_NAME
           INTO v_ret_val
           FROM TQC_USERS
          WHERE USR_CODE = v_agn_code;

         RETURN v_ret_val;
      ELSE
         SELECT AGN_NAME
           INTO v_ret_val
           FROM TQC_AGENCIES
          WHERE AGN_CODE = v_agn_code;

         RETURN v_ret_val;
      END IF;
   END GETAGENCY_NAME;

   FUNCTION ACCOUNT_TYPE_NAME (v_sht_desc VARCHAR2)
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
         SELECT ACT_ACCOUNT_TYPE
           INTO v_ret_val
           FROM TQC_ACCOUNT_TYPES
          WHERE ACT_TYPE_SHT_DESC = v_sht_desc;

         RETURN v_ret_val;
      END IF;
   END ACCOUNT_TYPE_NAME;

   FUNCTION getBirthDayAlerts (v_sht_desc TQC_ALERTS.ALRTS_SHT_DESC%TYPE)
      RETURN alertMSG_ref
   IS
      v_cursor   alertMSG_ref;
      v_type     VARCHAR2 (4);
      param      TQC_PARAMETERS.PARAM_VALUE%TYPE;
   BEGIN
      SELECT TQC_PARAMETERS_PKG.get_param_varchar ('EMAILS_FROM')
        INTO param
        FROM DUAL;

      SELECT ALRTS_DEST_TYPE
        INTO v_type
        FROM TQC_ALERTS
       WHERE ALRTS_SHT_DESC = v_sht_desc;

      IF v_type = 'AC'
      THEN
         OPEN v_cursor FOR
            SELECT CLNT_SHT_DESC,
                   TO_CHAR (CLNT_NAME || ' ' || CLNT_OTHER_NAMES),
                      TO_CHAR (CLNT_DOB, 'DD/MM')
                   || '/'
                   || TO_CHAR (SYSDATE, 'YYYY'),
                   CLNT_EMAIL_ADDRS,
                   MSGT_MSG,
                   ALRT_TYPE,
                   USR_NAME,
                   USR_EMAIL,
                   param
              FROM tqc_clients,
                   TQC_ALERTS,
                   TQC_ALERT_TYPES,
                   TQC_MSG_TEMPLATES,
                   TQC_USERS
             WHERE     CLNT_ACC_OFFICER = USR_CODE
                   AND ALRTS_ALRT_CODE = ALRT_CODE
                   AND ALRTS_MSGT_CODE = MSGT_CODE
                   AND ALRTS_SHT_DESC = v_sht_desc
                   AND ALRTS_STATUS = 'A'
                   AND TO_CHAR (CLNT_DOB - ALRTS_PERIOD, 'DD/MM') =
                          TO_CHAR (SYSDATE, 'DD/MM');
      ELSE
         OPEN v_cursor FOR
            SELECT CLNT_SHT_DESC,
                   TO_CHAR (CLNT_NAME || ' ' || CLNT_OTHER_NAMES),
                      TO_CHAR (CLNT_DOB, 'DD/MM')
                   || '/'
                   || TO_CHAR (SYSDATE, 'YYYY'),
                   CLNT_EMAIL_ADDRS,
                   MSGT_MSG,
                   ALRT_TYPE,
                   USR_NAME,
                   USR_EMAIL,
                   param
              FROM tqc_clients,
                   TQC_ALERTS,
                   TQC_ALERT_TYPES,
                   TQC_MSG_TEMPLATES,
                   TQC_USERS
             WHERE     USR_CODE = ALRTS_DEST_CODE
                   AND ALRTS_ALRT_CODE = ALRT_CODE
                   AND ALRTS_MSGT_CODE = MSGT_CODE
                   AND ALRTS_SHT_DESC = v_sht_desc
                   AND ALRTS_STATUS = 'A'
                   AND TO_CHAR (CLNT_DOB - ALRTS_PERIOD, 'DD/MM') =
                          TO_CHAR (SYSDATE, 'DD/MM');
      END IF;

      RETURN v_cursor;
   END;

   FUNCTION getStockAlerts (v_sht_desc     TQC_ALERTS.ALRTS_SHT_DESC%TYPE,
                            v_item_code    NUMBER)
      RETURN stockAlertMSG_ref
   IS
      v_cursor   stockAlertMSG_ref;
      v_type     VARCHAR2 (4);
      param      TQC_PARAMETERS.PARAM_VALUE%TYPE;
   BEGIN
      ---rAISE_error('v_item_code--'||v_item_code||'v_sht_desc--'||v_sht_desc);
      SELECT TQC_PARAMETERS_PKG.get_param_varchar ('EMAILS_FROM')
        INTO param
        FROM DUAL;

      SELECT ALRTS_DEST_TYPE
        INTO v_type
        FROM TQC_ALERTS
       WHERE ALRTS_SHT_DESC = v_sht_desc;

      IF v_type = 'U'
      THEN
         OPEN v_cursor FOR
              SELECT ITM_DESCRIPTION,
                     ITM_MAX_LEVEL,
                     ITM_REODR_LEVEL,
                     SUM (STC_QUANTITY),
                     UNT_DESCRIPTION,
                     MSGT_MSG,
                     ALRT_TYPE,
                     USR_NAME,
                     USR_EMAIL,
                     param
                FROM SIV_ITEMS,
                     SIV_UNITS,
                     SIV_STOCK,
                     TQC_ALERTS,
                     TQC_ALERT_TYPES,
                     TQC_MSG_TEMPLATES,
                     TQC_USERS
               WHERE     UNT_CODE = ITM_UNT_CODE
                     AND USR_CODE = ALRTS_DEST_CODE
                     AND ALRTS_ALRT_CODE = ALRT_CODE
                     AND ALRTS_MSGT_CODE = MSGT_CODE
                     AND ALRTS_SHT_DESC = v_sht_desc
                     AND ALRTS_STATUS = 'A'
                     AND ITM_QUANTITY <= ITM_REODR_LEVEL
                     AND ITM_CODE = STC_ITM_CODE
                     AND ITM_CODE = v_item_code
            GROUP BY ITM_DESCRIPTION,
                     ITM_MAX_LEVEL,
                     ITM_REODR_LEVEL,
                     UNT_DESCRIPTION,
                     MSGT_MSG,
                     ALRT_TYPE,
                     USR_NAME,
                     USR_EMAIL,
                     param;

         RETURN v_cursor;
      END IF;
   END;

   PROCEDURE UpdateSMSMessage (v_smsCode       NUMBER,
                               v_clientCode    NUMBER,
                               v_telNo         VARCHAR2,
                               v_message       VARCHAR2,
                               v_userCode      NUMBER)
   IS
      v_decodeSmsTel   VARCHAR2 (50);
   BEGIN
      IF v_telNo IS NOT NULL
      THEN
         BEGIN
            SELECT '+' || COU_ZIP_CODE || v_telNo
              INTO v_decodeSmsTel
              FROM TQC_COUNTRIES, TQC_CLIENTS
             WHERE CLNT_COU_CODE = COU_CODE AND CLNT_CODE = v_clientCode;
         EXCEPTION
            WHEN OTHERS
            THEN
               NULL;
         END;
      END IF;

      UPDATE TQC_SMS_MESSAGES
         SET SMS_CLNT_CODE = v_clientCode,
             SMS_TEL_NO = v_decodeSmsTel,
             SMS_MSG = v_message
       WHERE SMS_CODE = v_smsCode;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Message!');
   END UpdateSMSMessage;

   PROCEDURE sendServiceProvSms (v_sms_sys_code      IN NUMBER,
                                 v_sms_sys_module    IN VARCHAR2,
                                 v_sms_clnt_code     IN NUMBER,
                                 v_sms_agn_code      IN NUMBER,
                                 v_sms_quot_code     IN NUMBER,
                                 v_sms_pol_code      IN NUMBER,
                                 v_sms_pol_no        IN VARCHAR2,
                                 v_sms_clm_no        IN VARCHAR2,
                                 v_sms_tel_no        IN VARCHAR2,
                                 v_sms_msg           IN VARCHAR2,
                                 v_sms_status        IN VARCHAR2,
                                 v_sms_prepared_by   IN VARCHAR2,
                                 v_sms_quot_no       IN VARCHAR2)
   IS
      --v_user VARCHAR2(35):= Pkg_Global_Vars.get_pvarchar2 ('pkg_global_vars.pvg_username');

      v_sms_code   NUMBER;
      v_usr_code   NUMBER;
   BEGIN
      -- raise_error('v_sys_code ='||v_agn_code);
      SELECT TQC_SMS_CODE_SEQ.NEXTVAL INTO v_sms_code FROM DUAL;

      BEGIN
         SELECT USR_CODE
           INTO v_usr_code
           FROM TQC_USERS
          WHERE USR_USERNAME = v_sms_prepared_by;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (
               'Unable to fetch user code for ' || v_sms_prepared_by);
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
                                    SMS_USR_CODE,
                                    SMS_QUOT_NO)
           VALUES (v_sms_code,
                   v_sms_sys_code,
                   v_sms_sys_module,
                   v_sms_clnt_code,
                   v_sms_agn_code,
                   v_sms_quot_code,
                   v_sms_pol_code,
                   v_sms_pol_no,
                   v_sms_clm_no,
                   v_sms_tel_no,
                   v_sms_msg,
                   v_sms_status,
                   v_sms_prepared_by,
                   SYSDATE,
                   v_usr_code,
                   v_sms_quot_no);
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
   v_alrt_grp_usr_code      IN   NUMBER
)
IS
BEGIN
--RAISE_ERROR('v_add_edit'||v_add_edit||'v_alrt_code'||v_alrt_code);
   IF NVL(v_add_edit,'S')='A'
   THEN
      INSERT INTO tqc_alert_types
                  (alrt_code, alrt_type, alrt_sys_code,
                   alrt_email, alrt_sms, alrt_screen,
                   alrt_email_msgt_code, alrt_sms_msgt_code,
                   alrt_grp_usr_code
                  )
           VALUES (tqc_alrt_code_seq.NEXTVAL, v_alrt_type, v_alrt_sys_code,
                   v_alrt_email, v_alrt_sms, v_alrt_screen,
                   v_alrt_email_msgt_code, v_alrt_sms_msgt_code,
                   v_alrt_grp_usr_code
                  );
   ELSIF NVL (v_add_edit, 'S')='E'
   THEN
      UPDATE tqc_alert_types
         SET alrt_type = v_alrt_type,
             alrt_sys_code = v_alrt_sys_code,
             alrt_email = v_alrt_email,
             alrt_sms = v_alrt_sms,
             alrt_screen = v_alrt_screen,
             alrt_email_msgt_code = v_alrt_email_msgt_code,
             alrt_sms_msgt_code = v_alrt_sms_msgt_code,
             alrt_grp_usr_code = v_alrt_grp_usr_code
       WHERE alrt_code = v_alrt_code;
   ELSIF NVL (v_add_edit, 'S')='D'
   THEN
      DELETE      tqc_alert_types
            WHERE alrt_code = v_alrt_code;
   END IF;
END;
 FUNCTION GetNewAlerts(v_alrts_prepared_by IN VARCHAR2,v_cnt OUT NUMBER)
   RETURN Alerts_ref
IS
   v_cursor   Alerts_ref;
  BEGIN
    SELECT COUNT(*)
    INTO v_cnt
    FROM tqc_alerts
    WHERE ALRTS_PREPARED_BY=v_alrts_prepared_by
        AND ALRTS_STATUS='N'; 
   OPEN v_cursor FOR
      SELECT ALRTS_DESCRIPTION
        FROM tqc_alerts
        WHERE ALRTS_PREPARED_BY=v_alrts_prepared_by
        AND ALRTS_STATUS='N';
    RETURN v_cursor;
END;
PROCEDURE updateAlerts(v_alrts_prepared_by IN VARCHAR2)
IS
BEGIN
 UPDATE TQC_ALERTS
         SET ALRTS_STATUS='R'
         WHERE ALRTS_PREPARED_BY=v_alrts_prepared_by;
         COMMIT;
      
END;
PROCEDURE updateViewedAlerts(v_alrts_prepared_by IN VARCHAR2,v_status IN VARCHAR2,v_alrts_code in number)
IS
BEGIN
 UPDATE TQC_ALERTS
         SET ALRTS_STATUS=v_status
         WHERE alrts_code=v_alrts_code;
         COMMIT;
 END;
PROCEDURE updateViewedAlerts(v_alrts_prepared_by IN VARCHAR2,v_status IN VARCHAR2)
IS
BEGIN
 UPDATE TQC_ALERTS
         SET ALRTS_STATUS=v_status
         WHERE ALRTS_PREPARED_BY=v_alrts_prepared_by
         AND ALRTS_STATUS!='R';
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
             msgt_msg, alrts_screen, alrts_prepared_by, alrts_prepare_date,
             alrts_quot_no, alrts_pol_policy_no, alrts_claim_no
        FROM tqc_alerts, tqc_agencies, tqc_clients, tqc_msg_templates
       WHERE  clnt_code(+) = alrts_clnt_code
         AND agn_code(+) = alrts_agn_code
         AND msgt_code(+) = alrts_msgt_code
         AND alrts_prepared_by = v_alrts_prepared_by;

   RETURN v_cursor;
END;
END Tqc_Sms_Pkg030215; 
/