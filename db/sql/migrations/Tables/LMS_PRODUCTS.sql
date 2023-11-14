--
-- LMS_PRODUCTS  (Table) 
--
CREATE TABLE TQ_CRM.LMS_PRODUCTS
(
  PROD_CODE                     NUMBER(15)      DEFAULT 0                     NOT NULL,
  PROD_DESC                     VARCHAR2(50 BYTE) NOT NULL,
  PROD_SHT_DESC                 VARCHAR2(15 BYTE) NOT NULL,
  PROD_POL_CODE_FIX             VARCHAR2(8 BYTE) NOT NULL,
  PROD_CLM_CODE_FIX             VARCHAR2(8 BYTE) NOT NULL,
  PROD_LAPSE_PRD_DAYS           NUMBER(8)       DEFAULT 0                     NOT NULL,
  PROD_REIN_MAX_PRD_DAYS        NUMBER(8)       DEFAULT 0                     NOT NULL,
  PROD_MIN_AGE_LIMIT_YRS        NUMBER(3)       DEFAULT 0                     NOT NULL,
  PROD_MAX_AGE_LIMIT_YRS        NUMBER(3)       DEFAULT 0                     NOT NULL,
  PROD_CANC_NOTICE_DAYS         NUMBER(8)       DEFAULT 0                     NOT NULL,
  PROD_MIN_TERM_YRS             NUMBER(3)       DEFAULT 0                     NOT NULL,
  PROD_MAX_TERM_YRS             NUMBER(3)       DEFAULT 0                     NOT NULL,
  PROD_MIN_SURRENDER_PRD_DAYS   NUMBER(8)       DEFAULT 0                     NOT NULL,
  PROD_MIN_LOAN_PRD_DAYS        NUMBER(8)       DEFAULT 0,
  PROD_LOANABLE                 VARCHAR2(1 BYTE) NOT NULL,
  PROD_LOAN_LOOKUP              VARCHAR2(2 BYTE) NOT NULL,
  PROD_LOAN_MAX_PCT             NUMBER(4,2)     DEFAULT 0                     NOT NULL,
  PROD_CLA_CODE                 NUMBER(15)      NOT NULL,
  PROD_DEPENDT_COVERED          CHAR(2 BYTE)    DEFAULT 'S',
  PROD_LOAN_PRODUCT             VARCHAR2(1 BYTE) DEFAULT 'N',
  PROD_DEFAULT_DOB              DATE,
  PROD_POL_SEQ                  NUMBER(22)      DEFAULT 0                     NOT NULL,
  PROD_CLM_SEQ                  NUMBER(22)      DEFAULT 0                     NOT NULL,
  PROD_REFUND_ALLOWED           VARCHAR2(2 BYTE) DEFAULT 'N' NOT NULL,
  PROD_MONTHLY_LAPSE_PRD        NUMBER(8),
  PROD_QUARTER_LAPSE_PRD        NUMBER(8),
  PROD_SEMI_A_LAPSE_PRD         NUMBER(8),
  PROD_ANNUALLY_LAPSE_PRD       NUMBER(8),
  PROD_REMARKS                  VARCHAR2(4000 BYTE),
  PROD_YR_TO_MONTH_RATE         NUMBER(10,5),
  PROD_YR_TO_QUATER_RATE        NUMBER(10,5),
  PROD_YR_TO_S_ANNL_RATE        NUMBER(10,5),
  PROD_PRTL_MATURTY_PERCT       VARCHAR2(25 BYTE),
  PROD_PARTL_MATURTY_APPLBLE    VARCHAR2(3 BYTE),
  PROD_P_MATURITY_LAST_NO_YRS   NUMBER(5)       DEFAULT 0,
  PROD_LOANABLE_PERC            NUMBER(5)       DEFAULT 1,
  PROD_PROP_CODE_FIX            VARCHAR2(10 BYTE),
  PROD_PROP_SEQ                 NUMBER(22),
  PROD_POLICY_WORD_DOC          VARCHAR2(45 BYTE),
  PROD_PERIOD_MULTIPLES         NUMBER(5),
  PROD_PARTIAL_CANCEL_ALLOWED   VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_SAVINGS_COVERED          VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_MIN_PAID_UP_PRD_DAYS     NUMBER(8),
  PROD_MNTH_TO_QTR_FCTOR        NUMBER(15,5),
  PROD_MNTH_TO_S_ANNL_FCTOR     NUMBER(15,5),
  PROD_MNTH_TO_ANNL_FCTOR       NUMBER(15,5),
  PROD_APPLICABLE_LAPSE_TYPE    VARCHAR2(3 BYTE),
  PROD_MINIMUM_FCL_MEMBERS      NUMBER(5),
  PROD_FCL_MAX_AMT              NUMBER(23,5),
  PROD_FCL_FACTOR1              NUMBER(10,5),
  PROD_FCL_FACTOR2              NUMBER(10,5),
  PROD_EXPECTED_SCH_RECPT_DAY   NUMBER(4)       DEFAULT 0,
  PROD_RENEWAL_ALLOWED          VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_QUO_COSTING_SCH_RPT      VARCHAR2(15 BYTE),
  PROD_QUO_SAVINGS_SCH_RPT      VARCHAR2(15 BYTE),
  PROD_QUO_FE_SCH_RPT           VARCHAR2(15 BYTE),
  PROD_UW_COSTING_SCH_RPT       VARCHAR2(15 BYTE),
  PROD_UW_SAVING_SCH_RPT        VARCHAR2(15 BYTE),
  PROD_UW_FE_SCH_RPT            VARCHAR2(15 BYTE),
  PROD_ADD_REF_PREM_PERC        NUMBER(10,5),
  PROD_SCH_ADJ_PERIOD           NUMBER(10),
  PROD_MAX_EXT_PERIOD           NUMBER(10),
  PROD_INITIAL_SCH_ENDRSE       VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_EXTENTION_ALLOWED        VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_TYPE                     VARCHAR2(5 BYTE),
  PROD_SURR_EFF_DATE            VARCHAR2(2 BYTE) DEFAULT 'P',
  PROD_SURR_ALLOWED             VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_CANCEL_TYPE              VARCHAR2(3 BYTE),
  PROD_MIN_LOAN_AMT             NUMBER(22,5),
  PROD_UW_REFUND_SCH_RPT        VARCHAR2(15 BYTE),
  PROD_POL_COSTING_SCH_RPT      VARCHAR2(15 BYTE),
  PROD_POL_SAVING_SCH_RPT       VARCHAR2(15 BYTE),
  PROD_POL_FE_SCH_RPT           VARCHAR2(15 BYTE),
  PROD_FULL_MATRTY_AGE          NUMBER(10),
  PROD_MIN_MATRTY_TERM          NUMBER(10),
  PROD_OS_LOAN_CALC             VARCHAR2(2 BYTE) DEFAULT 'A',
  PROD_LOANS_ALLOWED            NUMBER(10),
  PROD_GRP_RATES_MIN            NUMBER(10),
  PROD_ACCPT_LTR_RPT            VARCHAR2(20 BYTE),
  PROD_POSTPN_DECL_RPT          VARCHAR2(20 BYTE),
  PROD_UW_ASSMT_RPT             VARCHAR2(20 BYTE),
  PROD_POL_SCH_RPT              VARCHAR2(20 BYTE),
  PROD_REIN_ADV_RPT             VARCHAR2(20 BYTE),
  PROD_QUO_WRITEUP_RPT          VARCHAR2(20 BYTE),
  PROD_QUO_RPT                  VARCHAR2(20 BYTE),
  PROD_PREM_TAX_RATE            NUMBER(10,5),
  PROD_REIN_PREM_TAX_RATE       NUMBER(10,5),
  PROD_OPEN_COVER               VARCHAR2(3 BYTE) DEFAULT 'N',
  PROD_AIR_COVERED              VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_HOSP_LIMIT               NUMBER(5),
  PROD_WKLY_INDMTY_LIMIT        NUMBER(5),
  PROD_MIN_SA                   NUMBER(23,5),
  PROD_MIN_PREM                 NUMBER(23,5),
  PROD_ANNUAL_PREM              NUMBER(23,5),
  PROD_PREM_DISC                NUMBER(10,4),
  PROD_CLAIM_EXPIRY_PRD         NUMBER(8),
  PROD_DEBIT_ACC_NO             VARCHAR2(30 BYTE),
  PROD_PAIDUP_VAL_FORMULA       VARCHAR2(5 BYTE),
  PROD_SURRENDER_VAL_FORMULA    VARCHAR2(5 BYTE),
  PROD_REN_COSTING_SCH_RPT      VARCHAR2(15 BYTE),
  PROD_REN_FE_SCH_RPT           VARCHAR2(15 BYTE),
  PROD_REN_SAVING_SCH_RPT       VARCHAR2(15 BYTE),
  PROD_MAX_FUNERAL_COVER        NUMBER(23,5),
  PROD_QUO_AGGR_DTLS_RPT        VARCHAR2(15 BYTE),
  PROD_UNIT_RATE_APPLICABLE     VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_PROFIT_SHARE_RATE_PERC   NUMBER(10,5),
  PROD_WEF                      DATE            DEFAULT to_date('01/01/1960','dd/mm/yyyy') NOT NULL,
  PROD_WET                      DATE,
  PROD_GEN_POL_NO               VARCHAR2(1 BYTE) DEFAULT 'N',
  PROD_RI_LEVEL                 VARCHAR2(1 BYTE) DEFAULT 'C',
  PROD_REN_GRACE_PRD_DAYS       NUMBER(10,5),
  PROD_EMP_FUND_PAY_PRD         NUMBER(5),
  PROD_LOANISSUEDT_DAYS         NUMBER(5),
  PROD_GL_CONTROL_CODE          VARCHAR2(15 BYTE),
  PROD_MATURITY_OPTION          VARCHAR2(1 BYTE) DEFAULT 'E',
  PROD_ANNUITY_ALLOWED          VARCHAR2(1 BYTE) DEFAULT 'N' NOT NULL,
  PROD_REFUND_COMM              VARCHAR2(1 BYTE),
  PROD_RISK_CHARGE_APPLICABLE   VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_INVEST_ALL_PREM          VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_SA_FACTR_SETUP           VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_BASIC_CVR_EXEMPTED       VARCHAR2(1 BYTE) DEFAULT 'N',
  PROD_COVER_CEASE_AGE          NUMBER(3),
  PROD_SAVINGS_SCHEDULE_NO_RPT  VARCHAR2(50 BYTE),
  PROD_FE_SCHEDULE_NO_RPT       VARCHAR2(50 BYTE),
  PROD_COSTING_SCHEDULE_NO_RPT  VARCHAR2(50 BYTE),
  PROD_F2_SVT_CODE              NUMBER(15),
  PROD_SVT_CODE                 NUMBER(15),
  PROD_ESCALATION_ALLOWED       VARCHAR2(1 BYTE) DEFAULT 'N' NOT NULL,
  PROD_PROP_INITIALIZATION      VARCHAR2(3 BYTE) DEFAULT 'UW' NOT NULL,
  PROD_XOL_SETUP                VARCHAR2(1 BYTE) DEFAULT 'N' NOT NULL,
  PROD_INIT_PRICE               NUMBER(15),
  PROD_INIT_DATE                DATE,
  PROD_PRTL_WITHDRWL_ALLOWED    VARCHAR2(2 BYTE) DEFAULT 'N' NOT NULL,
  PROD_SURR_VAL_SA              VARCHAR2(2 BYTE),
  PROD_CLM_SA                   VARCHAR2(3 BYTE),
  PROD_ALLOW_COMMUTATION        VARCHAR2(1 BYTE) DEFAULT 'N',
  PROD_PAY_OS_MATURITIES        VARCHAR2(5 BYTE),
  PROD_PYMT_REPORT              VARCHAR2(15 BYTE),
  PROD_LOAN_PYMT_RPT            VARCHAR2(15 BYTE),
  PROD_BONUS_MAX_OS_INST        NUMBER(5),
  PROD_MIN_COOLINGOFF_PRD       NUMBER(3),
  PROD_CLM_ADVICE               VARCHAR2(20 BYTE),
  PROD_FNL_DISCHARGE            VARCHAR2(30 BYTE),
  PROD_DEATH_CLM_ADVICE         VARCHAR2(30 BYTE),
  PROD_DEATH_CLM_PYMT_INPUT     VARCHAR2(30 BYTE),
  PROD_CLM_FNL_DISCHARGE        VARCHAR2(30 BYTE),
  PROD_ESC_COMP_MODE            VARCHAR2(2 BYTE),
  PROD_LAPSE_TYPE               VARCHAR2(5 BYTE) DEFAULT 'W',
  PROD_INV_PERC                 NUMBER(10),
  PROD_MIN_PRT_WTHDRW_PRD       NUMBER,
  PROD_PRT_WTHDRW_PECT          NUMBER,
  PROD_PRT_WTHDRW_ONCE_AFTER    NUMBER,
  PROD_MIN_PRT_WTHDRW_BAL       NUMBER,
  PROD_LOAN_GRACE_PRD           VARCHAR2(2 BYTE),
  PROD_LAS_SEX                  VARCHAR2(1 BYTE) DEFAULT 'B',
  PROD_USE_JOINT_ANB            VARCHAR2(1 BYTE) DEFAULT 'Y',
  PROD_ALLOC_FREQ               VARCHAR2(5 BYTE) DEFAULT 'Y',
  PROD_CALC_TERM_FR_RTR_AGE     VARCHAR2(5 BYTE),
  PROD_PAY_GRATUITY             VARCHAR2(2 BYTE),
  PROD_COVER_LETTER             VARCHAR2(30 BYTE),
  PROD_EXC_NOT_AGE              NUMBER(25),
  PROD_EXC_NOT_SA               NUMBER(25,5),
  PROD_MIN_EARNINGS_PRD         NUMBER(23),
  PROD_VAL_INTR_CALC_MODE       VARCHAR2(10 BYTE),
  PROD_EMV_CALCULATION_TYPE     VARCHAR2(25 BYTE),
  PROD_ALLOW_UNIT_RATE          VARCHAR2(2 BYTE),
  PROD_QUO_SEQ                  NUMBER(10),
  PROD_MIN_RTIR_AGE             NUMBER(2),
  PROD_MAX_RTIR_AGE             NUMBER(2),
  PROD_RTIR_AGE_DIST            VARCHAR2(2 BYTE),
  PROD_MANUAL_LC_FACTOR         VARCHAR2(5 BYTE) DEFAULT 'N',
  PROD_STATUS                   VARCHAR2(10 BYTE),
  PROD_CERT_OF_COVER            VARCHAR2(30 BYTE),
  PROD_PROPOSAL_PYMT_REPORT     VARCHAR2(30 BYTE),
  PROD_REINS_FORMULAR           VARCHAR2(5 BYTE),
  PROD_ANN_FORMULAR             VARCHAR2(5 BYTE),
  PROD_AMORTZ_RPT               VARCHAR2(20 BYTE),
  PROD_POL_CANC_PYMNT_RPT       VARCHAR2(5 BYTE),
  PROD_NFL_ALLOCATION           VARCHAR2(5 BYTE),
  PROD_NFL_PRD                  NUMBER(6),
  PROD_BONUS_FORMULAR           VARCHAR2(5 BYTE) DEFAULT 'N',
  PROD_TBONUS_ALLOWED           VARCHAR2(5 BYTE) DEFAULT 'N',
  PROD_RATE_GENDER              VARCHAR2(2 BYTE) DEFAULT 'N' NOT NULL,
  PROD_TBONUS_BASEDON           VARCHAR2(5 BYTE) DEFAULT 'SA',
  PROD_DTH_SA_FACTS             VARCHAR2(1 BYTE) DEFAULT 'N',
  PROD_CALC_INV_FROM_SA         VARCHAR2(1 BYTE) DEFAULT 'N',
  PROD_INV_YR_TO_MONTH_RATE     NUMBER(10,5),
  PROD_INV_YR_TO_QUATER_RATE    NUMBER(10,5),
  PROD_INV_YR_TO_S_ANNL_RATE    NUMBER(10,5),
  PROD_EARNING_PRD_TYPE         VARCHAR2(1 BYTE) DEFAULT 'M',
  PROD_FCL_CALC_TYPE            VARCHAR2(2 BYTE),
  PROD_MIN_TAX_EMPT_AGE         NUMBER(3),
  PROD_MIN_LUMPSUM_TAX_EMPT     NUMBER(23,5),
  PROD_LC_PREM_FROM_INTR        VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_QUO_COIN_SCH_RPT         VARCHAR2(15 BYTE),
  PROD_QUO_COINF_SCH_RPT        VARCHAR2(15 BYTE),
  PROD_NON_MEDICAL              VARCHAR2(5 BYTE) DEFAULT 'N',
  PROD_PENS_TYPE                VARCHAR2(5 BYTE),
  PROD_PENS_CONTRI_LIMIT        NUMBER(15,5),
  PROD_PENS_REGISTERED          VARCHAR2(2 BYTE),
  PROD_LOAN_GUARD               VARCHAR2(1 BYTE),
  PROD_DISP_INV_SA              VARCHAR2(2 BYTE) DEFAULT 'N',
  PROD_MNTH_TO_WEEKLY_FCTOR     NUMBER(10,5),
  PROD_MNTH_TO_DAILY_FCTOR      NUMBER(10,5),
  PROD_YR_TO_WEEKLY_RATE        NUMBER(10,5),
  PROD_YR_TO_DAILY_RATE         NUMBER(10,5),
  PROD_MIN_DTH_BNFT_ALLOWED     VARCHAR2(2 BYTE) DEFAULT 'N'
)
TABLESPACE LMSDATA
PCTUSED    40
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;

COMMENT ON TABLE TQ_CRM.LMS_PRODUCTS IS 'STORES PRODUCT DETAILS';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_CODE IS 'Table Primary Key column';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_DESC IS 'The unique key for the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_SHT_DESC IS 'Product description';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_POL_CODE_FIX IS 'A Prefix to be inherited by each policy number on the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_CLM_CODE_FIX IS 'A Prefix to be inherited by each claim number on the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_LAPSE_PRD_DAYS IS 'Grace period for checkoffs in months';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REIN_MAX_PRD_DAYS IS 'Maximu Reinstatement period for the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_AGE_LIMIT_YRS IS 'Minimum age limit for the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MAX_AGE_LIMIT_YRS IS 'Maximum age limit for the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_CANC_NOTICE_DAYS IS 'Cancellation notice period for the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_TERM_YRS IS 'Minimum allowable term in years for the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MAX_TERM_YRS IS 'Maximum allowable term in years for the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_SURRENDER_PRD_DAYS IS 'Minimum Surrender Period in days';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_LOAN_PRD_DAYS IS 'Minimum period in days for the policy to be inforce before a loan can be taken on the policy on the product';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_LOANABLE IS 'Can you take  a loan on the product ? (Valid values: Y/N)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_LOAN_LOOKUP IS 'On what is the maximum loan allowable to be computed on (Valid values: SV = Surrender Value, SA = Sum Assured)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_LOAN_MAX_PCT IS 'Maximum allowed percentage (of the lookup) as loan';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_CLA_CODE IS 'Primary key for the table';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_DEPENDT_COVERED IS 'DEPENDENTS COVERED (';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_LOAN_PRODUCT IS 'LOAN PRODUCT (Y- YES , N- NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_DEFAULT_DOB IS 'DEFAULT DATE OF BIRTH( IF NON IS GIVEN)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_POL_SEQ IS 'POLICY SEQUENCE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_CLM_SEQ IS 'CLAIM SEQUENCE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REFUND_ALLOWED IS 'REFUND ALLOWED(Y - YES, N-NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MONTHLY_LAPSE_PRD IS 'GRACE PERIOD MONTHLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_QUARTER_LAPSE_PRD IS 'GRACE PERIOD QUARTERLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_SEMI_A_LAPSE_PRD IS 'GRACE PERIOD SEMI ANNUALLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_ANNUALLY_LAPSE_PRD IS 'GRACE PERIOD  ANNUALLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REMARKS IS 'REMARKS';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_YR_TO_MONTH_RATE IS 'LOADING RATE FOR ANNUALL RATES FOR MONTHLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_YR_TO_QUATER_RATE IS 'LOADING RATE FOR ANNUALL RATES FOR QUARTERLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_YR_TO_S_ANNL_RATE IS 'LOADING RATE FOR ANNUALL RATES FOR SEMI ANNUALLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PARTL_MATURTY_APPLBLE IS 'PARTIAL MATURITY APPLICABLE ( N- NOT APPLICABLE, P - PARTIAL /FULL MARTURITY, A- AGE MATURITY)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_P_MATURITY_LAST_NO_YRS IS 'LAST YEARS FOR PARTIAL MATURITY PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_LOANABLE_PERC IS 'LOANABLE PERCENTAGE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PROP_CODE_FIX IS 'PROPOSAL PREFIX';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PROP_SEQ IS 'PROPOSAL SEQUENCE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_POLICY_WORD_DOC IS 'POLICY WORD DOCUMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PARTIAL_CANCEL_ALLOWED IS 'PARTIAL CANCELLATION ALLOWED';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_SAVINGS_COVERED IS 'SHARES OR SAVINGS COVERED';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_PAID_UP_PRD_DAYS IS 'ALLOWED DURATION BEFORE CONVERTING TO PAID UP';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MNTH_TO_QTR_FCTOR IS 'LOADING  RATE FOR MONTHLY RATES FOR QUARTERLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MNTH_TO_S_ANNL_FCTOR IS 'LOADING  RATE FOR MONTHLY RATES FOR SEMI ANNUAL FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MNTH_TO_ANNL_FCTOR IS 'LOADING  RATE FOR MONTHLY RATES FOR ANNUAL FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_APPLICABLE_LAPSE_TYPE IS 'APPLICABLE LAPSE TYPE ( P- PREMIUM, S -SCHEDULE, B- BOTH)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MINIMUM_FCL_MEMBERS IS 'MINIMUM NUMBER OF MEMBERS(FOR FREE COVER LIMIT COMPUTATION)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_FCL_MAX_AMT IS 'MAXIMUM FREE COVER LIMIT AMOUNT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_FCL_FACTOR1 IS 'FREE COVER LIMIT FACTOR( MEMBERS ABOVE MINIMUM ALLOWED)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_FCL_FACTOR2 IS 'FREE COVER LIMIT FACTOR( MEMBERS BELOW MINIMUM ALLOWED)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_EXPECTED_SCH_RECPT_DAY IS 'EXPECTED SCHEDULE RECEIPT DATE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_RENEWAL_ALLOWED IS 'RENEWAL ALLOWED (Y- YES, N- NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_QUO_COSTING_SCH_RPT IS 'QUOTATION COSTING SCHEDULE REPORT NAME';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_QUO_SAVINGS_SCH_RPT IS 'QUOTATION SAVINGS REPORT NAME';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_QUO_FE_SCH_RPT IS 'QUOTATION FUNERAL EXPENSE REPORT NAME';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_UW_COSTING_SCH_RPT IS 'UNDERWRITING COSTING SCHEDULE REPORT NAME';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_UW_SAVING_SCH_RPT IS 'UNDERWRITING SAVING SCHEDULE REPORT NAME';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_UW_FE_SCH_RPT IS 'UNDERWRITING FUNERAL EXPENSE SCHEDULE REPORT NAME';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_ADD_REF_PREM_PERC IS 'ALLOWED REFUND OR ADDITIONAL PREMIUM PERCENTAGE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_SCH_ADJ_PERIOD IS 'PERIOD ALLOWED FOR SCHEDULE ADJUSTMENT ENDORSEMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MAX_EXT_PERIOD IS 'MAXIMUM PERIOD ALLOWED FOR EXTENSION OF COVER';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_INITIAL_SCH_ENDRSE IS 'INITIAL SCHEDULE ENDORSEMENT ALLOWED 9y - YES, N- NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_EXTENTION_ALLOWED IS 'EXTENETION ALLOWED (Y- YES, N- NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_TYPE IS 'PRODUCT TYPE(ED- EDUCATION,EN- ENDOWMENT, TM-TERM,IN- INVESTMENT,PA- PERSONAL ACCIDENT,PN- PENSION INDIVIDUAL, EARN- EARNINGS, LOAN- LOANS, PENS- PENSION GROUP, FUNE- FUNERAL)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_SURR_EFF_DATE IS 'SURRENDER AMOUNT COMPUTED BASED ON (P - PAID TO DATE, S- SURRENDER DATE)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_SURR_ALLOWED IS 'SURRNDER ALLOWED (Y- YES, N- NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_CANCEL_TYPE IS 'CANCELLATION TYPE ( T- TOTAL)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_LOAN_AMT IS 'MINIMUM LOAN AMOUNT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_UW_REFUND_SCH_RPT IS 'UNDERWRITING REFUNDSCHEDULE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_POL_COSTING_SCH_RPT IS 'POLICY COSTING SCHEDULE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_POL_SAVING_SCH_RPT IS 'POLICY SHARES SCHEDULE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_POL_FE_SCH_RPT IS 'POLICY FUNERAL EXPENSE SCHEDULE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_FULL_MATRTY_AGE IS 'AGE ( PAYMENT OF MATURITY DUE TO AGE)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_MATRTY_TERM IS 'MINIMUM TERM FOR PARTIAL MATURITY PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_OS_LOAN_CALC IS 'CALCULATION TYPE FOR OUTSTANDING LOAN( A- AMORTIZATION, P- OUTSTANDING PERIOD)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_GRP_RATES_MIN IS 'MINIMUM MEMBERSHIP FOR APPLICATION OF UNIT RATE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_ACCPT_LTR_RPT IS 'ACCEPTANCE LETTER REPORT NAME';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_POSTPN_DECL_RPT IS 'POSTPONE/DECLINE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_UW_ASSMT_RPT IS 'UNDERWRITING ASSESSMENT REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_POL_SCH_RPT IS 'POLICY SCHEDULE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REIN_ADV_RPT IS 'REINSURANCE ADVICE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_QUO_WRITEUP_RPT IS 'QUOTATION WRITEUP REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_QUO_RPT IS 'QUOTATION REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PREM_TAX_RATE IS 'PREMIUM TAX RATE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REIN_PREM_TAX_RATE IS 'REINSURANCE PREMIUM TAX RATE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_OPEN_COVER IS 'OPEN COVER(Y- YES, N- NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_AIR_COVERED IS 'ACCIDENTAL INDEMNITY COVERED (Y- YES, N-NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_HOSP_LIMIT IS 'LIMIT FOR HOSPITALIZATION INDEMNITY';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_WKLY_INDMTY_LIMIT IS 'WEEKLY INDEMNITY LIMIT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_SA IS 'MINIMUM SUM ASSURED';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_PREM IS 'MINIMUM PREMIUM';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_ANNUAL_PREM IS 'MINIMUM ANNUAL PREMIUM';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_CLAIM_EXPIRY_PRD IS 'CLAIM EXPIRY PERIOD';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_DEBIT_ACC_NO IS 'DEBIT ACCOUNT NUMBER';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PAIDUP_VAL_FORMULA IS 'PAIDUP FORMULA';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_SURRENDER_VAL_FORMULA IS 'SURRENDER VALUE FORMULA';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REN_COSTING_SCH_RPT IS 'RENEWAL COSTING SCHEDULE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REN_FE_SCH_RPT IS 'RENEWAL FUNERAL EXPENSE SCHEDULE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REN_SAVING_SCH_RPT IS 'RENEWAL SHARES SCHEDULE REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MAX_FUNERAL_COVER IS 'MAXIMUM LIMIT FUNERAL COVER';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_QUO_AGGR_DTLS_RPT IS 'QUOTATION AGGREGATE DETAILS REPORT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_UNIT_RATE_APPLICABLE IS 'UNIT RATE APPLICABLE (Y- YES, N- NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PROFIT_SHARE_RATE_PERC IS 'PROFIT SHARE RATE';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_WEF IS 'WEF';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_WET IS 'WET';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_GEN_POL_NO IS 'GENERATION OF POLICY NUMBER( Y- YES, N- NO)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_RI_LEVEL IS 'RI LEVEL( C- COVER TYPE, R- RISK LEVEL)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REN_GRACE_PRD_DAYS IS 'RENEWAL GRACE PERIOD';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MATURITY_OPTION IS '(E)VEN DISTRIBUTION; (O)DD DISTRIBUTION';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_RISK_CHARGE_APPLICABLE IS 'RISK PREMIUM CHARGES APPLICABLE TO THE PRODUCT OR NOT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_INVEST_ALL_PREM IS 'IF THE PARAMETER INVEST_ALL_PREMIUMS IS Y THEN THE PRODUCTS THAT PREM IS INVESTED HAVE TO BE SET TO YES';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_ESCALATION_ALLOWED IS 'CONTROLS ESCALATION OF PREMIUM AND SA';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PROP_INITIALIZATION IS 'UW - UNDERWRITING; MKT - MARKETING;';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_XOL_SETUP IS 'Enable Excess of Loss Reinsurance Setup';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_INIT_PRICE IS 'STORES THE INITIAL UNIT PRICE IF UNITIZATION IS PER PRODUCT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_INIT_DATE IS 'STORES THE INITIAL UNITIZATION DATE IF UNITIZATION IS PER PRODUCT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PRTL_WITHDRWL_ALLOWED IS 'STATES WHETHER PARTIAL WITHRAWAL IS ALLOWED IF "Y" OR NOT IF "N"';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_SURR_VAL_SA IS 'Surrender Sum Assured (SA-based on Sum Assured, SV-based on Surr. Value, VA-based on the greater of Surr. Value or Sum Assured)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MANUAL_LC_FACTOR IS 'Life Cover Factor Is Picked Manually';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_REINS_FORMULAR IS 'Reinstatement Interest Computation Formular: CMP1-Compound Interest One, CMP2-Compound Interest Two, SI-Simple Interest';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_ANN_FORMULAR IS 'Annuity Computation Formular: F1-Lumpsum Annuity Formular, F2-Regular Annuity Formular';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_AMORTZ_RPT IS 'Amortization Report';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_NFL_ALLOCATION IS 'N - No NFL Loan, O - Outstanding Installments, P - NFL loan Amount upto a Number of Installments set at PROD_NFL_PRD column,';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_NFL_PRD IS 'Number of Installments that an NFL loan has to offset depending on column PROD_NFL_ALLOCATION';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_BONUS_FORMULAR IS 'N-Normal, AV-Accumulated Value';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_TBONUS_ALLOWED IS 'Y-Terminal Bonus is allowed.';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_RATE_GENDER IS 'Y - Premium rates are gender specific, N - Premium rates are not gender specific';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_TBONUS_BASEDON IS 'Calc Terminal Bonus Based on: AB - Accrued Bonuses, SA - Sum Assured';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_DTH_SA_FACTS IS 'S. A. Percentage payable at claims, Y- Death S.A. Factors are used else its setup at claims setup';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_CALC_INV_FROM_SA IS 'Calculate Investment contribution amount from S. A. Y-YES, N-NO';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_INV_YR_TO_MONTH_RATE IS 'Convertion factor for investment Contribution from Annual to monthly.';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_INV_YR_TO_QUATER_RATE IS 'Convertion factor for investment Contribution from Annual to quaterly.';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_INV_YR_TO_S_ANNL_RATE IS 'Convertion factor for investment Contribution from Annual to Semi-Annual.';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_TAX_EMPT_AGE IS 'Minimum tax exempt age';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_LUMPSUM_TAX_EMPT IS 'Minimum lumpsum tax exempt amount';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_LC_PREM_FROM_INTR IS 'Allocate Life cover premium from valuation Interest earned';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_NON_MEDICAL IS 'IS PRODUCT A NON MEDICAL OR  A CLIENT HAS TO GO FOR MEDICALS';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PENS_TYPE IS 'DEFINES THE TYPE OF PENSION PRODUCT. (PENSION, PROVIDENT OR INVESTMENT)';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PENS_CONTRI_LIMIT IS 'DEFINES THE EMPLOYEE CONTRIBUTION LIMIT ON A PENSION POLICY';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_PENS_REGISTERED IS 'For pension products that are registered for tax exemption guidlines: Y-Yes, N-No';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_LOAN_GUARD IS 'DETERMINES IF ITS A LOAN GUARD PRODUCT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_DISP_INV_SA IS 'Display investment sa(not used in prem. computation): Y-YES, N-NO';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MNTH_TO_WEEKLY_FCTOR IS 'LOADING  RATE FOR MONTHLY RATES FOR WEEKLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MNTH_TO_DAILY_FCTOR IS 'LOADING  RATE FOR MONTHLY RATES FOR DAILY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_YR_TO_WEEKLY_RATE IS 'LOADING RATE FOR ANNUAL RATES FOR WEEKLY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_YR_TO_DAILY_RATE IS 'LOADING RATE FOR ANNUAL RATES FOR DAILY FREQUENCY OF PAYMENT';

COMMENT ON COLUMN TQ_CRM.LMS_PRODUCTS.PROD_MIN_DTH_BNFT_ALLOWED IS 'Minimum Death benefit Allowed: Y-Yes, N-No';