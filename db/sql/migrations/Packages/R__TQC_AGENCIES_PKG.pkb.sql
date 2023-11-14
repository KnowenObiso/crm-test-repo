--
-- TQC_AGENCIES_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_agencies_pkg
AS
/****************************************TQC_AGENCY_DIRECTORS_PRC**************************************
   NAME:       TQC_AGENCIES_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        3/17/2010             1. Created this package body.
******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   FUNCTION getagentshtdesc (
      v_act_type         IN   VARCHAR2,
      v_brn_sht_desc     IN   VARCHAR2,
      v_agn_name         IN   VARCHAR2,
      v_direct_srl_fmt   IN   VARCHAR2
   )
      RETURN VARCHAR2
   IS
      v_agn_sht_desc      tqc_agencies.agn_sht_desc%TYPE;
      v_serial_length     NUMBER                           := 6;
      v_name_first_char   VARCHAR2 (20);
      v_no_of_chars       NUMBER                           := 1;
      v_agent_seq         NUMBER;
      v_agent_id          VARCHAR2 (50);
      v_count             NUMBER                           := 0;
   BEGIN
      BEGIN
         SELECT tqc_parameters_pkg.get_param_varchar ('AGN_ID_SERIAL_LENGTH')
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
         IF v_act_type IN ('A')
         THEN
            SELECT agent_id_a_seq.NEXTVAL
              INTO v_agent_seq
              FROM DUAL;
         ELSIF v_act_type IN ('IA')
         THEN
            SELECT agent_id_s_seq.NEXTVAL
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

         v_agent_id :=
            REPLACE (v_direct_srl_fmt,
                     '[SERIALNO]',
                     LPAD (v_agent_seq, v_serial_length, 0)
                    );
         v_agent_id := REPLACE (v_agent_id, '[FNAMEI]', v_name_first_char);
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

      v_agn_sht_desc := v_agent_id;
      RETURN v_agn_sht_desc;
   END;

   PROCEDURE tqc_agencies_prc (
      v_add_edit       IN       VARCHAR2,
      v_agencies_tab   IN       tqc_agencies_tab,
      v_user           IN       VARCHAR2,
      v_err            OUT      VARCHAR2
   )
   IS
      v_act_type         VARCHAR2 (2);
      v_created_by       VARCHAR2 (20);
      v_date_created     DATE;
      v_agn_sht_desc     VARCHAR2 (20);
      v_act_desc         VARCHAR2 (75);
      v_act_wthtx_rate   NUMBER;
      v_brn_sht_desc     tqc_branches.brn_sht_desc%TYPE;
      v_direct_srl_fmt   VARCHAR2 (30);
   BEGIN
      IF v_agencies_tab.COUNT = 0
      THEN
         raise_error ('No Agency data provided..');
      END IF;

      FOR i IN 1 .. v_agencies_tab.COUNT
      LOOP
         BEGIN
            SELECT RTRIM (LTRIM (act_id_serial_format)), act_type_sht_desc,
                   act_account_type, act_wthtx_rate
              INTO v_direct_srl_fmt, v_act_type,
                   v_act_desc, v_act_wthtx_rate
              FROM tqc_account_types
             WHERE act_code = v_agencies_tab (i).agn_act_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error getting Id format for account type ');
         --||v_agencies_tab(I).ACT_ACCOUNT_TYPE);
         END;

         IF v_add_edit = 'A'
         THEN
            IF v_direct_srl_fmt IS NULL
            THEN            --AND v_agencies_tab(I).AGN_SHT_DESC IS NULL THEN
               raise_error (   'Provide Id Format for  account type '
                            || v_act_desc
                            || '  OR use the button Account id to define the Id'
                           );
            END IF;

            IF v_agencies_tab (i).agn_name IS NULL
            THEN
               raise_error ('Agent Name cannot be null');
            END IF;

            v_created_by := v_user;
            v_date_created := TRUNC (SYSDATE);                 -- DATE CREATED
            v_agn_sht_desc := v_agencies_tab (i).agn_sht_desc;

            IF v_agn_sht_desc IS NULL
            THEN
               SELECT brn_sht_desc
                 INTO v_brn_sht_desc
                 FROM tqc_branches
                WHERE brn_code = v_agencies_tab (i).agn_brn_code;

               BEGIN
                  v_agn_sht_desc :=
                     getagentshtdesc (v_act_type,
                                      v_brn_sht_desc,
                                      v_agencies_tab (i).agn_name,
                                      v_direct_srl_fmt
                                     );
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('Error getting Agent Account ');
               END;
            END IF;

            INSERT INTO tqc_agencies
                        (agn_code,
                         agn_act_code, agn_sht_desc,
                         agn_name,
                         agn_physical_address,
                         agn_postal_address,
                         agn_twn_code,
                         agn_cou_code,
                         agn_email_address,
                         agn_web_address,
                         agn_zip,
                         agn_contact_person,
                         agn_contact_title,
                         agn_tel1,
                         agn_tel2,
                         agn_fax,
                         agn_acc_no,
                         agn_pin,
                         agn_agent_commission,
                         agn_credit_allowed,
                         agn_agent_wht_tax,
                         agn_print_dbnote,
                         agn_status, agn_date_created,
                         agn_created_by, agn_reg_code,
                         agn_comm_reserve_rate,
                         agn_annual_budget,
                         agn_status_eff_date,
                         agn_credit_period,
                         agn_comm_stat_eff_dt,
                         agn_comm_status_dt,
                         agn_comm_allowed,
                         agn_checked,
                         agn_checked_by,
                         agn_check_date,
                         agn_comp_comm_arrears,
                         agn_reinsurer,
                         agn_brn_code,
                         agn_town,
                         agn_country,
                         agn_status_desc,
                         agn_id_no,
                         agn_con_code,
                         agn_agn_code,
                         agn_sms_tel,
                         agn_ahc_code,
                         agn_sec_code,
                         agn_agnc_class_code,
                         agn_expiry_date,
                         agn_license_no,
                         agn_runoff,
                         agn_licensed,
                         agn_license_grace_pr,
                         agn_old_acc_no,
                         agn_status_remarks,
                         agn_vat_applicable,
                         agn_whtax_applicable
                        )
                 VALUES (tqc_agn_code_seq.NEXTVAL,
                         v_agencies_tab (i).agn_act_code, v_agn_sht_desc,
                         v_agencies_tab (i).agn_name,
                         v_agencies_tab (i).agn_physical_address,
                         v_agencies_tab (i).agn_postal_address,
                         v_agencies_tab (i).agn_twn_code,
                         v_agencies_tab (i).agn_cou_code,
                         v_agencies_tab (i).agn_email_address,
                         v_agencies_tab (i).agn_web_address,
                         v_agencies_tab (i).agn_zip,
                         v_agencies_tab (i).agn_contact_person,
                         v_agencies_tab (i).agn_contact_title,
                         v_agencies_tab (i).agn_tel1,
                         v_agencies_tab (i).agn_tel2,
                         v_agencies_tab (i).agn_fax,
                         v_agencies_tab (i).agn_acc_no,
                         v_agencies_tab (i).agn_pin,
                         v_agencies_tab (i).agn_agent_commission,
                         v_agencies_tab (i).agn_credit_allowed,
                         v_act_wthtx_rate,
                         v_agencies_tab (i).agn_print_dbnote,
                         v_agencies_tab (i).agn_status, v_date_created,
                         v_created_by, v_agencies_tab (i).agn_reg_code,
                         v_agencies_tab (i).agn_comm_reserve_rate,
                         v_agencies_tab (i).agn_annual_budget,
                         v_agencies_tab (i).agn_status_eff_date,
                         v_agencies_tab (i).agn_credit_period,
                         v_agencies_tab (i).agn_comm_stat_eff_dt,
                         v_agencies_tab (i).agn_comm_status_dt,
                         v_agencies_tab (i).agn_comm_allowed,
                         v_agencies_tab (i).agn_checked,
                         v_agencies_tab (i).agn_checked_by,
                         v_agencies_tab (i).agn_check_date,
                         v_agencies_tab (i).agn_comp_comm_arrears,
                         v_agencies_tab (i).agn_reinsurer,
                         v_agencies_tab (i).agn_brn_code,
                         v_agencies_tab (i).agn_town,
                         v_agencies_tab (i).agn_country,
                         v_agencies_tab (i).agn_status_desc,
                         v_agencies_tab (i).agn_id_no,
                         v_agencies_tab (i).agn_con_code,
                         v_agencies_tab (i).agn_agn_code,
                         v_agencies_tab (i).agn_sms_tel,
                         v_agencies_tab (i).agn_ahc_code,
                         v_agencies_tab (i).agn_sec_code,
                         v_agencies_tab (i).agn_agnc_class_code,
                         v_agencies_tab (i).agn_expiry_date,
                         v_agencies_tab (i).agn_license_no,
                         v_agencies_tab (i).agn_runoff,
                         v_agencies_tab (i).agn_licensed,
                         v_agencies_tab (i).agn_license_grace_pr,
                         v_agencies_tab (i).agn_old_acc_no,
                         v_agencies_tab (i).agn_status_remarks,
                         v_agencies_tab (i).agn_vat_applicable,
                         v_agencies_tab (i).agn_whtax_applicable
                        );
         ELSIF v_add_edit = 'E'
         THEN
            UPDATE tqc_agencies
               SET agn_act_code = v_agencies_tab (i).agn_act_code,
                   agn_sht_desc = v_agencies_tab (i).agn_sht_desc,
                   agn_name = v_agencies_tab (i).agn_name,
                   agn_physical_address =
                                       v_agencies_tab (i).agn_physical_address,
                   agn_postal_address = v_agencies_tab (i).agn_postal_address,
                   agn_twn_code = v_agencies_tab (i).agn_twn_code,
                   agn_cou_code = v_agencies_tab (i).agn_cou_code,
                   agn_email_address = v_agencies_tab (i).agn_email_address,
                   agn_web_address = v_agencies_tab (i).agn_web_address,
                   agn_zip = v_agencies_tab (i).agn_zip,
                   agn_contact_person = v_agencies_tab (i).agn_contact_person,
                   agn_contact_title = v_agencies_tab (i).agn_contact_title,
                   agn_tel1 = v_agencies_tab (i).agn_tel1,
                   agn_tel2 = v_agencies_tab (i).agn_tel2,
                   agn_fax = v_agencies_tab (i).agn_fax,
                   agn_acc_no = v_agencies_tab (i).agn_acc_no,
                   agn_pin = v_agencies_tab (i).agn_pin,
                   agn_agent_commission =
                                       v_agencies_tab (i).agn_agent_commission,
                   agn_credit_allowed = v_agencies_tab (i).agn_credit_allowed,
                   agn_agent_wht_tax = v_agencies_tab (i).agn_agent_wht_tax,
                   agn_print_dbnote = v_agencies_tab (i).agn_print_dbnote,
                   agn_status = v_agencies_tab (i).agn_status,
                   agn_date_created = v_agencies_tab (i).agn_date_created,
                   agn_created_by = v_agencies_tab (i).agn_created_by,
                   agn_reg_code = v_agencies_tab (i).agn_reg_code,
                   agn_comm_reserve_rate =
                                      v_agencies_tab (i).agn_comm_reserve_rate,
                   agn_annual_budget = v_agencies_tab (i).agn_annual_budget,
                   agn_status_eff_date =
                                        v_agencies_tab (i).agn_status_eff_date,
                   agn_credit_period = v_agencies_tab (i).agn_credit_period,
                   agn_comm_stat_eff_dt =
                                       v_agencies_tab (i).agn_comm_stat_eff_dt,
                   agn_comm_status_dt = v_agencies_tab (i).agn_comm_status_dt,
                   agn_comm_allowed = v_agencies_tab (i).agn_comm_allowed,
                   agn_checked = v_agencies_tab (i).agn_checked,
                   agn_checked_by = v_agencies_tab (i).agn_checked_by,
                   agn_check_date = v_agencies_tab (i).agn_check_date,
                   agn_comp_comm_arrears =
                                      v_agencies_tab (i).agn_comp_comm_arrears,
                   agn_reinsurer = v_agencies_tab (i).agn_reinsurer,
                   agn_brn_code = v_agencies_tab (i).agn_brn_code,
                   agn_town = v_agencies_tab (i).agn_town,
                   agn_country = v_agencies_tab (i).agn_country,
                   agn_status_desc = v_agencies_tab (i).agn_status_desc,
                   agn_id_no = v_agencies_tab (i).agn_id_no,
                   agn_con_code = v_agencies_tab (i).agn_con_code,
                   agn_agn_code = v_agencies_tab (i).agn_agn_code,
                   agn_sms_tel = v_agencies_tab (i).agn_sms_tel,
                   agn_ahc_code = v_agencies_tab (i).agn_ahc_code,
                   agn_sec_code = v_agencies_tab (i).agn_sec_code,
                   agn_agnc_class_code =
                                        v_agencies_tab (i).agn_agnc_class_code,
                   agn_expiry_date = v_agencies_tab (i).agn_expiry_date,
                   agn_license_no = v_agencies_tab (i).agn_license_no,
                   agn_runoff = v_agencies_tab (i).agn_runoff,
                   agn_licensed = v_agencies_tab (i).agn_licensed,
                   agn_license_grace_pr =
                                       v_agencies_tab (i).agn_license_grace_pr,
                   agn_old_acc_no = v_agencies_tab (i).agn_old_acc_no,
                   agn_status_remarks = v_agencies_tab (i).agn_status_remarks,
                   agn_vat_applicable = v_agencies_tab (i).agn_vat_applicable,
                   agn_whtax_applicable =
                                       v_agencies_tab (i).agn_whtax_applicable
             WHERE agn_code = v_agencies_tab (i).agn_code;
         ELSIF v_add_edit = 'D'
         THEN
            DELETE      tqc_agencies
                  WHERE agn_code = v_agencies_tab (i).agn_code;
         END IF;
      END LOOP;
   END;

   PROCEDURE tqc_agency_directors_prc (
      v_add_edit               IN       VARCHAR2,
      v_agency_directors_tab   IN       tqc_agency_directors_tab,
      v_err                    OUT      VARCHAR2
   )
   IS
   BEGIN
      IF v_agency_directors_tab.COUNT = 0
      THEN
         raise_error ('No Agency data provided..');
      END IF;

      FOR i IN 1 .. v_agency_directors_tab.COUNT
      LOOP
         IF v_add_edit IN ('A', 'E')
         THEN
            IF v_agency_directors_tab (i).adir_year IS NULL
            THEN
               raise_error ('Year not specified');
            ELSIF v_agency_directors_tab (i).adir_name IS NULL
            THEN
               raise_error (' Direcotor''s name not specified');
            END IF;
         END IF;

         IF v_add_edit = 'A'
         THEN
            INSERT INTO tqc_agency_directors
                        (adir_code,
                         adir_agn_code,
                         adir_year,
                         adir_name,
                         adir_qualifications,
                         adir_pct_holdg,
                         adir_designation,
                         adir_phone_number,
                         adir_principle,
                         adir_cou_code
                        )
                 VALUES (tqc_adir_code_seq.NEXTVAL,
                         v_agency_directors_tab (i).adir_agn_code,
                         v_agency_directors_tab (i).adir_year,
                         v_agency_directors_tab (i).adir_name,
                         v_agency_directors_tab (i).adir_qualifications,
                         v_agency_directors_tab (i).adir_pct_holdg,
                         v_agency_directors_tab (i).adir_designation,
                         v_agency_directors_tab (i).adir_phone_number,
                         v_agency_directors_tab (i).adir_principle_dir,                         
                         v_agency_directors_tab (i).ADIR_NATIONALITY
                        );
         ELSIF v_add_edit = 'E'
         THEN
            --  RAISE_ERROR('v_agency_directors_tab (i).adir_principle_dir'||v_agency_directors_tab (i).adir_principle_dir);
            UPDATE tqc_agency_directors
               SET adir_agn_code = v_agency_directors_tab (i).adir_agn_code,
                   adir_year = v_agency_directors_tab (i).adir_year,
                   adir_name = v_agency_directors_tab (i).adir_name,
                   adir_qualifications = v_agency_directors_tab (i).adir_qualifications,
                   adir_pct_holdg = v_agency_directors_tab (i).adir_pct_holdg,
                   adir_designation =  v_agency_directors_tab (i).adir_designation,
                   adir_phone_number = v_agency_directors_tab (i).adir_phone_number,
                   adir_principle = v_agency_directors_tab (i).adir_principle_dir,
                   adir_cou_code=v_agency_directors_tab (i).ADIR_NATIONALITY
             WHERE adir_code = v_agency_directors_tab (i).adir_code;
         ELSIF v_add_edit = 'D'
         THEN
            DELETE      tqc_agency_directors
                  WHERE adir_code = v_agency_directors_tab (i).adir_code;
         END IF;
      END LOOP;
   END;

   PROCEDURE tqc_agency_registration_prc (
      v_add_edit                  IN       VARCHAR2,
      v_agency_registration_tab   IN       tqc_agency_registration_tab,
      v_err                       OUT      VARCHAR2
   )
   IS
   BEGIN
      IF v_agency_registration_tab.COUNT = 0
      THEN
         raise_error ('No Agency data provided..');
      END IF;

      FOR i IN 1 .. v_agency_registration_tab.COUNT
      LOOP
         IF v_add_edit IN ('A', 'E')
         THEN
            IF v_agency_registration_tab (i).areg_year IS NULL
            THEN
               raise_error (' Year of registration not specified');
            ELSIF v_agency_registration_tab (i).areg_reg_no IS NULL
            THEN
               raise_error (' Registration number not specified');
            ELSIF v_agency_registration_tab (i).areg_wef IS NULL
            THEN
               raise_error (' Effective date of registration not specified');
            ELSIF v_agency_registration_tab (i).areg_wet IS NULL
            THEN
               raise_error (' Last date of registration not specified');
            ELSIF v_agency_registration_tab (i).areg_accepted IS NULL
            THEN
               raise_error (' Registration acceptance not specified');
            ELSIF v_agency_registration_tab (i).areg_wef >
                                        v_agency_registration_tab (i).areg_wet
            THEN
               raise_error (' Invalid range of registration dates entered');
            END IF;
         END IF;

         IF v_add_edit = 'A'
         THEN
            INSERT INTO tqc_agency_registration
                        (areg_code,
                         areg_agn_code,
                         areg_year,
                         areg_reg_no,
                         areg_wef,
                         areg_wet,
                         areg_accepted
                        )
                 VALUES (tqc_areg_code_seq.NEXTVAL,
                         v_agency_registration_tab (i).areg_agn_code,
                         v_agency_registration_tab (i).areg_year,
                         v_agency_registration_tab (i).areg_reg_no,
                         v_agency_registration_tab (i).areg_wef,
                         v_agency_registration_tab (i).areg_wet,
                         v_agency_registration_tab (i).areg_accepted
                        );
         ELSIF v_add_edit = 'E'
         THEN
            UPDATE tqc_agency_registration
               SET areg_agn_code = v_agency_registration_tab (i).areg_agn_code,
                   areg_year = v_agency_registration_tab (i).areg_year,
                   areg_reg_no = v_agency_registration_tab (i).areg_reg_no,
                   areg_wef = v_agency_registration_tab (i).areg_wef,
                   areg_wet = v_agency_registration_tab (i).areg_wet,
                   areg_accepted = v_agency_registration_tab (i).areg_accepted
             WHERE areg_code = v_agency_registration_tab (i).areg_code;
         ELSIF v_add_edit = 'D'
         THEN
            DELETE      tqc_agency_registration
                  WHERE areg_code = v_agency_registration_tab (i).areg_code;
         END IF;
      END LOOP;
   END;

   PROCEDURE tqc_agency_referees_prc (
      v_add_edit              IN       VARCHAR2,
      v_agency_referees_tab   IN       tqc_agency_referees_tab,
      v_err                   OUT      VARCHAR2
   )
   IS
   BEGIN
      IF v_agency_referees_tab.COUNT = 0
      THEN
         raise_error ('No Agency data provided..');
      END IF;

      FOR i IN 1 .. v_agency_referees_tab.COUNT
      LOOP
         IF v_add_edit = 'A'
         THEN
            INSERT INTO tqc_agency_referees
                        (aref_code,
                         aref_name,
                         aref_physical_address,
                         aref_postal_address,
                         aref_twn_code,
                         aref_cou_code,
                         aref_email_address,
                         aref_tel,
                         aref_id_no,
                         aref_agn_code
                        )
                 VALUES (tqc_agency_ref_seq.NEXTVAL,
                         v_agency_referees_tab (i).aref_name,
                         v_agency_referees_tab (i).aref_physical_address,
                         v_agency_referees_tab (i).aref_postal_address,
                         v_agency_referees_tab (i).aref_twn_code,
                         v_agency_referees_tab (i).aref_cou_code,
                         v_agency_referees_tab (i).aref_email_address,
                         v_agency_referees_tab (i).aref_tel,
                         v_agency_referees_tab (i).aref_id_no,
                         v_agency_referees_tab (i).aref_agn_code
                        );
         ELSIF v_add_edit = 'E'
         THEN
            UPDATE tqc_agency_referees
               SET aref_name = v_agency_referees_tab (i).aref_name,
                   aref_physical_address =
                               v_agency_referees_tab (i).aref_physical_address,
                   aref_postal_address =
                                 v_agency_referees_tab (i).aref_postal_address,
                   aref_twn_code = v_agency_referees_tab (i).aref_twn_code,
                   aref_cou_code = v_agency_referees_tab (i).aref_cou_code,
                   aref_email_address =
                                  v_agency_referees_tab (i).aref_email_address,
                   aref_tel = v_agency_referees_tab (i).aref_tel,
                   aref_id_no = v_agency_referees_tab (i).aref_id_no,
                   aref_agn_code = v_agency_referees_tab (i).aref_agn_code
             WHERE aref_code = v_agency_referees_tab (i).aref_code;
         ELSIF v_add_edit = 'D'
         THEN
            DELETE      tqc_agency_referees
                  WHERE aref_code = v_agency_referees_tab (i).aref_code;
         END IF;
      END LOOP;
   END;

   PROCEDURE grant_agent_system (
      v_add_edit   IN   VARCHAR2,
      v_agn_code   IN   NUMBER,
      v_sys_code   IN   NUMBER,
      v_wef        IN   DATE,
      v_wet        IN   DATE,
      v_comment    IN   VARCHAR2
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         IF NVL (v_wef, TRUNC (SYSDATE)) < TRUNC (SYSDATE)
         THEN
            raise_error ('Effective date can not be before today..');
         END IF;

--        if v_wef is null then
--          RAISE_ERROR (' WEF date not specified');
         IF v_wet IS NOT NULL
         THEN
            IF v_wef > v_wet
            THEN
               raise_error (' Invalid WEF date WET date range');
            END IF;
         END IF;

         INSERT INTO tqc_agency_systems
                     (asys_sys_code, asys_agn_code, asys_wef,
                      asys_wet, asys_comment
                     )
              VALUES (v_sys_code, v_agn_code, NVL (v_wef, TRUNC (SYSDATE)),
                      v_wet, v_comment
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_agency_systems
            SET asys_wet = v_wet,
                asys_comment = v_comment
          WHERE asys_sys_code = v_sys_code AND asys_agn_code = v_agn_code;
      END IF;
   END;

   PROCEDURE revoke_agent_system (v_agn_code IN NUMBER, v_sys_code IN NUMBER)
   IS
   BEGIN
      DELETE FROM tqc_agency_systems
            WHERE asys_sys_code = v_sys_code AND asys_agn_code = v_agn_code;
   END;

   PROCEDURE tqc_account_contacts_prc (
      v_add_edit               IN       VARCHAR2,
      v_account_contacts_tab   IN       tqc_account_contacts_tab,
      v_user                   IN       VARCHAR2,
      v_err                    OUT      VARCHAR2,
      v_accc_sys_code          IN       NUMBER
   )
   IS
      v_accc_pwd   VARCHAR2 (75);
   BEGIN
      IF v_account_contacts_tab.COUNT = 0
      THEN
         raise_error ('No Agency data provided..');
      END IF;

      FOR i IN 1 .. v_account_contacts_tab.COUNT
      LOOP
         IF v_add_edit = 'A'
         THEN
            IF LENGTH (v_account_contacts_tab (i).accc_pwd) <= 15
            THEN
               v_accc_pwd :=
                           gis_read.val (v_account_contacts_tab (i).accc_pwd);
            ELSE
               raise_error
                   (' Password may only be upt to a maximum of 15 characters');
            END IF;

            INSERT INTO tqc_account_contacts
                        (accc_code,
                         accc_acc_code,
                         accc_name,
                         accc_other_names,
                         accc_dob,
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
                         v_account_contacts_tab (i).accc_agn_code,
                         v_account_contacts_tab (i).accc_name,
                         v_account_contacts_tab (i).accc_other_names,
                         v_account_contacts_tab (i).accc_dob,
                         v_account_contacts_tab (i).accc_tel,
                         v_account_contacts_tab (i).accc_email_addr,
                         v_account_contacts_tab (i).accc_sms_tel,
                         v_account_contacts_tab (i).accc_username,
                         /*v_account_contacts_tab(I).ACCC_PWD*/
                         v_accc_pwd,
                         v_account_contacts_tab (i).accc_login_allowed,
                         SYSDATE, v_account_contacts_tab (i).accc_pwd_reset,
                         SYSDATE, v_account_contacts_tab (i).accc_status,
                         v_account_contacts_tab (i).accc_login_attempts,
                         v_account_contacts_tab (i).accc_personel_rank,
                         v_account_contacts_tab (i).accc_last_login_date,
                         v_account_contacts_tab (i).accc_created_by,
                         v_accc_sys_code
                        );
         ELSIF v_add_edit = 'E'
         THEN
            UPDATE tqc_account_contacts
               SET accc_acc_code = v_account_contacts_tab (i).accc_agn_code,
                   accc_name = v_account_contacts_tab (i).accc_name,
                   accc_other_names =
                                   v_account_contacts_tab (i).accc_other_names,
                   accc_dob = v_account_contacts_tab (i).accc_dob,
                   accc_tel = v_account_contacts_tab (i).accc_tel,
                   accc_email_addr =
                                    v_account_contacts_tab (i).accc_email_addr,
                   accc_sms_tel = v_account_contacts_tab (i).accc_sms_tel,
                   accc_username = v_account_contacts_tab (i).accc_username,
                   accc_pwd = v_accc_pwd,
                   --v_account_contacts_tab(I).ACCC_PWD,
                   accc_login_allowed =
                                 v_account_contacts_tab (i).accc_login_allowed,
                   accc_pwd_changed = SYSDATE,
                   accc_pwd_reset = v_account_contacts_tab (i).accc_pwd_reset,
                   accc_status = v_account_contacts_tab (i).accc_status,
                   accc_login_attempts =
                                v_account_contacts_tab (i).accc_login_attempts,
                   accc_personel_rank =
                                 v_account_contacts_tab (i).accc_personel_rank,
                   accc_last_login_date =
                               v_account_contacts_tab (i).accc_last_login_date,
                   accc_created_by =
                                    v_account_contacts_tab (i).accc_created_by,
                   accc_sys_code = v_accc_sys_code
             WHERE accc_code = v_account_contacts_tab (i).accc_code;
         ELSIF v_add_edit = 'D'
         THEN
            BEGIN
               UPDATE tqc_account_contacts
                  SET accc_status = 'I'
                WHERE accc_code = v_account_contacts_tab (i).accc_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error (' User could not be deactivated');
            END;
         END IF;
      END LOOP;
   END;

   PROCEDURE change_agn_acc_password (v_acc_code IN NUMBER, v_new_pwd IN NUMBER)
   IS
   BEGIN
      IF LENGTH (v_new_pwd) <= 15
      THEN
         BEGIN
            UPDATE tqc_account_contacts
               SET accc_pwd = gis_read.val (v_new_pwd)
             WHERE accc_code = v_acc_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (' User account could not be restored');
         END;
      ELSE
         raise_error
                   (' Password may only be upt to a maximum of 15 characters');
      END IF;
   END;

   PROCEDURE change_agn_sht_desc (
      v_agn_code       IN   NUMBER,
      v_agn_sht_desc   IN   VARCHAR2,
      v_agn_act_code   IN   NUMBER,
      v_new_sht_desc   IN   VARCHAR2,
      v_user           IN   VARCHAR2
   )
   IS
      al_id         NUMBER;
      v_dup_cnt     NUMBER;
      v_authorize   BOOLEAN := FALSE;
   BEGIN
      IF v_agn_sht_desc IS NULL
      THEN
         UPDATE tqc_agencies
            SET agn_sht_desc = v_new_sht_desc
          WHERE agn_code = v_agn_code;
      ELSIF v_new_sht_desc IS NULL
      THEN
         raise_error ('Please the new Account to change to.');
      ELSE
         --check if unique
         SELECT COUNT (1)
           INTO v_dup_cnt
           FROM tqc_agencies
          WHERE agn_act_code = v_agn_act_code
                AND agn_sht_desc = v_new_sht_desc;

         IF NVL (v_dup_cnt, 0) > 0
         THEN
            raise_error
               ('The new account provided is already assigned. Please specify a different account...'
               );
         ELSE
            BEGIN
               v_authorize :=
                  tqc_interfaces_pkg.check_authorization_roles (v_user,
                                                                37,
                                                                'AGN (ID)',
                                                                0,
                                                                'N',
                                                                NULL
                                                               );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error checking user authorization rights');
            END;

            IF v_authorize = FALSE
            THEN
               raise_error
                  ('You Do Not Have Enough Rights to Change Agent Description.'
                  );
            ELSE
               BEGIN
                  gis_utilities.change_agent_sht_desc (v_agn_code,
                                                       v_new_sht_desc
                                                      );
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('ERROR TRANSFERING ACCOUNT RECORDS...');
               END;
            END IF;
         END IF;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error defining/changing account id...');
   END;

   FUNCTION get_agency_accounts (v_agency_code IN NUMBER)
      RETURN agency_accounts_ref
   IS
      v_retcur   agency_accounts_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT aga_code, aga_sht_desc, aga_name, aga_agn_code,
                aga_created_by, aga_date_created, aga_status, aga_remarks,
                aga_wef, aga_wet, div_name, div_code
           FROM tqc_agency_accounts, tqc_divisions
          WHERE aga_agn_code = v_agency_code 
         AND (aga_div_code = div_code(+) 
         --or aga_div_code is null
         );

      RETURN (v_retcur);
   END;

   PROCEDURE create_agency_account (
      v_add_edit       VARCHAR2,
      v_acc_code       NUMBER,
      v_sht_desc       VARCHAR2,
      v_name           VARCHAR2,
      v_agn_code       NUMBER,
      v_created_by     VARCHAR2,
      v_date_created   DATE,
      v_status         VARCHAR2,
      v_remarks        VARCHAR2,
      v_wef            DATE,
      v_wet            DATE,
      v_div_code       NUMBER
   )
   IS
      v_srl_fmt         VARCHAR2 (100);
      v_act_code        NUMBER;
      v_act_type        VARCHAR2 (10);
      v_brn_code        NUMBER;
      v_brn_sht_desc    VARCHAR2 (15);
      v_count           NUMBER;
      v_serial_length   NUMBER;
      v_agent_id        VARCHAR2 (30);
      v_agent_seq       NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT agn_act_code, agn_brn_code
              INTO v_act_code, v_brn_code
              FROM tqc_agencies
             WHERE agn_code = v_agn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error fetching agency details...');
         END;

         BEGIN
            SELECT brn_sht_desc
              INTO v_brn_sht_desc
              FROM tqc_branches
             WHERE brn_code = v_brn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error fetching agency branch details...');
         END;

         BEGIN
            SELECT RTRIM (LTRIM (act_format)), act_type_id
              INTO v_srl_fmt, v_act_type
              FROM tqc_account_types
             WHERE act_code = v_act_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error getting Account format for account type ');
         END;

         BEGIN
            SELECT tqc_parameters_pkg.get_param_varchar
                                                     ('DEBTOR_SERIALNO_LENGTH')
              INTO v_serial_length
              FROM DUAL;
         EXCEPTION
            WHEN OTHERS
            THEN
               NULL;
         END;

         IF v_srl_fmt IS NULL
         THEN
            raise_error ('Provide account Format for  account type ');
         END IF;

         BEGIN
            IF v_act_type IN ('A', 'IA')
            THEN
               SELECT agent_ac_a_seq.NEXTVAL
                 INTO v_agent_seq
                 FROM DUAL;
            ELSIF v_act_type = 'B'
            THEN
               SELECT agent_ac_b_seq.NEXTVAL
                 INTO v_agent_seq
                 FROM DUAL;
            ELSIF v_act_type = 'D'
            THEN
               SELECT agent_ac_d_seq.NEXTVAL
                 INTO v_agent_seq
                 FROM DUAL;
            ELSIF v_act_type = 'I'
            THEN
               SELECT agent_ac_i_seq.NEXTVAL
                 INTO v_agent_seq
                 FROM DUAL;
            ELSIF v_act_type = 'R'
            THEN
               SELECT agent_ac_r_seq.NEXTVAL
                 INTO v_agent_seq
                 FROM DUAL;
            ELSE
               SELECT agent_ac_seq.NEXTVAL
                 INTO v_agent_seq
                 FROM DUAL;
            END IF;

            v_agent_id :=
               REPLACE (v_srl_fmt,
                        '[SERIALNO]',
                        LPAD (v_agent_seq, v_serial_length, 0)
                       );
            v_agent_id := REPLACE (v_agent_id, '[BRN]', v_brn_sht_desc);
            v_agent_id :=
               REPLACE (v_agent_id,
                        '[YEAR]',
                        TO_CHAR (TO_DATE (SYSDATE), 'RRRR')
                       );

            BEGIN
               SELECT COUNT (1)
                 INTO v_count
                 FROM tqc_agency_accounts
                WHERE aga_sht_desc = v_agent_id;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error Checking duplicate Agent Account');
            END;

            IF NVL (v_count, 0) <> 0
            THEN
               raise_error ('An Agent with the same account exist');
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error fetching the sequence...');
         END;

         BEGIN
            IF v_sht_desc IS NULL
            THEN
               INSERT INTO tqc_agency_accounts
                           (aga_code, aga_sht_desc, aga_name,
                            aga_agn_code, aga_created_by, aga_date_created,
                            aga_status, aga_remarks, aga_wef, aga_wet,
                            aga_div_code
                           )
                    VALUES (tqc_aga_code_seq.NEXTVAL, v_agent_id, v_name,
                            v_agn_code, v_created_by, v_date_created,
                            v_status, v_remarks, v_wef, v_wet,
                            v_div_code
                           );

               COMMIT;
            ELSE
               INSERT INTO tqc_agency_accounts
                           (aga_code, aga_sht_desc, aga_name,
                            aga_agn_code, aga_created_by, aga_date_created,
                            aga_status, aga_remarks, aga_wef, aga_wet,
                            aga_div_code
                           )
                    VALUES (tqc_aga_code_seq.NEXTVAL, v_sht_desc, v_name,
                            v_agn_code, v_created_by, v_date_created,
                            v_status, v_remarks, v_wef, v_wet,
                            v_div_code
                           );

               COMMIT;
            END IF;
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_agency_accounts
               SET aga_sht_desc = v_sht_desc,
                   aga_name = v_name,
                   aga_agn_code = v_agn_code,
                   aga_created_by = v_created_by,
                   aga_date_created = v_date_created,
                   aga_status = v_status,
                   aga_remarks = v_remarks,
                   aga_wef = v_wef,
                   aga_wet = v_wet,
                   aga_div_code = v_div_code
             WHERE aga_code = v_acc_code;
         END;
      ELSE
         DELETE FROM tqc_agency_accounts
               WHERE aga_code = v_acc_code;
      END IF;
   END;

   PROCEDURE create_agency_client (
      v_agnc_code        tqc_agency_clients.agnc_code%TYPE,
      v_agnc_agn_code    tqc_agency_clients.agnc_agn_code%TYPE DEFAULT NULL,
      v_agnc_clnt_code   tqc_agency_clients.agnc_clnt_code%TYPE DEFAULT NULL,
      v_add_edit         VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_agency_clients
                        (agnc_code, agnc_agn_code,
                         agnc_clnt_code
                        )
                 VALUES (tqc_agnc_code_seq.NEXTVAL, v_agnc_agn_code,
                         v_agnc_clnt_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR INSERTING RECORD');
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_agency_clients
               SET agnc_agn_code = v_agnc_agn_code
             WHERE agnc_clnt_code = v_agnc_clnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR UPDATING THE AGENCY CLIENT');
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE      tqc_agency_clients
                  WHERE agnc_clnt_code = v_agnc_clnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR DELETING RECORD');
         END;
      ELSE
         NULL;
      END IF;
   END create_agency_client;

   PROCEDURE tqc_embassy_contacts_prc (
      v_add_edit               IN       VARCHAR2,
      v_account_contacts_tab   IN       tqc_account_contacts_tab,
      v_user                   IN       VARCHAR2,
      v_err                    OUT      VARCHAR2
   )
   IS
      v_accc_pwd   VARCHAR2 (75);
   BEGIN
      IF v_account_contacts_tab.COUNT = 0
      THEN
         raise_error ('No Agency data provided..');
      END IF;

      FOR i IN 1 .. v_account_contacts_tab.COUNT
      LOOP
--    raise_error('v_account_contacts_tab(I).ACCC_CODE='||v_account_contacts_tab(I).ACCC_CODE);
         IF v_add_edit = 'A'
         THEN
            IF LENGTH (v_account_contacts_tab (i).accc_pwd) <= 15
            THEN
               v_accc_pwd :=
                           gis_read.val (v_account_contacts_tab (i).accc_pwd);
            ELSE
               raise_error
                   (' Password may only be upt to a maximum of 15 characters');
            END IF;

            INSERT INTO tqc_embassy_contacts
                        (accc_code,
                         accc_cou_code,
                         accc_name,
                         accc_other_names,
                         accc_dob,
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
                         v_account_contacts_tab (i).accc_agn_code,
                         v_account_contacts_tab (i).accc_name,
                         v_account_contacts_tab (i).accc_other_names,
                         v_account_contacts_tab (i).accc_dob,
                         v_account_contacts_tab (i).accc_tel,
                         v_account_contacts_tab (i).accc_email_addr,
                         v_account_contacts_tab (i).accc_sms_tel,
                         v_account_contacts_tab (i).accc_username,
                         /*v_account_contacts_tab(I).ACCC_PWD*/
                         v_accc_pwd,
                         v_account_contacts_tab (i).accc_login_allowed,
                         SYSDATE, v_account_contacts_tab (i).accc_pwd_reset,
                         SYSDATE, v_account_contacts_tab (i).accc_status,
                         v_account_contacts_tab (i).accc_login_attempts,
                         v_account_contacts_tab (i).accc_personel_rank,
                         v_account_contacts_tab (i).accc_last_login_date,
                         v_account_contacts_tab (i).accc_created_by
                        );
         ELSIF v_add_edit = 'E'
         THEN
            UPDATE tqc_embassy_contacts
               SET accc_cou_code = v_account_contacts_tab (i).accc_agn_code,
                   accc_name = v_account_contacts_tab (i).accc_name,
                   accc_other_names =
                                   v_account_contacts_tab (i).accc_other_names,
                   accc_dob = v_account_contacts_tab (i).accc_dob,
                   accc_tel = v_account_contacts_tab (i).accc_tel,
                   accc_email_addr =
                                    v_account_contacts_tab (i).accc_email_addr,
                   accc_sms_tel = v_account_contacts_tab (i).accc_sms_tel,
                   accc_username = v_account_contacts_tab (i).accc_username,
                   -- ACCC_PWD =  v_accc_pwd,--v_account_contacts_tab(I).ACCC_PWD,
                   accc_login_allowed =
                                 v_account_contacts_tab (i).accc_login_allowed,
                   accc_pwd_changed = SYSDATE,
                   accc_pwd_reset = v_account_contacts_tab (i).accc_pwd_reset,
                   accc_status = v_account_contacts_tab (i).accc_status,
                   accc_login_attempts =
                                v_account_contacts_tab (i).accc_login_attempts,
                   accc_personel_rank =
                                 v_account_contacts_tab (i).accc_personel_rank,
                   accc_last_login_date =
                               v_account_contacts_tab (i).accc_last_login_date,
                   accc_created_by =
                                    v_account_contacts_tab (i).accc_created_by
             WHERE accc_code = v_account_contacts_tab (i).accc_code;
         ELSIF v_add_edit = 'D'
         THEN
            BEGIN
               UPDATE tqc_embassy_contacts
                  SET accc_status = 'I'
                WHERE accc_code = v_account_contacts_tab (i).accc_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error (' User could not be deactivated');
            END;
         END IF;
      END LOOP;
   END;

   PROCEDURE create_agency_serv_prov (
      v_agnt_code       tqc_agency_service_providers.agnt_code%TYPE,
      v_agnt_spr_code   tqc_agency_service_providers.agnt_spr_code%TYPE
            DEFAULT NULL,
      v_agnt_agn_code   tqc_agency_service_providers.agnt_agn_code%TYPE
            DEFAULT NULL,
      v_add_edit        VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_agency_service_providers
                        (agnt_code, agnt_spr_code,
                         agnt_agn_code
                        )
                 VALUES (tqc_agnt_code_seq.NEXTVAL, v_agnt_spr_code,
                         v_agnt_agn_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR INSERTING RECORD');
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_agency_service_providers
               SET agnt_code = v_agnt_code
             WHERE agnt_spr_code = v_agnt_spr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR UPDATING THE AGENCY CLIENT');
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE      tqc_agency_service_providers
                  WHERE agnt_spr_code = v_agnt_spr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR DELETING RECORD');
         END;
      ELSE
         NULL;
      END IF;
   END create_agency_serv_prov;

   PROCEDURE getagencyprincipledir (
      v_agn_code    IN   NUMBER,
      v_principle   IN   VARCHAR2
   )
   IS
      v_cnt   NUMBER;
   BEGIN
      SELECT COUNT (*)
        INTO v_cnt
        FROM tqc_agency_directors
       WHERE adir_agn_code = v_agn_code AND adir_principle = 'Y';

      IF NVL (v_cnt, 0) > 0 AND NVL (v_principle, 'N') = 'Y'
      THEN
         raise_error
              ('You cannot have two principle directors for the same Account');
      END IF;
   END;
PROCEDURE add_agency_product(v_agent_code IN NUMBER, v_prod_code IN NUMBER, v_add_edit IN VARCHAR2)
IS
v_count NUMBER;
BEGIN
    IF v_add_edit = 'A' THEN
        SELECT COUNT(AGNP_PROD_CODE)
        INTO v_count
        FROM TQC_AGENCY_PRODUCTS
        WHERE   AGNP_AGN_CODE = v_agent_code
        AND     AGNP_PROD_CODE = v_prod_code;
        
        IF v_count = 0 THEN --Add the product
            INSERT INTO TQC_AGENCY_PRODUCTS
                (agnp_code, agnp_agn_code, agnp_prod_code)
            VALUES
                (tqc_agency_products_seq.NEXTVAL, v_agent_code, v_prod_code);
        END IF; 
    ELSIF v_add_edit = 'D' THEN
        DELETE TQC_AGENCY_PRODUCTS
        WHERE   AGNP_AGN_CODE = v_agent_code
        AND     AGNP_PROD_CODE = v_prod_code;
    END IF;
END;
PROCEDURE promoteDemoteManager(
            v_agn_code IN NUMBER, 
            v_promo_promote IN VARCHAR2, 
            v_trans_type IN VARCHAR2,
            v_demote IN VARCHAR)
IS
   v_act_code NUMBER;
    v_ia_sht_desc varchar(100);
    v_name varchar(100);
    v_brn_code NUMBER;
   v_bra_code NUMBER; 
   v_bru_code NUMBER;
   v_reg_code NUMBER;
BEGIN
-- RAISE_ERROR(
--   'v_agn_code: '||v_agn_code  ||
--   'v_promo_promote: '||v_promo_promote  || 
--   'v_trans_type: '||v_trans_type ||
--   'v_demote'||v_demote 
-- );
IF v_demote = 'Y' THEN
    IF v_trans_type = 'BA' THEN  
            UPDATE TQC_REGIONS
            SET reg_agn_code = null
            WHERE reg_agn_code=v_agn_code;
        
            UPDATE TQC_BRANCHES
            SET brn_agn_code = null
            WHERE brn_agn_code=v_agn_code;
            
    ELSIF v_trans_type = 'BE' THEN
            UPDATE TQC_REGIONS
            SET reg_agn_code = null
            WHERE reg_agn_code=v_agn_code;
            
            UPDATE TQC_BRANCHES
            SET brn_agn_code = null
            WHERE brn_agn_code=v_agn_code;
             
            UPDATE TQC_BRANCH_AGENCIES
            SET bra_agn_code = null
            WHERE bra_agn_code=v_agn_code;
            
    ELSIF v_trans_type = 'IA' THEN
            
            UPDATE TQC_REGIONS
            SET reg_agn_code = null
            WHERE reg_agn_code=v_agn_code;
            
            UPDATE TQC_BRANCHES
            SET brn_agn_code = null,
            brn_manager = null
            WHERE brn_agn_code=v_agn_code;
            
            UPDATE TQC_BRANCH_AGENCIES
            SET bra_agn_code = null,
            bra_manager=null
            WHERE bra_agn_code=v_agn_code;
             
            UPDATE TQC_BRANCH_UNITS
            SET bru_agn_code = null,
            bru_manager=null
            WHERE bru_agn_code=v_agn_code;
                
    END IF;
  END IF;
    /*lms_ord_misc.Generate_AgentNo(
                       v_trans_type,
                       v_ia_sht_desc,
                       v_reg_code,
                       v_brn_code,
                       v_bra_code,
                       v_bru_code,
                       v_agn_code,
                       v_name,
                       v_promo_promote
                       ); */ 
   --RAISE_ERROR('v_ia_sht_desc'||v_ia_sht_desc);
    EXCEPTION
       WHEN OTHERS
       THEN
       raise_error ('Unable to Promote/Demote..........');
END;

PROCEDURE agent_status_prc (
      v_result        OUT      VARCHAR2,
      v_err           OUT      VARCHAR2,
      v_agent_code   IN       NUMBER,
      v_status        IN       VARCHAR2
   )
   IS
   BEGIN
      UPDATE tqc_agencies
         SET agn_status = v_status
       WHERE agn_code = v_agent_code;
       v_result:='S';
   EXCEPTION
      WHEN OTHERS
      THEN
         v_err := 'ERROR AUTHORISING AGENT--------------';
   END agent_status_prc;
PROCEDURE Agent_Field_Log_Prc (
	 v_AGN_CODE in number,
	 v_no in number,
	 v_field IN VARCHAR2,
	 v_action IN VARCHAR2,
	 v_old_val IN VARCHAR2,
	 v_new_val IN VARCHAR2,
	 v_date date,
	 v_posted_by IN VARCHAR2
 )IS
   BEGIN
   
   IF NVL(v_old_val,'*')!=NVL(v_new_val,'*') THEN
        INSERT INTO Tqc_Agent_Logs
           (agnl_code, agnl_field, agnl_old_value, agnl_new_value, 
            agnl_action, agnl_date, agnl_AGN_CODE, agnl_posted_by,agnl_no)
         VALUES
           (Tqc_agnl_Code_Seq.NEXTVAL, v_field, v_old_val,v_new_val, 
            v_action, v_date, v_AGN_CODE, v_posted_by,v_no); 
       END IF;     
            
   END Agent_Field_Log_Prc;   
END tqc_agencies_pkg; 
/