-- 
-- Foreign Key Constraints for Table TQC_REPORT_DIV_GROUPS 
-- 
ALTER TABLE TQ_CRM.TQC_REPORT_DIV_GROUPS ADD (
  CONSTRAINT RDG_DIV_CODE_FK 
  FOREIGN KEY (RDG_DIV_CODE) 
  REFERENCES TQ_CRM.TQC_DIVISIONS (DIV_CODE)
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_REPORT_DIV_GROUPS ADD (
  CONSTRAINT RDG_TRG_CODE_FK 
  FOREIGN KEY (RDG_TRG_CODE) 
  REFERENCES TQ_CRM.TQC_REPORT_GROUPS (TRG_CODE)
  ENABLE VALIDATE);