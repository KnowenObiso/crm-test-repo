-- 
-- Non Foreign Key Constraints for Table TQC_CLIENT_DIRECTORS 
-- 
ALTER TABLE TQ_CRM.TQC_CLIENT_DIRECTORS ADD (
  CONSTRAINT PK_TQC_CLIENT_DIRECTORS
  PRIMARY KEY
  (CLNTDIR_CODE)
  USING INDEX TQ_CRM.PK_TQC_CLIENT_DIRECTORS
  ENABLE VALIDATE);