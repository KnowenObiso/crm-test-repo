--
-- TQC_SYS_RPT_SUB_MODULES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_RPT_SUB_MODULES
(
  RSM_CODE      NUMBER,
  RSM_NAME      VARCHAR2(30 BYTE)               NOT NULL,
  RSM_DESC      VARCHAR2(100 BYTE),
  RSM_SRM_CODE  NUMBER                          NOT NULL
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