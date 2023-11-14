   --SPECS
   --tqc_roles_cursor.revoke_role_process
   PROCEDURE revoke_role_process (
      v_srprc_code   IN   tqc_sys_roles_processes.SRPRC_CODE%TYPE
   );
   --BODY
   --tqc_roles_cursor.revoke_role_process
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
   
   ---------------------------------------------------------------------------
   --replace tqc_roles_cursor.revoke_role_process_area
   
   PROCEDURE revoke_role_process_area (
      v_processarea   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_role_code     IN   tqc_sys_roles.srls_code%TYPE
   )
   IS
   v_count NUMBER;
   v_srprc_sprc_code NUMBER;
   BEGIN
      --RAISE_ERROR(v_role_code ||';'||v_processArea);
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