-- 
-- Foreign Key Constraints for Table TQC_SYS_PRCSS_SUBAREA_LVLS 
-- 
ALTER TABLE TQ_CRM.TQC_SYS_PRCSS_SUBAREA_LVLS ADD (
  CONSTRAINT SPSAL_SPSAT_FK 
  FOREIGN KEY (SPSAL_SPSAT_CODE) 
  REFERENCES TQ_CRM.TQC_SYS_PRCSS_SUBAREA_LMTS (SPSAT_CODE)
  ENABLE VALIDATE);