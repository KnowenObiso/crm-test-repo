SET DEFINE OFF;
Insert into TQC_PARAMETERS
   (PARAM_CODE, PARAM_NAME, PARAM_VALUE, PARAM_STATUS, PARAM_DESC)
 Values
   (20150924, 'ALLOW_MULT_UNITS_MNGR', 'Y', 'ACTIVE', 'IF Y THEN ALLOW MANAGER TO BE TAGGED FOR MORE THAN ONE UNIT');
COMMIT;


PROCEDURE unitmanagers (v_managers_ref OUT managers_ref)
   IS
   v_param  VARCHAR2(5);
   BEGIN
        
    BEGIN
      SELECT PARAM_VALUE
      INTO  v_param
      FROM TQC_PARAMETERS
      WHERE PARAM_NAME = 'ALLOW_MULT_UNITS_MNGR';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_param := 'N';
    END;
    
    IF v_param = 'N' THEN 
      
      OPEN v_managers_ref FOR
         SELECT   agn_code, agn_sht_desc, agn_name, twn_name
             FROM tqc_agencies, tqc_towns
            WHERE agn_twn_code = twn_code(+)
              AND agn_act_code IN (SELECT act_code
                                     FROM tqc_account_types
                                    WHERE act_type_id IN ('IA', 'BM'))
              AND agn_code NOT IN (SELECT bru_agn_code
                                     FROM tqc_branch_units
                                    WHERE bru_agn_code IS NOT NULL)
              AND agn_status = 'ACTIVE'
         UNION ALL
         SELECT   NULL, NULL, 'NONE', 'NONE'
             FROM DUAL
         ORDER BY 2 NULLS FIRST;
    ELSE
    
    OPEN v_managers_ref FOR
        SELECT   agn_code, agn_sht_desc, agn_name, twn_name
             FROM tqc_agencies, tqc_towns
            WHERE agn_twn_code = twn_code(+)
              AND agn_act_code IN (SELECT act_code
                                     FROM tqc_account_types
                                    WHERE act_type_id IN ('IA', 'BM'))
              
              AND agn_status = 'ACTIVE'
         UNION ALL
         SELECT   NULL, NULL, 'NONE', 'NONE'
             FROM DUAL
         ORDER BY 2 NULLS FIRST;
    
    END IF;
   END unitmanagers;
   
   ---------------------------------------------------------------------------------
   PROCEDURE update_branch_agencies (
      v_add_edit                  IN       VARCHAR2,
      v_bra_code                  IN OUT   tqc_branch_agencies.bra_code%TYPE,
      v_bra_brn_code              IN       tqc_branch_agencies.bra_brn_code%TYPE,
      v_bra_sht_desc              IN       tqc_branch_agencies.bra_sht_desc%TYPE,
      v_bra_name                  IN       tqc_branch_agencies.bra_name%TYPE,
      v_bra_status                IN       tqc_branch_agencies.bra_status%TYPE,
      v_bra_manager               IN       tqc_branch_agencies.bra_manager%TYPE,
      v_bra_agn_code              IN       tqc_branch_agencies.bra_agn_code%TYPE,
      v_bra_post_level            IN       tqc_branch_agencies.bra_post_level%TYPE,
      v_bra_bru_mngr_seq_no       IN       tqc_branch_agencies.bra_bru_mngr_seq_no%TYPE,
      v_bra_agn_seq_no            IN       tqc_branch_agencies.bra_agn_seq_no%TYPE,
      v_bra_mngr_allowed          IN       tqc_branch_agencies.bra_mngr_allowed%TYPE,
      v_bra_overide_comm_earned   IN       tqc_branch_agencies.bra_overide_comm_earned%TYPE,
      v_bra_sht_descpref          IN       VARCHAR2,
      brncombuss                  IN       VARCHAR2,
      v_bra_pol_seq               IN       tqc_branch_agencies.bra_pol_seq%TYPE,
      v_bra_prop_seq              IN       tqc_branch_agencies.bra_prop_seq%TYPE,
      v_err                       OUT      VARCHAR2
   )
   IS
      v_rec_count               NUMBER;
      v_count                   NUMBER;
      v_param                   VARCHAR2(20);
      v_bra_sht_desc_new        VARCHAR2(20);
      
   BEGIN
        
       
      BEGIN
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_branch_agencies
          WHERE bra_code = v_bra_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error (   'The Branch_Agency Code Is Wrong...'
                         || SQLERRM (SQLCODE)
                        );
            RETURN;
      END;
      
      BEGIN
          SELECT PARAM_VALUE
          INTO v_param
          FROM TQC_PARAMETERS
          WHERE PARAM_NAME = 'CLIENT';
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_param := NULL;
      END;
      
     

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            SELECT tqc_bra_code_seq.NEXTVAL
              INTO v_bra_code
              FROM DUAL;
              
              IF v_param = 'UNION' THEN
                
                
                BEGIN
                   SELECT SUBSTR (brn_sht_desc, 0, 9) ||'/'|| v_bra_code
                     INTO v_bra_sht_desc_new
                     FROM tqc_branches
                    WHERE brn_code = v_bra_brn_code;
                EXCEPTION
                   WHEN OTHERS
                   THEN
                      NULL;
                END;
                
              ELSE
                v_bra_sht_desc_new := v_bra_code;
              
              END IF;
        
            BEGIN
               SELECT COUNT (*)
                 INTO v_count
                 FROM tqc_branch_agencies
                WHERE bra_sht_desc = TO_CHAR (v_bra_code);
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
            END;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;

            /* SELECT COUNT(1) INTO v_count
             FROM TQC_BRANCH_AGENCIES
             WHERE BRA_SHT_DESC = v_BRA_SHT_DESC;

             IF v_count > 0 THEN
                 RAISE_ERROR('Record with ID Exists!');
                 RETURN;
             END IF;
             SELECT tqc_bra_code_seq.nextval INTO v_bra_code FROM DUAL;*/
            INSERT INTO tqc_branch_agencies
                        (bra_code, bra_brn_code, bra_sht_desc, bra_name,
                         bra_status, bra_manager, bra_agn_code,
                         bra_post_level, bra_bru_mngr_seq_no,
                         bra_agn_seq_no, bra_pol_seq, bra_prop_seq
                        )
                 VALUES (v_bra_code, v_bra_brn_code, v_bra_sht_desc_new, v_bra_name,
                         v_bra_status, v_bra_manager, v_bra_agn_code,
                         v_bra_post_level, v_bra_bru_mngr_seq_no,
                         v_bra_agn_seq_no, v_bra_pol_seq, v_bra_prop_seq
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Inserting '
                            || v_bra_name
                            || ' Branch_Agency Details..'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_rec_count != 0
      THEN
         BEGIN
            --RAISE_ERROR('ERROR '||v_bra_brn_code||' v_bra_code '||v_bra_code||' v_bra_agn_code '||v_bra_agn_code);
            UPDATE tqc_branch_agencies
               SET bra_brn_code = NVL (v_bra_brn_code, bra_brn_code),
                   bra_sht_desc = NVL (v_bra_sht_desc, bra_sht_desc),
                   bra_name = NVL (v_bra_name, bra_name),
                   bra_status = NVL (v_bra_status, bra_status),
                   bra_manager = NVL (v_bra_manager, bra_manager),
                   bra_agn_code = NVL (v_bra_agn_code, bra_agn_code),
                   bra_post_level = NVL (v_bra_post_level, bra_post_level),
                   bra_bru_mngr_seq_no = v_bra_bru_mngr_seq_no,
                   bra_agn_seq_no = v_bra_agn_seq_no,
                   bra_pol_seq = v_bra_pol_seq,
                   bra_prop_seq = v_bra_prop_seq
             WHERE bra_code = v_bra_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Updating '
                            || v_bra_name
                            || ' Branch_Agency Details..'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSE
         raise_error ('System Unable To Resolve Branch_Agency ID ');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error Updating '
                      || v_bra_name
                      || ' Branch_Agency Details..'
                      || SQLERRM (SQLCODE)
                     );
         RETURN;
   END;
   
   -------------------------------------------------------------------------------------------------
    PROCEDURE update_units (
      v_add_edit                   IN       VARCHAR2,
      v_bru_code                   IN OUT   tqc_branch_units.bru_code%TYPE,
      v_bru_brn_code               IN       tqc_branch_units.bru_brn_code%TYPE,
      v_bru_sht_desc               IN       tqc_branch_units.bru_sht_desc%TYPE,
      v_bru_name                   IN       tqc_branch_units.bru_name%TYPE,
      v_bru_supervisor             IN       tqc_branch_units.bru_supervisor%TYPE,
      v_bru_status                 IN       tqc_branch_units.bru_status%TYPE,
      v_bru_agn_code               IN       tqc_branch_units.bru_agn_code%TYPE,
      v_bru_bra_code               IN       tqc_branch_units.bru_bra_code%TYPE,
      v_bru_manager                IN       tqc_branch_units.bru_manager%TYPE,
      v_bru_post_level             IN       tqc_branch_units.bru_post_level%TYPE,
      v_bru_agn_seq_no             IN       tqc_branch_units.bru_agn_seq_no%TYPE,
      v_bru_mngr_allowed           IN       tqc_branch_units.bru_mngr_allowed%TYPE
            DEFAULT NULL,
      v_bru_overide_comm_earned    IN       tqc_branch_units.bru_overide_comm_earned%TYPE
            DEFAULT NULL,
      v_bru_unt_sht_desc_prefix    IN       tqc_branch_units.bru_unt_sht_desc_prefix%TYPE,
      v_bru_compt_ov_on_own_buss   IN       tqc_branch_units.bru_compt_ov_on_own_buss%TYPE,
      v_bru_pol_seq                IN       tqc_branch_units.bru_pol_seq%TYPE,
      v_bru_prop_seq               IN       tqc_branch_units.bru_prop_seq%TYPE,
      v_err                        OUT      VARCHAR2
   )
   IS
      v_rec_count     NUMBER;
      v_count         NUMBER;
      v_unitshtdesc   VARCHAR2 (20);
      v_param         VARCHAR2(20);
   BEGIN
      --RAISE_ERROR('dsdsd '||v_bru_bra_code);
      BEGIN
          SELECT PARAM_VALUE
          INTO v_param
          FROM TQC_PARAMETERS
          WHERE PARAM_NAME = 'CLIENT';
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_param := NULL;
      END;
      
      BEGIN
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_branch_units
          WHERE bru_code = v_bru_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error ('The Unit Code Is Wrong...' || SQLERRM (SQLCODE));
            RETURN;
      END;
      
      IF v_param = 'UNION' THEN
                
                
            BEGIN
               SELECT SUBSTR (BRA_SHT_DESC, 0, 9) ||'/'|| v_bru_code
                 INTO v_unitshtdesc
                 FROM tqc_branch_agencies
                WHERE BRA_CODE = v_bru_bra_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  NULL;
            END;
                
       ELSE
            BEGIN
               SELECT SUBSTR (brn_sht_desc, 0, 9) || v_bru_code
                 INTO v_unitshtdesc
                 FROM tqc_branches
                WHERE brn_code = v_bru_brn_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  NULL;
            END;
              
              END IF;

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            SELECT tqc_bru_code_seq.NEXTVAL
              INTO v_bru_code
              FROM DUAL;

            BEGIN
               SELECT COUNT (1)
                 INTO v_count
                 FROM tqc_branch_units
                WHERE bru_sht_desc = TO_CHAR (v_bru_code);
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
            END;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;

            

            /*
            SELECT COUNT(1) INTO v_count
            FROM TQC_BRANCH_UNITS
            WHERE BRU_SHT_DESC = v_bru_sht_desc;

            IF v_count > 0 THEN
                RAISE_ERROR('Record with ID Exists!');
                RETURN;
            END IF;
            SELECT tqc_bru_code_seq.nextval INTO v_bru_code FROM DUAL;*/
            INSERT INTO tqc_branch_units
                        (bru_code, bru_brn_code, bru_sht_desc,
                         bru_name, bru_supervisor, bru_status,
                         bru_agn_code, bru_bra_code, bru_manager,
                         bru_post_level, bru_agn_seq_no,
                         bru_mngr_allowed, bru_overide_comm_earned,
                         bru_unt_sht_desc_prefix,
                         bru_compt_ov_on_own_buss, bru_pol_seq,
                         bru_prop_seq
                        )
                 VALUES (v_bru_code, v_bru_brn_code, v_unitshtdesc,
                         v_bru_name, v_bru_supervisor, v_bru_status,
                         v_bru_agn_code, v_bru_bra_code, v_bru_manager,
                         v_bru_post_level, v_bru_agn_seq_no,
                         v_bru_mngr_allowed, v_bru_overide_comm_earned,
                         v_bru_unt_sht_desc_prefix,
                         v_bru_compt_ov_on_own_buss, v_bru_pol_seq,
                         v_bru_prop_seq
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Inserting '
                            || v_bru_name
                            || ' Branch_Unit Details'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_rec_count != 0
      THEN
         IF v_bru_sht_desc IS NULL
         THEN
            BEGIN
               SELECT SUBSTR (brn_sht_desc, 0, 9) || v_bru_code
                 INTO v_unitshtdesc
                 FROM tqc_branches
                WHERE brn_code = v_bru_brn_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  NULL;
            END;
         ELSE
            v_unitshtdesc := v_bru_sht_desc;
         END IF;

         BEGIN
            UPDATE tqc_branch_units
               SET bru_brn_code = NVL (v_bru_brn_code, bru_brn_code),
                   bru_sht_desc = v_unitshtdesc,
                   bru_name = NVL (v_bru_name, bru_name),
                   bru_supervisor = v_bru_supervisor,
                   bru_status = v_bru_status,
                   bru_agn_code = v_bru_agn_code,
                   bru_bra_code = v_bru_bra_code,
                   bru_manager = v_bru_manager,
                   bru_post_level = v_bru_post_level,
                   bru_agn_seq_no = v_bru_agn_seq_no,
                   bru_mngr_allowed = v_bru_mngr_allowed,
                   bru_overide_comm_earned = v_bru_overide_comm_earned,
                   bru_unt_sht_desc_prefix = v_bru_unt_sht_desc_prefix,
                   bru_compt_ov_on_own_buss = v_bru_compt_ov_on_own_buss,
                   bru_pol_seq = v_bru_pol_seq,
                   bru_prop_seq = v_bru_prop_seq
             WHERE bru_code = v_bru_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating Units  ' || SQLERRM (SQLCODE));
               RETURN;
         END;
      ELSE
         raise_error ('System Unable To Resolve Branch_Unit ID');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating Branch_Units  ' || SQLERRM (SQLCODE));
         RETURN;
   END update_units;
   
   ----------------------------------------------------------------------------
   
   SET DEFINE OFF;
Insert into TQC_PARAMETERS
   (PARAM_CODE, PARAM_NAME, PARAM_VALUE, PARAM_STATUS, PARAM_DESC)
 Values
   (20150924, 'ALLOW_MULT_UNITS_MNGR', 'Y', 'ACTIVE', 'IF Y THEN ALLOW MANAGER TO BE TAGGED FOR MORE THAN ONE UNIT');
COMMIT;

SET DEFINE OFF;
Insert into TQC_PARAMETERS
   (PARAM_CODE, PARAM_NAME, PARAM_VALUE, PARAM_STATUS, PARAM_DESC)
 Values
   (20150205, 'CLIENT', 'UNION', 'ACTIVE', 'CLIENT');
COMMIT;

