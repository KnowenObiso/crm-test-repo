FUNCTION getusergrouptypes 
      RETURN usergrouptype_ref
   IS
      v_cursor   usergrouptype_ref;
   BEGIN
      OPEN v_cursor FOR
        SELECT usr_code,
               usr_type_sht_desc,
               usr_group_type,
               usr_id_serial_format,
               usr_type_id,
               
               usr_branch_code,
               (SELECT bns_name FROM tqc_branch_names WHERE bns_code = usr_branch_code) 
               FROM tqc_user_group_categories;

      RETURN v_cursor;
   END;
   
   
   FUNCTION getgrouptypesmembers (
      v_gtusr_grpt_usr_code   tqc_group_category_users.gtusr_grpt_usr_code%TYPE
   )
      RETURN usergrouptypes_ref
   IS
      v_cursor   usergrouptypes_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT usr_code, usr_username, usr_name, usr_email, gtusr_code,
                gtusr_grpt_usr_code, gtusr_team_leader
           FROM tqc_users, tqc_group_category_users
          WHERE usr_code = gtuser_usr_code
            AND gtusr_grpt_usr_code = v_gtusr_grpt_usr_code;

      RETURN v_cursor;
   END;
   
   
   FUNCTION getgrouptypesaccounts (
      v_gtacc_grpt_usr_code   tqc_group_category_accounts.gtacc_grpt_usr_code%TYPE
   )
      RETURN accountgrouptypes_ref
   IS
      v_cursor   accountgrouptypes_ref;
   BEGIN
      OPEN v_cursor FOR
          SELECT gtacc.gtacc_code,
				gtacc.gtacc_grpt_usr_code,
				
				act.acc_type,
				atype.act_account_type,
				agn.agn_code,
				agn.agn_name 
				FROM tqc_group_category_accounts gtacc
				INNER JOIN tqc_accounts act ON act.acc_code = gtacc.gtacc_acc_code
				LEFT OUTER JOIN tqc_account_types atype ON atype.act_type_id = act.acc_type
				LEFT OUTER JOIN tqc_agencies agn ON agn.agn_code = act.acc_type_code
				WHERE gtacc.gtacc_grpt_usr_code = v_gtacc_grpt_usr_code;

      RETURN v_cursor;
   END;