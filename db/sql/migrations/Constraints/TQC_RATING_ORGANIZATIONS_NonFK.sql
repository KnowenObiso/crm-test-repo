-- 
-- Non Foreign Key Constraints for Table TQC_RATING_ORGANIZATIONS 
-- 
ALTER TABLE TQ_CRM.TQC_RATING_ORGANIZATIONS ADD (
  CONSTRAINT TQC_RATING_ORGANIZATIONS_PK
  PRIMARY KEY
  (RORG_CODE)
  USING INDEX TQ_CRM.TQC_RATING_ORGANIZATIONS_PK
  ENABLE VALIDATE);