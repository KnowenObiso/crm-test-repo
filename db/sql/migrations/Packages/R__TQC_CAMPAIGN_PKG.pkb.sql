--
-- TQC_CAMPAIGN_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_campaign_pkg
AS
/******************************************************************************
   NAME:       TQC_CAMPAIGN_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        4/14/2011             1. Created this package body.
******************************************************************************/
   FUNCTION myfunction (param1 IN NUMBER)
      RETURN NUMBER
   IS
   BEGIN
      RETURN param1;
   END;

   PROCEDURE myprocedure (param1 IN NUMBER)
   IS
      tmpvar   NUMBER;
   BEGIN
      tmpvar := param1;
   END;

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
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_campaigns
                        (cmp_code, cmp_usr_code,
                         cmp_team_usr_code, cmp_date, cmp_sponsor,
                         cmp_org_code, cmp_prod_code, cmp_sys_code,
                         cmp_name, cmp_type, cmp_status,
                         cmp_expt_close_date, cmp_tar_audiance,
                         cmp_tar_size, cmp_num_sent, cmp_bgt_cost,
                         cmp_actl_cost, cmp_expt_cost,
                         cmp_expt_revenue, cmp_expt_sales_cnt,
                         cmp_actl_sales_cnt, cmp_actl_response_cnt,
                         cmp_expt_response_cnt, cmp_expt_roi,
                         cmp_actl_roi, cmp_desc, cmp_cur_code,
                         cmp_objective, cmp_impression_cnt, cmp_event,
                         cmp_venue, cmp_event_time
                        )
                 VALUES (cmp_code_seq.NEXTVAL, v_cmp_usr_code,
                         v_cmp_team_usr_code, v_cmp_date, v_cmp_sponsor,
                         v_cmp_org_code, v_cmp_prod_code, v_cmp_sys_code,
                         v_cmp_name, v_cmp_type, v_cmp_status,
                         v_cmp_expt_close_date, v_cmp_tar_audiance,
                         v_cmp_tar_size, v_cmp_num_sent, v_cmp_bgt_cost,
                         v_cmp_actl_cost, v_cmp_expt_cost,
                         v_cmp_expt_revenue, v_cmp_expt_sales_cnt,
                         v_cmp_actl_sales_cnt, v_cmp_actl_response_cnt,
                         v_cmp_expt_response_cnt, v_cmp_expt_roi,
                         v_cmp_actl_roi, v_cmp_desc, v_cmp_cur_code,
                         v_cmp_objective, v_cmp_impression_cnt, v_cmp_event,
                         v_cmp_venue, v_cmp_event_time
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error inerting record');
         END;
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_campaigns
            SET cmp_usr_code = v_cmp_usr_code,
                cmp_team_usr_code = v_cmp_team_usr_code,
                cmp_date = v_cmp_date,
                cmp_sponsor = v_cmp_sponsor,
                cmp_org_code = v_cmp_org_code,
                cmp_prod_code = v_cmp_prod_code,
                cmp_sys_code = v_cmp_sys_code,
                cmp_name = v_cmp_name,
                cmp_type = v_cmp_type,
                cmp_status = cmp_status,
                cmp_expt_close_date = v_cmp_expt_close_date,
                cmp_tar_audiance = v_cmp_tar_audiance,
                cmp_tar_size = v_cmp_tar_size,
                cmp_num_sent = v_cmp_num_sent,
                cmp_bgt_cost = v_cmp_bgt_cost,
                cmp_actl_cost = v_cmp_actl_cost,
                cmp_expt_cost = v_cmp_expt_cost,
                cmp_expt_revenue = v_cmp_expt_revenue,
                cmp_expt_sales_cnt = v_cmp_expt_sales_cnt,
                cmp_actl_sales_cnt = v_cmp_actl_sales_cnt,
                cmp_actl_response_cnt = v_cmp_actl_response_cnt,
                cmp_expt_response_cnt = v_cmp_expt_response_cnt,
                cmp_expt_roi = v_cmp_expt_roi,
                cmp_actl_roi = v_cmp_actl_roi,
                cmp_desc = v_cmp_desc,
                cmp_cur_code = v_cmp_cur_code,
                cmp_objective = v_cmp_objective,
                cmp_impression_cnt = v_cmp_impression_cnt,
                cmp_event = v_cmp_event,
                cmp_venue = v_cmp_venue,
                cmp_event_time = v_cmp_event_time
          WHERE cmp_code = v_cmp_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE      tqc_campaigns
               WHERE cmp_code = v_cmp_code;
      END IF;
   END tqc_campaigns_prc;

   PROCEDURE tqc_camp_activities_prc (
      v_add_edit       VARCHAR2,
      v_cma_code       tqc_camp_activities.cma_code%TYPE,
      v_cma_cmp_code   tqc_camp_activities.cma_cmp_code%TYPE,
      v_cma_act_code   tqc_camp_activities.cma_act_code%TYPE
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_camp_activities
                     (cma_code, cma_cmp_code, cma_act_code
                     )
              VALUES (cma_code_seq.NEXTVAL, v_cma_cmp_code, v_cma_act_code
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_camp_activities
            SET cma_cmp_code = v_cma_cmp_code,
                cma_act_code = v_cma_act_code
          WHERE cma_code = v_cma_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE      tqc_camp_activities
               WHERE cma_code = v_cma_code;
      END IF;
   END tqc_camp_activities_prc;

   PROCEDURE tqc_camp_contact_prc (
      v_add_edit       VARCHAR2,
      v_cmc_code       tqc_camp_contact.cmc_code%TYPE,
      v_cmc_agn_code   tqc_camp_contact.cmc_agn_code%TYPE,
      v_cmc_date       tqc_camp_contact.cmc_date%TYPE,
      v_cmc_cmp_code   tqc_camp_contact.cmc_code%TYPE
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_camp_contact
                     (cmc_code, cmc_agn_code, cmc_date,
                      cmc_cmp_code
                     )
              VALUES (cmc_code_seq.NEXTVAL, v_cmc_agn_code, v_cmc_date,
                      v_cmc_cmp_code
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_camp_contact
            SET cmc_agn_code = v_cmc_agn_code,
                cmc_date = v_cmc_date,
                cmc_cmp_code = v_cmc_cmp_code
          WHERE cmc_code = v_cmc_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE      tqc_camp_contact
               WHERE cmc_code = v_cmc_code;
      END IF;
   END tqc_camp_contact_prc;

   PROCEDURE save_campaign_targets (
      v_action     VARCHAR2,
      v_cmt_code   NUMBER,
      v_cmp_code   NUMBER,
      v_acc_code   NUMBER
   )
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_camp_targets
                        (cmt_code, cmt_acc_code, cmt_date,
                         cmt_cmp_code
                        )
                 VALUES (cmt_code_seq.NEXTVAL, v_acc_code, TRUNC (SYSDATE),
                         v_cmp_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Attaching Target to Campaign.');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_camp_targets
                  WHERE cmt_code = v_cmt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting Target to Campaign.');
         END;
      ELSE
         raise_error ('Invalid Option Selected');
      END IF;
   END;

   PROCEDURE save_campaign_message (
      v_action      VARCHAR2,
      v_cmsg_code   NUMBER,
      v_msg_type    VARCHAR2,
      v_subj        VARCHAR2,
      v_body        VARCHAR2,
      v_send_all    VARCHAR2,
      v_cmp_code    NUMBER
   )
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_campaign_messages
                        (cmsg_code, cmsg_type, cmsg_cmp_code,
                         cmsg_subj, cmsg_body, cmsg_date, cmsg_send_all
                        )
                 VALUES (cmsg_code_seq.NEXTVAL, v_msg_type, v_cmp_code,
                         v_subj, v_body, TRUNC (SYSDATE), v_send_all
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Campaign Messages.');
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_campaign_messages
               SET cmsg_type = v_msg_type,
                   cmsg_subj = v_subj,
                   cmsg_body = v_body,
                   cmsg_send_all = v_send_all
             WHERE cmsg_code = v_cmsg_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating Campaign Messages');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_campaign_messages
                  WHERE cmsg_code = v_cmsg_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting Campaign Messages');
         END;
      ELSE
         raise_error ('Invalid Option Selected.');
      END IF;
   END;

   PROCEDURE send_campaign_msg_email (
      v_cmsg_code   NUMBER,
      v_cmp_code    NUMBER,
      v_username    VARCHAR2
   )
   IS
      CURSOR camp_targets
      IS
         SELECT tqc_camp_targets.*, tqc_campaigns.*,
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
           FROM tqc_camp_targets, tqc_campaigns
          WHERE cmt_cmp_code = v_cmp_code AND cmp_code = cmt_cmp_code;

      v_send_all   VARCHAR2 (1);
      v_msg_type   VARCHAR2 (10);
      v_subj       VARCHAR2 (500);
      v_body       VARCHAR2 (1000);
      i            NUMBER          := 0;
      v_status     VARCHAR2 (10);
   BEGIN
      BEGIN
         SELECT cmsg_send_all, cmsg_type, cmsg_subj, cmsg_body, cmsg_status
           INTO v_send_all, v_msg_type, v_subj, v_body, v_status
           FROM tqc_campaign_messages
          WHERE cmsg_code = v_cmsg_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_send_all := 'Y';
         WHEN OTHERS
         THEN
            raise_error ('Error Determining Message Details.');
      END;

      IF v_status = 'S'
      THEN
         raise_error ('The Message Has Already been Sent Successfully');
      END IF;

      IF v_send_all = 'Y'
      THEN
         FOR camp_targets_rec IN camp_targets
         LOOP
            i := 1;

            --CHECK IF SMS OR EMAIL
            IF v_msg_type = 'EMAIL'
            THEN
               BEGIN
                  INSERT INTO tqc_email_messages
                              (email_code,
                               email_sys_code, email_sys_module,
                               email_clnt_code, email_agn_code,
                               email_status, email_address, email_subj,
                               email_msg, email_prepared_by,
                               email_prepared_date, email_send_date
                              )
                       VALUES (tqc_email_code_seq.NEXTVAL,
                               camp_targets_rec.cmp_sys_code, 'CA',
                               camp_targets_rec.cmt_acc_code, 0,
                               'D', camp_targets_rec.related_email, v_subj,
                               v_body, v_username,
                               TRUNC (SYSDATE), TRUNC (SYSDATE)
                              );
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('Error Inserting Email Messages');
               END;
            ELSIF v_msg_type = 'SMS'
            THEN
               BEGIN
                  INSERT INTO tqc_sms_messages
                              (sms_code,
                               sms_sys_code, sms_sys_module,
                               sms_clnt_code, sms_agn_code, sms_status,
                               sms_msg, sms_prepared_by, sms_prepared_date,
                               sms_send_date
                              )
                       VALUES (tqc_sms_code_seq.NEXTVAL,
                               camp_targets_rec.cmp_sys_code, 'CA',
                               camp_targets_rec.cmt_acc_code, 0, 'D',
                               v_body, v_username, TRUNC (SYSDATE),
                               TRUNC (SYSDATE)
                              );
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('Error Updating Email Messages');
               END;
            ELSE
               NULL;
            END IF;
         END LOOP;

         IF i > 0
         THEN
            BEGIN
               UPDATE tqc_campaign_messages
                  SET cmsg_status = 'S'
                WHERE cmsg_code = v_cmsg_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error Updating Campaign Message');
            END;
         END IF;
      ELSE
         NULL;
      END IF;
   END;

   PROCEDURE savecampaignproducts (
      v_action               VARCHAR2,
      v_cpro_code       IN   NUMBER,
      v_cpro_pro_code   IN   NUMBER,
      v_cpro_sht_desc   IN   VARCHAR2,
      v_cpro_cmp_code   IN   NUMBER
   )
   IS
   BEGIN
--RAISE_ERROR('v_cpro_cmp_code'||v_cpro_cmp_code);
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_campaign_products
                        (cpro_code, cpro_pro_code,
                         cpro_sht_desc, cpro_cmp_code
                        )
                 VALUES (tqc_cpro_code_seq.NEXTVAL, v_cpro_pro_code,
                         v_cpro_sht_desc, v_cpro_cmp_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Campaign products.');
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_campaign_products
               SET cpro_pro_code = v_cpro_pro_code,
                   cpro_sht_desc = v_cpro_sht_desc,
                   cpro_cmp_code = v_cpro_cmp_code
             WHERE cpro_code = v_cpro_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating Campaign products');
         END;
      ELSIF v_action = 'D'
      THEN
         --RAISE_ERROR(v_cpro_code);
         BEGIN
            DELETE FROM tqc_campaign_products
                  WHERE cpro_code = v_cpro_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting Campaign products');
         END;
      ELSE
         raise_error ('Invalid Option Selected.');
      END IF;
   END;
END tqc_campaign_pkg; 
/