--
-- BRA_SHT_DESC_UNK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.BRA_SHT_DESC_UNK ON TQ_CRM.TQC_BRANCH_AGENCIES
(BRA_SHT_DESC, BRA_BRN_CODE)
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