-- 
-- Foreign Key Constraints for Table TQC_ERROR 
-- 
ALTER TABLE TQ_CRM.TQC_ERROR ADD (
  CONSTRAINT ERR_SYSM_CODE 
  FOREIGN KEY (ERR_SERRM_CODE) 
  REFERENCES TQ_CRM.TQC_SYS_ERR_MODULES (SERRM_CODE)
  ENABLE VALIDATE);