--
-- TQC_AGENCY_LOADING_TEMP  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_LOADING_TEMP
(
  AGN_ACCOUNT_CODE    VARCHAR2(200 BYTE),
  AGN_ACCOUNT_TYPE    VARCHAR2(200 BYTE),
  AGN_ACCOUNT_NAME    VARCHAR2(200 BYTE),
  AGN_ADDRESS1        VARCHAR2(200 BYTE),
  AGN_ADDRESS2        VARCHAR2(200 BYTE),
  AGN_CITY_TOWN_CODE  VARCHAR2(200 BYTE),
  AGN_GENDER          VARCHAR2(200 BYTE),
  AGN_CONTACT_PERSON  VARCHAR2(200 BYTE),
  AGN_PHONE_NUMBER    VARCHAR2(200 BYTE),
  AGN_FAX_NUMBER      VARCHAR2(200 BYTE),
  AGN_EMAIL_ADDRESS   VARCHAR2(200 BYTE),
  AGN_JOINING_DATE    DATE,
  AGN_POSTED_ON       DATE,
  AGN_BRANCH          VARCHAR2(200 BYTE),
  AGN_OLD_BRANCH      VARCHAR2(200 BYTE),
  AGN_NL_CODE         VARCHAR2(200 BYTE),
  AGN_ID              NUMBER
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