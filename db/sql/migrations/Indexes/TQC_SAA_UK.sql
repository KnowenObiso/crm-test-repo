--
-- TQC_SAA_UK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.TQC_SAA_UK ON TQ_CRM.TQC_SYSTEM_AUTH_AREAS
(SAA_CODE, SAA_SYS_CODE)
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