-- 
-- Non Foreign Key Constraints for Table TQC_INC_DEPARTMENTS 
-- 
ALTER TABLE TQ_CRM.TQC_INC_DEPARTMENTS ADD (
  CONSTRAINT IDEP_CODE_PK
  PRIMARY KEY
  (IDEP_CODE)
  USING INDEX TQ_CRM.IDEP_CODE_PK
  ENABLE VALIDATE);