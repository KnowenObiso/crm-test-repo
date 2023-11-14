--
-- LCMNT_PK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.LCMNT_PK ON TQ_CRM.TQC_LEAD_COMMENTS
(LCMNT_CODE)
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