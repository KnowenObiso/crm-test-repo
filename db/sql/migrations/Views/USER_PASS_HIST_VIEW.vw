--
-- USER_PASS_HIST_VIEW  (View) 
--
CREATE OR REPLACE FORCE VIEW TQ_CRM.USER_PASS_HIST_VIEW
(USRPH_CODE, USRPH_USR_CODE, USRPH_USR_USERNAME, USR_PWD, USR_PWD_CHANGE_DT, 
 USR_PWD_CHANGE_BY)
AS 
SELECT usrph_code, usrph_usr_code, usrph_usr_username, usr_pwd,
          usr_pwd_change_dt, usr_pwd_change_by
     FROM tqc_users_passwd_hist 
   UNION
   SELECT usrph_code, usrph_usr_code, usrph_usr_username, usr_pwd,
          usr_pwd_change_dt, usr_pwd_change_by
     FROM tqc_users_passwd_hist;