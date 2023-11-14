-- 
-- Non Foreign Key Constraints for Table TQC_BANK_IMP_TMPLT 
-- 
ALTER TABLE TQ_CRM.TQC_BANK_IMP_TMPLT ADD (
  CONSTRAINT LMS_BNK_IMP_TMPLT_PK
  PRIMARY KEY
  (LPIT_CODE)
  USING INDEX TQ_CRM.LMS_BNK_IMP_TMPLT_PK
  ENABLE VALIDATE);