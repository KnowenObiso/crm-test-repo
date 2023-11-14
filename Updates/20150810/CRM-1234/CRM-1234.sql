create table tqc_clients20150810 as select * from tqc_clients

declare

    cursor c_tel is
        select * from tqc_clients
        where CLNT_SMS_TEL like '+254+254%';
     v_old_tel      varchar2(50);
     v_new_tel      varchar2(50);   
begin
    for i in c_tel loop
        v_old_tel := i.CLNT_SMS_TEL;
        
        select REPLACE(i.CLNT_SMS_TEL, '+254+254', '+254')
        into v_new_tel
        from dual;
        
        update tqc_clients
        set CLNT_SMS_TEL=v_new_tel
        where CLNT_CODE =i.CLNT_CODE;
    
    
    end loop;
    commit;
end;


create table tqc_agencies20150810 as select * from tqc_agencies




declare

    cursor c_tel is
        select * from tqc_agencies
        where AGN_SMS_TEL like '+254+254%';
     v_old_tel      varchar2(50);
     v_new_tel      varchar2(50);   
begin
    for i in c_tel loop
        v_old_tel := i.AGN_SMS_TEL;
        
        select REPLACE(i.AGN_SMS_TEL, '+254+254', '+254')
        into v_new_tel
        from dual;
        
        update tqc_agencies
        set AGN_SMS_TEL=v_new_tel
        where AGN_CODE =i.AGN_CODE;
    
    
    end loop;
    commit;
end;


--------------------------------------------------------------------------
--tqc_clients_pkg.client_extended_proc

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
      v_commMode                  IN       VARCHAR2 DEFAULT NULL,
      v_payroll                   IN       VARCHAR2 DEFAULT NULL,
      v_minsalary                 IN       NUMBER DEFAULT NULL,
      v_maxsalary                 IN       NUMBER DEFAULT NULL,
      v_clnt_dl_issue_date        IN       DATE DEFAULT NULL,
      v_work_permit               IN       VARCHAR2 DEFAULT NULL,      
      v_clnt_bpn_code             IN       NUMBER DEFAULT NULL,
      v_clnt_client_level         IN       VARCHAR2 DEFAULT NULL
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
      v_sms_tel_count       NUMBER;

      CURSOR clnts
      IS
         SELECT *
           FROM tqc_clients
          WHERE clnt_code = v_clnt_code;
   BEGIN
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
      
    IF v_id_reg_no IS NOT NULL THEN 
     BEGIN
         SELECT NVL(COUNT(*),0) 
         INTO v_id_cnt
         FROM( SELECT CLNT_ID_REG_NO 
               FROM TQC_CLIENTS
               WHERE CLNT_ID_REG_NO = v_id_reg_no 
               AND CLNT_CODE <> v_clnt_code
         );
     EXCEPTION
         WHEN NO_DATA_FOUND THEN
             v_id_cnt := 0;
     END;           
   END IF;
      
   IF NVL(v_id_cnt ,0) >= 2   THEN
     RAISE_ERROR('Id number already exists....'||v_id_cnt||' times');
   END IF;

      IF v_sms_tel IS NOT NULL
      THEN
         SELECT INSTR(v_sms_tel,'+')
         INTO v_sms_tel_count
         FROM DUAL ;
         
         IF v_sms_tel_count = 0 THEN 
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
         ELSE
			v_decodesmstel := v_sms_tel;
         END IF;
      END IF;

        
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
                         CLNT_SAL_MAX_RANGE,CLNT_DL_ISSUE_DATE,CLNT_WORK_PERMIT,
                         CLNT_DEFAULT_COMM_MODE,CLNT_CLIENT_LEVEL
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
                         v_maxsalary,v_clnt_dl_issue_date,v_work_permit,v_commMode,v_clnt_client_level
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
                   CLNT_SAL_MIN_RANGE = v_minsalary,
                   clnt_sal_max_range = v_maxsalary,
                   clnt_dl_issue_date = v_clnt_dl_issue_date,
                CLNT_WORK_PERMIT=v_work_permit,
                CLNT_DEFAULT_COMM_MODE = v_commMode,
                CLNT_CLIENT_LEVEL=v_clnt_client_level
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
    
    ------------------------------------------------------------------------------------------------------------------------------------------
	--- tqc_setups_pkg.agencies_prc
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
      v_agn_bbr_code              IN   tqc_agencies.agn_bbr_code%TYPE   DEFAULT NULL,
      v_agn_bank_acc_no           IN   tqc_agencies.agn_bank_acc_no%TYPE DEFAULT NULL,
      v_agn_unique_prefix         IN   tqc_agencies.agn_unique_prefix%TYPE DEFAULT NULL,
      v_agn_state_code            IN   tqc_agencies.agn_state_code%TYPE DEFAULT NULL,
      v_agn_crdt_rting            IN   tqc_agencies.agn_crdt_rting%TYPE DEFAULT NULL,
      v_agn_subagent              IN   tqc_agencies.agn_subagent%TYPE  DEFAULT NULL,
      v_agn_main_agn_code         IN   tqc_agencies.agn_main_agn_code%TYPE  DEFAULT NULL,
      v_agn_clnt_code             IN   tqc_agencies.agn_clnt_code%TYPE  DEFAULT NULL,
      v_agn_account_manager       IN   tqc_agencies.agn_account_manager%TYPE  DEFAULT NULL,
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
     v_agnResetCode              IN   VARCHAR2
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
      v_agn_authorised      VARCHAR2(5);
      v_old_agn_sht_desc    VARCHAR2(60);
      v_sms_tel_count       NUMBER;
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
                         agn_vat_applicable, agn_whtax_applicable,agn_tel_pay
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
                         v_vat_appl, v_withtaxappl,v_agn_tel_pay
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
            
            RAISE_ERROR('AGENT SHORT DESCRIPTION SHOULD NOT BE EDITED FOR AN AUTHORIZED CLIENT REVERT TO ....'||v_agn_sht_desc);
                
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
                   agn_tel_pay = v_agn_tel_pay
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

    
   
 
  