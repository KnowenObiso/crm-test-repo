--
-- ACTY_DESC_UQC  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.ACTY_DESC_UQC ON TQ_CRM.TQC_ACTIVITY_TYPES
(ACTY_DESC)
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