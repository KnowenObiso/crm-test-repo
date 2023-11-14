--
-- TQC_DIRECT_DEBIT  (Table) 
--
CREATE TABLE TQ_CRM.TQC_DIRECT_DEBIT
(
  DD_CODE            NUMBER(8)                  NOT NULL,
  DD_REF_NO          VARCHAR2(20 BYTE)          NOT NULL,
  DD_SENT_YN         VARCHAR2(1 BYTE)           DEFAULT 'N'                   NOT NULL,
  DD_ACNT_NO         VARCHAR2(20 BYTE),
  DD_BOOK_DATE       DATE                       NOT NULL,
  DD_BBR_CODE        NUMBER(15),
  DD_STATUS          VARCHAR2(1 BYTE)           DEFAULT 'D'                   NOT NULL,
  DD_RECEIPTED       VARCHAR2(1 BYTE)           DEFAULT 'N'                   NOT NULL,
  DD_VALUE_DATE      DATE,
  DD_RAISED_BY       VARCHAR2(50 BYTE),
  DD_DATE            DATE,
  DD_BNK_CODE        NUMBER(22),
  DD_AUTH_BY         VARCHAR2(50 BYTE),
  DD_AUTH_DATE       DATE,
  DD_AUTHORIZED      VARCHAR2(3 BYTE),
  DD_FILE_GENERATED  VARCHAR2(1 BYTE)           DEFAULT 'N',
  DD_FILE_NO         NUMBER(1),
  DD_GEN_DATE_VALUE  NUMBER,
  DD_RECEIPT_DATE    DATE
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

COMMENT ON COLUMN TQ_CRM.TQC_DIRECT_DEBIT.DD_STATUS IS '(D)RAFT; (A)UTHORIZED';