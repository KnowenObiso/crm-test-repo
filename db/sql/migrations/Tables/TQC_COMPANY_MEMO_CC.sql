--
-- TQC_COMPANY_MEMO_CC  (Table) 
--
CREATE TABLE TQ_CRM.TQC_COMPANY_MEMO_CC
(
  CC_CODE            NUMBER                     NOT NULL,
  CC_COMEM_CODE      NUMBER                     NOT NULL,
  CC_NAME            VARCHAR2(100 BYTE),
  CC_ADDRESS         VARCHAR2(102 BYTE),
  CC_REMARKS         VARCHAR2(200 BYTE),
  CC_TYPE            VARCHAR2(4 BYTE)           DEFAULT 'CC',
  CC_CODETOWN        VARCHAR2(100 BYTE),
  CC_EMAIL_ADDR      VARCHAR2(100 BYTE),
  CC_ADDRESSEE_CODE  NUMBER(20),
  CC_ADDRESSEE_TYPE  NUMBER(1)
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