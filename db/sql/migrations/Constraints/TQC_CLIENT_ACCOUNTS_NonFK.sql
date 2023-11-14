-- 
-- Non Foreign Key Constraints for Table TQC_CLIENT_ACCOUNTS 
-- 
ALTER TABLE TQ_CRM.TQC_CLIENT_ACCOUNTS ADD (
  CONSTRAINT CLNA_STATUS_CK
  CHECK (CLNA_STATUS IN ('ACTIVE','INACTIVE','SUSPENDED'))
  ENABLE VALIDATE);