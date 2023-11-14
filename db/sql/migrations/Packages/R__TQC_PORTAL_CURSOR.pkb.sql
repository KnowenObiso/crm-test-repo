--
-- TQC_PORTAL_CURSOR  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM."TQC_PORTAL_CURSOR"
AS
/******************************************************************************
 NAME: TQ_CRM.TQC_PORTAL_CURSOR
 PURPOSE:
getProductOption
 REVISIONS:
 Ver Date Author Description
 --------- ---------- --------------- ------------------------------------
 1.0 9/20/2012 1. Created this package body.
******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20033,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   FUNCTION getproductcategories (
      v_syscode          NUMBER,
      v_prodcode    IN   NUMBER DEFAULT NULL,
      v_agentcode   IN   NUMBER DEFAULT NULL
   )
      RETURN product_categories_ref
   IS
      v_cursor   product_categories_ref;
   BEGIN
-- RAISE_ERROR('v_prodCode: ' || v_prodCode || ' v_agentCode: ' || v_agentCode);
      IF v_prodcode IS NULL AND v_agentcode IS NULL
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT twpc_code, twpc_sys_code, twpc_name,
                            twpc_description
                       FROM tqc_web_product_categories, tqc_web_products
                      WHERE twpc_sys_code = v_syscode
                        AND twpc_code = twp_twpc_code
                   ORDER BY twpc_name;
      ELSIF v_agentcode IS NOT NULL
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT twp_prod_code, v_syscode, pro_desc,
                            twp_prod_desc
                       FROM tqc_web_products,
                            gin_products,
                            tqc_agency_products
                      WHERE pro_code = twp_prod_code(+)
                        AND agnp_prod_code = pro_code
                        AND agnp_agn_code = v_agentcode
                   ORDER BY pro_desc;
      ELSE
         OPEN v_cursor FOR
            SELECT DISTINCT twp_prod_code, v_syscode, pro_desc,
                            twp_prod_desc
                       FROM tqc_web_products, gin_products
                      WHERE pro_code = twp_prod_code
                        AND twp_prod_code = v_prodcode
                   ORDER BY pro_desc;
      END IF;

      RETURN v_cursor;
   END;

   /* FUNCTION getProducts(v_twpcCode NUMBER) RETURN products_ref IS
   v_cursor products_ref;
   BEGIN
   OPEN v_cursor FOR
   SELECT DISTINCT TWP_CODE,TWP_TWPC_CODE,TWP_PROD_CODE,
   PRO_DESC PROD_DESC,TWP_PROD_DESC
   FROM TQC_WEB_PRODUCTS,TQC_WEB_PRODUCT_CATEGORIES,GIN_PRODUCTS
   WHERE TWP_TWPC_CODE = TWPC_CODE
   AND TWP_PROD_CODE = PRO_CODE
   AND TWP_TWPC_CODE = v_twpcCode
   ORDER BY PROD_DESC;
   RETURN v_cursor;
   END;*/
   FUNCTION getproducts (v_twpccode NUMBER, v_syscode NUMBER DEFAULT NULL)
      RETURN products_ref
   IS
      v_cursor   products_ref;
   BEGIN
      IF v_syscode = 26
      THEN
         OPEN v_cursor FOR
            SELECT   twp_code, twp_twpc_code, twp_prod_code, prod_desc,
                     twp_prod_desc, NULL, NULL, NULL, NULL, NULL, NULL, NULL,twp_prod_url
                FROM tqc_web_products, lms_products
               WHERE twp_prod_code = prod_code AND twp_twpc_code = v_twpccode
            ORDER BY prod_desc;

         RETURN v_cursor;
      ELSIF v_syscode = 37
      THEN
         OPEN v_cursor FOR
            SELECT   twp_code, twp_twpc_code, twp_prod_code, pro_desc,
                     twp_prod_desc, bind_name, twp_binder, twp_bind_code,
                     agn_sht_desc, twp_agn_code, aga_code, aga_name,twp_prod_url
                FROM tqc_web_products,
                     gin_products,
                     gin_binders,
                     tqc_agencies,
                     tqc_agency_accounts
               WHERE twp_prod_code = pro_code
                 AND twp_bind_code = bind_code(+)
                 AND agn_code(+) = twp_agn_code
                 AND twp_aga_code = aga_code(+)
                 AND twp_twpc_code = v_twpccode
            ORDER BY pro_desc;

         RETURN v_cursor;
      ELSIF v_syscode IS NULL OR v_twpccode IS NULL
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT twp_code, twp_twpc_code, twp_prod_code,
                            pro_desc prod_desc, twp_prod_desc, bind_name,
                            twp_binder, twp_bind_code, agn_sht_desc,
                            twp_agn_code, aga_code, aga_name,twp_prod_url
                       FROM tqc_web_products,
                            tqc_web_product_categories,
                            gin_products,
                            gin_binders,
                            tqc_agencies,
                            tqc_agency_accounts
                      WHERE twp_prod_code = pro_code
                        AND twp_bind_code = bind_code(+)
                        AND agn_code(+) = twp_agn_code
                        AND twp_aga_code = aga_code(+)
                        AND twp_twpc_code = v_twpccode
                   ORDER BY prod_desc;

         RETURN v_cursor;
      END IF;
   END;

   /*FUNCTION getProductOption(v_prodCode NUMBER) RETURN product_options_ref IS
   v_cursor product_options_ref;
   BEGIN
   OPEN v_cursor FOR
   SELECT TWPO_CODE,TWPO_POP_CODE,TWPO_DESC,POP_DESC
   FROM TQC_WEB_PROD_OPTIONS,LMS_PROD_OPTIONS
   WHERE TWPO_POP_CODE = POP_CODE
   AND POP_PROD_CODE = v_prodCode
   ORDER BY TWPO_POP_CODE;
   RETURN v_cursor;
   END;*/
   FUNCTION getproductoption (v_prodcode NUMBER, v_syscode NUMBER DEFAULT NULL)
      RETURN product_options_ref
   IS
      v_cursor   product_options_ref;
   BEGIN
-- RAISE_ERROR('v_prodCode'||v_prodCode||'v_sysCode'||v_sysCode);
      IF v_syscode = 26
      THEN
         OPEN v_cursor FOR
            SELECT   twpo_code, twpo_pop_code, twpo_desc, pop_desc, NULL
                FROM tqc_web_prod_options, lms_prod_options
               WHERE twpo_pop_code = pop_code AND pop_prod_code = v_prodcode
            ORDER BY twpo_pop_code;

         RETURN v_cursor;
      ELSIF v_syscode = 37
      THEN
         OPEN v_cursor FOR
            SELECT   twpo_code, twpo_pop_code, twpo_desc, scl_desc,
                     bind_name
                FROM tqc_web_prod_options,
                     gin_product_sub_classes,
                     gin_sub_classes,
                     gin_binders
               WHERE twpo_pop_code = clp_code
                 AND clp_scl_code = scl_code
                 AND bind_code(+) = twpo_bind_code
                 AND clp_pro_code = v_prodcode
            ORDER BY twpo_pop_code;

         RETURN v_cursor;
      ELSIF v_prodcode IS NULL OR v_syscode IS NULL
      THEN
         OPEN v_cursor FOR
            SELECT   twpo_code, twpo_pop_code, twpo_desc, pop_desc, NULL
                FROM tqc_web_prod_options, lms_prod_options
               WHERE twpo_pop_code = pop_code AND pop_prod_code = v_prodcode
            ORDER BY twpo_pop_code;

         RETURN v_cursor;
      END IF;
   END;

   PROCEDURE getlifequoteoutput (
      v_quotecode    IN       NUMBER,
      v_totpremium   OUT      NUMBER,
      v_totsa        OUT      NUMBER
   )
   IS
   BEGIN
      SELECT tquo_tot_premium, tquo_tot_sa
        INTO v_totpremium, v_totsa
        FROM lms_tel_quotes
       WHERE tquo_code = v_quotecode;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
   END getlifequoteoutput;

   FUNCTION getdynamicvalues (v_mod VARCHAR2, v_pkid NUMBER)
      RETURN dynamic_values_ref
   IS
      v_cursor   dynamic_values_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT tsmsd_prompt, tsmsi_value, tsmsi_mode_code,
                         tsmsi_row, tsmsi_column, tsmsi_code,
                         tmsc_column_name, tmsc_table_name
                    FROM tqc_sys_mod_subunits_details,
                         tqc_sys_mod_subunits,
                         tqc_system_modules,
                         tqc_sys_mod_subunits_inputs,
                         tqc_mod_sys_columns
                   WHERE tsmsd_tsms_code = tsms_code
                     AND tsms_tsm_code = tsm_code
                     AND tsmsi_tsmsd_code = tsmsd_code
                     AND tsmsd_tmsc_code = tmsc_code
                     AND tsm_sht_desc = v_mod
                     --AND TSMS_ORDER = 1
                     AND tsmsd_parent IS NULL
                     AND tsmsi_code = v_pkid;

      RETURN v_cursor;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
   END;

   /*FUNCTION getLifeProducts RETURN sys_prod_ref IS
   v_cursor sys_prod_ref;
   BEGIN
   OPEN v_cursor FOR
   SELECT DISTINCT PROD_CODE,PROD_SHT_DESC,PROD_DESC
   FROM LMS_PRODUCTS,LMS_CLASSES,LMS_PROD_OPTIONS,LMS_PREMIUM_MASKS
   WHERE PROD_CLA_CODE = CLA_CODE
   AND POP_PROD_CODE = PROD_CODE
   AND PMAS_PROD_CODE = PROD_CODE
   AND PROD_CODE NOT IN (SELECT TWP_PROD_CODE FROM TQC_WEB_PRODUCTS)
   AND PMAS_DEFAULT = 'Y'
   AND CLA_TYPE = 'O';
   RETURN v_cursor;
   EXCEPTION WHEN OTHERS THEN
   RAISE_ERROR('Error Occured '||SQLERRM(SQLCODE));
   END;*/
   FUNCTION getlifeproducts (v_syscode IN NUMBER DEFAULT NULL)
      RETURN sys_prod_ref
   IS
      v_cursor   sys_prod_ref;
   BEGIN
      IF v_syscode = 26
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT prod_code, prod_sht_desc, prod_desc
                       FROM lms_products,
                            lms_classes,
                            lms_prod_options,
                            lms_premium_masks
                      WHERE prod_cla_code = cla_code
                        AND pop_prod_code = prod_code
                        AND pmas_prod_code = prod_code
                        AND prod_code NOT IN (SELECT twp_prod_code
                                                FROM tqc_web_products)
                        AND pmas_default = 'Y'
                        AND cla_type = 'O';

         RETURN v_cursor;
      ELSIF v_syscode = 37
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT pro_code, pro_sht_desc, pro_desc
                       FROM gin_products, gin_classes
                      WHERE pro_code NOT IN (SELECT twp_prod_code
                                               FROM tqc_web_products);

         RETURN v_cursor;
      ELSE
         OPEN v_cursor FOR
            SELECT DISTINCT prod_code, prod_sht_desc, prod_desc
                       FROM lms_products,
                            lms_classes,
                            lms_prod_options,
                            lms_premium_masks
                      WHERE prod_cla_code = cla_code
                        AND pop_prod_code = prod_code
                        AND pmas_prod_code = prod_code
                        AND prod_code NOT IN (SELECT twp_prod_code
                                                FROM tqc_web_products)
                        AND pmas_default = 'Y'
                        AND cla_type = 'O';

         RETURN v_cursor;
      END IF;
   END;

   /*FUNCTION getProdOptions(v_prodCode NUMBER)RETURN sys_prod_options_ref IS
   v_cursor sys_prod_options_ref;
   BEGIN
   OPEN v_cursor FOR
   SELECT POP_CODE,POP_SHT_DESC,POP_DESC
   FROM LMS_PROD_OPTIONS
   WHERE POP_PROD_CODE = v_prodCode;
   RETURN v_cursor;
   EXCEPTION WHEN OTHERS THEN
   RAISE_ERROR('Error Occured '||SQLERRM(SQLCODE));
   END;*/
   FUNCTION getprodoptions (v_prodcode NUMBER, v_syscode NUMBER DEFAULT NULL)
      RETURN sys_prod_options_ref
   IS
      v_cursor   sys_prod_options_ref;
   BEGIN
      IF v_syscode = 26
      THEN
         OPEN v_cursor FOR
            SELECT pop_code, pop_sht_desc, pop_desc
              FROM lms_prod_options
             WHERE pop_prod_code = v_prodcode;

         RETURN v_cursor;
      ELSIF v_syscode = 37
      THEN
         OPEN v_cursor FOR
            SELECT clp_code, clp_scl_code scl_sht_desc, scl_desc
              FROM gin_product_sub_classes, gin_sub_classes
             WHERE clp_scl_code = scl_code AND clp_pro_code = v_prodcode;

         RETURN v_cursor;
      ELSE
         OPEN v_cursor FOR
            SELECT pop_code, pop_sht_desc, pop_desc
              FROM lms_prod_options
             WHERE pop_prod_code = v_prodcode;

         RETURN v_cursor;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
   END;

--   FUNCTION getclientportfolio (v_clientcode NUMBER, v_usercode NUMBER)
--      RETURN portfolio_ref
--   IS
--      v_cursor   portfolio_ref;
--   BEGIN
--      --RAISE_ERROR(' v_clientCode '||v_clientCode);
--      OPEN v_cursor FOR
--         SELECT   pol_batch_no pol_code_batch_no, 37 sys_code,
--                  pol_policy_no policy_number, pol_quot_no prop_quote_no,
--                  pro_desc prod_desc, pol_policy_cover_from cover_from,
--                  pol_policy_cover_to cover_to,
--                  pol_total_sum_insured amount_insured,
--                  pol_current_status status, pol_prepared_by done_by,
--                  pol_inception_dt pol_inception_dt, NULL quot_ok,
--                  NULL prod_type, pol_tot_endos_diff_amt, pol_nett_premium,pro_code
--             FROM gin_policies, gin_products
--            WHERE pol_pro_code = pro_code
--              AND pol_prp_code = v_clientcode
--              AND (   pol_tcb_code IN (SELECT tcub_tcb_code
--                                         FROM tqc_client_usr_branches
--                                        WHERE tcub_acwa_code = v_usercode)
--                   OR pol_tcb_code IS NULL
--                  )
--              AND pol_current_status IN ('A')
--              ORDER BY 3;
----         UNION
----         SELECT   quot_code pol_code_batch_no, 37 sys_code,
----                  NULL policy_number, quot_no prop_quote_no,
----                  pro_desc prod_desc, qp_wef_date cover_from,
----                  qp_wet_date cover_to, qp_total_si amount_insured,
----                  quot_status status, quot_prepared_by done_by,
----                  quot_date pol_inception_dt, quot_loan_disbursed quot_ok,
----                  NULL prod_type, quot_premium, quot_premium
----             FROM gin_quotations, gin_quot_products, gin_products
----            WHERE quot_code = qp_quot_code
----              AND qp_pro_code = pro_code
----              AND quot_prp_code = v_clientcode
----              AND quot_status != 'Confirmed'
--         /*AND (QUOT_TCB_CODE IN (SELECT TCUB_TCB_CODE FROM TQC_CLIENT_USR_BRANCHES
--         WHERE TCUB_ACWA_CODE = v_userCode) OR QUOT_TCB_CODE IS NULL)*/
----         UNION
----         SELECT   pol_code, 27, pol_policy_no, pol_proposal_no, prod_desc,
----                  pol_effective_date, pol_maturity_date, endr_tot_sa,
----                  pol_status, pol_prepared_by, NULL initiated_date, NULL,
----                  prod_type, NULL, NULL
----             FROM lms_policies,
----                  lms_policy_endorsements,
----                  lms_proposers,
----                  lms_classes,
----                  lms_products
----            WHERE endr_code = pol_current_endr_code
----              AND pol_status IN ('D', 'A', 'R', 'V')
----              AND pol_client_prp_code = prp_code
----              AND pol_cla_code = cla_code
----              AND pol_prod_code = prod_code
----              AND prp_clnt_code = v_clientcode
----              AND cla_type = 'O'
----         ORDER BY 3 DESC;

--      RETURN v_cursor;
--   EXCEPTION
--      WHEN OTHERS
--      THEN
--         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
--   END;
FUNCTION getclientportfolio (
    v_clientcode NUMBER,
    v_usercode NUMBER, 
    v_page_no NUMBER default 1, 
    v_page_sz NUMBER default 100
    )
      RETURN sys_refcursor
   IS
      v_cursor   sys_refcursor;
   BEGIN
      --RAISE_ERROR(' v_clientCode '||v_clientCode);
      OPEN v_cursor FOR
        SELECT * FROM (SELECT a.*, ROWNUM x FROM ( SELECT   pol_batch_no pol_code_batch_no, 37 sys_code,
                  pol_policy_no policy_number, pol_quot_no prop_quote_no,
                  pro_desc prod_desc, pol_policy_cover_from cover_from,
                  pol_policy_cover_to cover_to,
                  pol_total_sum_insured amount_insured,
                  pol_current_status status, pol_prepared_by done_by,
                  pol_inception_dt pol_inception_dt, NULL quot_ok,
                  NULL prod_type, pol_tot_endos_diff_amt, pol_nett_premium,pro_code,
                  NULL screen_code
             FROM gin_policies, gin_products
            WHERE pol_pro_code = pro_code
              AND pol_prp_code = v_clientcode
              AND pol_current_status IN ('A')
              ORDER BY pol_prepared_date desc) a WHERE ROWNUM <= (v_page_no * v_page_sz)) 
              WHERE x >= ((v_page_no - 1) * v_page_sz + 1);
--         UNION
--         SELECT   quot_code pol_code_batch_no, 37 sys_code,
--                  NULL policy_number, quot_no prop_quote_no,
--                  pro_desc prod_desc, qp_wef_date cover_from,
--                  qp_wet_date cover_to, qp_total_si amount_insured,
--                  quot_status status, quot_prepared_by done_by,
--                  quot_date pol_inception_dt, quot_loan_disbursed quot_ok,
--                  NULL prod_type, quot_premium, quot_premium
--             FROM gin_quotations, gin_quot_products, gin_products
--            WHERE quot_code = qp_quot_code
--              AND qp_pro_code = pro_code
--              AND quot_prp_code = v_clientcode
--              AND quot_status != 'Confirmed'
         /*AND (QUOT_TCB_CODE IN (SELECT TCUB_TCB_CODE FROM TQC_CLIENT_USR_BRANCHES
         WHERE TCUB_ACWA_CODE = v_userCode) OR QUOT_TCB_CODE IS NULL)*/
--         UNION
--         SELECT   pol_code, 27, pol_policy_no, pol_proposal_no, prod_desc,
--                  pol_effective_date, pol_maturity_date, endr_tot_sa,
--                  pol_status, pol_prepared_by, NULL initiated_date, NULL,
--                  prod_type, NULL, NULL
--             FROM lms_policies,
--                  lms_policy_endorsements,
--                  lms_proposers,
--                  lms_classes,
--                  lms_products
--            WHERE endr_code = pol_current_endr_code
--              AND pol_status IN ('D', 'A', 'R', 'V')
--              AND pol_client_prp_code = prp_code
--              AND pol_cla_code = cla_code
--              AND pol_prod_code = prod_code
--              AND prp_clnt_code = v_clientcode
--              AND cla_type = 'O'
--         ORDER BY 3 DESC;

      RETURN v_cursor;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
   END;

   FUNCTION getclientquotations (
    v_clientcode NUMBER,
    v_usercode NUMBER, 
    v_page_no NUMBER default 1, 
    v_page_sz NUMBER default 100
   )
      RETURN sys_refcursor
   IS
      v_cursor   sys_refcursor;
   BEGIN
      --RAISE_ERROR(' v_clientCode '||v_clientCode);
      OPEN v_cursor FOR
    SELECT * FROM (SELECT a.*, ROWNUM r__ FROM ( 
         SELECT distinct   
                   q.quot_code pol_code_batch_no, 
                  37 sys_code,
                   NULL policy_number,
                   q.quot_no prop_quote_no,
                  p.pro_desc prod_desc, 
                  qp_wef_date cover_from,
                  qp_wet_date cover_to, 
                  qp_total_si amount_insured,
                  q.quot_status status, 
                  q.quot_prepared_by done_by,
                  q.quot_date pol_inception_dt, 
                  q.quot_loan_disbursed quot_ok,
                  NULL prod_type, 
                  (taxes.qpt_tax_amt + quot_premium) premium, 
                  (taxes.qpt_tax_amt + quot_premium) bal,
                  p.pro_code,
                  s.scl_unwr_scr_code
             FROM gin_quotations q, gin_quot_products ,gin_products p,gin_product_sub_classes c,gin_sub_classes s,
              (SELECT   SUM (qpt_tax_amt) qpt_tax_amt,
                                   qpt_quot_code
                              FROM gin_quot_product_taxes
                          GROUP BY qpt_quot_code) taxes
            
            WHERE q.quot_code = qp_quot_code
             and p.pro_code =c.clp_pro_code
              AND qp_pro_code = pro_code
              AND quot_prp_code = v_clientcode
              and clp_scl_code= scl_code
              AND  q.quot_code=taxes.qpt_quot_code(+)
              ORDER BY quot_date DESC 
              
              ) a WHERE ROWNUM <= (v_page_no * v_page_sz)) 
              WHERE r__ >= ((v_page_no - 1) * v_page_sz + 1);
      RETURN v_cursor;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
   END;

  FUNCTION getClientClaims (v_clientCode NUMBER, v_userCode NUMBER)
      RETURN claims_ref
   IS
      v_cursor   claims_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT
                wcb_policy_no,
                wcb_ipu_property_id,
                wcb_loss_date,
                wcb_date_notified,
                wcb_description,
                wcb_gis_claim_no,
                DECODE (wcb_gis_status, 'B', 'BOOKED', 'PENDING') status,
                wcb_code AS claim_ref_no,
                pol_pro_code AS pro_code
           FROM gin_web_claims_bookings, gin_policies
          WHERE     wcb_prp_code = v_clientcode
                AND wcb_gis_status = 'P'
                AND wcb_policy_no = pol_policy_no
                AND (pol_tcb_code IN (SELECT tcub_tcb_code
                                        FROM tqc_client_usr_branches
                                       WHERE tcub_acwa_code = v_usercode)
                     OR pol_tcb_code IS NULL)
         UNION
         SELECT DISTINCT
                cmb_pol_policy_no,
                cmb_ipu_property_id,
                cmb_loss_date_time,
                cmb_claim_date,
                cmb_loss_desc,
                cmb_claim_no,
                DECODE (cmb_claim_status,
                        'B', 'BOOKED',
                        'P', 'PENDING',
                        'J', 'REJECTED',
                        'N', 'CLOSED(NO CLAIM)',
                        'S', 'CLOSED(SETTLED)',
                        'R', 'REOPENED')
                   status,
                TO_NUMBER (cmb_claim_no) AS claim_ref_no,
                pol_pro_code AS pro_code
           FROM gin_claim_master_bookings, gin_policies
          WHERE     cmb_prp_code = v_clientcode
                AND cmb_pol_policy_no = pol_policy_no
                AND (pol_tcb_code IN (SELECT tcub_tcb_code
                                        FROM tqc_client_usr_branches
                                       WHERE tcub_acwa_code = v_usercode)
                     OR pol_tcb_code IS NULL)
         UNION
         SELECT endr_pol_policy_no,
                prp_surname || ' ' || prp_other_names,
                NVL (cnot_death_date, cnot_disability_date)
                   clm_date_death_accident,
                cnot_notification_date clm_date_claim_reported,
                caus_desc,
                ltr_clm_no,
                DECODE (clm_status,
                        NULL, 'PENDING',
                        'P', 'DRAFT',
                        'CN', 'CLOSED(NO CLAIM)',
                        'CS', 'CLOSED(SETTLED)',
                        'OS', 'OUTSTANDING')
                   status,
                cnot_code AS claim_ref_no,
                endr_prod_code AS pro_code
           FROM lms_claim_notifications,
                lms_proposers,
                lms_transactions,
                lms_causations,
                lms_policy_endorsements,
                lms_claims
          WHERE     prp_code = cnot_client_prp_code
                AND cnot_code = ltr_cnot_code
                AND ltr_endr_code = endr_code
                AND cnot_clm_no = clm_no(+)
                AND prp_clnt_code = v_clientcode
                AND caus_code = cnot_caus_code;

      RETURN v_cursor;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
   END;

   FUNCTION getsystems
      RETURN systems_ref
   IS
      v_cursor   systems_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT sys_code, sys_name
           FROM tqc_systems
          WHERE sys_code IN (26, 37);

      RETURN v_cursor;
   END;

   FUNCTION getclientallopenrequests (v_clientcode NUMBER, v_usercode NUMBER)
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                tsr_owner_code, tsr_status, tsr_due_date,
                tsr_resolution_date, tsr_summary, tsr_description,
                tsr_solution, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code(+)
            AND tsr_owner_code = uown.usr_code(+)
            AND tsr_acc_type = 'C'
            AND NVL (tsr_acc_code, v_clientcode) = v_clientcode
            AND (   tsr_tcb_code IN (SELECT tcub_tcb_code
                                       FROM tqc_client_usr_branches
                                      WHERE tcub_acwa_code = v_usercode)
                 OR tsr_tcb_code IS NULL
                )
            AND tsr_status = 'Open';

      RETURN v_cursor;
   END;

   FUNCTION getprodsettings (
      v_quotecode    NUMBER,
      v_syscode      NUMBER,
      v_clientcode   NUMBER
   )
      RETURN web_prod_settings_ref
   IS
      v_cursor   web_prod_settings_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT twpd_clnt_code, twpd_usr_code, twpd_username, twpd_dr_limit,
                twpd_cr_limit,
                CASE
                   WHEN NVL (twpd_dr_limit, 0) > qp_total_si
                      THEN 'Y'
                   ELSE 'N'
                END AS auth
           FROM tqc_web_product_details,
                tqc_web_products,
                tqc_web_product_categories,
                gin_quot_products
          WHERE twpd_twp_code = twp_code
            AND twp_twpc_code = twpc_code
            AND twp_prod_code = qp_pro_code
            AND qp_quot_code = v_quotecode
            AND twpc_sys_code = v_syscode
            AND twpd_clnt_code = v_clientcode;

      RETURN v_cursor;
   END;

   FUNCTION getsystemactive (v_syscode NUMBER)
      RETURN VARCHAR2
   IS
      v_active   VARCHAR2 (1) := 'N';
   BEGIN
      SELECT sys_active
        INTO v_active
        FROM tqc_systems
       WHERE sys_code = v_syscode;

      RETURN v_active;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN v_active;
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
   END;

   PROCEDURE lovvehiclemodels (
      v_models     OUT      vehiclemodel_dtls,
      v_vmk_code   IN       NUMBER
   )
   IS
   BEGIN
      OPEN v_models FOR
         SELECT vmod_code, vmod_name
           FROM gin_vehicle_model
          WHERE vmod_vmk_code = v_vmk_code;
   END lovvehiclemodels;

   PROCEDURE lovvehiclemakes (v_makes OUT vehiclemake_dtls)
   IS
   BEGIN
      OPEN v_makes FOR
         SELECT vmk_code, vmk_name
           FROM gin_vehicle_make;
   END lovvehiclemakes;

   PROCEDURE updateuserdetails (
      v_clnt_code        NUMBER,
      v_postal_addrs     VARCHAR2,
      v_prp_id_reg_no    VARCHAR2,
      v_physical_addrs   VARCHAR2,
      v_othernames       VARCHAR2,
      v_name             VARCHAR2,
      v_title            VARCHAR2,
      v_dob              DATE,
      v_email_addrs      VARCHAR2,
      v_tel              VARCHAR2,
      v_pin              VARCHAR2
   )
   IS
   BEGIN
      IF v_clnt_code IS NOT NULL
      THEN
         UPDATE tqc_clients
            SET clnt_postal_addrs = NVL (v_postal_addrs, clnt_postal_addrs),
                clnt_id_reg_no = NVL (v_prp_id_reg_no, clnt_id_reg_no),
                clnt_physical_addrs =
                                   NVL (v_physical_addrs, clnt_physical_addrs),
                clnt_other_names = NVL (v_othernames, clnt_other_names),
                clnt_surname = NVL (v_name, clnt_surname),
                clnt_title = NVL (v_title, clnt_title),
                clnt_dob = NVL (v_dob, clnt_dob),
                clnt_email_addrs = NVL (v_email_addrs, clnt_email_addrs),
                clnt_tel = NVL (v_tel, clnt_tel),
                clnt_pin = NVL (v_pin, clnt_pin)
          WHERE clnt_code = v_clnt_code;
--            UPDATE LMS_PROPOSERS SET
--                PRP_POSTAL_ADDRESS = NVL(v_postal_addrs, PRP_POSTAL_ADDRESS),
--                PRP_ID_REG_NO = NVL(v_prp_id_reg_no, PRP_ID_REG_NO),
--                PRP_PHYSICAL_ADDR =  NVL(v_physical_addrs, PRP_PHYSICAL_ADDR),
--                PRP_OTHER_NAMES =  NVL(v_othernames, PRP_OTHER_NAMES),
--                PRP_SURNAME = NVL(v_surname, PRP_SURNAME),
--                PRP_TITLE = NVL(v_title,PRP_TITLE),
--                PRP_DOB = NVL(v_dob,PRP_DOB),
--                PRP_EMAIL = NVL(v_email_addrs,PRP_EMAIL),
--                PRP_TEL = NVL(v_tel,PRP_TEL)
--                WHERE   PRP_CLNT_CODE = v_clnt_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating Details');
   END;
FUNCTION get_client_claim (v_clientcode     NUMBER,
                              v_usercode       NUMBER,
                              v_claim_no       NUMBER,
                              v_claimref_no    NUMBER)
      RETURN claims_ref
   IS
      v_cursor   claims_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT
                wcb_policy_no,
                wcb_ipu_property_id,
                wcb_loss_date,
                wcb_date_notified,
                wcb_description,
                wcb_gis_claim_no,
                DECODE (wcb_gis_status, 'B', 'BOOKED', 'PENDING') status,
                wcb_code AS claim_ref_no,
                pol_pro_code AS pro_code
           FROM gin_web_claims_bookings, gin_policies
          WHERE     wcb_prp_code = v_clientcode
                AND wcb_gis_status = 'P'
                AND wcb_policy_no = pol_policy_no
                AND (pol_tcb_code IN (SELECT tcub_tcb_code
                                        FROM tqc_client_usr_branches
                                       WHERE tcub_acwa_code = v_usercode)
                     OR pol_tcb_code IS NULL)
                AND wcb_code = v_claimref_no
         UNION
         SELECT DISTINCT
                cmb_pol_policy_no,
                cmb_ipu_property_id,
                cmb_loss_date_time,
                cmb_claim_date,
                cmb_loss_desc,
                cmb_claim_no,
                DECODE (cmb_claim_status,
                        'B', 'BOOKED',
                        'P', 'PENDING',
                        'J', 'REJECTED',
                        'N', 'CLOSED(NO CLAIM)',
                        'S', 'CLOSED(SETTLED)',
                        'R', 'REOPENED')
                   status,
                TO_NUMBER (cmb_claim_no) AS claim_ref_no,
                pol_pro_code AS pro_code
           FROM gin_claim_master_bookings, gin_policies
          WHERE     cmb_prp_code = v_clientcode
                AND cmb_pol_policy_no = pol_policy_no
                AND (pol_tcb_code IN (SELECT tcub_tcb_code
                                        FROM tqc_client_usr_branches
                                       WHERE tcub_acwa_code = v_usercode)
                     OR pol_tcb_code IS NULL)
                AND cmb_claim_no = v_claim_no;

      RETURN v_cursor;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
   END; 
   
   FUNCTION get_client_open_requests_count(v_clientCode NUMBER, v_userCode NUMBER)
      RETURN NUMBER
   IS
      v_cnt  NUMBER;
   BEGIN
      --raise_error('v_clientCode: '|| v_clientCode ||'v_userCode: '|| v_userCode);
       SELECT COUNT(1) INTO v_cnt
           FROM TQC_SERV_REQUESTS,
                TQC_SERV_REQ_CAT,
                TQC_USERS ASS,
                TQC_CLIENTS,
                TQC_USERS UOWN
          WHERE     TSR_TSRC_CODE = TSRC_CODE
                AND TSR_ASSIGNEE = ASS.USR_CODE
                AND TSR_ACC_CODE = CLNT_CODE(+)
                AND TSR_OWNER_CODE = UOWN.USR_CODE(+)
                AND TSR_ACC_TYPE = 'C'
                AND TSR_ACC_CODE = v_clientCode
                --AND (TSR_TCB_CODE IN (SELECT TCUB_TCB_CODE FROM TQC_CLIENT_USR_BRANCHES
                --WHERE TCUB_ACWA_CODE = v_userCode) OR TSR_TCB_CODE IS NULL)
                AND TSR_STATUS IN('Open','Closed')
                AND TSR_SOLUTION IS NOT NULL      
                ;
      RETURN v_cnt;
   END;  
FUNCTION get_banks 
      RETURN sys_refcursor
   IS
      v_rc   sys_refcursor;
   BEGIN
      OPEN v_rc FOR 
         SELECT  distinct bnk_bank_name, bnk_code
             FROM tqc_banks
         ORDER BY tqc_banks.bnk_bank_name ASC;
 
      RETURN v_rc;
   END; 
FUNCTION get_binders (v_scl_code IN gin_sub_classes.scl_code%TYPE,v_covt_code IN gin_cover_types.covt_code%TYPE)
      RETURN sys_refcursor
   IS
      v_rc   sys_refcursor;
   BEGIN
      OPEN v_rc FOR
         SELECT DISTINCT scl_code, scl_desc, bind_remarks, bind_name,
                         bind_code,scl_unwr_scr_code
                    FROM gin_subcl_covt_sections,
                         gin_sub_classes,
                         gin_cover_types, 
                         gin_binders
                   WHERE scvts_covt_code = covt_code(+)
                     AND scvts_scl_code = scl_code
                     AND scl_code = bind_scl_code
                     AND scvts_covt_code = NVL(v_covt_code,scvts_covt_code)
                     AND scvts_scl_code =  NVL(v_scl_code,scvts_scl_code);

      RETURN v_rc;
   END; 
FUNCTION get_currencies( 
       v_base_cur_code out number, 
       v_base_cur_symbol out varchar2,
       v_base_cur_rnd out number 
   )
      RETURN sys_refcursor
   IS
      v_rc   sys_refcursor;
   BEGIN
        v_base_cur_code := tqc_interfaces_pkg.orgcurrency (37, v_base_cur_symbol);
      
        SELECT NVL (cur_rnd, 0)
        INTO v_base_cur_rnd
        FROM tqc_currencies
       WHERE cur_code = v_base_cur_code;
       
      OPEN v_rc FOR
         SELECT  
          cur_code,
          cur_symbol, 
          cur_desc,
           GET_EXCHANGE_RATE(v_base_cur_code,cur_code,sysdate) 
           crt_rate, cur_rnd
        FROM tqc_currencies
        WHERE NVL(CUR_WEB_DEFAULT,'N') ='Y'; 
      RETURN v_rc;
   END get_currencies;   
FUNCTION get_section_type (v_sect_code IN gin_sub_classes.scl_code%TYPE)
      RETURN sys_refcursor
   IS
      v_rc   sys_refcursor;
   BEGIN
      OPEN v_rc FOR
          SELECT sect_code, sect_sht_desc, sect_desc, sect_type, sect_web_desc
                     FROM gin_sections
          WHERE sect_code=v_sect_code; 
      RETURN v_rc;
   END; 
FUNCTION get_subclass (v_scl_code IN NUMBER DEFAULT NULL)
      RETURN sys_refcursor
   IS
      v_rc   sys_refcursor;
   BEGIN
      OPEN v_rc FOR
         SELECT DISTINCT pro_code, pro_desc, scl_code, scl_desc, scl_unwr_scr_code,scl_narrative
                    FROM gin_product_sub_classes,gin_sub_classes,gin_products 
                   WHERE  clp_pro_code = pro_code
                   AND Clp_scl_code=scl_code
                     AND scl_code = NVL(v_scl_code,scl_code)
                     and pro_web_show='Y';
       RETURN v_rc;
   END; 
FUNCTION getAgentProducts(v_agnCode    NUMBER,v_sysCode NUMBER DEFAULT NULL)RETURN agn_products_ref IS
    v_cursor agn_products_ref;
  BEGIN
   --raise_error('v_agnCode: '||v_agnCode);
  IF v_sysCode=26
   THEN
    OPEN v_cursor FOR
    SELECT   agnp_agn_code, agnp_code, prod_code,prod_desc, 26 sys_code ,'LIFE INSURANCE' sys_desc
                           FROM  tqc_agency_products,lms_products
                            WHERE agnp_prod_code = prod_code
                                   AND  agnp_agn_code= v_agnCode;
    RETURN v_cursor;
  ELSIF v_sysCode=37
    THEN
     OPEN v_cursor FOR
      SELECT  agnp_agn_code, agnp_code, pro_code,pro_desc, 37 sys_code, 'GENERAL INSURANCE' sys_desc 
                      FROM tqc_agency_products,gin_products 
                            WHERE agnp_prod_code = pro_sht_desc 
                                      AND pro_web_show = 'Y'  
                         AND  agnp_agn_code=v_agnCode;
    RETURN v_cursor;
  END IF;
  EXCEPTION WHEN OTHERS THEN
    raise_error('Error Occured '||SQLERRM(SQLCODE));
  END; 
FUNCTION get_covertypes (v_scl_code IN gin_sub_classes.scl_code%TYPE)
      RETURN sys_refcursor
   IS
      v_rc   sys_refcursor;
   BEGIN
      OPEN v_rc FOR
         SELECT DISTINCT scl_code, scl_desc, covt_code, covt_desc,scl_unwr_scr_code
                    FROM gin_subclass_cover_types,
                         gin_sub_classes,
                         gin_cover_types 
                   WHERE sclcovt_covt_code = covt_code(+)
                     AND sclcovt_scl_code = scl_code(+) 
                     and sclcovt_web_show='Y'
                     AND scl_code = v_scl_code;

      RETURN v_rc;
   END; 
FUNCTION get_security_questions
      RETURN sys_refcursor
   IS
      v_rc   sys_refcursor;
   BEGIN
      OPEN v_rc FOR
         Select secq_code, secq_sht_desc, secq_desc
        from tqc_security_questions;
      RETURN v_rc;
   END;
FUNCTION get_ports(v_country_iso3 IN VARCHAR2 ,v_type IN VARCHAR2 )
      RETURN ports_ref
   IS
      v_rc   ports_ref;
   BEGIN
    ---RAISE_ERROR(v_cou_sht_desc||'.... '||v_type);
      OPEN v_rc FOR
         SELECT  port_code, port_country_iso3, port_name, port_country_id, port_type
             FROM tqc_ports
          WHERE TRIM(UPPER(port_country_iso3))=TRIM(UPPER(v_country_iso3))
          AND TRIM(UPPER(port_type))=TRIM(UPPER(v_type))
         ORDER BY port_name ASC;
      RETURN v_rc;
   END get_ports;   
FUNCTION get_pol_risks (V_Pol_Batch_No IN gin_sub_classes.scl_code%TYPE)
      RETURN sys_refcursor
   IS
      v_rc   sys_refcursor;
   BEGIN
      OPEN v_rc FOR
         Select Gin_Insured_Property_Unds.*,scl_desc ipu_scl_desc
        from Gin_Insured_Property_Unds,Gin_Sub_Classes
        Where Ipu_Sec_Scl_Code=Scl_Code(+) 
        and Ipu_Pol_Batch_No=V_Pol_Batch_No; 
      RETURN v_rc;
   END;
FUNCTION get_countries
      RETURN country_ref
   IS
      v_retcur   country_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT  id, iso3, initcap(name), null admin_reg_type
             FROM country
         ORDER BY name;

      RETURN (v_retcur);
   END;                                
END tqc_portal_cursor; 
/