--
-- TQC_ESCALATION_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_escalation_pkg
AS
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20037,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;
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

--   PROCEDURE escalate_issues
--   IS
--      v_msgt_msg            VARCHAR2 (400);
--      v_final_msgt_msg      VARCHAR2 (200);
--      v_alrt_grp_usr_code   NUMBER;
--      v_email               VARCHAR2 (200);
--      sender                VARCHAR2 (200);
--      v_out                 NUMBER;
--      v_alrt_code           NUMBER;
--      v_param_val           NUMBER;
--      v_count               NUMBER;
--      v_user_name           VARCHAR2 (200);
--      v_name                VARCHAR2 (200); 

--          
--        CURSOR requests_lvl
--      IS
--        SELECT NULL ticket_code, NULL ticket_ref_no, NULL ticket_assignee, NULL ticket_type FROM DUAL  --
--          UNION
--        SELECT tsr_code ticket_code,tsr_ref_number ticket_ref_no,tsr_assignee ticket_assignee, 'SRQ' ticket_type
--            FROM tqc_serv_requests,tqc_escalation_levels
--          WHERE getworkingdays(NVL(tsr_reopenned_date,tsr_date),SYSDATE) >( NVL(level_duration,0) )
--          AND tsr_assignee=level_assignee
--          AND tsr_status='Open'
--          AND tsr_code NOT IN (
--                      SELECT journ_ticket_code FROM tqc_escalation_journal
--                      WHERE getworkingdays(NVL(tsr_reopenned_date,tsr_date),SYSDATE) >    ( NVL(level_duration,0)+NVL(level_duration,0) )
--                      and sre_srt_code=tsr_srt_code
--                      and tsr_status='Open'
--          );
--   BEGIN
--     

--       FOR i in requests_lvl loop
--       
--       SELECT usr_username,usr_name
--       INTO v_user_name, v_name
--       FROM tqc_users
--       where usr_code = i.sre_lvl_two_assignee;
--       
--       BEGIN
--       
--       SELECT usr_email
--       INTO v_email
--       FROM tqc_users
--       where USR_CODE=  i.sre_lvl_two_assignee;
--       EXCEPTION
--            WHEN NO_DATA_FOUND THEN
--            raise_error ( 'User  '||v_user_name||' DOES NOT HAVE AN EMAIL ADDRESS...');
--       END ;
--       
--        SELECT REPLACE(v_msgt_msg, '[ASSIGNEE]', v_name)
--        INTO v_msgt_msg
--        FROM DUAL;
--        
--        SELECT REPLACE(v_msgt_msg, '[TICKET]', i.tsr_ref_number)
--        INTO v_msgt_msg
--        FROM DUAL;
--          IF i.tsr_assignee <>  i.sre_lvl_two_assignee THEN
--          

--                SELECT level_assignee into v_next_assignee
--                 FROM tqc_escalation_levels
--                WHERE 
--                level_no = (i.level_no+1) AND level_escp_point=i.level_escalation_point;
--                
--                UPDATE tqc_serv_requests
--                SET tsr_assignee= v_next_assignee
--                WHERE tsr_code=i.tsr_code;
--               
--                 BEGIN
--                    INSERT INTO tqc_escalation_journal
--                                (journ_code, 
--                                 journ_from_level,
--                                 journ_to_level,
--                                 journ_ticket_ref_no,
--                                 journ_ticket_code, 
--                                 journ_ticket_type,
--                                 journ_from,
--                                 journ_to, 
--                                 journ_subj, 
--                                 journ_status,
--                                 journ_prepared_by, 
--                                 journ_prepared_date,
--                                 journ_send_date, 
--                                )
--                         VALUES (tqc_journ_code_seq.NEXTVAL,
--                                 i.level_no, 
--                                 i.level_no+1, 
--                                 i.ticket_ref_number,
--                                 i.ticket_code, 
--                                 i.ticket_type, 
--                                 i.ticket_assignee,
--                                 v_email, 
--                                 NULL, 
--                                 v_msgt_msg, 
--                                 'R',
--                                 /*v_user*/
--                                 NULL, 
--                                 TRUNC (SYSDATE),
--                                 TRUNC (SYSDATE), 
--                                );
--                 END;
--                
--                 BEGIN
--                    escalation_email (
--                          v_to_user      NUMBER,
--                          v_from_user    NUMBER,
--                          v_cc        NUMBER,
--                          v_ticket_no    VARCHAR2,                                --tsr_ref_number
--                          v_details      VARCHAR2,
--                          v_tsracccode   NUMBER
--                       );
--                 EXCEPTION
--                    WHEN OTHERS
--                    THEN
--                       raise_error ('Sending Mail');
--                 END;
--                 
--           END IF;   
--      
--      END LOOP;
--         
--   
--    
--   EXCEPTION
--      WHEN OTHERS
--      THEN
--         raise_error ('ERROR SENDING SERVICE REQUEST ALERTS');
--   END;

   PROCEDURE escalation_email (
      v_to_user      NUMBER,
      v_from_user    NUMBER,
      v_cc           NUMBER,
      v_ticket_no    VARCHAR2,                                --tsr_ref_number
      v_details      VARCHAR2,
      v_tsracccode   NUMBER
   )
   IS
      v_msgt_msg            VARCHAR2 (800);
      v_alrt_grp_usr_code   NUMBER;
      v_email               VARCHAR2 (200);
      sender                VARCHAR2 (200);
      v_out                 NUMBER;
      v_alrt_code           NUMBER;
      v_param_val           NUMBER;
      v_count               NUMBER;
      v_user_name           VARCHAR2 (200);
      v_to_name             VARCHAR2 (200);
      v_from_name           VARCHAR2 (200);
   BEGIN
      BEGIN
         SELECT msgt_msg
           INTO v_msgt_msg
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'ESCALATION_EMAIL';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('ESCALATION_EMAIL Message Template not Defined...');
      END;

      BEGIN
         SELECT usr_username, usr_name, usr_email
           INTO v_user_name, v_to_name, v_email
           FROM tqc_users
          WHERE usr_code = v_to_user;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error ('Unable to fetch details for User  ' || v_to_user);
      END;

      BEGIN
         SELECT  usr_name
           INTO  v_from_name
           FROM tqc_users
          WHERE usr_code = v_from_user;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error ('Unable to fetch details for User  ' || v_from_user);
      END;
      
       SELECT REPLACE (v_msgt_msg, '[TO_ASSIGNEE]', v_to_name)
        INTO v_msgt_msg
        FROM DUAL;
        
       SELECT REPLACE (v_msgt_msg, '[FROM_ASSIGNEE]', v_from_name)
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
                     (email_code, email_sys_code, email_sys_module,
                      email_clnt_code, email_agn_code, email_pol_code,
                      email_pol_no, email_clm_no, email_quot_code,
                      email_address, email_subj, email_msg, email_status,
                      email_prepared_by, email_prepared_date,
                      email_send_date, email_to_send_date
                     )
              VALUES (tqc_sms_code_seq.NEXTVAL, 1, 'REC',
                      v_tsracccode, v_tsracccode, v_alrt_code,
                      v_alrt_code, v_alrt_code, v_alrt_code,
                      v_email, 'Service Request', v_msgt_msg, 'R',
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
            raise_error ('Sending Mail: ');
      END;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('ERROR SENDING SERVICE REQUEST ALERTS');
   END escalation_email;
END tqc_escalation_pkg; 
/