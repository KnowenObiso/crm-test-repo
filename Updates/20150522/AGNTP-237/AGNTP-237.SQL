ALTER TABLE TQ_CRM.TQC_SERV_REQ_TYPES
 ADD CONSTRAINT TQC_SERV_REQ_TYPES_PK
  PRIMARY KEY
  (SRT_CODE);
  
  
ALTER TABLE TQ_CRM.TQC_SERV_REQ_CAT
 ADD CONSTRAINT TSRC_SRT_FK 
  FOREIGN KEY (TSRC_SRT_CODE) 
  REFERENCES TQ_CRM.TQC_SERV_REQ_TYPES (SRT_CODE);
  
  ALTER TABLE TQ_CRM.TQC_SERV_REQUESTS
 MODIFY CONSTRAINT TQC_SERV_REQUESTS_R01
 ENABLE
 VALIDATE;