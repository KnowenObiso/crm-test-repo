-- 
-- Non Foreign Key Constraints for Table TQC_INCIDENT_TYPES 
-- 
ALTER TABLE TQ_CRM.TQC_INCIDENT_TYPES ADD (
  CONSTRAINT INCT_PK
  PRIMARY KEY
  (INCT_CODE)
  USING INDEX TQ_CRM.INCT_PK
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_INCIDENT_TYPES ADD (
  UNIQUE (INCT_SHT_DESC)
  USING INDEX
    TABLESPACE CRMDATA
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                NEXT             1M
                MAXSIZE          UNLIMITED
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
                FREELISTS        1
                FREELIST GROUPS  1
                BUFFER_POOL      DEFAULT
               )
  ENABLE VALIDATE);