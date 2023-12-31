--
-- TQC_AGENCIES_TEMP  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCIES_TEMP
(
  AGNL_CODE                  NUMBER,
  AGNL_AGN_ACT_CODE          VARCHAR2(200 BYTE),
  AGNL_ACCOUNT_TYPE          VARCHAR2(200 BYTE),
  AGNL_AGN_ACC_NAME          VARCHAR2(200 BYTE),
  AGNL_AGN_PHYSICAL_ADDRESS  VARCHAR2(200 BYTE),
  AGNL_AGN_POSTAL_ADDRESS    VARCHAR2(200 BYTE),
  AGNL_AGN_TWN_NAME          VARCHAR2(200 BYTE),
  AGNL_AGN_REG_CODE          VARCHAR2(200 BYTE),
  AGNL_AGN_CONTACT_PERSON    VARCHAR2(200 BYTE),
  AGNL_AGN_TEL1              VARCHAR2(200 BYTE),
  AGNL_AGN_FAX               VARCHAR2(200 BYTE),
  AGNL_AGN_EMAIL_ADDRESS     VARCHAR2(200 BYTE),
  AGNL_AGN_DATE_CREATED      DATE,
  AGNL_AGN_CHECK_DATE        DATE,
  AGNL_LOADED                VARCHAR2(30 BYTE),
  AGNL_AGN_BRANCH_NAME       VARCHAR2(200 BYTE)
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