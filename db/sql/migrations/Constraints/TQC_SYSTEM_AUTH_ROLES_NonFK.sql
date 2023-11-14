-- 
-- Non Foreign Key Constraints for Table TQC_SYSTEM_AUTH_ROLES 
-- 
ALTER TABLE TQ_CRM.TQC_SYSTEM_AUTH_ROLES ADD (
  CONSTRAINT SAR_PK
  PRIMARY KEY
  (SAR_CODE)
  USING INDEX TQ_CRM.SAR_PK
  ENABLE VALIDATE);