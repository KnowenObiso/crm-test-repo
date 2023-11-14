-- 
-- Non Foreign Key Constraints for Table TQC_CLM_PAYMENT_MODES 
-- 
ALTER TABLE TQ_CRM.TQC_CLM_PAYMENT_MODES ADD (
  CONSTRAINT CPM_SHT_DESC_CHK
  CHECK (CPM_SHT_DESC IN ('CHQ','EFT','MOB','RTGS','CR','JE'))
  ENABLE VALIDATE);