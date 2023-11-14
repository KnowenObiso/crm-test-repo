CREATE OR REPLACE TRIGGER TQ_GIS."MTRAN_TRIGGER_DIV"
   BEFORE INSERT OR UPDATE
   ON tq_gis.gin_master_transactions
   REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
DISABLE
DECLARE
   tmpvar       NUMBER;
   v_div_code   NUMBER;
BEGIN
   IF UPDATING OR INSERTING
   THEN
      IF :NEW.mtran_tran_type != 'ADM'
      THEN
         IF tqc_interfaces_pkg.get_org_type (37) != 'INS'
         THEN
            IF    :NEW.mtran_div_code IS NULL
               OR (:NEW.mtran_btr_trans_code IN ('JV', 'RFC'))
            THEN
               IF :NEW.mtran_client_type = 'D'
               THEN
                  BEGIN
                     SELECT clna_div_code
                       INTO v_div_code
                       FROM tqc_client_accounts
                      WHERE clna_sht_desc = :NEW.mtran_control_acc;
                  EXCEPTION
                     WHEN OTHERS
                     THEN
                        raise_application_error (-20056,
                                                    'TYPE='
                                                 || :NEW.mtran_client_type
                                                 || '=='
                                                 || :NEW.mtran_client_code
                                                 || ' CONTROL_ACC:'
                                                 || :NEW.mtran_control_acc
                                                 || SQLERRM (SQLCODE)
                                                );
                  END;
               ELSIF :NEW.mtran_client_type != 'D'
               THEN
                  BEGIN
                     SELECT aga_div_code
                       INTO v_div_code
                       FROM tqc_agency_accounts
                      WHERE aga_sht_desc = :NEW.mtran_control_acc;
                  EXCEPTION
                     WHEN OTHERS
                     THEN
                        raise_application_error (-20056,
                                                    'TYPE='
                                                 || :NEW.mtran_client_type
                                                 || '=='
                                                 || :NEW.mtran_client_code
                                                 || ' CONTROL_ACC:'
                                                 || :NEW.mtran_control_acc
                                                 || SQLERRM (SQLCODE)
                                                );
                  END;
               END IF;

               IF v_div_code IS NOT NULL
               THEN
                  :NEW.mtran_div_code := v_div_code;
               ELSE
                  raise_application_error
                          (-20056,
                              'MTRAN ERROR: Cannot Insert a Null Division...'
                           || :NEW.mtran_control_acc
                           || ' client type '
                           || :NEW.mtran_client_type
                          );
               END IF;
            END IF;

            IF :NEW.mtran_client_type = 'D'
            THEN
               IF :NEW.mtran_div_code = 23 AND :NEW.mtran_brn_code != 144
               THEN
                  IF :NEW.mtran_btr_trans_code IN ('JV', 'RFC')
                  THEN
                     :NEW.mtran_brn_code := 144;
                  ELSE
                     raise_application_error
                        (-20056,
                            'Transaction Error, Chester House Div Belongs To Chester Branch  Brn Code== '
                         || :NEW.mtran_brn_code
                         || ' Div Code == '
                         || :NEW.mtran_div_code
                        );
                  END IF;
               ELSIF :NEW.mtran_div_code = 27 AND :NEW.mtran_brn_code != 143
               THEN
                  IF :NEW.mtran_btr_trans_code IN ('JV', 'RFC')
                  THEN
                     :NEW.mtran_brn_code := 143;
                  ELSE
                     raise_application_error
                        (-20056,
                            'Transaction Error, Pwani Div Belongs To Mombasa Branch  Brn Code== '
                         || :NEW.mtran_brn_code
                         || ' Div Code == '
                         || :NEW.mtran_div_code
                        );
                  END IF;
               ELSIF :NEW.mtran_brn_code = 144 AND :NEW.mtran_div_code NOT IN(1,23)
               THEN
                  raise_application_error
                     (-20056,
                         'Transaction Error, Wrong Branch Attached (Chester)  Brn Code== '
                      || :NEW.mtran_brn_code
                      || ' Div Code == '
                      || :NEW.mtran_div_code
                      ||'MTRAN_NO=='
                      || :NEW.MTRAN_NO
                     );
               ELSIF :NEW.mtran_brn_code = 143 AND :NEW.mtran_div_code != 27
               THEN
                  raise_application_error
                     (-20056,
                         'Transaction Error, Wrong Branch Attached (Mombasa) Brn Code== '
                      || :NEW.mtran_brn_code
                      || ' Div Code == '
                      || :NEW.mtran_div_code
                     );
               END IF;
            END IF;
         END IF;
      END IF;
   END IF;
END mtran_trigger_div;
/


