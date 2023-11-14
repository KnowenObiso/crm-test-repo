--
-- TQC_AGENCIES_CURSORS  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_agencies_cursors
AS
/******************************************************************************
   NAME:       TQC_AGENCIES_CURSORS
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        3/18/2010             1. Created this package.
******************************************************************************/
   TYPE tqc_agns_by_acttype_rec IS RECORD (
      agn_code            tqc_agencies.agn_code%TYPE,
      agn_act_code        tqc_agencies.agn_act_code%TYPE,
      agn_sht_desc        tqc_agencies.agn_sht_desc%TYPE,
      agn_name            tqc_agencies.agn_name%TYPE,
      agn_comm_allowed    tqc_agencies.agn_comm_allowed%TYPE,
      agn_main_agn_code   tqc_agencies.agn_main_agn_code%TYPE
   );

   TYPE tqc_agns_by_acttype_ref IS REF CURSOR
      RETURN tqc_agns_by_acttype_rec;

   PROCEDURE get_agns_by_acttype (
      v_act_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agns_by_acttype_ref
   );

   FUNCTION getallagencies (
      v_agn_name       IN   VARCHAR2 DEFAULT '',
      v_agn_sht_desc   IN   VARCHAR2 DEFAULT ''
   )
      RETURN tqc_agns_by_acttype_ref;

   TYPE tqc_agencies_rec IS RECORD (
      agn_code                tqc_agencies.agn_code%TYPE,
      agn_act_code            tqc_agencies.agn_act_code%TYPE,
      agn_sht_desc            tqc_agencies.agn_sht_desc%TYPE,
      agn_name                tqc_agencies.agn_name%TYPE,
      agn_physical_address    tqc_agencies.agn_physical_address%TYPE,
      agn_postal_address      tqc_agencies.agn_postal_address%TYPE,
      agn_twn_code            tqc_agencies.agn_twn_code%TYPE,
      twn_name                tqc_towns.twn_name%TYPE,
      agn_cou_code            tqc_agencies.agn_cou_code%TYPE,
      cou_name                tqc_countries.cou_name%TYPE,
      agn_email_address       tqc_agencies.agn_email_address%TYPE,
      agn_web_address         tqc_agencies.agn_web_address%TYPE,
      agn_zip                 tqc_agencies.agn_zip%TYPE,
      agn_contact_person      tqc_agencies.agn_contact_person%TYPE,
      agn_contact_title       tqc_agencies.agn_contact_title%TYPE,
      agn_tel1                tqc_agencies.agn_tel1%TYPE,
      agn_tel2                tqc_agencies.agn_tel2%TYPE,
      agn_fax                 tqc_agencies.agn_fax%TYPE,
      agn_acc_no              tqc_agencies.agn_acc_no%TYPE,
      agn_pin                 tqc_agencies.agn_pin%TYPE,
      agn_agent_commission    tqc_agencies.agn_agent_commission%TYPE,
      agn_credit_allowed      tqc_agencies.agn_credit_allowed%TYPE,
      agn_agent_wht_tax       tqc_agencies.agn_agent_wht_tax%TYPE,
      agn_print_dbnote        tqc_agencies.agn_print_dbnote%TYPE,
      agn_status              tqc_agencies.agn_status%TYPE,
      agn_date_created        tqc_agencies.agn_date_created%TYPE,
      agn_created_by          tqc_agencies.agn_created_by%TYPE,
      agn_reg_code            tqc_agencies.agn_reg_code%TYPE,
      agn_comm_reserve_rate   tqc_agencies.agn_comm_reserve_rate%TYPE,
      agn_annual_budget       tqc_agencies.agn_annual_budget%TYPE,
      agn_status_eff_date     tqc_agencies.agn_status_eff_date%TYPE,
      agn_credit_period       tqc_agencies.agn_credit_period%TYPE,
      agn_comm_stat_eff_dt    tqc_agencies.agn_comm_stat_eff_dt%TYPE,
      agn_comm_status_dt      tqc_agencies.agn_comm_status_dt%TYPE,
      agn_comm_allowed        tqc_agencies.agn_comm_allowed%TYPE,
      agn_checked             tqc_agencies.agn_checked%TYPE,
      agn_checked_by          tqc_agencies.agn_checked_by%TYPE,
      agn_check_date          tqc_agencies.agn_check_date%TYPE,
      agn_comp_comm_arrears   tqc_agencies.agn_comp_comm_arrears%TYPE,
      agn_reinsurer           tqc_agencies.agn_reinsurer%TYPE,
      agn_brn_code            tqc_agencies.agn_brn_code%TYPE,
      agn_town                tqc_agencies.agn_town%TYPE,
      agn_country             tqc_agencies.agn_country%TYPE,
      agn_status_desc         tqc_agencies.agn_status_desc%TYPE,
      agn_id_no               tqc_agencies.agn_id_no%TYPE,
      agn_con_code            tqc_agencies.agn_con_code%TYPE,
      agn_agn_code            tqc_agencies.agn_agn_code%TYPE,
      agn_sms_tel             tqc_agencies.agn_sms_tel%TYPE,
      agn_ahc_code            tqc_agencies.agn_ahc_code%TYPE,
      agn_sec_code            tqc_agencies.agn_sec_code%TYPE,
      agn_agnc_class_code     tqc_agencies.agn_agnc_class_code%TYPE,
      agn_expiry_date         tqc_agencies.agn_expiry_date%TYPE,
      agn_license_no          tqc_agencies.agn_license_no%TYPE,
      agn_runoff              tqc_agencies.agn_runoff%TYPE,
      agn_licensed            tqc_agencies.agn_licensed%TYPE,
      agn_license_grace_pr    tqc_agencies.agn_license_grace_pr%TYPE,
      agn_old_acc_no          tqc_agencies.agn_old_acc_no%TYPE,
      agn_status_remarks      tqc_agencies.agn_status_remarks%TYPE,
      di_affiliate            tqc_agencies.agn_name%TYPE,
      ahc_name                tqc_agency_holding_company.ahc_name%TYPE,
      di_per_no               VARCHAR2 (35),
      di_personnel_names      VARCHAR2 (35),
      di_status_code          VARCHAR2 (35)
   );

   TYPE tqc_agencies_ref IS REF CURSOR
      RETURN tqc_agencies_rec;

   PROCEDURE get_agencies (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agencies_ref
   );

   TYPE tqc_agency_registration_rec IS RECORD (
      areg_code       tqc_agency_registration.areg_code%TYPE,
      areg_agn_code   tqc_agency_registration.areg_agn_code%TYPE,
      areg_year       tqc_agency_registration.areg_year%TYPE,
      areg_reg_no     tqc_agency_registration.areg_reg_no%TYPE,
      areg_wef        tqc_agency_registration.areg_wef%TYPE,
      areg_wet        tqc_agency_registration.areg_wet%TYPE,
      areg_accepted   tqc_agency_registration.areg_accepted%TYPE
   );

   TYPE tqc_agency_registration_ref IS REF CURSOR
      RETURN tqc_agency_registration_rec;

   PROCEDURE get_agency_registration (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agency_registration_ref
   );

   TYPE tqc_agency_directors_rec IS RECORD (
      adir_code             tqc_agency_directors.adir_code%TYPE,
      adir_agn_code         tqc_agency_directors.adir_agn_code%TYPE,
      adir_year             tqc_agency_directors.adir_year%TYPE,
      adir_name             tqc_agency_directors.adir_name%TYPE,
      adir_qualifications   tqc_agency_directors.adir_qualifications%TYPE,
      adir_pct_holdg        tqc_agency_directors.adir_pct_holdg%TYPE,
      adir_designation      tqc_agency_directors.adir_designation%TYPE,
      adir_phone_number     tqc_agency_directors.adir_phone_number%TYPE,
      adir_principle        tqc_agency_directors.adir_principle%TYPE,
      adir_cou_code         tqc_agency_directors.adir_cou_code%TYPE
   );

   TYPE tqc_agency_directors_ref IS REF CURSOR
      RETURN tqc_agency_directors_rec;

   PROCEDURE get_tqc_agency_directors (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agency_directors_ref
   );

   TYPE tqc_agency_referees_rec IS RECORD (
      aref_code               tqc_agency_referees.aref_code%TYPE,
      aref_name               tqc_agency_referees.aref_name%TYPE,
      aref_physical_address   tqc_agency_referees.aref_physical_address%TYPE,
      aref_postal_address     tqc_agency_referees.aref_postal_address%TYPE,
      aref_twn_code           tqc_agency_referees.aref_twn_code%TYPE,
      aref_cou_code           tqc_agency_referees.aref_cou_code%TYPE,
      aref_email_address      tqc_agency_referees.aref_email_address%TYPE,
      aref_tel                tqc_agency_referees.aref_tel%TYPE,
      aref_id_no              tqc_agency_referees.aref_id_no%TYPE,
      aref_agn_code           tqc_agency_referees.aref_agn_code%TYPE
   );

   TYPE tqc_agency_referees_ref IS REF CURSOR
      RETURN tqc_agency_referees_rec;

   PROCEDURE get_tqc_agency_referees (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agency_referees_ref
   );

   TYPE tqc_agn_unassigned_sys_rec IS RECORD (
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      sys_name       tqc_systems.sys_name%TYPE
   );

   TYPE tqc_agn_unassigned_sys_ref IS REF CURSOR
      RETURN tqc_agn_unassigned_sys_rec;

   PROCEDURE get_agn_unassigned_sys (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agn_unassigned_sys_ref
   );

   TYPE tqc_agency_systems_rec IS RECORD (
      csys_code        tqc_client_systems.csys_code%TYPE,
      csys_clnt_code   tqc_client_systems.csys_clnt_code%TYPE,
      csys_sys_code    tqc_client_systems.csys_sys_code%TYPE,
      csys_wef         tqc_client_systems.csys_wef%TYPE,
      csys_wet         tqc_client_systems.csys_wet%TYPE,
      sys_sht_desc     tqc_systems.sys_sht_desc%TYPE
   );

   TYPE tqc_agency_systems_ref IS REF CURSOR
      RETURN tqc_agency_systems_rec;

   PROCEDURE get_agn_assigned_sys (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agency_systems_ref
   );

   TYPE tqc_account_systems_rec IS RECORD (
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      sys_name       tqc_systems.sys_name%TYPE,
      sys_active     tqc_systems.sys_active%TYPE
   );

   TYPE tqc_account_systems_ref IS REF CURSOR
      RETURN tqc_account_systems_rec;

   PROCEDURE get_account_unassigned_systems (
      v_acc_code   IN       NUMBER,
      v_cursor     OUT      tqc_account_systems_ref
   );

   PROCEDURE get_account_assigned_systems (
      v_acc_code   IN       NUMBER,
      v_cursor     OUT      tqc_account_systems_ref
   );

   TYPE tqc_account_contacts_rec IS RECORD (
      accc_code            tqc_account_contacts.accc_code%TYPE,
      accc_agn_code        tqc_account_contacts.accc_acc_code%TYPE,
      accc_name            tqc_account_contacts.accc_name%TYPE,
      accc_username        tqc_account_contacts.accc_username%TYPE,
      accc_other_names     tqc_account_contacts.accc_other_names%TYPE,
      accc_personel_rank   tqc_account_contacts.accc_personel_rank%TYPE,
      accc_created_by      tqc_account_contacts.accc_created_by%TYPE,
      accc_dt_created      tqc_account_contacts.accc_dt_created%TYPE,
      accc_login_allowed   tqc_account_contacts.accc_login_allowed%TYPE,
      accc_status          tqc_account_contacts.accc_status%TYPE,
      accc_pwd_reset       tqc_account_contacts.accc_pwd_reset%TYPE,
      accc_tel             tqc_account_contacts.accc_tel%TYPE,
      accc_email_addr      tqc_account_contacts.accc_email_addr%TYPE
   );

   TYPE tqc_account_contacts_ref IS REF CURSOR
      RETURN tqc_account_contacts_rec;

   PROCEDURE get_account_contacts_sys (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_account_contacts_ref
   );

   TYPE tqc_agency_classes_rec IS RECORD (
      agnc_class_code   tqc_agencies_classes.agnc_class_code%TYPE,
      agnc_class_desc   tqc_agencies_classes.agnc_class_desc%TYPE
   );

   TYPE tqc_agency_classes_ref IS REF CURSOR
      RETURN tqc_agency_classes_rec;

   PROCEDURE get_agency_classes (v_refcur OUT tqc_agency_classes_ref);

   TYPE tqc_agency_holdco_rec IS RECORD (
      ahc_code   tqc_agency_holding_company.ahc_code%TYPE,
      ahc_name   tqc_agency_holding_company.ahc_name%TYPE
   );

   TYPE tqc_agency_holdco_ref IS REF CURSOR
      RETURN tqc_agency_holdco_rec;

   PROCEDURE get_agency_holdco (v_refcur OUT tqc_agency_holdco_ref);

   TYPE account_types IS RECORD (
      v_act_code           tqc_account_types.act_code%TYPE,
      v_act_account_type   tqc_account_types.act_account_type%TYPE
   );

   TYPE account_types_cursor IS REF CURSOR
      RETURN account_types;

   PROCEDURE get_account_types (v_cursor OUT account_types_cursor);

   PROCEDURE get_account_types (
      v_scl_code   IN       NUMBER,
      v_cursor     OUT      account_types_cursor
   );

   TYPE agents_for_commissions IS RECORD (
      v_agn_sht_desc   tqc_agencies.agn_sht_desc%TYPE,
      v_agn_name       tqc_agencies.agn_name%TYPE,
      v_agn_code       tqc_agencies.agn_code%TYPE,
      v_agn_acc_no     tqc_agencies.agn_acc_no%TYPE,
      v_brn_name       tqc_branches.brn_name%TYPE,
      v_agn_act_code   tqc_agencies.agn_act_code%TYPE
   );

   TYPE agents_for_comm_cursor IS REF CURSOR
      RETURN agents_for_commissions;

   PROCEDURE get_agent_commissions (v_ref_cursor OUT agents_for_comm_cursor);

   PROCEDURE get_product_agents (v_ref_cursor OUT agents_for_comm_cursor);

   PROCEDURE get_agns_schedules (
      v_act_code   IN       VARCHAR2,
      v_refcur     IN OUT   tqc_agns_by_acttype_ref
   );

   PROCEDURE get_treaty_agents (v_cursor OUT tqc_agns_by_acttype_ref);

   PROCEDURE get_product_agents (
      v_bcode        IN       NUMBER,
      v_ref_cursor   OUT      agents_for_comm_cursor
   );

   PROCEDURE get_binder_agents (
      v_ref_cursor    OUT      agents_for_comm_cursor,
      v_binder_type   IN       VARCHAR2
   );

   PROCEDURE get_credit_account_lov (
      v_acct             VARCHAR2,
      v_act_name         VARCHAR2,
      v_acctype          VARCHAR2,
      v_cursor     OUT   tqc_agns_by_acttype_ref
   );

   PROCEDURE get_insurance_pymts_agents (
      v_refcur   IN OUT   tqc_agns_by_acttype_ref
   );

   PROCEDURE get_certificate_agents (v_cursor OUT tqc_agns_by_acttype_ref);

   PROCEDURE get_comm_account_types (v_cursor OUT account_types_cursor);

   PROCEDURE get_prod_binder_agents (
      v_ref_cursor   OUT   agents_for_comm_cursor,
      v_agn_name           VARCHAR2
   );

   PROCEDURE get_fac_agencies (v_cursor OUT tqc_agns_by_acttype_ref);

   PROCEDURE get_marketers (v_cursor OUT tqc_agns_by_acttype_ref);

   TYPE agencyassignedsystems_rec IS RECORD (
      sys_code            tqc_systems.sys_code%TYPE,
      sys_sht_desc        tqc_systems.sys_sht_desc%TYPE,
      sys_name            tqc_systems.sys_name%TYPE,
      sys_active          tqc_systems.sys_active%TYPE,
      asys_sys_code       tqc_agency_systems.asys_sys_code%TYPE,
      asys_agn_code       tqc_agency_systems.asys_agn_code%TYPE,
      asys_wef            tqc_agency_systems.asys_wef%TYPE,
      asys_wet            tqc_agency_systems.asys_wet%TYPE,
      asys_comment        tqc_agency_systems.asys_comment%TYPE,
      asys_osd_code       tqc_agency_systems.asys_osd_code%TYPE,
      subdivision_name    VARCHAR2 (50 BYTE),
      asys_osd_id         tqc_agency_systems.asys_osd_id%TYPE,
      asys_agn_sht_desc   tqc_agency_systems.asys_agn_sht_desc%TYPE
   );

   TYPE agencyassignedsystems_ref IS REF CURSOR
      RETURN agencyassignedsystems_rec;

   FUNCTION getallagencyassignedsystems (v_agn_code NUMBER)
      RETURN agencyassignedsystems_ref;

   PROCEDURE get_embassy_contacts (
      v_cou_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_account_contacts_ref
   );

   FUNCTION subdivision_name (v_osd_code VARCHAR2)
      RETURN VARCHAR2;

   TYPE service_prov_rec IS RECORD (
      spr_code       tqc_service_providers.spr_code%TYPE,
      spr_sht_desc   tqc_service_providers.spr_sht_desc%TYPE,
      spr_name       tqc_service_providers.spr_name%TYPE
   );

   TYPE serv_providers_ref IS REF CURSOR
      RETURN service_prov_rec;

   PROCEDURE find_serv_providers (
      v_cursor      OUT      serv_providers_ref,
      v_agentcode   IN       NUMBER
   );

   TYPE agency_service_prov_rec IS RECORD (
      spr_code       tqc_service_providers.spr_code%TYPE,
      spr_sht_desc   tqc_service_providers.spr_sht_desc%TYPE,
      spr_name       tqc_service_providers.spr_name%TYPE,
      agnt_code      tqc_agency_service_providers.agnt_code%TYPE
   );

   TYPE agent_service_providers_ref IS REF CURSOR
      RETURN agency_service_prov_rec;

   FUNCTION getagentserviceproviders (v_agentcode IN NUMBER)
      RETURN agent_service_providers_ref;

   TYPE agencynoneclients_rec IS RECORD (
      clnt_code       tqc_clients.clnt_code%TYPE,
      clnt_sht_desc   tqc_clients.clnt_sht_desc%TYPE,
      client          VARCHAR2 (300)
   );

   TYPE agencynoneclients_ref IS REF CURSOR
      RETURN agencynoneclients_rec;

   FUNCTION fetchagencynoneclients (v_agn_code NUMBER)
      RETURN agencynoneclients_ref;

   PROCEDURE get_agn_by_agn_code (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agns_by_acttype_ref
   );

   TYPE branchagencies_rec IS RECORD (
      bra_code       tqc_branch_agencies.bra_code%TYPE,
      bra_brn_code   tqc_branch_agencies.bra_brn_code%TYPE,
      bra_name       tqc_branch_agencies.bra_name%TYPE,
      bra_agn_code   tqc_branch_agencies.bra_agn_code%TYPE,
      bra_sht_desc   tqc_branch_agencies.bra_sht_desc%TYPE,
      agn_sht_desc   tqc_agencies.agn_sht_desc%TYPE
   );

   TYPE branchagencies_ref IS REF CURSOR
      RETURN branchagencies_rec;

TYPE agency_products_rec IS RECORD (
      pro_code       tq_gis.gin_products.pro_code%TYPE,
      pro_desc    tq_gis.gin_products.pro_desc%TYPE,
      agnp_code          tqc_agency_products.agnp_code%TYPE
   );

   TYPE agency_products_ref IS REF CURSOR
      RETURN agency_products_rec;
      
FUNCTION fetchAgencyProducts(v_agent_code IN NUMBER) RETURN agency_products_ref;
      
FUNCTION fetchNoneAgencyProducts(v_agent_code IN NUMBER) RETURN agency_products_ref;

   FUNCTION fetchbranchagencies
      RETURN branchagencies_ref;

   TYPE allbranches_rec IS RECORD (
      brn_code       tqc_branches.brn_code%TYPE,
      brn_sht_desc   tqc_branches.brn_sht_desc%TYPE,
      brn_name       tqc_branches.brn_name%TYPE
   );

   TYPE allbranches_ref IS REF CURSOR
      RETURN allbranches_rec;

   FUNCTION fetchallbranches (v_org_code IN NUMBER)
      RETURN allbranches_ref;
PROCEDURE add_agency_product(v_agent_code IN NUMBER, v_prod_code IN NUMBER, v_add_edit IN VARCHAR2);

PROCEDURE get_insurance_pymts_agents (
      v_refcur     IN OUT   tqc_agns_by_acttype_ref,
      v_act_code   IN       NUMBER
   );
     
     
   TYPE managers_rec IS RECORD (
      agn_code       tqc_agencies.agn_code%TYPE,
      agn_sht_desc   tqc_agencies.agn_sht_desc%TYPE,
      agn_name       tqc_agencies.agn_name%TYPE
   );

   TYPE managers_ref IS REF CURSOR
      return managers_rec;
      --24112016
      PROCEDURE get_treaty_brokers(v_cursor OUT tqc_agns_by_acttype_ref);
FUNCTION fetch_agent_details (V_agn_Code IN tqc_agencies.agn_code%TYPE)
      RETURN sys_refcursor;
FUNCTION fetch_transferable_items(v_parent_code IN number,v_trn_type varchar2 ) RETURN sys_refcursor;      
END tqc_agencies_cursors; 
/