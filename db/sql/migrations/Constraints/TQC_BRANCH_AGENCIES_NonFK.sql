-- 
-- Non Foreign Key Constraints for Table TQC_BRANCH_AGENCIES 
-- 
ALTER TABLE TQ_CRM.TQC_BRANCH_AGENCIES ADD (
  CONSTRAINT BRA_CODE_PK
  PRIMARY KEY
  (BRA_CODE)
  USING INDEX TQ_CRM.BRA_CODE_PK
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_BRANCH_AGENCIES ADD (
  CONSTRAINT BRA_SHT_DESC_UNK
  UNIQUE (BRA_SHT_DESC, BRA_BRN_CODE)
  USING INDEX TQ_CRM.BRA_SHT_DESC_UNK
  ENABLE VALIDATE);