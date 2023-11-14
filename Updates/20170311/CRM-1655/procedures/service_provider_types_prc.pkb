--Update the following procedure service_provider_types_prc in: TQC_SETUPS_PKG body



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
            SELECT COUNT(*) INTO v_count FROM TQC_SERV_PRV_TYPE_ACTVTS
                WHERE SPTA_SPT_CODE = v_spt_code;
                
                IF v_count > 0
                THEN
                    raise_error (   'Ensure no service provider activities for selected type exist '
                                || SQLERRM (SQLCODE)
                               );
                ELSE
                    DELETE FROM tqc_service_provider_types
                          WHERE spt_code = v_spt_code;
                END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
   END;

   