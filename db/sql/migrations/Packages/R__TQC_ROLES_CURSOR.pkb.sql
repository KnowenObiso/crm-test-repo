--
-- TQC_ROLES_CURSOR  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_roles_cursor
AS
/******************************************************************************
   NAME:       TQC_ROLES_CURSOR
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        08/Oct/2009             1. Created this package body.
   07032018R-EUPDATE
******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20033,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   FUNCTION get_sys_roles (v_syscode IN tqc_systems.sys_code%TYPE)
      RETURN sys_roles_ref
   IS
      vcursor   sys_roles_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT srls_code, srls_name, srls_crt_date, srls_sht_desc,
                srls_status
           FROM tqc_sys_roles
          WHERE srls_sys_code = v_syscode AND srls_status = 'A';

      RETURN vcursor;
   END;

   FUNCTION get_user_roles (
      v_syscode   IN   tqc_systems.sys_code%TYPE,
      v_user      IN   tqc_users.usr_code%TYPE
   )
      RETURN sys_usrroles_ref
   IS
      vcursor   sys_usrroles_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sprc_code, srprc_code, srls_name, sprc_process,
                srls_crt_date, srls_status, srls_code, srls_sht_desc
           FROM tqc_sys_roles,
                tqc_sys_roles_processes,
                tqc_sys_processes,
                tqc_sys_user_roles
          WHERE srprc_srls_code = srls_code
            AND srprc_sprc_code = sprc_code
            AND usrr_srls_code = srls_code
            AND sprc_sys_code = NVL (v_syscode, sprc_sys_code)
            AND usrr_usr_code = v_user
            AND usrr_revoke_by_usr_code IS NULL;

      RETURN vcursor;
   END;

   FUNCTION getroleprocesses (
      v_syscode       IN   tqc_sys_processes.sprc_sys_code%TYPE,
      v_sysrolecode   IN   tqc_sys_roles_processes.srprc_srls_code%TYPE
   )
      RETURN role_processes_ref
   IS
      vcursor   role_processes_ref;
   BEGIN
--RAISE_ERROR('System '||v_sysCode ||' Role '||v_sysRoleCode);
      OPEN vcursor FOR
         SELECT  sprc_code, srprc_code,  sprc_process|| ' - ('||sprc_sht_desc||')' sprc_process,
                  DECODE (srprc_sprc_code, NULL, 'N', 'Y') process_assigned,
                  srprc_srls_code
             FROM tqc_sys_processes, tqc_sys_roles_processes
            WHERE sprc_code = srprc_sprc_code(+)
              AND srprc_srls_code(+) = v_sysrolecode
              AND sprc_sys_code = v_syscode
              AND sprc_visible = 'Y'
         ORDER BY sprc_process;

      RETURN vcursor;
   END;

   FUNCTION get_role_areas (
      v_process       IN   tqc_sys_processes.sprc_code%TYPE,
      v_roleprocess   IN   tqc_sys_roles_processes.srprc_code%TYPE
   )
      RETURN role_areas_ref
   IS
      vcursor   role_areas_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT sprca_code, sprca_area, srpra_code, srprc_code
                    FROM tqc_sys_roles_prcs_area,
                         tqc_sys_roles_processes,
                         tqc_sys_process_areas
                   WHERE srprc_code(+) = srpra_srprc_code
                     AND sprca_code = srpra_sprca_code(+)
                     AND sprca_sprc_code = v_process;

      RETURN vcursor;
   END;

   FUNCTION getprocessrolearea (
      v_process       IN   tqc_sys_processes.sprc_code%TYPE,
      v_sysrolecode   IN   tqc_sys_roles_processes.srprc_srls_code%TYPE
   )
      RETURN processrole_areas_ref
   IS
      vcursor   processrole_areas_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sprca_code,  sprca_area || ' - ('||sprca_sht_desc||')' sprca_area, srpra_sprca_code, srpra_code,
                srprc_code,
                DECODE (srpra_sprca_code, NULL, 'N', 'Y') area_assigned
           FROM tqc_sys_processes,
                tqc_sys_process_areas,
                (SELECT srprc_sprc_code, srpra_sprca_code, srpra_code,
                        srprc_code
                   FROM tqc_sys_roles_processes, tqc_sys_roles_prcs_area
                  WHERE srprc_code = srpra_srprc_code
                    AND srprc_srls_code = v_sysrolecode)
          WHERE sprc_code = sprca_sprc_code
            AND sprca_sprc_code = v_process
            AND sprca_sprc_code = srprc_sprc_code(+)
            AND sprca_code = srpra_sprca_code(+)
            AND sprca_visible='Y';

      RETURN vcursor;
   END;

   FUNCTION get_role_subareas (
      v_processarea       IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_roleprocessarea   IN   tqc_sys_roles_prcs_area.srpra_srprc_code%TYPE
   )
      RETURN role_subareas_ref
   IS
      vcursor   role_subareas_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT srpsa_code, sprsa_code, sprsa_sub_area, sprsa_type,
                srpsa_debit_limit, srpsa_credit_limit
           FROM tqc_sys_process_sub_areas,
                tqc_sys_roles_prcs_s_area,
                tqc_sys_process_areas
          WHERE sprsa_code = srpsa_sprsa_code(+)
            AND sprca_code = sprsa_sprca_code
            AND sprsa_sprca_code = v_processarea;

      --AND (SRPSA_SPRSA_CODE = v_roleProcessArea OR SRPSA_CODE IS NULL);
      RETURN vcursor;
   END;

   FUNCTION getprocessrole_subareas (
      v_processarea   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_sysrolecode   IN   tqc_sys_roles_processes.srprc_srls_code%TYPE,
      v_process       IN   tqc_sys_processes.sprc_code%TYPE
   )
      RETURN processrole_subareas_ref
   IS
      vcursor   processrole_subareas_ref;
   BEGIN
--RAISE_ERROR('Process Area'||v_ProcessArea||'SysRoleCode'||v_sysRoleCode||'Process'||v_Process);
      OPEN vcursor FOR
         SELECT srpsa_code, sprsa_code,sprsa_sub_area ||' - ('||sprsa_sht_desc||')'  sprsa_sub_area  , sprsa_type,
                srpsa_debit_limit, srpsa_credit_limit,
                DECODE (srpsa_sprsa_code, NULL, 'N', 'Y') s_area_assigned
           FROM tqc_sys_processes,
                tqc_sys_process_areas,
                tqc_sys_process_sub_areas,
                (SELECT srpsa_code, srprc_sprc_code, srpra_sprca_code,
                        srpsa_sprsa_code, srpsa_debit_limit,
                        srpsa_credit_limit                 --,SRPSA_SRPRA_CODE
                   FROM tqc_sys_roles_processes,
                        tqc_sys_roles_prcs_area,
                        tqc_sys_roles_prcs_s_area
                  WHERE srprc_code = srpra_srprc_code
                    AND srpra_code = srpsa_srpra_code
                    AND srprc_srls_code = v_sysrolecode
                                                       --AND SRPRA_SPRCA_CODE =v_ProcessArea
                )
          WHERE sprc_code = sprca_sprc_code
            AND sprca_code = sprsa_sprca_code
            AND sprsa_sprc_code = srprc_sprc_code(+)
            AND sprsa_sprca_code = srpra_sprca_code(+)
            AND sprsa_code = srpsa_sprsa_code(+)
            AND sprsa_visible = 'Y'
            --AND SPRCA_SPRC_CODE = v_Process
            AND sprca_code = v_processarea;

      RETURN vcursor;
   END;

   FUNCTION get_users
      RETURN users_ref
   IS
      vcursor   users_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usr_code, usr_username, usr_name, usr_email,
                usr_personel_rank, usr_dt_created, usr_type, usr_status,
                usr_pwd_reset, usr_per_id, usr_acct_mgr, usr_cell_phone_no,
                usr_wef_dt, usr_wet_dt, NULL, NULL,usr_created_by, NULL, NULL
           FROM tqc_users;

      RETURN vcursor;
   END;

   FUNCTION get_all_individual_users
      RETURN users_ref
   IS
      vcursor   users_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usr_code, usr_username, usr_name, usr_email,
                usr_personel_rank, usr_dt_created, usr_type, usr_status,
                usr_pwd_reset, usr_per_id, usr_acct_mgr, usr_cell_phone_no,
                usr_wef_dt, usr_wet_dt, NULL, NULL,usr_created_by, NULL, NULL
           FROM tqc_users
          WHERE usr_type = 'U';

      RETURN vcursor;
   END;

   PROCEDURE get_all_users (
      v_users_ref   OUT      users_ref,
      v_date        IN       VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      -- raise_error('DATE---->'|| v_date );
      IF v_date IS NULL
      THEN
         OPEN v_users_ref FOR
            SELECT   usr_code, usr_username, usr_name, usr_email,
                     usr_personel_rank, usr_dt_created, usr_type, usr_status,
                     usr_pwd_reset, usr_per_id, usr_acct_mgr,
                     usr_cell_phone_no, usr_wef_dt, usr_wet_dt, secq_desc,
                     usr_security_answer,usr_created_by, SACT_CODE, SACT_DESCRIPTION
                FROM tqc_users, tqc_security_questions, lms_sub_account_types
               WHERE usr_secq_code = secq_code(+)
                    AND usr_sact_code = sact_code(+)
            ORDER BY usr_code DESC;
      ELSE
         OPEN v_users_ref FOR
            SELECT   usr_code, usr_username, usr_name, usr_email,
                     usr_personel_rank, usr_dt_created, usr_type, usr_status,
                     usr_pwd_reset, usr_per_id, usr_acct_mgr,
                     usr_cell_phone_no, usr_wef_dt, usr_wet_dt, secq_desc,
                     usr_security_answer,usr_created_by, SACT_CODE, SACT_DESCRIPTION
                FROM tqc_users, tqc_security_questions, lms_sub_account_types
               WHERE usr_secq_code = secq_code(+)
                 AND usr_sact_code = sact_code(+)
                 AND usr_dt_created LIKE
                                   '%' || TO_DATE (v_date, 'dd/mm/yyyy')
                                   || '%'
            ORDER BY usr_code DESC;
      END IF;
   END get_all_users;

   PROCEDURE get_accountofficers (v_users_ref OUT users_ref)
   IS
   BEGIN
      OPEN v_users_ref FOR
         SELECT usr_code, usr_username, usr_name, usr_email,
                usr_personel_rank, usr_dt_created, usr_type, usr_status,
                usr_pwd_reset, usr_per_id, usr_acct_mgr, usr_cell_phone_no,
                usr_wef_dt, usr_wet_dt, NULL, NULL,usr_created_by, NULL, NULL
           FROM tqc_users
          WHERE usr_code IN (SELECT clnt_acc_officer
                               FROM tqc_clients
                              WHERE clnt_acc_officer IS NOT NULL);
   END get_accountofficers;

   FUNCTION get_systems
      RETURN systems_ref
   IS
      vcursor   systems_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_active
           FROM tqc_systems
          WHERE sys_active <> 'N' AND sys_code <> 30;

      RETURN vcursor;
   END;

   FUNCTION get_user_systems (v_user IN tqc_users.usr_code%TYPE)
      RETURN user_systems_ref
   IS
      vcursor   user_systems_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usys_code, sys_code, sys_sht_desc, usys_wef, sys_name,
                usys_wet, sys_path
           FROM tqc_systems, tqc_user_systems
          WHERE sys_code = usys_sys_code
            AND usys_usr_code = v_user
            AND (usys_wet IS NULL OR usys_wet >= SYSDATE);

      RETURN vcursor;
   END;
   FUNCTION get_user_active_systems (v_user IN tqc_users.usr_code%TYPE)
      RETURN user_systems_ref
   IS
      vcursor   user_systems_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usys_code, sys_code, sys_sht_desc, usys_wef, sys_name,
                usys_wet, sys_path
           FROM tqc_systems, tqc_user_systems
          WHERE sys_code = usys_sys_code
            AND usys_usr_code = v_user
            AND sys_active='Y'
            AND (usys_wet IS NULL OR usys_wet >= SYSDATE);

      RETURN vcursor;
   END;

   FUNCTION get_user_assigned_orgs (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN organizations_ref
   IS
      vcursor   organizations_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT org_code, org_sht_desc, org_name
                    FROM tqc_organizations,
                         tqc_regions,
                         tqc_branches,
                         tqc_user_branches
                   WHERE org_code = reg_org_code
                     AND reg_code = brn_reg_code
                     AND usb_brn_code = brn_code
                     AND usb_usr_code = v_user_code;

      RETURN vcursor;
   END;
   
FUNCTION get_user_assigned_div_orgs (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN organizations_ref
   IS
      vcursor   organizations_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT org_code, org_sht_desc, org_name
                    FROM tqc_organizations,
                         tqc_regions,
                         tqc_branches,
                         tqc_user_branches,
                         tqc_user_divisions
                   WHERE org_code = reg_org_code
                     AND reg_code = brn_reg_code
                     AND usb_brn_code = brn_code
                     AND usd_usr_code = usb_usr_code
                     AND usb_usr_code = v_user_code;

      RETURN vcursor;
   END;
  FUNCTION get_user_assigned_regions (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN regions_ref
   IS
      vcursor   regions_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT reg_code, reg_org_code, reg_name
            FROM tqc_regions, tqc_branches,tqc_user_branches
           WHERE brn_reg_code = reg_code
             AND reg_org_code = v_orgcode
             AND tqc_user_branches.usb_brn_code = tqc_branches.brn_code
             AND usb_usr_code = v_user_code;
      RETURN vcursor;
   END;
   
FUNCTION get_user_assigned_div_regions (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN regions_ref
   IS
      vcursor   regions_ref;
   BEGIN
      OPEN vcursor FOR
        SELECT DISTINCT reg_code, reg_org_code, reg_name
                FROM tqc_regions, tqc_branches,tqc_user_branches
               WHERE brn_reg_code = reg_code
                 AND reg_org_code = v_orgcode
                 AND tqc_user_branches.usb_brn_code = tqc_branches.brn_code
                 AND usb_usr_code = v_user_code
                 AND EXISTS(
                    SELECT usd_usr_code 
                    FROM tqc_user_divisions
                    WHERE usd_usr_code=tqc_user_branches.usb_usr_code
            );
      RETURN vcursor;
   END;
FUNCTION get_user_unassigned_regions (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN regions_ref
   IS
      vcursor   regions_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT reg_code, reg_org_code, reg_name
                    FROM tqc_regions, tqc_branches,tqc_user_branches
                   WHERE brn_reg_code = reg_code
                     AND reg_org_code = v_orgcode
                     AND usb_brn_code = brn_code
                     AND NOT EXISTS(
                           SELECT DISTINCT reg_code, reg_org_code, reg_name
                            FROM tqc_regions, tqc_branches,tqc_user_branches
                           WHERE brn_reg_code = reg_code
                             AND reg_org_code = v_orgcode
                             AND tqc_user_branches.usb_brn_code = tqc_branches.brn_code
                             AND usb_usr_code = v_user_code
                     );
      RETURN vcursor;
   END;
   
FUNCTION get_regions (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN regions_ref
   IS
      vcursor   regions_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT reg_code, reg_org_code, reg_name
                    FROM tqc_regions, tqc_branches,tqc_user_branches
                   WHERE brn_reg_code = reg_code
                     AND reg_org_code = v_orgcode
                     AND usb_brn_code = brn_code; 
      RETURN vcursor;
   END;


   FUNCTION get_user_unassigned_systems (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN user_unassigned_systems_ref
   IS
      vcursor   user_unassigned_systems_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_active
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND NOT EXISTS (
                   SELECT usys_code, usys_usr_code, usys_sys_code
                     FROM tqc_user_systems
                    WHERE tqc_systems.sys_code =
                                                tqc_user_systems.usys_sys_code
                      AND usys_usr_code = v_user_code);

      RETURN vcursor;
   END;

   FUNCTION get_user_assigned_systems (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN user_unassigned_systems_ref
   IS
      vcursor   user_unassigned_systems_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_active
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND EXISTS (
                   SELECT usys_code, usys_usr_code, usys_sys_code
                     FROM tqc_user_systems
                    WHERE tqc_systems.sys_code =
                                                tqc_user_systems.usys_sys_code
                      AND usys_usr_code = v_user_code);

      RETURN vcursor;
   END;

   FUNCTION get_user_unassigned_roles (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_sys_code    IN   tqc_systems.sys_code%TYPE
   )
      RETURN user_unassigned_roles_ref
   IS
      vcursor   user_unassigned_roles_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT srls_code, srls_sys_code, srls_name, srls_status,NULL,NULL
           FROM tqc_sys_roles
          WHERE srls_status = 'A'
            AND srls_sys_code = v_sys_code
            AND NOT EXISTS (
                   SELECT usrr_usr_code, usrr_srls_code
                     FROM tqc_sys_user_roles
                    WHERE tqc_sys_roles.srls_code =
                                             tqc_sys_user_roles.usrr_srls_code
                      AND usrr_usr_code = v_user_code)
            ORDER BY srls_name;

      RETURN vcursor;
   END;

   FUNCTION get_user_assigned_roles (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_sys_code    IN   tqc_systems.sys_code%TYPE
   )
      RETURN user_unassigned_roles_ref
   IS
      vcursor   user_unassigned_roles_ref;
   BEGIN
   --RAISE_ERROR(' v_user_code '||v_user_code||' v_sys_code '||v_sys_code);
      OPEN vcursor FOR
         SELECT distinct srls_code, srls_sys_code, srls_name, srls_status,usrr_revoke_date,usrr_grant_date
           FROM tqc_sys_roles,tqc_sys_user_roles
          WHERE srls_status NOT IN ('N', 'I')
            AND srls_sys_code = v_sys_code
            AND usrr_srls_code = srls_code
            AND usrr_usr_code = v_user_code
            AND EXISTS (
                   SELECT usrr_usr_code, usrr_srls_code
                     FROM tqc_sys_user_roles
                    WHERE tqc_sys_roles.srls_code =
                                             tqc_sys_user_roles.usrr_srls_code
                      AND usrr_usr_code = v_user_code)
            ORDER BY srls_name;

      RETURN vcursor;
   END;

   FUNCTION get_user_unassigned_branches (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_reg_code    IN   tqc_regions.reg_code%TYPE
   )
      RETURN branches_ref
   IS
      vcursor   branches_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name
           FROM tqc_branches
          WHERE brn_reg_code = v_reg_code
            AND NOT EXISTS (
                   SELECT usb_code, usb_brn_code, usb_usr_code, usb_status,
                          usb_dflt_brn
                     FROM tqc_user_branches
                    WHERE tqc_branches.brn_code =
                                                tqc_user_branches.usb_brn_code
                      AND usb_usr_code = v_user_code);

      RETURN vcursor;
   END;

   FUNCTION get_user_assigned_branches (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_reg_code    IN   tqc_regions.reg_code%TYPE
   )
      RETURN branches_ref
   IS
      vcursor   branches_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name
           FROM tqc_branches
          WHERE brn_reg_code = v_reg_code
            AND EXISTS (
                   SELECT usb_code, usb_brn_code, usb_usr_code, usb_status,
                          usb_dflt_brn
                     FROM tqc_user_branches
                    WHERE tqc_branches.brn_code =
                                                tqc_user_branches.usb_brn_code
                      AND usb_usr_code = v_user_code);

      RETURN vcursor;
   END;
   
   FUNCTION get_user_assigned_div_branches(
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_reg_code    IN   tqc_regions.reg_code%TYPE
   )
      RETURN branches_ref
   IS
      vcursor   branches_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name
           FROM tqc_branches,tqc_user_branches
          WHERE brn_reg_code = v_reg_code
            AND tqc_branches.brn_code =tqc_user_branches.usb_brn_code
            AND usb_usr_code = v_user_code
            AND EXISTS(
                SELECT usd_usr_code 
                FROM tqc_user_divisions
                WHERE usd_usr_code=tqc_user_branches.usb_usr_code
            );
      RETURN vcursor;
   END;
    
   FUNCTION get_branches (
      v_user_code   IN   tqc_users.usr_code%TYPE,
      v_reg_code    IN   tqc_regions.reg_code%TYPE
   )
      RETURN branches_ref
   IS
      vcursor   branches_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name
           FROM tqc_branches
          WHERE brn_reg_code = v_reg_code;
      RETURN vcursor;
   END;

  FUNCTION get_default_user_branch (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN default_branch_ref
   IS
      vcursor   default_branch_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT org_code, org_name || ' ' || org_sht_desc, reg_code,
                reg_name, brn_code, brn_name, brh_cur_code, cur_desc,
                cur_symbol
           FROM tqc_branches,
                tqc_regions,
                tqc_organizations,
                fms_branches,
                tqc_currencies
          WHERE brn_reg_code = reg_code
            AND reg_org_code = org_code
            AND brn_code = brh_code(+)
            AND brh_cur_code = cur_code
            AND EXISTS (
                   SELECT usb_code, usb_brn_code, usb_usr_code, usb_status,
                          usb_dflt_brn
                     FROM tqc_user_branches
                    WHERE tqc_branches.brn_code =
                                                tqc_user_branches.usb_brn_code
                      AND usb_dflt_brn = 'Y'
                      AND usb_usr_code = v_user_code);

      RETURN vcursor;
   END;
   FUNCTION get_user_defaults (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN default_details_ref
   IS
      vcursor   default_details_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT org_code, org_name, reg_code, reg_name, brn_code, brn_name,
                org_cou_code, cou_name, cou_admin_reg_type,org_cur_code,cou_zip_code
           FROM tqc_branches, tqc_regions, tqc_organizations, tqc_countries
          WHERE brn_reg_code = reg_code
            AND reg_org_code = org_code
            AND org_cou_code = cou_code
            AND EXISTS (
                   SELECT usb_code, usb_brn_code, usb_usr_code, usb_status,
                          usb_dflt_brn
                     FROM tqc_user_branches
                    WHERE tqc_branches.brn_code =
                                                tqc_user_branches.usb_brn_code
                      AND usb_dflt_brn = 'Y'
                      AND usb_usr_code = v_user_code);

      RETURN vcursor;
   END;

   FUNCTION get_nondefault_user_branch (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN default_branch_ref
   IS
      vcursor   default_branch_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT org_code, org_name, reg_code, reg_name, brn_code, brn_name,
                org_cur_code, cur_desc, cur_symbol
           FROM tqc_branches, tqc_regions, tqc_organizations, tqc_currencies
          WHERE brn_reg_code = reg_code
            AND reg_org_code = org_code
            AND org_cur_code = cur_code
            AND brn_code IN (
                   SELECT brn_code
                     FROM tqc_user_branches, tqc_branches
                    WHERE tqc_branches.brn_code =
                                                tqc_user_branches.usb_brn_code
                      AND usb_dflt_brn <> 'Y'
                      AND usb_usr_code = v_user_code);

      RETURN vcursor;
   END;

   FUNCTION get_user_unassigned_divisions (
      v_user_code     IN   tqc_users.usr_code%TYPE,
      v_branch_code   IN   tqc_branches.brn_code%TYPE
   )
      RETURN user_unassigned_divisions_ref
   IS
      vcursor   user_unassigned_divisions_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT div_code, div_name, div_sht_desc, div_division_status
           FROM tqc_divisions, tqc_brnch_divisions
          WHERE div_code = bdiv_div_code
            AND bdiv_brn_code = v_branch_code
            AND NOT EXISTS (
                   SELECT usd_code, usd_usr_code, usd_div_code, usd_default
                     FROM tqc_user_divisions
                    WHERE tqc_divisions.div_code =
                                               tqc_user_divisions.usd_div_code
                      AND usd_usr_code = v_user_code);

      RETURN vcursor;
   END;

   FUNCTION get_user_assigned_divisions (
      v_user_code     IN   tqc_users.usr_code%TYPE,
      v_branch_code   IN   tqc_branches.brn_code%TYPE
   )
      RETURN user_unassigned_divisions_ref
   IS
      vcursor   user_unassigned_divisions_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT div_code, div_name, div_sht_desc, div_division_status
           FROM tqc_divisions, tqc_brnch_divisions
          WHERE div_code = bdiv_div_code
            AND bdiv_brn_code = v_branch_code
            AND EXISTS (
                   SELECT usd_code, usd_usr_code, usd_div_code, usd_default
                     FROM tqc_user_divisions
                    WHERE tqc_divisions.div_code =
                                               tqc_user_divisions.usd_div_code
                      AND usd_usr_code = v_user_code);

      RETURN vcursor;
   END;

   FUNCTION get_default_user_division (v_user_code IN tqc_users.usr_code%TYPE)
      RETURN default_division_ref
   IS
      vcursor   default_division_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT org_code, org_name, reg_code, reg_name, brn_code, brn_name,
                div_code, div_name
           FROM tqc_divisions,
                tqc_brnch_divisions,
                tqc_branches,
                tqc_regions,
                tqc_organizations
          WHERE div_code = bdiv_div_code
            AND bdiv_brn_code = brn_code
            AND brn_reg_code = reg_code
            AND reg_org_code = org_code
            AND EXISTS (
                   SELECT usd_code, usd_usr_code, usd_div_code, usd_default
                     FROM tqc_user_divisions
                    WHERE tqc_divisions.div_code =
                                               tqc_user_divisions.usd_div_code
                      AND usd_default = 'Y'
                      AND usd_usr_code = v_user_code);

      RETURN vcursor;
   END;

   FUNCTION get_nondefault_user_division (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN default_division_ref
   IS
      vcursor   default_division_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT org_code, org_name, reg_code, reg_name, brn_code, brn_name,
                div_code, div_name
           FROM tqc_divisions,
                tqc_brnch_divisions,
                tqc_branches,
                tqc_regions,
                tqc_organizations
          WHERE div_code = bdiv_div_code
            AND bdiv_brn_code = brn_code
            AND brn_reg_code = reg_code
            AND reg_org_code = org_code
            AND EXISTS (
                   SELECT usd_code, usd_usr_code, usd_div_code, usd_default
                     FROM tqc_user_divisions
                    WHERE tqc_divisions.div_code =
                                               tqc_user_divisions.usd_div_code
                      AND usd_default <> 'Y'
                      AND usd_usr_code = v_user_code);

      RETURN vcursor;
   END;

   FUNCTION get_system_users (v_system IN tqc_user_systems.usys_sys_code%TYPE)
      RETURN users_ref
   IS
      vcursor   users_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usr_code, usr_username, usr_name, usr_email,
                usr_personel_rank, usr_dt_created, usr_type, usr_status,
                usr_pwd_reset, usr_per_id, usr_acct_mgr, usr_cell_phone_no,
                usr_wef_dt, usr_wet_dt, NULL, NULL,usr_created_by, NULL, NULL
           FROM tqc_users, tqc_user_systems
          WHERE usys_sys_code = v_system
            AND usr_code = usys_usr_code
            AND (usys_wet IS NULL OR usys_wet >= SYSDATE);

      RETURN vcursor;
   END;

   FUNCTION get_system_processes (
      v_syscode   IN   tqc_sys_processes.sprc_sys_code%TYPE
   )
      RETURN sys_processes_ref
   IS
      vcursor   sys_processes_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sprc_code, sprc_process
           FROM tqc_sys_processes
          WHERE sprc_sys_code = v_syscode;

      RETURN vcursor;
   END;

   FUNCTION getorganizations
      RETURN organizations_ref
   IS
      vcursor   organizations_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT org_code, org_sht_desc, org_name
           FROM tqc_organizations;

      RETURN vcursor;
   END;

   FUNCTION get_user_orgs (v_usrcode IN tqc_user_branches.usb_usr_code%TYPE)
      RETURN organizations_ref
   IS
      vcursor   organizations_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT org_code, org_sht_desc, org_name
                    FROM tqc_organizations, tqc_systems, tqc_user_systems
                   WHERE org_code = sys_org_code
                     AND sys_code = usys_sys_code
                     AND usys_usr_code = v_usrcode;

      RETURN vcursor;
   END;

   FUNCTION getregions (v_orgcode IN tqc_regions.reg_org_code%TYPE)
      RETURN regions_ref
   IS
      vcursor   regions_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT reg_code, reg_org_code, reg_name
           FROM tqc_regions
          WHERE reg_org_code = v_orgcode;

      RETURN vcursor;
   END;

   FUNCTION getbranchesbyorg (v_orgcode IN tqc_organizations.org_code%TYPE)
      RETURN branches_ref
   IS
      vcursor   branches_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT brn_code, brn_sht_desc, brn_reg_code, brn_name
                    FROM tqc_branches, tqc_regions
                   WHERE reg_org_code = v_orgcode AND brn_reg_code = reg_code;

      RETURN vcursor;
   END;

   FUNCTION getbranches (v_regcode IN tqc_branches.brn_reg_code%TYPE)
      RETURN branches_ref
   IS
      vcursor   branches_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT brn_code, brn_sht_desc, brn_reg_code, brn_name
                    FROM tqc_branches
                   WHERE brn_reg_code = v_regcode;

      RETURN vcursor;
   END;

   FUNCTION getuserbranches (
      v_usrcode   IN   tqc_user_branches.usb_usr_code%TYPE,
      v_orgcode   IN   tqc_organizations.org_code%TYPE
   )
      RETURN usrbranches_ref
   IS
      vcursor   usrbranches_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usb_code, usb_brn_code, usb_usr_code, usb_status,
                usb_dflt_brn, brn_name
           FROM tqc_user_branches, tqc_branches, tqc_regions
          WHERE usb_brn_code = brn_code
            AND reg_org_code = v_orgcode
            AND usb_usr_code = v_usrcode
            AND brn_reg_code = reg_code;

      RETURN vcursor;
   END;

   FUNCTION get_user_branches (
      v_usrcode   IN   tqc_user_branches.usb_usr_code%TYPE
   )
      RETURN usrbranches_ref
   IS
      vcursor   usrbranches_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usb_code, usb_brn_code, usb_usr_code, usb_status,
                usb_dflt_brn, brn_name
           FROM tqc_user_branches, tqc_branches
          WHERE usb_brn_code = brn_code AND usb_usr_code = v_usrcode;

      --AND USB_BRN_CODE = v_brnCode;
      RETURN vcursor;
   END;

--   PROCEDURE create_user (
--      v_username              IN       tqc_users.usr_username%TYPE,
--      v_names                 IN       tqc_users.usr_name%TYPE,
--      v_password              IN       tqc_users.usr_pwd%TYPE,
--      v_email                 IN       tqc_users.usr_email%TYPE,
--      v_personel              IN       tqc_users.usr_per_rank_sht_desc%TYPE,
--      v_status                IN       tqc_users.usr_status%TYPE,
--      v_type                  IN       tqc_users.usr_type%TYPE,
--      v_reset                 IN       tqc_users.usr_pwd_reset%TYPE,
--      v_per_id                IN       tqc_users.usr_per_id%TYPE,
--      v_user_code             OUT      tqc_users.usr_code%TYPE,
--      v_usr_acct_mgr          IN       tqc_users.usr_acct_mgr%TYPE,
--      v_usr_signature         IN       tqc_users.usr_signature%TYPE,
--      v_usr_cell_phone_no     IN       tqc_users.usr_cell_phone_no%TYPE,
--      v_usr_wef_dt            IN       tqc_users.usr_wef_dt%TYPE ,
--      v_usr_wet_dt            IN       tqc_users.usr_wet_dt%TYPE ,
--      v_usr_secq_code         IN       tqc_users.usr_secq_code%TYPE,
--      v_usr_security_answer   IN       tqc_users.usr_security_answer%TYPE,
--      v_created_by            IN       tqc_users.usr_created_by%TYPE,
--      v_sact_code              IN      LMS_SUB_ACCOUNT_TYPES.SACT_CODE%TYPE      
--   )
--   IS
--      v_cnt   NUMBER;
--      v_agn_code    NUMBER := NULL;
--      v_id                           VARCHAR2(100);
--      v_brn_code                NUMBER;
--      v_physical_address     VARCHAR2(100);
--      v_postal_address       VARCHAR2(100);
--      v_fax                        VARCHAR2(100);
--      v_acc_no                  VARCHAR2(100);
--      v_pin                         VARCHAR2(100);
--      v_agent_commission   NUMBER;
--      v_region_code            NUMBER;
--      v_twn_name              VARCHAR2(100); 
--      v_cou_name              VARCHAR2(100); 
--      
--   BEGIN

---- raise_error(
----            'v_username: '||v_username||
----            'v_names: '||v_names ||
----            'v_password: '||v_password||
----            'v_email: '||v_email||
----            'v_usr_cell_phone_no: '||v_usr_cell_phone_no  
----        ); 
--      SELECT COUNT (1)
--        INTO v_cnt
--        FROM tqc_users
--       WHERE ( LTRIM (RTRIM (usr_cell_phone_no)) = LTRIM (RTRIM (v_usr_cell_phone_no)) 
--                                           AND v_usr_cell_phone_no IS NOT NULL)
--         AND usr_name <> v_username;

--      IF NVL (v_cnt, 0) > 0
--      THEN
--         raise_error ('Cell phone number has to be unique per user...');
--      END IF;
--      
--      SELECT tqc_usr_code_seq.NEXTVAL
--        INTO v_user_code
--        FROM DUAL;
-- 
--      INSERT INTO tqc_users
--                  (usr_code, usr_username, usr_name,
--                   usr_pwd, usr_email, usr_personel_rank, usr_per_id,
--                   usr_status, usr_type, usr_dt_created, usr_pwd_changed,
--                   usr_pwd_reset, usr_acct_mgr, usr_signature,
--                   usr_cell_phone_no, usr_wef_dt, usr_wet_dt,
--                   usr_secq_code, usr_security_answer,usr_created_by,
--                   usr_sact_code
--                  )
--           VALUES (v_user_code, v_username, v_names,
--                   DECODE(v_password,null,'',gis_read.val (v_password)), v_email, v_personel, v_per_id,
--                   v_status, v_type, SYSDATE, SYSDATE,
--                   v_reset, v_usr_acct_mgr, v_usr_signature,
--                   v_usr_cell_phone_no, v_usr_wef_dt, v_usr_wet_dt,
--                   v_usr_secq_code, v_usr_security_answer,v_created_by,
--                   v_sact_code
--                  );
--                
--        
--      IF v_sact_code IS NOT NULL
--      THEN
--           BEGIN
--                SELECT brn_code, brn_reg_code, twn_name, cou_name
--                    INTO v_brn_code, v_region_code, v_twn_name, v_cou_name
--                FROM tqc_branches, tqc_regions, tqc_organizations, tqc_countries, tqc_towns
--                WHERE brn_reg_code = reg_code
--                    AND reg_org_code = org_code
--                    AND reg_org_code = 1
--                    AND org_twn_code = twn_code
--                    AND org_cou_code = cou_code 
--                    AND brn_name like 'HEAD OFFICE%';
--           EXCEPTION
--                WHEN OTHERS THEN
--                    v_brn_code := NULL;
--           END; 
--           raise_error('v_twn_name: '||v_twn_name);
--           /*LMS_WEB_PKG_SETUP.create_agents (
--                                      'X',
--                                      v_names,
--                                      v_brn_code,
--                                      16,
--                                      v_physical_address,
--                                      v_postal_address,
--                                      v_email,
--                                      v_names,
--                                      v_usr_cell_phone_no,
--                                      v_usr_cell_phone_no,
--                                      v_fax,
--                                      v_acc_no,
--                                      v_pin,
--                                      v_agent_commission,
--                                      'ACTIVE',
--                                      v_region_code,
--                                      v_twn_name,
--                                      v_cou_name,
--                                      NULL,
--                                      NULL,
--                                      TRUNC(SYSDATE),
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      'Y',
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      'Y',
--                                      'NORM',
--                                      NULL,
--                                      'N',
--                                      v_agn_code,
--                                      NULL,
--                                      NULL,
--                                      NULL,
--                                      'N',
--                                      NULL,
--                                      NULL,
--                                      v_sact_code
--                                   );*/
--                                  
--           UPDATE tqc_users
--                 SET usr_agn_code = v_agn_code
--            WHERE usr_code = v_user_code;
--      END IF;
--   END create_user;


 PROCEDURE create_user (
      v_username              IN       tqc_users.usr_username%TYPE,
      v_names                 IN       tqc_users.usr_name%TYPE,
      v_password              IN       tqc_users.usr_pwd%TYPE,
      v_email                 IN       tqc_users.usr_email%TYPE,
      v_personel              IN       tqc_users.usr_per_rank_sht_desc%TYPE,
      v_status                IN       tqc_users.usr_status%TYPE,
      v_type                  IN       tqc_users.usr_type%TYPE,
      v_reset                 IN       tqc_users.usr_pwd_reset%TYPE,
      v_per_id                IN       tqc_users.usr_per_id%TYPE,
      v_user_code             OUT      tqc_users.usr_code%TYPE,
      v_usr_acct_mgr          IN       tqc_users.usr_acct_mgr%TYPE,
      v_usr_signature         IN       tqc_users.usr_signature%TYPE,
      v_usr_cell_phone_no     IN       tqc_users.usr_cell_phone_no%TYPE,
      v_usr_wef_dt            IN       tqc_users.usr_wef_dt%TYPE,
      v_usr_wet_dt            IN       tqc_users.usr_wet_dt%TYPE,
      v_usr_secq_code         IN       tqc_users.usr_secq_code%TYPE,
      v_usr_security_answer   IN       tqc_users.usr_security_answer%TYPE,
      v_created_by            IN       tqc_users.usr_created_by%TYPE,
      v_sact_code             IN       lms_sub_account_types.sact_code%TYPE,
      v_pin                   IN       tqc_users.usr_pin%TYPE,
      v_msg                   OUT   VARCHAR2
   )
   IS
      v_cnt                NUMBER;
      v_agn_code           NUMBER         := NULL;
      v_id                 VARCHAR2 (100);
      v_brn_code           NUMBER;
      v_physical_address   VARCHAR2 (100);
      v_postal_address     VARCHAR2 (100);
      v_fax                VARCHAR2 (100);
      v_acc_no             VARCHAR2 (100);
      v_agent_commission   NUMBER;
      v_region_code        NUMBER;
      v_twn_name           VARCHAR2 (100);
      v_cou_name           VARCHAR2 (100);
      retval               NUMBER;
      v_cc                 VARCHAR2 (200);
      v_subject            VARCHAR2 (200);
      v_message            VARCHAR2 (200);
      v_org_name           VARCHAR2 (200) := NULL; 
      v_new_pwd  VARCHAR2 (200) := NULL;
      v_act_code number :=16;
      v_admin_auto_create_pwd VARCHAR2 (20) := Tqc_Parameters_Pkg.Get_Param_Varchar2('ADMIN_AUTO_CREATE_PWD');
   BEGIN
   
   V_New_Pwd := v_password;
   IF NVL(v_admin_auto_create_pwd,'N')='Y' THEN
     BEGIN
      Select dbms_random.string('U', 1)||dbms_random.string('P', 6)||trunc(dbms_random.value(1,10)) Into 
       V_New_Pwd From dual;
       Select REPLACE (v_new_pwd, ' ', '$') 
       Into V_New_Pwd From Dual;
     END;
   END IF;
   
-- raise_error(
--            'v_username: '||v_username||
--            'v_names: '||v_names ||
--            'v_password: '||v_password||
--            'v_email: '||v_email||
--            'v_usr_cell_phone_no: '||v_usr_cell_phone_no
--        );
      SELECT COUNT (1)
        INTO v_cnt
        FROM tqc_users
       WHERE (    LTRIM (RTRIM (usr_cell_phone_no)) =
                                           LTRIM (RTRIM (v_usr_cell_phone_no))
              AND v_usr_cell_phone_no IS NOT NULL
             )
         AND usr_name <> v_username;

      IF NVL (v_cnt, 0) > 0
      THEN 
          v_msg:= 'Cell phone number has to be unique per user...';
          RETURN;
      END IF;

      IF v_usr_wef_dt > v_usr_wet_dt THEN
          v_msg:= 'WEF Date should be before WET Date !....';
          RETURN;
      END IF; 
      
    IF (v_pin IS NULL or LENGTH(v_pin)=0) AND 
      NVL (Tqc_Forms_Pkg.Field_Required ('USER_PIN'), 'N') = 'Y'
      THEN
         BEGIN
            v_msg:= 'User pin is required!';
            RETURN;
         END;
     END IF;
      begin
      SELECT tqc_usr_code_seq.NEXTVAL
        INTO v_user_code
        FROM DUAL;
      
      --  raise_error('v_per_id: '||v_per_id);
      INSERT INTO tqc_users
                  (usr_code, usr_username, usr_name,
                   usr_pwd,
                   usr_email, usr_personel_rank, usr_per_id, usr_status,
                   usr_type, usr_dt_created, usr_pwd_changed, usr_pwd_reset,
                   usr_acct_mgr, usr_signature, usr_cell_phone_no,
                   usr_wef_dt, usr_wet_dt, usr_secq_code,
                   usr_security_answer, usr_created_by, usr_sact_code,usr_pin
                  )
           VALUES (v_user_code, v_username, v_names,
                   DECODE (V_New_Pwd, NULL, '', gis_read.val (V_New_Pwd)),
                   v_email, v_personel, v_per_id, v_status,
                   v_type, SYSDATE, SYSDATE, v_reset,
                   v_usr_acct_mgr, v_usr_signature, v_usr_cell_phone_no,
                   v_usr_wef_dt, v_usr_wet_dt, v_usr_secq_code,
                   v_usr_security_answer, v_created_by, v_sact_code,v_pin
                  );
                  
        EXCEPTION
               WHEN OTHERS
               THEN 
                  v_msg:= 'Unable update user... ' || SQLERRM (SQLCODE);
                  RETURN; 
        END;
        
      IF v_sact_code IS NOT NULL
      THEN
         BEGIN
            SELECT brn_code, brn_reg_code, twn_name, cou_name
              INTO v_brn_code, v_region_code, v_twn_name, v_cou_name
              FROM tqc_branches,
                   tqc_regions,
                   tqc_organizations,
                   tqc_countries,
                   tqc_towns
             WHERE brn_reg_code = reg_code
               AND reg_org_code = org_code
               AND reg_org_code = 1
               AND org_twn_code = twn_code
               AND org_cou_code = cou_code
               AND brn_name LIKE 'HEAD OFFICE%';
         EXCEPTION
            WHEN OTHERS
            THEN
               v_brn_code := NULL;
         END;

         --   raise_error('v_twn_name: '||v_twn_name);
         /*lms_web_pkg_setup.create_agents ('X',
                                          v_names,
                                          v_brn_code,
                                          v_act_code,
                                          v_physical_address,
                                          v_postal_address,
                                          v_email,
                                          v_names,
                                          v_usr_cell_phone_no,
                                          v_usr_cell_phone_no,
                                          v_fax,
                                          v_acc_no,
                                          v_pin,
                                          v_agent_commission,
                                          'ACTIVE',
                                          v_region_code,
                                          v_twn_name,
                                          v_cou_name,
                                          NULL,
                                          NULL,
                                          TRUNC (SYSDATE),
                                          NULL,
                                          NULL,
                                          NULL,
                                          NULL,
                                          'Y',
                                          NULL,
                                          NULL,
                                          NULL,
                                          NULL,
                                          NULL,
                                          NULL,
                                          NULL,
                                          NULL,
                                          NULL,
                                          'Y',
                                          'NORM',
                                          NULL,
                                          'N',
                                          v_agn_code,
                                          NULL,
                                          NULL,
                                          NULL,
                                          'N',
                                          NULL,
                                          NULL,
                                          v_sact_code
                                         );
*/
         UPDATE tqc_users
            SET usr_agn_code = v_agn_code
          WHERE usr_code = v_user_code;
      END IF;

      IF v_email IS NOT NULL AND NVL(v_admin_auto_create_pwd,'N')='Y'
      THEN
         BEGIN
            BEGIN
               SELECT org_name
                 INTO v_org_name
                 FROM tqc_organizations
                WHERE org_code = 2;
            END;

            v_cc := NULL;
            v_subject :=
               'New Account Information: [ORG]: Registration Successful Password!';
            v_message :=
               'Hi: [FULLNAME], [ORG]: Account Registration is Successful.
              Account Details: Username:[USERNAME] and  Password: [PASSWORD]';
            v_subject := REPLACE (v_subject, '[ORG]', v_org_name);
            v_message := REPLACE (v_message, '[FULLNAME]', v_names);
            v_message := REPLACE (v_message, '[ORG]', v_org_name);
            v_message := REPLACE (v_message, '[PASSWORD]', V_New_Pwd);
            v_message := REPLACE (v_message, '[USERNAME]', v_username);

             --- raise_error('v_email: '||v_email||'v_subject: '||v_subject||' v_message: '||v_message);
            BEGIN
               retval :=
                  tqc_email_pkg.send_email (NULL,
                                            v_email,
                                            v_cc,
                                            v_subject,
                                            v_message
                                           );
            EXCEPTION
               WHEN OTHERS
               THEN 
                  v_msg:= 'Unable to send email' || SQLERRM (SQLCODE);
                  RETURN; 
            END;
         END;
      END IF;
       v_msg:= 'User Created successfully!'; 
   END;

  PROCEDURE update_user (
      v_user                  IN       tqc_users.usr_code%TYPE,
      v_username              IN       tqc_users.usr_username%TYPE,
      v_names                 IN       tqc_users.usr_name%TYPE,
      v_email                 IN       tqc_users.usr_email%TYPE,
      v_personel              IN       tqc_users.usr_per_rank_sht_desc%TYPE,
      v_type                  IN       tqc_users.usr_type%TYPE,
      v_reset                 IN       tqc_users.usr_pwd_reset%TYPE,
      v_per_id                IN       tqc_users.usr_per_id%TYPE,
      v_status                IN       tqc_users.usr_status%TYPE,
      v_usr_acct_mgr          IN       tqc_users.usr_acct_mgr%TYPE,
      v_usr_cell_phone_no     IN       tqc_users.usr_cell_phone_no%TYPE,
      v_usr_wef_dt            IN       tqc_users.usr_wef_dt%TYPE,
      v_usr_wet_dt            IN       tqc_users.usr_wet_dt%TYPE,
      v_usr_secq_code         IN       tqc_users.usr_secq_code%TYPE,
      v_usr_security_answer   IN       tqc_users.usr_security_answer%TYPE,
      v_sact_code             IN       lms_sub_account_types.sact_code%TYPE
            DEFAULT NULL,
      v_posted_by             IN       VARCHAR DEFAULT NULL,
      v_pin                   IN       VARCHAR,
      v_msg                   OUT      VARCHAR
   )
   IS
      v_cnt   NUMBER;
      v_curr_status  varchar(100) := NULL;
   BEGIN
      ---raise_error ('v_posted_by: '||v_posted_by);
      SELECT COUNT (*)
        INTO v_cnt
        FROM tqc_users
       WHERE LTRIM (RTRIM (usr_cell_phone_no)) =
                                           LTRIM (RTRIM (v_usr_cell_phone_no))
         AND usr_username <> v_username
         AND usr_cell_phone_no IS NOT NULL;

      IF NVL (v_cnt, 0) > 0
      THEN
         v_msg := 'Cell phone number has to be unique per user!';
         RETURN;
      END IF;

      IF v_usr_wef_dt > v_usr_wet_dt
      THEN
         v_msg := 'WEF Date should be before WET Date !';
         RETURN;
      END IF;

      IF     (v_pin IS NULL OR LENGTH (v_pin) = 0)
         AND NVL (tqc_forms_pkg.field_required ('USER_PIN'), 'N') = 'Y'
      THEN
         BEGIN
            v_msg := 'User pin is required!';
            RETURN;
         END;
      END IF;
      
      SELECT usr_status
        INTO v_curr_status
        FROM tqc_users
       WHERE usr_code =v_user;
       
       
       
      IF NVL (v_curr_status, 'N')  = 'A' AND NVL (v_status, 'N')  = 'I' --deactivate account!
      THEN
      --raise_error ('v_posted_by: '||v_posted_by);
         UPDATE tqc_users
         SET  
             usr_deactivated_on = SYSDATE,   
             usr_deactivated_by = v_posted_by
       WHERE usr_code = v_user;
      END IF;
      
      IF NVL (v_curr_status, 'N')  = 'I' AND NVL (v_status, 'N')  = 'A'  --deactivate account!
      THEN
         UPDATE tqc_users
         SET 
             usr_activated_on = SYSDATE,  
             usr_activated_by = v_posted_by
       WHERE usr_code = v_user;
      END IF;

      BEGIN
         UPDATE tqc_users
            SET usr_username = v_username,
                usr_name = v_names,
                usr_email = NVL (v_email, usr_email),
                usr_personel_rank = NVL (v_personel, usr_personel_rank),
                usr_per_id = NVL (v_per_id, usr_per_id),
                usr_type = NVL (v_type, usr_type),
                usr_pwd_reset = NVL (v_reset, usr_pwd_reset),
                usr_status = NVL (v_status, usr_status),
                usr_login_attempts = 0,
                usr_acct_mgr = NVL (v_usr_acct_mgr, usr_acct_mgr),
                usr_cell_phone_no = v_usr_cell_phone_no,
                usr_wef_dt = v_usr_wef_dt,
                usr_wet_dt = v_usr_wet_dt,
                usr_secq_code = v_usr_secq_code,
                usr_security_answer = v_usr_security_answer,
                usr_sact_code = v_sact_code,
                usr_authorized = 'N',
                usr_updated_by = v_posted_by,
                usr_updated_date = SYSDATE,
                usr_pin = v_pin
          WHERE usr_code = v_user;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_msg := 'Error unable to update user!' || SQLERRM;
            RETURN;
      END;

      IF v_sact_code IS NOT NULL
      THEN
         UPDATE lms_agencies
            SET agn_sact_code = v_sact_code
          WHERE agn_code IN (SELECT usr_agn_code
                               FROM tqc_users
                              WHERE usr_code = v_user);
      END IF;

      v_msg := 'User updated successfully!';
   END update_user;
   
   PROCEDURE drop_user (v_user IN tqc_users.usr_code%TYPE)
   IS
      v_username   tqc_users.usr_username%TYPE;
   BEGIN
      SELECT usr_username
        INTO v_username
        FROM tqc_users
       WHERE usr_code = v_user;

      IF (UPPER (v_username) = 'ADMIN')
      THEN
         raise_error ('ERROR: YOU CANNOT DROP ADMINISTRATOR ACCCOUNT');
      ELSE
         UPDATE tqc_users
            SET usr_status = 'I'
          WHERE usr_code = v_user;
      END IF;
   END drop_user;

   PROCEDURE restore_user (v_user IN tqc_users.usr_code%TYPE)
   IS
   BEGIN
      UPDATE tqc_users
         SET usr_status = 'A'
       WHERE usr_code = v_user;
   END restore_user;

   PROCEDURE grant_user_system (
      v_user       IN   tqc_users.usr_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   )
   IS
      v_count   NUMBER;
      v_maker_chaker         VARCHAR2(1):=NULL;
   BEGIN
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_user_systems
       WHERE usys_sys_code = v_sys_code
         AND usys_usr_code = v_user
         AND (usys_wet IS NULL OR usys_wet > SYSDATE);

      IF (NVL (v_count, 0) = 0)
      THEN
         INSERT INTO tqc_user_systems
                     (usys_code, usys_usr_code, usys_sys_code,
                      usys_wef, usys_wet
                     )
              VALUES (tqc_usys_code_seq.NEXTVAL, v_user, v_sys_code,
                      SYSDATE, NULL
                     );
         BEGIN            
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN
             UPDATE tqc_users
             SET usr_updated_by = v_updated_by,
                 usr_updated_date = sysdate,
                 usr_authorized ='N'
             WHERE
                 usr_code = v_user;
         END IF;  
      END IF;
   END grant_user_system;
   PROCEDURE grant_user_system (
      v_user       IN   tqc_users.usr_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_user_systems
       WHERE usys_sys_code = v_sys_code
         AND usys_usr_code = v_user
         AND (usys_wet IS NULL OR usys_wet > SYSDATE);

      IF (NVL (v_count, 0) = 0)
      THEN
         INSERT INTO tqc_user_systems
                     (usys_code, usys_usr_code, usys_sys_code,
                      usys_wef, usys_wet
                     )
              VALUES (tqc_usys_code_seq.NEXTVAL, v_user, v_sys_code,
                      SYSDATE, NULL
                     ); 
      END IF;
   END grant_user_system;

   PROCEDURE revoke_user_system (
      v_user       IN   tqc_users.usr_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   )
   IS
   v_maker_chaker         VARCHAR2(1):=NULL;
   BEGIN
      DELETE FROM tqc_user_systems
            WHERE usys_usr_code = v_user
              AND usys_sys_code = v_sys_code
              AND usys_wet IS NULL;
              
         BEGIN            
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN      
              UPDATE tqc_users
                 SET usr_updated_by = v_updated_by,
                     usr_updated_date = sysdate,
                 usr_authorized ='N'
                 WHERE
                     usr_code = v_user;
         END IF;    
   END revoke_user_system;

   PROCEDURE revoke_user_role (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_user       IN   tqc_users.usr_code%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   )
   IS
      v_maker_chaker         VARCHAR2(1):=NULL;
      v_log_user_code        NUMBER;
   BEGIN
    DELETE FROM TQC_SYS_USER_ROLES
    WHERE USRR_USR_CODE = v_user
    AND USRR_SRLS_CODE = v_role;
    
         BEGIN            
          v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN
             UPDATE tqc_users
             SET usr_updated_by = v_updated_by,
                 usr_updated_date = sysdate,
                 usr_authorized ='N'
             WHERE
                 usr_code = v_user;
         END IF; 
         
        BEGIN
        SELECT usr_code 
        INTO v_log_user_code 
        FROM tqc_users 
        WHERE usr_username=v_updated_by;
      
      
        INSERT INTO tqc_user_system_roles_logs
                     (usrll_usr_code, usrll_srl_code, usrll_grant_date,
                      usrll_revoke_date, usrll_grant_by_usr_code,
                      usrll_revoke_by_usr_code
                     )
        VALUES (v_user, v_role, NULL,
                      SYSDATE,
                      NULL, v_log_user_code
                     );  
      END;
    
    /*UPDATE  TQC_SYS_USER_ROLES SET USRR_REVOKE_DATE = SYSDATE,USRR_REVOKE_BY_USR_CODE = v_userCode 
    WHERE USRR_USR_CODE = v_user
    AND USRR_SRLS_CODE = v_role;*/
   END revoke_user_role;

   PROCEDURE grant_user_role (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_user       IN   tqc_users.usr_code%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   )
   IS
      v_count           NUMBER;
      v_maker_chaker    VARCHAR2(1):=NULL;
      v_log_user_code    NUMBER;
   BEGIN
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_sys_user_roles
       WHERE usrr_usr_code = v_user AND usrr_srls_code = v_role;
       
         BEGIN            
          v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN
             UPDATE tqc_users
             SET usr_updated_by = v_updated_by,
                 usr_updated_date = sysdate,
                 usr_authorized ='N'
             WHERE
                 usr_code = v_user;
         END IF; 

      IF (NVL (v_count, 0) = 0)
      THEN
         INSERT INTO tqc_sys_user_roles
                     (usrr_usr_code, usrr_srls_code, usrr_grant_date,
                      usrr_revoke_date, usrr_grant_by_usr_code,
                      usrr_revoke_by_usr_code
                     )
              VALUES (v_user, v_role, SYSDATE,
                      NULL, v_usercode,
                      NULL
                     );
      ELSE
         UPDATE tqc_sys_user_roles
            SET usrr_revoke_date = NULL,
                usrr_revoke_by_usr_code = NULL
          WHERE usrr_usr_code = v_user AND usrr_srls_code = v_role;
      END IF;
      
      BEGIN
        SELECT usr_code 
        INTO v_log_user_code 
        FROM tqc_users 
        WHERE usr_username=v_updated_by;
      
      
      INSERT INTO tqc_user_system_roles_logs
                     (usrll_usr_code, usrll_srl_code, usrll_grant_date,
                      usrll_revoke_date, usrll_grant_by_usr_code,
                      usrll_revoke_by_usr_code
                     )
              VALUES (v_user, v_role, SYSDATE,
                      NULL, v_log_user_code,
                      NULL
                     );  
      END;
                     
   END grant_user_role;

   PROCEDURE grant_role_process_area (
      v_processarea   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_rolecode      IN   tqc_sys_roles_processes.srprc_srls_code%TYPE
   )
   IS
      CURSOR cur_prcss
      IS
         SELECT *
           FROM tqc_sys_process_areas
          WHERE sprca_code = v_processarea;

      CURSOR subarea_cur
      IS
         SELECT sprsa_code
           FROM tqc_sys_process_areas, tqc_sys_process_sub_areas
          WHERE sprsa_sprca_code = sprca_code AND sprca_code = v_processarea;

      v_srprc_code        tqc_sys_roles_processes.srprc_code%TYPE;
      v_srprc_sprc_code   tqc_sys_roles_processes.srprc_sprc_code%TYPE;
      v_roleareacode      tqc_sys_roles_prcs_area.srpra_code%TYPE;
      v_count             NUMBER;
      v_process_granted   BOOLEAN                                     := TRUE;
   BEGIN
   
      BEGIN 
           UPDATE tqc_sys_roles
                 SET srls_authorized = 'N'
           WHERE srls_code = v_rolecode;
      EXCEPTION
           WHEN OTHERS THEN
           raise_error('Error determining is process has been granted .. '||SQLERRM);
      END; 
      
      FOR cur_prcss_rec IN cur_prcss
      LOOP
         BEGIN
            SELECT srprc_code, srprc_sprc_code
              INTO v_srprc_code, v_srprc_sprc_code
              FROM tqc_sys_roles_processes
             WHERE srprc_srls_code = v_rolecode
               AND srprc_sprc_code = cur_prcss_rec.sprca_sprc_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_process_granted := FALSE;
            WHEN OTHERS
            THEN
               raise_error ('Error determining is process has been granted..');
         END;

         IF NOT v_process_granted
         THEN
            BEGIN
               grantprocessrole (cur_prcss_rec.sprca_sprc_code,
                                 v_rolecode,
                                 'Y'
                                );

               SELECT srprc_code, srprc_sprc_code
                 INTO v_srprc_code, v_srprc_sprc_code
                 FROM tqc_sys_roles_processes
                WHERE srprc_srls_code = v_rolecode
                  AND srprc_sprc_code = cur_prcss_rec.sprca_sprc_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error
                            (   'Error granting process... cur_prcss_rec is '
                             || cur_prcss_rec.sprca_sprc_code
                             || ' : RoleCode is '
                             || v_rolecode
                            );
            END;
         END IF;

         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM tqc_sys_roles_processes, tqc_sys_roles_prcs_area
             WHERE srprc_code = srpra_srprc_code
               AND srpra_sprca_code = cur_prcss_rec.sprca_code
               AND srprc_srls_code = v_rolecode;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error
                  ('Error determining if process areas has already been granted'
                  );
         END;

         IF (NVL (v_count, 0) = 0)
         THEN
            DBMS_OUTPUT.put_line ('INSERT');

            SELECT tqc_srpra_seq.NEXTVAL
              INTO v_roleareacode
              FROM DUAL;

            INSERT INTO tqc_sys_roles_prcs_area
                        (srpra_code, srpra_srprc_code, srpra_sprca_code
                        )
                 VALUES (v_roleareacode, v_srprc_code, v_processarea
                        );
         END IF;

         FOR subarea_rec IN subarea_cur
         LOOP
            DBMS_OUTPUT.put_line (subarea_rec.sprsa_code || '=' || v_rolecode);
            grant_role_process_subarea (subarea_rec.sprsa_code,
                                        v_rolecode,
                                        NULL,
                                        NULL
                                       );
         -- INSERT INTO TQC_SYS_ROLES_PRCS_S_AREA (SRPSA_CODE, SRPSA_SRPRA_CODE, SRPSA_SPRSA_CODE, SRPSA_DEBIT_LIMIT, SRPSA_CREDIT_LIMIT)
          --VALUES(TQC_SRPSA_SEQ.NEXTVAL,v_roleAreaCode,subArea_rec.SPRSA_CODE,NULL,NULL);
         END LOOP;
      END LOOP;
   END grant_role_process_area;

   PROCEDURE revoke_role_process_area (
      v_processarea   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_role_code     IN   tqc_sys_roles.srls_code%TYPE
   )
   IS
   v_count NUMBER;
   v_srprc_sprc_code NUMBER;
   BEGIN
      BEGIN 
           UPDATE tqc_sys_roles
                 SET srls_authorized = 'N'
           WHERE srls_code = v_role_code;
      EXCEPTION
           WHEN OTHERS THEN
           raise_error('Error determining is process has been granted .. '||SQLERRM);
      END;
      DELETE FROM tqc_sys_roles_prcs_s_area
            WHERE srpsa_srpra_code =
                     (SELECT srpra_code
                        FROM tqc_sys_roles_processes,
                             tqc_sys_roles_prcs_area
                       WHERE srprc_code = srpra_srprc_code
                         --AND srprc_code = v_role_code
                         AND srprc_srls_code= v_role_code
                         AND srpra_sprca_code = v_processarea);
                         
     
                         
      DELETE FROM tqc_sys_roles_prcs_area
            WHERE srpra_code =
                     (SELECT srpra_code
                        FROM tqc_sys_roles_processes, tqc_sys_roles_prcs_area
                       WHERE srprc_code = srpra_srprc_code
                         --AND srprc_code = v_role_code
                         AND srprc_srls_code= v_role_code
                         AND srpra_sprca_code = v_processarea); 

    
   END revoke_role_process_area;
   

   PROCEDURE grant_role_process_subarea (
      v_processsubarea   IN   tqc_sys_process_sub_areas.sprsa_code%TYPE,
      v_rolecode         IN   tqc_sys_roles_processes.srprc_srls_code%TYPE,
      v_debitlimit       IN   tqc_sys_roles_prcs_s_area.srpsa_debit_limit%TYPE,
      v_creditlimit      IN   tqc_sys_roles_prcs_s_area.srpsa_credit_limit%TYPE
   )
   IS
      v_sprca_code           tqc_sys_process_areas.sprca_code%TYPE;
      v_srpra_code           tqc_sys_roles_prcs_area.srpra_code%TYPE;
      v_sprsa_code           tqc_sys_process_sub_areas.sprsa_code%TYPE;
      v_count                NUMBER;
      v_process_granted      BOOLEAN                                  := TRUE;
      v_prcss_area_granted   BOOLEAN                                  := TRUE;
      v_cnt                  NUMBER                                      := 0;
   BEGIN
      -- RAISE_ERROR(v_debitLimit ||';'||v_creditLimit); 
      BEGIN 
           UPDATE tqc_sys_roles
                 SET srls_authorized = 'N'
           WHERE srls_code = v_rolecode;
      EXCEPTION
           WHEN OTHERS THEN
           raise_error('Error determining is process has been granted .. '||SQLERRM);
      END; 
      
      BEGIN
         --RAISE_ERROR(v_processsubArea ||';'||v_roleCode);
         SELECT sprca_code, sprsa_code
           INTO v_sprca_code, v_sprsa_code
           FROM tqc_sys_processes,
                tqc_sys_roles_processes,
                tqc_sys_process_areas,
                tqc_sys_process_sub_areas
          WHERE sprc_code = sprca_sprc_code
            AND sprc_code = srprc_sprc_code
            AND sprca_code = sprsa_sprca_code
            AND sprsa_code = v_processsubarea
            AND srprc_srls_code = v_rolecode;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_process_granted := FALSE;
         WHEN OTHERS
         THEN
            raise_error ('Error determining if process has been granted..');
      END;

      IF NOT v_process_granted
      THEN
         DBMS_OUTPUT.put_line ('GRANTING');

         BEGIN
            SELECT sprca_code, sprsa_code
              INTO v_sprca_code, v_sprsa_code
              FROM tqc_sys_processes,
                   tqc_sys_process_areas,
                   tqc_sys_process_sub_areas
             WHERE sprc_code = sprca_sprc_code
               AND sprca_code = sprsa_sprca_code
               AND sprsa_code = v_processsubarea;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error ('33');
            WHEN OTHERS
            THEN
               raise_error ('Error determining is process has been granted..');
         END;

         DBMS_OUTPUT.put_line ('v_sprca_code=' || v_sprca_code);

         BEGIN
            grant_role_process_area (v_sprca_code, v_rolecode);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error granting process RoleCode is '
                            || v_rolecode
                            || ' :: sprcaCode is '
                            || v_sprca_code
                           );
         END;
      END IF;

      BEGIN
         --RAISE_ERROR(v_roleCode ||';'||v_sprca_code);
         SELECT srpra_code
           INTO v_srpra_code
           FROM tqc_sys_roles_processes, tqc_sys_roles_prcs_area
          WHERE srprc_code = srpra_srprc_code
            AND srprc_srls_code = v_rolecode
            AND srpra_sprca_code = v_sprca_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_prcss_area_granted := FALSE;
         WHEN OTHERS
         THEN
            raise_error
                       (   'Error getting process and process area details..'
                        || v_rolecode
                        || ' = '
                        || v_sprca_code
                       );
      END;

      IF NOT v_prcss_area_granted
      THEN
         BEGIN
            grant_role_process_area (v_sprca_code, v_rolecode);

            SELECT srpra_code
              INTO v_srpra_code
              FROM tqc_sys_roles_processes, tqc_sys_roles_prcs_area
             WHERE srprc_code = srpra_srprc_code
               AND srprc_srls_code = v_rolecode
               AND srpra_sprca_code = v_sprca_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error granting process Area');
         END;
      END IF;

      -- raise_error('v_srpra_code='||v_srpra_code);
      BEGIN
         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_sys_roles_prcs_s_area
          WHERE srpsa_srpra_code = v_srpra_code
            AND srpsa_sprsa_code = v_sprsa_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error getting if role already granted..');
      END;

      IF NVL (v_cnt, 0) = 0
      THEN
         BEGIN
            INSERT INTO tqc_sys_roles_prcs_s_area
                        (srpsa_code, srpsa_srpra_code, srpsa_sprsa_code,
                         srpsa_debit_limit, srpsa_credit_limit
                        )
                 VALUES (tqc_srpsa_seq.NEXTVAL, v_srpra_code, v_sprsa_code,
                         v_debitlimit, v_creditlimit
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error inserting sub area..');
         END;
      ELSE
         BEGIN
            UPDATE tqc_sys_roles_prcs_s_area
               SET srpsa_debit_limit = v_debitlimit,
                   srpsa_credit_limit = v_creditlimit
             WHERE srpsa_srpra_code = v_srpra_code
               AND srpsa_sprsa_code = v_sprsa_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error updating role..');
         END;
      END IF;
   END grant_role_process_subarea;

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
   PROCEDURE revoke_role_process_subarea (
      v_processsubarea   IN   tqc_sys_process_sub_areas.sprsa_code%TYPE,
      v_role_code        IN   tqc_sys_roles.srls_code%TYPE
   )
   IS
   BEGIN
--RAISE_ERROR(v_processsubArea ||';'||v_role_code);
     BEGIN 
           UPDATE tqc_sys_roles
                 SET srls_authorized = 'N'
           WHERE srls_code = v_role_code;
      EXCEPTION
           WHEN OTHERS THEN
           raise_error('Error determining is process has been granted .. '||SQLERRM);
      END;
      DELETE      tqc_sys_roles_prcs_s_area a
            WHERE a.srpsa_code =
                     (SELECT b.srpsa_code
                        FROM tqc_sys_roles_processes,
                             tqc_sys_roles_prcs_area,
                             tqc_sys_roles_prcs_s_area b
                       WHERE srprc_code = srpra_srprc_code
                         AND srpra_code = b.srpsa_srpra_code
                         AND b.srpsa_sprsa_code = v_processsubarea
                         AND srprc_srls_code = v_role_code);
   END revoke_role_process_subarea;

   PROCEDURE creat_role (
      v_syscode         IN   tqc_sys_roles.srls_sys_code%TYPE,
      v_role_name       IN   tqc_sys_roles.srls_name%TYPE,
      v_role_sht_desc   IN   tqc_sys_roles.srls_sht_desc%TYPE,
      v_user            IN   varchar2 DEFAULT NULL
   )
   IS
      v_rolecode   tqc_sys_roles.srls_code%TYPE;
   BEGIN
      --raise_error('v_sysCode= '||v_sysCode);
      SELECT tqc_srls_seq.NEXTVAL
        INTO v_rolecode
        FROM DUAL;

      INSERT INTO tqc_sys_roles
                  (srls_code, srls_sys_code, srls_name, srls_crt_date,
                   srls_sht_desc, srls_status,srls_created_by
                  )
           VALUES (v_rolecode, v_syscode, upper(v_role_name), SYSDATE,
                   upper(v_role_sht_desc), 'I',v_user
                  );
   --Commented out by Gitau. 040110 to allow multiple processes on a role.
   --INSERT INTO TQC_SYS_ROLES_PROCESSES (SRPRC_CODE, SRPRC_SRLS_CODE, SRPRC_SPRC_CODE)
   --VALUES(TQC_SRPRC_SEQ.NEXTVAL,v_roleCode,v_Process);
   END creat_role;

   PROCEDURE edit_role (
      v_role_code     IN     tqc_sys_roles.srls_code%TYPE,
      v_role_name       IN   tqc_sys_roles.srls_name%TYPE,
      v_role_sht_desc   IN   tqc_sys_roles.srls_sht_desc%TYPE,
      v_status          IN   tqc_sys_roles.srls_status%TYPE,
      v_posted_by       IN   VARCHAR2
   )
   IS 
      v_created_by           VARCHAR2(100) := NULL;
      v_maker_chaker         VARCHAR2(1) := NULL;
   BEGIN
         /*SELECT SRPRC_SRLS_CODE INTO v_roleCode
      FROM TQC_SYS_ROLES_PROCESSES
      WHERE SRPRC_CODE = v_roleProcess;*/

      -- Assume the v_roleProcess is the same as v_roleCode
      
--       BEGIN
--         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('ROLE_MAKER_CHECKER_APP');
--        END;
--        
--          
--       BEGIN 
--           SELECT srls_created_by
--            INTO v_created_by
--            FROM tqc_sys_roles
--           WHERE ( srls_created_by IS NOT NULL)
--             AND srls_code =v_role_code;
--           EXCEPTION WHEN OTHERS
--           THEN 
--           v_created_by:=NULL;
--      END;     
--   
--     
--      IF v_created_by IS NULL
--      THEN
--           raise_error('Role not found or was created by unknown user!'); 
--      END IF;
--      
--      IF NVL(v_maker_chaker,'N')='Y' AND NVL(v_posted_by,'***')=NVL(v_created_by,'###')
--      THEN
--           raise_error( 'The same user cannot create and update Role!'); 
--      END IF;
      
        
      UPDATE tqc_sys_roles
         SET srls_name = v_role_name,
             srls_sht_desc = v_role_sht_desc,
             srls_status = v_status,
             srls_authorized ='N',
             srls_created_by = v_posted_by
       WHERE srls_code = v_role_code;
   END edit_role;

   PROCEDURE restore_role (
      v_roleprocess   IN   tqc_sys_roles_processes.srprc_code%TYPE
   )
   IS
      v_rolecode   tqc_sys_roles.srls_code%TYPE;
   BEGIN
      SELECT srprc_srls_code
        INTO v_rolecode
        FROM tqc_sys_roles_processes
       WHERE srprc_code = v_roleprocess;

      UPDATE tqc_sys_roles
         SET srls_status = 'A'
       WHERE srls_code = v_rolecode;
   END restore_role;

   PROCEDURE drop_role (
      v_roleprocess   IN   tqc_sys_roles_processes.srprc_code%TYPE
   )
   IS
      v_rolecode   tqc_sys_roles.srls_code%TYPE;
   BEGIN
      /*SELECT SRPRC_SRLS_CODE INTO v_roleCode FROM TQC_SYS_ROLES_PROCESSES
      WHERE SRPRC_CODE = v_roleProcess;*/

      -- Assume the v_roleProcess is the same as v_roleCode
      UPDATE tqc_sys_roles
         SET srls_status = 'I'
       WHERE srls_code = v_roleprocess;
   END drop_role;

   PROCEDURE edit_user_syswet (
      v_syscode   IN   tqc_user_systems.usys_code%TYPE,
      v_wet       IN   tqc_user_systems.usys_wet%TYPE
   )
   IS
   BEGIN
      UPDATE tqc_user_systems
         SET usys_wet = v_wet
       WHERE usys_code = v_syscode;
   END edit_user_syswet;

   PROCEDURE grant_user_branch (
      v_user          IN   tqc_user_branches.usb_usr_code%TYPE,
      v_branch_code   IN   tqc_user_branches.usb_brn_code%TYPE,
      v_updated_by    IN   tqc_users.usr_username%TYPE
      
   )
   IS
      v_count      NUMBER;
      v_usb_code   NUMBER;
      v_maker_chaker         VARCHAR2(1):=NULL;
   BEGIN
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_user_branches
       WHERE usb_brn_code = v_branch_code AND usb_usr_code = v_user;

      IF (NVL (v_count, 0) = 0)
      THEN
         SELECT NVL (MAX (usb_code), 0) + 1
           INTO v_usb_code
           FROM tqc_user_branches;

         INSERT INTO tqc_user_branches
                     (usb_code, usb_brn_code, usb_usr_code, usb_status,
                      usb_dflt_brn
                     )
              VALUES (v_usb_code, v_branch_code, v_user, 'A',
                      'N'
                     );
         BEGIN            
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN
             UPDATE tqc_users
             SET usr_updated_by = v_updated_by,
                 usr_updated_date = sysdate,
                 usr_authorized ='N'
             WHERE
                 usr_code = v_user;
         END IF; 
      END IF;
   END grant_user_branch;

   PROCEDURE revoke_user_branch (
      v_user          IN   tqc_user_branches.usb_usr_code%TYPE,
      v_branch_code   IN   tqc_user_branches.usb_brn_code%TYPE,
      v_updated_by    IN   tqc_users.usr_username%TYPE
   )
   IS
   v_maker_chaker         VARCHAR2(1):=NULL;
   BEGIN
      DELETE FROM tqc_user_branches
            WHERE usb_brn_code = v_branch_code AND usb_usr_code = v_user;
            
         BEGIN            
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN
             UPDATE tqc_users
             SET usr_updated_by = v_updated_by,
                 usr_updated_date = sysdate,
                 usr_authorized ='N'
             WHERE
                 usr_code = v_user;
         END IF; 
   END revoke_user_branch;

   PROCEDURE enabledisablebranch (
      v_usrbranchcode   IN   tqc_user_branches.usb_code%TYPE
   )
   IS
      v_status   VARCHAR2 (1);
   BEGIN
      SELECT usb_status
        INTO v_status
        FROM tqc_user_branches
       WHERE usb_code = v_usrbranchcode;

      IF NVL (v_status, 'A') = 'A'
      THEN
         UPDATE tqc_user_branches
            SET usb_status = 'I'
          WHERE usb_code = v_usrbranchcode;
      ELSE
         UPDATE tqc_user_branches
            SET usb_status = 'A'
          WHERE usb_code = v_usrbranchcode;
      END IF;
   END enabledisablebranch;

   PROCEDURE makedefaultbranch (
      v_usrbranchcode   IN   tqc_user_branches.usb_code%TYPE
   )
   IS
      v_default    NUMBER;
      v_regcode    NUMBER;
      v_usercode   NUMBER;
   BEGIN
      SELECT usb_usr_code, brn_reg_code
        INTO v_usercode, v_regcode
        FROM tqc_user_branches, tqc_branches
       WHERE usb_brn_code = brn_code AND usb_code = v_usrbranchcode;

      BEGIN
         SELECT usb_code
           INTO v_default
           FROM tqc_user_branches, tqc_branches
          WHERE usb_brn_code = brn_code
            AND brn_reg_code = v_regcode
            AND usb_usr_code = v_usercode
            AND usb_dflt_brn = 'Y';

         UPDATE tqc_user_branches
            SET usb_dflt_brn = 'N'
          WHERE usb_code = v_default;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      IF v_default = v_usrbranchcode
      THEN
         UPDATE tqc_user_branches
            SET usb_dflt_brn = 'N'
          WHERE usb_code = v_usrbranchcode;
      ELSE
         UPDATE tqc_user_branches
            SET usb_dflt_brn = 'Y'
          WHERE usb_code = v_usrbranchcode;
      END IF;
   END makedefaultbranch;

   PROCEDURE assign_user_default_branch (
      v_user_code     IN   tqc_user_branches.usb_usr_code%TYPE,
      v_branch_code   IN   tqc_user_branches.usb_brn_code%TYPE
   )
   IS
   BEGIN
      -- Lets first unselect the previous default branch(es)
      UPDATE tqc_user_branches
         SET usb_dflt_brn = 'N'
       WHERE usb_usr_code = v_user_code;

      -- Now, lets assign the new default branch
      UPDATE tqc_user_branches
         SET usb_dflt_brn = 'Y'
       WHERE usb_usr_code = v_user_code AND usb_brn_code = v_branch_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error occured while updating record '
                      || SQLERRM (SQLCODE)
                     );
   END assign_user_default_branch;

   PROCEDURE assign_user_default_division (
      v_user_code       IN   tqc_user_divisions.usd_usr_code%TYPE,
      v_division_code   IN   tqc_user_divisions.usd_div_code%TYPE
   )
   IS
   BEGIN
      -- Lets first unselect the pervious default division(s)
      UPDATE tqc_user_divisions
         SET usd_default = 'N'
       WHERE usd_usr_code = v_user_code;

      -- Now, lets assign the new default division
      UPDATE tqc_user_divisions
         SET usd_default = 'Y'
       WHERE usd_usr_code = v_user_code AND usd_div_code = v_division_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error occured while updating record '
                      || SQLERRM (SQLCODE)
                     );
   END assign_user_default_division;

   PROCEDURE grantprocessrole (
      v_processcode    IN   tqc_sys_processes.sprc_code%TYPE,
      v_rolecode       IN   tqc_sys_roles.srls_code%TYPE,
      v_process_only   IN   VARCHAR2 DEFAULT 'N'
   )
   IS
      v_processrolecode   tqc_sys_roles_processes.srprc_code%TYPE;

      CURSOR cur_prcss
      IS
         SELECT *
           FROM tqc_sys_processes
          WHERE sprc_code = v_processcode
            AND sprc_code NOT IN (SELECT srprc_sprc_code
                                    FROM tqc_sys_roles_processes
                                   WHERE srprc_srls_code = v_rolecode);

      CURSOR area_cur
      IS
         SELECT sprca_code
           FROM tqc_sys_process_areas
          WHERE sprca_sprc_code = v_processcode;
   BEGIN
-- raise_error(v_processCode||'='||v_roleCode||'='||v_process_only);
      FOR cur_prcss_rec IN cur_prcss
      LOOP
         SELECT tqc_srprc_seq.NEXTVAL
           INTO v_processrolecode
           FROM DUAL;

         BEGIN
            INSERT INTO tqc_sys_roles_processes
                        (srprc_code, srprc_srls_code,
                         srprc_sprc_code
                        )
                 VALUES (v_processrolecode, v_rolecode,
                         cur_prcss_rec.sprc_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error assigning process to the role..'
                            || v_processcode
                            || '='
                            || v_rolecode
                            || '='
                            || v_process_only
                           );
         END;
      END LOOP;

      IF NVL (v_process_only, 'N') != 'Y'
      THEN
         FOR prc_area IN area_cur
         LOOP
            grant_role_process_area (prc_area.sprca_code, v_rolecode);
         END LOOP;
      END IF;
   END grantprocessrole;

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
   PROCEDURE revokeprocessrole (
      v_processcode   IN   tqc_sys_roles_processes.srprc_sprc_code%TYPE,
      v_rolecode      IN   tqc_sys_roles_processes.srprc_srls_code%TYPE
   )
   IS
      CURSOR area_cur
      IS
         SELECT sprca_code
           FROM tqc_sys_process_areas
          WHERE sprca_sprc_code = v_processcode;

      v_processrolecode   tqc_sys_roles_processes.srprc_code%TYPE;
   BEGIN
      BEGIN
         --raise_error(v_processCode ||';'||v_roleCode);
         SELECT srprc_code
           INTO v_processrolecode
           FROM tqc_sys_roles_processes
          WHERE srprc_sprc_code = v_processcode
            AND srprc_srls_code = v_rolecode;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      FOR prc_area IN area_cur
      LOOP
         revoke_role_process_area (prc_area.sprca_code, v_processrolecode);
      END LOOP;

      DELETE FROM tqc_sys_roles_processes
            WHERE srprc_sprc_code = v_processcode
              AND srprc_srls_code = v_rolecode;
   END revokeprocessrole;

   FUNCTION getalluserassignedsystems (v_user_code NUMBER)
      RETURN userassignedsystems_ref
   IS
      v_cursor   userassignedsystems_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   sys_code, sys_sht_desc, sys_name, sys_active, usys_code,
                  usys_usr_code, usys_sys_code, usys_wef, usys_wet,
                  usys_spost_code, sys_post_name (usys_spost_code)
                                                                  spost_desc
             FROM tqc_user_systems, tqc_systems
            WHERE tqc_user_systems.usys_sys_code = tqc_systems.sys_code
              AND sys_active <> 'N'
              AND usys_usr_code = v_user_code
         ORDER BY sys_name;

      RETURN v_cursor;
   END;

   FUNCTION sys_post_name (v_spost_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (50);
   BEGIN
      SELECT spost_desc
        INTO v_ret_val
        FROM tqc_sys_posts
       WHERE spost_code = v_spost_code;

      RETURN v_ret_val;
   END sys_post_name;

   FUNCTION getregionsfiltered (
      v_orgcode     IN   tqc_regions.reg_org_code%TYPE,
      v_user_code        tqc_user_branches.usb_usr_code%TYPE
   )
      RETURN regions_ref
   IS
      vcursor   regions_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT reg_code, reg_org_code, reg_name
           FROM tqc_regions
          WHERE reg_org_code = v_orgcode
            AND reg_code IN (
                   SELECT reg_code
                     FROM (SELECT   COUNT (1), reg_code, reg_org_code
                               FROM tqc_regions, tqc_branches
                              WHERE reg_code = brn_reg_code
                                AND reg_org_code = v_orgcode
                                AND brn_code NOT IN (
                                              SELECT usb_brn_code
                                                FROM tqc_user_branches
                                               WHERE usb_usr_code =
                                                                   v_user_code)
                           GROUP BY reg_code, reg_org_code
                             HAVING COUNT (1) >= 1));

      RETURN vcursor;
   END;

   PROCEDURE grant_user_branchbyorg (
      v_user       IN   tqc_user_branches.usb_usr_code%TYPE,
      v_org_code   IN   tqc_regions.reg_org_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   )
   IS
      v_count      NUMBER;
      v_usb_code   NUMBER;
      v_maker_chaker         VARCHAR2(1):=NULL;
      CURSOR branches
      IS
         SELECT brn_code, brn_name
           FROM tqc_branches, tqc_regions
          WHERE brn_reg_code = reg_code AND reg_org_code = v_org_code;
   BEGIN
   
         BEGIN            
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN
             UPDATE tqc_users
             SET usr_updated_by = v_updated_by,
                 usr_updated_date = sysdate,
                 usr_authorized ='N'
             WHERE
                 usr_code = v_user;
         END IF; 
   
      FOR c IN branches
      LOOP
         SELECT COUNT (*)
           INTO v_count
           FROM tqc_user_branches
          WHERE usb_brn_code = c.brn_code AND usb_usr_code = v_user;

         IF (NVL (v_count, 0) = 0)
         THEN
            SELECT NVL (MAX (usb_code), 0) + 1
              INTO v_usb_code
              FROM tqc_user_branches;

            INSERT INTO tqc_user_branches
                        (usb_code, usb_brn_code, usb_usr_code, usb_status,
                         usb_dflt_brn
                        )
                 VALUES (v_usb_code, c.brn_code, v_user, 'A',
                         'N'
                        );
         END IF;
      END LOOP;
   END grant_user_branchbyorg;

   PROCEDURE revoke_user_branchbyorg (
      v_user       IN       tqc_user_branches.usb_usr_code%TYPE,
      v_org_code   IN       tqc_regions.reg_org_code%TYPE,
      v_err        OUT      VARCHAR2,
      v_updated_by IN   tqc_users.usr_username%TYPE
   )
   IS
      v_count      NUMBER;
      v_usb_code   NUMBER;
      v_def_brn    NUMBER;
      v_maker_chaker         VARCHAR2(1):=NULL;
      CURSOR branches
      IS
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name
           FROM tqc_branches, tqc_regions
          WHERE reg_org_code = v_org_code
            AND brn_reg_code = reg_code
            AND EXISTS (
                   SELECT usb_code, usb_brn_code, usb_usr_code, usb_status,
                          usb_dflt_brn
                     FROM tqc_user_branches
                    WHERE tqc_branches.brn_code =
                                                tqc_user_branches.usb_brn_code
                      AND usb_usr_code = v_user);
   BEGIN
   
         BEGIN            
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN
             UPDATE tqc_users
             SET usr_updated_by = v_updated_by,
                 usr_updated_date = sysdate,
                 usr_authorized ='N'
             WHERE
                 usr_code = v_user;
         END IF;
   
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_user_branches
       WHERE usb_dflt_brn = 'Y' AND usb_usr_code = v_user;

      IF (v_count > 0)
      THEN
         BEGIN
            SELECT NVL (usb_brn_code, -9999)
              INTO v_def_brn
              FROM tqc_user_branches
             WHERE usb_dflt_brn = 'Y' AND usb_usr_code = v_user;

            FOR c IN branches
            LOOP
               IF (NVL (v_def_brn, -9999) = c.brn_code)
               THEN
                  BEGIN
                     v_err := 'DEFAULT BRANCH NOT REMOVED';
                  END;
               ELSE
                  BEGIN
                     DELETE FROM tqc_user_branches
                           WHERE usb_brn_code = c.brn_code
                             AND usb_usr_code = v_user;
                  END;
               END IF;
            END LOOP;
         END;
      ELSE
         BEGIN
            FOR c IN branches
            LOOP
               DELETE FROM tqc_user_branches
                     WHERE usb_brn_code = c.brn_code
                           AND usb_usr_code = v_user;
            END LOOP;
         END;
      END IF;
   END revoke_user_branchbyorg;

   FUNCTION get_user_orgsfiltered (
      v_usrcode   IN   tqc_user_branches.usb_usr_code%TYPE
   )
      RETURN organizations_ref
   IS
      vcursor   organizations_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT DISTINCT org_code, org_sht_desc, org_name
                    FROM tqc_organizations, tqc_systems, tqc_user_systems
                   WHERE org_code = sys_org_code
                     AND sys_code = usys_sys_code
                     AND usys_usr_code = v_usrcode
                     AND org_code IN (
                            SELECT reg_org_code
                              FROM tqc_regions
                             WHERE reg_org_code = org_code
                               AND reg_code IN (
                                      SELECT reg_code
                                        FROM (SELECT   COUNT (1), reg_code,
                                                       reg_org_code
                                                  FROM tqc_regions,
                                                       tqc_branches
                                                 WHERE reg_code = brn_reg_code
                                                   AND reg_org_code =
                                                                  reg_org_code
                                                   AND brn_code NOT IN (
                                                          SELECT usb_brn_code
                                                            FROM tqc_user_branches
                                                           WHERE usb_usr_code =
                                                                     v_usrcode)
                                              GROUP BY reg_code, reg_org_code
                                                HAVING COUNT (1) >= 1)));

      RETURN vcursor;
   END;

   PROCEDURE grant_user_allroleinsys (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_user       IN   tqc_users.usr_code%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   )
   IS
      v_count   NUMBER;
      v_maker_chaker         VARCHAR2(1):=NULL;
      v_log_user_code        NUMBER;

      CURSOR usr_roles
      IS
         SELECT srls_code, srls_sys_code, srls_name, srls_status
           FROM tqc_sys_roles
          WHERE srls_status <> 'N'
            AND srls_sys_code = v_sys_code
            AND NOT EXISTS (
                   SELECT usrr_usr_code, usrr_srls_code
                     FROM tqc_sys_user_roles
                    WHERE tqc_sys_roles.srls_code =
                                             tqc_sys_user_roles.usrr_srls_code
                      AND usrr_usr_code = v_user);
   BEGIN
         BEGIN            
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN
             UPDATE tqc_users
             SET usr_updated_by = v_updated_by,
                 usr_updated_date = sysdate,
                 usr_authorized ='N'
             WHERE
                 usr_code = v_usercode;
         END IF; 
         
         BEGIN
            SELECT usr_code 
            INTO v_log_user_code 
            FROM tqc_users 
            WHERE usr_username=v_updated_by;
         END;
         
--RAISE_ERROR('v_user='||v_user);
      FOR c IN usr_roles
      LOOP
         INSERT INTO tqc_sys_user_roles
                     (usrr_usr_code, usrr_srls_code, usrr_grant_date,
                      usrr_revoke_date, usrr_grant_by_usr_code,
                      usrr_revoke_by_usr_code
                     )
              VALUES (v_user, c.srls_code, SYSDATE,
                      NULL, v_usercode,
                      NULL
                     );
          BEGIN
            INSERT INTO tqc_user_system_roles_logs
                     (usrll_usr_code, usrll_srl_code, usrll_grant_date,
                      usrll_revoke_date, usrll_grant_by_usr_code,
                      usrll_revoke_by_usr_code
                     )
            VALUES (v_user, c.srls_code, SYSDATE,
                      NULL, v_log_user_code,
                      NULL
                     );  
          END;    
      END LOOP;
   END grant_user_allroleinsys;

   PROCEDURE revoke_user_allroleinsys (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_user       IN   tqc_users.usr_code%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE,
      v_updated_by IN   tqc_users.usr_username%TYPE
   )
   IS
   v_maker_chaker         VARCHAR2(1):=NULL;
   v_log_user_code        NUMBER;
      CURSOR usr_roles
      IS
         SELECT srls_code, srls_sys_code, srls_name, srls_status
           FROM tqc_sys_roles
          WHERE srls_status <> 'I'
            AND srls_sys_code = v_sys_code
            AND EXISTS (
                   SELECT usrr_usr_code, usrr_srls_code
                     FROM tqc_sys_user_roles
                    WHERE tqc_sys_roles.srls_code =
                                             tqc_sys_user_roles.usrr_srls_code
                      AND usrr_usr_code = v_user);
   BEGIN 
        BEGIN            
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
         END;
         
         IF NVL(v_maker_chaker,'N')='Y' AND v_updated_by IS NOT NULL
         THEN
            
             UPDATE tqc_users
             SET usr_updated_by = v_updated_by,
                 usr_updated_date = sysdate,
                 usr_authorized ='N'
             WHERE
                 usr_code = v_usercode;
         END IF; 
         
         BEGIN
            SELECT usr_code 
            INTO v_log_user_code 
            FROM tqc_users 
            WHERE usr_username=v_updated_by;
         END;
   
      FOR c IN usr_roles
      LOOP
         DELETE FROM tqc_sys_user_roles
               WHERE usrr_usr_code = v_user AND usrr_srls_code = c.srls_code;
               
          BEGIN
            INSERT INTO tqc_user_system_roles_logs
                     (usrll_usr_code, usrll_srl_code, usrll_grant_date,
                      usrll_revoke_date, usrll_grant_by_usr_code,
                      usrll_revoke_by_usr_code
                     )
            VALUES (v_user, c.srls_code, NULL,
                      SYSDATE,NULL,v_log_user_code
                     );  
          END;
      END LOOP;
   END revoke_user_allroleinsys;
   
   PROCEDURE wet_user_allroleinsys (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_wet        IN   tqc_sys_user_roles.usrr_revoke_date%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE
   )
   IS
   
   BEGIN
      
     UPDATE tqc_sys_user_roles SET usrr_revoke_date = v_wet
     WHERE usrr_usr_code = v_usercode AND usrr_srls_code = v_role;
     
   END wet_user_allroleinsys;
   
   
PROCEDURE wef_user_allroleinsys (
      v_usercode   IN   tqc_users.usr_code%TYPE,
      v_wef        IN   tqc_sys_user_roles.usrr_grant_date%TYPE,
      v_role       IN   tqc_sys_roles.srls_code%TYPE,
      v_sys_code   IN   tqc_systems.sys_code%TYPE
   )
   IS
   
   BEGIN
      
     UPDATE tqc_sys_user_roles SET usrr_grant_date = v_wef
     WHERE usrr_usr_code = v_usercode AND usrr_srls_code = v_role;
     
   END wef_user_allroleinsys;
   FUNCTION get_assignedsystems_withrole (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN user_unassigned_systems_ref
   IS
      vcursor   user_unassigned_systems_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_active
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND EXISTS (
                   SELECT usys_code, usys_usr_code, usys_sys_code
                     FROM tqc_user_systems
                    WHERE tqc_systems.sys_code =
                                                tqc_user_systems.usys_sys_code
                      AND usys_usr_code = v_user_code)
            AND sys_code IN (
                   SELECT sys_code
                     FROM (SELECT   srls_sys_code AS sys_code, COUNT (*)
                               FROM tqc_sys_roles
                              WHERE srls_status NOT IN ('N', 'I')
                                -- AND SRLS_SYS_CODE = SYS_CODE
                                AND EXISTS (
                                       SELECT usrr_usr_code, usrr_srls_code
                                         FROM tqc_sys_user_roles
                                        WHERE tqc_sys_roles.srls_code =
                                                 tqc_sys_user_roles.usrr_srls_code
                                          AND usrr_usr_code = v_user_code)
                           GROUP BY srls_sys_code
                             HAVING COUNT (*) >= 1));

      RETURN vcursor;
   END;

   FUNCTION get_unassignedsystems_withrole (
      v_user_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN user_unassigned_systems_ref
   IS
      vcursor   user_unassigned_systems_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_active
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND EXISTS (
                   SELECT usys_code, usys_usr_code, usys_sys_code
                     FROM tqc_user_systems
                    WHERE tqc_systems.sys_code =
                                                tqc_user_systems.usys_sys_code
                      AND usys_usr_code = v_user_code)
            AND sys_code IN (
                   SELECT sys_code
                     FROM (SELECT   srls_sys_code AS sys_code, COUNT (*)
                               FROM tqc_sys_roles
                              WHERE srls_status NOT IN ('N', 'I')
                                -- AND SRLS_SYS_CODE = SYS_CODE
                                AND NOT EXISTS (
                                       SELECT usrr_usr_code, usrr_srls_code
                                         FROM tqc_sys_user_roles
                                        WHERE tqc_sys_roles.srls_code =
                                                 tqc_sys_user_roles.usrr_srls_code
                                          AND usrr_usr_code = v_user_code)
                           GROUP BY srls_sys_code
                             HAVING COUNT (*) >= 1));

      RETURN vcursor;
   END;

   PROCEDURE get_usersnotingrp (v_users_ref OUT users_ref, v_grp_code IN NUMBER)
   IS
   BEGIN
      --Raise_error('grp_code-->'||v_grp_code );
      OPEN v_users_ref FOR
         SELECT   usr_code, usr_username, usr_name, usr_email,
                  usr_personel_rank, usr_dt_created, usr_type, usr_status,
                  usr_pwd_reset, usr_per_id, usr_acct_mgr, usr_cell_phone_no,
                  usr_wef_dt, usr_wet_dt, NULL, NULL,usr_created_by, NULL, NULL
             FROM tqc_users
            WHERE usr_code NOT IN (SELECT gusr_usr_code
                                     FROM tqc_group_users
                                    WHERE gusr_grp_usr_code = v_grp_code)
              AND usr_type != 'G'
         ORDER BY usr_code DESC;
   END get_usersnotingrp;

   PROCEDURE getfulluserdetails (
      v_username      IN       tqc_users.usr_username%TYPE,
      v_userdetails   OUT      users_ref
   )
   IS
   BEGIN
      OPEN v_userdetails FOR
         SELECT usr_code, usr_username, usr_name, usr_email,
                usr_personel_rank, usr_dt_created, usr_type, usr_status,
                usr_pwd_reset, usr_per_id, usr_acct_mgr, usr_cell_phone_no,
                usr_wef_dt, usr_wet_dt, NULL, NULL,usr_created_by, NULL, NULL
           FROM tqc_users
          WHERE usr_username = v_username;
   END getfulluserdetails;

   PROCEDURE create_system_administrator (v_user VARCHAR2)
   IS
      v_success_status    VARCHAR2 (10 BYTE);
      v_role_code         NUMBER;
      v_admin_user_code   NUMBER;
      v_msg varchar2(20);

      CURSOR processes_cursor
      IS
         SELECT *
           FROM tqc_sys_processes
          WHERE sprc_sys_code = 0;

      CURSOR users_cur
      IS
         SELECT usr_username
           FROM tqc_users;

      v_count             NUMBER;
   BEGIN
      --CREATE ADMINISTRATOR USER ACCOUNT
      BEGIN
         SELECT COUNT (usr_code)
           INTO v_count
           FROM tqc_users
          WHERE UPPER (usr_username) = v_user;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error
               ('ERROR DETERMINING IF  THE ADMINISTRATOR ACCOUNT ALREADY EXITS'
               );
      END;

      IF v_count = 0
      THEN
         BEGIN
            create_user (
                         'ADMIN',
                         'SYSTEM ADMINISTRATOR',
                         'ADMINISTRATOR',
                         NULL,
                         NULL,
                         'A',
                         'U',
                         'Y',
                         NULL,
                         v_admin_user_code,
                         null,
                         null,
                         null,
                         null,
                         null,
                         null,
                         null,
                         null,
                         null,
                         null,
                         v_msg
                         
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'ERROR CREATING THE ADMINISTRATOR ACCOUNT'
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSE
         BEGIN
            SELECT usr_code
              INTO v_admin_user_code
              FROM tqc_users
             WHERE UPPER (usr_username) = v_user;
         -- UpDate_User(v_admin_user_code,'ADMIN','SYSTEM ADMINISTRATOR',NULL,NULL,'U',NULL,NULL);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error
                         ('ERROR DETERMINING  EXISTING ADMINISTRATOR ACCOUNT');
         END;
      END IF;

      --GRANT ADMININISTRATOR ACCESS TO CRM
      BEGIN
         grant_user_system (v_admin_user_code, 0);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error
                       ('ERROR GRANTING SYSTEMS ACCESS TO THE ADMINISTRATOR ');
      END;

      --CHECK IF AN ADMINISTRATOR ROLE  ALREADY EXISTS
      BEGIN
         SELECT srls_code
           INTO v_role_code
           FROM tqc_sys_roles
          WHERE srls_sys_code = 0 AND UPPER (srls_name) = 'ADMINISTRATOR';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      IF v_role_code IS NULL
      THEN
         BEGIN
            creat_role (0, 'ADMINISTRATOR', 'ADMIN');

            SELECT srls_code
              INTO v_role_code
              FROM tqc_sys_roles
             WHERE srls_sys_code = 0 AND UPPER (srls_name) = 'ADMINISTRATOR';

            INSERT INTO tqc_sys_user_roles
                        (usrr_usr_code, usrr_srls_code, usrr_grant_date,
                         usrr_revoke_date, usrr_grant_by_usr_code,
                         usrr_revoke_by_usr_code
                        )
                 VALUES (v_admin_user_code, v_role_code, SYSDATE,
                         NULL, v_admin_user_code,
                         NULL
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR CREATING ADMINISTRATOR ROLE ');
         END;
      ELSE
         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM tqc_sys_user_roles
             WHERE usrr_usr_code = v_admin_user_code
               AND usrr_srls_code = v_role_code;

            IF (NVL (v_count, 0) = 0)
            THEN
               INSERT INTO tqc_sys_user_roles
                           (usrr_usr_code, usrr_srls_code, usrr_grant_date,
                            usrr_revoke_date, usrr_grant_by_usr_code,
                            usrr_revoke_by_usr_code
                           )
                    VALUES (v_admin_user_code, v_role_code, SYSDATE,
                            NULL, v_admin_user_code,
                            NULL
                           );
            ELSE
               UPDATE tqc_sys_user_roles
                  SET usrr_revoke_date = NULL,
                      usrr_revoke_by_usr_code = NULL
                WHERE usrr_usr_code = v_admin_user_code
                  AND usrr_srls_code = v_role_code;
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error
                  (   'ERROR GRANTING ADMINISTRATOR ROLE TO ADMINISTRATOR USER '
                   || v_role_code
                  );
         END;
      END IF;

      BEGIN
         FOR process_rec IN processes_cursor
         LOOP
            grantprocessrole (process_rec.sprc_code, v_role_code);
         END LOOP;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('ERROR GRANTING PROCESSES TO ADMINISTRATOR ROLE ');
      END;

      COMMIT;
   END create_system_administrator;

   FUNCTION get_usremail (v_user_name VARCHAR2)
      RETURN VARCHAR2
   IS
      v_email   VARCHAR2 (50);
   BEGIN
      SELECT usr_email
        INTO v_email
        FROM tqc_users
       WHERE usr_username = v_user_name;

      RETURN v_email;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION get_usremailaddr (v_user_code NUMBER)
      RETURN VARCHAR2
   IS
      v_email   VARCHAR2 (50);
   BEGIN
      SELECT usr_email
        INTO v_email
        FROM tqc_users
       WHERE usr_code = v_user_code;

      RETURN v_email;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION getallsecurityquestions
      RETURN securityquestions_ref
   IS
      v_cursor   securityquestions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT secq_code, secq_sht_desc, secq_desc
           FROM tqc_security_questions;

      RETURN v_cursor;
   END;

   PROCEDURE update_security_questions (
      v_action          IN   VARCHAR2,
      v_secq_code       IN   NUMBER,
      v_secq_sht_desc   IN   VARCHAR2,
      v_secq_desc       IN   VARCHAR2
   )
   IS
   BEGIN
      IF NVL (v_action, 'A') = 'A'
      THEN
         INSERT INTO tqc_security_questions
                     (secq_code, secq_sht_desc, secq_desc
                     )
              VALUES (tqc_secq_code_seq.NEXTVAL, v_secq_sht_desc, v_secq_desc
                     );
      ELSIF NVL (v_action, 'A') = 'E'
      THEN
         UPDATE tqc_security_questions
            SET secq_sht_desc = v_secq_sht_desc,
                secq_desc = v_secq_desc
          WHERE secq_code = v_secq_code;
      ELSIF NVL (v_action, 'A') = 'D'
      THEN
         DELETE      tqc_security_questions
               WHERE secq_code = v_secq_code;
      END IF;
   END;
   
  PROCEDURE create_missing_prc_s_areas (
      v_sys_code          TQC_SYSTEMS.SYS_CODE%TYPE,
      v_sprc_sht_desc     TQC_SYS_PROCESSES.SPRC_SHT_DESC%TYPE,
      v_sprc_process      TQC_SYS_PROCESSES.SPRC_PROCESS%TYPE,
      v_sprca_sht_desc    TQC_SYS_PROCESS_AREAS.SPRCA_SHT_DESC%TYPE,
      v_sprca_area        TQC_SYS_PROCESS_AREAS.SPRCA_AREA%TYPE,
      v_sprsa_sht_desc    TQC_SYS_PROCESS_SUB_AREAS.SPRsA_SHT_DESC%TYPE,
      v_sprsa_sub_area    TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SUB_AREA%TYPE,
      v_sprsa_type        TQC_SYS_PROCESS_SUB_AREAS.SPRSA_TYPE%TYPE)
   IS
      v_sprc_code    TQC_SYS_PROCESSES.sprc_code%TYPE;
      v_sprca_code   TQC_SYS_PROCESS_AREAS.sprca_code%TYPE;
      v_sprsa_code   TQC_SYS_PROCESS_SUB_AREAS.sprsa_code%TYPE;
   BEGIN
--      raise_error (
--      'v_sys_code: '||v_sys_code||
--      'v_sprc_sht_desc: '||v_sprc_sht_desc||
--      'v_sprc_process: '||v_sprc_process||
--      'v_sprca_sht_desc: '||v_sprca_sht_desc ||
--      'v_sprca_area: '||v_sprca_area||
--      'v_sprsa_sht_desc: '||v_sprsa_sht_desc||
--      'v_sprsa_sub_area: '||v_sprsa_sub_area||
--      'v_sprsa_type: '||v_sprsa_type
--      );
   
      BEGIN
         SELECT SPRC_CODE
           INTO v_sprc_code
           FROM TQC_SYS_PROCESSES
          WHERE SPRC_SYS_CODE = v_sys_code
                AND SPRC_SHT_DESC = v_sprc_sht_desc;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_sprc_code := NULL;
         WHEN OTHERS
         THEN
           raise_error ('Error unable fetch process!');
      END;

      IF v_sprc_code IS NULL
      THEN
        BEGIN
          SELECT TQC_SPRC_SEQ.NEXTVAL 
           INTO v_sprc_code 
          FROM DUAL;
         INSERT INTO TQC_SYS_PROCESSES (SPRC_CODE,
                                        SPRC_SYS_CODE,
                                        SPRC_PROCESS,
                                        SPRC_SHT_DESC)
              VALUES (v_sprc_code,
                      v_sys_code,
                      v_sprc_process,
                      v_sprc_sht_desc);
         EXCEPTION WHEN OTHERS THEN
           raise_error ('Error unable to create process!');
         END;          
      END IF;

      BEGIN
         SELECT SPRCA_CODE
           INTO v_sprca_code
           FROM TQC_SYS_PROCESS_AREAS
          WHERE SPRCA_SPRC_CODE = v_sprc_code
                AND SPRCA_SHT_DESC = v_sprca_sht_desc;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_sprca_code := NULL;
         WHEN OTHERS
         THEN
            raise_error ('Error');
      END;

      IF v_sprca_code IS NULL
      THEN
         SELECT TQC_SPRCA_SEQ.NEXTVAL 
           INTO v_sprca_code 
         FROM DUAL;

         INSERT INTO TQC_SYS_PROCESS_AREAS (SPRCA_CODE,
                                            SPRCA_SPRC_CODE,
                                            SPRCA_AREA,
                                            SPRCA_SHT_DESC)
              VALUES (v_sprca_code,
                      v_sprc_code,
                      v_sprca_area,
                      v_sprca_sht_desc);
      END IF;

      BEGIN
         SELECT SPRSA_CODE
            INTO v_sprsa_code
           FROM TQC_SYS_PROCESS_SUB_AREAS
          WHERE     SPRSA_SPRC_CODE = v_sprc_code
                AND SPRSA_SPRCA_CODE = v_sprca_code
                AND SPRSA_SHT_DESC = v_sprsa_sht_desc;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_sprsa_code := NULL;
         WHEN OTHERS
         THEN
            raise_error ('Error');
      END;
      
      IF v_sprsa_code IS NULL -- INSERT NEW
      THEN
        BEGIN
             SELECT TQC_SPRSA_SEQ.NEXTVAL 
              INTO v_sprsa_code 
             FROM DUAL;

          INSERT INTO TQC_SYS_PROCESS_SUB_AREAS (SPRSA_CODE,
                                                SPRSA_SPRCA_CODE,
                                                SPRSA_SPRC_CODE,
                                                SPRSA_SUB_AREA,
                                                SPRSA_TYPE,
                                                SPRSA_SHT_DESC)
          VALUES (v_sprsa_code,
                  v_sprca_code,
                  v_sprc_code,
                  v_sprsa_sub_area,
                  v_sprsa_type,
                  v_sprsa_sht_desc);
                  COMMIT;
          END;
       ELSE --UPDATE OLD
          BEGIN
   
             UPDATE TQC_SYS_PROCESS_SUB_AREAS 
              SET 
                    SPRSA_SPRCA_CODE=v_sprca_code,
                    SPRSA_SPRC_CODE=v_sprc_code,
                    SPRSA_SUB_AREA=v_sprsa_sub_area,
                    SPRSA_TYPE=v_sprsa_type,
                    SPRSA_SHT_DESC=v_sprsa_sht_desc
              WHERE 
                  SPRSA_CODE=v_sprsa_code;      
             COMMIT;
           END;
      END IF;
   END create_missing_prc_s_areas;
   

   PROCEDURE grant_process_sub_area (
      v_sys_code          TQC_SYSTEMS.SYS_CODE%TYPE,
      v_srls_sht_desc     TQC_SYS_ROLES.SRLS_SHT_DESC%TYPE,
      v_srls_name         TQC_SYS_ROLES.SRLS_NAME%TYPE,
      v_sprc_sht_desc     TQC_SYS_PROCESSES.SPRC_SHT_DESC%TYPE,
      v_sprca_sht_desc    TQC_SYS_PROCESS_AREAS.SPRCA_SHT_DESC%TYPE,
      v_sprsa_sht_desc    TQC_SYS_PROCESS_SUB_AREAS.SPRSA_SHT_DESC%TYPE,
      v_grant             VARCHAR2,
      v_debit_limit       TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_DEBIT_LIMIT%TYPE,
      v_credit_limit      TQC_SYS_ROLES_PRCS_S_AREA.SRPSA_CREDIT_LIMIT%TYPE)
   IS
      v_srls_code    NUMBER;
      v_sprc_code    NUMBER;
      v_sprca_code   NUMBER;
      v_sprsa_code   NUMBER;
      v_srprc_code   NUMBER;
      v_srpra_code   NUMBER;
      v_srpsa_code   NUMBER;
   BEGIN
      BEGIN
         SELECT SRLS_CODE
           INTO v_srls_code
           FROM TQC_SYS_ROLES
          WHERE SRLS_SYS_CODE = v_sys_code
                AND SRLS_SHT_DESC = v_srls_sht_desc;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_srls_code := NULL;
         WHEN OTHERS
         THEN
            RAISE_ERROR ('Error getting role details..');
      END;

      BEGIN
         SELECT SPRC_CODE, SPRCA_CODE, SPRSA_CODE
           INTO v_sprc_code, v_sprca_code, v_sprsa_code
           FROM TQC_SYS_PROCESSES,
                TQC_SYS_PROCESS_AREAS,
                TQC_SYS_PROCESS_SUB_AREAS
          WHERE     SPRC_CODE = SPRCA_SPRC_CODE
                AND SPRCA_CODE = SPRSA_SPRCA_CODE
                AND SPRC_SHT_DESC = v_sprc_sht_desc
                AND SPRCA_SHT_DESC = v_sprca_sht_desc
                AND SPRSA_SHT_DESC = v_sprsa_sht_desc;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            RAISE_ERROR (
                  'Process area not defined..'
               || v_sprc_sht_desc
               || '='
               || v_sprca_sht_desc
               || '='
               || v_sprsa_sht_desc);
         WHEN OTHERS
         THEN
            RAISE_ERROR (
                  'Error..'
               || v_sprc_sht_desc
               || '='
               || v_sprca_sht_desc
               || '='
               || v_sprsa_sht_desc);
      END;

      IF v_srls_code IS NULL AND v_grant = 'Y'
      THEN
         SELECT tqc_SRLS_SEQ.NEXTVAL INTO v_srls_code FROM DUAL;

         INSERT INTO TQC_SYS_ROLES (SRLS_CODE,
                                    SRLS_SYS_CODE,
                                    SRLS_NAME,
                                    SRLS_CRT_DATE,
                                    SRLS_SHT_DESC,
                                    SRLS_STATUS)
              VALUES (v_srls_code,
                      v_sys_code,
                      v_srls_name,
                      TRUNC (SYSDATE),
                      v_srls_sht_desc,
                      'A');
      END IF;

      BEGIN
         SELECT SRPRC_CODE
           INTO v_srprc_code
           FROM TQC_SYS_ROLES_PROCESSES
          WHERE SRPRC_SRLS_CODE = v_srls_code
                AND SRPRC_SPRC_CODE = v_sprc_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_srprc_code := NULL;
         WHEN OTHERS
         THEN
            RAISE_ERROR ('Error getting Process code');
      END;

      IF v_srprc_code IS NULL AND v_grant = 'Y'
      THEN
         SELECT TQC_SRPRC_SEQ.NEXTVAL INTO v_srprc_code FROM DUAL;

         INSERT
           INTO TQC_SYS_ROLES_PROCESSES (SRPRC_CODE,
                                         SRPRC_SRLS_CODE,
                                         SRPRC_SPRC_CODE)
         VALUES (v_srprc_code, v_srls_code, v_sprc_code);
      END IF;

      BEGIN
         SELECT SRPRA_CODE
           INTO v_srpra_code
           FROM TQC_SYS_ROLES_PRCS_AREA
          WHERE SRPRA_SRPRC_CODE = v_srprc_code
                AND SRPRA_SPRCA_CODE = v_sprca_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_srpra_code := NULL;
         WHEN OTHERS
         THEN
            RAISE_ERROR ('Error getting Process Area code');
      END;

      IF v_srpra_code IS NULL AND v_grant = 'Y'
      THEN
         SELECT TQC_SRPRA_SEQ.NEXTVAL INTO v_srpra_code FROM DUAL;

         INSERT
           INTO TQC_SYS_ROLES_PRCS_AREA (SRPRA_CODE,
                                         SRPRA_SRPRC_CODE,
                                         SRPRA_SPRCA_CODE)
         VALUES (v_srpra_code, v_srprc_code, v_sprca_code);
      END IF;



      BEGIN
         SELECT SRPSA_CODE
           INTO v_srpsa_code
           FROM TQC_SYS_ROLES_PRCS_S_AREA
          WHERE SRPSA_SRPRA_CODE = v_srpra_code
                AND SRPSA_SPRSA_CODE = v_sprsa_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_srpsa_code := NULL;
         WHEN OTHERS
         THEN
            RAISE_ERROR ('Error getting Process Sub Area code');
      END;

      IF v_srpsa_code IS NULL AND v_grant = 'Y'
      THEN
         SELECT TQC_SRPSA_SEQ.NEXTVAL INTO v_srpsa_code FROM DUAL;

         INSERT INTO TQC_SYS_ROLES_PRCS_S_AREA (SRPSA_CODE,
                                                SRPSA_SRPRA_CODE,
                                                SRPSA_SPRSA_CODE,
                                                SRPSA_DEBIT_LIMIT,
                                                SRPSA_CREDIT_LIMIT)
              VALUES (v_srpsa_code,
                      v_srpra_code,
                      v_sprsa_code,
                      v_debit_limit,
                      v_credit_limit);
      ELSIF v_srpsa_code IS NOT NULL AND v_grant = 'N'
      THEN
         DELETE TQC_SYS_ROLES_PRCS_S_AREA
          WHERE SRPSA_CODE = v_srpsa_code;
      END IF;
   END;
   
   PROCEDURE get_normal_users (
      v_users_ref   OUT      users_ref,
      v_date        IN       VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      -- raise_error('DATE---->'|| v_date );
      IF v_date IS NULL
      THEN
         OPEN v_users_ref FOR
            SELECT   usr_code, usr_username, usr_name, usr_email,
                     usr_personel_rank, usr_dt_created, usr_type, usr_status,
                     usr_pwd_reset, usr_per_id, usr_acct_mgr,
                     usr_cell_phone_no, usr_wef_dt, usr_wet_dt, secq_desc,
                     usr_security_answer,usr_created_by, sact_code, sact_description
                FROM tqc_users, tqc_security_questions, lms_sub_account_types
               WHERE usr_secq_code = secq_code(+)
                   AND USR_SACT_CODE = SACT_CODE(+)
                   AND  USR_TYPE ='U'
               --    AND  USR_STATUS='A'
            ORDER BY usr_code DESC;
      ELSE
         OPEN v_users_ref FOR
            SELECT   usr_code, usr_username, usr_name, usr_email,
                     usr_personel_rank, usr_dt_created, usr_type, usr_status,
                     usr_pwd_reset, usr_per_id, usr_acct_mgr,
                     usr_cell_phone_no, usr_wef_dt, usr_wet_dt, secq_desc,
                     usr_security_answer,usr_created_by, SACT_CODE, SACT_DESCRIPTION
                FROM tqc_users, tqc_security_questions, lms_sub_account_types
               WHERE usr_secq_code = secq_code(+)
                 AND USR_SACT_CODE = SACT_CODE(+)
                 AND  USR_TYPE ='U'
                -- AND  USR_STATUS='A'
                 AND usr_dt_created LIKE
                                   '%' || TO_DATE (v_date, 'dd/mm/yyyy')
                                   || '%'
            ORDER BY usr_code DESC;
      END IF;
   END get_normal_users;
   
    
   PROCEDURE revoke_role_process (
      v_srprc_code   IN   tqc_sys_roles_processes.SRPRC_CODE%TYPE
   )
   IS
 
   BEGIN
      
     DELETE FROM tqc_sys_roles_prcs_s_area
            WHERE srpsa_srpra_code IN
                     ( select srpra_code FROM tqc_sys_roles_prcs_area
                        WHERE srpra_srprc_code =v_srprc_code);                         
     
                         
      DELETE FROM tqc_sys_roles_prcs_area
            WHERE srpra_srprc_code =v_srprc_code;
            
                         
      DELETE FROM tqc_sys_roles_processes
            WHERE srprc_code =v_srprc_code;

    
   END revoke_role_process;
   
   PROCEDURE authorize_user (
      v_msg               OUT      VARCHAR2,
      v_posted_by         IN       tqc_users.usr_name%TYPE,
      v_user_code          IN       tqc_users.usr_name%TYPE    
   )
   IS 
      v_created_by           VARCHAR2(100):=NULL;
      v_maker_chaker         VARCHAR2(1):=NULL;
      v_updated_by           VARCHAR2(100):=NULL;
      
   BEGIN 
--        raise_error(
--            'v_posted_by:'||v_posted_by || 
--            'v_usr_code:'||v_user_code 
--        ); 
        BEGIN
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
        END;
        
        
       BEGIN 
           SELECT USR_CREATED_BY,USR_UPDATED_BY
            INTO v_created_by,v_updated_by
            FROM tqc_users
           WHERE ( USR_CREATED_BY IS NOT NULL)
             AND usr_code =v_user_code;
           EXCEPTION WHEN OTHERS
           THEN 
           v_created_by:=NULL;
           v_updated_by:=NULL;
      END;     
            
--       raise_error(
--            'v_posted_by:'||v_posted_by || 
--            'v_created_by:'||v_created_by 
--        );   
      BEGIN
          IF v_updated_by IS NOT NULL
          THEN
              IF NVL(v_maker_chaker,'N')='Y' AND NVL(v_posted_by,'***')=NVL(v_updated_by,'###')
              THEN
                   v_msg := 'The same user cannot update and authorize a user!';
                   RETURN;
              END IF;
          ELSIF v_created_by IS NOT NULL
          THEN
              IF NVL(v_maker_chaker,'N')='Y' AND NVL(v_posted_by,'***')=NVL(v_created_by,'###')
              THEN
                    v_msg := 'The same user cannot create and authorize user!';
                    RETURN;
              END IF;
          ELSIF v_created_by IS NULL
          THEN
              v_msg := 'User not found or was created by unknown user!';
              RETURN;
          END IF;
      END;

      BEGIN
           UPDATE tqc_users
             SET usr_authorized = 'Y',
                 usr_authorized_by=v_posted_by
        WHERE usr_code = v_user_code;
       EXCEPTION
            WHEN OTHERS THEN
               v_msg := SQLERRM;
               RETURN;
       END;  
      v_msg:='success';
   END authorize_user;
   
   PROCEDURE authorize_role (
      v_msg               OUT      VARCHAR2,
      v_posted_by         IN       VARCHAR2,
      v_role_code         IN       NUMBER    
   )
   IS 
      v_created_by           VARCHAR2(100):=NULL;
      v_maker_chaker         VARCHAR2(1):=NULL;
      
   BEGIN 
--        raise_error(
--            'v_posted_by:'||v_posted_by || 
--            'v_role_code:'||v_role_code 
--        ); 
        BEGIN
         v_maker_chaker:=tqc_parameters_pkg.get_param_varchar('ROLE_MAKER_CHECKER_APP');
        END;
        
        
       BEGIN 
           SELECT srls_created_by
            INTO v_created_by
            FROM tqc_sys_roles
           WHERE ( srls_created_by IS NOT NULL)
             AND srls_code =v_role_code;
           EXCEPTION WHEN OTHERS
           THEN 
           v_created_by:=NULL;
      END;     
   
     
      IF v_created_by IS NULL
      THEN
           v_msg := 'Role not found or was created by unknown user!';
           RETURN;
      END IF;
--      raise_error(
--            'v_created_by:'||v_created_by  
--        );        
--       raise_error(
--            'v_posted_by:'||v_posted_by || 
--            'v_created_by:'||v_created_by 
--        );   
      
      IF NVL(v_maker_chaker,'N')='Y' AND NVL(v_posted_by,'***')=NVL(v_created_by,'###')
      THEN
           v_msg := 'The same user cannot create and authorize Role!';
           RETURN;
      END IF;

      BEGIN 
           UPDATE tqc_sys_roles
                 SET srls_authorized = 'Y',
                     srls_authorized_by = v_posted_by
           WHERE srls_code = v_role_code;
      EXCEPTION
           WHEN OTHERS THEN
               v_msg := SQLERRM;
               RETURN;
      END;  
      v_msg := 'success';
   END authorize_role;
   FUNCTION getGisUsers
      RETURN users_ref
   IS
      vcursor   users_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usr_code, usr_username, usr_name, usr_email,
                usr_personel_rank, usr_dt_created, usr_type, usr_status,
                usr_pwd_reset, usr_per_id, usr_acct_mgr, usr_cell_phone_no,
                usr_wef_dt, usr_wet_dt, NULL, NULL,usr_created_by, NULL, NULL
           FROM tqc_users, tqc_user_systems
           WHERE usr_code = usys_usr_code
             AND usr_status = 'A'
             AND usys_sys_code = 37;

      RETURN vcursor;
   END;
   
   FUNCTION getGisDebtOwners( v_usr_name   IN   VARCHAR2 DEFAULT NULL)
      RETURN debt_owners_ref
   IS
      vcursor   debt_owners_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usr_code, usr_username, usr_name, usr_email,
                usr_personel_rank, NVL(usr_dt_created, TRUNC(SYSDATE)), usr_type,
                usr_status, usr_pwd_reset
           FROM tqc_users, tqc_user_systems
           WHERE usr_code = usys_usr_code
             AND usr_status = 'A'
             AND usys_sys_code = 37
             AND (usr_name LIKE '%' || v_usr_name || '%' OR usr_username LIKE '%' || v_usr_name || '%');

      RETURN vcursor;
   END;
   FUNCTION getGisDtOwners( v_usr_name   IN   VARCHAR2 DEFAULT NULL)
      RETURN dt_owners_ref
   IS
      vcursor   dt_owners_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usr_code, usr_name, usr_username, usr_email,
                usr_personel_rank, NVL(usr_dt_created, TRUNC(SYSDATE)), usr_type,
                usr_status, usr_pwd_reset
           FROM tqc_users, tqc_user_systems
           WHERE usr_code = usys_usr_code
             AND usr_status = 'A'
             AND usr_email IS NOT NULL
             AND usys_sys_code = 37
          AND usr_name LIKE '%' || v_usr_name || '%';

      RETURN vcursor;
   END;
END tqc_roles_cursor;
/