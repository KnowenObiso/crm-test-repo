-- 
-- Foreign Key Constraints for Table JBPM4_PARTICIPATION 
-- 
ALTER TABLE TQ_CRM.JBPM4_PARTICIPATION ADD (
  CONSTRAINT FK_PART_SWIMLANE 
  FOREIGN KEY (SWIMLANE_) 
  REFERENCES TQ_CRM.JBPM4_SWIMLANE (DBID_)
  ENABLE VALIDATE);