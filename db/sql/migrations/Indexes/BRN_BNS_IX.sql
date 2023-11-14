--
-- BRN_BNS_IX  (Index) 
--
CREATE INDEX TQ_CRM.BRN_BNS_IX ON TQ_CRM.TQC_BRANCHES
(BRN_BNS_CODE)
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