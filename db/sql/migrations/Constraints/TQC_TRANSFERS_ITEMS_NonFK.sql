-- 
-- Non Foreign Key Constraints for Table TQC_TRANSFERS_ITEMS 
-- 
ALTER TABLE TQ_CRM.TQC_TRANSFERS_ITEMS ADD (
  CONSTRAINT TTI_CODE_PK
  PRIMARY KEY
  (TTI_CODE)
  USING INDEX TQ_CRM.TTI_CODE_PK
  ENABLE VALIDATE);