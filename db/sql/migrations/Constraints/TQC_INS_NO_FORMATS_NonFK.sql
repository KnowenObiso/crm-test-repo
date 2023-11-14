-- 
-- Non Foreign Key Constraints for Table TQC_INS_NO_FORMATS 
-- 
ALTER TABLE TQ_CRM.TQC_INS_NO_FORMATS ADD (
  CONSTRAINT TQC_INS_NO_FORMATS_PK
  PRIMARY KEY
  (INF_AGN_CODE)
  USING INDEX TQ_CRM.TQC_INS_NO_FORMATS_PK
  ENABLE VALIDATE);