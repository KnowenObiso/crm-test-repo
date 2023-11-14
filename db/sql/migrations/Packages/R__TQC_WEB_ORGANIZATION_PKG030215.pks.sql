--
-- TQC_WEB_ORGANIZATION_PKG030215  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_WEB_ORGANIZATION_PKG030215 AS
PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL);
PROCEDURE UPDATE_ORGANIZATION(v_add_edit       IN VARCHAR2,
                             v_org_code        IN tqc_organizations.org_code%type,
                             v_org_sht_desc    IN tqc_organizations.org_sht_desc%type, 
                             v_org_name        IN tqc_organizations.org_name%type,
                             v_org_addrs       IN tqc_organizations.org_addrs%type, 
                             v_org_twn_code    IN tqc_organizations.org_twn_code%type, 
                             v_org_cou_code    IN tqc_organizations.org_cou_code%type, 
                             v_org_email_addrs IN tqc_organizations.org_email_addrs%type, 
                             v_org_phy_addrs   IN tqc_organizations.org_phy_addrs%type, 
                             v_org_cur_code    IN tqc_organizations.org_cur_code%type, 
                             v_org_zip         IN tqc_organizations.org_zip%type, 
                             v_org_fax         IN tqc_organizations.org_fax%type, 
                             v_org_tel1        IN tqc_organizations.org_tel1%type, 
                             v_org_tel2        IN tqc_organizations.org_tel2%type, 
                             v_org_rpt_logo    IN tqc_organizations.ORG_RPT_LOGO%type, 
                             v_org_motto       IN tqc_organizations.org_motto%type, 
                             v_org_pin_no      IN tqc_organizations.org_pin_no%type  , 
                             v_org_ed_code     IN tqc_organizations.org_ed_code%type, 
                             v_org_item_acc_code IN tqc_organizations.org_item_acc_code%type, 
                             v_org_other_name  IN tqc_organizations.org_other_name%type,  
                             v_org_type        IN tqc_organizations.org_type%type, 
                             v_org_web_brn_code IN tqc_organizations.org_web_brn_code%type, 
                             v_org_web_addrs   IN tqc_organizations.org_web_addrs%type , 
                             v_org_agn_code    IN tqc_organizations.org_agn_code%type, 
                             v_org_directors   IN tqc_organizations.org_directors%type, 
                             v_org_lang_code   IN tqc_organizations.org_lang_code%type, 
                             v_org_avatar      IN VARCHAR DEFAULT NULL,
                             v_org_sts_code    IN tqc_organizations.org_sts_code%type,
                             v_org_grp_logo    IN tqc_organizations.ORG_GRP_LOGO%type,
                             v_err             OUT VARCHAR2,
                             v_org_b64            tqc_organizations.ORG_LOGO_B64%TYPE DEFAULT NULL) ;
PROCEDURE UPDATE_REGIONS(v_add_edit       IN VARCHAR2,
                         v_reg_code       IN tqc_regions.reg_code%type, 
                         v_reg_org_code   IN tqc_regions.reg_org_code%type, 
                         v_reg_sht_desc   IN tqc_regions.reg_sht_desc%type,
                         v_reg_name       IN tqc_regions.reg_name%type,
                         v_reg_wef        IN tqc_regions.reg_wef%type, 
                         v_reg_wet        IN tqc_regions.reg_wet%type,
                         v_reg_agn_code   IN tqc_regions.reg_agn_code%type, 
                         v_reg_post_level IN tqc_regions.reg_post_level%type,
                         v_reg_mngr_allowed IN tqc_regions.reg_mngr_allowed%type,
                         v_reg_overide_comm_earned IN tqc_regions.reg_overide_comm_earned%type,
                         v_reg_brn_mngr_seq_no     IN tqc_regions.REG_BRN_MNGR_SEQ_NO%type , 
                         v_reg_agn_seq_no          IN tqc_regions.REG_AGN_SEQ_NO%type,
                         
                         v_err            OUT VARCHAR2);
                                                      
                         
PROCEDURE UPDATE_BRANCHES(v_add_edit        IN VARCHAR2,
                          v_brn_code        IN tqc_branches.brn_code%type,
                          v_brn_sht_desc    IN tqc_branches.brn_sht_desc%type,  
                          v_brn_reg_code    IN tqc_branches.brn_reg_code%type, 
                          v_brn_name        IN tqc_branches.brn_name%type, 
                          v_brn_phy_addrs   IN tqc_branches.brn_phy_addrs%type, 
                          v_brn_email_addrs IN tqc_branches.brn_email_addrs%type,  
                          v_brn_post_addrs  IN tqc_branches.brn_post_addrs%type, 
                          v_brn_twn_code    IN tqc_branches.brn_twn_code%type, 
                          v_brn_cou_code    IN tqc_branches.brn_cou_code%type, 
                          v_brn_contact     IN tqc_branches.brn_contact%type, 
                          v_brn_manager     IN tqc_branches.brn_manager%type, 
                          v_brn_tel         IN tqc_branches.brn_tel%type, 
                          v_brn_fax         IN tqc_branches.brn_fax%type, 
                          v_brn_agn_code    IN tqc_branches.brn_agn_code%type, 
                          v_brn_post_level  IN tqc_branches.brn_post_level%type,
                          v_err             OUT VARCHAR2,
                          v_brn_bra_mngr_seq_no IN tqc_branches.BRN_BRA_MNGR_SEQ_NO%type, 
                          v_brn_agn_seq_no      IN tqc_branches.BRN_AGN_SEQ_NO%type,
                           v_brn_bns_code        Number  ,
                          v_brn_mngr_allowed      IN tqc_branches.BRN_MNGR_ALLOWED%type,
                           v_brn_overide_comm_earned      IN tqc_branches.brn_overide_comm_earned%type,
                           v_BRN_POL_PREFIX     IN TQC_BRANCHES.BRN_POL_PREFIX%TYPE  DEFAULT NULL ,
                            v_brn_pol_seq     IN TQC_BRANCHES.BRN_POL_SEQ%TYPE  DEFAULT NULL ,
                             v_brn_prop_seq     IN TQC_BRANCHES.BRN_PROP_SEQ%TYPE  DEFAULT NULL 
                            );
                          
 PROCEDURE DELETE_BRANCH(v_brn_code tqc_branches.brn_code%type,v_err_msg OUT VARCHAR2) ;
                               
PROCEDURE UPDATE_BRANCH_AGENCIES(v_add_edit          IN VARCHAR2,
                                v_bra_code           IN OUT tqc_branch_agencies.bra_code%type,
                                v_bra_brn_code       IN tqc_branch_agencies.bra_brn_code%type,
                                v_bra_sht_desc       IN tqc_branch_agencies.bra_sht_desc%type,
                                v_bra_name           IN tqc_branch_agencies.bra_name%type,
                                v_bra_status         IN tqc_branch_agencies.bra_status%type,
                                v_bra_manager        IN tqc_branch_agencies.bra_manager%type,
                                v_bra_agn_code       IN tqc_branch_agencies.bra_agn_code%type,
                                v_bra_post_level     IN tqc_branch_agencies.bra_post_level%type,
                                v_bra_bru_mngr_seq_no  IN tqc_branch_agencies.BRA_BRU_MNGR_SEQ_NO%type, 
                                v_bra_agn_seq_no       IN tqc_branch_agencies.BRA_AGN_SEQ_NO%type, 
                                v_bra_mngr_allowed       IN tqc_branch_agencies.bra_mngr_allowed%type,
                                v_bra_overide_comm_earned       IN tqc_branch_agencies.bra_overide_comm_earned%type ,
                                 v_bra_sht_descpref       IN varchar2,
                                 brnComBuss in varchar2,
                                v_bra_pol_seq  IN tqc_branch_agencies.bra_pol_seq%type, 
                                v_bra_prop_seq       IN tqc_branch_agencies.bra_prop_seq%type,                                  
                                v_err            OUT VARCHAR2) ;
                                
PROCEDURE DELETE_BRANCH_AGENCY(v_bra_code tqc_branch_agencies.bra_code%type,v_err_msg OUT VARCHAR2);
PROCEDURE UPDATE_UNITS(v_add_edit       IN VARCHAR2,  
                       v_bru_code       IN OUT tqc_branch_units.bru_code%type,
                       v_bru_brn_code   IN tqc_branch_units.bru_brn_code%type,  
                       v_bru_sht_desc   IN tqc_branch_units.bru_sht_desc%type,
                       v_bru_name       IN tqc_branch_units.bru_name%type,
                       v_bru_supervisor IN tqc_branch_units.bru_supervisor%type,
                       v_bru_status     IN tqc_branch_units.bru_status%type,
                       v_bru_agn_code   IN tqc_branch_units.bru_agn_code%type,
                       v_bru_bra_code   IN tqc_branch_units.bru_bra_code%type,
                       v_bru_manager    IN tqc_branch_units.bru_manager%type,
                       v_bru_post_level IN tqc_branch_units.bru_post_level%type,
                       v_bru_agn_seq_no IN tqc_branch_units.BRU_AGN_SEQ_NO%TYPE,
                       v_bru_mngr_allowed IN tqc_branch_units.BRU_MNGR_ALLOWED%TYPE DEFAULT NULL,
                       v_bru_overide_comm_earned IN tqc_branch_units.BRU_OVERIDE_COMM_EARNED%TYPE DEFAULT NULL,
                        v_BRU_UNT_SHT_DESC_PREFIX IN tqc_branch_units.BRU_UNT_SHT_DESC_PREFIX%type,
                        v_BRU_COMPT_OV_ON_OWN_BUSS IN tqc_branch_units.BRU_COMPT_OV_ON_OWN_BUSS%type,
                         v_bru_pol_seq IN tqc_branch_units.bru_pol_seq%type,
                        v_bru_prop_seq IN tqc_branch_units.bru_prop_seq%type,
                         v_err            OUT VARCHAR2) ;
                           
PROCEDURE DELETE_BRANCH_UNIT(v_bru_code tqc_branch_units.bru_code%type,v_err_msg OUT VARCHAR2);  
PROCEDURE DELETE_REGION(v_reg_code tqc_regions.reg_code%type,v_err_msg OUT VARCHAR2);                                                                     
PROCEDURE UPDATE_DIVISIONS(v_add_edit       IN VARCHAR2,
                           v_div_code       IN tqc_divisions.div_code%type,
                           v_div_name       IN tqc_divisions.DIV_NAME%type,
                           v_div_sht_desc   IN tqc_divisions.DIV_SHT_DESC%type, 
                           v_div_status     IN tqc_divisions.DIV_DIVISION_STATUS%type,
                           v_div_org_code   IN tqc_divisions.DIV_ORG_CODE%TYPE,
                           v_user           IN VARCHAR2, 
                           v_div_order       IN  tqc_divisions.DIV_ORDER%TYPE DEFAULT NULL,
                           v_div_director       IN  tqc_divisions.DIV_DIRECTOR%TYPE DEFAULT NULL,
                           v_div_asst_director       IN  tqc_divisions.DIV_ASST_DIRECTOR%TYPE DEFAULT NULL,
                                   v_err            OUT VARCHAR2);
                           
PROCEDURE UPDATE_SUBDIVISIONS(v_add_edit       IN VARCHAR2,
                              v_sdiv_code      IN tqc_subdivisions.sdiv_code%type,
                              v_sdiv_name      IN tqc_subdivisions.sdiv_name%type,
                              v_sdiv_sht_desc  IN tqc_subdivisions.sdiv_sht_desc%type,
                              v_sdiv_div_code  IN tqc_subdivisions.sdiv_div_code%type,
                              v_err            OUT VARCHAR2
                              );     
                              
PROCEDURE UPDATE_BRNCH_DIVISIONS(v_add_edit       IN VARCHAR2, 
                                 v_bdiv_code      IN tqc_brnch_divisions.bdiv_code%type,
                                 v_bdiv_brn_code  IN tqc_brnch_divisions.bdiv_brn_code%type,
                                 v_bdiv_div_code  IN tqc_brnch_divisions.bdiv_div_code%type,
                                 v_bdiv_wef       IN tqc_brnch_divisions.bdiv_wef%type,
                                 v_bdiv_wet       IN tqc_brnch_divisions.bdiv_wet%type,              
                                 v_err            OUT VARCHAR2
                                 );   
PROCEDURE update_image(v_image blob, v_org_code NUMBER,v_col NUMBER);   
 PROCEDURE UPDATE_BRANCH_NAMES( v_add_edit       IN VARCHAR2,
                                v_code  TQC_BRANCH_NAMES.BNS_CODE%TYPE  ,
                                v_sht_desc  TQC_BRANCH_NAMES.BNS_SHT_DESC%TYPE  ,
                                v_name TQC_BRANCH_NAMES.BNS_NAME%TYPE  ,
                                v_phy_addrs  TQC_BRANCH_NAMES.BNS_PHY_ADDRS%TYPE  ,
                                v_email_addrs TQC_BRANCH_NAMES.BNS_EMAIL_ADDRS%TYPE  ,
                                v_post_addrs  TQC_BRANCH_NAMES.BNS_POST_ADDRS%TYPE  , 
                                v_twn_code TQC_BRANCH_NAMES.BNS_TWN_CODE%TYPE  , 
                                v_cou_code TQC_BRANCH_NAMES.BNS_COU_CODE%TYPE  ,
                                v_contact TQC_BRANCH_NAMES.BNS_CONTACT%TYPE  , 
                                v_manager TQC_BRANCH_NAMES.BNS_MANAGER%TYPE  , 
                                v_tel  TQC_BRANCH_NAMES.BNS_TEL%TYPE , 
                                v_fax TQC_BRANCH_NAMES.BNS_FAX%TYPE  , 
                                v_state TQC_BRANCH_NAMES.BNS_STATE_CODE%TYPE  ,                 
                                v_err            OUT VARCHAR2); 
        PROCEDURE DELETE_BRANCH_NAME(v_bns_code TQC_BRANCH_NAMES.bns_code%type,v_err_msg OUT VARCHAR2) ;                                            
                                                  
END TQC_WEB_ORGANIZATION_PKG030215; 
 
/