-- 
-- Non Foreign Key Constraints for Table TQC_BANK_CHARGES 
-- 
ALTER TABLE TQ_CRM.TQC_BANK_CHARGES ADD (
  CONSTRAINT BCR_CODE_PK
  PRIMARY KEY
  (BCR_CODE)
  USING INDEX TQ_CRM.BCR_CODE_PK
  ENABLE VALIDATE);