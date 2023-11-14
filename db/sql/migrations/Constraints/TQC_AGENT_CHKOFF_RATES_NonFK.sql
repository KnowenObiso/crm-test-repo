-- 
-- Non Foreign Key Constraints for Table TQC_AGENT_CHKOFF_RATES 
-- 
ALTER TABLE TQ_CRM.TQC_AGENT_CHKOFF_RATES ADD (
  CONSTRAINT ACR_CODE_PK
  PRIMARY KEY
  (ACR_CODE)
  USING INDEX TQ_CRM.ACR_CODE_PK
  ENABLE VALIDATE);