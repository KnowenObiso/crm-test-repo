-- 
-- Non Foreign Key Constraints for Table MICROSOFTDTPROPERTIES 
-- 
ALTER TABLE TQ_CRM.MICROSOFTDTPROPERTIES ADD (
  CONSTRAINT MICROSOFT_PK_DTPROPERTIES
  PRIMARY KEY
  (ID, PROPERTY)
  USING INDEX TQ_CRM.MICROSOFT_PK_DTPROPERTIES
  ENABLE VALIDATE);