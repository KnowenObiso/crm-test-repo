--
-- TQC_DIRECT_DEBIT_VIEW  (View) 
--
CREATE OR REPLACE FORCE VIEW TQ_CRM.TQC_DIRECT_DEBIT_VIEW
(DDD_CODE, DDD_DDH_CODE, DDD_DD_CODE, DDD_SYS_CODE, DDD_POL_PROPOSAL_NO, 
 DDD_POL_POLICY_NO, DDD_AMOUNT, DDD_REMARKS, DDH_CLNT_SHT_DESC, DDH_CLNT_NAME)
AS 
SELECT tqc_direct_debit_detail.ddd_code, tqc_direct_debit_detail.ddd_ddh_code,tqc_direct_debit_detail.ddd_dd_code, 
tqc_direct_debit_detail.ddd_sys_code,tqc_direct_debit_detail.ddd_pol_proposal_no,tqc_direct_debit_detail.ddd_pol_policy_no, 
tqc_direct_debit_detail.ddd_amount,tqc_direct_debit_detail.ddd_remarks,tqc_direct_debit_header.ddh_clnt_sht_desc,tqc_direct_debit_header.ddh_clnt_name 
FROM tqc_direct_debit_detail, tqc_direct_debit_header 
WHERE ((tqc_direct_debit_header.ddh_code =tqc_direct_debit_detail.ddd_ddh_code));