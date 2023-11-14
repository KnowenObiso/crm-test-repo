--
-- TQC_PORTAL_CURSOR  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."TQC_PORTAL_CURSOR"
AS
/******************************************************************************
 NAME: TQ_CRM.TQC_PORTAL_CURSOR
 PURPOSE:

 REVISIONS:
 Ver Date Author Description
 --------- ---------- --------------- ------------------------------------
 1.0 9/20/2012 1. Created this package.
******************************************************************************/
   TYPE product_categories_rec IS RECORD (
      twpc_code          tqc_web_product_categories.twpc_code%TYPE,
      twpc_sys_code      tqc_web_product_categories.twpc_sys_code%TYPE,
      twpc_name          tqc_web_product_categories.twpc_name%TYPE,
      twpc_description   tqc_web_product_categories.twpc_description%TYPE
   );

   TYPE product_categories_ref IS REF CURSOR
      RETURN product_categories_rec;

   FUNCTION getproductcategories (
      v_syscode          NUMBER,
      v_prodcode    IN   NUMBER DEFAULT NULL,
      v_agentcode   IN   NUMBER DEFAULT NULL
   )
      RETURN product_categories_ref;

   TYPE products_rec IS RECORD (
      twp_code        tqc_web_products.twp_code%TYPE,
      twp_twpc_code   tqc_web_products.twp_twpc_code%TYPE,
      twp_prod_code   tqc_web_products.twp_prod_code%TYPE,
      prod_name       VARCHAR2 (50),
      twp_prod_desc   tqc_web_products.twp_prod_desc%TYPE,
      twp_bind_code   tqc_web_products.twp_bind_code%TYPE,
      twp_binder      tqc_web_products.twp_binder%TYPE,
      bind_name       gin_binders.bind_name%TYPE,
      agn_sht_desc    tqc_agencies.agn_sht_desc%TYPE,
      twp_aga_code    tqc_web_products.twp_aga_code%TYPE,
      aga_code        tqc_agency_accounts.aga_code%TYPE,
      aga_name        tqc_agency_accounts.aga_name%TYPE,
       twp_prod_url tqc_web_products.twp_prod_url%type
   );

   TYPE products_ref IS REF CURSOR
      RETURN products_rec;

   FUNCTION getproducts (v_twpccode NUMBER, v_syscode NUMBER DEFAULT NULL)
      RETURN products_ref;

   TYPE product_options_rec IS RECORD (
      twpo_code       tqc_web_prod_options.twpo_code%TYPE,
      twpo_pop_code   tqc_web_prod_options.twpo_pop_code%TYPE,
      twpo_desc       tqc_web_prod_options.twpo_desc%TYPE,
      pop_desc        lms_prod_options.pop_desc%TYPE,
      bind_name       gin_binders.bind_name%TYPE
   );

   TYPE product_options_ref IS REF CURSOR
      RETURN product_options_rec;

   FUNCTION getproductoption (v_prodcode NUMBER, v_syscode NUMBER DEFAULT NULL)
      RETURN product_options_ref;

   PROCEDURE getlifequoteoutput (
      v_quotecode    IN       NUMBER,
      v_totpremium   OUT      NUMBER,
      v_totsa        OUT      NUMBER
   );

   TYPE dynamic_values_rec IS RECORD (
      tsmsd_prompt       tqc_sys_mod_subunits_details.tsmsd_prompt%TYPE,
      tsmsi_value        tqc_sys_mod_subunits_inputs.tsmsi_value%TYPE,
      tsmsi_mode_code    tqc_sys_mod_subunits_inputs.tsmsi_mode_code%TYPE,
      tsmsi_row          tqc_sys_mod_subunits_inputs.tsmsi_row%TYPE,
      tsmsi_column       tqc_sys_mod_subunits_inputs.tsmsi_column%TYPE,
      tsmsi_code         tqc_sys_mod_subunits_inputs.tsmsi_code%TYPE,
      tmsc_column_name   tqc_mod_sys_columns.tmsc_column_name%TYPE,
      tmsc_table_name    tqc_mod_sys_columns.tmsc_table_name%TYPE
   );

   TYPE dynamic_values_ref IS REF CURSOR
      RETURN dynamic_values_rec;

   FUNCTION getdynamicvalues (v_mod VARCHAR2, v_pkid NUMBER)
      RETURN dynamic_values_ref;

   TYPE sys_prod_rec IS RECORD (
      prod_code       lms_products.prod_code%TYPE,
      prod_sht_desc   lms_products.prod_sht_desc%TYPE,
      prod_desc       lms_products.prod_desc%TYPE
   );

   TYPE sys_prod_ref IS REF CURSOR
      RETURN sys_prod_rec;

   FUNCTION getlifeproducts (v_syscode IN NUMBER DEFAULT NULL)
      RETURN sys_prod_ref;

   TYPE sys_prod_options_rec IS RECORD (
      pop_code       lms_prod_options.pop_code%TYPE,
      pop_sht_desc   lms_prod_options.pop_sht_desc%TYPE,
      pop_desc       lms_prod_options.pop_desc%TYPE
   );

   TYPE sys_prod_options_ref IS REF CURSOR
      RETURN sys_prod_options_rec;

   FUNCTION getprodoptions (v_prodcode NUMBER, v_syscode NUMBER DEFAULT NULL)
      RETURN sys_prod_options_ref;

   TYPE portfolio_rec IS RECORD (
      pol_code_batch_no   NUMBER,
      sys_code            NUMBER,
      policy_number       VARCHAR2 (50),
      prop_quote_no       VARCHAR2 (30),
      prod_desc           VARCHAR2 (100),
      cover_from          DATE,
      cover_to            DATE,
      amount_insured      NUMBER,
      status              VARCHAR2 (5),
      done_by             VARCHAR2 (100),
      pol_inception_dt    DATE,
      quot_ok             VARCHAR2 (30),
      prod_type           VARCHAR2 (30),
      premium             NUMBER,
      balance             NUMBER,
      pro_code           NUMBER
   );

   TYPE portfolio_ref IS REF CURSOR
      RETURN portfolio_rec;

--   FUNCTION getclientportfolio (v_clientcode NUMBER, v_usercode NUMBER)
--      RETURN portfolio_ref;
FUNCTION getclientportfolio (
    v_clientcode NUMBER,
    v_usercode NUMBER, 
    v_page_no NUMBER default 1, 
    v_page_sz NUMBER default 100
    )
      RETURN sys_refcursor;
 TYPE claims_rec IS RECORD(
 POLICY_NO VARCHAR2(50),
 PROPERTY_DESC VARCHAR2(100),
 LOSS_DATE DATE,
 NOTIFICATION_DATE DATE,
 DESCRIPTION VARCHAR2(250),
 CLAIM_NO VARCHAR2(50),
 STATUS VARCHAR2(50) ,
 CLAIM_REF_NO NUMBER,
 PRO_CODE NUMBER
 );

   TYPE claims_ref IS REF CURSOR
      RETURN claims_rec;

   FUNCTION getclientclaims (v_clientcode NUMBER, v_usercode NUMBER)
      RETURN claims_ref;

   TYPE systems_rec IS RECORD (
      sys_code   tqc_systems.sys_code%TYPE,
      sys_name   tqc_systems.sys_name%TYPE
   );

   TYPE systems_ref IS REF CURSOR
      RETURN systems_rec;

   FUNCTION getsystems
      RETURN systems_ref;

   TYPE serv_requests_rec IS RECORD (
      tsr_code              tqc_serv_requests.tsr_code%TYPE,
      tsr_tsrc_code         tqc_serv_requests.tsr_tsrc_code%TYPE,
      tsr_type              tqc_serv_requests.tsr_type%TYPE,
      tsr_acc_type          tqc_serv_requests.tsr_acc_type%TYPE,
      tsr_acc_code          tqc_serv_requests.tsr_acc_code%TYPE,
      tsr_date              tqc_serv_requests.tsr_date%TYPE,
      tsr_assignee          tqc_serv_requests.tsr_assignee%TYPE,
      tsr_owner_type        tqc_serv_requests.tsr_owner_type%TYPE,
      tsr_owner_code        tqc_serv_requests.tsr_owner_code%TYPE,
      tsr_status            tqc_serv_requests.tsr_status%TYPE,
      tsr_due_date          tqc_serv_requests.tsr_due_date%TYPE,
      tsr_resolution_date   tqc_serv_requests.tsr_resolution_date%TYPE,
      tsr_summary           tqc_serv_requests.tsr_summary%TYPE,
      tsr_description       tqc_serv_requests.tsr_description%TYPE,
      tsr_solution          tqc_serv_requests.tsr_solution%TYPE,
      tsrc_name             tqc_serv_req_cat.tsrc_name%TYPE,
      acc_type              VARCHAR2 (20),
      assignee              VARCHAR2 (100),
      account_name          VARCHAR2 (100),
      owner_type            VARCHAR2 (20),
      owner                 VARCHAR2 (100)
   );

   TYPE serv_requests_ref IS REF CURSOR
      RETURN serv_requests_rec;

   FUNCTION getclientallopenrequests (v_clientcode NUMBER, v_usercode NUMBER)
      RETURN serv_requests_ref;

   TYPE web_prod_settings_rec IS RECORD (
      twpd_clnt_code   tqc_web_product_details.twpd_clnt_code%TYPE,
      twpd_usr_code    tqc_web_product_details.twpd_usr_code%TYPE,
      twpd_username    tqc_web_product_details.twpd_username%TYPE,
      twpd_dr_limit    tqc_web_product_details.twpd_dr_limit%TYPE,
      twpd_cr_limit    tqc_web_product_details.twpd_cr_limit%TYPE,
      auth             VARCHAR2 (1)
   );

   TYPE web_prod_settings_ref IS REF CURSOR
      RETURN web_prod_settings_rec;

   FUNCTION getprodsettings (
      v_quotecode    NUMBER,
      v_syscode      NUMBER,
      v_clientcode   NUMBER
   )
      RETURN web_prod_settings_ref;

   FUNCTION getsystemactive (v_syscode NUMBER)
      RETURN VARCHAR2;

    FUNCTION getclientquotations (
    v_clientcode NUMBER,
    v_usercode NUMBER, 
    v_page_no NUMBER default 1, 
    v_page_sz NUMBER default 100
   )
      RETURN sys_refcursor;
   
   TYPE vehiclemake_rec IS RECORD (
        vmk_code             gin_vehicle_make.vmk_code%TYPE,
        vmk_name             gin_vehicle_make.vmk_name%TYPE
 );
 TYPE vehiclemake_dtls IS REF CURSOR RETURN vehiclemake_rec;

  TYPE vehiclemodel_rec IS RECORD (
      vmod_code             gin_vehicle_model.vmod_code%TYPE,
        vmod_name             gin_vehicle_model.vmod_name%TYPE
 );
 TYPE vehiclemodel_dtls IS REF CURSOR RETURN vehiclemodel_rec;
 
 
   PROCEDURE lovVehicleModels(
v_models OUT vehiclemodel_dtls,
v_vmk_code IN NUMBER
 ) ;
   PROCEDURE lovVehicleMakes(
v_makes OUT vehiclemake_dtls
 ); 
 
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
                            v_tel               VARCHAR2,
                            v_pin               VARCHAR2
                            );  
FUNCTION get_client_claim (v_clientcode NUMBER, v_usercode NUMBER,v_claim_no NUMBER,v_claimref_no NUMBER)
      RETURN claims_ref;                            
FUNCTION get_client_open_requests_count(v_clientCode NUMBER, v_userCode NUMBER)
      RETURN NUMBER;   
FUNCTION get_banks 
      RETURN sys_refcursor;
FUNCTION get_binders (v_scl_code IN gin_sub_classes.scl_code%TYPE,v_covt_code IN gin_cover_types.covt_code%TYPE)
      RETURN sys_refcursor;
FUNCTION get_currencies( 
       v_base_cur_code out number, 
       v_base_cur_symbol out varchar2,
       v_base_cur_rnd out number 
   )
      RETURN sys_refcursor;
FUNCTION get_section_type (v_sect_code IN gin_sub_classes.scl_code%TYPE)
      RETURN sys_refcursor;
FUNCTION get_subclass (v_scl_code IN NUMBER DEFAULT NULL)
      RETURN sys_refcursor;
TYPE agn_products_rec IS RECORD(
 
  agnp_agn_code number, 
  agnp_code number, 
  agnp_prod_code number,
  pro_desc VARCHAR2(100),
  sys_code number, 
  sys_desc VARCHAR2(100));
TYPE agn_products_ref IS REF CURSOR RETURN agn_products_rec;
FUNCTION getAgentProducts(v_agnCode    NUMBER,v_sysCode NUMBER DEFAULT NULL)
RETURN agn_products_ref; 
FUNCTION get_covertypes (v_scl_code IN gin_sub_classes.scl_code%TYPE)
      RETURN sys_refcursor; 
FUNCTION get_security_questions
      RETURN sys_refcursor;   
TYPE ports_rec IS RECORD (
    port_code                  tqc_ports.port_code%TYPE,
    port_country_iso3       tqc_ports.port_country_iso3%TYPE,
    port_name                  tqc_ports.port_name%TYPE,
    port_country_id              tqc_ports.port_country_id%TYPE,
    port_type                  tqc_ports.port_type%TYPE
   );

   TYPE ports_ref IS REF CURSOR
      RETURN ports_rec;      
FUNCTION get_ports(v_country_iso3 IN VARCHAR2 ,v_type IN VARCHAR2 )
      RETURN ports_ref; 
FUNCTION get_pol_risks (V_Pol_Batch_No IN gin_sub_classes.scl_code%TYPE)
      RETURN sys_refcursor;  
 TYPE country_rec IS RECORD (
      id               country.id%TYPE,
      iso3             country.iso3%TYPE,
      name             country.name%TYPE,
      admin_reg_type   varchar(200)
   );

   TYPE country_ref IS REF CURSOR
      RETURN country_rec;

	  FUNCTION get_countries
      RETURN country_ref;                                                                         
END tqc_portal_cursor; 
/