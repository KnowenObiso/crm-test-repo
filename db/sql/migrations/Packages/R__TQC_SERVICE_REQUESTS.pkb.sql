--
-- TQC_SERVICE_REQUESTS  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_service_requests
AS
/******************************************************************************
   NAME:       TQC_SERVICE_REQUESTS
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        22/Jun/2011             1. Created this package body.
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

   FUNCTION getservreqcategories (
      v_tsrc_srt_code   IN   NUMBER DEFAULT NULL,
      v_reqname         IN   VARCHAR2 DEFAULT NULL
   )
      RETURN serv_requests_cat_ref
   IS
      v_cursor   serv_requests_cat_ref;
   BEGIN
--RAISE_ERROR(v_tsrc_srt_code);
      OPEN v_cursor FOR
         SELECT tsrc_code, tsrc_name, tsrc_validity, tsrc_usr_code,
                usr_username, brn_name, brn_code, tsrc_default,
                tsrc_srt_code
           FROM tqc_serv_req_cat, tqc_users, tqc_branches
          WHERE tsrc_usr_code = usr_code(+)
            AND tsrc_brn_code = brn_code(+)
            AND UPPER (tsrc_name) = UPPER (NVL (v_reqname,tsrc_name ))
            AND NVL (tsrc_srt_code, -1) =
                                 NVL (NVL (v_tsrc_srt_code, tsrc_srt_code),
                                      -1);

      RETURN v_cursor;
   END;

   PROCEDURE categoryproc (
      v_addedit              VARCHAR2,
      v_code                 NUMBER,
      v_name                 VARCHAR2,
      v_validity             NUMBER,
      v_usrcode              NUMBER,
      v_tsrc_brn_code   IN   NUMBER DEFAULT NULL,
      v_tsrc_srt_code        VARCHAR2 DEFAULT NULL,
      v_tsrc_default    IN   VARCHAR2 DEFAULT NULL
   )
   IS
      v_catcode   NUMBER;
      v_cnt       NUMBER;
   BEGIN
--RAISE_ERROR(v_tsrc_srt_code);
      IF v_addedit = 'A'
      THEN
         BEGIN
            SELECT NVL (MAX (tsrc_code), 0)
              INTO v_catcode
              FROM tqc_serv_req_cat;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         v_cnt := 0;

         -- RAISE_ERROR('v_tsrc_srt_code'||v_tsrc_srt_code);
         SELECT COUNT (*)
           INTO v_cnt
           FROM tqc_serv_req_cat
          WHERE tsrc_srt_code = v_tsrc_srt_code AND tsrc_default = 'Y';

         IF NVL (v_cnt, 0) >= 1 AND NVL (v_tsrc_default, 'N') = 'Y'
         THEN
            raise_error
               ('Failed to Update.You cannot have the two Default Values for the same Category'
               );
         END IF;

         v_catcode := NVL (v_catcode, 0) + 1;

         INSERT INTO tqc_serv_req_cat
                     (tsrc_code, tsrc_name, tsrc_validity, tsrc_usr_code,
                      tsrc_brn_code, tsrc_srt_code, tsrc_default
                     )
              VALUES (v_catcode, v_name,v_validity, v_usrcode,
                      v_tsrc_brn_code, v_tsrc_srt_code, v_tsrc_default
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_serv_req_cat
            SET tsrc_name = v_name,
                tsrc_usr_code = v_usrcode,
                tsrc_validity = v_validity,
                tsrc_brn_code = v_tsrc_brn_code,
                tsrc_srt_code = v_tsrc_srt_code,
                tsrc_default = v_tsrc_default
          WHERE tsrc_code = v_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_serv_req_cat
               WHERE tsrc_code = v_code AND tsrc_code != 1;
      END IF;
   END categoryproc;

   PROCEDURE ordclientproppol (
      v_proppol    OUT   client_prop_pols_ref,
      v_clntcode         NUMBER
   )
   IS
   BEGIN
      OPEN v_proppol FOR
         SELECT proposal_no, policy_no, tot_sa, pol_code,
                sys_path || 'propPol.jspx', prod_desc, NULL, NULL, NULL
           FROM lms_crm_existing_policies_vw, tqc_systems
          WHERE client_code = v_clntcode AND sys_code = 27
                AND life_class = 'O';
   END ordclientproppol;

   PROCEDURE grpclientproppol (
      v_proppol    OUT   client_prop_pols_ref,
      v_clntcode         NUMBER
   )
   IS
   BEGIN
      OPEN v_proppol FOR
         SELECT proposal_no, policy_no, tot_sa, pol_code, NULL, prod_desc,
                NULL, NULL, NULL
           FROM lms_crm_existing_policies_vw
          WHERE client_code = v_clntcode AND life_class = 'G';
   END grpclientproppol;

   PROCEDURE gisclientproppol (
      v_proppol    OUT      client_prop_pols_ref,
      v_clntcode            NUMBER,
      v_type       IN       VARCHAR2
   )
   IS
   BEGIN
      IF NVL (v_type, 'C') = 'C'
      THEN
         OPEN v_proppol FOR
            SELECT DISTINCT NULL, pol_policy_no, pol_total_sum_insured,
                            pol_batch_no, sys_path || 'viewpolicy.jspx',
                            NULL, 'Policy', pol_check_date, NULL
                       FROM tqc_clients, gin_policies, tqc_systems
                      WHERE pol_prp_code = clnt_code
                        AND sys_code = 37
                        AND clnt_code = v_clntcode
            UNION
            SELECT DISTINCT NULL, TO_CHAR (quot_no), NULL, quot_code,
                            sys_path || 'viewquotation.jspx', NULL,
                            'Quotation', quot_authorised_dt, NULL
                       FROM tqc_clients, gin_quotations, tqc_systems
                      WHERE quot_prp_code = clnt_code
                        AND sys_code = 37
                        AND clnt_code = v_clntcode
            UNION
            SELECT DISTINCT NULL, cmb_claim_no, NULL, NULL,
                            sys_path || 'viewClaims.jspx', NULL, 'Claims',
                            cmb_loss_date_time, NULL
                       FROM tqc_clients,
                            gin_claim_master_bookings,
                            tqc_systems
                      WHERE cmb_prp_code = clnt_code
                        AND sys_code = 37
                        AND clnt_code = v_clntcode
                   ORDER BY 8 DESC;
      ELSE
         OPEN v_proppol FOR
            SELECT NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL
              FROM DUAL;
      END IF;
   END gisclientproppol;

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
      v_tsr_policy_no  IN            VARCHAR2 DEFAULT NULL,
      v_tsr_receive_date    IN       DATE DEFAULT NULL
   )
   IS
      v_nextval        NUMBER;
      v_ref_number     VARCHAR2 (200);
      v_srt_code       NUMBER;
      v_tsrcode_val    NUMBER;
      v_tsr_assignee   VARCHAR2 (200);
      v_user           VARCHAR2 (35)
            := pkg_global_vars.get_pvarchar2 ('PKG_GLOBAL_VARS.pvg_username');
      v_code           NUMBER;
--   v_tsr_srt_code NUMBER;
--   v_tsr_comments VARCHAR2(200);
   BEGIN
--RAISE_ERROR('v_tsrstatus'||v_tsrstatus);
      v_ref_number := generate_serv_req_ref (v_tsrtype);

      IF v_addedit = 'A'
      THEN
         BEGIN
            SELECT MAX (tsr_code)
              INTO v_nextval
              FROM tqc_serv_requests;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         v_nextval := NVL (v_nextval, 0) + 1;
         v_tsrcode := v_nextval;

         INSERT INTO tqc_serv_requests
                     (tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                      tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                      tsr_owner_code, tsr_status, tsr_due_date,
                      tsr_resolution_date, tsr_summary, tsr_description,
                      tsr_solution, tsr_tcb_code, tsr_comm_mode,
                      tsr_ref_number, tsr_comments, tsr_srt_code,
                      tsr_sec_comm_mode,
                      tsr_policy_no,tsr_receive_date
                     )
              VALUES (v_tsrcode, v_tsrccode, v_tsrtype, v_tsracctype,
                      v_tsracccode, SYSDATE, v_tsrasignee, v_tsrowntype,
                      v_tsrowner, v_tsrstatus, SYSDATE,
                      SYSDATE, v_tsrsummary, v_tsrdesc,
                      v_tsrsolution, v_tsrtcbcode, v_tsrmode,
                      v_ref_number, v_tsr_comments, v_tsr_srt_code,
                      v_tsr_sec_comm_mode,
                      v_tsr_policy_no,v_tsr_receive_date
                     );

         INSERT INTO tqc_serv_requests_hist
                     (tsr_code, tsr_tsrc_code, tsr_type,
                      tsr_acc_type, tsr_acc_code, tsr_date, tsr_assignee,
                      tsr_owner_type, tsr_owner_code, tsr_status,
                      tsr_due_date, tsr_resolution_date, tsr_summary,
                      tsr_description, tsr_solution, tsr_tcb_code,
                      tsr_comm_mode, tsr_ref_number, tsr_comments,
                      tsr_srt_code
                     )
              VALUES (tqc_tsr_code_seq.NEXTVAL, v_tsrccode, v_tsrtype,
                      v_tsracctype, v_tsracccode, SYSDATE, v_tsrasignee,
                      v_tsrowntype, v_tsrowner, v_tsrstatus,
                      SYSDATE, SYSDATE, v_tsrsummary,
                      v_tsrdesc, v_tsrsolution, v_tsrtcbcode,
                      v_tsrmode, v_ref_number, v_tsr_comments,
                      v_tsr_srt_code
                     );
                     
                     --Send email
       process_serv_req_email_alert(    v_tsrasignee,
                                        v_ref_number,
                                        v_tsrdesc,v_tsracccode);
      ELSIF v_addedit = 'E'
      THEN
         BEGIN
            SELECT tsr_srt_code, tsr_assignee
              INTO v_srt_code, v_tsr_assignee
              FROM tqc_serv_requests
             WHERE tsr_code = v_tsrcode;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Failed to obtain the request category');
         END;

         IF v_srt_code != v_tsr_srt_code
         THEN
            BEGIN
               SELECT MAX (tsr_code)
                 INTO v_nextval
                 FROM tqc_serv_requests;
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
            END;

            IF NVL (v_tsracctype, 'C') = 'IN'
            THEN
               v_code := 2;
            ELSE
               v_code := v_tsrccode;
            END IF;

            v_nextval := NVL (v_nextval, 0) + 1;
            v_tsrcode_val := v_nextval;

--RAISE_ERROR('TEST'||v_tsrcode);
            UPDATE tqc_serv_requests
               SET                               --tsr_tsrc_code = v_tsrccode,
                  tsr_type = v_tsrtype,
                  tsr_acc_type = v_tsracctype,
                  tsr_acc_code = v_tsracccode,
                  tsr_date = v_tsrdate,
                  tsr_assignee = v_tsrasignee,
                  tsr_owner_type = v_tsrowntype,
                  tsr_owner_code = v_tsrowner,
                  tsr_status = 'Closed',
                  tsr_due_date = v_duedate,
                  tsr_resolution_date = v_resoldate,
                  tsr_summary = v_tsrsummary,
                  tsr_description = v_tsrdesc,
                  tsr_solution = v_tsrsolution,
                  tsr_comm_mode = v_tsrmode,
                  tsr_ref_number = v_ref_number,
                  tsr_comments = v_tsr_comments,
                  tsr_policy_no=v_tsr_policy_no,
                  tsr_receive_date=v_tsr_receive_date
             --   tsr_srt_code=v_tsr_srt_code
            WHERE  tsr_code = v_tsrcode;

            --  RAISE_ERROR('v_tsracctype'||v_tsracctype);
            INSERT INTO tqc_serv_requests
                        (tsr_code,
                         tsr_tsrc_code,
                         tsr_type, tsr_acc_type, tsr_acc_code, tsr_date,
                         tsr_assignee, tsr_owner_type, tsr_owner_code,
                         tsr_status, tsr_due_date, tsr_resolution_date,
                         tsr_summary, tsr_description, tsr_solution,
                         tsr_tcb_code, tsr_comm_mode, tsr_ref_number,
                         tsr_comments, tsr_srt_code,
                         tsr_policy_no,tsr_receive_date
                        )
                 VALUES (v_tsrcode_val,
                         DECODE (v_tsracctype, 'IN', 2, v_tsrccode),
                         'CONVERSION', v_tsracctype, v_tsracccode, SYSDATE,
                         v_tsrasignee, v_tsrowntype, v_tsrowner,
                         'Open', SYSDATE, SYSDATE,
                         v_tsrsummary, v_tsrdesc, v_tsrsolution,
                         v_tsrtcbcode, v_tsrmode, v_ref_number,
                         v_tsr_comments, v_tsr_srt_code,
                         v_tsr_policy_no,v_tsr_receive_date
                        );
         ELSE
            --  RAISE_ERROR('v_tsrstatus'||v_tsrstatus||'v_tsrcode'||v_tsrcode);
            UPDATE tqc_serv_requests
               SET tsr_tsrc_code = v_tsrccode,
                   tsr_type = v_tsrtype,
                   tsr_acc_type = v_tsracctype,
                   tsr_acc_code = v_tsracccode,
                   tsr_date = v_tsrdate,
                   tsr_assignee = v_tsrasignee,
                   tsr_owner_type = v_tsrowntype,
                   tsr_owner_code = v_tsrowner,
                   tsr_status = v_tsrstatus,
                   tsr_due_date = v_duedate,
                   tsr_resolution_date = v_resoldate,
                   tsr_summary = v_tsrsummary,
                   tsr_description = v_tsrdesc,
                   tsr_solution = v_tsrsolution,
                   tsr_comm_mode = v_tsrmode,
                   tsr_ref_number = v_ref_number,
                   tsr_comments = v_tsr_comments,
                   tsr_srt_code = v_tsr_srt_code,
                   tsr_policy_no=v_tsr_policy_no,
                   tsr_receive_date = v_tsr_receive_date
             WHERE tsr_code = v_tsrcode;
         END IF;

         INSERT INTO tqc_serv_requests_hist
                     (tsr_code, tsr_tsrc_code, tsr_type,
                      tsr_acc_type, tsr_acc_code, tsr_date, tsr_assignee,
                      tsr_owner_type, tsr_owner_code, tsr_status,
                      tsr_due_date, tsr_resolution_date, tsr_summary,
                      tsr_description, tsr_solution, tsr_tcb_code,
                      tsr_comm_mode, tsr_ref_number, tsr_comments,
                      tsr_srt_code
                     )
              VALUES (tqc_tsr_code_seq.NEXTVAL, v_tsrccode, v_tsrtype,
                      v_tsracctype, v_tsracccode, SYSDATE, v_tsrasignee,
                      v_tsrowntype, v_tsrowner, v_tsrstatus,
                      SYSDATE, SYSDATE, v_tsrsummary,
                      v_tsrdesc, v_tsrsolution, v_tsrtcbcode,
                      v_tsrmode, v_ref_number, v_tsr_comments,
                      v_tsr_srt_code
                     );
--     IF v_tsr_assignee=v_tsrasignee THEN
--         gin_manage_alerts.service_req_alert (v_user,v_tsrcode);
--     END IF;
      END IF;
      
      
EXCEPTION
   WHEN OTHERS
   THEN
   raise_error ('Error Occured ' || SQLERRM);
   END;


   FUNCTION getclientopenrequests (
      v_clientcode          NUMBER,
      v_tsr_acc_type   IN   VARCHAR2
   )
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
--RAISE_ERROR('v_clientCode'||v_clientCode||'v_tsr_acc_type'||v_tsr_acc_type);
      OPEN v_cursor FOR
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                tsr_owner_code, tsr_status, tsr_due_date,
                tsr_resolution_date, tsr_summary, tsr_description,
                tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, clnt_sht_desc, clnt_physical_addrs,
                clnt_email_addrs, clnt_sms_tel, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'C'
            AND tsr_acc_code = v_clientcode
            AND tsr_status IN ( 'Open', 'Pending')
            AND tsr_type != 'Enquiry'
            AND srt_code(+) = tsr_srt_code
            AND tsr_acc_type = v_tsr_acc_type
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, agn_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, agn_sht_desc, agn_physical_address,
                agn_email_address, agn_sms_tel, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_agencies,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = agn_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'A'
            AND tsr_acc_code = v_clientcode
             AND tsr_status IN ( 'Open', 'Pending')
            AND tsr_type != 'Enquiry'
            AND srt_code(+) = tsr_srt_code
            AND tsr_acc_type = v_tsr_acc_type
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, spr_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, spr_sht_desc, spr_physical_address, spr_email,
                spr_sms_number, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_service_providers,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = spr_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'SP'
            AND tsr_acc_code = v_clientcode
             AND tsr_status IN ( 'Open', 'Pending')
            AND tsr_type != 'Enquiry'
            AND tsr_acc_type = v_tsr_acc_type
            AND srt_code(+) = tsr_srt_code
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, srid_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, srid_name, srid_physical_address,
                srid_email_address, srid_telephone, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_serv_req_ind_dtls,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = srid_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'O'
            AND tsr_acc_code = v_clientcode
             AND tsr_status IN ( 'Open', 'Pending')
            AND tsr_type != 'Enquiry'
            AND tsr_acc_type = v_tsr_acc_type
            AND srt_code(+) = tsr_srt_code
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, dlt_desc account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, org_sht_desc, org_phy_addrs, org_email_addrs,
                org_tel1, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_org_division_levels_type,
                tqc_users uown,
                tqc_serv_req_types,
                tqc_organizations
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = dlt_code_val
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'IN'
            AND tsr_acc_code = v_clientcode
             AND tsr_status IN ( 'Open', 'Pending')
            AND org_code = tsr_tsrc_code
            AND tsr_type != 'Enquiry'
            AND tsr_acc_type = v_tsr_acc_type
            AND srt_code(+) = tsr_srt_code;

      RETURN v_cursor;
   END;

   FUNCTION getclientallopenrequests (v_clientcode NUMBER)
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                tsr_owner_code, tsr_status, tsr_due_date,
                tsr_resolution_date, tsr_summary, tsr_description,
                tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, clnt_sht_desc, clnt_physical_addrs,
                clnt_email_addrs, clnt_sms_tel, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code(+)
            AND tsr_owner_code = uown.usr_code(+)
            AND tsr_acc_type = 'C'
            AND srt_code(+) = tsr_srt_code
            AND NVL (tsr_acc_code, v_clientcode) = v_clientcode
            AND tsr_status = 'Open';

      RETURN v_cursor;
   END;

   FUNCTION getallclientrequests (
      v_clientcode          NUMBER,
      v_tsr_acc_type   IN   VARCHAR2
   )
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
--RAISE_ERROR('v_clientCode'||v_clientCode);
      OPEN v_cursor FOR
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                tsr_owner_code, tsr_status, tsr_due_date,
                tsr_resolution_date, tsr_summary, tsr_description,
                tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, clnt_sht_desc, clnt_physical_addrs,
                clnt_email_addrs, clnt_sms_tel, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'C'
            AND tsr_status = 'Closed'
            AND srt_code(+) = tsr_srt_code
            AND tsr_acc_code = v_clientcode
            AND tsr_acc_type = v_tsr_acc_type
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, agn_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, agn_sht_desc, agn_physical_address,
                agn_email_address, agn_sms_tel, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_agencies,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = agn_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'A'
            AND tsr_status = 'Closed'
            AND srt_code(+) = tsr_srt_code
            AND tsr_acc_code = v_clientcode
            AND tsr_acc_type = v_tsr_acc_type
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, spr_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, spr_sht_desc, spr_physical_address, spr_email,
                spr_mobile_no, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_service_providers,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = spr_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'SP'
            AND tsr_status = 'Closed'
            AND srt_code(+) = tsr_srt_code
            AND tsr_acc_code = v_clientcode
            AND tsr_acc_type = v_tsr_acc_type
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, srid_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, NULL spr_sht_desc, srid_physical_address,
                srid_email_address, srid_telephone, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_serv_req_ind_dtls,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = srid_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'O'
            AND tsr_status = 'Closed'
            AND srt_code(+) = tsr_srt_code
            AND tsr_acc_code = v_clientcode
            AND tsr_acc_type = v_tsr_acc_type
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, dlt_desc account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, org_sht_desc, org_phy_addrs, org_email_addrs,
                org_tel1, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_org_division_levels_type,
                tqc_users uown,
                tqc_serv_req_types,
                tqc_organizations
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = dlt_code_val
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'IN'
            AND tsr_acc_code = v_clientcode
            AND tsr_status = 'Closed'
            AND org_code = tsr_tsrc_code
            AND tsr_type != 'Enquiry'
            AND tsr_acc_type = v_tsr_acc_type
            AND srt_code(+) = tsr_srt_code;

      RETURN v_cursor;
   END;

   FUNCTION getuseropenrequests (v_usercode NUMBER)
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                tsr_owner_code, tsr_status, tsr_due_date,
                tsr_resolution_date, tsr_summary, tsr_description,
                tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, clnt_sht_desc, clnt_physical_addrs,
                clnt_email_addrs, clnt_sms_tel, NULL, NULL
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code
            AND tsr_owner_code = uown.usr_code
            AND ass.usr_code = v_usercode
            AND tsr_status = 'Open'
            AND tsr_type != 'Enquiry';

      RETURN v_cursor;
   END;

   FUNCTION getuseroverduerequests (v_usercode NUMBER)
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                tsr_owner_code, tsr_status, tsr_due_date,
                tsr_resolution_date, tsr_summary, tsr_description,
                tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, clnt_sht_desc, clnt_physical_addrs,
                clnt_email_addrs, clnt_sms_tel, NULL, NULL
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code
            AND tsr_owner_code = uown.usr_code
            AND ass.usr_code = v_usercode
            AND tsr_status = 'Open'
            AND SYSDATE > tsr_due_date
            AND tsr_type != 'Enquiry';

      RETURN v_cursor;
   END;

   FUNCTION getoverduerequests (v_tsr_status IN VARCHAR2)
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                tsr_owner_code, tsr_status, tsr_due_date,
                tsr_resolution_date, tsr_summary, tsr_description,
                tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, clnt_sht_desc, clnt_physical_addrs,
                clnt_email_addrs, clnt_sms_tel, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_status = v_tsr_status
            AND srt_code(+) = tsr_srt_code
            AND SYSDATE > tsr_due_date
            AND tsr_type != 'Enquiry'
            AND tsr_acc_type = 'C'
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, agn_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, agn_sht_desc, agn_physical_address,
                agn_email_address, agn_sms_tel, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_agencies,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = agn_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_status = v_tsr_status
            AND SYSDATE > tsr_due_date
            AND srt_code(+) = tsr_srt_code
            AND tsr_type != 'Enquiry'
            AND tsr_acc_type = 'A'
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, spr_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, spr_sht_desc, spr_physical_address, spr_email,
                spr_mobile_no, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_service_providers,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = spr_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_status = v_tsr_status
            AND SYSDATE > tsr_due_date
            AND srt_code(+) = tsr_srt_code
            AND tsr_acc_type = 'SP'
            AND tsr_type != 'Enquiry'
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, srid_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, srid_name, srid_physical_address,
                srid_email_address, srid_telephone, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_serv_req_ind_dtls,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = srid_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'O'
            AND SYSDATE > tsr_due_date
            --AND TSR_ACC_CODE = v_clientCode
            AND tsr_status = 'Open'
            AND tsr_type != 'Enquiry'
            -- AND tsr_acc_type=v_tsr_acc_type
            AND srt_code(+) = tsr_srt_code
         UNION
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type, tsr_acc_code,
                tsr_date, tsr_assignee, tsr_owner_type, tsr_owner_code,
                tsr_status, tsr_due_date, tsr_resolution_date, tsr_summary,
                tsr_description, tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee, dlt_desc account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, org_sht_desc, org_phy_addrs, org_email_addrs,
                org_tel1, tsr_srt_code, srt_desc
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_org_division_levels_type,
                tqc_users uown,
                tqc_serv_req_types,
                tqc_organizations
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = dlt_code_val
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'IN'
            AND SYSDATE > tsr_due_date
            -- AND TSR_ACC_CODE = v_clientCode
            AND tsr_status = 'Open'
            AND org_code = tsr_tsrc_code
            AND tsr_type != 'Enquiry'
            -- AND tsr_acc_type=v_tsr_acc_type
            AND srt_code(+) = tsr_srt_code;

      RETURN v_cursor;
   END;

   FUNCTION getallpendingrequests
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
--RAISE_ERROR('TEST');
      OPEN v_cursor FOR
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                tsr_owner_code, tsr_status, tsr_due_date,
                tsr_resolution_date, tsr_summary, tsr_description,
                tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'IN', 'INTERNAL',
                        'U', 'USER',
                        'O', 'OTHERS'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'IN', 'INTERNAL',
                        'U', 'USER',
                        'OTHERS'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, clnt_sht_desc, clnt_physical_addrs,
                clnt_email_addrs, clnt_sms_tel, NULL, NULL
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_status = 'Open'
            AND tsr_type != 'Enquiry';

      RETURN v_cursor;
   END;

   FUNCTION getoverduerequestschat
      RETURN overduecount_ref
   IS
      v_cursor   overduecount_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   COUNT (*) user_issues, ass.usr_username assignee
             FROM tqc_serv_requests,
                  tqc_serv_req_cat,
                  tqc_users ass,
                  tqc_clients,
                  tqc_users uown
            WHERE tsr_tsrc_code = tsrc_code
              AND tsr_assignee = ass.usr_code
              AND tsr_acc_code = clnt_code
              AND tsr_owner_code = uown.usr_code
              AND tsr_status = 'Open'
              AND SYSDATE > tsr_due_date
              AND tsr_type != 'Enquiry'
         GROUP BY ass.usr_username;

      RETURN v_cursor;
   END;

   FUNCTION getroottopics
      RETURN kb_topics_ref
   IS
      v_cursor   kb_topics_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tkbt_id, tkbt_order, tkbt_sht_desc, tkbt_desc,
                tkbt_parent_id
           FROM tqc_kb_topics
          WHERE tkbt_parent_id IS NULL;

      RETURN v_cursor;
   END;

   FUNCTION getchildtopics (v_tkbt_id NUMBER)
      RETURN kb_topics_ref
   IS
      v_cursor   kb_topics_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tkbt_id, tkbt_order, tkbt_sht_desc, tkbt_desc,
                tkbt_parent_id
           FROM tqc_kb_topics
          WHERE tkbt_parent_id = v_tkbt_id;

      RETURN v_cursor;
   END;

   PROCEDURE kbtopicproc (
      v_addedit       VARCHAR2,
      v_tkbtid        NUMBER,
      v_tkbtorder     NUMBER,
      v_tkbtshtdesc   VARCHAR2,
      v_tkbtdesc      VARCHAR2,
      v_tkbtparent    NUMBER
   )
   IS
      v_code   NUMBER;
   BEGIN
      IF v_addedit = 'A'
      THEN
         BEGIN
            SELECT MAX (tkbt_id)
              INTO v_code
              FROM tqc_kb_topics;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         v_code := NVL (v_code, 0) + 1;

         INSERT INTO tqc_kb_topics
                     (tkbt_id, tkbt_order, tkbt_sht_desc, tkbt_desc,
                      tkbt_parent_id
                     )
              VALUES (v_code, v_tkbtorder, v_tkbtshtdesc, v_tkbtdesc,
                      v_tkbtparent
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_kb_topics
            SET tkbt_order = v_tkbtorder,
                tkbt_sht_desc = v_tkbtshtdesc,
                tkbt_desc = v_tkbtdesc,
                tkbt_parent_id = v_tkbtparent
          WHERE tkbt_id = v_tkbtid;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_kb_topics
               WHERE tkbt_id = v_tkbtid;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM);
   END kbtopicproc;

   FUNCTION getkbcontent (v_tkbt_id NUMBER)
      RETURN kb_content_ref
   IS
      v_cursor   kb_content_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tkbc_id, tkbc_tkbt_id, tkbc_order, tkbc_content
           FROM tqc_kb_content
          WHERE tkbc_tkbt_id = v_tkbt_id;

      RETURN v_cursor;
   END;

   PROCEDURE kbcontentproc (
      v_addedit     VARCHAR2,
      v_tkbcid      NUMBER,
      v_tkbtid      NUMBER,
      v_tkbcorder   NUMBER,
      v_tkbccont    VARCHAR2
   )
   IS
      v_code   NUMBER;
   BEGIN
      IF v_addedit = 'A'
      THEN
         BEGIN
            SELECT MAX (tkbc_id)
              INTO v_code
              FROM tqc_kb_content;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         v_code := NVL (v_code, 0) + 1;

         INSERT INTO tqc_kb_content
                     (tkbc_id, tkbc_tkbt_id, tkbc_order, tkbc_content
                     )
              VALUES (v_code, v_tkbtid, v_tkbcorder, v_tkbccont
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_kb_content
            SET tkbc_tkbt_id = v_tkbtid,
                tkbc_order = v_tkbcorder,
                tkbc_content = v_tkbccont
          WHERE tkbc_id = v_tkbcid;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_kb_content
               WHERE tkbc_id = v_tkbcid;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM);
   END kbcontentproc;

   FUNCTION getcategorybranches
      RETURN cat_branches_ref
   IS
      v_cursor   cat_branches_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT brn_code, brn_sht_desc, brn_reg_code, brn_name
           FROM tqc_branches, tqc_regions
          WHERE brn_reg_code = reg_code AND reg_org_code = 1;

      RETURN v_cursor;
   END;

   FUNCTION generate_serv_req_ref (v_tsrtype IN VARCHAR)
      RETURN VARCHAR2
   IS
      v_ref_number   VARCHAR2 (200);
   BEGIN
      v_ref_number :=
                   'SRQ/' || tqc_serv_req_ref_seq.NEXTVAL || '/' || v_tsrtype;
      RETURN v_ref_number;
   END;

   FUNCTION get_departments
      RETURN departments_ref
   IS
      v_cursor   departments_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT dep_code, dep_sht_desc, dep_name, dep_wef, dep_wet
           FROM tqc_departments;

      RETURN v_cursor;
   END;

   FUNCTION get_serv_request_types
      RETURN serv_request_types_ref
   IS
      v_cursor   serv_request_types_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT srt_code, srt_sht_desc, srt_desc
           FROM tqc_serv_req_types;

      RETURN v_cursor;
   END;

   PROCEDURE update_serv_request_types (
      v_addedit                      VARCHAR2,
      v_srt_code                IN   NUMBER,
      v_srt_desc                IN   VARCHAR2,
      v_srt_sht_desc            IN   VARCHAR2,
      v_srt_reason_for_change   IN   VARCHAR2
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_serv_req_types
                     (srt_code, srt_sht_desc, srt_desc
                     )
              VALUES (tqc_srt_code_seq.NEXTVAL, v_srt_sht_desc, v_srt_desc
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_serv_req_types
            SET srt_sht_desc = v_srt_sht_desc,
                srt_desc = v_srt_desc
          WHERE srt_code = v_srt_code;

         INSERT INTO tqc_serv_req_types_hist
                     (srt_code, srt_sht_desc, srt_desc,
                      srt_reason_for_change
                     )
              VALUES (tqc_srt_code_seq.NEXTVAL, v_srt_sht_desc, v_srt_desc,
                      v_srt_reason_for_change
                     );
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_serv_req_types
               WHERE srt_code = v_srt_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM);
   END update_serv_request_types;

   FUNCTION get_serv_providers
      RETURN serv_providers_ref
   IS
      v_cursor   serv_providers_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT spr_code, spr_sht_desc, spr_name, spr_physical_address,
                spr_postal_address, spr_phone, spr_email, spr_mobile_no,
                spr_sms_number
           FROM tqc_service_providers;

      RETURN v_cursor;
   END;

   FUNCTION getallpendingrequests (v_tsr_assignee IN VARCHAR2)
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
--   RAISE_ERROR('v_tsr_assignee' || v_tsr_assignee);
      OPEN v_cursor FOR
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                tsr_owner_code, tsr_status, tsr_due_date,
                tsr_resolution_date, tsr_summary, tsr_description,
                tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'IN', 'INTERNAL',
                        'U', 'USER',
                        'O', 'OTHERS'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, tsr_comm_mode, tsr_ref_number,
                tsr_comments, clnt_sht_desc, clnt_physical_addrs,
                clnt_email_addrs, clnt_sms_tel, NULL, NULL
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code(+)
            AND tsr_owner_code = uown.usr_code
            AND tsr_status = 'Open'
            AND tsr_type != 'Enquiry'
            AND ass.usr_username = v_tsr_assignee;

      RETURN v_cursor;
   END;

   PROCEDURE update_serv_req_other_dtls (
      v_addedit                          VARCHAR2,
      v_srid_code               IN OUT   NUMBER,
      v_srid_name               IN       VARCHAR2,
      v_srid_telephone          IN       VARCHAR2,
      v_srid_email_address      IN       VARCHAR2,
      v_srid_physical_address   IN       VARCHAR2,
      v_srid_id_number          IN       VARCHAR2
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         v_srid_code := tqc_srid_code_seq.NEXTVAL;

         INSERT INTO tqc_serv_req_ind_dtls
                     (srid_code, srid_name, srid_telephone,
                      srid_email_address, srid_physical_address,
                      srid_id_number
                     )
              VALUES (v_srid_code, v_srid_name, v_srid_telephone,
                      v_srid_email_address, v_srid_physical_address,
                      v_srid_id_number
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_serv_req_ind_dtls
            SET srid_name = v_srid_name,
                srid_telephone = v_srid_telephone,
                srid_email_address = v_srid_email_address,
                srid_physical_address = v_srid_physical_address,
                srid_id_number = v_srid_id_number
          WHERE srid_code = v_srid_code;
      ELSIF v_addedit = 'D'
      THEN
         NULL;

         DELETE FROM tqc_serv_req_ind_dtls
               WHERE srid_code = v_srid_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM);
   END update_serv_req_other_dtls;

   FUNCTION get_other_service_requesters (
      v_srid_name        IN   VARCHAR2,
      v_srid_id_number   IN   VARCHAR2
   )
      RETURN service_requesters_ref
   IS
      v_cursor   service_requesters_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT srid_code, srid_name, srid_telephone, srid_email_address,
                srid_physical_address, srid_id_number
           FROM tqc_serv_req_ind_dtls
          WHERE UPPER (srid_name) LIKE '%' || UPPER (v_srid_name) || '%'
            AND UPPER (srid_id_number) LIKE
                                         '%' || UPPER (v_srid_id_number)
                                         || '%';

      RETURN v_cursor;
   END;

   FUNCTION get_agent_commission (v_agn_code IN NUMBER)
      RETURN agent_commission_ref
   IS
      v_cursor   agent_commission_ref;
   BEGIN
--RAISE_ERROR('v_agn_code'||v_agn_code);
      OPEN v_cursor FOR
         SELECT cop_agn_code, agn_name, cop_date, cop_cr_ref_no,
                cop_dr_ref_no, cop_comm_amt, cop_whdtax_amt, cop_net_comm,
                cop_cur_code, cop_authorized, cop_authorized_by, cop_paid,
                cop_paid_chq_date, cop_paid_chq_no
           FROM gin_commission_pymts, tqc_agencies
          WHERE cop_agn_code = agn_code AND cop_agn_code = v_agn_code;

      RETURN v_cursor;
   END;

   FUNCTION getproviderfees (v_sprcode NUMBER)
      RETURN provider_fee_ref
   IS
      v_cursor   provider_fee_ref;
   BEGIN
--RAISE_ERROR('v_agn_code'||v_agn_code);
      OPEN v_cursor FOR
         SELECT DISTINCT clnt_name || ' ' || clnt_other_names, mfee_desc,
                         pofe_pol_policy_no, pofe_amount
                    FROM gin_pol_misc_fees,
                         tqc_service_providers,
                         gin_misc_fee_types,
                         gin_policies,
                         tqc_clients
                   WHERE pofe_cor_code = spr_code
                     AND pofe_mfee_code = mfee_code
                     AND pofe_pol_batch_no = pol_batch_no
                     AND clnt_code = pol_prp_code
                     AND spr_code = v_sprcode
         UNION
         SELECT DISTINCT prp_surname || ' ' || prp_other_names, mtl_desc,
                         pol_policy_no, payable_amt
                    FROM tqc_service_providers,
                         lms_med_facilitators_vw,
                         lms_client_medicals,
                         lms_med_tests_and_limits,
                         lms_policies,
                         lms_proposers
                   WHERE spr_code = tq_spr_code
                     AND cml_cmi_code = cmi_code
                     AND cml_mtl_code = mtl_code
                     AND cml_pol_code = pol_code
                     AND pol_prp_code = prp_code
                     AND spr_code = v_sprcode
                ORDER BY 1;

      RETURN v_cursor;
   END;
   
PROCEDURE setrequestAssignee(
   v_tsr_code              tqc_serv_requests.tsr_code%TYPE,
   v_tsr_assignee          tqc_serv_requests.tsr_assignee%TYPE) IS
   BEGIN
   UPDATE tqc_serv_requests
   SET tsr_assignee = v_tsr_assignee
   WHERE tsr_code = v_tsr_code;
   END;   
 
 FUNCTION getServReqClientComments (
      v_tsr_code   IN   NUMBER DEFAULT NULL
   )
      RETURN serv_req_client_comm_ref
   IS
      v_cursor   serv_req_client_comm_ref;
   BEGIN
   --raise_error('v_tsr_code '||v_tsr_code);
      OPEN v_cursor FOR
        select SRC_CODE,SRC_CLIENT_COMMENT,SRC_SOLUTION
        from TQC_SERV_REQ_COMMENTS
        where SRC_TSR_CODE =v_tsr_code;

      RETURN v_cursor;
   END;
   
  procedure servReqClntCommentProc(
       v_addedit                          VARCHAR2,
      v_src_code               IN OUT   NUMBER,
      v_src_client_comment               IN       VARCHAR2,
      v_src_solution          IN       VARCHAR2,
      v_src_tsr_code         IN       NUMBER
  ) IS
  
  BEGIN
         IF v_addedit = 'A'
      THEN
         v_src_code := tqc_src_code_seq.NEXTVAL;

         INSERT INTO TQC_SERV_REQ_COMMENTS
                     (SRC_CODE,SRC_CLIENT_COMMENT,SRC_SOLUTION,SRC_TSR_CODE
                     )
              VALUES (v_src_code,
                            v_src_client_comment,
                            v_src_solution,
                            v_src_tsr_code                           
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE TQC_SERV_REQ_COMMENTS
            SET SRC_CLIENT_COMMENT=v_src_client_comment,
            SRC_SOLUTION=v_src_solution
          WHERE SRC_CODE = v_src_code;
      ELSIF v_addedit = 'D'
      THEN
         NULL;

         DELETE FROM TQC_SERV_REQ_COMMENTS
               WHERE SRC_CODE = v_src_code;
      END IF;
  
  EXCEPTION
        WHEN OTHERS THEN
                RAISE_ERROR('Client comment proc .......'||SQLERRM(SQLCODE));
  END;
  
  /**
  This function returns the service requests types ascalation assignees
  **/
  FUNCTION getservreqscalations
      RETURN serv_req_escalations_ref
   IS
      v_cursor   serv_req_escalations_ref;
   BEGIN
      OPEN v_cursor FOR        
        SELECT sre_code, sre_srt_code,sre_lvl_duration,
        sre_lvl_one_assignee,sre_lvl_two_duration,sre_lvl_two_assignee,
        (select SRT_DESC from tqc_serv_req_types where srt_code=sre_srt_code) sre_srt_code_name,
        (select  USR_NAME  from tqc_users where USR_CODE=sre_lvl_one_assignee )  sre_lvl_one_assignee_name ,
        (select  USR_NAME  from tqc_users where USR_CODE= sre_lvl_two_assignee ) sre_lvl_two_assignee_name
        FROM TQC_SERV_REQ_ECALATIONS;

      RETURN v_cursor;
   END;
   
   /**
  This procedure  inserts updates and deletes  service requests types ascalation assignees
  **/
  procedure servReqClntEscalationsProc(
       v_addedit                           IN VARCHAR2,
      v_sre_code                           IN OUT NUMBER ,
       v_sre_srt_code                    IN NUMBER ,
        v_sre_lvl_duration               IN NUMBER,
       v_sre_lvl_one_assignee        IN NUMBER,
       v_sre_lvl_two_duration        IN NUMBER,
       v_sre_lvl_two_assignee       IN NUMBER
  ) IS
  v_cnt NUMBER;
  BEGIN
         IF v_addedit = 'A'
      THEN
         v_sre_code := tqc_src_code_seq.NEXTVAL;
         SELECT COUNT (*)
           INTO v_cnt
           FROM tqc_serv_req_ecalations
          WHERE sre_srt_code = v_sre_srt_code;
          
          IF NVL (v_cnt, 0) > 0
           THEN
                 v_sre_code:= -1;
          ELSE
          
             INSERT INTO tqc_serv_req_ecalations
                         (sre_code,sre_srt_code,sre_lvl_duration,
                           sre_lvl_one_assignee,sre_lvl_two_duration,sre_lvl_two_assignee
                         )
                  VALUES (v_sre_code,
                                v_sre_srt_code,
                                v_sre_lvl_duration,
                                v_sre_lvl_one_assignee,
                                v_sre_lvl_two_duration,
                                v_sre_lvl_two_assignee                          
                         );
          END IF;
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_serv_req_ecalations
            SET sre_srt_code=v_sre_srt_code,
            sre_lvl_duration=v_sre_lvl_duration,
            sre_lvl_one_assignee=v_sre_lvl_one_assignee,
            sre_lvl_two_duration=v_sre_lvl_two_duration,
            sre_lvl_two_assignee=v_sre_lvl_two_assignee
          where sre_code = v_sre_code;
      ELSIF v_addedit = 'D'
      THEN
         NULL;

         DELETE FROM tqc_serv_req_ecalations
               WHERE sre_code = v_sre_code;
      END IF;
  
  EXCEPTION
        WHEN OTHERS THEN
                RAISE_ERROR('Client escalation proc .......'||SQLERRM(SQLCODE));
  END;
  
  PROCEDURE process_serv_requests_alert
   IS
      v_msgt_msg            VARCHAR2 (400);
      v_final_msgt_msg      VARCHAR2 (200);
      v_alrt_grp_usr_code   NUMBER;
      v_email           VARCHAR2 (200);
      sender                VARCHAR2 (200);
      v_out                 NUMBER;
      v_alrt_code           NUMBER;
      v_param_val           NUMBER;
      v_count               NUMBER;
      v_user_name       VARCHAR2 (200);
       v_name               VARCHAR2 (200);

      CURSOR REQUESTS_LVL_TWO 
      IS
          SELECT  TSR_CODE,SRE_LVL_TWO_ASSIGNEE,TSR_REF_NUMBER,TSR_ASSIGNEE
             FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_ECALATIONS
          WHERE  getworkingdays(NVL(TSR_REOPENNED_DATE,TSR_DATE),SYSDATE)>    ( NVL(SRE_LVL_DURATION,0)+NVL(SRE_LVL_TWO_DURATION,0) )
          and  SRE_SRT_CODE=TSR_SRT_CODE
          AND TSR_STATUS='Open' ;
          
        CURSOR REQUESTS_LVL_ONE 
      IS
           SELECT  TSR_CODE,SRE_LVL_ONE_ASSIGNEE ,TSR_REF_NUMBER ,TSR_ASSIGNEE
            FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_ECALATIONS
          WHERE getworkingdays(NVL(TSR_REOPENNED_DATE,TSR_DATE),SYSDATE)>    ( NVL(SRE_LVL_DURATION,0) )
          and  SRE_SRT_CODE=TSR_SRT_CODE
          AND TSR_STATUS='Open'
          AND TSR_CODE NOT IN (
                      SELECT  TSR_CODE   FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_ECALATIONS
                      WHERE getworkingdays(NVL(TSR_REOPENNED_DATE,TSR_DATE),SYSDATE) >    ( NVL(SRE_LVL_DURATION,0)+NVL(SRE_LVL_TWO_DURATION,0) )
                      and  SRE_SRT_CODE=TSR_SRT_CODE
                      AND TSR_STATUS='Open'
          );
   BEGIN
     
 BEGIN
         SELECT msgt_msg
           INTO v_msgt_msg
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'SERV_REQ_ESC';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('SERV_REQ_ESC Message Template not Defined...');
      END;
       
  BEGIN
         SELECT alrt_grp_usr_code, alrt_code
           INTO v_alrt_grp_usr_code, v_alrt_code
           FROM tqc_alert_types
          WHERE alrt_sys_code = 0
            AND alrt_email = 'Y'
            AND alrt_type = 'SERV_REQ_ESC';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('DUE_INVOICES ALERT TYPE NOT ATTACHED TO A USER...');
      END;
     
     




       FOR i in REQUESTS_LVL_TWO loop
       
       SELECT USR_USERNAME,USR_NAME
       INTO v_user_name, v_name
       FROM tqc_users
       where USR_CODE=  i.SRE_LVL_TWO_ASSIGNEE;
       
              BEGIN
       
       SELECT USR_EMAIL
       INTO v_email
       FROM tqc_users
       where USR_CODE=  i.SRE_LVL_TWO_ASSIGNEE;
       EXCEPTION
            WHEN NO_DATA_FOUND THEN
       raise_error ( 'User  '||v_user_name||' DOES NOT HAVE AN EMAIL ADDRESS...');
       END ;
       
        SELECT REPLACE(v_msgt_msg, '[ASSIGNEE]', v_name)
        INTO v_msgt_msg
        FROM DUAL;
        
        SELECT REPLACE(v_msgt_msg, '[TICKET]', I.TSR_REF_NUMBER)
        INTO v_msgt_msg
        FROM DUAL;
  IF I.TSR_ASSIGNEE <>  i.SRE_LVL_TWO_ASSIGNEE THEN
        UPDATE TQC_SERV_REQUESTS
        SET TSR_ASSIGNEE= i.SRE_LVL_TWO_ASSIGNEE
        WHERE TSR_CODE=I.TSR_CODE;
       
         BEGIN
            INSERT INTO tqc_email_messages
                        (email_code, email_sys_code, email_sys_module,
                         email_clnt_code, email_agn_code, email_pol_code,
                         email_pol_no, email_clm_no, email_quot_code,
                         email_address, email_subj, email_msg, email_status,
                         email_prepared_by, email_prepared_date,
                         email_send_date, email_to_send_date
                        )
                 VALUES (tqc_sms_code_seq.NEXTVAL, 1, 'REC',
                         v_alrt_code, v_alrt_code, v_alrt_code,
                         v_alrt_code, v_alrt_code, v_alrt_code,
                         v_email, NULL, v_msgt_msg, 'R',
                         /*v_user*/
                         NULL, TRUNC (SYSDATE),
                         TRUNC (SYSDATE), TRUNC (SYSDATE)
                        );
         END;

         BEGIN
            tqc_email_pkg.send_mail (tqc_sms_code_seq.CURRVAL);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Sending Mail');
         END;
         
   END IF;   
      
      END LOOP;
         
         
     FOR i in REQUESTS_LVL_ONE loop
       
       SELECT USR_USERNAME,USR_NAME
       INTO v_user_name, v_name
       FROM tqc_users
       where USR_CODE=  i.SRE_LVL_ONE_ASSIGNEE;
       
              BEGIN
       
       SELECT USR_EMAIL
       INTO v_email
       FROM tqc_users
       where USR_CODE=  i.SRE_LVL_ONE_ASSIGNEE;
       EXCEPTION
            WHEN NO_DATA_FOUND THEN
       raise_error ( 'User  '||v_user_name||' DOES NOT HAVE AN EMAIL ADDRESS...');
       END ;
       
        SELECT REPLACE(v_msgt_msg, '[ASSIGNEE]', v_name)
        INTO v_msgt_msg
        FROM DUAL;
        
        SELECT REPLACE(v_msgt_msg, '[TICKET]', I.TSR_REF_NUMBER)
        INTO v_msgt_msg
        FROM DUAL;
        
         IF I.TSR_ASSIGNEE <>  i.SRE_LVL_ONE_ASSIGNEE THEN
                        UPDATE TQC_SERV_REQUESTS
                        SET TSR_ASSIGNEE= i.SRE_LVL_ONE_ASSIGNEE
                        WHERE TSR_CODE=I.TSR_CODE;
                       
                         BEGIN
                            INSERT INTO tqc_email_messages
                                        (email_code, email_sys_code, email_sys_module,
                                         email_clnt_code, email_agn_code, email_pol_code,
                                         email_pol_no, email_clm_no, email_quot_code,
                                         email_address, email_subj, email_msg, email_status,
                                         email_prepared_by, email_prepared_date,
                                         email_send_date, email_to_send_date
                                        )
                                 VALUES (tqc_sms_code_seq.NEXTVAL, 1, 'SRV',
                                         v_alrt_code, v_alrt_code, v_alrt_code,
                                         v_alrt_code, v_alrt_code, v_alrt_code,
                                         v_email, NULL, v_msgt_msg, 'R',
                                         /*v_user*/
                                         NULL, TRUNC (SYSDATE),
                                         TRUNC (SYSDATE), TRUNC (SYSDATE)
                                        );
                         END;

                         BEGIN
                            tqc_email_pkg.send_mail (tqc_sms_code_seq.CURRVAL);
                         EXCEPTION
                            WHEN OTHERS
                            THEN
                               raise_error ('Sending Mail');
                         END;
         END IF;
      
      
      END LOOP;
         
         
    
   EXCEPTION
      when others
      THEN NULL;
--         raise_error ('ERROR SENDING SERVICE REQUEST ALERTS');
   END process_serv_requests_alert;
   
     FUNCTION getworkingdays (v_date_from DATE, v_date_to DATE)
      RETURN NUMBER
   IS
      v_tot_no             NUMBER := 0;
      v_non_working_days   NUMBER := 0;
      v_count              NUMBER := 0;
      v_holiday_date       DATE;
   BEGIN
      v_tot_no := v_date_to - v_date_from;

      IF v_tot_no != 0
      THEN
         FOR v_day IN 0 .. (v_tot_no - 1)
         LOOP
            BEGIN
               SELECT   COUNT (*), TO_DATE (hld_date, 'DD/MM/RRRR')
                   INTO v_count, v_holiday_date
                   FROM tqc_holidays
                  WHERE TRUNC (TO_DATE (hld_date, 'DD/MM/RRRR')) =
                           TRUNC (TO_DATE (v_date_from + v_day, 'DD/MM/RRRR'))
               GROUP BY TO_DATE (hld_date, 'DD/MM/RRRR');
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_count := 0;
            END;

            v_non_working_days := v_non_working_days + v_count;

            IF TO_CHAR (TO_DATE ((v_date_from + v_day), 'DD/MM/RRRR'),
                        'DY',
                        'nls_date_language=english'
                       ) = 'SUN'
            THEN
               v_non_working_days := v_non_working_days + 1;
            --RAISE_ERROR('v_non_working_days='||v_non_working_days);
            ELSIF     TO_CHAR (TO_DATE ((v_date_from + v_day), 'DD/MM/RRRR'),
                               'DY',
                               'nls_date_language=english'
                              ) = 'SAT'
                  AND NVL (v_holiday_date, SYSDATE) !=
                                TO_DATE ((v_date_from + v_day), 'DD/MM/RRRR')
            THEN
               v_non_working_days := v_non_working_days + 1;
            -- raise_error('here...');
            END IF;
         --RAISE_ERROR('HERE...'||(v_date_from+v_day));
         END LOOP;

         v_tot_no := v_tot_no + 1;
      -- raise_error('Non working days='||v_non_working_days);
      ELSE
         v_tot_no := 1;
      END IF;

      RETURN v_tot_no - v_non_working_days;
   END getworkingdays;
   
  /* Formatted on 2015/06/24 14:48 (Formatter Plus v4.8.8) */
PROCEDURE process_serv_req_email_alert( 
        v_usr_code               NUMBER,
        v_ticket_no             VARCHAR2,
        v_details               VARCHAR2,
        v_tsracccode  NUMBER
  )
IS
   v_msgt_msg            VARCHAR2 (800);
   v_final_msgt_msg      VARCHAR2 (200);
   v_alrt_grp_usr_code   NUMBER;
   v_email               VARCHAR2 (200);
   sender                VARCHAR2 (200);
   v_out                 NUMBER;
   v_alrt_code           NUMBER;
   v_param_val           NUMBER;
   v_count               NUMBER;
   v_user_name           VARCHAR2 (200);
   v_name                varchar2(200);
   
BEGIN
   BEGIN
      SELECT msgt_msg
        INTO v_msgt_msg
        FROM tqc_msg_templates
       WHERE msgt_sht_desc = 'SERV_REQ_EMAIL';
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('SERV_REQ_EMAIL Message Template not Defined...');
   END;

      SELECT usr_username, usr_name
        INTO v_user_name, v_name
        FROM tqc_users
       WHERE usr_code = v_usr_code;

      BEGIN
         SELECT usr_email
           INTO v_email
           FROM tqc_users
          WHERE usr_code = v_usr_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error (   'User  '
                         || v_user_name
                         || ' DOES NOT HAVE AN EMAIL ADDRESS...'
                        );
      END;

      SELECT REPLACE (v_msgt_msg, '[ASSIGNEE]', v_name)
        INTO v_msgt_msg
        FROM DUAL;

      SELECT REPLACE (v_msgt_msg, '[TICKET]', v_ticket_no)
        INTO v_msgt_msg
        FROM DUAL;
        
        
        SELECT REPLACE (v_msgt_msg, '[Details]', v_details)
        INTO v_msgt_msg
        FROM DUAL;
        

     
      
        

         BEGIN
            INSERT INTO tqc_email_messages
                        (email_code,
                         email_sys_code,
                          email_sys_module,
                         email_clnt_code,
                          email_agn_code, 
                          email_pol_code,
                         email_pol_no, 
                         email_clm_no, 
                         email_quot_code,
                         email_address, 
                         email_subj, 
                         email_msg, 
                         email_status,
                         email_prepared_by, 
                         email_prepared_date,
                         email_send_date, 
                         email_to_send_date
                        )
                 VALUES (tqc_sms_code_seq.NEXTVAL,
                            1, 
                            'REC',
                            v_tsracccode, 
                            v_tsracccode, 
                            v_alrt_code,
                            v_alrt_code, 
                            v_alrt_code, 
                            v_alrt_code,
                            v_email, 
                            'Service Request',
                             v_msgt_msg,
                              'R',
                            /*v_user*/
                            NULL, TRUNC (SYSDATE),
                            TRUNC (SYSDATE), TRUNC (SYSDATE)
                        );
         END;

         BEGIN
            tqc_email_pkg.send_serv_req_mail (tqc_sms_code_seq.CURRVAL);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Sending Mail: ');
         END;
     
EXCEPTION
   WHEN OTHERS
   then
   NULL;
      --raise_error ('ERROR SENDING SERVICE REQUEST ALERTS');
END process_serv_req_email_alert;

PROCEDURE changeClntRequestStatus(v_tsr_code NUMBER,
                                  v_status   VARCHAR2)
IS

BEGIN
    --raise_error('v_status '||v_status|| ' v_tsr_code '||v_tsr_code);
    UPDATE TQC_SERV_REQUESTS
    SET TSR_CLIENT_STATUS= v_status
    WHERE TSR_CODE=v_tsr_code;
    
    IF v_status = 'Satisfied' THEN
        
        UPDATE TQC_SERV_REQUESTS
        SET TSR_STATUS= 'Closed'
        WHERE TSR_CODE=v_tsr_code;
    
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        RAISE_ERROR('Changing service request status ..'||SQLERRM(SQLCODE));
END;

PROCEDURE reassign_task(v_taskid  IN VARCHAR2,
                          v_user    IN VARCHAR2,
                          v_remarks IN VARCHAR2,
                          v_sys_code IN NUMBER,
                          v_from    IN VARCHAR2 DEFAULT NULL) IS
  BEGIN
    IF v_sys_code = 27 THEN 
    
        UPDATE tqc_bpm_tickets
           SET tckt_to      = v_user,
               tckt_remarks = v_remarks,
               TCKT_BY      = NVL(v_from, TCKT_BY)
         WHERE tckt_code = v_taskid;
      
        UPDATE jbpm4_task SET ASSIGNEE_ = v_user WHERE DBID_ = v_taskid;
        
    ELSIF v_sys_code = 37 THEN
    
         IF v_user = 'AOSINBOYEJO' THEN
                   raise_error ( 'Cannot assign ticket to this user...' );

               END IF;
               
		
        UPDATE gin_bpm_tickets
           SET tckt_to = v_user,
               tckt_remarks = v_remarks,
               tckt_by = NVL (v_from, tckt_by)
         WHERE tckt_code = v_taskid;
         
        UPDATE jbpm4_task_gis
           SET assignee_ = v_user
         WHERE dbid_ = v_taskid;
    
    END IF ;
    
    COMMIT;
    
  EXCEPTION
    WHEN OTHERS THEN
      raise_error('error updating task details for reassignment...');
  END;
  
  FUNCTION get_tckt_dtls( v_user     IN VARCHAR2 DEFAULT NULL,
                         v_quo_no VARCHAR2 DEFAULT NULL,
                         v_pol_no VARCHAR2 DEFAULT NULL,
                         v_claim_no VARCHAR2 DEFAULT NULL,
                         v_syscode  NUMBER DEFAULT NULL) RETURN tickets_ref 
IS
v_refcur tickets_ref;
v_quo_code NUMBER;
v_pol_code NUMBER;
BEGIN
   
    IF v_syscode = 37 THEN
    
        IF v_user IS NOT NULL  THEN 
        
        
        
            BEGIN
            OPEN v_refcur FOR
              SELECT *
                FROM (SELECT a.*
                        FROM (SELECT gin_bpm_tickets.tckt_code,
                                     DECODE(gin_bpm_tickets.tckt_sys_code,
                                            37,
                                            'GENERAL INSURANCE SYSTEM',
                                            27,
                                            'LIFE MANAGEMENT SYSTEM',
                                            1,
                                            'FINANCIAL MANAGEMENT SYSTEM',
                                            0,
                                            'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                                     DECODE(gin_bpm_tickets.tckt_sys_module,
                                            'Q',
                                            'Quotation',
                                            'P',
                                            'Underwriting',
                                            'C',
                                            'Claims') sysmodule,
                                     gin_bpm_tickets.tckt_clnt_code,
                                     (clnt_name || ' ' || clnt_other_names) client,
                                     gin_bpm_tickets.tckt_agn_code,
                                     agn_name AGENT,
                                     gin_bpm_tickets.tckt_pol_code,
                                     gin_bpm_tickets.tckt_pol_no,
                                     gin_bpm_tickets.tckt_clm_no,
                                     gin_bpm_tickets.tckt_quot_code,
                                     gin_bpm_tickets.tckt_quo_no,
                                     gin_bpm_tickets.tckt_by,
                                     gin_bpm_tickets.tckt_date,
                                     gin_bpm_tickets.tckt_process_id,
                                     gin_bpm_tickets.tckt_sys_module sys_module_code,
                                     tckt_endr_code,
                                     tckt_prod_type,
                                     tckt_to,
                                     tckt_remarks,
                                     name_ tckt_name,
                                     duedate_ tckt_due_date,
                                     tckt_endorsement,
                                     tckt_transno,
                                     DECODE(tckt_sys_module,
                                            'Q',
                                            NVL(tckt_quo_no,tckt_quot_code),
                                            'C',
                                            tckt_clm_no,
                                            tckt_pol_no) ref_no,
                                     tckt_prp_code,
                                     NULL,
                                     tckt_type,
                                     NULL POLICY_STATUS,
                                     TCKT_TRAN_EFF_DATE,
                                     TCKT_GGT_NO,
                                     USR_TYPE,
                                     NULL POL_CURRENT_STATUS
                                FROM gin_bpm_tickets,
                                     tqc_clients,
                                     tqc_agencies,
                                     jbpm4_task_gis,
                                     tqc_users
                               WHERE gin_bpm_tickets.tckt_clnt_code =
                                     clnt_code(+)
                                    --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                 AND agn_code(+) =
                                     gin_bpm_tickets.tckt_agn_code
                                 and ASSIGNEE_=USR_USERNAME
                                 AND dbid_ = gin_bpm_tickets.tckt_code
                                 AND (assignee_ = v_user
                                      or assignee_  IN (SELECT GRP.USR_USERNAME FROM TQC_USERS GRP,TQC_GROUP_USERS ,TQC_USERS USR
                                                         WHERE GRP.USR_CODE = GUSR_GRP_USR_CODE
                                                         AND USR.USR_CODE = GUSR_USR_CODE
                                                         AND USR.USR_USERNAME = v_user))
                                    --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                 AND tckt_active != 'N'
                                 AND tckt_type = 'S'
                                 AND tckt_sys_code =
                                     NVL(v_syscode, tckt_sys_code)
                               ORDER BY gin_bpm_tickets.tckt_code DESC) a
                       WHERE a.ref_no IS NOT NULL
                      UNION ALL
                      SELECT gin_bpm_tickets.tckt_code,
                             DECODE(gin_bpm_tickets.tckt_sys_code,
                                    37,
                                    'GENERAL INSURANCE SYSTEM',
                                    27,
                                    'LIFE MANAGEMENT SYSTEM',
                                    1,
                                    'FINANCIAL MANAGEMENT SYSTEM',
                                    0,
                                    'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                             DECODE(gin_bpm_tickets.tckt_sys_module,
                                    'Q',
                                    'Quotation',
                                    'P',
                                    'Underwriting',
                                    'C',
                                    'Claims') sysmodule,
                             gin_bpm_tickets.tckt_clnt_code,
                             (clnt_name || ' ' || clnt_other_names) client,
                             gin_bpm_tickets.tckt_agn_code,
                             agn_name AGENT,
                             gin_bpm_tickets.tckt_pol_code,
                             gin_bpm_tickets.tckt_pol_no,
                             gin_bpm_tickets.tckt_clm_no,
                             gin_bpm_tickets.tckt_quot_code,
                             gin_bpm_tickets.tckt_quo_no,
                             gin_bpm_tickets.tckt_by,
                             gin_bpm_tickets.tckt_date,
                             gin_bpm_tickets.tckt_process_id,
                             gin_bpm_tickets.tckt_sys_module sys_module_code,
                             tckt_endr_code,
                             tckt_prod_type,
                             tckt_to,
                             tckt_remarks,
                             name_ tckt_name,
                             duedate_ tckt_due_date,
                             tckt_endorsement,
                             tckt_transno,
                             tckt_extern_ref_no ref_no,
                             tckt_prp_code,
                             NULL,
                             tckt_type,
                             NULL POLICY_STATUS,
                             TCKT_TRAN_EFF_DATE,
                             TCKT_GGT_NO,
                             NULL USR_TYPE,
                             NULL POL_CURRENT_STATUS
                        FROM gin_bpm_tickets,
                             tqc_clients,
                             tqc_agencies,
                             jbpm4_task_gis
                       WHERE gin_bpm_tickets.tckt_clnt_code = clnt_code(+)
                         AND tckt_type = 'E'
                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                         AND agn_code(+) = gin_bpm_tickets.tckt_agn_code
                         AND dbid_ = gin_bpm_tickets.tckt_code
                            --AND TCKT_CLM_NO = NVL(:v_claim_no, TCKT_CLM_NO)
                         AND tckt_active != 'N'
                         AND assignee_ = v_user
                         AND tckt_sys_code = NVL(v_syscode, tckt_sys_code))
               ORDER BY tckt_code DESC;
          
            
          END;
          
          RETURN v_refcur;
       
        END IF;
        
        IF v_pol_no IS NOT NULL THEN
        
           
             OPEN v_refcur FOR
      SELECT *
        FROM (SELECT a.*
                FROM (SELECT gin_bpm_tickets.tckt_code,
                             DECODE(gin_bpm_tickets.tckt_sys_code,
                                    37,
                                    'GENERAL INSURANCE SYSTEM',
                                    27,
                                    'LIFE MANAGEMENT SYSTEM',
                                    1,
                                    'FINANCIAL MANAGEMENT SYSTEM',
                                    0,
                                    'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                             DECODE(gin_bpm_tickets.tckt_sys_module,
                                    'Q',
                                    'Quotation',
                                    'P',
                                    'Underwriting',
                                    'C',
                                    'Claims') sysmodule,
                             gin_bpm_tickets.tckt_clnt_code,
                             (clnt_name || ' ' || clnt_other_names) client,
                             gin_bpm_tickets.tckt_agn_code,
                             agn_name AGENT,
                             gin_bpm_tickets.tckt_pol_code,
                             gin_bpm_tickets.tckt_pol_no,
                             gin_bpm_tickets.tckt_clm_no,
                             gin_bpm_tickets.tckt_quot_code,
                             gin_bpm_tickets.tckt_quo_no,
                             gin_bpm_tickets.tckt_by,
                             gin_bpm_tickets.tckt_date,
                             gin_bpm_tickets.tckt_process_id,
                             gin_bpm_tickets.tckt_sys_module sys_module_code,
                             tckt_endr_code,
                             tckt_prod_type,
                             tckt_to,
                             tckt_remarks,
                             name_ tckt_name,
                             duedate_ tckt_due_date,
                             tckt_endorsement,
                             tckt_transno,
                             DECODE(tckt_sys_module,
                                    'Q',
                                    tckt_quo_no,
                                    'C',
                                    tckt_clm_no,
                                    tckt_pol_no) ref_no,
                             tckt_prp_code,
                             NULL,
                             tckt_type,
                             DECODE(POL_POLICY_STATUS,
                                    'NB',
                                    'NB',
                                    'SP',
                                    'NB',
                                    'RN',
                                    'NB',
                                    'RE',
                                    'NB',
                                    'DC',
                                    'EN',
                                    'EN',
                                    'EN',
                                    'EX',
                                    'EN',
                                    'CN',
                                    'EN',
                                    POL_POLICY_STATUS) POL_STAT,
                             TCKT_TRAN_EFF_DATE,
                             TCKT_GGT_NO,
                             NULL USR_TYPE,
                             POL_CURRENT_STATUS
                        FROM gin_bpm_tickets,
                             tqc_clients,
                             tqc_agencies,
                             jbpm4_task_gis,
                             gin_policies
                       WHERE gin_bpm_tickets.tckt_clnt_code = clnt_code(+)
                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                         AND agn_code(+) = gin_bpm_tickets.tckt_agn_code
                         and tckt_pol_code = pol_batch_no
                         AND dbid_ = gin_bpm_tickets.tckt_code
                            --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                         AND tckt_active != 'N'
                         AND tckt_type = 'S'
                         and TCKT_SYS_CODE = 37
                         and TCKT_PROCESS_ID is not null
                         AND gin_bpm_tickets.tckt_pol_no LIKE
                             '%' || v_pol_no || '%'
                         AND tckt_sys_module IN
                             ('P', 'U', 'R', 'RT', 'RE', 'E')
                      UNION
                      SELECT gin_bpm_tickets.tckt_code,
                             DECODE(gin_bpm_tickets.tckt_sys_code,
                                    37,
                                    'GENERAL INSURANCE SYSTEM',
                                    27,
                                    'LIFE MANAGEMENT SYSTEM',
                                    1,
                                    'FINANCIAL MANAGEMENT SYSTEM',
                                    0,
                                    'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                             DECODE(gin_bpm_tickets.tckt_sys_module,
                                    'Q',
                                    'Quotation',
                                    'P',
                                    'Underwriting',
                                    'C',
                                    'Claims') sysmodule,
                             gin_bpm_tickets.tckt_clnt_code,
                             (clnt_name || ' ' || clnt_other_names) client,
                             gin_bpm_tickets.tckt_agn_code,
                             agn_name AGENT,
                             gin_bpm_tickets.tckt_pol_code,
                             gin_bpm_tickets.tckt_pol_no,
                             gin_bpm_tickets.tckt_clm_no,
                             gin_bpm_tickets.tckt_quot_code,
                             gin_bpm_tickets.tckt_quo_no,
                             gin_bpm_tickets.tckt_by,
                             gin_bpm_tickets.tckt_date,
                             gin_bpm_tickets.tckt_process_id,
                             gin_bpm_tickets.tckt_sys_module sys_module_code,
                             tckt_endr_code,
                             tckt_prod_type,
                             tckt_to,
                             tckt_remarks,
                             name_ tckt_name,
                             duedate_ tckt_due_date,
                             tckt_endorsement,
                             tckt_transno,
                             DECODE(tckt_sys_module,
                                    'Q',
                                    tckt_quo_no,
                                    'C',
                                    tckt_clm_no,
                                    tckt_pol_no) ref_no,
                             tckt_prp_code,
                             NULL,
                             tckt_type,
                             DECODE(POL_POLICY_STATUS,
                                    'RN',
                                    'RN',
                                    'RE',
                                    'RE',
                                    POL_POLICY_STATUS) POL_STAT,
                             TCKT_TRAN_EFF_DATE,
                             TCKT_GGT_NO,
                             NULL USR_TYPE,
                             POL_CURRENT_STATUS
                        FROM gin_bpm_tickets,
                             tqc_clients,
                             tqc_agencies,
                             jbpm4_task_gis,
                             gin_ren_policies
                       WHERE gin_bpm_tickets.tckt_clnt_code = clnt_code(+)
                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                         AND agn_code(+) = gin_bpm_tickets.tckt_agn_code
                         and tckt_pol_code = pol_batch_no
                         AND dbid_ = gin_bpm_tickets.tckt_code
                            
                            --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                         AND tckt_active != 'N'
                         AND gin_bpm_tickets.tckt_pol_no LIKE
                             '%' || v_pol_no || '%'
                         AND tckt_type = 'S'
                         and TCKT_SYS_CODE = 37
                         and TCKT_PROCESS_ID is not null
                         AND tckt_sys_module IN
                             ('P', 'U', 'R', 'RT', 'RE', 'E')) a
               WHERE a.ref_no IS NOT NULL
              UNION ALL
              SELECT gin_bpm_tickets.tckt_code,
                     DECODE(gin_bpm_tickets.tckt_sys_code,
                            37,
                            'GENERAL INSURANCE SYSTEM',
                            27,
                            'LIFE MANAGEMENT SYSTEM',
                            1,
                            'FINANCIAL MANAGEMENT SYSTEM',
                            0,
                            'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                     DECODE(gin_bpm_tickets.tckt_sys_module,
                            'Q',
                            'Quotation',
                            'P',
                            'Underwriting',
                            'C',
                            'Claims') sysmodule,
                     gin_bpm_tickets.tckt_clnt_code,
                     (clnt_name || ' ' || clnt_other_names) client,
                     gin_bpm_tickets.tckt_agn_code,
                     agn_name AGENT,
                     gin_bpm_tickets.tckt_pol_code,
                     gin_bpm_tickets.tckt_pol_no,
                     gin_bpm_tickets.tckt_clm_no,
                     gin_bpm_tickets.tckt_quot_code,
                     gin_bpm_tickets.tckt_quo_no,
                     gin_bpm_tickets.tckt_by,
                     gin_bpm_tickets.tckt_date,
                     gin_bpm_tickets.tckt_process_id,
                     gin_bpm_tickets.tckt_sys_module sys_module_code,
                     tckt_endr_code,
                     tckt_prod_type,
                     tckt_to,
                     tckt_remarks,
                     name_ tckt_name,
                     duedate_ tckt_due_date,
                     tckt_endorsement,
                     tckt_transno,
                     tckt_extern_ref_no ref_no,
                     tckt_prp_code,
                     NULL,
                     tckt_type,
                     DECODE(POL_POLICY_STATUS,
                            'NB',
                            'NB',
                            'SP',
                            'NB',
                            'RN',
                            'NB',
                            'RE',
                            'NB',
                            'DC',
                            'EN',
                            'EN',
                            'EN',
                            'EX',
                            'EN',
                            'CN',
                            'EN',
                            POL_POLICY_STATUS) POL_STAT,
                     TCKT_TRAN_EFF_DATE,
                     TCKT_GGT_NO,
                     NULL USR_TYPE,
                     POL_CURRENT_STATUS
                FROM gin_bpm_tickets,
                     tqc_clients,
                     tqc_agencies,
                     jbpm4_task_gis,
                     gin_policies
               WHERE gin_bpm_tickets.tckt_clnt_code = clnt_code(+)
                 AND tckt_type = 'E'
                    --AND PRP_CLNT_CODE = CLNT_CODE(+)
                 AND agn_code(+) = gin_bpm_tickets.tckt_agn_code
                 AND dbid_ = gin_bpm_tickets.tckt_code
                 and TCKT_SYS_CODE = 37
                 and tckt_pol_code = pol_batch_no
                 AND gin_bpm_tickets.tckt_pol_no LIKE '%' || v_pol_no || '%'
                 and TCKT_PROCESS_ID is not null
                 AND tckt_sys_module IN ('P', 'U', 'R', 'RT', 'RE', 'E')
                    --AND TCKT_CLM_NO = NVL(:v_claim_no, TCKT_CLM_NO)
                 AND tckt_active != 'N'
              UNION
              SELECT gin_bpm_tickets.tckt_code,
                     DECODE(gin_bpm_tickets.tckt_sys_code,
                            37,
                            'GENERAL INSURANCE SYSTEM',
                            27,
                            'LIFE MANAGEMENT SYSTEM',
                            1,
                            'FINANCIAL MANAGEMENT SYSTEM',
                            0,
                            'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                     DECODE(gin_bpm_tickets.tckt_sys_module,
                            'Q',
                            'Quotation',
                            'P',
                            'Underwriting',
                            'C',
                            'Claims') sysmodule,
                     gin_bpm_tickets.tckt_clnt_code,
                     (clnt_name || ' ' || clnt_other_names) client,
                     gin_bpm_tickets.tckt_agn_code,
                     agn_name AGENT,
                     gin_bpm_tickets.tckt_pol_code,
                     gin_bpm_tickets.tckt_pol_no,
                     gin_bpm_tickets.tckt_clm_no,
                     gin_bpm_tickets.tckt_quot_code,
                     gin_bpm_tickets.tckt_quo_no,
                     gin_bpm_tickets.tckt_by,
                     gin_bpm_tickets.tckt_date,
                     gin_bpm_tickets.tckt_process_id,
                     gin_bpm_tickets.tckt_sys_module sys_module_code,
                     tckt_endr_code,
                     tckt_prod_type,
                     tckt_to,
                     tckt_remarks,
                     name_ tckt_name,
                     duedate_ tckt_due_date,
                     tckt_endorsement,
                     tckt_transno,
                     tckt_extern_ref_no ref_no,
                     tckt_prp_code,
                     NULL,
                     tckt_type,
                     DECODE(POL_POLICY_STATUS,
                            'RN',
                            'RN',
                            'RE',
                            'RE',
                            POL_POLICY_STATUS) POL_STAT,
                     TCKT_TRAN_EFF_DATE,
                     TCKT_GGT_NO,
                     NULL USR_TYPE,
                     POL_CURRENT_STATUS
                FROM gin_bpm_tickets,
                     tqc_clients,
                     tqc_agencies,
                     jbpm4_task_gis,
                     gin_ren_policies
               WHERE gin_bpm_tickets.tckt_clnt_code = clnt_code(+)
                 AND tckt_type = 'E'
                    --AND PRP_CLNT_CODE = CLNT_CODE(+)
                 AND agn_code(+) = gin_bpm_tickets.tckt_agn_code
                 AND dbid_ = gin_bpm_tickets.tckt_code
                 and TCKT_SYS_CODE = 37
                 and tckt_pol_code = pol_batch_no
                 and TCKT_PROCESS_ID is not null
                 AND tckt_sys_module IN ('RT', 'RE')
                 AND gin_bpm_tickets.tckt_pol_no LIKE '%' || v_pol_no || '%'
                 AND tckt_active != 'N')
       ORDER BY tckt_code DESC;
        
        END IF;
        
        RETURN(v_refcur);
    
    
    ELSIF v_syscode = 27 THEN
    
    BEGIN
      IF v_quo_no IS NOT NULL THEN
          BEGIN
                SELECT QUO_CODE
                INTO v_quo_code
                FROM LMS_QUOTATIONS
                WHERE QUO_NO = v_quo_no;
          EXCEPTION
            WHEN OTHERS THEN
                RAISE_ERROR('Error fetching quotation code!');
          END;    
      END IF; 
      
      IF v_pol_no IS NOT NULL THEN
          BEGIN
                SELECT POL_CODE
                INTO v_pol_code
                FROM LMS_POLICIES
                WHERE POL_POLICY_NO = v_pol_no;
          EXCEPTION
            WHEN OTHERS THEN
                RAISE_ERROR('Error fetching quotation code!');
          END;    
      END IF;  
  
  
    IF v_quo_code IS NOT NULL THEN    
      BEGIN
        OPEN v_refcur FOR
          SELECT a.*
            FROM (SELECT tqc_bpm_tickets.tckt_code,
                         DECODE(tqc_bpm_tickets.tckt_sys_code,
                                37,
                                'GENERAL INSURANCE SYSTEM',
                                27,
                                'LIFE MANAGEMENT SYSTEM',
                                26, 
                                'LIFE MANAGEMENT SYSTEM-LIFE',
                                1,
                                'FINANCIAL MANAGEMENT SYSTEM',
                                0,
                                'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                         DECODE(tqc_bpm_tickets.tckt_sys_module,
                                'Q',
                                'Quotation',
                                'P',
                                'Underwriting',
                                'E',
                                'Endorsements',
                                'R',
                                'Renewals',
                                'RT',
                                'Transfered Renewal',
                                'C',
                                'Claims') sysmodule,
                         tqc_bpm_tickets.tckt_clnt_code,
                         (clnt_name || ' ' || clnt_other_names) client,
                         tqc_bpm_tickets.tckt_agn_code,
                         agn_name AGENT,
                         tqc_bpm_tickets.tckt_pol_code,
                         tqc_bpm_tickets.tckt_pol_no,
                         tqc_bpm_tickets.tckt_clm_no,
                         tqc_bpm_tickets.tckt_quot_code,
                         tqc_bpm_tickets.tckt_quo_no,
                         tqc_bpm_tickets.tckt_by,
                         tqc_bpm_tickets.tckt_date,
                         tqc_bpm_tickets.tckt_process_id,
                         tqc_bpm_tickets.tckt_sys_module sys_module_code,
                         tckt_endr_code,
                         tckt_prod_type,
                         tckt_to,
                         tckt_remarks,
                         name_ tckt_name,
                         duedate_ tckt_due_date,
                         tckt_endorsement,
                         tckt_transno,
                         DECODE(tckt_sys_module,
                                'Q',
                                tckt_quo_no,
                                'C',
                                tckt_clm_no,
                                tckt_pol_no) ref_no,
                         tckt_prp_code,
                         NULL,
                         tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE, NULL STATUS
                    FROM tqc_bpm_tickets,
                         tqc_clients,
                         tqc_agencies,
                         jbpm4_task
                   WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                        --AND PRP_CLNT_CODE = CLNT_CODE(+)
                     AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                     AND dbid_ = tqc_bpm_tickets.tckt_code
                     AND tckt_quot_code = NVL(v_quo_code, tckt_quot_code)
                     AND tckt_active != 'N'
                     AND tckt_sys_code = NVL(v_syscode, tckt_sys_code)
                   ORDER BY tqc_bpm_tickets.tckt_code DESC) a
           WHERE a.ref_no IS NOT NULL;
      
        RETURN(v_refcur);
      END;
    ELSIF v_pol_code IS NOT NULL AND v_claim_no IS NULL THEN
      BEGIN
 --RAISE_ERROR('v_syscode='||v_syscode||'v_pol_code='||v_pol_code);
        OPEN v_refcur FOR
          SELECT a.*
            FROM (SELECT tqc_bpm_tickets.tckt_code,
                         DECODE(tqc_bpm_tickets.tckt_sys_code,
                                37,
                                'GENERAL INSURANCE SYSTEM',
                                27,
                                'LIFE MANAGEMENT SYSTEM',
                                26, 
                                'LIFE MANAGEMENT SYSTEM-LIFE',
                                1,
                                'FINANCIAL MANAGEMENT SYSTEM',
                                0,
                                'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                         DECODE(tqc_bpm_tickets.tckt_sys_module,
                                'Q',
                                'Quotation',
                                'P',
                                'Underwriting',
                                'C',
                                'Claims') sysmodule,
                         tqc_bpm_tickets.tckt_clnt_code,
                         (clnt_name || ' ' || clnt_other_names) client,
                         tqc_bpm_tickets.tckt_agn_code,
                         agn_name AGENT,
                         tqc_bpm_tickets.tckt_pol_code,
                         tqc_bpm_tickets.tckt_pol_no,
                         tqc_bpm_tickets.tckt_clm_no,
                         tqc_bpm_tickets.tckt_quot_code,
                         tqc_bpm_tickets.tckt_quo_no,
                         tqc_bpm_tickets.tckt_by,
                         tqc_bpm_tickets.tckt_date,
                         tqc_bpm_tickets.tckt_process_id,
                         tqc_bpm_tickets.tckt_sys_module sys_module_code,
                         tckt_endr_code,
                         tckt_prod_type,
                         tckt_to,
                         tckt_remarks,
                         name_ tckt_name,
                         duedate_ tckt_due_date,
                         tckt_endorsement,
                         tckt_transno,
                         DECODE(tckt_sys_module,
                                'Q',
                                tckt_quo_no,
                                'C',
                                tckt_clm_no,
                                tckt_pol_no) ref_no,
                         tckt_prp_code,
                         NULL pol_client_policy_number,
                         tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE,NULL STATUS
                    FROM tqc_bpm_tickets,
                         tqc_clients,
                         tqc_agencies,
                         jbpm4_task,
                         lms_policies
                   WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                     AND tckt_pol_code = pol_code
                        --AND PRP_CLNT_CODE = CLNT_CODE(+)
                     AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                     AND dbid_ = tqc_bpm_tickets.tckt_code
                     AND tckt_pol_code = NVL(v_pol_code, tckt_pol_code)
                     AND tckt_active != 'N'
                     AND tckt_sys_code = NVL(v_syscode, tckt_sys_code)
                   ORDER BY tqc_bpm_tickets.tckt_code DESC) a
           WHERE ref_no IS NOT NULL;
      
        RETURN(v_refcur);
      END;
    ELSIF v_pol_code IS NOT NULL AND v_claim_no IS NOT NULL THEN
      BEGIN
        --RAISE_ERROR(v_claim_no);
        OPEN v_refcur FOR
          SELECT a.*
            FROM (SELECT tqc_bpm_tickets.tckt_code,
                         DECODE(tqc_bpm_tickets.tckt_sys_code,
                                37,
                                'GENERAL INSURANCE SYSTEM',
                                27,
                                'LIFE MANAGEMENT SYSTEM',
                                26, 
                                'LIFE MANAGEMENT SYSTEM-LIFE',
                                1,
                                'FINANCIAL MANAGEMENT SYSTEM',
                                0,
                                'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                         DECODE(tqc_bpm_tickets.tckt_sys_module,
                                'Q',
                                'Quotation',
                                'P',
                                'Underwriting',
                                'C',
                                'Claims') sysmodule,
                         tqc_bpm_tickets.tckt_clnt_code,
                         (clnt_name || ' ' || clnt_other_names) client,
                         tqc_bpm_tickets.tckt_agn_code,
                         agn_name AGENT,
                         tqc_bpm_tickets.tckt_pol_code,
                         tqc_bpm_tickets.tckt_pol_no,
                         tqc_bpm_tickets.tckt_clm_no,
                         tqc_bpm_tickets.tckt_quot_code,
                         tqc_bpm_tickets.tckt_quo_no,
                         tqc_bpm_tickets.tckt_by,
                         tqc_bpm_tickets.tckt_date,
                         tqc_bpm_tickets.tckt_process_id,
                         tqc_bpm_tickets.tckt_sys_module sys_module_code,
                         tckt_endr_code,
                         tckt_prod_type,
                         tckt_to,
                         tckt_remarks,
                         name_ tckt_name,
                         duedate_ tckt_due_date,
                         tckt_endorsement,
                         tckt_transno,
                         DECODE(tckt_sys_module,
                                'Q',
                                tckt_quo_no,
                                'C',
                                tckt_clm_no,
                                tckt_pol_no) ref_no,
                         tckt_prp_code,
                         NULL,
                         tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE,NULL STATUS
                    FROM tqc_bpm_tickets,
                         tqc_clients,
                         tqc_agencies,
                         jbpm4_task
                   WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                        --AND PRP_CLNT_CODE = CLNT_CODE(+)
                     AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                     AND dbid_ = tqc_bpm_tickets.tckt_code
                     AND tckt_clm_no = NVL(v_claim_no, tckt_clm_no)
                     AND tckt_active != 'N'
                     AND tckt_sys_code = NVL(v_syscode, tckt_sys_code)
                   ORDER BY tqc_bpm_tickets.tckt_code DESC) a
           WHERE ref_no IS NOT NULL;
      
        RETURN(v_refcur);
      END;
    ELSE
      BEGIN
        IF NVL(v_syscode, 0) = 27 THEN
          BEGIN
            OPEN v_refcur FOR
              /*SELECT a.*
                       FROM (SELECT   tqc_bpm_tickets.tckt_code,
                                      DECODE
                                         (tqc_bpm_tickets.tckt_sys_code,
                                          37, 'GENERAL INSURANCE SYSTEM',
                                          27, 'LIFE MANAGEMENT SYSTEM',
                                          1, 'FINANCIAL MANAGEMENT SYSTEM',
                                          0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM'
                                         ) usrsystem,
                                      DECODE
                                         (tqc_bpm_tickets.tckt_sys_module,
                                          'Q', 'Quotation',
                                          'P', 'Underwriting',
                                          'C', 'Claims'
                                         ) sysmodule,
                                      tqc_bpm_tickets.tckt_clnt_code,
                                      (clnt_name || ' ' || clnt_other_names
                                      ) client,
                                      tqc_bpm_tickets.tckt_agn_code,
                                      agn_name AGENT,
                                      tqc_bpm_tickets.tckt_pol_code,
                                      pol_policy_no tckt_pol_no,
                                      tqc_bpm_tickets.tckt_clm_no,
                                      tqc_bpm_tickets.tckt_quot_code,
                                      tqc_bpm_tickets.tckt_quo_no,
                                      tqc_bpm_tickets.tckt_by,
                                      tqc_bpm_tickets.tckt_date,
                                      tqc_bpm_tickets.tckt_process_id,
                                      tqc_bpm_tickets.tckt_sys_module
                                                              sys_module_code,
                                      tckt_endr_code, tckt_prod_type, tckt_to,
                                      tckt_remarks, name_ tckt_name,
                                      duedate_ tckt_due_date,
                                      tckt_endorsement, tckt_transno,
                                      DECODE (tckt_sys_module,
                                              'Q', tckt_quo_no,
                                              'C', tckt_clm_no,
                                              'MKTPROP', ppr_proposal_no,
                                              'MKT-TO-UNDPROP', ppr_proposal_no,
                                              pol_proposal_no
                                             ) ref_no,
                                      tckt_prp_code, NULL, tckt_type
                                 FROM tqc_bpm_tickets,
                                      tqc_clients,
                                      tqc_agencies,
                                      jbpm4_task,
                                      lms_proposers,
                                      lms_policies,
                                      lms_pol_proposals
                                WHERE tqc_bpm_tickets.tckt_clnt_code = prp_code(+)
                                  AND prp_clnt_code = clnt_code(+)
                                  AND tckt_pol_code = ppr_code(+)
                                  AND tckt_pol_code = pol_code(+)
                                  AND agn_code(+) =
                                                 tqc_bpm_tickets.tckt_agn_code
                                  AND dbid_ = tqc_bpm_tickets.tckt_code
                                  AND assignee_ = v_user
                                  --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                                  AND tckt_active != 'N'
                                  AND tckt_sys_code =
                                                NVL (v_syscode, tckt_sys_code)
                             ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                      WHERE a.ref_no IS NOT NULL;*/
                      SELECT tckt_code,
                                  DECODE(TCKT_SYS_CODE,
                                              37, 'GENERAL INSURANCE SYSTEM',
                                              27, 'LIFE MANAGEMENT SYSTEM',
                                              1, 'FINANCIAL MANAGEMENT SYSTEM',
                                              0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM'
                                             ) usrsystem,
                                 DECODE(TCKT_SYS_MODULE,
                                              'Q', 'Quotation',
                                              'P', 'Underwriting',
                                              'C', 'Claims',
                                              'E', 'Endorsements'
                                             ) sysmodule,
                                  PRP_CLNT_CODE tckt_clnt_code, 
                                  (PRP_OTHER_NAMES || ' ' || PRP_SURNAME) client,
                                   ENDR_AGEN_CODE tckt_agn_code,
                                   AGN_NAME agent,
                                   POL_CODE tckt_pol_code,
                                   POL_POLICY_NO tckt_pol_no,
                                   tckt_clm_no,
                                   NULL tckt_quot_code,
                                   NULL tckt_quo_no,
                                   tckt_by,
                                   tckt_date,
                                   tckt_process_id,
                                   tckt_sys_module sys_module_code,
                                   ENDR_CODE tckt_endr_code, 
                                   POL_PROD_TYPE tckt_prod_type, 
                                   tckt_to,
                                   tckt_remarks, 
                                   NAME_ tckt_name,
                                   DUEDATE_ tckt_due_date,
                                   tckt_endorsement, 
                                   tckt_transno,
                                   DECODE (tckt_sys_module,
                                          'Q', tckt_quo_no,
                                          'C', tckt_clm_no,
                                          'P', pol_policy_no--,
                                          --'MKTPROP', ppr_proposal_no,
                                          --'MKT-TO-UNDPROP', ppr_proposal_no,
                                          --pol_proposal_no
                                         ) ref_no,
                                  tckt_prp_code, 
                                  NULL,
                                  tckt_type,
                                 NULL POLICY_STATUS,
                                 NULL TCKT_TRAN_EFF_DATE,
                                 NULL TCKT_GGT_NO,
                                 NULL USR_TYPE,NULL STATUS
                    FROM TQC_BPM_TICKETS, LMS_POLICIES, LMS_PROPOSERS, LMS_POLICY_ENDORSEMENTS,
                              LMS_AGENCIES, JBPM4_TASK
                    WHERE TCKT_POL_CODE = POL_CODE
                        AND ENDR_POL_CODE = POL_CODE
                        AND NVL(TCKT_ENDR_CODE, POL_CURRENT_ENDR_CODE) = ENDR_CODE
                        AND POL_PRP_CODE = PRP_CODE
                        AND ENDR_AGEN_CODE = AGN_CODE
                        AND TCKT_CODE = DBID_
                        AND TCKT_ACTIVE != 'N'
                        AND POL_CLA_SHT_DESC = 'GRP' --Added since AIICO only using Process flow for Group. should be removed.
                        AND assignee_ = DECODE(v_user, 'ALL', assignee_, v_user)
                        AND TCKT_SYS_CODE = v_syscode
                    UNION ALL
                    SELECT tckt_code,
                                  DECODE(TCKT_SYS_CODE,
                                              37, 'GENERAL INSURANCE SYSTEM',
                                              27, 'LIFE MANAGEMENT SYSTEM',
                                              1, 'FINANCIAL MANAGEMENT SYSTEM',
                                              0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM'
                                             ) usrsystem,
                                 DECODE(TCKT_SYS_MODULE,
                                              'Q', 'Quotation',
                                              'P', 'Underwriting',
                                              'C', 'Claims',
                                              'E', 'Endorsements'
                                             ) sysmodule,
                                  PRP_CLNT_CODE tckt_clnt_code, 
                                  (PRP_OTHER_NAMES || ' ' || PRP_SURNAME) client,
                                   QUO_AGENCY_CODE tckt_agn_code,
                                   AGN_NAME agent,
                                   tckt_pol_code,
                                   tckt_pol_no,
                                   tckt_clm_no,
                                   tckt_quot_code,
                                   tckt_quo_no,
                                   tckt_by,
                                   tckt_date,
                                   tckt_process_id,
                                   tckt_sys_module sys_module_code,
                                   tckt_endr_code, 
                                   QUO_PROD_TYPE tckt_prod_type, 
                                   tckt_to,
                                   tckt_remarks, 
                                   NAME_ tckt_name,
                                   DUEDATE_ tckt_due_date,
                                   tckt_endorsement, 
                                   tckt_transno,
                                   DECODE (tckt_sys_module,
                                          'Q', tckt_quo_no,
                                          'C', tckt_clm_no,
                                          'P', tckt_pol_no--,
                                          --'MKTPROP', ppr_proposal_no,
                                          --'MKT-TO-UNDPROP', ppr_proposal_no,
                                          --pol_proposal_no
                                         ) ref_no,
                                  tckt_prp_code, 
                                  NULL,
                                  tckt_type,
                                 NULL POLICY_STATUS,
                                 NULL TCKT_TRAN_EFF_DATE,
                                 NULL TCKT_GGT_NO,
                                 NULL USR_TYPE,NULL STATUS
                    FROM TQC_BPM_TICKETS, LMS_QUOTATIONS, LMS_PROPOSERS,
                              LMS_AGENCIES, JBPM4_TASK
                    WHERE TCKT_QUOT_CODE = QUO_CODE
                        AND QUO_PRP_CODE = PRP_CODE
                        AND QUO_AGENCY_CODE = AGN_CODE
                        AND TCKT_CODE = DBID_
                        AND TCKT_ACTIVE != 'N'
                        AND QUO_CLA_SHT_DESC = 'GRP' --Added since AIICO only using Process flow for Group. should be removed.
                        AND assignee_ = DECODE(v_user, 'ALL', assignee_, v_user)
                        AND TCKT_SYS_CODE = v_syscode
                    ORDER BY tckt_code DESC;

                  RETURN (v_refcur);
          
            RETURN(v_refcur);
          END;
        ELSIF NVL(v_syscode, 0) = 26 THEN
          BEGIN
            OPEN v_refcur FOR
              SELECT a.*
                FROM (SELECT tqc_bpm_tickets.tckt_code,
                             DECODE(tqc_bpm_tickets.tckt_sys_code,
                                    37,
                                    'GENERAL INSURANCE SYSTEM',
                                    27,
                                    'LIFE MANAGEMENT SYSTEM',
                                    26, 
                                    'LIFE MANAGEMENT SYSTEM-LIFE',
                                    1,
                                    'FINANCIAL MANAGEMENT SYSTEM',
                                    0,
                                    'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                             DECODE(tqc_bpm_tickets.tckt_sys_module,
                                    'Q',
                                    'Quotation',
                                    'P',
                                    'Underwriting',
                                    'C',
                                    'Claims') sysmodule,
                             tqc_bpm_tickets.tckt_clnt_code,
                             (clnt_name || ' ' || clnt_other_names) client,
                             tqc_bpm_tickets.tckt_agn_code,
                             agn_name AGENT,
                             tqc_bpm_tickets.tckt_pol_code,
                             pol_policy_no tckt_pol_no,
                             tqc_bpm_tickets.tckt_clm_no,
                             tqc_bpm_tickets.tckt_quot_code,
                             tqc_bpm_tickets.tckt_quo_no,
                             tqc_bpm_tickets.tckt_by,
                             tqc_bpm_tickets.tckt_date,
                             tqc_bpm_tickets.tckt_process_id,
                             tqc_bpm_tickets.tckt_sys_module sys_module_code,
                             tckt_endr_code,
                             tckt_prod_type,
                             tckt_to,
                             tckt_remarks,
                             name_ tckt_name,
                             duedate_ tckt_due_date,
                             tckt_endorsement,
                             tckt_transno,
                             DECODE(tckt_sys_module,
                                    'Q',
                                    tckt_quo_no,
                                    'C',
                                    tckt_clm_no,
                                    'MKTPROP',
                                    ppr_proposal_no,
                                    'MKT-TO-UNDPROP',
                                    ppr_proposal_no,
                                    pol_proposal_no) ref_no,
                             tckt_prp_code,
                             NULL,
                             tckt_type,
                             NULL POLICY_STATUS,
                             TCKT_TRAN_EFF_DATE,
                             TCKT_GGT_NO,
                             NULL USR_TYPE,NULL STATUS
                        FROM tqc_bpm_tickets,
                             tqc_clients,
                             tqc_agencies,
                             jbpm4_task,
                             lms_proposers,
                             lms_policies,
                             lms_pol_proposals
                       WHERE tqc_bpm_tickets.tckt_clnt_code = prp_code(+)
                         AND prp_clnt_code = clnt_code(+)
                         AND tckt_pol_code = ppr_code(+)
                         AND tckt_pol_code = pol_code(+)
                         AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                         AND dbid_ = tqc_bpm_tickets.tckt_code
                         AND assignee_ = DECODE(v_user, 'ALL', assignee_, v_user)
                            --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                         AND tckt_active != 'N'
                         AND tckt_sys_code = NVL(v_syscode, tckt_sys_code)
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
               WHERE a.ref_no IS NOT NULL;
          
            RETURN(v_refcur);
          END;
        ELSE
          BEGIN
            OPEN v_refcur FOR
              SELECT a.*
                FROM (SELECT tqc_bpm_tickets.tckt_code,
                             DECODE(tqc_bpm_tickets.tckt_sys_code,
                                    37,
                                    'GENERAL INSURANCE SYSTEM',
                                    27,
                                    'LIFE MANAGEMENT SYSTEM',
                                    26, 
                                    'LIFE MANAGEMENT SYSTEM-LIFE',
                                    1,
                                    'FINANCIAL MANAGEMENT SYSTEM',
                                    0,
                                    'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM') usrsystem,
                             DECODE(tqc_bpm_tickets.tckt_sys_module,
                                    'Q',
                                    'Quotation',
                                    'P',
                                    'Underwriting',
                                    'C',
                                    'Claims') sysmodule,
                             tqc_bpm_tickets.tckt_clnt_code,
                             (clnt_name || ' ' || clnt_other_names) client,
                             tqc_bpm_tickets.tckt_agn_code,
                             agn_name AGENT,
                             tqc_bpm_tickets.tckt_pol_code,
                             tqc_bpm_tickets.tckt_pol_no,
                             tqc_bpm_tickets.tckt_clm_no,
                             tqc_bpm_tickets.tckt_quot_code,
                             tqc_bpm_tickets.tckt_quo_no,
                             tqc_bpm_tickets.tckt_by,
                             tqc_bpm_tickets.tckt_date,
                             tqc_bpm_tickets.tckt_process_id,
                             tqc_bpm_tickets.tckt_sys_module sys_module_code,
                             tckt_endr_code,
                             tckt_prod_type,
                             tckt_to,
                             tckt_remarks,
                             name_ tckt_name,
                             duedate_ tckt_due_date,
                             tckt_endorsement,
                             tckt_transno,
                             DECODE(tckt_sys_module,
                                    'Q',
                                    tckt_quo_no,
                                    'C',
                                    tckt_clm_no,
                                    tckt_pol_no) ref_no,
                             tckt_prp_code,
                             NULL,
                             tckt_type,
                             NULL POLICY_STATUS,
                             TCKT_TRAN_EFF_DATE,
                             TCKT_GGT_NO,
                             NULL USR_TYPE,NULL STATUS
                        FROM tqc_bpm_tickets,
                             tqc_clients,
                             tqc_agencies,
                             jbpm4_task
                       WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                         AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                         AND dbid_ = tqc_bpm_tickets.tckt_code
                         AND assignee_ = DECODE(v_user, 'ALL', assignee_, v_user)
                            --AND TCKT_QUOT_CODE = NVL(v_quo_code, TCKT_QUOT_CODE)
                         AND tckt_active != 'N'
                         AND tckt_sys_code = NVL(v_syscode, tckt_sys_code)
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
               WHERE a.ref_no IS NOT NULL;
          
            RETURN(v_refcur);
          END;
        END IF;
      END;
    END IF;
  END;
  
  
    
    
    END IF;
    
    END;
END tqc_service_requests; 
/