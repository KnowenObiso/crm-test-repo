--
-- IDX_EXEC_SUBPI  (Index) 
--
CREATE INDEX TQ_CRM.IDX_EXEC_SUBPI ON TQ_CRM.JBPM4_EXECUTION
(SUBPROCINST_)
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