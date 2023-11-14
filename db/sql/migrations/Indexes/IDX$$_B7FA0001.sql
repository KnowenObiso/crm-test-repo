--
-- IDX$$_B7FA0001  (Index) 
--
CREATE INDEX TQ_CRM.IDX$$_B7FA0001 ON TQ_CRM.TQC_ALERTS
(ALRTS_POL_BATCH_NO, ALRTS_SYS_CODE)
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