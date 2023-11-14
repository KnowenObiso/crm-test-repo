--- spec --

TYPE email_templates_rec IS RECORD (
      msgt_code         tqc_msg_templates.msgt_code%TYPE,
      msgt_sht_desc     tqc_msg_templates.msgt_sht_desc%TYPE,
      msgt_msg          tqc_msg_templates.msgt_msg%TYPE,
      msgt_sys_code     tqc_msg_templates.msgt_sys_code%TYPE,
      msgt_sys_module   tqc_msg_templates.msgt_sys_module%TYPE,
      msgt_type         tqc_msg_templates.msgt_type%TYPE,
      
      msgt_prod_code    tqc_msg_templates.msgt_prod_code%TYPE,
      msgt_prod_name    VARCHAR2(255)
   );

-- body --

FUNCTION get_em_template (v_sys_code IN NUMBER)
      RETURN email_templates_ref
   IS
      v_cursor   email_templates_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type, msgt_prod_code, 
                (SELECT pro_desc from tq_gis.gin_products where pro_code = msgt_prod_code
                    UNION
                 SELECT prod_desc from tq_lms.lms_products where prod_code = msgt_prod_code
                )
           FROM tqc_msg_templates
          WHERE msgt_sys_code = NVL (v_sys_code, msgt_sys_code);

      RETURN (v_cursor);
   END;