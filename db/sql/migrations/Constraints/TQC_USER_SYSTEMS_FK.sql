-- 
-- Foreign Key Constraints for Table TQC_USER_SYSTEMS 
-- 
ALTER TABLE TQ_CRM.TQC_USER_SYSTEMS ADD (
  CONSTRAINT USYS_SPOST_FK 
  FOREIGN KEY (USYS_SPOST_CODE) 
  REFERENCES TQ_CRM.TQC_SYS_POSTS (SPOST_CODE)
  ENABLE VALIDATE);