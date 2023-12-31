-- 
-- Non Foreign Key Constraints for Table TQC_SERVICE_PROVIDER_DIRECTORS 
-- 
ALTER TABLE TQ_CRM.TQC_SERVICE_PROVIDER_DIRECTORS ADD (
  CONSTRAINT TQC_SPD_PK
  PRIMARY KEY
  (SPD_CODE)
  USING INDEX TQ_CRM.TQC_SPD_PK
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_SERVICE_PROVIDER_DIRECTORS ADD (
  CONSTRAINT TQC_SPD_UK
  UNIQUE (SPD_SPR_CODE, SPD_YEAR)
  USING INDEX TQ_CRM.TQC_SPD_UK
  ENABLE VALIDATE);