--
-- SYSTEMS_VIEW  (View) 
--
CREATE OR REPLACE FORCE VIEW TQ_CRM.SYSTEMS_VIEW
(SYS_CODE, SYS_SHT_DESC, SYS_NAME, SYS_PATH, SYS_COU_CODE)
AS 
SELECT sys_code, sys_sht_desc, sys_name, sys_path, sys_cou_code
     FROM tqc_systems
    WHERE sys_active = 'Y'
   UNION
   SELECT sys_code, sys_sht_desc, sys_name, sys_path, sys_cou_code
     FROM tqc_systems
    WHERE sys_active = 'Y';