--
-- TQC_CLIENTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENTS
(
  CLNT_CODE                  NUMBER(15)         NOT NULL,
  CLNT_SHT_DESC              VARCHAR2(30 BYTE),
  CLNT_NAME                  VARCHAR2(300 BYTE) NOT NULL,
  CLNT_OTHER_NAMES           VARCHAR2(300 BYTE),
  CLNT_ID_REG_NO             VARCHAR2(15 BYTE),
  CLNT_DOB                   DATE,
  CLNT_PIN                   VARCHAR2(20 BYTE),
  CLNT_PHYSICAL_ADDRS        VARCHAR2(300 BYTE),
  CLNT_POSTAL_ADDRS          VARCHAR2(300 BYTE),
  CLNT_TWN_CODE              NUMBER(8),
  CLNT_COU_CODE              NUMBER(8),
  CLNT_EMAIL_ADDRS           VARCHAR2(50 BYTE),
  CLNT_TEL                   VARCHAR2(200 BYTE),
  CLNT_TEL2                  VARCHAR2(50 BYTE),
  CLNT_STATUS                VARCHAR2(15 BYTE),
  CLNT_FAX                   VARCHAR2(30 BYTE),
  CLNT_REMARKS               VARCHAR2(100 BYTE),
  CLNT_SPCL_TERMS            VARCHAR2(1 BYTE),
  CLNT_DECLINED_PROP         VARCHAR2(1 BYTE),
  CLNT_INCREASED_PREMIUM     VARCHAR2(1 BYTE),
  CLNT_POLICY_CANCELLED      VARCHAR2(1 BYTE),
  CLNT_PROPOSER              VARCHAR2(1 BYTE),
  CLNT_ACCNT_NO              VARCHAR2(50 BYTE),
  CLNT_DOMICILE_COUNTRIES    NUMBER(8),
  CLNT_WEF                   DATE               NOT NULL,
  CLNT_WET                   DATE,
  CLNT_WITHDRAWAL_REASON     VARCHAR2(50 BYTE),
  CLNT_SEC_CODE              NUMBER(8),
  CLNT_SURNAME               VARCHAR2(200 BYTE),
  CLNT_TYPE                  VARCHAR2(15 BYTE)  NOT NULL,
  CLNT_TITLE                 VARCHAR2(15 BYTE),
  CLNT_BUSINESS              VARCHAR2(200 BYTE),
  CLNT_ZIP_CODE              VARCHAR2(15 BYTE),
  CLNT_BBR_CODE              NUMBER(15),
  CLNT_BANK_ACC_NO           VARCHAR2(30 BYTE),
  CLNT_CLNT_CODE             NUMBER,
  CANNON_LIFE_POL_NO         VARCHAR2(50 BYTE),
  CLNT_NON_DIRECT            VARCHAR2(2 BYTE),
  CLNT_CREATED_BY            VARCHAR2(15 BYTE)  NOT NULL,
  CLNT_SMS_TEL               VARCHAR2(35 BYTE),
  CLNT_AGNT_STATUS           VARCHAR2(1 BYTE)   DEFAULT 'N'                   NOT NULL,
  CLNT_DATE_CREATED          DATE               NOT NULL,
  CLNT_RUNOFF                VARCHAR2(25 BYTE),
  CLNT_LOADED_BY             VARCHAR2(15 BYTE),
  CLNT_DIRECT_CLIENT         VARCHAR2(1 BYTE)   DEFAULT 'N'                   NOT NULL,
  CLNT_OLD_ACCNT_NO          VARCHAR2(20 BYTE),
  CLNT_AGNT_CLIENT_ID        VARCHAR2(50 BYTE),
  CLNT_GENDER                VARCHAR2(1 BYTE)   DEFAULT null,
  CLNT_USR_CODE              NUMBER,
  CLNT_CRDT_ALLWD            VARCHAR2(1 BYTE),
  CLNT_CRDT_MAX_AMT          NUMBER(22,2),
  CLNT_LOC_CODE              NUMBER,
  CLNT_UPDATE_DT             DATE,
  CLNT_UPDATED_BY            VARCHAR2(20 BYTE),
  CLNT_UPDATED               VARCHAR2(2 BYTE),
  CLNT_CRM_ID                VARCHAR2(30 BYTE),
  CLNT_IMAGE                 BLOB,
  CLNT_CNTCT_PHONE_1         VARCHAR2(21 BYTE),
  CLNT_CNTCT_EMAIL_1         VARCHAR2(50 BYTE),
  CLNT_CNTCT_PHONE_2         VARCHAR2(21 BYTE),
  CLNT_CNTCT_EMAIL_2         VARCHAR2(50 BYTE),
  CLNT_PASSPORT_NO           VARCHAR2(30 BYTE),
  CLNT_CNTCT_NAME_1          VARCHAR2(50 BYTE),
  CLNT_CNTCT_NAME_2          VARCHAR2(50 BYTE),
  CLNT_STS_CODE              NUMBER(15),
  CLNT_BDIV_CODE             NUMBER(22),
  CLNT_BRN_CODE              NUMBER,
  CLNT_WEBSITE               VARCHAR2(150 BYTE),
  CLNT_AUDITORS              VARCHAR2(150 BYTE),
  CLNT_PARENT_COMPANY        NUMBER,
  CLNT_CURRENT_INSURER       VARCHAR2(150 BYTE),
  CLNT_PROJECTED_BUSINESS    NUMBER,
  CLNT_DATE_OF_EMPL          DATE,
  CLNT_DRIVING_LICENCE       VARCHAR2(150 BYTE),
  CLNT_SIGNATURE             BLOB,
  CLNT_ACC_OFFICER           NUMBER,
  CLNT_COMMONS_ID            VARCHAR2(30 BYTE),
  CLNT_COMMONS_CODE          VARCHAR2(50 BYTE),
  CLT_CELL_NO                VARCHAR2(200 BYTE),
  CLNT_EMPLOYER_CELL_NO      VARCHAR2(200 BYTE),
  CLNT_EMPLOYER_PHONE_NO     VARCHAR2(200 BYTE),
  CLNT_BANK_CELL_NO          VARCHAR2(200 CHAR),
  CLNT_BANK_PHONE_NO         VARCHAR2(200 BYTE),
  CLNT_OCCUPATION            VARCHAR2(100 BYTE),
  CLNT_OLD_SHT_DESC          VARCHAR2(50 BYTE),
  CLNT_ANNIVERSARY           DATE,
  CLNT_CRDT_RATING           VARCHAR2(10 BYTE),
  CLNT_REG_DATE              DATE,
  CLTN_CLIENT_TYPES          VARCHAR2(200 BYTE),
  CLNT_DRV_EXPERIENCE        VARCHAR2(50 BYTE),
  CLNT_SACCO                 VARCHAR2(20 BYTE),
  CLNT_REASON_UPDATED        VARCHAR2(200 BYTE),
  CLNT_PAYROLL_NO            NUMBER(23),
  CLNT_CREDIT_LIM_ALLOWED    VARCHAR2(1 BYTE)   DEFAULT 'N',
  CLNT_CREDIT_LIMIT          NUMBER,
  CLNT_OCC_CODE              NUMBER,
  CLNT_ENT_CODE              NUMBER,
  CLNT_BOUNCED_CHQ           VARCHAR2(1 BYTE)   DEFAULT 'N',
  CLNT_MARITAL_STATUS        VARCHAR2(2 BYTE),
  CLNT_BPN_CODE              NUMBER,
  CLNT_DD_ACCOUNT_NAME       VARCHAR2(200 BYTE),
  CLNT_DD_ACCOUNT_NO         VARCHAR2(50 BYTE),
  CLNT_DD_BBR_CODE           NUMBER,
  CLNT_CNTCT_RELATIONSHIP    VARCHAR2(50 BYTE),
  CLNT_GUARDN_NAME           VARCHAR2(100 BYTE),
  CLNT_GUARDN_AGE            VARCHAR2(50 BYTE),
  CLNT_GUARDN_RELATIONSHIP   VARCHAR2(50 BYTE),
  CLNT_CLIENT_LEVEL          VARCHAR2(1 BYTE)   DEFAULT 'N',
  CLNT_WORK_PERMIT           VARCHAR2(200 BYTE),
  CLNT_DL_ISSUE_DATE         DATE,
  CLNT_DEFAULT_COMM_MODE     VARCHAR2(25 BYTE),
  CLNT_SAL_MAX_RANGE         NUMBER,
  CLNT_SAL_MIN_RANGE         NUMBER,
  CLNT_TEL_PAY               VARCHAR2(50 BYTE),
  CLNT_EMAIL2                VARCHAR2(50 BYTE),
  CLNT_COUNTRY               VARCHAR2(60 BYTE),
  CLNT_COMPLIANCE            VARCHAR2(1 BYTE),
  CLNT_SMS_PREFIX            VARCHAR2(10 BYTE),
  CLNT_SPZ_CODE              NUMBER(23,5),
  CLNT_DRV_DL_ISSUE_DT       DATE,
  CLNT_UNLOCKED_ON           DATE,
  CLNT_LOCKED                VARCHAR2(1 BYTE)   DEFAULT 'N',
  CLNT_COMM_MODE             VARCHAR2(100 BYTE),
  CLNT_UNLOCKED_BY           VARCHAR2(100 BYTE),
  CLNT_AREA_CODE             VARCHAR2(15 BYTE),
  CLNT_RSA_PIN               VARCHAR2(15 BYTE),
  CLNT_GSM_ZIP_CODE          NUMBER,
  CLNT_INT_ZIP_CODE          NUMBER,
  CLNT_INT_TEL               VARCHAR2(50 BYTE),
  CLNT_DIGIT_CODE            VARCHAR2(5 BYTE),
  CLNT_PREFFERED_PAYMODE     VARCHAR2(20 BYTE)  DEFAULT 'EFT'                 NOT NULL,
  CLNT_STAFF_NO              VARCHAR2(40 BYTE),
  CLNT_CLNTY_CODE            NUMBER,
  CLNT_BENEFIT_PAYMENT_MODE  VARCHAR2(100 BYTE),
  CLNT_PREMIUM_PAYMENT_MODE  NUMBER(22),
  CLNT_SMS_PREFIX2           VARCHAR2(20 BYTE),
  CLNT_DIVISION              VARCHAR2(40 BYTE),
  CLNT_TEL_PAY2              VARCHAR2(20 BYTE),
  CLNT_MONTHLY_INCOME        NUMBER(22),
  CLNT_FIRST_NAME            VARCHAR2(80 BYTE),
  CLNT_IPRS_VALIDATED        VARCHAR2(1 BYTE),
  CLNT_DIV_CODE              NUMBER(22),
  CLNT_EDUCATION_LEVEL       VARCHAR2(20 BYTE),
  CLNT_VALIDATION_SOURCE     VARCHAR2(80 BYTE),
  CLNT_SMS_TEL2              VARCHAR2(20 BYTE),
  CLNT_PREMIUM_PAYMODE       VARCHAR2(100 BYTE)
)
TABLESPACE CRMDATA
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          11M
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

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_CODE IS 'PRIMARY KEY';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_SHT_DESC IS 'SHORT DESCRPTION (UNIQUE ID)';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_NAME IS 'CLIENT NAME (INCASE OF COOPERATE CLIENT)';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_OTHER_NAMES IS 'OTHER NAMES';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_ID_REG_NO IS 'REGISTERITION NUMBER';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_DOB IS 'DATE OF BIRTH';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_PIN IS 'PIN NUMBER';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_PHYSICAL_ADDRS IS 'PHYSICAL ADDRESS';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_POSTAL_ADDRS IS 'POSTAL ADDRESS';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_TWN_CODE IS 'TOWN REFERENCE NUMBER';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_COU_CODE IS 'COUNTRY REFERENCE NUMBER';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_EMAIL_ADDRS IS 'EMAIL';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_TEL IS 'TELEPHONE NUMBER ONE';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_TEL2 IS 'TELEPHONE NUMBER TWO';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_STATUS IS 'STATUS (''I'' INACTVIE, ''A'' ACTIVE)';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_FAX IS 'FAX';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_REMARKS IS 'REMARKS';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_SPCL_TERMS IS 'SPECIAL TERMS';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_PROPOSER IS 'CLIENT OR PROPOSER ';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_ACCNT_NO IS 'CLIENT ACCOUNT';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_DOMICILE_COUNTRIES IS 'COUNTRY OF DOMICILE';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_WEF IS 'EFFECTIVE FROM';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_WET IS 'EFFECTIVE TO';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_WITHDRAWAL_REASON IS 'REASONS FOR WITHDRAWAL';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_SEC_CODE IS 'SECTOR REFERENCE NUMBER';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_SURNAME IS 'SURNAME';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_TYPE IS 'CLIENT TYPE ''I'' INDIVIDUAL, ''C'' COOPERATE)';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_TITLE IS 'TITLE';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_BUSINESS IS 'BUSINESS TYPE';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_ZIP_CODE IS 'ZIP CODE';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_BBR_CODE IS 'BANK REFERENCE NUMBER';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_BANK_ACC_NO IS 'BANK ACCOUNT';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_NON_DIRECT IS 'DIRECT OR NON DIRECT ';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_CREATED_BY IS 'USER WHO CREATED THE CLIENT';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_SMS_TEL IS 'MOBILE PHONE NUMBER';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_DATE_CREATED IS 'DATE CREATED';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_RUNOFF IS 'RUN OFF OR NOT';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_LOADED_BY IS 'IF LOADED';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_OLD_ACCNT_NO IS 'OLD ACCOUNT FOR LOADED CLIENTS';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_GENDER IS 'GENDER (''F'', FEMALE,''M'', MALE)';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_CRM_ID IS 'Stores third party client id';

COMMENT ON COLUMN TQ_CRM.TQC_CLIENTS.CLNT_DATE_OF_EMPL IS 'DATE OF EMPLOY MENT';