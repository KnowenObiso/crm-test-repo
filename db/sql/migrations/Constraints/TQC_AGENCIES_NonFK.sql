-- 
-- Non Foreign Key Constraints for Table TQC_AGENCIES 
-- 
ALTER TABLE TQ_CRM.TQC_AGENCIES ADD (
  CONSTRAINT AGN_RUNOFF_CHK
  CHECK (AGN_RUNOFF IN('Y','N'))
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_AGENCIES ADD (
  CONSTRAINT AGN_STATUS_CK
  CHECK (AGN_STATUS IN ('DRAFT','ACTIVE','INACTIVE','READY','SUSPENDED'))
  ENABLE VALIDATE);