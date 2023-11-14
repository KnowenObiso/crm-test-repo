--
-- BBR_BNK_IDX  (Index) 
--
CREATE INDEX TQ_CRM.BBR_BNK_IDX ON TQ_CRM.TQC_BANK_BRANCHES
(BBR_BNK_CODE)
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