 --TQC_SETUPS_CURSOR.selectallMessages

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
                         NULL pol_current_status, clnt_name||'  '||clnt_other_names clnt_name , cou_code,
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
                               'RN', '%renewed%'
                                       )
                         )
                ORDER BY tsm_desc;

      RETURN v_messagesref;
   END;