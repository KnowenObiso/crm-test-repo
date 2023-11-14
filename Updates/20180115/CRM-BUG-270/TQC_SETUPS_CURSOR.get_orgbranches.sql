--- spec update type only
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
      brn_ref                   tqc_branches.brn_ref%TYPE,
      brn_ade_code              tqc_branches.brn_ade_code%TYPE,
      brn_ade_name              tqc_agencies.agn_name%TYPE,
      brn_town_name             tqc_branches.brn_town_name%TYPE,
      brn_post_code             tqc_branches.brn_post_code%TYPE
   );

   
---body
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