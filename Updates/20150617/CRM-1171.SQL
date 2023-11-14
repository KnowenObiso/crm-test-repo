ALTER TABLE TQ_CRM.TQC_CLIENTS
ADD (clnt_email2 VARCHAR2(200 BYTE));



----------------------------------------------------------
 FUNCTION client_extended_proc (
      v_direct_clnt               IN       VARCHAR2,
      v_sht_desc                  IN       VARCHAR2,
      v_first_name                IN       VARCHAR2,
      v_middle_name               IN       VARCHAR2,
      v_surname                   IN       VARCHAR2,
      v_pin                       IN       VARCHAR2,
      v_postal_addrs              IN       VARCHAR2,
      v_physical_addrs            IN       VARCHAR2,
      v_id_reg_no                 IN       VARCHAR2,
      v_user                      IN       VARCHAR2,
      v_wef                       IN       DATE,
      v_wet                       IN       DATE,
      v_title                     IN       VARCHAR2,
      v_dob                       IN       DATE,
      v_cou_code                  IN       NUMBER,
      v_twn_code                  IN       NUMBER,
      v_zip_code                  IN       VARCHAR2,
      v_email_addrs               IN       VARCHAR2,
      v_tel                       IN       VARCHAR2,
      v_sms_tel                   IN       VARCHAR2,
      v_fax                       IN       VARCHAR2,
      v_sec_code                  IN       NUMBER,
      v_business                  IN       VARCHAR2,
      v_domicile_countries        IN       NUMBER,
      v_proposer                  IN       VARCHAR2,
      v_status                    IN       VARCHAR2,
      v_runoff                    IN       VARCHAR2,
      v_withdrawal_reason         IN       VARCHAR2,
      v_remarks                   IN       VARCHAR2,
      v_bank_acc_no               IN       VARCHAR2,
      v_bbr_code                  IN       NUMBER,
      v_spcl_terms                IN       VARCHAR2,
      v_policy_cancelled          IN       VARCHAR2,
      v_increased_premium         IN       VARCHAR2,
      v_declined_prop             IN       VARCHAR2,
      v_client_type               IN       VARCHAR2,
      v_add_edit                  IN       VARCHAR2,
      v_clnt_code                 IN       NUMBER,
      v_acc_mngr_code             IN       NUMBER,
      v_clnt_cntct_name_1         IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_phone_1        IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_email_1        IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_name_2         IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_phone_2        IN       VARCHAR2 DEFAULT NULL,
      v_clnt_cntct_email_2        IN       VARCHAR2 DEFAULT NULL,
      v_passport_no               IN       VARCHAR2 DEFAULT NULL,
      v_sts_code                  IN       tqc_clients.clnt_sts_code%TYPE,
      v_website                   IN       VARCHAR2,
      v_auditors                  IN       VARCHAR2,
      v_parent_company            IN       NUMBER,
      v_current_insurer           IN       VARCHAR2,
      v_projectedbiz              IN       NUMBER,
      v_date_of_employ            IN       DATE,
      v_driving_licence           IN       VARCHAR2,
      v_brn_code                  IN       NUMBER,
      v_clnt_image                IN       BLOB,
      v_signature                 IN       BLOB,
      v_clnt_acc_officer          IN       NUMBER,
      v_gender                    IN       VARCHAR2,
      v_clnt_occupation           IN       VARCHAR2,
      v_clnt_bank_phone_no        IN       VARCHAR2,
      v_clnt_bank_cell_no         IN       VARCHAR2,
      v_clnt_employer_phone_no    IN       VARCHAR2,
      v_clnt_employer_cell_no     IN       VARCHAR2,
      v_clt_cell_no               IN       VARCHAR2,
      v_cltn_client_types         IN       VARCHAR2,
      vclntcode                   OUT      tqc_clients.clnt_code%TYPE,
      v_clnt_sht_desc             OUT      tqc_clients.clnt_sht_desc%TYPE,
      v_oldshtdesc                         VARCHAR2 DEFAULT NULL,
      v_clnt_anniversary          IN       DATE DEFAULT NULL,
      v_clnt_crdt_rating          IN       VARCHAR2 DEFAULT NULL,
      v_client_system_code        IN       NUMBER DEFAULT NULL,
      v_clnt_drv_experience       IN       VARCHAR2,
      v_clnt_sacco                IN       VARCHAR2 DEFAULT NULL,
      v_clnt_clnt_code            IN       NUMBER,
      v_clnt_reason_updated       IN       VARCHAR2 DEFAULT NULL,
      v_clnt_credit_lim_allowed   IN       VARCHAR2 DEFAULT NULL,
      v_clnt_credit_limit         IN       VARCHAR2 DEFAULT NULL,
      v_clnt_loc_code             IN       NUMBER DEFAULT NULL,
      v_clnt_occ_code             IN       NUMBER DEFAULT NULL,
      v_clnt_bounced_chq          IN       VARCHAR2 DEFAULT NULL,
      v_clnt_marital_status       IN       VARCHAR2 DEFAULT NULL,
      v_commmode                  IN       VARCHAR2 DEFAULT NULL,
      v_payroll                   IN       VARCHAR2 DEFAULT NULL,
      v_minsalary                 IN       NUMBER DEFAULT NULL,
      v_maxsalary                 IN       NUMBER DEFAULT NULL,
      v_clnt_dl_issue_date        IN       DATE DEFAULT NULL,
      v_work_permit               IN       VARCHAR2 DEFAULT NULL,
      v_clnt_bpn_code             IN       NUMBER DEFAULT NULL,
      v_clnt_client_level         IN       VARCHAR2 DEFAULT NULL,
      v_clnt_tel_pay              IN       VARCHAR2 DEFAULT NULL,
      v_clnt_email2               IN       VARCHAR2 DEFAULT NULL
   )
      RETURN NUMBER
   IS
      v_direct_clnt_seq     NUMBER;
      --v_direct_clnt varchar2(30);
      --v_direct_clnt varchar2(30);
      v_direct_srl_fmt      VARCHAR2 (50);
      v_clnt_param          VARCHAR2 (20);
      v_accnt_no            VARCHAR2 (35);
      v_prev_values         VARCHAR2 (2000);
      v_updated_values      VARCHAR2 (2000);
      v_updated_fields      VARCHAR2 (2000);
      v_update              NUMBER;
      v_brn_sht_desc        VARCHAR2 (35);
      v_gen_acc_no          VARCHAR2 (35);
      v_use_names_in_code   VARCHAR2 (1)    := 'Y';
      v_decodesmstel        VARCHAR2 (50);
      v_branch_mandatory    VARCHAR2 (1)    := 'Y';
      v_err                 VARCHAR2 (200);
      v_auth_auto           VARCHAR2 (1);
      v_id_cnt              NUMBER;
      v_client_code         NUMBER
          := pkg_global_vars.get_pvarchar2 ('PKG_GLOBAL_VARS.Pvg_ClientCode');
      
      v_sms_max_val NUMBER;
      v_sms_min_val NUMBER;
      v_tel_max_val NUMBER;
      v_tel_mIN_val NUMBER;

      CURSOR clnts
      IS
         SELECT *
           FROM tqc_clients
          WHERE clnt_code = v_clnt_code;
   BEGIN
   --raise_error('v_clnt_email2'||v_clnt_email2||' v_clnt_tel_pay '||v_clnt_tel_pay);
    --RAISE_ERROR('v_payroll '||v_payroll||' v_minSalary '||v_minSalary||' v_minSalary '||v_minSalary);
--RAISE_ERROR('v_sec_code='||v_sec_code);
      IF NVL (v_postal_addrs, 'P.O BOX') = 'P.O. BOX'
      THEN
         raise_error ('Please Enter Postal Address');
      END IF;

      IF INSTR (v_sms_tel, 'null') != 0
      THEN
         raise_error ('NULL PHONE NUMBER NOT ACCEPTED');
      END IF;

      IF v_id_reg_no IS NOT NULL
      THEN
         BEGIN
            SELECT NVL (COUNT (*), 0)
              INTO v_id_cnt
              FROM (SELECT clnt_id_reg_no
                      FROM tqc_clients
                     WHERE clnt_id_reg_no = v_id_reg_no
                       AND clnt_code <> v_clnt_code);
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_id_cnt := 0;
         END;
      END IF;

      IF NVL (v_id_cnt, 0) >= 2
      THEN
         raise_error ('Id number already exists....' || v_id_cnt || ' times');
      END IF;

      IF v_sms_tel IS NOT NULL
      THEN
         BEGIN
            SELECT '+' || cou_zip_code || v_sms_tel
              INTO v_decodesmstel
              FROM tqc_countries
             WHERE cou_code = v_cou_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               NULL;
         END;
      END IF;
      
      
      BEGIN
           /* SELECT to_number(PARAM_VALUE)
            INTO v_sms_max_val
            FROM TQC_PARAMETERS
            WHERE PARAM_NAME='SMS_NO_UPPER_LIMIT';
            
            SELECT to_number(PARAM_VALUE)
            INTO v_sms_min_val
            FROM TQC_PARAMETERS
            WHERE PARAM_NAME='SMS_NO_LOWER_LIMIT';
            
            SELECT to_number(PARAM_VALUE)
            INTO v_tel_max_val
            FROM TQC_PARAMETERS
            WHERE PARAM_NAME='TEL_NO_UPPER_LIMIT';
            
            SELECT to_number(PARAM_VALUE)
            INTO v_tel_min_val
            FROM TQC_PARAMETERS
            WHERE PARAM_NAME='TEL_NO_LOWER_LIMIT';*/
            
            IF length(v_decodesmstel) > v_sms_max_val  then
                    RAISE_ERROR('Too many values for GSM no please check A=='||length(v_decodesmstel)||' Max dig should be = '||v_sms_max_val);
            END IF;
            
            IF length(v_decodesmstel) < v_sms_min_val  then
                    RAISE_ERROR('Too few values for GSM no please check A=='||length(v_decodesmstel)||' Min digshould be = '||v_sms_min_val);
            END IF;
            
             IF length(v_tel) > v_tel_max_val  then
                    RAISE_ERROR('Too many values for Tel no please check A=='||length(v_tel)||' B== '||v_tel_max_val);
            END IF;
            
            IF length(v_tel) < v_tel_min_val  then
                    RAISE_ERROR('Too few values for Tel no please check A=='||length(v_tel)||' B== '||v_tel_min_val);
            END IF;
         END;

      --   RAISE_ERROR('v_decodeSmsTel'||v_decodeSmsTel);
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT gin_parameters_pkg.get_param_varchar
                                                      ('CLNT_BRANCH_MANDATORY')
              INTO v_branch_mandatory
              FROM DUAL;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_branch_mandatory := 'Y';
         END;

         IF NVL (v_branch_mandatory, 'Y') = 'Y' AND v_brn_code IS NULL
         THEN
            raise_error ('Specify Client branch');
         END IF;

         IF NVL (v_direct_clnt, 'K') NOT IN ('C', 'I', 'D')
         THEN
            raise_error ('Specify if this is a Direct Client '
                         || v_direct_clnt
                        );
         END IF;

         BEGIN
            SELECT tqc_parameters_pkg.get_param_varchar ('USE_NAMES_IN_CODE')
              INTO v_use_names_in_code
              FROM DUAL;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_use_names_in_code := 'Y';
         END;

         IF v_direct_clnt = 'Y' OR NVL (v_use_names_in_code, 'Y') = 'N'
         THEN
            BEGIN
               SELECT act_id_serial_format
                 INTO v_direct_srl_fmt
                 FROM tqc_account_types
                WHERE act_code = 1;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error getting direct clients format..');
            END;

            IF v_direct_srl_fmt IS NULL
            THEN
               raise_error ('Provide Id Format for the Direct Account type');
            END IF;

            IF v_brn_code IS NOT NULL
            THEN
               SELECT brn_sht_desc
                 INTO v_brn_sht_desc
                 FROM tqc_branches
                WHERE brn_code = v_brn_code;
            END IF;

            BEGIN
               SELECT direct_clnt_seq.NEXTVAL
                 INTO v_direct_clnt_seq
                 FROM DUAL;

               v_accnt_no := LPAD (v_direct_clnt_seq, 6, 0);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error fetching the sequence...');
            END;

--                BEGIN
--                    SELECT TQ_CRM.CLNT_ST_DESC_SEQ.NEXTVAL INTO v_direct_clnt_seq FROM DUAL;
--                    v_gen_acc_no:= REPLACE(v_direct_srl_fmt,'[SERIALNO]',LPAD(v_direct_clnt_seq,6,0));
--                    v_gen_acc_no:= REPLACE(v_gen_acc_no,'[BRN]',v_brn_sht_desc);
--                    v_gen_acc_no:=REPLACE(v_gen_acc_no,'[YY]',TO_CHAR(TO_DATE(SYSDATE),'RR'));
--                    v_gen_acc_no:= REPLACE(v_gen_acc_no,'[YEAR]',TO_CHAR(TO_DATE(SYSDATE),'RRRR'));
--                EXCEPTION
--                    WHEN OTHERS THEN
--                        RAISE_ERROR('Error fetching the client short desc sequence...');
--                END;
            BEGIN
               v_clnt_param :=
                    tqc_parameters_pkg.get_param_varchar ('CLIENT_ID_FORMAT');
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error fetching client parameter...');
            END;

            IF NVL (v_clnt_param, 'DEFAULT') = 'DEFAULT'
            THEN
               --v_clnt_sht_desc := v_gen_acc_no;
               v_clnt_sht_desc :=
                  tqc_sequences_pkg.get_number_format ('C',
                                                       v_brn_code,
                                                       TO_CHAR (v_wef, 'RRRR'),
                                                       NVL (v_client_type,
                                                            'I'),
                                                       v_direct_srl_fmt
                                                      );
            END IF;
         END IF;
         
         --RAISE_ERROR('ERROR '||v_first_name||' v_middle_name '||v_middle_name||'v_surname '||v_surname);

         IF v_clnt_sht_desc IS NULL
         THEN
            BEGIN
               v_clnt_sht_desc :=
                  get_client_sht_desc (LTRIM (RTRIM (   v_first_name
                                                     || ' '
                                                     || v_middle_name
                                                    )
                                             ),
                                       v_middle_name,
                                       v_surname
                                      );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error creating client Id...'
                               || v_clnt_sht_desc
                              );
            END;
         END IF;

         IF NVL (v_clnt_param, 'DEFAULT') = 'FMSURNAME'
         THEN
            v_accnt_no := v_sht_desc;
         END IF;

         IF v_accnt_no IS NULL
         THEN
            v_accnt_no := v_direct_clnt;
         END IF;

         BEGIN
            SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'YY'))
                   || tqc_clnt_code_seq.NEXTVAL
              INTO vclntcode
              FROM DUAL;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Fetching proposer code..');
         END;

         --RAISE_ERROR('v_clnt_sht_desc'||v_clnt_sht_desc);
         BEGIN
            INSERT INTO tqc_clients
                        (clnt_code, clnt_pin, clnt_sht_desc,
                         clnt_postal_addrs, clnt_physical_addrs,
                         clnt_other_names,
                         clnt_name,
                         clnt_id_reg_no, clnt_date_created, clnt_created_by,
                         clnt_wef, clnt_wet, clnt_direct_client, clnt_title,
                         clnt_dob, clnt_cou_code, clnt_twn_code,
                         clnt_zip_code, clnt_email_addrs, clnt_tel,
                         clnt_sms_tel, clnt_fax, clnt_sec_code,
                         clnt_business, clnt_domicile_countries,
                         clnt_accnt_no, clnt_proposer, clnt_status,
                         clnt_runoff, clnt_withdrawal_reason, clnt_remarks,
                         clnt_bank_acc_no, clnt_bbr_code, clnt_clnt_code,
                         clnt_spcl_terms, clnt_policy_cancelled,
                         clnt_increased_premium, clnt_declined_prop,
                         clnt_type,
                         clnt_usr_code, clnt_cntct_name_1,
                         clnt_cntct_phone_1, clnt_cntct_email_1,
                         clnt_cntct_name_2, clnt_cntct_phone_2,
                         clnt_cntct_email_2, clnt_passport_no,
                         clnt_sts_code, clnt_website, clnt_auditors,
                         clnt_parent_company, clnt_current_insurer,
                         clnt_projected_business, clnt_date_of_empl,
                         clnt_driving_licence, clnt_brn_code, clnt_image,
                         clnt_signature, clnt_acc_officer, clnt_gender,
                         clnt_occupation, clnt_bank_phone_no,
                         clnt_bank_cell_no, clnt_employer_phone_no,
                         clnt_employer_cell_no, clt_cell_no,
                         clnt_old_sht_desc, clnt_anniversary,
                         clnt_crdt_rating, cltn_client_types,
                         clnt_drv_experience, clnt_sacco,
                         clnt_credit_lim_allowed, clnt_credit_limit,
                         clnt_loc_code, clnt_occ_code,
                         clnt_bounced_chq, clnt_marital_status,
                         clnt_bpn_code, clnt_payroll_no, clnt_sal_min_range,
                         clnt_sal_max_range, clnt_dl_issue_date,
                         clnt_work_permit, clnt_default_comm_mode,clnt_tel_pay,
                         clnt_email2,clnt_client_level
                        )
                 VALUES (vclntcode, v_pin, v_clnt_sht_desc,
                         v_postal_addrs, v_physical_addrs,
                         LTRIM (RTRIM (v_first_name || ' ' || v_middle_name)),
                         LTRIM (RTRIM (NVL (v_surname, 'n/a'))),
                         v_id_reg_no, TRUNC (SYSDATE), v_user,
                         v_wef, v_wet, v_direct_clnt, v_title,
                         v_dob, v_cou_code, v_twn_code,
                         v_zip_code, v_email_addrs, v_tel,
                         v_decodesmstel, v_fax, v_sec_code,
                         v_clnt_occupation, v_domicile_countries,
                         v_accnt_no, v_proposer, v_status,
                         v_runoff, v_withdrawal_reason, v_remarks,
                         v_bank_acc_no, v_bbr_code, v_clnt_clnt_code,
                         v_spcl_terms, v_policy_cancelled,
                         v_increased_premium, v_declined_prop,
                         DECODE (v_cltn_client_types,
                                 'Staff', 'S',
                                 v_client_type
                                ),
                         v_acc_mngr_code, v_clnt_cntct_name_1,
                         v_clnt_cntct_phone_1, v_clnt_cntct_email_1,
                         v_clnt_cntct_name_2, v_clnt_cntct_phone_2,
                         v_clnt_cntct_email_2, v_passport_no,
                         v_sts_code, v_website, v_auditors,
                         v_parent_company, v_current_insurer,
                         v_projectedbiz, v_date_of_employ,
                         v_driving_licence, v_brn_code, v_clnt_image,
                         v_signature, v_clnt_acc_officer, v_gender,
                         v_clnt_occupation, v_clnt_bank_phone_no,
                         v_clnt_bank_cell_no, v_clnt_employer_phone_no,
                         v_clnt_employer_cell_no, v_clt_cell_no,
                         v_oldshtdesc, v_clnt_anniversary,
                         v_clnt_crdt_rating, v_cltn_client_types,
                         v_clnt_drv_experience, v_clnt_sacco,
                         v_clnt_credit_lim_allowed, v_clnt_credit_limit,
                         v_clnt_loc_code, v_clnt_occ_code,
                         v_clnt_bounced_chq, v_clnt_marital_status,
                         v_clnt_bpn_code, v_payroll, v_minsalary,
                         v_maxsalary, v_clnt_dl_issue_date,
                         v_work_permit, v_commmode,v_clnt_tel_pay,
                         v_clnt_email2,v_clnt_client_level
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error creating the client..');
         END;

           --  RAISE_ERROR('vclntcode ==='||vclntcode);
         --  -  --------------This should be revisted making sure this are passed as variables
         v_auth_auto :=
                tqc_parameters_pkg.get_param_varchar ('AUTHORISE_CLIENT_AUTO');

         BEGIN
            IF NVL (v_auth_auto, 'N') = 'Y'
            THEN
               tqc_clients_pkg.authorise_client (v_err, vclntcode, 'A');
            END IF;
         END;

         IF NVL (v_auth_auto, 'N') = 'Y'
         THEN
            BEGIN
               INSERT INTO tqc_client_systems
                           (csys_code, csys_clnt_code, csys_sys_code,
                            csys_wef, csys_wet
                           )
                    VALUES (tqc_csys_code_seq.NEXTVAL, vclntcode, 37,
                            SYSDATE, NULL
                           );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error asigning clients to GIS..');
            END;

            BEGIN
               INSERT INTO tqc_client_systems
                           (csys_code, csys_clnt_code, csys_sys_code,
                            csys_wef, csys_wet
                           )
                    VALUES (tqc_csys_code_seq.NEXTVAL, vclntcode, 1,
                            SYSDATE, NULL
                           );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error asigning clients to GIS..');
            END;
         END IF;

         INSERT INTO tqc_client_documents
                     (cdocr_code, cdocr_rdoc_code, cdocr_clnt_code,
                      cdocr_submited, cdocr_date_s, cdocr_ref_no, cdocr_rmrk,
                      cdocr_user_receivd)
            SELECT tqc_cdocr_code_seq.NEXTVAL, rdoc_code, vclntcode, 'N',
                   TRUNC (SYSDATE), NULL, NULL, v_user
              FROM tqc_required_documents
             WHERE NVL (roc_mandatory, 'N') = 'Y';

         RETURN (vclntcode);
      ELSIF v_add_edit = 'E'
      THEN                                                -- This is an UPDATE
         BEGIN
            -- RAISE_ERROR('v_clnt_marital_status'||v_clnt_marital_status||'Error creating the client..' ||v_clnt_code);
            UPDATE tqc_clients
               SET clnt_pin = v_pin,
                   --CLNT_SHT_DESC=v_sht_desc,
                   clnt_postal_addrs = v_postal_addrs,
                   clnt_physical_addrs = v_physical_addrs,
                   clnt_other_names =
                          LTRIM (RTRIM (v_first_name || ' ' || v_middle_name)),
                   --CLNT_OTHER_NAMES=v_middle_name,
                   clnt_name = LTRIM (RTRIM (v_surname)),
                   clnt_id_reg_no = v_id_reg_no,
                   --CLNT_DATE_CREATED,
                   --CLNT_CREATED_BY,CLNT_WEF,CLNT_WET,
                   clnt_direct_client = v_direct_clnt,
                   clnt_title = v_title,
                   clnt_dob = v_dob,
                   clnt_cou_code = v_cou_code,
                   clnt_twn_code = v_twn_code,
                   clnt_zip_code = v_zip_code,
                   clnt_email_addrs = v_email_addrs,
                   clnt_tel = v_tel,
                   clnt_sms_tel = v_decodesmstel,
                   clnt_fax = v_fax,
                   clnt_sec_code = v_sec_code,
                   clnt_business = v_clnt_occupation,
                   clnt_domicile_countries = v_domicile_countries,
                   clnt_accnt_no = v_accnt_no,
                   clnt_proposer = v_proposer,
                   clnt_status = v_status,
                   clnt_runoff = v_runoff,
                   clnt_withdrawal_reason = v_withdrawal_reason,
                   clnt_remarks = v_remarks,
                   clnt_bank_acc_no = v_bank_acc_no,
                   clnt_bbr_code = v_bbr_code,
                   clnt_clnt_code = v_clnt_clnt_code,
                   clnt_spcl_terms = v_spcl_terms,
                   clnt_policy_cancelled = v_policy_cancelled,
                   clnt_increased_premium = v_increased_premium,
                   clnt_declined_prop = v_declined_prop,
                   clnt_type = v_client_type,
                   clnt_cntct_name_1 = v_clnt_cntct_name_1,
                   clnt_cntct_phone_1 = v_clnt_cntct_phone_1,
                   clnt_cntct_email_1 = v_clnt_cntct_email_1,
                   clnt_cntct_name_2 = v_clnt_cntct_name_2,
                   clnt_cntct_phone_2 = v_clnt_cntct_phone_2,
                   clnt_cntct_email_2 = v_clnt_cntct_email_2,
                   clnt_passport_no = v_passport_no,
                   clnt_sts_code = v_sts_code,
                   clnt_website = v_website,
                   clnt_auditors = v_auditors,
                   clnt_parent_company = v_parent_company,
                   clnt_current_insurer = v_current_insurer,
                   clnt_projected_business = v_projectedbiz,
                   clnt_date_of_empl = v_date_of_employ,
                   clnt_driving_licence = v_driving_licence,
                   clnt_brn_code = v_brn_code,
                   clnt_image = NVL (v_clnt_image, clnt_image),
                   clnt_signature = NVL (v_signature, clnt_signature),
                   clnt_acc_officer = v_clnt_acc_officer,
                   clnt_gender = v_gender,
                   clnt_occupation = v_clnt_occupation,
                   clnt_bank_phone_no = v_clnt_bank_phone_no,
                   clnt_bank_cell_no = v_clnt_bank_cell_no,
                   clnt_employer_phone_no = v_clnt_employer_phone_no,
                   clnt_employer_cell_no = v_clnt_employer_cell_no,
                   clt_cell_no = v_clt_cell_no,
                   clnt_old_sht_desc = v_oldshtdesc,
                   clnt_anniversary = v_clnt_anniversary,
                   clnt_crdt_rating = v_clnt_crdt_rating,
                   clnt_drv_experience = v_clnt_drv_experience,
                   clnt_sacco = v_clnt_sacco,
                   clnt_reason_updated = v_clnt_reason_updated,
                   clnt_credit_lim_allowed = v_clnt_credit_lim_allowed,
                   clnt_credit_limit = v_clnt_credit_limit,
                   clnt_loc_code = v_clnt_loc_code,
                   clnt_occ_code = v_clnt_occ_code,
                   clnt_bounced_chq = v_clnt_bounced_chq,
                   clnt_marital_status = v_clnt_marital_status,
                   clnt_bpn_code = v_clnt_bpn_code,
                   clnt_payroll_no = v_payroll,
                   clnt_sal_min_range = v_minsalary,
                   clnt_sal_max_range = v_maxsalary,
                   clnt_dl_issue_date = v_clnt_dl_issue_date,
                   clnt_work_permit = v_work_permit,
                   clnt_default_comm_mode = v_commmode,
                   clnt_tel_pay = v_clnt_tel_pay,
                   clnt_email2 =v_clnt_email2,
                   clnt_client_level =v_clnt_client_level
             --  CLNT_CLNT_CODE=  v_clnt_clnt_code
            WHERE  clnt_code = v_clnt_code;

            update_proposer (v_clnt_code);

            SELECT v_clnt_code
              INTO vclntcode
              FROM DUAL;

            RETURN (v_clnt_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR UPDATING CLIENT...................');
         END;
      ELSIF v_add_edit = 'D'
      THEN
         --raise_error('v_updated_fields--->'||v_updated_fields   ||   'v_prev_values' ||v_prev_values);
         BEGIN
            -- Step 1: Remove the Client Documents
            DELETE FROM tqc_client_documents
                  WHERE cdocr_clnt_code = v_clnt_code;

            -- Step 2: Remove the Client Web Accounts
            DELETE FROM tqc_client_web_accounts
                  WHERE acwa_clnt_code = v_clnt_code;

            -- Step 3: Remove the Client  Accounts
            DELETE FROM tqc_client_accounts
                  WHERE clna_clnt_code = v_clnt_code;

            -- Step 4 Remove the Clients Systems
            DELETE FROM tqc_client_systems
                  WHERE csys_clnt_code = v_clnt_code;

            -- Step 5: Delete the Client
            DELETE FROM tqc_clients
                  WHERE clnt_code = v_clnt_code;

            RETURN (v_clnt_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR DELETING A CLIENT-----------');
         END;
      ELSIF v_add_edit = 'S'
      THEN
         BEGIN
            UPDATE tqc_clients
               SET clnt_status = v_status
             WHERE clnt_code = v_clnt_code;

            RETURN (v_clnt_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR AUTHORISING(REJECTING) CLIENT-----------');
         END;
      END IF;
   END;

  


  