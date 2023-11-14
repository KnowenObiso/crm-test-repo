--
-- TQC_AGNT_LOADING_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_AGNT_LOADING_OBJ"                                          AS OBJECT
(
AGNL_AGN_ACT_CODE          VARCHAR(200), 
AGNL_ACCOUNT_TYPE           VARCHAR2(200),
AGNL_AGN_ACC_NAME           VARCHAR2(200),
AGNL_AGN_PHYSICAL_ADDRESS   VARCHAR2(200),
AGNL_AGN_POSTAL_ADDRESS     VARCHAR2(200),
AGNL_AGN_TWN_CODE           VARCHAR2(200),
AGNL_AGN_REG_CODE           VARCHAR2(200),
AGNL_AGN_CONTACT_PERSON     VARCHAR2(200),
AGNL_AGN_TEL1               VARCHAR2(200),
AGNL_AGN_FAX                VARCHAR2(200),
AGNL_AGN_EMAIL_ADDRESS      VARCHAR2(200),
AGNL_AGN_DATE_CREATED       DATE,
AGNL_AGN_CHECK_DATE         DATE,
AGNL_AGN_BRANCH_NAME      VARCHAR2(200)
); 
/