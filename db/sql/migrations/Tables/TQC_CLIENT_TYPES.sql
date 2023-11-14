--
-- TQC_CLIENT_TYPES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_TYPES
(
  CLNTY_CODE       NUMBER                       NOT NULL,
  CLNTY_CLNT_TYPE  VARCHAR2(200 BYTE),
  CLNTY_CATEGORY   VARCHAR2(40 BYTE),
  CLNTY_DESC       VARCHAR2(50 BYTE),
  CLNTY_TYPE       VARCHAR2(10 BYTE),
  CLNTY_PERSON     VARCHAR2(1 BYTE)             DEFAULT 'N'
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