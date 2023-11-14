/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_SETUPS_CURSOR_05042017  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_setups_cursor_05042017
AS
   /******************************************************************************
      NAME:       TQC_SETUPS_CURSOR
      PURPOSE:

      REVISIONS:
      Ver        Date        Author           Description
      ---------  ----------  ---------------  ------------------------------------
      1.0        5/6/2010             1. Created this package.
   ******************************************************************************/
   FUNCTION myfunction (param1 IN NUMBER)
      RETURN NUMBER;

   PROCEDURE myprocedure (param1 IN NUMBER);

   TYPE organizations_rec IS RECORD (
      org_code             tqc_organizations.org_code%TYPE,
      org_sht_desc         tqc_organizations.org_sht_desc%TYPE,
      org_name             tqc_organizations.org_name%TYPE,
      org_addrs            tqc_organizations.org_addrs%TYPE,
      org_twn_code         tqc_organizations.org_twn_code%TYPE,
      org_cou_code         tqc_organizations.org_cou_code%TYPE,
      org_email_addrs      tqc_organizations.org_email_addrs%TYPE,
      org_phy_addrs        tqc_organizations.org_phy_addrs%TYPE,
      org_cur_code         tqc_organizations.org_cur_code%TYPE,
      org_zip              tqc_organizations.org_zip%TYPE,
      org_fax              tqc_organizations.org_fax%TYPE,
      org_tel1             tqc_organizations.org_tel1%TYPE,
      org_tel2             tqc_organizations.org_tel2%TYPE,
      org_rpt_logo         tqc_organizations.org_rpt_logo%TYPE,
      org_motto            tqc_organizations.org_motto%TYPE,
      org_pin_no           tqc_organizations.org_pin_no%TYPE,
      org_ed_code          tqc_organizations.org_ed_code%TYPE,
      org_item_acc_code    tqc_organizations.org_item_acc_code%TYPE,
      org_other_name       tqc_organizations.org_other_name%TYPE,
      org_type             tqc_organizations.org_type%TYPE,
      org_web_brn_code     tqc_organizations.org_web_brn_code%TYPE,
      org_web_addrs        tqc_organizations.org_web_addrs%TYPE,
      org_agn_code         tqc_organizations.org_agn_code%TYPE,
      org_directors        tqc_organizations.org_directors%TYPE,
      org_lang_code        tqc_organizations.org_lang_code%TYPE,
      org_avatar           tqc_organizations.org_avatar%TYPE,
      cur_symbol           VARCHAR2 (15),
      cur_desc             VARCHAR2 (80),
      cou_name             VARCHAR2 (50),
      twn_name             VARCHAR2 (50),
      org_sts_code         tqc_organizations.org_sts_code%TYPE,
      state_name           VARCHAR2 (50),
      org_grp_logo         tqc_organizations.org_grp_logo%TYPE,
      agn_name             tqc_agencies.agn_name%TYPE,
      org_vat_number       tqc_organizations.org_vat_number%TYPE,
      cou_admin_reg_type   tqc_countries.cou_admin_reg_type%TYPE,
      org_mobile1          tqc_organizations.org_mobile1%TYPE,
      org_mobile2          tqc_organizations.org_mobile2%TYPE,
      cou_zip_code         tqc_countries.cou_zip_code%TYPE,
      org_cert_sign         tqc_organizations.org_cert_sign%TYPE
   );

   TYPE organizations_ref IS REF CURSOR
      RETURN organizations_rec;

   PROCEDURE organizations (
      v_organizations_ref   OUT   organizations_ref,
      v_org_code                  tqc_organizations.org_code%TYPE
   );

   TYPE organization_details_rec IS RECORD (
      org_code            tqc_organizations.org_code%TYPE,
      org_sht_desc        tqc_organizations.org_sht_desc%TYPE,
      org_name            tqc_organizations.org_name%TYPE,
      org_addrs           tqc_organizations.org_addrs%TYPE,
      org_twn_code        tqc_organizations.org_twn_code%TYPE,
      org_cou_code        tqc_organizations.org_cou_code%TYPE,
      org_email_addrs     tqc_organizations.org_email_addrs%TYPE,
      org_phy_addrs       tqc_organizations.org_phy_addrs%TYPE,
      org_cur_code        tqc_organizations.org_cur_code%TYPE,
      org_zip             tqc_organizations.org_zip%TYPE,
      org_fax             tqc_organizations.org_fax%TYPE,
      org_tel1            tqc_organizations.org_tel1%TYPE,
      org_tel2            tqc_organizations.org_tel2%TYPE,
      org_rpt_logo        tqc_organizations.org_rpt_logo%TYPE,
      org_motto           tqc_organizations.org_motto%TYPE,
      org_pin_no          tqc_organizations.org_pin_no%TYPE,
      org_ed_code         tqc_organizations.org_ed_code%TYPE,
      org_item_acc_code   tqc_organizations.org_item_acc_code%TYPE,
      org_other_name      tqc_organizations.org_other_name%TYPE,
      org_type            tqc_organizations.org_type%TYPE,
      org_web_brn_code    tqc_organizations.org_web_brn_code%TYPE,
      org_web_addrs       tqc_organizations.org_web_addrs%TYPE,
      org_agn_code        tqc_organizations.org_agn_code%TYPE,
      org_directors       tqc_organizations.org_directors%TYPE,
      org_lang_code       tqc_organizations.org_lang_code%TYPE,
      org_avatar          tqc_organizations.org_avatar%TYPE,
      cur_symbol          VARCHAR2 (15),
      cur_desc            VARCHAR2 (80),
      cou_name            VARCHAR2 (50),
      twn_name            VARCHAR2 (50),
      org_sts_code        tqc_organizations.org_sts_code%TYPE,
      state_name          VARCHAR2 (50),
      org_grp_logo        tqc_organizations.org_grp_logo%TYPE
   );

   TYPE organization_details_ref IS REF CURSOR
      RETURN organization_details_rec;

   PROCEDURE organization_details (
      v_organization_details_ref   OUT   organization_details_ref,
      v_org_code                         tqc_organizations.org_code%TYPE
   );

   TYPE countries_rec IS RECORD (
      cou_code                  tqc_countries.cou_code%TYPE,
      cou_sht_desc              tqc_countries.cou_sht_desc%TYPE,
      cou_name                  tqc_countries.cou_name%TYPE,
      cou_base_curr             tqc_countries.cou_base_curr%TYPE,
      cou_base_curr_desc        tqc_currencies.cur_desc%TYPE,
      cou_nationality           tqc_countries.cou_nationality%TYPE,
      cou_zip_code              tqc_countries.cou_zip_code%TYPE,
      cou_admin_reg_type        tqc_countries.cou_admin_reg_type%TYPE,
      cou_admin_reg_mandatory   tqc_countries.cou_admin_reg_mandatory%TYPE,
      cou_schegen               tqc_countries.cou_schegen%TYPE,
      cou_emb_code              tqc_countries.cou_emb_code%TYPE,
      cou_curr_serial           tqc_countries.cou_curr_serial%TYPE,
      cou_mobile_prefix         tqc_countries.cou_mobile_prefix%TYPE,
      cou_client_number         tqc_countries.cou_client_number%TYPE
   );

   TYPE countries_ref IS REF CURSOR
      RETURN countries_rec;

--   PROCEDURE getcountries (
--        v_countries_ref OUT countries_ref,
--        v_schengen        IN  VARCHAR2,
--        v_search          IN  VARCHAR2
--    );
PROCEDURE getcountries (
   v_countries_ref   OUT   countries_ref,
   v_county_name           VARCHAR2 DEFAULT NULL,
   v_county_id             VARCHAR2 DEFAULT NULL,
   v_county_code           VARCHAR2 DEFAULT NULL
);
   FUNCTION getallcountries
      RETURN countries_ref;

   TYPE countries_and_currency_rec IS RECORD (
      cou_code                  tqc_countries.cou_code%TYPE,
      cou_sht_desc              tqc_countries.cou_sht_desc%TYPE,
      cou_name                  tqc_countries.cou_name%TYPE,
      cou_base_curr             tqc_countries.cou_base_curr%TYPE,
      cou_nationality           tqc_countries.cou_nationality%TYPE,
      cou_zip_code              tqc_countries.cou_zip_code%TYPE,
      cur_symbol                tqc_currencies.cur_symbol%TYPE,
      cur_desc                  tqc_currencies.cur_desc%TYPE,
      cou_admin_reg_type        tqc_countries.cou_admin_reg_type%TYPE,
      cou_admin_reg_mandatory   tqc_countries.cou_admin_reg_mandatory%TYPE
   );

   TYPE countries_and_currency_ref IS REF CURSOR
      RETURN countries_and_currency_rec;

   PROCEDURE get_countries_and_currency (
      v_countries_and_currency_ref   OUT   countries_and_currency_ref
   );

   PROCEDURE get_specific_country (
      v_cou_code              tqc_countries.cou_code%TYPE,
      v_countries_ref   OUT   countries_ref
   );

   TYPE towns_rec IS RECORD (
      twn_code         tqc_towns.twn_code%TYPE,
      twn_cou_code     tqc_towns.twn_cou_code%TYPE,
      twn_sht_desc     tqc_towns.twn_sht_desc%TYPE,
      twn_name         tqc_towns.twn_name%TYPE,
      twn_state_code   tqc_states.sts_code%TYPE,
      pst_desc         tqc_postal_codes.pst_desc%TYPE,
      pst_zip_code     tqc_postal_codes.pst_zip_code%TYPE
   );

   TYPE towns_ref IS REF CURSOR
      RETURN towns_rec;

   PROCEDURE gettowns (
      v_towns_ref   OUT   towns_ref,
      v_cou_code          tqc_towns.twn_cou_code%TYPE
   );

   FUNCTION gettownsbycountry (v_cou_code IN NUMBER)
      RETURN towns_ref;

   FUNCTION gettownsbystate (v_state_code IN NUMBER)
      RETURN towns_ref;

   PROCEDURE get_specific_town (
      v_twn_code          tqc_towns.twn_code%TYPE,
      v_towns_ref   OUT   towns_ref
   );

   TYPE states_rec IS RECORD (
      sts_code       tqc_states.sts_code%TYPE,
      sts_cou_code   tqc_states.sts_cou_code%TYPE,
      sts_sht_desc   tqc_states.sts_sht_desc%TYPE,
      sts_name       tqc_states.sts_name%TYPE
   );

   TYPE states_ref IS REF CURSOR
      RETURN states_rec;

   PROCEDURE getstates (
      v_states_ref   OUT   states_ref,
      v_cou_code           tqc_states.sts_cou_code%TYPE
   );

   TYPE admin_region_rec IS RECORD (
      adm_reg_code       tqc_admin_regions.adm_reg_code%TYPE,
      adm_reg_sht_desc   tqc_admin_regions.adm_reg_sht_desc%TYPE,
      adm_reg_name       tqc_admin_regions.adm_reg_name%TYPE,
      adm_reg_cou_code   tqc_admin_regions.adm_reg_cou_code%TYPE
   );

   TYPE admin_region_ref IS REF CURSOR
      RETURN admin_region_rec;

   PROCEDURE get_admin_regions (
      v_admin_regions_ref   OUT   admin_region_ref,
      v_adm_reg_cou_code          tqc_admin_regions.adm_reg_cou_code%TYPE
   );

   TYPE postal_code_by_town_rec IS RECORD (
      pst_code       tqc_postal_codes.pst_code%TYPE,
      pst_twn_code   tqc_postal_codes.pst_twn_code%TYPE,
      pst_desc       tqc_postal_codes.pst_desc%TYPE,
      pst_zip_code   tqc_postal_codes.pst_zip_code%TYPE
   );

   TYPE postal_code_by_town_ref IS REF CURSOR
      RETURN postal_code_by_town_rec;

   PROCEDURE get_postal_codes_by_town (
      v_pst_twn_code                    tqc_postal_codes.pst_twn_code%TYPE,
      v_postal_code_by_town_ref   OUT   postal_code_by_town_ref
   );
FUNCTION  getDDApplicationRmk RETURN SYS_REFCURSOR;
   FUNCTION getpostalcodesbytown (v_twn_code NUMBER)
      RETURN postal_code_by_town_ref;

   TYPE locations_rec IS RECORD (
      loc_code       tqc_locations.loc_code%TYPE,
      loc_twn_code   tqc_locations.loc_twn_code%TYPE,
      loc_sht_desc   tqc_locations.loc_sht_desc%TYPE,
      loc_name       tqc_locations.loc_name%TYPE,
      loc_landmark   tqc_locations.loc_landmark%TYPE
   );

   TYPE locations_ref IS REF CURSOR
      RETURN locations_rec;

   PROCEDURE get_locations_by_town (
      v_loc_twn_code          tqc_locations.loc_twn_code%TYPE,
      v_locations_ref   OUT   locations_ref
   );

   TYPE currencies_rec IS RECORD (
      cur_code           tqc_currencies.cur_code%TYPE,
      cur_symbol         tqc_currencies.cur_symbol%TYPE,
      cur_desc           tqc_currencies.cur_desc%TYPE,
      cur_rnd            tqc_currencies.cur_rnd%TYPE,
      cur_num_word       tqc_currencies.cur_num_word%TYPE,
      cur_decimal_word   tqc_currencies.cur_decimal_word%TYPE
   );

   TYPE currencies_ref IS REF CURSOR
      RETURN currencies_rec;

   PROCEDURE currencies (v_currencies_ref OUT currencies_ref);

   PROCEDURE get_currencies (
      v_currencies_ref   OUT   currencies_ref,
      v_cur_desc               VARCHAR2
   );

   PROCEDURE get_currencies_exclude_curr (
      v_cur_code               tqc_currencies.cur_code%TYPE,
      v_currencies_ref   OUT   currencies_ref
   );

   TYPE currency_denominations_rec IS RECORD (
      cud_code       tqc_currency_denominations.cud_code%TYPE,
      cud_cur_code   tqc_currency_denominations.cud_cur_code%TYPE,
      cud_value      tqc_currency_denominations.cud_value%TYPE,
      cud_name       tqc_currency_denominations.cud_name%TYPE,
      cud_wef        tqc_currency_denominations.cud_wef%TYPE
   );

   TYPE currency_denominations_ref IS REF CURSOR
      RETURN currency_denominations_rec;

   PROCEDURE get_currency_denominations (
      v_currency_denominations_ref   OUT   currency_denominations_ref
   );

   PROCEDURE get_curr_denominations_by_code (
      v_cud_cur_code                       tqc_currency_denominations.cud_cur_code%TYPE,
      v_currency_denominations_ref   OUT   currency_denominations_ref
   );

   FUNCTION fetch_currency_name (v_cur_code NUMBER)
      RETURN VARCHAR2;

   TYPE currency_rates_rec IS RECORD (
      crt_code            tqc_currency_rates.crt_code%TYPE,
      crt_cur_code        tqc_currency_rates.crt_cur_code%TYPE,
      crt_rate            tqc_currency_rates.crt_rate%TYPE,
      crt_date            tqc_currency_rates.crt_date%TYPE,
      crt_base_cur_code   tqc_currency_rates.crt_base_cur_code%TYPE
   );

   TYPE currency_rates_ref IS REF CURSOR
      RETURN currency_rates_rec;

   PROCEDURE get_currency_rates (v_currency_rates_ref OUT currency_rates_ref);

   TYPE currency_rates_by_curr_rec IS RECORD (
      crt_code            tqc_currency_rates.crt_code%TYPE,
      crt_cur_code        tqc_currency_rates.crt_cur_code%TYPE,
      crt_rate            tqc_currency_rates.crt_rate%TYPE,
      crt_date            tqc_currency_rates.crt_date%TYPE,
      crt_base_cur_code   tqc_currency_rates.crt_base_cur_code%TYPE,
      cur_desc            tqc_currencies.cur_code%TYPE,
      crt_wef             tqc_currency_rates.crt_wef%TYPE,
      crt_wet             tqc_currency_rates.crt_wet%TYPE
   );

   TYPE currency_rates_by_curr_ref IS REF CURSOR
      RETURN currency_rates_by_curr_rec;

   PROCEDURE get_currency_rates_by_currency (
      v_crt_cur_code               tqc_currency_rates.crt_cur_code%TYPE,
      v_currency_rates_ref   OUT   currency_rates_by_curr_ref
   );

   TYPE regions_rec IS RECORD (
      reg_code                  tqc_regions.reg_code%TYPE,
      reg_org_code              tqc_regions.reg_org_code%TYPE,
      reg_sht_desc              tqc_regions.reg_sht_desc%TYPE,
      reg_name                  tqc_regions.reg_name%TYPE,
      reg_wef                   tqc_regions.reg_wef%TYPE,
      reg_wet                   tqc_regions.reg_wet%TYPE,
      reg_agn_code              tqc_regions.reg_agn_code%TYPE,
      reg_post_level            tqc_regions.reg_post_level%TYPE,
      reg_mngr_allowed          tqc_regions.reg_mngr_allowed%TYPE,
      reg_overide_comm_earned   tqc_regions.reg_overide_comm_earned%TYPE,
      agn_name                  tqc_agencies.agn_name%TYPE,
      reg_brn_mngr_seq_no       tqc_regions.reg_brn_mngr_seq_no%TYPE,
      reg_agn_seq_no            tqc_regions.reg_agn_seq_no%TYPE,
      manager                   tqc_agencies.agn_name%TYPE
   );

   TYPE regions_ref IS REF CURSOR
      RETURN regions_rec;
      
 

   PROCEDURE regions (
      v_regions_ref   OUT   regions_ref,
      v_org_code            tqc_regions.reg_org_code%TYPE
   );
   
   TYPE bank_regions_rec IS RECORD (
  BNKR_CODE                     TQC_BANK_REGIONS.BNKR_CODE%TYPE,
  BNKR_ORG_CODE                 TQC_BANK_REGIONS.BNKR_ORG_CODE%TYPE,
  BNKR_SHT_DESC                 TQC_BANK_REGIONS.BNKR_SHT_DESC%TYPE,
  BNKR_NAME                     TQC_BANK_REGIONS.BNKR_NAME%TYPE,
  BNKR_WEF                      TQC_BANK_REGIONS.BNKR_WEF%TYPE,
  BNKR_WET                      TQC_BANK_REGIONS.BNKR_WET%TYPE,
  BNKR_REG_CODE                 TQC_BANK_REGIONS.BNKR_REG_CODE%TYPE,
  BNKR_AGN_CODE                 TQC_BANK_REGIONS.BNKR_AGN_CODE%TYPE,
  manager                       tqc_agencies.agn_name%TYPE
   );

TYPE bank_regions_ref IS REF CURSOR
      RETURN bank_regions_rec;
      
      
   PROCEDURE bank_regions(
      v_bank_regions_ref   OUT   bank_regions_ref
   );

   TYPE branches_rec IS RECORD (
      brn_code          tqc_branches.brn_code%TYPE,
      brn_sht_desc      tqc_branches.brn_sht_desc%TYPE,
      brn_reg_code      tqc_branches.brn_reg_code%TYPE,
      brn_name          tqc_branches.brn_name%TYPE,
      brn_phy_addrs     tqc_branches.brn_phy_addrs%TYPE,
      brn_email_addrs   tqc_branches.brn_email_addrs%TYPE,
      brn_post_addrs    tqc_branches.brn_post_addrs%TYPE,
      brn_twn_code      tqc_branches.brn_twn_code%TYPE,
      brn_cou_code      tqc_branches.brn_cou_code%TYPE,
      brn_contact       tqc_branches.brn_contact%TYPE,
      brn_manager       tqc_branches.brn_manager%TYPE,
      brn_tel           tqc_branches.brn_tel%TYPE,
      brn_fax           tqc_branches.brn_fax%TYPE,
      brn_gen_pol_clm   tqc_branches.brn_gen_pol_clm%TYPE,
      brn_bns_code      tqc_branches.brn_bns_code%TYPE,
      brn_agn_code      tqc_branches.brn_agn_code%TYPE,
      brn_post_level    tqc_branches.brn_post_level%TYPE,
      manager           tqc_agencies.agn_name%TYPE
   );

   TYPE branches_ref IS REF CURSOR
      RETURN branches_rec;

   PROCEDURE branches (
      v_branches_ref   OUT   branches_ref,
      v_reg_code             tqc_branches.brn_reg_code%TYPE
   );

   TYPE get_branches_rec IS RECORD (
      brn_code                  tqc_branches.brn_code%TYPE,
      brn_sht_desc              tqc_branches.brn_sht_desc%TYPE,
      brn_reg_code              tqc_branches.brn_reg_code%TYPE,
      brn_name                  tqc_branches.brn_name%TYPE,
      brn_phy_addrs             tqc_branches.brn_phy_addrs%TYPE,
      brn_email_addrs           tqc_branches.brn_email_addrs%TYPE,
      brn_post_addrs            tqc_branches.brn_post_addrs%TYPE,
      brn_twn_code              tqc_branches.brn_twn_code%TYPE,
      brn_cou_code              tqc_branches.brn_cou_code%TYPE,
      brn_contact               tqc_branches.brn_contact%TYPE,
      brn_manager               tqc_branches.brn_manager%TYPE,
      brn_tel                   tqc_branches.brn_tel%TYPE,
      brn_fax                   tqc_branches.brn_fax%TYPE,
      brn_gen_pol_clm           tqc_branches.brn_gen_pol_clm%TYPE,
      brn_bns_code              tqc_branches.brn_bns_code%TYPE,
      brn_agn_code              tqc_branches.brn_agn_code%TYPE,
      brn_post_level            tqc_branches.brn_post_level%TYPE,
      brn_mngr_allowed          tqc_branches.brn_mngr_allowed%TYPE,
      brn_overide_comm_earned   tqc_branches.brn_overide_comm_earned%TYPE,
      brn_bra_mngr_seq_no       tqc_branches.brn_bra_mngr_seq_no%TYPE,
      brn_agn_seq_no            tqc_branches.brn_agn_seq_no%TYPE,
      brn_pol_seq               tqc_branches.brn_pol_seq%TYPE,
      brn_prop_seq              tqc_branches.brn_prop_seq%TYPE,
      brn_ref                   tqc_branches.brn_ref%TYPE
   );

   TYPE get_branches_ref IS REF CURSOR
      RETURN get_branches_rec;

   PROCEDURE get_branches (
      v_branches_ref   OUT   get_branches_ref,
      v_reg_code             tqc_branches.brn_reg_code%TYPE
   );

   PROCEDURE get_orgbranches (
      v_branches_ref   OUT   get_branches_ref,
      v_orgcode              NUMBER,
      v_regcode              NUMBER DEFAULT NULL
   );

   PROCEDURE get_specific_branch (
      v_brn_code             tqc_branches.brn_code%TYPE,
      v_branches_ref   OUT   branches_ref
   );

   TYPE branches_contacts_rec IS RECORD (
      tbc_code               tqc_branch_contacts.tbc_code%TYPE,
      tbc_name               tqc_branch_contacts.tbc_name%TYPE,
      tbc_designation        tqc_branch_contacts.tbc_designation%TYPE,
      tbc_mobile_number      tqc_branch_contacts.tbc_mobile_number%TYPE,
      tbc_telephone          tqc_branch_contacts.tbc_telephone%TYPE,
      tbc_id_number          tqc_branch_contacts.tbc_id_number%TYPE,
      tbc_physical_address   tqc_branch_contacts.tbc_physical_address%TYPE,
      tbc_email_address      tqc_branch_contacts.tbc_email_address%TYPE
   );

   TYPE branches_contacts_ref IS REF CURSOR
      RETURN branches_contacts_rec;

   FUNCTION getbranchcontacts (v_brncode NUMBER)
      RETURN branches_contacts_ref;

   TYPE branch_agencies_rec IS RECORD (
      bra_code         tqc_branch_agencies.bra_code%TYPE,
      bra_brn_code     tqc_branch_agencies.bra_brn_code%TYPE,
      bra_sht_desc     tqc_branch_agencies.bra_sht_desc%TYPE,
      bra_name         tqc_branch_agencies.bra_name%TYPE,
      bra_status       tqc_branch_agencies.bra_status%TYPE,
      bra_manager      tqc_branch_agencies.bra_manager%TYPE,
      bra_agn_code     tqc_branch_agencies.bra_agn_code%TYPE,
      bra_post_level   tqc_branch_agencies.bra_post_level%TYPE
   );

   TYPE branch_agencies_ref IS REF CURSOR
      RETURN branch_agencies_rec;

   TYPE get_branch_agencies_rec IS RECORD (
      bra_code                  tqc_branch_agencies.bra_code%TYPE,
      bra_brn_code              tqc_branch_agencies.bra_brn_code%TYPE,
      bra_sht_desc              tqc_branch_agencies.bra_sht_desc%TYPE,
      bra_name                  tqc_branch_agencies.bra_name%TYPE,
      bra_status                tqc_branch_agencies.bra_status%TYPE,
      bra_manager               tqc_branch_agencies.bra_manager%TYPE,
      bra_agn_code              tqc_branch_agencies.bra_agn_code%TYPE,
      bra_post_level            tqc_branch_agencies.bra_post_level%TYPE,
      bra_mngr_allowed          tqc_branch_agencies.bra_mngr_allowed%TYPE,
      bra_overide_comm_earned   tqc_branch_agencies.bra_overide_comm_earned%TYPE,
      bra_bru_mngr_seq_no       tqc_branch_agencies.bra_bru_mngr_seq_no%TYPE,
      bra_agn_seq_no            tqc_branch_agencies.bra_agn_seq_no%TYPE,
      bra_pol_seq               tqc_branch_agencies.bra_pol_seq%TYPE,
      bra_prop_seq              tqc_branch_agencies.bra_prop_seq%TYPE,
      bra_unt_sht_desc_prefix   tqc_branch_agencies.bra_unt_sht_desc_prefix%TYPE,
      bra_compt_ov_on_own_buss  tqc_branch_agencies.bra_compt_ov_on_own_buss%TYPE
   );

   TYPE get_branch_agencies_ref IS REF CURSOR
      RETURN get_branch_agencies_rec;

   PROCEDURE get_branch_agencies (
      v_branch_agencies_ref   OUT   get_branch_agencies_ref,
      v_brn_code                    tqc_branch_agencies.bra_brn_code%TYPE
   );

   TYPE branch_units_rec IS RECORD (
      bru_code         tqc_branch_units.bru_code%TYPE,
      bru_brn_code     tqc_branch_units.bru_brn_code%TYPE,
      bru_sht_desc     tqc_branch_units.bru_sht_desc%TYPE,
      bru_name         tqc_branch_units.bru_name%TYPE,
      bru_supervisor   tqc_branch_units.bru_supervisor%TYPE,
      bru_status       tqc_branch_units.bru_status%TYPE,
      bru_agn_code     tqc_branch_units.bru_agn_code%TYPE,
      bru_bra_code     tqc_branch_units.bru_bra_code%TYPE,
      bru_manager      tqc_branch_units.bru_manager%TYPE,
      bru_post_level   tqc_branch_units.bru_post_level%TYPE
   );

   TYPE branch_units_ref IS REF CURSOR
      RETURN branch_units_rec;

   PROCEDURE branch_units (
      v_branch_units_ref   OUT   branch_units_ref,
      v_brn_code                 tqc_branch_units.bru_brn_code%TYPE
   );

   TYPE get_branch_units_rec IS RECORD (
      bru_code                  tqc_branch_units.bru_code%TYPE,
      bru_brn_code              tqc_branch_units.bru_brn_code%TYPE,
      bru_sht_desc              tqc_branch_units.bru_sht_desc%TYPE,
      bru_name                  tqc_branch_units.bru_name%TYPE,
      bru_supervisor            tqc_branch_units.bru_supervisor%TYPE,
      bru_status                tqc_branch_units.bru_status%TYPE,
      bru_agn_code              tqc_branch_units.bru_agn_code%TYPE,
      bru_bra_code              tqc_branch_units.bru_bra_code%TYPE,
      bru_manager               tqc_branch_units.bru_manager%TYPE,
      bru_post_level            tqc_branch_units.bru_post_level%TYPE,
      bru_mngr_allowed          tqc_branch_units.bru_mngr_allowed%TYPE,
      bru_overide_comm_earned   tqc_branch_units.bru_overide_comm_earned%TYPE,
      bru_agn_seq_no            tqc_branch_units.bru_agn_seq_no%TYPE,
      bru_pol_seq               tqc_branch_units.bru_pol_seq%TYPE,
      bru_prop_seq              tqc_branch_units.bru_prop_seq%TYPE
   );

   TYPE get_branch_units_ref IS REF CURSOR
      RETURN get_branch_units_rec;

   PROCEDURE get_branch_units (
      v_branch_units_ref   OUT   get_branch_units_ref,
      v_brn_code                 tqc_branch_units.bru_brn_code%TYPE
   );

   PROCEDURE get_branch_agency_units (
      v_branch_units_ref   OUT   get_branch_units_ref,
      v_bra_code                 tqc_branch_units.bru_bra_code%TYPE,
      v_brn_code                 tqc_branch_units.bru_brn_code%TYPE
   );

   TYPE branch_names_rec IS RECORD (
      bns_code          tqc_branch_names.bns_code%TYPE,
      bns_sht_desc      tqc_branch_names.bns_sht_desc%TYPE,
      bns_name          tqc_branch_names.bns_sht_desc%TYPE,
      bns_phy_addrs     tqc_branch_names.bns_sht_desc%TYPE,
      bns_email_addrs   tqc_branch_names.bns_sht_desc%TYPE,
      bns_post_addrs    tqc_branch_names.bns_sht_desc%TYPE,
      bns_twn_code      tqc_branch_names.bns_sht_desc%TYPE,
      bns_cou_code      tqc_branch_names.bns_sht_desc%TYPE,
      bns_contact       tqc_branch_names.bns_sht_desc%TYPE,
      bns_manager       tqc_branch_names.bns_sht_desc%TYPE,
      bns_tel           tqc_branch_names.bns_sht_desc%TYPE,
      bns_fax           tqc_branch_names.bns_sht_desc%TYPE
   );

   TYPE branch_names_ref IS REF CURSOR
      RETURN branch_names_rec;

   PROCEDURE branch_names (v_branch_names_ref OUT branch_names_ref);

   TYPE agencies_rec IS RECORD (
      agn_code       tqc_agencies.agn_code%TYPE,
      agn_sht_desc   tqc_agencies.agn_sht_desc%TYPE,
      agn_name       tqc_agencies.agn_name%TYPE
   );

   TYPE agencies_ref IS REF CURSOR
      RETURN agencies_rec;

   PROCEDURE agencies (v_agencies_ref OUT agencies_ref);

   TYPE all_agencies_rec IS RECORD (
      agn_code                tqc_agencies.agn_code%TYPE,
      agn_act_code            tqc_agencies.agn_act_code%TYPE,
      agn_sht_desc            tqc_agencies.agn_sht_desc%TYPE,
      agn_name                tqc_agencies.agn_name%TYPE,
      agn_physical_address    tqc_agencies.agn_physical_address%TYPE,
      agn_postal_address      tqc_agencies.agn_postal_address%TYPE,
      agn_twn_code            tqc_agencies.agn_twn_code%TYPE,
      agn_cou_code            tqc_agencies.agn_cou_code%TYPE,
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
      brn_name                tqc_branches.brn_name%TYPE,
      ahc_name                tqc_agency_holding_company.ahc_name%TYPE,
      sec_name                tqc_sectors.sec_name%TYPE,
      agnc_class_desc         tqc_agencies_classes.agnc_class_desc%TYPE
   );

   TYPE all_agencies_ref IS REF CURSOR
      RETURN all_agencies_rec;

   PROCEDURE get_agencies (
      v_agn_act_code       IN       tqc_agencies.agn_act_code%TYPE,
      v_all_agencies_ref   OUT      all_agencies_ref
   );

   TYPE agency_info_rec IS RECORD (
      agn_code                   tqc_agencies.agn_code%TYPE,
      agn_act_code               tqc_agencies.agn_act_code%TYPE,
      agn_sht_desc               tqc_agencies.agn_sht_desc%TYPE,
      agn_name                   tqc_agencies.agn_name%TYPE,
      agn_physical_address       tqc_agencies.agn_physical_address%TYPE,
      agn_postal_address         tqc_agencies.agn_postal_address%TYPE,
      agn_twn_code               tqc_agencies.agn_twn_code%TYPE,
      agn_cou_code               tqc_agencies.agn_cou_code%TYPE,
      agn_email_address          tqc_agencies.agn_email_address%TYPE,
      agn_web_address            tqc_agencies.agn_web_address%TYPE,
      agn_zip                    tqc_agencies.agn_zip%TYPE,
      agn_contact_person         tqc_agencies.agn_contact_person%TYPE,
      agn_contact_title          tqc_agencies.agn_contact_title%TYPE,
      agn_tel1                   tqc_agencies.agn_tel1%TYPE,
      agn_tel2                   tqc_agencies.agn_tel2%TYPE,
      agn_fax                    tqc_agencies.agn_fax%TYPE,
      agn_acc_no                 tqc_agencies.agn_acc_no%TYPE,
      agn_pin                    tqc_agencies.agn_pin%TYPE,
      agn_agent_commission       tqc_agencies.agn_agent_commission%TYPE,
      agn_credit_allowed         tqc_agencies.agn_credit_allowed%TYPE,
      agn_agent_wht_tax          tqc_agencies.agn_agent_wht_tax%TYPE,
      agn_print_dbnote           tqc_agencies.agn_print_dbnote%TYPE,
      agn_status                 tqc_agencies.agn_status%TYPE,
      agn_date_created           tqc_agencies.agn_date_created%TYPE,
      agn_created_by             tqc_agencies.agn_created_by%TYPE,
      agn_reg_code               tqc_agencies.agn_reg_code%TYPE,
      agn_comm_reserve_rate      tqc_agencies.agn_comm_reserve_rate%TYPE,
      agn_annual_budget          tqc_agencies.agn_annual_budget%TYPE,
      agn_status_eff_date        tqc_agencies.agn_status_eff_date%TYPE,
      agn_credit_period          tqc_agencies.agn_credit_period%TYPE,
      agn_comm_stat_eff_dt       tqc_agencies.agn_comm_stat_eff_dt%TYPE,
      agn_comm_status_dt         tqc_agencies.agn_comm_status_dt%TYPE,
      agn_comm_allowed           tqc_agencies.agn_comm_allowed%TYPE,
      agn_checked                tqc_agencies.agn_checked%TYPE,
      agn_checked_by             tqc_agencies.agn_checked_by%TYPE,
      agn_check_date             tqc_agencies.agn_check_date%TYPE,
      agn_comp_comm_arrears      tqc_agencies.agn_comp_comm_arrears%TYPE,
      agn_reinsurer              tqc_agencies.agn_reinsurer%TYPE,
      agn_brn_code               tqc_agencies.agn_brn_code%TYPE,
      agn_town                   tqc_agencies.agn_town%TYPE,
      agn_country                tqc_agencies.agn_country%TYPE,
      agn_status_desc            tqc_agencies.agn_status_desc%TYPE,
      agn_id_no                  tqc_agencies.agn_id_no%TYPE,
      agn_con_code               tqc_agencies.agn_con_code%TYPE,
      agn_agn_code               tqc_agencies.agn_agn_code%TYPE,
      agn_sms_tel                tqc_agencies.agn_sms_tel%TYPE,
      agn_ahc_code               tqc_agencies.agn_ahc_code%TYPE,
      agn_sec_code               tqc_agencies.agn_sec_code%TYPE,
      agn_agnc_class_code        tqc_agencies.agn_agnc_class_code%TYPE,
      agn_expiry_date            tqc_agencies.agn_expiry_date%TYPE,
      agn_license_no             tqc_agencies.agn_license_no%TYPE,
      agn_runoff                 tqc_agencies.agn_runoff%TYPE,
      agn_licensed               tqc_agencies.agn_licensed%TYPE,
      agn_license_grace_pr       tqc_agencies.agn_license_grace_pr%TYPE,
      agn_old_acc_no             tqc_agencies.agn_old_acc_no%TYPE,
      agn_status_remarks         tqc_agencies.agn_status_remarks%TYPE,
      bnk_code                   tqc_banks.bnk_code%TYPE,
      bnk_bank_name              tqc_banks.bnk_bank_name%TYPE,
      bbr_code                   tqc_bank_branches.bbr_code%TYPE,
      bbr_branch_name            tqc_bank_branches.bbr_branch_name%TYPE,
      agn_bank_acc_no            tqc_agencies.agn_bank_acc_no%TYPE,
      agn_unique_prefix          tqc_agencies.agn_unique_prefix%TYPE,
      cou_admin_reg_type         tqc_countries.cou_admin_reg_type%TYPE,
      agn_state_code             tqc_agencies.agn_state_code%TYPE,
      sts_name                   tqc_states.sts_name%TYPE,
      agn_crdt_rting             VARCHAR2 (20),
      clnt_name                  tqc_clients.clnt_name%TYPE,
      accountmanager             VARCHAR2 (200),
      prom_transaction_date      lms_agent_promotion.prom_transaction_date%TYPE,
      prom_transaction_type      lms_agent_promotion.prom_transaction_type%TYPE,
      prom_demo_promo_type       lms_agent_promotion.prom_demo_promo_type%TYPE,
      brn_name                   tqc_branches.brn_name%TYPE,
      bra_name                   tqc_branch_agencies.bra_name%TYPE,
      brn_code                   tqc_branches.brn_code%TYPE,
      bra_unt_sht_desc_prefix    tqc_branch_agencies.bra_unt_sht_desc_prefix%TYPE,
      bru_unt_sht_desc_prefix    tqc_branch_units.bru_unt_sht_desc_prefix%TYPE,
      bru_agn_seq_no             tqc_branch_units.bru_agn_seq_no%TYPE,
      prom_precontract_agn_seq   lms_agent_promotion.prom_precontract_agn_seq%TYPE,
      cou_zip_code               tqc_countries.cou_zip_code%TYPE,
      agn_account_manager        tqc_agencies.agn_account_manager%TYPE,
      agn_credit_limit           tqc_agencies.agn_credit_limit%TYPE,
      bru_code                   tqc_branch_units.bru_code%TYPE,
      bru_name                   tqc_branch_units.bru_name%TYPE,
      agn_local_international    tqc_agencies.agn_local_international%TYPE,
      agn_regulator_number       tqc_agencies.agn_regulator_number%TYPE,
      agn_authorised             tqc_agencies.agn_authorised%TYPE,
      agn_rorg_code              tqc_agencies.agn_rorg_code%TYPE,
      agn_ors_code               tqc_agencies.agn_ors_code%TYPE,
      rorg_desc                  tqc_rating_organizations.rorg_desc%TYPE,
      ors_desc                   tqc_org_rating_starndards.ors_desc%TYPE,
      agn_allocate_cert          tqc_agencies.agn_allocate_cert%TYPE,
      holding_companies          VARCHAR2 (200),
      agn_bounced_chq            tqc_agencies.agn_bounced_chq%TYPE,
      agn_default_comm_mode      tqc_agencies.agn_default_comm_mode%TYPE,
      agn_bpn_code               tqc_agencies.agn_bpn_code%TYPE,
      bpn_name                   tqc_business_persons.bpn_name%TYPE,
      agn_agent_type             tqc_agencies.agn_agent_type%TYPE,
      agn_group                  tqc_agencies.agn_group%TYPE,
      agn_main_agn_code          tqc_agencies.agn_main_agn_code%TYPE,
      main_agency_name           tqc_agencies.agn_name%TYPE,
      agn_vat_applicable         tqc_agencies.agn_vat_applicable%TYPE,
      agn_whtax_applicable       tqc_agencies.agn_whtax_applicable%TYPE,
      agn_tel_pay                tqc_agencies.agn_tel_pay%TYPE,
      agn_payment_freq           tqc_agencies.agn_payment_freq%TYPE,
      agn_default_cpm_mode       tqc_agencies.agn_default_cpm_mode%TYPE,
      agn_cpm_mode_desc          VARCHAR2(50),
      agn_pymt_validated         tqc_agencies.agn_pymt_validated%TYPE,
      agn_comm_levy_app       tqc_agencies.agn_comm_levy_app%TYPE,
      agn_comm_levy_rate      tqc_agencies.agn_comm_levy_rate%TYPE,
      agn_brr_code              tqc_agencies.agn_brr_code%TYPE,
      agn_brr_name             tqc_agencies.agn_brr_name%TYPE,
      agn_auth_name            tqc_agencies.agn_auth_name%TYPE
   );

   TYPE agency_info_ref IS REF CURSOR
      RETURN agency_info_rec;

   TYPE agency_info_by_name_rec IS RECORD (
      agn_code                tqc_agencies.agn_code%TYPE,
      agn_act_code            tqc_agencies.agn_act_code%TYPE,
      agn_sht_desc            tqc_agencies.agn_sht_desc%TYPE,
      agn_name                tqc_agencies.agn_name%TYPE,
      agn_physical_address    tqc_agencies.agn_physical_address%TYPE,
      agn_postal_address      tqc_agencies.agn_postal_address%TYPE,
      agn_twn_code            tqc_agencies.agn_twn_code%TYPE,
      agn_cou_code            tqc_agencies.agn_cou_code%TYPE,
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
      brn_name                tqc_branches.brn_name%TYPE
   );

   TYPE agency_info_by_name_ref IS REF CURSOR
      RETURN agency_info_by_name_rec;

   PROCEDURE get_agency_info (
      v_agn_act_code      IN       tqc_agencies.agn_act_code%TYPE,
      v_agency_info_ref   OUT      agency_info_ref
   );

   PROCEDURE get_agency_infobynames (
      v_agn_act_code           IN       tqc_agencies.agn_act_code%TYPE,
      v_agn_name                        tqc_agencies.agn_name%TYPE,
      v_agency_info_ref        OUT      agency_info_by_name_ref,
      v_agn_physical_address   IN       VARCHAR2,
      v_agn_email_address      IN       VARCHAR2,
      v_agn_pin                IN       VARCHAR2,
      v_agn_tel1               IN       VARCHAR2
   );

   TYPE managers_rec IS RECORD (
      agn_code       tqc_agencies.agn_code%TYPE,
      agn_sht_desc   tqc_agencies.agn_sht_desc%TYPE,
      agn_name       tqc_agencies.agn_name%TYPE,
      twn_name       tqc_towns.twn_name%TYPE
   );

   TYPE managers_ref IS REF CURSOR
      RETURN managers_rec;

   PROCEDURE managers (v_managers_ref OUT managers_ref);

   PROCEDURE agencymanagers (v_managers_ref OUT managers_ref);

   PROCEDURE unitmanagers (v_managers_ref OUT managers_ref);

   TYPE tqc_users_rec IS RECORD (
      v_usr_username   tqc_users.usr_username%TYPE,
      v_usr_name       tqc_users.usr_name%TYPE,
      v_usr_code       tqc_users.usr_code%TYPE
   );

   TYPE tqc_users_cursor IS REF CURSOR
      RETURN tqc_users_rec;

   PROCEDURE find_tqc_users (v_cursor OUT tqc_users_cursor);

   TYPE agency_holding_company_rec IS RECORD (
      v_ahc_code             tqc_agency_holding_company.ahc_code%TYPE,
      v_ahc_name             tqc_agency_holding_company.ahc_name%TYPE,
      ahc_postal_address     tqc_agency_holding_company.ahc_postal_address%TYPE,
      ahc_physical_address   tqc_agency_holding_company.ahc_physical_address%TYPE,
      ahc_telephone_number   tqc_agency_holding_company.ahc_telephone_number%TYPE,
      ahc_mobile_number      tqc_agency_holding_company.ahc_mobile_number%TYPE,
      ahc_contact_person     tqc_agency_holding_company.ahc_contact_person%TYPE
   );

   TYPE agency_holding_company_ref IS REF CURSOR
      RETURN agency_holding_company_rec;

   PROCEDURE get_agency_holding_company (
      v_agency_holding_company_ref   OUT   agency_holding_company_ref
   );

   PROCEDURE get_specific_holding_company (
      v_ahc_code                           tqc_agency_holding_company.ahc_code%TYPE,
      v_agency_holding_company_ref   OUT   agency_holding_company_ref
   );

   TYPE banks_rec IS RECORD (
      v_bnk_code                  tqc_banks.bnk_code%TYPE,
      v_bnk_bank_name             tqc_banks.bnk_bank_name%TYPE,
      v_bnk_remarks               tqc_banks.bnk_remarks%TYPE,
      v_bnk_sht_desc              tqc_banks.bnk_sht_desc%TYPE,
      v_bnk_ddr_code              tqc_banks.bnk_ddr_code%TYPE,
      v_dd_format_desc            tqc_banks.dd_format_desc%TYPE,
      v_bnk_forwarding_bnk_code   tqc_banks.bnk_forwarding_bnk_code%TYPE,
      v_bnk_kba_code              tqc_banks.bnk_kba_code%TYPE,
      fwd_bank_name               VARCHAR2 (100 BYTE),
      dd_report_format            VARCHAR2 (100 BYTE),
      bnk_eft_supported           tqc_banks.bnk_eft_supported%TYPE,
      bnk_class_type              tqc_banks.bnk_class_type%TYPE,
      bnk_accnt_digit_no          tqc_banks.bnk_accnt_digit_no%TYPE,
      bnk_negotiated_bank         tqc_banks.bnk_negotiated_bank%TYPE,
      bnk_administration_charge   tqc_banks.bnk_administration_charge%TYPE,
      bnk_logo                    tqc_banks.bnk_logo%TYPE,
      bnk_wef                     tqc_banks.bnk_wef%TYPE,
      bnk_wet                     tqc_banks.bnk_wet%TYPE,
      bnk_status                  tqc_banks.bnk_status%TYPE,
      bnk_acc_max_no              tqc_banks.bnk_acc_max_no%TYPE,
      bnk_acc_min_no              tqc_banks.bnk_acc_min_no%TYPE,
      bnk_cou_code                tqc_banks.bnk_cou_code%TYPE,
      cou_name                    tqc_countries.cou_name%TYPE
   );

   TYPE banks_ref IS REF CURSOR
      RETURN banks_rec;

   PROCEDURE get_banks (v_banks_ref OUT banks_ref);

   TYPE bank_branches_rec IS RECORD (
      v_bbr_code             tqc_bank_branches.bbr_code%TYPE,
      v_bbr_bnk_code         tqc_bank_branches.bbr_bnk_code%TYPE,
      v_bbr_branch_name      tqc_bank_branches.bbr_branch_name%TYPE,
      v_bbr_remarks          tqc_bank_branches.bbr_remarks%TYPE,
      v_bbr_sht_desc         tqc_bank_branches.bbr_sht_desc%TYPE,
      v_bbr_ref_code         tqc_bank_branches.bbr_ref_code%TYPE,
      v_bbr_eft_supported    tqc_bank_branches.bbr_eft_supported%TYPE,
      v_bbr_dd_supported     tqc_bank_branches.bbr_dd_supported%TYPE,
      v_bbr_date_created     tqc_bank_branches.bbr_date_created%TYPE,
      v_bbr_created_by       tqc_bank_branches.bbr_created_by%TYPE,
      v_bbr_physical_addrs   tqc_bank_branches.bbr_physical_addrs%TYPE,
      v_bbr_postal_addrs     tqc_bank_branches.bbr_postal_addrs%TYPE,
      v_bbr_kba_code         tqc_bank_branches.bbr_kba_code%TYPE,
      v_bbr_bnkt_code        tqc_bank_branches.bbr_bnkt_code%TYPE,
      v_bbr_bnkt_sht_desc    tqc_bank_branches.bbr_bnkt_sht_desc%TYPE
   );

   TYPE bank_branches_ref IS REF CURSOR
      RETURN bank_branches_rec;

   PROCEDURE get_bank_branches (v_bank_branches_ref OUT bank_branches_ref);

   PROCEDURE get_bank_branches_by_bank_code (
      v_bbr_bnk_code              tqc_bank_branches.bbr_bnk_code%TYPE,
      v_bank_branches_ref   OUT   bank_branches_ref
   );

   TYPE bank_territories_rec IS RECORD (
      bnkt_code             tqc_bank_territories.bnkt_code%TYPE,
      bnkt_territory_name   tqc_bank_territories.bnkt_territory_name%TYPE,
      bnkt_sht_desc         tqc_bank_territories.bnkt_sht_desc%TYPE,
      bnkt_bnk_code         tqc_bank_territories.bnkt_bnk_code%TYPE
   );

   TYPE bank_territories_ref IS REF CURSOR
      RETURN bank_territories_rec;


   PROCEDURE get_bank_territs_by_bank_code (
      v_bnkt_bnk_code             tqc_bank_territories.bnkt_bnk_code%TYPE,
      v_bank_territories_ref   OUT   bank_territories_ref
   );




   TYPE agency_classes_rec IS RECORD (
      v_agnc_class_code   tqc_agencies_classes.agnc_class_code%TYPE,
      v_agnc_class_desc   tqc_agencies_classes.agnc_class_desc%TYPE
   );

   TYPE agency_classes_ref IS REF CURSOR
      RETURN agency_classes_rec;

   PROCEDURE get_agency_classes (v_agency_classes_ref OUT agency_classes_ref);

   PROCEDURE get_specific_agency_classes (
      v_agnc_class_code            tqc_agencies_classes.agnc_class_code%TYPE,
      v_agency_classes_ref   OUT   agency_classes_ref
   );

   TYPE tqc_account_types_values IS RECORD (
      act_code                 tqc_account_types.act_code%TYPE,
      act_account_type         tqc_account_types.act_account_type%TYPE,
      act_type_sht_desc        tqc_account_types.act_type_sht_desc%TYPE,
      act_wthtx_rate           tqc_account_types.act_wthtx_rate%TYPE,
      act_type_id              tqc_account_types.act_type_id%TYPE,
      act_comm_reserve_rate    tqc_account_types.act_comm_reserve_rate%TYPE,
      act_max_adv_amt          tqc_account_types.act_max_adv_amt%TYPE,
      act_max_adv_rpymt_prd    tqc_account_types.act_max_adv_rpymt_prd%TYPE,
      act_rcpts_include_comm   tqc_account_types.act_rcpts_include_comm%TYPE,
      act_overide_rate         tqc_account_types.act_overide_rate%TYPE,
      act_id_serial_format     tqc_account_types.act_id_serial_format%TYPE,
      act_vat_rate             tqc_account_types.act_vat_rate%TYPE,
      act_format               tqc_account_types.act_format%TYPE,
      act_odl_code             tqc_account_types.act_odl_code%TYPE,
      ranking                  VARCHAR2(200),
      act_no_gen_code          tqc_account_types.act_no_gen_code%TYPE,
      act_commision_levy_rate tqc_account_types.act_commision_levy_rate%TYPE
   );

   TYPE tqc_account_types_cursor IS REF CURSOR
      RETURN tqc_account_types_values;

   PROCEDURE search_account_types (
      v_act_code                 IN       NUMBER DEFAULT NULL,
      tqc_account_types_values   OUT      tqc_account_types_cursor
   );

   TYPE branch_record IS RECORD (
      branch_name   VARCHAR2 (8),
      branch_code   VARCHAR2 (100)
   );

   TYPE branch_record_cursor IS REF CURSOR
      RETURN branch_record;

   PROCEDURE find_branches_code (branches OUT branch_record_cursor);
   
   TYPE region_record IS RECORD (
      region_name   VARCHAR2 (8),
      region_code   VARCHAR2 (100)
   );

   TYPE region_record_cursor IS REF CURSOR
      RETURN region_record;
      
   PROCEDURE find_regions(regions OUT region_record_cursor);

   TYPE sectors_rec IS RECORD (
      v_sec_code       tqc_sectors.sec_code%TYPE,
      v_sec_sht_desc   tqc_sectors.sec_sht_desc%TYPE,
      v_sec_name       tqc_sectors.sec_name%TYPE
   );

   TYPE sectors_ref IS REF CURSOR
      RETURN sectors_rec;

   PROCEDURE get_sectors (v_sectors_ref OUT sectors_ref);

   PROCEDURE get_specific_sectors (
      v_sec_code            tqc_sectors.sec_code%TYPE,
      v_sectors_ref   OUT   sectors_ref
   );

   TYPE service_provider_types_rec IS RECORD (
      v_spt_code        tqc_service_provider_types.spt_code%TYPE,
      v_spt_sht_desc    tqc_service_provider_types.spt_sht_desc%TYPE,
      v_spt_name        tqc_service_provider_types.spt_name%TYPE,
      v_spt_status      tqc_service_provider_types.spt_code%TYPE,
      v_spt_whtx_rate   tqc_service_provider_types.spt_whtx_rate%TYPE,
      v_spt_vat_rate    tqc_service_provider_types.spt_vat_rate%TYPE
   );

   TYPE service_provider_types_ref IS REF CURSOR
      RETURN service_provider_types_rec;

   TYPE serv_provider_types_act_rec IS RECORD (
      spta_code                 tqc_serv_prv_type_actvts.spta_code%TYPE,
      spta_spt_code             tqc_serv_prv_type_actvts.spta_spt_code%TYPE,
      spta_sht_desc             tqc_serv_prv_type_actvts.spta_sht_desc%TYPE,
      spta_desc                 tqc_serv_prv_type_actvts.spta_desc%TYPE,
      msgt_msg                  tqc_msg_templates.msgt_msg%TYPE,
      emailtemplate             VARCHAR2 (200),
      spta_send_msg_default     tqc_serv_prv_type_actvts.spta_send_msg_default%TYPE,
      spta_send_email_default   tqc_serv_prv_type_actvts.spta_send_email_default%TYPE,
      sms_code                  NUMBER,
      email_code                NUMBER,
      v_reportDays              tqc_serv_prv_type_actvts.spta_report_days%TYPE
   );

   TYPE serv_provider_types_act_ref IS REF CURSOR
      RETURN serv_provider_types_act_rec;

   PROCEDURE get_service_provider_types_act (
      v_spt_code                     IN       NUMBER,
      v_service_provider_types_ref   OUT      serv_provider_types_act_ref
   );

   PROCEDURE get_service_provider_types (
      v_service_provider_types_ref   OUT   service_provider_types_ref
   );

   TYPE service_providers_rec IS RECORD (
      v_spr_code               tqc_service_providers.spr_code%TYPE,
      v_spr_sht_desc           tqc_service_providers.spr_sht_desc%TYPE,
      v_spr_name               tqc_service_providers.spr_name%TYPE,
      v_spr_physical_address   tqc_service_providers.spr_physical_address%TYPE,
      v_spr_postal_address     tqc_service_providers.spr_postal_address%TYPE,
      v_spr_twn_code           tqc_service_providers.spr_twn_code%TYPE,
      v_spr_cou_code           tqc_service_providers.spr_cou_code%TYPE,
      v_spr_spt_code           tqc_service_providers.spr_spt_code%TYPE,
      v_spr_phone              tqc_service_providers.spr_phone%TYPE,
      v_spr_fax                tqc_service_providers.spr_fax%TYPE,
      v_spr_email              tqc_service_providers.spr_email%TYPE,
      v_spr_title              tqc_service_providers.spr_title%TYPE,
      v_spr_zip                tqc_service_providers.spr_zip%TYPE,
      v_spr_wef                tqc_service_providers.spr_wef%TYPE,
      v_spr_wet                tqc_service_providers.spr_wet%TYPE,
      v_spr_contact            tqc_service_providers.spr_contact%TYPE,
      v_spr_aims_code          tqc_service_providers.spr_aims_code%TYPE,
      v_spr_bbr_code           tqc_service_providers.spr_bbr_code%TYPE,
      v_spr_bank_acc_no        tqc_service_providers.spr_bank_acc_no%TYPE,
      v_spr_created_by         tqc_service_providers.spr_created_by%TYPE,
      v_spr_date_created       tqc_service_providers.spr_date_created%TYPE,
      v_spr_status_remarks     tqc_service_providers.spr_status_remarks%TYPE,
      v_spr_status             tqc_service_providers.spr_status%TYPE,
      v_spr_pin_number         tqc_service_providers.spr_pin_number%TYPE,
      v_spr_trs_occupation     tqc_service_providers.spr_trs_occupation%TYPE,
      v_spr_proff_body         tqc_service_providers.spr_proff_body%TYPE,
      v_spr_pin                tqc_service_providers.spr_pin%TYPE,
      v_spr_doc_phone          tqc_service_providers.spr_doc_phone%TYPE,
      v_spr_doc_email          tqc_service_providers.spr_doc_email%TYPE,
      country_name             VARCHAR2 (50),
      town_name                VARCHAR2 (50),
      bank_code                NUMBER,
      bank_name                VARCHAR2 (100),
      branch_name              VARCHAR2 (100),
      spr_inhouse              tqc_service_providers.spr_inhouse%TYPE,
      spr_sms_number           tqc_service_providers.spr_sms_number%TYPE,
      spr_invoice_number       tqc_service_providers.spr_invoice_number%TYPE,
      spr_clnt_code            tqc_service_providers.spr_clnt_code%TYPE,
      clnt_name                tqc_clients.clnt_name%TYPE,
      spr_bpn_code             tqc_service_providers.spr_bpn_code%TYPE,
      bpn_name                 tqc_business_persons.bpn_name%TYPE,
      spr_cont_person_phone    tqc_service_providers.spr_cont_person_phone%TYPE,
      spr_contact_person       tqc_service_providers.spr_contact_person%TYPE,
      spr_contact_email        tqc_service_providers.spr_contact_email%TYPE,
      spr_contact_tel          tqc_service_providers.spr_contact_tel%TYPE,
      spr_tel_pay              tqc_service_providers.spr_tel_pay%TYPE,
      cou_zip_code             tqc_countries.cou_zip_code%TYPE
   );

   TYPE service_providers_ref IS REF CURSOR
      RETURN service_providers_rec;

   PROCEDURE get_service_providers (
      v_spr_spt_code                  tqc_service_providers.spr_spt_code%TYPE,
      v_service_providers_ref   OUT   service_providers_ref
   );

   TYPE service_prov_activities_rec IS RECORD (
      v_spa_code           tqc_serv_prv_activities.spa_code%TYPE,
      v_spa_spt_code       tqc_serv_prv_activities.spa_spt_code%TYPE,
      v_spa_spt_sht_desc   tqc_serv_prv_activities.spa_spt_sht_desc%TYPE,
      v_spa_spr_code       tqc_serv_prv_activities.spa_spr_code%TYPE,
      v_spa_spr_sht_desc   tqc_serv_prv_activities.spa_spr_sht_desc%TYPE,
      v_spt_main_act       tqc_serv_prv_activities.spt_main_act%TYPE,
      v_spa_desc           tqc_serv_prv_activities.spa_desc%TYPE
   );

   TYPE service_prov_activities_ref IS REF CURSOR
      RETURN service_prov_activities_rec;

   PROCEDURE get_service_prov_activities (
      v_service_prov_activities_ref   OUT   service_prov_activities_ref
   );

   TYPE activities_by_provider_rec IS RECORD (
      v_spa_code           tqc_serv_prv_activities.spa_code%TYPE,
      v_spa_spt_code       tqc_serv_prv_activities.spa_spt_code%TYPE,
      v_spa_spt_sht_desc   tqc_serv_prv_activities.spa_spt_sht_desc%TYPE,
      v_spa_spr_code       tqc_serv_prv_activities.spa_spr_code%TYPE,
      v_spa_spr_sht_desc   tqc_serv_prv_activities.spa_spr_sht_desc%TYPE,
      v_spt_main_act       tqc_serv_prv_activities.spt_main_act%TYPE,
      v_spa_desc           tqc_serv_prv_activities.spa_desc%TYPE,
      v_spt_name           tqc_service_provider_types.spt_name%TYPE,
      v_spr_name           tqc_service_providers.spr_name%TYPE
   );

   TYPE activities_by_provider_ref IS REF CURSOR
      RETURN activities_by_provider_rec;

   PROCEDURE get_activities_by_provider (
      v_spa_spr_code                       tqc_serv_prv_activities.spa_spr_code%TYPE,
      v_activities_by_provider_ref   OUT   activities_by_provider_ref
   );

   TYPE service_prov_systems_rec IS RECORD (
      v_sps_code         tqc_service_provider_systems.sps_code%TYPE,
      v_sps_spr_code     tqc_service_provider_systems.sps_spr_code%TYPE,
      v_sps_sys_code     tqc_service_provider_systems.sps_sys_code%TYPE,
      v_sps_wef          tqc_service_provider_systems.sps_wef%TYPE,
      v_sps_wet          tqc_service_provider_systems.sps_wet%TYPE,
      v_sps_created_by   tqc_service_provider_systems.sps_created_by%TYPE
   );

   TYPE service_prov_systems_ref IS REF CURSOR
      RETURN service_prov_systems_rec;

   PROCEDURE get_service_prov_systems (
      v_service_prov_systems_ref   OUT   service_prov_systems_ref
   );

   TYPE systems_rec IS RECORD (
      v_sys_code                tqc_systems.sys_code%TYPE,
      v_sys_sht_desc            tqc_systems.sys_sht_desc%TYPE,
      v_sys_name                tqc_systems.sys_name%TYPE,
      v_sys_main_form_name      tqc_systems.sys_main_form_name%TYPE,
      v_sys_active              tqc_systems.sys_active%TYPE,
      v_sys_dbase_username      tqc_systems.sys_dbase_username%TYPE,
      v_sys_dbase_password      tqc_systems.sys_dbase_password%TYPE,
      v_sys_dbase_string        tqc_systems.sys_dbase_string%TYPE,
      v_sys_path                tqc_systems.sys_path%TYPE,
      v_sys_org_code            tqc_systems.sys_org_code%TYPE,
      v_sys_agn_main_frm_name   tqc_systems.sys_agn_main_frm_name%TYPE,
      v_sys_kba_code            tqc_systems.sys_kba_code%TYPE,
      v_sys_signature_path      tqc_systems.sys_signature_path%TYPE
   );

   TYPE systems_ref IS REF CURSOR
      RETURN systems_rec;

   PROCEDURE get_systems (v_systems_ref OUT systems_ref);

   PROCEDURE get_prov_unassigned_systems (
      v_provider_code                    tqc_service_providers.spr_code%TYPE,
      v_service_prov_systems_ref   OUT   systems_ref
   );

   PROCEDURE get_prov_assigned_systems (
      v_provider_code                    tqc_service_providers.spr_code%TYPE,
      v_service_prov_systems_ref   OUT   systems_ref
   );

   TYPE parameters_rec IS RECORD (
      v_param_code     tqc_parameters.param_code%TYPE,
      v_param_name     tqc_parameters.param_name%TYPE,
      v_param_value    tqc_parameters.param_value%TYPE,
      v_param_status   tqc_parameters.param_status%TYPE,
      v_param_desc     tqc_parameters.param_desc%TYPE
   );

   TYPE parameters_ref IS REF CURSOR
      RETURN parameters_rec;

   PROCEDURE get_parameters (v_parameters_ref OUT parameters_ref);

   PROCEDURE get_gis_branches (branches OUT branch_record_cursor);

   TYPE payment_modes_rec IS RECORD (
      v_pmod_code       tqc_payment_modes.pmod_code%TYPE,
      v_pmod_sht_desc   tqc_payment_modes.pmod_sht_desc%TYPE,
      v_pmod_desc       tqc_payment_modes.pmod_desc%TYPE,
      v_pmod_naration   tqc_payment_modes.pmod_naration%TYPE,
      v_pmod_default    tqc_payment_modes.pmod_default%TYPE
   );

   TYPE payment_modes_ref IS REF CURSOR
      RETURN payment_modes_rec;

   PROCEDURE get_payment_modes (v_payment_modes_ref OUT payment_modes_ref);

   TYPE client_titles_rec IS RECORD (
      v_clt_code       tqc_client_titles.clt_code%TYPE,
      v_clt_sht_desc   tqc_client_titles.clt_sht_desc%TYPE,
      v_clnt_desc      tqc_client_titles.clnt_desc%TYPE
   );

   TYPE client_titles_ref IS REF CURSOR
      RETURN client_titles_rec;

   PROCEDURE get_client_titles (
      v_client_titles_ref   OUT      client_titles_ref,
      v_clt_sht_desc        IN       VARCHAR2
   );

   PROCEDURE find_certificate_alloc_branch (
      v_agn_code       IN       NUMBER,
      v_branches_ref   OUT      branches_ref,
      v_error          OUT      VARCHAR2
   );

   TYPE auth_levels_rec IS RECORD (
      tqal_code         tqc_authorization_levels.tqal_code%TYPE,
      tqal_sprsa_code   tqc_authorization_levels.tqal_sprsa_code%TYPE,
      tqal_level_id     tqc_authorization_levels.tqal_level_id%TYPE,
      tqal_srls_code    tqc_authorization_levels.tqal_srls_code%TYPE,
      srls_name         tqc_sys_roles.srls_name%TYPE,
      sprsa_sub_area    tqc_sys_process_sub_areas.sprsa_sub_area%TYPE
   );

   TYPE auth_levels_ref IS REF CURSOR
      RETURN auth_levels_rec;

   FUNCTION getsubarealevels (
      v_sprsacode   tqc_authorization_levels.tqal_sprsa_code%TYPE
   )
      RETURN auth_levels_ref;

   TYPE system_subareas_rec IS RECORD (
      sprsa_code               tqc_sys_process_sub_areas.sprsa_code%TYPE,
      sprsa_sprca_code         tqc_sys_process_sub_areas.sprsa_sprca_code%TYPE,
      sprsa_sprc_code          tqc_sys_process_sub_areas.sprsa_sprc_code%TYPE,
      sprsa_sub_area           tqc_sys_process_sub_areas.sprsa_sub_area%TYPE,
      sprsa_type               tqc_sys_process_sub_areas.sprsa_type%TYPE,
      sprsa_sht_desc           tqc_sys_process_sub_areas.sprsa_sht_desc%TYPE,
      sprsa_default_usr_code   tqc_sys_process_sub_areas.sprsa_default_usr_code%TYPE
   );

   TYPE system_subareas_ref IS REF CURSOR
      RETURN system_subareas_rec;

   FUNCTION getsystemsubareas (v_syscode NUMBER)
      RETURN system_subareas_ref;

   TYPE systemroles_rec IS RECORD (
      srl_code       tqc_system_roles.srl_code%TYPE,
      srl_sys_code   tqc_system_roles.srl_sys_code%TYPE,
      srl_name       tqc_system_roles.srl_name%TYPE,
      srl_crt_date   tqc_system_roles.srl_crt_date%TYPE,
      srl_sht_desc   tqc_system_roles.srl_sht_desc%TYPE,
      srl_status     tqc_system_roles.srl_status%TYPE
   );

   TYPE systemroles_ref IS REF CURSOR
      RETURN systemroles_rec;

   FUNCTION getsystemroles (v_syscode NUMBER)
      RETURN systemroles_ref;

   PROCEDURE authlevelsproc (
      v_addedit     VARCHAR2,
      v_tqualcode   tqc_authorization_levels.tqal_code%TYPE,
      v_sprsacode   tqc_authorization_levels.tqal_sprsa_code%TYPE,
      v_levelid     tqc_authorization_levels.tqal_level_id%TYPE,
      v_srlscode    tqc_authorization_levels.tqal_srls_code%TYPE
   );

   TYPE bankbrancheslov_rec IS RECORD (
      bnk_sht_desc      tqc_banks.bnk_sht_desc%TYPE,
      bnk_code          tqc_banks.bnk_code%TYPE,
      bnk_bank_name     tqc_banks.bnk_bank_name%TYPE,
      bbr_branch_name   tqc_bank_branches.bbr_branch_name%TYPE,
      bbr_code          tqc_bank_branches.bbr_code%TYPE,
      bct_no            fms_bnk_accts.bct_no%TYPE
   );

   TYPE bankbrancheslov_ref IS REF CURSOR
      RETURN bankbrancheslov_rec;

   FUNCTION getbankbrancheslov
      RETURN bankbrancheslov_ref;

   FUNCTION bankbrancheslov
      RETURN bankbrancheslov_ref;

   FUNCTION bbranchesbank (v_bnkcode tqc_banks.bnk_code%TYPE)
      RETURN bankbrancheslov_ref;

   TYPE holidays_rec IS RECORD (
      hld_date   tqc_holidays.hld_date%TYPE,
      hld_desc   tqc_holidays.hld_desc%TYPE
   );

   TYPE holidays_ref IS REF CURSOR
      RETURN holidays_rec;

   FUNCTION getholidays (v_year NUMBER)
      RETURN holidays_ref;

   TYPE yearslov_rec IS RECORD (
      h_year   NUMBER (8)
   );

   TYPE yearslov_ref IS REF CURSOR
      RETURN yearslov_rec;

   FUNCTION getyearslov
      RETURN yearslov_ref;

   TYPE directdebit_rec IS RECORD (
      dd_code         tqc_direct_debit.dd_code%TYPE,
      dd_ref_no       tqc_direct_debit.dd_ref_no%TYPE,
      dd_sent_yn      tqc_direct_debit.dd_sent_yn%TYPE,
      dd_acnt_no      tqc_direct_debit.dd_acnt_no%TYPE,
      dd_book_date    tqc_direct_debit.dd_book_date%TYPE,
      dd_bbr_code     tqc_direct_debit.dd_bbr_code%TYPE,
      dd_status       tqc_direct_debit.dd_status%TYPE,
      dd_receipted    tqc_direct_debit.dd_receipted%TYPE,
      dd_value_date   tqc_direct_debit.dd_value_date%TYPE,
      dd_raised_by    tqc_direct_debit.dd_raised_by%TYPE,
      dd_date         tqc_direct_debit.dd_date%TYPE,
      dd_bnk_code     tqc_direct_debit.dd_bnk_code%TYPE,
      dd_auth_by      tqc_direct_debit.dd_auth_by%TYPE,
      dd_auth_date    tqc_direct_debit.dd_auth_date%TYPE,
      dd_authorized   tqc_direct_debit.dd_authorized%TYPE,
      bank_branch     VARCHAR2 (60)
   );

   TYPE directdebit_ref IS REF CURSOR
      RETURN directdebit_rec;

   FUNCTION getdirectdebitnonreceipt
      RETURN directdebit_ref;

   FUNCTION getdirectdebitauthorised
      RETURN directdebit_ref;

   TYPE directdebitheader_rec IS RECORD (
      ddh_code                 tqc_direct_debit_header.ddh_code%TYPE,
      ddh_dd_code              tqc_direct_debit_header.ddh_dd_code%TYPE,
      ddh_clnt_code            tqc_direct_debit_header.ddh_clnt_code%TYPE,
      ddh_clnt_bbr_code        tqc_direct_debit_header.ddh_clnt_bbr_code%TYPE,
      ddh_bbr_bnk_code         tqc_direct_debit_header.ddh_bbr_bnk_code%TYPE,
      ddh_clnt_sht_desc        tqc_direct_debit_header.ddh_clnt_sht_desc%TYPE,
      ddh_clnt_name            tqc_direct_debit_header.ddh_clnt_name%TYPE,
      ddh_clnt_bank_acc_no     tqc_direct_debit_header.ddh_clnt_bank_acc_no%TYPE,
      ddh_bbr_branch_name      tqc_direct_debit_header.ddh_bbr_branch_name%TYPE,
      ddh_bbr_sht_desc         tqc_direct_debit_header.ddh_bbr_sht_desc%TYPE,
      ddh_bbr_ref_code         tqc_direct_debit_header.ddh_bbr_ref_code%TYPE,
      ddh_tot_amt              tqc_direct_debit_header.ddh_tot_amt%TYPE,
      ddh_status               tqc_direct_debit_header.ddh_status%TYPE,
      ddh_receipted            tqc_direct_debit_header.ddh_receipted%TYPE,
      ddh_fail_date            tqc_direct_debit_header.ddh_fail_date%TYPE,
      ddh_fail_updated_by      tqc_direct_debit_header.ddh_fail_updated_by%TYPE,
      ddh_fail_update_date     tqc_direct_debit_header.ddh_fail_update_date%TYPE,
      ddh_fail_remarks         tqc_direct_debit_header.ddh_fail_remarks%TYPE,
      ddh_relaunch_date        tqc_direct_debit_header.ddh_relaunch_date%TYPE,
      ddh_relaunch_stop_date   tqc_direct_debit_header.ddh_relaunch_stop_date%TYPE,
      ddh_relaunched_by        tqc_direct_debit_header.ddh_relaunched_by%TYPE,
      ddh_relaunch_stoped_by   tqc_direct_debit_header.ddh_relaunch_stoped_by%TYPE,
      ddh_initial_book_date    tqc_direct_debit_header.ddh_initial_book_date%TYPE,
      ddh_prev_ddh_code        tqc_direct_debit_header.ddh_prev_ddh_code%TYPE,
      ddh_acc_holder           tqc_direct_debit_header.ddh_acc_holder%TYPE,
      bank_branch              VARCHAR2 (60)
   );

   TYPE directdebitheader_ref IS REF CURSOR
      RETURN directdebitheader_rec;

   FUNCTION getdirectdebitheader (v_dd_code NUMBER)
      RETURN directdebitheader_ref;

   TYPE directdebitdetail_rec IS RECORD (
      ddd_code                 tqc_direct_debit_detail.ddd_code%TYPE,
      ddd_ddh_code             tqc_direct_debit_detail.ddd_ddh_code%TYPE,
      ddd_dd_code              tqc_direct_debit_detail.ddd_dd_code%TYPE,
      ddd_sys_code             tqc_direct_debit_detail.ddd_sys_code%TYPE,
      ddd_pol_code             tqc_direct_debit_detail.ddd_pol_code%TYPE,
      ddd_pol_prp_code         tqc_direct_debit_detail.ddd_pol_prp_code%TYPE,
      ddd_pol_proposal_no      tqc_direct_debit_detail.ddd_pol_proposal_no%TYPE,
      ddd_pol_policy_no        tqc_direct_debit_detail.ddd_pol_policy_no%TYPE,
      ddd_other_identifier     tqc_direct_debit_detail.ddd_other_identifier%TYPE,
      ddd_amount               tqc_direct_debit_detail.ddd_amount%TYPE,
      ddd_remarks              tqc_direct_debit_detail.ddd_remarks%TYPE,
      ddd_start_dt             tqc_direct_debit_detail.ddd_start_dt%TYPE,
      ddd_stop_date            tqc_direct_debit_detail.ddd_stop_date%TYPE,
      ddd_status               tqc_direct_debit_detail.ddd_status%TYPE,
      ddd_fail_date            tqc_direct_debit_detail.ddd_fail_date%TYPE,
      ddd_receipted            tqc_direct_debit_detail.ddd_receipted%TYPE,
      ddd_fail_updated_by      tqc_direct_debit_detail.ddd_fail_updated_by%TYPE,
      ddd_fail_update_date     tqc_direct_debit_detail.ddd_fail_update_date%TYPE,
      ddd_ppr_code             tqc_direct_debit_detail.ddd_ppr_code%TYPE,
      ddd_pol_type             tqc_direct_debit_detail.ddd_pol_type%TYPE,
      ddd_receipted_by         tqc_direct_debit_detail.ddd_receipted_by%TYPE,
      ddd_receipt_no           tqc_direct_debit_detail.ddd_receipt_no%TYPE,
      ddd_receipt_date         tqc_direct_debit_detail.ddd_receipt_date%TYPE,
      ddd_receipted_date       tqc_direct_debit_detail.ddd_receipted_date%TYPE,
      ddd_fail_remarks         tqc_direct_debit_detail.ddd_fail_remarks%TYPE,
      ddd_relaunch_date        tqc_direct_debit_detail.ddd_relaunch_date%TYPE,
      ddd_relaunch_stop_date   tqc_direct_debit_detail.ddd_relaunch_stop_date%TYPE,
      ddd_relaunched_by        tqc_direct_debit_detail.ddd_relaunched_by%TYPE,
      ddd_relaunch_stoped_by   tqc_direct_debit_detail.ddd_relaunch_stoped_by%TYPE
   );

   TYPE directdebitdetail_ref IS REF CURSOR
      RETURN directdebitdetail_rec;

   FUNCTION getdirectdebitdetail (v_ddh_code NUMBER)
      RETURN directdebitdetail_ref;

   TYPE glaccounts_rec IS RECORD (
      acc_number   fms_accounts.acc_number%TYPE,
      acc_name     fms_accounts.acc_name%TYPE
   );

   TYPE glaccounts_ref IS REF CURSOR
      RETURN glaccounts_rec;

   FUNCTION getallglaccounts
      RETURN glaccounts_ref;

   PROCEDURE get_service_provider_types_act (
      v_service_provider_types_ref   OUT   serv_provider_types_act_ref
   );

   TYPE email_templates_rec IS RECORD (
      msgt_code         tqc_msg_templates.msgt_code%TYPE,
      msgt_sht_desc     tqc_msg_templates.msgt_sht_desc%TYPE,
      msgt_msg          tqc_msg_templates.msgt_msg%TYPE,
      msgt_sys_code     tqc_msg_templates.msgt_sys_code%TYPE,
      msgt_sys_module   tqc_msg_templates.msgt_sys_module%TYPE,
      msgt_type         tqc_msg_templates.msgt_type%TYPE
   );

   TYPE email_templates_ref IS REF CURSOR
      RETURN email_templates_rec;

   FUNCTION get_em_template (v_sys_code IN NUMBER)
      RETURN email_templates_ref;

   TYPE prospects_rec IS RECORD (
      prs_code             tqc_prospects.prs_code%TYPE,
      prs_surname          tqc_prospects.prs_surname%TYPE,
      prs_physical_addrs   tqc_prospects.prs_physical_addrs%TYPE,
      prs_postal_addrs     tqc_prospects.prs_postal_addrs%TYPE,
      prs_other_names      tqc_prospects.prs_other_names%TYPE,
      prs_tel_no           tqc_prospects.prs_tel_no%TYPE,
      prs_mobile_no        tqc_prospects.prs_mobile_no%TYPE,
      prs_email_addrs      tqc_prospects.prs_email_addrs%TYPE,
      prs_id_reg_no        tqc_prospects.prs_id_reg_no%TYPE,
      prs_dob              tqc_prospects.prs_dob%TYPE,
      prs_pin              tqc_prospects.prs_pin%TYPE,
      prs_twn_code         tqc_prospects.prs_twn_code%TYPE,
      prs_cou_code         tqc_prospects.prs_cou_code%TYPE,
      prs_postal_code      tqc_prospects.prs_postal_code%TYPE,
      country_name         VARCHAR2 (100),
      town_name            VARCHAR2 (100),
      pst_desc             tqc_postal_codes.pst_desc%TYPE,
      prs_type             tqc_prospects.prs_type%TYPE, 
      prs_contact          tqc_prospects.prs_contact%TYPE, 
      prs_contact_tel      tqc_prospects.prs_contact_tel%TYPE
   );

   TYPE prospects_ref IS REF CURSOR
      RETURN prospects_rec;

   FUNCTION getprospects
      RETURN prospects_ref;

   TYPE all_systems_rec IS RECORD (
      sys_code                tqc_systems.sys_code%TYPE,
      sys_sht_desc            tqc_systems.sys_sht_desc%TYPE,
      sys_name                tqc_systems.sys_name%TYPE,
      sys_main_form_name      tqc_systems.sys_main_form_name%TYPE,
      sys_active              tqc_systems.sys_active%TYPE,
      sys_dbase_username      tqc_systems.sys_dbase_username%TYPE,
      sys_dbase_password      tqc_systems.sys_dbase_password%TYPE,
      sys_dbase_string        tqc_systems.sys_dbase_string%TYPE,
      sys_path                tqc_systems.sys_path%TYPE,
      sys_org_code            tqc_systems.sys_org_code%TYPE,
      sys_agn_main_frm_name   tqc_systems.sys_agn_main_frm_name%TYPE,
      sys_kba_code            tqc_systems.sys_kba_code%TYPE,
      sys_signature_path      tqc_systems.sys_signature_path%TYPE,
      sys_template            tqc_systems.sys_template%TYPE
   );

   TYPE all_systems_ref IS REF CURSOR
      RETURN all_systems_rec;

   FUNCTION getallsystems
      RETURN all_systems_ref;

   TYPE orgdivlevelstype_rec IS RECORD (
      dlt_code        tqc_org_division_levels_type.dlt_code%TYPE,
      dlt_sys_code    tqc_org_division_levels_type.dlt_sys_code%TYPE,
      dlt_desc        tqc_org_division_levels_type.dlt_desc%TYPE,
      dlt_act_code    tqc_org_division_levels_type.dlt_act_code%TYPE,
      acc_type_name   VARCHAR2 (50),
      dlt_code_val    tqc_org_division_levels_type.dlt_code_val%TYPE
   );

   TYPE orgdivlevelstype_ref IS REF CURSOR
      RETURN orgdivlevelstype_rec;

   FUNCTION getorgdivlevelstype (v_sys_code IN NUMBER)
      RETURN orgdivlevelstype_ref;

   TYPE orgdivisionlevels_rec IS RECORD (
      odl_code       tqc_org_division_levels.odl_code%TYPE,
      odl_dlt_code   tqc_org_division_levels.odl_dlt_code%TYPE,
      odl_desc       tqc_org_division_levels.odl_desc%TYPE,
      odl_ranking    tqc_org_division_levels.odl_ranking%TYPE,
      odl_type       tqc_org_division_levels.odl_type%TYPE
   );

   TYPE orgdivisionlevels_ref IS REF CURSOR
      RETURN orgdivisionlevels_rec;

   FUNCTION getorgdivisionlevels (v_dlt_code IN VARCHAR2)
      RETURN orgdivisionlevels_ref;

   FUNCTION getorgdivisionlevelsbyranking (
      v_dlt_code   IN   VARCHAR2,
      v_ranking    IN   tqc_org_division_levels.odl_ranking%TYPE
   )
      RETURN orgdivisionlevels_ref;

   TYPE orgsubdivisions_rec IS RECORD (
      osd_code                tqc_org_subdivisions.osd_code%TYPE,
      osd_parent_osd_code     tqc_org_subdivisions.osd_parent_osd_code%TYPE,
      osd_dlt_code            tqc_org_subdivisions.osd_dlt_code%TYPE,
      osd_odl_code            tqc_org_subdivisions.osd_odl_code%TYPE,
      osd_name                tqc_org_subdivisions.osd_name%TYPE,
      osd_wef                 tqc_org_subdivisions.osd_wef%TYPE,
      osd_div_head_agn_code   tqc_org_subdivisions.osd_div_head_agn_code%TYPE,
      osd_sys_code            tqc_org_subdivisions.osd_sys_code%TYPE,
      dlt_desc                tqc_org_division_levels_type.dlt_desc%TYPE,
      odl_desc                tqc_org_division_levels.odl_desc%TYPE,
      agent_name              VARCHAR2 (100),
      odl_ranking             tqc_org_division_levels.odl_ranking%TYPE,
      odl_type                tqc_org_division_levels.odl_type%TYPE,
      osd_brn_code            tqc_org_subdivisions.osd_brn_code%TYPE,
      osd_reg_code            tqc_org_subdivisions.osd_reg_code%TYPE,
      osd_post_level          tqc_org_subdivisions.osd_post_level%TYPE,
      osd_manager_allwd       tqc_org_subdivisions.osd_manager_allwd%TYPE,
      osd_over_comm_earn      tqc_org_subdivisions.osd_over_comm_earn%TYPE,
      osd_id                  tqc_org_subdivisions.osd_id%TYPE,
      osd_parent_id           tqc_org_subdivisions.osd_parent_id%TYPE
   );

   TYPE orgsubdivisions_ref IS REF CURSOR
      RETURN orgsubdivisions_rec;

   FUNCTION getorgsubdivisionsbysys (v_sys_code IN NUMBER)
      RETURN orgsubdivisions_ref;

   FUNCTION getlowestorgsubdivsbysys (v_sys_code IN NUMBER, v_ac_type NUMBER)
      RETURN orgsubdivisions_ref;

   FUNCTION getorgsubdivisionsbysubdiv (v_osd_code IN VARCHAR2)
      RETURN orgsubdivisions_ref;

   FUNCTION getorgsubdivisionsbydlt (v_dlt_code IN VARCHAR2)
      RETURN orgsubdivisions_ref;

   TYPE agencieslov_rec IS RECORD (
      agn_code       tqc_agencies.agn_code%TYPE,
      agn_sht_desc   tqc_agencies.agn_sht_desc%TYPE,
      agn_name       tqc_agencies.agn_name%TYPE
   );

   TYPE agencieslov_ref IS REF CURSOR
      RETURN agencieslov_rec;

   FUNCTION getallagencieslov
      RETURN agencieslov_ref;

   FUNCTION getallmarketers (v_actcode NUMBER DEFAULT NULL)
      RETURN agencieslov_ref;

   FUNCTION getspecificmarketers (v_agn_code IN NUMBER)
      RETURN agencieslov_ref;

   TYPE agencyactivities_rec IS RECORD (
      aac_code            tqc_agency_activities.aac_code%TYPE,
      aac_acty_code       tqc_agency_activities.aac_acty_code%TYPE,
      aac_wef             tqc_agency_activities.aac_wef%TYPE,
      aac_estimate_wet    tqc_agency_activities.aac_estimate_wet%TYPE,
      aac_actual_wet      tqc_agency_activities.aac_actual_wet%TYPE,
      aac_remarks         tqc_agency_activities.aac_remarks%TYPE,
      aac_agn_code        tqc_agency_activities.aac_agn_code%TYPE,
      aac_clnt_code       tqc_agency_activities.aac_clnt_code%TYPE,
      aac_spr_code        tqc_agency_activities.aac_spr_code%TYPE,
      aac_sys_code        tqc_agency_activities.aac_sys_code%TYPE,
      aac_mktr_agn_code   tqc_agency_activities.aac_mktr_agn_code%TYPE,
      acty_desc           VARCHAR2 (100),
      client_name         VARCHAR2 (100),
      agency_name         VARCHAR2 (100),
      mktr_agency_name    VARCHAR2 (100),
      provider_name       VARCHAR2 (100),
      system_name         VARCHAR2 (100)
   );

   TYPE agencyactivities_ref IS REF CURSOR
      RETURN agencyactivities_rec;

   FUNCTION getagencyactivities (v_agn_code IN NUMBER)
      RETURN agencyactivities_ref;

   TYPE agencyactivitiesdetails_rec IS RECORD (
      aac_code           tqc_agency_activities.aac_code%TYPE,
      aac_acty_code      tqc_agency_activities.aac_acty_code%TYPE,
      aac_wef            tqc_agency_activities.aac_wef%TYPE,
      aac_estimate_wet   tqc_agency_activities.aac_estimate_wet%TYPE,
      aac_actual_wet     tqc_agency_activities.aac_actual_wet%TYPE,
      aac_remarks        tqc_agency_activities.aac_remarks%TYPE,
      aac_agn_code       tqc_agency_activities.aac_agn_code%TYPE,
      aac_sys_code       tqc_agency_activities.aac_sys_code%TYPE,
      aac_by_code        tqc_agency_activities.aac_by_code%TYPE,
      aac_by_type        tqc_agency_activities.aac_by_type%TYPE,
      acty_desc          VARCHAR2 (100),
      agency_name        VARCHAR2 (100),
      system_name        VARCHAR2 (100),
      acc_type_name      VARCHAR2 (150),
      aac_reasons        tqc_agency_activities.aac_reasons%TYPE
   );

   TYPE agencyactivitiesdetails_ref IS REF CURSOR
      RETURN agencyactivitiesdetails_rec;

   FUNCTION getagencyactivitiesdetails (v_agn_code IN NUMBER)
      RETURN agencyactivitiesdetails_ref;

   FUNCTION clnt_name (v_client_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION agn_name (v_agn_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION activity_desc (v_acty_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION provider_name (v_spr_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION system_name (v_sys_code NUMBER)
      RETURN VARCHAR2;

   TYPE activitytypes_rec IS RECORD (
      acty_code       tqc_activity_types.acty_code%TYPE,
      acty_sys_code   tqc_activity_types.acty_sys_code%TYPE,
      acty_desc       tqc_activity_types.acty_desc%TYPE
   );

   TYPE activitytypes_ref IS REF CURSOR
      RETURN activitytypes_rec;

   FUNCTION getallactivitytypes
      RETURN activitytypes_ref;

   FUNCTION getallactivitytypesbysys (v_sys_code NUMBER)
      RETURN activitytypes_ref;

   FUNCTION getactivitytypesbysystem (v_sys_code NUMBER)
      RETURN activitytypes_ref;

   TYPE clientslov_rec IS RECORD (
      clnt_code       tqc_clients.clnt_code%TYPE,
      clnt_sht_desc   tqc_clients.clnt_sht_desc%TYPE,
      clnt_name       tqc_clients.clnt_name%TYPE
   );

   TYPE clientslov_ref IS REF CURSOR
      RETURN clientslov_rec;

   FUNCTION getallclientslov
      RETURN clientslov_ref;

   TYPE serviceproviderlov_rec IS RECORD (
      spr_code       tqc_service_providers.spr_code%TYPE,
      spr_sht_desc   tqc_service_providers.spr_sht_desc%TYPE,
      spr_name       tqc_service_providers.spr_name%TYPE
   );

   TYPE serviceproviderlov_ref IS REF CURSOR
      RETURN serviceproviderlov_rec;

   FUNCTION getallserviceprovlov
      RETURN serviceproviderlov_ref;

   FUNCTION country_name (v_cou_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION town_name (v_twn_code NUMBER)
      RETURN VARCHAR2;

   TYPE holidaysdef_rec IS RECORD (
      thd_desc       tqc_holidays_definitions.thd_desc%TYPE,
      thd_day        tqc_holidays_definitions.thd_day%TYPE,
      thd_mon        tqc_holidays_definitions.thd_mon%TYPE,
      thd_status     tqc_holidays_definitions.thd_status%TYPE,
      thd_cou_code   tqc_holidays_definitions.thd_cou_code%TYPE
   );

   TYPE holidaysdef_ref IS REF CURSOR
      RETURN holidaysdef_rec;

   FUNCTION getholidaydefinitions (v_cou_code IN NUMBER)
      RETURN holidaysdef_ref;

   FUNCTION acc_type_name (v_act_code NUMBER)
      RETURN VARCHAR2;

   TYPE accounttypeslov_rec IS RECORD (
      act_code            tqc_account_types.act_code%TYPE,
      act_type_sht_desc   tqc_account_types.act_type_sht_desc%TYPE,
      act_account_type    tqc_account_types.act_account_type%TYPE
   );

   TYPE accounttypeslov_ref IS REF CURSOR
      RETURN accounttypeslov_rec;

   FUNCTION getallaccounttypeslov
      RETURN accounttypeslov_ref;

   FUNCTION gethierarchyacctypeslov (v_sys_code NUMBER)
      RETURN accounttypeslov_ref;

   TYPE orgsubdivprevheads_rec IS RECORD (
      osdph_code            tqc_org_subdiv_prev_heads.osdph_code%TYPE,
      osdph_osd_code        tqc_org_subdiv_prev_heads.osdph_osd_code%TYPE,
      osdph_prev_agn_code   tqc_org_subdiv_prev_heads.osdph_prev_agn_code%TYPE,
      osdph_wet             tqc_org_subdiv_prev_heads.osdph_wet%TYPE,
      osd_name              tqc_org_subdivisions.osd_name%TYPE,
      agency_name           VARCHAR2 (100),
      osdph_osd_id          tqc_org_subdiv_prev_heads.osdph_osd_id%TYPE
   );

   TYPE orgsubdivprevheads_ref IS REF CURSOR
      RETURN orgsubdivprevheads_rec;

   FUNCTION getorgsubdivprevheads (v_osd_code IN VARCHAR2)
      RETURN orgsubdivprevheads_ref;

   TYPE syspostlevels_rec IS RECORD (
      syspl_sys_code   tqc_sys_post_levels.syspl_sys_code%TYPE,
      syspl_code       tqc_sys_post_levels.syspl_code%TYPE,
      syspl_sht_desc   tqc_sys_post_levels.syspl_sht_desc%TYPE,
      syspl_desc       tqc_sys_post_levels.syspl_desc%TYPE,
      syspl_ranking    tqc_sys_post_levels.syspl_ranking%TYPE,
      syspl_wef        tqc_sys_post_levels.syspl_wef%TYPE
   );

   TYPE syspostlevels_ref IS REF CURSOR
      RETURN syspostlevels_rec;

   FUNCTION getsyspostlevelsbysys (v_sys_code NUMBER)
      RETURN syspostlevels_ref;

   TYPE syspost_rec IS RECORD (
      spost_sys_code            tqc_sys_posts.spost_sys_code%TYPE,
      spost_syspl_code          tqc_sys_posts.spost_syspl_code%TYPE,
      spost_parent_spost_code   tqc_sys_posts.spost_parent_spost_code%TYPE,
      spost_code                tqc_sys_posts.spost_code%TYPE,
      spost_sht_desc            tqc_sys_posts.spost_sht_desc%TYPE,
      spost_desc                tqc_sys_posts.spost_desc%TYPE,
      spost_remarks             tqc_sys_posts.spost_remarks%TYPE,
      spost_wef                 tqc_sys_posts.spost_wef%TYPE,
      spost_brn_code            tqc_sys_posts.spost_brn_code%TYPE,
      spost_subdiv_osd_code     tqc_sys_posts.spost_subdiv_osd_code%TYPE,
      spost_usr_code            tqc_sys_posts.spost_usr_code%TYPE
   );

   TYPE syspost_ref IS REF CURSOR
      RETURN syspost_rec;

   FUNCTION getsyspostsbylevel (v_syspl_code NUMBER)
      RETURN syspost_ref;

   FUNCTION getsyspostsbypost (v_spost_code NUMBER)
      RETURN syspost_ref;

   FUNCTION getsyspostsbysystem (v_sys_code NUMBER)
      RETURN syspost_ref;

   TYPE sysprcsssubarealmts_rec IS RECORD (
      spsat_code          tqc_sys_prcss_subarea_lmts.spsat_code%TYPE,
      spsat_sprsa_code    tqc_sys_prcss_subarea_lmts.spsat_sprsa_code%TYPE,
      spsat_no_of_level   tqc_sys_prcss_subarea_lmts.spsat_no_of_level%TYPE,
      spsat_min_limit     tqc_sys_prcss_subarea_lmts.spsat_min_limit%TYPE,
      spsat_max_limit     tqc_sys_prcss_subarea_lmts.spsat_max_limit%TYPE
   );

   TYPE sysprcsssubarealmts_ref IS REF CURSOR
      RETURN sysprcsssubarealmts_rec;

   FUNCTION getsysprcsssubarealmts (v_sprsa_code NUMBER)
      RETURN sysprcsssubarealmts_ref;

   TYPE sysprcsssubarealvls_rec IS RECORD (
      spsal_code            tqc_sys_prcss_subarea_lvls.spsal_code%TYPE,
      spsal_sprsa_code      tqc_sys_prcss_subarea_lvls.spsal_sprsa_code%TYPE,
      spsal_spsat_code      tqc_sys_prcss_subarea_lvls.spsal_spsat_code%TYPE,
      spsal_level           tqc_sys_prcss_subarea_lvls.spsal_level%TYPE,
      spsal_approver_type   tqc_sys_prcss_subarea_lvls.spsal_approver_type%TYPE,
      spsal_approver_id     tqc_sys_prcss_subarea_lvls.spsal_approver_id%TYPE,
      usr_username          tqc_users.usr_username%TYPE
   );

   TYPE sysprcsssubarealvls_ref IS REF CURSOR
      RETURN sysprcsssubarealvls_rec;

   FUNCTION getsysprcsssubarealvls (v_spsat_code NUMBER)
      RETURN sysprcsssubarealvls_ref;

   FUNCTION bank_name (v_bnk_code NUMBER)
      RETURN VARCHAR2;

   TYPE ddfwdingbanks_rec IS RECORD (
      bnk_code                 tqc_banks.bnk_code%TYPE,
      ddr_report_description   tqc_direct_debit_reports.ddr_report_description%TYPE,
      bnk_bank_name            tqc_banks.bnk_bank_name%TYPE
   );

   TYPE ddfwdingbanks_ref IS REF CURSOR
      RETURN ddfwdingbanks_rec;

   FUNCTION getddfwdingbankslov
      RETURN ddfwdingbanks_ref;

   TYPE directdebitreports_rec IS RECORD (
      ddr_code                 tqc_direct_debit_reports.ddr_code%TYPE,
      ddr_report_description   tqc_direct_debit_reports.ddr_report_description%TYPE
   );

   TYPE directdebitreports_ref IS REF CURSOR
      RETURN directdebitreports_rec;

   FUNCTION getdirectdebitreports
      RETURN directdebitreports_ref;

   FUNCTION ddreport_desc (v_ddr_code NUMBER)
      RETURN VARCHAR2;

   TYPE users_rec IS RECORD (
      usr_code                tqc_users.usr_code%TYPE,
      usr_username            tqc_users.usr_username%TYPE,
      usr_name                tqc_users.usr_name%TYPE,
      usr_last_date           tqc_users.usr_last_date%TYPE,
      usr_dt_created          tqc_users.usr_dt_created%TYPE,
      usr_status              tqc_users.usr_status%TYPE,
      usr_pwd                 tqc_users.usr_pwd%TYPE,
      usr_created_by          tqc_users.usr_created_by%TYPE,
      usr_email               tqc_users.usr_email%TYPE,
      usr_per_rank_id         tqc_users.usr_per_rank_id%TYPE,
      usr_per_rank_sht_desc   tqc_users.usr_per_rank_sht_desc%TYPE,
      usr_per_id              tqc_users.usr_per_id%TYPE,
      usr_personel_rank       tqc_users.usr_personel_rank%TYPE,
      usr_pwd_changed         tqc_users.usr_pwd_changed%TYPE,
      usr_pwd_reset           tqc_users.usr_pwd_reset%TYPE,
      usr_login_attempts      tqc_users.usr_login_attempts%TYPE,
      usr_sign                tqc_users.usr_sign%TYPE,
      usr_ref                 tqc_users.usr_ref%TYPE,
      usr_type                tqc_users.usr_type%TYPE,
      usr_signature           tqc_users.usr_signature%TYPE,
      usr_acct_mgr            tqc_users.usr_acct_mgr%TYPE
   );

   TYPE users_ref IS REF CURSOR
      RETURN users_rec;

   FUNCTION getusersbytypegroup
      RETURN users_ref;

   FUNCTION getusersbytypeuser
      RETURN users_ref;

   FUNCTION currency_sysmbol (v_cur_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION state_name (v_sts_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION admin_region_name (v_admin_region_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION system_param_val (v_param_name VARCHAR2)
      RETURN VARCHAR2;

   TYPE check_activity_exist_rec IS RECORD (
      v_count   NUMBER
   );

   TYPE check_activity_exist_ref IS REF CURSOR
      RETURN check_activity_exist_rec;

   FUNCTION check_activity_exist (v_param_name VARCHAR2)
      RETURN check_activity_exist_ref;

   FUNCTION check_hierarchy_exist (v_param_name VARCHAR2)
      RETURN check_activity_exist_ref;

   FUNCTION check_hierarchy_ranking_exist (
      v_odlt_code   VARCHAR2,
      v_rank        tqc_org_division_levels.odl_ranking%TYPE
   )
      RETURN check_activity_exist_ref;

   TYPE checkif_dob_required_rec IS RECORD (
      param_value   tqc_parameters.param_value%TYPE
   );

   TYPE checkif_dob_required_ref IS REF CURSOR
      RETURN checkif_dob_required_rec;

   FUNCTION checkif_dob_required
      RETURN checkif_dob_required_ref;

   FUNCTION checkif_sms_required
      RETURN checkif_dob_required_ref;

   FUNCTION checkif_default_date
      RETURN checkif_dob_required_ref;

   TYPE check_max_rank_rec IS RECORD (
      v_count   NUMBER
   );

   TYPE check_max_rank_ref IS REF CURSOR
      RETURN check_max_rank_rec;

   FUNCTION check_max_rank (v_dlt_code IN VARCHAR2)
      RETURN check_max_rank_ref;

   FUNCTION checkif_uppercase_required
      RETURN checkif_dob_required_ref;

   FUNCTION checkif_divisions_applic
      RETURN checkif_dob_required_ref;

   /*TYPE participantsByActivity_rec IS RECORD(

     PARTICIP_ID        TQC_ACTIVITY_PARTICIPANTS.PARTICIP_ID%TYPE,
     PARTICIP_CODE     TQC_ACTIVITY_PARTICIPANTS.PARTICIP_CODE%TYPE,
     PARTICIP_AAC_CODE TQC_ACTIVITY_PARTICIPANTS.PARTICIP_AAC_CODE%TYPE,
     PARTICIP_TYPE     VARCHAR2(300),
     PARTICIP_BY_CODE TQC_ACTIVITY_PARTICIPANTS.PARTICIP_BY_CODE%TYPE,
     agnName  VARCHAR2(300)



   );*/

   --TYPE  participantsByActivity_ref IS REF CURSOR RETURN  participantsByActivity_rec;
   --FUNCTION getParticipantsByActivity(v_aac_code TQC_ACTIVITY_PARTICIPANTS.PARTICIP_AAC_CODE%TYPE)  RETURN participantsByActivity_ref;
   FUNCTION get_agencyname (v_agn_type VARCHAR2, v_ag_code NUMBER)
      RETURN VARCHAR2;

   /*PROCEDURE create_activity_particip(v_addEdit VARCHAR2,
   v_particip_id          TQC_ACTIVITY_PARTICIPANTS.PARTICIP_ID%TYPE ,
   v_particip_aac_code  TQC_ACTIVITY_PARTICIPANTS.PARTICIP_AAC_CODE%TYPE,
   v_particip_type      TQC_ACTIVITY_PARTICIPANTS.PARTICIP_TYPE%TYPE,
   v_particip_by_code   TQC_ACTIVITY_PARTICIPANTS.PARTICIP_BY_CODE%TYPE,
   v_particip_code      TQC_ACTIVITY_PARTICIPANTS.PARTICIP_CODE%TYPE

         );*/
   TYPE checkif_user_active_rec IS RECORD (
      clnt_status   tqc_clients.clnt_status%TYPE
   );

   TYPE checkif_user_active_ref IS REF CURSOR
      RETURN checkif_user_active_rec;

   FUNCTION checkif_user_active (v_client_code tqc_clients.clnt_status%TYPE)
      RETURN checkif_user_active_ref;

   FUNCTION sector_name (v_sec_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION parent_company_name (v_parent_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION checkif_telephonerequired
      RETURN checkif_dob_required_ref;

   FUNCTION checkif_emailrequired
      RETURN checkif_dob_required_ref;

   FUNCTION getagenciesbyacctype (v_sht_desc IN VARCHAR2)
      RETURN agencieslov_ref;

   FUNCTION getagency_name (v_agn_code NUMBER, v_acc_type VARCHAR2)
      RETURN VARCHAR2;

   FUNCTION account_type_name (v_sht_desc VARCHAR2)
      RETURN VARCHAR2;

   TYPE returnparam_rec IS RECORD (
      param_value   tqc_parameters.param_value%TYPE
   );

   TYPE returnparam_ref IS REF CURSOR
      RETURN returnparam_rec;

   FUNCTION finddefaultsite
      RETURN returnparam_ref;

   TYPE returnbranches_rec IS RECORD (
      brn_code       tqc_branches.brn_code%TYPE,
      brn_sht_desc   tqc_branches.brn_sht_desc%TYPE,
      brn_name       tqc_branches.brn_name%TYPE,
      reg_sht_desc       tqc_regions.reg_sht_desc%type
   );

   TYPE returnbranches_ref IS REF CURSOR
      RETURN returnbranches_rec;

   FUNCTION findbranches (v_reg_code NUMBER, v_org_code NUMBER DEFAULT NULL)
     RETURN returnbranches_ref;

   FUNCTION branch_name (v_brn_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION bankbranch_name (v_bbr_code NUMBER)
      RETURN VARCHAR2;

   TYPE claimpaymodes_rec IS RECORD (
      cpm_code       tqc_clm_payment_modes.cpm_code%TYPE,
      cpm_sht_desc   tqc_clm_payment_modes.cpm_sht_desc%TYPE,
      cpm_desc       tqc_clm_payment_modes.cpm_desc%TYPE,
      cpm_remarks    tqc_clm_payment_modes.cpm_remarks%TYPE,
      cpm_min_amt    tqc_clm_payment_modes.cpm_min_amt%TYPE,
      cpm_max_amt    tqc_clm_payment_modes.cpm_max_amt%TYPE,
      cpm_default    tqc_clm_payment_modes.cpm_default%TYPE
   );

   TYPE claimpaymodes_ref IS REF CURSOR
      RETURN claimpaymodes_rec;

   FUNCTION getclaimpaymodes
      RETURN claimpaymodes_ref;

   TYPE mobilepaymenttypes_rec IS RECORD (
      mpt_code              tqc_mobile_pymnt_types.mpt_code%TYPE,
      mpt_sht_desc          tqc_mobile_pymnt_types.mpt_sht_desc%TYPE,
      mpt_desc              tqc_mobile_pymnt_types.mpt_desc%TYPE,
      mpt_min_amt_allowed   tqc_mobile_pymnt_types.mpt_min_amt_allowed%TYPE,
      mpt_max_amt_allowed   tqc_mobile_pymnt_types.mpt_max_amt_allowed%TYPE,
      mpt_cou_code          tqc_mobile_pymnt_types.mpt_cou_code%TYPE
   );

   TYPE mobilepaymenttypes_ref IS REF CURSOR
      RETURN mobilepaymenttypes_rec;

   FUNCTION getmobilepaymenttypes (
      v_cou_code   tqc_mobile_pymnt_types.mpt_cou_code%TYPE
   )
      RETURN mobilepaymenttypes_ref;

   TYPE mobiletypesprefix_rec IS RECORD (
      mptp_code            tqc_mob_pymnt_types_prefixes.mptp_code%TYPE,
      mptp_mob_no_prefix   tqc_mob_pymnt_types_prefixes.mptp_mob_no_prefix%TYPE,
      mptp_mpt_code        tqc_mob_pymnt_types_prefixes.mptp_mpt_code%TYPE,
      sms_number           VARCHAR2 (200)
   );

   TYPE mobiletypesprefix_ref IS REF CURSOR
      RETURN mobiletypesprefix_rec;

   FUNCTION getmobtypesprefixes (
      v_mptp_code   tqc_mob_pymnt_types_prefixes.mptp_mpt_code%TYPE
   )
      RETURN mobiletypesprefix_ref;

   FUNCTION getmobprefixes (v_coucode NUMBER)
      RETURN mobiletypesprefix_ref;

   TYPE user_rec IS RECORD (
      usr_code         tqc_users.usr_code%TYPE,
      usr_username     tqc_users.usr_username%TYPE,
      usr_name         tqc_users.usr_name%TYPE,
      usr_dt_created   tqc_users.usr_dt_created%TYPE,
      usr_created_by   tqc_users.usr_created_by%TYPE
   );

   TYPE user_ref IS REF CURSOR
      RETURN user_rec;

   FUNCTION getgroupusers (v_created_date VARCHAR2)
      RETURN user_ref;

   TYPE usergroup_rec IS RECORD (
      gusr_code           tqc_group_users.gusr_code%TYPE,
      gusr_grp_usr_code   tqc_group_users.gusr_grp_usr_code%TYPE,
      usr_code            tqc_users.usr_code%TYPE,
      usr_username        tqc_users.usr_username%TYPE,
      usr_name            tqc_users.usr_name%TYPE
   );

   TYPE usergroup_ref IS REF CURSOR
      RETURN usergroup_rec;

   FUNCTION getgroupsmembers (
      v_grp_usr_code   tqc_group_users.gusr_grp_usr_code%TYPE
   )
      RETURN usergroup_ref;

   FUNCTION findmemoclassapplicable
      RETURN returnparam_ref;

   TYPE clientgroup_rec IS RECORD (
      grp_code      tqc_group_client.grp_code%TYPE,
      grp_name      tqc_group_client.grp_name%TYPE,
      grp_minimum   tqc_group_client.grp_minimum%TYPE,
      grp_maximum   tqc_group_client.grp_maximum%TYPE
   );

   TYPE clientgroup_ref IS REF CURSOR
      RETURN clientgroup_rec;

   FUNCTION getclientgroups
      RETURN clientgroup_ref;

   TYPE clientgroupdtls_rec IS RECORD (
      grp_name           tqc_group_client.grp_code%TYPE,
      grpd_code          tqc_group_clnt_dtls.grpd_code%TYPE,
      grpd_clnt_code     tqc_group_clnt_dtls.grpd_clnt_code%TYPE,
      grpd_grp_code      tqc_group_clnt_dtls.grpd_grp_code%TYPE,
      clnt_sht_desc      tqc_clients.clnt_sht_desc%TYPE,
      clnt_name          tqc_clients.clnt_name%TYPE,
      clnt_other_names   tqc_clients.clnt_other_names%TYPE
   );

   TYPE clientgroupdtls_ref IS REF CURSOR
      RETURN clientgroupdtls_rec;

   FUNCTION getclientgroupmembers (
      v_grp_code   tqc_group_clnt_dtls.grpd_grp_code%TYPE
   )
      RETURN clientgroupdtls_ref;

   TYPE clientcomplaints_rec IS RECORD (
      comp_code          tqc_client_complaints.comp_code%TYPE,
      comp_clnt_code     tqc_client_complaints.comp_clnt_code%TYPE,
      comp_subject       tqc_client_complaints.comp_subject%TYPE,
      comp_message       tqc_client_complaints.comp_message%TYPE,
      clnt_sht_desc      tqc_clients.clnt_sht_desc%TYPE,
      clnt_name          tqc_clients.clnt_name%TYPE,
      clnt_other_names   tqc_clients.clnt_other_names%TYPE
   );

   TYPE clientcomplaints_ref IS REF CURSOR
      RETURN clientcomplaints_rec;

   FUNCTION getclientcomplaints (
      v_clnt_code   tqc_client_complaints.comp_clnt_code%TYPE
   )
      RETURN clientcomplaints_ref;

   FUNCTION physical_address_required
      RETURN returnparam_ref;

   FUNCTION postal_address_required
      RETURN returnparam_ref;

   TYPE print_server_rec IS RECORD (
      serv_code                tqc_print_servers.serv_code%TYPE,
      serv_name                tqc_print_servers.serv_name%TYPE,
      serv_filter              tqc_print_servers.serv_filter%TYPE,
      serv_uri                 tqc_print_servers.serv_uri%TYPE,
      serv_filter_command      tqc_print_servers.serv_filter_command%TYPE,
      serv_sec_username        tqc_print_servers.serv_sec_username%TYPE,
      serv_sec_password        tqc_print_servers.serv_sec_password%TYPE,
      serv_sec_auth_type       tqc_print_servers.serv_sec_auth_type%TYPE,
      serv_sec_encrpt_type     tqc_print_servers.serv_sec_encrpt_type%TYPE,
      serv_proxy_host          tqc_print_servers.serv_proxy_host%TYPE,
      serv_proxy_port          tqc_print_servers.serv_proxy_port%TYPE,
      serv_proxy_username      tqc_print_servers.serv_proxy_username%TYPE,
      serv_proxy_pasword       tqc_print_servers.serv_proxy_pasword%TYPE,
      serv_proxy_authen_type   tqc_print_servers.serv_proxy_authen_type%TYPE
   );

   TYPE print_server_ref IS REF CURSOR
      RETURN print_server_rec;

   FUNCTION get_print_servers
      RETURN print_server_ref;

   TYPE branch_names_details_rec IS RECORD (
      bns_code          tqc_branch_names.bns_code%TYPE,
      bns_sht_desc      tqc_branch_names.bns_sht_desc%TYPE,
      bns_name          tqc_branch_names.bns_name%TYPE,
      bns_phy_addrs     tqc_branch_names.bns_phy_addrs%TYPE,
      bns_email_addrs   tqc_branch_names.bns_email_addrs%TYPE,
      bns_post_addrs    tqc_branch_names.bns_post_addrs%TYPE,
      bns_twn_code      tqc_branch_names.bns_twn_code%TYPE,
      bns_cou_code      tqc_branch_names.bns_cou_code%TYPE,
      bns_contact       tqc_branch_names.bns_contact%TYPE,
      bns_manager       tqc_branch_names.bns_manager%TYPE,
      bns_tel           tqc_branch_names.bns_tel%TYPE,
      bns_fax           tqc_branch_names.bns_fax%TYPE,
      bns_state_code    tqc_branch_names.bns_state_code%TYPE,
      town              VARCHAR2 (100),
      country           VARCHAR2 (100),
      state             VARCHAR2 (100),
      bns_acc_type      tqc_branch_names.bns_acc_type%TYPE,
      bns_region        tqc_branch_names.bns_region%TYPE
   );

   TYPE branch_names_details_ref IS REF CURSOR
      RETURN branch_names_details_rec;

   FUNCTION getbranchnames
      RETURN branch_names_details_ref;

   TYPE branch_printer_rec IS RECORD (
      printer_name   VARCHAR2 (100)
   );

   TYPE branch_printer_ref IS REF CURSOR
      RETURN branch_printer_rec;

   PROCEDURE get_branch_printers (
      v_cursor   OUT      branch_printer_ref,
      brn_code   IN       NUMBER,
      div_code   IN       NUMBER
   );

   TYPE msg_template_rec IS RECORD (
      msgt_code         tqc_msg_templates.msgt_code%TYPE,
      msgt_sht_desc     tqc_msg_templates.msgt_sht_desc%TYPE,
      msgt_msg          tqc_msg_templates.msgt_msg%TYPE,
      msgt_sys_code     tqc_msg_templates.msgt_sys_code%TYPE,
      msgt_sys_module   tqc_msg_templates.msgt_sys_module%TYPE,
      msgt_type         tqc_msg_templates.msgt_type%TYPE
   );

   TYPE msg_template_ref IS REF CURSOR
      RETURN msg_template_rec;

   FUNCTION getmessagetemplates (v_sys_code NUMBER)
      RETURN msg_template_ref;

   FUNCTION getparameter (v_param VARCHAR2)
      RETURN returnparam_ref;

   TYPE branch_rec IS RECORD (
      brn_code       tqc_branches.brn_code%TYPE,
      brn_name       tqc_branches.brn_name%TYPE,
      brn_sht_desc   tqc_branches.brn_sht_desc%TYPE,
      brn_reg_code   tqc_branches.brn_reg_code%TYPE
   );

   TYPE branch_ref IS REF CURSOR
      RETURN branch_rec;

   PROCEDURE get_allbranches (v_refcur OUT branch_ref, v_org_code IN NUMBER);

   FUNCTION getallabudgettypeslov
      RETURN accounttypeslov_ref;

   FUNCTION get_em_template_by_type (v_sys_code IN NUMBER, v_msg_type VARCHAR2)
      RETURN email_templates_ref;

   TYPE lead_sources_rec IS RECORD (
      ldsrc_code   tqc_leads_sources.ldsrc_code%TYPE,
      ldsrc_desc   tqc_leads_sources.ldsrc_desc%TYPE
   );

   TYPE lead_sources_ref IS REF CURSOR
      RETURN lead_sources_rec;

   TYPE lead_statuses_rec IS RECORD (
      lsts_code   tqc_leads_statuses.lsts_code%TYPE,
      lsts_desc   tqc_leads_statuses.lsts_desc%TYPE
   );

   TYPE lead_statuses_ref IS REF CURSOR
      RETURN lead_statuses_rec;

   TYPE leads_rec IS RECORD (
      lds_code              NUMBER (15),
      lds_title             VARCHAR2 (100 BYTE),
      lds_surnname          VARCHAR2 (100 BYTE),
      lds_othernames        VARCHAR2 (100 BYTE),
      lds_camp_tel          VARCHAR2 (100 BYTE),
      lds_mob_tel           VARCHAR2 (100 BYTE),
      lds_cmp_code          NUMBER (15),
      lds_camp_name         VARCHAR2 (100 BYTE),
      lds_fax               VARCHAR2 (100 BYTE),
      lds_design            VARCHAR2 (50 BYTE),
      lds_email_addrs       VARCHAR2 (50 BYTE),
      lds_rate_type         VARCHAR2 (2 BYTE),
      lds_physical_addrs    VARCHAR2 (50 BYTE),
      lds_postal_addrs      VARCHAR2 (50 BYTE),
      lds_country           VARCHAR2 (50 BYTE),
      lds_postal_code       VARCHAR2 (50 BYTE),
      lds_state_name        VARCHAR2 (50 BYTE),
      lds_twn_code          NUMBER (15),
      lds_town_name         VARCHAR2 (50 BYTE),
      lds_cou_code          NUMBER (15),
      lds_state_code        NUMBER (15),
      lds_date              DATE,
      lds_desc              VARCHAR2 (500 BYTE),
      lds_usr_code          NUMBER (15),
      lds_usr_name          VARCHAR2 (50 BYTE),
      lds_org_code          NUMBER (15),
      lds_org_name          VARCHAR2 (50 BYTE),
      lds_sys_code          NUMBER (15),
      lds_sys_name          VARCHAR2 (50 BYTE),
      lds_converted         VARCHAR2 (2 BYTE),
      lds_pont_name         VARCHAR2 (50 BYTE),
      lds_pont_conrt        VARCHAR2 (2 BYTE),
      lds_pont_amount       NUMBER (15, 2),
      lds_cur_code          NUMBER (15),
      lds_cur_name          VARCHAR2 (50 BYTE),
      lds_pont_close_date   DATE,
      lds_pont_sale_stage   VARCHAR2 (50 BYTE),
      lds_industry          VARCHAR2 (50 BYTE),
      lds_ann_revenue       NUMBER (22, 5),
      lds_no_empyee         NUMBER (15),
      lds_web_site          VARCHAR2 (100 BYTE),
      lds_team_usr_code     NUMBER (15),
      lds_team_name         VARCHAR2 (50 BYTE),
      lds_acc_code          NUMBER (15),
      lds_account_name      VARCHAR2 (50 BYTE),
      lds_lsts_code         NUMBER (15),
      lds_lsts_desc         VARCHAR2 (50 BYTE),
      lds_ldsrc_code        NUMBER (15),
      lds_ldsrc_desc        VARCHAR2 (50 BYTE),
      lds_prod_code         NUMBER (15),
      lds_prod_name         VARCHAR2 (50 BYTE),
      lds_div_code          NUMBER (15),
      lds_div_name          VARCHAR (100 BYTE)
   );

   TYPE leads_ref IS REF CURSOR
      RETURN leads_rec;

   PROCEDURE get_lead_sources (v_refcur OUT lead_sources_ref);

   PROCEDURE get_lead_statuses (v_refcur OUT lead_sources_ref);

   PROCEDURE get_leads (v_refcur OUT leads_ref, v_lds_code NUMBER);

   TYPE lead_comments_rec IS RECORD (
      lcmnt_code      tqc_lead_comments.lcmnt_code%TYPE,
      lcmnt_comment   tqc_lead_comments.lcmnt_comment%TYPE,
      lcmnt_date      tqc_lead_comments.lcmnt_date%TYPE
   );

   TYPE lead_comments_ref IS REF CURSOR
      RETURN lead_comments_rec;

   TYPE lead_activities_rec IS RECORD (
      lacts_code     tqc_leads_activities.lacts_code%TYPE,
      act_code       NUMBER,
      act_subject    tqc_activities.act_subject%TYPE,
      act_location   tqc_activities.act_location%TYPE
   );

   TYPE lead_activities_ref IS REF CURSOR
      RETURN lead_activities_rec;

   PROCEDURE get_lead_comments (
      v_refcur     OUT   lead_comments_ref,
      v_lds_code         NUMBER
   );

   PROCEDURE get_lead_activities (
      v_refcur     OUT   lead_activities_ref,
      v_lds_code         NUMBER
   );

   TYPE leads_statuses_rec IS RECORD (
      lsts_code   tqc_leads_statuses.lsts_code%TYPE,
      lsts_desc   tqc_leads_statuses.lsts_desc%TYPE
   );

   TYPE leads_statuses_ref IS REF CURSOR
      RETURN leads_statuses_rec;

   TYPE rptgroup_rec IS RECORD (
      trg_code   tqc_report_groups.trg_code%TYPE,
      trg_name   tqc_report_groups.trg_name%TYPE
   );

   TYPE rptgroup_ref IS REF CURSOR
      RETURN rptgroup_rec;

   FUNCTION get_all_report_groups
      RETURN rptgroup_ref;

   TYPE rptgroupdiv_rec IS RECORD (
      rdg_code       tqc_report_div_groups.rdg_code%TYPE,
      rdg_trg_code   tqc_report_div_groups.rdg_trg_code%TYPE,
      rdg_div_code   tqc_report_div_groups.rdg_div_code%TYPE,
      div_name       tqc_divisions.div_name%TYPE,
      div_order      tqc_divisions.div_order%TYPE
   );

   TYPE rptgroupdiv_ref IS REF CURSOR
      RETURN rptgroupdiv_rec;

   PROCEDURE get_rptgroupdiv (
      v_trg_code         IN       tqc_report_groups.trg_code%TYPE,
      v_rptgroupdivcur   OUT      rptgroupdiv_ref
   );

   PROCEDURE getundefinedrptgrpdivisions (
      v_trg_code       IN       tqc_report_groups.trg_code%TYPE,
      v_divisios_cur   OUT      tqc_web_cursor.divisions_ref
   );

   TYPE sys_app_area_rec IS RECORD (
      saa_code          tqc_sys_applicable_areas.saa_code%TYPE,
      saa_sys_code      tqc_sys_applicable_areas.saa_sys_code%TYPE,
      system_name       VARCHAR2 (100),
      saa_description   tqc_sys_applicable_areas.saa_description%TYPE
   );

   TYPE sys_app_area_ref IS REF CURSOR
      RETURN sys_app_area_rec;

   PROCEDURE getsystemapparea (
      v_saa_sys_code           tqc_sys_applicable_areas.saa_sys_code%TYPE,
      v_sysapparea_cur   OUT   sys_app_area_ref
   );

   PROCEDURE get_branch_print_servers (
      v_cursor   OUT      print_server_ref,
      brn_code   IN       NUMBER,
      div_code   IN       NUMBER
   );

   TYPE mod_subunits_rec IS RECORD (
      tsms_code        tqc_sys_mod_subunits.tsms_code%TYPE,
      tsms_gsm_code    tqc_sys_mod_subunits.tsms_tsm_code%TYPE,
      tsms_sht_desc    tqc_sys_mod_subunits.tsms_sht_desc%TYPE,
      tsms_desc        tqc_sys_mod_subunits.tsms_desc%TYPE,
      tsms_order       tqc_sys_mod_subunits.tsms_order%TYPE,
      tsms_prod_code   tqc_sys_mod_subunits.tsms_prod_code%TYPE,
      pro_desc         lms_products.prod_desc%TYPE,
      tsm_desc         tqc_system_modules.tsm_desc%TYPE
   );

   TYPE mod_subunits_ref IS REF CURSOR
      RETURN mod_subunits_rec;

   FUNCTION get_modules_subunits (v_prod_code IN NUMBER)
      RETURN mod_subunits_ref;

   TYPE mod_subunits_det_rec IS RECORD (
      tsmsd_code                 tqc_sys_mod_subunits_details.tsmsd_code%TYPE,
      tsmsd_name                 tqc_sys_mod_subunits_details.tsmsd_name%TYPE,
      tsmsd_prompt               tqc_sys_mod_subunits_details.tsmsd_prompt%TYPE,
      tsmsd_type                 tqc_sys_mod_subunits_details.tsmsd_type%TYPE,
      tsmsd_order                tqc_sys_mod_subunits_details.tsmsd_order%TYPE,
      tsmsd_parent               tqc_sys_mod_subunits_details.tsmsd_parent%TYPE,
      tsmsd_more_dtls_appl       tqc_sys_mod_subunits_details.tsmsd_more_dtls_appl%TYPE,
      tsmsd_more_dtls            tqc_sys_mod_subunits_details.tsmsd_more_dtls%TYPE,
      tsmsd_root                 tqc_sys_mod_subunits_details.tsmsd_root%TYPE,
      tsmsd_more_dtls_required   tqc_sys_mod_subunits_details.tsmsd_more_dtls_required%TYPE,
      tsmsd_tmsc_code            tqc_sys_mod_subunits_details.tsmsd_tmsc_code%TYPE,
      tsmc_desc                  tqc_mod_sys_columns.tmsc_desc%TYPE
   );

   TYPE mod_subunits_det_ref IS REF CURSOR
      RETURN mod_subunits_det_rec;

   FUNCTION get_mod_subunits_det (v_code IN NUMBER)
      RETURN mod_subunits_det_ref;

   TYPE proposal_sched_mod_rec IS RECORD (
      tsm_code       tqc_system_modules.tsm_code%TYPE,
      tsm_sht_desc   tqc_system_modules.tsm_sht_desc%TYPE,
      tsm_desc       tqc_system_modules.tsm_desc%TYPE
   );

   TYPE proposal_sched_mod_ref IS REF CURSOR
      RETURN proposal_sched_mod_rec;

   FUNCTION get_proposal_mod_systems
      RETURN proposal_sched_mod_ref;

   TYPE subunits_options_rec IS RECORD (
      tsso_code          tqc_sys_subunits_options.tsso_code%TYPE,
      tsso_tsmsd_code    tqc_sys_subunits_options.tsso_tsmsd_code%TYPE,
      tsso_option_name   tqc_sys_subunits_options.tsso_option_name%TYPE,
      tsso_option_desc   tqc_sys_subunits_options.tsso_option_desc%TYPE,
      tsso_order         tqc_sys_subunits_options.tsso_order%TYPE,
      tsso_type          tqc_sys_subunits_options.tsso_type%TYPE
   );

   TYPE subunits_options_ref IS REF CURSOR
      RETURN subunits_options_rec;

   FUNCTION get_subunits_options (v_code IN NUMBER)
      RETURN subunits_options_ref;

   TYPE mapping_columns_rec IS RECORD (
      tmsc_code   tqc_mod_sys_columns.tmsc_code%TYPE,
      tmsc_desc   tqc_mod_sys_columns.tmsc_desc%TYPE
   );

   TYPE mapping_columns_ref IS REF CURSOR
      RETURN mapping_columns_rec;

   FUNCTION getsysmappingcolmns (v_syscode IN NUMBER)
      RETURN mapping_columns_ref;

   TYPE mail_settings_rec IS RECORD (
      mail_type          tqc_system_mails.mail_type%TYPE,
      mail_server_name   tqc_system_mails.mail_server_name%TYPE,
      mail_host          tqc_system_mails.mail_host%TYPE,
      mail_username      tqc_system_mails.mail_username%TYPE,
      mail_password      tqc_system_mails.mail_password%TYPE,
      mail_port          tqc_system_mails.mail_port%TYPE,
      mail_email         tqc_system_mails.mail_email%TYPE,
      mail_in_out        tqc_system_mails.mail_in_out%TYPE,
      mail_secure        tqc_system_mails.mail_secure%TYPE
   );

   TYPE mail_settings_ref IS REF CURSOR
      RETURN mail_settings_rec;

   FUNCTION getincomingmailsettings
      RETURN mail_settings_ref;

   FUNCTION getoutgoingmailsettings
      RETURN mail_settings_ref;

   TYPE alert_rec IS RECORD (
      qt_job_name               qrtz_triggers.qt_job_name%TYPE,
      qt_description            qrtz_triggers.qt_description%TYPE,
      qt_next_fire_time         qrtz_triggers.qt_next_fire_time%TYPE,
      qt_prev_fire_time         qrtz_triggers.qt_prev_fire_time%TYPE,
      qt_start_time             qrtz_triggers.qt_start_time%TYPE,
      qt_end_time               qrtz_triggers.qt_end_time%TYPE,
      qt_code                   qrtz_triggers.qt_code%TYPE,
      qt_sys_code               qrtz_triggers.qt_sys_code%TYPE,
      qt_recurrence             qrtz_triggers.qt_recurrence%TYPE,
      qt_recurrence_type        qrtz_triggers.qt_recurrence_type%TYPE,
      qt_job_assignee           qrtz_triggers.qt_job_assignee%TYPE,
      qt_notified_fail_user     qrtz_triggers.qt_notified_fail_user%TYPE,
      qt_notified_succ_user     qrtz_triggers.qt_notified_succ_user%TYPE,
      qt_reccurence_interval    qrtz_triggers.qt_reccurence_interval%TYPE,
      qt_job_type               qrtz_triggers.qt_job_type%TYPE,
      qt_job_template           qrtz_triggers.qt_job_template%TYPE,
      qt_fail_notify_template   qrtz_triggers.qt_fail_notify_template%TYPE,
      qt_succ_notify_template   qrtz_triggers.qt_succ_notify_template%TYPE,
      assignee                  tqc_users.usr_username%TYPE,
      fail_user                 tqc_users.usr_username%TYPE,
      succ_user                 tqc_users.usr_username%TYPE,
      job_template              VARCHAR2 (200),
      fail_notify_template      VARCHAR2 (200),
      succ_notify_template      VARCHAR2 (200),
      assignee_email            VARCHAR2 (200),
      fail_email                VARCHAR2 (200),
      succ_email                VARCHAR2 (200),
      qt_status                 VARCHAR2 (1),
      qt_threshold_type         VARCHAR2 (1),
      qt_threshold_value        NUMBER,
      usr_type                  tqc_users.usr_type%TYPE,
      qt_cron_expression        qrtz_triggers.qt_cron_expression%TYPE
   );

   TYPE alert_cursor IS REF CURSOR
      RETURN alert_rec;

   FUNCTION get_alerts (
      v_jobname   VARCHAR2 DEFAULT NULL,
      v_syscode   NUMBER DEFAULT NULL
   )
      RETURN alert_cursor;

   TYPE exec_object_rec IS RECORD (
      obj_type       VARCHAR2 (5),
      obj_code       NUMBER,
      obj_desc       VARCHAR2 (50),
      obj_location   VARCHAR2 (100)
   );

   TYPE exec_object_ref IS REF CURSOR
      RETURN exec_object_rec;

   FUNCTION getexecutionobjects (
      v_syscode   NUMBER,
      v_type      VARCHAR2,
      v_objcode   NUMBER DEFAULT NULL
   )
      RETURN exec_object_ref;

   TYPE households_rec IS RECORD (
      hh_id             tqc_households.hh_id%TYPE,
      hh_name           tqc_households.hh_name%TYPE,
      hh_created_by     tqc_users.usr_username%TYPE,
      hh_date_created   tqc_households.hh_date_created%TYPE,
      hh_category       tqc_households.hh_category%TYPE
   );

   TYPE households_ref IS REF CURSOR
      RETURN households_rec;

   FUNCTION gethouseholds
      RETURN households_ref;

   TYPE householddtls_rec IS RECORD (
      hh_name            tqc_households.hh_name%TYPE,
      hh_id              tqc_households.hh_id%TYPE,
      hhd_clnt_code      tqc_household_dtls.hhd_clnt_code%TYPE,
      clnt_sht_desc      tqc_clients.clnt_sht_desc%TYPE,
      clnt_name          tqc_clients.clnt_name%TYPE,
      clnt_other_names   tqc_clients.clnt_other_names%TYPE
   );

   TYPE householddtls_ref IS REF CURSOR
      RETURN householddtls_rec;

   FUNCTION gethouseholdmembers (v_hhid tqc_households.hh_id%TYPE)
      RETURN householddtls_ref;

   TYPE direct_debits_rec IS RECORD (
      ddd_code             NUMBER,
      policy_no            VARCHAR2 (50),
      account_no           VARCHAR2 (50),
      sort_code            VARCHAR2 (50),
      amount               NUMBER,
      account_name         VARCHAR2 (50),
      narration            VARCHAR2 (50),
      company              VARCHAR2 (50),
      bbr_ref_code         tqc_bank_branches.bbr_ref_code%TYPE,
      ddd_prem_due_date    tqc_direct_debit_detail.ddd_prem_due_date%TYPE,
      ddd_pol_freq_pymnt   VARCHAR2 (50),
      dd_ref_no            tqc_direct_debit.dd_ref_no%TYPE,
      bank_branch          VARCHAR2 (200),
      debit_day            VARCHAR2 (200),
      monthdesc            VARCHAR2 (50)
   );

   TYPE direct_debits_ref IS REF CURSOR
      RETURN direct_debits_rec;

   FUNCTION getdirectdebitsreport (v_ddcode NUMBER, v_query NUMBER)
      RETURN direct_debits_ref;

   TYPE debit_data_rec IS RECORD (
      debit_data   VARCHAR2 (3000),
      report_no    VARCHAR2 (100)
   );

   TYPE debit_data_ref IS REF CURSOR
      RETURN debit_data_rec;

   --FUNCTION getDirectDebitsData  (v_ddCode NUMBER,v_refNo  VARCHAR2,v_bankCode VARCHAR2,v_bbrCode  VARCHAR2,v_query    NUMBER)RETURN debit_data_ref;
   FUNCTION getdirectdebitsdata (
      v_ddcode     NUMBER,
      v_refno      VARCHAR2,
      v_bankcode   VARCHAR2,
      v_bbrcode    VARCHAR2,
      v_query      NUMBER
   )
      RETURN debit_data_ref;

   TYPE towns_with_zip_rec IS RECORD (
      twn_code         tqc_towns.twn_code%TYPE,
      twn_cou_code     tqc_towns.twn_cou_code%TYPE,
      twn_sht_desc     tqc_towns.twn_sht_desc%TYPE,
      twn_name         tqc_towns.twn_name%TYPE,
      twn_state_code   tqc_states.sts_code%TYPE,
      pst_desc         tqc_postal_codes.pst_desc%TYPE,
      pst_zip_code     tqc_postal_codes.pst_zip_code%TYPE
   );

   TYPE towns_with_zip_ref IS REF CURSOR
      RETURN towns_with_zip_rec;

   FUNCTION gettownswithzipcodebystate (v_state_code IN NUMBER)
      RETURN towns_with_zip_ref;

   TYPE ecm_setups_rec IS RECORD (
      est_code           tqc_ecm_setups.est_code%TYPE,
      est_ecm_url        tqc_ecm_setups.est_ecm_url%TYPE,
      est_ecm_username   tqc_ecm_setups.est_ecm_username%TYPE,
      est_ecm_password   tqc_ecm_setups.est_ecm_password%TYPE,
      est_sys_code       tqc_ecm_setups.est_sys_code%TYPE,
      est_root_folder    tqc_ecm_setups.est_root_folder%TYPE,
      sys_name           tqc_systems.sys_name%TYPE
   );

   TYPE ecm_setups_ref IS REF CURSOR
      RETURN ecm_setups_rec;

   FUNCTION get_ecm_setups
      RETURN ecm_setups_ref;

   FUNCTION get_system_ecm_setups (v_sys_code NUMBER)
      RETURN ecm_setups_ref;

   TYPE ecm_systems_rec IS RECORD (
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      sys_name       tqc_systems.sys_name%TYPE
   );

   TYPE ecm_systems_ref IS REF CURSOR
      RETURN ecm_systems_rec;

   TYPE ecm_processes_rec IS RECORD (
      sprc_code      tqc_sys_processes.sprc_code%TYPE,
      sprc_process   tqc_sys_processes.sprc_process%TYPE
   );

   TYPE ecm_processes_ref IS REF CURSOR
      RETURN ecm_processes_rec;

   FUNCTION get_ecm_systems
      RETURN ecm_systems_ref;

   FUNCTION get_ecm_processes (v_sys_code NUMBER)
      RETURN ecm_processes_ref;

   TYPE system_reports_rec IS RECORD (
      rpt_code          tqc_system_reports.rpt_code%TYPE,
      rpt_name          tqc_system_reports.rpt_name%TYPE,
      rpt_description   tqc_system_reports.rpt_description%TYPE
   );

   TYPE system_reports_ref IS REF CURSOR
      RETURN system_reports_rec;

   FUNCTION get_system_reports (v_sys_code NUMBER)
      RETURN system_reports_ref;

   TYPE sys_reports_rec IS RECORD (
      sprr_code          tqc_sys_process_reports.sprr_code%TYPE,
      sprr_rpt_code      tqc_sys_process_reports.sprr_rpt_code%TYPE,
      sprr_sprc_code     tqc_sys_process_reports.sprr_sprc_code%TYPE,
      sprr_desc          tqc_sys_process_reports.sprr_desc%TYPE,
      rpt_name           tqc_system_reports.rpt_name%TYPE,
      rpt_description    tqc_system_reports.rpt_description%TYPE,
      sprr_type          tqc_sys_process_reports.sprr_type%TYPE,
      sdt_content_name   tqc_sys_doc_types.sdt_content_name%TYPE,
      sdt_code           tqc_sys_doc_types.sdt_code%TYPE,
      sdt_content_type   tqc_sys_doc_types.sdt_content_type%TYPE
   );

   TYPE sys_reports_ref IS REF CURSOR
      RETURN sys_reports_rec;

   FUNCTION get_process_reports (v_proc_code NUMBER)
      RETURN sys_reports_ref;

   TYPE ecm_doc_types_rec IS RECORD (
      sdt_code           tqc_sys_doc_types.sdt_code%TYPE,
      sdt_content_name   tqc_sys_doc_types.sdt_content_name%TYPE,
      sdt_content_desc   tqc_sys_doc_types.sdt_content_desc%TYPE,
      sdt_content_type   tqc_sys_doc_types.sdt_content_type%TYPE
   );

   TYPE ecm_doc_types_ref IS REF CURSOR
      RETURN ecm_doc_types_rec;

   FUNCTION get_ecm_doc_types
      RETURN ecm_doc_types_ref;

   TYPE content_metadata_rec IS RECORD (
      ddm_code       tqc_dms_doc_metadata.ddm_code%TYPE,
      ddm_sdt_code   tqc_dms_doc_metadata.ddm_sdt_code%TYPE,
      ddm_name       tqc_dms_doc_metadata.ddm_name%TYPE,
      ddm_desc       tqc_dms_doc_metadata.ddm_desc%TYPE
   );

   TYPE content_metadata_ref IS REF CURSOR
      RETURN content_metadata_rec;

   FUNCTION get_content_metadata (v_sprr_code NUMBER)
      RETURN content_metadata_ref;

   FUNCTION get_process_report (v_proc_area VARCHAR2, v_sys_code NUMBER)
      RETURN sys_reports_ref;

   TYPE branch_data_rec IS RECORD (
      bbb_code                 tqc_bank_branches_branches.bbb_code%TYPE,
      bbb_brn_code             tqc_bank_branches_branches.bbb_brn_code%TYPE,
      bbb_brn_sht_desc         tqc_bank_branches_branches.bbb_brn_sht_desc%TYPE,
      bbb_brn_reg_code         tqc_bank_branches_branches.bbb_brn_reg_code%TYPE,
      bbb_brn_name             tqc_bank_branches_branches.bbb_brn_name%TYPE,
      bbb_brn_phy_addrs        tqc_bank_branches_branches.bbb_brn_phy_addrs%TYPE,
      bbb_bbr_code             tqc_bank_branches_branches.bbb_bbr_code%TYPE,
      bbb_bbr_bnk_code         tqc_bank_branches_branches.bbb_bbr_bnk_code%TYPE,
      bbb_bbr_branch_name      tqc_bank_branches_branches.bbb_bbr_branch_name%TYPE,
      bbb_bbr_sht_desc         tqc_bank_branches_branches.bbb_bbr_sht_desc%TYPE,
      bbb_bbr_physical_addrs   tqc_bank_branches_branches.bbb_bbr_physical_addrs%TYPE
   );

   TYPE branch_data_ref IS REF CURSOR
      RETURN branch_data_rec;

   FUNCTION get_branches_details (v_bbrcode NUMBER)
      RETURN branch_data_ref;

   TYPE all_branches_ref IS REF CURSOR
      RETURN branches_rec;

   PROCEDURE all_branches (v_all_branches_ref OUT all_branches_ref);

   TYPE bank_branch_rec IS RECORD (
      bbr_code             tqc_bank_branches.bbr_code%TYPE,
      bbr_bnk_code         tqc_bank_branches.bbr_bnk_code%TYPE,
      bbr_branch_name      tqc_bank_branches.bbr_branch_name%TYPE,
      bbr_remarks          tqc_bank_branches.bbr_remarks%TYPE,
      bbr_sht_desc         tqc_bank_branches.bbr_sht_desc%TYPE,
      bbr_ref_code         tqc_bank_branches.bbr_ref_code%TYPE,
      bbr_eft_supported    tqc_bank_branches.bbr_eft_supported%TYPE,
      bbr_dd_supported     tqc_bank_branches.bbr_dd_supported%TYPE,
      bbr_date_created     tqc_bank_branches.bbr_date_created%TYPE,
      bbr_created_by       tqc_bank_branches.bbr_created_by%TYPE,
      bbr_physical_addrs   tqc_bank_branches.bbr_physical_addrs%TYPE
   );

   TYPE sms_messagesrec IS RECORD (
      sms_code             tqc_sms_messages.sms_code%TYPE,
      sms_sys_code         tqc_sms_messages.sms_sys_code%TYPE,
      tsm_desc             tqc_system_modules.tsm_desc%TYPE,
      sms_clnt_code        tqc_sms_messages.sms_clnt_code%TYPE,
      sms_agn_code         tqc_sms_messages.sms_agn_code%TYPE,
      sms_pol_no           tqc_sms_messages.sms_pol_no%TYPE,
      sms_pol_code         tqc_sms_messages.sms_pol_code%TYPE,
      sms_clm_no           tqc_sms_messages.sms_clm_no%TYPE,
      sms_tel_no           tqc_sms_messages.sms_tel_no%TYPE,
      sms_msg              tqc_sms_messages.sms_msg%TYPE,
      sms_status           tqc_sms_messages.sms_status%TYPE,
      sms_prepared_by      tqc_sms_messages.sms_prepared_by%TYPE,
      sms_send_date        tqc_sms_messages.sms_send_date%TYPE,
      pol_current_status   gin_policies.pol_current_status%TYPE,
      clnt_name            tqc_clients.clnt_name%TYPE,
      cou_code             tqc_countries.cou_code%TYPE,
      cou_zip_code         tqc_countries.cou_zip_code%TYPE,
      div_name             tqc_divisions.div_name%TYPE,
      sms_prepared_date    tqc_sms_messages.sms_prepared_date%TYPE
      
   );

   TYPE allmessagesref IS REF CURSOR
      RETURN sms_messagesrec;

   TYPE all_bank_branches_ref IS REF CURSOR
      RETURN bank_branch_rec;

   TYPE allbankbranchesref IS REF CURSOR
      RETURN bank_branch_rec;

   FUNCTION all_bank__branches (v_branch_name VARCHAR2)
      RETURN all_bank_branches_ref;

   PROCEDURE allbankbranches (v_bankbranchref OUT allbankbranchesref);

   FUNCTION selectallmessages (
      v_sms_sys_code     VARCHAR2,
      v_sms_wef          DATE,
      v_sms_wet          DATE,
      v_sms_trans_type   VARCHAR2
   )
      RETURN allmessagesref;

   TYPE reserved_words_rec IS RECORD (
      tsrw_code        tqc_sys_reserved_words.tsrw_code%TYPE,
      tsrw_sys_code    tqc_sys_reserved_words.tsrw_sys_code%TYPE,
      tsrw_tsrc_code   tqc_sys_reserved_words.tsrw_tsrc_code%TYPE,
      tsrw_type        tqc_sys_reserved_words.tsrw_type%TYPE,
      tsrw_editable    tqc_sys_reserved_words.tsrw_editable%TYPE,
      tsrc_name        tqc_serv_req_cat.tsrc_name%TYPE,
      tsrw_name        tqc_sys_reserved_words.tsrw_name%TYPE,
      tsrw_desc        tqc_sys_reserved_words.tsrw_desc%TYPE,
      tsrc_validity    tqc_serv_req_cat.tsrc_validity%TYPE,
      tsrc_usr_code    tqc_serv_req_cat.tsrc_usr_code%TYPE,
      usr_type         tqc_users.usr_type%TYPE,
      usr_email        tqc_users.usr_email%TYPE
   );

   TYPE reserved_words_ref IS REF CURSOR
      RETURN reserved_words_rec;

   FUNCTION getreservedwords (v_syscode NUMBER)
      RETURN reserved_words_ref;

   TYPE sys_processes_rec IS RECORD (
      deployment      NUMBER,
      jsd_sys_code    jbpm4_sys_deployments.jsd_sys_code%TYPE,
      jsd_jpdl_name   jbpm4_sys_deployments.jsd_jpdl_name%TYPE
   );

   TYPE sys_processes_ref IS REF CURSOR
      RETURN sys_processes_rec;

   FUNCTION getsystemprocesses (
      v_syscode   tqc_sys_processes.sprc_sys_code%TYPE
   )
      RETURN sys_processes_ref;

   TYPE escalations_rec IS RECORD (
      tsel_code            tqc_sys_escalation_levels.tsel_code%TYPE,
      tsel_jsd_sys_code    tqc_sys_escalation_levels.tsel_jsd_sys_code%TYPE,
      tsel_jsd_jpdl_name   tqc_sys_escalation_levels.tsel_jsd_jpdl_name%TYPE,
      tsel_activity_name   tqc_sys_escalation_levels.tsel_activity_name%TYPE,
      tsel_level           tqc_sys_escalation_levels.tsel_level%TYPE,
      tsel_assignee        tqc_sys_escalation_levels.tsel_assignee%TYPE,
      usr_username         tqc_users.usr_username%TYPE,
      tsel_duration        tqc_sys_escalation_levels.tsel_duration%TYPE,
      tsel_cc              tqc_sys_escalation_levels.tsel_cc%TYPE,
      cc_username          tqc_users.usr_username%TYPE
   );

   TYPE escalations_ref IS REF CURSOR
      RETURN escalations_rec;

   FUNCTION getescalations (
      v_syscode    tqc_sys_escalation_levels.tsel_jsd_sys_code%TYPE,
      v_jpdlname   tqc_sys_escalation_levels.tsel_jsd_jpdl_name%TYPE,
      v_activity   tqc_sys_escalation_levels.tsel_activity_name%TYPE,
      v_level      tqc_sys_escalation_levels.tsel_level%TYPE DEFAULT NULL
   )
      RETURN escalations_ref;

   TYPE checkif_losstime_required_rec IS RECORD (
      param_value   tqc_parameters.param_value%TYPE
   );

   TYPE checkif_losstime_required_ref IS REF CURSOR
      RETURN checkif_losstime_required_rec;

   FUNCTION checkif_losstime_required
      RETURN checkif_losstime_required_ref;

   FUNCTION checkif_occupation_required
      RETURN checkif_losstime_required_ref;

   TYPE select_parent_agency_rec IS RECORD (
      agn_code       tqc_agencies.agn_code%TYPE,
      agn_sht_desc   tqc_agencies.agn_sht_desc%TYPE,
      agn_name       tqc_agencies.agn_name%TYPE
   );

   TYPE select_parent_agency_ref IS REF CURSOR
      RETURN select_parent_agency_rec;

   PROCEDURE select_parent_agency (
      v_cursor     OUT      select_parent_agency_ref,
      v_act_code   IN       NUMBER
   );

   TYPE select_subagents_datails_rec IS RECORD (
      agn_code               tqc_agencies.agn_code%TYPE,
      agn_act_code           tqc_agencies.agn_act_code%TYPE,
      agn_sht_desc           tqc_agencies.agn_sht_desc%TYPE,
      agn_name               tqc_agencies.agn_name%TYPE,
      agn_physical_address   tqc_agencies.agn_physical_address%TYPE,
      agn_postal_address     tqc_agencies.agn_postal_address%TYPE,
      agn_email_address      tqc_agencies.agn_email_address%TYPE,
      agn_subagent           tqc_agencies.agn_subagent%TYPE,
      agn_main_agn_code      tqc_agencies.agn_main_agn_code%TYPE,
      act_account_type       tqc_account_types.act_account_type%TYPE
   );

   TYPE select_subagents_datails_ref IS REF CURSOR
      RETURN select_subagents_datails_rec;

   PROCEDURE select_subagents_datails (
      v_cursor     OUT      select_subagents_datails_ref,
      v_agn_code   IN       NUMBER
   );

   TYPE checkif_serialno_required_rec IS RECORD (
      param_value   tqc_parameters.param_value%TYPE
   );

   TYPE checkif_serialno_required_ref IS REF CURSOR
      RETURN checkif_serialno_required_rec;

   FUNCTION checkif_serialno_required
      RETURN checkif_serialno_required_ref;

   TYPE sms_settings_rec IS RECORD (
      tss_desc       tqc_system_sms.tss_desc%TYPE,
      tss_url        tqc_system_sms.tss_url%TYPE,
      tss_username   tqc_system_sms.tss_username%TYPE,
      tss_password   tqc_system_sms.tss_password%TYPE,
      tss_source     tqc_system_sms.tss_source%TYPE,
      tss_code       tqc_system_sms.tss_code%TYPE,
      tss_default    tqc_system_sms.tss_default%TYPE
   );

   TYPE sms_settings_ref IS REF CURSOR
      RETURN sms_settings_rec;

   FUNCTION getsmssettings
      RETURN sms_settings_ref;

   FUNCTION getdefaultsmssettings
      RETURN sms_settings_ref;

   TYPE pending_sms_rec IS RECORD (
      sms_code     tqc_sms_messages.sms_code%TYPE,
      sms_tel_no   tqc_sms_messages.sms_tel_no%TYPE,
      sms_msg      tqc_sms_messages.sms_msg%TYPE
   );

   TYPE pending_sms_ref IS REF CURSOR
      RETURN pending_sms_rec;

   FUNCTION getpendingsms
      RETURN pending_sms_ref;

   TYPE email_rec IS RECORD (
      usr_email   tqc_users.usr_email%TYPE
   );

   TYPE email_ref IS REF CURSOR
      RETURN email_rec;

   FUNCTION getgroupedusersemails (v_gusr_grp_usr_code NUMBER)
      RETURN email_ref;

   FUNCTION checkifsaccorequired
      RETURN checkif_dob_required_ref;

   TYPE marketers_rec IS RECORD (
      agn_code       tqc_agencies.agn_code%TYPE,
      agn_act_code   tqc_agencies.agn_act_code%TYPE,
      agn_sht_desc   tqc_agencies.agn_sht_desc%TYPE,
      agn_name       tqc_agencies.agn_name%TYPE
   );

   TYPE marketers_ref IS REF CURSOR
      RETURN marketers_rec;

   FUNCTION getmarketers
      RETURN marketers_ref;

   TYPE webclientsbranchesrec IS RECORD (
      tcb_code        tqc_client_branches.tcb_code%TYPE,
      tcb_clnt_code   tqc_client_branches.tcb_clnt_code%TYPE,
      tcb_sht_desc    tqc_client_branches.tcb_sht_desc%TYPE,
      tcb_name        tqc_client_branches.tcb_name%TYPE
   );

   TYPE webclientsbranchesref IS REF CURSOR
      RETURN webclientsbranchesrec;

   TYPE webassignbranchrec IS RECORD (
      tcb_code        tqc_client_branches.tcb_code%TYPE,
      tcb_clnt_code   tqc_client_branches.tcb_clnt_code%TYPE,
      tcb_sht_desc    tqc_client_branches.tcb_sht_desc%TYPE,
      tcb_name        tqc_client_branches.tcb_name%TYPE
   );

   TYPE webassignbranchref IS REF CURSOR
      RETURN webassignbranchrec;

   FUNCTION getwebclientsbranches (v_tcb_clnt_code IN NUMBER)
      RETURN webclientsbranchesref;

   FUNCTION getassignedbranches (v_tcb_clnt_code IN NUMBER)
      RETURN webassignbranchref;

   FUNCTION getunassignedbranches (v_tcb_clnt_code IN NUMBER)
      RETURN webassignbranchref;

   FUNCTION getdefaultbranches
      RETURN webclientsbranchesref;

   TYPE webproductsrec IS RECORD (
      twp_code        tqc_web_products.twp_code%TYPE,
      twp_twpc_code   tqc_web_products.twp_twpc_code%TYPE,
      twp_prod_code   tqc_web_products.twp_prod_code%TYPE,
      twp_prod_desc   tqc_web_products.twp_prod_desc%TYPE,
      pro_desc        gin_products.pro_desc%TYPE
   );

   TYPE webproductsref IS REF CURSOR
      RETURN webproductsrec;

   FUNCTION getwebproducts
      RETURN webproductsref;

   TYPE webusersrec IS RECORD (
      usr_code       tqc_users.usr_code%TYPE,
      usr_username   tqc_users.usr_username%TYPE,
      usr_name       tqc_users.usr_name%TYPE,
      usr_status     tqc_users.usr_status%TYPE
   );

   TYPE webusersref IS REF CURSOR
      RETURN webusersrec;

   FUNCTION getwebusers
      RETURN webusersref;

   TYPE webproductsdtlsrec IS RECORD (
      twpd_clnt_code    tqc_web_product_details.twpd_clnt_code%TYPE,
      twpd_twp_code     tqc_web_product_details.twpd_twp_code%TYPE,
      clnt_name         tqc_clients.clnt_name%TYPE,
      twp_prod_desc     tqc_web_products.twp_prod_desc%TYPE,
      twpd_usr_code     tqc_web_product_details.twpd_usr_code%TYPE,
      twpd_username     tqc_web_product_details.twpd_username%TYPE,
      twpd_dr_limit     tqc_web_product_details.twpd_dr_limit%TYPE,
      twpd_cr_limit     tqc_web_product_details.twpd_cr_limit%TYPE,
      twpd_policy_use   tqc_web_product_details.twpd_policy_use%TYPE,
      twpd_endos_use    tqc_web_product_details.twpd_endos_use%TYPE,
      pro_desc          gin_products.pro_desc%TYPE,
      clna_clnt_code    tqc_client_accounts.clna_clnt_code%TYPE,
      clna_code         tqc_client_accounts.clna_code%TYPE
   );

   TYPE webproductsdtlsref IS REF CURSOR
      RETURN webproductsdtlsrec;

   FUNCTION getwebproductdetails (v_twpd_clnt_code IN NUMBER)
      RETURN webproductsdtlsref;

   PROCEDURE get_allclient_titles (v_client_titles_ref OUT client_titles_ref);

   PROCEDURE get_client_titles (v_client_titles_ref OUT client_titles_ref);

   TYPE allsystems_rec IS RECORD (
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      sys_name       tqc_systems.sys_name%TYPE
   );

   TYPE allsystems_ref IS REF CURSOR
      RETURN allsystems_rec;

   TYPE dispatch_docs_rec IS RECORD (
      docr_id              tqc_documents_register.docr_id%TYPE,
      docr_doc_name        tqc_documents_register.docr_doc_name%TYPE,
      docr_doc_url         tqc_documents_register.docr_doc_author%TYPE,
      docr_doc_author      tqc_documents_register.docr_doc_author%TYPE,
      docr_doc_desc        tqc_documents_register.docr_doc_desc%TYPE,
      docr_clnt_code       tqc_documents_register.docr_clnt_code%TYPE,
      docr_pol_policy_no   tqc_documents_register.docr_pol_policy_no%TYPE,
      docr_claim_no        tqc_documents_register.docr_claim_no%TYPE,
      docr_quot_code       tqc_documents_register.docr_quot_code%TYPE,
      docr_level           tqc_documents_register.docr_level%TYPE,
      docr_sys_code        tqc_documents_register.docr_sys_code%TYPE,
      docr_pol_batch_no    tqc_documents_register.docr_pol_batch_no%TYPE,
      docr_date_created    tqc_documents_register.docr_date_created%TYPE,
      docr_agn_code        tqc_documents_register.docr_agn_code%TYPE,
      docr_dispatched      tqc_documents_register.docr_dispatched%TYPE,
      docr_dispatch_dt     tqc_documents_register.docr_dispatch_dt%TYPE,
      docr_dispatchable    tqc_documents_register.docr_dispatchable%TYPE
   );

   TYPE dispatch_docs_ref IS REF CURSOR
      RETURN dispatch_docs_rec;

   FUNCTION get_dispatch_docs (
      v_batch       NUMBER,
      v_clnt_code   NUMBER,
      v_agn_code    NUMBER
   )
      RETURN dispatch_docs_ref;

   TYPE sms_templates_rec IS RECORD (
      msgt_code         tqc_msg_templates.msgt_code%TYPE,
      msgt_sht_desc     tqc_msg_templates.msgt_sht_desc%TYPE,
      msgt_msg          tqc_msg_templates.msgt_msg%TYPE,
      msgt_sys_code     tqc_msg_templates.msgt_sys_code%TYPE,
      msgt_sys_module   tqc_msg_templates.msgt_sys_module%TYPE,
      msgt_type         tqc_msg_templates.msgt_type%TYPE
   );

   TYPE sms_templates_ref IS REF CURSOR
      RETURN sms_templates_rec;

   FUNCTION get_sms_template
      RETURN sms_templates_ref;

   FUNCTION get_email_template
      RETURN sms_templates_ref;

   TYPE req_doc_rec IS RECORD (
      rdoc_id            gin_reqrd_documents.rdoc_id%TYPE,
      rdoc_sht_desc      gin_reqrd_documents.rdoc_sht_desc%TYPE,
      rdoc_desc          gin_reqrd_documents.rdoc_desc%TYPE,
      rdoc_mandtry       gin_reqrd_documents.rdoc_mandtry%TYPE,
      rdoc_nb_doc        gin_reqrd_documents.rdoc_nb_doc%TYPE,
      rdoc_en_doc        gin_reqrd_documents.rdoc_en_doc%TYPE,
      rdoc_rn_doc        gin_reqrd_documents.rdoc_rn_doc%TYPE,
      rdoc_cert_doc      gin_reqrd_documents.rdoc_cert_doc%TYPE,
      rdoc_clm_lop_doc   gin_reqrd_documents.rdoc_clm_lop_doc%TYPE,
      rdoc_clm_pay_doc   gin_reqrd_documents.rdoc_clm_pay_doc%TYPE,
      rdoc_valid_prd     gin_reqrd_documents.rdoc_valid_prd%TYPE,
      rqd_code           tqc_required_docs.rqd_code%TYPE
   );

   TYPE req_doc_ref IS REF CURSOR
      RETURN req_doc_rec;

   FUNCTION getsystemreqdocs (v_system_code IN NUMBER)
      RETURN req_doc_ref;

   TYPE mob_prefix_rec IS RECORD (
      mptp_code            tqc_mob_pymnt_types_prefixes.mptp_code%TYPE,
      mptp_mob_no_prefix   tqc_mob_pymnt_types_prefixes.mptp_mob_no_prefix%TYPE,
      mptp_mpt_code        tqc_mob_pymnt_types_prefixes.mptp_mpt_code%TYPE
   );

   TYPE mob_prefix_ref IS REF CURSOR
      RETURN mob_prefix_rec;

   FUNCTION get_available_prefixes
      RETURN mob_prefix_ref;

   TYPE scheduledjobs_rec IS RECORD (
      qt_job_name          qrtz_triggers.qt_job_name%TYPE,
      qt_description       qrtz_triggers.qt_description%TYPE,
      qt_start_time        qrtz_triggers.qt_start_time%TYPE,
      qt_end_time          qrtz_triggers.qt_end_time%TYPE,
      qt_cron_expression   qrtz_triggers.qt_cron_expression%TYPE
   );

   TYPE scheduledjobs_ref IS REF CURSOR
      RETURN scheduledjobs_rec;

   FUNCTION getscheduledjobs
      RETURN scheduledjobs_ref;

   TYPE policiesdueforren_rec IS RECORD (
      pol_policy_no         gin_policies.pol_policy_no%TYPE,
      pol_policy_cover_to   gin_policies.pol_policy_cover_to%TYPE,
      pol_renewal_dt        gin_policies.pol_renewal_dt%TYPE,
      clnt_code             tqc_clients.clnt_code%TYPE,
      client                VARCHAR2 (200),
      clnt_sms_tel          tqc_clients.clnt_sms_tel%TYPE,
      pol_agnt_agent_code   gin_policies.pol_agnt_agent_code%TYPE
   );

   TYPE policiesdueforren_ref IS REF CURSOR
      RETURN policiesdueforren_rec;

   FUNCTION getpoliciestwodaysren
      RETURN policiesdueforren_ref;

   FUNCTION getpoliciestwomonthsren
      RETURN policiesdueforren_ref;

   FUNCTION getpoliciesonemonthsren
      RETURN policiesdueforren_ref;

   FUNCTION getagencysystems
      RETURN all_systems_ref;

   TYPE clientbirhdays_rec IS RECORD (
      clnt_code      tqc_clients.clnt_code%TYPE,
      clientname     VARCHAR2 (200),
      clnt_dob       tqc_clients.clnt_dob%TYPE,
      clnt_sms_tel   tqc_clients.clnt_sms_tel%TYPE
   );

   TYPE clientbirhdays_ref IS REF CURSOR
      RETURN clientbirhdays_rec;

   FUNCTION getclientbirthdays
      RETURN clientbirhdays_ref;

   TYPE branchunits_rec IS RECORD (
      bru_code       tqc_branch_units.bru_code%TYPE,
      bru_brn_code   tqc_branch_units.bru_brn_code%TYPE,
      bru_sht_desc   tqc_branch_units.bru_sht_desc%TYPE,
      bru_name       tqc_branch_units.bru_name%TYPE
   );

   TYPE branchunits_ref IS REF CURSOR
      RETURN branchunits_rec;

   FUNCTION getbranchunits
      RETURN branchunits_ref;
   
   TYPE branchUnitsPerRegion_rec IS RECORD (
      bru_code       tqc_branch_units.bru_code%TYPE,
      bru_brn_code   tqc_branch_units.bru_brn_code%TYPE,
      bru_sht_desc   tqc_branch_units.bru_sht_desc%TYPE,
      bru_name       tqc_branch_units.bru_name%TYPE
   );

   TYPE branchUnitsPerRegion_ref IS REF CURSOR
      RETURN branchUnitsPerRegion_rec;

   FUNCTION getBranchUnitsPerRegion(v_region_code NUMBER)
      RETURN branchUnitsPerRegion_ref;

   FUNCTION get_debit_day (v_type VARCHAR2, v_value VARCHAR2)
      RETURN VARCHAR2;

   TYPE service_provider_rec IS RECORD (
      spr_code               tqc_service_providers.spr_code%TYPE,
      spr_sht_desc           tqc_service_providers.spr_sht_desc%TYPE,
      spr_name               tqc_service_providers.spr_name%TYPE,
      spr_physical_address   tqc_service_providers.spr_physical_address%TYPE,
      spr_postal_address     tqc_service_providers.spr_postal_address%TYPE,
      spr_phone              tqc_service_providers.spr_phone%TYPE,
      spr_fax                tqc_service_providers.spr_fax%TYPE,
      spr_email              tqc_service_providers.spr_email%TYPE,
      spr_created_by         tqc_service_providers.spr_created_by%TYPE,
      spr_date_created       tqc_service_providers.spr_date_created%TYPE,
      spr_status_remarks     tqc_service_providers.spr_status_remarks%TYPE,
      spr_status             tqc_service_providers.spr_status%TYPE,
      spr_pin_number         tqc_service_providers.spr_pin_number%TYPE,
      spr_mobile_no          tqc_service_providers.spr_mobile_no%TYPE,
      spr_inhouse            tqc_service_providers.spr_inhouse%TYPE,
      spr_sms_number         tqc_service_providers.spr_sms_number%TYPE
   );

   TYPE service_provider_ref IS REF CURSOR
      RETURN service_provider_rec;

   PROCEDURE getserviceprovider (
      v_spr_name               IN       VARCHAR2,
      v_service_provider_ref   OUT      service_provider_ref,
      v_spr_spt_code           IN       NUMBER
   );

   TYPE systemrpt_rec IS RECORD (
      rpt_code          tqc_system_reports.rpt_code%TYPE,
      rpt_name          tqc_system_reports.rpt_name%TYPE,
      rpt_description   tqc_system_reports.rpt_description%TYPE,
      rpt_active        tqc_system_reports.rpt_active%TYPE
   );

   TYPE systemrpt_ref IS REF CURSOR
      RETURN systemrpt_rec;

   FUNCTION getreportsassigned (v_rpt_rsm_code IN NUMBER)
      RETURN systemrpt_ref;

   FUNCTION getreportsunassigned (v_rpt_rsm_code IN NUMBER)
      RETURN systemrpt_ref;

   TYPE systemrptmodules_rec IS RECORD (
      srm_code       tqc_sys_report_modules.srm_code%TYPE,
      srm_name       tqc_sys_report_modules.srm_name%TYPE,
      srm_desc       tqc_sys_report_modules.srm_desc%TYPE,
      srm_sys_code   tqc_sys_report_modules.srm_sys_code%TYPE,
      sys_name       tqc_systems.sys_name%TYPE
   );

   TYPE systemrptmodules_ref IS REF CURSOR
      RETURN systemrptmodules_rec;

   FUNCTION getsystemrptmodules
      RETURN systemrptmodules_ref;

   TYPE systemrptsubmodules_rec IS RECORD (
      rsm_code   tqc_sys_rpt_sub_modules.rsm_code%TYPE,
      rsm_name   tqc_sys_rpt_sub_modules.rsm_name%TYPE,
      rsm_desc   tqc_sys_rpt_sub_modules.rsm_desc%TYPE
   );

   TYPE systemrptsubmodules_ref IS REF CURSOR
      RETURN systemrptsubmodules_rec;

   FUNCTION getsystemrptsubmodules (v_rsm_srm_code IN NUMBER)
      RETURN systemrptsubmodules_ref;

   TYPE campaigns_rec IS RECORD (
      cmp_code     tqc_campaigns.cmp_code%TYPE,
      cmp_date     tqc_campaigns.cmp_date%TYPE,
      cmp_name     tqc_campaigns.cmp_name%TYPE,
      cmp_type     tqc_campaigns.cmp_type%TYPE,
      cmp_status   tqc_campaigns.cmp_status%TYPE
   );

   TYPE campaigns_ref IS REF CURSOR
      RETURN campaigns_rec;

   FUNCTION get_campaigns
      RETURN campaigns_ref;

   TYPE sectors_occup_rec IS RECORD (
      v_sec_code       tqc_sectors.sec_code%TYPE,
      v_sec_sht_desc   tqc_sectors.sec_sht_desc%TYPE,
      v_sec_name       tqc_sectors.sec_name%TYPE,
      occ_name         tqc_occupations.occ_name%TYPE,
      occ_code         tqc_occupations.occ_code%TYPE
   );

   TYPE sectors_occup_ref IS REF CURSOR
      RETURN sectors_occup_rec;

   PROCEDURE get_sectors_occup (v_sectors_ref OUT sectors_occup_ref);

   PROCEDURE get_occupations (v_sect_code NUMBER, v_sectors_ref OUT sectors_ref);

   TYPE all_towns_rec IS RECORD (
      twn_code       tqc_towns.twn_code%TYPE,
      twn_cou_code   tqc_towns.twn_cou_code%TYPE,
      twn_sht_desc   tqc_towns.twn_sht_desc%TYPE,
      twn_name       tqc_towns.twn_name%TYPE
   );

   TYPE all_towns_ref IS REF CURSOR
      RETURN all_towns_rec;

   FUNCTION get_all_towns
      RETURN all_towns_ref;

   FUNCTION get_sms_template (v_msgt_sys_code IN NUMBER)
      RETURN sms_templates_ref;

   FUNCTION get_email_template (v_msgt_sys_code IN NUMBER)
      RETURN sms_templates_ref;

   TYPE allsubsidiary_rec IS RECORD (
      spr_name               VARCHAR2 (200),
      spt_name               VARCHAR2 (200),
      spr_sht_desc           VARCHAR2 (200),
      spr_physical_address   VARCHAR2 (200),
      spr_postal_address     VARCHAR2 (200),
      spr_email              VARCHAR2 (200)
   );

   TYPE allsubsidiary_ref IS REF CURSOR
      RETURN allsubsidiary_rec;

   FUNCTION getallsubsidiary (v_clnt_code IN NUMBER)
      RETURN allsubsidiary_ref;

   TYPE rating_starndards_rec IS RECORD (
      ors_code   tqc_org_rating_starndards.ors_code%TYPE,
      ors_desc   tqc_org_rating_starndards.ors_desc%TYPE
   );

   TYPE rating_starndards_ref IS REF CURSOR
      RETURN rating_starndards_rec;

   FUNCTION get_rating_starndards (v_ors_rorg_code NUMBER)
      RETURN rating_starndards_ref;

   TYPE ratings_rec IS RECORD (
      rorg_code       tqc_rating_organizations.rorg_code%TYPE,
      rorg_desc       tqc_rating_organizations.rorg_desc%TYPE,
      rorg_sht_desc   tqc_rating_organizations.rorg_sht_desc%TYPE
   );

   TYPE ratings_ref IS REF CURSOR
      RETURN ratings_rec;

   FUNCTION get_ratings
      RETURN ratings_ref;

   TYPE allsmsmessages_rec IS RECORD (
      sms_code            tqc_sms_messages.sms_code%TYPE,
      sms_sys_code        tqc_sms_messages.sms_sys_code%TYPE,
      sms_sys_module      tqc_sms_messages.sms_sys_module%TYPE,
      sms_clnt_code       tqc_sms_messages.sms_clnt_code%TYPE,
      sms_agn_code        tqc_sms_messages.sms_agn_code%TYPE,
      sms_pol_code        tqc_sms_messages.sms_pol_code%TYPE,
      sms_pol_no          tqc_sms_messages.sms_pol_no%TYPE,
      sms_clm_no          tqc_sms_messages.sms_clm_no%TYPE,
      sms_tel_no          tqc_sms_messages.sms_tel_no%TYPE,
      sms_msg             tqc_sms_messages.sms_msg%TYPE,
      sms_status          tqc_sms_messages.sms_status%TYPE,
      sms_prepared_by     tqc_sms_messages.sms_prepared_by%TYPE,
      sms_prepared_date   tqc_sms_messages.sms_prepared_date%TYPE,
      sms_send_date       tqc_sms_messages.sms_send_date%TYPE,
      sms_quot_code       tqc_sms_messages.sms_quot_code%TYPE,
      sms_quot_no         tqc_sms_messages.sms_quot_no%TYPE,
      sms_usr_code        tqc_sms_messages.sms_usr_code%TYPE,
      sms_sent_response   tqc_sms_messages.sms_sent_response%TYPE,
      client              VARCHAR2 (200),
      agn_name            tqc_agencies.agn_name%TYPE,
      usr_username        tqc_users.usr_username%TYPE,
      email_address       VARCHAR2 (200),
      email_subj          VARCHAR2 (200)
   );

   TYPE allsmsmessages_ref IS REF CURSOR
      RETURN allsmsmessages_rec;

   FUNCTION getallsmsmessages (
      v_sms_sys_code     IN   NUMBER,
      v_sms_wef               DATE,
      v_sms_wet               DATE,
      v_sms_trans_type        VARCHAR2
   )
      RETURN allmessagesref;

   FUNCTION getallemailmessages (
      v_email_sys_code     IN   NUMBER,
      v_email_status       IN   VARCHAR2,
      v_email_wef               DATE,
      v_email_wet               DATE,
      v_email_trans_type        VARCHAR2
   )
      RETURN allsmsmessages_ref;

   TYPE locationdetails_rec IS RECORD (
      loc_code       tqc_locations.loc_code%TYPE,
      loc_twn_code   tqc_locations.loc_twn_code%TYPE,
      loc_sht_desc   tqc_locations.loc_sht_desc%TYPE,
      loc_name       tqc_locations.loc_name%TYPE
   );

   TYPE locationdetails_ref IS REF CURSOR
      RETURN locationdetails_rec;

   FUNCTION getlocationdetails (v_loc_twn_code IN NUMBER)
      RETURN locationdetails_ref;

   FUNCTION getorgdivlevelstype
      RETURN orgdivlevelstype_ref;

   TYPE bussiness_person_rec IS RECORD (
      bpn_code             tqc_business_persons.bpn_code%TYPE,
      bpn_id_no            tqc_business_persons.bpn_id_no%TYPE,
      bpn_address          tqc_business_persons.bpn_address%TYPE,
      bpn_tel              tqc_business_persons.bpn_tel%TYPE,
      bpn_mobile_no        tqc_business_persons.bpn_mobile_no%TYPE,
      bpn_email            tqc_business_persons.bpn_email%TYPE,
      bpn_type             tqc_business_persons.bpn_type%TYPE,
      bpn_zip              tqc_business_persons.bpn_zip%TYPE,
      bpn_town             tqc_business_persons.bpn_town%TYPE,
      bpn_cou_code         tqc_business_persons.bpn_cou_code%TYPE,
      bpn_name             tqc_business_persons.bpn_name%TYPE,
      bpn_pin              tqc_business_persons.bpn_pin%TYPE,
      bpn_bbr_code         tqc_business_persons.bpn_bbr_code%TYPE,
      bpn_bank_acc_no      tqc_business_persons.bpn_bank_acc_no%TYPE,
      bpn_bbr_swift_code   tqc_business_persons.bpn_bbr_swift_code%TYPE,
      bpn_reg_clmt_code    tqc_business_persons.bpn_reg_clmt_code%TYPE,
      bbr_branch_name      tqc_bank_branches.bbr_branch_name%TYPE,
      cou_name             tqc_countries.cou_name%TYPE,
      cld_surname          gin_claimants.cld_surname%TYPE
   );

   TYPE bussiness_person_ref IS REF CURSOR
      RETURN bussiness_person_rec;

   FUNCTION get_bussiness_person (
      v_bpn_payee_type   IN   VARCHAR2,
      v_bpn_clnt_code    IN   NUMBER
   )
      RETURN bussiness_person_ref;

   TYPE bank_branches_val_rec IS RECORD (
      bbr_code          tqc_bank_branches.bbr_code%TYPE,
      bbr_bnk_code      tqc_bank_branches.bbr_bnk_code%TYPE,
      bbr_branch_name   tqc_bank_branches.bbr_branch_name%TYPE,
      bbr_remarks       tqc_bank_branches.bbr_remarks%TYPE,
      bbr_sht_desc      tqc_bank_branches.bbr_sht_desc%TYPE
   );

   TYPE bank_branches_val_ref IS REF CURSOR
      RETURN bank_branches_val_rec;

   FUNCTION get_bank_branches_val
      RETURN bank_branches_val_ref;

   TYPE registered_claimants_rec IS RECORD (
      reg_clmt_code     gin_rgstd_claimants.reg_clmt_code%TYPE,
      cld_code          gin_claimants.cld_code%TYPE,
      cld_id_no         gin_claimants.cld_id_no%TYPE,
      cld_other_names   gin_claimants.cld_other_names%TYPE
   );

   TYPE registered_claimants_ref IS REF CURSOR
      RETURN registered_claimants_rec;

   FUNCTION get_registered_claimants
      RETURN registered_claimants_ref;

   TYPE agent_type_rec IS RECORD (
      agn_agent_type   tqc_agencies.agn_agent_type%TYPE
   );

   TYPE agent_type_ref IS REF CURSOR
      RETURN agent_type_rec;

   FUNCTION get_agent_type (v_agn_act_code IN NUMBER)
      RETURN agent_type_ref;
      
      
   TYPE tqc_agent_types_rec IS RECORD (
      AGNTY_CODE tqc_agent_types.AGNTY_CODE%TYPE, 
      AGNTY_TYPE_SHT_DESC tqc_agent_types.AGNTY_TYPE_SHT_DESC%TYPE,
      AGNTY_TYPE tqc_agent_types.AGNTY_TYPE%TYPE,
      AGNTY_ACT_CODE  tqc_agent_types.AGNTY_ACT_CODE%TYPE,
      ACT_ACCOUNT_TYPE  tqc_account_types.ACT_ACCOUNT_TYPE%TYPE
   );

   TYPE tqc_agent_types_ref IS REF CURSOR
      RETURN tqc_agent_types_rec;
      
     FUNCTION get_agent_types RETURN tqc_agent_types_ref;
     

   FUNCTION get_agent_groups
      RETURN agent_type_ref;

   TYPE occupation_rec IS RECORD (
      occ_code       tqc_occupations.occ_code%TYPE,
      occ_sec_code   tqc_occupations.occ_sec_code%TYPE,
      occ_sht_desc   tqc_occupations.occ_sht_desc%TYPE,
      occ_name       tqc_occupations.occ_name%TYPE
   );

   TYPE occupation_ref IS REF CURSOR
      RETURN occupation_rec;

   FUNCTION get_occupation(v_sect_code NUMBER)
      RETURN occupation_ref;

   TYPE label_rec IS RECORD (
      label_name    tqc_labels.label_name%TYPE,
      label_value   tqc_labels.label_value%TYPE
   );

   TYPE label_ref IS REF CURSOR
      RETURN label_rec;

   FUNCTION getlabels (v_labelname VARCHAR2)
      RETURN label_ref;

   PROCEDURE get_labels (v_parameters_ref OUT parameters_ref);

   TYPE districts_rec IS RECORD (
      dist_code         tqc_districts.dist_code%TYPE,
      dist_cou_code     tqc_districts.dist_cou_code%TYPE,
      dist_sht_desc     tqc_districts.dist_sht_desc%TYPE,
      dist_name         tqc_districts.dist_name%TYPE,
      dist_state_code   tqc_states.sts_code%TYPE
   );

   TYPE districts_ref IS REF CURSOR
      RETURN districts_rec;

   PROCEDURE getdistricts (
      v_districts_ref   OUT   districts_ref,
      v_cou_code              tqc_districts.dist_cou_code%TYPE
   );

   FUNCTION getdistrictsbycountry (v_cou_code IN NUMBER)
      RETURN districts_ref;

   FUNCTION getdistrictsbystate (v_state_code IN NUMBER)
      RETURN districts_ref;

   TYPE agency_accounts_rec IS RECORD (
      aga_code         tqc_agency_accounts.aga_code%TYPE,
      aga_sht_desc     tqc_agency_accounts.aga_sht_desc%TYPE,
      aga_name         tqc_agency_accounts.aga_name%TYPE,
      aga_created_by   tqc_agency_accounts.aga_created_by%TYPE
   );

   TYPE agency_accounts_ref IS REF CURSOR
      RETURN agency_accounts_rec;
    
   
   TYPE tqc_dd_failed_remarks_rec IS RECORD(
    dfr_code                        tqc_dd_failed_remarks.dfr_code%TYPE,
    dfr_failed_remark       tqc_dd_failed_remarks.dfr_failed_remark%TYPE,
    dfr_appl_level              tqc_dd_failed_remarks.dfr_appl_level%TYPE
   );
   
   TYPE tqc_dd_failed_remarks_ref IS REF CURSOR
        RETURN tqc_dd_failed_remarks_rec;
     

   FUNCTION getagencyaccounts (agncode tqc_agency_accounts.aga_code%TYPE)
      RETURN agency_accounts_ref;

   PROCEDURE get_postal_codes (v_locations_ref OUT locations_ref);

   PROCEDURE get_base_currency (v_currencies_ref OUT currencies_ref);
   function getOrgType(
     v_org_code NUMBER
    )RETURN VARCHAR2;
   
TYPE TRANSFERS_REC IS RECORD (
  TT_CODE          NUMBER(8),
  TT_TRANS_TYPE    VARCHAR2(3),
  TT_TRANS_DATE    DATE,
  TT_TRANS_TO_CODE      NUMBER,
  TT_TRANS_TO_NAME      VARCHAR(80),
  TT_TRANS_FROM_CODE    NUMBER,
  TT_TRANS_FROM_NAME    VARCHAR(80),
  TT_DONE_BY       VARCHAR(80),
  TT_DONE_DATE     DATE,
  TT_AUTHORIZED    VARCHAR(80), 
  TT_AUTHORIZED_BY       VARCHAR(80),
  TT_AUTHORIZED_DATE     DATE 
);

TYPE TRANSFERS_REF IS REF CURSOR
      RETURN TRANSFERS_REC;

TYPE TRANSFER_ITEMS_REC IS RECORD(
  TTI_CODE          NUMBER(8),
  TTI_TT_CODE       NUMBER(32),
  TTI_ITEM_CODE     NUMBER(32),
  TTI_ITEM_NAME     VARCHAR(80),
  TTI_ITEM_SHT_DESC  VARCHAR(80),
  TTI_ITEM_TYPE     VARCHAR2(1)         
 );

TYPE TRANSFER_ITEMS_REF IS REF CURSOR
      RETURN TRANSFER_ITEMS_REC;
      
TYPE TRANSFER_XITEMS_REC IS RECORD (
  TTX_ITEM_CODE     NUMBER(32),
  TTX_ITEM_NAME     VARCHAR(80),
  TTX_ITEM_SHT_DESC  VARCHAR(80),
  TTX_ITEM_TYPE     VARCHAR2(1) 
 );
   
 TYPE TRANSFER_XITEMS_REF IS REF CURSOR
      RETURN TRANSFER_XITEMS_REC;
      
 PROCEDURE get_transfer_details (
        v_transfers_ref OUT transfers_ref, 
        v_trans_type varchar2,
        v_trans_from_code  number,
        v_trans_from_name  varchar2,
        v_trans_to_code  varchar2,
        v_trans_to_name  varchar2,
        v_done_by   varchar2
    );
   
   PROCEDURE get_transfered_items(
        v_transfer_items_ref OUT transfer_items_ref, 
        v_tt_code number
    );
   
    PROCEDURE get_transferable_items(
        v_transfer_xitems_ref OUT transfer_xitems_ref, 
        v_tt_code number
    );     
      TYPE units_ref_rec IS RECORD (
      OSD_NAME         TQC_ORG_SUBDIVISIONS.OSD_NAME%TYPE,
      ODL_DESC   TQC_ORG_DIVISION_LEVELS.ODL_DESC%TYPE,
      DLT_DESC       TQC_ORG_DIVISION_LEVELS_TYPE.DLT_DESC%TYPE,
      OSD_ID    TQC_ORG_SUBDIVISIONS.OSD_ID%TYPE,/*DESIGN CHANGE MADE TO MAKE IT PRIMARY KEY OF THE TABLE*/
      ODL_CODE    TQC_ORG_DIVISION_LEVELS.ODL_CODE%TYPE,
      DLT_CODE    TQC_ORG_DIVISION_LEVELS_TYPE.DLT_CODE%TYPE
   );

   TYPE units_refrec IS REF CURSOR
      RETURN units_ref_rec;  
    PROCEDURE fetch_sbus(v_units_ref OUT units_refrec,v_type VARCHAR2);  
    
     TYPE sub_acc_rec IS RECORD (
      SACT_CODE                    LMS_SUB_ACCOUNT_TYPES.SACT_CODE%TYPE,
      SACT_SHT_DESC            LMS_SUB_ACCOUNT_TYPES.SACT_SHT_DESC%TYPE,
      SACT_DESCRIPTION        LMS_SUB_ACCOUNT_TYPES.SACT_DESCRIPTION%TYPE
   );

   TYPE sub_accrec IS REF CURSOR
      RETURN sub_acc_rec;  
    PROCEDURE find_life_subacc_types(
        v_sub_acc_ref OUT sub_accrec
    );
    
     PROCEDURE get_all_occupations (v_sect_code NUMBER, v_sectors_ref OUT sectors_ref); 
    PROCEDURE get_sector_occupations (v_sect_code NUMBER, v_sectors_ref OUT sectors_ref);

     TYPE contact_datails_rec IS RECORD(
       code      number,
       names     varchar2(200),
       email      varchar2(200),
       telephone   varchar2(200)
    );
    
    TYPE contact_datails_cursor IS REF CURSOR
    RETURN contact_datails_rec;
    
PROCEDURE get_contact_details (contact_details OUT contact_datails_cursor, v_client_code IN VARCHAR2, v_client_type IN VARCHAR2);


TYPE taxauthorities_rec IS RECORD (
      auth_code                 tqc_tax_auth.auth_code%TYPE,
      auth_sht_code             tqc_tax_auth.auth_sht_code%TYPE,
      auth_name                 tqc_tax_auth.auth_name%TYPE
   );

TYPE taxauthorities_ref IS REF CURSOR
      RETURN taxauthorities_rec;
      
PROCEDURE getTaxAuthorities(
     v_taxauthorities_ref   OUT  taxauthorities_ref
);
 PROCEDURE getSchengenCountries(v_countries_ref OUT countries_ref,
                         v_schengen      IN VARCHAR2,  v_inbound IN VARCHAR2 DEFAULT NULL);
PROCEDURE get_countries(v_countries_ref OUT countries_ref, v_inbound VARCHAR2 DEFAULT NULL);
END tqc_setups_cursor_05042017; 
 
/