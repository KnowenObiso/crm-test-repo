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
-------------------------------------------------------------------------------------------------------------------
ALTER TABLE TQ_CRM.TQC_SERV_REQUESTS
ADD (TSR_REOPENNED_DATE DATE);
-----------------------------------------------------------------------------------------------------------------------

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
      WHEN OTHERS
      THEN
         raise_error ('ERROR SENDING SERVICE REQUEST ALERTS');
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