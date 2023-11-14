--
-- TQC_ROLES_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_roles_pkg AS
/******************************************************************************
   NAME:       tqc_roles_pkg
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/8/2016             1. Kevin wainaina Created this package.
******************************************************************************/

PROCEDURE authorize_role_processes (
      v_sprc_code   IN   tqc_sys_processes.sprc_code%TYPE,
      v_srls_code   IN   tqc_sys_roles.srls_code%TYPE,
      v_action      IN   VARCHAR
   );
   
PROCEDURE authorize_role_processes_areas (
      v_sprca_code   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_sprc_code    IN   tqc_sys_processes.sprc_code%TYPE,
      v_srls_code    IN   tqc_sys_roles.srls_code%TYPE,
      v_action       IN   VARCHAR
   );
   
 PROCEDURE authorize_role_sub_areas (
      v_sprsa_code   IN   tqc_sys_process_sub_areas.sprsa_code%TYPE,
      v_sprca_code   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_sprc_code    IN   tqc_sys_processes.sprc_code%TYPE,
      v_srls_code    IN   tqc_sys_roles.srls_code%TYPE,
      v_debitlimit   IN   tqc_sys_roles_prcs_s_area.srpsa_debit_limit%TYPE,
      v_creditlimit  IN   tqc_sys_roles_prcs_s_area.srpsa_credit_limit%TYPE,
      v_action       IN   VARCHAR
   );

END tqc_roles_pkg; 
/