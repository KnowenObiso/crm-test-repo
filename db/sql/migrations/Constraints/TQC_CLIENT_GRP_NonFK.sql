-- 
-- Non Foreign Key Constraints for Table TQC_CLIENT_GRP 
-- 
ALTER TABLE TQ_CRM.TQC_CLIENT_GRP ADD (
  CONSTRAINT TQC_CLIENT_GRP_PK
  PRIMARY KEY
  (CLNTG_CODE)
  USING INDEX TQ_CRM.TQC_CLIENT_GRP_PK
  ENABLE VALIDATE);

ALTER TABLE TQ_CRM.TQC_CLIENT_GRP ADD (
  UNIQUE (CLNG_NAME)
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