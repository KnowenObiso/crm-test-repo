--
-- TQC_CLIENT_ACCOUNTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_ACCOUNTS
(
  CLNA_CODE          NUMBER(22)                 NOT NULL,
  CLNA_SHT_DESC      VARCHAR2(15 BYTE)          NOT NULL,
  CLNA_NAME          VARCHAR2(300 BYTE)         NOT NULL,
  CLNA_CLNT_CODE     NUMBER(15)                 NOT NULL,
  CLNA_CREATED_BY    VARCHAR2(30 BYTE)          NOT NULL,
  CLNA_DATE_CREATED  DATE                       NOT NULL,
  CLNA_STATUS        VARCHAR2(10 BYTE)          NOT NULL,
  CLNA_REMARKS       VARCHAR2(300 BYTE),
  CLNA_WEF           DATE                       NOT NULL,
  CLNA_WET           DATE,
  CLNA_DIV_CODE      NUMBER(22),
  CLNA_CNT_NAME      VARCHAR2(200 BYTE),
  CLNA_CNT_EMAIL     VARCHAR2(200 BYTE),
  CLNA_CNT_TITLE     VARCHAR2(100 BYTE),
  CLNA_CNT_SMSNO     VARCHAR2(100 BYTE)
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