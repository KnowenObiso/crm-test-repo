/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_PORTAL_PKG030215  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."TQC_PORTAL_PKG030215" AS
/******************************************************************************
   NAME:       TQ_CRM.TQC_PORTAL_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        26/May/2011             1. Created this package.
******************************************************************************/

  TYPE account_contacts_rec IS RECORD(
  ACCC_CODE                 TQC_ACCOUNT_CONTACTS.ACCC_CODE%TYPE,
  ACCC_USERNAME             TQC_ACCOUNT_CONTACTS.ACCC_USERNAME%TYPE,
  ACCC_NAME                 TQC_ACCOUNT_CONTACTS.ACCC_NAME%TYPE,
  ACCC_LAST_LOGIN_DATE      TQC_ACCOUNT_CONTACTS.ACCC_LAST_LOGIN_DATE%TYPE,
  ACCC_STATUS               TQC_ACCOUNT_CONTACTS.ACCC_STATUS%TYPE, 
  ACCC_PWD                  TQC_ACCOUNT_CONTACTS.ACCC_PWD%TYPE,
  ACCC_AGN_CODE             TQC_ACCOUNT_CONTACTS.ACCC_AGN_CODE%TYPE
  );
  
  TYPE account_contacts_ref IS REF CURSOR RETURN account_contacts_rec;
  FUNCTION getAccountContacts RETURN account_contacts_ref;
  
  TYPE products_rec IS RECORD(
  PROD_CODE                 LMS_PRODUCTS.PROD_CODE%TYPE,
  PROD_SHT_DESC             LMS_PRODUCTS.PROD_SHT_DESC%TYPE,
  PROD_DESC                 LMS_PRODUCTS.PROD_DESC%TYPE,
  PROD_TYPE                 LMS_PRODUCTS.PROD_TYPE%TYPE,
  PROD_YR_TO_MONTH_RATE     LMS_PRODUCTS.PROD_YR_TO_MONTH_RATE%TYPE, 
  PROD_YR_TO_QUATER_RATE    LMS_PRODUCTS.PROD_YR_TO_QUATER_RATE%TYPE, 
  PROD_YR_TO_S_ANNL_RATE    LMS_PRODUCTS.PROD_YR_TO_S_ANNL_RATE%TYPE, 
  PROD_MNTH_TO_QTR_FCTOR    LMS_PRODUCTS.PROD_MNTH_TO_QTR_FCTOR%TYPE, 
  PROD_MNTH_TO_S_ANNL_FCTOR LMS_PRODUCTS.PROD_MNTH_TO_S_ANNL_FCTOR%TYPE, 
  PROD_MNTH_TO_ANNL_FCTOR   LMS_PRODUCTS.PROD_MNTH_TO_ANNL_FCTOR%TYPE);
  
  TYPE products_ref IS REF CURSOR RETURN products_rec;
  FUNCTION getProducts RETURN products_ref;
  
  TYPE prod_options_rec IS RECORD(
  POP_CODE                  LMS_PROD_OPTIONS.POP_CODE%TYPE,
  POP_PROD_CODE             LMS_PROD_OPTIONS.POP_PROD_CODE%TYPE, 
  POP_SHT_DESC              LMS_PROD_OPTIONS.POP_SHT_DESC%TYPE, 
  POP_DESC                  LMS_PROD_OPTIONS.POP_DESC%TYPE); 
  
  
  TYPE prod_options_ref IS REF CURSOR RETURN prod_options_rec;
  FUNCTION getProductOptions RETURN prod_options_ref;
  
  
  TYPE prod_prem_masks_rec IS RECORD(
  PMAS_CODE                 LMS_PREMIUM_MASKS.PMAS_CODE%TYPE,
  PMAS_PROD_CODE            LMS_PREMIUM_MASKS.PMAS_PROD_CODE%TYPE, 
  PMAS_SHT_DESC             LMS_PREMIUM_MASKS.PMAS_SHT_DESC%TYPE, 
  PMAS_DESC                 LMS_PREMIUM_MASKS.PMAS_DESC%TYPE);
  
  
  TYPE prod_prem_masks_ref IS REF CURSOR RETURN prod_prem_masks_rec;
  FUNCTION getProdPremMasks RETURN prod_prem_masks_ref;
  
  PROCEDURE create_clnt_proc(v_accountCode      IN  NUMBER,
                            v_first_name        IN  VARCHAR2,
                            v_middle_name       IN  VARCHAR2,
                            v_surname           IN  VARCHAR2,
                            v_sysCode           IN  NUMBER,
                            v_username          IN  VARCHAR2,
                            v_clientCode        OUT NUMBER,
                            v_clnt_id_reg_no IN VARCHAR2,
                            v_clnt_bank_acc_no IN VARCHAR2,
                            v_clnt_twn_code IN NUMBER) ;
  
  
  PROCEDURE categories_proc(v_addEdit       VARCHAR2,
                            v_sysCode       NUMBER,
                            v_twpcCode      NUMBER,
                            v_twpcName      VARCHAR2,
                            v_twpcDesc      VARCHAR2);
                            
   PROCEDURE products_proc(v_addEdit         VARCHAR2,
                          v_twpCode         NUMBER,
                          v_twpcCode        NUMBER,
                          v_prodCode        NUMBER,
                          v_prodDesc        VARCHAR2,
                          v_twp_binder IN VARCHAR2 DEFAULT NULL,
                          v_twp_bind_code IN NUMBER DEFAULT NULL,
                          v_twp_aga_code IN NUMBER DEFAULT NULL,
                          v_twp_agn_code IN NUMBER DEFAULT NULL) ;
   PROCEDURE options_proc(v_addEdit          VARCHAR2,
                         v_twpoCode         NUMBER,
                         v_popCode          NUMBER,
                         v_twpoDesc         VARCHAR2,
                         v_twpo_bind_code IN NUMBER,
                         v_twpo_twp_code IN NUMBER) ;
PROCEDURE authenticateAccount( v_account_number IN VARCHAR2,v_exists OUT VARCHAR2);
PROCEDURE authenticateIdNumber( v_idNumber IN VARCHAR2,v_exists OUT VARCHAR2);

PROCEDURE updateUserDetails (
                            v_clnt_code         NUMBER,
                            v_postal_addrs      VARCHAR2,
                            v_prp_id_reg_no     VARCHAR2,
                            v_physical_addrs    VARCHAR2,
                            v_othernames        VARCHAR2,
                            v_name           VARCHAR2,
                            v_title             VARCHAR2,
                            v_dob               DATE,
                            v_email_addrs       VARCHAR2,
                            v_tel               VARCHAR2
                            );
    TYPE client_dtls_rec IS RECORD(
  CLNT_CODE                    TQC_CLIENTS.CLNT_CODE%TYPE, 
  CLNT_POSTAL_ADDRS            TQC_CLIENTS.CLNT_POSTAL_ADDRS%TYPE, 
  CLNT_ID_REG_NO               TQC_CLIENTS.CLNT_ID_REG_NO%TYPE,
  CLNT_PHYSICAL_ADDRS          TQC_CLIENTS.CLNT_PHYSICAL_ADDRS%TYPE, 
  CLNT_OTHER_NAMES             TQC_CLIENTS.CLNT_OTHER_NAMES%TYPE,
  CLNT_NAME                 TQC_CLIENTS.CLNT_NAME%TYPE,
  CLNT_TITLE                   TQC_CLIENTS.CLNT_TITLE%TYPE,
  CLNT_DOB                     TQC_CLIENTS.CLNT_DOB%TYPE,
  CLNT_EMAIL_ADDRS             TQC_CLIENTS.CLNT_EMAIL_ADDRS%TYPE,
  CLNT_TEL                     TQC_CLIENTS.CLNT_TEL%TYPE,
  CLNT_PASSPORT_NO                     TQC_CLIENTS.CLNT_PASSPORT_NO%TYPE,
  CLNT_CNTCT_NAME_1                     TQC_CLIENTS.CLNT_CNTCT_NAME_1%TYPE,
  CLNT_CNTCT_PHONE_1                     TQC_CLIENTS.CLNT_CNTCT_PHONE_1%TYPE
  );
  
  TYPE client_dtls_ref IS REF CURSOR RETURN client_dtls_rec;
  
  FUNCTION getClientDtls(v_clnt_code  NUMBER)RETURN client_dtls_ref;
  
  
END TQC_PORTAL_PKG030215; 
 
/