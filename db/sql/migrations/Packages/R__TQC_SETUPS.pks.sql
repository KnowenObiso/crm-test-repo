--
-- TQC_SETUPS  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."TQC_SETUPS" AS

PROCEDURE agency_holding_company_prc( 
    v_add_edit  IN  VARCHAR2,
    v_AHC_CODE  IN  TQC_AGENCY_HOLDING_COMPANY.AHC_CODE%TYPE,
    v_AHC_NAME  IN  TQC_AGENCY_HOLDING_COMPANY.AHC_NAME%TYPE,
    v_error     OUT VARCHAR2);
    
PROCEDURE banks_prc( 
    v_add_edit              IN  VARCHAR2,
    v_BNK_CODE              IN  TQC_BANKS.BNK_CODE%TYPE,
    v_BNK_BANK_NAME         IN  TQC_BANKS.BNK_BANK_NAME%TYPE,
    v_BNK_REMARKS           IN  TQC_BANKS.BNK_REMARKS%TYPE,
    v_BNK_SHT_DESC          IN  TQC_BANKS.BNK_SHT_DESC%TYPE,
    v_BNK_DDR_CODE          IN  TQC_BANKS.BNK_DDR_CODE%TYPE,
    v_DD_FORMAT_DESC        IN  TQC_BANKS.DD_FORMAT_DESC%TYPE,
    v_BNK_FORWARDING_BNK_CODE   IN  TQC_BANKS.BNK_FORWARDING_BNK_CODE%TYPE,
    v_BNK_KBA_CODE          IN  TQC_BANKS.BNK_KBA_CODE%TYPE,
    v_error     OUT VARCHAR2);
    
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
    
PROCEDURE currencies_prc( 
    v_add_edit      IN  VARCHAR2,
    v_CUR_CODE      IN  TQC_CURRENCIES.CUR_CODE%TYPE,
    v_CUR_SYMBOL    IN  TQC_CURRENCIES.CUR_SYMBOL%TYPE,
    v_CUR_DESC      IN  TQC_CURRENCIES.CUR_DESC%TYPE,
    v_CUR_RND       IN  TQC_CURRENCIES.CUR_RND%TYPE,
    v_error         OUT VARCHAR2);

PROCEDURE currencies_denominations_prc( 
    v_add_edit      IN  VARCHAR2,
    v_CUD_CODE      IN  TQC_CURRENCY_DENOMINATIONS.CUD_CODE%TYPE,
    v_CUD_CUR_CODE  IN  TQC_CURRENCY_DENOMINATIONS.CUD_CUR_CODE%TYPE,
    v_CUD_VALUE     IN  TQC_CURRENCY_DENOMINATIONS.CUD_VALUE%TYPE,
    v_CUD_NAME      IN  TQC_CURRENCY_DENOMINATIONS.CUD_NAME%TYPE,
    v_CUD_WEF       IN  TQC_CURRENCY_DENOMINATIONS.CUD_WEF%TYPE,
    v_error         OUT VARCHAR2);
    
PROCEDURE currency_rates_prc( 
    v_add_edit          IN  VARCHAR2,
    v_CRT_CODE          IN  TQC_CURRENCY_RATES.CRT_CODE%TYPE,
    v_CRT_CUR_CODE      IN  TQC_CURRENCY_RATES.CRT_CUR_CODE%TYPE,
    v_CRT_RATE          IN  TQC_CURRENCY_RATES.CRT_RATE%TYPE,
    v_CRT_DATE          IN  TQC_CURRENCY_RATES.CRT_DATE%TYPE,
    v_CRT_BASE_CUR_CODE IN  TQC_CURRENCY_RATES.CRT_BASE_CUR_CODE%TYPE);
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
    
PROCEDURE countries_prc( 
     v_add_edit         IN  VARCHAR2,
      v_COU_CODE         IN TQC_COUNTRIES.COU_CODE%TYPE DEFAULT NULL,
      v_COU_SHT_DESC     IN TQC_COUNTRIES.COU_SHT_DESC%TYPE DEFAULT NULL,
      v_COU_NAME         IN TQC_COUNTRIES.COU_NAME%TYPE DEFAULT NULL,
      v_COU_BASE_CURR    IN TQC_COUNTRIES.COU_BASE_CURR%TYPE DEFAULT NULL,
      v_COU_NATIONALITY  IN TQC_COUNTRIES.COU_NATIONALITY%TYPE DEFAULT NULL,
      v_COU_ZIP_CODE     IN TQC_COUNTRIES.COU_ZIP_CODE%TYPE  DEFAULT NULL,
      v_cou_admin_reg_type IN TQC_COUNTRIES.COU_ADMIN_REG_TYPE%TYPE DEFAULT NULL,
      v_cou_schegen        IN  TQC_COUNTRIES.COU_SCHEGEN%TYPE DEFAULT NULL,
      v_cou_emb_code        IN  TQC_COUNTRIES.COU_EMB_CODE%TYPE DEFAULT NULL,
      v_cou_curr_serial        IN  TQC_COUNTRIES.COU_CURR_SERIAL%TYPE DEFAULT NULL
      
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
    
PROCEDURE service_providers_prc( 
      v_add_edit                IN  VARCHAR2,
      v_SPR_CODE                IN  TQC_SERVICE_PROVIDERS.SPR_CODE%TYPE,
      v_SPR_SHT_DESC            IN  TQC_SERVICE_PROVIDERS.SPR_SHT_DESC%TYPE,
      v_SPR_NAME                IN  TQC_SERVICE_PROVIDERS.SPR_NAME%TYPE,
      v_SPR_PHYSICAL_ADDRESS    IN  TQC_SERVICE_PROVIDERS.SPR_PHYSICAL_ADDRESS%TYPE,
      v_SPR_POSTAL_ADDRESS      IN  TQC_SERVICE_PROVIDERS.SPR_POSTAL_ADDRESS%TYPE,
      v_SPR_TWN_CODE            IN  TQC_SERVICE_PROVIDERS.SPR_TWN_CODE%TYPE,
      v_SPR_COU_CODE            IN  TQC_SERVICE_PROVIDERS.SPR_COU_CODE%TYPE,
      v_SPR_SPT_CODE            IN  TQC_SERVICE_PROVIDERS.SPR_SPT_CODE%TYPE,
      v_SPR_PHONE               IN  TQC_SERVICE_PROVIDERS.SPR_PHONE%TYPE,
      v_SPR_FAX                 IN  TQC_SERVICE_PROVIDERS.SPR_FAX%TYPE,
      v_SPR_EMAIL               IN  TQC_SERVICE_PROVIDERS.SPR_EMAIL%TYPE,
      v_SPR_TITLE               IN  TQC_SERVICE_PROVIDERS.SPR_TITLE%TYPE,
      v_SPR_ZIP                 IN  TQC_SERVICE_PROVIDERS.SPR_ZIP%TYPE,
      v_SPR_WEF                 IN  TQC_SERVICE_PROVIDERS.SPR_WEF%TYPE,
      v_SPR_WET                 IN  TQC_SERVICE_PROVIDERS.SPR_WET%TYPE,
      v_SPR_CONTACT             IN  TQC_SERVICE_PROVIDERS.SPR_CONTACT%TYPE,
      v_SPR_AIMS_CODE           IN  TQC_SERVICE_PROVIDERS.SPR_AIMS_CODE%TYPE,
      v_SPR_BBR_CODE            IN  TQC_SERVICE_PROVIDERS.SPR_BBR_CODE%TYPE,
      v_SPR_BANK_ACC_NO         IN  TQC_SERVICE_PROVIDERS.SPR_BANK_ACC_NO%TYPE,
      v_SPR_CREATED_BY          IN  TQC_SERVICE_PROVIDERS.SPR_CREATED_BY%TYPE,
      v_SPR_DATE_CREATED        IN  TQC_SERVICE_PROVIDERS.SPR_DATE_CREATED%TYPE,
      v_SPR_STATUS_REMARKS      IN  TQC_SERVICE_PROVIDERS.SPR_STATUS_REMARKS%TYPE,
      v_SPR_STATUS              IN  TQC_SERVICE_PROVIDERS.SPR_STATUS%TYPE,
      v_SPR_PIN_NUMBER          IN  TQC_SERVICE_PROVIDERS.SPR_PIN_NUMBER%TYPE,
      v_SPR_TRS_OCCUPATION      IN  TQC_SERVICE_PROVIDERS.SPR_TRS_OCCUPATION%TYPE,
      v_SPR_PROFF_BODY          IN  TQC_SERVICE_PROVIDERS.SPR_PROFF_BODY%TYPE,
      v_SPR_PIN                 IN  TQC_SERVICE_PROVIDERS.SPR_PIN%TYPE,
      v_SPR_DOC_PHONE           IN  TQC_SERVICE_PROVIDERS.SPR_DOC_PHONE%TYPE,
      v_SPR_DOC_EMAIL           IN  TQC_SERVICE_PROVIDERS.SPR_DOC_EMAIL%TYPE,
       v_SPR_GL_ACC_NO           IN  TQC_SERVICE_PROVIDERS.SPR_GL_ACC_NO%TYPE
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
    
PROCEDURE agencies_prc( 
      v_add_edit                IN  VARCHAR2,
      v_AGN_CODE                IN  TQC_AGENCIES.AGN_CODE%TYPE,
      v_AGN_ACT_CODE            IN  TQC_AGENCIES.AGN_ACT_CODE%TYPE,
      v_AGN_SHT_DESC           IN  TQC_AGENCIES.AGN_SHT_DESC%TYPE,
      v_AGN_NAME               IN  TQC_AGENCIES.AGN_NAME%TYPE,
      v_AGN_PHYSICAL_ADDRESS   IN  TQC_AGENCIES.AGN_PHYSICAL_ADDRESS%TYPE,
      v_AGN_POSTAL_ADDRESS     IN  TQC_AGENCIES.AGN_POSTAL_ADDRESS%TYPE,
      v_AGN_TWN_CODE           IN  TQC_AGENCIES.AGN_TWN_CODE%TYPE,
      v_AGN_COU_CODE           IN  TQC_AGENCIES.AGN_COU_CODE%TYPE,
      v_AGN_EMAIL_ADDRESS      IN  TQC_AGENCIES.AGN_EMAIL_ADDRESS%TYPE,
      v_AGN_WEB_ADDRESS        IN  TQC_AGENCIES.AGN_WEB_ADDRESS%TYPE,
      v_AGN_ZIP                IN  TQC_AGENCIES.AGN_ZIP%TYPE,
      v_AGN_CONTACT_PERSON     IN  TQC_AGENCIES.AGN_CONTACT_PERSON%TYPE,
      v_AGN_CONTACT_TITLE      IN  TQC_AGENCIES.AGN_CONTACT_TITLE%TYPE,
      v_AGN_TEL1               IN  TQC_AGENCIES.AGN_TEL1%TYPE,
      v_AGN_TEL2               IN  TQC_AGENCIES.AGN_TEL2%TYPE,
      v_AGN_FAX                IN  TQC_AGENCIES.AGN_FAX%TYPE,
      v_AGN_ACC_NO             IN  TQC_AGENCIES.AGN_ACC_NO%TYPE,
      v_AGN_PIN                IN  TQC_AGENCIES.AGN_PIN%TYPE,
      v_AGN_AGENT_COMMISSION   IN  TQC_AGENCIES.AGN_AGENT_COMMISSION%TYPE,
      v_AGN_CREDIT_ALLOWED     IN  TQC_AGENCIES.AGN_CREDIT_ALLOWED%TYPE,
      v_AGN_AGENT_WHT_TAX      IN  TQC_AGENCIES.AGN_AGENT_WHT_TAX%TYPE,
      v_AGN_PRINT_DBNOTE       IN  TQC_AGENCIES.AGN_PRINT_DBNOTE%TYPE,
      v_AGN_STATUS             IN  TQC_AGENCIES.AGN_STATUS%TYPE,
      v_AGN_DATE_CREATED       IN  TQC_AGENCIES.AGN_DATE_CREATED%TYPE,
      v_AGN_CREATED_BY         IN  TQC_AGENCIES.AGN_CREATED_BY%TYPE,
      v_AGN_REG_CODE           IN  TQC_AGENCIES.AGN_REG_CODE%TYPE,
      v_AGN_COMM_RESERVE_RATE  IN  TQC_AGENCIES.AGN_COMM_RESERVE_RATE%TYPE,
      v_AGN_ANNUAL_BUDGET      IN  TQC_AGENCIES.AGN_ANNUAL_BUDGET%TYPE,
      v_AGN_STATUS_EFF_DATE    IN  TQC_AGENCIES.AGN_STATUS_EFF_DATE%TYPE,
      v_AGN_CREDIT_PERIOD      IN  TQC_AGENCIES.AGN_CREDIT_PERIOD%TYPE,
      v_AGN_COMM_STAT_EFF_DT   IN  TQC_AGENCIES.AGN_COMM_STAT_EFF_DT%TYPE,
      v_AGN_COMM_STATUS_DT     IN  TQC_AGENCIES.AGN_COMM_STATUS_DT%TYPE,
      v_AGN_COMM_ALLOWED       IN  TQC_AGENCIES.AGN_COMM_ALLOWED%TYPE,
      v_AGN_CHECKED            IN  TQC_AGENCIES.AGN_CHECKED%TYPE,
      v_AGN_CHECKED_BY         IN  TQC_AGENCIES.AGN_CHECKED_BY%TYPE,
      v_AGN_CHECK_DATE         IN  TQC_AGENCIES.AGN_CHECK_DATE%TYPE,
      v_AGN_COMP_COMM_ARREARS  IN  TQC_AGENCIES.AGN_COMP_COMM_ARREARS%TYPE,
      v_AGN_REINSURER          IN  TQC_AGENCIES.AGN_REINSURER%TYPE,
      v_AGN_BRN_CODE           IN  TQC_AGENCIES.AGN_BRN_CODE%TYPE,
      v_AGN_TOWN               IN  TQC_AGENCIES.AGN_TOWN%TYPE,
      v_AGN_COUNTRY            IN  TQC_AGENCIES.AGN_COUNTRY%TYPE,
      v_AGN_STATUS_DESC        IN  TQC_AGENCIES.AGN_STATUS_DESC%TYPE,
      v_AGN_ID_NO              IN  TQC_AGENCIES.AGN_ID_NO%TYPE,
      v_AGN_CON_CODE           IN  TQC_AGENCIES.AGN_CON_CODE%TYPE,
      v_AGN_AGN_CODE           IN  TQC_AGENCIES.AGN_AGN_CODE%TYPE,
      v_AGN_SMS_TEL            IN  TQC_AGENCIES.AGN_SMS_TEL%TYPE,
      v_AGN_AHC_CODE           IN  TQC_AGENCIES.AGN_AHC_CODE%TYPE,
      v_AGN_SEC_CODE           IN  TQC_AGENCIES.AGN_SEC_CODE%TYPE,
      v_AGN_AGNC_CLASS_CODE    IN  TQC_AGENCIES.AGN_AGNC_CLASS_CODE%TYPE,
      v_AGN_EXPIRY_DATE        IN  TQC_AGENCIES.AGN_EXPIRY_DATE%TYPE,
      v_AGN_LICENSE_NO         IN  TQC_AGENCIES.AGN_LICENSE_NO%TYPE,
      v_AGN_RUNOFF             IN  TQC_AGENCIES.AGN_RUNOFF%TYPE,
      v_AGN_LICENSED           IN  TQC_AGENCIES.AGN_LICENSED%TYPE,
      v_AGN_LICENSE_GRACE_PR   IN  TQC_AGENCIES.AGN_LICENSE_GRACE_PR%TYPE,
      v_AGN_OLD_ACC_NO         IN  TQC_AGENCIES.AGN_OLD_ACC_NO%TYPE,
      v_AGN_STATUS_REMARKS     IN  TQC_AGENCIES.AGN_STATUS_REMARKS%TYPE,
      v_AGN_BBR_CODE           IN  TQC_AGENCIES.AGN_BBR_CODE%TYPE DEFAULT NULL,
      v_AGN_BANK_ACC_NO        IN  TQC_AGENCIES.AGN_BANK_ACC_NO%TYPE DEFAULT NULL,
      v_AGN_UNIQUE_PREFIX      IN  TQC_AGENCIES.AGN_UNIQUE_PREFIX%TYPE DEFAULT NULL,
      v_AGN_STATE_CODE      IN  TQC_AGENCIES.AGN_STATE_CODE%TYPE DEFAULT NULL
      
      
    );
    
    
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
        
PROCEDURE prospects_prc(
v_addEdit       VARCHAR2,
 v_prospects_tab IN TQC_PROSPECTS_TAB);

PROCEDURE holidays_prc(v_addEdit       VARCHAR2, v_holidays_tab IN TQC_HOLIDAYS_TAB);

PROCEDURE searchClients(v_username VARCHAR2, v_search_date DATE);

PROCEDURE generateDirectDebitRecs(v_username VARCHAR2, v_searched VARCHAR2, v_dd_bbr_code NUMBER, v_dd_company_acc VARCHAR2, v_search_date DATE);

PROCEDURE deleteDirectDebit(v_username VARCHAR2, v_dd_code NUMBER, v_dd_status VARCHAR2);

PROCEDURE authoriseDirectDebit(v_username VARCHAR2, v_dd_code NUMBER, v_dd_status VARCHAR2);

PROCEDURE deleteDirectDebitDetail(v_username VARCHAR2, v_ddd_code NUMBER, v_dd_status VARCHAR2);

PROCEDURE generateReceipts(v_username VARCHAR2, v_dd_code NUMBER, v_dd_receipted VARCHAR2, v_dd_book_date DATE);

PROCEDURE failUpdateDDHeader(v_username VARCHAR2, v_ddh_code NUMBER, v_fail_date DATE, v_fail_remarks VARCHAR2, v_ddh_dd_code NUMBER);
 PROCEDURE failUpdateDDHeader2(v_username VARCHAR2, v_ddh_code NUMBER, v_book_date DATE, v_fail_remarks VARCHAR2, v_ddh_dd_code IN OUT NUMBER,v_status VARCHAR DEFAULT 'F');
PROCEDURE relaunchDDHeader(v_username VARCHAR2, v_ddh_code NUMBER, v_ddh_status VARCHAR2, v_ddh_dd_code NUMBER);

PROCEDURE stopLaunchDDHeader(v_username VARCHAR2, v_ddh_code NUMBER, v_ddh_status VARCHAR2, v_ddh_dd_code NUMBER);

PROCEDURE service_provider_type_act_prc(
    v_add_edit     IN VARCHAR2,
    v_SPTA_CODE      IN TQC_SERV_PRV_TYPE_ACTVTS.SPTA_CODE%TYPE, 
    v_SPTA_SPT_CODE  IN TQC_SERV_PRV_TYPE_ACTVTS.SPTA_SPT_CODE%TYPE, 
    v_SPTA_SHT_DESC  IN TQC_SERV_PRV_TYPE_ACTVTS.SPTA_SHT_DESC%TYPE, 
    v_SPTA_DESC      IN TQC_SERV_PRV_TYPE_ACTVTS.SPTA_DESC%TYPE
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
    PROCEDURE save_lead_details(v_lds_code NUMBER default null,
                                v_desc     VARCHAR2 default null,
                                v_cmp_code number default null,
                                v_coTel    VARCHAR2 default null,
                                v_title    VARCHAR2 default null,
                                v_surname  VARCHAR2 default null,
                                v_oth_name VARCHAR2 default null,
                                v_mob_no   VARCHAR2 default null,
                                v_fax      VARCHAR2 default null,
                                v_email    VARCHAR2 default null,
                                v_post_addr VARCHAR2 default null,
                                v_post_code VARCHAR2 default null,
                                v_phy_addr  VARCHAR2 default null,
                                v_ldsrcCode NUMBER default null,
                                v_lstsCode  NUMBER default null,
                                v_lead_date DATE default null,
                                v_converted VARCHAR2 default null,
                                v_port_name  VARCHAR2 default null,
                                v_port_contr VARCHAR2 default null,
                                v_port_amt   NUMBER default null,
                                v_port_sale  VARCHAR2 default null,
                                v_port_clse  DATE default null,
                                v_ann_rev    NUMBER default null,
                                v_website    VARCHAR2 default null,
                                v_industry   VARCHAR2 default null,
                                                              
                                v_cou_code  NUMBER default null,
                                v_state_code  Number default null,
                                v_town_code NUMBER default null,
                                v_org_code NUMBER default null,
                                v_user_code NUMBER default null,
                                v_team_code NUMBER default null,
                                v_acc_code NUMBER default null,
                                v_prod_code number default null,
                                v_cur_code number default null,
                                v_lds_sys_code number default null,
                                v_lds_div_code number default null
                                
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
      
                 
    
END TQC_SETUPS; 
/