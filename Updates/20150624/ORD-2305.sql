--TQC_SETUPS_PKG.generatereceipts
--TQC_SMS_PKG.process_direct_debits_alerts

ALTER TABLE TQ_CRM.TQC_SMS_MESSAGES
MODIFY(SMS_AGN_CODE  NULL);
-----------------------------------------------------------------------------------------
PROCEDURE process_direct_debits_alerts(v_dd_code NUMBER, v_username VARCHAR2)
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
      v_msgt_sms           VARCHAR2 (400);
      v_alrt_grp_usr_code_sms      NUMBER;
      v_alrt_code_sms   NUMBER;
      v_clnt_sms_tel        VARCHAR2(20);
       

      CURSOR processed_receipts(v_dd_code NUMBER) 
      IS
          SELECT   ddh_code, ddh_dd_code, ddh_clnt_code, ddh_clnt_bbr_code,
                  ddh_bbr_bnk_code, ddh_clnt_sht_desc, ddh_clnt_name,
                  ddh_clnt_bank_acc_no, ddh_bbr_branch_name,
                  ddh_bbr_sht_desc, ddh_bbr_ref_code, ddh_tot_amt,
                  ddh_status, ddh_receipted, ddh_fail_date,
                  ddh_fail_updated_by, ddh_fail_update_date,
                  ddh_fail_remarks, ddh_relaunch_date,
                  ddh_relaunch_stop_date, ddh_relaunched_by,
                  ddh_relaunch_stoped_by, ddh_initial_book_date,
                  ddh_prev_ddh_code, ddh_acc_holder,
                  (SELECT bnk_bank_name || ' - '
                          || bbr_branch_name
                     FROM tqc_banks, tqc_bank_branches
                    WHERE bnk_code = bbr_bnk_code
                      AND bbr_code = ddh_clnt_bbr_code) bank_branch
             FROM tqc_direct_debit_header
            WHERE ddh_dd_code = v_dd_code
         ORDER BY ddh_clnt_name;
          
     
   BEGIN
     
          BEGIN
                 SELECT msgt_msg
                   INTO v_msgt_msg
                   FROM tqc_msg_templates
                  WHERE msgt_sht_desc = 'DIRECT_DBT_MSG';
          EXCEPTION
             WHEN OTHERS
             THEN
                raise_error ('DIRECT_DBT_MSG Message Template not Defined...');
          END;
       
        BEGIN
         SELECT alrt_grp_usr_code, alrt_code
           INTO v_alrt_grp_usr_code, v_alrt_code
           FROM tqc_alert_types
          WHERE alrt_sys_code = 0
            AND alrt_email = 'Y'
            AND alrt_type = 'DIRECT_DBT_MSG';
        EXCEPTION
             WHEN OTHERS
             THEN
                raise_error ('Direct Debit alert not attached to a user...');
        END;
        
         BEGIN
                 SELECT msgt_msg
                   INTO v_msgt_sms
                   FROM tqc_msg_templates
                  WHERE msgt_sht_desc = 'DIRECT_DBT_SMS';
          EXCEPTION
             WHEN OTHERS
             THEN
                raise_error ('DIRECT_DBT_MSG Message Template not Defined...');
          END;
       
        BEGIN
         SELECT alrt_grp_usr_code, alrt_code
           INTO v_alrt_grp_usr_code_sms, v_alrt_code_sms
           FROM tqc_alert_types
          WHERE alrt_sys_code = 0
            AND alrt_sms = 'Y'
            AND alrt_type = 'DIRECT_DBT_SMS';
        EXCEPTION
             WHEN OTHERS
             THEN
                raise_error ('Direct Debit alert not attached to a user...');
        END;
     




       FOR i in processed_receipts(v_dd_code) loop
       
       
       
       BEGIN
       
           SELECT CLNT_EMAIL_ADDRS,CLNT_SMS_TEL
           INTO v_email,v_clnt_sms_tel
           FROM TQC_CLIENTS
           where CLNT_SHT_DESC=  i.ddh_clnt_sht_desc;
       EXCEPTION
            WHEN NO_DATA_FOUND THEN
                raise_error ( 'Client  '||i.ddh_clnt_sht_desc||' DOES NOT HAVE AN EMAIL ADDRESS...');
       END ;
       
        SELECT REPLACE(v_msgt_msg, '[failremakrs]', DECODE(i.ddh_status,'F','FAILED','SUCCESS'))
        INTO v_msgt_msg
        FROM DUAL;
        
        SELECT REPLACE(v_msgt_msg, '[clientname]', I.ddh_clnt_name)
        INTO v_msgt_msg
        FROM DUAL;
        
        SELECT REPLACE(v_msgt_msg, '[failremakrs]', DECODE(i.ddh_status,'F','FAILED','SUCCESS'))
        INTO v_msgt_sms
        FROM DUAL;
        
        SELECT REPLACE(v_msgt_msg, '[clientname]', I.ddh_clnt_name)
        INTO v_msgt_sms
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
                 VALUES (tqc_sms_code_seq.NEXTVAL, 1, 'DD',
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
         
         
          INSERT INTO tqc_sms_messages
                  (sms_code, sms_sys_code, sms_sys_module,
                   sms_clnt_code,  sms_tel_no,
                   sms_msg, sms_status, sms_prepared_by, sms_prepared_date
                  )
           VALUES (TQC_SMS_CODE_SEQ.NEXTVAL, 0, 'DD',
                   i.ddh_clnt_code,v_clnt_sms_tel,
                   v_msgt_sms, 'D', v_username, SYSDATE
                  );  
       
      
      END LOOP;
         
         
    
         
         
    
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('ERROR SENDING SERVICE REQUEST ALERTS');
   END process_direct_debits_alerts;
   -----------------------------------------------------------------------------------------------------------------
   
   
    PROCEDURE generatereceipts (
      v_username       VARCHAR2,
      v_dd_code        NUMBER,
      v_dd_receipted   VARCHAR2,
      v_dd_book_date   DATE
   )
   IS
   BEGIN
      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATERECEIPTS'
            );
      ELSIF v_dd_receipted = 'Y'
      THEN
         tqc_error_manager.raise_error
            (12649,
             text_in        => 'Receipts for the selected batch have already been generated....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATERECEIPTS'
            );
      ELSIF v_dd_receipted = 'F'
      THEN
         tqc_error_manager.raise_error
            (12650,
             text_in        => 'Receipts for the selected batch have ALL been cancelled....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATERECEIPTS'
            );
      ELSIF direct_debit_pkg.get_business_date (v_dd_book_date, 4) > SYSDATE
      THEN
         tqc_error_manager.raise_error
            (12651,
             text_in        =>    'This batch can ONLY be receipted after '
                               || TO_CHAR
                                     (direct_debit_pkg.get_business_date
                                                              (v_dd_book_date,
                                                               4
                                                              ),
                                      'DD/MM/RRRR'
                                     )
                               || '....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATERECEIPTS'
            );
      ELSE
         direct_debit_pkg.generate_receipts (v_dd_code, v_username);
         tqc_sms_pkg.process_direct_debits_alerts(v_dd_code, v_username);
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                                  (text_in        =>    'Error Generating Receipt '
                                                     || SQLERRM (SQLCODE),
                                   name1_in       => 'MODULE NAME',
                                   value1_in      => 'GENERATERECEIPTS'
                                  );
   END generatereceipts;