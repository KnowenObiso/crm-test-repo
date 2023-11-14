/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_SETUPS_CURSOR_15102017  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_setups_cursor_15102017
AS
   /******************************************************************************
    NAME: TQC_SETUPS_CURSOR
    PURPOSE:

    REVISIONS:
    Ver Date Author Description
    --------- ---------- --------------- ------------------------------------
    1.0 5/6/2010 1. Created this package body.
   ******************************************************************************/
   FUNCTION myfunction (param1 IN NUMBER)
      RETURN NUMBER
   IS
   BEGIN
      RETURN param1;
   END;

   PROCEDURE myprocedure (param1 IN NUMBER)
   IS
      tmpvar   NUMBER;
   BEGIN
      tmpvar := param1;
   END;

   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

--   PROCEDURE organizations (
--      v_organizations_ref   OUT   organizations_ref,
--      v_org_code                  tqc_organizations.org_code%TYPE
--   )
--   IS
--   BEGIN
--      OPEN v_organizations_ref FOR
--         SELECT org_code, org_sht_desc, org_name, org_addrs, org_twn_code,
--                org_cou_code, org_email_addrs, org_phy_addrs, org_cur_code,
--                org_zip, org_fax, org_tel1, org_tel2, org_rpt_logo,
--                org_motto, org_pin_no, org_ed_code, org_item_acc_code,
--                org_other_name, org_type, org_web_brn_code, org_web_addrs,
--                org_agn_code, org_directors, org_lang_code, org_avatar,
--                currency_sysmbol (org_cur_code) cur_symbol,
--                fetch_currency_name (org_cur_code) cur_desc,
--                country_name (org_cou_code) cou_name,
--                town_name (org_twn_code) twn_name, org_sts_code,
--                state_name (org_sts_code) state_name, org_grp_logo,
--                agn_name (org_agn_code) manager, org_vat_number,
--                cou_admin_reg_type, org_mobile1, org_mobile2, cou_zip_code,org_cert_sign
--           FROM tqc_organizations, tqc_countries
--          WHERE org_code = NVL (v_org_code, org_code)
--            AND org_cou_code = cou_code;
--   END organizations;
PROCEDURE organizations (
      v_organizations_ref   OUT   organizations_ref,
      v_org_code                  tqc_organizations.org_code%TYPE
   )
   IS
   BEGIN
      OPEN v_organizations_ref FOR
         SELECT org_code, org_sht_desc, org_name, org_addrs, org_twn_code,
                org_cou_code, org_email_addrs, org_phy_addrs, org_cur_code,
                org_zip, org_fax, org_tel1, org_tel2, org_rpt_logo,
                org_motto, org_pin_no, org_ed_code, org_item_acc_code,
                org_other_name, org_type, org_web_brn_code, org_web_addrs,
                org_agn_code, org_directors, org_lang_code, org_avatar,
                currency_sysmbol (org_cur_code) cur_symbol,
                fetch_currency_name (org_cur_code) cur_desc,
                country_name (org_cou_code) cou_name,
                town_name (org_twn_code) twn_name, org_sts_code,
                state_name (org_sts_code) state_name, org_grp_logo,
                agn_name (org_agn_code) manager, org_vat_number,
                cou_admin_reg_type, org_mobile1, org_mobile2, cou_zip_code,org_cert_sign
           FROM tqc_organizations, tqc_countries
          WHERE org_code = NVL (v_org_code, org_code)
            AND org_cou_code = cou_code;
   END organizations;

   PROCEDURE organization_details (
      v_organization_details_ref   OUT   organization_details_ref,
      v_org_code                         tqc_organizations.org_code%TYPE
   )
   IS
   BEGIN
      OPEN v_organization_details_ref FOR
         SELECT org_code, org_sht_desc, org_name, org_addrs, org_twn_code,
                org_cou_code, org_email_addrs, org_phy_addrs, org_cur_code,
                org_zip, org_fax, org_tel1, org_tel2, org_rpt_logo,
                org_motto, org_pin_no, org_ed_code, org_item_acc_code,
                org_other_name, org_type, org_web_brn_code, org_web_addrs,
                org_agn_code, org_directors, org_lang_code, org_avatar,
                currency_sysmbol (org_cur_code) cur_symbol,
                fetch_currency_name (org_cur_code) cur_desc,
                country_name (org_cou_code) cou_name,
                town_name (org_twn_code) twn_name, org_sts_code,
                state_name (org_sts_code) state_name, org_grp_logo
           FROM tqc_organizations
          WHERE org_code = NVL (v_org_code, org_code);
   END organization_details;

--   PROCEDURE getcountries (
--        v_countries_ref OUT countries_ref,
--        v_schengen        IN  VARCHAR2,
--        v_search          IN  VARCHAR2
--    )
--   IS
--   BEGIN
----     raise_error('v_schengen: '||v_schengen||' v_search: '||v_search);
--      OPEN v_countries_ref FOR
--         SELECT   cou_code, cou_sht_desc, cou_name, cou_base_curr,
--                  cur_desc cou_base_curr_desc, cou_nationality, cou_zip_code,
--                  cou_admin_reg_type, cou_admin_reg_mandatory, cou_schegen,
--                  cou_emb_code, cou_curr_serial, cou_mobile_prefix,
--                  cou_client_number
--             FROM tqc_countries, tqc_currencies
--            WHERE cou_base_curr = cur_code(+)
--             AND UPPER(cou_name) LIKE '%'||UPPER(NVL(v_search,cou_name))||'%'
--             AND cou_schegen = NVL (v_schengen, cou_schegen)
--         ORDER BY cou_name ASC;
--   END getcountries;
PROCEDURE getcountries (
   v_countries_ref   OUT   countries_ref,
   v_county_name           VARCHAR2 DEFAULT NULL,
   v_county_id             VARCHAR2 DEFAULT NULL,
   v_county_code           VARCHAR2 DEFAULT NULL
)
IS
BEGIN
   -- raise_error('v_county_name '||v_county_name);
   OPEN v_countries_ref FOR
      SELECT   NULL, NULL, NULL cou_name, NULL, NULL, NULL, NULL, NULL, NULL,
               NULL, NULL, NULL, NULL, NULL
          FROM DUAL
      UNION
      SELECT   cou_code, cou_sht_desc, cou_name, cou_base_curr,
               cur_desc cou_base_curr_desc, cou_nationality, cou_zip_code,
               cou_admin_reg_type, cou_admin_reg_mandatory, cou_schegen,
               cou_emb_code, cou_curr_serial, cou_mobile_prefix,
               cou_client_number
          FROM tqc_countries, tqc_currencies
         WHERE cou_base_curr = cur_code(+)
           AND UPPER (cou_name) LIKE '%' || UPPER (v_county_name) || '%'
      ORDER BY cou_name ASC;
END getcountries;

   PROCEDURE get_specific_country (
      v_cou_code              tqc_countries.cou_code%TYPE,
      v_countries_ref   OUT   countries_ref
   )
   IS
   BEGIN
      OPEN v_countries_ref FOR
         SELECT   cou_code, cou_sht_desc, cou_name, cou_base_curr,
                  cur_desc cou_base_curr_desc, cou_nationality, cou_zip_code,
                  cou_admin_reg_type, cou_admin_reg_mandatory, cou_schegen,
                  cou_emb_code, cou_curr_serial, cou_mobile_prefix,
                  cou_client_number
             FROM tqc_countries, tqc_currencies
            WHERE cou_base_curr = cur_code(+) AND cou_code = v_cou_code
         ORDER BY cou_name ASC;
   END get_specific_country;

   PROCEDURE get_countries_and_currency (
      v_countries_and_currency_ref   OUT   countries_and_currency_ref
   )
   IS
   BEGIN
      OPEN v_countries_and_currency_ref FOR
         SELECT cou_code, cou_sht_desc, cou_name, cou_base_curr,
                cou_nationality, cou_zip_code, cur_symbol, cur_desc,
                cou_admin_reg_type, cou_admin_reg_mandatory
           FROM tqc_countries, tqc_currencies
          WHERE cou_base_curr = cur_code(+);
   END get_countries_and_currency;

   PROCEDURE getstates (
      v_states_ref   OUT   states_ref,
      v_cou_code           tqc_states.sts_cou_code%TYPE
   )
   IS
   BEGIN
      OPEN v_states_ref FOR
         SELECT   NULL sts_code, NULL sts_cou_code, NULL sts_sht_desc,
                  NULL sts_name
             FROM DUAL
         UNION
         SELECT   UPPER (sts_code) sts_code,
                  UPPER (sts_cou_code) sts_cou_code,
                  UPPER (sts_sht_desc) sts_sht_desc,
                  UPPER (sts_name) sts_name
             FROM tqc_states
            WHERE sts_cou_code = NVL (v_cou_code, sts_cou_code)
         ORDER BY sts_name ASC;
   END getstates;

   PROCEDURE get_admin_regions (
      v_admin_regions_ref   OUT   admin_region_ref,
      v_adm_reg_cou_code          tqc_admin_regions.adm_reg_cou_code%TYPE
   )
   IS
   BEGIN
      OPEN v_admin_regions_ref FOR
         SELECT   adm_reg_code, adm_reg_sht_desc, adm_reg_name,
                  adm_reg_cou_code
             FROM tqc_admin_regions
            WHERE adm_reg_cou_code =
                                   NVL (v_adm_reg_cou_code, adm_reg_cou_code)
         ORDER BY adm_reg_name ASC;
   END get_admin_regions;

   PROCEDURE gettowns (
      v_towns_ref   OUT   towns_ref,
      v_cou_code          tqc_towns.twn_cou_code%TYPE
   )
   IS
   BEGIN
      OPEN v_towns_ref FOR
         SELECT twn_code, twn_cou_code, UPPER (twn_sht_desc) twn_sht_desc,
                UPPER (twn_name) twn_name, twn_sts_code, NULL, NULL
           FROM tqc_towns
          WHERE twn_cou_code = NVL (v_cou_code, twn_cou_code);
   END gettowns;

   PROCEDURE get_specific_town (
      v_twn_code          tqc_towns.twn_code%TYPE,
      v_towns_ref   OUT   towns_ref
   )
   IS
   BEGIN
      OPEN v_towns_ref FOR
         SELECT twn_code, twn_cou_code, twn_sht_desc, twn_name, twn_sts_code,
                NULL, NULL
           FROM tqc_towns
          WHERE twn_code = v_twn_code;
   END get_specific_town;

   PROCEDURE get_postal_codes_by_town (
      v_pst_twn_code                    tqc_postal_codes.pst_twn_code%TYPE,
      v_postal_code_by_town_ref   OUT   postal_code_by_town_ref
   )
   IS
   BEGIN
      OPEN v_postal_code_by_town_ref FOR
         SELECT pst_code, pst_twn_code, pst_desc, pst_zip_code
           FROM tqc_postal_codes
          WHERE pst_twn_code = v_pst_twn_code;
   END get_postal_codes_by_town;

   PROCEDURE get_locations_by_town (
      v_loc_twn_code          tqc_locations.loc_twn_code%TYPE,
      v_locations_ref   OUT   locations_ref
   )
   IS
   BEGIN
      OPEN v_locations_ref FOR
         SELECT loc_code, loc_twn_code, loc_sht_desc, loc_name, loc_landmark
           FROM tqc_locations
          WHERE loc_twn_code = v_loc_twn_code;
   END get_locations_by_town;

   PROCEDURE currencies (v_currencies_ref OUT currencies_ref)
   IS
   BEGIN
      OPEN v_currencies_ref FOR
         SELECT   cur_code, cur_symbol, cur_desc, cur_rnd, cur_num_word,
                  cur_decimal_word
             FROM tqc_currencies
         ORDER BY cur_desc ASC;
   END currencies;

   PROCEDURE get_currencies (
      v_currencies_ref   OUT   currencies_ref,
      v_cur_desc               VARCHAR2
   )
   IS
   BEGIN
      OPEN v_currencies_ref FOR
         SELECT   cur_code, cur_symbol, cur_desc, cur_rnd, cur_num_word,
                  cur_decimal_word
             FROM tqc_currencies
            WHERE cur_symbol LIKE '%' || v_cur_desc || '%'
         ORDER BY cur_desc ASC;
   END get_currencies;

   PROCEDURE get_currencies_exclude_curr (
      v_cur_code               tqc_currencies.cur_code%TYPE,
      v_currencies_ref   OUT   currencies_ref
   )
   IS
   BEGIN
      OPEN v_currencies_ref FOR
         SELECT   cur_code, cur_symbol, cur_desc, cur_rnd, cur_num_word,
                  cur_decimal_word
             FROM tqc_currencies
            WHERE cur_code != v_cur_code
         ORDER BY cur_desc ASC;
   END get_currencies_exclude_curr;

   FUNCTION fetch_currency_name (v_cur_code NUMBER)
      RETURN VARCHAR2
   IS
      v_rtval   VARCHAR2 (150);
   BEGIN
      IF v_cur_code IS NULL
      THEN
         RETURN NULL;
      ELSE
         SELECT cur_desc
           INTO v_rtval
           FROM tqc_currencies
          WHERE cur_code = v_cur_code;
      END IF;

      RETURN v_rtval;
   END fetch_currency_name;

   PROCEDURE get_currency_denominations (
      v_currency_denominations_ref   OUT   currency_denominations_ref
   )
   IS
   BEGIN
      OPEN v_currency_denominations_ref FOR
         SELECT cud_code, cud_cur_code, cud_value, cud_name, cud_wef
           FROM tqc_currency_denominations;
   END get_currency_denominations;

   PROCEDURE get_curr_denominations_by_code (
      v_cud_cur_code                       tqc_currency_denominations.cud_cur_code%TYPE,
      v_currency_denominations_ref   OUT   currency_denominations_ref
   )
   IS
   BEGIN
      OPEN v_currency_denominations_ref FOR
         SELECT cud_code, cud_cur_code, cud_value, cud_name, cud_wef
           FROM tqc_currency_denominations
          WHERE cud_cur_code = v_cud_cur_code;
   END get_curr_denominations_by_code;

   PROCEDURE get_currency_rates (v_currency_rates_ref OUT currency_rates_ref)
   IS
   BEGIN
      OPEN v_currency_rates_ref FOR
         SELECT crt_code, crt_cur_code, crt_rate, crt_date,
                crt_base_cur_code
           FROM tqc_currency_rates;
   END get_currency_rates;

   PROCEDURE get_currency_rates_by_currency (
      v_crt_cur_code               tqc_currency_rates.crt_cur_code%TYPE,
      v_currency_rates_ref   OUT   currency_rates_by_curr_ref
   )
   IS
   BEGIN
      OPEN v_currency_rates_ref FOR
         SELECT crt_code, crt_cur_code, crt_rate, crt_date,
                crt_base_cur_code, cur_desc, crt_wef, crt_wet
           FROM tqc_currency_rates, tqc_currencies
          WHERE crt_cur_code = cur_code
                AND crt_base_cur_code = v_crt_cur_code;
   END get_currency_rates_by_currency;

   PROCEDURE regions (
      v_regions_ref   OUT   regions_ref,
      v_org_code            tqc_regions.reg_org_code%TYPE
   )
   IS
   BEGIN
--      raise_error('v_org_code: '||v_org_code);
      OPEN v_regions_ref FOR
         SELECT reg_code, reg_org_code, reg_sht_desc, reg_name, reg_wef,
                reg_wet, reg_agn_code, reg_post_level, reg_mngr_allowed,
                reg_overide_comm_earned, agn_name, reg_brn_mngr_seq_no,
                reg_agn_seq_no, agn_name (reg_agn_code) manager
           FROM tqc_regions, tqc_agencies
          WHERE reg_agn_code = agn_code(+) AND reg_org_code = v_org_code;
   END regions;

   PROCEDURE branches (
      v_branches_ref   OUT   branches_ref,
      v_reg_code             tqc_branches.brn_reg_code%TYPE
   )
   IS
   BEGIN
      OPEN v_branches_ref FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name,
                brn_phy_addrs, brn_email_addrs, brn_post_addrs, brn_twn_code,
                brn_cou_code, brn_contact, brn_manager, brn_tel, brn_fax,
                brn_gen_pol_clm, brn_bns_code, brn_agn_code, brn_post_level,
                agn_name (brn_agn_code) manager
           FROM tqc_branches
          WHERE brn_reg_code = v_reg_code;
   END branches;

 PROCEDURE bank_regions(
      v_bank_regions_ref   OUT   bank_regions_ref
   )
   IS
   BEGIN
      OPEN v_bank_regions_ref FOR
         SELECT BNKR_CODE,BNKR_ORG_CODE,BNKR_SHT_DESC,BNKR_NAME,BNKR_WEF,BNKR_WET, 
         BNKR_REG_CODE,BNKR_AGN_CODE,agn_name (BNKR_AGN_CODE) manager
       FROM tqc_bank_regions;
   END;
   
   PROCEDURE get_branches (
      v_branches_ref   OUT   get_branches_ref,
      v_reg_code             tqc_branches.brn_reg_code%TYPE
   )
   IS
   BEGIN
      OPEN v_branches_ref FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name,
                brn_phy_addrs, brn_email_addrs, brn_post_addrs, brn_twn_code,
                brn_cou_code, brn_contact, brn_manager, brn_tel, brn_fax,
                brn_gen_pol_clm, brn_bns_code, brn_agn_code, brn_post_level,
                brn_mngr_allowed, brn_overide_comm_earned,
                brn_bra_mngr_seq_no, brn_agn_seq_no, brn_pol_seq,
                brn_prop_seq, brn_ref, brn_ade_code,brn_town_name,brn_post_code,
                (SELECT agn_name
                   FROM tqc_agencies
                  WHERE agn_code = brn_ade_code) brn_ade_name
           FROM tqc_branches
          WHERE brn_reg_code = v_reg_code;
   END get_branches;

   FUNCTION getbranchcontacts (v_brncode NUMBER)
      RETURN branches_contacts_ref
   IS
      v_cursor   branches_contacts_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tbc_code, tbc_name, tbc_designation, tbc_mobile_number,
                tbc_telephone, tbc_id_number, tbc_physical_address,
                tbc_email_address
           FROM tqc_branch_contacts
          WHERE tbc_brn_code = v_brncode;

      RETURN v_cursor;
   END;

   PROCEDURE get_orgbranches (
      v_branches_ref   OUT   get_branches_ref,
      v_orgcode              NUMBER,
      v_regcode              NUMBER DEFAULT NULL
   )
   IS
   BEGIN
      OPEN v_branches_ref FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name,
                brn_phy_addrs, brn_email_addrs, brn_post_addrs, brn_twn_code,
                brn_cou_code, brn_contact, brn_manager, brn_tel, brn_fax,
                brn_gen_pol_clm, brn_bns_code, brn_agn_code, brn_post_level,
                brn_mngr_allowed, brn_overide_comm_earned,
                brn_bra_mngr_seq_no, brn_agn_seq_no, brn_pol_seq,
                brn_prop_seq, brn_ref, brn_ade_code,null,null,
                (SELECT agn_name
                   FROM tqc_agencies
                  WHERE agn_code = brn_ade_code) brn_ade_name
           FROM tqc_branches, tqc_regions
          WHERE brn_reg_code = reg_code
            AND reg_org_code = v_orgcode
            AND reg_code = NVL (v_regcode, reg_code);
   END get_orgbranches;

   PROCEDURE get_specific_branch (
      v_brn_code             tqc_branches.brn_code%TYPE,
      v_branches_ref   OUT   branches_ref
   )
   IS
   BEGIN
      OPEN v_branches_ref FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name,
                brn_phy_addrs, brn_email_addrs, brn_post_addrs, brn_twn_code,
                brn_cou_code, brn_contact, brn_manager, brn_tel, brn_fax,
                brn_gen_pol_clm, brn_bns_code, brn_agn_code, brn_post_level,
                agn_name (brn_agn_code) manager
           FROM tqc_branches
          WHERE brn_code = v_brn_code;
   END get_specific_branch;

   PROCEDURE find_certificate_alloc_branch (
      v_agn_code       IN       NUMBER,
      v_branches_ref   OUT      branches_ref,
      v_error          OUT      VARCHAR2
   )
   IS
      v_org_type   VARCHAR2 (10);
   BEGIN
      v_org_type := tqc_interfaces_pkg.get_org_type (37);

      IF v_agn_code = 0 OR NVL (v_org_type, 'INS') != 'INS'
      THEN
         OPEN v_branches_ref FOR
            SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name,
                   brn_phy_addrs, brn_email_addrs, brn_post_addrs,
                   brn_twn_code, brn_cou_code, brn_contact, brn_manager,
                   brn_tel, brn_fax, brn_gen_pol_clm, brn_bns_code,
                   brn_agn_code, brn_post_level,
                   agn_name (brn_agn_code) manager
              FROM tqc_branches
             WHERE brn_code != -2000;
      ELSE
         v_error :=
            'This option is allowed for certificates allocated for own consumption.';
      END IF;
   END find_certificate_alloc_branch;

   PROCEDURE branch_agencies (
      v_branch_agencies_ref   OUT   branch_agencies_ref,
      v_brn_code                    tqc_branch_agencies.bra_brn_code%TYPE
   )
   IS
   BEGIN
      OPEN v_branch_agencies_ref FOR
         SELECT bra_code, bra_brn_code, bra_sht_desc, bra_name, bra_status,
                agn_name bra_manager, bra_agn_code, bra_post_level
           FROM tqc_branch_agencies, tqc_agencies
          WHERE bra_brn_code = v_brn_code AND bra_agn_code = agn_code(+);
   END branch_agencies;

   PROCEDURE get_branch_agencies (
      v_branch_agencies_ref   OUT   get_branch_agencies_ref,
      v_brn_code                    tqc_branch_agencies.bra_brn_code%TYPE
   )
   IS
   BEGIN
      OPEN v_branch_agencies_ref FOR
         SELECT bra_code, bra_brn_code, bra_sht_desc, bra_name, bra_status,
                agn_name bra_manager, bra_agn_code, bra_post_level,
                bra_mngr_allowed, bra_overide_comm_earned,
                bra_bru_mngr_seq_no, bra_agn_seq_no, bra_pol_seq,
                bra_prop_seq,bra_unt_sht_desc_prefix,bra_compt_ov_on_own_buss
           FROM tqc_branch_agencies, tqc_agencies
          WHERE bra_brn_code = v_brn_code AND bra_agn_code = agn_code(+);
   END get_branch_agencies;

   PROCEDURE branch_units (
      v_branch_units_ref   OUT   branch_units_ref,
      v_brn_code                 tqc_branch_units.bru_brn_code%TYPE
   )
   IS
   BEGIN
      OPEN v_branch_units_ref FOR
         SELECT bru_code, bru_brn_code, bru_sht_desc, bru_name,
                bru_supervisor, bru_status, bru_agn_code, bru_bra_code,
                agn_name bru_manager, bru_post_level
           FROM tqc_branch_units, tqc_agencies
          WHERE bru_brn_code = v_brn_code AND bru_agn_code = agn_code(+);
   END branch_units;

   PROCEDURE get_branch_units (
      v_branch_units_ref   OUT   get_branch_units_ref,
      v_brn_code                 tqc_branch_units.bru_brn_code%TYPE
   )
   IS
   BEGIN
      OPEN v_branch_units_ref FOR
         SELECT bru_code, bru_brn_code, bru_sht_desc, bru_name,
                bru_supervisor, bru_status, bru_agn_code, bru_bra_code,
                agn_name bru_manager, bru_post_level, bru_mngr_allowed,
                bru_overide_comm_earned, bru_agn_seq_no, bru_pol_seq,
                bru_prop_seq
           FROM tqc_branch_units, tqc_agencies
          WHERE bru_brn_code = v_brn_code AND bru_agn_code = agn_code(+);
   END get_branch_units;

   PROCEDURE get_branch_agency_units (
      v_branch_units_ref   OUT   get_branch_units_ref,
      v_bra_code                 tqc_branch_units.bru_bra_code%TYPE,
      v_brn_code                 tqc_branch_units.bru_brn_code%TYPE
   )
   IS
   BEGIN
  /* IF v_bra_code IS NULL THEN
   RAISE_ERROR('v_bra_code '||v_bra_code||'v_brn_code '||v_brn_code);
   END IF;*/
   
      OPEN v_branch_units_ref FOR
         SELECT bru_code, bru_brn_code, bru_sht_desc, bru_name,
                bru_supervisor, bru_status, bru_agn_code, bru_bra_code,
                agn_name bru_manager, bru_post_level, bru_mngr_allowed,
                bru_overide_comm_earned, bru_agn_seq_no, bru_pol_seq,
                bru_prop_seq
           FROM tqc_branch_units, tqc_agencies
          WHERE (bru_bra_code = v_bra_code OR v_bra_code IS NULL)
          AND bru_brn_code = v_brn_code
          AND bru_agn_code = agn_code(+);
   END get_branch_agency_units;

   PROCEDURE branch_names (v_branch_names_ref OUT branch_names_ref)
   IS
   BEGIN
      OPEN v_branch_names_ref FOR
         SELECT   bns_code, bns_sht_desc, bns_name, bns_phy_addrs,
                  bns_email_addrs, bns_post_addrs, bns_twn_code,
                  bns_cou_code, bns_contact, bns_manager, bns_tel, bns_fax
             FROM tqc_branch_names
         ORDER BY 1;
   END branch_names;

   PROCEDURE agencies (v_agencies_ref OUT agencies_ref)
   IS
   BEGIN
      OPEN v_agencies_ref FOR
         SELECT agn_code, agn_sht_desc, agn_name
           FROM tqc_agencies, tqc_account_types
          WHERE act_type_id IN ('IA', 'BM', 'BR', 'BE')
            AND act_code = agn_act_code;
   -- AND NVL(:TQC_BRANCH_AGENCIES.BRA_MNGR_ALLOWED,'Y') ='Y';
   END agencies;

   PROCEDURE managers (v_managers_ref OUT managers_ref)
   IS
   BEGIN
      OPEN v_managers_ref FOR
         SELECT   agn_code, agn_sht_desc, agn_name, twn_name
             FROM tqc_agencies, tqc_towns
            WHERE agn_twn_code = twn_code(+)
              AND agn_act_code IN (SELECT act_code
                                     FROM tqc_account_types
                                    WHERE act_type_id IN ('IA', 'BM'))
              AND agn_status = 'ACTIVE'
         UNION ALL
         SELECT   NULL, NULL, 'NONE', 'NONE'
             FROM DUAL
         ORDER BY 2 NULLS FIRST;
   END managers;

   PROCEDURE agencymanagers (v_managers_ref OUT managers_ref)
   IS
   BEGIN
      OPEN v_managers_ref FOR
         SELECT   agn_code, agn_sht_desc, agn_name, twn_name
             FROM tqc_agencies, tqc_towns
            WHERE agn_twn_code = twn_code(+)
              AND agn_act_code IN (SELECT act_code
                                     FROM tqc_account_types
                                    WHERE act_type_id IN ('BA'))
              AND agn_code NOT IN (SELECT bra_agn_code
                                     FROM tqc_branch_agencies
                                    WHERE bra_agn_code IS NOT NULL)
              AND agn_status = 'ACTIVE'
         UNION ALL
         SELECT   NULL, NULL, 'NONE', 'NONE'
             FROM DUAL
         ORDER BY 2 NULLS FIRST;
   END agencymanagers;

--   PROCEDURE unitmanagers (v_managers_ref OUT managers_ref)
--   IS
--   BEGIN
--      OPEN v_managers_ref FOR
--         SELECT   agn_code, agn_sht_desc, agn_name, twn_name
--             FROM tqc_agencies, tqc_towns
--            WHERE agn_twn_code = twn_code(+)
--              AND agn_act_code IN (SELECT act_code
--                                     FROM tqc_account_types
--                                    WHERE act_type_id IN ('IA', 'BM'))
--              AND agn_code NOT IN (SELECT bru_agn_code
--                                     FROM tqc_branch_units
--                                    WHERE bru_agn_code IS NOT NULL)
--              AND agn_status = 'ACTIVE'
--         UNION ALL
--         SELECT   NULL, NULL, 'NONE', 'NONE'
--             FROM DUAL
--         ORDER BY 2 NULLS FIRST;
--   END unitmanagers;

PROCEDURE unitmanagers (v_managers_ref OUT managers_ref)
   IS
   v_param  VARCHAR2(5);
   BEGIN
        
    BEGIN
      SELECT PARAM_VALUE
      INTO  v_param
      FROM TQC_PARAMETERS
      WHERE PARAM_NAME = 'ALLOW_MULT_UNITS_MNGR';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_param := 'N';
    END;
    
    IF v_param = 'N' THEN 
      
      OPEN v_managers_ref FOR
         SELECT   agn_code, agn_sht_desc, agn_name, twn_name
             FROM tqc_agencies, tqc_towns
            WHERE agn_twn_code = twn_code(+)
              AND agn_act_code IN (SELECT act_code
                                     FROM tqc_account_types
                                    WHERE act_type_id IN ('BE'))
              AND agn_code NOT IN (SELECT bru_agn_code
                                     FROM tqc_branch_units
                                    WHERE bru_agn_code IS NOT NULL)
              AND agn_status = 'ACTIVE'
         UNION ALL
         SELECT   NULL, NULL, 'NONE', 'NONE'
             FROM DUAL
         ORDER BY 2 NULLS FIRST;
    ELSE
    
    OPEN v_managers_ref FOR
        SELECT   agn_code, agn_sht_desc, agn_name, twn_name
             FROM tqc_agencies, tqc_towns
            WHERE agn_twn_code = twn_code(+)
              AND agn_act_code IN (SELECT act_code
                                     FROM tqc_account_types
                                    WHERE act_type_id IN ('BE'))
              
              AND agn_status = 'ACTIVE'
         UNION ALL
         SELECT   NULL, NULL, 'NONE', 'NONE'
             FROM DUAL
         ORDER BY 2 NULLS FIRST;
    
    END IF;
   END unitmanagers;

   PROCEDURE find_tqc_users (v_cursor OUT tqc_users_cursor)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT usr_username clnt_sht_desc,
                         usr_name clnt_other_names, usr_code clnt_code
                    FROM tqc_users
                   WHERE NVL (usr_acct_mgr, 'N') = 'Y';
   END;

   PROCEDURE get_agency_holding_company (
      v_agency_holding_company_ref   OUT   agency_holding_company_ref
   )
   IS
   BEGIN
      OPEN v_agency_holding_company_ref FOR
         SELECT ahc_code, ahc_name, ahc_postal_address, ahc_physical_address,
                ahc_telephone_number, ahc_mobile_number, ahc_contact_person
           FROM tqc_agency_holding_company;
   END get_agency_holding_company;

   PROCEDURE get_specific_holding_company (
      v_ahc_code                           tqc_agency_holding_company.ahc_code%TYPE,
      v_agency_holding_company_ref   OUT   agency_holding_company_ref
   )
   IS
   BEGIN
      OPEN v_agency_holding_company_ref FOR
         SELECT ahc_code, ahc_name, ahc_postal_address, ahc_physical_address,
                ahc_telephone_number, ahc_mobile_number, ahc_contact_person
           FROM tqc_agency_holding_company
          WHERE ahc_code = v_ahc_code;
   END get_specific_holding_company;

   PROCEDURE get_banks (v_banks_ref OUT banks_ref)
   IS
   BEGIN
      OPEN v_banks_ref FOR
         SELECT   bnk_code, bnk_bank_name, bnk_remarks, bnk_sht_desc,
                  bnk_ddr_code, dd_format_desc, bnk_forwarding_bnk_code,
                  bnk_kba_code,
                  tq_crm.tqc_setups_cursor.bank_name
                                       (bnk_forwarding_bnk_code)
                                                               fwd_bank_name,
                  tqc_setups_cursor.ddreport_desc
                                               (bnk_ddr_code)
                                                            dd_report_format,
                  
                  -- TQC_SETUPS_CURSOR.DDREPORT_DESC(BNK_DDR_CODE) DD_REPORT_FORMAT,
                  bnk_eft_supported, bnk_class_type, bnk_accnt_digit_no,
                  bnk_negotiated_bank, bnk_administration_charge, bnk_logo,
                  bnk_wef,bnk_wet,bnk_status,bnk_acc_max_no,bnk_acc_min_no,bnk_cou_code,cou_name
             FROM tqc_banks,tqc_countries
             WHERE bnk_cou_code = cou_code(+)
         ORDER BY bnk_bank_name;
   END get_banks;

   PROCEDURE get_bank_branches (v_bank_branches_ref OUT bank_branches_ref)
   IS
   BEGIN
      OPEN v_bank_branches_ref FOR
         SELECT bbr_code, bbr_bnk_code, bbr_branch_name, bbr_remarks,
                bbr_sht_desc, bbr_ref_code, bbr_eft_supported,
                bbr_dd_supported, bbr_date_created, bbr_created_by,
                bbr_physical_addrs, bbr_postal_addrs, bbr_kba_code, null, null
           FROM tqc_bank_branches;
   END get_bank_branches;

   PROCEDURE get_bank_branches_by_bank_code (
      v_bbr_bnk_code              tqc_bank_branches.bbr_bnk_code%TYPE,
      v_bank_branches_ref   OUT   bank_branches_ref
   )
   IS
   BEGIN
      OPEN v_bank_branches_ref FOR
         SELECT bbr_code, bbr_bnk_code, bbr_branch_name, bbr_remarks,
                bbr_sht_desc, bbr_ref_code, bbr_eft_supported,
                bbr_dd_supported, bbr_date_created, bbr_created_by,
                bbr_physical_addrs, bbr_postal_addrs, bbr_kba_code,
                bbr_bnkt_code,bbr_bnkt_sht_desc
           FROM tqc_bank_branches
          WHERE bbr_bnk_code = v_bbr_bnk_code;
   END get_bank_branches_by_bank_code;
   
   
   PROCEDURE get_bank_branches_by_terr_code (
      v_bnkt_code              tqc_bank_territories.bnkt_code%TYPE,
      v_bank_branches_ref   OUT   bank_branches_ref
   )
   IS
   BEGIN
      OPEN v_bank_branches_ref FOR
         SELECT bbr_code, bbr_bnk_code, bbr_branch_name, bbr_remarks,
                bbr_sht_desc, bbr_ref_code, bbr_eft_supported,
                bbr_dd_supported, bbr_date_created, bbr_created_by,
                bbr_physical_addrs, bbr_postal_addrs, bbr_kba_code,null,null
           FROM tqc_bank_branches
          WHERE bbr_bnkt_code = v_bnkt_code;
   END ;
   PROCEDURE get_bank_territs_by_bank_code (
      v_bnkt_bnk_code             tqc_bank_territories.bnkt_bnk_code%TYPE,
      v_bank_territories_ref   OUT   bank_territories_ref
   )
   IS
   BEGIN
     raise_error('v_bnkt_bnk_code: '||v_bnkt_bnk_code);
      OPEN v_bank_territories_ref FOR
         SELECT bnkt_code, bnkt_territory_name, bnkt_sht_desc, bnkt_bnk_code
           FROM tqc_bank_territories
        WHERE bnkt_bnk_code = v_bnkt_bnk_code;
   END;

   PROCEDURE get_agencies (
      v_agn_act_code       IN       tqc_agencies.agn_act_code%TYPE,
      v_all_agencies_ref   OUT      all_agencies_ref
   )
   IS
   BEGIN
      OPEN v_all_agencies_ref FOR
         SELECT agn_code, agn_act_code, agn_sht_desc, agn_name,
                agn_physical_address, agn_postal_address, agn_twn_code,
                agn_cou_code, agn_email_address, agn_web_address, agn_zip,
                agn_contact_person, agn_contact_title, agn_tel1, agn_tel2,
                agn_fax, agn_acc_no, agn_pin, agn_agent_commission,
                agn_credit_allowed, agn_agent_wht_tax, agn_print_dbnote,
                agn_status, agn_date_created, agn_created_by, agn_reg_code,
                agn_comm_reserve_rate, agn_annual_budget,
                agn_status_eff_date, agn_credit_period, agn_comm_stat_eff_dt,
                agn_comm_status_dt, agn_comm_allowed, agn_checked,
                agn_checked_by, agn_check_date, agn_comp_comm_arrears,
                agn_reinsurer, agn_brn_code, agn_town, agn_country,
                agn_status_desc, agn_id_no, agn_con_code, agn_agn_code,
                agn_sms_tel, agn_ahc_code, agn_sec_code, agn_agnc_class_code,
                agn_expiry_date, agn_license_no, agn_runoff, agn_licensed,
                agn_license_grace_pr, agn_old_acc_no, agn_status_remarks,
                brn_name, ahc_name, sec_name, agnc_class_desc
           FROM tqc_agencies,
                tqc_branches,
                tqc_agency_holding_company,
                tqc_sectors,
                tqc_agencies_classes
          WHERE agn_brn_code = brn_code
            AND agn_ahc_code = ahc_code
            AND agn_sec_code = sec_code
            AND agn_agnc_class_code = agnc_class_code
            AND agn_act_code = NVL (v_agn_act_code, agn_act_code);
   END get_agencies;

--   PROCEDURE get_agency_info (
--      v_agn_act_code      IN       tqc_agencies.agn_act_code%TYPE,
--      v_agency_info_ref   OUT      agency_info_ref
--   )
--   IS
--   BEGIN
--      --RAISE_ERROR('v_agn_act_code'||v_agn_act_code);
--      OPEN v_agency_info_ref FOR
--         SELECT   a.agn_code, a.agn_act_code, a.agn_sht_desc, a.agn_name,
--                  a.agn_physical_address, a.agn_postal_address,
--                  a.agn_twn_code, a.agn_cou_code, a.agn_email_address,
--                  a.agn_web_address, a.agn_zip, a.agn_contact_person,
--                  a.agn_contact_title, a.agn_tel1, a.agn_tel2, a.agn_fax,
--                  a.agn_acc_no, a.agn_pin, a.agn_agent_commission,
--                  a.agn_credit_allowed, a.agn_agent_wht_tax,
--                  a.agn_print_dbnote, a.agn_status, a.agn_date_created,
--                  a.agn_created_by, a.agn_reg_code, a.agn_comm_reserve_rate,
--                  a.agn_annual_budget, a.agn_status_eff_date,
--                  a.agn_credit_period, a.agn_comm_stat_eff_dt,
--                  a.agn_comm_status_dt, a.agn_comm_allowed, a.agn_checked,
--                  a.agn_checked_by, a.agn_check_date,
--                  a.agn_comp_comm_arrears, a.agn_reinsurer, a.agn_brn_code,
--                  a.agn_town, a.agn_country, a.agn_status_desc, a.agn_id_no,
--                  a.agn_con_code, a.agn_agn_code, a.agn_sms_tel,
--                  a.agn_ahc_code, a.agn_sec_code, a.agn_agnc_class_code,
--                  a.agn_expiry_date, a.agn_license_no, a.agn_runoff,
--                  a.agn_licensed, a.agn_license_grace_pr, a.agn_old_acc_no,
--                  a.agn_status_remarks, bnk_code, bnk_bank_name, bbr_code,
--                  bbr_branch_name, a.agn_bank_acc_no, a.agn_unique_prefix,
--                  cou_admin_reg_type, a.agn_state_code, sts_name,
--                  a.agn_crdt_rting, clnt_name, b.agn_name accountmanager,
--                  prom_transaction_date, prom_transaction_type,
--                  prom_demo_promo_type, brn_name, bra_name, brn_code,
--                  prom_bra_unt_sht_desc_prefix, prom_bru_unt_sht_desc_prefix,
--                  prom_bru_agn_seq_no, prom_precontract_agn_seq,
--                  cou_zip_code, b.agn_code, a.agn_credit_limit, bru_code,
--                  bru_name, a.agn_local_international,
--                  a.agn_regulator_number, a.agn_authorised, a.agn_rorg_code,
--                  a.agn_ors_code, rorg_desc, ors_desc, a.agn_allocate_cert,
--                  c.agn_name, a.agn_bounced_chq, a.agn_default_comm_mode,
--                  a.agn_bpn_code, bpn_name, a.agn_agent_type, a.agn_group,
--                  a.agn_main_agn_code, d.agn_name, a.agn_vat_applicable,
--                  a.agn_whtax_applicable,a.agn_tel_pay
--             FROM tqc_agencies a,
--                  tqc_agencies b,
--                  tqc_agencies c,
--                  tqc_agencies d,
--                  tqc_bank_branches,
--                  tqc_banks,
--                  tqc_countries,
--                  tqc_states,
--                  tqc_clients,
--                  lms_agent_promotion,
--                  tqc_branches,
--                  tqc_branch_agencies,
--                  tqc_branch_units,
--                  tqc_rating_organizations,
--                  tqc_org_rating_starndards,
--                  tqc_business_persons
--            WHERE a.agn_bbr_code = bbr_code(+)
--              AND a.agn_cou_code = cou_code(+)
--              AND bbr_bnk_code = bnk_code(+)
--              AND a.agn_state_code = sts_code(+)
--              AND a.agn_clnt_code = clnt_code(+)
--              AND c.agn_code(+) = a.agn_agn_code
--              AND d.agn_code(+) = a.agn_main_agn_code
--              AND a.agn_account_manager = b.agn_code(+)
--              AND a.agn_act_code = NVL (v_agn_act_code, a.agn_act_code)
--              AND prom_agn_code(+) = a.agn_code
--              AND brn_code(+) = prom_brn_code
--              AND bru_code(+) = a.agn_bru_code
--              AND bra_code(+) = prom_bra_code
--              AND rorg_code(+) = a.agn_rorg_code
--              AND a.agn_ors_code = ors_code(+)
--              AND bpn_code(+) = a.agn_bpn_code
--         -- AND BRU_BRN_CODE(+)=BRN_CODE
--         ORDER BY a.agn_name;
--   END get_agency_info;

 PROCEDURE get_agency_info (
      v_agn_act_code      IN       tqc_agencies.agn_act_code%TYPE,
      v_agency_info_ref   OUT      agency_info_ref
   )
   IS
   BEGIN
      --RAISE_ERROR('v_agn_act_code'||v_agn_act_code);
      OPEN v_agency_info_ref FOR
         SELECT   a.agn_code, a.agn_act_code, a.agn_sht_desc, a.agn_name,
                  a.agn_physical_address, a.agn_postal_address,
                  a.agn_twn_code, a.agn_cou_code, a.agn_email_address,
                  a.agn_web_address, a.agn_zip, a.agn_contact_person,
                  a.agn_contact_title, a.agn_tel1, a.agn_tel2, a.agn_fax,
                  a.agn_acc_no, a.agn_pin, a.agn_agent_commission,
                  a.agn_credit_allowed, a.agn_agent_wht_tax,
                  a.agn_print_dbnote, a.agn_status, a.agn_date_created,
                  a.agn_created_by, a.agn_reg_code, a.agn_comm_reserve_rate,
                  a.agn_annual_budget, a.agn_status_eff_date,
                  a.agn_credit_period, a.agn_comm_stat_eff_dt,
                  a.agn_comm_status_dt, a.agn_comm_allowed, a.agn_checked,
                  a.agn_checked_by, a.agn_check_date,
                  a.agn_comp_comm_arrears, a.agn_reinsurer, a.agn_brn_code,
                  a.agn_town, a.agn_country, a.agn_status_desc, a.agn_id_no,
                  a.agn_con_code, a.agn_agn_code, a.agn_sms_tel,
                  a.agn_ahc_code, a.agn_sec_code, a.agn_agnc_class_code,
                  a.agn_expiry_date, a.agn_license_no, a.agn_runoff,
                  a.agn_licensed, a.agn_license_grace_pr, a.agn_old_acc_no,
                  a.agn_status_remarks, bnk_code, bnk_bank_name, bbr_code,
                  bbr_branch_name, a.agn_bank_acc_no, a.agn_unique_prefix,
                  cou_admin_reg_type, a.agn_state_code, sts_name,
                  a.agn_crdt_rting, clnt_name, b.agn_name accountmanager,
                  prom_transaction_date, prom_transaction_type,
                  prom_demo_promo_type, brn_name, bra_name, brn_code,
                  prom_bra_unt_sht_desc_prefix, prom_bru_unt_sht_desc_prefix,
                  prom_bru_agn_seq_no, prom_precontract_agn_seq,
                  cou_zip_code, b.agn_code, a.agn_credit_limit, bru_code,
                  bru_name, a.agn_local_international,
                  a.agn_regulator_number, a.agn_authorised, a.agn_rorg_code,
                  a.agn_ors_code, rorg_desc, ors_desc, a.agn_allocate_cert,
                  c.agn_name, a.agn_bounced_chq, a.agn_default_comm_mode,
                  a.agn_bpn_code, bpn_name, a.agn_agent_type, a.agn_group,
                  a.agn_main_agn_code, d.agn_name, a.agn_vat_applicable,
                  a.agn_whtax_applicable, a.agn_tel_pay, a.agn_payment_freq,
                  a.agn_default_cpm_mode,
                  (SELECT cpm_desc
                     FROM tqc_clm_payment_modes
                    WHERE cpm_code = a.agn_default_cpm_mode)
                                                           agn_cpm_mode_desc,
                  a.agn_pymt_validated, a.agn_comm_levy_app,
                  a.agn_comm_levy_rate, a.agn_brr_code, a.agn_brr_name,
                  a.agn_auth_name, a.agn_updated_by, a.agn_updated_on
             FROM tqc_agencies a,
                  tqc_agencies b,
                  tqc_agencies c,
                  tqc_agencies d,
                  tqc_bank_branches,
                  tqc_banks,
                  tqc_countries,
                  tqc_states,
                  tqc_clients,
                  lms_agent_promotion,
                  tqc_branches,
                  tqc_branch_agencies,
                  tqc_branch_units,
                  tqc_rating_organizations,
                  tqc_org_rating_starndards,
                  tqc_business_persons
            WHERE a.agn_bbr_code = bbr_code(+)
              AND a.agn_cou_code = cou_code(+)
              AND bbr_bnk_code = bnk_code(+)
              AND a.agn_state_code = sts_code(+)
              AND a.agn_clnt_code = clnt_code(+)
              AND c.agn_code(+) = a.agn_agn_code
              AND d.agn_code(+) = a.agn_main_agn_code
              AND a.agn_account_manager = b.agn_code(+)
              AND a.agn_act_code = NVL (v_agn_act_code, a.agn_act_code)
              AND prom_agn_code(+) = a.agn_code
              AND brn_code(+) = prom_brn_code
              AND bru_code(+) = a.agn_bru_code
              AND bra_code(+) = prom_bra_code
              AND rorg_code(+) = a.agn_rorg_code
              AND a.agn_ors_code = ors_code(+)
              AND bpn_code(+) = a.agn_bpn_code
         -- AND BRU_BRN_CODE(+)=BRN_CODE
         ORDER BY a.agn_name;
   END get_agency_info; 
   PROCEDURE get_agency_infobynames (
      v_agn_act_code           IN       tqc_agencies.agn_act_code%TYPE,
      v_agn_name                        tqc_agencies.agn_name%TYPE,
      v_agency_info_ref        OUT      agency_info_by_name_ref,
      v_agn_physical_address   IN       VARCHAR2,
      v_agn_email_address      IN       VARCHAR2,
      v_agn_pin                IN       VARCHAR2,
      v_agn_tel1               IN       VARCHAR2
   )
   IS
   BEGIN
      --RAISE_ERROR('v_agn_physical_address'||v_agn_physical_address||'v_agn_email_address'||v_agn_email_address);
      --v_agn_email_address||'v_agn_pin'||v_agn_pin||'v_agn_tel1'||v_agn_tel1);
      OPEN v_agency_info_ref FOR
         SELECT DISTINCT agn_code, agn_act_code, agn_sht_desc, agn_name,
                         agn_physical_address, agn_postal_address,
                         agn_twn_code, agn_cou_code, agn_email_address,
                         agn_web_address, agn_zip, agn_contact_person,
                         agn_contact_title, agn_tel1, agn_tel2, agn_fax,
                         agn_acc_no, agn_pin, agn_agent_commission,
                         agn_credit_allowed, agn_agent_wht_tax,
                         agn_print_dbnote, agn_status, agn_date_created,
                         agn_created_by, agn_reg_code, agn_comm_reserve_rate,
                         agn_annual_budget, agn_status_eff_date,
                         agn_credit_period, agn_comm_stat_eff_dt,
                         agn_comm_status_dt, agn_comm_allowed, agn_checked,
                         agn_checked_by, agn_check_date,
                         agn_comp_comm_arrears, agn_reinsurer, agn_brn_code,
                         agn_town, agn_country, agn_status_desc, agn_id_no,
                         agn_con_code, agn_agn_code, agn_sms_tel,
                         agn_ahc_code, agn_sec_code, agn_agnc_class_code,
                         agn_expiry_date, agn_license_no, agn_runoff,
                         agn_licensed, agn_license_grace_pr, agn_old_acc_no,
                         agn_status_remarks, brn_name
                    FROM tqc_agencies, tqc_branches
                   WHERE (agn_act_code = NVL (v_agn_act_code, agn_act_code)
                         )
                     AND agn_brn_code = brn_code
                     AND (   (   (agn_name = NVL (v_agn_name, 'HAKUNA'))
                              OR (agn_name LIKE '%' || v_agn_name || '%')
                              OR INSTR (v_agn_name, agn_name) != 0
                             )
                          OR (   (agn_physical_address =
                                        NVL (v_agn_physical_address, 'HAKUNA')
                                 )
                              OR (agn_physical_address LIKE
                                          '%' || v_agn_physical_address || '%'
                                 )
                              OR INSTR (v_agn_physical_address,
                                        agn_physical_address
                                       ) != 0
                             )
                          OR (   (agn_email_address =
                                           NVL (v_agn_email_address, 'HAKUNA')
                                 )
                              OR (agn_email_address LIKE
                                             '%' || v_agn_email_address || '%'
                                 )
                              OR INSTR (v_agn_email_address,
                                        agn_email_address) != 0
                             )
                          OR (   (agn_pin = NVL (v_agn_pin, 'HAKUNA'))
                              OR (agn_pin LIKE '%' || v_agn_pin || '%')
                              OR INSTR (agn_pin, v_agn_pin) != 0
                             )
                          OR (   (agn_tel1 = NVL (v_agn_tel1, 'HAKUNA'))
                              OR (agn_tel1 LIKE '%' || v_agn_tel1 || '%')
                              OR INSTR (agn_tel1, v_agn_tel1) != 0
                             )
                         )
                ORDER BY agn_name,
                         agn_physical_address,
                         agn_email_address,
                         agn_pin,
                         agn_tel1;
   END get_agency_infobynames;

   PROCEDURE get_agency_classes (v_agency_classes_ref OUT agency_classes_ref)
   IS
   BEGIN
      OPEN v_agency_classes_ref FOR
         SELECT agnc_class_code, agnc_class_desc
           FROM tqc_agencies_classes;
   END get_agency_classes;

   PROCEDURE get_specific_agency_classes (
      v_agnc_class_code            tqc_agencies_classes.agnc_class_code%TYPE,
      v_agency_classes_ref   OUT   agency_classes_ref
   )
   IS
   BEGIN
      OPEN v_agency_classes_ref FOR
         SELECT agnc_class_code, agnc_class_desc
           FROM tqc_agencies_classes
          WHERE agnc_class_code = v_agnc_class_code;
   END get_specific_agency_classes;

   PROCEDURE get_specific_agency_classes (
      v_act_code                 IN       NUMBER DEFAULT NULL,
      tqc_account_types_values   OUT      tqc_account_types_cursor
   )
   IS
   BEGIN
      OPEN tqc_account_types_values FOR
         SELECT   act_code, act_account_type, act_type_sht_desc,
                  act_wthtx_rate, act_type_id, act_comm_reserve_rate,
                  act_max_adv_amt, act_max_adv_rpymt_prd,
                  act_rcpts_include_comm, act_overide_rate,
                  act_id_serial_format, act_vat_rate, act_format,
                  act_odl_code, odl_ranking || ' ' || odl_desc ranking,
                  act_no_gen_code,act_commision_levy_rate
             FROM tqc_account_types, tqc_org_division_levels
            WHERE act_code = NVL (v_act_code, act_code)
                  AND act_odl_code = odl_code(+)
         ORDER BY act_code, act_account_type;
   END;

   PROCEDURE search_account_types (
      v_act_code                 IN       NUMBER DEFAULT NULL,
      tqc_account_types_values   OUT      tqc_account_types_cursor
   )
   IS
   BEGIN
      OPEN tqc_account_types_values FOR
         SELECT   act_code, act_account_type, act_type_sht_desc,
                  act_wthtx_rate, act_type_id, act_comm_reserve_rate,
                  act_max_adv_amt, act_max_adv_rpymt_prd,
                  act_rcpts_include_comm, act_overide_rate,
                  act_id_serial_format, act_vat_rate, act_format,
                  act_odl_code, odl_ranking || ' ' || odl_desc ranking,
                  act_no_gen_code,act_commision_levy_rate
             FROM tqc_account_types, tqc_org_division_levels
            WHERE act_code = NVL (v_act_code, act_code)
                  AND act_odl_code = odl_code(+)
         ORDER BY act_code, act_account_type;
   END;

   PROCEDURE find_branches_code (branches OUT branch_record_cursor)
   IS
   BEGIN
      OPEN branches FOR
         SELECT brn_name, TO_CHAR (brn_code)
           FROM tqc_branches, tqc_regions, tqc_organizations
          WHERE brn_reg_code = reg_code
            AND reg_org_code = org_code;
   END;
   
   PROCEDURE find_regions(regions OUT region_record_cursor)
   IS
   BEGIN
      OPEN regions FOR
         SELECT reg_name, TO_CHAR (reg_code)
           FROM tqc_regions, tqc_organizations
          WHERE reg_org_code = org_code;
   END;

   PROCEDURE get_sectors (v_sectors_ref OUT sectors_ref)
   IS
   BEGIN
      OPEN v_sectors_ref FOR
         SELECT sec_code, sec_sht_desc, sec_name
           FROM tqc_sectors;
   END get_sectors;

   PROCEDURE get_specific_sectors (
      v_sec_code            tqc_sectors.sec_code%TYPE,
      v_sectors_ref   OUT   sectors_ref
   )
   IS
   BEGIN
      OPEN v_sectors_ref FOR
         SELECT sec_code, sec_sht_desc, sec_name
           FROM tqc_sectors
          WHERE sec_code = v_sec_code;
   END get_specific_sectors;

   PROCEDURE get_service_provider_types (
      v_service_provider_types_ref   OUT   service_provider_types_ref
   )
   IS
   BEGIN
      OPEN v_service_provider_types_ref FOR
         SELECT   spt_code, spt_sht_desc, spt_name, spt_status,
                  spt_whtx_rate, spt_vat_rate, spt_suffixes
             FROM tqc_service_provider_types
         ORDER BY spt_name;
   END get_service_provider_types;

   PROCEDURE get_service_provider_types_act (
      v_spt_code                     IN       NUMBER,
      v_service_provider_types_ref   OUT      serv_provider_types_act_ref
   )
   IS
   BEGIN
      --RAISE_ERROR('v_spt_code'||v_spt_code);
      OPEN v_service_provider_types_ref FOR
         SELECT spta_code, spta_spt_code, spta_sht_desc, spta_desc,
                a.msgt_msg, b.msgt_msg emailtemplate, spta_send_msg_default,
                spta_send_email_default, a.msgt_code, b.msgt_code,spta_report_days
           FROM tqc_serv_prv_type_actvts,
                tqc_msg_templates a,
                tqc_msg_templates b
          WHERE spta_spt_code = v_spt_code
            AND a.msgt_code(+) = spta_sms_msgt_code
            AND b.msgt_code(+) = spta_email_msgt_code;
   END get_service_provider_types_act;

   PROCEDURE get_service_provider_types_act (
      v_service_provider_types_ref   OUT   serv_provider_types_act_ref
   )
   IS
   BEGIN
      OPEN v_service_provider_types_ref FOR
         SELECT spta_code, spta_spt_code, spta_sht_desc, spta_desc, NULL,
                NULL, NULL, NULL, NULL, NULL,spta_report_days
           FROM tqc_serv_prv_type_actvts;
   END get_service_provider_types_act;

   PROCEDURE get_service_providers (
      v_spr_spt_code                  tqc_service_providers.spr_spt_code%TYPE,
      v_service_providers_ref   OUT   service_providers_ref
   )
   IS
   BEGIN
      --RAISE_ERROR('v_spr_spt_code '||v_spr_spt_code);
      OPEN v_service_providers_ref FOR
         SELECT   spr_code, spr_sht_desc, spr_name, spr_physical_address,
                  spr_postal_address, spr_twn_code, spr_cou_code,
                  spr_spt_code, spr_phone, spr_fax, spr_email, spr_title,
                  spr_zip, spr_wef, spr_wet, spr_contact, spr_aims_code,
                  spr_bbr_code, spr_bank_acc_no, spr_created_by,
                  spr_date_created, spr_status_remarks, spr_status,
                  spr_pin_number, spr_trs_occupation, spr_proff_body,
                  spr_pin, spr_doc_phone, spr_doc_email,
                  country_name (spr_cou_code) country_name,
                  town_name (spr_twn_code) town_name, bnk_code,
                  bnk_bank_name, bbr_branch_name, spr_inhouse,
                  spr_sms_number, spr_invoice_number, spr_clnt_code,
                  clnt_name || ' ' || clnt_other_names, spr_bpn_code,
                  bpn_name, spr_cont_person_phone, spr_contact_person,
                  spr_contact_email, spr_contact_tel, spr_tel_pay,
                  cou_zip_code,spr_reg_no,spr_postal_code,spr_id_type,spr_id_no
             FROM tqc_service_providers,
                  tqc_bank_branches,
                  tqc_banks,
                  tqc_clients,
                  tqc_business_persons,
                  tqc_countries
            WHERE spr_spt_code = NVL (v_spr_spt_code, spr_spt_code)
              AND spr_bbr_code = bbr_code(+)
              AND bbr_bnk_code = bnk_code(+)
              AND spr_clnt_code = clnt_code(+)
              AND spr_cou_code = cou_code(+)
              AND bpn_code(+) = spr_bpn_code
         ORDER BY spr_name;
   END get_service_providers;

   PROCEDURE get_service_prov_activities (
      v_service_prov_activities_ref   OUT   service_prov_activities_ref
   )
   IS
   BEGIN
      OPEN v_service_prov_activities_ref FOR
         SELECT spa_code, spa_spt_code, spa_spt_sht_desc, spa_spr_code,
                spa_spr_sht_desc, spt_main_act, spa_desc
           FROM tqc_serv_prv_activities;
   END get_service_prov_activities;

   PROCEDURE get_activities_by_provider (
      v_spa_spr_code                       tqc_serv_prv_activities.spa_spr_code%TYPE,
      v_activities_by_provider_ref   OUT   activities_by_provider_ref
   )
   IS
   BEGIN
      OPEN v_activities_by_provider_ref FOR
         SELECT spa_code, spa_spt_code, spa_spt_sht_desc, spa_spr_code,
                spa_spr_sht_desc, spt_main_act, spta_desc spa_desc, spt_name,
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

   PROCEDURE get_service_prov_systems (
      v_service_prov_systems_ref   OUT   service_prov_systems_ref
   )
   IS
   BEGIN
      OPEN v_service_prov_systems_ref FOR
         SELECT sps_code, sps_spr_code, sps_sys_code, sps_wef, sps_wet,
                sps_created_by
           FROM tqc_service_provider_systems;
   END get_service_prov_systems;

   PROCEDURE get_prov_unassigned_systems (
      v_provider_code                    tqc_service_providers.spr_code%TYPE,
      v_service_prov_systems_ref   OUT   systems_ref
   )
   IS
   BEGIN
      OPEN v_service_prov_systems_ref FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_main_form_name,
                sys_active, sys_dbase_username, sys_dbase_password,
                sys_dbase_string, sys_path, sys_org_code,
                sys_agn_main_frm_name, sys_kba_code, sys_signature_path
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND NOT EXISTS (
                   SELECT sps_code, sps_spr_code, sps_sys_code, sps_wef,
                          sps_wet, sps_created_by
                     FROM tqc_service_provider_systems
                    WHERE tqc_systems.sys_code =
                                     tqc_service_provider_systems.sps_sys_code
                      AND sps_spr_code = v_provider_code);
   END get_prov_unassigned_systems;

   PROCEDURE get_prov_assigned_systems (
      v_provider_code                    tqc_service_providers.spr_code%TYPE,
      v_service_prov_systems_ref   OUT   systems_ref
   )
   IS
   BEGIN
      OPEN v_service_prov_systems_ref FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_main_form_name,
                sys_active, sys_dbase_username, sys_dbase_password,
                sys_dbase_string, sys_path, sys_org_code,
                sys_agn_main_frm_name, sys_kba_code, sys_signature_path
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND EXISTS (
                   SELECT sps_code, sps_spr_code, sps_sys_code, sps_wef,
                          sps_wet, sps_created_by
                     FROM tqc_service_provider_systems
                    WHERE tqc_systems.sys_code =
                                     tqc_service_provider_systems.sps_sys_code
                      AND sps_spr_code = v_provider_code);
   END get_prov_assigned_systems;

   PROCEDURE get_systems (v_systems_ref OUT systems_ref)
   IS
   BEGIN
      OPEN v_systems_ref FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_main_form_name,
                sys_active, sys_dbase_username, sys_dbase_password,
                sys_dbase_string, sys_path, sys_org_code,
                sys_agn_main_frm_name, sys_kba_code, sys_signature_path
           FROM tqc_systems;
   END get_systems;

   PROCEDURE get_parameters (v_parameters_ref OUT parameters_ref)
   IS
   BEGIN
      OPEN v_parameters_ref FOR
         SELECT param_code, param_name, param_value, param_status,
                param_desc
           FROM tqc_parameters;
   END get_parameters;

   PROCEDURE get_gis_branches (branches OUT branch_record_cursor)
   IS
   BEGIN
      OPEN branches FOR
         SELECT brn_code, brn_name
           FROM tqc_branches, tqc_regions
          WHERE reg_org_code = (SELECT sys_org_code
                                  FROM tqc_systems
                                 WHERE sys_sht_desc = 'GIS')
            AND reg_code = brn_reg_code;
   END;

   PROCEDURE get_payment_modes (v_payment_modes_ref OUT payment_modes_ref)
   IS
   BEGIN
      OPEN v_payment_modes_ref FOR
         SELECT pmod_code, pmod_sht_desc, pmod_desc, pmod_naration,
                pmod_default
           FROM tqc_payment_modes;
   END get_payment_modes;

   PROCEDURE get_client_titles (
      v_client_titles_ref   OUT      client_titles_ref,
      v_clt_sht_desc        IN       VARCHAR2
   )
   IS
   BEGIN
      OPEN v_client_titles_ref FOR
         SELECT clt_code, clt_sht_desc, clnt_desc
           FROM tqc_client_titles
          WHERE UPPER (clt_sht_desc) LIKE '%' || UPPER (v_clt_sht_desc)
                                          || '%'
            AND ROWNUM < 20;
   END get_client_titles;

   PROCEDURE get_client_titles (v_client_titles_ref OUT client_titles_ref)
   IS
   BEGIN
      OPEN v_client_titles_ref FOR
         SELECT clt_code, clt_sht_desc, clnt_desc
           FROM tqc_client_titles;
   END get_client_titles;

   PROCEDURE get_allclient_titles (v_client_titles_ref OUT client_titles_ref)
   IS
   BEGIN
      OPEN v_client_titles_ref FOR
         SELECT clt_code, clt_sht_desc, clnt_desc
           FROM tqc_client_titles;
   END get_allclient_titles;

   PROCEDURE find_business_intro_users (v_cursor OUT tqc_users_cursor)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT usr_username usr_id, usr_name, usr_code usr_no
           FROM tqc_users
          WHERE usr_username NOT IN (SELECT DISTINCT intro_usr_id
                                                FROM gin_introducer
                                               WHERE intro_usr_id IS NOT NULL);
   END;

   FUNCTION getsubarealevels (
      v_sprsacode   tqc_authorization_levels.tqal_sprsa_code%TYPE
   )
      RETURN auth_levels_ref
   IS
      v_cursor   auth_levels_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqal_code, tqal_sprsa_code, tqal_level_id, tqal_srls_code,
                srls_name, sprsa_sub_area
           FROM tqc_authorization_levels,
                tqc_sys_roles,
                tqc_sys_process_sub_areas
          WHERE tqal_srls_code = srls_code
            AND tqal_sprsa_code = sprsa_code
            AND sprsa_code = v_sprsacode;

      RETURN v_cursor;
   END;

   FUNCTION getsystemsubareas (v_syscode NUMBER)
      RETURN system_subareas_ref
   IS
      v_cursor   system_subareas_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT sprsa_code, sprsa_sprca_code, sprsa_sprc_code,
                sprsa_sub_area, sprsa_type, sprsa_sht_desc,
                sprsa_default_usr_code
           FROM tqc_sys_process_sub_areas, tqc_sys_processes
          WHERE sprsa_sprc_code = sprc_code
            AND sprsa_type = 'A'
            AND sprc_sys_code = v_syscode;

      RETURN v_cursor;
   END;

   FUNCTION getsystemroles (v_syscode NUMBER)
      RETURN systemroles_ref
   IS
      v_cursor   systemroles_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT srls_code, srls_sys_code, srls_name, srls_crt_date,
                srls_sht_desc, srls_status
           FROM tqc_sys_roles
          WHERE srls_sys_code = v_syscode;

      RETURN v_cursor;
   END;

   PROCEDURE authlevelsproc (
      v_addedit     VARCHAR2,
      v_tqualcode   tqc_authorization_levels.tqal_code%TYPE,
      v_sprsacode   tqc_authorization_levels.tqal_sprsa_code%TYPE,
      v_levelid     tqc_authorization_levels.tqal_level_id%TYPE,
      v_srlscode    tqc_authorization_levels.tqal_srls_code%TYPE
   )
   IS
      v_level   NUMBER;
   BEGIN
      IF v_addedit = 'A'
      THEN
         SELECT NVL (MAX (tqal_level_id), 0) + 1
           INTO v_level
           FROM tqc_authorization_levels
          WHERE tqal_srls_code = v_srlscode AND tqal_sprsa_code = v_sprsacode;

         INSERT INTO tqc_authorization_levels
                     (tqal_code, tqal_sprsa_code, tqal_level_id,
                      tqal_srls_code
                     )
              VALUES (tqal_code_seq.NEXTVAL, v_sprsacode, v_level,
                      v_srlscode
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_authorization_levels
            SET tqal_srls_code = v_srlscode
          WHERE tqal_code = v_tqualcode;
      ELSE
         DELETE      tqc_authorization_levels
               WHERE tqal_code = v_tqualcode;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         raise_error (   'Error Manipulating Authorization level Record'
                      || SQLERRM (SQLCODE)
                     );
   END authlevelsproc;

--   FUNCTION getbankbrancheslov
--      RETURN bankbrancheslov_ref
--   IS
--      v_cursor   bankbrancheslov_ref;
--   BEGIN
--      OPEN v_cursor FOR
--         SELECT ALL tqc_banks.bnk_sht_desc, tqc_banks.bnk_code,
--                    tqc_banks.bnk_bank_name,
--                    tqc_bank_branches.bbr_branch_name,
--                    tqc_bank_branches.bbr_code, fms_bnk_accts.bct_no
--               FROM tqc_bank_branches, tqc_banks, fms_bnk_accts
--              WHERE tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code
--                AND bct_bbr_code = bbr_code
--                AND NVL (bbr_dd_supported, 'N') = 'Y'
--           ORDER BY 1, 2;

--      RETURN v_cursor;
--   END;
 FUNCTION getbankbrancheslov
      RETURN bankbrancheslov_ref
   IS
      v_cursor   bankbrancheslov_ref;
      v_client VARCHAR2(50);
   BEGIN
      BEGIN
      SELECT PARAM_VALUE INTO 
      v_client  
      FROM TQC_PARAMETERS
      WHERE PARAM_NAME = 'CLIENT';
      EXCEPTION WHEN OTHERS THEN 
        NULL;
      END; 
      
      IF NVL(v_client,'TURNKEY') = 'HERITAGE' THEN
      
      OPEN v_cursor FOR
      SELECT ALL tqc_banks.bnk_sht_desc,
                 tqc_banks.bnk_code,
                 tqc_banks.bnk_bank_name,
                  null bbr_branch_name,
                null  bbr_code,
                null bct_no
        FROM --tqc_bank_branches, 
        tqc_banks--, fms_bnk_accts
       WHERE --tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code(+)
         --AND bct_bbr_code = bbr_code(+)
         --AND 
         NVL(bnk_dd_supported, 'N') = 'Y'
       ORDER BY 1, 2;
  
    RETURN v_cursor;
    
      ELSE
      
      OPEN v_cursor FOR
         SELECT ALL tqc_banks.bnk_sht_desc, tqc_banks.bnk_code,
                    tqc_banks.bnk_bank_name,
                    tqc_bank_branches.bbr_branch_name,
                    tqc_bank_branches.bbr_code, fms_bnk_accts.bct_no
               FROM tqc_bank_branches, tqc_banks, fms_bnk_accts
              WHERE tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code
                AND bct_bbr_code = bbr_code
                AND NVL (bbr_dd_supported, 'N') = 'Y'
           ORDER BY 1, 2;

      RETURN v_cursor;
      END IF;
   END;

   FUNCTION bankbrancheslov
      RETURN bankbrancheslov_ref
   IS
      v_cursor   bankbrancheslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT tqc_banks.bnk_sht_desc, tqc_banks.bnk_code,
                         tqc_banks.bnk_bank_name,
                         tqc_bank_branches.bbr_branch_name,
                         tqc_bank_branches.bbr_code, NULL
                    FROM tqc_bank_branches, tqc_banks
                   WHERE tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code
                ORDER BY 3;

      RETURN v_cursor;
   END;

   FUNCTION bbranchesbank (v_bnkcode tqc_banks.bnk_code%TYPE)
      RETURN bankbrancheslov_ref
   IS
      v_cursor   bankbrancheslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT tqc_banks.bnk_sht_desc, tqc_banks.bnk_code,
                         tqc_banks.bnk_bank_name,
                         tqc_bank_branches.bbr_branch_name,
                         tqc_bank_branches.bbr_code, NULL
                    FROM tqc_bank_branches, tqc_banks
                   WHERE tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code
                     AND bnk_code = v_bnkcode
                ORDER BY 3;

      RETURN v_cursor;
   END;

   FUNCTION getholidays (v_year NUMBER)
      RETURN holidays_ref
   IS
      v_cursor   holidays_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   hld_date, hld_desc
             FROM tqc_holidays
            WHERE TO_NUMBER (TO_CHAR (hld_date, 'RRRR')) =
                          NVL (v_year, TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR')))
         ORDER BY hld_date;

      RETURN v_cursor;
   END;

   FUNCTION getyearslov
      RETURN yearslov_ref
   IS
      v_cursor   yearslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT TO_NUMBER (TO_CHAR (hld_date, 'RRRR')) h_year
                    FROM tqc_holidays
                ORDER BY 1;

      RETURN v_cursor;
   END;

   FUNCTION getdirectdebitnonreceipt
      RETURN directdebit_ref
   IS
      v_cursor   directdebit_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   dd_code, dd_ref_no, dd_sent_yn, dd_acnt_no, dd_book_date,
                  dd_bbr_code, dd_status, dd_receipted, dd_value_date,
                  dd_raised_by, dd_date, dd_bnk_code, dd_auth_by,
                  dd_auth_date, dd_authorized,
                  (SELECT bnk_bank_name || ' - '
                          || bbr_branch_name
                     FROM tqc_bank_branches, tqc_banks
                    WHERE bbr_bnk_code = bnk_code
                      AND bbr_code = dd_bbr_code) bank_branch
             FROM tqc_direct_debit
            WHERE NVL (dd_receipted, 'N') = 'N'
         ORDER BY dd_book_date DESC;

      RETURN v_cursor;
   END;

   FUNCTION getdirectdebitheader (v_dd_code NUMBER)
      RETURN directdebitheader_ref
   IS
      v_cursor   directdebitheader_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   ddh_code, ddh_dd_code, ddh_clnt_code, ddh_clnt_bbr_code,
                  ddh_bbr_bnk_code, ddh_clnt_sht_desc, ddh_clnt_name,
                  ddh_clnt_bank_acc_no, ddh_bbr_branch_name,
                  ddh_bbr_sht_desc, ddh_bbr_ref_code, ddh_tot_amt,
                  ddh_status, ddh_receipted, ddh_fail_date,
                  ddh_fail_updated_by, ddh_fail_update_date,
                  ddh_fail_remarks, ddh_relaunch_date,
                  ddh_relaunch_stop_date, ddh_relaunched_by,
                  ddh_relaunch_stoped_by, ddh_initial_book_date,
                  ddh_prev_ddh_code, ddh_acc_holder,
                  (SELECT bnk_bank_name || ' - '
                          || bbr_branch_name
                     FROM tqc_banks, tqc_bank_branches
                    WHERE bnk_code = bbr_bnk_code
                      AND bbr_code = ddh_clnt_bbr_code) bank_branch
             FROM tqc_direct_debit_header
            WHERE ddh_dd_code = v_dd_code
         ORDER BY ddh_clnt_name;

      RETURN v_cursor;
   END;

   FUNCTION getdirectdebitdetail (v_ddh_code NUMBER)
      RETURN directdebitdetail_ref
   IS
      v_cursor   directdebitdetail_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   ddd_code, ddd_ddh_code, ddd_dd_code, ddd_sys_code,
                  ddd_pol_code, ddd_pol_prp_code, ddd_pol_proposal_no,
                  ddd_pol_policy_no, ddd_other_identifier, ddd_amount,
                  ddd_remarks, ddd_start_dt, ddd_stop_date, ddd_status,
                  ddd_fail_date, ddd_receipted, ddd_fail_updated_by,
                  ddd_fail_update_date, ddd_ppr_code, ddd_pol_type,
                  ddd_receipted_by, ddd_receipt_no, ddd_receipt_date,
                  ddd_receipted_date, ddd_fail_remarks, ddd_relaunch_date,
                  ddd_relaunch_stop_date, ddd_relaunched_by,
                  ddd_relaunch_stoped_by
             FROM tqc_direct_debit_detail
            WHERE ddd_ddh_code = v_ddh_code
         ORDER BY ddd_pol_policy_no;

      RETURN v_cursor;
   END;

   FUNCTION getdirectdebitauthorised
      RETURN directdebit_ref
   IS
      v_cursor   directdebit_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   dd_code, dd_ref_no, dd_sent_yn, dd_acnt_no, dd_book_date,
                  dd_bbr_code, dd_status, dd_receipted, dd_value_date,
                  dd_raised_by, dd_date, dd_bnk_code, dd_auth_by,
                  dd_auth_date, dd_authorized,
                  (SELECT bnk_bank_name || ' - '
                          || bbr_branch_name
                     FROM tqc_bank_branches, tqc_banks
                    WHERE bbr_bnk_code = bnk_code
                      AND bbr_code = dd_bbr_code) bank_branch
             FROM tqc_direct_debit
            WHERE NVL (dd_authorized, 'N') = 'Y'
                  AND NVL (dd_status, 'D') = 'A'
         ORDER BY dd_date DESC;

      RETURN v_cursor;
   END;

   FUNCTION getallglaccounts
      RETURN glaccounts_ref
   IS
      v_cursor   glaccounts_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   acc_number, acc_name
             FROM fms_accounts
         ORDER BY acc_number;

      RETURN v_cursor;
   END;

   --SELECT  BRN_NAME, BRN_CODE,BRN_SHT_DESC FROM tqc_BRANCHES where brn_code !=-2000
   FUNCTION get_em_template (v_sys_code IN NUMBER)
      RETURN email_templates_ref
   IS
      v_cursor   email_templates_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type, msgt_prod_code,
                (SELECT pro_desc
                   FROM tq_gis.gin_products
                  WHERE pro_code = msgt_prod_code
                 UNION
                 SELECT prod_desc
                   FROM tq_lms.lms_products
                  WHERE prod_code = msgt_prod_code)
           FROM tqc_msg_templates
          WHERE msgt_sys_code = NVL (v_sys_code, msgt_sys_code);

      RETURN (v_cursor);
   END;

FUNCTION get_em_template_by_type (v_sys_code IN NUMBER, v_msg_type VARCHAR2)
      RETURN email_templates_ref
   IS
      v_cursor   email_templates_ref;
   BEGIN
      --RAISE_ERROR(v_sys_code||';'||v_msg_type);
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type, msgt_prod_code,
                (SELECT pro_desc
                   FROM tq_gis.gin_products
                  WHERE pro_code = msgt_prod_code
                 UNION
                 SELECT prod_desc
                   FROM tq_lms.lms_products
                  WHERE prod_code = msgt_prod_code)
           FROM tqc_msg_templates
          WHERE msgt_sys_code = NVL (v_sys_code, 0)
                AND msgt_type = v_msg_type;

      RETURN (v_cursor);
   END;
   FUNCTION getprospects
      RETURN prospects_ref
   IS
      v_cursor   prospects_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   prs_code, prs_surname, prs_physical_addrs,
                  prs_postal_addrs, prs_other_names, prs_tel_no,
                  prs_mobile_no, prs_email_addrs, prs_id_reg_no, prs_dob,
                  prs_pin, prs_twn_code, prs_cou_code, prs_postal_code,
                  country_name (prs_cou_code) country_name,
                  town_name (prs_twn_code) town_name, pst_desc,prs_type,prs_contact,prs_contact_tel
             FROM tqc_prospects, tqc_postal_codes
            WHERE prs_postal_code = pst_code(+)
         ORDER BY prs_surname;

      RETURN v_cursor;
   END;

--   FUNCTION getallsystems
--      RETURN all_systems_ref
--   IS
--      v_cursor   all_systems_ref;
--   BEGIN
--      OPEN v_cursor FOR
--         SELECT   sys_code, sys_sht_desc, sys_name, sys_main_form_name,
--                  sys_active, sys_dbase_username, sys_dbase_password,
--                  sys_dbase_string, sys_path, sys_org_code,
--                  sys_agn_main_frm_name, sys_kba_code, sys_signature_path,
--                  sys_template
--             FROM tqc_systems
--            WHERE sys_active <> 'N' AND sys_code <> 30 AND sys_code <> 26
--         ORDER BY sys_name;

--      RETURN v_cursor;
--   END;

   FUNCTION getorgdivlevelstype (v_sys_code IN NUMBER)
      RETURN orgdivlevelstype_ref
   IS
      v_cursor   orgdivlevelstype_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   dlt_code, dlt_sys_code, dlt_desc, dlt_act_code,
                  acc_type_name (dlt_act_code) acc_type_name, dlt_code_val
             FROM tqc_org_division_levels_type
            WHERE dlt_sys_code = v_sys_code
         ORDER BY dlt_desc;

      RETURN v_cursor;
   END;

   FUNCTION getorgdivisionlevels (v_dlt_code IN VARCHAR2)
      RETURN orgdivisionlevels_ref
   IS
      v_cursor   orgdivisionlevels_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   odl_code, odl_dlt_code, odl_desc, odl_ranking,
                  NVL (odl_type, 'O') odl_type
             FROM tqc_org_division_levels
            WHERE odl_dlt_code = v_dlt_code
         ORDER BY odl_ranking;

      RETURN v_cursor;
   END;

   FUNCTION getorgdivisionlevelsbyranking (
      v_dlt_code   IN   VARCHAR2,
      v_ranking    IN   tqc_org_division_levels.odl_ranking%TYPE
   )
      RETURN orgdivisionlevels_ref
   IS
      v_cursor   orgdivisionlevels_ref;
   BEGIN
      -- 9999 means that v_rank is null
      IF v_ranking = 9999
      THEN
         OPEN v_cursor FOR
            SELECT odl_code, odl_dlt_code, odl_desc, odl_ranking,
                   NVL (odl_type, 'O') odl_type
              FROM tqc_org_division_levels
             WHERE odl_dlt_code = v_dlt_code;

         RETURN v_cursor;
      ELSE
         OPEN v_cursor FOR
            SELECT   odl_code, odl_dlt_code, odl_desc, odl_ranking,
                     NVL (odl_type, 'O') odl_type
                FROM tqc_org_division_levels
               WHERE odl_dlt_code = v_dlt_code AND odl_ranking > v_ranking
            ORDER BY odl_ranking;

         RETURN v_cursor;
      END IF;
   END;

   FUNCTION getorgsubdivisionsbysys (v_sys_code IN NUMBER)
      RETURN orgsubdivisions_ref
   IS
      v_cursor   orgsubdivisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   osd_code, osd_parent_osd_code, osd_dlt_code, osd_odl_code,
                  osd_name, osd_wef, osd_div_head_agn_code, osd_sys_code,
                  dlt_desc, odl_desc,
                  agn_name (osd_div_head_agn_code) agent_name, odl_ranking,
                  NVL (odl_type, 'O') odl_type, osd_brn_code, osd_reg_code,
                  osd_post_level, osd_manager_allwd, osd_over_comm_earn,
                  osd_id, osd_parent_id
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

   FUNCTION getlowestorgsubdivsbysys (v_sys_code IN NUMBER, v_ac_type NUMBER)
      RETURN orgsubdivisions_ref
   IS
      v_cursor   orgsubdivisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   osd_code, osd_parent_osd_code, osd_dlt_code, osd_odl_code,
                  osd_name, osd_wef, osd_div_head_agn_code, osd_sys_code,
                  dlt_desc, odl_desc,
                  agn_name (osd_div_head_agn_code) agent_name, odl_ranking,
                  NVL (odl_type, 'O') odl_type, osd_brn_code, osd_reg_code,
                  osd_post_level, osd_manager_allwd, osd_over_comm_earn,
                  osd_id, osd_parent_id
             FROM tqc_org_subdivisions,
                  tqc_org_division_levels_type,
                  tqc_org_division_levels
            WHERE osd_sys_code = v_sys_code
              AND osd_dlt_code = dlt_code
              AND osd_odl_code = odl_code
              AND osd_code NOT IN (SELECT DISTINCT osd_parent_osd_code
                                              FROM tqc_org_subdivisions
                                             WHERE osd_parent_osd_code IS NOT NULL)
              AND dlt_act_code = v_ac_type
         ORDER BY osd_name;

      RETURN v_cursor;
   END;

   FUNCTION getorgsubdivisionsbydlt (v_dlt_code IN VARCHAR2)
      RETURN orgsubdivisions_ref
   IS
      v_cursor   orgsubdivisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   osd_code, osd_parent_osd_code, osd_dlt_code, osd_odl_code,
                  osd_name, osd_wef, osd_div_head_agn_code, osd_sys_code,
                  dlt_desc, odl_desc,
                  agn_name (osd_div_head_agn_code) agent_name, odl_ranking,
                  NVL (odl_type, 'O') odl_type, osd_brn_code, osd_reg_code,
                  osd_post_level, osd_manager_allwd, osd_over_comm_earn,
                  osd_id, osd_parent_id
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

   FUNCTION getorgsubdivisionsbysubdiv (v_osd_code IN VARCHAR2)
      RETURN orgsubdivisions_ref
   IS
      v_cursor   orgsubdivisions_ref;
   BEGIN
      IF v_osd_code IS NULL
      THEN
         OPEN v_cursor FOR
            SELECT   osd_code, osd_parent_osd_code, osd_dlt_code,
                     osd_odl_code, osd_name, osd_wef, osd_div_head_agn_code,
                     osd_sys_code, dlt_desc, odl_desc,
                     agn_name (osd_div_head_agn_code) agent_name,
                     odl_ranking, NVL (odl_type, 'O') odl_type, osd_brn_code,
                     osd_reg_code, osd_post_level, osd_manager_allwd,
                     osd_over_comm_earn, osd_id, osd_parent_id
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
            SELECT   osd_code, osd_parent_osd_code, osd_dlt_code,
                     osd_odl_code, osd_name, osd_wef, osd_div_head_agn_code,
                     osd_sys_code, dlt_desc, odl_desc,
                     agn_name (osd_div_head_agn_code) agent_name,
                     odl_ranking, NVL (odl_type, 'O') odl_type, osd_brn_code,
                     osd_reg_code, osd_post_level, osd_manager_allwd,
                     osd_over_comm_earn, osd_id, osd_parent_id
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

   FUNCTION getallagencieslov
      RETURN agencieslov_ref
   IS
      v_cursor   agencieslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   agn_code, agn_sht_desc, agn_name
             FROM tqc_agencies
         ORDER BY agn_name;

      RETURN v_cursor;
   END;

   FUNCTION getallmarketers (v_actcode NUMBER DEFAULT NULL)
      RETURN agencieslov_ref
   IS
      v_cursor   agencieslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   agn_code, agn_sht_desc, agn_name
             FROM tqc_agencies
            WHERE agn_act_code = NVL (v_actcode, 10)
         UNION
         SELECT   NULL, 'NONE', 'NONE'
             FROM DUAL
         ORDER BY 1 NULLS FIRST, 3;

      RETURN v_cursor;
   END;

   FUNCTION getspecificmarketers (v_agn_code IN NUMBER)
      RETURN agencieslov_ref
   IS
      v_cursor   agencieslov_ref;
   BEGIN
      IF v_agn_code IS NULL
      THEN
         OPEN v_cursor FOR
            SELECT   agn_code, agn_sht_desc, agn_name
                FROM tqc_agencies
               WHERE agn_act_code = 10
            ORDER BY agn_name;

         RETURN v_cursor;
      ELSE
         OPEN v_cursor FOR
            SELECT   agn_code, agn_sht_desc, agn_name
                FROM tqc_agencies
               WHERE agn_act_code = 10 AND agn_code != v_agn_code
            ORDER BY agn_name;

         RETURN v_cursor;
      END IF;
   END;

   FUNCTION getagencyactivities (v_agn_code IN NUMBER)
      RETURN agencyactivities_ref
   IS
      v_cursor   agencyactivities_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   aac_code, aac_acty_code, aac_wef, aac_estimate_wet,
                  aac_actual_wet, aac_remarks, aac_agn_code, aac_clnt_code,
                  aac_spr_code, aac_sys_code, aac_mktr_agn_code,
                  activity_desc (aac_acty_code) acty_desc,
                  clnt_name (aac_clnt_code) client_name,
                  agn_name (aac_agn_code) agency_name,
                  agn_name (aac_mktr_agn_code) mktr_agency_name,
                  provider_name (aac_spr_code) provider_name,
                  system_name (aac_sys_code) system_name
             FROM tqc_agency_activities
            WHERE aac_agn_code = v_agn_code
         ORDER BY aac_code;

      RETURN v_cursor;
   END;

   FUNCTION getagencyactivitiesdetails (v_agn_code IN NUMBER)
      RETURN agencyactivitiesdetails_ref
   IS
      v_cursor   agencyactivitiesdetails_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   aac_code, aac_acty_code, aac_wef, aac_estimate_wet,
                  aac_actual_wet, aac_remarks, aac_agn_code, aac_sys_code,
                  aac_by_code, aac_by_type,
                  activity_desc (aac_acty_code) acty_desc,
                  getagency_name (aac_by_code, aac_by_type) agency_name,
                  system_name (aac_sys_code) system_name,
                  account_type_name (aac_by_type) acc_type_name, aac_reasons
             FROM tqc_agency_activities
            WHERE aac_agn_code = v_agn_code
         ORDER BY aac_code;

      RETURN v_cursor;
   END;

   FUNCTION clnt_name (v_client_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT clnt_name
        INTO v_ret_val
        FROM tqc_clients
       WHERE clnt_code = v_client_code;

      RETURN v_ret_val;
   END clnt_name;

   FUNCTION agn_name (v_agn_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT agn_name
        INTO v_ret_val
        FROM tqc_agencies
       WHERE agn_code = v_agn_code;

      RETURN v_ret_val;
   END agn_name;

   FUNCTION activity_desc (v_acty_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT acty_desc
        INTO v_ret_val
        FROM tqc_activity_types
       WHERE acty_code = v_acty_code;

      RETURN v_ret_val;
   END activity_desc;

   FUNCTION provider_name (v_spr_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT spr_name
        INTO v_ret_val
        FROM tqc_service_providers
       WHERE spr_code = v_spr_code;

      RETURN v_ret_val;
   END provider_name;

   FUNCTION system_name (v_sys_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT sys_name
        INTO v_ret_val
        FROM tqc_systems
       WHERE sys_code = v_sys_code;

      RETURN v_ret_val;
   END system_name;

   FUNCTION getallactivitytypes
      RETURN activitytypes_ref
   IS
      v_cursor   activitytypes_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   acty_code, acty_sys_code, acty_desc
             FROM tqc_activity_types
         ORDER BY acty_desc;

      RETURN v_cursor;
   END;

   FUNCTION getallactivitytypesbysys (v_sys_code NUMBER)
      RETURN activitytypes_ref
   IS
      v_cursor   activitytypes_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   acty_code, acty_sys_code, acty_desc
             FROM tqc_activity_types
            WHERE acty_sys_code = v_sys_code
         ORDER BY acty_desc;

      RETURN v_cursor;
   END;

   FUNCTION getactivitytypesbysystem (v_sys_code NUMBER)
      RETURN activitytypes_ref
   IS
      v_cursor   activitytypes_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   acty_code, acty_sys_code, acty_desc
             FROM tqc_activity_types
            WHERE acty_sys_code = v_sys_code
         ORDER BY acty_desc;

      RETURN v_cursor;
   END;

   FUNCTION getallclientslov
      RETURN clientslov_ref
   IS
      v_cursor   clientslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   clnt_code, clnt_sht_desc, clnt_name
             FROM tqc_clients
         ORDER BY clnt_name;

      RETURN v_cursor;
   END;

   FUNCTION getallserviceprovlov
      RETURN serviceproviderlov_ref
   IS
      v_cursor   serviceproviderlov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   spr_code, spr_sht_desc, spr_name
             FROM tqc_service_providers
         ORDER BY spr_name;

      RETURN v_cursor;
   END;

   FUNCTION getallcountries
      RETURN countries_ref
   IS
      v_cursor   countries_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   cou_code, cou_sht_desc, cou_name, cou_base_curr,
                  cur_desc cou_base_curr_desc, cou_nationality, cou_zip_code,
                  cou_admin_reg_type, cou_admin_reg_mandatory, cou_schegen,
                  cou_emb_code, cou_curr_serial, cou_mobile_prefix,
                  cou_client_number
             FROM tqc_countries, tqc_currencies
            WHERE cou_base_curr = cur_code(+)
         ORDER BY cou_name;

      RETURN v_cursor;
   END;

  FUNCTION gettownsbycountry (v_cou_code IN NUMBER)
      RETURN towns_ref
   IS
      v_cursor   towns_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   twn_code, twn_cou_code, twn_sht_desc, twn_name,
                  twn_sts_code,NULL,pst_zip_code
             FROM tqc_towns,tqc_postal_codes
            WHERE twn_cou_code = v_cou_code and twn_code = pst_twn_code
         ORDER BY twn_name;

      RETURN v_cursor;
   END;

   FUNCTION gettownsbystate (v_state_code IN NUMBER)
      RETURN towns_ref
   IS
      v_cursor   towns_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   twn_code, twn_cou_code, twn_sht_desc, twn_name,
                  twn_sts_code, pst_desc, pst_zip_code
             FROM tqc_towns, tqc_postal_codes
            WHERE twn_code = pst_twn_code(+) AND twn_sts_code = v_state_code
         ORDER BY twn_name;

      RETURN v_cursor;
   END;

   FUNCTION country_name (v_cou_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT cou_name
        INTO v_ret_val
        FROM tqc_countries
       WHERE cou_code = v_cou_code;

      RETURN v_ret_val;
   END country_name;

   FUNCTION town_name (v_twn_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT twn_name
        INTO v_ret_val
        FROM tqc_towns
       WHERE twn_code = v_twn_code;

      RETURN v_ret_val;
   END town_name;

   FUNCTION getholidaydefinitions (v_cou_code IN NUMBER)
      RETURN holidaysdef_ref
   IS
      v_cursor   holidaysdef_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   thd_desc, thd_day, thd_mon, thd_status, thd_cou_code
             FROM tqc_holidays_definitions
            WHERE thd_cou_code = v_cou_code
         ORDER BY thd_desc;

      RETURN v_cursor;
   END;

   FUNCTION getpostalcodesbytown (v_twn_code NUMBER)
      RETURN postal_code_by_town_ref
   IS
      v_cursor   postal_code_by_town_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   pst_code, pst_twn_code, pst_desc, pst_zip_code
             FROM tqc_postal_codes
            WHERE pst_twn_code = v_twn_code
         ORDER BY pst_desc;

      RETURN v_cursor;
   END;

   FUNCTION acc_type_name (v_act_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT act_account_type
        INTO v_ret_val
        FROM tqc_account_types
       WHERE act_code = v_act_code;

      RETURN v_ret_val;
   END acc_type_name;

   FUNCTION getallaccounttypeslov
      RETURN accounttypeslov_ref
   IS
      v_cursor   accounttypeslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   act_code, act_type_sht_desc, act_account_type
             FROM tqc_account_types
         ORDER BY act_account_type;

      RETURN v_cursor;
   END;

   FUNCTION getallabudgettypeslov
      RETURN accounttypeslov_ref
   IS
      v_cursor   accounttypeslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   act_code, act_type_sht_desc, act_account_type
             FROM tqc_account_types
            WHERE act_code IN (SELECT agn_act_code
                                 FROM tqc_agencies)
         ORDER BY act_account_type;

      RETURN v_cursor;
   END;

FUNCTION gethierarchyacctypeslov (v_sys_code NUMBER)
      RETURN accounttypeslov_ref
   IS
      v_cursor   accounttypeslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   act_code, act_type_sht_desc, act_account_type
             FROM tqc_account_types
            
         ORDER BY act_account_type;

      RETURN v_cursor;
   END;

   FUNCTION getorgsubdivprevheads (v_osd_code IN VARCHAR2)
      RETURN orgsubdivprevheads_ref
   IS
      v_cursor   orgsubdivprevheads_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   osdph_code, osdph_osd_code, osdph_prev_agn_code, osdph_wet,
                  osd_name, agn_name (osdph_prev_agn_code) agency_name,
                  osdph_osd_id
             FROM tqc_org_subdiv_prev_heads, tqc_org_subdivisions
            WHERE osdph_osd_code = osd_code AND osdph_osd_code = v_osd_code
         ORDER BY osdph_code;

      RETURN v_cursor;
   END;

   FUNCTION getsyspostlevelsbysys (v_sys_code NUMBER)
      RETURN syspostlevels_ref
   IS
      v_cursor   syspostlevels_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   syspl_sys_code, syspl_code, syspl_sht_desc, syspl_desc,
                  syspl_ranking, syspl_wef
             FROM tqc_sys_post_levels
            WHERE syspl_sys_code = v_sys_code
         ORDER BY syspl_desc;

      RETURN v_cursor;
   END;

   FUNCTION getsyspostsbysystem (v_sys_code NUMBER)
      RETURN syspost_ref
   IS
      v_cursor   syspost_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   spost_sys_code, spost_syspl_code, spost_parent_spost_code,
                  spost_code, spost_sht_desc, spost_desc, spost_remarks,
                  spost_wef, spost_brn_code, spost_subdiv_osd_code,
                  spost_usr_code
             FROM tqc_sys_posts
            WHERE spost_sys_code = v_sys_code
         ORDER BY spost_desc;

      RETURN v_cursor;
   END;

   FUNCTION getsyspostsbylevel (v_syspl_code NUMBER)
      RETURN syspost_ref
   IS
      v_cursor   syspost_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   spost_sys_code, spost_syspl_code, spost_parent_spost_code,
                  spost_code, spost_sht_desc, spost_desc, spost_remarks,
                  spost_wef, spost_brn_code, spost_subdiv_osd_code,
                  spost_usr_code
             FROM tqc_sys_posts
            WHERE spost_syspl_code = v_syspl_code
         ORDER BY spost_desc;

      RETURN v_cursor;
   END;

   FUNCTION getsyspostsbypost (v_spost_code NUMBER)
      RETURN syspost_ref
   IS
      v_cursor   syspost_ref;
   BEGIN
      IF v_spost_code IS NULL
      THEN
         OPEN v_cursor FOR
            SELECT   spost_sys_code, spost_syspl_code,
                     spost_parent_spost_code, spost_code, spost_sht_desc,
                     spost_desc, spost_remarks, spost_wef, spost_brn_code,
                     spost_subdiv_osd_code, spost_usr_code
                FROM tqc_sys_posts
               WHERE spost_parent_spost_code IS NULL
            ORDER BY spost_desc;

         RETURN v_cursor;
      ELSE
         OPEN v_cursor FOR
            SELECT   spost_sys_code, spost_syspl_code,
                     spost_parent_spost_code, spost_code, spost_sht_desc,
                     spost_desc, spost_remarks, spost_wef, spost_brn_code,
                     spost_subdiv_osd_code, spost_usr_code
                FROM tqc_sys_posts
               WHERE spost_parent_spost_code = v_spost_code
            ORDER BY spost_desc;

         RETURN v_cursor;
      END IF;
   END;

   FUNCTION getsysprcsssubarealmts (v_sprsa_code NUMBER)
      RETURN sysprcsssubarealmts_ref
   IS
      v_cursor   sysprcsssubarealmts_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   spsat_code, spsat_sprsa_code, spsat_no_of_level,
                  spsat_min_limit, spsat_max_limit
             FROM tqc_sys_prcss_subarea_lmts
            WHERE spsat_sprsa_code = v_sprsa_code
         ORDER BY spsat_code;

      RETURN v_cursor;
   END;

   FUNCTION getsysprcsssubarealvls (v_spsat_code NUMBER)
      RETURN sysprcsssubarealvls_ref
   IS
      v_cursor   sysprcsssubarealvls_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   spsal_code, spsal_sprsa_code, spsal_spsat_code,
                  spsal_level, spsal_approver_type, spsal_approver_id,
                  usr_username
             FROM tqc_sys_prcss_subarea_lvls, tqc_users
            WHERE spsal_approver_id = usr_code(+)
                  AND spsal_spsat_code = v_spsat_code
         ORDER BY spsal_code;

      RETURN v_cursor;
   END;

   FUNCTION bank_name (v_bnk_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT bnk_bank_name
        INTO v_ret_val
        FROM tqc_banks
       WHERE bnk_code = v_bnk_code;

      RETURN v_ret_val;
   END bank_name;

   FUNCTION getddfwdingbankslov
      RETURN ddfwdingbanks_ref
   IS
      v_cursor   ddfwdingbanks_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT NULL bnk_code, NULL ddr_report_description,
                         NULL bnk_bank_name
                    FROM DUAL
         UNION
         SELECT DISTINCT bnk_code, ddr_report_description,
                         tqc_banks.bnk_bank_name
                    FROM tqc_bank_branches,
                         tqc_banks,
                         tqc_direct_debit_reports,
                         fms_bnk_accts
                   WHERE tqc_bank_branches.bbr_bnk_code = tqc_banks.bnk_code
                     AND bct_bbr_code = bbr_code
                     AND ddr_code(+) = bnk_ddr_code
                     AND NVL (bbr_dd_supported, 'N') = 'Y'
                ORDER BY bnk_bank_name DESC;

      RETURN v_cursor;
   END;

   FUNCTION getdirectdebitreports
      RETURN directdebitreports_ref
   IS
      v_cursor   directdebitreports_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT ALL ddr_code, ddr_report_description
               FROM tqc_direct_debit_reports
           ORDER BY ddr_code;

      RETURN v_cursor;
   END;

   FUNCTION ddreport_desc (v_ddr_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT ddr_report_description
        INTO v_ret_val
        FROM tqc_direct_debit_reports
       WHERE ddr_code = v_ddr_code;

      RETURN v_ret_val;
   END ddreport_desc;

   FUNCTION getusersbytypegroup
      RETURN users_ref
   IS
      v_cursor   users_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT usr_code, usr_username, usr_name, usr_last_date,
                usr_dt_created, usr_status, usr_pwd, usr_created_by,
                usr_email, usr_per_rank_id, usr_per_rank_sht_desc,
                usr_per_id, usr_personel_rank, usr_pwd_changed,
                usr_pwd_reset, usr_login_attempts, usr_sign, usr_ref,
                usr_type, usr_signature, usr_acct_mgr
           FROM tqc_users
          WHERE usr_type = 'G';

      RETURN v_cursor;
   END;

   FUNCTION getusersbytypeuser
      RETURN users_ref
   IS
      v_cursor   users_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT usr_code, usr_username, usr_name, usr_last_date,
                usr_dt_created, usr_status, usr_pwd, usr_created_by,
                usr_email, usr_per_rank_id, usr_per_rank_sht_desc,
                usr_per_id, usr_personel_rank, usr_pwd_changed,
                usr_pwd_reset, usr_login_attempts, usr_sign, usr_ref,
                usr_type, usr_signature, usr_acct_mgr
           FROM tqc_users
          WHERE usr_type = 'U';

      RETURN v_cursor;
   END;

   FUNCTION currency_sysmbol (v_cur_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (15);
   BEGIN
      SELECT cur_symbol
        INTO v_ret_val
        FROM tqc_currencies
       WHERE cur_code = v_cur_code;

      RETURN v_ret_val;
   END currency_sysmbol;

   FUNCTION state_name (v_sts_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (50);
   BEGIN
      SELECT sts_name
        INTO v_ret_val
        FROM tqc_states
       WHERE sts_code = v_sts_code;

      RETURN v_ret_val;
   END state_name;

   FUNCTION admin_region_name (v_admin_region_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (50);
   BEGIN
      SELECT adm_reg_name
        INTO v_ret_val
        FROM tqc_admin_regions
       WHERE adm_reg_code = v_admin_region_code;

      RETURN v_ret_val;
   END admin_region_name;

   FUNCTION system_param_val (v_param_name VARCHAR2)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT NVL (param_value, 'N')
        INTO v_ret_val
        FROM tqc_parameters
       WHERE param_name = v_param_name;

      RETURN v_ret_val;
   END system_param_val;

   FUNCTION check_activity_exist (v_param_name VARCHAR2)
      RETURN check_activity_exist_ref
   IS
      v_cursor   check_activity_exist_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT COUNT (1) AS v_count
           FROM tqc_activity_types
          WHERE acty_desc = v_param_name;

      RETURN v_cursor;
   END;

   FUNCTION check_hierarchy_exist (v_param_name VARCHAR2)
      RETURN check_activity_exist_ref
   IS
      v_cursor   check_activity_exist_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT COUNT (1) AS v_count
           FROM tqc_org_division_levels
          WHERE odl_code = v_param_name;

      RETURN v_cursor;
   END;

   FUNCTION check_hierarchy_ranking_exist (
      v_odlt_code   VARCHAR2,
      v_rank        tqc_org_division_levels.odl_ranking%TYPE
   )
      RETURN check_activity_exist_ref
   IS
      v_cursor   check_activity_exist_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT COUNT (1) AS v_count
           FROM tqc_org_division_levels
          WHERE odl_dlt_code = v_odlt_code AND odl_ranking = v_rank;

      RETURN v_cursor;
   END;

   FUNCTION checkif_dob_required
      RETURN checkif_dob_required_ref
   IS
      v_cursor   checkif_dob_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar
                                             ('CLIENT_DATE_OF_BIRTH_REQUIRED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION checkif_sms_required
      RETURN checkif_dob_required_ref
   IS
      v_cursor   checkif_dob_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar ('CLIENT_SMS_REQUIRED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION checkif_default_date
      RETURN checkif_dob_required_ref
   IS
      v_cursor   checkif_dob_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar
                                                  ('CLIENT _DEFAULT_DOB_DATE')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION check_max_rank (v_dlt_code IN VARCHAR2)
      RETURN check_max_rank_ref
   IS
      v_cursor   check_max_rank_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT MAX (tqc_org_division_levels.odl_ranking)
           FROM tqc_org_division_levels
          WHERE odl_dlt_code = v_dlt_code;

      RETURN v_cursor;
   END;

   FUNCTION checkif_uppercase_required
      RETURN checkif_dob_required_ref
   IS
      v_cursor   checkif_dob_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar
                                                 ('CLIENT_UPPERCASE_REQUIRED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION checkifsaccorequired
      RETURN checkif_dob_required_ref
   IS
      v_cursor   checkif_dob_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar ('SACCO_ENEBLED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION checkif_divisions_applic
      RETURN checkif_dob_required_ref
   IS
      v_cursor   checkif_dob_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar ('DIVISIONS_APPLIC')
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
   FUNCTION get_agencyname (v_agn_type VARCHAR2, v_ag_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      IF v_agn_type = 'CL'
      THEN
         SELECT clnt_name
           INTO v_ret_val
           FROM tqc_clients
          WHERE clnt_code = v_ag_code;

         RETURN v_ret_val;
      ELSIF v_agn_type = 'SP'
      THEN
         SELECT spr_name
           INTO v_ret_val
           FROM tqc_service_providers
          WHERE spr_code = v_ag_code;

         RETURN v_ret_val;
      ELSIF v_agn_type = 'MK'
      THEN
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
   FUNCTION checkif_user_active (v_client_code tqc_clients.clnt_status%TYPE)
      RETURN checkif_user_active_ref
   IS
      v_cursor   checkif_user_active_ref;
   BEGIN
      --RAISE_ERROR('v_client_code'||v_client_code);
      OPEN v_cursor FOR
         SELECT clnt_status
           FROM tqc_clients
          WHERE clnt_code = v_client_code;

      RETURN v_cursor;
   END;

   FUNCTION sector_name (v_sec_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT sec_name
        INTO v_ret_val
        FROM tqc_sectors
       WHERE sec_code = v_sec_code;

      RETURN v_ret_val;
   END sector_name;

   FUNCTION parent_company_name (v_parent_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      SELECT clnt_name
        INTO v_ret_val
        FROM tqc_clients
       WHERE clnt_code = v_parent_code;

      RETURN v_ret_val;
   END parent_company_name;

   FUNCTION checkif_telephonerequired
      RETURN checkif_dob_required_ref
   IS
      v_cursor   checkif_dob_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar ('TELEPHONE_REQUIRED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION checkif_emailrequired
      RETURN checkif_dob_required_ref
   IS
      v_cursor   checkif_dob_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar ('EMAIL_REQUIRED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION getagenciesbyacctype (v_sht_desc IN VARCHAR2)
      RETURN agencieslov_ref
   IS
      v_cursor   agencieslov_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   agn_code, agn_sht_desc, agn_name
             FROM tqc_agencies, tqc_account_types
            WHERE act_type_sht_desc = v_sht_desc AND act_code = agn_act_code
         ORDER BY agn_name;

      RETURN v_cursor;
   END;

   FUNCTION getagency_name (v_agn_code NUMBER, v_acc_type VARCHAR2)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      IF v_acc_type = 'CL'
      THEN
         SELECT clnt_name
           INTO v_ret_val
           FROM tqc_clients
          WHERE clnt_code = v_agn_code;

         RETURN v_ret_val;
      ELSIF v_acc_type = 'SP'
      THEN
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

   FUNCTION account_type_name (v_sht_desc VARCHAR2)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      IF v_sht_desc = 'SP'
      THEN
         v_ret_val := 'SERVICE PROVIDER';
         RETURN v_ret_val;
      ELSIF v_sht_desc = 'CL'
      THEN
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

   FUNCTION finddefaultsite
      RETURN returnparam_ref
   IS
      v_cursor   returnparam_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar ('DEFAULT_SITE')
           FROM DUAL;

      RETURN v_cursor;
   END;

    FUNCTION findbranches (v_reg_code NUMBER, v_org_code NUMBER DEFAULT NULL)
     RETURN returnbranches_ref
  IS
     v_cursor   returnbranches_ref;
  BEGIN
     ---raise_error('v_org_code: '||v_org_code);
    IF v_org_code IS NOT NULL
    THEN
       OPEN v_cursor FOR
       SELECT brn_code, brn_sht_desc, brn_name ,reg_sht_desc
        FROM tqc_branches,tqc_regions      
        WHERE brn_reg_code=reg_code
           AND reg_code IN (SELECT reg_code FROM tqc_regions WHERE reg_org_code = v_org_code);
    ELSE
       --Default to GIS Branches. When pushing the System, we will resolve the branch name to get the correct code.
       OPEN v_cursor FOR
       SELECT brn_code, brn_sht_desc, brn_name ,reg_sht_desc
        FROM tqc_branches,tqc_regions      
        WHERE brn_reg_code=reg_code
           AND reg_code IN (SELECT reg_code FROM tqc_regions WHERE reg_org_code = 2);
    END IF;
     RETURN v_cursor;
  END;

   FUNCTION branch_name (v_brn_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (50);
   BEGIN
      SELECT brn_name
        INTO v_ret_val
        FROM tqc_branches
       WHERE brn_code = v_brn_code;

      RETURN v_ret_val;
   END branch_name;

   FUNCTION bankbranch_name (v_bbr_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      BEGIN
         SELECT bnk_bank_name || ' - ' || bbr_branch_name
           INTO v_ret_val
           FROM tqc_bank_branches, tqc_banks
          WHERE bbr_code = v_bbr_code AND bbr_bnk_code = bnk_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_ret_val := NULL;
      END;

      RETURN v_ret_val;
   END bankbranch_name;

   FUNCTION getclaimpaymodes
      RETURN claimpaymodes_ref
   IS
      v_cursor   claimpaymodes_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT cpm_code, cpm_sht_desc, cpm_desc, cpm_remarks, cpm_min_amt,
                cpm_max_amt, cpm_default
           FROM tqc_clm_payment_modes;

      RETURN v_cursor;
   END;

   FUNCTION getmobilepaymenttypes (
      v_cou_code   tqc_mobile_pymnt_types.mpt_cou_code%TYPE
   )
      RETURN mobilepaymenttypes_ref
   IS
      v_cursor   mobilepaymenttypes_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT mpt_code, mpt_sht_desc, mpt_desc, mpt_min_amt_allowed,
                mpt_max_amt_allowed, mpt_cou_code
           FROM tqc_mobile_pymnt_types
          WHERE mpt_cou_code = v_cou_code;

      RETURN v_cursor;
   END;

   FUNCTION getmobtypesprefixes (
      v_mptp_code   tqc_mob_pymnt_types_prefixes.mptp_mpt_code%TYPE
   )
      RETURN mobiletypesprefix_ref
   IS
      v_cursor   mobiletypesprefix_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT mptp_code, mptp_mob_no_prefix, mptp_mpt_code,
                NULL sms_number
           FROM tqc_mob_pymnt_types_prefixes
          WHERE mptp_mpt_code = v_mptp_code;

      RETURN v_cursor;
   END;

   FUNCTION getmobprefixes (v_coucode NUMBER)
      RETURN mobiletypesprefix_ref
   IS
      v_cursor   mobiletypesprefix_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT mptp_mob_no_prefix mptp_code, mptp_mob_no_prefix,
                         mptp_mob_no_prefix mptp_mpt_code, NULL sms_number
                    FROM tqc_mob_pymnt_types_prefixes,
                         tqc_mobile_pymnt_types
                   WHERE mptp_mpt_code = mpt_code
                     -- AND MPTP_MPT_CODE=34534535
                     AND mpt_cou_code = NVL (v_coucode, mpt_cou_code)
                ORDER BY mptp_mob_no_prefix;

      RETURN v_cursor;
   END;

   FUNCTION getgroupusers (v_created_date VARCHAR2)
      RETURN user_ref
   IS
      v_cursor   user_ref;
   BEGIN
      --raise_error('v_grp_usr_code---->'|| v_created_date );
      IF v_created_date IS NULL
      THEN
         OPEN v_cursor FOR
            SELECT usr_code, usr_username, usr_name, usr_dt_created,
                   usr_created_by
              FROM tqc_users
             WHERE usr_type = 'G';
      ELSE
         OPEN v_cursor FOR
            SELECT usr_code, usr_username, usr_name, usr_dt_created,
                   usr_created_by
              FROM tqc_users
             WHERE usr_type = 'G'
               AND usr_dt_created = TO_DATE (v_created_date, 'dd/mm/yyyy');
      END IF;

      RETURN v_cursor;
   END;

   FUNCTION getgroupsmembers (
      v_grp_usr_code   tqc_group_users.gusr_grp_usr_code%TYPE
   )
      RETURN usergroup_ref
   IS
      v_cursor   usergroup_ref;
   BEGIN
      ---  raise_error('v_grp_usr_code---->'|| v_grp_usr_code);
      OPEN v_cursor FOR
         SELECT usr_code, usr_username, usr_name, gusr_code,
                gusr_grp_usr_code
           FROM tqc_users, tqc_group_users
          WHERE usr_code = gusr_usr_code
            AND gusr_grp_usr_code = v_grp_usr_code;

      RETURN v_cursor;
   END;

   FUNCTION findmemoclassapplicable
      RETURN returnparam_ref
   IS
      v_cursor   returnparam_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT param_value
           FROM gin_parameters
          WHERE param_name = 'MEMO_CLASS_LVL';

      RETURN v_cursor;
   END;

   FUNCTION getclientgroups
      RETURN clientgroup_ref
   IS
      v_cursor   clientgroup_ref;
   BEGIN
      ---  raise_error('v_grp_usr_code---->'|| v_grp_usr_code);
      OPEN v_cursor FOR
         SELECT grp_code, grp_name, grp_minimum, grp_maximum
           FROM tqc_group_client;

      RETURN v_cursor;
   END;

   FUNCTION getclientgroupmembers (
      v_grp_code   tqc_group_clnt_dtls.grpd_grp_code%TYPE
   )
      RETURN clientgroupdtls_ref
   IS
      v_cursor   clientgroupdtls_ref;
   BEGIN
      ---  raise_error('v_grp_usr_code---->'|| v_grp_usr_code);
      OPEN v_cursor FOR
         SELECT grp_name, grpd_code, grpd_clnt_code, grpd_grp_code,
                clnt_sht_desc, clnt_name, clnt_other_names
           FROM tqc_group_client, tqc_group_clnt_dtls, tqc_clients
          WHERE grp_code = grpd_grp_code
            AND grpd_clnt_code = clnt_code
            AND grp_code = v_grp_code;

      RETURN v_cursor;
   END;

   FUNCTION getclientcomplaints (
      v_clnt_code   tqc_client_complaints.comp_clnt_code%TYPE
   )
      RETURN clientcomplaints_ref
   IS
      v_cursor   clientcomplaints_ref;
   BEGIN
      ---  raise_error('v_grp_usr_code---->'|| v_grp_usr_code);
      OPEN v_cursor FOR
         SELECT comp_code, comp_clnt_code, comp_subject, comp_message,
                clnt_sht_desc, clnt_name, clnt_other_names
           FROM tqc_client_complaints, tqc_clients
          WHERE comp_clnt_code = clnt_code AND comp_clnt_code = v_clnt_code;

      RETURN v_cursor;
   END;

   FUNCTION physical_address_required
      RETURN returnparam_ref
   IS
      v_cursor   returnparam_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar
                                                 ('PHYSICAL_ADDRESS_REQUIRED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION postal_address_required
      RETURN returnparam_ref
   IS
      v_cursor   returnparam_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar
                                                   ('POSTAL_ADDRESS_REQUIRED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION get_print_servers
      RETURN print_server_ref
   IS
      v_cursor   print_server_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT serv_code, serv_name, serv_filter, serv_uri,
                serv_filter_command, serv_sec_username, serv_sec_password,
                serv_sec_auth_type, serv_sec_encrpt_type, serv_proxy_host,
                serv_proxy_port, serv_proxy_username, serv_proxy_pasword,
                serv_proxy_authen_type
           FROM tqc_print_servers;

      RETURN v_cursor;
   END;

   FUNCTION getbranchnames
      RETURN branch_names_details_ref
   IS
      v_cursor   branch_names_details_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT bns_code, bns_sht_desc, bns_name, bns_phy_addrs,
                bns_email_addrs, bns_post_addrs, bns_twn_code, bns_cou_code,
                bns_contact, bns_manager, bns_tel, bns_fax, bns_state_code,
                town_name (bns_twn_code) town,
                country_name (bns_cou_code) country,
                state_name (bns_state_code) state, bns_acc_type, bns_region,
                bns_post_code
           FROM tqc_branch_names;

      RETURN v_cursor;
   END;

   PROCEDURE get_branch_printers (
      v_cursor   OUT      branch_printer_ref,
      brn_code   IN       NUMBER,
      div_code   IN       NUMBER
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT brp_printer_name
           FROM tqc_branch_printers
          WHERE brp_brn_code = brn_code AND brp_div_code = div_code;
   END;

   PROCEDURE get_branch_print_servers (
      v_cursor   OUT      print_server_ref,
      brn_code   IN       NUMBER,
      div_code   IN       NUMBER
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT serv_code, serv_name, serv_filter, serv_uri,
                serv_filter_command, serv_sec_username, serv_sec_password,
                serv_sec_auth_type, serv_sec_encrpt_type, serv_proxy_host,
                serv_proxy_port, serv_proxy_username, serv_proxy_pasword,
                serv_proxy_authen_type
           FROM tqc_print_servers;
   --WHERE ;
   END;

   FUNCTION getmessagetemplates (v_sys_code NUMBER)
      RETURN msg_template_ref
   IS
      v_cursor   msg_template_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type
           FROM tqc_msg_templates
          WHERE msgt_sys_code = v_sys_code;

      RETURN (v_cursor);
   END;

   FUNCTION getparameter (v_param VARCHAR2)
      RETURN returnparam_ref
   IS
      v_cursor   returnparam_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar (v_param)
           FROM DUAL;

      RETURN v_cursor;
   END;

   PROCEDURE get_allbranches (v_refcur OUT branch_ref, v_org_code IN NUMBER)
   IS
   BEGIN
      -- Raise_error('org code '||v_org_code);
      OPEN v_refcur FOR
         SELECT -2000 brn_code, 'ALL BRANCHES' brn_name, 'ALL' brn_sht_desc,
                0 brn_reg_code
           FROM DUAL
         UNION
         SELECT DISTINCT brn_code, brn_name, brn_sht_desc, brn_reg_code
                    FROM tqc_branches, tqc_regions
                   WHERE reg_code = brn_reg_code
                         AND reg_org_code = v_org_code;
   END;

   PROCEDURE get_lead_sources (v_refcur OUT lead_sources_ref)
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT ldsrc_code, ldsrc_desc
           FROM tqc_leads_sources;
   END;

   PROCEDURE get_lead_statuses (v_refcur OUT lead_sources_ref)
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT lsts_code, lsts_desc
           FROM tqc_leads_statuses;
   END;

   PROCEDURE get_leads (v_refcur OUT leads_ref, v_lds_code NUMBER)
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT lds_code, lds_title, lds_surnname, lds_othernames,
                lds_camp_tel, lds_mob_tel, lds_camp_code,
                (SELECT cmp_name
                   FROM tqc_campaigns
                  WHERE cmp_code = lds_camp_code) lds_camp_name, lds_fax,
                lds_design, lds_email_addrs, lds_rate_type,
                lds_physical_addrs, lds_postal_addrs,
                (SELECT cou_name
                   FROM tqc_countries
                  WHERE cou_code = lds_cou_code) lds_country,
                lds_postal_code,
                (SELECT sts_name
                   FROM tqc_states
                  WHERE sts_code = lds_state_code) lds_state_name,
                lds_twn_code, (SELECT twn_name
                                 FROM tqc_towns
                                WHERE twn_code = lds_twn_code) lds_town_name,
                lds_cou_code, lds_state_code,
                TO_DATE (lds_date, 'DD/MM/yyyy') lds_date, lds_desc,
                lds_usr_code, (SELECT usr_name
                                 FROM tqc_users
                                WHERE usr_code = lds_usr_code) lds_usr_name,
                lds_org_code, (SELECT org_name
                                 FROM tqc_organizations
                                WHERE org_code = lds_org_code) lds_org_name,
                lds_sys_code, (SELECT sys_name
                                 FROM tqc_systems
                                WHERE sys_code = lds_sys_code) lds_sys_name,
                lds_converted, lds_pont_name, lds_pont_conrt,
                lds_pont_amount, lds_cur_code,
                (SELECT cur_desc || '(' || cur_symbol || ')'
                   FROM tqc_currencies
                  WHERE cur_code = lds_cur_code) lds_cur_name,
                lds_pont_close_date, lds_pont_sale_stage, lds_industry,
                lds_ann_revenue, lds_no_empyee, lds_web_site,
                lds_team_usr_code,
                (SELECT usr_name
                   FROM tqc_users
                  WHERE usr_code = lds_team_usr_code) lds_team_name,
                lds_acc_code,
                (SELECT agn_name
                   FROM tqc_agencies
                  WHERE agn_code IN (
                               SELECT acc_type_code
                                 FROM tqc_accounts
                                WHERE acc_code =
                                                lds_acc_code)
                 UNION
                 SELECT clnt_name
                   FROM tqc_clients
                  WHERE clnt_code IN (
                               SELECT acc_type_code
                                 FROM tqc_accounts
                                WHERE acc_code =
                                                lds_acc_code)
                 UNION
                 SELECT spr_name
                   FROM tqc_service_providers
                  WHERE spr_code IN (
                               SELECT acc_type_code
                                 FROM tqc_accounts
                                WHERE acc_code =
                                                lds_acc_code))
                                                            lds_account_name,
                lds_lsts_code,
                (SELECT lsts_desc
                   FROM tqc_leads_statuses
                  WHERE lsts_code = lds_lsts_code) lds_lsts_desc,
                lds_ldsrc_code,
                (SELECT ldsrc_desc
                   FROM tqc_leads_sources
                  WHERE ldsrc_code = lds_ldsrc_code) lds_ldsrc_desc,
                lds_prod_code,
                (SELECT tqc_campaign_cursor.get_product_desc
                                                 (lds_sys_code,
                                                  lds_prod_code
                                                 )
                   FROM DUAL) lds_prod_name,
                lds_div_code, (SELECT div_name
                                 FROM tqc_divisions
                                WHERE div_code = lds_div_code) lds_div_name
           FROM tqc_leads
          WHERE lds_code = NVL (v_lds_code, lds_code);
   END;

   PROCEDURE get_lead_comments (
      v_refcur     OUT   lead_comments_ref,
      v_lds_code         NUMBER
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT lcmnt_code, lcmnt_comment, lcmnt_date
           FROM tqc_lead_comments
          WHERE lcmnt_lds_code = v_lds_code;
   END;

   PROCEDURE get_lead_activities (
      v_refcur     OUT   lead_activities_ref,
      v_lds_code         NUMBER
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT lacts_code, act_code, act_subject, act_location
           FROM tqc_leads_activities, tqc_activities
          WHERE lacts_lds_code = v_lds_code AND lacts_act_code = act_code;
   END;

   FUNCTION get_all_report_groups
      RETURN rptgroup_ref
   IS
      v_prtgroup_rec   rptgroup_ref;
   BEGIN
      OPEN v_prtgroup_rec FOR
         SELECT trg_code, trg_name
           FROM tqc_report_groups;

      RETURN (v_prtgroup_rec);
   END get_all_report_groups;

   PROCEDURE get_rptgroupdiv (
      v_trg_code         IN       tqc_report_groups.trg_code%TYPE,
      v_rptgroupdivcur   OUT      rptgroupdiv_ref
   )
   IS
   BEGIN
      OPEN v_rptgroupdivcur FOR
         SELECT rdg_code, rdg_trg_code, rdg_div_code, div_name, div_order
           FROM tqc_divisions, tqc_report_groups, tqc_report_div_groups
          WHERE rdg_div_code = div_code(+)
            AND rdg_trg_code = trg_code
            AND rdg_trg_code = v_trg_code;
   END get_rptgroupdiv;

   PROCEDURE getundefinedrptgrpdivisions (
      v_trg_code       IN       tqc_report_groups.trg_code%TYPE,
      v_divisios_cur   OUT      tqc_web_cursor.divisions_ref
   )
   IS
   BEGIN
      OPEN v_divisios_cur FOR
         SELECT div_code, div_name, div_sht_desc, div_division_status,
                div_order, NULL div_director_code, NULL div_director,
                NULL div_ass_director_code, NULL div_asst_director
           FROM tqc_divisions
          WHERE div_code NOT IN (SELECT rdg_div_code
                                   FROM tqc_report_div_groups
                                  WHERE rdg_trg_code = v_trg_code);
   END getundefinedrptgrpdivisions;

   PROCEDURE getsystemapparea (
      v_saa_sys_code           tqc_sys_applicable_areas.saa_sys_code%TYPE,
      v_sysapparea_cur   OUT   sys_app_area_ref
   )
   IS
   BEGIN
      OPEN v_sysapparea_cur FOR
         SELECT saa_code, saa_sys_code, sys_name, saa_description
           FROM tqc_systems, tqc_sys_applicable_areas
          WHERE saa_sys_code = sys_code(+)
            AND saa_sys_code = NVL (v_saa_sys_code, saa_sys_code);
   END getsystemapparea;

   FUNCTION get_modules_subunits (v_prod_code IN NUMBER)
      RETURN mod_subunits_ref
   IS
      v_ref   mod_subunits_ref;
   BEGIN
      --RAISE_ERROR('GURUWE '||vscl_code);
      OPEN v_ref FOR
         SELECT tsms_code, tsms_tsm_code, tsms_sht_desc, tsms_desc,
                tsms_order, tsms_prod_code, pro_desc, tsm_desc
           FROM tqc_sys_mod_subunits, gin_products, tqc_system_modules
          WHERE tsms_tsm_code = tsm_code
            AND tsms_prod_code = pro_code(+)
            AND tsm_sys_code IN (37,27)
            AND tsms_prod_code = v_prod_code;

      RETURN (v_ref);
   END;

   FUNCTION get_mod_subunits_det (v_code IN NUMBER)
      RETURN mod_subunits_det_ref
   IS
      v_ref   mod_subunits_det_ref;
   BEGIN
      OPEN v_ref FOR
         SELECT tsmsd_code, tsmsd_name, tsmsd_prompt, tsmsd_type,
                tsmsd_order, tsmsd_parent, tsmsd_more_dtls_appl,
                tsmsd_more_dtls, tsmsd_root, tsmsd_more_dtls_required,
                tsmsd_tmsc_code, tmsc_desc
           FROM tqc_sys_mod_subunits_details, tqc_mod_sys_columns
          WHERE tsmsd_tsms_code = v_code AND tsmsd_tmsc_code = tmsc_code(+);

      RETURN (v_ref);
   END;

   FUNCTION get_proposal_mod_systems
      RETURN proposal_sched_mod_ref
   IS
      v_ref   proposal_sched_mod_ref;
   BEGIN
      OPEN v_ref FOR
         SELECT tsm_code, tsm_sht_desc, tsm_desc
           FROM tqc_system_modules
          WHERE tsm_sys_code = 27;

      RETURN (v_ref);
   END;

   FUNCTION get_subunits_options (v_code IN NUMBER)
      RETURN subunits_options_ref
   IS
      v_ref   subunits_options_ref;
   BEGIN
      OPEN v_ref FOR
         SELECT tsso_code, tsso_tsmsd_code, tsso_option_name,
                tsso_option_desc, tsso_order, tsso_type
           FROM tqc_sys_subunits_options
          WHERE tsso_tsmsd_code = v_code;

      RETURN (v_ref);
   END;

   FUNCTION getsysmappingcolmns (v_syscode IN NUMBER)
      RETURN mapping_columns_ref
   IS
      v_cursor   mapping_columns_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tmsc_code, tmsc_desc
           FROM tqc_mod_sys_columns
          WHERE tmsc_sys_code = v_syscode;

      RETURN v_cursor;
   END;

   FUNCTION getincomingmailsettings
      RETURN mail_settings_ref
   IS
      v_cursor   mail_settings_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT mail_type, mail_server_name, mail_host, mail_username,
                mail_password, mail_port, mail_email, mail_in_out,
                mail_secure
           FROM tqc_system_mails
          WHERE mail_in_out = 'I';

      RETURN v_cursor;
   END;

   FUNCTION getoutgoingmailsettings
      RETURN mail_settings_ref
   IS
      v_cursor   mail_settings_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT mail_type, mail_server_name, mail_host, mail_username,
                mail_password, mail_port, mail_email, mail_in_out,
                mail_secure
           FROM tqc_system_mails
          WHERE mail_in_out = 'O';

      RETURN v_cursor;
   END;

   FUNCTION get_alerts (
      v_jobname   VARCHAR2 DEFAULT NULL,
      v_syscode   NUMBER DEFAULT NULL
   )
      RETURN alert_cursor
   IS
      v_cursor   alert_cursor;
   BEGIN
      OPEN v_cursor FOR
         SELECT qt_job_name, qt_description, qt_next_fire_time,
                qt_prev_fire_time, qt_start_time, qt_end_time, qt_code,
                qt_sys_code, qt_recurrence, qt_recurrence_type,
                qt_job_assignee, qt_notified_fail_user,
                qt_notified_succ_user, qt_reccurence_interval, qt_job_type,
                qt_job_template, qt_fail_notify_template,
                qt_succ_notify_template, a.usr_username assignee,
                b.usr_username fail_user, c.usr_username succ_user,
                CASE
                   WHEN qt_job_type = 'A'
                      THEN job_temp.msgt_msg
                   ELSE CASE
                   WHEN qt_job_type = 'R'
                      THEN rpt_description
                   ELSE CASE
                   WHEN qt_job_type = 'W'
                      THEN sprc_process
                   ELSE CASE
                   WHEN qt_job_type = 'J'
                      THEN tsr_sht_desc
                   ELSE NULL
                END
                END
                END
                END AS obj_execution,
                fail_temp.msgt_msg, succ_temp.msgt_msg, a.usr_email,
                b.usr_email, c.usr_email, qt_status, qt_threshold_type,
                qt_threshold_value, a.usr_type, qt_cron_expression
           FROM qrtz_triggers,
                tqc_users a,
                tqc_users b,
                tqc_users c,
                tqc_msg_templates fail_temp,
                tqc_msg_templates succ_temp,
                tqc_msg_templates job_temp,
                tqc_system_reports,
                jbpm4_deployprop,
                tqc_sys_processes,
                tqc_system_routines
          WHERE qt_job_assignee = a.usr_code(+)
            AND qt_notified_fail_user = b.usr_code(+)
            AND qt_job_name = NVL (v_jobname, qt_job_name)
            AND qt_sys_code = NVL (v_syscode, qt_sys_code)
            AND qt_notified_succ_user = c.usr_code(+)
            AND qt_job_template = rpt_code(+)
            AND qt_job_template = dbid_(+)
            AND qt_job_template = tsr_code(+)
            AND sprc_jpdl_desc(+) = objname_
            AND job_temp.msgt_code(+) = qt_job_template
            AND fail_temp.msgt_code(+) = qt_fail_notify_template
            AND succ_temp.msgt_code(+) = qt_succ_notify_template;

      RETURN (v_cursor);
   END;

   FUNCTION getexecutionobjects (
      v_syscode   NUMBER,
      v_type      VARCHAR2,
      v_objcode   NUMBER DEFAULT NULL
   )
      RETURN exec_object_ref
   IS
      v_cursor   exec_object_ref;
   BEGIN
      --RAISE_ERROR('v_type'||v_type||'v_syscode'||v_syscode||'v_objcode'||v_objcode);
      IF v_type = 'RPT'
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT 'RPT', rpt_code, rpt_description,
                            sys_reports_path
                       FROM tqc_system_reports, tqc_systems
                      WHERE rpt_sys_code = sys_code
                        AND rpt_description IS NOT NULL
                        AND sys_code = NVL (v_syscode, sys_code)
                        AND rpt_code = NVL (v_objcode, rpt_code)
                   ORDER BY rpt_description;

         RETURN v_cursor;
      ELSIF v_type = 'W'
      THEN
         OPEN v_cursor FOR
            SELECT   'W', MAX (jbpm4_deployprop.dbid_), sprc_process,
                     objname_
                FROM jbpm4_deployprop, tqc_sys_processes, jbpm4_variable
               WHERE sprc_jpdl_desc = objname_
                 AND jbpm4_variable.dbid_(+) = jbpm4_deployprop.dbid_
                 AND sprc_sys_code = NVL (v_syscode, sprc_sys_code)
                 AND jbpm4_deployprop.dbid_ =
                                       NVL (v_objcode, jbpm4_deployprop.dbid_)
            GROUP BY objname_, sprc_sys_code, sprc_process
            ORDER BY sprc_process;

         RETURN v_cursor;
      ELSIF v_type = 'J'
      THEN
         OPEN v_cursor FOR
            SELECT   'J', tsr_code, tsr_sht_desc, tsr_function
                FROM tqc_system_routines
               WHERE tsr_sys_code = NVL (v_syscode, tsr_sys_code)
                 AND tsr_code = NVL (v_objcode, tsr_code)
            ORDER BY tsr_sht_desc;

         RETURN v_cursor;
      END IF;

      RETURN (v_cursor);
   END;

   FUNCTION gethouseholds
      RETURN households_ref
   IS
      v_cursor   households_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT hh_id, hh_name, usr_username, hh_date_created, hh_category
           FROM tqc_households, tqc_users
          WHERE hh_created_by = usr_code;

      RETURN v_cursor;
   END;

   FUNCTION gethouseholdmembers (v_hhid tqc_households.hh_id%TYPE)
      RETURN householddtls_ref
   IS
      v_cursor   householddtls_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT hh_name, hh_id, hhd_clnt_code, clnt_sht_desc, clnt_name,
                clnt_other_names
           FROM tqc_households, tqc_household_dtls, tqc_clients
          WHERE hhd_hh_id = hh_id
            AND hhd_clnt_code = clnt_code
            AND hhd_id = v_hhid;

      RETURN v_cursor;
   END;

   FUNCTION getdirectdebitsreport (v_ddcode NUMBER, v_query NUMBER)
      RETURN direct_debits_ref
   IS
      v_cursor   direct_debits_ref;
   BEGIN
      --raise_error('v_ddCode='||v_ddCode);
      OPEN v_cursor FOR
         /*SELECT ddd_code, NVL (ddd_pol_policy_no, ddd_pol_proposal_no)
                                                                      POLICY,
                TO_CHAR (ddh_clnt_bank_acc_no) bank_acc_no,
                NVL (bnk_kba_code || '' || bbr_kba_code,
                     ddh_bbr_ref_code
                    ) forwarding_bnk,
                TO_CHAR (ddd_amount) amount, ddh_clnt_name clnt_name,
                TO_CHAR (SYSDATE, 'MONTH RRRR ') || 'PREM' narrattive,
                org_name, ddh_bbr_ref_code, ddd_prem_due_date,
                DECODE (ddd_pol_freq_pymnt,
                        'M', 'MONTHLY',
                        'Q', 'QUARTERLY',
                        'S', 'SEMI-ANNUALLY',
                        'A', 'ANNUALLY',
                        'F', 'SINGLE PREMIUM'
                       ) ddd_pol_freq_pymnt,
                dd_ref_no,
                (SELECT bnk_bank_name || ' - ' || bbr_branch_name
                   FROM tqc_bank_branches, tqc_banks
                  WHERE bbr_bnk_code = bnk_code
                    AND bbr_code = dd_bbr_code) bank_branch,
                tqc_setups_cursor.get_debit_day
                                             (ddd_pol_type,
                                              DECODE (ddd_pol_type,
                                                      'PL', ddd_pol_code,
                                                      'MKT', ddd_ppr_code
                                                     )
                                             ) debit_day
           FROM tqc_direct_debit,
                tqc_direct_debit_header,
                tqc_direct_debit_detail,
                tqc_organizations,
                tqc_systems,
                tqc_bank_branches,
                tqc_banks        --,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
          WHERE ddh_dd_code = dd_code
            AND ddh_code = ddd_ddh_code
            AND ddd_sys_code = sys_code
            AND sys_org_code = org_code
            AND bnk_code = ddh_bbr_bnk_code
            AND bbr_code = ddh_clnt_bbr_code
            AND bbr_bnk_code = bnk_code
            AND dd_code = v_ddcode;*/
         SELECT ddd_code, NVL (ddd_pol_policy_no, ddd_pol_proposal_no)
                                                                      POLICY,
                --'[' || TO_CHAR (ddh_clnt_bank_acc_no) || ']' bank_acc_no,
                ''''|| ddh_clnt_bank_acc_no bank_acc_no,
                NVL (bnk_kba_code || '' || bbr_kba_code,
                     ddh_bbr_ref_code
                    ) forwarding_bnk,
                TO_CHAR (ddd_amount) amount, ddh_clnt_name clnt_name,
                   TO_CHAR (SYSDATE, 'MONTH RRRR ')
                || 'PREM-'
                || DECODE (endr_no,
                           '0001', DECODE (pol_status,
                                           'D', 'NEW CASE',
                                           'V', 'NEW CASE',
                                           'L', 'LAPSED - '||DECODE(NVL(DDD_REC_NO, 1), 1, 'RECURRING', 'ARREARS'),
                                           'A', DECODE(NVL(DDD_REC_NO, 1), 1, 'RECURRING', 'ARREARS')
                                          ),
                           'UPDATES'
                          ) narrattive,
                org_name, ddh_bbr_ref_code, ddd_prem_due_date,
                DECODE (ddd_pol_freq_pymnt,
                        'M', 'MONTHLY',
                        'Q', 'QUARTERLY',
                        'S', 'SEMI-ANNUALLY',
                        'A', 'ANNUALLY',
                        'F', 'SINGLE PREMIUM'
                       ) ddd_pol_freq_pymnt,
                dd_ref_no,
                (SELECT bnk_bank_name || ' - ' || bbr_branch_name
                   FROM tqc_bank_branches, tqc_banks
                  WHERE bbr_bnk_code = bnk_code
                    AND bbr_code = ddh_clnt_bbr_code) bank_branch,
                tqc_setups_cursor.get_debit_day
                                             (ddd_pol_type,
                                              DECODE (ddd_pol_type,
                                                      'PL', ddd_pol_code,
                                                      'MKT', ddd_ppr_code
                                                     )
                                             ) debit_day,TO_CHAR (SYSDATE, 'MONTH RRRR ')
           FROM tqc_direct_debit,
                tqc_direct_debit_header,
                tqc_direct_debit_detail,
                tqc_organizations,
                tqc_systems,
                tqc_bank_branches,
                tqc_banks,
                lms_policies,
                lms_policy_endorsements
          --,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
         WHERE  ddh_dd_code = dd_code
            AND ddh_code = ddd_ddh_code
            AND ddd_sys_code = sys_code
            AND sys_org_code = org_code
            AND bnk_code = ddh_bbr_bnk_code
            AND bbr_code = ddh_clnt_bbr_code
            AND ddd_type !='LN'
            AND bbr_bnk_code = bnk_code
            AND ddd_pol_code = pol_code
            AND endr_pol_code = pol_code
            AND endr_code = pol_current_endr_code
            AND dd_code = v_ddcode
          UNION 
                SELECT ddd_code, NVL (ddd_pol_policy_no, ddd_pol_proposal_no)
                                                                  POLICY,
            '''' || TO_CHAR (ddh_clnt_bank_acc_no)  bank_acc_no,
            NVL (bnk_kba_code || '' || bbr_kba_code,
                 ddh_bbr_ref_code
                ) forwarding_bnk,
            TO_CHAR (ddd_amount) amount, ddh_clnt_name clnt_name,
               TO_CHAR (SYSDATE, 'MONTH RRRR ')
            || 'PREM-'
            || DECODE (endr_no,
                       '0001', DECODE (pol_status,
                                       'D', 'NEW CASE',
                                       'V', 'NEW CASE',
                                       'A', 'RECURRING'
                                      ),
                       'UPDATES'
                      ) narrattive,
            org_name, ddh_bbr_ref_code, ddd_prem_due_date,
            DECODE (ddd_pol_freq_pymnt,
                    'M', 'MONTHLY',
                    'Q', 'QUARTERLY',
                    'S', 'SEMI-ANNUALLY',
                    'A', 'ANNUALLY',
                    'F', 'SINGLE PREMIUM'
                   ) ddd_pol_freq_pymnt,
            dd_ref_no,
            (SELECT bnk_bank_name || ' - ' || bbr_branch_name
               FROM tqc_bank_branches, tqc_banks
              WHERE bbr_bnk_code = bnk_code
                AND bbr_code = ddh_clnt_bbr_code) bank_branch,
            tqc_setups_cursor.get_debit_day
                                         (ddd_pol_type,
                                          DECODE (ddd_pol_type,
                                                  'PL', ddd_pol_code,
                                                  'MKT', ddd_ppr_code
                                                 )
                                         ) debit_day,TO_CHAR (SYSDATE, 'MONTH RRRR ')
       FROM tqc_direct_debit,
            tqc_direct_debit_header,
            tqc_direct_debit_detail,
            tqc_organizations,
            tqc_systems,
            tqc_bank_branches,
            tqc_banks,
            lms_loan_applications,
            lms_policies,
            lms_policy_endorsements
      --,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
     WHERE  ddh_dd_code = dd_code
        AND ddh_code = ddd_ddh_code
        AND ddd_sys_code = sys_code
        AND sys_org_code = org_code
        AND bnk_code = ddh_bbr_bnk_code
        AND bbr_code = ddh_clnt_bbr_code
        AND bbr_bnk_code = bnk_code
        AND ddd_type ='LN'
        AND ddd_pol_code = lna_no
        AND lna_pol_code = pol_code
        AND endr_code = pol_current_endr_code
        AND dd_code = v_ddcode;
      RETURN v_cursor;
   END;

   FUNCTION getdirectdebitsdata (
      v_ddcode     NUMBER,
      v_refno      VARCHAR2,
      v_bankcode   VARCHAR2,
      v_bbrcode    VARCHAR2,
      v_query      NUMBER
   )
      RETURN debit_data_ref
   IS
      v_cursor      debit_data_ref;
      v_report_no   VARCHAR2 (10);
      v_client      VARCHAR2 (10);
      v_total       NUMBER;
      v_total_amt   NUMBER;
      v_dd_status   VARCHAR2 (1);
      v_cnt         NUMBER;

      CURSOR cursor_13
      IS
         SELECT ddd_amount, ddh_bbr_sht_desc, ddh_clnt_bank_acc_no,
                ddd_pol_policy_no, ddh_acc_holder, dd_book_date,
                dd_gen_date_value
           FROM tqc_direct_debit,
                tqc_direct_debit_header,
                tqc_direct_debit_detail,
                tqc_organizations,
                tqc_systems
          WHERE ddh_dd_code = dd_code
            AND ddh_code = ddd_ddh_code
            AND ddd_sys_code = sys_code
            AND sys_org_code = org_code
            AND dd_code = v_ddcode;
   BEGIN
      BEGIN
         SELECT param_value
           INTO v_client
           FROM tqc_parameters
          WHERE param_name = 'CLIENT';
      EXCEPTION
         WHEN OTHERS
         THEN
            NULL;
      END;

      IF NVL (v_client, 'TURNKEY') = 'HERITAGE'
      THEN
         BEGIN
            SELECT dd_file_no, dd_status
              INTO v_report_no, v_dd_status
              FROM tqc_direct_debit
             WHERE dd_code = v_ddcode AND dd_file_generated = 'Y';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               BEGIN
                  SELECT NVL (MAX (dd_file_no), 0) + 1
                    INTO v_report_no
                    FROM tqc_direct_debit
                   WHERE dd_ref_no = v_refno
                     AND dd_bnk_code = v_bankcode
                     -- AND dd_bbr_code = v_bbrcode
                     AND dd_file_generated = 'Y';

                  v_report_no := '0' || v_report_no;
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     v_report_no := '0' || 1;
               END;
         END;

         BEGIN
            SELECT dd_file_no, dd_status
              INTO v_report_no, v_dd_status
              FROM tqc_direct_debit
             WHERE dd_code = v_ddcode;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error getting dd status');
         END;

         IF NVL (v_dd_status, 'D') = 'D'
         THEN
            raise_error ('Cannot generate file for unauthorized transaction ');
         END IF;

         IF v_report_no IS NULL
         THEN
            v_report_no := '0' || 1;
         END IF;

         DELETE FROM tqc_direct_debit_rec
               WHERE dd_code = v_ddcode;

         FOR a IN cursor_13
         LOOP
            v_cnt := 0;

            SELECT COUNT (1)
              INTO v_cnt
              FROM tqc_direct_debit_rec
             WHERE ddh_clnt_bank_acc_no = a.ddh_clnt_bank_acc_no
               AND dd_code = v_ddcode;

            IF v_cnt = 1
            THEN
               UPDATE tqc_direct_debit_rec
                  SET dd_policies = dd_policies || '/' || a.ddd_pol_policy_no,
                      ddd_amount = ddd_amount + a.ddd_amount
                WHERE ddh_clnt_bank_acc_no = a.ddh_clnt_bank_acc_no
                  AND dd_code = v_ddcode;
            ELSE
               INSERT INTO tqc_direct_debit_rec
                           (ddd_amount, ddh_bbr_sht_desc,
                            ddh_clnt_bank_acc_no, dd_policies,
                            ddh_acc_holder, dd_code, dd_book_date,
                            dd_gen_date_value
                           )
                    VALUES (a.ddd_amount, a.ddh_bbr_sht_desc,
                            a.ddh_clnt_bank_acc_no, a.ddd_pol_policy_no,
                            a.ddh_acc_holder, v_ddcode, a.dd_book_date,
                            a.dd_gen_date_value
                           );
            END IF;
         END LOOP;

         --raise_error('v_bankcode '||v_bankcode||' v_ddcode '||v_ddcode);
         IF v_bankcode = 13
         THEN
            OPEN v_cursor FOR
               SELECT    'AMNT,'
                      || 'DEBIT BANK CODE,'
                      || 'DEBIT ACCT,'
                      || 'HERITAGE BANK CODE,'
                      || 'HERITAGE CR. A/C,'
                      || 'POLICY NO.,'
                      || 'POLICY HOLDERS NAME',
                      v_report_no
                 FROM DUAL
               UNION ALL
               SELECT    TO_CHAR (ddd_amount)
                      || ','
                      || ddh_bbr_sht_desc
                      || ','
                      || ddh_clnt_bank_acc_no
                      || ','
                      || '11002'
                      || ','
                      || '1136190113300'
                      || ','
                      || dd_policies
                      || ','
                      || ddh_acc_holder,
                      v_report_no
                 FROM tqc_direct_debit_rec
                WHERE dd_code = v_ddcode;
         ELSIF v_bankcode = 6
         THEN
            OPEN v_cursor FOR
               SELECT    'AMNT,'
                      || 'DEBIT BANK CODE,'
                      || 'DEBIT ACCT,'
                      || 'POLICY NO.'
                      || 'POLICY HOLDERS NAME',
                      v_report_no
                 FROM DUAL
               UNION ALL
               SELECT    TO_CHAR (ddd_amount)
                      || ','
                      || ddh_bbr_sht_desc
                      || ','
                      || ddh_clnt_bank_acc_no
                      || ','
                      || dd_policies
                      || ','
                      || ddh_acc_holder,
                      v_report_no
                 FROM tqc_direct_debit_rec
                WHERE dd_code = v_ddcode;
         ELSIF v_bankcode = 1
         THEN
            OPEN v_cursor FOR
               SELECT    TO_CHAR (ddd_amount)
                      || ','
                      || ddh_bbr_sht_desc
                      || ','
                      || ddh_clnt_bank_acc_no
                      || ','
                      || dd_policies
                      || ','
                      || ddh_acc_holder,
                      v_report_no
                 FROM tqc_direct_debit_rec
                WHERE dd_code = v_ddcode;
         ELSIF v_bankcode = 36
         THEN
            SELECT COUNT (1), SUM (ddd_amount)
              INTO v_total, v_total_amt
              FROM tqc_direct_debit_rec
             WHERE dd_code = v_ddcode;

            OPEN v_cursor FOR
               SELECT 'H' || ',' || '02' || ',' || '0104012022900', v_report_no
                 FROM DUAL
               UNION ALL
               SELECT    'D'
                      || ','
                      || SUBSTR (ddh_bbr_sht_desc, 0, 2)
                      || ','
                      || SUBSTR (ddh_bbr_sht_desc, 3, 5)
                      || ','
                      || ddh_clnt_bank_acc_no
                      || ','
                      || ddh_acc_holder
                      || ','
                      || dd_policies
                      || ','
                      || TO_CHAR (ddd_amount)
                      || ','
                      || TO_CHAR (SYSDATE, 'RRRR')
                      || TO_CHAR (dd_book_date, 'MM')
                      || DECODE (TO_CHAR (dd_gen_date_value),
                                 '5', '05',
                                 TO_CHAR (dd_gen_date_value)
                                )
                      || ','
                      || dd_policies,
                      v_report_no
                 FROM tqc_direct_debit_rec
                WHERE dd_code = v_ddcode
               UNION ALL
               SELECT    'T'
                      || ','
                      || TO_CHAR (v_total)
                      || ','
                      || '0'
                      || ','
                      || TO_CHAR (v_total_amt)
                      || ','
                      || '0',
                      v_report_no
                 FROM DUAL;
         ELSIF v_bankcode = 40
         THEN
            OPEN v_cursor FOR
               SELECT    '00'
                      || '40'
                      || pad_amount (ddd_amount)
                      || '0'
                      || SUBSTR (ddh_bbr_sht_desc, 0, 2)
                      || SUBSTR (ddh_bbr_sht_desc, 3, 5)
                      || LPAD (ddh_clnt_bank_acc_no, 15, '0')
                      || '68'
                      || '018'
                      || '000180296408232'
                      || '68'
                      || '018'
                      || '0116'
                      || pad_policies (dd_policies)
                      || '00000000000',
                      v_report_no
                 FROM tqc_direct_debit_rec
                WHERE dd_code = v_ddcode;
         END IF;

            -- AND v_query = 1
         /* UNION ALL
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
                        AND v_query = 3);*/
         RETURN v_cursor;
      ELSE
         BEGIN
         SELECT dd_file_no
           INTO v_report_no
           FROM tqc_direct_debit
          WHERE dd_code = v_ddcode AND dd_file_generated = 'Y';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            BEGIN
               SELECT NVL (MAX (dd_file_no), 0) + 1
                 INTO v_report_no
                 FROM tqc_direct_debit
                WHERE dd_ref_no = v_refno
                  AND dd_bnk_code = v_bankcode
                  AND dd_bbr_code = v_bbrcode
                  AND dd_file_generated = 'Y';

               v_report_no := '0' || v_report_no;
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  v_report_no := '0' || 1;
            END;
      END;

      IF v_report_no IS NULL
      THEN
         v_report_no := '0' || 1;
      END IF;

      OPEN v_cursor FOR
         SELECT    '0111'
                || SUBSTR (ddh_clnt_code, 1, 15)
                || '#'
                || ddd_amount
                || '#'
                || TO_CHAR (dd_book_date, 'DD/MM/RRRR')
                || '#'
                || SUBSTR (dd_acnt_no, 1, 10)
                || '#'
                || 'KES'
                || '#'
                || SUBSTR (ddh_clnt_code, 1, 35)
                || '#'
                || org_name
                || '#'
                || org_name
                || '#'
                || SUBSTR (ddh_clnt_bank_acc_no, 1, 13)
                || '#'
                || SUBSTR (ddh_clnt_name, 1, 35)
                || '#'
                || 'MADISON'
                || '#'
                || org_name
                || '#'
                || SUBSTR (ddh_bbr_ref_code, 1, 5)
                || '#'
                || org_name
                || '#'
                || SUBSTR (ddh_clnt_bank_acc_no, 1, 13) dd_rec,
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
         SELECT    '00'
                || '40'
                || LPAD (SUBSTR (ddd_amount, 1, 13), 13, 0)
                || '0'
                || SUBSTR (bnk_kba_code, 1, 2)
                || ''
                || LPAD (SUBSTR (bbr_kba_code, 1, 3), 3, 0)
                || ''
                || LPAD (SUBSTR (ddh_clnt_bank_acc_no, 1, 15), 15, 0)
                || ''
                || SUBSTR (direct_debit_pkg.forwarding_bnk_code (dd_bnk_code),
                           1,
                           2
                          )
                || ''
                || LPAD
                      (SUBSTR
                          (direct_debit_pkg.forwarding_bnk_bbr_code
                                                                  (dd_bbr_code),
                           1,
                           3
                          ),
                       3,
                       0
                      )
                || ''
                || LPAD (SUBSTR (dd_acnt_no, 1, 15), 15, 0)
                || ''
                || SUBSTR (direct_debit_pkg.forwarding_bnk_code (dd_bnk_code),
                           1,
                           2
                          )
                || ''
                || LPAD
                      (SUBSTR
                          (direct_debit_pkg.forwarding_bnk_bbr_code
                                                                  (dd_bbr_code),
                           1,
                           3
                          ),
                       3,
                       0
                      )
                || ''
                || SUBSTR (sys_kba_code, 1, 4)
                || ''
                || RPAD (SUBSTR (dd_ref_no, 1, 15), 15)
                || ''
                || RPAD (SUBSTR (NVL (ddd_pol_policy_no, ddd_pol_proposal_no),
                                 1,
                                 20
                                ),
                         20
                        )
                || '00000000000' dd_rec,
                v_report_no
           FROM tqc_direct_debit,
                tqc_direct_debit_header,
                tqc_direct_debit_detail,
                tqc_organizations,
                tqc_systems,
                tqc_bank_branches,
                tqc_banks        --,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
          WHERE ddh_dd_code = dd_code
            AND ddh_code = ddd_ddh_code
            AND ddd_sys_code = sys_code
            AND sys_org_code = org_code
            AND bnk_code = ddh_bbr_bnk_code
            AND bbr_code = ddh_clnt_bbr_code
            AND bbr_bnk_code = bnk_code
            AND dd_code = v_ddcode
            AND v_query = 2
         UNION ALL
         (SELECT DISTINCT LPAD
                             ((   'NGR-'
                               || TO_CHAR (SYSDATE, 'DDMMRRRR')
                               || '-'
                               || v_report_no
                              ),
                              (LENGTH
                                  (   NVL (ddd_pol_policy_no,
                                           ddd_pol_proposal_no
                                          )
                                   || ','
                                   || ddh_clnt_bank_acc_no
                                   || ','
                                   || direct_debit_pkg.forwarding_bnk_code
                                                                  (dd_bnk_code)
                                   || ''
                                   || direct_debit_pkg.forwarding_bnk_bbr_code
                                                                  (dd_bbr_code)
                                   || ','
                                   || ddd_amount
                                   || ','
                                   || ddh_clnt_name
                                   || ','
                                   || TO_CHAR (SYSDATE, 'MONTH RRRR ')
                                   || 'PREM,'
                                   || org_name
                                  )
                              )
                             ) dd_details,
                          v_report_no
                     FROM tqc_direct_debit,
                          tqc_direct_debit_header,
                          tqc_direct_debit_detail,
                          tqc_organizations,
                          tqc_systems,
                          tqc_bank_branches,
                          tqc_banks
                    --,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
          WHERE           ddh_dd_code = dd_code
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
          SELECT    NVL (ddd_pol_policy_no, ddd_pol_proposal_no)
                 || ','
                 || ddh_clnt_bank_acc_no
                 || ','
                 || bnk_kba_code
                 || ''
                 || bbr_kba_code
                 || ','
                 || ddd_amount
                 || ','
                 || ddh_clnt_name
                 || ','
                 || TO_CHAR (SYSDATE, 'MONTH RRRR ')
                 || 'PREM,'
                 || org_name dd_details,
                 v_report_no
            FROM tqc_direct_debit,
                 tqc_direct_debit_header,
                 tqc_direct_debit_detail,
                 tqc_organizations,
                 tqc_systems,
                 tqc_bank_branches,
                 tqc_banks       --,TQC_CLIENTS--TQC_BANK_BRANCHES--,TQC_BANKS
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
      END IF;
   END;

   FUNCTION gettownswithzipcodebystate (v_state_code IN NUMBER)
      RETURN towns_with_zip_ref
   IS
      v_cursor   towns_with_zip_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   twn_code, twn_cou_code, twn_sht_desc, twn_name,
                  twn_sts_code, pst_desc, pst_zip_code
             FROM tqc_towns, tqc_postal_codes
            WHERE twn_code = pst_twn_code(+) AND twn_sts_code = v_state_code
         ORDER BY twn_name;

      RETURN v_cursor;
   END;

   FUNCTION get_ecm_setups
      RETURN ecm_setups_ref
   IS
      v_cursor   ecm_setups_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT est_code, est_ecm_url, est_ecm_username, est_ecm_password,
                est_sys_code, est_root_folder, sys_name
           FROM tqc_ecm_setups, tqc_systems
          WHERE est_sys_code = sys_code;

      RETURN v_cursor;
   END;

   FUNCTION get_system_ecm_setups (v_sys_code NUMBER)
      RETURN ecm_setups_ref
   IS
      v_cursor   ecm_setups_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT est_code, est_ecm_url, est_ecm_username, est_ecm_password,
                est_sys_code, est_root_folder, sys_name
           FROM tqc_ecm_setups, tqc_systems
          WHERE est_sys_code = sys_code AND est_sys_code = v_sys_code;

      RETURN v_cursor;
   END;

   FUNCTION get_ecm_systems
      RETURN ecm_systems_ref
   IS
      v_cursor   ecm_systems_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT sys_code, sys_sht_desc, sys_name
           FROM tqc_systems;

      RETURN (v_cursor);
   END;

   FUNCTION get_ecm_processes (v_sys_code NUMBER)
      RETURN ecm_processes_ref
   IS
      v_cursor   ecm_processes_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT sprc_code, sprc_process
           FROM tqc_sys_processes
          WHERE sprc_sys_code = v_sys_code;

      RETURN (v_cursor);
   END;

   FUNCTION get_system_reports (v_sys_code NUMBER)
      RETURN system_reports_ref
   IS
      v_cursor   system_reports_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_description
           FROM tqc_system_reports
          WHERE rpt_sys_code = v_sys_code;

      RETURN (v_cursor);
   END;

   FUNCTION get_process_reports (v_proc_code NUMBER)
      RETURN sys_reports_ref
   IS
      v_cursor   sys_reports_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT sprr_code, sprr_rpt_code, sprr_sprc_code, sprr_desc,
                rpt_name, rpt_description, sprr_type, sdt_content_name,
                sdt_code, sdt_content_type
           FROM tqc_sys_process_reports,
                tqc_system_reports,
                tqc_sys_doc_types
          WHERE sprr_rpt_code = rpt_code
            AND sprr_sdt_code = sdt_code(+)
            AND sprr_sprc_code = v_proc_code;

      RETURN (v_cursor);
   END;

   FUNCTION get_process_report (v_proc_area VARCHAR2, v_sys_code NUMBER)
      RETURN sys_reports_ref
   IS
      v_cursor      sys_reports_ref;
      v_proc_code   NUMBER;
   BEGIN
      SELECT sprc_code
        INTO v_proc_code
        FROM tqc_sys_processes
       WHERE sprc_sht_desc = v_proc_area AND sprc_sys_code = v_sys_code;

      OPEN v_cursor FOR
         SELECT sprr_code, sprr_rpt_code, sprr_sprc_code, sprr_desc, rpt_name,
                rpt_description, sprr_type, sdt_content_name, sdt_code,
                sdt_content_type
           FROM tqc_sys_process_reports, tqc_system_reports,
                tqc_sys_doc_types
          WHERE sprr_rpt_code = rpt_code
            AND sprr_sdt_code = sdt_code(+)
            AND sprr_sprc_code = v_proc_code;

      RETURN (v_cursor);
   END;

   FUNCTION get_ecm_doc_types
      RETURN ecm_doc_types_ref
   IS
      v_cursor   ecm_doc_types_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT sdt_code, sdt_content_name, sdt_content_desc,
                sdt_content_type
           FROM tqc_sys_doc_types;

      RETURN (v_cursor);
   END;

   FUNCTION get_content_metadata (v_sprr_code NUMBER)
      RETURN content_metadata_ref
   IS
      v_cursor   content_metadata_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT ddm_code, ddm_sdt_code, ddm_name, ddm_desc
           FROM tqc_dms_doc_metadata
          WHERE ddm_sdt_code = v_sprr_code;

      RETURN (v_cursor);
   END;

   FUNCTION get_branches_details (v_bbrcode NUMBER)
      RETURN branch_data_ref
   IS
      v_cursor   branch_data_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT bbb_code, bbb_brn_code, bbb_brn_sht_desc, bbb_brn_reg_code,
                bbb_brn_name, bbb_brn_phy_addrs, bbb_bbr_code,
                bbb_bbr_bnk_code, bbb_bbr_branch_name, bbb_bbr_sht_desc,
                bbb_bbr_physical_addrs
           FROM tqc_bank_branches_branches
          WHERE bbb_bbr_code = v_bbrcode;

      RETURN (v_cursor);
   END;

   PROCEDURE all_branches (v_all_branches_ref OUT all_branches_ref)
   IS
   BEGIN
      OPEN v_all_branches_ref FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name,
                brn_phy_addrs, brn_email_addrs, brn_post_addrs, brn_twn_code,
                brn_cou_code, brn_contact, brn_manager, brn_tel, brn_fax,
                brn_gen_pol_clm, brn_bns_code, brn_agn_code, brn_post_level,
                agn_name (brn_agn_code) manager
           FROM tqc_branches;
   END all_branches;

   FUNCTION all_bank__branches (v_branch_name VARCHAR2)
      RETURN all_bank_branches_ref
   IS
      v_all_bankbranches_ref   all_bank_branches_ref;
   BEGIN
      OPEN v_all_bankbranches_ref FOR
         SELECT bbr_code, bbr_bnk_code, bbr_branch_name, bbr_remarks,
                bbr_sht_desc, bbr_ref_code, bbr_eft_supported,
                bbr_dd_supported, bbr_date_created, bbr_created_by,
                bbr_physical_addrs
           FROM tqc_bank_branches
          WHERE bbr_branch_name = v_branch_name;

      RETURN v_all_bankbranches_ref;
   END;

   PROCEDURE allbankbranches (v_bankbranchref OUT allbankbranchesref)
   IS
   BEGIN
      OPEN v_bankbranchref FOR
         SELECT bbr_code, bbr_bnk_code, bbr_branch_name, bbr_remarks,
                bbr_sht_desc, bbr_ref_code, bbr_eft_supported,
                bbr_dd_supported, bbr_date_created, bbr_created_by,
                bbr_physical_addrs
           FROM tqc_bank_branches;
   END allbankbranches;

    FUNCTION selectallmessages (
      v_sms_sys_code     VARCHAR2,
      v_sms_wef          DATE,
      v_sms_wet          DATE,
      v_sms_trans_type   VARCHAR2
   )
      RETURN allmessagesref
   IS
      v_messagesref   allmessagesref;
   BEGIN
--    raise_error('v_sms_wef = '||v_sms_wef
--        ||'; v_sms_wet = '||v_sms_wet
--        ||'; v_sms_trans_type = '||v_sms_trans_type
--        ||'; v_sms_sys_code = '||v_sms_sys_code
--        );
      OPEN v_messagesref FOR
         SELECT DISTINCT sms_code, sms_sys_code, tsm_desc, sms_clnt_code,
                         sms_agn_code, sms_pol_no, sms_pol_code, sms_clm_no,
                         sms_tel_no, sms_msg,
                         DECODE (sms_status, 'D', 'Not Sent','OK', 'Sent','X', 'Deleted'),
                         sms_prepared_by,
                         TO_CHAR (sms_send_date, 'dd-month-yyyy HH12:MI pm'),
                         NULL pol_current_status,  clnt_name || ' ' || clnt_other_names clnt_name, cou_code,
                         cou_zip_code,
                         (select div_name from gin_policies,tqc_divisions where pol_policy_no=sms_pol_no and pol_div_code = div_code(+) and pol_div_code is not null and rownum=1) div_name,
                         sms_prepared_date
                    FROM tqc_sms_messages,gin_policies,
                         tqc_clients,
                         tqc_system_modules,
                         tqc_countries,
                         tqc_divisions
                   WHERE
                         sms_status = 'D'
                     and sms_pol_no = pol_policy_no(+)
                     and pol_div_code = div_code(+)
                     AND sms_sys_module = tsm_sht_desc(+)
                     AND clnt_cou_code = cou_code(+)
                     AND sms_sys_code = v_sms_sys_code
                     AND sms_clnt_code = clnt_code
                     AND TO_DATE(sms_prepared_date,'dd-MON-rrrr') BETWEEN  TO_DATE(NVL (v_sms_wef, sms_prepared_date),'dd-MON-rrrr')
                     AND TO_DATE(NVL (v_sms_wet, sms_prepared_date),'dd-MON-rrrr')
                     -- AND div_name IS NOT NULL
                     -- AND sms_prepared_date >=
                     --                       NVL (v_sms_wef, sms_prepared_date)
                     --AND sms_prepared_date <=
                     --                       NVL (v_sms_wet, sms_prepared_date)
                     AND (
                              (LOWER (sms_msg) LIKE
                                    DECODE (v_sms_trans_type,
                                           'AUTH', '%authoriz%',
                                           'PI', '%first premium%',
                                           'PA', '%authoriz%',
                                           'PRA', '%deducted and allocated%',
                                           'DC', '%outstanding%',
                                           'CN', '%cancel%',
                                           'CONV', '%accept%',
                                           'LAPSE', '%laps%',
                                           'RN', '%renew%',
                                           'IF','%fail%',
                                           'BM', '%birthday%',
                                           'CLM', '%claim%',
                                           'DBT', '%debt%'
                                           ))
                                       OR 
                               NVL (v_sms_trans_type, 'U') = 'U'
                         )
                 ORDER BY tsm_desc;

      RETURN v_messagesref;
   END;

   FUNCTION getreservedwords (v_syscode NUMBER)
      RETURN reserved_words_ref
   IS
      v_cursor   reserved_words_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tsrw_code, tsrw_sys_code, tsrw_tsrc_code, tsrw_type,
                tsrw_editable, tsrc_name, tsrw_name, tsrw_desc,
                tsrc_validity, tsrc_usr_code, usr_type, usr_email
           FROM tqc_sys_reserved_words, tqc_serv_req_cat, tqc_users
          WHERE tsrw_tsrc_code = tsrc_code(+)
            AND tsrc_usr_code = usr_code(+)
            AND tsrw_sys_code = v_syscode;

      RETURN v_cursor;
   END;

   FUNCTION getsystemprocesses (
      v_syscode   tqc_sys_processes.sprc_sys_code%TYPE
   )
      RETURN sys_processes_ref
   IS
      v_cursor   sys_processes_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   MAX (deployment_) deployment, jsd_sys_code, jsd_jpdl_name
             FROM jbpm4_sys_deployments, jbpm4_deployprop
            WHERE jsd_sys_code = v_syscode AND jsd_jpdl_name = objname_
         GROUP BY jsd_sys_code, jsd_jpdl_name
         UNION
         SELECT   MAX (deployment_) deployment, jsd_sys_code, jsd_jpdl_name
             FROM jbpm4_sys_deployments, tq_gis.jbpm4_deployprop_gis
            WHERE jsd_jpdl_name = objname_ AND jsd_sys_code = v_syscode
         GROUP BY jsd_sys_code, jsd_jpdl_name
         ORDER BY jsd_jpdl_name;

      RETURN v_cursor;
   END;

   FUNCTION getescalations (
      v_syscode    tqc_sys_escalation_levels.tsel_jsd_sys_code%TYPE,
      v_jpdlname   tqc_sys_escalation_levels.tsel_jsd_jpdl_name%TYPE,
      v_activity   tqc_sys_escalation_levels.tsel_activity_name%TYPE,
      v_level      tqc_sys_escalation_levels.tsel_level%TYPE DEFAULT NULL
   )
      RETURN escalations_ref
   IS
      v_cursor   escalations_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tsel_code, tsel_jsd_sys_code, tsel_jsd_jpdl_name,
                tsel_activity_name, tsel_level, tsel_assignee,
                a.usr_username, tsel_duration, tsel_cc, b.usr_username
           FROM tqc_sys_escalation_levels, tqc_users a, tqc_users b
          WHERE tsel_assignee = a.usr_code
            AND tsel_jsd_sys_code = v_syscode
            AND tsel_jsd_jpdl_name = v_jpdlname
            AND tsel_level = NVL (v_level, tsel_level)
            AND tsel_cc = b.usr_code(+)
            AND tsel_activity_name = v_activity;

      RETURN v_cursor;
   END;

   FUNCTION checkif_losstime_required
      RETURN checkif_losstime_required_ref
   IS
      v_cursor   checkif_losstime_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT gin_parameters_pkg.get_param_varchar ('LOSS_DATE_REQUIRED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION checkif_occupation_required
      RETURN checkif_losstime_required_ref
   IS
      v_cursor   checkif_losstime_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tqc_parameters_pkg.get_param_varchar ('OCCUPATION')
           FROM DUAL;

      RETURN v_cursor;
   END;

   PROCEDURE select_parent_agency (
      v_cursor     OUT      select_parent_agency_ref,
      v_act_code   IN       NUMBER
   )
   IS
   -- v_cursor select_Parent_Agency_ref;
   BEGIN
      --RAISE_ERROR('v_act_Code'||v_act_Code);
      IF v_act_code = 25
      THEN
         OPEN v_cursor FOR
            SELECT agn_code, agn_sht_desc, agn_name
              FROM tqc_agencies
             WHERE agn_act_code IN (2);
      ELSIF v_act_code IN (4, 7)
      THEN
         OPEN v_cursor FOR
            SELECT agn_code, agn_sht_desc, agn_name
              FROM tqc_agencies
             WHERE agn_act_code IN (5, 3);
      END IF;
   END select_parent_agency;

   PROCEDURE select_subagents_datails (
      v_cursor     OUT      select_subagents_datails_ref,
      v_agn_code   IN       NUMBER
   )
   IS
   --v_cursor select_subAgents_datails_ref;
   BEGIN
      --RAISE_ERROR('v_agn_code'||v_agn_code);
      OPEN v_cursor FOR
         SELECT agn_code, agn_act_code, agn_sht_desc, agn_name,
                agn_physical_address, agn_postal_address, agn_email_address,
                agn_subagent, agn_main_agn_code, act_account_type
           FROM tqc_agencies, tqc_account_types
          WHERE agn_main_agn_code = v_agn_code AND agn_act_code = act_code;
   END select_subagents_datails;

   FUNCTION checkif_serialno_required
      RETURN checkif_serialno_required_ref
   IS
      v_cursor   checkif_serialno_required_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT gin_parameters_pkg.get_param_varchar
                                                    ('SERIAL_NUMBER_REQUIRED')
           FROM DUAL;

      RETURN v_cursor;
   END;

   FUNCTION getdefaultsmssettings
      RETURN sms_settings_ref
   IS
      v_cursor   sms_settings_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tss_desc, tss_url, tss_username, tss_password, tss_source,
                tss_code, tss_default
           FROM tqc_system_sms
          WHERE tss_default = 'Y';

      RETURN v_cursor;
   END;

   FUNCTION getpendingsms
      RETURN pending_sms_ref
   IS
      v_cursor   pending_sms_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT sms_code, sms_tel_no, sms_msg
           FROM tqc_sms_messages
          WHERE sms_status != 'OK';

      RETURN v_cursor;
   END;

   FUNCTION getsmssettings
      RETURN sms_settings_ref
   IS
      v_cursor   sms_settings_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tss_desc, tss_url, tss_username, tss_password, tss_source,
                tss_code, tss_default
           FROM tqc_system_sms;

      RETURN v_cursor;
   END;

   FUNCTION getgroupedusersemails (v_gusr_grp_usr_code NUMBER)
      RETURN email_ref
   IS
      maillist   email_ref;
   BEGIN
      OPEN maillist FOR
         SELECT usr_email
           FROM tqc_users
          WHERE usr_code IN (SELECT gusr_usr_code
                               FROM tqc_group_users
                              WHERE gusr_grp_usr_code = v_gusr_grp_usr_code);

      RETURN maillist;
   END;

   FUNCTION getmarketers
      RETURN marketers_ref
   IS
      v_cursor   marketers_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT agn_code, agn_act_code, agn_sht_desc, agn_name
           FROM tqc_agencies
          WHERE agn_act_code = 10;

      RETURN v_cursor;
   END;

   FUNCTION getwebclientsbranches (v_tcb_clnt_code IN NUMBER)
      RETURN webclientsbranchesref
   IS
      v_cursor   webclientsbranchesref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tcb_code, tcb_clnt_code, tcb_sht_desc, tcb_name
           FROM tqc_client_branches
          WHERE tcb_clnt_code = v_tcb_clnt_code;

      RETURN v_cursor;
   END;

   FUNCTION getunassignedbranches (v_tcb_clnt_code IN NUMBER)
      RETURN webassignbranchref
   IS
      v_cursor   webassignbranchref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tcb_code, tcb_clnt_code, tcb_sht_desc, tcb_name
           FROM tqc_client_branches
          WHERE tcb_code NOT IN (SELECT tcub_tcb_code
                                   FROM tqc_client_usr_branches)
            AND tcb_clnt_code = v_tcb_clnt_code;

      RETURN v_cursor;
   END;

   FUNCTION getassignedbranches (v_tcb_clnt_code IN NUMBER)
      RETURN webassignbranchref
   IS
      v_cursor   webassignbranchref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tcb_code, tcb_clnt_code, tcb_sht_desc, tcb_name
           FROM tqc_client_branches
          WHERE tcb_code IN (SELECT tcub_tcb_code
                               FROM tqc_client_usr_branches)
            AND tcb_clnt_code = v_tcb_clnt_code;

      RETURN v_cursor;
   END;

   FUNCTION getdefaultbranches
      RETURN webclientsbranchesref
   IS
      v_cursor   webclientsbranchesref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tcb_code, tcb_clnt_code, tcb_sht_desc, tcb_name
           FROM tqc_client_branches
          WHERE tcb_code IN (SELECT tcub_tcb_code
                               FROM tqc_client_usr_branches);

      RETURN v_cursor;
   END;

   FUNCTION getwebproducts
      RETURN webproductsref
   IS
      v_cursor   webproductsref;
   BEGIN
      OPEN v_cursor FOR
         SELECT twp_code, twp_twpc_code, twp_prod_code, twp_prod_desc,
                pro_desc
           FROM tqc_web_products, gin_products
          WHERE twp_prod_code = pro_code
         UNION
         SELECT twp_code, twp_twpc_code, twp_prod_code, twp_prod_desc,
                prod_desc
           FROM tqc_web_products, lms_products
          WHERE twp_prod_code = prod_code;

      RETURN v_cursor;
   END;

   FUNCTION getwebusers
      RETURN webusersref
   IS
      v_cursor   webusersref;
   BEGIN
      OPEN v_cursor FOR
         SELECT usr_code, usr_username, usr_name, usr_status
           FROM tqc_users, tqc_sys_roles, tqc_user_systems
          WHERE usys_usr_code = usr_code
            AND usys_sys_code = srls_sys_code
            AND srls_sht_desc = 'QT'
            AND usr_status = 'A';

      RETURN v_cursor;
   END;

   FUNCTION getwebproductdetails (v_twpd_clnt_code IN NUMBER)
      RETURN webproductsdtlsref
   IS
      v_cursor   webproductsdtlsref;
   BEGIN
      OPEN v_cursor FOR
         SELECT twpd_clnt_code, twpd_twp_code, clnt_name, twp_prod_desc,
                twpd_usr_code, twpd_username, twpd_dr_limit, twpd_cr_limit,
                twpd_policy_use, twpd_endos_use, pro_desc, clna_code,
                clna_sht_desc
           FROM tqc_web_product_details,
                tqc_clients,
                tqc_web_products,
                gin_products,
                tqc_client_accounts
          WHERE twpd_clnt_code = clnt_code
            AND twpd_twp_code = twp_code
            AND twp_prod_code = pro_code
            AND clnt_code = clna_clnt_code
            AND twpd_clnt_code = v_twpd_clnt_code;

      RETURN v_cursor;
   END;

   FUNCTION getallsystems
      RETURN allsystems_ref
   IS
      v_cursor   allsystems_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT sys_code, sys_sht_desc, sys_name
           FROM tqc_systems;

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
   FUNCTION get_dispatch_docs (
      v_batch       NUMBER,
      v_clnt_code   NUMBER,
      v_agn_code    NUMBER
   )
      RETURN dispatch_docs_ref
   IS
      v_cursor   dispatch_docs_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT docr_id, docr_doc_name, docr_doc_url, docr_doc_author,
                docr_doc_desc, docr_clnt_code, docr_pol_policy_no,
                docr_claim_no, docr_quot_code, docr_level, docr_sys_code,
                docr_pol_batch_no, docr_date_created, docr_agn_code,
                docr_dispatched, docr_dispatch_dt, docr_dispatchable
           FROM tqc_documents_register
          WHERE docr_pol_batch_no = v_batch
            AND docr_clnt_code = NVL (v_clnt_code, docr_clnt_code)
            --AND  NVL(DOCR_AGN_CODE,-2000)=NVL(NVL(v_agn_code,-2000),NVL(DOCR_AGN_CODE,-2000))
            AND docr_sys_code = 37;

      RETURN v_cursor;
   END;

   FUNCTION getmainreservedword (v_syscode NUMBER)
      RETURN reserved_words_ref
   IS
      v_cursor   reserved_words_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tsrw_code, tsrw_sys_code, tsrw_tsrc_code, tsrw_type,
                tsrw_editable, tsrc_name, tsrw_name, tsrw_desc,
                tsrc_validity, tsrc_usr_code, usr_type, usr_email
           FROM tqc_sys_reserved_words, tqc_serv_req_cat, tqc_users
          WHERE tsrw_tsrc_code = tsrc_code(+)
            AND tsrc_usr_code = usr_code(+)
            AND tsrw_sys_code = NVL (v_syscode, tsrw_sys_code)
            AND tsrw_editable = 'N';

      RETURN v_cursor;
   END;

   FUNCTION get_sms_template
      RETURN sms_templates_ref
   IS
      v_cursor   sms_templates_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type
           FROM tqc_msg_templates
          WHERE msgt_type = 'SMS';

      RETURN (v_cursor);
   END;

   FUNCTION get_email_template
      RETURN sms_templates_ref
   IS
      v_cursor   sms_templates_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type
           FROM tqc_msg_templates
          WHERE msgt_type = 'EMAIL';

      RETURN (v_cursor);
   END;

   FUNCTION get_sms_template (v_msgt_sys_code IN NUMBER)
      RETURN sms_templates_ref
   IS
      v_cursor   sms_templates_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type
           FROM tqc_msg_templates
          WHERE msgt_type = 'SMS';

      RETURN (v_cursor);
   END;

   FUNCTION get_email_template (v_msgt_sys_code IN NUMBER)
      RETURN sms_templates_ref
   IS
      v_cursor   sms_templates_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type
           FROM tqc_msg_templates
          WHERE msgt_type = 'EMAIL';

      RETURN (v_cursor);
   END;

   FUNCTION getsystemreqdocs (v_system_code IN NUMBER)
      RETURN req_doc_ref
   IS
      req_doc   req_doc_ref;
   BEGIN
      IF v_system_code = '37'
      THEN
         OPEN req_doc FOR
            SELECT rdoc_id, rdoc_sht_desc, rdoc_desc, rdoc_mandtry,
                   rdoc_nb_doc, rdoc_en_doc, rdoc_rn_doc, rdoc_cert_doc,
                   rdoc_clm_lop_doc, rdoc_clm_pay_doc, rdoc_valid_prd,
                   rqd_code
              FROM gin_reqrd_documents, tqc_required_docs
             WHERE rqc_rdoc_id(+) = rdoc_id;

         RETURN req_doc;
      ELSE
         OPEN req_doc FOR
            SELECT rdoc_id, rdoc_sht_desc, rdoc_desc, rdoc_mandtry,
                   rdoc_nb_doc, rdoc_en_doc, rdoc_rn_doc, rdoc_cert_doc,
                   rdoc_clm_lop_doc, rdoc_clm_pay_doc, rdoc_valid_prd,
                   rqd_code
              FROM gin_reqrd_documents, tqc_required_docs
             WHERE rqc_rdoc_id(+) = rdoc_id;

         RETURN req_doc;
      END IF;
   END;

   FUNCTION get_web_prod_user (v_prod_code NUMBER, v_clnt_code NUMBER)
      RETURN VARCHAR2
   IS
      v_user   VARCHAR2 (30);
   BEGIN
      SELECT twpd_username
        INTO v_user
        FROM tqc_web_products, tqc_web_product_details
       WHERE twpd_twp_code = twp_code
         AND twp_prod_code = v_prod_code
         AND twpd_clnt_code = v_clnt_code;

      RETURN v_user;
   END;

   FUNCTION get_available_prefixes
      RETURN mob_prefix_ref
   IS
      v_cursor   mob_prefix_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT mptp_code, mptp_mob_no_prefix, mptp_mpt_code
           FROM tqc_mob_pymnt_types_prefixes;

      RETURN v_cursor;
   END;

   FUNCTION getscheduledjobs
      RETURN scheduledjobs_ref
   IS
      v_cursor   scheduledjobs_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT qt_job_name, qt_description, qt_start_time, qt_end_time,
                qt_cron_expression
           FROM qrtz_triggers
          WHERE qt_job_name IS NOT NULL
            AND qt_cron_expression IS NOT NULL
            AND qt_status = 'A';

      RETURN (v_cursor);
   END;

   FUNCTION getpoliciestwomonthsren
      RETURN policiesdueforren_ref
   IS
      v_cursor   policiesdueforren_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT pol_policy_no, pol_policy_cover_to,
                TO_DATE (pol_renewal_dt, 'DD/MM/RRRR') pol_renewal_dt,
                clnt_code, clnt_name || '' || clnt_other_names client,
                clnt_sms_tel, pol_agnt_agent_code
           FROM gin_policies, tqc_clients
          WHERE pol_prp_code = clnt_code
            AND pol_policy_cover_to = TO_DATE (SYSDATE, 'DD/MM/RRRR') + 60;

      RETURN (v_cursor);
   END;

   FUNCTION getpoliciesonemonthsren
      RETURN policiesdueforren_ref
   IS
      v_cursor   policiesdueforren_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT pol_policy_no, pol_policy_cover_to,
                TO_DATE (pol_renewal_dt, 'DD/MM/RRRR') pol_renewal_dt,
                clnt_code, clnt_name || '' || clnt_other_names client,
                clnt_sms_tel, pol_agnt_agent_code
           FROM gin_policies, tqc_clients
          WHERE pol_prp_code = clnt_code
            AND pol_policy_cover_to = TO_DATE (SYSDATE, 'DD/MM/RRRR') + 30;

      RETURN (v_cursor);
   END;

   FUNCTION getpoliciestwodaysren
      RETURN policiesdueforren_ref
   IS
      v_cursor   policiesdueforren_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT pol_policy_no, pol_policy_cover_to,
                TO_DATE (pol_renewal_dt, 'DD/MM/RRRR') pol_renewal_dt,
                clnt_code, clnt_name || '' || clnt_other_names client,
                clnt_sms_tel, pol_agnt_agent_code
           FROM gin_policies, tqc_clients
          WHERE pol_prp_code = clnt_code
            AND pol_policy_cover_to = TO_DATE (SYSDATE, 'DD/MM/RRRR') + 2;

      RETURN (v_cursor);
   END;

   FUNCTION getagencysystems
      RETURN all_systems_ref
   IS
      v_cursor   all_systems_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   -2000 sys_code, 'GIS AND LMS', 'GIS AND LMS' sys_name,
                  NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                  NULL
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

   FUNCTION getclientbirthdays
      RETURN clientbirhdays_ref
   IS
      v_cursor   clientbirhdays_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT clnt_code, clnt_name || ' ' || clnt_other_names clientname,
                clnt_dob, clnt_sms_tel
           FROM tqc_clients
          WHERE TO_CHAR (clnt_dob, 'DD/MM') = TO_CHAR (SYSDATE, 'DD/MM');

      --AND CLNT_CODE=111;
      RETURN (v_cursor);
   END;

   FUNCTION getbranchunits
      RETURN branchunits_ref
   IS
      v_cursor   branchunits_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT bru_code, bru_brn_code, bru_sht_desc, bru_name
           FROM tqc_branch_units
          WHERE bru_status = 'A';

      RETURN v_cursor;
   END;
   
   FUNCTION getBranchUnitsPerRegion(v_region_code NUMBER)
      RETURN branchUnitsPerRegion_ref
   IS
      v_cursor   branchUnitsPerRegion_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT bru_code, bru_brn_code, bru_sht_desc, bru_name
            FROM tqc_branch_units,tqc_branches,tqc_regions
            WHERE bru_status = 'A'
            AND brn_reg_code = reg_code
            AND bru_brn_code = brn_code
            AND reg_code = brn_reg_code
            AND reg_code = v_region_code;
      RETURN v_cursor;
   END;

   FUNCTION get_debit_day (v_type VARCHAR2, v_value VARCHAR2)
      RETURN VARCHAR2
   IS
      v_return   VARCHAR2 (100);
   BEGIN
      IF v_type = 'PL'
      THEN
         BEGIN
            SELECT endr_bo_debit_day
              INTO v_return
              FROM lms_policies, lms_policy_endorsements
             WHERE endr_code = pol_current_endr_code
               AND pol_code = TO_NUMBER (v_value);
         EXCEPTION
            WHEN OTHERS
            THEN
               v_return := NULL;
         END;
      ELSIF v_type = 'MKT'
      THEN
         BEGIN
            SELECT ppr_bo_debit_day
              INTO v_return
              FROM lms_pol_proposals
             WHERE ppr_code = TO_NUMBER (v_value);
         EXCEPTION
            WHEN OTHERS
            THEN
               v_return := NULL;
         END;
      END IF;

      RETURN v_return;
   END get_debit_day;

   PROCEDURE getserviceprovider (
      v_spr_name               IN       VARCHAR2,
      v_service_provider_ref   OUT      service_provider_ref,
      v_spr_spt_code           IN       NUMBER
   )
   IS
   BEGIN
      OPEN v_service_provider_ref FOR
         SELECT spr_code, spr_sht_desc, spr_name, spr_physical_address,
                spr_postal_address, spr_phone, spr_fax, spr_email,
                spr_created_by, spr_date_created, spr_status_remarks,
                spr_status, spr_pin_number, spr_mobile_no, spr_inhouse,
                spr_sms_number
           FROM tqc_service_providers
          WHERE spr_name LIKE '%' || NVL (v_spr_name, 'NONAME') || '%'
            AND spr_spt_code = v_spr_spt_code;
   END;

   FUNCTION getreportsassigned (v_rpt_rsm_code IN NUMBER)
      RETURN systemrpt_ref
   IS
      v_cursor   systemrpt_ref;
   BEGIN
--RAISE_ERROR('v_rpt_rsm_code'||v_rpt_rsm_code);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_description, rpt_active
           FROM tqc_system_reports
          WHERE rpt_rsm_code = v_rpt_rsm_code;

      RETURN v_cursor;
   END;

   FUNCTION getreportsunassigned (v_rpt_rsm_code IN NUMBER)
      RETURN systemrpt_ref
   IS
      v_cursor   systemrpt_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_description, rpt_active
           FROM tqc_system_reports
          WHERE rpt_rsm_code != v_rpt_rsm_code
         UNION
         SELECT rpt_code, rpt_name, rpt_description, rpt_active
           FROM tqc_system_reports
          WHERE rpt_rsm_code IS NULL;

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
         SELECT cmp_code, cmp_date, cmp_name, cmp_type, cmp_status
           FROM tqc_campaigns;

      RETURN v_cursor;
   END;

   PROCEDURE get_sectors_occup (v_sectors_ref OUT sectors_occup_ref)
   IS
   BEGIN
      OPEN v_sectors_ref FOR
         SELECT   sectors.*
             FROM (SELECT NULL sec_code, NULL sec_sht_desc, NULL sec_name, NULL occ_name, NULL occ_code
                     FROM DUAL
                   UNION
                   SELECT sec_code, sec_sht_desc, sec_name, occ_name,
                          occ_code
                     FROM tqc_sectors, tqc_occupations
                    WHERE OCC_CODE IN (SELECT OCC_CODE FROM tqc_sector_occupations so WHERE so.OCC_SEC_CODE = SEC_CODE)) sectors
         ORDER BY sectors.sec_name NULLS FIRST;
   END get_sectors_occup;

   PROCEDURE get_occupations (v_sect_code NUMBER, v_sectors_ref OUT sectors_ref)
   IS
   BEGIN
      OPEN v_sectors_ref FOR
         SELECT   occ_code, occ_sht_desc, occ_name
             FROM tqc_occupations
            WHERE occ_sec_code = v_sect_code
         ORDER BY occ_name;
   END get_occupations;

   FUNCTION get_all_towns
      RETURN all_towns_ref
   IS
      v_cursor   all_towns_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT twn_code, twn_cou_code, twn_sht_desc, twn_name
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
          WHERE b.clnt_code = a.clnt_clnt_code
            AND a.clnt_clnt_code = v_clnt_code
         UNION
         SELECT spr_name, spt_name, spr_sht_desc, spr_physical_address,
                spr_postal_address, spr_email
           FROM tqc_service_providers, tqc_clients,
                tqc_service_provider_types
          WHERE spr_clnt_code = clnt_code
            AND spt_code = spr_spt_code
            AND spr_clnt_code = v_clnt_code
         UNION
         SELECT agn_name, act_account_type, agn_sht_desc,
                agn_physical_address, agn_postal_address, agn_email_address
           FROM tqc_agencies, tqc_clients, tqc_account_types
          WHERE agn_clnt_code = clnt_code
            AND act_code = agn_act_code
            AND agn_clnt_code = v_clnt_code;

      RETURN v_cursor;
   END;

   FUNCTION get_rating_starndards (v_ors_rorg_code NUMBER)
      RETURN rating_starndards_ref
   IS
      v_cursor   rating_starndards_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT ors_code, ors_desc
           FROM tqc_org_rating_starndards
          WHERE ors_rorg_code = v_ors_rorg_code;

      RETURN (v_cursor);
   END;

   FUNCTION get_ratings
      RETURN ratings_ref
   IS
      v_cursor   ratings_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT rorg_code, rorg_desc, rorg_sht_desc
           FROM tqc_rating_organizations;

      RETURN (v_cursor);
   END;

   FUNCTION getallsmsmessages (
      v_sms_sys_code     IN   NUMBER,
      v_sms_wef               DATE,
      v_sms_wet               DATE,
      v_sms_trans_type        VARCHAR2
   )
      RETURN allmessagesref
   IS
      v_cursor   allmessagesref;
   BEGIN
      --RAISE_ERROR('v_sms_sys_code'||v_sms_sys_code);
      OPEN v_cursor FOR
         SELECT DISTINCT sms_code, sms_sys_code, tsm_desc, sms_clnt_code,
                         sms_agn_code, sms_pol_no, sms_pol_code, sms_clm_no,
                         TO_CHAR (sms_tel_no), sms_msg,
                         DECODE (sms_status, 'D', 'Not Sent', 'Sent'),
                         sms_prepared_by,
                         TO_CHAR (sms_send_date, 'dd-month-yyyy HH12:MI pm'),
                         NULL pol_current_status, clnt_name, cou_code,
                         cou_zip_code,div_name,sms_prepared_date
                    FROM tqc_sms_messages,
                         tqc_clients,
                         tqc_agencies,
                         tqc_users,
                         tqc_countries,
                         tqc_system_modules,
                         tqc_brnch_divisions,
                         tqc_divisions
                   WHERE sms_clnt_code = clnt_code(+)
                     AND sms_agn_code = agn_code(+)
                     AND sms_usr_code = usr_code(+)
                     AND sms_sys_module = tsm_sht_desc(+)
                     AND clnt_bdiv_code = bdiv_code(+)
                     AND bdiv_div_code = div_code(+)
                     AND cou_code = clnt_cou_code
                     AND sms_sys_code = v_sms_sys_code
                     AND TO_DATE(sms_prepared_date,'dd-MON-rrrr') BETWEEN  TO_DATE(NVL (v_sms_wef, sms_prepared_date),'dd-MON-rrrr')
                     AND TO_DATE(NVL (v_sms_wet, sms_prepared_date),'dd-MON-rrrr')
                     --AND sms_prepared_date >=
                     --                       NVL (v_sms_wef, sms_prepared_date)
                     --AND sms_prepared_date <=
                     --                       NVL (v_sms_wet, sms_prepared_date)
                     AND (   NVL (v_sms_trans_type, 'U') = 'U'
                          OR LOWER (sms_msg) LIKE
                                DECODE (v_sms_trans_type,
                                           'AUTH', '%authoriz%',
                                           'PI', '%first premium%',
                                           'PA', '%authoriz%',
                                           'PRA', '%deducted and allocated%',
                                           'DC', '%outstanding%',
                                           'CN', '%cancel%',
                                           'CONV', '%accept%',
                                           'LAPSE', '%laps%',
                                           'RN', '%renew%',
                                           'IF','%fail%',
                                           'BM', '%birthday%',
                                           'CLM', '%claim%',
                                           'DBT', '%debt%'
                                           )
                         );

      RETURN (v_cursor);
   END;

   FUNCTION getallemailmessages (
      v_email_sys_code     IN   NUMBER,
      v_email_status       IN   VARCHAR2,
      v_email_wef               DATE,
      v_email_wet               DATE,
      v_email_trans_type        VARCHAR2
   )
      RETURN allsmsmessages_ref
   IS
      v_cursor   allsmsmessages_ref;
   BEGIN
    /*raise_error('v_email_wef = '||v_email_wef
        ||'; v_email_wet = '||v_email_wet
        ||'; v_email_trans_type = '||v_email_trans_type
        ||'; v_email_sys_code = '||v_email_sys_code
        ||'; v_email_status = '||v_email_status
        );*/
      OPEN v_cursor FOR
         SELECT email_code, email_sys_code, email_sys_module,
                email_clnt_code, email_agn_code, email_pol_code,
                email_pol_no, email_clm_no, NULL email_tel_no, email_msg,
                DECODE (email_status, 'S', 'Sent', 'Not Sent'),
                email_prepared_by, email_prepared_date, email_send_date,
                email_quot_code, NULL email_quot_no, email_usr_code,
                email_sent_response,
                clnt_name || ' ' || clnt_other_names client, agn_name,
                usr_username, email_address, email_subj
           FROM tqc_email_messages, tqc_clients, tqc_agencies, tqc_users
          WHERE email_clnt_code = clnt_code(+)
            AND email_agn_code = agn_code(+)
            AND email_usr_code = usr_code(+)
            AND email_sys_code = v_email_sys_code
            AND email_status = NVL (v_email_status, email_status)
            AND TO_DATE(email_prepared_date,'dd-MON-rrrr') BETWEEN  TO_DATE(NVL (v_email_wef, email_prepared_date),'dd-MON-rrrr')
            AND TO_DATE(NVL (v_email_wet, email_prepared_date),'dd-MON-rrrr')
            AND (   NVL (v_email_trans_type, 'U') = 'U'
                 OR LOWER (email_msg) LIKE
                       DECODE (v_email_trans_type,
                               'AUTH', '%authoriz%',
                               'PI', '%first premium%',
                               'PA', '%authoriz%',
                               'PRA', '%deducted and allocated%',
                               'DC', '%outstanding%',
                               'CN', '%cancel%',
                               'CONV', '%accept%',
                               'LAPSE', '%laps%',
                               'RN', '%renewed%',
                               'IF','%fail%'
                              )
                );

      RETURN (v_cursor);
   END;

   FUNCTION getlocationdetails (v_loc_twn_code IN NUMBER)
      RETURN locationdetails_ref
   IS
      v_cursor   locationdetails_ref;
   BEGIN
--RAISE_ERROR('v_loc_twn_code'||v_loc_twn_code);
      OPEN v_cursor FOR
         SELECT loc_code, loc_twn_code, loc_sht_desc, loc_name
           FROM tqc_locations
          WHERE loc_twn_code = v_loc_twn_code;

      RETURN (v_cursor);
   END;

   FUNCTION getorgdivlevelstype
      RETURN orgdivlevelstype_ref
   IS
      v_cursor   orgdivlevelstype_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   dlt_code, dlt_sys_code, dlt_desc, dlt_act_code,
                  acc_type_name (dlt_act_code) acc_type_name, dlt_code_val
             FROM tqc_org_division_levels_type
         ORDER BY dlt_desc;

      RETURN v_cursor;
   END;

   FUNCTION get_bussiness_person (
      v_bpn_payee_type   IN   VARCHAR2,
      v_bpn_clnt_code    IN   NUMBER
   )
      RETURN bussiness_person_ref
   IS
      v_cursor   bussiness_person_ref;
   BEGIN
-- RAISE_ERROR('v_bpn_payee_type'||v_bpn_payee_type||'v_bpn_clnt_code'||v_bpn_clnt_code);
      OPEN v_cursor FOR
         SELECT bpn_code, bpn_id_no, bpn_address, bpn_tel, bpn_mobile_no,
                bpn_email, bpn_type, bpn_zip, bpn_town, bpn_cou_code,
                bpn_name, bpn_pin, bpn_bbr_code, bpn_bank_acc_no,
                bpn_bbr_swift_code, bpn_reg_clmt_code, bbr_branch_name,
                cou_name, NULL bpn_payee_type
           FROM tqc_business_persons, tqc_bank_branches, tqc_countries
          WHERE bbr_code(+) = bpn_bbr_code
            AND cou_code(+) = bpn_cou_code
            AND bpn_payee_type = v_bpn_payee_type
            AND bpn_clnt_code = v_bpn_clnt_code;

      RETURN (v_cursor);
   END;

   FUNCTION get_bank_branches_val
      RETURN bank_branches_val_ref
   IS
      v_cursor   bank_branches_val_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT bbr_code, bbr_bnk_code, bbr_branch_name, bbr_remarks,
                bbr_sht_desc
           FROM tqc_bank_branches;

      RETURN (v_cursor);
   END;

   FUNCTION get_registered_claimants
      RETURN registered_claimants_ref
   IS
      v_cursor   registered_claimants_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT reg_clmt_code, cld_code, cld_id_no,
                cld_other_names || ' ' || cld_surname
           FROM gin_rgstd_claimants, gin_claimants
          WHERE cld_code = reg_cld_code;

      RETURN (v_cursor);
   END;



   FUNCTION get_agent_type (v_agn_act_code IN NUMBER)
      RETURN agent_type_ref
   IS
      v_cursor   agent_type_ref;
   BEGIN
    -- raise_error('v_agn_act_code: '||v_agn_act_code);
      OPEN v_cursor FOR
         SELECT DISTINCT AGNTY_TYPE
          FROM TQC_AGENT_TYPES
                  WHERE agnty_act_code = v_agn_act_code;
      RETURN (v_cursor);
   END;
   
   FUNCTION get_agent_types
      RETURN tqc_agent_types_ref
   IS
      v_cursor   tqc_agent_types_ref;
   BEGIN
      OPEN v_cursor FOR
          SELECT  AGNTY_CODE, AGNTY_TYPE_SHT_DESC, AGNTY_TYPE,AGNTY_ACT_CODE,ACT_ACCOUNT_TYPE
          FROM TQC_AGENT_TYPES,TQC_ACCOUNT_TYPES
          WHERE AGNTY_ACT_CODE=ACT_CODE;
      RETURN (v_cursor);
   END;

   FUNCTION get_agent_groups
      RETURN agent_type_ref
   IS
      v_cursor   agent_type_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT agn_group
                    FROM tqc_agencies
                   WHERE agn_group IS NOT NULL;

      RETURN (v_cursor);
   END;

   FUNCTION get_occupation(v_sect_code NUMBER)
      RETURN occupation_ref
   IS
      v_cursor   occupation_ref;
   BEGIN
--      raise_error('v_sect_code: '||v_sect_code);
      OPEN v_cursor FOR
         SELECT occ_code, occ_sec_code, occ_sht_desc, occ_name
           FROM tqc_occupations
           WHERE occ_sec_code=v_sect_code;
      RETURN (v_cursor);
   END;

   FUNCTION getlabels (v_labelname VARCHAR2)
      RETURN label_ref
   IS
      v_cursor   label_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT label_name, label_value
           FROM tqc_labels
          WHERE label_status = 'ACTIVE' AND label_name = v_labelname;

      RETURN (v_cursor);
   END;

   PROCEDURE get_labels (v_parameters_ref OUT parameters_ref)
   IS
   BEGIN
      OPEN v_parameters_ref FOR
         SELECT label_code, label_name, label_value, label_status,
                label_desc
           FROM tqc_labels;
   END get_labels;

   FUNCTION getdistrictsbycountry (v_cou_code IN NUMBER)
      RETURN districts_ref
   IS
      v_cursor   districts_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   dist_code, dist_cou_code, dist_sht_desc, dist_name,
                  dist_sts_code
             FROM tqc_districts
            WHERE dist_cou_code = v_cou_code
         ORDER BY dist_name;

      RETURN v_cursor;
   END;

   FUNCTION getdistrictsbystate (v_state_code IN NUMBER)
      RETURN districts_ref
   IS
      v_cursor   districts_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   dist_code, dist_cou_code, dist_sht_desc, dist_name,
                  dist_sts_code
             FROM tqc_districts
            WHERE dist_sts_code = v_state_code
         ORDER BY dist_name;

      RETURN v_cursor;
   END;

   PROCEDURE getdistricts (
      v_districts_ref   OUT   districts_ref,
      v_cou_code              tqc_districts.dist_cou_code%TYPE
   )
   IS
   BEGIN
      OPEN v_districts_ref FOR
         SELECT dist_code, dist_cou_code, UPPER (dist_sht_desc)
                                                               dist_sht_desc,
                UPPER (dist_name) dist_name, dist_sts_code
           FROM tqc_districts
          WHERE dist_cou_code = NVL (v_cou_code, dist_cou_code);
   END getdistricts;

   FUNCTION getagencyaccounts (agncode tqc_agency_accounts.aga_code%TYPE)
      RETURN agency_accounts_ref
   IS
      v_cursor   agency_accounts_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   aga_code, aga_sht_desc, aga_name, aga_created_by
             FROM tqc_agency_accounts
            WHERE aga_agn_code = NVL (agncode, aga_agn_code)
         ORDER BY aga_name;

      RETURN v_cursor;
   END;

   PROCEDURE get_base_currency (v_currencies_ref OUT currencies_ref)
   IS
   BEGIN
      OPEN v_currencies_ref FOR
         SELECT   DISTINCT cur_code, cur_symbol, cur_desc, cur_rnd, cur_num_word,
                  cur_decimal_word
             FROM tqc_currencies, tqc_organizations
            WHERE org_cur_code = cur_code
         ORDER BY cur_desc ASC;
   END get_base_currency;

   PROCEDURE get_postal_codes (v_locations_ref OUT locations_ref)
   IS
   BEGIN
      OPEN v_locations_ref FOR
         SELECT NULL loc_code, NULL loc_twn_code, pc_code, pc_name,NULL
           FROM gin_postal_codes;
   END get_postal_codes;
   
   FUNCTION  getDDApplicationRmk RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
   BEGIN
        OPEN v_cursor FOR
                SELECT DFR_CODE,DFR_FAILED_REMARK,DFR_APPL_LEVEL
                FROM tqc_dd_failed_remarks;
        RETURN v_cursor;
   
   EXCEPTION 
        WHEN OTHERS THEN
            RAISE_ERROR('Fetching Cursor ..'||SQLERRM(SQLCODE));
    END;
    
    FUNCTION getOrgType(
     v_org_code NUMBER
    )RETURN VARCHAR2
    IS 
       v_org_type VARCHAR2(100):=NULL;
    BEGIN
     BEGIN
     SELECT org_type INTO v_org_type 
     FROM tqc_organizations 
     WHERE org_code=v_org_code;
     EXCEPTION WHEN others THEN
      raise_error('Fetching Org_Type'||SQLERRM);
      END;
      RETURN v_org_type;
    END;
    PROCEDURE get_transfer_details (
        v_transfers_ref OUT transfers_ref, 
        v_trans_type varchar2,
        v_trans_from_code  number,
        v_trans_from_name  varchar2,
        v_trans_to_code  varchar2,
        v_trans_to_name  varchar2,
        v_done_by   varchar2
    )
   IS
    v_cnt number:=0;
    v_tt_code number:=null;
    BEGIN
      -- raise_error('v_trans_from_code: '||v_trans_from_code);
      BEGIN
      SELECT COUNT(1) INTO v_cnt
       FROM tqc_transfers
       WHERE tt_trans_from_code=v_trans_from_code
         AND tt_trans_type=v_trans_type
         AND NVL(tt_authorized,'N') <> 'Y'
         AND tt_trans_type IN('AUT','UAT','ABT','BRT','ROT');
         EXCEPTION WHEN OTHERS THEN
         v_cnt:=0;
      END;
   
      IF v_cnt=0 THEN
       BEGIN
           tqc_setups_pkg.transfer_prc ( 'A', v_tt_code, v_trans_type, sysdate, v_trans_to_code, v_trans_to_name, v_trans_from_code, v_trans_from_name,v_done_by, sysdate, 'N', null, null );
       END;
       
       ELSE
          BEGIN
          SELECT tt_code INTO v_tt_code
       FROM tqc_transfers
      WHERE tt_trans_from_code=v_trans_from_code
         AND tt_trans_type=v_trans_type
         AND NVL(tt_authorized,'N') <> 'Y'
         AND tt_trans_type IN('AUT','UAT','ABT','BRT','ROT');
          END;
         
       END IF;
         
       OPEN v_transfers_ref FOR
       SELECT tt_code  ,
        tt_trans_type ,
        tt_trans_date ,
        tt_trans_to_code  ,
        tt_trans_to_name  ,
        tt_trans_from_code  ,
        tt_trans_from_name  ,
        tt_done_by   ,
        tt_done_date ,
        tt_authorized  , 
        tt_authorized_by ,
        tt_authorized_date      
       FROM tqc_transfers
        where  tt_code = v_tt_code ;
      
   END ;
   
   PROCEDURE get_transfered_items(
        v_transfer_items_ref OUT transfer_items_ref, 
        v_tt_code number
    )
   IS
    v_cnt number:=0;
 BEGIN
      OPEN v_transfer_items_ref FOR
       SELECT 
            tti_code,
            tti_tt_code,
            tti_item_code,
            tti_item_name,
            tti_item_sht_desc,
            tti_item_type 
      FROM tqc_transfers_items,tqc_transfers 
      WHERE tt_code=v_tt_code
       AND NVL(tt_authorized,'N') <> 'Y'
       AND tt_trans_type IN('AUT','UAT','ABT','BRT','ROT')
       AND tti_tt_code=v_tt_code;
 END;
   
    PROCEDURE get_transferable_items(
        v_transfer_xitems_ref OUT transfer_xitems_ref, 
        v_tt_code number
    )
   IS 
    BEGIN 
       OPEN v_transfer_xitems_ref FOR
       SELECT  --_trans_type='ROT' Region transfer
            reg_code ttx_item_code,
            reg_sht_desc ttx_item_sht_desc,
            reg_name ttx_item_name ,
            'REG' ttx_item_type
            FROM tqc_regions,tqc_transfers WHERE
               reg_org_code= tt_trans_from_code
               AND tt_code = v_tt_code
               AND NVL(tt_authorized,'N') <> 'Y'
               AND NOT EXISTS (
                   SELECT  tti_item_code
                     FROM tqc_transfers_items,tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code = tqc_regions.reg_code
                          AND tti_tt_code = v_tt_code
                          AND tt_code = tti_tt_code
                          AND NVL(tt_authorized,'N') <> 'Y'
                          AND tt_trans_type ='ROT'     
                      )
       UNION     
      SELECT  --v_trans_type'BRT' -Branch transfer
            brn_code ttx_item_code,
            brn_sht_desc ttx_item_sht_desc,
            brn_name ttx_item_name ,
            'BRN' ttx_item_type
            FROM tqc_branches,tqc_transfers WHERE
               brn_reg_code= tt_trans_from_code
               AND tt_code = v_tt_code
               AND NVL(tt_authorized,'N') <> 'Y'
               AND NOT EXISTS (
                   SELECT  tti_item_code
                     FROM tqc_transfers_items,tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code = tqc_branches.brn_code
                          AND tti_tt_code = v_tt_code
                          AND tt_code = tti_tt_code
                          AND NVL(tt_authorized,'N') <> 'Y'
                          AND tt_trans_type ='BRT'     
                      )  
      UNION                    
       SELECT  --v_trans_type'ABT' -- Agency transfer
            bra_code ttx_item_code,
            bra_sht_desc ttx_item_sht_desc,
            bra_name ttx_item_name ,
            'AGN' ttx_item_type
            FROM tqc_branch_agencies,tqc_transfers WHERE
               bra_brn_code= tt_trans_from_code
               AND tt_code = v_tt_code
               AND NVL(tt_authorized,'N') <> 'Y'
               AND NOT EXISTS (
                   SELECT tti_item_code
                     FROM tqc_transfers_items,tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code = tqc_branch_agencies.bra_code
                          AND tti_tt_code = v_tt_code
                          AND tt_code = tti_tt_code
                          AND NVL(tt_authorized,'N') <> 'Y'
                          AND tt_trans_type ='ABT'     
                      )         
       UNION 
          SELECT -- v_trans_type 'UAT' Units transfer
            bru_code ttx_item_code,
            bru_sht_desc ttx_item_sht_desc,
            bru_name ttx_item_name ,
            'UNT' ttx_item_type
            FROM tqc_branch_units,tqc_transfers  where
               bru_agn_code=tt_trans_from_code
               AND tt_code = v_tt_code
               AND NVL(tt_authorized,'N') <> 'Y'
              AND NOT EXISTS (
                  SELECT tti_item_code
                    FROM tqc_transfers_items,tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code = tqc_branch_units.bru_code
                          AND tt_code = tti_tt_code
                          AND NVL(tt_authorized,'N') <> 'Y'
                          AND tt_trans_type = 'UAT'
                          AND tti_tt_code = v_tt_code
                     )  
       UNION 
           SELECT   --trans_type 'AUT' Agents Unit transfer
            agn_code ttx_item_code,
            agn_sht_desc ttx_item_sht_desc,
            agn_name ttx_item_name ,
            'AGT' ttx_item_type
            FROM tqc_agencies,tqc_account_types,tqc_transfers where
               agn_bru_code=tt_trans_from_code
               AND agn_act_code = act_code
               AND act_type_sht_desc ='A'
               AND tt_code = v_tt_code
               AND NVL(tt_authorized,'N') <> 'Y'
               AND NOT EXISTS (
                   SELECT tti_item_code
                     FROM tqc_transfers_items,tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code = tqc_agencies.agn_code
                          AND tti_tt_code = v_tt_code
                          AND tt_code = tti_tt_code
                          AND NVL(tt_authorized,'N') <> 'Y'
                          AND tt_trans_type = 'AUT'    
                      );  
 END;
 
PROCEDURE fetch_sbus(v_units_ref OUT units_refrec,v_type VARCHAR2)
   IS
   BEGIN
          
        if v_type='U'   THEN       
           OPEN v_units_ref FOR           
            select a.OSD_NAME COVERAGE_AREA,b.OSD_NAME SPOKE,c.OSD_NAME SBU,a.OSD_ID COVERAGE_CODE,b.OSD_ID SPOKE_CODE,b.OSD_ID SBU_CODE
            from tqc_org_division_levels_type,
            tqc_org_division_levels,
            tqc_org_subdivisions a,
            tqc_org_subdivisions b,
            tqc_org_subdivisions c
            where ODL_DLT_CODE = DLT_CODE
            and  a.OSD_ODL_CODE = ODL_CODE
            and DLT_ACT_CODE = 100
            and ODL_RANKING = 4
            and b.OSD_CODE = a.OSD_PARENT_OSD_CODE
            and c.OSD_CODE = b.OSD_PARENT_OSD_CODE
            and (a.OSD_WET is null or a.OSD_WET > trunc(sysdate))
            and (b.OSD_WET is null or b.OSD_WET > trunc(sysdate))
            and (c.OSD_WET is null or c.OSD_WET > trunc(sysdate));
         
        elsif   v_type='L'   then  
         
         OPEN v_units_ref FOR 

            select  a.OSD_NAME LOCATION_NAME,b.OSD_NAME ORGANIZATION_NAME,b.OSD_NAME ORGANIZATION_NAME,a.OSD_ID LOCATION_CODE,b.OSD_ID ORGANIZATION_CODE,b.OSD_ID ORGANIZATION_CODE
            from tqc_org_division_levels_type,
            tqc_org_division_levels,
            tqc_org_subdivisions a,
            tqc_org_subdivisions b
            where ODL_DLT_CODE = DLT_CODE
            and  a.OSD_ODL_CODE = ODL_CODE
            and DLT_ACT_CODE = 101
            and ODL_RANKING = 2
            and b.OSD_CODE = a.OSD_PARENT_OSD_CODE
            and (a.OSD_WET is null or a.OSD_WET > trunc(sysdate))
            and (b.OSD_WET is null or b.OSD_WET > trunc(sysdate));
         end if;        
   END;
PROCEDURE find_life_subacc_types(
        v_sub_acc_ref OUT sub_accrec
    )
 IS
 BEGIN
    OPEN v_sub_acc_ref FOR 
        SELECT SACT_CODE, SACT_SHT_DESC, SACT_DESCRIPTION
        FROM LMS_SUB_ACCOUNT_TYPES
        WHERE SACT_ACT_CODE = 16;
 END find_life_subacc_types;
     PROCEDURE get_all_occupations (v_sect_code NUMBER, v_sectors_ref OUT sectors_ref)
   IS
   BEGIN
     -- raise_error('v_sect_code: '||v_sect_code);
      OPEN v_sectors_ref FOR
         SELECT   occ_code, occ_sht_desc, occ_name
             FROM tqc_occupations
             WHERE OCC_CODE NOT IN (SELECT OCC_CODE FROM tqc_sector_occupations WHERE OCC_SEC_CODE = v_sect_code )
         ORDER BY occ_name;
   END get_all_occupations;
    PROCEDURE get_sector_occupations (v_sect_code NUMBER, v_sectors_ref OUT sectors_ref)
   IS
   BEGIN
      --raise_error('v_sect_code: '||v_sect_code);
      OPEN v_sectors_ref FOR
          SELECT   occ_code, occ_sht_desc, occ_name
             FROM tqc_occupations
             WHERE OCC_CODE IN (SELECT OCC_CODE FROM tqc_sector_occupations WHERE OCC_SEC_CODE = v_sect_code );
   END get_sector_occupations;
   
     PROCEDURE get_contact_details (
   contact_details   OUT      contact_datails_cursor,
   v_client_code     IN       VARCHAR2,
   v_client_type     IN       VARCHAR2
)
IS
BEGIN
   IF v_client_type = 'U'
   THEN
      OPEN contact_details FOR
         SELECT usr_code code, usr_username names, usr_email email,
                usr_cell_phone_no telephone
           FROM tqc_users
          WHERE usr_code = v_client_code;
   ELSIF v_client_type = 'I'
   THEN
      OPEN contact_details FOR
         SELECT agn_code code, agn_name names, agn_email_address email,
                NVL (agn_tel1, agn_tel2) telephone
           FROM tqc_agencies
          WHERE agn_code = v_client_code;
   ELSIF v_client_type = 'SP'
   THEN
      OPEN contact_details FOR
         SELECT spr_code code, spr_name names, spr_email email,
                NVL2 (spr_phone, spr_mobile_no, spr_sms_number) telephone
           FROM tqc_service_providers
          WHERE spr_code = v_client_code;
   ELSIF v_client_type = 'C'
   THEN
      OPEN contact_details FOR
         SELECT clnt_code code, clnt_name names, clnt_email_addrs email,
                NVL2 (clnt_tel, clnt_tel2, clnt_sms_tel)
           FROM tqc_clients
          WHERE clnt_code = v_client_code;
   END IF;
END;
PROCEDURE getTaxAuthorities(
     v_taxauthorities_ref   OUT  taxauthorities_ref
)
   IS
   BEGIN
      OPEN v_taxauthorities_ref FOR
         SELECT
                AUTH_CODE,
                AUTH_SHT_CODE,
                AUTH_NAME
         FROM   TQC_TAX_AUTH;
   END getTaxAuthorities;
      PROCEDURE getSchengenCountries(v_countries_ref OUT countries_ref,
                         v_schengen      IN VARCHAR2,  v_inbound IN VARCHAR2 DEFAULT NULL) IS
  BEGIN
     if v_inbound = 'INBOUND' THEN
    OPEN v_countries_ref FOR
          SELECT cou_code,
                 TWN_SHT_DESC,
                 TWN_NAME,
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
           FROM tqc_countries, tqc_currencies, TQC_TOWNS
               WHERE cou_base_curr = cur_code(+)
               AND COU_CODE = TWN_COU_CODE
               AND COU_CODE = 1100
               ORDER BY cou_name ASC;
       ELSE
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
             AND (cou_schegen = NVL(v_schengen, 'N') OR  NVL(v_schengen, 'N') = 'N')
           ORDER BY cou_name ASC;
           END IF;
  END getSchengenCountries;
  --16/02/2014
  --20/02/2017
   PROCEDURE get_countries(v_countries_ref OUT countries_ref, v_inbound VARCHAR2 DEFAULT NULL) IS
  BEGIN
    if v_inbound = 'INBOUND' THEN
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
                     TWN_SHT_DESC,
                     TWN_NAME,
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
                FROM tqc_countries, tqc_currencies, TQC_TOWNS
               WHERE cou_base_curr = cur_code(+)
               AND COU_CODE = TWN_COU_CODE
               AND COU_CODE = 1100
               ORDER BY cou_name ASC;
       ELSE
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
       END IF; 
  END get_countries;
  --20/02/2017
  
    FUNCTION getallsystems
      RETURN all_systems_ref
   IS
      v_cursor   all_systems_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   sys_code, sys_sht_desc, sys_name, sys_main_form_name,
                  sys_active, sys_dbase_username, sys_dbase_password,
                  sys_dbase_string, sys_path, sys_org_code,
                  sys_agn_main_frm_name, sys_kba_code, sys_signature_path,
                  sys_template
             FROM tqc_systems
            WHERE sys_active <> 'N' AND sys_code <> 30 AND sys_code <> 26
         ORDER BY sys_name;

      RETURN v_cursor;
   END;
PROCEDURE get_specific_agent (v_agn_code NUMBER, v_name OUT VARCHAR2)
   IS
   BEGIN
      SELECT agn_name
        INTO v_name
        FROM tqc_agencies
       WHERE agn_code = v_agn_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_name := NULL;
   END get_specific_agent;   
PROCEDURE get_specific_unit (v_bru_code NUMBER, v_name OUT VARCHAR2)
   IS
   BEGIN
      SELECT bru_name
        INTO v_name
        FROM tqc_branch_units
       WHERE bru_code = v_bru_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_name := NULL;
   END get_specific_unit;
PROCEDURE get_specific_bizperson (v_bpn_code NUMBER, v_name OUT VARCHAR2)
   IS
   BEGIN
      SELECT bpn_name
        INTO v_name
        FROM tqc_business_persons
       WHERE bpn_code = v_bpn_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_name := NULL;
   END get_specific_bizperson;
PROCEDURE get_specific_sbu (v_sbu_code NUMBER, v_name OUT VARCHAR2)
   IS
   BEGIN
      -- raise_error('v_sbu_code : '||v_sbu_code);
      SELECT c.osd_name
        INTO v_name
        FROM tqc_org_subdivisions b, tqc_org_subdivisions c
       WHERE c.osd_code = b.osd_parent_osd_code AND b.osd_id = v_sbu_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_name := NULL;
   END get_specific_sbu;
PROCEDURE get_specific_bankbranch (v_bbr_code NUMBER, v_name OUT VARCHAR2)
   IS
   BEGIN
      SELECT bbr_branch_name
        INTO v_name
        FROM tqc_bank_branches
       WHERE bbr_code = v_bbr_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_name := NULL;
   END get_specific_bankbranch; 
PROCEDURE provider_contacts_prc (
      v_spr_code                        TQC_SPR_CONTACT_PERSONS.spr_code%TYPE,
      v_provider_contacts_ref   OUT     provider_contacts_ref
   )
   IS
   BEGIN
   
  -- raise_error('code'||v_spr_code );
      OPEN v_provider_contacts_ref FOR
         SELECT  spr_cnt_code,spr_code,spr_cnt_name,spr_cnt_title,spr_cnt_office_tel,
                spr_cnt_home_tel,spr_cnt_email,null
           FROM  TQC_SPR_CONTACT_PERSONS
          WHERE  spr_code = v_spr_code;
          
   END provider_contacts_prc; 
PROCEDURE get_bancassurance_branches (
      v_bbr_code                           tqc_bank_branches.bbr_code%TYPE,
      v_bancassurance_branches_ref   OUT   bancassurance_branches_ref
   )
   IS
   BEGIN
      OPEN v_bancassurance_branches_ref FOR
         SELECT bbb_code, bbb_bbr_code, bbb_branch_name, bbb_remarks,
                bbb_sht_desc, bbb_date_created, bbb_created_by,
                bbb_physical_addrs, bbb_postal_addrs, bbb_kba_code,
                bbb_bnkt_code, bbb_bnkt_sht_desc, bbb_email, bbb_person_name,
                bbb_person_email, bbb_person_cou_code, bbb_person_phone,
                bbb_ter_code
           FROM tqc_bancassurance_branches
          WHERE bbb_bbr_code = v_bbr_code;
   END get_bancassurance_branches; 
FUNCTION getFaAgents
      RETURN getFaAgents_ref
   IS
      v_cursor   getFaAgents_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   fa_agn_org_code,fa_agn_sht_desc,                 
                  fa_team_code,fa_agn_name,fa_agent_code 
           FROM tqc_fa_agents;

      RETURN v_cursor;
   END;                 
END tqc_setups_cursor_15102017;
/