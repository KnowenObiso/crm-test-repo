ALTER TABLE TQ_CRM.TQC_BANK_BRANCHES
ADD (BBR_TWN_CODE NUMBER);

TQC_SETUPS_CURSOR .bank_branches_rec

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
      v_bbr_twn_code         tqc_bank_branches.bbr_twn_code%TYPE,
      v_twn_name             tqc_towns.twn_name%type
   );
   
      PROCEDURE get_bank_branches (v_bank_branches_ref OUT bank_branches_ref)
   IS
   BEGIN
      OPEN v_bank_branches_ref FOR
         SELECT bbr_code, bbr_bnk_code, bbr_branch_name, bbr_remarks,
                bbr_sht_desc, bbr_ref_code, bbr_eft_supported,
                bbr_dd_supported, bbr_date_created, bbr_created_by,
                bbr_physical_addrs, bbr_postal_addrs, bbr_kba_code,
                bbr_twn_code,(SELECT twn_name FROM TQC_TOWNS WHERE twn_code=bbr_twn_code ) twn_name
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
                bbr_twn_code,(SELECT twn_name FROM TQC_TOWNS WHERE twn_code=bbr_twn_code ) twn_name
           FROM tqc_bank_branches
          WHERE bbr_bnk_code = v_bbr_bnk_code;
   END get_bank_branches_by_bank_code;
   ------------------------------------------------------------------
   --tqc_setups_pkg.bank_branches_prc
   PROCEDURE bank_branches_prc (
      v_add_edit             IN       VARCHAR2,
      v_bbr_code             IN       tqc_bank_branches.bbr_code%TYPE,
      v_bbr_bnk_code         IN       tqc_bank_branches.bbr_bnk_code%TYPE,
      v_bbr_branch_name      IN       tqc_bank_branches.bbr_branch_name%TYPE,
      v_bbr_remarks          IN       tqc_bank_branches.bbr_remarks%TYPE,
      v_bbr_sht_desc         IN       tqc_bank_branches.bbr_sht_desc%TYPE,
      v_bbr_ref_code         IN       tqc_bank_branches.bbr_ref_code%TYPE,
      v_bbr_eft_supported    IN       tqc_bank_branches.bbr_eft_supported%TYPE,
      v_bbr_dd_supported     IN       tqc_bank_branches.bbr_dd_supported%TYPE,
      v_bbr_date_created     IN       tqc_bank_branches.bbr_date_created%TYPE,
      v_bbr_created_by       IN       tqc_bank_branches.bbr_created_by%TYPE,
      v_bbr_physical_addrs   IN       tqc_bank_branches.bbr_physical_addrs%TYPE,
      v_bbr_postal_addrs     IN       tqc_bank_branches.bbr_postal_addrs%TYPE,
      v_bbr_kba_code         IN       tqc_bank_branches.bbr_kba_code%TYPE,
      v_error                OUT      VARCHAR2,
      v_town_code            IN       NUMBER
   )
   IS
      v_count   NUMBER;
   BEGIN
      --RAISE_ERROR('***'||v_BBR_CODE);
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM tqc_bank_branches, tqc_banks
             WHERE bbr_bnk_code(+) = bnk_code
               AND bbr_bnk_code = v_bbr_bnk_code
               AND bbr_branch_name = v_bbr_branch_name;

            IF v_count != 0
            THEN
               v_error := 'Bank Branch of the Same Name Exists';
               RETURN;
            END IF;

            INSERT INTO tqc_bank_branches
                        (bbr_code, bbr_bnk_code,
                         bbr_branch_name, bbr_remarks, bbr_sht_desc,
                         bbr_ref_code, bbr_eft_supported,
                         bbr_dd_supported, bbr_date_created,
                         bbr_created_by, bbr_physical_addrs,
                         bbr_postal_addrs, bbr_kba_code,bbr_twn_code
                        )
                 VALUES (tqc_bbr_code_seq.NEXTVAL, v_bbr_bnk_code,
                         v_bbr_branch_name, v_bbr_remarks, v_bbr_sht_desc,
                         v_bbr_ref_code, v_bbr_eft_supported,
                         v_bbr_dd_supported, v_bbr_date_created,
                         v_bbr_created_by, v_bbr_physical_addrs,
                         v_bbr_postal_addrs, v_bbr_kba_code,v_town_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while creating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_bank_branches
               SET bbr_bnk_code = v_bbr_bnk_code,
                   bbr_branch_name = v_bbr_branch_name,
                   bbr_remarks = v_bbr_remarks,
                   bbr_sht_desc = v_bbr_sht_desc,
                   bbr_ref_code = v_bbr_ref_code,
                   bbr_eft_supported = v_bbr_eft_supported,
                   bbr_dd_supported = v_bbr_dd_supported,
                   -- BBR_DATE_CREATED    = v_BBR_DATE_CREATED,
                   -- BBR_CREATED_BY      = v_BBR_CREATED_BY,
                   bbr_physical_addrs = v_bbr_physical_addrs,
                   bbr_postal_addrs = v_bbr_postal_addrs,
                   bbr_kba_code = v_bbr_kba_code,
                   bbr_twn_code = v_town_code
             WHERE bbr_code = v_bbr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while updating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_bank_branches
                  WHERE bbr_code = v_bbr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while deleting record ' || SQLERRM (SQLCODE);
         END;
      END IF;
   ---  v_error := 'Bank Branch Processing Successful';
   END;