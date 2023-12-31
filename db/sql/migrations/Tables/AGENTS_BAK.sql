--
-- AGENTS_BAK  (Table) 
--
CREATE TABLE TQ_CRM.AGENTS_BAK
(
  AGN_CODE               NUMBER(15)             NOT NULL,
  AGN_ACT_CODE           NUMBER(8),
  AGN_SHT_DESC           VARCHAR2(15 BYTE)      NOT NULL,
  AGN_NAME               VARCHAR2(100 BYTE)     NOT NULL,
  AGN_PHYSICAL_ADDRESS   VARCHAR2(250 BYTE),
  AGN_POSTAL_ADDRESS     VARCHAR2(50 BYTE),
  AGN_TWN_CODE           NUMBER(8),
  AGN_COU_CODE           NUMBER(8),
  AGN_EMAIL_ADDRESS      VARCHAR2(250 BYTE),
  AGN_WEB_ADDRESS        VARCHAR2(50 BYTE),
  AGN_ZIP                VARCHAR2(15 BYTE),
  AGN_CONTACT_PERSON     VARCHAR2(60 BYTE),
  AGN_CONTACT_TITLE      VARCHAR2(60 BYTE),
  AGN_TEL1               VARCHAR2(100 BYTE),
  AGN_TEL2               VARCHAR2(100 BYTE),
  AGN_FAX                VARCHAR2(100 BYTE),
  AGN_ACC_NO             VARCHAR2(20 BYTE),
  AGN_PIN                VARCHAR2(25 BYTE),
  AGN_AGENT_COMMISSION   NUMBER(22,5),
  AGN_CREDIT_ALLOWED     CHAR(1 BYTE),
  AGN_AGENT_WHT_TAX      NUMBER(22,5),
  AGN_PRINT_DBNOTE       CHAR(1 BYTE),
  AGN_STATUS             VARCHAR2(10 BYTE),
  AGN_DATE_CREATED       DATE,
  AGN_CREATED_BY         VARCHAR2(40 BYTE),
  AGN_REG_CODE           VARCHAR2(15 BYTE),
  AGN_COMM_RESERVE_RATE  NUMBER(6),
  AGN_ANNUAL_BUDGET      NUMBER(15),
  AGN_STATUS_EFF_DATE    DATE,
  AGN_CREDIT_PERIOD      NUMBER(10),
  AGN_COMM_STAT_EFF_DT   DATE,
  AGN_COMM_STATUS_DT     DATE,
  AGN_COMM_ALLOWED       VARCHAR2(1 BYTE),
  AGN_CHECKED            VARCHAR2(1 BYTE),
  AGN_CHECKED_BY         VARCHAR2(30 BYTE),
  AGN_CHECK_DATE         DATE,
  AGN_COMP_COMM_ARREARS  VARCHAR2(1 BYTE),
  AGN_REINSURER          VARCHAR2(1 BYTE),
  AGN_BRN_CODE           NUMBER(8),
  AGN_TOWN               VARCHAR2(50 BYTE),
  AGN_COUNTRY            VARCHAR2(30 BYTE),
  AGN_STATUS_DESC        VARCHAR2(30 BYTE),
  AGN_ID_NO              VARCHAR2(20 BYTE),
  AGN_CON_CODE           VARCHAR2(10 BYTE),
  AGN_AGN_CODE           NUMBER(10),
  AGN_SMS_TEL            VARCHAR2(35 BYTE),
  AGN_AHC_CODE           NUMBER(15),
  AGN_SEC_CODE           NUMBER(8),
  AGN_AGNC_CLASS_CODE    VARCHAR2(25 BYTE),
  AGN_EXPIRY_DATE        DATE,
  AGN_LICENSE_NO         VARCHAR2(25 BYTE),
  AGN_RUNOFF             CHAR(1 BYTE)           NOT NULL,
  AGN_LICENSED           VARCHAR2(1 BYTE),
  AGN_LICENSE_GRACE_PR   NUMBER(10),
  AGN_OLD_ACC_NO         VARCHAR2(20 BYTE),
  AGN_STATUS_REMARKS     VARCHAR2(20 BYTE),
  AGN_OSD_CODE           VARCHAR2(10 BYTE)
)
TABLESPACE CRMDATA
PCTUSED    40
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          3M
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