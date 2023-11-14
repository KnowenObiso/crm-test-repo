--
-- TQC_CAMPAIGN_CURSOR  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_campaign_cursor
AS
/******************************************************************************
   NAME:       TQC_CAMPAIGN_CURSOR
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        4/14/2011             1. Created this package.
******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL);

   FUNCTION get_product_desc (v_sys_code IN NUMBER, v_prod_code IN NUMBER)
      RETURN VARCHAR;

   TYPE campaign_rec IS RECORD (
      cmp_code                tqc_campaigns.cmp_code%TYPE,
      cmp_usr_code            tqc_campaigns.cmp_usr_code%TYPE,
      cmp_user_name           VARCHAR2 (100 BYTE),
      cmp_team_usr_code       tqc_campaigns.cmp_team_usr_code%TYPE,
      cmp_team_name           VARCHAR2 (100),
      cmp_date                tqc_campaigns.cmp_date%TYPE,
      cmp_sponsor             tqc_campaigns.cmp_sponsor%TYPE,
      cmp_org_code            tqc_campaigns.cmp_org_code%TYPE,
      cmp_org_name            VARCHAR2 (100),
      cmp_prod_code           tqc_campaigns.cmp_prod_code%TYPE,
      cmp_prod_name           VARCHAR2 (100),
      cmp_sys_code            tqc_campaigns.cmp_sys_code%TYPE,
      cmp_sys_name            VARCHAR2 (100),
      cmp_name                tqc_campaigns.cmp_name%TYPE,
      cmp_type                tqc_campaigns.cmp_type%TYPE,
      cmp_status              tqc_campaigns.cmp_status%TYPE,
      cmp_expt_close_date     tqc_campaigns.cmp_expt_close_date%TYPE,
      cmp_tar_audiance        tqc_campaigns.cmp_tar_audiance%TYPE,
      cmp_tar_size            tqc_campaigns.cmp_tar_size%TYPE,
      cmp_num_sent            tqc_campaigns.cmp_num_sent%TYPE,
      cmp_bgt_cost            tqc_campaigns.cmp_bgt_cost%TYPE,
      cmp_actl_cost           tqc_campaigns.cmp_actl_cost%TYPE,
      cmp_expt_cost           tqc_campaigns.cmp_expt_cost%TYPE,
      cmp_expt_revenue        tqc_campaigns.cmp_expt_revenue%TYPE,
      cmp_expt_sales_cnt      tqc_campaigns.cmp_expt_sales_cnt%TYPE,
      cmp_actl_sales_cnt      tqc_campaigns.cmp_actl_sales_cnt%TYPE,
      cmp_actl_response_cnt   tqc_campaigns.cmp_actl_response_cnt%TYPE,
      cmp_expt_response_cnt   tqc_campaigns.cmp_expt_response_cnt%TYPE,
      cmp_expt_roi            tqc_campaigns.cmp_expt_roi%TYPE,
      cmp_actl_roi            tqc_campaigns.cmp_actl_roi%TYPE,
      cmp_desc                tqc_campaigns.cmp_desc%TYPE,
      cmp_cur_code            tqc_campaigns.cmp_cur_code%TYPE,
      cmp_cur_name            VARCHAR2 (100),
      cmp_objective           tqc_campaigns.cmp_objective%TYPE,
      cmp_impression_cnt      tqc_campaigns.cmp_impression_cnt%TYPE,
      cmp_event               tqc_campaigns.cmp_event%TYPE,
      cmp_venue               tqc_campaigns.cmp_venue%TYPE,
      cmp_event_time          tqc_campaigns.cmp_event_time%TYPE
   );

   TYPE campaign_ref IS REF CURSOR
      RETURN campaign_rec;

   PROCEDURE get_cammpaigns (
      v_cmp_code         IN       tqc_campaigns.cmp_code%TYPE,
      v_cmp_ref_cursor   OUT      campaign_ref
   );

   TYPE system_rec IS RECORD (
      sys_code       tqc_systems.sys_code%TYPE,
      sys_sht_desc   tqc_systems.sys_sht_desc%TYPE,
      sys_name       tqc_systems.sys_name%TYPE
   );

   TYPE system_ref IS REF CURSOR
      RETURN system_rec;

   FUNCTION get_camp_systems
      RETURN system_ref;

   TYPE cmp_product_rec IS RECORD (
      prod_code       lms_products.prod_code%TYPE,
      prod_sht_desc   lms_products.prod_sht_desc%TYPE,
      prod_desc       lms_products.prod_desc%TYPE,
      prod_system     VARCHAR2 (30)
   );

   TYPE cmp_product_ref IS REF CURSOR
      RETURN cmp_product_rec;

   PROCEDURE get_campaign_products (
      v_sys_code      IN       tqc_systems.sys_code%TYPE,
      v_prod_cursor   OUT      cmp_product_ref
   );

   TYPE camp_activity_rec IS RECORD (
      cma_code          tqc_camp_activities.cma_code%TYPE,
      cma_cmp_code      tqc_camp_activities.cma_cmp_code%TYPE,
      cma_act_code      tqc_camp_activities.cma_act_code%TYPE,
      act_subject       tqc_activities.act_subject%TYPE,
      act_description   tqc_activities.act_description%TYPE,
      act_wef           tqc_activities.act_wef%TYPE, 
      act_wet           tqc_activities.act_wet%TYPE
   );

   TYPE camp_activity_ref IS REF CURSOR
      RETURN camp_activity_rec;

   PROCEDURE get_camp_activities (
      v_cma_cmp_code   IN       tqc_camp_activities.cma_cmp_code%TYPE,
      vcampactcursor   OUT      camp_activity_ref
   );

   TYPE camp_targets_rec IS RECORD (
      cmt_code          tqc_camp_targets.cmt_code%TYPE,
      cmt_acc_code      tqc_camp_targets.cmt_acc_code%TYPE,
      cmt_date          tqc_camp_targets.cmt_date%TYPE,
      related_account   VARCHAR2 (100),
      related_email     VARCHAR2 (100)
   );

   TYPE camp_targets_ref IS REF CURSOR
      RETURN camp_targets_rec;

   PROCEDURE get_camp_targets (
      vcampactcursor   OUT      camp_targets_ref,
      v_cmp_code       IN       tqc_campaigns.cmp_code%TYPE
   );

   TYPE camp_messages_rec IS RECORD (
      cmsg_code            tqc_campaign_messages.cmsg_code%TYPE,
      cmsg_type            tqc_campaign_messages.cmsg_type%TYPE,
      cmsg_type_desc       VARCHAR2 (100),
      cmsg_subj            tqc_campaign_messages.cmsg_subj%TYPE,
      cmsg_body            tqc_campaign_messages.cmsg_body%TYPE,
      cmsg_status          tqc_campaign_messages.cmsg_status%TYPE,
      cmsg_status_desc     VARCHAR2 (100),
      cmsg_date            tqc_campaign_messages.cmsg_date%TYPE,
      cmsg_send_all        tqc_campaign_messages.cmsg_send_all%TYPE,
      cmsg_send_all_desc   VARCHAR2 (100)
   );

   TYPE camp_messages_ref IS REF CURSOR
      RETURN camp_messages_rec;

   PROCEDURE get_campaign_messages (
      vcampactcursor   OUT      camp_messages_ref,
      v_cmp_code       IN       tqc_campaigns.cmp_code%TYPE
   );

   PROCEDURE get_undefined_target_accounts (
      v_cmp_code         IN       tqc_campaigns.cmp_code%TYPE,
      v_account_cursor   OUT      tqc_activities_cursor.account_ref
   );

   PROCEDURE get_undefined_camp_activities (
      v_cmp_code          IN       tqc_campaigns.cmp_code%TYPE,
      v_activity_cursor   OUT      tqc_activities_cursor.activity_ref
   );

   TYPE all_products_rec IS RECORD (
      cpro_code       tqc_campaign_products.cpro_code%TYPE,
      cpro_pro_code   tqc_campaign_products.cpro_pro_code%TYPE,
      cpro_sht_desc   tqc_campaign_products.cpro_sht_desc%TYPE,
      cpro_cmp_code   tqc_campaign_products.cpro_cmp_code%TYPE,
      pro_sht_desc    gin_products.pro_sht_desc%TYPE,
      pro_desc        gin_products.pro_desc%TYPE
   );

   TYPE all_products_ref IS REF CURSOR
      RETURN all_products_rec;

   PROCEDURE get_all_products (
      v_all_products    OUT      all_products_ref,
      v_cpro_cmp_code   IN       VARCHAR2
   );
END tqc_campaign_cursor; 
/