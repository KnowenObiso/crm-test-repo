-- 
-- Non Foreign Key Constraints for Table TQC_USER_SYSTEM_ROLES 
-- 
ALTER TABLE TQ_CRM.TQC_USER_SYSTEM_ROLES ADD (
  CONSTRAINT UR_PK
  PRIMARY KEY
  (USRL_USR_CODE, USRL_SRL_CODE, USRL_GRANT_DATE)
  USING INDEX TQ_CRM.UR_PK
  ENABLE VALIDATE);