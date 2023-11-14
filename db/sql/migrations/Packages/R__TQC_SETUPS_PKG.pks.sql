/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_SETUPS_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_setups_pkg
AS
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL);

   PROCEDURE agency_holding_company_prc (
      v_add_edit          IN       VARCHAR2,
      v_ahc_code          IN       tqc_agency_holding_company.ahc_code%TYPE,
      v_ahc_name          IN       tqc_agency_holding_company.ahc_name%TYPE,
      v_postaladdress     IN       tqc_agency_holding_company.ahc_postal_address%TYPE,
      v_physicaladdress   IN       tqc_agency_holding_company.ahc_physical_address%TYPE,
      v_telnumber         IN       tqc_agency_holding_company.ahc_telephone_number%TYPE,
      v_mobnumber         IN       tqc_agency_holding_company.ahc_mobile_number%TYPE,
      v_contactperson     IN       tqc_agency_holding_company.ahc_contact_person%TYPE,
      v_error             OUT      VARCHAR2
   );

   PROCEDURE banks_prc (
      v_add_edit                    IN       VARCHAR2,
      v_bnk_code                    IN       tqc_banks.bnk_code%TYPE,
      v_bnk_bank_name               IN       tqc_banks.bnk_bank_name%TYPE,
      v_bnk_remarks                 IN       tqc_banks.bnk_remarks%TYPE,
      v_bnk_sht_desc                IN       tqc_banks.bnk_sht_desc%TYPE,
      v_bnk_ddr_code                IN       tqc_banks.bnk_ddr_code%TYPE,
      v_dd_format_desc              IN       tqc_banks.dd_format_desc%TYPE,
      v_bnk_forwarding_bnk_code     IN       tqc_banks.bnk_forwarding_bnk_code%TYPE,
      v_bnk_kba_code                IN       tqc_banks.bnk_kba_code%TYPE,
      v_bnk_eft_supported           IN       tqc_banks.bnk_eft_supported%TYPE,
      v_bnk_class_type              IN       tqc_banks.bnk_class_type%TYPE,
      v_bnk_accnt_digit_no          IN       tqc_banks.bnk_accnt_digit_no%TYPE,
      v_bnk_negotiated_bank         IN       tqc_banks.bnk_negotiated_bank%TYPE,
      v_bnk_administration_charge   IN       tqc_banks.bnk_administration_charge%TYPE,
      v_bnk_logo                    IN       tqc_banks.bnk_logo%TYPE,
      v_bnk_wef                     IN       tqc_banks.bnk_wef%TYPE,
      v_bnk_wet                     IN       tqc_banks.bnk_wet%TYPE,
      v_error                       OUT      VARCHAR2,
      v_bnk_status                  IN       tqc_banks.bnk_status%TYPE,
      v_bnk_acc_max_no              IN       tqc_banks.bnk_acc_max_no%TYPE,
      v_bnk_acc_min_no              IN       tqc_banks.bnk_acc_min_no%TYPE,
      v_bnk_cou_code                IN       tqc_banks.bnk_cou_code%TYPE
   );

   PROCEDURE bank_branches_prc (
      v_add_edit             IN     VARCHAR2,
      v_bbr_code             IN     tqc_bank_branches.bbr_code%TYPE,
      v_bbr_bnk_code         IN     tqc_bank_branches.bbr_bnk_code%TYPE,
      v_bbr_branch_name      IN     tqc_bank_branches.bbr_branch_name%TYPE,
      v_bbr_remarks          IN     tqc_bank_branches.bbr_remarks%TYPE,
      v_bbr_sht_desc         IN     tqc_bank_branches.bbr_sht_desc%TYPE,
      v_bbr_ref_code         IN     tqc_bank_branches.bbr_ref_code%TYPE,
      v_bbr_eft_supported    IN     tqc_bank_branches.bbr_eft_supported%TYPE,
      v_bbr_dd_supported     IN     tqc_bank_branches.bbr_dd_supported%TYPE,
      v_bbr_date_created     IN     tqc_bank_branches.bbr_date_created%TYPE,
      v_bbr_created_by       IN     tqc_bank_branches.bbr_created_by%TYPE,
      v_bbr_physical_addrs   IN     tqc_bank_branches.bbr_physical_addrs%TYPE,
      v_bbr_postal_addrs     IN     tqc_bank_branches.bbr_postal_addrs%TYPE,
      v_bbr_kba_code         IN     tqc_bank_branches.bbr_kba_code%TYPE,
      v_error                OUT    VARCHAR2,
      v_bnkt_code            IN     TQC_BANK_TERRITORIES.bnkt_code%TYPE  DEFAULT NULL,
      v_bbr_email            IN     tqc_bank_branches.bbr_email%TYPE DEFAULT NULL,
      v_bbr_person_name      IN     tqc_bank_branches.bbr_person_name%TYPE  DEFAULT NULL,
      v_bbr_person_email     IN     tqc_bank_branches.bbr_person_email%TYPE  DEFAULT NULL,
      v_bbr_person_cou_code  IN     tqc_bank_branches.bbr_person_cou_code%TYPE  DEFAULT NULL,
      v_bbr_person_phone     IN     tqc_bank_branches.bbr_person_phone%TYPE DEFAULT NULL);

   PROCEDURE agency_classes_prc (
      v_add_edit          IN       VARCHAR2,
      v_agnc_class_code   IN       tqc_agencies_classes.agnc_class_code%TYPE,
      v_agnc_class_desc   IN       tqc_agencies_classes.agnc_class_desc%TYPE,
      v_error             OUT      VARCHAR2
   );

   PROCEDURE currencies_prc (
      v_add_edit           IN       VARCHAR2,
      v_cur_code           IN       tqc_currencies.cur_code%TYPE,
      v_cur_symbol         IN       tqc_currencies.cur_symbol%TYPE,
      v_cur_desc           IN       tqc_currencies.cur_desc%TYPE,
      v_cur_rnd            IN       tqc_currencies.cur_rnd%TYPE,
      v_error              OUT      VARCHAR2,
      v_cur_num_word       IN       tqc_currencies.cur_num_word%TYPE,
      v_cur_decimal_word   IN       tqc_currencies.cur_decimal_word%TYPE
   );

   PROCEDURE currencies_denominations_prc (
      v_add_edit       IN       VARCHAR2,
      v_cud_code       IN       tqc_currency_denominations.cud_code%TYPE,
      v_cud_cur_code   IN       tqc_currency_denominations.cud_cur_code%TYPE,
      v_cud_value      IN       tqc_currency_denominations.cud_value%TYPE,
      v_cud_name       IN       tqc_currency_denominations.cud_name%TYPE,
      v_cud_wef        IN       tqc_currency_denominations.cud_wef%TYPE,
      v_error          OUT      VARCHAR2
   );

  PROCEDURE currency_rates_prc (
      v_add_edit            IN   VARCHAR2,
      v_crt_code            IN   tqc_currency_rates.crt_code%TYPE,
      v_crt_cur_code        IN   tqc_currency_rates.crt_cur_code%TYPE,
      v_crt_rate            IN   tqc_currency_rates.crt_rate%TYPE,
      v_crt_date            IN   TIMESTAMP,
      v_crt_base_cur_code   IN   tqc_currency_rates.crt_base_cur_code%TYPE,
      v_crt_wef             IN  TIMESTAMP,
      v_crt_wet             IN  TIMESTAMP,
      --v_user_code          IN NUMBER DEFAULT NULL
      v_usr_username          IN VARCHAR DEFAULT NULL
      
   );

    PROCEDURE acc_types_prc (
      v_act_code          NUMBER,
      v_clnt_mapping      VARCHAR2,
      v_wht_rate          NUMBER,
      v_ovr_rate          NUMBER,
      v_comm_rate         NUMBER,
      v_ac_format         VARCHAR2,
      v_vat_rate          NUMBER,
      v_sht_desc          VARCHAR2,
      v_act_no_gen_code   tqc_account_types.act_no_gen_code%TYPE,
      v_act_commision_levy_rate  NUMBER DEFAULT NULL
   );

   PROCEDURE sectors_prc (
      v_add_edit       IN   VARCHAR2,
      v_sec_code       IN   tqc_sectors.sec_code%TYPE,
      v_sec_sht_desc   IN   tqc_sectors.sec_sht_desc%TYPE,
      v_sec_name       IN   tqc_sectors.sec_name%TYPE
   );

   PROCEDURE service_provider_types_prc (
      v_add_edit        IN   VARCHAR2,
      v_spt_code        IN   tqc_service_provider_types.spt_code%TYPE,
      v_spt_sht_desc    IN   tqc_service_provider_types.spt_sht_desc%TYPE,
      v_spt_name        IN   tqc_service_provider_types.spt_name%TYPE,
      v_spt_status      IN   tqc_service_provider_types.spt_status%TYPE,
      v_spt_whtx_rate   IN   tqc_service_provider_types.spt_whtx_rate%TYPE,
      v_spt_vat_rate    IN   tqc_service_provider_types.spt_vat_rate%TYPE,
      v_spt_suffixes    IN   tqc_service_provider_types.spt_suffixes%TYPE
   );

   PROCEDURE countries_prc (
      v_add_edit             IN   VARCHAR2,
      v_cou_code             IN   tqc_countries.cou_code%TYPE DEFAULT NULL,
      v_cou_sht_desc         IN   tqc_countries.cou_sht_desc%TYPE
            DEFAULT NULL,
      v_cou_name             IN   tqc_countries.cou_name%TYPE DEFAULT NULL,
      v_cou_base_curr        IN   tqc_countries.cou_base_curr%TYPE
            DEFAULT NULL,
      v_cou_nationality      IN   tqc_countries.cou_nationality%TYPE
            DEFAULT NULL,
      v_cou_zip_code         IN   tqc_countries.cou_zip_code%TYPE
            DEFAULT NULL,
      v_cou_admin_reg_type   IN   tqc_countries.cou_admin_reg_type%TYPE
            DEFAULT NULL,
      v_cou_schegen          IN   tqc_countries.cou_schegen%TYPE DEFAULT NULL,
      v_cou_emb_code         IN   tqc_countries.cou_emb_code%TYPE
            DEFAULT NULL,
      v_cou_curr_serial      IN   tqc_countries.cou_curr_serial%TYPE
            DEFAULT NULL,
      v_cou_mobile_prefix    IN   tqc_countries.cou_mobile_prefix%TYPE
            DEFAULT NULL,
      v_cou_client_number    IN   tqc_countries.cou_client_number%TYPE
            DEFAULT NULL
   );

   PROCEDURE towns_prc (
      v_add_edit       IN   VARCHAR2,
      v_twn_code       IN   tqc_towns.twn_code%TYPE,
      v_twn_cou_code   IN   tqc_towns.twn_cou_code%TYPE,
      v_twn_sht_desc   IN   tqc_towns.twn_sht_desc%TYPE,
      v_twn_name       IN   tqc_towns.twn_name%TYPE,
      v_twn_sts_code   IN   tqc_towns.twn_sts_code%TYPE
   );

   PROCEDURE locations_prc (
      v_add_edit       IN   VARCHAR2,
      v_loc_code       IN   tqc_locations.loc_code%TYPE,
      v_loc_twn_code   IN   tqc_locations.loc_twn_code%TYPE,
      v_loc_sht_desc   IN   tqc_locations.loc_sht_desc%TYPE,
      v_loc_name       IN   tqc_locations.loc_name%TYPE,
      v_loc_landmark   IN   tqc_locations.loc_landmark%TYPE
   );

   PROCEDURE parameters_prc (
      v_add_edit       IN   VARCHAR2,
      v_param_code     IN   tqc_parameters.param_code%TYPE,
      v_param_name     IN   tqc_parameters.param_name%TYPE,
      v_param_value    IN   tqc_parameters.param_value%TYPE,
      v_param_status   IN   tqc_parameters.param_status%TYPE,
      v_param_desc     IN   tqc_parameters.param_desc%TYPE
   );

   PROCEDURE service_providers_prc (
      v_add_edit                IN   VARCHAR2,
      v_spr_code                IN   tqc_service_providers.spr_code%TYPE
            DEFAULT NULL,
      v_spr_sht_desc            IN   tqc_service_providers.spr_sht_desc%TYPE
            DEFAULT NULL,
      v_spr_name                IN   tqc_service_providers.spr_name%TYPE
            DEFAULT NULL,
      v_spr_physical_address    IN   tqc_service_providers.spr_physical_address%TYPE
            DEFAULT NULL,
      v_spr_postal_address      IN   tqc_service_providers.spr_postal_address%TYPE
            DEFAULT NULL,
      v_spr_twn_code            IN   tqc_service_providers.spr_twn_code%TYPE
            DEFAULT NULL,
      v_spr_cou_code            IN   tqc_service_providers.spr_cou_code%TYPE
            DEFAULT NULL,
      v_spr_spt_code            IN   tqc_service_providers.spr_spt_code%TYPE
            DEFAULT NULL,
      v_spr_phone               IN   tqc_service_providers.spr_phone%TYPE
            DEFAULT NULL,
      v_spr_fax                 IN   tqc_service_providers.spr_fax%TYPE
            DEFAULT NULL,
      v_spr_email               IN   tqc_service_providers.spr_email%TYPE
            DEFAULT NULL,
      v_spr_title               IN   tqc_service_providers.spr_title%TYPE
            DEFAULT NULL,
      v_spr_zip                 IN   tqc_service_providers.spr_zip%TYPE
            DEFAULT NULL,
      v_spr_wef                 IN   tqc_service_providers.spr_wef%TYPE
            DEFAULT NULL,
      v_spr_wet                 IN   tqc_service_providers.spr_wet%TYPE
            DEFAULT NULL,
      v_spr_contact             IN   tqc_service_providers.spr_contact%TYPE
            DEFAULT NULL,
      v_spr_aims_code           IN   tqc_service_providers.spr_aims_code%TYPE
            DEFAULT NULL,
      v_spr_bbr_code            IN   tqc_service_providers.spr_bbr_code%TYPE
            DEFAULT NULL,
      v_spr_bank_acc_no         IN   tqc_service_providers.spr_bank_acc_no%TYPE
            DEFAULT NULL,
      v_spr_created_by          IN   tqc_service_providers.spr_created_by%TYPE
            DEFAULT NULL,
      v_spr_date_created        IN   tqc_service_providers.spr_date_created%TYPE
            DEFAULT NULL,
      v_spr_status_remarks      IN   tqc_service_providers.spr_status_remarks%TYPE
            DEFAULT NULL,
      v_spr_status              IN   tqc_service_providers.spr_status%TYPE
            DEFAULT NULL,
      v_spr_pin_number          IN   tqc_service_providers.spr_pin_number%TYPE
            DEFAULT NULL,
      v_spr_trs_occupation      IN   tqc_service_providers.spr_trs_occupation%TYPE
            DEFAULT NULL,
      v_spr_proff_body          IN   tqc_service_providers.spr_proff_body%TYPE
            DEFAULT NULL,
      v_spr_pin                 IN   tqc_service_providers.spr_pin%TYPE
            DEFAULT NULL,
      v_spr_doc_phone           IN   tqc_service_providers.spr_doc_phone%TYPE
            DEFAULT NULL,
      v_spr_doc_email           IN   tqc_service_providers.spr_doc_email%TYPE
            DEFAULT NULL,
      v_spr_gl_acc_no           IN   tqc_service_providers.spr_gl_acc_no%TYPE
            DEFAULT NULL,
      v_sprinhouse              IN   tqc_service_providers.spr_inhouse%TYPE
            DEFAULT NULL,
      v_spr_sms_number          IN   tqc_service_providers.spr_sms_number%TYPE
            DEFAULT NULL,
      v_spr_contact_person      IN   tqc_service_providers.spr_contact_person%TYPE
            DEFAULT NULL,
      v_spr_cont_person_phone   IN   tqc_service_providers.spr_cont_person_phone%TYPE
            DEFAULT NULL,
      v_spr_invoice_number      IN   tqc_service_providers.spr_invoice_number%TYPE
            DEFAULT NULL,
      v_spr_clnt_code           IN   tqc_service_providers.spr_clnt_code%TYPE
            DEFAULT NULL,
      v_spr_bpn_code            IN   tqc_service_providers.spr_bpn_code%TYPE
            DEFAULT NULL,
      v_spr_contact_email       IN   tqc_service_providers.spr_contact_email%TYPE
            DEFAULT NULL,
      v_spr_contact_tel         IN   tqc_service_providers.spr_contact_tel%TYPE
            DEFAULT NULL,
      v_spr_tel_pay             IN   tqc_service_providers.spr_tel_pay%TYPE
            DEFAULT NULL,
      v_spr_default_provider    IN   tqc_service_providers.spr_default_provider%TYPE
            DEFAULT NULL,
      v_spr_reg_no              IN   tqc_service_providers.spr_reg_no%TYPE
            DEFAULT NULL,
      v_spr_postal_code         IN   tqc_service_providers.spr_postal_code%TYPE
            DEFAULT NULL,
      v_spr_id_type             IN   tqc_service_providers.spr_id_type%TYPE
            DEFAULT NULL,
      v_spr_id_no               IN   tqc_service_providers.spr_id_no%TYPE
            DEFAULT NULL,
      v_spr_iprs_validated IN VARCHAR2
   );
   

   PROCEDURE service_prov_activities_prc (
      v_add_edit           IN   VARCHAR2,
      v_spa_code           IN   tqc_serv_prv_activities.spa_code%TYPE,
      v_spa_spt_code       IN   tqc_serv_prv_activities.spa_spt_code%TYPE,
      v_spa_spt_sht_desc   IN   tqc_serv_prv_activities.spa_spt_sht_desc%TYPE,
      v_spa_spr_code       IN   tqc_serv_prv_activities.spa_spr_code%TYPE,
      v_spa_spr_sht_desc   IN   tqc_serv_prv_activities.spa_spr_sht_desc%TYPE,
      v_spt_main_act       IN   tqc_serv_prv_activities.spt_main_act%TYPE,
      v_spa_desc           IN   tqc_serv_prv_activities.spa_desc%TYPE,
      v_spa_spta_code      IN   NUMBER
   );

  PROCEDURE agencies_prc (
      v_add_edit                  IN       VARCHAR2,
      v_agn_code                  IN       tqc_agencies.agn_code%TYPE,
      v_agn_act_code              IN       tqc_agencies.agn_act_code%TYPE,
      v_agn_sht_desc              IN OUT   tqc_agencies.agn_sht_desc%TYPE,
      v_agn_name                  IN       tqc_agencies.agn_name%TYPE,
      v_agn_physical_address      IN       tqc_agencies.agn_physical_address%TYPE,
      v_agn_postal_address        IN       tqc_agencies.agn_postal_address%TYPE,
      v_agn_twn_code              IN       tqc_agencies.agn_twn_code%TYPE,
      v_agn_cou_code              IN       tqc_agencies.agn_cou_code%TYPE,
      v_agn_email_address         IN       tqc_agencies.agn_email_address%TYPE,
      v_agn_web_address           IN       tqc_agencies.agn_web_address%TYPE,
      v_agn_zip                   IN       tqc_agencies.agn_zip%TYPE,
      v_agn_contact_person        IN       tqc_agencies.agn_contact_person%TYPE,
      v_agn_contact_title         IN       tqc_agencies.agn_contact_title%TYPE,
      v_agn_tel1                  IN       tqc_agencies.agn_tel1%TYPE,
      v_agn_tel2                  IN       tqc_agencies.agn_tel2%TYPE,
      v_agn_fax                   IN       tqc_agencies.agn_fax%TYPE,
      v_agn_acc_no                IN       tqc_agencies.agn_acc_no%TYPE,
      v_agn_pin                   IN       tqc_agencies.agn_pin%TYPE,
      v_agn_agent_commission      IN       tqc_agencies.agn_agent_commission%TYPE,
      v_agn_credit_allowed        IN       tqc_agencies.agn_credit_allowed%TYPE,
      v_agn_agent_wht_tax         IN       tqc_agencies.agn_agent_wht_tax%TYPE,
      v_agn_print_dbnote          IN       tqc_agencies.agn_print_dbnote%TYPE,
      v_agn_status                IN       tqc_agencies.agn_status%TYPE,
      v_agn_date_created          IN       tqc_agencies.agn_date_created%TYPE,
      v_posted_by                 IN       VARCHAR2,
      v_agn_reg_code              IN       tqc_agencies.agn_reg_code%TYPE,
      v_agn_comm_reserve_rate     IN       tqc_agencies.agn_comm_reserve_rate%TYPE,
      v_agn_annual_budget         IN       tqc_agencies.agn_annual_budget%TYPE,
      v_agn_status_eff_date       IN       tqc_agencies.agn_status_eff_date%TYPE,
      v_agn_credit_period         IN       tqc_agencies.agn_credit_period%TYPE,
      v_agn_comm_stat_eff_dt      IN       tqc_agencies.agn_comm_stat_eff_dt%TYPE,
      v_agn_comm_status_dt        IN       tqc_agencies.agn_comm_status_dt%TYPE,
      v_agn_comm_allowed          IN       tqc_agencies.agn_comm_allowed%TYPE,
      v_agn_checked               IN       tqc_agencies.agn_checked%TYPE,
      v_agn_checked_by            IN       tqc_agencies.agn_checked_by%TYPE,
      v_agn_check_date            IN       tqc_agencies.agn_check_date%TYPE,
      v_agn_comp_comm_arrears     IN       tqc_agencies.agn_comp_comm_arrears%TYPE,
      v_agn_reinsurer             IN       tqc_agencies.agn_reinsurer%TYPE,
      v_agn_brn_code              IN       tqc_agencies.agn_brn_code%TYPE,
      v_agn_town                  IN       tqc_agencies.agn_town%TYPE,
      v_agn_country               IN       tqc_agencies.agn_country%TYPE,
      v_agn_status_desc           IN       tqc_agencies.agn_status_desc%TYPE,
      v_agn_id_no                 IN       tqc_agencies.agn_id_no%TYPE,
      v_agn_con_code              IN       tqc_agencies.agn_con_code%TYPE,
      v_agn_agn_code              IN       tqc_agencies.agn_agn_code%TYPE,
      v_agn_sms_tel               IN       tqc_agencies.agn_sms_tel%TYPE,
      v_agn_ahc_code              IN       tqc_agencies.agn_ahc_code%TYPE,
      v_agn_sec_code              IN       tqc_agencies.agn_sec_code%TYPE,
      v_agn_agnc_class_code       IN       tqc_agencies.agn_agnc_class_code%TYPE,
      v_agn_expiry_date           IN       tqc_agencies.agn_expiry_date%TYPE,
      v_agn_license_no            IN       tqc_agencies.agn_license_no%TYPE,
      v_agn_runoff                IN       tqc_agencies.agn_runoff%TYPE,
      v_agn_licensed              IN       tqc_agencies.agn_licensed%TYPE,
      v_agn_license_grace_pr      IN       tqc_agencies.agn_license_grace_pr%TYPE,
      v_agn_old_acc_no            IN       tqc_agencies.agn_old_acc_no%TYPE,
      v_agn_status_remarks        IN       tqc_agencies.agn_status_remarks%TYPE,
      v_agn_bbr_code              IN       tqc_agencies.agn_bbr_code%TYPE
            DEFAULT NULL,
      v_agn_bank_acc_no           IN       tqc_agencies.agn_bank_acc_no%TYPE
            DEFAULT NULL,
      v_agn_unique_prefix         IN       tqc_agencies.agn_unique_prefix%TYPE
            DEFAULT NULL,
      v_agn_state_code            IN       tqc_agencies.agn_state_code%TYPE
            DEFAULT NULL,
      v_agn_crdt_rting            IN       tqc_agencies.agn_crdt_rting%TYPE
            DEFAULT NULL,
      v_agn_subagent              IN       tqc_agencies.agn_subagent%TYPE
            DEFAULT NULL,
      v_agn_main_agn_code         IN       tqc_agencies.agn_main_agn_code%TYPE
            DEFAULT NULL,
      v_agn_clnt_code             IN       tqc_agencies.agn_clnt_code%TYPE
            DEFAULT NULL,
      v_agn_account_manager       IN       tqc_agencies.agn_account_manager%TYPE
            DEFAULT NULL,
      v_agn_credit_limit          IN       tqc_agencies.agn_credit_limit%TYPE
            DEFAULT NULL,
      v_agn_bru_code              IN       tqc_agencies.agn_bru_code%TYPE
            DEFAULT NULL,
      v_agn_local_international   IN       tqc_agencies.agn_local_international%TYPE
            DEFAULT NULL,
      v_agn_regulator_number      IN       tqc_agencies.agn_regulator_number%TYPE
            DEFAULT NULL,
      v_agn_rorg_code             IN       tqc_agencies.agn_rorg_code%TYPE
            DEFAULT NULL,
      v_agn_ors_code              IN       tqc_agencies.agn_ors_code%TYPE
            DEFAULT NULL,
      v_agn_allocate_cert         IN       tqc_agencies.agn_allocate_cert%TYPE
            DEFAULT NULL,
      v_agn_bounced_chq           IN       tqc_agencies.agn_bounced_chq%TYPE
            DEFAULT NULL,
      v_def_comm_mode             IN       tqc_agencies.agn_default_comm_mode%TYPE
            DEFAULT NULL,
      v_agn_bpn_code              IN       tqc_agencies.agn_bpn_code%TYPE
            DEFAULT NULL,
      v_agn_agent_type            IN       tqc_agencies.agn_agent_type%TYPE
            DEFAULT NULL,
      v_agn_group                 IN       tqc_agencies.agn_group%TYPE
            DEFAULT NULL,
      v_vat_appl                  IN       tqc_agencies.agn_vat_applicable%TYPE,
      v_withtaxappl               IN       tqc_agencies.agn_whtax_applicable%TYPE,
      v_agn_tel_pay               IN       tqc_agencies.agn_tel_pay%TYPE,
      v_agnresetcode              IN       VARCHAR2 DEFAULT NULL,
      v_freq_payment              IN       VARCHAR2 DEFAULT NULL,
      v_pymt_mode                 IN       VARCHAR2 DEFAULT NULL,
      v_pymt_validated            IN       VARCHAR2 DEFAULT NULL,
      v_agn_benefit_start_date    IN       DATE DEFAULT NULL,
      v_agn_dob                   IN       DATE DEFAULT NULL,
      v_agn_qualification         IN       VARCHAR2 DEFAULT NULL,
      v_agn_marital_status        IN       VARCHAR2 DEFAULT NULL,
      v_id_no_doc_used            IN       VARCHAR2 DEFAULT NULL,
      v_agn_sbu_code              IN       VARCHAR2 DEFAULT NULL,
      v_agn_comm_levy_app         IN       VARCHAR2 DEFAULT NULL,
      v_agn_comm_levy_rate        IN       NUMBER DEFAULT NULL,
      v_agn_brr_code              IN       NUMBER DEFAULT NULL,
      v_agn_brr_name              IN       VARCHAR2 DEFAULT NULL,
      v_agn_auth_name             IN       VARCHAR2 DEFAULT NULL,
      v_agn_gender                IN       tqc_agencies.agn_gender%TYPE,
      v_agn_iprs_validated        IN       VARCHAR2 DEFAULT NULL
--      ,v_client_country_name       IN       VARCHAR2 DEFAULT NULL
   );

   PROCEDURE payment_modes_prc (
      v_add_edit        IN   VARCHAR2,
      v_pmod_code       IN   tqc_payment_modes.pmod_code%TYPE,
      v_pmod_sht_desc   IN   tqc_payment_modes.pmod_sht_desc%TYPE,
      v_pmod_desc       IN   tqc_payment_modes.pmod_desc%TYPE,
      v_pmod_naration   IN   tqc_payment_modes.pmod_naration%TYPE,
      v_pmod_default    IN   tqc_payment_modes.pmod_default%TYPE
   );

   PROCEDURE client_titles_prc (
      v_add_edit       IN   VARCHAR2,
      v_clt_code       IN   tqc_client_titles.clt_code%TYPE,
      v_clt_sht_desc   IN   tqc_client_titles.clt_sht_desc%TYPE,
      v_clnt_desc      IN   tqc_client_titles.clnt_desc%TYPE
   );

   PROCEDURE grant_prov_system (
      v_prov_code    IN   tqc_service_providers.spr_code%TYPE,
      v_sys_code     IN   tqc_systems.sys_code%TYPE,
      v_created_by   IN   VARCHAR2
   );

   PROCEDURE revoke_prov_system (
      v_prov_code   IN   tqc_service_providers.spr_code%TYPE,
      v_sys_code    IN   tqc_systems.sys_code%TYPE
   );

   PROCEDURE prospects_prc (
      v_addedit              VARCHAR2,
      v_prospects_tab   IN   tqc_prospects_tab
   );

   PROCEDURE holidays_prc (
      v_addedit             VARCHAR2,
      v_holidays_tab   IN   tqc_holidays_tab
   );

   PROCEDURE searchclients (v_username VARCHAR2, v_search_date DATE);

   PROCEDURE generatedirectdebitrecs (
      v_username         VARCHAR2,
      v_searched         VARCHAR2,
      v_dd_bbr_code      NUMBER,
      v_dd_company_acc   VARCHAR2,
      v_search_date      DATE,
      v_install_day      NUMBER
   );

   PROCEDURE deletedirectdebit (
      v_username    VARCHAR2,
      v_dd_code     NUMBER,
      v_dd_status   VARCHAR2
   );

   PROCEDURE authorisedirectdebit (
      v_username    VARCHAR2,
      v_dd_code     NUMBER,
      v_dd_status   VARCHAR2
   );

   PROCEDURE deletedirectdebitdetail (
      v_username    VARCHAR2,
      v_ddd_code    NUMBER,
      v_dd_status   VARCHAR2
   );

--   PROCEDURE generatereceipts (
--      v_username       VARCHAR2,
--      v_dd_code        NUMBER,
--      v_dd_receipted   VARCHAR2,
--      v_dd_book_date   DATE
--   );

   PROCEDURE generatereceipts (
      v_username          VARCHAR2,
      v_dd_code           NUMBER,
      v_dd_receipted      VARCHAR2,
      v_dd_book_date      DATE,
      v_dd_receipt_date   DATE
   );

   PROCEDURE failupdateddheader (
      v_username       VARCHAR2,
      v_ddh_code       NUMBER,
      v_fail_date      DATE,
      v_fail_remarks   VARCHAR2,
      v_ddh_dd_code    NUMBER
   );

   PROCEDURE failupdateddheader2 (
      v_username                VARCHAR2,
      v_ddh_code                NUMBER,
      v_book_date               DATE,
      v_fail_remarks            VARCHAR2,
      v_ddh_dd_code    IN OUT   NUMBER,
      v_status                  VARCHAR DEFAULT 'F'
   );

   PROCEDURE failupdateheader (
      v_dddcode                 NUMBER,
      v_username                VARCHAR2,
      v_policyno                VARCHAR2,
      v_account                 VARCHAR2,
      v_book_date               DATE,
      v_fail_remarks            VARCHAR2,
      v_ddh_dd_code    IN OUT   NUMBER,
      v_status                  VARCHAR DEFAULT 'F',
      v_bankamount              NUMBER
   );

   PROCEDURE relaunchddheader (
      v_username      VARCHAR2,
      v_ddh_code      NUMBER,
      v_ddh_status    VARCHAR2,
      v_ddh_dd_code   NUMBER
   );

   PROCEDURE stoplaunchddheader (
      v_username      VARCHAR2,
      v_ddh_code      NUMBER,
      v_ddh_status    VARCHAR2,
      v_ddh_dd_code   NUMBER
   );

   PROCEDURE service_provider_type_act_prc (
      v_add_edit                  IN   VARCHAR2,
      v_spta_code                 IN   tqc_serv_prv_type_actvts.spta_code%TYPE,
      v_spta_spt_code             IN   tqc_serv_prv_type_actvts.spta_spt_code%TYPE,
      v_spta_sht_desc             IN   tqc_serv_prv_type_actvts.spta_sht_desc%TYPE,
      v_spta_desc                 IN   tqc_serv_prv_type_actvts.spta_desc%TYPE,
      v_spta_sms_msgt_code        IN   tqc_serv_prv_type_actvts.spta_sms_msgt_code%TYPE
            DEFAULT NULL,
      v_spta_email_msgt_code      IN   tqc_serv_prv_type_actvts.spta_email_msgt_code%TYPE
            DEFAULT NULL,
      v_spta_send_msg_default     IN   tqc_serv_prv_type_actvts.spta_send_msg_default%TYPE
            DEFAULT NULL,
      v_spta_send_email_default   IN   tqc_serv_prv_type_actvts.spta_send_email_default%TYPE
            DEFAULT NULL,
      v_reportdays                IN   tqc_serv_prv_type_actvts.spta_report_days%TYPE
            DEFAULT NULL
   );

   PROCEDURE message_templates_prc (
      v_add_edit          IN   VARCHAR2,
      v_msgt_code         IN   tqc_msg_templates.msgt_code%TYPE,
      v_msgt_sht_desc     IN   tqc_msg_templates.msgt_sht_desc%TYPE,
      v_msgt_msg          IN   tqc_msg_templates.msgt_msg%TYPE,
      v_msgt_sys_code     IN   tqc_msg_templates.msgt_sys_code%TYPE,
      v_msgt_sys_module   IN   tqc_msg_templates.msgt_sys_module%TYPE,
      v_msgt_type         IN   tqc_msg_templates.msgt_type%TYPE
   );

   PROCEDURE messagetemplates_prc (
      v_addedit                     VARCHAR2,
      v_messagetemplates_tab   IN   tqc_msg_templates_tab
   );

   PROCEDURE orgdivlevelstype_prc (
      v_addedit                     VARCHAR2,
      v_orgdivlevelstype_tab   IN   tqc_org_div_levels_type_tab
   );

   PROCEDURE orgdivisionlevels_prc (
      v_addedit                 VARCHAR2,
      v_orgdivlevels_tab   IN   tqc_org_division_levels_tab
   );

   PROCEDURE orgsubdivisions_prc (
      v_addedit                    VARCHAR2,
      v_orgsubdivisions_tab   IN   tqc_org_subdivisions_tab
   );

   -- PROCEDURE agencyActivities_prc(v_addEdit  VARCHAR2, v_agencyActivities_tab IN TQC_AGENCY_ACTIVITIES_TAB);
   PROCEDURE agencyactivities_prc (
      v_addedit            IN   VARCHAR2,
      v_aac_code                NUMBER,
      v_aac_sys_code            NUMBER,
      v_aac_acty_code           NUMBER,
      v_aac_wef                 DATE,
      v_aac_estimate_wet        DATE,
      v_aac_actual_wet          DATE,
      v_aac_remarks             VARCHAR2,
      v_aac_agn_code            NUMBER,
      v_aac_act_type            VARCHAR2,
      v_aac_reasons             VARCHAR2,
      v_aac_by_code             NUMBER
   );

   PROCEDURE holidaydefinitions_prc (
      v_addedit                       VARCHAR2,
      v_holidaydefinitions_tab   IN   tqc_holidays_definitions_tab
   );

   PROCEDURE postalcodes_prc (
      v_addedit                VARCHAR2,
      v_postalcodes_tab   IN   tqc_postal_codes_tab
   );

   PROCEDURE activitytypes_prc (
      v_addedit                  VARCHAR2,
      v_activitytypes_tab   IN   tqc_activity_types_tab
   );

   PROCEDURE orgsubdivprevheads_prc (
      v_addedit                      VARCHAR2,
      v_orgsubdivprevhead_tab   IN   tqc_org_subdiv_prev_heads_tab
   );

   PROCEDURE syspostlevels_prc (
      v_addedit                  VARCHAR2,
      v_syspostlevels_tab   IN   tqc_sys_post_levels_tab
   );

   PROCEDURE sysposts_prc (
      v_addedit             VARCHAR2,
      v_sysposts_tab   IN   tqc_sys_posts_tab
   );

   PROCEDURE sysprcsssubarealmts_prc (
      v_addedit                        VARCHAR2,
      v_sysprcsssubarealmts_tab   IN   tqc_sys_prcss_subarea_lmts_tab
   );

   PROCEDURE sysprcsssubarealvls_prc (
      v_addedit                        VARCHAR2,
      v_sysprcsssubarealvls_tab   IN   tqc_sys_prcss_subarea_lvls_tab
   );

   PROCEDURE usersystems_prc (
      v_addedit                VARCHAR2,
      v_usersystems_tab   IN   tqc_user_systems_tab
   );

   PROCEDURE agencysystems_prc (
      v_addedit                  VARCHAR2,
      v_agencysystems_tab   IN   tqc_agency_systems_tab
   );

   PROCEDURE claim_paymentmode_prc (
      v_add_edit   VARCHAR2,
      v_code       tqc_clm_payment_modes.cpm_code%TYPE,
      v_sht_desc   tqc_clm_payment_modes.cpm_sht_desc%TYPE,
      v_desc       tqc_clm_payment_modes.cpm_desc%TYPE,
      v_remarks    tqc_clm_payment_modes.cpm_remarks%TYPE,
      v_max_amt    tqc_clm_payment_modes.cpm_max_amt%TYPE,
      v_min_amt    tqc_clm_payment_modes.cpm_min_amt%TYPE,
      v_default    tqc_clm_payment_modes.cpm_default%TYPE
   );

   PROCEDURE mobilepaymenttypes_prc (
      v_add_edit          VARCHAR2,
      v_code              tqc_mobile_pymnt_types.mpt_code%TYPE,
      v_sht_desc          tqc_mobile_pymnt_types.mpt_sht_desc%TYPE,
      v_desc              tqc_mobile_pymnt_types.mpt_desc%TYPE,
      v_min_amt_allowed   tqc_mobile_pymnt_types.mpt_min_amt_allowed%TYPE,
      v_max_amt_allowed   tqc_mobile_pymnt_types.mpt_max_amt_allowed%TYPE,
      v_cou_code          tqc_mobile_pymnt_types.mpt_cou_code%TYPE
   );

   PROCEDURE mobiletypesprefix_prc (
      v_add_edit        VARCHAR2,
      v_code            tqc_mob_pymnt_types_prefixes.mptp_code%TYPE,
      v_mob_no_prefix   tqc_mob_pymnt_types_prefixes.mptp_mob_no_prefix%TYPE,
      v_mpt_code        tqc_mob_pymnt_types_prefixes.mptp_mpt_code%TYPE
   );

   PROCEDURE usergroup_prc (
      v_add_edit       VARCHAR2,
      v_code           tqc_group_users.gusr_code%TYPE,
      v_grp_usr_code   tqc_group_users.gusr_grp_usr_code%TYPE,
      v_usr_code       tqc_group_users.gusr_usr_code%TYPE
   );

   PROCEDURE clientgroup_prc (
      v_add_edit   VARCHAR2,
      v_code       tqc_group_client.grp_code%TYPE,
      v_name       tqc_group_client.grp_name%TYPE,
      v_minimum    tqc_group_client.grp_minimum%TYPE,
      v_maximum    tqc_group_client.grp_maximum%TYPE
   );

   PROCEDURE clientgroupmembers_prc (
      v_add_edit    VARCHAR2,
      v_code        tqc_group_clnt_dtls.grpd_code%TYPE,
      v_clnt_code   tqc_group_clnt_dtls.grpd_code%TYPE,
      v_grp_code    tqc_group_clnt_dtls.grpd_grp_code%TYPE
   );

   PROCEDURE printer_server_prc (
      v_add_edit            VARCHAR2,
      v_code                NUMBER,
      v_name                VARCHAR2,
      v_filter              VARCHAR2,
      v_uri                 VARCHAR2,
      v_filter_command      VARCHAR2,
      v_sec_username        VARCHAR2,
      v_sec_password        VARCHAR2,
      v_sec_auth_type       VARCHAR2,
      v_sec_encrpt_type     VARCHAR2,
      v_proxy_host          VARCHAR2,
      v_proxy_port          VARCHAR2,
      v_proxy_username      VARCHAR2,
      v_proxy_pasword       VARCHAR2,
      v_proxy_authen_type   VARCHAR2
   );

/* PROCEDURE adm_regions_prc(
        v_ADD_EDIT         IN  VARCHAR2,
        v_ADM_REG_CODE     IN  TQC_ADMIN_REGIONS.ADM_REG_CODE%TYPE,
        v_ADM_REG_SHT_DESC IN  TQC_ADMIN_REGIONS.ADM_REG_SHT_DESC%TYPE,
        v_ADM_REG_NAME     IN TQC_ADMIN_REGIONS.ADM_REG_NAME%TYPE,
        v_ADM_REG_COU_CODE IN TQC_ADMIN_REGIONS.ADM_REG_COU_CODE%TYPE
    );
    */
   PROCEDURE states_prc (
      v_add_edit       IN   VARCHAR2,
      v_sts_code       IN   tqc_states.sts_code%TYPE,
      v_sts_cou_code   IN   tqc_states.sts_cou_code%TYPE,
      v_sts_sht_desc   IN   tqc_states.sts_sht_desc%TYPE,
      v_sts_name       IN   tqc_states.sts_name%TYPE
   );

   PROCEDURE save_lead_details (
      v_lds_code              NUMBER,
      v_desc                  VARCHAR2,
      v_cmp_code              NUMBER,
      v_cotel                 VARCHAR2,
      v_title                 VARCHAR2,
      v_surname               VARCHAR2,
      v_oth_name              VARCHAR2,
      v_mob_no                VARCHAR2,
      v_fax                   VARCHAR2,
      v_email                 VARCHAR2,
      v_post_addr             VARCHAR2,
      v_post_code             VARCHAR2,
      v_phy_addr              VARCHAR2,
      v_ldsrccode             NUMBER,
      v_lstscode              NUMBER,
      v_lead_date             DATE,
      v_converted             VARCHAR2,
      v_port_name             VARCHAR2,
      v_port_contr            VARCHAR2,
      v_port_amt              NUMBER,
      v_port_sale             VARCHAR2,
      v_port_clse             DATE,
      v_ann_rev               NUMBER,
      v_website               VARCHAR2,
      v_industry              VARCHAR2,
      v_cou_code              NUMBER,
      v_state_code            NUMBER,
      v_town_code             NUMBER,
      v_org_code              NUMBER,
      v_user_code             NUMBER,
      v_team_code             NUMBER,
      v_acc_code              NUMBER,
      v_prod_code             NUMBER,
      v_cur_code              NUMBER,
      v_lds_sys_code          NUMBER,
      v_lds_div_code          NUMBER,
      v_lds_occupation   IN   VARCHAR2,
      v_lds_comp_name    IN   VARCHAR2,
      v_clnt_type        IN   VARCHAR2 default null,
      v_user_name        IN   VARCHAR2 default null
   );

   PROCEDURE delete_lead (v_lds_code NUMBER);

   PROCEDURE save_lead_comment (
      v_action       VARCHAR2,
      v_lds_code     NUMBER,
      v_lcmnt_code   NUMBER,
      v_comment      VARCHAR2,
      v_date         DATE,
      v_usr_code     NUMBER
   );

   PROCEDURE save_lead_activities (
      v_action       VARCHAR2,
      v_lacts_code   NUMBER,
      v_act_code     NUMBER,
      v_lds_code     NUMBER
   );

   PROCEDURE save_lead_sources (
      v_action   VARCHAR2,
      v_code     NUMBER,
      v_desc     VARCHAR2
   );

   PROCEDURE save_lead_statuses (
      v_action   VARCHAR2,
      v_code     NUMBER,
      v_desc     VARCHAR2
   );

   PROCEDURE report_groups_prc (
      v_action     IN   VARCHAR2 DEFAULT NULL,
      v_trg_code        tqc_report_groups.trg_code%TYPE DEFAULT NULL,
      v_trg_name        tqc_report_groups.trg_name%TYPE DEFAULT NULL
   );

   PROCEDURE report_div_groups_prc (
      v_action         IN   VARCHAR2 DEFAULT NULL,
      v_rdg_code            tqc_report_div_groups.rdg_code%TYPE DEFAULT NULL,
      v_rdg_trg_code        tqc_report_div_groups.rdg_code%TYPE DEFAULT NULL,
      v_rdg_div_code        tqc_report_div_groups.rdg_div_code%TYPE
            DEFAULT NULL
   );

   PROCEDURE system_app_areas_prc (
      v_add_edit          IN   VARCHAR2,
      v_saa_code          IN   tqc_sys_applicable_areas.saa_code%TYPE
            DEFAULT NULL,
      v_saa_sys_code      IN   tqc_sys_applicable_areas.saa_sys_code%TYPE
            DEFAULT NULL,
      v_saa_description   IN   tqc_sys_applicable_areas.saa_description%TYPE
            DEFAULT NULL
   );

   PROCEDURE tqc_sys_mod_subunits_prc (
      v_add_edit         IN   VARCHAR2,
      v_tsms_code        IN   NUMBER,
      v_tsms_tsm_code    IN   NUMBER,
      v_tsms_sht_desc    IN   VARCHAR2,
      v_tsms_desc        IN   VARCHAR2,
      v_tsms_order       IN   NUMBER,
      v_tsms_prod_code   IN   NUMBER
   );

   PROCEDURE mod_subunit_det_prc (
      v_add_edit                   IN   VARCHAR2,
      v_tsmsd_code                 IN   NUMBER,
      v_tsmsd_tsms_code            IN   NUMBER,
      v_tsmsd_name                 IN   VARCHAR2,
      v_tsmsd_prompt               IN   VARCHAR2,
      v_tsmsd_type                 IN   VARCHAR2,
      v_tsmsd_order                IN   VARCHAR2,
      v_tsmsd_parent               IN   NUMBER,
      v_tsmsd_more_dtls_appl       IN   VARCHAR2,
      v_tsmsd_more_dtls            IN   VARCHAR2,
      v_tsmsd_root                 IN   VARCHAR2,
      v_tsmsd_more_dtls_required   IN   VARCHAR2,
      v_tmsc_code                  IN   NUMBER
   );

   PROCEDURE sys_subunits_options_prc (
      v_add_edit           IN   VARCHAR2,
      v_tsso_code          IN   tqc_sys_subunits_options.tsso_code%TYPE,
      v_tsso_tsmsd_code    IN   tqc_sys_subunits_options.tsso_tsmsd_code%TYPE,
      v_tsso_option_name   IN   tqc_sys_subunits_options.tsso_option_name%TYPE,
      v_tsso_option_desc   IN   tqc_sys_subunits_options.tsso_option_desc%TYPE,
      v_tsso_order         IN   tqc_sys_subunits_options.tsso_order%TYPE,
      v_tsso_type          IN   tqc_sys_subunits_options.tsso_type%TYPE
   );

   PROCEDURE outgoingmailproc (
      v_mail_type     VARCHAR2,
      v_mail_secure   VARCHAR2,
      v_server        VARCHAR2,
      v_host          VARCHAR2,
      v_port          NUMBER,
      v_username      VARCHAR2,
      v_password      VARCHAR2,
      v_email         VARCHAR2
   );

   PROCEDURE incomingmailproc (
      v_mail_secure   VARCHAR2,
      v_server        VARCHAR2,
      v_host          VARCHAR2,
      v_port          NUMBER,
      v_username      VARCHAR2,
      v_password      VARCHAR2,
      v_type          VARCHAR2
   );

   PROCEDURE create_alerts (
      v_job_name          IN   VARCHAR2,
      v_job_desc          IN   VARCHAR2,
      v_start_time        IN   DATE,
      v_end_time          IN   DATE,
      v_code              IN   NUMBER,
      v_sys_code          IN   NUMBER,
      v_recurrence_type   IN   VARCHAR2,
      v_assignee          IN   NUMBER,
      v_notified_fail     IN   NUMBER,
      v_notified_succ     IN   NUMBER,
      v_job_type          IN   VARCHAR2,
      v_job_template      IN   VARCHAR2,
      v_fail_template     IN   VARCHAR2,
      v_succ_template     IN   VARCHAR2,
      v_add_edit          IN   VARCHAR2,
      v_cron_expr         IN   VARCHAR2,
      v_status            IN   VARCHAR2,
      v_repeat            IN   VARCHAR2,
      v_threshtype        IN   VARCHAR2,
      v_threshval         IN   NUMBER
   );

   PROCEDURE households_prc (
      v_add_edit   VARCHAR2,
      v_code       tqc_households.hh_id%TYPE,
      v_name       tqc_households.hh_name%TYPE,
      v_category   tqc_households.hh_category%TYPE,
      v_usrcode    NUMBER
   );

   PROCEDURE householdmembers_prc (
      v_add_edit    VARCHAR2,
      v_code        tqc_households.hh_id%TYPE,
      v_clnt_code   tqc_clients.clnt_code%TYPE
   );

   PROCEDURE agencies_editaccounttype (
      v_agn_code       IN   tqc_agencies.agn_code%TYPE,
      v_agn_act_code   IN   tqc_agencies.agn_act_code%TYPE
   );

   PROCEDURE ecm_setups_prc (
      v_est_code           tqc_ecm_setups.est_code%TYPE,
      v_est_ecm_url        tqc_ecm_setups.est_ecm_url%TYPE,
      v_est_ecm_username   tqc_ecm_setups.est_ecm_username%TYPE,
      v_est_ecm_password   tqc_ecm_setups.est_ecm_password%TYPE,
      v_est_sys_code       tqc_ecm_setups.est_sys_code%TYPE,
      v_est_root_folder    tqc_ecm_setups.est_root_folder%TYPE,
      v_add_edit           VARCHAR2
   );

   PROCEDURE attach_ecm_reports (
      v_add_edit         VARCHAR2,
      v_sprr_code        NUMBER,
      v_sprr_rpt_code    NUMBER,
      v_sprr_sprc_code   NUMBER,
      v_sprr_desc        VARCHAR2,
      v_sprr_type        VARCHAR2,
      v_doc_type_code    NUMBER
   );

   PROCEDURE ecm_doc_types (
      v_sdt_code           NUMBER,
      v_content_type       VARCHAR2,
      v_sdt_content_name   VARCHAR2,
      v_sdt_content_desc   VARCHAR2,
      v_add_edit           VARCHAR2
   );

   PROCEDURE ecm_metadata (
      v_ddm_code       NUMBER,
      v_ddm_sdt_code   NUMBER,
      v_ddm_name       VARCHAR2,
      v_ddm_desc       VARCHAR2,
      v_add_edit       VARCHAR2
   );

   PROCEDURE bank_branch_details (
      v_add_edit           VARCHAR2,
      v_bbb_code           NUMBER,
      v_bbb_brn_code       NUMBER,
      v_bbb_brn_reg_code   VARCHAR2,
      v_bbb_brn_name       VARCHAR2,
      v_bbb_bbr_code       NUMBER,
      v_bbb_bbr_bnk_code   NUMBER
   );

   PROCEDURE smsstatus (
      v_status                   VARCHAR2,
      v_sms_code                 NUMBER DEFAULT NULL,
      v_sms_sent_response   IN   VARCHAR2 DEFAULT NULL
   );

   PROCEDURE escalate_proc (
      v_taskid                      NUMBER,
      v_syscode                     NUMBER,
      v_processdefinitionid         VARCHAR2,
      v_taskname                    VARCHAR2,
      v_duration              OUT   NUMBER,
      v_assignee              OUT   VARCHAR2
   );

   PROCEDURE createbankbranches (
      v_addedit              VARCHAR2,
      v_tcb_code        IN   NUMBER,
      v_tcb_clnt_code   IN   NUMBER,
      v_tcb_sht_desc    IN   VARCHAR2,
      v_tcb_name        IN   VARCHAR
   );

   PROCEDURE assignbanktoclient (
      v_addedit         IN   VARCHAR2,
      v_tcb_acwa_code   IN   NUMBER,
      v_tcb_code        IN   NUMBER
   );

   PROCEDURE assigndefaultbranch (v_tcub_tcb_code IN NUMBER);

   PROCEDURE createwebproductdetails (
      v_addedit                VARCHAR2,
      v_twpd_clnt_code    IN   NUMBER,
      v_twpd_twp_code     IN   NUMBER,
      v_twpd_usr_code     IN   NUMBER,
      v_twpd_username     IN   VARCHAR2,
      v_twpd_dr_limit     IN   NUMBER,
      v_twpd_cr_limit     IN   NUMBER,
      v_twpd_policy_use   IN   VARCHAR2,
      v_twpd_endos_use    IN   VARCHAR2,
      v_twpd_endos_account IN VARCHAR2 DEFAULT NULL
   );

   PROCEDURE outgoingsmsproc (
      v_addedit      VARCHAR2,
      v_tsscode      tqc_system_sms.tss_code%TYPE,
      v_tssdesc      tqc_system_sms.tss_desc%TYPE,
      v_tssurls      tqc_system_sms.tss_url%TYPE,
      v_username     tqc_system_sms.tss_username%TYPE,
      v_password     tqc_system_sms.tss_password%TYPE,
      v_source       tqc_system_sms.tss_source%TYPE,
      v_tssdefault   tqc_system_sms.tss_default%TYPE
   );

   PROCEDURE getrequireddocs (
      v_add_edit        IN   VARCHAR2,
      v_rqd_code        IN   NUMBER,
      v_rqd_spt_code    IN   NUMBER,
      v_rqd_spta_code   IN   NUMBER,
      v_rqd_sys_code    IN   NUMBER,
      v_rqc_rdoc_id     IN   NUMBER
   );

   PROCEDURE getcountrypref (
      v_cou_name            IN       VARCHAR2,
      v_cou_mobile_prefix   OUT      NUMBER,
      v_cou_client_number   OUT      NUMBER
   );

   PROCEDURE escalations_proc (
      v_addedit    VARCHAR2,
      v_code       tqc_sys_escalation_levels.tsel_code%TYPE,
      v_syscode    tqc_sys_escalation_levels.tsel_jsd_sys_code%TYPE,
      v_jpdlname   tqc_sys_escalation_levels.tsel_jsd_jpdl_name%TYPE,
      v_activity   tqc_sys_escalation_levels.tsel_activity_name%TYPE,
      v_level      tqc_sys_escalation_levels.tsel_level%TYPE,
      v_user       tqc_sys_escalation_levels.tsel_assignee%TYPE,
      v_duration   tqc_sys_escalation_levels.tsel_duration%TYPE,
      v_cc         tqc_sys_escalation_levels.tsel_cc%TYPE
   );

   PROCEDURE reservedwords_proc (
      v_addedit    VARCHAR2,
      v_code       tqc_sys_reserved_words.tsrw_code%TYPE,
      v_syscode    tqc_sys_reserved_words.tsrw_sys_code%TYPE,
      v_tsrccode   tqc_sys_reserved_words.tsrw_tsrc_code%TYPE,
      v_type       tqc_sys_reserved_words.tsrw_type%TYPE,
      v_name       tqc_sys_reserved_words.tsrw_name%TYPE,
      v_desc       tqc_sys_reserved_words.tsrw_desc%TYPE
   );

   PROCEDURE addupdatecountry (
      v_addedit             VARCHAR2,
      v_tcou_code      IN   NUMBER,
      v_cou_sht_desc   IN   VARCHAR2,
      v_cou_name       IN   VARCHAR2
   );

   PROCEDURE authorizeaccount (v_user IN VARCHAR2, v_agn_code IN NUMBER);

   PROCEDURE addreportsubmodule (
      v_addedit             VARCHAR2,
      v_rsm_code       IN   NUMBER,
      v_rsm_name       IN   VARCHAR2,
      v_rsm_desc       IN   VARCHAR2,
      v_rsm_srm_code   IN   NUMBER
   );

   PROCEDURE assignreport (v_rpt_code IN NUMBER, v_rpt_rsm_code IN NUMBER);

   PROCEDURE unassignreport (v_rpt_code IN NUMBER, v_rpt_rsm_code IN NUMBER);

   PROCEDURE updatereportdetails (
      v_rpt_code          IN   NUMBER,
      v_rpt_description   IN   VARCHAR2
   );

   PROCEDURE occupations_prc (
      v_add_edit       IN   VARCHAR2,
      v_occ_code       IN   tqc_occupations.occ_code%TYPE,
      v_occ_sht_desc   IN   tqc_occupations.occ_sht_desc%TYPE,
      v_occ_name       IN   tqc_occupations.occ_name%TYPE,
      v_occ_sec_code   IN   NUMBER
   );

   PROCEDURE updateentities (
      v_add_edit               IN       VARCHAR2,
      v_ent_code               IN OUT   NUMBER,
      v_ent_sht_desc           IN       VARCHAR2,
      v_ent_name               IN       VARCHAR2,
      v_ent_physical_address   IN       VARCHAR2,
      v_ent_postal_address     IN       VARCHAR2,
      v_ent_twn_code           IN       NUMBER,
      v_ent_cou_code           IN       NUMBER,
      v_ent_email_address      IN       VARCHAR2,
      v_ent_fax                IN       VARCHAR2,
      v_ent_status             IN       VARCHAR2,
      v_ent_bbr_code           IN       NUMBER,
      v_ent_bank_acc_no        IN       VARCHAR2,
      v_ent_zip                IN       VARCHAR2,
      v_ent_sms_tel            IN       VARCHAR2,
      v_ent_created_by         IN       VARCHAR2,
      v_ent_date_created       IN       DATE,
      v_ent_pin                IN       VARCHAR2,
      v_ent_tel1               IN       VARCHAR2,
      v_ent_acc_no             IN       VARCHAR2,
      v_ent_status_remarks     IN       VARCHAR2,
      v_ent_brn_code           IN       NUMBER,
      v_ent_id_no              IN       VARCHAR2,
      v_ent_tel2               IN       VARCHAR2,
      v_ent_sec_code           IN       NUMBER,
      v_ent_runoff             IN       VARCHAR2,
      v_ent_old_acc_no         IN       VARCHAR2,
      v_ent_title              IN       VARCHAR2,
      v_ent_wef                IN       DATE,
      v_ent_wet                IN       DATE,
      v_ent_contact_person     IN       VARCHAR2,
      v_source                 IN       VARCHAR2
   );

   PROCEDURE save_client_details (
      v_type        VARCHAR2,
      v_clnt_code   NUMBER,
      v_pin_pass    VARCHAR2,
      v_pass        VARCHAR2,
      v_idno        VARCHAR2
   );

   PROCEDURE create_email_msg (
      v_clnt_code      IN   NUMBER,
      v_agn_code       IN   NUMBER,
      v_quot_code      IN   NUMBER,
      v_pol_code       IN   NUMBER,
      v_pol_no         IN   VARCHAR2,
      v_clm_no         IN   VARCHAR2,
      v_email_addr     IN   VARCHAR2,
      v_msg_subj       IN   VARCHAR2,
      v_msg_text       IN   VARCHAR2,
      v_sys_code       IN   NUMBER,
      v_sys_mod        IN   VARCHAR2,
      v_email_status   IN   VARCHAR2
   );

   PROCEDURE update_email_msg (
      v_email_code     IN   NUMBER,
      v_emailaddress   IN   VARCHAR2,
      v_subject        IN   VARCHAR2,
      v_emailmessage   IN   VARCHAR2,
      v_email_status   IN   VARCHAR2
   );

   PROCEDURE bussiness_person_proc (
      v_add_edit             IN   VARCHAR2,
      v_bpn_code             IN   NUMBER,
      v_bpn_id_no            IN   VARCHAR2,
      v_bpn_address          IN   VARCHAR2,
      v_bpn_tel              IN   VARCHAR2,
      v_bpn_mobile_no        IN   VARCHAR2,
      v_bpn_email            IN   VARCHAR2,
      v_bpn_type             IN   VARCHAR2,
      v_bpn_zip              IN   VARCHAR2,
      v_bpn_town             IN   VARCHAR2,
      v_bpn_cou_code         IN   NUMBER,
      v_bpn_name             IN   VARCHAR2,
      v_bpn_pin              IN   VARCHAR2,
      v_bpn_bbr_code         IN   NUMBER,
      v_bpn_bank_acc_no      IN   VARCHAR2,
      v_bpn_bbr_swift_code   IN   NUMBER,
      v_bpn_clnt_code        IN   NUMBER,
      v_bpn_payee_type       IN   VARCHAR2
   );

   PROCEDURE convert_lead_to_prospects (v_lds_code IN NUMBER);

   PROCEDURE convert_lead_to_client (
      v_lds_code      IN       NUMBER,
      v_clntshtdesc   OUT      VARCHAR2
   );

   PROCEDURE labels_prc (
      v_add_edit       IN   VARCHAR2,
      v_label_code     IN   tqc_labels.label_code%TYPE,
      v_label_name     IN   tqc_labels.label_name%TYPE,
      v_label_value    IN   tqc_labels.label_value%TYPE,
      v_label_status   IN   tqc_labels.label_status%TYPE,
      v_label_desc     IN   tqc_labels.label_desc%TYPE
   );

   TYPE agents_rec IS RECORD (
      agn_sht_desc           tqc_agencies.agn_sht_desc%TYPE,
      agn_code               tqc_agencies.agn_code%TYPE,
      agn_name               tqc_agencies.agn_name%TYPE,
      agn_brn_code           tqc_agencies.agn_brn_code%TYPE,
      brn_name               tqc_branches.brn_name%TYPE,
      agn_comm_allowed       tqc_agencies.agn_comm_allowed%TYPE,
      agn_runoff             tqc_agencies.agn_runoff%TYPE,
      agn_physical_address   tqc_agencies.agn_physical_address%TYPE,
      agn_email_address      tqc_agencies.agn_email_address%TYPE,
      agn_sms_tel            tqc_agencies.agn_sms_tel%TYPE
   );

   TYPE agents_ref IS REF CURSOR
      RETURN agents_rec;

   FUNCTION get_intermediaries (
      v_type          IN   VARCHAR2,
      v_agn_shtdesc   IN   VARCHAR2,
      v_agn_name      IN   VARCHAR2
   )
      RETURN agents_ref;

   PROCEDURE dists_prc (
      v_add_edit        IN   VARCHAR2,
      v_dist_code       IN   tqc_districts.dist_code%TYPE,
      v_dist_cou_code   IN   tqc_districts.dist_cou_code%TYPE,
      v_dist_sht_desc   IN   tqc_districts.dist_sht_desc%TYPE,
      v_dist_name       IN   tqc_districts.dist_name%TYPE,
      v_dist_sts_code   IN   tqc_districts.dist_sts_code%TYPE
   );

   PROCEDURE addupdateratingorg (
      v_addedit       IN   VARCHAR2,
      v_rorgcode      IN   tqc_rating_organizations.rorg_code%TYPE,
      v_rorgdesc      IN   tqc_rating_organizations.rorg_desc%TYPE,
      v_rorgshtdesc   IN   tqc_rating_organizations.rorg_sht_desc%TYPE
   );

   PROCEDURE addupdaterating (
      v_addedit    IN   VARCHAR2,
      v_orscode    IN   tqc_org_rating_starndards.ors_code%TYPE,
      v_orsdesc    IN   tqc_org_rating_starndards.ors_desc%TYPE,
      v_rorgcode   IN   tqc_org_rating_starndards.ors_rorg_code%TYPE
   );

   PROCEDURE bank_territory_prc (
      v_bnkt_add_edit                  VARCHAR2,
      v_bnkt_code             IN OUT   NUMBER,
      v_bnkt_territory_name            VARCHAR2,
      v_bnkt_sht_desc                  VARCHAR2,
      v_bnkt_bnk_code                  NUMBER
   );

   PROCEDURE transfer_prc (
      v_action                        VARCHAR2,
      v_tt_code              IN OUT   NUMBER,
      v_tt_trans_type                 VARCHAR2,
      v_tt_trans_date                 DATE,
      v_tt_trans_to_code              NUMBER,
      v_tt_trans_to_name              VARCHAR,
      v_tt_trans_from_code            NUMBER,
      v_tt_trans_from_name            VARCHAR,
      v_tt_done_by                    VARCHAR,
      v_tt_done_date                  DATE,
      v_tt_authorized                 VARCHAR,
      v_tt_authorized_by              VARCHAR,
      v_tt_authorized_date            DATE
   );

   PROCEDURE transfer_item_prc (
      v_action                         VARCHAR2,
      v_tti_code              IN OUT   NUMBER,
      v_tti_tt_code                    NUMBER,
      v_tti_item_code                  VARCHAR2,
      v_tti_item_name                  VARCHAR2,
      v_tti_item_sht_desc              VARCHAR2,
      v_tti_item_type                  VARCHAR2,
      v_tti_trans_date                 DATE,
      v_tti_trans_to                   VARCHAR2,
      v_tti_trans_from                 VARCHAR2,
      v_tti_done_by                    VARCHAR2,
      v_tti_done_date                  DATE,
      v_tti_authorized                 VARCHAR2,
      v_tti_authorized_by              VARCHAR2,
      v_tti_authorized_date            DATE
   );

  PROCEDURE add_transferable_items(
        v_item_code number, 
        v_item_name varchar2, 
        v_item_sht_desc varchar2, 
        v_item_type varchar2,
        v_trans_to number,
        v_trans_from number,  
        v_tt_code number,
        v_done_by varchar2
    );

   PROCEDURE remove_transfered_items (v_itt_code NUMBER);

   PROCEDURE authorize_transfer (v_tt_code NUMBER, v_tt_authorized_by VARCHAR);
   
function get_reg_code(
 v_reg_sht_desc varchar
)return number;

procedure transfer_branch(
   v_reg_from varchar,
   v_reg_to varchar,
   v_branch varchar
);
PROCEDURE transfer_branch_prc (    
                  v_from     NUMBER,
                  v_to       NUMBER,
                  v_item_code      NUMBER);
                  
    PROCEDURE transfer_agency_prc (  
      v_from     NUMBER,
      v_to       NUMBER,
      v_item_code   NUMBER
      );         

 PROCEDURE transfer_region_prc (  
  v_from     NUMBER,
  v_to       NUMBER,
  v_item_code NUMBER
  );
  PROCEDURE transfer_unit_prc (   
                                  v_from     NUMBER,
                                  v_to       NUMBER,
                                  v_item_code          NUMBER);
  PROCEDURE transfer_agent_prc (  
      v_from     NUMBER,
      v_to       NUMBER,
      v_item_code   NUMBER
      );
   FUNCTION get_field_varchar (v_col IN VARCHAR2, v_field IN VARCHAR2)
      RETURN VARCHAR2;
  
  PROCEDURE agent_type_proc (
      v_addedit           IN   VARCHAR2,
      v_agnty_code        IN   NUMBER,
      v_agnty_type   IN   VARCHAR2,
      v_agnty_type_sht_desc    IN   VARCHAR2,
      v_agnty_act_code    IN   NUMBER
   );
   
     PROCEDURE occupations_sections_prc (
      v_add_edit       IN   VARCHAR2,
      v_occ_code       IN   tqc_occupations.occ_code%TYPE,
      v_occ_sec_code   IN   NUMBER
   );
   
   PROCEDURE delete_agency_prc (
      v_agn_code   IN   NUMBER
   );
   PROCEDURE  updateddfailedrmks(
        v_addedit    IN   VARCHAR2,
        v_dfr_code           NUMBER    ,
        v_dfr_failed_remark  VARCHAR2,
        v_dfr_appl_level     VARCHAR2
        );
 FUNCTION get_form_field (   
    v_sys_code             IN NUMBER,
    v_field_name           IN VARCHAR2,
    v_field_col            IN VARCHAR2
   ) RETURN VARCHAR2;   
   PROCEDURE bank_details_prc(
                  v_add_edit            IN  VARCHAR,
                  v_bact_code           IN  tqc_bank_accounts.bact_code%TYPE,
                  v_bact_name           IN  tqc_bank_accounts.bact_name%TYPE,
                  v_bact_account_no     IN  tqc_bank_accounts.bact_account_no%TYPE,
                  v_bact_bbr_code       IN  tqc_bank_accounts.bact_bbr_code%TYPE,
                  v_bact_account_officer IN  tqc_bank_accounts.bact_account_officer%TYPE,
                  v_bact_cell_nos       IN  tqc_bank_accounts.bact_cell_nos%TYPE,
                  v_bact_tel_nos        IN  tqc_bank_accounts.bact_tel_nos%TYPE,
                  v_bact_account_type   IN  tqc_bank_accounts.bact_account_type%TYPE,
                  v_bact_default        IN  tqc_bank_accounts.bact_default%TYPE,
                  v_bact_cur_code       IN  tqc_bank_accounts.bact_cur_code%TYPE,
                  v_bact_acc_code       IN  tqc_bank_accounts.bact_acc_code%TYPE,
                  v_bact_iban           IN  tqc_bank_accounts.bact_iban%TYPE,
                  v_bact_status         IN  tqc_bank_accounts.bact_status%TYPE
                  );
  PROCEDURE failRelaunchUpdateHeader(
            v_dddCode NUMBER,
            v_username VARCHAR2, 
            v_policyNo VARCHAR2,
            v_account VARCHAR2, 
            v_book_date DATE, 
            v_fail_remarks VARCHAR2, 
            v_ddh_dd_code IN OUT NUMBER,
            v_status VARCHAR DEFAULT 'F',
            v_bnk_amt varchar2 default null
    );
 PROCEDURE agencies_audit_prc (v_agn_code IN NUMBER, v_posted_by VARCHAR2);
PROCEDURE spr_contact_persons_prc (
      v_add_edit         IN   VARCHAR,
      v_spr_short_desc   IN   NUMBER DEFAULT NULL,
      v_spr_code         IN   tqc_spr_contact_persons.spr_code%TYPE,
      v_sprcontname      IN   tqc_spr_contact_persons.spr_cnt_name%TYPE,
      v_sprconttitle     IN   tqc_spr_contact_persons.spr_cnt_title%TYPE,
      v_sprconttelno     IN   tqc_spr_contact_persons.spr_cnt_office_tel%TYPE,
      v_sprcontmobno     IN   tqc_spr_contact_persons.spr_cnt_home_tel%TYPE,
      v_sprcontemail     IN   tqc_spr_contact_persons.spr_cnt_email%TYPE
   );    
END tqc_setups_pkg;
/