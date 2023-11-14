  PROCEDURE get_currency_rates_by_currency (
      v_crt_cur_code               tqc_currency_rates.crt_cur_code%TYPE,
      v_currency_rates_ref   OUT   currency_rates_by_curr_ref
   )
   IS
   BEGIN
      OPEN v_currency_rates_ref FOR
         SELECT crt_code, crt_cur_code, crt_rate, crt_date,
                crt_base_cur_code, cur_desc, crt_wef, crt_wet
           FROM tqc_currency_rates, tqc_currencies
          WHERE crt_cur_code = cur_code
                AND crt_base_cur_code = v_crt_cur_code
                ORDER BY crt_wet DESC;
   END get_currency_rates_by_currency;