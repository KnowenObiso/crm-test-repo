PROCEDURE grouptype_prc (
        v_add_edit          VARCHAR2,
        v_usr_code          tqc_user_group_categories.usr_code%TYPE,        
        v_usr_type_id       tqc_user_group_categories.usr_type_id%TYPE,
        v_usr_group_type    tqc_user_group_categories.usr_group_type%TYPE,
        v_usr_type_sht_desc tqc_user_group_categories.usr_type_sht_desc%TYPE,
        v_usr_branch_code   tqc_user_group_categories.usr_branch_code%TYPE
    );