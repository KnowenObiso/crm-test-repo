-- 
-- Foreign Key Constraints for Table JBPM4_ID_GROUP 
-- 
ALTER TABLE TQ_CRM.JBPM4_ID_GROUP ADD (
  CONSTRAINT FK_GROUP_PARENT 
  FOREIGN KEY (PARENT_) 
  REFERENCES TQ_CRM.JBPM4_ID_GROUP (DBID_)
  ENABLE VALIDATE);