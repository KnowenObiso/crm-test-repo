--tqc_setups_cursor.getallsmsmessages

FUNCTION getallsmsmessages (
      v_sms_sys_code     IN   NUMBER,
      v_sms_wef               DATE,
      v_sms_wet               DATE,
      v_sms_trans_type        VARCHAR2
   )
      RETURN allmessagesref
   IS
      v_cursor   allmessagesref;
   BEGIN
      --RAISE_ERROR('v_sms_sys_code'||v_sms_sys_code);
      OPEN v_cursor FOR
         SELECT DISTINCT sms_code, sms_sys_code, tsm_desc, sms_clnt_code,
                         sms_agn_code, sms_pol_no, sms_pol_code, sms_clm_no,
                         TO_CHAR (sms_tel_no), sms_msg,
                         DECODE (sms_status, 'D', 'Not Sent', 'Sent'),
                         sms_prepared_by,
                         TO_CHAR (sms_send_date, 'dd-month-yyyy HH12:MI pm'),
                         NULL pol_current_status, clnt_name, cou_code,
                         cou_zip_code,div_name,sms_prepared_date
                    FROM tqc_sms_messages,
                         tqc_clients,
                         tqc_agencies,
                         tqc_users,
                         tqc_countries,
                         tqc_system_modules,
                         tqc_brnch_divisions,
                         tqc_divisions
                   WHERE sms_clnt_code = clnt_code(+)
                     AND sms_agn_code = agn_code(+)
                     AND sms_usr_code = usr_code(+)
                     AND sms_sys_module = tsm_sht_desc(+)
                     AND clnt_bdiv_code = bdiv_code(+)
                     AND bdiv_div_code = div_code(+)
                     AND cou_code = clnt_cou_code
                     AND sms_sys_code = v_sms_sys_code
                     AND TO_DATE(sms_prepared_date,'dd-MON-rrrr') BETWEEN  TO_DATE(NVL (v_sms_wef, sms_prepared_date),'dd-MON-rrrr')
                     AND TO_DATE(NVL (v_sms_wet, sms_prepared_date),'dd-MON-rrrr')
                     --AND sms_prepared_date >=
                     --                       NVL (v_sms_wef, sms_prepared_date)
                     --AND sms_prepared_date <=
                     --                       NVL (v_sms_wet, sms_prepared_date)
                     AND (   NVL (v_sms_trans_type, 'U') = 'U'
                          OR LOWER (sms_msg) LIKE
                                DECODE (v_sms_trans_type,
                                        'AUTH', '%authoriz%',
                               'PI', '%first premium%',
                               'PA', '%authoriz%',
                               'PRA', '%deducted and allocated%',
                               'DC', '%outstanding%',
                               'CN', '%cancel%',
                               'CONV', '%accept%',
                               'LAPSE', '%lapse%',
                               'RN', '%renewed%',
                               'IF','%fail%',
                               'BM','%birthday%'
                                       )
                                        OR (v_sms_trans_type = 'BN' AND SMS_SYS_MODULE IN ('L','SURR','RREF','C','FM','PM'))
                                        OR (v_sms_trans_type= 'NTU' AND SMS_SYS_MODULE IN ('NTU'))
                         );

      RETURN (v_cursor);
   END;
   
   ----TQC_SETUPS_CURSOR.selectallMessages
   
   FUNCTION selectallmessages (
      v_sms_sys_code     VARCHAR2,
      v_sms_wef          DATE,
      v_sms_wet          DATE,
      v_sms_trans_type   VARCHAR2
   )
      RETURN allmessagesref
   IS
      v_messagesref   allmessagesref;
   BEGIN
    --raise_error('v_sms_wef = '||v_sms_wef
    --    ||'; v_sms_wet = '||v_sms_wet
    --    ||'; v_sms_trans_type = '||v_sms_trans_type
    --    ||'; v_sms_sys_code = '||v_sms_sys_code
    --    );
      OPEN v_messagesref FOR
         SELECT DISTINCT sms_code, sms_sys_code, tsm_desc, sms_clnt_code,
                         sms_agn_code, sms_pol_no, sms_pol_code, sms_clm_no,
                         sms_tel_no, sms_msg,
                         DECODE (sms_status, 'D', 'Not Sent', 'Sent'),
                         sms_prepared_by,
                         TO_CHAR (sms_send_date, 'dd-month-yyyy HH12:MI pm'),
                         NULL pol_current_status, clnt_name, cou_code,
                         cou_zip_code,div_name,sms_prepared_date
                    FROM tqc_sms_messages,
                         tqc_clients,
                         tqc_system_modules,
                         tqc_countries,
                         tqc_brnch_divisions,
                         tqc_divisions
                   WHERE
                         --SMS_POL_NO = POL_POLICY_NO
                         sms_status = 'D'
                     AND sms_sys_module = tsm_sht_desc(+)
                     AND clnt_cou_code = cou_code(+)
                     AND sms_sys_code = v_sms_sys_code
                     AND sms_clnt_code = clnt_code
                     AND clnt_bdiv_code = bdiv_code(+)
                     AND bdiv_div_code = div_code(+)
                     AND TO_DATE(sms_prepared_date,'dd-MON-rrrr') BETWEEN  TO_DATE(NVL (v_sms_wef, sms_prepared_date),'dd-MON-rrrr')
                     AND TO_DATE(NVL (v_sms_wet, sms_prepared_date),'dd-MON-rrrr')
                     --AND sms_prepared_date >=
                     --                       NVL (v_sms_wef, sms_prepared_date)
                     --AND sms_prepared_date <=
                     --                       NVL (v_sms_wet, sms_prepared_date)
                     AND (   NVL (v_sms_trans_type, 'U') = 'U'
                          OR LOWER (sms_msg) LIKE
                                DECODE (v_sms_trans_type,
                                       /* 'AUTH', '%has been authori%',
                                        'CN', '%has been cancelled%',
                                        'CONV', '%has been received%',
                                        'LAPSE', '%laps%',
                                        'RN', '%renewed%'*/
                                        'AUTH', '%authoriz%',
                               'PI', '%first premium%',
                               'PA', '%authoriz%',
                               'PRA', '%deducted and allocated%',
                               'DC', '%outstanding%',
                               'CN', '%cancel%',
                               'CONV', '%accept%',
                               'LAPSE', '%laps%',
                               'RN', '%renewed%',
                               'IF','%fail%',
                               'BM', '%birthday%'
                                       )
                                        OR (v_sms_trans_type = 'BN' AND SMS_SYS_MODULE IN ('L','SURR','RREF','C','FM','PM'))
                                        OR (v_sms_trans_type= 'NTU' AND SMS_SYS_MODULE IN ('NTU'))
                         )
                ORDER BY tsm_desc;

      RETURN v_messagesref;
   END;
   
   
   ---  TQC_SETUPS_CURSOR.getallemailmessages
   
      FUNCTION getallemailmessages (
      v_email_sys_code     IN   NUMBER,
      v_email_status       IN   VARCHAR2,
      v_email_wef               DATE,
      v_email_wet               DATE,
      v_email_trans_type        VARCHAR2
   )
      RETURN allsmsmessages_ref
   IS
      v_cursor   allsmsmessages_ref;
   BEGIN
    /*raise_error('v_email_wef = '||v_email_wef
        ||'; v_email_wet = '||v_email_wet
        ||'; v_email_trans_type = '||v_email_trans_type
        ||'; v_email_sys_code = '||v_email_sys_code
        ||'; v_email_status = '||v_email_status
        );*/
      OPEN v_cursor FOR
         SELECT email_code, email_sys_code, email_sys_module,
                email_clnt_code, email_agn_code, email_pol_code,
                email_pol_no, email_clm_no, NULL email_tel_no, email_msg,
                DECODE (email_status, 'S', 'Sent', 'Not Sent'),
                email_prepared_by, email_prepared_date, email_send_date,
                email_quot_code, NULL email_quot_no, email_usr_code,
                email_sent_response,
                clnt_name || ' ' || clnt_other_names client, agn_name,
                usr_username, email_address, email_subj
           FROM tqc_email_messages, tqc_clients, tqc_agencies, tqc_users
          WHERE email_clnt_code = clnt_code(+)
            AND email_agn_code = agn_code(+)
            AND email_usr_code = usr_code(+)
            AND email_sys_code = v_email_sys_code
            AND email_status = NVL (v_email_status, email_status)
            AND TO_DATE(email_prepared_date,'dd-MON-rrrr') BETWEEN  TO_DATE(NVL (v_email_wef, email_prepared_date),'dd-MON-rrrr')
            AND TO_DATE(NVL (v_email_wet, email_prepared_date),'dd-MON-rrrr')
            AND (   NVL (v_email_trans_type, 'U') = 'U'
                 OR LOWER (email_msg) LIKE
                       DECODE (v_email_trans_type,
                               'AUTH', '%authoriz%',
                               'PI', '%first premium%',
                               'PA', '%authoriz%',
                               'PRA', '%deducted and allocated%',
                               'DC', '%outstanding%',
                               'CN', '%cancel%',
                               'CONV', '%accept%',
                               'LAPSE', '%lapse%',
                               'RN', '%renewed%',
                               'IF','%fail%',
                               'BM','%birthday%'
                              )
                         OR (v_email_trans_type = 'BN' AND EMAIL_SYS_MODULE IN ('L','SURR','RREF','C','FM','PM'))
                         OR (v_email_trans_type= 'NTU' AND EMAIL_SYS_MODULE IN ('NTU'))
                );

      RETURN (v_cursor);
   END;