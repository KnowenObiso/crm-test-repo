/* This object may not be sorted properly in the script due to cirular references. */
--
-- UPDATE_PROPOSER  (Procedure) 
--
CREATE OR REPLACE PROCEDURE TQ_CRM."UPDATE_PROPOSER" (
                                                    v_clnt_code                 TQC_CLIENTS.CLNT_CODE%TYPE, 
                                                    v_prp_code                  LMS_PROPOSERS.PRP_CODE%TYPE DEFAULT NULL,
                                                    v_prp_co_code               LMS_PROPOSERS.PRP_CO_CODE%TYPE DEFAULT NULL,
                                                    v_prp_business              LMS_PROPOSERS.PRP_BUSINESS%TYPE DEFAULT NULL,
                                                    v_prp_cover_history         LMS_PROPOSERS.PRP_COVER_HISTORY%TYPE DEFAULT NULL, 
                                                    v_prp_family_history        LMS_PROPOSERS.PRP_FAMILY_HISTORY%TYPE DEFAULT NULL, 
                                                    v_prp_personal_history      LMS_PROPOSERS.PRP_PERSONAL_HISTORY%TYPE DEFAULT NULL,
                                                    v_zip_code                  LMS_PROPOSERS.PRP_ZIP_CODE%TYPE DEFAULT NULL,
                                                    v_prp_type                  LMS_PROPOSERS.PRP_TYPE%TYPE DEFAULT NULL,
                                                    v_dob_admitted              LMS_PROPOSERS.PRP_DOB_ADMITTED%TYPE DEFAULT NULL,
                                                    v_prp_marital_status        LMS_PROPOSERS.PRP_MARITAL_STATUS%TYPE DEFAULT NULL,
                                                    v_prp_correspondence        LMS_PROPOSERS.PRP_CORRESPONDENCE%TYPE DEFAULT NULL,
                                                    v_prp_natinality            LMS_PROPOSERS.PRP_NATIONALITY%TYPE DEFAULT NULL,
                                                    v_prp_payroll_no            LMS_PROPOSERS.PRP_PAYROLL_NO%TYPE DEFAULT NULL,
                                                    v_digit_code                LMS_PROPOSERS.PRP_PAYROLL_NO%TYPE DEFAULT NULL,
                                                    v_prp_identf_doc_used       LMS_PROPOSERS.PRP_IDNTF_DOC_USED%TYPE DEFAULT 'I',
                                                    v_twn_code                  NUMBER DEFAULT NULL,
                                                    v_monthly_income            LMS_PROPOSERS.PRP_MONTHLY_INCOME%TYPE DEFAULT NULL) IS
                                                    --v_prp_is_beneficiary        LMS_PROPOSERS.PRP_IS_BENEFICIARY%TYPE DEFAULT 'N'
CURSOR c_client IS 
SELECT *
FROM TQC_CLIENTS
WHERE CLNT_CODE=v_clnt_code;
    v_town_name  VARCHAR2(30);
BEGIN
 BEGIN
    SELECT UPPER(TWN_NAME)
    INTO v_town_name
    FROM TQC_TOWNS
    WHERE TWN_CODE=v_twn_code;  
 EXCEPTION
 WHEN OTHERS THEN
 v_town_name :=null;
   
 END;
    
    FOR C IN c_client LOOP
        BEGIN
        UPDATE LMS_PROPOSERS 
        SET PRP_TITLE=NVL(C.CLNT_TITLE,PRP_TITLE),
            PRP_SURNAME=NVL(C.CLNT_NAME,PRP_SURNAME), 
            PRP_OTHER_NAMES=NVL(C.CLNT_OTHER_NAMES,PRP_OTHER_NAMES),
            PRP_SEX=NVL(C.CLNT_GENDER,PRP_SEX),
            PRP_ACC_NO=NVL(C.CLNT_ACCNT_NO,PRP_ACC_NO),
            PRP_TEL=NVL(C.CLNT_TEL, PRP_TEL),
            PRP_TEL2=NVL(C.CLNT_TEL2, PRP_TEL2),
            PRP_ID_REG_NO=NVL(C.CLNT_ID_REG_NO,PRP_ID_REG_NO),
            PRP_PIN= NVL(C.CLNT_PIN,PRP_PIN ),
            PRP_DOB=NVL(C.CLNT_DOB,PRP_DOB ), 
            PRP_BANK_ACC_NO=NVL(C.CLNT_BANK_ACC_NO,PRP_BANK_ACC_NO ),
            PRP_BBR_CODE=NVL(C.CLNT_BBR_CODE,PRP_BBR_CODE),
            PRP_BUSINESS=NVL(v_prp_business,PRP_BUSINESS),
             PRP_POSTAL_ADDRESS=NVL(C.CLNT_POSTAL_ADDRS,PRP_POSTAL_ADDRESS),
             PRP_ZIP_CODE=NVL(NVL(C.CLNT_ZIP_CODE,PRP_ZIP_CODE),v_zip_code),
             PRP_PHYSICAL_ADDR=NVL(C.CLNT_PHYSICAL_ADDRS,PRP_PHYSICAL_ADDR),
             PRP_CLNT_SMS_TEL=NVL(C.CLNT_SMS_TEL, PRP_CLNT_SMS_TEL),
             PRP_FAX=NVL(C.CLNT_FAX,PRP_FAX ),
             PRP_EMAIL=NVL(C.CLNT_EMAIL_ADDRS,PRP_EMAIL),
             PRP_REMARKS=NVL(C.CLNT_REMARKS,PRP_REMARKS), 
             PRP_SPCL_TERMS=NVL(C.CLNT_SPCL_TERMS,PRP_SPCL_TERMS),
             PRP_INCREASED_PREMIUM=NVL(C.CLNT_INCREASED_PREMIUM,PRP_INCREASED_PREMIUM),
             PRP_DECLINED_PROP=NVL(C.CLNT_DECLINED_PROP,PRP_DECLINED_PROP),
             PRP_POLICY_CANCELLED=NVL(C.CLNT_POLICY_CANCELLED,PRP_POLICY_CANCELLED),
             PRP_CO_CODE         = NVL(v_prp_co_code,PRP_CO_CODE),
             PRP_COVER_HISTORY = NVL(v_prp_cover_history,PRP_COVER_HISTORY),
             PRP_FAMILY_HISTORY = NVL(v_prp_family_history,PRP_FAMILY_HISTORY),
             PRP_PERSONAL_HISTORY = NVL(v_prp_personal_history,PRP_PERSONAL_HISTORY),
             PRP_TYPE = NVL(v_prp_type,PRP_TYPE),
             PRP_DOB_ADMITTED  = NVL(v_dob_admitted,PRP_DOB_ADMITTED),
             PRP_MARITAL_STATUS = NVL(v_prp_marital_status,PRP_MARITAL_STATUS),
             PRP_CORRESPONDENCE = NVL(v_prp_correspondence,PRP_CORRESPONDENCE),
             PRP_NATIONALITY = NVL(PRP_NATIONALITY,v_prp_natinality),
             PRP_PAYROLL_NO= nvl(v_prp_payroll_no,PRP_PAYROLL_NO),
             PRP_DIGIT_CODE=NVL(v_digit_code,PRP_DIGIT_CODE),
             PRP_IDNTF_DOC_USED=NVL(v_prp_identf_doc_used,'I'),
             PRP_TOWN=NVL(v_town_name,PRP_TOWN),
             PRP_TWN_CODE=NVL(v_twn_code,PRP_TWN_CODE),
             PRP_MONTHLY_INCOME=nvl(v_monthly_income, PRP_MONTHLY_INCOME),
             PRP_BRN_CODE=tqc_clients_pkg.get_lms_brn_code ( C.CLNT_BRN_CODE )
            -- PRP_IS_BENEFICIARY= NVL(v_prp_is_beneficiary,PRP_IS_BENEFICIARY)
            -- PRP_ZIP_CODE = NVL(v_prp_zip_code,PRP_ZIP_CODE)
             
          WHERE PRP_CLNT_CODE=C.CLNT_CODE; 
       EXCEPTION
         WHEN OTHERS THEN
           -- Consider logging the error and then re-raise
           RAISE_APPLICATION_ERROR(-20113,'ERROR UPDATING LMS_PROPOSERS' );
       END;
    END LOOP;
END update_proposer; 
 
/