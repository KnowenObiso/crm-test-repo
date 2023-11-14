--
-- LMS_BNK_IMP_TMPLT_PK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.LMS_BNK_IMP_TMPLT_PK ON TQ_CRM.TQC_BANK_IMP_TMPLT
(LPIT_CODE)
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