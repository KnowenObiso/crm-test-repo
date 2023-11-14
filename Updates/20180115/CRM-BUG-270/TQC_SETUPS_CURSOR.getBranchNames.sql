--spec update the type
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
      bns_region        tqc_branch_names.bns_region%TYPE,
      bns_postal_code   tqc_branch_names.bns_post_code%TYPE
   );

--- body
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