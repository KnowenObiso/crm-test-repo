--
-- PARAMETERS_VIEW  (View) 
--
CREATE OR REPLACE FORCE VIEW TQ_CRM.PARAMETERS_VIEW
(PARAM_CODE, PARAM_NAME, PARAM_VALUE, PARAM_STATUS, PARAM_DESC, 
 SYS_COU_CODE)
AS 
SELECT param_code, param_name, param_value, param_status, param_desc,
  sys_cou_code
FROM tqc_parameters , tqc_systems 
WHERE sys_code = 0
UNION
SELECT param_code, param_name, param_value, param_status, param_desc,
  sys_cou_code
FROM tqc_parameters , tqc_systems
WHERE sys_code = 0;