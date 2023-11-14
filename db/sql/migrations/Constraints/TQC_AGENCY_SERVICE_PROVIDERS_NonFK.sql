-- 
-- Non Foreign Key Constraints for Table TQC_AGENCY_SERVICE_PROVIDERS 
-- 
ALTER TABLE TQ_CRM.TQC_AGENCY_SERVICE_PROVIDERS ADD (
  CONSTRAINT TQC_AGENCY_SERVICE_PROVIDERSPK
  PRIMARY KEY
  (AGNT_CODE)
  USING INDEX TQ_CRM.TQC_AGENCY_SERVICE_PROVIDERSPK
  ENABLE VALIDATE);