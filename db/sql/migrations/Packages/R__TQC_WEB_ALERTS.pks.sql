--
-- TQC_WEB_ALERTS  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_web_alerts
AS
/******************************************************************************
 NAME: TQ_HRMS.HRMS_WEB_ALERTS
 PURPOSE:

 REVISIONS:
 Ver Date Author Description
 --------- ---------- --------------- ------------------------------------
 1.0 17/Mar/2010 1. Created this package.
******************************************************************************/
   TYPE serv_requests_rec IS RECORD (
      tsr_code          tqc_serv_requests.tsr_code%TYPE,
      tsr_type          tqc_serv_requests.tsr_type%TYPE,
      tsr_date          tqc_serv_requests.tsr_date%TYPE,
      tsr_assignee      tqc_serv_requests.tsr_assignee%TYPE,
      tsr_status        tqc_serv_requests.tsr_status%TYPE,
      tsr_due_date      tqc_serv_requests.tsr_due_date%TYPE,
      tsr_summary       tqc_serv_requests.tsr_summary%TYPE,
      tsr_description   tqc_serv_requests.tsr_description%TYPE,
      tsrc_name         tqc_serv_req_cat.tsrc_name%TYPE,
      acc_type          VARCHAR2 (20),
      assignee          VARCHAR2 (100),
      account_name      VARCHAR2 (100),
      owner_type        VARCHAR2 (20),
      owner             VARCHAR2 (100),
      assignee_email    VARCHAR2 (100),
      owner_email       VARCHAR2 (100)
   );

   TYPE serv_requests_ref IS REF CURSOR
      RETURN serv_requests_rec;

   FUNCTION getoverduerequests (v_tsr_status IN VARCHAR2)
      RETURN serv_requests_ref;

   TYPE alerts_rec IS RECORD (
      alert_name      VARCHAR2 (20),
      email_address   VARCHAR2 (100),
      email_content   VARCHAR2 (400)
   );

   TYPE alerts_ref IS REF CURSOR
      RETURN alerts_rec;

   FUNCTION getbirthdayalert
      RETURN alerts_ref;

   FUNCTION getholidaysalert
      RETURN alerts_ref;
 PROCEDURE payment_group_alrt(
     v_grp_usr_code IN NUMBER, 
	 v_account_no   IN VARCHAR2, 
	 v_account_type IN VARCHAR2, 
     v_currency     IN VARCHAR2, 
     v_amt          IN NUMBER,
	 v_paymode      IN   VARCHAR2, 
     v_3rdparty_transno   IN  VARCHAR2, 
	 v_timestamp          IN VARCHAR2,
	 v_receipt_no         IN VARCHAR2,
     v_policy_no          IN VARCHAR2,
     v_claim_no           IN VARCHAR2,
     v_quot_no            IN VARCHAR2,
	 v_pro_code           IN NUMBER, 
     v_si                 IN NUMBER, 
	 v_clnt_code   IN NUMBER, 
	 v_agn_code    IN NUMBER, 
	 v_posted_by   IN VARCHAR2
 );
PROCEDURE new_account_info_alert(v_agn_code IN number, v_new_pwd IN VARCHAR2,v_posted_by IN VARCHAR2);      
END tqc_web_alerts; 
/