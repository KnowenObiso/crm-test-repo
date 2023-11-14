ALTER TABLE TQ_CRM.TQC_CURRENCY_RATES
ADD (CRT_CREATED_BY VARCHAR2(30 BYTE));

ALTER TABLE TQ_CRM.TQC_CURRENCY_RATES
ADD (CRT_CREATED_DATE DATE);

ALTER TABLE TQ_CRM.TQC_CURRENCY_RATES
ADD (CRT_UPDATED_BY VARCHAR2(30 BYTE));

ALTER TABLE TQ_CRM.TQC_CURRENCY_RATES
ADD (CRT_UPDATED_DATE DATE);

  --tqc_setups_pkg.currency_rates_prc
    PROCEDURE currency_rates_prc (
      v_add_edit            IN   VARCHAR2,
      v_crt_code            IN   tqc_currency_rates.crt_code%TYPE,
      v_crt_cur_code        IN   tqc_currency_rates.crt_cur_code%TYPE,
      v_crt_rate            IN   tqc_currency_rates.crt_rate%TYPE,
      v_crt_date            IN   VARCHAR2,
      v_crt_base_cur_code   IN   tqc_currency_rates.crt_base_cur_code%TYPE,
      v_crt_wef             IN  VARCHAR2,
      v_crt_wet             IN  VARCHAR2,
      v_user_code          IN NUMBER DEFAULT NULL
   );
   
   
   --tqc_setups_pkg.currency_rates_prc
    PROCEDURE currency_rates_prc (
      v_add_edit            IN   VARCHAR2,
      v_crt_code            IN   tqc_currency_rates.crt_code%TYPE,
      v_crt_cur_code        IN   tqc_currency_rates.crt_cur_code%TYPE,
      v_crt_rate            IN   tqc_currency_rates.crt_rate%TYPE,
      v_crt_date            IN   VARCHAR2,
      v_crt_base_cur_code   IN   tqc_currency_rates.crt_base_cur_code%TYPE,
      v_crt_wef             IN  VARCHAR2,
      v_crt_wet             IN  VARCHAR2,
      v_user_code          IN NUMBER DEFAULT NULL
   )
   IS
      CURSOR currref(v_crt_wef_dt DATE)
      IS
         SELECT crt_code, crt_cur_code, crt_rate, crt_date,
                crt_base_cur_code, crt_wef, crt_wet
           FROM tqc_currency_rates
          WHERE crt_cur_code = v_crt_cur_code AND crt_wet > v_crt_wef_dt
          AND crt_code != v_crt_code;

      v_count   NUMBER;
      v_crt_date_dt          DATE;
      v_crt_wef_dt          DATE;
      v_crt_wet_dt          DATE;
   BEGIN
   
             v_crt_date_dt := to_date(v_crt_wet,'DD/MON/RRRR HH24:MI:SS');
             v_crt_wef_dt := to_date(v_crt_wef,'DD/MON/RRRR HH24:MI:SS');
             v_crt_wet_dt := to_date(v_crt_wet,'DD/MON/RRRR HH24:MI:SS');
      IF v_add_edit = 'A'
      THEN
         BEGIN
           -- raise_error ('v_CRT_WEF ' || v_crt_wef);

            SELECT COUNT (1)
              INTO v_count
              FROM tqc_currency_rates
             WHERE crt_code = v_crt_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

         
            
            
             
                IF trunc(v_crt_wef_dt) < trunc(SYSDATE)
            THEN
               raise_error ('Wef cannot be less than today...');
            END IF;
            
            
            FOR i IN currref(v_crt_wef_dt)
            LOOP
               UPDATE tqc_currency_rates
                  SET crt_wet = SYSDATE
                WHERE crt_code = i.crt_code;
            END LOOP;

            --raise_error('Currency Rate Processing Successful '||v_count);
            INSERT INTO tqc_currency_rates
                        (crt_code, crt_cur_code,
                         crt_rate, crt_date, crt_base_cur_code,
                         crt_wef, crt_wet,crt_created_date,crt_created_by
                        )
                 VALUES (tqc_crt_code_seq.NEXTVAL, v_crt_cur_code,
                         v_crt_rate, v_crt_date_dt, v_crt_base_cur_code,
                         v_crt_wef_dt, v_crt_wet_dt,SYSDATE,v_user_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            FOR i IN currref(v_crt_wef_dt)
            LOOP
               IF v_crt_wef BETWEEN i.crt_wef AND i.crt_wet
               THEN
                  raise_error
                     ('Your Currency Period Overlaps with another period.Please check'
                     );
               END IF;

               IF v_crt_wet BETWEEN i.crt_wef AND i.crt_wet
               THEN
                  raise_error
                     ('Your Currency Period Overlaps with another period.Please check'
                     );
               END IF;
            END LOOP;

            UPDATE tqc_currency_rates
               SET crt_cur_code = v_crt_cur_code,
                   crt_rate = v_crt_rate,
                   crt_date = v_crt_date_dt,
                   crt_base_cur_code = v_crt_base_cur_code,
                   crt_wef = v_crt_wef_dt,
                   crt_wet = v_crt_wet_dt,
                   crt_updated_date= SYSDATE,
                   crt_updated_by=v_user_code
             WHERE crt_code = v_crt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while updating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_currency_rates
                  WHERE crt_code = v_crt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   --raise_error('Currency Rate Processing Successful');
   END;
   -=------------------------------------------------------------------
   ALTER TABLE TQ_CRM.TQC_AGENCIES
ADD (agn_payment_freq VARCHAR2(5 BYTE));


ALTER TABLE TQ_CRM.TQC_AGENCIES
ADD (agn_default_cpm_mode VARCHAR2(20 BYTE));

-----------------------------------------------------------------------------

 PROCEDURE agencies_prc (
      v_add_edit                  IN   VARCHAR2,
      v_agn_code                  IN   tqc_agencies.agn_code%TYPE,
      v_agn_act_code              IN   tqc_agencies.agn_act_code%TYPE,
      v_agn_sht_desc              IN   tqc_agencies.agn_sht_desc%TYPE,
      v_agn_name                  IN   tqc_agencies.agn_name%TYPE,
      v_agn_physical_address      IN   tqc_agencies.agn_physical_address%TYPE,
      v_agn_postal_address        IN   tqc_agencies.agn_postal_address%TYPE,
      v_agn_twn_code              IN   tqc_agencies.agn_twn_code%TYPE,
      v_agn_cou_code              IN   tqc_agencies.agn_cou_code%TYPE,
      v_agn_email_address         IN   tqc_agencies.agn_email_address%TYPE,
      v_agn_web_address           IN   tqc_agencies.agn_web_address%TYPE,
      v_agn_zip                   IN   tqc_agencies.agn_zip%TYPE,
      v_agn_contact_person        IN   tqc_agencies.agn_contact_person%TYPE,
      v_agn_contact_title         IN   tqc_agencies.agn_contact_title%TYPE,
      v_agn_tel1                  IN   tqc_agencies.agn_tel1%TYPE,
      v_agn_tel2                  IN   tqc_agencies.agn_tel2%TYPE,
      v_agn_fax                   IN   tqc_agencies.agn_fax%TYPE,
      v_agn_acc_no                IN   tqc_agencies.agn_acc_no%TYPE,
      v_agn_pin                   IN   tqc_agencies.agn_pin%TYPE,
      v_agn_agent_commission      IN   tqc_agencies.agn_agent_commission%TYPE,
      v_agn_credit_allowed        IN   tqc_agencies.agn_credit_allowed%TYPE,
      v_agn_agent_wht_tax         IN   tqc_agencies.agn_agent_wht_tax%TYPE,
      v_agn_print_dbnote          IN   tqc_agencies.agn_print_dbnote%TYPE,
      v_agn_status                IN   tqc_agencies.agn_status%TYPE,
      v_agn_date_created          IN   tqc_agencies.agn_date_created%TYPE,
      v_agn_created_by            IN   tqc_agencies.agn_created_by%TYPE,
      v_agn_reg_code              IN   tqc_agencies.agn_reg_code%TYPE,
      v_agn_comm_reserve_rate     IN   tqc_agencies.agn_comm_reserve_rate%TYPE,
      v_agn_annual_budget         IN   tqc_agencies.agn_annual_budget%TYPE,
      v_agn_status_eff_date       IN   tqc_agencies.agn_status_eff_date%TYPE,
      v_agn_credit_period         IN   tqc_agencies.agn_credit_period%TYPE,
      v_agn_comm_stat_eff_dt      IN   tqc_agencies.agn_comm_stat_eff_dt%TYPE,
      v_agn_comm_status_dt        IN   tqc_agencies.agn_comm_status_dt%TYPE,
      v_agn_comm_allowed          IN   tqc_agencies.agn_comm_allowed%TYPE,
      v_agn_checked               IN   tqc_agencies.agn_checked%TYPE,
      v_agn_checked_by            IN   tqc_agencies.agn_checked_by%TYPE,
      v_agn_check_date            IN   tqc_agencies.agn_check_date%TYPE,
      v_agn_comp_comm_arrears     IN   tqc_agencies.agn_comp_comm_arrears%TYPE,
      v_agn_reinsurer             IN   tqc_agencies.agn_reinsurer%TYPE,
      v_agn_brn_code              IN   tqc_agencies.agn_brn_code%TYPE,
      v_agn_town                  IN   tqc_agencies.agn_town%TYPE,
      v_agn_country               IN   tqc_agencies.agn_country%TYPE,
      v_agn_status_desc           IN   tqc_agencies.agn_status_desc%TYPE,
      v_agn_id_no                 IN   tqc_agencies.agn_id_no%TYPE,
      v_agn_con_code              IN   tqc_agencies.agn_con_code%TYPE,
      v_agn_agn_code              IN   tqc_agencies.agn_agn_code%TYPE,
      v_agn_sms_tel               IN   tqc_agencies.agn_sms_tel%TYPE,
      v_agn_ahc_code              IN   tqc_agencies.agn_ahc_code%TYPE,
      v_agn_sec_code              IN   tqc_agencies.agn_sec_code%TYPE,
      v_agn_agnc_class_code       IN   tqc_agencies.agn_agnc_class_code%TYPE,
      v_agn_expiry_date           IN   tqc_agencies.agn_expiry_date%TYPE,
      v_agn_license_no            IN   tqc_agencies.agn_license_no%TYPE,
      v_agn_runoff                IN   tqc_agencies.agn_runoff%TYPE,
      v_agn_licensed              IN   tqc_agencies.agn_licensed%TYPE,
      v_agn_license_grace_pr      IN   tqc_agencies.agn_license_grace_pr%TYPE,
      v_agn_old_acc_no            IN   tqc_agencies.agn_old_acc_no%TYPE,
      v_agn_status_remarks        IN   tqc_agencies.agn_status_remarks%TYPE,
      v_agn_bbr_code              IN   tqc_agencies.agn_bbr_code%TYPE
            DEFAULT NULL,
      v_agn_bank_acc_no           IN   tqc_agencies.agn_bank_acc_no%TYPE
            DEFAULT NULL,
      v_agn_unique_prefix         IN   tqc_agencies.agn_unique_prefix%TYPE
            DEFAULT NULL,
      v_agn_state_code            IN   tqc_agencies.agn_state_code%TYPE
            DEFAULT NULL,
      v_agn_crdt_rting            IN   tqc_agencies.agn_crdt_rting%TYPE
            DEFAULT NULL,
      v_agn_subagent              IN   tqc_agencies.agn_subagent%TYPE
            DEFAULT NULL,
      v_agn_main_agn_code         IN   tqc_agencies.agn_main_agn_code%TYPE
            DEFAULT NULL,
      v_agn_clnt_code             IN   tqc_agencies.agn_clnt_code%TYPE
            DEFAULT NULL,
      v_agn_account_manager       IN   tqc_agencies.agn_account_manager%TYPE
            DEFAULT NULL,
      v_agn_credit_limit          IN   tqc_agencies.agn_credit_limit%TYPE
            DEFAULT NULL,
      v_agn_bru_code              IN   tqc_agencies.agn_bru_code%TYPE
            DEFAULT NULL,
      v_agn_local_international   IN   tqc_agencies.agn_local_international%TYPE
            DEFAULT NULL,
      v_agn_regulator_number      IN   tqc_agencies.agn_regulator_number%TYPE
            DEFAULT NULL,
      v_agn_rorg_code             IN   tqc_agencies.agn_rorg_code%TYPE
            DEFAULT NULL,
      v_agn_ors_code              IN   tqc_agencies.agn_ors_code%TYPE
            DEFAULT NULL,
      v_agn_allocate_cert         IN   tqc_agencies.agn_allocate_cert%TYPE
            DEFAULT NULL,
      v_agn_bounced_chq           IN   tqc_agencies.agn_bounced_chq%TYPE
            DEFAULT NULL,
      v_def_comm_mode             IN   tqc_agencies.agn_default_comm_mode%TYPE
            DEFAULT NULL,
      v_agn_bpn_code              IN   tqc_agencies.agn_bpn_code%TYPE
            DEFAULT NULL,
      v_agn_agent_type            IN   tqc_agencies.agn_agent_type%TYPE
            DEFAULT NULL,
      v_agn_group                 IN   tqc_agencies.agn_group%TYPE
            DEFAULT NULL,
      v_vat_appl                  IN   tqc_agencies.agn_vat_applicable%TYPE,
      v_withtaxappl               IN   tqc_agencies.agn_whtax_applicable%TYPE,
      v_agn_tel_pay               IN   tqc_agencies.agn_tel_pay%TYPE,
      v_agnResetCode              IN   VARCHAR2,
       v_agn_payment_freq     IN   tqc_agencies.agn_payment_freq%TYPE  
            DEFAULT NULL,
       v_agn_default_cpm_mode  IN   tqc_agencies.agn_default_cpm_mode%TYPE
            DEFAULT NULL
   )
   IS
      v_direct_srl_fmt    VARCHAR2 (50);
      v_agent_id          VARCHAR2 (50);
      v_agent_seq         NUMBER;
      v_name_first_char   VARCHAR2 (20);
      v_no_of_chars       NUMBER                           := 1;
      v_serial_length     NUMBER                           := 6;
      v_act_type          VARCHAR2 (2);
      v_count             NUMBER                           := 0;
      v_created_by        VARCHAR2 (20);
      v_date_created      DATE;
      v_agn_short_desc    VARCHAR2 (200);
      v_act_desc          VARCHAR2 (75);
      v_act_wthtx_rate    NUMBER;
      v_brn_sht_desc      tqc_branches.brn_sht_desc%TYPE;
      v_param_value       VARCHAR2 (30);
      v_decodesmstel      VARCHAR2 (50);
      v_rorg_code         NUMBER;
      v_ors_code          NUMBER;
      v_accCount          NUMBER;
   BEGIN
      --RAISE_ERROR('fdfdf'||v_agn_main_agn_code);
         --  RAISE_ERROR('v_agn_allocate_cert'||v_agn_allocate_cert||'v_agn_agn_code'||v_agn_agn_code);
         --RAISE_ERROR('v_agn_payment_freq '||v_agn_payment_freq||' v_agn_default_cpm_mode '||v_agn_default_cpm_mode);
      IF v_agn_sms_tel IS NOT NULL
      THEN
         BEGIN
            SELECT '+' || cou_zip_code || v_agn_sms_tel
              INTO v_decodesmstel
              FROM tqc_countries
             WHERE cou_code = v_agn_cou_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               NULL;
         END;
      END IF;

      BEGIN
         SELECT RTRIM (LTRIM (act_id_serial_format)), act_type_sht_desc,
                act_account_type, act_wthtx_rate
           INTO v_direct_srl_fmt, v_act_type,
                v_act_desc, v_act_wthtx_rate
           FROM tqc_account_types
          WHERE act_code = v_agn_act_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error getting Id format for account type ');
      END;

      IF v_add_edit = 'A'
      THEN
         IF v_agn_name IS NULL
         THEN
            raise_error ('Agent Name cannot be null');
         END IF;

         v_created_by := v_agn_created_by;
         v_date_created := TRUNC (SYSDATE);
         v_agn_short_desc := v_agn_sht_desc;

         IF v_agn_short_desc IS NULL
         THEN
            BEGIN
               SELECT tqc_parameters_pkg.get_param_varchar
                                                       ('AGN_ID_SERIAL_LENGTH')
                 INTO v_serial_length
                 FROM DUAL;
            EXCEPTION
               WHEN OTHERS
               THEN
                  NULL;
            END;

            BEGIN
               SELECT param_value
                 INTO v_no_of_chars
                 FROM tqc_parameters
                WHERE param_name = 'AGN_CHARS';
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_no_of_chars := 1;
            END;

            IF v_direct_srl_fmt IS NULL
            THEN
               raise_error (   'Provide Id Format for  account type '
                            || v_act_desc
                            || '  OR use the button Account id to define the Id'
                           );
            END IF;

            BEGIN
               SELECT SUBSTR (LTRIM (v_agn_name), 1, v_no_of_chars)
                 INTO v_name_first_char
                 FROM DUAL;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error getting Agent short description ');
            END;

            BEGIN
               IF v_act_type IN ('A', 'IA')
               THEN
                  SELECT agent_id_a_seq.NEXTVAL
                    INTO v_agent_seq
                    FROM DUAL;
               ELSIF v_act_type = 'B'
               THEN
                  SELECT agent_id_b_seq.NEXTVAL
                    INTO v_agent_seq
                    FROM DUAL;
               ELSIF v_act_type = 'D'
               THEN
                  SELECT agent_id_d_seq.NEXTVAL
                    INTO v_agent_seq
                    FROM DUAL;
               ELSIF v_act_type = 'I'
               THEN
                  SELECT agent_id_i_seq.NEXTVAL
                    INTO v_agent_seq
                    FROM DUAL;
               ELSIF v_act_type = 'R'
               THEN
                  SELECT agent_id_r_seq.NEXTVAL
                    INTO v_agent_seq
                    FROM DUAL;
               ELSE
                  SELECT agent_id_seq.NEXTVAL
                    INTO v_agent_seq
                    FROM DUAL;
               END IF;

               SELECT brn_sht_desc
                 INTO v_brn_sht_desc
                 FROM tqc_branches
                WHERE brn_code = v_agn_brn_code;

               v_agent_id :=
                  REPLACE (v_direct_srl_fmt,
                           '[SERIALNO]',
                           LPAD (v_agent_seq, v_serial_length, 0)
                          );
               v_agent_id :=
                           REPLACE (v_agent_id, '[FNAMEI]', v_name_first_char);
               v_agent_id := REPLACE (v_agent_id, '[BRN]', v_brn_sht_desc);

               BEGIN
                  SELECT COUNT (1)
                    INTO v_count
                    FROM tqc_agencies
                   WHERE agn_sht_desc = v_agent_id;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('Error Checking duplicate Agent id');
               END;

               IF NVL (v_count, 0) <> 0
               THEN
                  raise_error ('An Agent with the same id exist');
               END IF;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error fetching the sequence...');
            END;

            v_agn_short_desc := v_agent_id;
         END IF;

         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_agencies
             WHERE agn_code = v_agn_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_agencies
                        (agn_code, agn_act_code,
                         agn_sht_desc, agn_name,
                         agn_physical_address, agn_postal_address,
                         agn_twn_code, agn_cou_code, agn_email_address,
                         agn_web_address, agn_zip, agn_contact_person,
                         agn_contact_title, agn_tel1, agn_tel2,
                         agn_fax, agn_acc_no, agn_pin,
                         agn_agent_commission, agn_credit_allowed,
                         agn_agent_wht_tax, agn_print_dbnote,
                         agn_status, agn_date_created, agn_created_by,
                         agn_reg_code, agn_comm_reserve_rate,
                         agn_annual_budget, agn_status_eff_date,
                         agn_credit_period, agn_comm_stat_eff_dt,
                         agn_comm_status_dt, agn_comm_allowed,
                         agn_checked, agn_checked_by, agn_check_date,
                         agn_comp_comm_arrears, agn_reinsurer,
                         agn_brn_code, agn_town, agn_country,
                         agn_status_desc, agn_id_no, agn_con_code,
                         agn_agn_code, agn_sms_tel, agn_ahc_code,
                         agn_sec_code, agn_agnc_class_code,
                         agn_expiry_date, agn_license_no, agn_runoff,
                         agn_licensed, agn_license_grace_pr,
                         agn_old_acc_no, agn_status_remarks,
                         agn_bbr_code, agn_bank_acc_no,
                         agn_unique_prefix, agn_state_code,
                         agn_crdt_rting, agn_subagent,
                         agn_main_agn_code, agn_clnt_code,
                         agn_account_manager, agn_credit_limit,
                         agn_bru_code, agn_local_international,
                         agn_regulator_number, agn_rorg_code,
                         agn_ors_code, agn_allocate_cert,
                         agn_bounced_chq, agn_bpn_code, agn_agent_type,
                         agn_group, agn_default_comm_mode,
                         agn_vat_applicable, agn_whtax_applicable,agn_tel_pay,
                         agn_payment_freq,agn_default_cpm_mode
                        )
                 VALUES (tqc_agn_code_seq.NEXTVAL, v_agn_act_code,
                         v_agn_short_desc, v_agn_name,
                         v_agn_physical_address, v_agn_postal_address,
                         v_agn_twn_code, v_agn_cou_code, v_agn_email_address,
                         v_agn_web_address, v_agn_zip, v_agn_contact_person,
                         v_agn_contact_title, v_agn_tel1, v_agn_tel2,
                         v_agn_fax, v_agn_acc_no, v_agn_pin,
                         v_agn_agent_commission, v_agn_credit_allowed,
                         v_agn_agent_wht_tax, v_agn_print_dbnote,
                         v_agn_status, v_agn_date_created, v_agn_created_by,
                         v_agn_reg_code, v_agn_comm_reserve_rate,
                         v_agn_annual_budget, v_agn_status_eff_date,
                         v_agn_credit_period, v_agn_comm_stat_eff_dt,
                         v_agn_comm_status_dt, v_agn_comm_allowed,
                         v_agn_checked, v_agn_checked_by, v_agn_check_date,
                         v_agn_comp_comm_arrears, v_agn_reinsurer,
                         v_agn_brn_code, v_agn_town, v_agn_country,
                         v_agn_status_desc, v_agn_id_no, v_agn_con_code,
                         v_agn_agn_code, v_decodesmstel, v_agn_ahc_code,
                         v_agn_sec_code, v_agn_agnc_class_code,
                         v_agn_expiry_date, v_agn_license_no, v_agn_runoff,
                         v_agn_licensed, v_agn_license_grace_pr,
                         v_agn_old_acc_no, v_agn_status_remarks,
                         v_agn_bbr_code, v_agn_bank_acc_no,
                         v_agn_unique_prefix, v_agn_state_code,
                         v_agn_crdt_rting, v_agn_subagent,
                         v_agn_main_agn_code, v_agn_clnt_code,
                         v_agn_account_manager, v_agn_credit_limit,
                         v_agn_bru_code, v_agn_local_international,
                         v_agn_regulator_number, v_agn_rorg_code,
                         v_agn_ors_code, v_agn_allocate_cert,
                         v_agn_bounced_chq, v_agn_bpn_code, v_agn_agent_type,
                         v_agn_group, v_def_comm_mode,
                         v_vat_appl, v_withtaxappl,v_agn_tel_pay,
                          v_agn_payment_freq,v_agn_default_cpm_mode
                        );

--IF v_agn_act_code IN (2,25)
--THEN
            BEGIN
               SELECT param_value
                 INTO v_param_value
                 FROM tqc_parameters
                WHERE param_name = 'AUTO_ASSIGN_AGN_SYS';
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  v_param_value := 'N';
               WHEN OTHERS
               THEN
                  v_param_value := 'N';
            END;

            BEGIN
               IF v_param_value = 'Y'
               THEN
                  INSERT INTO tqc_agency_systems
                              (asys_sys_code, asys_agn_code, asys_wef,
                               asys_wet, asys_comment
                              )
                       VALUES (37, tqc_agn_code_seq.CURRVAL, SYSDATE,
                               NULL, NULL
                              );
               END IF;
            EXCEPTION
               WHEN OTHERS
               THEN
                  NULL;
            END;
            
            BEGIN
            INSERT INTO tqc_account_contacts
                        (accc_code,
                         accc_acc_code,
                         accc_name,
                         accc_other_names,
                         accc_tel,
                         accc_email_addr,
                         accc_sms_tel,
                         accc_username,
                         accc_pwd,
                         accc_login_allowed,
                         accc_pwd_changed, accc_pwd_reset,
                         accc_dt_created, accc_status,
                         accc_login_attempts,
                         accc_personel_rank,
                         accc_last_login_date,
                         accc_created_by,
                         accc_sys_code
                        )
                 VALUES (tqc_accc_code_seq.NEXTVAL,
                         tqc_agn_code_seq.CURRVAL,
                         v_agn_short_desc,
                         v_agn_name,
                         v_agn_tel1,
                         v_agn_email_address,
                         v_decodesmstel,
                         v_agn_short_desc,
                         /*v_account_contacts_tab(I).ACCC_PWD*/
                         GIS_READ.VAL('123456'),
                         'Y',
                         SYSDATE, 'Y',
                         SYSDATE, 'A',
                         0,
                         'AGENCY',
                         SYSDATE,
                         'SYSTEM',
                         NULL
                        );
            EXCEPTION
               WHEN OTHERS
               THEN
                  NULL;
            END;
         -- END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'E'
      THEN
      
         
         
         
         BEGIN
            BEGIN
               SELECT agn_rorg_code, agn_ors_code
                 INTO v_rorg_code, v_ors_code
                 FROM tqc_agencies
                WHERE agn_code = v_agn_code;
            END;

            IF v_rorg_code != v_agn_rorg_code OR v_ors_code != v_agn_ors_code
            THEN
               INSERT INTO tqc_agency_ratings_logs
                           (arl_code, arl_rorg_code,
                            arl_ors_code
                           )
                    VALUES (tqc_arl_code_seq.NEXTVAL, v_agn_rorg_code,
                            v_agn_ors_code
                           );
            END IF;

            UPDATE tqc_agencies
               SET agn_act_code = v_agn_act_code,
                   agn_sht_desc = v_agn_sht_desc,
                   agn_name = v_agn_name,
                   agn_physical_address = v_agn_physical_address,
                   agn_postal_address = v_agn_postal_address,
                   agn_twn_code = v_agn_twn_code,
                   agn_cou_code = v_agn_cou_code,
                   agn_email_address = v_agn_email_address,
                   agn_web_address = v_agn_web_address,
                   agn_zip = v_agn_zip,
                   agn_contact_person = v_agn_contact_person,
                   agn_contact_title = v_agn_contact_title,
                   agn_tel1 = v_agn_tel1,
                   agn_tel2 = v_agn_tel2,
                   agn_fax = v_agn_fax,
                   agn_acc_no = v_agn_acc_no,
                   agn_pin = v_agn_pin,
                   agn_agent_commission = v_agn_agent_commission,
                   agn_credit_allowed = v_agn_credit_allowed,
                   agn_agent_wht_tax = v_agn_agent_wht_tax,
                   agn_print_dbnote = v_agn_print_dbnote,
                   agn_status = v_agn_status,
                    --  agn_date_created = v_agn_date_created,
                   ---   agn_created_by = v_agn_created_by,
                   agn_reg_code = v_agn_reg_code,
                   agn_comm_reserve_rate = v_agn_comm_reserve_rate,
                   agn_annual_budget = v_agn_annual_budget,
                   agn_status_eff_date = v_agn_status_eff_date,
                   agn_credit_period = v_agn_credit_period,
                   agn_comm_stat_eff_dt = v_agn_comm_stat_eff_dt,
                   agn_comm_status_dt = v_agn_comm_status_dt,
                   agn_comm_allowed = v_agn_comm_allowed,
                   agn_checked = v_agn_checked,
                   agn_checked_by = v_agn_checked_by,
                   agn_check_date = v_agn_check_date,
                   agn_comp_comm_arrears = v_agn_comp_comm_arrears,
                   agn_reinsurer = v_agn_reinsurer,
                   agn_brn_code = v_agn_brn_code,
                   agn_town = v_agn_town,
                   agn_country = v_agn_country,
                   agn_status_desc = v_agn_status_desc,
                   agn_id_no = v_agn_id_no,
                   agn_con_code = v_agn_con_code,
                   agn_agn_code = v_agn_agn_code,
                   agn_sms_tel = v_decodesmstel,
                   agn_ahc_code = v_agn_ahc_code,
                   agn_sec_code = v_agn_sec_code,
                   agn_agnc_class_code = v_agn_agnc_class_code,
                   agn_expiry_date = v_agn_expiry_date,
                   agn_license_no = v_agn_license_no,
                   agn_runoff = v_agn_runoff,
                   agn_licensed = v_agn_licensed,
                   agn_license_grace_pr = v_agn_license_grace_pr,
                   agn_old_acc_no = v_agn_old_acc_no,
                   agn_status_remarks = v_agn_status_remarks,
                   agn_bbr_code = v_agn_bbr_code,
                   agn_bank_acc_no = v_agn_bank_acc_no,
                   agn_unique_prefix = v_agn_unique_prefix,
                   agn_state_code = v_agn_state_code,
                   agn_crdt_rting = v_agn_crdt_rting,
                   agn_subagent = v_agn_subagent,
                   agn_main_agn_code = v_agn_main_agn_code,
                   agn_clnt_code = v_agn_clnt_code,
                   agn_account_manager = v_agn_account_manager,
                   agn_credit_limit = v_agn_credit_limit,
                   agn_bru_code = v_agn_bru_code,
                   agn_local_international = v_agn_local_international,
                   agn_regulator_number = v_agn_regulator_number,
                   agn_rorg_code = v_agn_rorg_code,
                   agn_ors_code = v_agn_ors_code,
                   agn_bounced_chq = v_agn_bounced_chq,
                   agn_bpn_code = v_agn_bpn_code,
                   agn_agent_type = v_agn_agent_type,
                   agn_group = v_agn_group,
                   agn_default_comm_mode = v_def_comm_mode,
                   agn_allocate_cert = v_agn_allocate_cert,
                   agn_vat_applicable = v_vat_appl,
                   agn_whtax_applicable = v_withtaxappl,
                   agn_tel_pay = v_agn_tel_pay,
                   agn_payment_freq= v_agn_payment_freq,
                   agn_default_cpm_mode=v_agn_default_cpm_mode
                   
             WHERE agn_code = v_agn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while updating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
         
         SELECT COUNT(*) INTO v_accCount
         FROM TQC_ACCOUNT_CONTACTS
         WHERE ACCC_ACC_CODE = v_agn_code;
         
         IF NVL(v_accCount,0) = 0 THEN
         
         BEGIN
            INSERT INTO tqc_account_contacts
                        (accc_code,
                         accc_acc_code,
                         accc_name,
                         accc_other_names,
                         accc_tel,
                         accc_email_addr,
                         accc_sms_tel,
                         accc_username,
                         accc_pwd,
                         accc_login_allowed,
                         accc_pwd_changed, accc_pwd_reset,
                         accc_dt_created, accc_status,
                         accc_login_attempts,
                         accc_personel_rank,
                         accc_last_login_date,
                         accc_created_by
                        )
                 VALUES (tqc_accc_code_seq.NEXTVAL,
                         v_agn_code,
                         v_agn_sht_desc,
                         v_agn_name,
                         v_agn_tel1,
                         v_agn_email_address,
                         v_decodesmstel,
                         v_agn_sht_desc,
                         /*v_account_contacts_tab(I).ACCC_PWD*/
                         GIS_READ.VAL('123456'),
                         'Y',
                         SYSDATE, 'Y',
                         SYSDATE, 'A',
                         0,
                         'AGENCY',
                         SYSDATE,
                         'SYSTEM'
                        );
         EXCEPTION
           WHEN OTHERS
           THEN
              NULL;
         END;
         END IF;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_agencies
                  WHERE agn_code = v_agn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;
   -----------------------------------------------------------------------------------------------
   ALTER TABLE TQ_CRM.TQC_SERVICE_PROVIDERS
ADD (SPR_TEL_PAY VARCHAR2(35 BYTE));

ALTER TABLE TQ_CRM.TQC_SERVICE_PROVIDERS
ADD (spr_default VARCHAR2(1 BYTE));

------------------------------------------------------------------------------------------------------
PROCEDURE service_providers_prc (
      v_add_edit                IN   VARCHAR2,
      v_spr_code                IN   tqc_service_providers.spr_code%TYPE,
      v_spr_sht_desc            IN   tqc_service_providers.spr_sht_desc%TYPE,
      v_spr_name                IN   tqc_service_providers.spr_name%TYPE,
      v_spr_physical_address    IN   tqc_service_providers.spr_physical_address%TYPE,
      v_spr_postal_address      IN   tqc_service_providers.spr_postal_address%TYPE,
      v_spr_twn_code            IN   tqc_service_providers.spr_twn_code%TYPE,
      v_spr_cou_code            IN   tqc_service_providers.spr_cou_code%TYPE,
      v_spr_spt_code            IN   tqc_service_providers.spr_spt_code%TYPE,
      v_spr_phone               IN   tqc_service_providers.spr_phone%TYPE,
      v_spr_fax                 IN   tqc_service_providers.spr_fax%TYPE,
      v_spr_email               IN   tqc_service_providers.spr_email%TYPE,
      v_spr_title               IN   tqc_service_providers.spr_title%TYPE,
      v_spr_zip                 IN   tqc_service_providers.spr_zip%TYPE,
      v_spr_wef                 IN   tqc_service_providers.spr_wef%TYPE,
      v_spr_wet                 IN   tqc_service_providers.spr_wet%TYPE,
      v_spr_contact             IN   tqc_service_providers.spr_contact%TYPE,
      v_spr_aims_code           IN   tqc_service_providers.spr_aims_code%TYPE,
      v_spr_bbr_code            IN   tqc_service_providers.spr_bbr_code%TYPE,
      v_spr_bank_acc_no         IN   tqc_service_providers.spr_bank_acc_no%TYPE,
      v_spr_created_by          IN   tqc_service_providers.spr_created_by%TYPE,
      v_spr_date_created        IN   tqc_service_providers.spr_date_created%TYPE,
      v_spr_status_remarks      IN   tqc_service_providers.spr_status_remarks%TYPE,
      v_spr_status              IN   tqc_service_providers.spr_status%TYPE,
      v_spr_pin_number          IN   tqc_service_providers.spr_pin_number%TYPE,
      v_spr_trs_occupation      IN   tqc_service_providers.spr_trs_occupation%TYPE,
      v_spr_proff_body          IN   tqc_service_providers.spr_proff_body%TYPE,
      v_spr_pin                 IN   tqc_service_providers.spr_pin%TYPE,
      v_spr_doc_phone           IN   tqc_service_providers.spr_doc_phone%TYPE,
      v_spr_doc_email           IN   tqc_service_providers.spr_doc_email%TYPE,
      v_spr_gl_acc_no           IN   tqc_service_providers.spr_gl_acc_no%TYPE,
      v_sprinhouse              IN   tqc_service_providers.spr_inhouse%TYPE,
      v_spr_sms_number          IN   tqc_service_providers.spr_sms_number%TYPE,
      v_spr_contact_person      IN   tqc_service_providers.spr_contact_person%TYPE,
      v_spr_cont_person_phone   IN   tqc_service_providers.spr_cont_person_phone%TYPE,
      v_spr_invoice_number      IN   tqc_service_providers.spr_invoice_number%TYPE,
      v_spr_clnt_code           IN   tqc_service_providers.spr_clnt_code%TYPE,
      v_spr_bpn_code            IN   tqc_service_providers.spr_bpn_code%TYPE,
      v_spr_contact_email       IN   tqc_service_providers.spr_contact_email%TYPE,
      v_spr_contact_tel         IN   tqc_service_providers.spr_contact_tel%TYPE,
      v_spr_tel_pay             IN   tqc_service_providers.spr_tel_pay%TYPE,
      v_spr_default             IN   tqc_service_providers.spr_default%TYPE
   )
   IS
      v_count         NUMBER;
      v_code          NUMBER;
      v_param_value   VARCHAR2 (1);
   BEGIN
      --RAISE_ERROR('TEST');
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_service_providers
             WHERE spr_code = v_spr_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            BEGIN
               SELECT tqc_spr_code_seq.NEXTVAL
                 INTO v_code
                 FROM DUAL;
            END;

            INSERT INTO tqc_service_providers
                        (spr_code, spr_sht_desc, spr_name,
                         spr_physical_address, spr_postal_address,
                         spr_twn_code, spr_cou_code, spr_spt_code,
                         spr_phone, spr_fax, spr_email, spr_title,
                         spr_zip, spr_wef, spr_wet, spr_contact,
                         spr_aims_code, spr_bbr_code, spr_bank_acc_no,
                         spr_created_by, spr_date_created,
                         spr_status_remarks, spr_status,
                         spr_pin_number, spr_trs_occupation,
                         spr_proff_body, spr_pin, spr_doc_phone,
                         spr_doc_email, spr_gl_acc_no, spr_inhouse,
                         spr_sms_number, spr_contact_person,
                         spr_cont_person_phone, spr_invoice_number,
                         spr_clnt_code, spr_bpn_code,
                         spr_contact_email, spr_contact_tel,spr_tel_pay,
                         spr_default
                        )
                 VALUES (v_code, v_spr_sht_desc, v_spr_name,
                         v_spr_physical_address, v_spr_postal_address,
                         v_spr_twn_code, v_spr_cou_code, v_spr_spt_code,
                         v_spr_phone, v_spr_fax, v_spr_email, v_spr_title,
                         v_spr_zip, v_spr_wef, v_spr_wet, v_spr_contact,
                         v_spr_aims_code, v_spr_bbr_code, v_spr_bank_acc_no,
                         v_spr_created_by, v_spr_date_created,
                         v_spr_status_remarks, v_spr_status,
                         v_spr_pin_number, v_spr_trs_occupation,
                         v_spr_proff_body, v_spr_pin, v_spr_doc_phone,
                         v_spr_doc_email, v_spr_gl_acc_no, v_sprinhouse,
                         v_spr_sms_number, v_spr_contact_person,
                         v_spr_cont_person_phone, v_spr_invoice_number,
                         v_spr_clnt_code, v_spr_bpn_code,
                         v_spr_contact_email, v_spr_contact_tel,v_spr_tel_pay, 
                         v_spr_default
                        );

            BEGIN
               SELECT param_value
                 INTO v_param_value
                 FROM tqc_parameters
                WHERE param_name = 'AUTO_ASSIGN_SPR_SYS';
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  v_param_value := 'N';
               WHEN OTHERS
               THEN
                  v_param_value := 'N';
            END;

            BEGIN
               IF v_param_value = 'Y'
               THEN
                  grant_prov_system (v_code, 37, v_spr_created_by);
               END IF;
            EXCEPTION
               WHEN OTHERS
               THEN
                  NULL;
            END;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_service_providers
               SET spr_sht_desc = v_spr_sht_desc,
                   spr_name = v_spr_name,
                   spr_physical_address = v_spr_physical_address,
                   spr_postal_address = v_spr_postal_address,
                   spr_twn_code = v_spr_twn_code,
                   spr_cou_code = v_spr_cou_code,
                   spr_spt_code = v_spr_spt_code,
                   spr_phone = v_spr_phone,
                   spr_fax = v_spr_fax,
                   spr_email = v_spr_email,
                   spr_title = v_spr_title,
                   spr_zip = v_spr_zip,
                   spr_wef = v_spr_wef,
                   spr_wet = v_spr_wet,
                   spr_contact = v_spr_contact,
                   spr_aims_code = v_spr_aims_code,
                   spr_bbr_code = v_spr_bbr_code,
                   spr_bank_acc_no = v_spr_bank_acc_no,
                   spr_created_by = v_spr_created_by,
                   -- SPR_DATE_CREATED        = v_SPR_DATE_CREATED,
                   spr_status_remarks = v_spr_status_remarks,
                   spr_status = v_spr_status,
                   spr_pin_number = v_spr_pin_number,
                   spr_trs_occupation = v_spr_trs_occupation,
                   spr_proff_body = v_spr_proff_body,
                   spr_pin = v_spr_pin,
                   spr_doc_phone = v_spr_doc_phone,
                   spr_doc_email = v_spr_doc_email,
                   spr_gl_acc_no = v_spr_gl_acc_no,
                   spr_inhouse = v_sprinhouse,
                   spr_sms_number = v_spr_sms_number,
                   spr_contact_person = v_spr_contact_person,
                   spr_cont_person_phone = v_spr_cont_person_phone,
                   spr_invoice_number = v_spr_invoice_number,
                   spr_clnt_code = v_spr_clnt_code,
                   spr_bpn_code = v_spr_bpn_code,
                   spr_contact_email = v_spr_contact_email,
                   spr_contact_tel = v_spr_contact_tel,
                   spr_tel_pay = v_spr_tel_pay,
                   spr_default=v_spr_default
             WHERE spr_code = v_spr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while updating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_service_providers
                  WHERE spr_code = v_spr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;