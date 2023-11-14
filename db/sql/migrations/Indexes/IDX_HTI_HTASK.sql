--
-- IDX_HTI_HTASK  (Index) 
--
CREATE INDEX TQ_CRM.IDX_HTI_HTASK ON TQ_CRM.JBPM4_HIST_ACTINST
(HTASK_)
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