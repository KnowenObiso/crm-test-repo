PROCEDURE occupations_prc (
      v_add_edit       IN   VARCHAR2,
      v_occ_code       IN   tqc_occupations.occ_code%TYPE,
      v_occ_sht_desc   IN   tqc_occupations.occ_sht_desc%TYPE,
      v_occ_name       IN   tqc_occupations.occ_name%TYPE,
      v_occ_sec_code   IN   NUMBER
   )
   IS
      v_count   NUMBER;
      v_count_child NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
         
--         raise_error('v_occ_code:'||v_occ_code||' v_occ_sht_desc:'||v_occ_sht_desc||' v_occ_name:'||v_occ_name
--         ||' v_occ_sec_code:'||v_occ_sec_code);
--         
--            SELECT COUNT (1)
--              INTO v_count
--              FROM tqc_occupations
--             WHERE occ_code = v_occ_code;

            SELECT COUNT (1)
              INTO v_count
              FROM tqc_occupations
             WHERE occ_sec_code = v_occ_sec_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_occupations
                        (occ_code, occ_sec_code,
                         occ_sht_desc, occ_name
                        )
                 VALUES (tqc_occ_code_seq.NEXTVAL, v_occ_sec_code,
                         v_occ_sht_desc, v_occ_name
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_occupations
               SET occ_sht_desc = v_occ_sht_desc,
                   occ_name = v_occ_name
             WHERE occ_code = v_occ_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while updating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
--         raise_error('v_occ_code: '|| v_occ_code);

            SELECT COUNT(*) INTO v_count_child FROM tqc_sector_occupations
                WHERE occ_code = v_occ_code;
            IF v_count_child > 0
            THEN
                raise_error('Ensure that the occupation deleted is not attached to any sector.');
            ELSE                
                DELETE FROM tqc_occupations
                  WHERE occ_code = v_occ_code;
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   