--
-- OBJ_UK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.OBJ_UK ON TQ_CRM.TQC_SYSTEM_OBJECTS
(OBJ_NAME)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          128K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           );