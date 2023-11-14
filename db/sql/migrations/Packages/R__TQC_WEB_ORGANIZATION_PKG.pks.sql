--
-- TQC_WEB_ORGANIZATION_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_web_organization_pkg
AS
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL);

    PROCEDURE update_organization (
      v_add_edit            IN       VARCHAR2,
      v_org_code            IN       tqc_organizations.org_code%TYPE,
      v_org_sht_desc        IN       tqc_organizations.org_sht_desc%TYPE,
      v_org_name            IN       tqc_organizations.org_name%TYPE,
      v_org_addrs           IN       tqc_organizations.org_addrs%TYPE,
      v_org_twn_code        IN       tqc_organizations.org_twn_code%TYPE,
      v_org_cou_code        IN       tqc_organizations.org_cou_code%TYPE,
      v_org_email_addrs     IN       tqc_organizations.org_email_addrs%TYPE,
      v_org_phy_addrs       IN       tqc_organizations.org_phy_addrs%TYPE,
      v_org_cur_code        IN       tqc_organizations.org_cur_code%TYPE,
      v_org_zip             IN       tqc_organizations.org_zip%TYPE,
      v_org_fax             IN       tqc_organizations.org_fax%TYPE,
      v_org_tel1            IN       tqc_organizations.org_tel1%TYPE,
      v_org_tel2            IN       tqc_organizations.org_tel2%TYPE,
      v_org_rpt_logo        IN       tqc_organizations.org_rpt_logo%TYPE,
      v_org_motto           IN       tqc_organizations.org_motto%TYPE,
      v_org_pin_no          IN       tqc_organizations.org_pin_no%TYPE,
      v_org_ed_code         IN       tqc_organizations.org_ed_code%TYPE,
      v_org_item_acc_code   IN       tqc_organizations.org_item_acc_code%TYPE,
      v_org_other_name      IN       tqc_organizations.org_other_name%TYPE,
      v_org_type            IN       tqc_organizations.org_type%TYPE,
      v_org_web_brn_code    IN       tqc_organizations.org_web_brn_code%TYPE,
      v_org_web_addrs       IN       tqc_organizations.org_web_addrs%TYPE,
      v_org_agn_code        IN       tqc_organizations.org_agn_code%TYPE,
      v_org_directors       IN       tqc_organizations.org_directors%TYPE,
      v_org_lang_code       IN       tqc_organizations.org_lang_code%TYPE,
      v_org_avatar          IN       VARCHAR DEFAULT NULL,
      v_org_sts_code        IN       tqc_organizations.org_sts_code%TYPE,
      v_org_grp_logo        IN       tqc_organizations.org_grp_logo%TYPE,
      v_err                 OUT      VARCHAR2,
      v_org_b64                      tqc_organizations.org_logo_b64%TYPE  DEFAULT NULL,
      v_vatnumber           IN       tqc_organizations.org_vat_number%TYPE,
      v_mobile1             IN       tqc_organizations.org_mobile1%TYPE,
      v_mobile2             IN       tqc_organizations.org_mobile2%TYPE,
      v_cert_sign           IN       tqc_organizations.org_cert_sign%TYPE, 
      v_org_bank_account_name    IN       tqc_organizations.org_bank_account_name%TYPE,
      v_org_bank_account_no      IN       tqc_organizations.org_bank_account_no%TYPE,
      v_org_swift_code           IN       tqc_organizations.org_swift_code%TYPE,
      v_org_bnk_code             IN       tqc_organizations.org_bnk_code%TYPE,
      v_org_bbr_code             IN       tqc_organizations.org_bbr_code%TYPE
   );

   PROCEDURE update_regions (
      v_add_edit                  IN       VARCHAR2,
      v_reg_code                  IN       tqc_regions.reg_code%TYPE,
      v_reg_org_code              IN       tqc_regions.reg_org_code%TYPE,
      v_reg_sht_desc              IN       tqc_regions.reg_sht_desc%TYPE,
      v_reg_name                  IN       tqc_regions.reg_name%TYPE,
      v_reg_wef                   IN       tqc_regions.reg_wef%TYPE,
      v_reg_wet                   IN       tqc_regions.reg_wet%TYPE,
      v_reg_agn_code              IN       tqc_regions.reg_agn_code%TYPE,
      v_reg_post_level            IN       tqc_regions.reg_post_level%TYPE,
      v_reg_mngr_allowed          IN       tqc_regions.reg_mngr_allowed%TYPE,
      v_reg_overide_comm_earned   IN       tqc_regions.reg_overide_comm_earned%TYPE,
      v_reg_brn_mngr_seq_no       IN       tqc_regions.reg_brn_mngr_seq_no%TYPE,
      v_reg_agn_seq_no            IN       tqc_regions.reg_agn_seq_no%TYPE,
      v_err                       OUT      VARCHAR2
   );
   
   PROCEDURE bank_region_prc (
      v_add_edit                     VARCHAR2,
      v_bnkr_code                     tqc_bank_regions.bnkr_code%type,
      v_bnkr_org_code                 tqc_bank_regions.bnkr_org_code%type,
      v_bnkr_sht_desc                 tqc_bank_regions.bnkr_sht_desc%type,
      v_bnkr_name                     tqc_bank_regions.bnkr_name%type,
      v_bnkr_wef                      tqc_bank_regions.bnkr_wef%type,
      v_bnkr_wet                      tqc_bank_regions.bnkr_wet%type,
      v_bnkr_reg_code                 tqc_bank_regions.bnkr_reg_code%type,
      v_bnkr_agn_code                 tqc_bank_regions.bnkr_agn_code%type,
      v_err                       OUT      VARCHAR2
   );

   PROCEDURE update_branches (
      v_add_edit                  IN       VARCHAR2,
      v_brn_code                  IN       tqc_branches.brn_code%TYPE,
      v_brn_sht_desc              IN       tqc_branches.brn_sht_desc%TYPE,
      v_brn_reg_code              IN       tqc_branches.brn_reg_code%TYPE,
      v_brn_name                  IN       tqc_branches.brn_name%TYPE,
      v_brn_phy_addrs             IN       tqc_branches.brn_phy_addrs%TYPE,
      v_brn_email_addrs           IN       tqc_branches.brn_email_addrs%TYPE,
      v_brn_post_addrs            IN       tqc_branches.brn_post_addrs%TYPE,
      v_brn_twn_code              IN       tqc_branches.brn_twn_code%TYPE,
      v_brn_cou_code              IN       tqc_branches.brn_cou_code%TYPE,
      v_brn_contact               IN       tqc_branches.brn_contact%TYPE,
      v_brn_manager               IN       tqc_branches.brn_manager%TYPE,
      v_brn_tel                   IN       tqc_branches.brn_tel%TYPE,
      v_brn_fax                   IN       tqc_branches.brn_fax%TYPE,
      v_brn_agn_code              IN       tqc_branches.brn_agn_code%TYPE,
      v_brn_post_level            IN       tqc_branches.brn_post_level%TYPE,
      v_err                       OUT      VARCHAR2,
      v_brn_bra_mngr_seq_no       IN       tqc_branches.brn_bra_mngr_seq_no%TYPE,
      v_brn_agn_seq_no            IN       tqc_branches.brn_agn_seq_no%TYPE,
      v_brn_bns_code                       NUMBER,
      v_brn_mngr_allowed          IN       tqc_branches.brn_mngr_allowed%TYPE,
      v_brn_overide_comm_earned   IN       tqc_branches.brn_overide_comm_earned%TYPE,
      v_brn_pol_prefix            IN       tqc_branches.brn_pol_prefix%TYPE
            DEFAULT NULL,
      v_brn_pol_seq               IN       tqc_branches.brn_pol_seq%TYPE
            DEFAULT NULL,
      v_brn_prop_seq              IN       tqc_branches.brn_prop_seq%TYPE
            DEFAULT NULL,
      v_brn_ref              IN       tqc_branches.brn_ref%TYPE
            DEFAULT NULL
   );

   PROCEDURE delete_branch (
      v_brn_code         tqc_branches.brn_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   );

   PROCEDURE update_branch_agencies (
      v_add_edit                  IN       VARCHAR2,
      v_bra_code                  IN OUT   tqc_branch_agencies.bra_code%TYPE,
      v_bra_brn_code              IN       tqc_branch_agencies.bra_brn_code%TYPE,
      v_bra_sht_desc              IN       tqc_branch_agencies.bra_sht_desc%TYPE,
      v_bra_name                  IN       tqc_branch_agencies.bra_name%TYPE,
      v_bra_status                IN       tqc_branch_agencies.bra_status%TYPE,
      v_bra_manager               IN       tqc_branch_agencies.bra_manager%TYPE,
      v_bra_agn_code              IN       tqc_branch_agencies.bra_agn_code%TYPE,
      v_bra_post_level            IN       tqc_branch_agencies.bra_post_level%TYPE,
      v_bra_bru_mngr_seq_no       IN       tqc_branch_agencies.bra_bru_mngr_seq_no%TYPE,
      v_bra_agn_seq_no            IN       tqc_branch_agencies.bra_agn_seq_no%TYPE,
      v_bra_mngr_allowed          IN       tqc_branch_agencies.bra_mngr_allowed%TYPE,
      v_bra_overide_comm_earned   IN       tqc_branch_agencies.bra_overide_comm_earned%TYPE,
      v_bra_sht_descpref          IN       VARCHAR2,
      v_brncombuss                  IN       VARCHAR2,
      v_bra_pol_seq               IN       tqc_branch_agencies.bra_pol_seq%TYPE,
      v_bra_prop_seq              IN       tqc_branch_agencies.bra_prop_seq%TYPE,
      v_err                       OUT      VARCHAR2
   );

   PROCEDURE delete_branch_agency (
      v_bra_code         tqc_branch_agencies.bra_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   );

   PROCEDURE update_units (
      v_add_edit                   IN       VARCHAR2,
      v_bru_code                   IN OUT   tqc_branch_units.bru_code%TYPE,
      v_bru_brn_code               IN       tqc_branch_units.bru_brn_code%TYPE,
      v_bru_sht_desc               IN       tqc_branch_units.bru_sht_desc%TYPE,
      v_bru_name                   IN       tqc_branch_units.bru_name%TYPE,
      v_bru_supervisor             IN       tqc_branch_units.bru_supervisor%TYPE,
      v_bru_status                 IN       tqc_branch_units.bru_status%TYPE,
      v_bru_agn_code               IN       tqc_branch_units.bru_agn_code%TYPE,
      v_bru_bra_code               IN       tqc_branch_units.bru_bra_code%TYPE,
      v_bru_manager                IN       tqc_branch_units.bru_manager%TYPE,
      v_bru_post_level             IN       tqc_branch_units.bru_post_level%TYPE,
      v_bru_agn_seq_no             IN       tqc_branch_units.bru_agn_seq_no%TYPE,
      v_bru_mngr_allowed           IN       tqc_branch_units.bru_mngr_allowed%TYPE
            DEFAULT NULL,
      v_bru_overide_comm_earned    IN       tqc_branch_units.bru_overide_comm_earned%TYPE
            DEFAULT NULL,
      v_bru_unt_sht_desc_prefix    IN       tqc_branch_units.bru_unt_sht_desc_prefix%TYPE,
      v_bru_compt_ov_on_own_buss   IN       tqc_branch_units.bru_compt_ov_on_own_buss%TYPE,
      v_bru_pol_seq                IN       tqc_branch_units.bru_pol_seq%TYPE,
      v_bru_prop_seq               IN       tqc_branch_units.bru_prop_seq%TYPE,
      v_err                        OUT      VARCHAR2
   );

   PROCEDURE delete_branch_unit (
      v_bru_code         tqc_branch_units.bru_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   );

   PROCEDURE delete_region (
      v_reg_code         tqc_regions.reg_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   );

   PROCEDURE update_divisions (
      v_add_edit            IN       VARCHAR2,
      v_div_code            IN       tqc_divisions.div_code%TYPE,
      v_div_name            IN       tqc_divisions.div_name%TYPE,
      v_div_sht_desc        IN       tqc_divisions.div_sht_desc%TYPE,
      v_div_status          IN       tqc_divisions.div_division_status%TYPE,
      v_div_org_code        IN       tqc_divisions.div_org_code%TYPE,
      v_user                IN       VARCHAR2,
      v_div_order           IN       tqc_divisions.div_order%TYPE
            DEFAULT NULL,
      v_div_director        IN       tqc_divisions.div_director%TYPE
            DEFAULT NULL,
      v_div_asst_director   IN       tqc_divisions.div_asst_director%TYPE
            DEFAULT NULL,
      v_err                 OUT      VARCHAR2
   );

   PROCEDURE update_subdivisions (
      v_add_edit        IN       VARCHAR2,
      v_sdiv_code       IN       tqc_subdivisions.sdiv_code%TYPE,
      v_sdiv_name       IN       tqc_subdivisions.sdiv_name%TYPE,
      v_sdiv_sht_desc   IN       tqc_subdivisions.sdiv_sht_desc%TYPE,
      v_sdiv_div_code   IN       tqc_subdivisions.sdiv_div_code%TYPE,
      v_err             OUT      VARCHAR2
   );

   PROCEDURE update_brnch_divisions (
      v_add_edit        IN       VARCHAR2,
      v_bdiv_code       IN       tqc_brnch_divisions.bdiv_code%TYPE,
      v_bdiv_brn_code   IN       tqc_brnch_divisions.bdiv_brn_code%TYPE,
      v_bdiv_div_code   IN       tqc_brnch_divisions.bdiv_div_code%TYPE,
      v_bdiv_wef        IN       tqc_brnch_divisions.bdiv_wef%TYPE,
      v_bdiv_wet        IN       tqc_brnch_divisions.bdiv_wet%TYPE,
      v_err             OUT      VARCHAR2
   );

   PROCEDURE update_image (v_image BLOB, v_org_code NUMBER, v_col NUMBER);

   PROCEDURE update_branch_names (
      v_add_edit      IN       VARCHAR2,
      v_code                   tqc_branch_names.bns_code%TYPE,
      v_sht_desc               tqc_branch_names.bns_sht_desc%TYPE,
      v_name                   tqc_branch_names.bns_name%TYPE,
      v_phy_addrs              tqc_branch_names.bns_phy_addrs%TYPE,
      v_email_addrs            tqc_branch_names.bns_email_addrs%TYPE,
      v_post_addrs             tqc_branch_names.bns_post_addrs%TYPE,
      v_twn_code               tqc_branch_names.bns_twn_code%TYPE,
      v_cou_code               tqc_branch_names.bns_cou_code%TYPE,
      v_contact                tqc_branch_names.bns_contact%TYPE,
      v_manager                tqc_branch_names.bns_manager%TYPE,
      v_tel                    tqc_branch_names.bns_tel%TYPE,
      v_fax                    tqc_branch_names.bns_fax%TYPE,
      v_state                  tqc_branch_names.bns_state_code%TYPE,
      v_acctype                tqc_branch_names.bns_acc_type%TYPE,
      v_reg                    tqc_branch_names.bns_region%TYPE,
      v_post_code              tqc_branch_names.bns_post_code%TYPE,
      v_err           OUT      VARCHAR2
   );

   PROCEDURE delete_branch_name (
      v_bns_code         tqc_branch_names.bns_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   );

   PROCEDURE branch_contacts_proc (
      v_addedit          VARCHAR2,
      v_tbccode          NUMBER,
      v_tbcname          VARCHAR2,
      v_tbcdesignation   VARCHAR2,
      v_mobilephone      VARCHAR2,
      v_telephone        VARCHAR2,
      v_idnumber         VARCHAR2,
      v_physicaladd      VARCHAR2,
      v_emailaddress     VARCHAR2,
      v_brncode          NUMBER
   );
PROCEDURE delete_fa_agent (
      v_fa_agent_code         tqc_fa_agents.fa_agent_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   );  
PROCEDURE update_fa_agents (
      v_add_edit                  IN       VARCHAR2,      
      v_fa_agn_org_code           IN       tqc_fa_agents.fa_agn_org_code%TYPE,
      v_fa_agn_sht_desc           IN       tqc_fa_agents.fa_agn_sht_desc %TYPE,
      v_fa_team_code              IN       tqc_fa_agents.fa_team_code%TYPE, 
      v_fa_agn_name	              IN       tqc_fa_agents.fa_agn_name%TYPE,
      v_fa_agent_code	          IN       tqc_fa_agents.fa_agent_code%TYPE 
     
   );    
END tqc_web_organization_pkg; 
/