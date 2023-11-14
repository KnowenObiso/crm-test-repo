--
-- CENTRAL_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.CENTRAL_PKG AS
/******************************************************************************
   NAME:       CENTRAL_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        1/13/2014             1. Created this package body.
******************************************************************************/

  PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

  FUNCTION getSystems RETURN systems_ref IS
    v_cursor systems_ref;
  BEGIN
    OPEN v_cursor FOR
    SELECT DISTINCT SYS_CODE,SYS_SHT_DESC,SYS_NAME
    FROM SYSTEMS_VIEW;
    RETURN v_cursor;
  END getSystems;
  
  PROCEDURE update_login_attempt (v_accc_code IN VARCHAR2)
   IS
   BEGIN
      --BEGIN
      UPDATE users_view
         SET usr_login_attempts = NVL (usr_login_attempts, 0) + 1
       WHERE UPPER (usr_username) = UPPER (v_accc_code);

   END;
   
   PROCEDURE user_system_conn (
      v_user                    VARCHAR2,
      v_sys_code           IN   NUMBER,
      v_killopen_session   IN   VARCHAR2 DEFAULT 'N',
      v_sysPath            OUT  VARCHAR2
   )
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
      v_couCode         NUMBER;

      --vParamList PARAMLIST;
      CURSOR cur_session (vuser VARCHAR2, vdbuser IN VARCHAR2)
      IS
         SELECT SID, serial# serial,
                SUBSTR (module, INSTR (module, ':') + 1) killed_sid
           FROM tqc_session_log
          WHERE SUBSTR (module, 1, INSTR (module, ':') - 1) = vuser
            AND username = vdbuser
            AND status != 'KILLED';
   BEGIN
       --v_user := UPPER(:TI_USERNAME);
      --RAISE_ERROR(v_sys_code||';'||v_user);
      BEGIN
         BEGIN
            SELECT   COUNT (1), usr_code, sys_cou_code,sys_path
                INTO v_cnt, v_user_code,v_couCode,v_sysPath
                FROM USER_SYSTEMS_VIEW, USERS_VIEW,SYSTEMS_VIEW
               WHERE usys_usr_code = usr_code
                 AND usys_sys_code = sys_code
                 AND usr_username = v_user
                 AND usys_sys_code = v_sys_code
            GROUP BY usr_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               RAISE_ERROR('Access denied to selected system ');
            WHEN OTHERS
            THEN
               RAISE_ERROR('Error getting user details');
         END;

         IF NVL (v_cnt, 0) = 0
         THEN
            tqc_error_manager.raise_error (10010);
         END IF;
      END;

      BEGIN
         pkg_global_vars.set_pvar ('pkg_global_vars.pvg_username', v_user);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error
                      (
                       'Unable to set the application user global variable..'
                      );
      END;

      --Set the session id global variable
      BEGIN
         SELECT TO_NUMBER (   TO_CHAR (SYSDATE - 365, 'Y')
                           || tqc_usl_session_code_seq.NEXTVAL
                          )
           INTO nsessionid
           FROM DUAL;

         -- raise_when_others('nSessionId='||nSessionId);
         pkg_global_vars.set_pvar ('pkg_global_vars.pvg_session_id',
                                   nsessionid
                                  );
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error
               (-20001,
                'Unable to log user logon audit. contact system administrator'
               );
      END;

      --=================ALLOW MULTIPLE SESSIONS================================
      BEGIN
         v_multisessions :=
                     get_param_varchar ('SYSMULTISESSION',v_couCode);
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
          WHERE SUBSTR (module, 1, INSTR (module, ':') - 1) = v_user
            AND username = vdbuser
            AND status != 'KILLED';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (
                                     'Error determining active sessions...'
                                    );
      END;

      IF NVL (v_sessions, 0) >= 1 AND v_multisessions = 'N'
      THEN
         IF NVL (v_killopen_session, 'N') = 'Y'
         THEN
            FOR cur_session_rec IN cur_session (v_user, vdbuser)
            LOOP
               BEGIN
                  --alter system  kill session ''''||cur_session_REC.sid||','||cur_session_REC.serial||'''');
                  UPDATE user_logon_view
                     SET usl_logout_dt = SYSDATE
                   WHERE usl_usr_code = v_user_code
                     AND usl_session_code = cur_session_rec.killed_sid;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_application_error
                           (-20001,
                               'Unable to kill the Previous session. Error: '
                            || SQLERRM
                            || ' Contact the system Administrator'
                           );
               END;
            END LOOP;
         ELSE
            raise_application_error
                 (-20001,
                     'You have '
                  || v_sessions
                  || ' sessions open. System cannot open more than ONE session.'
                 );
         END IF;
      ELSE
         BEGIN
            v_sessions := 0;

            SELECT COUNT ('x')
              INTO v_sessions
              FROM user_logon_view
             WHERE usl_logout_dt IS NULL
               AND usl_usr_code = v_user_code
               AND TRUNC (usl_logout_dt) = TRUNC (SYSDATE);

            IF NVL (v_sessions, 0) > 0
            THEN
               UPDATE user_logon_view
                  SET usl_logout_dt = (usl_logon_dt + (15 / 1440))
                WHERE usl_usr_code = v_user_code;
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_application_error (-20001,
                                        'Error updating session details...'
                                       );
         END;
      END IF;

      BEGIN
         INSERT INTO user_logon_view
                     (usl_session_code, usl_usr_code, usl_sys_code,
                      usl_logon_dt
                     )
              VALUES (nsessionid, v_user_code, v_sys_code,
                      SYSDATE
                     );

         BEGIN
            DBMS_APPLICATION_INFO.set_module (   UPPER (v_user)
                                              || ':'
                                              || nsessionid,
                                              'PINNACLE'
                                             );
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
                                     'Logon audit could not be created'
                                    );
      END;

      BEGIN
         UPDATE USERS_VIEW
            SET usr_login_attempts = 0,
                usr_last_date = SYSDATE
          WHERE UPPER (usr_username) = UPPER (v_user);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error (-20001,
                                     'User login details writing failure'
                                    );
      END;

      COMMIT;

      BEGIN
         pkg_global_vars.set_pvar ('pkg_global_vars.pvg_autofix', vautofix);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error
                                   (-20001,
                                    'Error setting autofix global variable..'
                                   );
      END;
   END;
  
  FUNCTION check_user_pwd (
      v_entered_user      IN       VARCHAR2,
      v_entered_pwd       IN       VARCHAR2,
      v_msg               OUT      VARCHAR2,
      v_last_login_date   OUT      DATE,
      usercode            OUT      NUMBER,
      username            OUT      VARCHAR2,
      v_new_pwd1          IN       VARCHAR2,
      v_new_pwd2          IN       VARCHAR2,
      v_new_pwd_entry     IN       VARCHAR2 DEFAULT 'N',
      v_sys_code          IN       NUMBER,
      v_sysPath           OUT      VARCHAR2
   )
      RETURN NUMBER
   IS
      al_id                NUMBER;
      v_us_id              VARCHAR2 (30);
      v_status             VARCHAR2 (10);
      v_dummy              RAW (50);
      v_sessions           NUMBER         := 0;
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
      v_loginattempts      NUMBER         := 1;
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
      v_cnt                NUMBER         := 0;
      x                    NUMBER         := 0;
      nusercode            NUMBER;
      v_couCode            NUMBER;

      CURSOR usrpwds (vuser IN VARCHAR2)
      IS
         SELECT   usr_pwd, usr_pwd_change_dt
             FROM user_pass_hist_view
            WHERE UPPER (usrph_usr_code) = vuser
         ORDER BY usr_pwd_change_dt DESC;
   BEGIN
      --RAISE_ERROR(v_entered_user||';'||v_sys_code);
      BEGIN
         SELECT DISTINCT usr_code, UPPER (usr_username), UPPER (usr_status), usr_pwd,
                usr_pwd_changed, usr_dt_created, usr_personel_rank,
                usr_pwd_reset, usr_login_attempts,
                NVL (usr_last_date, TRUNC (SYSDATE)),sys_cou_code,sys_path
           INTO usercode, username, v_status, v_pwd,
                v_pwd_changed_dt, v_pwd_date_created, v_per_rnk,
                v_pwdreset, v_loginattempts,
                v_last_login_date,v_couCode,v_sysPath
           FROM users_view,user_systems_view,systems_view
          WHERE UPPER (usr_username) = v_entered_user
          AND usr_code = usys_usr_code
          AND usys_sys_code = sys_code
          AND usr_cou_code = sys_cou_code
          AND sys_code =  v_sys_code;
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
                    get_param_number ('SYSPWDLOGATTEMPTS',v_couCode);
         v_syspwdoldusecnt :=
                      get_param_number ('SYSPWDOLDUSECNT',v_couCode);
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

      IF     NVL (v_syspwdattempts, 0) > 0
         AND NVL (v_loginattempts, 0) >= NVL (v_syspwdattempts, 0)
      THEN
         v_msg :=
               'Max of '
            || NVL (v_syspwdattempts, 0)
            || ' login attempt failure attained. Contact System Administrator for assistance.';
         RETURN (3);
      ELSIF     NVL (v_pwd, '******') != gis_read.val (v_entered_pwd)
            AND NOT (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
      THEN
         update_login_attempt (v_entered_user);
         v_msg :=
            'Invalid username or password. Enter valid username and password and try again.';
         RETURN (2);
      END IF;

      IF     (NVL (v_pwdreset, 'N') = 'Y' OR v_new_pwd_entry = 'Y')
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
                 FROM TQC_USERS_PASSWD_HIST
                WHERE UPPER (usrph_usr_username) = v_entered_user
                  AND usr_pwd = gis_read.val (v_new_pwd1);
            EXCEPTION
               WHEN NO_DATA_FOUND THEN
               v_cnt:=0;
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

            --ELSE
            BEGIN
               UPDATE TQC_USERS
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
               INSERT INTO TQC_USERS_PASSWD_HIST
                           (usrph_code, usrph_usr_code,
                            usrph_usr_username, usr_pwd,
                            usr_pwd_change_dt
                           )
                    VALUES (tqc_usrph_code_seq.NEXTVAL, nusercode,
                            v_entered_user, gis_read.val (v_new_pwd1),
                            SYSDATE
                           );
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
                        get_param_number ('SYSPWDDRTNTIME',v_couCode);
      v_syspwdprmttime :=
                        get_param_number ('SYSPWDPRMTTIME',v_couCode);
      v_syspwdlength := get_param_number ('SYSPWDLENGTH',v_couCode);
      v_sysupppwlength :=
                        get_param_number ('SYSPWDUPPENGTH',v_couCode);
      v_syslwrpwlength :=
                       get_param_number ('SYSPWDLWRLENGTH',v_couCode);
      v_sysnumpwlength :=
                       get_param_number ('SYSPWDNUMLENGTH',v_couCode);
      v_sysspecpwlength :=
                      get_param_number ('SYSPWDSPECLENGTH',v_couCode);
      v_check_failed := FALSE;
      v_mandatory := TRUE;

      IF (TRUNC (SYSDATE) - v_pwd_changed_dt) >=
                                               (TO_NUMBER (v_syspwddrtntime)
                                               )
      THEN
         v_msg :=
            'Your password has expired. Please specify new password to continue.';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF NVL (v_pwdreset, 'N') = 'Y'
      THEN
         v_msg :=
                 'Your password has been reset. MUST specify a new password.';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF UPPER (v_entered_user) = UPPER (v_entered_pwd)
      THEN
         v_msg :=
                'Your password is same as the username. This is not allowed.';
         v_check_failed := TRUE;
         RETURN (4);
     
      ELSIF     NVL (plenght, 0) < NVL (v_syspwdlength, 0)
            AND NVL (v_syspwdlength, 0) > 0
      THEN
         v_msg :=
               'Your password must be a minimum of '
            || v_syspwdlength
            || ' alpha or numeric characters ';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF     NVL (capscnt, 0) < NVL (v_sysupppwlength, 0)
            AND NVL (v_sysupppwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysupppwlength
            || ' upper case characters ';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF     NVL (lwrcnt, 0) < NVL (v_syslwrpwlength, 0)
            AND NVL (v_syslwrpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_syslwrpwlength
            || ' lower case characters ';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF     NVL (numcnt, 0) < NVL (v_sysnumpwlength, 0)
            AND NVL (v_sysnumpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysnumpwlength
            || ' numeric characters ';
         v_check_failed := TRUE;
         RETURN (4);
      ELSIF     NVL (speccnt, 0) < NVL (v_sysspecpwlength, 0)
            AND NVL (v_sysspecpwlength, 0) > 0
      THEN
         v_msg :=
               'Your password should contain a minimum of '
            || v_sysspecpwlength
            || ' special characters ';
         v_check_failed := TRUE;
         v_mandatory := FALSE;
         RETURN (4);
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
      
      

      UPDATE TQC_USERS
      SET usr_last_date = TRUNC (SYSDATE)
      WHERE UPPER (usr_username) = v_entered_user;

      v_msg := v_msg || ' Login Successfull';
      RETURN (1);
   END;

   

  
  FUNCTION user_authenticate (
      vusername       IN       VARCHAR2,
      vpwd            IN       VARCHAR2,
      vmsg            OUT      VARCHAR2,
      lastlogindate   OUT      DATE,
      usercode        OUT      NUMBER,
      username        OUT      VARCHAR2,
      v_sys_code      IN       NUMBER,
      v_sysPath       OUT      VARCHAR2
   )
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
                         NULL,
                         NULL,
                         'N',
                         v_sys_code,
                         v_sysPath
                        );
      RETURN (voutput);
   END;

   FUNCTION user_authenticate (
      vusername       IN       VARCHAR2,
      vpwd            IN       VARCHAR2,
      v_pwd_reentry   IN       VARCHAR2,
      v_new_pwd1      IN       VARCHAR2,
      v_new_pwd2      IN       VARCHAR2,
      vmsg            OUT      VARCHAR2,
      lastlogindate   OUT      DATE,
      usercode        OUT      NUMBER,
      username        OUT      VARCHAR2,
      v_sys_code      IN       NUMBER,
      v_sysPath       OUT      VARCHAR2
   )
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
                         v_sys_code,
                         v_sysPath
                        );
      RETURN (voutput);
   END;

   
   
   FUNCTION get_param_varchar(v_param IN VARCHAR2,v_couCode IN NUMBER) RETURN VARCHAR2 IS
	v_param_value  VARCHAR2(500);
BEGIN

		SELECT PARAM_VALUE
        INTO v_param_value
        FROM PARAMETERS_VIEW
        WHERE PARAM_NAME = v_param
        AND SYS_COU_CODE = v_couCode;
        RETURN(v_param_value);
EXCEPTION
	WHEN NO_DATA_FOUND THEN
    	RAISE_ERROR('Parameter '||v_param||' not found ');
    WHEN TOO_MANY_ROWS THEN
    	RAISE_ERROR('More than one instance of Parameter '||v_param||' found ');
    WHEN OTHERS THEN
    	RAISE_ERROR('Error getting the parameter value set for '||v_param);
END;
FUNCTION get_param_NUMBER(v_param IN VARCHAR2,v_couCode IN NUMBER) RETURN NUMBER IS
	v_param_value  NUMBER;
BEGIN
		SELECT TO_NUMBER(PARAM_VALUE)
        INTO v_param_value
        FROM PARAMETERS_VIEW
        WHERE PARAM_NAME = v_param
        AND SYS_COU_CODE = v_couCode;
        RETURN(v_param_value);
EXCEPTION
	WHEN NO_DATA_FOUND THEN
    	RAISE_ERROR('Parameter '||v_param||' not found ');
    WHEN TOO_MANY_ROWS THEN
    	RAISE_ERROR('More than one instance of Parameter '||v_param||' found ');
    WHEN OTHERS THEN
    	RAISE_ERROR('Error getting the parameter value set for '||v_param);
END;
FUNCTION get_param_date(v_param IN VARCHAR2,v_couCode IN NUMBER) RETURN DATE IS
	v_param_value  DATE;
BEGIN
		SELECT TO_DATE(PARAM_VALUE)
        INTO v_param_value
        FROM PARAMETERS_VIEW
        WHERE PARAM_NAME = v_param
        AND SYS_COU_CODE = v_couCode;
        RETURN(v_param_value);
EXCEPTION
	WHEN NO_DATA_FOUND THEN
    	RAISE_ERROR('Parameter '||v_param||' not found ');
    WHEN TOO_MANY_ROWS THEN
    	RAISE_ERROR('More than one instance of Parameter '||v_param||' found ');
    WHEN OTHERS THEN
    	RAISE_ERROR('Error getting the parameter value set for '||v_param);
END;
   
END CENTRAL_PKG; 
/