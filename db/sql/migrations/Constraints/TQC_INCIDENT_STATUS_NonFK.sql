-- 
-- Non Foreign Key Constraints for Table TQC_INCIDENT_STATUS 
-- 
ALTER TABLE TQ_CRM.TQC_INCIDENT_STATUS ADD (
  CONSTRAINT INCS_PK
  PRIMARY KEY
  (INCS_CODE)
  USING INDEX TQ_CRM.INCS_PK
  ENABLE VALIDATE);