--
-- SYS_MTABLE_00001CBDC_IND_1  (Index) 
--
CREATE INDEX TQ_CRM.SYS_MTABLE_00001CBDC_IND_1 ON TQ_CRM.SYS_EXPORT_FULL_01
(OBJECT_SCHEMA, OBJECT_NAME, OBJECT_TYPE)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          128K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           );