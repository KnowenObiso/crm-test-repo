
-- BODY --

   FUNCTION get_em_template_by_type (v_sys_code IN NUMBER, v_msg_type VARCHAR2)
      RETURN email_templates_ref
   IS
      v_cursor   email_templates_ref;
   BEGIN
      --RAISE_ERROR(v_sys_code||';'||v_msg_type);
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type, msgt_prod_code, 
                (SELECT pro_desc from tq_gis.gin_products where pro_code = msgt_prod_code
                    UNION
                 SELECT prod_desc from tq_lms.lms_products where prod_code = msgt_prod_code
                )
           FROM tqc_msg_templates
          WHERE msgt_sys_code = NVL (v_sys_code, 0)
                AND msgt_type = v_msg_type;

      RETURN (v_cursor);
   END;