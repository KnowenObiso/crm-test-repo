-- 
-- Non Foreign Key Constraints for Table TQC_SYSTEM_OBJECT_PRIVS 
-- 
ALTER TABLE TQ_CRM.TQC_SYSTEM_OBJECT_PRIVS ADD (
  CONSTRAINT SOP_PK
  PRIMARY KEY
  (SOP_OBJ_CODE, SOP_SRL_CODE)
  USING INDEX TQ_CRM.SOP_PK
  ENABLE VALIDATE);