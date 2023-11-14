--
-- DIRECT_DEBIT_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.direct_debit_pkg
IS
   TYPE dr_db IS RECORD (
      sys_code           NUMBER (15),
      pol_code           NUMBER (15),
      ppr_code           NUMBER (15),
      prp_code           NUMBER (15),
      clnt_code          NUMBER (15),
      clnt_sht_desc      VARCHAR2 (20),
      clnt_name          VARCHAR2 (60),
      clnt_bank_acc_no   VARCHAR2 (30),
      clnt_bbr_code      NUMBER (15),
      bbr_bnk_code       NUMBER (15),
      bbr_branch_name    VARCHAR2 (30),
      bbr_sht_desc       VARCHAR2 (30),
      bbr_ref_code       VARCHAR2 (20),
      pol_type           VARCHAR2 (5),
      proposal_no        VARCHAR2 (20),
      policy_no          VARCHAR2 (20),
      prem_amt           NUMBER (20, 5),
      remarks            VARCHAR2 (200),
      prev_ddh_code      NUMBER,
      book_date          DATE,
      account_holder     VARCHAR2 (60),
      pay_type           VARCHAR2 (5),
      loan_no            VARCHAR2 (20)
   );

   TYPE dr_debit IS REF CURSOR
      RETURN dr_db;

   TYPE dr_db_table IS TABLE OF dr_db
      INDEX BY BINARY_INTEGER;

   FUNCTION get_tot_pol_prem (v_pol_code NUMBER)
      RETURN NUMBER;

   FUNCTION get_business_date (start_date DATE, days2add NUMBER)
      RETURN DATE;

   PROCEDURE get_direct_debit_recs (
      dr_dbt       IN OUT   dr_debit,
      v_sys_code            NUMBER,
      v_date                DATE,
      v_bnk_code            NUMBER,
      v_instal_day          NUMBER
   );

PROCEDURE Create_direct_debit_recs(v_sys_code NUMBER,
                                     v_bnk_code NUMBER,
                                     v_acc_no   VARCHAR2,
                                     v_user     VARCHAR2,
                                     v_date     DATE,
                                     v_instal_day NUMBER);

--   PROCEDURE create_direct_debit_recslms (
--      v_sys_code     NUMBER,
--      v_bbr_code     NUMBER,
--      v_acc_no       VARCHAR2,
--      v_user         VARCHAR2,
--      v_date         DATE,
--      v_instal_day   NUMBER
--   );

   PROCEDURE create_direct_debit_recsgis (
      v_sys_code     NUMBER,
      v_bnk_code     NUMBER,
      v_acc_no       VARCHAR2,
      v_user         VARCHAR2,
      v_date         DATE,
      v_instal_day   NUMBER
   );

   PROCEDURE update_bo_date (v_dd_code NUMBER);

   PROCEDURE delete_dd_detail (v_ddd_code NUMBER);

   PROCEDURE generate_receipts (v_dd_code NUMBER, v_username VARCHAR2);

   PROCEDURE generate_fixed_holidays (v_year VARCHAR2);

   PROCEDURE validate_working_day (v_date DATE);

   FUNCTION forwarding_bnk_code (v_bnk_code NUMBER)
      RETURN NUMBER;

   FUNCTION forwarding_bnk_bbr_code (v_bnk_bbr_code NUMBER)
      RETURN NUMBER;

   FUNCTION get_dd_report_format (v_fwd_bnk_code NUMBER)
      RETURN VARCHAR2;

   FUNCTION check_if_dd_generated (
      v_clnt_code   NUMBER,
      v_bbr_code    NUMBER,
      v_date        DATE,
      v_pol_code    NUMBER,
      v_ppr_code    NUMBER,
      v_ddd_type    VARCHAR2
   )
      RETURN VARCHAR2;

   FUNCTION validate_debit (
      v_bo_date        DATE,
      v_rcpt_date      DATE,
      v_paid_to_date   DATE DEFAULT NULL
   )
      RETURN VARCHAR2;

   FUNCTION func_unpaid_periods (v_pol_code NUMBER, v_date DATE)
      RETURN NUMBER;
END direct_debit_pkg; 
/