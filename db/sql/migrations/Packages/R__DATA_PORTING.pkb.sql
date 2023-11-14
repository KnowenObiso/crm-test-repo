CREATE OR REPLACE PACKAGE BODY TQ_CRM."DATA_PORTING"
AS
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20033,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      END IF;
   END raise_error;

   PROCEDURE create_client (v_old_sht_desc              VARCHAR2,
                            v_surname                   VARCHAR2,
                            v_other_names               VARCHAR2,
                            v_title                     VARCHAR2,
                            v_marital_status            VARCHAR2,
                            v_sex                       VARCHAR2,
                            v_dob                       DATE,
                            v_pin                       VARCHAR2,
                            v_passport_no               VARCHAR2,
                            v_id_reg_doc                VARCHAR2,
                            v_id_reg_no                 VARCHAR2,
                            v_physical_addr             VARCHAR2,
                            v_postal_address            VARCHAR2,
                            v_town                      VARCHAR2,
                            v_country                   VARCHAR2,
                            v_email                     VARCHAR2,
                            v_tel                       VARCHAR2,
                            v_tel2                      VARCHAR2,
                            v_fax                       VARCHAR2,
                            v_acc_no                    VARCHAR2,
                            v_domicile_country          VARCHAR2,
                            v_business                  VARCHAR2,
                            v_zip_code                  VARCHAR2,
                            v_bbr_code                  NUMBER,
                            v_bank_acc_no               VARCHAR2,
                            v_payroll_no                VARCHAR2,
                            v_credit_allowed            VARCHAR2,
                            v_credit_limit              NUMBER,
                            v_comm_mode                 VARCHAR2,
                            v_payment_mode              VARCHAR2,
                            v_client_type               VARCHAR2,
                            v_prp_code              OUT NUMBER,
                            v_sht_desc           IN OUT VARCHAR2,
                            v_clnt_brn_code             NUMBER,
                            v_load_lms                  VARCHAR2)
   IS
      v_clnt_code         NUMBER;
      v_twn_code          NUMBER;
      v_cou_code          NUMBER;
      v_co_code           NUMBER;
      v_lc_code           NUMBER;
      v_domicile_ctry     NUMBER;
      v_direct_clnt_seq   NUMBER;
      v_direct_srl_fmt    tqc_account_types.act_id_serial_format%TYPE;
      v_accnt_no          VARCHAR2 (35);
      v_org_type          VARCHAR2 (35);
      v_division          VARCHAR2 (200);
      v_auth_auto           VARCHAR2 (1):='Y';
      v_div_code          NUMBER := NULL;

      CURSOR divisions_cur
      IS
         SELECT DIV_CODE, DIV_NAME FROM tqc_divisions;
   BEGIN
      IF NVL (v_load_lms, '*') = 'Y'
      THEN
         BEGIN
            SELECT MAX (co_code)
              INTO v_co_code
              FROM lms_class_occupations
             WHERE co_desc LIKE '%' || v_business || '%';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               BEGIN
                  SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                         || lms_co_code_seq.NEXTVAL
                    INTO v_co_code
                    FROM DUAL;

                  SELECT MIN (lc_code)
                    INTO v_lc_code
                    FROM lms_life_classes
                   WHERE NVL (lc_hazardous, 'N') = 'N';

                  INSERT INTO lms_class_occupations (co_code,
                                                     co_sht_desc,
                                                     co_desc,
                                                     co_hazardous,
                                                     co_lc_code)
                       VALUES (v_co_code,
                               'DATA_LOADING',
                               v_business,
                               'N',
                               v_lc_code);
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('LMS OCCUP...');
               END;
         END;
      END IF;


      BEGIN
         SELECT MAX (twn_code)
           INTO v_twn_code
           FROM tqc_towns
          WHERE twn_name LIKE '%' || v_town || '%';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      BEGIN
         IF v_div_code IS NOT NULL
         THEN
            SELECT div_name
              INTO v_division
              FROM tqc_divisions
             WHERE div_code = v_div_code;
         END IF;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      BEGIN
         SELECT MAX (cou_code)
           INTO v_cou_code
           FROM tqc_countries
          WHERE cou_name LIKE '%' || v_country || '%';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      BEGIN
         v_org_type := tqc_parameters_pkg.get_org_type (0);
      END;

      BEGIN
         SELECT MAX (cou_code)
           INTO v_domicile_ctry
           FROM tqc_countries
          WHERE cou_name LIKE '%' || v_domicile_country || '%';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            BEGIN
               SELECT MAX (cou_code)
                 INTO v_domicile_ctry
                 FROM tqc_countries
                WHERE cou_name LIKE '%NIGERIA%';
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
            END;
         WHEN OTHERS
         THEN
            NULL;
      END;

      IF v_sht_desc IS NULL
      THEN
         --**************FETCHING CLIENT SHT DESC**************
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

         BEGIN
            SELECT direct_clnt_seq.NEXTVAL INTO v_direct_clnt_seq FROM DUAL;

            v_accnt_no := LPAD (v_direct_clnt_seq, 6, 0);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error fetching the sequence...');
         END;

         BEGIN
            v_sht_desc :=
               tqc_sequences_pkg.get_number_format (
                  'C',
                  v_clnt_brn_code,
                  TO_CHAR (SYSDATE, 'RRRR'),
                  'I',
                  v_direct_srl_fmt);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('TQC CLIENT...');
         END;

         v_clnt_code := tqc_clnt_code_seq.NEXTVAL;

         --*******************************************
         BEGIN
            INSERT INTO tqc_clients (clnt_code,
                                     clnt_sht_desc,
                                     clnt_old_sht_desc,
                                     clnt_name,
                                     clnt_wef,
                                     clnt_other_names,
                                     clnt_id_reg_no,
                                     clnt_dob,
                                     clnt_pin,
                                     clnt_passport_no,
                                     clnt_physical_addrs,
                                     clnt_postal_addrs,
                                     clnt_twn_code,
                                     clnt_cou_code,
                                     clnt_email_addrs,
                                     clnt_tel,
                                     clnt_tel2,
                                     clnt_status,
                                     clnt_fax,
                                     clnt_proposer,
                                     clnt_accnt_no,
                                     clnt_domicile_countries,
                                     clnt_surname,
                                     clnt_type,
                                     clnt_title,
                                     clnt_business,
                                     clnt_zip_code,
                                     clnt_bbr_code,
                                     clnt_bank_acc_no,
                                     clnt_created_by,
                                     clnt_date_created,
                                     clnt_brn_code,
                                     clnt_crdt_allwd,
                                     clnt_crdt_limit,
                                     clnt_comm_mode,
                                     clnt_preffered_paymode)
                 VALUES (v_clnt_code,
                         v_sht_desc,
                         v_old_sht_desc,
                         NVL (v_surname, v_other_names),
                         SYSDATE,
                         v_other_names,
                         v_id_reg_no,
                         v_dob,
                         v_pin,
                         v_passport_no,
                         v_physical_addr,
                         v_postal_address,
                         v_twn_code,
                         v_cou_code,
                         v_email,
                         v_tel,
                         v_tel2,
                         'A',
                         v_fax,
                         'Y',
                         v_accnt_no,
                         v_domicile_ctry,
                         v_surname,
                         v_client_type,
                         v_title,
                         v_business,
                         v_zip_code,
                         v_bbr_code,
                         v_bank_acc_no,
                         'DATA_LOADING',
                         SYSDATE,
                         v_clnt_brn_code,
                         v_credit_allowed,
                         v_credit_limit,
                         v_comm_mode,
                         v_payment_mode);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('TQC CLIENT...');
         END;

IF NVL (v_auth_auto, 'N') = 'Y'
         THEN
            BEGIN
               INSERT INTO tqc_client_systems (csys_code,
                                               csys_clnt_code,
                                               csys_sys_code,
                                               csys_wef,
                                               csys_wet)
                    VALUES (tqc_csys_code_seq.NEXTVAL,
                            v_clnt_code,
                            37,
                            SYSDATE,
                            NULL);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error asigning clients to GIS..');
            END;

            BEGIN
               INSERT INTO tqc_client_systems (csys_code,
                                               csys_clnt_code,
                                               csys_sys_code,
                                               csys_wef,
                                               csys_wet)
                    VALUES (tqc_csys_code_seq.NEXTVAL,
                            v_clnt_code,
                            1,
                            SYSDATE,
                            NULL);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error asigning clients to FMS..');
            END;

            BEGIN
               INSERT INTO tqc_client_systems (csys_code,
                                               csys_clnt_code,
                                               csys_sys_code,
                                               csys_wef,
                                               csys_wet)
                    VALUES (tqc_csys_code_seq.NEXTVAL,
                            v_clnt_code,
                            27,
                            SYSDATE,
                            NULL);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error asigning clients to LMS..');
            END;
         END IF;

         --RAISE_ERROR('v_org_type: '||v_org_type);
         IF NVL (v_org_type, '*') = 'BRK'
         THEN
            FOR p IN divisions_cur
            LOOP
               tqc_clients_pkg.create_client_account (
                  'A',
                  NULL,
                  NULL,
                  NVL (TRIM (RTRIM (v_surname || ' ' || v_other_names)),
                       v_surname),
                  v_clnt_code,
                  'DATA_LOADING',
                  TRUNC (SYSDATE),
                  'ACTIVE',
                  NULL,
                  TRUNC (SYSDATE),
                  NULL,
                  p.div_code,
                  v_clnt_brn_code,
                  NULL,
                  NULL,
                  NULL,
                  NULL);
            END LOOP;
         END IF;

         IF NVL (v_load_lms, '*') = 'Y'
         THEN
            BEGIN
               v_prp_code :=
                     TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                  || lms_prp_code_seq.NEXTVAL;

               INSERT INTO lms_proposers (prp_code,
                                          prp_sht_desc,
                                          prp_pin,
                                          prp_postal_address,
                                          prp_town,
                                          prp_country,
                                          prp_dob,
                                          prp_proposer,
                                          prp_domicile_countries,
                                          prp_business,
                                          prp_tel,
                                          prp_tel2,
                                          prp_email,
                                          prp_other_names,
                                          prp_surname,
                                          prp_type,
                                          prp_fax,
                                          prp_id_reg_no,
                                          prp_id_reg_doc,
                                          prp_co_code,
                                          prp_sex,
                                          prp_bbr_code,
                                          prp_title,
                                          prp_marital_status,
                                          prp_bank_acc_no,
                                          prp_payroll_no,
                                          prp_physical_addr,
                                          prp_clnt_code,
                                          prp_class_type)
                    VALUES (v_prp_code,
                            v_sht_desc,
                            v_pin,
                            v_postal_address,
                            v_town,
                            v_country,
                            v_dob,
                            'Y',
                            v_domicile_country,
                            v_business,
                            v_tel,
                            v_tel2,
                            v_email,
                            v_other_names,
                            NVL (v_surname, v_other_names),
                            'BOTH',
                            v_fax,
                            v_id_reg_no,
                            v_id_reg_doc,
                            v_co_code,
                            v_sex,
                            v_bbr_code,
                            v_title,
                            v_marital_status,
                            v_bank_acc_no,
                            v_payroll_no,
                            v_physical_addr,
                            v_clnt_code,
                            'O');
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('LMS CLIENT...');
            END;
         END IF;
      ELSE
         BEGIN
            SELECT MAX (clnt_code)
              INTO v_clnt_code
              FROM tqc_clients
             WHERE TRIM (clnt_sht_desc) = v_sht_desc;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         IF v_clnt_code IS NULL
         THEN
            v_clnt_code := tqc_clnt_code_seq.NEXTVAL;

            BEGIN
               INSERT INTO tqc_clients (clnt_code,
                                        clnt_sht_desc,
                                        clnt_old_sht_desc,
                                        clnt_name,
                                        clnt_wef,
                                        clnt_other_names,
                                        clnt_id_reg_no,
                                        clnt_dob,
                                        clnt_pin,
                                        clnt_passport_no,
                                        clnt_physical_addrs,
                                        clnt_postal_addrs,
                                        clnt_twn_code,
                                        clnt_cou_code,
                                        clnt_email_addrs,
                                        clnt_tel,
                                        clnt_tel2,
                                        clnt_status,
                                        clnt_fax,
                                        clnt_proposer,
                                        clnt_accnt_no,
                                        clnt_domicile_countries,
                                        clnt_surname,
                                        clnt_type,
                                        clnt_title,
                                        clnt_business,
                                        clnt_zip_code,
                                        clnt_bbr_code,
                                        clnt_bank_acc_no,
                                        clnt_created_by,
                                        clnt_date_created,
                                        clnt_brn_code,
                                        clnt_crdt_allwd,
                                        clnt_crdt_limit,
                                        clnt_comm_mode,
                                        clnt_preffered_paymode)
                    VALUES (v_clnt_code,
                            v_sht_desc,
                            v_old_sht_desc,
                            NVL (v_surname, v_other_names),
                            SYSDATE,
                            v_other_names,
                            v_id_reg_no,
                            v_dob,
                            v_pin,
                            v_passport_no,
                            v_physical_addr,
                            v_postal_address,
                            v_twn_code,
                            v_cou_code,
                            v_email,
                            v_tel,
                            v_tel2,
                            'A',
                            v_fax,
                            'Y',
                            v_accnt_no,
                            v_domicile_ctry,
                            v_surname,
                            v_client_type,
                            v_title,
                            v_business,
                            v_zip_code,
                            v_bbr_code,
                            v_bank_acc_no,
                            'DATA_LOADING',
                            SYSDATE,
                            v_clnt_brn_code,
                            v_credit_allowed,
                            v_credit_limit,
                            v_comm_mode,
                            v_payment_mode);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('TQC CLIENT...');
            END;
            
            IF NVL (v_auth_auto, 'N') = 'Y'
         THEN
            BEGIN
               INSERT INTO tqc_client_systems (csys_code,
                                               csys_clnt_code,
                                               csys_sys_code,
                                               csys_wef,
                                               csys_wet)
                    VALUES (tqc_csys_code_seq.NEXTVAL,
                            v_clnt_code,
                            37,
                            SYSDATE,
                            NULL);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error asigning clients to GIS..');
            END;

            BEGIN
               INSERT INTO tqc_client_systems (csys_code,
                                               csys_clnt_code,
                                               csys_sys_code,
                                               csys_wef,
                                               csys_wet)
                    VALUES (tqc_csys_code_seq.NEXTVAL,
                            v_clnt_code,
                            1,
                            SYSDATE,
                            NULL);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error asigning clients to FMS..');
            END;

            BEGIN
               INSERT INTO tqc_client_systems (csys_code,
                                               csys_clnt_code,
                                               csys_sys_code,
                                               csys_wef,
                                               csys_wet)
                    VALUES (tqc_csys_code_seq.NEXTVAL,
                            v_clnt_code,
                            27,
                            SYSDATE,
                            NULL);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error asigning clients to LMS..');
            END;
         END IF;

            IF NVL (v_org_type, '*') = 'BRK'
            THEN
               FOR p IN divisions_cur
               LOOP
                  tqc_clients_pkg.create_client_account (
                     'A',
                     NULL,
                     NULL,
                     NVL (TRIM (RTRIM (v_surname || ' ' || v_other_names)),
                          v_surname),
                     v_clnt_code,
                     'DATA_LOADING',
                     TRUNC (SYSDATE),
                     'ACTIVE',
                     NULL,
                     TRUNC (SYSDATE),
                     NULL,
                     p.div_code,
                     v_clnt_brn_code,
                     NULL,
                     NULL,
                     NULL,
                     NULL);
               END LOOP;
            END IF;
         END IF;
    IF NVL (v_load_lms, '*') = 'Y'
     THEN
         BEGIN
            SELECT prp_code
              INTO v_prp_code
              FROM lms_proposers
             WHERE TRIM (prp_sht_desc) = v_sht_desc;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               BEGIN
                  SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                         || lms_prp_code_seq.NEXTVAL
                    INTO v_prp_code
                    FROM DUAL;

                  INSERT INTO lms_proposers (prp_code,
                                             prp_sht_desc,
                                             prp_pin,
                                             prp_postal_address,
                                             prp_town,
                                             prp_country,
                                             prp_dob,
                                             prp_proposer,
                                             prp_domicile_countries,
                                             prp_business,
                                             prp_tel,
                                             prp_tel2,
                                             prp_email,
                                             prp_other_names,
                                             prp_surname,
                                             prp_type,
                                             prp_fax,
                                             prp_id_reg_no,
                                             prp_id_reg_doc,
                                             prp_co_code,
                                             prp_sex,
                                             prp_bbr_code,
                                             prp_title,
                                             prp_marital_status,
                                             prp_bank_acc_no,
                                             prp_payroll_no,
                                             prp_physical_addr,
                                             prp_clnt_code,
                                             prp_class_type)
                       VALUES (v_prp_code,
                               v_sht_desc,
                               v_pin,
                               v_postal_address,
                               v_town,
                               v_country,
                               v_dob,
                               'Y',
                               v_domicile_country,
                               v_business,
                               v_tel,
                               v_tel2,
                               v_email,
                               v_other_names,
                               NVL (v_surname, v_other_names),
                               'BOTH',
                               v_fax,
                               v_id_reg_no,
                               v_id_reg_doc,
                               v_co_code,
                               v_sex,
                               v_bbr_code,
                               v_title,
                               v_marital_status,
                               v_bank_acc_no,
                               v_payroll_no,
                               v_physical_addr,
                               v_clnt_code,
                               'O');
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('LMS CLIENT2...');
               END;
            WHEN OTHERS
            THEN
               raise_error ('FETCH CLIENT PRP_CODE...');
         END;

         UPDATE lms_proposers
            SET prp_clnt_code = v_clnt_code
          WHERE prp_code = v_prp_code;
          
         END IF; 
      END IF;
   END create_client;

   PROCEDURE create_agents (v_agn_sht_desc           VARCHAR2,
                            v_name                   VARCHAR2,
                            v_brn_code               NUMBER,
                            v_act_code               NUMBER,
                            v_physical_address       VARCHAR2,
                            v_postal_address         VARCHAR2,
                            v_postal_code            VARCHAR2,
                            v_email                  VARCHAR2,
                            v_contact_person         VARCHAR2,
                            v_tel1                   VARCHAR2,
                            v_tel2                   VARCHAR2,
                            v_fax                    VARCHAR2,
                            v_acc_no                 VARCHAR2,
                            v_pin                    VARCHAR2,
                            v_agent_commission       NUMBER,
                            v_status                 VARCHAR2,
                            v_region_code            NUMBER,
                            v_town                   VARCHAR2,
                            v_country                VARCHAR2,
                            v_id_no                  VARCHAR2,
                            v_date_terminated        DATE,
                            v_date_joined            DATE,
                            v_bank_acc_no            VARCHAR2,
                            v_bank                   VARCHAR2,
                            v_reg_no                 VARCHAR2,
                            v_bru_code               NUMBER,
                            v_balance                NUMBER,
                            v_agn_code           OUT NUMBER)
   IS
      v_twn_code   NUMBER;
      v_cou_code   NUMBER;
      v_cnt        NUMBER;
   BEGIN
      BEGIN
         SELECT MAX (twn_code)
           INTO v_twn_code
           FROM tqc_towns
          WHERE twn_name LIKE '%' || v_town || '%';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      BEGIN
         SELECT MAX (cou_code)
           INTO v_cou_code
           FROM tqc_countries
          WHERE cou_name LIKE '%' || v_country || '%';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      v_cnt := 0;

      SELECT COUNT (1)
        INTO v_cnt
        FROM tqc_agencies
       WHERE agn_sht_desc = v_agn_sht_desc;

      IF NVL (v_cnt, 0) = 0
      THEN
         BEGIN
            SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'RRRR'))
                   || tq_crm.tqc_agn_code_seq.NEXTVAL
              INTO v_agn_code
              FROM DUAL;

            INSERT INTO tqc_agencies (agn_code,
                                      agn_sht_desc,
                                      agn_name,
                                      agn_brn_code,
                                      agn_act_code,
                                      agn_physical_address,
                                      agn_postal_address,
                                      agn_twn_code,
                                      agn_cou_code,
                                      agn_email_address,
                                      agn_contact_person,
                                      agn_tel1,
                                      agn_tel2,
                                      agn_fax,
                                      agn_acc_no,
                                      agn_pin,
                                      agn_agent_commission,
                                      agn_status,
                                      agn_date_created,
                                      agn_created_by,
                                      agn_reg_code,
                                      agn_comm_allowed,
                                      agn_town,
                                      agn_country,
                                      agn_id_no,
                                      agn_zip)
                 VALUES (v_agn_code,
                         v_agn_sht_desc,
                         v_name,
                         v_brn_code,
                         v_act_code,
                         v_physical_address,
                         v_postal_address,
                         v_twn_code,
                         v_cou_code,
                         v_email,
                         v_contact_person,
                         v_tel1,
                         v_tel2,
                         v_fax,
                         v_acc_no,
                         v_pin,
                         v_agent_commission,
                         v_status,
                         SYSDATE,
                         'DATA_LOADING',
                         v_region_code,
                         'Y',
                         v_town,
                         v_country,
                         v_id_no,
                         v_postal_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('TQC agent...');
         END;
      ELSE
         BEGIN
            SELECT agn_code
              INTO v_agn_code
              FROM tqc_agencies
             WHERE agn_sht_desc = v_agn_sht_desc;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('TQC agent fetch...');
         END;
      END IF;

      v_cnt := 0;

      SELECT COUNT (1)
        INTO v_cnt
        FROM lms_agencies
       WHERE agn_sht_desc = v_agn_sht_desc;

      IF NVL (v_cnt, 0) = 0
      THEN
         BEGIN
            INSERT INTO lms_agencies (agn_code,
                                      agn_sht_desc,
                                      agn_name,
                                      agn_act_code,
                                      agn_physical_address,
                                      agn_postal_address,
                                      agn_twn_code,
                                      agn_cou_code,
                                      agn_email_address,
                                      agn_contact_person,
                                      agn_tel1,
                                      agn_tel2,
                                      agn_fax,
                                      agn_acc_no,
                                      agn_pin,
                                      agn_agent_commission,
                                      agn_status,
                                      agn_date_created,
                                      agn_created_by,
                                      agn_reg_code,
                                      agn_comm_allowed,
                                      agn_id_no,
                                      agn_brn_code,
                                      agn_reg_number,
                                      agn_bru_code,
                                      agn_date_joined,
                                      agn_date_terminated,
                                      agn_bank_acc_no,
                                      agn_bank,
                                      agn_comm_payable)
                 VALUES (v_agn_code,
                         v_agn_sht_desc,
                         v_name,
                         v_act_code,
                         v_physical_address,
                         v_postal_address,
                         v_twn_code,
                         v_cou_code,
                         v_email,
                         v_contact_person,
                         v_tel1,
                         v_tel2,
                         v_fax,
                         v_acc_no,
                         v_pin,
                         v_agent_commission,
                         v_status,
                         SYSDATE,
                         'DATA_LOADING',
                         v_region_code,
                         'Y',
                         v_id_no,
                         v_brn_code,
                         v_reg_no,
                         v_bru_code,
                         v_date_joined,
                         v_date_terminated,
                         v_bank_acc_no,
                         v_bank,
                         'Y');
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('LMS agent...');
         END;
      ELSE
         SELECT agn_code
           INTO v_agn_code
           FROM lms_agencies
          WHERE agn_sht_desc = v_agn_sht_desc;
      END IF;
   END create_agents; 
   
   

   PROCEDURE call_create_client (v_load_lms VARCHAR2)
   IS
      CURSOR client_cur
      IS
         SELECT a.bank_acct_no,
                a.bank_branch,
                TO_NUMBER (NVL (a.branch_code, 0)) branch_code,
                a.client_bank_name,
                a.client_code,
                a.client_dob,
                a.client_name,
                a.client_title,
                a.comm_mode,
                a.country,
                a.credit_allowed,
                a.credit_limit,
                a.email,
                a.fax,
                a.gender,
                a.id_no,
                a.marital_status,
                a.occupation,
                a.passport_no,
                a.payment_mode,
                a.physical_address,
                a.pin_no,
                a.postal_address,
                a.telephone_no,
                a.town,
                --DECODE(GENDER,'M','Mr.','Mrs.') client_title,
                DECODE (a.gender, 'M', 'M', 'F') sex,
                SUBSTR (LTRIM (RTRIM (a.client_name)),
                        1,
                        INSTR (LTRIM (RTRIM (a.client_name)), ' ') - 1)
                   surname,
                SUBSTR (LTRIM (RTRIM (a.client_name)),
                        INSTR (LTRIM (RTRIM (a.client_name)), ' ') + 1,
                        LENGTH (LTRIM (RTRIM (a.client_name))))
                   othername,
                (SELECT CLNTY_TYPE
                   FROM TQ_CRM.tqc_client_types
                  WHERE UPPER (CLNTY_CLNT_TYPE) = UPPER (a.client_type))
                   client_type
           FROM tq_crm.client_dataload a
          WHERE client_code NOT IN (SELECT clnt_sht_desc
                                      FROM tqc_clients);

      v_prp_code   NUMBER;
      v_sht_desc   VARCHAR2 (100);
   BEGIN
      FOR p IN client_cur
      LOOP
         v_sht_desc := p.client_code;
         data_porting.create_client (p.client_code,
                                     NVL (p.surname, ' '), --v_surname VARCHAR2,
                                     p.othername,    --v_other_names VARCHAR2,
                                     p.client_title,       --v_title VARCHAR2,
                                     p.marital_status, --v_marital_status VARCHAR2,
                                     p.sex,                  --v_sex VARCHAR2,
                                     TO_DATE (p.client_dob, 'MM/DD/YYYY'),
                                     --v_dob DATE,
                                     p.pin_no,               --v_pin VARCHAR2,
                                     p.passport_no, -- v_PASSPORT_NO VARCHAR2,
                                     'ID',     --NULL,--v_id_reg_doc VARCHAR2,
                                     p.id_no,          --v_id_reg_no VARCHAR2,
                                     p.physical_address, --v_physical_addr VARCHAR2,
                                     p.postal_address, --v_postal_address VARCHAR2,
                                     p.town,                --v_town VARCHAR2,
                                     p.country,          --v_country VARCHAR2,
                                     p.email,              --v_email VARCHAR2,
                                     p.telephone_no,         --v_tel VARCHAR2,
                                     NULL,                  --v_tel2 VARCHAR2,
                                     p.fax,                  --v_fax VARCHAR2,
                                     p.bank_acct_no,    --v_accnt_no VARCHAR2,
                                     p.country, --v_domicile_country VARCHAR2,
                                     p.occupation,      --v_business VARCHAR2,
                                     NULL,              --v_zip_code VARCHAR2,
                                     NULL,                --v_bbr_code NUMBER,
                                     p.bank_acct_no, --v_bank_acc_no VARCHAR2,
                                     NULL,            --v_payroll_no VARCHAR2,
                                     p.credit_allowed,
                                     p.credit_limit,
                                     p.comm_mode,
                                     NVL (p.payment_mode, 'EFT'),
                                     p.client_type,
                                     v_prp_code,
                                     v_sht_desc,
                                     p.branch_code,
                                     v_load_lms);     --v_clnt_brn_code NUMBER
         COMMIT;
      END LOOP;
   END call_create_client;

--   PROCEDURE call_create_agent
--   IS
--      CURSOR AGENT
--      IS
--         SELECT agents_dataload.*
--           FROM agents_dataload
--          WHERE agent_name IS NOT NULL;

--      v_agn_code   NUMBER;
--      v_brn_code   NUMBER := NULL;
--   BEGIN
--      SELECT org_web_brn_code
--        INTO v_brn_code
--        FROM tqc_organizations
--       WHERE org_code = 2;

--      FOR a IN AGENT
--      LOOP
--         --         data_porting.create_agents
--         --                           (a.agent_code,
--         --                            --v_agn_sht_desc VARCHAR2,   TQC_BRANCHES   TQC_ACCOUNT_TYPES
--         --                            a.agent_name,                   --v_name VARCHAR2,
--         --                            v_brn_code,                   --v_brn_code NUMBER,
--         --                            2,--NVL(a.account_type,2),                           --v_act_code NUMBER,
--         --                            null,--a.physical_address, --v_physical_address VARCHAR2,
--         --                            a.postal_address,     --v_postal_address VARCHAR2,
--         --                            a.postal_code, --v_postal_code  VARCHAR2,
--         --                            a.email,                       --v_email VARCHAR2,
--         --                            a.contact_person,     --v_contact_person VARCHAR2,
--         --                            a.tel_no,                       --v_tel1 VARCHAR2,
--         --                            NULL,                           --v_tel2 VARCHAR2,
--         --                            a.fax,                        --v_fax VARCHAR2,
--         --                            NULL,                         --v_acc_no VARCHAR2,
--         --                            a.pin_no,                        --v_pin VARCHAR2,
--         --                            NULL,                 --v_agent_commission NUMBER,
--         --                            a.agent_status,                         --v_status VARCHAR2,
--         --                            3,                         --v_region_code NUMBER,
--         --                            a.town,                           --v_town VARCHAR2,
--         --                            a.country,                        --v_country VARCHAR2,
--         --                            a.id_no,                          --v_id_no VARCHAR2,
--         --                            TO_DATE (a.terminated_date, 'MM/DD/YYYY'),
--         --                            --v_date_terminated DATE,
--         --                            TO_DATE (a.joining_date, 'MM/DD/YYYY'),                        --v_date_joined DATE,
--         --                            NULL,                    --v_bank_acc_no VARCHAR2,
--         --                            NULL,                           --v_bank VARCHAR2,
--         --                            NULL,                         --v_reg_no VARCHAR2,
--         --                            NULL,                         --v_bru_code NUMBER,
--         --                            a.balance,
--         --                            v_agn_code
--         --                           );
--         COMMIT;
--      END LOOP;
--   END call_create_agent; 

END data_porting;
/
