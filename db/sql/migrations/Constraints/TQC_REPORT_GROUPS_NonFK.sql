-- 
-- Non Foreign Key Constraints for Table TQC_REPORT_GROUPS 
-- 
ALTER TABLE TQ_CRM.TQC_REPORT_GROUPS ADD (
  CONSTRAINT TQC_REPORT_GROUPS_PK
  PRIMARY KEY
  (TRG_CODE)
  USING INDEX TQ_CRM.TQC_REPORT_GROUPS_PK
  ENABLE VALIDATE);