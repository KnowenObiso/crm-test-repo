--
-- IDX_HACTI_HPROCI  (Index) 
--
CREATE INDEX TQ_CRM.IDX_HACTI_HPROCI ON TQ_CRM.JBPM4_HIST_ACTINST
(HPROCI_)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          640K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           );