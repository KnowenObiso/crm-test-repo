-- 
-- Non Foreign Key Constraints for Table TQC_USER_SUB_DIVISIONS 
-- 
ALTER TABLE TQ_CRM.TQC_USER_SUB_DIVISIONS ADD (
  CONSTRAINT TQC_USER_SUBDIVISION_PK
  PRIMARY KEY
  (USSD_CODE)
  USING INDEX TQ_CRM.TQC_USER_SUBDIVISION_PK
  ENABLE VALIDATE);