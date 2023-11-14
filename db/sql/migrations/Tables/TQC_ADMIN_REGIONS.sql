--
-- TQC_ADMIN_REGIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ADMIN_REGIONS
(
  ADM_REG_CODE      NUMBER(8)                   NOT NULL,
  ADM_REG_SHT_DESC  VARCHAR2(10 BYTE)           NOT NULL,
  ADM_REG_NAME      VARCHAR2(100 BYTE)          NOT NULL,
  ADM_REG_COU_CODE  NUMBER(8)                   NOT NULL
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