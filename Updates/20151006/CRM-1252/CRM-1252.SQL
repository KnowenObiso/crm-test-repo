ALTER TABLE TQ_CRM.TQC_AGENCIES
ADD (AGN_PYMT_VALIDATED VARCHAR2(1 BYTE) DEFAULT 'N');


-------------------------------------------------------------------------------
--tqc_setups_cursor.get_agency_info
PROCEDURE get_agency_info (
      v_agn_act_code      IN       tqc_agencies.agn_act_code%TYPE,
      v_agency_info_ref   OUT      agency_info_ref
   )
   IS
   BEGIN
      --RAISE_ERROR('v_agn_act_code'||v_agn_act_code);
      OPEN v_agency_info_ref FOR
         SELECT   a.agn_code, a.agn_act_code, a.agn_sht_desc, a.agn_name,
                  a.agn_physical_address, a.agn_postal_address,
                  a.agn_twn_code, a.agn_cou_code, a.agn_email_address,
                  a.agn_web_address, a.agn_zip, a.agn_contact_person,
                  a.agn_contact_title, a.agn_tel1, a.agn_tel2, a.agn_fax,
                  a.agn_acc_no, a.agn_pin, a.agn_agent_commission,
                  a.agn_credit_allowed, a.agn_agent_wht_tax,
                  a.agn_print_dbnote, a.agn_status, a.agn_date_created,
                  a.agn_created_by, a.agn_reg_code, a.agn_comm_reserve_rate,
                  a.agn_annual_budget, a.agn_status_eff_date,
                  a.agn_credit_period, a.agn_comm_stat_eff_dt,
                  a.agn_comm_status_dt, a.agn_comm_allowed, a.agn_checked,
                  a.agn_checked_by, a.agn_check_date,
                  a.agn_comp_comm_arrears, a.agn_reinsurer, a.agn_brn_code,
                  a.agn_town, a.agn_country, a.agn_status_desc, a.agn_id_no,
                  a.agn_con_code, a.agn_agn_code, a.agn_sms_tel,
                  a.agn_ahc_code, a.agn_sec_code, a.agn_agnc_class_code,
                  a.agn_expiry_date, a.agn_license_no, a.agn_runoff,
                  a.agn_licensed, a.agn_license_grace_pr, a.agn_old_acc_no,
                  a.agn_status_remarks, bnk_code, bnk_bank_name, bbr_code,
                  bbr_branch_name, a.agn_bank_acc_no, a.agn_unique_prefix,
                  cou_admin_reg_type, a.agn_state_code, sts_name,
                  a.agn_crdt_rting, clnt_name, b.agn_name accountmanager,
                  prom_transaction_date, prom_transaction_type,
                  prom_demo_promo_type, brn_name, bra_name, brn_code,
                  prom_bra_unt_sht_desc_prefix, prom_bru_unt_sht_desc_prefix,
                  prom_bru_agn_seq_no, prom_precontract_agn_seq,
                  cou_zip_code, b.agn_code, a.agn_credit_limit, bru_code,
                  bru_name, a.agn_local_international,
                  a.agn_regulator_number, a.agn_authorised, a.agn_rorg_code,
                  a.agn_ors_code, rorg_desc, ors_desc, a.agn_allocate_cert,
                  c.agn_name, a.agn_bounced_chq, a.agn_default_comm_mode,
                  a.agn_bpn_code, bpn_name, a.agn_agent_type, a.agn_group,
                  a.agn_main_agn_code, d.agn_name, a.agn_vat_applicable,
                  a.agn_whtax_applicable,a.agn_tel_pay,a.agn_payment_freq,a.agn_default_cpm_mode,
                  (select cpm_desc from tqc_clm_payment_modes where cpm_code = a.agn_default_cpm_mode) agn_cpm_mode_desc,
                  a.agn_pymt_validated
             FROM tqc_agencies a,
                  tqc_agencies b,
                  tqc_agencies c,
                  tqc_agencies d,
                  tqc_bank_branches,
                  tqc_banks,
                  tqc_countries,
                  tqc_states,
                  tqc_clients,
                  lms_agent_promotion,
                  tqc_branches,
                  tqc_branch_agencies,
                  tqc_branch_units,
                  tqc_rating_organizations,
                  tqc_org_rating_starndards,
                  tqc_business_persons
            WHERE a.agn_bbr_code = bbr_code(+)
              AND a.agn_cou_code = cou_code(+)
              AND bbr_bnk_code = bnk_code(+)
              AND a.agn_state_code = sts_code(+)
              AND a.agn_clnt_code = clnt_code(+)
              AND c.agn_code(+) = a.agn_agn_code
              AND d.agn_code(+) = a.agn_main_agn_code
              AND a.agn_account_manager = b.agn_code(+)
              AND a.agn_act_code = NVL (v_agn_act_code, a.agn_act_code)
              AND prom_agn_code(+) = a.agn_code
              AND brn_code(+) = prom_brn_code
              AND bru_code(+) = a.agn_bru_code
              AND bra_code(+) = prom_bra_code
              AND rorg_code(+) = a.agn_rorg_code
              AND a.agn_ors_code = ors_code(+)
              AND bpn_code(+) = a.agn_bpn_code
         -- AND BRU_BRN_CODE(+)=BRN_CODE
         ORDER BY a.agn_name;
   END get_agency_info;
-------------------------------------------------------------------------------
--tqc_setups_cursor.agency_info_rec
TYPE agency_info_rec IS RECORD (
      agn_code                   tqc_agencies.agn_code%TYPE,
      agn_act_code               tqc_agencies.agn_act_code%TYPE,
      agn_sht_desc               tqc_agencies.agn_sht_desc%TYPE,
      agn_name                   tqc_agencies.agn_name%TYPE,
      agn_physical_address       tqc_agencies.agn_physical_address%TYPE,
      agn_postal_address         tqc_agencies.agn_postal_address%TYPE,
      agn_twn_code               tqc_agencies.agn_twn_code%TYPE,
      agn_cou_code               tqc_agencies.agn_cou_code%TYPE,
      agn_email_address          tqc_agencies.agn_email_address%TYPE,
      agn_web_address            tqc_agencies.agn_web_address%TYPE,
      agn_zip                    tqc_agencies.agn_zip%TYPE,
      agn_contact_person         tqc_agencies.agn_contact_person%TYPE,
      agn_contact_title          tqc_agencies.agn_contact_title%TYPE,
      agn_tel1                   tqc_agencies.agn_tel1%TYPE,
      agn_tel2                   tqc_agencies.agn_tel2%TYPE,
      agn_fax                    tqc_agencies.agn_fax%TYPE,
      agn_acc_no                 tqc_agencies.agn_acc_no%TYPE,
      agn_pin                    tqc_agencies.agn_pin%TYPE,
      agn_agent_commission       tqc_agencies.agn_agent_commission%TYPE,
      agn_credit_allowed         tqc_agencies.agn_credit_allowed%TYPE,
      agn_agent_wht_tax          tqc_agencies.agn_agent_wht_tax%TYPE,
      agn_print_dbnote           tqc_agencies.agn_print_dbnote%TYPE,
      agn_status                 tqc_agencies.agn_status%TYPE,
      agn_date_created           tqc_agencies.agn_date_created%TYPE,
      agn_created_by             tqc_agencies.agn_created_by%TYPE,
      agn_reg_code               tqc_agencies.agn_reg_code%TYPE,
      agn_comm_reserve_rate      tqc_agencies.agn_comm_reserve_rate%TYPE,
      agn_annual_budget          tqc_agencies.agn_annual_budget%TYPE,
      agn_status_eff_date        tqc_agencies.agn_status_eff_date%TYPE,
      agn_credit_period          tqc_agencies.agn_credit_period%TYPE,
      agn_comm_stat_eff_dt       tqc_agencies.agn_comm_stat_eff_dt%TYPE,
      agn_comm_status_dt         tqc_agencies.agn_comm_status_dt%TYPE,
      agn_comm_allowed           tqc_agencies.agn_comm_allowed%TYPE,
      agn_checked                tqc_agencies.agn_checked%TYPE,
      agn_checked_by             tqc_agencies.agn_checked_by%TYPE,
      agn_check_date             tqc_agencies.agn_check_date%TYPE,
      agn_comp_comm_arrears      tqc_agencies.agn_comp_comm_arrears%TYPE,
      agn_reinsurer              tqc_agencies.agn_reinsurer%TYPE,
      agn_brn_code               tqc_agencies.agn_brn_code%TYPE,
      agn_town                   tqc_agencies.agn_town%TYPE,
      agn_country                tqc_agencies.agn_country%TYPE,
      agn_status_desc            tqc_agencies.agn_status_desc%TYPE,
      agn_id_no                  tqc_agencies.agn_id_no%TYPE,
      agn_con_code               tqc_agencies.agn_con_code%TYPE,
      agn_agn_code               tqc_agencies.agn_agn_code%TYPE,
      agn_sms_tel                tqc_agencies.agn_sms_tel%TYPE,
      agn_ahc_code               tqc_agencies.agn_ahc_code%TYPE,
      agn_sec_code               tqc_agencies.agn_sec_code%TYPE,
      agn_agnc_class_code        tqc_agencies.agn_agnc_class_code%TYPE,
      agn_expiry_date            tqc_agencies.agn_expiry_date%TYPE,
      agn_license_no             tqc_agencies.agn_license_no%TYPE,
      agn_runoff                 tqc_agencies.agn_runoff%TYPE,
      agn_licensed               tqc_agencies.agn_licensed%TYPE,
      agn_license_grace_pr       tqc_agencies.agn_license_grace_pr%TYPE,
      agn_old_acc_no             tqc_agencies.agn_old_acc_no%TYPE,
      agn_status_remarks         tqc_agencies.agn_status_remarks%TYPE,
      bnk_code                   tqc_banks.bnk_code%TYPE,
      bnk_bank_name              tqc_banks.bnk_bank_name%TYPE,
      bbr_code                   tqc_bank_branches.bbr_code%TYPE,
      bbr_branch_name            tqc_bank_branches.bbr_branch_name%TYPE,
      agn_bank_acc_no            tqc_agencies.agn_bank_acc_no%TYPE,
      agn_unique_prefix          tqc_agencies.agn_unique_prefix%TYPE,
      cou_admin_reg_type         tqc_countries.cou_admin_reg_type%TYPE,
      agn_state_code             tqc_agencies.agn_state_code%TYPE,
      sts_name                   tqc_states.sts_name%TYPE,
      agn_crdt_rting             VARCHAR2 (20),
      clnt_name                  tqc_clients.clnt_name%TYPE,
      accountmanager             VARCHAR2 (200),
      prom_transaction_date      lms_agent_promotion.prom_transaction_date%TYPE,
      prom_transaction_type      lms_agent_promotion.prom_transaction_type%TYPE,
      prom_demo_promo_type       lms_agent_promotion.prom_demo_promo_type%TYPE,
      brn_name                   tqc_branches.brn_name%TYPE,
      bra_name                   tqc_branch_agencies.bra_name%TYPE,
      brn_code                   tqc_branches.brn_code%TYPE,
      bra_unt_sht_desc_prefix    tqc_branch_agencies.bra_unt_sht_desc_prefix%TYPE,
      bru_unt_sht_desc_prefix    tqc_branch_units.bru_unt_sht_desc_prefix%TYPE,
      bru_agn_seq_no             tqc_branch_units.bru_agn_seq_no%TYPE,
      prom_precontract_agn_seq   lms_agent_promotion.prom_precontract_agn_seq%TYPE,
      cou_zip_code               tqc_countries.cou_zip_code%TYPE,
      agn_account_manager        tqc_agencies.agn_account_manager%TYPE,
      agn_credit_limit           tqc_agencies.agn_credit_limit%TYPE,
      bru_code                   tqc_branch_units.bru_code%TYPE,
      bru_name                   tqc_branch_units.bru_name%TYPE,
      agn_local_international    tqc_agencies.agn_local_international%TYPE,
      agn_regulator_number       tqc_agencies.agn_regulator_number%TYPE,
      agn_authorised             tqc_agencies.agn_authorised%TYPE,
      agn_rorg_code              tqc_agencies.agn_rorg_code%TYPE,
      agn_ors_code               tqc_agencies.agn_ors_code%TYPE,
      rorg_desc                  tqc_rating_organizations.rorg_desc%TYPE,
      ors_desc                   tqc_org_rating_starndards.ors_desc%TYPE,
      agn_allocate_cert          tqc_agencies.agn_allocate_cert%TYPE,
      holding_companies          VARCHAR2 (200),
      agn_bounced_chq            tqc_agencies.agn_bounced_chq%TYPE,
      agn_default_comm_mode      tqc_agencies.agn_default_comm_mode%TYPE,
      agn_bpn_code               tqc_agencies.agn_bpn_code%TYPE,
      bpn_name                   tqc_business_persons.bpn_name%TYPE,
      agn_agent_type             tqc_agencies.agn_agent_type%TYPE,
      agn_group                  tqc_agencies.agn_group%TYPE,
      agn_main_agn_code          tqc_agencies.agn_main_agn_code%TYPE,
      main_agency_name           tqc_agencies.agn_name%TYPE,
      agn_vat_applicable         tqc_agencies.agn_vat_applicable%TYPE,
      agn_whtax_applicable       tqc_agencies.agn_whtax_applicable%TYPE,
      agn_tel_pay                tqc_agencies.agn_tel_pay%TYPE,
      agn_payment_freq           tqc_agencies.agn_payment_freq%TYPE,
      agn_default_cpm_mode       tqc_agencies.agn_default_cpm_mode%TYPE,
      agn_cpm_mode_desc          VARCHAR2(50),
      agn_pymt_validated         tqc_agencies.agn_pymt_validated%TYPE
      
   );
-------------------------------------------------------------------------------
--tqc_setups_pkg.agencies_prc
PROCEDURE agencies_prc (
      v_add_edit                  IN   VARCHAR2,
      v_agn_code                  IN   tqc_agencies.agn_code%TYPE,
      v_agn_act_code              IN   tqc_agencies.agn_act_code%TYPE,
      v_agn_sht_desc              IN OUT   tqc_agencies.agn_sht_desc%TYPE,
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
      v_freq_payment              IN   VARCHAR2, 
      v_pymt_mode                 IN   VARCHAR2,
      v_pymt_validated            IN   VARCHAR2
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
      v_sms_tel_count     number;
      v_agn_authorised    varchar2(10);
      v_old_agn_sht_desc  varchar2(30);
   BEGIN
      --RAISE_ERROR('fdfdf'||v_agn_main_agn_code);
         --  RAISE_ERROR('v_agn_allocate_cert'||v_agn_allocate_cert||'v_agn_agn_code'||v_agn_agn_code);
      IF v_agn_sms_tel IS NOT NULL
      THEN
      SELECT INSTR(v_agn_sms_tel,'+')
         INTO v_sms_tel_count
         FROM DUAL ;
         
         IF v_sms_tel_count = 0 THEN 
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
         ELSE
            v_decodesmstel := v_agn_sms_tel;
         END IF;
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
            
            v_agn_sht_desc := v_agn_short_desc;

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
                         agn_payment_freq,agn_default_cpm_mode,agn_pymt_validated
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
                         v_freq_payment, v_pymt_mode ,v_pymt_validated
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
                         accc_sys_code,
                         accc_reset_code
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
                         NULL,
                         v_agnResetCode
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
         
         SELECT AGN_AUTHORISED,agn_sht_desc
            INTO v_agn_authorised,v_old_agn_sht_desc
            FROM tqc_agencies
            WHERE agn_code = v_agn_code;
            
            IF v_agn_authorised = 'Y' AND v_old_agn_sht_desc<>v_agn_sht_desc THEN
            
            RAISE_ERROR('AGENT SHORT DESCRIPTION SHOULD NOT BE EDITED FOR AN AUTHORIZED CLIENT REVERT TO ....'||v_old_agn_sht_desc);
                
            END IF;
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
                   agn_payment_freq=v_freq_payment,
                   agn_default_cpm_mode = v_pymt_mode,
                   agn_pymt_validated = v_pymt_validated
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

 