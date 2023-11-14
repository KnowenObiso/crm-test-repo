--
-- TQC_MEMOS_DBPKG130815  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.Tqc_Memos_Dbpkg130815 AS
  /******************************************************************************
     NAME:       tqc_memos_pkg
     PURPOSE:
  
     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        10/29/2007             1. Created this package body.
  ******************************************************************************/

  PROCEDURE RAISE_ERROR(v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL) IS
    db_msg VARCHAR2(250);
  BEGIN
    IF SQLCODE = 0 THEN
      db_msg := '';
    ELSE
      db_msg := ' - ' || SQLERRM(SQLCODE);
    END IF;
    IF v_err_no IS NULL THEN
    
      RAISE_ERROR(v_msg || db_msg);
    ELSE
      RAISE_ERROR(v_msg || db_msg);
    END IF;
  END RAISE_ERROR;

  PROCEDURE Populate_Memo_Details(v_MTYP_CODE NUMBER, v_COMEM_CODE NUMBER) IS
    CURSOR cur_memo IS
      SELECT MEMO_CODE, MEMO_SUBJECT
        FROM TQC_MEMOS
       WHERE MEMO_MTYP_CODE = v_MTYP_CODE;
  
    CURSOR cur_mem_det(v_memo_code NUMBER) IS
      SELECT MEMDET_CODE, MEMDET_CONTENT, MEMDET_MEMO_CODE
        FROM TQC_MEMO_DETAILS
       WHERE MEMDET_MEMO_CODE = v_memo_code;
  
  BEGIN
    FOR cur_memo_rec IN cur_memo LOOP
      --- insert memo header
      BEGIN
        INSERT INTO TQC_MEMO_HEADER
          (MEMH_CODE, MEMH_COMEM_CODE, MEMH_MEMO_CODE, MEMH_MEMO_SUBJECT)
        VALUES
          (TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ||
           TQC_MEMH_CODE_SEQ.NEXTVAL,
           v_COMEM_CODE,
           cur_memo_rec.MEMO_CODE,
           cur_memo_rec.MEMO_SUBJECT);
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Unable to insert memo header');
      END;
    
      FOR cur_mem_det_rec IN cur_mem_det(cur_memo_rec.MEMO_CODE) LOOP
        BEGIN
          INSERT INTO TQC_MEMO_HEADER_DTLS
            (MEMDTLS_MEMH_CODE,
             MEMDTLS_MEMDET_CODE,
             MEMDTLS_DETAIL,
             MEMDTLS_CODE)
          VALUES
            (TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ||
             TQC_MEMH_CODE_SEQ.CURRVAL,
             cur_mem_det_rec.MEMDET_CODE,
             cur_mem_det_rec.MEMDET_CONTENT,
             TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY') ||
                       TQC_MEMDTLS_CODE_SEQ.NEXTVAL));
        EXCEPTION
          WHEN OTHERS THEN
            RAISE_ERROR('Unable to insert memo details');
        END;
      END LOOP; --cur_mem_det
    END LOOP; --cur_memo
  
  END;

  PROCEDURE Del_Memo_Details(v_COMEM_CODE NUMBER) IS
    CURSOR cur_memo_h IS
      SELECT MEMH_CODE, MEMH_MEMO_CODE, MEMH_COMEM_CODE
        FROM TQC_MEMO_HEADER
       WHERE MEMH_COMEM_CODE = v_COMEM_CODE;
  
  BEGIN
    FOR cur_memo_h_rec IN cur_memo_h LOOP
      BEGIN
        DELETE TQC_MEMO_HEADER_DTLS
         WHERE MEMDTLS_MEMH_CODE = cur_memo_h_rec.MEMH_CODE;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Unable delete memo details');
      END;
    END LOOP;
    DELETE TQC_MEMO_HEADER WHERE MEMH_COMEM_CODE = v_COMEM_CODE;
    DELETE TQC_COMPANY_MEMOS WHERE COMEM_CODE = v_COMEM_CODE;
  END;

  -----
  FUNCTION PROCESS_GIS_POL_MEMO(v_pol_batch_no IN NUMBER,
                                v_claim_no     IN VARCHAR2,
                                v_gcc_code     IN NUMBER,
                                v_raw_txt      IN VARCHAR2,
                                v_app_lvl      IN VARCHAR2) RETURN VARCHAR2 IS
  
    lv_temp_txt     VARCHAR2(10000);
    v_pol_no        GIN_POLICIES.POL_POLICY_NO%TYPE;
    v_clm_no        GIN_CLAIM_MASTER_BOOKINGS.CMB_CLAIM_NO%TYPE;
    v_sa            NUMBER;
    v_premium       NUMBER;
    v_client        TQC_CLIENTS.CLNT_NAME%TYPE;
    v_prem          NUMBER;
    v_date          DATE;
    v_time          DATE;
    v_reg           VARCHAR2(100);
    v_loc           VARCHAR2(100);
    v_courtname     VARCHAR2(100);
    v_courtdate     DATE;
    v_caseno        VARCHAR2(100);
    vcoverfrom      DATE;
    vcoverto        DATE;
    veffdate        DATE;
    vrendate        DATE;
    v_desc          VARCHAR2(100);
    v_loss_type     VARCHAR2(100);
    v_item_desc     VARCHAR2(100);
    v_claimant_type VARCHAR2(15);
    v_agent         TQC_AGENCIES.AGN_NAME%TYPE;
    v_int_parties   VARCHAR2(100);
    v_uw_year       INT;
    v_branch        TQC_BRANCHES.BRN_NAME%TYPE;
    v_address       VARCHAR2(100);
    v_twn           VARCHAR2(100);
    v_risk_id       VARCHAR2(100);
    v_surname       TQC_CLIENTS.CLNT_NAME%TYPE;
    v_risk_desc     GIN_INSURED_PROPERTY_UNDS.IPU_ITEM_DESC%TYPE;
    v_risk_wef      DATE;
    v_risk_wet      DATE;
    v_cover         VARCHAR2(50);
    v_risk_prem     DECIMAL(20, 2);
    v_risk_value    DECIMAL(20, 2);
    v_currency      VARCHAR2(5);
  
    v_agn_code     NUMBER;
    v_agn_name     TQC_AGENCIES.AGN_NAME%TYPE;
    v_agn_add      TQC_AGENCIES.AGN_PHYSICAL_ADDRESS%TYPE;
    v_client_code  NUMBER;
    v_client_add   VARCHAR2(100);
    v_insured_name VARCHAR2(300);
    v_insured_add  VARCHAR2(100);
    v_org_name     VARCHAR2(100);
    v_org_add      VARCHAR2(100);
    v_ipu_desc     VARCHAR2(100);
    v_amt_words    VARCHAR2(50);
    v_cur_code     NUMBER;
    v_period       NUMBER;
  
    v_obligor     GIN_BONDS.BON_OBLIGOR%TYPE;
    v_ob_address  GIN_BONDS.OBLIGOR_ADDRS%TYPE;
    v_ob_town     GIN_BONDS.BON_OBLIGOR_TOWN%TYPE;
    v_ob_country  GIN_BONDS.BON_OBLIGOR_COUNTRY%TYPE;
    v_ob_emp      GIN_BONDS.BON_EMPLOYER%TYPE;
    v_certs       VARCHAR2(200) := '';
    v_srv_fee_pct NUMBER(5, 1);
    CURSOR cur_certs(v_pol_batch IN NUMBER) IS
      select SRV_PROPERTY IPU_PROPERTY_ID
        from GIN_SURVEY_INFO
       where SRV_POL_BATCH_NO = v_pol_batch;
  BEGIN
    lv_temp_txt := v_raw_txt;
    --RAISE_ERROR('v_app_lvl '||v_app_lvl);
    IF v_app_lvl = 'U' THEN
      IF v_pol_batch_no IS NOT NULL THEN
        BEGIN
        
          SELECT DISTINCT POL_POLICY_NO,
                 POL_SI_DIFF,
                 GIS_UTILITIES.CLNT_NAME(CLNT_NAME, CLNT_OTHER_NAMES) CLNT_NAME,
                 POL_TOT_ENDOS_DIFF_AMT,
                 POL_POLICY_COVER_FROM,
                 POL_POLICY_COVER_TO,
                 POL_WEF_DT,
                 POL_RENEWAL_DT,
                 AGN_NAME,
                 POL_OTH_INT_PARTIES,
                 POL_UW_YEAR,
                 BRN_NAME,
                 CLNT_POSTAL_ADDRS,
                 TWN_NAME,
                 IPU_PROPERTY_ID,
                 IPU_ITEM_DESC,
                 IPU_WEF,
                 IPU_WET,
                 IPU_COVT_SHT_DESC,
                 IPU_ENDOS_DIFF_AMT,
                 IPU_VALUE
            INTO v_pol_no,
                 v_sa,
                 v_client,
                 v_prem,
                 vcoverfrom,
                 vcoverto,
                 veffdate,
                 vrendate,
                 v_agent,
                 v_int_parties,
                 v_uw_year,
                 v_branch,
                 v_address,
                 v_twn,
                 v_risk_id,
                 v_risk_desc,
                 v_risk_wef,
                 v_risk_wet,
                 v_cover,
                 v_risk_prem,
                 v_risk_value
            FROM GIN_POLICIES,
                 TQC_CLIENTS,
                 TQC_AGENCIES,
                 TQC_BRANCHES,
                 TQC_TOWNS,
                 TQ_GIS.GIN_INSURED_PROPERTY_UNDS
           WHERE POL_PRP_CODE = CLNT_CODE
             AND POL_AGNT_AGENT_CODE = AGN_CODE
             AND TWN_CODE(+) = CLNT_TWN_CODE
             AND BRN_CODE = POL_BRN_CODE
             AND IPU_POL_BATCH_NO = POL_BATCH_NO
             AND pol_batch_no = v_pol_batch_no
             AND ROWNUM < 2;
        --RAISE_ERROR('TEST');
          BEGIN
            FOR cur_certs_cur IN cur_certs(v_pol_batch_no) LOOP
              v_certs := v_certs || ' , ' || cur_certs_cur.IPU_PROPERTY_ID;
            END LOOP;
          EXCEPTION
            WHEN OTHERS THEN
              RAISE_APPLICATION_ERROR(-20001, 'Error checking certs....');
          END;
          BEGIN
            SELECT SRV_FEE_PCT
              INTO v_srv_fee_pct
              FROM GIN_SURVEY_INFO
             WHERE SRV_POL_BATCH_NO = v_pol_batch_no;
          EXCEPTION
            WHEN OTHERS THEN
              RAISE_APPLICATION_ERROR(-20001, 'Error checking certs....');
          END;
        
          lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
          lv_temp_txt := REPLACE(lv_temp_txt, '[POLNO]', v_pol_no);
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[COVERFROM]',
                                 TO_CHAR(vcoverfrom, 'DD/MM/RRRR'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[COVERTO]',
                                 TO_CHAR(vcoverto, 'DD/MM/RRRR'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[EFFDATE]',
                                 TO_CHAR(veffdate, 'DD/MM/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[RENEWALDATE]',
                                 TO_CHAR(vrendate, 'DD/MM/RRRR'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[SA]',
                                 TO_CHAR(NVL(v_sa, 0), 'FM999,999,999.00'));
          lv_temp_txt := REPLACE(lv_temp_txt, '[CLIENT]', v_client);
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[PREMIUM]',
                                 TO_CHAR(NVL(v_prem, 0), 'FM999,999,999.00'));
          lv_temp_txt := REPLACE(lv_temp_txt, '[AGENT]', v_agent);
          lv_temp_txt := REPLACE(lv_temp_txt, '[INTPARTIES]', v_int_parties);
          lv_temp_txt := REPLACE(lv_temp_txt, '[UWYEAR]', v_uw_year);
          lv_temp_txt := REPLACE(lv_temp_txt, '[BRANCH]', v_branch);
          lv_temp_txt := REPLACE(lv_temp_txt, '[ADDRESS]', v_address);
          lv_temp_txt := REPLACE(lv_temp_txt, '[TOWN]', v_twn);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKID]', v_risk_id);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKDESC]', v_risk_desc);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKWEF]', v_risk_wef);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKWET]', v_risk_wet);
          lv_temp_txt := REPLACE(lv_temp_txt, '[COVERTYPE]', v_cover);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKPREM]', v_risk_prem);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKVALUE]', v_risk_value);
          lv_temp_txt := REPLACE(lv_temp_txt, '[CERTS]', v_certs);
          lv_temp_txt := REPLACE(lv_temp_txt, '[FEERATE]', v_srv_fee_pct);
          RAISE_ERROR('HERE');
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[WEF]',
                                 TO_CHAR(veffdate, 'DD/MM/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[WET]',
                                 TO_CHAR(vcoverto, 'DD/MM/YYYY'));
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            NULL;
          WHEN OTHERS THEN
            RAISE_ERROR('ERROR: ' || SQLERRM(SQLCODE));
        END;
      ELSE
        IF v_pol_no IS NOT NULL THEN
          lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
        END IF;
      END IF;
    ELSIF v_app_lvl = 'S' THEN
    
      SELECT POL_POLICY_NO,
             POL_TOTAL_SUM_INSURED,
             GIS_UTILITIES.CLNT_NAME(CLNT_NAME, CLNT_OTHER_NAMES) CLNT_NAME,
             CLNT_NAME,
             POL_TOTAL_FAP,
             POL_POLICY_COVER_FROM,
             POL_POLICY_COVER_TO,
             POL_WEF_DT,
             POL_RENEWAL_DT,
             POL_AGNT_AGENT_CODE,
             POL_CUR_CODE,
             POL_OTH_INT_PARTIES
        INTO v_pol_no,
             v_sa,
             v_client,
             v_surname,
             v_prem,
             vcoverfrom,
             vcoverto,
             veffdate,
             vrendate,
             v_agn_code,
             v_cur_code,
             v_int_parties
        FROM GIN_POLICIES, TQC_CLIENTS
       WHERE POL_PRP_CODE = CLNT_CODE
         AND pol_batch_no = v_pol_batch_no;
      --MESSAGE('[POLICYNO]'||lv_temp_txt);PAUSE;
      lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
      lv_temp_txt := REPLACE(lv_temp_txt, '[CLIENT]', v_client);
      lv_temp_txt := REPLACE(lv_temp_txt, '[SURNAME]', v_surname);
      lv_temp_txt := REPLACE(lv_temp_txt,
                             '[COVERFROM]',
                             to_char(vcoverfrom, 'ddth') || ' day of ' ||
                             trim(to_char(vcoverfrom, 'Month')) || ' ' ||
                             to_char(vcoverfrom, 'RRRR'));
      lv_temp_txt := REPLACE(lv_temp_txt,
                             '[COVERTO]',
                             to_char(vcoverto, 'ddth') || ' day of ' ||
                             trim(to_char(vcoverto, 'Month')) || ' ' ||
                             to_char(vcoverto, 'RRRR'));
      lv_temp_txt := REPLACE(lv_temp_txt,
                             '[EFFDATE]',
                             to_char(veffdate, 'ddth') || ' day of ' ||
                             trim(to_char(vcoverfrom, 'Month')) || ' ' ||
                             to_char(vcoverfrom, 'RRRR'));
      lv_temp_txt := REPLACE(lv_temp_txt,
                             '[RENEWALDATE]',
                             TO_CHAR(vrendate, 'DD/MM/YYYY'));
      lv_temp_txt := REPLACE(lv_temp_txt, '[INTPARTIES]', v_int_parties);
      --MESSAGE(v_pol_no||lv_temp_txt);PAUSE;
      lv_temp_txt := REPLACE(lv_temp_txt,
                             '[SA]',
                             TO_CHAR(NVL(v_sa, 0), 'FM999,999,999.00'));
      -- lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
      lv_temp_txt := REPLACE(lv_temp_txt, '[PREMIUM]', NVL(v_prem, 0));
      lv_temp_txt := REPLACE(lv_temp_txt,
                             '[AMTINWORDS]',
                             FMS_CHEQUES_PKG.num_to_words(NVL(v_sa, 0),
                                                          v_cur_code));
      SELECT MONTHS_BETWEEN(vcoverto, vcoverfrom) INTO v_period FROM DUAL;
      lv_temp_txt := REPLACE(lv_temp_txt, '[PERIOD]', ROUND(v_period, 0));
      SELECT IPU_ITEM_DESC
        INTO v_ipu_desc
        FROM TQ_GIS.GIN_INSURED_PROPERTY_UNDS
       WHERE IPU_POL_BATCH_NO = v_pol_batch_no
         AND ROWNUM = 1;
      lv_temp_txt := REPLACE(lv_temp_txt, '[ITEMDESC]', v_ipu_desc);
    
      SELECT ORG_NAME, ORG_PHY_ADDRS
        INTO v_org_name, v_org_add
        FROM TQC_ORGANIZATIONS
       WHERE ORG_CODE = 2;
      lv_temp_txt := REPLACE(lv_temp_txt, '[ORGNAME]', v_org_name);
      lv_temp_txt := REPLACE(lv_temp_txt, '[ORGADDR]', v_org_add);
    
      SELECT AGN_NAME, AGN_PHYSICAL_ADDRESS
        INTO v_agn_name, v_agn_add
        FROM TQC_AGENCIES
       WHERE AGN_CODE = v_agn_code;
    
      lv_temp_txt := REPLACE(lv_temp_txt, '[AGNNAME]', v_agn_name);
      lv_temp_txt := REPLACE(lv_temp_txt, '[AGNADDR]', v_agn_add);
      BEGIN
        SELECT GIS_UTILITIES.CLNT_NAME(CLNT_NAME, CLNT_OTHER_NAMES) CLNT_NAME,
               CLNT_PHYSICAL_ADDRS
          INTO v_insured_name, v_insured_add
          FROM TQ_GIS.GIN_POLICY_INSUREDS, TQC_CLIENTS
         WHERE POLIN_PRP_CODE = CLNT_CODE
           AND POLIN_POL_BATCH_NO = v_pol_batch_no
           AND ROWNUM = 1;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
      END;
    
      lv_temp_txt := REPLACE(lv_temp_txt, '[INSNAME]', v_insured_name);
      lv_temp_txt := REPLACE(lv_temp_txt, '[INSADDR]', v_insured_add);
    
      BEGIN
        SELECT BON_OBLIGOR,
               OBLIGOR_ADDRS,
               BON_OBLIGOR_TOWN,
               BON_OBLIGOR_COUNTRY,
               BON_EMPLOYER,
               BON_CONTRACT_DATE
          INTO v_obligor,
               v_ob_address,
               v_ob_town,
               v_ob_country,
               v_ob_emp,
               v_date
          FROM GIN_BONDS, GIN_INSURED_PROPERTY_UNDS
         WHERE BON_IPU_CODE = IPU_CODE
           AND IPU_POL_BATCH_NO = v_pol_batch_no;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
      END;
    
      lv_temp_txt := REPLACE(lv_temp_txt, '[BANK]', v_obligor);
      lv_temp_txt := REPLACE(lv_temp_txt, '[BANKADD]', v_ob_address);
      lv_temp_txt := REPLACE(lv_temp_txt, '[BANKTWN]', v_ob_town);
      lv_temp_txt := REPLACE(lv_temp_txt, '[BANKSTATE]', v_ob_country);
      lv_temp_txt := REPLACE(lv_temp_txt, '[EMPLOYER]', v_ob_emp);
      lv_temp_txt := REPLACE(lv_temp_txt,
                             '[CONTRACTDATE]',
                             to_char(v_date, 'ddth') || ' day of ' ||
                             trim(to_char(v_date, 'Month')) || ' ' ||
                             to_char(v_date, 'RRRR'));
    ELSIF v_app_lvl = 'C' THEN
    
      BEGIN
      
        SELECT DISTINCT POL_POLICY_NO,
                        CMB_CLAIM_NO,
                        POL_TOTAL_SUM_INSURED,
                        DECODE(REG_THIRD_PARTY,
                               'S',
                               GIS_UTILITIES.CLNT_NAME(CLNT_NAME,
                                                       CLNT_SURNAME),
                               CLD_OTHER_NAMES || ' ' || CLD_SURNAME) CLAIMANT_NAME,
                        POL_TOT_ENDOS_DIFF_AMT,
                        CMB_CLAIM_DATE,
                        CMB_LOSS_DATE_TIME,
                        CMB_IPU_PROPERTY_ID RISK_ID,
                        CMB_LOCATION,
                        CMB_LOSS_DESC,
                        nvl(CMB_OTHER_COVER_DETAILS, 'Repairable loss') LOSS_TYPE,
                        IPU_ITEM_DESC RISK_DESCRIPTION,
                        DECODE(REG_THIRD_PARTY, 'S', 'SELF', 'THIRD PARTY') CLAIMANT_TYPE
          INTO v_pol_no,
               v_clm_no,
               v_sa,
               v_client,
               v_prem,
               v_date,
               v_time,
               v_reg,
               v_loc,
               v_desc,
               v_loss_type,
               v_item_desc,
               v_claimant_type
          FROM GIN_CLAIM_MASTER_BOOKINGS,
               GIN_POLICIES,
               TQC_CLIENTS,
               TQ_GIS.GIN_INSURED_PROPERTY_UNDS,
               GIN_RGSTD_CLAIMANTS,
               GIN_CLAIMANTS
         WHERE CMB_POL_BATCH_NO = POL_BATCH_NO
           AND REG_CLD_CODE = CLNT_CODE
           AND IPU_POL_BATCH_NO = POL_BATCH_NO
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
      
        lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CLAIMNO]', v_clm_no);
        lv_temp_txt := REPLACE(lv_temp_txt, '[SA]', v_sa);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CLAIMANTNAME]', v_client);
        lv_temp_txt := REPLACE(lv_temp_txt, '[PREMIUM]', v_prem);
        lv_temp_txt := REPLACE(lv_temp_txt, '[DATE]', v_date);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOSSTIME]', v_time);
        lv_temp_txt := REPLACE(lv_temp_txt, '[REGISTRATION]', v_reg);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOCATION]', v_loc);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CASENO]', v_caseno);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CASEDATE]', v_courtdate);
        lv_temp_txt := REPLACE(lv_temp_txt, '[COURT]', v_courtname);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOSSDESC]', v_desc);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOSSTYPE]', v_loss_type);
        lv_temp_txt := REPLACE(lv_temp_txt, '[RISKDESC]', v_item_desc);
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[CLAIMANTTYPE]',
                               v_claimant_type);
      
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
        WHEN OTHERS THEN
          RAISE_ERROR('ERROR: ' || SQLERRM(SQLCODE));
      END;
    
    ELSIF v_app_lvl = 'L' THEN
      NULL;
    
    ELSIF v_app_lvl = 'Q' THEN
      IF v_pol_batch_no IS NOT NULL THEN
        BEGIN
        
          SELECT QUOT_NO,
                 QUOT_TOT_PROPERTY_VAL,
                 GIS_UTILITIES.CLNT_NAME(CLNT_NAME, CLNT_OTHER_NAMES) CLNT_NAME,
                 QUOT_PREMIUM,
                 QUOT_COVER_FROM,
                 QUOT_COVER_TO,
                 QUOT_DATE,
                 AGN_NAME,
                 BRN_NAME,
                 CLNT_POSTAL_ADDRS,
                 TWN_NAME,
                 QR_PROPERTY_ID,
                 QR_ITEM_DESC,
                 QR_WEF,
                 QR_WET,
                 QR_COVT_SHT_DESC,
                 QR_PREMIUM,
                 QR_VALUE,
                 QUOT_CUR_SYMBOL
            INTO v_pol_no,
                 v_sa,
                 v_client,
                 v_prem,
                 vcoverfrom,
                 vcoverto,
                 veffdate,
                 v_agent,
                 v_branch,
                 v_address,
                 v_twn,
                 v_risk_id,
                 v_risk_desc,
                 v_risk_wef,
                 v_risk_wet,
                 v_cover,
                 v_risk_prem,
                 v_risk_value,
                 v_currency
            FROM GIN_QUOTATIONS,
                 TQC_CLIENTS,
                 TQC_AGENCIES,
                 TQC_BRANCHES,
                 TQC_TOWNS,
                 TQ_GIS.GIN_QUOT_RISKS
           WHERE QUOT_PRP_CODE = CLNT_CODE
             AND QR_QUOT_CODE = QUOT_CODE
             AND QUOT_AGNT_AGENT_CODE = AGN_CODE
             AND TWN_CODE(+) = CLNT_TWN_CODE
             AND BRN_CODE = QUOT_BRN_CODE
             AND QUOT_CODE = v_pol_batch_no
             AND ROWNUM < 2;
        
          lv_temp_txt := REPLACE(lv_temp_txt, '[QUOTNO]', v_pol_no);
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[COVERFROM]',
                                 TO_CHAR(vcoverfrom, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[COVERTO]',
                                 TO_CHAR(vcoverto, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[EFFDATE]',
                                 TO_CHAR(veffdate, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt, '[SA]', v_sa);
          lv_temp_txt := REPLACE(lv_temp_txt, '[CLIENT]', v_client);
          lv_temp_txt := REPLACE(lv_temp_txt, '[PREMIUM]', v_prem);
          lv_temp_txt := REPLACE(lv_temp_txt, '[CURRENCY]', v_currency);
          lv_temp_txt := REPLACE(lv_temp_txt, '[AGENT]', v_agent);
          lv_temp_txt := REPLACE(lv_temp_txt, '[BRANCH]', v_branch);
          lv_temp_txt := REPLACE(lv_temp_txt, '[ADDRESS]', v_address);
          lv_temp_txt := REPLACE(lv_temp_txt, '[TOWN]', v_twn);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKID]', v_risk_id);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKDESC]', v_risk_desc);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKWEF]', v_risk_wef);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKWET]', v_risk_wet);
          lv_temp_txt := REPLACE(lv_temp_txt, '[COVERTYPE]', v_cover);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKPREM]', v_risk_prem);
          lv_temp_txt := REPLACE(lv_temp_txt, '[RISKVALUE]', v_risk_value);
        
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            NULL;
          WHEN OTHERS THEN
            RAISE_ERROR('ERROR: ' || SQLERRM(SQLCODE));
        END;
      ELSE
        IF v_pol_no IS NOT NULL THEN
          lv_temp_txt := REPLACE(lv_temp_txt, '[QUOTNO]', v_pol_no);
        END IF;
      END IF;
    ELSE
      RAISE_ERROR('Application level ' || NVL(v_app_lvl, 'NONE') ||
                  ' not recognised..');
    END IF;
    RETURN(lv_temp_txt);
  END PROCESS_GIS_POL_MEMO;
END Tqc_Memos_Dbpkg130815; 
/