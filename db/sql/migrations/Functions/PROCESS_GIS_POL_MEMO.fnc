--
-- PROCESS_GIS_POL_MEMO  (Function) 
--
CREATE OR REPLACE FUNCTION TQ_CRM."PROCESS_GIS_POL_MEMO" (v_pol_batch_no IN NUMBER,
                        v_claim_no IN VARCHAR2,
                        v_gcc_code IN NUMBER,
                        v_raw_txt IN VARCHAR2,
                        v_app_lvl IN VARCHAR2) RETURN VARCHAR2 IS

    lv_temp_txt       VARCHAR2(2500);
    v_pol_no          VARCHAR2(25);
    v_clm_no          VARCHAR2(25);
    v_sa              NUMBER;
    v_premium         NUMBER;
    v_client          VARCHAR2(75);
    v_prem            NUMBER;
    v_date            DATE;
    v_time            DATE;
    v_reg             VARCHAR2(30);
    v_loc             VARCHAR2(50);
    v_courtname       VARCHAR2(50);
    v_courtdate       DATE ;
    v_caseno          VARCHAR2(50);
    vcoverfrom        DATE ;
    vcoverto          DATE ;
    veffdate          DATE ;
    vrendate          DATE;
    v_desc            VARCHAR2(50);
    v_loss_type       VARCHAR2(50);
    v_item_desc       VARCHAR2(50);
    v_claimant_type   VARCHAR2(15);
    v_agent           VARCHAR2(30);
    v_int_parties     VARCHAR2(30);
    v_uw_year         INT;
    v_branch          VARCHAR2(30); 
    v_address         VARCHAR2(30);
    v_twn             VARCHAR2(30);
    v_risk_id         VARCHAR2(50);
    v_risk_desc       VARCHAR2(50);
    v_risk_wef        DATE;
    v_risk_wet        DATE;
    v_cover           VARCHAR2(50);
    v_risk_prem       DECIMAL(20,2);
    v_risk_value      DECIMAL(20,2);
    v_currency        VARCHAR2(5);

    BEGIN
    lv_temp_txt := v_raw_txt;

    IF v_app_lvl = 'U' THEN
        IF v_pol_batch_no IS NOT NULL THEN
        BEGIN
              
             SELECT POL_POLICY_NO,POL_TOTAL_SUM_INSURED,GIS_UTILITIES.CLNT_NAME(CLNT_NAME,CLNT_OTHER_NAMES) CLNT_NAME,POL_TOTAL_FAP,
             POL_POLICY_COVER_FROM,POL_POLICY_COVER_TO,POL_WEF_DT,POL_RENEWAL_DT,AGN_NAME,POL_OTH_INT_PARTIES,
             POL_UW_YEAR,BRN_NAME,CLNT_POSTAL_ADDRS,TWN_NAME,IPU_PROPERTY_ID,IPU_ITEM_DESC,IPU_WEF,IPU_WET,IPU_COVT_SHT_DESC,IPU_ENDOS_DIFF_AMT,IPU_VALUE               
             INTO v_pol_no,v_sa,v_client,v_prem,vcoverfrom, vcoverto,veffdate, vrendate,v_agent,v_int_parties,v_uw_year,
             v_branch,v_address,v_twn,v_risk_id,v_risk_desc,v_risk_wef,v_risk_wet,v_cover,v_risk_prem,v_risk_value
             FROM GIN_POLICIES ,TQC_CLIENTS,TQC_AGENCIES,TQC_BRANCHES,TQC_TOWNS,GIN_INSURED_PROPERTY_UNDS
             WHERE POL_PRP_CODE= CLNT_CODE
             AND POL_AGNT_AGENT_CODE=AGN_CODE
             AND TWN_CODE(+)=CLNT_TWN_CODE
             AND BRN_CODE=POL_BRN_CODE
             AND IPU_POL_BATCH_NO=POL_BATCH_NO
             AND pol_batch_no = v_pol_batch_no
             AND ROWNUM <2;

             lv_temp_txt := REPLACE(lv_temp_txt,'[POLICYNO]',v_pol_no);
             lv_temp_txt := REPLACE(lv_temp_txt,'[COVERFROM]',TO_CHAR(vcoverfrom,'DD/Mon/YYYY'));
             lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTO]',TO_CHAR(vcoverto,'DD/Mon/YYYY'));
             lv_temp_txt := REPLACE(lv_temp_txt,'[EFFDATE]',TO_CHAR(veffdate,'DD/Mon/YYYY'));
             lv_temp_txt := REPLACE(lv_temp_txt,'[RENEWALDATE]',TO_CHAR(vrendate,'DD/Mon/YYYY'));
             lv_temp_txt := REPLACE(lv_temp_txt,'[SA]',v_sa);
             lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
             lv_temp_txt := REPLACE(lv_temp_txt,'[PREMIUM]',v_prem);
             lv_temp_txt := REPLACE(lv_temp_txt,'[AGENT]',v_agent);
             lv_temp_txt := REPLACE(lv_temp_txt,'[INTPARTIES]',v_int_parties);
             lv_temp_txt := REPLACE(lv_temp_txt,'[UWYEAR]',v_uw_year);
             lv_temp_txt := REPLACE(lv_temp_txt,'[BRANCH]',v_branch);
             lv_temp_txt := REPLACE(lv_temp_txt,'[ADDRESS]',v_address);
             lv_temp_txt := REPLACE(lv_temp_txt,'[TOWN]',v_twn);
             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKID]',v_risk_id);
             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKDESC]',v_risk_desc);
             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWEF]',v_risk_wef);
             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWET]',v_risk_wet);
             lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTYPE]',v_cover);
             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKPREM]',v_risk_prem);
             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKVALUE]',v_risk_value);
             
         EXCEPTION
             WHEN NO_DATA_FOUND THEN NULL;
             WHEN OTHERS THEN
                RAISE_APPLICATION_ERROR(-20001, 'ERROR: '||SQLERRM(SQLCODE));
         END;
        ELSE
            IF v_pol_no IS NOT NULL THEN
                 lv_temp_txt := REPLACE(lv_temp_txt,'[POLICYNO]',v_pol_no);
            END IF;
        END IF;

    ELSIF v_app_lvl = 'C' THEN

        BEGIN

              SELECT DISTINCT POL_POLICY_NO,CMB_CLAIM_NO,POL_TOTAL_SUM_INSURED,DECODE(REG_THIRD_PARTY,'S',GIS_UTILITIES.CLNT_NAME(CLNT_NAME,CLNT_SURNAME),CLD_OTHER_NAMES||' '||CLD_SURNAME) CLAIMANT_NAME
              ,POL_TOT_ENDOS_DIFF_AMT,CMB_CLAIM_DATE, CMB_LOSS_DATE_TIME, CMB_IPU_PROPERTY_ID RISK_ID, CMB_LOCATION,CMB_LOSS_DESC,
              nvl(CMB_OTHER_COVER_DETAILS,'Repairable loss') LOSS_TYPE,
             IPU_ITEM_DESC RISK_DESCRIPTION,
             DECODE(REG_THIRD_PARTY,'S','SELF','THIRD PARTY') CLAIMANT_TYPE
             INTO v_pol_no,v_clm_no,v_sa,v_client,v_prem, v_date, v_time, v_reg, v_loc,v_desc,v_loss_type,v_item_desc,v_claimant_type
             FROM GIN_CLAIM_MASTER_BOOKINGS,GIN_POLICIES ,TQC_CLIENTS,GIN_INSURED_PROPERTY_UNDS,GIN_RGSTD_CLAIMANTS,GIN_CLAIMANTS
             WHERE CMB_POL_BATCH_NO = POL_BATCH_NO
             AND REG_CLD_CODE=CLNT_CODE
             AND IPU_POL_BATCH_NO=POL_BATCH_NO
             AND REG_CLD_CODE = CLD_CODE(+)
             AND POL_PRP_CODE = CLNT_CODE
             AND CMB_CLAIM_NO = v_claim_no;

             IF v_gcc_code IS NOT NULL THEN
                    SELECT GCC_COURT_NAME, GCC_COURT_DATE, GCC_CASE_NO
                    INTO v_courtname, v_courtdate, v_caseno
                     FROM GIN_COURT_CASES
                     WHERE GCC_CMB_CLAIM_NO = v_claim_no
                     AND GCC_CODE = v_gcc_code;
             END IF;

             lv_temp_txt := REPLACE(lv_temp_txt,'[POLICYNO]',v_pol_no);
             lv_temp_txt := REPLACE(lv_temp_txt,'[CLAIMNO]',v_clm_no);
             lv_temp_txt := REPLACE(lv_temp_txt,'[SA]',v_sa);
             lv_temp_txt := REPLACE(lv_temp_txt,'[CLAIMANTNAME]',v_client);
             lv_temp_txt := REPLACE(lv_temp_txt,'[PREMIUM]',v_prem);
             lv_temp_txt := REPLACE(lv_temp_txt,'[DATE]',v_date);
              lv_temp_txt := REPLACE(lv_temp_txt,'[LOSSTIME]',v_time);
              lv_temp_txt := REPLACE(lv_temp_txt,'[REGISTRATION]',v_reg);
              lv_temp_txt := REPLACE(lv_temp_txt,'[LOCATION]',v_loc);
              lv_temp_txt := REPLACE(lv_temp_txt,'[CASENO]',v_caseno);
              lv_temp_txt := REPLACE(lv_temp_txt,'[CASEDATE]',v_courtdate);
              lv_temp_txt := REPLACE(lv_temp_txt,'[COURT]',v_courtname);
              lv_temp_txt := REPLACE(lv_temp_txt,'[LOSSDESC]',v_desc );
              lv_temp_txt := REPLACE(lv_temp_txt,'[LOSSTYPE]',v_loss_type);
              lv_temp_txt := REPLACE(lv_temp_txt,'[RISKDESC]',v_item_desc);
              lv_temp_txt := REPLACE(lv_temp_txt,'[CLAIMANTTYPE]',v_claimant_type);

        EXCEPTION
             WHEN NO_DATA_FOUND THEN NULL;
             WHEN OTHERS THEN
                RAISE_APPLICATION_ERROR(-20001, 'ERROR: '||SQLERRM(SQLCODE));
        END;

    ELSIF v_app_lvl = 'L' THEN
        NULL;
    
    ELSIF v_app_lvl = 'Q' THEN
        IF v_pol_batch_no IS NOT NULL THEN
            BEGIN
              
                SELECT QUOT_NO,QUOT_TOT_PROPERTY_VAL,GIS_UTILITIES.CLNT_NAME(CLNT_NAME,CLNT_OTHER_NAMES) CLNT_NAME,QUOT_PREMIUM,
                QUOT_COVER_FROM,QUOT_COVER_TO,QUOT_DATE,AGN_NAME,
                BRN_NAME,CLNT_POSTAL_ADDRS,TWN_NAME,QR_PROPERTY_ID,QR_ITEM_DESC,QR_WEF,QR_WET,QR_COVT_SHT_DESC,QR_PREMIUM,QR_VALUE, QUOT_CUR_SYMBOL             
                INTO v_pol_no,v_sa,v_client,v_prem,vcoverfrom, vcoverto,veffdate,v_agent,
                v_branch,v_address,v_twn,v_risk_id,v_risk_desc,v_risk_wef,v_risk_wet,v_cover,v_risk_prem,v_risk_value, v_currency
                FROM GIN_QUOTATIONS ,TQC_CLIENTS,TQC_AGENCIES,TQC_BRANCHES,TQC_TOWNS, GIN_QUOT_RISKS
                WHERE QUOT_PRP_CODE = CLNT_CODE
                AND QR_QUOT_CODE = QUOT_CODE
                AND QUOT_AGNT_AGENT_CODE = AGN_CODE
                AND TWN_CODE(+) = CLNT_TWN_CODE
                AND BRN_CODE = QUOT_BRN_CODE
                AND QUOT_CODE = v_pol_batch_no
                AND ROWNUM <2;
             
                 lv_temp_txt := REPLACE(lv_temp_txt,'[QUOTNO]',v_pol_no);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[COVERFROM]',TO_CHAR(vcoverfrom,'DD/Mon/YYYY'));
                 lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTO]',TO_CHAR(vcoverto,'DD/Mon/YYYY'));
                 lv_temp_txt := REPLACE(lv_temp_txt,'[EFFDATE]',TO_CHAR(veffdate,'DD/Mon/YYYY'));
                 lv_temp_txt := REPLACE(lv_temp_txt,'[SA]',v_sa);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[PREMIUM]',v_prem);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[CURRENCY]',v_currency);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[AGENT]',v_agent);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[BRANCH]',v_branch);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[ADDRESS]',v_address);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[TOWN]',v_twn);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKID]',v_risk_id);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKDESC]',v_risk_desc);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWEF]',v_risk_wef);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWET]',v_risk_wet);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTYPE]',v_cover);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKPREM]',v_risk_prem);
                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKVALUE]',v_risk_value);
             
            EXCEPTION
                 WHEN NO_DATA_FOUND THEN NULL;
                 WHEN OTHERS THEN
                    RAISE_APPLICATION_ERROR(-20001, 'ERROR: '||SQLERRM(SQLCODE));
            END;
        ELSE
            IF v_pol_no IS NOT NULL THEN
                 lv_temp_txt := REPLACE(lv_temp_txt,'[QUOTNO]',v_pol_no);
            END IF;
        END IF;
    ELSE
         RAISE_APPLICATION_ERROR(-20001, 'Application level '||NVL(v_app_lvl,'NONE')||' not recognised..');
    END IF;
    RETURN(lv_temp_txt);
END PROCESS_GIS_POL_MEMO;

 
 
 
 
 
 
 
 
 
 
 
 
 
 
/