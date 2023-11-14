--
-- TQC_WEB_PKG280814  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.Tqc_Web_Pkg280814 AS
  type User_Auth_Roles_rec IS RECORD(
    AUTH_ROLE_AREA_ID      TQC_USER_WEB_SYSTEM_AUTH_ROLES.USAR_SAR_CODE%TYPE,
    AUTH_ROLE_AREA_SHT_DESC TQC_WEB_SYSTEM_AUTH_AREAS.SAA_SHT_DESC%TYPE,
    AUTH_ROLE_AREA_NAME     TQC_WEB_SYSTEM_AUTH_AREAS.SAA_NAME%TYPE,
    AUTH_ROLE_SYS_CODE      TQC_WEB_SYSTEM_AUTH_AREAS.SAA_SYS_CODE%TYPE,
    AUTH_ROLE_SYS_NAME      TQC_WEB_SYSTEMS.SYS_NAME%TYPE);

  type User_Auth_Roles_ref IS REF CURSOR RETURN User_Auth_Roles_rec;

  FUNCTION User_Auth_Areas(user_code IN NUMBER, sys_code IN NUMBER)
    RETURN User_Auth_Roles_ref;
  PROCEDURE update_user_login_attempt(v_usr_name IN VARCHAR2);
  PROCEDURE user_system_conn(v_user             VARCHAR2,
                             v_sys_code         IN NUMBER,
                             v_killopen_session IN VARCHAR2 DEFAULT 'N');
  FUNCTION Agent_authenticate(vusername IN VARCHAR2, vpwd IN VARCHAR2)
    RETURN INTEGER;

  FUNCTION get_usr_divisions_val(v_usrcode IN NUMBER) RETURN VARCHAR2;

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

  FUNCTION client_authenticate(vusername        IN VARCHAR2,
                               vpwd             IN VARCHAR2,
                               vmsg             OUT VARCHAR2,
                               lastlogindate    OUT DATE,
                               agentcode        OUT NUMBER,
                               agentname        OUT VARCHAR2,
                               agentcontactcode OUT NUMBER,
                               agentcontactname OUT VARCHAR2) RETURN INTEGER;
   
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
                            v_prpCode     OUT NUMBER,
                            v_clientType    OUT VARCHAR2,
                            v_defaultBranch OUT NUMBER) RETURN INTEGER ;

  FUNCTION user_authenticate(vusername     IN VARCHAR2,
                             vpwd          IN VARCHAR2,
                             vmsg          OUT VARCHAR2,
                             lastlogindate OUT DATE,
                             usercode      OUT NUMBER,
                             username      OUT VARCHAR2,
                             v_sys_code    IN NUMBER) RETURN INTEGER;

  FUNCTION user_authenticate(vusername     IN VARCHAR2,
                             vpwd          IN VARCHAR2,
                             v_pwd_reentry IN VARCHAR2,
                             v_new_pwd1    IN VARCHAR2,
                             v_new_pwd2    IN VARCHAR2,
                             vmsg          OUT VARCHAR2,
                             lastlogindate OUT DATE,
                             usercode      OUT NUMBER,
                             username      OUT VARCHAR2,
                             v_sys_code    IN NUMBER) RETURN INTEGER;

  FUNCTION user_sys_authenticate(vusername     IN VARCHAR2,
                                 vmsg          OUT VARCHAR2,
                                 lastlogindate OUT DATE,
                                 usercode      OUT NUMBER,
                                 username      OUT VARCHAR2,
                                 v_sys_code    IN NUMBER) RETURN INTEGER;

  TYPE usr_branches_rec IS RECORD(
    BRN_CODE        TQC_BRANChES.BRN_CODE%TYPE,
    BRN_SHT_DESC    TQC_BRANCHES.BRN_SHT_DESC%TYPE,
    BRN_NAME        TQC_BRANCHES.BRN_NAME%TYPE,
    BRN_GEN_POL_CLM TQC_BRANCHES.BRN_GEN_POL_CLM%TYPE);
  TYPE usr_branches_ref IS REF CURSOR RETURN usr_branches_rec;
  FUNCTION get_usr_branches(v_usrname IN VARCHAR2,v_agn_code IN NUMBER DEFAULT NULL) RETURN usr_branches_ref;
  FUNCTION get_usr_branches(v_usrcode IN NUMBER,v_agn_code IN NUMBER DEFAULT NULL) RETURN usr_branches_ref;
  FUNCTION get_usr_dflt_branch(v_usrname IN VARCHAR2,v_agn_code IN NUMBER DEFAULT NULL) RETURN usr_branches_ref;
  FUNCTION get_usr_dflt_branch(v_usrcode IN NUMBER,v_agn_code IN NUMBER DEFAULT NULL) RETURN usr_branches_ref;

  TYPE usr_divisions_rec IS RECORD(
    USD_DIV_CODE TQC_USER_DIVISIONS.USD_DIV_CODE%TYPE,
    DIV_SHT_DESC TQC_DIVISIONS.DIV_SHT_DESC%TYPE,
    DIV_NAME     TQC_DIVISIONS.DIV_NAME%TYPE);
  TYPE usr_divisions_ref IS REF CURSOR RETURN usr_divisions_rec;
  FUNCTION get_dflt_usr_branch_div(v_usrname IN VARCHAR2)
    RETURN usr_divisions_ref;
  FUNCTION get_usr_divisions(v_usrcode IN NUMBER) RETURN usr_divisions_ref;
  FUNCTION get_usr_dflt_divisions(v_usrcode IN NUMBER)
    RETURN usr_divisions_ref;

  TYPE currencies_rec IS RECORD(
    CUR_CODE   TQC_CURRENCIES.CUR_CODE%TYPE,
    CUR_SYMBOL TQC_CURRENCIES.CUR_SYMBOL%TYPE);
  TYPE currencies_ref IS REF CURSOR RETURN currencies_rec;
  FUNCTION get_currencies RETURN currencies_ref;
  TYPE process IS RECORD(
    SPRC_CODE      NUMBER,
    SPRC_BPM_ID    VARCHAR2(100),
    SPRC_SHT_DESC  VARCHAR2(100),
    SPRC_JPDL_DESC VARCHAR2(50));
  TYPE process_rec IS REF CURSOR RETURN process;
  PROCEDURE get_process_dtls(v_syscode     IN NUMBER,
                             v_process     IN VARCHAR2,
                             v_process_cur OUT process_rec);
  PROCEDURE save_process_attributes(v_tckt_code     NUMBER,
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
                                    v_type          IN VARCHAR2 DEFAULT 'S',
                                    v_extern_ref_no IN VARCHAR2 DEFAULT NULL,
                                    v_adhoc_name    IN VARCHAR2 DEFAULT NULL,
                                    v_eff_date      IN DATE DEFAULT NULL,
                                    v_ggt_no        IN NUMBER DEFAULT NULL);
  PROCEDURE update_process_attributes(v_tckt_code  NUMBER,
                                      v_sys_code   NUMBER,
                                      v_sys_module VARCHAR2,
                                      v_clnt_code  NUMBER,
                                      v_agn_code   NUMBER,
                                      v_pol_code   NUMBER,
                                      v_pol_no     VARCHAR2,
                                      v_clm_no     VARCHAR2,
                                      v_quot_code  NUMBER,
                                      v_user       VARCHAR2,
                                      v_tckt_date  DATE,
                                      v_process_id VARCHAR2,
                                      v_quot_no    VARCHAR2,
                                      v_endr_code  NUMBER,
                                      v_prod_type  VARCHAR2,
                                      v_tckt_to    VARCHAR2,
                                      v_remarks    VARCHAR2,
                                      v_endorsment VARCHAR2,
                                      v_trans_no   NUMBER);
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
  PROCEDURE reassign_task(v_taskid  IN VARCHAR2,
                          v_user    IN VARCHAR2,
                          v_remarks IN VARCHAR2,
                          v_from    IN VARCHAR2 DEFAULT NULL);
  PROCEDURE check_task_completion(v_task_point IN VARCHAR2,
                                  v_taskID     IN VARCHAR2,
                                  v_sys_code   IN NUMBER,
                                  v_complete   OUT VARCHAR2);
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
                              agentbrncode     OUT NUMBER) RETURN INTEGER;
  type tickets_rec IS RECORD(
    TCKT_CODE                NUMBER,
    USRSYSTEM                VARCHAR(50),
    SYSMODULE                VARCHAR(50),
    TCKT_CLNT_CODE           NUMBER,
    CLIENT                   VARCHAR(100),
    TCKT_AGN_CODE            NUMBER,
    AGENT                    VARCHAR(100),
    TCKT_POL_CODE            NUMBER,
    TCKT_POL_NO              VARCHAR(50),
    TCKT_CLM_NO              VARCHAR(50),
    TCKT_QUOT_CODE           NUMBER,
    QUO_NO                   VARCHAR(50),
    TCKT_BY                  VARCHAR(50),
    TCKT_DATE                DATE,
    TCKT_PROCESS_ID          VARCHAR2(100),
    SYS_MODULE_CODE          VARCHAR2(10),
    TCKT_ENDR_CODE           NUMBER,
    TCKT_PROD_TYPE           VARCHAR2(20),
    TCKT_TO                  VARCHAR2(100),
    TCKT_REMARKS             VARCHAR2(300),
    TCKT_NAME                VARCHAR2(100),
    TCKT_DUE_DATE            DATE,
    TCKT_ENDORSEMENT         VARCHAR2(25),
    TCKT_TRANSNO             NUMBER,
    REF_NO                   VARCHAR2(100),
    TCKT_PRP_CODE            NUMBER,
    POL_CLIENT_POLICY_NUMBER VARCHAR2(30),
    TCKT_TYPE                VARCHAR2(1),
    POL_POLICY_STATUS        GIN_POLICIES.POL_POLICY_STATUS%TYPE,
    TCKT_TRAN_EFF_DATE       DATE,
    TCKT_GGT_NO              NUMBER,
    USR_TYPE                 VARCHAR2(1));
  type tickets_ref IS REF CURSOR RETURN tickets_rec;
  TYPE country_states IS RECORD(
    STS_CODE     TQC_STATES.STS_CODE%TYPE,
    STS_COU_CODE TQC_STATES.STS_COU_CODE%TYPE,
    STS_SHT_DESC TQC_STATES.STS_SHT_DESC%TYPE,
    STS_NAME     TQC_STATES.STS_NAME%TYPE);
  TYPE country_states_rec IS REF CURSOR RETURN country_states;
  FUNCTION get_tckt_dtls_by_usr(v_user     IN VARCHAR2,
                                v_quo_code NUMBER DEFAULT NULL,
                                v_pol_code NUMBER DEFAULT NULL,
                                v_claim_no VARCHAR2 DEFAULT NULL,
                                v_sysCode  NUMBER DEFAULT NULL,
                                v_cla_type VARCHAR2 DEFAULT NULL)
    RETURN tickets_ref;
  FUNCTION get_tckt_dtls_by_usr_life(v_user     IN VARCHAR2,
                                     v_quo_code NUMBER DEFAULT NULL,
                                     v_pol_code NUMBER DEFAULT NULL,
                                     v_claim_no VARCHAR2 DEFAULT NULL,
                                     v_sysCode  NUMBER DEFAULT NULL)
    RETURN tickets_ref;
  FUNCTION get_tckt_dtls(v_tckt_code IN NUMBER) RETURN tickets_ref;
  PROCEDURE get_bpm_task_srn(v_taskname  IN VARCHAR,
                             v_scrn_name OUT VARCHAR2,
                             v_sys_code  NUMBER);
  PROCEDURE update_cal_activity(v_loc       VARCHAR2,
                                v_title     VARCHAR2,
                                v_today     VARCHAR2,
                                v_startdate VARCHAR2,
                                v_enddate   VARCHAR2,
                                v_user      VARCHAR2);
  PROCEDURE conv_proposer_to_clnt(v_clnt_code IN NUMBER,
                                  v_sht_desc  OUT VARCHAR2,
                                  v_name      OUT VARCHAR2,
                                  v_err       OUT VARCHAR2);
  PROCEDURE find_states(v_states OUT country_states_rec, v_cou_code NUMBER);
  PROCEDURE inactivate_tickets(v_endorsement NUMBER);
  PROCEDURE save_workflw_attributes(v_tckt_code  NUMBER,
                                    v_sys_code   NUMBER,
                                    v_sys_module VARCHAR2,
                                    v_clnt_code  NUMBER,
                                    v_agn_code   NUMBER,
                                    v_pol_code   NUMBER,
                                    v_pol_no     VARCHAR2,
                                    v_clm_no     VARCHAR2,
                                    v_quot_code  NUMBER,
                                    v_user       VARCHAR2,
                                    v_tckt_date  DATE,
                                    v_process_id VARCHAR2,
                                    v_quot_no    VARCHAR2,
                                    v_endr_code  NUMBER,
                                    v_prod_type  VARCHAR2,
                                    v_tckt_to    VARCHAR2,
                                    v_remarks    VARCHAR2,
                                    v_endorsment VARCHAR2,
                                    v_trans_no   NUMBER,
                                    v_prp_code   NUMBER DEFAULT NULL,
                                    v_cla_type   VARCHAR2 DEFAULT NULL);
  FUNCTION get_default_usr_branch(v_usrname  IN VARCHAR2,
                                  v_sys_code IN NUMBER)
    RETURN usr_branches_ref;
  FUNCTION check_user_pwd(v_entered_user    IN VARCHAR2,
                          v_entered_pwd     IN VARCHAR2,
                          v_msg             OUT VARCHAR2,
                          v_last_login_date OUT DATE,
                          usercode          OUT NUMBER,
                          username          OUT VARCHAR2,
                          v_new_pwd1        IN VARCHAR2,
                          v_new_pwd2        IN VARCHAR2,
                          v_new_pwd_entry   IN VARCHAR2 DEFAULT 'N',
                          v_sys_code        IN NUMBER) RETURN NUMBER;

  PROCEDURE PopulateRequiredDocuments(v_clnt_code NUMBER, v_user VARCHAR2);
  v_error TQC_ERROR_MANAGEr.error_info_rt;

  TYPE sys_users IS RECORD(
    USR_CODE     TQC_USERS.USR_CODE%TYPE,
    USR_USERNAME TQC_USERS.USR_USERNAME%TYPE,
    USR_NAME     TQC_USERS.USR_NAME%TYPE,
    USR_TYPE     TQC_USERS.USR_TYPE%TYPE);
  TYPE sys_users_rec IS REF CURSOR RETURN sys_users;
  PROCEDURE get_sys_users(v_syscode       IN NUMBER,
                          v_div_code      IN NUMBER,
                          v_sys_users_cur OUT sys_users_rec);

  PROCEDURE saveRequiredDocuments(v_action         VARCHAR2,
                                  v_rdoc_code      NUMBER,
                                  v_rdoc_sht_desc  VARCHAR2,
                                  v_rdoc_desc      VARCHAR2,
                                  v_rdoc_mandatory VARCHAR2,
                                  v_rdoc_date      DATE,
                                  
                                  v_error OUT VARCHAR2);

  PROCEDURE saveClientDocuments(v_action         VARCHAR2,
                                v_cdocr_code     NUMBER,
                                v_rdoc_code      NUMBER,
                                v_clnt_code      NUMBER,
                                v_submitted      VARCHAR2,
                                v_date_submitted DATE,
                                v_ref_no         VARCHAR2,
                                v_rmrk           VARCHAR2,
                                v_user           VARCHAR2);
  PROCEDURE bpm_process_flow(v_module IN VARCHAR2, v_bpm OUT VARCHAR2);
 PROCEDURE save_memo_type(v_action      VARCHAR2,
                           v_mtyp_code   NUMBER,
                           v_sys_code    NUMBER,
                           v_memo_type   VARCHAR2,
                           v_sy_app_code number,
                           v_status      VARCHAR2 DEFAULT 'A',
                           v_sub_class   NUMBER,
                           v_applLvl     VARCHAR2);
  PROCEDURE save_memo_details(v_action      VARCHAR2,
                              v_subject     VARCHAR2,
                              v_content     VARCHAR2,
                              v_memo_code   NUMBER,
                              v_mtyp_code   NUMBER,
                              v_memdet_code NUMBER);
  PROCEDURE inactivate_renewal_tickets(v_pol_code NUMBER);
  PROCEDURE get_batch_pol_bpm_details(v_pol_batch_no NUMBER,
                                      v_action       VARCHAR2,
                                      v_pol_status   OUT VARCHAR2,
                                      v_tckt_code    OUT NUMBER);
  PROCEDURE delete_email_attachment(v_ett_code NUMBER);
  TYPE avail_tickets IS RECORD(
    
    POL_BATCH_NO      GIN_POLICIES.POL_BATCH_NO%TYPE,
    POL_POLICY_NO     GIN_POLICIES.POL_POLICY_NO%TYPE,
    CLIENT            VARCHAR2(100),
    POL_POLICY_STATUS GIN_POLICIES.POL_POLICY_STATUS%TYPE,
    AUTHORISED        VARCHAR2(100),
    CLNT_CODE         TQC_CLIENTS.CLNT_CODE%TYPE);
  TYPE avail_tickets_rec IS REF CURSOR RETURN avail_tickets;

  TYPE tickets_points IS RECORD(
    
    TBTP_CODE     TQC_BPM_TASK_POINTS.TBTP_CODE%TYPE,
    TPTP_SHT_DESC TQC_BPM_TASK_POINTS.TPTP_SHT_DESC%TYPE,
    TPTP_DESC     TQC_BPM_TASK_POINTS.TPTP_DESC%TYPE);
  TYPE tickets_points_rec IS REF CURSOR RETURN tickets_points;
  PROCEDURE get_avail_quotations(v_tickets  OUT avail_tickets_rec,
                                 v_quote_no VARCHAR2,
                                 v_client   VARCHAR2);
  PROCEDURE get_process_task_points(v_syscode     IN NUMBER,
                                    v_process     IN VARCHAR2,
                                    v_process_cur OUT tickets_points_rec);
  PROCEDURE get_avail_policies(v_tickets OUT avail_tickets_rec,
                               v_status  VARCHAR2);

  PROCEDURE inactivate_tickets(v_trans_type VARCHAR2, v_batch_no NUMBER);

  PROCEDURE bpm_claims_process_flow(v_module IN VARCHAR2,
                                    v_bpm    OUT VARCHAR2);

  PROCEDURE inactivate_ext_tickets(v_ticket_no IN NUMBER);

  PROCEDURE remove_tickets(v_task_id NUMBER);

  FUNCTION get_ticket_dtls_by_tckt(v_tckt_no IN NUMBER) RETURN tickets_ref;

  FUNCTION get_clm_tickets(v_clm_no VARCHAR2, v_name VARCHAR2)
    RETURN tickets_ref;

  FUNCTION get_quote_tickets(v_quo_no VARCHAR2, v_name VARCHAR2)
    RETURN tickets_ref;

  FUNCTION get_policy_tickets(v_pol_no VARCHAR2, v_name VARCHAR2)
    RETURN tickets_ref;
  FUNCTION check_trvl_agent_pwd(v_entered_user    IN VARCHAR2,
                                v_entered_pwd     IN VARCHAR2,
                                v_msg             OUT VARCHAR2,
                                v_last_login_date OUT DATE,
                                v_user_code       OUT NUMBER,
                                coucode           OUT NUMBER,
                                v_new_pwd1        IN VARCHAR2,
                                v_new_pwd2        IN VARCHAR2,
                                v_new_pwd_entry   IN VARCHAR2 DEFAULT 'N'
                                
                                ) RETURN NUMBER;

  PROCEDURE get_sys_users(v_syscode       IN NUMBER,
                          v_sys_users_cur OUT sys_users_rec);

  FUNCTION get_div_code(v_module IN VARCHAR2, v_code IN VARCHAR2)
    RETURN NUMBER;

  TYPE rein_tickets_rec IS RECORD(
    
    TCKT_CODE   TQC_BPM_TICKETS.TCKT_CODE%TYPE,
    TCKT_POL_NO TQC_BPM_TICKETS.TCKT_CODE%TYPE,
    TCKT_GGT_NO TQC_BPM_TICKETS.TCKT_GGT_NO%TYPE,
    NAME_       JBPM4_TASK.NAME_%TYPE);

  TYPE rein_tickets_ref IS REF CURSOR RETURN rein_tickets_rec;

  FUNCTION get_edit_rein_tckts(v_ggt_code NUMBER) RETURN rein_tickets_ref;
  
  FUNCTION recoverPassword (v_entered_user IN VARCHAR2,
                              v_reset_code    IN VARCHAR2) RETURN VARCHAR2;
 FUNCTION recoverClientPassword (v_entered_user IN VARCHAR2,
                                v_reset_code    IN VARCHAR2) RETURN VARCHAR2;
                                
FUNCTION createWebClient(v_username    IN   VARCHAR2,
                         v_password    IN   VARCHAR2,
                         v_msg         OUT  VARCHAR2,
                         v_lastlogindate OUT DATE,
                         v_clientCode  OUT NUMBER,
                         v_clientName  OUT VARCHAR2,
                         v_accountCode OUT NUMBER,
                         v_clientShtDesc  OUT VARCHAR2) RETURN NUMBER;

  
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
                            v_accountCode OUT NUMBER) RETURN INTEGER ;
                            PROCEDURE resetAgentsPassword (v_accc_code IN NUMBER, v_accc_pwd IN VARCHAR2, v_error_msg OUT VARCHAR2);
  
  function get_policy_tickets_by_user(v_user varchar2,v_policy_no varchar2)
  return tickets_ref;                     
                          FUNCTION user_authenticate(vusername IN VARCHAR2,
                              vpwd IN VARCHAR2,
                            v_pwd_reentry IN VARCHAR2,
                            v_new_pwd1 IN VARCHAR2,
                            v_new_pwd2 IN VARCHAR2,
                              vmsg OUT VARCHAR2,
                              lastlogindate OUT DATE,
                              usercode OUT NUMBER,
                              username OUT VARCHAR2,
                            v_sys_code IN NUMBER,
                            v_reset_code    IN VARCHAR2) RETURN INTEGER ;
                            FUNCTION check_user_pwd(v_entered_user IN VARCHAR2,
                        v_entered_pwd IN VARCHAR2,
                        v_msg   OUT VARCHAR2,
                        v_last_login_date OUT DATE,
                        usercode OUT NUMBER,
                          username OUT VARCHAR2,
                        v_new_pwd1 IN VARCHAR2,
                        v_new_pwd2 IN VARCHAR2,
                        v_new_pwd_entry IN VARCHAR2 DEFAULT 'N',
                        v_sys_code IN NUMBER,
                        v_reset_code    IN VARCHAR2) RETURN NUMBER;
                        PROCEDURE undoPassowrdReset (v_entered_user IN VARCHAR2) ;
                       
 PROCEDURE get_sys_system_users(v_syscode       IN NUMBER,
                               v_div_code      IN NUMBER,
                               v_grp_user      IN  NUMBER,
                               v_sys_users_cur OUT sys_users_rec);
  type Default_Country_rec IS RECORD(
        COU_CODE    TQC_COUNTRIES.COU_CODE%TYPE,
        COU_NAME    TQC_COUNTRIES.COU_NAME%TYPE);
  type Default_Country_ref IS REF CURSOR RETURN Default_Country_rec;                             
  FUNCTION DefaultCountry(v_cou_type IN NUMBER)  RETURN Default_Country_ref;                             
END Tqc_Web_Pkg280814; 
 
/