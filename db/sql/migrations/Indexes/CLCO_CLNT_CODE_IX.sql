--
-- CLCO_CLNT_CODE_IX  (Index) 
--
CREATE INDEX TQ_CRM.CLCO_CLNT_CODE_IX ON TQ_CRM.TQC_CLIENT_CONTACTS
(CLCO_CLNT_CODE)
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