-- 
-- Non Foreign Key Constraints for Table TQC_LEAD_COMMENTS 
-- 
ALTER TABLE TQ_CRM.TQC_LEAD_COMMENTS ADD (
  CONSTRAINT LCMNT_PK
  PRIMARY KEY
  (LCMNT_CODE)
  USING INDEX TQ_CRM.LCMNT_PK
  ENABLE VALIDATE);