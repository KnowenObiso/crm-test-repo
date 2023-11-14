--
-- TQC_HOLIDAYS_DEFINITIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_HOLIDAYS_DEFINITIONS
(
  THD_DESC      VARCHAR2(200 BYTE)              NOT NULL,
  THD_DAY       NUMBER(3)                       NOT NULL,
  THD_MON       NUMBER(3)                       NOT NULL,
  THD_STATUS    VARCHAR2(5 BYTE)                DEFAULT 'A',
  THD_COU_CODE  NUMBER(8)                       NOT NULL
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