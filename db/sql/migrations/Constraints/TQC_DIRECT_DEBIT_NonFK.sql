-- 
-- Non Foreign Key Constraints for Table TQC_DIRECT_DEBIT 
-- 
ALTER TABLE TQ_CRM.TQC_DIRECT_DEBIT ADD (
  CONSTRAINT DD_AUTHORIZED_CHK
  CHECK (DD_AUTHORIZED IN ('N','Y'))
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_DIRECT_DEBIT ADD (
  CONSTRAINT DD_STATUS_CHK
  CHECK (DD_STATUS IN ('D','A'))
  ENABLE VALIDATE);