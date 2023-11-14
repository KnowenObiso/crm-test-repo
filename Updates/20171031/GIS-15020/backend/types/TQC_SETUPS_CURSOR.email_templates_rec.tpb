   TYPE email_templates_rec IS RECORD (
      msgt_code         tqc_msg_templates.msgt_code%TYPE,
      msgt_sht_desc     tqc_msg_templates.msgt_sht_desc%TYPE,
      msgt_msg          tqc_msg_templates.msgt_msg%TYPE,
      msgt_sys_code     tqc_msg_templates.msgt_sys_code%TYPE,
      msgt_sys_module   tqc_msg_templates.msgt_sys_module%TYPE,
      msgt_type         tqc_msg_templates.msgt_type%TYPE,
      msgt_prod_code    tqc_msg_templates.msgt_prod_code%TYPE,
      msgt_prod_name    VARCHAR2 (255),
      msgt_msg_subject  tqc_msg_templates.msgt_msg_subject%TYPE
   );

   TYPE email_templates_ref IS REF CURSOR
      RETURN email_templates_rec;