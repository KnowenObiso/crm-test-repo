-- 
-- Non Foreign Key Constraints for Table TQC_WEB_CLIENTS 
-- 
ALTER TABLE TQ_CRM.TQC_WEB_CLIENTS ADD (
  CONSTRAINT TQC_WEB_CLIENTS_PK
  PRIMARY KEY
  (WCLNT_CODE)
  USING INDEX TQ_CRM.TQC_WEB_CLIENTS_PK
  ENABLE VALIDATE);