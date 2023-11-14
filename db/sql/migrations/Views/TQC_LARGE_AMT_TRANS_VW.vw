--
-- TQC_LARGE_AMT_TRANS_VW  (View) 
--
CREATE OR REPLACE FORCE VIEW TQ_CRM.TQC_LARGE_AMT_TRANS_VW
(CLNT_SHT_DESC, CLNT_TITLE, CLNT_NAME, CLNT_OTHER_NAMES, CLNT_SURNAME, 
 CLNT_ID_REG_NO, CLNT_PIN, CLNT_PHYSICAL_ADDRS, CLNT_DOB, CLNT_DOMICILE_COUNTRIES, 
 CLNT_POSTAL_ADDRS, CLNT_TELEPHONE, TWN_NAME, COU_NAME, CLNT_EMAIL_ADDRS, 
 CLNT_BUSINESS, POL_DRCR_NO, CLNT_TYPE, POL_POLICY_NO, POL_REN_ENDOS_NO, 
 POL_CHECK_DATE, COVER_PERIOD, POLICY_STATUS, POL_POLICY_STATUS, PRO_SHT_DESC, 
 PRO_DESC, BRN_CODE, BRN_NAME, PMOD_DESC, POL_CHECKED_BY, 
 POL_PMOD_CODE, PAY_METHOD, TOTAL_SUM_INSURED, TOTAL_FAP, TOTAL_GP, 
 PREMIUM, PAID_AMT, POL_CUR_CODE, POL_CUR_RATE, AGN_SHT_DESC, 
 AGN_NAME, AGN_PHYSICAL_ADDRESS, AGN_POSTAL_ADDRESS, AGN_CONTACT_PERSON, AGN_CONTACT_TITLE, 
 AGN_TEL, AGN_ID_NO, AGN_TWN_NAME, AGN_COU_NAME, CLNT_STATE_NAME, 
 AGN_STATE_NAME, CLNT_ID_TYPE, ACCOUNT_TYPE)
AS 
(SELECT DISTINCT CLNT_SHT_DESC,CLNT_TITLE, CLNT_NAME, CLNT_OTHER_NAMES,CLNT_SURNAME, CLNT_ID_REG_NO, CLNT_PIN,
CLNT_PHYSICAL_ADDRS,TO_CHAR(CLNT_DOB,'DD/MM/RRRR') CLNT_DOB, CLNT_DOMICILE_COUNTRIES,
CLNT_POSTAL_ADDRS||' '||CLNT_ZIP_CODE CLNT_POSTAL_ADDRS,CLNT_TEL||', '|| CLNT_TEL2 CLNT_TELEPHONE,
D.TWN_NAME TWN_NAME,C.COU_NAME COU_NAME, CLNT_EMAIL_ADDRS, CLNT_BUSINESS,POL_DRCR_NO,
DECODE(CLNT_TYPE,'I','INDIVIDUAL','C','CORPORATE',CLNT_TYPE) CLNT_TYPE,
POL_POLICY_NO, POL_REN_ENDOS_NO,TO_CHAR(POL_CHECK_DATE,'DD/MM/RRRR') POL_CHECK_DATE,
TO_CHAR(POL_WEF_DT,'DD/MM/RRRR')||' TO '||TO_CHAR(POL_WET_DT,'DD/MM/RRRR') COVER_PERIOD,
DECODE(POL_POLICY_STATUS,'NB','NEW BUSINESS','EX','EXTENSION','EN','ENDORSEMENT','RN','RENEWAL','SP','SHORT PERIOD',
'CN','CANCELLATION','CONTRA TRANS') POLICY_STATUS,POL_POLICY_STATUS,PRO_SHT_DESC, PRO_DESC,BRN_CODE,
BRN_NAME,PMOD_DESC,POL_CHECKED_BY,TO_CHAR(POL_PMOD_CODE),PAY_METHOD,
NVL(POL_TOTAL_SUM_INSURED,0) TOTAL_SUM_INSURED,
NVL(POL_TOTAL_FAP,0) TOTAL_FAP,
NVL(POL_TOTAL_GP,0) TOTAL_GP,
NVL(POL_TOT_ENDOS_DIFF_AMT,0) PREMIUM,
NVL(PAID_AMT,0) CLM_PAID_AMT ,POL_CUR_CODE, POL_CUR_RATE,
AGN_SHT_DESC, AGN_NAME, AGN_PHYSICAL_ADDRESS, AGN_POSTAL_ADDRESS,
AGN_CONTACT_PERSON, AGN_CONTACT_TITLE, AGN_TEL1||' '||AGN_TEL2 AGN_TEL, AGN_ID_NO,
a.TWN_NAME AGN_TWN_NAME,b.COU_NAME AGN_COU_NAME,E.STS_NAME CLNT_STATE_NAME,F.STS_NAME AGN_STATE_NAME,
DECODE(CLNT_ID_REG_NO,NULL,'Driving Licence','National ID/Passport') CLNT_ID_TYPE,
ACT_ACCOUNT_TYPE ACCOUNT_TYPE
FROM GIN_POLICIES,TQC_CLIENTS,TQC_TOWNS D,TQC_COUNTRIES C,TQC_PAYMENT_MODES,
GIN_PRODUCTS,TQC_BRANCHES,TQC_AGENCIES,TQC_TOWNS A,TQC_COUNTRIES B,TQC_STATES E,TQC_STATES F,TQC_ACCOUNT_TYPES,
(SELECT CMB_POL_POLICY_NO, CMB_POL_BATCH_NO,
DECODE(CPV_PAY_METHOD,'CK','CHEQUE','CHQ','CHEQUE','CR','CREDIT NOTE','JV','JOURNAL VOUCHER',CPV_PAY_METHOD) PAY_METHOD,
SUM(NVL(CPV_AMOUNT,0)) PAID_AMT
FROM GIN_CLAIM_MASTER_BOOKINGS,GIN_CLM_PAYMENT_VOUCHERS
WHERE CMB_CLAIM_NO = CPV_CMB_CLAIM_NO
GROUP BY CMB_POL_POLICY_NO, CMB_POL_BATCH_NO,CPV_PAY_METHOD )
WHERE POL_PRP_CODE = clnt_code
AND POL_BATCH_NO = CMB_POL_BATCH_NO
AND CLNT_COU_CODE = C.COU_CODE(+)
AND CLNT_TWN_CODE = D.TWN_CODE(+)
AND CLNT_STS_CODE = E.STS_CODE(+)
AND POL_PRO_CODE = PRO_CODE
AND POL_BRN_CODE = BRN_CODE
AND POL_PMOD_CODE = PMOD_CODE
AND POL_AGNT_AGENT_CODE = AGN_CODE
AND AGN_COU_CODE = B.COU_CODE(+)
AND AGN_TWN_CODE = A.TWN_CODE(+)
AND AGN_STATE_CODE = F.STS_CODE(+)
AND AGN_ACT_CODE = ACT_CODE
UNION
SELECT distinct CLNT_SHT_DESC,CLNT_TITLE, CLNT_NAME, CLNT_OTHER_NAMES,CLNT_SURNAME, CLNT_ID_REG_NO, CLNT_PIN,
CLNT_PHYSICAL_ADDRS,TO_CHAR(CLNT_DOB,'DD/MM/RRRR') CLNT_DOB, CLNT_DOMICILE_COUNTRIES,
CLNT_POSTAL_ADDRS||' '||CLNT_ZIP_CODE CLNT_POSTAL_ADDRS,CLNT_TEL||', '|| CLNT_TEL2 CLNT_TELEPHONE,
D.TWN_NAME TWN_NAME,C.COU_NAME COU_NAME, CLNT_EMAIL_ADDRS, CLNT_BUSINESS,MTRAN_REF_NO POL_DRCR_NO,
DECODE(CLNT_TYPE,'I','INDIVIDUAL','C','CORPORATE',CLNT_TYPE) CLNT_TYPE,
MTRAN_REF_NO, MTRAN_OTHER_REF,TO_CHAR(MTRAN_POSTING_DATE,'DD/MM/RRRR') POL_CHECK_DATE,
NULL COVER_PERIOD,
DECODE(MTRAN_BTR_TRANS_CODE,'RC',DECODE(MTRAN_DC,'D','RECEIPT DEBITS','C','RECEIPT CREDITS'),'PM','PAYMENT') POLICY_STATUS,
MTRAN_TRAN_TYPE POL_POLICY_STATUS,MTRAN_BTR_TRANS_CODE PRO_SHT_DESC,
DECODE(MTRAN_BTR_TRANS_CODE,'RC','RECEIPTS','PM','PAYMENTS') PRO_DESC,
BRN_CODE,BRN_NAME,NVL(RCT_PAYMT_MODE,'CHEQUE') PMOD_DESC,MTRAN_POSTED_BY POL_CHECKED_BY,NULL POL_PMOD_CODE,
RCT_PAYMT_MODE PAY_METHOD,
0 TOTAL_SUM_INSURED,
0 TOTAL_FAP,
0 TOTAL_GP,
NVL(MTRAN_AMOUNT,0) PREMIUM,
0 CLM_PAID_AMT ,MTRAN_CUR_CODE POL_CUR_CODE,MTRAN_CUR_RATE POL_CUR_RATE ,
CLNT_SHT_DESC AGN_SHT_DESC, CLNT_NAME AGN_NAME,CLNT_PHYSICAL_ADDRS AGN_PHYSICAL_ADDRESS,CLNT_POSTAL_ADDRS||' '||CLNT_ZIP_CODE AGN_POSTAL_ADDRESS,
CLNT_NAME AGN_CONTACT_PERSON,CLNT_TITLE AGN_CONTACT_TITLE,CLNT_TEL||' '||CLNT_TEL2 AGN_TEL,CLNT_ID_REG_NO AGN_ID_NO,
D.TWN_NAME AGN_TWN_NAME,C.COU_NAME AGN_COU_NAME,E.STS_NAME CLNT_STATE_NAME,E.STS_NAME AGN_STATE_NAME,
DECODE(CLNT_ID_REG_NO,NULL,'Driving Licence','National ID/Passport') CLNT_ID_TYPE,
DECODE(CLNT_TYPE,'I','INDIVIDUAL','C','CORPORATE',CLNT_TYPE) ACCOUNT_TYPE
FROM GIN_MASTER_TRANSACTIONS,TQC_CLIENTS,FMS_RECEIPTS,
TQC_TOWNS D,TQC_COUNTRIES C,TQC_BRANCHES,TQC_AGENCIES,TQC_STATES E
WHERE MTRAN_BTR_TRANS_CODE IN ('RC','PM')
AND MTRAN_CLIENT_CODE = CLNT_CODE
AND MTRAN_RCT_CODE = RCT_NO(+)
AND CLNT_COU_CODE = C.COU_CODE(+)
AND CLNT_TWN_CODE = D.TWN_CODE(+)
AND CLNT_STS_CODE = E.STS_CODE(+)
AND MTRAN_BRN_CODE = BRN_CODE(+)
AND MTRAN_CLIENT_TYPE = 'D'
AND NVL(MTRAN_AUTHORISED,'N') = 'Y'
UNION
SELECT distinct CLNT_SHT_DESC,CLNT_TITLE, CLNT_NAME, CLNT_OTHER_NAMES,CLNT_SURNAME, CLNT_ID_REG_NO, CLNT_PIN,
CLNT_PHYSICAL_ADDRS,TO_CHAR(CLNT_DOB,'DD/MM/RRRR') CLNT_DOB, CLNT_DOMICILE_COUNTRIES,
CLNT_POSTAL_ADDRS||' '||CLNT_ZIP_CODE CLNT_POSTAL_ADDRS,CLNT_TEL||', '|| CLNT_TEL2 CLNT_TELEPHONE,
D.TWN_NAME TWN_NAME,C.COU_NAME COU_NAME, CLNT_EMAIL_ADDRS, CLNT_BUSINESS,MTRAN_REF_NO POL_DRCR_NO,
DECODE(CLNT_TYPE,'I','INDIVIDUAL','C','CORPORATE',CLNT_TYPE) CLNT_TYPE,
MTRAN_REF_NO, MTRAN_OTHER_REF,TO_CHAR(MTRAN_POSTING_DATE,'DD/MM/RRRR') POL_CHECK_DATE,
NULL COVER_PERIOD,
DECODE(MTRAN_BTR_TRANS_CODE,'RC',DECODE(MTRAN_DC,'D','RECEIPT DEBITS','C','RECEIPT CREDITS'),'PM','PAYMENT') POLICY_STATUS,
MTRAN_TRAN_TYPE POL_POLICY_STATUS,MTRAN_BTR_TRANS_CODE PRO_SHT_DESC,
DECODE(MTRAN_BTR_TRANS_CODE,'RC','RECEIPTS','PM','PAYMENTS') PRO_DESC,
BRN_CODE,BRN_NAME,NVL(RCT_PAYMT_MODE,'CHEQUE') PMOD_DESC,MTRAN_POSTED_BY POL_CHECKED_BY,NULL POL_PMOD_CODE,
RCT_PAYMT_MODE PAY_METHOD,
0 TOTAL_SUM_INSURED,
0 TOTAL_FAP,
0 TOTAL_GP,
NVL(MTRAN_AMOUNT,0) PREMIUM,
0 CLM_PAID_AMT ,MTRAN_CUR_CODE POL_CUR_CODE,MTRAN_CUR_RATE POL_CUR_RATE ,
AGN_SHT_DESC, AGN_NAME, AGN_PHYSICAL_ADDRESS, AGN_POSTAL_ADDRESS,
AGN_CONTACT_PERSON, AGN_CONTACT_TITLE, AGN_TEL1||' '||AGN_TEL2 AGN_TEL, AGN_ID_NO,
a.TWN_NAME AGN_TWN_NAME,b.COU_NAME AGN_COU_NAME,E.STS_NAME CLNT_STATE_NAME,F.STS_NAME AGN_STATE_NAME,
DECODE(CLNT_ID_REG_NO,NULL,'Driving Licence','National ID/Passport') CLNT_ID_TYPE,
ACT_ACCOUNT_TYPE ACCOUNT_TYPE
FROM GIN_MASTER_TRANSACTIONS,TQC_AGENCIES,FMS_RECEIPTS,TQC_CLIENTS,
TQC_TOWNS A,TQC_COUNTRIES B,TQC_BRANCHES,TQC_ACCOUNT_TYPES,TQC_TOWNS D,TQC_COUNTRIES C,
TQC_STATES E,TQC_STATES F
WHERE MTRAN_BTR_TRANS_CODE IN ('RC','PM')
AND MTRAN_CLIENT_CODE = AGN_CODE
AND AGN_ACT_CODE = ACT_CODE
AND MTRAN_RCT_CODE = RCT_NO(+)
AND AGN_COU_CODE = B.COU_CODE(+)
AND AGN_TWN_CODE = A.TWN_CODE(+)
AND MTRAN_BRN_CODE = BRN_CODE(+)
AND MTRAN_PRP_CODE = CLNT_CODE(+)
AND CLNT_COU_CODE = C.COU_CODE(+)
AND CLNT_TWN_CODE = D.TWN_CODE(+)
AND CLNT_STS_CODE = E.STS_CODE(+)
AND MTRAN_CLIENT_TYPE != 'D'
AND NVL(MTRAN_AUTHORISED,'N') = 'Y')
UNION ALL
(SELECT CLNT_SHT_DESC,CLNT_TITLE, CLNT_NAME, CLNT_OTHER_NAMES,CLNT_SURNAME, NVL(CLNT_ID_REG_NO,PRP_ID_REG_NO)CLNT_ID_REG_NO, CLNT_PIN,
CLNT_PHYSICAL_ADDRS,TO_CHAR(CLNT_DOB,'DD/MM/RRRR') CLNT_DOB, CLNT_DOMICILE_COUNTRIES,
CLNT_POSTAL_ADDRS||' '||CLNT_ZIP_CODE CLNT_POSTAL_ADDRS,CLNT_TEL||', '|| CLNT_TEL2 CLNT_TELEPHONE,
D.TWN_NAME,G.COU_NAME, CLNT_EMAIL_ADDRS, CLNT_BUSINESS,NULL POL_DRCR_NO,
DECODE(CLNT_TYPE,'I','INDIVIDUAL','C','CORPORATE',CLNT_TYPE) CLNT_TYPE,
POL_POLICY_NO, NULL POL_REN_ENDOS_NO,
TO_CHAR(ENDR_AUTHORIZATION_DATE,'dd/MMM/RRRR') POL_CHECK_DATE,
TO_CHAR(POL_EFFECTIVE_DATE,'DD/MM/RRRR')||' TO '||TO_CHAR(POL_MATURITY_DATE,'DD/MM/RRRR') COVER_PERIOD,
lms_reports_pkg.POL_STATUS(POL_STATUS)POL_STATUS,NULL,PROD_SHT_DESC,
PROD_DESC,BRN_CODE,BRN_NAME,DECODE(ENDR_PAY_METHOD,'C','CASH','Q','CHEQUE','S','STANDING ORDER',
'K', 'CHECK-OFF','R','STAFF REMITTANCE','DD','DIRECT DEBIT','CASH') PMOD_DESC,ENDR_AUTHORIZED_BY POL_CHECKED_BY,
NVL(ENDR_PAY_METHOD,'C') POL_PMOD_CODE,DECODE(ENDR_PAY_METHOD,'C','CASH','Q','CHEQUE','S','STANDING ORDER',
'K', 'CHECK-OFF','R','STAFF REMITTANCE','DD','DIRECT DEBIT','CASH')PAY_METHOD,
NVL(PCVT_SA,0) TOTAL_SUM_INSURED,
NULL TOTAL_FAP,
NULL TOTAL_GP,
(LMS_ORD_MISC.INST_PREM(POL_CODE)+LMS_PROCESS_RECEIPTS.POL_HOLDER_ALLOC(POL_CODE,'INST')) PREMIUM,
NULL CLM_PAID_AMT ,null,null,
AGN_SHT_DESC, AGN_NAME, AGN_PHYSICAL_ADDRESS, AGN_POSTAL_ADDRESS,
AGN_CONTACT_PERSON, AGN_CONTACT_TITLE, AGN_TEL1||' '||AGN_TEL2, TO_CHAR(AGN_ID_NO),
C.TWN_NAME AGN_TWN_NAME, F.COU_NAME AGN_COU_NAME, A.STS_NAME CLNT_STATE_NAME, B.STS_NAME AGN_STATE_NAME,
DECODE(CLNT_ID_REG_NO,NULL,'Driving Licence','National ID/Passport')CLNT_ID_TYPE,
ACT_ACCOUNT_TYPE ACCOUNT_TYPE
FROM LMS_POLICIES,LMS_ORD_PREM_RECEIPTS,LMS_PROPOSERS,LMS_POLICY_ENDORSEMENTS,
LMS_CLASS_OCCUPATIONS,LMS_PRODUCTS,TQC_CLIENTS,TQC_TOWNS D,TQC_STATES A ,TQC_BRANCHES,
TQC_COUNTRIES G,LMS_AGENCIES, TQC_STATES B,TQC_TOWNS C,TQC_COUNTRIES F,TQC_ACCOUNT_TYPES,
(SELECT PCVT_ENDR_CODE, SUM(PCVT_SA)PCVT_SA FROM LMS_POLICY_COVER_TYPES
GROUP BY PCVT_ENDR_CODE)
WHERE OPR_POL_CODE = POL_CODE
AND POL_PRP_CODE = PRP_CODE(+)
AND PRP_CO_CODE = CO_CODE(+)
AND PRP_CLNT_CODE = CLNT_CODE(+)
AND PRP_TWN_CODE = D.TWN_CODE(+)
AND D.TWN_STS_CODE = A.STS_CODE(+)
AND POL_CURRENT_ENDR_CODE = ENDR_CODE
AND POL_CURRENT_ENDR_CODE = PCVT_ENDR_CODE(+)
AND POL_BRA_CODE= BRN_CODE(+)
AND CLNT_COU_CODE = G.COU_CODE(+)
AND NOT POL_STATUS IN ('D','V')
AND POL_PROD_CODE = PROD_CODE
AND ENDR_AGEN_CODE =AGN_CODE
AND AGN_TWN_CODE = C.TWN_CODE(+)
AND C.TWN_STS_CODE = B.STS_CODE(+)
AND AGN_COU_CODE = F.COU_CODE(+)
AND AGN_ACT_CODE = ACT_CODE
UNION ALL
SELECT CLNT_SHT_DESC,CLNT_TITLE, CLNT_NAME, CLNT_OTHER_NAMES,CLNT_SURNAME, NVL(CLNT_ID_REG_NO,PRP_ID_REG_NO)CLNT_ID_REG_NO, CLNT_PIN,
CLNT_PHYSICAL_ADDRS,TO_CHAR(CLNT_DOB,'dd/MM/RRRR') CLNT_DOB, CLNT_DOMICILE_COUNTRIES,
CLNT_POSTAL_ADDRS||' '||CLNT_ZIP_CODE CLNT_POSTAL_ADDRS,CLNT_TEL||', '|| CLNT_TEL2 CLNT_TELEPHONE,
D.TWN_NAME,G.COU_NAME, CLNT_EMAIL_ADDRS, CLNT_BUSINESS,NULL POL_DRCR_NO,
DECODE(CLNT_TYPE,'I','INDIVIDUAL','C','CORPORATE',CLNT_TYPE) CLNT_TYPE,
POL_POLICY_NO, NULL POL_REN_ENDOS_NO,
TO_CHAR(ENDR_AUTHORIZATION_DATE,'DD/MM/RRRR') POL_CHECK_DATE,
TO_CHAR(POL_EFFECTIVE_DATE,'DD/MM/RRRR')||' TO '||TO_CHAR(POL_MATURITY_DATE,'DD/MM/RRRR') COVER_PERIOD,
lms_reports_pkg.POL_STATUS(POL_STATUS)POL_STATUS,NULL,PROD_SHT_DESC,
PROD_DESC,BRN_CODE,BRN_NAME,DECODE(ENDR_PAY_METHOD,'C','CASH','Q','CHEQUE','S','STANDING ORDER',
'K', 'CHECK-OFF','R','STAFF REMITTANCE','DD','DIRECT DEBIT','CASH') PMOD_DESC,ENDR_AUTHORIZED_BY POL_CHECKED_BY,
NVL(ENDR_PAY_METHOD,'C') POL_PMOD_CODE,DECODE(ENDR_PAY_METHOD,'C','CASH','Q','CHEQUE','S','STANDING ORDER',
'K', 'CHECK-OFF','R','STAFF REMITTANCE','DD','DIRECT DEBIT','CASH')PAY_METHOD,
NVL(PCVT_SA,0) TOTAL_SUM_INSURED,
NULL TOTAL_FAP,
NULL TOTAL_GP,
(LMS_ORD_MISC.INST_PREM(POL_CODE)+LMS_PROCESS_RECEIPTS.POL_HOLDER_ALLOC(POL_CODE,'INST')) PREMIUM,
NVL(CPVD_AMOUNT,0) CLM_PAID_AMT,nulL,null,
AGN_SHT_DESC, AGN_NAME, AGN_PHYSICAL_ADDRESS, AGN_POSTAL_ADDRESS,
AGN_CONTACT_PERSON, AGN_CONTACT_TITLE, AGN_TEL1||' '||AGN_TEL2, TO_CHAR(AGN_ID_NO),
C.TWN_NAME AGN_TWN_NAME, F.COU_NAME AGN_COU_NAME, A.STS_NAME CLNT_STATE_NAME, B.STS_NAME AGN_STATE_NAME,
DECODE(CLNT_ID_REG_NO,NULL,'Driving Licence','National ID/Passport')CLNT_ID_TYPE,
ACT_ACCOUNT_TYPE ACCOUNT_TYPE
FROM LMS_POLICIES,LMS_PROPOSERS,LMS_POLICY_ENDORSEMENTS,
LMS_CLASS_OCCUPATIONS,LMS_PRODUCTS,TQC_CLIENTS,TQC_TOWNS D,TQC_STATES A,TQC_BRANCHES,
TQC_COUNTRIES G,LMS_CLAIMS,LMS_AGENCIES, TQC_STATES B,TQC_TOWNS C,TQC_COUNTRIES F,TQC_ACCOUNT_TYPES,
(SELECT PCVT_ENDR_CODE, SUM(PCVT_SA)PCVT_SA FROM LMS_POLICY_COVER_TYPES
GROUP BY PCVT_ENDR_CODE),
(SELECT CPVD_CLM_NO, SUM(CPVD_AMOUNT)CPVD_AMOUNT FROM LMS_CLM_PAYMENT_VOUCHER_DTLS
GROUP BY CPVD_CLM_NO)
WHERE CLM_POL_CODE = POL_CODE
AND POL_PRP_CODE = PRP_CODE(+)
AND PRP_CO_CODE = CO_CODE(+)
AND PRP_CLNT_CODE = CLNT_CODE(+)
AND PRP_TWN_CODE = D.TWN_CODE(+)
AND D.TWN_STS_CODE = A.STS_CODE(+)
AND POL_CURRENT_ENDR_CODE = ENDR_CODE
AND POL_CURRENT_ENDR_CODE = PCVT_ENDR_CODE(+)
AND POL_BRA_CODE= BRN_CODE(+)
AND CLNT_COU_CODE = G.COU_CODE(+)
AND CLM_NO = CPVD_CLM_NO
AND POL_PROD_CODE = PROD_CODE
AND ENDR_AGEN_CODE =AGN_CODE
AND AGN_TWN_CODE = C.TWN_CODE(+)
AND C.TWN_STS_CODE = B.STS_CODE(+)
AND AGN_COU_CODE = F.COU_CODE(+)
AND AGN_ACT_CODE = ACT_CODE);