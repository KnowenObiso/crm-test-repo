--
-- TQC_SERVICE_REQUESTS030215  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_SERVICE_REQUESTS030215 AS
/******************************************************************************
   NAME:       TQC_SERVICE_REQUESTS
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        22/Jun/2011             1. Created this package.
******************************************************************************/

TYPE serv_requests_cat_rec IS RECORD(
TSRC_CODE               TQC_SERV_REQ_CAT.TSRC_CODE%TYPE, 
TSRC_NAME               TQC_SERV_REQ_CAT.TSRC_NAME%TYPE, 
TSRC_VALIDITY           TQC_SERV_REQ_CAT.TSRC_VALIDITY%TYPE,
TSRC_USR_CODE           TQC_SERV_REQ_CAT.TSRC_USR_CODE%TYPE,
USR_USERNAME            TQC_USERS.USR_USERNAME%TYPE,
BRN_NAME            TQC_BRANCHES.BRN_NAME%TYPE,
BRN_CODE            TQC_BRANCHES.BRN_CODE%TYPE,
TSRC_DEFAULT           TQC_SERV_REQ_CAT.TSRC_DEFAULT%TYPE,
TSRC_SRT_CODE            TQC_SERV_REQ_CAT.TSRC_SRT_CODE%TYPE
);

TYPE  serv_requests_cat_ref IS REF CURSOR RETURN serv_requests_cat_rec;
FUNCTION getServReqCategories(V_TSRC_SRT_CODE IN NUMBER) RETURN serv_requests_cat_ref;

PROCEDURE categoryProc (v_addEdit   VARCHAR2,
                        v_code      NUMBER,
                        v_name      VARCHAR2,
                        v_validity  NUMBER,
                        v_usrCode   NUMBER,
                        v_tsrc_brn_code in NUMBER default null,
                        v_tsrc_srt_code VARCHAR2 default null,
                        v_tsrc_default in varchar2 default null ) ;

TYPE client_prop_pols_rec IS RECORD(
PROPOSAL_NO             VARCHAR2(50),
POLICY_NO               VARCHAR2(50),
SUM_ASSURED             NUMBER,
POL_CODE                NUMBER,
URL                     VARCHAR2(200),
PRODUCT_NAME            VARCHAR2(50),
v_type varchar2(200),
POL_CHECK_DATE date );

TYPE client_prop_pols_ref IS REF CURSOR RETURN client_prop_pols_rec;
PROCEDURE OrdClientPropPol(v_PropPol OUT client_prop_pols_ref,
                            v_clntCode  NUMBER);
PROCEDURE GrpClientPropPol(v_PropPol OUT client_prop_pols_ref,
                            v_clntCode  NUMBER);
PROCEDURE GisClientPropPol(v_PropPol OUT client_prop_pols_ref,
                            v_clntCode  NUMBER,V_TYPE IN VARCHAR2) ;
                            
PROCEDURE servicerequestsproc (
   v_addedit                 VARCHAR2,
   v_tsrcode        IN OUT   NUMBER,
   v_tsrccode                NUMBER,
   v_tsrtype                 VARCHAR2,
   v_tsracctype              VARCHAR2,
   v_tsracccode              NUMBER,
   v_tsrdate                 DATE,
   v_tsrasignee              NUMBER,
   v_tsrowntype              VARCHAR2,
   v_tsrowner                NUMBER,
   v_tsrstatus               VARCHAR2,
   v_duedate                 DATE,
   v_resoldate               DATE,
   v_tsrsummary              VARCHAR2,
   v_tsrdesc                 VARCHAR2,
   v_tsrsolution             VARCHAR2,
   v_tsrtcbcode              NUMBER DEFAULT NULL,
   v_tsrmode                 VARCHAR2 DEFAULT NULL,
   v_tsr_comments            VARCHAR2 DEFAULT NULL,
   v_tsr_srt_code NUMBER DEFAULT NULL
);
TYPE serv_requests_rec IS RECORD(
TSR_CODE                        TQC_SERV_REQUESTS.TSR_CODE%TYPE, 
TSR_TSRC_CODE                   TQC_SERV_REQUESTS.TSR_TSRC_CODE%TYPE, 
TSR_TYPE                        TQC_SERV_REQUESTS.TSR_TYPE%TYPE, 
TSR_ACC_TYPE                    TQC_SERV_REQUESTS.TSR_ACC_TYPE%TYPE, 
TSR_ACC_CODE                    TQC_SERV_REQUESTS.TSR_ACC_CODE%TYPE, 
TSR_DATE                        TQC_SERV_REQUESTS.TSR_DATE%TYPE, 
TSR_ASSIGNEE                    TQC_SERV_REQUESTS.TSR_ASSIGNEE%TYPE, 
TSR_OWNER_TYPE                  TQC_SERV_REQUESTS.TSR_OWNER_TYPE%TYPE, 
TSR_OWNER_CODE                  TQC_SERV_REQUESTS.TSR_OWNER_CODE%TYPE, 
TSR_STATUS                      TQC_SERV_REQUESTS.TSR_STATUS%TYPE, 
TSR_DUE_DATE                    TQC_SERV_REQUESTS.TSR_DUE_DATE%TYPE, 
TSR_RESOLUTION_DATE             TQC_SERV_REQUESTS.TSR_RESOLUTION_DATE%TYPE, 
TSR_SUMMARY                     TQC_SERV_REQUESTS.TSR_SUMMARY%TYPE, 
TSR_DESCRIPTION                 TQC_SERV_REQUESTS.TSR_DESCRIPTION%TYPE, 
TSR_SOLUTION                    TQC_SERV_REQUESTS.TSR_SOLUTION%TYPE,
TSRC_NAME                       TQC_SERV_REQ_CAT.TSRC_NAME%TYPE,
ACC_TYPE                        VARCHAR2(20),
ASSIGNEE                        VARCHAR2(100),
ACCOUNT_NAME                    VARCHAR2(100),
OWNER_TYPE                      VARCHAR2(20),
OWNER                           VARCHAR2(100),
TSR_COMM_MODE                   TQC_SERV_REQUESTS.TSR_COMM_MODE%TYPE,
TSR_REF_NUMBER                   TQC_SERV_REQUESTS.TSR_REF_NUMBER%TYPE,
TSR_COMMENTS                   TQC_SERV_REQUESTS.TSR_COMMENTS%TYPE,
CLNT_SHT_DESC                   TQC_CLIENTS.CLNT_SHT_DESC%TYPE,
CLNT_PHYSICAL_ADDRS                   TQC_CLIENTS.CLNT_PHYSICAL_ADDRS%TYPE,
CLNT_EMAIL_ADDRS                   TQC_CLIENTS.CLNT_EMAIL_ADDRS%TYPE,
CLNT_SMS_TEL                   TQC_CLIENTS.CLNT_SMS_TEL%TYPE,
TSR_SRT_CODE                   TQC_SERV_REQUESTS.TSR_SRT_CODE%TYPE,
SRT_DESC                   TQC_SERV_REQ_TYPES.SRT_DESC%TYPE

);


TYPE serv_requests_ref IS REF CURSOR RETURN  serv_requests_rec;
FUNCTION getClientOpenRequests(v_clientCode     NUMBER,v_tsr_acc_type in varchar2) RETURN serv_requests_ref;
FUNCTION getClientAllOpenRequests(v_clientCode     NUMBER) RETURN serv_requests_ref;
FUNCTION getAllClientRequests(v_clientCode     NUMBER,V_TSR_ACC_TYPE IN VARCHAR2) RETURN serv_requests_ref;
FUNCTION getUserOpenRequests(v_userCode     NUMBER) RETURN serv_requests_ref;
FUNCTION getUserOverdueRequests(v_userCode     NUMBER) RETURN serv_requests_ref;
FUNCTION getOverdueRequests(v_tsr_status in varchar2) RETURN serv_requests_ref;
FUNCTION getAllPendingRequests RETURN serv_requests_ref;

TYPE overdueCount_rec IS RECORD(
COUNT_NO                        NUMBER,
ASSIGNEE                        VARCHAR2(100));

TYPE overdueCount_ref IS REF CURSOR RETURN overdueCount_rec;
FUNCTION getOverdueRequestsChat RETURN overdueCount_ref;

TYPE kb_topics_rec IS RECORD(
TKBT_ID                 TQC_KB_TOPICS.TKBT_ID%TYPE, 
TKBT_ORDER              TQC_KB_TOPICS.TKBT_ORDER%TYPE, 
TKBT_SHT_DESC           TQC_KB_TOPICS.TKBT_SHT_DESC%TYPE, 
TKBT_DESC               TQC_KB_TOPICS.TKBT_DESC%TYPE, 
TKBT_PARENT_ID          TQC_KB_TOPICS.TKBT_PARENT_ID%TYPE);


TYPE kb_topics_ref IS REF CURSOR RETURN kb_topics_rec;
FUNCTION getRootTopics RETURN kb_topics_ref;
FUNCTION getChildTopics(v_tkbt_id   NUMBER) RETURN kb_topics_ref;

PROCEDURE kbTopicProc(v_addEdit         VARCHAR2,
                      v_tkbtId          NUMBER,
                      v_tkbtorder       NUMBER,
                      v_tkbtShtDesc     VARCHAR2,
                      v_tkbtDesc        VARCHAR2,
                      v_tkbtParent      NUMBER);

TYPE kb_content_rec IS RECORD(
TKBC_ID                 TQC_KB_CONTENT.TKBC_ID%TYPE, 
TKBC_TKBT_ID            TQC_KB_CONTENT.TKBC_TKBT_ID%TYPE, 
TKBC_ORDER              TQC_KB_CONTENT.TKBC_ORDER%TYPE, 
TKBC_CONTENT            TQC_KB_CONTENT.TKBC_CONTENT%TYPE);

TYPE kb_content_ref IS REF CURSOR RETURN kb_content_rec;
FUNCTION getKbContent(v_tkbt_id   NUMBER) RETURN kb_content_ref;


PROCEDURE kbContentProc(v_addEdit   VARCHAR2,
                        v_tkbcId    NUMBER,
                        v_tkbtId    NUMBER,
                        v_tkbcOrder NUMBER,
                        v_tkbcCont  VARCHAR2);


TYPE cat_Branches_rec IS RECORD(
BRN_CODE               tqc_branches.BRN_CODE%TYPE, 
BRN_SHT_DESC               tqc_branches.BRN_SHT_DESC%TYPE, 
BRN_REG_CODE           tqc_branches.BRN_REG_CODE%TYPE,
BRN_NAME           tqc_branches.BRN_NAME%TYPE);

TYPE  cat_Branches_ref IS REF CURSOR RETURN cat_Branches_rec;
FUNCTION getCategoryBranches RETURN cat_Branches_ref;
function GENERATE_SERV_REQ_REF (v_tsrType IN VARCHAR) RETURN VARCHAR2;


TYPE departments_rec IS RECORD(
DEP_CODE               TQC_DEPARTMENTS.DEP_CODE%TYPE, 
DEP_SHT_DESC               TQC_DEPARTMENTS.DEP_SHT_DESC%TYPE, 
DEP_NAME           TQC_DEPARTMENTS.DEP_NAME%TYPE,
DEP_WEF           TQC_DEPARTMENTS.DEP_WEF%TYPE,
DEP_WET           TQC_DEPARTMENTS.DEP_WET%TYPE);
TYPE  departments_ref IS REF CURSOR RETURN departments_rec;
FUNCTION get_departments RETURN departments_ref;

TYPE serv_request_types_rec IS RECORD(
SRT_CODE               TQC_SERV_REQ_TYPES.SRT_CODE%TYPE, 
SRT_SHT_DESC               TQC_SERV_REQ_TYPES.SRT_SHT_DESC%TYPE, 
SRT_DESC           TQC_SERV_REQ_TYPES.SRT_DESC%TYPE);
TYPE  serv_request_types_ref IS REF CURSOR RETURN serv_request_types_rec;
 FUNCTION get_serv_request_types RETURN serv_request_types_ref ;
 PROCEDURE update_serv_request_types (
   v_addedit             VARCHAR2,
   v_srt_code       IN   NUMBER,
   v_srt_desc   IN   VARCHAR2,
   v_srt_sht_desc      IN   VARCHAR2
);


TYPE serv_providers_rec IS RECORD(
SPR_CODE               tqc_service_providers.SPR_CODE%TYPE, 
SPR_SHT_DESC               tqc_service_providers.SPR_SHT_DESC%TYPE, 
SPR_NAME           tqc_service_providers.SPR_NAME%TYPE,
SPR_PHYSICAL_ADDRESS               tqc_service_providers.SPR_PHYSICAL_ADDRESS%TYPE, 
SPR_POSTAL_ADDRESS               tqc_service_providers.SPR_POSTAL_ADDRESS%TYPE, 
SPR_PHONE           tqc_service_providers.SPR_PHONE%TYPE,

SPR_EMAIL               tqc_service_providers.SPR_EMAIL%TYPE, 
SPR_MOBILE_NO           tqc_service_providers.SPR_MOBILE_NO%TYPE,
SPR_SMS_NUMBER           tqc_service_providers.SPR_SMS_NUMBER%TYPE);
TYPE  serv_providers_ref IS REF CURSOR RETURN serv_providers_rec;
 FUNCTION get_serv_providers RETURN serv_providers_ref;
 FUNCTION getAllPendingRequests(v_tsr_assignee in varchar2) RETURN serv_requests_ref ;
 PROCEDURE update_serv_req_other_dtls (
   v_addedit                      VARCHAR2,
   v_srid_code               IN OUT  NUMBER,
   v_srid_name               IN   VARCHAR2,
   v_srid_telephone          IN   VARCHAR2,
   v_srid_email_address      IN   VARCHAR2,
   v_srid_physical_address   IN   VARCHAR2,
   v_srid_id_number          IN   VARCHAR2
);


TYPE service_requesters_rec IS RECORD(
SRID_CODE               tqc_serv_req_ind_dtls.SRID_CODE%TYPE, 
SRID_NAME               tqc_serv_req_ind_dtls.SRID_NAME%TYPE, 
SRID_TELEPHONE           tqc_serv_req_ind_dtls.SRID_TELEPHONE%TYPE,
SRID_EMAIL_ADDRESS               tqc_serv_req_ind_dtls.SRID_EMAIL_ADDRESS%TYPE, 
SRID_PHYSICAL_ADDRESS               tqc_serv_req_ind_dtls.SRID_PHYSICAL_ADDRESS%TYPE, 
SRID_ID_NUMBER           tqc_serv_req_ind_dtls.SRID_ID_NUMBER%TYPE
);
TYPE  service_requesters_ref IS REF CURSOR RETURN service_requesters_rec;
FUNCTION get_other_service_requesters (
   v_srid_name        IN   VARCHAR2,
   v_srid_id_number   IN   VARCHAR2
)
   RETURN service_requesters_ref;
   
   TYPE Agent_commission_rec IS RECORD(
cop_agn_code               gin_commission_pymts.cop_agn_code%TYPE, 
agn_name               tqc_agencies.agn_name%TYPE, 
cop_date           gin_commission_pymts.cop_date%TYPE,
cop_cr_ref_no               gin_commission_pymts.cop_cr_ref_no%TYPE, 
cop_dr_ref_no               gin_commission_pymts.cop_dr_ref_no%TYPE, 
cop_comm_amt           gin_commission_pymts.cop_comm_amt%TYPE,
cop_whdtax_amt               gin_commission_pymts.cop_whdtax_amt%TYPE, 
cop_net_comm               gin_commission_pymts.cop_net_comm%TYPE, 
cop_cur_code           gin_commission_pymts.cop_cur_code%TYPE,

cop_authorized               gin_commission_pymts.cop_authorized%TYPE, 
cop_authorized_by           gin_commission_pymts.cop_authorized_by%TYPE,
cop_paid               gin_commission_pymts.cop_paid%TYPE, 
cop_paid_chq_date               gin_commission_pymts.cop_paid_chq_date%TYPE, 
cop_paid_chq_no           gin_commission_pymts.cop_paid_chq_no%TYPE
);
TYPE  Agent_commission_ref IS REF CURSOR RETURN Agent_commission_rec;
   
   FUNCTION get_Agent_commission(v_agn_code in number)
   RETURN Agent_commission_ref;
END TQC_SERVICE_REQUESTS030215; 
 
/