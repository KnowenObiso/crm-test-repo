-- 
-- Non Foreign Key Constraints for Table TQC_INCI_ACTION_TYPES 
-- 
ALTER TABLE TQ_CRM.TQC_INCI_ACTION_TYPES ADD (
  CONSTRAINT ICTY_CODE_PK
  PRIMARY KEY
  (ICTY_CODE)
  USING INDEX TQ_CRM.ICTY_CODE_PK
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_INCI_ACTION_TYPES ADD (
  UNIQUE (ICTY_SHT_DESC)
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