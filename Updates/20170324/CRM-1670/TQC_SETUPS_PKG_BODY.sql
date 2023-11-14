PROCEDURE usergrouptype_prc (
	  v_add_edit 				VARCHAR2,
	  v_gtusr_code				tqc_group_category_users.gtusr_code%TYPE,
	  v_gtusr_grpt_usr_code 	tqc_group_category_users.gtusr_grpt_usr_code%TYPE,
	  v_gtuser_usr_code			tqc_group_category_users.gtuser_usr_code%TYPE
   )
   IS 
	  v_count 		NUMBER;
   BEGIN 
	  IF v_add_edit = 'A'
	  THEN 
		 BEGIN 
			 INSERT INTO tqc_group_category_users
						  (gtusr_code, gtusr_grpt_usr_code,
							 gtuser_usr_code
						  )
				     VALUES (tqc_grp_ctg_users_code_seq.NEXTVAL, v_gtusr_grpt_usr_code,
							 v_gtuser_usr_code
							);
			 EXCEPTION
				WHEN OTHERS
				THEN raise_error ( 'Error occured while creating record'
								 || SQLERRM(SQLCODE)
								 );
			 
		 END;
	  ELSIF v_add_edit = 'D'
	  THEN
		 BEGIN
			 BEGIN
				 DELETE FROM tqc_group_category_users
					WHERE gtusr_code=v_gtusr_code;
			 EXCEPTION
				 WHEN OTHERS
					THEN raise_error ( 'Error occured while deleting record'
									 || SQLERRM(SQLCODE)
									 );
			 END;
		 END;
	  END IF;
   END;
   
   PROCEDURE accountgrouptype_prc (
        v_add_edit                  VARCHAR2,
        v_gtacc_code                tqc_group_category_accounts.gtacc_code%TYPE,
        v_gtacc_grpt_usr_code       tqc_group_category_accounts.gtacc_grpt_usr_code%TYPE,
        v_gtacc_acc_code            tqc_group_category_accounts.gtacc_acc_code%TYPE
   )
   IS 
        v_count             NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_group_category_accounts
                            (gtacc_code, gtacc_grpt_usr_code, gtacc_acc_code)
                            VALUES (tqc_grp_ctg_accounts_code_seq.nextval,
                                v_gtacc_grpt_usr_code,  v_gtacc_acc_code);
            EXCEPTION
                WHEN OTHERS
                THEN RAISE_ERROR ( 'Error occured while creating record'
                                    || sqlerrm(sqlcode)
                                 );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN 
             DELETE FROM tqc_group_category_accounts
                WHERE gtacc_code = v_gtacc_code;
             EXCEPTION
                WHEN OTHERS
                    THEN RAISE_ERROR( 'Error occured while deleting record.'
                                    || SQLERRM(SQLCODE)
                                    );
         END;
      END IF;
   END;
   
   PROCEDURE grouptype_prc (
        v_add_edit          VARCHAR2,
        v_usr_code          tqc_user_group_categories.usr_code%TYPE,
        v_usr_type_id       tqc_user_group_categories.usr_type_id%TYPE,
        v_usr_group_type    tqc_user_group_categories.usr_group_type%TYPE,
        v_usr_type_sht_desc tqc_user_group_categories.usr_type_sht_desc%TYPE,
        v_usr_branch_code   tqc_user_group_categories.usr_branch_code%TYPE
    )
    IS 
        v_count NUMBER; 
    BEGIN
        IF v_add_edit = 'A'
        THEN
            BEGIN
                INSERT INTO tqc_user_group_categories
                            (usr_code, usr_type_id, usr_group_type, usr_type_sht_desc, usr_branch_code)
                     VALUES (tqc_usr_grp_ctg_code_seq.NEXTVAL, v_usr_type_id, v_usr_group_type, v_usr_type_sht_desc,
                                v_usr_branch_code);
                EXCEPTION
                    WHEN OTHERS
                    THEN raise_error ( 'Error occured while creating record'
                                     || SQLERRM(SQLCODE)
                                     );
            END;
        ELSIF v_add_edit = 'E'
        THEN
            BEGIN
                UPDATE tqc_user_group_categories
                    SET usr_group_type = v_usr_group_type,
                        usr_type_sht_desc = v_usr_type_sht_desc,
                        usr_branch_code = v_usr_branch_code,
                        usr_type_id = v_usr_type_id
                    WHERE usr_code = v_usr_code;
                EXCEPTION
                    WHEN OTHERS
                    THEN raise_error ( 'Error occured while updating record'
                                     || SQLERRM(SQLCODE)
                                     );
            END;
        ELSIF v_add_edit = 'D'
        THEN
            BEGIN
                DELETE FROM tqc_user_group_categories
                    WHERE usr_code = v_usr_code;
                EXCEPTION
                    WHEN OTHERS
                    THEN raise_error ( 'Error occured while deleting record'
                                     || SQLERRM(SQLCODE)
                                     );
            END;
        END IF;
    END grouptype_prc;
    
    PROCEDURE usergrouptypeteamleader_prc (
        v_gtusr_grpt_usr_code           tqc_group_category_users.gtusr_grpt_usr_code%TYPE,
        v_gtuser_usr_code               tqc_group_category_users.gtuser_usr_code%TYPE
    )
    IS
        v_count                         NUMBER;
    BEGIN
        -- raise_error('Params: ' || v_gtusr_grpt_usr_code || ' ' || v_gtuser_usr_code);
        -- DESELECT ALL
        UPDATE tqc_group_category_users grpt
            SET grpt.gtusr_team_leader = 'N'
            WHERE grpt.gtusr_grpt_usr_code = v_gtusr_grpt_usr_code;
        -- SET THE CURRENT SELECTED USER AS TEAMLEADEAR
        UPDATE tqc_group_category_users grpt
            SET grpt.gtusr_team_leader = 'Y'
            WHERE grpt.gtusr_grpt_usr_code =v_gtusr_grpt_usr_code
                AND grpt.gtuser_usr_code =  v_gtuser_usr_code;
        EXCEPTION
            WHEN OTHERS
            THEN RAISE_ERROR ('Could not update the team leader.' 
                        || sqlerrm(sqlcode)
                        );
    END;