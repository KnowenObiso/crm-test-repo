-- 
-- Non Foreign Key Constraints for Table TQC_SYS_PROCESS_SUB_AREAS 
-- 
ALTER TABLE TQ_CRM.TQC_SYS_PROCESS_SUB_AREAS ADD (
  CONSTRAINT TQC_SYS_PROCESS_SUB_AREAS_C01
  CHECK (SPRSA_TYPE IN ('A','C'))
  ENABLE VALIDATE);