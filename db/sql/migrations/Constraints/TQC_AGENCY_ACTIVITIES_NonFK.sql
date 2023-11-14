-- 
-- Non Foreign Key Constraints for Table TQC_AGENCY_ACTIVITIES 
-- 
ALTER TABLE TQ_CRM.TQC_AGENCY_ACTIVITIES ADD (
  CONSTRAINT ACC_PK
  PRIMARY KEY
  (AAC_CODE)
  USING INDEX TQ_CRM.ACC_PK
  ENABLE VALIDATE);