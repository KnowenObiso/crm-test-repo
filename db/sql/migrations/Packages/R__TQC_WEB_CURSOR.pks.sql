--
-- TQC_WEB_CURSOR  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_web_cursor
AS
   /******************************************************************************
      NAME:       TQC_WEB_CURSOR
      PURPOSE:

      REVISIONS:
      Ver        Date        Author           Description
      ---------  ----------  ---------------  ------------------------------------
      1.0        30/Nov/2009             1. Created this package.
   ******************************************************************************/
   TYPE msgtemplates_rec IS RECORD (
      msgt_code         tqc_msg_templates.msgt_code%TYPE,
      msgt_sht_desc     tqc_msg_templates.msgt_sht_desc%TYPE,
      msgt_msg          tqc_msg_templates.msgt_msg%TYPE,
      msgt_sys_code     tqc_msg_templates.msgt_sys_code%TYPE,
      msgt_sys_module   tqc_msg_templates.msgt_sys_module%TYPE,
      msgt_type         tqc_msg_templates.msgt_type%TYPE
   );

   TYPE msgtemplates_ref IS REF CURSOR
      RETURN msgtemplates_rec;

   FUNCTION getmsgtemplate (
      v_msgshtdesc   IN   tqc_msg_templates.msgt_sht_desc%TYPE
   )
      RETURN msgtemplates_ref;

   PROCEDURE messagetemplates (
      v_msgtmplatecur   OUT   msgtemplates_ref,
      v_syscode               NUMBER,
      v_sysmodule             VARCHAR2,
      v_msgtype               VARCHAR2,
      v_msgshtdesc            VARCHAR2 DEFAULT NULL
   );

   TYPE users_rec IS RECORD (
      usr_code            tqc_users.usr_code%TYPE,
      usr_username        tqc_users.usr_username%TYPE,
      usr_name            tqc_users.usr_name%TYPE,
      usr_email           tqc_users.usr_email%TYPE,
      usr_personel_rank   tqc_users.usr_personel_rank%TYPE,
      usr_dt_created      tqc_users.usr_dt_created%TYPE,
      usr_type            tqc_users.usr_type%TYPE,
      usr_status          tqc_users.usr_status%TYPE,
      usr_pwd_reset       tqc_users.usr_pwd_reset%TYPE
   );

   TYPE users_ref IS REF CURSOR
      RETURN users_rec;

   FUNCTION get_users
      RETURN users_ref;

   FUNCTION get_activeusers
      RETURN users_ref;

   FUNCTION get_escalationuser
      RETURN tqc_users.usr_code%TYPE;

   TYPE tasks_rec IS RECORD (
      tqsbt_sys_code       tqc_system_bpm_tickets.tqsbt_sys_code%TYPE,
      tqsbt_sys_module     tqc_system_bpm_tickets.tqsbt_sys_module%TYPE,
      tqsbt_execution_id   tqc_system_bpm_tickets.tqsbt_execution_id%TYPE,
      tqsbt_ticket_no      tqc_system_bpm_tickets.tqsbt_ticket_no%TYPE,
      tqsbt_ticket_code    tqc_system_bpm_tickets.tqsbt_ticket_code%TYPE,
      tqsbt_ticket_desc    tqc_system_bpm_tickets.tqsbt_ticket_desc%TYPE,
      creation_date        jbpm4_task.create_%TYPE,
      usr_username         tqc_users.usr_username%TYPE,
      priority_            jbpm4_task.priority_%TYPE
   );

   TYPE tasks_ref IS REF CURSOR
      RETURN tasks_rec;

   FUNCTION gettasks (v_usrcode IN jbpm4_task.assignee_%TYPE)
      RETURN tasks_ref;

   FUNCTION getalltasks (v_usrcode IN jbpm4_task.assignee_%TYPE)
      RETURN tasks_ref;

   FUNCTION getticketcodedetails (v_tcktcode VARCHAR2, v_type VARCHAR2)
      RETURN tasks_ref;

   TYPE correspondence_dtls_rec IS RECORD (
      corr_type             VARCHAR2 (15),
      client_name           VARCHAR2 (250),
      contact               VARCHAR2 (100),
      subject               tqc_email_messages.email_subj%TYPE,
      MESSAGE               tqc_email_messages.email_msg%TYPE,
      status                VARCHAR2 (15),
      email_prepared_by     tqc_email_messages.email_prepared_by%TYPE,
      email_prepared_date   tqc_email_messages.email_prepared_date%TYPE,
      email_send_date       tqc_email_messages.email_send_date%TYPE
   );

   TYPE correspendence_dtls_ref IS REF CURSOR
      RETURN correspondence_dtls_rec;

   FUNCTION getcorrespondences (v_telquotecode NUMBER)
      RETURN correspendence_dtls_ref;

   TYPE countries_rec IS RECORD (
      cou_code          tqc_countries.cou_code%TYPE,
      cou_sht_desc      tqc_countries.cou_sht_desc%TYPE,
      cou_name          tqc_countries.cou_name%TYPE,
      cou_base_curr     tqc_countries.cou_base_curr%TYPE,
      cou_nationality   tqc_countries.cou_nationality%TYPE,
      cou_zip_code      tqc_countries.cou_zip_code%TYPE
   );

   TYPE countries_ref IS REF CURSOR
      RETURN countries_rec;

   --PROCEDURE getcountries(v_refcur OUT countries_ref);
   TYPE cal_activites_rec IS RECORD (
      cala_location       tqc_calendar_activities.cala_location%TYPE,
      cala_title          tqc_calendar_activities.cala_title%TYPE,
      cala_startadddays   tqc_calendar_activities.cala_startadddays%TYPE,
      cala_starthour      tqc_calendar_activities.cala_starthour%TYPE,
      cala_startmin       tqc_calendar_activities.cala_startmin%TYPE,
      cala_endadddays     tqc_calendar_activities.cala_endadddays%TYPE,
      cala_endaddmin      tqc_calendar_activities.cala_endaddmin%TYPE
   );

   TYPE cal_activites_ref IS REF CURSOR
      RETURN cal_activites_rec;

   --PROCEDURE getcalendar_activities(v_refcur OUT cal_activites_ref);
   TYPE org_division_rec IS RECORD (
      v_div_name       tqc_divisions.div_name%TYPE,
      v_div_sht_desc   tqc_divisions.div_sht_desc%TYPE
   );

   TYPE org_divisions_cur IS REF CURSOR
      RETURN org_division_rec;

   PROCEDURE getorgdivision (
      v_div_code           IN       NUMBER,
      v_org_division_cur   OUT      org_divisions_cur
   );

   TYPE org_sub_divisions_rec IS RECORD (
      v_sdiv_name       tqc_subdivisions.sdiv_name%TYPE,
      v_sdiv_sht_desc   tqc_subdivisions.sdiv_sht_desc%TYPE
   );

   TYPE org_sub_divisions_cur IS REF CURSOR
      RETURN org_sub_divisions_rec;

   PROCEDURE getorgsubdivision (
      v_sdiv_code                   NUMBER,
      v_org_subdivision_cur   OUT   org_sub_divisions_cur
   );

   TYPE report_dtls_rec IS RECORD (
      rpt_code              tqc_system_reports.rpt_code%TYPE,
      rpt_name              tqc_system_reports.rpt_name%TYPE,
      rpt_data_file         tqc_system_reports.rpt_data_file%TYPE,
      rpt_tmpl_code         tqc_sys_rpt_templates.rpt_tmpl_code%TYPE,
      rpt_tmpl_file         tqc_sys_rpt_templates.rpt_tmpl_file%TYPE,
      rpt_tmpl_name         tqc_sys_rpt_templates.rpt_tmpl_name%TYPE,
      rpt_tmpl_style_file   tqc_sys_rpt_templates.rpt_tmpl_style_file%TYPE,
      rpt_prnt_srv_appl     tqc_system_reports.rpt_prnt_srv_appl%TYPE
   );

   TYPE report_dtls_ref IS REF CURSOR
      RETURN report_dtls_rec;

   FUNCTION getrptdetails (
      v_rptcode   tqc_system_reports.rpt_code%TYPE,
      v_orgcode   tqc_sys_rpt_templates.rpt_tmpl_org_code%TYPE DEFAULT NULL
   )
      RETURN report_dtls_ref;

   TYPE divisions_rec IS RECORD (
      div_code                tqc_divisions.div_code%TYPE,
      div_name                tqc_divisions.div_name%TYPE,
      div_sht_desc            tqc_divisions.div_sht_desc%TYPE,
      div_division_status     tqc_divisions.div_division_status%TYPE,
      div_order               tqc_divisions.div_order%TYPE,
      div_director_code       NUMBER (8),
      div_director            tqc_divisions.div_director%TYPE,
      div_ass_director_code   NUMBER (8),
      div_asst_director       tqc_divisions.div_asst_director%TYPE
   );

   TYPE divisions_ref IS REF CURSOR
      RETURN divisions_rec;

   FUNCTION getdivisions (v_div_code tqc_divisions.div_code%TYPE)
      RETURN divisions_ref;

   FUNCTION get_org_divisions (v_org_code tqc_organizations.org_code%TYPE)
      RETURN divisions_ref;

   TYPE sub_divisions_rec IS RECORD (
      sdiv_code       tqc_subdivisions.sdiv_code%TYPE,
      sdiv_name       tqc_subdivisions.sdiv_name%TYPE,
      sdiv_sht_desc   tqc_subdivisions.sdiv_sht_desc%TYPE,
      sdiv_div_code   tqc_subdivisions.sdiv_div_code%TYPE
   );

   TYPE sub_divisions_ref IS REF CURSOR
      RETURN sub_divisions_rec;

   FUNCTION get_brn_unassgnd_div (
      v_org_code        tqc_organizations.org_code%TYPE,
      v_brn_code   IN   tqc_branches.brn_code%TYPE
   )
      RETURN divisions_ref;

   FUNCTION getsubdivisions (v_sdiv_code tqc_subdivisions.sdiv_code%TYPE)
      RETURN sub_divisions_ref;

   TYPE user_divisions_rec IS RECORD (
      usd_code              tqc_user_divisions.usd_code%TYPE,
      usd_usr_code          tqc_user_divisions.usd_usr_code%TYPE,
      usd_div_code          tqc_user_divisions.usd_div_code%TYPE,
      div_code              tqc_divisions.div_code%TYPE,
      div_name              tqc_divisions.div_name%TYPE,
      div_sht_desc          tqc_divisions.div_sht_desc%TYPE,
      div_division_status   tqc_divisions.div_division_status%TYPE,
      usd_default           tqc_user_divisions.usd_default%TYPE
   );

   TYPE user_divisions_ref IS REF CURSOR
      RETURN user_divisions_rec;

   FUNCTION get_user_divisions (
      v_user_code   tqc_user_divisions.usd_usr_code%TYPE
   )
      RETURN user_divisions_ref;

   TYPE branch_divisions_rec IS RECORD (
      bdiv_code   tqc_brnch_divisions.bdiv_code%TYPE,
      brn_name    VARCHAR2 (100),
      div_name    VARCHAR2 (100),
      bdiv_wef    tqc_brnch_divisions.bdiv_wef%TYPE,
      bdiv_wet    tqc_brnch_divisions.bdiv_wet%TYPE,
      div_code    tqc_divisions.div_code%TYPE
   );

   TYPE branch_divisions_ref IS REF CURSOR
      RETURN branch_divisions_rec;

   FUNCTION getbranchdivisions (
      v_div_code   tqc_brnch_divisions.bdiv_div_code%TYPE
   )
      RETURN branch_divisions_ref;

   FUNCTION getdivisionsbybrnch (
      v_brn_code        tqc_brnch_divisions.bdiv_div_code%TYPE,
      v_usercode   IN   NUMBER DEFAULT NULL
   )
      RETURN branch_divisions_ref;

  TYPE req_docs_rec IS RECORD (
      rdoc_code        tqc_required_documents.rdoc_code%TYPE,
      rdoc_sht_desc    VARCHAR2 (100),
      roc_desc         VARCHAR2 (100),
      roc_mandatory    tqc_required_documents.roc_mandatory%TYPE,
      roc_date_added   tqc_required_documents.roc_date_added%TYPE,
      ROC_ACCOUNT_TYPE  VARCHAR2 (1)
   );

   TYPE req_docs_ref IS REF CURSOR
      RETURN req_docs_rec;

    FUNCTION getrequireddocs
      RETURN req_docs_ref;

   TYPE client_doc_rec IS RECORD (
      cdocr_code           tqc_client_documents.cdocr_code%TYPE,
      cdocr_rdoc_code      tqc_client_documents.cdocr_rdoc_code%TYPE,
      cdocr_clnt_code      tqc_client_documents.cdocr_clnt_code%TYPE,
      cdocr_submited       tqc_client_documents.cdocr_submited%TYPE,
      cdocr_date_s         tqc_client_documents.cdocr_date_s%TYPE,
      cdocr_ref_no         tqc_client_documents.cdocr_ref_no%TYPE,
      cdocr_rmrk           tqc_client_documents.cdocr_rmrk%TYPE,
      cdocr_user_receivd   tqc_client_documents.cdocr_user_receivd%TYPE,
      roc_desc             VARCHAR2 (100),
      cdocr_docid          tqc_client_documents.cdocr_docid%TYPE
   );

   TYPE client_doc_ref IS REF CURSOR
      RETURN client_doc_rec;


     FUNCTION get_client_docs (
      v_clnt_code   tqc_client_documents.cdocr_clnt_code%TYPE
   )
      RETURN client_doc_ref;

   TYPE acc_mngr_rec IS RECORD (
      usr_username   tqc_users.usr_username%TYPE,
      usr_name       tqc_users.usr_name%TYPE,
      usr_code       tqc_users.usr_code%TYPE
   );

   TYPE acc_mngr_ref IS REF CURSOR
      RETURN acc_mngr_rec;

   FUNCTION get_acc_mangrs
      RETURN acc_mngr_ref;

   TYPE user_div_rec IS RECORD (
      usd_code       tqc_user_divisions.usd_code%TYPE,
      usd_usr_code   tqc_user_divisions.usd_usr_code%TYPE,
      usd_div_code   tqc_user_divisions.usd_div_code%TYPE,
      usd_default    tqc_user_divisions.usd_default%TYPE
   );

   TYPE user_div_ref IS REF CURSOR
      RETURN user_div_rec;

   PROCEDURE grant_user_division (
      v_code        IN   tqc_user_divisions.usd_code%TYPE,
      v_user_code   IN   tqc_user_divisions.usd_usr_code%TYPE,
      v_div_code    IN   tqc_user_divisions.usd_div_code%TYPE,
      v_default     IN   tqc_user_divisions.usd_default%TYPE
   );

   PROCEDURE revoke_user_division (
      v_user_code   IN   tqc_user_divisions.usd_usr_code%TYPE,
      v_div_code    IN   tqc_user_divisions.usd_div_code%TYPE
   );

   TYPE currencies_rec IS RECORD (
      cur_code     tqc_currencies.cur_code%TYPE,
      cur_symbol   tqc_currencies.cur_symbol%TYPE,
      cur_desc     tqc_currencies.cur_desc%TYPE,
      cur_rnd      tqc_currencies.cur_rnd%TYPE
   );

   TYPE currencies_ref IS REF CURSOR
      RETURN currencies_rec;

   FUNCTION getcurrencies
      RETURN currencies_ref;

   FUNCTION getsysdateformat
      RETURN VARCHAR2;

   FUNCTION getminimumdecimalplaces
      RETURN VARCHAR2;

   FUNCTION getmaximumdecimalplaces
      RETURN VARCHAR2;

   FUNCTION getsystemplate (v_syscode NUMBER)
      RETURN VARCHAR2;

   FUNCTION getsysorgcode (v_syscode NUMBER)
      RETURN NUMBER;

   FUNCTION getuserbyusercode (v_usercode NUMBER)
      RETURN VARCHAR2;

   FUNCTION getrptdetailsschedules (v_prod_code IN NUMBER)
      RETURN report_dtls_ref;

   TYPE email_attachment_rec IS RECORD (
      eatt_code       tqc_email_attachments.eatt_code%TYPE,
      eatt_filename   tqc_email_attachments.eatt_filename%TYPE
   );

   TYPE email_attachment_ref IS REF CURSOR
      RETURN email_attachment_rec;

   PROCEDURE getemailattachments (
      v_cursor   OUT      email_attachment_ref,
      v_code     IN       NUMBER
   );

   FUNCTION get_mail_from
      RETURN VARCHAR2;

   FUNCTION get_mail_host
      RETURN VARCHAR2;

   FUNCTION get_mail_port
      RETURN VARCHAR2;

   PROCEDURE getusername (
      v_usercode   IN       tqc_users.usr_code%TYPE,
      v_username   OUT      tqc_users.usr_username%TYPE,
      v_lastdate   OUT      tqc_users.usr_last_date%TYPE
   );

   TYPE required_docs_rec IS RECORD (
      rdoc_code        tqc_required_documents.rdoc_code%TYPE,
      rdoc_sht_desc    VARCHAR2 (100),
      roc_desc         VARCHAR2 (100),
      roc_mandatory    tqc_required_documents.roc_mandatory%TYPE,
      roc_date_added   tqc_required_documents.roc_date_added%TYPE,
      roc_exempted     tqc_required_documents.roc_exempted%TYPE
   );

   TYPE required_docs_ref IS REF CURSOR
      RETURN required_docs_rec;

   FUNCTION get_user_email (v_code IN NUMBER)
      RETURN VARCHAR2;

   FUNCTION get_ticket_name (v_code IN NUMBER)
      RETURN VARCHAR2;

   FUNCTION check_ticket_status (v_code IN VARCHAR2, v_level IN VARCHAR2)
      RETURN VARCHAR2;

   TYPE products_rec IS RECORD (
      prod_code       NUMBER (15),
      prod_sht_desc   VARCHAR2 (20),
      prod_desc       VARCHAR2 (100),
      prod_type       VARCHAR2 (10)
   );

   TYPE products_ref IS REF CURSOR
      RETURN products_rec;

   FUNCTION products (v_syscode NUMBER, v_clatype VARCHAR2 DEFAULT NULL)
      RETURN products_ref;

   TYPE paymthdmode_rec IS RECORD (
      pay_id     VARCHAR2 (5),
      pay_desc   VARCHAR2 (20)
   );

   TYPE paymthdmode_ref IS REF CURSOR
      RETURN paymthdmode_rec;

   FUNCTION paymthdmode (v_type VARCHAR2)
      RETURN paymthdmode_ref;

   TYPE report_params_rec IS RECORD (
      rptp_code            tqc_sys_rpt_parameters.rptp_code%TYPE,
      rptp_param_prompt    tqc_sys_rpt_parameters.rptp_param_prompt%TYPE,
      rptp_param_type      tqc_sys_rpt_parameters.rptp_param_type%TYPE,
      rptp_parent_code     tqc_sys_rpt_parameters.rptp_parent_code%TYPE,
      rptp_query           tqc_sys_rpt_parameters.rptp_query%TYPE,
      rptp_param_name      tqc_sys_rpt_parameters.rptp_param_name%TYPE,
      rptp_param_clause    tqc_sys_rpt_parameters.rptp_param_clause%TYPE,
      rptp_user_required   tqc_sys_rpt_parameters.rptp_user_required%TYPE,
      rptp_child_code      tqc_sys_rpt_parameters.rptp_child_code%TYPE
   );

   TYPE report_params_ref IS REF CURSOR
      RETURN report_params_rec;

   PROCEDURE get_report_parameters (
      v_report_params_ref   OUT   report_params_ref,
      v_rpt_code                  NUMBER,
      v_param_type                VARCHAR2,
      v_parent_code               NUMBER
   );

   FUNCTION get_report_param_qry (v_rpt_code NUMBER, v_param_code IN VARCHAR2)
      RETURN VARCHAR2;

   FUNCTION getrptdetailsschedulesren (v_prod_code IN NUMBER)
      RETURN report_dtls_ref;

   TYPE report_dtls IS RECORD (
      rpt_code              tqc_system_reports.rpt_code%TYPE,
      rpt_name              tqc_system_reports.rpt_name%TYPE,
      rpt_data_file         tqc_system_reports.rpt_data_file%TYPE,
      rpt_tmpl_code         tqc_sys_rpt_templates.rpt_tmpl_code%TYPE,
      rpt_tmpl_file         tqc_sys_rpt_templates.rpt_tmpl_file%TYPE,
      rpt_tmpl_name         tqc_sys_rpt_templates.rpt_tmpl_name%TYPE,
      rpt_tmpl_style_file   tqc_sys_rpt_templates.rpt_tmpl_style_file%TYPE
   );

   TYPE report_certs_ref IS REF CURSOR
      RETURN report_dtls;

   FUNCTION getrptdetailscertificates (v_prod_code IN NUMBER)
      RETURN report_certs_ref;

   FUNCTION getrptdetailsendosment (v_prod_code IN NUMBER)
      RETURN report_dtls_ref;

   FUNCTION getrptdetailspoldoc (v_prod_code IN NUMBER)
      RETURN report_dtls_ref;

   FUNCTION getrptdetailsschedulesendors (v_prod_code IN NUMBER)
      RETURN report_dtls_ref;

   FUNCTION getrptdetail (
      v_rptcode   tqc_system_reports.rpt_code%TYPE,
      v_orgcode   tqc_sys_rpt_templates.rpt_tmpl_org_code%TYPE DEFAULT NULL
   )
      RETURN report_dtls_ref;

   PROCEDURE get_clients_details (
      v_cmb_claim_no    IN       VARCHAR2,
      v_email_address   OUT      VARCHAR2,
      v_message         OUT      VARCHAR2
   );

   TYPE dmsreport_rec IS RECORD (
      sprr_rpt_code   tqc_sys_process_reports.sprr_rpt_code%TYPE
   );

   TYPE dmsreport_ref IS REF CURSOR
      RETURN dmsreport_rec;

   PROCEDURE dmsreports (
      v_dmsreport_ref   OUT   dmsreport_ref,
      v_prodcode              NUMBER DEFAULT NULL,
      v_param_val             VARCHAR2 DEFAULT NULL,
      v_syscode               NUMBER,
      v_process               VARCHAR2
   );

   TYPE schedule_details_rec IS RECORD (
      screen_show_default_risks   gin_screens.screen_show_default_risks%TYPE,
      screen_no_of_risks          gin_screens.screen_no_of_risks%TYPE,
      screen_show_si              gin_screens.screen_show_si%TYPE
   );

   TYPE schedule_details_ref IS REF CURSOR
      RETURN schedule_details_rec;

   FUNCTION get_schedule_details (v_prod_code IN NUMBER)
      RETURN schedule_details_ref;

   TYPE report_dtls_ind_rec IS RECORD (
      rpt_code              tqc_system_reports.rpt_code%TYPE,
      rpt_name              tqc_system_reports.rpt_name%TYPE,
      rpt_data_file         tqc_system_reports.rpt_data_file%TYPE,
      rpt_tmpl_code         tqc_sys_rpt_templates.rpt_tmpl_code%TYPE,
      rpt_tmpl_file         tqc_sys_rpt_templates.rpt_tmpl_file%TYPE,
      rpt_tmpl_name         tqc_sys_rpt_templates.rpt_tmpl_name%TYPE,
      rpt_tmpl_style_file   tqc_sys_rpt_templates.rpt_tmpl_style_file%TYPE
   );

   TYPE report_dtls_ind_ref IS REF CURSOR
      RETURN report_dtls_ind_rec;

   FUNCTION getrptdetailsschedulesind (v_prod_code IN NUMBER)
      RETURN report_dtls_ind_ref;

   FUNCTION getrptdtlsendorsschind (v_prod_code IN NUMBER)
      RETURN report_dtls_ind_ref;

   FUNCTION getrptdtlsendorssch (v_prod_code IN NUMBER)
      RETURN report_dtls_ind_ref;
   FUNCTION getrptdetailsschedulesrenwl
      RETURN report_dtls_ref;
    FUNCTION check_rpt_details (
      v_rptcode   tqc_system_reports.rpt_code%TYPE
   )RETURN varchar2;
    FUNCTION get_user_email_from (v_number IN NUMBER, v_module IN VARCHAR2)
   RETURN VARCHAR2;
   FUNCTION get_salvage_user_email_from (v_username IN VARCHAR2)
   RETURN VARCHAR2;
FUNCTION get_agent_docs (
      v_agn_code   tqc_agency_documents.Adocr_agn_code%TYPE
   )
      RETURN client_doc_ref; 
 FUNCTION get_sp_docs (
      v_agn_code   tqc_sp_documents.sdocr_clnt_code%TYPE
   )
      RETURN client_doc_ref;
FUNCTION getagentrequireddocs(v_roc_account_type in varchar2 default null)
      RETURN req_docs_ref;              
END tqc_web_cursor; 
/