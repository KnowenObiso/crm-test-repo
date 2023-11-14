-- 
-- Non Foreign Key Constraints for Table TQC_REPORT_DIV_GROUPS 
-- 
ALTER TABLE TQ_CRM.TQC_REPORT_DIV_GROUPS ADD (
  CONSTRAINT TQC_REPORT_DIV_GROUPS_PK
  PRIMARY KEY
  (RDG_CODE)
  USING INDEX TQ_CRM.TQC_REPORT_DIV_GROUPS_PK
  ENABLE VALIDATE);