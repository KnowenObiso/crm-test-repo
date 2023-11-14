--
-- IDX_VAR_EXECUTION  (Index) 
--
CREATE INDEX TQ_CRM.IDX_VAR_EXECUTION ON TQ_CRM.JBPM4_VARIABLE
(EXECUTION_)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          512K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           );