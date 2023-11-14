--
-- PROCESS_GIS_POL_MEMO_2_0122016  (Function) 
--
CREATE OR REPLACE FUNCTION TQ_CRM.process_gis_pol_memo_2_0122016 (
      v_pol_batch_no   IN   NUMBER,
      v_claim_no       IN   VARCHAR2,
      v_raw_txt        IN   VARCHAR2,
      v_app_lvl        IN   VARCHAR2  ,
  v_gcc_code IN   NUMBER  DEFAULT NULL,
  v_csd_code IN   NUMBER  DEFAULT NULL,
  v_trans_no       IN   NUMBER DEFAULT NULL
   )
      RETURN VARCHAR2
   IS
      lv_temp_txt       VARCHAR2 (10000);
      v_pol_no          gin_policies.pol_policy_no%TYPE;
      v_clm_no          gin_claim_master_bookings.cmb_claim_no%TYPE;
      v_sa              NUMBER;
      v_premium         NUMBER;
      v_client          tqc_clients.clnt_name%TYPE;
      v_prem            NUMBER;
      v_date            DATE;
      v_time            DATE;
      v_reg             VARCHAR2 (100);
      v_loc             VARCHAR2 (100);
      v_courtname       VARCHAR2 (100);
      v_courtdate       DATE;
      v_caseno          VARCHAR2 (100);
      vcoverfrom        DATE;
      vcoverto          DATE;
      veffdate          DATE;
      vrendate          DATE;
      v_desc            VARCHAR2 (100);
      v_loss_type       VARCHAR2 (100);
      v_item_desc       VARCHAR2 (100);
      v_claimant_type   VARCHAR2 (15);
      v_agent           tqc_agencies.agn_name%TYPE;
      v_int_parties     VARCHAR2 (100);
      v_uw_year         INT;
      v_branch          tqc_branches.brn_name%TYPE;
      v_address         VARCHAR2 (100);
      v_twn             VARCHAR2 (100);
      v_risk_id         VARCHAR2 (100);
      v_surname         tqc_clients.clnt_name%TYPE;
      v_risk_desc       gin_insured_property_unds.ipu_item_desc%TYPE;
      v_risk_wef        DATE;
      v_risk_wet        DATE;
      v_cover           VARCHAR2 (50);
      v_risk_prem       DECIMAL (20, 2);
      v_risk_value      DECIMAL (20, 2);
      v_currency        VARCHAR2 (5);
      v_agn_code        NUMBER;
      v_agn_name        tqc_agencies.agn_name%TYPE;
      v_agn_add         tqc_agencies.agn_physical_address%TYPE;
      v_client_code     NUMBER;
      v_client_add      VARCHAR2 (100);
      v_insured_name    VARCHAR2 (300);
      v_insured_add     VARCHAR2 (100);
      v_org_name        VARCHAR2 (100);
      v_org_add         VARCHAR2 (100);
      v_ipu_desc        VARCHAR2 (100);
      v_amt_words       VARCHAR2 (50);
      v_cur_code        NUMBER;
      v_period          NUMBER;
      v_obligor         gin_bonds.bon_obligor%TYPE;
      v_ob_address      gin_bonds.obligor_addrs%TYPE;
      v_ob_town         gin_bonds.bon_obligor_town%TYPE;
      v_ob_country      gin_bonds.bon_obligor_country%TYPE;
      v_ob_emp          gin_bonds.bon_employer%TYPE;
      v_certs           VARCHAR2 (200)                                 := '';
      v_srv_fee_pct     NUMBER (5, 1);
      v_bank_name VARCHAR2 (200)   ;
      v_preffered_mode VARCHAR2 (5)   ;
      v_acc_no     VARCHAR2 (200)   ;
      v_clmt_code  NUMBER;
      v_apco_code NUMBER;
      v_clmt_type  VARCHAR2 (1)   ;
      v_amount     NUMBER;
      v_cur_symbol VARCHAR2 (10);
      v_paymentmode VARCHAR2 (10);
      bank_cursor                sys_refcursor;
      v_branch_name VARCHAR2 (100);
      v_cpv_code    number;

      CURSOR cur_certs (v_pol_batch IN NUMBER)
      IS
         SELECT srv_property ipu_property_id
           FROM gin_survey_info
          WHERE srv_pol_batch_no = v_pol_batch;
   BEGIN
      lv_temp_txt := v_raw_txt;

--      RAISE_ERROR('v_app_lvl '||v_app_lvl);
      IF v_app_lvl = 'U'
      THEN
         IF v_pol_batch_no IS NOT NULL
         THEN
            BEGIN
               SELECT pol_policy_no, pol_si_diff,
                      gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                      pol_tot_endos_diff_amt, pol_policy_cover_from,
                      pol_policy_cover_to, pol_wef_dt, pol_renewal_dt,
                      agn_name, pol_oth_int_parties, pol_uw_year, brn_name,
                      clnt_postal_addrs, twn_name, ipu_property_id,
                      ipu_item_desc, ipu_wef, ipu_wet, ipu_covt_sht_desc,
                      ipu_endos_diff_amt, ipu_value
                 INTO v_pol_no, v_sa,
                      v_client,
                      v_prem, vcoverfrom,
                      vcoverto, veffdate, vrendate,
                      v_agent, v_int_parties, v_uw_year, v_branch,
                      v_address, v_twn, v_risk_id,
                      v_risk_desc, v_risk_wef, v_risk_wet, v_cover,
                      v_risk_prem, v_risk_value
                 FROM gin_policies,
                      tqc_clients,
                      tqc_agencies,
                      tqc_branches,
                      tqc_towns,
                      tq_gis.gin_insured_property_unds
                WHERE pol_prp_code = clnt_code
                  AND pol_agnt_agent_code = agn_code
                  AND twn_code(+) = clnt_twn_code
                  AND brn_code = pol_brn_code
                  AND ipu_pol_batch_no = pol_batch_no
                  AND pol_batch_no = v_pol_batch_no
                  AND ROWNUM < 2;

               BEGIN
                  FOR cur_certs_cur IN cur_certs (v_pol_batch_no)
                  LOOP
                     v_certs :=
                            v_certs || ' , ' || cur_certs_cur.ipu_property_id;
                  END LOOP;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_application_error (-20001,
                                              'Error checking certs....'
                                             );
               END;

               BEGIN
                  SELECT srv_fee_pct
                    INTO v_srv_fee_pct
                    FROM gin_survey_info
                   WHERE srv_pol_batch_no = v_pol_batch_no;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_application_error (-20001,
                                              'Error checking certs....'
                                             );
               END;

               lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
               lv_temp_txt := REPLACE (lv_temp_txt, '[POLNO]', v_pol_no);
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERFROM]',
                           TO_CHAR (vcoverfrom, 'DD/MM/RRRR')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERTO]',
                           TO_CHAR (vcoverto, 'DD/MM/RRRR')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[EFFDATE]',
                           TO_CHAR (veffdate, 'DD/MM/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[RENEWALDATE]',
                           TO_CHAR (vrendate, 'DD/MM/RRRR')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[SA]',
                           TO_CHAR (NVL (v_sa, 0), 'FM999,999,999.00')
                          );
               lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[PREMIUM]',
                           TO_CHAR (NVL (v_prem, 0), 'FM999,999,999.00')
                          );
               lv_temp_txt := REPLACE (lv_temp_txt, '[AGENT]', v_agent);
               lv_temp_txt :=
                          REPLACE (lv_temp_txt, '[INTPARTIES]', v_int_parties);
               lv_temp_txt := REPLACE (lv_temp_txt, '[UWYEAR]', v_uw_year);
               lv_temp_txt := REPLACE (lv_temp_txt, '[BRANCH]', v_branch);
               lv_temp_txt := REPLACE (lv_temp_txt, '[ADDRESS]', v_address);
               lv_temp_txt := REPLACE (lv_temp_txt, '[TOWN]', v_twn);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKID]', v_risk_id);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKDESC]', v_risk_desc);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWEF]', v_risk_wef);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWET]', v_risk_wet);
               lv_temp_txt := REPLACE (lv_temp_txt, '[COVERTYPE]', v_cover);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKPREM]', v_risk_prem);
               lv_temp_txt :=
                            REPLACE (lv_temp_txt, '[RISKVALUE]', v_risk_value);
               lv_temp_txt := REPLACE (lv_temp_txt, '[CERTS]', v_certs);
               lv_temp_txt :=
                             REPLACE (lv_temp_txt, '[FEERATE]', v_srv_fee_pct);
               raise_error ('HERE');
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[WEF]',
                           TO_CHAR (veffdate, 'DD/MM/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[WET]',
                           TO_CHAR (vcoverto, 'DD/MM/YYYY')
                          );
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
               WHEN OTHERS
               THEN
                  raise_error ('ERROR: ' || SQLERRM (SQLCODE));
            END;
         ELSE
            IF v_pol_no IS NOT NULL
            THEN
               lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
            END IF;
         END IF;
      ELSIF v_app_lvl = 'S'
      THEN
         SELECT pol_policy_no, pol_total_sum_insured,
                gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                clnt_name, pol_total_fap, pol_policy_cover_from,
                pol_policy_cover_to, pol_wef_dt, pol_renewal_dt,
                pol_agnt_agent_code, pol_cur_code, pol_oth_int_parties
           INTO v_pol_no, v_sa,
                v_client,
                v_surname, v_prem, vcoverfrom,
                vcoverto, veffdate, vrendate,
                v_agn_code, v_cur_code, v_int_parties
           FROM gin_policies, tqc_clients
          WHERE pol_prp_code = clnt_code AND pol_batch_no = v_pol_batch_no;

         --MESSAGE('[POLICYNO]'||lv_temp_txt);PAUSE;
         lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
         lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
         lv_temp_txt := REPLACE (lv_temp_txt, '[SURNAME]', v_surname);
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[COVERFROM]',
                        TO_CHAR (vcoverfrom, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[COVERTO]',
                        TO_CHAR (vcoverto, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverto, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverto, 'RRRR')
                    );
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[EFFDATE]',
                        TO_CHAR (veffdate, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[RENEWALDATE]',
                     TO_CHAR (vrendate, 'DD/MM/YYYY')
                    );
         lv_temp_txt := REPLACE (lv_temp_txt, '[INTPARTIES]', v_int_parties);
         --MESSAGE(v_pol_no||lv_temp_txt);PAUSE;
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[SA]',
                     TO_CHAR (NVL (v_sa, 0), 'FM999,999,999.00')
                    );
         -- lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
         lv_temp_txt := REPLACE (lv_temp_txt, '[PREMIUM]', NVL (v_prem, 0));
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[AMTINWORDS]',
                     fms_cheques_pkg.num_to_words (NVL (v_sa, 0), v_cur_code)
                    );

         SELECT MONTHS_BETWEEN (vcoverto, vcoverfrom)
           INTO v_period
           FROM DUAL;

         lv_temp_txt := REPLACE (lv_temp_txt, '[PERIOD]', ROUND (v_period, 0));

         SELECT ipu_item_desc
           INTO v_ipu_desc
           FROM tq_gis.gin_insured_property_unds
          WHERE ipu_pol_batch_no = v_pol_batch_no AND ROWNUM = 1;

         lv_temp_txt := REPLACE (lv_temp_txt, '[ITEMDESC]', v_ipu_desc);

         SELECT org_name, org_phy_addrs
           INTO v_org_name, v_org_add
           FROM tqc_organizations
          WHERE org_code = 2;

         lv_temp_txt := REPLACE (lv_temp_txt, '[ORGNAME]', v_org_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[ORGADDR]', v_org_add);

         SELECT agn_name, agn_physical_address
           INTO v_agn_name, v_agn_add
           FROM tqc_agencies
          WHERE agn_code = v_agn_code;

         lv_temp_txt := REPLACE (lv_temp_txt, '[AGNNAME]', v_agn_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[AGNADDR]', v_agn_add);

         BEGIN
            SELECT gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                   clnt_physical_addrs
              INTO v_insured_name,
                   v_insured_add
              FROM tq_gis.gin_policy_insureds, tqc_clients
             WHERE polin_prp_code = clnt_code
               AND polin_pol_batch_no = v_pol_batch_no
               AND ROWNUM = 1;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         lv_temp_txt := REPLACE (lv_temp_txt, '[INSNAME]', v_insured_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[INSADDR]', v_insured_add);

         BEGIN
            SELECT bon_obligor, obligor_addrs, bon_obligor_town,
                   bon_obligor_country, bon_employer, bon_contract_date
              INTO v_obligor, v_ob_address, v_ob_town,
                   v_ob_country, v_ob_emp, v_date
              FROM gin_bonds, gin_insured_property_unds
             WHERE bon_ipu_code = ipu_code
               AND ipu_pol_batch_no = v_pol_batch_no;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         lv_temp_txt := REPLACE (lv_temp_txt, '[BANK]', v_obligor);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKADD]', v_ob_address);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKTWN]', v_ob_town);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKSTATE]', v_ob_country);
         lv_temp_txt := REPLACE (lv_temp_txt, '[EMPLOYER]', v_ob_emp);
         
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[CONTRACTDATE]',
                        TO_CHAR (v_date, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (v_date, 'Month'))
                     || ' '
                     || TO_CHAR (v_date, 'RRRR')
                    );
      ELSIF v_app_lvl = 'C'
      THEN

         BEGIN
            SELECT DISTINCT pol_policy_no, cmb_claim_no,
                            pol_total_sum_insured,
                            DECODE (reg_third_party,
                                    'S', gis_utilities.clnt_name (clnt_name,
                                                                  clnt_surname
                                                                 ),
                                    cld_other_names || ' ' || cld_surname
                                   ) claimant_name,
                            pol_tot_endos_diff_amt, cmb_claim_date,
                            cmb_loss_date_time, cmb_ipu_property_id risk_id,
                            cmb_location, cmb_loss_desc,
                            NVL (cmb_other_cover_details, 'Repairable loss')
                                                                    loss_type,
                            ipu_item_desc risk_description,
                            DECODE (reg_third_party,
                                    'S', 'SELF',
                                    'THIRD PARTY'
                                   ) claimant_type
                       INTO v_pol_no, v_clm_no,
                            v_sa,
                            v_client,
                            v_prem, v_date,
                            v_time, v_reg,
                            v_loc, v_desc,
                            v_loss_type,
                            v_item_desc,
                            v_claimant_type
                       FROM gin_claim_master_bookings,
                            gin_policies,
                            tqc_clients,
                            tq_gis.gin_insured_property_unds,
                            gin_rgstd_claimants,
                            gin_claimants
                      WHERE cmb_pol_batch_no = pol_batch_no
                        AND reg_cld_code = clnt_code
                        AND ipu_pol_batch_no = pol_batch_no
                        AND reg_cld_code = cld_code(+)
                        AND pol_prp_code = clnt_code
                        AND cmb_claim_no = v_claim_no
                        and rownum = 1;

            IF v_gcc_code IS NOT NULL
            THEN
               SELECT gcc_court_name, gcc_court_date, gcc_case_no
                 INTO v_courtname, v_courtdate, v_caseno
                 FROM gin_court_cases
                WHERE gcc_cmb_claim_no = v_claim_no AND gcc_code = v_gcc_code;
            END IF;
            
--            raise_error('v_trans_no'||v_trans_no);
           BEGIN
               SELECT CPV_REG_CLMT_CODE, CPV_APCO_COR_CODE,CPV_CLAIMANT_TYPE,CPV_NET_PAID,CUR_SYMBOL,CPV_PAYMENT_MODE,
               CPV_VOUCHER_NO
               INTO V_CLMT_CODE,V_APCO_CODE,V_CLMT_TYPE,V_AMOUNT ,V_CUR_SYMBOL,V_PAYMENTMODE,V_CPV_CODE
                FROM GIN_CLM_PAYMENT_VOUCHERS,TQC_CURRENCIES
               WHERE  CPV_GGT_TRANS_NO = V_TRANS_NO
               AND  CUR_CODE = CPV_CUR_CODE ;
           EXCEPTION WHEN OTHERS THEN 
           NULL;
           END;    
           
           IF V_PAYMENTMODE IN ('EFT','RTGS')   THEN   
           BEGIN    
               BANK_CURSOR :=
                     gin_web_claims_pkg.get_bank_details1 (v_cpv_code );
              
          
                  LOOP
                     EXIT WHEN bank_cursor%NOTFOUND;

                     FETCH bank_cursor
                      INTO v_acc_no,v_bank_name,v_branch_name,v_preffered_mode;
                  END LOOP;
            EXCEPTION WHEN OTHERS THEN         
            NULL;
           END;       
          
           END IF ;
            
              lv_temp_txt := REPLACE (lv_temp_txt, '[BANKNAME]', v_bank_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKACCOUNT]', v_acc_no);
          lv_temp_txt := REPLACE (lv_temp_txt, '[AMOUNT]', v_amount);
           lv_temp_txt := REPLACE (lv_temp_txt, '[CURRENCYSYMBOL]', v_cur_symbol);
           lv_temp_txt := replace (lv_temp_txt, '[PAYMENTMODE]', v_paymentmode);

            lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLAIMNO]', v_clm_no);
            lv_temp_txt := REPLACE (lv_temp_txt, '[SA]', v_sa);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLAIMANTNAME]', v_client);
            lv_temp_txt := REPLACE (lv_temp_txt, '[PREMIUM]', v_prem);
            lv_temp_txt := REPLACE (lv_temp_txt, '[DATE]', v_date);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOSSTIME]', v_time);
            lv_temp_txt := REPLACE (lv_temp_txt, '[REGISTRATION]', v_reg);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOCATION]', v_loc);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CASENO]', v_caseno);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CASEDATE]', v_courtdate);
            lv_temp_txt := REPLACE (lv_temp_txt, '[COURT]', v_courtname);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOSSDESC]', v_desc);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOSSTYPE]', v_loss_type);
            lv_temp_txt := REPLACE (lv_temp_txt, '[RISKDESC]', v_item_desc);
            lv_temp_txt :=
                      REPLACE (lv_temp_txt, '[CLAIMANTTYPE]', v_claimant_type);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLAIMANT]', v_client);         
         
--         EXCEPTION
--            WHEN NO_DATA_FOUND
--            THEN
--               NULL;
--            WHEN OTHERS
--            THEN
--               raise_error ('ERROR: ' || SQLERRM (SQLCODE));
         END;
      ELSIF v_app_lvl = 'L'
      THEN
         NULL;
      ELSIF v_app_lvl = 'Q'
      THEN
         IF v_pol_batch_no IS NOT NULL
         THEN
            BEGIN
               SELECT quot_no, quot_tot_property_val,
                      gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                      quot_premium, quot_cover_from, quot_cover_to,
                      quot_date, agn_name, brn_name, clnt_postal_addrs,
                      twn_name, qr_property_id, qr_item_desc, qr_wef,
                      qr_wet, qr_covt_sht_desc, qr_premium, qr_value,
                      quot_cur_symbol
                 INTO v_pol_no, v_sa,
                      v_client,
                      v_prem, vcoverfrom, vcoverto,
                      veffdate, v_agent, v_branch, v_address,
                      v_twn, v_risk_id, v_risk_desc, v_risk_wef,
                      v_risk_wet, v_cover, v_risk_prem, v_risk_value,
                      v_currency
                 FROM gin_quotations,
                      tqc_clients,
                      tqc_agencies,
                      tqc_branches,
                      tqc_towns,
                      tq_gis.gin_quot_risks
                WHERE quot_prp_code = clnt_code
                  AND qr_quot_code = quot_code
                  AND quot_agnt_agent_code = agn_code
                  AND twn_code(+) = clnt_twn_code
                  AND brn_code = quot_brn_code
                  AND quot_code = v_pol_batch_no
                  AND ROWNUM < 2;

               lv_temp_txt := REPLACE (lv_temp_txt, '[QUOTNO]', v_pol_no);
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERFROM]',
                           TO_CHAR (vcoverfrom, 'DD/Mon/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERTO]',
                           TO_CHAR (vcoverto, 'DD/Mon/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[EFFDATE]',
                           TO_CHAR (veffdate, 'DD/Mon/YYYY')
                          );
               lv_temp_txt := REPLACE (lv_temp_txt, '[SA]', v_sa);
               lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
               lv_temp_txt := REPLACE (lv_temp_txt, '[PREMIUM]', v_prem);
               lv_temp_txt := REPLACE (lv_temp_txt, '[CURRENCY]', v_currency);
               lv_temp_txt := REPLACE (lv_temp_txt, '[AGENT]', v_agent);
               lv_temp_txt := REPLACE (lv_temp_txt, '[BRANCH]', v_branch);
               lv_temp_txt := REPLACE (lv_temp_txt, '[ADDRESS]', v_address);
               lv_temp_txt := REPLACE (lv_temp_txt, '[TOWN]', v_twn);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKID]', v_risk_id);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKDESC]', v_risk_desc);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWEF]', v_risk_wef);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWET]', v_risk_wet);
               lv_temp_txt := REPLACE (lv_temp_txt, '[COVERTYPE]', v_cover);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKPREM]', v_risk_prem);
               lv_temp_txt :=
                            REPLACE (lv_temp_txt, '[RISKVALUE]', v_risk_value);
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
               WHEN OTHERS
               THEN
                  raise_error ('ERROR: ' || SQLERRM (SQLCODE));
            END;
         ELSE
            IF v_pol_no IS NOT NULL
            THEN
               lv_temp_txt := REPLACE (lv_temp_txt, '[QUOTNO]', v_pol_no);
            END IF;
         END IF;
      ELSE
         raise_error (   'Application level '
                      || NVL (v_app_lvl, 'NONE')
                      || ' not recognised..'
                     );
      END IF;

      return (LV_TEMP_TXT);
   End; 
 
/