--
-- TQC_SERVICE_REQUESTS  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_service_requests
AS
/******************************************************************************
   NAME:       TQC_SERVICE_REQUESTS
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        22/Jun/2011             1. Created this package.
******************************************************************************/
   TYPE serv_requests_cat_rec IS RECORD (
      tsrc_code       tqc_serv_req_cat.tsrc_code%TYPE,
      tsrc_name       tqc_serv_req_cat.tsrc_name%TYPE,
      tsrc_validity   tqc_serv_req_cat.tsrc_validity%TYPE,
      tsrc_usr_code   tqc_serv_req_cat.tsrc_usr_code%TYPE,
      usr_username    tqc_users.usr_username%TYPE,
      brn_name        tqc_branches.brn_name%TYPE,
      brn_code        tqc_branches.brn_code%TYPE,
      tsrc_default    tqc_serv_req_cat.tsrc_default%TYPE,
      tsrc_srt_code   tqc_serv_req_cat.tsrc_srt_code%TYPE
   );

   TYPE serv_requests_cat_ref IS REF CURSOR
      RETURN serv_requests_cat_rec;

   FUNCTION getservreqcategories (
      v_tsrc_srt_code   IN   NUMBER DEFAULT NULL,
      v_reqname         IN   VARCHAR2 DEFAULT NULL
   )
      RETURN serv_requests_cat_ref;

   PROCEDURE categoryproc (
      v_addedit              VARCHAR2,
      v_code                 NUMBER,
      v_name                 VARCHAR2,
      v_validity             NUMBER,
      v_usrcode              NUMBER,
      v_tsrc_brn_code   IN   NUMBER DEFAULT NULL,
      v_tsrc_srt_code        VARCHAR2 DEFAULT NULL,
      v_tsrc_default    IN   VARCHAR2 DEFAULT NULL
   );

   TYPE client_prop_pols_rec IS RECORD (
      proposal_no      VARCHAR2 (50),
      policy_no        VARCHAR2 (50),
      sum_assured      NUMBER,
      pol_code         NUMBER,
      url              VARCHAR2 (200),
      product_name     VARCHAR2 (50),
      v_type           VARCHAR2 (200),
      pol_check_date   DATE,
      endr_code        NUMBER
   );

   TYPE client_prop_pols_ref IS REF CURSOR
      RETURN client_prop_pols_rec;

   PROCEDURE ordclientproppol (
      v_proppol    OUT   client_prop_pols_ref,
      v_clntcode         NUMBER
   );

   PROCEDURE grpclientproppol (
      v_proppol    OUT   client_prop_pols_ref,
      v_clntcode         NUMBER
   );

   PROCEDURE gisclientproppol (
      v_proppol    OUT      client_prop_pols_ref,
      v_clntcode            NUMBER,
      v_type       IN       VARCHAR2
   );

   PROCEDURE servicerequestsproc (
      v_addedit                      VARCHAR2,
      v_tsrcode             IN OUT   NUMBER,
      v_tsrccode                     NUMBER,
      v_tsrtype                      VARCHAR2,
      v_tsracctype                   VARCHAR2,
      v_tsracccode                   NUMBER,
      v_tsrdate                      DATE,
      v_tsrasignee                   NUMBER,
      v_tsrowntype                   VARCHAR2,
      v_tsrowner                     NUMBER,
      v_tsrstatus                    VARCHAR2,
      v_duedate                      DATE,
      v_resoldate                    DATE,
      v_tsrsummary                   VARCHAR2,
      v_tsrdesc                      VARCHAR2,
      v_tsrsolution                  VARCHAR2,
      v_tsrtcbcode                   NUMBER DEFAULT NULL,
      v_tsrmode                      VARCHAR2 DEFAULT NULL,
      v_tsr_comments                 VARCHAR2 DEFAULT NULL,
      v_tsr_srt_code                 NUMBER DEFAULT NULL,
      v_tsr_sec_comm_mode   IN       VARCHAR2 DEFAULT NULL,
      v_tsr_policy_no       IN       VARCHAR2 DEFAULT NULL,
      v_tsr_receive_date    IN       DATE DEFAULT NULL
   );

   TYPE serv_requests_rec IS RECORD (
      tsr_code              tqc_serv_requests.tsr_code%TYPE,
      tsr_tsrc_code         tqc_serv_requests.tsr_tsrc_code%TYPE,
      tsr_type              tqc_serv_requests.tsr_type%TYPE,
      tsr_acc_type          tqc_serv_requests.tsr_acc_type%TYPE,
      tsr_acc_code          tqc_serv_requests.tsr_acc_code%TYPE,
      tsr_date              tqc_serv_requests.tsr_date%TYPE,
      tsr_assignee          tqc_serv_requests.tsr_assignee%TYPE,
      tsr_owner_type        tqc_serv_requests.tsr_owner_type%TYPE,
      tsr_owner_code        tqc_serv_requests.tsr_owner_code%TYPE,
      tsr_status            tqc_serv_requests.tsr_status%TYPE,
      tsr_due_date          tqc_serv_requests.tsr_due_date%TYPE,
      tsr_resolution_date   tqc_serv_requests.tsr_resolution_date%TYPE,
      tsr_summary           tqc_serv_requests.tsr_summary%TYPE,
      tsr_description       tqc_serv_requests.tsr_description%TYPE,
      tsr_solution          tqc_serv_requests.tsr_solution%TYPE,
      tsrc_name             tqc_serv_req_cat.tsrc_name%TYPE,
      acc_type              VARCHAR2 (20),
      assignee              VARCHAR2 (100),
      account_name          VARCHAR2 (100),
      owner_type            VARCHAR2 (20),
      owner                 VARCHAR2 (100),
      tsr_comm_mode         tqc_serv_requests.tsr_comm_mode%TYPE,
      tsr_ref_number        tqc_serv_requests.tsr_ref_number%TYPE,
      tsr_comments          tqc_serv_requests.tsr_comments%TYPE,
      clnt_sht_desc         tqc_clients.clnt_sht_desc%TYPE,
      clnt_physical_addrs   tqc_clients.clnt_physical_addrs%TYPE,
      clnt_email_addrs      tqc_clients.clnt_email_addrs%TYPE,
      clnt_sms_tel          tqc_clients.clnt_sms_tel%TYPE,
      tsr_srt_code          tqc_serv_requests.tsr_srt_code%TYPE,
      srt_desc              tqc_serv_req_types.srt_desc%TYPE
   );

   TYPE serv_requests_ref IS REF CURSOR
      RETURN serv_requests_rec;

   FUNCTION getclientopenrequests (
      v_clientcode          NUMBER,
      v_tsr_acc_type   IN   VARCHAR2
   )
      RETURN serv_requests_ref;

   FUNCTION getclientallopenrequests (v_clientcode NUMBER)
      RETURN serv_requests_ref;

   FUNCTION getallclientrequests (
      v_clientcode          NUMBER,
      v_tsr_acc_type   IN   VARCHAR2
   )
      RETURN serv_requests_ref;

   FUNCTION getuseropenrequests (v_usercode NUMBER)
      RETURN serv_requests_ref;

   FUNCTION getuseroverduerequests (v_usercode NUMBER)
      RETURN serv_requests_ref;

   FUNCTION getoverduerequests (v_tsr_status IN VARCHAR2)
      RETURN serv_requests_ref;

   FUNCTION getallpendingrequests
      RETURN serv_requests_ref;

   TYPE overduecount_rec IS RECORD (
      count_no   NUMBER,
      assignee   VARCHAR2 (100)
   );

   TYPE overduecount_ref IS REF CURSOR
      RETURN overduecount_rec;

   FUNCTION getoverduerequestschat
      RETURN overduecount_ref;

   TYPE kb_topics_rec IS RECORD (
      tkbt_id          tqc_kb_topics.tkbt_id%TYPE,
      tkbt_order       tqc_kb_topics.tkbt_order%TYPE,
      tkbt_sht_desc    tqc_kb_topics.tkbt_sht_desc%TYPE,
      tkbt_desc        tqc_kb_topics.tkbt_desc%TYPE,
      tkbt_parent_id   tqc_kb_topics.tkbt_parent_id%TYPE
   );

   TYPE kb_topics_ref IS REF CURSOR
      RETURN kb_topics_rec;

   FUNCTION getroottopics
      RETURN kb_topics_ref;

   FUNCTION getchildtopics (v_tkbt_id NUMBER)
      RETURN kb_topics_ref;

   PROCEDURE kbtopicproc (
      v_addedit       VARCHAR2,
      v_tkbtid        NUMBER,
      v_tkbtorder     NUMBER,
      v_tkbtshtdesc   VARCHAR2,
      v_tkbtdesc      VARCHAR2,
      v_tkbtparent    NUMBER
   );

   TYPE kb_content_rec IS RECORD (
      tkbc_id        tqc_kb_content.tkbc_id%TYPE,
      tkbc_tkbt_id   tqc_kb_content.tkbc_tkbt_id%TYPE,
      tkbc_order     tqc_kb_content.tkbc_order%TYPE,
      tkbc_content   tqc_kb_content.tkbc_content%TYPE
   );

   TYPE kb_content_ref IS REF CURSOR
      RETURN kb_content_rec;

   FUNCTION getkbcontent (v_tkbt_id NUMBER)
      RETURN kb_content_ref;

   PROCEDURE kbcontentproc (
      v_addedit     VARCHAR2,
      v_tkbcid      NUMBER,
      v_tkbtid      NUMBER,
      v_tkbcorder   NUMBER,
      v_tkbccont    VARCHAR2
   );

   TYPE cat_branches_rec IS RECORD (
      brn_code       tqc_branches.brn_code%TYPE,
      brn_sht_desc   tqc_branches.brn_sht_desc%TYPE,
      brn_reg_code   tqc_branches.brn_reg_code%TYPE,
      brn_name       tqc_branches.brn_name%TYPE
   );

   TYPE cat_branches_ref IS REF CURSOR
      RETURN cat_branches_rec;

   FUNCTION getcategorybranches
      RETURN cat_branches_ref;

   FUNCTION generate_serv_req_ref (v_tsrtype IN VARCHAR)
      RETURN VARCHAR2;

   TYPE departments_rec IS RECORD (
      dep_code       tqc_departments.dep_code%TYPE,
      dep_sht_desc   tqc_departments.dep_sht_desc%TYPE,
      dep_name       tqc_departments.dep_name%TYPE,
      dep_wef        tqc_departments.dep_wef%TYPE,
      dep_wet        tqc_departments.dep_wet%TYPE
   );

   TYPE departments_ref IS REF CURSOR
      RETURN departments_rec;

   FUNCTION get_departments
      RETURN departments_ref;

   TYPE serv_request_types_rec IS RECORD (
      srt_code       tqc_serv_req_types.srt_code%TYPE,
      srt_sht_desc   tqc_serv_req_types.srt_sht_desc%TYPE,
      srt_desc       tqc_serv_req_types.srt_desc%TYPE
   );

   TYPE serv_request_types_ref IS REF CURSOR
      RETURN serv_request_types_rec;

   FUNCTION get_serv_request_types
      RETURN serv_request_types_ref;

   PROCEDURE update_serv_request_types (
      v_addedit                      VARCHAR2,
      v_srt_code                IN   NUMBER,
      v_srt_desc                IN   VARCHAR2,
      v_srt_sht_desc            IN   VARCHAR2,
      v_srt_reason_for_change   IN   VARCHAR2
   );

   TYPE serv_providers_rec IS RECORD (
      spr_code               tqc_service_providers.spr_code%TYPE,
      spr_sht_desc           tqc_service_providers.spr_sht_desc%TYPE,
      spr_name               tqc_service_providers.spr_name%TYPE,
      spr_physical_address   tqc_service_providers.spr_physical_address%TYPE,
      spr_postal_address     tqc_service_providers.spr_postal_address%TYPE,
      spr_phone              tqc_service_providers.spr_phone%TYPE,
      spr_email              tqc_service_providers.spr_email%TYPE,
      spr_mobile_no          tqc_service_providers.spr_mobile_no%TYPE,
      spr_sms_number         tqc_service_providers.spr_sms_number%TYPE
   );

   TYPE serv_providers_ref IS REF CURSOR
      RETURN serv_providers_rec;

   FUNCTION get_serv_providers
      RETURN serv_providers_ref;

   FUNCTION getallpendingrequests (v_tsr_assignee IN VARCHAR2)
      RETURN serv_requests_ref;

   PROCEDURE update_serv_req_other_dtls (
      v_addedit                          VARCHAR2,
      v_srid_code               IN OUT   NUMBER,
      v_srid_name               IN       VARCHAR2,
      v_srid_telephone          IN       VARCHAR2,
      v_srid_email_address      IN       VARCHAR2,
      v_srid_physical_address   IN       VARCHAR2,
      v_srid_id_number          IN       VARCHAR2
   );

   TYPE service_requesters_rec IS RECORD (
      srid_code               tqc_serv_req_ind_dtls.srid_code%TYPE,
      srid_name               tqc_serv_req_ind_dtls.srid_name%TYPE,
      srid_telephone          tqc_serv_req_ind_dtls.srid_telephone%TYPE,
      srid_email_address      tqc_serv_req_ind_dtls.srid_email_address%TYPE,
      srid_physical_address   tqc_serv_req_ind_dtls.srid_physical_address%TYPE,
      srid_id_number          tqc_serv_req_ind_dtls.srid_id_number%TYPE
   );

   TYPE service_requesters_ref IS REF CURSOR
      RETURN service_requesters_rec;

   FUNCTION get_other_service_requesters (
      v_srid_name        IN   VARCHAR2,
      v_srid_id_number   IN   VARCHAR2
   )
      RETURN service_requesters_ref;

   TYPE agent_commission_rec IS RECORD (
      cop_agn_code        gin_commission_pymts.cop_agn_code%TYPE,
      agn_name            tqc_agencies.agn_name%TYPE,
      cop_date            gin_commission_pymts.cop_date%TYPE,
      cop_cr_ref_no       gin_commission_pymts.cop_cr_ref_no%TYPE,
      cop_dr_ref_no       gin_commission_pymts.cop_dr_ref_no%TYPE,
      cop_comm_amt        gin_commission_pymts.cop_comm_amt%TYPE,
      cop_whdtax_amt      gin_commission_pymts.cop_whdtax_amt%TYPE,
      cop_net_comm        gin_commission_pymts.cop_net_comm%TYPE,
      cop_cur_code        gin_commission_pymts.cop_cur_code%TYPE,
      cop_authorized      gin_commission_pymts.cop_authorized%TYPE,
      cop_authorized_by   gin_commission_pymts.cop_authorized_by%TYPE,
      cop_paid            gin_commission_pymts.cop_paid%TYPE,
      cop_paid_chq_date   gin_commission_pymts.cop_paid_chq_date%TYPE,
      cop_paid_chq_no     gin_commission_pymts.cop_paid_chq_no%TYPE
   );

   TYPE agent_commission_ref IS REF CURSOR
      RETURN agent_commission_rec;

   FUNCTION get_agent_commission (v_agn_code IN NUMBER)
      RETURN agent_commission_ref;

   TYPE provider_fee_rec IS RECORD (
      client_name     VARCHAR2 (250),
      fee_desc        VARCHAR2 (250),
      policy_number   VARCHAR2 (15),
      fee_amount      NUMBER
   );

   TYPE provider_fee_ref IS REF CURSOR
      RETURN provider_fee_rec;

   FUNCTION getproviderfees (v_sprcode NUMBER)
      RETURN provider_fee_ref;
 PROCEDURE setrequestAssignee(
   v_tsr_code              tqc_serv_requests.tsr_code%TYPE,
   v_tsr_assignee          tqc_serv_requests.tsr_assignee%TYPE);     
  
    TYPE serv_req_client_comm_rec IS RECORD (
      SRC_CODE     NUMBER,
      SRC_CLIENT_COMMENT        VARCHAR2 (250),
      SRC_SOLUTION   VARCHAR2 (250)
   );

   TYPE serv_req_client_comm_ref IS REF CURSOR
      RETURN serv_req_client_comm_rec; 
      
     FUNCTION getServReqClientComments (
      v_tsr_code   IN   NUMBER DEFAULT NULL
   )
      RETURN serv_req_client_comm_ref;
      
     procedure servReqClntCommentProc(
       v_addedit                          VARCHAR2,
      v_src_code               IN OUT   NUMBER,
      v_src_client_comment               IN       VARCHAR2,
      v_src_solution          IN       VARCHAR2,
      v_src_tsr_code         IN       NUMBER
  );
   
     TYPE serv_req_escalations_rec IS RECORD (
     SRE_CODE                           NUMBER,
     SRE_SRT_CODE                   NUMBER,
     SRE_LVL_DURATION             NUMBER,
     SRE_LVL_ONE_ASSIGNEE      NUMBER,
     SRE_LVL_TWO_DURATION    NUMBER,
     SRE_LVL_TWO_ASSIGNEE    NUMBER,
     SRE_SRT_CODE_NAME         VARCHAR(200),
     SRE_LVL_ONE_ASSIGNEE_NAME  VARCHAR(200),
     SRE_LVL_TWO_ASSIGNEE_NAME VARCHAR(200)
   );

   TYPE serv_req_escalations_ref IS REF CURSOR
      RETURN serv_req_escalations_rec; 
  
  FUNCTION getservreqscalations
      RETURN serv_req_escalations_ref;
   
  procedure servReqClntEscalationsProc(
       v_addedit                           IN VARCHAR2,
      v_sre_code                           IN OUT NUMBER ,
       v_sre_srt_code                    IN NUMBER ,
        v_sre_lvl_duration               IN NUMBER,
       v_sre_lvl_one_assignee        IN NUMBER,
       v_sre_lvl_two_duration        IN NUMBER,
       v_sre_lvl_two_assignee       IN NUMBER
  );
    PROCEDURE process_serv_requests_alert;
     FUNCTION getworkingdays (v_date_from DATE, v_date_to DATE)
      RETURN NUMBER;
     
     PROCEDURE process_serv_req_email_alert(v_usr_code               NUMBER,
                                        v_ticket_no             VARCHAR2,
                                        v_details               VARCHAR2,
                                        v_tsracccode  NUMBER
                                        );
  PROCEDURE changeClntRequestStatus(v_tsr_code NUMBER,
                                  v_status   VARCHAR2);
                                
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
      pol_policy_status          gin_policies.pol_policy_status%TYPE,
      tckt_tran_eff_date         DATE,
      tckt_ggt_no                NUMBER,
      usr_type                   VARCHAR2 (1),
      pol_current_status         gin_policies.pol_current_status%TYPE
   );

   TYPE tickets_ref IS REF CURSOR
      RETURN tickets_rec;
  
  FUNCTION get_tckt_dtls( v_user     IN VARCHAR2 DEFAULT NULL,
                         v_quo_no VARCHAR2 DEFAULT NULL,
                         v_pol_no VARCHAR2 DEFAULT NULL,
                         v_claim_no VARCHAR2 DEFAULT NULL,
                         v_syscode  NUMBER DEFAULT NULL) RETURN tickets_ref ;
                        
   PROCEDURE reassign_task(v_taskid  IN VARCHAR2,
                          v_user    IN VARCHAR2,
                          v_remarks IN VARCHAR2,
                          v_sys_code IN NUMBER,
                          v_from    IN VARCHAR2 DEFAULT NULL) ;
END tqc_service_requests; 
/