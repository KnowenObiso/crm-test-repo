--
-- TQC_SYS_REPORT_MODULES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_REPORT_MODULES
(
  SRM_CODE      NUMBER,
  SRM_NAME      VARCHAR2(30 BYTE)               NOT NULL,
  SRM_DESC      VARCHAR2(100 BYTE),
  SRM_SYS_CODE  NUMBER                          NOT NULL
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