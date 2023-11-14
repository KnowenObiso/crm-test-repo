/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_CLIENTS_PKGBKP  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_clients_pkgbkp
AS
/******************************************************************************
   NAME:       TQC_CLIENTS_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        27/03/09             1. Created this package body.
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

   FUNCTION get_client_sht_desc (
      v_firstname     VARCHAR2,
      v_middle_name   VARCHAR2,
      v_surname       VARCHAR2
   )
      RETURN VARCHAR2
   IS
      v_prp_sht_desc     VARCHAR2 (50);
      v_prp_sht_desc2    VARCHAR2 (50);
      v_firstnamei       VARCHAR2 (3);
      v_middle_namei     VARCHAR2 (3);
      v_surname2         VARCHAR2 (100);
      v_surnamei         VARCHAR2 (100);
      x                  NUMBER;
      v_cnt              NUMBER;
      v_clnt_id_format   VARCHAR2 (20);
      v_clnt_seq         NUMBER;
   BEGIN
      BEGIN
         v_clnt_id_format :=
                    tqc_parameters_pkg.get_param_varchar ('CLIENT_ID_FORMAT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_clnt_id_format := 'DEFAULT';
      END;

      IF v_clnt_id_format = 'DEFAULT'
      THEN
         IF INSTR (LTRIM (v_surname), 'THE ') = 1
         THEN
            v_surname2 := SUBSTR (LTRIM (v_surname), 4);
         ELSE
            v_surname2 := v_surname;
         END IF;

         IF INSTR (LTRIM (v_surname), ' ') != 0
         THEN
            v_surnamei :=
                  SUBSTR (LTRIM (v_surname2),
                          1,
                          LEAST (INSTR (LTRIM (v_surname2), ' ') - 1, 9)
                         )
               || SUBSTR (LTRIM (v_surname2),
                          INSTR (LTRIM (v_surname2), ' ') + 1,
                          1
                         );
         ELSE
            v_surnamei := SUBSTR (LTRIM (v_surname2), 1, 9);
         END IF;

         IF INSTR (LTRIM (v_firstname), ' ') != 0
         THEN
            v_firstnamei :=
                  SUBSTR (LTRIM (v_firstname), 1, 1)
               || SUBSTR (LTRIM (v_firstname),
                          INSTR (LTRIM (v_firstname), ' ') + 1,
                          1
                         );
         ELSE
            v_firstnamei := SUBSTR (LTRIM (v_firstname), 1, 1);
         END IF;

         IF INSTR (LTRIM (v_middle_name), ' ') != 0
         THEN
            v_middle_namei :=
                  SUBSTR (LTRIM (v_middle_name), 1, 1)
               || SUBSTR (LTRIM (v_middle_name),
                          INSTR (LTRIM (v_middle_name), ' ') + 1,
                          1
                         );
         ELSE
            v_middle_namei := SUBSTR (LTRIM (v_middle_name), 1, 1);
         END IF;

         v_prp_sht_desc :=
                          v_surnamei || '  ' || v_firstnamei || v_middle_namei;

         BEGIN
            SELECT COUNT (1)
              INTO v_cnt
              FROM tqc_clients
             WHERE REPLACE (clnt_sht_desc, ' ', '') = v_prp_sht_desc;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error
                       ('ERROR CHECKING CLIENT SHORT DESCRIPTION IF UNIQUE..');
         END;

         IF NVL (v_cnt, 0) != 0
         THEN
            x := 0;

            <<prpshtdesc>>
            LOOP
               x := NVL (x, 0) + 1;
               v_prp_sht_desc2 := v_prp_sht_desc || x;

               BEGIN
                  SELECT COUNT (1)
                    INTO v_cnt
                    FROM tqc_clients
                   WHERE REPLACE (clnt_sht_desc, ' ', '') = v_prp_sht_desc2;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error
                              ('ERROR CHECKING CLIENT SHORT NAME IF UNIQUE..');
               END;

               IF NVL (v_cnt, 0) = 0
               THEN
                  v_prp_sht_desc := v_prp_sht_desc2;
                  EXIT prpshtdesc;
               END IF;
            END LOOP;
         END IF;
      ELSIF v_clnt_id_format = 'INITIAL/SERIAL'
      THEN
         IF INSTR (LTRIM (v_surname), 'THE ') = 1
         THEN
            v_surname2 := SUBSTR (LTRIM (v_surname), 4);
         ELSE
            v_surname2 := v_surname;
         END IF;

         IF INSTR (LTRIM (v_surname), ' ') != 0
         THEN
            v_surnamei :=
                  SUBSTR (LTRIM (v_surname2),
                          1,
                          LEAST (INSTR (LTRIM (v_surname2), ' ') - 1, 9)
                         )
               || SUBSTR (LTRIM (v_surname2),
                          INSTR (LTRIM (v_surname2), ' ') + 1,
                          1
                         );
         ELSE
            v_surnamei := SUBSTR (LTRIM (v_surname2), 1, 9);
         END IF;

         IF INSTR (LTRIM (v_firstname), ' ') != 0
         THEN
            v_firstnamei :=
                  SUBSTR (LTRIM (v_firstname), 1, 1)
               || SUBSTR (LTRIM (v_firstname),
                          INSTR (LTRIM (v_firstname), ' ') + 1,
                          1
                         );
         ELSE
            v_firstnamei := SUBSTR (LTRIM (v_firstname), 1, 1);
         END IF;

         IF INSTR (LTRIM (v_middle_name), ' ') != 0
         THEN
            v_middle_namei :=
                  SUBSTR (LTRIM (v_middle_name), 1, 1)
               || SUBSTR (LTRIM (v_middle_name),
                          INSTR (LTRIM (v_middle_name), ' ') + 1,
                          1
                         );
         ELSE
            v_middle_namei := SUBSTR (LTRIM (v_middle_name), 1, 1);
         END IF;

         IF INSTR (LTRIM (v_firstname), ' ') != 0
         THEN
            v_firstnamei :=
                  SUBSTR (LTRIM (v_firstname), 1, 1)
               || SUBSTR (LTRIM (v_firstname),
                          INSTR (LTRIM (v_firstname), ' ') + 1,
                          1
                         );
         ELSE
            v_firstnamei := SUBSTR (LTRIM (v_firstname), 1, 1);
         END IF;

         IF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'A'
         THEN
            SELECT clnt_id_a_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'B'
         THEN
            SELECT clnt_id_b_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'C'
         THEN
            SELECT clnt_id_c_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'D'
         THEN
            SELECT clnt_id_d_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'E'
         THEN
            SELECT clnt_id_e_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'F'
         THEN
            SELECT clnt_id_f_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'G'
         THEN
            SELECT clnt_id_g_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'H'
         THEN
            SELECT clnt_id_h_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'I'
         THEN
            SELECT clnt_id_i_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'J'
         THEN
            SELECT clnt_id_j_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'K'
         THEN
            SELECT clnt_id_k_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'L'
         THEN
            SELECT clnt_id_l_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'M'
         THEN
            SELECT clnt_id_m_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'N'
         THEN
            SELECT clnt_id_n_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'O'
         THEN
            SELECT clnt_id_o_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'P'
         THEN
            SELECT clnt_id_p_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'Q'
         THEN
            SELECT clnt_id_q_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'R'
         THEN
            SELECT clnt_id_r_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'S'
         THEN
            SELECT clnt_id_s_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'T'
         THEN
            SELECT clnt_id_t_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'V'
         THEN
            SELECT clnt_id_v_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'U'
         THEN
            SELECT clnt_id_u_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'W'
         THEN
            SELECT clnt_id_w_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'X'
         THEN
            SELECT clnt_id_x_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'Y'
         THEN
            SELECT clnt_id_y_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSIF NVL (v_firstnamei, SUBSTR (LTRIM (v_surnamei), 1, 1)) = 'Z'
         THEN
            SELECT clnt_id_z_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         ELSE
            SELECT clnt_id_seq.NEXTVAL
              INTO v_clnt_seq
              FROM DUAL;
         END IF;

         v_prp_sht_desc :=
               SUBSTR (LTRIM (NVL (v_firstname, v_surnamei)), 1, 1)
            || LPAD (v_clnt_seq, 6, 0);
      ELSIF v_clnt_id_format = 'FMSURNAME'
      THEN
         IF INSTR (LTRIM (v_surname), 'THE ') = 1
         THEN
            v_surname2 := SUBSTR (LTRIM (v_surname), 4);
         ELSE
            v_surname2 := v_surname;
         END IF;

         IF INSTR (LTRIM (v_surname), ' ') != 0
         THEN
            v_surnamei :=
                  SUBSTR (LTRIM (v_surname2),
                          1,
                          LEAST (INSTR (LTRIM (v_surname2), ' ') - 1, 9)
                         )
               || SUBSTR (LTRIM (v_surname2),
                          INSTR (LTRIM (v_surname2), ' ') + 1,
                          1
                         );
         ELSE
            v_surnamei := SUBSTR (LTRIM (v_surname2), 1, 9);
         END IF;

         IF INSTR (LTRIM (v_firstname), ' ') != 0
         THEN
            v_firstnamei :=
                  SUBSTR (LTRIM (v_firstname), 1, 1)
               || SUBSTR (LTRIM (v_firstname),
                          INSTR (LTRIM (v_firstname), ' ') + 1,
                          1
                         );
         ELSE
            v_firstnamei := SUBSTR (LTRIM (v_firstname), 1, 1);
         END IF;

         IF INSTR (LTRIM (v_middle_name), ' ') != 0
         THEN
            v_middle_namei :=
                  SUBSTR (LTRIM (v_middle_name), 1, 1)
               || SUBSTR (LTRIM (v_middle_name),
                          INSTR (LTRIM (v_middle_name), ' ') + 1,
                          1
                         );
         ELSE
            v_middle_namei := SUBSTR (LTRIM (v_middle_name), 1, 1);
         END IF;

         v_prp_sht_desc :=
               SUBSTR (LTRIM (v_firstnamei), 1, 1)
            || SUBSTR (LTRIM (v_middle_namei), 1, 1)
            || ' '
            || v_surname2;

         IF LENGTH (v_prp_sht_desc) > 25
         THEN
            v_prp_sht_desc := RPAD (v_prp_sht_desc, 24);
         END IF;

         -- RAISE_ERROR('v_prp_sht_desc'||v_prp_sht_desc);
         BEGIN
            SELECT COUNT (1)
              INTO v_cnt
              FROM tqc_clients
             WHERE REPLACE (clnt_sht_desc, ' ', '') = v_prp_sht_desc;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR CHECKING CLIENT SHORT NAME IF UNIQUE..');
         END;

         IF NVL (v_cnt, 0) != 0
         THEN
            x := 0;

            <<prpshtdesc1>>
            LOOP
               x := NVL (x, 0) + 1;
               v_prp_sht_desc2 := v_prp_sht_desc || x;

               BEGIN
                  SELECT COUNT (1)
                    INTO v_cnt
                    FROM tqc_clients
                   WHERE REPLACE (clnt_sht_desc, ' ', '') = v_prp_sht_desc2;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error
                              ('ERROR CHECKING CLIENT SHORT NAME IF UNIQUE..');
               END;

               IF NVL (v_cnt, 0) = 0
               THEN
                  v_prp_sht_desc := v_prp_sht_desc2;
                  EXIT prpshtdesc1;
               END IF;
            END LOOP;
         END IF;
      END IF;

      RETURN (v_prp_sht_desc);
   END;

   FUNCTION clients_qry (
      v_clnt_first_name    VARCHAR2,
      v_clnt_middle_name   VARCHAR2,
      v_clnt_surname       VARCHAR2,
      v_clnt_post_add      VARCHAR2,
      v_clnt_id            VARCHAR2,
      v_clnt_pin_no        VARCHAR2,
      searchcriteria       VARCHAR2
   )
      RETURN clients_ref
   IS
      v_retcur   clients_ref;
   BEGIN
      IF searchcriteria = 'ALL'
      THEN
         OPEN v_retcur FOR
            SELECT clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                   clnt_other_names, clnt_name, clnt_id_reg_no,
                   clnt_sht_desc, clnt_zip_code
              FROM tqc_clients
             WHERE NVL (clnt_other_names, 'NONE') LIKE
                               '%' || NVL (v_clnt_first_name, 'HAKUNA')
                               || '%'
               AND NVL (clnt_other_names, 'NONE') LIKE
                               '%' || NVL (v_clnt_middle_name, 'HAKUNA')
                               || '%'
               AND (   NVL (clnt_other_names, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
                    OR NVL (clnt_name, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
                   );
      ELSIF searchcriteria = 'FSE'
      THEN
         OPEN v_retcur FOR
            SELECT clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                   clnt_other_names, clnt_name, clnt_id_reg_no,
                   clnt_sht_desc, clnt_zip_code
              FROM tqc_clients
             WHERE (       NVL (clnt_other_names, 'NONE') LIKE
                                '%' || NVL (v_clnt_first_name, 'HAKUNA')
                                || '%'
                       AND NVL (clnt_name, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
                    OR     NVL (clnt_name, 'NONE') LIKE
                                '%' || NVL (v_clnt_first_name, 'HAKUNA')
                                || '%'
                       AND NVL (clnt_other_names, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
                   );
      ELSIF searchcriteria = 'SNE'
      THEN
         OPEN v_retcur FOR
            SELECT clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                   clnt_other_names, clnt_name, clnt_id_reg_no,
                   clnt_sht_desc, clnt_zip_code
              FROM tqc_clients
             WHERE NVL (clnt_other_names, 'NONE') LIKE
                                  '%' || NVL (v_clnt_surname, 'HAKUNA')
                                  || '%'
                OR NVL (clnt_name, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%';
      ELSIF searchcriteria = 'ANE'
      THEN
         OPEN v_retcur FOR
            SELECT clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                   clnt_other_names, clnt_name, clnt_id_reg_no,
                   clnt_sht_desc, clnt_zip_code
              FROM tqc_clients
             WHERE NVL (clnt_other_names, 'NONE') LIKE
                               '%' || NVL (v_clnt_first_name, 'HAKUNA')
                               || '%'
                OR NVL (clnt_name, 'NONE') LIKE
                                '%' || NVL (v_clnt_first_name, 'HAKUNA')
                                || '%'
                OR NVL (clnt_other_names, 'NONE') LIKE
                               '%' || NVL (v_clnt_middle_name, 'HAKUNA')
                               || '%'
                OR NVL (clnt_name, 'NONE') LIKE
                               '%' || NVL (v_clnt_middle_name, 'HAKUNA')
                               || '%'
                OR NVL (clnt_other_names, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
                OR NVL (clnt_name, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%';
      END IF;

      RETURN (v_retcur);
   END;

   FUNCTION get_clients
      RETURN clients_ref
   IS
      v_retcur   clients_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT   clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                  clnt_other_names, clnt_name, clnt_id_reg_no, clnt_sht_desc,
                  clnt_zip_code
             FROM tqc_clients
         ORDER BY clnt_name;

      RETURN (v_retcur);
   END;

   FUNCTION get_pol_clients
      RETURN clients_ref_cursor
   IS
      v_retcur   clients_ref_cursor;
   BEGIN
      OPEN v_retcur FOR
         SELECT clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                clnt_other_names, clnt_name, clnt_id_reg_no, clnt_sht_desc,
                clnt_zip_code, clnt_surname
           FROM tqc_clients, tqc_client_systems
          WHERE clnt_code = csys_clnt_code
            AND csys_sys_code = 37
            AND TRUNC (SYSDATE) BETWEEN TRUNC(csys_wef)
                                    AND NVL (csys_wet, TRUNC (SYSDATE));

      RETURN (v_retcur);
   END;

   FUNCTION get_client_dtls (v_clnt_code IN NUMBER)
      RETURN clients_dtls_ref
   IS
      v_retcur   clients_dtls_ref;
   BEGIN
--       RAISE_ERROR('v_clnt_code '||v_clnt_code);
      OPEN v_retcur FOR
         SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,
                clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,
                clnt_postal_addrs, clnt_twn_code,
                tqc_setups_cursor.town_name (clnt_twn_code) twn_name,
                clnt_cou_code,
                tqc_setups_cursor.country_name (clnt_cou_code) cou_name,
                clnt_email_addrs, clnt_tel, clnt_tel2, clnt_status, clnt_fax,
                clnt_remarks, clnt_spcl_terms, clnt_declined_prop,
                clnt_increased_premium, clnt_policy_cancelled, clnt_proposer,
                clnt_accnt_no, clnt_domicile_countries, clnt_wef, clnt_wet,
                clnt_withdrawal_reason, clnt_sec_code, clnt_surname,
                clnt_type, clnt_title, clnt_business, clnt_zip_code,
                clnt_bbr_code, clnt_bank_acc_no, clnt_clnt_code,
                clnt_non_direct, clnt_created_by, clnt_sms_tel,
                clnt_agnt_status, clnt_date_created, clnt_runoff,
                clnt_loaded_by, clnt_direct_client, clnt_old_accnt_no,
                clnt_usr_code,
                tqc_interfaces_pkg.username (clnt_usr_code) usr_name,
                clnt_cntct_phone_1, clnt_cntct_email_1, clnt_cntct_phone_2,
                clnt_cntct_email_2, clnt_sts_code,
                tqc_setups_cursor.state_name (clnt_sts_code) sts_name,
                clnt_passport_no, clnt_gender, clnt_cntct_name_1,
                clnt_cntct_name_2, clt_cell_no,
                bnk_bank_name || ' - ' || bbr_branch_name
           FROM tqc_clients, tqc_bank_branches, tqc_banks
          WHERE clnt_code = NVL (v_clnt_code, clnt_code)
            AND clnt_bbr_code = bbr_code(+)
            AND bbr_bnk_code = bnk_code(+);

      RETURN (v_retcur);
   END;

   FUNCTION get_client_details (v_clnt_code IN NUMBER)
      RETURN clients_details_ref
   IS
      v_retcur   clients_details_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,
                clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,
                clnt_postal_addrs, clnt_twn_code,
                tqc_setups_cursor.town_name (clnt_twn_code) twn_name,
                clnt_cou_code,
                tqc_setups_cursor.country_name (clnt_cou_code) cou_name,
                clnt_email_addrs, clnt_tel, clnt_tel2, clnt_status, clnt_fax,
                clnt_remarks, clnt_spcl_terms, clnt_declined_prop,
                clnt_increased_premium, clnt_policy_cancelled, clnt_proposer,
                clnt_accnt_no, clnt_domicile_countries, clnt_wef, clnt_wet,
                clnt_withdrawal_reason, clnt_sec_code, clnt_surname,
                clnt_type, clnt_title, clnt_business, clnt_zip_code,
                clnt_bbr_code, clnt_bank_acc_no, clnt_clnt_code,
                clnt_non_direct, clnt_created_by, clnt_sms_tel,
                clnt_agnt_status, clnt_date_created, clnt_runoff,
                clnt_loaded_by, clnt_direct_client, clnt_old_accnt_no,
                clnt_usr_code,
                tqc_interfaces_pkg.username (clnt_usr_code) usr_name,
                clnt_cntct_phone_1, clnt_cntct_email_1, clnt_cntct_phone_2,
                clnt_cntct_email_2, clnt_sts_code,
                tqc_setups_cursor.state_name (clnt_sts_code) sts_name,
                clnt_passport_no, clnt_gender, clnt_cntct_name_1,
                clnt_cntct_name_2,
                tqc_setups_cursor.sector_name (clnt_sec_code) sector_name,
                clnt_website, clnt_auditors, clnt_parent_company,
                clnt_current_insurer, clnt_projected_business,
                clnt_date_of_empl, clnt_driving_licence,
                tqc_setups_cursor.parent_company_name
                                                   (clnt_parent_company)
                                                                       pname,
                clnt_brn_code,
                tqc_setups_cursor.branch_name (clnt_brn_code) branch_name,
                clnt_credit_lim_allowed, clnt_credit_limit, clnt_bounced_chq,
                clnt_marital_status, NULL, NULL, clnt_payroll_no,
                clnt_sal_max_range, clnt_sal_min_range
           FROM tqc_clients
          WHERE clnt_code = NVL (v_clnt_code, clnt_code);

      RETURN (v_retcur);
   END;

   FUNCTION get_clnt_by_accofficer (v_usr_code IN NUMBER)
      RETURN clients_details_ref
   IS
      v_retcur   clients_details_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,
                clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,
                clnt_postal_addrs, clnt_twn_code,
                tqc_setups_cursor.town_name (clnt_twn_code) twn_name,
                clnt_cou_code,
                tqc_setups_cursor.country_name (clnt_cou_code) cou_name,
                clnt_email_addrs, clnt_tel, clnt_tel2, clnt_status, clnt_fax,
                clnt_remarks, clnt_spcl_terms, clnt_declined_prop,
                clnt_increased_premium, clnt_policy_cancelled, clnt_proposer,
                clnt_accnt_no, clnt_domicile_countries, clnt_wef, clnt_wet,
                clnt_withdrawal_reason, clnt_sec_code, clnt_surname,
                clnt_type, clnt_title, clnt_business, clnt_zip_code,
                clnt_bbr_code, clnt_bank_acc_no, clnt_clnt_code,
                clnt_non_direct, clnt_created_by, clnt_sms_tel,
                clnt_agnt_status, clnt_date_created, clnt_runoff,
                clnt_loaded_by, clnt_direct_client, clnt_old_accnt_no,
                clnt_usr_code,
                tqc_interfaces_pkg.username (clnt_usr_code) usr_name,
                clnt_cntct_phone_1, clnt_cntct_email_1, clnt_cntct_phone_2,
                clnt_cntct_email_2, clnt_sts_code,
                tqc_setups_cursor.state_name (clnt_sts_code) sts_name,
                clnt_passport_no, clnt_gender, clnt_cntct_name_1,
                clnt_cntct_name_2,
                tqc_setups_cursor.sector_name (clnt_sec_code) sector_name,
                clnt_website, clnt_auditors, clnt_parent_company,
                clnt_current_insurer, clnt_projected_business,
                clnt_date_of_empl, clnt_driving_licence,
                tqc_setups_cursor.parent_company_name
                                                   (clnt_parent_company)
                                                                       pname,
                clnt_brn_code,
                tqc_setups_cursor.branch_name (clnt_brn_code) branch_name,
                clnt_credit_lim_allowed, clnt_credit_limit, clnt_bounced_chq,
                clnt_marital_status, NULL, NULL, clnt_payroll_no,
                clnt_sal_max_range, clnt_sal_min_range
           FROM tqc_clients
          WHERE clnt_acc_officer = v_usr_code;

      RETURN (v_retcur);
   END;

   FUNCTION get_client_detailsbycriteria (v_col IN VARCHAR2, v_grp_code NUMBER)
      RETURN clients_details_ref
   IS
      v_retcur   clients_details_ref;
      search     VARCHAR2 (15000);
   BEGIN
      --RAISE_ERROR(' v_col '||v_col);
      OPEN v_retcur FOR
          -- execute immediate SEARCH INTO v_retcur;
           --var_str := 'select COUNT(*) from TQC_CLIENTS '||v_where;
         --execute immediate var_str into v_count;
         SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,
                clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,
                clnt_postal_addrs, clnt_twn_code,
                tqc_setups_cursor.town_name (clnt_twn_code) twn_name,
                clnt_cou_code,
                tqc_setups_cursor.country_name (clnt_cou_code) cou_name,
                clnt_email_addrs, clnt_tel, clnt_tel2, clnt_status, clnt_fax,
                clnt_remarks, clnt_spcl_terms, clnt_declined_prop,
                clnt_increased_premium, clnt_policy_cancelled, clnt_proposer,
                clnt_accnt_no, clnt_domicile_countries, clnt_wef, clnt_wet,
                clnt_withdrawal_reason, clnt_sec_code, clnt_surname,
                clnt_type, clnt_title, clnt_business, clnt_zip_code,
                clnt_bbr_code, clnt_bank_acc_no, clnt_clnt_code,
                clnt_non_direct, clnt_created_by, clnt_sms_tel,
                clnt_agnt_status, clnt_date_created, clnt_runoff,
                clnt_loaded_by, clnt_direct_client, clnt_old_accnt_no,
                clnt_usr_code,
                tqc_interfaces_pkg.username (clnt_usr_code) usr_name,
                clnt_cntct_phone_1, clnt_cntct_email_1, clnt_cntct_phone_2,
                clnt_cntct_email_2, clnt_sts_code,
                tqc_setups_cursor.state_name (clnt_sts_code) sts_name,
                clnt_passport_no, clnt_gender, clnt_cntct_name_1,
                clnt_cntct_name_2,
                tqc_setups_cursor.sector_name (clnt_sec_code) sector_name,
                clnt_website, clnt_auditors, clnt_parent_company,
                clnt_current_insurer, clnt_projected_business,
                clnt_date_of_empl, clnt_driving_licence,
                tqc_setups_cursor.parent_company_name
                                                   (clnt_parent_company)
                                                                       pname,
                clnt_brn_code,
                tqc_setups_cursor.branch_name (clnt_brn_code) branch_name,
                clnt_credit_lim_allowed, clnt_credit_limit, clnt_bounced_chq,
                clnt_marital_status, NULL, NULL, clnt_payroll_no,
                clnt_sal_max_range, clnt_sal_min_range
           FROM tqc_clients;

      RETURN (v_retcur);
   END;

   FUNCTION getclntdetailsbycriteria (v_col IN VARCHAR2)
      RETURN clients_details_ref
   IS
      var_str                 VARCHAR2 (10000);

      TYPE clientdetails IS REF CURSOR;

      v_clients_details_rec   clientdetails;
   BEGIN
      -- raise_error('v_col='||v_col);
      var_str :=
            'SELECT    DISTINCT  TQC_CLIENTS.CLNT_CODE, TQC_CLIENTS.CLNT_SHT_DESC,TQC_CLIENTS.CLNT_NAME, TQC_CLIENTS.CLNT_OTHER_NAMES, TQC_CLIENTS.CLNT_ID_REG_NO, TQC_CLIENTS.CLNT_DOB, 
            TQC_CLIENTS.CLNT_PIN, TQC_CLIENTS.CLNT_PHYSICAL_ADDRS, TQC_CLIENTS.CLNT_POSTAL_ADDRS, TQC_CLIENTS.CLNT_TWN_CODE, 
            TQC_SETUPS_CURSOR.TOWN_NAME(TQC_CLIENTS.CLNT_TWN_CODE) TWN_NAME, TQC_CLIENTS.CLNT_COU_CODE ,         
            TQC_SETUPS_CURSOR.COUNTRY_NAME(TQC_CLIENTS.CLNT_COU_CODE) COU_NAME, 
            TQC_CLIENTS.CLNT_EMAIL_ADDRS, TQC_CLIENTS.CLNT_TEL, TQC_CLIENTS.CLNT_TEL2, TQC_CLIENTS.CLNT_STATUS, TQC_CLIENTS.CLNT_FAX, TQC_CLIENTS.CLNT_REMARKS, 
            TQC_CLIENTS.CLNT_SPCL_TERMS, TQC_CLIENTS.CLNT_DECLINED_PROP, TQC_CLIENTS.CLNT_INCREASED_PREMIUM, TQC_CLIENTS.CLNT_POLICY_CANCELLED, 
            TQC_CLIENTS.CLNT_PROPOSER,TQC_CLIENTS.CLNT_ACCNT_NO, TQC_CLIENTS.CLNT_DOMICILE_COUNTRIES, TQC_CLIENTS.CLNT_WEF, TQC_CLIENTS.CLNT_WET, 
            TQC_CLIENTS.CLNT_WITHDRAWAL_REASON, TQC_CLIENTS.CLNT_SEC_CODE, TQC_CLIENTS.CLNT_SURNAME, TQC_CLIENTS.CLNT_TYPE, TQC_CLIENTS.CLNT_TITLE, TQC_CLIENTS.CLNT_BUSINESS, 
            TQC_COUNTRIES.COU_ZIP_CODE CLNT_ZIP_CODE, TQC_CLIENTS.CLNT_BBR_CODE, TQC_CLIENTS.CLNT_BANK_ACC_NO, TQC_CLIENTS.CLNT_CLNT_CODE, 
            TQC_CLIENTS.CLNT_NON_DIRECT, TQC_CLIENTS.CLNT_CREATED_BY, TQC_CLIENTS.CLNT_SMS_TEL, TQC_CLIENTS.CLNT_AGNT_STATUS, TQC_CLIENTS.CLNT_DATE_CREATED, 
            TQC_CLIENTS.CLNT_RUNOFF, TQC_CLIENTS.CLNT_LOADED_BY, TQC_CLIENTS.CLNT_DIRECT_CLIENT, TQC_CLIENTS.CLNT_OLD_ACCNT_NO, TQC_CLIENTS.CLNT_USR_CODE, 
            TQC_INTERFACES_PKG.USERNAME(TQC_CLIENTS.CLNT_USR_CODE) USR_NAME,
            TQC_CLIENTS.CLNT_CNTCT_PHONE_1, TQC_CLIENTS.CLNT_CNTCT_EMAIL_1, TQC_CLIENTS.CLNT_CNTCT_PHONE_2, TQC_CLIENTS.CLNT_CNTCT_EMAIL_2,
            TQC_CLIENTS.CLNT_STS_CODE, 
            TQC_SETUPS_CURSOR.STATE_NAME(TQC_CLIENTS.CLNT_STS_CODE) STS_NAME, 
            TQC_CLIENTS.CLNT_PASSPORT_NO, TQC_CLIENTS.CLNT_GENDER, TQC_CLIENTS.CLNT_CNTCT_NAME_1, TQC_CLIENTS.CLNT_CNTCT_NAME_2, TQC_SETUPS_CURSOR.SECTOR_NAME(TQC_CLIENTS.CLNT_SEC_CODE) SECTOR_NAME,
            TQC_CLIENTS.CLNT_WEBSITE,TQC_CLIENTS.CLNT_AUDITORS,
            TQC_CLIENTS.CLNT_PARENT_COMPANY,
            TQC_CLIENTS.CLNT_CURRENT_INSURER,TQC_CLIENTS.CLNT_PROJECTED_BUSINESS    ,TQC_CLIENTS.CLNT_DATE_OF_EMPL    ,
            TQC_CLIENTS.CLNT_DRIVING_LICENCE    ,
            TQC_SETUPS_CURSOR.PARENT_COMPANY_NAME(TQC_CLIENTS.CLNT_PARENT_COMPANY)  PNAME,
            TQC_CLIENTS.CLNT_BRN_CODE,TQC_SETUPS_CURSOR.BRANCH_NAME(TQC_CLIENTS.CLNT_BRN_CODE) BRANCH_NAME,
            TQC_CLIENTS.CLNT_ACC_OFFICER, TQC_INTERFACES_PKG.USERNAME(TQC_CLIENTS.CLNT_ACC_OFFICER) CLNT_ACC_NAME,
            TQC_CLIENTS.CLNT_OLD_SHT_DESC,TQC_CLIENTS.CLT_CELL_NO,
            TQC_SETUPS_CURSOR.BANKBRANCH_NAME(TQC_CLIENTS.CLNT_BBR_CODE) BANKBRANCH_NAME,
            TQC_CLIENTS.CLNT_OCCUPATION,
            TQC_CLIENTS.CLNT_ANNIVERSARY ,
            TQC_CLIENTS.CLNT_CRDT_RATING,CLTN_CLIENT_TYPES,TQC_CLIENTS_PKG.getCLNTNAME(CLNT_CLNT_CODE) HOLDING_CLNT,
            CLNT_SACCO,COU_ZIP_CODE,CLNT_CREDIT_LIM_ALLOWED, CLNT_CREDIT_LIMIT,LOC_NAME,CLNT_BOUNCED_CHQ,
            CLNT_MARITAL_STATUS,CLNT_DEFAULT_COMM_MODE,CLNT_PAYROLL_NO,CLNT_SAL_MIN_RANGE,CLNT_SAL_MAX_RANGE,CLNT_BPN_CODE,BPN_NAME,CLNT_TEL_PAY
      FROM TQC_CLIENTS,tqc_client_accounts,tqc_countries,LMS_POLICIES,LMS_PROPOSERS,TQC_LOCATIONS,TQC_BUSINESS_PERSONS
        '
         || v_col
         || '
         AND CLNT_CODE= CLNA_CLNT_CODE(+) 
         AND CLNT_COU_CODE = COU_CODE(+)
         AND POL_PRP_CODE=PRP_CODE(+)
         AND PRP_CLNT_CODE=CLNT_CODE(+)
         AND CLNT_LOC_CODE=LOC_CODE(+)
         and BPN_CODE(+)=CLNT_BPN_CODE
         UNION
         SELECT    DISTINCT  TQC_CLIENTS.CLNT_CODE, TQC_CLIENTS.CLNT_SHT_DESC,TQC_CLIENTS.CLNT_NAME, TQC_CLIENTS.CLNT_OTHER_NAMES, TQC_CLIENTS.CLNT_ID_REG_NO, TQC_CLIENTS.CLNT_DOB, 
            TQC_CLIENTS.CLNT_PIN, TQC_CLIENTS.CLNT_PHYSICAL_ADDRS, TQC_CLIENTS.CLNT_POSTAL_ADDRS, TQC_CLIENTS.CLNT_TWN_CODE, 
            TQC_SETUPS_CURSOR.TOWN_NAME(TQC_CLIENTS.CLNT_TWN_CODE) TWN_NAME, TQC_CLIENTS.CLNT_COU_CODE ,         
            TQC_SETUPS_CURSOR.COUNTRY_NAME(TQC_CLIENTS.CLNT_COU_CODE) COU_NAME, 
            TQC_CLIENTS.CLNT_EMAIL_ADDRS, TQC_CLIENTS.CLNT_TEL, TQC_CLIENTS.CLNT_TEL2, TQC_CLIENTS.CLNT_STATUS, TQC_CLIENTS.CLNT_FAX, TQC_CLIENTS.CLNT_REMARKS, 
            TQC_CLIENTS.CLNT_SPCL_TERMS, TQC_CLIENTS.CLNT_DECLINED_PROP, TQC_CLIENTS.CLNT_INCREASED_PREMIUM, TQC_CLIENTS.CLNT_POLICY_CANCELLED, 
            TQC_CLIENTS.CLNT_PROPOSER,TQC_CLIENTS.CLNT_ACCNT_NO, TQC_CLIENTS.CLNT_DOMICILE_COUNTRIES, TQC_CLIENTS.CLNT_WEF, TQC_CLIENTS.CLNT_WET, 
            TQC_CLIENTS.CLNT_WITHDRAWAL_REASON, TQC_CLIENTS.CLNT_SEC_CODE, TQC_CLIENTS.CLNT_SURNAME, TQC_CLIENTS.CLNT_TYPE, TQC_CLIENTS.CLNT_TITLE, TQC_CLIENTS.CLNT_BUSINESS, 
            TQC_COUNTRIES.COU_ZIP_CODE CLNT_ZIP_CODE, TQC_CLIENTS.CLNT_BBR_CODE, TQC_CLIENTS.CLNT_BANK_ACC_NO, TQC_CLIENTS.CLNT_CLNT_CODE, 
            TQC_CLIENTS.CLNT_NON_DIRECT, TQC_CLIENTS.CLNT_CREATED_BY, TQC_CLIENTS.CLNT_SMS_TEL, TQC_CLIENTS.CLNT_AGNT_STATUS, TQC_CLIENTS.CLNT_DATE_CREATED, 
            TQC_CLIENTS.CLNT_RUNOFF, TQC_CLIENTS.CLNT_LOADED_BY, TQC_CLIENTS.CLNT_DIRECT_CLIENT, TQC_CLIENTS.CLNT_OLD_ACCNT_NO, TQC_CLIENTS.CLNT_USR_CODE, 
            TQC_INTERFACES_PKG.USERNAME(TQC_CLIENTS.CLNT_USR_CODE) USR_NAME,
            TQC_CLIENTS.CLNT_CNTCT_PHONE_1, TQC_CLIENTS.CLNT_CNTCT_EMAIL_1, TQC_CLIENTS.CLNT_CNTCT_PHONE_2, TQC_CLIENTS.CLNT_CNTCT_EMAIL_2,
            TQC_CLIENTS.CLNT_STS_CODE, 
            TQC_SETUPS_CURSOR.STATE_NAME(TQC_CLIENTS.CLNT_STS_CODE) STS_NAME, 
            TQC_CLIENTS.CLNT_PASSPORT_NO, TQC_CLIENTS.CLNT_GENDER, TQC_CLIENTS.CLNT_CNTCT_NAME_1, TQC_CLIENTS.CLNT_CNTCT_NAME_2, TQC_SETUPS_CURSOR.SECTOR_NAME(TQC_CLIENTS.CLNT_SEC_CODE) SECTOR_NAME,
            TQC_CLIENTS.CLNT_WEBSITE,TQC_CLIENTS.CLNT_AUDITORS,
            TQC_CLIENTS.CLNT_PARENT_COMPANY,
            TQC_CLIENTS.CLNT_CURRENT_INSURER,TQC_CLIENTS.CLNT_PROJECTED_BUSINESS    ,TQC_CLIENTS.CLNT_DATE_OF_EMPL    ,
            TQC_CLIENTS.CLNT_DRIVING_LICENCE    ,
            TQC_SETUPS_CURSOR.PARENT_COMPANY_NAME(TQC_CLIENTS.CLNT_PARENT_COMPANY)  PNAME,
            TQC_CLIENTS.CLNT_BRN_CODE,TQC_SETUPS_CURSOR.BRANCH_NAME(TQC_CLIENTS.CLNT_BRN_CODE) BRANCH_NAME,
            TQC_CLIENTS.CLNT_ACC_OFFICER, TQC_INTERFACES_PKG.USERNAME(TQC_CLIENTS.CLNT_ACC_OFFICER) CLNT_ACC_NAME,
            TQC_CLIENTS.CLNT_OLD_SHT_DESC,TQC_CLIENTS.CLT_CELL_NO,
            TQC_SETUPS_CURSOR.BANKBRANCH_NAME(TQC_CLIENTS.CLNT_BBR_CODE) BANKBRANCH_NAME,
            TQC_CLIENTS.CLNT_OCCUPATION,
            TQC_CLIENTS.CLNT_ANNIVERSARY ,
            TQC_CLIENTS.CLNT_CRDT_RATING,CLTN_CLIENT_TYPES,TQC_CLIENTS_PKG.getCLNTNAME(CLNT_CLNT_CODE) 
            HOLDING_CLNT,CLNT_SACCO,COU_ZIP_CODE,CLNT_CREDIT_LIM_ALLOWED, CLNT_CREDIT_LIMIT,LOC_NAME,CLNT_BOUNCED_CHQ,
            CLNT_MARITAL_STATUS,CLNT_DEFAULT_COMM_MODE,CLNT_PAYROLL_NO,CLNT_SAL_MIN_RANGE,CLNT_SAL_MAX_RANGE,CLNT_BPN_CODE,BPN_NAME,CLNT_TEL_PAY   
      FROM TQC_CLIENTS,tqc_client_accounts,tqc_countries,GIN_POLICIES,TQC_LOCATIONS,TQC_BUSINESS_PERSONS
         '
         || v_col
         || '
         AND CLNT_CODE= CLNA_CLNT_CODE(+) 
         AND CLNT_COU_CODE = COU_CODE(+)
         AND CLNT_LOC_CODE=LOC_CODE(+)
          and BPN_CODE(+)=CLNT_BPN_CODE
           and BPN_CODE(+)=CLNT_BPN_CODE
         AND POL_PRP_CODE(+)=CLNT_CODE';

      ---  RAISE_ERROR( v_col);
      OPEN v_clients_details_rec FOR var_str;

      RETURN v_clients_details_rec;
   END;

   FUNCTION getclntname (v_clnt_code IN NUMBER)
      RETURN VARCHAR2
   IS
      v_clnt_name   tqc_clients.clnt_name%TYPE;
   BEGIN
      SELECT clnt_name || ' ' || clnt_other_names
        INTO v_clnt_name
        FROM tqc_clients
       WHERE clnt_code = v_clnt_code;

      RETURN v_clnt_name;
   EXCEPTION
      WHEN OTHERS
      THEN
         RETURN NULL;
   END;

   FUNCTION getclntemail (v_clnt_code IN NUMBER)
      RETURN VARCHAR2
   IS
      v_clnt_email   tqc_clients.clnt_email_addrs%TYPE;
   BEGIN
      SELECT clnt_email_addrs
        INTO v_clnt_email
        FROM tqc_clients
       WHERE clnt_code = v_clnt_code;

      RETURN v_clnt_email;
   EXCEPTION
      WHEN OTHERS
      THEN
         RETURN NULL;
   END;

   FUNCTION get_client_detailsnotingroup (
      v_clnt_code   IN   NUMBER,
      v_grp_code         tqc_group_clnt_dtls.grpd_grp_code%TYPE
   )
      RETURN clients_details_ref
   IS
      v_retcur   clients_details_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,
                clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,
                clnt_postal_addrs, clnt_twn_code,
                tqc_setups_cursor.town_name (clnt_twn_code) twn_name,
                clnt_cou_code,
                tqc_setups_cursor.country_name (clnt_cou_code) cou_name,
                clnt_email_addrs, clnt_tel, clnt_tel2, clnt_status, clnt_fax,
                clnt_remarks, clnt_spcl_terms, clnt_declined_prop,
                clnt_increased_premium, clnt_policy_cancelled, clnt_proposer,
                clnt_accnt_no, clnt_domicile_countries, clnt_wef, clnt_wet,
                clnt_withdrawal_reason, clnt_sec_code, clnt_surname,
                clnt_type, clnt_title, clnt_business, clnt_zip_code,
                clnt_bbr_code, clnt_bank_acc_no, clnt_clnt_code,
                clnt_non_direct, clnt_created_by, clnt_sms_tel,
                clnt_agnt_status, clnt_date_created, clnt_runoff,
                clnt_loaded_by, clnt_direct_client, clnt_old_accnt_no,
                clnt_usr_code,
                tqc_interfaces_pkg.username (clnt_usr_code) usr_name,
                clnt_cntct_phone_1, clnt_cntct_email_1, clnt_cntct_phone_2,
                clnt_cntct_email_2, clnt_sts_code,
                tqc_setups_cursor.state_name (clnt_sts_code) sts_name,
                clnt_passport_no, clnt_gender, clnt_cntct_name_1,
                clnt_cntct_name_2,
                tqc_setups_cursor.sector_name (clnt_sec_code) sector_name,
                clnt_website, clnt_auditors, clnt_parent_company,
                clnt_current_insurer, clnt_projected_business,
                clnt_date_of_empl, clnt_driving_licence,
                tqc_setups_cursor.parent_company_name
                                                   (clnt_parent_company)
                                                                       pname,
                clnt_brn_code,
                tqc_setups_cursor.branch_name (clnt_brn_code) branch_name,
                clnt_credit_lim_allowed, clnt_credit_limit, clnt_bounced_chq,
                clnt_marital_status, NULL, NULL, clnt_payroll_no,
                clnt_sal_max_range, clnt_sal_min_range
           FROM tqc_clients
          WHERE clnt_code NOT IN (SELECT grpd_clnt_code
                                    FROM tqc_group_clnt_dtls
                                   WHERE grpd_grp_code = v_grp_code);

      RETURN (v_retcur);
   END;

   FUNCTION get_cnt_client_details (
      v_clnt_code   IN   NUMBER,
      v_grp_code         tqc_group_clnt_dtls.grpd_grp_code%TYPE
   )
      RETURN NUMBER
   IS
      v_count   NUMBER;
   BEGIN
      SELECT COUNT (1)
        INTO v_count
        FROM tqc_clients
       WHERE clnt_code NOT IN (SELECT grpd_clnt_code
                                 FROM tqc_group_clnt_dtls
                                WHERE grpd_grp_code = v_grp_code);

      RETURN (v_count);
   END;

   FUNCTION get_client_dtls_bynames (
      v_surname                 tqc_clients.clnt_name%TYPE,
      v_other_name              tqc_clients.clnt_other_names%TYPE,
      v_clnt_passport_no   IN   VARCHAR2 DEFAULT NULL,
      v_clnt_pin           IN   VARCHAR2 DEFAULT NULL,
      v_clnt_id_reg_no     IN   VARCHAR2 DEFAULT NULL
   )
      RETURN clients_dtls_ref
   IS
      v_retcur   clients_dtls_ref;
   BEGIN
      -- RAISE_ERROR('V_CLNT_ID_REG_NO'||V_CLNT_ID_REG_NO||'v_clnt_pin'||v_clnt_pin||'v_surname'||v_surname||'v_other_name'||v_other_name);
      OPEN v_retcur FOR
         SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,
                clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,
                clnt_postal_addrs, clnt_twn_code,
                tqc_setups_cursor.town_name (clnt_twn_code) twn_name,
                clnt_cou_code,
                tqc_setups_cursor.country_name (clnt_cou_code) cou_name,
                clnt_email_addrs, clnt_tel, clnt_tel2, clnt_status, clnt_fax,
                clnt_remarks, clnt_spcl_terms, clnt_declined_prop,
                clnt_increased_premium, clnt_policy_cancelled, clnt_proposer,
                clnt_accnt_no, clnt_domicile_countries, clnt_wef, clnt_wet,
                clnt_withdrawal_reason, clnt_sec_code, clnt_surname,
                clnt_type, clnt_title, clnt_business, clnt_zip_code,
                clnt_bbr_code, clnt_bank_acc_no, clnt_clnt_code,
                clnt_non_direct, clnt_created_by, clnt_sms_tel,
                clnt_agnt_status, clnt_date_created, clnt_runoff,
                clnt_loaded_by, clnt_direct_client, clnt_old_accnt_no,
                clnt_usr_code,
                tqc_interfaces_pkg.username (clnt_usr_code) usr_name,
                clnt_cntct_phone_1, clnt_cntct_email_1, clnt_cntct_phone_2,
                clnt_cntct_email_2, clnt_sts_code,
                tqc_setups_cursor.state_name (clnt_sts_code) sts_name,
                clnt_passport_no, clnt_gender, clnt_cntct_name_1,
                clnt_cntct_name_2, NULL, NULL
           FROM tqc_clients
          WHERE (       UPPER (clnt_other_names) LIKE
                            '%' || UPPER (NVL (v_other_name, 'HAKUNA)'))
                            || '%'
                    AND UPPER (clnt_name) LIKE
                                '%' || UPPER (NVL (v_surname, 'HAKUNA'))
                                || '%'
                 OR UPPER (clnt_pin) LIKE
                               '%' || UPPER (NVL (v_clnt_pin, 'HAKUNA'))
                               || '%'
                 OR UPPER (clnt_passport_no) LIKE
                       '%' || UPPER (NVL (v_clnt_passport_no, 'HAKUNA'))
                       || '%'
                 OR UPPER (clnt_id_reg_no) LIKE
                         '%' || UPPER (NVL (v_clnt_id_reg_no, 'HAKUNA'))
                         || '%'
                );

--        UNION ALL
--
--           SELECT CLNT_CODE, CLNT_SHT_DESC, CLNT_NAME, CLNT_OTHER_NAMES, CLNT_ID_REG_NO, CLNT_DOB,
--            CLNT_PIN, CLNT_PHYSICAL_ADDRS, CLNT_POSTAL_ADDRS, CLNT_TWN_CODE,
--            TQC_SETUPS_CURSOR.TOWN_NAME(CLNT_TWN_CODE) TWN_NAME, CLNT_COU_CODE,
--            TQC_SETUPS_CURSOR.COUNTRY_NAME(CLNT_COU_CODE) COU_NAME,
--            CLNT_EMAIL_ADDRS, CLNT_TEL, CLNT_TEL2, CLNT_STATUS, CLNT_FAX, CLNT_REMARKS,
--            CLNT_SPCL_TERMS, CLNT_DECLINED_PROP, CLNT_INCREASED_PREMIUM, CLNT_POLICY_CANCELLED,
--            CLNT_PROPOSER, CLNT_ACCNT_NO, CLNT_DOMICILE_COUNTRIES, CLNT_WEF, CLNT_WET,
--            CLNT_WITHDRAWAL_REASON, CLNT_SEC_CODE, CLNT_SURNAME, CLNT_TYPE, CLNT_TITLE, CLNT_BUSINESS,
--            CLNT_ZIP_CODE, CLNT_BBR_CODE, CLNT_BANK_ACC_NO, CLNT_CLNT_CODE,
--            CLNT_NON_DIRECT, CLNT_CREATED_BY, CLNT_SMS_TEL, CLNT_AGNT_STATUS, CLNT_DATE_CREATED,
--            CLNT_RUNOFF, CLNT_LOADED_BY, CLNT_DIRECT_CLIENT, CLNT_OLD_ACCNT_NO, CLNT_USR_CODE,
--            TQC_INTERFACES_PKG.USERNAME(CLNT_USR_CODE) USR_NAME,
--            CLNT_CNTCT_PHONE_1, CLNT_CNTCT_EMAIL_1, CLNT_CNTCT_PHONE_2, CLNT_CNTCT_EMAIL_2,
--            CLNT_STS_CODE,
--            TQC_SETUPS_CURSOR.STATE_NAME(CLNT_STS_CODE) STS_NAME,
--            CLNT_PASSPORT_NO, CLNT_GENDER, CLNT_CNTCT_NAME_1, CLNT_CNTCT_NAME_2,NULL,NULL
--        FROM TQC_CLIENTS
--        WHERE
--       UPPER(CLNT_OTHER_NAMES) LIKE '%'||
--        UPPER(NVL(v_other_name,'HAKUNA)'))||
--        '%' AND UPPER(CLNT_NAME) LIKE '%'||
--         UPPER(NVL(v_other_name,'HAKUNA'))||'%'
--       OR UPPER(CLNT_PIN) LIKE '%'||
--         UPPER(NVL(v_clnt_pin,'HAKUNA'))||'%'
--         OR UPPER(CLNT_PASSPORT_NO) LIKE '%'||
--         UPPER(NVL(v_clnt_passport_no,'HAKUNA'))||'%'
--         OR UPPER(CLNT_ID_REG_NO) LIKE '%'||
--         UPPER(NVL(v_clnt_id_reg_no,'HAKUNA'))||'%'
--        ;
      RETURN (v_retcur);
   END;

   FUNCTION get_client_web_accounts (v_clnt_code IN NUMBER)
      RETURN clients_web_acc_ref
   IS
      v_retcur   clients_web_acc_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT acwa_code, acwa_username, acwa_pwd, acwa_login_allowed,
                acwa_personel_rank, acwa_dt_created, acwa_status,
                acwa_clnt_code, acwa_created_by, acwa_name, acwa_email_addrs
           FROM tqc_client_web_accounts
          WHERE acwa_clnt_code = v_clnt_code;

      RETURN (v_retcur);
   END;

   FUNCTION get_client_accounts (v_clnt_code IN NUMBER)
      RETURN client_accounts_ref
   IS
      v_retcur   client_accounts_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT clna_code, clna_sht_desc, clna_name, clna_clnt_code,
                clna_created_by, clna_date_created, clna_status,
                clna_remarks, clna_wef, clna_wet, div_code, NULL brn_name,
                div_name
           FROM tqc_client_accounts, tqc_divisions
          WHERE clna_clnt_code = v_clnt_code AND clna_div_code = div_code(+);

      RETURN (v_retcur);
   END;

   PROCEDURE clients_qry (
      clnt_tab             IN OUT   clients_tab,
      v_clnt_first_name             VARCHAR2,
      v_clnt_middle_name            VARCHAR2,
      v_clnt_surname                VARCHAR2,
      v_clnt_post_add               VARCHAR2,
      v_clnt_id                     VARCHAR2,
      v_clnt_pin_no                 VARCHAR2,
      searchcriteria                VARCHAR2
   )
   IS
      x                  NUMBER          := 0;
      lv_cursor_id_num   PLS_INTEGER;
      lv_rowcount_num    PLS_INTEGER     := 0;
      sqltext            VARCHAR2 (1000)
         := 'SELECT CLNT_CODE, CLNT_PIN, CLNT_POSTAL_ADDRS, CLNT_TEL, CLNT_OTHER_NAMES, CLNT_NAME, CLNT_ID_REG_NO,CLNT_SHT_DESC,CLNT_POSTAL_CODE FROM TQC_CLIENTS ';
      sqlwhere           VARCHAR2 (500);

      CURSOR clnts_all
      IS
         SELECT clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                clnt_other_names, clnt_name, clnt_id_reg_no,
                clnt_sht_desc                             --,CLNT_POSTAL_CODE
           FROM tqc_clients
          WHERE NVL (clnt_other_names, 'NONE') LIKE
                               '%' || NVL (v_clnt_first_name, 'HAKUNA')
                               || '%'
            AND NVL (clnt_other_names, 'NONE') LIKE
                               '%' || NVL (v_clnt_middle_name, 'HAKUNA')
                               || '%'
            AND (   NVL (clnt_other_names, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
                 OR NVL (clnt_name, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
                );

      CURSOR clnts_fse
      IS
         SELECT clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                clnt_other_names, clnt_name, clnt_id_reg_no,
                clnt_sht_desc                              --,CLNT_POSTAL_CODE
           FROM tqc_clients
          WHERE (       NVL (clnt_other_names, 'NONE') LIKE
                                '%' || NVL (v_clnt_first_name, 'HAKUNA')
                                || '%'
                    AND NVL (clnt_name, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
                 OR     NVL (clnt_name, 'NONE') LIKE
                                '%' || NVL (v_clnt_first_name, 'HAKUNA')
                                || '%'
                    AND NVL (clnt_other_names, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
                );

      CURSOR clnts_sne
      IS
         SELECT clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                clnt_other_names, clnt_name, clnt_id_reg_no,
                clnt_sht_desc                              --,CLNT_POSTAL_CODE
           FROM tqc_clients
          WHERE NVL (clnt_other_names, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
             OR NVL (clnt_name, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%';

      CURSOR clnts_ane
      IS
         SELECT clnt_code, clnt_pin, clnt_postal_addrs, clnt_tel,
                clnt_other_names, clnt_name, clnt_id_reg_no,
                clnt_sht_desc                              --,CLNT_POSTAL_CODE
           FROM tqc_clients
          WHERE NVL (clnt_other_names, 'NONE') LIKE
                                '%' || NVL (v_clnt_first_name, 'HAKUNA')
                                || '%'
             OR NVL (clnt_name, 'NONE') LIKE
                                '%' || NVL (v_clnt_first_name, 'HAKUNA')
                                || '%'
             OR NVL (clnt_other_names, 'NONE') LIKE
                               '%' || NVL (v_clnt_middle_name, 'HAKUNA')
                               || '%'
             OR NVL (clnt_name, 'NONE') LIKE
                               '%' || NVL (v_clnt_middle_name, 'HAKUNA')
                               || '%'
             OR NVL (clnt_other_names, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%'
             OR NVL (clnt_name, 'NONE') LIKE
                                   '%' || NVL (v_clnt_surname, 'HAKUNA')
                                   || '%';
   BEGIN
      IF searchcriteria = 'ALL'
      THEN
         FOR clnts_rec IN clnts_all
         LOOP
            x := NVL (x, 0) + 1;
            clnt_tab (x).clnt_code := clnts_rec.clnt_code;
            clnt_tab (x).clnt_pin := clnts_rec.clnt_pin;
            clnt_tab (x).clnt_postal_addrs := clnts_rec.clnt_postal_addrs;
            clnt_tab (x).clnt_tel := clnts_rec.clnt_tel;
            clnt_tab (x).clnt_other_names := clnts_rec.clnt_other_names;
            clnt_tab (x).clnt_name := clnts_rec.clnt_name;
            clnt_tab (x).clnt_id_reg_no := clnts_rec.clnt_id_reg_no;
            clnt_tab (x).clnt_sht_desc := clnts_rec.clnt_sht_desc;
         --clnt_tab(x).CLNT_POSTAL_CODE:= clnts_rec.CLNT_POSTAL_CODE;
         END LOOP;
      ELSIF searchcriteria = 'FSE'
      THEN
         FOR clnts_rec IN clnts_fse
         LOOP
            --raise_error('start ='||clnts_rec.CLNT_CODE);
            x := NVL (x, 0) + 1;
            clnt_tab (x).clnt_code := clnts_rec.clnt_code;
            clnt_tab (x).clnt_pin := clnts_rec.clnt_pin;
            clnt_tab (x).clnt_postal_addrs := clnts_rec.clnt_postal_addrs;
            clnt_tab (x).clnt_tel := clnts_rec.clnt_tel;
            clnt_tab (x).clnt_other_names := clnts_rec.clnt_other_names;
            clnt_tab (x).clnt_name := clnts_rec.clnt_name;
            clnt_tab (x).clnt_id_reg_no := clnts_rec.clnt_id_reg_no;
            clnt_tab (x).clnt_sht_desc := clnts_rec.clnt_sht_desc;
         --clnt_tab(x).CLNT_POSTAL_CODE:= clnts_rec.CLNT_POSTAL_CODE;
         END LOOP;
      ELSIF searchcriteria = 'SNE'
      THEN
         FOR clnts_rec IN clnts_sne
         LOOP
            x := NVL (x, 0) + 1;
            clnt_tab (x).clnt_code := clnts_rec.clnt_code;
            clnt_tab (x).clnt_pin := clnts_rec.clnt_pin;
            clnt_tab (x).clnt_postal_addrs := clnts_rec.clnt_postal_addrs;
            clnt_tab (x).clnt_tel := clnts_rec.clnt_tel;
            clnt_tab (x).clnt_other_names := clnts_rec.clnt_other_names;
            clnt_tab (x).clnt_name := clnts_rec.clnt_name;
            clnt_tab (x).clnt_id_reg_no := clnts_rec.clnt_id_reg_no;
            clnt_tab (x).clnt_sht_desc := clnts_rec.clnt_sht_desc;
         --clnt_tab(x).CLNT_POSTAL_CODE:= clnts_rec.CLNT_POSTAL_CODE;
         END LOOP;
      ELSIF searchcriteria = 'ANE'
      THEN
         FOR clnts_rec IN clnts_ane
         LOOP
            x := NVL (x, 0) + 1;
            clnt_tab (x).clnt_code := clnts_rec.clnt_code;
            clnt_tab (x).clnt_pin := clnts_rec.clnt_pin;
            clnt_tab (x).clnt_postal_addrs := clnts_rec.clnt_postal_addrs;
            clnt_tab (x).clnt_tel := clnts_rec.clnt_tel;
            clnt_tab (x).clnt_other_names := clnts_rec.clnt_other_names;
            clnt_tab (x).clnt_name := clnts_rec.clnt_name;
            clnt_tab (x).clnt_id_reg_no := clnts_rec.clnt_id_reg_no;
            clnt_tab (x).clnt_sht_desc := clnts_rec.clnt_sht_desc;
         --clnt_tab(x).CLNT_POSTAL_CODE:= clnts_rec.CLNT_POSTAL_CODE;
         END LOOP;
      END IF;
   END;

   FUNCTION create_clnt_proc (
      v_direct_clnt            IN   VARCHAR2,
      v_sht_desc               IN   VARCHAR2,
      v_first_name             IN   VARCHAR2,
      v_middle_name            IN   VARCHAR2,
      v_surname                IN   VARCHAR2,
      v_pin                    IN   VARCHAR2,
      v_postal_addrs           IN   VARCHAR2,
      v_physical_addrs         IN   VARCHAR2,
      v_id_reg_no              IN   VARCHAR2,
      v_user                   IN   VARCHAR2,
      v_wef                    IN   DATE,
      v_wet                    IN   DATE,
      --v_direct_client          IN VARCHAR2,
      v_title                  IN   VARCHAR2,
      v_dob                    IN   DATE,
      v_cou_code               IN   NUMBER,
      v_twn_code               IN   NUMBER,
      v_zip_code               IN   VARCHAR2,
      v_email_addrs            IN   VARCHAR2,
      v_tel                    IN   VARCHAR2,
      v_sms_tel                IN   VARCHAR2,
      v_fax                    IN   VARCHAR2,
      v_sec_code               IN   NUMBER,
      v_business               IN   VARCHAR2,
      v_domicile_countries     IN   NUMBER,
      v_proposer               IN   VARCHAR2,
      v_status                 IN   VARCHAR2,
      v_runoff                 IN   VARCHAR2,
      v_withdrawal_reason      IN   VARCHAR2,
      v_remarks                IN   VARCHAR2,
      v_bank_acc_no            IN   VARCHAR2,
      v_bbr_code               IN   NUMBER,
      v_spcl_terms             IN   VARCHAR2,
      v_policy_cancelled       IN   VARCHAR2,
      v_increased_premium      IN   VARCHAR2,
      v_declined_prop          IN   VARCHAR2,
      v_client_type            IN   VARCHAR2,
      v_add_edit               IN   VARCHAR2,
      v_clnt_code              IN   NUMBER,
      v_sex                         VARCHAR2 DEFAULT NULL,
      v_prp_code                    lms_proposers.prp_code%TYPE DEFAULT NULL,
      v_prp_co_code                 lms_proposers.prp_co_code%TYPE
            DEFAULT NULL,
      v_prp_business                lms_proposers.prp_business%TYPE
            DEFAULT NULL,
      v_prp_cover_history           lms_proposers.prp_cover_history%TYPE
            DEFAULT NULL,
      v_prp_family_history          lms_proposers.prp_family_history%TYPE
            DEFAULT NULL,
      v_prp_personal_history        lms_proposers.prp_personal_history%TYPE
            DEFAULT NULL,
      v_sts_code                    tqc_states.sts_code%TYPE DEFAULT NULL,
      v_prp_type                    lms_proposers.prp_type%TYPE DEFAULT NULL,
      v_dob_admitted                lms_proposers.prp_dob_admitted%TYPE
            DEFAULT NULL,
      v_clnt_brn_code               tqc_clients.clnt_brn_code%TYPE
            DEFAULT NULL,
      v_clnt_cntct_name_1           tqc_clients.clnt_cntct_name_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_phone_1          tqc_clients.clnt_cntct_phone_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_email_1          tqc_clients.clnt_cntct_email_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_name_2           tqc_clients.clnt_cntct_name_2%TYPE
            DEFAULT NULL,
      v_clnt_cntct_phone_2          tqc_clients.clnt_cntct_phone_2%TYPE
            DEFAULT NULL,
      v_clnt_cntct_email_2          tqc_clients.clnt_cntct_email_2%TYPE
            DEFAULT NULL,
      v_clnt_acc_officer            tqc_clients.clnt_acc_officer%TYPE
            DEFAULT NULL
   --v_acc_mngr_code       IN NUMBER
   )
      RETURN NUMBER
   IS
      vclntcode             NUMBER;
      v_direct_clnt_seq     NUMBER;
      --v_direct_clnt varchar2(30);
      --v_direct_clnt varchar2(30);
      v_direct_srl_fmt      VARCHAR2 (100);
      v_clnt_param          VARCHAR2 (20);
      v_accnt_no            VARCHAR2 (35);
      v_clnt_sht_desc       VARCHAR2 (30);
      v_use_names_in_code   VARCHAR2 (1)   := 'Y';
   --v_cnt NUMBER;
   --x NUMBER:=1;
   --al_id number;
   BEGIN
      raise_error ('v_client_type=' || v_client_type);

      -- This is not an UPDATE. We are creating a new Client
      IF v_add_edit = 'A'
      THEN
         IF NVL (v_direct_clnt, 'K') NOT IN ('N', 'Y')
         THEN
            raise_error ('Specify if this is a Direct Client');
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

            BEGIN
               SELECT direct_clnt_seq.NEXTVAL
                 INTO v_direct_clnt_seq
                 FROM DUAL;

               -- v_accnt_no:= REPLACE(v_direct_srl_fmt,'[SERIALNO]',LPAD(v_direct_clnt_seq,6,0));
               v_accnt_no := LPAD (v_direct_clnt_seq, 6, 0);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error fetching the sequence...');
            END;

            BEGIN
               v_clnt_param :=
                    tqc_parameters_pkg.get_param_varchar ('CLIENT_ID_FORMAT');
            -- TQC_PARAMETERS
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
                                                       v_clnt_brn_code,
                                                       TO_CHAR (v_wef, 'RRRR'),
                                                       NVL (v_client_type,
                                                            'I'),
                                                       v_direct_srl_fmt,
                                                       v_surname,
                                                       v_first_name
                                                      );
            END IF;
         END IF;

         IF v_clnt_sht_desc IS NULL
         THEN
            BEGIN
               v_clnt_sht_desc :=
                  get_client_sht_desc (v_first_name, v_middle_name,
                                       v_surname);
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

         BEGIN
            --RAISE_ERROR(v_surname);
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
                         clnt_type, clnt_usr_code, clnt_gender,
                         clnt_sts_code, clnt_brn_code, clnt_cntct_name_1,
                         clnt_cntct_phone_1, clnt_cntct_email_1,
                         clnt_cntct_name_2, clnt_cntct_phone_2,
                         clnt_cntct_email_2, clnt_acc_officer
                        )
                 VALUES (vclntcode, v_pin, v_clnt_sht_desc,
                         v_postal_addrs, v_physical_addrs,
                         LTRIM (RTRIM (v_first_name || ' ' || v_middle_name)),
                         LTRIM (RTRIM (NVL (v_surname, 'n/a'))),
                         v_id_reg_no, TRUNC (SYSDATE), v_user,
                         v_wef, v_wet, v_direct_clnt, v_title,
                         v_dob, v_cou_code, v_twn_code,
                         v_zip_code, v_email_addrs, v_tel,
                         v_sms_tel, v_fax, v_sec_code,
                         v_business, v_domicile_countries,
                         v_accnt_no, v_proposer, v_status,
                         v_runoff, v_withdrawal_reason, v_remarks,
                         v_bank_acc_no, v_bbr_code, v_clnt_code,
                         v_spcl_terms, v_policy_cancelled,
                         v_increased_premium, v_declined_prop,
                         v_client_type, NULL, NVL (v_sex, 'M'),
                         v_sts_code, v_clnt_brn_code, v_clnt_cntct_name_1,
                         v_clnt_cntct_phone_1, v_clnt_cntct_email_1,
                         v_clnt_cntct_name_2, v_clnt_cntct_phone_2,
                         v_clnt_cntct_email_2, v_clnt_acc_officer
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error creating the client..');
         END;

         RETURN (vclntcode);
      ELSIF v_add_edit = 'E'
      THEN                                                -- This is an UPDATE
         UPDATE tqc_clients
            SET clnt_pin = v_pin,
                clnt_sht_desc = v_sht_desc,
                clnt_postal_addrs = v_postal_addrs,
                clnt_physical_addrs = v_physical_addrs,
                clnt_other_names =
                          LTRIM (RTRIM (v_first_name || ' ' || v_middle_name)),
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
                clnt_sms_tel = v_sms_tel,
                clnt_fax = v_fax,
                clnt_sec_code = v_sec_code,
                clnt_business = v_business,
                clnt_domicile_countries = v_domicile_countries,
                clnt_accnt_no = v_accnt_no,
                clnt_proposer = v_proposer,
                clnt_status = v_status,
                clnt_runoff = v_runoff,
                clnt_withdrawal_reason = v_withdrawal_reason,
                clnt_remarks = v_remarks,
                clnt_bank_acc_no = v_bank_acc_no,
                clnt_bbr_code = v_bbr_code,
                clnt_clnt_code = v_clnt_code,
                clnt_spcl_terms = v_spcl_terms,
                clnt_policy_cancelled = v_policy_cancelled,
                clnt_increased_premium = v_increased_premium,
                clnt_declined_prop = v_declined_prop,
                clnt_type = v_client_type,
                clnt_gender = v_sex,
                clnt_sts_code = v_sts_code,
                clnt_brn_code = v_clnt_brn_code,
                clnt_cntct_name_1 = v_clnt_cntct_name_1,
                clnt_cntct_phone_1 = v_clnt_cntct_phone_1,
                clnt_cntct_email_1 = v_clnt_cntct_email_1,
                clnt_cntct_name_2 = v_clnt_cntct_name_2,
                clnt_cntct_phone_2 = v_clnt_cntct_phone_2,
                clnt_cntct_email_2 = v_clnt_cntct_email_2,
                clnt_acc_officer = v_clnt_acc_officer
          WHERE clnt_code = v_clnt_code;

         update_proposer (v_clnt_code,
                          v_prp_code,
                          v_prp_co_code,
                          v_prp_business,
                          v_prp_cover_history,
                          v_prp_family_history,
                          v_prp_personal_history,
                          v_zip_code,
                          v_prp_type,
                          v_dob_admitted
                         );
         RETURN (v_clnt_code);
      ELSIF v_add_edit = 'D'
      THEN
         -- Step 1: Remove the Client Documents
         DELETE FROM tqc_client_documents
               WHERE cdocr_clnt_code = v_clnt_code;

         -- Step 2: Remove the Client Web Accounts
         DELETE FROM tqc_client_web_accounts
               WHERE acwa_clnt_code = v_clnt_code;

         -- Step 3: Remove the Clients Systems
         DELETE FROM tqc_client_systems
               WHERE csys_clnt_code = v_clnt_code;

         -- Step 4: Delete the Client
         DELETE FROM tqc_clients
               WHERE clnt_code = v_clnt_code;

         RETURN (v_clnt_code);
      END IF;
   END create_clnt_proc;

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
      v_clnt_tel_pay              IN       VARCHAR2 DEFAULT NULL
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
                         clnt_work_permit, clnt_default_comm_mode,clnt_tel_pay
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
                         v_work_permit, v_commmode,v_clnt_tel_pay
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
                   clnt_tel_pay = v_clnt_tel_pay
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

   FUNCTION get_countries
      RETURN country_ref
   IS
      v_retcur   country_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT   cou_code, cou_sht_desc, cou_name, cou_admin_reg_type
             FROM tqc_countries
         ORDER BY 2;

      RETURN (v_retcur);
   END;

   FUNCTION get_towns (v_cou_code IN NUMBER, v_sts_code NUMBER DEFAULT NULL)
      RETURN towns_ref
   IS
      v_retcur   towns_ref;
   BEGIN
      -- raise_application_error(-20001,'COUNTRY '||v_cou_code);
      OPEN v_retcur FOR
         SELECT   twn_code, pst_desc, twn_name, pst_zip_code, sts_name,
                  sts_code
             FROM tqc_towns, tqc_postal_codes, tqc_states
            WHERE twn_cou_code = v_cou_code
              AND pst_twn_code(+) = twn_code
              AND twn_sts_code = sts_code
              AND twn_sts_code = NVL (v_sts_code, twn_sts_code)
         ORDER BY 3;

      RETURN (v_retcur);
   END;

   FUNCTION get_sectors
      RETURN sectors_ref
   IS
      v_retcur   sectors_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT   sec_sht_desc, sec_name, sec_code
             FROM tqc_sectors
         ORDER BY 2;

      RETURN (v_retcur);
   END;

   FUNCTION get_banks
      RETURN banks_ref
   IS
      v_retcur   banks_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT   bnk_bank_name, bbr_branch_name, bbr_code
             FROM tqc_banks, tqc_bank_branches
            WHERE ((tqc_banks.bnk_code = tqc_bank_branches.bbr_bnk_code))
         ORDER BY tqc_banks.bnk_bank_name ASC;

      RETURN (v_retcur);
   END;

   FUNCTION get_banks_branches (v_bank IN NUMBER)
      RETURN banks_branches_ref
   IS
      v_retcur   banks_branches_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT bbr_code, bbr_branch_name, bbr_remarks, bbr_sht_desc,
                bbr_ref_code, bbr_eft_supported, bbr_dd_supported,
                bbr_date_created, bbr_created_by
           FROM tqc_bank_branches
          WHERE (bbr_bnk_code = v_bank);

      RETURN (v_retcur);
   END;

   FUNCTION get_affiliates (v_clnt_code IN NUMBER)
      RETURN affiliates_ref
   IS
      v_retcur   affiliates_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT DISTINCT clnt_sht_desc, clnt_name || ' ' || clnt_other_names,
                         clnt_code
                    FROM tqc_clients
                   WHERE clnt_code != v_clnt_code
         UNION
         SELECT   'NONE ', NULL, TO_NUMBER (NULL)
             FROM DUAL
         ORDER BY 1;

      RETURN (v_retcur);
   END;

   FUNCTION get_clnt_unallcted_sys (v_clnt_code IN NUMBER)
      RETURN systems_ref
   IS
      v_retcur   systems_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT sys_code, sys_sht_desc, sys_name
           FROM tqc_systems;

      /*WHERE SYS_CODE NOT IN (SELECT CSYS_SYS_CODE
                              FROM TQC_CLIENT_SYSTEMS
                              WHERE CSYS_CLNT_CODE = v_clnt_code);*/
      RETURN (v_retcur);
   END;

   FUNCTION get_clnt_allcted_sys (v_clnt_code IN NUMBER)
      RETURN systems_ref
   IS
      v_retcur   systems_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT sys_code, sys_sht_desc, sys_name
           FROM tqc_systems, tqc_client_systems
          WHERE sys_code = csys_sys_code
            AND csys_clnt_code = v_clnt_code
            AND TRUNC (SYSDATE) BETWEEN csys_wef
                                    AND NVL (csys_wet, TRUNC (SYSDATE));

      RETURN (v_retcur);
   END;

   PROCEDURE alloc_clnt_system (v_clnt_code IN NUMBER, v_sys_code IN NUMBER)
   IS
      v_count   NUMBER;
   BEGIN
      --RAISE_ERROR('Error while assigning the system ...' || v_clnt_code || ' and ' || v_sys_code);
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_client_systems
       WHERE csys_clnt_code = v_clnt_code AND csys_sys_code = v_sys_code;

      IF (NVL (v_count, 0) = 0)
      THEN
         BEGIN
            INSERT INTO tqc_client_systems
                        (csys_code, csys_clnt_code, csys_sys_code,
                         csys_wef, csys_wet
                        )
                 VALUES (tqc_csys_code_seq.NEXTVAL, v_clnt_code, v_sys_code,
                         TRUNC (SYSDATE), NULL
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error while assigning the system ...');
         END;

         IF v_sys_code = 27
         THEN
            --CREATE LMS_PROPOSER RECORD.
            lms_web_pkg_grp.addproposer ('A', v_clnt_code, NULL, NULL);
         END IF;
      END IF;
   END;

   FUNCTION get_client_assigned_systems (
      v_client_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN client_unassigned_systems_ref
   IS
      vcursor   client_unassigned_systems_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_active
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND EXISTS (
                   SELECT csys_code, csys_clnt_code, csys_sys_code
                     FROM tqc_client_systems
                    WHERE tqc_systems.sys_code =
                                              tqc_client_systems.csys_sys_code
                      AND csys_clnt_code = v_client_code);

      RETURN vcursor;
   END;

   FUNCTION get_client_unassigned_systems (
      v_client_code   IN   tqc_users.usr_code%TYPE
   )
      RETURN client_unassigned_systems_ref
   IS
      vcursor   client_unassigned_systems_ref;
   BEGIN
      OPEN vcursor FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_active
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND NOT EXISTS (
                   SELECT csys_code, csys_clnt_code, csys_sys_code
                     FROM tqc_client_systems
                    WHERE tqc_systems.sys_code =
                                              tqc_client_systems.csys_sys_code
                      AND csys_clnt_code = v_client_code);

      RETURN vcursor;
   END;

   PROCEDURE create_client_web_account (
      v_username             VARCHAR2,
      v_pwd                  VARCHAR2,
      v_login_allowed        VARCHAR2,
      v_pwd_changed          DATE,
      v_pwd_reset            VARCHAR2,
      v_personel_rank        VARCHAR2,
      v_acwa_status          VARCHAR2,
      v_clnt_code            NUMBER,
      v_created_by           VARCHAR2,
      v_email_address        VARCHAR2,
      v_add_edit             VARCHAR2,
      v_acwa_code            NUMBER,
      v_acwa_type       IN   VARCHAR2
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_client_web_accounts
                        (acwa_code, acwa_username,
                         acwa_pwd, acwa_login_allowed,
                         acwa_pwd_changed, acwa_pwd_reset,
                         acwa_login_attempts, acwa_personel_rank,
                         acwa_dt_created, acwa_status, acwa_last_login_date,
                         acwa_clnt_code, acwa_created_by, acwa_name,
                         acwa_email_addrs, acwa_type
                        )
                 VALUES (tqc_acwa_code_seq.NEXTVAL, v_username,
                         gis_read.val (v_pwd), v_login_allowed,
                         v_pwd_changed, NULL,
                         NULL, v_personel_rank,
                         TRUNC (SYSDATE), v_acwa_status, NULL,
                         v_clnt_code, v_created_by, v_username,
                         v_email_address, v_acwa_type
                        );

            COMMIT;
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_client_web_accounts
               SET acwa_login_allowed = v_login_allowed,
                   acwa_personel_rank = v_personel_rank,
                   acwa_status = v_acwa_status,
                   acwa_email_addrs = v_email_address,
                   acwa_type = v_acwa_type
             WHERE acwa_code = v_acwa_code;
         END;
      ELSE
         DELETE FROM tqc_client_web_accounts
               WHERE acwa_code = v_acwa_code;
      END IF;
   END;

   PROCEDURE create_client_account (
      v_add_edit       VARCHAR2,
      v_acc_code       NUMBER,
      v_sht_desc       VARCHAR2,
      v_name           VARCHAR2,
      v_clnt_code      NUMBER,
      v_created_by     VARCHAR2,
      v_date_created   DATE,
      v_status         VARCHAR2,
      v_remarks        VARCHAR2,
      v_wef            DATE DEFAULT NULL,
      v_wet            DATE DEFAULT NULL,
      v_div_code       NUMBER,
      v_brn_code       NUMBER
   )
   IS
      v_srl_fmt         VARCHAR2 (100);
      v_act_code        NUMBER;
      v_act_type        VARCHAR2 (10);
      v_brn_sht_desc    VARCHAR2 (15);
      v_count           NUMBER;
      v_serial_length   NUMBER;
      v_agent_id        VARCHAR2 (30);
      v_agent_seq       NUMBER;
      v_count1          NUMBER;
      v_client          VARCHAR (200);
   BEGIN
      IF v_add_edit = 'A'
      THEN
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
             WHERE act_code = 1;
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
            IF v_act_type = 'D'
            THEN
               SELECT agent_ac_d_seq.NEXTVAL
                 INTO v_agent_seq
                 FROM DUAL;
            END IF;

            v_agent_id :=
               REPLACE (v_srl_fmt,
                        '[SERIALNO]',
                        LPAD (v_agent_seq, v_serial_length, 0)
                       );
            v_agent_id := REPLACE (v_agent_id, '[BRN]', v_brn_sht_desc);

            BEGIN
               SELECT COUNT (1)
                 INTO v_count
                 FROM tqc_client_accounts
                WHERE clna_sht_desc = v_agent_id;
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
               INSERT INTO tqc_client_accounts
                           (clna_code, clna_sht_desc, clna_name,
                            clna_clnt_code, clna_created_by,
                            clna_date_created, clna_status, clna_remarks,
                            clna_wef, clna_wet, clna_div_code
                           )
                    VALUES (tqc_clna_code_seq.NEXTVAL, v_agent_id, v_name,
                            v_clnt_code, v_created_by,
                            v_date_created, v_status, v_remarks,
                            v_wef, v_wet, v_div_code
                           );
            ELSE
               BEGIN
                  SELECT      clnt_sht_desc
                           || '    '
                           || clnt_name
                           || ' '
                           || clnt_other_names,
                           COUNT (*)
                      INTO v_client,
                           v_count1
                      FROM tqc_clients, tqc_client_accounts
                     WHERE clna_sht_desc = v_sht_desc
                       AND clna_clnt_code = clnt_code
                  GROUP BY clnt_sht_desc, clnt_name, clnt_other_names;
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     v_count1 := 0;
               END;

               IF NVL (v_count1, 0) = 0
               THEN
                  BEGIN
                     INSERT INTO tqc_client_accounts
                                 (clna_code, clna_sht_desc,
                                  clna_name, clna_clnt_code,
                                  clna_created_by, clna_date_created,
                                  clna_status, clna_remarks, clna_wef,
                                  clna_wet, clna_div_code
                                 )
                          VALUES (tqc_clna_code_seq.NEXTVAL, v_sht_desc,
                                  v_name, v_clnt_code,
                                  v_created_by, v_date_created,
                                  v_status, v_remarks, v_wef,
                                  v_wet, v_div_code
                                 );
                  EXCEPTION
                     WHEN OTHERS
                     THEN
                        raise_error ('Error Inserting Record',
                                     SQLERRM (SQLCODE)
                                    );
                  END;
               ELSE
                  raise_error ('Account is Already In use by ' || v_client);
               END IF;
            END IF;
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_client_accounts
               SET clna_sht_desc = v_sht_desc,
                   clna_name = v_name,
                   clna_clnt_code = v_clnt_code,
                   clna_created_by = v_created_by,
                   clna_date_created = v_date_created,
                   clna_status = v_status,
                   clna_remarks = v_remarks,
                   clna_wef = v_wef,
                   clna_wet = v_wet,
                   clna_div_code = v_div_code
             WHERE clna_code = v_acc_code;
         END;
      ELSE
         DELETE FROM tqc_client_accounts
               WHERE clna_code = v_acc_code;
      END IF;
   END;

   PROCEDURE unalloc_clnt_system (v_clnt_code IN NUMBER, v_sys_code IN NUMBER)
   IS
   BEGIN
      DELETE FROM tqc_client_systems
            WHERE csys_clnt_code = v_clnt_code AND csys_sys_code = v_sys_code;
   END;

   /*Procedure Create_Loaded_client(v_cust_id IN NUMBER,
                                  v_bank_accNo IN VARCHAR2,
                                  v_id_ppt_no IN VARCHAR2,
                                  v_username IN VARCHAR2) IS
       vClientExists_inTquest Number;
       vClientExists  Number;
   begin
       -- check if client exists in TQUEST
       RAISE_ERROR('THIS CODE MUST BE FINISHED..');
       SELECT count('x')
       into vClientExists_inTquest
       FROM TQC_CLIENTS
       WHERE
       ( (CLNT_CODE = v_cust_id) OR (v_cust_id IS NULL ) ) OR
       ( (CLNT_BANK_ACC_NO = v_bank_accNo) OR (v_bank_accNo IS NULL ) ) OR
       ( (CLNT_ID_REG_NO = v_id_ppt_no) OR (v_id_ppt_no IS NULL ) );

       IF nvl(vClientExists_inTquest,0) <> 0 then

               raise_error('CLIENT ALREADY LOADED');

       ELSIF nvl(vClientExists_inTquest,0) = 0 then
               -- call function to check if exists in core banking.  n load data from core banking into the tquest TQC_LOAD_CLIENTS table
               RAISE_ERROR('THIS CODE MUST BE FINISHED..');
               --vClientExists := TQC_LOAD_DATA.GET_BANK_CLIENT ( v_cust_id, v_id_ppt_no  ,   v_bank_accNo   )    ;

               IF vClientExists = 1 then

                   INSERT INTO TQC_CLIENTS
                       (CLNT_CODE ,         CLNT_SHT_DESC ,
                       CLNT_NAME ,         CLNT_OTHER_NAMES ,         CLNT_ID_REG_NO ,
                       CLNT_DOB ,         CLNT_PIN ,         CLNT_PHYSICAL_ADDRS ,
                       CLNT_POSTAL_ADDRS ,         CLNT_EMAIL_ADDRS ,         CLNT_TEL ,
                       CLNT_STATUS ,         CLNT_FAX ,
                       CLNT_ACCNT_NO ,         CLNT_WEF ,         CLNT_TITLE ,
                       CLNT_ZIP_CODE ,         CLNT_BANK_ACC_NO ,         CLNT_CREATED_BY ,
                       CLNT_SMS_TEL ,         CLNT_RUNOFF ,         CLNT_DATE_CREATED ,
                       CLNT_LOADED_BY)
                   SELECT
                       CLNT_CODE1,         CLNT_SHT_DESC1,
                       CLNT_NAME1,         CLNT_OTHER_NAMES1,         CLNT_ID_REG_NO1,
                       CLNT_DOB1,         CLNT_PIN1,         CLNT_PHYSICAL_ADDRS1,
                       CLNT_POSTAL_ADDRS1,         CLNT_EMAIL_ADDRS1,         CLNT_TEL1,
                       CLNT_STATUS1,         CLNT_FAX1,
                       CLNT_ACCNT_NO1,         CLNT_WEF1,         CLNT_TITLE1,
                       CLNT_ZIP_CODE1,         CLNT_BANK_ACC_NO1,         CLNT_CREATED_BY1,
                       CLNT_SMS_TEL1,         CLNT_RUNOFF1,         CLNT_DATE_CREATED1,
                       v_username --    CLNT_LOADED_BY1
                   FROM
                   TQC_LOAD_CLIENTS
                   WHERE
                   CLNT_CODE1 = v_cust_id;

                       -- update flat table as loaded

                   UPDATE TQC_LOAD_CLIENTS
                   SET
                   CLNT_LOADED1 = 'Y'
                   WHERE
                   CLNT_CODE1 = v_cust_id;

               -- SAVE TRANSACTION
               commit;

           END IF;
       END IF;

   exception
       when others then
           raise_when_others('Loading..');

   end;*/
   /*
   PROCEDURE get_duplicates_clnts(v_direct_clnt IN VARCHAR2,
                                  v_clnt_first_name IN VARCHAR2,
                                  v_clnt_surname IN VARCHAR2,
                                  v_clnt_postal_addrs
                                  v_clnt_type IN VARCHAR2
                                  )
       al_id number;
       v_msg varchar2(200):= ' You have not provided the ';
       msg_show boolean:=false;
       v_direct_clnt_seq NUMBER;
       clnt_tab GIS_UTILITIES.clients_tab;
       --v_clnt_surname VARCHAR2(150);
       v_search_criteria VARCHAR2(5);
       v_direct_clnt varchar2(30);
       v_direct_srl_fmt varchar2(30);
       v_clnt_param    varchar2(20);
   begin
       IF NVL(v_direct_clnt,'K') NOT IN ('N', 'Y') THEN
           RAISE_ERROR('Specify if this is a Direct Client');
       END IF;
       IF v_direct_clnt = 'Y' THEN
           BEGIN
               SELECT ACT_ID_SERIAL_FORMAT
               INTO v_direct_srl_fmt
               FROM TQC_ACCOUNT_TYPES
               WHERE ACT_CODE = 1;
           EXCEPTION
               WHEN OTHERS THEN
                   RAISE_ERROR('Error getting direct clients format..');
           END;
           IF v_direct_srl_fmt IS NULL THEN
                       RAISE_ERROR('Provide Id Format for the Direct Account type');
           END IF;
           BEGIN
               SELECT DIRECT_CLNT_SEQ.NEXTVAL INTO v_direct_clnt_seq FROM DUAL;
               v_direct_clnt:= REPLACE(v_direct_srl_fmt,'[SERIALNO]',LPAD(v_direct_clnt_seq,6,0));
           EXCEPTION
               WHEN OTHERS THEN
                   RAISE_ERROR('Error fetching the sequence...');
           END;
           BEGIN
               v_clnt_param := TQC_PARAMETERS_PKG.get_param_varchar('CLIENT_ID_FORMAT');
           EXCEPTION
               WHEN OTHERS THEN
                   RAISE_ERROR('Error fetching client parameter...');
           END;
       END IF;
       IF UPPER(GET_ITEM_PROPERTY('CREATE_CLNT_BTN',LABEL)) != UPPER('Continue?') THEN
           if v_clnt_first_name is null AND v_clnt_type = 'I' then
               RAISE_ERROR('Provide client First Name');
           elsif v_clnt_surname is null then
               RAISE_ERROR('Provide client Surname');
           else
               if v_clnt_postal_addrs is null then
                   v_msg := v_msg||'Postal Address';
                   msg_show := true;
               end if;
               if :NEW_CLNT_BLK.clnt_id_REG_NO is null then
                   v_msg := v_msg||', Client ID/Passport';
                   msg_show := true;
               end if;
               if :NEW_CLNT_BLK.clnt_pin is null then
                   v_msg := v_msg||', Pin number ';
                   msg_show := true;
               end if;
               IF SUBSTR(v_msg,1,1) = ',' THEN
                   v_msg := SUBSTR(v_msg,2);
               END IF;

               if msg_show then
                   v_msg := v_msg||'. Continue creating client?';
                   SET_ALERT_PROPERTY('CANCEL',ALERT_MESSAGE_TEXT,v_msg);
                   al_id := SHOW_ALERT('CANCEL');
                   IF al_id != alert_button1 then
                       RAISE FORM_TRIGGER_FAILURE;
                   END IF;
               END IF;
               v_search_criteria := :SEARCH_CRITERIA;
               IF v_search_criteria != 'NOS' THEN
                   IF v_clnt_type != 'I' THEN
                       v_search_criteria := 'SNE';
                       IF INSTR(LTRIM(v_clnt_surname),'THE') = 1 THEN
                           v_clnt_surname := SUBSTR(LTRIM(v_clnt_surname),4);
                       END IF;
                   END IF;
           --        message('here 1 = '||v_clnt_surname);pause;
                   BEGIN
                       GIS_UTILITIES.clients_QRY(clnt_tab,
                                           LTRIM(v_clnt_first_name),
                                                                    LTRIM(:CLNT_MIDDLE_NAME),
                                           LTRIM(v_clnt_surname),
                                           REPLACE(:NEW_CLNT_BLK.CLNT_POSTAL_ADDRS,'P.O. BOX',''),
                                           :NEW_CLNT_BLK.CLNT_ID_REG_NO,
                                           :NEW_CLNT_BLK.CLNT_PIN,
                                           v_search_criteria) ;
                   EXCEPTION
                       WHEN OTHERS THEN
                           RAISE_ERROR('Error checking existing members..');
                   END;
               --        message('here 2 = '||clnt_tab.count);pause;

                   IF nvl(clnt_tab.count,0) != 0 THEN
                       SET_ALERT_PROPERTY('CFG_INFORMATION',ALERT_MESSAGE_TEXT,'Records matching the information given above found..');
                       al_id := SHOW_ALERT('CFG_INFORMATION');
                       go_block('EXISTING_CLIENTS');
                       FIRST_RECORD;
                       FOR x IN 1..clnt_tab.count LOOP
                           :EXISTING_CLIENTS.CLNT_CODE := clnt_tab(x).CLNT_CODE;
                           :EXISTING_CLIENTS.CLNT_PIN := clnt_tab(x).CLNT_PIN ;
                           :EXISTING_CLIENTS.CLNT_POSTAL_ADDRS := clnt_tab(x).CLNT_POSTAL_ADDRS ;
                           :EXISTING_CLIENTS.CLNT_TEL := clnt_tab(x).CLNT_TEL ;
                           :EXISTING_CLIENTS.CLNT_OTHER_NAMES := clnt_tab(x).CLNT_OTHER_NAMES ;
                           :EXISTING_CLIENTS.CLNT_SURNAME := clnt_tab(x).CLNT_NAME ;
                           :EXISTING_CLIENTS.CLNT_ID_REG_NO := clnt_tab(x).CLNT_ID_REG_NO;
                           :EXISTING_CLIENTS.CLNT_SHT_DESC := clnt_tab(x).CLNT_SHT_DESC;
                           --:EXISTING_CLIENTS.CLNT_POSTAL_CODE := clnt_tab(x).CLNT_POSTAL_CODE;
                           NEXT_RECORD;
                       END LOOP;
                       FIRST_RECORD;
                       SET_ITEM_PROPERTY('CREATE_CLNT_BTN',LABEL,'Continue?');
                   ELSE
                       IF  :NEW_CLNT_BLK.CLNT_SHT_DESC IS NULL THEN
                               BEGIN
                               :NEW_CLNT_BLK.CLNT_SHT_DESC := Gis_Utilities.GET_CLIENT_SHT_DESC(:NEW_CLNT_BLK.CLNT_FIRST_NAME,
                                                                                                                                                                :NEW_CLNT_BLK.CLNT_MIDDLE_NAME,
                                                                                                                               v_clnt_surname);
                               EXCEPTION
                                   WHEN OTHERS THEN
                                       RAISE_ERROR('Error creating client Id...'||:NEW_CLNT_BLK.CLNT_SHT_DESC);
                               END;
                       END IF;
                       IF NVL(v_clnt_param,'DEFAULT')    = 'FMSURNAME' THEN
                           :NEW_CLNT_BLK.CLNT_ACCNT_NO := :NEW_CLNT_BLK.CLNT_SHT_DESC;
                       END IF;
                       IF  :NEW_CLNT_BLK.CLNT_ACCNT_NO IS NULL THEN
                           :NEW_CLNT_BLK.CLNT_ACCNT_NO := v_direct_clnt;
                       END IF;
                       create_clnt_proc;
                   END IF;
               ELSE
               IF  :NEW_CLNT_BLK.CLNT_SHT_DESC IS NULL THEN
                       BEGIN
                       :NEW_CLNT_BLK.CLNT_SHT_DESC := Gis_Utilities.GET_CLIENT_SHT_DESC(:NEW_CLNT_BLK.CLNT_FIRST_NAME,
                                                                                                                                                           :NEW_CLNT_BLK.CLNT_MIDDLE_NAME,
                                                                                                                           v_clnt_surname);
                       EXCEPTION
                           WHEN OTHERS THEN
                               RAISE_ERROR('Error creating client Id...'||:NEW_CLNT_BLK.CLNT_SHT_DESC);
                       END;
               END IF;
               IF NVL(v_clnt_param,'DEFAULT')    = 'FMSURNAME' THEN
                   :NEW_CLNT_BLK.CLNT_ACCNT_NO := :NEW_CLNT_BLK.CLNT_SHT_DESC;
               END IF;
               IF  :NEW_CLNT_BLK.CLNT_ACCNT_NO IS NULL THEN
                        :NEW_CLNT_BLK.CLNT_ACCNT_NO := v_direct_clnt;
               END IF;
                   create_clnt_proc;
               END IF;
           END IF;
       ELSE
           IF  :NEW_CLNT_BLK.CLNT_SHT_DESC IS NULL THEN
               BEGIN
                   :NEW_CLNT_BLK.CLNT_SHT_DESC := Gis_Utilities.GET_CLIENT_SHT_DESC(:NEW_CLNT_BLK.CLNT_FIRST_NAME,
                                                                                   :NEW_CLNT_BLK.CLNT_MIDDLE_NAME,
                                                                                     v_clnt_surname);
               EXCEPTION
                   WHEN OTHERS THEN
                       RAISE_ERROR('Error creating client Id...'||:NEW_CLNT_BLK.CLNT_SHT_DESC);
               END;
           END IF;
           IF NVL(v_clnt_param,'DEFAULT')    = 'FMSURNAME' THEN
               :NEW_CLNT_BLK.CLNT_ACCNT_NO := :NEW_CLNT_BLK.CLNT_SHT_DESC;
           END IF;
           IF  :NEW_CLNT_BLK.CLNT_ACCNT_NO IS NULL THEN
                        :NEW_CLNT_BLK.CLNT_ACCNT_NO := v_direct_clnt;
           END IF;
           create_clnt_proc;
       END IF;
   END;"*/
   PROCEDURE update_image (v_image BLOB, v_client_code NUMBER)
   IS
   BEGIN
      --RAISE_ERROR(v_client_code);
      UPDATE tqc_clients
         SET clnt_image = v_image
       WHERE clnt_code = v_client_code;

      COMMIT;
   END;

   PROCEDURE update_signature (v_image BLOB, v_client_code NUMBER)
   IS
   BEGIN
      --RAISE_ERROR(v_client_code);
      UPDATE tqc_clients
         SET clnt_signature = v_image
       WHERE clnt_code = v_client_code;

      COMMIT;
   END;

   FUNCTION get_images (v_type VARCHAR2, v_client_code NUMBER)
      RETURN clients_image_ref
   IS
      v_retcur   clients_image_ref;
      v_image    BLOB;
   BEGIN
      IF v_type = 'C'
      THEN
         SELECT clnt_image
           INTO v_image
           FROM tqc_clients
          WHERE clnt_code = v_client_code;

         IF v_image IS NULL
         THEN
            OPEN v_retcur FOR
               SELECT org_avatar
                 FROM tqc_organizations
                WHERE org_code = 2;

            RETURN (v_retcur);
         ELSE
            OPEN v_retcur FOR
               SELECT clnt_image
                 FROM tqc_clients
                WHERE clnt_code = v_client_code;

            RETURN (v_retcur);
         END IF;
      ELSE
         OPEN v_retcur FOR
            SELECT org_rpt_logo
              FROM tqc_organizations
             WHERE org_code = 2;

         RETURN (v_retcur);
      END IF;
   END;

   PROCEDURE get_accounts_clients (
      v_cursor      OUT   tqc_accounttrans_cursor,
      v_client_id         VARCHAR2,
      v_client            VARCHAR
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT clnt_sht_desc prp_sht_desc,
                clnt_name || ' ' || clnt_other_names names,
                clnt_code prp_code
           FROM tqc_clients
          WHERE clnt_sht_desc LIKE '%' || v_client_id || '%'
            AND clnt_name || ' ' || clnt_other_names LIKE
                                                        '%' || v_client || '%';
   END;

   PROCEDURE get_tqc_account_types (v_cursor OUT tqc_account_type_cursor)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT act_account_type account_type1, act_code account_type1
           FROM tqc_account_types;
   END;

   PROCEDURE get_ref_cheq_account_types (v_cursor OUT tqc_account_type_cursor)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT act_account_type account_type1, act_code account_type1
           FROM tqc_account_types
          WHERE act_code = 1;
   END;

   PROCEDURE get_tqc_accounts_agents (
      v_acc_type   IN       NUMBER,
      v_cursor     OUT      tqc_accounts_agents_cursor,
      v_agnt_id             VARCHAR2,
      v_agent               VARCHAR2
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT   agn_sht_desc agnt_sht_desc, agn_name agent_name,
                  agn_code agnt_agen_code, act_type_sht_desc
             FROM tqc_agencies, tqc_account_types
            WHERE agn_code != 0
              AND agn_act_code = act_code
              AND agn_act_code = v_acc_type
              AND agn_sht_desc LIKE '%' || v_agnt_id || '%'
              AND agn_name LIKE '%' || v_agent || '%'
         ORDER BY 2;
   END;

   PROCEDURE get_divisions (
      v_brn_code   IN       NUMBER,
      v_cursor     OUT      tqc_divisions_cursor
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT div_name, bdiv_code
           FROM tqc_divisions, tqc_brnch_divisions
          WHERE div_code = bdiv_div_code
            AND bdiv_brn_code = NVL (v_brn_code, bdiv_brn_code);
   END;

   PROCEDURE get_gis_clients (
      v_cursor        OUT   tqc_accounttrans_cursor,
      v_client_id           VARCHAR2,
      v_client_name         VARCHAR2
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT clnt_sht_desc prp_sht_desc,
                clnt_name || ' ' || clnt_other_names NAME,
                clnt_code prp_code
           FROM tqc_clients, tqc_client_systems
          WHERE clnt_code = csys_clnt_code
            AND clnt_sht_desc LIKE '%' || v_client_id || '%'
            AND clnt_name || ' ' || clnt_other_names LIKE
                                                   '%' || v_client_name || '%'
            AND csys_sys_code = 37;
   END;

   PROCEDURE get_client_name (
      v_client_code   IN       NUMBER,
      v_cursor        OUT      tqc_accounttrans_cursor
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT clnt_sht_desc prp_sht_desc,
                clnt_name || ' ' || clnt_other_names NAME,
                clnt_code prp_code
           FROM tqc_clients
          WHERE clnt_code = v_client_code;
   END;

   FUNCTION get_proposer_dtls (v_clnt_code IN NUMBER)
      RETURN clients_detls_ref
   IS
      v_retcur   clients_detls_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,
                clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,
                clnt_postal_addrs, clnt_twn_code, twn_name, clnt_cou_code,
                cou_name, clnt_email_addrs, clnt_tel, clnt_tel2, clnt_status,
                clnt_fax, clnt_remarks, clnt_spcl_terms, clnt_declined_prop,
                clnt_increased_premium, clnt_policy_cancelled, clnt_proposer,
                clnt_accnt_no, clnt_domicile_countries, clnt_wef, clnt_wet,
                clnt_withdrawal_reason, clnt_sec_code, clnt_surname,
                clnt_type, clnt_title, clnt_business, clnt_zip_code,
                clnt_bbr_code, clnt_bank_acc_no, clnt_clnt_code,
                clnt_non_direct, clnt_created_by, clnt_sms_tel,
                clnt_agnt_status, clnt_date_created, clnt_runoff,
                clnt_loaded_by, clnt_direct_client, clnt_old_accnt_no,
                clnt_gender,
                bnk_bank_name || ' - ' || bbr_branch_name bank_branch,
                prp_co_code, co_desc, prp_cover_history, prp_family_history,
                prp_personal_history, clnt_sts_code,
                tqc_setups_cursor.state_name (clnt_sts_code) sts_name,
                prp_type, prp_dob_admitted, clnt_brn_code, brn_name,
                clnt_cntct_name_1, clnt_cntct_phone_1, clnt_cntct_email_1,
                clnt_cntct_name_2, clnt_cntct_phone_2, clnt_cntct_email_2,
                clnt_acc_officer, usr_name, prp_marital_status,
                cou_admin_reg_type, prp_correspondence
           FROM tqc_clients,
                tqc_towns,
                tqc_countries,
                lms_proposers,
                tqc_banks,
                tqc_bank_branches,
                lms_class_occupations,
                tqc_branches,
                tqc_users
          WHERE prp_clnt_code(+) = clnt_code
            AND clnt_twn_code = twn_code(+)
            AND twn_cou_code = cou_code(+)
            AND clnt_cou_code = twn_cou_code(+)
            AND prp_co_code = co_code(+)
            AND clnt_acc_officer = usr_code(+)
            AND clnt_bbr_code = bbr_code(+)
            AND clnt_brn_code = brn_code(+)
            AND bbr_bnk_code = bnk_code(+)
            AND clnt_code = v_clnt_code;

      RETURN (v_retcur);
   END get_proposer_dtls;

   PROCEDURE update_client_status (
      v_client_code   NUMBER,
      v_status        tqc_clients.clnt_status%TYPE
   )
   IS
   BEGIN
      UPDATE tqc_clients
         SET clnt_status = v_status
       WHERE clnt_code = v_client_code;
   END;

   PROCEDURE tqc_client_directors_prc (
      v_add_edit                 IN       VARCHAR2,
      v_clntdir_code             IN       NUMBER,
      v_clntdir_clnt_code                 NUMBER,
      v_clntdir_year             IN       NUMBER,
      v_clntdir_name             IN       VARCHAR2,
      v_clntdir_qualifications   IN       VARCHAR2,
      v_clntdir_pct_holdg        IN       NUMBER,
      v_clntdir_designation      IN       VARCHAR2,
      v_err                      OUT      VARCHAR2
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_client_directors
                     (clntdir_code,
                      clntdir_clnt_code, clntdir_year, clntdir_name,
                      clntdir_qualifications, clntdir_pct_holdg,
                      clntdir_designation
                     )
              VALUES (tq_crm.client_directors_seq.NEXTVAL,
                      v_clntdir_clnt_code, v_clntdir_year, v_clntdir_name,
                      v_clntdir_qualifications, v_clntdir_pct_holdg,
                      v_clntdir_designation
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_client_directors
            SET clntdir_clnt_code = v_clntdir_clnt_code,
                clntdir_year = v_clntdir_year,
                clntdir_name = v_clntdir_name,
                clntdir_qualifications = v_clntdir_qualifications,
                clntdir_pct_holdg = v_clntdir_pct_holdg,
                clntdir_designation = v_clntdir_designation
          WHERE clntdir_code = v_clntdir_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE      tqc_client_directors
               WHERE clntdir_code = v_clntdir_code;
      END IF;
   END;

   PROCEDURE get_tqc_client_directors (
      v_clnt_code   IN       NUMBER,
      v_refcur      IN OUT   tqc_client_directors_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT *
           FROM tqc_client_directors
          WHERE clntdir_clnt_code = v_clnt_code;
   END;

   PROCEDURE tqc_client_auditors_prc (
      v_add_edit                 IN       VARCHAR2,
      v_clntaud_code             IN       NUMBER,
      v_clntaud_clnt_code                 NUMBER,
      v_clntaud_year             IN       NUMBER,
      v_clntaud_name             IN       VARCHAR2,
      v_clntaud_qualifications   IN       VARCHAR2,
      v_clntaud_telephone        IN       VARCHAR2,
      v_clntaud_designation      IN       VARCHAR2,
      v_err                      OUT      VARCHAR2
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_client_auditors
                     (clntaud_code,
                      clntaud_clnt_code, clntaud_year, clntaud_name,
                      clntaud_qualifications, clntaud_telephone,
                      clntaud_designation
                     )
              VALUES (tq_crm.client_auditors_seq.NEXTVAL,
                      v_clntaud_clnt_code, v_clntaud_year, v_clntaud_name,
                      v_clntaud_qualifications, v_clntaud_telephone,
                      v_clntaud_designation
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_client_auditors
            SET clntaud_clnt_code = v_clntaud_clnt_code,
                clntaud_year = v_clntaud_year,
                clntaud_name = v_clntaud_name,
                clntaud_qualifications = v_clntaud_qualifications,
                clntaud_telephone = v_clntaud_telephone,
                clntaud_designation = v_clntaud_designation
          WHERE clntaud_code = v_clntaud_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE      tqc_client_auditors
               WHERE clntaud_code = v_clntaud_code;
      END IF;
   END;

   PROCEDURE get_tqc_client_auditors (
      v_clnt_code   IN       NUMBER,
      v_refcur      IN OUT   tqc_client_auditors_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT *
           FROM tqc_client_auditors
          WHERE clntaud_clnt_code = v_clnt_code;
   END;

   PROCEDURE get_parentcompany (v_refcur OUT get_parentcompany_ref)
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names
           FROM tqc_clients
          WHERE clnt_proposer = 'Y';
   END;

   FUNCTION getclientscount (v_where VARCHAR2)
      RETURN NUMBER
   IS
      v_count    NUMBER;
      sql_stmt   VARCHAR2 (200);
      var_str    VARCHAR2 (200);
   BEGIN
      var_str := 'select COUNT(*) from TQC_CLIENTS ' || v_where;

      EXECUTE IMMEDIATE var_str
                   INTO v_count;

      --sql_stmt := 'SELECT COUNT(*) INTO '||v_count ||' FROM TQC_CLIENTS'
      --           || v_where ;
      --EXECUTE IMMEDIATE sql_stmt USING v_where;
      RETURN v_count;
   END;

   PROCEDURE tqc_client_complaints_prc (
      v_add_edit         IN       VARCHAR2,
      v_comp_code        IN       NUMBER,
      v_comp_clnt_code   IN       NUMBER,
      v_comp_subject     IN       VARCHAR2,
      v_comp_message     IN       VARCHAR2,
      v_err              OUT      VARCHAR2
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_client_complaints
                     (comp_code, comp_clnt_code,
                      comp_subject, comp_message
                     )
              VALUES (tq_crm.comp_code_seq.NEXTVAL, v_comp_clnt_code,
                      v_comp_subject, v_comp_message
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_client_complaints
            SET comp_clnt_code = v_comp_clnt_code,
                comp_subject = v_comp_subject,
                comp_message = v_comp_message
          WHERE comp_code = v_comp_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE      tqc_client_complaints
               WHERE comp_code = v_comp_code;
      END IF;
   END;

   PROCEDURE updateaccountofficer (
      v_err        OUT      VARCHAR2,
      v_user_old   IN       NUMBER,
      v_user_new   IN       NUMBER
   )
   IS
   BEGIN
      UPDATE tqc_clients
         SET clnt_acc_officer = v_user_new
       WHERE clnt_acc_officer = v_user_old;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_err := 'ERROR UPDATING ACCOUNT OFFICER--------------';
   END;

   PROCEDURE authorise_client (
      v_err           OUT      VARCHAR2,
      v_client_code   IN       NUMBER,
      v_status        IN       VARCHAR2
   )
   IS
   BEGIN
      UPDATE tqc_clients
         SET clnt_status = v_status
       WHERE clnt_code = v_client_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_err := 'ERROR AUTHORISING CLIENT--------------';
   END;

   PROCEDURE delete_client (v_err OUT VARCHAR2, v_client_code IN NUMBER)
   IS
   BEGIN
      DELETE FROM tqc_clients
            WHERE clnt_code = v_client_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_err := 'ERROR AUTHORISING CLIENT--------------';
   END;

   FUNCTION create_client_code (
      v_surname       IN   VARCHAR2,
      v_other_names   IN   VARCHAR2,
      v_id_number     IN   VARCHAR,
      v_tel_number    IN   VARCHAR2
   )
      RETURN VARCHAR2
   IS
      --ASSUMPTION : THE CLIENT CODE MUST BE ATMOST 8 DIGITS LONG
      --           : THE ID NUMBER INPUT IS ALWAYS UNIQUE PER CLIENT HENCE IF PASSED A CHECK IS DONE
      --             TO VERIFY IF A CLIENT CODE FOR THE CLIENT HAD BEEN GENERETED PREVIOUSLY
      v_count         NUMBER;
      v_shrtdesc      VARCHAR (50);
      v_client_code   VARCHAR (50);
   BEGIN
      --CHECK FOR NULL INPUTS
      IF v_surname IS NULL OR LTRIM (v_surname) = ''
      THEN
         raise_error ('SURNAME MUST BE SUPPLIED');
      ELSIF v_id_number IS NULL OR LTRIM (v_id_number) = ''
      THEN
         raise_error ('ID NUMBER MUST BE SUPPLIED');
      ELSIF v_tel_number IS NULL OR LTRIM (v_tel_number) = ''
      THEN
         raise_error ('TEL NO. MUST BE SUPPLIED');
      END IF;

      --check if id Number exists already
      BEGIN
         SELECT COUNT (*)
           INTO v_count
           FROM tqc_clients
          WHERE clnt_id_reg_no = v_id_number;

         IF v_count != 0
         THEN
            SELECT clnt_code
              INTO v_client_code
              FROM tqc_clients
             WHERE clnt_id_reg_no = v_id_number;

            RETURN v_client_code;
         END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error
               ('ERROR OCCURED WHILE DETERMINING IF A CLIENT CODE ALREADY EXISTS'
               );
      END;

      v_client_code :=
         client_extended_proc
               ('Y',
                NULL,                     --v_sht_desc            IN VARCHAR2,
                v_surname,                --v_first_name          IN VARCHAR2,
                v_other_names,            --v_middle_name         IN VARCHAR2,
                v_surname,                --v_surname             IN VARCHAR2,
                NULL,                     --v_pin                 IN VARCHAR2,
                NULL,                     --v_postal_addrs        IN VARCHAR2,
                NULL,                     --v_physical_addrs      IN VARCHAR2,
                v_id_number,              --v_id_reg_no           IN VARCHAR2,
                'ADMIN',                  --v_user                IN VARCHAR2,
                TRUNC (SYSDATE),              --v_wef                 IN DATE,
                NULL,                        -- v_wet                 IN DATE,
                NULL,                     --v_title               IN VARCHAR2,
                NULL,                         --v_dob                 IN DATE,
                NULL,                       --v_cou_code            IN NUMBER,
                NULL,                       --v_twn_code            IN NUMBER,
                NULL,                     --v_zip_code            IN VARCHAR2,
                NULL,                     --v_email_addrs         IN VARCHAR2,
                v_tel_number,             --v_tel                 IN VARCHAR2,
                NULL,                     --v_sms_tel             IN VARCHAR2,
                NULL,                     --v_fax                 IN VARCHAR2,
                NULL,                       --v_sec_code            IN NUMBER,
                NULL,                     --v_business            IN VARCHAR2,
                NULL,                       --v_domicile_countries  IN NUMBER,
                NULL,                     --v_proposer            IN VARCHAR2,
                'A',                      --v_status              IN VARCHAR2,
                NULL,                     --v_runoff              IN VARCHAR2,
                NULL,                     --v_withdrawal_reason   IN VARCHAR2,
                NULL,                     --v_remarks             IN VARCHAR2,
                NULL,                     --v_bank_acc_no         IN VARCHAR2,
                NULL,                       --v_bbr_code            IN NUMBER,
                NULL,                     --v_spcl_terms          IN VARCHAR2,
                NULL,                     --v_policy_cancelled    IN VARCHAR2,
                NULL,                     --v_increased_premium   IN VARCHAR2,
                NULL,                     --v_declined_prop       IN VARCHAR2,
                NULL,                     --v_client_type         IN VARCHAR2,
                'A',                      --v_add_edit            IN VARCHAR2,
                NULL,                       --v_clnt_code           IN NUMBER,
                NULL,                       --v_acc_mngr_code       IN NUMBER,
                NULL,          --v_CLNT_CNTCT_NAME_1 IN VARCHAR2 DEFAULT NULL,
                NULL,         --v_CLNT_CNTCT_PHONE_1 IN VARCHAR2 DEFAULT NULL,
                NULL,         --v_CLNT_CNTCT_EMAIL_1 IN VARCHAR2 DEFAULT NULL,
                NULL,          --v_CLNT_CNTCT_NAME_2 IN VARCHAR2 DEFAULT NULL,
                NULL,         --v_CLNT_CNTCT_PHONE_2 IN VARCHAR2 DEFAULT NULL,
                NULL,         --v_CLNT_CNTCT_EMAIL_2 IN VARCHAR2 DEFAULT NULL,
                NULL,                --v_passport_no IN VARCHAR2 DEFAULT NULL,
                NULL,
                     --v_sts_code           IN TQC_CLIENTS.CLNT_STS_CODE%TYPE,
                NULL,                     --v_website            IN  VARCHAR2,
                NULL,                     --v_auditors           IN  VARCHAR2,
                NULL,                       --v_parent_company     IN  NUMBER,
                NULL,                     --v_current_insurer    IN  VARCHAR2,
                NULL,                        --v_projectedBiz       IN NUMBER,
                NULL,                          --v_date_of_employ     IN DATE,
                NULL,                      --v_driving_licence    IN VARCHAR2,
                163,                         --v_brn_code           IN Number,
                NULL,                         --v_clnt_image         IN Blob ,
                NULL,                         --v_signature          IN Blob ,
                NULL,                        --v_clnt_acc_officer   IN Number,
                NULL,                                --v_gender   IN VARCHAR2,
                NULL,                        --v_clnt_occupation  IN VARCHAR2,
                NULL,                      --v_clnt_bank_phone_no IN VARCHAR2,
                NULL,                       --v_clnt_bank_cell_no IN VARCHAR2,
                NULL,                  --v_clnt_employer_phone_no IN VARCHAR2,
                NULL,                   --v_clnt_employer_cell_no IN VARCHAR2,
                NULL,                             --v_clt_cell_no IN VARCHAR2,
                NULL,
                v_client_code,
                --vclntcode            OUT      TQC_CLIENTS .CLNT_CODE%TYPE,
                v_shrtdesc,
                -- v_clnt_sht_desc      OUT      TQC_CLIENTS.CLNT_SHT_DESC%TYPE
                NULL,
                NULL,
                NULL,
                NULL,
                NULL,
                NULL,
                NULL
               );
      RETURN (v_client_code);
   END create_client_code;

   PROCEDURE clientagent (
      v_addedit       IN   VARCHAR2,
      v_client_code   IN   NUMBER,
      v_agentcode     IN   NUMBER
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_addedit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_agency_clients
                        (agnc_code, agnc_agn_code,
                         agnc_clnt_code
                        )
                 VALUES (tqc_agnc_code_seq.NEXTVAL, v_agentcode,
                         v_client_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR INSERTING RECORD');
         END;
      ELSIF v_addedit = 'E'
      THEN
         BEGIN
            SELECT agnc_clnt_code
              INTO v_count
              FROM tqc_agency_clients
             WHERE agnc_clnt_code = v_client_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         IF NVL (v_count, 0) > 0
         THEN
            UPDATE tqc_agency_clients
               SET agnc_agn_code = v_agentcode
             WHERE agnc_clnt_code = v_client_code;
         ELSE
            INSERT INTO tqc_agency_clients
                        (agnc_code, agnc_agn_code,
                         agnc_clnt_code
                        )
                 VALUES (tqc_agnc_code_seq.NEXTVAL, v_agentcode,
                         v_client_code
                        );
         END IF;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_agency_clients
               WHERE agnc_agn_code = v_agentcode
                 AND agnc_clnt_code = v_client_code;
      END IF;
   END;

   FUNCTION getclientsagent (v_clientcode IN NUMBER)
      RETURN client_agent_ref
   IS
      v_cursor   client_agent_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT agn_code, agn_sht_desc, agn_name
           FROM tqc_agencies, tqc_agency_clients
          WHERE agn_code = agnc_agn_code AND agnc_clnt_code = v_clientcode;

      RETURN v_cursor;
   END;

   FUNCTION getagentclients (v_agentcode IN NUMBER)
      RETURN agent_client_ref
   IS
      v_cursor   agent_client_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT clnt_code, clnt_sht_desc,
                clnt_name || '  ' || clnt_other_names
           FROM tqc_clients, tqc_agency_clients
          WHERE clnt_code = agnc_clnt_code AND agnc_agn_code = v_agentcode;

      RETURN v_cursor;
   END;

   FUNCTION getagentnonclients
      RETURN agent_client_ref
   IS
      v_cursor   agent_client_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT clnt_code, clnt_sht_desc,
                clnt_name || '  ' || clnt_other_names
           FROM tqc_clients
          WHERE clnt_direct_client = 'N'
            AND clnt_code NOT IN (SELECT agnc_clnt_code
                                    FROM tqc_agency_clients);

      RETURN v_cursor;
   END;

   FUNCTION create_client_proc (
      v_direct_clnt            IN   VARCHAR2,
      v_sht_desc               IN   VARCHAR2,
      v_surname                IN   VARCHAR2,
      v_othernames             IN   VARCHAR2,
      v_clientname             IN   VARCHAR2,
      v_pin                    IN   VARCHAR2,
      v_postal_addrs           IN   VARCHAR2,
      v_physical_addrs         IN   VARCHAR2,
      v_id_reg_no              IN   VARCHAR2,
      v_user                   IN   VARCHAR2,
      v_wef                    IN   DATE,
      v_wet                    IN   DATE,
      --v_direct_client          IN VARCHAR2,
      v_title                  IN   VARCHAR2,
      v_dob                    IN   DATE,
      v_cou_code               IN   NUMBER,
      v_twn_code               IN   NUMBER,
      v_zip_code               IN   VARCHAR2,
      v_email_addrs            IN   VARCHAR2,
      v_tel                    IN   VARCHAR2,
      v_sms_tel                IN   VARCHAR2,
      v_fax                    IN   VARCHAR2,
      v_sec_code               IN   NUMBER,
      v_business               IN   VARCHAR2,
      v_domicile_countries     IN   NUMBER,
      v_proposer               IN   VARCHAR2,
      v_status                 IN   VARCHAR2,
      v_runoff                 IN   VARCHAR2,
      v_withdrawal_reason      IN   VARCHAR2,
      v_remarks                IN   VARCHAR2,
      v_bank_acc_no            IN   VARCHAR2,
      v_bbr_code               IN   NUMBER,
      v_spcl_terms             IN   VARCHAR2,
      v_policy_cancelled       IN   VARCHAR2,
      v_increased_premium      IN   VARCHAR2,
      v_declined_prop          IN   VARCHAR2,
      v_client_type            IN   VARCHAR2,
      v_add_edit               IN   VARCHAR2,
      v_clnt_code              IN   NUMBER,
      v_sex                         VARCHAR2 DEFAULT NULL,
      v_prp_code                    lms_proposers.prp_code%TYPE DEFAULT NULL,
      v_prp_co_code                 lms_proposers.prp_co_code%TYPE
            DEFAULT NULL,
      v_prp_business                lms_proposers.prp_business%TYPE
            DEFAULT NULL,
      v_prp_cover_history           lms_proposers.prp_cover_history%TYPE
            DEFAULT NULL,
      v_prp_family_history          lms_proposers.prp_family_history%TYPE
            DEFAULT NULL,
      v_prp_personal_history        lms_proposers.prp_personal_history%TYPE
            DEFAULT NULL,
      v_sts_code                    tqc_states.sts_code%TYPE DEFAULT NULL,
      v_prp_type                    lms_proposers.prp_type%TYPE DEFAULT NULL,
      v_dob_admitted                lms_proposers.prp_dob_admitted%TYPE
            DEFAULT NULL,
      v_clnt_brn_code               tqc_clients.clnt_brn_code%TYPE
            DEFAULT NULL,
      v_clnt_cntct_name_1           tqc_clients.clnt_cntct_name_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_phone_1          tqc_clients.clnt_cntct_phone_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_email_1          tqc_clients.clnt_cntct_email_1%TYPE
            DEFAULT NULL,
      v_clnt_cntct_name_2           tqc_clients.clnt_cntct_name_2%TYPE
            DEFAULT NULL,
      v_clnt_cntct_phone_2          tqc_clients.clnt_cntct_phone_2%TYPE
            DEFAULT NULL,
      v_clnt_cntct_email_2          tqc_clients.clnt_cntct_email_2%TYPE
            DEFAULT NULL,
      v_clnt_acc_officer            tqc_clients.clnt_acc_officer%TYPE
            DEFAULT NULL,
      v_prp_marital_status          lms_proposers.prp_marital_status%TYPE
            DEFAULT NULL,
      v_prp_correspondence          lms_proposers.prp_correspondence%TYPE
            DEFAULT NULL
   --v_acc_mngr_code       IN NUMBER
   )
      RETURN NUMBER
   IS
      vclntcode             NUMBER;
      v_clnt_count          NUMBER;
      v_direct_clnt_seq     NUMBER;
      --v_direct_clnt varchar2(30);
      --v_direct_clnt varchar2(30);
      v_direct_srl_fmt      VARCHAR2 (100);
      v_clnt_param          VARCHAR2 (20);
      v_accnt_no            VARCHAR2 (35);
      v_clnt_sht_desc       VARCHAR2 (30);
      v_use_names_in_code   VARCHAR2 (1)   := 'Y';
   --v_cnt NUMBER;
   --x NUMBER:=1;
   --al_id number;
   BEGIN
      --RAISE_ERROR(v_wet||';'||v_wef);
          -- This is not an UPDATE. We are creating a new Client
      IF v_add_edit = 'A'
      THEN
         IF NVL (v_direct_clnt, 'K') NOT IN ('N', 'Y')
         THEN
            raise_error ('Specify if this is a Direct Client');
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

            BEGIN
               SELECT direct_clnt_seq.NEXTVAL
                 INTO v_direct_clnt_seq
                 FROM DUAL;

               -- v_accnt_no:= REPLACE(v_direct_srl_fmt,'[SERIALNO]',LPAD(v_direct_clnt_seq,6,0));
               v_accnt_no := LPAD (v_direct_clnt_seq, 6, 0);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error fetching the sequence...');
            END;

            BEGIN
               v_clnt_param :=
                    tqc_parameters_pkg.get_param_varchar ('CLIENT_ID_FORMAT');
            -- TQC_PARAMETERS
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
                                                       v_clnt_brn_code,
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
                  get_client_sht_desc (v_surname, v_othernames, v_clientname);
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

         BEGIN
            ---RAISE_ERROR(v_surname);
            SELECT COUNT (clnt_code)
              INTO v_clnt_count
              FROM tqc_clients
             WHERE clnt_name = LTRIM (RTRIM (v_clientname))
               AND clnt_other_names = LTRIM (RTRIM (v_othernames))
               AND clnt_postal_addrs = v_postal_addrs
               AND clnt_title = v_title;

            IF v_clnt_count = 0
            THEN                    --CLIENT DOESN'T EXIST, ADD THE NEW CLIENT
               INSERT INTO tqc_clients
                           (clnt_code, clnt_pin, clnt_sht_desc,
                            clnt_postal_addrs, clnt_physical_addrs,
                            clnt_other_names,
                            clnt_name, clnt_id_reg_no,
                            clnt_date_created, clnt_created_by, clnt_wef,
                            clnt_wet, clnt_direct_client, clnt_title,
                            clnt_dob, clnt_cou_code, clnt_twn_code,
                            clnt_zip_code, clnt_email_addrs, clnt_tel,
                            clnt_sms_tel, clnt_fax, clnt_sec_code,
                            clnt_business, clnt_domicile_countries,
                            clnt_accnt_no, clnt_proposer, clnt_status,
                            clnt_runoff, clnt_withdrawal_reason,
                            clnt_remarks, clnt_bank_acc_no, clnt_bbr_code,
                            clnt_clnt_code, clnt_spcl_terms,
                            clnt_policy_cancelled, clnt_increased_premium,
                            clnt_declined_prop, clnt_type, clnt_usr_code,
                            clnt_gender, clnt_sts_code, clnt_brn_code,
                            clnt_cntct_name_1, clnt_cntct_phone_1,
                            clnt_cntct_email_1, clnt_cntct_name_2,
                            clnt_cntct_phone_2, clnt_cntct_email_2,
                            clnt_acc_officer, clnt_surname,
                            clnt_marital_status
                           )
                    VALUES (vclntcode, v_pin, v_clnt_sht_desc,
                            v_postal_addrs, v_physical_addrs,
                            LTRIM (RTRIM (v_othernames)),
                            LTRIM (RTRIM (v_clientname)), v_id_reg_no,
                            TRUNC (SYSDATE), v_user, v_wef,
                            v_wet, v_direct_clnt, v_title,
                            v_dob, v_cou_code, v_twn_code,
                            v_zip_code, v_email_addrs, v_tel,
                            v_sms_tel, v_fax, v_sec_code,
                            v_business, v_domicile_countries,
                            v_accnt_no, v_proposer, v_status,
                            v_runoff, v_withdrawal_reason,
                            v_remarks, v_bank_acc_no, v_bbr_code,
                            v_clnt_code, v_spcl_terms,
                            v_policy_cancelled, v_increased_premium,
                            v_declined_prop, v_client_type, NULL,
                            NVL (v_sex, 'M'), v_sts_code, v_clnt_brn_code,
                            v_clnt_cntct_name_1, v_clnt_cntct_phone_1,
                            v_clnt_cntct_email_1, v_clnt_cntct_name_2,
                            v_clnt_cntct_phone_2, v_clnt_cntct_email_2,
                            v_clnt_acc_officer, v_surname,
                            v_prp_marital_status
                           );
            ELSE
               SELECT clnt_code
                 INTO vclntcode
                 FROM tqc_clients
                WHERE clnt_name = LTRIM (RTRIM (v_clientname))
                  AND clnt_other_names = LTRIM (RTRIM (v_othernames))
                  AND clnt_postal_addrs = v_postal_addrs
                  AND clnt_title = v_title
                  AND ROWNUM = 1;
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error creating the client..');
         END;

         RETURN (vclntcode);
      ELSIF v_add_edit = 'E'
      THEN                                                -- This is an UPDATE
         ---RAISE_ERROR(v_othernames||';'||v_surname);
         UPDATE tqc_clients
            SET clnt_pin = v_pin,
                --CLNT_SHT_DESC=v_sht_desc,
                clnt_postal_addrs = v_postal_addrs,
                clnt_physical_addrs = v_physical_addrs,
                clnt_other_names = v_othernames,
                clnt_name = LTRIM (RTRIM (v_clientname)),
                clnt_id_reg_no = v_id_reg_no,
                clnt_direct_client = v_direct_clnt,
                clnt_title = v_title,
                clnt_dob = v_dob,
                clnt_cou_code = v_cou_code,
                clnt_twn_code = v_twn_code,
                clnt_zip_code = v_zip_code,
                clnt_email_addrs = v_email_addrs,
                clnt_tel = v_tel,
                clnt_sms_tel = v_sms_tel,
                clnt_fax = v_fax,
                clnt_sec_code = v_sec_code,
                clnt_business = v_business,
                clnt_domicile_countries = v_domicile_countries,
                clnt_accnt_no = v_accnt_no,
                clnt_proposer = v_proposer,
                clnt_status = v_status,
                clnt_runoff = v_runoff,
                clnt_withdrawal_reason = v_withdrawal_reason,
                clnt_remarks = v_remarks,
                clnt_bank_acc_no = v_bank_acc_no,
                clnt_bbr_code = v_bbr_code,
                clnt_clnt_code = v_clnt_code,
                clnt_spcl_terms = v_spcl_terms,
                clnt_policy_cancelled = v_policy_cancelled,
                clnt_increased_premium = v_increased_premium,
                clnt_declined_prop = v_declined_prop,
                clnt_type = v_client_type,
                clnt_gender = v_sex,
                clnt_sts_code = v_sts_code,
                clnt_brn_code = v_clnt_brn_code,
                clnt_cntct_name_1 = v_clnt_cntct_name_1,
                clnt_cntct_phone_1 = v_clnt_cntct_phone_1,
                clnt_cntct_email_1 = v_clnt_cntct_email_1,
                clnt_cntct_name_2 = v_clnt_cntct_name_2,
                clnt_cntct_phone_2 = v_clnt_cntct_phone_2,
                clnt_cntct_email_2 = v_clnt_cntct_email_2,
                clnt_acc_officer = v_clnt_acc_officer,
                clnt_surname = v_surname,
                clnt_wef = v_wef,
                clnt_wet = v_wet,
                clnt_marital_status = v_prp_marital_status
          WHERE clnt_code = v_clnt_code;

         update_proposer (v_clnt_code,
                          v_prp_code,
                          v_prp_co_code,
                          v_prp_business,
                          v_prp_cover_history,
                          v_prp_family_history,
                          v_prp_personal_history,
                          v_zip_code,
                          v_prp_type,
                          v_dob_admitted,
                          v_prp_marital_status,
                          v_prp_correspondence
                         );
         RETURN (v_clnt_code);
      ELSIF v_add_edit = 'D'
      THEN
         -- Step 1: Remove the Client Documents
         DELETE FROM tqc_client_documents
               WHERE cdocr_clnt_code = v_clnt_code;

         -- Step 2: Remove the Client Web Accounts
         DELETE FROM tqc_client_web_accounts
               WHERE acwa_clnt_code = v_clnt_code;

         -- Step 3: Remove the Clients Systems
         DELETE FROM tqc_client_systems
               WHERE csys_clnt_code = v_clnt_code;

         -- Step 4: Delete the Client
         DELETE FROM tqc_clients
               WHERE clnt_code = v_clnt_code;

         RETURN (v_clnt_code);
      END IF;
   END create_client_proc;

   FUNCTION getclientcontacts (
      v_clntcode   tqc_client_contacts.clco_clnt_code%TYPE
   )
      RETURN client_contacts_ref
   IS
      v_cursor   client_contacts_ref;
      v_paramValue  VARCHAR2(1) := 'N';
   BEGIN
      BEGIN
        SELECT PARAM_VALUE 
        INTO v_paramValue
        FROM TQC_PARAMETERS WHERE PARAM_NAME  = 'CLIENT_CONTACT_DEBTOR';
      EXCEPTION WHEN OTHERS THEN
        NULL;
      END;
      IF NVL(v_paramValue,'N') = 'N' THEN
      OPEN v_cursor FOR
         SELECT clco_code, clco_clnt_code, clco_name, clco_postal_addrs,
                clco_physical_addrs, clco_sec_code, sec_name
           FROM tqc_client_contacts, tqc_sectors
          WHERE clco_sec_code = sec_code(+) AND clco_clnt_code = v_clntcode;

      RETURN v_cursor;
      ELSE
      OPEN v_cursor FOR
         SELECT clco_code, clco_clnt_code, clco_name, clco_postal_addrs,
                clco_physical_addrs, clco_sec_code, clna_name
           FROM tqc_client_contacts, tqc_client_accounts
          WHERE clco_sec_code = clna_code(+) AND clco_clnt_code = v_clntcode;

      RETURN v_cursor;
      END IF;
   END;

   PROCEDURE client_contacts_proc (
      v_addedit                 VARCHAR2,
      v_clntcont_tab   IN       tqc_client_contacts_tab,
      v_clcocode       OUT      tqc_client_contacts.clco_code%TYPE
   )
   IS
   BEGIN
      IF v_clntcont_tab.COUNT = 0
      THEN
         raise_error ('No Client Contact data provided..');
      END IF;

      FOR i IN 1 .. v_clntcont_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT clco_code_seq.NEXTVAL
              INTO v_clcocode
              FROM DUAL;

            INSERT INTO tqc_client_contacts
                        (clco_code, clco_clnt_code,
                         clco_name,
                         clco_postal_addrs,
                         clco_physical_addrs,
                         clco_sec_code
                        )
                 VALUES (v_clcocode, v_clntcont_tab (i).clco_clnt_code,
                         v_clntcont_tab (i).clco_name,
                         v_clntcont_tab (i).clco_postal_addrs,
                         v_clntcont_tab (i).clco_physical_addrs,
                         v_clntcont_tab (i).clco_sec_code
                        );
         ELSIF v_addedit = 'E'
         THEN
            v_clcocode := v_clntcont_tab (i).clco_code;

            UPDATE tqc_client_contacts
               SET clco_clnt_code = v_clntcont_tab (i).clco_clnt_code,
                   clco_name = v_clntcont_tab (i).clco_name,
                   clco_postal_addrs = v_clntcont_tab (i).clco_postal_addrs,
                   clco_physical_addrs =
                                        v_clntcont_tab (i).clco_physical_addrs,
                   clco_sec_code = v_clntcont_tab (i).clco_sec_code
             WHERE clco_code = v_clntcont_tab (i).clco_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_client_contacts
                  WHERE clco_code = v_clntcont_tab (i).clco_code;
         END IF;
      END LOOP;
   END client_contacts_proc;

   FUNCTION getholdingclients
      RETURN getholdingclients_ref
   IS
      v_cursor   getholdingclients_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT clnt_code, clnt_sht_desc,
                gis_utilities.clnt_name (clnt_name,
                                         clnt_other_names
                                        ) clnt_name,
                clnt_clnt_code
           FROM tqc_clients
          WHERE clnt_proposer = 'Y';

      RETURN v_cursor;
   END;

   FUNCTION getclienttypes
      RETURN getclienttypes_ref
   IS
      v_cursor   getclienttypes_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT clnty_code, clnty_clnt_type, clnty_category
           FROM tqc_client_types;

      RETURN v_cursor;
   END;

-- FUNCTION getClientTypes RETURN getClientTypes_ref IS
--        v_cursor  getClientTypes_ref;
--BEGIN
--    OPEN v_cursor FOR
--    SELECT CLNTY_CODE, CLNTY_CLNT_TYPE, CLNTY_CATEGORY
--    FROM TQC_CLIENT_TYPES;
--   RETURN v_cursor;
--END;
   PROCEDURE client_type_proc (
      v_addedit           IN   VARCHAR2,
      v_clnty_code        IN   NUMBER,
      v_clnty_clnt_type   IN   VARCHAR2,
      v_clnty_category    IN   VARCHAR2
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_client_types
                        (clnty_code, clnty_clnt_type,
                         clnty_category
                        )
                 VALUES (tqc_clnty_code_seq.NEXTVAL, v_clnty_clnt_type,
                         v_clnty_category
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR INSERTING RECORD');
         END;
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_client_types
            SET clnty_clnt_type = v_clnty_clnt_type,
                clnty_category = v_clnty_category
          WHERE clnty_code = v_clnty_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_client_types
               WHERE clnty_code = v_clnty_code;
      END IF;
   END;

   PROCEDURE get_clnt_types (v_cursor OUT getclttypes_ref)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT clnty_clnt_type
           FROM tqc_client_types;
   END;

   PROCEDURE agencyclients (v_agentcode NUMBER, v_clientcode NUMBER)
   IS
      v_count      NUMBER;
      v_agnccode   NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (*)
           INTO v_count
           FROM tqc_agency_clients
          WHERE agnc_agn_code = v_agentcode AND agnc_clnt_code = v_clientcode;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      BEGIN
         SELECT MAX (agnc_code)
           INTO v_agnccode
           FROM tqc_agency_clients;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      v_agnccode := NVL (v_agnccode, 0) + 1;

      IF NVL (v_count, 0) = 0
      THEN
         INSERT INTO tqc_agency_clients
                     (agnc_code, agnc_agn_code, agnc_clnt_code
                     )
              VALUES (v_agnccode, v_agentcode, v_clientcode
                     );
      END IF;
   END agencyclients;

   PROCEDURE check_if_id_unique (
      v_id_number   IN       VARCHAR2,
      v_message     OUT      VARCHAR2
   )
   IS
      v_cnt   NUMBER;
   BEGIN
      v_message := NULL;

      SELECT COUNT (1)
        INTO v_cnt
        FROM tqc_clients
       WHERE clnt_id_reg_no = v_id_number;

      IF NVL (v_cnt, 0) > 0
      THEN
         v_message :=
                    'That Id is not Unique.Please provide a unique id number';
      END IF;
   END;

   FUNCTION getrenparameters (v_param_name IN VARCHAR2)
      RETURN renparameters_ref
   IS
      v_cursor   renparameters_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT param_value, param_desc
           FROM tqc_parameters
          WHERE param_name = v_param_name;

      RETURN v_cursor;
   END;

   PROCEDURE saverenewalmessages (
      v_sms_sys_code     IN   NUMBER,
      v_sms_sys_module   IN   VARCHAR2,
      v_sms_clnt_code    IN   NUMBER,
      v_sms_agn_code     IN   NUMBER,
      v_sms_pol_code     IN   NUMBER,
      v_sms_pol_no       IN   VARCHAR2,
      v_sms_tel_no       IN   VARCHAR2,
      v_sms_status       IN   VARCHAR2,
      v_renewaldate      IN   DATE,
      v_clientname       IN   VARCHAR2
   )
   IS
      lv_temp_txt   VARCHAR2 (32767);
   BEGIN
      BEGIN
         SELECT msgt_msg
           INTO lv_temp_txt
           FROM tqc_msg_templates
          WHERE msgt_sys_code = 37
            AND msgt_sys_module = 'RN'
            AND msgt_type = 'SMS'
            AND NVL (msgt_default, 'N') = 'Y';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No template is defined for renewal process......');
      END;

      lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_sms_pol_no);
      lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENTNAME]', v_clientname);
      lv_temp_txt :=
         REPLACE (lv_temp_txt,
                  '[RENEWALDATE]',
                  TO_CHAR (v_renewaldate, 'DD/MM/RRRR')
                 );

      BEGIN
         INSERT INTO tqc_sms_messages
                     (sms_code, sms_sys_code,
                      sms_sys_module, sms_clnt_code, sms_agn_code,
                      sms_pol_code, sms_pol_no, sms_tel_no,
                      sms_msg, sms_status
                     )
              VALUES (tqc_sms_code_seq.NEXTVAL, v_sms_sys_code,
                      v_sms_sys_module, v_sms_clnt_code, v_sms_agn_code,
                      v_sms_pol_code, v_sms_pol_no, v_sms_tel_no,
                      lv_temp_txt, v_sms_status
                     );
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('ERROR INSERTING RECORD');
      END;
   END;

   PROCEDURE savebirthdaymessages (
      v_sms_sys_code     IN   NUMBER,
      v_sms_sys_module   IN   VARCHAR2,
      v_sms_clnt_code    IN   NUMBER,
      v_sms_tel_no       IN   VARCHAR2,
      v_sms_status       IN   VARCHAR2,
      v_clientname       IN   VARCHAR2
   )
   IS
      lv_temp_txt   VARCHAR2 (32767);
   BEGIN
      BEGIN
         SELECT msgt_msg
           INTO lv_temp_txt
           FROM tqc_msg_templates
          WHERE msgt_sys_code = 0
            AND msgt_sys_module = 'OC'
            AND msgt_type = 'SMS'
            AND NVL (msgt_default, 'N') = 'Y';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('No template is defined for renewal process......');
      END;

      lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENTNAME]', v_clientname);

      BEGIN
         INSERT INTO tqc_sms_messages
                     (sms_code, sms_sys_code,
                      sms_sys_module, sms_clnt_code, sms_tel_no,
                      sms_msg, sms_status, sms_agn_code
                     )
              VALUES (tqc_sms_code_seq.NEXTVAL, v_sms_sys_code,
                      v_sms_sys_module, v_sms_clnt_code, v_sms_tel_no,
                      lv_temp_txt, v_sms_status, 0
                     );
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('ERROR INSERTING RECORD');
      END;
   END;

   FUNCTION createwebserviceclient (
      v_first_name       IN   VARCHAR2,
      v_middle_name      IN   VARCHAR2,
      v_surname          IN   VARCHAR2,
      v_clnt_id_reg_no   IN   VARCHAR2
   )
      RETURN VARCHAR2
   IS
      v_clnt_sht_desc   VARCHAR2 (200);
      vclntcode         NUMBER;
   BEGIN
      BEGIN
         v_clnt_sht_desc :=
                 get_client_sht_desc (v_first_name, v_middle_name, v_surname);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error creating client Id...' || v_clnt_sht_desc);
      END;

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

      BEGIN
         INSERT INTO tqc_clients
                     (clnt_code, clnt_sht_desc,
                      clnt_other_names,
                      clnt_name,
                      clnt_id_reg_no, clnt_created_by, clnt_date_created,
                      clnt_direct_client, clnt_wef, clnt_status
                     )
              VALUES (vclntcode, v_clnt_sht_desc,
                      LTRIM (RTRIM (v_first_name || ' ' || v_middle_name)),
                      LTRIM (RTRIM (NVL (v_surname, 'n/a'))),
                      v_clnt_id_reg_no, 'CLIENT PORTAL', SYSDATE,
                      'Y', SYSDATE, 'A'
                     );
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error creating the client..');
      END;

      RETURN (v_clnt_sht_desc);
   END;

   FUNCTION checkclientexists (v_clnt_sht_desc IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_exists   VARCHAR2 (20);
      v_cnt      NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_clients
          WHERE clnt_sht_desc = v_clnt_sht_desc;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Cannot obtain if client exists');
      END;

      IF NVL (v_cnt, 0) > 0
      THEN
         v_exists := 'Y';
      ELSE
         v_exists := 'N';
      END IF;

      RETURN (v_exists);
   END;

   FUNCTION getgisclients (v_clnt_code IN NUMBER)
      RETURN gis_clients_ref
   IS
      v_retcur   gis_clients_ref;
   BEGIN
      --  RAISE_ERROR('v_clnt_code'||v_clnt_code);
      IF v_clnt_code IS NOT NULL
      THEN
         --  raise_error('test');
         OPEN v_retcur FOR
            SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,
                   clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,
                   clnt_postal_addrs, clnt_twn_code,
                   tqc_setups_cursor.town_name (clnt_twn_code) twn_name,
                   clnt_cou_code,
                   tqc_setups_cursor.country_name (clnt_cou_code) cou_name,
                   clnt_email_addrs, clnt_tel, clnt_tel2, clnt_status,
                   clnt_fax, clnt_remarks, clnt_spcl_terms,
                   clnt_declined_prop, clnt_increased_premium,
                   clnt_policy_cancelled, clnt_proposer, clnt_accnt_no,
                   clnt_domicile_countries, clnt_wef, clnt_wet,
                   clnt_withdrawal_reason, clnt_sec_code, clnt_surname,
                   DECODE (clnt_type,
                           'I', 'Individual',
                           'Corporate'
                          ) clnt_type,
                   clnt_title, clnt_business, clnt_zip_code, clnt_bbr_code,
                   clnt_bank_acc_no, clnt_clnt_code, clnt_non_direct,
                   clnt_created_by, clnt_sms_tel, clnt_agnt_status,
                   clnt_date_created, clnt_runoff, clnt_loaded_by,
                   clnt_direct_client, clnt_old_accnt_no, clnt_usr_code,
                   tqc_interfaces_pkg.username (clnt_usr_code) usr_name,
                   clnt_cntct_phone_1, clnt_cntct_email_1,
                   clnt_cntct_phone_2, clnt_cntct_email_2, clnt_sts_code,
                   tqc_setups_cursor.state_name (clnt_sts_code) sts_name,
                   clnt_passport_no, clnt_gender, clnt_cntct_name_1,
                   clnt_cntct_name_2,
                   tqc_setups_cursor.sector_name (clnt_sec_code) sector_name,
                   clnt_website, clnt_auditors, clnt_parent_company,
                   clnt_current_insurer, clnt_projected_business,
                   clnt_date_of_empl, clnt_driving_licence,
                   tqc_setups_cursor.parent_company_name
                                                   (clnt_parent_company)
                                                                       pname,
                   clnt_brn_code,
                   tqc_setups_cursor.branch_name (clnt_brn_code) branch_name,
                   clnt_credit_lim_allowed, clnt_credit_limit, agn_name,
                   clnt_drv_experience, clnt_old_sht_desc, usr_username,
                   clnt_bank_phone_no, clnt_bank_cell_no
              FROM tqc_clients, tqc_agency_clients, tqc_agencies, tqc_users
             WHERE clnt_code = agnc_clnt_code(+)
               AND agnc_agn_code = agn_code(+)
               AND usr_code(+) = clnt_usr_code
               AND clnt_code = v_clnt_code;
      ELSE
         OPEN v_retcur FOR
            SELECT NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                   NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'A', NULL, NULL,
                   NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                   NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                   NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                   NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                   NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                   NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
                   NULL, NULL, NULL, NULL, NULL
              FROM DUAL;
      END IF;

      RETURN (v_retcur);
   END;

   PROCEDURE getclienttype (v_clnt_code IN NUMBER, v_client_type OUT VARCHAR2)
   IS
   BEGIN
      SELECT clnt_type
        INTO v_client_type
        FROM tqc_clients
       WHERE clnt_code = v_clnt_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Failed to Obtain Client Type');
   END;

   FUNCTION getholdingcompany (v_clnt_code IN NUMBER)
      RETURN holdingcompany_ref
   IS
      v_retcur   holdingcompany_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT clnt_code, clnt_sht_desc, clnt_name, clnt_other_names,
                clnt_id_reg_no, clnt_dob, clnt_pin, clnt_physical_addrs,
                clnt_postal_addrs, clnt_email_addrs, clnt_status, clnt_type,
                clnt_title, clnt_direct_client, clnt_gender,
                clnt_passport_no, clnt_occupation, cltn_client_types
           FROM tqc_clients
          WHERE clnt_clnt_code = v_clnt_code;

      RETURN (v_retcur);
   END;
END tqc_clients_pkgbkp; 
/