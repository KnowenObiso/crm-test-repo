 FUNCTION get_usr_tckt_dtls (
      v_user       IN   VARCHAR2,
      v_clnt_code        NUMBER DEFAULT NULL,
      v_agn_code        NUMBER DEFAULT NULL,
      v_spr_code        VARCHAR2 DEFAULT NULL,
      v_syscode            NUMBER DEFAULT NULL,
      v_cla_type        VARCHAR2 DEFAULT NULL,
      v_tkt_type VARCHAR2 DEFAULT NULL
   )
      RETURN tqc_tickets_ref
   IS
      v_refcur   tqc_tickets_ref;
   BEGIN
      --    raise_error('v_syscode = '||v_syscode
      --        ||'; v_user = '||v_user
      --        ||'; v_agn_code = '||v_agn_code
      --        ||'; v_spr_code = '||v_spr_code
      --        ||'; v_cla_type = '||v_cla_type
      --        ||'; v_clnt_code = '||v_clnt_code
      --    );
      IF v_clnt_code IS NOT NULL
      THEN
         BEGIN
            OPEN v_refcur FOR
               SELECT a.*
                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                DECODE (
                                   tqc_bpm_tickets.tckt_sys_code,
                                   37, 'GENERAL INSURANCE SYSTEM',
                                   27, 'LIFE MANAGEMENT SYSTEM',
                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                   usrsystem,
                                DECODE (tqc_bpm_tickets.tckt_sys_module,
                                        'Q', 'Quotation',
                                        'P', 'Underwriting',
                                        'E', 'Endorsements',
                                        'R', 'Renewals',
                                        'RT', 'Transfered Renewal',
                                        'C', 'Claims')
                                   sysmodule,
                                tqc_bpm_tickets.tckt_clnt_code,
                                (clnt_name || ' ' || clnt_other_names) client,
                                tqc_bpm_tickets.tckt_agn_code,
                                agn_name AGENT,
                                tqc_bpm_tickets.tckt_pol_code,
                                tqc_bpm_tickets.tckt_pol_no,
                                tqc_bpm_tickets.tckt_clm_no,
                                tqc_bpm_tickets.tckt_quot_code,
                                tqc_bpm_tickets.tckt_quo_no,
                                NVL (tqc_bpm_tickets.tckt_assigned_by,
                                     tqc_bpm_tickets.tckt_by)
                                   tckt_by,
                                tqc_bpm_tickets.tckt_date,
                                tqc_bpm_tickets.tckt_process_id,
                                tqc_bpm_tickets.tckt_sys_module
                                                              sys_module_code,
                                tckt_endr_code, tckt_prod_type, tckt_to,
                                tckt_remarks, name_ tckt_name,
                                duedate_ tckt_due_date, tckt_endorsement,
                                tckt_transno,
                                DECODE (tckt_sys_module,
                                        'Q', tckt_quo_no,
                                        'C', tckt_clm_no,
                                        tckt_pol_no
                                       ) ref_no,
                                tckt_prp_code, NULL, tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE,null tckt_assignment_date
                           FROM tqc_bpm_tickets,
                                tqc_clients,
                                tqc_agencies,
                                jbpm4_task
                          WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                            AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                            AND dbid_ = tqc_bpm_tickets.tckt_code
                            AND tckt_quot_code =
                                              NVL (v_clnt_code, tckt_quot_code)
                            AND tckt_active != 'N'
                            AND tckt_sys_code = NVL (v_syscode, tckt_sys_code)
                            AND tckt_type = 'S'
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                WHERE a.ref_no IS NOT NULL
               UNION ALL
               SELECT tqc_bpm_tickets.tckt_code,
                      DECODE (tqc_bpm_tickets.tckt_sys_code,
                              37, 'GENERAL INSURANCE SYSTEM',
                              27, 'LIFE MANAGEMENT SYSTEM',
                              26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                              1, 'FINANCIAL MANAGEMENT SYSTEM',
                              0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                         usrsystem,
                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                              'Q', 'Quotation',
                              'P', 'Underwriting',
                              'E', 'Endorsements',
                              'R', 'Renewals',
                              'RT', 'Transfered Renewal',
                              'C', 'Claims'
                             ) sysmodule,
                      tqc_bpm_tickets.tckt_clnt_code,
                      (clnt_name || ' ' || clnt_other_names) client,
                      tqc_bpm_tickets.tckt_agn_code,
                      agn_name AGENT,
                      tqc_bpm_tickets.tckt_pol_code,
                      tqc_bpm_tickets.tckt_pol_no,
                      tqc_bpm_tickets.tckt_clm_no,
                      tqc_bpm_tickets.tckt_quot_code,
                      tqc_bpm_tickets.tckt_quo_no, tqc_bpm_tickets.tckt_by,
                      tqc_bpm_tickets.tckt_date,
                      tqc_bpm_tickets.tckt_process_id,
                      tqc_bpm_tickets.tckt_sys_module sys_module_code,
                      tckt_endr_code, tckt_prod_type, tckt_to, tckt_remarks,
                      name_ tckt_name, duedate_ tckt_due_date,
                      tckt_endorsement, tckt_transno,
                      DECODE (tckt_sys_module,
                              'Q', tckt_quo_no,
                              'C', tckt_clm_no,
                              tckt_pol_no
                             ) ref_no,
                      tckt_prp_code, NULL, tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE,null tckt_assignment_date
                 FROM tqc_bpm_tickets, tqc_clients, tqc_agencies, jbpm4_task
                WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                  --AND PRP_CLNT_CODE = CLNT_CODE(+)
                  AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                  AND dbid_ = tqc_bpm_tickets.tckt_code(+)
--        AND TCKT_QUOT_CODE = NVL(v_clnt_code, TCKT_QUOT_CODE)
                  AND tckt_active != 'N'
                  AND tckt_sys_code = NVL (v_syscode, tckt_sys_code)
                  AND tckt_type = 'E';

            RETURN (v_refcur);
         END;
      ELSIF v_agn_code IS NOT NULL AND v_spr_code IS NULL
      THEN
         BEGIN
            OPEN v_refcur FOR
               SELECT a.*
                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                DECODE (
                                   tqc_bpm_tickets.tckt_sys_code,
                                   37, 'GENERAL INSURANCE SYSTEM',
                                   27, 'LIFE MANAGEMENT SYSTEM',
                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                   usrsystem,
                                DECODE (tqc_bpm_tickets.tckt_sys_module,
                                        'Q', 'Quotation',
                                        'P', 'Underwriting',
                                        'C', 'Claims')
                                   sysmodule,
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
                                tqc_bpm_tickets.tckt_sys_module
                                                              sys_module_code,
                                tckt_endr_code, tckt_prod_type, tckt_to,
                                tckt_remarks, name_ tckt_name,
                                duedate_ tckt_due_date, tckt_endorsement,
                                tckt_transno,
                                DECODE (tckt_sys_module,
                                        'Q', tckt_quo_no,
                                        'C', tckt_clm_no,
                                        tckt_pol_no
                                       ) ref_no,
                                tckt_prp_code, null pol_client_policy_number,
                                tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE,null tckt_assignment_date
                           FROM tqc_bpm_tickets,
                                tqc_clients,
                                tqc_agencies,
                                jbpm4_task
                          WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                            AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                            AND dbid_ = tqc_bpm_tickets.tckt_code
                            AND tckt_active != 'N'
                            AND tckt_sys_code = NVL (v_syscode, tckt_sys_code)
                            AND tckt_type = 'S'
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                WHERE ref_no IS NOT NULL
               UNION ALL
               SELECT tqc_bpm_tickets.tckt_code,
                      DECODE (tqc_bpm_tickets.tckt_sys_code,
                              37, 'GENERAL INSURANCE SYSTEM',
                              27, 'LIFE MANAGEMENT SYSTEM',
                              26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                              1, 'FINANCIAL MANAGEMENT SYSTEM',
                              0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                         usrsystem,
                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                              'Q', 'Quotation',
                              'P', 'Underwriting',
                              'C', 'Claims'
                             ) sysmodule,
                      tqc_bpm_tickets.tckt_clnt_code,
                      (clnt_name || ' ' || clnt_other_names) client,
                      tqc_bpm_tickets.tckt_agn_code,
                      agn_name AGENT,
                      tqc_bpm_tickets.tckt_pol_code,
                      tqc_bpm_tickets.tckt_pol_no,
                      tqc_bpm_tickets.tckt_clm_no,
                      tqc_bpm_tickets.tckt_quot_code,
                      tqc_bpm_tickets.tckt_quo_no, tqc_bpm_tickets.tckt_by,
                      tqc_bpm_tickets.tckt_date,
                      tqc_bpm_tickets.tckt_process_id,
                      tqc_bpm_tickets.tckt_sys_module sys_module_code,
                      tckt_endr_code, tckt_prod_type, tckt_to, tckt_remarks,
                      name_ tckt_name, duedate_ tckt_due_date,
                      tckt_endorsement, tckt_transno,
                      DECODE (tckt_sys_module,
                              'Q', tckt_quo_no,
                              'C', tckt_clm_no,
                              tckt_pol_no
                             ) ref_no,
                      tckt_prp_code, pol_client_policy_number, tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE,null tckt_assignment_date
                 FROM tqc_bpm_tickets,
                      tqc_clients,
                      tqc_agencies,
                      jbpm4_task,
                      gin_policies
                WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                  AND tckt_pol_code = pol_batch_no(+)
                  --AND PRP_CLNT_CODE = CLNT_CODE(+)
                  AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                  AND dbid_ = tqc_bpm_tickets.tckt_code
                  AND tckt_pol_code = NVL (v_agn_code, tckt_pol_code)
                  AND tckt_active != 'N'
                  AND tckt_sys_code = NVL (v_syscode, tckt_sys_code)
                  AND tckt_type = 'E';

            RETURN (v_refcur);
         END;
      ELSIF v_spr_code IS NOT NULL AND v_agn_code IS  NULL
      THEN
         BEGIN
            --RAISE_ERROR(v_spr_code);
            OPEN v_refcur FOR
               SELECT a.*
                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                DECODE (
                                   tqc_bpm_tickets.tckt_sys_code,
                                   37, 'GENERAL INSURANCE SYSTEM',
                                   27, 'LIFE MANAGEMENT SYSTEM',
                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                   usrsystem,
                                DECODE (tqc_bpm_tickets.tckt_sys_module,
                                        'Q', 'Quotation',
                                        'P', 'Underwriting',
                                        'C', 'Claims')
                                   sysmodule,
                                tqc_bpm_tickets.tckt_clnt_code,
                                (clnt_name || ' ' || clnt_other_names
                                ) client, tqc_bpm_tickets.tckt_agn_code,
                                agn_name AGENT, tqc_bpm_tickets.tckt_pol_code,
                                tqc_bpm_tickets.tckt_pol_no,
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
                                duedate_ tckt_due_date, tckt_endorsement,
                                tckt_transno,
                                DECODE (tckt_sys_module,
                                        'Q', tckt_quo_no,
                                        'C', tckt_clm_no,
                                        tckt_pol_no
                                       ) ref_no,
                                tckt_prp_code, NULL, tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE,null tckt_assignment_date
                           FROM tqc_bpm_tickets,
                                tqc_clients,
                                tqc_agencies,
                                jbpm4_task
                          WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                            AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                            AND dbid_ = tqc_bpm_tickets.tckt_code
                            AND tckt_clm_no = NVL (v_spr_code, tckt_clm_no)
                            AND tckt_active != 'N'
                            AND tckt_sys_code = NVL (v_syscode, tckt_sys_code)
                            AND tckt_type = 'S'
                       ORDER BY tqc_bpm_tickets.tckt_code DESC) a
                WHERE ref_no IS NOT NULL
               UNION ALL
               SELECT tqc_bpm_tickets.tckt_code,
                      DECODE (tqc_bpm_tickets.tckt_sys_code,
                              37, 'GENERAL INSURANCE SYSTEM',
                              27, 'LIFE MANAGEMENT SYSTEM',
                              26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                              1, 'FINANCIAL MANAGEMENT SYSTEM',
                              0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                         usrsystem,
                      DECODE (tqc_bpm_tickets.tckt_sys_module,
                              'Q', 'Quotation',
                              'P', 'Underwriting',
                              'C', 'Claims'
                             ) sysmodule,
                      tqc_bpm_tickets.tckt_clnt_code,
                      (clnt_name || ' ' || clnt_other_names) client,
                      tqc_bpm_tickets.tckt_agn_code,
                      agn_name AGENT,
                      tqc_bpm_tickets.tckt_pol_code,
                      tqc_bpm_tickets.tckt_pol_no,
                      tqc_bpm_tickets.tckt_clm_no,
                      tqc_bpm_tickets.tckt_quot_code,
                      tqc_bpm_tickets.tckt_quo_no, tqc_bpm_tickets.tckt_by,
                      tqc_bpm_tickets.tckt_date,
                      tqc_bpm_tickets.tckt_process_id,
                      tqc_bpm_tickets.tckt_sys_module sys_module_code,
                      tckt_endr_code, tckt_prod_type, tckt_to, tckt_remarks,
                      name_ tckt_name, duedate_ tckt_due_date,
                      tckt_endorsement, tckt_transno,
                      DECODE (tckt_sys_module,
                              'Q', tckt_quo_no,
                              'C', tckt_clm_no,
                              tckt_pol_no
                             ) ref_no,
                      tckt_prp_code, NULL, tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE,null tckt_assignment_date
                 FROM tqc_bpm_tickets, tqc_clients, tqc_agencies, jbpm4_task
                WHERE tqc_bpm_tickets.tckt_clnt_code = clnt_code(+)
                  --AND PRP_CLNT_CODE = CLNT_CODE(+)
                  AND agn_code(+) = tqc_bpm_tickets.tckt_agn_code
                  AND dbid_ = tqc_bpm_tickets.tckt_code
                  AND tckt_clm_no = NVL (v_spr_code, tckt_clm_no)
                  AND tckt_active != 'N'
                  AND tckt_sys_code = NVL (v_syscode, tckt_sys_code)
                  AND tckt_type = 'E';

            RETURN (v_refcur);
         END;
      ELSE
         BEGIN
            BEGIN
                  OPEN v_refcur FOR
                       SELECT *
                         FROM (SELECT a.*
                                 FROM (  SELECT tqc_bpm_tickets.tckt_code,
                                                DECODE (
                                                   tqc_bpm_tickets.tckt_sys_code,
                                                   37, 'GENERAL INSURANCE SYSTEM',
                                                   27, 'LIFE MANAGEMENT SYSTEM',
                                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                                   usrsystem,
                                                DECODE (
                                                   tqc_bpm_tickets.tckt_sys_module,
                                                   'Q', 'Quotation',
                                                   'P', 'Underwriting',
                                                   'C', 'Claims')
                                                   sysmodule,
                                                tqc_bpm_tickets.tckt_clnt_code,
                                                (   clnt_name
                                                 || ' '
                                                 || clnt_other_names)
                                                   client,
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
                                                tqc_bpm_tickets.tckt_sys_module
                                                              sys_module_code,
                                                tckt_endr_code,
                                                tckt_prod_type, tckt_to,
                                                tckt_remarks, name_ tckt_name,
                                                duedate_ tckt_due_date,
                                                tckt_endorsement,
                                                tckt_transno,
                                                DECODE
                                                     (tckt_sys_module,
                                                      'Q', tckt_quo_no,
                                                      'C', tckt_clm_no,
                                                      tckt_pol_no
                                                     ) ref_no,
                                                tckt_prp_code, NULL,
                                                tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE, null tckt_assignment_date
                                           FROM tqc_bpm_tickets,
                                                tqc_clients,
                                                tqc_agencies,
                                                jbpm4_task,
                                                gin_policies
                                          WHERE tqc_bpm_tickets.tckt_clnt_code =
                                                                           clnt_code(+)
                                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                            AND agn_code(+) =
                                                   tqc_bpm_tickets.tckt_agn_code
                                            AND dbid_ =
                                                     tqc_bpm_tickets.tckt_code
                                            AND tckt_pol_code = pol_batch_no
                                            and tckt_sys_module IN ('RT','RE','R','P','X','E','L')
                                            AND assignee_ = v_user
                                            --AND TCKT_QUOT_CODE = NVL(v_clnt_code, TCKT_QUOT_CODE)
                                            AND tckt_active != 'N'
                                            AND tckt_type = 'S'
                                            AND tckt_sys_code =
                                                   NVL (v_syscode,
                                                        tckt_sys_code
                                                       )
                                            UNION ALL
                                            SELECT tqc_bpm_tickets.tckt_code,
                                                DECODE (
                                                   tqc_bpm_tickets.tckt_sys_code,
                                                   37, 'GENERAL INSURANCE SYSTEM',
                                                   27, 'LIFE MANAGEMENT SYSTEM',
                                                   26, 'LIFE MANAGEMENT SYSTEM-LIFE',
                                                   1, 'FINANCIAL MANAGEMENT SYSTEM',
                                                   0, 'CUSTOMER RELATIONSHIP MANAGEMENT SYSTEM')
                                                   usrsystem,
                                                DECODE (
                                                   tqc_bpm_tickets.tckt_sys_module,
                                                   'Q', 'Quotation',
                                                   'P', 'Underwriting',
                                                   'C', 'Claims')
                                                   sysmodule,
                                                tqc_bpm_tickets.tckt_clnt_code,
                                                (   clnt_name
                                                 || ' '
                                                 || clnt_other_names)
                                                   client,
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
                                                tqc_bpm_tickets.tckt_sys_module
                                                              sys_module_code,
                                                tckt_endr_code,
                                                tckt_prod_type, tckt_to,
                                                tckt_remarks, name_ tckt_name,
                                                duedate_ tckt_due_date,
                                                tckt_endorsement,
                                                tckt_transno,
                                                DECODE
                                                     (tckt_sys_module,
                                                      'Q', tckt_quo_no,
                                                      'C', tckt_clm_no,
                                                      tckt_pol_no
                                                     ) ref_no,
                                                tckt_prp_code, NULL,
                                                tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE, null tckt_assignment_date
                                           FROM tqc_bpm_tickets,
                                                tqc_clients,
                                                tqc_agencies,
                                                jbpm4_task,
                                                lms_policies
                                          WHERE tqc_bpm_tickets.tckt_clnt_code =
                                                                           clnt_code(+)
                                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                            AND agn_code(+) =
                                                   tqc_bpm_tickets.tckt_agn_code
                                            AND dbid_ =
                                                     tqc_bpm_tickets.tckt_code
                                            AND tckt_pol_code = pol_code
                                            and tckt_sys_module IN ('RT','RE','R','P','X','E','L')
                                            AND assignee_ = v_user
                                            --AND TCKT_QUOT_CODE = NVL(v_clnt_code, TCKT_QUOT_CODE)
                                            AND tckt_active != 'N'
                                            AND tckt_type = 'S'
                                            AND tckt_sys_code =
                                                   NVL (v_syscode,
                                                        tckt_sys_code
                                                       )
                                            UNION ALL
                                            SELECT   tqc_bpm_tickets.tckt_code,
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
                                                (   clnt_name
                                                 || ' '
                                                 || clnt_other_names
                                                ) client,
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
                                                tqc_bpm_tickets.tckt_sys_module
                                                              sys_module_code,
                                                tckt_endr_code,
                                                tckt_prod_type, tckt_to,
                                                tckt_remarks, name_ tckt_name,
                                                duedate_ tckt_due_date,
                                                tckt_endorsement,
                                                tckt_transno,
                                                DECODE
                                                     (tckt_sys_module,
                                                      'Q', tckt_quo_no,
                                                      'C', tckt_clm_no,
                                                      tckt_pol_no
                                                     ) ref_no,
                                                tckt_prp_code, NULL,
                                                tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE,null tckt_assignment_date
                                           FROM tqc_bpm_tickets,
                                                tqc_clients,
                                                tqc_agencies,
                                                jbpm4_task,
                                                gin_quotations
                                          WHERE tqc_bpm_tickets.tckt_clnt_code =
                                                                           clnt_code(+)
                                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                            AND agn_code(+) =
                                                   tqc_bpm_tickets.tckt_agn_code
                                            AND dbid_ =
                                                     tqc_bpm_tickets.tckt_code
                                            AND tckt_quot_code = quot_code
                                            and tckt_sys_module IN ('Q')
                                            AND assignee_ = v_user
                                            --AND TCKT_QUOT_CODE = NVL(v_clnt_code, TCKT_QUOT_CODE)
                                            AND tckt_active != 'N'
                                            AND tckt_type = 'S'
                                            AND tckt_sys_code =
                                                   NVL (v_syscode,
                                                        tckt_sys_code
                                                       ) 
                                            UNION ALL
                                            SELECT   tqc_bpm_tickets.tckt_code,
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
                                                (   clnt_name
                                                 || ' '
                                                 || clnt_other_names
                                                ) client,
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
                                                tqc_bpm_tickets.tckt_sys_module
                                                              sys_module_code,
                                                tckt_endr_code,
                                                tckt_prod_type, tckt_to,
                                                tckt_remarks, name_ tckt_name,
                                                duedate_ tckt_due_date,
                                                tckt_endorsement,
                                                tckt_transno,
                                                DECODE
                                                     (tckt_sys_module,
                                                      'Q', tckt_quo_no,
                                                      'C', tckt_clm_no,
                                                      'A',tckt_pol_code,
                                                      tckt_pol_no
                                                     ) ref_no,
                                                tckt_prp_code, NULL,
                                                tckt_type,
                         NULL POLICY_STATUS,
                         TCKT_TRAN_EFF_DATE,
                         TCKT_GGT_NO,
                         NULL USR_TYPE, null tckt_assignment_date
                                           FROM tqc_bpm_tickets,
                                                tqc_clients,
                                                tqc_agencies,
                                                jbpm4_task,
                                                gin_adminstration_fee
                                          WHERE tqc_bpm_tickets.tckt_clnt_code =
                                                                           clnt_code(+)
                                            --AND PRP_CLNT_CODE = CLNT_CODE(+)
                                            AND agn_code(+) =
                                                   tqc_bpm_tickets.tckt_agn_code
                                            AND dbid_ =
                                                     tqc_bpm_tickets.tckt_code
                                            AND tckt_pol_code = adf_code
                                            and tckt_sys_module IN ('A')
                                            AND assignee_ = v_user
                                            --AND TCKT_QUOT_CODE = NVL(v_clnt_code, TCKT_QUOT_CODE)
                                            AND tckt_active != 'N'
                                            AND tckt_type = 'S'
                                            AND tckt_sys_code =
                                                   NVL (v_syscode,
                                                        tckt_sys_code
                                                       )  ) a
                                WHERE a.ref_no IS NOT NULL
                                ORDER BY a.tckt_code
                               )
                     ORDER BY tckt_code DESC;

                  RETURN (v_refcur);
               END;
         END;
      END IF;
   END;
    