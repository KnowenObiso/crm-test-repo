--
-- TQC_WEB_CURSOR  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_web_cursor
AS
   /******************************************************************************
      NAME:       TQC_WEB_CURSOR
      PURPOSE:

      REVISIONS:
      Ver        Date        Author           Description
      ---------  ----------  ---------------  ------------------------------------
      1.0        30/Nov/2009             1. Created this package body.
   ******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20033,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   FUNCTION getmsgtemplate (
      v_msgshtdesc   IN   tqc_msg_templates.msgt_sht_desc%TYPE
   )
      RETURN msgtemplates_ref
   IS
      v_cursor   msgtemplates_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = v_msgshtdesc;

      RETURN v_cursor;
   END;

   PROCEDURE messagetemplates (
      v_msgtmplatecur   OUT   msgtemplates_ref,
      v_syscode               NUMBER,
      v_sysmodule             VARCHAR2,
      v_msgtype               VARCHAR2,
      v_msgshtdesc            VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      --  RAISE_ERROR('Sys code ' ||v_sysCode || 'sysmodule ' ||v_sysModule || 'msg Type '||v_msgType);
      OPEN v_msgtmplatecur FOR
         SELECT msgt_code, msgt_sht_desc, msgt_msg, msgt_sys_code,
                msgt_sys_module, msgt_type
           FROM tqc_msg_templates
          WHERE msgt_type = v_msgtype
            AND msgt_sys_code = v_syscode
            AND msgt_sys_module = v_sysmodule;
   --AND msgt_sht_desc = NVL(v_msgshtdesc, msgt_sht_desc);
   END messagetemplates;

   FUNCTION get_users
      RETURN users_ref
   IS
      vcursor   users_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT   usr_code, usr_username, usr_name, usr_email,
                  usr_personel_rank, usr_dt_created, usr_type, usr_status,
                  usr_pwd_reset
             FROM tqc_users
            WHERE usr_status = 'A'
         ORDER BY usr_username;

      RETURN vcursor;
   END;

   FUNCTION get_activeusers
      RETURN users_ref
   IS
      vcursor   users_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT usr_code, usr_username, usr_name, usr_email,
                usr_personel_rank, usr_dt_created, usr_type, usr_status,
                usr_pwd_reset
           FROM tqc_users
          WHERE usr_status = 'A';

      RETURN vcursor;
   END;

   FUNCTION get_escalationuser
      RETURN tqc_users.usr_code%TYPE
   IS
      v_usercode   NUMBER;
   BEGIN
      SELECT usr_code
        INTO v_usercode
        FROM tqc_users, tqc_escalation_levels
       WHERE tqel_user_code = usr_code AND usr_status = 'A';

      RETURN v_usercode;
   END;

   FUNCTION gettasks (v_usrcode IN jbpm4_task.assignee_%TYPE)
      RETURN tasks_ref
   IS
      v_cursor   tasks_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   tqsbt_sys_code, tqsbt_sys_module, tqsbt_execution_id,
                  tqsbt_ticket_no, tqsbt_ticket_code, tqsbt_ticket_desc,
                  create_, usr_username, priority_
             FROM tqc_system_bpm_tickets, jbpm4_task, tqc_users
            WHERE dbid_ = tqsbt_ticket_no
              AND assignee_ = TO_CHAR (usr_code)
              AND assignee_ = TO_CHAR (v_usrcode)
         ORDER BY tqsbt_sys_module, tqsbt_ticket_desc, create_;

      RETURN v_cursor;
   END;

   FUNCTION getalltasks (v_usrcode IN jbpm4_task.assignee_%TYPE)
      RETURN tasks_ref
   IS
      v_cursor   tasks_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   tqsbt_sys_code, tqsbt_sys_module, tqsbt_execution_id,
                  tqsbt_ticket_no, tqsbt_ticket_code, tqsbt_ticket_desc,
                  create_, usr_username, priority_
             FROM tqc_system_bpm_tickets, jbpm4_task, tqc_users
            WHERE dbid_ = tqsbt_ticket_no
         ORDER BY tqsbt_sys_module, tqsbt_ticket_desc, create_;

      RETURN v_cursor;
   END;

   FUNCTION getticketcodedetails (v_tcktcode VARCHAR2, v_type VARCHAR2)
      RETURN tasks_ref
   IS
      v_cursor   tasks_ref;
   BEGIN
      -- RAISE_ERROR('GATA '||v_tcktCode||' Type '||v_type);
      IF NVL (v_type, 'A') = 'MK'
      THEN
         OPEN v_cursor FOR
            SELECT tckt_sys_code, tckt_sys_module, execution_id_, tckt_code,
                   tckt_code, tckt_remarks, create_, usr_username, priority_
              FROM tqc_bpm_tickets, jbpm4_task, tqc_users
             WHERE dbid_ = tckt_code
               AND assignee_ = TO_CHAR (usr_code)
               AND (   tckt_sys_module = 'MKTPROP'
                    OR tckt_sys_module = 'MKT-TO-UNDPROP'
                   )
               AND tckt_active = 'Y'
               AND tckt_pol_code = v_tcktcode;

         RETURN v_cursor;
      ELSIF v_type = 'Q'
      THEN
         OPEN v_cursor FOR
            SELECT tckt_sys_code, tckt_sys_module, execution_id_, tckt_code,
                   tckt_code, tckt_remarks, create_, usr_username, priority_
              FROM tqc_bpm_tickets, jbpm4_task, tqc_users
             WHERE dbid_ = tckt_code
               AND assignee_ = TO_CHAR (usr_code)
               AND tckt_active = 'Y'
               AND tckt_quot_code = v_tcktcode;

         RETURN v_cursor;
      ELSIF v_type = 'UW'
      THEN
         OPEN v_cursor FOR
            SELECT tckt_sys_code, tckt_sys_module, execution_id_, tckt_code,
                   tckt_code, tckt_remarks, create_, usr_username, priority_
              FROM tqc_bpm_tickets, jbpm4_task, tqc_users
             WHERE dbid_ = tckt_code
               AND assignee_ = TO_CHAR (usr_code)
               AND (    tckt_sys_module <> 'MKTPROP'
                    AND tckt_sys_module <> 'MKT-TO-UNDPROP'
                   )
               AND tckt_active = 'Y'
               AND tckt_pol_code = v_tcktcode;

         RETURN v_cursor;
      ELSIF v_type = 'C'
      THEN
         OPEN v_cursor FOR
            SELECT tckt_sys_code, tckt_sys_module, execution_id_, tckt_code,
                   tckt_code, tckt_remarks, create_, usr_username, priority_
              FROM tqc_bpm_tickets, jbpm4_task, tqc_users
             WHERE dbid_ = tckt_code
               AND assignee_ = TO_CHAR (usr_code)
               AND tckt_active = 'Y'
               AND tckt_clm_no = v_tcktcode;

         RETURN v_cursor;
      ELSE
         OPEN v_cursor FOR
            SELECT tckt_sys_code, tckt_sys_module, execution_id_, tckt_code,
                   tckt_code, tckt_remarks, create_, usr_username, priority_
              FROM tqc_bpm_tickets, jbpm4_task, tqc_users
             WHERE dbid_ = tckt_code
               AND assignee_ = TO_CHAR (usr_code)
               AND tckt_active = 'Y';

         RETURN v_cursor;
      END IF;
   END;

   PROCEDURE getorgdivision (
      v_div_code           IN       NUMBER,
      v_org_division_cur   OUT      org_divisions_cur
   )
   IS
   BEGIN
      OPEN v_org_division_cur FOR
         SELECT div_name, div_sht_desc
           FROM tqc_divisions
          WHERE div_code = v_div_code;
   END;

   PROCEDURE getorgsubdivision (
      v_sdiv_code                   NUMBER,
      v_org_subdivision_cur   OUT   org_sub_divisions_cur
   )
   IS
   BEGIN
      OPEN v_org_subdivision_cur FOR
         SELECT sdiv_name, sdiv_sht_desc
           FROM tqc_subdivisions
          WHERE sdiv_code = v_sdiv_code;
   END;

   FUNCTION getcorrespondences (v_telquotecode NUMBER)
      RETURN correspendence_dtls_ref
   IS
      v_cursor   correspendence_dtls_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT 'E-MAIL', tquo_sur_name || '  ' || tquo_other_names,
                email_address, email_subj, email_msg, email_status,
                email_prepared_by, email_prepared_date, email_send_date
           FROM tqc_email_messages, lms_tel_quotes
          WHERE email_sys_module = 'TQ'
            AND email_quot_code = tquo_code
            AND tquo_code = v_telquotecode
         UNION
         SELECT 'SMS', tquo_sur_name || '  ' || tquo_other_names, sms_tel_no,
                NULL, sms_msg, sms_status, sms_prepared_by, sms_prepared_date,
                sms_send_date
           FROM tqc_sms_messages, lms_tel_quotes
          WHERE sms_quot_code = tquo_code
            AND sms_sys_module = 'TQ'
            AND tquo_code = v_telquotecode;

      RETURN v_cursor;
   END;

   FUNCTION getrptdetails (
      v_rptcode   tqc_system_reports.rpt_code%TYPE,
      v_orgcode   tqc_sys_rpt_templates.rpt_tmpl_org_code%TYPE DEFAULT NULL
   )
      RETURN report_dtls_ref
   IS
      v_cursor   report_dtls_ref;
      v_cnt number;
   BEGIN
      --RAISE_ERROR('v_rptcode'||v_rptcode||'v_orgcode'||v_orgcode);
      BEGIN
          SELECT count(1) into v_cnt
               FROM tqc_system_reports
              WHERE rpt_code = v_rptcode;
      EXCEPTION WHEN OTHERS THEN
            v_cnt :=0 ;
      END;
      
      IF v_cnt=0 THEN
            raise_error('Report :'||v_rptcode||' Not Found IN TQC_SYSTEM_REPORTS!');
      END IF;
      
      BEGIN
      SELECT count(1) into v_cnt
           FROM tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = v_rptcode ;
      EXCEPTION WHEN OTHERS THEN
         v_cnt:=0; 
      END;
      IF v_cnt=0 THEN
            raise_error('Template for Report :'||v_rptcode||' Not Found IN TQC_SYS_RPT_TEMPLATES!');
      END IF;
      
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file,
                rpt_prnt_srv_appl
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND (   NVL (rpt_tmpl_org_code, v_orgcode) = v_orgcode
                 OR v_orgcode IS NULL
                )
            AND NVL (rpt_tmpl_active, 'A') = 'A'
            AND rpt_code = v_rptcode;

      RETURN v_cursor;
   END;

   FUNCTION getrptdetailsschedules (v_prod_code IN NUMBER)
      RETURN report_dtls_ref
   IS
      v_cursor    report_dtls_ref;
      v_sch_rpt   VARCHAR2 (30);
   BEGIN
      BEGIN
        -- NULL;
         v_sch_rpt := gin_schedules_pkg.get_schedu_report (v_prod_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No Schedule Attached not found');
      END;

      --RAISE_ERROR(' v_sch_rpt '|| v_sch_rpt);
       OPEN v_cursor FOR
      SELECT rpt_code,
             rpt_name,
             rpt_data_file,
             rpt_tmpl_code,
             rpt_tmpl_file,
             rpt_tmpl_name,
             rpt_tmpl_style_file,
             rpt_prnt_srv_appl
        FROM tqc_system_reports, tqc_sys_rpt_templates
       WHERE rpt_tmpl_rpt_code = rpt_code
         AND UPPER(rpt_data_file) = UPPER(v_sch_rpt) || '.XML';
  
    RETURN v_cursor;
   END;

   FUNCTION getrptdetailspoldoc (v_prod_code IN NUMBER)
      RETURN report_dtls_ref
   IS
      v_cursor    report_dtls_ref;
      v_sch_rpt   VARCHAR2 (30);
   BEGIN
--  raise_error('v_prod_code'||v_prod_code);
      BEGIN
         NULL;
         v_sch_rpt := gin_schedules_pkg.get_pol_doc_report (v_prod_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No Schedule Attached not found');
      END;

      --RAISE_ERROR(' v_sch_rpt '|| v_sch_rpt);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file,
                rpt_prnt_srv_appl
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND UPPER (rpt_data_file) = UPPER (v_sch_rpt) || '.XML';

      RETURN v_cursor;
   END;

   FUNCTION getrptdetailsschedulesendors (v_prod_code IN NUMBER)
      RETURN report_dtls_ref
   IS
      v_cursor    report_dtls_ref;
      v_sch_rpt   VARCHAR2 (30);
   BEGIN
      BEGIN
         NULL;
         v_sch_rpt := gin_schedules_pkg.get_endos_sch_report (v_prod_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No Schedule Attached not found');
      END;

      --RAISE_ERROR(' v_sch_rpt '|| v_sch_rpt);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file,
                rpt_prnt_srv_appl
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND UPPER (rpt_data_file) = UPPER (v_sch_rpt) || '.XML';

      RETURN v_cursor;
   END;

   FUNCTION getdivisions (v_div_code tqc_divisions.div_code%TYPE)
      RETURN divisions_ref
   IS
      v_cursor   divisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT div_code, div_name, div_sht_desc, div_division_status,
                div_order, per_id div_director_code,
                   per_np_code
                || ' '
                || per_first_names
                || ' '
                || per_middle_name
                || ' '
                || per_last_name div_director,
                per_id div_ass_director_code,
                   per_np_code
                || ' '
                || per_first_names
                || ' '
                || per_middle_name
                || ' '
                || per_last_name div_asst_director
           FROM tqc_divisions, tq_hrms.hco_personnels
          WHERE div_director = per_id(+)
            AND div_asst_director = per_id(+)
            AND div_code = NVL (v_div_code, div_code);

      RETURN v_cursor;
   END;

   FUNCTION get_org_divisions (v_org_code tqc_organizations.org_code%TYPE)
      RETURN divisions_ref
   IS
      v_cursor   divisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT div_code, div_name, div_sht_desc, div_division_status,
                div_order, per_id div_director_code,
                   per_np_code
                || ' '
                || per_first_names
                || ' '
                || per_middle_name
                || ' '
                || per_last_name div_director,
                per_id div_ass_director_code,
                   per_np_code
                || ' '
                || per_first_names
                || ' '
                || per_middle_name
                || ' '
                || per_last_name div_asst_director
           FROM tqc_divisions, tq_hrms.hco_personnels
          WHERE div_director = per_id(+)
            AND div_asst_director = per_id(+)
            AND div_org_code = v_org_code;

      RETURN v_cursor;
   END;

   FUNCTION getsubdivisions (v_sdiv_code tqc_subdivisions.sdiv_code%TYPE)
      RETURN sub_divisions_ref
   IS
      v_cursor   sub_divisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT sdiv_code, sdiv_name, sdiv_sht_desc, sdiv_div_code
           FROM tqc_subdivisions
          WHERE sdiv_div_code = NVL (v_sdiv_code, sdiv_div_code);

      RETURN v_cursor;
   END;

   FUNCTION get_user_divisions (
      v_user_code   tqc_user_divisions.usd_usr_code%TYPE
   )
      RETURN user_divisions_ref
   IS
      v_cursor   user_divisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT usd_code, usd_usr_code, usd_div_code, div_code, div_name,
                div_sht_desc, div_division_status, usd_default
           FROM tqc_user_divisions, tqc_divisions
          WHERE usd_div_code = div_code
            AND usd_usr_code = NVL (v_user_code, usd_usr_code);

      RETURN v_cursor;
   END;

   FUNCTION get_brn_unassgnd_div (
      v_org_code        tqc_organizations.org_code%TYPE,
      v_brn_code   IN   tqc_branches.brn_code%TYPE
   )
      RETURN divisions_ref
   IS
      v_cursor   divisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT div_code, div_name, div_sht_desc, div_division_status,
                div_order, NULL div_director_code, NULL div_director,
                NULL div_ass_director_code, NULL div_asst_director
           FROM tqc_divisions
          WHERE div_org_code = v_org_code
            AND div_code NOT IN (SELECT bdiv_div_code
                                   FROM tqc_brnch_divisions
                                  WHERE bdiv_brn_code = v_brn_code);

      RETURN v_cursor;
   END;

   FUNCTION getbranchdivisions (
      v_div_code   tqc_brnch_divisions.bdiv_div_code%TYPE
   )
      RETURN branch_divisions_ref
   IS
      v_cursor   branch_divisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT bdiv_code, brn_name, div_name, bdiv_wef, bdiv_wet, div_code
           FROM tqc_brnch_divisions, tqc_branches, tqc_divisions
          WHERE bdiv_brn_code = brn_code
            AND bdiv_div_code = div_code
            AND bdiv_div_code = v_div_code;

      RETURN v_cursor;
   END;

   FUNCTION getdivisionsbybrnch (
      v_brn_code        tqc_brnch_divisions.bdiv_div_code%TYPE,
      v_usercode   IN   NUMBER DEFAULT NULL
   )
      RETURN branch_divisions_ref
   IS
      v_cursor   branch_divisions_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT bdiv_code, brn_name, div_name, bdiv_wef, bdiv_wet, div_code
           FROM tqc_brnch_divisions, tqc_branches, tqc_divisions
          WHERE bdiv_brn_code = brn_code
            AND bdiv_div_code = div_code
            AND bdiv_brn_code = v_brn_code;

      RETURN v_cursor;
   END;

   PROCEDURE grant_user_division (
      v_code        IN   tqc_user_divisions.usd_code%TYPE,
      v_user_code   IN   tqc_user_divisions.usd_usr_code%TYPE,
      v_div_code    IN   tqc_user_divisions.usd_div_code%TYPE,
      v_default     IN   tqc_user_divisions.usd_default%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_user_divisions
       WHERE usd_usr_code = v_user_code AND usd_div_code = v_div_code;

      IF (NVL (v_count, 0) = 0)
      THEN
         INSERT INTO tqc_user_divisions
                     (usd_code, usd_usr_code, usd_div_code, usd_default
                     )
              VALUES (tqc_usd_seq.NEXTVAL, v_user_code, v_div_code, v_default
                     );
      END IF;
   END grant_user_division;

   PROCEDURE revoke_user_division (
      v_user_code   IN   tqc_user_divisions.usd_usr_code%TYPE,
      v_div_code    IN   tqc_user_divisions.usd_div_code%TYPE
   )
   IS
   BEGIN
      DELETE FROM tqc_user_divisions
            WHERE usd_usr_code = v_user_code AND usd_div_code = v_div_code;
   END revoke_user_division;

   FUNCTION getrequireddocs
      RETURN req_docs_ref
   IS
      v_cursor   req_docs_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT rdoc_code, rdoc_sht_desc, roc_desc, roc_mandatory,
                roc_date_added,ROC_ACCOUNT_TYPE
           FROM tqc_required_documents;

      RETURN v_cursor;
   END;

   FUNCTION get_client_docs (
      v_clnt_code   tqc_client_documents.cdocr_clnt_code%TYPE
   )
      RETURN client_doc_ref
   IS
      v_cursor   client_doc_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT cdocr_code, cdocr_rdoc_code, cdocr_clnt_code, cdocr_submited,
                cdocr_date_s, cdocr_ref_no, cdocr_rmrk, cdocr_user_receivd,
                roc_desc,null
           FROM tqc_client_documents, tqc_required_documents
          WHERE cdocr_rdoc_code = rdoc_code AND cdocr_clnt_code = v_clnt_code;

      RETURN v_cursor;
   END;

   FUNCTION get_acc_mangrs
      RETURN acc_mngr_ref
   IS
      v_cursor   acc_mngr_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT usr_username, usr_name, usr_code
                    FROM tqc_users
                   WHERE NVL (usr_acct_mgr, 'N') = 'Y';

      RETURN v_cursor;
   END;

   FUNCTION getcurrencies
      RETURN currencies_ref
   IS
      v_cursor   currencies_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT cur_code, cur_symbol, cur_desc, cur_rnd
           FROM tqc_currencies;

      RETURN v_cursor;
   END;

   FUNCTION getsysdateformat
      RETURN VARCHAR2
   IS
      v_format   VARCHAR2 (40);
   BEGIN
      SELECT param_value
        INTO v_format
        FROM tqc_parameters
       WHERE param_name = 'SYSDATEFORMAT';

      RETURN v_format;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION getminimumdecimalplaces
      RETURN VARCHAR2
   IS
      v_value   NUMBER;
   BEGIN
      SELECT param_value
        INTO v_value
        FROM tqc_parameters
       WHERE param_name = 'SYSMINDEC';

      RETURN v_value;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION getsystemplate (v_syscode NUMBER)
      RETURN VARCHAR2
   IS
      v_value   NUMBER;
   BEGIN
      SELECT sys_template
        INTO v_value
        FROM tqc_systems
       WHERE sys_code = v_syscode;

      RETURN v_value;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION getmaximumdecimalplaces
      RETURN VARCHAR2
   IS
      v_value   NUMBER;
   BEGIN
      SELECT param_value
        INTO v_value
        FROM tqc_parameters
       WHERE param_name = 'SYSMAXDEC';

      RETURN v_value;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION getsysorgcode (v_syscode NUMBER)
      RETURN NUMBER
   IS
      v_value   NUMBER;
   BEGIN
      SELECT sys_org_code
        INTO v_value
        FROM tqc_systems
       WHERE sys_code = v_syscode;

      RETURN v_value;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION getuserbyusercode (v_usercode NUMBER)
      RETURN VARCHAR2
   IS
      v_value   VARCHAR2 (50);
   BEGIN
      SELECT usr_username
        INTO v_value
        FROM tqc_users
       WHERE usr_code = v_usercode;

      RETURN v_value;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   PROCEDURE getemailattachments (
      v_cursor   OUT      email_attachment_ref,
      v_code     IN       NUMBER
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT eatt_code, eatt_filename
           FROM tqc_email_attachments
          WHERE eatt_email_code = v_code;
   END;

   FUNCTION get_mail_from
      RETURN VARCHAR2
   IS
      v_mail_from   VARCHAR2 (100);
   BEGIN
      SELECT param_value
        INTO v_mail_from
        FROM tqc_parameters
       WHERE param_name = 'EMAILS_FROM';

      RETURN (v_mail_from);
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         tqc_error_manager.raise_unanticipated
            (text_in      => 'EMAILS_FROM parameter not defined. Contact Turnkey Africa Limited...'
            );
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                            (text_in      => 'Error getting email address from...');
   END;

   FUNCTION get_mail_host
      RETURN VARCHAR2
   IS
      v_mail_host   VARCHAR2 (100);
   BEGIN
      SELECT param_value
        INTO v_mail_host
        FROM tqc_parameters
       WHERE param_name = 'MAIL_SERVER';

      RETURN (v_mail_host);
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         tqc_error_manager.raise_unanticipated
            (text_in      => 'MAIL_SERVER parameter not defined. Contact Turnkey Africa Limited...'
            );
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                            (text_in      => 'Error getting mail server details..');
   END;

   FUNCTION get_mail_port
      RETURN VARCHAR2
   IS
      v_mail_port   VARCHAR2 (100);
   BEGIN
      SELECT param_value
        INTO v_mail_port
        FROM tqc_parameters
       WHERE param_name = 'MAIL_PORT';

      RETURN (v_mail_port);
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         tqc_error_manager.raise_unanticipated
            (text_in      => 'MAIL_PORT parameter not defined. Contact Turnkey Africa Limited...'
            );
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                              (text_in      => 'Error getting mail Port details..');
   END;

   PROCEDURE getusername (
      v_usercode   IN       tqc_users.usr_code%TYPE,
      v_username   OUT      tqc_users.usr_username%TYPE,
      v_lastdate   OUT      tqc_users.usr_last_date%TYPE
   )
   IS
   BEGIN
      SELECT usr_username, usr_last_date
        INTO v_username, v_lastdate
        FROM tqc_users
       WHERE usr_code = v_usercode;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         raise_error ('Username Not Found');
   END getusername;

   FUNCTION getallrequireddocs
      RETURN required_docs_ref
   IS
      v_cursor   required_docs_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT rdoc_code, rdoc_sht_desc, roc_desc, roc_mandatory,
                roc_date_added, roc_exempted
           FROM tqc_required_documents;

      RETURN v_cursor;
   END;

   FUNCTION get_user_email (v_code IN NUMBER)
      RETURN VARCHAR2
   IS
      v_value   VARCHAR2 (50);
   BEGIN
      SELECT usr_email
        INTO v_value
        FROM tqc_users
       WHERE usr_code = v_code;

      RETURN v_value;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN 't';
      WHEN OTHERS
      THEN
         raise_error ('Unable to get Email Details of the user ' || v_code);
   END;

   FUNCTION get_ticket_name (v_code IN NUMBER)
      RETURN VARCHAR2
   IS
      v_value   VARCHAR2 (50);
   BEGIN
      SELECT name_
        INTO v_value
        FROM tqc_bpm_tickets, jbpm4_task
       WHERE dbid_ = tqc_bpm_tickets.tckt_code(+) AND tckt_code = v_code;

      RETURN v_value;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Unable to get Email Details of the user');
   END;

   FUNCTION check_ticket_status (v_code IN VARCHAR2, v_level IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_db_id    NUMBER;
      v_return   VARCHAR2 (1);
      v_count    NUMBER;
   BEGIN
      IF v_level = 'P' OR v_level = 'E' OR v_level = 'R'
      THEN
         SELECT MAX (tckt_code)
           INTO v_db_id
           FROM tqc_bpm_tickets
          WHERE tckt_pol_code = v_code;

         SELECT COUNT (1)
           INTO v_count
           FROM jbpm4_hist_task
          WHERE dbid_ = v_db_id AND outcome_ IS NULL;
      ELSIF v_level = 'Q'
      THEN
         SELECT MAX (tckt_code)
           INTO v_db_id
           FROM tqc_bpm_tickets
          WHERE tckt_quot_code = v_code;

         SELECT COUNT (1)
           INTO v_count
           FROM jbpm4_hist_task
          WHERE dbid_ = v_db_id AND outcome_ IS NULL;
      ELSIF v_level = 'C'
      THEN
         SELECT MAX (tckt_code)
           INTO v_db_id
           FROM tqc_bpm_tickets
          WHERE tckt_clm_no = v_code;

         SELECT COUNT (1)
           INTO v_count
           FROM jbpm4_hist_task
          WHERE dbid_ = v_db_id AND outcome_ IS NULL;
      END IF;

      IF NVL (v_count, 0) > 0
      THEN
         v_return := 'Y';
      ELSE
         v_return := 'N';
      END IF;

      RETURN (v_return);
   END;

   FUNCTION products (v_syscode NUMBER, v_clatype VARCHAR2 DEFAULT NULL)
      RETURN products_ref
   IS
      v_cursor   products_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT prod_code, prod_desc, prod_sht_desc, prod_type
           FROM tqc_products_view
          WHERE sys_code = v_syscode AND cla_type = v_clatype;

      RETURN v_cursor;
   END;

   FUNCTION paymthdmode (v_type VARCHAR2)
      RETURN paymthdmode_ref
   IS
      v_cursor   paymthdmode_ref;
   BEGIN
      IF v_type = 'FREQ'
      THEN
         --PAY FREQUENCY..
         OPEN v_cursor FOR
            SELECT 'M', 'MONTHLY'
              FROM DUAL
            UNION
            SELECT 'Q', 'QUARTERLY'
              FROM DUAL
            UNION
            SELECT 'S', 'SEMI-ANNUALLY'
              FROM DUAL
            UNION
            SELECT 'A', 'ANNUALLY'
              FROM DUAL
            UNION
            SELECT 'F', 'SINGLE PREMIUM'
              FROM DUAL
            UNION
            SELECT 'D', 'DAILY'
              FROM DUAL
            UNION
            SELECT 'W', 'WEEKLY'
              FROM DUAL;

         RETURN v_cursor;
      ELSIF v_type = 'MODE'
      THEN
         --PAY MODE..
         OPEN v_cursor FOR
            SELECT pmod_sht_desc, pmod_desc
              FROM tqc_payment_modes;

         RETURN v_cursor;
      END IF;
   END;

   PROCEDURE get_report_parameters (
      v_report_params_ref   OUT   report_params_ref,
      v_rpt_code                  NUMBER,
      v_param_type                VARCHAR2,
      v_parent_code               NUMBER
   )
   IS
   BEGIN
      OPEN v_report_params_ref FOR
         SELECT   rptp_code, rptp_param_prompt, rptp_param_type,
                  rptp_parent_code, rptp_query, rptp_param_name,
                  rptp_param_clause, rptp_user_required, rptp_child_code
             FROM tqc_sys_rpt_parameters
            WHERE rptp_rpt_code = v_rpt_code
              AND rptp_param_type = NVL (v_param_type, rptp_param_type)
              AND NVL (rptp_parent_code, -1) =
                                NVL (NVL (v_parent_code, rptp_parent_code),
                                     -1)
         ORDER BY rptp_parent_code ASC NULLS FIRST, rptp_code;
   END;

   FUNCTION get_report_param_qry (v_rpt_code NUMBER, v_param_code IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_query   VARCHAR2 (2000);
   BEGIN
      v_query := '';

      BEGIN
         SELECT rptp_query
           INTO v_query
           FROM tqc_sys_rpt_parameters
          WHERE rptp_rpt_code = v_rpt_code AND rptp_param_name = v_param_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_query := '';
      END;

      RETURN v_query;
   END;

--   FUNCTION getrptdetailsschedulesren (v_prod_code IN NUMBER)
--      RETURN report_dtls_ref
--   IS
--      v_cursor    report_dtls_ref;
--      v_sch_rpt   VARCHAR2 (30);
--   BEGIN
--      BEGIN
--         NULL;
--         v_sch_rpt := gin_schedules_pkg.get_renewal_report (v_prod_code);
--      EXCEPTION
--         WHEN OTHERS
--         THEN
--            raise_error ('No Schedule Attached not found');
--      END;
--
--      OPEN v_cursor FOR
--         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
--                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file
--           FROM tqc_system_reports, tqc_sys_rpt_templates
--          WHERE rpt_tmpl_rpt_code = rpt_code
--            AND UPPER (rpt_data_file) = UPPER (v_sch_rpt) || '.XML';
--
--      RETURN v_cursor;
--   END;

FUNCTION getrptdetailsschedulesren (v_prod_code IN NUMBER)
      RETURN report_dtls_ref
   IS
      v_cursor    report_dtls_ref;
      v_sch_rpt   VARCHAR2 (30);
      v_ren_changes VARCHAR2(1);
   BEGIN

  SELECT pro_dp_ren_change
  INTO v_ren_changes
  FROM tq_gis.gin_products
 WHERE pro_code = v_prod_code;
      BEGIN
         v_sch_rpt := gin_schedules_pkg.get_renewal_report (v_prod_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No Schedule Attached not found');
      END;

--raise_error('v_sch_rpt==='||v_sch_rpt);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file,
                rpt_prnt_srv_appl
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND UPPER (rpt_data_file) = UPPER (v_sch_rpt) || '.XML';

      RETURN v_cursor;
   END;

   FUNCTION getrptdetailsendosment (v_prod_code IN NUMBER)
      RETURN report_dtls_ref
   IS
      v_cursor    report_dtls_ref;
      v_sch_rpt   VARCHAR2 (30);
   BEGIN
      --RAISE_ERROR('v_prod_code'||v_prod_code);
      BEGIN
         NULL;
         v_sch_rpt := gin_schedules_pkg.get_endos_sch_report (v_prod_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No Schedule Attached not found');
      END;

      -- RAISE_ERROR('v_sch_rpt'||v_sch_rpt);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file,
                rpt_prnt_srv_appl
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND UPPER (rpt_data_file) = UPPER (v_sch_rpt) || '.XML';

      RETURN v_cursor;
   END;

   FUNCTION getrptdetailscertificates (v_prod_code IN NUMBER)
      RETURN report_certs_ref
   IS
      v_cursor      report_certs_ref;
      v_certy_rpt   VARCHAR2 (30);
   BEGIN
      BEGIN
         NULL;
         v_certy_rpt := gin_schedules_pkg.get_cert_report (v_prod_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No Schedule Attached not found');
      END;

      --RAISE_ERROR(' v_sch_rpt '|| v_certy_rpt);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND UPPER (rpt_data_file) = UPPER (v_certy_rpt) || '.XML';

      RETURN v_cursor;
   END;

   FUNCTION getrptdetail (
      v_rptcode   tqc_system_reports.rpt_code%TYPE,
      v_orgcode   tqc_sys_rpt_templates.rpt_tmpl_org_code%TYPE DEFAULT NULL
   )
      RETURN report_dtls_ref
   IS
      v_cursor   report_dtls_ref;
   BEGIN
      --RAISE_ERROR('v_rptcode'||v_rptcode||'v_orgcode'||v_orgcode);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file,
                rpt_prnt_srv_appl
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND (   NVL (rpt_tmpl_org_code, v_orgcode) = v_orgcode
                 OR v_orgcode IS NULL
                )
            AND NVL (rpt_tmpl_active, 'A') = 'A'
            AND rpt_code = v_rptcode;

      RETURN v_cursor;
   END;

   PROCEDURE get_clients_details (
      v_cmb_claim_no    IN       VARCHAR2,
      v_email_address   OUT      VARCHAR2,
      v_message         OUT      VARCHAR2
   )
   IS
   BEGIN
      SELECT clnt_email_addrs
        INTO v_email_address
        FROM gin_claim_master_bookings, gin_policies, tqc_clients
       WHERE cmb_pol_batch_no = pol_batch_no
         AND pol_prp_code = clnt_code
         AND cmb_claim_no = v_cmb_claim_no;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_message := 'The Client Email Address is not Defined';
   END;

   PROCEDURE dmsreports (
      v_dmsreport_ref   OUT   dmsreport_ref,
      v_prodcode              NUMBER DEFAULT NULL,
      v_param_val             VARCHAR2 DEFAULT NULL,
      v_syscode               NUMBER,
      v_process               VARCHAR2
   )
   IS
      v_prodcode1   NUMBER := v_prodcode;
      v_prodcode2   NUMBER := v_prodcode;
      v_prodcode3   NUMBER := v_prodcode;
   BEGIN
      BEGIN
         IF v_param_val = 'NONPROD'
         THEN
            OPEN v_dmsreport_ref FOR
               SELECT sprr_rpt_code
                 FROM tqc_sys_process_reports,
                      tqc_system_reports,
                      tqc_sys_processes
                WHERE rpt_code = sprr_rpt_code
                  AND sprr_sprc_code = sprc_code
                  AND sprc_sht_desc = v_process
                  AND rpt_sys_code = v_syscode
                  AND rpt_name NOT IN (SELECT prod_pol_sch_rpt
                                         FROM lms_products
                                        WHERE prod_code = v_prodcode)
                  AND rpt_name NOT IN (SELECT prod_accpt_ltr_rpt
                                         FROM lms_products
                                        WHERE prod_code = v_prodcode1)
                  AND rpt_name NOT IN (SELECT prod_amortz_rpt
                                         FROM lms_products
                                        WHERE prod_code = v_prodcode2)
                  AND rpt_name NOT IN (SELECT prod_cover_letter
                                         FROM lms_products
                                        WHERE prod_code = v_prodcode3);
         ELSIF v_param_val = 'SCH'
         THEN
            OPEN v_dmsreport_ref FOR
               SELECT sprr_rpt_code
                 FROM tqc_sys_process_reports,
                      tqc_system_reports,
                      tqc_sys_processes
                WHERE rpt_code = sprr_rpt_code
                  AND sprr_sprc_code = sprc_code
                  AND sprc_sht_desc = v_process
                  AND rpt_sys_code = v_syscode
                  AND rpt_name IN (SELECT prod_pol_sch_rpt
                                     FROM lms_products
                                    WHERE prod_code = v_prodcode);
         ELSIF v_param_val = 'COV'
         THEN
            OPEN v_dmsreport_ref FOR
               SELECT sprr_rpt_code
                 FROM tqc_sys_process_reports,
                      tqc_system_reports,
                      tqc_sys_processes
                WHERE rpt_code = sprr_rpt_code
                  AND sprr_sprc_code = sprc_code
                  AND sprc_sht_desc = v_process
                  AND rpt_sys_code = v_syscode
                  AND rpt_name IN (SELECT prod_cover_letter
                                     FROM lms_products
                                    WHERE prod_code = v_prodcode);
         ELSIF v_param_val = 'AMOR'
         THEN
            OPEN v_dmsreport_ref FOR
               SELECT sprr_rpt_code
                 FROM tqc_sys_process_reports,
                      tqc_system_reports,
                      tqc_sys_processes
                WHERE rpt_code = sprr_rpt_code
                  AND sprr_sprc_code = sprc_code
                  AND sprc_sht_desc = v_process
                  AND rpt_sys_code = v_syscode
                  AND rpt_name IN (SELECT prod_amortz_rpt
                                     FROM lms_products
                                    WHERE prod_code = v_prodcode);
         ELSIF v_param_val = 'ACCP'
         THEN
            OPEN v_dmsreport_ref FOR
               SELECT sprr_rpt_code
                 FROM tqc_sys_process_reports,
                      tqc_system_reports,
                      tqc_sys_processes
                WHERE rpt_code = sprr_rpt_code
                  AND sprr_sprc_code = sprc_code
                  AND sprc_sht_desc = v_process
                  AND rpt_sys_code = v_syscode
                  AND rpt_name IN (SELECT prod_accpt_ltr_rpt
                                     FROM lms_products
                                    WHERE prod_code = v_prodcode);
         ELSE
            OPEN v_dmsreport_ref FOR
               SELECT sprr_rpt_code
                 FROM tqc_sys_process_reports,
                      tqc_system_reports,
                      tqc_sys_processes
                WHERE rpt_code = sprr_rpt_code
                  AND sprr_sprc_code = sprc_code
                  AND sprc_sht_desc = v_process
                  AND rpt_sys_code = v_syscode;
         END IF;
      END;
   END dmsreports;

   FUNCTION get_schedule_details (v_prod_code IN NUMBER)
      RETURN schedule_details_ref
   IS
      v_cursor   schedule_details_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT screen_show_default_risks, screen_no_of_risks,
                screen_show_si
           FROM gin_screens, gin_products
          WHERE pro_unwr_scr_code = screen_code AND pro_code = v_prod_code;

      RETURN v_cursor;
   END;

   FUNCTION getrptdetailsschedulesind (v_prod_code IN NUMBER)
      RETURN report_dtls_ind_ref
   IS
      v_cursor    report_dtls_ind_ref;
      v_sch_rpt   VARCHAR2 (30);
   BEGIN
      BEGIN
         NULL;
         v_sch_rpt := gin_schedules_pkg.get_schedu_report (v_prod_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No Schedule Attached not found');
      END;

      -- RAISE_ERROR(' v_sch_rpt '|| v_sch_rpt);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND UPPER (rpt_data_file) = UPPER (v_sch_rpt) || '_IND.XML';

      RETURN v_cursor;
   END;

   FUNCTION getrptdtlsendorsschind (v_prod_code IN NUMBER)
      RETURN report_dtls_ind_ref
   IS
      v_cursor    report_dtls_ind_ref;
      v_sch_rpt   VARCHAR2 (30);
   BEGIN
      BEGIN
         NULL;
         v_sch_rpt := gin_schedules_pkg.get_schedu_report (v_prod_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No Schedule Attached not found');
      END;

      -- RAISE_ERROR(' v_sch_rpt '|| v_sch_rpt);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND UPPER (rpt_data_file) = UPPER (v_sch_rpt) || '_IND_PSP.XML';

      RETURN v_cursor;
   END;

   FUNCTION getrptdtlsendorssch (v_prod_code IN NUMBER)
      RETURN report_dtls_ind_ref
   IS
      v_cursor    report_dtls_ind_ref;
      v_sch_rpt   VARCHAR2 (30);
   BEGIN
      BEGIN
         NULL;
         v_sch_rpt := gin_schedules_pkg.get_schedu_report (v_prod_code);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No Schedule Attached not found');
      END;

      -- RAISE_ERROR(' v_sch_rpt '|| v_sch_rpt);
      OPEN v_cursor FOR
         SELECT rpt_code, rpt_name, rpt_data_file, rpt_tmpl_code,
                rpt_tmpl_file, rpt_tmpl_name, rpt_tmpl_style_file
           FROM tqc_system_reports, tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = rpt_code
            AND UPPER (rpt_data_file) = UPPER (v_sch_rpt) || '_PSP.XML';

      RETURN v_cursor;
   END;
   
  FUNCTION getrptdetailsschedulesrenwl
    RETURN report_dtls_ref IS
    v_cursor  report_dtls_ref;
    v_sch_rpt VARCHAR2(30);
  BEGIN
    BEGIN
      --NULL;
      v_sch_rpt := gin_schedules_pkg.get_motor_rnwl;
    EXCEPTION
      WHEN OTHERS THEN
        raise_error('No Schedule Attached not found');
    END;
  --RAISE_ERROR('v_sch_rpt='||v_sch_rpt);
    OPEN v_cursor FOR
      SELECT rpt_code,
             rpt_name,
             rpt_data_file,
             rpt_tmpl_code,
             rpt_tmpl_file,
             rpt_tmpl_name,
             rpt_tmpl_style_file,
             rpt_prnt_srv_appl
        FROM tqc_system_reports, tqc_sys_rpt_templates
       WHERE rpt_tmpl_rpt_code = rpt_code
         AND UPPER(rpt_data_file) = UPPER(v_sch_rpt) || '.XML';
  
    RETURN v_cursor;
  END;
  FUNCTION check_rpt_details (
      v_rptcode   tqc_system_reports.rpt_code%TYPE
   )
      RETURN varchar2
   IS
      v_cnt number;
   BEGIN
      --RAISE_ERROR('v_rptcode'||v_rptcode||'v_orgcode'||v_orgcode);
      BEGIN
          SELECT count(1) into v_cnt
               FROM tqc_system_reports
              WHERE rpt_code = v_rptcode;
      EXCEPTION WHEN OTHERS THEN
            v_cnt :=0 ;
      END;
      
      IF v_cnt=0 THEN
           RETURN  ('Report with RPT_CODE:'||v_rptcode||' Not Found IN TQC_SYSTEM_REPORTS!');
      END IF;
      
      BEGIN
      SELECT count(1) into v_cnt
           FROM tqc_sys_rpt_templates
          WHERE rpt_tmpl_rpt_code = v_rptcode ;
      EXCEPTION WHEN OTHERS THEN
         v_cnt:=0; 
      END;
      IF v_cnt=0 THEN
            RETURN ('Template for Report :'||v_rptcode||' Not Found IN TQC_SYS_RPT_TEMPLATES!');
      END IF;

      RETURN 'OK';
   END;
   FUNCTION get_user_email_from (v_number IN NUMBER, v_module IN VARCHAR2)
   RETURN VARCHAR2
IS
   v_mail_from   VARCHAR2 (100);
BEGIN

   IF v_module = 'Q'
   THEN
      SELECT usr_email
        INTO v_mail_from
        FROM tqc_users, gin_quotations
       WHERE quot_authorised_by = usr_username(+) AND quot_code = v_number;
   END IF;
   RETURN v_mail_from;
END; 
FUNCTION get_salvage_user_email_from (v_username IN VARCHAR2)
   RETURN VARCHAR2
IS
   v_mail_from   VARCHAR2 (100);
BEGIN
      SELECT usr_email
        INTO v_mail_from
        FROM tqc_users
       WHERE usr_username = v_username;
   RETURN v_mail_from;
END;
FUNCTION get_agent_docs (
      v_agn_code   tqc_agency_documents.Adocr_agn_code%TYPE
   )
      RETURN client_doc_ref
   IS
      v_cursor   client_doc_ref;
   BEGIN

      OPEN v_cursor FOR
         SELECT Adocr_code, Adocr_rdoc_code, Adocr_agn_code, Adocr_submited,
                Adocr_date_s, Adocr_ref_no, Adocr_rmrk, Adocr_user_receivd,
                roc_desc,Adocr_docid
           FROM tqc_agency_documents, tqc_required_documents
          WHERE Adocr_rdoc_code = rdoc_code AND Adocr_agn_code = v_agn_code;

      RETURN v_cursor;
   END;
 FUNCTION get_sp_docs (
      v_agn_code   tqc_sp_documents.sdocr_clnt_code%TYPE
   )
      RETURN client_doc_ref
   IS
      v_cursor   client_doc_ref;
   BEGIN

      OPEN v_cursor FOR
         SELECT sdocr_code, sdocr_rdoc_code, sdocr_clnt_code, sdocr_submited,
                sdocr_date_s, sdocr_ref_no, sdocr_rmrk, sdocr_user_receivd,
                roc_desc,sdocr_docid
           FROM tqc_sp_documents, tqc_required_documents
          WHERE sdocr_rdoc_code = rdoc_code AND sdocr_clnt_code = v_agn_code;

      RETURN v_cursor;
   END;
FUNCTION getagentrequireddocs(v_roc_account_type in varchar2 default null)
      RETURN req_docs_ref
   IS
      v_cursor   req_docs_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT rdoc_code, rdoc_sht_desc, roc_desc, roc_mandatory,
                roc_date_added,roc_account_type
           FROM tqc_required_documents where ROC_ACCOUNT_TYPE =nvl( v_roc_account_type,roc_account_type);

      RETURN v_cursor;
   END;      
END; 
/