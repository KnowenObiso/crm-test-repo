-- 
-- Non Foreign Key Constraints for Table TQC_CLIENTS_TRACKING 
-- 
ALTER TABLE TQ_CRM.TQC_CLIENTS_TRACKING ADD (
  CONSTRAINT TQC_CLIENTS_TRACKING_PK
  PRIMARY KEY
  (CLNTT_CODE)
  USING INDEX TQ_CRM.TQC_CLIENTS_TRACKING_PK
  ENABLE VALIDATE);