--
-- AGNT_ACCT_I  (Index) 
--
CREATE INDEX TQ_CRM.AGNT_ACCT_I ON TQ_CRM.LOAD_AGENTS
(ACCOUNT)
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