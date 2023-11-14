--
-- TQC_OCCUPATIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_OCCUPATIONS
(
  OCC_CODE      NUMBER,
  OCC_SEC_CODE  NUMBER(8),
  OCC_SHT_DESC  VARCHAR2(15 BYTE)               NOT NULL,
  OCC_NAME      VARCHAR2(100 BYTE)              NOT NULL
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