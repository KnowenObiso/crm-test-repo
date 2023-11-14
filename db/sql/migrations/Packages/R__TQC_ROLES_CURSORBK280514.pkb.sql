--
-- TQC_ROLES_CURSORBK280514  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_ROLES_CURSORBK280514 AS
/******************************************************************************
   NAME:       TQC_ROLES_CURSOR
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        08/Oct/2009             1. Created this package body.
******************************************************************************/

PROCEDURE RAISE_ERROR(v_msg IN VARCHAR2,v_err_no IN NUMBER:=NULL) IS
    BEGIN
        IF v_err_no IS NULL THEN
            RAISE_APPLICATION_ERROR(-20033, v_msg||' - '||SQLERRM(SQLCODE));
        ELSE
            RAISE_APPLICATION_ERROR(v_err_no, v_msg||' - '||SQLERRM(SQLCODE));
        END IF;
    END RAISE_ERROR;
    
FUNCTION Get_Sys_Roles(v_syscode IN TQC_SYSTEMS.SYS_CODE%TYPE) RETURN  Sys_roles_ref IS
    vcursor Sys_roles_ref;
BEGIN
     OPEN vcursor FOR
     SELECT SRLS_CODE, SRLS_NAME, SRLS_CRT_DATE, SRLS_SHT_DESC, SRLS_STATUS 
     FROM TQC_SYS_ROLES
     WHERE SRLS_SYS_CODE =v_syscode
     AND SRLS_STATUS = 'A';
     return vcursor;
END;

FUNCTION Get_User_Roles(v_syscode IN TQC_SYSTEMS.SYS_CODE%TYPE,
                        v_user    IN  TQC_USERS.USR_CODE%TYPE) RETURN  Sys_UsrRoles_ref IS
    vcursor Sys_UsrRoles_ref;
BEGIN
     OPEN vcursor FOR
     SELECT SPRC_CODE, SRPRC_CODE, SRLS_NAME, SPRC_PROCESS ,SRLS_CRT_DATE, SRLS_STATUS, SRLS_CODE, SRLS_SHT_DESC
     FROM TQC_SYS_ROLES,TQC_SYS_ROLES_PROCESSES,TQC_SYS_PROCESSES,TQC_SYS_USER_ROLES
     WHERE SRPRC_SRLS_CODE = SRLS_CODE
     AND SRPRC_SPRC_CODE = SPRC_CODE
     AND USRR_SRLS_CODE = SRLS_CODE
     AND SPRC_SYS_CODE = NVL (v_syscode, SPRC_SYS_CODE)
     AND USRR_USR_CODE = v_user
     AND USRR_REVOKE_BY_USR_CODE IS NULL;
     return vcursor;
END;

FUNCTION GetRoleProcesses (v_sysCode    IN  TQC_SYS_PROCESSES.SPRC_SYS_CODE%TYPE,
                           v_sysRoleCode    IN  TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE) RETURN Role_Processes_ref IS
    vcursor Role_Processes_ref;
BEGIN
--RAISE_ERROR('System '||v_sysCode ||' Role '||v_sysRoleCode);
    OPEN vcursor FOR
    
    SELECT SPRC_CODE,SRPRC_CODE,SPRC_PROCESS,DECODE(SRPRC_SPRC_CODE,NULL,'N','Y') PROCESS_ASSIGNED,SRPRC_SRLS_CODE
    FROM TQC_SYS_PROCESSES,TQC_SYS_ROLES_PROCESSES
    WHERE SPRC_CODE = SRPRC_SPRC_CODE(+)
    AND SRPRC_SRLS_CODE(+) = v_sysRoleCode
    AND SPRC_SYS_CODE = v_sysCode
    ORDER BY SPRC_PROCESS;
    return vcursor;
END;
  

FUNCTION Get_Role_Areas(v_Process       IN TQC_SYS_PROCESSES.SPRC_CODE%TYPE,
                        v_roleProcess   IN TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE) RETURN  Role_Areas_ref IS
     vcursor Role_Areas_ref;
BEGIN
     OPEN vcursor FOR
     SELECT DISTINCT SPRCA_CODE,SPRCA_AREA,SRPRA_CODE,SRPRC_CODE
     FROM TQC_SYS_ROLES_PRCS_AREA,TQC_SYS_ROLES_PROCESSES,TQC_SYS_PROCESS_AREAS 
     WHERE SRPRC_CODE(+) = SRPRA_SRPRC_CODE
     AND SPRCA_CODE = SRPRA_SPRCA_CODE(+)
     AND SPRCA_SPRC_CODE = v_Process;
         return vcursor;
END;

FUNCTION GetProcessRoleArea  (v_Process       IN TQC_SYS_PROCESSES.SPRC_CODE%TYPE,
                              v_sysRoleCode    IN  TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE) RETURN  ProcessRole_Areas_ref IS
     vcursor ProcessRole_Areas_ref;                
BEGIN
     OPEN vcursor FOR
     SELECT SPRCA_CODE,SPRCA_AREA,SRPRA_SPRCA_CODE,SRPRA_CODE,SRPRC_CODE,
     DECODE(SRPRA_SPRCA_CODE,NULL,'N','Y') AREA_ASSIGNED
     FROM TQC_SYS_PROCESSES,TQC_SYS_PROCESS_AREAS,
        (SELECT SRPRC_SPRC_CODE,SRPRA_SPRCA_CODE,SRPRA_CODE,SRPRC_CODE
         FROM TQC_SYS_ROLES_PROCESSES,TQC_SYS_ROLES_PRCS_AREA
         WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
         AND SRPRC_SRLS_CODE = v_sysRoleCode)
         WHERE SPRC_CODE = SPRCA_SPRC_CODE
         AND SPRCA_SPRC_CODE = v_Process
         AND SPRCA_SPRC_CODE = SRPRC_SPRC_CODE(+)
         AND SPRCA_CODE = SRPRA_SPRCA_CODE(+);
    return vcursor;
END;


FUNCTION Get_Role_SubAreas(v_ProcessArea       IN TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
                           v_roleProcessArea   IN TQC_SYS_ROLES_PRCS_AREA.SRPRA_SRPRC_CODE%TYPE) RETURN  Role_SubAreas_ref IS
    vcursor Role_SubAreas_ref;
BEGIN
     OPEN vcursor FOR
     SELECT SRPSA_CODE,SPRSA_CODE,SPRSA_SUB_AREA, SPRSA_TYPE,SRPSA_DEBIT_LIMIT, SRPSA_CREDIT_LIMIT 
     FROM TQC_SYS_PROCESS_SUB_AREAS,TQC_SYS_ROLES_PRCS_S_AREA,TQC_SYS_PROCESS_AREAS
     WHERE SPRSA_CODE = SRPSA_SPRSA_CODE(+)
     AND SPRCA_CODE = SPRSA_SPRCA_CODE
     AND SPRSA_SPRCA_CODE = v_ProcessArea;
     --AND (SRPSA_SPRSA_CODE = v_roleProcessArea OR SRPSA_CODE IS NULL);
     return vcursor;
END;

FUNCTION GetProcessRole_SubAreas(v_ProcessArea       IN TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
                                 v_sysRoleCode       IN  TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE,
                                 v_Process           IN TQC_SYS_PROCESSES.SPRC_CODE%TYPE) RETURN  ProcessRole_SubAreas_ref IS
 vcursor ProcessRole_SubAreas_ref;
BEGIN
--RAISE_ERROR('Process Area='||v_ProcessArea||'SysRoleCode='||v_sysRoleCode||'Process='||v_Process);
     OPEN vcursor FOR
     
     SELECT SRPSA_CODE,SPRSA_CODE,SPRSA_SUB_AREA, SPRSA_TYPE,SRPSA_DEBIT_LIMIT, SRPSA_CREDIT_LIMIT ,
     DECODE(SRPSA_SPRSA_CODE,NULL,'N','Y') S_AREA_ASSIGNED
     FROM TQC_SYS_PROCESSES,TQC_SYS_PROCESS_AREAS,TQC_SYS_PROCESS_SUB_AREAS,
            (SELECT SRPSA_CODE,SRPRC_SPRC_CODE,SRPRA_SPRCA_CODE,SRPSA_SPRSA_CODE,SRPSA_DEBIT_LIMIT, SRPSA_CREDIT_LIMIT--,SRPSA_SRPRA_CODE
            FROM TQC_SYS_ROLES_PROCESSES,TQC_SYS_ROLES_PRCS_AREA,TQC_SYS_ROLES_PRCS_S_AREA
            WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
            AND SRPRA_CODE = SRPSA_SRPRA_CODE
            AND SRPRC_SRLS_CODE = v_sysRoleCode
            --AND SRPRA_SPRCA_CODE =v_ProcessArea
            )
     WHERE SPRC_CODE = SPRCA_SPRC_CODE
     AND SPRCA_CODE = SPRSA_SPRCA_CODE
     AND SPRSA_SPRC_CODE = SRPRC_SPRC_CODE(+)
     AND SPRSA_SPRCA_CODE = SRPRA_SPRCA_CODE(+)
     AND SPRSA_CODE = SRPSA_SPRSA_CODE(+)
     --AND SPRCA_SPRC_CODE = v_Process
     AND SPRCA_CODE = v_ProcessArea;
     return vcursor;
END;    

FUNCTION Get_Users RETURN  Users_ref IS
    vcursor Users_ref;
BEGIN
     OPEN vcursor FOR
     SELECT USR_CODE,USR_USERNAME,USR_NAME,USR_EMAIL,USR_PERSONEL_RANK,
     USR_DT_CREATED,USR_TYPE,USR_STATUS,USR_PWD_RESET, USR_PER_ID,USR_ACCT_MGR,USR_CELL_PHONE_NO,
     USR_WEF_DT, USR_WET_DT,NULL,NULL
     FROM TQC_USERS;
     return vcursor;
END;
    FUNCTION get_all_individual_users RETURN Users_ref
    IS
   vcursor Users_ref;
BEGIN
     OPEN vcursor FOR
     SELECT USR_CODE,USR_USERNAME,USR_NAME,USR_EMAIL,USR_PERSONEL_RANK,
     USR_DT_CREATED,USR_TYPE,USR_STATUS,USR_PWD_RESET, USR_PER_ID,USR_ACCT_MGR,USR_CELL_PHONE_NO,
     USR_WEF_DT, USR_WET_DT,NULL,NULL
     FROM TQC_USERS
     WHERE USR_TYPE='U';
     return vcursor;
END;
PROCEDURE get_all_users(v_Users_ref OUT Users_ref,v_date IN VARCHAR2 )
      
  IS
      
  BEGIN
 -- raise_error('DATE---->'|| v_date );
      IF v_date IS NULL THEN
          OPEN v_Users_ref FOR
          SELECT USR_CODE, USR_USERNAME, USR_NAME, USR_EMAIL, USR_PERSONEL_RANK,
             USR_DT_CREATED, USR_TYPE, USR_STATUS, USR_PWD_RESET, USR_PER_ID,USR_ACCT_MGR,USR_CELL_PHONE_NO,
             USR_WEF_DT, USR_WET_DT,SECQ_DESC, USR_SECURITY_ANSWER
          FROM TQC_USERS,TQC_SECURITY_QUESTIONS
          WHERE USR_SECQ_CODE=SECQ_CODE(+)  order by USR_CODE  Desc;
     ELSE 
          OPEN v_Users_ref FOR
          SELECT USR_CODE, USR_USERNAME, USR_NAME, USR_EMAIL, USR_PERSONEL_RANK,
             USR_DT_CREATED, USR_TYPE, USR_STATUS, USR_PWD_RESET, USR_PER_ID,USR_ACCT_MGR,USR_CELL_PHONE_NO,
             USR_WEF_DT, USR_WET_DT,SECQ_DESC, USR_SECURITY_ANSWER
          FROM TQC_USERS,TQC_SECURITY_QUESTIONS WHERE USR_SECQ_CODE=SECQ_CODE(+)  AND   USR_DT_CREATED LIKE '%'||to_date(v_date,'dd/mm/yyyy')||'%'   order by USR_CODE   Desc;
     END IF;
     
 END get_all_users;  
 PROCEDURE get_AccountOfficers(v_Users_ref OUT Users_ref)
      
  IS
      
  BEGIN
      
  OPEN v_Users_ref FOR
  SELECT USR_CODE, USR_USERNAME, USR_NAME, USR_EMAIL, USR_PERSONEL_RANK,
     USR_DT_CREATED, USR_TYPE, USR_STATUS, USR_PWD_RESET, USR_PER_ID,USR_ACCT_MGR,USR_CELL_PHONE_NO,
     USR_WEF_DT, USR_WET_DT,NULL,NULL
  FROM TQC_USERS where USR_CODE IN (SELECT CLNT_ACC_OFFICER FROM tqc_clients where CLNT_ACC_OFFICER IS NOT NULL);
     
 END get_AccountOfficers;

FUNCTION Get_Systems RETURN  Systems_ref IS
    vcursor Systems_ref;
BEGIN
     OPEN vcursor FOR
     SELECT SYS_CODE,SYS_SHT_DESC,SYS_NAME,SYS_ACTIVE
     FROM TQC_SYSTEMS
     WHERE SYS_ACTIVE <> 'N'
     AND SYS_CODE <>30;
    
     return vcursor;
END;

FUNCTION Get_User_Systems (v_user   IN TQC_USERS.USR_CODE%TYPE)RETURN  User_Systems_ref IS
    vcursor User_Systems_ref;
BEGIN
     OPEN vcursor FOR
     SELECT USYS_CODE,SYS_CODE,SYS_SHT_DESC,USYS_WEF,SYS_NAME,USYS_WET,SYS_PATH
     FROM TQC_SYSTEMS,TQC_USER_SYSTEMS
     WHERE SYS_CODE = USYS_SYS_CODE
     AND USYS_USR_CODE = v_user
     AND (USYS_WET IS NULL OR USYS_WET >= SYSDATE);
     return vcursor;
END;

FUNCTION Get_user_assigned_Orgs(
      v_user_code   IN TQC_USERS.USR_CODE%TYPE) 
      RETURN Organizations_ref 
      IS vcursor Organizations_ref;
BEGIN
    OPEN vcursor FOR
      SELECT DISTINCT ORG_CODE, ORG_SHT_DESC, ORG_NAME
      FROM TQC_ORGANIZATIONS, TQC_REGIONS,TQC_BRANCHES,TQC_USER_BRANCHES
      WHERE ORG_CODE = REG_ORG_CODE
      AND REG_CODE = BRN_REG_CODE
      AND USB_BRN_CODE = BRN_CODE
      AND USB_USR_CODE = v_user_code;
    RETURN vcursor;
END; 

FUNCTION Get_User_assigned_Regions(
      v_orgCode   IN  TQC_REGIONS.REG_ORG_CODE%TYPE,
      v_user_code   IN TQC_USERS.USR_CODE%TYPE )
      RETURN Regions_ref 
      IS vcursor Regions_ref;
BEGIN
    OPEN vcursor FOR
      SELECT DISTINCT REG_CODE, REG_ORG_CODE, REG_NAME
      FROM TQC_REGIONS,TQC_BRANCHES,TQC_USER_BRANCHES
      WHERE BRN_REG_CODE = REG_CODE
      AND REG_ORG_CODE =  v_orgCode
      AND USB_BRN_CODE = BRN_CODE
      AND USB_USR_CODE = v_user_code;
    RETURN vcursor;
END; 

FUNCTION get_user_unassigned_systems (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE)
        RETURN  user_unassigned_systems_ref 
        IS vcursor user_unassigned_systems_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT SYS_CODE, SYS_SHT_DESC, SYS_NAME, SYS_ACTIVE
        FROM TQC_SYSTEMS
        WHERE SYS_ACTIVE <> 'N'
        AND NOT EXISTS
        (
            SELECT USYS_CODE, USYS_USR_CODE, USYS_SYS_CODE 
            FROM TQC_USER_SYSTEMS
            WHERE TQC_SYSTEMS.SYS_CODE = TQC_USER_SYSTEMS.USYS_SYS_CODE
            AND USYS_USR_CODE = v_user_code
        );
             
     return vcursor;
END;

FUNCTION get_user_assigned_systems (v_user_code   IN TQC_USERS.USR_CODE%TYPE) RETURN  user_unassigned_systems_ref 
        IS vcursor user_unassigned_systems_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT SYS_CODE, SYS_SHT_DESC, SYS_NAME, SYS_ACTIVE
        FROM TQC_SYSTEMS
        WHERE SYS_ACTIVE <> 'N'
        AND EXISTS
        (
            SELECT USYS_CODE, USYS_USR_CODE, USYS_SYS_CODE 
            FROM TQC_USER_SYSTEMS
            WHERE TQC_SYSTEMS.SYS_CODE = TQC_USER_SYSTEMS.USYS_SYS_CODE
            AND USYS_USR_CODE = v_user_code
        );
             
     return vcursor;
END;

FUNCTION get_user_unassigned_roles (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE,
        v_sys_code    IN TQC_SYSTEMS.SYS_CODE%TYPE)
        RETURN  user_unassigned_roles_ref 
        IS vcursor user_unassigned_roles_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT SRLS_CODE, SRLS_SYS_CODE, SRLS_NAME, SRLS_STATUS
        FROM TQC_SYS_ROLES
        WHERE SRLS_STATUS='A'
        AND SRLS_SYS_CODE = v_sys_code
        AND NOT EXISTS 
        (
            SELECT USRR_USR_CODE, USRR_SRLS_CODE
            FROM TQC_SYS_USER_ROLES
            WHERE TQC_SYS_ROLES.SRLS_CODE = TQC_SYS_USER_ROLES.USRR_SRLS_CODE
            AND USRR_USR_CODE = v_user_code
        );
             
     return vcursor;
END;

FUNCTION get_user_assigned_roles (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE,
        v_sys_code    IN TQC_SYSTEMS.SYS_CODE%TYPE)
        RETURN  user_unassigned_roles_ref 
        IS vcursor user_unassigned_roles_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT SRLS_CODE, SRLS_SYS_CODE, SRLS_NAME, SRLS_STATUS
        FROM TQC_SYS_ROLES
        WHERE SRLS_STATUS NOT IN('N','I')
        AND SRLS_SYS_CODE = v_sys_code
        AND EXISTS 
        (
            SELECT USRR_USR_CODE, USRR_SRLS_CODE
            FROM TQC_SYS_USER_ROLES
            WHERE TQC_SYS_ROLES.SRLS_CODE = TQC_SYS_USER_ROLES.USRR_SRLS_CODE
            AND USRR_USR_CODE = v_user_code
        );
             
     return vcursor;
END;

FUNCTION get_user_unassigned_branches (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE,
        v_reg_code    IN TQC_REGIONS.REG_CODE%TYPE)
        RETURN  Branches_ref 
        IS vcursor Branches_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT BRN_CODE, BRN_SHT_DESC, BRN_REG_CODE, BRN_NAME
        FROM TQC_BRANCHES
        WHERE BRN_REG_CODE = v_reg_code
        AND NOT EXISTS 
        (
            SELECT USB_CODE, USB_BRN_CODE, USB_USR_CODE, USB_STATUS, USB_DFLT_BRN
            FROM TQC_USER_BRANCHES
            WHERE TQC_BRANCHES.BRN_CODE = TQC_USER_BRANCHES.USB_BRN_CODE
            AND USB_USR_CODE = v_user_code
        );
             
     return vcursor;
END;

FUNCTION get_user_assigned_branches (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE,
        v_reg_code    IN TQC_REGIONS.REG_CODE%TYPE)
        RETURN  Branches_ref 
        IS vcursor Branches_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT BRN_CODE, BRN_SHT_DESC, BRN_REG_CODE, BRN_NAME
        FROM TQC_BRANCHES
        WHERE BRN_REG_CODE = v_reg_code
        AND EXISTS 
        (
            SELECT USB_CODE, USB_BRN_CODE, USB_USR_CODE, USB_STATUS, USB_DFLT_BRN
            FROM TQC_USER_BRANCHES
            WHERE TQC_BRANCHES.BRN_CODE = TQC_USER_BRANCHES.USB_BRN_CODE
            AND USB_USR_CODE = v_user_code
        );
             
     return vcursor;
END;

FUNCTION get_default_user_branch (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE)
        RETURN  default_branch_ref 
        IS vcursor default_branch_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT ORG_CODE, ORG_NAME, REG_CODE, REG_NAME, BRN_CODE, BRN_NAME, BRH_CUR_CODE, CUR_DESC
        FROM TQC_BRANCHES, TQC_REGIONS, TQC_ORGANIZATIONS, FMS_BRANCHES,TQC_CURRENCIES
        WHERE BRN_REG_CODE = REG_CODE
        AND REG_ORG_CODE = ORG_CODE
        AND BRN_CODE = BRH_CODE(+)
        AND BRH_CUR_CODE = CUR_CODE
        AND EXISTS 
        (
            SELECT USB_CODE, USB_BRN_CODE, USB_USR_CODE, USB_STATUS, USB_DFLT_BRN
            FROM TQC_USER_BRANCHES
            WHERE TQC_BRANCHES.BRN_CODE = TQC_USER_BRANCHES.USB_BRN_CODE
            AND USB_DFLT_BRN = 'Y'
            AND USB_USR_CODE = v_user_code
        );
             
     return vcursor;
END;
FUNCTION get_user_defaults (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE)
        RETURN  default_details_ref 
        IS vcursor default_details_ref;
BEGIN
     OPEN vcursor FOR
       SELECT ORG_CODE, ORG_NAME, REG_CODE, REG_NAME, BRN_CODE, BRN_NAME, ORG_COU_CODE,COU_NAME,COU_ADMIN_REG_TYPE
        FROM TQC_BRANCHES, TQC_REGIONS, TQC_ORGANIZATIONS,TQC_COUNTRIES
        WHERE BRN_REG_CODE = REG_CODE
        AND REG_ORG_CODE = ORG_CODE
        
        AND ORG_COU_CODE=COU_CODE
        AND EXISTS 
        (
            SELECT USB_CODE, USB_BRN_CODE, USB_USR_CODE, USB_STATUS, USB_DFLT_BRN
            FROM TQC_USER_BRANCHES
            WHERE TQC_BRANCHES.BRN_CODE = TQC_USER_BRANCHES.USB_BRN_CODE
            AND USB_DFLT_BRN = 'Y'
            AND USB_USR_CODE = v_user_code
        );
             
     return vcursor;
END;
FUNCTION get_nondefault_user_branch (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE)
        RETURN  default_branch_ref 
        IS vcursor default_branch_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT ORG_CODE, ORG_NAME, REG_CODE, REG_NAME, BRN_CODE, BRN_NAME, ORG_CUR_CODE, CUR_DESC
        FROM TQC_BRANCHES, TQC_REGIONS, TQC_ORGANIZATIONS, TQC_CURRENCIES
        WHERE BRN_REG_CODE = REG_CODE
        AND REG_ORG_CODE = ORG_CODE
        AND ORG_CUR_CODE = CUR_CODE
        AND BRN_CODE  in  
        (
            SELECT BRN_CODE
            FROM TQC_USER_BRANCHES,TQC_BRANCHES
            WHERE TQC_BRANCHES.BRN_CODE = TQC_USER_BRANCHES.USB_BRN_CODE
            AND USB_DFLT_BRN <> 'Y'
            AND USB_USR_CODE = v_user_code
        );
             
     return vcursor;
END;

FUNCTION get_user_unassigned_divisions (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE,
        v_branch_code    IN TQC_BRANCHES.BRN_CODE%TYPE)
        RETURN  user_unassigned_divisions_ref 
        IS vcursor user_unassigned_divisions_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT DIV_CODE, DIV_NAME, DIV_SHT_DESC, DIV_DIVISION_STATUS
        FROM TQC_DIVISIONS, TQC_BRNCH_DIVISIONS
        WHERE DIV_CODE = BDIV_DIV_CODE
        AND BDIV_BRN_CODE = v_branch_code
        AND NOT EXISTS
        (
            SELECT USD_CODE, USD_USR_CODE, USD_DIV_CODE, USD_DEFAULT
            FROM TQC_USER_DIVISIONS
            WHERE TQC_DIVISIONS.DIV_CODE = TQC_USER_DIVISIONS.USD_DIV_CODE
            AND USD_USR_CODE = v_user_code
        );
             
     return vcursor;
END;


FUNCTION get_user_assigned_divisions (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE,
        v_branch_code    IN TQC_BRANCHES.BRN_CODE%TYPE)
        RETURN  user_unassigned_divisions_ref 
        IS vcursor user_unassigned_divisions_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT DIV_CODE, DIV_NAME, DIV_SHT_DESC, DIV_DIVISION_STATUS
        FROM TQC_DIVISIONS, TQC_BRNCH_DIVISIONS
        WHERE DIV_CODE = BDIV_DIV_CODE
        AND BDIV_BRN_CODE = v_branch_code
        AND EXISTS
        (
            SELECT USD_CODE, USD_USR_CODE, USD_DIV_CODE, USD_DEFAULT
            FROM TQC_USER_DIVISIONS
            WHERE TQC_DIVISIONS.DIV_CODE = TQC_USER_DIVISIONS.USD_DIV_CODE
            AND USD_USR_CODE = v_user_code
        );
             
     return vcursor;
END;


FUNCTION get_default_user_division (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE)
        RETURN  default_division_ref 
        IS vcursor default_division_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT ORG_CODE, ORG_NAME, REG_CODE, REG_NAME, BRN_CODE, BRN_NAME, DIV_CODE, DIV_NAME
        FROM TQC_DIVISIONS, TQC_BRNCH_DIVISIONS, TQC_BRANCHES, TQC_REGIONS, TQC_ORGANIZATIONS
        WHERE DIV_CODE = BDIV_DIV_CODE
        AND BDIV_BRN_CODE = BRN_CODE
        AND BRN_REG_CODE = REG_CODE
        AND REG_ORG_CODE = ORG_CODE
        AND EXISTS
        (
            SELECT USD_CODE, USD_USR_CODE, USD_DIV_CODE, USD_DEFAULT
            FROM TQC_USER_DIVISIONS
            WHERE TQC_DIVISIONS.DIV_CODE = TQC_USER_DIVISIONS.USD_DIV_CODE
            AND USD_DEFAULT = 'Y'
            AND USD_USR_CODE = v_user_code
        );
             
     return vcursor;
END;

FUNCTION get_nondefault_user_division (
        v_user_code   IN TQC_USERS.USR_CODE%TYPE)
        RETURN  default_division_ref 
        IS vcursor default_division_ref;
BEGIN
     OPEN vcursor FOR
     
        SELECT ORG_CODE, ORG_NAME, REG_CODE, REG_NAME, BRN_CODE, BRN_NAME, DIV_CODE, DIV_NAME
        FROM TQC_DIVISIONS, TQC_BRNCH_DIVISIONS, TQC_BRANCHES, TQC_REGIONS, TQC_ORGANIZATIONS
        WHERE DIV_CODE = BDIV_DIV_CODE
        AND BDIV_BRN_CODE = BRN_CODE
        AND BRN_REG_CODE = REG_CODE
        AND REG_ORG_CODE = ORG_CODE
        AND EXISTS
        (
            SELECT USD_CODE, USD_USR_CODE, USD_DIV_CODE, USD_DEFAULT
            FROM TQC_USER_DIVISIONS
            WHERE TQC_DIVISIONS.DIV_CODE = TQC_USER_DIVISIONS.USD_DIV_CODE
            AND USD_DEFAULT <> 'Y'
            AND USD_USR_CODE = v_user_code
        );
             
     return vcursor;
END;


FUNCTION Get_system_Users(v_system IN TQC_USER_SYSTEMS.USYS_SYS_CODE%TYPE) RETURN  Users_ref IS
    vcursor Users_ref;
BEGIN
     OPEN vcursor FOR
     SELECT USR_CODE,USR_USERNAME,USR_NAME,USR_EMAIL,USR_PERSONEL_RANK,
     USR_DT_CREATED,USR_TYPE,USR_STATUS,USR_PWD_RESET, USR_PER_ID,USR_ACCT_MGR,USR_CELL_PHONE_NO,
     USR_WEF_DT, USR_WET_DT,NULL,NULL
     FROM TQC_USERS, TQC_USER_SYSTEMS
     WHERE USYS_SYS_CODE = v_system
     AND USR_CODE = USYS_USR_CODE
     AND (USYS_WET IS NULL OR USYS_WET >= SYSDATE);
     return vcursor;
END; 
FUNCTION Get_System_Processes (v_sysCode    IN TQC_SYS_PROCESSES.SPRC_SYS_CODE%TYPE) RETURN Sys_Processes_ref IS
    vcursor Sys_Processes_ref;
BEGIN
     OPEN vcursor FOR
     SELECT SPRC_CODE, SPRC_PROCESS
     FROM TQC_SYS_PROCESSES
     WHERE SPRC_SYS_CODE = v_sysCode;
     return vcursor;
END;

FUNCTION GetOrganizations RETURN Organizations_ref IS
    vcursor Organizations_ref;
BEGIN
    OPEN vcursor FOR
    SELECT ORG_CODE, ORG_SHT_DESC, ORG_NAME
    FROM TQC_ORGANIZATIONS;
    RETURN vcursor;
END;
FUNCTION Get_user_Orgs(v_usrCode  IN  TQC_USER_BRANCHES.USB_USR_CODE%TYPE) RETURN Organizations_ref IS
    vcursor Organizations_ref;
BEGIN
    OPEN vcursor FOR
    SELECT DISTINCT ORG_CODE, ORG_SHT_DESC, ORG_NAME
    FROM TQC_ORGANIZATIONS,TQC_SYSTEMS, TQC_USER_SYSTEMS
    WHERE ORG_CODE = SYS_ORG_CODE
    AND SYS_CODE = USYS_SYS_CODE
    AND USYS_USR_CODE = v_usrCode;
    
    RETURN vcursor;
END;

FUNCTION GetRegions(v_orgCode   IN  TQC_REGIONS.REG_ORG_CODE%TYPE)RETURN Regions_ref IS
    vcursor Regions_ref;
BEGIN
    OPEN vcursor FOR
    SELECT REG_CODE, REG_ORG_CODE, REG_NAME
    FROM TQC_REGIONS 
    WHERE REG_ORG_CODE =  v_orgCode;
    RETURN vcursor;
END;

FUNCTION GetBranchesbyOrg(v_orgCode   IN TQC_ORGANIZATIONS.ORG_CODE%TYPE) RETURN Branches_ref IS
    vcursor Branches_ref;
BEGIN
    OPEN vcursor FOR
    SELECT DISTINCT BRN_CODE, BRN_SHT_DESC, BRN_REG_CODE, BRN_NAME
    FROM TQC_BRANCHES, TQC_REGIONS
    WHERE REG_ORG_CODE = v_orgCode
    AND BRN_REG_CODE = REG_CODE;
    RETURN vcursor;
END;


FUNCTION GetBranches(v_regCode   IN TQC_BRANCHES.BRN_REG_CODE%TYPE) RETURN Branches_ref IS
    vcursor Branches_ref;
BEGIN
    OPEN vcursor FOR
    SELECT DISTINCT BRN_CODE, BRN_SHT_DESC, BRN_REG_CODE, BRN_NAME
    FROM TQC_BRANCHES
    WHERE BRN_REG_CODE = v_regCode;
    RETURN vcursor;
END;

FUNCTION GetUserBranches(v_usrCode      IN  TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
                         v_OrgCode      IN TQC_ORGANIZATIONS.ORG_CODE%TYPE) RETURN UsrBranches_ref IS
    vcursor UsrBranches_ref;
BEGIN
    OPEN vcursor FOR
    SELECT USB_CODE, USB_BRN_CODE, USB_USR_CODE, USB_STATUS, USB_DFLT_BRN,BRN_NAME
    FROM TQC_USER_BRANCHES,TQC_BRANCHES, TQC_REGIONS
    WHERE USB_BRN_CODE = BRN_CODE
    AND REG_ORG_CODE = v_OrgCode
    AND USB_USR_CODE = v_usrCode
    AND BRN_REG_CODE = REG_CODE;
    RETURN vcursor;
END;


FUNCTION get_user_branches(v_usrCode      IN  TQC_USER_BRANCHES.USB_USR_CODE%TYPE) RETURN UsrBranches_ref IS
    vcursor UsrBranches_ref;
BEGIN
    OPEN vcursor FOR
    SELECT USB_CODE, USB_BRN_CODE, USB_USR_CODE, USB_STATUS, USB_DFLT_BRN, BRN_NAME
    FROM TQC_USER_BRANCHES,TQC_BRANCHES
    WHERE USB_BRN_CODE = BRN_CODE
    AND USB_USR_CODE = v_usrCode;
    --AND USB_BRN_CODE = v_brnCode;    
    RETURN vcursor;
END;
    

PROCEDURE Create_User (v_username       IN  TQC_USERS.USR_USERNAME%TYPE,
                       v_names          IN  TQC_USERS.USR_NAME%TYPE,
                       v_password       IN  TQC_USERS.USR_PWD%TYPE,
                       v_email          IN  TQC_USERS.USR_EMAIL%TYPE,
                       v_personel       IN  TQC_USERS.USR_PER_RANK_SHT_DESC%TYPE,
                       v_status         IN  TQC_USERS.USR_STATUS%TYPE,
                       v_type           IN  TQC_USERS.USR_TYPE%TYPE,
                       v_reset          IN  TQC_USERS.USR_PWD_RESET%TYPE,
                       v_per_id         IN  TQC_USERS.USR_PER_ID%TYPE,
                       v_user_code      OUT TQC_USERS.USR_CODE%TYPE,
                       v_usr_acct_mgr     IN   TQC_USERS.USR_ACCT_MGR%TYPE DEFAULT NULL,
                       v_usr_signature    IN   TQC_USERS.USR_SIGNATURE%TYPE DEFAULT NULL,
                       v_usr_cell_phone_no    IN   TQC_USERS.USR_CELL_PHONE_NO%TYPE DEFAULT NULL,
                       V_USR_WEF_DT  IN   TQC_USERS.USR_WEF_DT%TYPE DEFAULT NULL, 
                       V_USR_WET_DT  IN   TQC_USERS.USR_WET_DT%TYPE DEFAULT NULL,
                       V_USR_SECQ_CODE IN   TQC_USERS.USR_SECQ_CODE%TYPE DEFAULT NULL, 
                       V_USR_SECURITY_ANSWER IN   TQC_USERS.USR_SECURITY_ANSWER%TYPE DEFAULT NULL
                       ) IS
                           
BEGIN

     SELECT TQC_USR_CODE_SEQ.NEXTVAL INTO v_user_code FROM DUAL;
           INSERT INTO TQC_USERS (
     USR_CODE, USR_USERNAME, USR_NAME, USR_PWD, USR_EMAIL, USR_PERSONEL_RANK,
     USR_PER_ID, USR_STATUS, USR_TYPE, USR_DT_CREATED, USR_PWD_CHANGED, 
     USR_PWD_RESET,USR_ACCT_MGR,USR_SIGNATURE,USR_CELL_PHONE_NO,USR_WEF_DT, USR_WET_DT,
     USR_SECQ_CODE,USR_SECURITY_ANSWER) 
     VALUES (v_user_code, v_username, v_names, GIS_READ.VAL(v_password), v_email,
     v_personel, v_per_id, v_status, v_type, SYSDATE, SYSDATE, v_reset,v_usr_acct_mgr,v_usr_signature,V_USR_CELL_PHONE_NO,
     v_usr_wef_dt, v_usr_wet_dt,V_USR_SECQ_CODE,V_USR_SECURITY_ANSWER);
                        
END Create_User;    

PROCEDURE UpDate_User (v_user           IN  TQC_USERS.USR_CODE%TYPE,
                       v_username       IN  TQC_USERS.USR_USERNAME%TYPE,
                       v_names          IN  TQC_USERS.USR_NAME%TYPE,
                       v_email          IN  TQC_USERS.USR_EMAIL%TYPE,
                       v_personel       IN  TQC_USERS.USR_PER_RANK_SHT_DESC%TYPE,
                       v_type           IN  TQC_USERS.USR_TYPE%TYPE,
                       v_reset          IN  TQC_USERS.USR_PWD_RESET%TYPE,
                       v_per_id         IN  TQC_USERS.USR_PER_ID%TYPE,
                        v_status         IN  TQC_USERS.USR_STATUS%TYPE,
                        v_usr_acct_mgr    IN TQC_USERS.USR_ACCT_MGR%TYPE,
                        v_usr_cell_phone_no    IN TQC_USERS.USR_CELL_PHONE_NO%TYPE,
                        v_USR_WEF_DT    IN TQC_USERS.USR_WEF_DT%TYPE,
                        v_USR_WET_DT    IN TQC_USERS.USR_WET_DT%TYPE,
                        v_USR_SECQ_CODE    IN TQC_USERS.USR_SECQ_CODE%TYPE,
                        v_USR_SECURITY_ANSWER    IN TQC_USERS.USR_SECURITY_ANSWER%TYPE) IS
BEGIN    
--     RAISE_ERROR(v_USR_SECQ_CODE);
      UPDATE TQC_USERS 
      SET 
        USR_USERNAME = v_username,
        USR_NAME = v_names,
        USR_EMAIL = NVL(v_email,USR_EMAIL),
        USR_PERSONEL_RANK = NVL(v_personel,USR_PERSONEL_RANK),
        USR_PER_ID = NVL(v_per_id,USR_PER_ID),
        USR_TYPE = NVL(v_type,USR_TYPE),
        USR_PWD_RESET = NVL(v_reset,USR_PWD_RESET),
        USR_STATUS = NVL(v_status,USR_STATUS),
        USR_LOGIN_ATTEMPTS=0,
        USR_ACCT_MGR=NVL(v_usr_acct_mgr,USR_ACCT_MGR),
        USR_CELL_PHONE_NO=v_USR_CELL_PHONE_NO,
        USR_WEF_DT=v_USR_WEF_DT, 
        USR_WET_DT=v_USR_WET_DT,
        USR_SECQ_CODE=V_USR_SECQ_CODE, 
        USR_SECURITY_ANSWER=V_USR_SECURITY_ANSWER
      WHERE USR_CODE = v_user;
     
     COMMIT;
END UpDate_User;
PROCEDURE Drop_User (v_user           IN  TQC_USERS.USR_CODE%TYPE) IS
v_username TQC_USERS.USR_USERNAME%TYPE;
BEGIN
      SELECT USR_USERNAME INTO v_username FROM   TQC_USERS WHERE USR_CODE=v_user; 
      IF(UPPER(v_username)='ADMIN') THEN
      RAISE_ERROR('ERROR: YOU CANNOT DROP ADMINISTRATOR ACCCOUNT');
      ELSE
      UPDATE TQC_USERS SET USR_STATUS = 'I'
      WHERE USR_CODE = v_user;
      END IF;
END Drop_User;
                           
PROCEDURE Restore_User (v_user           IN  TQC_USERS.USR_CODE%TYPE) IS
BEGIN
      UPDATE TQC_USERS SET USR_STATUS = 'A'
      WHERE USR_CODE = v_user;
END Restore_User;

                           
PROCEDURE Grant_User_System (v_user     IN  TQC_USERS.USR_CODE%TYPE,
                             v_sys_code IN  TQC_SYSTEMS.SYS_CODE%TYPE) IS
   v_Count NUMBER;
   v_usr_status VARCHAR2(200);
BEGIN
    BEGIN
    SELECT USR_STATUS
    INTO v_usr_status
    FROM TQC_USERS
    WHERE USR_CODE=v_user;
    END;
    
    IF NVL(v_usr_status,'I')!='A'
    THEN
    RAISE_ERROR('You cannot assign a system to an inactive user');
    END IF;
    
    SELECT COUNT(*) INTO v_Count
    FROM TQC_USER_SYSTEMS
    WHERE USYS_SYS_CODE = v_sys_code
    AND USYS_USR_CODE = v_user
    AND (USYS_WET IS NULL OR USYS_WET > SYSDATE);
    IF(NVL(v_Count,0)=0)THEN
        INSERT INTO TQC_USER_SYSTEMS (USYS_CODE, USYS_USR_CODE, USYS_SYS_CODE, USYS_WEF,USYS_WET)
        VALUES (TQC_USYS_CODE_SEQ.NEXTVAL,v_user,v_sys_code,SYSDATE,NULL);
    END IF;        
END Grant_User_System; 

                              
PROCEDURE Revoke_User_System (v_user     IN  TQC_USERS.USR_CODE%TYPE,
                              v_sys_code IN  TQC_SYSTEMS.SYS_CODE%TYPE) IS
BEGIN
    DELETE FROM TQC_USER_SYSTEMS 
    WHERE USYS_USR_CODE = v_user
    AND USYS_SYS_CODE = v_sys_code
    AND USYS_WET IS NULL;
END Revoke_User_System;                               
   
PROCEDURE Revoke_User_Role (v_userCode       IN  TQC_USERS.USR_CODE%TYPE,
                            v_user           IN  TQC_USERS.USR_CODE%TYPE,
                            v_role           IN  TQC_SYS_ROLES.SRLS_CODE%TYPE) IS
BEGIN

    --RAISE_ERROR(v_userCode ||';'|| v_role);
    DELETE FROM TQC_SYS_USER_ROLES
    WHERE USRR_USR_CODE = v_user
    AND USRR_SRLS_CODE = v_role;
    
    /*UPDATE  TQC_SYS_USER_ROLES SET USRR_REVOKE_DATE = SYSDATE,USRR_REVOKE_BY_USR_CODE = v_userCode 
    WHERE USRR_USR_CODE = v_user
    AND USRR_SRLS_CODE = v_role;*/
END Revoke_User_Role;
PROCEDURE Grant_User_Role (v_sys_code       IN TQC_SYSTEMS.SYS_CODE%TYPE,
                           v_username       IN  TQC_USERS.USR_USERNAME%TYPE,
                           v_srls_sht_desc           IN  TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
                           v_grant          IN VARCHAR2) IS
      v_srls_code NUMBER;
      v_usr_code NUMBER;
      CURSOR SRLS IS SELECT SRLS_CODE  FROM TQC_SYS_ROLES  
      WHERE SRLS_SHT_DESC = v_srls_sht_desc
      AND SRLS_SYS_CODE = DECODE(NVL(v_sys_code,-2000),-2000,SRLS_SYS_CODE,v_sys_code);
BEGIN
    BEGIN
        SELECT USR_CODE
        INTO v_usr_code
        FROM TQC_USERS
        WHERE USR_USERNAME = v_username;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_usr_code := NULL;
        WHEN OTHERS THEN 
            RAISE_ERROR('User not defined..');
    END;
    IF v_usr_code IS NOT NULL THEN 
        FOR R IN SRLS LOOP
            IF NVL(v_grant,'Y') = 'Y' THEN 
                Grant_User_Role (NULL,v_usr_code,R.srls_code) ;
            ELSE
                Revoke_User_Role (NULL,v_usr_code,R.srls_code);
            END IF;
        END LOOP;
    END IF;
END;

PROCEDURE Grant_User_Role (v_userCode       IN  TQC_USERS.USR_CODE%TYPE,
                           v_user           IN  TQC_USERS.USR_CODE%TYPE,
                           v_role           IN  TQC_SYS_ROLES.SRLS_CODE%TYPE) IS
      v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM TQC_SYS_USER_ROLES
    WHERE USRR_USR_CODE = v_user
    AND USRR_SRLS_CODE = v_role;
    
    IF(NVL(v_count,0)=0) THEN
    INSERT INTO TQC_SYS_USER_ROLES (USRR_USR_CODE, USRR_SRLS_CODE, USRR_GRANT_DATE, 
                USRR_REVOKE_DATE, USRR_GRANT_BY_USR_CODE, USRR_REVOKE_BY_USR_CODE)
    VALUES (v_user,v_role,SYSDATE,
            NULL,v_userCode,NULL);
    ELSE
    UPDATE  TQC_SYS_USER_ROLES SET USRR_REVOKE_DATE = NULL,USRR_REVOKE_BY_USR_CODE = NULL 
    WHERE USRR_USR_CODE = v_user
    AND USRR_SRLS_CODE = v_role;
    END IF;
END Grant_User_Role;


PROCEDURE Grant_Role_Process_Area (v_processArea       IN  TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
                                   v_roleCode          IN  TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE)IS
                                   
     CURSOR cur_prcss IS SELECT * FROM TQC_SYS_PROCESS_AREAS WHERE SPRCA_CODE = v_processArea;
     
     CURSOR subArea_cur IS  SELECT SPRSA_CODE FROM TQC_SYS_PROCESS_AREAS,TQC_SYS_PROCESS_SUB_AREAS
     WHERE SPRSA_SPRCA_CODE = SPRCA_CODE
     AND SPRCA_CODE = v_processArea;
     
     v_srprc_code      TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE;
     v_srprc_sprc_code TQC_SYS_ROLES_PROCESSES.SRPRC_SPRC_CODE%TYPE;
     v_roleAreaCode TQC_SYS_ROLES_PRCS_AREA.SRPRA_CODE%TYPE;
     v_count        NUMBER; 
     v_process_granted BOOLEAN:= TRUE;
BEGIN

--RAISE_ERROR(v_processArea||'='||v_roleCode);
    FOR cur_prcss_rec IN cur_prcss LOOP
        BEGIN
            SELECT SRPRC_CODE,SRPRC_SPRC_CODE
            INTO v_srprc_code,v_srprc_sprc_code
            FROM TQC_SYS_ROLES_PROCESSES
            WHERE  SRPRC_SRLS_CODE = v_roleCode
            AND    SRPRC_SPRC_CODE = cur_prcss_rec.SPRCA_SPRC_CODE;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN 
                v_process_granted := FALSE;
            WHEN OTHERS THEN 
                RAISE_ERROR('Error determining is process has been granted..');
        END;
        IF NOT v_process_granted THEN 
            BEGIN
                grantProcessRole(cur_prcss_rec.SPRCA_SPRC_CODE,
                               v_roleCode,
                               'Y');
                               
                SELECT SRPRC_CODE,SRPRC_SPRC_CODE
                INTO v_srprc_code,v_srprc_sprc_code
                FROM TQC_SYS_ROLES_PROCESSES
                WHERE  SRPRC_SRLS_CODE = v_roleCode
                AND    SRPRC_SPRC_CODE = cur_prcss_rec.SPRCA_SPRC_CODE;
            EXCEPTION
                WHEN OTHERS THEN 
                    RAISE_ERROR('Error granting process... cur_prcss_rec is '||cur_prcss_rec.SPRCA_SPRC_CODE||' : RoleCode is '||v_roleCode);
            END;
        END IF;
        BEGIN
            SELECT COUNT(*) 
            INTO v_count 
            FROM TQC_SYS_ROLES_PROCESSES,TQC_SYS_ROLES_PRCS_AREA
            WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
            AND SRPRA_SPRCA_CODE = cur_prcss_rec.SPRCA_CODE
            AND SRPRC_SRLS_CODE = v_roleCode;
        EXCEPTION
            WHEN OTHERS THEN 
                RAISE_ERROR('Error determining if process areas has already been granted');
        END;
    
        IF (NVL(v_count,0)=0) THEN
        DBMS_OUTPUT.PUT_LINE('INSERT');
            SELECT TQC_SRPRA_SEQ.NEXTVAL INTO v_roleAreaCode FROM DUAL;
            INSERT INTO TQC_SYS_ROLES_PRCS_AREA (SRPRA_CODE, SRPRA_SRPRC_CODE, SRPRA_SPRCA_CODE)
            VALUES(v_roleAreaCode,v_srprc_code,v_processArea);
        END IF;
    
        FOR subArea_rec IN subArea_cur LOOP
        
        DBMS_OUTPUT.PUT_LINE(subArea_rec.SPRSA_CODE||'='||v_roleCode);
            Grant_Role_Process_SubArea (subArea_rec.SPRSA_CODE,
                                        v_roleCode,
                                        null,
                                        null);
           -- INSERT INTO TQC_SYS_ROLES_PRCS_S_AREA (SRPSA_CODE, SRPSA_SRPRA_CODE, SRPSA_SPRSA_CODE, SRPSA_DEBIT_LIMIT, SRPSA_CREDIT_LIMIT)
            --VALUES(TQC_SRPSA_SEQ.NEXTVAL,v_roleAreaCode,subArea_rec.SPRSA_CODE,NULL,NULL);
        END LOOP;
    END LOOP;
END Grant_Role_Process_Area;

PROCEDURE Revoke_Role_Process_Area (v_processArea       IN  TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE,
                                    v_role_code       IN  TQC_SYS_ROLES.SRLS_CODE%TYPE)IS
                                   
    
BEGIN
   --RAISE_ERROR(v_role_code ||';'||v_processArea);
    DELETE FROM TQC_SYS_ROLES_PRCS_S_AREA 
    WHERE SRPSA_SRPRA_CODE = (SELECT SRPRA_CODE 
                             FROM TQC_SYS_ROLES_PROCESSES,TQC_SYS_ROLES_PRCS_AREA
                             WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
                             AND SRPRC_SRLS_CODE = v_role_code 
                             AND SRPRA_SPRCA_CODE = v_processArea);
    
    DELETE FROM TQC_SYS_ROLES_PRCS_AREA 
    WHERE SRPRA_CODE = (SELECT SRPRA_CODE 
                         FROM TQC_SYS_ROLES_PROCESSES,TQC_SYS_ROLES_PRCS_AREA
                         WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
                         AND SRPRC_SRLS_CODE = v_role_code 
                         AND SRPRA_SPRCA_CODE = v_processArea);
                         
END Revoke_Role_Process_Area;

PROCEDURE Grant_Role_Process_SubArea (v_processsubArea       IN  TQC_SYS_PROCESS_SUB_AREAS.SPRSA_CODE%TYPE,
                                      v_roleCode             IN  TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE,
                                      v_debitLimit           IN  TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_DEBIT_LIMIT%TYPE,
                                      v_creditLimit          IN  TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CREDIT_LIMIT%TYPE) IS
    
    v_sprca_code   TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE;
    v_srpra_code  TQC_SYS_ROLES_PRCS_AREA.SRPRA_CODE%TYPE;
    v_sprsa_code  TQC_SYS_PROCESS_SUB_AREAS.SPRSA_CODE%TYPE;
    v_count         NUMBER;
    v_process_granted BOOLEAN:= TRUE;
    v_prcss_area_granted BOOLEAN:= TRUE; 
    v_cnt NUMBER:=0;                                 
BEGIN
  -- RAISE_ERROR(v_processsubArea ||';'||v_roleCode);
    BEGIN
    --RAISE_ERROR(v_processsubArea ||';'||v_roleCode);
        SELECT SPRCA_CODE,SPRSA_CODE
        INTO v_sprca_code,v_sprsa_code
        FROM TQC_SYS_PROCESSES,TQC_SYS_ROLES_PROCESSES,TQC_SYS_PROCESS_AREAS,TQC_SYS_PROCESS_SUB_AREAS
        WHERE SPRC_CODE = SPRCA_SPRC_CODE
        AND SPRC_CODE   = SRPRC_SPRC_CODE
        AND SPRCA_CODE  = SPRSA_SPRCA_CODE
        AND SPRSA_CODE  = v_processsubArea
        AND SRPRC_SRLS_CODE = v_roleCode;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_process_granted := FALSE;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error determining if process has been granted..');
    END;
    IF NOT v_process_granted THEN 
    DBMS_OUTPUT.PUT_LINE('GRANTING');
        BEGIN
            SELECT SPRCA_CODE,SPRSA_CODE
            INTO v_sprca_code,v_sprsa_code
            FROM TQC_SYS_PROCESSES,TQC_SYS_PROCESS_AREAS,TQC_SYS_PROCESS_SUB_AREAS
            WHERE SPRC_CODE = SPRCA_SPRC_CODE
            AND SPRCA_CODE  = SPRSA_SPRCA_CODE
            AND SPRSA_CODE  = v_processsubArea;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN 
                RAISE_ERROR('33');
            WHEN OTHERS THEN 
                RAISE_ERROR('Error determining is process has been granted..');
        END;
        DBMS_OUTPUT.PUT_LINE('v_sprca_code='||v_sprca_code);
        BEGIN
            Grant_Role_Process_Area (v_sprca_code,
                                     v_roleCode);
        EXCEPTION
            WHEN OTHERS THEN 
                RAISE_ERROR('Error granting process RoleCode is ' ||v_roleCode||' :: sprcaCode is '||v_sprca_code);
        END;
    END IF;
    
    BEGIN
    --RAISE_ERROR(v_roleCode ||';'||v_sprca_code);
        SELECT SRPRA_CODE
        INTO v_srpra_code
        FROM TQC_SYS_ROLES_PROCESSES,TQC_SYS_ROLES_PRCS_AREA
        WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
        AND SRPRC_SRLS_CODE = v_roleCode
        AND SRPRA_SPRCA_CODE = v_sprca_code;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_prcss_area_granted := FALSE;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error getting process and process area details..'||v_roleCode||' = '||v_sprca_code);
    END;
    IF NOT v_prcss_area_granted THEN 
        BEGIN
            Grant_Role_Process_Area (v_sprca_code,
                                     v_roleCode);
                                     
            SELECT SRPRA_CODE
            INTO v_srpra_code
            FROM TQC_SYS_ROLES_PROCESSES,TQC_SYS_ROLES_PRCS_AREA
            WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
            AND SRPRC_SRLS_CODE = v_roleCode
            AND SRPRA_SPRCA_CODE = v_sprca_code;
        EXCEPTION
            WHEN OTHERS THEN 
                RAISE_ERROR('Error granting process Area');
        END;
    END IF;
  -- raise_error('v_srpra_code='||v_srpra_code);
    BEGIN
        SELECT COUNT(1)
        INTO v_cnt
        FROM TQC_SYS_ROLES_PRCS_S_AREA
        WHERE SRPSA_SRPRA_CODE =v_srpra_code 
        AND SRPSA_SPRSA_CODE = v_sprsa_code;
    EXCEPTION
        WHEN OTHERS THEN 
            RAISE_ERROR('Error getting if role already granted..');
    END;
    IF NVL(v_cnt,0) = 0 THEN 
        BEGIN
            INSERT INTO TQC_SYS_ROLES_PRCS_S_AREA (SRPSA_CODE, SRPSA_SRPRA_CODE, SRPSA_SPRSA_CODE, SRPSA_DEBIT_LIMIT, SRPSA_CREDIT_LIMIT)
            VALUES(TQC_SRPSA_SEQ.NEXTVAL,v_srpra_code,v_sprsa_code,v_debitLimit,v_creditLimit);
        EXCEPTION
            WHEN OTHERS THEN 
                RAISE_ERROR('Error inserting sub area..');
        END;
    ELSE
        BEGIN
            UPDATE TQC_SYS_ROLES_PRCS_S_AREA SET 
            SRPSA_DEBIT_LIMIT = v_debitLimit, 
            SRPSA_CREDIT_LIMIT= v_creditLimit
            WHERE SRPSA_SRPRA_CODE =v_srpra_code 
            AND SRPSA_SPRSA_CODE = v_sprsa_code;
        EXCEPTION
            WHEN OTHERS THEN 
                RAISE_ERROR('Error updating role..');
        END;
    END IF;
END Grant_Role_Process_SubArea;

/*PROCEDURE Grant_Role_Process_SubArea (v_processsubArea       IN  TQC_SYS_PROCESS_SUB_AREAS.SPRSA_CODE%TYPE,
                                      v_roleCode             IN  TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE,
                                      v_debitLimit           IN  TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_DEBIT_LIMIT%TYPE,
                                      v_creditLimit          IN  TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CREDIT_LIMIT%TYPE) IS
    
    v_sprca_code   TQC_SYS_PROCESS_AREAS.SPRCA_CODE%TYPE;
    v_srpra_code  TQC_SYS_ROLES_PRCS_AREA.SRPRA_CODE%TYPE;
    v_sprsa_code  TQC_SYS_PROCESS_SUB_AREAS.SPRSA_CODE%TYPE;
    v_count         NUMBER;
    v_process_granted BOOLEAN:= TRUE;
    v_prcss_area_granted BOOLEAN:= TRUE;                                  
BEGIN
    BEGIN
    --RAISE_ERROR(v_processsubArea ||';'||v_roleCode);
        SELECT SPRCA_CODE,SPRSA_CODE
        INTO v_sprca_code,v_sprsa_code
        FROM TQC_SYS_PROCESSES,TQC_SYS_ROLES_PROCESSES,TQC_SYS_PROCESS_AREAS,TQC_SYS_PROCESS_SUB_AREAS
        WHERE SPRC_CODE = SPRCA_SPRC_CODE
        AND SPRC_CODE   = SRPRC_SPRC_CODE
        AND SPRCA_CODE  = SPRSA_SPRCA_CODE
        AND SPRSA_CODE  = v_processsubArea
        AND SRPRC_SRLS_CODE = v_roleCode;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_process_granted := FALSE;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error determining if process has been granted..');
    END;
    IF NOT v_process_granted THEN 
    DBMS_OUTPUT.PUT_LINE('GRANTING');
        BEGIN
            SELECT SPRCA_CODE,SPRSA_CODE
            INTO v_sprca_code,v_sprsa_code
            FROM TQC_SYS_PROCESSES,TQC_SYS_PROCESS_AREAS,TQC_SYS_PROCESS_SUB_AREAS
            WHERE SPRC_CODE = SPRCA_SPRC_CODE
            AND SPRCA_CODE  = SPRSA_SPRCA_CODE
            AND SPRSA_CODE  = v_processsubArea;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN 
                RAISE_ERROR('33');
            WHEN OTHERS THEN 
                RAISE_ERROR('Error determining is process has been granted..');
        END;
        DBMS_OUTPUT.PUT_LINE('v_sprca_code='||v_sprca_code);
        BEGIN
            Grant_Role_Process_Area (v_sprca_code,
                                     v_roleCode);
        EXCEPTION
            WHEN OTHERS THEN 
                RAISE_ERROR('Error granting process ');
        END;
    END IF;
    
    BEGIN
    --RAISE_ERROR(v_roleCode ||';'||v_sprca_code);
        SELECT SRPRA_CODE
        INTO v_srpra_code
        FROM TQC_SYS_ROLES_PROCESSES,TQC_SYS_ROLES_PRCS_AREA
        WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
        AND SRPRC_SRLS_CODE = v_roleCode
        AND SRPRA_SPRCA_CODE = v_sprca_code;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_prcss_area_granted := FALSE;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error getting process and process area details..'||v_roleCode||' = '||v_sprca_code);
    END;
    /*
    IF NOT v_prcss_area_granted THEN 
        BEGIN
            Grant_Role_Process_Area (v_sprca_code,
                                     v_roleCode);
                                     
            --SELECT SRPRA_CODE
           -- INTO v_srpra_code
            --FROM TQC_SYS_ROLES_PROCESSES,TQC_SYS_ROLES_PRCS_AREA
           -- WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
            --AND SRPRC_SRLS_CODE = v_roleCode
            --AND SRPRA_SPRCA_CODE = v_sprca_code;
        EXCEPTION
            WHEN OTHERS THEN 
                RAISE_ERROR('Error granting process Area');
        END;
    END IF;*//*
  -- raise_error('v_srpra_code='||v_srpra_code);
    BEGIN
        INSERT INTO TQC_SYS_ROLES_PRCS_S_AREA (SRPSA_CODE, SRPSA_SRPRA_CODE, SRPSA_SPRSA_CODE, SRPSA_DEBIT_LIMIT, SRPSA_CREDIT_LIMIT)
        VALUES(TQC_SRPSA_SEQ.NEXTVAL,v_srpra_code,v_sprsa_code,v_debitLimit,v_creditLimit);
    EXCEPTION
        WHEN OTHERS THEN 
            RAISE_ERROR('Error inserting sub area..');
    END;
END Grant_Role_Process_SubArea;
*/
PROCEDURE Revoke_Role_Process_SubArea (v_processsubArea       IN  TQC_SYS_PROCESS_SUB_AREAS.SPRSA_CODE%TYPE,
                                       v_role_code            IN TQC_SYS_ROLES.SRLS_CODE%TYPE) IS

BEGIN
--RAISE_ERROR(v_processsubArea ||';'||v_role_code);

    DELETE  TQC_SYS_ROLES_PRCS_S_AREA A
    WHERE A.SRPSA_CODE = (SELECT B.SRPSA_CODE
                        FROM TQC_SYS_ROLES_PROCESSES, TQC_SYS_ROLES_PRCS_AREA,TQC_SYS_ROLES_PRCS_S_AREA B
                        WHERE SRPRC_CODE = SRPRA_SRPRC_CODE
                        AND SRPRA_CODE = B.SRPSA_SRPRA_CODE
                        AND B.SRPSA_SPRSA_CODE = v_processsubArea
                        AND SRPRC_SRLS_CODE = v_role_code);    
END Revoke_Role_Process_SubArea;

PROCEDURE Creat_Role (v_sysCode       IN    TQC_SYS_ROLES.SRLS_SYS_CODE%TYPE,
                      v_role_name     IN    TQC_SYS_ROLES.SRLS_NAME%TYPE,
                      v_role_sht_Desc IN    TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
                      v_Process       IN    TQC_SYS_ROLES_PROCESSES.SRPRC_SPRC_CODE%TYPE DEFAULT NULL) IS
                      
    v_roleCode   TQC_SYS_ROLES.SRLS_CODE%TYPE;
BEGIN
    SELECT TQC_SRLS_SEQ.NEXTVAL INTO v_roleCode FROM DUAL;
    
    INSERT INTO TQC_SYS_ROLES (SRLS_CODE, SRLS_SYS_CODE, SRLS_NAME, SRLS_CRT_DATE, SRLS_SHT_DESC, SRLS_STATUS)
    VALUES (v_roleCode,v_sysCode,v_role_name,SYSDATE,v_role_sht_Desc,'A');
    
    --Commented out by Gitau. 040110 to allow multiple processes on a role.
    --INSERT INTO TQC_SYS_ROLES_PROCESSES (SRPRC_CODE, SRPRC_SRLS_CODE, SRPRC_SPRC_CODE)
    --VALUES(TQC_SRPRC_SEQ.NEXTVAL,v_roleCode,v_Process);
    
END Creat_Role;
                          
PROCEDURE   Edit_Role (v_roleProcess       IN  TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE,
                       v_role_name     IN    TQC_SYS_ROLES.SRLS_NAME%TYPE,
                       v_role_sht_Desc IN    TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
                       v_status        IN    TQC_SYS_ROLES.SRLS_STATUS%TYPE                                              
                       ) IS
                       
     v_roleCode   TQC_SYS_ROLES.SRLS_CODE%TYPE;
BEGIN
    /*SELECT SRPRC_SRLS_CODE INTO v_roleCode 
    FROM TQC_SYS_ROLES_PROCESSES
    WHERE SRPRC_CODE = v_roleProcess;*/
    
    -- Assume the v_roleProcess is the same as v_roleCode 
    
    UPDATE TQC_SYS_ROLES SET SRLS_NAME = v_role_name, SRLS_SHT_DESC = v_role_sht_Desc,SRLS_STATUS=v_status
    WHERE SRLS_CODE = v_roleProcess;
END Edit_Role;

PROCEDURE Restore_Role (v_roleProcess       IN  TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE) IS

    v_roleCode      TQC_SYS_ROLES.SRLS_CODE%TYPE;
BEGIN
    SELECT SRPRC_SRLS_CODE INTO v_roleCode FROM TQC_SYS_ROLES_PROCESSES
    WHERE SRPRC_CODE = v_roleProcess;
    
    UPDATE TQC_SYS_ROLES SET SRLS_STATUS = 'A'
    WHERE SRLS_CODE = v_roleCode;
END Restore_Role;

PROCEDURE Drop_Role (v_roleProcess       IN  TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE) IS

    v_roleCode      TQC_SYS_ROLES.SRLS_CODE%TYPE;
BEGIN
    /*SELECT SRPRC_SRLS_CODE INTO v_roleCode FROM TQC_SYS_ROLES_PROCESSES
    WHERE SRPRC_CODE = v_roleProcess;*/
    
    -- Assume the v_roleProcess is the same as v_roleCode 
    
    UPDATE TQC_SYS_ROLES SET SRLS_STATUS = 'I'
    WHERE SRLS_CODE = v_roleProcess;
END Drop_Role;

PROCEDURE Edit_User_SysWet (v_sysCode       IN  TQC_USER_SYSTEMS.USYS_CODE%TYPE,
                            v_wet           IN  TQC_USER_SYSTEMS.USYS_WET%TYPE) IS
                            
BEGIN
    UPDATE TQC_USER_SYSTEMS SET USYS_WET = v_wet
    WHERE USYS_CODE = v_sysCode;
END Edit_User_SysWet;
     
                            
PROCEDURE Grant_User_Branch (v_user         IN  TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
                             v_branch_code  IN  TQC_USER_BRANCHES.USB_BRN_CODE%TYPE) IS
    v_Count         NUMBER;
    v_usb_code      NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_Count
    FROM TQC_USER_BRANCHES
    WHERE USB_BRN_CODE = v_branch_code
    AND USB_USR_CODE = v_user;
    IF(NVL(v_Count,0)=0)THEN
    select nvl( max(usb_code),0) + 1 into     v_usb_code from tqc_user_branches;
        INSERT INTO TQC_USER_BRANCHES (USB_CODE, USB_BRN_CODE, USB_USR_CODE, USB_STATUS, USB_DFLT_BRN)
        VALUES (v_usb_code,v_branch_code,v_user,'A','N');
    END IF;
END Grant_User_Branch;
                                 
PROCEDURE Revoke_User_Branch (v_user            IN  TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
                              v_branch_code      IN  TQC_USER_BRANCHES.USB_BRN_CODE%TYPE) IS
BEGIN
    DELETE FROM TQC_USER_BRANCHES 
    WHERE USB_BRN_CODE = v_branch_code
    AND USB_USR_CODE = v_user;
END Revoke_User_Branch;

PROCEDURE enableDisableBranch(v_usrBranchCode       IN TQC_USER_BRANCHES.USB_CODE%TYPE) IS
    v_status VARCHAR2(1);
BEGIN
    SELECT USB_STATUS INTO v_status
    FROM TQC_USER_BRANCHES
    WHERE USB_CODE = v_usrBranchCode;
    IF NVL(v_status,'A') = 'A' THEN
        UPDATE TQC_USER_BRANCHES SET USB_STATUS = 'I'
        WHERE USB_CODE = v_usrBranchCode;
    ELSE
        UPDATE TQC_USER_BRANCHES SET USB_STATUS = 'A'
        WHERE USB_CODE = v_usrBranchCode;
    END IF;
END enableDisableBranch;
    
PROCEDURE makeDefaultBranch(v_usrBranchCode       IN TQC_USER_BRANCHES.USB_CODE%TYPE) IS
    v_default   NUMBER;
    v_regCode   NUMBER;
    v_userCode  NUMBER;
BEGIN
    SELECT USB_USR_CODE, BRN_REG_CODE INTO v_userCode,v_regCode
    FROM TQC_USER_BRANCHES,TQC_BRANCHES
    WHERE USB_BRN_CODE = BRN_CODE
    AND USB_CODE = v_usrBranchCode;
    
    BEGIN
        SELECT USB_CODE INTO v_default
        FROM TQC_USER_BRANCHES,TQC_BRANCHES
        WHERE USB_BRN_CODE = BRN_CODE
        AND BRN_REG_CODE = v_regCode
        AND USB_USR_CODE = v_userCode
        AND USB_DFLT_BRN = 'Y';
    
        UPDATE TQC_USER_BRANCHES SET USB_DFLT_BRN ='N' WHERE USB_CODE = v_default;
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
        NULL;
    END;
    
    if v_default = v_usrBranchCode then
    UPDATE TQC_USER_BRANCHES SET USB_DFLT_BRN ='N' WHERE USB_CODE = v_usrBranchCode;
    else
    UPDATE TQC_USER_BRANCHES SET USB_DFLT_BRN ='Y' WHERE USB_CODE = v_usrBranchCode;
    end if;
END makeDefaultBranch;




PROCEDURE assign_user_default_branch(
      v_user_code   IN TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
      v_branch_code IN TQC_USER_BRANCHES.USB_BRN_CODE%TYPE
      )
IS
BEGIN
    -- Lets first unselect the previous default branch(es)
    UPDATE TQC_USER_BRANCHES
    SET USB_DFLT_BRN = 'N'
    WHERE USB_USR_CODE = v_user_code;
    
    -- Now, lets assign the new default branch
    UPDATE TQC_USER_BRANCHES
    SET USB_DFLT_BRN = 'Y'
    WHERE USB_USR_CODE = v_user_code
    AND USB_BRN_CODE = v_branch_code;
    
    EXCEPTION
    WHEN OTHERS THEN
    raise_error('Error occured while updating record '||SQLERRM(SQLCODE));
                
END assign_user_default_branch;




PROCEDURE assign_user_default_division(
      v_user_code     IN TQC_USER_DIVISIONS.USD_USR_CODE%TYPE,
      v_division_code IN TQC_USER_DIVISIONS.USD_DIV_CODE%TYPE
      )
IS
BEGIN
    -- Lets first unselect the pervious default division(s)
    UPDATE TQC_USER_DIVISIONS
    SET USD_DEFAULT = 'N'
    WHERE USD_USR_CODE = v_user_code;
    
    -- Now, lets assign the new default division
    UPDATE TQC_USER_DIVISIONS
    SET USD_DEFAULT = 'Y'
    WHERE USD_USR_CODE = v_user_code
    AND USD_DIV_CODE = v_division_code;
    
    EXCEPTION
    WHEN OTHERS THEN
    raise_error('Error occured while updating record '||SQLERRM(SQLCODE));
    
END assign_user_default_division;




 PROCEDURE grantProcessRole(v_processCode IN TQC_SYS_PROCESSES.SPRC_CODE%TYPE,

    v_roleCode   IN TQC_SYS_ROLES.SRLS_CODE%TYPE,

    v_process_only   IN VARCHAR2 DEFAULT 'N')
    IS

v_processRoleCode
TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE;

CURSOR cur_prcss IS SELECT *

FROM TQC_SYS_PROCESSES

WHERE SPRC_CODE = v_processCode

AND SPRC_CODE NOT IN (SELECT SRPRC_SPRC_CODE

FROM TQC_SYS_ROLES_PROCESSES

WHERE SRPRC_SRLS_CODE = v_roleCode);

CURSOR AREA_CUR IS SELECT SPRCA_CODE FROM TQC_SYS_PROCESS_AREAS

WHERE SPRCA_SPRC_CODE = v_processCode;

BEGIN

-- raise_error(v_processCode||'='||v_roleCode||'='||v_process_only);

FOR cur_prcss_rec IN cur_prcss LOOP

SELECT TQC_SRPRC_SEQ.NEXTVAL INTO v_processRoleCode FROM DUAL;

BEGIN

INSERT INTO TQC_SYS_ROLES_PROCESSES (SRPRC_CODE, SRPRC_SRLS_CODE, SRPRC_SPRC_CODE)

VALUES(v_processRoleCode,v_roleCode,cur_prcss_rec.SPRC_CODE);

EXCEPTION

WHEN OTHERS THEN

RAISE_ERROR
('Error assigning process to the role..'||v_processCode||'='||v_roleCode||'='||v_process_only);

END;

IF NVL(v_process_only,'N') != 'Y' THEN

FOR prc_Area IN AREA_CUR LOOP

Grant_Role_Process_Area
(prc_Area.SPRCA_CODE,v_roleCode);

END LOOP;

END IF;

END LOOP;

END grantProcessRole;

/*PROCEDURE grantProcessRole(v_processCode    IN      TQC_SYS_ROLES_PROCESSES.SRPRC_SPRC_CODE%TYPE,
                           v_roleCode       IN      TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE,
                           v_process_only   IN VARCHAR2 DEFAULT 'N') IS
    v_processRoleCode   TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE;
    
    CURSOR cur_prcss IS SELECT  *
                        FROM TQC_SYS_PROCESSES
                        WHERE SPRC_CODE = v_processCode
                        AND SPRC_CODE NOT IN (SELECT SRPRC_SPRC_CODE
                                                FROM TQC_SYS_ROLES_PROCESSES
                                                WHERE SRPRC_SRLS_CODE = v_roleCode);
    
    CURSOR AREA_CUR IS  SELECT SPRCA_CODE FROM TQC_SYS_PROCESS_AREAS
    WHERE SPRCA_SPRC_CODE = v_processCode;
     
BEGIN
    FOR cur_prcss_rec IN cur_prcss LOOP
        SELECT TQC_SRPRC_SEQ.NEXTVAL INTO v_processRoleCode FROM DUAL;
    
        INSERT INTO TQC_SYS_ROLES_PROCESSES (SRPRC_CODE, SRPRC_SRLS_CODE, SRPRC_SPRC_CODE)
        VALUES(v_processRoleCode,v_roleCode,cur_prcss_rec.SPRC_CODE);
        IF NVL(v_process_only,'N') != 'Y' THEN 
            FOR prc_Area IN AREA_CUR LOOP
                Grant_Role_Process_Area(prc_Area.SPRCA_CODE,v_processRoleCode);
            END LOOP;
        END IF;
    END LOOP;
END grantProcessRole;
*/
PROCEDURE revokeProcessRole(v_processCode    IN      TQC_SYS_ROLES_PROCESSES.SRPRC_SPRC_CODE%TYPE,
                            v_roleCode       IN      TQC_SYS_ROLES_PROCESSES.SRPRC_SRLS_CODE%TYPE) IS
    CURSOR AREA_CUR IS  SELECT SPRCA_CODE FROM TQC_SYS_PROCESS_AREAS
    WHERE SPRCA_SPRC_CODE = v_processCode;
    
    v_processRoleCode   TQC_SYS_ROLES_PROCESSES.SRPRC_CODE%TYPE;
BEGIN
    BEGIN
    --raise_error(v_processCode ||';'||v_roleCode);
        SELECT SRPRC_CODE INTO v_processRoleCode
        FROM TQC_SYS_ROLES_PROCESSES
        WHERE SRPRC_SPRC_CODE = v_processCode
        AND SRPRC_SRLS_CODE = v_roleCode;
    EXCEPTION WHEN NO_DATA_FOUND THEN
        NULL;
    END;
    
    FOR prc_Area IN AREA_CUR LOOP
        Revoke_Role_Process_Area(prc_Area.SPRCA_CODE,v_processRoleCode);
    END LOOP;
    
    DELETE FROM TQC_SYS_ROLES_PROCESSES 
    WHERE SRPRC_SPRC_CODE = v_processCode
    AND SRPRC_SRLS_CODE = v_roleCode;
    
END revokeProcessRole;

FUNCTION getAllUserAssignedSystems (v_user_code NUMBER) RETURN  userAssignedSystems_ref 
        IS v_cursor userAssignedSystems_ref;
BEGIN
     OPEN v_cursor FOR
        SELECT SYS_CODE, SYS_SHT_DESC, SYS_NAME, SYS_ACTIVE, 
        USYS_CODE, USYS_USR_CODE, USYS_SYS_CODE, USYS_WEF, USYS_WET, USYS_SPOST_CODE,
        SYS_POST_NAME(USYS_SPOST_CODE) SPOST_DESC
        FROM TQC_USER_SYSTEMS, TQC_SYSTEMS
        WHERE TQC_USER_SYSTEMS.USYS_SYS_CODE = TQC_SYSTEMS.SYS_CODE
        AND SYS_ACTIVE <> 'N'
        AND USYS_USR_CODE = v_user_code
        ORDER BY SYS_NAME;
    RETURN v_cursor;
END;

FUNCTION SYS_POST_NAME(v_spost_code NUMBER ) RETURN VARCHAR2 IS 
    v_ret_val VARCHAR2(50);
	BEGIN
			SELECT SPOST_DESC
			INTO v_ret_val
			FROM TQC_SYS_POSTS
			WHERE  SPOST_CODE = v_spost_code;
		RETURN v_ret_val;
	END SYS_POST_NAME;
  FUNCTION GetRegionsFiltered(v_orgCode   IN  TQC_REGIONS.REG_ORG_CODE%TYPE,v_user_code TQC_USER_BRANCHES.USB_USR_CODE%TYPE )RETURN Regions_ref IS
    vcursor Regions_ref;
BEGIN
    OPEN vcursor FOR
   SELECT  REG_CODE, REG_ORG_CODE, REG_NAME
    FROM TQC_REGIONS 
    WHERE REG_ORG_CODE =  v_orgCode 
    AND REG_CODE  IN
        (  SELECT REG_CODE FROM(
    select count(1),REG_CODE,REG_ORG_CODE
    from tqc_regions,tqc_branches
    where REG_CODE = brn_REG_CODE
    AND REG_ORG_CODE =  v_orgCode
    and BRN_CODE  NOT in  (select USB_BRN_CODE from TQC_USER_BRANCHES where  USB_USR_CODE= v_user_code)
    group by REG_CODE,REG_ORG_CODE
    HAVING count(1)>=1));
    
    
    
  
    RETURN vcursor;
    
    
    
    
END;
  
PROCEDURE Grant_User_BranchByOrg (v_user         IN  TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
                             v_org_code  IN  TQC_REGIONS.REG_ORG_CODE%TYPE) IS
    v_Count         NUMBER;
    v_usb_code      NUMBER;
     CURSOR branches IS
        
        SELECT BRN_CODE ,BRN_NAME
        FROM TQC_BRANCHES,TQC_REGIONS
        WHERE  BRN_REG_CODE=REG_CODE AND REG_ORG_CODE =  v_org_code;
BEGIN

    
 
   FOR C IN branches LOOP
        SELECT COUNT(*) INTO v_Count
        FROM TQC_USER_BRANCHES
        WHERE USB_BRN_CODE = C.BRN_CODE
        AND USB_USR_CODE = v_user;
    
    
    
            IF(NVL(v_Count,0)=0)THEN
    
    
     
                 select nvl( max(usb_code),0) + 1 into     v_usb_code from tqc_user_branches;
                    INSERT INTO TQC_USER_BRANCHES (USB_CODE, USB_BRN_CODE, USB_USR_CODE, USB_STATUS, USB_DFLT_BRN)
                    VALUES (v_usb_code,C.BRN_CODE,v_user,'A','N');
     
     
    
    
             END IF;
    END LOOP;
END  Grant_User_BranchByOrg;
PROCEDURE Revoke_User_BranchByOrg (v_user         IN  TQC_USER_BRANCHES.USB_USR_CODE%TYPE,
                             v_org_code  IN  TQC_REGIONS.REG_ORG_CODE%TYPE,
                             v_err OUT VARCHAR2) IS
    v_Count         NUMBER;
    v_usb_code      NUMBER;
    v_def_brn Number;
     CURSOR branches IS
        
        
         SELECT BRN_CODE, BRN_SHT_DESC, BRN_REG_CODE, BRN_NAME
        FROM TQC_BRANCHES,TQC_REGIONS 
        WHERE  REG_ORG_CODE=v_org_code and BRN_REG_CODE=REG_CODE
        AND EXISTS 
        (
            SELECT USB_CODE, USB_BRN_CODE, USB_USR_CODE, USB_STATUS, USB_DFLT_BRN
            FROM TQC_USER_BRANCHES
            WHERE TQC_BRANCHES.BRN_CODE = TQC_USER_BRANCHES.USB_BRN_CODE
            AND USB_USR_CODE = v_user
        );
        
BEGIN
select count(*) INTO v_Count  FROM TQC_USER_BRANCHES where USB_DFLT_BRN='Y' AND USB_USR_CODE=v_user;
IF (v_Count>0) THEN
  BEGIN
        SELECT NVL(USB_BRN_CODE,-9999) into v_def_brn  FROM TQC_USER_BRANCHES where USB_DFLT_BRN='Y' AND USB_USR_CODE=v_user;
    
 
           FOR C IN branches LOOP
       
           IF (nvL(v_def_brn,-9999)=C.BRN_CODE) THEN
             BEGIN
               v_err:='DEFAULT BRANCH NOT REMOVED';
             END;
     
           ELSE  
             BEGIN
              Delete from TQC_USER_BRANCHES WHERE  USB_BRN_CODE=C.BRN_CODE AND USB_USR_CODE=v_user;
             END  ; 
             END IF;       
            END LOOP;
   
   END;
   ELSE 
   BEGIN
          FOR C IN branches LOOP
       

          Delete from TQC_USER_BRANCHES WHERE  USB_BRN_CODE=C.BRN_CODE AND USB_USR_CODE=v_user;
             
          END LOOP;
   END;
    
  END IF;
END Revoke_User_BranchByOrg;
 FUNCTION Get_user_OrgsFiltered(v_usrCode  IN  TQC_USER_BRANCHES.USB_USR_CODE%TYPE) RETURN Organizations_ref IS
    vcursor Organizations_ref;
BEGIN
    OPEN vcursor FOR
    
     SELECT DISTINCT ORG_CODE, ORG_SHT_DESC, ORG_NAME
    FROM TQC_ORGANIZATIONS,TQC_SYSTEMS, TQC_USER_SYSTEMS 
    WHERE ORG_CODE = SYS_ORG_CODE
    AND SYS_CODE = USYS_SYS_CODE
    AND USYS_USR_CODE = v_usrCode 
    AND ORG_CODE IN( SELECT  REG_ORG_CODE  
    FROM TQC_REGIONS 
    WHERE REG_ORG_CODE = ORG_CODE 
    AND REG_CODE  IN
        (  SELECT REG_CODE FROM(
                        select count(1),REG_CODE,REG_ORG_CODE
                        from tqc_regions,tqc_branches
                        where REG_CODE = brn_REG_CODE
                        AND REG_ORG_CODE = REG_ORG_CODE
                        and BRN_CODE  NOT in  (select USB_BRN_CODE from TQC_USER_BRANCHES where  USB_USR_CODE= v_usrCode )
                        group by REG_CODE,REG_ORG_CODE
                    HAVING count(1)>=1)));
    
    RETURN vcursor;
END;
  PROCEDURE Grant_User_AllRoleInSys (v_userCode       IN  TQC_USERS.USR_CODE%TYPE,
                             v_user           IN  TQC_USERS.USR_CODE%TYPE,
                             v_role           IN  TQC_SYS_ROLES.SRLS_CODE%TYPE,
                             v_sys_code            IN  TQC_SYSTEMS.SYS_CODE%TYPE
                             ) IS
      v_count NUMBER;
         
 Cursor usr_roles IS   SELECT SRLS_CODE, SRLS_SYS_CODE, SRLS_NAME, SRLS_STATUS
        FROM TQC_SYS_ROLES
        WHERE SRLS_STATUS <> 'N'
        AND SRLS_SYS_CODE = v_sys_code
        AND NOT EXISTS 
        (
            SELECT USRR_USR_CODE, USRR_SRLS_CODE
            FROM TQC_SYS_USER_ROLES
            WHERE TQC_SYS_ROLES.SRLS_CODE = TQC_SYS_USER_ROLES.USRR_SRLS_CODE
            AND USRR_USR_CODE = v_user
        );
BEGIN
    
 
     FOR C IN usr_roles LOOP   
    
            
            INSERT INTO TQC_SYS_USER_ROLES (USRR_USR_CODE, USRR_SRLS_CODE, USRR_GRANT_DATE, 
                        USRR_REVOKE_DATE, USRR_GRANT_BY_USR_CODE, USRR_REVOKE_BY_USR_CODE)
            VALUES (v_user,C.SRLS_CODE,SYSDATE,
                    NULL,v_userCode,NULL);
            
    END LOOP;
END Grant_User_AllRoleInSys;
 PROCEDURE Revoke_User_AllRoleInSys (v_userCode       IN  TQC_USERS.USR_CODE%TYPE,
                             v_user           IN  TQC_USERS.USR_CODE%TYPE,
                             v_role           IN  TQC_SYS_ROLES.SRLS_CODE%TYPE,
                             v_sys_code            IN  TQC_SYSTEMS.SYS_CODE%TYPE
                             ) IS
      
         
 Cursor usr_roles IS   SELECT SRLS_CODE, SRLS_SYS_CODE, SRLS_NAME, SRLS_STATUS
        FROM TQC_SYS_ROLES
        WHERE SRLS_STATUS <> 'I'
        AND SRLS_SYS_CODE = v_sys_code
        AND  EXISTS 
        (
            SELECT USRR_USR_CODE, USRR_SRLS_CODE
            FROM TQC_SYS_USER_ROLES
            WHERE TQC_SYS_ROLES.SRLS_CODE = TQC_SYS_USER_ROLES.USRR_SRLS_CODE
            AND USRR_USR_CODE = v_user
        );
BEGIN
    
 
     FOR C IN usr_roles LOOP 
     DELETE FROM TQC_SYS_USER_ROLES
    WHERE USRR_USR_CODE = v_user
    AND USRR_SRLS_CODE = C.SRLS_CODE;
     
           
    END LOOP;
END Revoke_User_AllRoleInSys;
FUNCTION get_AssignedSystems_withRole (v_user_code   IN TQC_USERS.USR_CODE%TYPE) RETURN  user_unassigned_systems_ref 
        IS vcursor user_unassigned_systems_ref;
BEGIN
     OPEN vcursor FOR
     
       SELECT SYS_CODE, SYS_SHT_DESC, SYS_NAME, SYS_ACTIVE
        FROM TQC_SYSTEMS
        WHERE SYS_ACTIVE <> 'N'
        AND EXISTS
        (
            SELECT USYS_CODE, USYS_USR_CODE, USYS_SYS_CODE 
            FROM TQC_USER_SYSTEMS
            WHERE TQC_SYSTEMS.SYS_CODE = TQC_USER_SYSTEMS.USYS_SYS_CODE
            AND USYS_USR_CODE = v_user_code 
        )
        AND  SYS_CODE IN(select SYS_CODE from(
        
        
        SELECT SRLS_SYS_CODE AS SYS_CODE ,COUNT(*) 
        FROM TQC_SYS_ROLES
        WHERE SRLS_STATUS NOT IN('N','I')
       -- AND SRLS_SYS_CODE = SYS_CODE 
        AND    EXISTS 
        (
            SELECT USRR_USR_CODE, USRR_SRLS_CODE
            FROM TQC_SYS_USER_ROLES
            WHERE TQC_SYS_ROLES.SRLS_CODE = TQC_SYS_USER_ROLES.USRR_SRLS_CODE
            AND USRR_USR_CODE = v_user_code
        )  
       GROUP BY SRLS_SYS_CODE  Having count(*)>=1));
     return vcursor;
END;
FUNCTION get_UnAssignedSystems_withRole (v_user_code   IN TQC_USERS.USR_CODE%TYPE) RETURN  user_unassigned_systems_ref 
        IS vcursor user_unassigned_systems_ref;
BEGIN
     OPEN vcursor FOR
     
       SELECT SYS_CODE, SYS_SHT_DESC, SYS_NAME, SYS_ACTIVE
        FROM TQC_SYSTEMS
        WHERE SYS_ACTIVE <> 'N'
        AND EXISTS
        (
            SELECT USYS_CODE, USYS_USR_CODE, USYS_SYS_CODE 
            FROM TQC_USER_SYSTEMS
            WHERE TQC_SYSTEMS.SYS_CODE = TQC_USER_SYSTEMS.USYS_SYS_CODE
            AND USYS_USR_CODE = v_user_code 
        )
        AND  SYS_CODE IN(select SYS_CODE from(
        
        
        SELECT SRLS_SYS_CODE AS SYS_CODE ,COUNT(*) 
        FROM TQC_SYS_ROLES
        WHERE SRLS_STATUS NOT IN('N','I')
       -- AND SRLS_SYS_CODE = SYS_CODE 
        AND  NOT  EXISTS 
        (
            SELECT USRR_USR_CODE, USRR_SRLS_CODE
            FROM TQC_SYS_USER_ROLES
            WHERE TQC_SYS_ROLES.SRLS_CODE = TQC_SYS_USER_ROLES.USRR_SRLS_CODE
            AND USRR_USR_CODE = v_user_code
        )  
       GROUP BY SRLS_SYS_CODE  Having count(*)>=1));
     return vcursor;
END;
 PROCEDURE get_usersNotInGrp(v_Users_ref OUT Users_ref,v_grp_code IN Number )
      
  IS
      
  BEGIN
 --Raise_error('grp_code-->'||v_grp_code );
  OPEN v_Users_ref FOR
  SELECT USR_CODE, USR_USERNAME, USR_NAME, USR_EMAIL, USR_PERSONEL_RANK,
     USR_DT_CREATED, USR_TYPE, USR_STATUS, USR_PWD_RESET, USR_PER_ID,USR_ACCT_MGR,USR_CELL_PHONE_NO,
     USR_WEF_DT, USR_WET_DT,NULL,NULL
  FROM TQC_USERS 
  where USR_CODE NOT IN  (SELECT GUSR_USR_CODE  FROM   TQC_GROUP_USERS  WHERE   GUSR_GRP_USR_CODE=v_grp_code) order by USR_CODE  Desc;
     
 END get_usersNotInGrp;
 
PROCEDURE getFullUserDetails(v_userName IN TQC_USERS.USR_USERNAME%TYPE,
        v_userDetails OUT Users_ref)
        IS
        BEGIN
        OPEN v_userDetails FOR
        SELECT USR_CODE, USR_USERNAME, USR_NAME, USR_EMAIL, USR_PERSONEL_RANK,
              USR_DT_CREATED, USR_TYPE, USR_STATUS, USR_PWD_RESET, USR_PER_ID,USR_ACCT_MGR,USR_CELL_PHONE_NO,
              USR_WEF_DT, USR_WET_DT,NULL,NULL
         FROM TQC_USERS 
         WHERE USR_USERNAME=v_userName;
        END getFullUserDetails;
 
PROCEDURE create_system_administrator(v_user varchar2)
 IS



  v_success_status VARCHAR2(10 BYTE);
  v_role_code NUMBER;
  v_admin_user_code NUMBER;
  CURSOR processes_cursor IS SELECT * FROM TQC_SYS_PROCESSES WHERE SPRC_SYS_CODE=0;
  CURSOR USERS_CUR  IS SELECT USR_USERNAME FROM TQC_USERS;
  v_count NUMBER;
 
  
 BEGIN

 --CREATE ADMINISTRATOR USER ACCOUNT
     BEGIN
    SELECT COUNT(USR_CODE) INTO v_count    FROM TQC_USERS WHERE UPPER(USR_USERNAME)=v_user;
  EXCEPTION 
          WHEN OTHERS THEN 
           RAISE_ERROR('ERROR DETERMINING IF  THE ADMINISTRATOR ACCOUNT ALREADY EXITS');
  END; 
  IF v_count=0 THEN 
  
     BEGIN
         
      
     Create_User('ADMIN','SYSTEM ADMINISTRATOR','ADMINISTRATOR',NULL,NULL,'A','U','Y',NULL,v_admin_user_code,null,null);
      
             EXCEPTION 
          WHEN OTHERS THEN 
           RAISE_ERROR('ERROR CREATING THE ADMINISTRATOR ACCOUNT'||SQLERRM(SQLCODE));
       
     END;
  ELSE
     BEGIN
        SELECT USR_CODE INTO v_admin_user_code    FROM TQC_USERS WHERE UPPER(USR_USERNAME)=v_user;
       -- UpDate_User(v_admin_user_code,'ADMIN','SYSTEM ADMINISTRATOR',NULL,NULL,'U',NULL,NULL);
     
     EXCEPTION 
         
          WHEN OTHERS THEN 
           RAISE_ERROR('ERROR DETERMINING  EXISTING ADMINISTRATOR ACCOUNT');
     END;
  END IF;
  --GRANT ADMININISTRATOR ACCESS TO CRM
  BEGIN
       Grant_User_System(v_admin_user_code,0);
  EXCEPTION 
          WHEN OTHERS THEN 
           RAISE_ERROR('ERROR GRANTING SYSTEMS ACCESS TO THE ADMINISTRATOR ');
  END; 

  
 --CHECK IF AN ADMINISTRATOR ROLE  ALREADY EXISTS
  BEGIN
  SELECT SRLS_CODE  INTO v_role_code  FROM TQC_SYS_ROLES
  WHERE SRLS_SYS_CODE=0 
  AND UPPER(SRLS_NAME)='ADMINISTRATOR'; 
  EXCEPTION 
          WHEN NO_DATA_FOUND THEN 
          NULL;
  END; 
  
  IF v_role_code IS NULL THEN
       BEGIN
       Creat_Role(0,'ADMINISTRATOR','ADMIN');
       SELECT SRLS_CODE INTO  v_role_code FROM TQC_SYS_ROLES
       WHERE SRLS_SYS_CODE=0 
            AND UPPER(SRLS_NAME)='ADMINISTRATOR'; 
     
       INSERT INTO TQC_SYS_USER_ROLES (USRR_USR_CODE, USRR_SRLS_CODE, USRR_GRANT_DATE, 
                USRR_REVOKE_DATE, USRR_GRANT_BY_USR_CODE, USRR_REVOKE_BY_USR_CODE)
       VALUES (v_admin_user_code,v_role_code,SYSDATE,
            NULL,v_admin_user_code,NULL);
    
       EXCEPTION 
              WHEN OTHERS THEN 
               RAISE_ERROR('ERROR CREATING ADMINISTRATOR ROLE ');
       END;
   ELSE
       BEGIN
         SELECT COUNT(*) INTO v_count
        FROM TQC_SYS_USER_ROLES
        WHERE USRR_USR_CODE = v_admin_user_code
        AND USRR_SRLS_CODE = v_role_code;
    
        IF(NVL(v_count,0)=0) THEN
        INSERT INTO TQC_SYS_USER_ROLES (USRR_USR_CODE, USRR_SRLS_CODE, USRR_GRANT_DATE, 
                    USRR_REVOKE_DATE, USRR_GRANT_BY_USR_CODE, USRR_REVOKE_BY_USR_CODE)
        VALUES (v_admin_user_code,v_role_code,SYSDATE,
                NULL,v_admin_user_code,NULL);
        ELSE
        UPDATE  TQC_SYS_USER_ROLES SET USRR_REVOKE_DATE = NULL,USRR_REVOKE_BY_USR_CODE = NULL 
        WHERE USRR_USR_CODE = v_admin_user_code
        AND USRR_SRLS_CODE = v_role_code;
        END IF;
       EXCEPTION 
              WHEN OTHERS THEN 
               RAISE_ERROR('ERROR GRANTING ADMINISTRATOR ROLE TO ADMINISTRATOR USER '||v_role_code);
       END; 
   END IF;
   
  
   
   BEGIN
        
        FOR process_rec IN processes_cursor LOOP
            grantProcessRole(process_rec.SPRC_CODE,v_role_code);
        END LOOP;
    
    EXCEPTION 
              WHEN OTHERS THEN 
               RAISE_ERROR('ERROR GRANTING PROCESSES TO ADMINISTRATOR ROLE ');
   END;
   
  
   COMMIT;
  
 END create_system_administrator;
 
  
   
FUNCTION get_UsrEmail (v_user_name   varchar2) RETURN  Varchar2  
        IS 
        v_email varchar2(50);
        
BEGIN

 select USR_EMAIL into v_email from tqc_users where USR_NAME=v_user_name ;
 return v_email;
END;

FUNCTION get_UsrEmailAddr (v_user_code   Number) RETURN  Varchar2  
        IS 
        v_email varchar2(50);
        
BEGIN

 select USR_EMAIL into v_email from tqc_users where USR_CODE=v_user_code ;
 return v_email;
END;
PROCEDURE create_missing_prc_s_areas (v_sys_code TQC_SYSTEMS.SYS_CODE%TYPE,
                                      v_sprc_sht_desc  TQC_SYS_PROCESSES.SPRC_SHT_DESC%TYPE,
                                      v_sprc_process  TQC_SYS_PROCESSES.SPRC_PROCESS%TYPE,
                                      v_sprca_sht_desc  TQC_SYS_PROCESS_AREAS.SPRCA_SHT_DESC%TYPE,
                                      v_sprca_area  TQC_SYS_PROCESS_AREAS.SPRCA_AREA%TYPE,
                                      v_sprsa_sht_desc  TQC_SYS_PROCESS_SUB_AREAS.SPRsA_SHT_DESC%TYPE,
                                      v_sprsa_sub_area  TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SUB_AREA%TYPE,
                                      v_sprsa_type  TQC_SYS_PROCESS_SUB_AREAS.SPRSA_TYPE%TYPE) IS
                                      
                                      
    v_sprc_code     TQC_SYS_PROCESSES.sprc_code%TYPE; 
    v_sprca_code    TQC_SYS_PROCESS_AREAS.sprca_code%TYPE;
    v_sprsa_code    TQC_SYS_PROCESS_SUB_AREAS.sprsa_code%TYPE;
BEGIN
    BEGIN
        SELECT SPRC_CODE
        INTO v_sprc_code
        FROM TQC_SYS_PROCESSES
        WHERE SPRC_SYS_CODE = v_sys_code
        AND SPRC_SHT_DESC = v_sprc_sht_desc;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_sprc_code := NULL;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error');
    END;
    
    IF v_sprc_code IS NULL THEN 
        SELECT TQC_SPRC_SEQ.NEXTVAL INTO v_sprc_code FROM DUAL;
        INSERT INTO TQC_SYS_PROCESSES
            (SPRC_CODE, SPRC_SYS_CODE, SPRC_PROCESS, SPRC_SHT_DESC)
        VALUES
            (v_sprc_code, v_sys_code, v_sprc_process, v_sprc_sht_desc);
    END IF;
    
    BEGIN
        SELECT SPRCA_CODE
        INTO v_sprca_code 
        FROM TQC_SYS_PROCESS_AREAS
        WHERE SPRCA_SPRC_CODE = v_sprc_code
        AND SPRCA_SHT_DESC = v_sprca_sht_desc;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_sprca_code := NULL;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error');
    END;
    
    IF v_sprca_code IS NULL THEN 
        SELECT TQC_SPRCA_SEQ.NEXTVAL INTO v_sprca_code FROM DUAL;
        INSERT INTO TQC_SYS_PROCESS_AREAS
            (SPRCA_CODE, SPRCA_SPRC_CODE, SPRCA_AREA, SPRCA_SHT_DESC)
        VALUES
            (v_sprca_code, v_sprc_code, v_sprca_area, v_sprca_sht_desc);
    END IF;
    
    BEGIN
        SELECT SPRSA_CODE
        INTO v_sprsa_code 
        FROM TQC_SYS_PROCESS_SUB_AREAS
        WHERE SPRSA_SPRC_CODE = v_sprc_code
        AND SPRSA_SPRCA_CODE = v_sprca_code
        AND SPRSA_SHT_DESC = v_sprsa_sht_desc;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_sprsa_code := NULL;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error');
    END;
    
    IF v_sprsa_code IS NULL THEN 
        SELECT TQC_SPRCA_SEQ.NEXTVAL INTO v_sprsa_code FROM DUAL;
        INSERT INTO TQC_SYS_PROCESS_SUB_AREAS
            (SPRSA_CODE, SPRSA_SPRCA_CODE, SPRSA_SPRC_CODE, SPRSA_SUB_AREA, SPRSA_TYPE, SPRSA_SHT_DESC)
        VALUES
            (v_sprsa_code, v_sprca_code,v_sprc_code, v_sprsa_sub_area,v_sprsa_type, v_sprsa_sht_desc);
    END IF;
END create_missing_prc_s_areas;
PROCEDURE grant_process_sub_area(v_sys_code TQC_SYSTEMS.SYS_CODE%TYPE,
                                  v_srls_sht_desc TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
                                  v_srls_name TQC_SYS_ROLES.SRLS_NAME%TYPE,
                                  v_sprc_sht_desc  TQC_SYS_PROCESSES.SPRC_SHT_DESC%TYPE,
                                  v_sprca_sht_desc  TQC_SYS_PROCESS_AREAS.SPRCA_SHT_DESC%TYPE,
                                  v_sprsa_sht_desc  TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SHT_DESC%TYPE,
                                  v_grant        VARCHAR2,
                                  v_debit_limit  TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_DEBIT_LIMIT%TYPE,
                                  v_credit_limit TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CREDIT_LIMIT%TYPE
                                  ) IS
    v_srls_code NUMBER;
    v_sprc_code NUMBER;
    v_sprca_code NUMBER;
    v_sprsa_code NUMBER;
    v_srprc_code NUMBER;
    v_srpra_code NUMBER;
    v_srpsa_code NUMBER;
BEGIN
    BEGIN
        SELECT SRLS_CODE
        INTO v_srls_code
        FROM TQC_SYS_ROLES
        WHERE SRLS_SYS_CODE = v_sys_code
        AND SRLS_SHT_DESC = v_srls_sht_desc;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_srls_code := NULL;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error getting role details..');
    END;
    
    BEGIN
        SELECT  SPRC_CODE, SPRCA_CODE,SPRSA_CODE
        INTO v_sprc_code, v_sprca_code,v_sprsa_code
        FROM TQC_SYS_PROCESSES, TQC_SYS_PROCESS_AREAS, TQC_SYS_PROCESS_SUB_AREAS
        WHERE SPRC_CODE = SPRCA_SPRC_CODE 
        AND SPRCA_CODE = SPRSA_SPRCA_CODE
        AND SPRC_SHT_DESC = v_sprc_sht_desc
        AND SPRCA_SHT_DESC = v_sprca_sht_desc
        AND SPRSA_SHT_DESC = v_sprsa_sht_desc;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            RAISE_ERROR('Process area not defined..'||v_sprc_sht_desc||'='||v_sprca_sht_desc||'='||v_sprsa_sht_desc);
        WHEN OTHERS THEN 
            RAISE_ERROR('Error..'||v_sprc_sht_desc||'='||v_sprca_sht_desc||'='||v_sprsa_sht_desc);
            
    END;
        
    IF v_srls_code IS NULL AND v_grant = 'Y' THEN 
        SELECT tqc_SRLS_SEQ.NEXTVAL INTO v_srls_code FROM DUAL;
        INSERT INTO TQC_SYS_ROLES
            (SRLS_CODE, SRLS_SYS_CODE, SRLS_NAME, SRLS_CRT_DATE, SRLS_SHT_DESC, SRLS_STATUS)
        VALUES
            (v_srls_code, v_sys_code, v_srls_name, TRUNC(SYSDATE), v_srls_sht_desc, 'A');
    END IF;
    
    BEGIN
        SELECT SRPRC_CODE
        INTO v_srprc_code
        FROM TQC_SYS_ROLES_PROCESSES
        WHERE SRPRC_SRLS_CODE =  v_srls_code
        AND SRPRC_SPRC_CODE = v_sprc_code;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_srprc_code := NULL;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error getting Process code');
    END;
    IF v_srprc_code IS NULL AND v_grant = 'Y'  THEN 
        SELECT TQC_SRPRC_SEQ.NEXTVAL INTO v_srprc_code FROM DUAL;
        INSERT INTO TQC_SYS_ROLES_PROCESSES
            (SRPRC_CODE, SRPRC_SRLS_CODE, SRPRC_SPRC_CODE)
        VALUES
            (v_srprc_code, v_srls_code, v_sprc_code);
    END IF;
    
    BEGIN 
        SELECT SRPRA_CODE
        INTO v_srpra_code
        FROM TQC_SYS_ROLES_PRCS_AREA
        WHERE SRPRA_SRPRC_CODE = v_srprc_code 
        AND SRPRA_SPRCA_CODE = v_sprca_code;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_srpra_code := NULL;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error getting Process Area code');
    END;
    IF v_srpra_code IS NULL AND v_grant = 'Y' THEN 
        SELECT TQC_SRPRA_SEQ.NEXTVAL INTO v_srpra_code FROM DUAL;
        INSERT INTO TQC_SYS_ROLES_PRCS_AREA
            (SRPRA_CODE, SRPRA_SRPRC_CODE, SRPRA_SPRCA_CODE)
        VALUES
            (v_srpra_code, v_srprc_code, v_sprca_code);
    END IF;
    
    
    
    BEGIN 
        SELECT SRPSA_CODE
        INTO v_srpsa_code
        FROM TQC_SYS_ROLES_PRCS_S_AREA
        WHERE SRPSA_SRPRA_CODE = v_srpra_code 
        AND SRPSA_SPRSA_CODE = v_sprsa_code;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN 
            v_srpsa_code := NULL;
        WHEN OTHERS THEN 
            RAISE_ERROR('Error getting Process Sub Area code');
    END;
    IF v_srpsa_code IS NULL AND v_grant = 'Y' THEN  
        SELECT TQC_SRPSA_SEQ.NEXTVAL INTO v_srpsa_code FROM DUAL;
        INSERT INTO TQC_SYS_ROLES_PRCS_S_AREA
            (SRPSA_CODE, SRPSA_SRPRA_CODE, SRPSA_SPRSA_CODE, SRPSA_DEBIT_LIMIT, SRPSA_CREDIT_LIMIT)
        VALUES
            (v_srpsa_code, v_srpra_code, v_sprsa_code,v_debit_limit,v_credit_limit );
    ELSIF v_srpsa_code IS NOT NULL AND v_grant = 'N' THEN 
        DELETE TQC_SYS_ROLES_PRCS_S_AREA
        WHERE SRPSA_CODE = v_srpsa_code;
    END IF;
END;
END TQC_ROLES_CURSORBK280514; 
/