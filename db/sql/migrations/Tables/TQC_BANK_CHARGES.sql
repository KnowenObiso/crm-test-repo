--
-- TQC_BANK_CHARGES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BANK_CHARGES
(
  BCR_CODE       NUMBER(15)                     NOT NULL,
  BCR_BNK_CODE   NUMBER(15)                     NOT NULL,
  BCR_RATE       NUMBER(15,5)                   NOT NULL,
  BCR_DATE_FROM  DATE                           NOT NULL,
  BCR_DATE_TO    DATE,
  BCR_RATE_TYPE  VARCHAR2(1 BYTE)               DEFAULT 'P'                   NOT NULL
)
TABLESPACE GISDATA
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