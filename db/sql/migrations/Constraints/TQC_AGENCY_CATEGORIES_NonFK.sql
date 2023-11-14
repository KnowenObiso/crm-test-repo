-- 
-- Non Foreign Key Constraints for Table TQC_AGENCY_CATEGORIES 
-- 
ALTER TABLE TQ_CRM.TQC_AGENCY_CATEGORIES ADD (
  CONSTRAINT TQC_AGENCY_CATEGORIES_PK
  PRIMARY KEY
  (AGC_CODE)
  USING INDEX TQ_CRM.TQC_AGENCY_CATEGORIES_PK
  ENABLE VALIDATE);