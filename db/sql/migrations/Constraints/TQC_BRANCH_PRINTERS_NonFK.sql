-- 
-- Non Foreign Key Constraints for Table TQC_BRANCH_PRINTERS 
-- 
ALTER TABLE TQ_CRM.TQC_BRANCH_PRINTERS ADD (
  CONSTRAINT TQC_BRANCH_PRINTERS_PK
  PRIMARY KEY
  (BRP_CODE)
  USING INDEX TQ_CRM.TQC_BRANCH_PRINTERS_PK
  ENABLE VALIDATE);