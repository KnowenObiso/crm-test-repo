--
-- BNS_ALLNAMES_IX  (Index) 
--
CREATE INDEX TQ_CRM.BNS_ALLNAMES_IX ON TQ_CRM.TQC_BRANCH_NAMES
(BNS_SHT_DESC, BNS_NAME)
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