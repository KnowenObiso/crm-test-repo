-- 
-- Non Foreign Key Constraints for Table TQC_CLIENT_USR_BRANCHES 
-- 
ALTER TABLE TQ_CRM.TQC_CLIENT_USR_BRANCHES ADD (
  CONSTRAINT TQC_CLIENT_USR_BRANCHES_PK
  PRIMARY KEY
  (TCUB_CODE)
  USING INDEX TQ_CRM.TQC_CLIENT_USR_BRANCHES_PK
  ENABLE VALIDATE);