--
-- IDX$$_00010001  (Index) 
--
CREATE INDEX TQ_CRM.IDX$$_00010001 ON TQ_CRM.TQC_CLIENTS
(CLNT_AGNT_CLIENT_ID)
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