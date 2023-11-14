/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_PORTAL_PKG03112015  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."TQC_PORTAL_PKG03112015"
AS
/******************************************************************************
   NAME:       TQ_CRM.TQC_PORTAL_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        26/May/2011             1. Created this package.
******************************************************************************/
   TYPE account_contacts_rec IS RECORD (
      accc_code              tqc_account_contacts.accc_code%TYPE,
      accc_username          tqc_account_contacts.accc_username%TYPE,
      accc_name              tqc_account_contacts.accc_name%TYPE,
      accc_last_login_date   tqc_account_contacts.accc_last_login_date%TYPE,
      accc_status            tqc_account_contacts.accc_status%TYPE,
      accc_pwd               tqc_account_contacts.accc_pwd%TYPE,
      accc_agn_code          tqc_account_contacts.accc_agn_code%TYPE
   );

   TYPE account_contacts_ref IS REF CURSOR
      RETURN account_contacts_rec;

   FUNCTION getaccountcontacts
      RETURN account_contacts_ref;

   TYPE products_rec IS RECORD (
      prod_code                   lms_products.prod_code%TYPE,
      prod_sht_desc               lms_products.prod_sht_desc%TYPE,
      prod_desc                   lms_products.prod_desc%TYPE,
      prod_type                   lms_products.prod_type%TYPE,
      prod_yr_to_month_rate       lms_products.prod_yr_to_month_rate%TYPE,
      prod_yr_to_quater_rate      lms_products.prod_yr_to_quater_rate%TYPE,
      prod_yr_to_s_annl_rate      lms_products.prod_yr_to_s_annl_rate%TYPE,
      prod_mnth_to_qtr_fctor      lms_products.prod_mnth_to_qtr_fctor%TYPE,
      prod_mnth_to_s_annl_fctor   lms_products.prod_mnth_to_s_annl_fctor%TYPE,
      prod_mnth_to_annl_fctor     lms_products.prod_mnth_to_annl_fctor%TYPE
   );

   TYPE products_ref IS REF CURSOR
      RETURN products_rec;

   FUNCTION getproducts
      RETURN products_ref;

   TYPE prod_options_rec IS RECORD (
      pop_code        lms_prod_options.pop_code%TYPE,
      pop_prod_code   lms_prod_options.pop_prod_code%TYPE,
      pop_sht_desc    lms_prod_options.pop_sht_desc%TYPE,
      pop_desc        lms_prod_options.pop_desc%TYPE
   );

   TYPE prod_options_ref IS REF CURSOR
      RETURN prod_options_rec;

   FUNCTION getproductoptions
      RETURN prod_options_ref;

   TYPE prod_prem_masks_rec IS RECORD (
      pmas_code        lms_premium_masks.pmas_code%TYPE,
      pmas_prod_code   lms_premium_masks.pmas_prod_code%TYPE,
      pmas_sht_desc    lms_premium_masks.pmas_sht_desc%TYPE,
      pmas_desc        lms_premium_masks.pmas_desc%TYPE
   );

   TYPE prod_prem_masks_ref IS REF CURSOR
      RETURN prod_prem_masks_rec;

   FUNCTION getprodpremmasks
      RETURN prod_prem_masks_ref;

   PROCEDURE create_clnt_proc (
      v_accountcode        IN       NUMBER,
      v_first_name         IN       VARCHAR2,
      v_middle_name        IN       VARCHAR2,
      v_surname            IN       VARCHAR2,
      v_syscode            IN       NUMBER,
      v_username           IN       VARCHAR2,
      v_clientcode         OUT      NUMBER,
      v_clnt_id_reg_no     IN       VARCHAR2,
      v_clnt_bank_acc_no   IN       VARCHAR2,
      v_clnt_twn_code      IN       NUMBER
   );

   PROCEDURE categories_proc (
      v_addedit    VARCHAR2,
      v_syscode    NUMBER,
      v_twpccode   NUMBER,
      v_twpcname   VARCHAR2,
      v_twpcdesc   VARCHAR2
   );

   PROCEDURE products_proc (
      v_addedit              VARCHAR2,
      v_twpcode              NUMBER,
      v_twpccode             NUMBER,
      v_prodcode             NUMBER,
      v_proddesc             VARCHAR2,
      v_twp_binder      IN   VARCHAR2 DEFAULT NULL,
      v_twp_bind_code   IN   NUMBER DEFAULT NULL,
      v_twp_aga_code    IN   NUMBER DEFAULT NULL,
      v_twp_agn_code    IN   NUMBER DEFAULT NULL
   );

   PROCEDURE options_proc (
      v_addedit               VARCHAR2,
      v_twpocode              NUMBER,
      v_popcode               NUMBER,
      v_twpodesc              VARCHAR2,
      v_twpo_bind_code   IN   NUMBER,
      v_twpo_twp_code    IN   NUMBER
   );

   PROCEDURE authenticateaccount (
      v_account_number   IN       VARCHAR2,
      v_exists           OUT      VARCHAR2
   );

   PROCEDURE authenticateidnumber (
      v_idnumber   IN       VARCHAR2,
      v_exists     OUT      VARCHAR2
   );

--   TYPE client_dtls_rec IS RECORD (
--      clnt_code             tqc_clients.clnt_code%TYPE,
--      clnt_postal_addrs     tqc_clients.clnt_postal_addrs%TYPE,
--      clnt_id_reg_no        tqc_clients.clnt_id_reg_no%TYPE,
--      clnt_physical_addrs   tqc_clients.clnt_physical_addrs%TYPE,
--      clnt_other_names      tqc_clients.clnt_other_names%TYPE,
--      clnt_surname          tqc_clients.clnt_surname%TYPE,
--      clnt_title            tqc_clients.clnt_title%TYPE,
--      clnt_dob              tqc_clients.clnt_dob%TYPE,
--      clnt_email_addrs      tqc_clients.clnt_email_addrs%TYPE,
--      clnt_tel              tqc_clients.clnt_tel%TYPE
--   );
   
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

   TYPE client_dtls_ref IS REF CURSOR
      RETURN client_dtls_rec;
FUNCTION getClientDtls(v_clnt_code  NUMBER)RETURN client_dtls_ref;

   PROCEDURE updateuserdetails (
      v_clnt_code        NUMBER,
      v_postal_addrs     VARCHAR2,
      v_prp_id_reg_no    VARCHAR2,
      v_physical_addrs   VARCHAR2,
      v_othernames       VARCHAR2,
      v_surname          VARCHAR2,
      v_title            VARCHAR2,
      v_dob              DATE,
      v_email_addrs      VARCHAR2,
      v_tel              VARCHAR2
   );
END tqc_portal_pkg03112015;
 
/