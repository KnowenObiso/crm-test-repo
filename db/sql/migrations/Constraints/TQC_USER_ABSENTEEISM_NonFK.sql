-- 
-- Non Foreign Key Constraints for Table TQC_USER_ABSENTEEISM 
-- 
ALTER TABLE TQ_CRM.TQC_USER_ABSENTEEISM ADD (
  CONSTRAINT TQC_USER_ABSENTEEISM_PK
  PRIMARY KEY
  (TQUA_ID)
  USING INDEX TQ_CRM.TQC_USER_ABSENTEEISM_PK
  ENABLE VALIDATE);