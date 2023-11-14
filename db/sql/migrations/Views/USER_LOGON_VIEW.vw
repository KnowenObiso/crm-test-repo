--
-- USER_LOGON_VIEW  (View) 
--
CREATE OR REPLACE FORCE VIEW TQ_CRM.USER_LOGON_VIEW
(USL_SESSION_CODE, USL_USR_CODE, USL_SYS_CODE, USL_LOGON_DT, USL_LOGOUT_DT)
AS 
SELECT usl_session_code, usl_usr_code, usl_sys_code, usl_logon_dt,
          usl_logout_dt
     FROM tqc_user_logon 
   UNION
   SELECT usl_session_code, usl_usr_code, usl_sys_code, usl_logon_dt,
          usl_logout_dt
     FROM tqc_user_logon;