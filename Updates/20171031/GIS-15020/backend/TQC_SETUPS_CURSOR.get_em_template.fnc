--spec
   FUNCTION get_em_template (v_sys_code IN NUMBER)
      RETURN email_templates_ref;
      
      
--body
   FUNCTION get_em_template (v_sys_code IN NUMBER)
      RETURN email_templates_ref
   IS
      v_cursor   email_templates_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type, msgt_prod_code,
                (SELECT pro_desc
                   FROM tq_gis.gin_products
                  WHERE pro_code = msgt_prod_code
                 UNION
                 SELECT prod_desc
                   FROM tq_lms.lms_products
                  WHERE prod_code = msgt_prod_code), msgt_msg_subject
           FROM tqc_msg_templates
          WHERE msgt_sys_code = NVL (v_sys_code, msgt_sys_code);

      RETURN (v_cursor);
   END;

