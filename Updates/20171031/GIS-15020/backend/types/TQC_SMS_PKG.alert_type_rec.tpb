   TYPE alert_type_rec IS RECORD (
      alrt_code              tqc_alert_types.alrt_code%TYPE,
      alrt_type              tqc_alert_types.alrt_type%TYPE,
      alrt_sys_code          tqc_alert_types.alrt_sys_code%TYPE,
      alrt_email             tqc_alert_types.alrt_email%TYPE,
      alrt_sms               tqc_alert_types.alrt_sms%TYPE,
      sys_name               VARCHAR2 (200),
      alrt_screen            tqc_alert_types.alrt_screen%TYPE,
      alrt_email_msgt_code   tqc_alert_types.alrt_email_msgt_code%TYPE,
      alrt_sms_msgt_code     tqc_alert_types.alrt_sms_msgt_code%TYPE,
      alrt_grp_usr_code      tqc_alert_types.alrt_grp_usr_code%TYPE,
      email                  VARCHAR2 (200),
      sms                    VARCHAR2 (200),
      grp_user               VARCHAR2 (200),
      alrt_check_alert       tqc_alert_types.alrt_check_alert%TYPE,
      alrt_sht_desc          tqc_alert_types.alrt_sht_desc%TYPE
   );

   TYPE alert_type_ref IS REF CURSOR
      RETURN alert_type_rec;