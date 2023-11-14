PROCEDURE usergrouptype_prc (
	  v_add_edit 				VARCHAR2,
	  v_gtusr_code				tqc_group_category_users.gtusr_code%TYPE,
	  v_gtusr_grpt_usr_code 	tqc_group_category_users.gtusr_grpt_usr_code%TYPE,
	  v_gtuser_usr_code			tqc_group_category_users.gtuser_usr_code%TYPE
   );
   PROCEDURE accountgrouptype_prc (
        v_add_edit                  VARCHAR2,
        v_gtacc_code                tqc_group_category_accounts.gtacc_code%TYPE,
        v_gtacc_grpt_usr_code       tqc_group_category_accounts.gtacc_grpt_usr_code%TYPE,
        v_gtacc_acc_code            tqc_group_category_accounts.gtacc_acc_code%TYPE
   );
   
   PROCEDURE grouptype_prc (
        v_add_edit          VARCHAR2,
        v_usr_code          tqc_user_group_categories.usr_code%TYPE,        
        v_usr_type_id       tqc_user_group_categories.usr_type_id%TYPE,
        v_usr_group_type    tqc_user_group_categories.usr_group_type%TYPE,
        v_usr_type_sht_desc tqc_user_group_categories.usr_type_sht_desc%TYPE,
        v_usr_branch_code   tqc_user_group_categories.usr_branch_code%TYPE
    );
    PROCEDURE usergrouptypeteamleader_prc (
        v_gtusr_grpt_usr_code           tqc_group_category_users.gtusr_grpt_usr_code%TYPE,
        v_gtuser_usr_code               tqc_group_category_users.gtuser_usr_code%TYPE
    );