CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_web_pkg
AS
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      END IF;
   END raise_error;

   PROCEDURE update_login_attempt (v_accc_code IN VARCHAR2)
   IS
   BEGIN
      --BEGIN
      UPDATE tqc_account_contacts
         SET accc_login_attempts = NVL (accc_login_attempts, 0) + 1
       WHERE UPPER (accc_username) = UPPER (v_accc_code);

      --EXCEPTION

      --WHEN OTHERS THEN
      -- RAISE_WHEN_OTHERS('Error registering login failure..');
      --END;
      COMMIT;
   END;

   FUNCTION check_agent_pwd (v_entered_user      IN     VARCHAR2,
                             v_entered_pwd       IN     VARCHAR2,
                             v_msg                  OUT VARCHAR2,
                             v_last_login_date      OUT DATE,
                             agentcode              OUT NUMBER,
                             agentname              OUT VARCHAR2,
                             agentshtdesc           OUT VARCHAR2,
                             agentcontactcode       OUT NUMBER,
                             agentcontactname       OUT VARCHAR2,
                             v_new_pwd1          IN     VARCHAR2,
                             v_new_pwd2          IN     VARCHAR2,
                             v_new_pwd_entry     IN     VARCHAR2 DEFAULT 'N',
                             agentbrncode           OUT NUMBER,
                             v_user_code            OUT NUMBER)
      RETURN NUMBER
   IS
      v_password           VARCHAR (30);
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      -- v_last_login_date DATE;
      v_pwd                VARCHAR2 (100);
      --v_msg VARCHAR2(150);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      --v_agn_sht_desc varchar2(35);
      x                    NUMBER := 0;
      nusercode            NUMBER;

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
           SELECT accph_pwd, accph_pwd_change_dt
             FROM tqc_acc_contacts_pwd_hist
            WHERE UPPER (accph_accc_code) = vuser
         ORDER BY accph_pwd_change_dt DESC;
   BEGIN
      --MSG_BOX('DANI');
      BEGIN
         SELECT accc_code,
                TO_NUMBER (accc_code),
                UPPER (accc_username),
                UPPER (accc_status),
                accc_pwd,
                accc_pwd_changed,
                accc_dt_created,
                accc_personel_rank,
                accc_pwd_reset,
                accc_login_attempts,
                NVL (accc_last_login_date, TRUNC (SYSDATE)),
                agn_code,
                agn_name,
                agn_sht_desc,
                accc_code,
                accc_name,
                accc_status,
                agn_brn_code
           INTO nusercode,
                v_user_code,
                v_us_id,
                v_status,
                v_pwd,
                v_pwd_changed_dt,
                v_pwd_date_created,
                v_per_rnk,
                v_pwdreset,
                v_loginattempts,
                v_last_login_date,
                agentcode,
                agentname,
                agentshtdesc,
                agentcontactcode,
                agentcontactname,
                v_status,
                agentbrncode
           FROM tqc_account_contacts, tqc_agencies
          WHERE     agn_code = accc_acc_code
                AND accc_login_allowed = 'Y'
                AND UPPER (accc_username) = v_entered_user;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_msg :=
               'The user is not a valid TurnQuest user. Enter valid username ';
            RETURN (2);
         WHEN OTHERS
         THEN
            v_msg := 'Unable to verify the username and password.';
            RETURN (2);
      END;

      IF NVL (v_pwdreset, 'N') = 'Y' AND NVL (v_new_pwd_entry, 'N') = 'N'
      THEN
         v_msg := 'Reset Password';
         RETURN (3);
      END IF;

      IF NVL (v_pwdreset, 'N') = 'N'
      THEN
         BEGIN
            SELECT accc_code
              INTO nusercode
              FROM tqc_account_contacts
             WHERE accc_pwd = gis_read.val (v_entered_pwd)
                   AND UPPER (accc_username) = v_entered_user;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_msg := 'Wrong User Password. Contact System Administrator';
               RETURN (2);
            WHEN OTHERS
            THEN
               v_msg :=
                  'Unable to verify the username and password. Contact System Administrator';
               RETURN (2);
         END;
      END IF;

      IF NVL (v_status, 'I') != 'A'
      THEN
         v_msg :=
            'Your account has been deactivated. Contact System Administrator.';
         RETURN (2);
      END IF;

      BEGIN
         v_syspwdattempts :=
            tqc_parameters_pkg.get_param_number ('SYSPWDLOGATTEMPTS');
         v_syspwdoldusecnt :=
            tqc_parameters_pkg.get_param_number ('SYSPWDOLDUSECNT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Error getting parameters...';
            RETURN (2);
      END;

      IF NVL (v_syspwdattempts, 0) > 0
         AND NVL (v_loginattempts, 0) >= NVL (v_syspwdattempts, 0)
      THEN
         v_msg :=
            'Max of ' || NVL (v_syspwdattempts, 0)
            || ' login attempt failure attained. Contact System Administrator for assistance.';
         RETURN (2);
      ELSIF NVL (v_pwd, '******') != gis_read.val (v_entered_pwd)
            AND NOT (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
      THEN
         update_login_attempt (v_entered_user);
         v_msg :=
            'Invalid username or password. Enter valid username and password and try again.';
         RETURN (2);
      END IF;

      --    MSG_BOX('DANI4');
      BEGIN
         IF v_new_pwd1 IS NOT NULL
         THEN
            v_password := v_new_pwd1;
         ELSE
            v_password := v_entered_pwd;
         END IF;

         plenght := LENGTH (v_password);

         FOR x IN 1 .. plenght
         LOOP
            IF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 65 AND 90
            THEN
               capscnt := NVL (capscnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 97 AND 122
            THEN
               lwrcnt := NVL (lwrcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 49 AND 57
            THEN
               numcnt := NVL (numcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) IN
                     (35, 36, 37, 38, 42, 64)
            THEN
               speccnt := NVL (numcnt, 0) + 1;
            END IF;
         END LOOP;
      END;

      v_syspwddrtntime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDDRTNTIME');
      v_syspwdprmttime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDPRMTTIME');
      v_syspwdlength := tqc_parameters_pkg.get_param_number ('SYSPWDLENGTH');
      v_sysupppwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDUPPENGTH');
      v_syslwrpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDLWRLENGTH');
      v_sysnumpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDNUMLENGTH');
      v_sysspecpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDSPECLENGTH');
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      IF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
            (TO_NUMBER (v_syspwddrtntime))
      THEN
         v_msg :=
            'Your password has expired. Please specify new password to continue.';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y', accc_pwd_changed = TRUNC (SYSDATE)
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      /*ELSIF NVL(v_pwdreset,'N') = 'Y' THEN
         v_msg := 'Your password has been reset. MUST specify a new password.';
         v_check_failed := TRUE;
           RETURN(2);*/
      ELSIF UPPER (v_entered_user) = UPPER (v_entered_pwd)
      THEN
         v_msg :=
            'Your password is same as the username. This is not allowed.';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
         /*ELSIF (TRUNC(SYSDATE)- v_pwd_changed_dt) >= (TO_NUMBER(v_syspwddrtntime)-TO_NUMBER(v_syspwdprmttime))  THEN
            v_msg := 'Your password is due to expire in '||((TO_NUMBER(v_syspwddrtntime)*1440)-(TO_NUMBER(v_syspwdprmttime)*1440))/1440||' days. ';
            v_check_failed := TRUE;
            v_mandatory       := FALSE;*/
         RETURN (2);
      ELSIF NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum ofxxxxxx '
            || v_sysupppwlength
            || ' upper case characters ';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;                                      ----TRUE
         RETURN (2);                                                   --FALSE
      ELSIF NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum ofmmmmm '
            || v_sysnumpwlength
            || ' numeric characters ';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
            AND NVL (v_sysspecpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum ofjjjjj '
            || v_sysspecpwlength
            || ' special characters ';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      --v_mandatory     := FALSE;
      END IF;

      IF (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
         AND (v_new_pwd1 IS NOT NULL OR v_new_pwd2 IS NOT NULL)
      THEN
         IF NVL (v_new_pwd1, '****') != NVL (v_new_pwd2, '%%%%')
         THEN
            v_msg :=
               'Your password has been re-entered incorrectly. Please try again! ';
            RETURN (5);
         ELSE
            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt
                 FROM tqc_acc_contacts_pwd_hist
                WHERE UPPER (accph_accc_username) = v_entered_user
                      AND accph_pwd = gis_read.val (v_new_pwd1);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Cannot verify if password ever used before..';
                  RETURN (5);
            END;

            IF NVL (v_cnt, 0) > NVL (v_syspwdoldusecnt, 0)
            THEN
               v_msg :=
                  'Cannot use a password more than '
                  || NVL (v_syspwdoldusecnt, 0)
                  || ' time(s). Please specify a new password never used before.. ';
               RETURN (5);
            END IF;

            BEGIN
               x := 0;

               FOR usrs IN usrpwds (v_entered_user)
               LOOP
                  IF usrs.accph_pwd = gis_read.val (v_new_pwd1)
                  THEN
                     v_msg :=
                        'Cannot Reuse the last two passwords. Please specify a new password never user before.. ';
                     RETURN (5);
                  END IF;

                  x := x + 1;

                  IF x >= 2
                  THEN
                     EXIT;
                  END IF;
               END LOOP;
            END;

            --ELSE
            BEGIN
               UPDATE tqc_account_contacts
                  SET accc_pwd_reset = 'N',
                      accc_pwd_changed = TRUNC (SYSDATE),
                      accc_pwd = gis_read.val (v_new_pwd1),
                      accc_login_attempts = 0
                WHERE UPPER (accc_username) = v_entered_user;
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'User password could not be changed...';
                  RETURN (5);
            END;

            BEGIN
               INSERT INTO tqc_acc_contacts_pwd_hist (accph_code,
                                                      accph_accc_code,
                                                      accph_accc_username,
                                                      accph_pwd,
                                                      accph_pwd_change_dt)
                    VALUES (tqc_accph_code_seq.NEXTVAL,
                            nusercode,
                            v_entered_user,
                            gis_read.val (v_new_pwd1),
                            SYSDATE);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Error updating password change history..';
                  RETURN (5);
            END;

            v_pwdreset := 'N';
            v_pwd_changed_dt := TRUNC (SYSDATE);
            v_loginattempts := 0;
         END IF;
      END IF;

      /*IF v_check_failed AND NOT v_mandatory THEN
     v_msg:=v_msg||' Do you want to change it now? ';
       RETURN(4);
   ELSIF v_check_failed AND  v_mandatory THEN
       v_msg:=v_msg||' Do you want to change it now? ';
     RETURN(3);
  END IF;*/
      UPDATE tqc_account_contacts
         SET accc_last_login_date = TRUNC (SYSDATE)
       WHERE UPPER (accc_username) = v_entered_user;

      COMMIT;
      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   END;

   FUNCTION check_agent_pwd (v_entered_user      IN     VARCHAR2,
                             v_entered_pwd       IN     VARCHAR2,
                             v_msg                  OUT VARCHAR2,
                             v_last_login_date      OUT DATE,
                             agentcode              OUT NUMBER,
                             agentname              OUT VARCHAR2,
                             agentshtdesc           OUT VARCHAR2,
                             agentcontactcode       OUT NUMBER,
                             agentcontactname       OUT VARCHAR2,
                             v_new_pwd1          IN     VARCHAR2,
                             v_new_pwd2          IN     VARCHAR2,
                             v_new_pwd_entry     IN     VARCHAR2 DEFAULT 'N',
                             agentbrncode           OUT NUMBER,
                             v_user_code            OUT NUMBER,
                             v_accc_sys_code        OUT NUMBER)
      RETURN NUMBER
   IS
      v_password           VARCHAR (30);
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      -- v_last_login_date DATE;
      v_pwd                VARCHAR2 (100);
      --v_msg VARCHAR2(150);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      --v_agn_sht_desc varchar2(35);
      x                    NUMBER := 0;
      nusercode            NUMBER;

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
           SELECT accph_pwd, accph_pwd_change_dt
             FROM tqc_acc_contacts_pwd_hist
            WHERE UPPER (accph_accc_code) = vuser
         ORDER BY accph_pwd_change_dt DESC;
   BEGIN
      --MSG_BOX('DANI');

      BEGIN
         SELECT accc_code,
                TO_NUMBER (accc_code),
                UPPER (accc_username),
                UPPER (accc_status),
                accc_pwd,
                accc_pwd_changed,
                accc_dt_created,
                accc_personel_rank,
                accc_pwd_reset,
                accc_login_attempts,
                NVL (accc_last_login_date, TRUNC (SYSDATE)),
                agn_code,
                agn_name,
                agn_sht_desc,
                accc_code,
                accc_name,
                accc_status,
                agn_brn_code,
                accc_sys_code
           INTO nusercode,
                v_user_code,
                v_us_id,
                v_status,
                v_pwd,
                v_pwd_changed_dt,
                v_pwd_date_created,
                v_per_rnk,
                v_pwdreset,
                v_loginattempts,
                v_last_login_date,
                agentcode,
                agentname,
                agentshtdesc,
                agentcontactcode,
                agentcontactname,
                v_status,
                agentbrncode,
                v_accc_sys_code
           FROM tqc_account_contacts, tqc_agencies
          WHERE     agn_code = accc_acc_code
                AND accc_login_allowed = 'Y'
                AND UPPER (accc_username) = v_entered_user;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_msg :=
               'The user is not a valid TurnQuest user. Enter valid username ';
            RETURN (2);
         WHEN OTHERS
         THEN
            v_msg := 'Unable to verify the username and password.';
            RETURN (2);
      END;

      IF NVL (v_pwdreset, 'N') = 'Y' AND NVL (v_new_pwd_entry, 'N') = 'N'
      THEN
         v_msg := 'Reset Password';
         RETURN (3);
      END IF;

      IF NVL (v_pwdreset, 'N') = 'N'
      THEN
         BEGIN
            SELECT accc_code
              INTO nusercode
              FROM tqc_account_contacts
             WHERE accc_pwd = gis_read.val (v_entered_pwd)
                   AND UPPER (accc_username) = v_entered_user;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_msg := 'Wrong User Password. Contact System Administrator';
               RETURN (2);
            WHEN OTHERS
            THEN
               v_msg :=
                  'Unable to verify the username and password. Contact System Administrator';
               RETURN (2);
         END;
      END IF;

      IF NVL (v_status, 'I') != 'A'
      THEN
         v_msg :=
            'Your account has been deactivated. Contact System Administrator.';
         RETURN (2);
      END IF;

      BEGIN
         v_syspwdattempts :=
            tqc_parameters_pkg.get_param_number ('SYSPWDLOGATTEMPTS');
         v_syspwdoldusecnt :=
            tqc_parameters_pkg.get_param_number ('SYSPWDOLDUSECNT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Error getting parameters...';
            RETURN (2);
      END;

      IF NVL (v_syspwdattempts, 0) > 0
         AND NVL (v_loginattempts, 0) >= NVL (v_syspwdattempts, 0)
      THEN
         v_msg :=
            'Max of ' || NVL (v_syspwdattempts, 0)
            || ' login attempt failure attained. Contact System Administrator for assistance.';
         RETURN (2);
      ELSIF NVL (v_pwd, '******') != gis_read.val (v_entered_pwd)
            AND NOT (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
      THEN
         update_login_attempt (v_entered_user);
         v_msg :=
            'Invalid username or password. Enter valid username and password and try again.';
         RETURN (2);
      END IF;

      --RETURN(1);
      /*
      BEGIN
         SELECT COUNT(1) INTO v_cnt FROM TQC_USER_SYSTEMS
         WHERE USYS_ACCC_CODE =  nUserCode
         AND USYS_SYS_CODE = :LI_SYSTEM;
         IF v_cnt > 0 THEN*/
      -- this code has been moved to system_connect
      /*BEGIN
         SELECT SYS_MAIN_FORM_NAME, SYS_DBASE_USERNAME, SYS_DBASE_PASSWORD,
         nvl(SYS_ACTIVE,'I'), SYS_PATH
         INTO vMainForm, vDbUser, vDbPassword, vSysStatus, vDefltPath
         FROM TQC_SYSTEMS
         WHERE SYS_CODE = TO_NUMBER(:LI_SYSTEM);
         IF vMainForm IS NULL or vDbUser IS NULL or vDbPassword IS NULL THEN
            RAISE_WHEN_OTHERS('Selected system access details incomplete. Contact administrator');
         END IF;
         IF vSysStatus = 'I' then
            RAISE_WHEN_OTHERS('This system has been deactivated. Contact administrator');
         END IF;
      EXCEPTION
         WHEN OTHERS THEN
            RAISE_WHEN_OTHERS ('Error determining user logon rights');
      END;*/
      /*null
         ELSE
            raise_when_others('Access denied to selected system ');
         END IF;
      END;*/
      --    MSG_BOX('DANI4');
      BEGIN
         IF v_new_pwd1 IS NOT NULL
         THEN
            v_password := v_new_pwd1;
         ELSE
            v_password := v_entered_pwd;
         END IF;

         plenght := LENGTH (v_password);

         FOR x IN 1 .. plenght
         LOOP
            IF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 65 AND 90
            THEN
               capscnt := NVL (capscnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 97 AND 122
            THEN
               lwrcnt := NVL (lwrcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 49 AND 57
            THEN
               numcnt := NVL (numcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) IN
                     (35, 36, 37, 38, 42, 64)
            THEN
               speccnt := NVL (numcnt, 0) + 1;
            END IF;
         END LOOP;
      END;

      v_syspwddrtntime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDDRTNTIME');
      v_syspwdprmttime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDPRMTTIME');
      v_syspwdlength := tqc_parameters_pkg.get_param_number ('SYSPWDLENGTH');
      v_sysupppwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDUPPENGTH');
      v_syslwrpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDLWRLENGTH');
      v_sysnumpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDNUMLENGTH');
      v_sysspecpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDSPECLENGTH');
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      IF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
            (TO_NUMBER (v_syspwddrtntime))
      THEN
         v_msg :=
            'Your password has expired. Please specify new password to continue.';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y', accc_pwd_changed = TRUNC (SYSDATE)
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      /*ELSIF NVL(v_pwdreset,'N') = 'Y' THEN
      v_msg := 'Your password has been reset. MUST specify a new password.';
      v_check_failed := TRUE;
        RETURN(2);*/
      ELSIF UPPER (v_entered_user) = UPPER (v_entered_pwd)
      THEN
         v_msg :=
            'Your password is same as the username. This is not allowed.';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
         /*ELSIF (TRUNC(SYSDATE)- v_pwd_changed_dt) >= (TO_NUMBER(v_syspwddrtntime)-TO_NUMBER(v_syspwdprmttime))  THEN
         v_msg := 'Your password is due to expire in '||((TO_NUMBER(v_syspwddrtntime)*1440)-(TO_NUMBER(v_syspwdprmttime)*1440))/1440||' days. ';
         v_check_failed := TRUE;
         v_mandatory       := FALSE;*/
         RETURN (2);
      ELSIF NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysupppwlength
            || ' upper case characters ';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysnumpwlength
            || ' numeric characters ';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      --    ELSIF NVL(speccnt, 0) < NVL(v_sysspecpwlength, 0) AND
      --          NVL(v_sysspecpwlength, 0) > 0 THEN
      --      v_msg := 'Your password should contain a minimum of ' ||
      --               v_sysspecpwlength || ' special characters ';
      --
      --      UPDATE tqc_account_contacts
      --         SET accc_pwd_reset = 'Y'
      --       WHERE accc_name = v_entered_user;
      --
      --      v_check_failed := TRUE;
      --      RETURN(2);
      --v_mandatory     := FALSE;
      END IF;

      IF (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
         AND (v_new_pwd1 IS NOT NULL OR v_new_pwd2 IS NOT NULL)
      THEN
         IF NVL (v_new_pwd1, '****') != NVL (v_new_pwd2, '%%%%')
         THEN
            v_msg :=
               'Your password has been re-entered incorrectly. Please try again! ';
            RETURN (5);
         ELSE
            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt
                 FROM tqc_acc_contacts_pwd_hist
                WHERE UPPER (accph_accc_username) = v_entered_user
                      AND accph_pwd = gis_read.val (v_new_pwd1);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Cannot verify if password ever used before..';
                  RETURN (5);
            END;

            IF NVL (v_cnt, 0) > NVL (v_syspwdoldusecnt, 0)
            THEN
               v_msg :=
                  'Cannot use a password more than '
                  || NVL (v_syspwdoldusecnt, 0)
                  || ' time(s). Please specify a new password never used before.. ';
               RETURN (5);
            END IF;

            BEGIN
               x := 0;

               FOR usrs IN usrpwds (v_entered_user)
               LOOP
                  IF usrs.accph_pwd = gis_read.val (v_new_pwd1)
                  THEN
                     v_msg :=
                        'Cannot Reuse the last two passwords. Please specify a new password never user before.. ';
                     RETURN (5);
                  END IF;

                  x := x + 1;

                  IF x >= 2
                  THEN
                     EXIT;
                  END IF;
               END LOOP;
            END;

            --ELSE
            BEGIN
               UPDATE tqc_account_contacts
                  SET accc_pwd_reset = 'N',
                      accc_pwd_changed = TRUNC (SYSDATE),
                      accc_pwd = gis_read.val (v_new_pwd1),
                      accc_login_attempts = 0
                WHERE UPPER (accc_username) = v_entered_user;
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'User password could not be changed...';
                  RETURN (5);
            END;

            BEGIN
               INSERT INTO tqc_acc_contacts_pwd_hist (accph_code,
                                                      accph_accc_code,
                                                      accph_accc_username,
                                                      accph_pwd,
                                                      accph_pwd_change_dt)
                    VALUES (tqc_accph_code_seq.NEXTVAL,
                            nusercode,
                            v_entered_user,
                            gis_read.val (v_new_pwd1),
                            SYSDATE);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Error updating password change history..';
                  RETURN (5);
            END;

            v_pwdreset := 'N';
            v_pwd_changed_dt := TRUNC (SYSDATE);
            v_loginattempts := 0;
         END IF;
      END IF;

      /*IF v_check_failed AND NOT v_mandatory THEN
         v_msg:=v_msg||' Do you want to change it now? ';
           RETURN(4);
       ELSIF v_check_failed AND  v_mandatory THEN
           v_msg:=v_msg||' Do you want to change it now? ';
         RETURN(3);
      END IF;*/
      UPDATE tqc_account_contacts
         SET accc_last_login_date = TRUNC (SYSDATE)
       WHERE UPPER (accc_username) = v_entered_user;

      -- COMMIT;
      --raise_Error('here');
      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   END;

   FUNCTION check_user_pwd (v_entered_user      IN     VARCHAR2,
                            v_entered_pwd       IN     VARCHAR2,
                            v_msg                  OUT VARCHAR2,
                            v_last_login_date      OUT DATE,
                            usercode               OUT NUMBER,
                            username               OUT VARCHAR2,
                            v_new_pwd1          IN     VARCHAR2,
                            v_new_pwd2          IN     VARCHAR2,
                            v_new_pwd_entry     IN     VARCHAR2 DEFAULT 'N',
                            v_sys_code          IN     NUMBER)
      RETURN NUMBER
   IS
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      v_pwd                VARCHAR2 (100);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      x                    NUMBER := 0;
      nusercode            NUMBER;

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
           SELECT usr_pwd, usr_pwd_change_dt
             FROM tqc_users_passwd_hist
            WHERE UPPER (usrph_usr_code) = vuser
         ORDER BY usr_pwd_change_dt DESC;
   BEGIN
      BEGIN
         SELECT usr_code,
                UPPER (usr_username),
                UPPER (usr_status),
                usr_pwd,
                usr_pwd_changed,
                usr_dt_created,
                usr_personel_rank,
                usr_pwd_reset,
                usr_login_attempts,
                NVL (usr_last_date, TRUNC (SYSDATE))
           INTO usercode,
                username,
                v_status,
                v_pwd,
                v_pwd_changed_dt,
                v_pwd_date_created,
                v_per_rnk,
                v_pwdreset,
                v_loginattempts,
                v_last_login_date
           FROM tqc_users
          WHERE UPPER (usr_username) = v_entered_user;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_msg :=
               'Invalid username or password. Enter valid username and password and try again.';
            RETURN (3);
         WHEN OTHERS
         THEN
            v_msg := 'Unable to verify the username and password.';
            RETURN (3);
      END;

      BEGIN
         v_syspwdattempts :=
            tqc_parameters_pkg.get_param_number ('SYSPWDLOGATTEMPTS');
         v_syspwdoldusecnt :=
            tqc_parameters_pkg.get_param_number ('SYSPWDOLDUSECNT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Error getting parameters...';
            RETURN (3);
      END;

      IF v_status = 'I'
      THEN
         v_msg :=
            'The account with the given username is deactivated. Please contact the Administrator.';
         RETURN (3);
      END IF;

      --RAISE_ERROR(' v_entered_pwd '||v_entered_pwd);

      IF NVL (v_syspwdattempts, 0) > 0
         AND NVL (v_loginattempts, 0) >= NVL (v_syspwdattempts, 0)
      THEN
         v_msg :=
            'Max of ' || NVL (v_syspwdattempts, 0)
            || ' login attempt failure attained. Contact System Administrator for assistance.';
         RETURN (3);
      ELSIF NVL (v_pwd, '******') != gis_read.val (v_entered_pwd)
            AND NOT (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
      THEN
         update_login_attempt (v_entered_user);
         v_msg :=
            'Invalid username or password. Enter valid username and password and try again.';
         RETURN (2);
      END IF;

      IF (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
         AND (v_new_pwd1 IS NOT NULL OR v_new_pwd2 IS NOT NULL)
      THEN
         IF NVL (v_new_pwd1, '****') != NVL (v_new_pwd2, '%%%%')
         THEN
            v_msg :=
               'Your password has been re-entered incorrectly. Please try again! ';
            RETURN (3);
         ELSE
            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt
                 FROM tqc_users_passwd_hist
                WHERE UPPER (usrph_usr_username) = v_entered_user
                      AND usr_pwd = gis_read.val (v_new_pwd1);
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  v_cnt := 0;
               WHEN OTHERS
               THEN
                  v_msg := 'Cannot verify if password ever used before..';
                  RETURN (3);
            END;

            IF NVL (v_cnt, 0) >= NVL (v_syspwdoldusecnt, 0)
            THEN
               v_msg :=
                  'Cannot use a password more than '
                  || NVL (v_syspwdoldusecnt, 0)
                  || ' time(s). Please specify a new password never user before.. ';
               RETURN (4);
            END IF;

            BEGIN
               x := 0;

               FOR usrs IN usrpwds (v_entered_user)
               LOOP
                  IF usrs.usr_pwd = gis_read.val (v_new_pwd1)
                  THEN
                     v_msg :=
                        'Cannot Reuse the last two passwords. Please specify a new password never user before.. ';
                     RETURN (4);
                  END IF;

                  x := x + 1;

                  IF x >= 2
                  THEN
                     EXIT;
                  END IF;
               END LOOP;
            END;

            BEGIN
               UPDATE tqc_users
                  SET usr_pwd_reset = 'N',
                      usr_pwd_changed = TRUNC (SYSDATE),
                      usr_pwd = gis_read.val (v_new_pwd1),
                      usr_login_attempts = 0
                WHERE UPPER (usr_username) = v_entered_user;
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'User password could not be changed...';
                  RETURN (3);
            END;

            BEGIN
               INSERT INTO tqc_users_passwd_hist (usrph_code,
                                                  usrph_usr_code,
                                                  usrph_usr_username,
                                                  usr_pwd,
                                                  usr_pwd_change_dt)
                    VALUES (tqc_usrph_code_seq.NEXTVAL,
                            nusercode,
                            v_entered_user,
                            gis_read.val (v_new_pwd1),
                            SYSDATE);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Error updating password change history..';
                  RETURN (3);
            END;

            v_pwdreset := 'N';
            v_pwd_changed_dt := TRUNC (SYSDATE);
            v_loginattempts := 0;
         END IF;
      END IF;

      BEGIN
         IF v_new_pwd1 IS NULL
         THEN
            plenght := LENGTH (v_entered_pwd);
         ELSE
            plenght := LENGTH (v_new_pwd1);
         END IF;

         FOR x IN 1 .. plenght
         LOOP
            IF ASCII (SUBSTR (v_entered_pwd, x, 1)) BETWEEN 65 AND 90
            THEN
               capscnt := NVL (capscnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_entered_pwd, x, 1)) BETWEEN 97 AND 122
            THEN
               lwrcnt := NVL (lwrcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_entered_pwd, x, 1)) BETWEEN 49 AND 57
            THEN
               numcnt := NVL (numcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_entered_pwd, x, 1)) IN
                     (35, 36, 37, 38, 42, 64)
            THEN
               speccnt := NVL (numcnt, 0) + 1;
            END IF;
         END LOOP;
      END;

      v_syspwddrtntime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDDRTNTIME');
      v_syspwdprmttime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDPRMTTIME');
      v_syspwdlength := tqc_parameters_pkg.get_param_number ('SYSPWDLENGTH');
      v_sysupppwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDUPPENGTH');
      v_syslwrpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDLWRLENGTH');
      v_sysnumpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDNUMLENGTH');
      v_sysspecpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDSPECLENGTH');
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      --   RAISE_ERROR('v_pwd_changed_dt=='||v_pwd_changed_dt||'v_syspwddrtntime=='||v_syspwddrtntime||'v_syspwdprmttime=='||v_syspwdprmttime);
      IF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
            (TO_NUMBER (v_syspwddrtntime))
      THEN
         v_msg :=
            'Your password has expired. Please specify new password to continue.';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF NVL (v_pwdreset, 'N') = 'Y'
      THEN
         v_msg := 'Your password has been reset. MUST specify a new password.';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF UPPER (v_entered_user) = UPPER (v_entered_pwd)
      THEN
         v_msg :=
            'Your password is same as the username. This is not allowed.';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
               (TO_NUMBER (v_syspwddrtntime) - TO_NUMBER (v_syspwdprmttime))
      THEN
         v_msg :=
            'Your password is due to expire in '
            || TO_CHAR (
                  TO_NUMBER (v_syspwddrtntime)
                  - (TRUNC (SYSDATE) - v_pwd_changed_dt))
            || ' days. ';
         v_check_failed := TRUE;
         v_mandatory := TRUE;
         RETURN (7);
      ELSIF NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysupppwlength
            || ' upper case characters ';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysnumpwlength
            || ' numeric characters ';
         v_check_failed := TRUE;
         RETURN (4);
      --      ELSIF     NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
      --            AND NVL (v_sysspecpwlength, 0) > 0
      --      THEN
      --         v_msg :=
      --               'Your password should contain a minimum of '
      --            || v_sysspecpwlength
      --            || ' special characters ';
      --         v_check_failed := TRUE;
      --         v_mandatory := FALSE;
      --         RETURN (4);
      END IF;

      IF v_check_failed AND NOT v_mandatory
      THEN
         v_msg := v_msg || ' Change it now ';
         RETURN (4);
      ELSIF v_check_failed AND v_mandatory
      THEN
         v_msg := v_msg || ' Login not allowed ';
         RETURN (2);
      END IF;

      UPDATE tqc_users
         SET usr_last_date = TRUNC (SYSDATE)
       WHERE UPPER (usr_username) = v_entered_user;

      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   END;

   FUNCTION check_user_sys (v_entered_user      IN     VARCHAR2,
                            v_msg                  OUT VARCHAR2,
                            v_last_login_date      OUT DATE,
                            usercode               OUT NUMBER,
                            username               OUT VARCHAR2,
                            v_sys_code          IN     NUMBER)
      RETURN NUMBER
   IS
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      -- v_last_login_date DATE;
      v_pwd                VARCHAR2 (100);
      --v_msg VARCHAR2(150);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      x                    NUMBER := 0;
      nusercode            NUMBER;

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
           SELECT usr_pwd, usr_pwd_change_dt
             FROM tqc_users_passwd_hist
            WHERE UPPER (usrph_usr_code) = vuser
         ORDER BY usr_pwd_change_dt DESC;
   BEGIN
      --MSG_BOX('DANI');
      BEGIN
         SELECT usr_code,
                UPPER (usr_username),
                UPPER (usr_status),
                usr_pwd,
                usr_pwd_changed,
                usr_dt_created,
                usr_personel_rank,
                usr_pwd_reset,
                usr_login_attempts,
                NVL (usr_last_date, TRUNC (SYSDATE))
           INTO usercode,
                username,
                v_status,
                v_pwd,
                v_pwd_changed_dt,
                v_pwd_date_created,
                v_per_rnk,
                v_pwdreset,
                v_loginattempts,
                v_last_login_date
           FROM tqc_users
          WHERE UPPER (usr_username) = v_entered_user;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            v_msg := 'Unable to verify the username and password.';
            RETURN (3);
      END;

      /*

         --RETURN(1);

         BEGIN
            SELECT COUNT(1) INTO v_cnt FROM TQC_USER_SYSTEMS
            WHERE USYS_ACWA_CODE =  nUserCode
            AND USYS_SYS_CODE = :LI_SYSTEM;
            IF v_cnt > 0 THEN*/
      -- this code has been moved to system_connect
      /*BEGIN
            SELECT SYS_MAIN_FORM_NAME, SYS_DBASE_USERNAME, SYS_DBASE_PASSWORD,
            nvl(SYS_ACTIVE,'I'), SYS_PATH
            INTO vMainForm, vDbUser, vDbPassword, vSysStatus, vDefltPath
            FROM TQC_SYSTEMS
            WHERE SYS_CODE = TO_NUMBER(:LI_SYSTEM);

            IF vMainForm IS NULL or vDbUser IS NULL or vDbPassword IS NULL THEN
               RAISE_WHEN_OTHERS('Selected system access details incomplete. Contact administrator');
            END IF;

            IF vSysStatus = 'I' then
               RAISE_WHEN_OTHERS('This system has been deactivated. Contact administrator');
            END IF;
         EXCEPTION
            WHEN OTHERS THEN
               RAISE_WHEN_OTHERS ('Error determining user logon rights');
         END;*/
            /*null
   ELSE
      raise_when_others('Access denied to selected system ');
   END IF;
END;*/
      --    MSG_BOX('DANI4');
      --RAISE_ERROR(v_sys_code||';'||v_entered_user);
      BEGIN
         user_system_conn (v_entered_user, v_sys_code, 'N');
         v_msg := v_msg || ' Login Successfull';
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'ACCESS TO THE SYSTEM DENIED .';
            RETURN (3);
      --RAISE_ERROR(v_msg);
      END;

      RETURN (1);
   END;

   PROCEDURE user_system_conn (v_user                  VARCHAR2,
                               v_sys_code           IN NUMBER,
                               v_killopen_session   IN VARCHAR2 DEFAULT 'N')
   IS
      v_sessions        NUMBER;
      --v_user varchar2(30);
      al_id             NUMBER;
      v_sid             NUMBER;
      v_serial          NUMBER;
      v_killed_sid      NUMBER;
      vautofix          VARCHAR2 (5);
      v_cnt             NUMBER;
      vmainform         VARCHAR2 (20);
      vdbuser           VARCHAR2 (20);
      vdbpassword       VARCHAR2 (20);
      vsysstatus        VARCHAR2 (10);
      vdefltpath        VARCHAR2 (100);
      vcalledform       VARCHAR2 (100);
      v_multisessions   VARCHAR2 (30);
      nsessionid        NUMBER;
      v_user_code       NUMBER;

      --vParamList PARAMLIST;
      CURSOR cur_session (
         vuser        VARCHAR2,
         vdbuser   IN VARCHAR2)
      IS
         SELECT SID,
                serial# serial,
                SUBSTR (module, INSTR (module, ':') + 1) killed_sid
           FROM tqc_session_log
          WHERE     SUBSTR (module, 1, INSTR (module, ':') - 1) = vuser
                AND username = vdbuser
                AND status != 'KILLED';
   BEGIN
      --v_user := UPPER(:TI_USERNAME);
      --RAISE_ERROR(v_sys_code||';'||v_user);
      BEGIN
         BEGIN
              SELECT COUNT (1), usr_code
                INTO v_cnt, v_user_code
                FROM tqc_user_systems, tqc_users
               WHERE     usys_usr_code = usr_code
                     AND UPPER (usr_username) = UPPER (v_user)
                     AND usys_sys_code = v_sys_code
            GROUP BY usr_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               -- RAISE_ERROR(v_sys_code||'1;'||v_user);
               --TQC_ERROR_MANAGER.RAISE_ERROR(10010);
               raise_error ('Access denied to selected system ');
            --RAISE_APPLICATION_ERROR(-20001,'Access denied to selected system ');
            WHEN OTHERS
            THEN
               raise_error ('Error getting user details');
         --RAISE_APPLICATION_ERROR(-20001,'Error getting usercode...');
         -- RAISE_ERROR(v_sys_code||'222;'||v_user);
         END;

         IF NVL (v_cnt, 0) = 0
         THEN
            --RAISE_ERROR(v_sys_code||'333;'||v_user);
            tqc_error_manager.raise_error (10010);
         --RAISE_APPLICATION_ERROR(-20001,'Access denied to selected system ');
         END IF;
      END;

      BEGIN
         pkg_global_vars.set_pvar ('pkg_global_vars.pvg_username', v_user);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error (
               -20001,
               'Unable to set the application user global variable..');
      END;

      --Set the session id global variable
      BEGIN
         SELECT TO_NUMBER (
                   TO_CHAR (SYSDATE - 365, 'Y')
                   || tqc_usl_session_code_seq.NEXTVAL)
           INTO nsessionid
           FROM DUAL;

         -- raise_when_others('nSessionId='||nSessionId);
         pkg_global_vars.set_pvar ('pkg_global_vars.pvg_session_id',
                                   nsessionid);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error (
               -20001,
               'Unable to log user logon audit. contact system administrator');
      END;

      --=================ALLOW MULTIPLE SESSIONS================================
      BEGIN
         v_multisessions :=
            tqc_parameters_pkg.get_param_varchar ('SYSMULTISESSION');
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_multisessions := 'N';
         WHEN OTHERS
         THEN
            raise_application_error (-20001, 'Error getting parameters...');
      END;

      v_sessions := 0;

      BEGIN
         SELECT COUNT ('x')
           INTO v_sessions
           FROM tqc_session_log
          WHERE     SUBSTR (module, 1, INSTR (module, ':') - 1) = v_user
                AND username = vdbuser
                AND status != 'KILLED';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error (-20001,
                                     'Error determining active sessions...');
      END;

      IF NVL (v_sessions, 0) >= 1 AND v_multisessions = 'N'
      THEN
         IF NVL (v_killopen_session, 'N') = 'Y'
         THEN
            FOR cur_session_rec IN cur_session (v_user, vdbuser)
            LOOP
               BEGIN
                  --alter system  kill session ''''||cur_session_REC.sid||','||cur_session_REC.serial||'''');
                  UPDATE tqc_user_logon
                     SET usl_logout_dt = SYSDATE
                   WHERE usl_usr_code = v_user_code
                         AND usl_session_code = cur_session_rec.killed_sid;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_application_error (
                        -20001,
                           'Unable to kill the Previous session. Error: '
                        || SQLERRM
                        || ' Contact the system Administrator');
               END;
            END LOOP;
         ELSE
            raise_application_error (
               -20001,
                  'You have '
               || v_sessions
               || ' sessions open. System cannot open more than ONE session.');
         END IF;
      ELSE
         BEGIN
            v_sessions := 0;

            SELECT COUNT ('x')
              INTO v_sessions
              FROM tqc_user_logon
             WHERE     usl_logout_dt IS NULL
                   AND usl_usr_code = v_user_code
                   AND TRUNC (usl_logout_dt) = TRUNC (SYSDATE);

            IF NVL (v_sessions, 0) > 0
            THEN
               UPDATE tqc_user_logon
                  SET usl_logout_dt = (usl_logon_dt + (15 / 1440))
                WHERE usl_usr_code = v_user_code;
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_application_error (-20001,
                                        'Error updating session details...');
         END;
      END IF;

      BEGIN
         INSERT INTO tqc_user_logon (usl_session_code,
                                     usl_usr_code,
                                     usl_sys_code,
                                     usl_logon_dt)
              VALUES (nsessionid,
                      v_user_code,
                      v_sys_code,
                      SYSDATE);

         BEGIN
            DBMS_APPLICATION_INFO.set_module (
               UPPER (v_user) || ':' || nsessionid,
               'PINNACLE');
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_application_error (-20001, 'Error:- application error');
         END;
      --:system.message_level :='25';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error (-20001,
                                     'Logon audit could not be created');
      END;

      BEGIN
         UPDATE tqc_users
            SET usr_login_attempts = 0, usr_last_date = SYSDATE
          WHERE UPPER (usr_username) = UPPER (v_user);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error (-20001,
                                     'User login details writing failure');
      END;

      COMMIT;

      BEGIN
         pkg_global_vars.set_pvar ('pkg_global_vars.pvg_autofix', vautofix);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error (
               -20001,
               'Error setting autofix global variable..');
      END;
   END;

   FUNCTION check_client_pwd (
      v_entered_user      IN     VARCHAR2,
      v_entered_pwd       IN     VARCHAR2,
      v_msg                  OUT VARCHAR2,
      v_last_login_date      OUT DATE,
      clientcode             OUT NUMBER,
      clientname             OUT VARCHAR2,
      clientcontactcode      OUT NUMBER,
      clientcontactname      OUT VARCHAR2,
      v_new_pwd1          IN     VARCHAR2,
      v_new_pwd2          IN     VARCHAR2,
      v_new_pwd_entry     IN     VARCHAR2 DEFAULT 'N')
      RETURN NUMBER
   IS
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      -- v_last_login_date DATE;
      v_pwd                VARCHAR2 (100);
      --v_msg VARCHAR2(150);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      x                    NUMBER := 0;
      nusercode            NUMBER;

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
           SELECT cwwph_pwd, cwwph_pwd_change_dt
             FROM tqc_client_web_pwd_hist
            WHERE UPPER (cwwph_acwa_code) = vuser
         ORDER BY cwwph_pwd_change_dt DESC;
   BEGIN
      --MSG_BOX('DANI');
      --RAISE_ERROR(v_entered_user);
      BEGIN
         SELECT acwa_code,
                UPPER (acwa_username),
                UPPER (acwa_status),
                acwa_pwd,
                acwa_pwd_changed,
                acwa_dt_created,
                acwa_personel_rank,
                acwa_pwd_reset,
                acwa_login_attempts,
                NVL (acwa_last_login_date, TRUNC (SYSDATE)),
                clnt_code,
                LTRIM (RTRIM (clnt_surname || ' ' || clnt_other_names)),
                acwa_code,
                clnt_surname
           INTO nusercode,
                v_us_id,
                v_status,
                v_pwd,
                v_pwd_changed_dt,
                v_pwd_date_created,
                v_per_rnk,
                v_pwdreset,
                v_loginattempts,
                v_last_login_date,
                clientcode,
                clientname,
                clientcontactcode,
                clientcontactname
           FROM tqc_client_web_accounts, tqc_clients
          WHERE clnt_code = acwa_clnt_code
                AND UPPER (acwa_username) = v_entered_user;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_msg :=
               'The user is not a valid TurnQuest user. Enter valid username ';
            RETURN (3);
         WHEN OTHERS
         THEN
            v_msg := 'Unable to verify the username and password.';
            RETURN (3);
      END;

      BEGIN
         v_syspwdattempts :=
            tqc_parameters_pkg.get_param_number ('SYSAGNPWDLOGATTEMPTS');
         v_syspwdoldusecnt :=
            tqc_parameters_pkg.get_param_number ('SYSAGNPWDOLDUSECNT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Error getting parameters...';
            RETURN (3);
      END;

      IF NVL (v_syspwdattempts, 0) > 0
         AND NVL (v_loginattempts, 0) >= NVL (v_syspwdattempts, 0)
      THEN
         v_msg :=
            'Max of ' || NVL (v_syspwdattempts, 0)
            || ' login attempt failure attained. Contact System Administrator for assistance.';
         RETURN (3);
      ELSIF NVL (v_pwd, '******') != gis_read.val (v_entered_pwd)
            AND NOT (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
      THEN
         update_login_attempt (v_entered_user);
         v_msg :=
            'Invalid username or password. Enter valid username and password and try again.';
         RETURN (2);
      END IF;

      IF (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
         AND (v_new_pwd1 IS NOT NULL OR v_new_pwd2 IS NOT NULL)
      THEN
         IF NVL (v_new_pwd1, '****') != NVL (v_new_pwd2, '%%%%')
         THEN
            v_msg :=
               'Your password has been re-entered incorrectly. Please try again! ';
            RETURN (3);
         ELSE
            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt
                 FROM tqc_client_web_pwd_hist
                WHERE UPPER (cwwph_acwa_username) = v_entered_user
                      AND cwwph_pwd = gis_read.val (v_new_pwd1);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Cannot verify if password ever used before..';
                  RETURN (3);
            END;

            IF NVL (v_cnt, 0) > NVL (v_syspwdoldusecnt, 0)
            THEN
               v_msg :=
                  'Cannot use a password more than '
                  || NVL (v_syspwdoldusecnt, 0)
                  || ' time(s). Please specify a new password never user before.. ';
               RETURN (3);
            END IF;

            BEGIN
               x := 0;

               FOR usrs IN usrpwds (v_entered_user)
               LOOP
                  IF usrs.cwwph_pwd = gis_read.val (v_new_pwd1)
                  THEN
                     v_msg :=
                        'Cannot Reuse the last two passwords. Please specify a new password never user before.. ';
                     RETURN (3);
                  END IF;

                  x := x + 1;

                  IF x >= 2
                  THEN
                     EXIT;
                  END IF;
               END LOOP;
            END;

            --ELSE
            BEGIN
               UPDATE tqc_client_web_accounts
                  SET acwa_pwd_reset = 'N',
                      acwa_pwd_changed = TRUNC (SYSDATE),
                      acwa_pwd = gis_read.val (v_new_pwd1),
                      acwa_login_attempts = 0
                WHERE UPPER (acwa_username) = v_entered_user;
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'User password could not be changed...';
                  RETURN (3);
            END;

            BEGIN
               INSERT INTO tqc_client_web_pwd_hist (cwwph_code,
                                                    cwwph_acwa_code,
                                                    cwwph_acwa_username,
                                                    cwwph_pwd,
                                                    cwwph_pwd_change_dt)
                    VALUES (tqc_cwwph_code_seq.NEXTVAL,
                            nusercode,
                            v_entered_user,
                            gis_read.val (v_new_pwd1),
                            SYSDATE);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Error updating password change history..';
                  RETURN (3);
            END;

            v_pwdreset := 'N';
            v_pwd_changed_dt := TRUNC (SYSDATE);
            v_loginattempts := 0;
         END IF;
      END IF;

      --RETURN(1);
      /*
      BEGIN
         SELECT COUNT(1) INTO v_cnt FROM TQC_USER_SYSTEMS
         WHERE USYS_ACWA_CODE =  nUserCode
         AND USYS_SYS_CODE = :LI_SYSTEM;
         IF v_cnt > 0 THEN*/
      -- this code has been moved to system_connect
      /*BEGIN
            SELECT SYS_MAIN_FORM_NAME, SYS_DBASE_USERNAME, SYS_DBASE_PASSWORD,
            nvl(SYS_ACTIVE,'I'), SYS_PATH
            INTO vMainForm, vDbUser, vDbPassword, vSysStatus, vDefltPath
            FROM TQC_SYSTEMS
            WHERE SYS_CODE = TO_NUMBER(:LI_SYSTEM);

            IF vMainForm IS NULL or vDbUser IS NULL or vDbPassword IS NULL THEN
               RAISE_WHEN_OTHERS('Selected system access details incomplete. Contact administrator');
            END IF;

            IF vSysStatus = 'I' then
               RAISE_WHEN_OTHERS('This system has been deactivated. Contact administrator');
            END IF;
         EXCEPTION
            WHEN OTHERS THEN
               RAISE_WHEN_OTHERS ('Error determining user logon rights');
         END;*/
            /*null
   ELSE
      raise_when_others('Access denied to selected system ');
   END IF;
END;*/
      --    MSG_BOX('DANI4');
      BEGIN
         plenght := LENGTH (v_entered_pwd);

         FOR x IN 1 .. plenght
         LOOP
            IF ASCII (SUBSTR (v_entered_pwd, x, 1)) BETWEEN 65 AND 90
            THEN
               capscnt := NVL (capscnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_entered_pwd, x, 1)) BETWEEN 97 AND 122
            THEN
               lwrcnt := NVL (lwrcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_entered_pwd, x, 1)) BETWEEN 49 AND 57
            THEN
               numcnt := NVL (numcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_entered_pwd, x, 1)) IN
                     (35, 36, 37, 38, 42, 64)
            THEN
               speccnt := NVL (numcnt, 0) + 1;
            END IF;
         END LOOP;
      END;

      v_syspwddrtntime :=
         tqc_parameters_pkg.get_param_number ('SYSAGNPWDDRTNTIME');
      v_syspwdprmttime :=
         tqc_parameters_pkg.get_param_number ('SYSAGNPWDPRMTTIME');
      v_syspwdlength :=
         tqc_parameters_pkg.get_param_number ('SYSAGNPWDLENGTH');
      v_sysupppwlength :=
         tqc_parameters_pkg.get_param_number ('SYSAGNPWDUPPENGTH');
      v_syslwrpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSAGNPWDLWRLENGTH');
      v_sysnumpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSAGNPWDNUMLENGTH');
      v_sysspecpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSAGNPWDSPECLENGTH');
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      IF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
            (TO_NUMBER (v_syspwddrtntime))
      THEN
         v_msg :=
            'Your password has expired. Please specify new password to continue.';
         v_check_failed := TRUE;
      ELSIF NVL (v_pwdreset, 'N') = 'Y'
      THEN
         v_msg := 'Your password has been reset. MUST specify a new password.';
         v_check_failed := TRUE;
      ELSIF UPPER (v_entered_user) = UPPER (v_entered_pwd)
      THEN
         v_msg :=
            'Your password is same as the username. This is not allowed.';
         v_check_failed := TRUE;
      ELSIF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
               (TO_NUMBER (v_syspwddrtntime) - TO_NUMBER (v_syspwdprmttime))
      THEN
         v_msg :=
            'Your password is due to expire in '
            || ( (TO_NUMBER (v_syspwddrtntime) * 1440)
                - (TO_NUMBER (v_syspwdprmttime) * 1440))
               / 1440
            || ' days. ';
         v_check_failed := TRUE;
         v_mandatory := FALSE;
      ELSIF NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';
         v_check_failed := TRUE;
      ELSIF NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysupppwlength
            || ' upper case characters ';
         v_check_failed := TRUE;
      ELSIF NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';
         v_check_failed := TRUE;
      ELSIF NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysnumpwlength
            || ' numeric characters ';
         v_check_failed := TRUE;
      ELSIF NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
            AND NVL (v_sysspecpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysspecpwlength
            || ' special characters ';
         v_check_failed := TRUE;
         v_mandatory := FALSE;
      END IF;

      IF v_check_failed AND NOT v_mandatory
      THEN
         v_msg := v_msg || ' Do you want to change it now? ';
         RETURN (4);
      ELSIF v_check_failed AND v_mandatory
      THEN
         v_msg := v_msg || ' Login not allowed ';
         RETURN (2);
      END IF;

      UPDATE tqc_client_web_accounts
         SET acwa_last_login_date = TRUNC (SYSDATE)
       WHERE UPPER (acwa_username) = v_entered_user;

      COMMIT;
      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   END;

   FUNCTION agent_authenticate (vusername IN VARCHAR2, vpwd IN VARCHAR2)
      RETURN INTEGER
   IS
      voutput            INTEGER;
      vmsg               VARCHAR2 (500);
      lastlogindate      DATE;
      agentcode          NUMBER;
      agentname          VARCHAR2 (150);
      agentshtdesc       VARCHAR2 (35);
      agentcontactcode   NUMBER;
      agentcontactname   VARCHAR2 (150);
      agentbrncode       NUMBER;
      v_user_code        NUMBER;
   BEGIN
      voutput :=
         check_agent_pwd (vusername,
                          vpwd,
                          vmsg,
                          lastlogindate,
                          agentcode,
                          agentname,
                          agentshtdesc,
                          agentcontactcode,
                          agentcontactname,
                          NULL,
                          NULL,
                          'N',
                          agentbrncode,
                          v_user_code);
      RETURN (voutput);
   END;

   FUNCTION client_authenticate (vusername IN VARCHAR2, vpwd IN VARCHAR2)
      RETURN INTEGER
   IS
      voutput            INTEGER;
      vmsg               VARCHAR2 (500);
      lastlogindate      DATE;
      agentcode          NUMBER;
      agentname          VARCHAR2 (150);
      agentcontactcode   NUMBER;
      agentcontactname   VARCHAR2 (150);
   BEGIN
      voutput :=
         check_client_pwd (vusername,
                           vpwd,
                           vmsg,
                           lastlogindate,
                           agentcode,
                           agentname,
                           agentcontactcode,
                           agentcontactname,
                           NULL,
                           NULL,
                           'N');
      RETURN (voutput);
   END;

   FUNCTION user_authenticate (vusername    IN VARCHAR2,
                               vpwd         IN VARCHAR2,
                               v_sys_code   IN VARCHAR2)
      RETURN INTEGER
   IS
      voutput         INTEGER;
      vmsg            VARCHAR2 (500);
      lastlogindate   DATE;
      usercode        NUMBER;
      username        VARCHAR2 (150);
   BEGIN
      --raise_error(vusername);
      voutput :=
         check_user_pwd (vusername,
                         vpwd,
                         vmsg,
                         lastlogindate,
                         usercode,
                         username,
                         NULL,
                         NULL,
                         'N',
                         v_sys_code);
      RETURN (voutput);
   END;

   FUNCTION user_auth_areas (user_code IN NUMBER, sys_code IN NUMBER)
      RETURN user_auth_roles_ref
   IS
      vcursor   user_auth_roles_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usar_sar_code,
                saa_sht_desc,
                saa_name,
                saa_sys_code,
                sys_name
           FROM tqc_user_web_system_auth_roles,
                tqc_web_system_auth_areas,
                tqc_web_systems
          WHERE     sys_code = saa_sys_code
                AND saa_code = usar_sar_code
                AND sys_code = sys_code
                AND usar_usr_code = user_code;

      RETURN (vcursor);
   END;

   FUNCTION agent_authenticate (vusername          IN     VARCHAR2,
                                vpwd               IN     VARCHAR2,
                                vmsg                  OUT VARCHAR2,
                                lastlogindate         OUT DATE,
                                agentcode             OUT NUMBER,
                                agentname             OUT VARCHAR2,
                                agentshtdesc          OUT VARCHAR2,
                                agentcontactcode      OUT NUMBER,
                                agentcontactname      OUT VARCHAR2,
                                v_user_code           OUT NUMBER)
      RETURN INTEGER
   IS
      voutput        INTEGER;
      agentbrncode   NUMBER;
   BEGIN
      -- RAISE_APPLICATION_ERROR(-20000,'Password '||vpwd);
      voutput :=
         check_agent_pwd (vusername,
                          vpwd,
                          vmsg,
                          lastlogindate,
                          agentcode,
                          agentname,
                          agentshtdesc,
                          agentcontactcode,
                          agentcontactname,
                          NULL,
                          NULL,
                          'N',
                          agentbrncode,
                          v_user_code);
      RETURN (voutput);
   END;


   FUNCTION agent_authenticate (vusername          IN     VARCHAR2,
                                vpwd               IN     VARCHAR2,
                                v_pwd_reentry      IN     VARCHAR2,
                                v_new_pwd1         IN     VARCHAR2,
                                v_new_pwd2         IN     VARCHAR2,
                                vmsg                  OUT VARCHAR2,
                                lastlogindate         OUT DATE,
                                agentcode             OUT NUMBER,
                                agentname             OUT VARCHAR2,
                                agentshtdesc          OUT VARCHAR2,
                                agentcontactcode      OUT NUMBER,
                                agentcontactname      OUT VARCHAR2,
                                agentbrncode          OUT NUMBER,
                                v_system_code         OUT NUMBER)
      RETURN INTEGER
   IS
      voutput      INTEGER;
      v_usercode   NUMBER;
   BEGIN
      voutput :=
         check_agent_pwd (vusername,
                          vpwd,
                          vmsg,
                          lastlogindate,
                          agentcode,
                          agentname,
                          agentshtdesc,
                          agentcontactcode,
                          agentcontactname,
                          v_new_pwd2,
                          v_new_pwd2,
                          v_pwd_reentry,
                          agentbrncode,
                          v_usercode,
                          v_system_code);
      RETURN (voutput);
   END;

   FUNCTION agent_authenticate (vusername          IN     VARCHAR2,
                                vpwd               IN     VARCHAR2,
                                v_pwd_reentry      IN     VARCHAR2,
                                v_new_pwd1         IN     VARCHAR2,
                                v_new_pwd2         IN     VARCHAR2,
                                vmsg                  OUT VARCHAR2,
                                lastlogindate         OUT DATE,
                                agentcode             OUT NUMBER,
                                agentname             OUT VARCHAR2,
                                agentshtdesc          OUT VARCHAR2,
                                agentcontactcode      OUT NUMBER,
                                agentcontactname      OUT VARCHAR2,
                                agentbrncode          OUT NUMBER)
      RETURN INTEGER
   IS
      voutput      INTEGER;
      v_usercode   NUMBER;
   BEGIN
      voutput :=
         check_agent_pwd (vusername,
                          vpwd,
                          vmsg,
                          lastlogindate,
                          agentcode,
                          agentname,
                          agentshtdesc,
                          agentcontactcode,
                          agentcontactname,
                          v_new_pwd2,
                          v_new_pwd2,
                          v_pwd_reentry,
                          agentbrncode,
                          v_usercode);
      RETURN (voutput);
   END;

   FUNCTION client_authenticate (vusername          IN     VARCHAR2,
                                 vpwd               IN     VARCHAR2,
                                 vmsg                  OUT VARCHAR2,
                                 lastlogindate         OUT DATE,
                                 agentcode             OUT NUMBER,
                                 agentname             OUT VARCHAR2,
                                 agentcontactcode      OUT NUMBER,
                                 agentcontactname      OUT VARCHAR2)
      RETURN INTEGER
   IS
      voutput   INTEGER;
   BEGIN
      voutput :=
         check_client_pwd (vusername,
                           vpwd,
                           vmsg,
                           lastlogindate,
                           agentcode,
                           agentname,
                           agentcontactcode,
                           agentcontactname,
                           NULL,
                           NULL,
                           'N');
      RETURN (voutput);
   END;



   FUNCTION client_authenticate (vusername          IN     VARCHAR2,
                                 vpwd               IN     VARCHAR2,
                                 v_pwd_reentry      IN     VARCHAR2,
                                 v_new_pwd1         IN     VARCHAR2,
                                 v_new_pwd2         IN     VARCHAR2,
                                 vmsg                  OUT VARCHAR2,
                                 lastlogindate         OUT DATE,
                                 agentcode             OUT NUMBER,
                                 agentname             OUT VARCHAR2,
                                 agentcontactcode      OUT NUMBER,
                                 agentcontactname      OUT VARCHAR2)
      RETURN INTEGER
   IS
      voutput   INTEGER;
   BEGIN
      voutput :=
         check_client_pwd (vusername,
                           vpwd,
                           vmsg,
                           lastlogindate,
                           agentcode,
                           agentname,
                           agentcontactcode,
                           agentcontactname,
                           v_new_pwd2,
                           v_new_pwd2,
                           v_pwd_reentry);
      RETURN (voutput);
   END;

   FUNCTION client_authenticate (vusername         IN     VARCHAR2,
                                 vpwd              IN     VARCHAR2,
                                 v_pwd_reentry     IN     VARCHAR2,
                                 v_new_pwd1        IN     VARCHAR2,
                                 v_new_pwd2        IN     VARCHAR2,
                                 v_resetcode       IN     VARCHAR2,
                                 v_msg                OUT VARCHAR2,
                                 v_lastlogindate      OUT DATE,
                                 v_clientcode         OUT NUMBER,
                                 v_clientname         OUT VARCHAR2,
                                 v_accountcode        OUT NUMBER)
      RETURN INTEGER
   IS
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      -- v_last_login_date DATE;
      v_pwd                VARCHAR2 (100);
      --v_msg VARCHAR2(150);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      x                    NUMBER := 0;
      nusercode            NUMBER;
      v_reset              VARCHAR2 (100);

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
           SELECT cwwph_pwd, cwwph_pwd_change_dt
             FROM tqc_client_web_pwd_hist
            WHERE UPPER (cwwph_acwa_code) = vuser
         ORDER BY cwwph_pwd_change_dt DESC;
   BEGIN
      --MSG_BOX('DANI');
      --  RAISE_ERROR(v_entered_user);
      BEGIN
         SELECT acwa_code,
                UPPER (acwa_username),
                UPPER (acwa_status),
                acwa_pwd,
                acwa_pwd_changed,
                acwa_dt_created,
                acwa_personel_rank,
                acwa_pwd_reset,
                acwa_login_attempts,
                NVL (acwa_last_login_date, TRUNC (SYSDATE)),
                clnt_code,
                LTRIM (RTRIM (clnt_surname || ' ' || clnt_other_names)),
                acwa_code,
                acwa_reset_code
           INTO nusercode,
                v_us_id,
                v_status,
                v_pwd,
                v_pwd_changed_dt,
                v_pwd_date_created,
                v_per_rnk,
                v_pwdreset,
                v_loginattempts,
                v_lastlogindate,
                v_clientcode,
                v_clientname,
                v_accountcode,
                v_reset
           FROM tqc_client_web_accounts, tqc_clients
          WHERE clnt_code(+) = acwa_clnt_code AND acwa_username = vusername;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_msg :=
               'The user is not a valid TurnQuest user. Enter valid username ';
            RETURN (3);
         WHEN OTHERS
         THEN
            v_msg := 'Unable to verify the username and password.';
            RETURN (3);
      END;

      BEGIN
         v_syspwdattempts :=
            tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLOGATTEMPTS');
         v_syspwdoldusecnt :=
            tqc_parameters_pkg.get_param_number ('SYSCLNTPWDOLDUSECNT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Error getting parameters...';
            RETURN (3);
      END;

      IF v_reset <> v_resetcode
      THEN
         v_msg := 'Reset Code Incorrect. Please Check.';
         RETURN (4);
      END IF;

      IF NVL (v_syspwdattempts, 0) > 0
         AND NVL (v_loginattempts, 0) >= NVL (v_syspwdattempts, 0)
      THEN
         v_msg :=
            'Max of ' || NVL (v_syspwdattempts, 0)
            || ' login attempt failure attained. Contact System Administrator for assistance.';
         RETURN (3);
      ELSIF NVL (v_pwd, '******') != gis_read.val (vpwd)
            AND NOT (NVL (v_pwdreset, 'N') = 'Y' OR v_pwd_reentry = 'Y')
      THEN
         update_login_attempt (vusername);
         v_msg :=
            'Invalid username or password. Enter valid username and password and try again.';
         RETURN (2);
      END IF;

      IF (NVL (v_pwdreset, 'N') = 'Y' OR v_pwd_reentry = 'Y')
         AND (v_new_pwd1 IS NOT NULL OR v_new_pwd2 IS NOT NULL)
      THEN
         IF NVL (v_new_pwd1, '****') != NVL (v_new_pwd2, '%%%%')
         THEN
            v_msg :=
               'Your password has been re-entered incorrectly. Please try again! ';
            RETURN (3);
         ELSE
            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt
                 FROM tqc_client_web_pwd_hist
                WHERE cwwph_acwa_username = vusername
                      AND cwwph_pwd = gis_read.val (v_new_pwd1);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Cannot verify if password ever used before..';
                  RETURN (3);
            END;

            IF NVL (v_cnt, 0) > NVL (v_syspwdoldusecnt, 0)
            THEN
               v_msg :=
                  'Cannot use a password more than '
                  || NVL (v_syspwdoldusecnt, 0)
                  || ' time(s). Please specify a new password never user before.. ';
               RETURN (3);
            END IF;

            BEGIN
               x := 0;

               FOR usrs IN usrpwds (vusername)
               LOOP
                  IF usrs.cwwph_pwd = gis_read.val (v_new_pwd1)
                  THEN
                     v_msg :=
                        'Cannot Reuse the last two passwords. Please specify a new password never user before.. ';
                     RETURN (3);
                  END IF;

                  x := x + 1;

                  IF x >= 2
                  THEN
                     EXIT;
                  END IF;
               END LOOP;
            END;

            --ELSE
            BEGIN
               UPDATE tqc_client_web_accounts
                  SET acwa_pwd_reset = 'N',
                      acwa_pwd_changed = TRUNC (SYSDATE),
                      acwa_pwd = gis_read.val (v_new_pwd1),
                      acwa_login_attempts = 0
                WHERE UPPER (acwa_username) = vusername;
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'User password could not be changed...';
                  RETURN (3);
            END;

            BEGIN
               INSERT INTO tqc_client_web_pwd_hist (cwwph_code,
                                                    cwwph_acwa_code,
                                                    cwwph_acwa_username,
                                                    cwwph_pwd,
                                                    cwwph_pwd_change_dt)
                    VALUES (tqc_cwwph_code_seq.NEXTVAL,
                            nusercode,
                            vusername,
                            gis_read.val (v_new_pwd1),
                            SYSDATE);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Error updating password change history..';
                  RETURN (3);
            END;

            v_pwdreset := 'N';
            v_pwd_changed_dt := TRUNC (SYSDATE);
            v_loginattempts := 0;
         END IF;
      END IF;

      BEGIN
         plenght := LENGTH (vpwd);

         FOR x IN 1 .. plenght
         LOOP
            IF ASCII (SUBSTR (vpwd, x, 1)) BETWEEN 65 AND 90
            THEN
               capscnt := NVL (capscnt, 0) + 1;
            ELSIF ASCII (SUBSTR (vpwd, x, 1)) BETWEEN 97 AND 122
            THEN
               lwrcnt := NVL (lwrcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (vpwd, x, 1)) BETWEEN 49 AND 57
            THEN
               numcnt := NVL (numcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (vpwd, x, 1)) IN (35, 36, 37, 38, 42, 64)
            THEN
               speccnt := NVL (numcnt, 0) + 1;
            END IF;
         END LOOP;
      END;

      v_syspwddrtntime :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDDRTNTIME');
      v_syspwdprmttime :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDPRMTTIME');
      v_syspwdlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLENGTH');
      v_sysupppwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDUPPENGTH');
      v_syslwrpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLWRLENGTH');
      v_sysnumpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDNUMLENGTH');
      v_sysspecpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDSPECLENGTH');
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      IF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
            (TO_NUMBER (v_syspwddrtntime))
      THEN
         v_msg :=
            'Your password has expired. Please specify new password to continue.';
         v_check_failed := TRUE;
      ELSIF NVL (v_pwdreset, 'N') = 'Y' AND v_reset IS NOT NULL
      THEN
         v_msg := 'Your password has been reset. MUST specify a new password.';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF UPPER (vusername) = UPPER (vpwd)
      THEN
         v_msg :=
            'Your password is same as the username. This is not allowed.';
         v_check_failed := TRUE;
      ELSIF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
               (TO_NUMBER (v_syspwddrtntime) - TO_NUMBER (v_syspwdprmttime))
      THEN
         v_msg :=
            'Your password is due to expire in '
            || ( (TO_NUMBER (v_syspwddrtntime) * 1440)
                - (TO_NUMBER (v_syspwdprmttime) * 1440))
               / 1440
            || ' days. ';
         v_check_failed := TRUE;
         v_mandatory := FALSE;
      ELSIF NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';
         v_check_failed := TRUE;
      ELSIF NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysupppwlength
            || ' upper case characters ';
         v_check_failed := TRUE;
      ELSIF NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';
         v_check_failed := TRUE;
      ELSIF NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysnumpwlength
            || ' numeric characters ';
         v_check_failed := TRUE;
      ELSIF NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
            AND NVL (v_sysspecpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysspecpwlength
            || ' special characters ';
         v_check_failed := TRUE;
         v_mandatory := FALSE;
      END IF;

      IF v_check_failed AND NOT v_mandatory
      THEN
         --v_msg := v_msg || ' Do you want to change it now? ';
         RETURN (4);
      ELSIF v_check_failed AND v_mandatory
      THEN
         v_msg := v_msg || ' Login not allowed ';
         RETURN (2);
      END IF;

      UPDATE tqc_client_web_accounts
         SET acwa_last_login_date = TRUNC (SYSDATE)
       WHERE acwa_username = vusername;

      COMMIT;
      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   END;

   FUNCTION user_authenticate (vusername       IN     VARCHAR2,
                               vpwd            IN     VARCHAR2,
                               vmsg               OUT VARCHAR2,
                               lastlogindate      OUT DATE,
                               usercode           OUT NUMBER,
                               username           OUT VARCHAR2,
                               v_sys_code      IN     NUMBER)
      RETURN INTEGER
   IS
      voutput   INTEGER;
   BEGIN
      --RAISE_ERROR(vusername);
      voutput :=
         check_user_pwd (vusername,
                         vpwd,
                         vmsg,
                         lastlogindate,
                         usercode,
                         username,
                         NULL,
                         NULL,
                         'N',
                         v_sys_code);
      RETURN (voutput);
   END;

   FUNCTION user_authenticate (vusername       IN     VARCHAR2,
                               vpwd            IN     VARCHAR2,
                               v_pwd_reentry   IN     VARCHAR2,
                               v_new_pwd1      IN     VARCHAR2,
                               v_new_pwd2      IN     VARCHAR2,
                               vmsg               OUT VARCHAR2,
                               lastlogindate      OUT DATE,
                               usercode           OUT NUMBER,
                               username           OUT VARCHAR2,
                               v_sys_code      IN     NUMBER)
      RETURN INTEGER
   IS
      voutput   INTEGER;
   BEGIN
      voutput :=
         check_user_pwd (vusername,
                         vpwd,
                         vmsg,
                         lastlogindate,
                         usercode,
                         username,
                         v_new_pwd2,
                         v_new_pwd2,
                         v_pwd_reentry,
                         v_sys_code);
      RETURN (voutput);
   END;

   FUNCTION user_sys_authenticate (vusername       IN     VARCHAR2,
                                   vmsg               OUT VARCHAR2,
                                   lastlogindate      OUT DATE,
                                   usercode           OUT NUMBER,
                                   username           OUT VARCHAR2,
                                   v_sys_code      IN     NUMBER)
      RETURN INTEGER
   IS
      voutput   INTEGER;
   BEGIN
      voutput :=
         check_user_sys (vusername,
                         vmsg,
                         lastlogindate,
                         usercode,
                         username,
                         v_sys_code);
      RETURN (voutput);
   END;

   FUNCTION get_usr_branches (v_usrname IN VARCHAR2)
      RETURN usr_branches_ref
   IS
      v_refcur    usr_branches_ref;
      v_usrcode   NUMBER;
   BEGIN
      v_usrcode := tqc_interfaces_pkg.usercode (v_usrname);
      v_refcur := get_usr_branches (v_usrcode);
      RETURN (v_refcur);
   END;

   FUNCTION get_dflt_usr_branch_div (v_usrname IN VARCHAR2)
      RETURN usr_divisions_ref
   IS
      v_refcur    usr_divisions_ref;
      v_usrcode   NUMBER;
   BEGIN
      v_usrcode := tqc_interfaces_pkg.usercode (v_usrname);
      v_refcur := get_usr_dflt_divisions (v_usrcode);
      RETURN (v_refcur);
   END;

   FUNCTION get_usr_branches (v_usrcode IN NUMBER)
      RETURN usr_branches_ref
   IS
      v_refcur   usr_branches_ref;
   BEGIN
      raise_error (v_usrcode);

      OPEN v_refcur FOR
           SELECT DISTINCT brn_code,
                           brn_sht_desc,
                           org_sht_desc || '-' || brn_name,
                           brn_gen_pol_clm
             FROM tqc_branches,
                  tqc_regions,
                  tqc_organizations,
                  tqc_systems
            WHERE     reg_org_code = org_code
                  AND sys_org_code = org_code
                  AND brn_reg_code = reg_code
                  AND brn_code IN (SELECT usb_brn_code
                                     FROM tqc_user_branches
                                    WHERE usb_usr_code = v_usrcode)
         ORDER BY brn_name;

      RETURN (v_refcur);
   END;

   FUNCTION get_usr_divisions_val (v_usrcode IN NUMBER)
      RETURN VARCHAR2
   IS
      CURSOR cur
      IS
         SELECT *
           FROM tqc_user_divisions, tqc_divisions
          WHERE usd_div_code = div_code(+) AND usd_usr_code = v_usrcode;

      v_branch_codes   VARCHAR2 (300);
   BEGIN
      FOR i IN cur
      LOOP
         IF v_branch_codes IS NULL
         THEN
            v_branch_codes := i.usd_div_code;
         ELSE
            v_branch_codes := v_branch_codes || ',' || i.usd_div_code;
         END IF;
      END LOOP;

      RETURN (v_branch_codes);
   END;

   FUNCTION get_usr_divisions (v_usrcode IN NUMBER)
      RETURN usr_divisions_ref
   IS
      v_refcur   usr_divisions_ref;
   BEGIN
      OPEN v_refcur FOR
         SELECT usd_div_code, div_sht_desc, div_name
           FROM tqc_user_divisions, tqc_divisions
          WHERE usd_div_code = div_code AND usd_usr_code = v_usrcode;

      RETURN (v_refcur);
   END;

   FUNCTION get_usr_dflt_divisions (v_usrcode IN NUMBER)
      RETURN usr_divisions_ref
   IS
      v_refcur   usr_divisions_ref;
   BEGIN
      OPEN v_refcur FOR
         SELECT usd_div_code, div_sht_desc, div_name
           FROM tqc_user_divisions, tqc_divisions
          WHERE     usd_div_code = div_code
                AND usd_default = 'Y'
                AND usd_usr_code = v_usrcode;

      RETURN (v_refcur);
   END;

   FUNCTION get_usr_dflt_branch (v_usrname IN VARCHAR2)
      RETURN usr_branches_ref
   IS
      v_refcur    usr_branches_ref;
      v_usrcode   NUMBER;
   BEGIN
      v_usrcode := tqc_interfaces_pkg.usercode (v_usrname);
      v_refcur := get_usr_dflt_branch (v_usrcode);
      RETURN (v_refcur);
   END;

   FUNCTION get_usr_dflt_branch (v_usrcode IN NUMBER)
      RETURN usr_branches_ref
   IS
      v_refcur   usr_branches_ref;
   BEGIN
      OPEN v_refcur FOR
           SELECT DISTINCT brn_code,
                           brn_sht_desc,
                           org_sht_desc || '-' || brn_name,
                           brn_gen_pol_clm
             FROM tqc_branches,
                  tqc_regions,
                  tqc_organizations,
                  tqc_systems
            WHERE     reg_org_code = org_code
                  AND sys_org_code = org_code
                  AND brn_reg_code = reg_code
                  AND brn_code IN
                         (SELECT usb_brn_code
                            FROM tqc_user_branches
                           WHERE usb_usr_code = v_usrcode
                                 AND NVL (usb_dflt_brn, 'N') = 'Y')
         ORDER BY brn_name;

      RETURN (v_refcur);
   END;

   FUNCTION get_currencies
      RETURN currencies_ref
   IS
      v_refcur   currencies_ref;
   BEGIN
      OPEN v_refcur FOR SELECT cur_code, cur_symbol FROM tqc_currencies;

      RETURN (v_refcur);
   END;

   PROCEDURE get_process_dtls (v_syscode       IN     NUMBER,
                               v_process       IN     VARCHAR2,
                               v_process_cur      OUT process_rec)
   IS
   BEGIN
      OPEN v_process_cur FOR
         SELECT sprc_code,
                sprc_bpm_id,
                sprc_sht_desc,
                sprc_jpdl_desc
           FROM tqc_sys_processes
          WHERE sprc_sys_code = v_syscode AND sprc_sht_desc = v_process;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('error determining the process to be undertaken...');
   END;

   PROCEDURE save_process_attributes (
      v_tckt_code          NUMBER,
      v_sys_code           NUMBER,
      v_sys_module         VARCHAR2,
      v_clnt_code          NUMBER,
      v_agn_code           NUMBER,
      v_pol_code           NUMBER,
      v_pol_no             VARCHAR2,
      v_clm_no             VARCHAR2,
      v_quot_code          NUMBER,
      v_user               VARCHAR2,
      v_tckt_date          DATE,
      v_process_id         VARCHAR2,
      v_quot_no            VARCHAR2,
      v_endr_code          NUMBER,
      v_prod_type          VARCHAR2,
      v_tckt_to            VARCHAR2,
      v_remarks            VARCHAR2,
      v_endorsment         VARCHAR2,
      v_trans_no           NUMBER,
      v_type            IN VARCHAR2 DEFAULT 'S',
      v_extern_ref_no   IN VARCHAR2 DEFAULT NULL,
      v_adhoc_name      IN VARCHAR2 DEFAULT NULL)
   IS
      v_cnt   NUMBER;
      v_end   NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (tckt_code)
           INTO v_cnt
           FROM tqc_bpm_tickets
          WHERE tckt_code = v_tckt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('error determining ticket information...');
      END;

      -- RAISE_ERROR('COUNT '||v_cnt||'TCKT_SYS_CODE'||v_sys_code||'TCKT_SYS_MODULE'||v_sys_module||'v_tckt_code'||v_tckt_code);
      IF v_cnt > 0
      THEN
         SELECT tckt_code
           INTO v_end
           FROM tqc_bpm_tickets
          WHERE tckt_code = v_tckt_code;
      ELSE
         BEGIN
            INSERT INTO tqc_bpm_tickets (tckt_code,
                                         tckt_sys_code,
                                         tckt_sys_module,
                                         tckt_clnt_code,
                                         tckt_agn_code,
                                         tckt_pol_code,
                                         tckt_pol_no,
                                         tckt_clm_no,
                                         tckt_quot_code,
                                         tckt_by,
                                         tckt_date,
                                         tckt_process_id,
                                         tckt_quo_no,
                                         tckt_endr_code,
                                         tckt_prod_type,
                                         tckt_to,
                                         tckt_remarks,
                                         tckt_endorsement,
                                         tckt_transno,
                                         tckt_type,
                                         tckt_extern_ref_no,
                                         tckt_adhoc_name)
                 VALUES (v_tckt_code,
                         v_sys_code,
                         v_sys_module,
                         v_clnt_code,
                         v_agn_code,
                         v_pol_code,
                         v_pol_no,
                         v_clm_no,
                         v_quot_code,
                         v_user,
                         SYSDATE,
                         v_process_id,
                         v_quot_no,
                         v_endr_code,
                         v_prod_type,
                         v_tckt_to,
                         v_remarks,
                         v_endorsment,
                         v_trans_no,
                         v_type,
                         v_extern_ref_no,
                         v_adhoc_name);

            COMMIT;
         -- RAISE_ERROR('COUNT '||v_cnt||'TCKT_SYS_CODE'||v_sys_code||'TCKT_SYS_MODULE'||v_sys_module||'v_tckt_code'||v_tckt_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error inserting ticket attribute information..');
         END;
      END IF;
   END;

   PROCEDURE update_process_attributes (v_tckt_code     NUMBER,
                                        v_sys_code      NUMBER,
                                        v_sys_module    VARCHAR2,
                                        v_clnt_code     NUMBER,
                                        v_agn_code      NUMBER,
                                        v_pol_code      NUMBER,
                                        v_pol_no        VARCHAR2,
                                        v_clm_no        VARCHAR2,
                                        v_quot_code     NUMBER,
                                        v_user          VARCHAR2,
                                        v_tckt_date     DATE,
                                        v_process_id    VARCHAR2,
                                        v_quot_no       VARCHAR2,
                                        v_endr_code     NUMBER,
                                        v_prod_type     VARCHAR2,
                                        v_tckt_to       VARCHAR2,
                                        v_remarks       VARCHAR2,
                                        v_endorsment    VARCHAR2,
                                        v_trans_no      NUMBER)
   IS
   BEGIN
      UPDATE tqc_bpm_tickets
         SET tckt_sys_code = NVL (v_sys_code, tckt_sys_code),
             tckt_sys_module = NVL (v_sys_module, tckt_sys_module),
             tckt_clnt_code = NVL (v_clnt_code, tckt_clnt_code),
             tckt_agn_code = NVL (v_agn_code, tckt_agn_code),
             tckt_pol_code = NVL (v_pol_code, tckt_pol_code),
             tckt_pol_no = NVL (v_pol_no, tckt_pol_no),
             tckt_clm_no = NVL (v_clm_no, tckt_clm_no),
             tckt_quot_code = NVL (v_quot_code, tckt_quot_code),
             tckt_by = NVL (v_user, tckt_by),
             --TCKT_DATE = NVL(),
             --TCKT_PROCESS_ID = NVL(),
             tckt_quo_no = NVL (v_quot_no, tckt_quo_no),
             tckt_endr_code = NVL (v_endr_code, tckt_endr_code),
             tckt_prod_type = NVL (v_prod_type, tckt_prod_type),
             --TCKT_TO = NVL(),
             tckt_remarks = NVL (v_remarks, tckt_remarks),
             tckt_endorsement = NVL (v_endorsment, tckt_endorsement),
             tckt_transno = NVL (v_trans_no, tckt_transno)
       WHERE tckt_code = v_tckt_code;

      COMMIT;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error updating ticket attribute information...');
   END;

   PROCEDURE reassign_task (v_taskid    IN VARCHAR2,
                            v_user      IN VARCHAR2,
                            v_remarks      VARCHAR2)
   IS
      v_assigned_by   VARCHAR2 (50)
         := pkg_global_vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
   BEGIN
      UPDATE tqc_bpm_tickets
         SET tckt_to = v_user,
             tckt_remarks = v_remarks,
             tckt_assignment_date = SYSDATE,
             tckt_assigned_by = v_assigned_by
       WHERE tckt_code = v_taskid;

      UPDATE jbpm4_task
         SET assignee_ = v_user
       WHERE dbid_ = v_taskid;

      COMMIT;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('error updating task details for reassignment...');
   END;

   PROCEDURE check_task_completion (v_task_point   IN     VARCHAR2,
                                    v_taskid       IN     VARCHAR2,
                                    v_sys_code     IN     NUMBER,
                                    v_complete        OUT VARCHAR2)
   IS
      v_point_desc   VARCHAR2 (100);
      v_task_name    VARCHAR2 (100);
   BEGIN
      BEGIN
         --RAISE_ERROR('Task Point '||v_task_point||' Task ID '||v_taskID);
         SELECT tptp_desc
           INTO v_point_desc
           FROM tqc_bpm_task_points
          WHERE tptp_sht_desc = v_task_point
                AND tptp_sys_code = NVL (v_sys_code, 27);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (
                  'error determining the task point...'
               || v_task_point
               || '='
               || v_taskid);
      END;

      BEGIN
         SELECT name_
           INTO v_task_name
           FROM jbpm4_task
          WHERE dbid_ = v_taskid;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (
                  'error determining the task details...'
               || v_taskid
               || '='
               || v_task_point);
      END;

      IF v_point_desc = v_task_name
      THEN
         v_complete := 'Y';
      ELSE
         v_complete := 'N';
      END IF;
   END;

   FUNCTION get_tckt_dtls (v_tckt_code IN NUMBER)
      RETURN tickets_ref
   IS
      v_refcur   tickets_ref;
   BEGIN
      OPEN v_refcur FOR
           /*
                    select TQC_BPM_TICKETS.TCKT_CODE, DECODE(TQC_BPM_TICKETS.TCKT_SYS_CODE,
                                             37, 'GENERAL INSURANCE SYSTEM',
                                             27, 'LIFE MANAGEMENT SYSTEM',
                                             1, 'FINANCIAL MANAGEMENT SYSTEM',
                                             0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')USRSYSTEM,
                    DECODE(TQC_BPM_TICKETS.TCKT_SYS_MODULE, 'Q', 'Quotation',
                                                   'P', 'Underwriting',
                                                   'C', 'Claims')SYSMODULE,
                    TQC_BPM_TICKETS.TCKT_CLNT_CODE, (CLNT_NAME || ' ' || CLNT_OTHER_NAMES) CLIENT,
                    TQC_BPM_TICKETS.TCKT_AGN_CODE, AGN_NAME AGENT, TQC_BPM_TICKETS.TCKT_POL_CODE,
                    TQC_BPM_TICKETS.TCKT_POL_NO, TQC_BPM_TICKETS.TCKT_CLM_NO, TQC_BPM_TICKETS.TCKT_QUOT_CODE,
                    TQC_BPM_TICKETS.TCKT_QUO_NO, TQC_BPM_TICKETS.TCKT_BY, TQC_BPM_TICKETS.TCKT_DATE, TQC_BPM_TICKETS.TCKT_PROCESS_ID,
                    TQC_BPM_TICKETS.TCKT_SYS_MODULE SYS_MODULE_CODE
                    from TQC_BPM_TICKETS, LMS_PROPOSERS, TQC_CLIENTS, LMS_AGENCIES, LMS_POLICIES, LMS_CLAIMS,
                    LMS_QUOTATIONS
                    WHERE TQC_BPM_TICKETS.TCKT_CLNT_CODE = PRP_CODE
                    AND PRP_CLNT_CODE = CLNT_CODE
                    AND AGN_CODE = TQC_BPM_TICKETS.TCKT_AGN_CODE(+)
                    AND TQC_BPM_TICKETS.TCKT_CODE = v_tckt_code
                    order by TQC_BPM_TICKETS.TCKT_CODE;

                    */
           SELECT tqc_bpm_tickets.tckt_code,
                  DECODE (tqc_bpm_tickets.tckt_sys_code,
                          37, 'GENERAL INSURANCE SYSTEM',
                          27, 'LIFE MANAGEMENT SYSTEM',
                          26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                          1, 'FINANCIAL MANAGEMENT SYSTEM',
                          0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                     usrsystem,
                  DECODE (tqc_bpm_tickets.tckt_sys_module,
                          'Q', 'Quotation',
                          'P', 'Underwriting',
                          'E', 'Endorsements',
                          'R', 'Renewals',
                          'RT', 'Transfered Renewal',
                          'C', 'Claims',
                          'RE', 'Reinstatement')
                     sysmodule,
                  tqc_bpm_tickets.tckt_clnt_code,
                  (clnt_name || ' ' || clnt_other_names) client,
                  tqc_bpm_tickets.tckt_agn_code,
                  agn_name AGENT,
                  tqc_bpm_tickets.tckt_pol_code,
                  tqc_bpm_tickets.tckt_pol_no,
                  tqc_bpm_tickets.tckt_clm_no,
                  tqc_bpm_tickets.tckt_quot_code,
                  tqc_bpm_tickets.tckt_quo_no,
                  tqc_bpm_tickets.tckt_by,
                  tqc_bpm_tickets.tckt_date,
                  tqc_bpm_tickets.tckt_process_id,
                  tqc_bpm_tickets.tckt_sys_module sys_module_code,
                  tckt_endr_code,
                  tckt_prod_type,
                  tckt_to,
                  tckt_remarks,
                  NULL,
                  NULL,
                  tckt_endorsement,
                  tckt_transno,
                  DECODE (tckt_sys_module,
                          'Q', tckt_quo_no,
                          'C', tckt_clm_no,
                          tckt_pol_no)
                     ref_no,
                  tckt_prp_code,
                  NULL,
                  tckt_type,
                  NULL,
                  TCKT_TRAN_EFF_DATE,
                  TCKT_GGT_NO,
                  NULL,
                  NVL (tqc_bpm_tickets.tckt_assignment_date,
                       tqc_bpm_tickets.tckt_date)
                     assignment_date
             FROM tqc_bpm_tickets, tqc_clients, tqc_agencies
            WHERE     tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                  --AND PRP_CLNT_CODE = CLNT_CODE(+)
                  AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                  AND tqc_bpm_tickets.tckt_code = v_tckt_code
                  AND tckt_active != 'N'
         ORDER BY tqc_bpm_tickets.tckt_code;

      RETURN (v_refcur);
   END;

   FUNCTION getchqrtcktdts (v_tckt_code IN NUMBER)
      RETURN chrq_tckt_ref
   IS
      v_refcur   chrq_tckt_ref;
   BEGIN
      OPEN v_refcur FOR
           SELECT tqc_bpm_tickets.tckt_code,
                  DECODE (tqc_bpm_tickets.tckt_sys_module,
                          'N', 'NEW',
                          'A', 'AUTHORIZED',
                          'RJ', 'REJECTED',
                          'P', 'PRINTED')
                     sysmodule,
                  fms_cheques.cqr_no,
                  fms_cheques.cqr_payee,
                  fms_cheques.cqr_narrative,
                  tqc_bpm_tickets.tckt_by,
                  tqc_bpm_tickets.tckt_date,
                  tqc_bpm_tickets.tckt_process_id,
                  tqc_bpm_tickets.tckt_sys_module,
                  tqc_bpm_tickets.tckt_to,
                  tqc_bpm_tickets.tckt_remarks,
                  name_ tckt_name,
                  duedate_ tckt_due_date
             FROM tqc_bpm_tickets, fms_cheques, jbpm4_task
            WHERE     tqc_bpm_tickets.tckt_cqr_no = cqr_no(+)
                  AND dbid_ = tqc_bpm_tickets.tckt_code
                  AND tqc_bpm_tickets.tckt_code = v_tckt_code
                  AND tckt_active != 'N'
                  AND tckt_sys_code = 1
         ORDER BY tqc_bpm_tickets.tckt_code DESC;

      RETURN (v_refcur);
   END;



   FUNCTION get_tckt_dtls_by_usr (v_user       IN VARCHAR2,
                                  v_quo_code      NUMBER DEFAULT NULL,
                                  v_pol_code      NUMBER DEFAULT NULL,
                                  v_claim_no      VARCHAR2 DEFAULT NULL,
                                  v_syscode       NUMBER DEFAULT NULL,
                                  v_cla_type      VARCHAR2 DEFAULT NULL,
                                  v_tkt_type      VARCHAR2 DEFAULT NULL --L:LMS, D:DMS
                                                                       )
      RETURN tickets_ref
   IS
      v_refcur   tickets_ref;
   BEGIN
      --    raise_error('v_syscode = '||v_syscode
      --        ||'; v_user = '||v_user
      --        ||'; v_pol_code = '||v_pol_code
      --        ||'; v_claim_no = '||v_claim_no
      --        ||'; v_cla_type = '||v_cla_type
      --        ||'; v_quo_code = '||v_quo_code
      --    );
      IF v_quo_code IS NOT NULL
      THEN
         BEGIN
            OPEN v_refcur FOR
               SELECT a.*
                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                DECODE (
                                   tqc_bpm_tickets.tckt_sys_code,
                                   37, 'GENERAL INSURANCE SYSTEM',
                                   27, 'LIFE MANAGEMENT SYSTEM',
                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                   usrsystem,
                                DECODE (tqc_bpm_tickets.tckt_sys_module,
                                        'Q', 'Quotation',
                                        'P', 'Underwriting',
                                        'E', 'Endorsements',
                                        'R', 'Renewals',
                                        'RT', 'Transfered Renewal',
                                        'C', 'Claims')
                                   sysmodule,
                                tqc_bpm_tickets.tckt_clnt_code,
                                (clnt_name || ' ' || clnt_other_names) client,
                                tqc_bpm_tickets.tckt_agn_code,
                                agn_name AGENT,
                                tqc_bpm_tickets.tckt_pol_code,
                                tqc_bpm_tickets.tckt_pol_no,
                                tqc_bpm_tickets.tckt_clm_no,
                                tqc_bpm_tickets.tckt_quot_code,
                                tqc_bpm_tickets.tckt_quo_no,
                                NVL (tqc_bpm_tickets.tckt_assigned_by,
                                     tqc_bpm_tickets.tckt_by)
                                   tckt_by,
                                tqc_bpm_tickets.tckt_date,
                                NVL (tqc_bpm_tickets.tckt_process_id,
                                     EXECUTION_ID_)
                                   tckt_process_id,
                                tqc_bpm_tickets.tckt_sys_module sys_module_code,
                                tckt_endr_code,
                                tckt_prod_type,
                                tckt_to,
                                tckt_remarks,
                                name_ tckt_name,
                                duedate_ tckt_due_date,
                                tckt_endorsement,
                                tckt_transno,
                                DECODE (tckt_sys_module,
                                        'Q', tckt_quo_no,
                                        'C', tckt_clm_no,
                                        tckt_pol_no)
                                   ref_no,
                                tckt_prp_code,
                                NULL,
                                tckt_type,
                                NULL POLICY_STATUS,
                                TCKT_TRAN_EFF_DATE,
                                TCKT_GGT_NO,
                                USR_TYPE,
                                NVL (tqc_bpm_tickets.tckt_assignment_date,
                                     tqc_bpm_tickets.tckt_date)
                                   assignment_date
                           FROM tqc_bpm_tickets,
                                tqc_clients,
                                tqc_agencies,
                                jbpm4_task,
                                tqc_users
                          WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+) --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                AND assignee_ = usr_username
                                AND (assignee_ = v_user
                                     OR assignee_ IN
                                           (SELECT GRP.USR_USERNAME
                                              FROM TQC_USERS GRP,
                                                   TQC_GROUP_USERS,
                                                   TQC_USERS USR
                                             WHERE GRP.USR_CODE =
                                                      GUSR_GRP_USR_CODE
                                                   AND USR.USR_CODE =
                                                          GUSR_USR_CODE
                                                   AND USR.USR_USERNAME =
                                                          v_user))
                                AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                                AND dbid_ = tqc_bpm_tickets.tckt_code
                                AND tckt_quot_code =
                                       NVL (v_quo_code, tckt_quot_code)
                                AND tckt_active != 'N'
                                AND tckt_sys_code =
                                       NVL (v_syscode, tckt_sys_code)
                                AND tckt_type = 'S'
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                WHERE a.ref_no IS NOT NULL
               UNION ALL
               SELECT tqc_bpm_tickets.tckt_code,
                      DECODE (tqc_bpm_tickets.tckt_sys_code,
                              37, 'GENERAL INSURANCE SYSTEM',
                              27, 'LIFE MANAGEMENT SYSTEM',
                              26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                              1, 'FINANCIAL MANAGEMENT SYSTEM',
                              0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                         usrsystem,
                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                              'Q', 'Quotation',
                              'P', 'Underwriting',
                              'E', 'Endorsements',
                              'R', 'Renewals',
                              'RT', 'Transfered Renewal',
                              'C', 'Claims')
                         sysmodule,
                      tqc_bpm_tickets.tckt_clnt_code,
                      (clnt_name || ' ' || clnt_other_names) client,
                      tqc_bpm_tickets.tckt_agn_code,
                      agn_name AGENT,
                      tqc_bpm_tickets.tckt_pol_code,
                      tqc_bpm_tickets.tckt_pol_no,
                      tqc_bpm_tickets.tckt_clm_no,
                      tqc_bpm_tickets.tckt_quot_code,
                      tqc_bpm_tickets.tckt_quo_no,
                      NVL (tqc_bpm_tickets.tckt_assigned_by,
                           tqc_bpm_tickets.tckt_by)
                         tckt_by,
                      tqc_bpm_tickets.tckt_date,
                      NVL (tqc_bpm_tickets.tckt_process_id, EXECUTION_ID_)
                         tckt_process_id,
                      tqc_bpm_tickets.tckt_sys_module sys_module_code,
                      tckt_endr_code,
                      tckt_prod_type,
                      tckt_to,
                      tckt_remarks,
                      name_ tckt_name,
                      duedate_ tckt_due_date,
                      tckt_endorsement,
                      tckt_transno,
                      DECODE (tckt_sys_module,
                              'Q', tckt_quo_no,
                              'C', tckt_clm_no,
                              tckt_pol_no)
                         ref_no,
                      tckt_prp_code,
                      NULL,
                      tckt_type,
                      NULL POLICY_STATUS,
                      TCKT_TRAN_EFF_DATE,
                      TCKT_GGT_NO,
                      NULL USR_TYPE,
                      NVL (tqc_bpm_tickets.tckt_assignment_date,
                           tqc_bpm_tickets.tckt_date)
                         assignment_date
                 FROM tqc_bpm_tickets,
                      tqc_clients,
                      tqc_agencies,
                      jbpm4_task
                WHERE     tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                      --AND PRP_CLNT_CODE = CLNT_CODE(+)
                      AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                      AND dbid_ = tqc_bpm_tickets.tckt_code(+)
                      AND TCKT_QUOT_CODE = NVL (v_quo_code, TCKT_QUOT_CODE)
                      AND tckt_active != 'N'
                      AND tckt_sys_code = NVL (v_syscode, tckt_sys_code)
                      AND tckt_type = 'E';

            RETURN (v_refcur);
         END;
      ELSIF v_pol_code IS NOT NULL AND v_claim_no IS NULL
      THEN
         BEGIN
            OPEN v_refcur FOR
               SELECT a.*
                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                DECODE (
                                   tqc_bpm_tickets.tckt_sys_code,
                                   37, 'GENERAL INSURANCE SYSTEM',
                                   27, 'LIFE MANAGEMENT SYSTEM',
                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                   usrsystem,
                                DECODE (tqc_bpm_tickets.tckt_sys_module,
                                        'Q', 'Quotation',
                                        'P', 'Underwriting',
                                        'C', 'Claims')
                                   sysmodule,
                                tqc_bpm_tickets.tckt_clnt_code,
                                (clnt_name || ' ' || clnt_other_names) client,
                                tqc_bpm_tickets.tckt_agn_code,
                                agn_name AGENT,
                                tqc_bpm_tickets.tckt_pol_code,
                                tqc_bpm_tickets.tckt_pol_no,
                                tqc_bpm_tickets.tckt_clm_no,
                                tqc_bpm_tickets.tckt_quot_code,
                                tqc_bpm_tickets.tckt_quo_no,
                                NVL (tqc_bpm_tickets.tckt_assigned_by,
                                     tqc_bpm_tickets.tckt_by)
                                   tckt_by,
                                tqc_bpm_tickets.tckt_date,
                                NVL (tqc_bpm_tickets.tckt_process_id,
                                     EXECUTION_ID_)
                                   tckt_process_id,
                                tqc_bpm_tickets.tckt_sys_module sys_module_code,
                                tckt_endr_code,
                                tckt_prod_type,
                                tckt_to,
                                tckt_remarks,
                                name_ tckt_name,
                                duedate_ tckt_due_date,
                                tckt_endorsement,
                                tckt_transno,
                                DECODE (tckt_sys_module,
                                        'Q', tckt_quo_no,
                                        'C', tckt_clm_no,
                                        tckt_pol_no)
                                   ref_no,
                                tckt_prp_code,
                                pol_client_policy_number,
                                tckt_type,
                                NULL POLICY_STATUS,
                                TCKT_TRAN_EFF_DATE,
                                TCKT_GGT_NO,
                                USR_TYPE,
                                NVL (tqc_bpm_tickets.tckt_assignment_date,
                                     tqc_bpm_tickets.tckt_date)
                                   assignment_date
                           FROM tqc_bpm_tickets,
                                tqc_clients,
                                tqc_agencies,
                                jbpm4_task,
                                gin_policies,
                                tqc_users
                          WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                                AND tckt_pol_code = pol_batch_no
                                AND assignee_ = usr_username
                                AND (assignee_ = v_user
                                     OR assignee_ IN
                                           (SELECT GRP.USR_USERNAME
                                              FROM TQC_USERS GRP,
                                                   TQC_GROUP_USERS,
                                                   TQC_USERS USR
                                             WHERE GRP.USR_CODE =
                                                      GUSR_GRP_USR_CODE
                                                   AND USR.USR_CODE =
                                                          GUSR_USR_CODE
                                                   AND USR.USR_USERNAME =
                                                          v_user))
                                --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                                AND dbid_ = tqc_bpm_tickets.tckt_code
                                AND tckt_pol_code =
                                       NVL (v_pol_code, tckt_pol_code)
                                AND tckt_active != 'N'
                                AND tckt_sys_code =
                                       NVL (v_syscode, tckt_sys_code)
                                AND tckt_type = 'S'
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                WHERE ref_no IS NOT NULL
               UNION ALL
               SELECT tqc_bpm_tickets.tckt_code,
                      DECODE (tqc_bpm_tickets.tckt_sys_code,
                              37, 'GENERAL INSURANCE SYSTEM',
                              27, 'LIFE MANAGEMENT SYSTEM',
                              26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                              1, 'FINANCIAL MANAGEMENT SYSTEM',
                              0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                         usrsystem,
                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                              'Q', 'Quotation',
                              'P', 'Underwriting',
                              'C', 'Claims')
                         sysmodule,
                      tqc_bpm_tickets.tckt_clnt_code,
                      (clnt_name || ' ' || clnt_other_names) client,
                      tqc_bpm_tickets.tckt_agn_code,
                      agn_name AGENT,
                      tqc_bpm_tickets.tckt_pol_code,
                      tqc_bpm_tickets.tckt_pol_no,
                      tqc_bpm_tickets.tckt_clm_no,
                      tqc_bpm_tickets.tckt_quot_code,
                      tqc_bpm_tickets.tckt_quo_no,
                      NVL (tqc_bpm_tickets.tckt_assigned_by,
                           tqc_bpm_tickets.tckt_by)
                         tckt_by,
                      tqc_bpm_tickets.tckt_date,
                      NVL (tqc_bpm_tickets.tckt_process_id, EXECUTION_ID_)
                         tckt_process_id,
                      tqc_bpm_tickets.tckt_sys_module sys_module_code,
                      tckt_endr_code,
                      tckt_prod_type,
                      tckt_to,
                      tckt_remarks,
                      name_ tckt_name,
                      duedate_ tckt_due_date,
                      tckt_endorsement,
                      tckt_transno,
                      DECODE (tckt_sys_module,
                              'Q', tckt_quo_no,
                              'C', tckt_clm_no,
                              tckt_pol_no)
                         ref_no,
                      tckt_prp_code,
                      pol_client_policy_number,
                      tckt_type,
                      NULL POLICY_STATUS,
                      TCKT_TRAN_EFF_DATE,
                      TCKT_GGT_NO,
                      NULL USR_TYPE,
                      NVL (tqc_bpm_tickets.tckt_assignment_date,
                           tqc_bpm_tickets.tckt_date)
                         assignment_date
                 FROM tqc_bpm_tickets,
                      tqc_clients,
                      tqc_agencies,
                      jbpm4_task,
                      gin_policies
                WHERE     tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                      AND tckt_pol_code = pol_batch_no(+)
                      --AND PRP_CLNT_CODE = CLNT_CODE(+)
                      AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                      AND dbid_ = tqc_bpm_tickets.tckt_code
                      AND tckt_pol_code = NVL (v_pol_code, tckt_pol_code)
                      AND tckt_active != 'N'
                      AND tckt_sys_code = NVL (v_syscode, tckt_sys_code)
                      AND tckt_type = 'E';

            RETURN (v_refcur);
         END;
      ELSIF v_claim_no IS NOT NULL
      THEN
         BEGIN
            -- RAISE_ERROR(v_claim_no);
            OPEN v_refcur FOR
               SELECT a.*
                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                DECODE (
                                   tqc_bpm_tickets.tckt_sys_code,
                                   37, 'GENERAL INSURANCE SYSTEM',
                                   27, 'LIFE MANAGEMENT SYSTEM',
                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                   usrsystem,
                                DECODE (tqc_bpm_tickets.tckt_sys_module,
                                        'Q', 'Quotation',
                                        'P', 'Underwriting',
                                        'C', 'Claims')
                                   sysmodule,
                                tqc_bpm_tickets.tckt_clnt_code,
                                (clnt_name || ' ' || clnt_other_names) client,
                                tqc_bpm_tickets.tckt_agn_code,
                                agn_name AGENT,
                                tqc_bpm_tickets.tckt_pol_code,
                                tqc_bpm_tickets.tckt_pol_no,
                                tqc_bpm_tickets.tckt_clm_no,
                                tqc_bpm_tickets.tckt_quot_code,
                                tqc_bpm_tickets.tckt_quo_no,
                                NVL (tqc_bpm_tickets.tckt_assigned_by,
                                     tqc_bpm_tickets.tckt_by)
                                   tckt_by,
                                tqc_bpm_tickets.tckt_date,
                                NVL (tqc_bpm_tickets.tckt_process_id,
                                     EXECUTION_ID_)
                                   tckt_process_id,
                                tqc_bpm_tickets.tckt_sys_module sys_module_code,
                                tckt_endr_code,
                                tckt_prod_type,
                                tckt_to,
                                tckt_remarks,
                                name_ tckt_name,
                                duedate_ tckt_due_date,
                                tckt_endorsement,
                                tckt_transno,
                                DECODE (tckt_sys_module,
                                        'Q', tckt_quo_no,
                                        'C', tckt_clm_no,
                                        tckt_pol_no)
                                   ref_no,
                                tckt_prp_code,
                                NULL,
                                tckt_type,
                                NULL POLICY_STATUS,
                                TCKT_TRAN_EFF_DATE,
                                TCKT_GGT_NO,
                                USR_TYPE,
                                NVL (tqc_bpm_tickets.tckt_assignment_date,
                                     tqc_bpm_tickets.tckt_date)
                                   assignment_date
                           FROM tqc_bpm_tickets,
                                tqc_clients,
                                tqc_agencies,
                                jbpm4_task,
                                tqc_users
                          WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+) --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                                AND assignee_ = usr_username
                                AND (assignee_ = v_user
                                     OR assignee_ IN
                                           (SELECT GRP.USR_USERNAME
                                              FROM TQC_USERS GRP,
                                                   TQC_GROUP_USERS,
                                                   TQC_USERS USR
                                             WHERE GRP.USR_CODE =
                                                      GUSR_GRP_USR_CODE
                                                   AND USR.USR_CODE =
                                                          GUSR_USR_CODE
                                                   AND USR.USR_USERNAME =
                                                          v_user))
                                AND dbid_ = tqc_bpm_tickets.tckt_code
                                AND tckt_clm_no = NVL (v_claim_no, tckt_clm_no)
                                AND tckt_active != 'N'
                                AND tckt_sys_code =
                                       NVL (v_syscode, tckt_sys_code)
                                AND tckt_type = 'S'
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                WHERE ref_no IS NOT NULL
               UNION ALL
               SELECT tqc_bpm_tickets.tckt_code,
                      DECODE (tqc_bpm_tickets.tckt_sys_code,
                              37, 'GENERAL INSURANCE SYSTEM',
                              27, 'LIFE MANAGEMENT SYSTEM',
                              26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                              1, 'FINANCIAL MANAGEMENT SYSTEM',
                              0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                         usrsystem,
                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                              'Q', 'Quotation',
                              'P', 'Underwriting',
                              'C', 'Claims')
                         sysmodule,
                      tqc_bpm_tickets.tckt_clnt_code,
                      (clnt_name || ' ' || clnt_other_names) client,
                      tqc_bpm_tickets.tckt_agn_code,
                      agn_name AGENT,
                      tqc_bpm_tickets.tckt_pol_code,
                      tqc_bpm_tickets.tckt_pol_no,
                      tqc_bpm_tickets.tckt_clm_no,
                      tqc_bpm_tickets.tckt_quot_code,
                      tqc_bpm_tickets.tckt_quo_no,
                      NVL (tqc_bpm_tickets.tckt_assigned_by,
                           tqc_bpm_tickets.tckt_by)
                         tckt_by,
                      tqc_bpm_tickets.tckt_date,
                      NVL (tqc_bpm_tickets.tckt_process_id, EXECUTION_ID_)
                         tckt_process_id,
                      tqc_bpm_tickets.tckt_sys_module sys_module_code,
                      tckt_endr_code,
                      tckt_prod_type,
                      tckt_to,
                      tckt_remarks,
                      name_ tckt_name,
                      duedate_ tckt_due_date,
                      tckt_endorsement,
                      tckt_transno,
                      DECODE (tckt_sys_module,
                              'Q', tckt_quo_no,
                              'C', tckt_clm_no,
                              tckt_pol_no)
                         ref_no,
                      tckt_prp_code,
                      NULL,
                      tckt_type,
                      NULL POLICY_STATUS,
                      TCKT_TRAN_EFF_DATE,
                      TCKT_GGT_NO,
                      NULL USR_TYPE,
                      NVL (tqc_bpm_tickets.tckt_assignment_date,
                           tqc_bpm_tickets.tckt_date)
                         assignment_date
                 FROM tqc_bpm_tickets,
                      tqc_clients,
                      tqc_agencies,
                      jbpm4_task
                WHERE     tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                      --AND PRP_CLNT_CODE = CLNT_CODE(+)
                      AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                      AND dbid_ = tqc_bpm_tickets.tckt_code
                      AND tckt_clm_no = NVL (v_claim_no, tckt_clm_no)
                      AND tckt_active != 'N'
                      AND tckt_sys_code = NVL (v_syscode, tckt_sys_code)
                      AND tckt_type = 'E';

            RETURN (v_refcur);
         END;
      ELSE
         BEGIN
            IF NVL (v_syscode, 0) = 27
            THEN
               BEGIN
                  --  RAISE_ERROR('v_user=>'||v_user);
                  OPEN v_refcur FOR
                     SELECT a.*
                       FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                      DECODE (
                                         tqc_bpm_tickets.tckt_sys_code,
                                         37, 'GENERAL INSURANCE SYSTEM',
                                         27, 'LIFE MANAGEMENT SYSTEM',
                                         26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                         1, 'FINANCIAL MANAGEMENT SYSTEM',
                                         0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                         usrsystem,
                                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                                              'Q', 'Quotation',
                                              'P', 'Underwriting',
                                              'C', 'Claims')
                                         sysmodule,
                                      tqc_bpm_tickets.tckt_clnt_code,
                                      (clnt_name || ' ' || clnt_other_names)
                                         client,
                                      tqc_bpm_tickets.tckt_agn_code,
                                      agn_name AGENT,
                                      tqc_bpm_tickets.tckt_pol_code,
                                      pol_policy_no tckt_pol_no,
                                      tqc_bpm_tickets.tckt_clm_no,
                                      tqc_bpm_tickets.tckt_quot_code,
                                      tqc_bpm_tickets.tckt_quo_no,
                                      NVL (tqc_bpm_tickets.tckt_assigned_by,
                                           tqc_bpm_tickets.tckt_by)
                                         tckt_by,
                                      tqc_bpm_tickets.tckt_date,
                                      NVL (tqc_bpm_tickets.tckt_process_id,
                                           EXECUTION_ID_)
                                         tckt_process_id,
                                      tqc_bpm_tickets.tckt_sys_module
                                         sys_module_code,
                                      tckt_endr_code,
                                      tckt_prod_type,
                                      tckt_to,
                                      tckt_remarks,
                                      name_ tckt_name,
                                      duedate_ tckt_due_date,
                                      tckt_endorsement,
                                      tckt_transno,
                                      DECODE (
                                         tckt_sys_module,
                                         'Q', tckt_quo_no,
                                         'C', tckt_clm_no,
                                         'MKTPROP', ppr_proposal_no,
                                         'MKT-TO-UNDPROP', ppr_proposal_no,
                                         pol_proposal_no)
                                         ref_no,
                                      tckt_prp_code,
                                      NULL,
                                      tckt_type,
                                      NULL POLICY_STATUS,
                                      TCKT_TRAN_EFF_DATE,
                                      TCKT_GGT_NO,
                                      NULL USR_TYPE,
                                      NVL (
                                         tqc_bpm_tickets.tckt_assignment_date,
                                         tqc_bpm_tickets.tckt_date)
                                         assignment_date
                                 FROM tqc_bpm_tickets,
                                      tqc_clients,
                                      tqc_agencies,
                                      jbpm4_task,
                                      lms_proposers,
                                      lms_policies,
                                      lms_pol_proposals
                                WHERE tqc_bpm_tickets.tckt_clnt_code =
                                         prp_code(+)
                                      AND prp_clnt_code = clnt_code(+)
                                      AND tckt_pol_code = ppr_code(+)
                                      AND tckt_pol_code = pol_code(+)
                                      AND agn_code(+) =
                                             tqc_bpm_tickets.tckt_agn_code
                                      AND dbid_ = tqc_bpm_tickets.tckt_code
                                      AND (assignee_ = v_user
                                           OR TCKT_TO = v_user)
                                      --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                      AND tckt_active != 'N'
                                      AND tckt_sys_code =
                                             NVL (v_syscode, tckt_sys_code)
                                      AND NVL (tckt_cla_type, 'X') =
                                             NVL (v_cla_type, 'X')
                             ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                      WHERE a.ref_no IS NOT NULL;

                  RETURN (v_refcur);
               END;
            ELSIF NVL (v_syscode, 0) = 26
            THEN
               BEGIN
                  --RAISE_ERROR('v_tkt_type=>'||v_tkt_type);
                  OPEN v_refcur FOR
                       SELECT a.*
                         FROM (SELECT tqc_bpm_tickets.tckt_code,
                                      DECODE (
                                         tqc_bpm_tickets.tckt_sys_code,
                                         37, 'GENERAL INSURANCE SYSTEM',
                                         27, 'LIFE MANAGEMENT SYSTEM',
                                         26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                         1, 'FINANCIAL MANAGEMENT SYSTEM',
                                         0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                         usrsystem,
                                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                                              'Q', 'Quotation',
                                              'P', 'Underwriting',
                                              'C', 'Claims')
                                         sysmodule,
                                      tqc_bpm_tickets.tckt_clnt_code,
                                      (clnt_name || ' ' || clnt_other_names)
                                         client,
                                      tqc_bpm_tickets.tckt_agn_code,
                                      agn_name AGENT,
                                      tqc_bpm_tickets.tckt_pol_code,
                                      pol_policy_no tckt_pol_no,
                                      tqc_bpm_tickets.tckt_clm_no,
                                      tqc_bpm_tickets.tckt_quot_code,
                                      tqc_bpm_tickets.tckt_quo_no,
                                      NVL (tqc_bpm_tickets.tckt_assigned_by,
                                           tqc_bpm_tickets.tckt_by)
                                         tckt_by,
                                      tqc_bpm_tickets.tckt_date,
                                      NVL (tqc_bpm_tickets.tckt_process_id,
                                           EXECUTION_ID_)
                                         tckt_process_id,
                                      tqc_bpm_tickets.tckt_sys_module
                                         sys_module_code,
                                      tckt_endr_code,
                                      tckt_prod_type,
                                      tckt_to,
                                      tckt_remarks,
                                      name_ tckt_name,
                                      TRUNC (duedate_) tckt_due_date,
                                      tckt_endorsement,
                                      tckt_transno,
                                      DECODE (
                                         tckt_sys_module,
                                         'Q', tckt_quo_no,
                                         'C', tckt_clm_no,
                                         'MKTPROP', ppr_proposal_no,
                                         'MKT-TO-UNDPROP', ppr_proposal_no,
                                         pol_proposal_no)
                                         ref_no,
                                      tckt_prp_code,
                                      NULL,
                                      tckt_type,
                                      NULL POLICY_STATUS,
                                      TCKT_TRAN_EFF_DATE,
                                      TCKT_GGT_NO,
                                      NULL USR_TYPE,
                                      NVL (
                                         tqc_bpm_tickets.tckt_assignment_date,
                                         tqc_bpm_tickets.tckt_date)
                                         assignment_date
                                 FROM tqc_bpm_tickets,
                                      tqc_clients,
                                      tqc_agencies,
                                      jbpm4_task,
                                      lms_proposers,
                                      lms_policies,
                                      lms_pol_proposals,
                                      lms_policy_endorsements
                                WHERE tqc_bpm_tickets.tckt_clnt_code =
                                         prp_code(+)
                                      AND tckt_endr_code(+) = endr_code
                                      AND prp_clnt_code = clnt_code(+)
                                      AND tckt_pol_code = ppr_code(+)
                                      AND tckt_pol_code = pol_code(+)
                                      AND agn_code(+) =
                                             tqc_bpm_tickets.tckt_agn_code
                                      AND dbid_ = tqc_bpm_tickets.tckt_code
                                      AND (assignee_ = v_user
                                           OR TCKT_TO = v_user)
                                      --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                      AND tckt_active != 'N'
                                      AND tckt_sys_code =
                                             NVL (v_syscode, tckt_sys_code)
                                      AND NVL (tckt_cla_type, 'X') =
                                             NVL (v_cla_type, 'X')
                                      AND (NVL (v_tkt_type, 'L') = 'L')
                               --ORDER BY tqc_bpm_tickets.tckt_code DESC

                               UNION ALL
                               SELECT dd_code tckt_code,
                                      'LIFE MANAGEMENT SYSTEM-LIFE' usrsystem,
                                      'Document Management System' sysmodule,
                                      dd_code tckt_clnt_code,
                                      NULL client,
                                      dd_code tckt_agn_code,
                                      NULL AGENT,
                                      dd_code tckt_pol_code,
                                      dd_doc_id tckt_pol_no,
                                      NULL tckt_clm_no,
                                      NULL tckt_quot_code,
                                      NULL tckt_quo_no,
                                      dd_by tckt_by,
                                      dd_date tckt_date,
                                      NULL tckt_process_id,
                                      'DMS' sys_module_code,
                                      NULL tckt_endr_code,
                                      NULL tckt_prod_type,
                                      dd_by tckt_to,
                                      'Process Dms Document' tckt_remarks,
                                      'Process Dms Document' tckt_name,
                                      dd_date tckt_due_date,
                                      NULL tckt_endorsement,
                                      NULL tckt_transno,
                                      dd_doc_name ref_no,
                                      NULL tckt_prp_code,
                                      NULL,
                                      NULL tckt_type,
                                      NULL policy_status,
                                      NULL tckt_tran_eff_date,
                                      NULL tckt_ggt_no,
                                      NULL usr_type,
                                      NULL tckt_assignment_date
                                 FROM LMS_DMS_DOCUMENTS
                                WHERE     dd_by = v_user
                                      AND dd_system_id = 'LMS_ORD'
                                      AND NVL (dd_processed, 'N') = 'N'
                                      AND (NVL (v_tkt_type, 'D') = 'D')) a
                        WHERE a.ref_no IS NOT NULL
                     ORDER BY tckt_date DESC;

                  -- ORDER BY tckt_code DESC;

                  RETURN (v_refcur);
               END;
            ELSE
               BEGIN
                  OPEN v_refcur FOR
                       SELECT *
                         FROM (SELECT a.*
                                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                                DECODE (
                                                   tqc_bpm_tickets.tckt_sys_code,
                                                   37, 'GENERAL INSURANCE SYSTEM',
                                                   27, 'LIFE MANAGEMENT SYSTEM',
                                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                                   usrsystem,
                                                DECODE (
                                                   tqc_bpm_tickets.tckt_sys_module,
                                                   'Q', 'Quotation',
                                                   'P', 'Underwriting',
                                                   'C', 'Claims')
                                                   sysmodule,
                                                tqc_bpm_tickets.tckt_clnt_code,
                                                (   clnt_name
                                                 || ' '
                                                 || clnt_other_names)
                                                   client,
                                                tqc_bpm_tickets.tckt_agn_code,
                                                agn_name AGENT,
                                                tqc_bpm_tickets.tckt_pol_code,
                                                tqc_bpm_tickets.tckt_pol_no,
                                                tqc_bpm_tickets.tckt_clm_no,
                                                tqc_bpm_tickets.tckt_quot_code,
                                                tqc_bpm_tickets.tckt_quo_no,
                                                NVL (
                                                   tqc_bpm_tickets.tckt_assigned_by,
                                                   tqc_bpm_tickets.tckt_by)
                                                   tckt_by,
                                                tqc_bpm_tickets.tckt_date,
                                                NVL (
                                                   tqc_bpm_tickets.tckt_process_id,
                                                   EXECUTION_ID_)
                                                   tckt_process_id,
                                                tqc_bpm_tickets.tckt_sys_module
                                                   sys_module_code,
                                                tckt_endr_code,
                                                tckt_prod_type,
                                                tckt_to,
                                                tckt_remarks,
                                                name_ tckt_name,
                                                duedate_ tckt_due_date,
                                                tckt_endorsement,
                                                tckt_transno,
                                                DECODE (
                                                   tckt_sys_module,
                                                   'Q', NVL (tckt_quo_no,
                                                             tckt_quot_code),
                                                   'C', tckt_clm_no,
                                                   tckt_pol_no)
                                                   ref_no,
                                                tckt_prp_code,
                                                NULL,
                                                tckt_type,
                                                NULL POLICY_STATUS,
                                                TCKT_TRAN_EFF_DATE,
                                                TCKT_GGT_NO,
                                                USR_TYPE,
                                                NVL (
                                                   tqc_bpm_tickets.tckt_assignment_date,
                                                   tqc_bpm_tickets.tckt_date)
                                                   assignment_date
                                           FROM tqc_bpm_tickets,
                                                tqc_clients,
                                                tqc_agencies,
                                                jbpm4_task,
                                                tqc_users
                                          WHERE tqc_bpm_tickets.tckt_clnt_code =
                                                   clnt_code(+)
                                                --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                                AND agn_code(+) =
                                                       tqc_bpm_tickets.tckt_agn_code
                                                AND ASSIGNEE_ = USR_USERNAME
                                                AND dbid_ =
                                                       tqc_bpm_tickets.tckt_code
                                                AND (assignee_ = v_user
                                                     OR assignee_ IN
                                                           (SELECT GRP.USR_USERNAME
                                                              FROM TQC_USERS GRP,
                                                                   TQC_GROUP_USERS,
                                                                   TQC_USERS USR
                                                             WHERE GRP.USR_CODE =
                                                                      GUSR_GRP_USR_CODE
                                                                   AND USR.USR_CODE =
                                                                          GUSR_USR_CODE
                                                                   AND USR.USR_USERNAME =
                                                                          v_user))
                                                --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                                AND tckt_active != 'N'
                                                AND tckt_type = 'S'
                                                AND tckt_sys_code =
                                                       NVL (v_syscode,
                                                            tckt_sys_code)
                                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                                WHERE a.ref_no IS NOT NULL
                               UNION ALL
                               SELECT tqc_bpm_tickets.tckt_code,
                                      DECODE (
                                         tqc_bpm_tickets.tckt_sys_code,
                                         37, 'GENERAL INSURANCE SYSTEM',
                                         27, 'LIFE MANAGEMENT SYSTEM',
                                         26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                         1, 'FINANCIAL MANAGEMENT SYSTEM',
                                         0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                         usrsystem,
                                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                                              'Q', 'Quotation',
                                              'P', 'Underwriting',
                                              'C', 'Claims')
                                         sysmodule,
                                      tqc_bpm_tickets.tckt_clnt_code,
                                      (clnt_name || ' ' || clnt_other_names)
                                         client,
                                      tqc_bpm_tickets.tckt_agn_code,
                                      agn_name AGENT,
                                      tqc_bpm_tickets.tckt_pol_code,
                                      tqc_bpm_tickets.tckt_pol_no,
                                      tqc_bpm_tickets.tckt_clm_no,
                                      tqc_bpm_tickets.tckt_quot_code,
                                      tqc_bpm_tickets.tckt_quo_no,
                                      NVL (tqc_bpm_tickets.tckt_assigned_by,
                                           tqc_bpm_tickets.tckt_by)
                                         tckt_by,
                                      tqc_bpm_tickets.tckt_date,
                                      NVL (tqc_bpm_tickets.tckt_process_id,
                                           EXECUTION_ID_)
                                         tckt_process_id,
                                      tqc_bpm_tickets.tckt_sys_module
                                         sys_module_code,
                                      tckt_endr_code,
                                      tckt_prod_type,
                                      tckt_to,
                                      tckt_remarks,
                                      name_ tckt_name,
                                      duedate_ tckt_due_date,
                                      tckt_endorsement,
                                      tckt_transno,
                                      tckt_extern_ref_no ref_no,
                                      tckt_prp_code,
                                      NULL,
                                      tckt_type,
                                      NULL POLICY_STATUS,
                                      TCKT_TRAN_EFF_DATE,
                                      TCKT_GGT_NO,
                                      NULL USR_TYPE,
                                      NVL (
                                         tqc_bpm_tickets.tckt_assignment_date,
                                         tqc_bpm_tickets.tckt_date)
                                         assignment_date
                                 FROM tqc_bpm_tickets,
                                      tqc_clients,
                                      tqc_agencies,
                                      jbpm4_task
                                WHERE tqc_bpm_tickets.tckt_clnt_code =
                                         clnt_code(+)
                                      AND tckt_type = 'E'
                                      --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                      AND agn_code(+) =
                                             tqc_bpm_tickets.tckt_agn_code
                                      AND dbid_ = tqc_bpm_tickets.tckt_code
                                      --AND TCKT_CLM_NO = NVL(:v_claim_no, TCKT_CLM_NO)
                                      AND tckt_active != 'N'
                                      AND assignee_ = v_user
                                      AND tckt_sys_code =
                                             NVL (v_syscode, tckt_sys_code))
                     ORDER BY tckt_code DESC;

                  RETURN (v_refcur);
               END;
            END IF;
         END;
      END IF;
   END;

   FUNCTION getchqrtcktdtslbyuser (v_user IN VARCHAR2)
      RETURN chrq_tckt_ref
   IS
      v_refcur   chrq_tckt_ref;
   BEGIN
      --  RAISE_ERROR('v_user ='||v_user);
      OPEN v_refcur FOR
           SELECT tqc_bpm_tickets.tckt_code,
                  DECODE (tqc_bpm_tickets.tckt_sys_module,
                          'N', 'NEW',
                          'A', 'AUTHORIZED',
                          'RJ', 'REJECTED',
                          'P', 'PRINTED')
                     sysmodule,
                  fms_cheques.cqr_no,
                  fms_cheques.cqr_payee,
                  fms_cheques.cqr_narrative,
                  tqc_bpm_tickets.tckt_by,
                  tqc_bpm_tickets.tckt_date,
                  tqc_bpm_tickets.tckt_process_id,
                  tqc_bpm_tickets.tckt_sys_module,
                  tqc_bpm_tickets.tckt_to,
                  tqc_bpm_tickets.tckt_remarks,
                  name_ tckt_name,
                  duedate_ tckt_due_date
             FROM tqc_bpm_tickets, fms_cheques, jbpm4_task
            WHERE     tqc_bpm_tickets.tckt_cqr_no = cqr_no
                  AND dbid_ = tqc_bpm_tickets.tckt_code
                  AND assignee_ = v_user
                  AND tckt_active != 'N'
         ORDER BY tqc_bpm_tickets.tckt_code DESC;

      RETURN (v_refcur);
   END;

   FUNCTION get_tckt_dtls_by_usr_life (v_user       IN VARCHAR2,
                                       v_quo_code      NUMBER DEFAULT NULL,
                                       v_pol_code      NUMBER DEFAULT NULL,
                                       v_claim_no      VARCHAR2 DEFAULT NULL,
                                       v_syscode       NUMBER DEFAULT NULL)
      RETURN tickets_ref
   IS
      v_refcur   tickets_ref;
   BEGIN
      --RAISE_ERROR('v_quo_code ' || v_quo_code ||' v_pol_code '|| v_pol_code || 'v_claim_no ' || v_claim_no || 'v_syscode ' || v_syscode);
      IF v_quo_code IS NOT NULL
      THEN
         BEGIN
            OPEN v_refcur FOR
               SELECT a.*
                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                DECODE (
                                   tqc_bpm_tickets.tckt_sys_code,
                                   37, 'GENERAL INSURANCE SYSTEM',
                                   27, 'LIFE MANAGEMENT SYSTEM',
                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                   usrsystem,
                                DECODE (tqc_bpm_tickets.tckt_sys_module,
                                        'Q', 'Quotation',
                                        'P', 'Underwriting',
                                        'E', 'Endorsements',
                                        'R', 'Renewals',
                                        'RT', 'Transfered Renewal',
                                        'C', 'Claims')
                                   sysmodule,
                                tqc_bpm_tickets.tckt_clnt_code,
                                (clnt_name || ' ' || clnt_other_names) client,
                                tqc_bpm_tickets.tckt_agn_code,
                                agn_name AGENT,
                                tqc_bpm_tickets.tckt_pol_code,
                                tqc_bpm_tickets.tckt_pol_no,
                                tqc_bpm_tickets.tckt_clm_no,
                                tqc_bpm_tickets.tckt_quot_code,
                                tqc_bpm_tickets.tckt_quo_no,
                                tqc_bpm_tickets.tckt_by,
                                tqc_bpm_tickets.tckt_date,
                                tqc_bpm_tickets.tckt_process_id,
                                tqc_bpm_tickets.tckt_sys_module sys_module_code,
                                tckt_endr_code,
                                tckt_prod_type,
                                tckt_to,
                                tckt_remarks,
                                name_ tckt_name,
                                duedate_ tckt_due_date,
                                tckt_endorsement,
                                tckt_transno,
                                DECODE (tckt_sys_module,
                                        'Q', tckt_quo_no,
                                        'C', tckt_clm_no,
                                        tckt_pol_no)
                                   ref_no,
                                tckt_prp_code,
                                NULL,
                                tckt_type,
                                NULL POLICY_STATUS,
                                TCKT_TRAN_EFF_DATE,
                                TCKT_GGT_NO,
                                NULL USR_TYPE,
                                NVL (tqc_bpm_tickets.tckt_assignment_date,
                                     tqc_bpm_tickets.tckt_date)
                                   assignment_date
                           FROM tqc_bpm_tickets,
                                tqc_clients,
                                tqc_agencies,
                                jbpm4_task
                          WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+) --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                                AND dbid_ = tqc_bpm_tickets.tckt_code
                                AND tckt_quot_code =
                                       NVL (v_quo_code, tckt_quot_code)
                                AND tckt_active != 'N'
                                AND tckt_sys_code =
                                       NVL (v_syscode, tckt_sys_code)
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                WHERE a.ref_no IS NOT NULL;

            RETURN (v_refcur);
         END;
      ELSIF v_pol_code IS NOT NULL AND v_claim_no IS NULL
      THEN
         BEGIN
            OPEN v_refcur FOR
               SELECT a.*
                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                DECODE (
                                   tqc_bpm_tickets.tckt_sys_code,
                                   37, 'GENERAL INSURANCE SYSTEM',
                                   27, 'LIFE MANAGEMENT SYSTEM',
                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                   usrsystem,
                                DECODE (tqc_bpm_tickets.tckt_sys_module,
                                        'Q', 'Quotation',
                                        'P', 'Underwriting',
                                        'C', 'Claims')
                                   sysmodule,
                                tqc_bpm_tickets.tckt_clnt_code,
                                (clnt_name || ' ' || clnt_other_names) client,
                                tqc_bpm_tickets.tckt_agn_code,
                                agn_name AGENT,
                                tqc_bpm_tickets.tckt_pol_code,
                                tqc_bpm_tickets.tckt_pol_no,
                                tqc_bpm_tickets.tckt_clm_no,
                                tqc_bpm_tickets.tckt_quot_code,
                                tqc_bpm_tickets.tckt_quo_no,
                                tqc_bpm_tickets.tckt_by,
                                tqc_bpm_tickets.tckt_date,
                                tqc_bpm_tickets.tckt_process_id,
                                tqc_bpm_tickets.tckt_sys_module sys_module_code,
                                tckt_endr_code,
                                tckt_prod_type,
                                tckt_to,
                                tckt_remarks,
                                name_ tckt_name,
                                duedate_ tckt_due_date,
                                tckt_endorsement,
                                tckt_transno,
                                DECODE (tckt_sys_module,
                                        'Q', tckt_quo_no,
                                        'C', tckt_clm_no,
                                        tckt_pol_no)
                                   ref_no,
                                tckt_prp_code,
                                NULL pol_client_policy_number,
                                tckt_type,
                                NULL POLICY_STATUS,
                                TCKT_TRAN_EFF_DATE,
                                TCKT_GGT_NO,
                                NULL USR_TYPE,
                                NVL (tqc_bpm_tickets.tckt_assignment_date,
                                     tqc_bpm_tickets.tckt_date)
                                   assignment_date
                           FROM tqc_bpm_tickets,
                                tqc_clients,
                                tqc_agencies,
                                jbpm4_task,
                                lms_policies
                          WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                                AND tckt_pol_code = pol_code
                                --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                                AND dbid_ = tqc_bpm_tickets.tckt_code
                                AND tckt_pol_code =
                                       NVL (v_pol_code, tckt_pol_code)
                                AND tckt_active != 'N'
                                AND tckt_sys_code =
                                       NVL (v_syscode, tckt_sys_code)
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                WHERE ref_no IS NOT NULL;

            RETURN (v_refcur);
         END;
      ELSIF v_pol_code IS NOT NULL AND v_claim_no IS NOT NULL
      THEN
         BEGIN
            --RAISE_ERROR(v_claim_no);
            OPEN v_refcur FOR
               SELECT a.*
                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                DECODE (
                                   tqc_bpm_tickets.tckt_sys_code,
                                   37, 'GENERAL INSURANCE SYSTEM',
                                   27, 'LIFE MANAGEMENT SYSTEM',
                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                   usrsystem,
                                DECODE (tqc_bpm_tickets.tckt_sys_module,
                                        'Q', 'Quotation',
                                        'P', 'Underwriting',
                                        'C', 'Claims')
                                   sysmodule,
                                tqc_bpm_tickets.tckt_clnt_code,
                                (clnt_name || ' ' || clnt_other_names) client,
                                tqc_bpm_tickets.tckt_agn_code,
                                agn_name AGENT,
                                tqc_bpm_tickets.tckt_pol_code,
                                tqc_bpm_tickets.tckt_pol_no,
                                tqc_bpm_tickets.tckt_clm_no,
                                tqc_bpm_tickets.tckt_quot_code,
                                tqc_bpm_tickets.tckt_quo_no,
                                tqc_bpm_tickets.tckt_by,
                                tqc_bpm_tickets.tckt_date,
                                tqc_bpm_tickets.tckt_process_id,
                                tqc_bpm_tickets.tckt_sys_module sys_module_code,
                                tckt_endr_code,
                                tckt_prod_type,
                                tckt_to,
                                tckt_remarks,
                                name_ tckt_name,
                                duedate_ tckt_due_date,
                                tckt_endorsement,
                                tckt_transno,
                                DECODE (tckt_sys_module,
                                        'Q', tckt_quo_no,
                                        'C', tckt_clm_no,
                                        tckt_pol_no)
                                   ref_no,
                                tckt_prp_code,
                                NULL,
                                tckt_type,
                                NULL POLICY_STATUS,
                                TCKT_TRAN_EFF_DATE,
                                TCKT_GGT_NO,
                                NULL USR_TYPE,
                                NVL (tqc_bpm_tickets.tckt_assignment_date,
                                     tqc_bpm_tickets.tckt_date)
                                   assignment_date
                           FROM tqc_bpm_tickets,
                                tqc_clients,
                                tqc_agencies,
                                jbpm4_task
                          WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+) --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                                AND dbid_ = tqc_bpm_tickets.tckt_code
                                AND tckt_clm_no = NVL (v_claim_no, tckt_clm_no)
                                AND tckt_active != 'N'
                                AND tckt_sys_code =
                                       NVL (v_syscode, tckt_sys_code)
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                WHERE ref_no IS NOT NULL;

            RETURN (v_refcur);
         END;
      ELSE
         BEGIN
            IF NVL (v_syscode, 0) = 27
            THEN
               BEGIN
                  OPEN v_refcur FOR
                     SELECT a.*
                       FROM (SELECT tqc_bpm_tickets.tckt_code,
                                    DECODE (
                                       tqc_bpm_tickets.tckt_sys_code,
                                       37, 'GENERAL INSURANCE SYSTEM',
                                       27, 'LIFE MANAGEMENT SYSTEM',
                                       26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                       1, 'FINANCIAL MANAGEMENT SYSTEM',
                                       0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                       usrsystem,
                                    DECODE (tqc_bpm_tickets.tckt_sys_module,
                                            'Q', 'Quotation',
                                            'P', 'Underwriting',
                                            'C', 'Claims',
                                            'E', 'Endorsement')
                                       sysmodule,
                                    tqc_bpm_tickets.tckt_clnt_code,
                                    (clnt_name || ' ' || clnt_other_names)
                                       client,
                                    tqc_bpm_tickets.tckt_agn_code,
                                    agn_name AGENT,
                                    tqc_bpm_tickets.tckt_pol_code,
                                    pol_policy_no tckt_pol_no,
                                    tqc_bpm_tickets.tckt_clm_no,
                                    tqc_bpm_tickets.tckt_quot_code,
                                    tqc_bpm_tickets.tckt_quo_no,
                                    tqc_bpm_tickets.tckt_by,
                                    tqc_bpm_tickets.tckt_date,
                                    tqc_bpm_tickets.tckt_process_id,
                                    tqc_bpm_tickets.tckt_sys_module
                                       sys_module_code,
                                    tckt_endr_code,
                                    tckt_prod_type,
                                    tckt_to,
                                    tckt_remarks,
                                    name_ tckt_name,
                                    duedate_ tckt_due_date,
                                    tckt_endorsement,
                                    tckt_transno,
                                    DECODE (
                                       tckt_sys_module,
                                       'Q', tckt_quo_no,
                                       'C', tckt_clm_no,
                                       'MKTPROP', ppr_proposal_no,
                                       'MKT-TO-UNDPROP', ppr_proposal_no,
                                       'P', tckt_pol_no,
                                       'E', tckt_pol_no,
                                       pol_proposal_no)
                                       ref_no,
                                    tckt_prp_code,
                                    NULL,
                                    tckt_type,
                                    NULL POLICY_STATUS,
                                    TCKT_TRAN_EFF_DATE,
                                    TCKT_GGT_NO,
                                    NULL USR_TYPE,
                                    NVL (
                                       tqc_bpm_tickets.tckt_assignment_date,
                                       tqc_bpm_tickets.tckt_date)
                                       assignment_date
                               FROM tqc_bpm_tickets,
                                    tqc_clients,
                                    tqc_agencies,
                                    jbpm4_task,
                                    lms_proposers,
                                    lms_policies,
                                    lms_pol_proposals
                              WHERE tqc_bpm_tickets.tckt_clnt_code =
                                       prp_clnt_code(+)
                                    AND prp_clnt_code = clnt_code(+)
                                    AND tckt_pol_code = ppr_code(+)
                                    AND tckt_pol_code = pol_code(+)
                                    AND agn_code(+) =
                                           tqc_bpm_tickets.tckt_agn_code
                                    AND dbid_ = tqc_bpm_tickets.tckt_code
                                    AND assignee_ = v_user
                                    AND tckt_to IS NOT NULL
                                    --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                    AND tckt_active != 'N'
                                    AND tckt_sys_code =
                                           NVL (v_syscode, tckt_sys_code)
                             UNION
                             SELECT dd_code tckt_code,
                                    'LIFE MANAGEMENT SYSTEM-LIFE' usrsystem,
                                    'Document Management System' sysmodule,
                                    dd_code tckt_clnt_code,
                                    NULL client,
                                    dd_code tckt_agn_code,
                                    NULL AGENT,
                                    dd_code tckt_pol_code,
                                    dd_doc_id tckt_pol_no,
                                    NULL tckt_clm_no,
                                    NULL tckt_quot_code,
                                    NULL tckt_quo_no,
                                    dd_by tckt_by,
                                    dd_date tckt_date,
                                    NULL tckt_process_id,
                                    'DMS' sys_module_code,
                                    NULL tckt_endr_code,
                                    NULL tckt_prod_type,
                                    dd_by tckt_to,
                                    'Process Dms Document' tckt_remarks,
                                    'DMS' tckt_name,
                                    dd_date tckt_due_date,
                                    NULL tckt_endorsement,
                                    NULL tckt_transno,
                                    dd_doc_name ref_no,
                                    NULL tckt_prp_code,
                                    NULL,
                                    NULL tckt_type,
                                    NULL policy_status,
                                    NULL tckt_tran_eff_date,
                                    NULL tckt_ggt_no,
                                    NULL usr_type,
                                    NULL tckt_assignment_date
                               FROM LMS_DMS_DOCUMENTS
                              WHERE     dd_by = v_user
                                    AND dd_system_id = 'LMS_GRP'
                                    AND NVL (dd_processed, 'N') = 'N'
                             ORDER BY tckt_code DESC) a
                      WHERE a.ref_no IS NOT NULL;

                  RETURN (v_refcur);
               END;
            ELSIF NVL (v_syscode, 0) = 26
            THEN
               BEGIN
                  OPEN v_refcur FOR
                     SELECT a.*
                       FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                      DECODE (
                                         tqc_bpm_tickets.tckt_sys_code,
                                         37, 'GENERAL INSURANCE SYSTEM',
                                         27, 'LIFE MANAGEMENT SYSTEM',
                                         26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                         1, 'FINANCIAL MANAGEMENT SYSTEM',
                                         0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                         usrsystem,
                                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                                              'Q', 'Quotation',
                                              'P', 'Underwriting',
                                              'C', 'Claims')
                                         sysmodule,
                                      tqc_bpm_tickets.tckt_clnt_code,
                                      (clnt_name || ' ' || clnt_other_names)
                                         client,
                                      tqc_bpm_tickets.tckt_agn_code,
                                      agn_name AGENT,
                                      tqc_bpm_tickets.tckt_pol_code,
                                      pol_policy_no tckt_pol_no,
                                      tqc_bpm_tickets.tckt_clm_no,
                                      tqc_bpm_tickets.tckt_quot_code,
                                      tqc_bpm_tickets.tckt_quo_no,
                                      tqc_bpm_tickets.tckt_by,
                                      tqc_bpm_tickets.tckt_date,
                                      tqc_bpm_tickets.tckt_process_id,
                                      tqc_bpm_tickets.tckt_sys_module
                                         sys_module_code,
                                      tckt_endr_code,
                                      tckt_prod_type,
                                      tckt_to,
                                      tckt_remarks,
                                      name_ tckt_name,
                                      duedate_ tckt_due_date,
                                      tckt_endorsement,
                                      tckt_transno,
                                      DECODE (
                                         tckt_sys_module,
                                         'Q', tckt_quo_no,
                                         'C', tckt_clm_no,
                                         'MKTPROP', ppr_proposal_no,
                                         'MKT-TO-UNDPROP', ppr_proposal_no,
                                         pol_proposal_no)
                                         ref_no,
                                      tckt_prp_code,
                                      NULL,
                                      tckt_type,
                                      NULL POLICY_STATUS,
                                      TCKT_TRAN_EFF_DATE,
                                      TCKT_GGT_NO,
                                      NULL USR_TYPE,
                                      NVL (
                                         tqc_bpm_tickets.tckt_assignment_date,
                                         tqc_bpm_tickets.tckt_date)
                                         assignment_date
                                 FROM tqc_bpm_tickets,
                                      tqc_clients,
                                      tqc_agencies,
                                      jbpm4_task,
                                      lms_proposers,
                                      lms_policies,
                                      lms_pol_proposals
                                WHERE tqc_bpm_tickets.tckt_clnt_code =
                                         prp_code(+)
                                      AND prp_clnt_code = clnt_code(+)
                                      AND tckt_pol_code = ppr_code(+)
                                      AND tckt_pol_code = pol_code(+)
                                      AND agn_code(+) =
                                             tqc_bpm_tickets.tckt_agn_code
                                      AND dbid_ = tqc_bpm_tickets.tckt_code
                                      AND assignee_ = v_user
                                      --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                      AND tckt_active != 'N'
                                      AND tckt_sys_code =
                                             NVL (v_syscode, tckt_sys_code)
                             ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                      WHERE a.ref_no IS NOT NULL;

                  RETURN (v_refcur);
               END;
            ELSE
               BEGIN
                  OPEN v_refcur FOR
                     SELECT a.*
                       FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                      DECODE (
                                         tqc_bpm_tickets.tckt_sys_code,
                                         37, 'GENERAL INSURANCE SYSTEM',
                                         27, 'LIFE MANAGEMENT SYSTEM',
                                         26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                         1, 'FINANCIAL MANAGEMENT SYSTEM',
                                         0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                         usrsystem,
                                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                                              'Q', 'Quotation',
                                              'P', 'Underwriting',
                                              'C', 'Claims')
                                         sysmodule,
                                      tqc_bpm_tickets.tckt_clnt_code,
                                      (clnt_name || ' ' || clnt_other_names)
                                         client,
                                      tqc_bpm_tickets.tckt_agn_code,
                                      agn_name AGENT,
                                      tqc_bpm_tickets.tckt_pol_code,
                                      tqc_bpm_tickets.tckt_pol_no,
                                      tqc_bpm_tickets.tckt_clm_no,
                                      tqc_bpm_tickets.tckt_quot_code,
                                      tqc_bpm_tickets.tckt_quo_no,
                                      tqc_bpm_tickets.tckt_by,
                                      tqc_bpm_tickets.tckt_date,
                                      tqc_bpm_tickets.tckt_process_id,
                                      tqc_bpm_tickets.tckt_sys_module
                                         sys_module_code,
                                      tckt_endr_code,
                                      tckt_prod_type,
                                      tckt_to,
                                      tckt_remarks,
                                      name_ tckt_name,
                                      duedate_ tckt_due_date,
                                      tckt_endorsement,
                                      tckt_transno,
                                      DECODE (tckt_sys_module,
                                              'Q', tckt_quo_no,
                                              'C', tckt_clm_no,
                                              tckt_pol_no)
                                         ref_no,
                                      tckt_prp_code,
                                      NULL,
                                      tckt_type,
                                      NULL POLICY_STATUS,
                                      TCKT_TRAN_EFF_DATE,
                                      TCKT_GGT_NO,
                                      NULL USR_TYPE,
                                      NVL (
                                         tqc_bpm_tickets.tckt_assignment_date,
                                         tqc_bpm_tickets.tckt_date)
                                         assignment_date
                                 FROM tqc_bpm_tickets,
                                      tqc_clients,
                                      tqc_agencies,
                                      jbpm4_task
                                WHERE tqc_bpm_tickets.tckt_clnt_code =
                                         clnt_code(+)
                                      --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                      AND agn_code(+) =
                                             tqc_bpm_tickets.tckt_agn_code
                                      AND dbid_ = tqc_bpm_tickets.tckt_code
                                      AND assignee_ = v_user
                                      --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                      AND tckt_active != 'N'
                                      AND tckt_sys_code =
                                             NVL (v_syscode, tckt_sys_code)
                             ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                      WHERE a.ref_no IS NOT NULL;

                  RETURN (v_refcur);
               END;
            END IF;
         END;
      END IF;
   END;

   PROCEDURE get_bpm_task_srn (v_taskname    IN     VARCHAR,
                               v_scrn_name      OUT VARCHAR2,
                               v_sys_code           NUMBER)
   IS
   BEGIN
      BEGIN
         --RAISE_ERROR(v_sys_code||';'||v_taskname);
         SELECT scrn_name
           INTO v_scrn_name
           FROM tqc_bpm_screens
          WHERE scrn_task_desc = v_taskname
                AND scrn_sys_code = NVL (v_sys_code, 27);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (
               'error determining the screen for the task...' || v_taskname);
      END;
   END;

   PROCEDURE update_cal_activity (v_loc          VARCHAR2,
                                  v_title        VARCHAR2,
                                  v_today        VARCHAR2,
                                  v_startdate    VARCHAR2,
                                  v_enddate      VARCHAR2,
                                  v_user         VARCHAR2)
   IS
      v_st_days       NUMBER;
      v_st_hrs        NUMBER;
      v_end_days      NUMBER;
      v_end_hrs       NUMBER;
      v_create_date   DATE;
   BEGIN
      --RAISE_ERROR(v_today);

      /*SELECT
          trunc((((86400*(TO_DATE(v_today, 'DD.MM.YYYY HH12:MI:SS')-TO_DATE(v_startdate, 'DD.MM.YYYY HH12:MI:SS')))/60)/60)/24) startDays,
          TO_CHAR(TO_DATE(v_startdate,'DD.MM.YYYY HH12:MI:SS'), 'HH12') starthrs,
          trunc((((86400*(TO_DATE(v_today, 'DD.MM.YYYY HH12:MI:SS')-TO_DATE(v_enddate, 'DD.MM.YYYY HH12:MI:SS')))/60)/60)/24) endDays,
          TO_CHAR(TO_DATE(v_enddate,'DD.MM.YYYY HH12:MI:SS'), 'HH12') endhrs,
          TO_CHAR(TO_DATE(v_today, 'DD.MM.YYYY HH12:MI:SS'), 'DD/MM/RRRR') createdays
          INTO v_st_days, v_st_hrs, v_end_days, v_end_hrs, v_create_date
          FROM dual;
      */
      BEGIN
         --RAISE_ERROR('Start Days '||v_startdate||'end Days ' ||v_enddate||'today '||v_today);
         SELECT CASE
                   WHEN v_today < v_startdate
                   THEN
                      TRUNC (
                         ( ( (86400
                              * (TO_DATE (v_startdate,
                                          'DD/MM/YYYY HH:MI:SS AM')
                                 - TO_DATE (v_today,
                                            'DD/MM/YYYY HH:MI:SS AM')))
                            / 60)
                          / 60)
                         / 24)
                   ELSE
                      TRUNC (
                         ( ( (86400
                              * (TO_DATE (v_today, 'DD/MM/YYYY HH:MI:SS AM')
                                 - TO_DATE (v_startdate,
                                            'DD/MM/YYYY HH:MI:SS AM')))
                            / 60)
                          / 60)
                         / 24)
                END
                   AS startdays,
                TO_CHAR (TO_DATE (v_startdate, 'DD/MM/YYYY HH:MI:SS AM'),
                         'HH24')
                   starthrs,
                CASE
                   WHEN v_today < v_enddate
                   THEN
                      TRUNC (
                         ( ( (86400
                              * (TO_DATE (v_enddate,
                                          'DD/MM/YYYY HH:MI:SS AM')
                                 - TO_DATE (v_today,
                                            'DD/MM/YYYY HH:MI:SS AM')))
                            / 60)
                          / 60)
                         / 24)
                   ELSE
                      TRUNC (
                         ( ( (86400
                              * (TO_DATE (v_today, 'DD/MM/YYYY HH:MI:SS AM')
                                 - TO_DATE (v_enddate,
                                            'DD/MM/YYYY HH:MI:SS AM')))
                            / 60)
                          / 60)
                         / 24)
                END
                   AS enddays,
                TO_CHAR (TO_DATE (v_enddate, 'DD/MM/YYYY HH:MI:SS AM'),
                         'HH12')
                   endhrs,
                TO_DATE (TO_CHAR (SYSDATE, 'DD/MM/YYYY'), 'DD/MM/YYYY')
                   createdays
           INTO v_st_days,
                v_st_hrs,
                v_end_days,
                v_end_hrs,
                v_create_date
           FROM DUAL;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (
               'error parsing Calendar dates...' || SQLERRM (SQLCODE));
      END;

      --raise_error('start days' ||v_st_days||'end days'||v_end_days);
      IF v_today > v_enddate
      THEN
         BEGIN
            v_end_days := v_end_days * -1;
         END;
      END IF;

      IF v_today > v_startdate
      THEN
         BEGIN
            v_st_days := v_st_days * -1;
         END;
      END IF;

      --RAISE_ERROR('Start Days '||v_st_days||'starthr '||v_st_hrs||'end Days ' ||v_end_days||'end hr '||v_end_hrs);
      INSERT INTO tqc_calendar_activities (cala_code,
                                           cala_location,
                                           cala_title,
                                           cala_startadddays,
                                           cala_starthour,
                                           cala_endadddays,
                                           cala_endaddhours,
                                           cala_user,
                                           cala_create_date)
           VALUES (calendar_activities_seq.NEXTVAL,
                   v_loc,
                   v_title,
                   v_st_days,
                   v_st_hrs,
                   v_end_days,
                   v_end_hrs,
                   v_user,
                   v_create_date);
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (
            'error inserting calendar activities...' || SQLERRM (SQLCODE));
   END;

   PROCEDURE conv_proposer_to_clnt (v_clnt_code   IN     NUMBER,
                                    v_sht_desc       OUT VARCHAR2,
                                    v_name           OUT VARCHAR2,
                                    v_err            OUT VARCHAR2)
   IS
   BEGIN
      BEGIN
         BEGIN
            UPDATE tqc_clients
               SET clnt_proposer = 'N'
             WHERE clnt_code = v_clnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_err := 'error converting proposer to client...';
               RETURN;
         END;

         SELECT clnt_sht_desc,
                LTRIM (RTRIM (clnt_name || ' ' || clnt_other_names)) fullname
           INTO v_sht_desc, v_name
           FROM tqc_clients
          WHERE clnt_code = v_clnt_code;
      END;
   END;

   PROCEDURE find_states (v_states OUT country_states_rec, v_cou_code NUMBER)
   IS
   BEGIN
      -- RAISE_ERROR('COUNTRY '||v_cou_code);
      OPEN v_states FOR
         SELECT sts_code,
                sts_cou_code,
                sts_sht_desc,
                sts_name
           FROM tqc_states
          WHERE sts_cou_code = v_cou_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('error determining country states...' || v_cou_code);
   END;

   PROCEDURE inactivate_tickets (v_endorsement NUMBER)
   IS
   BEGIN
      UPDATE tqc_bpm_tickets
         SET tckt_active = 'N'
       WHERE tckt_endr_code = v_endorsement;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('error updating ticket status...');
   END;

   PROCEDURE inactivate_tickets (v_trans_type VARCHAR2, v_batch_no NUMBER)
   IS
   BEGIN
      --
      IF v_trans_type = 'NB'
      THEN
         BEGIN
            UPDATE tqc_bpm_tickets
               SET tckt_active = 'N'
             WHERE tckt_pol_code = v_batch_no AND tckt_sys_module = 'P';
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('error updating ticket status...');
         END;
      ELSIF v_trans_type IN ('DC', 'CO', 'EN', 'CN', 'EX')
      THEN
         BEGIN
            UPDATE tqc_bpm_tickets
               SET tckt_active = 'N'
             WHERE tckt_pol_code = v_batch_no AND tckt_sys_module = 'E';
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('error updating ticket status...');
         END;
      ELSIF v_trans_type = 'RN'
      THEN
         BEGIN
            UPDATE tqc_bpm_tickets
               SET tckt_active = 'N'
             WHERE tckt_pol_code = v_batch_no
                   AND tckt_sys_module IN ('R', 'RT');
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('error updating ticket status...');
         END;
      ELSE
         BEGIN
            raise_error (
               'Provide A Valid Transaction Type. Type provided is '
               || v_trans_type);
         END;
      END IF;
   END;

   PROCEDURE save_workflw_attributes (v_tckt_code      NUMBER,
                                      v_sys_code       NUMBER,
                                      v_sys_module     VARCHAR2,
                                      v_clnt_code      NUMBER,
                                      v_agn_code       NUMBER,
                                      v_pol_code       NUMBER,
                                      v_pol_no         VARCHAR2,
                                      v_clm_no         VARCHAR2,
                                      v_quot_code      NUMBER,
                                      v_user           VARCHAR2,
                                      v_tckt_date      DATE,
                                      v_process_id     VARCHAR2,
                                      v_quot_no        VARCHAR2,
                                      v_endr_code      NUMBER,
                                      v_prod_type      VARCHAR2,
                                      v_tckt_to        VARCHAR2,
                                      v_remarks        VARCHAR2,
                                      v_endorsment     VARCHAR2,
                                      v_trans_no       NUMBER,
                                      v_prp_code       NUMBER DEFAULT NULL,
                                      v_cla_type       VARCHAR2 DEFAULT NULL,
                                      v_tckt_cqr_no    VARCHAR2 DEFAULT NULL)
   IS
      v_cnt        NUMBER;
      v_end        NUMBER;
      v_batch_no   NUMBER;
      v_module     VARCHAR2 (25);
   BEGIN
      BEGIN
         SELECT COUNT (tckt_code)
           INTO v_cnt
           FROM tqc_bpm_tickets
          WHERE tckt_code = v_tckt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('error determining ticket information...');
      END;

      IF v_cnt > 0
      THEN
         SELECT tckt_code
           INTO v_end
           FROM tqc_bpm_tickets
          WHERE tckt_code = v_tckt_code;

         SELECT NVL (tckt_pol_code, v_pol_code)
           INTO v_batch_no
           FROM tqc_bpm_tickets
          WHERE tckt_code = v_tckt_code;

         --       RAISE_ERROR(' v_batch_no == '||v_batch_no);
         SELECT tckt_sys_module
           INTO v_module
           FROM tqc_bpm_tickets
          WHERE tckt_code = v_tckt_code;

         IF v_module != 'MKT-TO-UNDPROP'
         THEN
            IF v_batch_no != v_pol_code
            THEN
               raise_error (
                     'Error saving ticket details...Ticket Batch..'
                  || v_batch_no
                  || '  v_pol_code..'
                  || v_pol_code);
            END IF;
         END IF;

         BEGIN
            UPDATE tqc_bpm_tickets
               SET tckt_endr_code = NVL (v_endr_code, tckt_endr_code),
                   tckt_pol_code = NVL (v_pol_code, tckt_pol_code),
                   tckt_pol_no = NVL (v_pol_no, tckt_pol_no),
                   tckt_clm_no = NVL (v_clm_no, tckt_clm_no),
                   tckt_prp_code = NVL (v_prp_code, tckt_prp_code),
                   tckt_transno = NVL (v_trans_no, tckt_transno),
                   tckt_cqr_no = NVL (v_tckt_cqr_no, tckt_cqr_no)
             WHERE tckt_code = v_tckt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating Ticket Status.');
         END;
      ELSE
         BEGIN
            INSERT INTO tqc_bpm_tickets (tckt_code,
                                         tckt_sys_code,
                                         tckt_sys_module,
                                         tckt_clnt_code,
                                         tckt_agn_code,
                                         tckt_pol_code,
                                         tckt_pol_no,
                                         tckt_clm_no,
                                         tckt_quot_code,
                                         tckt_by,
                                         tckt_date,
                                         tckt_process_id,
                                         tckt_quo_no,
                                         tckt_endr_code,
                                         tckt_prod_type,
                                         tckt_to,
                                         tckt_remarks,
                                         tckt_endorsement,
                                         tckt_transno,
                                         tckt_prp_code,
                                         tckt_cla_type,
                                         tckt_cqr_no)
                 VALUES (v_tckt_code,
                         v_sys_code,
                         v_sys_module,
                         v_clnt_code,
                         v_agn_code,
                         v_pol_code,
                         v_pol_no,
                         v_clm_no,
                         v_quot_code,
                         v_user,
                         SYSDATE,
                         v_process_id,
                         v_quot_no,
                         v_endr_code,
                         v_prod_type,
                         v_tckt_to,
                         v_remarks,
                         v_endorsment,
                         v_trans_no,
                         v_prp_code,
                         v_cla_type,
                         v_tckt_cqr_no);
         --COMMIT;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error inserting ticket attribute information..');
         END;
      END IF;
   END;

   FUNCTION get_default_usr_branch (v_usrname    IN VARCHAR2,
                                    v_sys_code   IN NUMBER)
      RETURN usr_branches_ref
   IS
      v_refcur    usr_branches_ref;
      v_usrcode   NUMBER;
   BEGIN
      v_usrcode := tqc_interfaces_pkg.usercode (v_usrname);

      OPEN v_refcur FOR
           SELECT brn_code,
                  brn_sht_desc,
                  brn_name,
                  brn_gen_pol_clm
             FROM tqc_branches,
                  tqc_regions,
                  tqc_organizations,
                  tqc_systems,
                  tqc_user_branches
            WHERE     reg_org_code = org_code
                  AND sys_org_code = org_code
                  AND sys_code = v_sys_code
                  AND brn_reg_code = reg_code
                  AND usb_brn_code = brn_code
                  AND usb_usr_code = v_usrcode
                  AND usb_dflt_brn = 'Y'
         ORDER BY brn_name;

      RETURN (v_refcur);
   END;

   PROCEDURE saverequireddocuments (v_action               VARCHAR2,
                                    v_rdoc_code            NUMBER,
                                    v_rdoc_sht_desc        VARCHAR2,
                                    v_rdoc_desc            VARCHAR2,
                                    v_rdoc_mandatory       VARCHAR2,
                                    v_rdoc_date            DATE,
                                    v_error            OUT VARCHAR2)
   IS
      v_count           NUMBER;
      v_new_rdoc_code   NUMBER;
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_required_documents
             WHERE rdoc_sht_desc = v_rdoc_sht_desc;

            IF v_count > 0
            THEN
               v_error := 'Record with ID Exists!';
               RETURN;
            END IF;

            SELECT tqc_rdoc_code_seq.NEXTVAL INTO v_new_rdoc_code FROM DUAL;

            INSERT INTO tqc_required_documents (rdoc_code,
                                                rdoc_sht_desc,
                                                roc_desc,
                                                roc_mandatory,
                                                roc_date_added)
                 VALUES (v_new_rdoc_code,
                         v_rdoc_sht_desc,
                         v_rdoc_desc,
                         v_rdoc_mandatory,
                         NVL (v_rdoc_date, SYSDATE));
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error := 'Error inserting Required Documents...';
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_required_documents
               SET rdoc_sht_desc = NVL (v_rdoc_sht_desc, rdoc_sht_desc),
                   roc_desc = NVL (v_rdoc_desc, roc_desc),
                   roc_mandatory = NVL (v_rdoc_mandatory, roc_mandatory)
             WHERE rdoc_code = v_rdoc_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error := 'Error Updating Required Documents...';
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_required_documents
                  WHERE rdoc_code = v_rdoc_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error := 'Error Deleting Required Documents...';
         END;
      ELSE
         v_error := 'Invalid Action Selected...';
      END IF;
   END;

   PROCEDURE saveclientdocuments (v_action            VARCHAR2,
                                  v_cdocr_code        NUMBER,
                                  v_rdoc_code         NUMBER,
                                  v_clnt_code         NUMBER,
                                  v_submitted         VARCHAR2,
                                  v_date_submitted    DATE,
                                  v_ref_no            VARCHAR2,
                                  v_rmrk              VARCHAR2,
                                  v_user              VARCHAR2)
   IS
      v_new_cdocr_code   NUMBER;
      v_mandatory        VARCHAR2 (1);
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            SELECT tqc_cdocr_code_seq.NEXTVAL INTO v_new_cdocr_code FROM DUAL;

            INSERT INTO tqc_client_documents (cdocr_code,
                                              cdocr_rdoc_code,
                                              cdocr_clnt_code,
                                              cdocr_submited,
                                              cdocr_date_s,
                                              cdocr_ref_no,
                                              cdocr_rmrk,
                                              cdocr_user_receivd)
                 VALUES (v_new_cdocr_code,
                         v_rdoc_code,
                         v_clnt_code,
                         v_submitted,
                         NVL (v_date_submitted, SYSDATE),
                         v_ref_no,
                         v_rmrk,
                         v_user);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Client Document...');
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_client_documents
               SET cdocr_submited = NVL (v_submitted, cdocr_submited),
                   cdocr_date_s = NVL (v_date_submitted, cdocr_date_s),
                   cdocr_ref_no = NVL (v_ref_no, cdocr_ref_no),
                   cdocr_rmrk = NVL (v_rmrk, cdocr_rmrk),
                   cdocr_user_receivd = NVL (v_user, cdocr_user_receivd)
             WHERE cdocr_code = v_cdocr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error updating Client Document...');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            --- RAISE_ERROR('*************'||v_rdoc_code);
            SELECT roc_mandatory
              INTO v_mandatory
              FROM tqc_required_documents
             WHERE rdoc_code = v_rdoc_code;

            IF v_mandatory = 'Y'
            THEN
               DELETE FROM tqc_client_documents
                     WHERE cdocr_code = v_cdocr_code;
            ELSE
               raise_error ('You Cannot Delete a Mandatory Document...');
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error deleting Client Documents...');
         END;
      END IF;
   END;

   PROCEDURE populaterequireddocuments (v_clnt_code NUMBER, v_user VARCHAR2)
   IS
      CURSOR reqdocs
      IS
         SELECT *
           FROM tqc_required_documents
          WHERE roc_mandatory = 'Y';
   BEGIN
      FOR rd IN reqdocs
      LOOP
         INSERT INTO tqc_client_documents (cdocr_code,
                                           cdocr_rdoc_code,
                                           cdocr_clnt_code,
                                           cdocr_submited,
                                           cdocr_date_s,
                                           cdocr_user_receivd)
              VALUES (tqc_cdocr_code_seq.NEXTVAL,
                      rd.rdoc_code,
                      v_clnt_code,
                      'N',
                      NULL,
                      v_user);
      END LOOP;
   END;

   PROCEDURE get_sys_users (v_syscode         IN     NUMBER,
                            v_sys_users_cur      OUT sys_users_rec)
   IS
   BEGIN
      OPEN v_sys_users_cur FOR
         SELECT usr_code, usr_username, usr_name
           FROM tqc_users, tqc_user_systems
          WHERE usr_code = usys_usr_code AND usys_sys_code = v_syscode;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('error determining system Users...');
   END;

   PROCEDURE bpm_process_flow (v_module IN VARCHAR2, v_bpm OUT VARCHAR2)
   IS
   BEGIN
      BEGIN
         SELECT gin_parameters_pkg.get_param_varchar ('POLICIES_BPM')
           INTO v_bpm
           FROM DUAL;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_bpm := 'Y';
      END;
   END;

   PROCEDURE bpm_claims_process_flow (v_module   IN     VARCHAR2,
                                      v_bpm         OUT VARCHAR2)
   IS
   BEGIN
      BEGIN
         SELECT gin_parameters_pkg.get_param_varchar ('CLAIMS_BPM')
           INTO v_bpm
           FROM DUAL;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_bpm := 'Y';
      END;
   END;

   PROCEDURE save_memo_type (v_action         VARCHAR2,
                             v_mtyp_code      NUMBER,
                             v_sys_code       NUMBER,
                             v_memo_type      VARCHAR2,
                             v_sy_app_code    NUMBER,
                             v_status         VARCHAR2 DEFAULT 'A',
                             v_sub_class      NUMBER,
                             v_appllvl        VARCHAR2)
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_memo_types (mtyp_code,
                                        mtyp_sys_code,
                                        mtyp_memo_type,
                                        mtyp_saa_code,
                                        mtyp_status,
                                        mtyp_sub_code,
                                        mtyp_appl_lvl)
                 VALUES (tqc_mtyp_code_seq.NEXTVAL,
                         v_sys_code,
                         v_memo_type,
                         v_sy_app_code,
                         v_status,
                         v_sub_class,
                         v_appllvl);
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_memo_types
               SET mtyp_memo_type = NVL (v_memo_type, mtyp_memo_type),
                   mtyp_saa_code = NVL (v_sy_app_code, mtyp_saa_code),
                   mtyp_status = NVL (v_status, mtyp_status),
                   mtyp_sub_code = v_sub_class,
                   mtyp_appl_lvl = NVL (v_appllvl, mtyp_appl_lvl)
             WHERE mtyp_code = v_mtyp_code;
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_memo_types
                  WHERE mtyp_code = v_mtyp_code;
         END;
      ELSE
         BEGIN
            raise_error ('invalid Action');
         END;
      END IF;
   END;

   PROCEDURE save_memo_details (v_action         VARCHAR2,
                                v_subject        VARCHAR2,
                                v_content        VARCHAR2,
                                v_memo_code      NUMBER,
                                v_mtyp_code      NUMBER,
                                v_memdet_code    NUMBER)
   IS
      v_new_memo_code   NUMBER;
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            SELECT tqc_memo_code_seq.NEXTVAL INTO v_new_memo_code FROM DUAL;

            INSERT INTO tqc_memos (memo_code, memo_subject, memo_mtyp_code)
                 VALUES (v_new_memo_code, v_subject, v_mtyp_code);

            INSERT
              INTO tqc_memo_details (memdet_code,
                                     memdet_content,
                                     memdet_memo_code)
            VALUES (tqc_memdet_code_seq.NEXTVAL, v_content, v_new_memo_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error inserting memo Details...');
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_memos
               SET memo_subject = v_subject
             WHERE memo_code = v_memo_code;

            UPDATE tqc_memo_details
               SET memdet_content = NVL (v_content, memdet_content)
             WHERE memdet_code = v_memdet_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error updating memo Details...');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_memo_details
                  WHERE memdet_code = v_memdet_code;

            DELETE FROM tqc_memos
                  WHERE memo_code = v_memo_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error deleting memo Details...');
         END;
      ELSE
         BEGIN
            raise_error ('invalid Action');
         END;
      END IF;
   END;

   PROCEDURE inactivate_renewal_tickets (v_pol_code NUMBER)
   IS
      CURSOR tickets (
         vpolcode IN NUMBER)
      IS
         SELECT DISTINCT tckt_code
           FROM jbpm4_task, tqc_bpm_tickets
          WHERE name_ IN
                   ('Underwrite Policy Renewal',
                    'Underwrite Policy Reinstatement')
                AND tckt_code = dbid_
                AND tckt_pol_code = vpolcode;
   BEGIN
      FOR tickts IN tickets (v_pol_code)
      LOOP
         BEGIN
            --RAISE_ERROR(TICKTS.TCKT_CODE);
            UPDATE tqc_bpm_tickets
               SET tckt_active = 'N'
             WHERE tckt_code = tickts.tckt_code;

            COMMIT;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('error updating ticket status...');
         END;
      END LOOP;
   END;

   PROCEDURE get_batch_pol_bpm_details (v_pol_batch_no       NUMBER,
                                        v_action             VARCHAR2,
                                        v_pol_status     OUT VARCHAR2,
                                        v_tckt_code      OUT NUMBER)
   IS
   BEGIN
      BEGIN
         SELECT pol_policy_status
           INTO v_pol_status
           FROM gin_policies
          WHERE pol_batch_no = v_pol_batch_no;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error Determining Policy Status');
      END;

      IF v_pol_status IN ('NB')
      THEN
         BEGIN
            SELECT DISTINCT tckt_code
              INTO v_tckt_code
              FROM jbpm4_task, tqc_bpm_tickets
             WHERE     name_ = v_action
                   AND tckt_code = dbid_
                   AND tckt_pol_code = v_pol_batch_no;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_pol_status := 'NF';
               RETURN;
         END;
      ELSE
         BEGIN
            SELECT DISTINCT tckt_code
              INTO v_tckt_code
              FROM jbpm4_task, tqc_bpm_tickets
             WHERE     name_ = v_action
                   AND tckt_code = dbid_
                   AND tckt_endr_code = v_pol_batch_no;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_pol_status := 'NF';
               RETURN;
         END;
      END IF;
   END;

   PROCEDURE delete_email_attachment (v_ett_code NUMBER)
   IS
   BEGIN
      DELETE FROM tqc_email_attachments
            WHERE eatt_code = v_ett_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error deleting Email Attachment');
   END;

   PROCEDURE get_avail_policies (v_tickets   OUT avail_tickets_rec,
                                 v_status        VARCHAR2)
   IS
   BEGIN
      OPEN v_tickets FOR
         SELECT pol_batch_no,
                pol_policy_no,
                clnt_other_names || ' ' || clnt_name AS client,
                pol_policy_status,
                DECODE (NVL (pol_authosrised, 'N'), 'N', 'No', 'Yes')
                   authorised,
                clnt_code
           FROM gin_policies, tqc_clients
          WHERE pol_prp_code = clnt_code AND pol_policy_status = v_status;
   --AND TCKT_CODE NOT IN(SELECT DBID_ FROM JBPM4_LOB)
   --AND TCKT_SYS_CODE = 37;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('error determining system policies...');
   END;

   PROCEDURE get_avail_quotations (v_tickets OUT avail_tickets_rec)
   IS
   BEGIN
      OPEN v_tickets FOR
         SELECT quot_code,
                quot_no,
                clnt_other_names || ' ' || clnt_name client,
                quot_status,
                DECODE (NVL (quot_confirmed, 'N'), 'N', 'No', 'Yes')
                   confirmed,
                clnt_code
           FROM gin_quotations, tqc_clients
          WHERE quot_prp_code = clnt_code;
   --AND QUOT_CODE = TCKT_QUOT_CODE
   --AND TCKT_CODE NOT IN(SELECT DBID_ FROM JBPM4_LOB)
   --AND TCKT_SYS_CODE = 37;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('error determining system policies...');
   END;

   PROCEDURE get_process_task_points (
      v_syscode       IN     NUMBER,
      v_process       IN     VARCHAR2,
      v_process_cur      OUT tickets_points_rec)
   IS
   BEGIN
      OPEN v_process_cur FOR
           SELECT tbtp_code, tptp_sht_desc, tptp_desc
             FROM tqc_sys_processes, tqc_bpm_task_points
            WHERE     sprc_sys_code = v_syscode
                  AND sprc_sht_desc = v_process
                  AND tbtp_sprc_code = sprc_code
         ORDER BY tbtp_code ASC;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('error determining the process to be undertaken...');
   END;

   PROCEDURE inactivate_ext_tickets (v_ticket_no IN NUMBER)
   IS
   BEGIN
      UPDATE tqc_bpm_tickets
         SET tckt_active = 'N'
       WHERE tckt_code = v_ticket_no;
   END;

   PROCEDURE remove_tickets (v_task_id NUMBER)
   IS
   BEGIN
      DELETE FROM tqc_bpm_tickets
            WHERE tckt_code = v_task_id;

      DELETE FROM jbpm4_task
            WHERE dbid_ = v_task_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Removing Ticket Details..');
   END;

   FUNCTION get_ticket_dtls_by_tckt (v_tckt_no IN NUMBER)
      RETURN tickets_ref
   IS
      v_refcur   tickets_ref;
   BEGIN
      OPEN v_refcur FOR
           SELECT *
             FROM (SELECT a.*
                     FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                    DECODE (
                                       tqc_bpm_tickets.tckt_sys_code,
                                       37, 'GENERAL INSURANCE SYSTEM',
                                       27, 'LIFE MANAGEMENT SYSTEM',
                                       1, 'FINANCIAL MANAGEMENT SYSTEM',
                                       0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                       usrsystem,
                                    DECODE (tqc_bpm_tickets.tckt_sys_module,
                                            'Q', 'Quotation',
                                            'P', 'Underwriting',
                                            'C', 'Claims')
                                       sysmodule,
                                    tqc_bpm_tickets.tckt_clnt_code,
                                    (clnt_name || ' ' || clnt_other_names) client,
                                    tqc_bpm_tickets.tckt_agn_code,
                                    agn_name AGENT,
                                    tqc_bpm_tickets.tckt_pol_code,
                                    tqc_bpm_tickets.tckt_pol_no,
                                    tqc_bpm_tickets.tckt_clm_no,
                                    tqc_bpm_tickets.tckt_quot_code,
                                    tqc_bpm_tickets.tckt_quo_no,
                                    tqc_bpm_tickets.tckt_by,
                                    tqc_bpm_tickets.tckt_date,
                                    tqc_bpm_tickets.tckt_process_id,
                                    tqc_bpm_tickets.tckt_sys_module
                                       sys_module_code,
                                    tckt_endr_code,
                                    tckt_prod_type,
                                    tckt_to,
                                    tckt_remarks,
                                    name_ tckt_name,
                                    duedate_ tckt_due_date,
                                    tckt_endorsement,
                                    tckt_transno,
                                    DECODE (tckt_sys_module,
                                            'Q', tckt_quo_no,
                                            'C', tckt_clm_no,
                                            tckt_pol_no)
                                       ref_no,
                                    tckt_prp_code,
                                    NULL,
                                    tckt_type,
                                    NULL POLICY_STATUS,
                                    TCKT_TRAN_EFF_DATE,
                                    TCKT_GGT_NO,
                                    NULL USR_TYPE,
                                    NVL (tqc_bpm_tickets.tckt_assignment_date,
                                         tqc_bpm_tickets.tckt_date)
                                       assignment_date
                               FROM tqc_bpm_tickets,
                                    tqc_clients,
                                    tqc_agencies,
                                    jbpm4_task
                              WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                                    --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                    AND agn_code(+) =
                                           tqc_bpm_tickets.tckt_agn_code
                                    AND dbid_ = tqc_bpm_tickets.tckt_code
                                    --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                    AND tckt_active != 'N'
                                    AND tckt_type = 'S'
                                    AND tckt_code = v_tckt_no
                           ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                    WHERE a.ref_no IS NOT NULL
                   UNION ALL
                   SELECT tqc_bpm_tickets.tckt_code,
                          DECODE (tqc_bpm_tickets.tckt_sys_code,
                                  37, 'GENERAL INSURANCE SYSTEM',
                                  27, 'LIFE MANAGEMENT SYSTEM',
                                  1, 'FINANCIAL MANAGEMENT SYSTEM',
                                  0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                             usrsystem,
                          DECODE (tqc_bpm_tickets.tckt_sys_module,
                                  'Q', 'Quotation',
                                  'P', 'Underwriting',
                                  'C', 'Claims')
                             sysmodule,
                          tqc_bpm_tickets.tckt_clnt_code,
                          (clnt_name || ' ' || clnt_other_names) client,
                          tqc_bpm_tickets.tckt_agn_code,
                          agn_name AGENT,
                          tqc_bpm_tickets.tckt_pol_code,
                          tqc_bpm_tickets.tckt_pol_no,
                          tqc_bpm_tickets.tckt_clm_no,
                          tqc_bpm_tickets.tckt_quot_code,
                          tqc_bpm_tickets.tckt_quo_no,
                          tqc_bpm_tickets.tckt_by,
                          tqc_bpm_tickets.tckt_date,
                          tqc_bpm_tickets.tckt_process_id,
                          tqc_bpm_tickets.tckt_sys_module sys_module_code,
                          tckt_endr_code,
                          tckt_prod_type,
                          tckt_to,
                          tckt_remarks,
                          name_ tckt_name,
                          duedate_ tckt_due_date,
                          tckt_endorsement,
                          tckt_transno,
                          tckt_extern_ref_no ref_no,
                          tckt_prp_code,
                          NULL,
                          tckt_type,
                          NULL POLICY_STATUS,
                          TCKT_TRAN_EFF_DATE,
                          TCKT_GGT_NO,
                          NULL USR_TYPE,
                          NVL (tqc_bpm_tickets.tckt_assignment_date,
                               tqc_bpm_tickets.tckt_date)
                             assignment_date
                     FROM tqc_bpm_tickets,
                          tqc_clients,
                          tqc_agencies,
                          jbpm4_task
                    WHERE     tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                          AND tckt_type = 'E'
                          --AND PRP_CLNT_CODE = CLNT_CODE(+)
                          AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                          AND dbid_ = tqc_bpm_tickets.tckt_code
                          --AND TCKT_CLM_NO = NVL(:v_claim_no, TCKT_CLM_NO)
                          AND tckt_active != 'N'
                          AND tckt_code = v_tckt_no)
         ORDER BY tckt_code DESC;

      RETURN v_refcur;
   END;

   FUNCTION get_policy_tickets (v_pol_no VARCHAR2, v_name VARCHAR2)
      RETURN tickets_ref
   IS
      v_refcur   tickets_ref;
   BEGIN
      OPEN v_refcur FOR
           SELECT *
             FROM (SELECT a.*
                     FROM (SELECT tqc_bpm_tickets.tckt_code,
                                  DECODE (
                                     tqc_bpm_tickets.tckt_sys_code,
                                     37, 'GENERAL INSURANCE SYSTEM',
                                     27, 'LIFE MANAGEMENT SYSTEM',
                                     1, 'FINANCIAL MANAGEMENT SYSTEM',
                                     0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                     usrsystem,
                                  DECODE (tqc_bpm_tickets.tckt_sys_module,
                                          'Q', 'Quotation',
                                          'P', 'Underwriting',
                                          'C', 'Claims')
                                     sysmodule,
                                  tqc_bpm_tickets.tckt_clnt_code,
                                  (clnt_name || ' ' || clnt_other_names) client,
                                  tqc_bpm_tickets.tckt_agn_code,
                                  agn_name AGENT,
                                  tqc_bpm_tickets.tckt_pol_code,
                                  tqc_bpm_tickets.tckt_pol_no,
                                  tqc_bpm_tickets.tckt_clm_no,
                                  tqc_bpm_tickets.tckt_quot_code,
                                  tqc_bpm_tickets.tckt_quo_no,
                                  tqc_bpm_tickets.tckt_by,
                                  tqc_bpm_tickets.tckt_date,
                                  tqc_bpm_tickets.tckt_process_id,
                                  tqc_bpm_tickets.tckt_sys_module
                                     sys_module_code,
                                  tckt_endr_code,
                                  tckt_prod_type,
                                  tckt_to,
                                  tckt_remarks,
                                  name_ tckt_name,
                                  duedate_ tckt_due_date,
                                  tckt_endorsement,
                                  tckt_transno,
                                  DECODE (tckt_sys_module,
                                          'Q', tckt_quo_no,
                                          'C', tckt_clm_no,
                                          tckt_pol_no)
                                     ref_no,
                                  tckt_prp_code,
                                  NULL,
                                  tckt_type,
                                  DECODE (POL_POLICY_STATUS,
                                          'NB', 'NB',
                                          'SP', 'NB',
                                          'RN', 'NB',
                                          'RE', 'NB',
                                          'DC', 'EN',
                                          'EN', 'EN',
                                          'EX', 'EN',
                                          'CN', 'EN',
                                          POL_POLICY_STATUS)
                                     POL_STAT,
                                  TCKT_TRAN_EFF_DATE,
                                  TCKT_GGT_NO,
                                  NULL USR_TYPE,
                                  NVL (tqc_bpm_tickets.tckt_assignment_date,
                                       tqc_bpm_tickets.tckt_date)
                                     assignment_date
                             FROM tqc_bpm_tickets,
                                  tqc_clients,
                                  tqc_agencies,
                                  jbpm4_task,
                                  gin_policies
                            WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                                  --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                  AND agn_code(+) =
                                         tqc_bpm_tickets.tckt_agn_code
                                  AND tckt_pol_code = pol_batch_no
                                  AND dbid_ = tqc_bpm_tickets.tckt_code
                                  --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                  AND tckt_active != 'N'
                                  AND tckt_type = 'S'
                                  AND TCKT_SYS_CODE = 37
                                  AND TCKT_PROCESS_ID IS NOT NULL
                                  AND tqc_bpm_tickets.tckt_pol_no LIKE
                                         '%' || v_pol_no || '%'
                                  AND name_ LIKE '%' || v_name || '%'
                                  AND tckt_sys_module IN
                                         ('P', 'U', 'R', 'RT', 'RE', 'E')
                           UNION
                           SELECT tqc_bpm_tickets.tckt_code,
                                  DECODE (
                                     tqc_bpm_tickets.tckt_sys_code,
                                     37, 'GENERAL INSURANCE SYSTEM',
                                     27, 'LIFE MANAGEMENT SYSTEM',
                                     1, 'FINANCIAL MANAGEMENT SYSTEM',
                                     0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                     usrsystem,
                                  DECODE (tqc_bpm_tickets.tckt_sys_module,
                                          'Q', 'Quotation',
                                          'P', 'Underwriting',
                                          'C', 'Claims')
                                     sysmodule,
                                  tqc_bpm_tickets.tckt_clnt_code,
                                  (clnt_name || ' ' || clnt_other_names) client,
                                  tqc_bpm_tickets.tckt_agn_code,
                                  agn_name AGENT,
                                  tqc_bpm_tickets.tckt_pol_code,
                                  tqc_bpm_tickets.tckt_pol_no,
                                  tqc_bpm_tickets.tckt_clm_no,
                                  tqc_bpm_tickets.tckt_quot_code,
                                  tqc_bpm_tickets.tckt_quo_no,
                                  tqc_bpm_tickets.tckt_by,
                                  tqc_bpm_tickets.tckt_date,
                                  tqc_bpm_tickets.tckt_process_id,
                                  tqc_bpm_tickets.tckt_sys_module
                                     sys_module_code,
                                  tckt_endr_code,
                                  tckt_prod_type,
                                  tckt_to,
                                  tckt_remarks,
                                  name_ tckt_name,
                                  duedate_ tckt_due_date,
                                  tckt_endorsement,
                                  tckt_transno,
                                  DECODE (tckt_sys_module,
                                          'Q', tckt_quo_no,
                                          'C', tckt_clm_no,
                                          tckt_pol_no)
                                     ref_no,
                                  tckt_prp_code,
                                  NULL,
                                  tckt_type,
                                  DECODE (POL_POLICY_STATUS,
                                          'RN', 'RN',
                                          'RE', 'RE',
                                          POL_POLICY_STATUS)
                                     POL_STAT,
                                  TCKT_TRAN_EFF_DATE,
                                  TCKT_GGT_NO,
                                  NULL USR_TYPE,
                                  NVL (tqc_bpm_tickets.tckt_assignment_date,
                                       tqc_bpm_tickets.tckt_date)
                                     assignment_date
                             FROM tqc_bpm_tickets,
                                  tqc_clients,
                                  tqc_agencies,
                                  jbpm4_task,
                                  gin_ren_policies
                            WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                                  --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                  AND agn_code(+) =
                                         tqc_bpm_tickets.tckt_agn_code
                                  AND tckt_pol_code = pol_batch_no
                                  AND dbid_ = tqc_bpm_tickets.tckt_code
                                  --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                  AND tckt_active != 'N'
                                  AND tqc_bpm_tickets.tckt_pol_no LIKE
                                         '%' || v_pol_no || '%'
                                  AND name_ LIKE '%' || v_name || '%'
                                  AND tckt_type = 'S'
                                  AND TCKT_SYS_CODE = 37
                                  AND TCKT_PROCESS_ID IS NOT NULL
                                  AND tckt_sys_module IN
                                         ('P', 'U', 'R', 'RT', 'RE', 'E')) a
                    WHERE a.ref_no IS NOT NULL
                   UNION ALL
                   SELECT tqc_bpm_tickets.tckt_code,
                          DECODE (tqc_bpm_tickets.tckt_sys_code,
                                  37, 'GENERAL INSURANCE SYSTEM',
                                  27, 'LIFE MANAGEMENT SYSTEM',
                                  1, 'FINANCIAL MANAGEMENT SYSTEM',
                                  0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                             usrsystem,
                          DECODE (tqc_bpm_tickets.tckt_sys_module,
                                  'Q', 'Quotation',
                                  'P', 'Underwriting',
                                  'C', 'Claims')
                             sysmodule,
                          tqc_bpm_tickets.tckt_clnt_code,
                          (clnt_name || ' ' || clnt_other_names) client,
                          tqc_bpm_tickets.tckt_agn_code,
                          agn_name AGENT,
                          tqc_bpm_tickets.tckt_pol_code,
                          tqc_bpm_tickets.tckt_pol_no,
                          tqc_bpm_tickets.tckt_clm_no,
                          tqc_bpm_tickets.tckt_quot_code,
                          tqc_bpm_tickets.tckt_quo_no,
                          tqc_bpm_tickets.tckt_by,
                          tqc_bpm_tickets.tckt_date,
                          tqc_bpm_tickets.tckt_process_id,
                          tqc_bpm_tickets.tckt_sys_module sys_module_code,
                          tckt_endr_code,
                          tckt_prod_type,
                          tckt_to,
                          tckt_remarks,
                          name_ tckt_name,
                          duedate_ tckt_due_date,
                          tckt_endorsement,
                          tckt_transno,
                          tckt_extern_ref_no ref_no,
                          tckt_prp_code,
                          NULL,
                          tckt_type,
                          DECODE (POL_POLICY_STATUS,
                                  'NB', 'NB',
                                  'SP', 'NB',
                                  'RN', 'NB',
                                  'RE', 'NB',
                                  'DC', 'EN',
                                  'EN', 'EN',
                                  'EX', 'EN',
                                  'CN', 'EN',
                                  POL_POLICY_STATUS)
                             POL_STAT,
                          TCKT_TRAN_EFF_DATE,
                          TCKT_GGT_NO,
                          NULL USR_TYPE,
                          NVL (tqc_bpm_tickets.tckt_assignment_date,
                               tqc_bpm_tickets.tckt_date)
                             assignment_date
                     FROM tqc_bpm_tickets,
                          tqc_clients,
                          tqc_agencies,
                          jbpm4_task,
                          gin_policies
                    WHERE     tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                          AND tckt_type = 'E'
                          --AND PRP_CLNT_CODE = CLNT_CODE(+)
                          AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                          AND dbid_ = tqc_bpm_tickets.tckt_code
                          AND TCKT_SYS_CODE = 37
                          AND tckt_pol_code = pol_batch_no
                          AND tqc_bpm_tickets.tckt_pol_no LIKE
                                 '%' || v_pol_no || '%'
                          AND name_ LIKE '%' || v_name || '%'
                          AND TCKT_PROCESS_ID IS NOT NULL
                          AND tckt_sys_module IN
                                 ('P', 'U', 'R', 'RT', 'RE', 'E')
                          --AND TCKT_CLM_NO = NVL(:v_claim_no, TCKT_CLM_NO)
                          AND tckt_active != 'N'
                   UNION
                   SELECT tqc_bpm_tickets.tckt_code,
                          DECODE (tqc_bpm_tickets.tckt_sys_code,
                                  37, 'GENERAL INSURANCE SYSTEM',
                                  27, 'LIFE MANAGEMENT SYSTEM',
                                  1, 'FINANCIAL MANAGEMENT SYSTEM',
                                  0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                             usrsystem,
                          DECODE (tqc_bpm_tickets.tckt_sys_module,
                                  'Q', 'Quotation',
                                  'P', 'Underwriting',
                                  'C', 'Claims')
                             sysmodule,
                          tqc_bpm_tickets.tckt_clnt_code,
                          (clnt_name || ' ' || clnt_other_names) client,
                          tqc_bpm_tickets.tckt_agn_code,
                          agn_name AGENT,
                          tqc_bpm_tickets.tckt_pol_code,
                          tqc_bpm_tickets.tckt_pol_no,
                          tqc_bpm_tickets.tckt_clm_no,
                          tqc_bpm_tickets.tckt_quot_code,
                          tqc_bpm_tickets.tckt_quo_no,
                          tqc_bpm_tickets.tckt_by,
                          tqc_bpm_tickets.tckt_date,
                          tqc_bpm_tickets.tckt_process_id,
                          tqc_bpm_tickets.tckt_sys_module sys_module_code,
                          tckt_endr_code,
                          tckt_prod_type,
                          tckt_to,
                          tckt_remarks,
                          name_ tckt_name,
                          duedate_ tckt_due_date,
                          tckt_endorsement,
                          tckt_transno,
                          tckt_extern_ref_no ref_no,
                          tckt_prp_code,
                          NULL,
                          tckt_type,
                          DECODE (POL_POLICY_STATUS,
                                  'RN', 'RN',
                                  'RE', 'RE',
                                  POL_POLICY_STATUS)
                             POL_STAT,
                          TCKT_TRAN_EFF_DATE,
                          TCKT_GGT_NO,
                          NULL USR_TYPE,
                          NVL (tqc_bpm_tickets.tckt_assignment_date,
                               tqc_bpm_tickets.tckt_date)
                             assignment_date
                     FROM tqc_bpm_tickets,
                          tqc_clients,
                          tqc_agencies,
                          jbpm4_task,
                          gin_ren_policies
                    WHERE     tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                          AND tckt_type = 'E'
                          --AND PRP_CLNT_CODE = CLNT_CODE(+)
                          AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                          AND dbid_ = tqc_bpm_tickets.tckt_code
                          AND TCKT_SYS_CODE = 37
                          AND tckt_pol_code = pol_batch_no
                          AND TCKT_PROCESS_ID IS NOT NULL
                          AND tckt_sys_module IN ('RT', 'RE')
                          AND tqc_bpm_tickets.tckt_pol_no LIKE
                                 '%' || v_pol_no || '%'
                          AND name_ LIKE '%' || v_name || '%'
                          --AND TCKT_CLM_NO = NVL(:v_claim_no, TCKT_CLM_NO)
                          AND tckt_active != 'N')
         ORDER BY tckt_code DESC;

      RETURN v_refcur;
   END;

   FUNCTION get_quote_tickets (v_quo_no VARCHAR2, v_name VARCHAR2)
      RETURN tickets_ref
   IS
      v_refcur   tickets_ref;
   BEGIN
      OPEN v_refcur FOR
           SELECT *
             FROM (SELECT a.*
                     FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                    DECODE (
                                       tqc_bpm_tickets.tckt_sys_code,
                                       37, 'GENERAL INSURANCE SYSTEM',
                                       27, 'LIFE MANAGEMENT SYSTEM',
                                       1, 'FINANCIAL MANAGEMENT SYSTEM',
                                       0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                       usrsystem,
                                    DECODE (tqc_bpm_tickets.tckt_sys_module,
                                            'Q', 'Quotation',
                                            'P', 'Underwriting',
                                            'C', 'Claims')
                                       sysmodule,
                                    tqc_bpm_tickets.tckt_clnt_code,
                                    (clnt_name || ' ' || clnt_other_names) client,
                                    tqc_bpm_tickets.tckt_agn_code,
                                    agn_name AGENT,
                                    tqc_bpm_tickets.tckt_pol_code,
                                    tqc_bpm_tickets.tckt_pol_no,
                                    tqc_bpm_tickets.tckt_clm_no,
                                    tqc_bpm_tickets.tckt_quot_code,
                                    tqc_bpm_tickets.tckt_quo_no,
                                    tqc_bpm_tickets.tckt_by,
                                    tqc_bpm_tickets.tckt_date,
                                    tqc_bpm_tickets.tckt_process_id,
                                    tqc_bpm_tickets.tckt_sys_module
                                       sys_module_code,
                                    tckt_endr_code,
                                    tckt_prod_type,
                                    tckt_to,
                                    tckt_remarks,
                                    name_ tckt_name,
                                    duedate_ tckt_due_date,
                                    tckt_endorsement,
                                    tckt_transno,
                                    DECODE (tckt_sys_module,
                                            'Q', tckt_quo_no,
                                            'C', tckt_clm_no,
                                            tckt_pol_no)
                                       ref_no,
                                    tckt_prp_code,
                                    NULL,
                                    tckt_type,
                                    NULL POLICY_STATUS,
                                    TCKT_TRAN_EFF_DATE,
                                    TCKT_GGT_NO,
                                    NULL USR_TYPE,
                                    NVL (tqc_bpm_tickets.tckt_assignment_date,
                                         tqc_bpm_tickets.tckt_date)
                                       assignment_date
                               FROM tqc_bpm_tickets,
                                    tqc_clients,
                                    tqc_agencies,
                                    jbpm4_task
                              WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                                    --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                    AND agn_code(+) =
                                           tqc_bpm_tickets.tckt_agn_code
                                    AND dbid_ = tqc_bpm_tickets.tckt_code
                                    --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                    AND tckt_active != 'N'
                                    AND tckt_type = 'S'
                                    AND tqc_bpm_tickets.tckt_quo_no LIKE
                                           '%' || v_quo_no || '%'
                                    AND name_ LIKE '%' || v_name || '%'
                                    AND tckt_sys_module IN ('Q')
                           ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                    WHERE a.ref_no IS NOT NULL
                   UNION ALL
                   SELECT tqc_bpm_tickets.tckt_code,
                          DECODE (tqc_bpm_tickets.tckt_sys_code,
                                  37, 'GENERAL INSURANCE SYSTEM',
                                  27, 'LIFE MANAGEMENT SYSTEM',
                                  1, 'FINANCIAL MANAGEMENT SYSTEM',
                                  0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                             usrsystem,
                          DECODE (tqc_bpm_tickets.tckt_sys_module,
                                  'Q', 'Quotation',
                                  'P', 'Underwriting',
                                  'C', 'Claims')
                             sysmodule,
                          tqc_bpm_tickets.tckt_clnt_code,
                          (clnt_name || ' ' || clnt_other_names) client,
                          tqc_bpm_tickets.tckt_agn_code,
                          agn_name AGENT,
                          tqc_bpm_tickets.tckt_pol_code,
                          tqc_bpm_tickets.tckt_pol_no,
                          tqc_bpm_tickets.tckt_clm_no,
                          tqc_bpm_tickets.tckt_quot_code,
                          tqc_bpm_tickets.tckt_quo_no,
                          tqc_bpm_tickets.tckt_by,
                          tqc_bpm_tickets.tckt_date,
                          tqc_bpm_tickets.tckt_process_id,
                          tqc_bpm_tickets.tckt_sys_module sys_module_code,
                          tckt_endr_code,
                          tckt_prod_type,
                          tckt_to,
                          tckt_remarks,
                          name_ tckt_name,
                          duedate_ tckt_due_date,
                          tckt_endorsement,
                          tckt_transno,
                          tckt_extern_ref_no ref_no,
                          tckt_prp_code,
                          NULL,
                          tckt_type,
                          NULL POLICY_STATUS,
                          TCKT_TRAN_EFF_DATE,
                          TCKT_GGT_NO,
                          NULL USR_TYPE,
                          NVL (tqc_bpm_tickets.tckt_assignment_date,
                               tqc_bpm_tickets.tckt_date)
                             assignment_date
                     FROM tqc_bpm_tickets,
                          tqc_clients,
                          tqc_agencies,
                          jbpm4_task
                    WHERE     tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                          AND tckt_type = 'E'
                          --AND PRP_CLNT_CODE = CLNT_CODE(+)
                          AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                          AND dbid_ = tqc_bpm_tickets.tckt_code
                          AND tckt_sys_module IN ('Q')
                          AND tqc_bpm_tickets.tckt_quo_no LIKE
                                 '%' || v_quo_no || '%'
                          AND name_ LIKE '%' || v_name || '%'
                          --AND TCKT_CLM_NO = NVL(:v_claim_no, TCKT_CLM_NO)
                          AND tckt_active != 'N')
         ORDER BY tckt_code DESC;

      RETURN v_refcur;
   END;

   FUNCTION get_clm_tickets (v_clm_no VARCHAR2, v_name VARCHAR2)
      RETURN tickets_ref
   IS
      v_refcur   tickets_ref;
   BEGIN
      OPEN v_refcur FOR
           SELECT *
             FROM (SELECT a.*
                     FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                    DECODE (
                                       tqc_bpm_tickets.tckt_sys_code,
                                       37, 'GENERAL INSURANCE SYSTEM',
                                       27, 'LIFE MANAGEMENT SYSTEM',
                                       1, 'FINANCIAL MANAGEMENT SYSTEM',
                                       0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                       usrsystem,
                                    DECODE (tqc_bpm_tickets.tckt_sys_module,
                                            'Q', 'Quotation',
                                            'P', 'Underwriting',
                                            'C', 'Claims')
                                       sysmodule,
                                    tqc_bpm_tickets.tckt_clnt_code,
                                    (clnt_name || ' ' || clnt_other_names) client,
                                    tqc_bpm_tickets.tckt_agn_code,
                                    agn_name AGENT,
                                    tqc_bpm_tickets.tckt_pol_code,
                                    tqc_bpm_tickets.tckt_pol_no,
                                    tqc_bpm_tickets.tckt_clm_no,
                                    tqc_bpm_tickets.tckt_quot_code,
                                    tqc_bpm_tickets.tckt_quo_no,
                                    tqc_bpm_tickets.tckt_by,
                                    tqc_bpm_tickets.tckt_date,
                                    tqc_bpm_tickets.tckt_process_id,
                                    tqc_bpm_tickets.tckt_sys_module
                                       sys_module_code,
                                    tckt_endr_code,
                                    tckt_prod_type,
                                    tckt_to,
                                    tckt_remarks,
                                    name_ tckt_name,
                                    duedate_ tckt_due_date,
                                    tckt_endorsement,
                                    tckt_transno,
                                    DECODE (tckt_sys_module,
                                            'Q', tckt_quo_no,
                                            'C', tckt_clm_no,
                                            tckt_pol_no)
                                       ref_no,
                                    tckt_prp_code,
                                    NULL,
                                    tckt_type,
                                    NULL POLICY_STATUS,
                                    TCKT_TRAN_EFF_DATE,
                                    TCKT_GGT_NO,
                                    NULL USR_TYPE,
                                    NVL (tqc_bpm_tickets.tckt_assignment_date,
                                         tqc_bpm_tickets.tckt_date)
                                       assignment_date
                               FROM tqc_bpm_tickets,
                                    tqc_clients,
                                    tqc_agencies,
                                    jbpm4_task
                              WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                                    --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                    AND agn_code(+) =
                                           tqc_bpm_tickets.tckt_agn_code
                                    AND dbid_ = tqc_bpm_tickets.tckt_code
                                    --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                    AND tckt_active != 'N'
                                    AND tqc_bpm_tickets.tckt_clm_no LIKE
                                           '%' || v_clm_no || '%'
                                    AND name_ LIKE '%' || v_name || '%'
                                    AND tckt_type = 'S'
                                    AND tckt_sys_module IN ('C')
                           ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                    WHERE a.ref_no IS NOT NULL
                   UNION ALL
                   SELECT tqc_bpm_tickets.tckt_code,
                          DECODE (tqc_bpm_tickets.tckt_sys_code,
                                  37, 'GENERAL INSURANCE SYSTEM',
                                  27, 'LIFE MANAGEMENT SYSTEM',
                                  1, 'FINANCIAL MANAGEMENT SYSTEM',
                                  0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                             usrsystem,
                          DECODE (tqc_bpm_tickets.tckt_sys_module,
                                  'Q', 'Quotation',
                                  'P', 'Underwriting',
                                  'C', 'Claims')
                             sysmodule,
                          tqc_bpm_tickets.tckt_clnt_code,
                          (clnt_name || ' ' || clnt_other_names) client,
                          tqc_bpm_tickets.tckt_agn_code,
                          agn_name AGENT,
                          tqc_bpm_tickets.tckt_pol_code,
                          tqc_bpm_tickets.tckt_pol_no,
                          tqc_bpm_tickets.tckt_clm_no,
                          tqc_bpm_tickets.tckt_quot_code,
                          tqc_bpm_tickets.tckt_quo_no,
                          tqc_bpm_tickets.tckt_by,
                          tqc_bpm_tickets.tckt_date,
                          tqc_bpm_tickets.tckt_process_id,
                          tqc_bpm_tickets.tckt_sys_module sys_module_code,
                          tckt_endr_code,
                          tckt_prod_type,
                          tckt_to,
                          tckt_remarks,
                          name_ tckt_name,
                          duedate_ tckt_due_date,
                          tckt_endorsement,
                          tckt_transno,
                          tckt_extern_ref_no ref_no,
                          tckt_prp_code,
                          NULL,
                          tckt_type,
                          NULL POLICY_STATUS,
                          TCKT_TRAN_EFF_DATE,
                          TCKT_GGT_NO,
                          NULL USR_TYPE,
                          NVL (tqc_bpm_tickets.tckt_assignment_date,
                               tqc_bpm_tickets.tckt_date)
                             assignment_date
                     FROM tqc_bpm_tickets,
                          tqc_clients,
                          tqc_agencies,
                          jbpm4_task
                    WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                          AND tckt_type = 'E'
                          AND tqc_bpm_tickets.tckt_clm_no LIKE
                                 '%' || v_clm_no || '%'
                          AND name_ LIKE '%' || v_name || '%'
                          --AND PRP_CLNT_CODE = CLNT_CODE(+)
                          AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                          AND dbid_ = tqc_bpm_tickets.tckt_code
                          AND tckt_sys_module IN ('C')
                          --AND TCKT_CLM_NO = NVL(:v_claim_no, TCKT_CLM_NO)
                          AND tckt_active != 'N')
         ORDER BY tckt_code DESC;

      RETURN v_refcur;
   END;

   FUNCTION check_trvl_agent_pwd (
      v_entered_user      IN     VARCHAR2,
      v_entered_pwd       IN     VARCHAR2,
      v_msg                  OUT VARCHAR2,
      v_last_login_date      OUT DATE,
      v_user_code            OUT NUMBER,
      coucode                OUT NUMBER,
      v_new_pwd1          IN     VARCHAR2,
      v_new_pwd2          IN     VARCHAR2,
      v_new_pwd_entry     IN     VARCHAR2 DEFAULT 'N')
      RETURN NUMBER
   IS
      v_password           VARCHAR (30);
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      couname              VARCHAR2 (100);
      agentshtdesc         VARCHAR2 (100);
      agentcontactcode     NUMBER;
      agentcontactname     VARCHAR2 (100);
      agentbrncode         NUMBER;
      -- v_last_login_date DATE;
      v_pwd                VARCHAR2 (100);
      --v_msg VARCHAR2(150);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      --v_agn_sht_desc varchar2(35);
      x                    NUMBER := 0;
      nusercode            NUMBER;

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
           SELECT accph_pwd, accph_pwd_change_dt
             FROM tqc_acc_contacts_pwd_hist
            WHERE UPPER (accph_accc_code) = vuser
         ORDER BY accph_pwd_change_dt DESC;
   BEGIN
      --MSG_BOX('DANI');
      BEGIN
         SELECT accc_code,
                TO_NUMBER (accc_code),
                UPPER (accc_username),
                UPPER (accc_status),
                accc_pwd,
                accc_pwd_changed,
                accc_dt_created,
                accc_personel_rank,
                accc_pwd_reset,
                accc_login_attempts,
                NVL (accc_last_login_date, TRUNC (SYSDATE)),
                cou_code,
                cou_name,
                NULL agn_sht_desc,
                accc_code,
                accc_name,
                accc_status,
                NULL agn_brn_code
           INTO nusercode,
                v_user_code,
                v_us_id,
                v_status,
                v_pwd,
                v_pwd_changed_dt,
                v_pwd_date_created,
                v_per_rnk,
                v_pwdreset,
                v_loginattempts,
                v_last_login_date,
                coucode,
                couname,
                agentshtdesc,
                agentcontactcode,
                agentcontactname,
                v_status,
                agentbrncode
           FROM tqc_embassy_contacts, tqc_countries
          WHERE     cou_code = accc_cou_code
                AND accc_login_allowed = 'Y'
                AND UPPER (accc_username) = v_entered_user;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_msg :=
               'The user is not a valid TurnQuest user. Enter valid username ';
            RETURN (2);
         WHEN OTHERS
         THEN
            v_msg := 'Unable to verify the username and password.';
            RETURN (2);
      END;

      IF NVL (v_pwdreset, 'N') = 'Y' AND NVL (v_new_pwd_entry, 'N') = 'N'
      THEN
         v_msg := 'Reset Password';
         RETURN (3);
      END IF;

      IF NVL (v_pwdreset, 'N') = 'N'
      THEN
         BEGIN
            SELECT accc_code
              INTO nusercode
              FROM tqc_embassy_contacts
             WHERE accc_pwd = gis_read.val (v_entered_pwd)
                   AND UPPER (accc_username) = v_entered_user;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_msg := 'Wrong User Password. Contact System Administrator';
               RETURN (2);
            WHEN OTHERS
            THEN
               v_msg :=
                  'Unable to verify the username and password. Contact System Administrator';
               RETURN (2);
         END;
      END IF;

      IF NVL (v_status, 'I') != 'A'
      THEN
         v_msg :=
            'Your account has been deactivated. Contact System Administrator.';
         RETURN (2);
      END IF;

      BEGIN
         v_syspwdattempts :=
            tqc_parameters_pkg.get_param_number ('SYSPWDLOGATTEMPTS');
         v_syspwdoldusecnt :=
            tqc_parameters_pkg.get_param_number ('SYSPWDOLDUSECNT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Error getting parameters...';
            RETURN (2);
      END;

      IF NVL (v_syspwdattempts, 0) > 0
         AND NVL (v_loginattempts, 0) >= NVL (v_syspwdattempts, 0)
      THEN
         v_msg :=
            'Max of ' || NVL (v_syspwdattempts, 0)
            || ' login attempt failure attained. Contact System Administrator for assistance.';
         RETURN (2);
      ELSIF NVL (v_pwd, '******') != gis_read.val (v_entered_pwd)
            AND NOT (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
      THEN
         update_login_attempt (v_entered_user);
         v_msg :=
            'Invalid username or password. Enter valid username and password and try again.';
         RETURN (2);
      END IF;

      BEGIN
         IF v_new_pwd1 IS NOT NULL
         THEN
            v_password := v_new_pwd1;
         ELSE
            v_password := v_entered_pwd;
         END IF;

         plenght := LENGTH (v_password);

         FOR x IN 1 .. plenght
         LOOP
            IF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 65 AND 90
            THEN
               capscnt := NVL (capscnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 97 AND 122
            THEN
               lwrcnt := NVL (lwrcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 49 AND 57
            THEN
               numcnt := NVL (numcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) IN
                     (35, 36, 37, 38, 42, 64)
            THEN
               speccnt := NVL (numcnt, 0) + 1;
            END IF;
         END LOOP;
      END;

      v_syspwddrtntime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDDRTNTIME');
      v_syspwdprmttime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDPRMTTIME');
      v_syspwdlength := tqc_parameters_pkg.get_param_number ('SYSPWDLENGTH');
      v_sysupppwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDUPPENGTH');
      v_syslwrpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDLWRLENGTH');
      v_sysnumpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDNUMLENGTH');
      v_sysspecpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDSPECLENGTH');
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      IF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
            (TO_NUMBER (v_syspwddrtntime))
      THEN
         v_msg :=
            'Your password has expired. Please specify new password to continue.';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y', accc_pwd_changed = TRUNC (SYSDATE)
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      /*ELSIF NVL(v_pwdreset,'N') = 'Y' THEN
         v_msg := 'Your password has been reset. MUST specify a new password.';
         v_check_failed := TRUE;
           RETURN(2);*/
      ELSIF UPPER (v_entered_user) = UPPER (v_entered_pwd)
      THEN
         v_msg :=
            'Your password is same as the username. This is not allowed.';

         UPDATE tqc_account_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
         /*ELSIF (TRUNC(SYSDATE)- v_pwd_changed_dt) >= (TO_NUMBER(v_syspwddrtntime)-TO_NUMBER(v_syspwdprmttime))  THEN
            v_msg := 'Your password is due to expire in '||((TO_NUMBER(v_syspwddrtntime)*1440)-(TO_NUMBER(v_syspwdprmttime)*1440))/1440||' days. ';
            v_check_failed := TRUE;
            v_mandatory       := FALSE;*/
         RETURN (2);
      ELSIF NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';

         UPDATE tqc_embassy_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysupppwlength
            || ' upper case characters ';

         UPDATE tqc_embassy_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';

         UPDATE tqc_embassy_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysnumpwlength
            || ' numeric characters ';

         UPDATE tqc_embassy_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      ELSIF NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
            AND NVL (v_sysspecpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysspecpwlength
            || ' special characters ';

         UPDATE tqc_embassy_contacts
            SET accc_pwd_reset = 'Y'
          WHERE accc_name = v_entered_user;

         v_check_failed := TRUE;
         RETURN (2);
      --v_mandatory     := FALSE;
      END IF;

      IF (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
         AND (v_new_pwd1 IS NOT NULL OR v_new_pwd2 IS NOT NULL)
      THEN
         IF NVL (v_new_pwd1, '****') != NVL (v_new_pwd2, '%%%%')
         THEN
            v_msg :=
               'Your password has been re-entered incorrectly. Please try again! ';
            RETURN (5);
         ELSE
            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt
                 FROM tqc_acc_embassy_pwd_hist
                WHERE UPPER (accph_accc_username) = v_entered_user
                      AND accph_pwd = gis_read.val (v_new_pwd1);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Cannot verify if password ever used before..';
                  RETURN (5);
            END;

            IF NVL (v_cnt, 0) > NVL (v_syspwdoldusecnt, 0)
            THEN
               v_msg :=
                  'Cannot use a password more than '
                  || NVL (v_syspwdoldusecnt, 0)
                  || ' time(s). Please specify a new password never used before.. ';
               RETURN (5);
            END IF;

            BEGIN
               x := 0;

               FOR usrs IN usrpwds (v_entered_user)
               LOOP
                  IF usrs.accph_pwd = gis_read.val (v_new_pwd1)
                  THEN
                     v_msg :=
                        'Cannot Reuse the last two passwords. Please specify a new password never user before.. ';
                     RETURN (5);
                  END IF;

                  x := x + 1;

                  IF x >= 2
                  THEN
                     EXIT;
                  END IF;
               END LOOP;
            END;

            --ELSE
            BEGIN
               UPDATE tqc_embassy_contacts
                  SET accc_pwd_reset = 'N',
                      accc_pwd_changed = TRUNC (SYSDATE),
                      accc_pwd = gis_read.val (v_new_pwd1),
                      accc_login_attempts = 0
                WHERE UPPER (accc_username) = v_entered_user;
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'User password could not be changed...';
                  RETURN (5);
            END;

            BEGIN
               INSERT INTO tqc_acc_embassy_pwd_hist (accph_code,
                                                     accph_accc_code,
                                                     accph_accc_username,
                                                     accph_pwd,
                                                     accph_pwd_change_dt)
                    VALUES (tqc_accph_code_seq.NEXTVAL,
                            nusercode,
                            v_entered_user,
                            gis_read.val (v_new_pwd1),
                            SYSDATE);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Error updating password change history..';
                  RETURN (5);
            END;

            v_pwdreset := 'N';
            v_pwd_changed_dt := TRUNC (SYSDATE);
            v_loginattempts := 0;
         END IF;
      END IF;

      UPDATE tqc_embassy_contacts
         SET accc_last_login_date = TRUNC (SYSDATE)
       WHERE UPPER (accc_username) = v_entered_user;

      --COMMIT;
      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   END;

   --   FUNCTION get_cheques_tickets
   --      RETURN tickets_ref
   --   IS
   --      v_refcur   tickets_ref;
   --   BEGIN
   --      -- RAISE_ERROR('HERERERER');
   --      OPEN v_refcur FOR
   --         SELECT   tqc_bpm_tickets.tckt_code,
   --                  DECODE (tqc_bpm_tickets.tckt_sys_code,
   --                          37, 'GENERAL INSURANCE SYSTEM',
   --                          27, 'LIFE MANAGEMENT SYSTEM',
   --                          1, 'FINANCIAL MANAGEMENT SYSTEM',
   --                          0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM'
   --                         ) usrsystem,
   --                  DECODE (tqc_bpm_tickets.tckt_sys_module,
   --                          'CBR', 'Cheque Requisition'
   --                         ) sysmodule,
   --                  tqc_bpm_tickets.tckt_clnt_code, NULL client,
   --                  tqc_bpm_tickets.tckt_agn_code, NULL AGENT,
   --                  tqc_bpm_tickets.tckt_pol_code, tqc_bpm_tickets.tckt_pol_no,
   --                  tqc_bpm_tickets.tckt_clm_no,
   --                  tqc_bpm_tickets.tckt_quot_code,
   --                  tqc_bpm_tickets.tckt_quo_no, tqc_bpm_tickets.tckt_by,
   --                  tqc_bpm_tickets.tckt_date, tqc_bpm_tickets.tckt_process_id,
   --                  tqc_bpm_tickets.tckt_sys_module sys_module_code,
   --                  tckt_endr_code, tckt_prod_type, tckt_to, tckt_remarks,
   --                  name_ tckt_name, duedate_ tckt_due_date, tckt_endorsement,
   --                  tckt_transno,
   --                  DECODE (tckt_sys_module,
   --                          'Q', tckt_quo_no,
   --                          'C', tckt_clm_no,
   --                          tckt_pol_no
   --                         ) ref_no,
   --                  tckt_prp_code, NULL, tckt_type
   --             FROM tqc_bpm_tickets, fms_cheques, jbpm4_task
   --            WHERE tqc_bpm_tickets.tckt_transno = cqr_no(+)
   --              AND dbid_ = tqc_bpm_tickets.tckt_code
   --              AND tckt_active != 'N'
   --              AND tckt_type = 'S'
   --              AND tckt_sys_code = 1
   --         ORDER BY tckt_code DESC;

   --      RETURN v_refcur;
   --   END;

   FUNCTION recoverpassword (v_entered_user   IN VARCHAR2,
                             v_reset_code     IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_usercode       NUMBER;
      v_emailaddress   VARCHAR2 (100);
   BEGIN
      SELECT usr_code, usr_email
        INTO v_usercode, v_emailaddress
        FROM tqc_users
       WHERE UPPER (usr_username) = v_entered_user;

      IF v_emailaddress IS NULL
      THEN
         raise_error (
            'No Email Address Defined For User.Please Contact Administrator');
      END IF;

      UPDATE tqc_users
         SET usr_pwd_reset_code = v_reset_code, usr_pwd_reset = 'Y'
       WHERE usr_code = v_usercode;

      RETURN v_emailaddress;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         raise_error (
            'Invalid username . Enter valid username and try again.');
   END recoverpassword;

   FUNCTION recoverclientpassword (v_entered_user   IN VARCHAR2,
                                   v_reset_code     IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_usercode       NUMBER;
      v_emailaddress   VARCHAR2 (100);
   BEGIN
      SELECT acwa_code, acwa_email_addrs
        INTO v_usercode, v_emailaddress
        FROM tqc_client_web_accounts
       WHERE UPPER (acwa_username) = v_entered_user;

      IF v_emailaddress IS NULL
      THEN
         raise_error (
            'No Email Address Defined For User.Please Contact Administrator');
      END IF;

      UPDATE tqc_client_web_accounts
         SET acwa_reset_code = v_reset_code, acwa_pwd_reset = 'Y'
       WHERE acwa_code = v_usercode;

      RETURN v_emailaddress;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         raise_error (
            'Invalid username . Enter valid username and try again.');
   END recoverclientpassword;

   --   FUNCTION createwebclient (
   --      v_username        IN       VARCHAR2,
   --      v_password        IN       VARCHAR2,
   --      v_msg             OUT      VARCHAR2,
   --      v_lastlogindate   OUT      DATE,
   --      v_clientcode      OUT      NUMBER,
   --      v_clientname      OUT      VARCHAR2,
   --      v_accountcode     OUT      NUMBER
   --   )
   --      RETURN NUMBER
   --   IS
   --      al_id                NUMBER;
   --      v_us_id              VARCHAR2 (30);
   --      v_status             VARCHAR2 (10);
   --      v_dummy              RAW (50);
   --      v_sessions           NUMBER         := 0;
   --      v_mod_txt            VARCHAR2 (48);
   --      v_act_txt            VARCHAR2 (48);
   --      v_sid                NUMBER;
   --      v_serial             NUMBER;
   --      v_killed_sid         NUMBER;
   --      vautofix             VARCHAR2 (3);
   --      v_syspwddrtntime     NUMBER;
   --      v_pwd_changed_dt     DATE;
   --      v_pwd_date_created   DATE;
   --      v_per_rnk            VARCHAR2 (150);
   --      v_syspwdprmttime     VARCHAR2 (4);
   --      v_loginattempts      NUMBER         := 1;
   --      v_pwdreset           VARCHAR2 (2);
   --      -- v_last_login_date DATE;
   --      v_pwd                VARCHAR2 (100);
   --      --v_msg VARCHAR2(150);
   --      plenght              NUMBER;
   --      capscnt              NUMBER;
   --      lwrcnt               NUMBER;
   --      numcnt               NUMBER;
   --      speccnt              NUMBER;
   --      v_sysupppwlength     NUMBER;
   --      v_syslwrpwlength     NUMBER;
   --      v_sysnumpwlength     NUMBER;
   --      v_sysspecpwlength    NUMBER;
   --      v_syspwdlength       NUMBER;
   --      v_check_failed       BOOLEAN;
   --      v_syspwdattempts     NUMBER;
   --      v_syspwdoldusecnt    NUMBER;
   --      v_mandatory          BOOLEAN;
   --      v_cnt                NUMBER         := 0;
   --      x                    NUMBER         := 0;
   --      nusercode            NUMBER;
   --      v_reset              VARCHAR2 (100);
   --   BEGIN
   --      BEGIN
   --         v_syspwdattempts :=
   --                tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLOGATTEMPTS');
   --         v_syspwdoldusecnt :=
   --                  tqc_parameters_pkg.get_param_number ('SYSCLNTPWDOLDUSECNT');
   --      EXCEPTION
   --         WHEN OTHERS
   --         THEN
   --            v_msg := 'Error getting parameters...';
   --            RETURN (3);
   --      END;

   --      BEGIN
   --         plenght := LENGTH (v_password);

   --         FOR x IN 1 .. plenght
   --         LOOP
   --            IF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 65 AND 90
   --            THEN
   --               capscnt := NVL (capscnt, 0) + 1;
   --            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 97 AND 122
   --            THEN
   --               lwrcnt := NVL (lwrcnt, 0) + 1;
   --            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 49 AND 57
   --            THEN
   --               numcnt := NVL (numcnt, 0) + 1;
   --            ELSIF ASCII (SUBSTR (v_password, x, 1)) IN
   --                                                     (35, 36, 37, 38, 42, 64)
   --            THEN
   --               speccnt := NVL (numcnt, 0) + 1;
   --            END IF;
   --         END LOOP;
   --      END;

   --      v_syspwddrtntime :=
   --                    tqc_parameters_pkg.get_param_number ('SYSCLNTPWDDRTNTIME');
   --      v_syspwdprmttime :=
   --                    tqc_parameters_pkg.get_param_number ('SYSCLNTPWDPRMTTIME');
   --      v_syspwdlength :=
   --                      tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLENGTH');
   --      v_sysupppwlength :=
   --                    tqc_parameters_pkg.get_param_number ('SYSCLNTPWDUPPENGTH');
   --      v_syslwrpwlength :=
   --                   tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLWRLENGTH');
   --      v_sysnumpwlength :=
   --                   tqc_parameters_pkg.get_param_number ('SYSCLNTPWDNUMLENGTH');
   --      v_sysspecpwlength :=
   --                  tqc_parameters_pkg.get_param_number ('SYSCLNTPWDSPECLENGTH');
   --      v_check_failed := FALSE;
   --      v_mandatory := TRUE;

   --      IF UPPER (v_username) = UPPER (v_password)
   --      THEN
   --         v_msg :=
   --                'Your password is same as the username. This is not allowed.';
   --         v_check_failed := TRUE;
   --      ELSIF     NVL (plenght, 0) < NVL (v_syspwdlength, 0)
   --            AND NVL (v_syspwdlength, 0) > 0
   --      THEN
   --         v_msg :=
   --               'Your password must be a minimum of '
   --            || v_syspwdlength
   --            || ' alpha or numeric characters ';
   --         v_check_failed := TRUE;
   --      ELSIF     NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
   --            AND NVL (v_sysupppwlength, 0) > 0
   --      THEN
   --         v_msg :=
   --               'Your password should contain a minimum of '
   --            || v_sysupppwlength
   --            || ' upper case characters ';
   --         v_check_failed := TRUE;
   --      ELSIF     NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
   --            AND NVL (v_syslwrpwlength, 0) > 0
   --      THEN
   --         v_msg :=
   --               'Your password should contain a minimum of '
   --            || v_syslwrpwlength
   --            || ' lower case characters ';
   --         v_check_failed := TRUE;
   --      ELSIF     NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
   --            AND NVL (v_sysnumpwlength, 0) > 0
   --      THEN
   --         v_msg :=
   --               'Your password should contain a minimum of '
   --            || v_sysnumpwlength
   --            || ' numeric characters ';
   --         v_check_failed := TRUE;
   --      ELSIF     NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
   --            AND NVL (v_sysspecpwlength, 0) > 0
   --      THEN
   --         v_msg :=
   --               'Your password should contain a minimum of '
   --            || v_sysspecpwlength
   --            || ' special characters ';
   --         v_check_failed := TRUE;
   --         v_mandatory := FALSE;
   --      END IF;

   --      IF v_check_failed AND NOT v_mandatory
   --      THEN
   --         --v_msg := v_msg || ' Do you want to change it now? ';
   --         RETURN (4);
   --      ELSIF v_check_failed AND v_mandatory
   --      THEN
   --         v_msg := v_msg || ' Login not allowed ';
   --         RETURN (2);
   --      END IF;

   --      v_pwdreset := 'N';
   --      v_pwd_changed_dt := TRUNC (SYSDATE);
   --      v_loginattempts := 0;
   --      v_lastlogindate := TRUNC (SYSDATE);

   --      BEGIN
   --         SELECT DISTINCT clnt_code,
   --                         TRIM (clnt_name) || '' || TRIM (clnt_other_names)
   --                    INTO v_clientcode,
   --                         v_clientname
   --                    FROM tqc_clients
   --                   WHERE clnt_email_addrs = v_username AND ROWNUM <= 1;

   --         SELECT COUNT (*)
   --           INTO v_cnt
   --           FROM tqc_client_web_accounts
   --          WHERE acwa_clnt_code = v_clientcode;
   --      EXCEPTION
   --         WHEN NO_DATA_FOUND
   --         THEN
   --            NULL;
   --      END;

   --      IF NVL (v_cnt, 0) > 0
   --      THEN
   --         v_msg :=
   --               v_msg
   --            || ' Account Already Exists For Client. Have you forgotten your Password? ';
   --         RETURN (6);
   --      ELSE
   --         v_clientname := v_username;
   --      END IF;

   --      SELECT tqc_acwa_code_seq.NEXTVAL
   --        INTO v_accountcode
   --        FROM DUAL;

   --      INSERT INTO tqc_client_web_accounts
   --                  (acwa_code, acwa_username, acwa_pwd, acwa_login_allowed,
   --                   acwa_pwd_changed, acwa_pwd_reset, acwa_login_attempts,
   --                   acwa_personel_rank, acwa_dt_created, acwa_status,
   --                   acwa_last_login_date, acwa_clnt_code, acwa_created_by,
   --                   acwa_name, acwa_email_addrs, acwa_reset_code
   --                  )
   --           VALUES (v_accountcode, v_username, gis_read.val (v_password), 'Y',
   --                   v_pwd_changed_dt, v_pwdreset, v_loginattempts,
   --                   NULL, TRUNC (SYSDATE), 'A',
   --                   v_lastlogindate, v_clientcode, 'SYSTEM',
   --                   v_username, v_username, NULL
   --                  );

   --      INSERT INTO tqc_client_web_pwd_hist
   --                  (cwwph_code, cwwph_acwa_code, cwwph_acwa_username,
   --                   cwwph_pwd, cwwph_pwd_change_dt
   --                  )
   --           VALUES (tqc_cwwph_code_seq.NEXTVAL, v_accountcode, v_username,
   --                   gis_read.val (v_password), SYSDATE
   --                  );

   --      v_msg := v_msg || ' Login Successfull';
   --      RETURN (1);
   --   EXCEPTION
   --      WHEN OTHERS
   --      THEN
   --         raise_error ('ERROR OCCURED' || SQLERRM (SQLCODE));
   --         RETURN (3);
   --   END createwebclient;

   FUNCTION createWebClient (v_username        IN     VARCHAR2,
                             v_password        IN     VARCHAR2,
                             v_msg                OUT VARCHAR2,
                             v_lastlogindate      OUT DATE,
                             v_clientCode         OUT NUMBER,
                             v_clientName         OUT VARCHAR2,
                             v_accountCode        OUT NUMBER,
                             v_clientShtDesc      OUT VARCHAR2,
                             v_fullName        IN     VARCHAR2 DEFAULT NULL,
                             v_mobileNumber    IN     VARCHAR2 DEFAULT NULL,
                             v_resetPassword   IN     VARCHAR2 DEFAULT NULL)
      RETURN NUMBER
   IS
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      -- v_last_login_date DATE;
      v_pwd                VARCHAR2 (100);
      --v_msg VARCHAR2(150);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      x                    NUMBER := 0;
      nusercode            NUMBER;
      v_reset              VARCHAR2 (100);
      v_firstname          VARCHAR2 (50);
      v_secondname         VARCHAR2 (50);
      vclntcode            NUMBER;
      v_shortcode          VARCHAR2 (50);
   BEGIN
      BEGIN
         v_syspwdattempts :=
            tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLOGATTEMPTS');
         v_syspwdoldusecnt :=
            tqc_parameters_pkg.get_param_number ('SYSCLNTPWDOLDUSECNT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Error getting parameters...';
            RETURN (3);
      END;


      BEGIN
         plenght := LENGTH (v_password);

         FOR x IN 1 .. plenght
         LOOP
            IF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 65 AND 90
            THEN
               capscnt := NVL (capscnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 97 AND 122
            THEN
               lwrcnt := NVL (lwrcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 49 AND 57
            THEN
               numcnt := NVL (numcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) IN
                     (35, 36, 37, 38, 42, 64)
            THEN
               speccnt := NVL (numcnt, 0) + 1;
            END IF;
         END LOOP;
      END;

      v_syspwddrtntime :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDDRTNTIME');
      v_syspwdprmttime :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDPRMTTIME');
      v_syspwdlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLENGTH');
      v_sysupppwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDUPPENGTH');
      v_syslwrpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLWRLENGTH');
      v_sysnumpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDNUMLENGTH');
      v_sysspecpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDSPECLENGTH');
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      IF UPPER (v_username) = UPPER (v_password)
      THEN
         v_msg :=
            'Your password is same as the username. This is not allowed.';
         v_check_failed := TRUE;
      ELSIF NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';
         v_check_failed := TRUE;
      ELSIF NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysupppwlength
            || ' upper case characters ';
         v_check_failed := TRUE;
      ELSIF NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';
         v_check_failed := TRUE;
      ELSIF NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysnumpwlength
            || ' numeric characters ';
         v_check_failed := TRUE;
      ELSIF NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
            AND NVL (v_sysspecpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysspecpwlength
            || ' special characters ';
         v_check_failed := TRUE;
         v_mandatory := FALSE;
      END IF;

      IF v_check_failed AND NOT v_mandatory
      THEN
         --v_msg := v_msg || ' Do you want to change it now? ';
         RETURN (4);
      ELSIF v_check_failed AND v_mandatory
      THEN
         v_msg := v_msg || ' Login not allowed ';
         RETURN (2);
      END IF;

      v_pwdreset := 'N';
      v_pwd_changed_dt := TRUNC (SYSDATE);
      v_loginattempts := 0;
      v_lastlogindate := TRUNC (SYSDATE);

      BEGIN
         SELECT DISTINCT
                CLNT_CODE,
                TRIM (CLNT_NAME) || '' || TRIM (CLNT_OTHER_NAMES),
                CLNT_SHT_DESC
           INTO v_clientCode, v_clientName, v_clientShtDesc
           FROM TQC_CLIENTS
          WHERE CLNT_EMAIL_ADDRS = v_username AND ROWNUM <= 1;

         SELECT COUNT (*)
           INTO v_cnt
           FROM tqc_client_web_accounts
          WHERE acwa_clnt_code = v_clientCode;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            BEGIN
               v_firstname := SUBSTR (v_fullName, 0, INSTR (v_fullName, ' '));
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_firstname := v_fullName;
            END;

            BEGIN
               v_secondname :=
                  SUBSTR (v_fullName,
                          LENGTH (v_firstname),
                          INSTR (v_fullName, ' ', -1));     --LENGTH(v_name));
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_secondname := v_fullName;
            END;

            SELECT TO_NUMBER (TO_CHAR (SYSDATE, 'YY'))
                   || tqc_clnt_code_seq.NEXTVAL
              INTO vclntcode
              FROM DUAL;

            --RAISE_ERROR(' v_firstname '||v_firstname||' v_secondname '||v_secondname||' v_fullName '||v_fullName);

            v_shortcode :=
               REPLACE (
                  tqc_clients_pkg.get_client_sht_desc (v_firstname,
                                                       v_secondname,
                                                       v_fullName),
                  ' ',
                  '');

            INSERT INTO tqc_clients (clnt_code,
                                     clnt_name,
                                     clnt_other_names,
                                     clnt_sht_desc,
                                     clnt_email_addrs,
                                     clnt_wef,
                                     clnt_created_by,
                                     clnt_date_created,
                                     clnt_tel)
                 VALUES (vclntcode,
                         v_firstname,
                         v_secondname,
                         v_shortcode,
                         v_username,
                         SYSDATE,
                         'SYSTEM',
                         SYSDATE,
                         v_mobileNumber);

            INSERT INTO TQC_CLIENT_SYSTEMS (CSYS_CODE,
                                            CSYS_CLNT_CODE,
                                            CSYS_SYS_CODE,
                                            CSYS_WEF)
                 VALUES (TQC_CSYS_CODE_SEQ.NEXTVAL,
                         vclntcode,
                         37,
                         SYSDATE);

            BEGIN
               SELECT DISTINCT
                      CLNT_CODE,
                      TRIM (CLNT_NAME) || '' || TRIM (CLNT_OTHER_NAMES),
                      CLNT_SHT_DESC
                 INTO v_clientCode, v_clientName, v_clientShtDesc
                 FROM TQC_CLIENTS
                WHERE CLNT_EMAIL_ADDRS = v_username AND ROWNUM <= 1;
            EXCEPTION
               WHEN OTHERS
               THEN
                  NULL;
            END;
      END;



      IF NVL (v_cnt, 0) > 0
      THEN
         v_msg :=
            v_msg
            || ' Account Already Exists For Client. Have you forgotten your Password? ';
         RETURN (6);
      ELSE
         v_clientName := v_username;
      END IF;

      --      RAISE_ERROR('v_mobileNumber: ' || v_mobileNumber || ' v_fullName: ' || v_fullName || ' v_password: ' || v_password);

      IF NVL (v_resetPassword, 'N') = 'Y'
      THEN
         v_pwdreset := 'Y';
      END IF;

      SELECT tqc_acwa_code_seq.NEXTVAL INTO v_accountCode FROM DUAL;

      INSERT INTO tqc_client_web_accounts (acwa_code,
                                           acwa_username,
                                           acwa_pwd,
                                           acwa_login_allowed,
                                           acwa_pwd_changed,
                                           acwa_pwd_reset,
                                           acwa_login_attempts,
                                           acwa_personel_rank,
                                           acwa_dt_created,
                                           acwa_status,
                                           acwa_last_login_date,
                                           acwa_clnt_code,
                                           acwa_created_by,
                                           acwa_name,
                                           acwa_email_addrs,
                                           acwa_reset_code,
                                           acwa_mobile_number)
           VALUES (v_accountCode,
                   v_username,
                   gis_read.val (v_password),
                   'Y',
                   v_pwd_changed_dt,
                   v_pwdreset,
                   v_loginattempts,
                   NULL,
                   TRUNC (SYSDATE),
                   'A',
                   v_lastlogindate,
                   v_clientCode,
                   'SYSTEM',
                   NVL (v_fullName, v_username),
                   v_username,
                   NULL,
                   v_mobileNumber);

      INSERT INTO tqc_client_web_pwd_hist (cwwph_code,
                                           cwwph_acwa_code,
                                           cwwph_acwa_username,
                                           cwwph_pwd,
                                           cwwph_pwd_change_dt)
           VALUES (tqc_cwwph_code_seq.NEXTVAL,
                   v_accountCode,
                   v_username,
                   gis_read.val (v_password),
                   SYSDATE);

      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE_ERROR ('ERROR OCCURED' || SQLERRM (SQLCODE));
         RETURN (3);
   END createWebClient;

   PROCEDURE defaultcountry (v_defaultcountry_ref   OUT defaultcountry_ref,
                             v_org_code                 NUMBER)
   IS
   BEGIN
      OPEN v_defaultcountry_ref FOR
         SELECT DISTINCT cou_code, cou_name
           FROM tqc_organizations, tqc_countries
          WHERE org_code = 1 AND org_cou_code = cou_code;
   END defaultcountry;

   FUNCTION ldap_authenticate (vusername           IN     VARCHAR2,
                               v_msg                  OUT VARCHAR2,
                               v_last_login_date      OUT DATE,
                               usercode               OUT NUMBER,
                               username               OUT VARCHAR2,
                               v_sys_code          IN     NUMBER)
      RETURN INTEGER
   IS
      voutput              INTEGER;
      vuser                VARCHAR2 (35)
         := pkg_global_vars.get_pvarchar2 ('PKG_GLOBAL_VARS.pvg_username');
      vpwd                 VARCHAR2 (10);
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      -- v_last_login_date DATE;
      v_pwd                VARCHAR2 (100);
      --v_msg VARCHAR2(150);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      x                    NUMBER := 0;
      nusercode            NUMBER;

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
           SELECT usr_pwd, usr_pwd_change_dt
             FROM tqc_users_passwd_hist
            WHERE UPPER (usrph_usr_code) = vuser
         ORDER BY usr_pwd_change_dt DESC;
   BEGIN
      --RAISE_ERROR(v_entered_user||';'||v_sys_code);
      BEGIN
         SELECT usr_code,
                UPPER (usr_username),
                UPPER (usr_status),
                usr_pwd,
                usr_pwd_changed,
                usr_dt_created,
                usr_personel_rank,
                usr_pwd_reset,
                usr_login_attempts,
                NVL (usr_last_date, TRUNC (SYSDATE))
           INTO usercode,
                username,
                v_status,
                v_pwd,
                v_pwd_changed_dt,
                v_pwd_date_created,
                v_per_rnk,
                v_pwdreset,
                v_loginattempts,
                v_last_login_date
           FROM tqc_users
          WHERE UPPER (usr_username) = UPPER (vusername);
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Unable to verify the username and password.';
            RETURN (3);
      END;

      IF v_status = 'I'
      THEN
         v_msg :=
            'The account with the given username is deactivated. Please contact the Administrator.';
         RETURN (3);
      END IF;

      UPDATE tqc_users
         SET usr_last_date = TRUNC (SYSDATE)
       WHERE UPPER (usr_username) = vusername;

      user_system_conn (vusername, v_sys_code, 'N');
      COMMIT;
      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   END;

   FUNCTION checksecurityquestion (v_usr_username            IN VARCHAR2,
                                   v_usr_security_question   IN VARCHAR2,
                                   v_usr_security_answer     IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_security_question   VARCHAR2 (200);
      v_security_answer     VARCHAR2 (200);
   BEGIN
      --RAISE_ERROR('v_usr_security_question'||v_usr_security_question||'v_usr_security_answer'||v_usr_security_answer||'v_usr_username'||v_usr_username);
      SELECT usr_secq_code, usr_security_answer
        INTO v_security_question, v_security_answer
        FROM tqc_users
       WHERE usr_username = v_usr_username;

      IF v_usr_security_question = v_security_question
         AND v_usr_security_answer = v_security_answer
      THEN
         RETURN 'Y';
      ELSE
         RETURN 'N';
      END IF;
   END;

   FUNCTION client_authenticate (vusername         IN     VARCHAR2,
                                 vpwd              IN     VARCHAR2,
                                 v_pwd_reentry     IN     VARCHAR2,
                                 v_new_pwd1        IN     VARCHAR2,
                                 v_new_pwd2        IN     VARCHAR2,
                                 v_resetCode       IN     VARCHAR2,
                                 v_msg                OUT VARCHAR2,
                                 v_lastlogindate      OUT DATE,
                                 v_clientCode         OUT NUMBER,
                                 v_clientName         OUT VARCHAR2,
                                 v_accountCode        OUT NUMBER,
                                 v_prpCode            OUT NUMBER,
                                 v_clientType         OUT VARCHAR2,
                                 v_defaultBranch      OUT NUMBER)
      RETURN INTEGER
   IS
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER := 0;
      v_mod_txt            VARCHAR2 (48);
      v_act_txt            VARCHAR2 (48);
      v_sid                NUMBER;
      v_serial             NUMBER;
      v_killed_sid         NUMBER;
      vautofix             VARCHAR2 (3);
      v_syspwddrtntime     NUMBER;
      v_pwd_changed_dt     DATE;
      v_pwd_date_created   DATE;
      v_per_rnk            VARCHAR2 (150);
      v_syspwdprmttime     VARCHAR2 (4);
      v_loginattempts      NUMBER := 1;
      v_pwdreset           VARCHAR2 (2);
      -- v_last_login_date DATE;
      v_pwd                VARCHAR2 (100);
      --v_msg VARCHAR2(150);
      plenght              NUMBER;
      capscnt              NUMBER;
      lwrcnt               NUMBER;
      numcnt               NUMBER;
      speccnt              NUMBER;
      v_sysupppwlength     NUMBER;
      v_syslwrpwlength     NUMBER;
      v_sysnumpwlength     NUMBER;
      v_sysspecpwlength    NUMBER;
      v_syspwdlength       NUMBER;
      v_check_failed       BOOLEAN;
      v_syspwdattempts     NUMBER;
      v_syspwdoldusecnt    NUMBER;
      v_mandatory          BOOLEAN;
      v_cnt                NUMBER := 0;
      x                    NUMBER := 0;
      nusercode            NUMBER;
      v_reset              VARCHAR2 (100);

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
           SELECT cwwph_pwd, cwwph_pwd_change_dt
             FROM tqc_client_web_pwd_hist
            WHERE UPPER (cwwph_acwa_code) = vuser
         ORDER BY cwwph_pwd_change_dt DESC;
   BEGIN
      --MSG_BOX('DANI');
      --  RAISE_ERROR(v_entered_user);
      BEGIN
         SELECT acwa_code,
                UPPER (acwa_username),
                UPPER (acwa_status),
                acwa_pwd,
                acwa_pwd_changed,
                acwa_dt_created,
                acwa_personel_rank,
                acwa_pwd_reset,
                acwa_login_attempts,
                NVL (acwa_last_login_date, TRUNC (SYSDATE)),
                clnt_code,
                LTRIM (RTRIM (clnt_surname || ' ' || clnt_other_names)),
                acwa_code,
                acwa_reset_code,
                prp_code,
                acwa_type,
                tcub_tcb_code
           INTO nusercode,
                v_us_id,
                v_status,
                v_pwd,
                v_pwd_changed_dt,
                v_pwd_date_created,
                v_per_rnk,
                v_pwdreset,
                v_loginattempts,
                v_lastlogindate,
                v_clientCode,
                v_clientname,
                v_accountCode,
                v_reset,
                v_prpCode,
                v_clientType,
                v_defaultBranch
           FROM tqc_client_web_accounts,
                tqc_clients,
                lms_proposers,
                tqc_client_usr_branches
          WHERE     clnt_code(+) = acwa_clnt_code
                AND clnt_code = prp_clnt_code(+)
                AND ACWA_CODE = TCUB_ACWA_CODE(+)
                AND (tcub_default = 'y' OR tcub_acwa_code IS NULL)
                AND acwa_username = vusername;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_msg :=
               'The user is not a valid TurnQuest user. Enter valid username ';
            RETURN (3);
         WHEN OTHERS
         THEN
            v_msg := 'Unable to verify the username and password.';
            RETURN (3);
      END;

      BEGIN
         v_syspwdattempts :=
            tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLOGATTEMPTS');
         v_syspwdoldusecnt :=
            tqc_parameters_pkg.get_param_number ('SYSCLNTPWDOLDUSECNT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Error getting parameters...';
            RETURN (3);
      END;

      IF v_reset <> v_resetCode
      THEN
         v_msg := 'Reset Code Incorrect. Please Check.';
         RETURN (4);
      END IF;

      IF NVL (v_syspwdattempts, 0) > 0
         AND NVL (v_loginattempts, 0) >= NVL (v_syspwdattempts, 0)
      THEN
         v_msg :=
            'Max of ' || NVL (v_syspwdattempts, 0)
            || ' login attempt failure attained. Contact System Administrator for assistance.';
         RETURN (3);
      ELSIF NVL (v_pwd, '******') != gis_read.val (vpwd)
            AND NOT (NVL (v_pwdreset, 'N') = 'Y' OR v_pwd_reentry = 'Y')
      THEN
         update_login_attempt (vusername);
         v_msg :=
            'Invalid username or password. Enter valid username and password and try again.';
         RETURN (2);
      END IF;

      IF (NVL (v_pwdreset, 'N') = 'Y' OR v_pwd_reentry = 'Y')
         AND (v_new_pwd1 IS NOT NULL OR v_new_pwd2 IS NOT NULL)
      THEN
         IF NVL (v_new_pwd1, '****') != NVL (v_new_pwd2, '%%%%')
         THEN
            v_msg :=
               'Your password has been re-entered incorrectly. Please try again! ';
            RETURN (3);
         ELSE
            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt
                 FROM tqc_client_web_pwd_hist
                WHERE cwwph_acwa_username = vusername
                      AND cwwph_pwd = gis_read.val (v_new_pwd1);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Cannot verify if password ever used before..';
                  RETURN (3);
            END;

            IF NVL (v_cnt, 0) > NVL (v_syspwdoldusecnt, 0)
            THEN
               v_msg :=
                  'Cannot use a password more than '
                  || NVL (v_syspwdoldusecnt, 0)
                  || ' time(s). Please specify a new password never user before.. ';
               RETURN (3);
            END IF;

            BEGIN
               x := 0;

               FOR usrs IN usrpwds (vusername)
               LOOP
                  IF usrs.cwwph_pwd = gis_read.val (v_new_pwd1)
                  THEN
                     v_msg :=
                        'Cannot Reuse the last two passwords. Please specify a new password never user before.. ';
                     RETURN (3);
                  END IF;

                  x := x + 1;

                  IF x >= 2
                  THEN
                     EXIT;
                  END IF;
               END LOOP;
            END;

            --ELSE
            BEGIN
               UPDATE tqc_client_web_accounts
                  SET acwa_pwd_reset = 'N',
                      acwa_pwd_changed = TRUNC (SYSDATE),
                      acwa_pwd = gis_read.val (v_new_pwd1),
                      acwa_login_attempts = 0
                WHERE UPPER (acwa_username) = vusername;
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'User password could not be changed...';
                  RETURN (3);
            END;

            BEGIN
               INSERT INTO tqc_client_web_pwd_hist (cwwph_code,
                                                    cwwph_acwa_code,
                                                    cwwph_acwa_username,
                                                    cwwph_pwd,
                                                    cwwph_pwd_change_dt)
                    VALUES (tqc_cwwph_code_seq.NEXTVAL,
                            nusercode,
                            vusername,
                            gis_read.val (v_new_pwd1),
                            SYSDATE);
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_msg := 'Error updating password change history..';
                  RETURN (3);
            END;

            v_pwdreset := 'N';
            v_pwd_changed_dt := TRUNC (SYSDATE);
            v_loginattempts := 0;
         END IF;
      END IF;


      BEGIN
         plenght := LENGTH (vpwd);

         FOR x IN 1 .. plenght
         LOOP
            IF ASCII (SUBSTR (vpwd, x, 1)) BETWEEN 65 AND 90
            THEN
               capscnt := NVL (capscnt, 0) + 1;
            ELSIF ASCII (SUBSTR (vpwd, x, 1)) BETWEEN 97 AND 122
            THEN
               lwrcnt := NVL (lwrcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (vpwd, x, 1)) BETWEEN 49 AND 57
            THEN
               numcnt := NVL (numcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (vpwd, x, 1)) IN (35, 36, 37, 38, 42, 64)
            THEN
               speccnt := NVL (numcnt, 0) + 1;
            END IF;
         END LOOP;
      END;

      v_syspwddrtntime :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDDRTNTIME');
      v_syspwdprmttime :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDPRMTTIME');
      v_syspwdlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLENGTH');
      v_sysupppwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDUPPENGTH');
      v_syslwrpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDLWRLENGTH');
      v_sysnumpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDNUMLENGTH');
      v_sysspecpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSCLNTPWDSPECLENGTH');
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      IF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
            (TO_NUMBER (v_syspwddrtntime))
      THEN
         v_msg :=
            'Your password has expired. Please specify new password to continue.';
         v_check_failed := TRUE;
      ELSIF NVL (v_pwdreset, 'N') = 'Y' AND v_reset IS NOT NULL
      THEN
         v_msg := 'Your password has been reset. MUST specify a new password.';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF UPPER (vusername) = UPPER (vpwd)
      THEN
         v_msg :=
            'Your password is same as the username. This is not allowed.';
         v_check_failed := TRUE;
      ELSIF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
               (TO_NUMBER (v_syspwddrtntime) - TO_NUMBER (v_syspwdprmttime))
      THEN
         v_msg :=
            'Your password is due to expire in '
            || ( (TO_NUMBER (v_syspwddrtntime) * 1440)
                - (TO_NUMBER (v_syspwdprmttime) * 1440))
               / 1440
            || ' days. ';
         v_check_failed := TRUE;
         v_mandatory := FALSE;
      ELSIF NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';
         v_check_failed := TRUE;
      ELSIF NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysupppwlength
            || ' upper case characters ';
         v_check_failed := TRUE;
      ELSIF NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';
         v_check_failed := TRUE;
      ELSIF NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysnumpwlength
            || ' numeric characters ';
         v_check_failed := TRUE;
      ELSIF NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
            AND NVL (v_sysspecpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysspecpwlength
            || ' special characters ';
         v_check_failed := TRUE;
         v_mandatory := FALSE;
      END IF;

      IF v_check_failed AND NOT v_mandatory
      THEN
         --v_msg := v_msg || ' Do you want to change it now? ';
         RETURN (4);
      ELSIF v_check_failed AND v_mandatory
      THEN
         v_msg := v_msg || ' Login not allowed ';
         RETURN (2);
      END IF;

      UPDATE tqc_client_web_accounts
         SET acwa_last_login_date = TRUNC (SYSDATE)
       WHERE acwa_username = vusername;

      COMMIT;
      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   END;

   FUNCTION getuseremail (v_usrcode NUMBER)
      RETURN user_emails_ref
   IS
      v_cursor     user_emails_ref;
      v_usertype   VARCHAR2 (1);
   BEGIN
      --     raise_error('We is here: ' || v_usrcode);


      SELECT usr_type
        INTO v_usertype
        FROM tqc_users
       WHERE usr_code = v_usrcode;


      --     raise_error('We is here: ' || v_usrcode);


      IF NVL (v_usertype, 'U') = 'U'
      THEN
         OPEN v_cursor FOR
            SELECT usr_email
              FROM tqc_users
             WHERE usr_code = v_usrcode;

         RETURN v_cursor;
      ELSE
         OPEN v_cursor FOR
            SELECT usr_email
              FROM tqc_group_users, tqc_users
             WHERE gusr_grp_usr_code = v_usrcode AND usr_code = gusr_usr_code;

         RETURN v_cursor;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (
               'ERROR FINDING USER EMAIL'
            || v_usertype
            || ' '
            || SQLERRM (SQLCODE));
   END;

   PROCEDURE resetAgentsPassword (v_accc_code   IN     NUMBER,
                                  v_accc_pwd    IN     VARCHAR2,
                                  v_error_msg      OUT VARCHAR2,
                                  v_old_pwd     IN     VARCHAR2 DEFAULT NULL,
                                  v_resetCode   IN     VARCHAR2 DEFAULT NULL)
   IS
      v_password          VARCHAR2 (300);
      v_sysupppwlength    NUMBER;
      v_syslwrpwlength    NUMBER;
      v_sysnumpwlength    NUMBER;
      v_sysspecpwlength   NUMBER;
      v_syspwdlength      NUMBER;
      v_check_failed      BOOLEAN;
      v_syspwdattempts    NUMBER;
      v_syspwdoldusecnt   NUMBER;
      v_mandatory         BOOLEAN;
      plenght             NUMBER;
      v_cnt               NUMBER := 0;
      capscnt             NUMBER;
      lwrcnt              NUMBER;
      numcnt              NUMBER;
      speccnt             NUMBER;
      v_syspwddrtntime    NUMBER;
      v_pwd_changed_dt    DATE;
      v_syspwdprmttime    VARCHAR2 (4);
      v_msg               VARCHAR2 (200);
      v_usercount         NUMBER := 0;
      v_passRestVal       VARCHAR2 (100);
   BEGIN
      IF LENGTH (v_accc_pwd) <= 15
      THEN
         v_password := gis_read.val (v_accc_pwd);
      ELSE
         v_error_msg :=
            ' Password may only be upt to a maximum of 15 characters';
         RETURN;
      END IF;

      IF v_old_pwd IS NOT NULL
      THEN
         BEGIN
            SELECT COUNT (accc_code)
              INTO v_usercount
              FROM tqc_account_contacts
             WHERE accc_pwd = gis_read.val (v_old_pwd)
                   AND accc_code = v_accc_code;

            IF v_usercount = 0
            THEN
               v_error_msg := ' Enter the current correct password';
               RETURN;
            END IF;
         END;
      END IF;

      BEGIN
         SELECT ACCC_RESET_CODE
           INTO v_passRestVal
           FROM tqc_account_contacts
          WHERE accc_code = v_accc_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      IF NVL (v_passRestVal, 0) != NVL (v_resetCode, 0)
      THEN
         v_error_msg := 'Invalid Reset Code';
         RETURN;
      END IF;



      --=====================================================================================
      BEGIN
         IF v_accc_pwd IS NOT NULL
         THEN
            v_password := v_accc_pwd;
         ELSE
            v_password := v_accc_pwd;
         END IF;

         plenght := LENGTH (v_password);

         FOR x IN 1 .. plenght
         LOOP
            IF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 65 AND 90
            THEN
               capscnt := NVL (capscnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 97 AND 122
            THEN
               lwrcnt := NVL (lwrcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) BETWEEN 49 AND 57
            THEN
               numcnt := NVL (numcnt, 0) + 1;
            ELSIF ASCII (SUBSTR (v_password, x, 1)) IN
                     (35, 36, 37, 38, 42, 64)
            THEN
               speccnt := NVL (numcnt, 0) + 1;
            END IF;
         END LOOP;
      END;

      v_syspwddrtntime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDDRTNTIME');
      v_syspwdprmttime :=
         tqc_parameters_pkg.get_param_number ('SYSPWDPRMTTIME');
      v_syspwdlength := tqc_parameters_pkg.get_param_number ('SYSPWDLENGTH');
      v_sysupppwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDUPPENGTH');
      v_syslwrpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDLWRLENGTH');
      v_sysnumpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDNUMLENGTH');
      v_sysspecpwlength :=
         tqc_parameters_pkg.get_param_number ('SYSPWDSPECLENGTH');
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      IF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
            (TO_NUMBER (v_syspwddrtntime))
      THEN
         v_error_msg :=
            'Your password has expired. Please specify new password to continue.';
         RETURN;
      ELSIF NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_error_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';
         RETURN;
      ELSIF NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_error_msg :=
               'Your password should contain a minimum of '
            || v_sysupppwlength
            || ' upper case characters ';
         RETURN;
      ELSIF NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_error_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';
         RETURN;
      ELSIF NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_error_msg :=
               'Your password should contain a minimum of '
            || v_sysnumpwlength
            || ' numeric characters ';
         RETURN;
      ELSIF NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
            AND NVL (v_sysspecpwlength, 0) > 0
      THEN
         v_error_msg :=
               'Your password should contain a minimum of '
            || v_sysspecpwlength
            || ' special characters ';
         RETURN;
      END IF;

      --=====================================================================================
      v_error_msg := '0';

      UPDATE tqc_account_contacts
         SET accc_pwd = gis_read.val (v_accc_pwd),
             accc_pwd_reset = 'N',
             ACCC_PWD_CHANGED = SYSDATE
       WHERE accc_code = v_accc_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_error_msg := 'Error Reseting the Agency User Password';
   END;
END tqc_web_pkg;
/