--
-- TQC_SYS_PRCSS_SUBAREA_LVLS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_PRCSS_SUBAREA_LVLS
(
  SPSAL_CODE           NUMBER(8)                NOT NULL,
  SPSAL_SPRSA_CODE     NUMBER(8)                NOT NULL,
  SPSAL_SPSAT_CODE     NUMBER(8)                NOT NULL,
  SPSAL_LEVEL          NUMBER(5)                NOT NULL,
  SPSAL_APPROVER_TYPE  VARCHAR2(5 BYTE)         NOT NULL,
  SPSAL_APPROVER_ID    NUMBER(8)                NOT NULL
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