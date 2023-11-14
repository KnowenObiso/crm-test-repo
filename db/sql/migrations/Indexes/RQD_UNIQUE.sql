--
-- RQD_UNIQUE  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.RQD_UNIQUE ON TQ_CRM.TQC_REQUIRED_DOCS
(RQD_SYS_CODE, RQD_SPTA_CODE, RQD_SPT_CODE, RQC_RDOC_ID)
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