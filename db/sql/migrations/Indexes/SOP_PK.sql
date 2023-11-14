--
-- SOP_PK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.SOP_PK ON TQ_CRM.TQC_SYSTEM_OBJECT_PRIVS
(SOP_OBJ_CODE, SOP_SRL_CODE)
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
           );