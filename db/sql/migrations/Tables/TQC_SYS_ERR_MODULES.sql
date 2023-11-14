--
-- TQC_SYS_ERR_MODULES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_ERR_MODULES
(
  SERRM_CODE      NUMBER(8)                     NOT NULL,
  SERRM_SYS_CODE  NUMBER(8),
  SERRM_MODULE    VARCHAR2(25 BYTE)
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