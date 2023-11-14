--
-- TQC_CAMPAIGN_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_campaign_pkg
AS
/******************************************************************************
   NAME:       TQC_CAMPAIGN_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        4/14/2011             1. Created this package.
******************************************************************************/
   FUNCTION myfunction (param1 IN NUMBER)
      RETURN NUMBER;

   PROCEDURE myprocedure (param1 IN NUMBER);

   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL);

   PROCEDURE tqc_campaigns_prc (
      v_add_edit                VARCHAR2,
      v_cmp_code                tqc_campaigns.cmp_code%TYPE,
      v_cmp_usr_code            tqc_campaigns.cmp_usr_code%TYPE,
      v_cmp_team_usr_code       tqc_campaigns.cmp_team_usr_code%TYPE,
      v_cmp_date                tqc_campaigns.cmp_date%TYPE,
      v_cmp_sponsor             tqc_campaigns.cmp_sponsor%TYPE,
      v_cmp_org_code            tqc_campaigns.cmp_org_code%TYPE,
      v_cmp_prod_code           tqc_campaigns.cmp_prod_code%TYPE,
      v_cmp_sys_code            tqc_campaigns.cmp_sys_code%TYPE,
      v_cmp_name                tqc_campaigns.cmp_name%TYPE,
      v_cmp_type                tqc_campaigns.cmp_type%TYPE,
      v_cmp_status              tqc_campaigns.cmp_status%TYPE,
      v_cmp_expt_close_date     tqc_campaigns.cmp_expt_close_date%TYPE,
      v_cmp_tar_audiance        tqc_campaigns.cmp_tar_audiance%TYPE,
      v_cmp_tar_size            tqc_campaigns.cmp_tar_size%TYPE,
      v_cmp_num_sent            tqc_campaigns.cmp_num_sent%TYPE,
      v_cmp_bgt_cost            tqc_campaigns.cmp_bgt_cost%TYPE,
      v_cmp_actl_cost           tqc_campaigns.cmp_actl_cost%TYPE,
      v_cmp_expt_cost           tqc_campaigns.cmp_expt_cost%TYPE,
      v_cmp_expt_revenue        tqc_campaigns.cmp_expt_revenue%TYPE,
      v_cmp_expt_sales_cnt      tqc_campaigns.cmp_expt_sales_cnt%TYPE,
      v_cmp_actl_sales_cnt      tqc_campaigns.cmp_actl_sales_cnt%TYPE,
      v_cmp_actl_response_cnt   tqc_campaigns.cmp_actl_response_cnt%TYPE,
      v_cmp_expt_response_cnt   tqc_campaigns.cmp_expt_response_cnt%TYPE,
      v_cmp_expt_roi            tqc_campaigns.cmp_expt_roi%TYPE,
      v_cmp_actl_roi            tqc_campaigns.cmp_actl_roi%TYPE,
      v_cmp_desc                tqc_campaigns.cmp_desc%TYPE,
      v_cmp_cur_code            tqc_campaigns.cmp_cur_code%TYPE,
      v_cmp_objective           tqc_campaigns.cmp_objective%TYPE,
      v_cmp_impression_cnt      tqc_campaigns.cmp_impression_cnt%TYPE,
      v_cmp_event               tqc_campaigns.cmp_event%TYPE,
      v_cmp_venue               tqc_campaigns.cmp_venue%TYPE,
      v_cmp_event_time          tqc_campaigns.cmp_event_time%TYPE
   );

   PROCEDURE tqc_camp_activities_prc (
      v_add_edit       VARCHAR2,
      v_cma_code       tqc_camp_activities.cma_code%TYPE,
      v_cma_cmp_code   tqc_camp_activities.cma_cmp_code%TYPE,
      v_cma_act_code   tqc_camp_activities.cma_act_code%TYPE
   );

   PROCEDURE tqc_camp_contact_prc (
      v_add_edit       VARCHAR2,
      v_cmc_code       tqc_camp_contact.cmc_code%TYPE,
      v_cmc_agn_code   tqc_camp_contact.cmc_agn_code%TYPE,
      v_cmc_date       tqc_camp_contact.cmc_date%TYPE,
      v_cmc_cmp_code   tqc_camp_contact.cmc_code%TYPE
   );

   PROCEDURE save_campaign_targets (
      v_action     VARCHAR2,
      v_cmt_code   NUMBER,
      v_cmp_code   NUMBER,
      v_acc_code   NUMBER
   );

   PROCEDURE save_campaign_message (
      v_action      VARCHAR2,
      v_cmsg_code   NUMBER,
      v_msg_type    VARCHAR2,
      v_subj        VARCHAR2,
      v_body        VARCHAR2,
      v_send_all    VARCHAR2,
      v_cmp_code    NUMBER
   );

   PROCEDURE send_campaign_msg_email (
      v_cmsg_code   NUMBER,
      v_cmp_code    NUMBER,
      v_username    VARCHAR2
   );

   PROCEDURE savecampaignproducts (
      v_action               VARCHAR2,
      v_cpro_code       IN   NUMBER,
      v_cpro_pro_code   IN   NUMBER,
      v_cpro_sht_desc   IN   VARCHAR2,
      v_cpro_cmp_code   IN   NUMBER
   );
END tqc_campaign_pkg; 
/