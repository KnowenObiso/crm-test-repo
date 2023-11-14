-- 
-- Non Foreign Key Constraints for Table TQC_EMAIL_DMS_REPORTS 
-- 
ALTER TABLE TQ_CRM.TQC_EMAIL_DMS_REPORTS ADD (
  CONSTRAINT TQC_EMAIL_DMS_REPORTS_PK
  PRIMARY KEY
  (EMRPT_CODE)
  USING INDEX TQ_CRM.TQC_EMAIL_DMS_REPORTS_PK
  ENABLE VALIDATE);