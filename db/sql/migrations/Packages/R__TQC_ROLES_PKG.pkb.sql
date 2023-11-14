--
-- TQC_ROLES_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_roles_pkg AS 
/******************************************************************************
   NAME:       tqc_roles_pkg
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/8/2016             1. Kevin wainaina Created this package.
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

PROCEDURE authorize_role_processes (
      v_sprc_code   IN   tqc_sys_processes.sprc_code%TYPE,
      v_srls_code   IN   tqc_sys_roles.srls_code%TYPE,
      v_action      IN   VARCHAR
   )
   IS
    v_srprc_code        NUMBER;
    v_count             NUMBER;
   BEGIN
   
     IF v_action = 'U'
     THEN
     --raise_error ('UnAuthorize');
     
         BEGIN
             DELETE FROM tqc_sys_roles_prcs_s_area
                    WHERE srpsa_srpra_code IN
                             (SELECT srpra_code  FROM tqc_sys_roles_prcs_area
                               WHERE srpra_srprc_code IN
                                    (SELECT srprc_code FROM tqc_sys_roles_processes
                                     WHERE srprc_sprc_code =v_sprc_code
                                     AND   srprc_srls_code =v_srls_code));                         
             
                                 
             DELETE FROM tqc_sys_roles_prcs_area
                    WHERE srpra_srprc_code IN
                             (SELECT srprc_code FROM tqc_sys_roles_processes
                              WHERE srprc_sprc_code =v_sprc_code
                              AND   srprc_srls_code =v_srls_code);
                              
             DELETE FROM tqc_sys_roles_processes
                              WHERE srprc_sprc_code =v_sprc_code
                              AND   srprc_srls_code =v_srls_code;
         EXCEPTION
                WHEN OTHERS
                THEN
                   raise_error ('Error revoking Process, Process Areas and Sub Areas.');
         END;
      
     ELSIF v_action ='A'
     THEN
        
        --raise_error ('Authorize');
        
       SELECT tqc_srprc_seq.NEXTVAL
       INTO v_srprc_code
       FROM DUAL;
       
       SELECT COUNT(1) 
       INTO v_count
       FROM tqc_sys_roles_processes
       WHERE srprc_sprc_code =v_sprc_code
       AND   srprc_srls_code =v_srls_code;
       
       IF(NVL(v_count,0)=0)
       THEN
           BEGIN
               INSERT INTO tqc_sys_roles_processes
                            (srprc_code, srprc_srls_code,
                             srprc_sprc_code
                            )
                     VALUES (v_srprc_code, v_srls_code,
                             v_sprc_code
                        );
           EXCEPTION
                    WHEN OTHERS
                    THEN
                       raise_error ('Error Granting Process..');
             END;
       END IF;
     END IF;
   END authorize_role_processes;
PROCEDURE authorize_role_processes_areas (
      v_sprca_code   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_sprc_code    IN   tqc_sys_processes.sprc_code%TYPE,
      v_srls_code    IN   tqc_sys_roles.srls_code%TYPE,
      v_action       IN   VARCHAR
   )
   IS
   v_srprc_code NUMBER;
   v_srpra_code NUMBER;
   v_count NUMBER;
 
   BEGIN
   
  
     IF v_action = 'U'
     THEN
     
       SELECT srpra_code
       INTO v_srpra_code
       FROM tqc_sys_roles,
            tqc_sys_processes,
            tqc_sys_process_areas,
            tqc_sys_roles_prcs_area,
            tqc_sys_roles_processes
       WHERE srls_code = v_srls_code 
       AND sprc_code = v_sprc_code
       AND sprca_code =v_sprca_code
       AND srpra_srprc_code =srprc_code
       AND srpra_sprca_code = sprca_code 
       AND srprc_srls_code = srls_code
       AND srprc_sprc_code = sprc_code;
       
    -- raise_error ('UnAuthorize');
         BEGIN
         
             DELETE FROM tqc_sys_roles_prcs_s_area
                    WHERE srpsa_srpra_code = v_srpra_code;                         
             
                                 
             DELETE FROM tqc_sys_roles_prcs_area
                WHERE srpra_code =v_srpra_code;
         EXCEPTION
                    WHEN OTHERS
                    THEN
                       raise_error ('Error Revoking Process Area and Sub Areas..');
         END;
                
     ELSIF v_action ='A'
     THEN
        --raise_error ('Authorize');
        
        SELECT tqc_srpra_seq.NEXTVAL
        INTO v_srpra_code
        FROM DUAL;
    
       SELECT COUNT(1)
       INTO v_count
       FROM tqc_sys_roles,
            tqc_sys_processes,
            tqc_sys_process_areas,
            tqc_sys_roles_prcs_area,
            tqc_sys_roles_processes
       WHERE srls_code = v_srls_code 
       AND sprc_code = v_sprc_code
       AND sprca_code =v_sprca_code
       AND srpra_srprc_code =srprc_code
       AND srpra_sprca_code = sprca_code 
       AND srprc_srls_code = srls_code
       AND srprc_sprc_code = sprc_code;

        IF(NVL(v_count,0)=0)
        THEN
        
        SELECT srprc_code
        INTO v_srprc_code
            FROM tqc_sys_roles,
            tqc_sys_processes,
            tqc_sys_roles_processes
        WHERE srls_code =v_srls_code 
        AND sprc_code = v_sprc_code 
        AND srprc_srls_code = srls_code
        AND srprc_sprc_code = sprc_code;
       
            BEGIN
                INSERT INTO tqc_sys_roles_prcs_area
                      (srpra_code, srpra_srprc_code, srpra_sprca_code)
                VALUES (v_srpra_code, v_srprc_code, v_sprca_code);
            EXCEPTION
                        WHEN OTHERS
                        THEN
                           raise_error ('Error Revoking Process Area and Sub Areas..');
            END;
        END IF;
     END IF;
   END authorize_role_processes_areas;
   
   PROCEDURE authorize_role_sub_areas (
      v_sprsa_code   IN   tqc_sys_process_sub_areas.sprsa_code%TYPE,
      v_sprca_code   IN   tqc_sys_process_areas.sprca_code%TYPE,
      v_sprc_code    IN   tqc_sys_processes.sprc_code%TYPE,
      v_srls_code    IN   tqc_sys_roles.srls_code%TYPE,
      v_debitlimit   IN   tqc_sys_roles_prcs_s_area.srpsa_debit_limit%TYPE,
      v_creditlimit  IN   tqc_sys_roles_prcs_s_area.srpsa_credit_limit%TYPE,
      v_action       IN   VARCHAR
   )
   IS
    v_srpra_code NUMBER;
    v_srpsa_code NUMBER;
    v_count      NUMBER;
   BEGIN
   --raise_error('v_sprsa_code '||v_sprsa_code||'v_debitlimit '||v_debitlimit||'v_creditlimit '||v_creditlimit);
   
     IF v_action = 'U'
     THEN
     
       SELECT srpsa_code
       INTO v_srpsa_code
       FROM tqc_sys_roles,
            tqc_sys_processes,
            tqc_sys_process_areas,
            tqc_sys_roles_prcs_area,
            tqc_sys_roles_processes,
            tqc_sys_roles_prcs_s_area,
            tqc_sys_process_sub_areas
       WHERE srls_code  =   v_srls_code 
       AND sprc_code    =   v_sprc_code
       AND sprca_code   =   v_sprca_code
       AND sprsa_code   =   v_sprsa_code
       AND srpsa_sprsa_code = sprsa_code
       AND srpsa_srpra_code = srpra_code 
       AND srpra_srprc_code = srprc_code 
       AND srpra_sprca_code = sprca_code
       AND srprc_srls_code  = srls_code
       AND srprc_sprc_code = sprc_code;
    -- raise_error ('UnAuthorize');
         BEGIN
             DELETE FROM tqc_sys_roles_prcs_s_area
                    WHERE srpsa_code = v_srpsa_code;
         EXCEPTION
                        WHEN OTHERS
                        THEN
                           raise_error ('Error Revoking Sub Area..');
         END;
                        
     ELSIF v_action ='A'
     THEN
        
        --raise_error ('Authorize');
       SELECT COUNT(1)
       INTO v_count
       FROM tqc_sys_roles,
            tqc_sys_processes,
            tqc_sys_process_areas,
            tqc_sys_roles_prcs_area,
            tqc_sys_roles_processes,
            tqc_sys_roles_prcs_s_area,
            tqc_sys_process_sub_areas
       WHERE srls_code  =   v_srls_code 
       AND sprc_code    =   v_sprc_code
       AND sprca_code   =   v_sprca_code
       AND sprsa_code   =   v_sprsa_code
       AND srpsa_sprsa_code = sprsa_code
       AND srpsa_srpra_code = srpra_code 
       AND srpra_srprc_code = srprc_code 
       AND srpra_sprca_code = sprca_code
       AND srprc_srls_code  = srls_code
       AND srprc_sprc_code = sprc_code;
       
       SELECT srpra_code
           INTO v_srpra_code
           FROM tqc_sys_roles,
                tqc_sys_processes,
                tqc_sys_process_areas,
                tqc_sys_roles_prcs_area,
                tqc_sys_roles_processes
           WHERE srls_code  =   v_srls_code 
           AND sprc_code    =   v_sprc_code
           AND sprca_code   =   v_sprca_code
           AND srpra_srprc_code = srprc_code 
           AND srpra_sprca_code = sprca_code
           AND srprc_srls_code  = srls_code
           AND srprc_sprc_code  = sprc_code;
           
        IF(NVL(v_count,0)=0)
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
                               raise_error ('Error Granting Sub Area..');
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
                               raise_error ('Error Updating Sub Area..');
            END;
        END IF;
     END IF;
    
   END authorize_role_sub_areas;
END tqc_roles_pkg; 
/