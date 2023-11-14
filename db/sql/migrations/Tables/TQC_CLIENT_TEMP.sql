--
-- TQC_CLIENT_TEMP  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_TEMP
(
  CLN_CODE                 NUMBER,
  CLN_CLNT_SHT_DESC        VARCHAR2(200 BYTE),
  CLN_CLNT_NAME            VARCHAR2(200 BYTE),
  CLN_CLNT_OTHER_NAMES     VARCHAR2(200 BYTE),
  CLN_CLNT_POSTAL_ADDRS    VARCHAR2(200 BYTE),
  CLN_CLNT_PHYSICAL_ADDRS  VARCHAR2(200 BYTE),
  CLN_CLNT_TEL             VARCHAR2(200 BYTE),
  CLN_CLNT_TEL2            VARCHAR2(200 BYTE),
  CLN_CLNT_FAX             VARCHAR2(200 BYTE),
  CLN_CLNT_CNTCT_EMAIL_1   VARCHAR2(200 BYTE),
  CLN_CLNT_ID_REG_NO       VARCHAR2(200 BYTE),
  CLN_CLNT_DOB             DATE,
  CLN_LOADED               VARCHAR2(30 BYTE),
  CLN_CLNT_TWN_CODE        VARCHAR2(200 BYTE),
  CLNT_OLD_SHT_DESC        VARCHAR2(200 BYTE),
  CLN_REMARKS              VARCHAR2(400 BYTE)
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