-- 
-- Foreign Key Constraints for Table TQC_SYS_MOD_SUBUNITS 
-- 
ALTER TABLE TQ_CRM.TQC_SYS_MOD_SUBUNITS ADD (
  CONSTRAINT TQC_SYS_MOD_SUBUNITS_R01 
  FOREIGN KEY (TSMS_TSM_CODE) 
  REFERENCES TQ_CRM.TQC_SYSTEM_MODULES (TSM_CODE)
  ENABLE VALIDATE);