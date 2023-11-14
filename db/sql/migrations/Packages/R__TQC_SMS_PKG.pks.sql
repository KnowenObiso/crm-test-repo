--
-- TQC_SMS_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_sms_pkg
AS
  PROCEDURE create_sms_msg (
      v_reciepient   IN       VARCHAR2,
      v_clnt_code    IN       NUMBER,
      v_agn_code     IN       NUMBER,
      v_quot_code    IN       NUMBER,
      v_quot_no      IN       VARCHAR2,
      v_pol_code     IN       NUMBER,
      v_pol_no       IN       VARCHAR2,
      v_clm_no       IN       VARCHAR2,
      v_msgt_code    IN       NUMBER,
      v_sys_code     IN       NUMBER,
      v_sys_mod      IN       VARCHAR2,
      v_user         IN       VARCHAR2,
      v_sms_code     IN OUT   NUMBER,
      v_app_lvl      IN       VARCHAR2,
      v_usr_code     IN       NUMBER,
      v_trans_no     IN   NUMBER DEFAULT NULL
   );

   PROCEDURE send_sms_msg (
      v_clnt_code   IN       NUMBER,
      v_agn_code    IN       NUMBER,
      v_quot_code   IN       NUMBER,
      v_quot_no     IN       VARCHAR,
      v_pol_code    IN       NUMBER,
      v_pol_no      IN       VARCHAR2,
      v_clm_no      IN       VARCHAR2,
      v_tel_no      IN       VARCHAR2,
      v_sms_text    IN       VARCHAR2,
      v_sys_code    IN       NUMBER,
      v_sys_mod     IN       VARCHAR2,
      v_user        IN       VARCHAR2,
      v_sms_code    OUT      NUMBER
   );

   PROCEDURE send_sms_msg (
      v_clnt_code   IN       NUMBER,
      v_agn_code    IN       NUMBER,
      v_quot_code   IN       NUMBER,
      v_pol_code    IN       NUMBER,
      v_pol_no      IN       VARCHAR2,
      v_clm_no      IN       VARCHAR2,
      v_tel_no      IN       VARCHAR2,
      v_sms_text    IN       VARCHAR2,
      v_sys_code    IN       NUMBER,
      v_sys_mod     IN       VARCHAR2,
      v_user        IN       VARCHAR2,
      v_sms_code    OUT      NUMBER
   );

   PROCEDURE send_sms_msg (
      v_clnt_code   IN   NUMBER,
      v_agn_code    IN   NUMBER,
      v_quot_code   IN   NUMBER,
      v_pol_code    IN   NUMBER,
      v_pol_no      IN   VARCHAR2,
      v_clm_no      IN   VARCHAR2,
      v_tel_no      IN   VARCHAR2,
      v_sms_text    IN   VARCHAR2,
      v_sys_code    IN   NUMBER,
      v_sys_mod     IN   VARCHAR2,
      v_user        IN   VARCHAR2
   );

   PROCEDURE receive_sms_msg (v_tel_no IN VARCHAR2, v_sms_text IN VARCHAR2);

   PROCEDURE prompt_inbox_usr;

   PROCEDURE prompt_usr_esc;

   PROCEDURE create_sms_job;

   PROCEDURE create_sms_esc_job;

   PROCEDURE remove_sms_job;

   PROCEDURE remove_sms_esc_job;

   v_job   NUMBER;

   PROCEDURE create_sms_xml (v_sms_code IN NUMBER DEFAULT NULL);

   PROCEDURE ftp_file;

   TYPE sms_list_rec IS RECORD (
      contact_name        VARCHAR2 (150),
      contact_tel         VARCHAR2 (50),
      contact_code        NUMBER,
      contact_policy_no   VARCHAR2 (30),
      msg_text            VARCHAR2 (500)
   );

   TYPE sms_list_tab IS TABLE OF sms_list_rec
      INDEX BY BINARY_INTEGER;

   TYPE sms_list_ref IS REF CURSOR
      RETURN sms_list_rec;

   PROCEDURE sms_contact_query (
      resultset       IN OUT   sms_list_ref,
      listtype                 VARCHAR2,
      v_prod_code              NUMBER,
      v_pol_no                 VARCHAR2,
      v_sys_code               NUMBER,
      sms_temp_text   IN       VARCHAR2
   );

   TYPE account_type_rec IS RECORD (
      agnt_code   tqc_agencies.agn_code%TYPE,
      ant_name    tqc_agencies.agn_name%TYPE,
      ant_email   tqc_agencies.agn_email_address%TYPE,
      ant_tel     tqc_agencies.agn_sms_tel%TYPE
   );

   TYPE account_type_tab IS TABLE OF account_type_rec
      INDEX BY BINARY_INTEGER;

   TYPE account_type_ref IS REF CURSOR
      RETURN account_type_rec;

   PROCEDURE account_tpye (
      resultset    IN OUT   account_type_ref,
      v_act_code   IN       NUMBER
   );

   TYPE sms_rec IS RECORD (
      sms_code            tqc_sms_messages.sms_code%TYPE,
      sms_sys_code        tqc_sms_messages.sms_sys_code%TYPE,
      sys_name            tqc_systems.sys_name%TYPE,
      sms_sys_module      tqc_sms_messages.sms_sys_module%TYPE,
      sms_clnt_code       tqc_sms_messages.sms_clnt_code%TYPE,
      client_name         VARCHAR2 (250),
      sms_agn_code        tqc_sms_messages.sms_agn_code%TYPE,
      agn_name            tqc_agencies.agn_name%TYPE,
      sms_pol_code        tqc_sms_messages.sms_pol_code%TYPE,
      sms_pol_no          tqc_sms_messages.sms_pol_no%TYPE,
      sms_clm_no          tqc_sms_messages.sms_clm_no%TYPE,
      sms_tel_no          tqc_sms_messages.sms_tel_no%TYPE,
      sms_msg             tqc_sms_messages.sms_msg%TYPE,
      sms_status          tqc_sms_messages.sms_status%TYPE,
      sms_prepared_by     tqc_sms_messages.sms_prepared_by%TYPE,
      sms_prepared_date   tqc_sms_messages.sms_prepared_date%TYPE,
      sms_send_date       tqc_sms_messages.sms_send_date%TYPE,
      sms_quot_code       tqc_sms_messages.sms_quot_code%TYPE,
      sms_quot_no         tqc_sms_messages.sms_quot_no%TYPE,
      sms_usr_code        tqc_sms_messages.sms_usr_code%TYPE
   );

   TYPE sms_ref IS REF CURSOR
      RETURN sms_rec;

   FUNCTION get_sms (
      v_sms_type    IN   VARCHAR2,
      v_clnt_code   IN   NUMBER DEFAULT -2000,
      v_agn_code    IN   NUMBER DEFAULT -2000,
      v_pol_code    IN   NUMBER DEFAULT -2000,
      v_quot_code   IN   NUMBER DEFAULT -2000,
      v_clm_no      IN   VARCHAR2 DEFAULT 'ALL',
      v_sys_code    IN   NUMBER DEFAULT -2000
   )
      RETURN sms_ref;

   TYPE smsquotation IS RECORD (
      quot_code   gin_quotations.quot_code%TYPE,
      quot_no     gin_quotations.quot_no%TYPE
   );

   TYPE smsquotationrec IS REF CURSOR
      RETURN smsquotation;

   PROCEDURE getsmsquotation (v_sms_quotation OUT smsquotationrec);

   TYPE sms_template_rec IS RECORD (
      msgt_code       tqc_msg_templates.msgt_code%TYPE,
      msgt_sht_desc   tqc_msg_templates.msgt_sht_desc%TYPE,
      msgt_msg        tqc_msg_templates.msgt_msg%TYPE
   );

   TYPE sms_template_ref IS REF CURSOR
      RETURN sms_template_rec;

   FUNCTION get_sms_templates (
      v_sys_code     IN   NUMBER,
      v_sys_module   IN   VARCHAR2,
      v_msgt_type    IN   VARCHAR2
   )
      RETURN sms_template_ref;

   PROCEDURE alerttype_prc (
      v_addedit                  VARCHAR2,
      v_alerttype_tab   IN       tqc_alert_types_tab,
      v_err             OUT      VARCHAR2
   );

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

   PROCEDURE get_alerttype (
      v_refcur          OUT      alert_type_ref,
      v_alrt_sys_code   IN       NUMBER,
      v_shtdesc         IN       VARCHAR2 DEFAULT NULL
   );

   PROCEDURE alert_prc (
      v_addedit              VARCHAR2,
      v_alert_tab   IN       tqc_alerts_tab,
      v_err         OUT      VARCHAR2
   );

   TYPE alert_rec IS RECORD (
      alrts_code          tqc_alerts.alrts_code%TYPE,
      alrts_alrt_code     tqc_alerts.alrts_alrt_code%TYPE,
      alrts_sys_code      tqc_alerts.alrts_sys_code%TYPE,
      alrts_agn_code      tqc_alerts.alrts_agn_code%TYPE,
      alrts_clnt_code     tqc_alerts.alrts_clnt_code%TYPE,
      alrts_description   tqc_alerts.alrts_description%TYPE,
      alrts_date          tqc_alerts.alrts_date%TYPE,
      alrts_period        tqc_alerts.alrts_period%TYPE,
      alrts_user_code     tqc_alerts.alrts_user_code%TYPE,
      alrts_dest_type     tqc_alerts.alrts_dest_type%TYPE,
      alrts_dest_code     tqc_alerts.alrts_dest_code%TYPE,
      alrts_msgt_code     tqc_alerts.alrts_msgt_code%TYPE,
      agency_name         VARCHAR2 (200),
      msgt_sht_desc       VARCHAR2 (200),
      acc_type_name       VARCHAR2 (200),
      alrts_status        tqc_alerts.alrts_status%TYPE,
      alrts_sht_desc      tqc_alerts.alrts_sht_desc%TYPE
   );

   TYPE alert_ref IS REF CURSOR
      RETURN alert_rec;

   PROCEDURE get_alert (v_alert_code IN NUMBER, v_refcur OUT alert_ref);

   FUNCTION getagency_name (v_agn_code NUMBER, v_acc_type VARCHAR2)
      RETURN VARCHAR2;

   FUNCTION account_type_name (v_sht_desc VARCHAR2)
      RETURN VARCHAR2;

   TYPE alertmsg_rec IS RECORD (
      clnt_sht_desc      tqc_clients.clnt_sht_desc%TYPE,
      clnt_name          tqc_clients.clnt_name%TYPE,
      clnt_dob           tqc_clients.clnt_dob%TYPE,
      clnt_email_addrs   tqc_clients.clnt_email_addrs%TYPE,
      msgt_msg           tqc_msg_templates.msgt_msg%TYPE,
      alrt_type          tqc_alert_types.alrt_type%TYPE,
      usr_name           tqc_users.usr_name%TYPE,
      usr_email          tqc_users.usr_email%TYPE,
      param              tqc_parameters.param_value%TYPE
   );

   TYPE alertmsg_ref IS REF CURSOR
      RETURN alertmsg_rec;

   FUNCTION getbirthdayalerts (v_sht_desc tqc_alerts.alrts_sht_desc%TYPE)
      RETURN alertmsg_ref;

   TYPE stockalertmsg_rec IS RECORD (
      itm_description   VARCHAR2 (50),
      itm_max_level     NUMBER,
      itm_min_level     NUMBER,
      itm_quantity      NUMBER,
      unt_description   VARCHAR2 (50),
      msgt_msg          tqc_msg_templates.msgt_msg%TYPE,
      alrt_type         tqc_alert_types.alrt_type%TYPE,
      usr_name          tqc_users.usr_name%TYPE,
      usr_email         tqc_users.usr_email%TYPE,
      param             tqc_parameters.param_value%TYPE
   );

   TYPE stockalertmsg_ref IS REF CURSOR
      RETURN stockalertmsg_rec;

   FUNCTION getstockalerts (
      v_sht_desc    tqc_alerts.alrts_sht_desc%TYPE,
      v_item_code   NUMBER
   )
      RETURN stockalertmsg_ref;

   PROCEDURE updatesmsmessage (
      v_smscode      NUMBER,
      v_clientcode   NUMBER,
      v_telno        VARCHAR2,
      v_message      VARCHAR2,
      v_usercode     NUMBER
   );

   PROCEDURE sendserviceprov (
      v_add_edit               IN   VARCHAR2,
      v_alrt_code              IN   NUMBER,
      v_alrt_type              IN   VARCHAR,
      v_alrt_sys_code          IN   NUMBER,
      v_alrt_email             IN   VARCHAR,
      v_alrt_sms               IN   VARCHAR,
      v_alrt_screen            IN   VARCHAR,
      v_alrt_email_msgt_code   IN   NUMBER,
      v_alrt_sms_msgt_code     IN   NUMBER,
      v_alrt_grp_usr_code      IN   NUMBER,
      v_alrt_check_alert       IN   VARCHAR2,
      v_shtdesc                IN   VARCHAR2
   );

   TYPE alerts_rec IS RECORD (
      alrts_description   tqc_alerts.alrts_description%TYPE
   );

   TYPE alerts_ref IS REF CURSOR
      RETURN alerts_rec;

   FUNCTION getnewalerts (v_alrts_prepared_by IN VARCHAR2, v_cnt OUT NUMBER)
      RETURN alerts_ref;

   PROCEDURE updatealerts (v_alrts_prepared_by IN VARCHAR2);

   PROCEDURE updateviewedalerts (
      v_alrts_prepared_by   IN   VARCHAR2,
      v_status              IN   VARCHAR2
   );

   TYPE alluseralerts_rec IS RECORD (
      alrts_code            tqc_alerts.alrts_code%TYPE,
      alrts_alrt_code       tqc_alerts.alrts_alrt_code%TYPE,
      agn_name              tqc_agencies.agn_name%TYPE,
      clnt_name             tqc_clients.clnt_name%TYPE,
      alrts_description     tqc_alerts.alrts_description%TYPE,
      alrts_date            tqc_alerts.alrts_date%TYPE,
      alrts_period          tqc_alerts.alrts_period%TYPE,
      alrts_status          tqc_alerts.alrts_status%TYPE,
      msgt_msg              tqc_msg_templates.msgt_msg%TYPE,
      alrts_screen          tqc_alerts.alrts_screen%TYPE,
      alrts_prepared_by     tqc_alerts.alrts_prepared_by%TYPE,
      alrts_prepare_date    tqc_alerts.alrts_prepare_date%TYPE,
      alrts_quot_no         tqc_alerts.alrts_quot_no%TYPE,
      alrts_pol_policy_no   tqc_alerts.alrts_pol_policy_no%TYPE,
      alrts_claim_no        tqc_alerts.alrts_claim_no%TYPE
   );

   TYPE alluseralerts_ref IS REF CURSOR
      RETURN alluseralerts_rec;

   FUNCTION getalluseralerts (v_alrts_prepared_by IN VARCHAR2)
      RETURN alluseralerts_ref;

   PROCEDURE updateviewedalerts (
      v_alrts_prepared_by   IN   VARCHAR2,
      v_status              IN   VARCHAR2,
      v_alrts_code          IN   NUMBER
   );

   PROCEDURE sendprovsms (
      v_sms_sys_code      IN       NUMBER,
      v_sms_sys_module    IN       VARCHAR2,
      v_sms_clnt_code     IN       NUMBER,
      v_sms_agn_code      IN       NUMBER,
      v_sms_quot_code     IN       NUMBER,
      v_sms_pol_code      IN       NUMBER,
      v_sms_pol_no        IN       VARCHAR2,
      v_sms_clm_no        IN       VARCHAR2,
      v_sms_tel_no        IN       VARCHAR2,
      v_sms_msg           IN       VARCHAR2,
      v_sms_status        IN       VARCHAR2,
      v_sms_prepared_by   IN       VARCHAR2,
      v_sms_quot_no       IN       VARCHAR2,
      v_smscode           OUT      NUMBER
   );
  PROCEDURE send_instant_sms(v_sms_code NUMBER);
PROCEDURE create_telematic_sms_msg (
      v_reciepient   IN       VARCHAR2,
      v_clnt_code    IN       NUMBER,
      v_agn_code     IN       NUMBER,
      v_quot_code    IN       NUMBER,
      v_quot_no      IN       VARCHAR2,
      v_pol_code     IN       NUMBER,
      v_pol_no       IN       VARCHAR2,
      v_clm_no       IN       VARCHAR2,
      v_sms_msg      IN       VARCHAR2,
      v_sys_code     IN       NUMBER,
      v_sys_mod      IN       VARCHAR2,
      v_user         IN       VARCHAR2,
      v_sms_code     IN OUT   NUMBER,
      v_usr_code     IN       NUMBER
   );
END tqc_sms_pkg;
/