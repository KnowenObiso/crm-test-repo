-- 
-- Foreign Key Constraints for Table TQC_SYS_MOD_SUBUNITS_DETAILS 
-- 
ALTER TABLE TQ_CRM.TQC_SYS_MOD_SUBUNITS_DETAILS ADD (
  CONSTRAINT TQC_SYS_MOD_SUBUNITS_DETAI_R01 
  FOREIGN KEY (TSMSD_TSMS_CODE) 
  REFERENCES TQ_CRM.TQC_SYS_MOD_SUBUNITS (TSMS_CODE)
  ENABLE VALIDATE);