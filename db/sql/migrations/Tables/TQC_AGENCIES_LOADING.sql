--
-- TQC_AGENCIES_LOADING  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCIES_LOADING
(
  AGNL_CODE                  NUMBER             NOT NULL,
  AGNL_AGN_ACT_CODE          NUMBER,
  AGNL_AGN_SHT_DESC          VARCHAR2(200 BYTE),
  AGNL_AGN_NAME              VARCHAR2(200 BYTE),
  AGNL_AGN_PHYSICAL_ADDRESS  VARCHAR2(200 BYTE),
  AGNL_AGN_POSTAL_ADDRESS    VARCHAR2(200 BYTE),
  AGNL_AGN_TWN_CODE          NUMBER,
  AGNL_AGN_COU_CODE          NUMBER,
  AGNL_AGN_EMAIL_ADDRESS     VARCHAR2(200 BYTE),
  AGNL_AGN_CONTACT_PERSON    VARCHAR2(200 BYTE),
  AGNL_AGN_ACC_NO            VARCHAR2(200 BYTE),
  AGNL_AGN_CREDIT_ALLOWED    VARCHAR2(200 BYTE),
  AGNL_AGN_DATE_CREATED      DATE,
  AGNL_AGN_CREATED_BY        VARCHAR2(200 BYTE),
  AGNL_AGN_BRN_CODE          NUMBER,
  AGNL_AGN_RUNOFF            VARCHAR2(200 BYTE),
  AGNL_AGN_CRDT_RTING        VARCHAR2(200 BYTE),
  AGNL_LOADED                VARCHAR2(1 BYTE),
  AGNL_SYSTEM                VARCHAR2(200 BYTE)
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