/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_SETUPS_CURSORBK280514  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_SETUPS_CURSORBK280514 AS
  /******************************************************************************
   NAME: TQC_SETUPS_CURSOR
   PURPOSE:
  
   REVISIONS:
   Ver Date Author Description
   --------- ---------- --------------- ------------------------------------
   1.0 5/6/2010 1. Created this package body.
  ******************************************************************************/
  FUNCTION myfunction(param1 IN NUMBER) RETURN NUMBER IS
  BEGIN
    RETURN param1;
  END;

  PROCEDURE myprocedure(param1 IN NUMBER) IS
    tmpvar NUMBER;
  BEGIN
    tmpvar := param1;
  END;

  PROCEDURE raise_error(v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL) IS
  BEGIN
    IF v_err_no IS NULL THEN
      raise_application_error(-20015, v_msg || ' - ' || SQLERRM(SQLCODE));
    ELSE
      raise_application_error(v_err_no, v_msg || ' - ' || SQLERRM(SQLCODE));
    END IF;
  END raise_error;

  PROCEDURE organizations(v_organizations_ref OUT organizations_ref,
                          v_org_code          tqc_organizations.org_code%TYPE) IS
  BEGIN
    OPEN v_organizations_ref FOR
      SELECT org_code,
             org_sht_desc,
             org_name,
             org_addrs,
             org_twn_code,
             org_cou_code,
             org_email_addrs,
             org_phy_addrs,
             org_cur_code,
             org_zip,
             org_fax,
             org_tel1,
             org_tel2,
             org_rpt_logo,
             org_motto,
             org_pin_no,
             org_ed_code,
             org_item_acc_code,
             org_other_name,
             org_type,
             org_web_brn_code,
             org_web_addrs,
             org_agn_code,
             org_directors,
             org_lang_code,
             org_avatar,
             currency_sysmbol(org_cur_code) cur_symbol,
             fetch_currency_name(org_cur_code) cur_desc,
             country_name(org_cou_code) cou_name,
             town_name(org_twn_code) twn_name,
             org_sts_code,
             state_name(org_sts_code) state_name,
             org_grp_logo,
             agn_name(org_agn_code) manager,ORG_VAT_NUMBER
        FROM tqc_organizations
       WHERE org_code = NVL(v_org_code, org_code);
  END organizations;

  PROCEDURE organization_details(v_organization_details_ref OUT organization_details_ref,
                                 v_org_code                 tqc_organizations.org_code%TYPE) IS
  BEGIN
    OPEN v_organization_details_ref FOR
      SELECT org_code,
             org_sht_desc,
             org_name,
             org_addrs,
             org_twn_code,
             org_cou_code,
             org_email_addrs,
             org_phy_addrs,
             org_cur_code,
             org_zip,
             org_fax,
             org_tel1,
             org_tel2,
             org_rpt_logo,
             org_motto,
             org_pin_no,
             org_ed_code,
             org_item_acc_code,
             org_other_name,
             org_type,
             org_web_brn_code,
             org_web_addrs,
             org_agn_code,
             org_directors,
             org_lang_code,
             org_avatar,
             currency_sysmbol(org_cur_code) cur_symbol,
             fetch_currency_name(org_cur_code) cur_desc,
             country_name(org_cou_code) cou_name,
             town_name(org_twn_code) twn_name,
             org_sts_code,
             state_name(org_sts_code) state_name,
             org_grp_logo
        FROM tqc_organizations
       WHERE org_code = NVL(v_org_code, org_code);
  END organization_details;

  PROCEDURE getcountries(v_countries_ref OUT countries_ref) IS
  BEGIN
    OPEN v_countries_ref FOR
     SELECT null,
             null,
             null cou_name,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null, 
             null
        FROM dual
       union
      SELECT cou_code,
             cou_sht_desc,
             cou_name,
             cou_base_curr,
             cur_desc cou_base_curr_desc,
             cou_nationality,
             cou_zip_code,
             cou_admin_reg_type,
             cou_admin_reg_mandatory,
             cou_schegen,
             cou_emb_code,
             cou_curr_serial,
             cou_mobile_prefix, 
             cou_client_number
        FROM tqc_countries, tqc_currencies
       WHERE cou_base_curr = cur_code(+)
       ORDER BY cou_name ASC;
  END getcountries;

  PROCEDURE getcountries(v_countries_ref OUT countries_ref,
                         v_schengen      IN VARCHAR2) IS
  BEGIN
    OPEN v_countries_ref FOR
      SELECT cou_code,
             cou_sht_desc,
             cou_name,
             cou_base_curr,
             cur_desc cou_base_curr_desc,
             cou_nationality,
             cou_zip_code,
             cou_admin_reg_type,
             cou_admin_reg_mandatory,
             cou_schegen,
             cou_emb_code,
             cou_curr_serial,
             cou_mobile_prefix, 
             cou_client_number
        FROM tqc_countries, tqc_currencies
       WHERE cou_base_curr= cur_code(+) 
         AND cou_schegen = NVL(v_schengen, 'N')
       ORDER BY cou_name ASC;
  END getcountries;

  PROCEDURE get_specific_country(v_cou_code      tqc_countries.cou_code%TYPE,
                                 v_countries_ref OUT countries_ref) IS
  BEGIN
    OPEN v_countries_ref FOR
      SELECT cou_code,
             cou_sht_desc,
             cou_name,
             cou_base_curr,
             cur_desc cou_base_curr_desc,
             cou_nationality,
             cou_zip_code,
             cou_admin_reg_type,
             cou_admin_reg_mandatory,
             cou_schegen,
             cou_emb_code,
             cou_curr_serial,
             cou_mobile_prefix, 
             cou_client_number
        FROM tqc_countries, tqc_currencies
       WHERE cou_base_curr = cur_code(+)
         AND cou_code = v_cou_code
       ORDER BY cou_name ASC;
  END get_specific_country;

  PROCEDURE get_countries_and_currency(v_countries_and_currency_ref OUT countries_and_currency_ref) IS
  BEGIN
    OPEN v_countries_and_currency_ref FOR
      SELECT cou_code,
             cou_sht_desc,
             cou_name,
             cou_base_curr,
             cou_nationality,
             cou_zip_code,
             cur_symbol,
             cur_desc,cou_admin_reg_type, cou_admin_reg_mandatory
        FROM tqc_countries, tqc_currencies
       WHERE cou_base_curr = cur_code(+);
  END get_countries_and_currency;


  PROCEDURE getstates(v_states_ref OUT states_ref,
                      v_cou_code   tqc_states.sts_cou_code%TYPE) IS
  BEGIN
    OPEN v_states_ref FOR
    SELECT null sts_code,null sts_cou_code,null sts_sht_desc,null sts_name
        FROM dual
        union
      SELECT UPPER(sts_code)  sts_code, UPPER(sts_cou_code)  sts_cou_code,UPPER(sts_sht_desc)   sts_sht_desc, UPPER(sts_name)  sts_name
        FROM tqc_states
       WHERE sts_cou_code = NVL(v_cou_code, sts_cou_code)
       ORDER BY sts_name ASC;
  END getstates;

  PROCEDURE get_admin_regions(v_admin_regions_ref OUT admin_region_ref,
                              v_adm_reg_cou_code  tqc_admin_regions.adm_reg_cou_code%TYPE) IS
  BEGIN
    OPEN v_admin_regions_ref FOR
      SELECT adm_reg_code, adm_reg_sht_desc, adm_reg_name, adm_reg_cou_code
        FROM tqc_admin_regions
       WHERE adm_reg_cou_code = NVL(v_adm_reg_cou_code, adm_reg_cou_code)
       ORDER BY adm_reg_name ASC;
  END get_admin_regions;

  PROCEDURE gettowns(v_towns_ref OUT towns_ref,
                     v_cou_code  tqc_towns.twn_cou_code%TYPE) IS
  BEGIN
    OPEN v_towns_ref FOR
      SELECT twn_code, twn_cou_code, UPPER(twn_sht_desc) twn_sht_desc, UPPER(twn_name) twn_name, twn_sts_code,NULL,NULL
        FROM tqc_towns
       WHERE twn_cou_code = NVL(v_cou_code, twn_cou_code);
  END gettowns;

  PROCEDURE get_specific_town(v_twn_code  tqc_towns.twn_code%TYPE,
                              v_towns_ref OUT towns_ref) IS
  BEGIN
    OPEN v_towns_ref FOR
      SELECT twn_code, twn_cou_code, twn_sht_desc, twn_name, twn_sts_code,NULL,NULL
        FROM tqc_towns
       WHERE twn_code = v_twn_code;
  END get_specific_town;

  PROCEDURE get_postal_codes_by_town(v_pst_twn_code            tqc_postal_codes.pst_twn_code%TYPE,
                                     v_postal_code_by_town_ref OUT postal_code_by_town_ref) IS
  BEGIN
    OPEN v_postal_code_by_town_ref FOR
      SELECT pst_code, pst_twn_code, pst_desc, pst_zip_code
        FROM tqc_postal_codes
       WHERE pst_twn_code = v_pst_twn_code;
  END get_postal_codes_by_town;

  PROCEDURE get_locations_by_town(v_loc_twn_code  tqc_locations.loc_twn_code%TYPE,
                                  v_locations_ref OUT locations_ref) IS
  BEGIN
    OPEN v_locations_ref FOR
      SELECT loc_code, loc_twn_code, loc_sht_desc, loc_name
        FROM tqc_locations
       WHERE loc_twn_code = v_loc_twn_code;
  END get_locations_by_town;

  PROCEDURE currencies(v_currencies_ref OUT currencies_ref) IS
  BEGIN
    OPEN v_currencies_ref FOR
      SELECT cur_code,
             cur_symbol,
             cur_desc,
             cur_rnd,
             cur_num_word,
             cur_decimal_word
        FROM tqc_currencies
       ORDER BY cur_desc ASC;
  END currencies;
  
   PROCEDURE get_currencies(v_currencies_ref OUT currencies_ref,v_cur_desc VARCHAR2) IS
  BEGIN
    OPEN v_currencies_ref FOR
      SELECT cur_code,
             cur_symbol,
             cur_desc,
             cur_rnd,
             cur_num_word,
             cur_decimal_word
        FROM tqc_currencies
        WHERE cur_symbol LIKE '%'||v_cur_desc||'%'
       ORDER BY cur_desc ASC;
  END get_currencies;

  PROCEDURE get_currencies_exclude_curr(v_cur_code       tqc_currencies.cur_code%TYPE,
                                        v_currencies_ref OUT currencies_ref) IS
  BEGIN
    OPEN v_currencies_ref FOR
      SELECT cur_code,
             cur_symbol,
             cur_desc,
             cur_rnd,
             cur_num_word,
             cur_decimal_word
        FROM tqc_currencies
       WHERE cur_code != v_cur_code
       ORDER BY cur_desc ASC;
  END get_currencies_exclude_curr;

  FUNCTION fetch_currency_name(v_cur_code NUMBER) RETURN VARCHAR2 IS
    v_rtval VARCHAR2(150);
  BEGIN
    IF v_cur_code IS NULL THEN
      RETURN NULL;
    ELSE
      SELECT cur_desc
        INTO v_rtval
        FROM tqc_currencies
       WHERE cur_code = v_cur_code;
    END IF;
  
    RETURN v_rtval;
  END fetch_currency_name;

  PROCEDURE get_currency_denominations(v_currency_denominations_ref OUT currency_denominations_ref) IS
  BEGIN
    OPEN v_currency_denominations_ref FOR
      SELECT cud_code, cud_cur_code, cud_value, cud_name, cud_wef
        FROM tqc_currency_denominations;
  END get_currency_denominations;

  PROCEDURE get_curr_denominations_by_code(v_cud_cur_code               tqc_currency_denominations.cud_cur_code%TYPE,
                                           v_currency_denominations_ref OUT currency_denominations_ref) IS
  BEGIN
    OPEN v_currency_denominations_ref FOR
      SELECT cud_code, cud_cur_code, cud_value, cud_name, cud_wef
        FROM tqc_currency_denominations
       WHERE cud_cur_code = v_cud_cur_code;
  END get_curr_denominations_by_code;

  PROCEDURE get_currency_rates(v_currency_rates_ref OUT currency_rates_ref) IS
  BEGIN
    OPEN v_currency_rates_ref FOR
      SELECT crt_code, crt_cur_code, crt_rate, crt_date, crt_base_cur_code
        FROM tqc_currency_rates;
  END get_currency_rates;

  PROCEDURE get_currency_rates_by_currency(v_crt_cur_code       tqc_currency_rates.crt_cur_code%TYPE,
                                           v_currency_rates_ref OUT currency_rates_by_curr_ref) IS
  BEGIN
    OPEN v_currency_rates_ref FOR
      SELECT crt_code,
             crt_cur_code,
             crt_rate,
             crt_date,
             crt_base_cur_code,
             cur_desc,CRT_WEF, CRT_WET
        FROM tqc_currency_rates, tqc_currencies
       WHERE crt_cur_code = cur_code
         AND crt_base_cur_code = v_crt_cur_code;
  END get_currency_rates_by_currency;

  PROCEDURE regions(v_regions_ref OUT regions_ref,
                    v_org_code    tqc_regions.reg_org_code%TYPE) IS
  BEGIN
    OPEN v_regions_ref FOR
      SELECT reg_code,
             reg_org_code,
             reg_sht_desc,
             reg_name,
             reg_wef,
             reg_wet,
             reg_agn_code,
             reg_post_level,
             reg_mngr_allowed,
             reg_overide_comm_earned,
             agn_name,
             reg_brn_mngr_seq_no,
             reg_agn_seq_no,
             agn_name(reg_agn_code) manager
        FROM tqc_regions, tqc_agencies
       WHERE reg_agn_code = agn_code(+)
         AND reg_org_code = v_org_code;
  END regions;

  PROCEDURE branches(v_branches_ref OUT branches_ref,
                     v_reg_code     tqc_branches.brn_reg_code%TYPE) IS
  BEGIN
    OPEN v_branches_ref FOR
      SELECT brn_code,
             brn_sht_desc,
             brn_reg_code,
             brn_name,
             brn_phy_addrs,
             brn_email_addrs,
             brn_post_addrs,
             brn_twn_code,
             brn_cou_code,
             brn_contact,
             brn_manager,
             brn_tel,
             brn_fax,
             brn_gen_pol_clm,
             brn_bns_code,
             brn_agn_code,
             brn_post_level,
             agn_name(brn_agn_code) manager
        FROM tqc_branches
       WHERE brn_reg_code = v_reg_code;
  END branches;

  PROCEDURE get_branches(v_branches_ref OUT get_branches_ref,
                         v_reg_code     tqc_branches.brn_reg_code%TYPE) IS
  BEGIN
    OPEN v_branches_ref FOR
      SELECT brn_code,
             brn_sht_desc,
             brn_reg_code,
             brn_name,
             brn_phy_addrs,
             brn_email_addrs,
             brn_post_addrs,
             brn_twn_code,
             brn_cou_code,
             brn_contact,
             brn_manager,
             brn_tel,
             brn_fax,
             brn_gen_pol_clm,
             brn_bns_code,
             brn_agn_code,
             brn_post_level,
             brn_mngr_allowed,
             brn_overide_comm_earned,
             brn_bra_mngr_seq_no,
             brn_agn_seq_no,
             brn_pol_seq, brn_prop_seq
        FROM tqc_branches
       WHERE brn_reg_code = v_reg_code;
  END get_branches;

  PROCEDURE get_orgbranches(v_branches_ref OUT get_branches_ref,
                            v_orgcode      NUMBER,
                            v_regcode      NUMBER DEFAULT NULL) IS
  BEGIN
    OPEN v_branches_ref FOR
      SELECT brn_code,
             brn_sht_desc,
             brn_reg_code,
             brn_name,
             brn_phy_addrs,
             brn_email_addrs,
             brn_post_addrs,
             brn_twn_code,
             brn_cou_code,
             brn_contact,
             brn_manager,
             brn_tel,
             brn_fax,
             brn_gen_pol_clm,
             brn_bns_code,
             brn_agn_code,
             brn_post_level,
             brn_mngr_allowed,
             brn_overide_comm_earned,
             brn_bra_mngr_seq_no,
             brn_agn_seq_no,
              brn_pol_seq, brn_prop_seq
        FROM tqc_branches, tqc_regions
       WHERE brn_reg_code = reg_code
         AND reg_org_code = v_orgcode
         AND reg_code = NVL(v_regcode, reg_code);
  END get_orgbranches;

  PROCEDURE get_specific_branch(v_brn_code     tqc_branches.brn_code%TYPE,
                                v_branches_ref OUT branches_ref) IS
  BEGIN
    OPEN v_branches_ref FOR
      SELECT brn_code,
             brn_sht_desc,
             brn_reg_code,
             brn_name,
             brn_phy_addrs,
             brn_email_addrs,
             brn_post_addrs,
             brn_twn_code,
             brn_cou_code,
             brn_contact,
             brn_manager,
             brn_tel,
             brn_fax,
             brn_gen_pol_clm,
             brn_bns_code,
             brn_agn_code,
             brn_post_level,
             agn_name(brn_agn_code) manager
        FROM tqc_branches
       WHERE brn_code = v_brn_code;
  END get_specific_branch;

  PROCEDURE find_certificate_alloc_branch(v_agn_code     IN NUMBER,
                                          v_branches_ref OUT branches_ref,
                                          v_error        OUT VARCHAR2) IS
    v_org_type VARCHAR2(10);
  BEGIN
    v_org_type := tqc_interfaces_pkg.get_org_type(37);
  
    IF v_agn_code = 0 OR NVL(v_org_type, 'INS') != 'INS' THEN
      OPEN v_branches_ref FOR
        SELECT brn_code,
               brn_sht_desc,
               brn_reg_code,
               brn_name,
               brn_phy_addrs,
               brn_email_addrs,
               brn_post_addrs,
               brn_twn_code,
               brn_cou_code,
               brn_contact,
               brn_manager,
               brn_tel,
               brn_fax,
               brn_gen_pol_clm,
               brn_bns_code,
               brn_agn_code,
               brn_post_level,
               agn_name(brn_agn_code) manager
          FROM tqc_branches
         WHERE brn_code != -2000;
    ELSE
      v_error := 'This option is allowed for certificates allocated for own consumption.';
    END IF;
  END find_certificate_alloc_branch;

  PROCEDURE branch_agencies(v_branch_agencies_ref OUT branch_agencies_ref,
                            v_brn_code            tqc_branch_agencies.bra_brn_code%TYPE) IS
  BEGIN
    OPEN v_branch_agencies_ref FOR
      SELECT bra_code,
             bra_brn_code,
             bra_sht_desc,
             bra_name,
             bra_status,
             agn_name bra_manager,
             bra_agn_code,
             bra_post_level
        FROM tqc_branch_agencies, tqc_agencies
       WHERE bra_brn_code = v_brn_code
         AND bra_agn_code = agn_code(+);
  END branch_agencies;

  PROCEDURE get_branch_agencies(v_branch_agencies_ref OUT get_branch_agencies_ref,
                                v_brn_code            tqc_branch_agencies.bra_brn_code%TYPE) IS
  BEGIN
    OPEN v_branch_agencies_ref FOR
      SELECT bra_code,
             bra_brn_code,
             bra_sht_desc,
             bra_name,
             bra_status,
             agn_name bra_manager,
             bra_agn_code,
             bra_post_level,
             bra_mngr_allowed,
             bra_overide_comm_earned,
             bra_bru_mngr_seq_no,
             bra_agn_seq_no,BRA_POL_SEQ,BRA_PROP_SEQ
        FROM tqc_branch_agencies, tqc_agencies
       WHERE bra_brn_code = v_brn_code
         AND bra_agn_code = agn_code(+);
  END get_branch_agencies;

  PROCEDURE branch_units(v_branch_units_ref OUT branch_units_ref,
                         v_brn_code         tqc_branch_units.bru_brn_code%TYPE) IS
  BEGIN
    OPEN v_branch_units_ref FOR
      SELECT bru_code,
             bru_brn_code,
             bru_sht_desc,
             bru_name,
             bru_supervisor,
             bru_status,
             bru_agn_code,
             bru_bra_code,
             agn_name bru_manager,
             bru_post_level
        FROM tqc_branch_units, tqc_agencies
       WHERE bru_brn_code = v_brn_code
         AND bru_agn_code = agn_code(+);
  END branch_units;

  PROCEDURE get_branch_units(v_branch_units_ref OUT get_branch_units_ref,
                             v_brn_code         tqc_branch_units.bru_brn_code%TYPE) IS
  BEGIN
    OPEN v_branch_units_ref FOR
      SELECT bru_code,
             bru_brn_code,
             bru_sht_desc,
             bru_name,
             bru_supervisor,
             bru_status,
             bru_agn_code,
             bru_bra_code,
             agn_name bru_manager,
             bru_post_level,
             bru_mngr_allowed,
             bru_overide_comm_earned,
             bru_agn_seq_no,
             bru_pol_seq, bru_prop_seq
        FROM tqc_branch_units, tqc_agencies
       WHERE bru_brn_code = v_brn_code
         AND bru_agn_code = agn_code(+);
  END get_branch_units;

  PROCEDURE get_branch_agency_units(v_branch_units_ref OUT get_branch_units_ref,
                                    v_bra_code         tqc_branch_units.bru_bra_code%TYPE) IS
  BEGIN
    OPEN v_branch_units_ref FOR
      SELECT bru_code,
             bru_brn_code,
             bru_sht_desc,
             bru_name,
             bru_supervisor,
             bru_status,
             bru_agn_code,
             bru_bra_code,
             agn_name bru_manager,
             bru_post_level,
             bru_mngr_allowed,
             bru_overide_comm_earned,
             bru_agn_seq_no,
             bru_pol_seq, bru_prop_seq
        FROM tqc_branch_units, tqc_agencies
       WHERE bru_bra_code = v_bra_code
         AND bru_agn_code = agn_code(+);
  END get_branch_agency_units;

  PROCEDURE branch_names(v_branch_names_ref OUT branch_names_ref) IS
  BEGIN
    OPEN v_branch_names_ref FOR
      SELECT bns_code,
             bns_sht_desc,
             bns_name,
             bns_phy_addrs,
             bns_email_addrs,
             bns_post_addrs,
             bns_twn_code,
             bns_cou_code,
             bns_contact,
             bns_manager,
             bns_tel,
             bns_fax
        FROM tqc_branch_names
       ORDER BY 1;
  END branch_names;

  PROCEDURE agencies(v_agencies_ref OUT agencies_ref) IS
  BEGIN
    OPEN v_agencies_ref FOR
      SELECT agn_code, agn_sht_desc, agn_name
        FROM tqc_agencies, tqc_account_types
       WHERE act_type_id IN ('IA', 'BM', 'BR', 'BE')
         AND act_code = agn_act_code;
    -- AND NVL(:TQC_BRANCH_AGENCIES.BRA_MNGR_ALLOWED,'Y') ='Y';
  END agencies;

  PROCEDURE managers(v_managers_ref OUT managers_ref) IS
  BEGIN
    OPEN v_managers_ref FOR
      SELECT agn_code, agn_sht_desc, agn_name, twn_name
        FROM tqc_agencies, tqc_towns
       WHERE agn_twn_code = twn_code(+)
         AND agn_act_code IN
             (SELECT act_code
                FROM tqc_account_types
               WHERE act_type_id IN ('IA', 'BM'))
         AND agn_status = 'ACTIVE'
      UNION ALL
      SELECT NULL, NULL, 'NONE', 'NONE' FROM DUAL ORDER BY 2 NULLS FIRST;
  END managers;

  PROCEDURE agencymanagers(v_managers_ref OUT managers_ref) IS
  BEGIN
    OPEN v_managers_ref FOR
      SELECT agn_code, agn_sht_desc, agn_name, twn_name
        FROM tqc_agencies, tqc_towns
       WHERE agn_twn_code = twn_code(+)
         AND agn_act_code IN
             (SELECT act_code
                FROM tqc_account_types
               WHERE act_type_id IN ('IA', 'BM'))
         AND agn_code NOT IN
             (SELECT bra_agn_code
                FROM tqc_branch_agencies
               WHERE bra_agn_code IS NOT NULL)
         AND agn_status = 'ACTIVE'
      UNION ALL
      SELECT NULL, NULL, 'NONE', 'NONE' FROM DUAL ORDER BY 2 NULLS FIRST;
  END agencymanagers;

  PROCEDURE unitmanagers(v_managers_ref OUT managers_ref) IS
  BEGIN
    OPEN v_managers_ref FOR
      SELECT agn_code, agn_sht_desc, agn_name, twn_name
        FROM tqc_agencies, tqc_towns
       WHERE agn_twn_code = twn_code(+)
         AND agn_act_code IN
             (SELECT act_code
                FROM tqc_account_types
               WHERE act_type_id IN ('IA', 'BM'))
         AND agn_code NOT IN
             (SELECT bru_agn_code
                FROM tqc_branch_units
               WHERE bru_agn_code IS NOT NULL)
         AND agn_status = 'ACTIVE'
      UNION ALL
      SELECT NULL, NULL, 'NONE', 'NONE' FROM DUAL ORDER BY 2 NULLS FIRST;
  END unitmanagers;

  PROCEDURE find_tqc_users(v_cursor OUT tqc_users_cursor) IS
  BEGIN
    OPEN v_cursor FOR
      SELECT DISTINCT usr_username clnt_sht_desc,
                      usr_name     clnt_other_names,
                      usr_code     clnt_code
        FROM tqc_users
       WHERE NVL(usr_acct_mgr, 'N') = 'Y';
  END;

  PROCEDURE get_agency_holding_company(v_agency_holding_company_ref OUT agency_holding_company_ref) IS
  BEGIN
    OPEN v_agency_holding_company_ref FOR
      SELECT ahc_code,
             ahc_name,
             ahc_postal_address,
             ahc_physical_address,
             ahc_telephone_number,
             ahc_mobile_number,
             ahc_contact_person
        FROM tqc_agency_holding_company;
  END get_agency_holding_company;

  PROCEDURE get_specific_holding_company(v_ahc_code                   tqc_agency_holding_company.ahc_code%TYPE,
                                         v_agency_holding_company_ref OUT agency_holding_company_ref) IS
  BEGIN
    OPEN v_agency_holding_company_ref FOR
      SELECT ahc_code,
             ahc_name,
             ahc_postal_address,
             ahc_physical_address,
             ahc_telephone_number,
             ahc_mobile_number,
             ahc_contact_person
        FROM tqc_agency_holding_company
       WHERE ahc_code = v_ahc_code;
  END get_specific_holding_company;

  PROCEDURE get_banks(v_banks_ref OUT banks_ref) IS
  BEGIN
    OPEN v_banks_ref FOR
      SELECT bnk_code,
             bnk_bank_name,
             bnk_remarks,
             bnk_sht_desc,
             bnk_ddr_code,
             dd_format_desc,
             bnk_forwarding_bnk_code,
             bnk_kba_code, 
             tq_crm.tqc_setups_cursor.bank_name(bnk_forwarding_bnk_code) fwd_bank_name,
             tqc_setups_cursor.ddreport_desc(bnk_ddr_code) dd_report_format,
             -- TQC_SETUPS_CURSOR.DDREPORT_DESC(BNK_DDR_CODE) DD_REPORT_FORMAT,
              BNK_EFT_SUPPORTED,BNK_CLASS_TYPE,bnk_accnt_digit_no,bnk_negotiated_bank, bnk_administration_charge,
              BNK_LOGO
        FROM tqc_banks
       ORDER BY bnk_bank_name;
  END get_banks;

  PROCEDURE get_bank_branches(v_bank_branches_ref OUT bank_branches_ref) IS
  BEGIN
    OPEN v_bank_branches_ref FOR
      SELECT bbr_code,
             bbr_bnk_code,
             bbr_branch_name,
             bbr_remarks,
             bbr_sht_desc,
             bbr_ref_code,
             bbr_eft_supported,
             bbr_dd_supported,
             bbr_date_created,
             bbr_created_by,
             bbr_physical_addrs,
             bbr_postal_addrs,
             bbr_kba_code
        FROM tqc_bank_branches;
  END get_bank_branches;

  PROCEDURE get_bank_branches_by_bank_code(v_bbr_bnk_code      tqc_bank_branches.bbr_bnk_code%TYPE,
                                           v_bank_branches_ref OUT bank_branches_ref) IS
  BEGIN
    OPEN v_bank_branches_ref FOR
      SELECT bbr_code,
             bbr_bnk_code,
             bbr_branch_name,
             bbr_remarks,
             bbr_sht_desc,
             bbr_ref_code,
             bbr_eft_supported,
             bbr_dd_supported,
             bbr_date_created,
             bbr_created_by,
             bbr_physical_addrs,
             bbr_postal_addrs,
             bbr_kba_code
        FROM tqc_bank_branches
       WHERE bbr_bnk_code = v_bbr_bnk_code;
  END get_bank_branches_by_bank_code;

  PROCEDURE get_agencies(v_agn_act_code     IN tqc_agencies.agn_act_code%TYPE,
                         v_all_agencies_ref OUT all_agencies_ref) IS
  BEGIN
    OPEN v_all_agencies_ref FOR
      SELECT agn_code,
             agn_act_code,
             agn_sht_desc,
             agn_name,
             agn_physical_address,
             agn_postal_address,
             agn_twn_code,
             agn_cou_code,
             agn_email_address,
             agn_web_address,
             agn_zip,
             agn_contact_person,
             agn_contact_title,
             agn_tel1,
             agn_tel2,
             agn_fax,
             agn_acc_no,
             agn_pin,
             agn_agent_commission,
             agn_credit_allowed,
             agn_agent_wht_tax,
             agn_print_dbnote,
             agn_status,
             agn_date_created,
             agn_created_by,
             agn_reg_code,
             agn_comm_reserve_rate,
             agn_annual_budget,
             agn_status_eff_date,
             agn_credit_period,
             agn_comm_stat_eff_dt,
             agn_comm_status_dt,
             agn_comm_allowed,
             agn_checked,
             agn_checked_by,
             agn_check_date,
             agn_comp_comm_arrears,
             agn_reinsurer,
             agn_brn_code,
             agn_town,
             agn_country,
             agn_status_desc,
             agn_id_no,
             agn_con_code,
             agn_agn_code,
             agn_sms_tel,
             agn_ahc_code,
             agn_sec_code,
             agn_agnc_class_code,
             agn_expiry_date,
             agn_license_no,
             agn_runoff,
             agn_licensed,
             agn_license_grace_pr,
             agn_old_acc_no,
             agn_status_remarks,
             brn_name,
             ahc_name,
             sec_name,
             agnc_class_desc
        FROM tqc_agencies,
             tqc_branches,
             tqc_agency_holding_company,
             tqc_sectors,
             tqc_agencies_classes
       WHERE agn_brn_code = brn_code
         AND agn_ahc_code = ahc_code
         AND agn_sec_code = sec_code
         AND agn_agnc_class_code = agnc_class_code
         AND agn_act_code = NVL(v_agn_act_code, agn_act_code);
  END get_agencies;

--PROCEDURE get_agency_info(v_agn_act_code    IN tqc_agencies.agn_act_code%TYPE,
--                            v_agency_info_ref OUT agency_info_ref) IS
--  BEGIN
--  --RAISE_ERROR('v_agn_act_code'||v_agn_act_code);
--    OPEN v_agency_info_ref FOR
--     SELECT a.agn_code,
--             a.agn_act_code,
--             a.agn_sht_desc,
--             a.agn_name,
--             a.agn_physical_address,
--             a.agn_postal_address,
--             a.agn_twn_code,
--             a.agn_cou_code,
--             a.agn_email_address,
--             a.agn_web_address,
--             a.agn_zip,
--             a.agn_contact_person,
--             a.agn_contact_title,
--             a.agn_tel1,
--             a.agn_tel2,
--             a.agn_fax,
--             a.agn_acc_no,
--             a.agn_pin,
--             a.agn_agent_commission,
--             a.agn_credit_allowed,
--             a.agn_agent_wht_tax,
--             a.agn_print_dbnote,
--             a.agn_status,
--             a.agn_date_created,
--             a.agn_created_by,
--             a.agn_reg_code,
--             a.agn_comm_reserve_rate,
--             a.agn_annual_budget,
--             a.agn_status_eff_date,
--             a.agn_credit_period,
--             a.agn_comm_stat_eff_dt,
--             a.agn_comm_status_dt,
--             a.agn_comm_allowed,
--             a.agn_checked,
--             a.agn_checked_by,
--             a.agn_check_date,
--             a.agn_comp_comm_arrears,
--             a.agn_reinsurer,
--             a.agn_brn_code,
--             a.agn_town,
--             a.agn_country,
--             a.agn_status_desc,
--             a.agn_id_no,
--             a.agn_con_code,
--             a.agn_agn_code,
--             a.agn_sms_tel,
--             a.agn_ahc_code,
--             a.agn_sec_code,
--             a.agn_agnc_class_code,
--             a.agn_expiry_date,
--             a.agn_license_no,
--             a.agn_runoff,
--             a.agn_licensed,
--             a.agn_license_grace_pr,
--             a.agn_old_acc_no,
--             a.agn_status_remarks,
--             bnk_code,
--             bnk_bank_name,
--             bbr_code,
--             bbr_branch_name,
--             a.agn_bank_acc_no,
--             a.agn_unique_prefix,
--             cou_admin_reg_type,
--             a.agn_state_code,
--             sts_name,
--             a.agn_crdt_rting,
--             clnt_name,
--             b.agn_name accountmanager,prom_transaction_date,
--             prom_transaction_type,prom_demo_promo_type,
--             brn_name,bra_name,brn_code,
--             prom_bra_unt_sht_desc_prefix,
--             prom_bru_unt_sht_desc_prefix,
--             prom_bru_agn_seq_no,prom_precontract_agn_seq,cou_zip_code,b.AGN_CODE,a.agn_credit_limit,BRU_CODE, BRU_NAME,
--             a.AGN_LOCAL_INTERNATIONAL,a.AGN_REGULATOR_NUMBER,a.AGN_AUTHORISED,
--             A.AGN_RORG_CODE, A.AGN_ORS_CODE,RORG_DESC,ORS_DESC,A.AGN_ALLOCATE_CERT,C.AGN_NAME,
--             a.AGN_BOUNCED_CHQ
--        FROM tqc_agencies a,tqc_agencies b,tqc_agencies c,
--             tqc_bank_branches,
--             tqc_banks,
--             tqc_countries,
--             tqc_states,
--             tqc_clients,LMS_AGENT_PROMOTION,TQC_BRANCHES,TQC_BRANCH_AGENCIES,TQC_BRANCH_UNITS,
--             TQC_RATING_ORGANIZATIONS,TQC_ORG_RATING_STARNDARDS
--       WHERE a.agn_bbr_code = bbr_code(+)
--         AND a.agn_cou_code = cou_code(+)
--         AND bbr_bnk_code = bnk_code(+)
--         AND a.agn_state_code = sts_code(+)
--         AND a.agn_clnt_code= clnt_code(+)
--         and c.AGN_CODE(+)=A.AGN_AGN_CODE
--         AND a.agn_account_manager=b.agn_code(+)
--         AND a.agn_act_code = NVL(v_agn_act_code, a.agn_act_code)
--         AND PROM_AGN_CODE(+)=A.AGN_CODE
--         AND BRN_CODE(+)=PROM_BRN_CODE
--         and BRU_CODE(+)=a.AGN_BRU_CODE
--         AND BRA_CODE(+)=PROM_BRA_CODE
--         AND RORG_CODE(+)=A.AGN_RORG_CODE
--         AND A.AGN_ORS_CODE=ORS_CODE(+)
--          -- AND BRU_BRN_CODE(+)=BRN_CODE
--       ORDER BY a.agn_name;
--  END get_agency_info;
PROCEDURE get_agency_info(v_agn_act_code    IN tqc_agencies.agn_act_code%TYPE,
                            v_agency_info_ref OUT agency_info_ref) IS
  BEGIN
  --RAISE_ERROR('v_agn_act_code'||v_agn_act_code);
    OPEN v_agency_info_ref FOR
     SELECT a.agn_code,
             a.agn_act_code,
             a.agn_sht_desc,
             a.agn_name,
             a.agn_physical_address,
             a.agn_postal_address,
             a.agn_twn_code,
             a.agn_cou_code,
             a.agn_email_address,
             a.agn_web_address,
             a.agn_zip,
             a.agn_contact_person,
             a.agn_contact_title,
             a.agn_tel1,
             a.agn_tel2,
             a.agn_fax,
             a.agn_acc_no,
             a.agn_pin,
             a.agn_agent_commission,
             a.agn_credit_allowed,
             a.agn_agent_wht_tax,
             a.agn_print_dbnote,
             a.agn_status,
             a.agn_date_created,
             a.agn_created_by,
             a.agn_reg_code,
             a.agn_comm_reserve_rate,
             a.agn_annual_budget,
             a.agn_status_eff_date,
             a.agn_credit_period,
             a.agn_comm_stat_eff_dt,
             a.agn_comm_status_dt,
             a.agn_comm_allowed,
             a.agn_checked,
             a.agn_checked_by,
             a.agn_check_date,
             a.agn_comp_comm_arrears,
             a.agn_reinsurer,
             a.agn_brn_code,
             a.agn_town,
             a.agn_country,
             a.agn_status_desc,
             a.agn_id_no,
             a.agn_con_code,
             a.agn_agn_code,
             a.agn_sms_tel,
             a.agn_ahc_code,
             a.agn_sec_code,
             a.agn_agnc_class_code,
             a.agn_expiry_date,
             a.agn_license_no,
             a.agn_runoff,
             a.agn_licensed,
             a.agn_license_grace_pr,
             a.agn_old_acc_no,
             a.agn_status_remarks,
             bnk_code,
             bnk_bank_name,
             bbr_code,
             bbr_branch_name,
             a.agn_bank_acc_no,
             a.agn_unique_prefix,
             cou_admin_reg_type,
             a.agn_state_code,
             sts_name,
             a.agn_crdt_rting,
             clnt_name,
             b.agn_name accountmanager,prom_transaction_date,
             prom_transaction_type,prom_demo_promo_type,
             brn_name,bra_name,brn_code,
             prom_bra_unt_sht_desc_prefix,
             prom_bru_unt_sht_desc_prefix,
             prom_bru_agn_seq_no,prom_precontract_agn_seq,cou_zip_code,b.AGN_CODE,a.agn_credit_limit,BRU_CODE, BRU_NAME,
             a.AGN_LOCAL_INTERNATIONAL,a.AGN_REGULATOR_NUMBER,a.AGN_AUTHORISED,
             A.AGN_RORG_CODE, A.AGN_ORS_CODE,RORG_DESC,ORS_DESC,A.AGN_ALLOCATE_CERT,C.AGN_NAME,
             a.AGN_BOUNCED_CHQ,A.AGN_BPN_CODE,BPN_NAME,
             a.AGN_AGENT_TYPE, a.AGN_GROUP
        FROM tqc_agencies a,tqc_agencies b,tqc_agencies c,
             tqc_bank_branches,
             tqc_banks,
             tqc_countries,
             tqc_states,
             tqc_clients,LMS_AGENT_PROMOTION,TQC_BRANCHES,TQC_BRANCH_AGENCIES,TQC_BRANCH_UNITS,
             TQC_RATING_ORGANIZATIONS,TQC_ORG_RATING_STARNDARDS,TQC_BUSINESS_PERSONS
       WHERE a.agn_bbr_code = bbr_code(+)
         AND a.agn_cou_code = cou_code(+)
         AND bbr_bnk_code = bnk_code(+)
         AND a.agn_state_code = sts_code(+)
         AND a.agn_clnt_code= clnt_code(+)
         and c.AGN_CODE(+)=A.AGN_AGN_CODE
         AND a.agn_account_manager=b.agn_code(+)
         AND a.agn_act_code = NVL(v_agn_act_code, a.agn_act_code)
         AND PROM_AGN_CODE(+)=A.AGN_CODE
         AND BRN_CODE(+)=PROM_BRN_CODE
         and BRU_CODE(+)=a.AGN_BRU_CODE
         AND BRA_CODE(+)=PROM_BRA_CODE
         AND RORG_CODE(+)=A.AGN_RORG_CODE
         AND A.AGN_ORS_CODE=ORS_CODE(+)
         AND BPN_CODE(+)=A.AGN_BPN_CODE
          -- AND BRU_BRN_CODE(+)=BRN_CODE
       ORDER BY a.agn_name;
  END get_agency_info;
  PROCEDURE get_agency_infobynames(v_agn_act_code    IN tqc_agencies.agn_act_code%TYPE,
                                   v_agn_name        tqc_agencies.agn_name%TYPE,
                                   v_agency_info_ref OUT agency_info_by_name_ref,
                                   v_agn_physical_address IN VARCHAR2,
                                   v_agn_email_address IN VARCHAR2,
                                   v_agn_pin IN VARCHAR2,
                                   v_agn_tel1 IN VARCHAR2) IS
  BEGIN
  --RAISE_ERROR('v_agn_physical_address'||v_agn_physical_address||'v_agn_email_address'||v_agn_email_address);
  --v_agn_email_address||'v_agn_pin'||v_agn_pin||'v_agn_tel1'||v_agn_tel1);
    OPEN v_agency_info_ref FOR
      SELECT DISTINCT agn_code,
             agn_act_code,
             agn_sht_desc,
             agn_name,
             agn_physical_address,
             agn_postal_address,
             agn_twn_code,
             agn_cou_code,
             agn_email_address,
             agn_web_address,
             agn_zip,
             agn_contact_person,
             agn_contact_title,
             agn_tel1,
             agn_tel2,
             agn_fax,
             agn_acc_no,
             agn_pin,
             agn_agent_commission,
             agn_credit_allowed,
             agn_agent_wht_tax,
             agn_print_dbnote,
             agn_status,
             agn_date_created,
             agn_created_by,
             agn_reg_code,
             agn_comm_reserve_rate,
             agn_annual_budget,
             agn_status_eff_date,
             agn_credit_period,
             agn_comm_stat_eff_dt,
             agn_comm_status_dt,
             agn_comm_allowed,
             agn_checked,
             agn_checked_by,
             agn_check_date,
             agn_comp_comm_arrears,
             agn_reinsurer,
             agn_brn_code,
             agn_town,
             agn_country,
             agn_status_desc,
             agn_id_no,
             agn_con_code,
             agn_agn_code,
             agn_sms_tel,
             agn_ahc_code,
             agn_sec_code,
             agn_agnc_class_code,
             agn_expiry_date,
             agn_license_no,
             agn_runoff,
             agn_licensed,
             agn_license_grace_pr,
             agn_old_acc_no,
             agn_status_remarks,
             brn_name
        FROM tqc_agencies, tqc_branches
       WHERE (agn_act_code = NVL(v_agn_act_code, agn_act_code)) 
        AND    agn_brn_code = brn_code 
        AND  (((agn_name = NVL(v_agn_name, 'HAKUNA')) OR
             (agn_name LIKE '%' || v_agn_name || '%') OR
             INSTR(v_agn_name, agn_name) != 0)OR
            ((agn_physical_address=NVL(v_agn_physical_address,'HAKUNA'))OR
              (agn_physical_address LIKE '%'||v_agn_physical_address||'%')OR
              INSTR(v_agn_physical_address,agn_physical_address)!=0)OR
              ((agn_email_address=NVL(v_agn_email_address,'HAKUNA'))OR
              (agn_email_address LIKE '%'||v_agn_email_address||'%')OR
              INSTR(v_agn_email_address,agn_email_address)!=0)OR
              ((agn_pin=NVL(v_agn_pin,'HAKUNA'))OR
              (agn_pin LIKE '%'||v_agn_pin||'%')OR
              INSTR(agn_pin,v_agn_pin)!=0)OR
              ((agn_tel1=NVL(v_agn_tel1,'HAKUNA'))OR
              (agn_tel1 LIKE '%'||v_agn_tel1||'%')OR
              INSTR(agn_tel1,v_agn_tel1)!=0))
         ORDER BY agn_name,agn_physical_address,agn_email_address,agn_pin,agn_tel1;
  END get_agency_infobynames;

  PROCEDURE get_agency_classes(v_agency_classes_ref OUT agency_classes_ref) IS
  BEGIN
    OPEN v_agency_classes_ref FOR
      SELECT agnc_class_code, agnc_class_desc FROM tqc_agencies_classes;
  END get_agency_classes;

  PROCEDURE get_specific_agency_classes(v_agnc_class_code    tqc_agencies_classes.agnc_class_code%TYPE,
                                        v_agency_classes_ref OUT agency_classes_ref) IS
  BEGIN
    OPEN v_agency_classes_ref FOR
      SELECT agnc_class_code, agnc_class_desc
        FROM tqc_agencies_classes
       WHERE agnc_class_code = v_agnc_class_code;
  END get_specific_agency_classes;

  PROCEDURE get_specific_agency_classes(v_act_code               IN NUMBER DEFAULT NULL,
                                        tqc_account_types_values OUT tqc_account_types_cursor) IS
  BEGIN
    OPEN tqc_account_types_values FOR
      SELECT act_code,
             act_account_type,
             act_type_sht_desc,
             act_wthtx_rate,
             act_type_id,
             act_comm_reserve_rate,
             act_max_adv_amt,
             act_max_adv_rpymt_prd,
             act_rcpts_include_comm,
             act_overide_rate,
             act_id_serial_format,
             act_vat_rate,
             act_format,
             act_odl_code,
             odl_ranking || ' ' || odl_desc ranking,
             act_no_gen_code
        FROM tqc_account_types, tqc_org_division_levels
       WHERE act_code = NVL(v_act_code, act_code)
         AND act_odl_code = odl_code(+)
       ORDER BY act_code, act_account_type;
  END;

  PROCEDURE search_account_types(v_act_code               IN NUMBER DEFAULT NULL,
                                 tqc_account_types_values OUT tqc_account_types_cursor) IS
  BEGIN
    OPEN tqc_account_types_values FOR
      SELECT act_code,
             act_account_type,
             act_type_sht_desc,
             act_wthtx_rate,
             act_type_id,
             act_comm_reserve_rate,
             act_max_adv_amt,
             act_max_adv_rpymt_prd,
             act_rcpts_include_comm,
             act_overide_rate,
             act_id_serial_format,
             act_vat_rate,
             act_format,
             act_odl_code,
             odl_ranking || ' ' || odl_desc ranking,
             act_no_gen_code
        FROM tqc_account_types, tqc_org_division_levels
       WHERE act_code = NVL(v_act_code, act_code)
         AND act_odl_code = odl_code(+)
       ORDER BY act_code, act_account_type;
  END;

  PROCEDURE find_branches_code(branches OUT branch_record_cursor) IS
  BEGIN
    OPEN branches FOR
      SELECT brn_name, TO_CHAR(brn_code)
        FROM tqc_branches, tqc_regions, tqc_organizations
       WHERE brn_reg_code = reg_code
         AND reg_org_code = org_code
         AND org_code = 2;
  END;

  PROCEDURE get_sectors(v_sectors_ref OUT sectors_ref) IS
  BEGIN
    OPEN v_sectors_ref FOR
      SELECT sec_code, sec_sht_desc, sec_name FROM tqc_sectors;
  END get_sectors;

  PROCEDURE get_specific_sectors(v_sec_code    tqc_sectors.sec_code%TYPE,
                                 v_sectors_ref OUT sectors_ref) IS
  BEGIN
    OPEN v_sectors_ref FOR
      SELECT sec_code, sec_sht_desc, sec_name
        FROM tqc_sectors
       WHERE sec_code = v_sec_code;
  END get_specific_sectors;

  PROCEDURE get_service_provider_types(v_service_provider_types_ref OUT service_provider_types_ref) IS
  BEGIN
    OPEN v_service_provider_types_ref FOR
      SELECT spt_code,
             spt_sht_desc,
             spt_name,
             spt_status,
             spt_whtx_rate,
             spt_vat_rate
        FROM tqc_service_provider_types
       ORDER BY spt_name;
  END get_service_provider_types;

  PROCEDURE get_service_provider_types_act(v_spt_code                   IN NUMBER,
                                           v_service_provider_types_ref OUT serv_provider_types_act_ref) IS
  BEGIN
  --RAISE_ERROR('v_spt_code'||v_spt_code);
    OPEN v_service_provider_types_ref FOR
      SELECT spta_code, spta_spt_code, spta_sht_desc,spta_desc,A.MSGT_MSG,B.MSGT_MSG emailtemplate,
      SPTA_SEND_MSG_DEFAULT, SPTA_SEND_EMAIL_DEFAULT,A.MSGT_CODE,B.MSGT_CODE
        FROM tqc_serv_prv_type_actvts ,tqc_msg_templates A,tqc_msg_templates B
       WHERE spta_spt_code = v_spt_code
       AND A.MSGT_CODE(+)=SPTA_SMS_MSGT_CODE
       AND B.MSGT_CODE(+)=SPTA_EMAIL_MSGT_CODE;   
  END get_service_provider_types_act;

  PROCEDURE get_service_provider_types_act(v_service_provider_types_ref OUT serv_provider_types_act_ref) IS
  BEGIN
    OPEN v_service_provider_types_ref FOR
      SELECT spta_code, spta_spt_code, spta_sht_desc, spta_desc,NULL,null,NULL,NULL,NULL,NULL
        FROM tqc_serv_prv_type_actvts;
  END get_service_provider_types_act;

--  PROCEDURE get_service_providers(v_spr_spt_code          tqc_service_providers.spr_spt_code%TYPE,
--                                  v_service_providers_ref OUT service_providers_ref) IS
--  BEGIN
--    OPEN v_service_providers_ref FOR
--      SELECT spr_code,
--             spr_sht_desc,
--             spr_name,
--             spr_physical_address,
--             spr_postal_address,
--             spr_twn_code,
--             spr_cou_code,
--             spr_spt_code,
--             spr_phone,
--             spr_fax,
--             spr_email,
--             spr_title,
--             spr_zip,
--             spr_wef,
--             spr_wet,
--             spr_contact,
--             spr_aims_code,
--             spr_bbr_code,
--             spr_bank_acc_no,
--             spr_created_by,
--             spr_date_created,
--             spr_status_remarks,
--             spr_status,
--             spr_pin_number,
--             spr_trs_occupation,
--             spr_proff_body,
--             spr_pin,
--             spr_doc_phone,
--             spr_doc_email,
--             country_name(spr_cou_code) country_name,
--             town_name(spr_twn_code) town_name,
--             bnk_code,
--             bnk_bank_name,
--             bbr_branch_name,
--             spr_inhouse,spr_sms_number,spr_invoice_number,SPR_CLNT_CODE,CLNT_NAME||' '||CLNT_OTHER_NAMES
--        FROM tqc_service_providers, tqc_bank_branches, tqc_banks,tqc_clients
--       WHERE spr_spt_code = NVL(v_spr_spt_code, spr_spt_code)
--         AND spr_bbr_code = bbr_code(+)
--         AND bbr_bnk_code = bnk_code(+)
--         AND SPR_CLNT_CODE=CLNT_CODE(+)
--       ORDER BY spr_name;
--  END get_service_providers;

PROCEDURE get_service_providers(v_spr_spt_code          tqc_service_providers.spr_spt_code%TYPE,
                                  v_service_providers_ref OUT service_providers_ref) IS
  BEGIN
    OPEN v_service_providers_ref FOR
      SELECT spr_code,
             spr_sht_desc,
             spr_name,
             spr_physical_address,
             spr_postal_address,
             spr_twn_code,
             spr_cou_code,
             spr_spt_code,
             spr_phone,
             spr_fax,
             spr_email,
             spr_title,
             spr_zip,
             spr_wef,
             spr_wet,
             spr_contact,
             spr_aims_code,
             spr_bbr_code,
             spr_bank_acc_no,
             spr_created_by,
             spr_date_created,
             spr_status_remarks,
             spr_status,
             spr_pin_number,
             spr_trs_occupation,
             spr_proff_body,
             spr_pin,
             spr_doc_phone,
             spr_doc_email,
             country_name(spr_cou_code) country_name,
             town_name(spr_twn_code) town_name,
             bnk_code,
             bnk_bank_name,
             bbr_branch_name,
             spr_inhouse,spr_sms_number,spr_invoice_number,SPR_CLNT_CODE,CLNT_NAME||' '||CLNT_OTHER_NAMES,
             SPR_BPN_CODE,BPN_NAME
        FROM tqc_service_providers, tqc_bank_branches, tqc_banks,tqc_clients,TQC_BUSINESS_PERSONS
       WHERE spr_spt_code = NVL(v_spr_spt_code, spr_spt_code)
         AND spr_bbr_code = bbr_code(+)
         AND bbr_bnk_code = bnk_code(+)
         AND SPR_CLNT_CODE=CLNT_CODE(+)
         AND BPN_CODE(+)=SPR_BPN_CODE
       ORDER BY spr_name;
  END get_service_providers;

  PROCEDURE get_service_prov_activities(v_service_prov_activities_ref OUT service_prov_activities_ref) IS
  BEGIN
    OPEN v_service_prov_activities_ref FOR
      SELECT spa_code,
             spa_spt_code,
             spa_spt_sht_desc,
             spa_spr_code,
             spa_spr_sht_desc,
             spt_main_act,
             spa_desc
        FROM tqc_serv_prv_activities;
  END get_service_prov_activities;

  PROCEDURE get_activities_by_provider(v_spa_spr_code               tqc_serv_prv_activities.spa_spr_code%TYPE,
                                       v_activities_by_provider_ref OUT activities_by_provider_ref) IS
  BEGIN
    OPEN v_activities_by_provider_ref FOR
      SELECT spa_code,
             spa_spt_code,
             spa_spt_sht_desc,
             spa_spr_code,
             spa_spr_sht_desc,
             spt_main_act,
             spta_desc spa_desc,
             spt_name,
             spr_name
        FROM tqc_serv_prv_activities,
             tqc_service_provider_types,
             tqc_service_providers,
             tqc_serv_prv_type_actvts
       WHERE spa_spt_code = spt_code
         AND spa_spr_code = spr_code
         AND spa_spta_code = spta_code(+)
         AND spa_spr_code = v_spa_spr_code;
  END get_activities_by_provider;

  PROCEDURE get_service_prov_systems(v_service_prov_systems_ref OUT service_prov_systems_ref) IS
  BEGIN
    OPEN v_service_prov_systems_ref FOR
      SELECT sps_code,
             sps_spr_code,
             sps_sys_code,
             sps_wef,
             sps_wet,
             sps_created_by
        FROM tqc_service_provider_systems;
  END get_service_prov_systems;

  PROCEDURE get_prov_unassigned_systems(v_provider_code            tqc_service_providers.spr_code%TYPE,
                                        v_service_prov_systems_ref OUT systems_ref) IS
  BEGIN
    OPEN v_service_prov_systems_ref FOR
      SELECT sys_code,
             sys_sht_desc,
             sys_name,
             sys_main_form_name,
             sys_active,
             sys_dbase_username,
             sys_dbase_password,
             sys_dbase_string,
             sys_path,
             sys_org_code,
             sys_agn_main_frm_name,
             sys_kba_code,
             sys_signature_path
        FROM tqc_systems
       WHERE sys_active <> 'N'
         AND NOT EXISTS (SELECT sps_code,
                     sps_spr_code,
                     sps_sys_code,
                     sps_wef,
                     sps_wet,
                     sps_created_by
                FROM tqc_service_provider_systems
               WHERE tqc_systems.sys_code =
                     tqc_service_provider_systems.sps_sys_code
                 AND sps_spr_code = v_provider_code);
  END get_prov_unassigned_systems;

  PROCEDURE get_prov_assigned_systems(v_provider_code            tqc_service_providers.spr_code%TYPE,
                                      v_service_prov_systems_ref OUT systems_ref) IS
  BEGIN
    OPEN v_service_prov_systems_ref FOR
      SELECT sys_code,
             sys_sht_desc,
             sys_name,
             sys_main_form_name,
             sys_active,
             sys_dbase_username,
             sys_dbase_password,
             sys_dbase_string,
             sys_path,
             sys_org_code,
             sys_agn_main_frm_name,
             sys_kba_code,
             sys_signature_path
        FROM tqc_systems
       WHERE sys_active <> 'N'
         AND EXISTS (SELECT sps_code,
                     sps_spr_code,
                     sps_sys_code,
                     sps_wef,
                     sps_wet,
                     sps_created_by
                FROM tqc_service_provider_systems
               WHERE tqc_systems.sys_code =
                     tqc_service_provider_systems.sps_sys_code
                 AND sps_spr_code = v_provider_code);
  END get_prov_assigned_systems;

  PROCEDURE get_systems(v_systems_ref OUT systems_ref) IS
  BEGIN
    OPEN v_systems_ref FOR
      SELECT sys_code,
             sys_sht_desc,
             sys_name,
             sys_main_form_name,
             sys_active,
             sys_dbase_username,
             sys_dbase_password,
             sys_dbase_string,
             sys_path,
             sys_org_code,
             sys_agn_main_frm_name,
             sys_kba_code,
             sys_signature_path
        FROM tqc_systems;
  END get_systems;

  PROCEDURE get_parameters(v_parameters_ref OUT parameters_ref) IS
  BEGIN
    OPEN v_parameters_ref FOR
      SELECT param_code, param_name, param_value, param_status, param_desc
        FROM tqc_parameters;
  END get_parameters;

  PROCEDURE get_gis_branches(branches OUT branch_record_cursor) IS
  BEGIN
    OPEN branches FOR
      SELECT brn_code, brn_name
        FROM tqc_branches, tqc_regions
       WHERE reg_org_code = (SELECT sys_org_code
                               FROM tqc_systems
                              WHERE sys_sht_desc = 'GIS')
         AND reg_code = brn_reg_code;
  END;

  PROCEDURE get_payment_modes(v_payment_modes_ref OUT payment_modes_ref) IS
  BEGIN
    OPEN v_payment_modes_ref FOR
      SELECT pmod_code,
             pmod_sht_desc,
             pmod_desc,
             pmod_naration,
             pmod_default
        FROM tqc_payment_modes;
  END get_payment_modes;

  PROCEDURE get_client_titles(v_client_titles_ref OUT client_titles_ref,v_clt_sht_desc IN VARCHAR2) IS
  BEGIN
  
    OPEN v_client_titles_ref FOR
      SELECT clt_code, clt_sht_desc, clnt_desc FROM tqc_client_titles
      WHERE UPPER (clt_sht_desc) LIKE '%'||UPPER (v_clt_sht_desc)||'%'
      AND rownum < 20;
  
  END get_client_titles;
   PROCEDURE get_client_titles(v_client_titles_ref OUT client_titles_ref) IS
  BEGIN
    OPEN v_client_titles_ref FOR
      SELECT clt_code, clt_sht_desc, clnt_desc FROM tqc_client_titles;
  END get_client_titles;
 PROCEDURE get_allclient_titles(v_client_titles_ref OUT client_titles_ref) IS
  BEGIN
   OPEN v_client_titles_ref FOR
      SELECT clt_code, clt_sht_desc, clnt_desc FROM tqc_client_titles;
     
   END get_allclient_titles;
  PROCEDURE find_business_intro_users(v_cursor OUT tqc_users_cursor) IS
  BEGIN
    OPEN v_cursor FOR
      SELECT usr_username usr_id, usr_name, usr_code usr_no
        FROM tqc_users
       WHERE usr_username NOT IN
             (SELECT DISTINCT intro_usr_id
                FROM gin_introducer
               WHERE intro_usr_id IS NOT NULL);
  END;

  FUNCTION getsubarealevels(v_sprsacode tqc_authorization_levels.tqal_sprsa_code%TYPE)
    RETURN auth_levels_ref IS
    v_cursor auth_levels_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqal_code,
             tqal_sprsa_code,
             tqal_level_id,
             tqal_srls_code,
             srls_name,
             sprsa_sub_area
        FROM tqc_authorization_levels,
             tqc_sys_roles,
             tqc_sys_process_sub_areas
       WHERE tqal_srls_code = srls_code
         AND tqal_sprsa_code = sprsa_code
         AND sprsa_code = v_sprsacode;
  
    RETURN v_cursor;
  END;

  FUNCTION getsystemsubareas(v_syscode NUMBER) RETURN system_subareas_ref IS
    v_cursor system_subareas_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT sprsa_code,
             sprsa_sprca_code,
             sprsa_sprc_code,
             sprsa_sub_area,
             sprsa_type,
             sprsa_sht_desc,
             sprsa_default_usr_code
        FROM tqc_sys_process_sub_areas, tqc_sys_processes
       WHERE sprsa_sprc_code = sprc_code
         AND sprsa_type = 'A'
         AND sprc_sys_code = v_syscode;
  
    RETURN v_cursor;
  END;

  FUNCTION getsystemroles(v_syscode NUMBER) RETURN systemroles_ref IS
    v_cursor systemroles_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT srls_code,
             srls_sys_code,
             srls_name,
             srls_crt_date,
             srls_sht_desc,
             srls_status
        FROM tqc_sys_roles
       WHERE srls_sys_code = v_syscode;
  
    RETURN v_cursor;
  END;

  PROCEDURE authlevelsproc(v_addedit   VARCHAR2,
                           v_tqualcode tqc_authorization_levels.tqal_code%TYPE,
                           v_sprsacode tqc_authorization_levels.tqal_sprsa_code%TYPE,
                           v_levelid   tqc_authorization_levels.tqal_level_id%TYPE,
                           v_srlscode  tqc_authorization_levels.tqal_srls_code%TYPE) IS
    v_level NUMBER;
  BEGIN
    IF v_addedit = 'A' THEN
      SELECT NVL(MAX(tqal_level_id), 0) + 1
        INTO v_level
        FROM tqc_authorization_levels
       WHERE tqal_srls_code = v_srlscode
         AND tqal_sprsa_code = v_sprsacode;
    
      INSERT INTO tqc_authorization_levels
        (tqal_code, tqal_sprsa_code, tqal_level_id, tqal_srls_code)
      VALUES
        (tqal_code_seq.NEXTVAL, v_sprsacode, v_level, v_srlscode);
    ELSIF v_addedit = 'E' THEN
      UPDATE tqc_authorization_levels
         SET tqal_srls_code = v_srlscode
       WHERE tqal_code = v_tqualcode;
    ELSE
      DELETE tqc_authorization_levels WHERE tqal_code = v_tqualcode;
    END IF;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      raise_error('Error Manipulating Authorization level Record' ||
                  SQLERRM(SQLCODE));
  END authlevelsproc;

  FUNCTION getbankbrancheslov RETURN bankbrancheslov_ref IS
    v_cursor bankbrancheslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT ALL tqc_banks.bnk_sht_desc,
                 tqc_banks.bnk_code,
                 tqc_banks.bnk_bank_name,
                 tqc_bank_branches.bbr_branch_name,
                 tqc_bank_branches.bbr_code,
                 fms_bnk_accts.bct_no
        FROM tqc_bank_branches, tqc_banks, fms_bnk_accts
       WHERE tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code
         AND bct_bbr_code = bbr_code
         AND NVL(bbr_dd_supported, 'N') = 'Y'
       ORDER BY 1, 2;
  
    RETURN v_cursor;
  END;

  FUNCTION bankbrancheslov RETURN bankbrancheslov_ref IS
    v_cursor bankbrancheslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT DISTINCT tqc_banks.bnk_sht_desc,
                      tqc_banks.bnk_code,
                      tqc_banks.bnk_bank_name,
                      tqc_bank_branches.bbr_branch_name,
                      tqc_bank_branches.bbr_code,
                      NULL
        FROM tqc_bank_branches, tqc_banks
       WHERE tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code
       ORDER BY 3;
  
    RETURN v_cursor;
  END;

  FUNCTION bbranchesbank(v_bnkcode tqc_banks.bnk_code%TYPE)
    RETURN bankbrancheslov_ref IS
    v_cursor bankbrancheslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT DISTINCT tqc_banks.bnk_sht_desc,
                      tqc_banks.bnk_code,
                      tqc_banks.bnk_bank_name,
                      tqc_bank_branches.bbr_branch_name,
                      tqc_bank_branches.bbr_code,
                      NULL
        FROM tqc_bank_branches, tqc_banks
       WHERE tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code
         AND bnk_code = v_bnkcode
       ORDER BY 3;
  
    RETURN v_cursor;
  END;

  FUNCTION getholidays(v_year NUMBER) RETURN holidays_ref IS
    v_cursor holidays_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT hld_date, hld_desc
        FROM tqc_holidays
       WHERE TO_NUMBER(TO_CHAR(hld_date, 'RRRR')) =
             NVL(v_year, TO_NUMBER(TO_CHAR(SYSDATE, 'RRRR')))
       ORDER BY hld_date;
  
    RETURN v_cursor;
  END;

  FUNCTION getyearslov RETURN yearslov_ref IS
    v_cursor yearslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT DISTINCT TO_NUMBER(TO_CHAR(hld_date, 'RRRR')) h_year
        FROM tqc_holidays
       ORDER BY 1;
  
    RETURN v_cursor;
  END;

  FUNCTION getdirectdebitnonreceipt RETURN directdebit_ref IS
    v_cursor directdebit_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT dd_code,
             dd_ref_no,
             dd_sent_yn,
             dd_acnt_no,
             dd_book_date,
             dd_bbr_code,
             dd_status,
             dd_receipted,
             dd_value_date,
             dd_raised_by,
             dd_date,
             dd_bnk_code,
             dd_auth_by,
             dd_auth_date,
             dd_authorized,
             (SELECT bnk_bank_name || ' - ' || bbr_branch_name
                FROM tqc_bank_branches, tqc_banks
               WHERE bbr_bnk_code = bnk_code
                 AND bbr_code = dd_bbr_code) bank_branch
        FROM tqc_direct_debit
       WHERE NVL(dd_receipted, 'N') = 'N'
       ORDER BY dd_book_date DESC;
  
    RETURN v_cursor;
  END;

  FUNCTION getdirectdebitheader(v_dd_code NUMBER)
    RETURN directdebitheader_ref IS
    v_cursor directdebitheader_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT ddh_code,
             ddh_dd_code,
             ddh_clnt_code,
             ddh_clnt_bbr_code,
             ddh_bbr_bnk_code,
             ddh_clnt_sht_desc,
             ddh_clnt_name,
             ddh_clnt_bank_acc_no,
             ddh_bbr_branch_name,
             ddh_bbr_sht_desc,
             ddh_bbr_ref_code,
             ddh_tot_amt,
             ddh_status,
             ddh_receipted,
             ddh_fail_date,
             ddh_fail_updated_by,
             ddh_fail_update_date,
             ddh_fail_remarks,
             ddh_relaunch_date,
             ddh_relaunch_stop_date,
             ddh_relaunched_by,
             ddh_relaunch_stoped_by,
             ddh_initial_book_date,
             ddh_prev_ddh_code,
             ddh_acc_holder,
             (SELECT bnk_bank_name || ' - ' || bbr_branch_name
                FROM tqc_banks, tqc_bank_branches
               WHERE bnk_code = bbr_bnk_code
                 AND bbr_code = ddh_clnt_bbr_code) bank_branch
        FROM tqc_direct_debit_header
       WHERE ddh_dd_code = v_dd_code
       ORDER BY ddh_clnt_name;
  
    RETURN v_cursor;
  END;

  FUNCTION getdirectdebitdetail(v_ddh_code NUMBER)
    RETURN directdebitdetail_ref IS
    v_cursor directdebitdetail_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT ddd_code,
             ddd_ddh_code,
             ddd_dd_code,
             ddd_sys_code,
             ddd_pol_code,
             ddd_pol_prp_code,
             ddd_pol_proposal_no,
             ddd_pol_policy_no,
             ddd_other_identifier,
             ddd_amount,
             ddd_remarks,
             ddd_start_dt,
             ddd_stop_date,
             ddd_status,
             ddd_fail_date,
             ddd_receipted,
             ddd_fail_updated_by,
             ddd_fail_update_date,
             ddd_ppr_code,
             ddd_pol_type,
             ddd_receipted_by,
             ddd_receipt_no,
             ddd_receipt_date,
             ddd_receipted_date,
             ddd_fail_remarks,
             ddd_relaunch_date,
             ddd_relaunch_stop_date,
             ddd_relaunched_by,
             ddd_relaunch_stoped_by
        FROM tqc_direct_debit_detail
       WHERE ddd_ddh_code = v_ddh_code
       ORDER BY ddd_pol_policy_no;
  
    RETURN v_cursor;
  END;

  FUNCTION getdirectdebitauthorised RETURN directdebit_ref IS
    v_cursor directdebit_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT dd_code,
             dd_ref_no,
             dd_sent_yn,
             dd_acnt_no,
             dd_book_date,
             dd_bbr_code,
             dd_status,
             dd_receipted,
             dd_value_date,
             dd_raised_by,
             dd_date,
             dd_bnk_code,
             dd_auth_by,
             dd_auth_date,
             dd_authorized,
             (SELECT bnk_bank_name || ' - ' || bbr_branch_name
                FROM tqc_bank_branches, tqc_banks
               WHERE bbr_bnk_code = bnk_code
                 AND bbr_code = dd_bbr_code) bank_branch
        FROM tqc_direct_debit
       WHERE NVL(dd_authorized, 'N') = 'Y'
         AND NVL(dd_status, 'D') = 'A'
       ORDER BY dd_date DESC;
  
    RETURN v_cursor;
  END;

  FUNCTION getallglaccounts RETURN glaccounts_ref IS
    v_cursor glaccounts_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT acc_number, acc_name FROM fms_accounts ORDER BY acc_number;
  
    RETURN v_cursor;
  END;

  --SELECT  BRN_NAME, BRN_CODE,BRN_SHT_DESC FROM tqc_BRANCHES where brn_code !=-2000
  FUNCTION get_em_template(v_sys_code IN NUMBER) RETURN email_templates_ref IS
    v_cursor email_templates_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT msgt_code,
             msgt_sht_desc,
             msgt_msg,
             msgt_sys_code,
             msgt_sys_module,
             msgt_type
        FROM tqc_msg_templates
       WHERE msgt_sys_code = NVL(v_sys_code, msgt_sys_code);
  
    RETURN(v_cursor);
  END;

  FUNCTION get_em_template_by_type(v_sys_code IN NUMBER,
                                   v_msg_type VARCHAR2)
    RETURN email_templates_ref IS
    v_cursor email_templates_ref;
  BEGIN
    --RAISE_ERROR(v_sys_code||';'||v_msg_type);
    OPEN v_cursor FOR
      SELECT msgt_code,
             msgt_sht_desc,
             msgt_msg,
             msgt_sys_code,
             msgt_sys_module,
             msgt_type
        FROM tqc_msg_templates
       WHERE msgt_sys_code = NVL(v_sys_code, 0)
         AND msgt_type = v_msg_type;
  
    RETURN(v_cursor);
  END;

  FUNCTION getprospects RETURN prospects_ref IS
    v_cursor prospects_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT prs_code,
             prs_surname,
             prs_physical_addrs,
             prs_postal_addrs,
             prs_other_names,
             prs_tel_no,
             prs_mobile_no,
             prs_email_addrs,
             prs_id_reg_no,
             prs_dob,
             prs_pin,
             prs_twn_code,
             prs_cou_code,
             prs_postal_code,
             country_name(prs_cou_code) country_name,
             town_name(prs_twn_code) town_name,PST_DESC
        FROM tqc_prospects,tqc_postal_codes
        WHERE PRS_POSTAL_CODE=PST_CODE(+)
       ORDER BY prs_surname;
  
    RETURN v_cursor;
  END;

  FUNCTION getallsystems RETURN all_systems_ref IS
    v_cursor all_systems_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT sys_code,
             sys_sht_desc,
             sys_name,
             sys_main_form_name,
             sys_active,
             sys_dbase_username,
             sys_dbase_password,
             sys_dbase_string,
             sys_path,
             sys_org_code,
             sys_agn_main_frm_name,
             sys_kba_code,
             sys_signature_path,
             sys_template
        FROM tqc_systems
       WHERE sys_active <> 'N'
         AND sys_code <> 30
         AND sys_code <> 26
       ORDER BY sys_name;
  
    RETURN v_cursor;
  END;

  FUNCTION getorgdivlevelstype(v_sys_code IN NUMBER)
    RETURN orgdivlevelstype_ref IS
    v_cursor orgdivlevelstype_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT dlt_code,
             dlt_sys_code,
             dlt_desc,
             dlt_act_code,
             acc_type_name(dlt_act_code) acc_type_name,
             DLT_CODE_VAL
        FROM tqc_org_division_levels_type
       WHERE dlt_sys_code = v_sys_code
       ORDER BY dlt_desc;
  
    RETURN v_cursor;
  END;

  FUNCTION getorgdivisionlevels(v_dlt_code IN VARCHAR2)
    RETURN orgdivisionlevels_ref IS
    v_cursor orgdivisionlevels_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT odl_code,
             odl_dlt_code,
             odl_desc,
             odl_ranking,
             NVL(odl_type, 'O') odl_type
        FROM tqc_org_division_levels
       WHERE odl_dlt_code = v_dlt_code
       ORDER BY odl_ranking;
  
    RETURN v_cursor;
  END;

  FUNCTION getorgdivisionlevelsbyranking(v_dlt_code IN VARCHAR2,
                                         v_ranking  IN tqc_org_division_levels.odl_ranking%TYPE)
    RETURN orgdivisionlevels_ref IS
    v_cursor orgdivisionlevels_ref;
  BEGIN
    -- 9999 means that v_rank is null
    IF v_ranking = 9999 THEN
      OPEN v_cursor FOR
        SELECT odl_code,
               odl_dlt_code,
               odl_desc,
               odl_ranking,
               NVL(odl_type, 'O') odl_type
          FROM tqc_org_division_levels
         WHERE odl_dlt_code = v_dlt_code;
    
      RETURN v_cursor;
    ELSE
      OPEN v_cursor FOR
        SELECT odl_code,
               odl_dlt_code,
               odl_desc,
               odl_ranking,
               NVL(odl_type, 'O') odl_type
          FROM tqc_org_division_levels
         WHERE odl_dlt_code = v_dlt_code
           AND odl_ranking > v_ranking
         ORDER BY odl_ranking;
    
      RETURN v_cursor;
    END IF;
  END;

  FUNCTION getorgsubdivisionsbysys(v_sys_code IN NUMBER)
    RETURN orgsubdivisions_ref IS
    v_cursor orgsubdivisions_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT osd_code,
             osd_parent_osd_code,
             osd_dlt_code,
             osd_odl_code,
             osd_name,
             osd_wef,
             osd_div_head_agn_code,
             osd_sys_code,
             dlt_desc,
             odl_desc,
             agn_name(osd_div_head_agn_code) agent_name,
             odl_ranking,
             NVL(odl_type, 'O') odl_type,
             osd_brn_code,
             osd_reg_code,
             osd_post_level,
             osd_manager_allwd,
             osd_over_comm_earn,
             osd_id,
             osd_parent_id
        FROM tqc_org_subdivisions,
             tqc_org_division_levels_type,
             tqc_org_division_levels
       WHERE osd_sys_code = v_sys_code
         AND osd_dlt_code = dlt_code
         AND osd_odl_code = odl_code
         AND osd_parent_osd_code IS NULL
       ORDER BY osd_name;
  
    RETURN v_cursor;
  END;

  FUNCTION getlowestorgsubdivsbysys(v_sys_code IN NUMBER, v_ac_type NUMBER)
    RETURN orgsubdivisions_ref IS
    v_cursor orgsubdivisions_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT osd_code,
             osd_parent_osd_code,
             osd_dlt_code,
             osd_odl_code,
             osd_name,
             osd_wef,
             osd_div_head_agn_code,
             osd_sys_code,
             dlt_desc,
             odl_desc,
             agn_name(osd_div_head_agn_code) agent_name,
             odl_ranking,
             NVL(odl_type, 'O') odl_type,
             osd_brn_code,
             osd_reg_code,
             osd_post_level,
             osd_manager_allwd,
             osd_over_comm_earn,
             osd_id,
             osd_parent_id
        FROM tqc_org_subdivisions,
             tqc_org_division_levels_type,
             tqc_org_division_levels
       WHERE osd_sys_code = v_sys_code
         AND osd_dlt_code = dlt_code
         AND osd_odl_code = odl_code
         AND osd_code NOT IN
             (SELECT DISTINCT osd_parent_osd_code
                FROM tqc_org_subdivisions
               WHERE osd_parent_osd_code IS NOT NULL)
         AND dlt_act_code = v_ac_type
       ORDER BY osd_name;
  
    RETURN v_cursor;
  END;

  FUNCTION getorgsubdivisionsbydlt(v_dlt_code IN VARCHAR2)
    RETURN orgsubdivisions_ref IS
    v_cursor orgsubdivisions_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT osd_code,
             osd_parent_osd_code,
             osd_dlt_code,
             osd_odl_code,
             osd_name,
             osd_wef,
             osd_div_head_agn_code,
             osd_sys_code,
             dlt_desc,
             odl_desc,
             agn_name(osd_div_head_agn_code) agent_name,
             odl_ranking,
             NVL(odl_type, 'O') odl_type,
             osd_brn_code,
             osd_reg_code,
             osd_post_level,
             osd_manager_allwd,
             osd_over_comm_earn,
             osd_id,
             osd_parent_id
        FROM tqc_org_subdivisions,
             tqc_org_division_levels_type,
             tqc_org_division_levels
       WHERE osd_dlt_code = v_dlt_code
         AND osd_dlt_code = dlt_code
         AND osd_odl_code = odl_code
         AND osd_parent_osd_code IS NULL
       ORDER BY osd_name;
  
    RETURN v_cursor;
  END;

  FUNCTION getorgsubdivisionsbysubdiv(v_osd_code IN VARCHAR2)
    RETURN orgsubdivisions_ref IS
    v_cursor orgsubdivisions_ref;
  BEGIN
    IF v_osd_code IS NULL THEN
      OPEN v_cursor FOR
        SELECT osd_code,
               osd_parent_osd_code,
               osd_dlt_code,
               osd_odl_code,
               osd_name,
               osd_wef,
               osd_div_head_agn_code,
               osd_sys_code,
               dlt_desc,
               odl_desc,
               agn_name(osd_div_head_agn_code) agent_name,
               odl_ranking,
               NVL(odl_type, 'O') odl_type,
               osd_brn_code,
               osd_reg_code,
               osd_post_level,
               osd_manager_allwd,
               osd_over_comm_earn,
               osd_id,
               osd_parent_id
          FROM tqc_org_subdivisions,
               tqc_org_division_levels_type,
               tqc_org_division_levels
         WHERE osd_parent_osd_code IS NULL
           AND osd_dlt_code = dlt_code
           AND osd_odl_code = odl_code
         ORDER BY osd_name;
    
      RETURN v_cursor;
    ELSE
      OPEN v_cursor FOR
        SELECT osd_code,
               osd_parent_osd_code,
               osd_dlt_code,
               osd_odl_code,
               osd_name,
               osd_wef,
               osd_div_head_agn_code,
               osd_sys_code,
               dlt_desc,
               odl_desc,
               agn_name(osd_div_head_agn_code) agent_name,
               odl_ranking,
               NVL(odl_type, 'O') odl_type,
               osd_brn_code,
               osd_reg_code,
               osd_post_level,
               osd_manager_allwd,
               osd_over_comm_earn,
               osd_id,
               osd_parent_id
          FROM tqc_org_subdivisions,
               tqc_org_division_levels_type,
               tqc_org_division_levels
         WHERE osd_parent_osd_code = v_osd_code
           AND osd_dlt_code = dlt_code
           AND osd_odl_code = odl_code
         ORDER BY osd_name;
    
      RETURN v_cursor;
    END IF;
  END;

  FUNCTION getallagencieslov RETURN agencieslov_ref IS
    v_cursor agencieslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT agn_code, agn_sht_desc, agn_name
        FROM tqc_agencies
       ORDER BY agn_name;
  
    RETURN v_cursor;
  END;

  FUNCTION getallmarketers(v_actcode NUMBER DEFAULT NULL)
    RETURN agencieslov_ref IS
    v_cursor agencieslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT agn_code, agn_sht_desc, agn_name
        FROM tqc_agencies
       WHERE agn_act_code = NVL(v_actcode, 10)
      UNION
      SELECT NULL, 'NONE', 'NONE' FROM DUAL ORDER BY 1 NULLS FIRST, 3;
  
    RETURN v_cursor;
  END;

  FUNCTION getspecificmarketers(v_agn_code IN NUMBER) RETURN agencieslov_ref IS
    v_cursor agencieslov_ref;
  BEGIN
    IF v_agn_code IS NULL THEN
      OPEN v_cursor FOR
        SELECT agn_code, agn_sht_desc, agn_name
          FROM tqc_agencies
         WHERE agn_act_code = 10
         ORDER BY agn_name;
    
      RETURN v_cursor;
    ELSE
      OPEN v_cursor FOR
        SELECT agn_code, agn_sht_desc, agn_name
          FROM tqc_agencies
         WHERE agn_act_code = 10
           AND agn_code != v_agn_code
         ORDER BY agn_name;
    
      RETURN v_cursor;
    END IF;
  END;

  FUNCTION getagencyactivities(v_agn_code IN NUMBER)
    RETURN agencyactivities_ref IS
    v_cursor agencyactivities_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT aac_code,
             aac_acty_code,
             aac_wef,
             aac_estimate_wet,
             aac_actual_wet,
             aac_remarks,
             aac_agn_code,
             aac_clnt_code,
             aac_spr_code,
             aac_sys_code,
             aac_mktr_agn_code,
             activity_desc(aac_acty_code) acty_desc,
             clnt_name(aac_clnt_code) client_name,
             agn_name(aac_agn_code) agency_name,
             agn_name(aac_mktr_agn_code) mktr_agency_name,
             provider_name(aac_spr_code) provider_name,
             system_name(aac_sys_code) system_name
        FROM tqc_agency_activities
       WHERE aac_agn_code = v_agn_code
       ORDER BY aac_code;
  
    RETURN v_cursor;
  END;

  FUNCTION getagencyactivitiesdetails(v_agn_code IN NUMBER)
    RETURN agencyactivitiesdetails_ref IS
    v_cursor agencyactivitiesdetails_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT aac_code,
             aac_acty_code,
             aac_wef,
             aac_estimate_wet,
             aac_actual_wet,
             aac_remarks,
             aac_agn_code,
             aac_sys_code,
             aac_by_code,
             aac_by_type,
             activity_desc(aac_acty_code) acty_desc,
             getagency_name(aac_by_code, aac_by_type) agency_name,
             system_name(aac_sys_code) system_name,
             account_type_name(aac_by_type) acc_type_name,
             aac_reasons
        FROM tqc_agency_activities
       WHERE aac_agn_code = v_agn_code
       ORDER BY aac_code;
  
    RETURN v_cursor;
  END;

  FUNCTION clnt_name(v_client_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT clnt_name
      INTO v_ret_val
      FROM tqc_clients
     WHERE clnt_code = v_client_code;
  
    RETURN v_ret_val;
  END clnt_name;

  FUNCTION agn_name(v_agn_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT agn_name
      INTO v_ret_val
      FROM tqc_agencies
     WHERE agn_code = v_agn_code;
  
    RETURN v_ret_val;
  END agn_name;

  FUNCTION activity_desc(v_acty_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT acty_desc
      INTO v_ret_val
      FROM tqc_activity_types
     WHERE acty_code = v_acty_code;
  
    RETURN v_ret_val;
  END activity_desc;

  FUNCTION provider_name(v_spr_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT spr_name
      INTO v_ret_val
      FROM tqc_service_providers
     WHERE spr_code = v_spr_code;
  
    RETURN v_ret_val;
  END provider_name;

  FUNCTION system_name(v_sys_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT sys_name
      INTO v_ret_val
      FROM tqc_systems
     WHERE sys_code = v_sys_code;
  
    RETURN v_ret_val;
  END system_name;

  FUNCTION getallactivitytypes RETURN activitytypes_ref IS
    v_cursor activitytypes_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT acty_code, acty_sys_code, acty_desc
        FROM tqc_activity_types
       ORDER BY acty_desc;
  
    RETURN v_cursor;
  END;

  FUNCTION getallactivitytypesbysys(v_sys_code NUMBER)
    RETURN activitytypes_ref IS
    v_cursor activitytypes_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT acty_code, acty_sys_code, acty_desc
        FROM tqc_activity_types
       WHERE acty_sys_code = v_sys_code
       ORDER BY acty_desc;
  
    RETURN v_cursor;
  END;

  FUNCTION getactivitytypesbysystem(v_sys_code NUMBER)
    RETURN activitytypes_ref IS
    v_cursor activitytypes_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT acty_code, acty_sys_code, acty_desc
        FROM tqc_activity_types
       WHERE acty_sys_code = v_sys_code
       ORDER BY acty_desc;
  
    RETURN v_cursor;
  END;

  FUNCTION getallclientslov RETURN clientslov_ref IS
    v_cursor clientslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT clnt_code, clnt_sht_desc, clnt_name
        FROM tqc_clients
       ORDER BY clnt_name;
  
    RETURN v_cursor;
  END;

  FUNCTION getallserviceprovlov RETURN serviceproviderlov_ref IS
    v_cursor serviceproviderlov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT spr_code, spr_sht_desc, spr_name
        FROM tqc_service_providers
       ORDER BY spr_name;
  
    RETURN v_cursor;
  END;

  FUNCTION getallcountries RETURN countries_ref IS
    v_cursor countries_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT cou_code,
             cou_sht_desc,
             cou_name,
             cou_base_curr,
             cur_desc cou_base_curr_desc,
             cou_nationality,
             cou_zip_code,
             cou_admin_reg_type,
             cou_admin_reg_mandatory,
             cou_schegen,
             cou_emb_code,
             cou_curr_serial,
             cou_mobile_prefix, 
             cou_client_number
        FROM tqc_countries, tqc_currencies
       WHERE cou_base_curr = cur_code(+)
       ORDER BY cou_name;
  
    RETURN v_cursor;
  END;

  FUNCTION gettownsbycountry(v_cou_code IN NUMBER) RETURN towns_ref IS
    v_cursor towns_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT twn_code, twn_cou_code, twn_sht_desc, twn_name, twn_sts_code,NULL,NULL
        FROM tqc_towns
       WHERE twn_cou_code = v_cou_code
       ORDER BY twn_name;
  
    RETURN v_cursor;
  END;

  FUNCTION gettownsbystate(v_state_code IN NUMBER) RETURN towns_ref IS
    v_cursor towns_ref;
  BEGIN
    OPEN v_cursor FOR
        SELECT twn_code,
             twn_cou_code,
             twn_sht_desc,
             twn_name,
             twn_sts_code,
             pst_desc,
             pst_zip_code
        FROM tqc_towns, tqc_postal_codes
       WHERE twn_code = pst_twn_code(+)
         AND twn_sts_code = v_state_code
       ORDER BY twn_name;
     RETURN v_cursor;
  END;

  FUNCTION country_name(v_cou_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT cou_name
      INTO v_ret_val
      FROM tqc_countries
     WHERE cou_code = v_cou_code;
  
    RETURN v_ret_val;
  END country_name;

  FUNCTION town_name(v_twn_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT twn_name
      INTO v_ret_val
      FROM tqc_towns
     WHERE twn_code = v_twn_code;
  
    RETURN v_ret_val;
  END town_name;

  FUNCTION getholidaydefinitions(v_cou_code IN NUMBER) RETURN holidaysdef_ref IS
    v_cursor holidaysdef_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT thd_desc, thd_day, thd_mon, thd_status, thd_cou_code
        FROM tqc_holidays_definitions
       WHERE thd_cou_code = v_cou_code
       ORDER BY thd_desc;
  
    RETURN v_cursor;
  END;

  FUNCTION getpostalcodesbytown(v_twn_code NUMBER)
    RETURN postal_code_by_town_ref IS
    v_cursor postal_code_by_town_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT pst_code, pst_twn_code, pst_desc, pst_zip_code
        FROM tqc_postal_codes
       WHERE pst_twn_code = v_twn_code
       ORDER BY pst_desc;
  
    RETURN v_cursor;
  END;

  FUNCTION acc_type_name(v_act_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT act_account_type
      INTO v_ret_val
      FROM tqc_account_types
     WHERE act_code = v_act_code;
  
    RETURN v_ret_val;
  END acc_type_name;

  FUNCTION getallaccounttypeslov RETURN accounttypeslov_ref IS
    v_cursor accounttypeslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT act_code, act_type_sht_desc, act_account_type
        FROM tqc_account_types
       ORDER BY act_account_type;
  
    RETURN v_cursor;
  END;

  FUNCTION getallabudgettypeslov RETURN accounttypeslov_ref IS
    v_cursor accounttypeslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT act_code, act_type_sht_desc, act_account_type
        FROM tqc_account_types
       WHERE act_code IN (SELECT agn_act_code FROM tqc_agencies)
       ORDER BY act_account_type;
  
    RETURN v_cursor;
  END;

  FUNCTION gethierarchyacctypeslov(v_sys_code NUMBER)
    RETURN accounttypeslov_ref IS
    v_cursor accounttypeslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT act_code, act_type_sht_desc, act_account_type
        FROM tqc_account_types
       WHERE NOT EXISTS
       (SELECT dlt_code, dlt_sys_code, dlt_desc, dlt_act_code
                FROM tqc_org_division_levels_type
               WHERE dlt_sys_code = v_sys_code
                 AND tqc_account_types.act_code =
                     tqc_org_division_levels_type.dlt_act_code)
       ORDER BY act_account_type;
  
    RETURN v_cursor;
  END;

  FUNCTION getorgsubdivprevheads(v_osd_code IN VARCHAR2)
    RETURN orgsubdivprevheads_ref IS
    v_cursor orgsubdivprevheads_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT osdph_code,
             osdph_osd_code,
             osdph_prev_agn_code,
             osdph_wet,
             osd_name,
             agn_name(osdph_prev_agn_code) agency_name,
             osdph_osd_id
        FROM tqc_org_subdiv_prev_heads, tqc_org_subdivisions
       WHERE osdph_osd_code = osd_code
         AND osdph_osd_code = v_osd_code
       ORDER BY osdph_code;
  
    RETURN v_cursor;
  END;

  FUNCTION getsyspostlevelsbysys(v_sys_code NUMBER) RETURN syspostlevels_ref IS
    v_cursor syspostlevels_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT syspl_sys_code,
             syspl_code,
             syspl_sht_desc,
             syspl_desc,
             syspl_ranking,
             syspl_wef
        FROM tqc_sys_post_levels
       WHERE syspl_sys_code = v_sys_code
       ORDER BY syspl_desc;
  
    RETURN v_cursor;
  END;

  FUNCTION getsyspostsbysystem(v_sys_code NUMBER) RETURN syspost_ref IS
    v_cursor syspost_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT spost_sys_code,
             spost_syspl_code,
             spost_parent_spost_code,
             spost_code,
             spost_sht_desc,
             spost_desc,
             spost_remarks,
             spost_wef,
             spost_brn_code,
             spost_subdiv_osd_code,
             spost_usr_code
        FROM tqc_sys_posts
       WHERE spost_sys_code = v_sys_code
       ORDER BY spost_desc;
  
    RETURN v_cursor;
  END;

  FUNCTION getsyspostsbylevel(v_syspl_code NUMBER) RETURN syspost_ref IS
    v_cursor syspost_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT spost_sys_code,
             spost_syspl_code,
             spost_parent_spost_code,
             spost_code,
             spost_sht_desc,
             spost_desc,
             spost_remarks,
             spost_wef,
             spost_brn_code,
             spost_subdiv_osd_code,
             spost_usr_code
        FROM tqc_sys_posts
       WHERE spost_syspl_code = v_syspl_code
       ORDER BY spost_desc;
  
    RETURN v_cursor;
  END;

  FUNCTION getsyspostsbypost(v_spost_code NUMBER) RETURN syspost_ref IS
    v_cursor syspost_ref;
  BEGIN
    IF v_spost_code IS NULL THEN
      OPEN v_cursor FOR
        SELECT spost_sys_code,
               spost_syspl_code,
               spost_parent_spost_code,
               spost_code,
               spost_sht_desc,
               spost_desc,
               spost_remarks,
               spost_wef,
               spost_brn_code,
               spost_subdiv_osd_code,
               spost_usr_code
          FROM tqc_sys_posts
         WHERE spost_parent_spost_code IS NULL
         ORDER BY spost_desc;
    
      RETURN v_cursor;
    ELSE
      OPEN v_cursor FOR
        SELECT spost_sys_code,
               spost_syspl_code,
               spost_parent_spost_code,
               spost_code,
               spost_sht_desc,
               spost_desc,
               spost_remarks,
               spost_wef,
               spost_brn_code,
               spost_subdiv_osd_code,
               spost_usr_code
          FROM tqc_sys_posts
         WHERE spost_parent_spost_code = v_spost_code
         ORDER BY spost_desc;
    
      RETURN v_cursor;
    END IF;
  END;

  FUNCTION getsysprcsssubarealmts(v_sprsa_code NUMBER)
    RETURN sysprcsssubarealmts_ref IS
    v_cursor sysprcsssubarealmts_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT spsat_code,
             spsat_sprsa_code,
             spsat_no_of_level,
             spsat_min_limit,
             spsat_max_limit
        FROM tqc_sys_prcss_subarea_lmts
       WHERE spsat_sprsa_code = v_sprsa_code
       ORDER BY spsat_code;
  
    RETURN v_cursor;
  END;

  FUNCTION getsysprcsssubarealvls(v_spsat_code NUMBER)
    RETURN sysprcsssubarealvls_ref IS
    v_cursor sysprcsssubarealvls_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT spsal_code,
             spsal_sprsa_code,
             spsal_spsat_code,
             spsal_level,
             spsal_approver_type,
             spsal_approver_id,
             usr_username
        FROM tqc_sys_prcss_subarea_lvls, tqc_users
       WHERE spsal_approver_id = usr_code(+)
         AND spsal_spsat_code = v_spsat_code
       ORDER BY spsal_code;
  
    RETURN v_cursor;
  END;

  FUNCTION bank_name(v_bnk_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT bnk_bank_name
      INTO v_ret_val
      FROM tqc_banks
     WHERE bnk_code = v_bnk_code;
  
    RETURN v_ret_val;
  END bank_name;

  FUNCTION getddfwdingbankslov RETURN ddfwdingbanks_ref IS
    v_cursor ddfwdingbanks_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT DISTINCT NULL bnk_code,
                      NULL ddr_report_description,
                      NULL bnk_bank_name
        FROM DUAL
      UNION
      SELECT DISTINCT bnk_code,
                      ddr_report_description,
                      tqc_banks.bnk_bank_name
        FROM tqc_bank_branches,
             tqc_banks,
             tqc_direct_debit_reports,
             fms_bnk_accts
       WHERE tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code
         AND bct_bbr_code = bbr_code
         AND ddr_code(+) = bnk_ddr_code
         AND NVL(bbr_dd_supported, 'N') = 'Y'
       ORDER BY bnk_bank_name DESC;
  
    RETURN v_cursor;
  END;

  FUNCTION getdirectdebitreports RETURN directdebitreports_ref IS
    v_cursor directdebitreports_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT ALL ddr_code, ddr_report_description
        FROM tqc_direct_debit_reports
       ORDER BY ddr_code;
  
    RETURN v_cursor;
  END;

  FUNCTION ddreport_desc(v_ddr_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT ddr_report_description
      INTO v_ret_val
      FROM tqc_direct_debit_reports
     WHERE ddr_code = v_ddr_code;
  
    RETURN v_ret_val;
  END ddreport_desc;

  FUNCTION getusersbytypegroup RETURN users_ref IS
    v_cursor users_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT usr_code,
             usr_username,
             usr_name,
             usr_last_date,
             usr_dt_created,
             usr_status,
             usr_pwd,
             usr_created_by,
             usr_email,
             usr_per_rank_id,
             usr_per_rank_sht_desc,
             usr_per_id,
             usr_personel_rank,
             usr_pwd_changed,
             usr_pwd_reset,
             usr_login_attempts,
             usr_sign,
             usr_ref,
             usr_type,
             usr_signature,
             usr_acct_mgr
        FROM tqc_users
       WHERE usr_type = 'G';
  
    RETURN v_cursor;
  END;

  FUNCTION getusersbytypeuser RETURN users_ref IS
    v_cursor users_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT usr_code,
             usr_username,
             usr_name,
             usr_last_date,
             usr_dt_created,
             usr_status,
             usr_pwd,
             usr_created_by,
             usr_email,
             usr_per_rank_id,
             usr_per_rank_sht_desc,
             usr_per_id,
             usr_personel_rank,
             usr_pwd_changed,
             usr_pwd_reset,
             usr_login_attempts,
             usr_sign,
             usr_ref,
             usr_type,
             usr_signature,
             usr_acct_mgr
        FROM tqc_users
       WHERE usr_type = 'U';
  
    RETURN v_cursor;
  END;

  FUNCTION currency_sysmbol(v_cur_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(15);
  BEGIN
    SELECT cur_symbol
      INTO v_ret_val
      FROM tqc_currencies
     WHERE cur_code = v_cur_code;
  
    RETURN v_ret_val;
  END currency_sysmbol;

  FUNCTION state_name(v_sts_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(50);
  BEGIN
    SELECT sts_name
      INTO v_ret_val
      FROM tqc_states
     WHERE sts_code = v_sts_code;
  
    RETURN v_ret_val;
  END state_name;

  FUNCTION admin_region_name(v_admin_region_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(50);
  BEGIN
    SELECT adm_reg_name
      INTO v_ret_val
      FROM tqc_admin_regions
     WHERE adm_reg_code = v_admin_region_code;
  
    RETURN v_ret_val;
  END admin_region_name;

  FUNCTION system_param_val(v_param_name VARCHAR2) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT NVL(param_value, 'N')
      INTO v_ret_val
      FROM tqc_parameters
     WHERE param_name = v_param_name;
  
    RETURN v_ret_val;
  END system_param_val;

  FUNCTION check_activity_exist(v_param_name VARCHAR2)
    RETURN check_activity_exist_ref IS
    v_cursor check_activity_exist_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT COUNT(1) AS v_count
        FROM tqc_activity_types
       WHERE acty_desc = v_param_name;
  
    RETURN v_cursor;
  END;

  FUNCTION check_hierarchy_exist(v_param_name VARCHAR2)
    RETURN check_activity_exist_ref IS
    v_cursor check_activity_exist_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT COUNT(1) AS v_count
        FROM tqc_org_division_levels
       WHERE odl_code = v_param_name;
  
    RETURN v_cursor;
  END;

  FUNCTION check_hierarchy_ranking_exist(v_odlt_code VARCHAR2,
                                         v_rank      tqc_org_division_levels.odl_ranking%TYPE)
    RETURN check_activity_exist_ref IS
    v_cursor check_activity_exist_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT COUNT(1) AS v_count
        FROM tqc_org_division_levels
       WHERE odl_dlt_code = v_odlt_code
         AND odl_ranking = v_rank;
  
    RETURN v_cursor;
  END;

  FUNCTION checkif_dob_required RETURN checkif_dob_required_ref IS
    v_cursor checkif_dob_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('CLIENT_DATE_OF_BIRTH_REQUIRED')
        FROM DUAL;
  
    RETURN v_cursor;
  END;

  FUNCTION checkif_sms_required RETURN checkif_dob_required_ref IS
    v_cursor checkif_dob_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('CLIENT_SMS_REQUIRED')
        FROM DUAL;
  
    RETURN v_cursor;
  END;

  FUNCTION checkif_default_date RETURN checkif_dob_required_ref IS
    v_cursor checkif_dob_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('CLIENT _DEFAULT_DOB_DATE')
        FROM DUAL;
  
    RETURN v_cursor;
  END;

  FUNCTION check_max_rank(v_dlt_code IN VARCHAR2) RETURN check_max_rank_ref IS
    v_cursor check_max_rank_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT MAX(tqc_org_division_levels.odl_ranking)
        FROM tqc_org_division_levels
       WHERE odl_dlt_code = v_dlt_code;
  
    RETURN v_cursor;
  END;

  FUNCTION checkif_uppercase_required RETURN checkif_dob_required_ref IS
    v_cursor checkif_dob_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('CLIENT_UPPERCASE_REQUIRED')
        FROM DUAL;
  
    RETURN v_cursor;
  END;
FUNCTION checkifSaccoRequired RETURN checkif_dob_required_ref IS
    v_cursor checkif_dob_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('SACCO_ENEBLED')
        FROM DUAL;
  RETURN v_cursor;
  END;

  FUNCTION checkif_divisions_applic RETURN checkif_dob_required_ref IS
    v_cursor checkif_dob_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('DIVISIONS_APPLIC')
        FROM DUAL;
  
    RETURN v_cursor;
  END;

  /*FUNCTION getParticipantsByActivity(v_aac_code TQC_ACTIVITY_PARTICIPANTS.PARTICIP_AAC_CODE%TYPE)  RETURN participantsByActivity_ref IS
     v_cursor  participantsByActivity_ref;
  BEGIN
  
      OPEN v_cursor FOR
      SELECT  PARTICIP_ID, PARTICIP_CODE,PARTICIP_AAC_CODE,ACCOUNT_TYPE_NAME(PARTICIP_TYPE) as  PARTICIP_TYPE,PARTICIP_BY_CODE, GETAGENCY_NAME(PARTICIP_BY_CODE,PARTICIP_TYPE) agnName
  
      FROM   TQC_ACTIVITY_PARTICIPANTS
      WHERE  PARTICIP_AAC_CODE=v_aac_code ;
  
      RETURN v_cursor;
  END; */
  FUNCTION get_agencyname(v_agn_type VARCHAR2, v_ag_code NUMBER)
    RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    IF v_agn_type = 'CL' THEN
      SELECT clnt_name
        INTO v_ret_val
        FROM tqc_clients
       WHERE clnt_code = v_ag_code;
    
      RETURN v_ret_val;
    ELSIF v_agn_type = 'SP' THEN
      SELECT spr_name
        INTO v_ret_val
        FROM tqc_service_providers
       WHERE spr_code = v_ag_code;
    
      RETURN v_ret_val;
    ELSIF v_agn_type = 'MK' THEN
      SELECT agn_name
        INTO v_ret_val
        FROM tqc_agencies
       WHERE agn_code = v_ag_code;
    
      RETURN v_ret_val;
    END IF;
  END get_agencyname;

  /*PROCEDURE create_activity_particip(v_addEdit VARCHAR2,
                            v_particip_id          TQC_ACTIVITY_PARTICIPANTS.PARTICIP_ID%TYPE ,
                            v_particip_aac_code  TQC_ACTIVITY_PARTICIPANTS.PARTICIP_AAC_CODE%TYPE,
                            v_particip_type      TQC_ACTIVITY_PARTICIPANTS.PARTICIP_TYPE%TYPE,
                            v_particip_by_code   TQC_ACTIVITY_PARTICIPANTS.PARTICIP_BY_CODE%TYPE,
                            v_particip_code      TQC_ACTIVITY_PARTICIPANTS.PARTICIP_CODE%TYPE
  
                                  ) IS
  
      v_count Number;
  BEGIN
      IF v_addEdit = 'A' THEN
  
      select count(*) into v_count from TQC_ACTIVITY_PARTICIPANTS
          where particip_aac_code=v_particip_aac_code
          AND particip_by_code=v_particip_by_code;
          IF v_count>0 THEN
           RAISE_ERROR('Duplicate Entry::Participants you are Trying to add  Alreaddy Exist::'|| SQLERRM (SQLCODE));
         END IF;
  
          INSERT INTO  TQC_ACTIVITY_PARTICIPANTS(
                            PARTICIP_ID ,
                            PARTICIP_AAC_CODE ,
                            PARTICIP_TYPE ,
                            PARTICIP_BY_CODE,
                            PARTICIP_CODE
                            )
          VALUES(           ACT_PARTICIP_SEQ.NEXTVAL,
                            v_particip_aac_code ,
                            v_particip_type,
                            v_particip_by_code,
                            v_particip_code
  
          );
  
      ELSIF v_addEdit = 'E' THEN
          UPDATE TQC_ACTIVITY_PARTICIPANTS SET
                            PARTICIP_AAC_CODE=v_particip_aac_code ,
                            PARTICIP_TYPE=v_particip_type ,
                            PARTICIP_BY_CODE=v_particip_by_code,
                            PARTICIP_CODE=v_particip_code
  
          WHERE             PARTICIP_ID=v_particip_id;
      ELSE
          DELETE TQC_ACTIVITY_PARTICIPANTS
          WHERE PARTICIP_ID = v_particip_id;
      END IF;
  EXCEPTION WHEN NO_DATA_FOUND THEN
      RAISE_ERROR('Error Manipulating Activity Participants  Record'|| SQLERRM (SQLCODE));
  END create_activity_particip;*/
  FUNCTION checkif_user_active(v_client_code tqc_clients.clnt_status%TYPE)
    RETURN checkif_user_active_ref IS
    v_cursor checkif_user_active_ref;
  BEGIN
  --RAISE_ERROR('v_client_code'||v_client_code);
    OPEN v_cursor FOR
      SELECT clnt_status FROM tqc_clients WHERE clnt_code = v_client_code;
  
    RETURN v_cursor;
  END;

  FUNCTION sector_name(v_sec_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT sec_name
      INTO v_ret_val
      FROM tqc_sectors
     WHERE sec_code = v_sec_code;
  
    RETURN v_ret_val;
  END sector_name;

  FUNCTION parent_company_name(v_parent_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    SELECT clnt_name
      INTO v_ret_val
      FROM tqc_clients
     WHERE clnt_code = v_parent_code;
  
    RETURN v_ret_val;
  END parent_company_name;

  FUNCTION checkif_telephonerequired RETURN checkif_dob_required_ref IS
    v_cursor checkif_dob_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('TELEPHONE_REQUIRED')
        FROM DUAL;
  
    RETURN v_cursor;
  END;

  FUNCTION checkif_emailrequired RETURN checkif_dob_required_ref IS
    v_cursor checkif_dob_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('EMAIL_REQUIRED')
        FROM DUAL;
  
    RETURN v_cursor;
  END;

  FUNCTION getagenciesbyacctype(v_sht_desc IN VARCHAR2)
    RETURN agencieslov_ref IS
    v_cursor agencieslov_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT agn_code, agn_sht_desc, agn_name
        FROM tqc_agencies, tqc_account_types
       WHERE act_type_sht_desc = v_sht_desc
         AND act_code = agn_act_code
       ORDER BY agn_name;
  
    RETURN v_cursor;
  END;

  FUNCTION getagency_name(v_agn_code NUMBER, v_acc_type VARCHAR2)
    RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    IF v_acc_type = 'CL' THEN
      SELECT clnt_name
        INTO v_ret_val
        FROM tqc_clients
       WHERE clnt_code = v_agn_code;
    
      RETURN v_ret_val;
    ELSIF v_acc_type = 'SP' THEN
      SELECT spr_name
        INTO v_ret_val
        FROM tqc_service_providers
       WHERE spr_code = v_agn_code;
    
      RETURN v_ret_val;
    ELSE
      SELECT agn_name
        INTO v_ret_val
        FROM tqc_agencies
       WHERE agn_code = v_agn_code;
    
      RETURN v_ret_val;
    END IF;
  END getagency_name;

  FUNCTION account_type_name(v_sht_desc VARCHAR2) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    IF v_sht_desc = 'SP' THEN
      v_ret_val := 'SERVICE PROVIDER';
      RETURN v_ret_val;
    ELSIF v_sht_desc = 'CL' THEN
      v_ret_val := 'CLIENT';
      RETURN v_ret_val;
    ELSE
      SELECT act_account_type
        INTO v_ret_val
        FROM tqc_account_types
       WHERE act_type_sht_desc = v_sht_desc;
    
      RETURN v_ret_val;
    END IF;
  END account_type_name;

  FUNCTION finddefaultsite RETURN returnparam_ref IS
    v_cursor returnparam_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('DEFAULT_SITE')
        FROM DUAL;
  
    RETURN v_cursor;
  END;

  FUNCTION findbranches(v_reg_code NUMBER) RETURN returnbranches_ref IS
    v_cursor returnbranches_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT brn_code, brn_sht_desc, brn_name FROM tqc_branches; -- where BRN_REG_CODE=v_reg_code ;
  
    RETURN v_cursor;
  END;

  FUNCTION branch_name(v_brn_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(50);
  BEGIN
    SELECT brn_name
      INTO v_ret_val
      FROM tqc_branches
     WHERE brn_code = v_brn_code;
  
    RETURN v_ret_val;
  END branch_name;

  FUNCTION bankbranch_name(v_bbr_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    BEGIN
      SELECT bnk_bank_name || ' - ' || bbr_branch_name
        INTO v_ret_val
        FROM tqc_bank_branches, tqc_banks
       WHERE bbr_code = v_bbr_code
         AND bbr_bnk_code = bnk_code;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_ret_val := NULL;
    END;
  
    RETURN v_ret_val;
  END bankbranch_name;

  FUNCTION getclaimpaymodes RETURN claimpaymodes_ref IS
    v_cursor claimpaymodes_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT cpm_code,
             cpm_sht_desc,
             cpm_desc,
             cpm_remarks,
             cpm_min_amt,
             cpm_max_amt,
             cpm_default
        FROM tqc_clm_payment_modes;
  
    RETURN v_cursor;
  END;

  FUNCTION getmobilepaymenttypes(v_cou_code tqc_mobile_pymnt_types.mpt_cou_code%TYPE)
    RETURN mobilepaymenttypes_ref IS
    v_cursor mobilepaymenttypes_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT mpt_code,
             mpt_sht_desc,
             mpt_desc,
             mpt_min_amt_allowed,
             mpt_max_amt_allowed,
             mpt_cou_code
        FROM tqc_mobile_pymnt_types
       WHERE mpt_cou_code = v_cou_code;
  
    RETURN v_cursor;
  END;

  FUNCTION getmobtypesprefixes(v_mptp_code tqc_mob_pymnt_types_prefixes.mptp_mpt_code%TYPE)
    RETURN mobiletypesprefix_ref IS
    v_cursor mobiletypesprefix_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT mptp_code, mptp_mob_no_prefix, mptp_mpt_code,null sms_number
        FROM tqc_mob_pymnt_types_prefixes
       WHERE mptp_mpt_code = v_mptp_code;
  
    RETURN v_cursor;
  END;
  
  FUNCTION getMobPrefixes(v_couCode     NUMBER)
    RETURN mobiletypesprefix_ref IS
    v_cursor mobiletypesprefix_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT DISTINCT mptp_mob_no_prefix mptp_code, mptp_mob_no_prefix, mptp_mob_no_prefix mptp_mpt_code,null sms_number
        FROM tqc_mob_pymnt_types_prefixes,tqc_mobile_pymnt_types
       WHERE mptp_mpt_code = MPT_CODE
      -- AND MPTP_MPT_CODE=34534535
       AND MPT_COU_CODE = NVL(v_couCode,MPT_COU_CODE)
       ORDER BY mptp_mob_no_prefix;
  
    RETURN v_cursor;
  END;

  FUNCTION getgroupusers(v_created_date VARCHAR2) RETURN user_ref IS
    v_cursor user_ref;
  BEGIN
    --raise_error('v_grp_usr_code---->'|| v_created_date );
    IF v_created_date IS NULL THEN
      OPEN v_cursor FOR
        SELECT usr_code,
               usr_username,
               usr_name,
               usr_dt_created,
               usr_created_by
          FROM tqc_users
         WHERE usr_type = 'G';
    ELSE
      OPEN v_cursor FOR
        SELECT usr_code,
               usr_username,
               usr_name,
               usr_dt_created,
               usr_created_by
          FROM tqc_users
         WHERE usr_type = 'G'
           AND usr_dt_created = TO_DATE(v_created_date, 'dd/mm/yyyy');
    END IF;
  
    RETURN v_cursor;
  END;

  FUNCTION getgroupsmembers(v_grp_usr_code tqc_group_users.gusr_grp_usr_code%TYPE)
    RETURN usergroup_ref IS
    v_cursor usergroup_ref;
  BEGIN
    ---  raise_error('v_grp_usr_code---->'|| v_grp_usr_code);
    OPEN v_cursor FOR
      SELECT usr_code, usr_username, usr_name, gusr_code, gusr_grp_usr_code
        FROM tqc_users, tqc_group_users
       WHERE usr_code = gusr_usr_code
         AND gusr_grp_usr_code = v_grp_usr_code;
  
    RETURN v_cursor;
  END;

  FUNCTION findmemoclassapplicable RETURN returnparam_ref IS
    v_cursor returnparam_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT param_value
        FROM gin_parameters
       WHERE param_name = 'MEMO_CLASS_LVL';
  
    RETURN v_cursor;
  END;

  FUNCTION getclientgroups RETURN clientgroup_ref IS
    v_cursor clientgroup_ref;
  BEGIN
    ---  raise_error('v_grp_usr_code---->'|| v_grp_usr_code);
    OPEN v_cursor FOR
      SELECT grp_code, grp_name, grp_minimum, grp_maximum
        FROM tqc_group_client;
  
    RETURN v_cursor;
  END;

  FUNCTION getclientgroupmembers(v_grp_code tqc_group_clnt_dtls.grpd_grp_code%TYPE)
    RETURN clientgroupdtls_ref IS
    v_cursor clientgroupdtls_ref;
  BEGIN
    ---  raise_error('v_grp_usr_code---->'|| v_grp_usr_code);
    OPEN v_cursor FOR
      SELECT grp_name,
             grpd_code,
             grpd_clnt_code,
             grpd_grp_code,
             clnt_sht_desc,
             clnt_name,
             clnt_other_names
        FROM tqc_group_client, tqc_group_clnt_dtls, tqc_clients
       WHERE grp_code = grpd_grp_code
         AND grpd_clnt_code = clnt_code
         AND grp_code = v_grp_code;
  
    RETURN v_cursor;
  END;

  FUNCTION getclientcomplaints(v_clnt_code tqc_client_complaints.comp_clnt_code%TYPE)
    RETURN clientcomplaints_ref IS
    v_cursor clientcomplaints_ref;
  BEGIN
    ---  raise_error('v_grp_usr_code---->'|| v_grp_usr_code);
    OPEN v_cursor FOR
      SELECT comp_code,
             comp_clnt_code,
             comp_subject,
             comp_message,
             clnt_sht_desc,
             clnt_name,
             clnt_other_names
        FROM tqc_client_complaints, tqc_clients
       WHERE comp_clnt_code = clnt_code
         AND comp_clnt_code = v_clnt_code;
  
    RETURN v_cursor;
  END;

  FUNCTION physical_address_required RETURN returnparam_ref IS
    v_cursor returnparam_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('PHYSICAL_ADDRESS_REQUIRED')
        FROM DUAL;
  
    RETURN v_cursor;
  END;

  FUNCTION postal_address_required RETURN returnparam_ref IS
    v_cursor returnparam_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('POSTAL_ADDRESS_REQUIRED')
        FROM DUAL;
  
    RETURN v_cursor;
  END;

  FUNCTION get_print_servers RETURN print_server_ref IS
    v_cursor print_server_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT serv_code,
             serv_name,
             serv_filter,
             serv_uri,
             serv_filter_command,
             serv_sec_username,
             serv_sec_password,
             serv_sec_auth_type,
             serv_sec_encrpt_type,
             serv_proxy_host,
             serv_proxy_port,
             serv_proxy_username,
             serv_proxy_pasword,
             serv_proxy_authen_type
        FROM tqc_print_servers;
  
    RETURN v_cursor;
  END;

  FUNCTION getbranchnames RETURN branch_names_details_ref IS
    v_cursor branch_names_details_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT bns_code,
             bns_sht_desc,
             bns_name,
             bns_phy_addrs,
             bns_email_addrs,
             bns_post_addrs,
             bns_twn_code,
             bns_cou_code,
             bns_contact,
             bns_manager,
             bns_tel,
             bns_fax,
             bns_state_code,
             town_name(bns_twn_code) town,
             country_name(bns_cou_code) country,
             state_name(bns_state_code) state
        FROM tqc_branch_names;
  
    RETURN v_cursor;
  END;

  PROCEDURE get_branch_printers(v_cursor OUT branch_printer_ref,
                                brn_code IN NUMBER,
                                div_code IN NUMBER) IS
  BEGIN
    OPEN v_cursor FOR
      SELECT brp_printer_name
        FROM tqc_branch_printers
       WHERE brp_brn_code = brn_code
         AND brp_div_code = div_code;
  END;

  PROCEDURE get_branch_print_servers(v_cursor OUT print_server_ref,
                                     brn_code IN NUMBER,
                                     div_code IN NUMBER) IS
  BEGIN
    OPEN v_cursor FOR
      SELECT serv_code,
             serv_name,
             serv_filter,
             serv_uri,
             serv_filter_command,
             serv_sec_username,
             serv_sec_password,
             serv_sec_auth_type,
             serv_sec_encrpt_type,
             serv_proxy_host,
             serv_proxy_port,
             serv_proxy_username,
             serv_proxy_pasword,
             serv_proxy_authen_type
        FROM tqc_print_servers;
    --WHERE ;
  END;

  FUNCTION getmessagetemplates(v_sys_code NUMBER) RETURN msg_template_ref IS
    v_cursor msg_template_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT msgt_code,
             msgt_sht_desc,
             msgt_msg,
             msgt_sys_code,
             msgt_sys_module,
             msgt_type
        FROM tqc_msg_templates
       WHERE msgt_sys_code = v_sys_code;
  
    RETURN(v_cursor);
  END;

  FUNCTION getparameter(v_param VARCHAR2) RETURN returnparam_ref IS
    v_cursor returnparam_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar(v_param) FROM DUAL;
  
    RETURN v_cursor;
  END;

  PROCEDURE get_allbranches(v_refcur OUT branch_ref, v_org_code IN NUMBER) IS
  BEGIN
    -- Raise_error('org code '||v_org_code);
    OPEN v_refcur FOR
      SELECT -2000 brn_code,
             'ALL BRANCHES' brn_name,
             'ALL' brn_sht_desc,
             0 brn_reg_code
        FROM DUAL
      UNION
      SELECT DISTINCT brn_code, brn_name, brn_sht_desc, brn_reg_code
        FROM tqc_branches, tqc_regions
       WHERE reg_code = brn_reg_code
         AND reg_org_code = v_org_code;
  END;

  PROCEDURE get_lead_sources(v_refcur OUT lead_sources_ref) IS
  BEGIN
    OPEN v_refcur FOR
      SELECT ldsrc_code, ldsrc_desc FROM tqc_leads_sources;
  END;

  PROCEDURE get_lead_statuses(v_refcur OUT lead_sources_ref) IS
  BEGIN
    OPEN v_refcur FOR
      SELECT lsts_code, lsts_desc FROM tqc_leads_statuses;
  END;

  PROCEDURE get_leads(v_refcur OUT leads_ref, v_lds_code NUMBER) IS
  BEGIN
    OPEN v_refcur FOR
      SELECT lds_code,
             lds_title,
             lds_surnname,
             lds_othernames,
             lds_camp_tel,
             lds_mob_tel,
             lds_camp_code,
             (SELECT cmp_name
                FROM tqc_campaigns
               WHERE cmp_code = lds_camp_code) lds_camp_name,
             lds_fax,
             lds_design,
             lds_email_addrs,
             lds_rate_type,
             lds_physical_addrs,
             lds_postal_addrs,
             (SELECT cou_name
                FROM tqc_countries
               WHERE cou_code = lds_cou_code) lds_country,
             lds_postal_code,
             (SELECT sts_name
                FROM tqc_states
               WHERE sts_code = lds_state_code) lds_state_name,
             lds_twn_code,
             (SELECT twn_name FROM tqc_towns WHERE twn_code = lds_twn_code) lds_town_name,
             lds_cou_code,
             lds_state_code,
             TO_DATE(lds_date, 'DD/MM/yyyy') lds_date,
             lds_desc,
             lds_usr_code,
             (SELECT usr_name FROM tqc_users WHERE usr_code = lds_usr_code) lds_usr_name,
             lds_org_code,
             (SELECT org_name
                FROM tqc_organizations
               WHERE org_code = lds_org_code) lds_org_name,
             lds_sys_code,
             (SELECT sys_name FROM tqc_systems WHERE sys_code = lds_sys_code) lds_sys_name,
             lds_converted,
             lds_pont_name,
             lds_pont_conrt,
             lds_pont_amount,
             lds_cur_code,
             (SELECT cur_desc || '(' || cur_symbol || ')'
                FROM tqc_currencies
               WHERE cur_code = lds_cur_code) lds_cur_name,
             lds_pont_close_date,
             lds_pont_sale_stage,
             lds_industry,
             lds_ann_revenue,
             lds_no_empyee,
             lds_web_site,
             lds_team_usr_code,
             (SELECT usr_name
                FROM tqc_users
               WHERE usr_code = lds_team_usr_code) lds_team_name,
             lds_acc_code,
             (SELECT agn_name
                FROM tqc_agencies
               WHERE agn_code IN
                     (SELECT acc_type_code
                        FROM tqc_accounts
                       WHERE acc_code = lds_acc_code)
              UNION
              SELECT clnt_name
                FROM tqc_clients
               WHERE clnt_code IN
                     (SELECT acc_type_code
                        FROM tqc_accounts
                       WHERE acc_code = lds_acc_code)
              UNION
              SELECT spr_name
                FROM tqc_service_providers
               WHERE spr_code IN
                     (SELECT acc_type_code
                        FROM tqc_accounts
                       WHERE acc_code = lds_acc_code)) lds_account_name,
             lds_lsts_code,
             (SELECT lsts_desc
                FROM tqc_leads_statuses
               WHERE lsts_code = lds_lsts_code) lds_lsts_desc,
             lds_ldsrc_code,
             (SELECT ldsrc_desc
                FROM tqc_leads_sources
               WHERE ldsrc_code = lds_ldsrc_code) lds_ldsrc_desc,
             lds_prod_code,
             (SELECT tqc_campaign_cursor.get_product_desc(lds_sys_code,
                                                          lds_prod_code)
                FROM DUAL) lds_prod_name,
             lds_div_code,
             (SELECT div_name
                FROM tqc_divisions
               WHERE div_code = lds_div_code) lds_div_name
        FROM tqc_leads
       WHERE lds_code = NVL(v_lds_code, lds_code);
  END;

  PROCEDURE get_lead_comments(v_refcur   OUT lead_comments_ref,
                              v_lds_code NUMBER) IS
  BEGIN
    OPEN v_refcur FOR
      SELECT lcmnt_code, lcmnt_comment, lcmnt_date
        FROM tqc_lead_comments
       WHERE lcmnt_lds_code = v_lds_code;
  END;

  PROCEDURE get_lead_activities(v_refcur   OUT lead_activities_ref,
                                v_lds_code NUMBER) IS
  BEGIN
    OPEN v_refcur FOR
      SELECT lacts_code, act_code, act_subject, act_location
        FROM tqc_leads_activities, tqc_activities
       WHERE lacts_lds_code = v_lds_code
         AND lacts_act_code = act_code;
  END;

  FUNCTION get_all_report_groups RETURN rptgroup_ref IS
    v_prtgroup_rec rptgroup_ref;
  BEGIN
    OPEN v_prtgroup_rec FOR
      SELECT trg_code, trg_name FROM tqc_report_groups;
  
    RETURN(v_prtgroup_rec);
  END get_all_report_groups;

  PROCEDURE get_rptgroupdiv(v_trg_code       IN tqc_report_groups.trg_code%TYPE,
                            v_rptgroupdivcur OUT rptgroupdiv_ref) IS
  BEGIN
    OPEN v_rptgroupdivcur FOR
      SELECT rdg_code, rdg_trg_code, rdg_div_code, div_name, div_order
        FROM tqc_divisions, tqc_report_groups, tqc_report_div_groups
       WHERE rdg_div_code = div_code(+)
         AND rdg_trg_code = trg_code
         AND rdg_trg_code = v_trg_code;
  END get_rptgroupdiv;

  PROCEDURE getundefinedrptgrpdivisions(v_trg_code     IN tqc_report_groups.trg_code%TYPE,
                                        v_divisios_cur OUT tqc_web_cursor.divisions_ref) IS
  BEGIN
    OPEN v_divisios_cur FOR
      SELECT div_code,
             div_name,
             div_sht_desc,
             div_division_status,
             div_order,
             NULL div_director_code,
             NULL div_director,
             NULL div_ass_director_code,
             NULL div_asst_director
        FROM tqc_divisions
       WHERE div_code NOT IN
             (SELECT rdg_div_code
                FROM tqc_report_div_groups
               WHERE rdg_trg_code = v_trg_code);
  END getundefinedrptgrpdivisions;

  PROCEDURE getsystemapparea(v_saa_sys_code   tqc_sys_applicable_areas.saa_sys_code%TYPE,
                             v_sysapparea_cur OUT sys_app_area_ref) IS
  BEGIN
    OPEN v_sysapparea_cur FOR
      SELECT saa_code, saa_sys_code, sys_name, saa_description
        FROM tqc_systems, tqc_sys_applicable_areas
       WHERE saa_sys_code = sys_code(+)
         AND saa_sys_code = NVL(v_saa_sys_code, saa_sys_code);
  END getsystemapparea;

  FUNCTION get_modules_subunits(v_prod_code IN NUMBER)
    RETURN mod_subunits_ref IS
    v_ref mod_subunits_ref;
  BEGIN
    --RAISE_ERROR('GURUWE '||vscl_code);
    OPEN v_ref FOR
      SELECT tsms_code,
             tsms_tsm_code,
             tsms_sht_desc,
             tsms_desc,
             tsms_order,
             tsms_prod_code,
             pro_desc,
             tsm_desc
        FROM tqc_sys_mod_subunits, gin_products, tqc_system_modules
       WHERE tsms_tsm_code = tsm_code
         AND tsms_prod_code = pro_code(+)
         AND tsm_sys_code = 37
         AND tsms_prod_code = v_prod_code;
  
    RETURN(v_ref);
  END;

  FUNCTION get_mod_subunits_det(v_code IN NUMBER) RETURN mod_subunits_det_ref IS
    v_ref mod_subunits_det_ref;
  BEGIN
    OPEN v_ref FOR
      SELECT tsmsd_code,
             tsmsd_name,
             tsmsd_prompt,
             tsmsd_type,
             tsmsd_order,
             tsmsd_parent,
             tsmsd_more_dtls_appl,
             tsmsd_more_dtls,
             tsmsd_root,
             tsmsd_more_dtls_required,
             tsmsd_tmsc_code,
             tmsc_desc
        FROM tqc_sys_mod_subunits_details, tqc_mod_sys_columns
       WHERE tsmsd_tsms_code = v_code
         AND tsmsd_tmsc_code = tmsc_code(+);
  
    RETURN(v_ref);
  END;

  FUNCTION get_proposal_mod_systems RETURN proposal_sched_mod_ref IS
    v_ref proposal_sched_mod_ref;
  BEGIN
    OPEN v_ref FOR
      SELECT tsm_code, tsm_sht_desc, tsm_desc
        FROM tqc_system_modules
       WHERE tsm_sys_code = 27;
  
    RETURN(v_ref);
  END;

  FUNCTION get_subunits_options(v_code IN NUMBER) RETURN subunits_options_ref IS
    v_ref subunits_options_ref;
  BEGIN
    OPEN v_ref FOR
      SELECT tsso_code,
             tsso_tsmsd_code,
             tsso_option_name,
             tsso_option_desc,
             tsso_order,
             tsso_type
        FROM tqc_sys_subunits_options
       WHERE tsso_tsmsd_code = v_code;
  
    RETURN(v_ref);
  END;

  FUNCTION getsysmappingcolmns(v_syscode IN NUMBER)
    RETURN mapping_columns_ref IS
    v_cursor mapping_columns_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tmsc_code, tmsc_desc
        FROM tqc_mod_sys_columns
       WHERE tmsc_sys_code = v_syscode;
  
    RETURN v_cursor;
  END;

  FUNCTION getincomingmailsettings RETURN mail_settings_ref IS
    v_cursor mail_settings_ref;
  BEGIN
    OPEN v_cursor FOR
       SELECT MAIL_TYPE, MAIL_SERVER_NAME, MAIL_HOST, MAIL_USERNAME, 
    MAIL_PASSWORD, MAIL_PORT, MAIL_EMAIL, MAIL_IN_OUT,MAIL_SECURE
        FROM tqc_system_mails
       WHERE mail_in_out = 'I';
  
    RETURN v_cursor;
  END;

  FUNCTION getoutgoingmailsettings RETURN mail_settings_ref IS
    v_cursor mail_settings_ref;
  BEGIN
    OPEN v_cursor FOR
       SELECT MAIL_TYPE, MAIL_SERVER_NAME, MAIL_HOST, MAIL_USERNAME, 
    MAIL_PASSWORD, MAIL_PORT, MAIL_EMAIL, MAIL_IN_OUT,MAIL_SECURE
        FROM tqc_system_mails
       WHERE mail_in_out = 'O';
  
    RETURN v_cursor;
  END;

 FUNCTION get_alerts(v_jobName   VARCHAR2 DEFAULT NULL,
                    v_sysCode   NUMBER   DEFAULT NULL) RETURN alert_cursor
IS
v_cursor alert_cursor;
BEGIN

    OPEN v_cursor FOR
    SELECT QT_JOB_NAME, QT_DESCRIPTION, QT_NEXT_FIRE_TIME, QT_PREV_FIRE_TIME, QT_START_TIME, 
    QT_END_TIME, QT_CODE, QT_SYS_CODE, QT_RECURRENCE, QT_RECURRENCE_TYPE, QT_JOB_ASSIGNEE, 
    QT_NOTIFIED_FAIL_USER, QT_NOTIFIED_SUCC_USER, QT_RECCURENCE_INTERVAL,QT_JOB_TYPE, 
    QT_JOB_TEMPLATE, QT_FAIL_NOTIFY_TEMPLATE, QT_SUCC_NOTIFY_TEMPLATE,A.USR_USERNAME ASSIGNEE,
    B.USR_USERNAME FAIL_USER,C.USR_USERNAME SUCC_USER, 
    CASE WHEN QT_JOB_TYPE = 'A' THEN JOB_TEMP.MSGT_MSG ELSE 
    CASE WHEN QT_JOB_TYPE = 'R' THEN RPT_DESCRIPTION ELSE 
    CASE WHEN QT_JOB_TYPE = 'W' THEN SPRC_PROCESS ELSE 
    CASE WHEN QT_JOB_TYPE = 'J' THEN TSR_SHT_DESC ELSE NULL END
    END END END AS OBJ_EXECUTION ,FAIL_TEMP.MSGT_MSG,
    SUCC_TEMP.MSGT_MSG,A.USR_EMAIL,B.USR_EMAIL,C.USR_EMAIL,QT_STATUS,QT_THRESHOLD_TYPE, QT_THRESHOLD_VALUE,A.USR_TYPE,
    QT_CRON_EXPRESSION
    FROM QRTZ_TRIGGERS,TQC_USERS A,TQC_USERS B,TQC_USERS C,
    TQC_MSG_TEMPLATES FAIL_TEMP,TQC_MSG_TEMPLATES SUCC_TEMP,TQC_MSG_TEMPLATES JOB_TEMP,
    TQC_SYSTEM_REPORTS,JBPM4_DEPLOYPROP,TQC_SYS_PROCESSES,TQC_SYSTEM_ROUTINES
    WHERE QT_JOB_ASSIGNEE = A.USR_CODE(+)
    AND QT_NOTIFIED_FAIL_USER = B.USR_CODE(+)
    AND QT_JOB_NAME = NVL(v_jobName,QT_JOB_NAME)
    AND QT_SYS_CODE = NVL(v_sysCode,QT_SYS_CODE)
    AND QT_NOTIFIED_SUCC_USER = C.USR_CODE(+)
    AND QT_JOB_TEMPLATE = RPT_CODE(+)
    AND QT_JOB_TEMPLATE = DBID_(+)
    AND QT_JOB_TEMPLATE = TSR_CODE(+)
    AND SPRC_JPDL_DESC(+) = OBJNAME_
    AND JOB_TEMP.MSGT_CODE(+) = QT_JOB_TEMPLATE
    AND FAIL_TEMP.MSGT_CODE(+) = QT_FAIL_NOTIFY_TEMPLATE
    AND SUCC_TEMP.MSGT_CODE(+) = QT_SUCC_NOTIFY_TEMPLATE;
    
   RETURN (v_cursor);

END;

  FUNCTION getexecutionobjects(v_syscode NUMBER,
                               v_type    VARCHAR2,
                               v_objcode NUMBER DEFAULT NULL)
    RETURN exec_object_ref IS
    v_cursor exec_object_ref;
  BEGIN
  --RAISE_ERROR('v_type'||v_type||'v_syscode'||v_syscode||'v_objcode'||v_objcode);
    IF v_type = 'RPT' THEN
    OPEN v_cursor FOR
    SELECT DISTINCT 'RPT',RPT_CODE,RPT_DESCRIPTION,SYS_REPORTS_PATH
    FROM TQC_SYSTEM_REPORTS,TQC_SYSTEMS
    WHERE RPT_SYS_CODE = SYS_CODE
    AND RPT_DESCRIPTION IS NOT NULL
    AND SYS_CODE = NVL(v_sysCode,SYS_CODE)
    AND RPT_CODE = NVL(v_objCode,RPT_CODE)
    ORDER BY RPT_DESCRIPTION;
    RETURN v_cursor;
    ELSIF v_type = 'W' THEN
    OPEN v_cursor FOR
    SELECT 'W',MAX(JBPM4_DEPLOYPROP.DBID_),SPRC_PROCESS,OBJNAME_
    FROM JBPM4_DEPLOYPROP,TQC_SYS_PROCESSES,JBPM4_VARIABLE
    WHERE SPRC_JPDL_DESC = OBJNAME_
    AND JBPM4_VARIABLE.DBID_(+) = JBPM4_DEPLOYPROP.DBID_
    AND SPRC_SYS_CODE = NVL(v_sysCode,SPRC_SYS_CODE)
    AND JBPM4_DEPLOYPROP.DBID_ = NVL(v_objCode,JBPM4_DEPLOYPROP.DBID_)
    GROUP BY OBJNAME_,SPRC_SYS_CODE,SPRC_PROCESS
    ORDER BY SPRC_PROCESS;
    RETURN v_cursor;
    ELSIF v_type = 'J' THEN
    OPEN v_cursor FOR
    SELECT 'J',TSR_CODE,TSR_SHT_DESC,TSR_FUNCTION
    FROM TQC_SYSTEM_ROUTINES
    WHERE TSR_SYS_CODE = NVL(v_sysCode,TSR_SYS_CODE)
    AND TSR_CODE = NVL(v_objCode,TSR_CODE)
    ORDER BY TSR_SHT_DESC;
    RETURN v_cursor;
    END IF;
    RETURN(v_cursor);
  END;

  FUNCTION gethouseholds RETURN households_ref IS
    v_cursor households_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT hh_id, hh_name, usr_username, hh_date_created, hh_category
        FROM tqc_households, tqc_users
       WHERE hh_created_by = usr_code;
  
    RETURN v_cursor;
  END;

  FUNCTION gethouseholdmembers(v_hhid tqc_households.hh_id%TYPE)
    RETURN householddtls_ref IS
    v_cursor householddtls_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT hh_name,
             hh_id,
             hhd_clnt_code,
             clnt_sht_desc,
             clnt_name,
             clnt_other_names
        FROM tqc_households, tqc_household_dtls, tqc_clients
       WHERE hhd_hh_id = hh_id
         AND hhd_clnt_code = clnt_code
         AND hhd_id = v_hhid;
  
    RETURN v_cursor;
  END;

 FUNCTION getDirectDebitsReport (v_ddCode    NUMBER,v_query  NUMBER) RETURN direct_debits_ref IS
    v_cursor     direct_debits_ref;
BEGIN
   --raise_error('v_ddCode='||v_ddCode);
    OPEN v_cursor FOR
    SELECT DDD_CODE,NVL(DDD_POL_POLICY_NO,DDD_POL_PROPOSAL_NO)policy,TO_CHAR(DDH_CLNT_BANK_ACC_NO) BANK_ACC_NO,nvl(BNK_KBA_CODE||''||BBR_KBA_CODE,DDH_BBR_REF_CODE) FORWARDING_BNK,TO_CHAR(DDD_AMOUNT) AMOUNT,
                DDH_CLNT_NAME CLNT_NAME,to_char(sysdate,'MONTH RRRR ')||'PREM' narrattive ,ORG_NAME ,DDH_BBR_REF_CODE,
                DDD_PREM_DUE_DATE,DECODE(DDD_POL_FREQ_PYMNT,'M','MONTHLY','Q','QUARTERLY','S','SEMI-ANNUALLY','A','ANNUALLY','F','SINGLE PREMIUM')DDD_POL_FREQ_PYMNT,DD_REF_NO, 
                (SELECT bnk_bank_name || ' - ' || bbr_branch_name
                FROM tqc_bank_branches, tqc_banks
               WHERE bbr_bnk_code = bnk_code
                 AND bbr_code = dd_bbr_code) Bank_branch,
                 TQC_SETUPS_CURSOR.get_debit_day(DDD_POL_TYPE, DECODE(DDD_POL_TYPE, 'PL', DDD_POL_CODE, 'MKT', DDD_PPR_CODE)) Debit_Day
                FROM TQC_DIRECT_DEBIT, TQC_DIRECT_DEBIT_HEADER, TQC_DIRECT_DEBIT_DETAIL, TQC_ORGANIZATIONS,
                TQC_SYSTEMS,TQC_BANK_BRANCHES,TQC_BANKS--,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
                WHERE DDH_DD_CODE = DD_CODE
                AND DDH_CODE = DDD_DDH_CODE
                AND DDD_SYS_CODE = SYS_CODE 
                AND SYS_ORG_CODE = ORG_CODE
                AND BNK_CODE=DDH_BBR_BNK_CODE
                AND BBR_CODE=DDH_CLNT_BBR_CODE
                AND BBR_BNK_CODE=BNK_CODE
                AND DD_CODE = v_ddCode;
    RETURN v_cursor;
END;
  FUNCTION getdirectdebitsdata(v_ddcode   NUMBER,
                               v_refno    VARCHAR2,
                               v_bankcode VARCHAR2,
                               v_bbrcode  VARCHAR2,
                               v_query    NUMBER) RETURN debit_data_ref IS
    v_cursor    debit_data_ref;
    v_report_no VARCHAR2(10);
  BEGIN
    BEGIN
      SELECT dd_file_no
        INTO v_report_no
        FROM tqc_direct_debit
       WHERE dd_code = v_ddcode
         AND dd_file_generated = 'Y';
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        BEGIN
          SELECT NVL(MAX(dd_file_no), 0) + 1
            INTO v_report_no
            FROM tqc_direct_debit
           WHERE dd_ref_no = v_refno
             AND dd_bnk_code = v_bankcode
             AND dd_bbr_code = v_bbrcode
             AND dd_file_generated = 'Y';
        
          v_report_no := '0' || v_report_no;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_report_no := '0' || 1;
        END;
    END;
  
    IF v_report_no IS NULL THEN
      v_report_no := '0' || 1;
    END IF;
  
    OPEN v_cursor FOR
      SELECT '0111' || SUBSTR(ddh_clnt_code, 1, 15) || '#' || ddd_amount || '#' ||
             TO_CHAR(dd_book_date, 'DD/MM/RRRR') || '#' ||
             SUBSTR(dd_acnt_no, 1, 10) || '#' || 'KES' || '#' ||
             SUBSTR(ddh_clnt_code, 1, 35) || '#' || org_name || '#' ||
             org_name || '#' || SUBSTR(ddh_clnt_bank_acc_no, 1, 13) || '#' ||
             SUBSTR(ddh_clnt_name, 1, 35) || '#' || 'MADISON' || '#' ||
             org_name || '#' || SUBSTR(ddh_bbr_ref_code, 1, 5) || '#' ||
             org_name || '#' || SUBSTR(ddh_clnt_bank_acc_no, 1, 13) dd_rec,
             v_report_no
        FROM tqc_direct_debit,
             tqc_direct_debit_header,
             tqc_direct_debit_detail,
             tqc_organizations,
             tqc_systems
       WHERE ddh_dd_code = dd_code
         AND ddh_code = ddd_ddh_code
         AND ddd_sys_code = sys_code
         AND sys_org_code = org_code
         AND dd_code = v_ddcode
         AND v_query = 1
      UNION ALL
      SELECT '00' || '40' || LPAD(SUBSTR(ddd_amount, 1, 13), 13, 0) || '0' ||
             SUBSTR(bnk_kba_code, 1, 2) || '' ||
             LPAD(SUBSTR(bbr_kba_code, 1, 3), 3, 0) || '' ||
             LPAD(SUBSTR(ddh_clnt_bank_acc_no, 1, 15), 15, 0) || '' ||
             SUBSTR(direct_debit_pkg.forwarding_bnk_code(dd_bnk_code), 1, 2) || '' ||
             LPAD(SUBSTR(direct_debit_pkg.forwarding_bnk_bbr_code(dd_bbr_code),
                         1,
                         3),
                  3,
                  0) || '' || LPAD(SUBSTR(dd_acnt_no, 1, 15), 15, 0) || '' ||
             SUBSTR(direct_debit_pkg.forwarding_bnk_code(dd_bnk_code), 1, 2) || '' ||
             LPAD(SUBSTR(direct_debit_pkg.forwarding_bnk_bbr_code(dd_bbr_code),
                         1,
                         3),
                  3,
                  0) || '' || SUBSTR(sys_kba_code, 1, 4) || '' ||
             RPAD(SUBSTR(dd_ref_no, 1, 15), 15) || '' ||
             RPAD(SUBSTR(NVL(ddd_pol_policy_no, ddd_pol_proposal_no), 1, 20),
                  20) || '00000000000' dd_rec,
             v_report_no
        FROM tqc_direct_debit,
             tqc_direct_debit_header,
             tqc_direct_debit_detail,
             tqc_organizations,
             tqc_systems,
             tqc_bank_branches,
             tqc_banks --,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
       WHERE ddh_dd_code = dd_code
         AND ddh_code = ddd_ddh_code
         AND ddd_sys_code = sys_code
         AND sys_org_code = org_code
         AND bnk_code = ddh_bbr_bnk_code
         AND bbr_code = ddh_clnt_bbr_code
         AND bbr_bnk_code = bnk_code
         AND dd_code = v_ddcode
         AND v_query = 2
      UNION ALL (SELECT DISTINCT LPAD(('NGR-' ||
                                      TO_CHAR(SYSDATE, 'DDMMRRRR') || '-' ||
                                      v_report_no),
                                      (LENGTH(NVL(ddd_pol_policy_no,
                                                  ddd_pol_proposal_no) || ',' ||
                                              ddh_clnt_bank_acc_no || ',' ||
                                              direct_debit_pkg.forwarding_bnk_code(dd_bnk_code) || '' ||
                                              direct_debit_pkg.forwarding_bnk_bbr_code(dd_bbr_code) || ',' ||
                                              ddd_amount || ',' ||
                                              ddh_clnt_name || ',' ||
                                              TO_CHAR(SYSDATE, 'MONTH RRRR ') ||
                                              'PREM,' || org_name))) dd_details,
                                 v_report_no
                   FROM tqc_direct_debit,
                        tqc_direct_debit_header,
                        tqc_direct_debit_detail,
                        tqc_organizations,
                        tqc_systems,
                        tqc_bank_branches,
                        tqc_banks
                 --,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
                  WHERE ddh_dd_code = dd_code
                    AND ddh_code = ddd_ddh_code
                    AND ddd_sys_code = sys_code
                    AND sys_org_code = org_code
                    AND bnk_code = ddh_bbr_bnk_code
                    AND bbr_code = ddh_clnt_bbr_code
                    AND bbr_bnk_code = bnk_code
                    AND dd_code = v_ddcode
                    AND v_query = 3
                    AND ROWNUM = 1
                 UNION ALL
                 SELECT NVL(ddd_pol_policy_no, ddd_pol_proposal_no) || ',' ||
                        ddh_clnt_bank_acc_no || ',' || bnk_kba_code || '' ||
                        bbr_kba_code || ',' || ddd_amount || ',' ||
                        ddh_clnt_name || ',' ||
                        TO_CHAR(SYSDATE, 'MONTH RRRR ') || 'PREM,' ||
                        org_name dd_details,
                        v_report_no
                   FROM tqc_direct_debit,
                        tqc_direct_debit_header,
                        tqc_direct_debit_detail,
                        tqc_organizations,
                        tqc_systems,
                        tqc_bank_branches,
                        tqc_banks --,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
                  WHERE ddh_dd_code = dd_code
                    AND ddh_code = ddd_ddh_code
                    AND ddd_sys_code = sys_code
                    AND sys_org_code = org_code
                    AND bnk_code = ddh_bbr_bnk_code
                    AND bbr_code = ddh_clnt_bbr_code
                    AND bbr_bnk_code = bnk_code
                    AND dd_code = v_ddcode
                    AND v_query = 3);
  
    RETURN v_cursor;
  END;

  FUNCTION gettownswithzipcodebystate(v_state_code IN NUMBER)
    RETURN towns_with_zip_ref IS
    v_cursor towns_with_zip_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT twn_code,
             twn_cou_code,
             twn_sht_desc,
             twn_name,
             twn_sts_code,
             pst_desc,
             pst_zip_code
        FROM tqc_towns, tqc_postal_codes
       WHERE twn_code = pst_twn_code(+)
         AND twn_sts_code = v_state_code
       ORDER BY twn_name;
  
    RETURN v_cursor;
  END;

  FUNCTION get_ecm_setups RETURN ecm_setups_ref IS
    v_cursor ecm_setups_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT est_code,
             est_ecm_url,
             est_ecm_username,
             est_ecm_password,
             est_sys_code,
             est_root_folder,
             sys_name
        FROM tqc_ecm_setups, tqc_systems
       WHERE est_sys_code = sys_code;
  
    RETURN v_cursor;
  END;

  FUNCTION get_system_ecm_setups(v_sys_code NUMBER) RETURN ecm_setups_ref IS
    v_cursor ecm_setups_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT est_code,
             est_ecm_url,
             est_ecm_username,
             est_ecm_password,
             est_sys_code,
             est_root_folder,
             sys_name
        FROM tqc_ecm_setups, tqc_systems
       WHERE est_sys_code = sys_code
         AND est_sys_code = v_sys_code;
  
    RETURN v_cursor;
  END;

  FUNCTION get_ecm_systems RETURN ecm_systems_ref IS
    v_cursor ecm_systems_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT sys_code, sys_sht_desc, sys_name FROM tqc_systems;
  
    RETURN(v_cursor);
  END;

  FUNCTION get_ecm_processes(v_sys_code NUMBER) RETURN ecm_processes_ref IS
    v_cursor ecm_processes_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT sprc_code, sprc_process
        FROM tqc_sys_processes
       WHERE sprc_sys_code = v_sys_code;
  
    RETURN(v_cursor);
  END;

  FUNCTION get_system_reports(v_sys_code NUMBER) RETURN system_reports_ref IS
    v_cursor system_reports_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT rpt_code, rpt_name, rpt_description
        FROM tqc_system_reports
       WHERE rpt_sys_code = v_sys_code;
  
    RETURN(v_cursor);
  END;

  FUNCTION get_process_reports(v_proc_code NUMBER) RETURN sys_reports_ref IS
    v_cursor sys_reports_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT sprr_code,
             sprr_rpt_code,
             sprr_sprc_code,
             sprr_desc,
             rpt_name,
             rpt_description,
             sprr_type,
             SDT_CONTENT_NAME,
             SDT_CODE,SDT_CONTENT_TYPE
        FROM tqc_sys_process_reports, tqc_system_reports,tqc_sys_doc_types
       WHERE sprr_rpt_code = rpt_code
         AND SPRR_SDT_CODE=SDT_CODE(+)
         AND sprr_sprc_code = v_proc_code;
  
    RETURN(v_cursor);
  END;

  FUNCTION get_process_report(v_proc_area VARCHAR2, v_sys_code NUMBER)
    RETURN sys_reports_ref IS
    v_cursor    sys_reports_ref;
    v_proc_code NUMBER;
  BEGIN
  
    SELECT SPRC_CODE
      INTO v_proc_code
      FROM TQC_SYS_PROCESSES
     WHERE SPRC_SHT_DESC = v_proc_area
       AND SPRC_SYS_CODE = v_sys_code;
  
    OPEN v_cursor FOR
      SELECT sprr_code,
             sprr_rpt_code,
             sprr_sprc_code,
             sprr_desc,
             rpt_name,
             rpt_description,
             sprr_type,
             SDT_CONTENT_NAME,
             SDT_CODE,SDT_CONTENT_TYPE
       FROM tqc_sys_process_reports, tqc_system_reports,tqc_sys_doc_types
       WHERE sprr_rpt_code = rpt_code
       AND SPRR_SDT_CODE=SDT_CODE(+)
         AND sprr_sprc_code = v_proc_code;
  
    RETURN(v_cursor);
  END;

  FUNCTION get_ecm_doc_types RETURN ecm_doc_types_ref IS
    v_cursor ecm_doc_types_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT sdt_code, sdt_content_name, sdt_content_desc,SDT_CONTENT_TYPE
        FROM tqc_sys_doc_types;
  
    RETURN(v_cursor);
  END;

  FUNCTION get_content_metadata(v_sprr_code NUMBER)
    RETURN content_metadata_ref IS
    v_cursor content_metadata_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT ddm_code, ddm_sdt_code, ddm_name, ddm_desc
        FROM tqc_dms_doc_metadata
       WHERE ddm_sdt_code = v_sprr_code;
  
    RETURN(v_cursor);
  END;

  FUNCTION get_branches_details(v_bbrcode number) RETURN branch_data_ref IS
    v_cursor branch_data_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT BBB_CODE,
             BBB_BRN_CODE,
             BBB_BRN_SHT_DESC,
             BBB_BRN_REG_CODE,
             BBB_BRN_NAME,
             BBB_BRN_PHY_ADDRS,
             BBB_BBR_CODE,
             BBB_BBR_BNK_CODE,
             BBB_BBR_BRANCH_NAME,
             BBB_BBR_SHT_DESC,
             BBB_BBR_PHYSICAL_ADDRS
        FROM TQC_BANK_BRANCHES_BRANCHES
       where BBB_BBR_CODE = v_bbrcode;
    RETURN(v_cursor);
  END;
  PROCEDURE all_branches(v_all_branches_ref OUT all_branches_ref) IS
  BEGIN
    OPEN v_all_branches_ref FOR
      SELECT brn_code,
             brn_sht_desc,
             brn_reg_code,
             brn_name,
             brn_phy_addrs,
             brn_email_addrs,
             brn_post_addrs,
             brn_twn_code,
             brn_cou_code,
             brn_contact,
             brn_manager,
             brn_tel,
             brn_fax,
             brn_gen_pol_clm,
             brn_bns_code,
             brn_agn_code,
             brn_post_level,
             agn_name(brn_agn_code) manager
        FROM tqc_branches;
  END all_branches;
  FUNCTION all_bank__branches(v_branch_name VARCHAR2)
    RETURN all_bank_branches_ref IS
    v_all_bankbranches_ref all_bank_branches_ref;
  BEGIN
    OPEN v_all_bankbranches_ref FOR
      SELECT BBR_CODE,
             BBR_BNK_CODE,
             BBR_BRANCH_NAME,
             BBR_REMARKS,
             BBR_SHT_DESC,
             BBR_REF_CODE,
             BBR_EFT_SUPPORTED,
             BBR_DD_SUPPORTED,
             BBR_DATE_CREATED,
             BBR_CREATED_BY,
             BBR_PHYSICAL_ADDRS
        FROM TQC_BANK_BRANCHES
      WHERE BBR_BRANCH_NAME = v_branch_name;
    RETURN v_all_bankbranches_ref;
  END;
  PROCEDURE allbankbranches(v_bankbranchref OUT allbankbranchesref) IS
  BEGIN
    OPEN v_bankbranchref FOR
      SELECT BBR_CODE,
             BBR_BNK_CODE,
             BBR_BRANCH_NAME,
             BBR_REMARKS,
             BBR_SHT_DESC,
             BBR_REF_CODE,
             BBR_EFT_SUPPORTED,
             BBR_DD_SUPPORTED,
             BBR_DATE_CREATED,
             BBR_CREATED_BY,
             BBR_PHYSICAL_ADDRS
        FROM TQC_BANK_BRANCHES;
  END allbankbranches;
  FUNCTION selectallMessages(v_sms_sys_code VARCHAR2) return allmessagesref IS
    v_messagesref allmessagesref;
  BEGIN
    OPEN v_messagesref FOR
      SELECT DISTINCT SMS_CODE,
             SMS_SYS_CODE,
             TSM_DESC,
             SMS_CLNT_CODE,
             SMS_AGN_CODE,
             SMS_POL_NO,
             SMS_POL_CODE,
             SMS_CLM_NO,
             SMS_TEL_NO,
             SMS_MSG,
             SMS_STATUS,
             SMS_PREPARED_BY,
             SMS_SEND_DATE,
            NULL POL_CURRENT_STATUS,CLNT_NAME,COU_CODE,COU_ZIP_CODE
        FROM TQC_SMS_MESSAGES,TQC_CLIENTS,TQC_SYSTEM_MODULES,TQC_COUNTRIES
       where 
       --SMS_POL_NO = POL_POLICY_NO
          SMS_STATUS = 'D'
          AND SMS_SYS_MODULE = TSM_SHT_DESC(+)
          AND CLNT_COU_CODE = COU_CODE(+)
         AND SMS_SYS_CODE = v_sms_sys_code
         and SMS_CLNT_CODE=CLNT_CODE
         ORDER BY TSM_DESC;
    return v_messagesref;
  END;
  FUNCTION getReservedWords(v_sysCode NUMBER) RETURN reserved_words_ref IS
    v_cursor reserved_words_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSRW_CODE, TSRW_SYS_CODE, TSRW_TSRC_CODE, TSRW_TYPE, TSRW_EDITABLE,TSRC_NAME,TSRW_NAME,TSRW_DESC,
    TSRC_VALIDITY,TSRC_USR_CODE,USR_TYPE,USR_EMAIL
    FROM TQC_SYS_RESERVED_WORDS,TQC_SERV_REQ_CAT,TQC_USERS
    WHERE TSRW_TSRC_CODE = TSRC_CODE(+)
    AND TSRC_USR_CODE = USR_CODE(+)
    AND TSRW_SYS_CODE = v_sysCode;
    RETURN v_cursor;
END;
FUNCTION getSystemProcesses(v_sysCode       TQC_SYS_PROCESSES.SPRC_SYS_CODE%TYPE)RETURN  sys_processes_ref IS
    v_cursor    sys_processes_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT  MAX(DEPLOYMENT_) DEPLOYMENT , JSD_SYS_CODE, JSD_JPDL_NAME
    FROM JBPM4_SYS_DEPLOYMENTS ,JBPM4_DEPLOYPROP
    WHERE JSD_SYS_CODE = v_sysCode
    AND JSD_JPDL_NAME = OBJNAME_
    GROUP BY JSD_SYS_CODE, JSD_JPDL_NAME
    UNION 
    SELECT  MAX(DEPLOYMENT_) DEPLOYMENT , JSD_SYS_CODE, JSD_JPDL_NAME
    FROM JBPM4_SYS_DEPLOYMENTS ,TQ_GIS.JBPM4_DEPLOYPROP_gis
    WHERE jSD_JPDL_NAME = OBJNAME_
    AND JSD_SYS_CODE = v_sysCode
    GROUP BY JSD_SYS_CODE, JSD_JPDL_NAME
    ORDER BY JSD_JPDL_NAME;
    RETURN v_cursor;
END;
FUNCTION getEscalations(v_sysCode   TQC_SYS_ESCALATION_LEVELS.TSEL_JSD_SYS_CODE%TYPE,
                        v_jpdlName  TQC_SYS_ESCALATION_LEVELS.TSEL_JSD_JPDL_NAME%TYPE,
                        v_activity  TQC_SYS_ESCALATION_LEVELS.TSEL_ACTIVITY_NAME%TYPE,
                        v_level     TQC_SYS_ESCALATION_LEVELS.TSEL_LEVEL%TYPE DEFAULT NULL) RETURN  escalations_ref IS
    v_cursor   escalations_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSEL_CODE,TSEL_JSD_SYS_CODE,TSEL_JSD_JPDL_NAME,TSEL_ACTIVITY_NAME,TSEL_LEVEL,TSEL_ASSIGNEE,a.USR_USERNAME,
    TSEL_DURATION,TSEL_CC,b.USR_USERNAME
    FROM TQC_SYS_ESCALATION_LEVELS,TQC_USERS a,TQC_USERS b
    WHERE TSEL_ASSIGNEE = a.USR_CODE
    AND TSEL_JSD_SYS_CODE = v_sysCode
    AND TSEL_JSD_JPDL_NAME = v_jpdlName
    AND TSEL_LEVEL = NVL(v_level,TSEL_LEVEL)
    AND TSEL_CC = b.USR_CODE(+)
    AND TSEL_ACTIVITY_NAME = v_activity;
    RETURN v_cursor;
END; 
 FUNCTION checkif_losstime_required RETURN checkif_losstime_required_ref IS
    v_cursor checkif_losstime_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT gin_parameters_pkg.get_param_varchar('LOSS_DATE_REQUIRED')
        FROM DUAL;
  
    RETURN v_cursor;
  END;
   FUNCTION checkif_Occupation_required RETURN checkif_losstime_required_ref IS
    v_cursor checkif_losstime_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT tqc_parameters_pkg.get_param_varchar('OCCUPATION')
        FROM DUAL;
  
    RETURN v_cursor;
  END;
   PROCEDURE select_Parent_Agency(v_cursor OUT select_Parent_Agency_ref,v_act_Code in NUMBER) IS
  -- v_cursor select_Parent_Agency_ref;
  BEGIN
  --RAISE_ERROR('v_act_Code'||v_act_Code);
  IF v_act_Code=25 THEN
    OPEN v_cursor FOR
      SELECT AGN_CODE, AGN_SHT_DESC, AGN_NAME
        FROM tqc_agencies
        WHERE AGN_ACT_CODE IN (2) ;
  ELSIF v_act_Code IN(4,7) THEN
    OPEN v_cursor FOR
      SELECT AGN_CODE, AGN_SHT_DESC, AGN_NAME
        FROM tqc_agencies
        WHERE AGN_ACT_CODE IN (5,3) ;
  END IF;
 END select_Parent_Agency;
   PROCEDURE select_subAgents_datails(v_cursor OUT select_subAgents_datails_ref,v_agn_code IN NUMBER) IS
   --v_cursor select_subAgents_datails_ref;
  BEGIN
  --RAISE_ERROR('v_agn_code'||v_agn_code);
    OPEN v_cursor FOR
      SELECT AGN_CODE, AGN_ACT_CODE, AGN_SHT_DESC, AGN_NAME,
             AGN_PHYSICAL_ADDRESS, AGN_POSTAL_ADDRESS, 
             AGN_EMAIL_ADDRESS, AGN_SUBAGENT, AGN_MAIN_AGN_CODE,ACT_ACCOUNT_TYPE
        FROM tqc_agencies,tqc_account_types
        WHERE AGN_MAIN_AGN_CODE=v_agn_code
        AND AGN_ACT_CODE=ACT_CODE;
  END select_subAgents_datails;
  FUNCTION checkif_SerialNo_required RETURN checkif_SerialNo_required_ref IS
    v_cursor checkif_SerialNo_required_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT gin_parameters_pkg.get_param_varchar('SERIAL_NUMBER_REQUIRED')
        FROM DUAL;
  
    RETURN v_cursor;
  END;
  FUNCTION getDefaultSmsSettings RETURN sms_settings_ref IS
    v_cursor sms_settings_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSS_DESC, TSS_URL, TSS_USERNAME, TSS_PASSWORD, TSS_SOURCE,TSS_CODE,TSS_DEFAULT
    FROM TQC_SYSTEM_SMS
    WHERE TSS_DEFAULT = 'Y';
    RETURN v_cursor;
END;

FUNCTION getPendingSms RETURN pending_sms_ref IS
    v_cursor    pending_sms_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT SMS_CODE,SMS_TEL_NO,SMS_MSG 
    FROM TQC_SMS_MESSAGES
    WHERE SMS_STATUS != 'OK';
    RETURN v_cursor;
END;
FUNCTION getSmsSettings RETURN sms_settings_ref IS
    v_cursor sms_settings_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSS_DESC, TSS_URL, TSS_USERNAME, TSS_PASSWORD, TSS_SOURCE,TSS_CODE,TSS_DEFAULT
    FROM TQC_SYSTEM_SMS;
    RETURN v_cursor;
END;
FUNCTION getGroupedUsersEmails(v_GUSR_GRP_USR_CODE NUMBER) RETURN email_ref
IS
 mailList email_ref;
 
BEGIN
OPEN mailList FOR
SELECT USR_EMAIL FROM TQC_USERS
WHERE  USR_CODE IN (SELECT GUSR_USR_CODE FROM TQC_GROUP_USERS WHERE GUSR_GRP_USR_CODE=v_GUSR_GRP_USR_CODE);

RETURN mailList;
END;
FUNCTION getMarketers RETURN Marketers_ref IS
    v_cursor Marketers_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT AGN_CODE, AGN_ACT_CODE, AGN_SHT_DESC, AGN_NAME
    FROM TQC_AGENCIES
   WHERE AGN_ACT_CODE=10;
    RETURN v_cursor;
END;
FUNCTION getwebClientsBranches(v_tcb_clnt_code IN NUMBER) RETURN webClientsBranchesRef IS
    v_cursor webClientsBranchesRef;
BEGIN
    OPEN v_cursor FOR
    SELECT TCB_CODE, TCB_CLNT_CODE, TCB_SHT_DESC, TCB_NAME
    FROM TQC_CLIENT_BRANCHES
    WHERE TCB_CLNT_CODE=v_tcb_clnt_code;
   RETURN v_cursor;
END;
FUNCTION getUnassignedBranches(v_tcb_clnt_code IN NUMBER)  RETURN webassignBranchRef IS
    v_cursor webassignBranchRef;
BEGIN
    OPEN v_cursor FOR
    SELECT TCB_CODE, TCB_CLNT_CODE, TCB_SHT_DESC, TCB_NAME
    FROM TQC_CLIENT_BRANCHES
     WHERE TCB_CODE NOT IN(SELECT TCUB_TCB_CODE FROM TQC_CLIENT_USR_BRANCHES)
    AND TCB_CLNT_CODE=v_tcb_clnt_code;
   RETURN v_cursor;
END;
FUNCTION getassignedBranches(v_tcb_clnt_code IN NUMBER) RETURN webassignBranchRef IS
    v_cursor webassignBranchRef;
BEGIN
  OPEN v_cursor FOR
    SELECT TCB_CODE, TCB_CLNT_CODE, TCB_SHT_DESC, TCB_NAME
    FROM TQC_CLIENT_BRANCHES
    WHERE TCB_CODE  IN(SELECT TCUB_TCB_CODE FROM TQC_CLIENT_USR_BRANCHES) 
     AND TCB_CLNT_CODE=v_tcb_clnt_code;
    RETURN v_cursor;
END;
FUNCTION getDefaultBranches RETURN webClientsBranchesRef IS
    v_cursor webClientsBranchesRef;
BEGIN
    OPEN v_cursor FOR
    SELECT TCB_CODE, TCB_CLNT_CODE, TCB_SHT_DESC, TCB_NAME
    FROM TQC_CLIENT_BRANCHES
    where  TCB_CODE IN(SELECT  TCUB_TCB_CODE FROM TQC_CLIENT_USR_BRANCHES);
   RETURN v_cursor;
END;
FUNCTION getwebProducts RETURN webProductsRef IS
    v_cursor webProductsRef;
BEGIN
    OPEN v_cursor FOR
    SELECT TWP_CODE, TWP_TWPC_CODE, TWP_PROD_CODE, TWP_PROD_DESC,PRO_DESC
    FROM   TQC_WEB_PRODUCTS,GIN_PRODUCTS
    WHERE  TWP_PROD_CODE=PRO_CODE
    UNION 
    SELECT TWP_CODE, TWP_TWPC_CODE, TWP_PROD_CODE, TWP_PROD_DESC,PROD_DESC
    FROM   TQC_WEB_PRODUCTS,LMS_PRODUCTS
    WHERE  TWP_PROD_CODE=PROD_CODE;
    RETURN v_cursor;
END;
FUNCTION getWebUsers RETURN WebUsersRef IS
    v_cursor WebUsersRef;
BEGIN
    OPEN v_cursor FOR
    SELECT USR_CODE, USR_USERNAME, USR_NAME,USR_STATUS
    FROM TQC_USERS,TQC_SYS_ROLES,TQC_USER_SYSTEMS
    WHERE USYS_USR_CODE=USR_CODE
     AND USYS_SYS_CODE=SRLS_SYS_CODE
     AND SRLS_SHT_DESC='QT'
    AND USR_STATUS='A';
   RETURN v_cursor;
END;
FUNCTION getWebProductDetails(v_twpd_clnt_code IN NUMBER) RETURN WebProductsDtlsRef IS
    v_cursor WebProductsDtlsRef;
BEGIN
    OPEN v_cursor FOR
    SELECT TWPD_CLNT_CODE, TWPD_TWP_CODE,CLNT_NAME, TWP_PROD_DESC, TWPD_USR_CODE, TWPD_USERNAME, TWPD_DR_LIMIT, TWPD_CR_LIMIT,
    TWPD_POLICY_USE, TWPD_ENDOS_USE,PRO_DESC,CLNA_CODE, CLNA_SHT_DESC
    FROM TQC_WEB_PRODUCT_DETAILS,TQC_CLIENTS,TQC_WEB_PRODUCTS,GIN_PRODUCTS,TQC_CLIENT_ACCOUNTS
    WHERE TWPD_CLNT_CODE=CLNT_CODE
    AND TWPD_TWP_CODE=TWP_CODE
    AND TWP_PROD_CODE=PRO_CODE
    and CLNT_CODE=CLNA_CLNT_CODE
    AND TWPD_CLNT_CODE=v_twpd_clnt_code;
   RETURN v_cursor;
END;
FUNCTION getAllSystems RETURN AllSystems_ref IS
    v_cursor AllSystems_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT SYS_CODE, SYS_SHT_DESC, SYS_NAME
    FROM TQC_SYSTEMS;
   RETURN v_cursor;
END;



--FUNCTION get_dispatch_docs(v_batch NUMBER,v_clnt_code NUMBER,v_agn_code NUMBER) RETURN dispatch_docs_ref IS
--    v_cursor dispatch_docs_ref;
--BEGIN
--    OPEN v_cursor FOR
--    SELECT DOCR_ID, DOCR_DOC_NAME, DOCR_DOC_URL, DOCR_DOC_AUTHOR, DOCR_DOC_DESC, DOCR_CLNT_CODE, DOCR_POL_POLICY_NO, 
--    DOCR_CLAIM_NO, DOCR_QUOT_CODE, DOCR_LEVEL, DOCR_SYS_CODE, DOCR_POL_BATCH_NO, DOCR_DATE_CREATED, DOCR_AGN_CODE,
--    DOCR_DISPATCHED, DOCR_DISPATCH_DT, DOCR_DISPATCHABLE
--    FROM TQC_DOCUMENTS_REGISTER
--    WHERE DOCR_POL_BATCH_NO=NVL(v_batch,DOCR_POL_BATCH_NO)
--    AND DOCR_CLNT_CODE=NVL(v_clnt_code,DOCR_CLNT_CODE)
--    AND  DOCR_AGN_CODE=NVL(v_agn_code,DOCR_AGN_CODE)
--    AND DOCR_SYS_CODE=37;
--   RETURN v_cursor;
--END;
FUNCTION get_dispatch_docs(v_batch NUMBER,v_clnt_code NUMBER,v_agn_code NUMBER) RETURN dispatch_docs_ref IS
    v_cursor dispatch_docs_ref;
BEGIN

    OPEN v_cursor FOR
    SELECT DOCR_ID, DOCR_DOC_NAME, DOCR_DOC_URL, DOCR_DOC_AUTHOR, DOCR_DOC_DESC, DOCR_CLNT_CODE, DOCR_POL_POLICY_NO, 
    DOCR_CLAIM_NO, DOCR_QUOT_CODE, DOCR_LEVEL, DOCR_SYS_CODE, DOCR_POL_BATCH_NO, DOCR_DATE_CREATED, DOCR_AGN_CODE,
    DOCR_DISPATCHED, DOCR_DISPATCH_DT, DOCR_DISPATCHABLE
    FROM TQC_DOCUMENTS_REGISTER
    WHERE DOCR_POL_BATCH_NO=v_batch
 AND DOCR_CLNT_CODE=NVL(v_clnt_code,DOCR_CLNT_CODE)
  --AND  NVL(DOCR_AGN_CODE,-2000)=NVL(NVL(v_agn_code,-2000),NVL(DOCR_AGN_CODE,-2000))
    AND DOCR_SYS_CODE=37;
   RETURN v_cursor;
   end;
  FUNCTION getMainReservedWord(v_sysCode NUMBER) RETURN reserved_words_ref IS
    v_cursor reserved_words_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSRW_CODE, TSRW_SYS_CODE, TSRW_TSRC_CODE, TSRW_TYPE, TSRW_EDITABLE,TSRC_NAME,TSRW_NAME,TSRW_DESC,
    TSRC_VALIDITY,TSRC_USR_CODE,USR_TYPE,USR_EMAIL
    FROM TQC_SYS_RESERVED_WORDS,TQC_SERV_REQ_CAT,TQC_USERS
    WHERE TSRW_TSRC_CODE = TSRC_CODE(+)
    AND TSRC_USR_CODE = USR_CODE(+)
    AND TSRW_SYS_CODE = NVL(v_sysCode,TSRW_SYS_CODE)
    AND TSRW_EDITABLE = 'N';
    RETURN v_cursor;
END;    
 FUNCTION get_sms_template RETURN sms_templates_ref IS
    v_cursor sms_templates_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT msgt_code,
             msgt_sht_desc,
             msgt_msg,
             msgt_sys_code,
             msgt_sys_module,
             msgt_type
        FROM tqc_msg_templates
       WHERE MSGT_TYPE='SMS';
    RETURN(v_cursor);
  END;
 FUNCTION get_email_template RETURN sms_templates_ref IS
    v_cursor sms_templates_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT msgt_code,
             msgt_sht_desc,
             msgt_msg,
             msgt_sys_code,
             msgt_sys_module,
             msgt_type
        FROM tqc_msg_templates
       WHERE MSGT_TYPE='EMAIL';
    RETURN(v_cursor);
  END;
   FUNCTION get_sms_template(V_msgt_sys_code IN NUMBER) RETURN sms_templates_ref IS
    v_cursor sms_templates_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT msgt_code,
             msgt_sht_desc,
             msgt_msg,
             msgt_sys_code,
             msgt_sys_module,
             msgt_type
        FROM tqc_msg_templates
       WHERE MSGT_TYPE='SMS';
    RETURN(v_cursor);
  END;
 FUNCTION get_email_template(V_msgt_sys_code IN NUMBER) RETURN sms_templates_ref IS
    v_cursor sms_templates_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT msgt_code,
             msgt_sht_desc,
             msgt_msg,
             msgt_sys_code,
             msgt_sys_module,
             msgt_type
        FROM tqc_msg_templates
       WHERE MSGT_TYPE='EMAIL';
    RETURN(v_cursor);
  END;
  FUNCTION getSystemReqDocs(v_system_code IN  NUMBER) RETURN req_doc_ref
IS
 req_doc req_doc_ref;
 
BEGIN
IF v_system_code = '37' THEN
OPEN req_doc FOR
SELECT RDOC_ID, RDOC_SHT_DESC, RDOC_DESC, RDOC_MANDTRY,
         RDOC_NB_DOC, RDOC_EN_DOC, RDOC_RN_DOC,
          RDOC_CERT_DOC, RDOC_CLM_LOP_DOC, 
          RDOC_CLM_PAY_DOC, RDOC_VALID_PRD,RQD_CODE
FROM GIN_REQRD_DOCUMENTS,TQC_REQUIRED_DOCS
WHERE RQC_RDOC_ID(+)=RDOC_ID;
RETURN req_doc;
ELSE
OPEN req_doc FOR
SELECT RDOC_ID, RDOC_SHT_DESC, RDOC_DESC, RDOC_MANDTRY,
         RDOC_NB_DOC, RDOC_EN_DOC, RDOC_RN_DOC,
          RDOC_CERT_DOC, RDOC_CLM_LOP_DOC, 
          RDOC_CLM_PAY_DOC, RDOC_VALID_PRD,RQD_CODE
FROM GIN_REQRD_DOCUMENTS,TQC_REQUIRED_DOCS
WHERE RQC_RDOC_ID(+)=RDOC_ID;
RETURN req_doc;
END IF;
END;

FUNCTION get_web_prod_user(v_prod_code NUMBER,v_clnt_code NUMBER) RETURN VARCHAR2 IS
    v_user VARCHAR2(30);
BEGIN
   select TWPD_USERNAME into v_user
   from tqc_web_products,tqc_web_product_details
   where TWPD_TWP_CODE = TWP_CODE
   AND TWP_PROD_CODE=v_prod_code
   AND TWPD_CLNT_CODE=v_clnt_code;
   RETURN v_user;
END;
FUNCTION get_available_prefixes RETURN mob_prefix_ref 
IS
v_cursor mob_prefix_ref;
BEGIN 
OPEN v_cursor FOR
SELECT MPTP_CODE, MPTP_MOB_NO_PREFIX, MPTP_MPT_CODE
FROM tqc_mob_pymnt_types_prefixes;
RETURN  v_cursor;
END;
FUNCTION getScheduledJobs RETURN ScheduledJobs_ref IS
    v_cursor ScheduledJobs_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT QT_JOB_NAME, QT_DESCRIPTION, QT_START_TIME, QT_END_TIME, QT_CRON_EXPRESSION
        FROM qrtz_triggers
        where QT_JOB_NAME IS NOT NULL
       and  QT_CRON_EXPRESSION IS NOT NULL
        AND QT_STATUS='A';
    RETURN(v_cursor);
  END;
  FUNCTION getPoliciesTwoMonthsRen RETURN PoliciesDueForRen_ref IS
    v_cursor PoliciesDueForRen_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT POL_POLICY_NO,
             POL_POLICY_COVER_TO,
            TO_DATE(POL_RENEWAL_DT,'DD/MM/RRRR') POL_RENEWAL_DT,
             CLNT_CODE, 
             CLNT_NAME||''||CLNT_OTHER_NAMES client, 
             CLNT_SMS_TEL,POL_AGNT_AGENT_CODE
        FROM gin_policies,tqc_clients
        where POL_PRP_CODE=CLNT_CODE
        AND POL_POLICY_COVER_TO=TO_DATE(SYSDATE,'DD/MM/RRRR')+60;
    RETURN(v_cursor);
  END;
  FUNCTION getPoliciesOneMonthsRen RETURN PoliciesDueForRen_ref IS
    v_cursor PoliciesDueForRen_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT POL_POLICY_NO,
             POL_POLICY_COVER_TO,
             TO_DATE(POL_RENEWAL_DT,'DD/MM/RRRR') POL_RENEWAL_DT,
             CLNT_CODE, 
             CLNT_NAME||''||CLNT_OTHER_NAMES client, 
             CLNT_SMS_TEL,POL_AGNT_AGENT_CODE
        FROM gin_policies,tqc_clients
        where POL_PRP_CODE=CLNT_CODE
        AND POL_POLICY_COVER_TO=TO_DATE(SYSDATE,'DD/MM/RRRR')+30;
    RETURN(v_cursor);
  END;
   FUNCTION getPoliciesTwoDaysRen RETURN PoliciesDueForRen_ref IS
    v_cursor PoliciesDueForRen_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT POL_POLICY_NO,
             POL_POLICY_COVER_TO,
             TO_DATE(POL_RENEWAL_DT,'DD/MM/RRRR') POL_RENEWAL_DT,
             CLNT_CODE, 
             CLNT_NAME||''||CLNT_OTHER_NAMES client, 
             CLNT_SMS_TEL,POL_AGNT_AGENT_CODE
        FROM gin_policies,tqc_clients
        where POL_PRP_CODE=CLNT_CODE
        AND POL_POLICY_COVER_TO=TO_DATE(SYSDATE,'DD/MM/RRRR')+2;
    RETURN(v_cursor);
  END;
  FUNCTION getagencysystems
   RETURN all_systems_ref
IS
   v_cursor   all_systems_ref;
BEGIN
   OPEN v_cursor FOR
      SELECT   -2000 sys_code, 'GIS AND LMS', 'GIS AND LMS' sys_name, NULL, NULL, NULL, NULL,
               NULL, NULL, NULL, NULL, NULL, NULL, NULL
          FROM DUAL
      UNION
      SELECT   sys_code, sys_sht_desc, sys_name, sys_main_form_name,
               sys_active, sys_dbase_username, sys_dbase_password,
               sys_dbase_string, sys_path, sys_org_code,
               sys_agn_main_frm_name, sys_kba_code, sys_signature_path,
               sys_template
          FROM tqc_systems
         WHERE sys_active <> 'N' AND sys_code <> 30 AND sys_code <> 26
      ORDER BY sys_code ASC;

   RETURN v_cursor;
END;
  FUNCTION getClientBirthDays RETURN ClientBirhDays_ref IS
    v_cursor ClientBirhDays_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT CLNT_CODE,CLNT_NAME||' '||CLNT_OTHER_NAMES CLIENTNAME, CLNT_DOB,CLNT_SMS_TEL
        FROM tqc_clients
        where TO_CHAR(CLNT_DOB,'DD/MM')=TO_CHAR(SYSDATE,'DD/MM');
        --AND CLNT_CODE=111;
    RETURN(v_cursor);
  END;
  FUNCTION getBranchUnits RETURN BranchUnits_ref IS
    v_cursor BranchUnits_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT BRU_CODE, BRU_BRN_CODE, BRU_SHT_DESC, BRU_NAME
    FROM TQC_BRANCH_UNITS
   WHERE BRU_STATUS='A';
    RETURN v_cursor;
END;
 FUNCTION get_debit_day(v_type VARCHAR2, v_value VARCHAR2) RETURN VARCHAR2 IS
  v_return VARCHAR2(100);
  BEGIN
    IF v_type = 'PL' THEN
        BEGIN
            SELECT endr_bo_debit_day
                INTO v_return
            FROM LMS_POLICIES, LMS_POLICY_ENDORSEMENTS
            WHERE endr_code = pol_current_endr_code
                AND pol_code = TO_NUMBER(v_value);
        EXCEPTION
            WHEN OTHERS THEN
                v_return := NULL;
        END;
    ELSIF v_type = 'MKT' THEN
        BEGIN
            SELECT ppr_bo_debit_day
               INTO v_return
            FROM LMS_POL_PROPOSALS
            WHERE ppr_code = TO_NUMBER(v_value);
        EXCEPTION
            WHEN OTHERS THEN
                v_return := NULL;
        END;
    END IF;
    RETURN v_return;
  END get_debit_day;
  PROCEDURE getServiceProvider(v_spr_name IN VARCHAR2,
  v_service_provider_ref OUT service_provider_ref,
  v_spr_spt_code IN NUMBER)
  IS 
  BEGIN
   OPEN v_service_provider_ref FOR
  SELECT
  SPR_CODE, SPR_SHT_DESC, SPR_NAME, SPR_PHYSICAL_ADDRESS,
   SPR_POSTAL_ADDRESS, SPR_PHONE, SPR_FAX, SPR_EMAIL, 
   SPR_CREATED_BY, SPR_DATE_CREATED, SPR_STATUS_REMARKS,
    SPR_STATUS, SPR_PIN_NUMBER, SPR_MOBILE_NO, SPR_INHOUSE,
     SPR_SMS_NUMBER
FROM TQC_SERVICE_PROVIDERS
  WHERE SPR_NAME LIKE '%' || NVL(v_spr_name,'NONAME') || '%'
  AND spr_spt_code=v_spr_spt_code;
  
  END;
  FUNCTION getReportsAssigned (v_rpt_rsm_code IN NUMBER)
   RETURN systemrpt_ref
IS
   v_cursor   systemrpt_ref;
BEGIN
--RAISE_ERROR('v_rpt_rsm_code'||v_rpt_rsm_code);
   OPEN v_cursor FOR
      SELECT RPT_CODE,RPT_NAME, RPT_DESCRIPTION, RPT_ACTIVE
        FROM tqc_system_reports
       WHERE RPT_RSM_CODE = v_rpt_rsm_code;
RETURN v_cursor;
END;
FUNCTION getReportsUnAssigned (v_rpt_rsm_code IN NUMBER)
   RETURN systemrpt_ref
IS
   v_cursor   systemrpt_ref;
BEGIN
   OPEN v_cursor FOR
      SELECT RPT_CODE,RPT_NAME, RPT_DESCRIPTION, RPT_ACTIVE
        FROM tqc_system_reports
        where RPT_RSM_CODE !=v_rpt_rsm_code
        UNION
         SELECT RPT_CODE,RPT_NAME, RPT_DESCRIPTION, RPT_ACTIVE
        FROM tqc_system_reports
        where RPT_RSM_CODE IS NULL;
RETURN v_cursor;
END;
FUNCTION getsystemrptmodules
   RETURN systemrptmodules_ref
IS
   v_cursor   systemrptmodules_ref;
BEGIN
   OPEN v_cursor FOR
      SELECT srm_code, srm_name, srm_desc, srm_sys_code, sys_name
        FROM tqc_sys_report_modules, tqc_systems
       WHERE sys_code = srm_sys_code;

   RETURN v_cursor;
END;
FUNCTION getsystemrptsubmodules (v_rsm_srm_code IN NUMBER)
   RETURN systemrptsubmodules_ref
IS
   v_cursor   systemrptsubmodules_ref;
BEGIN
   OPEN v_cursor FOR
      SELECT rsm_code, rsm_name, rsm_desc
        FROM tqc_sys_rpt_sub_modules
       WHERE rsm_srm_code = v_rsm_srm_code;

   RETURN v_cursor;
END;
FUNCTION get_campaigns
   RETURN campaigns_ref
IS
   v_cursor   campaigns_ref;
BEGIN
   OPEN v_cursor FOR
      SELECT CMP_CODE, CMP_DATE, CMP_NAME, CMP_TYPE,CMP_STATUS
        FROM TQC_CAMPAIGNS;
     
   RETURN v_cursor;
END;
PROCEDURE get_sectors_occup(v_sectors_ref OUT sectors_occup_ref) IS
  BEGIN
    OPEN v_sectors_ref FOR
     SELECT sectors.* from(
     SELECT  NULL sec_code,NULL sec_sht_desc,NULL sec_name,NULL OCC_NAME ,NULL OCC_CODE FROM DUAL
    UNION
        SELECT sec_code, sec_sht_desc, sec_name,OCC_NAME,OCC_CODE FROM tqc_sectors,tqc_occupations
       WHERE SEC_CODE=OCC_SEC_CODE(+)) sectors
      order by sectors.sec_name nulls FIRST;
  END get_sectors_occup;
   PROCEDURE get_occupations(v_sect_code NUMBER,v_sectors_ref OUT sectors_ref) IS
  BEGIN
    OPEN v_sectors_ref FOR
      SELECT occ_code,occ_sht_desc, occ_name FROM tqc_occupations
      WHERE occ_sec_code=v_sect_code
      order by occ_name;
  END get_occupations;
  FUNCTION get_All_towns
   RETURN All_towns_ref
IS
   v_cursor   All_towns_ref;
BEGIN
   OPEN v_cursor FOR
      SELECT TWN_CODE, TWN_COU_CODE, TWN_SHT_DESC, TWN_NAME
        FROM tqc_towns;
   RETURN v_cursor;
END;

FUNCTION getallsubsidiary (v_clnt_code IN NUMBER)
   RETURN allsubsidiary_ref
IS
   v_cursor   allsubsidiary_ref;
BEGIN
   OPEN v_cursor FOR
      SELECT a.clnt_name || ' ' || a.clnt_other_names client,
             DECODE (a.clnt_type,
                     'I', 'Individual',
                     'C', 'Cooperate'
                    ) clnt_type,
             a.clnt_sht_desc, a.clnt_physical_addrs, a.clnt_postal_addrs,
             a.clnt_email_addrs
        FROM tqc_clients a, tqc_clients b
       WHERE b.clnt_code = a.clnt_clnt_code AND a.clnt_clnt_code = v_clnt_code
      UNION
      SELECT spr_name, spt_name, spr_sht_desc, spr_physical_address,
             spr_postal_address, spr_email
        FROM tqc_service_providers, tqc_clients, tqc_service_provider_types
       WHERE spr_clnt_code = clnt_code
         AND spt_code = spr_spt_code
         AND spr_clnt_code = v_clnt_code
      UNION
      SELECT agn_name, act_account_type, agn_sht_desc, agn_physical_address,
             agn_postal_address, agn_email_address
        FROM tqc_agencies, tqc_clients, tqc_account_types
       WHERE agn_clnt_code = clnt_code
         AND act_code = agn_act_code
         AND agn_clnt_code = v_clnt_code;

   RETURN v_cursor;
END;

FUNCTION get_rating_starndards(v_ors_rorg_code NUMBER) RETURN rating_starndards_ref IS
    v_cursor rating_starndards_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT ORS_CODE, ORS_DESC
        FROM TQC_ORG_RATING_STARNDARDS
        WHERE ORS_RORG_CODE=v_ors_rorg_code;
    RETURN(v_cursor);
  END;

FUNCTION get_ratings RETURN ratings_ref IS
    v_cursor ratings_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT RORG_CODE, RORG_DESC, RORG_SHT_DESC
        FROM TQC_RATING_ORGANIZATIONS;
    RETURN(v_cursor);
  END;
  FUNCTION getAllSmsMessages(v_sms_sys_code in number) RETURN AllSmsMessages_ref IS
    v_cursor AllSmsMessages_ref;
  BEGIN
  --RAISE_ERROR('v_sms_sys_code'||v_sms_sys_code);
    OPEN v_cursor FOR
      SELECT SMS_CODE, SMS_SYS_CODE, SMS_SYS_MODULE,
       SMS_CLNT_CODE, SMS_AGN_CODE, SMS_POL_CODE, SMS_POL_NO, 
       SMS_CLM_NO, SMS_TEL_NO, 
      SMS_MSG, SMS_STATUS, SMS_PREPARED_BY, SMS_PREPARED_DATE, 
      SMS_SEND_DATE, SMS_QUOT_CODE, SMS_QUOT_NO, SMS_USR_CODE, SMS_SENT_RESPONSE,CLNT_NAME||' '||CLNT_OTHER_NAMES CLIENT,
       AGN_NAME,USR_USERNAME,null,null
        FROM TQC_SMS_MESSAGES,tqc_clients,tqc_agencies,tqc_users
        WHERE SMS_CLNT_CODE=CLNT_CODE(+)
        AND SMS_AGN_CODE=AGN_CODE(+)
        AND SMS_USR_CODE=USR_CODE(+)
        AND SMS_SYS_CODE=v_sms_sys_code;
     RETURN(v_cursor);
  END;
   FUNCTION getAllEmailMessages(v_email_sys_code in number,v_email_status IN VARCHAR2) RETURN AllSmsMessages_ref IS
    v_cursor AllSmsMessages_ref;
  BEGIN
    OPEN v_cursor FOR
     SELECT EMAIL_CODE, EMAIL_SYS_CODE, EMAIL_SYS_MODULE, EMAIL_CLNT_CODE,
       EMAIL_AGN_CODE, EMAIL_POL_CODE, EMAIL_POL_NO, EMAIL_CLM_NO, NULL EMAIL_TEL_NO,
        EMAIL_MSG, EMAIL_STATUS, 
      EMAIL_PREPARED_BY, EMAIL_PREPARED_DATE, EMAIL_SEND_DATE, EMAIL_QUOT_CODE,
       NULL EMAIL_QUOT_NO, EMAIL_USR_CODE, EMAIL_SENT_RESPONSE,CLNT_NAME||' '||CLNT_OTHER_NAMES CLIENT,
       AGN_NAME,USR_USERNAME,EMAIL_ADDRESS,EMAIL_SUBJ
        FROM TQC_EMAIL_MESSAGES,tqc_clients,tqc_agencies,tqc_users
        WHERE EMAIL_CLNT_CODE(+)=CLNT_CODE
        AND EMAIL_AGN_CODE=AGN_CODE(+)
        AND EMAIL_USR_CODE=USR_CODE(+)
        AND EMAIL_SYS_CODE=v_email_sys_code
        AND email_status=NVL(v_email_status,email_status);
     RETURN(v_cursor);
  END;
  FUNCTION getLocationDetails(v_loc_twn_code IN NUMBER)
   RETURN locationdetails_ref
IS
   v_cursor   locationdetails_ref;
BEGIN
--RAISE_ERROR('v_loc_twn_code'||v_loc_twn_code);
   OPEN v_cursor FOR
      SELECT LOC_CODE, LOC_TWN_CODE, LOC_SHT_DESC, LOC_NAME
        FROM tqc_locations
       WHERE LOC_TWN_CODE = v_loc_twn_code;
RETURN (v_cursor);
END;
FUNCTION getorgdivlevelstype
    RETURN orgdivlevelstype_ref IS
    v_cursor orgdivlevelstype_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT dlt_code,
             dlt_sys_code,
             dlt_desc,
             dlt_act_code,
             acc_type_name(dlt_act_code) acc_type_name,
             DLT_CODE_VAL
        FROM tqc_org_division_levels_type
       ORDER BY dlt_desc;
  
    RETURN v_cursor;
  END;
   FUNCTION get_bussiness_person
   RETURN bussiness_person_ref
IS
   v_cursor   bussiness_person_ref;
   begin
  -- RAISE_ERROR('TEST');
   OPEN v_cursor FOR
      SELECT BPN_CODE, BPN_ID_NO, BPN_ADDRESS, BPN_TEL,
       BPN_MOBILE_NO, BPN_EMAIL, BPN_TYPE, BPN_ZIP, BPN_TOWN, 
      BPN_COU_CODE, BPN_NAME, BPN_PIN, BPN_BBR_CODE, 
      BPN_BANK_ACC_NO, BPN_BBR_SWIFT_CODE, BPN_REG_CLMT_CODE,
      BBR_BRANCH_NAME,COU_NAME,CLD_OTHER_NAMES||' '||CLD_SURNAME
        FROM TQC_BUSINESS_PERSONS,tqc_bank_branches,TQC_COUNTRIES,GIN_RGSTD_CLAIMANTS,gin_claimants
        where BBR_CODE(+)=BPN_BBR_CODE
        AND COU_CODE(+)=BPN_COU_CODE
        AND REG_CLMT_CODE(+)=BPN_REG_CLMT_CODE
        and CLD_CODE(+)=REG_CLD_CODE;
      RETURN (v_cursor);
END;

FUNCTION get_bank_branches_val
   RETURN bank_branches_val_ref
IS
   v_cursor   bank_branches_val_ref;
   begin
   OPEN v_cursor FOR
      SELECT BBR_CODE, BBR_BNK_CODE, BBR_BRANCH_NAME, BBR_REMARKS, BBR_SHT_DESC
        FROM tqc_bank_branches;
       
      RETURN (v_cursor);
END;
FUNCTION get_registered_claimants
   RETURN registered_claimants_ref
IS
   v_cursor   registered_claimants_ref;
   begin
   OPEN v_cursor FOR
      SELECT REG_CLMT_CODE,CLD_CODE, CLD_ID_NO, CLD_OTHER_NAMES||' '||CLD_SURNAME
        FROM GIN_RGSTD_CLAIMANTS,gin_claimants
        where CLD_CODE=REG_CLD_CODE;
       
      RETURN (v_cursor);
END;
END TQC_SETUPS_CURSORBK280514; 
/