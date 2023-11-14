-- SPEC --
PROCEDURE messagetemplates_prc (
      v_addedit                     VARCHAR2,
      v_messagetemplates_tab   IN   tqc_msg_templates_tab
   );
-- BODY ---

PROCEDURE messagetemplates_prc (
      v_addedit                     VARCHAR2,
      v_messagetemplates_tab   IN   tqc_msg_templates_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_messagetemplates_tab.COUNT = 0
      THEN
         raise_error
                    (   'Error occured. No Message Template Data Provided : '
                     || SQLERRM (SQLCODE)
                    );
      END IF;

      FOR i IN 1 .. v_messagetemplates_tab.COUNT
      LOOP      
         --- raise_error('msgt_prod_code: ' || v_messagetemplates_tab (i).msgt_prod_code);
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_msg_templates
             WHERE msgt_sht_desc = v_messagetemplates_tab (i).msgt_sht_desc
               AND msgt_type = v_messagetemplates_tab (i).msgt_type;

            IF v_count > 0
            THEN
               raise_error
                        ('Record with the same Message Code and Type Exists!');
            END IF;

            INSERT INTO tqc_msg_templates
                        (msgt_code,
                         msgt_sht_desc,
                         msgt_msg,
                         msgt_sys_code,
                         msgt_sys_module,
                         msgt_type,
                         msgt_created_by,
                         msgt_updated_by,
                         msgt_prod_code
                        )
                 VALUES (tqc_msgt_code_seq.NEXTVAL,
                         v_messagetemplates_tab (i).msgt_sht_desc,
                         v_messagetemplates_tab (i).msgt_msg,
                         v_messagetemplates_tab (i).msgt_sys_code,
                         v_messagetemplates_tab (i).msgt_sys_module,
                         v_messagetemplates_tab (i).msgt_type,
                         v_messagetemplates_tab (i).msgt_created_by,
                         v_messagetemplates_tab (i).msgt_updated_by,
                         v_messagetemplates_tab (i).msgt_prod_code
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_msg_templates
               SET msgt_sht_desc = v_messagetemplates_tab (i).msgt_sht_desc,
                   msgt_msg = v_messagetemplates_tab (i).msgt_msg,
                   msgt_sys_code = v_messagetemplates_tab (i).msgt_sys_code,
                   msgt_sys_module =
                                    v_messagetemplates_tab (i).msgt_sys_module,
                   msgt_type = v_messagetemplates_tab (i).msgt_type,
                   msgt_created_by =
                                    v_messagetemplates_tab (i).msgt_created_by,
                   msgt_updated_by =
                                    v_messagetemplates_tab (i).msgt_updated_by,
                   msgt_prod_code =
                                    v_messagetemplates_tab (i).msgt_prod_code
             WHERE msgt_code = v_messagetemplates_tab (i).msgt_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_msg_templates
                  WHERE msgt_code = v_messagetemplates_tab (i).msgt_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Message Templates!');
   END messagetemplates_prc;