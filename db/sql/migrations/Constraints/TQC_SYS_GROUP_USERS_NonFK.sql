-- 
-- Non Foreign Key Constraints for Table TQC_SYS_GROUP_USERS 
-- 
ALTER TABLE TQ_CRM.TQC_SYS_GROUP_USERS ADD (
  CONSTRAINT TQC_GRP_USRS_PK
  PRIMARY KEY
  (GSUSR_CODE)
  USING INDEX TQ_CRM.TQC_GRP_USRS_PK
  ENABLE VALIDATE);