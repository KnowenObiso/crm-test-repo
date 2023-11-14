--
-- USER_SYSTEMS_VIEW  (View) 
--
CREATE OR REPLACE FORCE VIEW TQ_CRM.USER_SYSTEMS_VIEW
(USYS_CODE, USYS_USR_CODE, USYS_SYS_CODE, USYS_WEF, USYS_WET, 
 USYS_SPOST_CODE)
AS 
SELECT usys_code, usys_usr_code, usys_sys_code, usys_wef, usys_wet,
          usys_spost_code
     FROM tqc_user_systems
    WHERE SYSDATE >= usys_wef AND (usys_wet IS NULL OR usys_wet >= SYSDATE)
   UNION
   SELECT usys_code, usys_usr_code, usys_sys_code, usys_wef, usys_wet,
          usys_spost_code
     FROM tqc_user_systems
    WHERE SYSDATE >= usys_wef AND (usys_wet IS NULL OR usys_wet >= SYSDATE);