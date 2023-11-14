-- 
-- Non Foreign Key Constraints for Table TQC_DIVISIONS_AUDIT_TRL 
-- 
ALTER TABLE TQ_CRM.TQC_DIVISIONS_AUDIT_TRL ADD (
  CONSTRAINT TQC_DIVISIONS_AUDIT_TRL_PK
  PRIMARY KEY
  (DAUDT_CODE)
  USING INDEX TQ_CRM.TQC_DIVISIONS_AUDIT_TRL_PK
  ENABLE VALIDATE);