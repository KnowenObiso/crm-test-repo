-- 
-- Foreign Key Constraints for Table TQC_HOUSEHOLD_DTLS 
-- 
ALTER TABLE TQ_CRM.TQC_HOUSEHOLD_DTLS ADD (
  CONSTRAINT TQC_HOUSEHOLD_DTLS_R01 
  FOREIGN KEY (HHD_HH_ID) 
  REFERENCES TQ_CRM.TQC_HOUSEHOLDS (HH_ID)
  ENABLE VALIDATE);