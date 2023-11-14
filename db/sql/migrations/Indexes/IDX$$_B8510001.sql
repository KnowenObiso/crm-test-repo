--
-- IDX$$_B8510001  (Index) 
--
CREATE INDEX TQ_CRM.IDX$$_B8510001 ON TQ_CRM.TQC_CALENDAR_ACTIVITIES
(CALA_USER)
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