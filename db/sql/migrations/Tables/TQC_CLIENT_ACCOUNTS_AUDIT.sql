--
-- TQC_CLIENT_ACCOUNTS_AUDIT  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_ACCOUNTS_AUDIT
(
  CLNAA_CODE               NUMBER(22)           NOT NULL,
  CLNA_CODE                NUMBER(22)           NOT NULL,
  CLNA_SHT_DESC_OLD        VARCHAR2(40 BYTE),
  CLNA_SHT_DESC_NEW        VARCHAR2(40 BYTE),
  CLNA_NAME_OLD            VARCHAR2(300 BYTE),
  CLNA_NAME_NEW            VARCHAR2(300 BYTE),
  CLNA_STATUS_OLD          VARCHAR2(10 BYTE),
  CLNA_STATUS_NEW          VARCHAR2(10 BYTE),
  CLNA_REMARKS_OLD         VARCHAR2(300 BYTE),
  CLNA_REMARKS_NEW         VARCHAR2(300 BYTE),
  CLNA_WEF_OLD             DATE,
  CLNA_WEF_NEW             DATE,
  CLNA_WET_OLD             DATE,
  CLNA_WET_NEW             DATE,
  CLNA_DIV_CODE_OLD        NUMBER(22),
  CLNA_DIV_CODE_NEW        NUMBER(22),
  CLNAA_OPERATION          VARCHAR2(10 BYTE),
  CLNAA_OPERATION_DATE     DATE,
  CLNAA_OPERATION_DONE_BY  VARCHAR2(30 BYTE)
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