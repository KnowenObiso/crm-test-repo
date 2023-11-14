--
-- TQC_CURRENCY_RATES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CURRENCY_RATES
(
  CRT_CODE           NUMBER(8)                  NOT NULL,
  CRT_CUR_CODE       NUMBER(8)                  NOT NULL,
  CRT_RATE           NUMBER(22,5)               NOT NULL,
  CRT_DATE           DATE,
  CRT_BASE_CUR_CODE  NUMBER(8),
  CRT_WEF            DATE,
  CRT_WET            DATE,
  CRT_CREATED_BY     VARCHAR2(30 BYTE),
  CRT_CREATED_DATE   DATE,
  CRT_UPDATED_BY     VARCHAR2(30 BYTE),
  CRT_UPDATED_DATE   DATE
)
TABLESPACE USERS
PCTUSED    0
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
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;

COMMENT ON TABLE TQ_CRM.TQC_CURRENCY_RATES IS ' This table is used to store the currency conversion rates';