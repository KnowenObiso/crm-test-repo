-- 
-- Non Foreign Key Constraints for Table TQC_ACTIVITIES 
-- 
ALTER TABLE TQ_CRM.TQC_ACTIVITIES ADD (
  CONSTRAINT TQC_ACTIVITIES_PK
  PRIMARY KEY
  (ACT_CODE)
  USING INDEX TQ_CRM.TQC_ACTIVITIES_PK
  ENABLE VALIDATE);