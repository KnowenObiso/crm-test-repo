-- 
-- Foreign Key Constraints for Table TQC_BANKS 
-- 
ALTER TABLE TQ_CRM.TQC_BANKS ADD (
  CONSTRAINT BNK_DDR_CODE_FK 
  FOREIGN KEY (BNK_DDR_CODE) 
  REFERENCES TQ_CRM.TQC_DIRECT_DEBIT_REPORTS (DDR_CODE)
  ENABLE VALIDATE);