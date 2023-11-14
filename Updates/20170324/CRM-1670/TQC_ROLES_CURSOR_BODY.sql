PROCEDURE get_usersnotingrouptype (v_users_ref OUT users_ref, v_grp_code IN NUMBER)
  IS
  BEGIN
     OPEN v_users_ref FOR
        SELECT   usr_code, usr_username, usr_name, usr_email,
                 usr_personel_rank, usr_dt_created, usr_type, usr_status,
                 usr_pwd_reset, usr_per_id, usr_acct_mgr, usr_cell_phone_no,
                 usr_wef_dt, usr_wet_dt, NULL, NULL, usr_created_by, NULL,
                 NULL
            FROM tqc_users
           WHERE usr_code NOT IN (SELECT gtuser_usr_code
                                    FROM tqc_group_category_users
                                   WHERE gtusr_grpt_usr_code = v_grp_code)
             AND usr_type != 'G'
        ORDER BY usr_code DESC;
  END get_usersnotingrouptype;
  
  PROCEDURE get_accountsnotingrouptype(
                        v_accounts_ref OUT accounts_ref, 
                        v_grp_code IN NUMBER,
                        v_acc_type_id IN tqc_account_types.act_type_id%TYPE)
  IS
  BEGIN
    OPEN v_accounts_ref FOR
        SELECT act.acc_code,
               act.acc_type,
               
               atype.act_account_type,
               
               agn.agn_code,
               agn.agn_name 
        FROM tqc_accounts act
        LEFT OUTER JOIN tqc_account_types atype ON atype.act_type_id = act.acc_type
        LEFT OUTER JOIN tqc_agencies agn ON agn.agn_code = act.acc_type_code
        WHERE act.acc_code NOT IN (SELECT gtacc_acc_code
                                    FROM tqc_group_category_accounts
                                   WHERE gtacc_grpt_usr_code = v_grp_code)
        AND atype.act_type_id = v_acc_type_id
        ORDER BY act.acc_type ASC;
  END get_accountsnotingrouptype;
  
  FUNCTION getallbranches
      RETURN branches_ref
   IS
      vcursor   branches_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT brn_code, brn_sht_desc, brn_reg_code, brn_name
                    FROM tqc_branches;

      RETURN vcursor;
   END;