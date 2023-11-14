-- 
-- Non Foreign Key Constraints for Table TQC_SYSTEM_MODULES 
-- 
ALTER TABLE TQ_CRM.TQC_SYSTEM_MODULES ADD (
  CONSTRAINT TQC_SYSTEM_MODULES_PK
  PRIMARY KEY
  (TSM_CODE)
  USING INDEX TQ_CRM.TQC_SYSTEM_MODULES_PK
  ENABLE VALIDATE);