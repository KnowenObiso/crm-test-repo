PROCEDURE delete_branch_name (
      v_bns_code         tqc_branch_names.bns_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   )
   IS
   BEGIN
--    raise_error ('Branch Name Deletion not possible!');
      IF v_bns_code IS NULL
      THEN
         raise_error ('Pass the primary key for branch name record.');
         RETURN;
      ELSE
         BEGIN
           DELETE TQC_BRANCH_NAMES WHERE bns_code = v_bns_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error deleting branch name record.');
               RETURN;
         END;
      END IF;
   END delete_branch_name;