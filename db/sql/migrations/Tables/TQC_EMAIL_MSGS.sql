--
-- TQC_EMAIL_MSGS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_EMAIL_MSGS
(
  EMAIL_CODE           NUMBER(15),
  EMAIL_SYS_CODE       NUMBER(15)               NOT NULL,
  EMAIL_SYS_MODULE     VARCHAR2(5 BYTE)         DEFAULT 'U'                   NOT NULL,
  EMAIL_CLNT_CODE      NUMBER(15)               NOT NULL,
  EMAIL_AGN_CODE       NUMBER(15)               NOT NULL,
  EMAIL_POL_CODE       NUMBER(15),
  EMAIL_POL_NO         VARCHAR2(35 BYTE),
  EMAIL_CLM_NO         VARCHAR2(35 BYTE),
  EMAIL_TEL_NO         VARCHAR2(35 BYTE),
  EMAIL_MSG            VARCHAR2(500 BYTE),
  EMAIL_STATUS         VARCHAR2(2 BYTE)         DEFAULT 'D'                   NOT NULL,
  EMAIL_PREPARED_BY    VARCHAR2(35 BYTE),
  EMAIL_PREPARED_DATE  DATE,
  EMAIL_SEND_DATE      DATE,
  EMAIL_QUOT_CODE      NUMBER(15),
  EMAIL_QUOT_NO        VARCHAR2(30 BYTE),
  EMAIL_USR_CODE       NUMBER(22),
  EMAIL_SENT_RESPONSE  VARCHAR2(200 BYTE),
  EMAIL_SUBJ           VARCHAR2(1000 BYTE),
  EMAIL_ADDRESS        VARCHAR2(35 BYTE)
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