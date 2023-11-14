--
-- CLIENTS  (View) 
--
CREATE OR REPLACE FORCE VIEW TQ_CRM.CLIENTS
(CLNT_CODE, CLNT_SHT_DESC, CLNT_NAME, CLNT_OTHER_NAMES, CLNT_ID_REG_NO, 
 CLNT_DOB, CLNT_PIN, CLNT_PHYSICAL_ADDRS, CLNT_POSTAL_ADDRS, CLNT_TWN_CODE, 
 CLNT_COU_CODE, CLNT_EMAIL_ADDRS, CLNT_TEL, CLNT_TEL2, CLNT_STATUS, 
 CLNT_FAX, CLNT_REMARKS, CLNT_SPCL_TERMS, CLNT_DECLINED_PROP, CLNT_INCREASED_PREMIUM, 
 CLNT_POLICY_CANCELLED, CLNT_PROPOSER, CLNT_ACCNT_NO, CLNT_DOMICILE_COUNTRIES, CLNT_WEF, 
 CLNT_WET, CLNT_WITHDRAWAL_REASON, CLNT_SEC_CODE, CLNT_SURNAME, CLNT_TYPE, 
 CLNT_TITLE, CLNT_BUSINESS, CLNT_ZIP_CODE, CLNT_BBR_CODE, CLNT_BANK_ACC_NO, 
 CLNT_CLNT_CODE, CANNON_LIFE_POL_NO, CLNT_NON_DIRECT, CLNT_CREATED_BY, CLNT_SMS_TEL, 
 CLNT_AGNT_STATUS, CLNT_DATE_CREATED, CLNT_RUNOFF, CLNT_LOADED_BY, CLNT_DIRECT_CLIENT, 
 CLNT_OLD_ACCNT_NO, CLNT_AGNT_CLIENT_ID, CLNT_GENDER, CLNT_USR_CODE, CLNT_CRDT_ALLWD, 
 CLNT_CRDT_MAX_AMT, CLNT_LOC_CODE, CLNT_UPDATE_DT, CLNT_UPDATED_BY, CLNT_UPDATED, 
 CLNT_CRM_ID, CLNT_IMAGE, CLNT_BDIV_CODE, CLNT_CNTCT_PHONE_1, CLNT_CNTCT_EMAIL_1, 
 CLNT_CNTCT_PHONE_2, CLNT_CNTCT_EMAIL_2, CLNT_CNTCT_NAME_1, CLNT_CNTCT_NAME_2, CLNT_PASSPORT_NO, 
 CLNT_STS_CODE, CLNT_SIGNATURE, CLNT_WEBSITE, CLNT_AUDITORS, CLNT_CURRENT_INSURER, 
 CLNT_PROJECTED_BUSINESS, CLNT_DATE_OF_EMPL, CLNT_DRIVING_LICENCE, CLNT_PARENT_COMPANY, CLNT_BRN_CODE)
AS 
SELECT "CLNT_CODE","CLNT_SHT_DESC","CLNT_NAME","CLNT_OTHER_NAMES","CLNT_ID_REG_NO","CLNT_DOB","CLNT_PIN","CLNT_PHYSICAL_ADDRS","CLNT_POSTAL_ADDRS","CLNT_TWN_CODE","CLNT_COU_CODE","CLNT_EMAIL_ADDRS","CLNT_TEL","CLNT_TEL2","CLNT_STATUS","CLNT_FAX","CLNT_REMARKS","CLNT_SPCL_TERMS","CLNT_DECLINED_PROP","CLNT_INCREASED_PREMIUM","CLNT_POLICY_CANCELLED","CLNT_PROPOSER","CLNT_ACCNT_NO","CLNT_DOMICILE_COUNTRIES","CLNT_WEF","CLNT_WET","CLNT_WITHDRAWAL_REASON","CLNT_SEC_CODE","CLNT_SURNAME","CLNT_TYPE","CLNT_TITLE","CLNT_BUSINESS","CLNT_ZIP_CODE","CLNT_BBR_CODE","CLNT_BANK_ACC_NO","CLNT_CLNT_CODE","CANNON_LIFE_POL_NO","CLNT_NON_DIRECT","CLNT_CREATED_BY","CLNT_SMS_TEL","CLNT_AGNT_STATUS","CLNT_DATE_CREATED","CLNT_RUNOFF","CLNT_LOADED_BY","CLNT_DIRECT_CLIENT","CLNT_OLD_ACCNT_NO","CLNT_AGNT_CLIENT_ID","CLNT_GENDER","CLNT_USR_CODE","CLNT_CRDT_ALLWD","CLNT_CRDT_MAX_AMT","CLNT_LOC_CODE","CLNT_UPDATE_DT","CLNT_UPDATED_BY","CLNT_UPDATED","CLNT_CRM_ID","CLNT_IMAGE","CLNT_BDIV_CODE","CLNT_CNTCT_PHONE_1","CLNT_CNTCT_EMAIL_1","CLNT_CNTCT_PHONE_2","CLNT_CNTCT_EMAIL_2","CLNT_CNTCT_NAME_1","CLNT_CNTCT_NAME_2","CLNT_PASSPORT_NO","CLNT_STS_CODE","CLNT_SIGNATURE","CLNT_WEBSITE","CLNT_AUDITORS","CLNT_CURRENT_INSURER","CLNT_PROJECTED_BUSINESS","CLNT_DATE_OF_EMPL","CLNT_DRIVING_LICENCE","CLNT_PARENT_COMPANY","CLNT_BRN_CODE" FROM TQC_CLIENTS;