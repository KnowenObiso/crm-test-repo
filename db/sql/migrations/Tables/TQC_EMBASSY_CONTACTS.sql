--
-- TQC_EMBASSY_CONTACTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_EMBASSY_CONTACTS
(
  ACCC_CODE             NUMBER(15),
  ACCC_COU_CODE         NUMBER(8),
  ACCC_NAME             VARCHAR2(75 BYTE),
  ACCC_OTHER_NAMES      VARCHAR2(100 BYTE),
  ACCC_DOB              DATE,
  ACCC_TEL              VARCHAR2(35 BYTE),
  ACCC_EMAIL_ADDR       VARCHAR2(100 BYTE),
  ACCC_SMS_TEL          VARCHAR2(35 BYTE),
  ACCC_USERNAME         VARCHAR2(15 BYTE),
  ACCC_PWD              VARCHAR2(50 BYTE),
  ACCC_LOGIN_ALLOWED    VARCHAR2(1 BYTE)        DEFAULT 'N',
  ACCC_PWD_CHANGED      DATE,
  ACCC_PWD_RESET        VARCHAR2(2 BYTE),
  ACCC_DT_CREATED       DATE,
  ACCC_STATUS           VARCHAR2(2 BYTE),
  ACCC_LOGIN_ATTEMPTS   NUMBER(8),
  ACCC_PERSONEL_RANK    VARCHAR2(50 BYTE),
  ACCC_LAST_LOGIN_DATE  DATE,
  ACCC_CREATED_BY       VARCHAR2(50 BYTE)
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