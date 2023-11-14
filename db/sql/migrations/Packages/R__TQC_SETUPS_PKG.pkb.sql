/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_SETUPS_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_setups_pkg
AS
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

   PROCEDURE agency_holding_company_prc (
      v_add_edit          IN       VARCHAR2,
      v_ahc_code          IN       tqc_agency_holding_company.ahc_code%TYPE,
      v_ahc_name          IN       tqc_agency_holding_company.ahc_name%TYPE,
      v_postaladdress     IN       tqc_agency_holding_company.ahc_postal_address%TYPE,
      v_physicaladdress   IN       tqc_agency_holding_company.ahc_physical_address%TYPE,
      v_telnumber         IN       tqc_agency_holding_company.ahc_telephone_number%TYPE,
      v_mobnumber         IN       tqc_agency_holding_company.ahc_mobile_number%TYPE,
      v_contactperson     IN       tqc_agency_holding_company.ahc_contact_person%TYPE,
      v_error             OUT      VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_agency_holding_company
             WHERE ahc_name = v_ahc_name;

            IF v_count > 0
            THEN
               v_error := 'Record with ID Exists!';
               RETURN;
            END IF;

            INSERT INTO tqc_agency_holding_company (ahc_code,
                                                    ahc_name,
                                                    ahc_postal_address,
                                                    ahc_physical_address,
                                                    ahc_telephone_number,
                                                    ahc_mobile_number,
                                                    ahc_contact_person)
                 VALUES (tqc_ahc_code_seq.NEXTVAL,
                         v_ahc_name,
                         v_postaladdress,
                         v_physicaladdress,
                         v_telnumber,
                         v_mobnumber,
                         v_contactperson);
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
            UPDATE tqc_agency_holding_company
               SET ahc_name = v_ahc_name,
                   ahc_postal_address = v_postaladdress,
                   ahc_physical_address = v_physicaladdress,
                   ahc_telephone_number = v_telnumber,
                   ahc_mobile_number = v_mobnumber,
                   ahc_contact_person = v_contactperson
             WHERE ahc_code = v_ahc_code;
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
            DELETE FROM tqc_agency_holding_company
                  WHERE ahc_code = v_ahc_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while deleting record ' || SQLERRM (SQLCODE);
         END;
      END IF;
   END;

   PROCEDURE banks_prc (
      v_add_edit                    IN       VARCHAR2,
      v_bnk_code                    IN       tqc_banks.bnk_code%TYPE,
      v_bnk_bank_name               IN       tqc_banks.bnk_bank_name%TYPE,
      v_bnk_remarks                 IN       tqc_banks.bnk_remarks%TYPE,
      v_bnk_sht_desc                IN       tqc_banks.bnk_sht_desc%TYPE,
      v_bnk_ddr_code                IN       tqc_banks.bnk_ddr_code%TYPE,
      v_dd_format_desc              IN       tqc_banks.dd_format_desc%TYPE,
      v_bnk_forwarding_bnk_code     IN       tqc_banks.bnk_forwarding_bnk_code%TYPE,
      v_bnk_kba_code                IN       tqc_banks.bnk_kba_code%TYPE,
      v_bnk_eft_supported           IN       tqc_banks.bnk_eft_supported%TYPE,
      v_bnk_class_type              IN       tqc_banks.bnk_class_type%TYPE,
      v_bnk_accnt_digit_no          IN       tqc_banks.bnk_accnt_digit_no%TYPE,
      v_bnk_negotiated_bank         IN       tqc_banks.bnk_negotiated_bank%TYPE,
      v_bnk_administration_charge   IN       tqc_banks.bnk_administration_charge%TYPE,
      v_bnk_logo                    IN       tqc_banks.bnk_logo%TYPE,
      v_bnk_wef                     IN       tqc_banks.bnk_wef%TYPE,
      v_bnk_wet                     IN       tqc_banks.bnk_wet%TYPE,
      v_error                       OUT      VARCHAR2,
      v_bnk_status                  IN       tqc_banks.bnk_status%TYPE,
      v_bnk_acc_max_no              IN       tqc_banks.bnk_acc_max_no%TYPE,
      v_bnk_acc_min_no              IN       tqc_banks.bnk_acc_min_no%TYPE,
      v_bnk_cou_code                IN       tqc_banks.bnk_cou_code%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_banks
             WHERE bnk_bank_name = v_bnk_bank_name;

            IF v_count > 0
            THEN
               v_error := 'Record with ID Exists!';
               RETURN;
            END IF;

            INSERT INTO tqc_banks (bnk_code,
                                   bnk_bank_name,
                                   bnk_remarks,
                                   bnk_sht_desc,
                                   bnk_ddr_code,
                                   dd_format_desc,
                                   bnk_forwarding_bnk_code,
                                   bnk_kba_code,
                                   bnk_eft_supported,
                                   bnk_class_type,
                                   bnk_accnt_digit_no,
                                   bnk_negotiated_bank,
                                   bnk_administration_charge,
                                   bnk_logo,
                                   bnk_wef,
                                   bnk_wet,
                                   bnk_status,
                                   bnk_acc_max_no,
                                   bnk_acc_min_no,
                                   bnk_cou_code)
                 VALUES (tqc_bnk_code_seq.NEXTVAL,
                         v_bnk_bank_name,
                         v_bnk_remarks,
                         v_bnk_sht_desc,
                         v_bnk_ddr_code,
                         v_dd_format_desc,
                         v_bnk_forwarding_bnk_code,
                         v_bnk_kba_code,
                         v_bnk_eft_supported,
                         v_bnk_class_type,
                         v_bnk_accnt_digit_no,
                         v_bnk_negotiated_bank,
                         v_bnk_administration_charge,
                         v_bnk_logo,
                         v_bnk_wef,
                         v_bnk_wet,
                         v_bnk_status,
                         v_bnk_acc_max_no,
                         v_bnk_acc_min_no,
                         v_bnk_cou_code
                         );
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while creating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            --  RAISE_ERROR('v_bnk_class_type'||v_bnk_class_type);
            UPDATE tqc_banks
               SET bnk_bank_name = v_bnk_bank_name,
                   bnk_remarks = v_bnk_remarks,
                   bnk_sht_desc = v_bnk_sht_desc,
                   bnk_ddr_code = v_bnk_ddr_code,
                   dd_format_desc = v_dd_format_desc,
                   bnk_forwarding_bnk_code = v_bnk_forwarding_bnk_code,
                   bnk_kba_code = v_bnk_kba_code,
                   bnk_eft_supported = v_bnk_eft_supported,
                   bnk_class_type = v_bnk_class_type,
                   bnk_accnt_digit_no = v_bnk_accnt_digit_no,
                   bnk_negotiated_bank = v_bnk_negotiated_bank,
                   bnk_administration_charge = v_bnk_administration_charge,
                   bnk_logo = v_bnk_logo,
                   bnk_wef = v_bnk_wef,
                   bnk_wet = v_bnk_wet,
                   bnk_status=v_bnk_status,
                   bnk_acc_max_no = v_bnk_acc_max_no,
                   bnk_acc_min_no = v_bnk_acc_min_no,
                   bnk_cou_code = v_bnk_cou_code
             WHERE bnk_code = v_bnk_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while updating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_banks
                  WHERE bnk_code = v_bnk_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while deleting record ' || SQLERRM (SQLCODE);
         END;
      END IF;
   --  v_error := 'Bank Processing Successful';
   END;

   PROCEDURE bank_branches_prc (
      v_add_edit             IN     VARCHAR2,
      v_bbr_code             IN     tqc_bank_branches.bbr_code%TYPE,
      v_bbr_bnk_code         IN     tqc_bank_branches.bbr_bnk_code%TYPE,
      v_bbr_branch_name      IN     tqc_bank_branches.bbr_branch_name%TYPE,
      v_bbr_remarks          IN     tqc_bank_branches.bbr_remarks%TYPE,
      v_bbr_sht_desc         IN     tqc_bank_branches.bbr_sht_desc%TYPE,
      v_bbr_ref_code         IN     tqc_bank_branches.bbr_ref_code%TYPE,
      v_bbr_eft_supported    IN     tqc_bank_branches.bbr_eft_supported%TYPE,
      v_bbr_dd_supported     IN     tqc_bank_branches.bbr_dd_supported%TYPE,
      v_bbr_date_created     IN     tqc_bank_branches.bbr_date_created%TYPE,
      v_bbr_created_by       IN     tqc_bank_branches.bbr_created_by%TYPE,
      v_bbr_physical_addrs   IN     tqc_bank_branches.bbr_physical_addrs%TYPE,
      v_bbr_postal_addrs     IN     tqc_bank_branches.bbr_postal_addrs%TYPE,
      v_bbr_kba_code         IN     tqc_bank_branches.bbr_kba_code%TYPE,
      v_error                OUT    VARCHAR2,
      v_bnkt_code            IN     TQC_BANK_TERRITORIES.bnkt_code%TYPE  DEFAULT NULL,
      v_bbr_email            IN     tqc_bank_branches.bbr_email%TYPE DEFAULT NULL,
      v_bbr_person_name      IN     tqc_bank_branches.bbr_person_name%TYPE  DEFAULT NULL,
      v_bbr_person_email     IN     tqc_bank_branches.bbr_person_email%TYPE  DEFAULT NULL,
      v_bbr_person_cou_code  IN     tqc_bank_branches.bbr_person_cou_code%TYPE  DEFAULT NULL,
      v_bbr_person_phone     IN     tqc_bank_branches.bbr_person_phone%TYPE DEFAULT NULL)
   IS
      v_count   NUMBER;
   BEGIN
      --RAISE_ERROR('***'||v_BBR_CODE);
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM tqc_bank_branches, tqc_banks
             WHERE     bbr_bnk_code(+) = bnk_code
                   AND bbr_bnk_code = v_bbr_bnk_code
                   AND bbr_branch_name = v_bbr_branch_name;

            IF v_count != 0
            THEN
               v_error := 'Bank Branch of the Same Name Exists';
               RETURN;
            END IF;

            INSERT INTO tqc_bank_branches (bbr_code,
                                           bbr_bnk_code,
                                           bbr_branch_name,
                                           bbr_remarks,
                                           bbr_sht_desc,
                                           bbr_ref_code,
                                           bbr_eft_supported,
                                           bbr_dd_supported,
                                           bbr_date_created,
                                           bbr_created_by,
                                           bbr_physical_addrs,
                                           bbr_postal_addrs,
                                           bbr_kba_code,
                                           bbr_bnkt_code,
                                           bbr_email,
                                           bbr_person_name,
                                           bbr_person_email,
                                           bbr_person_cou_code,
                                           bbr_person_phone)
                 VALUES (tqc_bbr_code_seq.NEXTVAL,
                         v_bbr_bnk_code,
                         v_bbr_branch_name,
                         v_bbr_remarks,
                         v_bbr_sht_desc,
                         v_bbr_ref_code,
                         v_bbr_eft_supported,
                         v_bbr_dd_supported,
                         v_bbr_date_created,
                         v_bbr_created_by,
                         v_bbr_physical_addrs,
                         v_bbr_postal_addrs,
                         v_bbr_kba_code,
                         v_bnkt_code,
                         v_bbr_email,
                         v_bbr_person_name,
                         v_bbr_person_email,
                         v_bbr_person_cou_code,
                         v_bbr_person_phone);
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while creating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_bank_branches
               SET bbr_bnk_code = v_bbr_bnk_code,
                   bbr_branch_name = v_bbr_branch_name,
                   bbr_remarks = v_bbr_remarks,
                   bbr_sht_desc = v_bbr_sht_desc,
                   bbr_ref_code = v_bbr_ref_code,
                   bbr_eft_supported = v_bbr_eft_supported,
                   bbr_dd_supported = v_bbr_dd_supported,
                   -- BBR_DATE_CREATED    = v_BBR_DATE_CREATED,
                   -- BBR_CREATED_BY      = v_BBR_CREATED_BY,
                   bbr_physical_addrs = v_bbr_physical_addrs,
                   bbr_postal_addrs = v_bbr_postal_addrs,
                   bbr_kba_code = v_bbr_kba_code,
                   bbr_bnkt_code = v_bnkt_code,
                   bbr_email=v_bbr_email,
                   bbr_person_name=v_bbr_person_name,
                   bbr_person_email=v_bbr_person_email,
                   bbr_person_cou_code=v_bbr_person_cou_code,
                   bbr_person_phone=v_bbr_person_phone
             WHERE bbr_code = v_bbr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while updating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_bank_branches
                  WHERE bbr_code = v_bbr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while deleting record ' || SQLERRM (SQLCODE);
         END;
      END IF;
   ---  v_error := 'Bank Branch Processing Successful';
   END;

   PROCEDURE agency_classes_prc (
      v_add_edit          IN       VARCHAR2,
      v_agnc_class_code   IN       tqc_agencies_classes.agnc_class_code%TYPE,
      v_agnc_class_desc   IN       tqc_agencies_classes.agnc_class_desc%TYPE,
      v_error             OUT      VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_agencies_classes
             WHERE agnc_class_desc = v_agnc_class_desc;

            IF v_count > 0
            THEN
               v_error := 'Record with ID Exists!';
               RETURN;
            END IF;

            INSERT
              INTO tqc_agencies_classes (agnc_class_code, agnc_class_desc)
            VALUES (tqc_agnc_class_code_seq.NEXTVAL, v_agnc_class_desc);
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while creating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_agencies_classes
               SET agnc_class_desc = v_agnc_class_desc
             WHERE agnc_class_code = v_agnc_class_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while updating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_agencies_classes
                  WHERE agnc_class_code = v_agnc_class_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while deleting record ' || SQLERRM (SQLCODE);
         END;
      END IF;
   END;

   PROCEDURE currencies_prc (
      v_add_edit           IN     VARCHAR2,
      v_cur_code           IN     tqc_currencies.cur_code%TYPE,
      v_cur_symbol         IN     tqc_currencies.cur_symbol%TYPE,
      v_cur_desc           IN     tqc_currencies.cur_desc%TYPE,
      v_cur_rnd            IN     tqc_currencies.cur_rnd%TYPE,
      v_error                 OUT VARCHAR2,
      v_cur_num_word       IN     tqc_currencies.cur_num_word%TYPE,
      v_cur_decimal_word   IN     tqc_currencies.cur_decimal_word%TYPE)
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_currencies
             WHERE cur_symbol = v_cur_symbol;

            IF v_count > 0
            THEN
               v_error := 'Record with ID Exists!';
               RETURN;
            END IF;

            INSERT INTO tqc_currencies (cur_code,
                                        cur_symbol,
                                        cur_desc,
                                        cur_rnd,
                                        cur_num_word,
                                        cur_decimal_word)
                 VALUES (tqc_cur_code_seq.NEXTVAL,
                         v_cur_symbol,
                         v_cur_desc,
                         v_cur_rnd,
                         v_cur_num_word,
                         v_cur_decimal_word);
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while creating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_currencies
               SET cur_symbol = v_cur_symbol,
                   cur_desc = v_cur_desc,
                   cur_rnd = v_cur_rnd,
                   cur_num_word = v_cur_num_word,
                   cur_decimal_word = v_cur_decimal_word
             WHERE cur_code = v_cur_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while updating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_currencies
                  WHERE cur_code = v_cur_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while deleting record ' || SQLERRM (SQLCODE);
         END;
      END IF;
   END;

   PROCEDURE currencies_denominations_prc (
      v_add_edit       IN     VARCHAR2,
      v_cud_code       IN     tqc_currency_denominations.cud_code%TYPE,
      v_cud_cur_code   IN     tqc_currency_denominations.cud_cur_code%TYPE,
      v_cud_value      IN     tqc_currency_denominations.cud_value%TYPE,
      v_cud_name       IN     tqc_currency_denominations.cud_name%TYPE,
      v_cud_wef        IN     tqc_currency_denominations.cud_wef%TYPE,
      v_error             OUT VARCHAR2)
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_currency_denominations
             WHERE cud_name = v_cud_name;

            IF v_count > 0
            THEN
               v_error := 'Record with ID Exists!';
               RETURN;
            END IF;

            INSERT INTO tqc_currency_denominations (cud_code,
                                                    cud_cur_code,
                                                    cud_value,
                                                    cud_name,
                                                    cud_wef)
                 VALUES (tqc_cud_code_seq.NEXTVAL,
                         v_cud_cur_code,
                         v_cud_value,
                         v_cud_name,
                         v_cud_wef);
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while creating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_currency_denominations
               SET cud_cur_code = v_cud_cur_code,
                   cud_value = v_cud_value,
                   cud_name = v_cud_name,
                   cud_wef = v_cud_wef
             WHERE cud_code = v_cud_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while updating record ' || SQLERRM (SQLCODE);
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_currency_denominations
                  WHERE cud_code = v_cud_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_error :=
                  'Error occured while deleting record ' || SQLERRM (SQLCODE);
         END;
      END IF;
   END;

   PROCEDURE currency_rates_prc (
      v_add_edit            IN VARCHAR2,
      v_crt_code            IN tqc_currency_rates.crt_code%TYPE,
      v_crt_cur_code        IN tqc_currency_rates.crt_cur_code%TYPE,
      v_crt_rate            IN tqc_currency_rates.crt_rate%TYPE,
      v_crt_date            IN TIMESTAMP,
      v_crt_base_cur_code   IN tqc_currency_rates.crt_base_cur_code%TYPE,
      v_crt_wef             IN TIMESTAMP,
      v_crt_wet             IN TIMESTAMP,
      --v_user_code          IN NUMBER DEFAULT NULL
      v_usr_username        IN VARCHAR DEFAULT NULL)
   IS
      CURSOR currref (
         v_crt_wef_dt DATE)
      IS
         SELECT crt_code,
                crt_cur_code,
                crt_rate,
                crt_date,
                crt_base_cur_code,
                crt_wef,
                crt_wet
           FROM tqc_currency_rates
          WHERE     crt_cur_code = v_crt_cur_code
                AND crt_wet > v_crt_wef_dt
                AND crt_code != v_crt_code;

      v_count         NUMBER;
      v_crt_date_dt   DATE;
      v_crt_wef_dt    DATE;
      v_crt_wet_dt    DATE;
   BEGIN
     v_crt_date_dt := v_crt_date;
     v_crt_wef_dt := v_crt_wef;
     v_crt_wet_dt := v_crt_wet;
     
     --raise_error ('v_crt_code: '||v_crt_code||' v_crt_cur_code: '||v_crt_cur_code||'v_crt_date_dt ' || v_crt_date_dt||' v_crt_wet_dt: '||v_crt_wet_dt||' v_crt_wet: '||v_crt_wet);

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



            IF TRUNC (v_crt_wef_dt) < TRUNC (SYSDATE)
            THEN
               raise_error ('Wef cannot be less than today...');
            END IF;


            FOR i IN currref (v_crt_wef_dt)
            LOOP
               UPDATE tqc_currency_rates
                  SET crt_wet = SYSDATE
                WHERE crt_code = i.crt_code;
            END LOOP;

            --raise_error('Currency Rate Processing Successful '||v_count);
            INSERT INTO tqc_currency_rates (crt_code,
                                            crt_cur_code,
                                            crt_rate,
                                            crt_date,
                                            crt_base_cur_code,
                                            crt_wef,
                                            crt_wet,
                                            crt_created_date,
                                            crt_created_by)
                 VALUES (tqc_crt_code_seq.NEXTVAL,
                         v_crt_cur_code,
                         v_crt_rate,
                         v_crt_date_dt,
                         v_crt_base_cur_code,
                         v_crt_wef_dt,
                         v_crt_wet_dt,
                         SYSDATE,
                         v_usr_username                          --v_user_code
                                       );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while creating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            FOR i IN currref (v_crt_wef_dt)
            LOOP
               IF v_crt_wef BETWEEN i.crt_wef AND i.crt_wet
               THEN
                  raise_error (
                     'Your Currency Period Overlaps with another period.Please check');
               END IF;

               IF v_crt_wet BETWEEN i.crt_wef AND i.crt_wet
               THEN
                  raise_error (
                     'Your Currency Period Overlaps with another period.Please check');
               END IF;
            END LOOP;

            UPDATE tqc_currency_rates
               SET crt_cur_code = v_crt_cur_code,
                   crt_rate = v_crt_rate,
                   crt_date = v_crt_date_dt,
                   crt_base_cur_code = v_crt_base_cur_code,
                   crt_wef = v_crt_wef_dt,
                   crt_wet = v_crt_wet_dt,
                   crt_updated_date = SYSDATE,
                   crt_updated_by = v_usr_username              --v_user_code,
             WHERE crt_code = v_crt_code
             AND crt_cur_code = v_crt_cur_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while updating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_currency_rates
                  WHERE crt_code = v_crt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while deleting record ' || SQLERRM (SQLCODE));
         END;
      END IF;
   --raise_error('Currency Rate Processing Successful');
   END;

   PROCEDURE acc_types_prc (
      v_act_code                   NUMBER,
      v_clnt_mapping               VARCHAR2,
      v_wht_rate                   NUMBER,
      v_ovr_rate                   NUMBER,
      v_comm_rate                  NUMBER,
      v_ac_format                  VARCHAR2,
      v_vat_rate                   NUMBER,
      v_sht_desc                   VARCHAR2,
      v_act_no_gen_code            tqc_account_types.act_no_gen_code%TYPE,
      v_act_commision_levy_rate    NUMBER DEFAULT NULL)
   IS
   BEGIN
      --      raise_error('v_act_commision_levy_rate '||v_act_commision_levy_rate);
      UPDATE tqc_account_types
         SET act_wthtx_rate = NVL (v_wht_rate, act_wthtx_rate),
             act_account_type = NVL (v_clnt_mapping, act_account_type),
             act_overide_rate = NVL (v_ovr_rate, act_overide_rate),
             act_comm_reserve_rate = NVL (v_comm_rate, act_comm_reserve_rate),
             act_format = NVL (v_ac_format, act_id_serial_format),
             act_vat_rate = NVL (v_vat_rate, act_vat_rate),
             act_id_serial_format = v_sht_desc,
             act_no_gen_code = v_act_no_gen_code,
             act_commision_levy_rate = v_act_commision_levy_rate
       WHERE act_code = v_act_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (
            'Error occured while Updating record ' || SQLERRM (SQLCODE));
   END;

   PROCEDURE sectors_prc (v_add_edit       IN VARCHAR2,
                          v_sec_code       IN tqc_sectors.sec_code%TYPE,
                          v_sec_sht_desc   IN tqc_sectors.sec_sht_desc%TYPE,
                          v_sec_name       IN tqc_sectors.sec_name%TYPE)
   IS
      v_count   NUMBER;
   BEGIN
      --      raise_error(
      --      'v_add_edit: '||v_add_edit ||
      --      'v_sec_code: '||v_sec_code ||
      --      'v_sec_sht_desc: '||v_sec_sht_desc||
      --      'v_sec_name: '||v_sec_name
      --      );
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_sectors
             WHERE sec_code = v_sec_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_sectors
                        (sec_code, sec_sht_desc, sec_name
                        )
                 VALUES (tqc_sec_code_seq.NEXTVAL, v_sec_sht_desc, v_sec_name
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while creating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_sectors
               SET sec_sht_desc = v_sec_sht_desc,
                   sec_name = v_sec_name
             WHERE sec_code = v_sec_code;
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
            DELETE FROM tqc_sectors
                  WHERE sec_code = v_sec_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE service_provider_type_act_prc (
      v_add_edit                  IN VARCHAR2,
      v_spta_code                 IN tqc_serv_prv_type_actvts.spta_code%TYPE,
      v_spta_spt_code             IN tqc_serv_prv_type_actvts.spta_spt_code%TYPE,
      v_spta_sht_desc             IN tqc_serv_prv_type_actvts.spta_sht_desc%TYPE,
      v_spta_desc                 IN tqc_serv_prv_type_actvts.spta_desc%TYPE,
      v_spta_sms_msgt_code        IN tqc_serv_prv_type_actvts.spta_sms_msgt_code%TYPE DEFAULT NULL,
      v_spta_email_msgt_code      IN tqc_serv_prv_type_actvts.spta_email_msgt_code%TYPE DEFAULT NULL,
      v_spta_send_msg_default     IN tqc_serv_prv_type_actvts.spta_send_msg_default%TYPE DEFAULT NULL,
      v_spta_send_email_default   IN tqc_serv_prv_type_actvts.spta_send_email_default%TYPE DEFAULT NULL,
      v_reportDays                IN tqc_serv_prv_type_actvts.spta_report_days%TYPE DEFAULT NULL)
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_serv_prv_type_actvts (spta_code,
                                               spta_spt_code,
                                               spta_sht_desc,
                                               spta_desc,
                                               spta_sms_msgt_code,
                                               spta_email_msgt_code,
                                               spta_send_msg_default,
                                               spta_send_email_default,
                                               spta_report_days)
              VALUES (tqc_spta_code_seq.NEXTVAL,
                      v_spta_spt_code,
                      v_spta_sht_desc,
                      v_spta_desc,
                      v_spta_sms_msgt_code,
                      v_spta_email_msgt_code,
                      v_spta_send_msg_default,
                      v_spta_send_email_default,
                      v_reportDays);
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_serv_prv_type_actvts
            SET spta_spt_code = v_spta_spt_code,
                spta_sht_desc = v_spta_sht_desc,
                spta_desc = v_spta_desc,
                spta_sms_msgt_code = v_spta_sms_msgt_code,
                spta_email_msgt_code = v_spta_email_msgt_code,
                spta_send_msg_default = v_spta_send_msg_default,
                spta_send_email_default = v_spta_send_email_default,
                spta_report_days = v_reportDays
          WHERE spta_code = v_spta_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_serv_prv_type_actvts
               WHERE spta_code = v_spta_code;
      END IF;
   END;

   PROCEDURE service_provider_types_prc (
      v_add_edit        IN   VARCHAR2,
      v_spt_code        IN   tqc_service_provider_types.spt_code%TYPE,
      v_spt_sht_desc    IN   tqc_service_provider_types.spt_sht_desc%TYPE,
      v_spt_name        IN   tqc_service_provider_types.spt_name%TYPE,
      v_spt_status      IN   tqc_service_provider_types.spt_status%TYPE,
      v_spt_whtx_rate   IN   tqc_service_provider_types.spt_whtx_rate%TYPE,
      v_spt_vat_rate    IN   tqc_service_provider_types.spt_vat_rate%TYPE,
      v_spt_suffixes    IN   tqc_service_provider_types.spt_suffixes%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_service_provider_types
             WHERE spt_code = v_spt_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_service_provider_types
                        (spt_code, spt_sht_desc,
                         spt_name, spt_status, spt_whtx_rate,
                         spt_vat_rate, spt_suffixes
                        )
                 VALUES (tqc_spt_code_seq.NEXTVAL, v_spt_sht_desc,
                         v_spt_name, v_spt_status, v_spt_whtx_rate,
                         v_spt_vat_rate, v_spt_suffixes
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
            UPDATE tqc_service_provider_types
               SET spt_sht_desc = v_spt_sht_desc,
                   spt_name = v_spt_name,
                   spt_status = v_spt_status,
                   spt_whtx_rate = v_spt_whtx_rate,
                   spt_vat_rate = v_spt_vat_rate,
                   spt_suffixes = v_spt_suffixes
             WHERE spt_code = v_spt_code;
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
            DELETE FROM tqc_service_provider_types
                  WHERE spt_code = v_spt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE countries_prc (
      v_add_edit             IN VARCHAR2,
      v_cou_code             IN tqc_countries.cou_code%TYPE DEFAULT NULL,
      v_cou_sht_desc         IN tqc_countries.cou_sht_desc%TYPE DEFAULT NULL,
      v_cou_name             IN tqc_countries.cou_name%TYPE DEFAULT NULL,
      v_cou_base_curr        IN tqc_countries.cou_base_curr%TYPE DEFAULT NULL,
      v_cou_nationality      IN tqc_countries.cou_nationality%TYPE DEFAULT NULL,
      v_cou_zip_code         IN tqc_countries.cou_zip_code%TYPE DEFAULT NULL,
      v_cou_admin_reg_type   IN tqc_countries.cou_admin_reg_type%TYPE DEFAULT NULL,
      v_cou_schegen          IN tqc_countries.cou_schegen%TYPE DEFAULT NULL,
      v_cou_emb_code         IN tqc_countries.cou_emb_code%TYPE DEFAULT NULL,
      v_cou_curr_serial      IN tqc_countries.cou_curr_serial%TYPE DEFAULT NULL,
      v_cou_mobile_prefix    IN tqc_countries.cou_mobile_prefix%TYPE DEFAULT NULL,
      v_cou_client_number    IN tqc_countries.cou_client_number%TYPE DEFAULT NULL)
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_countries
             WHERE cou_code = v_cou_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_countries (cou_code,
                                       cou_sht_desc,
                                       cou_name,
                                       cou_base_curr,
                                       cou_nationality,
                                       cou_zip_code,
                                       cou_admin_reg_type,
                                       cou_schegen,
                                       cou_emb_code,
                                       cou_curr_serial,
                                       cou_mobile_prefix,
                                       cou_client_number)
                 VALUES (tqc_cou_code_seq.NEXTVAL,
                         v_cou_sht_desc,
                         v_cou_name,
                         v_cou_base_curr,
                         v_cou_nationality,
                         v_cou_zip_code,
                         v_cou_admin_reg_type,
                         v_cou_schegen,
                         v_cou_emb_code,
                         v_cou_curr_serial,
                         v_cou_mobile_prefix,
                         v_cou_client_number);
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
            UPDATE tqc_countries
               SET cou_sht_desc = v_cou_sht_desc,
                   cou_name = v_cou_name,
                   cou_base_curr = v_cou_base_curr,
                   cou_nationality = v_cou_nationality,
                   cou_zip_code = v_cou_zip_code,
                   cou_admin_reg_type = v_cou_admin_reg_type,
                   cou_schegen = v_cou_schegen,
                   cou_emb_code = v_cou_emb_code,
                   cou_curr_serial = v_cou_curr_serial,
                   cou_mobile_prefix = v_cou_mobile_prefix,
                   cou_client_number = v_cou_client_number
             WHERE cou_code = v_cou_code;
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
            DELETE FROM tqc_countries
                  WHERE cou_code = v_cou_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE states_prc (
      v_add_edit       IN   VARCHAR2,
      v_sts_code       IN   tqc_states.sts_code%TYPE,
      v_sts_cou_code   IN   tqc_states.sts_cou_code%TYPE,
      v_sts_sht_desc   IN   tqc_states.sts_sht_desc%TYPE,
      v_sts_name       IN   tqc_states.sts_name%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_states
             WHERE sts_code = v_sts_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_states (sts_code,
                                    sts_cou_code,
                                    sts_sht_desc,
                                    sts_name)
                 VALUES (tqc_sts_code_seq.NEXTVAL,
                         v_sts_cou_code,
                         v_sts_sht_desc,
                         v_sts_name);
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
            UPDATE tqc_states
               SET sts_cou_code = v_sts_cou_code,
                   sts_sht_desc = v_sts_sht_desc,
                   sts_name = v_sts_name
             WHERE sts_code = v_sts_code;
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
            DELETE FROM tqc_states
                  WHERE sts_code = v_sts_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE towns_prc (
      v_add_edit       IN   VARCHAR2,
      v_twn_code       IN   tqc_towns.twn_code%TYPE,
      v_twn_cou_code   IN   tqc_towns.twn_cou_code%TYPE,
      v_twn_sht_desc   IN   tqc_towns.twn_sht_desc%TYPE,
      v_twn_name       IN   tqc_towns.twn_name%TYPE,
      v_twn_sts_code   IN   tqc_towns.twn_sts_code%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_towns
             WHERE twn_code = v_twn_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_towns (twn_code,
                                   twn_cou_code,
                                   twn_sht_desc,
                                   twn_name,
                                   twn_sts_code)
                 VALUES (tqc_twn_code_seq.NEXTVAL,
                         v_twn_cou_code,
                         v_twn_sht_desc,
                         v_twn_name,
                         v_twn_sts_code);
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
            UPDATE tqc_towns
               SET twn_cou_code = v_twn_cou_code,
                   twn_sht_desc = v_twn_sht_desc,
                   twn_name = v_twn_name,
                   twn_sts_code = v_twn_sts_code
             WHERE twn_code = v_twn_code;
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
            DELETE FROM tqc_towns
                  WHERE twn_code = v_twn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE message_templates_prc (
      v_add_edit          IN   VARCHAR2,
      v_msgt_code         IN   tqc_msg_templates.msgt_code%TYPE,
      v_msgt_sht_desc     IN   tqc_msg_templates.msgt_sht_desc%TYPE,
      v_msgt_msg          IN   tqc_msg_templates.msgt_msg%TYPE,
      v_msgt_sys_code     IN   tqc_msg_templates.msgt_sys_code%TYPE,
      v_msgt_sys_module   IN   tqc_msg_templates.msgt_sys_module%TYPE,
      v_msgt_type         IN   tqc_msg_templates.msgt_type%TYPE
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_msg_templates (msgt_code,
                                        msgt_sht_desc,
                                        msgt_msg,
                                        msgt_sys_code,
                                        msgt_sys_module,
                                        msgt_type)
              VALUES (tqc_msgt_code_seq.NEXTVAL,
                      v_msgt_sht_desc,
                      v_msgt_msg,
                      v_msgt_sys_code,
                      v_msgt_sys_module,
                      v_msgt_type);
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_msg_templates
            SET msgt_sht_desc = v_msgt_sht_desc,
                msgt_msg = v_msgt_msg,
                msgt_sys_code = v_msgt_sys_code,
                msgt_sys_module = v_msgt_sys_module,
                msgt_type = v_msgt_type
          WHERE msgt_code = v_msgt_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_msg_templates
               WHERE msgt_code = v_msgt_code;
      END IF;
   END;

   PROCEDURE messagetemplates_prc (
      v_addedit                     VARCHAR2,
      v_messagetemplates_tab   IN   tqc_msg_templates_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_messagetemplates_tab.COUNT = 0
      THEN
         raise_error
                    (   'Error occured. No Message Template Data Provided : '
                     || SQLERRM (SQLCODE)
                    );
      END IF;

      FOR i IN 1 .. v_messagetemplates_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_msg_templates
             WHERE msgt_sht_desc = v_messagetemplates_tab (i).msgt_sht_desc
                   AND msgt_type = v_messagetemplates_tab (i).msgt_type;

            IF v_count > 0
            THEN
               raise_error
                        ('Record with the same Message Code and Type Exists!');
            END IF;

            INSERT INTO tqc_msg_templates
                        (msgt_code,
                         msgt_sht_desc,
                         msgt_msg,
                         msgt_sys_code,
                         msgt_sys_module,
                         msgt_type,
                         MSGT_CREATED_BY,
                         MSGT_UPDATED_BY
                        )
                 VALUES (tqc_msgt_code_seq.NEXTVAL,
                         v_messagetemplates_tab (i).msgt_sht_desc,
                         v_messagetemplates_tab (i).msgt_msg,
                         v_messagetemplates_tab (i).msgt_sys_code,
                         v_messagetemplates_tab (i).msgt_sys_module,
                         v_messagetemplates_tab (i).msgt_type,
                         v_messagetemplates_tab (i).MSGT_CREATED_BY,
                         v_messagetemplates_tab (i).MSGT_UPDATED_BY
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_msg_templates
               SET msgt_sht_desc = v_messagetemplates_tab (i).msgt_sht_desc,
                   msgt_msg = v_messagetemplates_tab (i).msgt_msg,
                   msgt_sys_code = v_messagetemplates_tab (i).msgt_sys_code,
                   msgt_sys_module =
                                    v_messagetemplates_tab (i).msgt_sys_module,
                   msgt_type = v_messagetemplates_tab (i).msgt_type,
                   MSGT_CREATED_BY= v_messagetemplates_tab (i).MSGT_CREATED_BY,
                   MSGT_UPDATED_BY= v_messagetemplates_tab (i).MSGT_UPDATED_BY
             WHERE msgt_code = v_messagetemplates_tab (i).msgt_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_msg_templates
             WHERE msgt_code = v_messagetemplates_tab (i).msgt_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Message Templates!');
   END messagetemplates_prc;

   PROCEDURE locations_prc (
      v_add_edit       IN   VARCHAR2,
      v_loc_code       IN   tqc_locations.loc_code%TYPE,
      v_loc_twn_code   IN   tqc_locations.loc_twn_code%TYPE,
      v_loc_sht_desc   IN   tqc_locations.loc_sht_desc%TYPE,
      v_loc_name       IN   tqc_locations.loc_name%TYPE,
      v_loc_landmark   IN   tqc_locations.loc_landmark%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_locations
             WHERE loc_code = v_loc_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_locations (loc_code,
                                       loc_twn_code,
                                       loc_sht_desc,
                                       loc_name,
                                       loc_landmark)
                 VALUES (tqc_locations_seq.NEXTVAL,
                         v_loc_twn_code,
                         v_loc_sht_desc,
                         v_loc_name,
                         v_loc_landmark);
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
            UPDATE tqc_locations
               SET loc_twn_code = v_loc_twn_code,
                   loc_sht_desc = v_loc_sht_desc,
                   loc_name = v_loc_name,
                   loc_landmark = v_loc_landmark
             WHERE loc_code = v_loc_code;
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
            DELETE FROM tqc_locations
                  WHERE loc_code = v_loc_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE parameters_prc (
      v_add_edit       IN   VARCHAR2,
      v_param_code     IN   tqc_parameters.param_code%TYPE,
      v_param_name     IN   tqc_parameters.param_name%TYPE,
      v_param_value    IN   tqc_parameters.param_value%TYPE,
      v_param_status   IN   tqc_parameters.param_status%TYPE,
      v_param_desc     IN   tqc_parameters.param_desc%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_parameters
             WHERE param_code = v_param_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_parameters (param_code,
                                        param_name,
                                        param_value,
                                        param_status,
                                        param_desc)
                 VALUES (tqc_param_code_seq.NEXTVAL,
                         v_param_name,
                         v_param_value,
                         v_param_status,
                         v_param_desc);
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
            UPDATE tqc_parameters
               SET param_name = v_param_name,
                   param_value = v_param_value,
                   param_status = v_param_status,
                   param_desc = v_param_desc
             WHERE param_code = v_param_code;
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
            DELETE FROM tqc_parameters
                  WHERE param_code = v_param_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;
    
    PROCEDURE service_providers_prc (
      v_add_edit                IN   VARCHAR2,
      v_spr_code                IN   tqc_service_providers.spr_code%TYPE
            DEFAULT NULL,
      v_spr_sht_desc            IN   tqc_service_providers.spr_sht_desc%TYPE
            DEFAULT NULL,
      v_spr_name                IN   tqc_service_providers.spr_name%TYPE
            DEFAULT NULL,
      v_spr_physical_address    IN   tqc_service_providers.spr_physical_address%TYPE
            DEFAULT NULL,
      v_spr_postal_address      IN   tqc_service_providers.spr_postal_address%TYPE
            DEFAULT NULL,
      v_spr_twn_code            IN   tqc_service_providers.spr_twn_code%TYPE
            DEFAULT NULL,
      v_spr_cou_code            IN   tqc_service_providers.spr_cou_code%TYPE
            DEFAULT NULL,
      v_spr_spt_code            IN   tqc_service_providers.spr_spt_code%TYPE
            DEFAULT NULL,
      v_spr_phone               IN   tqc_service_providers.spr_phone%TYPE
            DEFAULT NULL,
      v_spr_fax                 IN   tqc_service_providers.spr_fax%TYPE
            DEFAULT NULL,
      v_spr_email               IN   tqc_service_providers.spr_email%TYPE
            DEFAULT NULL,
      v_spr_title               IN   tqc_service_providers.spr_title%TYPE
            DEFAULT NULL,
      v_spr_zip                 IN   tqc_service_providers.spr_zip%TYPE
            DEFAULT NULL,
      v_spr_wef                 IN   tqc_service_providers.spr_wef%TYPE
            DEFAULT NULL,
      v_spr_wet                 IN   tqc_service_providers.spr_wet%TYPE
            DEFAULT NULL,
      v_spr_contact             IN   tqc_service_providers.spr_contact%TYPE
            DEFAULT NULL,
      v_spr_aims_code           IN   tqc_service_providers.spr_aims_code%TYPE
            DEFAULT NULL,
      v_spr_bbr_code            IN   tqc_service_providers.spr_bbr_code%TYPE
            DEFAULT NULL,
      v_spr_bank_acc_no         IN   tqc_service_providers.spr_bank_acc_no%TYPE
            DEFAULT NULL,
      v_spr_created_by          IN   tqc_service_providers.spr_created_by%TYPE
            DEFAULT NULL,
      v_spr_date_created        IN   tqc_service_providers.spr_date_created%TYPE
            DEFAULT NULL,
      v_spr_status_remarks      IN   tqc_service_providers.spr_status_remarks%TYPE
            DEFAULT NULL,
      v_spr_status              IN   tqc_service_providers.spr_status%TYPE
            DEFAULT NULL,
      v_spr_pin_number          IN   tqc_service_providers.spr_pin_number%TYPE
            DEFAULT NULL,
      v_spr_trs_occupation      IN   tqc_service_providers.spr_trs_occupation%TYPE
            DEFAULT NULL,
      v_spr_proff_body          IN   tqc_service_providers.spr_proff_body%TYPE
            DEFAULT NULL,
      v_spr_pin                 IN   tqc_service_providers.spr_pin%TYPE
            DEFAULT NULL,
      v_spr_doc_phone           IN   tqc_service_providers.spr_doc_phone%TYPE
            DEFAULT NULL,
      v_spr_doc_email           IN   tqc_service_providers.spr_doc_email%TYPE
            DEFAULT NULL,
      v_spr_gl_acc_no           IN   tqc_service_providers.spr_gl_acc_no%TYPE
            DEFAULT NULL,
      v_sprinhouse              IN   tqc_service_providers.spr_inhouse%TYPE
            DEFAULT NULL,
      v_spr_sms_number          IN   tqc_service_providers.spr_sms_number%TYPE
            DEFAULT NULL,
      v_spr_contact_person      IN   tqc_service_providers.spr_contact_person%TYPE
            DEFAULT NULL,
      v_spr_cont_person_phone   IN   tqc_service_providers.spr_cont_person_phone%TYPE
            DEFAULT NULL,
      v_spr_invoice_number      IN   tqc_service_providers.spr_invoice_number%TYPE
            DEFAULT NULL,
      v_spr_clnt_code           IN   tqc_service_providers.spr_clnt_code%TYPE
            DEFAULT NULL,
      v_spr_bpn_code            IN   tqc_service_providers.spr_bpn_code%TYPE
            DEFAULT NULL,
      v_spr_contact_email       IN   tqc_service_providers.spr_contact_email%TYPE
            DEFAULT NULL,
      v_spr_contact_tel         IN   tqc_service_providers.spr_contact_tel%TYPE
            DEFAULT NULL,
      v_spr_tel_pay             IN   tqc_service_providers.spr_tel_pay%TYPE
            DEFAULT NULL,
      v_spr_default_provider    IN   tqc_service_providers.spr_default_provider%TYPE
            DEFAULT NULL,
      v_spr_reg_no              IN   tqc_service_providers.spr_reg_no%TYPE
            DEFAULT NULL,
      v_spr_postal_code         IN   tqc_service_providers.spr_postal_code%TYPE
            DEFAULT NULL,
      v_spr_id_type             IN   tqc_service_providers.spr_id_type%TYPE
            DEFAULT NULL,
      v_spr_id_no               IN   tqc_service_providers.spr_id_no%TYPE
            DEFAULT NULL,
      v_spr_iprs_validated IN VARCHAR2
   )
   IS
      v_count            NUMBER;
      v_code             NUMBER;
      v_param_value      VARCHAR2 (1);
      v_new_ent_code     NUMBER;
      v_ent_code         NUMBER;
      v_accc_srl_fmt     VARCHAR (60);
      v_act_suffix       VARCHAR (20);
      v_agent_seq        NUMBER;
      v_serial_length    NUMBER;
      v_spt_id           VARCHAR (20);
      v_spr_short_desc   VARCHAR (60):=v_spr_sht_desc;
   BEGIN
      --raise_error ('v_spr_cou_code'||v_spr_spt_code  ||v_add_edit);
      ---added



      ---end added
      IF v_add_edit != 'D'
      THEN
         BEGIN
            SELECT ent_code
              INTO v_ent_code
              FROM tqc_entities
             WHERE ent_pin = v_spr_pin;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_ent_code := 0;
         END;

         IF v_ent_code = 0
         THEN
            tqc_entities_pkg.entities_prc ('A',
                                           v_new_ent_code,
                                           v_spr_short_desc,
                                           v_spr_name,
                                           v_spr_pin,
                                           v_spr_postal_address,
                                           v_spr_physical_address,
                                           v_spr_phone,
                                           v_spr_email
                                          );
         END IF;
      END IF;

      IF v_add_edit = 'A'
      THEN
      
      
      
            --raise_error('v_spr_id_type '||v_spr_id_type);
      BEGIN
         BEGIN
            SELECT RTRIM (LTRIM (spt_serial_format)), spt_suffixes,
                   NVL (spt_next_no, 0)
              INTO v_accc_srl_fmt, v_act_suffix,
                   v_agent_seq
              FROM tqc_service_provider_types
             WHERE spt_code = v_spr_spt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error
                     (   'Error getting Id format for service provider type '
                      || v_spr_spt_code
                     );
         END;

         BEGIN
            SELECT tqc_parameters_pkg.get_param_varchar
                                                       ('SPT_ID_SERIAL_LENGTH')
              INTO v_serial_length
              FROM DUAL;
         EXCEPTION
            WHEN OTHERS
            THEN
               NULL;
         END;

         BEGIN
            IF v_accc_srl_fmt IS NULL
            THEN
               raise_error
                          (   'Provide Id Format for  servive provider type '
                           || v_spr_spt_code
                          );
            END IF;
         END;

         v_spt_id :=
            REPLACE (v_accc_srl_fmt,
                     '[SERIALNO]',
                     LPAD (v_agent_seq, v_serial_length, '0')
                    );
         v_spt_id := REPLACE (v_spt_id, '[SFX]', v_act_suffix);

         IF NVL (v_count, 0) <> 0
         THEN
            raise_error ('An Service Provider  with the same id exist');
         END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error fetching the sequence...');
      END;

      UPDATE tqc_service_provider_types
         SET spt_next_no = NVL (spt_next_no, 0) + 1
       WHERE spt_code = v_spr_spt_code;

      v_spr_short_desc := v_spt_id;

      --raise_error('v_spr_short_desc'|| v_spr_short_desc);
      
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_service_providers
             WHERE spr_code = v_spr_code
                OR spr_id_no = v_spr_id_no
                OR spr_reg_no = v_spr_reg_no;

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
                         spr_contact_email, spr_contact_tel,
                         spr_tel_pay, spr_default_provider,
                         spr_ent_code, spr_reg_no, spr_postal_code,
                         spr_id_type, spr_id_no,spr_iprs_validated
                        )
                 VALUES (v_code, v_spr_short_desc, v_spr_name,
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
                         v_spr_contact_email, v_spr_contact_tel,
                         v_spr_tel_pay, v_spr_default_provider,
                         v_new_ent_code, v_spr_reg_no, v_spr_postal_code,
                         v_spr_id_type, v_spr_id_no,v_spr_iprs_validated
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
               SET spr_sht_desc = v_spr_short_desc,
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
                   spr_default_provider = v_spr_default_provider,
                   spr_reg_no = v_spr_reg_no,
                   spr_postal_code = v_spr_postal_code,
                   spr_id_type = v_spr_id_type,
                   spr_id_no = v_spr_id_no,
                   spr_iprs_validated=v_spr_iprs_validated
             WHERE spr_code = v_spr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while updating record v_spr_short_desc:'||v_spr_short_desc||' '
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
   PROCEDURE service_providers_prc091117 (
      v_add_edit                IN   VARCHAR2,
      v_spr_code                IN   tqc_service_providers.spr_code%TYPE
            DEFAULT NULL,
      v_spr_sht_desc            IN   tqc_service_providers.spr_sht_desc%TYPE
            DEFAULT NULL,
      v_spr_name                IN   tqc_service_providers.spr_name%TYPE
            DEFAULT NULL,
      v_spr_physical_address    IN   tqc_service_providers.spr_physical_address%TYPE
            DEFAULT NULL,
      v_spr_postal_address      IN   tqc_service_providers.spr_postal_address%TYPE
            DEFAULT NULL,
      v_spr_twn_code            IN   tqc_service_providers.spr_twn_code%TYPE
            DEFAULT NULL,
      v_spr_cou_code            IN   tqc_service_providers.spr_cou_code%TYPE
            DEFAULT NULL,
      v_spr_spt_code            IN   tqc_service_providers.spr_spt_code%TYPE
            DEFAULT NULL,
      v_spr_phone               IN   tqc_service_providers.spr_phone%TYPE
            DEFAULT NULL,
      v_spr_fax                 IN   tqc_service_providers.spr_fax%TYPE
            DEFAULT NULL,
      v_spr_email               IN   tqc_service_providers.spr_email%TYPE
            DEFAULT NULL,
      v_spr_title               IN   tqc_service_providers.spr_title%TYPE
            DEFAULT NULL,
      v_spr_zip                 IN   tqc_service_providers.spr_zip%TYPE
            DEFAULT NULL,
      v_spr_wef                 IN   tqc_service_providers.spr_wef%TYPE
            DEFAULT NULL,
      v_spr_wet                 IN   tqc_service_providers.spr_wet%TYPE
            DEFAULT NULL,
      v_spr_contact             IN   tqc_service_providers.spr_contact%TYPE
            DEFAULT NULL,
      v_spr_aims_code           IN   tqc_service_providers.spr_aims_code%TYPE
            DEFAULT NULL,
      v_spr_bbr_code            IN   tqc_service_providers.spr_bbr_code%TYPE
            DEFAULT NULL,
      v_spr_bank_acc_no         IN   tqc_service_providers.spr_bank_acc_no%TYPE
            DEFAULT NULL,
      v_spr_created_by          IN   tqc_service_providers.spr_created_by%TYPE
            DEFAULT NULL,
      v_spr_date_created        IN   tqc_service_providers.spr_date_created%TYPE
            DEFAULT NULL,
      v_spr_status_remarks      IN   tqc_service_providers.spr_status_remarks%TYPE
            DEFAULT NULL,
      v_spr_status              IN   tqc_service_providers.spr_status%TYPE
            DEFAULT NULL,
      v_spr_pin_number          IN   tqc_service_providers.spr_pin_number%TYPE
            DEFAULT NULL,
      v_spr_trs_occupation      IN   tqc_service_providers.spr_trs_occupation%TYPE
            DEFAULT NULL,
      v_spr_proff_body          IN   tqc_service_providers.spr_proff_body%TYPE
            DEFAULT NULL,
      v_spr_pin                 IN   tqc_service_providers.spr_pin%TYPE
            DEFAULT NULL,
      v_spr_doc_phone           IN   tqc_service_providers.spr_doc_phone%TYPE
            DEFAULT NULL,
      v_spr_doc_email           IN   tqc_service_providers.spr_doc_email%TYPE
            DEFAULT NULL,
      v_spr_gl_acc_no           IN   tqc_service_providers.spr_gl_acc_no%TYPE
            DEFAULT NULL,
      v_sprinhouse              IN   tqc_service_providers.spr_inhouse%TYPE
            DEFAULT NULL,
      v_spr_sms_number          IN   tqc_service_providers.spr_sms_number%TYPE
            DEFAULT NULL,
      v_spr_contact_person      IN   tqc_service_providers.spr_contact_person%TYPE
            DEFAULT NULL,
      v_spr_cont_person_phone   IN   tqc_service_providers.spr_cont_person_phone%TYPE
            DEFAULT NULL,
      v_spr_invoice_number      IN   tqc_service_providers.spr_invoice_number%TYPE
            DEFAULT NULL,
      v_spr_clnt_code           IN   tqc_service_providers.spr_clnt_code%TYPE
            DEFAULT NULL,
      v_spr_bpn_code            IN   tqc_service_providers.spr_bpn_code%TYPE
            DEFAULT NULL,
      v_spr_contact_email       IN   tqc_service_providers.spr_contact_email%TYPE
            DEFAULT NULL,
      v_spr_contact_tel         IN   tqc_service_providers.spr_contact_tel%TYPE
            DEFAULT NULL,
      v_spr_tel_pay             IN   tqc_service_providers.spr_tel_pay%TYPE
            DEFAULT NULL,
      v_spr_default_provider    IN   tqc_service_providers.spr_default_provider%TYPE
            DEFAULT NULL,
      v_spr_reg_no              IN   tqc_service_providers.spr_reg_no%TYPE
            DEFAULT NULL,
      v_spr_postal_code         IN   tqc_service_providers.spr_postal_code%TYPE
            DEFAULT NULL,
      v_spr_id_type             IN   tqc_service_providers.spr_id_type%TYPE
            DEFAULT NULL,
      v_spr_id_no               IN   tqc_service_providers.spr_id_no%TYPE
            DEFAULT NULL,
      v_spr_iprs_validated IN VARCHAR2
   )
   IS
      v_count            NUMBER;
      v_code             NUMBER;
      v_param_value      VARCHAR2 (1);
      v_new_ent_code     NUMBER;
      v_ent_code         NUMBER;
      v_accc_srl_fmt     VARCHAR (60);
      v_act_suffix       VARCHAR (20);
      v_agent_seq        NUMBER;
      v_serial_length    NUMBER;
      v_spt_id           VARCHAR (20);
      v_spr_short_desc   VARCHAR (60):=v_spr_sht_desc;
   BEGIN
      --raise_error ('v_spr_cou_code'||v_spr_spt_code  ||v_add_edit);
      ---added



      ---end added
      IF v_add_edit != 'D'
      THEN
         BEGIN
            SELECT ent_code
              INTO v_ent_code
              FROM tqc_entities
             WHERE ent_pin = v_spr_pin;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_ent_code := 0;
         END;

         IF v_ent_code = 0
         THEN
            tqc_entities_pkg.entities_prc ('A',
                                           v_new_ent_code,
                                           v_spr_short_desc,
                                           v_spr_name,
                                           v_spr_pin,
                                           v_spr_postal_address,
                                           v_spr_physical_address,
                                           v_spr_phone,
                                           v_spr_email
                                          );
         END IF;
      END IF;

      IF v_add_edit = 'A'
      THEN
      
      
      
            --raise_error('v_spr_id_type '||v_spr_id_type);
      BEGIN
         BEGIN
            SELECT RTRIM (LTRIM (spt_serial_format)), spt_suffixes,
                   NVL (spt_next_no, 0)
              INTO v_accc_srl_fmt, v_act_suffix,
                   v_agent_seq
              FROM tqc_service_provider_types
             WHERE spt_code = v_spr_spt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error
                     (   'Error getting Id format for service provider type '
                      || v_spr_spt_code
                     );
         END;

         BEGIN
            SELECT tqc_parameters_pkg.get_param_varchar
                                                       ('SPT_ID_SERIAL_LENGTH')
              INTO v_serial_length
              FROM DUAL;
         EXCEPTION
            WHEN OTHERS
            THEN
               NULL;
         END;

         BEGIN
            IF v_accc_srl_fmt IS NULL
            THEN
               raise_error
                          (   'Provide Id Format for  servive provider type '
                           || v_spr_spt_code
                          );
            END IF;
         END;

         v_spt_id :=
            REPLACE (v_accc_srl_fmt,
                     '[SERIALNO]',
                     LPAD (v_agent_seq, v_serial_length, '0')
                    );
         v_spt_id := REPLACE (v_spt_id, '[SFX]', v_act_suffix);

         IF NVL (v_count, 0) <> 0
         THEN
            raise_error ('An Service Provider  with the same id exist');
         END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error fetching the sequence...');
      END;

      UPDATE tqc_service_provider_types
         SET spt_next_no = NVL (spt_next_no, 0) + 1
       WHERE spt_code = v_spr_spt_code;

      v_spr_short_desc := v_spt_id;

      --raise_error('v_spr_short_desc'|| v_spr_short_desc);
      
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_service_providers
             WHERE spr_code = v_spr_code
                OR spr_id_no = v_spr_id_no
                OR spr_reg_no = v_spr_reg_no;

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
                         spr_contact_email, spr_contact_tel,
                         spr_tel_pay, spr_default_provider,
                         spr_ent_code, spr_reg_no, spr_postal_code,
                         spr_id_type, spr_id_no,spr_iprs_validated
                        )
                 VALUES (v_code, v_spr_short_desc, v_spr_name,
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
                         v_spr_contact_email, v_spr_contact_tel,
                         v_spr_tel_pay, v_spr_default_provider,
                         v_new_ent_code, v_spr_reg_no, v_spr_postal_code,
                         v_spr_id_type, v_spr_id_no,v_spr_iprs_validated
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
               SET spr_sht_desc = v_spr_short_desc,
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
                   spr_default_provider = v_spr_default_provider,
                   spr_reg_no = v_spr_reg_no,
                   spr_postal_code = v_spr_postal_code,
                   spr_id_type = v_spr_id_type,
                   spr_id_no = v_spr_id_no,
                   spr_iprs_validated=v_spr_iprs_validated
             WHERE spr_code = v_spr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while updating record v_spr_short_desc:'||v_spr_short_desc||' '
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

   PROCEDURE service_prov_activities_prc (
      v_add_edit           IN   VARCHAR2,
      v_spa_code           IN   tqc_serv_prv_activities.spa_code%TYPE,
      v_spa_spt_code       IN   tqc_serv_prv_activities.spa_spt_code%TYPE,
      v_spa_spt_sht_desc   IN   tqc_serv_prv_activities.spa_spt_sht_desc%TYPE,
      v_spa_spr_code       IN   tqc_serv_prv_activities.spa_spr_code%TYPE,
      v_spa_spr_sht_desc   IN   tqc_serv_prv_activities.spa_spr_sht_desc%TYPE,
      v_spt_main_act       IN   tqc_serv_prv_activities.spt_main_act%TYPE,
      v_spa_desc           IN   tqc_serv_prv_activities.spa_desc%TYPE,
      v_spa_spta_code      IN   NUMBER
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_serv_prv_activities
             WHERE spa_code = v_spa_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_serv_prv_activities (spa_code,
                                                 spa_spt_code,
                                                 spa_spt_sht_desc,
                                                 spa_spr_code,
                                                 spa_spr_sht_desc,
                                                 spt_main_act,
                                                 spa_desc,
                                                 spa_spta_code)
                 VALUES (tqc_spa_code_seq.NEXTVAL,
                         v_spa_spt_code,
                         v_spa_spt_sht_desc,
                         v_spa_spr_code,
                         v_spa_spr_sht_desc,
                         v_spt_main_act,
                         v_spa_desc,
                         v_spa_spta_code);
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
            UPDATE tqc_serv_prv_activities
               SET spa_spt_code = v_spa_spt_code,
                   spa_spt_sht_desc = v_spa_spt_sht_desc,
                   spa_spr_code = v_spa_spr_code,
                   spa_spr_sht_desc = v_spa_spr_sht_desc,
                   spt_main_act = v_spt_main_act,
                   spa_desc = v_spa_desc,
                   spa_spta_code = v_spa_spta_code
             WHERE spa_code = v_spa_code;
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
            DELETE FROM tqc_serv_prv_activities
                  WHERE spa_code = v_spa_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

--   PROCEDURE agencies_prc (
--      v_add_edit                  IN     VARCHAR2,
--      v_agn_code                  IN     tqc_agencies.agn_code%TYPE,
--      v_agn_act_code              IN     tqc_agencies.agn_act_code%TYPE,
--      v_agn_sht_desc              IN OUT tqc_agencies.agn_sht_desc%TYPE,
--      v_agn_name                  IN     tqc_agencies.agn_name%TYPE,
--      v_agn_physical_address      IN     tqc_agencies.agn_physical_address%TYPE,
--      v_agn_postal_address        IN     tqc_agencies.agn_postal_address%TYPE,
--      v_agn_twn_code              IN     tqc_agencies.agn_twn_code%TYPE,
--      v_agn_cou_code              IN     tqc_agencies.agn_cou_code%TYPE,
--      v_agn_email_address         IN     tqc_agencies.agn_email_address%TYPE,
--      v_agn_web_address           IN     tqc_agencies.agn_web_address%TYPE,
--      v_agn_zip                   IN     tqc_agencies.agn_zip%TYPE,
--      v_agn_contact_person        IN     tqc_agencies.agn_contact_person%TYPE,
--      v_agn_contact_title         IN     tqc_agencies.agn_contact_title%TYPE,
--      v_agn_tel1                  IN     tqc_agencies.agn_tel1%TYPE,
--      v_agn_tel2                  IN     tqc_agencies.agn_tel2%TYPE,
--      v_agn_fax                   IN     tqc_agencies.agn_fax%TYPE,
--      v_agn_acc_no                IN     tqc_agencies.agn_acc_no%TYPE,
--      v_agn_pin                   IN     tqc_agencies.agn_pin%TYPE,
--      v_agn_agent_commission      IN     tqc_agencies.agn_agent_commission%TYPE,
--      v_agn_credit_allowed        IN     tqc_agencies.agn_credit_allowed%TYPE,
--      v_agn_agent_wht_tax         IN     tqc_agencies.agn_agent_wht_tax%TYPE,
--      v_agn_print_dbnote          IN     tqc_agencies.agn_print_dbnote%TYPE,
--      v_agn_status                IN     tqc_agencies.agn_status%TYPE,
--      v_agn_date_created          IN     tqc_agencies.agn_date_created%TYPE,
--      v_agn_created_by            IN     tqc_agencies.agn_created_by%TYPE,
--      v_agn_reg_code              IN     tqc_agencies.agn_reg_code%TYPE,
--      v_agn_comm_reserve_rate     IN     tqc_agencies.agn_comm_reserve_rate%TYPE,
--      v_agn_annual_budget         IN     tqc_agencies.agn_annual_budget%TYPE,
--      v_agn_status_eff_date       IN     tqc_agencies.agn_status_eff_date%TYPE,
--      v_agn_credit_period         IN     tqc_agencies.agn_credit_period%TYPE,
--      v_agn_comm_stat_eff_dt      IN     tqc_agencies.agn_comm_stat_eff_dt%TYPE,
--      v_agn_comm_status_dt        IN     tqc_agencies.agn_comm_status_dt%TYPE,
--      v_agn_comm_allowed          IN     tqc_agencies.agn_comm_allowed%TYPE,
--      v_agn_checked               IN     tqc_agencies.agn_checked%TYPE,
--      v_agn_checked_by            IN     tqc_agencies.agn_checked_by%TYPE,
--      v_agn_check_date            IN     tqc_agencies.agn_check_date%TYPE,
--      v_agn_comp_comm_arrears     IN     tqc_agencies.agn_comp_comm_arrears%TYPE,
--      v_agn_reinsurer             IN     tqc_agencies.agn_reinsurer%TYPE,
--      v_agn_brn_code              IN     tqc_agencies.agn_brn_code%TYPE,
--      v_agn_town                  IN     tqc_agencies.agn_town%TYPE,
--      v_agn_country               IN     tqc_agencies.agn_country%TYPE,
--      v_agn_status_desc           IN     tqc_agencies.agn_status_desc%TYPE,
--      v_agn_id_no                 IN     tqc_agencies.agn_id_no%TYPE,
--      v_agn_con_code              IN     tqc_agencies.agn_con_code%TYPE,
--      v_agn_agn_code              IN     tqc_agencies.agn_agn_code%TYPE,
--      v_agn_sms_tel               IN     tqc_agencies.agn_sms_tel%TYPE,
--      v_agn_ahc_code              IN     tqc_agencies.agn_ahc_code%TYPE,
--      v_agn_sec_code              IN     tqc_agencies.agn_sec_code%TYPE,
--      v_agn_agnc_class_code       IN     tqc_agencies.agn_agnc_class_code%TYPE,
--      v_agn_expiry_date           IN     tqc_agencies.agn_expiry_date%TYPE,
--      v_agn_license_no            IN     tqc_agencies.agn_license_no%TYPE,
--      v_agn_runoff                IN     tqc_agencies.agn_runoff%TYPE,
--      v_agn_licensed              IN     tqc_agencies.agn_licensed%TYPE,
--      v_agn_license_grace_pr      IN     tqc_agencies.agn_license_grace_pr%TYPE,
--      v_agn_old_acc_no            IN     tqc_agencies.agn_old_acc_no%TYPE,
--      v_agn_status_remarks        IN     tqc_agencies.agn_status_remarks%TYPE,
--      v_agn_bbr_code              IN     tqc_agencies.agn_bbr_code%TYPE DEFAULT NULL,
--      v_agn_bank_acc_no           IN     tqc_agencies.agn_bank_acc_no%TYPE DEFAULT NULL,
--      v_agn_unique_prefix         IN     tqc_agencies.agn_unique_prefix%TYPE DEFAULT NULL,
--      v_agn_state_code            IN     tqc_agencies.agn_state_code%TYPE DEFAULT NULL,
--      v_agn_crdt_rting            IN     tqc_agencies.agn_crdt_rting%TYPE DEFAULT NULL,
--      v_agn_subagent              IN     tqc_agencies.agn_subagent%TYPE DEFAULT NULL,
--      v_agn_main_agn_code         IN     tqc_agencies.agn_main_agn_code%TYPE DEFAULT NULL,
--      v_agn_clnt_code             IN     tqc_agencies.agn_clnt_code%TYPE DEFAULT NULL,
--      v_agn_account_manager       IN     tqc_agencies.agn_account_manager%TYPE DEFAULT NULL,
--      v_agn_credit_limit          IN     tqc_agencies.agn_credit_limit%TYPE DEFAULT NULL,
--      v_agn_bru_code              IN     tqc_agencies.agn_bru_code%TYPE DEFAULT NULL,
--      v_agn_local_international   IN     tqc_agencies.agn_local_international%TYPE DEFAULT NULL,
--      v_agn_regulator_number      IN     tqc_agencies.agn_regulator_number%TYPE DEFAULT NULL,
--      v_agn_rorg_code             IN     tqc_agencies.agn_rorg_code%TYPE DEFAULT NULL,
--      v_agn_ors_code              IN     tqc_agencies.agn_ors_code%TYPE DEFAULT NULL,
--      v_agn_allocate_cert         IN     tqc_agencies.agn_allocate_cert%TYPE DEFAULT NULL,
--      v_agn_bounced_chq           IN     tqc_agencies.agn_bounced_chq%TYPE DEFAULT NULL,
--      v_def_comm_mode             IN     tqc_agencies.agn_default_comm_mode%TYPE DEFAULT NULL,
--      v_agn_bpn_code              IN     tqc_agencies.agn_bpn_code%TYPE DEFAULT NULL,
--      v_agn_agent_type            IN     tqc_agencies.agn_agent_type%TYPE DEFAULT NULL,
--      v_agn_group                 IN     tqc_agencies.agn_group%TYPE DEFAULT NULL,
--      v_vat_appl                  IN     tqc_agencies.agn_vat_applicable%TYPE,
--      v_withtaxappl               IN     tqc_agencies.agn_whtax_applicable%TYPE,
--      v_agn_tel_pay               IN     tqc_agencies.agn_tel_pay%TYPE,
--      v_agnResetCode              IN     VARCHAR2 DEFAULT NULL,
--      v_freq_payment              IN     VARCHAR2 DEFAULT NULL,
--      v_pymt_mode                 IN     VARCHAR2 DEFAULT NULL,
--      v_pymt_validated            IN     VARCHAR2 DEFAULT NULL,
--      v_agn_benefit_start_date    IN     DATE DEFAULT NULL,
--      v_agn_dob                   IN     DATE DEFAULT NULL,
--      v_agn_qualification         IN     VARCHAR2 DEFAULT NULL,
--      v_agn_marital_status        IN     VARCHAR2 DEFAULT NULL,
--      v_id_no_doc_used            IN     VARCHAR2 DEFAULT NULL,
--      v_agn_sbu_code              IN     VARCHAR2 DEFAULT NULL,
--      v_agn_comm_levy_app         IN     VARCHAR2,
--      v_agn_comm_levy_rate        IN     NUMBER,
--      v_agn_brr_code              IN     NUMBER,
--      v_agn_brr_name              IN     VARCHAR2 DEFAULT NULL)
--   IS
--      v_direct_srl_fmt     VARCHAR2 (50);
--      v_agent_id           VARCHAR2 (50);
--      v_agent_seq          NUMBER;
--      v_name_first_char    VARCHAR2 (20);
--      v_no_of_chars        NUMBER := 1;
--      v_serial_length      NUMBER := 6;
--      v_act_type           VARCHAR2 (2);
--      v_count              NUMBER := 0;
--      v_created_by         VARCHAR2 (20);
--      v_date_created       DATE;
--      v_agn_short_desc     VARCHAR2 (200);
--      v_act_desc           VARCHAR2 (75);
--      v_act_wthtx_rate     NUMBER;
--      v_brn_sht_desc       tqc_branches.brn_sht_desc%TYPE;
--      v_param_value        VARCHAR2 (30);
--      v_decodesmstel       VARCHAR2 (50);
--      v_rorg_code          NUMBER;
--      v_ors_code           NUMBER;
--      v_accCount           NUMBER;
--      v_sms_tel_count      NUMBER;
--      v_agn_authorised     VARCHAR2 (10);
--      v_old_agn_sht_desc   VARCHAR2 (30);
--      V_ERR_MSG            VARCHAR2 (200);
--   ----------------
--
--   BEGIN
--      --RAISE_ERROR('fdfdf'||v_agn_main_agn_code);
--      --        RAISE_ERROR(
--      --        'v_agn_comm_levy_app: '||v_agn_comm_levy_app||
--      --        'v_agn_comm_levy_rate: '||v_agn_comm_levy_rate
--      --       );
--      IF v_agn_sms_tel IS NOT NULL
--      THEN
--         SELECT INSTR (v_agn_sms_tel, '+') INTO v_sms_tel_count FROM DUAL;
--
--         IF v_sms_tel_count = 0
--         THEN
--            BEGIN
--               SELECT '+' || cou_zip_code || v_agn_sms_tel
--                 INTO v_decodesmstel
--                 FROM tqc_countries
--                WHERE cou_code = v_agn_cou_code;
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  NULL;
--            END;
--         ELSE
--            v_decodesmstel := v_agn_sms_tel;
--         END IF;
--      END IF;
--
--      BEGIN
--         SELECT RTRIM (LTRIM (act_id_serial_format)),
--                act_type_sht_desc,
--                act_account_type,
--                act_wthtx_rate
--           INTO v_direct_srl_fmt,
--                v_act_type,
--                v_act_desc,
--                v_act_wthtx_rate
--           FROM tqc_account_types
--          WHERE act_code = v_agn_act_code;
--      EXCEPTION
--         WHEN OTHERS
--         THEN
--            raise_error ('Error getting Id format for account type ');
--      END;
--
--      IF v_add_edit = 'A'
--      THEN
--         IF v_agn_name IS NULL
--         THEN
--            raise_error ('Agent Name cannot be null');
--         END IF;
--
--         v_created_by := v_agn_created_by;
--         v_date_created := TRUNC (SYSDATE);
--         v_agn_short_desc := v_agn_sht_desc;
--
--         IF v_agn_short_desc IS NULL
--         THEN
--            BEGIN
--               SELECT tqc_parameters_pkg.get_param_varchar (
--                         'AGN_ID_SERIAL_LENGTH')
--                 INTO v_serial_length
--                 FROM DUAL;
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  NULL;
--            END;
--
--            BEGIN
--               SELECT param_value
--                 INTO v_no_of_chars
--                 FROM tqc_parameters
--                WHERE param_name = 'AGN_CHARS';
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  v_no_of_chars := 1;
--            END;
--
--            IF v_direct_srl_fmt IS NULL
--            THEN
--               raise_error (
--                     'Provide Id Format for  account type '
--                  || v_act_desc
--                  || '  OR use the button Account id to define the Id');
--            END IF;
--
--            BEGIN
--               SELECT SUBSTR (LTRIM (v_agn_name), 1, v_no_of_chars)
--                 INTO v_name_first_char
--                 FROM DUAL;
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  raise_error ('Error getting Agent short description ');
--            END;
--
--            BEGIN
--               IF v_act_type IN ('A', 'IA')
--               THEN
--                  SELECT agent_id_a_seq.NEXTVAL INTO v_agent_seq FROM DUAL;
--               ELSIF v_act_type = 'B'
--               THEN
--                  SELECT agent_id_b_seq.NEXTVAL INTO v_agent_seq FROM DUAL;
--               ELSIF v_act_type = 'D'
--               THEN
--                  SELECT agent_id_d_seq.NEXTVAL INTO v_agent_seq FROM DUAL;
--               ELSIF v_act_type = 'I'
--               THEN
--                  SELECT agent_id_i_seq.NEXTVAL INTO v_agent_seq FROM DUAL;
--               ELSIF v_act_type = 'R'
--               THEN
--                  SELECT agent_id_r_seq.NEXTVAL INTO v_agent_seq FROM DUAL;
--               ELSE
--                  SELECT agent_id_seq.NEXTVAL INTO v_agent_seq FROM DUAL;
--               END IF;
--
--               BEGIN
--                  SELECT brn_sht_desc
--                    INTO v_brn_sht_desc
--                    FROM tqc_branches
--                   WHERE brn_code = v_agn_brn_code;
--               EXCEPTION
--                  WHEN OTHERS
--                  THEN
--                     raise_error ('Unable to fetch branch!');
--               END;
--
--               v_agent_id :=
--                  REPLACE (v_direct_srl_fmt,
--                           '[SERIALNO]',
--                           LPAD (v_agent_seq, v_serial_length, 0));
--               v_agent_id :=
--                  REPLACE (v_agent_id, '[FNAMEI]', v_name_first_char);
--               v_agent_id := REPLACE (v_agent_id, '[BRN]', v_brn_sht_desc);
--
--               BEGIN
--                  SELECT COUNT (1)
--                    INTO v_count
--                    FROM tqc_agencies
--                   WHERE agn_sht_desc = v_agent_id;
--               EXCEPTION
--                  WHEN OTHERS
--                  THEN
--                     raise_error ('Error Checking duplicate Agent id');
--               END;
--
--               IF NVL (v_count, 0) <> 0
--               THEN
--                  raise_error ('An Agent with the same id exist');
--               END IF;
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  raise_error ('Error fetching the sequence...');
--            END;
--
--            v_agn_short_desc := v_agent_id;
--         END IF;
--
--         BEGIN
--            SELECT COUNT (1)
--              INTO v_count
--              FROM tqc_agencies
--             WHERE agn_code = v_agn_code;
--
--            IF v_count > 0
--            THEN
--               raise_error ('Record with ID Exists!');
--            END IF;
--
--            v_agn_sht_desc := v_agn_short_desc;
--
--            INSERT INTO tqc_agencies (agn_code,
--                                      agn_act_code,
--                                      agn_sht_desc,
--                                      agn_name,
--                                      agn_physical_address,
--                                      agn_postal_address,
--                                      agn_twn_code,
--                                      agn_cou_code,
--                                      agn_email_address,
--                                      agn_web_address,
--                                      agn_zip,
--                                      agn_contact_person,
--                                      agn_contact_title,
--                                      agn_tel1,
--                                      agn_tel2,
--                                      agn_fax,
--                                      agn_acc_no,
--                                      agn_pin,
--                                      agn_agent_commission,
--                                      agn_credit_allowed,
--                                      agn_agent_wht_tax,
--                                      agn_print_dbnote,
--                                      agn_status,
--                                      agn_date_created,
--                                      agn_created_by,
--                                      agn_reg_code,
--                                      agn_comm_reserve_rate,
--                                      agn_annual_budget,
--                                      agn_status_eff_date,
--                                      agn_credit_period,
--                                      agn_comm_stat_eff_dt,
--                                      agn_comm_status_dt,
--                                      agn_comm_allowed,
--                                      agn_checked,
--                                      agn_checked_by,
--                                      agn_check_date,
--                                      agn_comp_comm_arrears,
--                                      agn_reinsurer,
--                                      agn_brn_code,
--                                      agn_town,
--                                      agn_country,
--                                      agn_status_desc,
--                                      agn_id_no,
--                                      agn_con_code,
--                                      agn_agn_code,
--                                      agn_sms_tel,
--                                      agn_ahc_code,
--                                      agn_sec_code,
--                                      agn_agnc_class_code,
--                                      agn_expiry_date,
--                                      agn_license_no,
--                                      agn_runoff,
--                                      agn_licensed,
--                                      agn_license_grace_pr,
--                                      agn_old_acc_no,
--                                      agn_status_remarks,
--                                      agn_bbr_code,
--                                      agn_bank_acc_no,
--                                      agn_unique_prefix,
--                                      agn_state_code,
--                                      agn_crdt_rting,
--                                      agn_subagent,
--                                      agn_main_agn_code,
--                                      agn_clnt_code,
--                                      agn_account_manager,
--                                      agn_credit_limit,
--                                      agn_bru_code,
--                                      agn_local_international,
--                                      agn_regulator_number,
--                                      agn_rorg_code,
--                                      agn_ors_code,
--                                      agn_allocate_cert,
--                                      agn_bounced_chq,
--                                      agn_bpn_code,
--                                      agn_agent_type,
--                                      agn_group,
--                                      agn_default_comm_mode,
--                                      agn_vat_applicable,
--                                      agn_whtax_applicable,
--                                      agn_tel_pay,
--                                      agn_payment_freq,
--                                      agn_default_cpm_mode,
--                                      agn_pymt_validated,
--                                      agn_benefit_start_date,
--                                      agn_birth_date,
--                                      agn_qualification,
--                                      agn_marital_status,
--                                      agn_id_no_doc_used,
--                                      agn_sbu_code,
--                                      agn_comm_levy_app,
--                                      agn_comm_levy_rate,
--                                      agn_brr_code,
--                                      agn_brr_name)
--                 VALUES (tqc_agn_code_seq.NEXTVAL,
--                         v_agn_act_code,
--                         v_agn_short_desc,
--                         v_agn_name,
--                         v_agn_physical_address,
--                         v_agn_postal_address,
--                         v_agn_twn_code,
--                         v_agn_cou_code,
--                         v_agn_email_address,
--                         v_agn_web_address,
--                         v_agn_zip,
--                         v_agn_contact_person,
--                         v_agn_contact_title,
--                         v_agn_tel1,
--                         v_agn_tel2,
--                         v_agn_fax,
--                         v_agn_acc_no,
--                         v_agn_pin,
--                         v_agn_agent_commission,
--                         v_agn_credit_allowed,
--                         v_agn_agent_wht_tax,
--                         v_agn_print_dbnote,
--                         v_agn_status,
--                         v_agn_date_created,
--                         v_agn_created_by,
--                         v_agn_reg_code,
--                         v_agn_comm_reserve_rate,
--                         v_agn_annual_budget,
--                         v_agn_status_eff_date,
--                         v_agn_credit_period,
--                         v_agn_comm_stat_eff_dt,
--                         v_agn_comm_status_dt,
--                         v_agn_comm_allowed,
--                         v_agn_checked,
--                         v_agn_checked_by,
--                         v_agn_check_date,
--                         v_agn_comp_comm_arrears,
--                         v_agn_reinsurer,
--                         v_agn_brn_code,
--                         v_agn_town,
--                         v_agn_country,
--                         v_agn_status_desc,
--                         v_agn_id_no,
--                         v_agn_con_code,
--                         v_agn_agn_code,
--                         v_decodesmstel,
--                         v_agn_ahc_code,
--                         v_agn_sec_code,
--                         v_agn_agnc_class_code,
--                         v_agn_expiry_date,
--                         v_agn_license_no,
--                         v_agn_runoff,
--                         v_agn_licensed,
--                         v_agn_license_grace_pr,
--                         v_agn_old_acc_no,
--                         v_agn_status_remarks,
--                         v_agn_bbr_code,
--                         v_agn_bank_acc_no,
--                         v_agn_unique_prefix,
--                         v_agn_state_code,
--                         v_agn_crdt_rting,
--                         v_agn_subagent,
--                         v_agn_main_agn_code,
--                         v_agn_clnt_code,
--                         v_agn_account_manager,
--                         v_agn_credit_limit,
--                         v_agn_bru_code,
--                         v_agn_local_international,
--                         v_agn_regulator_number,
--                         v_agn_rorg_code,
--                         v_agn_ors_code,
--                         v_agn_allocate_cert,
--                         v_agn_bounced_chq,
--                         v_agn_bpn_code,
--                         v_agn_agent_type,
--                         v_agn_group,
--                         v_def_comm_mode,
--                         v_vat_appl,
--                         v_withtaxappl,
--                         v_agn_tel_pay,
--                         v_freq_payment,
--                         v_pymt_mode,
--                         v_pymt_validated,
--                         v_agn_benefit_start_date,
--                         v_agn_dob,
--                         v_agn_qualification,
--                         v_agn_marital_status,
--                         v_id_no_doc_used,
--                         v_agn_sbu_code,
--                         v_agn_comm_levy_app,
--                         v_agn_comm_levy_rate,
--                         v_agn_brr_code,
--                         v_agn_brr_name);
--
--            tq_lms.lms_web_pkg_setup.updateagenies (v_agn_code,
--                                                    v_agn_short_desc,
--                                                    v_agn_name,
--                                                    v_agn_id_no,
--                                                    v_agn_sms_tel,
--                                                    v_agn_comm_allowed,
--                                                    NULL,   --  V_CONT_STATUS,
--                                                    NULL,       --  V_CERT_NO,
--                                                    NULL,     --  V_CONT_DATE,
--                                                    NULL,   --  V_DATE_JOINED,
--                                                    NULL, --  V_DATE_TERMINATED,
--                                                    NULL,          --  V_BANK,
--                                                    v_agn_bank_acc_no,
--                                                    v_agn_sms_tel,
--                                                    v_agn_email_address,
--                                                    v_agn_reinsurer,
--                                                    NULL,           --V_GL_ACC
--                                                    v_agn_credit_allowed,
--                                                    NULL,       --v_agn_wxtax,
--                                                    NULL,      --v_agn_dbnote,
--                                                    v_agn_status,
--                                                    NULL,  --v_agn_reg_status,
--                                                    NULL,  --v_agn_reg_number,
--                                                    NULL, --v_agn_comm_payroll,
--                                                    NULL,    --v_agn_pay_mthd,
--                                                    v_agn_act_code,
--                                                    v_agn_pin,
--                                                    v_agn_bru_code,
--                                                    v_agn_brn_code,
--                                                    NULL,      --v_agn_ref_no,
--                                                    NULL,  --v_agn_post_level,
--                                                    v_err_msg,
--                                                    NULL,  --v_agn_sub_status,
--                                                    NULL, --v_agn_sub_status_date,
--                                                    v_agn_bbr_code,
--                                                    NULL, --v_agn_cap_exempted,
--                                                    NULL, --v_agn_phy_address,
--                                                    v_agn_dob,
--                                                    NULL, --v_agn_retainer_amnt,
--                                                    NULL     --v_agn_sact_code
--                                                        );
--
--            --IF v_agn_act_code IN (2,25)
--            --THEN
--            BEGIN
--               SELECT param_value
--                 INTO v_param_value
--                 FROM tqc_parameters
--                WHERE param_name = 'AUTO_ASSIGN_AGN_SYS';
--            EXCEPTION
--               WHEN NO_DATA_FOUND
--               THEN
--                  v_param_value := 'N';
--               WHEN OTHERS
--               THEN
--                  v_param_value := 'N';
--            END;
--
--            BEGIN
--               IF v_param_value = 'Y'
--               THEN
--                  INSERT INTO tqc_agency_systems (asys_sys_code,
--                                                  asys_agn_code,
--                                                  asys_wef,
--                                                  asys_wet,
--                                                  asys_comment)
--                       VALUES (37,
--                               tqc_agn_code_seq.CURRVAL,
--                               SYSDATE,
--                               NULL,
--                               NULL);
--               END IF;
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  NULL;
--            END;
--
--            BEGIN
--               INSERT INTO tqc_account_contacts (accc_code,
--                                                 accc_acc_code,
--                                                 accc_name,
--                                                 accc_other_names,
--                                                 accc_tel,
--                                                 accc_email_addr,
--                                                 accc_sms_tel,
--                                                 accc_username,
--                                                 accc_pwd,
--                                                 accc_login_allowed,
--                                                 accc_pwd_changed,
--                                                 accc_pwd_reset,
--                                                 accc_dt_created,
--                                                 accc_status,
--                                                 accc_login_attempts,
--                                                 accc_personel_rank,
--                                                 accc_last_login_date,
--                                                 accc_created_by,
--                                                 accc_sys_code,
--                                                 accc_reset_code)
--                    VALUES (tqc_accc_code_seq.NEXTVAL,
--                            tqc_agn_code_seq.CURRVAL,
--                            v_agn_short_desc,
--                            v_agn_name,
--                            v_agn_tel1,
--                            v_agn_email_address,
--                            v_decodesmstel,
--                            v_agn_short_desc,
--                            /*v_account_contacts_tab(I).ACCC_PWD*/
--                            GIS_READ.VAL ('123456'),
--                            'Y',
--                            SYSDATE,
--                            'Y',
--                            SYSDATE,
--                            'A',
--                            0,
--                            'AGENCY',
--                            SYSDATE,
--                            'SYSTEM',
--                            NULL,
--                            v_agnResetCode);
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  NULL;
--            END;
--         -- END IF;
--         EXCEPTION
--            WHEN OTHERS
--            THEN
--               raise_error (
--                  'Error occured while creating record ' || SQLERRM (SQLCODE));
--         END;
--      ELSIF v_add_edit = 'E'
--      THEN
--         BEGIN
--            SELECT AGN_AUTHORISED, agn_sht_desc
--              INTO v_agn_authorised, v_old_agn_sht_desc
--              FROM tqc_agencies
--             WHERE agn_code = v_agn_code;
--
--            IF v_agn_authorised = 'Y'
--               AND v_old_agn_sht_desc <> v_agn_sht_desc
--            THEN
--               RAISE_ERROR (
--                  'AGENT SHORT DESCRIPTION SHOULD NOT BE EDITED FOR AN AUTHORIZED CLIENT REVERT TO ....'
--                  || v_old_agn_sht_desc);
--            END IF;
--
--            BEGIN
--               SELECT agn_rorg_code, agn_ors_code
--                 INTO v_rorg_code, v_ors_code
--                 FROM tqc_agencies
--                WHERE agn_code = v_agn_code;
--            END;
--
--            IF v_rorg_code != v_agn_rorg_code OR v_ors_code != v_agn_ors_code
--            THEN
--               INSERT
--                 INTO tqc_agency_ratings_logs (arl_code,
--                                               arl_rorg_code,
--                                               arl_ors_code)
--               VALUES (
--                         tqc_arl_code_seq.NEXTVAL,
--                         v_agn_rorg_code,
--                         v_agn_ors_code);
--            END IF;
--
--            UPDATE tqc_agencies
--               SET agn_act_code = v_agn_act_code,
--                   agn_sht_desc = v_agn_sht_desc,
--                   agn_name = v_agn_name,
--                   agn_physical_address = v_agn_physical_address,
--                   agn_postal_address = v_agn_postal_address,
--                   agn_twn_code = v_agn_twn_code,
--                   agn_cou_code = v_agn_cou_code,
--                   agn_email_address = v_agn_email_address,
--                   agn_web_address = v_agn_web_address,
--                   agn_zip = v_agn_zip,
--                   agn_contact_person = v_agn_contact_person,
--                   agn_contact_title = v_agn_contact_title,
--                   agn_tel1 = v_agn_tel1,
--                   agn_tel2 = v_agn_tel2,
--                   agn_fax = v_agn_fax,
--                   agn_acc_no = v_agn_acc_no,
--                   agn_pin = v_agn_pin,
--                   agn_agent_commission = v_agn_agent_commission,
--                   agn_credit_allowed = v_agn_credit_allowed,
--                   agn_agent_wht_tax = v_agn_agent_wht_tax,
--                   agn_print_dbnote = v_agn_print_dbnote,
--                   agn_status = v_agn_status,
--                   --  agn_date_created = v_agn_date_created,
--                   ---   agn_created_by = v_agn_created_by,
--                   agn_reg_code = v_agn_reg_code,
--                   agn_comm_reserve_rate = v_agn_comm_reserve_rate,
--                   agn_annual_budget = v_agn_annual_budget,
--                   agn_status_eff_date = v_agn_status_eff_date,
--                   agn_credit_period = v_agn_credit_period,
--                   agn_comm_stat_eff_dt = v_agn_comm_stat_eff_dt,
--                   agn_comm_status_dt = v_agn_comm_status_dt,
--                   agn_comm_allowed = v_agn_comm_allowed,
--                   agn_checked = v_agn_checked,
--                   agn_checked_by = v_agn_checked_by,
--                   agn_check_date = v_agn_check_date,
--                   agn_comp_comm_arrears = v_agn_comp_comm_arrears,
--                   agn_reinsurer = v_agn_reinsurer,
--                   agn_brn_code = v_agn_brn_code,
--                   agn_town = v_agn_town,
--                   agn_country = v_agn_country,
--                   agn_status_desc = v_agn_status_desc,
--                   agn_id_no = v_agn_id_no,
--                   agn_con_code = v_agn_con_code,
--                   agn_agn_code = v_agn_agn_code,
--                   agn_sms_tel = v_decodesmstel,
--                   agn_ahc_code = v_agn_ahc_code,
--                   agn_sec_code = v_agn_sec_code,
--                   agn_agnc_class_code = v_agn_agnc_class_code,
--                   agn_expiry_date = v_agn_expiry_date,
--                   agn_license_no = v_agn_license_no,
--                   agn_runoff = v_agn_runoff,
--                   agn_licensed = v_agn_licensed,
--                   agn_license_grace_pr = v_agn_license_grace_pr,
--                   agn_old_acc_no = v_agn_old_acc_no,
--                   agn_status_remarks = v_agn_status_remarks,
--                   agn_bbr_code = v_agn_bbr_code,
--                   agn_bank_acc_no = v_agn_bank_acc_no,
--                   agn_unique_prefix = v_agn_unique_prefix,
--                   agn_state_code = v_agn_state_code,
--                   agn_crdt_rting = v_agn_crdt_rting,
--                   agn_subagent = v_agn_subagent,
--                   agn_main_agn_code = v_agn_main_agn_code,
--                   agn_clnt_code = v_agn_clnt_code,
--                   agn_account_manager = v_agn_account_manager,
--                   agn_credit_limit = v_agn_credit_limit,
--                   agn_bru_code = v_agn_bru_code,
--                   agn_local_international = v_agn_local_international,
--                   agn_regulator_number = v_agn_regulator_number,
--                   agn_rorg_code = v_agn_rorg_code,
--                   agn_ors_code = v_agn_ors_code,
--                   agn_bounced_chq = v_agn_bounced_chq,
--                   agn_bpn_code = v_agn_bpn_code,
--                   agn_agent_type = v_agn_agent_type,
--                   agn_group = v_agn_group,
--                   agn_default_comm_mode = v_def_comm_mode,
--                   agn_allocate_cert = v_agn_allocate_cert,
--                   agn_vat_applicable = v_vat_appl,
--                   agn_whtax_applicable = v_withtaxappl,
--                   agn_tel_pay = v_agn_tel_pay,
--                   agn_payment_freq = v_freq_payment,
--                   agn_default_cpm_mode = v_pymt_mode,
--                   agn_pymt_validated = v_pymt_validated,
--                   agn_benefit_start_date = v_agn_benefit_start_date,
--                   agn_birth_date = v_agn_dob,
--                   agn_qualification = v_agn_qualification,
--                   agn_marital_status = v_agn_marital_status,
--                   agn_id_no_doc_used = v_id_no_doc_used,
--                   agn_sbu_code = v_agn_sbu_code,
--                   agn_comm_levy_app = v_agn_comm_levy_app,
--                   agn_comm_levy_rate = v_agn_comm_levy_rate,
--                   agn_brr_code = v_agn_brr_code,
--                   agn_brr_name = v_agn_brr_name
--             WHERE agn_code = v_agn_code;
--
--            tq_lms.lms_web_pkg_setup.updateagenies (v_agn_code,
--                                                    v_agn_short_desc,
--                                                    v_agn_name,
--                                                    v_agn_id_no,
--                                                    v_agn_sms_tel,
--                                                    v_agn_comm_allowed,
--                                                    NULL,   --  V_CONT_STATUS,
--                                                    NULL,       --  V_CERT_NO,
--                                                    NULL,     --  V_CONT_DATE,
--                                                    NULL,   --  V_DATE_JOINED,
--                                                    NULL, --  V_DATE_TERMINATED,
--                                                    NULL,          --  V_BANK,
--                                                    v_agn_bank_acc_no,
--                                                    v_agn_sms_tel,
--                                                    v_agn_email_address,
--                                                    v_agn_reinsurer,
--                                                    NULL,           --V_GL_ACC
--                                                    v_agn_credit_allowed,
--                                                    NULL,       --v_agn_wxtax,
--                                                    NULL,      --v_agn_dbnote,
--                                                    v_agn_status,
--                                                    NULL,  --v_agn_reg_status,
--                                                    NULL,  --v_agn_reg_number,
--                                                    NULL, --v_agn_comm_payroll,
--                                                    NULL,    --v_agn_pay_mthd,
--                                                    v_agn_act_code,
--                                                    v_agn_pin,
--                                                    v_agn_bru_code,
--                                                    v_agn_brn_code,
--                                                    NULL,      --v_agn_ref_no,
--                                                    NULL,  --v_agn_post_level,
--                                                    v_err_msg,
--                                                    NULL,  --v_agn_sub_status,
--                                                    NULL, --v_agn_sub_status_date,
--                                                    v_agn_bbr_code,
--                                                    NULL, --v_agn_cap_exempted,
--                                                    NULL, --v_agn_phy_address,
--                                                    v_agn_dob,
--                                                    NULL, --v_agn_retainer_amnt,
--                                                    NULL     --v_agn_sact_code
--                                                        );
--         EXCEPTION
--            WHEN OTHERS
--            THEN
--               raise_error (
--                  'Error occured while updating record ' || SQLERRM (SQLCODE));
--         END;
--
--         SELECT COUNT (*)
--           INTO v_accCount
--           FROM TQC_ACCOUNT_CONTACTS
--          WHERE ACCC_ACC_CODE = v_agn_code;
--
--         IF NVL (v_accCount, 0) = 0
--         THEN
--            BEGIN
--               INSERT INTO tqc_account_contacts (accc_code,
--                                                 accc_acc_code,
--                                                 accc_name,
--                                                 accc_other_names,
--                                                 accc_tel,
--                                                 accc_email_addr,
--                                                 accc_sms_tel,
--                                                 accc_username,
--                                                 accc_pwd,
--                                                 accc_login_allowed,
--                                                 accc_pwd_changed,
--                                                 accc_pwd_reset,
--                                                 accc_dt_created,
--                                                 accc_status,
--                                                 accc_login_attempts,
--                                                 accc_personel_rank,
--                                                 accc_last_login_date,
--                                                 accc_created_by)
--                    VALUES (tqc_accc_code_seq.NEXTVAL,
--                            v_agn_code,
--                            v_agn_sht_desc,
--                            v_agn_name,
--                            v_agn_tel1,
--                            v_agn_email_address,
--                            v_decodesmstel,
--                            v_agn_sht_desc,
--                            /*v_account_contacts_tab(I).ACCC_PWD*/
--                            GIS_READ.VAL ('123456'),
--                            'Y',
--                            SYSDATE,
--                            'Y',
--                            SYSDATE,
--                            'A',
--                            0,
--                            'AGENCY',
--                            SYSDATE,
--                            'SYSTEM');
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  NULL;
--            END;
--         END IF;
--      ELSIF v_add_edit = 'D'
--      THEN
--         BEGIN
--            DELETE FROM tqc_agencies
--                  WHERE agn_code = v_agn_code;
--         EXCEPTION
--            WHEN OTHERS
--            THEN
--               raise_error (
--                  'Error occured while deleting record ' || SQLERRM (SQLCODE));
--         END;
--      END IF;
--   END;

 PROCEDURE agencies_prc (
      v_add_edit                  IN       VARCHAR2,
      v_agn_code                  IN       tqc_agencies.agn_code%TYPE,
      v_agn_act_code              IN       tqc_agencies.agn_act_code%TYPE,
      v_agn_sht_desc              IN OUT   tqc_agencies.agn_sht_desc%TYPE,
      v_agn_name                  IN       tqc_agencies.agn_name%TYPE,
      v_agn_physical_address      IN       tqc_agencies.agn_physical_address%TYPE,
      v_agn_postal_address        IN       tqc_agencies.agn_postal_address%TYPE,
      v_agn_twn_code              IN       tqc_agencies.agn_twn_code%TYPE,
      v_agn_cou_code              IN       tqc_agencies.agn_cou_code%TYPE,
      v_agn_email_address         IN       tqc_agencies.agn_email_address%TYPE,
      v_agn_web_address           IN       tqc_agencies.agn_web_address%TYPE,
      v_agn_zip                   IN       tqc_agencies.agn_zip%TYPE,
      v_agn_contact_person        IN       tqc_agencies.agn_contact_person%TYPE,
      v_agn_contact_title         IN       tqc_agencies.agn_contact_title%TYPE,
      v_agn_tel1                  IN       tqc_agencies.agn_tel1%TYPE,
      v_agn_tel2                  IN       tqc_agencies.agn_tel2%TYPE,
      v_agn_fax                   IN       tqc_agencies.agn_fax%TYPE,
      v_agn_acc_no                IN       tqc_agencies.agn_acc_no%TYPE,
      v_agn_pin                   IN       tqc_agencies.agn_pin%TYPE,
      v_agn_agent_commission      IN       tqc_agencies.agn_agent_commission%TYPE,
      v_agn_credit_allowed        IN       tqc_agencies.agn_credit_allowed%TYPE,
      v_agn_agent_wht_tax         IN       tqc_agencies.agn_agent_wht_tax%TYPE,
      v_agn_print_dbnote          IN       tqc_agencies.agn_print_dbnote%TYPE,
      v_agn_status                IN       tqc_agencies.agn_status%TYPE,
      v_agn_date_created          IN       tqc_agencies.agn_date_created%TYPE,
      v_posted_by                 IN       VARCHAR2,
      v_agn_reg_code              IN       tqc_agencies.agn_reg_code%TYPE,
      v_agn_comm_reserve_rate     IN       tqc_agencies.agn_comm_reserve_rate%TYPE,
      v_agn_annual_budget         IN       tqc_agencies.agn_annual_budget%TYPE,
      v_agn_status_eff_date       IN       tqc_agencies.agn_status_eff_date%TYPE,
      v_agn_credit_period         IN       tqc_agencies.agn_credit_period%TYPE,
      v_agn_comm_stat_eff_dt      IN       tqc_agencies.agn_comm_stat_eff_dt%TYPE,
      v_agn_comm_status_dt        IN       tqc_agencies.agn_comm_status_dt%TYPE,
      v_agn_comm_allowed          IN       tqc_agencies.agn_comm_allowed%TYPE,
      v_agn_checked               IN       tqc_agencies.agn_checked%TYPE,
      v_agn_checked_by            IN       tqc_agencies.agn_checked_by%TYPE,
      v_agn_check_date            IN       tqc_agencies.agn_check_date%TYPE,
      v_agn_comp_comm_arrears     IN       tqc_agencies.agn_comp_comm_arrears%TYPE,
      v_agn_reinsurer             IN       tqc_agencies.agn_reinsurer%TYPE,
      v_agn_brn_code              IN       tqc_agencies.agn_brn_code%TYPE,
      v_agn_town                  IN       tqc_agencies.agn_town%TYPE,
      v_agn_country               IN       tqc_agencies.agn_country%TYPE,
      v_agn_status_desc           IN       tqc_agencies.agn_status_desc%TYPE,
      v_agn_id_no                 IN       tqc_agencies.agn_id_no%TYPE,
      v_agn_con_code              IN       tqc_agencies.agn_con_code%TYPE,
      v_agn_agn_code              IN       tqc_agencies.agn_agn_code%TYPE,
      v_agn_sms_tel               IN       tqc_agencies.agn_sms_tel%TYPE,
      v_agn_ahc_code              IN       tqc_agencies.agn_ahc_code%TYPE,
      v_agn_sec_code              IN       tqc_agencies.agn_sec_code%TYPE,
      v_agn_agnc_class_code       IN       tqc_agencies.agn_agnc_class_code%TYPE,
      v_agn_expiry_date           IN       tqc_agencies.agn_expiry_date%TYPE,
      v_agn_license_no            IN       tqc_agencies.agn_license_no%TYPE,
      v_agn_runoff                IN       tqc_agencies.agn_runoff%TYPE,
      v_agn_licensed              IN       tqc_agencies.agn_licensed%TYPE,
      v_agn_license_grace_pr      IN       tqc_agencies.agn_license_grace_pr%TYPE,
      v_agn_old_acc_no            IN       tqc_agencies.agn_old_acc_no%TYPE,
      v_agn_status_remarks        IN       tqc_agencies.agn_status_remarks%TYPE,
      v_agn_bbr_code              IN       tqc_agencies.agn_bbr_code%TYPE
            DEFAULT NULL,
      v_agn_bank_acc_no           IN       tqc_agencies.agn_bank_acc_no%TYPE
            DEFAULT NULL,
      v_agn_unique_prefix         IN       tqc_agencies.agn_unique_prefix%TYPE
            DEFAULT NULL,
      v_agn_state_code            IN       tqc_agencies.agn_state_code%TYPE
            DEFAULT NULL,
      v_agn_crdt_rting            IN       tqc_agencies.agn_crdt_rting%TYPE
            DEFAULT NULL,
      v_agn_subagent              IN       tqc_agencies.agn_subagent%TYPE
            DEFAULT NULL,
      v_agn_main_agn_code         IN       tqc_agencies.agn_main_agn_code%TYPE
            DEFAULT NULL,
      v_agn_clnt_code             IN       tqc_agencies.agn_clnt_code%TYPE
            DEFAULT NULL,
      v_agn_account_manager       IN       tqc_agencies.agn_account_manager%TYPE
            DEFAULT NULL,
      v_agn_credit_limit          IN       tqc_agencies.agn_credit_limit%TYPE
            DEFAULT NULL,
      v_agn_bru_code              IN       tqc_agencies.agn_bru_code%TYPE
            DEFAULT NULL,
      v_agn_local_international   IN       tqc_agencies.agn_local_international%TYPE
            DEFAULT NULL,
      v_agn_regulator_number      IN       tqc_agencies.agn_regulator_number%TYPE
            DEFAULT NULL,
      v_agn_rorg_code             IN       tqc_agencies.agn_rorg_code%TYPE
            DEFAULT NULL,
      v_agn_ors_code              IN       tqc_agencies.agn_ors_code%TYPE
            DEFAULT NULL,
      v_agn_allocate_cert         IN       tqc_agencies.agn_allocate_cert%TYPE
            DEFAULT NULL,
      v_agn_bounced_chq           IN       tqc_agencies.agn_bounced_chq%TYPE
            DEFAULT NULL,
      v_def_comm_mode             IN       tqc_agencies.agn_default_comm_mode%TYPE
            DEFAULT NULL,
      v_agn_bpn_code              IN       tqc_agencies.agn_bpn_code%TYPE
            DEFAULT NULL,
      v_agn_agent_type            IN       tqc_agencies.agn_agent_type%TYPE
            DEFAULT NULL,
      v_agn_group                 IN       tqc_agencies.agn_group%TYPE
            DEFAULT NULL,
      v_vat_appl                  IN       tqc_agencies.agn_vat_applicable%TYPE,
      v_withtaxappl               IN       tqc_agencies.agn_whtax_applicable%TYPE,
      v_agn_tel_pay               IN       tqc_agencies.agn_tel_pay%TYPE,
      v_agnresetcode              IN       VARCHAR2 DEFAULT NULL,
      v_freq_payment              IN       VARCHAR2 DEFAULT NULL,
      v_pymt_mode                 IN       VARCHAR2 DEFAULT NULL,
      v_pymt_validated            IN       VARCHAR2 DEFAULT NULL,
      v_agn_benefit_start_date    IN       DATE DEFAULT NULL,
      v_agn_dob                   IN       DATE DEFAULT NULL,
      v_agn_qualification         IN       VARCHAR2 DEFAULT NULL,
      v_agn_marital_status        IN       VARCHAR2 DEFAULT NULL,
      v_id_no_doc_used            IN       VARCHAR2 DEFAULT NULL,
      v_agn_sbu_code              IN       VARCHAR2 DEFAULT NULL,
      v_agn_comm_levy_app         IN       VARCHAR2 DEFAULT NULL,
      v_agn_comm_levy_rate        IN       NUMBER DEFAULT NULL,
      v_agn_brr_code              IN       NUMBER DEFAULT NULL,
      v_agn_brr_name              IN       VARCHAR2 DEFAULT NULL,
      v_agn_auth_name             IN       VARCHAR2 DEFAULT NULL,
      v_agn_gender                IN       tqc_agencies.agn_gender%TYPE,
      v_agn_iprs_validated        IN       VARCHAR2 DEFAULT NULL
--      ,v_client_country_name       IN       VARCHAR2 DEFAULT NULL
   )
   IS
      v_direct_srl_fmt     VARCHAR2 (50);
      v_agent_id           VARCHAR2 (50);
      v_agent_seq          NUMBER;
      v_name_first_char    VARCHAR2 (20);
      v_no_of_chars        NUMBER                           := 1;
      v_serial_length      NUMBER                           := 6;
      v_act_type           VARCHAR2 (2);
      v_count              NUMBER                           := 0;
      v_created_by         VARCHAR2 (20);
      v_date_created       DATE;
      v_agn_short_desc     VARCHAR2 (200);
      v_act_desc           VARCHAR2 (75);
      v_act_wthtx_rate     NUMBER;
      v_brn_sht_desc       tqc_branches.brn_sht_desc%TYPE;
      v_param_value        VARCHAR2 (30);
      v_decodesmstel       VARCHAR2 (50);
      v_rorg_code          NUMBER;
      v_ors_code           NUMBER;
      v_acccount           NUMBER;
      v_sms_tel_count      NUMBER;
      v_agn_authorised     VARCHAR2 (50);
      v_old_agn_sht_desc   VARCHAR2 (50);
      v_new_ent_code       NUMBER;
      v_ent_code           NUMBER;
      v_pin_unique         VARCHAR2 (50)
             := tqc_parameters_pkg.get_param_varchar ('AGENT_PIN_UNIQUE_APP');
             v_id_unique VARCHAR2 (200):= tqc_parameters_pkg.get_param_varchar ('AGENT_IDNO_UNIQUE_APP');
      v_err_msg            VARCHAR2 (200);
      v_new_pwd            VARCHAR2 (200);
      v_agn_accc_no        VARCHAR (10);
      v_agn_ac_temp        VARCHAR (10);
      v_agn_accc_no_f      VARCHAR (10);
      v_act_suffix         VARCHAR (10);
----------------
   BEGIN
--   raise_error('v_client_country_name: '||v_client_country_name);
      IF v_agn_pin IS NOT NULL AND NVL (v_pin_unique, 'N') = 'Y'
      THEN
         BEGIN
            SELECT COUNT (agn_pin)
              INTO v_count
              FROM tqc_agencies
             WHERE agn_pin = v_agn_pin
               AND (agn_code <> v_agn_code OR v_agn_code IS NULL)
               AND agn_status='ACTIVE';

            IF v_count > 0
            THEN
               raise_error (   'Pin Number: '
                            || v_agn_pin
                            || ' Already Exists !....'
                           );
            END IF;
         END;
      END IF;
      
      
       IF v_agn_id_no IS NOT NULL AND NVL (v_id_unique, 'N') = 'Y'
      THEN
         BEGIN
            SELECT COUNT (agn_id_no)
              INTO v_count
              FROM tqc_agencies
             WHERE agn_id_no = v_agn_id_no
               AND (agn_code <> v_agn_code OR v_agn_code IS NULL);

            IF v_count > 0
            THEN
               raise_error (   'Id Number: '
                            || v_agn_id_no
                            || ' Already Exists !....'
                           );
            END IF;
         END;
      END IF;

      IF v_agn_sms_tel IS NOT NULL
      THEN
         SELECT INSTR (v_agn_sms_tel, '+')
           INTO v_sms_tel_count
           FROM DUAL;

         IF v_sms_tel_count = 0
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
         ELSE
            v_decodesmstel := v_agn_sms_tel;
         END IF;
      END IF;

      BEGIN
         SELECT RTRIM (LTRIM (act_id_serial_format)), act_type_sht_desc,
                act_account_type, act_wthtx_rate, act_suffix,
                NVL (act_next_no, 0)
           INTO v_direct_srl_fmt, v_act_type,
                v_act_desc, v_act_wthtx_rate, v_act_suffix,
                v_agent_seq
           FROM tqc_account_types
          WHERE act_code = v_agn_act_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (   'Error getting Id format for account type '
                         || v_agn_act_code
                        );
      END;

      IF v_add_edit = 'A'
      THEN
         IF v_agn_name IS NULL
         THEN
            raise_error ('Agent Name cannot be null');
         END IF;
         
         if v_agn_brn_code is null
         then 
            raise_error('Agent Branch is required to proceed.');
         end if;
         
--         if v_agn_agent_type is null 
--         then 
--            RAISE_ERROR('Please ensure you have selected Agent Type.');
--         end if;

         v_created_by := v_posted_by;
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
               -- for shared suffix no
               IF v_act_type IN ('A', 'IA')
               THEN
                  SELECT agent_id_a_seq.NEXTVAL
                    INTO v_agent_seq
                    FROM DUAL;
               ELSIF v_act_type IN ('I', 'FO')
               THEN
                  SELECT agent_id_i_seq.NEXTVAL
                    INTO v_agent_seq
                    FROM DUAL;
               END IF;

               BEGIN
                  SELECT brn_sht_desc
                    INTO v_brn_sht_desc
                    FROM tqc_branches
                   WHERE brn_code = v_agn_brn_code;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('Unable to fetch branch!');
               END;

               v_agent_id :=
                  REPLACE (v_direct_srl_fmt,
                           '[SERIALNO]',
                           LPAD (v_agent_seq, v_serial_length, '0')
                          );
               v_agent_id :=
                           REPLACE (v_agent_id, '[FNAMEI]', v_name_first_char);
               v_agent_id := REPLACE (v_agent_id, '[BRN]', v_brn_sht_desc);
               v_agent_id := REPLACE (v_agent_id, '[SFX]', v_act_suffix);

               UPDATE tqc_account_types
                  SET act_next_no = NVL (act_next_no, 0) + 1
                WHERE act_code = v_agn_act_code;

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
                  ROLLBACK;
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

            BEGIN
               SELECT ent_code
                 INTO v_ent_code
                 FROM tqc_entities
                WHERE ent_pin = v_agn_pin;
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  v_ent_code := 0;
            END;

            IF v_ent_code = 0
            THEN
               tqc_entities_pkg.entities_prc ('A',
                                              v_new_ent_code,
                                              v_agn_sht_desc,
                                              v_agn_name,
                                              v_agn_pin,
                                              v_agn_physical_address,
                                              v_agn_postal_address,
                                              v_agn_tel1,
                                              v_agn_email_address
                                             );
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
                         agn_vat_applicable, agn_whtax_applicable,
                         agn_tel_pay, agn_payment_freq, agn_default_cpm_mode,
                         agn_pymt_validated, agn_benefit_start_date,
                         agn_birth_date, agn_qualification,
                         agn_marital_status, agn_id_no_doc_used,
                         agn_sbu_code, agn_comm_levy_app,
                         agn_comm_levy_rate, agn_brr_code,
                         agn_brr_name, agn_auth_name, agn_updated_by,
                         agn_updated_on, agn_ent_code, agn_gender,agn_iprs_validated
                        --, agn_cou_code --commented out because already exists
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
                         v_agn_status, v_agn_date_created, v_posted_by,
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
                         v_vat_appl, v_withtaxappl,
                         v_agn_tel_pay, v_freq_payment, v_pymt_mode,
                         v_pymt_validated, v_agn_benefit_start_date,
                         v_agn_dob, v_agn_qualification,
                         v_agn_marital_status, v_id_no_doc_used,
                         v_agn_sbu_code, v_agn_comm_levy_app,
                         v_agn_comm_levy_rate, v_agn_brr_code,
                         v_agn_brr_name, v_agn_auth_name, NULL,
                         NULL, v_new_ent_code, v_agn_gender,v_agn_iprs_validated
                        --, v_client_country_name
                        );

            tqc_setups_pkg.agencies_audit_prc (v_agn_code, v_posted_by);

            IF v_new_ent_code IS NOT NULL
            THEN
               BEGIN
                  tqc_entities_pkg.entities_prc ('E',
                                                 v_new_ent_code,
                                                 v_agn_sht_desc,
                                                 v_agn_name,
                                                 v_agn_pin,
                                                 v_agn_physical_address,
                                                 v_agn_postal_address,
                                                 v_agn_tel1,
                                                 v_agn_email_address
                                                );
               END;
            ELSE
               tqc_entities_pkg.entities_prc ('A',
                                              v_new_ent_code,
                                              v_agn_sht_desc,
                                              v_agn_name,
                                              v_agn_pin,
                                              v_agn_physical_address,
                                              v_agn_postal_address,
                                              v_agn_tel1,
                                              v_agn_email_address
                                             );
            END IF;

--             tq_lms.lms_web_pkg_setup.updateagenies (
--                        v_agn_code,
--                        v_agn_short_desc,
--                        v_agn_name,
--                        v_agn_id_no,
--                        v_agn_sms_tel,
--                        v_agn_comm_allowed,
--                        NULL,--  V_CONT_STATUS,
--                        NULL,--  V_CERT_NO,
--                        NULL,--  V_CONT_DATE,
--                        NULL,--  V_DATE_JOINED,
--                        NULL,--  V_DATE_TERMINATED,
--                        NULL,--  V_BANK,
--                        v_agn_bank_acc_no,
--                        v_agn_sms_tel,
--                        v_agn_email_address,
--                        v_agn_reinsurer,
--                        NULL, --V_GL_ACC
--                        v_agn_credit_allowed,
--                        NULL,--v_agn_wxtax,
--                        NULL,--v_agn_dbnote,
--                        v_agn_status,
--                        NULL,--v_agn_reg_status,
--                        NULL,--v_agn_reg_number,
--                        NULL,--v_agn_comm_payroll,
--                        NULL,--v_agn_pay_mthd,
--                        v_agn_act_code,
--                        v_agn_pin,
--                        v_agn_bru_code,
--                        v_agn_brn_code,
--                        NULL,--v_agn_ref_no,
--                        NULL,--v_agn_post_level,
--                        v_err_msg,
--                        NULL,--v_agn_sub_status,
--                        NULL,--v_agn_sub_status_date,
--                        v_agn_bbr_code,
--                        NULL,--v_agn_cap_exempted,
--                        NULL,--v_agn_phy_address,
--                        v_agn_dob,
--                        NULL,--v_agn_retainer_amnt,
--                        NULL,--v_agn_sact_code
--                        NULL,--v_agn_score
--                        NULL,--v_agn_train_stat
--                        NULL,--v_agn_benefit_start_date
--                        NULL,--v_agn_earns_retainer
--                        NULL,--v_ref_agn_code
--                        NULL,--v_agn_confirm_date
--                        NULL,--v_agn_id_no_doc_used
--                        NULL,--v_agn_employer_bbr_code
--                        NULL,--v_agn_pay_third_party
--                        NULL,--v_agn_third_party_type
--                        NULL,--v_agn_office_address
--                        NULL,--v_agn_mobility
--                        NULL,--v_agn_local_resident
--                        NULL--v_agn_co_code
--                );
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
               BEGIN
                  SELECT DBMS_RANDOM.STRING ('X', 10)
                    INTO v_new_pwd
                    FROM DUAL;
               END;

               INSERT INTO tqc_account_contacts
                           (accc_code,
                            accc_acc_code, accc_name,
                            accc_other_names, accc_tel, accc_email_addr,
                            accc_sms_tel, accc_username,
                            accc_pwd, accc_login_allowed, accc_pwd_changed,
                            accc_pwd_reset, accc_dt_created, accc_status,
                            accc_login_attempts, accc_personel_rank,
                            accc_last_login_date, accc_created_by,
                            accc_sys_code, accc_reset_code, accc_agn_code
                           )
                    VALUES (tqc_accc_code_seq.NEXTVAL,
                            tqc_agn_code_seq.CURRVAL, v_agn_short_desc,
                            v_agn_name, v_agn_tel1, v_agn_email_address,
                            v_decodesmstel, v_agn_short_desc,
                            /*v_account_contacts_tab(I).ACCC_PWD*/
                            gis_read.val (v_new_pwd), 'Y', SYSDATE,
                            'Y', SYSDATE, 'A',
                            0, 'AGENCY',
                            SYSDATE, 'SYSTEM',
                            NULL, v_agnresetcode, tqc_agn_code_seq.CURRVAL
                           );

               tqc_web_alerts.new_account_info_alert (v_agn_code,
                                                      v_new_pwd,
                                                      v_posted_by
                                                     );
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
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_business_persons
             WHERE bpn_clnt_code = v_agn_code;

            -- raise_error(v_count||':'||v_agn_pin);
            IF NVL (v_count, 0) > 0 AND LENGTH (v_agn_pin) = 0
            THEN
               raise_error ('Agent Pin Is Mandatory When Payee Is Defined!');
            END IF;

            SELECT agn_authorised, agn_sht_desc
              INTO v_agn_authorised, v_old_agn_sht_desc
              FROM tqc_agencies
             WHERE agn_code = v_agn_code;

            IF v_agn_authorised = 'Y' AND v_old_agn_sht_desc <> v_agn_sht_desc
            THEN
               raise_error
                  (   'AGENT SHORT DESCRIPTION SHOULD NOT BE EDITED FOR AN AUTHORIZED CLIENT REVERT TO ....'
                   || v_old_agn_sht_desc
                  );
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

            --- raise_error('v_agn_ahc_code: '||v_agn_ahc_code);
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
                   agn_payment_freq = v_freq_payment,
                   agn_default_cpm_mode = v_pymt_mode,
                   agn_pymt_validated = v_pymt_validated,
                   agn_benefit_start_date = v_agn_benefit_start_date,
                   agn_birth_date = v_agn_dob,
                   agn_qualification = v_agn_qualification,
                   agn_marital_status = v_agn_marital_status,
                   agn_id_no_doc_used = v_id_no_doc_used,
                   agn_sbu_code = v_agn_sbu_code,
                   agn_comm_levy_app = v_agn_comm_levy_app,
                   agn_comm_levy_rate = v_agn_comm_levy_rate,
                   agn_brr_code = v_agn_brr_code,
                   agn_brr_name = v_agn_brr_name,
                   agn_auth_name = v_agn_auth_name,
                   agn_updated_by = v_posted_by,
                   agn_updated_on = SYSDATE,
                   agn_gender = v_agn_gender,
                   agn_iprs_validated = v_agn_iprs_validated
             WHERE agn_code = v_agn_code;

            tqc_setups_pkg.agencies_audit_prc (v_agn_code, v_posted_by);
--             tq_lms.lms_web_pkg_setup.updateagenies (
--                        v_agn_code,
--                        v_agn_short_desc,
--                        v_agn_name,
--                        v_agn_id_no,
--                        v_agn_sms_tel,
--                        v_agn_comm_allowed,
--                        NULL,--  V_CONT_STATUS,
--                        NULL,--  V_CERT_NO,
--                        NULL,--  V_CONT_DATE,
--                        NULL,--  V_DATE_JOINED,
--                        NULL,--  V_DATE_TERMINATED,
--                        NULL,--  V_BANK,
--                        v_agn_bank_acc_no,
--                        v_agn_sms_tel,
--                        v_agn_email_address,
--                        v_agn_reinsurer,
--                        NULL, --V_GL_ACC
--                        v_agn_credit_allowed,
--                        NULL,--v_agn_wxtax,
--                        NULL,--v_agn_dbnote,
--                        v_agn_status,
--                        NULL,--v_agn_reg_status,
--                        NULL,--v_agn_reg_number,
--                        NULL,--v_agn_comm_payroll,
--                        NULL,--v_agn_pay_mthd,
--                        v_agn_act_code,
--                        v_agn_pin,
--                        v_agn_bru_code,
--                        v_agn_brn_code,
--                        NULL,--v_agn_ref_no,
--                        NULL,--v_agn_post_level,
--                        v_err_msg,
--                        NULL,--v_agn_sub_status,
--                        NULL,--v_agn_sub_status_date,
--                        v_agn_bbr_code,
--                        NULL,--v_agn_cap_exempted,
--                        NULL,--v_agn_phy_address,
--                        v_agn_dob,
--                        NULL,--v_agn_retainer_amnt,
--                        NULL,--v_agn_sact_code
--                        NULL,--v_agn_score
--                        NULL,--v_agn_train_stat
--                        NULL,--v_agn_benefit_start_date
--                        NULL,--v_agn_earns_retainer
--                        NULL,--v_ref_agn_code
--                        NULL,--v_agn_confirm_date
--                        NULL,--v_agn_id_no_doc_used
--                        NULL,--v_agn_employer_bbr_code
--                        NULL,--v_agn_pay_third_party
--                        NULL,--v_agn_third_party_type
--                        NULL,--v_agn_office_address
--                        NULL,--v_agn_mobility
--                        NULL,--v_agn_local_resident
--                        NULL--v_agn_co_code
--                );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while updating record '
                            || SQLERRM (SQLCODE)
                           );
         END;

         SELECT COUNT (*)
           INTO v_acccount
           FROM tqc_account_contacts
          WHERE accc_acc_code = v_agn_code;

         IF NVL (v_acccount, 0) = 0
         THEN
            BEGIN
               BEGIN
                  SELECT DBMS_RANDOM.STRING ('X', 10)
                    INTO v_new_pwd
                    FROM DUAL;
               END;

               INSERT INTO tqc_account_contacts
                           (accc_code, accc_acc_code,
                            accc_name, accc_other_names, accc_tel,
                            accc_email_addr, accc_sms_tel,
                            accc_username, accc_pwd, accc_login_allowed,
                            accc_pwd_changed, accc_pwd_reset,
                            accc_dt_created, accc_status,
                            accc_login_attempts, accc_personel_rank,
                            accc_last_login_date, accc_created_by
                           )
                    VALUES (tqc_accc_code_seq.NEXTVAL, v_agn_code,
                            v_agn_sht_desc, v_agn_name, v_agn_tel1,
                            v_agn_email_address, v_decodesmstel,
                            v_agn_sht_desc,
                                           /*v_account_contacts_tab(I).ACCC_PWD*/
                                           gis_read.val (v_new_pwd), 'Y',
                            SYSDATE, 'Y',
                            SYSDATE, 'A',
                            0, 'AGENCY',
                            SYSDATE, 'SYSTEM'
                           );

               tqc_web_alerts.new_account_info_alert (v_agn_code,
                                                      v_new_pwd,
                                                      v_posted_by
                                                     );
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
   END agencies_prc;
 
 PROCEDURE payment_modes_prc (
      v_add_edit        IN   VARCHAR2,
      v_pmod_code       IN   tqc_payment_modes.pmod_code%TYPE,
      v_pmod_sht_desc   IN   tqc_payment_modes.pmod_sht_desc%TYPE,
      v_pmod_desc       IN   tqc_payment_modes.pmod_desc%TYPE,
      v_pmod_naration   IN   tqc_payment_modes.pmod_naration%TYPE,
      v_pmod_default    IN   tqc_payment_modes.pmod_default%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
       IF v_pmod_default = 'Y'
      THEN
            BEGIN
                SELECT COUNT (*)
                      INTO v_count
                      FROM tqc_payment_modes
                     WHERE pmod_default = v_pmod_default;

                 IF v_count >= 1
                 THEN
                   raise_error('Default Payment Mode has already been Set');
                 END IF;
           END;
     END IF;
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_payment_modes
             WHERE pmod_code = v_pmod_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_payment_modes (pmod_code,
                                           pmod_sht_desc,
                                           pmod_desc,
                                           pmod_naration,
                                           pmod_default)
                 VALUES (tqc_pmod_code_seq.NEXTVAL,
                         v_pmod_sht_desc,
                         v_pmod_desc,
                         v_pmod_naration,
                         v_pmod_default);
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
            UPDATE tqc_payment_modes
               SET pmod_sht_desc = v_pmod_sht_desc,
                   pmod_desc = v_pmod_desc,
                   pmod_naration = v_pmod_naration,
                   pmod_default = v_pmod_default
             WHERE pmod_code = v_pmod_code;
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
            DELETE FROM tqc_payment_modes
                  WHERE pmod_code = v_pmod_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE client_titles_prc (
      v_add_edit       IN   VARCHAR2,
      v_clt_code       IN   tqc_client_titles.clt_code%TYPE,
      v_clt_sht_desc   IN   tqc_client_titles.clt_sht_desc%TYPE,
      v_clnt_desc      IN   tqc_client_titles.clnt_desc%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_client_titles
             WHERE clt_sht_desc = v_clt_sht_desc;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_client_titles
                        (clt_code, clt_sht_desc, clnt_desc
                        )
                 VALUES (tqc_clt_code_seq.NEXTVAL, v_clt_sht_desc, v_clnt_desc
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
            UPDATE tqc_client_titles
               SET clt_sht_desc = v_clt_sht_desc,
                   clnt_desc = v_clnt_desc
             WHERE clt_code = v_clt_code;
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
            DELETE FROM tqc_client_titles
                  WHERE clt_code = v_clt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE grant_prov_system (
      v_prov_code    IN   tqc_service_providers.spr_code%TYPE,
      v_sys_code     IN   tqc_systems.sys_code%TYPE,
      v_created_by   IN   VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_service_provider_systems
       WHERE sps_sys_code = v_sys_code
         AND sps_spr_code = v_prov_code
         AND (sps_wet IS NULL OR sps_wet > SYSDATE);

      IF (NVL (v_count, 0) = 0)
      THEN
         INSERT INTO tqc_service_provider_systems (sps_code,
                                                   sps_spr_code,
                                                   sps_sys_code,
                                                   sps_wef,
                                                   sps_wet,
                                                   sps_created_by)
              VALUES (tqc_sps_code_seq.NEXTVAL,
                      v_prov_code,
                      v_sys_code,
                      SYSDATE,
                      NULL,
                      v_created_by);
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error occured while creating record '
                      || SQLERRM (SQLCODE)
                     );
   END grant_prov_system;

   PROCEDURE revoke_prov_system (
      v_prov_code   IN   tqc_service_providers.spr_code%TYPE,
      v_sys_code    IN   tqc_systems.sys_code%TYPE
   )
   IS
   BEGIN
      DELETE FROM tqc_service_provider_systems
            WHERE sps_spr_code = v_prov_code
              AND sps_sys_code = v_sys_code
              AND sps_wet IS NULL;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error occured while deleting record '
                      || SQLERRM (SQLCODE)
                     );
   END revoke_prov_system;

   PROCEDURE prospects_prc (
      v_addedit              VARCHAR2,
      v_prospects_tab   IN   tqc_prospects_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_prospects_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No Prospects Data Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_prospects_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_prospects
             WHERE prs_code = v_prospects_tab (i).prs_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_prospects
                        (prs_code,
                         prs_surname,
                         prs_physical_addrs,
                         prs_postal_addrs,
                         prs_other_names,
                         prs_tel_no,
                         prs_mobile_no,
                         prs_email_addrs,
                         prs_id_reg_no,
                         prs_dob,
                         prs_pin,
                         prs_twn_code,
                         prs_cou_code,
                         prs_postal_code,
                         prs_type,
                         prs_contact,
                         prs_contact_tel
                        )
                 VALUES (tqc_prs_code_seq.NEXTVAL,
                         v_prospects_tab (i).prs_surname,
                         v_prospects_tab (i).prs_physical_addrs,
                         v_prospects_tab (i).prs_postal_addrs,
                         v_prospects_tab (i).prs_other_names,
                         v_prospects_tab (i).prs_tel_no,
                         v_prospects_tab (i).prs_mobile_no,
                         v_prospects_tab (i).prs_email_addrs,
                         v_prospects_tab (i).prs_id_reg_no,
                         v_prospects_tab (i).prs_dob,
                         v_prospects_tab (i).prs_pin,
                         v_prospects_tab (i).prs_twn_code,
                         v_prospects_tab (i).prs_cou_code,
                         v_prospects_tab (i).prs_postal_code,
                         v_prospects_tab (i).prs_type,
                         v_prospects_tab (i).prs_contact,
                         v_prospects_tab (i).prs_contact_tel
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_prospects
               SET prs_surname = v_prospects_tab (i).prs_surname,
                   prs_physical_addrs = v_prospects_tab (i).prs_physical_addrs,
                   prs_postal_addrs = v_prospects_tab (i).prs_postal_addrs,
                   prs_other_names = v_prospects_tab (i).prs_other_names,
                   prs_tel_no = v_prospects_tab (i).prs_tel_no,
                   prs_mobile_no = v_prospects_tab (i).prs_mobile_no,
                   prs_email_addrs = v_prospects_tab (i).prs_email_addrs,
                   prs_id_reg_no = v_prospects_tab (i).prs_id_reg_no,
                   prs_dob = v_prospects_tab (i).prs_dob,
                   prs_pin = v_prospects_tab (i).prs_pin,
                   prs_twn_code = v_prospects_tab (i).prs_twn_code,
                   prs_cou_code = v_prospects_tab (i).prs_cou_code,
                   prs_postal_code = v_prospects_tab (i).prs_postal_code,
                   prs_type = v_prospects_tab (i).prs_type,
                   prs_contact = v_prospects_tab (i).prs_contact,
                   prs_contact_tel = v_prospects_tab (i).prs_contact_tel
             WHERE prs_code = v_prospects_tab (i).prs_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_prospects
             WHERE prs_code = v_prospects_tab (i).prs_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Prospects!');
   END prospects_prc;

   PROCEDURE holidays_prc (
      v_addedit             VARCHAR2,
      v_holidays_tab   IN   tqc_holidays_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_holidays_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No Holidays Data Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_holidays_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_holidays
             WHERE hld_date = v_holidays_tab (i).hld_date;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_holidays
                        (hld_date,
                         hld_desc
                        )
                 VALUES (v_holidays_tab (i).hld_date,
                         v_holidays_tab (i).hld_desc
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_holidays
               SET hld_date = v_holidays_tab (i).hld_date,
                   hld_desc = v_holidays_tab (i).hld_desc
             WHERE hld_date = v_holidays_tab (i).hld_date;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_holidays
             WHERE hld_date = v_holidays_tab (i).hld_date;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Holidays!');
   END holidays_prc;

   PROCEDURE searchclients (v_username VARCHAR2, v_search_date DATE)
   IS
      v_day_month_param   VARCHAR2 (5);
   BEGIN
      BEGIN
         v_day_month_param :=
            NVL (
               tqc_parameters_pkg.get_param_varchar (
                  'USE_BOOKING_DAY_OR_MONTH'),
               'D');
      END;

      IF v_username IS NULL
      THEN
         raise_error('System UNABLE to verify connected user....RECONNECT SESSION....');
      ELSIF v_search_date IS NULL
      THEN
         raise_error ('Specify the Booking Date to proceed....');
      END IF;

      BEGIN
         direct_debit_pkg.validate_working_day (v_search_date);
      END;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Authorising Batch ' || SQLERRM (SQLCODE));
   END searchclients;

  PROCEDURE generatedirectdebitrecs (
      v_username         VARCHAR2,
      v_searched         VARCHAR2,
      v_dd_bbr_code      NUMBER,
      v_dd_company_acc   VARCHAR2,
      v_search_date      DATE,
      v_install_day      number
   )
   IS
      v_cnt               NUMBER;
      v_file_no_param     VARCHAR2 (5);
      v_day_month_param   VARCHAR2 (5);
      v_client            VARCHAR2 (10);
   BEGIN
      BEGIN
         v_file_no_param :=
            NVL (
               tqc_parameters_pkg.get_param_varchar (
                  'GENERATE_DIRECT_DEBIT_FILE_NO'),
               'D');
      END;

      BEGIN
         v_day_month_param :=
            NVL (
               tqc_parameters_pkg.get_param_varchar (
                  'USE_BOOKING_DAY_OR_MONTH'),
               'D');
      END;

      IF v_username IS NULL
      THEN
            raise_error('System UNABLE to verify connected user....RECONNECT SESSION....'||SQLERRM);
      ELSIF NVL (v_searched, 'N') <> 'Y'
      THEN
            raise_error('First search for records to process....'||SQLERRM);
      ELSIF v_dd_bbr_code IS NULL
      THEN
             raise_error('Select the BANK BRANCH to proceed....'||SQLERRM);
      ELSIF v_dd_company_acc IS NULL
      THEN
            raise_error('Specify the Company Direct Debit A/C to proceed....'||SQLERRM);
      ELSE
         IF NVL (v_file_no_param, 'N') != 'Y'
         THEN
            IF NVL (v_day_month_param, 'D') = 'M'
            THEN
               BEGIN
                  SELECT COUNT (1)
                    INTO v_cnt
                    FROM tqc_direct_debit
                   WHERE TO_CHAR (TRUNC (dd_book_date), 'MMRRRR') =
                            TO_CHAR (TRUNC (v_search_date - 1), 'MMRRRR');
               EXCEPTION
                  WHEN OTHERS
                  THEN
                        raise_error('Unable to validate entry....'||SQLERRM);
               END;
            ELSE
               BEGIN
                  SELECT COUNT (1)
                    INTO v_cnt
                    FROM tqc_direct_debit
                   WHERE TRUNC (dd_book_date) = TRUNC (v_search_date - 1);
               EXCEPTION
                  WHEN OTHERS
                  THEN
                   raise_error('Unable to validate entry....'||SQLERRM); 
               END;
            END IF;
         END IF;

         IF NVL (v_cnt, 0) > 0
         THEN
            --            tqc_error_manager.raise_error
            --               (12666,
            --                text_in        => 'Direct Debits have already been generated for selected debit date/month....',
            --                name1_in       => 'MODULE NAME',
            --                value1_in      => 'GENERATEDIRECTDEBITRECS'
            --               );
            raise_error (
               'Direct Debits have already been generated for selected debit date/month....'
               || SQLERRM);
         ELSE
            /* LOOP THROUGH THE RECORDS
            BEGIN

            END;
            */
      BEGIN
      SELECT PARAM_VALUE INTO 
      v_client  
      FROM TQC_PARAMETERS
      WHERE PARAM_NAME = 'CLIENT';
      EXCEPTION WHEN OTHERS THEN 
        NULL;
      END; 
      
      IF NVL(v_client,'TURNKEY') = 'HERITAGE' THEN
            BEGIN
               direct_debit_pkg.create_direct_debit_recs (37,
                                                          v_dd_bbr_code,
                                                          v_dd_company_acc,
                                                          v_username,
                                                          v_search_date,
                                                          v_install_day
                                                         );
            EXCEPTION
               WHEN OTHERS
               THEN
               raise_error('Gen error....'||SQLERRM);
            END;
      ELSE
      BEGIN
               direct_debit_pkg.create_direct_debit_recs (27,
                                                          v_dd_bbr_code,
                                                          v_dd_company_acc,
                                                          v_username,
                                                          v_search_date,
                                                          v_install_day
                                                         );
            EXCEPTION
               WHEN OTHERS
               THEN
                raise_error('Gen error....'||SQLERRM); 
            END;
      END IF;
         END IF;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error('Error Generating Direct Debit Records '||SQLERRM); 
   END generatedirectdebitrecs;

   PROCEDURE deletedirectdebit (
      v_username    VARCHAR2,
      v_dd_code     NUMBER,
      v_dd_status   VARCHAR2
   )
   IS
   BEGIN
      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error (
            12648,
            text_in     => 'System UNABLE to verify connected user....RECONNECT SESSION....',
            name1_in    => 'MODULE NAME',
            value1_in   => 'DELETEDIRECTDEBIT');
      END IF;

      IF NVL (v_dd_status, 'D') = 'D'
      THEN
         DELETE tqc_direct_debit_detail
          WHERE ddd_dd_code = v_dd_code;

         DELETE tqc_direct_debit_header
          WHERE ddh_dd_code = v_dd_code;

         DELETE tqc_direct_debit
          WHERE dd_code = v_dd_code;
      ELSE
         tqc_error_manager.raise_error (
            12657,
            text_in     => 'Delete NOT ALLOWED for authorized records....',
            name1_in    => 'MODULE NAME',
            value1_in   => 'DELETEDIRECTDEBIT');
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated (
            text_in     => 'Error Authorising Batch ' || SQLERRM (SQLCODE),
            name1_in    => 'MODULE NAME',
            value1_in   => 'DELETEDIRECTDEBIT');
   END deletedirectdebit;

   PROCEDURE authorisedirectdebit (
      v_username    VARCHAR2,
      v_dd_code     NUMBER,
      v_dd_status   VARCHAR2
   )
   IS
      v_cnt   NUMBER;
   BEGIN
      IF v_username IS NULL
      THEN
         raise_error('System UNABLE to verify connected user....RECONNECT SESSION....');
      ELSIF NVL (v_dd_status, 'D') = 'A'
      THEN
         raise_error('The selected batch has already been authorized....');
      END IF;

      SELECT COUNT (1)
        INTO v_cnt
        FROM tqc_direct_debit_detail, tqc_direct_debit_header
       WHERE ddh_code = ddd_ddh_code AND ddh_dd_code = v_dd_code;

      IF NVL (v_cnt, 0) = 0
      THEN
         raise_error('This batch has no generated records attached....');
      ELSE
         BEGIN
            direct_debit_pkg.update_bo_date (v_dd_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error('Unable to update policy dtls....');
         END;

         BEGIN
            UPDATE tqc_direct_debit_detail
               SET ddd_status = 'A'
             WHERE ddd_dd_code = v_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error('Unable to update Direct Debit detail records....');
         END;

         BEGIN
            UPDATE tqc_direct_debit_header
               SET ddh_status = 'A'
             WHERE ddh_dd_code = v_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Unable to update Direct Debit header records....');
         END;

         BEGIN
            UPDATE tqc_direct_debit
               SET dd_status = 'A',
                   dd_authorized = 'Y',
                   dd_auth_by = v_username,
                   dd_auth_date = SYSDATE
             WHERE dd_code = v_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error('Unable to update Direct Debit record dtls....');
         END;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
        raise_error(  'Error Authorising Batch '|| SQLERRM (SQLCODE));
   END authorisedirectdebit;

   PROCEDURE deletedirectdebitdetail (
      v_username    VARCHAR2,
      v_ddd_code    NUMBER,
      v_dd_status   VARCHAR2
   )
   IS
   BEGIN
      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'DELETEDIRECTDEBIT'
            );
      END IF;

      IF NVL (v_dd_status, 'D') = 'D'
      THEN
         BEGIN
            direct_debit_pkg.delete_dd_detail (v_ddd_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               tqc_error_manager.raise_error
                                            (12658,
                                             text_in        => 'Delete error....',
                                             name1_in       => 'MODULE NAME',
                                             value1_in      => 'DELETEDIRECTDEBIT'
                                            );
         END;
      ELSE
         tqc_error_manager.raise_error
                 (12657,
                  text_in        => 'Delete NOT ALLOWED for authorized records....',
                  name1_in       => 'MODULE NAME',
                  value1_in      => 'DELETEDIRECTDEBIT'
                 );
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                                   (text_in        =>    'Error Authorising Batch '
                                                      || SQLERRM (SQLCODE),
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'DELETEDIRECTDEBIT'
                                   );
   END deletedirectdebitdetail;

   --   PROCEDURE generatereceipts (
   --      v_username       VARCHAR2,
   --      v_dd_code        NUMBER,
   --      v_dd_receipted   VARCHAR2,
   --      v_dd_book_date   DATE
   --   )
   --   IS
   --   BEGIN
   --      IF v_username IS NULL
   --      THEN
   ----         tqc_error_manager.raise_error
   ----            (12648,
   ----             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
   ----             name1_in       => 'MODULE NAME',
   ----             value1_in      => 'GENERATERECEIPTS'
   ----            );
   --            raise_error('System UNABLE to verify connected user....RECONNECT SESSION....');
   --      ELSIF v_dd_receipted = 'Y'
   --      THEN
   ----         tqc_error_manager.raise_error
   ----            (12649,
   ----             text_in        => 'Receipts for the selected batch have already been generated....',
   ----             name1_in       => 'MODULE NAME',
   ----             value1_in      => 'GENERATERECEIPTS'
   ----            );
   --            raise_error('Receipts for the selected batch have already been generated....');
   --      ELSIF v_dd_receipted = 'F'
   --      THEN
   ----         tqc_error_manager.raise_error
   ----            (12650,
   ----             text_in        => 'Receipts for the selected batch have ALL been cancelled....',
   ----             name1_in       => 'MODULE NAME',
   ----             value1_in      => 'GENERATERECEIPTS'
   ----            );
   --            raise_error('Receipts for the selected batch have ALL been cancelled....');
   --      ELSIF direct_debit_pkg.get_business_date (v_dd_book_date, 4) > SYSDATE
   --      THEN
   ----         tqc_error_manager.raise_error
   ----            (12651,
   ----             text_in        =>    'This batch can ONLY be receipted after '
   ----                               || TO_CHAR
   ----                                     (direct_debit_pkg.get_business_date
   ----                                                              (v_dd_book_date,
   ----                                                               4
   ----                                                              ),
   ----                                      'DD/MM/RRRR'
   ----                                     )
   ----                               || '....',
   ----             name1_in       => 'MODULE NAME',
   ----             value1_in      => 'GENERATERECEIPTS'
   ----            );
   --            raise_error('This batch can ONLY be receipted after '
   --                               || TO_CHAR
   --                                     (direct_debit_pkg.get_business_date
   --                                                              (v_dd_book_date,
   --                                                               4
   --                                                              ),
   --                                      'DD/MM/RRRR'
   --                                     )
   --                               || '....');
   --      ELSE
   --         direct_debit_pkg.generate_receipts (v_dd_code, v_username);
   --      END IF;
   --   EXCEPTION
   --      WHEN OTHERS
   --      THEN
   --            raise_error('Error Generating Receipt '|| SQLERRM (SQLCODE));
   ----         tqc_error_manager.raise_unanticipated
   ----                                  (text_in        =>    'Error Generating Receipt '
   ----                                                     || SQLERRM (SQLCODE),
   ----                                   name1_in       => 'MODULE NAME',
   ----                                   value1_in      => 'GENERATERECEIPTS'
   ----                                  );

--   END generatereceipts;
   PROCEDURE generatereceipts (
      v_username       VARCHAR2,
      v_dd_code        NUMBER,
      v_dd_receipted   VARCHAR2,
      v_dd_book_date   DATE,
      v_dd_receipt_date   DATE
   )
   IS
      v_dd_receipting_days   NUMBER;
   BEGIN
--   raise_error(
--     'v_dd_code: '|| v_dd_code||
--     'v_dd_receipted: '||v_dd_receipted ||
--     'v_dd_book_date: '|| v_dd_book_date ||
--     'v_dd_receipt_date: '|| v_dd_receipt_date
--   );
      BEGIN
         v_dd_receipting_days:=tqc_parameters_pkg.get_param_varchar('DD_RECEIPTING_DAYS');
      END;
      
      IF v_username IS NULL
      THEN
            raise_error('System UNABLE to verify connected user....RECONNECT SESSION....');
      ELSIF v_dd_receipted = 'Y'
      THEN
            raise_error('Receipts for the selected batch have already been generated....');
      ELSIF v_dd_receipted = 'F'
      THEN
            raise_error('Receipts for the selected batch have ALL been cancelled....');
      ELSIF direct_debit_pkg.get_business_date (v_dd_book_date, v_dd_receipting_days) > SYSDATE
      THEN
            raise_error('This batch can ONLY be receipted after '
                               || TO_CHAR
                                     (direct_debit_pkg.get_business_date
                                                              (v_dd_book_date,
                                                               4
                                                              ),
                                      'DD/MM/RRRR'
                                     )
                               || '....');
      ELSE
         BEGIN
            UPDATE tqc_direct_debit
             SET dd_receipt_date=v_dd_receipt_date
            WHERE dd_code=v_dd_code; 
            EXCEPTION when others then
              raise_error('Unable update receipt date: '||SQLERRM);
         END;

         direct_debit_pkg.generate_receipts (v_dd_code, v_username);
      END IF;
--   EXCEPTION
--      WHEN OTHERS
--      THEN
--         raise_error('Error Generating Receipt '|| SQLERRM );
   END generatereceipts;

   PROCEDURE failupdateddheader (
      v_username       VARCHAR2,
      v_ddh_code       NUMBER,
      v_fail_date      DATE,
      v_fail_remarks   VARCHAR2,
      v_ddh_dd_code    NUMBER
   )
   IS
      v_cnt1     NUMBER;
      v_cnt2     NUMBER;
      v_count1   NUMBER;
      v_count2   NUMBER;
   BEGIN
      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'FAILUPDATEDDHEADER'
            );
      ELSIF v_fail_date IS NULL
      THEN
         tqc_error_manager.raise_error
            (12671,
             text_in        => 'Provide the Direct Debit failure date to proceed....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'FAILUPDATEDDHEADER'
            );
      ELSIF v_fail_remarks IS NULL
      THEN
         tqc_error_manager.raise_error
                 (12672,
                  text_in        => 'Provide the reason for failure to proceed....',
                  name1_in       => 'MODULE NAME',
                  value1_in      => 'FAILUPDATEDDHEADER'
                 );
      END IF;

      BEGIN
         UPDATE tqc_direct_debit_detail
            SET ddd_status = 'F',
                ddd_fail_date = v_fail_date,
                ddd_fail_updated_by = v_username,
                ddd_fail_update_date = SYSDATE,
                ddd_fail_remarks = v_fail_remarks
          WHERE ddd_ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                        (12673,
                                         text_in        => 'Update Detail error...',
                                         name1_in       => 'MODULE NAME',
                                         value1_in      => 'FAILUPDATEDDHEADER'
                                        );
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_count1
           FROM tqc_direct_debit_detail
          WHERE ddd_ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Child records not found2....' || SQLERRM);
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_count2
           FROM tqc_direct_debit_detail
          WHERE ddd_ddh_code = v_ddh_code AND ddd_status = 'F';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Child records not found3....' || SQLERRM);
      END;

      IF NVL (v_count1, 0) = NVL (v_count2, 0) AND NVL (v_count1, 0) > 0
      THEN
         BEGIN
            UPDATE tqc_direct_debit_header
               SET ddh_status = 'F',
                   ddh_fail_date = NVL (v_fail_date, ddh_fail_date),
                   ddh_fail_updated_by = v_username,
                   ddh_fail_update_date = SYSDATE,
                   ddh_fail_remarks = v_fail_remarks
             WHERE ddh_code = v_ddh_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Update Header error....v_status=' || SQLERRM);
         END;
      END IF;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt1
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                   (12675,
                                    text_in        => 'Child records not found....',
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'FAILUPDATEDDHEADER'
                                   );
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt2
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code AND ddh_status = 'F';
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                   (12675,
                                    text_in        => 'Child records not found....',
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'FAILUPDATEDDHEADER'
                                   );
      END;

      IF NVL (v_cnt1, 0) = NVL (v_cnt2, 0) AND NVL (v_cnt1, 0) > 0
      THEN
         BEGIN
            UPDATE tqc_direct_debit
               SET dd_receipted = 'F'
             WHERE dd_code = v_ddh_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               tqc_error_manager.raise_error
                                           (12676,
                                            text_in        => 'Update DD error...',
                                            name1_in       => 'MODULE NAME',
                                            value1_in      => 'FAILUPDATEDDHEADER'
                                           );
         END;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                                        (text_in        =>    'Error Updating DDH '
                                                           || SQLERRM (SQLCODE),
                                         name1_in       => 'MODULE NAME',
                                         value1_in      => 'FAILUPDATEDDHEADER'
                                        );
   END failupdateddheader;

   PROCEDURE failupdateddheader2 (
      v_username                VARCHAR2,
      v_ddh_code                NUMBER,
      v_book_date               DATE,
      v_fail_remarks            VARCHAR2,
      v_ddh_dd_code    IN OUT   NUMBER,
      v_status                  VARCHAR DEFAULT 'F'
   )
   IS
      v_cnt1        NUMBER;
      v_cnt2        NUMBER;
      v_fail_date   DATE;
   BEGIN
      -- RAISE_ERROR('werterterter');
      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'FAILUPDATEDDHEADER'
            );
      ELSIF v_book_date IS NULL
      THEN
         tqc_error_manager.raise_error
             (12671,
              text_in        => 'Provide the Direct Debit Book date to proceed....',
              name1_in       => 'MODULE NAME',
              value1_in      => 'FAILUPDATEDDHEADER'
             );
--        ELSIF v_fail_remarks IS NULL THEN
--            TQC_ERROR_MANAGER.RAISE_ERROR(12672,
--                                         text_in => 'Provide the reason for failure to proceed....'
--                                         , name1_in       => 'MODULE NAME'
--                                          , value1_in      => 'FAILUPDATEDDHEADER');
      END IF;

      BEGIN
         v_fail_date := direct_debit_pkg.get_business_date (v_book_date, 4);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error Getting Fail Date');
      END;

      IF v_ddh_dd_code IS NULL
      THEN
         BEGIN
            SELECT ddh_dd_code
              INTO v_ddh_dd_code
              FROM tqc_direct_debit_header
             WHERE ddh_code = v_ddh_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               tqc_error_manager.raise_error
                                  (12675,
                                   text_in        => 'Child records not found1....',
                                   name1_in       => 'MODULE NAME',
                                   value1_in      => 'FAILUPDATEDDHEADER'
                                  );
         END;
      END IF;

      BEGIN
         UPDATE tqc_direct_debit_detail
            SET ddd_status = NVL (v_status, 'F'),
                ddd_fail_date = NVL (v_fail_date, ddd_fail_date),
                ddd_fail_updated_by = v_username,
                ddd_fail_update_date = SYSDATE,
                ddd_fail_remarks = v_fail_remarks
          WHERE ddd_ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Update Detail error....');
      END;

      BEGIN
         UPDATE tqc_direct_debit_header
            SET ddh_status = NVL (v_status, 'F'),
                ddh_fail_date = NVL (v_fail_date, ddh_fail_date),
                ddh_fail_updated_by = v_username,
                ddh_fail_update_date = SYSDATE,
                ddh_fail_remarks = v_fail_remarks
          WHERE ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                        (12674,
                                         text_in        => 'Update Header error...',
                                         name1_in       => 'MODULE NAME',
                                         value1_in      => 'FAILUPDATEDDHEADER'
                                        );
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt1
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                   (12676,
                                    text_in        => 'Child records not found....',
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'FAILUPDATEDDHEADER'
                                   );
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt2
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code
            AND ddh_status = NVL (v_status, 'F');
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                   (12677,
                                    text_in        => 'Child records not found....',
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'FAILUPDATEDDHEADER'
                                   );
      END;

      IF NVL (v_cnt1, 0) = NVL (v_cnt2, 0) AND NVL (v_cnt1, 0) > 0
      THEN
         BEGIN
            UPDATE tqc_direct_debit
               SET dd_receipted = NVL (v_status, 'F')
             WHERE dd_code = v_ddh_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               tqc_error_manager.raise_error
                                           (12678,
                                            text_in        => 'Update DD error...',
                                            name1_in       => 'MODULE NAME',
                                            value1_in      => 'FAILUPDATEDDHEADER'
                                           );
         END;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating DDH');
   END failupdateddheader2;

   PROCEDURE failupdateheader (
      v_dddcode                 NUMBER,
      v_username                VARCHAR2,
      v_policyno                VARCHAR2,
      v_account                 VARCHAR2,
      v_book_date               DATE,
      v_fail_remarks            VARCHAR2,
      v_ddh_dd_code    IN OUT   NUMBER,
      v_status                  VARCHAR DEFAULT 'F',
      v_bankAmount              NUMBER
   )
   IS
      v_cnt1        NUMBER;
      v_cnt2        NUMBER;
      v_ddh_code    NUMBER;
      v_fail_date   DATE;
      v_count1      NUMBER;
      v_count2      NUMBER;
      v_dd_code     NUMBER;
      v_dd_ref_no   NUMBER;
   BEGIN
      BEGIN
         SELECT dd_ref_no
           INTO v_dd_ref_no
           FROM tqc_direct_debit
          WHERE dd_code = v_ddh_dd_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (   'Error Fetching Batch REF.NO for Batch..'
                         || v_ddh_dd_code
                        );
      END;

      BEGIN
         SELECT ddd_dd_code
           INTO v_dd_code
           FROM tqc_direct_debit_detail
          WHERE ddd_code = v_dddcode;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error
                    (   'The direct debit record with EXPORT NUMBER..'
                     || v_dddcode
                     || '...'
                     || 'does not belong to the exported batch with REF.NO..'
                     || v_dd_ref_no
                    );
      END;

      IF v_dd_code != v_ddh_dd_code
      THEN
         raise_error
                    (   'The direct debit record with EXPORT NUMBER..'
                     || v_dddcode
                     || '...'
                     || 'does not belong to the exported batch with REF.NO..'
                     || v_dd_ref_no
                    );
      END IF;

      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'FAILUPDATEDDHEADER'
            );
      ELSIF v_book_date IS NULL
      THEN
         tqc_error_manager.raise_error
             (12671,
              text_in        => 'Provide the Direct Debit Book date to proceed....',
              name1_in       => 'MODULE NAME',
              value1_in      => 'FAILUPDATEDDHEADER'
             );
--        ELSIF v_fail_remarks IS NULL THEN
--            TQC_ERROR_MANAGER.RAISE_ERROR(12672,
--                                         text_in => 'Provide the reason for failure to proceed....'
--                                         , name1_in       => 'MODULE NAME'
--                                          , value1_in      => 'FAILUPDATEDDHEADER');
      END IF;

      BEGIN
         v_fail_date := direct_debit_pkg.get_business_date (v_book_date, 4);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error Getting Fail Date' || SQLERRM);
      END;

      --raise_error('v_dddCode='||v_dddCode);
      BEGIN
         SELECT ddh_dd_code, ddh_code
           INTO v_ddh_dd_code, v_ddh_code
           FROM tqc_direct_debit_header, tqc_direct_debit_detail
          WHERE ddd_ddh_code = ddh_code AND ddd_code = v_dddcode;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (   'Child records not found1....'
                         || v_policyno
                         || 'Account'
                         || v_account
                         || ' v_ddh_dd_code '
                         || v_ddh_dd_code
                         || ';v_dddCode='
                         || v_dddcode
                        );
      END;

      BEGIN
         UPDATE tqc_direct_debit_detail
            SET ddd_status = NVL (v_status, 'A'),
                ddd_fail_date = NVL (v_fail_date, ddd_fail_date),
                ddd_fail_updated_by = v_username,
                ddd_fail_update_date = SYSDATE,
                ddd_fail_remarks = v_fail_remarks,
                ddd_bank_amount = v_bankAmount
          WHERE ddd_code = v_dddcode;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Update Detail error....');
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_count1
           FROM tqc_direct_debit_detail
          WHERE ddd_ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Child records not found2....' || SQLERRM);
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_count2
           FROM tqc_direct_debit_detail
          WHERE ddd_ddh_code = v_ddh_code
                AND ddd_status = NVL (v_status, 'F');
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Child records not found3....' || SQLERRM);
      END;

      IF NVL (v_count1, 0) = NVL (v_count2, 0) AND NVL (v_count1, 0) > 0
      THEN
         BEGIN
            UPDATE tqc_direct_debit_header
               SET ddh_status = NVL (v_status, 'A'),
                   ddh_fail_date = NVL (v_fail_date, ddh_fail_date),
                   ddh_fail_updated_by = v_username,
                   ddh_fail_update_date = SYSDATE,
                   ddh_fail_remarks = v_fail_remarks,
                   ddh_amount = v_bankAmount
             WHERE ddh_code = v_ddh_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                     'Update Header error....v_status='
                  || v_status
                  || ' '
                  || SQLERRM);
         END;
      END IF;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt1
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Child records not found2....' || SQLERRM);
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt2
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code
                AND ddh_status = NVL (v_status, 'F');
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Child records not found3....' || SQLERRM);
      END;

      --raise_error('v_cnt1='||v_cnt1||'v_cnt2='||v_cnt2);
      IF NVL (v_cnt1, 0) = NVL (v_cnt2, 0) AND NVL (v_cnt1, 0) > 0
      THEN
         BEGIN
            UPDATE tqc_direct_debit
               SET dd_receipted = NVL (v_status, 'F')
             WHERE dd_code = v_ddh_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Update DD error...' || SQLERRM);
         END;
      END IF;
--    EXCEPTION
--        WHEN OTHERS THEN
--            RAISE_ERROR('Error Updating DDH v_status='||v_status||' '|| SQLERRM);
   END failupdateheader;

   PROCEDURE relaunchddheader (
      v_username      VARCHAR2,
      v_ddh_code      NUMBER,
      v_ddh_status    VARCHAR2,
      v_ddh_dd_code   NUMBER
   )
   IS
      v_cnt1   NUMBER := 0;
      v_cnt2   NUMBER := 0;
      v_cnt3   NUMBER := 0;
   BEGIN
      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'RELAUNCHDDHEADER'
            );
      ELSIF v_ddh_status NOT IN ('F', 'R')
      THEN
         tqc_error_manager.raise_error
            (12677,
             text_in        => 'Cannot relaunch a draft or authorised direct debit....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'RELAUNCHDDHEADER'
            );
      END IF;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt3
           FROM tqc_direct_debit_header
          WHERE ddh_prev_ddh_code = v_ddh_code;
      END;

      IF v_cnt3 <> 0
      THEN
         tqc_error_manager.raise_error
            (12678,
             text_in        => 'Relaunch has already been done and another Direct Debit generated......delete the existing one then continue.......',
             name1_in       => 'MODULE NAME',
             value1_in      => 'RELAUNCHDDHEADER'
            );
      END IF;

      BEGIN
         UPDATE tqc_direct_debit_detail
            SET ddd_status = 'R',
                ddd_relaunched_by = v_username,
                ddd_relaunch_date = SYSDATE
          WHERE ddd_ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                        (12679,
                                         text_in        => 'Update detail error...',
                                         name1_in       => 'MODULE NAME',
                                         value1_in      => 'RELAUNCHDDHEADER'
                                        );
      END;

      BEGIN
         UPDATE tqc_direct_debit_header
            SET ddh_status = 'R',
                ddh_relaunched_by = v_username,
                ddh_relaunch_date = SYSDATE
          WHERE ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                        (12679,
                                         text_in        => 'Update header error...',
                                         name1_in       => 'MODULE NAME',
                                         value1_in      => 'RELAUNCHDDHEADER'
                                        );
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt1
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                   (12675,
                                    text_in        => 'Child records not found....',
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'RELAUNCHDDHEADER'
                                   );
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt2
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code AND ddh_status = 'R';
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                   (12675,
                                    text_in        => 'Child records not found....',
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'RELAUNCHDDHEADER'
                                   );
      END;

      IF NVL (v_cnt1, 0) = NVL (v_cnt2, 0) AND NVL (v_cnt1, 0) > 0
      THEN
         BEGIN
            UPDATE tqc_direct_debit
               SET dd_receipted = 'R'
             WHERE dd_code = v_ddh_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               tqc_error_manager.raise_error
                                            (12679,
                                             text_in        => 'Update DD error...',
                                             name1_in       => 'MODULE NAME',
                                             value1_in      => 'RELAUNCHDDHEADER'
                                            );
         END;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                                        (text_in        =>    'Error Updating DDH '
                                                           || SQLERRM (SQLCODE),
                                         name1_in       => 'MODULE NAME',
                                         value1_in      => 'RELAUNCHDDHEADER'
                                        );
   END relaunchddheader;

   PROCEDURE stoplaunchddheader (
      v_username      VARCHAR2,
      v_ddh_code      NUMBER,
      v_ddh_status    VARCHAR2,
      v_ddh_dd_code   NUMBER
   )
   IS
      v_cnt1   NUMBER := 0;
      v_cnt2   NUMBER := 0;
   BEGIN
      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'STOPLAUNCHDDHEADER'
            );
      ELSIF v_ddh_status NOT IN ('R')
      THEN
         tqc_error_manager.raise_error
            (12680,
             text_in        => 'Cannot stop relaunch of  direct debit which has not been relaunched....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'STOPLAUNCHDDHEADER'
            );
      END IF;

      BEGIN
         UPDATE tqc_direct_debit_detail
            SET ddd_status = 'F',
                ddd_relaunch_stoped_by = v_username,
                ddd_relaunch_stop_date = SYSDATE
          WHERE ddd_ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                        (12679,
                                         text_in        => 'Update header error...',
                                         name1_in       => 'MODULE NAME',
                                         value1_in      => 'STOPLAUNCHDDHEADER'
                                        );
      END;

      BEGIN
         UPDATE tqc_direct_debit_header
            SET ddh_status = 'F',
                ddh_relaunch_stoped_by = v_username,
                ddh_relaunch_stop_date = SYSDATE
          WHERE ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                        (12679,
                                         text_in        => 'Update detail error...',
                                         name1_in       => 'MODULE NAME',
                                         value1_in      => 'STOPLAUNCHDDHEADER'
                                        );
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt1
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                   (12675,
                                    text_in        => 'Child records not found....',
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'STOPLAUNCHDDHEADER'
                                   );
      END;

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt2
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_ddh_dd_code AND ddh_status = 'F';
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                   (12675,
                                    text_in        => 'Child records not found....',
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'STOPLAUNCHDDHEADER'
                                   );
      END;

      IF NVL (v_cnt1, 0) = NVL (v_cnt2, 0) AND NVL (v_cnt1, 0) > 0
      THEN
         BEGIN
            UPDATE tqc_direct_debit
               SET dd_receipted = 'F'
             WHERE dd_code = v_ddh_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               tqc_error_manager.raise_error
                                           (12679,
                                            text_in        => 'Update DD error...',
                                            name1_in       => 'MODULE NAME',
                                            value1_in      => 'STOPLAUNCHDDHEADER'
                                           );
         END;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                                        (text_in        =>    'Error Updating DDH '
                                                           || SQLERRM (SQLCODE),
                                         name1_in       => 'MODULE NAME',
                                         value1_in      => 'STOPLAUNCHDDHEADER'
                                        );
   END stoplaunchddheader;

   PROCEDURE orgdivlevelstype_prc (
      v_addedit                     VARCHAR2,
      v_orgdivlevelstype_tab   IN   tqc_org_div_levels_type_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_orgdivlevelstype_tab.COUNT = 0
      THEN
         raise_error
             (   'Error occured. No Org Division Level Type Data Provided : '
              || SQLERRM (SQLCODE)
             );
      END IF;

      FOR i IN 1 .. v_orgdivlevelstype_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_org_division_levels_type
             WHERE dlt_code = v_orgdivlevelstype_tab (i).dlt_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_org_division_levels_type
                        (dlt_code,
                         dlt_sys_code,
                         dlt_desc,
                         dlt_act_code,
                         dlt_code_val
                        )
                 VALUES (v_orgdivlevelstype_tab (i).dlt_code,
                         v_orgdivlevelstype_tab (i).dlt_sys_code,
                         v_orgdivlevelstype_tab (i).dlt_desc,
                         v_orgdivlevelstype_tab (i).dlt_act_code,
                         v_orgdivlevelstype_tab (i).dlt_act_code--,tqc_dlt_code_val_seq.NEXTVAL
                        
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_org_division_levels_type
               SET dlt_sys_code = v_orgdivlevelstype_tab (i).dlt_sys_code,
                   dlt_desc = v_orgdivlevelstype_tab (i).dlt_desc,
                   dlt_act_code = v_orgdivlevelstype_tab (i).dlt_act_code
             WHERE dlt_code = v_orgdivlevelstype_tab (i).dlt_code;
         ELSIF v_addedit = 'D'
         THEN
            -- You want to DELETE the child records before deleting the parent
            /*DELETE TQC_ORG_SUBDIV_PREV_HEADS
            WHERE OSDPH_OSD_CODE IN (SELECT OSD_CODE FROM TQC_ORG_SUBDIVISIONS WHERE OSD_DLT_CODE = v_orgDivLevelsType_tab(I).DLT_CODE )

            DELETE TQC_ORG_SUBDIVISIONS
            WHERE OSD_DLT_CODE = v_orgDivLevelsType_tab(I).DLT_CODE;

            DELETE TQC_ORG_DIVISION_LEVELS
            WHERE ODL_DLT_CODE = v_orgDivLevelsType_tab(I).DLT_CODE;*/

            -- Now you can DELETE the parent record
            DELETE tqc_org_division_levels_type
             WHERE dlt_code = v_orgdivlevelstype_tab (i).dlt_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Org Division Level Type!');
   END orgdivlevelstype_prc;

   PROCEDURE orgdivisionlevels_prc (
      v_addedit                 VARCHAR2,
      v_orgdivlevels_tab   IN   tqc_org_division_levels_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_orgdivlevels_tab.COUNT = 0
      THEN
         raise_error
                  (   'Error occured. No Org Division Level Data Provided : '
                   || SQLERRM (SQLCODE)
                  );
      END IF;

      FOR i IN 1 .. v_orgdivlevels_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_org_division_levels
             WHERE odl_code = v_orgdivlevels_tab (i).odl_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_org_division_levels
                        (odl_code,
                         odl_dlt_code,
                         odl_desc,
                         odl_ranking,
                         odl_type
                        )
                 VALUES (v_orgdivlevels_tab (i).odl_code,
                         v_orgdivlevels_tab (i).odl_dlt_code,
                         v_orgdivlevels_tab (i).odl_desc,
                         v_orgdivlevels_tab (i).odl_ranking,
                         v_orgdivlevels_tab (i).odl_type
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_org_division_levels
               SET odl_dlt_code = v_orgdivlevels_tab (i).odl_dlt_code,
                   odl_desc = v_orgdivlevels_tab (i).odl_desc,
                   odl_ranking = v_orgdivlevels_tab (i).odl_ranking,
                   odl_type = v_orgdivlevels_tab (i).odl_type
             WHERE odl_code = v_orgdivlevels_tab (i).odl_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_org_division_levels
             WHERE odl_code = v_orgdivlevels_tab (i).odl_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         --raise_error('Error Manipulating Org Division Level!');
         raise_application_error (-20401,
                                     'Error Manipulating Org Division Level!'
                                  || SQLERRM (SQLCODE)
                                 );
   END orgdivisionlevels_prc;

   PROCEDURE orgsubdivisions_prc (
      v_addedit                    VARCHAR2,
      v_orgsubdivisions_tab   IN   tqc_org_subdivisions_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_orgsubdivisions_tab.COUNT = 0
      THEN
         raise_error
                    (   'Error occured. No Org Sub Division Data Provided : '
                     || SQLERRM (SQLCODE)
                    );
      END IF;

      FOR i IN 1 .. v_orgsubdivisions_tab.COUNT
      LOOP
--         raise_error(
--            'v_orgsubdivisions_tab (i).osd_status: '||v_orgsubdivisions_tab (i).osd_status||
--            'v_orgsubdivisions_tab (i).osd_wet: '||v_orgsubdivisions_tab (i).osd_wet
--         );
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_org_subdivisions
             WHERE osd_code = v_orgsubdivisions_tab (i).osd_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_org_subdivisions
                        (osd_code,
                         osd_parent_osd_code,
                         osd_dlt_code,
                         osd_odl_code,
                         osd_name,
                         osd_status,
                         osd_wef,
                         osd_wet,
                         osd_div_head_agn_code,
                         osd_sys_code,
                         osd_brn_code,
                         osd_reg_code,
                         osd_post_level,
                         osd_manager_allwd,
                         osd_over_comm_earn,
                         osd_id,
                         osd_parent_id
                        )
                 VALUES (v_orgsubdivisions_tab (i).osd_code,
                         v_orgsubdivisions_tab (i).osd_parent_osd_code,
                         v_orgsubdivisions_tab (i).osd_dlt_code,
                         v_orgsubdivisions_tab (i).osd_odl_code,
                         v_orgsubdivisions_tab (i).osd_name,
                         v_orgsubdivisions_tab (i).osd_status,
                         v_orgsubdivisions_tab (i).osd_wef,
                         v_orgsubdivisions_tab (i).osd_wet,
                         v_orgsubdivisions_tab (i).osd_div_head_agn_code,
                         v_orgsubdivisions_tab (i).osd_sys_code,
                         v_orgsubdivisions_tab (i).osd_brn_code,
                         v_orgsubdivisions_tab (i).osd_reg_code,
                         v_orgsubdivisions_tab (i).osd_post_level,
                         v_orgsubdivisions_tab (i).osd_manager_allwd,
                         v_orgsubdivisions_tab (i).osd_over_comm_earn,
                         v_orgsubdivisions_tab (i).osd_id,
                         v_orgsubdivisions_tab (i).osd_parent_id
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_org_subdivisions
               SET osd_parent_osd_code =  v_orgsubdivisions_tab (i).osd_parent_osd_code,
                   osd_dlt_code = v_orgsubdivisions_tab (i).osd_dlt_code,
                   osd_odl_code = v_orgsubdivisions_tab (i).osd_odl_code,
                   osd_name = v_orgsubdivisions_tab (i).osd_name,
                   osd_status = v_orgsubdivisions_tab (i).osd_status,
                   osd_wef = v_orgsubdivisions_tab (i).osd_wef,
                   osd_wet = v_orgsubdivisions_tab (i).osd_wet,
                   osd_div_head_agn_code = v_orgsubdivisions_tab (i).osd_div_head_agn_code,
                   osd_sys_code = v_orgsubdivisions_tab (i).osd_sys_code,
                   osd_brn_code = v_orgsubdivisions_tab (i).osd_brn_code,
                   osd_reg_code = v_orgsubdivisions_tab (i).osd_reg_code,
                   osd_post_level = v_orgsubdivisions_tab (i).osd_post_level,
                   osd_manager_allwd = v_orgsubdivisions_tab (i).osd_manager_allwd,            
                   osd_over_comm_earn = v_orgsubdivisions_tab (i).osd_over_comm_earn,   
                   osd_id = v_orgsubdivisions_tab (i).osd_id,
                   osd_parent_id = v_orgsubdivisions_tab (i).osd_parent_id
             WHERE osd_code = v_orgsubdivisions_tab (i).osd_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_org_subdivisions
             WHERE osd_code = v_orgsubdivisions_tab (i).osd_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Org Division Level!');
   END orgsubdivisions_prc;

   PROCEDURE agencyactivities_prc (
      v_addedit            IN   VARCHAR2,
      v_aac_code                NUMBER,
      v_aac_sys_code            NUMBER,
      v_aac_acty_code           NUMBER,
      v_aac_wef                 DATE,
      v_aac_estimate_wet        DATE,
      v_aac_actual_wet          DATE,
      v_aac_remarks             VARCHAR2,
      v_aac_agn_code            NUMBER,
      v_aac_act_type            VARCHAR2,
      v_aac_reasons             VARCHAR2,
      v_aac_by_code             NUMBER
   )
   IS
      v_count   NUMBER;
   BEGIN
      -- IF v_agencyActivities_tab.COUNT = 0 THEN
        --   raise_error('Error occured. No Agency Activity Data Provided : '||SQLERRM(SQLCODE));
      --- END IF;
      IF v_addedit = 'A'
      THEN
         SELECT COUNT (1)
           INTO v_count
           FROM tqc_agency_activities
          WHERE aac_code = v_aac_code;

         IF v_count > 0
         THEN
            raise_error ('Record with ID Exists!');
         END IF;

         INSERT INTO tqc_agency_activities (aac_code,
                                            aac_sys_code,
                                            aac_acty_code,
                                            aac_wef,
                                            aac_estimate_wet,
                                            aac_actual_wet,
                                            aac_remarks,
                                            aac_agn_code,
                                            aac_by_type,
                                            aac_reasons,
                                            aac_by_code)
              VALUES (tqc_aac_code_seq.NEXTVAL,
                      v_aac_sys_code,
                      v_aac_acty_code,
                      v_aac_wef,
                      v_aac_estimate_wet,
                      v_aac_actual_wet,
                      v_aac_remarks,
                      v_aac_agn_code,
                      v_aac_act_type,
                      v_aac_reasons,
                      v_aac_by_code);
      -- v_agencyActivities_tab(I).AAC_MKTR_AGN_CODE);
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_agency_activities
            SET aac_sys_code = v_aac_sys_code,
                aac_code = v_aac_code,
                aac_acty_code = v_aac_acty_code,
                aac_wef = v_aac_wef,
                aac_estimate_wet = v_aac_estimate_wet,
                aac_actual_wet = v_aac_actual_wet,
                aac_remarks = v_aac_remarks,
                aac_agn_code = v_aac_agn_code,
                aac_by_type = v_aac_act_type,
                aac_reasons = v_aac_reasons,
                aac_by_code = v_aac_by_code
          --  AAC_MKTR_AGN_CODE = v_agencyActivities_tab(I).AAC_MKTR_AGN_CODE
          WHERE aac_code = v_aac_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE tqc_agency_activities
          WHERE aac_code = v_aac_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Agency Activities!');
   END agencyactivities_prc;

   PROCEDURE holidaydefinitions_prc (
      v_addedit                       VARCHAR2,
      v_holidaydefinitions_tab   IN   tqc_holidays_definitions_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_holidaydefinitions_tab.COUNT = 0
      THEN
         raise_error
                  (   'Error occured. No Holiday Definition Data Provided : '
                   || SQLERRM (SQLCODE)
                  );
      END IF;

      FOR i IN 1 .. v_holidaydefinitions_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_holidays_definitions
             WHERE thd_desc = v_holidaydefinitions_tab (i).thd_desc
               AND thd_day = v_holidaydefinitions_tab (i).thd_day
               AND thd_mon = v_holidaydefinitions_tab (i).thd_mon
               AND thd_cou_code = v_holidaydefinitions_tab (i).thd_cou_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_holidays_definitions
                        (thd_desc,
                         thd_day,
                         thd_mon,
                         thd_status,
                         thd_cou_code
                        )
                 VALUES (v_holidaydefinitions_tab (i).thd_desc,
                         v_holidaydefinitions_tab (i).thd_day,
                         v_holidaydefinitions_tab (i).thd_mon,
                         v_holidaydefinitions_tab (i).thd_status,
                         v_holidaydefinitions_tab (i).thd_cou_code
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_holidays_definitions
               SET thd_status = v_holidaydefinitions_tab (i).thd_status
             WHERE thd_desc = v_holidaydefinitions_tab (i).thd_desc
               AND thd_day = v_holidaydefinitions_tab (i).thd_day
               AND thd_mon = v_holidaydefinitions_tab (i).thd_mon
               AND thd_cou_code = v_holidaydefinitions_tab (i).thd_cou_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE FROM tqc_holidays
                  WHERE hld_desc = v_holidaydefinitions_tab (i).thd_desc;

            DELETE      tqc_holidays_definitions
                  WHERE thd_desc = v_holidaydefinitions_tab (i).thd_desc
                    AND thd_day = v_holidaydefinitions_tab (i).thd_day
                    AND thd_mon = v_holidaydefinitions_tab (i).thd_mon
                    AND thd_cou_code =
                                     v_holidaydefinitions_tab (i).thd_cou_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Holiday Definitions!');
   END holidaydefinitions_prc;

   PROCEDURE postalcodes_prc (
      v_addedit                VARCHAR2,
      v_postalcodes_tab   IN   tqc_postal_codes_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_postalcodes_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No Postal Codes Data Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_postalcodes_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_postal_codes
             WHERE pst_code = v_postalcodes_tab (i).pst_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_postal_codes
                        (pst_code,
                         pst_twn_code,
                         pst_desc,
                         pst_zip_code
                        )
                 VALUES (tqc_postal_code_seq.NEXTVAL,
                         v_postalcodes_tab (i).pst_twn_code,
                         v_postalcodes_tab (i).pst_desc,
                         v_postalcodes_tab (i).pst_zip_code
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_postal_codes
               SET pst_twn_code = v_postalcodes_tab (i).pst_twn_code,
                   pst_desc = v_postalcodes_tab (i).pst_desc,
                   pst_zip_code = v_postalcodes_tab (i).pst_zip_code
             WHERE pst_code = v_postalcodes_tab (i).pst_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_postal_codes
             WHERE pst_code = v_postalcodes_tab (i).pst_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Postal Codes!');
   END postalcodes_prc;

   PROCEDURE activitytypes_prc (
      v_addedit                  VARCHAR2,
      v_activitytypes_tab   IN   tqc_activity_types_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_activitytypes_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No Activity Type Data Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_activitytypes_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_activity_types
             WHERE acty_desc = v_activitytypes_tab (i).acty_desc;

            IF v_count > 0
            THEN
               raise_error ('Record with Description Exists!');
            END IF;

            INSERT INTO tqc_activity_types
                        (acty_code,
                         acty_sys_code,
                         acty_desc
                        )
                 VALUES (tqc_acty_code_seq.NEXTVAL,
                         v_activitytypes_tab (i).acty_sys_code,
                         v_activitytypes_tab (i).acty_desc
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_activity_types
               SET acty_sys_code = v_activitytypes_tab (i).acty_sys_code,
                   acty_desc = v_activitytypes_tab (i).acty_desc
             WHERE acty_code = v_activitytypes_tab (i).acty_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_activity_types
                  WHERE acty_code = v_activitytypes_tab (i).acty_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Activity Types!');
   END activitytypes_prc;

   PROCEDURE orgsubdivprevheads_prc (
      v_addedit                      VARCHAR2,
      v_orgsubdivprevhead_tab   IN   tqc_org_subdiv_prev_heads_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_orgsubdivprevhead_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No Previous Head Data Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_orgsubdivprevhead_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            INSERT INTO tqc_org_subdiv_prev_heads
                        (osdph_code,
                         osdph_osd_code,
                         osdph_prev_agn_code,
                         osdph_wet,
                         osdph_osd_id
                        )
                 VALUES (tqc_osdph_code_seq.NEXTVAL,
                         v_orgsubdivprevhead_tab (i).osdph_osd_code,
                         v_orgsubdivprevhead_tab (i).osdph_prev_agn_code,
                         v_orgsubdivprevhead_tab (i).osdph_wet,
                         v_orgsubdivprevhead_tab (i).osdph_osd_id
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_org_subdiv_prev_heads
               SET osdph_osd_code = v_orgsubdivprevhead_tab (i).osdph_osd_code,
                   osdph_prev_agn_code =
                               v_orgsubdivprevhead_tab (i).osdph_prev_agn_code,
                   osdph_wet = v_orgsubdivprevhead_tab (i).osdph_wet,
                   osdph_osd_id = v_orgsubdivprevhead_tab (i).osdph_osd_id
             WHERE osdph_code = v_orgsubdivprevhead_tab (i).osdph_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_org_subdiv_prev_heads
                  WHERE osdph_code = v_orgsubdivprevhead_tab (i).osdph_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Previous Head data!');
   END orgsubdivprevheads_prc;

   PROCEDURE syspostlevels_prc (
      v_addedit                  VARCHAR2,
      v_syspostlevels_tab   IN   tqc_sys_post_levels_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_syspostlevels_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No Sys Post Levels Data Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_syspostlevels_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            INSERT INTO tqc_sys_post_levels
                        (syspl_sys_code,
                         syspl_code,
                         syspl_sht_desc,
                         syspl_desc,
                         syspl_ranking,
                         syspl_wef
                        )
                 VALUES (v_syspostlevels_tab (i).syspl_sys_code,
                         tqc_syspl_code_seq.NEXTVAL,
                         v_syspostlevels_tab (i).syspl_sht_desc,
                         v_syspostlevels_tab (i).syspl_desc,
                         v_syspostlevels_tab (i).syspl_ranking,
                         v_syspostlevels_tab (i).syspl_wef
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_sys_post_levels
               SET syspl_sys_code = v_syspostlevels_tab (i).syspl_sys_code,
                   syspl_sht_desc = v_syspostlevels_tab (i).syspl_sht_desc,
                   syspl_desc = v_syspostlevels_tab (i).syspl_desc,
                   syspl_ranking = v_syspostlevels_tab (i).syspl_ranking,
                   syspl_wef = v_syspostlevels_tab (i).syspl_wef
             WHERE syspl_code = v_syspostlevels_tab (i).syspl_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_sys_post_levels
                  WHERE syspl_code = v_syspostlevels_tab (i).syspl_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Sys Post Levels data!');
   END syspostlevels_prc;

   PROCEDURE sysposts_prc (
      v_addedit             VARCHAR2,
      v_sysposts_tab   IN   tqc_sys_posts_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_sysposts_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No Sys Post Data Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_sysposts_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            INSERT INTO tqc_sys_posts
                        (spost_sys_code,
                         spost_syspl_code,
                         spost_parent_spost_code,
                         spost_code,
                         spost_sht_desc,
                         spost_desc,
                         spost_remarks,
                         spost_wef,
                         spost_brn_code,
                         spost_subdiv_osd_code,
                         spost_usr_code
                        )
                 VALUES (v_sysposts_tab (i).spost_sys_code,
                         v_sysposts_tab (i).spost_syspl_code,
                         v_sysposts_tab (i).spost_parent_spost_code,
                         tqc_spost_code_seq.NEXTVAL,
                         v_sysposts_tab (i).spost_sht_desc,
                         v_sysposts_tab (i).spost_desc,
                         v_sysposts_tab (i).spost_remarks,
                         v_sysposts_tab (i).spost_wef,
                         v_sysposts_tab (i).spost_brn_code,
                         v_sysposts_tab (i).spost_subdiv_osd_code,
                         v_sysposts_tab (i).spost_usr_code
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_sys_posts
               SET spost_sys_code = v_sysposts_tab (i).spost_sys_code,
                   spost_syspl_code = v_sysposts_tab (i).spost_syspl_code,
                   spost_parent_spost_code =
                                    v_sysposts_tab (i).spost_parent_spost_code,
                   spost_sht_desc = v_sysposts_tab (i).spost_sht_desc,
                   spost_desc = v_sysposts_tab (i).spost_desc,
                   spost_remarks = v_sysposts_tab (i).spost_remarks,
                   spost_wef = v_sysposts_tab (i).spost_wef,
                   spost_brn_code = v_sysposts_tab (i).spost_brn_code,
                   spost_subdiv_osd_code =
                                      v_sysposts_tab (i).spost_subdiv_osd_code,
                   spost_usr_code = v_sysposts_tab (i).spost_usr_code
             WHERE spost_code = v_sysposts_tab (i).spost_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_sys_posts
             WHERE spost_code = v_sysposts_tab (i).spost_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Sys Post data!');
   END sysposts_prc;

   PROCEDURE sysprcsssubarealmts_prc (
      v_addedit                      VARCHAR2,
      v_sysprcsssubarealmts_tab   IN tqc_sys_prcss_subarea_lmts_tab)
   IS
      v_count   NUMBER;
   BEGIN
      IF v_sysprcsssubarealmts_tab.COUNT = 0
      THEN
         raise_error (
            'Error occured. No Sys Process SubArea Limits Data Provided : '
            || SQLERRM (SQLCODE));
      END IF;

      FOR i IN 1 .. v_sysprcsssubarealmts_tab.COUNT
      LOOP
         IF v_sysprcsssubarealmts_tab (i).spsat_min_limit >=
               v_sysprcsssubarealmts_tab (i).spsat_max_limit
         THEN
            raise_error (   'Max limit cannot be less than min limit '
                         || SQLERRM (SQLCODE)
                        );
         END IF;

         IF v_addedit = 'A'
         THEN
            INSERT INTO tqc_sys_prcss_subarea_lmts
                        (spsat_code,
                         spsat_sprsa_code,
                         spsat_no_of_level,
                         spsat_min_limit,
                         spsat_max_limit
                        )
                 VALUES (tqc_spsat_code_seq.NEXTVAL,
                         v_sysprcsssubarealmts_tab (i).spsat_sprsa_code,
                         v_sysprcsssubarealmts_tab (i).spsat_no_of_level,
                         v_sysprcsssubarealmts_tab (i).spsat_min_limit,
                         v_sysprcsssubarealmts_tab (i).spsat_max_limit
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_sys_prcss_subarea_lmts
               SET spsat_sprsa_code =
                      v_sysprcsssubarealmts_tab (i).spsat_sprsa_code,
                   spsat_no_of_level =
                      v_sysprcsssubarealmts_tab (i).spsat_no_of_level,
                   spsat_min_limit =
                      v_sysprcsssubarealmts_tab (i).spsat_min_limit,
                   spsat_max_limit =
                      v_sysprcsssubarealmts_tab (i).spsat_max_limit
             WHERE spsat_code = v_sysprcsssubarealmts_tab (i).spsat_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_sys_prcss_subarea_lmts
             WHERE spsat_code = v_sysprcsssubarealmts_tab (i).spsat_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Sys Process SubArea Limits data!');
   END sysprcsssubarealmts_prc;

   PROCEDURE sysprcsssubarealvls_prc (
      v_addedit                        VARCHAR2,
      v_sysprcsssubarealvls_tab   IN   tqc_sys_prcss_subarea_lvls_tab
   )
   IS
      v_count               NUMBER;
      v_spsat_no_of_level   NUMBER;
   BEGIN
      IF v_sysprcsssubarealvls_tab.COUNT = 0
      THEN
         raise_error
            (   'Error occured. No Sys Process SubArea Levels Data Provided : '
             || SQLERRM (SQLCODE)
            );
      END IF;

      FOR i IN 1 .. v_sysprcsssubarealvls_tab.COUNT
      LOOP
         BEGIN
            SELECT spsat_no_of_level
              INTO v_spsat_no_of_level
              FROM tqc_sys_prcss_subarea_lmts
             WHERE spsat_code = v_sysprcsssubarealvls_tab (i).spsal_spsat_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               NULL;
         END;

         IF v_addedit = 'A'
         THEN
            IF v_sysprcsssubarealvls_tab (i).spsal_level >
                  v_spsat_no_of_level
            THEN
               raise_error (   'Only '
                            || v_spsat_no_of_level
                            || ' Number of levels allowed...'
                           );
            END IF;

            INSERT INTO tqc_sys_prcss_subarea_lvls
                        (spsal_code,
                         spsal_sprsa_code,
                         spsal_spsat_code,
                         spsal_level,
                         spsal_approver_type,
                         spsal_approver_id
                        )
                 VALUES (tqc_spsal_code_seq.NEXTVAL,
                         v_sysprcsssubarealvls_tab (i).spsal_sprsa_code,
                         v_sysprcsssubarealvls_tab (i).spsal_spsat_code,
                         v_sysprcsssubarealvls_tab (i).spsal_level,
                         v_sysprcsssubarealvls_tab (i).spsal_approver_type,
                         v_sysprcsssubarealvls_tab (i).spsal_approver_id
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_sys_prcss_subarea_lvls
               SET spsal_sprsa_code =
                      v_sysprcsssubarealvls_tab (i).spsal_sprsa_code,
                   spsal_spsat_code =
                      v_sysprcsssubarealvls_tab (i).spsal_spsat_code,
                   spsal_level = v_sysprcsssubarealvls_tab (i).spsal_level,
                   spsal_approver_type =
                      v_sysprcsssubarealvls_tab (i).spsal_approver_type,
                   spsal_approver_id =
                      v_sysprcsssubarealvls_tab (i).spsal_approver_id
             WHERE spsal_code = v_sysprcsssubarealvls_tab (i).spsal_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_sys_prcss_subarea_lvls
             WHERE spsal_code = v_sysprcsssubarealvls_tab (i).spsal_code;
         END IF;

         SELECT COUNT (*)
           INTO v_count
           FROM tqc_sys_prcss_subarea_lvls
          WHERE spsal_level = v_sysprcsssubarealvls_tab (i).spsal_level
                AND spsal_spsat_code =
                       v_sysprcsssubarealvls_tab (i).spsal_spsat_code;

         IF v_count > 1
         THEN
            raise_error ('Duplicate level exists');
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Sys Process SubArea Limits data!');
   END sysprcsssubarealvls_prc;

   PROCEDURE usersystems_prc (
      v_addedit                VARCHAR2,
      v_usersystems_tab   IN   tqc_user_systems_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_usersystems_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No User Systems Data Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_usersystems_tab.COUNT
      LOOP
         IF v_addedit = 'A'
         THEN
            INSERT INTO tqc_user_systems
                        (usys_code,
                         usys_usr_code,
                         usys_sys_code,
                         usys_wef,
                         usys_wet,
                         usys_spost_code
                        )
                 VALUES (tqc_usys_code_seq.NEXTVAL,
                         v_usersystems_tab (i).usys_usr_code,
                         v_usersystems_tab (i).usys_sys_code,
                         v_usersystems_tab (i).usys_wef,
                         v_usersystems_tab (i).usys_wet,
                         v_usersystems_tab (i).usys_spost_code
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_user_systems
               SET usys_usr_code = v_usersystems_tab (i).usys_usr_code,
                   usys_sys_code = v_usersystems_tab (i).usys_sys_code,
                   usys_wef = v_usersystems_tab (i).usys_wef,
                   usys_wet = v_usersystems_tab (i).usys_wet,
                   usys_spost_code = v_usersystems_tab (i).usys_spost_code
             WHERE usys_code = v_usersystems_tab (i).usys_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_user_systems
                  WHERE usys_code = v_usersystems_tab (i).usys_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating User System data!');
   END usersystems_prc;

   PROCEDURE agencysystems_prc (
      v_addedit                  VARCHAR2,
      v_agencysystems_tab   IN   tqc_agency_systems_tab
   )
   IS
      v_count             NUMBER;
      v_shtdesc           VARCHAR2 (100);
      v_orgshtdesc        VARCHAR2 (10);
      v_direct_srl_fmt    VARCHAR2 (50);
      v_agent_id          VARCHAR2 (50);
      v_agent_seq         NUMBER;
      v_name_first_char   VARCHAR2 (20);
      v_no_of_chars       NUMBER         := 1;
      v_serial_length     NUMBER         := 6;
      v_act_type          VARCHAR2 (2);
      v_count             NUMBER         := 0;
      v_created_by        VARCHAR2 (20);
      v_date_created      DATE;
      v_agn_short_desc    VARCHAR2 (20);
      v_act_desc          VARCHAR2 (75);
      v_act_wthtx_rate    NUMBER;
   BEGIN
      IF v_agencysystems_tab.COUNT = 0
      THEN
         raise_error (   'Error occured. No Agency Systems Data Provided : '
                      || SQLERRM (SQLCODE)
                     );
      END IF;

      FOR i IN 1 .. v_agencysystems_tab.COUNT
      LOOP
         IF v_agencysystems_tab (i).asys_agn_sht_desc IS NULL
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

            SELECT org_sht_desc
              INTO v_orgshtdesc
              FROM tqc_organizations, tqc_systems
             WHERE sys_org_code = org_code
               AND sys_code = v_agencysystems_tab (i).asys_sys_code;

            SELECT agent_id_seq.NEXTVAL
              INTO v_agent_seq
              FROM DUAL;

            v_agent_seq := LPAD (v_agent_seq, v_serial_length, 0);
            v_shtdesc := v_orgshtdesc || '/' || v_agent_seq;
         END IF;

         IF v_addedit = 'A'
         THEN
            INSERT INTO tqc_agency_systems (asys_sys_code,
                                            asys_agn_code,
                                            asys_wef,
                                            asys_wet,
                                            asys_comment,
                                            asys_osd_code,
                                            asys_osd_id,
                                            asys_agn_sht_desc)
                 VALUES (v_agencysystems_tab (i).asys_sys_code,
                         v_agencysystems_tab (i).asys_agn_code,
                         v_agencysystems_tab (i).asys_wef,
                         v_agencysystems_tab (i).asys_wet,
                         v_agencysystems_tab (i).asys_comment,
                         v_agencysystems_tab (i).asys_osd_code,
                         v_agencysystems_tab (i).asys_osd_id, v_shtdesc
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_agency_systems
               SET asys_wef = v_agencysystems_tab (i).asys_wef,
                   asys_wet = v_agencysystems_tab (i).asys_wet,
                   asys_comment = v_agencysystems_tab (i).asys_comment,
                   asys_osd_code = v_agencysystems_tab (i).asys_osd_code,
                   asys_osd_id = v_agencysystems_tab (i).asys_osd_id,
                   asys_agn_sht_desc = NVL (asys_agn_sht_desc, v_shtdesc)
             WHERE asys_sys_code = v_agencysystems_tab (i).asys_sys_code
                   AND asys_agn_code = v_agencysystems_tab (i).asys_agn_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE tqc_agency_systems
             WHERE asys_sys_code = v_agencysystems_tab (i).asys_sys_code
                   AND asys_agn_code = v_agencysystems_tab (i).asys_agn_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Agency System data!');
   END agencysystems_prc;

   PROCEDURE claim_paymentmode_prc (
      v_add_edit   VARCHAR2,
      v_code       tqc_clm_payment_modes.cpm_code%TYPE,
      v_sht_desc   tqc_clm_payment_modes.cpm_sht_desc%TYPE,
      v_desc       tqc_clm_payment_modes.cpm_desc%TYPE,
      v_remarks    tqc_clm_payment_modes.cpm_remarks%TYPE,
      v_max_amt    tqc_clm_payment_modes.cpm_max_amt%TYPE,
      v_min_amt    tqc_clm_payment_modes.cpm_min_amt%TYPE,
      v_default    tqc_clm_payment_modes.cpm_default%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
   --raise_error('v_default'||v_default);
      IF v_default = 'Y'
      THEN
        BEGIN
            SELECT COUNT (*)
                  INTO v_count
                  FROM tqc_clm_payment_modes
                 WHERE cpm_default = v_default;

             IF v_count >= 1
             THEN
               raise_error('Default Payment Mode has already been Set');
             END IF;
       END;
     END IF;
     
      IF v_add_edit = 'A'
      THEN
         BEGIN
          INSERT INTO tqc_clm_payment_modes (cpm_code,
                                               cpm_sht_desc,
                                               cpm_desc,
                                               cpm_remarks,
                                               cpm_min_amt,
                                               cpm_max_amt,
                                               cpm_default)
                 VALUES (tq_crm.cpm_code_seq.NEXTVAL,
                         v_sht_desc,
                         v_desc,
                         v_remarks,
                         v_min_amt,
                         v_max_amt,
                         v_default);
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
            UPDATE tqc_clm_payment_modes
               SET cpm_sht_desc = v_sht_desc,
                   cpm_desc = v_desc,
                   cpm_remarks = v_remarks,
                   cpm_min_amt = v_min_amt,
                   cpm_max_amt = v_max_amt,
                   cpm_default = v_default
             WHERE cpm_code = v_code;
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
            DELETE FROM tqc_clm_payment_modes
                  WHERE cpm_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE mobilepaymenttypes_prc (
      v_add_edit          VARCHAR2,
      v_code              tqc_mobile_pymnt_types.mpt_code%TYPE,
      v_sht_desc          tqc_mobile_pymnt_types.mpt_sht_desc%TYPE,
      v_desc              tqc_mobile_pymnt_types.mpt_desc%TYPE,
      v_min_amt_allowed   tqc_mobile_pymnt_types.mpt_min_amt_allowed%TYPE,
      v_max_amt_allowed   tqc_mobile_pymnt_types.mpt_max_amt_allowed%TYPE,
      v_cou_code          tqc_mobile_pymnt_types.mpt_cou_code%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM tqc_mobile_pymnt_types
             WHERE mpt_sht_desc = v_sht_desc;

            IF v_count = 1
            THEN
               raise_error
                  ('The Short Description,already exist ::Enter a different one'
                  );
            END IF;

            INSERT INTO tqc_mobile_pymnt_types (mpt_code,
                                                mpt_sht_desc,
                                                mpt_desc,
                                                mpt_min_amt_allowed,
                                                mpt_max_amt_allowed,
                                                mpt_cou_code)
                 VALUES (tq_crm.mpt_code_seq.NEXTVAL,
                         v_sht_desc,
                         v_desc,
                         v_min_amt_allowed,
                         v_max_amt_allowed,
                         v_cou_code);
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
            UPDATE tqc_mobile_pymnt_types
               SET mpt_code = v_code,
                   mpt_sht_desc = v_sht_desc,
                   mpt_desc = v_desc,
                   mpt_min_amt_allowed = v_min_amt_allowed,
                   mpt_max_amt_allowed = v_max_amt_allowed,
                   mpt_cou_code = v_cou_code
             WHERE mpt_code = v_code;
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
            DELETE FROM tqc_mobile_pymnt_types
                  WHERE mpt_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE mobiletypesprefix_prc (
      v_add_edit        VARCHAR2,
      v_code            tqc_mob_pymnt_types_prefixes.mptp_code%TYPE,
      v_mob_no_prefix   tqc_mob_pymnt_types_prefixes.mptp_mob_no_prefix%TYPE,
      v_mpt_code        tqc_mob_pymnt_types_prefixes.mptp_mpt_code%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            ---select count(*) INTO v_count  from TQC_MOB_PYMNT_TYPES_PREFIXES where MPT_SHT_DESC =v_sht_desc;
            IF v_count = 1
            THEN
               raise_error
                  ('The Short Description,already exist ::Enter a different one'
                  );
            END IF;

            INSERT
              INTO tqc_mob_pymnt_types_prefixes (mptp_code,
                                                 mptp_mob_no_prefix,
                                                 mptp_mpt_code)
            VALUES (
                      tq_crm.mptp_code_seq.NEXTVAL,
                      v_mob_no_prefix,
                      v_mpt_code);
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
            UPDATE tqc_mob_pymnt_types_prefixes
               SET mptp_mob_no_prefix = v_mob_no_prefix,
                   mptp_mpt_code = v_mpt_code
             WHERE mptp_code = v_code;
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
            DELETE FROM tqc_mob_pymnt_types_prefixes
                  WHERE mptp_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE usergroup_prc (
      v_add_edit       VARCHAR2,
      v_code           tqc_group_users.gusr_code%TYPE,
      v_grp_usr_code   tqc_group_users.gusr_grp_usr_code%TYPE,
      v_usr_code       tqc_group_users.gusr_usr_code%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_group_users
                        (gusr_code, gusr_grp_usr_code,
                         gusr_usr_code
                        )
                 VALUES (tq_crm.gusr_code_seq.NEXTVAL, v_grp_usr_code,
                         v_usr_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_group_users
                  WHERE gusr_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE clientgroup_prc (
      v_add_edit   VARCHAR2,
      v_code       tqc_group_client.grp_code%TYPE,
      v_name       tqc_group_client.grp_name%TYPE,
      v_minimum    tqc_group_client.grp_minimum%TYPE,
      v_maximum    tqc_group_client.grp_maximum%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM tqc_group_client
             WHERE grp_name = v_name;

            IF v_count < 1
            THEN
               INSERT INTO tqc_group_client (grp_code,
                                             grp_name,
                                             grp_minimum,
                                             grp_maximum)
                    VALUES (tq_crm.grp_code_seq.NEXTVAL,
                            v_name,
                            v_minimum,
                            v_maximum);
            ELSE
               raise_error
                       ('The Group Name Already exist::Enter Different Name ');
            END IF;
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
            UPDATE tqc_group_client
               SET grp_name = v_name,
                   grp_minimum = v_minimum,
                   grp_maximum = v_maximum
             WHERE grp_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_group_clnt_dtls
                  WHERE grpd_grp_code = v_code;

            DELETE FROM tqc_group_client
                  WHERE grp_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE clientgroupmembers_prc (
      v_add_edit    VARCHAR2,
      v_code        tqc_group_clnt_dtls.grpd_code%TYPE,
      v_clnt_code   tqc_group_clnt_dtls.grpd_code%TYPE,
      v_grp_code    tqc_group_clnt_dtls.grpd_grp_code%TYPE
   )
   IS
      v_count   NUMBER;
      v_max     NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM tqc_group_clnt_dtls
             WHERE grpd_grp_code = v_grp_code;

            SELECT grp_maximum
              INTO v_max
              FROM tqc_group_client
             WHERE grp_code = v_grp_code;

            IF v_count <= v_max
            THEN
               INSERT INTO tqc_group_clnt_dtls
                           (grpd_code, grpd_clnt_code,
                            grpd_grp_code
                           )
                    VALUES (tq_crm.grpd_code_seq.NEXTVAL, v_clnt_code,
                            v_grp_code
                           );
            ELSE
               raise_error
                  ('The Maximum number of Client  allowed for that Group  exceeded:: '
                  );
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_group_clnt_dtls
                  WHERE grpd_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE printer_server_prc (
      v_add_edit            VARCHAR2,
      v_code                NUMBER,
      v_name                VARCHAR2,
      v_filter              VARCHAR2,
      v_uri                 VARCHAR2,
      v_filter_command      VARCHAR2,
      v_sec_username        VARCHAR2,
      v_sec_password        VARCHAR2,
      v_sec_auth_type       VARCHAR2,
      v_sec_encrpt_type     VARCHAR2,
      v_proxy_host          VARCHAR2,
      v_proxy_port          VARCHAR2,
      v_proxy_username      VARCHAR2,
      v_proxy_pasword       VARCHAR2,
      v_proxy_authen_type   VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM tqc_print_servers
             WHERE serv_name = v_name;

            IF v_count < 1
            THEN
               INSERT INTO tqc_print_servers
                           (serv_code, serv_name, serv_filter,
                            serv_uri, serv_filter_command,
                            serv_sec_username, serv_sec_password,
                            serv_sec_auth_type, serv_sec_encrpt_type,
                            serv_proxy_host, serv_proxy_port,
                            serv_proxy_username, serv_proxy_pasword,
                            serv_proxy_authen_type
                           )
                    VALUES (tqc_serv_code_seq.NEXTVAL, v_name, v_filter,
                            v_uri, v_filter_command,
                            v_sec_username, v_sec_password,
                            v_sec_auth_type, v_sec_encrpt_type,
                            v_proxy_host, v_proxy_port,
                            v_proxy_username, v_proxy_pasword,
                            v_proxy_authen_type
                           );
            ELSE
               raise_error
                  ('Print Server Exist::Enter A different  Name   and  Try again :: '
                  );
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while Saving  Printer '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF (v_add_edit = 'E')
      THEN
         BEGIN
            UPDATE tqc_print_servers
               SET serv_name = v_name,
                   serv_filter = v_filter,
                   serv_uri = v_uri,
                   serv_filter_command = v_filter_command,
                   serv_sec_username = v_sec_username,
                   serv_sec_password = v_sec_password,
                   serv_sec_auth_type = v_sec_auth_type,
                   serv_sec_encrpt_type = v_sec_encrpt_type,
                   serv_proxy_host = v_proxy_host,
                   serv_proxy_port = v_proxy_port,
                   serv_proxy_username = v_proxy_username,
                   serv_proxy_pasword = v_proxy_pasword,
                   serv_proxy_authen_type = v_proxy_authen_type
             WHERE serv_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while Updating Printer '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_print_servers
                  WHERE serv_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting Printer '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE save_lead_details (
      v_lds_code              NUMBER,
      v_desc                  VARCHAR2,
      v_cmp_code              NUMBER,
      v_cotel                 VARCHAR2,
      v_title                 VARCHAR2,
      v_surname               VARCHAR2,
      v_oth_name              VARCHAR2,
      v_mob_no                VARCHAR2,
      v_fax                   VARCHAR2,
      v_email                 VARCHAR2,
      v_post_addr             VARCHAR2,
      v_post_code             VARCHAR2,
      v_phy_addr              VARCHAR2,
      v_ldsrccode             NUMBER,
      v_lstscode              NUMBER,
      v_lead_date             DATE,
      v_converted             VARCHAR2,
      v_port_name             VARCHAR2,
      v_port_contr            VARCHAR2,
      v_port_amt              NUMBER,
      v_port_sale             VARCHAR2,
      v_port_clse             DATE,
      v_ann_rev               NUMBER,
      v_website               VARCHAR2,
      v_industry              VARCHAR2,
      v_cou_code              NUMBER,
      v_state_code            NUMBER,
      v_town_code             NUMBER,
      v_org_code              NUMBER,
      v_user_code             NUMBER,
      v_team_code             NUMBER,
      v_acc_code              NUMBER,
      v_prod_code             NUMBER,
      v_cur_code              NUMBER,
      v_lds_sys_code          NUMBER,
      v_lds_div_code          NUMBER,
      v_lds_occupation   IN   VARCHAR2,
      v_lds_comp_name    IN   VARCHAR2,
      v_clnt_type        IN   VARCHAR2 default null,
      v_user_name        IN   VARCHAR2 default null
   )
   IS
      v_cnt   NUMBER := 0;
   BEGIN
             -- raise_error (' v_surname: '||v_surname||' v_oth_name: '||v_oth_name);
             
             ---raise_error (' v_clnt_type: '||v_clnt_type||' v_user_name: '||v_user_name);
      IF v_user_name IS NOT NULL
      THEN
         BEGIN
            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt
                 FROM TQC_USERS
                WHERE USR_NAME = v_user_name OR USR_USERNAME = v_user_name;
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_cnt := 0;
            END;

            IF v_cnt = 0
            THEN
               BEGIN
                  raise_error (' User: ' || v_user_name || ' not found!');
               END;
            END IF;
         END;
      END IF;

      IF v_lds_code IS NULL
      THEN
         BEGIN
            INSERT INTO tqc_leads (lds_code,
                                   lds_title,
                                   lds_surnname,
                                   lds_othernames,
                                   lds_camp_tel,
                                   lds_mob_tel,
                                   lds_camp_code,
                                   lds_fax,
                                   lds_email_addrs,
                                   lds_ldsrc_code,
                                   lds_lsts_code,
                                   lds_physical_addrs,
                                   lds_postal_addrs,
                                   lds_postal_code,
                                   lds_date,
                                   lds_desc,
                                   lds_converted,
                                   lds_industry,
                                   lds_ann_revenue,
                                   lds_web_site,
                                   lds_cou_code,
                                   lds_state_code,
                                   lds_twn_code,
                                   lds_org_code,
                                   lds_usr_code,
                                   lds_team_usr_code,
                                   lds_acc_code,
                                   lds_prod_code,
                                   lds_cur_code,
                                   lds_sys_code,
                                   lds_div_code,
                                   lds_occupation,
                                   lds_comp_name,
                                   lds_usr)
                 VALUES (tqc_lds_code_seq.NEXTVAL,
                         v_title,
                         v_surname,
                         v_oth_name,
                         v_cotel,
                         v_mob_no,
                         v_cmp_code,
                         v_fax,
                         v_email,
                         v_ldsrccode,
                         v_lstscode,
                         v_phy_addr,
                         v_post_addr,
                         v_post_code,
                         v_lead_date,
                         v_desc,
                         v_converted,
                         v_industry,
                         v_ann_rev,
                         v_website,
                         v_cou_code,
                         v_state_code,
                         v_town_code,
                         v_org_code,
                         v_user_code,
                         v_team_code,
                         v_acc_code,
                         v_prod_code,
                         v_cur_code,
                         v_lds_sys_code,
                         v_lds_div_code,
                         v_lds_occupation,
                         v_lds_comp_name,
                         v_user_name);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Lead Details.');
         END;
      ELSE
         BEGIN
            UPDATE tqc_leads
               SET lds_title = NVL (v_title, lds_title),
                   lds_surnname = NVL (v_surname, lds_surnname),
                   lds_othernames = NVL (v_oth_name, lds_othernames),
                   lds_camp_tel = NVL (v_cotel, lds_camp_tel),
                   lds_mob_tel = NVL (v_mob_no, lds_mob_tel),
                   lds_camp_code = NVL (v_cmp_code, lds_camp_code),
                   lds_fax = NVL (v_fax, lds_fax),
                   lds_email_addrs = NVL (v_email, lds_email_addrs),
                   lds_ldsrc_code = NVL (v_ldsrccode, lds_ldsrc_code),
                   lds_lsts_code = NVL (v_lstscode, lds_lsts_code),
                   lds_physical_addrs = NVL (v_phy_addr, lds_physical_addrs),
                   lds_postal_addrs = NVL (v_post_addr, lds_postal_addrs),
                   lds_postal_code = NVL (v_post_code, lds_postal_code),
                   lds_date = NVL (v_lead_date, lds_date),
                   lds_desc = NVL (v_desc, lds_desc),
                   lds_converted = NVL (v_converted, lds_converted),
                   lds_industry = NVL (v_industry, lds_industry),
                   lds_ann_revenue = NVL (v_ann_rev, lds_ann_revenue),
                   lds_web_site = NVL (v_website, lds_web_site),
                   lds_cou_code = v_cou_code,
                   lds_state_code = v_state_code,
                   lds_twn_code = v_town_code,
                   lds_org_code = v_org_code,
                   lds_usr_code = v_user_code,
                   lds_team_usr_code = v_team_code,
                   lds_acc_code = v_acc_code,
                   lds_cur_code = v_cur_code,
                   lds_prod_code = v_prod_code,
                   lds_sys_code = v_lds_sys_code,
                   lds_div_code = v_lds_div_code,
                   lds_occupation = v_lds_occupation,
                   lds_comp_name = v_lds_comp_name,
                   lds_usr = v_user_name
             WHERE lds_code = v_lds_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating Lead Details.');
         END;
      END IF;
   END;

   PROCEDURE delete_lead (v_lds_code NUMBER)
   IS
   BEGIN
      DELETE tqc_leads
       WHERE lds_code = v_lds_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Deleting Lead Details.');
   END;

   PROCEDURE save_lead_comment (
      v_action       VARCHAR2,
      v_lds_code     NUMBER,
      v_lcmnt_code   NUMBER,
      v_comment      VARCHAR2,
      v_date         DATE,
      v_usr_code     NUMBER
   )
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_lead_comments (lcmnt_code,
                                           lcmnt_lds_code,
                                           lcmnt_usr_code,
                                           lcmnt_comment,
                                           lcmnt_date)
                 VALUES (tqc_lcmnt_code_seq.NEXTVAL,
                         v_lds_code,
                         v_usr_code,
                         v_comment,
                         v_date);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Lead Comment.');
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_lead_comments
               SET lcmnt_comment = v_comment,
                   lcmnt_date = v_date
             WHERE lcmnt_code = v_lcmnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating Lead Comment.');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_lead_comments
                  WHERE lcmnt_code = v_lcmnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting Lead Comment.');
         END;
      ELSE
         raise_error ('Invalid Option Selected.');
      END IF;
   END;

   PROCEDURE save_lead_activities (
      v_action       VARCHAR2,
      v_lacts_code   NUMBER,
      v_act_code     NUMBER,
      v_lds_code     NUMBER
   )
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT
              INTO tqc_leads_activities (lacts_code,
                                         lacts_lds_code,
                                         lacts_act_code)
            VALUES (tqc_lacts_code_seq.NEXTVAL, v_lds_code, v_act_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Lead Activity.');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE tqc_leads_activities
             WHERE lacts_code = v_lacts_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Lead Activity.');
         END;
      ELSE
         raise_error ('Invalid Option Selected.');
      END IF;
   END;

   PROCEDURE save_lead_sources (
      v_action   VARCHAR2,
      v_code     NUMBER,
      v_desc     VARCHAR2
   )
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_leads_sources (ldsrc_code, ldsrc_desc)
                 VALUES (tqc_ldsrc_code_seq.NEXTVAL, v_desc);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Lead Source.');
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_leads_sources
               SET ldsrc_desc = v_desc
             WHERE ldsrc_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating Lead Source.');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_leads_sources
                  WHERE ldsrc_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting Lead Source.');
         END;
      ELSE
         raise_error ('Invalid Option Selected.');
      END IF;
   END;

   PROCEDURE save_lead_statuses (
      v_action   VARCHAR2,
      v_code     NUMBER,
      v_desc     VARCHAR2
   )
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_leads_statuses (lsts_code, lsts_desc)
                 VALUES (tqc_lsts_code_seq.NEXTVAL, v_desc);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Lead Source.');
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_leads_statuses
               SET lsts_desc = v_desc
             WHERE lsts_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating Lead Source.');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_leads_statuses
                  WHERE lsts_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting Lead Source.');
         END;
      ELSE
         raise_error ('Invalid Option Selected.');
      END IF;
   END;

   PROCEDURE report_groups_prc (
      v_action     IN   VARCHAR2 DEFAULT NULL,
      v_trg_code        tqc_report_groups.trg_code%TYPE DEFAULT NULL,
      v_trg_name        tqc_report_groups.trg_name%TYPE DEFAULT NULL
   )
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_report_groups (trg_code, trg_name)
                 VALUES (tqc_trg_code_seq.NEXTVAL, v_trg_name);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting RECORD.');
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_report_groups
               SET trg_name = v_trg_name
             WHERE trg_code = v_trg_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating RECORD');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_report_groups
                  WHERE trg_code = v_trg_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting RECORD.');
         END;
      ELSE
         raise_error ('Invalid Option Selected.');
      END IF;
   END;

   PROCEDURE report_div_groups_prc (
      v_action         IN VARCHAR2 DEFAULT NULL,
      v_rdg_code          tqc_report_div_groups.rdg_code%TYPE DEFAULT NULL,
      v_rdg_trg_code      tqc_report_div_groups.rdg_code%TYPE DEFAULT NULL,
      v_rdg_div_code      tqc_report_div_groups.rdg_div_code%TYPE DEFAULT NULL)
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT
              INTO tqc_report_div_groups (rdg_code, rdg_trg_code, rdg_div_code)
            VALUES (tq_rdg_code_seq.NEXTVAL, v_rdg_trg_code, v_rdg_div_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting RECORD.');
         END;
      ELSIF v_action = 'E'
      THEN
         BEGIN
            UPDATE tqc_report_div_groups
               SET rdg_trg_code = v_rdg_trg_code,
                   rdg_div_code = v_rdg_div_code
             WHERE rdg_code = v_rdg_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating RECORD.');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_report_div_groups
                  WHERE rdg_code = v_rdg_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting RECORD.');
         END;
      ELSE
         raise_error ('Invalid Option Selected.');
      END IF;
   END report_div_groups_prc;

   PROCEDURE system_app_areas_prc (
      v_add_edit          IN VARCHAR2,
      v_saa_code          IN tqc_sys_applicable_areas.saa_code%TYPE DEFAULT NULL,
      v_saa_sys_code      IN tqc_sys_applicable_areas.saa_sys_code%TYPE DEFAULT NULL,
      v_saa_description   IN tqc_sys_applicable_areas.saa_description%TYPE DEFAULT NULL)
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT
              INTO tqc_sys_applicable_areas (saa_code,
                                             saa_sys_code,
                                             saa_description)
            VALUES (tqc_sapa_seq.NEXTVAL, v_saa_sys_code, v_saa_description);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting RECORD.');
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_sys_applicable_areas
               SET saa_sys_code = v_saa_sys_code,
                   saa_description = v_saa_description
             WHERE saa_code = v_saa_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating RECORD.');
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_sys_applicable_areas
                  WHERE saa_code = v_saa_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting RECORD.');
         END;
      ELSE
         raise_error ('Invalid Option Selected.');
      END IF;
   END system_app_areas_prc;

   PROCEDURE tqc_sys_mod_subunits_prc (
      v_add_edit         IN   VARCHAR2,
      v_tsms_code        IN   NUMBER,
      v_tsms_tsm_code    IN   NUMBER,
      v_tsms_sht_desc    IN   VARCHAR2,
      v_tsms_desc        IN   VARCHAR2,
      v_tsms_order       IN   NUMBER,
      v_tsms_prod_code   IN   NUMBER
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_sys_mod_subunits (tsms_code,
                                           tsms_tsm_code,
                                           tsms_sht_desc,
                                           tsms_desc,
                                           tsms_order,
                                           tsms_prod_code)
              VALUES (tqc_tsms_code_seq.NEXTVAL,
                      v_tsms_tsm_code,
                      v_tsms_sht_desc,
                      v_tsms_desc,
                      v_tsms_order,
                      v_tsms_prod_code);
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_sys_mod_subunits
            SET tsms_tsm_code = v_tsms_tsm_code,
                tsms_sht_desc = v_tsms_sht_desc,
                tsms_desc = v_tsms_desc,
                tsms_order = v_tsms_order,
                tsms_prod_code = v_tsms_prod_code
          WHERE tsms_code = v_tsms_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_sys_mod_subunits
               WHERE tsms_code = v_tsms_code;
      END IF;
   END;

   PROCEDURE mod_subunit_det_prc (
      v_add_edit                   IN   VARCHAR2,
      v_tsmsd_code                 IN   NUMBER,
      v_tsmsd_tsms_code            IN   NUMBER,
      v_tsmsd_name                 IN   VARCHAR2,
      v_tsmsd_prompt               IN   VARCHAR2,
      v_tsmsd_type                 IN   VARCHAR2,
      v_tsmsd_order                IN   VARCHAR2,
      v_tsmsd_parent               IN   NUMBER,
      v_tsmsd_more_dtls_appl       IN   VARCHAR2,
      v_tsmsd_more_dtls            IN   VARCHAR2,
      v_tsmsd_root                 IN   VARCHAR2,
      v_tsmsd_more_dtls_required   IN   VARCHAR2,
      v_tmsc_code                  IN   NUMBER
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_sys_mod_subunits_details (tsmsd_code,
                                                   tsmsd_tsms_code,
                                                   tsmsd_name,
                                                   tsmsd_prompt,
                                                   tsmsd_type,
                                                   tsmsd_order,
                                                   tsmsd_parent,
                                                   tsmsd_more_dtls_appl,
                                                   tsmsd_more_dtls,
                                                   tsmsd_root,
                                                   tsmsd_more_dtls_required,
                                                   tsmsd_tmsc_code)
              VALUES (tqc_tsmsd_code_seq.NEXTVAL,
                      v_tsmsd_tsms_code,
                      v_tsmsd_name,
                      v_tsmsd_prompt,
                      v_tsmsd_type,
                      v_tsmsd_order,
                      v_tsmsd_parent,
                      v_tsmsd_more_dtls_appl,
                      v_tsmsd_more_dtls,
                      v_tsmsd_root,
                      v_tsmsd_more_dtls_required,
                      v_tmsc_code);
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_sys_mod_subunits_details
            SET tsmsd_tsms_code = v_tsmsd_tsms_code,
                tsmsd_name = v_tsmsd_name,
                tsmsd_prompt = v_tsmsd_prompt,
                tsmsd_type = v_tsmsd_type,
                tsmsd_order = v_tsmsd_order,
                tsmsd_parent = v_tsmsd_parent,
                tsmsd_more_dtls_appl = v_tsmsd_more_dtls_appl,
                tsmsd_more_dtls = v_tsmsd_more_dtls,
                tsmsd_root = v_tsmsd_root,
                tsmsd_more_dtls_required = v_tsmsd_more_dtls_required,
                tsmsd_tmsc_code = v_tmsc_code
          WHERE tsmsd_code = v_tsmsd_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_sys_mod_subunits_details
               WHERE tsmsd_code = v_tsmsd_code;
      END IF;
   END;

   PROCEDURE sys_subunits_options_prc (
      v_add_edit           IN   VARCHAR2,
      v_tsso_code          IN   tqc_sys_subunits_options.tsso_code%TYPE,
      v_tsso_tsmsd_code    IN   tqc_sys_subunits_options.tsso_tsmsd_code%TYPE,
      v_tsso_option_name   IN   tqc_sys_subunits_options.tsso_option_name%TYPE,
      v_tsso_option_desc   IN   tqc_sys_subunits_options.tsso_option_desc%TYPE,
      v_tsso_order         IN   tqc_sys_subunits_options.tsso_order%TYPE,
      v_tsso_type          IN   tqc_sys_subunits_options.tsso_type%TYPE
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_sys_subunits_options (tsso_code,
                                               tsso_tsmsd_code,
                                               tsso_option_name,
                                               tsso_option_desc,
                                               tsso_order,
                                               tsso_type)
              VALUES (tqc_tsso_code_seq.NEXTVAL,
                      v_tsso_tsmsd_code,
                      v_tsso_option_name,
                      v_tsso_option_desc,
                      v_tsso_order,
                      v_tsso_type);
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_sys_subunits_options
            SET tsso_tsmsd_code = v_tsso_tsmsd_code,
                tsso_option_name = v_tsso_option_name,
                tsso_option_desc = v_tsso_option_desc,
                tsso_order = v_tsso_order,
                tsso_type = v_tsso_type
          WHERE tsso_code = v_tsso_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_sys_subunits_options
               WHERE tsso_code = v_tsso_code;
      END IF;
   END;

   PROCEDURE outgoingmailproc (v_mail_type      VARCHAR2,
                               v_mail_secure    VARCHAR2,
                               v_server         VARCHAR2,
                               v_host           VARCHAR2,
                               v_port           NUMBER,
                               v_username       VARCHAR2,
                               v_password       VARCHAR2,
                               v_email          VARCHAR2)
   IS
      v_count   NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (*)
           INTO v_count
           FROM tqc_system_mails
          WHERE mail_in_out = 'O';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      IF NVL (v_count, 0) = 0
      THEN
         INSERT INTO tqc_system_mails (mail_type,
                                       mail_server_name,
                                       mail_host,
                                       mail_username,
                                       mail_password,
                                       mail_port,
                                       mail_email,
                                       mail_in_out,
                                       mail_secure)
              VALUES (v_mail_type,
                      v_server,
                      v_host,
                      v_username,
                      v_password,
                      v_port,
                      v_email,
                      'O',
                      v_mail_secure);
      ELSE
         UPDATE tqc_system_mails
            SET mail_server_name = v_server,
                mail_host = v_host,
                mail_username = v_username,
                mail_password = v_password,
                mail_port = v_port,
                mail_email = v_email,
                mail_type = v_mail_type,
                mail_secure = v_mail_secure
          WHERE mail_in_out = 'O';
      END IF;
   END outgoingmailproc;

   PROCEDURE incomingmailproc (
      v_mail_secure   VARCHAR2,
      v_server        VARCHAR2,
      v_host          VARCHAR2,
      v_port          NUMBER,
      v_username      VARCHAR2,
      v_password      VARCHAR2,
      v_type          VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (*)
           INTO v_count
           FROM tqc_system_mails
          WHERE mail_in_out = 'I';
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      IF NVL (v_count, 0) = 0
      THEN
         INSERT INTO tqc_system_mails (mail_type,
                                       mail_server_name,
                                       mail_host,
                                       mail_username,
                                       mail_password,
                                       mail_port,
                                       mail_email,
                                       mail_in_out,
                                       mail_secure)
              VALUES (v_type,
                      v_server,
                      v_host,
                      v_username,
                      v_password,
                      v_port,
                      NULL,
                      'I',
                      v_mail_secure);
      ELSE
         UPDATE tqc_system_mails
            SET mail_server_name = v_server,
                mail_host = v_host,
                mail_username = v_username,
                mail_password = v_password,
                mail_port = v_port,
                mail_type = v_type,
                mail_secure = v_mail_secure
          WHERE mail_in_out = 'I';
      END IF;
   END incomingmailproc;

   PROCEDURE create_alerts (
      v_job_name          IN   VARCHAR2,
      v_job_desc          IN   VARCHAR2,
      v_start_time        IN   DATE,
      v_end_time          IN   DATE,
      v_code              IN   NUMBER,
      v_sys_code          IN   NUMBER,
      v_recurrence_type   IN   VARCHAR2,
      v_assignee          IN   NUMBER,
      v_notified_fail     IN   NUMBER,
      v_notified_succ     IN   NUMBER,
      v_job_type          IN   VARCHAR2,
      v_job_template      IN   VARCHAR2,
      v_fail_template     IN   VARCHAR2,
      v_succ_template     IN   VARCHAR2,
      v_add_edit          IN   VARCHAR2,
      v_cron_expr         IN   VARCHAR2,
      v_status            IN   VARCHAR2,
      v_repeat            IN   VARCHAR2,
      v_threshtype        IN   VARCHAR2,
      v_threshval         IN   NUMBER
   )
   IS
      v_pcode   NUMBER;
      v_count   NUMBER;
   BEGIN

     IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM qrtz_triggers
             WHERE qt_job_name = v_job_name;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         IF NVL (v_count, 0) = 0
         THEN
            SELECT tqc_qt_code_seq.NEXTVAL
              INTO v_pcode
              FROM DUAL;

            INSERT INTO qrtz_triggers (qt_job_name,
                                       qt_description,
                                       qt_next_fire_time,
                                       qt_prev_fire_time,
                                       qt_start_time,
                                       qt_end_time,
                                       qt_code,
                                       qt_sys_code,
                                       qt_recurrence,
                                       qt_recurrence_type,
                                       qt_job_assignee,
                                       qt_notified_fail_user,
                                       qt_notified_succ_user,
                                       qt_reccurence_interval,
                                       qt_job_type,
                                       qt_job_template,
                                       qt_fail_notify_template,
                                       qt_succ_notify_template,
                                       qt_cron_expression,
                                       qt_status,
                                       qt_threshold_type,
                                       qt_threshold_value)
                 VALUES (v_job_name,
                         v_job_desc,
                         NULL,
                         NULL,
                         v_start_time,
                         v_end_time,
                         v_pcode,
                         v_sys_code,
                         v_repeat,
                         v_recurrence_type,
                         v_assignee,
                         v_notified_fail,
                         v_notified_succ,
                         NULL,
                         v_job_type,
                         v_job_template,
                         v_fail_template,
                         v_succ_template,
                         v_cron_expr,
                         v_status,
                         v_threshtype,
                         v_threshval);
         ELSE
            raise_error ('Job Name Already Exists');
         END IF;
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE qrtz_triggers
            SET qt_job_name = v_job_name,
                qt_description = v_job_desc,
                qt_start_time = v_start_time,
                qt_end_time = v_end_time,
                qt_sys_code = v_sys_code,
                qt_recurrence = v_repeat,
                qt_recurrence_type = v_recurrence_type,
                qt_job_assignee = v_assignee,
                qt_notified_fail_user = v_notified_fail,
                qt_notified_succ_user = v_notified_succ,
                qt_reccurence_interval = NULL,
                qt_job_type = NVL (v_job_type, qt_job_type),
                qt_job_template = v_job_template,
                qt_fail_notify_template = v_fail_template,
                qt_succ_notify_template = v_succ_template,
                qt_cron_expression = v_cron_expr,
                qt_status = v_status,
                qt_threshold_type = v_threshtype,
                qt_threshold_value = v_threshval
          WHERE qt_code = v_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM qrtz_triggers
               WHERE qt_code = v_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Schedule' || SQLERRM);
   END create_alerts;

   PROCEDURE households_prc (
      v_add_edit   VARCHAR2,
      v_code       tqc_households.hh_id%TYPE,
      v_name       tqc_households.hh_name%TYPE,
      v_category   tqc_households.hh_category%TYPE,
      v_usrcode    NUMBER
   )
   IS
      v_count   NUMBER;
      v_id      NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (*)
              INTO v_count
              FROM tqc_households
             WHERE hh_name = v_name;

            IF v_count < 1
            THEN
               INSERT INTO tqc_households (hh_id,
                                           hh_name,
                                           hh_created_by,
                                           hh_date_created,
                                           hh_category)
                    VALUES (tq_crm.tqc_hh_code_seq.NEXTVAL,
                            v_name,
                            v_usrcode,
                            SYSDATE,
                            v_category);
            ELSE
               raise_error
                   ('The HouseHold Name Already exist::Enter Different Name ');
            END IF;
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
            UPDATE tqc_households
               SET hh_name = v_name,
                   hh_category = v_category
             WHERE hh_id = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_households
                  WHERE hh_id = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END households_prc;

   PROCEDURE householdmembers_prc (
      v_add_edit    VARCHAR2,
      v_code        tqc_households.hh_id%TYPE,
      v_clnt_code   tqc_clients.clnt_code%TYPE
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_household_dtls
                        (hhd_id, hhd_hh_id, hhd_clnt_code
                        )
                 VALUES (tqc_hhd_code_seq.NEXTVAL, v_code, v_clnt_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_household_dtls
                  WHERE hhd_hh_id = v_code AND hhd_clnt_code = v_clnt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END householdmembers_prc;

   PROCEDURE agencies_editaccounttype (
      v_agn_code       IN   tqc_agencies.agn_code%TYPE,
      v_agn_act_code   IN   tqc_agencies.agn_act_code%TYPE
   )
   IS
   BEGIN
      -- RAISE_ERROR('v_AGN_CODE='||v_AGN_CODE);
      UPDATE tqc_agencies
         SET agn_act_code = v_agn_act_code
       WHERE agn_code = v_agn_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error
                    ('ERROR OCCURED WHILE TRYING TO EDIT AGENCY ACCOUNT TYPE');
   END;

   PROCEDURE ecm_setups_prc (
      v_est_code           tqc_ecm_setups.est_code%TYPE,
      v_est_ecm_url        tqc_ecm_setups.est_ecm_url%TYPE,
      v_est_ecm_username   tqc_ecm_setups.est_ecm_username%TYPE,
      v_est_ecm_password   tqc_ecm_setups.est_ecm_password%TYPE,
      v_est_sys_code       tqc_ecm_setups.est_sys_code%TYPE,
      v_est_root_folder    tqc_ecm_setups.est_root_folder%TYPE,
      v_add_edit           VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         SELECT COUNT (1)
           INTO v_count
           FROM tqc_ecm_setups
          WHERE est_sys_code = v_est_sys_code;

         IF v_count > 0
         THEN
            raise_error ('Set up for  the system has already been done');
         END IF;

         INSERT INTO tqc_ecm_setups
                     (est_code, est_ecm_url,
                      est_ecm_username, est_ecm_password, est_sys_code,
                      est_root_folder
                     )
              VALUES (tqc_est_code_seq.NEXTVAL, v_est_ecm_url,
                      v_est_ecm_username, v_est_ecm_password, v_est_sys_code,
                      v_est_root_folder
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_ecm_setups
            SET est_ecm_url = v_est_ecm_url,
                est_ecm_username = v_est_ecm_username,
                est_ecm_password = v_est_ecm_password,
                est_sys_code = v_est_sys_code,
                est_root_folder = v_est_root_folder
          WHERE est_code = v_est_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_ecm_setups
               WHERE est_code = v_est_code;
      END IF;
   END;

   PROCEDURE attach_ecm_reports (
      v_add_edit         VARCHAR2,
      v_sprr_code        NUMBER,
      v_sprr_rpt_code    NUMBER,
      v_sprr_sprc_code   NUMBER,
      v_sprr_desc        VARCHAR2,
      v_sprr_type        VARCHAR2,
      v_doc_type_code    NUMBER
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_sys_process_reports (sprr_code,
                                              sprr_rpt_code,
                                              sprr_sprc_code,
                                              sprr_desc,
                                              sprr_type,
                                              sprr_sdt_code)
              VALUES (tqc_sprr_code_seq.NEXTVAL,
                      v_sprr_rpt_code,
                      v_sprr_sprc_code,
                      v_sprr_desc,
                      v_sprr_type,
                      v_doc_type_code);
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_sys_process_reports
            SET sprr_rpt_code = v_sprr_rpt_code,
                sprr_sprc_code = v_sprr_sprc_code,
                sprr_desc = v_sprr_desc,
                sprr_type = v_sprr_type,
                sprr_sdt_code = NVL (v_doc_type_code, sprr_sdt_code)
          WHERE sprr_code = v_sprr_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_sys_process_reports
               WHERE sprr_code = v_sprr_code;
      END IF;
   END;

   PROCEDURE ecm_doc_types (
      v_sdt_code           NUMBER,
      v_content_type       VARCHAR2,
      v_sdt_content_name   VARCHAR2,
      v_sdt_content_desc   VARCHAR2,
      v_add_edit           VARCHAR2
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_sys_doc_types (sdt_code,
                                        sdt_content_type,
                                        sdt_content_name,
                                        sdt_content_desc)
              VALUES (tqc_sprr_code_seq.NEXTVAL,
                      v_content_type,
                      v_sdt_content_name,
                      v_sdt_content_desc);
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_sys_doc_types
            SET sdt_content_type = v_content_type,
                sdt_content_name = v_sdt_content_name,
                sdt_content_desc = v_sdt_content_desc
          WHERE sdt_code = v_sdt_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_sys_doc_types
               WHERE sdt_code = v_sdt_code;
      END IF;
   END;

   PROCEDURE ecm_metadata (
      v_ddm_code       NUMBER,
      v_ddm_sdt_code   NUMBER,
      v_ddm_name       VARCHAR2,
      v_ddm_desc       VARCHAR2,
      v_add_edit       VARCHAR2
   )
   IS
   BEGIN
      --RAISE_ERROR('v_ddm_code'||v_ddm_code||'v_add_edit'||v_add_edit);
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_dms_doc_metadata (ddm_code,
                                           ddm_sdt_code,
                                           ddm_name,
                                           ddm_desc)
              VALUES (tqc_sprr_code_seq.NEXTVAL,
                      v_ddm_sdt_code,
                      v_ddm_name,
                      v_ddm_desc);
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_dms_doc_metadata
            SET ddm_sdt_code = v_ddm_sdt_code,
                ddm_name = v_ddm_name,
                ddm_desc = v_ddm_desc
          WHERE ddm_code = v_ddm_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_dms_doc_metadata
               WHERE ddm_code = v_ddm_code;
      END IF;
   END;

   PROCEDURE bank_branch_details (
      v_add_edit           VARCHAR2,
      v_bbb_code           NUMBER,
      v_bbb_brn_code       NUMBER,
      v_bbb_brn_reg_code   VARCHAR2,
      v_bbb_brn_name       VARCHAR2,
      v_bbb_bbr_code       NUMBER,
      v_bbb_bbr_bnk_code   NUMBER
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_bank_branches_branches (bbb_code,
                                                 bbb_brn_code,
                                                 bbb_brn_reg_code,
                                                 bbb_brn_name,
                                                 bbb_bbr_code,
                                                 bbb_bbr_bnk_code)
              VALUES (gin_bbb_code_seq.NEXTVAL,
                      v_bbb_brn_code,
                      v_bbb_brn_reg_code,
                      v_bbb_brn_name,
                      v_bbb_bbr_code,
                      v_bbb_bbr_bnk_code);
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_bank_branches_branches
            SET -- BBB_CODE=v_bbb_code,
                bbb_brn_code = v_bbb_brn_code,
                bbb_brn_reg_code = v_bbb_brn_reg_code,
                bbb_brn_name = v_bbb_brn_name,
                bbb_bbr_code = v_bbb_bbr_code,
                bbb_bbr_bnk_code = v_bbb_bbr_bnk_code
          WHERE bbb_code = v_bbb_code;
      END IF;
   END;

   PROCEDURE smsstatus (
      v_status                   VARCHAR2,
      v_sms_code                 NUMBER DEFAULT NULL,
      v_sms_sent_response   IN   VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      UPDATE tqc_sms_messages
            SET sms_status = v_status,
                sms_send_date = SYSDATE,
                sms_sent_response = v_sms_sent_response
       WHERE sms_code = v_sms_code;
   END;

   PROCEDURE escalate_proc (
      v_taskid                      NUMBER,
      v_syscode                     NUMBER,
      v_processdefinitionid         VARCHAR2,
      v_taskname                    VARCHAR2,
      v_duration              OUT   NUMBER,
      v_assignee              OUT   VARCHAR2
   )
   IS
      v_count      NUMBER;
      v_level      NUMBER;
      v_jpdlname   VARCHAR2 (50);
   BEGIN
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_task_escalations
       WHERE tte_task_id = v_taskid;

      SELECT objname_
        INTO v_jpdlname
        FROM jbpm4_deployprop
       WHERE stringval_ = v_processdefinitionid;

      IF NVL (v_count, 0) = 0
      THEN
         BEGIN
            SELECT tsel_assignee, tsel_duration
              INTO v_assignee, v_duration
              FROM tqc_sys_escalation_levels
             WHERE     tsel_jsd_sys_code = v_syscode
                   AND tsel_jsd_jpdl_name = v_jpdlname
                   AND tsel_activity_name = v_taskname
                   AND tsel_level = 1;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         INSERT INTO tqc_task_escalations (tte_task_id,
                                           tte_jpdl_name,
                                           tte_activity_name,
                                           tte_level,
                                           tte_assignee)
              VALUES (v_taskid,
                      v_jpdlname,
                      v_taskname,
                      1,
                      v_assignee);
      ELSE
         BEGIN
            SELECT MAX (tte_level)
              INTO v_level
              FROM tqc_task_escalations
             WHERE tte_task_id = v_taskid;

            SELECT tsel_assignee, tsel_duration
              INTO v_assignee, v_duration
              FROM tqc_sys_escalation_levels
             WHERE tsel_jsd_sys_code = v_syscode
               AND tsel_jsd_jpdl_name = v_jpdlname
               AND tsel_activity_name = v_taskname
               AND tsel_level = v_level;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         IF NVL (v_level, 1) = 4
         THEN
            NULL;
         ELSE
            v_level := v_level + 1;

            INSERT INTO tqc_task_escalations (tte_task_id,
                                              tte_jpdl_name,
                                              tte_activity_name,
                                              tte_level,
                                              tte_assignee)
                 VALUES (v_taskid,
                         v_jpdlname,
                         v_taskname,
                         v_level,
                         v_assignee);
         END IF;
      END IF;

      IF v_duration IS NOT NULL
      THEN
         INSERT INTO jbpm4_job (dbid_,
                                class_,
                                dbversion_,
                                duedate_,
                                state_,
                                isexclusive_,
                                retries_,
                                processinstance_,
                                execution_,
                                event_)
            SELECT hibernate_sequence.NEXTVAL,
                   'Timer',
                   dbversion_,
                   (SYSDATE + v_duration),
                   'waiting',
                   0,
                   3,
                   procinst_,
                   execution_,
                   'timeout'
              FROM jbpm4_task
             WHERE dbid_ = v_taskid;
      END IF;
   END escalate_proc;

   PROCEDURE createbankbranches (
      v_addedit              VARCHAR2,
      v_tcb_code        IN   NUMBER,
      v_tcb_clnt_code   IN   NUMBER,
      v_tcb_sht_desc    IN   VARCHAR2,
      v_tcb_name        IN   VARCHAR
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_client_branches (tcb_code,
                                          tcb_clnt_code,
                                          tcb_sht_desc,
                                          tcb_name)
              VALUES (tqc_tcb_code_seq.NEXTVAL,
                      v_tcb_clnt_code,
                      v_tcb_sht_desc,
                      v_tcb_name);
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_client_branches
            SET tcb_sht_desc = v_tcb_sht_desc,
                tcb_name = v_tcb_name
          WHERE tcb_code = v_tcb_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_client_branches
               WHERE tcb_code = v_tcb_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error  Manipulating Branch details '
                      || SQLERRM (SQLCODE)
                     );
   END createbankbranches;

   PROCEDURE assignbanktoclient (
      v_addedit         IN   VARCHAR2,
      v_tcb_acwa_code   IN   NUMBER,
      v_tcb_code        IN   NUMBER
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_client_usr_branches
                     (tcub_code, tcub_acwa_code, tcub_tcb_code
                     )
              VALUES (tqc_tcub_code_seq.NEXTVAL, v_tcb_acwa_code, v_tcb_code
                     );
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_client_usr_branches
               WHERE tcub_tcb_code = v_tcb_code;
      --AND TCUB_ACWA_CODE=v_tcb_acwa_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error  Updating Bank Details ' || SQLERRM (SQLCODE));
   END assignbanktoclient;

   PROCEDURE assigndefaultbranch (v_tcub_tcb_code IN NUMBER)
   IS
      v_count   NUMBER;
   BEGIN
      UPDATE tqc_client_usr_branches
         SET tcub_default = 'Y'
       WHERE tcub_tcb_code = v_tcub_tcb_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error  Updating Bank Details ' || SQLERRM (SQLCODE));
   END assigndefaultbranch;

  PROCEDURE createwebproductdetails (
      v_addedit                VARCHAR2,
      v_twpd_clnt_code    IN   NUMBER,
      v_twpd_twp_code     IN   NUMBER,
      v_twpd_usr_code     IN   NUMBER,
      v_twpd_username     IN   VARCHAR2,
      v_twpd_dr_limit     IN   NUMBER,
      v_twpd_cr_limit     IN   NUMBER,
      v_twpd_policy_use   IN   VARCHAR2,
      v_twpd_endos_use    IN   VARCHAR2,
      v_twpd_endos_account IN VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_web_product_details
                     (twpd_clnt_code, twpd_twp_code, twpd_usr_code,
                      twpd_username, twpd_dr_limit, twpd_cr_limit,
                      twpd_policy_use, twpd_endos_use
                     )
              VALUES (v_twpd_clnt_code, v_twpd_twp_code, v_twpd_usr_code,
                      v_twpd_username, v_twpd_dr_limit, v_twpd_cr_limit,
                      v_twpd_policy_use, v_twpd_endos_use
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_web_product_details
            SET twpd_twp_code = v_twpd_twp_code,
                twpd_usr_code = v_twpd_usr_code,
                twpd_username = v_twpd_username,
                twpd_dr_limit = v_twpd_dr_limit,
                twpd_cr_limit = v_twpd_cr_limit,
                twpd_policy_use = v_twpd_policy_use,
                twpd_endos_use = v_twpd_endos_use
          WHERE twpd_clnt_code = v_twpd_clnt_code
            AND twpd_twp_code = v_twpd_twp_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_web_product_details
               WHERE twpd_clnt_code = v_twpd_clnt_code
                 AND twpd_twp_code = v_twpd_twp_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error  Manipulating Product details '
                      || SQLERRM (SQLCODE)
                     );
   END createwebproductdetails;  

   PROCEDURE outgoingsmsproc (
      v_addedit      VARCHAR2,
      v_tsscode      tqc_system_sms.tss_code%TYPE,
      v_tssdesc      tqc_system_sms.tss_desc%TYPE,
      v_tssurls      tqc_system_sms.tss_url%TYPE,
      v_username     tqc_system_sms.tss_username%TYPE,
      v_password     tqc_system_sms.tss_password%TYPE,
      v_source       tqc_system_sms.tss_source%TYPE,
      v_tssdefault   tqc_system_sms.tss_default%TYPE
   )
   IS
   BEGIN
      IF NVL (v_tssdefault, 'N') = 'Y'
      THEN
         UPDATE tqc_system_sms
            SET tss_default = 'N';
      END IF;

      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_system_sms (tss_code,
                                     tss_desc,
                                     tss_url,
                                     tss_username,
                                     tss_password,
                                     tss_source,
                                     tss_default)
              VALUES (tss_code_seq.NEXTVAL,
                      v_tssdesc,
                      v_tssurls,
                      v_username,
                      v_password,
                      v_source,
                      v_tssdefault);
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_system_sms
            SET tss_desc = v_tssdesc,
                tss_url = v_tssurls,
                tss_username = v_username,
                tss_password = v_password,
                tss_source = v_source,
                tss_default = v_tssdefault
          WHERE tss_code = v_tsscode;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_system_sms
               WHERE tss_code = v_tsscode;
      END IF;
   END outgoingsmsproc;

   PROCEDURE getrequireddocs (
      v_add_edit        IN   VARCHAR2,
      v_rqd_code        IN   NUMBER,
      v_rqd_spt_code    IN   NUMBER,
      v_rqd_spta_code   IN   NUMBER,
      v_rqd_sys_code    IN   NUMBER,
      v_rqc_rdoc_id     IN   NUMBER
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_required_docs (rqd_code,
                                           rqd_spt_code,
                                           rqd_spta_code,
                                           rqd_sys_code,
                                           rqc_rdoc_id)
                 VALUES (tqc_rqd_code_seq.NEXTVAL,
                         v_rqd_spt_code,
                         v_rqd_spta_code,
                         v_rqd_sys_code,
                         v_rqc_rdoc_id);
         EXCEPTION
            WHEN OTHERS
            THEN
               NULL;
         END;
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_required_docs
            SET rqd_spt_code = v_rqd_spt_code,
                rqd_spta_code = v_rqd_spta_code,
                rqd_sys_code = v_rqd_sys_code,
                rqc_rdoc_id = v_rqc_rdoc_id
          WHERE rqd_code = v_rqd_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_required_docs
               WHERE rqd_code = v_rqd_code;
      END IF;
   END;

   PROCEDURE getcountrypref (
      v_cou_name            IN       VARCHAR2,
      v_cou_mobile_prefix   OUT      NUMBER,
      v_cou_client_number   OUT      NUMBER
   )
   IS
   BEGIN
      --RAISE_ERROR('v_cou_name'||v_cou_name);
      SELECT cou_mobile_prefix, cou_client_number
        INTO v_cou_mobile_prefix, v_cou_client_number
        FROM tqc_countries
       WHERE cou_name = v_cou_name;
   END;

   PROCEDURE escalations_proc (
      v_addedit    VARCHAR2,
      v_code       tqc_sys_escalation_levels.tsel_code%TYPE,
      v_syscode    tqc_sys_escalation_levels.tsel_jsd_sys_code%TYPE,
      v_jpdlname   tqc_sys_escalation_levels.tsel_jsd_jpdl_name%TYPE,
      v_activity   tqc_sys_escalation_levels.tsel_activity_name%TYPE,
      v_level      tqc_sys_escalation_levels.tsel_level%TYPE,
      v_user       tqc_sys_escalation_levels.tsel_assignee%TYPE,
      v_duration   tqc_sys_escalation_levels.tsel_duration%TYPE,
      v_cc         tqc_sys_escalation_levels.tsel_cc%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      --RAISE_ERROR('ERROR '||v_addEdit||' v_code '||v_code||' v_level '||v_level);
      IF v_addedit = 'A'
      THEN
         SELECT COUNT (*)
           INTO v_count
           FROM tqc_sys_escalation_levels
          WHERE tsel_jsd_sys_code = v_syscode
            AND tsel_jsd_jpdl_name = v_jpdlname
            AND tsel_activity_name = v_activity
            AND tsel_level = v_level;

         IF NVL (v_count, 0) > 0
         THEN
            raise_error (   'ESCALATION LEVEL ('
                         || v_level
                         || ') FOR ACTIVITY PROCESS ALREADY DEFINED'
                        );
         END IF;

         ---raise_error(' v_syscode: '||v_syscode|| 'v_jpdlname: '|| v_jpdlname);
         INSERT INTO tqc_sys_escalation_levels (tsel_code,
                                                tsel_jsd_sys_code,
                                                tsel_jsd_jpdl_name,
                                                tsel_activity_name,
                                                tsel_level,
                                                tsel_assignee,
                                                tsel_duration,
                                                tsel_cc)
              VALUES (tsel_code_seq.NEXTVAL,
                      v_syscode,
                      v_jpdlname,
                      v_activity,
                      v_level,
                      v_user,
                      v_duration,
                      v_cc);
      ELSIF v_addedit = 'E'
      THEN
         SELECT COUNT (*)
           INTO v_count
           FROM tqc_sys_escalation_levels
          WHERE     tsel_jsd_sys_code = v_syscode
                AND tsel_jsd_jpdl_name = v_jpdlname
                AND tsel_activity_name = v_activity
                AND tsel_level = v_level
                AND tsel_code <> v_code;

         IF NVL (v_count, 0) > 0
         THEN
            raise_error (
                  'ESCALATION LEVEL ('
               || v_level
               || ') FOR ACTIVITY PROCESS ALREADY DEFINED');
         END IF;

         UPDATE tqc_sys_escalation_levels
            SET tsel_level = v_level,
                tsel_assignee = v_user,
                tsel_duration = v_duration,
                tsel_cc = v_cc
          WHERE tsel_code = v_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_sys_escalation_levels
               WHERE tsel_code = v_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error
             (   'ERROR OCCURED WHILE TRYING TO MANIPULATE ESCALATION LEVELS'
              || SQLERRM (SQLCODE)
             );
   END escalations_proc;

   PROCEDURE reservedwords_proc (
      v_addedit    VARCHAR2,
      v_code       tqc_sys_reserved_words.tsrw_code%TYPE,
      v_syscode    tqc_sys_reserved_words.tsrw_sys_code%TYPE,
      v_tsrccode   tqc_sys_reserved_words.tsrw_tsrc_code%TYPE,
      v_type       tqc_sys_reserved_words.tsrw_type%TYPE,
      v_name       tqc_sys_reserved_words.tsrw_name%TYPE,
      v_desc       tqc_sys_reserved_words.tsrw_desc%TYPE
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_sys_reserved_words (tsrw_code,
                                             tsrw_sys_code,
                                             tsrw_tsrc_code,
                                             tsrw_type,
                                             tsrw_name,
                                             tsrw_desc)
              VALUES (tsrw_code_seq.NEXTVAL,
                      v_syscode,
                      v_tsrccode,
                      v_type,
                      v_name,
                      v_desc);
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_sys_reserved_words
            SET tsrw_tsrc_code = v_tsrccode,
                tsrw_type = v_type,
                tsrw_name = v_name,
                tsrw_desc = v_desc
          WHERE tsrw_code = v_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_sys_reserved_words
               WHERE tsrw_code = v_code;
      END IF;
   END reservedwords_proc;

   PROCEDURE obtainclientnumber (
      v_clnt_code      IN       NUMBER,
      v_clnt_sms_tel   OUT      VARCHAR2
   )
   IS
   BEGIN
      SELECT clnt_sms_tel
        INTO v_clnt_sms_tel
        FROM tqc_clients
       WHERE clnt_code = v_clnt_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
   END;

   PROCEDURE addupdatecountry (
      v_addedit             VARCHAR2,
      v_tcou_code      IN   NUMBER,
      v_cou_sht_desc   IN   VARCHAR2,
      v_cou_name       IN   VARCHAR2
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_countries (cou_code,
                                    cou_sht_desc,
                                    cou_name,
                                    cou_nationality)
              VALUES (tsrw_code_seq.NEXTVAL,
                      v_cou_sht_desc,
                      v_cou_name,
                      'KENYANESE');
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_countries
            SET cou_sht_desc = v_cou_sht_desc,
                cou_name = v_cou_name
          WHERE cou_code = v_tcou_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_countries
               WHERE cou_code = v_tcou_code;
      END IF;
   END addupdatecountry;

   PROCEDURE authorizeaccount (v_user IN VARCHAR2, v_agn_code IN NUMBER)
   IS
   BEGIN
      UPDATE tqc_agencies
         SET agn_authorised = 'Y',
             agn_authorised_by = v_user,
             agn_authorised_date = SYSDATE
       WHERE agn_code = v_agn_code;
   END;

   PROCEDURE addreportsubmodule (
      v_addedit             VARCHAR2,
      v_rsm_code       IN   NUMBER,
      v_rsm_name       IN   VARCHAR2,
      v_rsm_desc       IN   VARCHAR2,
      v_rsm_srm_code   IN   NUMBER
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_sys_rpt_sub_modules (rsm_code,
                                              rsm_name,
                                              rsm_desc,
                                              rsm_srm_code)
              VALUES (tqc_rsm_code_seq.NEXTVAL,
                      v_rsm_name,
                      v_rsm_desc,
                      v_rsm_srm_code);
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_sys_rpt_sub_modules
            SET rsm_name = v_rsm_name,
                rsm_desc = v_rsm_desc,
                rsm_srm_code = v_rsm_srm_code
          WHERE rsm_code = v_rsm_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_sys_rpt_sub_modules
               WHERE rsm_code = v_rsm_code;
      END IF;
   END addreportsubmodule;

   PROCEDURE assignreport (v_rpt_code IN NUMBER, v_rpt_rsm_code IN NUMBER)
   IS
   BEGIN
--RAISE_ERROR('TEST'||v_rpt_code||'v_rpt_rsm_code'||v_rpt_rsm_code);
      UPDATE tqc_system_reports
         SET rpt_rsm_code = v_rpt_rsm_code
       WHERE rpt_code = v_rpt_code;
   END;

   PROCEDURE unassignreport (v_rpt_code IN NUMBER, v_rpt_rsm_code IN NUMBER)
   IS
   BEGIN
      UPDATE tqc_system_reports
         SET rpt_rsm_code = NULL
       WHERE rpt_code = v_rpt_code;
   END;

   PROCEDURE updatereportdetails (
      v_rpt_code          IN   NUMBER,
      v_rpt_description   IN   VARCHAR2
   )
   IS
   BEGIN
--raise_error('v_rpt_code'||v_rpt_code);
      UPDATE tqc_system_reports
         SET rpt_description = v_rpt_description
       WHERE rpt_code = v_rpt_code;
   END;

   PROCEDURE occupations_prc (
      v_add_edit       IN   VARCHAR2,
      v_occ_code       IN   tqc_occupations.occ_code%TYPE,
      v_occ_sht_desc   IN   tqc_occupations.occ_sht_desc%TYPE,
      v_occ_name       IN   tqc_occupations.occ_name%TYPE,
      v_occ_sec_code   IN   NUMBER
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_occupations
             WHERE occ_code = v_occ_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_occupations (occ_code,
                                         occ_sec_code,
                                         occ_sht_desc,
                                         occ_name)
                 VALUES (tqc_occ_code_seq.NEXTVAL,
                         v_occ_sec_code,
                         v_occ_sht_desc,
                         v_occ_name);
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
            UPDATE tqc_occupations
               SET occ_sht_desc = v_occ_sht_desc,
                   occ_name = v_occ_name
             WHERE occ_code = v_occ_code;
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
            DELETE FROM tqc_occupations
                  WHERE occ_code = v_occ_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE updateentities (
      v_add_edit               IN       VARCHAR2,
      v_ent_code               IN OUT   NUMBER,
      v_ent_sht_desc           IN       VARCHAR2,
      v_ent_name               IN       VARCHAR2,
      v_ent_physical_address   IN       VARCHAR2,
      v_ent_postal_address     IN       VARCHAR2,
      v_ent_twn_code           IN       NUMBER,
      v_ent_cou_code           IN       NUMBER,
      v_ent_email_address      IN       VARCHAR2,
      v_ent_fax                IN       VARCHAR2,
      v_ent_status             IN       VARCHAR2,
      v_ent_bbr_code           IN       NUMBER,
      v_ent_bank_acc_no        IN       VARCHAR2,
      v_ent_zip                IN       VARCHAR2,
      v_ent_sms_tel            IN       VARCHAR2,
      v_ent_created_by         IN       VARCHAR2,
      v_ent_date_created       IN       DATE,
      v_ent_pin                IN       VARCHAR2,
      v_ent_tel1               IN       VARCHAR2,
      v_ent_acc_no             IN       VARCHAR2,
      v_ent_status_remarks     IN       VARCHAR2,
      v_ent_brn_code           IN       NUMBER,
      v_ent_id_no              IN       VARCHAR2,
      v_ent_tel2               IN       VARCHAR2,
      v_ent_sec_code           IN       NUMBER,
      v_ent_runoff             IN       VARCHAR2,
      v_ent_old_acc_no         IN       VARCHAR2,
      v_ent_title              IN       VARCHAR2,
      v_ent_wef                IN       DATE,
      v_ent_wet                IN       DATE,
      v_ent_contact_person     IN       VARCHAR2,
      v_source                 IN       VARCHAR2
   )
   IS
      v_count          NUMBER;
      v_ent_code_val   VARCHAR2 (200);
      vclntcode        NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT TO_NUMBER (TO_CHAR (SYSDATE, 'YY'))
                   || tqc_clnt_code_seq.NEXTVAL
              INTO vclntcode
              FROM DUAL;

            v_ent_code_val := gin_ent_code_seq.NEXTVAL;

            INSERT INTO tqc_entities (ent_code,
                                      ent_sht_desc,
                                      ent_name,
                                      ent_physical_address,
                                      ent_postal_address,
                                      ent_twn_code,
                                      ent_cou_code,
                                      ent_email_address,
                                      ent_fax,
                                      ent_status,
                                      ent_bbr_code,
                                      ent_bank_acc_no,
                                      ent_zip,
                                      ent_sms_tel,
                                      ent_created_by,
                                      ent_date_created,
                                      ent_pin,
                                      ent_tel1,
                                      ent_acc_no,
                                      ent_status_remarks,
                                      ent_brn_code,
                                      ent_id_no,
                                      ent_tel2,
                                      ent_sec_code,
                                      ent_runoff,
                                      ent_old_acc_no,
                                      ent_title,
                                      ent_wef,
                                      ent_wet,
                                      ent_contact_person)
                 VALUES (v_ent_code_val,
                         v_ent_sht_desc,
                         v_ent_name,
                         v_ent_physical_address,
                         v_ent_postal_address,
                         v_ent_twn_code,
                         v_ent_cou_code,
                         v_ent_email_address,
                         v_ent_fax,
                         v_ent_status,
                         v_ent_bbr_code,
                         v_ent_bank_acc_no,
                         v_ent_zip,
                         v_ent_sms_tel,
                         v_ent_created_by,
                         SYSDATE,
                         v_ent_pin,
                         v_ent_tel1,
                         v_ent_acc_no,
                         v_ent_status_remarks,
                         v_ent_brn_code,
                         v_ent_id_no,
                         v_ent_tel2,
                         v_ent_sec_code,
                         v_ent_runoff,
                         v_ent_old_acc_no,
                         v_ent_title,
                         v_ent_wef,
                         v_ent_wet,
                         v_ent_contact_person);

            IF NVL (v_source, 'N') = 'C'
            THEN
               INSERT INTO tqc_clients (clnt_code,
                                        clnt_sht_desc,
                                        clnt_name,
                                        clnt_wef,
                                        clnt_created_by,
                                        clnt_agnt_status,
                                        clnt_date_created,
                                        clnt_direct_client,
                                        clnt_ent_code)
                    VALUES (vclntcode,
                            v_ent_sht_desc,
                            v_ent_name,
                            NVL (v_ent_wef, SYSDATE),
                            v_ent_created_by,
                            'N',
                            SYSDATE,
                            ' ',
                            v_ent_code_val);
            ELSIF NVL (v_source, 'N') = 'A'
            THEN
               INSERT INTO tqc_agencies (agn_code,
                                         agn_sht_desc,
                                         agn_name,
                                         agn_brn_code,
                                         agn_runoff,
                                         agn_enable_web_edit)
                    VALUES (tqc_agn_code_seq.NEXTVAL,
                            v_ent_sht_desc,
                            v_ent_name,
                            v_ent_brn_code,
                            'N',
                            'N');
            ELSIF NVL (v_source, 'N') = 'S'
            THEN
               INSERT INTO tqc_service_providers (spr_code,
                                                  spr_sht_desc,
                                                  spr_name,
                                                  spr_status)
                    VALUES (tqc_spr_code_seq.NEXTVAL,
                            v_ent_sht_desc,
                            v_ent_name,
                            'A');
            END IF;
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
            UPDATE tqc_entities
               SET ent_code = v_ent_code,
                   ent_sht_desc = v_ent_sht_desc,
                   ent_name = v_ent_name,
                   ent_physical_address = v_ent_physical_address,
                   ent_postal_address = v_ent_postal_address,
                   ent_twn_code = v_ent_twn_code,
                   ent_cou_code = v_ent_cou_code,
                   ent_email_address = v_ent_email_address,
                   ent_fax = v_ent_fax,
                   ent_status = v_ent_status,
                   ent_bbr_code = v_ent_bbr_code,
                   ent_bank_acc_no = v_ent_bank_acc_no,
                   ent_zip = v_ent_zip,
                   ent_sms_tel = v_ent_sms_tel,
                   ent_created_by = v_ent_created_by,
                   ent_date_created = v_ent_date_created,
                   ent_pin = v_ent_pin,
                   ent_tel1 = v_ent_tel1,
                   ent_acc_no = v_ent_acc_no,
                   ent_status_remarks = v_ent_status_remarks,
                   ent_brn_code = v_ent_brn_code,
                   ent_id_no = v_ent_id_no,
                   ent_tel2 = v_ent_tel2,
                   ent_sec_code = v_ent_sec_code,
                   ent_runoff = v_ent_runoff,
                   ent_old_acc_no = v_ent_old_acc_no,
                   ent_title = v_ent_title,
                   ent_wef = v_ent_wef,
                   ent_wet = v_ent_wet,
                   ent_contact_person = v_ent_contact_person
             WHERE ent_code = v_ent_code;
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
            DELETE FROM tqc_entities
                  WHERE ent_code = v_ent_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE save_client_details (
      v_type        VARCHAR2,
      v_clnt_code   NUMBER,
      v_pin_pass    VARCHAR2,
      v_pass        VARCHAR2,
      v_idno        VARCHAR2
   )
   IS
   BEGIN
      --raise_error('v_type '||v_type);
      UPDATE tqc_clients
         SET clnt_passport_no = v_pass,
             clnt_id_reg_no = v_idno,
             clnt_pin = v_pin_pass
       WHERE clnt_code = v_clnt_code;
   END;

--   PROCEDURE create_email_msg (
--      v_clnt_code      IN   NUMBER,
--      v_agn_code       IN   NUMBER,
--      v_quot_code      IN   NUMBER,
--      v_pol_code       IN   NUMBER,
--      v_pol_no         IN   VARCHAR2,
--      v_clm_no         IN   VARCHAR2,
--      v_email_addr     IN   VARCHAR2,
--      v_msg_subj       IN   VARCHAR2,
--      v_msg_text       IN   VARCHAR2,
--      v_sys_code       IN   NUMBER,
--      v_sys_mod        IN   VARCHAR2,
--      v_email_status   IN   VARCHAR2
--   )
--   IS
--      v_user         VARCHAR2 (35)
--            := pkg_global_vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
--      v_email_code   NUMBER;
--   BEGIN
--      SELECT tqc_sms_code_seq.NEXTVAL
--        INTO v_email_code
--        FROM DUAL;

--      INSERT INTO tqc_email_messages (email_code,
--                                      email_sys_code,
--                                      email_sys_module,
--                                      email_clnt_code,
--                                      email_agn_code,
--                                      email_pol_code,
--                                      email_pol_no,
--                                      email_clm_no,
--                                      email_quot_code,
--                                      email_address,
--                                      email_subj,
--                                      email_msg,
--                                      email_status,
--                                      email_prepared_by,
--                                      email_prepared_date,
--                                      email_send_date)
--           VALUES (v_email_code,
--                   v_sys_code,
--                   v_sys_mod,
--                   v_clnt_code,
--                   v_agn_code,
--                   v_pol_code,
--                   v_pol_no,
--                   v_clm_no,
--                   v_quot_code,
--                   v_email_addr,
--                   v_msg_subj,
--                   v_msg_text,
--                   'S',
--                   v_user,
--                   SYSDATE,
--                   SYSDATE);
--   END;

   PROCEDURE create_email_msg (
      v_clnt_code      IN   NUMBER,
      v_agn_code       IN   NUMBER,
      v_quot_code      IN   NUMBER,
      v_pol_code       IN   NUMBER,
      v_pol_no         IN   VARCHAR2,
      v_clm_no         IN   VARCHAR2,
      v_email_addr     IN   VARCHAR2,
      v_msg_subj       IN   VARCHAR2,
      v_msg_text       IN   VARCHAR2,
      v_sys_code       IN   NUMBER,
      v_sys_mod        IN   VARCHAR2,
      v_email_status   IN   VARCHAR2
   )
   IS
      v_user         VARCHAR2 (35)
            := pkg_global_vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
      v_email_code   NUMBER;
   BEGIN
      SELECT tqc_sms_code_seq.NEXTVAL
        INTO v_email_code
        FROM DUAL;

      INSERT INTO tqc_email_messages (email_code,
                                      email_sys_code,
                                      email_sys_module,
                                      email_clnt_code,
                                      email_agn_code,
                                      email_pol_code,
                                      email_pol_no,
                                      email_clm_no,
                                      email_quot_code,
                                      email_address,
                                      email_subj,
                                      email_msg,
                                      email_status,
                                      email_prepared_by,
                                      email_prepared_date,
                                      email_send_date)
           VALUES (v_email_code,
                   v_sys_code,
                   v_sys_mod,
                   v_clnt_code,
                   v_agn_code,
                   v_pol_code,
                   v_pol_no,
                   v_clm_no,
                   v_quot_code,
                   v_email_addr,
                   v_msg_subj,
                   v_msg_text,
                   v_email_status,
                   v_user,
                   SYSDATE,
                   SYSDATE);
   END;


   PROCEDURE update_email_msg (
      v_email_code     IN   NUMBER,
      v_emailaddress   IN   VARCHAR2,
      v_subject        IN   VARCHAR2,
      v_emailmessage   IN   VARCHAR2,
      v_email_status   IN   VARCHAR2
   )
   IS
   BEGIN
      UPDATE tqc_email_messages
         SET email_address = NVL (v_emailaddress, email_address),
             email_subj = NVL (v_subject, email_subj),
             email_msg = NVL (v_emailmessage, email_msg),
             email_status = v_email_status,
             email_send_date = SYSDATE
       WHERE email_code = v_email_code;
   END;

   PROCEDURE bussiness_person_proc (
      v_add_edit             IN   VARCHAR2,
      v_bpn_code             IN   NUMBER,
      v_bpn_id_no            IN   VARCHAR2,
      v_bpn_address          IN   VARCHAR2,
      v_bpn_tel              IN   VARCHAR2,
      v_bpn_mobile_no        IN   VARCHAR2,
      v_bpn_email            IN   VARCHAR2,
      v_bpn_type             IN   VARCHAR2,
      v_bpn_zip              IN   VARCHAR2,
      v_bpn_town             IN   VARCHAR2,
      v_bpn_cou_code         IN   NUMBER,
      v_bpn_name             IN   VARCHAR2,
      v_bpn_pin              IN   VARCHAR2,
      v_bpn_bbr_code         IN   NUMBER,
      v_bpn_bank_acc_no      IN   VARCHAR2,
      v_bpn_bbr_swift_code   IN   NUMBER,
      v_bpn_clnt_code        IN   NUMBER,
      v_bpn_payee_type       IN   VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
--raise_error('v_bpn_bbr_code'||v_bpn_bbr_code||'v_add_edit'||v_add_edit||'v_bpn_code'||v_bpn_code||'v_bpn_name'||v_bpn_name);
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_business_persons (bpn_code,
                                              bpn_id_no,
                                              bpn_address,
                                              bpn_tel,
                                              bpn_mobile_no,
                                              bpn_email,
                                              bpn_type,
                                              bpn_zip,
                                              bpn_town,
                                              bpn_cou_code,
                                              bpn_name,
                                              bpn_pin,
                                              bpn_bbr_code,
                                              bpn_bank_acc_no,
                                              bpn_bbr_swift_code,
                                              bpn_clnt_code,
                                              bpn_payee_type)
                 VALUES (tqc_bpn_code_seq.NEXTVAL,
                         v_bpn_id_no,
                         v_bpn_address,
                         v_bpn_tel,
                         v_bpn_mobile_no,
                         v_bpn_email,
                         v_bpn_type,
                         v_bpn_zip,
                         v_bpn_town,
                         v_bpn_cou_code,
                         v_bpn_name,
                         v_bpn_pin,
                         v_bpn_bbr_code,
                         v_bpn_bank_acc_no,
                         v_bpn_bbr_swift_code,
                         v_bpn_clnt_code,
                         v_bpn_payee_type);
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
            UPDATE tqc_business_persons
               SET bpn_id_no = v_bpn_id_no,
                   bpn_address = v_bpn_address,
                   bpn_tel = v_bpn_tel,
                   bpn_mobile_no = v_bpn_mobile_no,
                   bpn_email = v_bpn_email,
                   bpn_type = v_bpn_type,
                   bpn_zip = v_bpn_zip,
                   bpn_town = v_bpn_town,
                   bpn_cou_code = v_bpn_cou_code,
                   bpn_name = v_bpn_name,
                   bpn_pin = v_bpn_pin,
                   bpn_bbr_code = v_bpn_bbr_code,
                   bpn_bank_acc_no = v_bpn_bank_acc_no,
                   bpn_bbr_swift_code = v_bpn_bbr_swift_code,
                   bpn_clnt_code = v_bpn_clnt_code,
                   bpn_payee_type = v_bpn_payee_type
             WHERE bpn_code = v_bpn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while updating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_business_persons
                  WHERE bpn_code = v_bpn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while deleting record ' || SQLERRM (SQLCODE));
         END;
      END IF;
   END;

   PROCEDURE convert_lead_to_prospects (v_lds_code IN NUMBER)
   IS
      CURSOR leads_ref
      IS
         SELECT *
           FROM tqc_leads
          WHERE lds_code = v_lds_code;
   BEGIN
      FOR i IN leads_ref
      LOOP
         INSERT INTO tqc_prospects (prs_code,
                                    prs_surname,
                                    prs_physical_addrs,
                                    prs_postal_addrs,
                                    prs_other_names,
                                    prs_tel_no,
                                    prs_mobile_no,
                                    prs_email_addrs,
                                    prs_id_reg_no,
                                    prs_dob,
                                    prs_pin,
                                    prs_twn_code,
                                    prs_cou_code,
                                    prs_postal_code,
                                    prs_lds_code)
              VALUES (tqc_prs_code_seq.NEXTVAL,
                      i.lds_surnname,
                      i.lds_physical_addrs,
                      i.lds_postal_addrs,
                      i.lds_othernames,
                      i.lds_camp_tel,
                      i.lds_mob_tel,
                      i.lds_email_addrs,
                      NULL,
                      NULL,
                      NULL,
                      i.lds_twn_code,
                      i.lds_cou_code,
                      NULL,
                      v_lds_code);
      END LOOP;

      BEGIN
         UPDATE tqc_leads
            SET lds_convert_prospect = 'Y'
          WHERE lds_code = v_lds_code;
      END;
   END;

    PROCEDURE convert_lead_to_client (
      v_lds_code      IN       NUMBER,
      v_clntshtdesc   OUT      VARCHAR2
   )
   IS
      CURSOR leads_ref
      IS
         SELECT *
           FROM tqc_leads
          WHERE lds_code = v_lds_code;

      v_clientcode   NUMBER;
   BEGIN
      FOR i IN leads_ref
      LOOP
         v_clientcode :=
            tqc_clients_pkg.create_client_proc (
               'Y',
               NULL,                      --v_sht_desc            IN VARCHAR2,
               i.lds_surnname,               --v_surname          IN VARCHAR2,
               i.lds_othernames,           --v_othernames         IN VARCHAR2,
               i.lds_surnname || ' ' || i.lds_othernames,
               NULL,                      --v_pin                 IN VARCHAR2,
               i.lds_postal_addrs,        --v_postal_addrs        IN VARCHAR2,
               i.lds_physical_addrs,      --v_physical_addrs      IN VARCHAR2,
               NULL,                      --v_id_reg_no           IN VARCHAR2,
               pkg_global_vars.get_pvarchar2 ('pkg_global_vars.pvg_username'),
               SYSDATE,                       --v_wef                 IN DATE,
               NULL,                          --v_wet                 IN DATE,
               i.lds_title,               --v_title               IN VARCHAR2,
               NULL,                          --v_dob                 IN DATE,
               i.lds_cou_code,              --v_cou_code            IN NUMBER,
               i.lds_twn_code,              --v_twn_code            IN NUMBER,
               NULL,                      --v_zip_code            IN VARCHAR2,
               i.lds_email_addrs,         --v_email_addrs         IN VARCHAR2,
               i.lds_camp_tel,            --v_tel                 IN VARCHAR2,
               i.lds_mob_tel,             --v_sms_tel             IN VARCHAR2,
               i.lds_fax,                 --v_fax                 IN VARCHAR2,
               NULL,                        --v_sec_code            IN NUMBER,
               i.lds_occupation,          --v_business            IN VARCHAR2,
               NULL,                        --v_domicile_countries  IN NUMBER,
               NULL,                      --v_proposer            IN VARCHAR2,
               'A',                       --v_status              IN VARCHAR2,
               NULL,                      --v_runoff              IN VARCHAR2,
               NULL,                      --v_withdrawal_reason   IN VARCHAR2,
               NULL,                      --v_remarks             IN VARCHAR2,
               NULL,                      --v_bank_acc_no         IN VARCHAR2,
               NULL,                        --v_bbr_code            IN NUMBER,
               NULL,                      --v_spcl_terms          IN VARCHAR2,
               NULL,                      --v_policy_cancelled    IN VARCHAR2,
               NULL,                      --v_increased_premium   IN VARCHAR2,
               NULL,                      --v_declined_prop       IN VARCHAR2,
               'I',                       --v_client_type         IN VARCHAR2,
               'A',                       --v_add_edit            IN VARCHAR2,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL             --v_sex                 VARCHAR2 DEFAULT NULL,
                   );
      END LOOP;

      BEGIN
         SELECT clnt_sht_desc
           INTO v_clntshtdesc
           FROM tqc_clients
          WHERE clnt_code = v_clientcode;

         UPDATE tqc_leads
            SET lds_clnt_code = v_clientcode,
                lds_clnt_sht_desc = v_clntshtdesc
          WHERE lds_code = v_lds_code;
      END;
   END;

   PROCEDURE labels_prc (
      v_add_edit       IN   VARCHAR2,
      v_label_code     IN   tqc_labels.label_code%TYPE,
      v_label_name     IN   tqc_labels.label_name%TYPE,
      v_label_value    IN   tqc_labels.label_value%TYPE,
      v_label_status   IN   tqc_labels.label_status%TYPE,
      v_label_desc     IN   tqc_labels.label_desc%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_labels
             WHERE label_code = v_label_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_labels (label_code,
                                    label_name,
                                    label_value,
                                    label_status,
                                    label_desc)
                 VALUES (tqc_param_code_seq.NEXTVAL,
                         v_label_name,
                         v_label_value,
                         v_label_status,
                         v_label_desc);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while creating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_labels
               SET label_name = v_label_name,
                   label_value = v_label_value,
                   label_status = v_label_status,
                   label_desc = v_label_desc
             WHERE label_code = v_label_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while updating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_labels
                  WHERE label_code = v_label_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while deleting record ' || SQLERRM (SQLCODE));
         END;
      END IF;
   END;

   FUNCTION get_intermediaries (
      v_type          IN   VARCHAR2,
      v_agn_shtdesc   IN   VARCHAR2,
      v_agn_name      IN   VARCHAR2
   )
      RETURN agents_ref
   IS
      vrefcursor      agents_ref;
      v_code          NUMBER;
      v_facre_param   VARCHAR2 (10);
   BEGIN
      -- raise_error('innn');
      BEGIN
         SELECT gin_parameters_pkg.get_param_varchar ('COMBINE_FAC_ACCOUNTS')
           INTO v_facre_param
           FROM DUAL;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_facre_param := 'N';
      END;

      -- RAISE_ERROR('v_type '||v_type);
      IF v_type = 'AGENTS/BROKERS'
      THEN
         OPEN vrefcursor FOR
              SELECT agn_sht_desc,
                     agn_code,
                     agn_name,
                     agn_brn_code,
                     act_account_type brn_name,
                     DECODE (agn_code, 0, 'N', NVL (agn_comm_allowed, 'Y')),
                     agn_runoff,
                     agn_physical_address,
                     agn_email_address,
                     agn_sms_tel
                FROM tqc_agencies,
                     tqc_agency_systems,
                     tqc_branches,
                     tqc_account_types
               WHERE     agn_status = 'ACTIVE'
                     AND agn_act_code = act_code
                     AND agn_act_code IN (1, 2, 3, 16, 32)   --, 5, 9, 10, 16)
                     AND agn_brn_code = brn_code(+)
                     AND agn_status = 'ACTIVE'
                     AND agn_code = asys_agn_code
                     AND asys_sys_code = 37
                     AND UPPER (agn_sht_desc) LIKE
                            '%' || UPPER (v_agn_shtdesc) || '%'
                     AND UPPER (agn_name) LIKE '%' || UPPER (v_agn_name) || '%'
            ORDER BY agn_name;

         RETURN (vrefcursor);
      ELSIF v_type = 'DIRECT'                               --Added by Gitonga
      THEN
         OPEN vrefcursor FOR
              SELECT agn_sht_desc,
                     agn_code,
                     agn_name,
                     agn_brn_code,
                     act_account_type brn_name,
                     DECODE (agn_code, 0, 'N', NVL (agn_comm_allowed, 'Y')),
                     agn_runoff,
                     agn_physical_address,
                     agn_email_address,
                     agn_sms_tel
                FROM tqc_agencies,
                     tqc_agency_systems,
                     tqc_branches,
                     tqc_account_types
               WHERE     agn_status = 'ACTIVE'
                     AND agn_act_code = act_code
                     AND agn_act_code IN (1)                 --, 5, 9, 10, 16)
                     AND agn_brn_code = brn_code(+)
                     AND agn_status = 'ACTIVE'
                     AND agn_code = asys_agn_code
                     AND asys_sys_code = 37
                     AND UPPER (agn_sht_desc) LIKE
                            '%' || UPPER (v_agn_shtdesc) || '%'
                     AND UPPER (agn_name) LIKE '%' || UPPER (v_agn_name) || '%'
            ORDER BY agn_name;

         RETURN (vrefcursor);
      ELSE
         IF v_type = 'FACREIN'
         THEN
            IF NVL (v_facre_param, 'N') = 'N'
            THEN
               OPEN vrefcursor FOR
                    SELECT agn_sht_desc,
                           agn_code,
                           agn_name,
                           agn_brn_code,
                           brn_name,
                           DECODE (agn_code,
                                   0, 'N',
                                   NVL (agn_comm_allowed, 'Y')),
                           agn_runoff,
                           agn_physical_address,
                           agn_email_address,
                           agn_sms_tel
                      FROM tqc_agencies, tqc_agency_systems, tqc_branches
                     WHERE     agn_status = 'ACTIVE'
                           AND agn_act_code IN (4)
                           AND agn_brn_code = brn_code(+)
                           AND agn_status = 'ACTIVE'
                           AND agn_code = asys_agn_code
                           AND asys_sys_code = 37
                           AND UPPER (agn_sht_desc) LIKE
                                  '%' || UPPER (v_agn_shtdesc) || '%'
                           AND UPPER (agn_name) LIKE
                                  '%' || UPPER (v_agn_name) || '%'
                  ORDER BY agn_name;

               RETURN (vrefcursor);
            ELSE
               OPEN vrefcursor FOR
                    SELECT agn_sht_desc,
                           agn_code,
                           agn_name,
                           agn_brn_code,
                           brn_name,
                           DECODE (agn_code,
                                   0, 'N',
                                   NVL (agn_comm_allowed, 'Y')),
                           agn_runoff,
                           agn_physical_address,
                           agn_email_address,
                           agn_sms_tel
                      FROM tqc_agencies, tqc_agency_systems, tqc_branches
                     WHERE     agn_status = 'ACTIVE'
                           AND agn_act_code IN (4, 7)
                           AND agn_brn_code = brn_code(+)
                           AND agn_status = 'ACTIVE'
                           AND agn_code = asys_agn_code
                           AND asys_sys_code = 37
                           AND UPPER (agn_sht_desc) LIKE
                                  '%' || UPPER (v_agn_shtdesc) || '%'
                           AND UPPER (agn_name) LIKE
                                  '%' || UPPER (v_agn_name) || '%'
                  ORDER BY agn_name;

               RETURN (vrefcursor);
            END IF;
         ELSIF v_type = 'FACREOUT'
         THEN
            IF NVL (v_facre_param, 'N') = 'N'
            THEN
               v_code := 7;
            ELSE
               OPEN vrefcursor FOR
                    SELECT agn_sht_desc,
                           agn_code,
                           agn_name,
                           agn_brn_code,
                           brn_name,
                           DECODE (agn_code,
                                   0, 'N',
                                   NVL (agn_comm_allowed, 'Y')),
                           agn_runoff,
                           agn_physical_address,
                           agn_email_address,
                           agn_sms_tel
                      FROM tqc_agencies, tqc_agency_systems, tqc_branches
                     WHERE     agn_status = 'ACTIVE'
                           AND agn_act_code IN (4, 7)
                           AND agn_brn_code = brn_code(+)
                           AND agn_status = 'ACTIVE'
                           AND agn_code = asys_agn_code
                           AND asys_sys_code = 37
                           AND UPPER (agn_sht_desc) LIKE
                                  '%' || UPPER (v_agn_shtdesc) || '%'
                           AND UPPER (agn_name) LIKE
                                  '%' || UPPER (v_agn_name) || '%'
                  ORDER BY agn_name;

               RETURN (vrefcursor);
            END IF;
         ELSIF v_type IN ('COINSURANCE', 'CARRIER')
         THEN
            v_code := 5;
         ELSIF v_type = 'REINSURANCE'
         THEN
            v_code := 6;
         ELSE
            --RAISE_ERROR('Type ' || v_type || ' not defined..');
            v_code := 4;
         END IF;

         OPEN vrefcursor FOR
              SELECT agn_sht_desc,
                     agn_code,
                     agn_name,
                     agn_brn_code,
                     brn_name,
                     DECODE (agn_code, 0, 'N', NVL (agn_comm_allowed, 'Y')),
                     agn_runoff,
                     agn_physical_address,
                     agn_email_address,
                     agn_sms_tel
                FROM tqc_agencies, tqc_agency_systems, tqc_branches
               WHERE     agn_status = 'ACTIVE'
                     AND agn_act_code = v_code
                     AND agn_brn_code = brn_code(+)
                     AND agn_status = 'ACTIVE'
                     AND agn_code = asys_agn_code
                     AND asys_sys_code = 37
                     AND UPPER (agn_sht_desc) LIKE
                            '%' || UPPER (v_agn_shtdesc) || '%'
                     AND UPPER (agn_name) LIKE '%' || UPPER (v_agn_name) || '%'
            ORDER BY agn_name;

         RETURN (vrefcursor);
      END IF;
   END;

   PROCEDURE dists_prc (
      v_add_edit        IN   VARCHAR2,
      v_dist_code       IN   tqc_districts.dist_code%TYPE,
      v_dist_cou_code   IN   tqc_districts.dist_cou_code%TYPE,
      v_dist_sht_desc   IN   tqc_districts.dist_sht_desc%TYPE,
      v_dist_name       IN   tqc_districts.dist_name%TYPE,
      v_dist_sts_code   IN   tqc_districts.dist_sts_code%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_districts
             WHERE dist_code = v_dist_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_districts (dist_code,
                                       dist_cou_code,
                                       dist_sht_desc,
                                       dist_name,
                                       dist_sts_code)
                 VALUES (tqc_twn_code_seq.NEXTVAL,
                         v_dist_cou_code,
                         v_dist_sht_desc,
                         v_dist_name,
                         v_dist_sts_code);
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
            UPDATE tqc_districts
               SET dist_cou_code = v_dist_cou_code,
                   dist_sht_desc = v_dist_sht_desc,
                   dist_name = v_dist_name,
                   dist_sts_code = v_dist_sts_code
             WHERE dist_code = v_dist_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while updating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_districts
                  WHERE dist_code = v_dist_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   PROCEDURE addupdateratingorg (
      v_addedit       IN   VARCHAR2,
      v_rorgcode      IN   tqc_rating_organizations.rorg_code%TYPE,
      v_rorgdesc      IN   tqc_rating_organizations.rorg_desc%TYPE,
      v_rorgshtdesc   IN   tqc_rating_organizations.rorg_sht_desc%TYPE
   )
   IS
      v_rorg_code   NUMBER := 0;
   BEGIN
      IF v_addedit = 'A'
      THEN
         BEGIN
            SELECT MAX (rorg_code)
              INTO v_rorg_code
              FROM tqc_rating_organizations;

            v_rorg_code := NVL (v_rorg_code, 0) + 1;

            INSERT
              INTO tqc_rating_organizations (rorg_code,
                                             rorg_desc,
                                             rorg_sht_desc)
            VALUES (v_rorg_code, v_rorgdesc, v_rorgshtdesc);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while creating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_addedit = 'E'
      THEN
         BEGIN
            UPDATE tqc_rating_organizations
               SET rorg_desc = v_rorgdesc,
                   rorg_sht_desc = v_rorgshtdesc
             WHERE rorg_code = v_rorgcode;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while updating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_addedit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_rating_organizations
                  WHERE rorg_code = v_rorgcode;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while deleting record ' || SQLERRM (SQLCODE));
         END;
      END IF;
   END;

   PROCEDURE addupdaterating (
      v_addedit    IN   VARCHAR2,
      v_orscode    IN   tqc_org_rating_starndards.ors_code%TYPE,
      v_orsdesc    IN   tqc_org_rating_starndards.ors_desc%TYPE,
      v_rorgcode   IN   tqc_org_rating_starndards.ors_rorg_code%TYPE
   )
   IS
      v_ors_code   NUMBER := 0;
   BEGIN
      IF v_addedit = 'A'
      THEN
         BEGIN
            SELECT MAX (ors_code)
              INTO v_ors_code
              FROM tqc_org_rating_starndards;

            v_ors_code := NVL (v_ors_code, 0) + 1;

            INSERT
              INTO tqc_org_rating_starndards (ors_code,
                                              ors_desc,
                                              ors_rorg_code)
            VALUES (v_ors_code, v_orsdesc, v_rorgcode);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while creating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_addedit = 'E'
      THEN
         BEGIN
            UPDATE tqc_org_rating_starndards
               SET ors_desc = v_orsdesc, ors_rorg_code = v_rorgcode
             WHERE ors_code = v_orscode;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while updating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_addedit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_org_rating_starndards
                  WHERE ors_code = v_orscode;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while deleting record ' || SQLERRM (SQLCODE));
         END;
      END IF;
   END;
   procedure bank_territory_prc(
      v_bnkt_add_edit               varchar2,
      v_bnkt_code                   in out number,
      v_bnkt_territory_name         varchar2,
      v_bnkt_sht_desc               varchar2,
      v_bnkt_bnk_code               number
   )
   is
   begin
   
--      raise_error(
--      'v_bnkt_add_edit: '||v_bnkt_add_edit||
--      'v_bnkt_code: '||v_bnkt_code||
--      'v_bnkt_territory_name: '||v_bnkt_territory_name||
--      'v_bnkt_sht_desc: '||v_bnkt_sht_desc||
--      'v_bnkt_bnk_code'||v_bnkt_bnk_code 
--      );

     IF NVL(v_bnkt_add_edit,'N')= 'A' THEN
       BEGIN
        SELECT tqc_bnkt_code_seq.NEXTVAL INTO 
        v_bnkt_code FROM DUAL;
        INSERT into tqc_bank_territories
           (bnkt_code, bnkt_territory_name, bnkt_sht_desc, bnkt_bnk_code)
         VALUES
           (tqc_bnkt_code_seq.NEXTVAL, v_bnkt_territory_name, v_bnkt_sht_desc, v_bnkt_bnk_code);
        END;
      ELSIF NVL(v_bnkt_add_edit,'N')= 'E' THEN
         BEGIN
          UPDATE tqc_bank_territories
            SET  
            bnkt_territory_name=v_bnkt_territory_name, 
            bnkt_sht_desc=v_bnkt_sht_desc, 
            bnkt_bnk_code=v_bnkt_bnk_code
          WHERE 
               bnkt_code=v_bnkt_code;
          END;
      ELSIF NVL(v_bnkt_add_edit,'N')= 'D' THEN
        BEGIN 
        DELETE FROM tqc_bank_territories 
          WHERE 
               bnkt_code=v_bnkt_code;
        END;
      END IF; 

   end;
   
   PROCEDURE transfer_prc (
      v_action varchar2,
      v_tt_code in out number,
      v_tt_trans_type    varchar2,
      v_tt_trans_date    date,
      v_tt_trans_to_code      number,
      v_tt_trans_to_name      varchar,
      v_tt_trans_from_code    number,
      v_tt_trans_from_name    varchar,
      v_tt_done_by       varchar,
      v_tt_done_date     date,
      v_tt_authorized    varchar, 
      v_tt_authorized_by  varchar,
      v_tt_authorized_date date 
    )
   IS
      v_cnt   NUMBER := 0;
   BEGIN
      --       v_cnt:=0;
      IF NVL (V_ACTION, '*') = 'A'
      THEN
         BEGIN
            SELECT TQC_TT_CODE_SEQ.NEXTVAL INTO v_tt_code FROM DUAL;

            INSERT INTO tqc_transfers (tt_code,
                                       tt_trans_type,
                                       tt_trans_date,
                                       tt_trans_to_code,
                                       tt_trans_TO_name,
                                       tt_trans_from_code,
                                       tt_trans_from_name,
                                       tt_done_by,
                                       tt_done_date,
                                       tt_authorized,
                                       tt_authorized_by,
                                       tt_authorized_date)
                 VALUES (v_tt_code,
                         v_tt_trans_type,
                         v_tt_trans_date,
                         v_tt_trans_to_code,
                         v_tt_trans_to_name,
                         v_tt_trans_from_code,
                         v_tt_trans_from_name,
                         v_tt_done_by,
                         v_tt_done_date,
                         v_tt_authorized,
                         v_tt_authorized_by,
                         v_tt_authorized_date);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Unable to create transfer.' || SQLERRM);
         END;
      ELSIF NVL (V_ACTION, '*') = 'E'
      THEN
         BEGIN
            UPDATE tqc_transfers
               SET tt_trans_type = NVL (v_tt_trans_type, tt_trans_type),
                   tt_trans_date = NVL (v_tt_trans_date, tt_trans_date),
                   tt_trans_to_code =
                      NVL (v_tt_trans_to_code, tt_trans_to_code),
                   tt_trans_to_name =
                      NVL (v_tt_trans_to_name, tt_trans_to_name),
                   tt_trans_from_code =
                      NVL (v_tt_trans_from_code, tt_trans_from_code),
                   tt_trans_from_name =
                      NVL (v_tt_trans_from_name, tt_trans_from_name),
                   tt_done_by = NVL (v_tt_done_by, tt_done_by),
                   tt_done_date = NVL (v_tt_done_date, tt_done_date),
                   tt_authorized = NVL (v_tt_authorized, tt_authorized),
                   tt_authorized_by =
                      NVL (v_tt_authorized_by, tt_authorized_by),
                   tt_authorized_date =
                      NVL (v_tt_authorized_date, tt_authorized_date)
             WHERE tt_code = v_tt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Unable to modify transfer.' || SQLERRM);
         END;
      ELSIF NVL (V_ACTION, '*') = 'D'
      THEN
         BEGIN
            DELETE tqc_transfers
             WHERE tt_code = v_tt_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Unable to delete transfer.' || SQLERRM);
         END;
      END IF;
   END;

   PROCEDURE transfer_item_prc (v_action                       VARCHAR2,
                                v_tti_code              IN OUT NUMBER,
                                v_tti_tt_code                  NUMBER,
                                v_tti_item_code                VARCHAR2,
                                v_tti_item_name                VARCHAR2,
                                v_tti_item_sht_desc            VARCHAR2,
                                v_tti_item_type                VARCHAR2,
                                v_tti_trans_date               DATE,
                                v_tti_trans_to                 VARCHAR2,
                                v_tti_trans_from               VARCHAR2,
                                v_tti_done_by                  VARCHAR2,
                                v_tti_done_date                DATE,
                                v_tti_authorized               VARCHAR2,
                                v_tti_authorized_by            VARCHAR2,
                                v_tti_authorized_date          DATE)
   IS
      v_cnt   NUMBER := 0;
   BEGIN
      --       v_cnt:=0;
      IF NVL (v_action, '*') = 'A'
      THEN
         BEGIN
            SELECT TQC_TT_CODE_SEQ.NEXTVAL INTO v_tti_code FROM DUAL;

            INSERT INTO tqc_transfers_items (tti_code,
                                             tti_tt_code,
                                             tti_item_code,
                                             tti_item_name,
                                             tti_item_sht_desc,
                                             tti_item_type,
                                             tti_trans_date,
                                             tti_trans_to,
                                             tti_trans_from,
                                             tti_done_by,
                                             tti_done_date,
                                             tti_authorized,
                                             tti_authorized_by,
                                             tti_authorized_date)
                 VALUES (v_tti_code,
                         v_tti_tt_code,
                         v_tti_item_code,
                         v_tti_item_name,
                         v_tti_item_sht_desc,
                         v_tti_item_type,
                         v_tti_trans_date,
                         v_tti_trans_to,
                         v_tti_trans_from,
                         v_tti_done_by,
                         v_tti_done_date,
                         v_tti_authorized,
                         v_tti_authorized_by,
                         v_tti_authorized_date);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Unable to create transfer item.' || SQLERRM);
         END;
      ELSIF NVL (V_ACTION, '*') = 'E'
      THEN
         BEGIN
            UPDATE tqc_transfers_items
               SET tti_tt_code = NVL (v_tti_tt_code, tti_tt_code),
                   tti_item_code = NVL (v_tti_item_code, v_tti_item_code),
                   tti_item_name = NVL (v_tti_item_name, tti_item_name),
                   tti_item_sht_desc =
                      NVL (v_tti_item_sht_desc, tti_item_sht_desc),
                   tti_item_type = NVL (v_tti_item_type, tti_item_type),
                   tti_trans_date = NVL (v_tti_trans_date, tti_trans_date),
                   tti_trans_to = NVL (v_tti_trans_to, tti_trans_to),
                   tti_trans_from = NVL (v_tti_trans_from, tti_trans_from),
                   tti_done_by = NVL (v_tti_done_by, tti_done_by),
                   tti_done_date = NVL (v_tti_done_date, tti_done_date),
                   tti_authorized = NVL (v_tti_authorized, tti_authorized),
                   tti_authorized_by =
                      NVL (v_tti_authorized_by, tti_authorized_by),
                   tti_authorized_date =
                      NVL (v_tti_authorized_date, tti_authorized_date)
             WHERE tti_code = v_tti_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Unable to modify transfer item.' || SQLERRM);
         END;
      ELSIF NVL (V_ACTION, '*') = 'D'
      THEN
         BEGIN
            DELETE TQC_TRANSFERS_ITEMS
             WHERE TTI_CODE = V_TTI_CODE;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Unable to delete transfer item.' || SQLERRM);
         END;
      END IF;
   END;

   PROCEDURE add_transferable_items(
        v_item_code number, 
        v_item_name varchar2, 
        v_item_sht_desc varchar2, 
        v_item_type varchar2,
        v_trans_to number,
        v_trans_from number,  
        v_tt_code number,
        v_done_by varchar2
    )
   IS 
      v_tti_code     NUMBER;
      v_cnt          NUMBER := 0;
   BEGIN
      --    raise_error('v_item_code: '|| v_item_code ||
      --        'v_item_name: '||v_item_name ||
      --        'v_item_sht_desc: '||v_item_sht_desc||
      --        'v_item_type: '||v_item_type||
      --        'v_tt_code: '||v_tt_code||
      --        'v_done_by: '||v_done_by);
      BEGIN
         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_transfers
          WHERE     tt_code = v_tt_code
                AND NVL (tt_authorized, 'N') <> 'Y'
                AND tt_trans_type IN ('AUT', 'UAT', 'ABT', 'BRT','ROT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_cnt := 0;
      END;
   

      IF v_cnt = 0
      THEN
         BEGIN
            raise_error ('Transfer fetch error!' || SQLERRM);
         END;
      END IF; 

      BEGIN
         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_transfers_items
          WHERE tqc_transfers_items.tti_item_code = v_item_code
                AND tti_tt_code = v_tt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_cnt := 0;
      END;

      IF NVL (v_cnt, 0) = 0
      THEN
         tq_crm.tqc_setups_pkg.transfer_item_prc ('A',
                                                  v_tti_code,
                                                  v_tt_code,
                                                  v_item_code,
                                                  v_item_name,
                                                  v_item_sht_desc,
                                                  v_item_type,
                                                  SYSDATE,
                                                  v_trans_to,
                                                  v_trans_from,
                                                  v_done_by,
                                                  SYSDATE,
                                                  'N',
                                                  NULL,
                                                  NULL);
      END IF;
   END;

   PROCEDURE remove_transfered_items (v_itt_code NUMBER)
   IS
      v_cnt   NUMBER := 0;
   BEGIN
      --      raise_error('v_itt_code: '|| v_itt_code );
      IF v_itt_code IS NULL
      THEN
         raise_error ('Item Id not provided!');
      END IF;

      BEGIN
         DELETE tqc_transfers_items
          WHERE tti_code = v_itt_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Unable to delete transfer item.' || SQLERRM);
      END;
   END;

   
   PROCEDURE authorize_transfer (v_tt_code             NUMBER,
                                 v_tt_authorized_by    VARCHAR)
   IS
      v_cnt          NUMBER := NULL;
      v_agn_code     NUMBER := NULL;
      v_brn_code     NUMBER := NULL;
      v_reg_code     NUMBER := NULL;
      v_trans_type   VARCHAR (4) := NULL;
      v_err          VARCHAR (1000) := NULL;
      

      CURSOR c_transfers (
         p_tt_code NUMBER)
      IS
         SELECT *
           FROM tqc_transfers
          WHERE     tt_code = p_tt_code
                AND NVL (tt_authorized, 'N') <> 'Y'
                AND tt_trans_to_code IS NOT NULL
                AND tt_trans_type IN ('AUT', 'UAT', 'ABT', 'BRT','ROT');

      CURSOR c_transfer_items (p_tt_code NUMBER)
      IS
         SELECT *
           FROM tqc_transfers_items
          WHERE tti_tt_code = p_tt_code
            AND tti_item_code IS NOT NULL;
   BEGIN
      BEGIN
         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_transfers
          WHERE     tt_code = v_tt_code
            AND tt_trans_to_code IS NOT NULL
            AND NVL (tt_authorized, 'N') <> 'Y'
            AND tt_trans_type IN ('AUT', 'UAT', 'ABT', 'BRT','ROT');
      EXCEPTION
         WHEN OTHERS
         THEN
            v_cnt := 0;
      END;
--      
      IF v_cnt = 0
      THEN
         BEGIN
            raise_error ('Transfer fetch error!' || SQLERRM);
         END;
      ELSE 
        FOR t IN c_transfers (v_tt_code)
            LOOP 
               FOR i IN c_transfer_items (t.tt_code)
               LOOP
                  BEGIN 
                      IF NVL (t.tt_trans_type, '*') = 'ROT' THEN 
                          BEGIN                       
                              transfer_region_prc (i.tti_trans_from,t.tt_trans_to_code,i.tti_item_code); 
                          END;    
                      ELSIF NVL (t.tt_trans_type, '*') = 'BRT' THEN
                            BEGIN                        
                              transfer_branch_prc (i.tti_trans_from,t.tt_trans_to_code,i.tti_item_code);
                            END; 
                      ELSIF NVL (t.tt_trans_type, '*') = 'ABT' THEN 
                           BEGIN                         
                               transfer_agency_prc (i.tti_trans_from,t.tt_trans_to_code,i.tti_item_code);
                            END;  
                      ELSIF NVL (t.tt_trans_type, '*') = 'UAT' THEN  
                            BEGIN                        
                                transfer_unit_prc (i.tti_trans_from,t.tt_trans_to_code,i.tti_item_code); 
                            END; 
                      ELSIF NVL (t.tt_trans_type, '*') = 'AUT' THEN
                            BEGIN                          
                                transfer_agent_prc (i.tti_trans_from,t.tt_trans_to_code,i.tti_item_code);
                             END;  
                      ELSE 
                         BEGIN                         
                               raise_error ('Transfer error provide transfer type!');
                         END;             
                      END IF;
                        BEGIN
                    UPDATE tqc_transfers_items
                      SET tti_authorized = 'Y',
                          tti_authorized_by = v_tt_authorized_by,
                          tti_authorized_date = SYSDATE
                        WHERE tti_item_code = i.tti_item_code;
                    EXCEPTION
                       WHEN OTHERS
                       THEN
                          raise_error ('Unable to authorize transfer.' || SQLERRM);
                    END;
                  END;
               END LOOP;
                   
               BEGIN
                   UPDATE tqc_transfers
                      SET tt_authorized = 'Y',
                          tt_authorized_by = v_tt_authorized_by,
                          tt_authorized_date = SYSDATE
                    WHERE tt_code = t.tt_code;
                EXCEPTION
                   WHEN OTHERS
                   THEN
                      raise_error ('Unable to authorize transfer.' || SQLERRM);
                END;
        END LOOP;  
      END IF;
   END;
   PROCEDURE transfer_agency_prc (  
      v_from     NUMBER,
      v_to       NUMBER,
      v_item_code   NUMBER
      )
   IS
      v_agn_code     NUMBER := NULL;
      v_reg_code     NUMBER := NULL;
   BEGIN
            BEGIN
                 SELECT brn_reg_code
                  INTO v_reg_code
                  FROM tqc_branches
                 WHERE brn_code = v_to  
                 AND ROWNUM = 1;
                 EXCEPTION WHEN OTHERS THEN
                   v_reg_code:=NULL;
             END;
             IF NVL(v_reg_code,0)=0 THEN
                 BEGIN 
                      raise_error (
                          'Region not found!:'
                          || SQLERRM); 
                 END;  
             END IF;
             BEGIN 
             UPDATE tqc_branch_units
                   SET bru_brn_code = v_to
                 WHERE bru_bra_code = v_item_code
                 AND bru_brn_code = v_from;
             EXCEPTION
                WHEN OTHERS
                THEN
                   raise_error (
                      'Unable to authorize transfer Branch unit:'
                      || SQLERRM);
             END;

             BEGIN
                SELECT bra_agn_code
                  INTO v_agn_code
                  FROM tqc_branch_agencies
                 WHERE bra_code = v_item_code AND ROWNUM = 1;
             EXCEPTION
                WHEN OTHERS
                THEN
                   v_agn_code := NULL;
             END;

             IF v_agn_code IS NOT NULL
             THEN
                BEGIN
                   UPDATE tqc_agencies
                      SET agn_brn_code = v_to  
                    WHERE agn_code = v_agn_code
                    and agn_brn_code= v_from;
                EXCEPTION
                   WHEN OTHERS
                   THEN
                      RAISE_ERROR (
                         'Unable to authorize transfer Agency:'
                         || SQLERRM);
                END;
                BEGIN
                   UPDATE lms_agencies
                      SET agn_brn_code = v_to  
                    WHERE agn_code = v_agn_code
                    and agn_brn_code= v_from;
                EXCEPTION
                   WHEN OTHERS
                   THEN
                      RAISE_ERROR (
                         'Unable to authorize transfer Agency:'
                         || SQLERRM);
                END;
             END IF;
             --[FIXME]-- MOVE AGENTS ACT_CODE=2

             BEGIN
                UPDATE tqc_branch_agencies
                   SET bra_brn_code = v_to
                 WHERE bra_code = v_item_code;
             EXCEPTION
                WHEN OTHERS
                THEN
                   raise_error (
                      'Unable to authorize transfer Branch agency:'
                      || SQLERRM);
             END; 
   END;
 PROCEDURE transfer_agent_prc (  
      v_from     NUMBER,
      v_to       NUMBER,
      v_item_code   NUMBER
      )
   IS
      v_agn_code     NUMBER := NULL;
      v_brn_code     NUMBER := NULL; 
   BEGIN
        BEGIN
             SELECT bru_brn_code
              INTO v_brn_code
              FROM tqc_branch_units
             WHERE bru_code = v_to  
             AND ROWNUM = 1;
             EXCEPTION WHEN OTHERS THEN
               v_brn_code:=NULL; 
        END;
       IF NVL(v_brn_code,0)=0 THEN
           BEGIN
           raise_error (
                 ' Branch not found!:'
                 || SQLERRM);
           END;
       END IF;
       BEGIN
           UPDATE lms_agencies
              SET agn_bru_code = v_to, 
              agn_brn_code = v_brn_code 
            WHERE agn_code = v_item_code
            AND agn_bru_code = v_from; -- and agn_act_code = 2 
        EXCEPTION
           WHEN OTHERS
           THEN
              RAISE_ERROR (
                 'Unable to Authorize transfer Agents:'
                 || SQLERRM);
        END;
        BEGIN
           UPDATE tqc_agencies
              SET agn_bru_code = v_to, 
              agn_brn_code = v_brn_code 
            WHERE agn_code = v_item_code
            AND agn_bru_code = v_from; -- and agn_act_code = 2 
        EXCEPTION
           WHEN OTHERS
           THEN
              RAISE_ERROR (
                 'Unable to Authorize transfer Agents:'
                 || SQLERRM);
        END; 
   END;
   PROCEDURE transfer_unit_prc (    
                                  v_from     NUMBER,
                                  v_to       NUMBER,
                                  v_item_code          NUMBER)
   IS
    v_agn_code     NUMBER := NULL;
    v_brn_code     NUMBER := NULL;
    v_reg_code     NUMBER := NULL;
   BEGIN
   
        BEGIN
             SELECT bra_brn_code,bra_agn_code,brn_reg_code
              INTO v_brn_code,v_agn_code,v_reg_code
              FROM tqc_branch_agencies,tqc_branches
             WHERE bra_code = v_to 
             AND bra_brn_code = brn_code 
             AND ROWNUM = 1;
             EXCEPTION WHEN OTHERS THEN
               v_brn_code:=NULL;
               v_reg_code:=NULL;
               v_agn_code:=null;
        END; 
      
      IF NVL(v_agn_code,0)!=0 AND NVL(v_brn_code,0)!=0 AND NVL(v_reg_code,0)!=0 THEN
          BEGIN   ---include agents[FIXME] 
               raise_error ('Agency /Branch/Region not found!.');
          END; 
      END IF;
      BEGIN
          UPDATE tqc_agencies
                SET agn_brn_code = v_brn_code 
              WHERE agn_act_code = 2 
              AND agn_bru_code = v_item_code;
          EXCEPTION
             WHEN OTHERS
             THEN
                raise_error ('Unable to authorize unit transfer.' || SQLERRM);
      END;
          
      BEGIN
          UPDATE lms_agencies
                SET agn_brn_code = v_brn_code 
              WHERE agn_act_code = 2 
              AND agn_bru_code = v_item_code;
          EXCEPTION
             WHEN OTHERS
             THEN
                raise_error ('Unable to authorize unit transfer.' || SQLERRM);
      END;
      
        UPDATE tqc_branch_units
          SET bru_bra_code = v_to,
              bru_agn_code = v_agn_code,
              bru_brn_code = v_brn_code
        WHERE bru_bra_code = v_from
        AND bru_code = v_item_code;
        EXCEPTION
           WHEN OTHERS
           THEN
              RAISE_ERROR (
                 'Unable to authorize transfer Unit:'
                 || SQLERRM);
   END;
   PROCEDURE transfer_branch_prc (    
                                  v_from     NUMBER,
                                  v_to       NUMBER,
                                  v_item_code          NUMBER)
   IS
   BEGIN 
      BEGIN
         UPDATE tqc_branches
            SET brn_reg_code = v_to
          WHERE brn_reg_code = v_from AND brn_code = v_item_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Unable to authorize branch transfer.' || SQLERRM);
      END;
   END;
   
   PROCEDURE transfer_region_prc (     
          v_from        NUMBER,
          v_to         NUMBER,
          v_item_code  NUMBER
          )
   IS
   BEGIN
      BEGIN
         UPDATE tqc_regions
            SET reg_org_code = v_to
          WHERE reg_org_code = v_from AND 
          reg_code = v_item_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error('Transfer Region fail'||SQLERRM);
      END;
   END;
   

   FUNCTION get_reg_code (v_reg_sht_desc VARCHAR)
      RETURN NUMBER
   IS
      v_reg_code   NUMBER := NULL;
   BEGIN
      BEGIN
         SELECT reg_code
           INTO v_reg_code
           FROM tqc_regions
          WHERE reg_sht_desc = v_reg_sht_desc;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_reg_code := NULL;
      END;

      RETURN v_reg_code;
   END;

   PROCEDURE transfer_branch (v_reg_from    VARCHAR,
                              v_reg_to      VARCHAR,
                              v_branch      VARCHAR)
   IS
      v_reg_code_from   NUMBER := NULL;
      v_reg_code_to     NUMBER := NULL;
      v_brn_code        NUMBER := NULL;
   BEGIN
      v_reg_code_from := get_reg_code (v_reg_from);
      v_reg_code_to := get_reg_code (v_reg_to);

      BEGIN
         SELECT brn_code
           INTO v_brn_code
           FROM tqc_branches
          WHERE brn_sht_desc = v_branch;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_brn_code := NULL;
      END;

      IF     v_reg_code_from IS NOT NULL
         AND v_reg_code_to IS NOT NULL
         AND v_brn_code IS NOT NULL
      THEN
         BEGIN
            transfer_branch_prc (v_reg_code_from, v_reg_code_to, v_brn_code);
         END;
      END IF;
   END;

   FUNCTION get_field_varchar (v_col IN VARCHAR2, v_field IN VARCHAR2)
      RETURN VARCHAR2
   IS
      v_fm_mandatory     VARCHAR2 (1 BYTE);
      v_fm_field_label   VARCHAR2 (200 BYTE);
      v_fm_visible       VARCHAR2 (1 BYTE);
   BEGIN
      BEGIN
         SELECT fm_mandatory, fm_field_label, fm_visible
           INTO v_fm_mandatory, v_fm_field_label, v_fm_visible
           FROM tqc_form_fields
          WHERE fm_field_name = v_field;

         IF NVL (v_col, '*') = 'R'
         THEN
            RETURN (v_fm_mandatory);
         ELSIF NVL (v_col, '*') = 'L'
         THEN
            RETURN (v_fm_field_label);
         ELSIF NVL (v_col, '*') = 'V'
         THEN
            RETURN (v_fm_visible);
         END IF;

         RETURN NULL;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            RAISE_ERROR ('Field ' || v_field || ' not found ');
         WHEN TOO_MANY_ROWS
         THEN
            RAISE_ERROR (
               'More than one instance of Field ' || v_field || ' found ');
         WHEN OTHERS
         THEN
            RAISE_ERROR (
               'Error getting the Field' || v_field || ' Error: ' || SQLERRM);
      END;

      RETURN NULL;
   END;

   PROCEDURE agent_type_proc (v_addedit               IN VARCHAR2,
                              v_agnty_code            IN NUMBER,
                              v_agnty_type            IN VARCHAR2,
                              v_agnty_type_sht_desc   IN VARCHAR2,
                              v_agnty_act_code        IN NUMBER)
   IS
   BEGIN
      --      RAISE_ERROR(
      --      'v_addedit: '||v_addedit||
      --      'v_agnty_code: '||v_agnty_code||
      --      'v_agnty_type: '||v_agnty_type||
      --      'v_agnty_type_sht_desc: '||v_agnty_type_sht_desc
      --      );
      IF v_addedit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_agent_types (agnty_code,
                                         agnty_type_sht_desc,
                                         agnty_type,
                                         agnty_act_code)
                 VALUES (tqc_agnty_code_seq.NEXTVAL,
                         v_agnty_type_sht_desc,
                         v_agnty_type,
                         v_agnty_act_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR INSERTING RECORD' || SQLERRM);
         END;
      ELSIF v_addedit = 'E'
      THEN
         BEGIN
            UPDATE tqc_agent_types
               SET agnty_type = v_agnty_type,
                   agnty_type_sht_desc = v_agnty_type_sht_desc,
                   agnty_act_code = v_agnty_act_code
             WHERE agnty_code = v_agnty_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('ERROR UPDATING RECORD' || SQLERRM);
         END;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_agent_types
               WHERE agnty_code = v_agnty_code;
      END IF;
   END;
    PROCEDURE occupations_sections_prc (
      v_add_edit       IN   VARCHAR2,
      v_occ_code       IN   tqc_occupations.occ_code%TYPE,
      v_occ_sec_code   IN   NUMBER
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_sector_occupations
             WHERE occ_code = v_occ_code
             AND occ_sec_code  = v_occ_sec_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

            INSERT INTO tqc_sector_occupations (OCC_CODE, OCC_SEC_CODE)
                 VALUES (v_occ_code, v_occ_sec_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while creating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_sector_occupations
                  WHERE occ_code = v_occ_code
                  AND  OCC_SEC_CODE= v_occ_sec_code ;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while deleting record ' || SQLERRM (SQLCODE));
         END;
      END IF;
   END;

   PROCEDURE delete_agency_prc (v_agn_code IN NUMBER)
   IS
      v_count   NUMBER;
   BEGIN
      DELETE FROM tqc_agency_accounts
            WHERE aga_agn_code = v_agn_code;

      DELETE FROM tqc_agencies
            WHERE agn_code = v_agn_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (
            'Error occured while deleting record ' || SQLERRM (SQLCODE));
   END;

   PROCEDURE updateddfailedrmks (v_addedit             IN VARCHAR2,
                                 v_dfr_code               NUMBER,
                                 v_dfr_failed_remark      VARCHAR2,
                                 v_dfr_appl_level         VARCHAR2)
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         BEGIN
            INSERT
              INTO tqc_dd_failed_remarks (DFR_CODE,
                                          DFR_APPL_LEVEL,
                                          DFR_FAILED_REMARK)
            VALUES (
                      TQC_DFR_CODE_SEQ.NEXTVAL,
                      v_dfr_appl_level,
                      v_dfr_failed_remark);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while creating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_addedit = 'E'
      THEN
         BEGIN
            UPDATE tqc_dd_failed_remarks
               SET DFR_APPL_LEVEL = v_dfr_failed_remark,
                   DFR_FAILED_REMARK = v_dfr_appl_level
             WHERE DFR_CODE = v_dfr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while updating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_addedit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_dd_failed_remarks
                  WHERE DFR_CODE = v_dfr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while deleting record ' || SQLERRM (SQLCODE));
         END;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE_ERROR ('Remarks Proc ' || SQLERRM (SQLCODE));
   END;

    FUNCTION get_form_field (   
    v_sys_code             IN NUMBER,
    v_field_name           IN VARCHAR2,
    v_field_col            IN VARCHAR2
   ) RETURN VARCHAR2
   IS
    V_LABEL VARCHAR2(100):=NULL; 
    V_VISIBLE  VARCHAR2(100):=NULL; 
    V_SCREEN_NAME VARCHAR2(100):=NULL;  
    V_DISABLED VARCHAR2(100):=NULL; 
    V_MANDATORY VARCHAR2(100):=NULL; 
   BEGIN
       BEGIN
        SELECT FM_FIELD_LABEL, FM_VISIBLE, FM_SCREEN_NAME, FM_DISABLED,FM_MANDATORY
        INTO V_LABEL, V_VISIBLE, V_SCREEN_NAME, V_DISABLED,V_MANDATORY
         FROM TQC_FORM_FIELDS
         WHERE FM_FIELD_NAME=v_field_name; 
       END;
      IF v_field_col = 'MANDATORY'
      THEN 
           RETURN  V_MANDATORY; 
      ELSIF v_field_col = 'LABEL'
      THEN
          RETURN  V_LABEL; 
      ELSIF v_field_col = 'VISIBLE'
      THEN
         RETURN  V_VISIBLE; 
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         RAISE_ERROR ('Remarks Proc ' || SQLERRM (SQLCODE));
   END;
   PROCEDURE bank_details_prc(
                  v_add_edit            IN  VARCHAR,
                  v_bact_code           IN  tqc_bank_accounts.bact_code%TYPE,
                  v_bact_name           IN  tqc_bank_accounts.bact_name%TYPE,
                  v_bact_account_no     IN  tqc_bank_accounts.bact_account_no%TYPE,
                  v_bact_bbr_code       IN  tqc_bank_accounts.bact_bbr_code%TYPE,
                  v_bact_account_officer IN  tqc_bank_accounts.bact_account_officer%TYPE,
                  v_bact_cell_nos       IN  tqc_bank_accounts.bact_cell_nos%TYPE,
                  v_bact_tel_nos        IN  tqc_bank_accounts.bact_tel_nos%TYPE,
                  v_bact_account_type   IN  tqc_bank_accounts.bact_account_type%TYPE,
                  v_bact_default        IN  tqc_bank_accounts.bact_default%TYPE,
                  v_bact_cur_code       IN  tqc_bank_accounts.bact_cur_code%TYPE,
                  v_bact_acc_code       IN  tqc_bank_accounts.bact_acc_code%TYPE,
                  v_bact_iban           IN  tqc_bank_accounts.bact_iban%TYPE,
                  v_bact_status         IN  tqc_bank_accounts.bact_status%TYPE
                  )
   IS
   v_count      NUMBER;
   BEGIN
--     RAISE_ERROR('v_add_edit'||v_add_edit||
--      v_bact_code||'v_bact_code'||
--      v_bact_name||'v_bact_name'||
--      v_bact_account_no||'v_bact_account_no'||
--      v_bact_bbr_code||'v_bact_bbr_code'||
--      v_bact_account_officer||'v_bact_account_officer'||
--      v_bact_cell_nos||'v_bact_cell_nos'||
--      v_bact_tel_nos||'v_bact_tel_nos'||
--      v_bact_account_type||'v_bact_account_type'||
--      v_bact_default||'v_bact_default'||
--      v_bact_cur_code||'v_bact_cur_code'||
--      v_bact_acc_code||'v_bact_acc_code');
        
        SELECT COUNT(1) INTO v_count FROM tqc_bank_accounts
        WHERE bact_cur_code = v_bact_cur_code  and bact_default=v_bact_default AND bact_default = 'Y' and bact_acc_code=v_bact_acc_code
         and ( bact_code<>v_bact_code or v_bact_code is null); 


             IF v_add_edit='A'
                THEN
                    BEGIN
                      IF NVL(v_count,0)>0
                      THEN
                        RAISE_ERROR ('Default Account has already been set for this currency  '||v_bact_cur_code);
                        RETURN;
                      ELSE
                        BEGIN
                          INSERT INTO tqc_bank_accounts
                              (bact_code,bact_name,bact_account_no, bact_bbr_code,
                               bact_account_officer,bact_cell_nos,bact_tel_nos,
                               bact_account_type,bact_default,bact_cur_code,bact_acc_code,bact_iban,bact_status)
                          VALUES
                              (TQC_BNK_DTLS_SEQ.NEXTVAL,v_bact_name,v_bact_account_no, v_bact_bbr_code,
                               v_bact_account_officer,v_bact_cell_nos,v_bact_tel_nos,
                               v_bact_account_type,v_bact_default,v_bact_cur_code,v_bact_acc_code,v_bact_iban,v_bact_status);
                        EXCEPTION
                          WHEN OTHERS
                          THEN
                             RAISE_ERROR ('Error Adding Bank Details: ' || SQLERRM (SQLCODE));
                        END;
                      END IF;
                    END;
             ELSIF v_add_edit='E'
                THEN
                    BEGIN
                      IF NVL(v_count,0)>0
                      THEN
                        RAISE_ERROR ('Default Account has already been set for this currency  '||v_bact_cur_code);
                        RETURN;
                      ELSE
                        BEGIN
                          UPDATE tqc_bank_accounts SET
                              bact_name         = v_bact_name,
                              bact_account_no   = v_bact_account_no, 
                              bact_bbr_code     = v_bact_bbr_code,
                              bact_account_officer     = v_bact_account_officer,
                              bact_cell_nos     = v_bact_cell_nos,
                              bact_tel_nos      = v_bact_tel_nos,
                              bact_account_type = v_bact_account_type,
                              bact_default      = v_bact_default,
                              bact_cur_code     = v_bact_cur_code,
                              bact_acc_code     = v_bact_acc_code,
                              bact_iban         = v_bact_iban,
                              bact_status       = v_bact_status
                          WHERE
                              bact_code         = v_bact_code;
                    EXCEPTION
                      WHEN OTHERS
                      THEN
                         RAISE_ERROR ('Error Editing Bank Details: ' || SQLERRM (SQLCODE));
                    END;
                  END IF;
                 END;
             ELSIF v_add_edit='D'
                THEN
                    BEGIN
                        DELETE FROM tqc_bank_accounts WHERE  bact_code = v_bact_code;
                    EXCEPTION
                      WHEN OTHERS
                      THEN
                         RAISE_ERROR ('Error Deleting Bank Details: ' || SQLERRM (SQLCODE));
                    END;
             END IF;
--     EXCEPTION
--      WHEN OTHERS
--      THEN
--         RAISE_ERROR ('Bank Details Error: ' || SQLERRM (SQLCODE));
   END;
   PROCEDURE failRelaunchUpdateHeader
   (
            v_dddCode NUMBER,
            v_username VARCHAR2, 
            v_policyNo VARCHAR2,
            v_account VARCHAR2, 
            v_book_date DATE, 
            v_fail_remarks VARCHAR2, 
            v_ddh_dd_code IN OUT NUMBER,
            v_status VARCHAR DEFAULT 'F',
            v_bnk_amt varchar2 default null
    ) IS 
    v_cnt1              NUMBER;  
    v_cnt2              NUMBER; 
    v_ddh_code          NUMBER; 
    v_fail_date         DATE;
    v_count1         NUMBER; 
    v_count2         NUMBER; 
    v_dd_code        NUMBER;
    v_dd_ref_no      NUMBER;
    BEGIN
        BEGIN
        SELECT DD_REF_NO INTO v_dd_ref_no FROM 
        TQC_DIRECT_DEBIT
        WHERE 
        DD_CODE=v_ddh_dd_code;
        EXCEPTION 
         WHEN OTHERS THEN
         RAISE_ERROR('Error Fetching Batch REF.NO for Batch..'||v_ddh_dd_code);
        END;
        BEGIN
        SELECT DDD_DD_CODE INTO v_dd_code 
        FROM TQC_DIRECT_DEBIT_DETAIL 
        WHERE
        DDD_CODE=v_dddCode;
        EXCEPTION 
          WHEN NO_DATA_FOUND THEN 
          RAISE_ERROR('The direct debit record with EXPORT NUMBER..'||v_dddCode||'...'||'does not belong to the exported batch with REF.NO..'||v_dd_ref_no);
        END;
        IF v_dd_code!=v_ddh_dd_code THEN
        RAISE_ERROR('The direct debit record with EXPORT NUMBER..'||v_dddCode||'...'||'does not belong to the exported batch with REF.NO..'||v_dd_ref_no);
        END IF;
        IF v_username IS NULL THEN
            TQC_ERROR_MANAGER.RAISE_ERROR(12648,
                                         text_in => 'System UNABLE to verify connected user....RECONNECT SESSION....'
                                         , name1_in       => 'MODULE NAME'
                                          , value1_in      => 'FAILUPDATEDDHEADER');
                                          
        ELSIF v_book_date IS NULL THEN
         
             TQC_ERROR_MANAGER.RAISE_ERROR(12671,
                                         text_in => 'Provide the Direct Debit Book date to proceed....'
                                         , name1_in       => 'MODULE NAME'
                                       , value1_in      => 'FAILUPDATEDDHEADER');
--        ELSIF v_fail_remarks IS NULL THEN
--            TQC_ERROR_MANAGER.RAISE_ERROR(12672,
--                                         text_in => 'Provide the reason for failure to proceed....'
--                                         , name1_in       => 'MODULE NAME'
--                                          , value1_in      => 'FAILUPDATEDDHEADER');
        END IF;
          
        BEGIN
         v_fail_date := Direct_Debit_Pkg.Get_business_date(v_book_date,4);
         EXCEPTION
         WHEN OTHERS THEN
         RAISE_ERROR('Error Getting Fail Date'|| SQLERRM);
         END;
         
        --raise_error('v_dddCode='||v_dddCode);
        BEGIN
            SELECT DDH_DD_CODE,DDH_CODE INTO v_ddh_dd_code,v_ddh_code
            FROM TQC_DIRECT_DEBIT_HEADER,TQC_DIRECT_DEBIT_DETAIL
            WHERE DDD_DDH_CODE = DDH_CODE
            AND  DDD_CODE = v_dddCode;
        EXCEPTION
            WHEN OTHERS THEN
            RAISE_ERROR('Child records not found1....'|| v_policyNo||'Account'||v_account||' v_ddh_dd_code '||v_ddh_dd_code||';v_dddCode='||v_dddCode);
        END;
            
        BEGIN
            
            IF v_status = 'F' THEN
                TQC_SETUPS_PKG.relaunchDDHeader(v_username, v_ddh_code, v_status, v_ddh_dd_code);
            END IF;
            
            EXCEPTION
                WHEN OTHERS THEN
                RAISE_ERROR('Update Detail error....');         
            END;
--    EXCEPTION
--        WHEN OTHERS THEN
--            RAISE_ERROR('Error Updating DDH v_status='||v_status||' '|| SQLERRM);
                           
    END failRelaunchUpdateHeader;
PROCEDURE agencies_audit_prc (v_agn_code IN NUMBER, v_posted_by VARCHAR2)
   IS
      v_cnt   NUMBER := 0;

      CURSOR c_agent
      IS
         SELECT *
           FROM tqc_agencies
          WHERE agn_code = v_agn_code;
   BEGIN
      FOR c IN c_agent
      LOOP
         --  Raise_Error(' c.agn_name: '|| c.agn_name);
         INSERT INTO tqc_agencies_audit
                     (old_agn_code, old_agn_act_code, old_agn_sht_desc,
                      old_agn_name, old_agn_physical_address,
                      old_agn_postal_address, old_agn_twn_code,
                      old_agn_cou_code, old_agn_email_address,
                      old_agn_web_address, old_agn_zip,
                      old_agn_contact_person, old_agn_contact_title,
                      old_agn_tel1, old_agn_tel2, old_agn_fax,
                      old_agn_acc_no, old_agn_pin, old_agn_agent_commission,
                      old_agn_credit_allowed, old_agn_agent_wht_tax,
                      old_agn_print_dbnote, old_agn_status,
                      old_agn_reg_code, old_agn_comm_reserve_rate,
                      old_agn_annual_budget, old_agn_status_eff_date,
                      old_agn_credit_period, old_agn_comm_stat_eff_dt,
                      old_agn_comm_status_dt, old_agn_comm_allowed,
                      old_agn_checked, old_agn_checked_by,
                      old_agn_check_date, old_agn_comp_comm_arrears,
                      old_agn_reinsurer, old_agn_brn_code, old_agn_town,
                      old_agn_country, old_agn_status_desc, old_agn_id_no,
                      old_agn_con_code, old_agn_sms_tel, old_agn_ahc_code,
                      old_agn_sec_code, old_agn_agnc_class_code,
                      old_agn_expiry_date, old_agn_license_no,
                      old_agn_runoff, old_agn_licensed,
                      old_agn_license_grace_pr, old_agn_old_acc_no,
                      old_agn_status_remarks, old_agn_osd_code,
                      old_agn_bank_acc_no, old_agn_unique_prefix,
                      old_agn_bbr_code, old_agn_inique_prefix,
                      old_agn_state_code, old_agn_bbr_acc_code,
                      old_agn_crdt_rting, old_agn_subagent,
                      old_agn_main_agn_code, old_agn_agn_code,
                      old_agn_clnt_code, old_agn_payee,
                      old_agn_account_manager, old_agn_enable_web_edit,
                      old_agn_birth_date, old_agn_credit_limit,
                      old_agn_bru_code, old_agn_local_international,
                      old_agn_regulator_number, old_agn_authorised,
                      old_agn_authorised_by, old_agn_authorised_date,
                      old_agn_ors_code, old_agn_rorg_code,
                      old_agn_allocate_cert, old_agn_bounced_chq,
                      old_agn_agent_type, old_agn_bpn_code, old_agn_group,
                      old_agn_forgn, old_agn_whtax_applicable,
                      old_agn_vat_applicable, old_agn_default_comm_mode,
                      old_agn_tel_pay, old_agn_payment_freq,
                      old_agn_default_cpm_mode, old_agn_gl_rcvbl_acc_no,
                      old_agn_paydetail_validated, old_agn_inst_comm_allwd,
                      old_agn_comm_levy_applic, old_agn_comm_levy_rate,
                      old_agn_pymt_validated, new_agn_code,
                      new_agn_act_code, new_agn_sht_desc, new_agn_name,
                      new_agn_physical_address, new_agn_postal_address,
                      new_agn_twn_code, new_agn_cou_code,
                      new_agn_email_address, new_agn_web_address,
                      new_agn_zip, new_agn_contact_person,
                      new_agn_contact_title, new_agn_tel1, new_agn_tel2,
                      new_agn_fax, new_agn_acc_no, new_agn_pin,
                      new_agn_agent_commission, new_agn_credit_allowed,
                      new_agn_agent_wht_tax, new_agn_print_dbnote,
                      new_agn_status, new_agn_reg_code,
                      new_agn_comm_reserve_rate, new_agn_annual_budget,
                      new_agn_status_eff_date, new_agn_credit_period,
                      new_agn_comm_stat_eff_dt, new_agn_comm_status_dt,
                      new_agn_comm_allowed, new_agn_checked,
                      new_agn_checked_by, new_agn_check_date,
                      new_agn_comp_comm_arrears, new_agn_reinsurer,
                      new_agn_brn_code, new_agn_town, new_agn_country,
                      new_agn_status_desc, new_agn_id_no, new_agn_con_code,
                      new_agn_sms_tel, new_agn_ahc_code, new_agn_sec_code,
                      new_agn_agnc_class_code, new_agn_expiry_date,
                      new_agn_license_no, new_agn_runoff, new_agn_licensed,
                      new_agn_license_grace_pr, new_agn_new_acc_no,
                      new_agn_status_remarks, new_agn_osd_code,
                      new_agn_bank_acc_no, new_agn_unique_prefix,
                      new_agn_bbr_code, new_agn_inique_prefix,
                      new_agn_state_code, new_agn_bbr_acc_code,
                      new_agn_crdt_rting, new_agn_subagent,
                      new_agn_main_agn_code, new_agn_agn_code,
                      new_agn_clnt_code, new_agn_payee,
                      new_agn_account_manager, new_agn_enable_web_edit,
                      new_agn_birth_date, new_agn_credit_limit,
                      new_agn_bru_code, new_agn_local_international,
                      new_agn_regulator_number, new_agn_authorised,
                      new_agn_authorised_by, new_agn_authorised_date,
                      new_agn_ors_code, new_agn_rorg_code,
                      new_agn_allocate_cert, new_agn_bounced_chq,
                      new_agn_agent_type, new_agn_bpn_code, new_agn_group,
                      new_agn_forgn, new_agn_whtax_applicable,
                      new_agn_vat_applicable, new_agn_default_comm_mode,
                      new_agn_tel_pay, new_agn_payment_freq,
                      new_agn_default_cpm_mode, new_agn_gl_rcvbl_acc_no,
                      new_agn_paydetail_validated, new_agn_inst_comm_allwd,
                      new_agn_comm_levy_applic, new_agn_comm_levy_rate,
                      new_agn_pymt_validated, changed_by, changed_date
                     )
              VALUES (v_agn_code, c.agn_act_code, c.agn_sht_desc,
                      c.agn_name, c.agn_physical_address,
                      c.agn_postal_address, c.agn_twn_code,
                      c.agn_cou_code, c.agn_email_address,
                      c.agn_web_address, c.agn_zip,
                      c.agn_contact_person, c.agn_contact_title,
                      c.agn_tel1, c.agn_tel2, c.agn_fax,
                      c.agn_acc_no, c.agn_pin, c.agn_agent_commission,
                      c.agn_credit_allowed, c.agn_agent_wht_tax,
                      c.agn_print_dbnote, c.agn_status,
                      c.agn_reg_code, c.agn_comm_reserve_rate,
                      c.agn_annual_budget, c.agn_status_eff_date,
                      c.agn_credit_period, c.agn_comm_stat_eff_dt,
                      c.agn_comm_status_dt, c.agn_comm_allowed,
                      c.agn_checked, c.agn_checked_by,
                      c.agn_check_date, c.agn_comp_comm_arrears,
                      c.agn_reinsurer, c.agn_brn_code, c.agn_town,
                      c.agn_country, c.agn_status_desc, c.agn_id_no,
                      c.agn_con_code, c.agn_sms_tel, c.agn_ahc_code,
                      c.agn_sec_code, c.agn_agnc_class_code,
                      c.agn_expiry_date, c.agn_license_no,
                      c.agn_runoff, c.agn_licensed,
                      c.agn_license_grace_pr, c.agn_old_acc_no,
                      c.agn_status_remarks, c.agn_osd_code,
                      c.agn_bank_acc_no, c.agn_unique_prefix,
                      c.agn_bbr_code, c.agn_unique_prefix,
                      c.agn_state_code, c.agn_bbr_acc_code,
                      c.agn_crdt_rting, c.agn_subagent,
                      c.agn_main_agn_code, c.agn_agn_code,
                      c.agn_clnt_code, c.agn_payee,
                      c.agn_account_manager, c.agn_enable_web_edit,
                      c.agn_birth_date, c.agn_credit_limit,
                      c.agn_bru_code, c.agn_local_international,
                      c.agn_regulator_number, c.agn_authorised,
                      c.agn_authorised_by, c.agn_authorised_date,
                      c.agn_ors_code, c.agn_rorg_code,
                      c.agn_allocate_cert, c.agn_bounced_chq,
                      c.agn_agent_type, c.agn_bpn_code, c.agn_group,
                      'N', c.agn_whtax_applicable,
                      c.agn_vat_applicable, c.agn_default_comm_mode,
                      c.agn_tel_pay, c.agn_payment_freq,
                      c.agn_default_cpm_mode, NULL,
                      'N', 'N',
                      c.agn_comm_levy_app, c.agn_comm_levy_rate,
                      c.agn_pymt_validated, c.agn_code,
                      c.agn_act_code, c.agn_sht_desc, c.agn_name,
                      c.agn_physical_address, c.agn_postal_address,
                      c.agn_twn_code, c.agn_cou_code,
                      c.agn_email_address, c.agn_web_address,
                      c.agn_zip, c.agn_contact_person,
                      c.agn_contact_title, c.agn_tel1, c.agn_tel2,
                      c.agn_fax, c.agn_acc_no, c.agn_pin,
                      c.agn_agent_commission, c.agn_credit_allowed,
                      c.agn_agent_wht_tax, c.agn_print_dbnote,
                      c.agn_status, c.agn_reg_code,
                      c.agn_comm_reserve_rate, c.agn_annual_budget,
                      c.agn_status_eff_date, c.agn_credit_period,
                      c.agn_comm_stat_eff_dt, c.agn_comm_status_dt,
                      c.agn_comm_allowed, c.agn_checked,
                      c.agn_checked_by, c.agn_check_date,
                      c.agn_comp_comm_arrears, c.agn_reinsurer,
                      c.agn_brn_code, c.agn_town, c.agn_country,
                      c.agn_status_desc, c.agn_id_no, c.agn_con_code,
                      c.agn_sms_tel, c.agn_ahc_code, c.agn_sec_code,
                      c.agn_agnc_class_code, c.agn_expiry_date,
                      c.agn_license_no, c.agn_runoff, c.agn_licensed,
                      c.agn_license_grace_pr, c.agn_acc_no,
                      c.agn_status_remarks, c.agn_osd_code,
                      c.agn_bank_acc_no, c.agn_unique_prefix,
                      c.agn_bbr_code, c.agn_unique_prefix,
                      c.agn_state_code, c.agn_bbr_acc_code,
                      c.agn_crdt_rting, c.agn_subagent,
                      c.agn_main_agn_code, c.agn_agn_code,
                      c.agn_clnt_code, c.agn_payee,
                      c.agn_account_manager, c.agn_enable_web_edit,
                      c.agn_birth_date, c.agn_credit_limit,
                      c.agn_bru_code, c.agn_local_international,
                      c.agn_regulator_number, c.agn_authorised,
                      c.agn_authorised_by, c.agn_authorised_date,
                      c.agn_ors_code, c.agn_rorg_code,
                      c.agn_allocate_cert, c.agn_bounced_chq,
                      c.agn_agent_type, c.agn_bpn_code, c.agn_group,
                      'N', c.agn_whtax_applicable,
                      c.agn_vat_applicable, c.agn_default_comm_mode,
                      c.agn_tel_pay, c.agn_payment_freq,
                      c.agn_default_cpm_mode, NULL,
                      'N', 'N',
                      c.agn_comm_levy_app, c.agn_comm_levy_rate,
                      c.agn_pymt_validated, v_posted_by, SYSDATE
                     );
      END LOOP;
   END agencies_audit_prc; 
PROCEDURE spr_contact_persons_prc (
      v_add_edit         IN   VARCHAR,
      v_spr_short_desc   IN   NUMBER DEFAULT NULL,
      v_spr_code         IN   tqc_spr_contact_persons.spr_code%TYPE,
      v_sprcontname      IN   tqc_spr_contact_persons.spr_cnt_name%TYPE,
      v_sprconttitle     IN   tqc_spr_contact_persons.spr_cnt_title%TYPE,
      v_sprconttelno     IN   tqc_spr_contact_persons.spr_cnt_office_tel%TYPE,
      v_sprcontmobno     IN   tqc_spr_contact_persons.spr_cnt_home_tel%TYPE,
      v_sprcontemail     IN   tqc_spr_contact_persons.spr_cnt_email%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
--     RAISE_ERROR('v_add_edit'||v_add_edit||
--      v_spr_code||'v_spr_code'||
--      v_sprContName||'v_sprContName'||
--      v_sprContTitle||'v_sprContTitle'||
--      v_sprContTelNo||'v_sprContTelNo'||
--      v_sprContMobNo||'v_sprContMobNo'||
--      v_sprContEmail||'v_sprContEmail'||
      SELECT COUNT (1)
        INTO v_count
        FROM tqc_spr_contact_persons
       WHERE spr_cnt_email = v_sprcontemail;

      IF v_add_edit = 'A'
      THEN
         BEGIN
            IF NVL (v_count, 0) > 0
            THEN
               raise_error
                          (   'Contact has already been taken for this Email'
                           || v_sprcontemail
                          );
               RETURN;
            ELSE
               BEGIN
                  INSERT INTO tqc_spr_contact_persons
                              (spr_cnt_code, spr_code,
                               spr_cnt_name, spr_cnt_title,
                               spr_cnt_office_tel, spr_cnt_home_tel,
                               spr_cnt_email
                              )
                       VALUES (tqc_cnt_dtls_seq.NEXTVAL, v_spr_code,
                               v_sprcontname, v_sprconttitle,
                               v_sprconttelno, v_sprcontmobno,
                               v_sprcontemail
                              );
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error (   'Error Adding Contact Details: '
                                  || SQLERRM (SQLCODE)
                                 );
               END;
            END IF;
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            --raise_error('v_spr_short_desc'|| v_spr_short_desc);
            UPDATE tqc_spr_contact_persons
               SET spr_cnt_name = v_sprcontname,
                   spr_cnt_title = v_sprconttitle,
                   spr_cnt_office_tel = v_sprconttelno,
                   spr_cnt_home_tel = v_sprcontmobno,
                   spr_cnt_email = v_sprcontemail
             WHERE spr_cnt_code = v_spr_short_desc;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Editing Contact Details: '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_spr_contact_persons
                  WHERE spr_cnt_code = v_spr_short_desc;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Deleting Contact Details: '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
--     EXCEPTION
--      WHEN OTHERS
--      THEN
--         RAISE_ERROR ('Bank Details Error: ' || SQLERRM (SQLCODE));
   END;    
END tqc_setups_pkg;
/