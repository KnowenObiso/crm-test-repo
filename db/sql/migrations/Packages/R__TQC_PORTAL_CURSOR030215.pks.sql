--
-- TQC_PORTAL_CURSOR030215  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."TQC_PORTAL_CURSOR030215" AS
/******************************************************************************
   NAME:       TQ_CRM.TQC_PORTAL_CURSOR
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        9/20/2012             1. Created this package.
******************************************************************************/

  TYPE product_categories_rec IS RECORD(
  TWPC_CODE                 TQC_WEB_PRODUCT_CATEGORIES.TWPC_CODE%TYPE, 
  TWPC_SYS_CODE             TQC_WEB_PRODUCT_CATEGORIES.TWPC_SYS_CODE%TYPE, 
  TWPC_NAME                 TQC_WEB_PRODUCT_CATEGORIES.TWPC_NAME%TYPE, 
  TWPC_DESCRIPTION          TQC_WEB_PRODUCT_CATEGORIES.TWPC_DESCRIPTION%TYPE);
  
  TYPE product_categories_ref IS REF CURSOR RETURN product_categories_rec;
  FUNCTION  getProductCategories(v_sysCode  NUMBER) RETURN product_categories_ref;
  
  TYPE products_rec IS RECORD(
  TWP_CODE                  TQC_WEB_PRODUCTS.TWP_CODE%TYPE, 
  TWP_TWPC_CODE             TQC_WEB_PRODUCTS.TWP_TWPC_CODE%TYPE, 
  TWP_PROD_CODE             TQC_WEB_PRODUCTS.TWP_PROD_CODE%TYPE,
  PROD_NAME                 VARCHAR2(50), 
  TWP_PROD_DESC             TQC_WEB_PRODUCTS .TWP_PROD_DESC%TYPE,
   TWP_BIND_CODE             TQC_WEB_PRODUCTS .TWP_BIND_CODE%TYPE,
    TWP_BINDER             TQC_WEB_PRODUCTS .TWP_BINDER%TYPE,
     BIND_NAME             gin_binders.BIND_NAME%TYPE,
     AGN_SHT_DESC             TQC_AGENCIES .AGN_SHT_DESC%TYPE,
      TWP_AGA_CODE             TQC_WEB_PRODUCTS .TWP_AGA_CODE%TYPE,
      AGA_CODE             TQC_AGENCY_ACCOUNTS .AGA_CODE%TYPE,
      AGA_NAME             TQC_AGENCY_ACCOUNTS .AGA_NAME%TYPE);
  TYPE products_ref IS REF CURSOR RETURN products_rec;
  FUNCTION getProducts (v_twpcCode   NUMBER,v_sysCode NUMBER DEFAULT NULL) RETURN products_ref;
  
  TYPE product_options_rec IS RECORD(
  TWPO_CODE                 TQC_WEB_PROD_OPTIONS.TWPO_CODE%TYPE, 
  TWPO_POP_CODE             TQC_WEB_PROD_OPTIONS.TWPO_POP_CODE%TYPE, 
  TWPO_DESC                 TQC_WEB_PROD_OPTIONS.TWPO_DESC%TYPE,
 -- POP_DESC                  LMS_PROD_OPTIONS.POP_DESC%TYPE,
  BIND_NAME                  GIN_BINDERS.BIND_NAME%TYPE);
  
  TYPE product_options_ref IS REF CURSOR RETURN product_options_rec;
  FUNCTION getProductOption(v_prodCode  NUMBER,v_sysCode NUMBER DEFAULT NULL) RETURN product_options_ref;
  PROCEDURE getLifeQuoteOutput(v_quoteCode      IN  NUMBER,
                               v_totPremium     OUT NUMBER,
                               v_totSA          OUT NUMBER);
  TYPE dynamic_values_rec IS RECORD(
  TSMSD_PROMPT              TQC_SYS_MOD_SUBUNITS_DETAILS.TSMSD_PROMPT%TYPE,
  TSMSI_VALUE               TQC_SYS_MOD_SUBUNITS_INPUTS.TSMSI_VALUE%TYPE, 
  TSMSI_MODE_CODE           TQC_SYS_MOD_SUBUNITS_INPUTS.TSMSI_MODE_CODE%TYPE, 
  TSMSI_ROW                 TQC_SYS_MOD_SUBUNITS_INPUTS.TSMSI_ROW%TYPE, 
  TSMSI_COLUMN              TQC_SYS_MOD_SUBUNITS_INPUTS.TSMSI_COLUMN%TYPE,
  TSMSI_CODE                TQC_SYS_MOD_SUBUNITS_INPUTS.TSMSI_CODE%TYPE,
  TMSC_COLUMN_NAME          TQC_MOD_SYS_COLUMNS.TMSC_COLUMN_NAME%TYPE, 
  TMSC_TABLE_NAME           TQC_MOD_SYS_COLUMNS.TMSC_TABLE_NAME%TYPE);
  
  TYPE dynamic_values_ref IS REF CURSOR RETURN dynamic_values_rec;
  FUNCTION getDynamicValues(v_mod   VARCHAR2,
                            v_pkid  NUMBER) RETURN dynamic_values_ref;
                            
  TYPE sys_prod_rec IS RECORD(
  PROD_CODE                 LMS_PRODUCTS.PROD_CODE%TYPE,
  PROD_SHT_DESC             LMS_PRODUCTS.PROD_SHT_DESC%TYPE,
  PROD_DESC                 LMS_PRODUCTS.PROD_DESC%TYPE);
  
  TYPE sys_prod_ref IS REF CURSOR RETURN sys_prod_rec;
  FUNCTION getLifeProducts(v_syscode IN NUMBER DEFAULT NULL) RETURN sys_prod_ref;
  
  TYPE sys_prod_options_rec IS RECORD(
  --POP_CODE                  LMS_PROD_OPTIONS.POP_CODE%TYPE, 
  POP_SHT_DESC              LMS_PROD_OPTIONS.POP_SHT_DESC%TYPE, 
  POP_DESC                  LMS_PROD_OPTIONS.POP_DESC%TYPE);
  
  TYPE sys_prod_options_ref IS REF CURSOR RETURN sys_prod_options_rec;
  FUNCTION getProdOptions(v_prodCode    NUMBER,v_sysCode NUMBER DEFAULT NULL)RETURN sys_prod_options_ref;
  
  TYPE portfolio_rec IS RECORD(
  POL_CODE_BATCH_NO         NUMBER,
  SYS_CODE                  NUMBER,
  POLICY_NUMBER             VARCHAR2(50),
  PROP_QUOTE_NO             VARCHAR2(50),
  PROD_DESC                 VARCHAR2(100),
  COVER_FROM                DATE,
  COVER_TO                  DATE,  
  AMOUNT_INSURED            NUMBER,
  STATUS                    VARCHAR2(5),
  DONE_BY                   VARCHAR2(100),
  POL_INCEPTION_DT DATE,
  QUOT_OK VARCHAR2(30),
  PROD_TYPE VARCHAR2(30));
  
  TYPE portfolio_ref IS REF CURSOR RETURN portfolio_rec;
  FUNCTION getClientPortfolio(v_clientCode  NUMBER,v_userCode   NUMBER)RETURN portfolio_ref;
  
  TYPE claims_rec IS RECORD(
  POLICY_NO                 VARCHAR2(50),
  PROPERTY_DESC             VARCHAR2(100),
  LOSS_DATE                 DATE,
  NOTIFICATION_DATE         DATE,
  DESCRIPTION               VARCHAR2(250),
  CLAIM_NO                  VARCHAR2(50),
  STATUS                    VARCHAR2(50));
  
  TYPE claims_ref IS REF CURSOR RETURN claims_rec;
  FUNCTION getClientClaims(v_clientCode     NUMBER,v_userCode   NUMBER)RETURN claims_ref;
  
  TYPE systems_rec IS RECORD(

  SYS_CODE                 TQC_SYSTEMS.SYS_CODE%TYPE, 
  SYS_NAME             TQC_SYSTEMS.SYS_NAME%TYPE);
  
  TYPE systems_ref IS REF CURSOR RETURN systems_rec;
  FUNCTION  getSystems RETURN systems_ref;
  
  TYPE serv_requests_rec IS RECORD(
TSR_CODE                        TQC_SERV_REQUESTS.TSR_CODE%TYPE, 
TSR_TSRC_CODE                   TQC_SERV_REQUESTS.TSR_TSRC_CODE%TYPE, 
TSR_TYPE                        TQC_SERV_REQUESTS.TSR_TYPE%TYPE, 
TSR_ACC_TYPE                    TQC_SERV_REQUESTS.TSR_ACC_TYPE%TYPE, 
TSR_ACC_CODE                    TQC_SERV_REQUESTS.TSR_ACC_CODE%TYPE, 
TSR_DATE                        TQC_SERV_REQUESTS.TSR_DATE%TYPE, 
TSR_ASSIGNEE                    TQC_SERV_REQUESTS.TSR_ASSIGNEE%TYPE, 
TSR_OWNER_TYPE                  TQC_SERV_REQUESTS.TSR_OWNER_TYPE%TYPE, 
TSR_OWNER_CODE                  TQC_SERV_REQUESTS.TSR_OWNER_CODE%TYPE, 
TSR_STATUS                      TQC_SERV_REQUESTS.TSR_STATUS%TYPE, 
TSR_DUE_DATE                    TQC_SERV_REQUESTS.TSR_DUE_DATE%TYPE, 
TSR_RESOLUTION_DATE             TQC_SERV_REQUESTS.TSR_RESOLUTION_DATE%TYPE, 
TSR_SUMMARY                     TQC_SERV_REQUESTS.TSR_SUMMARY%TYPE, 
TSR_DESCRIPTION                 TQC_SERV_REQUESTS.TSR_DESCRIPTION%TYPE, 
TSR_SOLUTION                    TQC_SERV_REQUESTS.TSR_SOLUTION%TYPE,
TSRC_NAME                       TQC_SERV_REQ_CAT.TSRC_NAME%TYPE,
ACC_TYPE                        VARCHAR2(20),
ASSIGNEE                        VARCHAR2(100),
ACCOUNT_NAME                    VARCHAR2(100),
OWNER_TYPE                      VARCHAR2(20),
OWNER                           VARCHAR2(100));


TYPE serv_requests_ref IS REF CURSOR RETURN  serv_requests_rec;
FUNCTION getClientAllOpenRequests(v_clientCode     NUMBER,v_userCode    NUMBER) RETURN serv_requests_ref;


TYPE web_prod_settings_rec IS RECORD(
TWPD_CLNT_CODE                  TQC_WEB_PRODUCT_DETAILS.TWPD_CLNT_CODE%TYPE, 
TWPD_USR_CODE                   TQC_WEB_PRODUCT_DETAILS.TWPD_USR_CODE%TYPE, 
TWPD_USERNAME                   TQC_WEB_PRODUCT_DETAILS.TWPD_USERNAME%TYPE, 
TWPD_DR_LIMIT                   TQC_WEB_PRODUCT_DETAILS.TWPD_DR_LIMIT%TYPE, 
TWPD_CR_LIMIT                   TQC_WEB_PRODUCT_DETAILS.TWPD_CR_LIMIT%TYPE,
AUTH                            VARCHAR2(1));

TYPE web_prod_settings_ref IS REF CURSOR RETURN web_prod_settings_rec;
FUNCTION getProdSettings(v_quoteCode     NUMBER,v_sysCode NUMBER,v_clientCode    NUMBER) RETURN web_prod_settings_ref;

 FUNCTION getSystemActive(v_sysCode        NUMBER) RETURN VARCHAR2; 

END TQC_PORTAL_CURSOR030215; 
 
/