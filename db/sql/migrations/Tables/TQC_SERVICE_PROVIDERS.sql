--
-- TQC_SERVICE_PROVIDERS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SERVICE_PROVIDERS
(
  SPR_CODE               NUMBER(8)              NOT NULL,
  SPR_SHT_DESC           VARCHAR2(15 BYTE)      NOT NULL,
  SPR_NAME               VARCHAR2(100 BYTE)     NOT NULL,
  SPR_PHYSICAL_ADDRESS   VARCHAR2(300 BYTE),
  SPR_POSTAL_ADDRESS     VARCHAR2(300 BYTE),
  SPR_TWN_CODE           NUMBER(8),
  SPR_COU_CODE           NUMBER(8),
  SPR_SPT_CODE           NUMBER(8),
  SPR_PHONE              VARCHAR2(20 BYTE),
  SPR_FAX                VARCHAR2(10 BYTE),
  SPR_EMAIL              VARCHAR2(100 BYTE),
  SPR_TITLE              VARCHAR2(8 BYTE),
  SPR_ZIP                VARCHAR2(10 BYTE),
  SPR_WEF                DATE,
  SPR_WET                DATE,
  SPR_CONTACT            VARCHAR2(100 BYTE),
  SPR_AIMS_CODE          VARCHAR2(5 BYTE),
  SPR_BBR_CODE           NUMBER(15),
  SPR_BANK_ACC_NO        VARCHAR2(15 BYTE),
  SPR_CREATED_BY         VARCHAR2(30 BYTE),
  SPR_DATE_CREATED       DATE,
  SPR_STATUS_REMARKS     VARCHAR2(300 BYTE),
  SPR_STATUS             VARCHAR2(1 BYTE)       DEFAULT 'Y'                   NOT NULL,
  SPR_PIN_NUMBER         VARCHAR2(20 BYTE),
  SPR_TRS_OCCUPATION     VARCHAR2(50 BYTE),
  SPR_PROFF_BODY         VARCHAR2(100 BYTE),
  SPR_PIN                VARCHAR2(20 BYTE),
  SPR_DOC_PHONE          VARCHAR2(100 BYTE),
  SPR_DOC_EMAIL          VARCHAR2(100 BYTE),
  SPR_VAL_AMT            NUMBER(20),
  SPR_MOBILE_NO          VARCHAR2(50 BYTE),
  SPR_GL_ACC_NO          VARCHAR2(30 BYTE),
  SPR_INHOUSE            VARCHAR2(1 BYTE)       DEFAULT 'N',
  SPR_SMS_NUMBER         VARCHAR2(30 BYTE),
  SPR_VAT_NO             VARCHAR2(15 BYTE),
  SPR_INVOICE_NUMBER     VARCHAR2(200 BYTE),
  SPR_CONTACT_PERSON     VARCHAR2(50 BYTE),
  SPR_CONT_PERSON_PHONE  VARCHAR2(20 BYTE),
  SPR_CLNT_CODE          NUMBER,
  SPR_BPN_CODE           NUMBER,
  SPR_TEL_PAY            VARCHAR2(35 BYTE),
  SPR_CONTACT_EMAIL      VARCHAR2(50 BYTE),
  SPR_CONTACT_TEL        VARCHAR2(50 BYTE),
  SPR_DEFAULT_PROVIDER   VARCHAR2(10 BYTE)      DEFAULT 'N',
  SPR_POSTAL_CODE        VARCHAR2(20 BYTE),
  SPR_GENDER             VARCHAR2(1 BYTE),
  SPR_PASSPORT_NO        VARCHAR2(80 BYTE),
  SPR_ID_TYPE            VARCHAR2(20 BYTE),
  SPR_ID_NO              VARCHAR2(80 BYTE),
  SPR_ENT_CODE           NUMBER(22),
  SPR_VALIDATION_SOURCE  VARCHAR2(80 BYTE),
  SPT_NEXT_NO            NUMBER(22),
  SPR_REG_NO             VARCHAR2(20 BYTE),
  SPR_IPRS_VALIDATED     VARCHAR2(1 BYTE),
  SPR_TYPE               VARCHAR2(20 BYTE)      DEFAULT 'WORKS'
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          192K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;