--
-- TQC_ROLES_CURSOR030215  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_ROLES_CURSOR030215
AS
   /******************************************************************************
      NAME:       TQC_ROLES_CURSOR
      PURPOSE:

      REVISIONS:
      Ver        Date        Author           Description
      ---------  ----------  ---------------  ------------------------------------
      1.0        08/Oct/2009             1. Created this package.
   ******************************************************************************/



   TYPE Sys_roles_rec IS RECORD
   (
      SRLS_CODE       TQC_SYS_ROLES.SRLS_CODE%TYPE,
      SRLS_NAME       TQC_SYS_ROLES.SRLS_NAME%TYPE,
      SRLS_CRT_DATE   TQC_SYS_ROLES.SRLS_CRT_DATE%TYPE,
      SRLS_SHT_DESC   TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
      SRLS_STATUS     TQC_SYS_ROLES.SRLS_STATUS%TYPE
   );


   TYPE Sys_roles_ref IS REF CURSOR
      RETURN Sys_roles_rec;

   FUNCTION Get_Sys_Roles (v_syscode IN TQC_SYSTEMS.SYS_CODE%TYPE)
      RETURN Sys_roles_ref;

   TYPE Sys_UsrRoles_rec IS RECORD
   (
      SPRC_CODE       TQC_SYS_PROCESSES.SPRC_CODE%TYPE,
      SRPRC_CODE      TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE,
      SRLS_NAME       TQC_SYS_ROLES.SRLS_NAME%TYPE,
      SPRC_PROCESS    TQC_SYS_PROCESSES.SPRC_PROCESS%TYPE,
      SRLS_CRT_DATE   TQC_SYS_ROLES.SRLS_CRT_DATE%TYPE,
      SRLS_STATUS     TQC_SYS_ROLES.SRLS_STATUS%TYPE,
      SRLS_CODE       TQC_SYS_ROLES.SRLS_CODE%TYPE,
      SRLS_SHT_DESC   TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE
   );

   TYPE Sys_UsrRoles_ref IS REF CURSOR
      RETURN Sys_UsrRoles_rec;

   FUNCTION Get_User_Roles (v_syscode   IN TQC_SYSTEMS.SYS_CODE%TYPE,
                            v_user      IN TQC_USERS.USR_CODE%TYPE)
      RETURN Sys_UsrRoles_ref;

   TYPE Role_Areas_rec IS RECORD
   (
      SPRCA_CODE         TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
      SPRCA_AREA         TQC_SYS_PROCESS_AREAS.SPRCA_AREA%TYPE,
      SRPRA_SRPRC_CODE   TQC_SYS_ROLES_PRCS_AREA.SRPRA_SRPRC_CODE%TYPE,
      SRPRC_CODE         TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE
   );

   TYPE Role_Areas_ref IS REF CURSOR
      RETURN Role_Areas_rec;

   FUNCTION Get_Role_Areas (
      v_Process       IN TQC_SYS_PROCESSES.SPRC_CODE%TYPE,
      v_roleProcess   IN TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE)
      RETURN Role_Areas_ref;

   TYPE ProcessRole_Areas_rec IS RECORD
   (
      SPRCA_CODE         TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
      SPRCA_AREA         TQC_SYS_PROCESS_AREAS.SPRCA_AREA%TYPE,
      SRPRA_SPRCA_CODE   TQC_SYS_ROLES_PRCS_AREA.SRPRA_SRPRC_CODE%TYPE,
      SRPRA_CODE         TQC_SYS_ROLES_PRCS_AREA.SRPRA_CODE%TYPE,
      SRPRC_CODE         TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE,
      AREA_ASSIGNED      VARCHAR2 (1)
   );

   TYPE ProcessRole_Areas_ref IS REF CURSOR
      RETURN ProcessRole_Areas_rec;

   FUNCTION GetProcessRoleArea (
      v_Process       IN TQC_SYS_PROCESSES.SPRC_CODE%TYPE,
      v_sysRoleCode   IN TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE)
      RETURN ProcessRole_Areas_ref;


   TYPE Role_Processes_rec IS RECORD
   (
      SPRC_CODE          TQC_SYS_PROCESSES.SPRC_CODE%TYPE,
      SRPRC_CODE         TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE,
      SPRC_PROCESS       TQC_SYS_PROCESSES.SPRC_PROCESS%TYPE,
      PROCESS_ASSIGNED   VARCHAR2 (1),
      SRPRC_SRLS_CODE    TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE
   );


   TYPE Role_Processes_ref IS REF CURSOR
      RETURN Role_Processes_rec;

   FUNCTION GetRoleProcesses (
      v_sysCode       IN TQC_SYS_PROCESSES.SPRC_SYS_CODE%TYPE,
      v_sysRoleCode   IN TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE)
      RETURN Role_Processes_ref;


   TYPE Role_SubAreas_rec IS RECORD
   (
      SRPSA_CODE           TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CODE%TYPE,
      SPRSA_CODE           TQC_SYS_PROCESS_SUB_AREAS.SPRSA_CODE%TYPE,
      SPRSA_SUB_AREA       TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SUB_AREA%TYPE,
      SPRSA_TYPE           TQC_SYS_PROCESS_SUB_AREAS.SPRSA_TYPE%TYPE,
      SRPSA_DEBIT_LIMIT    TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_DEBIT_LIMIT%TYPE,
      SRPSA_CREDIT_LIMIT   TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CREDIT_LIMIT%TYPE
   );

   TYPE Role_SubAreas_ref IS REF CURSOR
      RETURN Role_SubAreas_rec;

   FUNCTION Get_Role_SubAreas (
      v_ProcessArea       IN TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
      v_roleProcessArea   IN TQC_SYS_ROLES_PRCS_AREA.SRPRA_SRPRC_CODE%TYPE)
      RETURN Role_SubAreas_ref;

   TYPE ProcessRole_SubAreas_rec IS RECORD
   (
      SRPSA_CODE           TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CODE%TYPE,
      SPRSA_CODE           TQC_SYS_PROCESS_SUB_AREAS.SPRSA_CODE%TYPE,
      SPRSA_SUB_AREA       TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SUB_AREA%TYPE,
      SPRSA_TYPE           TQC_SYS_PROCESS_SUB_AREAS.SPRSA_TYPE%TYPE,
      SRPSA_DEBIT_LIMIT    TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_DEBIT_LIMIT%TYPE,
      SRPSA_CREDIT_LIMIT   TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CREDIT_LIMIT%TYPE,
      SUBAREA_ASSIGNED     VARCHAR2 (1)
   );

   TYPE ProcessRole_SubAreas_ref IS REF CURSOR
      RETURN ProcessRole_SubAreas_rec;

   FUNCTION GetProcessRole_SubAreas (
      v_ProcessArea   IN TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
      v_sysRoleCode   IN TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE,
      v_Process       IN TQC_SYS_PROCESSES.SPRC_CODE%TYPE)
      RETURN ProcessRole_SubAreas_ref;



   TYPE Users_rec IS RECORD
   (
      USR_CODE              TQC_USERS.USR_CODE%TYPE,
      USR_USERNAME          TQC_USERS.USR_USERNAME%TYPE,
      USR_NAME              TQC_USERS.USR_NAME%TYPE,
      USR_EMAIL             TQC_USERS.USR_EMAIL%TYPE,
      USR_PERSONEL_RANK     TQC_USERS.USR_PERSONEL_RANK%TYPE,
      USR_DT_CREATED        TQC_USERS.USR_DT_CREATED%TYPE,
      USR_TYPE              TQC_USERS.USR_TYPE%TYPE,
      USR_STATUS            TQC_USERS.USR_STATUS%TYPE,
      USR_PWD_RESET         TQC_USERS.USR_PWD_RESET%TYPE,
      USR_PER_ID            TQC_USERS.USR_PER_ID%TYPE,
      USR_ACCT_MGR          TQC_USERS.USR_ACCT_MGR%TYPE,
      USR_CELL_PHONE_NO     TQC_USERS.USR_CELL_PHONE_NO%TYPE,
      USR_WEF_DT            TQC_USERS.USR_WEF_DT%TYPE,
      USR_WET_DT            TQC_USERS.USR_WET_DT%TYPE,
      USR_SECQ_CODE         TQC_USERS.USR_SECQ_CODE%TYPE,
      USR_SECURITY_ANSWER   TQC_USERS.USR_SECURITY_ANSWER%TYPE
   );

   TYPE Users_ref IS REF CURSOR
      RETURN Users_rec;

   FUNCTION Get_Users
      RETURN Users_ref;

   PROCEDURE get_all_users (v_Users_ref OUT Users_ref, v_date IN VARCHAR2);

   FUNCTION get_all_individual_users
      RETURN Users_ref;

   PROCEDURE get_AccountOfficers (v_Users_ref OUT Users_ref);

   TYPE Systems_rec IS RECORD
   (
      SYS_CODE       TQC_SYSTEMS.SYS_CODE%TYPE,
      SYS_SHT_DESC   TQC_SYSTEMS.SYS_SHT_DESC%TYPE,
      SYS_NAME       TQC_SYSTEMS.SYS_NAME%TYPE,
      SYS_ACTIVE     TQC_SYSTEMS.SYS_ACTIVE%TYPE
   );

   TYPE Systems_ref IS REF CURSOR
      RETURN Systems_rec;

   FUNCTION Get_Systems
      RETURN Systems_ref;

   TYPE User_Systems_rec IS RECORD
   (
      USYS_CODE      TQC_USER_SYSTEMS.USYS_CODE%TYPE,
      SYS_CODE       TQC_SYSTEMS.SYS_CODE%TYPE,
      SYS_SHT_DESC   TQC_SYSTEMS.SYS_SHT_DESC%TYPE,
      USYS_WEF       TQC_USER_SYSTEMS.USYS_WEF%TYPE,
      SYS_NAME       TQC_SYSTEMS.SYS_NAME%TYPE,
      USYS_WET       TQC_USER_SYSTEMS.USYS_WET%TYPE,
      SYS_PATH       TQC_SYSTEMS.SYS_PATH%TYPE
   );

   TYPE User_Systems_ref IS REF CURSOR
      RETURN User_Systems_rec;

   FUNCTION Get_User_Systems (v_user IN TQC_USERS.USR_CODE%TYPE)
      RETURN User_Systems_ref;

   FUNCTION Get_system_Users (
      v_system IN TQC_USER_SYSTEMS.USYS_SYS_CODE%TYPE)
      RETURN Users_ref;


   TYPE user_unassigned_systems_rec IS RECORD
   (
      SYS_CODE       TQC_SYSTEMS.SYS_CODE%TYPE,
      SYS_SHT_DESC   TQC_SYSTEMS.SYS_SHT_DESC%TYPE,
      SYS_NAME       TQC_SYSTEMS.SYS_NAME%TYPE,
      SYS_ACTIVE     TQC_SYSTEMS.SYS_ACTIVE%TYPE
   );

   TYPE user_unassigned_systems_ref IS REF CURSOR
      RETURN user_unassigned_systems_rec;

   FUNCTION get_user_unassigned_systems (
      v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN user_unassigned_systems_ref;

   FUNCTION get_user_assigned_systems (
      v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN user_unassigned_systems_ref;


   TYPE user_unassigned_roles_rec IS RECORD
   (
      SRLS_CODE       TQC_SYS_ROLES.SRLS_CODE%TYPE,
      SRLS_SYS_CODE   TQC_SYS_ROLES.SRLS_SYS_CODE%TYPE,
      SRLS_NAME       TQC_SYS_ROLES.SRLS_NAME%TYPE,
      SRLS_STATUS     TQC_SYS_ROLES.SRLS_STATUS%TYPE
   );

   TYPE user_unassigned_roles_ref IS REF CURSOR
      RETURN user_unassigned_roles_rec;

   FUNCTION get_user_unassigned_roles (
      v_user_code   IN TQC_USERS.USR_CODE%TYPE,
      v_sys_code    IN TQC_SYSTEMS.SYS_CODE%TYPE)
      RETURN user_unassigned_roles_ref;

   FUNCTION get_user_assigned_roles (
      v_user_code   IN TQC_USERS.USR_CODE%TYPE,
      v_sys_code    IN TQC_SYSTEMS.SYS_CODE%TYPE)
      RETURN user_unassigned_roles_ref;



   TYPE user_unassigned_divisions_rec IS RECORD
   (
      DIV_CODE              TQC_DIVISIONS.DIV_CODE%TYPE,
      DIV_NAME              TQC_DIVISIONS.DIV_NAME%TYPE,
      DIV_SHT_DESC          TQC_DIVISIONS.DIV_SHT_DESC%TYPE,
      DIV_DIVISION_STATUS   TQC_DIVISIONS.DIV_DIVISION_STATUS%TYPE
   );

   TYPE user_unassigned_divisions_ref IS REF CURSOR
      RETURN user_unassigned_divisions_rec;

   FUNCTION get_user_unassigned_divisions (
      v_user_code     IN TQC_USERS.USR_CODE%TYPE,
      v_branch_code   IN TQC_BRANCHES.BRN_CODE%TYPE)
      RETURN user_unassigned_divisions_ref;

   FUNCTION get_user_assigned_divisions (
      v_user_code     IN TQC_USERS.USR_CODE%TYPE,
      v_branch_code   IN TQC_BRANCHES.BRN_CODE%TYPE)
      RETURN user_unassigned_divisions_ref;



   TYPE Sys_Processes_rec IS RECORD
   (
      SPRC_CODE      TQC_SYS_PROCESSES.SPRC_CODE%TYPE,
      SPRC_PROCESS   TQC_SYS_PROCESSES.SPRC_PROCESS%TYPE
   );

   TYPE Sys_Processes_ref IS REF CURSOR
      RETURN Sys_Processes_rec;

   FUNCTION Get_System_Processes (
      v_sysCode IN TQC_SYS_PROCESSES.SPRC_SYS_CODE%TYPE)
      RETURN Sys_Processes_ref;

   TYPE Organizations_rec IS RECORD
   (
      ORG_CODE       TQC_ORGANIZATIONS.ORG_CODE%TYPE,
      ORG_SHT_DESC   TQC_ORGANIZATIONS.ORG_SHT_DESC%TYPE,
      ORG_NAME       TQC_ORGANIZATIONS.ORG_NAME%TYPE
   );

   TYPE Organizations_ref IS REF CURSOR
      RETURN Organizations_rec;

   FUNCTION GetOrganizations
      RETURN Organizations_ref;

   FUNCTION Get_user_Orgs (v_usrCode IN TQC_USER_BRANCHES.USB_USR_CODE%TYPE)
      RETURN Organizations_ref;

   FUNCTION Get_user_assigned_Orgs (v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN Organizations_ref;

   TYPE Regions_rec IS RECORD
   (
      REG_CODE       TQC_REGIONS.REG_CODE%TYPE,
      REG_ORG_CODE   TQC_REGIONS.REG_ORG_CODE%TYPE,
      REG_NAME       TQC_REGIONS.REG_NAME%TYPE
   );

   TYPE Regions_ref IS REF CURSOR
      RETURN Regions_rec;

   FUNCTION GetRegions (v_orgCode IN TQC_REGIONS.REG_ORG_CODE%TYPE)
      RETURN Regions_ref;

   FUNCTION Get_User_assigned_Regions (
      v_orgCode     IN TQC_REGIONS.REG_ORG_CODE%TYPE,
      v_user_code   IN TQC_USERS.USR_CODE%TYPE)
      RETURN Regions_ref;

   TYPE Branches_rec IS RECORD
   (
      BRN_CODE       TQC_BRANCHES.BRN_CODE%TYPE,
      BRN_SHT_DESC   TQC_BRANCHES.BRN_SHT_DESC%TYPE,
      BRN_REG_CODE   TQC_BRANCHES.BRN_REG_CODE%TYPE,
      BRN_NAME       TQC_BRANCHES.BRN_NAME%TYPE
   );

   TYPE Branches_ref IS REF CURSOR
      RETURN Branches_rec;

   FUNCTION GetBranches (v_regCode IN TQC_BRANCHES.BRN_REG_CODE%TYPE)
      RETURN Branches_ref;

   FUNCTION get_user_unassigned_branches (
      v_user_code   IN TQC_USERS.USR_CODE%TYPE,
      v_reg_code    IN TQC_REGIONS.REG_CODE%TYPE)
      RETURN Branches_ref;

   FUNCTION get_user_assigned_branches (
      v_user_code   IN TQC_USERS.USR_CODE%TYPE,
      v_reg_code    IN TQC_REGIONS.REG_CODE%TYPE)
      RETURN Branches_ref;

   TYPE UsrBranches_rec IS RECORD
   (
      USB_CODE       TQC_USER_BRANCHES.USB_CODE%TYPE,
      USB_BRN_CODE   TQC_USER_BRANCHES.USB_BRN_CODE%TYPE,
      USB_USR_CODE   TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
      USB_STATUS     TQC_USER_BRANCHES.USB_STATUS%TYPE,
      USB_DFLT_BRN   TQC_USER_BRANCHES.USB_DFLT_BRN%TYPE,
      BRN_NAME       TQC_BRANCHES.BRN_NAME%TYPE
   );

   TYPE UsrBranches_ref IS REF CURSOR
      RETURN UsrBranches_rec;

   FUNCTION GetUserBranches (
      v_usrCode   IN TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
      v_OrgCode   IN TQC_ORGANIZATIONS.ORG_CODE%TYPE)
      RETURN UsrBranches_ref;

   FUNCTION get_user_branches (
      v_usrCode IN TQC_USER_BRANCHES.USB_USR_CODE%TYPE)
      RETURN UsrBranches_ref;

   FUNCTION GetBranchesbyOrg (v_orgCode IN TQC_ORGANIZATIONS.ORG_CODE%TYPE)
      RETURN Branches_ref;


   TYPE default_branch_rec IS RECORD
   (
      ORG_CODE       TQC_ORGANIZATIONS.ORG_CODE%TYPE,
      ORG_NAME       TQC_ORGANIZATIONS.ORG_NAME%TYPE,
      REG_CODE       TQC_REGIONS.REG_CODE%TYPE,
      REG_NAME       TQC_REGIONS.REG_NAME%TYPE,
      BRN_CODE       TQC_BRANCHES.BRN_CODE%TYPE,
      BRN_NAME       TQC_BRANCHES.BRN_NAME%TYPE,
      ORG_CUR_CODE   TQC_ORGANIZATIONS.ORG_CUR_CODE%TYPE,
      CUR_DESC       TQC_CURRENCIES.CUR_DESC%TYPE
   );

   TYPE default_branch_ref IS REF CURSOR
      RETURN default_branch_rec;

   TYPE default_details_rec IS RECORD
   (
      ORG_CODE             TQC_ORGANIZATIONS.ORG_CODE%TYPE,
      ORG_NAME             TQC_ORGANIZATIONS.ORG_NAME%TYPE,
      REG_CODE             TQC_REGIONS.REG_CODE%TYPE,
      REG_NAME             TQC_REGIONS.REG_NAME%TYPE,
      BRN_CODE             TQC_BRANCHES.BRN_CODE%TYPE,
      BRN_NAME             TQC_BRANCHES.BRN_NAME%TYPE,
      ORG_COU_CODE         TQC_ORGANIZATIONS.ORG_COU_CODE%TYPE,
      COU_NAME             TQC_COUNTRIES.COU_NAME%TYPE,
      COU_ADMIN_REG_TYPE   TQC_COUNTRIES.COU_ADMIN_REG_TYPE%TYPE
   );

   TYPE default_details_ref IS REF CURSOR
      RETURN default_details_rec;

   FUNCTION get_user_defaults (v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN default_details_ref;

   FUNCTION get_default_user_branch (v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN default_branch_ref;

   FUNCTION get_nondefault_user_branch (
      v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN default_branch_ref;


   TYPE default_division_rec IS RECORD
   (
      ORG_CODE   TQC_ORGANIZATIONS.ORG_CODE%TYPE,
      ORG_NAME   TQC_ORGANIZATIONS.ORG_NAME%TYPE,
      REG_CODE   TQC_REGIONS.REG_CODE%TYPE,
      REG_NAME   TQC_REGIONS.REG_NAME%TYPE,
      BRN_CODE   TQC_BRANCHES.BRN_CODE%TYPE,
      BRN_NAME   TQC_BRANCHES.BRN_NAME%TYPE,
      DIV_CODE   TQC_DIVISIONS.DIV_CODE%TYPE,
      DIV_NAME   TQC_DIVISIONS.DIV_NAME%TYPE
   );

   TYPE default_division_ref IS REF CURSOR
      RETURN default_division_rec;

   FUNCTION get_default_user_division (
      v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN default_division_ref;

   FUNCTION get_nondefault_user_division (
      v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN default_division_ref;


   PROCEDURE Create_User (
      v_username              IN     TQC_USERS.USR_USERNAME%TYPE,
      v_names                 IN     TQC_USERS.USR_NAME%TYPE,
      v_password              IN     TQC_USERS.USR_PWD%TYPE,
      v_email                 IN     TQC_USERS.USR_EMAIL%TYPE,
      v_personel              IN     TQC_USERS.USR_PER_RANK_SHT_DESC%TYPE,
      v_status                IN     TQC_USERS.USR_STATUS%TYPE,
      v_type                  IN     TQC_USERS.USR_TYPE%TYPE,
      v_reset                 IN     TQC_USERS.USR_PWD_RESET%TYPE,
      v_per_id                IN     TQC_USERS.USR_PER_ID%TYPE,
      v_user_code                OUT TQC_USERS.USR_CODE%TYPE,
      v_usr_acct_mgr          IN     TQC_USERS.USR_ACCT_MGR%TYPE DEFAULT NULL,
      v_usr_signature         IN     TQC_USERS.USR_SIGNATURE%TYPE DEFAULT NULL,
      v_usr_cell_phone_no     IN     TQC_USERS.USR_CELL_PHONE_NO%TYPE DEFAULT NULL,
      V_USR_WEF_DT            IN     TQC_USERS.USR_WEF_DT%TYPE DEFAULT NULL,
      V_USR_WET_DT            IN     TQC_USERS.USR_WET_DT%TYPE DEFAULT NULL,
      V_USR_SECQ_CODE         IN     TQC_USERS.USR_SECQ_CODE%TYPE DEFAULT NULL,
      V_USR_SECURITY_ANSWER   IN     TQC_USERS.USR_SECURITY_ANSWER%TYPE DEFAULT NULL);

   PROCEDURE UpDate_User (
      v_user                  IN TQC_USERS.USR_CODE%TYPE,
      v_username              IN TQC_USERS.USR_USERNAME%TYPE,
      v_names                 IN TQC_USERS.USR_NAME%TYPE,
      v_email                 IN TQC_USERS.USR_EMAIL%TYPE,
      v_personel              IN TQC_USERS.USR_PER_RANK_SHT_DESC%TYPE,
      v_type                  IN TQC_USERS.USR_TYPE%TYPE,
      v_reset                 IN TQC_USERS.USR_PWD_RESET%TYPE,
      v_per_id                IN TQC_USERS.USR_PER_ID%TYPE,
      v_status                IN TQC_USERS.USR_STATUS%TYPE,
      v_usr_acct_mgr          IN TQC_USERS.USR_ACCT_MGR%TYPE,
      v_usr_cell_phone_no     IN TQC_USERS.USR_CELL_PHONE_NO%TYPE,
      v_USR_WEF_DT            IN TQC_USERS.USR_WEF_DT%TYPE,
      v_USR_WET_DT            IN TQC_USERS.USR_WET_DT%TYPE,
      v_USR_SECQ_CODE         IN TQC_USERS.USR_SECQ_CODE%TYPE,
      v_USR_SECURITY_ANSWER   IN TQC_USERS.USR_SECURITY_ANSWER%TYPE);

   PROCEDURE Drop_User (v_user IN TQC_USERS.USR_CODE%TYPE);

   PROCEDURE Restore_User (v_user IN TQC_USERS.USR_CODE%TYPE);

   PROCEDURE Grant_User_System (v_user       IN TQC_USERS.USR_CODE%TYPE,
                                v_sys_code   IN TQC_SYSTEMS.SYS_CODE%TYPE);

   PROCEDURE Revoke_User_System (v_user       IN TQC_USERS.USR_CODE%TYPE,
                                 v_sys_code   IN TQC_SYSTEMS.SYS_CODE%TYPE);

   PROCEDURE Revoke_User_Role (v_userCode   IN TQC_USERS.USR_CODE%TYPE,
                               v_user       IN TQC_USERS.USR_CODE%TYPE,
                               v_role       IN TQC_SYS_ROLES.SRLS_CODE%TYPE);

   PROCEDURE Grant_User_Role (v_userCode   IN TQC_USERS.USR_CODE%TYPE,
                              v_user       IN TQC_USERS.USR_CODE%TYPE,
                              v_role       IN TQC_SYS_ROLES.SRLS_CODE%TYPE);

    PROCEDURE Grant_User_Role (v_sys_code       IN TQC_SYSTEMS.SYS_CODE%TYPE,
                           v_username       IN  TQC_USERS.USR_USERNAME%TYPE,
                           v_srls_sht_desc           IN  TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
                           v_grant          IN VARCHAR2);
   PROCEDURE Grant_Role_Process_Area (
      v_processArea   IN TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
      v_roleCode      IN TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE);

   PROCEDURE Revoke_Role_Process_Area (
      v_processArea   IN TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
      v_role_code     IN TQC_SYS_ROLES.SRLS_CODE%TYPE);

   PROCEDURE Grant_Role_Process_SubArea (
      v_processsubArea   IN TQC_SYS_PROCESS_SUB_AREAS.SPRSA_CODE%TYPE,
      v_roleCode         IN TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE,
      v_debitLimit       IN TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_DEBIT_LIMIT%TYPE,
      v_creditLimit      IN TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CREDIT_LIMIT%TYPE);


   PROCEDURE Revoke_Role_Process_SubArea (
      v_processsubArea   IN TQC_SYS_PROCESS_SUB_AREAS.SPRSA_CODE%TYPE,
      v_role_code        IN TQC_SYS_ROLES.SRLS_CODE%TYPE);

   PROCEDURE Creat_Role (
      v_sysCode         IN TQC_SYS_ROLES.SRLS_SYS_CODE%TYPE,
      v_role_name       IN TQC_SYS_ROLES.SRLS_NAME%TYPE,
      v_role_sht_Desc   IN TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
      v_Process         IN TQC_SYS_ROLES_PROCESSES.SRPRC_SPRC_CODE%TYPE DEFAULT NULL);

   PROCEDURE Edit_Role (
      v_roleProcess     IN TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE,
      v_role_name       IN TQC_SYS_ROLES.SRLS_NAME%TYPE,
      v_role_sht_Desc   IN TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
      v_status          IN TQC_SYS_ROLES.SRLS_STATUS%TYPE);

   PROCEDURE Restore_Role (
      v_roleProcess IN TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE);

   PROCEDURE Drop_Role (
      v_roleProcess IN TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE);

   PROCEDURE Edit_User_SysWet (
      v_sysCode   IN TQC_USER_SYSTEMS.USYS_CODE%TYPE,
      v_wet       IN TQC_USER_SYSTEMS.USYS_WET%TYPE);

   PROCEDURE Grant_User_Branch (
      v_user          IN TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
      v_branch_code   IN TQC_USER_BRANCHES.USB_BRN_CODE%TYPE);

   PROCEDURE Revoke_User_Branch (
      v_user          IN TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
      v_branch_code   IN TQC_USER_BRANCHES.USB_BRN_CODE%TYPE);

   PROCEDURE enableDisableBranch (
      v_usrBranchCode IN TQC_USER_BRANCHES.USB_CODE%TYPE);

   PROCEDURE makeDefaultBranch (
      v_usrBranchCode IN TQC_USER_BRANCHES.USB_CODE%TYPE);


   PROCEDURE grantProcessRole (
      v_processCode    IN TQC_SYS_PROCESSES.SPRC_CODE%TYPE,
      v_roleCode       IN TQC_SYS_ROLES.SRLS_CODE%TYPE,
      v_process_only   IN VARCHAR2 DEFAULT 'N');

   PROCEDURE revokeProcessRole (
      v_processCode   IN TQC_SYS_ROLES_PROCESSES.SRPRC_SPRC_CODE%TYPE,
      v_roleCode      IN TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE);


   PROCEDURE assign_user_default_branch (
      v_user_code     IN TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
      v_branch_code   IN TQC_USER_BRANCHES.USB_BRN_CODE%TYPE);

   PROCEDURE assign_user_default_division (
      v_user_code       IN TQC_USER_DIVISIONS.USD_USR_CODE%TYPE,
      v_division_code   IN TQC_USER_DIVISIONS.USD_DIV_CODE%TYPE);

   TYPE userAssignedSystems_rec IS RECORD
   (
      SYS_CODE          TQC_SYSTEMS.SYS_CODE%TYPE,
      SYS_SHT_DESC      TQC_SYSTEMS.SYS_SHT_DESC%TYPE,
      SYS_NAME          TQC_SYSTEMS.SYS_NAME%TYPE,
      SYS_ACTIVE        TQC_SYSTEMS.SYS_ACTIVE%TYPE,
      USYS_CODE         TQC_USER_SYSTEMS.USYS_CODE%TYPE,
      USYS_USR_CODE     TQC_USER_SYSTEMS.USYS_USR_CODE%TYPE,
      USYS_SYS_CODE     TQC_USER_SYSTEMS.USYS_SYS_CODE%TYPE,
      USYS_WEF          TQC_USER_SYSTEMS.USYS_WEF%TYPE,
      USYS_WET          TQC_USER_SYSTEMS.USYS_WET%TYPE,
      USYS_SPOST_CODE   TQC_USER_SYSTEMS.USYS_SPOST_CODE%TYPE,
      SPOST_DESC        VARCHAR2 (50 BYTE)
   );

   TYPE userAssignedSystems_ref IS REF CURSOR
      RETURN userAssignedSystems_rec;

   FUNCTION getAllUserAssignedSystems (v_user_code NUMBER)
      RETURN userAssignedSystems_ref;

   FUNCTION SYS_POST_NAME (v_spost_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION GetRegionsFiltered (
      v_orgCode     IN TQC_REGIONS.REG_ORG_CODE%TYPE,
      v_user_code      TQC_USER_BRANCHES.USB_USR_CODE%TYPE)
      RETURN Regions_ref;

   PROCEDURE Grant_User_BranchByOrg (
      v_user       IN TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
      v_org_code   IN TQC_REGIONS.REG_ORG_CODE%TYPE);

   PROCEDURE Revoke_User_BranchByOrg (
      v_user       IN     TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
      v_org_code   IN     TQC_REGIONS.REG_ORG_CODE%TYPE,
      v_err           OUT VARCHAR2);

   FUNCTION Get_user_OrgsFiltered (
      v_usrCode IN TQC_USER_BRANCHES.USB_USR_CODE%TYPE)
      RETURN Organizations_ref;

   PROCEDURE Grant_User_AllRoleInSys (
      v_userCode   IN TQC_USERS.USR_CODE%TYPE,
      v_user       IN TQC_USERS.USR_CODE%TYPE,
      v_role       IN TQC_SYS_ROLES.SRLS_CODE%TYPE,
      v_sys_code   IN TQC_SYSTEMS.SYS_CODE%TYPE);

   PROCEDURE Revoke_User_AllRoleInSys (
      v_userCode   IN TQC_USERS.USR_CODE%TYPE,
      v_user       IN TQC_USERS.USR_CODE%TYPE,
      v_role       IN TQC_SYS_ROLES.SRLS_CODE%TYPE,
      v_sys_code   IN TQC_SYSTEMS.SYS_CODE%TYPE);

   FUNCTION get_AssignedSystems_withRole (
      v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN user_unassigned_systems_ref;

   FUNCTION get_UnAssignedSystems_withRole (
      v_user_code IN TQC_USERS.USR_CODE%TYPE)
      RETURN user_unassigned_systems_ref;

   PROCEDURE get_usersNotInGrp (v_Users_ref      OUT Users_ref,
                                v_grp_code    IN     NUMBER);

   FUNCTION get_UsrEmail (v_user_name VARCHAR2)
      RETURN VARCHAR2;

   FUNCTION get_UsrEmailAddr (v_user_code NUMBER)
      RETURN VARCHAR2;

   PROCEDURE getFullUserDetails (
      v_userName      IN     TQC_USERS.USR_USERNAME%TYPE,
      v_userDetails      OUT Users_ref);

   PROCEDURE create_system_administrator (v_user VARCHAR2);

   PROCEDURE update_user_pass (v_user       IN tqc_users.USR_USERNAME%TYPE,
                               v_password   IN tqc_users.usr_pwd%TYPE);

   PROCEDURE create_missing_prc_s_areas (
      v_sys_code          TQC_SYSTEMS.SYS_CODE%TYPE,
      v_sprc_sht_desc     TQC_SYS_PROCESSES.SPRC_SHT_DESC%TYPE,
      v_sprc_process      TQC_SYS_PROCESSES.SPRC_PROCESS%TYPE,
      v_sprca_sht_desc    TQC_SYS_PROCESS_AREAS.SPRCA_SHT_DESC%TYPE,
      v_sprca_area        TQC_SYS_PROCESS_AREAS.SPRCA_AREA%TYPE,
      v_sprsa_sht_desc    TQC_SYS_PROCESS_SUB_AREAS.SPRsA_SHT_DESC%TYPE,
      v_sprsa_sub_area    TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SUB_AREA%TYPE,
      v_sprsa_type        TQC_SYS_PROCESS_SUB_AREAS.SPRSA_TYPE%TYPE);

   PROCEDURE grant_process_sub_area (
      v_sys_code          TQC_SYSTEMS.SYS_CODE%TYPE,
      v_srls_sht_desc     TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
      v_srls_name         TQC_SYS_ROLES.SRLS_NAME%TYPE,
      v_sprc_sht_desc     TQC_SYS_PROCESSES.SPRC_SHT_DESC%TYPE,
      v_sprca_sht_desc    TQC_SYS_PROCESS_AREAS.SPRCA_SHT_DESC%TYPE,
      v_sprsa_sht_desc    TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SHT_DESC%TYPE,
      v_grant             VARCHAR2,
      v_debit_limit       TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_DEBIT_LIMIT%TYPE,
      v_credit_limit      TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CREDIT_LIMIT%TYPE);
      
      TYPE web_users_rec IS RECORD
   (
      ACCC_CODE              TQC_ACCOUNT_CONTACTS.ACCC_CODE%TYPE,
      ACCC_NAME              TQC_ACCOUNT_CONTACTS.ACCC_NAME%TYPE,
       ACCC_USERNAME              TQC_ACCOUNT_CONTACTS.ACCC_USERNAME%TYPE,
       ACCC_STATUS              TQC_ACCOUNT_CONTACTS.ACCC_STATUS%TYPE,
       ACCC_PERSONEL_RANK              TQC_ACCOUNT_CONTACTS.ACCC_PERSONEL_RANK%TYPE
   );

   TYPE web_users_ref IS REF CURSOR
      RETURN web_users_rec;
	  
	  
	  FUNCTION get_web_users(v_agent_code IN NUMBER)
      RETURN web_users_ref;
END TQC_ROLES_CURSOR030215; 
 
/