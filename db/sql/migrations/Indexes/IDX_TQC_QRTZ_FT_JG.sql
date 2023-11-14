--
-- IDX_TQC_QRTZ_FT_JG  (Index) 
--
CREATE INDEX TQ_CRM.IDX_TQC_QRTZ_FT_JG ON TQ_CRM.TQC_QRTZ_FIRED_TRIGGERS
(SCHED_NAME, JOB_GROUP)
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