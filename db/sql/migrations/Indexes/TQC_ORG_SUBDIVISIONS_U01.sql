--
-- TQC_ORG_SUBDIVISIONS_U01  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.TQC_ORG_SUBDIVISIONS_U01 ON TQ_CRM.TQC_ORG_SUBDIVISIONS
(OSD_ID)
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