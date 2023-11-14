TYPE usergrouptype_rec IS RECORD (
      usr_code               tqc_user_group_categories.usr_code%TYPE,
      usr_type_sht_desc      tqc_user_group_categories.usr_type_sht_desc%TYPE,
      usr_group_type         tqc_user_group_categories.usr_group_type%TYPE,
      usr_id_serial_format   tqc_user_group_categories.usr_id_serial_format%TYPE,
      usr_type_id            tqc_user_group_categories.usr_type_id%TYPE,
      
      usr_branch_code        tqc_user_group_categories.usr_branch_code%TYPE,
      usr_branch_name        tqc_branch_names.bns_name%TYPE
   );

   TYPE usergrouptype_ref IS REF CURSOR
      RETURN usergrouptype_rec;

   FUNCTION getusergrouptypes
      RETURN usergrouptype_ref;

   TYPE usergrouptypes_rec IS RECORD (
      gtusr_code            tqc_group_category_users.gtusr_code%TYPE,
      gtusr_grpt_usr_code   tqc_group_category_users.gtusr_grpt_usr_code%TYPE,
      gtusr_team_leader     tqc_group_category_users.gtusr_team_leader%TYPE,
      usr_code              tqc_users.usr_code%TYPE,
      usr_username          tqc_users.usr_username%TYPE,
      usr_name              tqc_users.usr_name%TYPE,
      usr_email             tqc_users.usr_email%TYPE
   );

   TYPE usergrouptypes_ref IS REF CURSOR
      RETURN usergrouptypes_rec;

   FUNCTION getgrouptypesmembers (
      v_gtusr_grpt_usr_code   tqc_group_category_users.gtusr_grpt_usr_code%TYPE
   )
      RETURN usergrouptypes_ref;

   TYPE accountgrouptypes_rec IS RECORD (
      gtacc_code            tqc_group_category_accounts.gtacc_code%TYPE,
      gtacc_grpt_usr_code   tqc_group_category_accounts.gtacc_grpt_usr_code%TYPE,
      acc_code              tqc_accounts.acc_code%TYPE,
      act_account_type      tqc_account_types.act_account_type%TYPE,
      agn_code              tqc_agencies.agn_code%TYPE,
      agn_name              tqc_agencies.agn_name%TYPE
   );

   TYPE accountgrouptypes_ref IS REF CURSOR
      RETURN accountgrouptypes_rec;

   FUNCTION getgrouptypesaccounts (
      v_gtacc_grpt_usr_code   tqc_group_category_accounts.gtacc_grpt_usr_code%TYPE
   )
      RETURN accountgrouptypes_ref;