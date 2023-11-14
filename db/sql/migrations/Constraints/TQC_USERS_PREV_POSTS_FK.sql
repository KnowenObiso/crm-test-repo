-- 
-- Foreign Key Constraints for Table TQC_USERS_PREV_POSTS 
-- 
ALTER TABLE TQ_CRM.TQC_USERS_PREV_POSTS ADD (
  CONSTRAINT USRPP_SPOST_FK 
  FOREIGN KEY (USRPP_SPOST_CODE) 
  REFERENCES TQ_CRM.TQC_SYS_POSTS (SPOST_CODE)
  ENABLE VALIDATE);