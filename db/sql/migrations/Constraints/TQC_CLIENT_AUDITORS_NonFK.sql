-- 
-- Non Foreign Key Constraints for Table TQC_CLIENT_AUDITORS 
-- 
ALTER TABLE TQ_CRM.TQC_CLIENT_AUDITORS ADD (
  CONSTRAINT PK_TQC_CLIENT_AUDITORS
  PRIMARY KEY
  (CLNTAUD_CODE)
  USING INDEX TQ_CRM.PK_TQC_CLIENT_AUDITORS
  ENABLE VALIDATE);