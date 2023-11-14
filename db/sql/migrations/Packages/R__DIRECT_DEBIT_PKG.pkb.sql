--
-- DIRECT_DEBIT_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.direct_debit_pkg
AS
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

   FUNCTION get_tot_pol_prem (v_pol_code NUMBER)
      RETURN NUMBER
   IS
      CURSOR pol
      IS
         SELECT pol_grp_life_rider, pol_instlmt_prem, pol_pens_instlmt_prem,
                pol_prod_type, pol_type, pol_status, endr_tot_premium,
                popns_employee_contr, pol_code
           FROM lms_policies,
                lms_policy_endorsements,
                lms_pol_ord_active_pensions,
                lms_pol_ord_pension_dtls
          WHERE pol_current_endr_code = endr_code
            AND poapn_endr_code(+) = endr_code
            AND poapn_popns_code = popns_code(+)
            AND pol_code = v_pol_code;

      v_tot_prem   NUMBER;
   BEGIN
      FOR p IN pol
      LOOP
         IF p.pol_prod_type IN ('IN', 'PN')
         THEN
            IF    p.pol_type = 'PR'
               OR (p.pol_status = 'V' AND p.pol_type = 'PL')
            THEN
               v_tot_prem := p.popns_employee_contr;
            ELSE
               IF NVL (p.pol_grp_life_rider, 'N') = 'N'
               THEN
                  v_tot_prem := NVL (p.pol_pens_instlmt_prem, 0);
               ELSE
                  v_tot_prem :=
                       NVL (p.pol_pens_instlmt_prem, 0)
                     + NVL (p.pol_instlmt_prem, 0);
               END IF;
            END IF;
         ELSE
            IF    p.pol_type = 'PR'
               OR (p.pol_status = 'V' AND p.pol_type = 'PL')
            THEN
               v_tot_prem := p.endr_tot_premium;
            ELSE
               v_tot_prem := NVL (p.pol_instlmt_prem, 0);
            END IF;
         END IF;
      END LOOP;

      RETURN NVL (v_tot_prem, 0);
   END get_tot_pol_prem;

   FUNCTION get_business_date (start_date DATE, days2add NUMBER)
      RETURN DATE
   IS
      counter    NUMBER := 0;
      curdate    DATE   := start_date;
      daynum     NUMBER;
      skipcntr   NUMBER := 0;
      v_cnt      NUMBER;
   BEGIN
      WHILE counter < days2add
      LOOP
         curdate := curdate + 1;
         daynum := TO_CHAR (curdate, 'D');

         BEGIN
            v_cnt := 0;

            SELECT COUNT (1)
              INTO v_cnt
              FROM tqc_holidays
             WHERE TRUNC (hld_date) = TRUNC (curdate);
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_cnt := 0;
         END;

         IF daynum BETWEEN 2 AND 6 AND v_cnt = 0
         THEN
            counter := counter + 1;
         ELSE
            skipcntr := skipcntr + 1;
         END IF;
      END LOOP;

      RETURN start_date + counter + skipcntr;
   END get_business_date;

   FUNCTION validate_debit (
      v_bo_date        DATE,
      v_rcpt_date      DATE,
      v_paid_to_date   DATE DEFAULT NULL
   )
      RETURN VARCHAR2
   IS
      v_valid             VARCHAR2 (1)                      := 'N';
      v_date1             NUMBER;
      v_date2             NUMBER;
      v_day_month_param   tqc_parameters.param_value%TYPE;
      v_fut_debits        tqc_parameters.param_value%TYPE;
      v_date3             DATE;
   BEGIN
      v_day_month_param :=
            tqc_parameters_pkg.get_param_varchar ('USE_BOOKING_DAY_OR_MONTH');
      v_fut_debits :=
           tqc_parameters_pkg.get_param_varchar ('DD_GENERATE_FUTURE_DEBITS');
      v_date1 :=
          TO_NUMBER (TO_CHAR (get_business_date ((v_bo_date - 1), 1), 'DD'));
      v_date2 :=
         TO_NUMBER (TO_CHAR (get_business_date ((v_rcpt_date - 1), 1), 'DD'));

        --RAISE_ERROR( v_bo_date ||'  '||v_date1||'v_day='||v_date2 ||'  '|| v_rcpt_date);
      --raise_error( v_bo_date  ||'==='|| v_rcpt_date   ||'==v_day_month_param='||v_day_month_param ||'==='|| Get_business_date((v_bo_date - 1),1)  ||'==='|| Get_business_date((v_rcpt_date - 1),1)  );
      IF NVL (v_fut_debits, 'N') = 'Y'
      THEN
         IF v_bo_date > v_rcpt_date
         THEN
             v_valid := 'N';
         ELSE
             v_valid := 'Y';
         END IF;
      ELSIF NVL (v_day_month_param, 'M') = 'D'
      THEN
         IF get_business_date ((v_bo_date - 1), 1) =
                                    get_business_date ((v_rcpt_date - 1), 1)
         THEN
            v_valid := 'Y';
         END IF;

         IF TO_CHAR (v_bo_date, 'DD') >
                            TO_NUMBER (TO_CHAR (LAST_DAY (v_rcpt_date), 'DD'))
         THEN
            v_date3 :=
               TO_DATE (   TO_CHAR (LAST_DAY (v_rcpt_date), 'DD')
                        || TO_CHAR (v_rcpt_date, '/MM/RRRR'),
                        'DD/MM/RRRR'
                       );
         ELSE
            v_date3 :=
               TO_DATE (   TO_CHAR (v_bo_date, 'DD')
                        || TO_CHAR (v_rcpt_date, '/MM/RRRR'),
                        'DD/MM/RRRR'
                       );
         END IF;

         IF     v_paid_to_date IS NOT NULL
            AND v_valid = 'N'
            AND v_paid_to_date < v_rcpt_date
            AND v_date2 =
                   TO_NUMBER (TO_CHAR (get_business_date ((v_date3 - 1), 1),
                                       'DD'
                                      )
                             )
         THEN
            v_valid := 'Y';
         END IF;
      ELSE
         IF v_date1 <= v_date2
         THEN
            v_valid := 'Y';
         END IF;
      END IF;

      RETURN v_valid;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('VALIDATE DEBIT.....' || v_bo_date);
   END;

   PROCEDURE get_direct_debit_recs (
      dr_dbt       IN OUT   dr_debit,
      v_sys_code            NUMBER,
      v_date                DATE,
      v_bnk_code            NUMBER,
      v_instal_day          NUMBER
   )
   IS
   BEGIN
      --RAISE_ERROR('v_bnk_code='||v_bnk_code||';'||v_date||';'||v_sys_code);
      IF v_sys_code = 27
      THEN
         OPEN Dr_dbt FOR
        SELECT v_sys_code, --SYS_CODE    NUMBER(15),
               POL_CODE, --POL_CODE    NUMBER(15),
               NULL PPR_CODE, --PPR_CODE    NUMBER(15),
               PRP_CODE, --PRP_CODE    NUMBER(15),
               CLNT_CODE, --CLNT_CODE    NUMBER(15),
               CLNT_SHT_DESC, --CLNT_SHT_DESC  VARCHAR2(20),
               CLNT_NAME || ' ' || CLNT_OTHER_NAMES, --CLNT_NAME     VARCHAR2(60),
               NVL(ENDR_BANK_ACC_NO, CLNT_BANK_ACC_NO), --CLNT_BANK_ACC_NO  VARCHAR2(30),
               ENDR_BBR_CODE, --CLNT_BBR_CODE  NUMBER(15),
               BNK_CODE, --BBR_BNK_CODE  NUMBER(15),
               BBR_BRANCH_NAME, --BBR_BRANCH_NAME    VARCHAR2(30),
               BBR_SHT_DESC, --BBR_SHT_DESC     VARCHAR2(30),
               BBR_REF_CODE, --BBR_REF_CODE     VARCHAR2(20),
               DECODE(POL_STATUS, 'V', 'PR', POL_TYPE), --POL_TYPE   VARCHAR2(5),
               POL_PROPOSAL_NO, --PROPOSAL_NO    VARCHAR2(20),
               POL_POLICY_NO, --POLICY_NO      VARCHAR2(20),
               Get_tot_pol_prem(POL_CODE), --POL_TOT_INSTLMT,--PREM_AMT     NUMBER(20,5),
               'PAID UPTO ' || TO_CHAR(POL_PAID_TO_DATE, 'DD/MM/RRRR'), --REMARKS       VARCHAR2(200));,
               NULL DDH_CODE,
               NULL DD_BOOK_DATE,
               nvl(ENDR_ACC_HOLDER, CLNT_NAME || ' ' || CLNT_OTHER_NAMES),
               'PL' PAY_TYPE,
               NULL LOAN_NO
          FROM LMS_POLICIES,
               TQC_CLIENTS,
               TQC_BANKS,
               TQC_BANK_BRANCHES,
               LMS_PROPOSERS,
               LMS_POLICY_ENDORSEMENTS
         WHERE PRP_CLNT_CODE = CLNT_CODE
           AND POL_PRP_CODE = PRP_CODE
           AND ENDR_BBR_CODE = BBR_CODE
           AND BBR_BNK_CODE = BNK_CODE
           AND POL_CURRENT_ENDR_CODE = ENDR_CODE
           AND POL_STATUS IN ('A', 'R', 'D', 'V')
           AND NVL(ENDR_PAY_METHOD, 'C') = 'DD'
           AND NVL(POL_BO_DEBIT_DATE, ENDR_BO_START_DATE) IS NOT NULL
           AND BNK_FORWARDING_BNK_CODE = v_bnk_code
           AND Validate_debit(TRUNC(NVL(POL_BO_DEBIT_DATE,ENDR_BO_START_DATE)),v_date) = 'Y'
           AND NVL(check_if_dd_generated(CLNT_CODE, CLNT_BBR_CODE, v_date,POL_CODE,NULL,'PL'),
                   'Y') = 'Y'
        UNION
        SELECT v_sys_code, --SYS_CODE    NUMBER(15),
               NULL, --POL_CODE    NUMBER(15),
               PPR_CODE, --PPR_CODE   NUMBER(15),
               PRP_CODE, --PRP_CODE    NUMBER(15),
               CLNT_CODE, --CLNT_CODE    NUMBER(15),
               CLNT_SHT_DESC, --CLNT_SHT_DESC  VARCHAR2(20),
               CLNT_NAME || ' ' || CLNT_OTHER_NAMES, --CLNT_NAME     VARCHAR2(60),
               NVL(PPR_BANK_ACC_NO, CLNT_BANK_ACC_NO), --CLNT_BANK_ACC_NO VARCHAR2(30),
               PPR_BBR_CODE, --CLNT_BBR_CODE  NUMBER(15),
               BNK_CODE, --BBR_BNK_CODE  NUMBER(15),
               BBR_BRANCH_NAME, --BBR_BRANCH_NAME    VARCHAR2(30),
               BBR_SHT_DESC, --BBR_SHT_DESC     VARCHAR2(30),
               BBR_REF_CODE, --BBR_REF_CODE     VARCHAR2(20),
               'MKT', --POL_TYPE    VARCHAR2(5),
               PPR_PROPOSAL_NO, --PROPOSAL_NO    VARCHAR2(20),
               NULL, --POLICY_NO     VARCHAR2(20),
               NVL(PPR_PREM_AMT, PPR_MIPP_CONTR), --PREM_AMT      NUMBER(20,5),
               'PAID UPTO ' || TO_CHAR(PPR_PAID_DATE, 'DD/MM/RRRR'), --REMARKS        VARCHAR2(200));,
               NULL,
               NULL,
               nvl(PPR_ACC_HOLDER, CLNT_NAME || ' ' || CLNT_OTHER_NAMES),
               'PR',
               NULL LOAN_NO
          FROM LMS_POL_PROPOSALS,
               TQC_CLIENTS,
               TQC_BANKS,
               TQC_BANK_BRANCHES,
               LMS_PROPOSERS
         WHERE PRP_CLNT_CODE = CLNT_CODE
           AND PPR_PRP_CODE = PRP_CODE
           AND PPR_BBR_CODE = BBR_CODE
           AND BBR_BNK_CODE = BNK_CODE
           AND NVL(PPR_PYMT_MODE, 'C') = 'DD'
           AND NVL(PPR_BO_DEBIT_DATE, PPR_BO_START_DATE) IS NOT NULL
           AND BNK_FORWARDING_BNK_CODE = v_bnk_code
           AND Validate_debit(TRUNC(NVL(PPR_BO_DEBIT_DATE,PPR_BO_START_DATE)),v_date) = 'Y'
           AND NOT PPR_PROPOSAL_NO IN
                (SELECT POL_PROPOSAL_NO
                      FROM LMS_POLICIES
                     WHERE POL_PROPOSAL_NO = PPR_PROPOSAL_NO)
           AND NVL(check_if_dd_generated(CLNT_CODE, CLNT_BBR_CODE, v_date,NULL,PPR_CODE,'PR'),
                   'Y') = 'Y'
        UNION
        SELECT v_sys_code, --SYS_CODE    NUMBER(15),
               LNA_NO, --POL_CODE    NUMBER(15),
               NULL PPR_CODE, --PPR_CODE    NUMBER(15),
               PRP_CODE, --PRP_CODE    NUMBER(15),
               CLNT_CODE, --CLNT_CODE    NUMBER(15),
               CLNT_SHT_DESC, --CLNT_SHT_DESC  VARCHAR2(20),
               CLNT_NAME || ' ' || CLNT_OTHER_NAMES, --CLNT_NAME     VARCHAR2(60),
               LNA_RPYMT_ACC_NO, --CLNT_BANK_ACC_NO  VARCHAR2(30),
               LNA_RPYMT_BBR_CODE, --CLNT_BBR_CODE  NUMBER(15),
               BNK_CODE, --BBR_BNK_CODE  NUMBER(15),
               BBR_BRANCH_NAME, --BBR_BRANCH_NAME    VARCHAR2(30),
               BBR_SHT_DESC, --BBR_SHT_DESC     VARCHAR2(30),
               BBR_REF_CODE, --BBR_REF_CODE     VARCHAR2(20),
               'LN', --POL_TYPE   VARCHAR2(5),
               POL_PROPOSAL_NO, --PROPOSAL_NO    VARCHAR2(20),
               POL_POLICY_NO, --POLICY_NO      VARCHAR2(20),
               LNA_INSTALLMENT_AMOUNT, --POL_TOT_INSTLMT,--PREM_AMT     NUMBER(20,5),
               'PAID UPTO ' || TO_CHAR(LNB_WEF, 'DD/MM/RRRR'), --REMARKS       VARCHAR2(200));,
               NULL DDH_CODE,
               NULL DD_BOOK_DATE,
               nvl(LNA_RPYMT_ACC_HOLDER, CLNT_NAME || ' ' || CLNT_OTHER_NAMES),
               'LN',
               LNA_NO 
          FROM LMS_POLICIES,
               TQC_CLIENTS,
               TQC_BANKS,
               TQC_BANK_BRANCHES,
               LMS_PROPOSERS,
               LMS_POLICY_ENDORSEMENTS,
               LMS_LOAN_APPLICATIONS,
               LMS_POLICY_LOANS,
               LMS_LOAN_BALANCES
         WHERE PRP_CLNT_CODE = CLNT_CODE
           AND POL_PRP_CODE = PRP_CODE
           AND PLN_LNA_NO = LNA_NO
           AND LNB_PLN_LNA_NO = PLN_LNA_NO
           AND LNA_POL_CODE = POL_CODE
           AND LNB_CURRENT = 'Y'
           AND LNA_STATUS = 'I'
           AND LNA_RPYMT_BBR_CODE = BBR_CODE
           AND BBR_BNK_CODE = BNK_CODE
           AND POL_CURRENT_ENDR_CODE = ENDR_CODE
           AND NVL(LNA_PAY_METHOD, 'C') = 'DD'
           AND BNK_FORWARDING_BNK_CODE = v_bnk_code
              --AND Get_business_date((TRUNC(NVL(POL_BO_DEBIT_DATE,ENDR_BO_START_DATE))-1),1) = Get_business_date(TRUNC(v_date),1)
          /* AND DECODE(NVL(v_day_month_param, 'D'),
                      'M',
                      TO_CHAR(Get_business_date((TRUNC(ADD_MONTHS(LARS_INSTALLMENT_DUE_DATE,DECODE(LNA_LOAN_FREQ_OF_PAYMENT,'M',1,'Q',3,'S',6,12))) - 1),
                                                1),
                              'MMRRRR'),
                      Get_business_date((TRUNC(ADD_MONTHS(LARS_INSTALLMENT_DUE_DATE,DECODE(LNA_LOAN_FREQ_OF_PAYMENT,'M',1,'Q',3,'S',6,12))) - 1),
                                        1)) =
               DECODE(NVL(v_day_month_param, 'D'),
                      'M',
                      TO_CHAR(Get_business_date(TRUNC(v_date), 1), 'MMRRRR'),
                      Get_business_date(TRUNC(v_date), 1))*/
           AND NVL(check_if_dd_generated(CLNT_CODE, CLNT_BBR_CODE, v_date,LNA_NO,NULL,'LN'),
                   'Y') = 'Y'
        UNION
        SELECT v_sys_code, --SYS_CODE         NUMBER(15),
               DDD_POL_CODE, --POL_CODE        NUMBER(15),
               DDD_PPR_CODE, --PPR_CODE        NUMBER(15),
               DDD_POL_PRP_CODE, --PRP_CODE        NUMBER(15),
               DDH_CLNT_CODE, --CLNT_CODE        NUMBER(15),
               DDH_CLNT_SHT_DESC, --CLNT_SHT_DESC    VARCHAR2(20),
               DDH_CLNT_NAME, --CLNT_NAME            VARCHAR2(60),
               TO_CHAR(DDH_CLNT_BANK_ACC_NO), --CLNT_BANK_ACC_NO    VARCHAR2(30),
               DDH_CLNT_BBR_CODE, --CLNT_BBR_CODE    NUMBER(15),
               DDH_BBR_BNK_CODE, --BBR_BNK_CODE    NUMBER(15),
               DDH_BBR_BRANCH_NAME, --BBR_BRANCH_NAME        VARCHAR2(30),
               DDH_BBR_SHT_DESC, --BBR_SHT_DESC       VARCHAR2(30),
               DDH_BBR_REF_CODE, --BBR_REF_CODE       VARCHAR2(20),
               DDD_POL_TYPE, --POL_TYPE        VARCHAR2(5),
               DDD_POL_PROPOSAL_NO, --PROPOSAL_NO        VARCHAR2(20),
               DDD_POL_POLICY_NO, --POLICY_NO            VARCHAR2(20),
               DDD_AMOUNT, --POL_TOT_INSTLMT,--PREM_AMT            NUMBER(20,5),
               DDD_REMARKS, --REMARKS                VARCHAR2(200));
               DDH_CODE,
               DD_BOOK_DATE,
               NVL(DDH_ACC_HOLDER, DDH_CLNT_NAME),
               DDD_TYPE,
               DECODE(DDD_TYPE,'LN',DDD_POL_CODE,NULL)
          FROM TQC_DIRECT_DEBIT_HEADER,
               TQC_DIRECT_DEBIT_DETAIL,
               TQC_DIRECT_DEBIT,
               TQC_BANKS,
               TQC_BANK_BRANCHES
         WHERE DDH_DD_CODE = DD_CODE
           AND DDD_DD_CODE = DD_CODE
           AND DDH_STATUS = 'R'
           AND BBR_BNK_CODE = BNK_CODE
           AND DDH_CLNT_BBR_CODE = BBR_CODE
           AND DDD_DDH_CODE = DDH_CODE
           AND BNK_FORWARDING_BNK_CODE = v_bnk_code;
      ELSIF v_sys_code = 37 THEN
        OPEN Dr_dbt FOR
        SELECT v_sys_code, --SYS_CODE    NUMBER(15),
               POL_BATCH_NO POL_CODE, --POL_CODE    NUMBER(15),
               NULL PPR_CODE, --PPR_CODE    NUMBER(15),
              NULL PRP_CODE, --PRP_CODE    NUMBER(15),
               CLNT_CODE, --CLNT_CODE    NUMBER(15),
               CLNT_SHT_DESC, --CLNT_SHT_DESC  VARCHAR2(20),
               CLNT_NAME || ' ' || CLNT_OTHER_NAMES, --CLNT_NAME     VARCHAR2(60),
               CLNT_BANK_ACC_NO, --CLNT_BANK_ACC_NO  VARCHAR2(30),
               CLNT_BBR_CODE ENDR_BBR_CODE, --CLNT_BBR_CODE  NUMBER(15),
               BNK_CODE, --BBR_BNK_CODE  NUMBER(15),
               BBR_BRANCH_NAME, --BBR_BRANCH_NAME    VARCHAR2(30),
               BBR_SHT_DESC, --BBR_SHT_DESC     VARCHAR2(30),
               BBR_REF_CODE, --BBR_REF_CODE     VARCHAR2(20),
               POL_POLICY_STATUS POL_TYPE, --POL_TYPE   VARCHAR2(5),
               NULL POL_PROPOSAL_NO, --PROPOSAL_NO    VARCHAR2(20),
                POL_POLICY_NO, --POLICY_NO      VARCHAR2(20),
               POL_INSTLMT_PREM, --POL_TOT_INSTLMT,--PREM_AMT     NUMBER(20,5),
               'PAID UPTO ' || TO_CHAR(POL_PAID_TO_DATE, 'DD/MM/RRRR'), --REMARKS       VARCHAR2(200));,
               NULL DDH_CODE,
               NULL DD_BOOK_DATE,
                CLNT_NAME || ' ' || CLNT_OTHER_NAMES,
               NULL PAY_TYPE,
               NULL LOAN_NO
          FROM GIN_POLICIES,
               TQC_CLIENTS,
               TQC_BANKS,
               TQC_BANK_BRANCHES
         WHERE POL_PRP_CODE = CLNT_CODE
           AND BBR_BNK_CODE = BNK_CODE
           AND POL_POLICY_STATUS IN ('NB','RN','ME')
           AND POL_CURRENT_STATUS !='D'
           AND NVL(POL_AUTHOSRISED,'N')='A' 
           AND NVL(POL_PAY_METHOD,'C') = 'DD'
           AND pol_instlmt_day = v_instal_day
           AND NVL(bnk_dd_supported,'N') = 'Y'
           AND clnt_bbr_code =  bbr_code
           AND NVL(POL_MATURITY_DATE, POL_PAID_UP_DATE) IS NOT NULL
           AND bnk_code = v_bnk_code
           --AND Validate_debit(TRUNC(NVL(POL_PAID_UP_DATE,POL_MATURITY_DATE)),v_date) = 'Y'
           AND POL_UW_YEAR=GIN_MISC_PKG.pol_uw_yr(pol_policy_no)
           ;
          -- AND NVL(check_if_dd_generated(CLNT_CODE, CLNT_BBR_CODE, v_date,POL_CODE,NULL,'PL'), 'Y') = 'Y';
      ELSE
         raise_error ('System code ' || v_sys_code || ' not provided for....');
      END IF;
   END get_direct_debit_recs;

  PROCEDURE Create_direct_debit_recs(v_sys_code NUMBER,
                                     v_bnk_code NUMBER,
                                     v_acc_no   VARCHAR2,
                                     v_user     VARCHAR2,
                                     v_date     DATE,
                                     v_instal_day NUMBER) IS
    v_day_month_param VARCHAR2(5);
    CURSOR Company IS
      SELECT bnk_code
        FROM TQC_BANKS--, TQC_BANK_BRANCHES, FMS_BNK_ACCTS
       WHERE --BBR_BNK_CODE = BNK_CODE
          NVL(bnk_dd_supported, 'N') = 'Y'
        -- AND BCT_BBR_CODE = BBR_CODE
        -- AND BCT_NO = v_acc_no
         AND bnk_code = v_bnk_code;
    CURSOR Clnt IS
         SELECT DISTINCT CLNT_CODE,
                      CLNT_SHT_DESC,
                      CLNT_NAME || ' ' || CLNT_OTHER_NAMES CLIENT,
                      clnt_dd_account_no,
                      clnt_dd_bbr_code,
                      BNK_CODE,
                       BBR_BRANCH_NAME,
                       BBR_SHT_DESC,
                       BBR_REF_CODE,
                      NULL DD_BOOK_DATE,
                      NULL DDH_CODE,
                      NULL DDH_DD_CODE,
                      clnt_dd_account_name ACC_HOLDER
         FROM GIN_POLICIES,
               TQC_CLIENTS,
               TQC_BANKS,
               TQC_BANK_BRANCHES
         WHERE POL_PRP_CODE = CLNT_CODE
           AND BBR_BNK_CODE = BNK_CODE
           AND POL_POLICY_STATUS IN ('NB','RN','ME')
           AND POL_CURRENT_STATUS !='D'
           AND NVL(POL_AUTHOSRISED,'N')='A' 
           AND NVL(POL_PAY_METHOD,'C') = 'DD'
           AND clnt_dd_bbr_code =  bbr_code
           AND v_sys_code = 37
           AND NVL(POL_MATURITY_DATE, POL_PAID_UP_DATE) IS NOT NULL
           and bnk_code = v_bnk_code
           --AND BNK_FORWARDING_BNK_CODE IN (SELECT BBR_BNK_CODE FROM TQC_BANK_BRANCHES WHERE BBR_CODE = v_bbr_code )
          -- AND Validate_debit(TRUNC(NVL(POL_PAID_UP_DATE,POL_MATURITY_DATE)),v_date) = 'Y'
          -- and pol_prp_code  = 10377191
        ;
    CURSOR Clnt_dtls(v_clnt_code NUMBER) IS
      SELECT POL_BATCH_NO POL_CODE,
             NULL PPR_CODE,
             null PRP_CODE,
             CLNT_CODE,
             null POL_PROPOSAL_NO,
             NVL(pol_old_policy_number,POL_POLICY_NO) POL_POLICY_NO,
             POL_POLICY_STATUS POL_TYPE,
             POL_INSTLMT_PREM TOT_INSTLMT,
             'PAID UPTO ' || TO_CHAR(POL_PAID_TO_DATE, 'DD/MM/RRRR') REMARKS,
            null  POL_BO_DEBIT_DATE,
             null ENDR_BO_START_DATE,
             'PL' PAY_TYPE
        FROM GIN_POLICIES,
             TQC_CLIENTS,
             TQC_BANKS,
             TQC_BANK_BRANCHES
       WHERE POL_PRP_CODE = CLNT_CODE
           AND BBR_BNK_CODE = BNK_CODE
           AND POL_POLICY_STATUS IN ('NB','RN','ME')
           AND POL_CURRENT_STATUS !='D'
           AND NVL(POL_AUTHOSRISED,'N')='A' 
           AND NVL(POL_PAY_METHOD,'C') = 'DD'
           AND pol_instlmt_day = v_instal_day
           AND NVL(bnk_dd_supported,'N') = 'Y'
           AND clnt_dd_bbr_code =  bbr_code
           AND NVL(POL_MATURITY_DATE, POL_PAID_UP_DATE) IS NOT NULL
           AND bnk_code = v_bnk_code
           and pol_prp_code = v_clnt_code
           --AND Validate_debit(TRUNC(NVL(POL_PAID_UP_DATE,POL_MATURITY_DATE)),v_date) = 'Y'
           AND POL_UW_YEAR=GIN_MISC_PKG.pol_uw_yr(pol_policy_no)
           --AND BNK_FORWARDING_BNK_CODE IN (SELECT BBR_BNK_CODE FROM TQC_BANK_BRANCHES WHERE BBR_CODE = v_bbr_code )
          -- AND Validate_debit(TRUNC(NVL(POL_PAID_UP_DATE,POL_MATURITY_DATE)),v_date) = 'Y'
          -- and pol_prp_code  = 10377191
      UNION
      SELECT DDD_POL_CODE,
             DDD_POL_PRP_CODE,
             DDD_PPR_CODE,
             DDH_CLNT_CODE,
             DDD_POL_PROPOSAL_NO,
             DDD_POL_POLICY_NO,
             DDD_POL_TYPE,
             DDD_AMOUNT,
             DDD_REMARKS,
             NULL POL_BO_DEBIT_DATE,
             DDD_START_DT,
             DDD_TYPE
        FROM TQC_DIRECT_DEBIT_DETAIL,
             TQC_DIRECT_DEBIT_HEADER,
             TQC_DIRECT_DEBIT
       WHERE DDH_CLNT_CODE = v_clnt_code
         AND DDD_DDH_CODE = DDH_CODE
         AND DDH_DD_CODE = DD_CODE
         AND DDD_DD_CODE = DD_CODE
         AND dd_bnk_code = v_bnk_code
         AND DDH_STATUS = 'R'
        -- and pol_prp_code  = 10377191
         AND v_sys_code = 27;
    v_dd_code    NUMBER;
    v_ddh_code   NUMBER;
    v_ddd_code   NUMBER;
    nTotalAmount NUMBER := 0;
    v_cnt1       NUMBER;
    v_cnt2       NUMBER;
  BEGIN
    v_day_month_param := NVL(TQC_PARAMETERS_PKG.get_param_varchar('USE_BOOKING_DAY_OR_MONTH'),
                              'D');
 -- RAISE_ERROR('v_sys_code='||v_sys_code||';'||v_bbr_code||';'||v_acc_no||';'||v_user||';'||v_date);
    FOR CO IN Company LOOP
      BEGIN
        SELECT TQC_DD_CODE_SEQ.NEXTVAL INTO v_dd_code FROM DUAL;
        INSERT INTO TQC_DIRECT_DEBIT
          (DD_CODE,
           DD_REF_NO,
           DD_BOOK_DATE,
           DD_BNK_CODE,
          -- DD_BBR_CODE,
          -- DD_ACNT_NO,
           DD_DATE,
           DD_RAISED_BY,
           DD_VALUE_DATE,
           dd_gen_date_value)
        VALUES
          (v_dd_code,
           TO_CHAR(TRUNC(SYSDATE), 'RRRRMMDD'),
           (v_date),
           CO.bnk_code,
         --  CO.BBR_CODE,
          -- CO.BCT_NO,
           SYSDATE,
           v_user,
           Get_business_date(v_date, 4),
           v_instal_day
           );
      EXCEPTION
        WHEN OTHERS THEN
          Raise_error('Error#1...');
      END;
      FOR C IN Clnt LOOP
      
        nTotalAmount := 0;
        IF v_sys_code = 37 THEN
          BEGIN
            SELECT TQC_DDH_CODE_SEQ.NEXTVAL INTO v_ddh_code FROM DUAL;
            INSERT INTO TQC_DIRECT_DEBIT_HEADER
              (DDH_CODE,
               DDH_DD_CODE,
               DDH_CLNT_CODE,
               DDH_CLNT_BBR_CODE,
               DDH_BBR_BNK_CODE,
               DDH_CLNT_SHT_DESC,
               DDH_CLNT_NAME,
               DDH_CLNT_BANK_ACC_NO,
               DDH_BBR_BRANCH_NAME,
               DDH_BBR_SHT_DESC,
               DDH_BBR_REF_CODE,
               DDH_TOT_AMT,
               DDH_INITIAL_BOOK_DATE,
               DDH_PREV_DDH_CODE,
               DDH_ACC_HOLDER)
            VALUES
              (v_ddh_code,
               v_dd_code,
               C.CLNT_CODE,
               C.clnt_dd_bbr_code,
               C.BNK_CODE,
               C.CLNT_SHT_DESC,
               C.CLIENT,
               C.clnt_dd_account_no,
               C.BBR_BRANCH_NAME,
               C.BBR_SHT_DESC,
               C.BBR_REF_CODE,
               0,
               C.DD_BOOK_DATE,
               C.DDH_CODE,
               c.ACC_HOLDER);
          EXCEPTION
            WHEN OTHERS THEN
              Raise_error('Error#2...'||C.CLIENT);
          END;
           nTotalAmount :=0;
          FOR D IN Clnt_dtls(C.CLNT_CODE) LOOP
            
            BEGIN
              SELECT TQC_DDD_CODE_SEQ.NEXTVAL INTO v_ddd_code FROM DUAL;
              INSERT INTO TQC_DIRECT_DEBIT_DETAIL
                (DDD_CODE,
                 DDD_DDH_CODE,
                 DDD_DD_CODE,
                 DDD_SYS_CODE,
                 DDD_POL_CODE,
                 DDD_POL_PRP_CODE,
                 DDD_POL_PROPOSAL_NO,
                 DDD_POL_POLICY_NO,
                 DDD_AMOUNT,
                 DDD_REMARKS,
                 DDD_START_DT,
                 DDD_STOP_DATE,
                 DDD_PPR_CODE,
                 DDD_POL_TYPE,
                 DDD_TYPE)
              VALUES
                (v_ddd_code,
                 v_ddh_code,
                 v_dd_code,
                 v_sys_code,
                 D.POL_CODE,
                 D.PRP_CODE,
                 D.POL_PROPOSAL_NO,
                 D.POL_POLICY_NO,
                 D.TOT_INSTLMT,
                 D.REMARKS,
                 D.ENDR_BO_START_DATE,
                 NULL,
                 D.PPR_CODE,
                 D.POL_TYPE,
                 D.PAY_TYPE);
              nTotalAmount := nTotalAmount + NVL(D.TOT_INSTLMT, 0);
            EXCEPTION
              WHEN OTHERS THEN
                Raise_error('Error#3...');
            END;
          END LOOP;
         -- raise_error('nTotalAmount '||nTotalAmount);
          UPDATE TQC_DIRECT_DEBIT_HEADER
             SET DDH_TOT_AMT = nTotalAmount
           WHERE DDH_CODE = v_ddh_code;
        ELSE
          Raise_error('System code ' || v_sys_code ||
                      ' not provided for..2..');
        END IF;
      END LOOP;
      FOR C IN Clnt LOOP
        BEGIN
          SELECT COUNT(1)
            INTO v_cnt1
            FROM TQC_DIRECT_DEBIT_HEADER
           WHERE DDH_DD_CODE = (SELECT DDH_DD_CODE
                                  FROM TQC_DIRECT_DEBIT_HEADER
                                 WHERE DDH_CODE = C.DDH_CODE);
        EXCEPTION
          WHEN OTHERS THEN
            RAISE_ERROR('Child records not found....');
        END;
        BEGIN
          SELECT COUNT(1)
            INTO v_cnt2
            FROM TQC_DIRECT_DEBIT_HEADER
           WHERE DDH_DD_CODE = (SELECT DDH_DD_CODE
                                  FROM TQC_DIRECT_DEBIT_HEADER
                                 WHERE DDH_CODE = C.DDH_CODE)
             AND DDH_STATUS = 'R';
        EXCEPTION
          WHEN OTHERS THEN
            RAISE_ERROR('Child records not found....');
        END;
        IF NVL(v_cnt1, 0) = NVL(v_cnt2, 0) AND NVL(v_cnt1, 0) > 0 THEN
          BEGIN
            UPDATE TQC_DIRECT_DEBIT
               SET DD_RECEIPTED = 'F'
             WHERE DD_CODE = C.DDH_DD_CODE;
          EXCEPTION
            WHEN OTHERS THEN
              RAISE_ERROR('Update DD error...');
          END;
        END IF;
        BEGIN
          UPDATE TQC_DIRECT_DEBIT_HEADER
             SET DDH_STATUS = 'F'
           WHERE DDH_CODE = C.DDH_CODE;
        END;
        BEGIN
          UPDATE TQC_DIRECT_DEBIT_DETAIL
             SET DDD_STATUS = 'F'
           WHERE DDD_DDH_CODE = C.DDH_CODE;
        END;
      END LOOP;
    END LOOP;
  END Create_direct_debit_recs;

   PROCEDURE create_direct_debit_recsgis (
      v_sys_code     NUMBER,
      v_bnk_code     NUMBER,
      v_acc_no       VARCHAR2,
      v_user         VARCHAR2,
      v_date         DATE,
      v_instal_day   NUMBER
   )
   IS
      v_day_month_param   VARCHAR2 (5);

      CURSOR company
      IS
         SELECT bnk_code
           FROM tqc_banks                --, TQC_BANK_BRANCHES, FMS_BNK_ACCTS
          WHERE                                     --BBR_BNK_CODE = BNK_CODE
                NVL (bnk_dd_supported, 'N') = 'Y'
                                                 -- AND BCT_BBR_CODE = BBR_CODE
                                                 -- AND BCT_NO = v_acc_no
                AND bnk_code = v_bnk_code;

      CURSOR clnt
      IS
         SELECT DISTINCT clnt_code, clnt_sht_desc,
                         clnt_name || ' ' || clnt_other_names client,
                         clnt_dd_account_no, clnt_dd_bbr_code, bnk_code,
                         bbr_branch_name, bbr_sht_desc, bbr_ref_code,
                         NULL dd_book_date, NULL ddh_code, NULL ddh_dd_code,
                         clnt_dd_account_name acc_holder
                    FROM gin_policies,
                         tqc_clients,
                         tqc_banks,
                         tqc_bank_branches
                   WHERE pol_prp_code = clnt_code
                     AND bbr_bnk_code = bnk_code
                     AND pol_policy_status IN ('NB', 'RN', 'ME')
                     AND pol_current_status != 'D'
                     AND NVL (pol_authosrised, 'N') = 'A'
                     AND NVL (pol_pay_method, 'C') = 'DD'
                     AND clnt_dd_bbr_code = bbr_code
                     AND v_sys_code = 37
                     AND NVL (pol_maturity_date, pol_paid_up_date) IS NOT NULL
                     AND bnk_code = v_bnk_code
                     --AND BNK_FORWARDING_BNK_CODE IN (SELECT BBR_BNK_CODE FROM TQC_BANK_BRANCHES WHERE BBR_CODE = v_bbr_code )
                     AND validate_debit (TRUNC (NVL (pol_paid_up_date,
                                                     pol_maturity_date
                                                    )
                                               ),
                                         v_date
                                        ) = 'Y'
                                               -- and pol_prp_code  = 10377191
      ;

      CURSOR clnt_dtls (v_clnt_code NUMBER)
      IS
         SELECT pol_batch_no pol_code, NULL ppr_code, NULL prp_code,
                clnt_code, NULL pol_proposal_no, pol_policy_no,
                pol_policy_status pol_type, pol_instlmt_prem tot_instlmt,
                   'PAID UPTO '
                || TO_CHAR (pol_paid_to_date, 'DD/MM/RRRR') remarks,
                NULL pol_bo_debit_date, NULL endr_bo_start_date,
                'PL' pay_type
           FROM gin_policies, tqc_clients, tqc_banks, tqc_bank_branches
          WHERE pol_prp_code = clnt_code
            AND bbr_bnk_code = bnk_code
            AND pol_policy_status IN ('NB', 'RN', 'ME')
            AND pol_current_status != 'D'
            AND NVL (pol_authosrised, 'N') = 'A'
            AND NVL (pol_pay_method, 'C') = 'DD'
            AND pol_instlmt_day = v_instal_day
            AND NVL (bnk_dd_supported, 'N') = 'Y'
            AND clnt_dd_bbr_code = bbr_code
            AND NVL (pol_maturity_date, pol_paid_up_date) IS NOT NULL
            AND bnk_code = v_bnk_code
            AND pol_prp_code = v_clnt_code
            AND validate_debit (TRUNC (NVL (pol_paid_up_date,
                                            pol_maturity_date
                                           )
                                      ),
                                v_date
                               ) = 'Y'
            AND pol_uw_year = gin_misc_pkg.pol_uw_yr (pol_policy_no)
            --AND BNK_FORWARDING_BNK_CODE IN (SELECT BBR_BNK_CODE FROM TQC_BANK_BRANCHES WHERE BBR_CODE = v_bbr_code )
            AND validate_debit (TRUNC (NVL (pol_paid_up_date,
                                            pol_maturity_date
                                           )
                                      ),
                                v_date
                               ) = 'Y'
         -- and pol_prp_code  = 10377191
         UNION
         SELECT ddd_pol_code, ddd_pol_prp_code, ddd_ppr_code, ddh_clnt_code,
                ddd_pol_proposal_no, ddd_pol_policy_no, ddd_pol_type,
                ddd_amount, ddd_remarks, NULL pol_bo_debit_date, ddd_start_dt,
                ddd_type
           FROM tqc_direct_debit_detail,
                tqc_direct_debit_header,
                tqc_direct_debit
          WHERE ddh_clnt_code = v_clnt_code
            AND ddd_ddh_code = ddh_code
            AND ddh_dd_code = dd_code
            AND ddd_dd_code = dd_code
            AND dd_bnk_code = v_bnk_code
            AND ddh_status = 'R'
            -- and pol_prp_code  = 10377191
            AND v_sys_code = 27;

      v_dd_code           NUMBER;
      v_ddh_code          NUMBER;
      v_ddd_code          NUMBER;
      ntotalamount        NUMBER       := 0;
      v_cnt1              NUMBER;
      v_cnt2              NUMBER;
   BEGIN
      v_day_month_param :=
         NVL
            (tqc_parameters_pkg.get_param_varchar ('USE_BOOKING_DAY_OR_MONTH'),
             'D'
            );

      -- RAISE_ERROR('v_sys_code='||v_sys_code||';'||v_bbr_code||';'||v_acc_no||';'||v_user||';'||v_date);
      FOR co IN company
      LOOP
         BEGIN
            SELECT tqc_dd_code_seq.NEXTVAL
              INTO v_dd_code
              FROM DUAL;

            INSERT INTO tqc_direct_debit
                        (dd_code, dd_ref_no,
                         dd_book_date, dd_bnk_code,
                                                   -- DD_BBR_CODE,
                                                   -- DD_ACNT_NO,
                                                   dd_date, dd_raised_by,
                         dd_value_date, dd_gen_date_value
                        )
                 VALUES (v_dd_code, TO_CHAR (TRUNC (SYSDATE), 'RRRRMMDD'),
                         (v_date
                         ), co.bnk_code,
                                        --  CO.BBR_CODE,
                                         -- CO.BCT_NO,
                                        SYSDATE, v_user,
                         get_business_date (v_date, 4), v_instal_day
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error#1...');
         END;

         FOR c IN clnt
         LOOP
            ntotalamount := 0;

            IF v_sys_code = 37
            THEN
               BEGIN
                  SELECT tqc_ddh_code_seq.NEXTVAL
                    INTO v_ddh_code
                    FROM DUAL;

                  INSERT INTO tqc_direct_debit_header
                              (ddh_code, ddh_dd_code, ddh_clnt_code,
                               ddh_clnt_bbr_code, ddh_bbr_bnk_code,
                               ddh_clnt_sht_desc, ddh_clnt_name,
                               ddh_clnt_bank_acc_no, ddh_bbr_branch_name,
                               ddh_bbr_sht_desc, ddh_bbr_ref_code,
                               ddh_tot_amt, ddh_initial_book_date,
                               ddh_prev_ddh_code, ddh_acc_holder
                              )
                       VALUES (v_ddh_code, v_dd_code, c.clnt_code,
                               c.clnt_dd_bbr_code, c.bnk_code,
                               c.clnt_sht_desc, c.client,
                               c.clnt_dd_account_no, c.bbr_branch_name,
                               c.bbr_sht_desc, c.bbr_ref_code,
                               0, c.dd_book_date,
                               c.ddh_code, c.acc_holder
                              );
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('Error#2...' || c.client);
               END;

               ntotalamount := 0;

               FOR d IN clnt_dtls (c.clnt_code)
               LOOP
                  BEGIN
                     SELECT tqc_ddd_code_seq.NEXTVAL
                       INTO v_ddd_code
                       FROM DUAL;

                     INSERT INTO tqc_direct_debit_detail
                                 (ddd_code, ddd_ddh_code, ddd_dd_code,
                                  ddd_sys_code, ddd_pol_code,
                                  ddd_pol_prp_code, ddd_pol_proposal_no,
                                  ddd_pol_policy_no, ddd_amount, ddd_remarks,
                                  ddd_start_dt, ddd_stop_date, ddd_ppr_code,
                                  ddd_pol_type, ddd_type
                                 )
                          VALUES (v_ddd_code, v_ddh_code, v_dd_code,
                                  v_sys_code, d.pol_code,
                                  d.prp_code, d.pol_proposal_no,
                                  d.pol_policy_no, d.tot_instlmt, d.remarks,
                                  d.endr_bo_start_date, NULL, d.ppr_code,
                                  d.pol_type, d.pay_type
                                 );

                     ntotalamount := ntotalamount + NVL (d.tot_instlmt, 0);
                  EXCEPTION
                     WHEN OTHERS
                     THEN
                        raise_error ('Error#3...');
                  END;
               END LOOP;

               -- raise_error('nTotalAmount '||nTotalAmount);
               UPDATE tqc_direct_debit_header
                  SET ddh_tot_amt = ntotalamount
                WHERE ddh_code = v_ddh_code;
            ELSE
               raise_error (   'System code '
                            || v_sys_code
                            || ' not provided for..2..'
                           );
            END IF;
         END LOOP;

         FOR c IN clnt
         LOOP
            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt1
                 FROM tqc_direct_debit_header
                WHERE ddh_dd_code = (SELECT ddh_dd_code
                                       FROM tqc_direct_debit_header
                                      WHERE ddh_code = c.ddh_code);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Child records not found....');
            END;

            BEGIN
               SELECT COUNT (1)
                 INTO v_cnt2
                 FROM tqc_direct_debit_header
                WHERE ddh_dd_code = (SELECT ddh_dd_code
                                       FROM tqc_direct_debit_header
                                      WHERE ddh_code = c.ddh_code)
                  AND ddh_status = 'R';
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Child records not found....');
            END;

            IF NVL (v_cnt1, 0) = NVL (v_cnt2, 0) AND NVL (v_cnt1, 0) > 0
            THEN
               BEGIN
                  UPDATE tqc_direct_debit
                     SET dd_receipted = 'F'
                   WHERE dd_code = c.ddh_dd_code;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('Update DD error...');
               END;
            END IF;

            BEGIN
               UPDATE tqc_direct_debit_header
                  SET ddh_status = 'F'
                WHERE ddh_code = c.ddh_code;
            END;

            BEGIN
               UPDATE tqc_direct_debit_detail
                  SET ddd_status = 'F'
                WHERE ddd_ddh_code = c.ddh_code;
            END;
         END LOOP;
      END LOOP;
   END create_direct_debit_recsgis;

--   PROCEDURE create_direct_debit_recslms (
--      v_sys_code     NUMBER,
--      v_bbr_code     NUMBER,
--      v_acc_no       VARCHAR2,
--      v_user         VARCHAR2,
--      v_date         DATE,
--      v_instal_day   NUMBER
--   )
--   IS
--      v_day_month_param   VARCHAR2 (5);
--      v_dd_cnt            NUMBER;
--      v_fut_debits        VARCHAR2 (1);

--      CURSOR company
--      IS
--         SELECT bbr_code, bbr_bnk_code, bct_no
--           FROM tqc_banks, tqc_bank_branches, fms_bnk_accts
--          WHERE bbr_bnk_code = bnk_code
--            AND NVL (bbr_dd_supported, 'N') = 'Y'
--            AND bct_bbr_code = bbr_code
--            AND bct_no = v_acc_no
--            AND bbr_code = v_bbr_code;

--      CURSOR clnt
--      IS
--         SELECT DISTINCT clnt_code, clnt_sht_desc,
--                         clnt_name || ' ' || clnt_other_names client,
--                         NVL (endr_bank_acc_no,
--                              clnt_bank_acc_no
--                             ) clnt_bank_acc_no,
--                         endr_bbr_code clnt_bbr_code, bnk_code,
--                         a.bbr_branch_name, a.bbr_sht_desc, a.bbr_ref_code,
--                         NULL dd_book_date, NULL ddh_code, NULL ddh_dd_code,
--                         NVL (endr_acc_holder,
--                              clnt_name || ' ' || clnt_other_names
--                             ) acc_holder,
--                         pol_paid_to_date, pol_effective_date, 'PL' dtype
--                    FROM lms_policies,
--                         tqc_clients,
--                         tqc_banks,
--                         tqc_bank_branches a,
--                         lms_proposers,
--                         lms_policy_endorsements,
--                         tqc_bank_branches b
--                   WHERE prp_clnt_code = clnt_code
--                     AND pol_prp_code = prp_code
--                     AND endr_bbr_code = a.bbr_code
--                     AND bnk_forwarding_bnk_code = b.bbr_bnk_code
--                     AND b.bbr_code = v_bbr_code
--                     AND a.bbr_bnk_code = bnk_code
--                     AND pol_current_endr_code = endr_code
--                     AND pol_status IN ('A', 'R', 'D', 'V', 'L')
--                     AND NVL (pol_tot_instlmt, 1) >
--                                                  NVL (pol_paid_instlmt_no, 0)
--                     AND endr_pay_method = 'DD'
--                     --AND NVL(Lms_Ord_Misc.Inst_prem(POL_CODE),0) > NVL(Lms_Process_Receipts.Pol_holder_alloc(POL_CODE,'INST'),0)
--                     AND NVL (lms_ord_misc.inst_prem (pol_code), 0) >
--                                             0
--                                              --Arising coz of loaded policies
--                     --AND Lms_Ord_Misc.Check_claim(POL_CODE) <> 'Y'
--                     AND lms_ord_misc.check_unauth_surrender (pol_code) <> 'Y'
--                     --AND NVL(POL_PAID_TO_DATE,POL_EFFECTIVE_DATE) <= v_date
--                     AND NVL (pol_paid_to_date, pol_effective_date) <=
--                            DECODE (NVL (v_fut_debits, 'N'),
--                                    'Y', NVL (pol_paid_to_date,
--                                              pol_effective_date
--                                             ),
--                                    v_date
--                                   )
--                     AND NVL (pol_bo_debit_date, endr_bo_start_date) IS NOT NULL
--                     AND validate_debit (TRUNC (NVL (pol_bo_debit_date,
--                                                     endr_bo_start_date
--                                                    )
--                                               ),
--                                         v_date
--                                        ) = 'Y'
----         AND DECODE(NVL(v_day_month_param, 'D'),
----                    'M',
----                    TO_CHAR(Get_business_date((TRUNC(NVL(POL_BO_DEBIT_DATE,
----                                                         ENDR_BO_START_DATE)) - 1),
----                                              1),
----                            'MMRRRR'),
----                    Get_business_date((TRUNC(NVL(POL_BO_DEBIT_DATE,
----                                                 ENDR_BO_START_DATE)) - 1),
----                                      1)) =
----             DECODE(NVL(v_day_month_param, 'D'),
----                    'M',
----                    TO_CHAR(Get_business_date(TRUNC(v_date), 1), 'MMRRRR'),
----                    Get_business_date(TRUNC(v_date), 1))
--                     AND v_sys_code = 27
--                     AND NVL (check_if_dd_generated (clnt_code,
--                                                     clnt_bbr_code,
--                                                     v_date,
--                                                     pol_code,
--                                                     NULL,
--                                                     'PL'
--                                                    ),
--                              'Y'
--                             ) = 'Y'
--         UNION ALL
--         SELECT DISTINCT clnt_code, clnt_sht_desc,
--                         clnt_name || ' ' || clnt_other_names client,
--                         NVL (ppr_bank_acc_no,
--                              clnt_bank_acc_no
--                             ) clnt_bank_acc_no,
--                         ppr_bbr_code, bnk_code, a.bbr_branch_name,
--                         a.bbr_sht_desc, a.bbr_ref_code, NULL dd_book_date,
--                         NULL ddh_code, NULL ddh_dd_code,
--                         NVL (ppr_acc_holder,
--                              clnt_name || ' ' || clnt_other_names
--                             ) ppr_acc_holder,
--                         NULL pol_paid_to_date,
--                         ppr_bo_start_date pol_effective_date, 'PR' dtype
--                    FROM lms_pol_proposals p1,
--                         tqc_clients,
--                         tqc_banks,
--                         tqc_bank_branches a,
--                         lms_proposers,
--                         tqc_bank_branches b
--                   WHERE prp_clnt_code = clnt_code
--                     AND ppr_prp_code = prp_code
--                     AND ppr_bbr_code = a.bbr_code
--                     AND a.bbr_bnk_code = bnk_code
--                     AND bnk_forwarding_bnk_code = b.bbr_bnk_code
--                     AND b.bbr_code = v_bbr_code
--                     AND NVL (ppr_pymt_mode, 'C') = 'DD'
--                     AND NVL (ppr_bo_debit_date, ppr_bo_start_date) IS NOT NULL
--                     AND validate_debit (TRUNC (NVL (ppr_bo_debit_date,
--                                                     ppr_bo_start_date
--                                                    )
--                                               ),
--                                         v_date
--                                        ) = 'Y'
----         AND DECODE(NVL(v_day_month_param, 'D'),
----                    'M',
----                    TO_CHAR(Get_business_date((TRUNC(NVL(PPR_BO_DEBIT_DATE,
----                                                         PPR_BO_START_DATE)) - 1),
----                                              1),
----                            'MMRRRR'),
----                    Get_business_date((TRUNC(NVL(PPR_BO_DEBIT_DATE,
----                                                 PPR_BO_START_DATE)) - 1),
----                                      1)) =
----             DECODE(NVL(v_day_month_param, 'D'),
----                    'M',
----                    TO_CHAR(Get_business_date(TRUNC(v_date), 1), 'MMRRRR'),
----                    Get_business_date(TRUNC(v_date), 1))
--            --AND Get_business_date((TRUNC(NVL(PPR_BO_DEBIT_DATE,PPR_BO_START_DATE))-1),1) = Get_business_date(TRUNC(v_date),1)
--                     AND NOT ppr_proposal_no IN (
--                            SELECT pol_proposal_no
--                              FROM lms_policies, lms_pol_proposals p2
--                             WHERE pol_proposal_no = p2.ppr_proposal_no
--                               AND p1.ppr_proposal_no = p2.ppr_proposal_no)
--                     AND v_sys_code = 27
--                     AND NVL (check_if_dd_generated (clnt_code,
--                                                     clnt_bbr_code,
--                                                     v_date,
--                                                     NULL,
--                                                     ppr_code,
--                                                     'PR'
--                                                    ),
--                              'Y'
--                             ) = 'Y'
--         UNION
--         SELECT DISTINCT clnt_code, clnt_sht_desc,
--                         clnt_name || ' ' || clnt_other_names client,
--                         lna_rpymt_acc_no,
--                         NVL (lna_rpymt_bbr_code,
--                              endr_bbr_code
--                             ) lna_rpymt_bbr_code,
--                         bnk_code, a.bbr_branch_name, a.bbr_sht_desc,
--                         a.bbr_ref_code, NULL dd_book_date, NULL ddh_code,
--                         NULL ddh_dd_code,
--                         NVL (lna_rpymt_acc_holder,
--                              clnt_name || ' ' || clnt_other_names
--                             ) acc_holder,
--                         NULL pol_paid_to_date, NULL pol_effective_date,
--                         'LN' dtype
--                    FROM lms_policies,
--                         tqc_clients,
--                         tqc_banks,
--                         tqc_bank_branches a,
--                         lms_proposers,
--                         lms_policy_endorsements,
--                         lms_loan_applications,
--                         lms_policy_loans,
--                         lms_loan_balances,
--                         tqc_bank_branches b
--                   WHERE prp_clnt_code = clnt_code
--                     AND pol_prp_code = prp_code
--                     AND pln_lna_no = lna_no
--                     AND lnb_pln_lna_no = pln_lna_no
--                     AND lna_pol_code = pol_code
--                     AND lnb_current = 'Y'
--                     AND lna_status = 'I'
--                     AND NVL (lna_rpymt_bbr_code, endr_bbr_code) = a.bbr_code
--                     AND a.bbr_bnk_code = bnk_code
--                     AND bnk_forwarding_bnk_code = b.bbr_bnk_code
--                     AND b.bbr_code = v_bbr_code
--                     --AND BNK_FORWARDING_BNK_CODE IN (SELECT BBR_BNK_CODE FROM TQC_BANK_BRANCHES WHERE BBR_CODE = v_bbr_code )
--                     AND pol_current_endr_code = endr_code
--                     AND NVL (pln_os_bal_amount, 0) > 0
--                     AND NVL (lna_pay_method, 'C') = 'DD'
--                     --AND Lms_Ord_Misc.Check_claim(POL_CODE) <> 'Y'
--                     AND lms_ord_misc.check_unauth_surrender (pol_code) <> 'Y'
--                     AND validate_debit (TRUNC (NVL (lna_date_issued,
--                                                     lna_date)
--                                               ),
--                                         v_date
--                                        ) = 'Y'
--                        --AND Get_business_date((TRUNC(NVL(POL_BO_DEBIT_DATE,ENDR_BO_START_DATE))-1),1) = Get_business_date(TRUNC(v_date),1)
--                     /*AND DECODE(NVL(v_day_month_param, 'D'),
--                                'M',
--                                TO_CHAR(Get_business_date((TRUNC(ADD_MONTHS(LARS_INSTALLMENT_DUE_DATE,DECODE(LNA_LOAN_FREQ_OF_PAYMENT,'M',1,'Q',3,'S',6,12))) - 1),
--                                                          1),
--                                        'MMRRRR'),
--                                Get_business_date((TRUNC(ADD_MONTHS(LARS_INSTALLMENT_DUE_DATE,DECODE(LNA_LOAN_FREQ_OF_PAYMENT,'M',1,'Q',3,'S',6,12))) - 1),
--                                                  1)) =
--                         DECODE(NVL(v_day_month_param, 'D'),
--                                'M',
--                                TO_CHAR(Get_business_date(TRUNC(v_date), 1), 'MMRRRR'),
--                                Get_business_date(TRUNC(v_date), 1))*/
--                     AND NVL (check_if_dd_generated (clnt_code,
--                                                     clnt_bbr_code,
--                                                     v_date,
--                                                     lna_no,
--                                                     NULL,
--                                                     'LN'
--                                                    ),
--                              'Y'
--                             ) = 'Y'
--         UNION
--         SELECT ddh_clnt_code, ddh_clnt_sht_desc, ddh_clnt_name,
--                TO_CHAR (ddh_clnt_bank_acc_no), ddh_clnt_bbr_code,
--                ddh_bbr_bnk_code, ddh_bbr_branch_name, ddh_bbr_sht_desc,
--                ddh_bbr_ref_code, dd_book_date, ddh_code, ddh_dd_code,
--                NVL (ddh_acc_holder, ddh_clnt_name) ddh_acc_holder, NULL,
--                NULL, NULL
--           FROM tqc_direct_debit_header, tqc_direct_debit
--          WHERE ddh_dd_code = dd_code
--            AND dd_bbr_code = v_bbr_code
--            AND ddh_status = 'R'
--            AND v_sys_code = 27;

--      CURSOR clnt_dtls
--      IS
--         SELECT   pol_code, ppr_code, prp_code, clnt_code, pol_proposal_no,
--                  pol_policy_no, pol_type, ROUND (tot_instlmt, 2) tot_instlmt,
--                  remarks, pol_bo_debit_date, endr_bo_start_date, pay_type,
--                  duedate, pol_freq_of_payment, clnt_bank_acc_no,
--                  clnt_sht_desc, client, clnt_bbr_code, bnk_code,
--                  bbr_branch_name, bbr_sht_desc, bbr_ref_code, dd_book_date,
--                  ddh_code, ddh_dd_code, acc_holder, os_instalments
--             FROM (SELECT pol_code, NULL ppr_code, prp_code, clnt_code,
--                          pol_proposal_no, pol_policy_no,
--                          DECODE (pol_status, 'V', 'PR', pol_type) pol_type,
--                          
--                          --NVL(Lms_Ord_Misc.Inst_prem(POL_CODE),0)+NVL(Lms_Process_Receipts.Pol_holder_alloc(POL_CODE,'INST'),0) TOT_INSTLMT,
--                          ROUND
--                             (NVL
--                                 (lms_ord_misc.get_premium
--                                     (pol_current_endr_code,
--                                      GREATEST
--                                         (LEAST
--                                               (NVL (ADD_MONTHS (v_date, 1),
--                                                     NVL (pol_paid_to_date + 1,
--                                                          pol_effective_date
--                                                         )
--                                                    ),
--                                                NVL (pol_paid_to_date + 1,
--                                                     pol_effective_date
--                                                    )
--                                               ),
--                               --NVL(POL_PAID_TO_DATE + 1,POL_EFFECTIVE_DATE),
--                                          LEAST
--                                             (pol_maturity_date,
--                                              ADD_MONTHS (NVL (v_date,
--                                                               TRUNC (SYSDATE)
--                                                              ),
--                                                          1
--                                                         )
--                                             )
--                                         )
--                                     ),
--                                  0
--                                 ),
--                              2
--                             ) tot_instlmt,
--                             'PAID UPTO '
--                          || TO_CHAR (pol_paid_to_date, 'DD/MM/RRRR') remarks,
--                          pol_bo_debit_date, endr_bo_start_date,
--                          'PL' pay_type, pol_paid_to_date + 1 duedate,
--                          pol_freq_of_payment,
--                          NVL (endr_bank_acc_no,
--                               clnt_bank_acc_no
--                              ) clnt_bank_acc_no,
--                          clnt_sht_desc,
--                          clnt_name || ' ' || clnt_other_names client,
--                          endr_bbr_code clnt_bbr_code, bnk_code,
--                          a.bbr_branch_name, a.bbr_sht_desc, a.bbr_ref_code,
--                          NULL dd_book_date, NULL ddh_code, NULL ddh_dd_code,
--                          NVL (endr_acc_holder,
--                               clnt_name || ' ' || clnt_other_names
--                              ) acc_holder,
--                          func_unpaid_periods (pol_code,
--                                               v_date
--                                              ) os_instalments
--                     FROM lms_policies,
--                          tqc_clients,
--                          tqc_banks,
--                          tqc_bank_branches a,
--                          lms_proposers,
--                          lms_policy_endorsements,
--                          tqc_bank_branches b
--                    WHERE prp_clnt_code = clnt_code
--                      AND pol_prp_code = prp_code
--                      AND endr_bbr_code = a.bbr_code
--                      AND a.bbr_bnk_code = bnk_code
--                      AND bnk_forwarding_bnk_code = b.bbr_bnk_code
--                      AND b.bbr_code = v_bbr_code
--                      --AND BNK_FORWARDING_BNK_CODE IN (SELECT BBR_BNK_CODE FROM TQC_BANK_BRANCHES WHERE BBR_CODE = v_bbr_code )
--                      AND pol_current_endr_code = endr_code
--                      AND pol_status IN ('A', 'R', 'D', 'V', 'L')
--                      AND NVL (pol_tot_instlmt, 1) >
--                                                  NVL (pol_paid_instlmt_no, 0)
--                      AND endr_pay_method = 'DD'
--                      --AND NVL(Lms_Ord_Misc.Inst_prem(POL_CODE),0) > NVL(Lms_Process_Receipts.Pol_holder_alloc(POL_CODE,'INST'),0)
--                      AND NVL (lms_ord_misc.inst_prem (pol_code), 0) >
--                                             0
--                                              --Arising coz of loaded policies
--                      --AND Lms_Ord_Misc.Check_claim(POL_CODE) <> 'Y'
--                      AND lms_ord_misc.check_unauth_surrender (pol_code) <>
--                                                                           'Y'
--                      --AND NVL(POL_PAID_TO_DATE,POL_EFFECTIVE_DATE) <= v_date
--                      AND NVL (pol_paid_to_date, pol_effective_date) <=
--                             DECODE (NVL (v_fut_debits, 'N'),
--                                     'Y', NVL (pol_paid_to_date,
--                                               pol_effective_date
--                                              ),
--                                     v_date
--                                    )
--                      AND NVL (pol_bo_debit_date, endr_bo_start_date) IS NOT NULL
--                      AND validate_debit (TRUNC (NVL (pol_bo_debit_date,
--                                                      endr_bo_start_date
--                                                     )
--                                                ),
--                                          v_date
--                                         ) = 'Y'
--                      --         AND DECODE(NVL(v_day_month_param, 'D'),
--                      --                    'M',
--                      --                    TO_CHAR(Get_business_date((TRUNC(NVL(POL_BO_DEBIT_DATE,
--                      --                                                         ENDR_BO_START_DATE)) - 1),
--                      --                                              1),
--                      --                            'MMRRRR'),
--                      --                    Get_business_date((TRUNC(NVL(POL_BO_DEBIT_DATE,
--                      --                                                 ENDR_BO_START_DATE)) - 1),
--                      --                                      1)) =
--                      --             DECODE(NVL(v_day_month_param, 'D'),
--                      --                    'M',
--                      --                    TO_CHAR(Get_business_date(TRUNC(v_date), 1), 'MMRRRR'),
--                      --                    Get_business_date(TRUNC(v_date), 1))

--                      --AND Get_business_date((TRUNC(NVL(POL_BO_DEBIT_DATE,ENDR_BO_START_DATE))-1),1) = Get_business_date(TRUNC(v_date),1)
--                      --         AND CLNT_CODE = v_clnt_code
--                      --         and NVL(ENDR_BANK_ACC_NO, CLNT_BANK_ACC_NO) = v_accno
--                      AND v_sys_code = 27
--                      AND NVL (check_if_dd_generated (clnt_code,
--                                                      clnt_bbr_code,
--                                                      v_date,
--                                                      pol_code,
--                                                      NULL,
--                                                      'PL'
--                                                     ),
--                               'Y'
--                              ) = 'Y'
--                   UNION
--                   SELECT NULL, ppr_code, prp_code, clnt_code,
--                          ppr_proposal_no, NULL, 'MKT',
--                          NVL (ppr_prem_amt, 0) + NVL (ppr_mipp_contr, 0),
--                             'PAID UPTO '
--                          || TO_CHAR (ppr_paid_date, 'DD/MM/RRRR') remarks,
--                          NVL (ppr_bo_debit_date, ppr_bo_start_date),
--                          ppr_bo_start_date, 'PR', ppr_effective_date duedate,
--                          ppr_freq_of_pymt,
--                          NVL (ppr_bank_acc_no,
--                               clnt_bank_acc_no
--                              ) clnt_bank_acc_no,
--                          clnt_sht_desc,
--                          clnt_name || ' ' || clnt_other_names client,
--                          ppr_bbr_code, bnk_code, a.bbr_branch_name,
--                          a.bbr_sht_desc, a.bbr_ref_code, NULL dd_book_date,
--                          NULL ddh_code, NULL ddh_dd_code,
--                          NVL (ppr_acc_holder,
--                               clnt_name || ' ' || clnt_other_names
--                              ) ppr_acc_holder,
--                          NULL os_instalments
--                     FROM lms_pol_proposals p1,
--                          tqc_clients,
--                          tqc_banks,
--                          tqc_bank_branches a,
--                          lms_proposers,
--                          tqc_bank_branches b
--                    WHERE prp_clnt_code = clnt_code
--                      AND ppr_prp_code = prp_code
--                      AND ppr_bbr_code = a.bbr_code
--                      AND bnk_forwarding_bnk_code = b.bbr_bnk_code
--                      AND b.bbr_code = v_bbr_code
--                      --AND BNK_FORWARDING_BNK_CODE IN (SELECT BBR_BNK_CODE FROM TQC_BANK_BRANCHES WHERE BBR_CODE = v_bbr_code )
--                      AND a.bbr_bnk_code = bnk_code
--                      AND NVL (ppr_pymt_mode, 'C') = 'DD'
--                      AND ppr_bo_start_date IS NOT NULL
--                      AND validate_debit (TRUNC (NVL (ppr_bo_debit_date,
--                                                      ppr_bo_start_date
--                                                     )
--                                                ),
--                                          v_date
--                                         ) = 'Y'
--                      --         AND DECODE(NVL(v_day_month_param, 'D'),
--                      --                    'M',
--                      --                    TO_CHAR(Get_business_date((TRUNC(PPR_BO_START_DATE) - 1),
--                      --                                              1),
--                      --                            'MMRRRR'),
--                      --                    Get_business_date((TRUNC(PPR_BO_START_DATE) - 1), 1)) =
--                      --             DECODE(NVL(v_day_month_param, 'D'),
--                      --                    'M',
--                      --                    TO_CHAR(Get_business_date(TRUNC(v_date), 1), 'MMRRRR'),
--                      --                    Get_business_date(TRUNC(v_date), 1))
--                      AND NOT ppr_proposal_no IN (
--                             SELECT pol_proposal_no
--                               FROM lms_policies, lms_pol_proposals p2
--                              WHERE pol_proposal_no = p2.ppr_proposal_no
--                                AND p1.ppr_proposal_no = p2.ppr_proposal_no)
--                      --         AND CLNT_CODE = v_clnt_code
--                      --         and NVL(PPR_BANK_ACC_NO, CLNT_BANK_ACC_NO)= v_accno
--                      AND v_sys_code = 27
--                      AND NVL (check_if_dd_generated (clnt_code,
--                                                      clnt_bbr_code,
--                                                      v_date,
--                                                      NULL,
--                                                      ppr_code,
--                                                      'PR'
--                                                     ),
--                               'Y'
--                              ) = 'Y'
--                   UNION
--                   SELECT lna_no, NULL ppr_code, prp_code, clnt_code,
--                          pol_proposal_no, pol_policy_no,
--                          DECODE (pol_status, 'V', 'PR', pol_type) pol_type,
--                          lna_installment_amount tot_instlmt,
--                             'PAID UPTO '
--                          || TO_CHAR (lnb_wef, 'DD/MM/RRRR') remarks,
--                          NULL, TRUNC (ADD_MONTHS (lnb_wef, 1)), 'LN',
--                          TRUNC (ADD_MONTHS (lnb_wef, 1)), 'M',
--                          lna_rpymt_acc_no clnt_bank_acc_no, clnt_sht_desc,
--                          clnt_name || ' ' || clnt_other_names client,
--                          NVL (lna_rpymt_bbr_code,
--                               endr_bbr_code
--                              ) lna_rpymt_bbr_code,
--                          bnk_code, a.bbr_branch_name, a.bbr_sht_desc,
--                          a.bbr_ref_code, NULL dd_book_date, NULL ddh_code,
--                          NULL ddh_dd_code,
--                          NVL (lna_rpymt_acc_holder,
--                               clnt_name || ' ' || clnt_other_names
--                              ) acc_holder,
--                          NULL os_instalments
--                     FROM lms_policies,
--                          tqc_clients,
--                          tqc_banks,
--                          tqc_bank_branches a,
--                          lms_proposers,
--                          lms_policy_endorsements,
--                          lms_loan_applications,
--                          lms_policy_loans,
--                          lms_loan_balances,
--                          tqc_bank_branches b
--                    WHERE prp_clnt_code = clnt_code
--                      AND pol_prp_code = prp_code
--                      AND pln_lna_no = lna_no
--                      AND lnb_pln_lna_no = pln_lna_no
--                      AND lna_pol_code = pol_code
--                      AND lnb_current = 'Y'
--                      AND lna_status = 'I'
--                      AND NVL (lna_rpymt_bbr_code, endr_bbr_code) = a.bbr_code
--                      AND bnk_forwarding_bnk_code = b.bbr_bnk_code
--                      AND b.bbr_code = v_bbr_code
--                      --AND BNK_FORWARDING_BNK_CODE IN (SELECT BBR_BNK_CODE FROM TQC_BANK_BRANCHES WHERE BBR_CODE = v_bbr_code )
--                      AND a.bbr_bnk_code = bnk_code
--                      AND pol_current_endr_code = endr_code
--                      AND NVL (lna_pay_method, 'C') = 'DD'
--                      AND NVL (pln_os_bal_amount, 0) > 0
--                      --AND Lms_Ord_Misc.Check_claim(POL_CODE) <> 'Y'
--                      AND lms_ord_misc.check_unauth_surrender (pol_code) <>
--                                                                           'Y'
--                      AND validate_debit (TRUNC (NVL (lna_date_issued,
--                                                      lna_date
--                                                     )
--                                                ),
--                                          v_date
--                                         ) = 'Y'
--                         --AND Get_business_date((TRUNC(NVL(POL_BO_DEBIT_DATE,ENDR_BO_START_DATE))-1),1) = Get_business_date(TRUNC(v_date),1)
--                      /*AND DECODE(NVL(v_day_month_param, 'D'),
--                                 'M',
--                                 TO_CHAR(Get_business_date((TRUNC(ADD_MONTHS(LNB_WEF,DECODE(LNA_LOAN_FREQ_OF_PAYMENT,'M',1,'Q',3,'S',6,12))) - 1),
--                                                           1),
--                                         'MMRRRR'),
--                                 Get_business_date((TRUNC(ADD_MONTHS(LNB_WEF,DECODE(LNA_LOAN_FREQ_OF_PAYMENT,'M',1,'Q',3,'S',6,12))) - 1),
--                                                   1)) =
--                          DECODE(NVL(v_day_month_param, 'D'),
--                                 'M',
--                                 TO_CHAR(Get_business_date(TRUNC(v_date), 1), 'MMRRRR'),
--                                 Get_business_date(TRUNC(v_date), 1))*/
--                      AND NVL (check_if_dd_generated (clnt_code,
--                                                      clnt_bbr_code,
--                                                      v_date,
--                                                      lna_no,
--                                                      NULL,
--                                                      'LN'
--                                                     ),
--                               'Y'
--                              ) = 'Y'
--                      AND v_sys_code = 27
--                   --          AND CLNT_CODE = v_clnt_code
--                   --          and  LNA_RPYMT_ACC_NO = v_accno
--                         --AND NVL(check_if_dd_generated(CLNT_CODE,CLNT_BBR_CODE,v_date),'Y') = 'N'
--                   UNION
--                   SELECT ddd_pol_code, ddd_pol_prp_code, ddd_ppr_code,
--                          ddh_clnt_code, ddd_pol_proposal_no,
--                          ddd_pol_policy_no, ddd_pol_type, ddd_amount,
--                          ddd_remarks, NULL pol_bo_debit_date, ddd_start_dt,
--                          ddd_type, ddd_prem_due_date, ddd_pol_freq_pymnt,
--                          TO_CHAR (ddh_clnt_bank_acc_no) clnt_bank_acc_no,
--                          ddh_clnt_sht_desc, ddh_clnt_name, ddh_clnt_bbr_code,
--                          ddh_bbr_bnk_code, ddh_bbr_branch_name,
--                          ddh_bbr_sht_desc, ddh_bbr_ref_code, dd_book_date,
--                          ddh_code, ddh_dd_code,
--                          NVL (ddh_acc_holder, ddh_clnt_name) ddh_acc_holder,
--                          NULL os_instalments
--                     FROM tqc_direct_debit_detail,
--                          tqc_direct_debit_header,
--                          tqc_direct_debit
--                    WHERE ddd_ddh_code = ddh_code
--                      --and  TO_CHAR(DDH_CLNT_BANK_ACC_NO)= v_accno
--                      --AND DDH_CLNT_CODE = v_clnt_code
--                      AND ddh_dd_code = dd_code
--                      AND ddd_dd_code = dd_code
--                      AND dd_bbr_code = v_bbr_code
--                      AND ddh_status = 'R'
--                      AND v_sys_code = 27)
--         ORDER BY clnt_code, clnt_bank_acc_no, clnt_bbr_code;

--      v_dd_code           NUMBER;
--      v_ddh_code          NUMBER;
--      v_ddd_code          NUMBER;
--      ntotalamount        NUMBER       := 0;
--      v_cnt1              NUMBER;
--      v_cnt2              NUMBER;
--      v_cnt               NUMBER;
--   BEGIN
--      v_day_month_param :=
--         NVL
--            (tqc_parameters_pkg.get_param_varchar ('USE_BOOKING_DAY_OR_MONTH'),
--             'D'
--            );
--      v_fut_debits :=
--         NVL
--            (tqc_parameters_pkg.get_param_varchar ('DD_GENERATE_FUTURE_DEBITS'),
--             'N'
--            );

--      --RAISE_ERROR('DONE TESTING '||v_bbr_code||'; v_acc_no='||v_acc_no||';v_date='||v_date);
--      FOR co IN company
--      LOOP
--         BEGIN
--            SELECT tqc_dd_code_seq.NEXTVAL
--              INTO v_dd_code
--              FROM DUAL;

--            INSERT INTO tqc_direct_debit
--                        (dd_code, dd_ref_no,
--                         dd_book_date, dd_bnk_code, dd_bbr_code, dd_acnt_no,
--                         dd_date, dd_raised_by, dd_value_date
--                        )
--                 VALUES (v_dd_code, TO_CHAR (TRUNC (SYSDATE), 'RRRRMMDD'),
--                         (v_date
--                         ), co.bbr_bnk_code, co.bbr_code, co.bct_no,
--                         SYSDATE, v_user, get_business_date (v_date, 4)
--                        );
--         EXCEPTION
--            WHEN OTHERS
--            THEN
--               raise_error ('Error#1...');
--         END;

----      delete myCount;
----      insert into myCount (CNT, DD_CODE) values(0,v_dd_code);
--         FOR i IN clnt_dtls
--         LOOP
--            IF v_sys_code = 27
--            THEN
--               SELECT COUNT (1)
--                 INTO v_cnt
--                 FROM tqc_direct_debit_header
--                WHERE ddh_dd_code = v_dd_code
--                  AND ddh_clnt_code = i.clnt_code
--                  AND ddh_clnt_bbr_code = i.clnt_bbr_code
--                  AND ddh_bbr_bnk_code = i.bnk_code
--                  AND ddh_clnt_bank_acc_no = i.clnt_bank_acc_no;

--               IF NVL (v_cnt, 0) = 0
--               THEN
--                  ntotalamount := 0;

--                  BEGIN
--                     SELECT tqc_ddh_code_seq.NEXTVAL
--                       INTO v_ddh_code
--                       FROM DUAL;

--                     INSERT INTO tqc_direct_debit_header
--                                 (ddh_code, ddh_dd_code, ddh_clnt_code,
--                                  ddh_clnt_bbr_code, ddh_bbr_bnk_code,
--                                  ddh_clnt_sht_desc, ddh_clnt_name,
--                                  ddh_clnt_bank_acc_no, ddh_bbr_branch_name,
--                                  ddh_bbr_sht_desc, ddh_bbr_ref_code,
--                                  ddh_tot_amt, ddh_initial_book_date,
--                                  ddh_prev_ddh_code, ddh_acc_holder
--                                 )
--                          VALUES (v_ddh_code, v_dd_code, i.clnt_code,
--                                  i.clnt_bbr_code, i.bnk_code,
--                                  i.clnt_sht_desc, i.client,
--                                  i.clnt_bank_acc_no, i.bbr_branch_name,
--                                  i.bbr_sht_desc, i.bbr_ref_code,
--                                  0, i.dd_book_date,
--                                  i.ddh_code, i.acc_holder
--                                 );
--                  EXCEPTION
--                     WHEN OTHERS
--                     THEN
--                        raise_error ('Error#2...');
--                  END;
--               ELSE
--                  BEGIN
--                     SELECT ddh_code
--                       INTO v_ddh_code
--                       FROM tqc_direct_debit_header
--                      WHERE ddh_dd_code = v_dd_code
--                        AND ddh_clnt_code = i.clnt_code
--                        AND ddh_clnt_bbr_code = i.clnt_bbr_code
--                        AND ddh_bbr_bnk_code = i.bnk_code
--                        AND ddh_clnt_bank_acc_no = i.clnt_bank_acc_no;
--                  EXCEPTION
--                     WHEN OTHERS
--                     THEN
--                        raise_error
--                            ('Error fetching the Direct Debit header details');
--                  END;
--               END IF;

--               --        update myCount set CNT=nvl(CNT,0)+1;
--               --        commit;
--               v_dd_cnt := 0;

----            IF D.POL_POLICY_NO = '12062862' THEN
----                Raise_Error('POL_POLICY_NO='||D.POL_POLICY_NO||';OS_INSTALMENTS='||D.OS_INSTALMENTS);
----            END IF;
--               WHILE v_dd_cnt <= NVL (i.os_instalments, 0) OR v_dd_cnt = 0
--               LOOP
--                  v_dd_cnt := v_dd_cnt + 1;

--                  BEGIN
--                     SELECT tqc_ddd_code_seq.NEXTVAL
--                       INTO v_ddd_code
--                       FROM DUAL;

--                     INSERT INTO tqc_direct_debit_detail
--                                 (ddd_code, ddd_ddh_code, ddd_dd_code,
--                                  ddd_sys_code, ddd_pol_code,
--                                  ddd_pol_prp_code, ddd_pol_proposal_no,
--                                  ddd_pol_policy_no, ddd_amount, ddd_remarks,
--                                  ddd_start_dt, ddd_stop_date, ddd_ppr_code,
--                                  ddd_pol_type, ddd_type, ddd_prem_due_date,
--                                  ddd_pol_freq_pymnt, ddd_rec_no
--                                 )
--                          VALUES (v_ddd_code, v_ddh_code, v_dd_code,
--                                  v_sys_code, i.pol_code,
--                                  i.prp_code, i.pol_proposal_no,
--                                  i.pol_policy_no, i.tot_instlmt, i.remarks,
--                                  i.endr_bo_start_date, NULL, i.ppr_code,
--                                  i.pol_type, i.pay_type, i.duedate,
--                                  i.pol_freq_of_payment, v_dd_cnt
--                                 );

--                     ntotalamount := ntotalamount + NVL (i.tot_instlmt, 0);
--                  EXCEPTION
--                     WHEN OTHERS
--                     THEN
--                        raise_error ('Error#3...');
--                  END;

--                  UPDATE tqc_direct_debit_header
--                     SET ddh_tot_amt = ntotalamount
--                   WHERE ddh_code = v_ddh_code;

--                  EXIT WHEN v_dd_cnt >= NVL (i.os_instalments, 0);
--               END LOOP;
--            ELSE
--               raise_error (   'System code '
--                            || v_sys_code
--                            || ' not provided for..2..'
--                           );
--            END IF;
--         END LOOP;

--         FOR c IN clnt
--         LOOP
--            BEGIN
--               SELECT COUNT (1)
--                 INTO v_cnt1
--                 FROM tqc_direct_debit_header
--                WHERE ddh_dd_code = (SELECT ddh_dd_code
--                                       FROM tqc_direct_debit_header
--                                      WHERE ddh_code = c.ddh_code);
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  raise_error ('Child records not found....');
--            END;

--            BEGIN
--               SELECT COUNT (1)
--                 INTO v_cnt2
--                 FROM tqc_direct_debit_header
--                WHERE ddh_dd_code = (SELECT ddh_dd_code
--                                       FROM tqc_direct_debit_header
--                                      WHERE ddh_code = c.ddh_code)
--                  AND ddh_status = 'R';
--            EXCEPTION
--               WHEN OTHERS
--               THEN
--                  raise_error ('Child records not found....');
--            END;

--            IF NVL (v_cnt1, 0) = NVL (v_cnt2, 0) AND NVL (v_cnt1, 0) > 0
--            THEN
--               BEGIN
--                  UPDATE tqc_direct_debit
--                     SET dd_receipted = 'F'
--                   WHERE dd_code = c.ddh_dd_code;
--               EXCEPTION
--                  WHEN OTHERS
--                  THEN
--                     raise_error ('Update DD error...');
--               END;
--            END IF;

--            BEGIN
--               UPDATE tqc_direct_debit_header
--                  SET ddh_status = 'F'
--                WHERE ddh_code = c.ddh_code;
--            END;

--            BEGIN
--               UPDATE tqc_direct_debit_detail
--                  SET ddd_status = 'F'
--                WHERE ddd_ddh_code = c.ddh_code;
--            END;
--         END LOOP;
--      END LOOP;
--   --RAISE_ERROR('DONE TESTING '||v_bbr_code);
--   --RAISE_ERROR('DONE v_sys_code='||v_sys_code||';'||v_bbr_code||';'||v_acc_no||';'||v_user||';'||v_date);
--   END create_direct_debit_recslms;

   PROCEDURE update_bo_date (v_dd_code NUMBER)
   IS
      CURSOR bo
      IS
         SELECT ddd_sys_code, ddd_pol_code,
                DECODE (endr_freq_of_payment,
                        'M', 1,
                        'Q', 3,
                        'S', 6,
                        'A', 12
                       ) freq,
                ddd_ppr_code, ddd_pol_type
           FROM tqc_direct_debit_detail,
                lms_policies,
                lms_policy_endorsements
          WHERE ddd_pol_code = pol_code
            AND pol_current_endr_code = endr_code
            AND ddd_dd_code = v_dd_code
         UNION
         SELECT ddd_sys_code, ddd_pol_code,
                DECODE (ppr_freq_of_pymt,
                        'M', 1,
                        'Q', 3,
                        'S', 6,
                        'A', 12
                       ) freq,
                ddd_ppr_code, ddd_pol_type
           FROM tqc_direct_debit_detail, lms_pol_proposals
          WHERE ddd_ppr_code = ppr_code AND ddd_dd_code = v_dd_code;
   BEGIN
      FOR b IN bo
      LOOP
         IF b.ddd_pol_type IN ('PR', 'PL')
         THEN
            UPDATE lms_policies
               SET pol_bo_debit_date = ADD_MONTHS (pol_bo_debit_date, b.freq)
             WHERE pol_code = b.ddd_pol_code
               AND pol_bo_debit_date <= ADD_MONTHS (pol_paid_to_date, b.freq);
         ELSIF b.ddd_pol_type IN ('MKT')
         THEN
            UPDATE lms_pol_proposals
               SET ppr_bo_debit_date =
                      ADD_MONTHS (NVL (ppr_bo_debit_date, ppr_bo_start_date),
                                  b.freq
                                 )
             WHERE ppr_code = b.ddd_ppr_code;
         ELSE
            raise_error ('Policy Type undefined....');
         END IF;
      END LOOP;
   END update_bo_date;

   PROCEDURE delete_dd_detail (v_ddd_code NUMBER)
   IS
      v_cnt        NUMBER;
      v_dd_code    NUMBER;
      v_ddh_code   NUMBER;
   BEGIN
      BEGIN
         SELECT ddd_ddh_code, ddd_dd_code
           INTO v_ddh_code, v_dd_code
           FROM tqc_direct_debit_detail
          WHERE ddd_code = v_ddd_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Parent record not found..1..');
      END;

      BEGIN
         v_cnt := 0;

         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_direct_debit_detail
          WHERE ddd_ddh_code = v_ddh_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error counting records....');
      END;

      IF NVL (v_cnt, 0) = 0
      THEN
         raise_error ('Parent record not found..2..');
      ELSIF NVL (v_cnt, 0) = 1
      THEN
         DELETE      tqc_direct_debit_detail
               WHERE ddd_code = v_ddd_code;

         DELETE      tqc_direct_debit_header
               WHERE ddh_code = v_ddh_code;
      ELSE
         DELETE      tqc_direct_debit_detail
               WHERE ddd_code = v_ddd_code;

         UPDATE tqc_direct_debit_header
            SET ddh_tot_amt = (SELECT SUM (ddd_amount)
                                 FROM tqc_direct_debit_detail
                                WHERE ddd_ddh_code = v_ddh_code)
          WHERE ddh_code = v_ddh_code;
      END IF;

      BEGIN
         v_cnt := 0;

         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_direct_debit_header
          WHERE ddh_dd_code = v_dd_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Parent record not found..3..');
      END;

      IF NVL (v_cnt, 0) = 0
      THEN
         DELETE      tqc_direct_debit
               WHERE dd_code = v_dd_code;
      END IF;
   END delete_dd_detail;

   PROCEDURE Generate_Receipts(v_dd_code NUMBER, v_Username VARCHAR2) IS
    CURSOR c_Direct_Debit_Batch IS
      SELECT DD_VALUE_DATE,
             DD_ACNT_NO,
             DD_CODE,
             DD_REF_NO,
             DD_BOOK_DATE,
             DD_BNK_CODE,
             DD_BBR_CODE,
             DD_STATUS,
             DD_RECEIPTED,
             DD_RAISED_BY,
             DD_AUTH_BY
        FROM TQC_DIRECT_DEBIT
       WHERE DD_CODE = v_dd_code
         AND DD_STATUS = 'A';
    CURSOR c_Direct_Debit_Details(vDirectDebitCode NUMBER) IS
      SELECT *
        FROM TQC_DIRECT_DEBIT_DETAIL, TQC_DIRECT_DEBIT_HEADER
       WHERE DDH_CODE = DDD_DDH_CODE
         AND DDH_STATUS = 'A'
         AND DDD_DD_CODE = vDirectDebitCode;
    vReceiptPK            NUMBER;
    v_account_type_code   NUMBER;
    vReceiptNumber        varchar2(50);
    vReceiptCode          VARCHAR2(20);
    v_sys_sht_desc        VARCHAR2(5);
    v_rct_dtl_rec         Fms_Rcts_Pkg.type_rct_dtl_table;
    v_rct_alloc_rec       Fms_Interfaces_Pkg.type_def_alloc_amts_table;
    v_rct_alloc_multi_rec Fms_Rcts_Pkg.type_rct_client_table;
    v_rct_charge_dtl_rec          fms_rcts_pkg.type_rct_charges_dtl_table;
    v_cur_code            NUMBER;
    v_rct_bct_code        NUMBER;
    v_rct_bct_brh_code    NUMBER;
    v_rct_brh_code        NUMBER; --BRANCH OR RECEIPTING
    v_brh_sht_desc        VARCHAR2(20);
    v_sys_code            NUMBER;
    v_rct_type            VARCHAR2(5);
    v_trnt_type           VARCHAR2(20);
    v_prod_code           NUMBER;
    v_trnt_code           VARCHAR2(20);
    v_trnt_gl_code        VARCHAR2(20);
    v_trnt_contra_gl_code VARCHAR2(20);
    v_POL_TYPE            VARCHAR2(5);
    v_org_code            NUMBER;
    v_lms_org_code        NUMBER;
    vNo                   NUMBER := 0;
    vNo2                  NUMBER := 0;
    v_rpt_id              NUMBER;
    v_org_name            VARCHAR2(50);
  BEGIN
    FOR DD IN c_Direct_Debit_Batch LOOP
      v_account_type_code := Tqc_Interfaces_Pkg.accountcode('D');
      v_cur_code          := 1;
      --v_rct_bct_code :=  dd.DD_BNK_CODE ;
      
      begin
        select SYS_ORG_CODE, ORG_NAME
          into v_lms_org_code, v_org_name
          from tqc_systems, tqc_organizations
         where SYS_SHT_DESC = 'GIS'
           and ORG_CODE = SYS_ORG_CODE;
      end;
    
      BEGIN
        SELECT BCT_BRH_CODE, BCT_CODE
          INTO v_rct_bct_brh_code, v_rct_bct_code
          FROM FMS_BNK_ACCTS
         WHERE BCT_NO = to_chaR(dd.DD_ACNT_NO)
           AND BCT_CRT_ACC_ORG_CODE = v_lms_org_code;
      
      EXCEPTION
        when no_data_found then
          raise_error('Bank Account no -' || dd.DD_ACNT_NO ||
                      ' is not defined for system ' || v_org_name);
        WHEN OTHERS THEN
          raise_error('Rcpt branch...' || sqlerrm(sqlcode) || '   ' ||
                      dd.DD_ACNT_NO);
      END;
    raise_error('ssssssssssss');
      --RAISE_ERROR('Rcpt branch...'||v_rct_bct_code||';'||v_rct_bct_brh_code||';'||dd.DD_ACNT_NO);
    
      v_rct_brh_code := v_rct_bct_brh_code;
      v_sys_code     := Fms_Interfaces_Pkg.systemcode('LMS');
      v_cur_code     := Lms_Interfaces_Pkg.Lms_Def_Cur(v_rct_brh_code);
      v_org_code     := fms_interfaces_pkg.get_org(v_rct_brh_code);
      --raise_error('v_org_code='||v_org_code);
      FOR dd_detail IN c_Direct_Debit_Details(dd.DD_CODE) LOOP
        v_rct_dtl_rec.DELETE;
        v_rct_alloc_rec.DELETE;
        v_rct_alloc_multi_rec.DELETE;
        vNo            := 0;
        vNo2           := 0;
        v_rct_type     := 'O'; --(O)RDINARY, (G)roup,  DBT(DEBIT NOTES)
        v_sys_sht_desc := Tqc_Interfaces_Pkg.systemname(dd_detail.DDD_SYS_CODE,
                                                        'S'); --RETURNS LMS/GIS/MED...
        v_pol_type     := lms_interfaces_pkg.POL_TYPE(NVL(dd_detail.DDD_POL_CODE,
                                                          dd_detail.DDD_PPR_CODE),
                                                      NVL(dd_detail.DDD_POL_POLICY_NO,
                                                          dd_detail.DDD_POL_PROPOSAL_NO));
        v_prod_code    := TQ_LMS.LMS_API.Get_Prod_Code(NVL(dd_detail.DDD_POL_CODE,
                                                           dd_detail.DDD_PPR_CODE),
                                                       v_POL_TYPE);
        IF v_pol_type IN ('MPR', 'PR') THEN
          v_trnt_type := 'PRRCT';
        ELSIF v_POL_TYPE = 'PL' THEN
          v_trnt_type := 'UP';
        ELSIF v_POL_TYPE = 'LN' THEN
          v_trnt_type :='NL';
        ELSE
          raise_error('Unknown POL TYPE FROM LMS ' || v_POL_TYPE);
        END IF;
        --GET THE GL ACCOUNT FROM LMS THAT IS TO BE CREDITED
        TQ_LMS.LMS_API.Get_Gl_Code(v_trnt_type,
                                   v_prod_code,
                                   v_trnt_code,
                                   v_trnt_gl_code,
                                   v_trnt_contra_gl_code);
        TQ_LMS.LMS_API.valid_acc(v_trnt_gl_code,
                                 v_org_code,
                                 v_rct_brh_code,
                                 v_trnt_type);
        --v_rct_dtl_rec IS USED TO CREATE THE CB DETAIL REC THAT HAS THE CREDIT SIDE
        --GOINT TO AN ACCOUNT IN v_trnt_gl_code;
        vNo := vNo + 1;
        v_rct_dtl_rec(vNo).RCD_ACC_NO := v_trnt_gl_code;
        v_rct_dtl_rec(vNo).RCD_SRV_NO := NVL(dd_detail.DDD_POL_CODE,
                                             dd_detail.DDD_PPR_CODE);
        v_rct_dtl_rec(vNo).RCD_AMOUNT := dd_detail.DDD_AMOUNT;
        v_rct_dtl_rec(vNo).RCD_NARRATION := 'Premium - DIRECT DEBIT';
        v_rct_dtl_rec(vNo).RCD_ACC_TYPE := NULL;
        v_rct_dtl_rec(vNo).RCD_MTRAN_NO := NULL;
        v_rct_dtl_rec(vNo).RCD_REF_NO := NVL(dd_detail.DDD_POL_POLICY_NO,
                                             dd_detail.DDD_POL_PROPOSAL_NO);
        v_rct_dtl_rec(vNo).RCD_ACT_CODE := NULL;
        v_rct_dtl_rec(vNo).RCD_ALLOCATED := 'Y';
        v_rct_dtl_rec(vNo).RCD_INC_COMM := 'N';
        v_rct_dtl_rec(vNo).RCD_COMM_AMOUNT := 0;
      
        --CREATE ALLOC RECORDS LOAN PENS AND D.SURE ARE ZERO FOR NOW IN DIRECT DEBIT
        --THIS ALLOC REC IS USED BY LMS WHEN POSTING THE RECEIPT.
        vNo2 := vNo2 + 1;
        v_rct_alloc_rec(vNo2).pol_code := NVL(dd_detail.DDD_POL_CODE,
                                              dd_detail.DDD_PPR_CODE);
        v_rct_alloc_rec(vNo2).pol_policy_no := NVL(dd_detail.DDD_POL_POLICY_NO,
                                                   dd_detail.DDD_POL_PROPOSAL_NO);
        IF dd_detail.DDD_TYPE = 'LN' THEN 
            v_rct_alloc_rec(vNo2).pol_loan_alloc := dd_detail.DDD_AMOUNT;
            v_rct_alloc_rec(vNo2).pol_prem_alloc := 0;
        ELSE
            v_rct_alloc_rec(vNo2).pol_prem_alloc := dd_detail.DDD_AMOUNT;
            v_rct_alloc_rec(vNo2).pol_loan_alloc := 0;
        END IF;
        --v_rct_alloc_rec(vNo2).pol_loan_alloc := 0; --NVL(:O_LIFE_TRANS.POL_LOAN_ALLOC,to_number(null));
        v_rct_alloc_rec(vNo2).pol_pension_alloc := 0;
        v_rct_alloc_rec(vNo2).double_sure_alloc := 0;
        BEGIN
          SELECT RPT_ID
            INTO v_rpt_id
            FROM FMS_RECEIPTING_POINTS
           WHERE RPT_NAME = 'DIRECTDEBITS';
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            RAISE_ERROR('Receipting point DIRECTDEBITS is not defined.....');
        END;
        vReceiptNumber := Fms_Rcts_Pkg.get_rct_num(v_rpt_id);
      
        v_brh_sht_desc := Tqc_Interfaces_Pkg.branchname(v_rct_brh_code, 'S');
        vReceiptCode   := FMS_RCTS_PKG.Brh_Rct_Code(v_sys_sht_desc,
                                                    v_brh_sht_desc,
                                                    vReceiptNumber);
        --raise_error('ggg');
        --TQ_FMS.pkg_global_vars.SET_PVAR('pkg_global_vars.pvg_username', v_Username);
        FMS_RCTS_PKG.create_web_rct(NVL(dd.DD_VALUE_DATE, SYSDATE), --v_rct_date           DATE,
                                Tqc_Interfaces_Pkg.usercode(v_Username), --Tqc_Interfaces_Pkg.usercode( dd.DD_AUTH_BY)  ,--v_rct_captured_by    NUMBER,
                                dd_detail.DDD_AMOUNT, --v_rct_amount         NUMBER,
                                'DD', --v_rct_paymt_mode     VARCHAR2,
                                dd.DD_REF_NO, --v_rct_paymt_memo     VARCHAR2,
                                'SYSTEM - DIRECT DEBIT', --v_rct_paid_by        VARCHAR2,
                                NULL, --v_rct_doc_date       DATE,
                                'DIRECT DEBIT', --v_rct_desc           VARCHAR2,
                                'N', --v_rct_printed        CHAR,
                                dd_detail.DDD_SYS_CODE, --v_rct_app            NUMBER,
                                v_account_type_code, --v_rct_act_code       NUMBER,
                                'D', --v_rct_acc_type       CHAR,
                                v_rct_brh_code, --v_rct_brh_code       NUMBER,
                                NULL, --v_rct_act_sht_desc   VARCHAR2,
                                v_cur_code, --v_rct_cur_code       NUMBER,
                                v_rct_bct_code, --v_rct_bct_code       NUMBER,
                                v_rct_bct_brh_code, --v_rct_bct_brh_code   NUMBER,
                                'N', --v_rct_cb_posted      CHAR,
                                'N', -- v_upd_status         CHAR DEFAULT 'N',
                                vReceiptNumber, --v_rct_brh_rct_no     NUMBER,
                                vReceiptCode, --v_rct_brh_rct_code   VARCHAR2,
                                NULL, --v_rct_drawers_bank   VARCHAR2,
                                'D', --v_rct_acct_type_id   VARCHAR2,
                                v_rct_type, --v_rct_type           VARCHAR2,
                                dd.DD_REF_NO, --v_rct_ref            VARCHAR2,
                                'G', --v_rct_Net_Gross      VARCHAR2 DEFAULT 'G',
                                NULL, --v_rct_GL_Account      VARCHAR2 DEFAULT NULL,
                                NULL, --v_rct_pol_type        VARCHAR2 DEFAULT NULL,
                                v_sys_sht_desc, --v_sys_sht_desc       VARCHAR2  ,
                                NULL, --v_rct_point_id       NUMBER DEFAULT NULL,
                                NULL, --v_Auto_Manual        VARCHAR2 DEFAULT NULL,
                                'N',
                                v_rct_dtl_rec, --v_rct_dtl_rec Fms_Rcts_Pkg.type_rct_dtl_table,
                                v_rct_alloc_rec, --v_rct_alloc_rec Fms_Interfaces_Pkg.type_def_alloc_amts_table,
                                v_rct_alloc_multi_rec, --v_rct_alloc_multi_rec Fms_Rcts_Pkg.type_rct_client_table,
                                vReceiptPK, --v_rct_no   OUT NUMBER,
                                NULL,--v_agn_no              NUMBER DEFAULT NULL,
                                NULL,--v_received_from       VARCHAR2 DEFAULT NULL,
                                NULL,--v_cac_code          NUMBER DEFAULT NULL,
                                v_rct_charge_dtl_rec,--          fms_rcts_pkg.type_rct_charges_dtl_table,
                                'N',--v_multi_org            VARCHAR2 DEFAULT 'N',
                                FMS_GEN_PKG.GET_ORG(v_rct_brh_code),--v_source_org           NUMBER,
                                dd_detail.DDD_AMOUNT--v_multi_org_amt        NUMBER  
                                );
      
        Fms_Rcts_Pkg.UPDATE_RCT_OS_AMOUNT(vReceiptPK);
      
        Fms_Rcts_Pkg.UPDATE_SOURCE(vReceiptPK,
                                   v_sys_sht_desc,
                                   Tqc_Interfaces_Pkg.usercode(v_Username),
                                   'N',
                                   'N');
        --raise_error('ggg');
        UPDATE TQC_DIRECT_DEBIT_DETAIL
           SET DDD_RECEIPT_NO     = vReceiptCode,
               DDD_RECEIPT_DATE   = NVL(dd.DD_VALUE_DATE, SYSDATE),
               DDD_RECEIPTED_DATE = SYSDATE,
               DDD_RECEIPTED_BY   = v_Username,
               DDD_RECEIPTED      = 'Y'
         WHERE DDD_CODE = dd_detail.DDD_CODE;
      END LOOP;
    
      UPDATE TQC_DIRECT_DEBIT_HEADER
         SET DDH_RECEIPTED = 'Y'
       WHERE DDH_DD_CODE = v_DD_CODE
         AND DDH_STATUS = 'A';
    
      UPDATE TQC_DIRECT_DEBIT TQC_DIRECT_DEBIT_VIEW
         SET DD_RECEIPTED = 'Y'
       WHERE DD_CODE = v_DD_CODE;
    END LOOP;
    
  EXCEPTION
    WHEN OTHERS THEN
--      Raise_error('Q..');
      Raise_error('Generate_Receipts Error..');
  END Generate_Receipts;
 PROCEDURE generate_receipts_old (v_dd_code NUMBER, v_username VARCHAR2)
   IS
      CURSOR c_direct_debit_batch
      IS
         SELECT dd_value_date, dd_acnt_no, dd_code, dd_ref_no, dd_book_date,
                dd_bnk_code, dd_bbr_code, dd_status, dd_receipted,
                dd_raised_by, dd_auth_by
           FROM tqc_direct_debit
          WHERE dd_code = v_dd_code AND dd_status = 'A';

      CURSOR c_direct_debit_details (vdirectdebitcode NUMBER)
      IS
         SELECT *
           FROM tqc_direct_debit_detail, tqc_direct_debit_header
          WHERE ddh_code = ddd_ddh_code
            AND ddh_status = 'A'
            AND ddd_dd_code = vdirectdebitcode;

      vreceiptpk              NUMBER;
      v_account_type_code     NUMBER;
      vreceiptnumber          VARCHAR2 (50);
      vreceiptcode            VARCHAR2 (20);
      v_sys_sht_desc          VARCHAR2 (5);
      v_rct_dtl_rec           fms_rcts_pkg.type_rct_dtl_table;
      v_rct_alloc_rec         fms_interfaces_pkg.type_def_alloc_amts_table;
      v_rct_alloc_multi_rec   fms_rcts_pkg.type_rct_client_table;
      v_rct_charge_dtl_rec    fms_rcts_pkg.type_rct_charges_dtl_table;
      v_cur_code              NUMBER;
      v_rct_bct_code          NUMBER;
      v_rct_bct_brh_code      NUMBER;
      v_rct_brh_code          NUMBER;                   --BRANCH OR RECEIPTING
      v_brh_sht_desc          VARCHAR2 (20);
      v_sys_code              NUMBER;
      v_rct_type              VARCHAR2 (5);
      v_trnt_type             VARCHAR2 (20);
      v_prod_code             NUMBER;
      v_trnt_code             VARCHAR2 (20);
      v_trnt_gl_code          VARCHAR2 (20);
      v_trnt_contra_gl_code   VARCHAR2 (20);
      v_pol_type              VARCHAR2 (5);
      v_org_code              NUMBER;
      v_lms_org_code          NUMBER;
      vno                     NUMBER                                      := 0;
      vno2                    NUMBER                                      := 0;
      v_rpt_id                NUMBER;
      v_org_name              VARCHAR2 (50);
      v_rcp_point_name        VARCHAR2 (50);
   BEGIN
      FOR dd IN c_direct_debit_batch
      LOOP
         v_account_type_code := tqc_interfaces_pkg.accountcode ('D');
         v_cur_code := 1;

         --v_rct_bct_code := dd.DD_BNK_CODE ;
         BEGIN
            SELECT sys_org_code, org_name
              INTO v_lms_org_code, v_org_name
              FROM tqc_systems, tqc_organizations
             WHERE sys_sht_desc = 'LMS' AND org_code = sys_org_code;
         END;

         BEGIN
            SELECT bct_brh_code, bct_code
              INTO v_rct_bct_brh_code, v_rct_bct_code
              FROM fms_bnk_accts
             WHERE bct_no = TO_CHAR (dd.dd_acnt_no)
               AND bct_crt_acc_org_code = v_lms_org_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error (   'Bank Account no -'
                            || dd.dd_acnt_no
                            || ' is not defined for system '
                            || v_org_name
                           );
            WHEN OTHERS
            THEN
               raise_error (   'Rcpt branch...'
                            || SQLERRM (SQLCODE)
                            || ' '
                            || dd.dd_acnt_no
                           );
         END;

         --RAISE_ERROR('Rcpt branch...'||v_rct_bct_code||';'||v_rct_bct_brh_code||';'||dd.DD_ACNT_NO);
         v_rct_brh_code := v_rct_bct_brh_code;
         v_sys_code := fms_interfaces_pkg.systemcode ('LMS');
         v_cur_code := lms_interfaces_pkg.lms_def_cur (v_rct_brh_code);
         v_org_code := fms_interfaces_pkg.get_org (v_rct_brh_code);

         --raise_error('v_org_code='||v_org_code);
         FOR dd_detail IN c_direct_debit_details (dd.dd_code)
         LOOP
            v_rct_dtl_rec.DELETE;
            v_rct_alloc_rec.DELETE;
            v_rct_alloc_multi_rec.DELETE;
            vno := 0;
            vno2 := 0;
            v_rct_type := 'O';        --(O)RDINARY, (G)roup, DBT(DEBIT NOTES)
            v_sys_sht_desc :=
                  tqc_interfaces_pkg.systemname (dd_detail.ddd_sys_code, 'S');
                                                     --RETURNS LMS/GIS/MED...
            v_pol_type :=
               lms_interfaces_pkg.pol_type
                                          (NVL (dd_detail.ddd_pol_code,
                                                dd_detail.ddd_ppr_code
                                               ),
                                           NVL (dd_detail.ddd_pol_policy_no,
                                                dd_detail.ddd_pol_proposal_no
                                               )
                                          );
            v_prod_code :=
               tq_lms.lms_api.get_prod_code (NVL (dd_detail.ddd_pol_code,
                                                  dd_detail.ddd_ppr_code
                                                 ),
                                             v_pol_type
                                            );
            /* commented out since we are supposed to credit direct receipt account which will be debited at the point of posting allocations*/
            -- IF v_pol_type IN ('MPR', 'PR') THEN
            -- v_trnt_type := 'PRRCT';
            -- ELSIF v_POL_TYPE = 'PL' THEN
            -- v_trnt_type := 'UP';
            -- ELSIF v_POL_TYPE = 'LN' THEN
            -- v_trnt_type :='NL';
            -- ELSE
            -- raise_error('Unknown POL TYPE FROM LMS ' || v_POL_TYPE);
            -- END IF;
            v_trnt_type := 'DR_RCT';
            --GET THE GL ACCOUNT FROM LMS THAT IS TO BE CREDITED
            tq_lms.lms_api.get_gl_code (v_trnt_type,
                                        v_prod_code,
                                        v_trnt_code,
                                        v_trnt_gl_code,
                                        v_trnt_contra_gl_code
                                       );
            tq_lms.lms_api.valid_acc (v_trnt_gl_code,
                                      v_org_code,
                                      v_rct_brh_code,
                                      v_trnt_type
                                     );
            --v_rct_dtl_rec IS USED TO CREATE THE CB DETAIL REC THAT HAS THE CREDIT SIDE
            --GOINT TO AN ACCOUNT IN v_trnt_gl_code;
            vno := vno + 1;
            v_rct_dtl_rec (vno).rcd_acc_no := v_trnt_gl_code;
            v_rct_dtl_rec (vno).rcd_srv_no :=
                          NVL (dd_detail.ddd_pol_code, dd_detail.ddd_ppr_code);
            v_rct_dtl_rec (vno).rcd_amount := dd_detail.ddd_amount;
            v_rct_dtl_rec (vno).rcd_narration := 'Premium - DIRECT DEBIT';
            v_rct_dtl_rec (vno).rcd_acc_type := NULL;
            v_rct_dtl_rec (vno).rcd_mtran_no := NULL;
            v_rct_dtl_rec (vno).rcd_ref_no :=
               NVL (dd_detail.ddd_pol_policy_no,
                    dd_detail.ddd_pol_proposal_no);
            v_rct_dtl_rec (vno).rcd_act_code := NULL;
            v_rct_dtl_rec (vno).rcd_allocated := 'Y';
            v_rct_dtl_rec (vno).rcd_inc_comm := 'N';
            v_rct_dtl_rec (vno).rcd_comm_amount := 0;
            --CREATE ALLOC RECORDS LOAN PENS AND D.SURE ARE ZERO FOR NOW IN DIRECT DEBIT
            --THIS ALLOC REC IS USED BY LMS WHEN POSTING THE RECEIPT.
            vno2 := vno2 + 1;
            v_rct_alloc_rec (vno2).pol_code :=
                          NVL (dd_detail.ddd_pol_code, dd_detail.ddd_ppr_code);
            v_rct_alloc_rec (vno2).pol_policy_no :=
               NVL (dd_detail.ddd_pol_policy_no,
                    dd_detail.ddd_pol_proposal_no);

            IF dd_detail.ddd_type = 'LN'
            THEN
               v_rct_alloc_rec (vno2).pol_loan_alloc := dd_detail.ddd_amount;
               v_rct_alloc_rec (vno2).pol_prem_alloc := 0;
            ELSE
               v_rct_alloc_rec (vno2).pol_prem_alloc := dd_detail.ddd_amount;
               v_rct_alloc_rec (vno2).pol_loan_alloc := 0;
            END IF;

            --v_rct_alloc_rec(vNo2).pol_loan_alloc := 0; --NVL(:O_LIFE_TRANS.POL_LOAN_ALLOC,to_number(null));
            v_rct_alloc_rec (vno2).pol_pension_alloc := 0;
            v_rct_alloc_rec (vno2).double_sure_alloc := 0;

            BEGIN
               SELECT rpt_id
                 INTO v_rpt_id
                 FROM fms_receipting_points
                WHERE rpt_name = 'DIRECTDEBITS';
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  raise_error
                         ('Receipting point DIRECTDEBITS is not defined.....');
            END;

            v_rcp_point_name := 'DIRECTDEBITS';

            BEGIN
               vreceiptnumber := fms_rcts_pkg.get_rct_num (v_rpt_id);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('at get_rct_num(i)');
            --Raise_error('Generate_Receipts..');
            END;

--raise_error('vReceiptNumber = '||vReceiptNumber);
            BEGIN
               v_brh_sht_desc :=
                          tqc_interfaces_pkg.branchname (v_rct_brh_code, 'S');
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('at branchname()');
            --Raise_error('Generate_Receipts..');
            END;

            --RAISE_ERROR('v_sys_sht_desc '||v_sys_sht_desc);
            BEGIN
               vreceiptcode :=
                  fms_rcts_pkg.brh_rct_code (v_sys_sht_desc,
                                             v_brh_sht_desc,
                                             vreceiptnumber,
                                             v_rcp_point_name
                                            );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('at brh_rct_code');
            --Raise_error('Generate_Receipts..');
            END;

            --raise_error('ggg');
            --TQ_FMS.pkg_global_vars.SET_PVAR('pkg_global_vars.pvg_username', v_Username);
            BEGIN
               fms_rcts_pkg.create_web_rct
                  (NVL (dd.dd_value_date, SYSDATE),         --v_rct_date DATE,
                   tqc_interfaces_pkg.usercode (v_username),
    --Tqc_Interfaces_Pkg.usercode( dd.DD_AUTH_BY) ,--v_rct_captured_by NUMBER,
                   dd_detail.ddd_amount,                --v_rct_amount NUMBER,
                   'DD',                          --v_rct_paymt_mode VARCHAR2,
                   dd.dd_ref_no,                  --v_rct_paymt_memo VARCHAR2,
                   'SYSTEM - DIRECT DEBIT',          --v_rct_paid_by VARCHAR2,
                   NULL,                                --v_rct_doc_date DATE,
                   'DIRECT DEBIT',                      --v_rct_desc VARCHAR2,
                   'N',                                  --v_rct_printed CHAR,
                   dd_detail.ddd_sys_code,                 --v_rct_app NUMBER,
                   v_account_type_code,               --v_rct_act_code NUMBER,
                   'D',                                 --v_rct_acc_type CHAR,
                   v_rct_brh_code,                    --v_rct_brh_code NUMBER,
                   NULL,                        --v_rct_act_sht_desc VARCHAR2,
                   v_cur_code,                        --v_rct_cur_code NUMBER,
                   v_rct_bct_code,                    --v_rct_bct_code NUMBER,
                   v_rct_bct_brh_code,            --v_rct_bct_brh_code NUMBER,
                   'N',                                --v_rct_cb_posted CHAR,
                   'N',                      -- v_upd_status CHAR DEFAULT 'N',
                   vreceiptnumber,                  --v_rct_brh_rct_no NUMBER,
                   vreceiptcode,                --v_rct_brh_rct_code VARCHAR2,
                   NULL,                        --v_rct_drawers_bank VARCHAR2,
                   'D',                         --v_rct_acct_type_id VARCHAR2,
                   v_rct_type,                          --v_rct_type VARCHAR2,
                   dd.dd_ref_no,                         --v_rct_ref VARCHAR2,
                   'G',                --v_rct_Net_Gross VARCHAR2 DEFAULT 'G',
                   NULL,             --v_rct_GL_Account VARCHAR2 DEFAULT NULL,
                   NULL,               --v_rct_pol_type VARCHAR2 DEFAULT NULL,
                   v_sys_sht_desc,                 --v_sys_sht_desc VARCHAR2 ,
                   NULL,                 --v_rct_point_id NUMBER DEFAULT NULL,
                   NULL,                --v_Auto_Manual VARCHAR2 DEFAULT NULL,
                   'N',
                   v_rct_dtl_rec,
                              --v_rct_dtl_rec Fms_Rcts_Pkg.type_rct_dtl_table,
                   v_rct_alloc_rec,
               --v_rct_alloc_rec Fms_Interfaces_Pkg.type_def_alloc_amts_table,
                   v_rct_alloc_multi_rec,
                   --v_rct_alloc_multi_rec Fms_Rcts_Pkg.type_rct_client_table,
                   vreceiptpk,                          --v_rct_no OUT NUMBER,
                   NULL,                       --v_agn_no NUMBER DEFAULT NULL,
                   NULL,              --v_received_from VARCHAR2 DEFAULT NULL,
                   NULL,                     --v_cac_code NUMBER DEFAULT NULL,
                   v_rct_charge_dtl_rec,
                                   -- fms_rcts_pkg.type_rct_charges_dtl_table,
                   'N',                    --v_multi_org VARCHAR2 DEFAULT 'N',
                   fms_gen_pkg.get_org (v_rct_brh_code),
                                                        --v_source_org NUMBER,
                   dd_detail.ddd_amount               --v_multi_org_amt NUMBER
                  );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('at create_web_rct');
            --Raise_error('Generate_Receipts..');
            END;

            BEGIN
               fms_rcts_pkg.update_rct_os_amount (vreceiptpk);
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('at update_rct_os_amount');
            --Raise_error('Generate_Receipts..');
            END;

            BEGIN
               fms_rcts_pkg.update_source
                                    (vreceiptpk,
                                     v_sys_sht_desc,
                                     tqc_interfaces_pkg.usercode (v_username),
                                     'N',
                                     'N'
                                    );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('at update_source');
            --Raise_error('Generate_Receipts..');
            END;

            --raise_error('ggg');
            UPDATE tqc_direct_debit_detail
               SET ddd_receipt_no = vreceiptcode,
                   ddd_receipt_date = NVL (dd.dd_value_date, SYSDATE),
                   ddd_receipted_date = SYSDATE,
                   ddd_receipted_by = v_username,
                   ddd_receipted = 'Y'
             WHERE ddd_code = dd_detail.ddd_code;
         END LOOP;

         UPDATE tqc_direct_debit_header
            SET ddh_receipted = 'Y'
          WHERE ddh_dd_code = v_dd_code AND ddh_status = 'A';

         UPDATE tqc_direct_debit tqc_direct_debit_view
            SET dd_receipted = 'Y'
          WHERE dd_code = v_dd_code;
      END LOOP;
   --raise_error('ssssssssssss');
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Q..');
   --Raise_error('Generate_Receipts..');
   END;
--   PROCEDURE generate_fixed_holidays (v_year VARCHAR2)
--   IS
--      CURSOR c_fixed_holidays (v_country_code NUMBER)
--      IS
--         SELECT TO_DATE (thd_day || '/' || thd_mon || '/' || v_year,
--                         'DD/MM/RRRR'
--                        ) h_date,
--                thd_desc description
--           FROM tqc_holidays_definitions
--          WHERE thd_cou_code = v_country_code AND thd_status = 'A';

--      ncount       NUMBER;
--      v_cou_code   NUMBER;
--   BEGIN
--      BEGIN
--         SELECT DISTINCT org_cou_code
--                    INTO v_cou_code
--                    FROM tqc_organizations, tqc_systems
--                   WHERE sys_org_code = org_code AND sys_sht_desc = 'LMS';
--      EXCEPTION
--         WHEN OTHERS
--         THEN
--            raise_error ('Error getting the country....');
--      END;

--      FOR h IN c_fixed_holidays (v_cou_code)
--      LOOP
--         SELECT COUNT (1)
--           INTO ncount
--           FROM tqc_holidays
--          WHERE hld_date = h.h_date;

--         IF NVL (ncount, 0) = 0
--         THEN
--            INSERT INTO tqc_holidays
--                        (hld_date, hld_desc
--                        )
--                 VALUES (TRUNC (h.h_date), h.description
--                        );
--         END IF;
--      END LOOP;
--   EXCEPTION
--      WHEN OTHERS
--      THEN
--         raise_error ('Generate Holidays....');
--   END generate_fixed_holidays;
PROCEDURE generate_fixed_holidays (v_year VARCHAR2)
   IS
      CURSOR c_fixed_holidays (v_country_code NUMBER)
      IS
         SELECT TO_DATE (thd_day || '/' || thd_mon || '/' || v_year,
                         'DD/MM/RRRR'
                        ) h_date,
                thd_desc description
           FROM tqc_holidays_definitions
          WHERE thd_cou_code = v_country_code AND thd_status = 'A';

      ncount       NUMBER;
      v_cou_code   NUMBER;
   BEGIN
      BEGIN
         SELECT DISTINCT org_cou_code
                    INTO v_cou_code
                    FROM tqc_organizations, tqc_systems
                   WHERE sys_org_code = org_code AND sys_sht_desc = 'LMS';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error getting the country....');
      END;

      FOR h IN c_fixed_holidays (v_cou_code)
      LOOP
         SELECT COUNT (1)
           INTO ncount
           FROM tqc_holidays
          WHERE hld_date = h.h_date;

         IF NVL (ncount, 0) = 0
         THEN
            INSERT INTO tqc_holidays
                        (hld_date, hld_desc
                        )
                 VALUES (TRUNC (h.h_date), h.description
                        );
         END IF;
      END LOOP;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Generate Holidays....');
   END generate_fixed_holidays;
   PROCEDURE validate_working_day (v_date DATE)
   IS
      v_cnt   NUMBER := 0;
   BEGIN
      BEGIN
         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_holidays
          WHERE TO_CHAR (hld_date, 'RRRR') = TO_CHAR (v_date, 'RRRR');
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_cnt := 0;
      END;

      IF NVL (v_cnt, 0) = 0
      THEN
         raise_error (   'Generate the holidays for year '
                      || TO_CHAR (v_date, 'RRRR')
                      || ' to proceed....'
                     );
      END IF;

      BEGIN
         v_cnt := 0;

         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_holidays
          WHERE TRUNC (hld_date) = TRUNC (v_date);
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            v_cnt := 0;
      END;

      IF TO_CHAR (v_date, 'D') IN (1, 7) OR v_cnt <> 0
      THEN
         raise_error ('The date passed is NOT a working day....');
      END IF;
   END validate_working_day;

   FUNCTION forwarding_bnk_code (v_bnk_code NUMBER)
      RETURN NUMBER
   IS
      v_forwarding_bnk_code   NUMBER;
   BEGIN
      SELECT bnk_kba_code
        INTO v_forwarding_bnk_code
        FROM tqc_banks
       WHERE bnk_code = v_bnk_code;

      RETURN v_forwarding_bnk_code;
   END forwarding_bnk_code;

   FUNCTION forwarding_bnk_bbr_code (v_bnk_bbr_code NUMBER)
      RETURN NUMBER
   IS
      v_forwarding_bnk_bbr_code   NUMBER;
   BEGIN
      SELECT bbr_kba_code
        INTO v_forwarding_bnk_bbr_code
        FROM tqc_bank_branches
       WHERE bbr_code = v_bnk_bbr_code;

      RETURN v_forwarding_bnk_bbr_code;
   END forwarding_bnk_bbr_code;

   FUNCTION get_dd_report_format (v_fwd_bnk_code NUMBER)
      RETURN VARCHAR2
   IS
      v_rpt_desc       VARCHAR2 (100);
      v_bnk_ddr_code   NUMBER;
   BEGIN
      IF v_fwd_bnk_code IS NOT NULL
      THEN
         BEGIN
            SELECT bnk_ddr_code
              INTO v_bnk_ddr_code
              FROM tqc_banks
             WHERE bnk_code = v_fwd_bnk_code;
         END;

         BEGIN
            SELECT ddr_report_description
              INTO v_rpt_desc
              FROM tqc_direct_debit_reports
             WHERE ddr_code = v_bnk_ddr_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
            WHEN OTHERS
            THEN
               raise_error ('direct debit reports error......');
         END;

         RETURN (v_rpt_desc);
      END IF;
   END get_dd_report_format;

   FUNCTION check_if_dd_generated (
      v_clnt_code   NUMBER,
      v_bbr_code    NUMBER,
      v_date        DATE,
      v_pol_code    NUMBER,
      v_ppr_code    NUMBER,
      v_ddd_type    VARCHAR2
   )
      RETURN VARCHAR2
   IS
      v_day_month_param    VARCHAR2 (5);
      v_clnt_cnt           NUMBER;
      v_generate_allowed   VARCHAR2 (5);
      v_freq               NUMBER;
      v_prev_bookg_date    DATE;
   BEGIN
      IF v_pol_code IS NOT NULL
      THEN
         IF v_ddd_type = 'PL'
         THEN
            BEGIN
               SELECT   -1
                      * (  DECODE (pol_freq_of_payment,
                                   'M', 1,
                                   'Q', 3,
                                   'S', 6,
                                   12
                                  )
                         - 1
                        )
                 INTO v_freq
                 FROM lms_policies
                WHERE pol_code = v_pol_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error (   'Error fetching policy details..pol_code='
                               || v_pol_code
                              );
            END;

            BEGIN
               SELECT MAX (dd_book_date)
                 INTO v_prev_bookg_date
                 FROM tqc_direct_debit_header,
                      tqc_direct_debit,
                      tqc_direct_debit_detail
                WHERE ddh_clnt_code = v_clnt_code
                  AND ddd_pol_code = v_pol_code
                  AND ddd_dd_code = dd_code
                  AND ddd_ddh_code = ddh_code
                  AND ddh_dd_code = dd_code
                  AND ddd_type = v_ddd_type;
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
               WHEN OTHERS
               THEN
                  raise_error (   'Error fetching policy dtls..pol_code='
                               || v_pol_code
                              );
            END;

            IF v_prev_bookg_date IS NULL
            THEN
               --RAISE_ERROR('v_prev_bookg_date IS NULL v_ddd_type='||v_ddd_type||';'||v_clnt_code||';'||v_pol_code||';'||v_bbr_code);
               BEGIN
                  SELECT MAX (dd_book_date)
                    INTO v_prev_bookg_date
                    FROM tqc_direct_debit_header,
                         tqc_direct_debit,
                         tqc_direct_debit_detail,
                         lms_policies,
                         lms_pol_proposals
                   WHERE ddh_clnt_code = v_clnt_code
                     AND ddd_ppr_code = ppr_code
                     AND ddd_dd_code = dd_code
                     AND ddd_ddh_code = ddh_code
                     AND ddh_dd_code = dd_code
                     AND ddd_type = 'PR'
                     AND ppr_proposal_no = pol_proposal_no
                     AND pol_code = v_pol_code;
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     v_prev_bookg_date :=
                                 TRUNC (TO_DATE ('01/01/0000', 'DD/MM/RRRR'));
                  WHEN OTHERS
                  THEN
                     raise_error
                         (   'Error fetching policy proposal dtls..ppr_code='
                          || v_ppr_code
                         );
               END;
            END IF;

            IF v_prev_bookg_date IS NULL
            THEN
               v_prev_bookg_date :=
                                 TRUNC (TO_DATE ('01/01/1900', 'DD/MM/RRRR'));
            END IF;

            SELECT COUNT (1)
              INTO v_clnt_cnt
              FROM tqc_direct_debit_header,
                   tqc_direct_debit,
                   tqc_direct_debit_detail
             WHERE ddh_clnt_code = v_clnt_code
               AND ddd_pol_code = v_pol_code
               AND ddd_dd_code = dd_code
               AND ddd_ddh_code = ddh_code
               AND dd_book_date BETWEEN v_date AND ADD_MONTHS (v_date, v_freq)
               AND ddh_dd_code = dd_code
               AND ddd_type = v_ddd_type;

            IF v_clnt_cnt = 0
            THEN
               SELECT COUNT (1)
                 INTO v_clnt_cnt
                 FROM tqc_direct_debit_header,
                      tqc_direct_debit,
                      tqc_direct_debit_detail,
                      lms_policies,
                      lms_pol_proposals
                WHERE ddh_clnt_code = v_clnt_code
                  AND ddd_ppr_code = ppr_code
                  AND ddd_dd_code = dd_code
                  AND ddd_ddh_code = ddh_code
                  AND dd_book_date BETWEEN v_date AND ADD_MONTHS (v_date,
                                                                  v_freq
                                                                 )
                  AND ddh_dd_code = dd_code
                  AND ddd_type = 'PR'
                  AND ppr_proposal_no = pol_proposal_no
                  AND pol_code = v_pol_code;
            END IF;
         ELSIF v_ddd_type = 'LN'
         THEN
            BEGIN
               SELECT   -1
                      * (  DECODE (NVL (lna_loan_freq_of_payment,
                                        pol_freq_of_payment
                                       ),
                                   'M', 1,
                                   'Q', 3,
                                   'S', 6,
                                   12
                                  )
                         - 1
                        )
                 INTO v_freq
                 FROM lms_loan_applications, lms_policies
                WHERE lna_no = v_pol_code AND lna_pol_code = pol_code;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error (   'Error fetching Loan details..Lna_no='
                               || v_pol_code
                              );
            END;

            BEGIN
               SELECT MAX (dd_book_date)
                 INTO v_prev_bookg_date
                 FROM tqc_direct_debit_header,
                      tqc_direct_debit,
                      tqc_direct_debit_detail
                WHERE ddh_clnt_code = v_clnt_code
                  AND ddd_pol_code = v_pol_code
                  AND ddd_dd_code = dd_code
                  AND ddd_ddh_code = ddh_code
                  AND ddh_dd_code = dd_code
                  AND ddd_type = v_ddd_type;
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  v_prev_bookg_date :=
                                 TRUNC (TO_DATE ('01/01/0000', 'DD/MM/RRRR'));
               WHEN OTHERS
               THEN
                  raise_error (   'Error fetching Loan dtls..Lna_no='
                               || v_pol_code
                              );
            END;

            SELECT COUNT (1)
              INTO v_clnt_cnt
              FROM tqc_direct_debit_header,
                   tqc_direct_debit,
                   tqc_direct_debit_detail
             WHERE ddh_clnt_code = v_clnt_code
               AND ddd_pol_code = v_pol_code
               AND ddd_dd_code = dd_code
               AND ddd_ddh_code = ddh_code
               AND dd_book_date BETWEEN v_date AND ADD_MONTHS (v_date, v_freq)
               AND ddh_dd_code = dd_code
               AND ddd_type = v_ddd_type;
         END IF;
      ELSIF v_ppr_code IS NOT NULL
      THEN
         BEGIN
            SELECT   -1
                   * (DECODE (ppr_freq_of_pymt, 'M', 1, 'Q', 3, 'S', 6, 12)
                      - 1
                     )
              INTO v_freq
              FROM lms_pol_proposals
             WHERE ppr_code = v_ppr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error fetching proposal details..ppr_code='
                            || v_ppr_code
                           );
         END;

         BEGIN
            SELECT MAX (dd_book_date)
              INTO v_prev_bookg_date
              FROM tqc_direct_debit_header,
                   tqc_direct_debit,
                   tqc_direct_debit_detail
             WHERE ddh_clnt_code = v_clnt_code
               AND ddd_ppr_code = v_ppr_code
               AND ddd_dd_code = dd_code
               AND ddd_ddh_code = ddh_code
               AND ddh_dd_code = dd_code
               AND ddd_type = v_ddd_type;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_prev_bookg_date :=
                                 TRUNC (TO_DATE ('01/01/0000', 'DD/MM/RRRR'));
            WHEN OTHERS
            THEN
               raise_error (   'Error fetching proposal dtls..ppr_code='
                            || v_ppr_code
                           );
         END;

         SELECT COUNT (1)
           INTO v_clnt_cnt
           FROM tqc_direct_debit_header,
                tqc_direct_debit,
                tqc_direct_debit_detail
          WHERE ddh_clnt_code = v_clnt_code
            AND ddd_ppr_code = v_ppr_code
            AND ddd_dd_code = dd_code
            AND ddd_ddh_code = ddh_code
            AND dd_book_date BETWEEN v_date AND ADD_MONTHS (v_date, v_freq)
            AND ddh_dd_code = dd_code
            AND ddd_type = v_ddd_type;
      END IF;

      IF     v_clnt_cnt = 0
         AND NVL (v_prev_bookg_date, v_date - 1) < v_date
         AND v_date - SYSDATE <= 30
      THEN
         v_generate_allowed := 'Y';
      ELSE
         v_generate_allowed := 'N';
      END IF;

--    END IF;
--if v_clnt_code = 1167079 then
--    RAISE_ERROR('VVV v_generate_allowed='||v_generate_allowed||';v_prev_bookg_date='||v_prev_bookg_date
--    ||';v_date='||v_date||';v_ppr_code='||v_ppr_code||';v_pol_code='||v_pol_code||
--    ';v_clnt_code='||v_clnt_code||';v_ddd_type='||v_ddd_type||';v_freq='||v_freq||
--    '; v_clnt_cnt='||v_clnt_cnt);
--end if;
      RETURN v_generate_allowed;
   END;

   FUNCTION func_unpaid_periods (v_pol_code NUMBER, v_date DATE)
      RETURN NUMBER
   IS
      v_unpaid_prd         NUMBER;
      v_clm_date           DATE          := NVL (v_date, TRUNC (SYSDATE));
      v_tot_os_prem        NUMBER;
      v_unpaid_param       VARCHAR2 (1);
      v_unpaid_instlmnts   VARCHAR2 (10) := NULL;
      v_pol_status         VARCHAR2 (5);
   BEGIN
      lms_ord_misc.system_prod_param_val ('DD_RECORDS_INC_UNPAID_PRDS',
                                          NULL,
                                          v_unpaid_param,
                                          v_unpaid_instlmnts
                                         );
      v_unpaid_prd :=
         lms_ord_misc.func_unpaid_clm_periods (v_pol_code,
                                               v_clm_date,
                                               v_tot_os_prem
                                              );

      SELECT pol_status
        INTO v_pol_status
        FROM lms_policies
       WHERE pol_code = v_pol_code;

      IF v_unpaid_param = 'N' AND v_unpaid_prd > 1
      THEN
         v_unpaid_prd := 1;
      ELSIF v_pol_status IN ('D', 'V', 'L')
      THEN
         v_unpaid_prd := 1;
      ELSIF     v_unpaid_param = 'Y'
            AND v_unpaid_prd > NVL (TO_NUMBER (v_unpaid_instlmnts), 99999)
      THEN
         v_unpaid_prd := v_unpaid_instlmnts;
      ELSIF v_unpaid_param = 'Y' AND v_unpaid_prd <= 0
      THEN
         -- raise_Error('unpaid periods is zero');
         v_unpaid_prd := 1;
      END IF;

      RETURN (v_unpaid_prd);
   END func_unpaid_periods;
END direct_debit_pkg; 
/