   --tqc_setups_cursor.currency_rates_by_curr_rec
   TYPE currency_rates_by_curr_rec IS RECORD (
      crt_code            tqc_currency_rates.crt_code%TYPE,
      crt_cur_code        tqc_currency_rates.crt_cur_code%TYPE,
      crt_rate            tqc_currency_rates.crt_rate%TYPE,
      crt_date            VARCHAR2(50),
      crt_base_cur_code   tqc_currency_rates.crt_base_cur_code%TYPE,
      cur_desc            tqc_currencies.cur_code%TYPE,
      crt_wef             VARCHAR2(50),
      crt_wet            VARCHAR2(50)
   );
   
   --tqc_setups_cursor.get_currency_rates_by_currency
      PROCEDURE get_currency_rates_by_currency (
      v_crt_cur_code               tqc_currency_rates.crt_cur_code%TYPE,
      v_currency_rates_ref   OUT   currency_rates_by_curr_ref
   )
   IS
   BEGIN
      OPEN v_currency_rates_ref FOR
         SELECT crt_code, crt_cur_code, crt_rate, to_char(crt_date,'DD/MON/RRRR HH24:MI:SS') crt_date,
                crt_base_cur_code, cur_desc, to_char(crt_wef,'DD/MON/RRRR HH24:MI:SS')  crt_wef,
                to_char(crt_wet,'DD/MON/RRRR HH24:MI:SS') crt_wet
           FROM tqc_currency_rates, tqc_currencies
          WHERE crt_cur_code = cur_code
                AND crt_base_cur_code = v_crt_cur_code;
   END get_currency_rates_by_currency;

   
   --tqc_setups_pkg.currency_rates_prc
    PROCEDURE currency_rates_prc (
      v_add_edit            IN   VARCHAR2,
      v_crt_code            IN   tqc_currency_rates.crt_code%TYPE,
      v_crt_cur_code        IN   tqc_currency_rates.crt_cur_code%TYPE,
      v_crt_rate            IN   tqc_currency_rates.crt_rate%TYPE,
      v_crt_date            IN   VARCHAR2,
      v_crt_base_cur_code   IN   tqc_currency_rates.crt_base_cur_code%TYPE,
      v_crt_wef             IN  VARCHAR2,
      v_crt_wet             IN  VARCHAR2,
      v_user_code          IN NUMBER DEFAULT NULL
   );
   
   
   --tqc_setups_pkg.currency_rates_prc
    PROCEDURE currency_rates_prc (
      v_add_edit            IN   VARCHAR2,
      v_crt_code            IN   tqc_currency_rates.crt_code%TYPE,
      v_crt_cur_code        IN   tqc_currency_rates.crt_cur_code%TYPE,
      v_crt_rate            IN   tqc_currency_rates.crt_rate%TYPE,
      v_crt_date            IN   VARCHAR2,
      v_crt_base_cur_code   IN   tqc_currency_rates.crt_base_cur_code%TYPE,
      v_crt_wef             IN  VARCHAR2,
      v_crt_wet             IN  VARCHAR2,
      v_user_code          IN NUMBER DEFAULT NULL
   )
   IS
      CURSOR currref(v_crt_wef_dt DATE)
      IS
         SELECT crt_code, crt_cur_code, crt_rate, crt_date,
                crt_base_cur_code, crt_wef, crt_wet
           FROM tqc_currency_rates
          WHERE crt_cur_code = v_crt_cur_code AND crt_wet > v_crt_wef_dt
          AND crt_code != v_crt_code;

      v_count   NUMBER;
      v_crt_date_dt          DATE;
      v_crt_wef_dt          DATE;
      v_crt_wet_dt          DATE;
   BEGIN
   
             v_crt_date_dt := to_date(v_crt_wet,'DD/MON/RRRR HH24:MI:SS');
             v_crt_wef_dt := to_date(v_crt_wef,'DD/MON/RRRR HH24:MI:SS');
             v_crt_wet_dt := to_date(v_crt_wet,'DD/MON/RRRR HH24:MI:SS');
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

         
            
            
             
                IF trunc(v_crt_wef_dt) < trunc(SYSDATE)
            THEN
               raise_error ('Wef cannot be less than today...');
            END IF;
            
            
            FOR i IN currref(v_crt_wef_dt)
            LOOP
               UPDATE tqc_currency_rates
                  SET crt_wet = SYSDATE
                WHERE crt_code = i.crt_code;
            END LOOP;

            --raise_error('Currency Rate Processing Successful '||v_count);
            INSERT INTO tqc_currency_rates
                        (crt_code, crt_cur_code,
                         crt_rate, crt_date, crt_base_cur_code,
                         crt_wef, crt_wet,crt_created_date,crt_created_by
                        )
                 VALUES (tqc_crt_code_seq.NEXTVAL, v_crt_cur_code,
                         v_crt_rate, v_crt_date_dt, v_crt_base_cur_code,
                         v_crt_wef_dt, v_crt_wet_dt,SYSDATE,v_user_code
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
            FOR i IN currref(v_crt_wef_dt)
            LOOP
               IF v_crt_wef BETWEEN i.crt_wef AND i.crt_wet
               THEN
                  raise_error
                     ('Your Currency Period Overlaps with another period.Please check'
                     );
               END IF;

               IF v_crt_wet BETWEEN i.crt_wef AND i.crt_wet
               THEN
                  raise_error
                     ('Your Currency Period Overlaps with another period.Please check'
                     );
               END IF;
            END LOOP;

            UPDATE tqc_currency_rates
               SET crt_cur_code = v_crt_cur_code,
                   crt_rate = v_crt_rate,
                   crt_date = v_crt_date_dt,
                   crt_base_cur_code = v_crt_base_cur_code,
                   crt_wef = v_crt_wef_dt,
                   crt_wet = v_crt_wet_dt,
                   crt_updated_date= SYSDATE,
                   crt_updated_by=v_user_code
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