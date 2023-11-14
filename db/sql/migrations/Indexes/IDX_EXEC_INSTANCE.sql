--
-- IDX_EXEC_INSTANCE  (Index) 
--
CREATE INDEX TQ_CRM.IDX_EXEC_INSTANCE ON TQ_CRM.JBPM4_EXECUTION
(INSTANCE_)
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