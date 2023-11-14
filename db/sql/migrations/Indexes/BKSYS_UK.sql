--
-- BKSYS_UK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.BKSYS_UK ON TQ_CRM.TQC_BACKUP_SYSTEMS
(BKSYS_SYS_CODE, BKSYS_TIMESTAMP)
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