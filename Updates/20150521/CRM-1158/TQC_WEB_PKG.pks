CREATE OR REPLACE PACKAGE TQ_CRM.tqc_web_pkg
AS
   TYPE user_auth_roles_rec IS RECORD (
      auth_role_area_id         tqc_user_web_system_auth_roles.usar_sar_code%TYPE,
      auth_role_area_sht_desc   tqc_web_system_auth_areas.saa_sht_desc%TYPE,
      auth_role_area_name       tqc_web_system_auth_areas.saa_name%TYPE,
      auth_role_sys_code        tqc_web_system_auth_areas.saa_sys_code%TYPE,
      auth_role_sys_name        tqc_web_systems.sys_name%TYPE
   );

   TYPE user_auth_roles_ref IS REF CURSOR
      RETURN user_auth_roles_rec;

   FUNCTION user_auth_areas (user_code IN NUMBER, sys_code IN NUMBER)
      RETURN user_auth_roles_ref;

   PROCEDURE user_system_conn (
      v_user                    VARCHAR2,
      v_sys_code           IN   NUMBER,
      v_killopen_session   IN   VARCHAR2 DEFAULT 'N'
   );

   FUNCTION agent_authenticate (vusername IN VARCHAR2, vpwd IN VARCHAR2)
      RETURN INTEGER;

   FUNCTION get_usr_divisions_val (v_usrcode IN NUMBER)
      RETURN VARCHAR2;

   FUNCTION Agent_authenticate(vusername        IN VARCHAR2,
                              vpwd             IN VARCHAR2,
                              vmsg             OUT VARCHAR2,
                              lastlogindate    OUT DATE,
                              agentcode        OUT NUMBER,
                              agentname        OUT VARCHAR2,
                              agentshtdesc     OUT VARCHAR2,
                              agentcontactcode OUT NUMBER,
                              agentcontactname OUT VARCHAR2,
                              v_user_code      OUT NUMBER) RETURN INTEGER;

    FUNCTION agent_authenticate(vusername        IN VARCHAR2,
                              vpwd             IN VARCHAR2,
                              v_pwd_reentry    IN VARCHAR2,
                              v_new_pwd1       IN VARCHAR2,
                              v_new_pwd2       IN VARCHAR2,
                              vmsg             OUT VARCHAR2,
                              lastlogindate    OUT DATE,
                              agentcode        OUT NUMBER,
                              agentname        OUT VARCHAR2,
                              agentshtdesc     OUT VARCHAR2,
                              agentcontactcode OUT NUMBER,
                              agentcontactname OUT VARCHAR2,
                              agentbrncode     OUT NUMBER,
                              v_system_code OUT NUMBER) RETURN INTEGER ;

   FUNCTION client_authenticate (
      vusername          IN       VARCHAR2,
      vpwd               IN       VARCHAR2,
      vmsg               OUT      VARCHAR2,
      lastlogindate      OUT      DATE,
      agentcode          OUT      NUMBER,
      agentname          OUT      VARCHAR2,
      agentcontactcode   OUT      NUMBER,
      agentcontactname   OUT      VARCHAR2
   )
      RETURN INTEGER;

   FUNCTION client_authenticate (
      vusername         IN       VARCHAR2,
      vpwd              IN       VARCHAR2,
      v_pwd_reentry     IN       VARCHAR2,
      v_new_pwd1        IN       VARCHAR2,
      v_new_pwd2        IN       VARCHAR2,
      v_resetcode       IN       VARCHAR2,
      v_msg             OUT      VARCHAR2,
      v_lastlogindate   OUT      DATE,
      v_clientcode      OUT      NUMBER,
      v_clientname      OUT      VARCHAR2,
      v_accountcode     OUT      NUMBER
   )
      RETURN INTEGER;
      
      FUNCTION client_authenticate(vusername IN VARCHAR2,
                              vpwd IN VARCHAR2,
                            v_pwd_reentry IN VARCHAR2,
                            v_new_pwd1 IN VARCHAR2,
                            v_new_pwd2 IN VARCHAR2,
                            v_resetCode IN  VARCHAR2,
                              v_msg OUT VARCHAR2,
                              v_lastlogindate OUT DATE,
                              v_clientCode  OUT NUMBER,
                              v_clientName  OUT VARCHAR2,
                            v_accountCode OUT NUMBER,
                            v_prpCode   OUT NUMBER,
                            v_clientType    OUT VARCHAR2,
                            v_defaultBranch OUT NUMBER) RETURN INTEGER;

   FUNCTION user_authenticate (
      vusername       IN       VARCHAR2,
      vpwd            IN       VARCHAR2,
      vmsg            OUT      VARCHAR2,
      lastlogindate   OUT      DATE,
      usercode        OUT      NUMBER,
      username        OUT      VARCHAR2,
      v_sys_code      IN       NUMBER
   )
      RETURN INTEGER;

   FUNCTION user_authenticate (
      vusername       IN       VARCHAR2,
      vpwd            IN       VARCHAR2,
      v_pwd_reentry   IN       VARCHAR2,
      v_new_pwd1      IN       VARCHAR2,
      v_new_pwd2      IN       VARCHAR2,
      vmsg            OUT      VARCHAR2,
      lastlogindate   OUT      DATE,
      usercode        OUT      NUMBER,
      username        OUT      VARCHAR2,
      v_sys_code      IN       NUMBER
   )
      RETURN INTEGER;

   FUNCTION user_sys_authenticate (
      vusername       IN       VARCHAR2,
      vmsg            OUT      VARCHAR2,
      lastlogindate   OUT      DATE,
      usercode        OUT      NUMBER,
      username        OUT      VARCHAR2,
      v_sys_code      IN       NUMBER
   )
      RETURN INTEGER;

   TYPE usr_branches_rec IS RECORD (
      brn_code          tqc_branches.brn_code%TYPE,
      brn_sht_desc      tqc_branches.brn_sht_desc%TYPE,
      brn_name          tqc_branches.brn_name%TYPE,
      brn_gen_pol_clm   tqc_branches.brn_gen_pol_clm%TYPE
   );

   TYPE usr_branches_ref IS REF CURSOR
      RETURN usr_branches_rec;

   FUNCTION get_usr_branches (v_usrname IN VARCHAR2)
      RETURN usr_branches_ref;

   FUNCTION get_usr_branches (v_usrcode IN NUMBER)
      RETURN usr_branches_ref;

   FUNCTION get_usr_dflt_branch (v_usrname IN VARCHAR2)
      RETURN usr_branches_ref;

   FUNCTION get_usr_dflt_branch (v_usrcode IN NUMBER)
      RETURN usr_branches_ref;

   TYPE usr_divisions_rec IS RECORD (
      usd_div_code   tqc_user_divisions.usd_div_code%TYPE,
      div_sht_desc   tqc_divisions.div_sht_desc%TYPE,
      div_name       tqc_divisions.div_name%TYPE
   );

   TYPE usr_divisions_ref IS REF CURSOR
      RETURN usr_divisions_rec;

   FUNCTION get_dflt_usr_branch_div (v_usrname IN VARCHAR2)
      RETURN usr_divisions_ref;

   FUNCTION get_usr_divisions (v_usrcode IN NUMBER)
      RETURN usr_divisions_ref;

   FUNCTION get_usr_dflt_divisions (v_usrcode IN NUMBER)
      RETURN usr_divisions_ref;

   TYPE currencies_rec IS RECORD (
      cur_code     tqc_currencies.cur_code%TYPE,
      cur_symbol   tqc_currencies.cur_symbol%TYPE
   );

   TYPE currencies_ref IS REF CURSOR
      RETURN currencies_rec;

   FUNCTION get_currencies
      RETURN currencies_ref;

   TYPE process IS RECORD (
      sprc_code        NUMBER,
      sprc_bpm_id      VARCHAR2 (100),
      sprc_sht_desc    VARCHAR2 (100),
      sprc_jpdl_desc   VARCHAR2 (50)
   );

   TYPE process_rec IS REF CURSOR
      RETURN process;

   PROCEDURE get_process_dtls (
      v_syscode       IN       NUMBER,
      v_process       IN       VARCHAR2,
      v_process_cur   OUT      process_rec
   );

   PROCEDURE save_process_attributes (
      v_tckt_code            NUMBER,
      v_sys_code             NUMBER,
      v_sys_module           VARCHAR2,
      v_clnt_code            NUMBER,
      v_agn_code             NUMBER,
      v_pol_code             NUMBER,
      v_pol_no               VARCHAR2,
      v_clm_no               VARCHAR2,
      v_quot_code            NUMBER,
      v_user                 VARCHAR2,
      v_tckt_date            DATE,
      v_process_id           VARCHAR2,
      v_quot_no              VARCHAR2,
      v_endr_code            NUMBER,
      v_prod_type            VARCHAR2,
      v_tckt_to              VARCHAR2,
      v_remarks              VARCHAR2,
      v_endorsment           VARCHAR2,
      v_trans_no             NUMBER,
      v_type            IN   VARCHAR2 DEFAULT 'S',
      v_extern_ref_no   IN   VARCHAR2 DEFAULT NULL,
      v_adhoc_name      IN   VARCHAR2 DEFAULT NULL
   );

   PROCEDURE update_process_attributes (
      v_tckt_code    NUMBER,
      v_sys_code     NUMBER,
      v_sys_module   VARCHAR2,
      v_clnt_code    NUMBER,
      v_agn_code     NUMBER,
      v_pol_code     NUMBER,
      v_pol_no       VARCHAR2,
      v_clm_no       VARCHAR2,
      v_quot_code    NUMBER,
      v_user         VARCHAR2,
      v_tckt_date    DATE,
      v_process_id   VARCHAR2,
      v_quot_no      VARCHAR2,
      v_endr_code    NUMBER,
      v_prod_type    VARCHAR2,
      v_tckt_to      VARCHAR2,
      v_remarks      VARCHAR2,
      v_endorsment   VARCHAR2,
      v_trans_no     NUMBER
   );
FUNCTION check_agent_pwd(v_entered_user    IN VARCHAR2,
                           v_entered_pwd     IN VARCHAR2,
                           v_msg             OUT VARCHAR2,
                           v_last_login_date OUT DATE,
                           agentcode         OUT NUMBER,
                           agentname         OUT VARCHAR2,
                           agentshtdesc      OUT VARCHAR2,
                           agentcontactcode  OUT NUMBER,
                           agentcontactname  OUT VARCHAR2,
                           v_new_pwd1        IN VARCHAR2,
                           v_new_pwd2        IN VARCHAR2,
                           v_new_pwd_entry   IN VARCHAR2 DEFAULT 'N',
                           agentbrncode      OUT NUMBER,
                           v_user_code       OUT NUMBER,
                           v_accc_sys_code OUT NUMBER) RETURN NUMBER;
                           

   PROCEDURE reassign_task (
      v_taskid    IN   VARCHAR2,
      v_user      IN   VARCHAR2,
      v_remarks        VARCHAR2
   );

   PROCEDURE check_task_completion (
      v_task_point   IN       VARCHAR2,
      v_taskid       IN       VARCHAR2,
      v_sys_code     IN       NUMBER,
      v_complete     OUT      VARCHAR2
   );

   TYPE tickets_rec IS RECORD (
      tckt_code                  NUMBER,
      usrsystem                  VARCHAR (50),
      sysmodule                  VARCHAR (50),
      tckt_clnt_code             NUMBER,
      client                     VARCHAR (100),
      tckt_agn_code              NUMBER,
      AGENT                      VARCHAR (100),
      tckt_pol_code              NUMBER,
      tckt_pol_no                VARCHAR (50),
      tckt_clm_no                VARCHAR (50),
      tckt_quot_code             NUMBER,
      quo_no                     VARCHAR (50),
      tckt_by                    VARCHAR (50),
      tckt_date                  DATE,
      tckt_process_id            VARCHAR2 (100),
      sys_module_code            VARCHAR2 (10),
      tckt_endr_code             NUMBER,
      tckt_prod_type             VARCHAR2 (20),
      tckt_to                    VARCHAR2 (100),
      tckt_remarks               VARCHAR2 (300),
      tckt_name                  VARCHAR2 (100),
      tckt_due_date              DATE,
      tckt_endorsement           VARCHAR2 (25),
      tckt_transno               NUMBER,
      ref_no                     VARCHAR2 (100),
      tckt_prp_code              NUMBER,
      pol_client_policy_number   VARCHAR2 (30),
      tckt_type                  VARCHAR2 (1),
      POL_POLICY_STATUS        GIN_POLICIES.POL_POLICY_STATUS%TYPE,
      TCKT_TRAN_EFF_DATE       DATE,
      TCKT_GGT_NO              NUMBER,
      USR_TYPE                 VARCHAR2(1),
      tckt_assignment_date        DATE
   );

   TYPE tickets_ref IS REF CURSOR
      RETURN tickets_rec;

   TYPE country_states IS RECORD (
      sts_code       tqc_states.sts_code%TYPE,
      sts_cou_code   tqc_states.sts_cou_code%TYPE,
      sts_sht_desc   tqc_states.sts_sht_desc%TYPE,
      sts_name       tqc_states.sts_name%TYPE
   );

   TYPE country_states_rec IS REF CURSOR
      RETURN country_states;

   FUNCTION get_tckt_dtls_by_usr (
      v_user       IN   VARCHAR2,
      v_quo_code        NUMBER DEFAULT NULL,
      v_pol_code        NUMBER DEFAULT NULL,
      v_claim_no        VARCHAR2 DEFAULT NULL,
      v_syscode         NUMBER DEFAULT NULL,
      v_cla_type        VARCHAR2 DEFAULT NULL,
        v_tkt_type VARCHAR2 DEFAULT NULL
   )
      RETURN tickets_ref;

   FUNCTION get_tckt_dtls_by_usr_life (
      v_user       IN   VARCHAR2,
      v_quo_code        NUMBER DEFAULT NULL,
      v_pol_code        NUMBER DEFAULT NULL,
      v_claim_no        VARCHAR2 DEFAULT NULL,
      v_syscode         NUMBER DEFAULT NULL
   )
      RETURN tickets_ref;

   FUNCTION get_tckt_dtls (v_tckt_code IN NUMBER)
      RETURN tickets_ref;

   PROCEDURE get_bpm_task_srn (
      v_taskname    IN       VARCHAR,
      v_scrn_name   OUT      VARCHAR2,
      v_sys_code             NUMBER
   );

   PROCEDURE update_cal_activity (
      v_loc         VARCHAR2,
      v_title       VARCHAR2,
      v_today       VARCHAR2,
      v_startdate   VARCHAR2,
      v_enddate     VARCHAR2,
      v_user        VARCHAR2
   );

   PROCEDURE conv_proposer_to_clnt (
      v_clnt_code   IN       NUMBER,
      v_sht_desc    OUT      VARCHAR2,
      v_name        OUT      VARCHAR2,
      v_err         OUT      VARCHAR2
   );

   PROCEDURE find_states (v_states OUT country_states_rec, v_cou_code NUMBER);

   PROCEDURE inactivate_tickets (v_endorsement NUMBER);

   PROCEDURE save_workflw_attributes (
      v_tckt_code     NUMBER,
      v_sys_code      NUMBER,
      v_sys_module    VARCHAR2,
      v_clnt_code     NUMBER,
      v_agn_code      NUMBER,
      v_pol_code      NUMBER,
      v_pol_no        VARCHAR2,
      v_clm_no        VARCHAR2,
      v_quot_code     NUMBER,
      v_user          VARCHAR2,
      v_tckt_date     DATE,
      v_process_id    VARCHAR2,
      v_quot_no       VARCHAR2,
      v_endr_code     NUMBER,
      v_prod_type     VARCHAR2,
      v_tckt_to       VARCHAR2,
      v_remarks       VARCHAR2,
      v_endorsment    VARCHAR2,
      v_trans_no      NUMBER,
      v_prp_code      NUMBER DEFAULT NULL,
      v_cla_type      VARCHAR2 DEFAULT NULL,
      v_tckt_cqr_no   VARCHAR2 DEFAULT NULL
   );

   FUNCTION get_default_usr_branch (v_usrname IN VARCHAR2, v_sys_code IN NUMBER)
      RETURN usr_branches_ref;

   FUNCTION check_user_pwd (
      v_entered_user      IN       VARCHAR2,
      v_entered_pwd       IN       VARCHAR2,
      v_msg               OUT      VARCHAR2,
      v_last_login_date   OUT      DATE,
      usercode            OUT      NUMBER,
      username            OUT      VARCHAR2,
      v_new_pwd1          IN       VARCHAR2,
      v_new_pwd2          IN       VARCHAR2,
      v_new_pwd_entry     IN       VARCHAR2 DEFAULT 'N',
      v_sys_code          IN       NUMBER
   )
      RETURN NUMBER;

   PROCEDURE populaterequireddocuments (v_clnt_code NUMBER, v_user VARCHAR2);

   v_error   tqc_error_manager.error_info_rt;

   TYPE sys_users IS RECORD (
      usr_code       tqc_users.usr_code%TYPE,
      usr_username   tqc_users.usr_username%TYPE,
      usr_name       tqc_users.usr_name%TYPE
   );

   TYPE sys_users_rec IS REF CURSOR
      RETURN sys_users;

   PROCEDURE get_sys_users (
      v_syscode         IN       NUMBER,
      v_sys_users_cur   OUT      sys_users_rec
   );

   PROCEDURE saverequireddocuments (
      v_action                 VARCHAR2,
      v_rdoc_code              NUMBER,
      v_rdoc_sht_desc          VARCHAR2,
      v_rdoc_desc              VARCHAR2,
      v_rdoc_mandatory         VARCHAR2,
      v_rdoc_date              DATE,
      v_error            OUT   VARCHAR2
   );

   PROCEDURE saveclientdocuments (
      v_action           VARCHAR2,
      v_cdocr_code       NUMBER,
      v_rdoc_code        NUMBER,
      v_clnt_code        NUMBER,
      v_submitted        VARCHAR2,
      v_date_submitted   DATE,
      v_ref_no           VARCHAR2,
      v_rmrk             VARCHAR2,
      v_user             VARCHAR2
   );

   PROCEDURE bpm_process_flow (v_module IN VARCHAR2, v_bpm OUT VARCHAR2);

   PROCEDURE save_memo_type (
      v_action        VARCHAR2,
      v_mtyp_code     NUMBER,
      v_sys_code      NUMBER,
      v_memo_type     VARCHAR2,
      v_sy_app_code   NUMBER,
      v_status        VARCHAR2 DEFAULT 'A',
      v_sub_class     NUMBER,
      v_appllvl       VARCHAR2
   );

   PROCEDURE save_memo_details (
      v_action        VARCHAR2,
      v_subject       VARCHAR2,
      v_content       VARCHAR2,
      v_memo_code     NUMBER,
      v_mtyp_code     NUMBER,
      v_memdet_code   NUMBER
   );

   PROCEDURE inactivate_renewal_tickets (v_pol_code NUMBER);

   PROCEDURE get_batch_pol_bpm_details (
      v_pol_batch_no         NUMBER,
      v_action               VARCHAR2,
      v_pol_status     OUT   VARCHAR2,
      v_tckt_code      OUT   NUMBER
   );

   PROCEDURE delete_email_attachment (v_ett_code NUMBER);

   TYPE avail_tickets IS RECORD (
      pol_batch_no        gin_policies.pol_batch_no%TYPE,
      pol_policy_no       gin_policies.pol_policy_no%TYPE,
      client              VARCHAR2 (100),
      pol_policy_status   gin_policies.pol_policy_status%TYPE,
      authorised          VARCHAR2 (100),
      clnt_code           tqc_clients.clnt_code%TYPE
   );

   TYPE avail_tickets_rec IS REF CURSOR
      RETURN avail_tickets;

   TYPE tickets_points IS RECORD (
      tbtp_code       tqc_bpm_task_points.tbtp_code%TYPE,
      tptp_sht_desc   tqc_bpm_task_points.tptp_sht_desc%TYPE,
      tptp_desc       tqc_bpm_task_points.tptp_desc%TYPE
   );

   TYPE tickets_points_rec IS REF CURSOR
      RETURN tickets_points;

   PROCEDURE get_avail_quotations (v_tickets OUT avail_tickets_rec);

   PROCEDURE get_process_task_points (
      v_syscode       IN       NUMBER,
      v_process       IN       VARCHAR2,
      v_process_cur   OUT      tickets_points_rec
   );

   PROCEDURE get_avail_policies (
      v_tickets   OUT   avail_tickets_rec,
      v_status          VARCHAR2
   );

   PROCEDURE inactivate_tickets (v_trans_type VARCHAR2, v_batch_no NUMBER);

   PROCEDURE bpm_claims_process_flow (v_module IN VARCHAR2, v_bpm OUT VARCHAR2);

   PROCEDURE inactivate_ext_tickets (v_ticket_no IN NUMBER);

   PROCEDURE remove_tickets (v_task_id NUMBER);

   FUNCTION get_ticket_dtls_by_tckt (v_tckt_no IN NUMBER)
      RETURN tickets_ref;

FUNCTION get_clm_tickets(v_clm_no VARCHAR2, v_name VARCHAR2)
    RETURN tickets_ref;

   FUNCTION get_quote_tickets(v_quo_no VARCHAR2, v_name VARCHAR2)
    RETURN tickets_ref;

  FUNCTION get_policy_tickets(v_pol_no VARCHAR2, v_name VARCHAR2)
    RETURN tickets_ref;

   FUNCTION check_trvl_agent_pwd (
      v_entered_user      IN       VARCHAR2,
      v_entered_pwd       IN       VARCHAR2,
      v_msg               OUT      VARCHAR2,
      v_last_login_date   OUT      DATE,
      v_user_code         OUT      NUMBER,
      coucode             OUT      NUMBER,
      v_new_pwd1          IN       VARCHAR2,
      v_new_pwd2          IN       VARCHAR2,
      v_new_pwd_entry     IN       VARCHAR2 DEFAULT 'N'
   )
      RETURN NUMBER;

   TYPE chrq_tckt_rec IS RECORD (
      tckt_code         tqc_bpm_tickets.tckt_code%TYPE,
      tckt_sys_module   VARCHAR2 (50),
      cqr_no            fms_cheques.cqr_no%TYPE,
      cqr_payee         fms_cheques.cqr_payee%TYPE,
      cqr_narrative     fms_cheques.cqr_narrative%TYPE,
      tckt_by           tqc_bpm_tickets.tckt_by%TYPE,
      tckt_date         tqc_bpm_tickets.tckt_date%TYPE,
      tckt_process_id   tqc_bpm_tickets.tckt_process_id%TYPE,
      sys_module_code   tqc_bpm_tickets.tckt_sys_module%TYPE,
      tckt_to           tqc_bpm_tickets.tckt_to%TYPE,
      tckt_remarks      tqc_bpm_tickets.tckt_remarks%TYPE,
      tckt_name         jbpm4_task.name_%TYPE,
      tckt_due_date     jbpm4_task.duedate_%TYPE
   );

--   FUNCTION get_cheques_tickets
--      RETURN tickets_ref;

   TYPE chrq_tckt_ref IS REF CURSOR
      RETURN chrq_tckt_rec;

   FUNCTION getchqrtcktdtslbyuser (v_user IN VARCHAR2)
      RETURN chrq_tckt_ref;

   FUNCTION getchqrtcktdts (v_tckt_code IN NUMBER)
      RETURN chrq_tckt_ref;

   FUNCTION recoverpassword (
      v_entered_user   IN   VARCHAR2,
      v_reset_code     IN   VARCHAR2
   )
      RETURN VARCHAR2;

   FUNCTION recoverclientpassword (
      v_entered_user   IN   VARCHAR2,
      v_reset_code     IN   VARCHAR2
   )
      RETURN VARCHAR2;

  FUNCTION createWebClient(v_username    IN   VARCHAR2,
                         v_password    IN   VARCHAR2,
                         v_msg         OUT  VARCHAR2,
                         v_lastlogindate OUT DATE,
                         v_clientCode  OUT NUMBER,
                         v_clientName  OUT VARCHAR2,
                         v_accountCode OUT NUMBER,
                         v_clientShtDesc  OUT VARCHAR2,
                         v_fullName  IN VARCHAR2 DEFAULT NULL,
                         v_mobileNumber  IN VARCHAR2 DEFAULT NULL,
                         v_resetPassword  IN VARCHAR2 DEFAULT NULL)
      RETURN NUMBER;

   TYPE defaultcountry_rec IS RECORD (
      cou_code   tqc_countries.cou_code%TYPE,
      cou_name   tqc_countries.cou_name%TYPE
   );

   TYPE defaultcountry_ref IS REF CURSOR
      RETURN defaultcountry_rec;

   PROCEDURE defaultcountry (
      v_defaultcountry_ref   OUT   defaultcountry_ref,
      v_org_code                   NUMBER
   );

   FUNCTION ldap_authenticate (
      vusername           IN       VARCHAR2,
      v_msg               OUT      VARCHAR2,
      v_last_login_date   OUT      DATE,
      usercode            OUT      NUMBER,
      username            OUT      VARCHAR2,
      v_sys_code          IN       NUMBER
   )
      RETURN INTEGER;

   FUNCTION checksecurityquestion (
      v_usr_username            IN   VARCHAR2,
      v_usr_security_question   IN   VARCHAR2,
      v_usr_security_answer     IN   VARCHAR2
   )
      RETURN VARCHAR2;
      
    FUNCTION check_agent_pwd (
      v_entered_user      IN       VARCHAR2,
      v_entered_pwd       IN       VARCHAR2,
      v_msg               OUT      VARCHAR2,
      v_last_login_date   OUT      DATE,
      agentcode           OUT      NUMBER,
      agentname           OUT      VARCHAR2,
      agentshtdesc        OUT      VARCHAR2,
      agentcontactcode    OUT      NUMBER,
      agentcontactname    OUT      VARCHAR2,
      v_new_pwd1          IN       VARCHAR2,
      v_new_pwd2          IN       VARCHAR2,
      v_new_pwd_entry     IN       VARCHAR2 DEFAULT 'N',
      agentbrncode        OUT      NUMBER,
      v_user_code         OUT      NUMBER
   )
      RETURN NUMBER;
 TYPE users_email_rec IS RECORD (
      email_address   VARCHAR2 (250)
   );

   TYPE user_emails_ref IS REF CURSOR
      RETURN users_email_rec;

   FUNCTION getuseremail (v_usrcode NUMBER)
      RETURN user_emails_ref;     
  PROCEDURE resetAgentsPassword (v_accc_code IN NUMBER, v_accc_pwd IN VARCHAR2, v_error_msg OUT VARCHAR2, v_old_pwd IN VARCHAR2 DEFAULT NULL, v_resetCode IN VARCHAR2 DEFAULT NULL);        
END tqc_web_pkg;
/