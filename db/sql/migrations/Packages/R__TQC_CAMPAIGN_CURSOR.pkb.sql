--
-- TQC_CAMPAIGN_CURSOR  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_campaign_cursor
AS
/******************************************************************************
   NAME:       TQC_CAMPAIGN_CURSOR
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        4/14/2011             1. Created this package body.
******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   FUNCTION get_product_desc (v_sys_code IN NUMBER, v_prod_code IN NUMBER)
      RETURN VARCHAR
   IS
      v_prod_desc      VARCHAR (100);
      v_sys_sht_desc   VARCHAR (10);
   BEGIN
      BEGIN
         SELECT sys_sht_desc
           INTO v_sys_sht_desc
           FROM tqc_systems
          WHERE sys_code = v_sys_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_prod_desc := 'NOT FOUND';
      END;

      IF UPPER (v_sys_sht_desc) = 'LMS'
      THEN
         BEGIN
            SELECT prod_desc
              INTO v_prod_desc
              FROM lms_products
             WHERE prod_code = v_prod_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_prod_desc := 'NOT FOUND';
         END;
      ELSIF UPPER (v_sys_sht_desc) = 'GIS'
      THEN
         BEGIN
            SELECT pro_desc
              INTO v_prod_desc
              FROM gin_products
             WHERE pro_code = v_prod_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_prod_desc := 'NOT SPECIFIED';
         END;
      --ELSIF UPPER(v_sys_sht_desc)='MED' THEN
      -- SELECT PDCT_DESCRIPTION INTO v_prod_desc FROM MED_PRODUCTS WHERE PDCT_ID=v_prod_code;
      ELSE
         v_prod_desc := 'NOT FOUND';
      END IF;

      RETURN (v_prod_desc);
   END get_product_desc;

   PROCEDURE get_cammpaigns (
      v_cmp_code         IN       tqc_campaigns.cmp_code%TYPE,
      v_cmp_ref_cursor   OUT      campaign_ref
   )
   IS
   BEGIN
      OPEN v_cmp_ref_cursor FOR
         SELECT cmp_code, cmp_usr_code,
                (SELECT usr_username
                   FROM tqc_users
                  WHERE usr_code = cmp_usr_code) cmp_user_name,
                cmp_team_usr_code,
                (SELECT usr_username
                   FROM tqc_users
                  WHERE usr_code = cmp_team_usr_code) cmp_team_name,
                cmp_date, cmp_sponsor, cmp_org_code,
                (SELECT org_name
                   FROM tqc_organizations
                  WHERE org_code = cmp_org_code) cmp_org_name, cmp_prod_code,
                (SELECT tqc_campaign_cursor.get_product_desc
                                                 (cmp_sys_code,
                                                  cmp_prod_code
                                                 )
                   FROM DUAL) cmp_prod_name,
                cmp_sys_code, (SELECT sys_name
                                 FROM tqc_systems
                                WHERE sys_code = cmp_sys_code) cmp_sys_name,
                cmp_name, cmp_type, cmp_status, cmp_expt_close_date,
                cmp_tar_audiance, cmp_tar_size, cmp_num_sent, cmp_bgt_cost,
                cmp_actl_cost, cmp_expt_cost, cmp_expt_revenue,
                cmp_expt_sales_cnt, cmp_actl_sales_cnt,
                cmp_actl_response_cnt, cmp_expt_response_cnt, cmp_expt_roi,
                cmp_actl_roi, cmp_desc, cmp_cur_code,
                (SELECT cur_desc || '(' || cur_symbol || ')'
                   FROM tqc_currencies
                  WHERE cur_code = cmp_cur_code) cmp_cur_name, cmp_objective,
                cmp_impression_cnt, cmp_event, cmp_venue, cmp_event_time
           FROM tqc_campaigns
          WHERE cmp_code = NVL (v_cmp_code, cmp_code);
   END get_cammpaigns;

   FUNCTION get_camp_systems
      RETURN system_ref
   IS
      v_system_cursor   system_ref;
   BEGIN
      OPEN v_system_cursor FOR
         SELECT sys_code, sys_sht_desc, sys_name
           FROM tqc_systems
          WHERE sys_code IN (27, 37);

      RETURN (v_system_cursor);
   END get_camp_systems;

   PROCEDURE get_campaign_products (
      v_sys_code      IN       tqc_systems.sys_code%TYPE,
      v_prod_cursor   OUT      cmp_product_ref
   )
   IS
   BEGIN
      IF v_sys_code = 27
      THEN
         OPEN v_prod_cursor FOR
            SELECT prod_code, prod_sht_desc, prod_desc, 'LMS' prod_system
              FROM lms_products;
      ELSIF v_sys_code = 37
      THEN
         OPEN v_prod_cursor FOR
            SELECT pro_code prod_code, pro_sht_desc prod_sht_desc,
                   pro_desc prod_desc, 'GIS' prod_system
              FROM gin_products;
      ELSIF v_sys_code IS NULL
      THEN
         OPEN v_prod_cursor FOR
            SELECT prod_code, prod_sht_desc, prod_desc, 'LMS' prod_system
              FROM lms_products
            UNION
            SELECT pro_code prod_code, pro_sht_desc prod_sht_desc,
                   pro_desc prod_desc, 'GIS' prod_system
              FROM gin_products;
      END IF;
   END get_campaign_products;

   PROCEDURE get_camp_activities (
      v_cma_cmp_code   IN       tqc_camp_activities.cma_cmp_code%TYPE,
      vcampactcursor   OUT      camp_activity_ref
   )
   IS
   BEGIN
      OPEN vcampactcursor FOR
         SELECT cma_code, cma_cmp_code, cma_act_code, act_subject,
                act_description,act_wef,act_wet
           FROM tqc_camp_activities, tqc_campaigns, tqc_activities
          WHERE cma_cmp_code = cmp_code
            AND cma_act_code = act_code
            AND cma_cmp_code = v_cma_cmp_code;
   END get_camp_activities;

   PROCEDURE get_camp_targets (
      vcampactcursor   OUT      camp_targets_ref,
      v_cmp_code       IN       tqc_campaigns.cmp_code%TYPE
   )
   IS
   BEGIN
      OPEN vcampactcursor FOR
         SELECT cmt_code, cmt_acc_code, cmt_date,
                (SELECT agn_name
                   FROM tqc_agencies
                  WHERE agn_code IN (
                                SELECT acc_type_code
                                  FROM tqc_accounts
                                 WHERE acc_code =
                                                 cmt_acc_code)
                 UNION
                 SELECT clnt_name
                   FROM tqc_clients
                  WHERE clnt_code IN (
                                SELECT acc_type_code
                                  FROM tqc_accounts
                                 WHERE acc_code =
                                                 cmt_acc_code)
                 UNION
                 SELECT spr_name
                   FROM tqc_service_providers
                  WHERE spr_code IN (
                                SELECT acc_type_code
                                  FROM tqc_accounts
                                 WHERE acc_code =
                                                 cmt_acc_code))
                                                             related_account,
                (SELECT agn_email_address
                   FROM tqc_agencies
                  WHERE agn_code IN (
                                  SELECT acc_type_code
                                    FROM tqc_accounts
                                   WHERE acc_code =
                                                   cmt_acc_code)
                 UNION
                 SELECT clnt_email_addrs
                   FROM tqc_clients
                  WHERE clnt_code IN (
                                  SELECT acc_type_code
                                    FROM tqc_accounts
                                   WHERE acc_code =
                                                   cmt_acc_code)
                 UNION
                 SELECT spr_email
                   FROM tqc_service_providers
                  WHERE spr_code IN (
                                  SELECT acc_type_code
                                    FROM tqc_accounts
                                   WHERE acc_code =
                                                   cmt_acc_code))
                                                               related_email
           FROM tqc_camp_targets
          WHERE cmt_cmp_code = v_cmp_code;
   END;

   PROCEDURE get_undefined_target_accounts (
      v_cmp_code         IN       tqc_campaigns.cmp_code%TYPE,
      v_account_cursor   OUT      tqc_activities_cursor.account_ref
   )
   IS
   BEGIN
      OPEN v_account_cursor FOR
         SELECT   acc_code, 'Agency' acc_type, acc_type_code,
                  agn_name acc_name, agn_email_address acc_email
             FROM tqc_agencies, tqc_accounts
            WHERE acc_type_code = agn_code
              AND acc_code NOT IN (
                       SELECT cmt_acc_code
                         FROM tqc_camp_targets, tqc_campaigns
                        WHERE cmt_cmp_code = cmp_code
                              AND cmp_code = v_cmp_code)
         UNION
         SELECT   acc_code, 'Client' acc_type, acc_type_code,
                  clnt_name acc_name, clnt_email_addrs acc_email
             FROM tqc_clients, tqc_accounts
            WHERE acc_type_code = clnt_code
              AND acc_code NOT IN (
                       SELECT cmt_acc_code
                         FROM tqc_camp_targets, tqc_campaigns
                        WHERE cmt_cmp_code = cmp_code
                              AND cmp_code = v_cmp_code)
         UNION
         SELECT   acc_code, 'Service Provider' acc_type, acc_type_code,
                  spr_name acc_name, spr_email acc_email
             FROM tqc_service_providers, tqc_accounts
            WHERE acc_type_code = spr_code
              AND acc_code NOT IN (
                       SELECT cmt_acc_code
                         FROM tqc_camp_targets, tqc_campaigns
                        WHERE cmt_cmp_code = cmp_code
                              AND cmp_code = v_cmp_code)
         ORDER BY acc_type;
   END get_undefined_target_accounts;

   PROCEDURE get_campaign_messages (
      vcampactcursor   OUT      camp_messages_ref,
      v_cmp_code       IN       tqc_campaigns.cmp_code%TYPE
   )
   IS
   BEGIN
      OPEN vcampactcursor FOR
         SELECT cmsg_code, cmsg_type, cmsg_type cmsg_type_desc, cmsg_subj,
                cmsg_body, cmsg_status,
                DECODE (cmsg_status,
                        'P', 'PENDING',
                        'S', 'SENT',
                        'Q', 'QUEUED'
                       ) cmsg_status_desc,
                cmsg_date, cmsg_send_all,
                DECODE (cmsg_send_all, 'Y', 'Yes', 'No') cmsg_send_all_desc
           FROM tqc_campaign_messages
          WHERE cmsg_cmp_code = v_cmp_code;
   END;

   PROCEDURE get_undefined_camp_activities (
      v_cmp_code          IN       tqc_campaigns.cmp_code%TYPE,
      v_activity_cursor   OUT      tqc_activities_cursor.activity_ref
   )
   IS
   BEGIN
      OPEN v_activity_cursor FOR
         SELECT act_code, act_acty_code,
                (SELECT acty_desc
                   FROM tqc_activity_types
                  WHERE acty_code = act_acty_code) act_type, act_wef,
                act_wet, act_duration, act_subject, act_location,
                act_assigned_to,
                (SELECT usr_name
                   FROM tqc_users
                  WHERE usr_code = act_assigned_to) assigned_user,
                act_related_to,
                (SELECT agn_name
                   FROM tqc_agencies
                  WHERE agn_code IN (
                              SELECT acc_type_code
                                FROM tqc_accounts
                               WHERE acc_code =
                                               act_related_to)
                 UNION
                 SELECT clnt_name
                   FROM tqc_clients
                  WHERE clnt_code IN (
                              SELECT acc_type_code
                                FROM tqc_accounts
                               WHERE acc_code =
                                               act_related_to)
                 UNION
                 SELECT spr_name
                   FROM tqc_service_providers
                  WHERE spr_code IN (
                              SELECT acc_type_code
                                FROM tqc_accounts
                               WHERE acc_code =
                                               act_related_to))
                                                             related_account,
                act_status_id, (SELECT status_desc
                                  FROM tqc_statuses
                                 WHERE status_id = act_status_id) status,
                act_description, act_reminder, act_team,
                (SELECT usr_name
                   FROM tqc_users
                  WHERE usr_code = act_team) team_name, act_reminder_time,
                NULL act_msgt_code, NULL msg_sht_dsc
           FROM tqc_activities
          WHERE act_code NOT IN (
                      SELECT cma_act_code
                        FROM tqc_camp_activities, tqc_campaigns
                       WHERE cma_cmp_code = cmp_code
                             AND cmp_code = v_cmp_code);
   END get_undefined_camp_activities;

   PROCEDURE get_all_products (
      v_all_products    OUT      all_products_ref,
      v_cpro_cmp_code   IN       VARCHAR2
   )
   IS
   BEGIN
--RAISE_ERROR('v_cpro_cmp_code'||v_cpro_cmp_code);
      OPEN v_all_products FOR
         SELECT cpro_code, cpro_pro_code, cpro_sht_desc, cpro_cmp_code,
                pro_sht_desc, pro_desc
           FROM tqc_campaign_products, gin_products
          WHERE cpro_pro_code = pro_code
            AND cpro_sht_desc = pro_sht_desc
            AND cpro_cmp_code = v_cpro_cmp_code
         UNION
         SELECT cpro_code, cpro_pro_code, cpro_sht_desc, cpro_cmp_code,
                prod_sht_desc, prod_desc
           FROM tqc_campaign_products, lms_products
          WHERE cpro_pro_code = prod_code
            AND cpro_sht_desc = prod_sht_desc
            AND cpro_cmp_code = v_cpro_cmp_code;
   END get_all_products;
END tqc_campaign_cursor; 
/