--
-- TQC_SETUPS_PKG030215  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_setups_pkg030215 AS

PROCEDURE agency_holding_company_prc( 
    v_add_edit          IN  VARCHAR2,
    v_AHC_CODE          IN  TQC_AGENCY_HOLDING_COMPANY.AHC_CODE%TYPE,
    v_AHC_NAME          IN  TQC_AGENCY_HOLDING_COMPANY.AHC_NAME%TYPE,
    v_postalAddress     IN  TQC_AGENCY_HOLDING_COMPANY.AHC_POSTAL_ADDRESS%TYPE,
    v_physicalAddress   IN  TQC_AGENCY_HOLDING_COMPANY.AHC_PHYSICAL_ADDRESS%TYPE,
    v_telNumber         IN  TQC_AGENCY_HOLDING_COMPANY.AHC_TELEPHONE_NUMBER%TYPE,
    v_mobNumber         IN  TQC_AGENCY_HOLDING_COMPANY.AHC_MOBILE_NUMBER%TYPE,
    v_contactPerson     IN  TQC_AGENCY_HOLDING_COMPANY.AHC_CONTACT_PERSON%TYPE,
    v_error     OUT VARCHAR2);
    
 PROCEDURE banks_prc (
      v_add_edit                  IN       VARCHAR2,
      v_bnk_code                  IN       tqc_banks.bnk_code%TYPE,
      v_bnk_bank_name             IN       tqc_banks.bnk_bank_name%TYPE,
      v_bnk_remarks               IN       tqc_banks.bnk_remarks%TYPE,
      v_bnk_sht_desc              IN       tqc_banks.bnk_sht_desc%TYPE,
      v_bnk_ddr_code              IN       tqc_banks.bnk_ddr_code%TYPE,
      v_dd_format_desc            IN       tqc_banks.dd_format_desc%TYPE,
      v_bnk_dd_supported          IN      tqc_banks.bnk_dd_supported%TYPE,
      v_bnk_kba_code              IN       tqc_banks.bnk_kba_code%TYPE,
      v_bnk_eft_supported         IN       tqc_banks.bnk_eft_supported%TYPE,
      v_bnk_class_type            IN       tqc_banks.bnk_class_type%TYPE,
      v_bnk_accnt_digit_no        IN       tqc_banks.bnk_accnt_digit_no%TYPE,
      v_bnk_negotiated_bank       IN       tqc_banks.bnk_negotiated_bank%TYPE,
      v_bnk_administration_charge IN       tqc_banks.bnk_administration_charge%TYPE,
      v_bnk_logo IN       tqc_banks.BNK_LOGO%TYPE,
      v_error                     OUT      VARCHAR2
   );
    
PROCEDURE bank_branches_prc( 
    v_add_edit              IN  VARCHAR2,
    v_BBR_CODE              IN  TQC_BANK_BRANCHES.BBR_CODE%TYPE,
    v_BBR_BNK_CODE          IN  TQC_BANK_BRANCHES.BBR_BNK_CODE%TYPE,
    v_BBR_BRANCH_NAME       IN  TQC_BANK_BRANCHES.BBR_BRANCH_NAME%TYPE,
    v_BBR_REMARKS           IN  TQC_BANK_BRANCHES.BBR_REMARKS%TYPE,
    v_BBR_SHT_DESC          IN  TQC_BANK_BRANCHES.BBR_SHT_DESC%TYPE,
    v_BBR_REF_CODE          IN  TQC_BANK_BRANCHES.BBR_REF_CODE%TYPE,
    v_BBR_EFT_SUPPORTED     IN  TQC_BANK_BRANCHES.BBR_EFT_SUPPORTED%TYPE,
    v_BBR_DD_SUPPORTED      IN  TQC_BANK_BRANCHES.BBR_DD_SUPPORTED%TYPE,
    v_BBR_DATE_CREATED      IN  TQC_BANK_BRANCHES.BBR_DATE_CREATED%TYPE,
    v_BBR_CREATED_BY        IN  TQC_BANK_BRANCHES.BBR_CREATED_BY%TYPE,
    v_BBR_PHYSICAL_ADDRS    IN  TQC_BANK_BRANCHES.BBR_PHYSICAL_ADDRS%TYPE,
    v_BBR_POSTAL_ADDRS      IN  TQC_BANK_BRANCHES.BBR_POSTAL_ADDRS%TYPE,
    v_BBR_KBA_CODE          IN  TQC_BANK_BRANCHES.BBR_KBA_CODE%TYPE,
    v_error                 OUT VARCHAR2);
    
PROCEDURE agency_classes_prc( 
    v_add_edit  IN  VARCHAR2,
    v_AGNC_CLASS_CODE  IN  TQC_AGENCIES_CLASSES.AGNC_CLASS_CODE%TYPE,
    v_AGNC_CLASS_DESC  IN  TQC_AGENCIES_CLASSES.AGNC_CLASS_DESC%TYPE,
    v_error     OUT VARCHAR2);
    
  PROCEDURE currencies_prc (
      v_add_edit     IN       VARCHAR2,
      v_cur_code     IN       tqc_currencies.cur_code%TYPE,
      v_cur_symbol   IN       tqc_currencies.cur_symbol%TYPE,
      v_cur_desc     IN       tqc_currencies.cur_desc%TYPE,
      v_cur_rnd      IN       tqc_currencies.cur_rnd%TYPE,
      v_error        OUT      VARCHAR2,
      v_cur_num_word      IN       tqc_currencies.cur_num_word%TYPE,
      v_cur_decimal_word     IN       tqc_currencies.cur_decimal_word%TYPE
   );

PROCEDURE currencies_denominations_prc( 
    v_add_edit      IN  VARCHAR2,
    v_CUD_CODE      IN  TQC_CURRENCY_DENOMINATIONS.CUD_CODE%TYPE,
    v_CUD_CUR_CODE  IN  TQC_CURRENCY_DENOMINATIONS.CUD_CUR_CODE%TYPE,
    v_CUD_VALUE     IN  TQC_CURRENCY_DENOMINATIONS.CUD_VALUE%TYPE,
    v_CUD_NAME      IN  TQC_CURRENCY_DENOMINATIONS.CUD_NAME%TYPE,
    v_CUD_WEF       IN  TQC_CURRENCY_DENOMINATIONS.CUD_WEF%TYPE,
    v_error         OUT VARCHAR2);
    
PROCEDURE currency_rates_prc (
      v_add_edit            IN   VARCHAR2,
      v_crt_code            IN   tqc_currency_rates.crt_code%TYPE,
      v_crt_cur_code        IN   tqc_currency_rates.crt_cur_code%TYPE,
      v_crt_rate            IN   tqc_currency_rates.crt_rate%TYPE,
      v_crt_date            IN   tqc_currency_rates.crt_date%TYPE,
      v_crt_base_cur_code   IN   tqc_currency_rates.crt_base_cur_code%TYPE,
       v_crt_wef            IN   tqc_currency_rates.CRT_WEF%TYPE,
        v_crt_wet           IN   tqc_currency_rates.CRT_WET%TYPE,
        v_user              IN   VARCHAR2 default null
   );
 PROCEDURE acc_types_prc(v_act_code     NUMBER,
                            v_clnt_mapping VARCHAR2,
                            v_wht_rate     NUMBER,
                            v_ovr_rate     NUMBER,
                            v_comm_rate    NUMBER,
                            v_ac_format    VARCHAR2,
                            v_vat_rate     NUMBER,
                            v_sht_desc     VARCHAR2,
                            v_act_no_gen_code   TQC_ACCOUNT_TYPES.ACT_NO_GEN_CODE%TYPE
                            );
                            
PROCEDURE sectors_prc( 
    v_add_edit          IN  VARCHAR2,
    v_SEC_CODE          IN  TQC_SECTORS.SEC_CODE%TYPE,
    v_SEC_SHT_DESC      IN  TQC_SECTORS.SEC_SHT_DESC%TYPE,
    v_SEC_NAME          IN  TQC_SECTORS.SEC_NAME%TYPE    
    );
    
PROCEDURE service_provider_types_prc( 
    v_add_edit          IN  VARCHAR2,
    v_SPT_CODE          IN  TQC_SERVICE_PROVIDER_TYPES.SPT_CODE%TYPE,
    v_SPT_SHT_DESC      IN  TQC_SERVICE_PROVIDER_TYPES.SPT_SHT_DESC%TYPE,
    v_SPT_NAME          IN  TQC_SERVICE_PROVIDER_TYPES.SPT_NAME%TYPE,
    v_SPT_STATUS        IN  TQC_SERVICE_PROVIDER_TYPES.SPT_STATUS%TYPE,
    v_SPT_WHTX_RATE     IN  TQC_SERVICE_PROVIDER_TYPES.SPT_WHTX_RATE%TYPE,
    v_SPT_VAT_RATE      IN  TQC_SERVICE_PROVIDER_TYPES.SPT_VAT_RATE%TYPE 
    );
    
 PROCEDURE countries_prc (
      v_add_edit             IN   VARCHAR2,
      v_cou_code             IN   tqc_countries.cou_code%TYPE DEFAULT NULL,
      v_cou_sht_desc         IN   tqc_countries.cou_sht_desc%TYPE DEFAULT NULL,
      v_cou_name             IN   tqc_countries.cou_name%TYPE DEFAULT NULL,
      v_cou_base_curr        IN   tqc_countries.cou_base_curr%TYPE
            DEFAULT NULL,
      v_cou_nationality      IN   tqc_countries.cou_nationality%TYPE
            DEFAULT NULL,
      v_cou_zip_code         IN   tqc_countries.cou_zip_code%TYPE DEFAULT NULL,
      v_cou_admin_reg_type   IN   tqc_countries.cou_admin_reg_type%TYPE
            DEFAULT NULL,
      v_cou_schegen          IN   tqc_countries.cou_schegen%TYPE DEFAULT NULL,
      v_cou_emb_code         IN   tqc_countries.cou_emb_code%TYPE DEFAULT NULL,
      v_cou_curr_serial      IN   tqc_countries.cou_curr_serial%TYPE  DEFAULT NULL,
      v_cou_mobile_prefix      IN   tqc_countries.cou_mobile_prefix %TYPE  DEFAULT NULL,
       v_cou_client_number      IN   tqc_countries.cou_client_number %TYPE  DEFAULT NULL
   );

 PROCEDURE towns_prc( 
      v_add_edit        IN  VARCHAR2,
      v_TWN_CODE        IN TQC_TOWNS.TWN_CODE%TYPE,
      v_TWN_COU_CODE    IN TQC_TOWNS.TWN_COU_CODE%TYPE,
      v_TWN_SHT_DESC    IN TQC_TOWNS.TWN_SHT_DESC%TYPE,
      v_TWN_NAME        IN TQC_TOWNS.TWN_NAME%TYPE,
      v_TWN_STS_CODE     IN TQC_TOWNS.TWN_STS_CODE%TYPE
    );
    
PROCEDURE locations_prc( 
      v_add_edit        IN  VARCHAR2,
      v_LOC_CODE        IN  TQC_LOCATIONS.LOC_CODE%TYPE,
      v_LOC_TWN_CODE    IN  TQC_LOCATIONS.LOC_TWN_CODE%TYPE,
      v_LOC_SHT_DESC    IN  TQC_LOCATIONS.LOC_SHT_DESC%TYPE,
      v_LOC_NAME        IN  TQC_LOCATIONS.LOC_NAME%TYPE
    );
    
PROCEDURE parameters_prc( 
      v_add_edit        IN  VARCHAR2,
      v_PARAM_CODE      IN  TQC_PARAMETERS.PARAM_CODE%TYPE,
      v_PARAM_NAME      IN  TQC_PARAMETERS.PARAM_NAME%TYPE,
      v_PARAM_VALUE     IN  TQC_PARAMETERS.PARAM_VALUE%TYPE,
      v_PARAM_STATUS    IN  TQC_PARAMETERS.PARAM_STATUS%TYPE,
      v_PARAM_DESC      IN  TQC_PARAMETERS.PARAM_DESC%TYPE
    );
    
 PROCEDURE service_providers_prc (
      v_add_edit               IN   VARCHAR2,
      v_spr_code               IN   tqc_service_providers.spr_code%TYPE,
      v_spr_sht_desc           IN   tqc_service_providers.spr_sht_desc%TYPE,
      v_spr_name               IN   tqc_service_providers.spr_name%TYPE,
      v_spr_physical_address   IN   tqc_service_providers.spr_physical_address%TYPE,
      v_spr_postal_address     IN   tqc_service_providers.spr_postal_address%TYPE,
      v_spr_twn_code           IN   tqc_service_providers.spr_twn_code%TYPE,
      v_spr_cou_code           IN   tqc_service_providers.spr_cou_code%TYPE,
      v_spr_spt_code           IN   tqc_service_providers.spr_spt_code%TYPE,
      v_spr_phone              IN   tqc_service_providers.spr_phone%TYPE,
      v_spr_fax                IN   tqc_service_providers.spr_fax%TYPE,
      v_spr_email              IN   tqc_service_providers.spr_email%TYPE,
      v_spr_title              IN   tqc_service_providers.spr_title%TYPE,
      v_spr_zip                IN   tqc_service_providers.spr_zip%TYPE,
      v_spr_wef                IN   tqc_service_providers.spr_wef%TYPE,
      v_spr_wet                IN   tqc_service_providers.spr_wet%TYPE,
      v_spr_contact            IN   tqc_service_providers.spr_contact%TYPE,
      v_spr_aims_code          IN   tqc_service_providers.spr_aims_code%TYPE,
      v_spr_bbr_code           IN   tqc_service_providers.spr_bbr_code%TYPE,
      v_spr_bank_acc_no        IN   tqc_service_providers.spr_bank_acc_no%TYPE,
      v_spr_created_by         IN   tqc_service_providers.spr_created_by%TYPE,
      v_spr_date_created       IN   tqc_service_providers.spr_date_created%TYPE,
      v_spr_status_remarks     IN   tqc_service_providers.spr_status_remarks%TYPE,
      v_spr_status             IN   tqc_service_providers.spr_status%TYPE,
      v_spr_pin_number         IN   tqc_service_providers.spr_pin_number%TYPE,
      v_spr_trs_occupation     IN   tqc_service_providers.spr_trs_occupation%TYPE,
      v_spr_proff_body         IN   tqc_service_providers.spr_proff_body%TYPE,
      v_spr_pin                IN   tqc_service_providers.spr_pin%TYPE,
      v_spr_doc_phone          IN   tqc_service_providers.spr_doc_phone%TYPE,
      v_spr_doc_email          IN   tqc_service_providers.spr_doc_email%TYPE,
      v_spr_gl_acc_no          IN   tqc_service_providers.spr_gl_acc_no%TYPE,
      v_sprinhouse             IN   tqc_service_providers.spr_inhouse%TYPE,
      v_spr_sms_number          IN   tqc_service_providers.spr_sms_number%TYPE,
      v_spr_contact_person      IN  tqc_service_providers.spr_contact_person%TYPE,
      v_spr_cont_person_phone   IN  tqc_service_providers.spr_cont_person_phone%TYPE,
      v_spr_invoice_number   IN  tqc_service_providers.SPR_INVOICE_NUMBER%TYPE,
      v_spr_clnt_code   IN  tqc_service_providers.SPR_CLNT_CODE%TYPE,
      v_spr_bpn_code   IN  tqc_service_providers.SPR_BPN_CODE%TYPE
   );
    
PROCEDURE service_prov_activities_prc( 
    v_add_edit          IN  VARCHAR2,
    v_SPA_CODE          IN  TQC_SERV_PRV_ACTIVITIES.SPA_CODE%TYPE,
    v_SPA_SPT_CODE      IN  TQC_SERV_PRV_ACTIVITIES.SPA_SPT_CODE%TYPE,
    v_SPA_SPT_SHT_DESC  IN  TQC_SERV_PRV_ACTIVITIES.SPA_SPT_SHT_DESC%TYPE,
    v_SPA_SPR_CODE      IN  TQC_SERV_PRV_ACTIVITIES.SPA_SPR_CODE%TYPE,
    v_SPA_SPR_SHT_DESC  IN  TQC_SERV_PRV_ACTIVITIES.SPA_SPR_SHT_DESC%TYPE,
    v_SPT_MAIN_ACT      IN  TQC_SERV_PRV_ACTIVITIES.SPT_MAIN_ACT%TYPE,
    v_SPA_DESC          IN  TQC_SERV_PRV_ACTIVITIES.SPA_DESC%TYPE,
    v_SPA_SPTA_CODE     IN  NUMBER
    );
    
 PROCEDURE agencies_prc (
   v_add_edit                IN   VARCHAR2,
   v_agn_code                IN   tqc_agencies.agn_code%TYPE,
   v_agn_act_code            IN   tqc_agencies.agn_act_code%TYPE,
   v_agn_sht_desc            IN   tqc_agencies.agn_sht_desc%TYPE,
   v_agn_name                IN   tqc_agencies.agn_name%TYPE,
   v_agn_physical_address    IN   tqc_agencies.agn_physical_address%TYPE,
   v_agn_postal_address      IN   tqc_agencies.agn_postal_address%TYPE,
   v_agn_twn_code            IN   tqc_agencies.agn_twn_code%TYPE,
   v_agn_cou_code            IN   tqc_agencies.agn_cou_code%TYPE,
   v_agn_email_address       IN   tqc_agencies.agn_email_address%TYPE,
   v_agn_web_address         IN   tqc_agencies.agn_web_address%TYPE,
   v_agn_zip                 IN   tqc_agencies.agn_zip%TYPE,
   v_agn_contact_person      IN   tqc_agencies.agn_contact_person%TYPE,
   v_agn_contact_title       IN   tqc_agencies.agn_contact_title%TYPE,
   v_agn_tel1                IN   tqc_agencies.agn_tel1%TYPE,
   v_agn_tel2                IN   tqc_agencies.agn_tel2%TYPE,
   v_agn_fax                 IN   tqc_agencies.agn_fax%TYPE,
   v_agn_acc_no              IN   tqc_agencies.agn_acc_no%TYPE,
   v_agn_pin                 IN   tqc_agencies.agn_pin%TYPE,
   v_agn_agent_commission    IN   tqc_agencies.agn_agent_commission%TYPE,
   v_agn_credit_allowed      IN   tqc_agencies.agn_credit_allowed%TYPE,
   v_agn_agent_wht_tax       IN   tqc_agencies.agn_agent_wht_tax%TYPE,
   v_agn_print_dbnote        IN   tqc_agencies.agn_print_dbnote%TYPE,
   v_agn_status              IN   tqc_agencies.agn_status%TYPE,
   v_agn_date_created        IN   tqc_agencies.agn_date_created%TYPE,
   v_agn_created_by          IN   tqc_agencies.agn_created_by%TYPE,
   v_agn_reg_code            IN   tqc_agencies.agn_reg_code%TYPE,
   v_agn_comm_reserve_rate   IN   tqc_agencies.agn_comm_reserve_rate%TYPE,
   v_agn_annual_budget       IN   tqc_agencies.agn_annual_budget%TYPE,
   v_agn_status_eff_date     IN   tqc_agencies.agn_status_eff_date%TYPE,
   v_agn_credit_period       IN   tqc_agencies.agn_credit_period%TYPE,
   v_agn_comm_stat_eff_dt    IN   tqc_agencies.agn_comm_stat_eff_dt%TYPE,
   v_agn_comm_status_dt      IN   tqc_agencies.agn_comm_status_dt%TYPE,
   v_agn_comm_allowed        IN   tqc_agencies.agn_comm_allowed%TYPE,
   v_agn_checked             IN   tqc_agencies.agn_checked%TYPE,
   v_agn_checked_by          IN   tqc_agencies.agn_checked_by%TYPE,
   v_agn_check_date          IN   tqc_agencies.agn_check_date%TYPE,
   v_agn_comp_comm_arrears   IN   tqc_agencies.agn_comp_comm_arrears%TYPE,
   v_agn_reinsurer           IN   tqc_agencies.agn_reinsurer%TYPE,
   v_agn_brn_code            IN   tqc_agencies.agn_brn_code%TYPE,
   v_agn_town                IN   tqc_agencies.agn_town%TYPE,
   v_agn_country             IN   tqc_agencies.agn_country%TYPE,
   v_agn_status_desc         IN   tqc_agencies.agn_status_desc%TYPE,
   v_agn_id_no               IN   tqc_agencies.agn_id_no%TYPE,
   v_agn_con_code            IN   tqc_agencies.agn_con_code%TYPE,
   v_agn_agn_code            IN   tqc_agencies.agn_agn_code%TYPE,
   v_agn_sms_tel             IN   tqc_agencies.agn_sms_tel%TYPE,
   v_agn_ahc_code            IN   tqc_agencies.agn_ahc_code%TYPE,
   v_agn_sec_code            IN   tqc_agencies.agn_sec_code%TYPE,
   v_agn_agnc_class_code     IN   tqc_agencies.agn_agnc_class_code%TYPE,
   v_agn_expiry_date         IN   tqc_agencies.agn_expiry_date%TYPE,
   v_agn_license_no          IN   tqc_agencies.agn_license_no%TYPE,
   v_agn_runoff              IN   tqc_agencies.agn_runoff%TYPE,
   v_agn_licensed            IN   tqc_agencies.agn_licensed%TYPE,
   v_agn_license_grace_pr    IN   tqc_agencies.agn_license_grace_pr%TYPE,
   v_agn_old_acc_no          IN   tqc_agencies.agn_old_acc_no%TYPE,
   v_agn_status_remarks      IN   tqc_agencies.agn_status_remarks%TYPE,
   v_agn_bbr_code            IN   tqc_agencies.agn_bbr_code%TYPE DEFAULT NULL,
   v_agn_bank_acc_no         IN   tqc_agencies.agn_bank_acc_no%TYPE
         DEFAULT NULL,
   v_agn_unique_prefix       IN   tqc_agencies.agn_unique_prefix%TYPE
         DEFAULT NULL,
   v_agn_state_code          IN   tqc_agencies.agn_state_code%TYPE
         DEFAULT NULL,
   v_agn_crdt_rting          IN   tqc_agencies.agn_crdt_rting%TYPE
         DEFAULT NULL,
   v_agn_subagent            IN   tqc_agencies.agn_subagent%TYPE DEFAULT NULL,
   v_agn_main_agn_code       IN   tqc_agencies.agn_main_agn_code%TYPE
         DEFAULT NULL,
   v_agn_clnt_code           IN   tqc_agencies.agn_clnt_code%TYPE DEFAULT NULL,
   v_agn_account_manager     IN   tqc_agencies.AGN_ACCOUNT_MANAGER%TYPE DEFAULT NULL,
   v_agn_credit_limit     IN   tqc_agencies.AGN_CREDIT_LIMIT%TYPE DEFAULT NULL,
    v_agn_bru_code     IN   tqc_agencies.agn_bru_code%TYPE DEFAULT NULL,
     v_agn_local_international     IN   tqc_agencies.agn_local_international%TYPE DEFAULT NULL,
     v_agn_regulator_number     IN   tqc_agencies.agn_regulator_number%TYPE DEFAULT NULL,
      v_AGN_RORG_CODE     IN   tqc_agencies.AGN_RORG_CODE%TYPE DEFAULT NULL,
     v_AGN_ORS_CODE     IN   tqc_agencies.AGN_ORS_CODE %TYPE DEFAULT NULL,
     v_agn_allocate_cert     IN   tqc_agencies.agn_allocate_cert %TYPE DEFAULT NULL,
     v_agn_bounced_chq     IN   tqc_agencies.AGN_BOUNCED_CHQ %TYPE DEFAULT NULL,
     v_AGN_BPN_CODE     IN   tqc_agencies.AGN_BPN_CODE %TYPE DEFAULT NULL,
     v_AGN_AGENT_TYPE     IN   tqc_agencies.AGN_AGENT_TYPE %TYPE DEFAULT NULL,
     v_AGN_GROUP     IN   tqc_agencies.AGN_GROUP %TYPE DEFAULT NULL
) ;
PROCEDURE payment_modes_prc( 
    v_add_edit          IN  VARCHAR2,
    v_PMOD_CODE         IN  TQC_PAYMENT_MODES.PMOD_CODE%TYPE,
    v_PMOD_SHT_DESC     IN  TQC_PAYMENT_MODES.PMOD_SHT_DESC%TYPE,
    v_PMOD_DESC         IN  TQC_PAYMENT_MODES.PMOD_DESC%TYPE,
    v_PMOD_NARATION     IN  TQC_PAYMENT_MODES.PMOD_NARATION%TYPE,
    v_PMOD_DEFAULT      IN  TQC_PAYMENT_MODES.PMOD_DEFAULT%TYPE
    );
    
PROCEDURE client_titles_prc( 
    v_add_edit          IN  VARCHAR2,
    v_CLT_CODE          IN  TQC_CLIENT_TITLES.CLT_CODE%TYPE,
    v_CLT_SHT_DESC      IN  TQC_CLIENT_TITLES.CLT_SHT_DESC%TYPE,
    v_CLNT_DESC          IN  TQC_CLIENT_TITLES.CLNT_DESC%TYPE    
    );
    
PROCEDURE grant_prov_System (
        v_prov_code     IN  TQC_SERVICE_PROVIDERS.SPR_CODE%TYPE,
        v_sys_code      IN  TQC_SYSTEMS.SYS_CODE%TYPE,
        v_created_by    IN  VARCHAR2);
        
PROCEDURE revoke_prov_System (
        v_prov_code     IN  TQC_SERVICE_PROVIDERS.SPR_CODE%TYPE,
        v_sys_code      IN  TQC_SYSTEMS.SYS_CODE%TYPE);
        
  PROCEDURE prospects_prc (
      v_addedit              VARCHAR2,
      v_prospects_tab   IN   tqc_prospects_tab,
      v_prs_code         OUT  NUMBER
   );

PROCEDURE holidays_prc(v_addEdit       VARCHAR2, v_holidays_tab IN TQC_HOLIDAYS_TAB);

PROCEDURE searchClients(v_username VARCHAR2, v_search_date DATE);

PROCEDURE generateDirectDebitRecs(v_username VARCHAR2, v_searched VARCHAR2, v_dd_bbr_code NUMBER, v_dd_company_acc VARCHAR2, v_search_date DATE,
      v_install_day      number);

PROCEDURE deleteDirectDebit(v_username VARCHAR2, v_dd_code NUMBER, v_dd_status VARCHAR2);

PROCEDURE authoriseDirectDebit(v_username VARCHAR2, v_dd_code NUMBER, v_dd_status VARCHAR2);

PROCEDURE deleteDirectDebitDetail(v_username VARCHAR2, v_ddd_code NUMBER, v_dd_status VARCHAR2);

PROCEDURE generateReceipts(v_username VARCHAR2, v_dd_code NUMBER, v_dd_receipted VARCHAR2, v_dd_book_date DATE);

PROCEDURE failUpdateDDHeader(v_username VARCHAR2, v_ddh_code NUMBER, v_fail_date DATE, v_fail_remarks VARCHAR2, v_ddh_dd_code NUMBER);
 PROCEDURE failUpdateDDHeader2(v_username VARCHAR2, v_ddh_code NUMBER, v_book_date DATE, v_fail_remarks VARCHAR2, v_ddh_dd_code IN OUT NUMBER,v_status VARCHAR DEFAULT 'F');
PROCEDURE failUpdateHeader(v_dddCode    NUMBER,v_username VARCHAR2, v_policyNo VARCHAR2,v_account VARCHAR2, v_book_date DATE, v_fail_remarks VARCHAR2, v_ddh_dd_code IN OUT NUMBER,v_status VARCHAR DEFAULT 'F');

PROCEDURE relaunchDDHeader(v_username VARCHAR2, v_ddh_code NUMBER, v_ddh_status VARCHAR2, v_ddh_dd_code NUMBER);

PROCEDURE stopLaunchDDHeader(v_username VARCHAR2, v_ddh_code NUMBER, v_ddh_status VARCHAR2, v_ddh_dd_code NUMBER);

  PROCEDURE service_provider_type_act_prc (
      v_add_edit        IN   VARCHAR2,
      v_spta_code       IN   tqc_serv_prv_type_actvts.spta_code%TYPE,
      v_spta_spt_code   IN   tqc_serv_prv_type_actvts.spta_spt_code%TYPE,
      v_spta_sht_desc   IN   tqc_serv_prv_type_actvts.spta_sht_desc%TYPE,
      v_spta_desc       IN   tqc_serv_prv_type_actvts.spta_desc%TYPE,
      v_spta_sms_msgt_code IN   tqc_serv_prv_type_actvts.spta_sms_msgt_code%TYPE DEFAULT NULL, 
      v_spta_email_msgt_code IN   tqc_serv_prv_type_actvts.spta_email_msgt_code%TYPE DEFAULT NULL,
       v_spta_send_msg_default IN   tqc_serv_prv_type_actvts.SPTA_SEND_MSG_DEFAULT%TYPE DEFAULT NULL, 
      v_spta_send_email_default IN   tqc_serv_prv_type_actvts.SPTA_SEND_EMAIL_DEFAULT%TYPE DEFAULT NULL
      
   );
   

 PROCEDURE message_templates_prc(
        v_add_edit        IN VARCHAR2,
        v_MSGT_CODE       IN   TQC_MSG_TEMPLATES.MSGT_CODE      %TYPE, 
        v_MSGT_SHT_DESC   IN   TQC_MSG_TEMPLATES.MSGT_SHT_DESC  %TYPE, 
        v_MSGT_MSG        IN   TQC_MSG_TEMPLATES.MSGT_MSG       %TYPE, 
        v_MSGT_SYS_CODE   IN   TQC_MSG_TEMPLATES.MSGT_SYS_CODE  %TYPE, 
        v_MSGT_SYS_MODULE IN TQC_MSG_TEMPLATES.MSGT_SYS_MODULE%TYPE, 
        v_MSGT_TYPE       IN  TQC_MSG_TEMPLATES.MSGT_TYPE      %TYPE
        );
        
 PROCEDURE messageTemplates_prc(v_addEdit  VARCHAR2, v_messageTemplates_tab IN TQC_MSG_TEMPLATES_TAB);
        
 PROCEDURE orgDivLevelsType_prc(v_addEdit  VARCHAR2, v_orgDivLevelsType_tab IN TQC_ORG_DIV_LEVELS_TYPE_TAB);

 PROCEDURE orgDivisionLevels_prc(v_addEdit  VARCHAR2, v_orgDivLevels_tab IN TQC_ORG_DIVISION_LEVELS_TAB);
 
 PROCEDURE orgSubDivisions_prc(v_addEdit  VARCHAR2, v_orgSubDivisions_tab IN TQC_ORG_SUBDIVISIONS_TAB);
 -- PROCEDURE agencyActivities_prc(v_addEdit  VARCHAR2, v_agencyActivities_tab IN TQC_AGENCY_ACTIVITIES_TAB);

PROCEDURE agencyActivities_prc(v_addEdit  IN VARCHAR2, 
                                                          
                               v_aac_code           NUMBER ,
                               v_aac_sys_code       NUMBER ,
                               v_aac_acty_code      NUMBER ,
                               v_aac_wef            DATE,
                               v_aac_estimate_wet   DATE,
                               v_aac_actual_wet     DATE,
                               v_aac_remarks        VARCHAR2 ,
                               v_aac_agn_code       NUMBER ,                              
                               v_aac_act_type       varchar2, 
                               v_aac_reasons        varchar2,
                               v_aac_by_code        Number                         
                                                    
                                                              
                               );
 
 PROCEDURE holidayDefinitions_prc(v_addEdit  VARCHAR2, v_holidayDefinitions_tab IN TQC_HOLIDAYS_DEFINITIONS_TAB);
 
 PROCEDURE postalCodes_prc(v_addEdit  VARCHAR2, v_postalCodes_tab IN TQC_POSTAL_CODES_TAB);
 
 PROCEDURE activityTypes_prc(v_addEdit  VARCHAR2, v_activityTypes_tab IN TQC_ACTIVITY_TYPES_TAB);
 
 PROCEDURE orgSubDivPrevHeads_prc(v_addEdit  VARCHAR2, v_orgSubDivPrevHead_tab IN TQC_ORG_SUBDIV_PREV_HEADS_TAB);
 
 PROCEDURE sysPostLevels_prc(v_addEdit  VARCHAR2, v_sysPostLevels_tab IN TQC_SYS_POST_LEVELS_TAB);
 
 PROCEDURE sysPosts_prc(v_addEdit  VARCHAR2, v_sysPosts_tab IN TQC_SYS_POSTS_TAB);
 
 PROCEDURE sysPrcssSubAreaLmts_prc(v_addEdit  VARCHAR2, v_sysPrcssSubAreaLmts_tab IN TQC_SYS_PRCSS_SUBAREA_LMTS_TAB);
 
 PROCEDURE sysPrcssSubAreaLvls_prc(v_addEdit  VARCHAR2, v_sysPrcssSubAreaLvls_tab IN TQC_SYS_PRCSS_SUBAREA_LVLS_TAB);
 
 PROCEDURE userSystems_prc(v_addEdit  VARCHAR2, v_userSystems_tab IN TQC_USER_SYSTEMS_TAB);
 
 PROCEDURE agencySystems_prc(v_addEdit  VARCHAR2, v_agencySystems_tab IN TQC_AGENCY_SYSTEMS_TAB);
PROCEDURE claim_paymentMode_prc( v_add_edit VARCHAR2,
                                   v_code TQC_CLM_PAYMENT_MODES.CPM_CODE%TYPE  ,
                                   v_sht_desc TQC_CLM_PAYMENT_MODES.CPM_SHT_DESC%TYPE ,
                                   v_desc TQC_CLM_PAYMENT_MODES.CPM_DESC%TYPE ,
                                   v_remarks TQC_CLM_PAYMENT_MODES.CPM_REMARKS%TYPE ,
                                   v_max_amt TQC_CLM_PAYMENT_MODES.CPM_MAX_AMT%TYPE ,
                                   v_min_amt TQC_CLM_PAYMENT_MODES.CPM_MIN_AMT%TYPE ,
                                   v_default TQC_CLM_PAYMENT_MODES.CPM_DEFAULT%TYPE );
PROCEDURE mobilePaymentTypes_prc(        v_add_edit VARCHAR2,
                                         v_code   TQC_MOBILE_PYMNT_TYPES.MPT_CODE%TYPE ,
                                         v_sht_desc TQC_MOBILE_PYMNT_TYPES.MPT_SHT_DESC%TYPE ,
                                         v_desc TQC_MOBILE_PYMNT_TYPES.MPT_DESC%TYPE , 
                                         v_min_amt_allowed TQC_MOBILE_PYMNT_TYPES.MPT_MIN_AMT_ALLOWED%TYPE , 
                                         v_max_amt_allowed TQC_MOBILE_PYMNT_TYPES.MPT_MAX_AMT_ALLOWED%TYPE ,
                                         v_cou_code TQC_MOBILE_PYMNT_TYPES.MPT_COU_CODE%TYPE
                                   );

PROCEDURE mobileTypesPrefix_prc(      v_add_edit VARCHAR2,
                                        v_code TQC_MOB_PYMNT_TYPES_PREFIXES.MPTP_CODE%TYPE,
                                        v_mob_no_prefix TQC_MOB_PYMNT_TYPES_PREFIXES.MPTP_MOB_NO_PREFIX%TYPE ,
                                        v_mpt_code TQC_MOB_PYMNT_TYPES_PREFIXES.MPTP_MPT_CODE%TYPE
                                   );
PROCEDURE userGroup_prc(     
                                        v_add_edit VARCHAR2,
                                        v_code tqc_group_users.GUSR_CODE %TYPE,
                                        v_grp_usr_code tqc_group_users.GUSR_GRP_USR_CODE %TYPE,
                                        v_usr_code tqc_group_users.GUSR_USR_CODE %TYPE
                                   );
                                   
PROCEDURE clientGroup_prc(   v_add_edit varchar2,  
                                        v_code     TQC_GROUP_CLIENT.GRP_CODE%TYPE, 
                                        v_name     TQC_GROUP_CLIENT.GRP_NAME%TYPE, 
                                        v_minimum  TQC_GROUP_CLIENT.GRP_MINIMUM%TYPE,
                                        v_maximum  TQC_GROUP_CLIENT.GRP_MAXIMUM%TYPE
                                   );
 PROCEDURE clientGroupMembers_prc(       v_add_edit varchar2,  
                                         v_code TQC_GROUP_CLNT_DTLS.GRPD_CODE%TYPE,
                                         v_clnt_code TQC_GROUP_CLNT_DTLS.GRPD_CODE%TYPE, 
                                         v_grp_code TQC_GROUP_CLNT_DTLS.GRPD_GRP_CODE%TYPE
                                   );
  PROCEDURE printer_server_prc(         v_add_edit varchar2,  
                                        v_code Number ,
                                        v_name varchar2,
                                        v_filter varchar2,
                                        v_uri varchar2,
                                        v_filter_command varchar2,
                                        v_sec_username varchar2,
                                        v_sec_password varchar2,
                                        v_sec_auth_type varchar2,
                                        v_sec_encrpt_type varchar2, 
                                        v_proxy_host varchar2,
                                        v_proxy_port varchar2,
                                        v_proxy_username varchar2,
                                        v_proxy_pasword varchar2, 
                                        v_proxy_authen_type varchar2
                                   
                                   
                                   
                                   );
                                   
                                   
/* PROCEDURE adm_regions_prc( 
        v_ADD_EDIT         IN  VARCHAR2,
        v_ADM_REG_CODE     IN  TQC_ADMIN_REGIONS.ADM_REG_CODE%TYPE,
        v_ADM_REG_SHT_DESC IN  TQC_ADMIN_REGIONS.ADM_REG_SHT_DESC%TYPE,
        v_ADM_REG_NAME     IN TQC_ADMIN_REGIONS.ADM_REG_NAME%TYPE, 
        v_ADM_REG_COU_CODE IN TQC_ADMIN_REGIONS.ADM_REG_COU_CODE%TYPE
    );
    */
     PROCEDURE states_prc( 
      v_add_edit        IN  VARCHAR2,
      v_STS_CODE        IN TQC_STATES.STS_CODE%TYPE,
      v_STS_COU_CODE    IN TQC_STATES.STS_COU_CODE%TYPE,
      v_STS_SHT_DESC    IN TQC_STATES.STS_SHT_DESC%TYPE,
      v_STS_NAME        IN TQC_STATES.STS_NAME%TYPE
    );
   PROCEDURE save_lead_details (
      v_lds_code       NUMBER,
      v_desc           VARCHAR2,
      v_cmp_code       NUMBER,
      v_cotel          VARCHAR2,
      v_title          VARCHAR2,
      v_surname        VARCHAR2,
      v_oth_name       VARCHAR2,
      v_mob_no         VARCHAR2,
      v_fax            VARCHAR2,
      v_email          VARCHAR2,
      v_post_addr      VARCHAR2,
      v_post_code      VARCHAR2,
      v_phy_addr       VARCHAR2,
      v_ldsrccode      NUMBER,
      v_lstscode       NUMBER,
      v_lead_date      DATE,
      v_converted      VARCHAR2,
      v_port_name      VARCHAR2,
      v_port_contr     VARCHAR2,
      v_port_amt       NUMBER,
      v_port_sale      VARCHAR2,
      v_port_clse      DATE,
      v_ann_rev        NUMBER,
      v_website        VARCHAR2,
      v_industry       VARCHAR2,
      v_cou_code       NUMBER,
      v_state_code     NUMBER,
      v_town_code      NUMBER,
      v_org_code       NUMBER,
      v_user_code      NUMBER,
      v_team_code      NUMBER,
      v_acc_code       NUMBER,
      v_prod_code      NUMBER,
      v_cur_code       NUMBER,
      v_lds_sys_code   NUMBER,
      v_lds_div_code   NUMBER,
      v_lds_occupation IN VARCHAR2,
      v_lds_comp_name IN VARCHAR2 DEFAULT NULL
   );
     PROCEDURE delete_lead(v_lds_code NUMBER);
     PROCEDURE save_lead_comment(v_action VARCHAR2,
                                v_lds_code  NUMBER,
                                v_lcmnt_code NUMBER,
                                v_comment   VARCHAR2,
                                v_date      DATE,
                                v_usr_code  NUMBER
                                );
   PROCEDURE save_lead_activities(v_action VARCHAR2,
                                   v_lacts_code NUMBER,
                                   v_act_code  NUMBER,
                                   v_lds_code NUMBER
                                  );
  PROCEDURE save_lead_sources(v_action VARCHAR2,
                              v_code NUMBER,
                              v_desc VARCHAR2);
  PROCEDURE save_lead_statuses(v_action VARCHAR2,
                               v_code NUMBER,
                               v_desc VARCHAR2);
    
  PROCEDURE report_groups_prc( 
                v_action        IN  VARCHAR2 DEFAULT NULL,
                v_trg_code        TQC_REPORT_GROUPS.TRG_CODE%TYPE DEFAULT NULL,        
                v_trg_name        TQC_REPORT_GROUPS.TRG_NAME%TYPE DEFAULT NULL
                ); 
                
  PROCEDURE report_div_groups_prc( 
                v_action        IN  VARCHAR2 DEFAULT NULL,
                v_rdg_code        TQC_REPORT_DIV_GROUPS .RDG_CODE%TYPE DEFAULT NULL,        
                v_rdg_trg_code    TQC_REPORT_DIV_GROUPS .RDG_CODE%TYPE DEFAULT NULL,
                v_rdg_div_code    TQC_REPORT_DIV_GROUPS  .RDG_DIV_CODE%TYPE DEFAULT NULL
                
                );  
                
   PROCEDURE system_app_areas_prc( 
      v_add_edit        IN  VARCHAR2,
      v_SAA_CODE        IN TQC_SYS_APPLICABLE_AREAS.SAA_CODE%TYPE DEFAULT NULL, 
      v_SAA_SYS_CODE    IN TQC_SYS_APPLICABLE_AREAS.SAA_SYS_CODE%TYPE DEFAULT NULL, 
      v_SAA_DESCRIPTION    IN TQC_SYS_APPLICABLE_AREAS.SAA_DESCRIPTION%TYPE DEFAULT NULL
      );
      
  PROCEDURE tqc_sys_mod_subunits_prc (
      v_add_edit        IN   VARCHAR2,
      v_tsms_code       IN   NUMBER,
      v_tsms_tsm_code   IN   NUMBER,
      v_tsms_sht_desc   IN   VARCHAR2,
      v_tsms_desc       IN   VARCHAR2,
      v_tsms_order      IN   NUMBER,
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
      v_mail_type  VARCHAR2, 
      v_mail_secure VARCHAR2,
      v_server     VARCHAR2,
      v_host       VARCHAR2,
      v_port       NUMBER,
      v_username   VARCHAR2,
      v_password   VARCHAR2,
      v_email      VARCHAR2
   );
                              
   PROCEDURE incomingMailProc(
    v_mail_secure VARCHAR2,
    v_server      VARCHAR2,
                              v_host        VARCHAR2,
                              v_port        NUMBER,
                              v_username    VARCHAR2,
                              v_password    VARCHAR2,
                              v_type       VARCHAR2);
   PROCEDURE create_alerts (v_job_name          IN VARCHAR2,
                            v_job_desc          IN VARCHAR2,
                            v_start_time        IN DATE,
                            v_end_time          IN DATE,
                            v_code              IN NUMBER,
                            v_sys_code          IN NUMBER,
                            v_recurrence_type   IN VARCHAR2,
                            v_assignee          IN NUMBER,
                            v_notified_fail     IN NUMBER,
                            v_notified_succ     IN NUMBER,
                            v_job_type          IN VARCHAR2,
                            v_job_template      IN VARCHAR2,
                            v_fail_template     IN VARCHAR2,
                            v_succ_template     IN VARCHAR2,
                            v_add_edit          IN VARCHAR2,
                            v_cron_expr         IN VARCHAR2,
                            v_status            IN VARCHAR2,
                            v_repeat            IN VARCHAR2,
                            v_threshType        IN VARCHAR2,
                            v_threshVal         IN NUMBER);  
 PROCEDURE households_prc(v_add_edit    VARCHAR2,  
                          v_code        TQC_HOUSEHOLDS.HH_ID%TYPE, 
                          v_name        TQC_HOUSEHOLDS.HH_NAME%TYPE, 
                          v_category    TQC_HOUSEHOLDS.HH_CATEGORY%TYPE,
                          v_usrCode     NUMBER);  
                          
 PROCEDURE houseHoldMembers_prc(v_add_edit varchar2,  
                                v_code          TQC_HOUSEHOLDS.HH_ID%TYPE,
                                v_clnt_code     TQC_CLIENTS.CLNT_CODE%TYPE);
 PROCEDURE agencies_editAccountType( 
   v_AGN_CODE               IN  TQC_AGENCIES.AGN_CODE%TYPE,
   v_AGN_ACT_CODE           IN  TQC_AGENCIES.AGN_ACT_CODE%TYPE);
  
 PROCEDURE ecm_setups_prc(v_est_code         TQC_ECM_SETUPS.est_code%type , 
                          v_est_ecm_url      TQC_ECM_SETUPS.est_ecm_url%type, 
                          v_est_ecm_username TQC_ECM_SETUPS.est_ecm_username%type, 
                          v_est_ecm_password TQC_ECM_SETUPS.est_ecm_password%type, 
                          v_est_sys_code     TQC_ECM_SETUPS.est_sys_code%type, 
                          v_est_root_folder  TQC_ECM_SETUPS.est_root_folder%type,
                          v_add_edit         VARCHAR2);
                         
 
 
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
      v_content_type      VARCHAR2,
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
        
  PROCEDURE bank_branch_details (v_add_edit VARCHAR2,
        v_bbb_code number,
       v_bbb_brn_code NUMBER, 
       v_bbb_brn_reg_code VARCHAR2,
       v_bbb_brn_name VARCHAR2, 
       v_bbb_bbr_code NUMBER,
        v_bbb_bbr_bnk_code NUMBER
   );
     PROCEDURE smsStatus (v_status varchar2,v_sms_code number DEFAULT NULL,v_sms_sent_response IN VARCHAR2 DEFAULT NULL
   );
   
   
    PROCEDURE escalate_proc(v_taskId                NUMBER,
                        v_sysCode               NUMBER,                        
                        v_processDefinitionId   VARCHAR2,
                        v_taskName              VARCHAR2,
                        v_duration          OUT NUMBER,
                        v_assignee          OUT VARCHAR2
                        );
 PROCEDURE createBankBranches(v_addEdit       VARCHAR2,
                           v_tcb_code IN NUMBER, 
                           v_tcb_clnt_code IN NUMBER,
                           v_tcb_sht_desc IN  VARCHAR2,
                           v_tcb_name IN VARCHAR) ;
                           
PROCEDURE assignBankToClient(v_addEdit IN VARCHAR2,
                             v_tcb_acwa_code IN NUMBER,
                             v_tcb_code IN NUMBER);
                     PROCEDURE assignDefaultBranch(v_tcub_tcb_code IN NUMBER) ;
                     
                  PROCEDURE createWebProductDetails(v_addEdit       VARCHAR2,
                          v_twpd_clnt_code IN NUMBER, 
                          v_twpd_twp_code IN NUMBER, 
                          v_twpd_usr_code IN NUMBER, 
                          v_twpd_username IN VARCHAR2, 
                          v_twpd_dr_limit IN NUMBER,
                          v_twpd_cr_limit IN NUMBER, 
                          v_twpd_policy_use IN VARCHAR2, 
                          v_twpd_endos_use IN VARCHAR2);
                          PROCEDURE outgoingSmsProc(v_addEdit     VARCHAR2,
                          v_tssCode             TQC_SYSTEM_SMS.TSS_CODE%TYPE,                        
                          v_tssDesc             TQC_SYSTEM_SMS.TSS_DESC%TYPE,
                          v_tssUrls             TQC_SYSTEM_SMS.TSS_URL%TYPE,
                          v_username            TQC_SYSTEM_SMS.TSS_USERNAME%TYPE,
                          v_password            TQC_SYSTEM_SMS.TSS_PASSWORD%TYPE,
                          v_source              TQC_SYSTEM_SMS.TSS_SOURCE%TYPE,
                          v_tssDefault          TQC_SYSTEM_SMS.TSS_DEFAULT%TYPE);
                       PROCEDURE getRequiredDocs (
      v_add_edit      IN VARCHAR2,
      v_rqd_code IN NUMBER,
      v_rqd_spt_code IN NUMBER,
      v_rqd_spta_code IN NUMBER, 
      v_rqd_sys_code IN NUMBER,
      v_rqc_rdoc_id IN NUMBER
      
   );
    PROCEDURE getCountryPref(
   v_cou_name IN VARCHAR2,
   v_cou_mobile_prefix OUT NUMBER, 
   v_cou_client_number OUT NUMBER
   );
   
   PROCEDURE escalations_proc(v_addEdit    VARCHAR2,
                        v_code      TQC_SYS_ESCALATION_LEVELS.TSEL_CODE%TYPE, 
                        v_sysCode   TQC_SYS_ESCALATION_LEVELS.TSEL_JSD_SYS_CODE%TYPE,
                        v_jpdlName  TQC_SYS_ESCALATION_LEVELS.TSEL_JSD_JPDL_NAME%TYPE,
                        v_activity  TQC_SYS_ESCALATION_LEVELS.TSEL_ACTIVITY_NAME%TYPE,
                        v_level     TQC_SYS_ESCALATION_LEVELS.TSEL_LEVEL%TYPE, 
                        v_user      TQC_SYS_ESCALATION_LEVELS.TSEL_ASSIGNEE%TYPE,
                        v_duration  TQC_SYS_ESCALATION_LEVELS.TSEL_DURATION%TYPE,
                        v_cc        TQC_SYS_ESCALATION_LEVELS.TSEL_CC%TYPE); 
                        
  PROCEDURE reservedWords_proc(v_addEdit  VARCHAR2,
                            v_code              TQC_SYS_RESERVED_WORDS.TSRW_CODE%TYPE, 
                            v_sysCode           TQC_SYS_RESERVED_WORDS.TSRW_SYS_CODE%TYPE, 
                            v_tsrcCode          TQC_SYS_RESERVED_WORDS.TSRW_TSRC_CODE%TYPE, 
                            v_type              TQC_SYS_RESERVED_WORDS.TSRW_TYPE%TYPE,
                            v_name              TQC_SYS_RESERVED_WORDS.TSRW_NAME%TYPE,
                            v_desc              TQC_SYS_RESERVED_WORDS.TSRW_DESC%TYPE);
                         
  PROCEDURE AddUpdateCountry(v_addEdit  VARCHAR2,
                              v_tcou_code IN NUMBER, 
                              v_cou_sht_desc IN VARCHAR2,
                               v_cou_name IN VARCHAR2
                           
                            ) ;
 PROCEDURE AuthorizeAccount(v_user IN VARCHAR2,v_agn_code IN NUMBER);
 PROCEDURE addReportSubModule(v_addEdit  VARCHAR2,
                            v_RSM_CODE IN NUMBER, 
                            v_RSM_NAME  IN VARCHAR2, 
                            v_RSM_DESC   IN VARCHAR2, 
                            v_RSM_SRM_CODE    IN NUMBER);
 PROCEDURE AssignReport(v_rpt_code IN NUMBER,v_rpt_rsm_code IN NUMBER);
PROCEDURE UnAssignReport(v_rpt_code IN NUMBER,v_rpt_rsm_code IN NUMBER);
PROCEDURE UpdateReportDetails(v_rpt_code IN NUMBER,v_rpt_description IN VARCHAR2);
PROCEDURE occupations_prc (
      v_add_edit       IN   VARCHAR2,
      v_occ_code       IN   tqc_occupations.occ_code%TYPE,
      v_occ_sht_desc   IN   tqc_occupations.occ_sht_desc%TYPE,
      v_occ_name       IN   tqc_occupations.occ_name%TYPE,
      v_occ_sec_code   IN   NUMBER
   );
   PROCEDURE updateentities (
   v_add_edit               IN   VARCHAR2,
   v_ent_code               IN OUT   NUMBER,
   v_ent_sht_desc           IN   VARCHAR2,
   v_ent_name               IN   VARCHAR2,
   v_ent_physical_address   IN   VARCHAR2,
   v_ent_postal_address     IN   VARCHAR2,
   v_ent_twn_code           IN   NUMBER,
   v_ent_cou_code           IN   NUMBER,
   v_ent_email_address      IN   VARCHAR2,
   v_ent_fax                IN   VARCHAR2,
   v_ent_status             IN   VARCHAR2,
   v_ent_bbr_code           IN   NUMBER,
   v_ent_bank_acc_no        IN   VARCHAR2,
   v_ent_zip                IN   VARCHAR2,
   v_ent_sms_tel            IN   VARCHAR2,
   v_ent_created_by         IN   VARCHAR2,
   v_ent_date_created       IN   DATE,
   v_ent_pin                IN   VARCHAR2,
   v_ent_tel1               IN   VARCHAR2,
   v_ent_acc_no             IN   VARCHAR2,
   v_ent_status_remarks     IN   VARCHAR2,
   v_ent_brn_code           IN   NUMBER,
   v_ent_id_no              IN   VARCHAR2,
   v_ent_tel2               IN   VARCHAR2,
   v_ent_sec_code           IN   NUMBER,
   v_ent_runoff             IN   VARCHAR2,
   v_ent_old_acc_no         IN   VARCHAR2,
   v_ent_title              IN   VARCHAR2,
   v_ent_wef                IN   DATE,
   v_ent_wet                IN   DATE,
   v_ent_contact_person     IN   VARCHAR2,
   v_source IN VARCHAR2
);


PROCEDURE save_client_details(v_type VARCHAR2,v_clnt_code NUMBER,v_pin_pass VARCHAR2,v_pass VARCHAR2,v_idno VARCHAR2);

 PROCEDURE CREATE_EMAIL_MSG(v_clnt_code IN NUMBER,
                        v_agn_code IN NUMBER,
                        v_quot_code IN NUMBER,
                        v_pol_code IN NUMBER,
                        v_pol_no IN VARCHAR2,
                        v_clm_no IN VARCHAR2,
                        v_email_addr IN VARCHAR2,
                        v_msg_subj IN VARCHAR2,
                        v_msg_text IN VARCHAR2,
                        v_sys_code IN NUMBER,
                        v_sys_mod    IN VARCHAR2,
                        V_EMAIL_STATUS IN VARCHAR2);
                        
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
   v_bpn_clnt_code    IN   NUMBER,
   v_bpn_payee_type IN VARCHAR2
);

 PROCEDURE prospects_prc (
      v_addedit              VARCHAR2,
      v_prospects_tab   IN   tqc_prospects_tab,
      v_client_type     IN   VARCHAR2,
      v_prs_code         OUT  NUMBER
   );
END tqc_setups_pkg030215; 
 
/