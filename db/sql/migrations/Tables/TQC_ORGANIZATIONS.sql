--
-- TQC_ORGANIZATIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ORGANIZATIONS
(
  ORG_CODE               NUMBER(8)              NOT NULL,
  ORG_SHT_DESC           VARCHAR2(20 BYTE)      NOT NULL,
  ORG_NAME               VARCHAR2(100 BYTE)     NOT NULL,
  ORG_ADDRS              VARCHAR2(100 BYTE),
  ORG_TWN_CODE           NUMBER(8),
  ORG_COU_CODE           NUMBER(8),
  ORG_EMAIL_ADDRS        VARCHAR2(150 BYTE),
  ORG_PHY_ADDRS          VARCHAR2(150 BYTE),
  ORG_CUR_CODE           NUMBER(8),
  ORG_ZIP                VARCHAR2(15 BYTE),
  ORG_FAX                VARCHAR2(50 BYTE),
  ORG_TEL1               VARCHAR2(50 BYTE),
  ORG_TEL2               VARCHAR2(30 BYTE),
  ORG_RPT_LOGO           BLOB,
  ORG_MOTTO              VARCHAR2(2000 BYTE),
  ORG_PIN_NO             VARCHAR2(30 BYTE),
  ORG_ED_CODE            VARCHAR2(15 BYTE),
  ORG_ITEM_ACC_CODE      VARCHAR2(15 BYTE),
  ORG_OTHER_NAME         VARCHAR2(35 BYTE),
  ORG_TYPE               VARCHAR2(3 BYTE)       DEFAULT 'INS',
  ORG_WEB_BRN_CODE       NUMBER(15),
  ORG_WEB_ADDRS          VARCHAR2(150 BYTE),
  ORG_AGN_CODE           NUMBER(15),
  ORG_DIRECTORS          VARCHAR2(2000 BYTE),
  ORG_LANG_CODE          NUMBER,
  ORG_AVATAR             BLOB,
  ORG_LOGO_B64           BLOB,
  ORG_ALT_BRN_CODE       NUMBER(23),
  ORG_STS_CODE           NUMBER,
  ORG_GRP_LOGO           BLOB,
  ORG_REGULATOR_LOGO     BLOB,
  ORG_EXEC_DIRECTOR      VARCHAR2(100 BYTE),
  ORG_MANAGING_DIRECTOR  VARCHAR2(100 BYTE),
  ORG_CERT_NAME          VARCHAR2(100 BYTE),
  ORG_KRA_TAX_REG        VARCHAR2(30 BYTE),
  ORG_VAT_NUMBER         NUMBER,
  ORG_LOGO4              LONG RAW,
  ORG_MOBILE1            VARCHAR2(50 BYTE),
  ORG_MOBILE2            VARCHAR2(50 BYTE),
  ORG_CERT_SIGN          BLOB,
  ORG_BNK_CODE           NUMBER,
  ORG_BBR_CODE           NUMBER,
  ORG_BANK_ACCOUNT_NO    VARCHAR2(20 BYTE),
  ORG_BANK_ACCOUNT_NAME  VARCHAR2(100 BYTE),
  ORG_SWIFT_CODE         VARCHAR2(40 BYTE),
  ORG_DEFAULT            VARCHAR2(10 BYTE)
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

COMMENT ON COLUMN TQ_CRM.TQC_ORGANIZATIONS.ORG_TYPE IS '''INS'' for Insurance, ''BRK'' for Broker, ''BNK for bancassurance''';