--
-- TQC_BRU_UK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.TQC_BRU_UK ON TQ_CRM.TQC_BRANCH_UNITS
(BRU_BRN_CODE, BRU_SHT_DESC)
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