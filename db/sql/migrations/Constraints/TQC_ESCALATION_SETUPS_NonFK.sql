-- 
-- Non Foreign Key Constraints for Table TQC_ESCALATION_SETUPS 
-- 
ALTER TABLE TQ_CRM.TQC_ESCALATION_SETUPS ADD (
  CONSTRAINT TQC_ESCALATION_SETUPS_PK
  PRIMARY KEY
  (TQES_ID)
  USING INDEX TQ_CRM.TQC_ESCALATION_SETUPS_PK
  ENABLE VALIDATE);