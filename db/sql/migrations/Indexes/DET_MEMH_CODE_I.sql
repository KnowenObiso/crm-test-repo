--
-- DET_MEMH_CODE_I  (Index) 
--
CREATE INDEX TQ_CRM.DET_MEMH_CODE_I ON TQ_CRM.TQC_MEMO_HEADER_DTLS
(MEMDTLS_MEMDET_CODE, MEMDTLS_MEMH_CODE)
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