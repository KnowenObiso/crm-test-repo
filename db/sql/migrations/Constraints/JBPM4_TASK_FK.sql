-- 
-- Foreign Key Constraints for Table JBPM4_TASK 
-- 
ALTER TABLE TQ_CRM.JBPM4_TASK ADD (
  CONSTRAINT FK_TASK_SWIML 
  FOREIGN KEY (SWIMLANE_) 
  REFERENCES TQ_CRM.JBPM4_SWIMLANE (DBID_)
  ENABLE VALIDATE);