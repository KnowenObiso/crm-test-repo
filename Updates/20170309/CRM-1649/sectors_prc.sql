PROCEDURE sectors_prc (
      v_add_edit       IN   VARCHAR2,
      v_sec_code       IN   tqc_sectors.sec_code%TYPE,
      v_sec_sht_desc   IN   tqc_sectors.sec_sht_desc%TYPE,
      v_sec_name       IN   tqc_sectors.sec_name%TYPE
   )
   IS
      v_count   NUMBER;
      v_child_count NUMBER;
   BEGIN
      --      raise_error(
      --      'v_add_edit: '||v_add_edit ||
      --      'v_sec_code: '||v_sec_code ||
      --      'v_sec_sht_desc: '||v_sec_sht_desc||
      --      'v_sec_name: '||v_sec_name
      --      );
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_sectors
             WHERE sec_code = v_sec_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_sectors
                        (sec_code, sec_sht_desc, sec_name
                        )
                 VALUES (tqc_sec_code_seq.NEXTVAL, v_sec_sht_desc, v_sec_name
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
            UPDATE tqc_sectors
               SET sec_sht_desc = v_sec_sht_desc,
                   sec_name = v_sec_name
             WHERE sec_code = v_sec_code;
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
         
            SELECT COUNT(*) INTO v_child_count FROM tqc_sector_occupations
                WHERE occ_sec_code = v_sec_code;
                
                IF v_child_count >0
                THEN
                    raise_error('Remove selected occupations from Selected occupations panel');
                ELSE            
                    DELETE FROM tqc_sectors
                        WHERE sec_code = v_sec_code;
                END IF;
         EXCEPTION            
            WHEN OTHERS
            THEN
               raise_error (    SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;