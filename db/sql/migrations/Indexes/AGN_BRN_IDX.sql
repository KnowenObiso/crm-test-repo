--
-- AGN_BRN_IDX  (Index) 
--
CREATE INDEX TQ_CRM.AGN_BRN_IDX ON TQ_CRM.TQC_AGENCIES
(AGN_BRN_CODE)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          320K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           );