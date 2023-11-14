--
-- TQC_SYSTEM_MODULES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYSTEM_MODULES
(
  TSM_CODE      NUMBER                          NOT NULL,
  TSM_SHT_DESC  VARCHAR2(10 BYTE)               NOT NULL,
  TSM_DESC      VARCHAR2(20 BYTE)               NOT NULL,
  TSM_SYS_CODE  NUMBER                          NOT NULL
)
TABLESPACE CRMDATA
PCTUSED    40
PCTFREE    10
INITRANS   1
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
           )
NOCOMPRESS ;