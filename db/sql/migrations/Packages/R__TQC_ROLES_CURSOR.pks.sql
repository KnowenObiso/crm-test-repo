--
-- TQC_ROLES_CURSOR  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_roles_cursor
AS
/******************************************************************************
   NAME:       TQC_ROLES_CURSOR
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        08/Oct/2009             1. Created this package.
   07082018 -REUPDATE
******************************************************************************/
   TYPE sys_roles_rec IS RECORD (
      srls_code       tqc_sys_roles.srls_code%TYPE,
      srls_name       tqc_sys_roles.srls_name%TYPE,
      srls_crt_date   tqc_sys_roles.srls_crt_date%TYPE,
      srls_sht_desc   tqc_sys_roles.srls_sht_desc%TYPE,
      srls_status     tqc_sys_roles.srls_status%TYPE
   );

   TYPE sys_roles_ref IS REF CURSOR
      RETURN sys_roles_rec;

   FUNCTION get_sys_roles (v_syscode IN tqc_systems.sys_code%TYPE)
      RETURN sys_roles_ref;

   TYPE sys_usrroles_rec IS RECORD (
      sprc_code       tqc_sys_processes.sprc_code%TYPE,
      srprc_code      tqc_sys_roles_processes.srprc_code%TYPE,
      srls_name       tqc_sys_roles.srls_name%TYPE,
      sprc_process    tqc_sys_processes.sprc_process%TYPE,
      srls_crt_date   tqc_sys_roles.srls_crt_date%TYPE,
      srls_status     tqc_sys_roles.srls_status%TYPE,
      srls_code       tqc_sys_roles.srls_code%TYPE,
      srls_sht_desc   tqc_sys_roles.srls_sht_desc%TYPE
   );

   TYPE sys_usrroles_ref IS REF CURSOR
      RETURN sys_usrroles_rec;

   FUNCTION get_user_roles (
      v_syscode   IN   tqc_systems.sys_code%TYPE,
      v_user      IN   tqc_users.usr_code%TYPE
   )
      RETURN sys_usrroles_ref;

   TYPE role_areas_rec IS RECORD (
      sprca_code         tqc_sys_process_areas.sprca_code%TYPE,
      sprca_area         tqc_sys_process_areas.sprca_area%TYPE,
      srpra_srprc_code   tqc_sys_roles_prcs_area.srpra_srprc_code%TYPE,
      srprc_code         tqc_sys_roles_processes.srprc_code%TYPE
   );

   TYPE role_areas_ref IS REF CURSOR
      RETURN role_areas_rec;

   FUNCTION get_role_areas (
      v_process       IN   tqc_sys_processes.sprc_code%TYPE,
      v_roleprocess   IN   tqc_sys_roles_processes.srprc_code%TYPE
   )
      RETURN role_areas_ref;

   TYPE processrole_areas_rec IS RECORD (
      sprca_code         tqc_sys_process_areas.sprca_code%TYPE,
      sprca_area         tqc_sys_process_areas.sprca_area%TYPE,
      srpra_sprca_code   tqc_sys_roles_prcs_area.srpra_srprc_code%TYPE,
      srpra_code         tqc_sys_roles_prcs_area.srpra_code%TYPE,
      srprc_code         tqc_sys_roles_processes.srprc_code%TYPE,
      area_assigned      VARCHAR2 (1)
   );

   TYPE processrole_areas_ref IS REF CURSOR
      RETURN processrole_areas_rec;

   FUNCTION getprocessrolearea (
      v_process       IN   tqc_sys_processes.sprc_code%TYPE,
      v_sysrolecode   IN   tqc_sys_roles_processes.srprc_srls_code%TYPE
   )
      RETURN processrole_areas_ref;

   TYPE role_processes_rec IS RECORD (
      sprc_code          tqc_sys_processes.sprc_code%TYPE,
      srprc_code         tqc_sys_roles_processes.srprc_code%TYPE,
      sprc_process       tqc_sys_processes.sprc_process%TYPE,
      process_assigned   VARCHAR2 (1),
      srprc_srls_code    tqc_sys_roles_processes.srprc_srls_code%TYPE
   );

   TYPE role_processes_ref IS REF CURSOR
      RETURN role_processes_rec;

   FUNCTION getroleprocesses (
      v_syscode       IN   tqc_sys_processes.sprc_sys_code%TYPE,
      v_sysrolecode   IN   tqc_sys_roles_processes.srprc_srls_code%TYPE
   )
      RETURN role_processes_ref;

   TYPE role_subareas_rec IS RECORD (
      srpsa_code           tqc_sys_roles_prcs_s_area.srpsa_code%TYPE,
      sprsa_code           tqc_sys_process_sub_areas.sprsa_code%TYPE,
      sprsa_sub_area       tqc_sys_process_sub_areas.sprsa_sub_area%TYPE,
      sprsa_type           tqc_sys_process_sub_areas.sprsa_type%TYPE,
      srpsa_debit_limit    tqc_sys_roles_prcs_s_area.srpsa_debit_limit%TYPE,
      srpsa_credit_limit   tqc_sys_roles_prcs_s_area.srpsa_credit_limit%TYPE
   );

   TYPE role_subareas_ref IS REF CURSOR
      RETURN role_subareas_rec;

   FUNCTION get_role_subareas (
      v_processarea       IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_roleprocessarea   IN   tqc_sys_roles_prcs_area.srpra_srprc_code%TYPE
   )
      RETURN role_subareas_ref;

   TYPE processrole_subareas_rec IS RECORD (
      srpsa_code           tqc_sys_roles_prcs_s_area.srpsa_code%TYPE,
      sprsa_code           tqc_sys_process_sub_areas.sprsa_code%TYPE,
      sprsa_sub_area       tqc_sys_process_sub_areas.sprsa_sub_area%TYPE,
      sprsa_type           tqc_sys_process_sub_areas.sprsa_type%TYPE,
      srpsa_debit_limit    tqc_sys_roles_prcs_s_area.srpsa_debit_limit%TYPE,
      srpsa_credit_limit   tqc_sys_roles_prcs_s_area.srpsa_credit_limit%TYPE,
      subarea_assigned     VARCHAR2 (1)
   );

   TYPE processrole_subareas_ref IS REF CURSOR
      RETURN processrole_subareas_rec;

   FUNCTION getprocessrole_subareas (
      v_processarea   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_sysrolecode   IN   tqc_sys_roles_processes.srprc_srls_code%TYPE,
      v_process       IN   tqc_sys_processes.sprc_code%TYPE
   )
      RETURN processrole_subareas_ref;

   TYPE users_rec IS RECORD (
      usr_code              tqc_users.usr_code%TYPE,
      usr_username          tqc_users.usr_username%TYPE,
      usr_name              tqc_users.usr_name%TYPE,
      usr_email             tqc_users.usr_email%TYPE,
      usr_personel_rank     tqc_users.usr_personel_rank%TYPE,
      usr_dt_created        tqc_users.usr_dt_created%TYPE,
      usr_type              tqc_users.usr_type%TYPE,
      usr_status            tqc_users.usr_status%TYPE,
      usr_pwd_reset         tqc_users.usr_pwd_reset%TYPE,
      usr_per_id            tqc_users.usr_per_id%TYPE,
      usr_acct_mgr          tqc_users.usr_acct_mgr%TYPE,
      usr_cell_phone_no     tqc_users.usr_cell_phone_no%TYPE,
      usr_wef_dt            tqc_users.usr_wef_dt%TYPE,
      usr_wet_dt            tqc_users.usr_wet_dt%TYPE,
      usr_secq_code         tqc_users.usr_secq_code%TYPE,
      usr_security_answer   tqc_users.usr_security_answer%TYPE,
      usr_created_by        tqc_users.usr_created_by%TYPE,
      SACT_CODE            lms_sub_account_types.SACT_CODE%TYPE,
      SACT_DESCRIPTION  lms_sub_account_types.SACT_DESCRIPTION%TYPE
   );

   TYPE users_ref IS REF CURSOR
      RETURN users_rec;

   FUNCTION get_users
      RETURN users_ref;

   PROCEDURE get_all_users (
      v_users_ref   OUT      users_ref,
      v_date        IN       VARCHAR2 DEFAULT NULL
   );

   FUNCTION get_all_individual_users
      RETURN users_ref;

   PROCEDURE get_accountofficers (v_users_ref OUT users_ref);

   TYPE systems_rec IS RECORD (
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      sys_name       tqc_systems.sys_name%TYPE,
      sys_active     tqc_systems.sys_active%TYPE
   );

   TYPE systems_ref IS REF CURSOR
      RETURN systems_rec;

   FUNCTION get_systems
      RETURN systems_ref;

   TYPE user_systems_rec IS RECORD (
      usys_code      tqc_user_systems.usys_code%TYPE,
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      usys_wef       tqc_user_systems.usys_wef%TYPE,
      sys_name       tqc_systems.sys_name%TYPE,
      usys_wet       tqc_user_systems.usys_wet%TYPE,
      sys_path       tqc_systems.sys_path%TYPE
   );

   TYPE user_systems_ref IS REF CURSOR
      RETURN user_systems_rec;

   FUNCTION get_user_systems (v_user IN tqc_users.usr_code%TYPE)
      RETURN user_systems_ref;

   FUNCTION get_user_active_systems (v_user IN tqc_users.usr_code%TYPE)
      RETURN user_systems_ref;
   FUNCTION get_system_users (v_system IN tqc_user_systems.usys_sys_code%TYPE)
      RETURN users_ref;

   TYPE user_unassigned_systems_rec IS RECORD (
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      sys_name       tqc_systems.sys_name%TYPE,
      sys_active     tqc_systems.sys_active%TYPE
   );

   TYPE user_unassigned_systems_ref IS REF CURSOR
      RETURN user_unassigned_systems_rec;

   FUNCTION get_user_unassigned_systems (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN user_unassigned_systems_ref;

   FUNCTION get_user_assigned_systems (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN user_unassigned_systems_ref;

   TYPE user_unassigned_roles_rec IS RECORD (
      srls_code       tqc_sys_roles.srls_code%TYPE,
      srls_sys_code   tqc_sys_roles.srls_sys_code%TYPE,
      srls_name       tqc_sys_roles.srls_name%TYPE,
      srls_status     tqc_sys_roles.srls_status%TYPE,
      wet_date        DATE,
      wef_date        DATE
   );

   TYPE user_unassigned_roles_ref IS REF CURSOR
      RETURN user_unassigned_roles_rec;

   FUNCTION get_user_unassigned_roles (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_sys_code    IN   tqc_systems.sys_code%TYPE
   )
      RETURN user_unassigned_roles_ref;

   FUNCTION get_user_assigned_roles (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_sys_code    IN   tqc_systems.sys_code%TYPE
   )
      RETURN user_unassigned_roles_ref;

   TYPE user_unassigned_divisions_rec IS RECORD (
      div_code              tqc_divisions.div_code%TYPE,
      div_name              tqc_divisions.div_name%TYPE,
      div_sht_desc          tqc_divisions.div_sht_desc%TYPE,
      div_division_status   tqc_divisions.div_division_status%TYPE
   );

   TYPE user_unassigned_divisions_ref IS REF CURSOR
      RETURN user_unassigned_divisions_rec;

   FUNCTION get_user_unassigned_divisions (
      v_user_code     IN   tqc_users.usr_code%TYPE,
      v_branch_code   IN   tqc_branches.brn_code%TYPE
   )
      RETURN user_unassigned_divisions_ref;

   FUNCTION get_user_assigned_divisions (
      v_user_code     IN   tqc_users.usr_code%TYPE,
      v_branch_code   IN   tqc_branches.brn_code%TYPE
   )
      RETURN user_unassigned_divisions_ref;

   TYPE sys_processes_rec IS RECORD (
      sprc_code      tqc_sys_processes.sprc_code%TYPE,
      sprc_process   tqc_sys_processes.sprc_process%TYPE
   );

   TYPE sys_processes_ref IS REF CURSOR
      RETURN sys_processes_rec;

   FUNCTION get_system_processes (
      v_syscode   IN   tqc_sys_processes.sprc_sys_code%TYPE
   )
      RETURN sys_processes_ref;

   TYPE organizations_rec IS RECORD (
      org_code       tqc_organizations.org_code%TYPE,
      org_sht_desc   tqc_organizations.org_sht_desc%TYPE,
      org_name       tqc_organizations.org_name%TYPE
   );

   TYPE organizations_ref IS REF CURSOR
      RETURN organizations_rec;

   FUNCTION getorganizations
      RETURN organizations_ref;

   FUNCTION get_user_orgs (v_usrcode IN tqc_user_branches.usb_usr_code%TYPE)
      RETURN organizations_ref;

   FUNCTION get_user_assigned_orgs (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN organizations_ref;
      
  FUNCTION get_user_assigned_div_orgs (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN organizations_ref;

   TYPE regions_rec IS RECORD (
      reg_code       tqc_regions.reg_code%TYPE,
      reg_org_code   tqc_regions.reg_org_code%TYPE,
      reg_name       tqc_regions.reg_name%TYPE
   );

   TYPE regions_ref IS REF CURSOR
      RETURN regions_rec;

   FUNCTION getregions (v_orgcode IN tqc_regions.reg_org_code%TYPE)
      RETURN regions_ref;

   FUNCTION get_user_assigned_regions (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN regions_ref;
FUNCTION get_user_assigned_div_regions (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN regions_ref;


   FUNCTION get_user_unassigned_regions (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN regions_ref;
      
   FUNCTION get_regions (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN regions_ref;   
   TYPE branches_rec IS RECORD (
      brn_code       tqc_branches.brn_code%TYPE,
      brn_sht_desc   tqc_branches.brn_sht_desc%TYPE,
      brn_reg_code   tqc_branches.brn_reg_code%TYPE,
      brn_name       tqc_branches.brn_name%TYPE
   );

   TYPE branches_ref IS REF CURSOR
      RETURN branches_rec;

   FUNCTION getbranches (v_regcode IN tqc_branches.brn_reg_code%TYPE)
      RETURN branches_ref;
      
   FUNCTION get_branches (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_reg_code    IN   tqc_regions.reg_code%TYPE
   )
      RETURN branches_ref;

   FUNCTION get_user_unassigned_branches (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_reg_code    IN   tqc_regions.reg_code%TYPE
   )
      RETURN branches_ref;

   FUNCTION get_user_assigned_branches (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_reg_code    IN   tqc_regions.reg_code%TYPE
   )
      RETURN branches_ref;
      
 FUNCTION get_user_assigned_div_branches(
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_reg_code    IN   tqc_regions.reg_code%TYPE
   )
      RETURN branches_ref;

   TYPE usrbranches_rec IS RECORD (
      usb_code       tqc_user_branches.usb_code%TYPE,
      usb_brn_code   tqc_user_branches.usb_brn_code%TYPE,
      usb_usr_code   tqc_user_branches.usb_usr_code%TYPE,
      usb_status     tqc_user_branches.usb_status%TYPE,
      usb_dflt_brn   tqc_user_branches.usb_dflt_brn%TYPE,
      brn_name       tqc_branches.brn_name%TYPE
   );

   TYPE usrbranches_ref IS REF CURSOR
      RETURN usrbranches_rec;

   FUNCTION getuserbranches (
      v_usrcode   IN   tqc_user_branches.usb_usr_code%TYPE,
      v_orgcode   IN   tqc_organizations.org_code%TYPE
   )
      RETURN usrbranches_ref;

   FUNCTION get_user_branches (
      v_usrcode   IN   tqc_user_branches.usb_usr_code%TYPE
   )
      RETURN usrbranches_ref;

   FUNCTION getbranchesbyorg (v_orgcode IN tqc_organizations.org_code%TYPE)
      RETURN branches_ref;

   TYPE default_branch_rec IS RECORD (
      org_code       tqc_organizations.org_code%TYPE,
      org_name       tqc_organizations.org_name%TYPE,
      reg_code       tqc_regions.reg_code%TYPE,
      reg_name       tqc_regions.reg_name%TYPE,
      brn_code       tqc_branches.brn_code%TYPE,
      brn_name       tqc_branches.brn_name%TYPE,
      org_cur_code   tqc_organizations.org_cur_code%TYPE,
      cur_desc       tqc_currencies.cur_desc%TYPE,
      cur_symbol     tqc_currencies.cur_symbol%TYPE
   );

   TYPE default_branch_ref IS REF CURSOR
      RETURN default_branch_rec;

   TYPE default_details_rec IS RECORD (
      org_code             tqc_organizations.org_code%TYPE,
      org_name             tqc_organizations.org_name%TYPE,
      reg_code             tqc_regions.reg_code%TYPE,
      reg_name             tqc_regions.reg_name%TYPE,
      brn_code             tqc_branches.brn_code%TYPE,
      brn_name             tqc_branches.brn_name%TYPE,
      org_cou_code         tqc_organizations.org_cou_code%TYPE,
      cou_name             tqc_countries.cou_name%TYPE,
      cou_admin_reg_type   tqc_countries.cou_admin_reg_type%TYPE,
      org_cur_code         tqc_organizations.org_cur_code%TYPE,
      cou_zip_code         tqc_countries.cou_zip_code%TYPE
   );

   TYPE default_details_ref IS REF CURSOR
      RETURN default_details_rec;

   FUNCTION get_user_defaults (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN default_details_ref;

   FUNCTION get_default_user_branch (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN default_branch_ref;

   FUNCTION get_nondefault_user_branch (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN default_branch_ref;

   TYPE default_division_rec IS RECORD (
      org_code   tqc_organizations.org_code%TYPE,
      org_name   tqc_organizations.org_name%TYPE,
      reg_code   tqc_regions.reg_code%TYPE,
      reg_name   tqc_regions.reg_name%TYPE,
      brn_code   tqc_branches.brn_code%TYPE,
      brn_name   tqc_branches.brn_name%TYPE,
      div_code   tqc_divisions.div_code%TYPE,
      div_name   tqc_divisions.div_name%TYPE
   );

   TYPE default_division_ref IS REF CURSOR
      RETURN default_division_rec;

   FUNCTION get_default_user_division (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN default_division_ref;

   FUNCTION get_nondefault_user_division (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN default_division_ref;

--   PROCEDURE create_user (
--      v_username              IN       tqc_users.usr_username%TYPE,
--      v_names                 IN       tqc_users.usr_name%TYPE,
--      v_password              IN       tqc_users.usr_pwd%TYPE,
--      v_email                 IN       tqc_users.usr_email%TYPE,
--      v_personel              IN       tqc_users.usr_per_rank_sht_desc%TYPE,
--      v_status                IN       tqc_users.usr_status%TYPE,
--      v_type                  IN       tqc_users.usr_type%TYPE,
--      v_reset                 IN       tqc_users.usr_pwd_reset%TYPE,
--      v_per_id                IN       tqc_users.usr_per_id%TYPE,
--      v_user_code             OUT      tqc_users.usr_code%TYPE,
--      v_usr_acct_mgr          IN       tqc_users.usr_acct_mgr%TYPE,
--      v_usr_signature         IN       tqc_users.usr_signature%TYPE,
--      v_usr_cell_phone_no     IN       tqc_users.usr_cell_phone_no%TYPE,
--      v_usr_wef_dt            IN       tqc_users.usr_wef_dt%TYPE ,
--      v_usr_wet_dt            IN       tqc_users.usr_wet_dt%TYPE ,
--      v_usr_secq_code         IN       tqc_users.usr_secq_code%TYPE,
--      v_usr_security_answer   IN       tqc_users.usr_security_answer%TYPE,
--      v_created_by            IN       tqc_users.usr_created_by%TYPE,
--      v_sact_code              IN       LMS_SUB_ACCOUNT_TYPES.SACT_CODE%TYPE      
--   );

PROCEDURE create_user (
      v_username              IN       tqc_users.usr_username%TYPE,
      v_names                 IN       tqc_users.usr_name%TYPE,
      v_password              IN       tqc_users.usr_pwd%TYPE,
      v_email                 IN       tqc_users.usr_email%TYPE,
      v_personel              IN       tqc_users.usr_per_rank_sht_desc%TYPE,
      v_status                IN       tqc_users.usr_status%TYPE,
      v_type                  IN       tqc_users.usr_type%TYPE,
      v_reset                 IN       tqc_users.usr_pwd_reset%TYPE,
      v_per_id                IN       tqc_users.usr_per_id%TYPE,
      v_user_code             OUT      tqc_users.usr_code%TYPE,
      v_usr_acct_mgr          IN       tqc_users.usr_acct_mgr%TYPE,
      v_usr_signature         IN       tqc_users.usr_signature%TYPE,
      v_usr_cell_phone_no     IN       tqc_users.usr_cell_phone_no%TYPE,
      v_usr_wef_dt            IN       tqc_users.usr_wef_dt%TYPE,
      v_usr_wet_dt            IN       tqc_users.usr_wet_dt%TYPE,
      v_usr_secq_code         IN       tqc_users.usr_secq_code%TYPE,
      v_usr_security_answer   IN       tqc_users.usr_security_answer%TYPE,
      v_created_by            IN       tqc_users.usr_created_by%TYPE,
      v_sact_code             IN       lms_sub_account_types.sact_code%TYPE,
      v_pin                   IN       tqc_users.usr_pin%TYPE,
      v_msg                   OUT   VARCHAR2
   );

   PROCEDURE update_user (
      v_user                  IN       tqc_users.usr_code%TYPE,
      v_username              IN       tqc_users.usr_username%TYPE,
      v_names                 IN       tqc_users.usr_name%TYPE,
      v_email                 IN       tqc_users.usr_email%TYPE,
      v_personel              IN       tqc_users.usr_per_rank_sht_desc%TYPE,
      v_type                  IN       tqc_users.usr_type%TYPE,
      v_reset                 IN       tqc_users.usr_pwd_reset%TYPE,
      v_per_id                IN       tqc_users.usr_per_id%TYPE,
      v_status                IN       tqc_users.usr_status%TYPE,
      v_usr_acct_mgr          IN       tqc_users.usr_acct_mgr%TYPE,
      v_usr_cell_phone_no     IN       tqc_users.usr_cell_phone_no%TYPE,
      v_usr_wef_dt            IN       tqc_users.usr_wef_dt%TYPE,
      v_usr_wet_dt            IN       tqc_users.usr_wet_dt%TYPE,
      v_usr_secq_code         IN       tqc_users.usr_secq_code%TYPE,
      v_usr_security_answer   IN       tqc_users.usr_security_answer%TYPE,
      v_sact_code             IN       lms_sub_account_types.sact_code%TYPE
            DEFAULT NULL,
      v_posted_by             IN       VARCHAR DEFAULT NULL,
      v_pin                   IN       VARCHAR,
      v_msg                   OUT      VARCHAR
   );

   PROCEDURE drop_user (v_user IN tqc_users.usr_code%TYPE);

   PROCEDURE restore_user (v_user IN tqc_users.usr_code%TYPE);
   
   PROCEDURE grant_user_system (
      v_user       IN   tqc_users.usr_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE
   );

   PROCEDURE grant_user_system (
      v_user       IN   tqc_users.usr_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   );

   PROCEDURE revoke_user_system (
      v_user       IN   tqc_users.usr_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   );

   PROCEDURE revoke_user_role (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_user       IN   tqc_users.usr_code%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   );

   PROCEDURE grant_user_role (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_user       IN   tqc_users.usr_code%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   );

   PROCEDURE grant_role_process_area (
      v_processarea   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_rolecode      IN   tqc_sys_roles_processes.srprc_srls_code%TYPE
   );

   PROCEDURE revoke_role_process_area (
      v_processarea   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_role_code     IN   tqc_sys_roles.srls_code%TYPE
   );

   PROCEDURE grant_role_process_subarea (
      v_processsubarea   IN   tqc_sys_process_sub_areas.sprsa_code%TYPE,
      v_rolecode         IN   tqc_sys_roles_processes.srprc_srls_code%TYPE,
      v_debitlimit       IN   tqc_sys_roles_prcs_s_area.srpsa_debit_limit%TYPE,
      v_creditlimit      IN   tqc_sys_roles_prcs_s_area.srpsa_credit_limit%TYPE
   );

   PROCEDURE revoke_role_process_subarea (
      v_processsubarea   IN   tqc_sys_process_sub_areas.sprsa_code%TYPE,
      v_role_code        IN   tqc_sys_roles.srls_code%TYPE
   );

    PROCEDURE creat_role (
      v_syscode         IN   tqc_sys_roles.srls_sys_code%TYPE,
      v_role_name       IN   tqc_sys_roles.srls_name%TYPE,
      v_role_sht_desc   IN   tqc_sys_roles.srls_sht_desc%TYPE,
      v_user            IN   varchar2 DEFAULT NULL
   );

     PROCEDURE edit_role (
      v_role_code     IN     tqc_sys_roles.srls_code%TYPE,
      v_role_name       IN   tqc_sys_roles.srls_name%TYPE,
      v_role_sht_desc   IN   tqc_sys_roles.srls_sht_desc%TYPE,
      v_status          IN   tqc_sys_roles.srls_status%TYPE,
      v_posted_by       IN   VARCHAR2
   );

   PROCEDURE restore_role (
      v_roleprocess   IN   tqc_sys_roles_processes.srprc_code%TYPE
   );

   PROCEDURE drop_role (
      v_roleprocess   IN   tqc_sys_roles_processes.srprc_code%TYPE
   );

   PROCEDURE edit_user_syswet (
      v_syscode   IN   tqc_user_systems.usys_code%TYPE,
      v_wet       IN   tqc_user_systems.usys_wet%TYPE
   );

   PROCEDURE grant_user_branch (
      v_user          IN   tqc_user_branches.usb_usr_code%TYPE,
      v_branch_code   IN   tqc_user_branches.usb_brn_code%TYPE,
      v_updated_by    IN   tqc_users.usr_username%TYPE
   );

   PROCEDURE revoke_user_branch (
      v_user          IN   tqc_user_branches.usb_usr_code%TYPE,
      v_branch_code   IN   tqc_user_branches.usb_brn_code%TYPE,
      v_updated_by    IN   tqc_users.usr_username%TYPE
   );

   PROCEDURE enabledisablebranch (
      v_usrbranchcode   IN   tqc_user_branches.usb_code%TYPE
   );

   PROCEDURE makedefaultbranch (
      v_usrbranchcode   IN   tqc_user_branches.usb_code%TYPE
   );

   PROCEDURE grantprocessrole (
      v_processcode    IN   tqc_sys_processes.sprc_code%TYPE,
      v_rolecode       IN   tqc_sys_roles.srls_code%TYPE,
      v_process_only   IN   VARCHAR2 DEFAULT 'N'
   );

   PROCEDURE revokeprocessrole (
      v_processcode   IN   tqc_sys_roles_processes.srprc_sprc_code%TYPE,
      v_rolecode      IN   tqc_sys_roles_processes.srprc_srls_code%TYPE
   );

   PROCEDURE assign_user_default_branch (
      v_user_code     IN   tqc_user_branches.usb_usr_code%TYPE,
      v_branch_code   IN   tqc_user_branches.usb_brn_code%TYPE
   );

   PROCEDURE assign_user_default_division (
      v_user_code       IN   tqc_user_divisions.usd_usr_code%TYPE,
      v_division_code   IN   tqc_user_divisions.usd_div_code%TYPE
   );

   TYPE userassignedsystems_rec IS RECORD (
      sys_code          tqc_systems.sys_code%TYPE,
      sys_sht_desc      tqc_systems.sys_sht_desc%TYPE,
      sys_name          tqc_systems.sys_name%TYPE,
      sys_active        tqc_systems.sys_active%TYPE,
      usys_code         tqc_user_systems.usys_code%TYPE,
      usys_usr_code     tqc_user_systems.usys_usr_code%TYPE,
      usys_sys_code     tqc_user_systems.usys_sys_code%TYPE,
      usys_wef          tqc_user_systems.usys_wef%TYPE,
      usys_wet          tqc_user_systems.usys_wet%TYPE,
      usys_spost_code   tqc_user_systems.usys_spost_code%TYPE,
      spost_desc        VARCHAR2 (50 BYTE)
   );

   TYPE userassignedsystems_ref IS REF CURSOR
      RETURN userassignedsystems_rec;

   FUNCTION getalluserassignedsystems (v_user_code NUMBER)
      RETURN userassignedsystems_ref;

   FUNCTION sys_post_name (v_spost_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION getregionsfiltered (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code        tqc_user_branches.usb_usr_code%TYPE
   )
      RETURN regions_ref;

   PROCEDURE grant_user_branchbyorg (
      v_user       IN   tqc_user_branches.usb_usr_code%TYPE,
      v_org_code   IN   tqc_regions.reg_org_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   );

   PROCEDURE revoke_user_branchbyorg (
      v_user       IN       tqc_user_branches.usb_usr_code%TYPE,
      v_org_code   IN       tqc_regions.reg_org_code%TYPE,
      v_err        OUT      VARCHAR2,
      v_updated_by IN   tqc_users.usr_username%TYPE
   );

   FUNCTION get_user_orgsfiltered (
      v_usrcode   IN   tqc_user_branches.usb_usr_code%TYPE
   )
      RETURN organizations_ref;

   PROCEDURE grant_user_allroleinsys (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_user       IN   tqc_users.usr_code%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   );

   PROCEDURE revoke_user_allroleinsys (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_user       IN   tqc_users.usr_code%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   );
   
   PROCEDURE wet_user_allroleinsys (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_wet        IN   tqc_sys_user_roles.usrr_revoke_date%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE
   );
   
   PROCEDURE wef_user_allroleinsys (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_wef        IN   tqc_sys_user_roles.usrr_grant_date%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE
   );

   FUNCTION get_assignedsystems_withrole (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN user_unassigned_systems_ref;

   FUNCTION get_unassignedsystems_withrole (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN user_unassigned_systems_ref;

   PROCEDURE get_usersnotingrp (v_users_ref OUT users_ref, v_grp_code IN NUMBER);

   FUNCTION get_usremail (v_user_name VARCHAR2)
      RETURN VARCHAR2;

   FUNCTION get_usremailaddr (v_user_code NUMBER)
      RETURN VARCHAR2;

   PROCEDURE getfulluserdetails (
      v_username      IN       tqc_users.usr_username%TYPE,
      v_userdetails   OUT      users_ref
   );

   PROCEDURE create_system_administrator (v_user VARCHAR2);

   TYPE test_user_rec IS RECORD (
      usr_username   VARCHAR2 (30),
      usr_email      VARCHAR2 (30)
   );

   TYPE test_user_ref IS REF CURSOR
      RETURN test_user_rec;

   TYPE securityquestions_rec IS RECORD (
      secq_code       tqc_security_questions.secq_code%TYPE,
      secq_sht_desc   tqc_security_questions.secq_sht_desc%TYPE,
      secq_desc       tqc_security_questions.secq_desc%TYPE
   );

   TYPE securityquestions_ref IS REF CURSOR
      RETURN securityquestions_rec;

   FUNCTION getallsecurityquestions
      RETURN securityquestions_ref;

   PROCEDURE update_security_questions (
      v_action          IN   VARCHAR2,
      v_secq_code       IN   NUMBER,
      v_secq_sht_desc   IN   VARCHAR2,
      v_secq_desc       IN   VARCHAR2
   );
   
   PROCEDURE create_missing_prc_s_areas (
      v_sys_code          TQC_SYSTEMS.SYS_CODE%TYPE,
      v_sprc_sht_desc     TQC_SYS_PROCESSES.SPRC_SHT_DESC%TYPE,
      v_sprc_process      TQC_SYS_PROCESSES.SPRC_PROCESS%TYPE,
      v_sprca_sht_desc    TQC_SYS_PROCESS_AREAS.SPRCA_SHT_DESC%TYPE,
      v_sprca_area        TQC_SYS_PROCESS_AREAS.SPRCA_AREA%TYPE,
      v_sprsa_sht_desc    TQC_SYS_PROCESS_SUB_AREAS.SPRsA_SHT_DESC%TYPE,
      v_sprsa_sub_area    TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SUB_AREA%TYPE,
      v_sprsa_type        TQC_SYS_PROCESS_SUB_AREAS.SPRSA_TYPE%TYPE);
PROCEDURE grant_process_sub_area (
      v_sys_code          TQC_SYSTEMS.SYS_CODE%TYPE,
      v_srls_sht_desc     TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
      v_srls_name         TQC_SYS_ROLES.SRLS_NAME%TYPE,
      v_sprc_sht_desc     TQC_SYS_PROCESSES.SPRC_SHT_DESC%TYPE,
      v_sprca_sht_desc    TQC_SYS_PROCESS_AREAS.SPRCA_SHT_DESC%TYPE,
      v_sprsa_sht_desc    TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SHT_DESC%TYPE,
      v_grant             VARCHAR2,
      v_debit_limit       TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_DEBIT_LIMIT%TYPE,
      v_credit_limit      TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CREDIT_LIMIT%TYPE);
     
 PROCEDURE get_normal_users (
      v_users_ref   OUT      users_ref,
      v_date        IN       VARCHAR2 DEFAULT NULL
   );
  
 PROCEDURE revoke_role_process (
      v_srprc_code   IN   tqc_sys_roles_processes.SRPRC_CODE%TYPE
   );
   PROCEDURE authorize_user (
      v_msg               OUT      VARCHAR2,
      v_posted_by         IN       tqc_users.usr_name%TYPE,
      v_user_code          IN       tqc_users.usr_name%TYPE    
   );
   PROCEDURE authorize_role (
      v_msg               OUT      VARCHAR2,
      v_posted_by         IN       VARCHAR2,
      v_role_code         IN       NUMBER    
   );
   FUNCTION getGisUsers
      RETURN users_ref;
      
   TYPE debt_owners_rec IS RECORD (
      v_usr_code          tqc_users.usr_code%TYPE,
      v_usr_name          tqc_users.usr_name%TYPE,
      v_usr_username      tqc_users.usr_username%TYPE,
      v_usr_email          tqc_users.usr_email%TYPE,
       usr_personel_rank   tqc_users.usr_personel_rank%TYPE, 
       usr_dt_created    tqc_users.usr_dt_created%TYPE,
       usr_type    tqc_users.usr_type%TYPE,
       usr_status  tqc_users.usr_status%TYPE,
       usr_pwd_reset tqc_users.usr_pwd_reset%TYPE
   );
TYPE debt_owners_ref IS REF CURSOR RETURN debt_owners_rec;
      FUNCTION  getGisDebtOwners(
   v_usr_name   IN   VARCHAR2 DEFAULT NULL
)
   RETURN debt_owners_ref;
   TYPE dt_owners_rec IS RECORD (
      v_usr_code          tqc_users.usr_code%TYPE,
      v_usr_name          tqc_users.usr_name%TYPE,
      v_usr_username      tqc_users.usr_username%TYPE,
      v_usr_email          tqc_users.usr_email%TYPE,
       usr_personel_rank   tqc_users.usr_personel_rank%TYPE, 
       usr_dt_created    tqc_users.usr_dt_created%TYPE,
       usr_type    tqc_users.usr_type%TYPE,
       usr_status  tqc_users.usr_status%TYPE,
       usr_pwd_reset tqc_users.usr_pwd_reset%TYPE
   );
   TYPE dt_owners_ref IS REF CURSOR RETURN debt_owners_rec;
        FUNCTION  getGisDtOwners(
   v_usr_name   IN   VARCHAR2 DEFAULT NULL
)
   RETURN dt_owners_ref;
END tqc_roles_cursor;
/