-- 
-- Non Foreign Key Constraints for Table TQC_USER_DIVISIONS 
-- 
ALTER TABLE TQ_CRM.TQC_USER_DIVISIONS ADD (
  CONSTRAINT TQC_USER_DIVISIONS_PK
  PRIMARY KEY
  (USD_CODE)
  USING INDEX TQ_CRM.TQC_USER_DIVISIONS_PK
  ENABLE VALIDATE);