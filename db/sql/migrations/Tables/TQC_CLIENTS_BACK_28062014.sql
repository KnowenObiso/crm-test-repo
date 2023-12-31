--
-- TQC_CLIENTS_BACK_28062014  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENTS_BACK_28062014
(
  CLNT_CODE                NUMBER(15)           NOT NULL,
  CLNT_SHT_DESC            VARCHAR2(50 BYTE)    NOT NULL,
  CLNT_NAME                VARCHAR2(300 BYTE)   NOT NULL,
  CLNT_OTHER_NAMES         VARCHAR2(300 BYTE),
  CLNT_ID_REG_NO           VARCHAR2(15 BYTE),
  CLNT_DOB                 DATE,
  CLNT_PIN                 VARCHAR2(20 BYTE),
  CLNT_PHYSICAL_ADDRS      VARCHAR2(300 BYTE),
  CLNT_POSTAL_ADDRS        VARCHAR2(300 BYTE),
  CLNT_TWN_CODE            NUMBER(8),
  CLNT_COU_CODE            NUMBER(8),
  CLNT_EMAIL_ADDRS         VARCHAR2(50 BYTE),
  CLNT_TEL                 VARCHAR2(200 BYTE),
  CLNT_TEL2                VARCHAR2(50 BYTE),
  CLNT_STATUS              VARCHAR2(15 BYTE),
  CLNT_FAX                 VARCHAR2(30 BYTE),
  CLNT_REMARKS             VARCHAR2(100 BYTE),
  CLNT_SPCL_TERMS          VARCHAR2(1 BYTE),
  CLNT_DECLINED_PROP       VARCHAR2(1 BYTE),
  CLNT_INCREASED_PREMIUM   VARCHAR2(1 BYTE),
  CLNT_POLICY_CANCELLED    VARCHAR2(1 BYTE),
  CLNT_PROPOSER            VARCHAR2(1 BYTE),
  CLNT_ACCNT_NO            VARCHAR2(50 BYTE),
  CLNT_DOMICILE_COUNTRIES  NUMBER(8),
  CLNT_WEF                 DATE                 NOT NULL,
  CLNT_WET                 DATE,
  CLNT_WITHDRAWAL_REASON   VARCHAR2(50 BYTE),
  CLNT_SEC_CODE            NUMBER(8),
  CLNT_SURNAME             VARCHAR2(200 BYTE),
  CLNT_TYPE                VARCHAR2(15 BYTE),
  CLNT_TITLE               VARCHAR2(10 BYTE),
  CLNT_BUSINESS            VARCHAR2(200 BYTE),
  CLNT_ZIP_CODE            VARCHAR2(15 BYTE),
  CLNT_BBR_CODE            NUMBER(15),
  CLNT_BANK_ACC_NO         VARCHAR2(30 BYTE),
  CLNT_CLNT_CODE           NUMBER,
  CANNON_LIFE_POL_NO       VARCHAR2(50 BYTE),
  CLNT_NON_DIRECT          VARCHAR2(2 BYTE),
  CLNT_CREATED_BY          VARCHAR2(15 BYTE)    NOT NULL,
  CLNT_SMS_TEL             VARCHAR2(35 BYTE),
  CLNT_AGNT_STATUS         VARCHAR2(1 BYTE)     NOT NULL,
  CLNT_DATE_CREATED        DATE                 NOT NULL,
  CLNT_RUNOFF              VARCHAR2(25 BYTE),
  CLNT_LOADED_BY           VARCHAR2(15 BYTE),
  CLNT_DIRECT_CLIENT       VARCHAR2(1 BYTE)     NOT NULL,
  CLNT_OLD_ACCNT_NO        VARCHAR2(20 BYTE),
  CLNT_AGNT_CLIENT_ID      VARCHAR2(50 BYTE),
  CLNT_GENDER              VARCHAR2(1 BYTE),
  CLNT_USR_CODE            NUMBER,
  CLNT_CRDT_ALLWD          VARCHAR2(1 BYTE),
  CLNT_CRDT_MAX_AMT        NUMBER(22,2),
  CLNT_LOC_CODE            NUMBER,
  CLNT_UPDATE_DT           DATE,
  CLNT_UPDATED_BY          VARCHAR2(20 BYTE),
  CLNT_UPDATED             VARCHAR2(2 BYTE),
  CLNT_CRM_ID              VARCHAR2(30 BYTE),
  CLNT_IMAGE               BLOB,
  CLNT_CNTCT_PHONE_1       VARCHAR2(21 BYTE),
  CLNT_CNTCT_EMAIL_1       VARCHAR2(50 BYTE),
  CLNT_CNTCT_PHONE_2       VARCHAR2(21 BYTE),
  CLNT_CNTCT_EMAIL_2       VARCHAR2(50 BYTE),
  CLNT_PASSPORT_NO         VARCHAR2(30 BYTE),
  CLNT_CNTCT_NAME_1        VARCHAR2(50 BYTE),
  CLNT_CNTCT_NAME_2        VARCHAR2(50 BYTE),
  CLNT_STS_CODE            NUMBER(15),
  CLNT_BDIV_CODE           NUMBER(22),
  CLNT_BRN_CODE            NUMBER,
  CLNT_WEBSITE             VARCHAR2(150 BYTE),
  CLNT_AUDITORS            VARCHAR2(150 BYTE),
  CLNT_PARENT_COMPANY      NUMBER,
  CLNT_CURRENT_INSURER     VARCHAR2(150 BYTE),
  CLNT_PROJECTED_BUSINESS  NUMBER,
  CLNT_DATE_OF_EMPL        DATE,
  CLNT_DRIVING_LICENCE     VARCHAR2(150 BYTE),
  CLNT_SIGNATURE           BLOB,
  CLNT_ACC_OFFICER         NUMBER,
  CLNT_COMMONS_ID          VARCHAR2(30 BYTE),
  CLNT_COMMONS_CODE        VARCHAR2(50 BYTE),
  CLT_CELL_NO              VARCHAR2(200 BYTE),
  CLNT_EMPLOYER_CELL_NO    VARCHAR2(200 BYTE),
  CLNT_EMPLOYER_PHONE_NO   VARCHAR2(200 BYTE),
  CLNT_BANK_CELL_NO        VARCHAR2(200 CHAR),
  CLNT_BANK_PHONE_NO       VARCHAR2(200 BYTE),
  CLNT_OCCUPATION          VARCHAR2(100 BYTE),
  CLNT_OLD_SHT_DESC        VARCHAR2(50 BYTE),
  CLNT_ANNIVERSARY         DATE,
  CLNT_CRDT_RATING         VARCHAR2(10 BYTE),
  CLNT_REG_DATE            DATE,
  CLTN_CLIENT_TYPES        VARCHAR2(200 BYTE),
  CLNT_DRV_EXPERIENCE      VARCHAR2(50 BYTE),
  CLNT_SACCO               VARCHAR2(20 BYTE),
  CLNT_REASON_UPDATED      VARCHAR2(200 BYTE),
  CLNT_PAYROLL_NO          NUMBER(23),
  CLNT_CREDIT_LIM_ALLOWED  VARCHAR2(1 BYTE),
  CLNT_CREDIT_LIMIT        NUMBER,
  CLNT_OCC_CODE            NUMBER,
  CLNT_ENT_CODE            NUMBER,
  CLNT_BOUNCED_CHQ         VARCHAR2(1 BYTE),
  CLNT_MARITAL_STATUS      VARCHAR2(2 BYTE),
  CLNT_BPN_CODE            NUMBER
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