-- 
-- Non Foreign Key Constraints for Table TQC_BACKUP_SYSTEMS 
-- 
ALTER TABLE TQ_CRM.TQC_BACKUP_SYSTEMS ADD (
  CONSTRAINT BKSYS_UK
  UNIQUE (BKSYS_SYS_CODE, BKSYS_TIMESTAMP)
  USING INDEX TQ_CRM.BKSYS_UK
  ENABLE VALIDATE);