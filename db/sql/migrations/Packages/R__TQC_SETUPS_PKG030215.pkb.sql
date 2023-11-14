--
-- TQC_SETUPS_PKG030215  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_setups_pkg030215
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

            INSERT INTO tqc_agency_holding_company
                        (ahc_code, ahc_name,
                         ahc_postal_address, ahc_physical_address,
                         ahc_telephone_number, ahc_mobile_number,
                         ahc_contact_person
                        )
                 VALUES (tqc_ahc_code_seq.NEXTVAL, v_ahc_name,
                         v_postaladdress, v_physicaladdress,
                         v_telnumber, v_mobnumber,
                         v_contactperson
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
      v_add_edit                  IN       VARCHAR2,
      v_bnk_code                  IN       tqc_banks.bnk_code%TYPE,
      v_bnk_bank_name             IN       tqc_banks.bnk_bank_name%TYPE,
      v_bnk_remarks               IN       tqc_banks.bnk_remarks%TYPE,
      v_bnk_sht_desc              IN       tqc_banks.bnk_sht_desc%TYPE,
      v_bnk_ddr_code              IN       tqc_banks.bnk_ddr_code%TYPE,
      v_dd_format_desc            IN       tqc_banks.dd_format_desc%TYPE,
      v_bnk_dd_supported          IN      tqc_banks.bnk_dd_supported%TYPE,
      v_bnk_kba_code              IN       tqc_banks.bnk_kba_code%TYPE,
      v_bnk_eft_supported         IN       tqc_banks.bnk_eft_supported%TYPE,
      v_bnk_class_type            IN       tqc_banks.bnk_class_type%TYPE,
      v_bnk_accnt_digit_no        IN       tqc_banks.bnk_accnt_digit_no%TYPE,
      v_bnk_negotiated_bank       IN       tqc_banks.bnk_negotiated_bank%TYPE,
      v_bnk_administration_charge IN       tqc_banks.bnk_administration_charge%TYPE,
      v_bnk_logo IN       tqc_banks.BNK_LOGO%TYPE,
      v_error                     OUT      VARCHAR2
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

            INSERT INTO tqc_banks
                        (bnk_code, bnk_bank_name,
                         bnk_remarks, bnk_sht_desc, bnk_ddr_code,
                         dd_format_desc, bnk_dd_supported,
                         bnk_kba_code,bnk_eft_supported,bnk_class_type,bnk_accnt_digit_no,
                         bnk_negotiated_bank, bnk_administration_charge,BNK_LOGO
                        )
                 VALUES (tqc_bnk_code_seq.NEXTVAL, v_bnk_bank_name,
                         v_bnk_remarks, v_bnk_sht_desc, v_bnk_ddr_code,
                         v_dd_format_desc, v_bnk_dd_supported,
                         v_bnk_kba_code,v_bnk_eft_supported,v_bnk_class_type,v_bnk_accnt_digit_no,
                          v_bnk_negotiated_bank, v_bnk_administration_charge,v_bnk_logo
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
         
         IF v_bnk_logo   IS NULL
         THEN
            UPDATE tqc_banks
               SET bnk_bank_name = v_bnk_bank_name,
                   bnk_remarks = v_bnk_remarks,
                   bnk_sht_desc = v_bnk_sht_desc,
                   bnk_ddr_code = v_bnk_ddr_code,
                   dd_format_desc = v_dd_format_desc,
                   bnk_dd_supported = v_bnk_dd_supported,
                   bnk_kba_code = v_bnk_kba_code,
                   bnk_eft_supported=v_bnk_eft_supported,
                   bnk_class_type=v_bnk_class_type,
                   bnk_accnt_digit_no=v_bnk_accnt_digit_no,
                   bnk_negotiated_bank=v_bnk_negotiated_bank, 
                   bnk_administration_charge=v_bnk_administration_charge
             WHERE bnk_code = v_bnk_code;
           ELSE
                UPDATE tqc_banks
               SET bnk_bank_name = v_bnk_bank_name,
                   bnk_remarks = v_bnk_remarks,
                   bnk_sht_desc = v_bnk_sht_desc,
                   bnk_ddr_code = v_bnk_ddr_code,
                   dd_format_desc = v_dd_format_desc,
                   bnk_dd_supported = v_bnk_dd_supported,
                   bnk_kba_code = v_bnk_kba_code,
                   bnk_eft_supported=v_bnk_eft_supported,
                   bnk_class_type=v_bnk_class_type,
                   bnk_accnt_digit_no=v_bnk_accnt_digit_no,
                   bnk_negotiated_bank=v_bnk_negotiated_bank, 
                   bnk_administration_charge=v_bnk_administration_charge,
                   bnk_logo=v_bnk_logo
             WHERE bnk_code = v_bnk_code;
            
           END IF;
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
      v_add_edit             IN       VARCHAR2,
      v_bbr_code             IN       tqc_bank_branches.bbr_code%TYPE,
      v_bbr_bnk_code         IN       tqc_bank_branches.bbr_bnk_code%TYPE,
      v_bbr_branch_name      IN       tqc_bank_branches.bbr_branch_name%TYPE,
      v_bbr_remarks          IN       tqc_bank_branches.bbr_remarks%TYPE,
      v_bbr_sht_desc         IN       tqc_bank_branches.bbr_sht_desc%TYPE,
      v_bbr_ref_code         IN       tqc_bank_branches.bbr_ref_code%TYPE,
      v_bbr_eft_supported    IN       tqc_bank_branches.bbr_eft_supported%TYPE,
      v_bbr_dd_supported     IN       tqc_bank_branches.bbr_dd_supported%TYPE,
      v_bbr_date_created     IN       tqc_bank_branches.bbr_date_created%TYPE,
      v_bbr_created_by       IN       tqc_bank_branches.bbr_created_by%TYPE,
      v_bbr_physical_addrs   IN       tqc_bank_branches.bbr_physical_addrs%TYPE,
      v_bbr_postal_addrs     IN       tqc_bank_branches.bbr_postal_addrs%TYPE,
      v_bbr_kba_code         IN       tqc_bank_branches.bbr_kba_code%TYPE,
      v_error                OUT      VARCHAR2
   )
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
             WHERE bbr_bnk_code(+) = bnk_code
               AND bbr_bnk_code = v_bbr_bnk_code
               AND bbr_branch_name = v_bbr_branch_name;

            IF v_count != 0
            THEN
               v_error := 'Bank Branch of the Same Name Exists';
               RETURN;
            END IF;

            INSERT INTO tqc_bank_branches
                        (bbr_code, bbr_bnk_code,
                         bbr_branch_name, bbr_remarks, bbr_sht_desc,
                         bbr_ref_code, bbr_eft_supported,
                         bbr_dd_supported, bbr_date_created,
                         bbr_created_by, bbr_physical_addrs,
                         bbr_postal_addrs, bbr_kba_code
                        )
                 VALUES (tqc_bbr_code_seq.NEXTVAL, v_bbr_bnk_code,
                         v_bbr_branch_name, v_bbr_remarks, v_bbr_sht_desc,
                         v_bbr_ref_code, v_bbr_eft_supported,
                         v_bbr_dd_supported, v_bbr_date_created,
                         v_bbr_created_by, v_bbr_physical_addrs,
                         v_bbr_postal_addrs, v_bbr_kba_code
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
                   bbr_kba_code = v_bbr_kba_code
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

            INSERT INTO tqc_agencies_classes
                        (agnc_class_code, agnc_class_desc
                        )
                 VALUES (tqc_agnc_class_code_seq.NEXTVAL, v_agnc_class_desc
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
      v_add_edit     IN       VARCHAR2,
      v_cur_code     IN       tqc_currencies.cur_code%TYPE,
      v_cur_symbol   IN       tqc_currencies.cur_symbol%TYPE,
      v_cur_desc     IN       tqc_currencies.cur_desc%TYPE,
      v_cur_rnd      IN       tqc_currencies.cur_rnd%TYPE,
      v_error        OUT      VARCHAR2,
      v_cur_num_word      IN       tqc_currencies.cur_num_word%TYPE,
      v_cur_decimal_word     IN       tqc_currencies.cur_decimal_word%TYPE
   )
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

            INSERT INTO tqc_currencies
                        (cur_code, cur_symbol, cur_desc,
                         cur_rnd,cur_num_word, cur_decimal_word
                        )
                 VALUES (tqc_cur_code_seq.NEXTVAL, v_cur_symbol, v_cur_desc,
                         v_cur_rnd,v_cur_num_word, v_cur_decimal_word
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
            UPDATE tqc_currencies
               SET cur_symbol = v_cur_symbol,
                   cur_desc = v_cur_desc,
                   cur_rnd = v_cur_rnd,
                   cur_num_word=v_cur_num_word,
                   cur_decimal_word=v_cur_decimal_word
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
      v_add_edit       IN       VARCHAR2,
      v_cud_code       IN       tqc_currency_denominations.cud_code%TYPE,
      v_cud_cur_code   IN       tqc_currency_denominations.cud_cur_code%TYPE,
      v_cud_value      IN       tqc_currency_denominations.cud_value%TYPE,
      v_cud_name       IN       tqc_currency_denominations.cud_name%TYPE,
      v_cud_wef        IN       tqc_currency_denominations.cud_wef%TYPE,
      v_error          OUT      VARCHAR2
   )
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

            INSERT INTO tqc_currency_denominations
                        (cud_code, cud_cur_code,
                         cud_value, cud_name, cud_wef
                        )
                 VALUES (tqc_cud_code_seq.NEXTVAL, v_cud_cur_code,
                         v_cud_value, v_cud_name, v_cud_wef
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
      v_crt_date            IN tqc_currency_rates.crt_date%TYPE,
      v_crt_base_cur_code   IN tqc_currency_rates.crt_base_cur_code%TYPE,
      v_crt_wef             IN tqc_currency_rates.CRT_WEF%TYPE,
      v_crt_wet             IN tqc_currency_rates.CRT_WET%TYPE,
      v_user                IN VARCHAR2 DEFAULT NULL)
   IS
      CURSOR CURRREF
      IS
        SELECT CRT_CODE,
                CRT_CUR_CODE,
                CRT_RATE,
                CRT_DATE,
                CRT_BASE_CUR_CODE,
                CRT_WEF,
                CRT_WET
           FROM TQC_CURRENCY_RATES   
          WHERE     CRT_CUR_CODE = v_crt_cur_code
                AND SYSDATE BETWEEN CRT_WEF AND CRT_WET;
      v_count       NUMBER;

      v_rec_count   NUMBER;
   BEGIN
   
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_currency_rates
             WHERE crt_code = v_crt_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;

             IF v_CRT_WET < SYSDATE THEN
                  raise_error ('Wet cannot be less than Today');
               END IF;
               
            FOR I IN CURRREF  LOOP 

               UPDATE tqc_currency_rates
                  SET crt_wet = SYSDATE
                WHERE v_crt_wef BETWEEN I.CRT_WEF AND I.CRT_WET
                      AND crt_cur_code = v_crt_cur_code
                      AND CRT_CODE=I.CRT_CODE ;

               UPDATE tqc_currency_rates
                  SET crt_wet = SYSDATE
                WHERE v_crt_wet BETWEEN I.CRT_WEF AND I.CRT_WET
                      AND crt_cur_code = v_crt_cur_code
                      AND CRT_CODE=I.CRT_CODE ;
            /*
             IF v_crt_wef BETWEEN I.CRT_WEF AND I.CRT_WET
             THEN
             RAISE_ERROR('Your Currency Period Overlaps with another period.Please check');
             END IF;

              IF v_crt_wet BETWEEN I.CRT_WEF AND I.CRT_WET
             THEN
             RAISE_ERROR('Your Currency Period Overlaps with another period.Please check');
             END IF;*/


            END LOOP;
--RAISE_ERROR(TO_DATE(v_CRT_WET||' 10:23:49 AM','DD/MM/RRRR hh24:mi:ss'));

            IF NVL (v_rec_count, 0) = 0
            THEN
               INSERT INTO tqc_currency_rates (crt_code,
                                               crt_cur_code,
                                               crt_rate,
                                               crt_date,
                                               crt_base_cur_code,
                                               CRT_WEF,
                                               CRT_WET,
                                               crt_created_by,
                                               crt_created_date)
                    VALUES (tqc_crt_code_seq.NEXTVAL,
                            v_crt_cur_code,
                            v_crt_rate,
                            SYSDATE,
                            v_crt_base_cur_code,
                            v_CRT_WEF,
                            TO_DATE(v_CRT_WET, 'DD/MM/RRRR')  + INTERVAL '0 23:59:59.59' DAY TO SECOND,
                            v_user,
                            SYSDATE);
            ELSIF NVL (v_rec_count, 0) > 0 -- WHAT DOES THIS ACHIEVE AND THE IS NO ASSIGNING OF THE VALUE
            THEN
               INSERT INTO tqc_currency_rates (crt_code,
                                               crt_cur_code,
                                               crt_rate,
                                               crt_date,
                                               crt_base_cur_code,
                                               CRT_WEF,
                                               CRT_WET,
                                               crt_created_by,
                                               crt_created_date)
                    VALUES (tqc_crt_code_seq.NEXTVAL,
                            v_crt_cur_code,
                            v_crt_rate,
                            SYSDATE,
                            v_crt_base_cur_code,
                            SYSDATE,
                            TO_DATE(v_CRT_WET, 'DD/MM/RRRR')  + INTERVAL '0 23:59:59.59' DAY TO SECOND,
                            v_user,
                            SYSDATE);

               v_rec_count := NVL (v_rec_count, 0) + 1;
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (
                  'Error occured while creating record ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            FOR I IN CURRREF
            LOOP
               IF v_crt_wef BETWEEN I.CRT_WEF AND I.CRT_WET
               THEN
                  RAISE_ERROR (
                     'Your Currency Period Overlaps with another period.Please check');
               END IF;

               IF v_crt_wet BETWEEN I.CRT_WEF AND I.CRT_WET
               THEN
                  RAISE_ERROR (
                     'Your Currency Period Overlaps with another period.Please check');
               END IF;
            END LOOP;

            UPDATE tqc_currency_rates
               SET crt_cur_code = v_crt_cur_code,
                   -- crt_rate = v_crt_rate,
                   -- crt_date = v_crt_date,
                   crt_base_cur_code = v_crt_base_cur_code,
                   crt_updated_by = v_user,
                   crt_updated_date = SYSDATE
             WHERE crt_code = v_crt_code;
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
   PROCEDURE currency_rates_prcBK (
      v_add_edit            IN   VARCHAR2,
      v_crt_code            IN   tqc_currency_rates.crt_code%TYPE,
      v_crt_cur_code        IN   tqc_currency_rates.crt_cur_code%TYPE,
      v_crt_rate            IN   tqc_currency_rates.crt_rate%TYPE,
      v_crt_date            IN   tqc_currency_rates.crt_date%TYPE,
      v_crt_base_cur_code   IN   tqc_currency_rates.crt_base_cur_code%TYPE,
       v_crt_wef            IN   tqc_currency_rates.CRT_WEF%TYPE,
        v_crt_wet           IN   tqc_currency_rates.CRT_WET%TYPE,
        v_user              IN   VARCHAR2 default null
   )
   IS
   CURSOR CURRREF IS SELECT CRT_CODE, CRT_CUR_CODE, CRT_RATE, CRT_DATE, CRT_BASE_CUR_CODE, CRT_WEF, CRT_WET
             FROM tqc_currency_rates
             WHERE CRT_CUR_CODE=v_crt_cur_code;
      v_count   NUMBER;
      
      v_rec_count NUMBER;
   BEGIN
  
      IF v_add_edit = 'A'
      THEN
         BEGIN
         
         
         
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_currency_rates
             WHERE crt_code = v_crt_code;
           
           IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
            END IF;
           
             FOR I IN CURRREF
             LOOP
             
                IF v_CRT_WET < SYSDATE
                THEN
                  raise_error ('Wet cannot be less than Today');
                END IF;
             
                  UPDATE tqc_currency_rates
                  SET crt_wet = SYSDATE
                  WHERE v_crt_wef BETWEEN I.CRT_WEF AND I.CRT_WET
                  AND crt_cur_code = v_crt_cur_code ;
                  
                   UPDATE tqc_currency_rates
                  SET crt_wet = SYSDATE
                  WHERE v_crt_wet BETWEEN I.CRT_WEF AND I.CRT_WET
                   AND crt_cur_code = v_crt_cur_code ;
                  
                    
                
                 /* 
                  IF v_crt_wef BETWEEN I.CRT_WEF AND I.CRT_WET
                  THEN
                  RAISE_ERROR('Your Currency Period Overlaps with another period.Please check');
                  END IF;
                  
                   IF v_crt_wet BETWEEN I.CRT_WEF AND I.CRT_WET
                  THEN
                  RAISE_ERROR('Your Currency Period Overlaps with another period.Please check');
                  END IF;*/
             
             
             END LOOP;
            
            IF NVL(v_rec_count,0) = 0
            THEN
            INSERT INTO tqc_currency_rates
                        (crt_code, crt_cur_code,
                         crt_rate, crt_date, crt_base_cur_code,
                         CRT_WEF, CRT_WET,crt_created_by, crt_created_date
                        )
                 VALUES (tqc_crt_code_seq.NEXTVAL, v_crt_cur_code,
                         v_crt_rate, sysdate, v_crt_base_cur_code,
                         v_CRT_WEF, v_CRT_WET,v_user,sysdate
                        );
            ELSIF NVL(v_rec_count,0) > 0
            THEN
                 INSERT INTO tqc_currency_rates
                        (crt_code, crt_cur_code,
                         crt_rate, crt_date, crt_base_cur_code,
                         CRT_WEF, CRT_WET,crt_created_by, crt_created_date
                        )
                 VALUES (tqc_crt_code_seq.NEXTVAL, v_crt_cur_code,
                         v_crt_rate, sysdate, v_crt_base_cur_code,
                         SYSDATE, v_CRT_WET,v_user,sysdate
                        );
                  v_rec_count :=NVL(v_rec_count,0) + 1;
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
         
           FOR I IN CURRREF
             LOOP
                  IF v_crt_wef BETWEEN I.CRT_WEF AND I.CRT_WET
                  THEN
                  RAISE_ERROR('Your Currency Period Overlaps with another period.Please check');
                  END IF;
                  
                   IF v_crt_wet BETWEEN I.CRT_WEF AND I.CRT_WET
                  THEN
                  RAISE_ERROR('Your Currency Period Overlaps with another period.Please check');
                  END IF;
             
             
             END LOOP;
         
            UPDATE tqc_currency_rates
               SET crt_cur_code = v_crt_cur_code,
                  -- crt_rate = v_crt_rate,
                  -- crt_date = v_crt_date,
                   crt_base_cur_code = v_crt_base_cur_code,
                   crt_updated_by=v_user, 
                   crt_updated_date=sysdate
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

   PROCEDURE acc_types_prc (
      v_act_code          NUMBER,
      v_clnt_mapping      VARCHAR2,
      v_wht_rate          NUMBER,
      v_ovr_rate          NUMBER,
      v_comm_rate         NUMBER,
      v_ac_format         VARCHAR2,
      v_vat_rate          NUMBER,
      v_sht_desc          VARCHAR2,
      v_act_no_gen_code   tqc_account_types.act_no_gen_code%TYPE
   )
   IS
   BEGIN
      -- raise_error('Error occured while Updating record*** '||v_act_no_gen_code);
      UPDATE tqc_account_types
         SET act_wthtx_rate = NVL (v_wht_rate, act_wthtx_rate),
             act_account_type = NVL (v_clnt_mapping, act_account_type),
             act_overide_rate = NVL (v_ovr_rate, act_overide_rate),
             act_comm_reserve_rate = NVL (v_comm_rate, act_comm_reserve_rate),
             act_format = NVL (v_ac_format, act_id_serial_format),
             act_vat_rate = NVL (v_vat_rate, act_vat_rate),
             act_id_serial_format = v_sht_desc,
             act_no_gen_code = v_act_no_gen_code
       WHERE act_code = v_act_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error occured while Updating record '
                      || SQLERRM (SQLCODE)
                     );
   END;

   PROCEDURE sectors_prc (
      v_add_edit       IN   VARCHAR2,
      v_sec_code       IN   tqc_sectors.sec_code%TYPE,
      v_sec_sht_desc   IN   tqc_sectors.sec_sht_desc%TYPE,
      v_sec_name       IN   tqc_sectors.sec_name%TYPE
   )
   IS
      v_count   NUMBER;
   BEGIN
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
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
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
      v_add_edit        IN   VARCHAR2,
      v_spta_code       IN   tqc_serv_prv_type_actvts.spta_code%TYPE,
      v_spta_spt_code   IN   tqc_serv_prv_type_actvts.spta_spt_code%TYPE,
      v_spta_sht_desc   IN   tqc_serv_prv_type_actvts.spta_sht_desc%TYPE,
      v_spta_desc       IN   tqc_serv_prv_type_actvts.spta_desc%TYPE,
      v_spta_sms_msgt_code IN   tqc_serv_prv_type_actvts.spta_sms_msgt_code%TYPE DEFAULT NULL, 
      v_spta_email_msgt_code IN   tqc_serv_prv_type_actvts.spta_email_msgt_code%TYPE DEFAULT NULL,
       v_spta_send_msg_default IN   tqc_serv_prv_type_actvts.SPTA_SEND_MSG_DEFAULT%TYPE DEFAULT NULL, 
      v_spta_send_email_default IN   tqc_serv_prv_type_actvts.SPTA_SEND_EMAIL_DEFAULT%TYPE DEFAULT NULL
      
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_serv_prv_type_actvts
                     (spta_code, spta_spt_code,
                      spta_sht_desc, spta_desc,
                      spta_sms_msgt_code, spta_email_msgt_code,
                      spta_send_msg_default, spta_send_email_default
                     )
              VALUES (tqc_spta_code_seq.NEXTVAL, v_spta_spt_code,
                      v_spta_sht_desc, v_spta_desc,
                      v_spta_sms_msgt_code, v_spta_email_msgt_code,
                      v_spta_send_msg_default, v_spta_send_email_default
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_serv_prv_type_actvts
            SET spta_spt_code = v_spta_spt_code,
                spta_sht_desc = v_spta_sht_desc,
                spta_desc = v_spta_desc,
                spta_sms_msgt_code=v_spta_sms_msgt_code, 
                spta_email_msgt_code=v_spta_email_msgt_code,
                spta_send_msg_default=v_spta_send_msg_default, 
                spta_send_email_default=v_spta_send_email_default
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
      v_spt_vat_rate    IN   tqc_service_provider_types.spt_vat_rate%TYPE
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
                         spt_vat_rate
                        )
                 VALUES (tqc_spt_code_seq.NEXTVAL, v_spt_sht_desc,
                         v_spt_name, v_spt_status, v_spt_whtx_rate,
                         v_spt_vat_rate
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
                   spt_vat_rate = v_spt_vat_rate
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
      v_add_edit             IN   VARCHAR2,
      v_cou_code             IN   tqc_countries.cou_code%TYPE DEFAULT NULL,
      v_cou_sht_desc         IN   tqc_countries.cou_sht_desc%TYPE DEFAULT NULL,
      v_cou_name             IN   tqc_countries.cou_name%TYPE DEFAULT NULL,
      v_cou_base_curr        IN   tqc_countries.cou_base_curr%TYPE
            DEFAULT NULL,
      v_cou_nationality      IN   tqc_countries.cou_nationality%TYPE
            DEFAULT NULL,
      v_cou_zip_code         IN   tqc_countries.cou_zip_code%TYPE DEFAULT NULL,
      v_cou_admin_reg_type   IN   tqc_countries.cou_admin_reg_type%TYPE
            DEFAULT NULL,
      v_cou_schegen          IN   tqc_countries.cou_schegen%TYPE DEFAULT NULL,
      v_cou_emb_code         IN   tqc_countries.cou_emb_code%TYPE DEFAULT NULL,
      v_cou_curr_serial      IN   tqc_countries.cou_curr_serial%TYPE  DEFAULT NULL,
      v_cou_mobile_prefix      IN   tqc_countries.cou_mobile_prefix %TYPE  DEFAULT NULL,
       v_cou_client_number      IN   tqc_countries.cou_client_number %TYPE  DEFAULT NULL
   )
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

            INSERT INTO tqc_countries
                        (cou_code, cou_sht_desc,
                         cou_name, cou_base_curr, cou_nationality,
                         cou_zip_code, cou_admin_reg_type, cou_schegen,
                         cou_emb_code, cou_curr_serial,cou_mobile_prefix,
                         cou_client_number
                        )
                 VALUES (tqc_cou_code_seq.NEXTVAL, v_cou_sht_desc,
                         v_cou_name, v_cou_base_curr, v_cou_nationality,
                         v_cou_zip_code, v_cou_admin_reg_type, v_cou_schegen,
                         v_cou_emb_code, v_cou_curr_serial,v_cou_mobile_prefix,
                         v_cou_client_number
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
                   cou_mobile_prefix=v_cou_mobile_prefix,
                   cou_client_number=v_cou_client_number
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

            INSERT INTO tqc_states
                        (sts_code, sts_cou_code,
                         sts_sht_desc, sts_name
                        )
                 VALUES (tqc_sts_code_seq.NEXTVAL, v_sts_cou_code,
                         v_sts_sht_desc, v_sts_name
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

            INSERT INTO tqc_towns
                        (twn_code, twn_cou_code,
                         twn_sht_desc, twn_name, twn_sts_code
                        )
                 VALUES (tqc_twn_code_seq.NEXTVAL, v_twn_cou_code,
                         v_twn_sht_desc, v_twn_name, v_twn_sts_code
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
         INSERT INTO tqc_msg_templates
                     (msgt_code, msgt_sht_desc,
                      msgt_msg, msgt_sys_code, msgt_sys_module,
                      msgt_type
                     )
              VALUES (tqc_msgt_code_seq.NEXTVAL, v_msgt_sht_desc,
                      v_msgt_msg, v_msgt_sys_code, v_msgt_sys_module,
                      v_msgt_type
                     );
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
                         msgt_type
                        )
                 VALUES (tqc_msgt_code_seq.NEXTVAL,
                         v_messagetemplates_tab (i).msgt_sht_desc,
                         v_messagetemplates_tab (i).msgt_msg,
                         v_messagetemplates_tab (i).msgt_sys_code,
                         v_messagetemplates_tab (i).msgt_sys_module,
                         v_messagetemplates_tab (i).msgt_type
                        );
         ELSIF v_addedit = 'E'
         THEN
            UPDATE tqc_msg_templates
               SET msgt_sht_desc = v_messagetemplates_tab (i).msgt_sht_desc,
                   msgt_msg = v_messagetemplates_tab (i).msgt_msg,
                   msgt_sys_code = v_messagetemplates_tab (i).msgt_sys_code,
                   msgt_sys_module =
                                    v_messagetemplates_tab (i).msgt_sys_module,
                   msgt_type = v_messagetemplates_tab (i).msgt_type
             WHERE msgt_code = v_messagetemplates_tab (i).msgt_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_msg_templates
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
      v_loc_name       IN   tqc_locations.loc_name%TYPE
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

            INSERT INTO tqc_locations
                        (loc_code, loc_twn_code,
                         loc_sht_desc, loc_name
                        )
                 VALUES (tqc_locations_seq.NEXTVAL, v_loc_twn_code,
                         v_loc_sht_desc, v_loc_name
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
            UPDATE tqc_locations
               SET loc_twn_code = v_loc_twn_code,
                   loc_sht_desc = v_loc_sht_desc,
                   loc_name = v_loc_name
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

            INSERT INTO tqc_parameters
                        (param_code, param_name,
                         param_value, param_status, param_desc
                        )
                 VALUES (tqc_param_code_seq.NEXTVAL, v_param_name,
                         v_param_value, v_param_status, v_param_desc
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

   PROCEDURE service_providers_prcbk270514 (
      v_add_edit               IN   VARCHAR2,
      v_spr_code               IN   tqc_service_providers.spr_code%TYPE,
      v_spr_sht_desc           IN   tqc_service_providers.spr_sht_desc%TYPE,
      v_spr_name               IN   tqc_service_providers.spr_name%TYPE,
      v_spr_physical_address   IN   tqc_service_providers.spr_physical_address%TYPE,
      v_spr_postal_address     IN   tqc_service_providers.spr_postal_address%TYPE,
      v_spr_twn_code           IN   tqc_service_providers.spr_twn_code%TYPE,
      v_spr_cou_code           IN   tqc_service_providers.spr_cou_code%TYPE,
      v_spr_spt_code           IN   tqc_service_providers.spr_spt_code%TYPE,
      v_spr_phone              IN   tqc_service_providers.spr_phone%TYPE,
      v_spr_fax                IN   tqc_service_providers.spr_fax%TYPE,
      v_spr_email              IN   tqc_service_providers.spr_email%TYPE,
      v_spr_title              IN   tqc_service_providers.spr_title%TYPE,
      v_spr_zip                IN   tqc_service_providers.spr_zip%TYPE,
      v_spr_wef                IN   tqc_service_providers.spr_wef%TYPE,
      v_spr_wet                IN   tqc_service_providers.spr_wet%TYPE,
      v_spr_contact            IN   tqc_service_providers.spr_contact%TYPE,
      v_spr_aims_code          IN   tqc_service_providers.spr_aims_code%TYPE,
      v_spr_bbr_code           IN   tqc_service_providers.spr_bbr_code%TYPE,
      v_spr_bank_acc_no        IN   tqc_service_providers.spr_bank_acc_no%TYPE,
      v_spr_created_by         IN   tqc_service_providers.spr_created_by%TYPE,
      v_spr_date_created       IN   tqc_service_providers.spr_date_created%TYPE,
      v_spr_status_remarks     IN   tqc_service_providers.spr_status_remarks%TYPE,
      v_spr_status             IN   tqc_service_providers.spr_status%TYPE,
      v_spr_pin_number         IN   tqc_service_providers.spr_pin_number%TYPE,
      v_spr_trs_occupation     IN   tqc_service_providers.spr_trs_occupation%TYPE,
      v_spr_proff_body         IN   tqc_service_providers.spr_proff_body%TYPE,
      v_spr_pin                IN   tqc_service_providers.spr_pin%TYPE,
      v_spr_doc_phone          IN   tqc_service_providers.spr_doc_phone%TYPE,
      v_spr_doc_email          IN   tqc_service_providers.spr_doc_email%TYPE,
      v_spr_gl_acc_no          IN   tqc_service_providers.spr_gl_acc_no%TYPE,
      v_sprinhouse             IN   tqc_service_providers.spr_inhouse%TYPE,
      v_spr_sms_number          IN   tqc_service_providers.spr_sms_number%TYPE,
      v_spr_contact_person      IN  tqc_service_providers.spr_contact_person%TYPE,
      v_spr_cont_person_phone   IN  tqc_service_providers.spr_cont_person_phone%TYPE,
      v_spr_invoice_number   IN  tqc_service_providers.SPR_INVOICE_NUMBER%TYPE,
      v_spr_clnt_code   IN  tqc_service_providers.SPR_CLNT_CODE%TYPE
   )
   IS
      v_count   NUMBER;
      v_code NUMBER;
      v_param_value VARCHAR2(1);
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
                        (spr_code, spr_sht_desc,
                         spr_name, spr_physical_address,
                         spr_postal_address, spr_twn_code,
                         spr_cou_code, spr_spt_code, spr_phone,
                         spr_fax, spr_email, spr_title, spr_zip,
                         spr_wef, spr_wet, spr_contact,
                         spr_aims_code, spr_bbr_code, spr_bank_acc_no,
                         spr_created_by, spr_date_created,
                         spr_status_remarks, spr_status,
                         spr_pin_number, spr_trs_occupation,
                         spr_proff_body, spr_pin, spr_doc_phone,
                         spr_doc_email, spr_gl_acc_no, spr_inhouse,spr_sms_number,
                         spr_contact_person,spr_cont_person_phone,spr_invoice_number, spr_clnt_code 
                        )
                 VALUES (v_code, v_spr_sht_desc,
                         v_spr_name, v_spr_physical_address,
                         v_spr_postal_address, v_spr_twn_code,
                         v_spr_cou_code, v_spr_spt_code, v_spr_phone,
                         v_spr_fax, v_spr_email, v_spr_title, v_spr_zip,
                         v_spr_wef, v_spr_wet, v_spr_contact,
                         v_spr_aims_code, v_spr_bbr_code, v_spr_bank_acc_no,
                         v_spr_created_by, v_spr_date_created,
                         v_spr_status_remarks, v_spr_status,
                         v_spr_pin_number, v_spr_trs_occupation,
                         v_spr_proff_body, v_spr_pin, v_spr_doc_phone,
                         v_spr_doc_email, v_spr_gl_acc_no, v_sprinhouse,v_spr_sms_number,
                         v_spr_contact_person,v_spr_cont_person_phone,v_spr_invoice_number, v_spr_clnt_code 
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
                   spr_sms_number=v_spr_sms_number,
                   spr_contact_person=v_spr_contact_person,
                   spr_cont_person_phone=v_spr_cont_person_phone,
                   spr_invoice_number=v_spr_invoice_number, 
                   spr_clnt_code=v_spr_clnt_code 
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
 PROCEDURE service_providers_prc (
      v_add_edit               IN   VARCHAR2,
      v_spr_code               IN   tqc_service_providers.spr_code%TYPE,
      v_spr_sht_desc           IN   tqc_service_providers.spr_sht_desc%TYPE,
      v_spr_name               IN   tqc_service_providers.spr_name%TYPE,
      v_spr_physical_address   IN   tqc_service_providers.spr_physical_address%TYPE,
      v_spr_postal_address     IN   tqc_service_providers.spr_postal_address%TYPE,
      v_spr_twn_code           IN   tqc_service_providers.spr_twn_code%TYPE,
      v_spr_cou_code           IN   tqc_service_providers.spr_cou_code%TYPE,
      v_spr_spt_code           IN   tqc_service_providers.spr_spt_code%TYPE,
      v_spr_phone              IN   tqc_service_providers.spr_phone%TYPE,
      v_spr_fax                IN   tqc_service_providers.spr_fax%TYPE,
      v_spr_email              IN   tqc_service_providers.spr_email%TYPE,
      v_spr_title              IN   tqc_service_providers.spr_title%TYPE,
      v_spr_zip                IN   tqc_service_providers.spr_zip%TYPE,
      v_spr_wef                IN   tqc_service_providers.spr_wef%TYPE,
      v_spr_wet                IN   tqc_service_providers.spr_wet%TYPE,
      v_spr_contact            IN   tqc_service_providers.spr_contact%TYPE,
      v_spr_aims_code          IN   tqc_service_providers.spr_aims_code%TYPE,
      v_spr_bbr_code           IN   tqc_service_providers.spr_bbr_code%TYPE,
      v_spr_bank_acc_no        IN   tqc_service_providers.spr_bank_acc_no%TYPE,
      v_spr_created_by         IN   tqc_service_providers.spr_created_by%TYPE,
      v_spr_date_created       IN   tqc_service_providers.spr_date_created%TYPE,
      v_spr_status_remarks     IN   tqc_service_providers.spr_status_remarks%TYPE,
      v_spr_status             IN   tqc_service_providers.spr_status%TYPE,
      v_spr_pin_number         IN   tqc_service_providers.spr_pin_number%TYPE,
      v_spr_trs_occupation     IN   tqc_service_providers.spr_trs_occupation%TYPE,
      v_spr_proff_body         IN   tqc_service_providers.spr_proff_body%TYPE,
      v_spr_pin                IN   tqc_service_providers.spr_pin%TYPE,
      v_spr_doc_phone          IN   tqc_service_providers.spr_doc_phone%TYPE,
      v_spr_doc_email          IN   tqc_service_providers.spr_doc_email%TYPE,
      v_spr_gl_acc_no          IN   tqc_service_providers.spr_gl_acc_no%TYPE,
      v_sprinhouse             IN   tqc_service_providers.spr_inhouse%TYPE,
      v_spr_sms_number          IN   tqc_service_providers.spr_sms_number%TYPE,
      v_spr_contact_person      IN  tqc_service_providers.spr_contact_person%TYPE,
      v_spr_cont_person_phone   IN  tqc_service_providers.spr_cont_person_phone%TYPE,
      v_spr_invoice_number   IN  tqc_service_providers.SPR_INVOICE_NUMBER%TYPE,
      v_spr_clnt_code   IN  tqc_service_providers.SPR_CLNT_CODE%TYPE,
      v_spr_bpn_code   IN  tqc_service_providers.SPR_BPN_CODE%TYPE
   )
   IS
      v_count   NUMBER;
      v_code NUMBER;
      v_param_value VARCHAR2(1);
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
                        (spr_code, spr_sht_desc,
                         spr_name, spr_physical_address,
                         spr_postal_address, spr_twn_code,
                         spr_cou_code, spr_spt_code, spr_phone,
                         spr_fax, spr_email, spr_title, spr_zip,
                         spr_wef, spr_wet, spr_contact,
                         spr_aims_code, spr_bbr_code, spr_bank_acc_no,
                         spr_created_by, spr_date_created,
                         spr_status_remarks, spr_status,
                         spr_pin_number, spr_trs_occupation,
                         spr_proff_body, spr_pin, spr_doc_phone,
                         spr_doc_email, spr_gl_acc_no, spr_inhouse,spr_sms_number,
                         spr_contact_person,spr_cont_person_phone,spr_invoice_number, spr_clnt_code,
                         spr_bpn_code
                        )
                 VALUES (v_code, v_spr_sht_desc,
                         v_spr_name, v_spr_physical_address,
                         v_spr_postal_address, v_spr_twn_code,
                         v_spr_cou_code, v_spr_spt_code, v_spr_phone,
                         v_spr_fax, v_spr_email, v_spr_title, v_spr_zip,
                         v_spr_wef, v_spr_wet, v_spr_contact,
                         v_spr_aims_code, v_spr_bbr_code, v_spr_bank_acc_no,
                         v_spr_created_by, v_spr_date_created,
                         v_spr_status_remarks, v_spr_status,
                         v_spr_pin_number, v_spr_trs_occupation,
                         v_spr_proff_body, v_spr_pin, v_spr_doc_phone,
                         v_spr_doc_email, v_spr_gl_acc_no, v_sprinhouse,v_spr_sms_number,
                         v_spr_contact_person,v_spr_cont_person_phone,v_spr_invoice_number, v_spr_clnt_code,
                         v_spr_bpn_code
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
                   spr_sms_number=v_spr_sms_number,
                   spr_contact_person=v_spr_contact_person,
                   spr_cont_person_phone=v_spr_cont_person_phone,
                   spr_invoice_number=v_spr_invoice_number, 
                   spr_clnt_code=v_spr_clnt_code,
                   spr_bpn_code=v_spr_bpn_code
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

            INSERT INTO tqc_serv_prv_activities
                        (spa_code, spa_spt_code,
                         spa_spt_sht_desc, spa_spr_code,
                         spa_spr_sht_desc, spt_main_act, spa_desc,
                         spa_spta_code
                        )
                 VALUES (tqc_spa_code_seq.NEXTVAL, v_spa_spt_code,
                         v_spa_spt_sht_desc, v_spa_spr_code,
                         v_spa_spr_sht_desc, v_spt_main_act, v_spa_desc,
                         v_spa_spta_code
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

  /* Formatted on 20/03/2013 12:39 (Formatter Plus v4.8.8) */
PROCEDURE agencies_prc (
   v_add_edit                IN   VARCHAR2,
   v_agn_code                IN   tqc_agencies.agn_code%TYPE,
   v_agn_act_code            IN   tqc_agencies.agn_act_code%TYPE,
   v_agn_sht_desc            IN   tqc_agencies.agn_sht_desc%TYPE,
   v_agn_name                IN   tqc_agencies.agn_name%TYPE,
   v_agn_physical_address    IN   tqc_agencies.agn_physical_address%TYPE,
   v_agn_postal_address      IN   tqc_agencies.agn_postal_address%TYPE,
   v_agn_twn_code            IN   tqc_agencies.agn_twn_code%TYPE,
   v_agn_cou_code            IN   tqc_agencies.agn_cou_code%TYPE,
   v_agn_email_address       IN   tqc_agencies.agn_email_address%TYPE,
   v_agn_web_address         IN   tqc_agencies.agn_web_address%TYPE,
   v_agn_zip                 IN   tqc_agencies.agn_zip%TYPE,
   v_agn_contact_person      IN   tqc_agencies.agn_contact_person%TYPE,
   v_agn_contact_title       IN   tqc_agencies.agn_contact_title%TYPE,
   v_agn_tel1                IN   tqc_agencies.agn_tel1%TYPE,
   v_agn_tel2                IN   tqc_agencies.agn_tel2%TYPE,
   v_agn_fax                 IN   tqc_agencies.agn_fax%TYPE,
   v_agn_acc_no              IN   tqc_agencies.agn_acc_no%TYPE,
   v_agn_pin                 IN   tqc_agencies.agn_pin%TYPE,
   v_agn_agent_commission    IN   tqc_agencies.agn_agent_commission%TYPE,
   v_agn_credit_allowed      IN   tqc_agencies.agn_credit_allowed%TYPE,
   v_agn_agent_wht_tax       IN   tqc_agencies.agn_agent_wht_tax%TYPE,
   v_agn_print_dbnote        IN   tqc_agencies.agn_print_dbnote%TYPE,
   v_agn_status              IN   tqc_agencies.agn_status%TYPE,
   v_agn_date_created        IN   tqc_agencies.agn_date_created%TYPE,
   v_agn_created_by          IN   tqc_agencies.agn_created_by%TYPE,
   v_agn_reg_code            IN   tqc_agencies.agn_reg_code%TYPE,
   v_agn_comm_reserve_rate   IN   tqc_agencies.agn_comm_reserve_rate%TYPE,
   v_agn_annual_budget       IN   tqc_agencies.agn_annual_budget%TYPE,
   v_agn_status_eff_date     IN   tqc_agencies.agn_status_eff_date%TYPE,
   v_agn_credit_period       IN   tqc_agencies.agn_credit_period%TYPE,
   v_agn_comm_stat_eff_dt    IN   tqc_agencies.agn_comm_stat_eff_dt%TYPE,
   v_agn_comm_status_dt      IN   tqc_agencies.agn_comm_status_dt%TYPE,
   v_agn_comm_allowed        IN   tqc_agencies.agn_comm_allowed%TYPE,
   v_agn_checked             IN   tqc_agencies.agn_checked%TYPE,
   v_agn_checked_by          IN   tqc_agencies.agn_checked_by%TYPE,
   v_agn_check_date          IN   tqc_agencies.agn_check_date%TYPE,
   v_agn_comp_comm_arrears   IN   tqc_agencies.agn_comp_comm_arrears%TYPE,
   v_agn_reinsurer           IN   tqc_agencies.agn_reinsurer%TYPE,
   v_agn_brn_code            IN   tqc_agencies.agn_brn_code%TYPE,
   v_agn_town                IN   tqc_agencies.agn_town%TYPE,
   v_agn_country             IN   tqc_agencies.agn_country%TYPE,
   v_agn_status_desc         IN   tqc_agencies.agn_status_desc%TYPE,
   v_agn_id_no               IN   tqc_agencies.agn_id_no%TYPE,
   v_agn_con_code            IN   tqc_agencies.agn_con_code%TYPE,
   v_agn_agn_code            IN   tqc_agencies.agn_agn_code%TYPE,
   v_agn_sms_tel             IN   tqc_agencies.agn_sms_tel%TYPE,
   v_agn_ahc_code            IN   tqc_agencies.agn_ahc_code%TYPE,
   v_agn_sec_code            IN   tqc_agencies.agn_sec_code%TYPE,
   v_agn_agnc_class_code     IN   tqc_agencies.agn_agnc_class_code%TYPE,
   v_agn_expiry_date         IN   tqc_agencies.agn_expiry_date%TYPE,
   v_agn_license_no          IN   tqc_agencies.agn_license_no%TYPE,
   v_agn_runoff              IN   tqc_agencies.agn_runoff%TYPE,
   v_agn_licensed            IN   tqc_agencies.agn_licensed%TYPE,
   v_agn_license_grace_pr    IN   tqc_agencies.agn_license_grace_pr%TYPE,
   v_agn_old_acc_no          IN   tqc_agencies.agn_old_acc_no%TYPE,
   v_agn_status_remarks      IN   tqc_agencies.agn_status_remarks%TYPE,
   v_agn_bbr_code            IN   tqc_agencies.agn_bbr_code%TYPE DEFAULT NULL,
   v_agn_bank_acc_no         IN   tqc_agencies.agn_bank_acc_no%TYPE
         DEFAULT NULL,
   v_agn_unique_prefix       IN   tqc_agencies.agn_unique_prefix%TYPE
         DEFAULT NULL,
   v_agn_state_code          IN   tqc_agencies.agn_state_code%TYPE
         DEFAULT NULL,
   v_agn_crdt_rting          IN   tqc_agencies.agn_crdt_rting%TYPE
         DEFAULT NULL,
   v_agn_subagent            IN   tqc_agencies.agn_subagent%TYPE DEFAULT NULL,
   v_agn_main_agn_code       IN   tqc_agencies.agn_main_agn_code%TYPE
         DEFAULT NULL,
   v_agn_clnt_code           IN   tqc_agencies.agn_clnt_code%TYPE DEFAULT NULL,
   v_agn_account_manager     IN   tqc_agencies.AGN_ACCOUNT_MANAGER%TYPE DEFAULT NULL,
   v_agn_credit_limit     IN   tqc_agencies.AGN_CREDIT_LIMIT%TYPE DEFAULT NULL,
    v_agn_bru_code     IN   tqc_agencies.agn_bru_code%TYPE DEFAULT NULL,
     v_agn_local_international     IN   tqc_agencies.agn_local_international%TYPE DEFAULT NULL,
     v_agn_regulator_number     IN   tqc_agencies.agn_regulator_number%TYPE DEFAULT NULL,
      v_AGN_RORG_CODE     IN   tqc_agencies.AGN_RORG_CODE%TYPE DEFAULT NULL,
     v_AGN_ORS_CODE     IN   tqc_agencies.AGN_ORS_CODE %TYPE DEFAULT NULL,
     v_agn_allocate_cert     IN   tqc_agencies.agn_allocate_cert %TYPE DEFAULT NULL,
     v_agn_bounced_chq     IN   tqc_agencies.AGN_BOUNCED_CHQ %TYPE DEFAULT NULL,
     v_AGN_BPN_CODE     IN   tqc_agencies.AGN_BPN_CODE %TYPE DEFAULT NULL,
     v_AGN_AGENT_TYPE     IN   tqc_agencies.AGN_AGENT_TYPE %TYPE DEFAULT NULL,
     v_AGN_GROUP     IN   tqc_agencies.AGN_GROUP %TYPE DEFAULT NULL
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
   v_decodeSmsTel         VARCHAR2(50);
   v_rorg_code NUMBER;
   v_ors_code  NUMBER;
BEGIN
  --  RAISE_ERROR('v_agn_allocate_cert'||v_agn_allocate_cert||'v_agn_agn_code'||v_agn_agn_code);
   IF v_agn_sms_tel IS NOT NULL THEN
           BEGIN
           SELECT '+'||COU_ZIP_CODE||v_agn_sms_tel INTO v_decodeSmsTel
           FROM TQC_COUNTRIES
           WHERE COU_CODE = v_agn_cou_code;
           EXCEPTION WHEN OTHERS THEN NULL;
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
                      agn_sht_desc, agn_name, agn_physical_address,
                      agn_postal_address, agn_twn_code, agn_cou_code,
                      agn_email_address, agn_web_address, agn_zip,
                      agn_contact_person, agn_contact_title, agn_tel1,
                      agn_tel2, agn_fax, agn_acc_no, agn_pin,
                      agn_agent_commission, agn_credit_allowed,
                      agn_agent_wht_tax, agn_print_dbnote, agn_status,
                      agn_date_created, agn_created_by, agn_reg_code,
                      agn_comm_reserve_rate, agn_annual_budget,
                      agn_status_eff_date, agn_credit_period,
                      agn_comm_stat_eff_dt, agn_comm_status_dt,
                      agn_comm_allowed, agn_checked, agn_checked_by,
                      agn_check_date, agn_comp_comm_arrears,
                      agn_reinsurer, agn_brn_code, agn_town,
                      agn_country, agn_status_desc, agn_id_no,
                      agn_con_code, agn_agn_code, agn_sms_tel,
                      agn_ahc_code, agn_sec_code, agn_agnc_class_code,
                      agn_expiry_date, agn_license_no, agn_runoff,
                      agn_licensed, agn_license_grace_pr,
                      agn_old_acc_no, agn_status_remarks, agn_bbr_code,
                      agn_bank_acc_no, agn_unique_prefix,
                      agn_state_code, agn_crdt_rting, agn_subagent,
                      agn_main_agn_code, agn_clnt_code,agn_account_manager,agn_credit_limit,
                      AGN_BRU_CODE,agn_local_international,AGN_REGULATOR_NUMBER,
                      AGN_RORG_CODE, AGN_ORS_CODE,agn_allocate_cert,
                      agn_bounced_chq,AGN_BPN_CODE,
                      AGN_AGENT_TYPE, AGN_GROUP
                     )
              VALUES (tqc_agn_code_seq.NEXTVAL, v_agn_act_code,
                      upper(v_agn_short_desc), upper(v_agn_name), upper(v_agn_physical_address),
                      upper(v_agn_postal_address), v_agn_twn_code, v_agn_cou_code,
                      v_agn_email_address, v_agn_web_address, v_agn_zip,
                      v_agn_contact_person, v_agn_contact_title, v_agn_tel1,
                      v_agn_tel2, v_agn_fax, v_agn_acc_no, v_agn_pin,
                      v_agn_agent_commission, v_agn_credit_allowed,
                      v_agn_agent_wht_tax, v_agn_print_dbnote, v_agn_status,
                      v_agn_date_created, v_agn_created_by, v_agn_reg_code,
                      v_agn_comm_reserve_rate, v_agn_annual_budget,
                      v_agn_status_eff_date, v_agn_credit_period,
                      v_agn_comm_stat_eff_dt, v_agn_comm_status_dt,
                      v_agn_comm_allowed, v_agn_checked, v_agn_checked_by,
                      v_agn_check_date, v_agn_comp_comm_arrears,
                      v_agn_reinsurer, v_agn_brn_code, v_agn_town,
                      v_agn_country, v_agn_status_desc, v_agn_id_no,
                      v_agn_con_code, v_agn_agn_code, v_decodeSmsTel,
                      v_agn_ahc_code, v_agn_sec_code, v_agn_agnc_class_code,
                      v_agn_expiry_date, v_agn_license_no, v_agn_runoff,
                      v_agn_licensed, v_agn_license_grace_pr,
                      v_agn_old_acc_no, v_agn_status_remarks, v_agn_bbr_code,
                      v_agn_bank_acc_no, v_agn_unique_prefix,
                      v_agn_state_code, v_agn_crdt_rting, v_agn_subagent,
                      v_agn_main_agn_code, v_agn_clnt_code,v_agn_account_manager,v_agn_credit_limit,
                      v_agn_bru_code,v_agn_local_international,v_agn_regulator_number,
                       v_agn_rorg_code, v_agn_ors_code,v_agn_allocate_cert,
                       v_agn_bounced_chq,v_AGN_BPN_CODE,
                       v_AGN_AGENT_TYPE, v_AGN_GROUP
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
          SELECT AGN_RORG_CODE, AGN_ORS_CODE
          INTO v_rorg_code, v_ors_code
          FROM TQC_AGENCIES
          WHERE agn_code = v_agn_code; 
      END;
      
      IF v_rorg_code!=v_agn_rorg_code OR v_ors_code!=v_agn_ors_code
      THEN
       INSERT INTO TQC_AGENCY_RATINGS_LOGS(ARL_CODE, ARL_RORG_CODE, ARL_ORS_CODE)
       VALUES(TQC_ARL_CODE_SEQ.NEXTVAL,v_agn_rorg_code,v_agn_ors_code);
      END IF;
         
     
         UPDATE tqc_agencies
            SET agn_act_code = v_agn_act_code,
                agn_sht_desc = upper(v_agn_sht_desc),
                agn_name = upper(v_agn_name),
                agn_physical_address = upper(v_agn_physical_address),
                agn_postal_address = upper(v_agn_postal_address),
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
                agn_sms_tel = v_decodeSmsTel,
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
                agn_clnt_code = v_agn_clnt_code,
                agn_account_manager=v_agn_account_manager,
                agn_credit_limit=v_agn_credit_limit,
                agn_bru_code=v_agn_bru_code,
                agn_local_international=v_agn_local_international,
                agn_regulator_number=v_agn_regulator_number,
                agn_rorg_code=v_agn_rorg_code, 
                agn_ors_code=v_agn_ors_code,
                agn_bounced_chq=v_agn_bounced_chq,
                AGN_BPN_CODE=v_AGN_BPN_CODE,
                AGN_AGENT_TYPE=V_AGN_AGENT_TYPE, 
                AGN_GROUP=V_AGN_GROUP
          WHERE agn_code = v_agn_code;
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

            INSERT INTO tqc_payment_modes
                        (pmod_code, pmod_sht_desc,
                         pmod_desc, pmod_naration, pmod_default
                        )
                 VALUES (tqc_pmod_code_seq.NEXTVAL, v_pmod_sht_desc,
                         v_pmod_desc, v_pmod_naration, v_pmod_default
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
         INSERT INTO tqc_service_provider_systems
                     (sps_code, sps_spr_code, sps_sys_code,
                      sps_wef, sps_wet, sps_created_by
                     )
              VALUES (tqc_sps_code_seq.NEXTVAL, v_prov_code, v_sys_code,
                      SYSDATE, NULL, v_created_by
                     );
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
      v_prospects_tab   IN   tqc_prospects_tab,
      v_prs_code         OUT  NUMBER
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

            SELECT tqc_prs_code_seq.NEXTVAL INTO v_prs_code FROM DUAL;
                  
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
                         prs_postal_code
                        )
                 VALUES (v_prs_code,
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
                         v_prospects_tab (i).prs_postal_code
                        );
         ELSIF v_addedit = 'E'
         THEN
           v_prs_code :=v_prospects_tab (i).prs_code;
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
                   prs_postal_code = v_prospects_tab (i).prs_postal_code
             WHERE prs_code = v_prospects_tab (i).prs_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_prospects
                  WHERE prs_code = v_prospects_tab (i).prs_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Prospects!');
   END prospects_prc;
   
   PROCEDURE prospects_prc (
      v_addedit              VARCHAR2,
      v_prospects_tab   IN   tqc_prospects_tab,
      v_client_type     IN   VARCHAR2,
      v_prs_code         OUT  NUMBER
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

            SELECT tqc_prs_code_seq.NEXTVAL INTO v_prs_code FROM DUAL;
                  
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
                         prs_clnt_type
                        )
                 VALUES (v_prs_code,
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
                         v_client_type
                        );
         ELSIF v_addedit = 'E'
         THEN
           v_prs_code :=v_prospects_tab (i).prs_code;
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
                   prs_clnt_type  = v_client_type
             WHERE prs_code = v_prospects_tab (i).prs_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_prospects
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
            DELETE      tqc_holidays
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
            NVL
               (tqc_parameters_pkg.get_param_varchar
                                                   ('USE_BOOKING_DAY_OR_MONTH'),
                'D'
               );
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error (12668,
                                           text_in        => 'Param error.....',
                                           name1_in       => 'MODULE NAME',
                                           value1_in      => 'SEARCHCLIENTS'
                                          );
      END;

      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'SEARCHCLIENTS'
            );
      ELSIF v_search_date IS NULL
      THEN
         tqc_error_manager.raise_error
                       (12669,
                        text_in        => 'Specify the Booking Date to proceed....',
                        name1_in       => 'MODULE NAME',
                        value1_in      => 'SEARCHCLIENTS'
                       );
      END IF;

      BEGIN
         direct_debit_pkg.validate_working_day (v_search_date);
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error (12670,
                                           text_in        => 'Error..',
                                           name1_in       => 'MODULE NAME',
                                           value1_in      => 'SEARCHCLIENTS'
                                          );
      END;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                                   (text_in        =>    'Error Authorising Batch '
                                                      || SQLERRM (SQLCODE),
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'SEARCHCLIENTS'
                                   );
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
   BEGIN
      BEGIN
         v_file_no_param :=
            NVL
               (tqc_parameters_pkg.get_param_varchar
                                              ('GENERATE_DIRECT_DEBIT_FILE_NO'),
                'D'
               );
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                      (12659,
                                       text_in        => 'param error....',
                                       name1_in       => 'MODULE NAME',
                                       value1_in      => 'GENERATEDIRECTDEBITRECS'
                                      );
      END;

      BEGIN
         v_day_month_param :=
            NVL
               (tqc_parameters_pkg.get_param_varchar
                                                   ('USE_BOOKING_DAY_OR_MONTH'),
                'D'
               );
      EXCEPTION
         WHEN OTHERS
         THEN
            tqc_error_manager.raise_error
                                      (12660,
                                       text_in        => 'Param error..1...',
                                       name1_in       => 'MODULE NAME',
                                       value1_in      => 'GENERATEDIRECTDEBITRECS'
                                      );
      END;

      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATEDIRECTDEBITRECS'
            );
      ELSIF NVL (v_searched, 'N') <> 'Y'
      THEN
         tqc_error_manager.raise_error
                       (12661,
                        text_in        => 'First search for records to process....',
                        name1_in       => 'MODULE NAME',
                        value1_in      => 'GENERATEDIRECTDEBITRECS'
                       );
     /* ELSIF v_dd_bbr_code IS NULL
      THEN
         tqc_error_manager.raise_error
                         (12662,
                          text_in        => 'Select the BANK BRANCH to proceed....',
                          name1_in       => 'MODULE NAME',
                          value1_in      => 'GENERATEDIRECTDEBITRECS'
                         );
      ELSIF v_dd_company_acc IS NULL
      THEN
         tqc_error_manager.raise_error
            (12663,
             text_in        => 'Specify the Company Direct Debit A/C to proceed....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATEDIRECTDEBITRECS'
            );*/
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
                     tqc_error_manager.raise_error
                                  (12664,
                                   text_in        => 'Unable to validate entry....',
                                   name1_in       => 'MODULE NAME',
                                   value1_in      => 'GENERATEDIRECTDEBITRECS'
                                  );
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
                     tqc_error_manager.raise_error
                                  (12665,
                                   text_in        => 'Unable to validate entry....',
                                   name1_in       => 'MODULE NAME',
                                   value1_in      => 'GENERATEDIRECTDEBITRECS'
                                  );
               END;
            END IF;
         END IF;

         IF NVL (v_cnt, 0) > 0
         THEN
            tqc_error_manager.raise_error
               (12666,
                text_in        => 'Direct Debits have already been generated for selected debit date/month....',
                name1_in       => 'MODULE NAME',
                value1_in      => 'GENERATEDIRECTDEBITRECS'
               );
         ELSE
            /* LOOP THROUGH THE RECORDS
            BEGIN

            END;
            */
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
                  tqc_error_manager.raise_error
                                      (12667,
                                       text_in        => 'Gen error....',
                                       name1_in       => 'MODULE NAME',
                                       value1_in      => 'GENERATEDIRECTDEBITRECS'
                                      );
            END;
         END IF;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                     (text_in        =>    'Error Generating Direct Debit Records '
                                        || SQLERRM (SQLCODE),
                      name1_in       => 'MODULE NAME',
                      value1_in      => 'GENERATEDIRECTDEBITRECS'
                     );
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
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'DELETEDIRECTDEBIT'
            );
      END IF;

      IF NVL (v_dd_status, 'D') = 'D'
      THEN
         DELETE      tqc_direct_debit_detail
               WHERE ddd_dd_code = v_dd_code;

         DELETE      tqc_direct_debit_header
               WHERE ddh_dd_code = v_dd_code;

         DELETE      tqc_direct_debit
               WHERE dd_code = v_dd_code;
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
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'AUTHORISEDIRECTDEBIT'
            );
      ELSIF NVL (v_dd_status, 'D') = 'A'
      THEN
         tqc_error_manager.raise_error
            (12652,
             text_in        => 'The selected batch has already been authorized....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'AUTHORISEDIRECTDEBIT'
            );
      END IF;

      SELECT COUNT (1)
        INTO v_cnt
        FROM tqc_direct_debit_detail, tqc_direct_debit_header
       WHERE ddh_code = ddd_ddh_code AND ddh_dd_code = v_dd_code;

      IF NVL (v_cnt, 0) = 0
      THEN
         tqc_error_manager.raise_error
              (12652,
               text_in        => 'This batch has no generated records attached....',
               name1_in       => 'MODULE NAME',
               value1_in      => 'AUTHORISEDIRECTDEBIT'
              );
      ELSE
         BEGIN
            direct_debit_pkg.update_bo_date (v_dd_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               tqc_error_manager.raise_error
                              (12653,
                               text_in        => 'Unable to update policy dtls....',
                               name1_in       => 'MODULE NAME',
                               value1_in      => 'AUTHORISEDIRECTDEBIT'
                              );
         END;

         BEGIN
            UPDATE tqc_direct_debit_detail
               SET ddd_status = 'A'
             WHERE ddd_dd_code = v_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               tqc_error_manager.raise_error
                  (12654,
                   text_in        => 'Unable to update Direct Debit detail records....',
                   name1_in       => 'MODULE NAME',
                   value1_in      => 'AUTHORISEDIRECTDEBIT'
                  );
         END;

         BEGIN
            UPDATE tqc_direct_debit_header
               SET ddh_status = 'A'
             WHERE ddh_dd_code = v_dd_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               tqc_error_manager.raise_error
                  (12655,
                   text_in        => 'Unable to update Direct Debit header records....',
                   name1_in       => 'MODULE NAME',
                   value1_in      => 'AUTHORISEDIRECTDEBIT'
                  );
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
               tqc_error_manager.raise_error
                  (12656,
                   text_in        => 'Unable to update Direct Debit record dtls....',
                   name1_in       => 'MODULE NAME',
                   value1_in      => 'AUTHORISEDIRECTDEBIT'
                  );
         END;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                                   (text_in        =>    'Error Authorising Batch '
                                                      || SQLERRM (SQLCODE),
                                    name1_in       => 'MODULE NAME',
                                    value1_in      => 'AUTHORISEDIRECTDEBIT'
                                   );
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

   PROCEDURE generatereceipts (
      v_username       VARCHAR2,
      v_dd_code        NUMBER,
      v_dd_receipted   VARCHAR2,
      v_dd_book_date   DATE
   )
   IS
   BEGIN
      IF v_username IS NULL
      THEN
         tqc_error_manager.raise_error
            (12648,
             text_in        => 'System UNABLE to verify connected user....RECONNECT SESSION....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATERECEIPTS'
            );
      ELSIF v_dd_receipted = 'Y'
      THEN
         tqc_error_manager.raise_error
            (12649,
             text_in        => 'Receipts for the selected batch have already been generated....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATERECEIPTS'
            );
      ELSIF v_dd_receipted = 'F'
      THEN
         tqc_error_manager.raise_error
            (12650,
             text_in        => 'Receipts for the selected batch have ALL been cancelled....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATERECEIPTS'
            );
      ELSIF direct_debit_pkg.get_business_date (v_dd_book_date, 0) > SYSDATE
      THEN
         tqc_error_manager.raise_error
            (12651,
             text_in        =>    'This batch can ONLY be receipted after '||v_dd_book_date||'  '
                               || TO_CHAR
                                     (direct_debit_pkg.get_business_date
                                                              (v_dd_book_date,
                                                               0
                                                              ),
                                      'DD/MM/RRRR'
                                     )
                               || '....',
             name1_in       => 'MODULE NAME',
             value1_in      => 'GENERATERECEIPTS'
            );
      ELSE
         direct_debit_pkg.generate_receipts (v_dd_code, v_username);
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         tqc_error_manager.raise_unanticipated
                                  (text_in        =>    'Error Generating Receipt '
                                                     || SQLERRM (SQLCODE),
                                   name1_in       => 'MODULE NAME',
                                   value1_in      => 'GENERATERECEIPTS'
                                  );
   END generatereceipts;

   PROCEDURE failUpdateDDHeader(v_username VARCHAR2, v_ddh_code NUMBER, v_fail_date DATE, v_fail_remarks VARCHAR2, v_ddh_dd_code NUMBER) IS 
    v_cnt1              NUMBER;  
    v_cnt2              NUMBER; 
    v_count1         NUMBER; 
    v_count2         NUMBER; 
    BEGIN
    
        IF v_username IS NULL THEN
            TQC_ERROR_MANAGER.RAISE_ERROR(12648,
                                         text_in => 'System UNABLE to verify connected user....RECONNECT SESSION....'
                                         , name1_in       => 'MODULE NAME'
                                          , value1_in      => 'FAILUPDATEDDHEADER');
                                          
        ELSIF v_fail_date IS NULL THEN
            TQC_ERROR_MANAGER.RAISE_ERROR(12671,
                                         text_in => 'Provide the Direct Debit failure date to proceed....'
                                         , name1_in       => 'MODULE NAME'
                                          , value1_in      => 'FAILUPDATEDDHEADER');
        ELSIF v_fail_remarks IS NULL THEN
            TQC_ERROR_MANAGER.RAISE_ERROR(12672,
                                         text_in => 'Provide the reason for failure to proceed....'
                                         , name1_in       => 'MODULE NAME'
                                          , value1_in      => 'FAILUPDATEDDHEADER');
        END IF;
        
        
        BEGIN
            UPDATE TQC_DIRECT_DEBIT_DETAIL
            SET DDD_STATUS = 'F', 
                    DDD_FAIL_DATE = v_fail_date, 
                    DDD_FAIL_UPDATED_BY = v_username, 
                    DDD_FAIL_UPDATE_DATE = SYSDATE,
                    DDD_FAIL_REMARKS = v_fail_remarks
            WHERE DDD_DDH_CODE = v_ddh_code;
        EXCEPTION
            WHEN OTHERS THEN
                TQC_ERROR_MANAGER.RAISE_ERROR(12673,
                                         text_in => 'Update Detail error...'
                                         , name1_in       => 'MODULE NAME'
                                          , value1_in      => 'FAILUPDATEDDHEADER');
        END;
        
            BEGIN
             SELECT COUNT(1) INTO v_count1
             FROM TQC_DIRECT_DEBIT_DETAIL
             WHERE DDD_DDH_CODE = v_ddh_code;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE_ERROR('Child records not found2....'|| SQLERRM);
        END;
        BEGIN
            SELECT COUNT(1) INTO v_count2
            FROM TQC_DIRECT_DEBIT_DETAIL
            WHERE DDD_DDH_CODE = v_ddh_code
            AND DDD_STATUS = 'F';
        EXCEPTION
            WHEN OTHERS THEN
            RAISE_ERROR('Child records not found3....'|| SQLERRM);
        END;
        IF NVL(v_count1,0) = NVL(v_count2,0) AND NVL(v_count1,0) > 0 THEN
               
            BEGIN
                UPDATE TQC_DIRECT_DEBIT_HEADER
                SET DDH_STATUS = 'F', 
                        DDH_FAIL_DATE = NVL(v_fail_date,DDH_FAIL_DATE),
                        DDH_FAIL_UPDATED_BY = v_username, 
                        DDH_FAIL_UPDATE_DATE = SYSDATE,
                        DDH_FAIL_REMARKS = v_fail_remarks
                WHERE DDH_CODE = v_ddh_code;
            EXCEPTION
                WHEN OTHERS THEN
                RAISE_ERROR('Update Header error....v_status='|| SQLERRM);
            END;
        END IF; 
        BEGIN
             SELECT COUNT(1) INTO v_cnt1
             FROM TQC_DIRECT_DEBIT_HEADER
             WHERE DDH_DD_CODE = v_ddh_dd_code;
        EXCEPTION
            WHEN OTHERS THEN
                TQC_ERROR_MANAGER.RAISE_ERROR(12675,
                                         text_in => 'Child records not found....'
                                         , name1_in       => 'MODULE NAME'
                                          , value1_in      => 'FAILUPDATEDDHEADER');
        END;
        
        
        BEGIN
            SELECT COUNT(1) INTO v_cnt2
            FROM TQC_DIRECT_DEBIT_HEADER
            WHERE DDH_DD_CODE = v_ddh_dd_code
            AND DDH_STATUS = 'F';
        EXCEPTION
            WHEN OTHERS THEN
                TQC_ERROR_MANAGER.RAISE_ERROR(12675,
                                         text_in => 'Child records not found....'
                                         , name1_in       => 'MODULE NAME'
                                          , value1_in      => 'FAILUPDATEDDHEADER');
        END;
        
        IF NVL(v_cnt1,0) = NVL(v_cnt2,0) AND NVL(v_cnt1,0) > 0 THEN        
            BEGIN
                UPDATE TQC_DIRECT_DEBIT
                SET DD_RECEIPTED = 'F'
                WHERE DD_CODE = v_ddh_dd_code;
            EXCEPTION
                WHEN OTHERS THEN
                    TQC_ERROR_MANAGER.RAISE_ERROR(12676,
                                         text_in => 'Update DD error...'
                                         , name1_in       => 'MODULE NAME'
                                          , value1_in      => 'FAILUPDATEDDHEADER');
            END;
        END IF;
        
    EXCEPTION
        WHEN OTHERS THEN
            TQC_ERROR_MANAGER.RAISE_UNANTICIPATED
                                           ( text_in => 'Error Updating DDH '||SQLERRM(SQLCODE)
                                          , name1_in       => 'MODULE NAME'
                                          , value1_in      => 'FAILUPDATEDDHEADER'
                                            );    
    END failUpdateDDHeader;

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

   PROCEDURE failUpdateHeader(v_dddCode NUMBER,v_username VARCHAR2, v_policyNo VARCHAR2,v_account VARCHAR2, v_book_date DATE, v_fail_remarks VARCHAR2, v_ddh_dd_code IN OUT NUMBER,v_status VARCHAR DEFAULT 'F') IS 
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
            UPDATE TQC_DIRECT_DEBIT_DETAIL
            SET DDD_STATUS = NVL(v_status,'A'),
                    DDD_FAIL_DATE = NVL(v_fail_date,DDD_FAIL_DATE) ,
                    DDD_FAIL_UPDATED_BY = v_username, 
                    DDD_FAIL_UPDATE_DATE = SYSDATE,
                    DDD_FAIL_REMARKS = v_fail_remarks
            WHERE DDD_CODE = v_dddCode;
        EXCEPTION
            WHEN OTHERS THEN
            RAISE_ERROR('Update Detail error....');
         
        END;
        BEGIN
             SELECT COUNT(1) INTO v_count1
             FROM TQC_DIRECT_DEBIT_DETAIL
             WHERE DDD_DDH_CODE = v_ddh_code;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE_ERROR('Child records not found2....'|| SQLERRM);
        END;
        
        
        BEGIN
            SELECT COUNT(1) INTO v_count2
            FROM TQC_DIRECT_DEBIT_DETAIL
            WHERE DDD_DDH_CODE = v_ddh_code
            AND DDD_STATUS = NVL(v_status,'F');
        EXCEPTION
            WHEN OTHERS THEN
            RAISE_ERROR('Child records not found3....'|| SQLERRM);
        END;
        IF NVL(v_count1,0) = NVL(v_count2,0) AND NVL(v_count1,0) > 0 THEN  
            BEGIN
                UPDATE TQC_DIRECT_DEBIT_HEADER
                SET DDH_STATUS = NVL(v_status,'A'), 
                        DDH_FAIL_DATE = NVL(v_fail_date,DDH_FAIL_DATE),
                        DDH_FAIL_UPDATED_BY = v_username, 
                        DDH_FAIL_UPDATE_DATE = SYSDATE,
                        DDH_FAIL_REMARKS = v_fail_remarks
                WHERE DDH_CODE = v_ddh_code;
            EXCEPTION
                WHEN OTHERS THEN
                RAISE_ERROR('Update Header error....v_status='||v_status||' '|| SQLERRM);
            END;
        END IF;
        BEGIN
             SELECT COUNT(1) INTO v_cnt1
             FROM TQC_DIRECT_DEBIT_HEADER
             WHERE DDH_DD_CODE = v_ddh_dd_code;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE_ERROR('Child records not found2....'|| SQLERRM);
        END;
        
        
        BEGIN
            SELECT COUNT(1) INTO v_cnt2
            FROM TQC_DIRECT_DEBIT_HEADER
            WHERE DDH_DD_CODE = v_ddh_dd_code
            AND DDH_STATUS = NVL(v_status,'F');
        EXCEPTION
            WHEN OTHERS THEN
            RAISE_ERROR('Child records not found3....'|| SQLERRM);
        END;
        --raise_error('v_cnt1='||v_cnt1||'v_cnt2='||v_cnt2);
        IF NVL(v_cnt1,0) = NVL(v_cnt2,0) AND NVL(v_cnt1,0) > 0 THEN        
            BEGIN
                UPDATE TQC_DIRECT_DEBIT
                SET DD_RECEIPTED = NVL(v_status,'F')
                WHERE DD_CODE = v_ddh_dd_code;
            EXCEPTION
                WHEN OTHERS THEN
                RAISE_ERROR('Update DD error...'|| SQLERRM);
            END;
        END IF;
        
--    EXCEPTION
--        WHEN OTHERS THEN
--            RAISE_ERROR('Error Updating DDH v_status='||v_status||' '|| SQLERRM);
                           
    END failUpdateHeader;

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
                         dlt_act_code,DLT_CODE_VAL
                        )
                 VALUES (v_orgdivlevelstype_tab (i).dlt_code,
                         v_orgdivlevelstype_tab (i).dlt_sys_code,
                         v_orgdivlevelstype_tab (i).dlt_desc,
                         v_orgdivlevelstype_tab (i).dlt_act_code,
                         TQC_DLT_CODE_VAL_SEQ.NEXTVAL
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
            DELETE      tqc_org_division_levels_type
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
            DELETE      tqc_org_division_levels
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
                         osd_wef,
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
                         v_orgsubdivisions_tab (i).osd_wef,
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
               SET osd_parent_osd_code =
                                 v_orgsubdivisions_tab (i).osd_parent_osd_code,
                   osd_dlt_code = v_orgsubdivisions_tab (i).osd_dlt_code,
                   osd_odl_code = v_orgsubdivisions_tab (i).osd_odl_code,
                   osd_name = v_orgsubdivisions_tab (i).osd_name,
                   osd_wef = v_orgsubdivisions_tab (i).osd_wef,
                   osd_div_head_agn_code =
                               v_orgsubdivisions_tab (i).osd_div_head_agn_code,
                   osd_sys_code = v_orgsubdivisions_tab (i).osd_sys_code,
                   osd_brn_code = v_orgsubdivisions_tab (i).osd_brn_code,
                   osd_reg_code = v_orgsubdivisions_tab (i).osd_reg_code,
                   osd_post_level = v_orgsubdivisions_tab (i).osd_post_level,
                   osd_manager_allwd =
                                   v_orgsubdivisions_tab (i).osd_manager_allwd,
                   osd_over_comm_earn =
                                  v_orgsubdivisions_tab (i).osd_over_comm_earn,
                   osd_id = v_orgsubdivisions_tab (i).osd_id,
                   osd_parent_id = v_orgsubdivisions_tab (i).osd_parent_id
             WHERE osd_code = v_orgsubdivisions_tab (i).osd_code;
         ELSIF v_addedit = 'D'
         THEN
            DELETE      tqc_org_subdivisions
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

         INSERT INTO tqc_agency_activities
                     (aac_code, aac_sys_code,
                      aac_acty_code, aac_wef, aac_estimate_wet,
                      aac_actual_wet, aac_remarks, aac_agn_code,
                      aac_by_type, aac_reasons, aac_by_code
                     )
              VALUES (tqc_aac_code_seq.NEXTVAL, v_aac_sys_code,
                      v_aac_acty_code, v_aac_wef, v_aac_estimate_wet,
                      v_aac_actual_wet, v_aac_remarks, v_aac_agn_code,
                      v_aac_act_type, v_aac_reasons, v_aac_by_code
                     );
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
         WHERE  aac_code = v_aac_code;
      ELSIF v_addedit = 'D'
      THEN
         DELETE      tqc_agency_activities
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
            DELETE      tqc_postal_codes
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
            DELETE      tqc_sys_posts
                  WHERE spost_code = v_sysposts_tab (i).spost_code;
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Sys Post data!');
   END sysposts_prc;

   PROCEDURE sysprcsssubarealmts_prc (
      v_addedit                        VARCHAR2,
      v_sysprcsssubarealmts_tab   IN   tqc_sys_prcss_subarea_lmts_tab
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_sysprcsssubarealmts_tab.COUNT = 0
      THEN
         raise_error
            (   'Error occured. No Sys Process SubArea Limits Data Provided : '
             || SQLERRM (SQLCODE)
            );
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
            DELETE      tqc_sys_prcss_subarea_lmts
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
            DELETE      tqc_sys_prcss_subarea_lvls
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
            INSERT INTO tqc_agency_systems
                        (asys_sys_code,
                         asys_agn_code,
                         asys_wef,
                         asys_wet,
                         asys_comment,
                         asys_osd_code,
                         asys_osd_id, asys_agn_sht_desc
                        )
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
            DELETE      tqc_agency_systems
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
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_clm_payment_modes
                        (cpm_code, cpm_sht_desc, cpm_desc,
                         cpm_remarks, cpm_min_amt, cpm_max_amt, cpm_default
                        )
                 VALUES (tq_crm.cpm_code_seq.NEXTVAL, v_sht_desc, v_desc,
                         v_remarks, v_min_amt, v_max_amt, v_default
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

            INSERT INTO tqc_mobile_pymnt_types
                        (mpt_code, mpt_sht_desc, mpt_desc,
                         mpt_min_amt_allowed, mpt_max_amt_allowed,
                         mpt_cou_code
                        )
                 VALUES (tq_crm.mpt_code_seq.NEXTVAL, v_sht_desc, v_desc,
                         v_min_amt_allowed, v_max_amt_allowed,
                         v_cou_code
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

            INSERT INTO tqc_mob_pymnt_types_prefixes
                        (mptp_code, mptp_mob_no_prefix,
                         mptp_mpt_code
                        )
                 VALUES (tq_crm.mptp_code_seq.NEXTVAL, v_mob_no_prefix,
                         v_mpt_code
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
               INSERT INTO tqc_group_client
                           (grp_code, grp_name, grp_minimum,
                            grp_maximum
                           )
                    VALUES (tq_crm.grp_code_seq.NEXTVAL, v_name, v_minimum,
                            v_maximum
                           );
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
      v_lds_code       NUMBER,
      v_desc           VARCHAR2,
      v_cmp_code       NUMBER,
      v_cotel          VARCHAR2,
      v_title          VARCHAR2,
      v_surname        VARCHAR2,
      v_oth_name       VARCHAR2,
      v_mob_no         VARCHAR2,
      v_fax            VARCHAR2,
      v_email          VARCHAR2,
      v_post_addr      VARCHAR2,
      v_post_code      VARCHAR2,
      v_phy_addr       VARCHAR2,
      v_ldsrccode      NUMBER,
      v_lstscode       NUMBER,
      v_lead_date      DATE,
      v_converted      VARCHAR2,
      v_port_name      VARCHAR2,
      v_port_contr     VARCHAR2,
      v_port_amt       NUMBER,
      v_port_sale      VARCHAR2,
      v_port_clse      DATE,
      v_ann_rev        NUMBER,
      v_website        VARCHAR2,
      v_industry       VARCHAR2,
      v_cou_code       NUMBER,
      v_state_code     NUMBER,
      v_town_code      NUMBER,
      v_org_code       NUMBER,
      v_user_code      NUMBER,
      v_team_code      NUMBER,
      v_acc_code       NUMBER,
      v_prod_code      NUMBER,
      v_cur_code       NUMBER,
      v_lds_sys_code   NUMBER,
      v_lds_div_code   NUMBER,
      v_lds_occupation IN VARCHAR2,
      v_lds_comp_name IN VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      IF v_lds_code IS NULL
      THEN
         BEGIN
            INSERT INTO tqc_leads
                        (lds_code, lds_title, lds_surnname,
                         lds_othernames, lds_camp_tel, lds_mob_tel,
                         lds_camp_code, lds_fax, lds_email_addrs,
                         lds_ldsrc_code, lds_lsts_code, lds_physical_addrs,
                         lds_postal_addrs, lds_postal_code, lds_date,
                         lds_desc, lds_converted, lds_industry,
                         lds_ann_revenue, lds_web_site, lds_cou_code,
                         lds_state_code, lds_twn_code, lds_org_code,
                         lds_usr_code, lds_team_usr_code, lds_acc_code,
                         lds_prod_code, lds_cur_code, lds_sys_code,
                         lds_div_code,lds_occupation,lds_comp_name
                        )
                 VALUES (tqc_lds_code_seq.NEXTVAL, v_title, v_surname,
                         v_oth_name, v_cotel, v_mob_no,
                         v_cmp_code, v_fax, v_email,
                         v_ldsrccode, v_lstscode, v_phy_addr,
                         v_post_addr, v_post_code, v_lead_date,
                         v_desc, v_converted, v_industry,
                         v_ann_rev, v_website, v_cou_code,
                         v_state_code, v_town_code, v_org_code,
                         v_user_code, v_team_code, v_acc_code,
                         v_prod_code, v_cur_code, v_lds_sys_code,
                         v_lds_div_code,v_lds_occupation,v_lds_comp_name
                        );
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
                   lds_occupation=v_lds_occupation,
                   lds_comp_name=v_lds_comp_name
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
      DELETE      tqc_leads
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
            INSERT INTO tqc_lead_comments
                        (lcmnt_code, lcmnt_lds_code, lcmnt_usr_code,
                         lcmnt_comment, lcmnt_date
                        )
                 VALUES (tqc_lcmnt_code_seq.NEXTVAL, v_lds_code, v_usr_code,
                         v_comment, v_date
                        );
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
            INSERT INTO tqc_leads_activities
                        (lacts_code, lacts_lds_code, lacts_act_code
                        )
                 VALUES (tqc_lacts_code_seq.NEXTVAL, v_lds_code, v_act_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting Lead Activity.');
         END;
      ELSIF v_action = 'D'
      THEN
         BEGIN
            DELETE      tqc_leads_activities
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
            INSERT INTO tqc_leads_sources
                        (ldsrc_code, ldsrc_desc
                        )
                 VALUES (tqc_ldsrc_code_seq.NEXTVAL, v_desc
                        );
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
            INSERT INTO tqc_leads_statuses
                        (lsts_code, lsts_desc
                        )
                 VALUES (tqc_lsts_code_seq.NEXTVAL, v_desc
                        );
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
            INSERT INTO tqc_report_groups
                        (trg_code, trg_name
                        )
                 VALUES (tqc_trg_code_seq.NEXTVAL, v_trg_name
                        );
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
      v_action         IN   VARCHAR2 DEFAULT NULL,
      v_rdg_code            tqc_report_div_groups.rdg_code%TYPE DEFAULT NULL,
      v_rdg_trg_code        tqc_report_div_groups.rdg_code%TYPE DEFAULT NULL,
      v_rdg_div_code        tqc_report_div_groups.rdg_div_code%TYPE
            DEFAULT NULL
   )
   IS
   BEGIN
      IF v_action = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_report_div_groups
                        (rdg_code, rdg_trg_code,
                         rdg_div_code
                        )
                 VALUES (tq_rdg_code_seq.NEXTVAL, v_rdg_trg_code,
                         v_rdg_div_code
                        );
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
      v_add_edit          IN   VARCHAR2,
      v_saa_code          IN   tqc_sys_applicable_areas.saa_code%TYPE
            DEFAULT NULL,
      v_saa_sys_code      IN   tqc_sys_applicable_areas.saa_sys_code%TYPE
            DEFAULT NULL,
      v_saa_description   IN   tqc_sys_applicable_areas.saa_description%TYPE
            DEFAULT NULL
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_sys_applicable_areas
                        (saa_code, saa_sys_code,
                         saa_description
                        )
                 VALUES (tqc_sapa_seq.NEXTVAL, v_saa_sys_code,
                         v_saa_description
                        );
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
         INSERT INTO tqc_sys_mod_subunits
                     (tsms_code, tsms_tsm_code,
                      tsms_sht_desc, tsms_desc, tsms_order,
                      tsms_prod_code
                     )
              VALUES (tqc_tsms_code_seq.NEXTVAL, v_tsms_tsm_code,
                      v_tsms_sht_desc, v_tsms_desc, v_tsms_order,
                      v_tsms_prod_code
                     );
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
         INSERT INTO tqc_sys_mod_subunits_details
                     (tsmsd_code, tsmsd_tsms_code,
                      tsmsd_name, tsmsd_prompt, tsmsd_type,
                      tsmsd_order, tsmsd_parent, tsmsd_more_dtls_appl,
                      tsmsd_more_dtls, tsmsd_root,
                      tsmsd_more_dtls_required, tsmsd_tmsc_code
                     )
              VALUES (tqc_tsmsd_code_seq.NEXTVAL, v_tsmsd_tsms_code,
                      v_tsmsd_name, v_tsmsd_prompt, v_tsmsd_type,
                      v_tsmsd_order, v_tsmsd_parent, v_tsmsd_more_dtls_appl,
                      v_tsmsd_more_dtls, v_tsmsd_root,
                      v_tsmsd_more_dtls_required, v_tmsc_code
                     );
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
         INSERT INTO tqc_sys_subunits_options
                     (tsso_code, tsso_tsmsd_code,
                      tsso_option_name, tsso_option_desc, tsso_order,
                      tsso_type
                     )
              VALUES (tqc_tsso_code_seq.NEXTVAL, v_tsso_tsmsd_code,
                      v_tsso_option_name, v_tsso_option_desc, v_tsso_order,
                      v_tsso_type
                     );
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

   PROCEDURE outgoingmailproc (
      v_mail_type  VARCHAR2, 
      v_mail_secure VARCHAR2,
      v_server     VARCHAR2,
      v_host       VARCHAR2,
      v_port       NUMBER,
      v_username   VARCHAR2,
      v_password   VARCHAR2,
      v_email      VARCHAR2
   )
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
         INSERT INTO tqc_system_mails
                     (mail_type, mail_server_name, mail_host, mail_username,
                      mail_password, mail_port, mail_email, mail_in_out, MAIL_SECURE
                     )
              VALUES (v_mail_type, v_server, v_host, v_username,
                      v_password, v_port, v_email,'O', v_mail_secure
                     );
      ELSE
         UPDATE tqc_system_mails
            SET mail_server_name = v_server,
                mail_host = v_host,
                mail_username = v_username,
                mail_password = v_password,
                mail_port = v_port,
                mail_email = v_email,
                mail_type=v_mail_type, 
                mail_secure=v_mail_secure
          WHERE mail_in_out = 'O';
      END IF;
   END outgoingmailproc;

   PROCEDURE incomingmailproc (
      v_mail_secure VARCHAR2,
      v_server     VARCHAR2,
      v_host       VARCHAR2,
      v_port       NUMBER,
      v_username   VARCHAR2,
      v_password   VARCHAR2,
      v_type       VARCHAR2
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
         INSERT INTO tqc_system_mails
                     (mail_type, mail_server_name, mail_host, mail_username,
                      mail_password, mail_port, mail_email, mail_in_out,mail_secure
                     )
              VALUES (v_type, v_server, v_host, v_username,
                      v_password, v_port, NULL, 'I',v_mail_secure
                     );
      ELSE
         UPDATE tqc_system_mails
            SET mail_server_name = v_server,
                mail_host = v_host,
                mail_username = v_username,
                mail_password = v_password,
                mail_port = v_port,
                mail_type = v_type,
                mail_secure=v_mail_secure
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
            INSERT INTO qrtz_triggers
                        (qt_job_name, qt_description, qt_next_fire_time,
                         qt_prev_fire_time, qt_start_time, qt_end_time,
                         qt_code, qt_sys_code, qt_recurrence,
                         qt_recurrence_type, qt_job_assignee,
                         qt_notified_fail_user, qt_notified_succ_user,
                         qt_reccurence_interval, qt_job_type,
                         qt_job_template, qt_fail_notify_template,
                         qt_succ_notify_template, qt_cron_expression,
                         qt_status, qt_threshold_type, qt_threshold_value
                        )
                 VALUES (v_job_name, v_job_desc, NULL,
                         NULL, v_start_time, v_end_time,
                         v_pcode, v_sys_code, v_repeat,
                         v_recurrence_type, v_assignee,
                         v_notified_fail, v_notified_succ,
                         NULL, v_job_type,
                         v_job_template, v_fail_template,
                         v_succ_template, v_cron_expr,
                         v_status, v_threshtype, v_threshval
                        );
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
               INSERT INTO tqc_households
                           (hh_id, hh_name,
                            hh_created_by, hh_date_created, hh_category
                           )
                    VALUES (tq_crm.tqc_hh_code_seq.NEXTVAL, v_name,
                            v_usrcode, SYSDATE, v_category
                           );
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
         INSERT INTO tqc_sys_process_reports
                     (sprr_code, sprr_rpt_code,
                      sprr_sprc_code, sprr_desc, sprr_type,SPRR_SDT_CODE
                     )
              VALUES (tqc_sprr_code_seq.NEXTVAL, v_sprr_rpt_code,
                      v_sprr_sprc_code, v_sprr_desc, v_sprr_type,v_doc_type_code
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_sys_process_reports
            SET sprr_rpt_code = v_sprr_rpt_code,
                sprr_sprc_code = v_sprr_sprc_code,
                sprr_desc = v_sprr_desc,
                sprr_type = v_sprr_type,
                SPRR_SDT_CODE=NVL(v_doc_type_code,SPRR_SDT_CODE)
          WHERE sprr_code = v_sprr_code;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE FROM tqc_sys_process_reports
               WHERE sprr_code = v_sprr_code;
      END IF;
   END;

   PROCEDURE ecm_doc_types (
      v_sdt_code           NUMBER,
      v_content_type      VARCHAR2,
      v_sdt_content_name   VARCHAR2,
      v_sdt_content_desc   VARCHAR2,
      v_add_edit           VARCHAR2
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO tqc_sys_doc_types
                     (sdt_code, SDT_CONTENT_TYPE,
                      sdt_content_name, sdt_content_desc
                     )
              VALUES (tqc_sprr_code_seq.NEXTVAL, v_content_type,
                      v_sdt_content_name, v_sdt_content_desc
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE tqc_sys_doc_types
            SET SDT_CONTENT_TYPE = v_content_type,
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
         INSERT INTO tqc_dms_doc_metadata
                     (ddm_code, ddm_sdt_code, ddm_name,
                      ddm_desc
                     )
              VALUES (tqc_sprr_code_seq.NEXTVAL, v_ddm_sdt_code, v_ddm_name,
                      v_ddm_desc
                     );
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
    PROCEDURE bank_branch_details (v_add_edit VARCHAR2,
        v_bbb_code number,
       v_bbb_brn_code NUMBER, 
       v_bbb_brn_reg_code VARCHAR2,
       v_bbb_brn_name VARCHAR2, 
       v_bbb_bbr_code NUMBER,
        v_bbb_bbr_bnk_code NUMBER
   )
   IS
   BEGIN
      IF v_add_edit = 'A'
      THEN
         INSERT INTO TQC_BANK_BRANCHES_BRANCHES
                     (BBB_CODE, BBB_BRN_CODE, 
                     BBB_BRN_REG_CODE, BBB_BRN_NAME,
                      
                      BBB_BBR_CODE, BBB_BBR_BNK_CODE 
                     
                     )
              VALUES ( GIN_BBB_CODE_SEQ.NEXTVAL , v_bbb_brn_code ,
       v_bbb_brn_reg_code ,v_bbb_brn_name , 
       v_bbb_bbr_code ,v_bbb_bbr_bnk_code  
                     );
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE TQC_BANK_BRANCHES_BRANCHES
            SET
            -- BBB_CODE=v_bbb_code, 
            BBB_BRN_CODE=v_bbb_brn_code, 
             BBB_BRN_REG_CODE=v_bbb_brn_reg_code, 
             BBB_BRN_NAME=v_bbb_brn_name,
            BBB_BBR_CODE=v_bbb_bbr_code,
            BBB_BBR_BNK_CODE=v_bbb_bbr_bnk_code 
           WHERE BBB_CODE=v_bbb_code;
      END IF;
   END;
    PROCEDURE smsStatus (v_status varchar2,v_sms_code number DEFAULT NULL,v_sms_sent_response IN VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      --  RAISE_ERROR('v_status'||v_status);
         UPDATE TQC_SMS_MESSAGES
            SET  
            SMS_STATUS=v_status,
            SMS_SEND_DATE = SYSDATE,
            SMS_SENT_RESPONSE=v_sms_sent_response
           WHERE SMS_CODE=v_sms_code;
    END;
    
    PROCEDURE escalate_proc(v_taskId                NUMBER,
                        v_sysCode               NUMBER,                        
                        v_processDefinitionId   VARCHAR2,
                        v_taskName              VARCHAR2,
                        v_duration          OUT NUMBER,
                        v_assignee          OUT VARCHAR2
                        ) IS 
    v_count     NUMBER;
    v_level     NUMBER;
    v_jpdlName  VARCHAR2(50);
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM TQC_TASK_ESCALATIONS
    WHERE TTE_TASK_ID = v_taskId;
    
    SELECT OBJNAME_ INTO v_jpdlName
    FROM JBPM4_DEPLOYPROP
    WHERE STRINGVAL_ = v_processDefinitionId;
    
    IF NVL(v_count,0) = 0 THEN
        BEGIN
            SELECT TSEL_ASSIGNEE , TSEL_DURATION INTO v_assignee,v_duration
            FROM TQC_SYS_ESCALATION_LEVELS
            WHERE TSEL_JSD_SYS_CODE = v_sysCode 
            AND TSEL_JSD_JPDL_NAME = v_jpdlName 
            AND TSEL_ACTIVITY_NAME = v_taskName 
            AND TSEL_LEVEL = 1;
            EXCEPTION WHEN NO_DATA_FOUND THEN 
        NULL;
        END;
            
    INSERT INTO TQC_TASK_ESCALATIONS (TTE_TASK_ID, TTE_JPDL_NAME, TTE_ACTIVITY_NAME, TTE_LEVEL,TTE_ASSIGNEE)
    VALUES(v_taskId,v_jpdlName,v_taskName,1,v_assignee);
    ELSE
        BEGIN
            SELECT MAX(TTE_LEVEL) INTO v_level
            FROM TQC_TASK_ESCALATIONS
            WHERE TTE_TASK_ID = v_taskId;
            
            SELECT TSEL_ASSIGNEE , TSEL_DURATION INTO v_assignee,v_duration
            FROM TQC_SYS_ESCALATION_LEVELS
            WHERE TSEL_JSD_SYS_CODE = v_sysCode 
            AND TSEL_JSD_JPDL_NAME = v_jpdlName 
            AND TSEL_ACTIVITY_NAME = v_taskName 
            AND TSEL_LEVEL = v_level;
        EXCEPTION WHEN NO_DATA_FOUND THEN 
        NULL;
        END;
            
        IF NVL(v_level,1) = 4 THEN
            NULL;
        ELSE
            v_level := v_level + 1;
            INSERT INTO TQC_TASK_ESCALATIONS (TTE_TASK_ID, TTE_JPDL_NAME, TTE_ACTIVITY_NAME, TTE_LEVEL,TTE_ASSIGNEE)
            VALUES(v_taskId,v_jpdlName,v_taskName,v_level,v_assignee);
        END IF;
        
        
    END IF;
    
    
    
    IF v_duration IS NOT NULL THEN
    INSERT INTO JBPM4_JOB (DBID_, CLASS_, DBVERSION_, DUEDATE_, STATE_, 
    ISEXCLUSIVE_,RETRIES_,PROCESSINSTANCE_, EXECUTION_, EVENT_)
    SELECT HIBERNATE_SEQUENCE.NEXTVAL,'Timer',DBVERSION_,(SYSDATE + v_duration),'waiting',
    0,3,PROCINST_,EXECUTION_,'timeout'
    FROM JBPM4_TASK
    WHERE DBID_ = v_taskId;
    END IF;
    
    
   
END escalate_proc;
  PROCEDURE createBankBranches(v_addEdit       VARCHAR2,
                           v_tcb_code IN NUMBER, 
                           v_tcb_clnt_code IN NUMBER,
                           v_tcb_sht_desc IN  VARCHAR2,
                           v_tcb_name IN VARCHAR) IS
  BEGIN
        IF v_addEdit = 'A' THEN
            INSERT INTO TQC_CLIENT_BRANCHES
            (TCB_CODE, TCB_CLNT_CODE, TCB_SHT_DESC, TCB_NAME)
            VALUES(TQC_TCB_CODE_SEQ.NEXTVAL,v_tcb_clnt_code,v_tcb_sht_desc,v_tcb_name);
        ELSIF v_addEdit = 'E' THEN
            UPDATE TQC_CLIENT_BRANCHES
            SET 
            TCB_SHT_DESC = v_tcb_sht_desc, 
            TCB_NAME = v_tcb_name
            WHERE TCB_CODE = v_tcb_code;
        ELSIF v_addEdit = 'D' THEN
            DELETE FROM TQC_CLIENT_BRANCHES
            WHERE TCB_CODE = v_tcb_code;
        END IF;
  EXCEPTION WHEN OTHERS
   THEN
        RAISE_ERROR('Error  Manipulating Branch details '|| SQLERRM (SQLCODE)); 
  END createBankBranches;  
PROCEDURE assignBankToClient(v_addEdit IN VARCHAR2,
                             v_tcb_acwa_code IN NUMBER,
                             v_tcb_code IN NUMBER) IS
  BEGIN
   IF v_addEdit = 'A' THEN
   INSERT INTO TQC_CLIENT_USR_BRANCHES
   (TCUB_CODE, TCUB_ACWA_CODE, TCUB_TCB_CODE)
   VALUES(TQC_TCUB_CODE_SEQ.NEXTVAL,v_tcb_acwa_code,v_tcb_code);
   ELSIF v_addEdit='D'
   THEN
   DELETE FROM TQC_CLIENT_USR_BRANCHES 
   WHERE TCUB_TCB_CODE=v_tcb_code;
   --AND TCUB_ACWA_CODE=v_tcb_acwa_code;
   END IF;
  EXCEPTION WHEN OTHERS
   THEN
        RAISE_ERROR('Error  Updating Bank Details '|| SQLERRM (SQLCODE)); 
  END assignBankToClient;  
PROCEDURE assignDefaultBranch(v_tcub_tcb_code IN NUMBER) IS
 v_count     NUMBER;

  BEGIN
   UPDATE TQC_CLIENT_USR_BRANCHES
    SET   TCUB_DEFAULT = 'Y' 
    WHERE TCUB_TCB_CODE = v_tcub_tcb_code;
  EXCEPTION WHEN OTHERS
   THEN
        RAISE_ERROR('Error  Updating Bank Details '|| SQLERRM (SQLCODE)); 
  END assignDefaultBranch;  
  PROCEDURE createWebProductDetails(v_addEdit       VARCHAR2,
                          v_twpd_clnt_code IN NUMBER, 
                          v_twpd_twp_code IN NUMBER, 
                          v_twpd_usr_code IN NUMBER, 
                          v_twpd_username IN VARCHAR2, 
                          v_twpd_dr_limit IN NUMBER,
                          v_twpd_cr_limit IN NUMBER, 
                          v_twpd_policy_use IN VARCHAR2, 
                          v_twpd_endos_use IN VARCHAR2) IS
  BEGIN
        IF v_addEdit = 'A' THEN
            INSERT INTO TQC_WEB_PRODUCT_DETAILS
            (TWPD_CLNT_CODE, TWPD_TWP_CODE, TWPD_USR_CODE, TWPD_USERNAME, TWPD_DR_LIMIT, TWPD_CR_LIMIT, TWPD_POLICY_USE, TWPD_ENDOS_USE)
            VALUES(v_twpd_clnt_code, v_twpd_twp_code, v_twpd_usr_code, v_twpd_username, v_twpd_dr_limit, 
                   v_twpd_cr_limit, v_twpd_policy_use, v_twpd_endos_use);
        ELSIF v_addEdit = 'E' THEN
            UPDATE TQC_WEB_PRODUCT_DETAILS
            SET  TWPD_TWP_CODE=v_twpd_twp_code,
                 TWPD_USR_CODE=v_twpd_usr_code, 
                 TWPD_USERNAME=v_twpd_username, 
                 TWPD_DR_LIMIT=v_twpd_dr_limit, 
                 TWPD_CR_LIMIT=v_twpd_cr_limit, 
                 TWPD_POLICY_USE=v_twpd_policy_use, 
                 TWPD_ENDOS_USE=v_twpd_endos_use
            WHERE TWPD_CLNT_CODE = v_twpd_clnt_code
            AND TWPD_TWP_CODE=v_twpd_twp_code;
        ELSIF v_addEdit = 'D' THEN
            DELETE FROM TQC_WEB_PRODUCT_DETAILS
            WHERE TWPD_CLNT_CODE = v_twpd_clnt_code
            AND TWPD_TWP_CODE=v_twpd_twp_code;
        END IF;
  EXCEPTION WHEN OTHERS
   THEN
        RAISE_ERROR('Error  Manipulating Product details '|| SQLERRM (SQLCODE)); 
  END createWebProductDetails; 
 PROCEDURE outgoingSmsProc(v_addEdit     VARCHAR2,
                          v_tssCode             TQC_SYSTEM_SMS.TSS_CODE%TYPE,                        
                          v_tssDesc             TQC_SYSTEM_SMS.TSS_DESC%TYPE,
                          v_tssUrls             TQC_SYSTEM_SMS.TSS_URL%TYPE,
                          v_username            TQC_SYSTEM_SMS.TSS_USERNAME%TYPE,
                          v_password            TQC_SYSTEM_SMS.TSS_PASSWORD%TYPE,
                          v_source              TQC_SYSTEM_SMS.TSS_SOURCE%TYPE,
                          v_tssDefault          TQC_SYSTEM_SMS.TSS_DEFAULT%TYPE) IS
       
BEGIN
    IF NVL(v_tssDefault,'N') = 'Y' THEN
        UPDATE TQC_SYSTEM_SMS SET 
        TSS_DEFAULT = 'N';
    END IF;
    
    IF v_addEdit = 'A' THEN
        INSERT INTO TQC_SYSTEM_SMS (TSS_CODE,TSS_DESC, TSS_URL, TSS_USERNAME, TSS_PASSWORD, TSS_SOURCE,TSS_DEFAULT)
        VALUES (tss_code_seq.nextval,v_tssDesc,v_tssUrls,v_username,v_password,v_source,v_tssDefault); 
        
    ELSIF v_addEdit = 'E' THEN
        UPDATE TQC_SYSTEM_SMS SET 
        TSS_DESC = v_tssDesc,
        TSS_URL = v_tssUrls,
        TSS_USERNAME = v_username,
        TSS_PASSWORD = v_password,
        TSS_SOURCE = v_source,
        TSS_DEFAULT = v_tssDefault
        WHERE TSS_CODE = v_tssCode;
    ELSIF v_addEdit = 'D' THEN
        DELETE FROM TQC_SYSTEM_SMS
        WHERE TSS_CODE = v_tssCode;
    END IF;
END outgoingSmsProc;
PROCEDURE getRequiredDocs (
      v_add_edit      IN VARCHAR2,
      v_rqd_code IN NUMBER,
      v_rqd_spt_code IN NUMBER,
      v_rqd_spta_code IN NUMBER, 
      v_rqd_sys_code IN NUMBER,
      v_rqc_rdoc_id IN NUMBER
      
   )
   IS
   BEGIN
   
      IF v_add_edit = 'A'
      THEN
      BEGIN
         INSERT INTO TQC_REQUIRED_DOCS
                     (RQD_CODE, RQD_SPT_CODE, RQD_SPTA_CODE, RQD_SYS_CODE,RQC_RDOC_ID
                     )
              VALUES (tqc_rqd_code_seq.NEXTVAL, v_rqd_spt_code, v_rqd_spta_code, v_rqd_sys_code,v_rqc_rdoc_id  );
      EXCEPTION
       WHEN OTHERS
         THEN
             NULL;
      END;
                   
      ELSIF v_add_edit = 'E'
      THEN
         UPDATE TQC_REQUIRED_DOCS
            SET RQD_SPT_CODE=v_rqd_spt_code,
                RQD_SPTA_CODE=v_rqd_spta_code, 
                RQD_SYS_CODE=v_rqd_sys_code,
                RQC_RDOC_ID=v_rqc_rdoc_id
           WHERE RQD_CODE = v_rqd_code;
      ELSIF v_add_edit = 'D'
      THEN
       DELETE FROM TQC_REQUIRED_DOCS
               WHERE RQD_CODE = v_rqd_code;
               
      END IF;
   END;
   PROCEDURE getCountryPref(
   v_cou_name IN VARCHAR2,
   v_cou_mobile_prefix OUT NUMBER, 
   v_cou_client_number OUT NUMBER
   )
   IS
   BEGIN
   --RAISE_ERROR('v_cou_name'||v_cou_name);
   SELECT COU_MOBILE_PREFIX, COU_CLIENT_NUMBER
   INTO v_cou_mobile_prefix,v_cou_client_number
   FROM TQC_COUNTRIES
   WHERE COU_NAME=v_cou_name;
   END;
  /* Formatted on 02/04/2013 17:15 (Formatter Plus v4.8.8) */
  
  PROCEDURE escalations_proc(v_addEdit    VARCHAR2,
                        v_code      TQC_SYS_ESCALATION_LEVELS.TSEL_CODE%TYPE, 
                        v_sysCode   TQC_SYS_ESCALATION_LEVELS.TSEL_JSD_SYS_CODE%TYPE,
                        v_jpdlName  TQC_SYS_ESCALATION_LEVELS.TSEL_JSD_JPDL_NAME%TYPE,
                        v_activity  TQC_SYS_ESCALATION_LEVELS.TSEL_ACTIVITY_NAME%TYPE,
                        v_level     TQC_SYS_ESCALATION_LEVELS.TSEL_LEVEL%TYPE, 
                        v_user      TQC_SYS_ESCALATION_LEVELS.TSEL_ASSIGNEE%TYPE,
                        v_duration  TQC_SYS_ESCALATION_LEVELS.TSEL_DURATION%TYPE,
                        v_cc        TQC_SYS_ESCALATION_LEVELS.TSEL_CC%TYPE) IS
      v_count   NUMBER;
BEGIN
   -- RAISE_ERROR('ERROR '||v_addEdit||' v_code '||v_code||' v_level '||v_level);
    IF v_addEdit = 'A' THEN
        SELECT COUNT(*) INTO v_count
        FROM TQC_SYS_ESCALATION_LEVELS
        WHERE TSEL_JSD_SYS_CODE = v_sysCode
        AND TSEL_JSD_JPDL_NAME = v_jpdlName 
        AND TSEL_ACTIVITY_NAME = v_activity
        AND TSEL_LEVEL = v_level;
        
        IF NVL(v_count,0) > 0 THEN
            RAISE_ERROR('ESCALATION LEVEL ('||v_level||') FOR ACTIVITY PROCESS ALREADY DEFINED');
        END IF;
        
        INSERT INTO TQC_SYS_ESCALATION_LEVELS(TSEL_CODE, TSEL_JSD_SYS_CODE, TSEL_JSD_JPDL_NAME, 
        TSEL_ACTIVITY_NAME, TSEL_LEVEL, TSEL_ASSIGNEE,TSEL_DURATION,TSEL_CC)
        VALUES (TSEL_CODE_SEQ.NEXTVAL,v_sysCode,v_jpdlName,v_activity,v_level,v_user,v_duration,v_cc);
    ELSIF v_addEdit = 'E' THEN
    
        SELECT COUNT(*) INTO v_count
        FROM TQC_SYS_ESCALATION_LEVELS
        WHERE TSEL_JSD_SYS_CODE = v_sysCode
        AND TSEL_JSD_JPDL_NAME = v_jpdlName 
        AND TSEL_ACTIVITY_NAME = v_activity
        AND TSEL_LEVEL = v_level
        AND TSEL_CODE <> v_code;
        
        IF NVL(v_count,0) > 0 THEN
            RAISE_ERROR('ESCALATION LEVEL ('||v_level||') FOR ACTIVITY PROCESS ALREADY DEFINED');
        END IF;
        
        UPDATE TQC_SYS_ESCALATION_LEVELS SET
        TSEL_LEVEL = v_level, 
        TSEL_ASSIGNEE = v_user,
        TSEL_DURATION = v_duration,
        TSEL_CC       = v_cc
        WHERE TSEL_CODE = v_code; 
    ELSIF v_addEdit = 'D' THEN
        DELETE FROM TQC_SYS_ESCALATION_LEVELS
        WHERE TSEL_CODE = v_code;
    END IF;
EXCEPTION
         WHEN OTHERS THEN
         RAISE_ERROR('ERROR OCCURED WHILE TRYING TO MANIPULATE ESCALATION LEVELS' || SQLERRM (SQLCODE));
END escalations_proc;

PROCEDURE reservedWords_proc(v_addEdit  VARCHAR2,
                            v_code              TQC_SYS_RESERVED_WORDS.TSRW_CODE%TYPE, 
                            v_sysCode           TQC_SYS_RESERVED_WORDS.TSRW_SYS_CODE%TYPE, 
                            v_tsrcCode          TQC_SYS_RESERVED_WORDS.TSRW_TSRC_CODE%TYPE, 
                            v_type              TQC_SYS_RESERVED_WORDS.TSRW_TYPE%TYPE,
                            v_name              TQC_SYS_RESERVED_WORDS.TSRW_NAME%TYPE,
                            v_desc              TQC_SYS_RESERVED_WORDS.TSRW_DESC%TYPE) IS
BEGIN
    IF v_addEdit = 'A' THEN
        INSERT INTO TQC_SYS_RESERVED_WORDS (TSRW_CODE, TSRW_SYS_CODE, TSRW_TSRC_CODE, TSRW_TYPE,TSRW_NAME,TSRW_DESC)
        VALUES(TSRW_CODE_SEQ.NEXTVAL,v_sysCode,v_tsrcCode,v_type,v_name,v_desc);
    ELSIF v_addEdit = 'E' THEN
        UPDATE TQC_SYS_RESERVED_WORDS SET 
        TSRW_TSRC_CODE = v_tsrcCode,
        TSRW_TYPE = v_type ,
        TSRW_NAME = v_name,
        TSRW_DESC = v_desc
        WHERE TSRW_CODE = v_code;
    ELSIF v_addEdit = 'D' THEN
        DELETE FROM TQC_SYS_RESERVED_WORDS
        WHERE TSRW_CODE = v_code;
    END IF;
END reservedWords_proc;
PROCEDURE obtainClientNumber(v_clnt_code IN NUMBER,
                            v_clnt_sms_tel OUT VARCHAR2) IS

BEGIN
SELECT CLNT_SMS_TEL
INTO v_clnt_sms_tel
FROM TQC_CLIENTS
WHERE CLNT_CODE=v_clnt_code;
EXCEPTION
WHEN OTHERS
THEN
NULL;
END;
PROCEDURE AddUpdateCountry(v_addEdit  VARCHAR2,
                              v_tcou_code IN NUMBER, 
                              v_cou_sht_desc IN VARCHAR2,
                               v_cou_name IN VARCHAR2
                           
                            ) IS
BEGIN
    IF v_addEdit = 'A' THEN
        INSERT INTO tqc_countries (COU_CODE, COU_SHT_DESC, COU_NAME, COU_NATIONALITY)
        VALUES(TSRW_CODE_SEQ.NEXTVAL,v_cou_sht_desc,v_cou_name,'KENYANESE');
    ELSIF v_addEdit = 'E' THEN
        UPDATE tqc_countries SET 
        COU_SHT_DESC = v_cou_sht_desc,
        COU_NAME = v_cou_name 
        WHERE COU_CODE = v_tcou_code;
    ELSIF v_addEdit = 'D' THEN
        DELETE FROM tqc_countries
        WHERE COU_CODE = v_tcou_code;
    END IF;
END AddUpdateCountry;
PROCEDURE AuthorizeAccount(v_user IN VARCHAR2,v_agn_code IN NUMBER)
IS
BEGIN
UPDATE TQC_AGENCIES
SET  AGN_AUTHORISED='Y', 
     AGN_AUTHORISED_BY=v_user,
     AGN_AUTHORISED_DATE=SYSDATE
WHERE AGN_CODE=v_agn_code;
END;
PROCEDURE addReportSubModule(v_addEdit  VARCHAR2,
                            v_RSM_CODE IN NUMBER, 
                            v_RSM_NAME  IN VARCHAR2, 
                            v_RSM_DESC   IN VARCHAR2, 
                            v_RSM_SRM_CODE    IN NUMBER        ) IS
BEGIN
    IF v_addEdit = 'A' THEN
        insert into tqc_sys_rpt_sub_modules (rsm_code, rsm_name, rsm_desc, rsm_srm_code)
        values(tqc_rsm_code_seq.nextval,v_rsm_name,v_rsm_desc,v_rsm_srm_code);
    ELSIF v_addEdit = 'E' THEN
        UPDATE tqc_sys_rpt_sub_modules SET 
        RSM_NAME=v_RSM_NAME,
        RSM_DESC=v_RSM_DESC, 
        RSM_SRM_CODE=v_RSM_SRM_CODE
        WHERE RSM_CODE = v_RSM_CODE;
    ELSIF v_addEdit = 'D' THEN
        DELETE FROM tqc_sys_rpt_sub_modules
        WHERE RSM_CODE = v_RSM_CODE;
    END IF;
END addReportSubModule;
PROCEDURE AssignReport(v_rpt_code IN NUMBER,v_rpt_rsm_code IN NUMBER)
IS
BEGIN
--RAISE_ERROR('TEST'||v_rpt_code||'v_rpt_rsm_code'||v_rpt_rsm_code);
UPDATE TQC_SYSTEM_REPORTS
SET RPT_RSM_CODE=v_rpt_rsm_code
WHERE RPT_CODE=v_rpt_code;
END;
PROCEDURE UnAssignReport(v_rpt_code IN NUMBER,v_rpt_rsm_code IN NUMBER)
IS
BEGIN
UPDATE TQC_SYSTEM_REPORTS
SET RPT_RSM_CODE=NULL
WHERE RPT_CODE=v_rpt_code;
END;
PROCEDURE UpdateReportDetails(v_rpt_code IN NUMBER,v_rpt_description IN VARCHAR2)
IS
BEGIN
--raise_error('v_rpt_code'||v_rpt_code);
UPDATE TQC_SYSTEM_REPORTS
SET RPT_DESCRIPTION=v_rpt_description
WHERE RPT_CODE=v_rpt_code;
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

            INSERT INTO tqc_occupations
                        (occ_code, occ_sec_code, occ_sht_desc, occ_name
                        )
                 VALUES (tqc_occ_code_seq.NEXTVAL, v_occ_sec_code,v_occ_sht_desc, v_occ_name
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
         SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'YY'))
                || tqc_clnt_code_seq.NEXTVAL
           INTO vclntcode
           FROM DUAL;

         v_ent_code_val := gin_ent_code_seq.NEXTVAL;

         INSERT INTO tqc_entities
                     (ent_code, ent_sht_desc, ent_name,
                      ent_physical_address, ent_postal_address,
                      ent_twn_code, ent_cou_code, ent_email_address,
                      ent_fax, ent_status, ent_bbr_code,
                      ent_bank_acc_no, ent_zip, ent_sms_tel,
                      ent_created_by, ent_date_created, ent_pin, ent_tel1,
                      ent_acc_no, ent_status_remarks, ent_brn_code,
                      ent_id_no, ent_tel2, ent_sec_code, ent_runoff,
                      ent_old_acc_no, ent_title, ent_wef, ent_wet,
                      ent_contact_person
                     )
              VALUES (v_ent_code_val, v_ent_sht_desc, v_ent_name,
                      v_ent_physical_address, v_ent_postal_address,
                      v_ent_twn_code, v_ent_cou_code, v_ent_email_address,
                      v_ent_fax, v_ent_status, v_ent_bbr_code,
                      v_ent_bank_acc_no, v_ent_zip, v_ent_sms_tel,
                      v_ent_created_by, SYSDATE, v_ent_pin, v_ent_tel1,
                      v_ent_acc_no, v_ent_status_remarks, v_ent_brn_code,
                      v_ent_id_no, v_ent_tel2, v_ent_sec_code, v_ent_runoff,
                      v_ent_old_acc_no, v_ent_title, v_ent_wef, v_ent_wet,
                      v_ent_contact_person
                     );

         IF NVL (v_source, 'N') = 'C'
         THEN
            INSERT INTO tqc_clients
                        (clnt_code, clnt_sht_desc, clnt_name,
                         clnt_wef, clnt_created_by, clnt_agnt_status,
                         clnt_date_created, clnt_direct_client, clnt_ent_code
                        )
                 VALUES (vclntcode, v_ent_sht_desc, v_ent_name,
                         NVL (v_ent_wef, SYSDATE), v_ent_created_by, 'N',
                         SYSDATE, ' ', v_ent_code_val
                        );
         ELSIF NVL (v_source, 'N') = 'A'
         THEN
            INSERT INTO tqc_agencies
                        (agn_code, agn_sht_desc,
                         agn_name, agn_brn_code, agn_runoff,
                         agn_enable_web_edit
                        )
                 VALUES (tqc_agn_code_seq.NEXTVAL, v_ent_sht_desc,
                         v_ent_name, v_ent_brn_code, 'N',
                         'N'
                        );
         ELSIF NVL (v_source, 'N') = 'S'
         THEN
            INSERT INTO tqc_service_providers
                        (spr_code, spr_sht_desc,
                         spr_name, spr_status
                        )
                 VALUES (tqc_spr_code_seq.NEXTVAL, v_ent_sht_desc,
                         v_ent_name, 'A'
                        );
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

PROCEDURE save_client_details(v_type VARCHAR2,v_clnt_code NUMBER,v_pin_pass VARCHAR2,v_pass VARCHAR2,v_idno VARCHAR2)
IS
BEGIN
   
  --raise_error('v_type '||v_type);
    UPDATE TQC_CLIENTS
    SET CLNT_PASSPORT_NO=v_pass,
    CLNT_ID_REG_NO=v_idno, 
    CLNT_PIN=v_pin_pass
    WHERE CLNT_CODE=v_clnt_code;
  
  
  


END;
 PROCEDURE CREATE_EMAIL_MSG(v_clnt_code IN NUMBER,
                        v_agn_code IN NUMBER,
                        v_quot_code IN NUMBER,
                        v_pol_code IN NUMBER,
                        v_pol_no IN VARCHAR2,
                        v_clm_no IN VARCHAR2,
                        v_email_addr IN VARCHAR2,
                        v_msg_subj IN VARCHAR2,
                        v_msg_text IN VARCHAR2,
                        v_sys_code IN NUMBER,
                        v_sys_mod    IN VARCHAR2,
                        v_email_status IN VARCHAR2) IS
   v_user VARCHAR2(35):= Pkg_Global_Vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
   v_email_code NUMBER;
BEGIN
   
  SELECT TQC_SMS_CODE_SEQ.NEXTVAL INTO v_email_code FROM DUAL;
  INSERT INTO TQC_EMAIL_MESSAGES(EMAIL_CODE, EMAIL_SYS_CODE, EMAIL_SYS_MODULE, EMAIL_CLNT_CODE, 
  EMAIL_AGN_CODE, EMAIL_POL_CODE, EMAIL_POL_NO, EMAIL_CLM_NO, EMAIL_QUOT_CODE,EMAIL_ADDRESS,
  EMAIL_SUBJ,EMAIL_MSG, EMAIL_STATUS, EMAIL_PREPARED_BY, EMAIL_PREPARED_DATE, EMAIL_SEND_DATE)
                      VALUES(v_email_code,v_sys_code,v_sys_mod,v_clnt_code,
                            v_agn_code, v_pol_code, v_pol_no,
                            v_clm_no, v_quot_code, v_email_addr,v_msg_subj,v_msg_text,'S',
                            v_user, SYSDATE,SYSDATE);
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
   v_bpn_reg_clmt_code    IN   NUMBER
)
IS
   v_count   NUMBER;
BEGIN
--raise_error('v_bpn_code'||v_bpn_code||'v_add_edit'||v_add_edit);
   IF v_add_edit = 'A'
   THEN
      BEGIN
         INSERT INTO tqc_business_persons
                     (bpn_code, bpn_id_no, bpn_address,
                      bpn_tel, bpn_mobile_no, bpn_email, bpn_type,
                      bpn_zip, bpn_town, bpn_cou_code, bpn_name,
                      bpn_pin, bpn_bbr_code, bpn_bank_acc_no,
                      bpn_bbr_swift_code, bpn_reg_clmt_code
                     )
              VALUES (tqc_bpn_code_seq.NEXTVAL, v_bpn_id_no, v_bpn_address,
                      v_bpn_tel, v_bpn_mobile_no, v_bpn_email, v_bpn_type,
                      v_bpn_zip, v_bpn_town, v_bpn_cou_code, v_bpn_name,
                      v_bpn_pin, v_bpn_bbr_code, v_bpn_bank_acc_no,
                      v_bpn_bbr_swift_code, v_bpn_reg_clmt_code
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
                bpn_reg_clmt_code = v_bpn_reg_clmt_code
          WHERE bpn_code = v_bpn_code;
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
         DELETE FROM tqc_business_persons
               WHERE bpn_code = v_bpn_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (   'Error occured while deleting record '
                         || SQLERRM (SQLCODE)
                        );
      END;
   END IF;
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
   v_bpn_clnt_code    IN   NUMBER,
   v_bpn_payee_type IN VARCHAR2
)
IS
   v_count   NUMBER;
BEGIN
--raise_error('v_bpn_bbr_code'||v_bpn_bbr_code||'v_add_edit'||v_add_edit||'v_bpn_code'||v_bpn_code||'v_bpn_name'||v_bpn_name);
   IF v_add_edit = 'A'
   THEN
      BEGIN
         INSERT INTO tqc_business_persons
                     (bpn_code, bpn_id_no, bpn_address,
                      bpn_tel, bpn_mobile_no, bpn_email, bpn_type,
                      bpn_zip, bpn_town, bpn_cou_code, bpn_name,
                      bpn_pin, bpn_bbr_code, bpn_bank_acc_no,
                      bpn_bbr_swift_code, BPN_CLNT_CODE,
                      BPN_PAYEE_TYPE
                     )
              VALUES (tqc_bpn_code_seq.NEXTVAL, v_bpn_id_no, v_bpn_address,
                      v_bpn_tel, v_bpn_mobile_no, v_bpn_email, v_bpn_type,
                      v_bpn_zip, v_bpn_town, v_bpn_cou_code, v_bpn_name,
                      v_bpn_pin, v_bpn_bbr_code, v_bpn_bank_acc_no,
                      v_bpn_bbr_swift_code, v_bpn_clnt_code,
                      v_bpn_payee_type
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
                BPN_CLNT_CODE = v_bpn_clnt_code,
                bpn_payee_type=v_bpn_payee_type
          WHERE bpn_code = v_bpn_code;
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
         DELETE FROM tqc_business_persons
               WHERE bpn_code = v_bpn_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error (   'Error occured while deleting record '
                         || SQLERRM (SQLCODE)
                        );
      END;
   END IF;
END;
  END; 
/