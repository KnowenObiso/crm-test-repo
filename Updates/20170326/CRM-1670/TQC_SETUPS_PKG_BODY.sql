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
                -- 
                UPDATE tqc_group_category_accounts
                    SET gtacc_acc_code = null
                    WHERE gtacc_grpt_usr_code = v_usr_code;
                DELETE FROM tqc_group_category_accounts             
                    WHERE gtacc_grpt_usr_code = v_usr_code;
                --
                UPDATE tqc_group_category_users
                    SET gtuser_usr_code = null
                    WHERE gtusr_grpt_usr_code = v_usr_code;
                DELETE FROM tqc_group_category_users
                    WHERE gtusr_grpt_usr_code = v_usr_code;    
            
            
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