--
-- TQC_AGENT_AUDIT_TRIG  (Trigger) 
--
CREATE OR REPLACE TRIGGER TQ_CRM.TQC_AGENT_AUDIT_TRIG
AFTER DELETE OR INSERT OR UPDATE
ON TQ_CRM.TQC_AGENCIES REFERENCING NEW AS New OLD AS Old
FOR EACH ROW
DECLARE
v_ChangeType varchar2(1);
V_Posted_By varchar2(100) := Pkg_Global_Vars.Get_Pvarchar2('pkg_global_vars.pvg_username');
V_AGN_CODE Number:=:NEW.AGN_CODE;
V_No Number:=TQC_agnl_NO_SEQ.NEXTVAL;
V_Field Varchar2(200);
V_Action Varchar2(200);
V_Old_Val Varchar2(200);
V_New_Val Varchar2(200);
V_Date Date := SYSDATE;

BEGIN
   
   IF INSERTING THEN
       v_ChangeType := 'A';
   ELSIF UPDATING THEN
       v_ChangeType := 'E';
   ELSE
       v_ChangeType := 'D';
   END IF;
   
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'SHORT_DESC', v_ChangeType, :OLD.AGN_SHT_DESC, :NEW.AGN_SHT_DESC, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'NAME', v_ChangeType, :OLD.AGN_NAME, :NEW.AGN_NAME, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'PHYSICAL_ADDRESS', v_ChangeType, :OLD.AGN_PHYSICAL_ADDRESS, :NEW.AGN_PHYSICAL_ADDRESS, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'POSTAL_ADDRESS', v_ChangeType, :OLD.AGN_POSTAL_ADDRESS, :NEW.AGN_POSTAL_ADDRESS, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'TWN_CODE', v_ChangeType, :OLD.AGN_TWN_CODE, :NEW.AGN_TWN_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'COU_CODE', v_ChangeType, :OLD.AGN_COU_CODE, :NEW.AGN_COU_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'EMAIL_ADDRESS', v_ChangeType, :OLD.AGN_EMAIL_ADDRESS, :NEW.AGN_EMAIL_ADDRESS, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'WEB_ADDRESS', v_ChangeType, :OLD.AGN_WEB_ADDRESS, :NEW.AGN_WEB_ADDRESS, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'ZIP', v_ChangeType, :OLD.AGN_ZIP, :NEW.AGN_ZIP, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CONTACT_PERSON', v_ChangeType, :OLD.AGN_CONTACT_PERSON, :NEW.AGN_CONTACT_PERSON, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CONTACT_TITLE', v_ChangeType, :OLD.AGN_CONTACT_TITLE, :NEW.AGN_CONTACT_TITLE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'TEL1', v_ChangeType, :OLD.AGN_TEL1, :NEW.AGN_TEL1, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'TEL2', v_ChangeType, :OLD.AGN_TEL2, :NEW.AGN_TEL2, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'FAX', v_ChangeType, :OLD.AGN_FAX, :NEW.AGN_FAX, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'ACC_NO', v_ChangeType, :OLD.AGN_ACC_NO, :NEW.AGN_ACC_NO, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'PIN', v_ChangeType, :OLD.AGN_PIN, :NEW.AGN_PIN, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AGENT_COMMISSION', v_ChangeType, :OLD.AGN_AGENT_COMMISSION, :NEW.AGN_AGENT_COMMISSION, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CREDIT_ALLOWED', v_ChangeType, :OLD.AGN_CREDIT_ALLOWED, :NEW.AGN_CREDIT_ALLOWED, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AGENT_WHT_TAX', v_ChangeType, :OLD.AGN_AGENT_WHT_TAX, :NEW.AGN_AGENT_WHT_TAX, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'PRINT_DBNOTE', v_ChangeType, :OLD.AGN_PRINT_DBNOTE, :NEW.AGN_PRINT_DBNOTE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'STATUS', v_ChangeType, :OLD.AGN_STATUS, :NEW.AGN_STATUS, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'DATE_CREATED', v_ChangeType, :OLD.AGN_DATE_CREATED, :NEW.AGN_DATE_CREATED, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CREATED_BY', v_ChangeType, :OLD.AGN_CREATED_BY, :NEW.AGN_CREATED_BY, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'REG_CODE', v_ChangeType, :OLD.AGN_REG_CODE, :NEW.AGN_REG_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'COMM_RESERVE_RATE', v_ChangeType, :OLD.AGN_COMM_RESERVE_RATE, :NEW.AGN_COMM_RESERVE_RATE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'ANNUAL_BUDGET', v_ChangeType, :OLD.AGN_ANNUAL_BUDGET, :NEW.AGN_ANNUAL_BUDGET, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'STATUS_EFF_DATE', v_ChangeType, :OLD.AGN_STATUS_EFF_DATE, :NEW.AGN_STATUS_EFF_DATE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CREDIT_PERIOD', v_ChangeType, :OLD.AGN_CREDIT_PERIOD, :NEW.AGN_CREDIT_PERIOD, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'COMM_STAT_EFF_DT', v_ChangeType, :OLD.AGN_COMM_STAT_EFF_DT, :NEW.AGN_COMM_STAT_EFF_DT, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'COMM_STATUS_DT', v_ChangeType, :OLD.AGN_COMM_STATUS_DT, :NEW.AGN_COMM_STATUS_DT, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'COMM_ALLOWED', v_ChangeType, :OLD.AGN_COMM_ALLOWED, :NEW.AGN_COMM_ALLOWED, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CHECKED', v_ChangeType, :OLD.AGN_CHECKED, :NEW.AGN_CHECKED, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CHECKED_BY', v_ChangeType, :OLD.AGN_CHECKED_BY, :NEW.AGN_CHECKED_BY, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CHECK_DATE', v_ChangeType, :OLD.AGN_CHECK_DATE, :NEW.AGN_CHECK_DATE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'COMP_COMM_ARREARS', v_ChangeType, :OLD.AGN_COMP_COMM_ARREARS, :NEW.AGN_COMP_COMM_ARREARS, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'REINSURER', v_ChangeType, :OLD.AGN_REINSURER, :NEW.AGN_REINSURER, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BRN_CODE', v_ChangeType, :OLD.AGN_BRN_CODE, :NEW.AGN_BRN_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'TOWN', v_ChangeType, :OLD.AGN_TOWN, :NEW.AGN_TOWN, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'COUNTRY', v_ChangeType, :OLD.AGN_COUNTRY, :NEW.AGN_COUNTRY, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'STATUS_DESC', v_ChangeType, :OLD.AGN_STATUS_DESC, :NEW.AGN_STATUS_DESC, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'ID_NO', v_ChangeType, :OLD.AGN_ID_NO, :NEW.AGN_ID_NO, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CON_CODE', v_ChangeType, :OLD.AGN_CON_CODE, :NEW.AGN_CON_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'SMS_TEL', v_ChangeType, :OLD.AGN_SMS_TEL, :NEW.AGN_SMS_TEL, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AHC_CODE', v_ChangeType, :OLD.AGN_AHC_CODE, :NEW.AGN_AHC_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'SEC_CODE', v_ChangeType, :OLD.AGN_SEC_CODE, :NEW.AGN_SEC_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AGNC_CLASS_CODE', v_ChangeType, :OLD.AGN_AGNC_CLASS_CODE, :NEW.AGN_AGNC_CLASS_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'EXPIRY_DATE', v_ChangeType, :OLD.AGN_EXPIRY_DATE, :NEW.AGN_EXPIRY_DATE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'LICENSE_NO', v_ChangeType, :OLD.AGN_LICENSE_NO, :NEW.AGN_LICENSE_NO, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'RUNOFF', v_ChangeType, :OLD.AGN_RUNOFF, :NEW.AGN_RUNOFF, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'LICENSED', v_ChangeType, :OLD.AGN_LICENSED, :NEW.AGN_LICENSED, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'LICENSE_GRACE_PR', v_ChangeType, :OLD.AGN_LICENSE_GRACE_PR, :NEW.AGN_LICENSE_GRACE_PR, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'OLD_ACC_NO', v_ChangeType, :OLD.AGN_OLD_ACC_NO, :NEW.AGN_OLD_ACC_NO, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'STATUS_REMARKS', v_ChangeType, :OLD.AGN_STATUS_REMARKS, :NEW.AGN_STATUS_REMARKS, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'OSD_CODE', v_ChangeType, :OLD.AGN_OSD_CODE, :NEW.AGN_OSD_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BANK_ACC_NO', v_ChangeType, :OLD.AGN_BANK_ACC_NO, :NEW.AGN_BANK_ACC_NO, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'UNIQUE_PREFIX', v_ChangeType, :OLD.AGN_UNIQUE_PREFIX, :NEW.AGN_UNIQUE_PREFIX, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'STATE_CODE', v_ChangeType, :OLD.AGN_STATE_CODE, :NEW.AGN_STATE_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CRDT_RTING', v_ChangeType, :OLD.AGN_CRDT_RTING, :NEW.AGN_CRDT_RTING, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CLNT_CODE', v_ChangeType, :OLD.AGN_CLNT_CODE, :NEW.AGN_CLNT_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BIRTH_DATE', v_ChangeType, :OLD.AGN_BIRTH_DATE, :NEW.AGN_BIRTH_DATE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'CREDIT_LIMIT', v_ChangeType, :OLD.AGN_CREDIT_LIMIT, :NEW.AGN_CREDIT_LIMIT, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BRU_CODE', v_ChangeType, :OLD.AGN_BRU_CODE, :NEW.AGN_BRU_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'LOCAL_INTERNATIONAL', v_ChangeType, :OLD.AGN_LOCAL_INTERNATIONAL, :NEW.AGN_LOCAL_INTERNATIONAL, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'REGULATOR_NUMBER', v_ChangeType, :OLD.AGN_REGULATOR_NUMBER, :NEW.AGN_REGULATOR_NUMBER, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AUTHORISED', v_ChangeType, :OLD.AGN_AUTHORISED, :NEW.AGN_AUTHORISED, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AUTHORISED_BY', v_ChangeType, :OLD.AGN_AUTHORISED_BY, :NEW.AGN_AUTHORISED_BY, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AUTHORISED_DATE', v_ChangeType, :OLD.AGN_AUTHORISED_DATE, :NEW.AGN_AUTHORISED_DATE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'RORG_CODE', v_ChangeType, :OLD.AGN_RORG_CODE, :NEW.AGN_RORG_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'ORS_CODE', v_ChangeType, :OLD.AGN_ORS_CODE, :NEW.AGN_ORS_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'ALLOCATE_CERT', v_ChangeType, :OLD.AGN_ALLOCATE_CERT, :NEW.AGN_ALLOCATE_CERT, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BOUNCED_CHQ', v_ChangeType, :OLD.AGN_BOUNCED_CHQ, :NEW.AGN_BOUNCED_CHQ, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BPN_CODE', v_ChangeType, :OLD.AGN_BPN_CODE, :NEW.AGN_BPN_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AGENT_TYPE', v_ChangeType, :OLD.AGN_AGENT_TYPE, :NEW.AGN_AGENT_TYPE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'GROUP', v_ChangeType, :OLD.AGN_GROUP, :NEW.AGN_GROUP, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'SUBAGENT', v_ChangeType, :OLD.AGN_SUBAGENT, :NEW.AGN_SUBAGENT, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'PAYEE', v_ChangeType, :OLD.AGN_PAYEE, :NEW.AGN_PAYEE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'ENABLE_WEB_EDIT', v_ChangeType, :OLD.AGN_ENABLE_WEB_EDIT, :NEW.AGN_ENABLE_WEB_EDIT, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'ACCOUNT_MANAGER', v_ChangeType, :OLD.AGN_ACCOUNT_MANAGER, :NEW.AGN_ACCOUNT_MANAGER, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'DEFAULT_COMM_MODE', v_ChangeType, :OLD.AGN_DEFAULT_COMM_MODE, :NEW.AGN_DEFAULT_COMM_MODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'VAT_APPLICABLE', v_ChangeType, :OLD.AGN_VAT_APPLICABLE, :NEW.AGN_VAT_APPLICABLE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'WHTAX_APPLICABLE', v_ChangeType, :OLD.AGN_WHTAX_APPLICABLE, :NEW.AGN_WHTAX_APPLICABLE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'TEL_PAY', v_ChangeType, :OLD.AGN_TEL_PAY, :NEW.AGN_TEL_PAY, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'PAYMENT_FREQ', v_ChangeType, :OLD.AGN_PAYMENT_FREQ, :NEW.AGN_PAYMENT_FREQ, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'DEFAULT_CPM_MODE', v_ChangeType, :OLD.AGN_DEFAULT_CPM_MODE, :NEW.AGN_DEFAULT_CPM_MODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'PYMT_VALIDATED', v_ChangeType, :OLD.AGN_PYMT_VALIDATED, :NEW.AGN_PYMT_VALIDATED, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'QUALIFICATION', v_ChangeType, :OLD.AGN_QUALIFICATION, :NEW.AGN_QUALIFICATION, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'MARITAL_STATUS', v_ChangeType, :OLD.AGN_MARITAL_STATUS, :NEW.AGN_MARITAL_STATUS, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BENEFIT_START_DATE', v_ChangeType, :OLD.AGN_BENEFIT_START_DATE, :NEW.AGN_BENEFIT_START_DATE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'ID_NO_DOC_USED', v_ChangeType, :OLD.AGN_ID_NO_DOC_USED, :NEW.AGN_ID_NO_DOC_USED, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AGNTY_CODE', v_ChangeType, :OLD.AGN_AGNTY_CODE, :NEW.AGN_AGNTY_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'SBU_CODE', v_ChangeType, :OLD.AGN_SBU_CODE, :NEW.AGN_SBU_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'COMM_LEVY_APP', v_ChangeType, :OLD.AGN_COMM_LEVY_APP, :NEW.AGN_COMM_LEVY_APP, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'COMM_LEVY_RATE', v_ChangeType, :OLD.AGN_COMM_LEVY_RATE, :NEW.AGN_COMM_LEVY_RATE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BBR_ACC_CODE', v_ChangeType, :OLD.AGN_BBR_ACC_CODE, :NEW.AGN_BBR_ACC_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BBR_CODE', v_ChangeType, :OLD.AGN_BBR_CODE, :NEW.AGN_BBR_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BRR_CODE', v_ChangeType, :OLD.AGN_BRR_CODE, :NEW.AGN_BRR_CODE, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'BRR_NAME', v_ChangeType, :OLD.AGN_BRR_NAME, :NEW.AGN_BRR_NAME, V_Date, V_Posted_By );
    Tqc_Agencies_Pkg.Agent_Field_Log_Prc( V_Agn_Code, V_No, 'AUTH_NAME', v_ChangeType, :OLD.AGN_AUTH_NAME, :NEW.AGN_AUTH_NAME, V_Date, V_Posted_By );


   EXCEPTION
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       raise_application_error (-20015, 'Error agent audit log - ' || SQLERRM (SQLCODE));
END TQC_client_AUDIT_TRIG;
/