/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_CLIENTS_PKGBKP  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_clients_pkgbkp
AS
/******************************************************************************
   NAME:       TQC_CLIENTS_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        27/03/09             1. Created this package.
******************************************************************************/
   TYPE clients_rec IS RECORD (
      clnt_code           tqc_clients.clnt_code%TYPE,
      clnt_pin            tqc_clients.clnt_pin%TYPE,
      clnt_postal_addrs   tqc_clients.clnt_postal_addrs%TYPE,
      clnt_tel            tqc_clients.clnt_tel%TYPE,
      clnt_other_names    tqc_clients.clnt_other_names%TYPE,
      clnt_name           tqc_clients.clnt_surname%TYPE,
      clnt_id_reg_no      tqc_clients.clnt_id_reg_no%TYPE,
      clnt_sht_desc       tqc_clients.clnt_sht_desc%TYPE,
      clnt_zip_code       tqc_clients.clnt_zip_code%TYPE
   );

   TYPE clients_ref IS REF CURSOR
      RETURN clients_rec;

   TYPE clients_tab IS TABLE OF clients_rec
      INDEX BY BINARY_INTEGER;

   TYPE clients_record IS RECORD (
      clnt_code           tqc_clients.clnt_code%TYPE,
      clnt_pin            tqc_clients.clnt_pin%TYPE,
      clnt_postal_addrs   tqc_clients.clnt_postal_addrs%TYPE,
      clnt_tel            tqc_clients.clnt_tel%TYPE,
      clnt_other_names    tqc_clients.clnt_other_names%TYPE,
      clnt_name           tqc_clients.clnt_surname%TYPE,
      clnt_id_reg_no      tqc_clients.clnt_id_reg_no%TYPE,
      clnt_sht_desc       tqc_clients.clnt_sht_desc%TYPE,
      clnt_zip_code       tqc_clients.clnt_zip_code%TYPE,
      clnt_surname        tqc_clients.clnt_surname%TYPE
   );

   TYPE clients_ref_cursor IS REF CURSOR
      RETURN clients_record;

   TYPE clients_web_acc_rec IS RECORD (
      acwa_code            tqc_client_web_accounts.acwa_code%TYPE,
      acwa_username        tqc_client_web_accounts.acwa_username%TYPE,
      acwa_pwd             tqc_client_web_accounts.acwa_pwd%TYPE,
      acwa_login_allowed   tqc_client_web_accounts.acwa_login_allowed%TYPE,
      acwa_personel_rank   tqc_client_web_accounts.acwa_personel_rank%TYPE,
      acwa_dt_created      tqc_client_web_accounts.acwa_dt_created%TYPE,
      acwa_status          tqc_client_web_accounts.acwa_status%TYPE,
      acwa_clnt_code       tqc_client_web_accounts.acwa_clnt_code%TYPE,
      acwa_created_by      tqc_client_web_accounts.acwa_created_by%TYPE,
      acwa_name            tqc_client_web_accounts.acwa_name%TYPE,
      acwa_email_addrs     tqc_client_web_accounts.acwa_email_addrs%TYPE
   --CLNT_POSTAL_CODE TQC_CLIENTS.CLNT_POSTAL_CODE%TYPE
   );

   TYPE clients_web_acc_ref IS REF CURSOR
      RETURN clients_web_acc_rec;

   TYPE clients_web_acc_tab IS TABLE OF clients_web_acc_rec
      INDEX BY BINARY_INTEGER;

   TYPE client_accounts_rec IS RECORD (
      clna_code           tqc_client_accounts.clna_code%TYPE,
      clna_sht_desc       tqc_client_accounts.clna_sht_desc%TYPE,
      clna_name           tqc_client_accounts.clna_name%TYPE,
      clna_clnt_code      tqc_client_accounts.clna_clnt_code%TYPE,
      clna_created_by     tqc_client_accounts.clna_created_by%TYPE,
      clna_date_created   tqc_client_accounts.clna_date_created%TYPE,
      clna_status         tqc_client_accounts.clna_status%TYPE,
      clna_remarks        tqc_client_accounts.clna_remarks%TYPE,
      clna_wef            tqc_client_accounts.clna_wef%TYPE,
      clna_wet            tqc_client_accounts.clna_wet%TYPE,
      bdiv_code           tqc_brnch_divisions.bdiv_code%TYPE,
      brn_name            tqc_branches.brn_name%TYPE,
      div_name            tqc_divisions.div_name%TYPE
   );

   TYPE client_accounts_ref IS REF CURSOR
      RETURN client_accounts_rec;

   FUNCTION get_client_accounts (v_clnt_code IN NUMBER)
      RETURN client_accounts_ref;

   TYPE clients_dtls_rec IS RECORD (
      clnt_code                 tqc_clients.clnt_code%TYPE,
      clnt_sht_desc             tqc_clients.clnt_sht_desc%TYPE,
      clnt_name                 tqc_clients.clnt_name%TYPE,
      clnt_other_names          tqc_clients.clnt_other_names%TYPE,
      clnt_id_reg_no            tqc_clients.clnt_id_reg_no%TYPE,
      clnt_dob                  tqc_clients.clnt_dob%TYPE,
      clnt_pin                  tqc_clients.clnt_pin%TYPE,
      clnt_physical_addrs       tqc_clients.clnt_physical_addrs%TYPE,
      clnt_postal_addrs         tqc_clients.clnt_postal_addrs%TYPE,
      clnt_twn_code             tqc_clients.clnt_twn_code%TYPE,
      twn_name                  VARCHAR2 (50),
      clnt_cou_code             tqc_clients.clnt_cou_code%TYPE,
      cou_name                  VARCHAR2 (50),
      clnt_email_addrs          tqc_clients.clnt_email_addrs%TYPE,
      clnt_tel                  tqc_clients.clnt_tel%TYPE,
      clnt_tel2                 tqc_clients.clnt_tel2%TYPE,
      clnt_status               tqc_clients.clnt_status%TYPE,
      clnt_fax                  tqc_clients.clnt_fax%TYPE,
      clnt_remarks              tqc_clients.clnt_remarks%TYPE,
      clnt_spcl_terms           tqc_clients.clnt_spcl_terms%TYPE,
      clnt_declined_prop        tqc_clients.clnt_declined_prop%TYPE,
      clnt_increased_premium    tqc_clients.clnt_increased_premium%TYPE,
      clnt_policy_cancelled     tqc_clients.clnt_policy_cancelled%TYPE,
      clnt_proposer             tqc_clients.clnt_proposer%TYPE,
      clnt_accnt_no             tqc_clients.clnt_accnt_no%TYPE,
      clnt_domicile_countries   tqc_clients.clnt_domicile_countries%TYPE,
      clnt_wef                  tqc_clients.clnt_wef%TYPE,
      clnt_wet                  tqc_clients.clnt_wet%TYPE,
      clnt_withdrawal_reason    tqc_clients.clnt_withdrawal_reason%TYPE,
      clnt_sec_code             tqc_clients.clnt_sec_code%TYPE,
      clnt_surname              tqc_clients.clnt_surname%TYPE,
      clnt_type                 tqc_clients.clnt_type%TYPE,
      clnt_title                tqc_clients.clnt_title%TYPE,
      clnt_business             tqc_clients.clnt_business%TYPE,
      clnt_zip_code             tqc_clients.clnt_zip_code%TYPE,
      clnt_bbr_code             tqc_clients.clnt_bbr_code%TYPE,
      clnt_bank_acc_no          tqc_clients.clnt_bank_acc_no%TYPE,
      clnt_clnt_code            tqc_clients.clnt_clnt_code%TYPE,
      clnt_non_direct           tqc_clients.clnt_non_direct%TYPE,
      clnt_created_by           tqc_clients.clnt_created_by%TYPE,
      clnt_sms_tel              tqc_clients.clnt_sms_tel%TYPE,
      clnt_agnt_status          tqc_clients.clnt_agnt_status%TYPE,
      clnt_date_created         tqc_clients.clnt_date_created%TYPE,
      clnt_runoff               tqc_clients.clnt_runoff%TYPE,
      clnt_loaded_by            tqc_clients.clnt_loaded_by%TYPE,
      clnt_direct_client        tqc_clients.clnt_direct_client%TYPE,
      clnt_old_accnt_no         tqc_clients.clnt_old_accnt_no%TYPE,
      clnt_usr_code             tqc_clients.clnt_usr_code%TYPE,
      usr_name                  VARCHAR (100),
      clnt_cntct_phone_1        tqc_clients.clnt_cntct_phone_1%TYPE,
      clnt_cntct_email_1        tqc_clients.clnt_cntct_email_1%TYPE,
      clnt_cntct_phone_2        tqc_clients.clnt_cntct_phone_2%TYPE,
      clnt_cntct_email_2        tqc_clients.clnt_cntct_email_2%TYPE,
      sts_code                  NUMBER,
      sts_name                  VARCHAR2 (100),
      clnt_passport_no          VARCHAR2 (50),
      clnt_gender               VARCHAR2 (1),
      clnt_cntct_name_1         tqc_clients.clnt_cntct_name_1%TYPE,
      clnt_cntct_name_2         tqc_clients.clnt_cntct_name_2%TYPE,
      clt_cell_no               tqc_clients.clt_cell_no%TYPE,
      bank_branch               VARCHAR2 (100)
   );

   TYPE clients_dtls_ref IS REF CURSOR
      RETURN clients_dtls_rec;

   TYPE clients_dtls_tab IS TABLE OF clients_dtls_rec
      INDEX BY BINARY_INTEGER;

   TYPE clients_image_rec IS RECORD (
      clnt_image   tqc_clients.clnt_image%TYPE
   --CLNT_POSTAL_CODE TQC_CLIENTS.CLNT_POSTAL_CODE%TYPE
   );

   TYPE clients_image_ref IS REF CURSOR
      RETURN clients_image_rec;

   FUNCTION get_client_dtls (v_clnt_code IN NUMBER)
      RETURN clients_dtls_ref;

   FUNCTION get_client_dtls_bynames (
      v_surname                 tqc_clients.clnt_name%TYPE,
      v_other_name              tqc_clients.clnt_other_names%TYPE,
      v_clnt_passport_no   IN   VARCHAR2 DEFAULT NULL,
      v_clnt_pin           IN   VARCHAR2 DEFAULT NULL,
      v_clnt_id_reg_no     IN   VARCHAR2 DEFAULT NULL
   )
      RETURN clients_dtls_ref;

   PROCEDURE clients_qry (
      clnt_tab             IN OUT   clients_tab,
      v_clnt_first_name             VARCHAR2,
      v_clnt_middle_name            VARCHAR2,
      v_clnt_surname                VARCHAR2,
      v_clnt_post_add               VARCHAR2,
      v_clnt_id                     VARCHAR2,
      v_clnt_pin_no                 VARCHAR2,
      searchcriteria                VARCHAR2
   );

   FUNCTION clients_qry (
      v_clnt_first_name    VARCHAR2,
      v_clnt_middle_name   VARCHAR2,
      v_clnt_surname       VARCHAR2,
      v_clnt_post_add      VARCHAR2,
      v_clnt_id            VARCHAR2,
      v_clnt_pin_no        VARCHAR2,
      searchcriteria       VARCHAR2
   )
      RETURN clients_ref;

   TYPE clients_details_rec IS RECORD (
      clnt_code                 tqc_clients.clnt_code%TYPE,
      clnt_sht_desc             tqc_clients.clnt_sht_desc%TYPE,
      clnt_name                 tqc_clients.clnt_name%TYPE,
      clnt_other_names          tqc_clients.clnt_other_names%TYPE,
      clnt_id_reg_no            tqc_clients.clnt_id_reg_no%TYPE,
      clnt_dob                  tqc_clients.clnt_dob%TYPE,
      clnt_pin                  tqc_clients.clnt_pin%TYPE,
      clnt_physical_addrs       tqc_clients.clnt_physical_addrs%TYPE,
      clnt_postal_addrs         tqc_clients.clnt_postal_addrs%TYPE,
      clnt_twn_code             tqc_clients.clnt_twn_code%TYPE,
      twn_name                  VARCHAR2 (50),
      clnt_cou_code             tqc_clients.clnt_cou_code%TYPE,
      cou_name                  VARCHAR2 (50),
      clnt_email_addrs          tqc_clients.clnt_email_addrs%TYPE,
      clnt_tel                  tqc_clients.clnt_tel%TYPE,
      clnt_tel2                 tqc_clients.clnt_tel2%TYPE,
      clnt_status               tqc_clients.clnt_status%TYPE,
      clnt_fax                  tqc_clients.clnt_fax%TYPE,
      clnt_remarks              tqc_clients.clnt_remarks%TYPE,
      clnt_spcl_terms           tqc_clients.clnt_spcl_terms%TYPE,
      clnt_declined_prop        tqc_clients.clnt_declined_prop%TYPE,
      clnt_increased_premium    tqc_clients.clnt_increased_premium%TYPE,
      clnt_policy_cancelled     tqc_clients.clnt_policy_cancelled%TYPE,
      clnt_proposer             tqc_clients.clnt_proposer%TYPE,
      clnt_accnt_no             tqc_clients.clnt_accnt_no%TYPE,
      clnt_domicile_countries   tqc_clients.clnt_domicile_countries%TYPE,
      clnt_wef                  tqc_clients.clnt_wef%TYPE,
      clnt_wet                  tqc_clients.clnt_wet%TYPE,
      clnt_withdrawal_reason    tqc_clients.clnt_withdrawal_reason%TYPE,
      clnt_sec_code             tqc_clients.clnt_sec_code%TYPE,
      clnt_surname              tqc_clients.clnt_surname%TYPE,
      clnt_type                 tqc_clients.clnt_type%TYPE,
      clnt_title                tqc_clients.clnt_title%TYPE,
      clnt_business             tqc_clients.clnt_business%TYPE,
      clnt_zip_code             tqc_clients.clnt_zip_code%TYPE,
      clnt_bbr_code             tqc_clients.clnt_bbr_code%TYPE,
      clnt_bank_acc_no          tqc_clients.clnt_bank_acc_no%TYPE,
      clnt_clnt_code            tqc_clients.clnt_clnt_code%TYPE,
      clnt_non_direct           tqc_clients.clnt_non_direct%TYPE,
      clnt_created_by           tqc_clients.clnt_created_by%TYPE,
      clnt_sms_tel              tqc_clients.clnt_sms_tel%TYPE,
      clnt_agnt_status          tqc_clients.clnt_agnt_status%TYPE,
      clnt_date_created         tqc_clients.clnt_date_created%TYPE,
      clnt_runoff               tqc_clients.clnt_runoff%TYPE,
      clnt_loaded_by            tqc_clients.clnt_loaded_by%TYPE,
      clnt_direct_client        tqc_clients.clnt_direct_client%TYPE,
      clnt_old_accnt_no         tqc_clients.clnt_old_accnt_no%TYPE,
      clnt_usr_code             tqc_clients.clnt_usr_code%TYPE,
      usr_name                  VARCHAR (100),
      clnt_cntct_phone_1        tqc_clients.clnt_cntct_phone_1%TYPE,
      clnt_cntct_email_1        tqc_clients.clnt_cntct_email_1%TYPE,
      clnt_cntct_phone_2        tqc_clients.clnt_cntct_phone_2%TYPE,
      clnt_cntct_email_2        tqc_clients.clnt_cntct_email_2%TYPE,
      sts_code                  NUMBER,
      sts_name                  VARCHAR2 (100),
      clnt_passport_no          VARCHAR2 (50),
      clnt_gender               VARCHAR2 (1),
      clnt_cntct_name_1         tqc_clients.clnt_cntct_name_1%TYPE,
      clnt_cntct_name_2         tqc_clients.clnt_cntct_name_2%TYPE,
      client_name               VARCHAR2 (100),
      clnt_website              tqc_clients.clnt_website%TYPE,
      clnt_auditors             tqc_clients.clnt_auditors%TYPE,
      clnt_parent_company       tqc_clients.clnt_parent_company%TYPE,
      clnt_current_insurer      tqc_clients.clnt_current_insurer%TYPE,
      clnt_projected_business   tqc_clients.clnt_projected_business%TYPE,
      clnt_date_of_empl         tqc_clients.clnt_date_of_empl%TYPE,
      clnt_driving_licence      tqc_clients.clnt_driving_licence%TYPE,
      pname                     VARCHAR2 (300),
      clnt_brn_code             tqc_clients.clnt_brn_code%TYPE,
      branch_name               VARCHAR2 (300),
      clnt_credit_lim_allowed   tqc_clients.clnt_credit_lim_allowed%TYPE,
      clnt_credit_limit         tqc_clients.clnt_credit_limit%TYPE,
      clnt_bounced_chq          tqc_clients.clnt_bounced_chq%TYPE,
      clnt_marital_status       tqc_clients.clnt_marital_status%TYPE,
      clnt_bpn_code             tqc_clients.clnt_bpn_code%TYPE,
      bpn_name                  tqc_business_persons.bpn_name%TYPE,
      clnt_payroll_no           tqc_clients.clnt_payroll_no%TYPE,
      clnt_sal_max_range        tqc_clients.clnt_sal_max_range%TYPE,
      clnt_sal_min_range        tqc_clients.clnt_sal_min_range%TYPE
   );

   TYPE clients_details_ref IS REF CURSOR
      RETURN clients_details_rec;

   FUNCTION get_client_details (v_clnt_code IN NUMBER)
      RETURN clients_details_ref;

   FUNCTION get_clnt_by_accofficer (v_usr_code IN NUMBER)
      RETURN clients_details_ref;

   FUNCTION get_client_detailsnotingroup (
      v_clnt_code   IN   NUMBER,
      v_grp_code         tqc_group_clnt_dtls.grpd_grp_code%TYPE
   )
      RETURN clients_details_ref;

   FUNCTION get_client_detailsbycriteria (v_col IN VARCHAR2, v_grp_code NUMBER)
      RETURN clients_details_ref;

   FUNCTION getclntdetailsbycriteria (v_col IN VARCHAR2)
      RETURN clients_details_ref;

   FUNCTION getclntname (v_clnt_code IN NUMBER)
      RETURN VARCHAR2;
      
   FUNCTION getclntemail (v_clnt_code IN NUMBER)
      RETURN VARCHAR2;

   FUNCTION get_client_sht_desc (
      v_firstname     VARCHAR2,
      v_middle_name   VARCHAR2,
      v_surname       VARCHAR2
   )
      RETURN VARCHAR2;

   TYPE country_rec IS RECORD (
      cou_code             tqc_countries.cou_code%TYPE,
      cou_sht_desc         tqc_countries.cou_sht_desc%TYPE,
      cou_name             tqc_countries.cou_name%TYPE,
      cou_admin_reg_type   tqc_countries.cou_admin_reg_type%TYPE
   );

   TYPE country_ref IS REF CURSOR
      RETURN country_rec;

   FUNCTION get_countries
      RETURN country_ref;

   TYPE towns_rec IS RECORD (
      twn_code       tqc_towns.twn_code%TYPE,
      pst_desc       tqc_postal_codes.pst_desc%TYPE,
      twn_name       tqc_towns.twn_name%TYPE,
      pst_zip_code   tqc_postal_codes.pst_zip_code%TYPE,
      twn_sts_name   tqc_states.sts_name%TYPE,
      twn_sts_code   tqc_towns.twn_sts_code%TYPE
   );

   TYPE towns_ref IS REF CURSOR
      RETURN towns_rec;

   FUNCTION get_towns (v_cou_code IN NUMBER, v_sts_code NUMBER DEFAULT NULL)
      RETURN towns_ref;

   TYPE sectors_rec IS RECORD (
      sec_sht_desc   tqc_sectors.sec_sht_desc%TYPE,
      sec_name       tqc_sectors.sec_name%TYPE,
      sec_code       tqc_sectors.sec_code%TYPE
   );

   TYPE sectors_ref IS REF CURSOR
      RETURN sectors_rec;

   FUNCTION get_sectors
      RETURN sectors_ref;

   TYPE banks_rec IS RECORD (
      bnk_bank_name     tqc_banks.bnk_bank_name%TYPE,
      bbr_branch_name   tqc_bank_branches.bbr_branch_name%TYPE,
      bbr_code          tqc_bank_branches.bbr_code%TYPE
   );

   TYPE banks_ref IS REF CURSOR
      RETURN banks_rec;

   TYPE banks_branches_rec IS RECORD (
      bbr_code            tqc_bank_branches.bbr_code%TYPE,
      bbr_branch_name     tqc_bank_branches.bbr_branch_name%TYPE,
      bbr_remarks         tqc_bank_branches.bbr_remarks%TYPE,
      bbr_sht_desc        tqc_bank_branches.bbr_sht_desc%TYPE,
      bbr_ref_code        tqc_bank_branches.bbr_ref_code%TYPE,
      bbr_eft_supported   tqc_bank_branches.bbr_eft_supported%TYPE,
      bbr_dd_supported    tqc_bank_branches.bbr_dd_supported%TYPE,
      bbr_date_created    tqc_bank_branches.bbr_date_created%TYPE,
      bbr_created_by      tqc_bank_branches.bbr_created_by%TYPE
   );

   TYPE banks_branches_ref IS REF CURSOR
      RETURN banks_branches_rec;

   FUNCTION get_banks
      RETURN banks_ref;

   TYPE affiliates_rec IS RECORD (
      clnt_sht_desc   tqc_clients.clnt_sht_desc%TYPE,
      client_name     VARCHAR2 (250),
      clnt_code       tqc_clients.clnt_code%TYPE
   );

   TYPE affiliates_ref IS REF CURSOR
      RETURN affiliates_rec;

   FUNCTION get_affiliates (v_clnt_code IN NUMBER)
      RETURN affiliates_ref;

   TYPE client_unassigned_systems_rec IS RECORD (
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      sys_name       tqc_systems.sys_name%TYPE,
      sys_active     tqc_systems.sys_active%TYPE
   );

   TYPE client_unassigned_systems_ref IS REF CURSOR
      RETURN client_unassigned_systems_rec;

   FUNCTION get_client_unassigned_systems (
      v_client_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN client_unassigned_systems_ref;

   FUNCTION get_client_assigned_systems (
      v_client_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN client_unassigned_systems_ref;

   TYPE systems_rec IS RECORD (
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      sys_name       tqc_systems.sys_name%TYPE
   );

   TYPE systems_ref IS REF CURSOR
      RETURN systems_rec;

   FUNCTION get_clnt_unallcted_sys (v_clnt_code IN NUMBER)
      RETURN systems_ref;

   FUNCTION get_clnt_allcted_sys (v_clnt_code IN NUMBER)
      RETURN systems_ref;

   FUNCTION get_client_web_accounts (v_clnt_code IN NUMBER)
      RETURN clients_web_acc_ref;

   PROCEDURE alloc_clnt_system (v_clnt_code IN NUMBER, v_sys_code IN NUMBER);

   PROCEDURE unalloc_clnt_system (v_clnt_code IN NUMBER, v_sys_code IN NUMBER);

   PROCEDURE create_client_web_account (
      v_username             VARCHAR2,
      v_pwd                  VARCHAR2,
      v_login_allowed        VARCHAR2,
      v_pwd_changed          DATE,
      v_pwd_reset            VARCHAR2,
      v_personel_rank        VARCHAR2,
      v_acwa_status          VARCHAR2,
      v_clnt_code            NUMBER,
      v_created_by           VARCHAR2,
      v_email_address        VARCHAR2,
      v_add_edit             VARCHAR2,
      v_acwa_code            NUMBER,
      v_acwa_type       IN   VARCHAR2
   );

   PROCEDURE create_client_account (
      v_add_edit       VARCHAR2,
      v_acc_code       NUMBER,
      v_sht_desc       VARCHAR2,
      v_name           VARCHAR2,
      v_clnt_code      NUMBER,
      v_created_by     VARCHAR2,
      v_date_created   DATE,
      v_status         VARCHAR2,
      v_remarks        VARCHAR2,
      v_wef            DATE DEFAULT NULL,
      v_wet            DATE DEFAULT NULL,
      v_div_code       NUMBER,
      v_brn_code       NUMBER
   );

   FUNCTION create_clnt_proc (
      v_direct_clnt            IN   VARCHAR2,
      v_sht_desc               IN   VARCHAR2,
      v_first_name             IN   VARCHAR2,
      v_middle_name            IN   VARCHAR2,
      v_surname                IN   VARCHAR2,
      v_pin                    IN   VARCHAR2,
      v_postal_addrs           IN   VARCHAR2,
      v_physical_addrs         IN   VARCHAR2,
      v_id_reg_no              IN   VARCHAR2,
      v_user                   IN   VARCHAR2,
      v_wef                    IN   DATE,
      v_wet                    IN   DATE,
      --v_direct_client          IN VARCHAR2,
      v_title                  IN   VARCHAR2,
      v_dob                    IN   DATE,
      v_cou_code               IN   NUMBER,
      v_twn_code               IN   NUMBER,
      v_zip_code               IN   VARCHAR2,
      v_email_addrs            IN   VARCHAR2,
      v_tel                    IN   VARCHAR2,
      v_sms_tel                IN   VARCHAR2,
      v_fax                    IN   VARCHAR2,
      v_sec_code               IN   NUMBER,
      v_business               IN   VARCHAR2,
      v_domicile_countries     IN   NUMBER,
      v_proposer               IN   VARCHAR2,
      v_status                 IN   VARCHAR2,
      v_runoff                 IN   VARCHAR2,
      v_withdrawal_reason      IN   VARCHAR2,
      v_remarks                IN   VARCHAR2,
      v_bank_acc_no            IN   VARCHAR2,
      v_bbr_code               IN   NUMBER,
      v_spcl_terms             IN   VARCHAR2,
      v_policy_cancelled       IN   VARCHAR2,
      v_increased_premium      IN   VARCHAR2,
      v_declined_prop          IN   VARCHAR2,
      v_client_type            IN   VARCHAR2,
      v_add_edit               IN   VARCHAR2,
      v_clnt_code              IN   NUMBER,
      v_sex                         VARCHAR2 DEFAULT NULL,
      v_prp_code                    lms_proposers.prp_code%TYPE DEFAULT NULL,
      v_prp_co_code                 lms_proposers.prp_co_code%TYPE
            DEFAULT NULL,
      v_prp_business                lms_proposers.prp_business%TYPE
            DEFAULT NULL,
      v_prp_cover_history           lms_proposers.prp_cover_history%TYPE
            DEFAULT NULL,
      v_prp_family_history          lms_proposers.prp_family_history%TYPE
            DEFAULT NULL,
      v_prp_personal_history        lms_proposers.prp_personal_history%TYPE
            DEFAULT NULL,
      v_sts_code                    tqc_states.sts_code%TYPE DEFAULT NULL,
      v_prp_type                    lms_proposers.prp_type%TYPE DEFAULT NULL,
      v_dob_admitted                lms_proposers.prp_dob_admitted%TYPE
            DEFAULT NULL,
      v_clnt_brn_code               tqc_clients.clnt_brn_code%TYPE
            DEFAULT NULL,
      v_clnt_cntct_name_1           tqc_clients.clnt_cntct_name_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_phone_1          tqc_clients.clnt_cntct_phone_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_email_1          tqc_clients.clnt_cntct_email_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_name_2           tqc_clients.clnt_cntct_name_2%TYPE
            DEFAULT NULL,
      v_clnt_cntct_phone_2          tqc_clients.clnt_cntct_phone_2%TYPE
            DEFAULT NULL,
      v_clnt_cntct_email_2          tqc_clients.clnt_cntct_email_2%TYPE
            DEFAULT NULL,
      v_clnt_acc_officer            tqc_clients.clnt_acc_officer%TYPE
            DEFAULT NULL
   --v_acc_mngr_code       IN NUMBER
   )
      RETURN NUMBER;

   FUNCTION client_extended_proc (
      v_direct_clnt               IN       VARCHAR2,
      v_sht_desc                  IN       VARCHAR2,
      v_first_name                IN       VARCHAR2,
      v_middle_name               IN       VARCHAR2,
      v_surname                   IN       VARCHAR2,
      v_pin                       IN       VARCHAR2,
      v_postal_addrs              IN       VARCHAR2,
      v_physical_addrs            IN       VARCHAR2,
      v_id_reg_no                 IN       VARCHAR2,
      v_user                      IN       VARCHAR2,
      v_wef                       IN       DATE,
      v_wet                       IN       DATE,
      v_title                     IN       VARCHAR2,
      v_dob                       IN       DATE,
      v_cou_code                  IN       NUMBER,
      v_twn_code                  IN       NUMBER,
      v_zip_code                  IN       VARCHAR2,
      v_email_addrs               IN       VARCHAR2,
      v_tel                       IN       VARCHAR2,
      v_sms_tel                   IN       VARCHAR2,
      v_fax                       IN       VARCHAR2,
      v_sec_code                  IN       NUMBER,
      v_business                  IN       VARCHAR2,
      v_domicile_countries        IN       NUMBER,
      v_proposer                  IN       VARCHAR2,
      v_status                    IN       VARCHAR2,
      v_runoff                    IN       VARCHAR2,
      v_withdrawal_reason         IN       VARCHAR2,
      v_remarks                   IN       VARCHAR2,
      v_bank_acc_no               IN       VARCHAR2,
      v_bbr_code                  IN       NUMBER,
      v_spcl_terms                IN       VARCHAR2,
      v_policy_cancelled          IN       VARCHAR2,
      v_increased_premium         IN       VARCHAR2,
      v_declined_prop             IN       VARCHAR2,
      v_client_type               IN       VARCHAR2,
      v_add_edit                  IN       VARCHAR2,
      v_clnt_code                 IN       NUMBER,
      v_acc_mngr_code             IN       NUMBER,
      v_clnt_cntct_name_1         IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_phone_1        IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_email_1        IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_name_2         IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_phone_2        IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_email_2        IN       VARCHAR2 DEFAULT NULL,
      v_passport_no               IN       VARCHAR2 DEFAULT NULL,
      v_sts_code                  IN       tqc_clients.clnt_sts_code%TYPE,
      v_website                   IN       VARCHAR2,
      v_auditors                  IN       VARCHAR2,
      v_parent_company            IN       NUMBER,
      v_current_insurer           IN       VARCHAR2,
      v_projectedbiz              IN       NUMBER,
      v_date_of_employ            IN       DATE,
      v_driving_licence           IN       VARCHAR2,
      v_brn_code                  IN       NUMBER,
      v_clnt_image                IN       BLOB,
      v_signature                 IN       BLOB,
      v_clnt_acc_officer          IN       NUMBER,
      v_gender                    IN       VARCHAR2,
      v_clnt_occupation           IN       VARCHAR2,
      v_clnt_bank_phone_no        IN       VARCHAR2,
      v_clnt_bank_cell_no         IN       VARCHAR2,
      v_clnt_employer_phone_no    IN       VARCHAR2,
      v_clnt_employer_cell_no     IN       VARCHAR2,
      v_clt_cell_no               IN       VARCHAR2,
      v_cltn_client_types         IN       VARCHAR2,
      vclntcode                   OUT      tqc_clients.clnt_code%TYPE,
      v_clnt_sht_desc             OUT      tqc_clients.clnt_sht_desc%TYPE,
      v_oldshtdesc                         VARCHAR2 DEFAULT NULL,
      v_clnt_anniversary          IN       DATE DEFAULT NULL,
      v_clnt_crdt_rating          IN       VARCHAR2 DEFAULT NULL,
      v_client_system_code        IN       NUMBER DEFAULT NULL,
      v_clnt_drv_experience       IN       VARCHAR2,
      v_clnt_sacco                IN       VARCHAR2 DEFAULT NULL,
      v_clnt_clnt_code            IN       NUMBER,
      v_clnt_reason_updated       IN       VARCHAR2 DEFAULT NULL,
      v_clnt_credit_lim_allowed   IN       VARCHAR2 DEFAULT NULL,
      v_clnt_credit_limit         IN       VARCHAR2 DEFAULT NULL,
      v_clnt_loc_code             IN       NUMBER DEFAULT NULL,
      
      v_clnt_occ_code             IN       NUMBER DEFAULT NULL,
      v_clnt_bounced_chq          IN       VARCHAR2 DEFAULT NULL,
      v_clnt_marital_status       IN       VARCHAR2 DEFAULT NULL,
      v_commMode                  IN       VARCHAR2 DEFAULT NULL,
      v_payroll                   IN       VARCHAR2 DEFAULT NULL,
      v_minsalary                 IN       NUMBER DEFAULT NULL,
      v_maxsalary                 IN       NUMBER DEFAULT NULL,
      v_clnt_dl_issue_date        IN       DATE DEFAULT NULL,
      v_work_permit               IN       VARCHAR2 DEFAULT NULL,
      v_clnt_bpn_code             IN       NUMBER DEFAULT NULL,
      v_clnt_tel_pay              IN       VARCHAR2 DEFAULT NULL
   )
      RETURN NUMBER;

   PROCEDURE update_image (v_image BLOB, v_client_code NUMBER);

   PROCEDURE update_signature (v_image BLOB, v_client_code NUMBER);

   FUNCTION get_images (v_type VARCHAR2, v_client_code NUMBER)
      RETURN clients_image_ref;

   FUNCTION get_pol_clients
      RETURN clients_ref_cursor;

   TYPE tqc_accounttrans_clients IS RECORD (
      v_short_desc   tqc_clients.clnt_sht_desc%TYPE,
      v_name         VARCHAR2 (150),
      v_code         tqc_clients.clnt_code%TYPE
   );

   TYPE tqc_accounttrans_cursor IS REF CURSOR
      RETURN tqc_accounttrans_clients;

   PROCEDURE get_accounts_clients (
      v_cursor      OUT   tqc_accounttrans_cursor,
      v_client_id         VARCHAR2,
      v_client            VARCHAR
   );

   TYPE tqc_account_types_rec IS RECORD (
      v_act_account_type   tqc_account_types.act_account_type%TYPE,
      v_act_code           tqc_account_types.act_code%TYPE
   );

   TYPE tqc_account_type_cursor IS REF CURSOR
      RETURN tqc_account_types_rec;

   PROCEDURE get_tqc_account_types (v_cursor OUT tqc_account_type_cursor);

   TYPE tqc_acccounts_agents_rec IS RECORD (
      v_agn_sht_desc        tqc_agencies.agn_sht_desc%TYPE,
      v_agn_name            tqc_agencies.agn_name%TYPE,
      v_agn_code            tqc_agencies.agn_code%TYPE,
      v_act_type_sht_desc   tqc_account_types.act_type_sht_desc%TYPE
   );

   TYPE tqc_accounts_agents_cursor IS REF CURSOR
      RETURN tqc_acccounts_agents_rec;

   PROCEDURE get_tqc_accounts_agents (
      v_acc_type   IN       NUMBER,
      v_cursor     OUT      tqc_accounts_agents_cursor,
      v_agnt_id             VARCHAR2,
      v_agent               VARCHAR2
   );

   TYPE tqc_divisions_rec IS RECORD (
      v_div_name    tqc_divisions.div_name%TYPE,
      v_bdiv_code   tqc_brnch_divisions.bdiv_code%TYPE
   );

   TYPE tqc_divisions_cursor IS REF CURSOR
      RETURN tqc_divisions_rec;

   PROCEDURE get_divisions (
      v_brn_code   IN       NUMBER,
      v_cursor     OUT      tqc_divisions_cursor
   );

   PROCEDURE get_ref_cheq_account_types (v_cursor OUT tqc_account_type_cursor);

   PROCEDURE get_gis_clients (
      v_cursor        OUT   tqc_accounttrans_cursor,
      v_client_id           VARCHAR2,
      v_client_name         VARCHAR2
   );

   PROCEDURE get_client_name (
      v_client_code   IN       NUMBER,
      v_cursor        OUT      tqc_accounttrans_cursor
   );

   TYPE clients_detls_rec IS RECORD (
      clnt_code                 tqc_clients.clnt_code%TYPE,
      clnt_sht_desc             tqc_clients.clnt_sht_desc%TYPE,
      clnt_name                 tqc_clients.clnt_name%TYPE,
      clnt_other_names          tqc_clients.clnt_other_names%TYPE,
      clnt_id_reg_no            tqc_clients.clnt_id_reg_no%TYPE,
      clnt_dob                  tqc_clients.clnt_dob%TYPE,
      clnt_pin                  tqc_clients.clnt_pin%TYPE,
      clnt_physical_addrs       tqc_clients.clnt_physical_addrs%TYPE,
      clnt_postal_addrs         tqc_clients.clnt_postal_addrs%TYPE,
      clnt_twn_code             tqc_clients.clnt_twn_code%TYPE,
      twn_name                  tqc_towns.twn_name%TYPE,
      clnt_cou_code             tqc_clients.clnt_cou_code%TYPE,
      cou_name                  tqc_countries.cou_name%TYPE,
      clnt_email_addrs          tqc_clients.clnt_email_addrs%TYPE,
      clnt_tel                  tqc_clients.clnt_tel%TYPE,
      clnt_tel2                 tqc_clients.clnt_tel2%TYPE,
      clnt_status               tqc_clients.clnt_status%TYPE,
      clnt_fax                  tqc_clients.clnt_fax%TYPE,
      clnt_remarks              tqc_clients.clnt_remarks%TYPE,
      clnt_spcl_terms           tqc_clients.clnt_spcl_terms%TYPE,
      clnt_declined_prop        tqc_clients.clnt_declined_prop%TYPE,
      clnt_increased_premium    tqc_clients.clnt_increased_premium%TYPE,
      clnt_policy_cancelled     tqc_clients.clnt_policy_cancelled%TYPE,
      clnt_proposer             tqc_clients.clnt_proposer%TYPE,
      clnt_accnt_no             tqc_clients.clnt_accnt_no%TYPE,
      clnt_domicile_countries   tqc_clients.clnt_domicile_countries%TYPE,
      clnt_wef                  tqc_clients.clnt_wef%TYPE,
      clnt_wet                  tqc_clients.clnt_wet%TYPE,
      clnt_withdrawal_reason    tqc_clients.clnt_withdrawal_reason%TYPE,
      clnt_sec_code             tqc_clients.clnt_sec_code%TYPE,
      clnt_surname              tqc_clients.clnt_surname%TYPE,
      clnt_type                 tqc_clients.clnt_type%TYPE,
      clnt_title                tqc_clients.clnt_title%TYPE,
      clnt_business             tqc_clients.clnt_business%TYPE,
      clnt_zip_code             tqc_clients.clnt_zip_code%TYPE,
      clnt_bbr_code             tqc_clients.clnt_bbr_code%TYPE,
      clnt_bank_acc_no          tqc_clients.clnt_bank_acc_no%TYPE,
      clnt_clnt_code            tqc_clients.clnt_clnt_code%TYPE,
      clnt_non_direct           tqc_clients.clnt_non_direct%TYPE,
      clnt_created_by           tqc_clients.clnt_created_by%TYPE,
      clnt_sms_tel              tqc_clients.clnt_sms_tel%TYPE,
      clnt_agnt_status          tqc_clients.clnt_agnt_status%TYPE,
      clnt_date_created         tqc_clients.clnt_date_created%TYPE,
      clnt_runoff               tqc_clients.clnt_runoff%TYPE,
      clnt_loaded_by            tqc_clients.clnt_loaded_by%TYPE,
      clnt_direct_client        tqc_clients.clnt_direct_client%TYPE,
      clnt_old_accnt_no         tqc_clients.clnt_old_accnt_no%TYPE,
      clnt_gender               tqc_clients.clnt_gender%TYPE,
      bank_branch               VARCHAR2 (100),
      prp_co_code               lms_proposers.prp_co_code%TYPE,
      co_desc                   tq_lms.lms_class_occupations.co_desc%TYPE,
      prp_cover_history         lms_proposers.prp_cover_history%TYPE,
      prp_family_history        lms_proposers.prp_family_history%TYPE,
      prp_personal_history      lms_proposers.prp_personal_history%TYPE,
      clnt_sts_code             tqc_clients.clnt_sts_code%TYPE,
      sts_name                  tqc_states.sts_name%TYPE,
      prp_type                  lms_proposers.prp_type%TYPE,
      prp_dob_admitted          lms_proposers.prp_dob_admitted%TYPE,
      clnt_brn_code             tqc_clients.clnt_brn_code%TYPE,
      brn_name                  tqc_branches.brn_name%TYPE,
      clnt_cntct_name_1         tqc_clients.clnt_cntct_name_1%TYPE,
      clnt_cntct_phone_1        tqc_clients.clnt_cntct_phone_1%TYPE,
      clnt_cntct_email_1        tqc_clients.clnt_cntct_email_1%TYPE,
      clnt_cntct_name_2         tqc_clients.clnt_cntct_name_2%TYPE,
      clnt_cntct_phone_2        tqc_clients.clnt_cntct_phone_2%TYPE,
      clnt_cntct_email_2        tqc_clients.clnt_cntct_email_2%TYPE,
      clnt_acc_officer          tqc_clients.clnt_acc_officer%TYPE,
      usr_name                  tqc_users.usr_name%TYPE,
      prp_marital_status        lms_proposers.prp_marital_status%TYPE,
      cou_admin_reg_type        tqc_countries.cou_admin_reg_type%TYPE,
      prp_correspondence        lms_proposers.prp_correspondence%TYPE
   );

   TYPE clients_detls_ref IS REF CURSOR
      RETURN clients_detls_rec;

   FUNCTION get_proposer_dtls (v_clnt_code IN NUMBER)
      RETURN clients_detls_ref;

   PROCEDURE update_client_status (
      v_client_code   NUMBER,
      v_status        tqc_clients.clnt_status%TYPE
   );

   PROCEDURE tqc_client_directors_prc (
      v_add_edit                 IN       VARCHAR2,
      v_clntdir_code             IN       NUMBER,
      v_clntdir_clnt_code                 NUMBER,
      v_clntdir_year             IN       NUMBER,
      v_clntdir_name             IN       VARCHAR2,
      v_clntdir_qualifications   IN       VARCHAR2,
      v_clntdir_pct_holdg        IN       NUMBER,
      v_clntdir_designation      IN       VARCHAR2,
      v_err                      OUT      VARCHAR2
   );

   TYPE client_directors_rec IS RECORD (
      clntdir_code             tqc_client_directors.clntdir_code%TYPE,
      cntdir_clnt_code         tqc_client_directors.clntdir_clnt_code%TYPE,
      clntdir_year             tqc_client_directors.clntdir_year%TYPE,
      clntdir_name             tqc_client_directors.clntdir_name%TYPE,
      clntdir_qualifications   tqc_client_directors.clntdir_qualifications%TYPE,
      clntdir_pct_holdg        tqc_client_directors.clntdir_pct_holdg%TYPE,
      clntdir_designation      tqc_client_directors.clntdir_designation%TYPE
   );

   TYPE tqc_client_directors_ref IS REF CURSOR
      RETURN client_directors_rec;

   PROCEDURE get_tqc_client_directors (
      v_clnt_code   IN       NUMBER,
      v_refcur      IN OUT   tqc_client_directors_ref
   );

   PROCEDURE tqc_client_auditors_prc (
      v_add_edit                 IN       VARCHAR2,
      v_clntaud_code             IN       NUMBER,
      v_clntaud_clnt_code                 NUMBER,
      v_clntaud_year             IN       NUMBER,
      v_clntaud_name             IN       VARCHAR2,
      v_clntaud_qualifications   IN       VARCHAR2,
      v_clntaud_telephone        IN       VARCHAR2,
      v_clntaud_designation      IN       VARCHAR2,
      v_err                      OUT      VARCHAR2
   );

   TYPE client_auditors_rec IS RECORD (
      clntaud_code             tqc_client_auditors.clntaud_code%TYPE,
      cntdir_clnt_code         tqc_client_auditors.clntaud_clnt_code%TYPE,
      clntaud_year             tqc_client_auditors.clntaud_year%TYPE,
      clntaud_name             tqc_client_auditors.clntaud_name%TYPE,
      clntaud_qualifications   tqc_client_auditors.clntaud_qualifications%TYPE,
      clntaud_telephone        tqc_client_auditors.clntaud_telephone%TYPE,
      clntaud_designation      tqc_client_auditors.clntaud_designation%TYPE
   );

   TYPE tqc_client_auditors_ref IS REF CURSOR
      RETURN client_auditors_rec;

   PROCEDURE get_tqc_client_auditors (
      v_clnt_code   IN       NUMBER,
      v_refcur      IN OUT   tqc_client_auditors_ref
   );

   TYPE get_parentcompany_rec IS RECORD (
      clnt_code          tqc_clients.clnt_code%TYPE,
      clnt_sht_desc      tqc_clients.clnt_sht_desc%TYPE,
      clnt_name          tqc_clients.clnt_name%TYPE,
      clnt_other_names   tqc_clients.clnt_other_names%TYPE
   );

   TYPE get_parentcompany_ref IS REF CURSOR
      RETURN get_parentcompany_rec;

   PROCEDURE get_parentcompany (v_refcur OUT get_parentcompany_ref);

   FUNCTION get_cnt_client_details (
      v_clnt_code   IN   NUMBER,
      v_grp_code         tqc_group_clnt_dtls.grpd_grp_code%TYPE
   )
      RETURN NUMBER;

   PROCEDURE tqc_client_complaints_prc (
      v_add_edit         IN       VARCHAR2,
      v_comp_code        IN       NUMBER,
      v_comp_clnt_code   IN       NUMBER,
      v_comp_subject     IN       VARCHAR2,
      v_comp_message     IN       VARCHAR2,
      v_err              OUT      VARCHAR2
   );

   FUNCTION getclientscount (v_where VARCHAR2)
      RETURN NUMBER;

   PROCEDURE updateaccountofficer (
      v_err        OUT      VARCHAR2,
      v_user_old   IN       NUMBER,
      v_user_new   IN       NUMBER
   );

   PROCEDURE authorise_client (
      v_err           OUT      VARCHAR2,
      v_client_code   IN       NUMBER,
      v_status        IN       VARCHAR2
   );

   PROCEDURE delete_client (v_err OUT VARCHAR2, v_client_code IN NUMBER);

   FUNCTION create_client_code (
      v_surname       IN   VARCHAR2,
      v_other_names   IN   VARCHAR2,
      v_id_number     IN   VARCHAR,
      v_tel_number    IN   VARCHAR2
   )
      RETURN VARCHAR2;

   PROCEDURE clientagent (
      v_addedit       IN   VARCHAR2,
      v_client_code   IN   NUMBER,
      v_agentcode     IN   NUMBER
   );

   TYPE client_agent_rec IS RECORD (
      agn_code       tqc_agencies.agn_code%TYPE,
      agn_sht_desc   tqc_agencies.agn_sht_desc%TYPE,
      agn_name       tqc_agencies.agn_name%TYPE
   );

   TYPE client_agent_ref IS REF CURSOR
      RETURN client_agent_rec;

   FUNCTION getclientsagent (v_clientcode IN NUMBER)
      RETURN client_agent_ref;

   TYPE agent_client_rec IS RECORD (
      clnt_code       tqc_clients.clnt_code%TYPE,
      clnt_sht_desc   tqc_clients.clnt_sht_desc%TYPE,
      names           VARCHAR2 (200)
   );

   TYPE agent_client_ref IS REF CURSOR
      RETURN agent_client_rec;

   FUNCTION getagentclients (v_agentcode IN NUMBER)
      RETURN agent_client_ref;

   FUNCTION getagentnonclients
      RETURN agent_client_ref;

   FUNCTION create_client_proc (
      v_direct_clnt            IN   VARCHAR2,
      v_sht_desc               IN   VARCHAR2,
      v_surname                IN   VARCHAR2,
      v_othernames             IN   VARCHAR2,
      v_clientname             IN   VARCHAR2,
      v_pin                    IN   VARCHAR2,
      v_postal_addrs           IN   VARCHAR2,
      v_physical_addrs         IN   VARCHAR2,
      v_id_reg_no              IN   VARCHAR2,
      v_user                   IN   VARCHAR2,
      v_wef                    IN   DATE,
      v_wet                    IN   DATE,
      --v_direct_client          IN VARCHAR2,
      v_title                  IN   VARCHAR2,
      v_dob                    IN   DATE,
      v_cou_code               IN   NUMBER,
      v_twn_code               IN   NUMBER,
      v_zip_code               IN   VARCHAR2,
      v_email_addrs            IN   VARCHAR2,
      v_tel                    IN   VARCHAR2,
      v_sms_tel                IN   VARCHAR2,
      v_fax                    IN   VARCHAR2,
      v_sec_code               IN   NUMBER,
      v_business               IN   VARCHAR2,
      v_domicile_countries     IN   NUMBER,
      v_proposer               IN   VARCHAR2,
      v_status                 IN   VARCHAR2,
      v_runoff                 IN   VARCHAR2,
      v_withdrawal_reason      IN   VARCHAR2,
      v_remarks                IN   VARCHAR2,
      v_bank_acc_no            IN   VARCHAR2,
      v_bbr_code               IN   NUMBER,
      v_spcl_terms             IN   VARCHAR2,
      v_policy_cancelled       IN   VARCHAR2,
      v_increased_premium      IN   VARCHAR2,
      v_declined_prop          IN   VARCHAR2,
      v_client_type            IN   VARCHAR2,
      v_add_edit               IN   VARCHAR2,
      v_clnt_code              IN   NUMBER,
      v_sex                         VARCHAR2 DEFAULT NULL,
      v_prp_code                    lms_proposers.prp_code%TYPE DEFAULT NULL,
      v_prp_co_code                 lms_proposers.prp_co_code%TYPE
            DEFAULT NULL,
      v_prp_business                lms_proposers.prp_business%TYPE
            DEFAULT NULL,
      v_prp_cover_history           lms_proposers.prp_cover_history%TYPE
            DEFAULT NULL,
      v_prp_family_history          lms_proposers.prp_family_history%TYPE
            DEFAULT NULL,
      v_prp_personal_history        lms_proposers.prp_personal_history%TYPE
            DEFAULT NULL,
      v_sts_code                    tqc_states.sts_code%TYPE DEFAULT NULL,
      v_prp_type                    lms_proposers.prp_type%TYPE DEFAULT NULL,
      v_dob_admitted                lms_proposers.prp_dob_admitted%TYPE
            DEFAULT NULL,
      v_clnt_brn_code               tqc_clients.clnt_brn_code%TYPE
            DEFAULT NULL,
      v_clnt_cntct_name_1           tqc_clients.clnt_cntct_name_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_phone_1          tqc_clients.clnt_cntct_phone_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_email_1          tqc_clients.clnt_cntct_email_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_name_2           tqc_clients.clnt_cntct_name_2%TYPE
            DEFAULT NULL,
      v_clnt_cntct_phone_2          tqc_clients.clnt_cntct_phone_2%TYPE
            DEFAULT NULL,
      v_clnt_cntct_email_2          tqc_clients.clnt_cntct_email_2%TYPE
            DEFAULT NULL,
      v_clnt_acc_officer            tqc_clients.clnt_acc_officer%TYPE
            DEFAULT NULL,
      v_prp_marital_status          lms_proposers.prp_marital_status%TYPE
            DEFAULT NULL,
      v_prp_correspondence          lms_proposers.prp_correspondence%TYPE
            DEFAULT NULL
   --v_acc_mngr_code       IN NUMBER
   )
      RETURN NUMBER;

   TYPE client_contacts_rec IS RECORD (
      clco_code             tqc_client_contacts.clco_code%TYPE,
      clco_clnt_code        tqc_client_contacts.clco_clnt_code%TYPE,
      clco_name             tqc_client_contacts.clco_name%TYPE,
      clco_postal_addrs     tqc_client_contacts.clco_postal_addrs%TYPE,
      clco_physical_addrs   tqc_client_contacts.clco_physical_addrs%TYPE,
      clco_sec_code         tqc_client_contacts.clco_sec_code%TYPE,
      sec_name              tqc_sectors.sec_name%TYPE
   );

   TYPE client_contacts_ref IS REF CURSOR
      RETURN client_contacts_rec;

   FUNCTION getclientcontacts (
      v_clntcode   tqc_client_contacts.clco_clnt_code%TYPE
   )
      RETURN client_contacts_ref;

   PROCEDURE client_contacts_proc (
      v_addedit                 VARCHAR2,
      v_clntcont_tab   IN       tqc_client_contacts_tab,
      v_clcocode       OUT      tqc_client_contacts.clco_code%TYPE
   );

   TYPE getholdingclients_rec IS RECORD (
      clnt_code        tqc_clients.clnt_code%TYPE,
      clnt_sht_desc    tqc_clients.clnt_sht_desc%TYPE,
      clnt_name        tqc_clients.clnt_name%TYPE,
      clnt_clnt_code   tqc_clients.clnt_clnt_code%TYPE
   );

   TYPE getholdingclients_ref IS REF CURSOR
      RETURN getholdingclients_rec;

   FUNCTION getholdingclients
      RETURN getholdingclients_ref;

   TYPE getclienttypes_rec IS RECORD (
      clnty_code        tqc_client_types.clnty_code%TYPE,
      clnty_clnt_type   tqc_client_types.clnty_clnt_type%TYPE,
      clnty_category    tqc_client_types.clnty_category%TYPE
   );

   TYPE getclienttypes_ref IS REF CURSOR
      RETURN getclienttypes_rec;

   FUNCTION getclienttypes
      RETURN getclienttypes_ref;

   PROCEDURE client_type_proc (
      v_addedit           IN   VARCHAR2,
      v_clnty_code        IN   NUMBER,
      v_clnty_clnt_type   IN   VARCHAR2,
      v_clnty_category    IN   VARCHAR2
   );

   TYPE getclttypes_rec IS RECORD (
      clnty_clnt_type   tqc_client_types.clnty_clnt_type%TYPE
   );

   TYPE getclttypes_ref IS REF CURSOR
      RETURN getclttypes_rec;

   PROCEDURE get_clnt_types (v_cursor OUT getclttypes_ref);

   PROCEDURE agencyclients (v_agentcode NUMBER, v_clientcode NUMBER);

   PROCEDURE check_if_id_unique (
      v_id_number   IN       VARCHAR2,
      v_message     OUT      VARCHAR2
   );

   TYPE renparameters_rec IS RECORD (
      param_value   tqc_parameters.param_value%TYPE,
      param_desc    tqc_parameters.param_desc%TYPE
   );

   TYPE renparameters_ref IS REF CURSOR
      RETURN renparameters_rec;

   FUNCTION getrenparameters (v_param_name IN VARCHAR2)
      RETURN renparameters_ref;

   PROCEDURE saverenewalmessages (
      v_sms_sys_code     IN   NUMBER,
      v_sms_sys_module   IN   VARCHAR2,
      v_sms_clnt_code    IN   NUMBER,
      v_sms_agn_code     IN   NUMBER,
      v_sms_pol_code     IN   NUMBER,
      v_sms_pol_no       IN   VARCHAR2,
      v_sms_tel_no       IN   VARCHAR2,
      v_sms_status       IN   VARCHAR2,
      v_renewaldate      IN   DATE,
      v_clientname       IN   VARCHAR2
   );

   PROCEDURE savebirthdaymessages (
      v_sms_sys_code     IN   NUMBER,
      v_sms_sys_module   IN   VARCHAR2,
      v_sms_clnt_code    IN   NUMBER,
      v_sms_tel_no       IN   VARCHAR2,
      v_sms_status       IN   VARCHAR2,
      v_clientname       IN   VARCHAR2
   );

   FUNCTION createwebserviceclient (
      v_first_name       IN   VARCHAR2,
      v_middle_name      IN   VARCHAR2,
      v_surname          IN   VARCHAR2,
      v_clnt_id_reg_no   IN   VARCHAR2
   )
      RETURN VARCHAR2;

   FUNCTION checkclientexists (v_clnt_sht_desc IN VARCHAR2)
      RETURN VARCHAR2;

   TYPE gis_clients_rec IS RECORD (
      clnt_code                 tqc_clients.clnt_code%TYPE,
      clnt_sht_desc             tqc_clients.clnt_sht_desc%TYPE,
      clnt_name                 tqc_clients.clnt_name%TYPE,
      clnt_other_names          tqc_clients.clnt_other_names%TYPE,
      clnt_id_reg_no            tqc_clients.clnt_id_reg_no%TYPE,
      clnt_dob                  tqc_clients.clnt_dob%TYPE,
      clnt_pin                  tqc_clients.clnt_pin%TYPE,
      clnt_physical_addrs       tqc_clients.clnt_physical_addrs%TYPE,
      clnt_postal_addrs         tqc_clients.clnt_postal_addrs%TYPE,
      clnt_twn_code             tqc_clients.clnt_twn_code%TYPE,
      twn_name                  VARCHAR2 (50),
      clnt_cou_code             tqc_clients.clnt_cou_code%TYPE,
      cou_name                  VARCHAR2 (50),
      clnt_email_addrs          tqc_clients.clnt_email_addrs%TYPE,
      clnt_tel                  tqc_clients.clnt_tel%TYPE,
      clnt_tel2                 tqc_clients.clnt_tel2%TYPE,
      clnt_status               tqc_clients.clnt_status%TYPE,
      clnt_fax                  tqc_clients.clnt_fax%TYPE,
      clnt_remarks              tqc_clients.clnt_remarks%TYPE,
      clnt_spcl_terms           tqc_clients.clnt_spcl_terms%TYPE,
      clnt_declined_prop        tqc_clients.clnt_declined_prop%TYPE,
      clnt_increased_premium    tqc_clients.clnt_increased_premium%TYPE,
      clnt_policy_cancelled     tqc_clients.clnt_policy_cancelled%TYPE,
      clnt_proposer             tqc_clients.clnt_proposer%TYPE,
      clnt_accnt_no             tqc_clients.clnt_accnt_no%TYPE,
      clnt_domicile_countries   tqc_clients.clnt_domicile_countries%TYPE,
      clnt_wef                  tqc_clients.clnt_wef%TYPE,
      clnt_wet                  tqc_clients.clnt_wet%TYPE,
      clnt_withdrawal_reason    tqc_clients.clnt_withdrawal_reason%TYPE,
      clnt_sec_code             tqc_clients.clnt_sec_code%TYPE,
      clnt_surname              tqc_clients.clnt_surname%TYPE,
      clnt_type                 tqc_clients.clnt_type%TYPE,
      clnt_title                tqc_clients.clnt_title%TYPE,
      clnt_business             tqc_clients.clnt_business%TYPE,
      clnt_zip_code             tqc_clients.clnt_zip_code%TYPE,
      clnt_bbr_code             tqc_clients.clnt_bbr_code%TYPE,
      clnt_bank_acc_no          tqc_clients.clnt_bank_acc_no%TYPE,
      clnt_clnt_code            tqc_clients.clnt_clnt_code%TYPE,
      clnt_non_direct           tqc_clients.clnt_non_direct%TYPE,
      clnt_created_by           tqc_clients.clnt_created_by%TYPE,
      clnt_sms_tel              tqc_clients.clnt_sms_tel%TYPE,
      clnt_agnt_status          tqc_clients.clnt_agnt_status%TYPE,
      clnt_date_created         tqc_clients.clnt_date_created%TYPE,
      clnt_runoff               tqc_clients.clnt_runoff%TYPE,
      clnt_loaded_by            tqc_clients.clnt_loaded_by%TYPE,
      clnt_direct_client        tqc_clients.clnt_direct_client%TYPE,
      clnt_old_accnt_no         tqc_clients.clnt_old_accnt_no%TYPE,
      clnt_usr_code             tqc_clients.clnt_usr_code%TYPE,
      usr_name                  VARCHAR (100),
      clnt_cntct_phone_1        tqc_clients.clnt_cntct_phone_1%TYPE,
      clnt_cntct_email_1        tqc_clients.clnt_cntct_email_1%TYPE,
      clnt_cntct_phone_2        tqc_clients.clnt_cntct_phone_2%TYPE,
      clnt_cntct_email_2        tqc_clients.clnt_cntct_email_2%TYPE,
      sts_code                  NUMBER,
      sts_name                  VARCHAR2 (100),
      clnt_passport_no          VARCHAR2 (50),
      clnt_gender               VARCHAR2 (1),
      clnt_cntct_name_1         tqc_clients.clnt_cntct_name_1%TYPE,
      clnt_cntct_name_2         tqc_clients.clnt_cntct_name_2%TYPE,
      client_name               VARCHAR2 (100),
      clnt_website              tqc_clients.clnt_website%TYPE,
      clnt_auditors             tqc_clients.clnt_auditors%TYPE,
      clnt_parent_company       tqc_clients.clnt_parent_company%TYPE,
      clnt_current_insurer      tqc_clients.clnt_current_insurer%TYPE,
      clnt_projected_business   tqc_clients.clnt_projected_business%TYPE,
      clnt_date_of_empl         tqc_clients.clnt_date_of_empl%TYPE,
      clnt_driving_licence      tqc_clients.clnt_driving_licence%TYPE,
      pname                     VARCHAR2 (300),
      clnt_brn_code             tqc_clients.clnt_brn_code%TYPE,
      branch_name               VARCHAR2 (300),
      clnt_credit_lim_allowed   tqc_clients.clnt_credit_lim_allowed%TYPE,
      clnt_credit_limit         tqc_clients.clnt_credit_limit%TYPE,
      agn_name                  tqc_agencies.agn_name%TYPE,
      clnt_old_sht_desc         tqc_clients.clnt_old_sht_desc%TYPE,
      clnt_drv_experience       tqc_clients.clnt_drv_experience%TYPE,
      usr_username              tqc_users.usr_username%TYPE,
      clnt_bank_phone_no        tqc_clients.clnt_bank_phone_no%TYPE,
      clnt_bank_cell_no         tqc_clients.clnt_bank_cell_no%TYPE
   );

   TYPE gis_clients_ref IS REF CURSOR
      RETURN gis_clients_rec;

   FUNCTION getgisclients (v_clnt_code IN NUMBER)
      RETURN gis_clients_ref;

   PROCEDURE getclienttype (v_clnt_code IN NUMBER, v_client_type OUT VARCHAR2);

   TYPE holdingcompany_rec IS RECORD (
      clnt_code             tqc_clients.clnt_code%TYPE,
      clnt_sht_desc         tqc_clients.clnt_sht_desc%TYPE,
      clnt_name             tqc_clients.clnt_name%TYPE,
      clnt_other_names      tqc_clients.clnt_other_names%TYPE,
      clnt_id_reg_no        tqc_clients.clnt_id_reg_no%TYPE,
      clnt_dob              tqc_clients.clnt_dob%TYPE,
      clnt_pin              tqc_clients.clnt_pin%TYPE,
      clnt_physical_addrs   tqc_clients.clnt_physical_addrs%TYPE,
      clnt_postal_addrs     tqc_clients.clnt_postal_addrs%TYPE,
      clnt_email_addrs      tqc_clients.clnt_email_addrs%TYPE,
      clnt_status           tqc_clients.clnt_status%TYPE,
      clnt_type             tqc_clients.clnt_type%TYPE,
      clnt_title            tqc_clients.clnt_title%TYPE,
      clnt_direct_client    tqc_clients.clnt_direct_client%TYPE,
      clnt_gender           tqc_clients.clnt_gender%TYPE,
      clnt_passport_no      tqc_clients.clnt_passport_no%TYPE,
      clnt_occupation       tqc_clients.clnt_occupation%TYPE,
      cltn_client_types     tqc_clients.cltn_client_types%TYPE
   );

   TYPE holdingcompany_ref IS REF CURSOR
      RETURN holdingcompany_rec;

   FUNCTION getholdingcompany (v_clnt_code IN NUMBER)
      RETURN holdingcompany_ref;
END tqc_clients_pkgbkp; 
 
/