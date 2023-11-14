-- 
-- Non Foreign Key Constraints for Table TQC_AGENCY_RATINGS_LOGS 
-- 
ALTER TABLE TQ_CRM.TQC_AGENCY_RATINGS_LOGS ADD (
  CONSTRAINT TQC_AGENCY_RATINGS_LOGS_PK
  PRIMARY KEY
  (ARL_CODE)
  USING INDEX TQ_CRM.TQC_AGENCY_RATINGS_LOGS_PK
  ENABLE VALIDATE);