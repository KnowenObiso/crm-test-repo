-- 
-- Foreign Key Constraints for Table TQC_CAMP_ACTIVITIES 
-- 
ALTER TABLE TQ_CRM.TQC_CAMP_ACTIVITIES ADD (
  CONSTRAINT CMA_ACT_FK 
  FOREIGN KEY (CMA_ACT_CODE) 
  REFERENCES TQ_CRM.TQC_ACTIVITIES (ACT_CODE)
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_CAMP_ACTIVITIES ADD (
  CONSTRAINT CMA_CMP_CODE_FK 
  FOREIGN KEY (CMA_CMP_CODE) 
  REFERENCES TQ_CRM.TQC_CAMPAIGNS (CMP_CODE)
  ENABLE VALIDATE);