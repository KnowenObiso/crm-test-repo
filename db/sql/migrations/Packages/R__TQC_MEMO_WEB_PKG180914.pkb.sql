--
-- TQC_MEMO_WEB_PKG180914  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_MEMO_WEB_PKG180914 AS
  PROCEDURE RAISE_ERROR(v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL) IS
  BEGIN
    IF v_err_no IS NULL THEN
      RAISE_APPLICATION_ERROR(-20037, v_msg || ' - ' || SQLERRM(SQLCODE));
    ELSE
      RAISE_APPLICATION_ERROR(v_err_no, v_msg || ' - ' || SQLERRM(SQLCODE));
    END IF;
  END RAISE_ERROR;
  PROCEDURE EditableCopy(v_mem_code  NUMBER,
                         v_memh_code NUMBER,
                         v_ref OUT  varchar2,
                         v_user IN   VARCHAR2,
                         v_new_mem_code OUT NUMBER) IS
    ncomemcode number;
    v_name     VARCHAR2(40);
    v_rank     VARCHAR2(80);
    nmemhcode  number;
    v_usr_ref  varchar2(50);
    ncount     NUMBER;
    v_count    NUMBER;
    --v_user     VARCHAR2 (35)  := pkg_global_vars.get_pvarchar2 ('PKG_GLOBAL_VARS.pvg_username');
  BEGIN
    select TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ||
           TQC_COMEM_CODE_SEQ.NEXTVAL
      INTO ncomemcode
      FROM DUAL;
      
      v_new_mem_code :=ncomemcode;
      
      
    select TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) || TQC_MEMH_CODE_SEQ.NEXTVAL
      INTO nmemhcode
      FROM DUAL;
    BEGIN
      SELECT USR_NAME,
             decode(USR_SIGN, 'Y', USR_PERSONEL_RANK, 'FOR THE COMPANY'),
             usr_ref
        INTO v_name, v_rank, v_usr_ref
        FROM TQC_USERS
       WHERE USR_USERNAME = v_user;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        NULL;
      WHEN OTHERS THEn
        NULL;
    END;
    
    v_ref := nvl(v_usr_ref, v_user) || '/' || ncomemcode;
    
    BEGIN
      INSERT INTO TQC_COMPANY_MEMOS
        (COMEM_CODE,
         COMEM_DESC,
         COMEM_DATE,
         COMEM_CMB_CLAIM_NO,
         COMEM_SUBJECT,
         COMEM_REG_CLMT_CODE,
         COMEM_ADDRESS,
         COMEM_COMMENTS,
         COMEM_POL_POLICY_NO,
         COMEM_APPL_LVL,
         COMEM_DONE_BY,
         COMEM_AUTHORIZED,
         COMEM_YOUR_REF,
         COMEM_ADDRESSEE,
         COMEM_SIGNATORY,
         COMEM_CC_LIST,
         COMEM_MTYP_CODE,
         COMEM_POL_REN_ENDOS_NO,
         COMEM_BRN_CODE,
         COMEM_PREPARED_BY,
         COMEM_CORR_CODE,
         COMEM_REF,
         COMEM_POL_BATCH_NO,
         COMEM_AUTHORIZED_DATE,
         COMEM_FILE_NO,
         COMEM_AUTHORIZED_BY,
         COMEM_SIGN_RANK,
         COMEM_INSURED,
         COMEM_RPT_TYPE,
         COMEM_ADDRESSEE_TYPE,
         COMEM_PRP_CODE,
         COMEM_EDITS,
         COMEM_REMINDERS,
         COMEM_CODETOWN,
         comem_sys_code)
        SELECT ncomemcode,
               COMEM_DESC,
               TO_DATE(TO_CHAR(sysdate, 'DD/MM/YYYY'), 'DD/MM/YYYY'),
               COMEM_CMB_CLAIM_NO,
               COMEM_SUBJECT,
               COMEM_REG_CLMT_CODE,
               COMEM_ADDRESS,
               COMEM_COMMENTS,
               COMEM_POL_POLICY_NO,
               COMEM_APPL_LVL,
               COMEM_DONE_BY,
               'N',
               COMEM_YOUR_REF,
               COMEM_ADDRESSEE,
               v_name,
               COMEM_CC_LIST,
               COMEM_MTYP_CODE,
               COMEM_POL_REN_ENDOS_NO,
               COMEM_BRN_CODE,
               v_user,
               COMEM_CORR_CODE,
               COMEM_REF,
               COMEM_POL_BATCH_NO,
               NULL,
               v_ref,
               COMEM_AUTHORIZED_BY,
               v_rank,
               COMEM_INSURED,
               COMEM_RPT_TYPE,
               COMEM_ADDRESSEE_TYPE,
               COMEM_PRP_CODE,
               0,
               0,
               COMEM_CODETOWN,
               37
          FROM TQC_COMPANY_MEMOS
         WHERE COMEM_CODE = v_mem_code;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR UPDATING COMPANY MEMOS' || ncomemcode);
    END;
    
       SELECT  COUNT(*)  INTO  ncount
       FROM TQC_MEMO_HEADER
       WHERE MEMH_COMEM_CODE=ncomemcode;

       IF  ncount >1 THEN
       RAISE_ERROR('Unique constraint for comem code violated ');
       END IF;
    
    
    BEGIN
      INSERT INTO TQC_MEMO_HEADER
        (MEMH_CODE, MEMH_COMEM_CODE, MEMH_MEMO_CODE, MEMH_MEMO_SUBJECT)
        SELECT nmemhcode, ncomemcode, MEMH_MEMO_CODE, MEMH_MEMO_SUBJECT
          FROM TQC_MEMO_HEADER
         WHERE MEMH_COMEM_CODE = v_mem_code;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR UPDATING COMPANY MEMOS HEADER');
    END;
    BEGIN
      INSERT INTO TQC_COMPANY_MEMO_CC
        (CC_CODE, CC_COMEM_CODE, CC_NAME, CC_ADDRESS, CC_REMARKS, CC_TYPE)
        SELECT TQC_CC_CODE_SEQ.NEXTVAL,
               ncomemcode,
               CC_NAME,
               CC_ADDRESS,
               CC_REMARKS,
               CC_TYPE
          FROM TQC_COMPANY_MEMO_CC
         WHERE CC_COMEM_CODE = v_mem_code;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR UPDATING COMPANY MEMOS CC');
    END;
    
       SELECT  COUNT(*)  INTO  v_count
       FROM TQC_MEMO_HEADER_DTLS
       WHERE MEMDTLS_MEMH_CODE=nmemhcode;

       IF  ncount >0 THEN
       RAISE_ERROR('Unique constraint for comem code violated ');
       END IF;
    
    BEGIN
      INSERT INTO TQC_MEMO_HEADER_DTLS
        (MEMDTLS_MEMH_CODE,
         MEMDTLS_MEMDET_CODE,
         MEMDTLS_DETAIL,
         MEMDTLS_CODE)
        SELECT nmemhcode,
               MEMDTLS_MEMDET_CODE,
               MEMDTLS_DETAIL,
               TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY') ||
                         TQC_MEMDTLS_CODE_SEQ.NEXTVAL)
          FROM TQC_MEMO_HEADER_DTLS
         WHERE MEMDTLS_MEMH_CODE = v_memh_code;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR UPDATING COMPANY MEMOS DTLS');
    END;
  END;
  PROCEDURE memoSubject(v_comm_mtyp_code   NUMBER,
                        v_user             VARCHAR2,
                        v_comem_code       NUMBER,
                        v_cmb_pol_batch_no NUMBER,
                        v_cmb_claim_no     VARCHAR2,
                        v_gcc_code         NUMBER,
                        v_appl_lvl         VARCHAR2) IS
    v_usr          VARCHAR2(60);
    v_usr_ref      VARCHAR2(60);
    al_id          NUMBER;
    v_memo         VARCHAR2(2500);
    v_raw_txt      VARCHAR2(2500);
    v_dtl_memo     VARCHAR2(2500);
    v_dtl_raw_txt  VARCHAR2(2500);
    v_mem_code     NUMBER;
    v_memh_code    NUMBER;
    v_memo_code    NUMBER;
    v_memdet_code  NUMBER;
    v_memdtls_code NUMBER;
  BEGIN
    BEGIN
      DELETE TQC_MEMO_HEADER_DTLS
       WHERE MEMDTLS_MEMH_CODE IN
             (SELECT MEMH_CODE
                FROM TQC_MEMO_HEADER
               WHERE MEMH_COMEM_CODE = v_comem_code);
      DELETE TQC_MEMO_HEADER WHERE MEMH_COMEM_CODE = v_comem_code;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Error  deleting memo headers details...');
    END;
    BEGIN
      select usr_name, nvl(usr_ref, usr_USERNAME)
        into v_usr, v_usr_ref
        from TQC_users
       where usr_USERNAME = v_user;
    EXCEPTION
      WHEN OTHERS THEN
        v_usr := 'TEST';
    END;
    IF v_usr IS NULL THEN
      RAISE_ERROR('Username not defined..');
    END IF;
    --RAISE_ERROR(v_comm_mtyp_code);
    IF v_comm_mtyp_code IS NOT NULL THEN
      BEGIN
        select TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ||
               TQC_MEMH_CODE_SEQ.NEXTVAL,
               MEMO_CODE,
               MEMO_SUBJECT
          INTO v_memh_code, v_memo_code, v_raw_txt
          from TQC_MEMOS
         WHERE TQC_MEMOS.MEMO_MTYP_CODE = v_comm_mtyp_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Accessing Memo Details');
      END;
      BEGIN
        null;
        v_memo := TQC_MEMOS_DBPKG.PROCESS_GIS_POL_MEMO(v_cmb_pol_batch_no,
                                                       v_cmb_claim_no,
                                                       v_gcc_code,
                                                       v_raw_txt,
                                                       v_appl_lvl);
      EXCEPTION
        WHEN OTHERS THEN
          v_memo := v_raw_txt;
          RAISE_ERROR('Memo processing failed. ');
      END;
      BEGIN
        INSERT INTO TQC_MEMO_HEADER
          (MEMH_CODE, MEMH_COMEM_CODE, MEMH_MEMO_CODE, MEMH_MEMO_SUBJECT)
        VALUES
          (v_memh_code, v_comem_code, v_memo_code, v_memo);
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Creating Memo Header Record...');
      END;
      BEGIN
        select MEMDET_CODE,
               MEMDET_CONTENT,
               TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY') ||
                         TQC_MEMDTLS_CODE_SEQ.NEXTVAL)
          INTO v_memdet_code, v_dtl_raw_txt, v_memdtls_code
          from TQC_MEMO_DETAILS
         where MEMDET_MEMO_CODE = v_memo_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error Populating memo details...');
      END;
      BEGIN
      
        v_dtl_memo := TQC_MEMOS_DBPKG.PROCESS_GIS_POL_MEMO(v_cmb_pol_batch_no,
                                                           v_cmb_claim_no,
                                                           v_gcc_code,
                                                           v_dtl_raw_txt,
                                                           v_appl_lvl);
      EXCEPTION
        WHEN OTHERS THEN
          v_dtl_memo := v_dtl_raw_txt;
          RAISE_ERROR('Memo processing failed. ');
      END;
      BEGIN
        INSERT INTO TQC_MEMO_HEADER_DTLS
          (MEMDTLS_MEMH_CODE,
           MEMDTLS_MEMDET_CODE,
           MEMDTLS_DETAIL,
           MEMDTLS_CODE)
        values
          (v_memh_code, v_memdet_code, v_dtl_memo, v_memdtls_code);
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error updating memo subject and details..');
      END;
    END IF;
  END;

  PROCEDURE processMemorandumHeader(v_memo_subject     VARCHAR2,
                                    v_memh_code        NUMBER DEFAULT NULL,
                                    v_comem_code       NUMBER,
                                    v_cmb_pol_batch_no NUMBER,
                                    v_cmb_claim_no     VARCHAR2,
                                    v_gcc_code         NUMBER,
                                    v_appl_lvl         VARCHAR2,
                                    v_mtyp_code        NUMBER) IS
    v_memo       VARCHAR2(2500);
    v_memoh_code NUMBER;
  BEGIN
    v_memo       := v_memo_subject;
    v_memoh_code := v_memh_code;
    if v_memh_code is null or v_mEmo is null then
      select TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ||
             TQC_MEMH_CODE_SEQ.NEXTVAL
        INTO v_memoh_code
        FROM DUAL;
      BEGIN
        INSERT INTO TQC_MEMO_HEADER
          (MEMH_CODE, MEMH_COMEM_CODE, MEMH_MEMO_CODE, MEMH_MEMO_SUBJECT)
        VALUES
          (v_memoh_code, v_comem_code, v_mtyp_code, v_memo);
        COMMIT;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Memo Processing Failed...');
      END;
    
    else
      BEGIN
        v_memo := TQC_MEMOS_DBPKG.PROCESS_GIS_POL_MEMO(v_cmb_pol_batch_no,
                                                       v_cmb_claim_no,
                                                       v_gcc_code,
                                                       v_memo_subject,
                                                       v_appl_lvl);
        null;
      EXCEPTION
        WHEN OTHERS THEN
          v_memo := v_memo_subject;
          RAISE_ERROR('Memo Processing Failed...');
      END;
    
    end if;
  END;

  PROCEDURE processMemorandumBody(v_mem_dtls_code    NUMBER,
                                  v_msg_dtls         VARCHAR2,
                                  v_comem_code       NUMBER,
                                  v_cmb_pol_batch_no NUMBER,
                                  v_cmb_claim_no     VARCHAR2,
                                  v_gcc_code         NUMBER,
                                  v_appl_lvl         VARCHAR2) IS
    v_memo                VARCHAR2(2500);
    v_memdtls_memdet_code NUMBER;
    --v_memdtldtls_code NUMBER;
  BEGIN
    RAISE_APPLICATION_ERROR(-20001,
                            'Error check this one=' || v_mem_dtls_code);
    IF v_mem_dtls_code IS NULL THEN
    
      RAISE_ERROR('Select field to process..');
    END IF;
    v_memo := v_msg_dtls;
    BEGIN
      v_memo := TQC_MEMOS_DBPKG.PROCESS_GIS_POL_MEMO(v_cmb_pol_batch_no,
                                                     v_cmb_claim_no,
                                                     v_gcc_code,
                                                     v_msg_dtls,
                                                     v_appl_lvl);
    
    EXCEPTION
      WHEN OTHERS THEN
        v_memo := v_msg_dtls;
        --SET_ALERT_PROPERTY('CFG_ERROR',ALERT_MESSAGE_TEXT,'Memo processing failed. '||sqlerrm);
      --al_id := SHOW_ALERT('CFG_ERROR');
    END;
    BEGIN
      UPDATE TQC_MEMO_HEADER_DTLS
         SET MEMDTLS_DETAIL = v_memo
       WHERE MEMDTLS_CODE = v_mem_dtls_code;
      --AND MEMDTLS_MEMDET_CODE = :TQC_MEMO_HEADER_DTLS.MEMDTLS_MEMDET_CODE;
      COMMIT;
    
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Unable to read the memo wording....');
    END;
  END;
  PROCEDURE saveCCList(v_mem_code   NUMBER,
                       v_cc_name    VARCHAR2,
                       v_cc_address VARCHAR2,
                       v_cc_remarks VARCHAR2,
                       v_cc_type    VARCHAR2,
                       v_cc_code    NUMBER default null) IS
    v_new_cc_code NUMBER;
  BEGIN
    IF v_cc_code IS NOT NULL THEN
      BEGIN
        UPDATE TQC_COMPANY_MEMO_CC
           SET CC_COMEM_CODE = NVL(v_mem_code, CC_COMEM_CODE),
               CC_NAME       = NVL(v_cc_name, CC_NAME),
               CC_ADDRESS    = NVL(v_cc_address, CC_ADDRESS),
               CC_REMARKS    = NVL(v_cc_remarks, CC_REMARKS),
               CC_TYPE       = NVL(v_cc_type, CC_TYPE)
         WHERE CC_CODE = v_cc_code;
        COMMIT;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Unable to update cc list record....');
      END;
    ELSE
      BEGIN
        SELECT TQC_CC_CODE_SEQ.NEXTVAL INTO v_new_cc_code FROM DUAL;
        INSERT INTO TQC_COMPANY_MEMO_CC
          (CC_CODE,
           CC_COMEM_CODE,
           CC_NAME,
           CC_ADDRESS,
           CC_REMARKS,
           CC_TYPE)
        VALUES
          (v_new_cc_code,
           v_mem_code,
           v_cc_name,
           v_cc_address,
           v_cc_remarks,
           v_cc_type);
        COMMIT;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error creating cc list record...');
      end;
    END IF;
  END;
  PROCEDURE deleteCCList(v_cc_code NUMBER) IS
  BEGIN
    DELETE FROM TQC_COMPANY_MEMO_CC WHERE CC_CODE = v_cc_code;
    COMMIT;
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_ERROR('Error deleting cc list record...');
  END;
  PROCEDURE updateMemo(v_user           VARCHAR2,
                       v_appl_lvl       VARCHAR2,
                       v_memo_date      DATE,
                       v_policy_number  VARCHAR2,
                       v_quot_no        VARCHAR2,
                       v_quot_code      NUMBER,
                       v_endos_no       VARCHAR2,
                       v_clm_no         VARCHAR2 DEFAULT NULL,
                       v_gcc_code       NUMBER DEFAULT NULL,
                       v_court_case_no  VARCHAR2 DEFAULT NULL,
                       v_insured        VARCHAR2,
                       v_subject        VARCHAR2,
                       v_addressee      VARCHAR2,
                       v_address        VARCHAR2,
                       v_signatory      VARCHAR2,
                       v_authorised     VARCHAR2,
                       v_mtyp_code      NUMBER,
                       v_file_no        VARCHAR2,
                       v_comem_brn_code NUMBER,
                       v_mem_desc       VARCHAR2,
                       v_corr_code      NUMBER DEFAULT NULL,
                       v_branch         VARCHAR2,
                       v_addresse_type  VARCHAR2,
                       v_your_ref       VARCHAR2,
                       v_batch_no       NUMBER,
                       v_ref            VARCHar2,
                       v_rpt_type       VARCHAR2,
                       v_code_town      VARCHAR2,
                       v_mem_code       IN OUT NUMBER,
                       v_tran_type      VARCHAR2,
                       v_memo_subject   IN VARCHAR2,
                       v_mem_detail     IN VARCHAR2,
                       v_add_edit       IN VARCHAR2,
                       v_comem_issued IN VARCHAR2 DEFAULT NULl,
                       v_refno          OUT  VARCHAR2) IS
    v_mem_count   NUMBER;
    v_memo_code   NUMBER;
    v_memh_code   NUMBER;
    v_memdet_code NUMBER;
    v_mem_det_cnt NUMBER;
    v_count       NUMBER;
    v_usr_ref     VARCHAR2(50);
     v_name     VARCHAR2(40);
    v_rank     VARCHAR2(80);
  BEGIN
  
    IF v_tran_type = 'N' THEN
      BEGIN
      
        IF v_mtyp_code IS NOT NULL THEN
          SELECT COUNT(1)
            INTO v_mem_count
            FROM TQC_MEMOS
           WHERE MEMO_MTYP_CODE = v_mtyp_code;
        ELSE
          RAISE_ERROR('Error Creating memo record Without memo type Details.Ensure MEMO Type is Selected ...');
        END IF;
        
        
      
              BEGIN
              SELECT USR_NAME,
                     decode(USR_SIGN, 'Y', USR_PERSONEL_RANK, 'FOR THE COMPANY'),
                     usr_ref
                INTO v_name, v_rank, v_usr_ref
                FROM TQC_USERS
               WHERE USR_USERNAME = v_user;
            EXCEPTION
              WHEN NO_DATA_FOUND THEN
                NULL;
              WHEN OTHERS THEN
                NULL;
            END;
      
        --        TQC_MEMO_TYPES
        SELECT TQC_COMEM_CODE_SEQ.NEXTVAL INTO v_mem_code FROM DUAL;
        
        v_refno := nvl(v_usr_ref, v_user) || '/' || v_mem_code;
      
        INSERT INTO TQC_COMPANY_MEMOS
          (COMEM_CODE,
           COMEM_SYS_CODE,
           COMEM_DESC,
           COMEM_DATE,
           COMEM_CMB_CLAIM_NO,
           COMEM_SUBJECT,
           COMEM_ADDRESS,
           COMEM_POL_POLICY_NO,
           COMEM_APPL_LVL,
           COMEM_DONE_BY,
           COMEM_AUTHORIZED,
           COMEM_YOUR_REF,
           COMEM_ADDRESSEE,
           COMEM_MTYP_CODE,
           COMEM_POL_REN_ENDOS_NO,
           COMEM_BRN_CODE,
           COMEM_PREPARED_BY,
           COMEM_CORR_CODE,
           COMEM_REF,
           COMEM_POL_BATCH_NO,
           COMEM_FILE_NO,
           COMEM_INSURED,
           COMEM_RPT_TYPE,
           COMEM_CODETOWN,
           COMEM_COURT_CASE_NO,
           COMEM_GCC_CODE,
           COMEM_QUOT_CODE,
           COMEM_QUOT_NO)
        VALUES
          (v_mem_code,
           37,
           v_mem_desc,
           v_memo_date,
           v_clm_no,
           v_subject,
           v_address,
           v_policy_number,
           v_appl_lvl,
           v_user,
           'N',
           v_your_ref,
           v_addressee,
           v_mtyp_code,
           v_endos_no,
           v_comem_brn_code,
           v_user,
           v_corr_code,
           v_ref,
           v_batch_no,
           v_refno,
           v_insured,
           v_rpt_type,
           v_code_town,
           v_court_case_no,
           v_gcc_code,
           v_quot_code,
           v_quot_no);
      
        --TO INCLUDE CLAIM ACTIVITIES ON MEMO CREATION..
        -- gin_stp_claims_pkg.create_clm_activities(null,'',SYSDATE,'A',v_clm_no,'CREATED A MEMO','','created a memo',SYSDATE);
      
        IF NVL(v_mem_count, 0) > 0 THEN
        
          SELECT MAX(MEMO_CODE)
            INTO v_memo_code
            FROM TQC_MEMOS
           WHERE MEMO_MTYP_CODE = v_mtyp_code;
        
          SELECT TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ||
                 TQC_MEMH_CODE_SEQ.NEXTVAL
            INTO v_memh_code
            FROM DUAL;
        
          INSERT INTO TQC_MEMO_HEADER
            (MEMH_CODE, MEMH_COMEM_CODE, MEMH_MEMO_CODE, MEMH_MEMO_SUBJECT)
          VALUES
            (v_memh_code, v_mem_code, v_memo_code, v_memo_subject);
        
          SELECT MEMDET_CODE
            INTO v_memdet_code
            FROM TQC_MEMO_DETAILS
           WHERE MEMDET_MEMO_CODE = v_memo_code;
        
          INSERT INTO TQC_MEMO_HEADER_DTLS
            (MEMDTLS_MEMH_CODE,
             MEMDTLS_MEMDET_CODE,
             MEMDTLS_DETAIL,
             MEMDTLS_CODE)
          VALUES
            (v_memh_code,
             v_memdet_code,
             v_mem_detail,
             TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY') ||
                       TQC_MEMDTLS_CODE_SEQ.NEXTVAL));
        ELSE
          RAISE_ERROR('Error Creating memo/Letter Subject and Details Record Check the Selected Memo Type ...');
        END IF;
      
      EXCEPTION
        WHEN OTHERS THEN
          null;
          --RAISE_ERROR('Error Creating memo record ' || v_mtyp_code ||
                    --  'Memo Code ' || v_memo_code);
      END;
    
    ELSE
    
      BEGIN
      
        IF v_mtyp_code IS NOT NULL THEN
          SELECT COUNT(1)
            INTO v_mem_count
            FROM TQC_MEMOS
           WHERE MEMO_MTYP_CODE = v_mtyp_code;
--        ELSE
--          RAISE_ERROR('Error UPDATING memo record Without memo type Details.Ensure MEMO Type is Selected ...');
       END IF;
        UPDATE TQC_COMPANY_MEMOS
           SET COMEM_DESC             = NVL(v_mem_desc, COMEM_DESC),
               COMEM_DATE             = NVL(v_memo_date, COMEM_DATE),
               COMEM_CMB_CLAIM_NO     = NVL(v_clm_no, COMEM_CMB_CLAIM_NO),
               COMEM_SUBJECT          = NVL(v_subject, COMEM_SUBJECT),
               COMEM_ADDRESS          = NVL(v_address, COMEM_ADDRESS),
               COMEM_POL_POLICY_NO    = NVL(v_policY_number,
                                            COMEM_POL_POLICY_NO),
               COMEM_APPL_LVL         = NVL(v_appl_lvl, COMEM_APPL_LVL),
               COMEM_DONE_BY          = NVL(v_user, COMEM_DONE_BY),
               COMEM_YOUR_REF         = NVL(v_your_ref, COMEM_YOUR_REF),
               COMEM_ADDRESSEE        = NVL(v_addressee, COMEM_ADDRESSEE),
              -- COMEM_MTYP_CODE        = NVL(v_mtyp_code, COMEM_MTYP_CODE),
               COMEM_POL_REN_ENDOS_NO = NVL(v_endos_no,
                                            COMEM_POL_REN_ENDOS_NO),
               COMEM_BRN_CODE         = NVL(v_comem_brn_code, COMEM_BRN_CODE),
               COMEM_PREPARED_BY      = NVL(v_user, COMEM_PREPARED_BY),
               COMEM_CORR_CODE        = NVL(v_corr_code, COMEM_CORR_CODE),
               COMEM_REF              = NVL(v_ref, COMEM_REF),
               COMEM_POL_BATCH_NO     = NVL(v_batch_no, COMEM_POL_BATCH_NO),
               COMEM_FILE_NO          = NVL(v_file_no, COMEM_FILE_NO),
               COMEM_INSURED          = NVL(v_insured, COMEM_INSURED),
               COMEM_RPT_TYPE         = NVL(v_rpt_type, COMEM_RPT_TYPE),
               COMEM_CODETOWN         = NVL(v_code_town, COMEM_CODETOWN),
               COMEM_COURT_CASE_NO    = NVL(v_court_case_no,
                                            COMEM_COURT_CASE_NO),
               COMEM_GCC_CODE         = NVL(v_gcc_code, COMEM_GCC_CODE),
               COMEM_QUOT_CODE        = NVL(v_quot_code, COMEM_QUOT_CODE),
               COMEM_QUOT_NO          = NVL(v_quot_no, COMEM_QUOT_NO)
         WHERE COMEM_CODE = v_mem_code;
      
        IF NVL(v_mem_count, 0) > 0 THEN
        
          SELECT MAX(MEMO_CODE)
            INTO v_memo_code
            FROM TQC_MEMOS
           WHERE MEMO_MTYP_CODE = v_mtyp_code;
        
          BEGIN
         -- RAISE_ERROR('v_mem_code'||v_mem_code);
            SELECT MAX(MEMH_CODE)
              INTO v_memh_code
              FROM TQC_MEMO_HEADER
             WHERE MEMH_COMEM_CODE = v_mem_code;
            
             
            UPDATE TQC_MEMO_HEADER
               SET MEMH_MEMO_SUBJECT = v_memo_subject
             WHERE MEMH_CODE = v_memh_code;
          
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              SELECT MAX(MEMO_CODE)
                INTO v_memo_code
                FROM TQC_MEMOS
               WHERE MEMO_MTYP_CODE = v_mtyp_code;
            
              SELECT TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ||
                     TQC_MEMH_CODE_SEQ.NEXTVAL
                INTO v_memh_code
                FROM DUAL;
            
              INSERT INTO TQC_MEMO_HEADER
                (MEMH_CODE,
                 MEMH_COMEM_CODE,
                 MEMH_MEMO_CODE,
                 MEMH_MEMO_SUBJECT)
              VALUES
                (v_memh_code, v_mem_code, v_memo_code, v_memo_subject);
            
--            WHEN OTHERS THEN
--              RAISE_ERROR('An Error Occured while creating memo header');
          END;
        
          SELECT COUNT(1)
            INTO v_mem_det_cnt
            FROM TQC_MEMO_HEADER_DTLS
           WHERE MEMDTLS_MEMH_CODE = v_memh_code;
        
          IF NVL(v_mem_det_cnt, 0) = 0 THEN
            SELECT MEMDET_CODE
              INTO v_memdet_code
              FROM TQC_MEMO_DETAILS
             WHERE MEMDET_MEMO_CODE = v_memo_code;
          
            INSERT INTO TQC_MEMO_HEADER_DTLS
              (MEMDTLS_MEMH_CODE,
               MEMDTLS_MEMDET_CODE,
               MEMDTLS_DETAIL,
               MEMDTLS_CODE)
            VALUES
              (v_memh_code,
               v_memdet_code,
               v_mem_detail,
               TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY') ||
                         TQC_MEMDTLS_CODE_SEQ.NEXTVAL));
          
          ELSE
            SELECT MEMDET_CODE
              INTO v_memdet_code
              FROM TQC_MEMO_DETAILS
             WHERE MEMDET_MEMO_CODE = v_memo_code;
          
            UPDATE TQC_MEMO_HEADER_DTLS
               SET MEMDTLS_DETAIL = v_mem_detail
             WHERE MEMDTLS_MEMH_CODE = v_memh_code
               AND MEMDTLS_MEMDET_CODE = v_memdet_code;
          
          END IF;
        ELSE
          RAISE_ERROR('Error UPDATING memo/Letter Subject and Details Record Check the Selected Memo Type ...');
        END IF;
      
--      EXCEPTION
--        WHEN OTHERS THEN
--          RAISE_ERROR('Error updating memo record no...' || v_mtyp_code);
      END;
    END IF;
  END;
  PROCEDURE authorise_memo(v_user      VARCHAR2,
                           v_memo_code NUMBER,
                           v_status    OUT VARCHAR2,
                           v_auth_dt   OUT DATE,
                           v_sign      OUT VARCHAR2) IS
    v_name VARCHAR2(30);
    v_rank VARCHAR2(60);
    ncode  number;
  BEGIN
  -- RAISE_ERROR('v_memo_code '||v_memo_code);
    BEGIN
      SELECT USR_NAME,
             decode(USR_SIGN,
                    'Y',
                    USR_PERSONEL_RANK,
                    nvl(USR_PERSONEL_RANK, 'FOR THE COMPANY'))
        INTO v_name, v_rank
        FROM TQC_USERS
       WHERE USR_USERNAME = v_user;
    
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        RAISE_error('Error retrieving name and rank of user ' || v_user ||
                    ' username missing in DB..');
      WHEN OTHERS THEN
        RAISE_ERROR('Error retrieving name and rank of user ' || v_user);
    END;
    
    v_auth_dt :=TRUNC(SYSDATE);
    
    v_sign := v_name ; 
    
    BEGIN
      UPDATE TQC_COMPANY_MEMOS
         SET COMEM_AUTHORIZED      = 'Y',
             COMEM_AUTHORIZED_DATE = v_auth_dt,
             COMEM_AUTHORIZED_BY   = v_name,
             COMEM_SIGN_RANK       = v_rank,
             COMEM_SIGNATORY       = v_name
       WHERE COMEM_CODE = v_memo_code;
      v_status := 'Y';
      ---and COMEM_MTYP_CODE = :TQC_COMPANY_MEMOS.COMEM_MTYP_CODE;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Error authorizing the letter/memo..');
    END;
  END;
  PROCEDURE cancel_authorise_memo(v_user VARCHAR2, v_memo_code NUMBER) IS
    v_name VARCHAR2(30);
    v_rank VARCHAR2(60);
    ncode  number;
  BEGIN
    BEGIN
      SELECT USR_NAME,
             decode(USR_SIGN,
                    'Y',
                    USR_PERSONEL_RANK,
                    nvl(USR_PERSONEL_RANK, 'FOR THE COMPANY'))
        INTO v_name, v_rank
        FROM TQC_USERS
       WHERE USR_USERNAME = v_user;
    
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        RAISE_error('Error retrieving name and rank of user ' || v_user ||
                    ' username missing in DB..');
      WHEN OTHERS THEN
        RAISE_ERROR('Error retrieving name and rank of user ' || v_user);
    END;
    BEGIN
      UPDATE TQC_COMPANY_MEMOS
         SET COMEM_STATUS      = 'C',
             COMEM_CANCEL_DATE = TRUNC(SYSDATE),
             COMEM_CANCEL_BY   = v_name
       WHERE COMEM_CODE = v_memo_code;
      COMMIT;
      ---and COMEM_MTYP_CODE = :TQC_COMPANY_MEMOS.COMEM_MTYP_CODE;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Error authorizing the letter/memo..');
    END;
  END;
 PROCEDURE delete_memo(v_memo_code NUMBER)
IS
cursor cur_memo_h is select MEMH_CODE, MEMH_MEMO_CODE,MEMH_COMEM_CODE
        from TQC_MEMO_HEADER
        where MEMH_COMEM_CODE=v_memo_code;
BEGIN
   FOR cur_memo_h_rec IN cur_memo_h LOOP
      begin
          delete TQC_MEMO_HEADER_DTLS
          where MEMDTLS_MEMH_CODE = cur_memo_h_rec.MEMH_CODE;
     EXCEPTION
        when others then
        raise_application_error(-20003,'Unable delete memo details');
        END;
    END LOOP;
    DELETE TQC_COMPANY_MEMO_CC 
    WHERE CC_COMEM_CODE=v_memo_code; 
    delete TQC_MEMO_HEADER
    where MEMH_COMEM_CODE =v_memo_code;
    DELETE TQC_COMPANY_MEMOS
    WHERE COMEM_CODE=v_memo_code;
 EXCEPTION
  WHEN OTHERS THEN
      RAISE_ERROR('Error deleting cc list record...'); 
END;
--FUNCTION PROCESS_GIS_POL_MEMO(v_pol_batch_no IN NUMBER,
--                        v_claim_no IN VARCHAR2,
--                        v_gcc_code IN NUMBER,
--                        v_raw_txt IN VARCHAR2,
--                        v_app_lvl IN VARCHAR2) RETURN VARCHAR2 IS

--      lv_temp_txt       VARCHAR2(10000);
--    v_pol_no          GIN_POLICIES.POL_POLICY_NO%TYPE;
--    v_clm_no          GIN_CLAIM_MASTER_BOOKINGS.CMB_CLAIM_NO%TYPE;
--    v_sa              NUMBER;
--    v_premium         NUMBER;
--    v_client          TQC_CLIENTS.CLNT_NAME%TYPE;
--    v_prem            NUMBER;
--    v_date            DATE;
--    v_time            DATE;
--    v_reg             VARCHAR2(100);
--    v_loc             VARCHAR2(100);
--    v_courtname       VARCHAR2(100);
--    v_courtdate       DATE ;
--    v_caseno          VARCHAR2(100);
--    vcoverfrom        DATE ;
--    vcoverto          DATE ;
--    veffdate          DATE ;
--    vrendate          DATE;
--    v_desc            VARCHAR2(100);
--    v_loss_type       VARCHAR2(100);
--    v_item_desc       VARCHAR2(100);
--    v_claimant_type   VARCHAR2(15);
--    v_agent           TQC_AGENCIES.AGN_NAME%TYPE;
--    v_int_parties     VARCHAR2(100);
--    v_uw_year         INT;
--    v_branch          TQC_BRANCHES.BRN_NAME%TYPE; 
--    v_address         VARCHAR2(100);
--    v_twn             VARCHAR2(100);
--    v_risk_id         VARCHAR2(100);
--    v_surname         TQC_CLIENTS.CLNT_NAME%TYPE;
--    v_risk_desc       GIN_INSURED_PROPERTY_UNDS.IPU_ITEM_DESC%TYPE;
--    v_risk_wef        DATE;
--    v_risk_wet        DATE;
--    v_cover           VARCHAR2(50);
--    v_risk_prem       DECIMAL(20,2);
--    v_risk_value      DECIMAL(20,2);
--    v_currency        VARCHAR2(5);
--    v_agn_code NUMBER;
--    v_agn_name TQC_AGENCIES.AGN_NAME%TYPE;
--    v_agn_add TQC_AGENCIES.AGN_PHYSICAL_ADDRESS%TYPE;
--    v_client_code NUMBER;
--    v_client_add VARCHAR2(100);
--    v_insured_name VARCHAR2(300);
--    v_insured_add VARCHAR2(100);
--    v_org_name VARCHAR2(100);
--    v_org_add VARCHAR2(100);
--    v_ipu_desc GIN_INSURED_PROPERTY_UNDS.IPU_ITEM_DESC%TYPE;
--    v_amt_words VARCHAR2(50);
--    v_cur_code NUMBER;
--    v_period NUMBER; 
--    
--     v_obligor   GIN_BONDS.BON_OBLIGOR%TYPE;
--    v_ob_address GIN_BONDS.OBLIGOR_ADDRS%TYPE;
--    v_ob_town   GIN_BONDS.BON_OBLIGOR_TOWN%TYPE;
--    v_ob_country GIN_BONDS.BON_OBLIGOR_COUNTRY%TYPE;
--    v_ob_emp     GIN_BONDS.BON_EMPLOYER%TYPE; 
--    v_res_amt    GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;  
--    v_original_estimate GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE; 
--    v_dep_amt GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
--    v_excess_amt GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
--    v_ipu_code   GIN_INSURED_PROPERTY_UNDS.IPU_CODE%TYPE;
--    v_clnt_tel   TQC_CLIENTS.CLNT_TEL%TYPE;
--    v_peril      GIN_CLAIM_PERILS.CLMP_PER_PT_SHT_DESC%TYPE;
--    v_excess_rate NUMBER;
--    v_dep_rate NUMBER;
--    v_agn_tel  TQC_AGENCIES.AGN_TEL1%TYPE;
--    BEGIN
--     
--    lv_temp_txt := v_raw_txt;
--    IF v_app_lvl = 'U' THEN
--        IF v_pol_batch_no IS NOT NULL THEN
--        BEGIN
--              
--             SELECT POL_POLICY_NO,POL_SI_DIFF,GIS_UTILITIES.CLNT_NAME(CLNT_NAME,CLNT_OTHER_NAMES) CLNT_NAME,POL_TOT_ENDOS_DIFF_AMT,
--             POL_POLICY_COVER_FROM,POL_POLICY_COVER_TO,POL_WEF_DT,POL_RENEWAL_DT,AGN_NAME,POL_OTH_INT_PARTIES,
--             POL_UW_YEAR,BRN_NAME,CLNT_POSTAL_ADDRS,TWN_NAME,IPU_PROPERTY_ID,IPU_ITEM_DESC,IPU_WEF,IPU_WET,IPU_COVT_SHT_DESC,IPU_ENDOS_DIFF_AMT,IPU_VALUE               
--             INTO v_pol_no,v_sa,v_client,v_prem,vcoverfrom, vcoverto,veffdate, vrendate,v_agent,v_int_parties,v_uw_year,
--             v_branch,v_address,v_twn,v_risk_id,v_risk_desc,v_risk_wef,v_risk_wet,v_cover,v_risk_prem,v_risk_value
--             FROM GIN_POLICIES ,TQC_CLIENTS,TQC_AGENCIES,TQC_BRANCHES,TQC_TOWNS,TQ_GIS.GIN_INSURED_PROPERTY_UNDS
--             WHERE POL_PRP_CODE= CLNT_CODE
--             AND POL_AGNT_AGENT_CODE=AGN_CODE
--             AND TWN_CODE(+)=CLNT_TWN_CODE
--             AND BRN_CODE=POL_BRN_CODE
--             AND IPU_POL_BATCH_NO=POL_BATCH_NO
--             AND pol_batch_no = v_pol_batch_no
--             AND ROWNUM <2;

--             lv_temp_txt := REPLACE(lv_temp_txt,'[POLICYNO]',v_pol_no);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[POLNO]',v_pol_no);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[COVERFROM]',TO_CHAR(vcoverfrom,'DD/MM/RRRR'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTO]',TO_CHAR(vcoverto,'DD/MM/RRRR'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[EFFDATE]',TO_CHAR(veffdate,'DD/MM/YYYY'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RENEWALDATE]',TO_CHAR(vrendate,'DD/MM/RRRR'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[SA]',TO_CHAR(NVL(v_sa,0),'FM999,999,999.00'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[PREMIUM]',TO_CHAR(NVL(v_prem,0),'FM999,999,999.00'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[AGENT]',v_agent);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[INTPARTIES]',v_int_parties);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[UWYEAR]',v_uw_year);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[BRANCH]',v_branch);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[ADDRESS]',v_address);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[TOWN]',v_twn);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKID]',v_risk_id);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKDESC]',v_risk_desc);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWEF]',v_risk_wef);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWET]',v_risk_wet);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTYPE]',v_cover);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKPREM]',v_risk_prem);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKVALUE]',v_risk_value); 
--             --RAISE_ERROR('HERE');
--              lv_temp_txt := REPLACE(lv_temp_txt,'[WEF]',TO_CHAR(veffdate,'DD/MM/YYYY'));
--              lv_temp_txt := REPLACE(lv_temp_txt,'[WET]',TO_CHAR(vcoverto,'DD/MM/YYYY'));
--         EXCEPTION
--             WHEN NO_DATA_FOUND THEN NULL;
--             WHEN OTHERS THEN
--                RAISE_APPLICATION_ERROR(-20001,'ERROR: '||SQLERRM(SQLCODE));
--         END;
--        ELSE
--            IF v_pol_no IS NOT NULL THEN
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[POLICYNO]',v_pol_no);
--            END IF;
--        END IF;
--        ELSIF v_app_lvl IN('R','RN')  THEN
--        IF v_pol_batch_no IS NOT NULL THEN
--        BEGIN
--             SELECT POL_POLICY_NO,POL_SI_DIFF,GIS_UTILITIES.CLNT_NAME(CLNT_NAME,CLNT_OTHER_NAMES) CLNT_NAME,POL_TOT_ENDOS_DIFF_AMT,
--             POL_POLICY_COVER_FROM,POL_POLICY_COVER_TO,POL_WEF_DT,POL_RENEWAL_DT,AGN_NAME,POL_OTH_INT_PARTIES,
--             POL_UW_YEAR,BRN_NAME,CLNT_POSTAL_ADDRS,TWN_NAME,IPU_PROPERTY_ID,IPU_ITEM_DESC,IPU_WEF,IPU_WET,IPU_COVT_SHT_DESC,IPU_ENDOS_DIFF_AMT,IPU_VALUE               
--             INTO v_pol_no,v_sa,v_client,v_prem,vcoverfrom, vcoverto,veffdate, vrendate,v_agent,v_int_parties,v_uw_year,
--             v_branch,v_address,v_twn,v_risk_id,v_risk_desc,v_risk_wef,v_risk_wet,v_cover,v_risk_prem,v_risk_value
--             FROM GIN_REN_POLICIES ,TQC_CLIENTS,TQC_AGENCIES,TQC_BRANCHES,TQC_TOWNS,TQ_GIS.GIN_INSURED_PROPERTY_UNDS
--             WHERE POL_PRP_CODE= CLNT_CODE
--             AND POL_AGNT_AGENT_CODE=AGN_CODE
--             AND TWN_CODE(+)=CLNT_TWN_CODE
--             AND BRN_CODE=POL_BRN_CODE
--             AND IPU_POL_BATCH_NO=POL_BATCH_NO
--             AND pol_batch_no = v_pol_batch_no
--             AND ROWNUM <2;

--             lv_temp_txt := REPLACE(lv_temp_txt,'[POLICYNO]',v_pol_no);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[POLNO]',v_pol_no);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[COVERFROM]',TO_CHAR(vcoverfrom,'DD/MM/RRRR'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTO]',TO_CHAR(vcoverto,'DD/MM/RRRR'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[EFFDATE]',TO_CHAR(veffdate,'DD/MM/YYYY'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RENEWALDATE]',TO_CHAR(vrendate,'DD/MM/RRRR'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[SA]',TO_CHAR(NVL(v_sa,0),'FM999,999,999.00'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[PREMIUM]',TO_CHAR(NVL(v_prem,0),'FM999,999,999.00'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[AGENT]',v_agent);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[INTPARTIES]',v_int_parties);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[UWYEAR]',v_uw_year);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[BRANCH]',v_branch);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[ADDRESS]',v_address);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[TOWN]',v_twn);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKID]',v_risk_id);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKDESC]',v_risk_desc);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWEF]',v_risk_wef);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWET]',v_risk_wet);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTYPE]',v_cover);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKPREM]',v_risk_prem);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[RISKVALUE]',v_risk_value); 
--             --RAISE_ERROR('HERE');
--              lv_temp_txt := REPLACE(lv_temp_txt,'[WEF]',TO_CHAR(veffdate,'DD/MM/YYYY'));
--              lv_temp_txt := REPLACE(lv_temp_txt,'[WET]',TO_CHAR(vcoverto,'DD/MM/YYYY'));
--         EXCEPTION
--             WHEN NO_DATA_FOUND THEN NULL;
--             WHEN OTHERS THEN
--                RAISE_APPLICATION_ERROR(-20001,'ERROR: '||SQLERRM(SQLCODE));
--         END;
--        ELSE
--            IF v_pol_no IS NOT NULL THEN
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[POLICYNO]',v_pol_no);
--            END IF;
--        END IF;
--    ELSIF v_app_lvl = 'S' THEN
--    
--           
--    
--      SELECT POL_POLICY_NO,POL_TOTAL_SUM_INSURED,GIS_UTILITIES.CLNT_NAME(CLNT_NAME,CLNT_OTHER_NAMES) CLNT_NAME,
--       CLNT_NAME,POL_TOTAL_FAP,
--        POL_POLICY_COVER_FROM,POL_POLICY_COVER_TO,POL_WEF_DT,POL_RENEWAL_DT,POL_AGNT_AGENT_CODE,POL_CUR_CODE,POL_OTH_INT_PARTIES
--        INTO v_pol_no,v_sa,v_client,v_surname, v_prem,
--        vcoverfrom, vcoverto,veffdate, vrendate,v_agn_code,v_cur_code,v_int_parties
--        FROM GIN_POLICIES ,TQC_CLIENTS
--        WHERE POL_PRP_CODE = CLNT_CODE
--        AND pol_batch_no = v_pol_batch_no;
--        --MESSAGE('[POLICYNO]'||lv_temp_txt);PAUSE;
--        lv_temp_txt := REPLACE(lv_temp_txt,'[POLICYNO]',v_pol_no);
--        lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
--        lv_temp_txt := REPLACE(lv_temp_txt,'[SURNAME]',v_surname); 
--        lv_temp_txt := REPLACE(lv_temp_txt,'[COVERFROM]',to_char(vcoverfrom,'ddth') ||' day of '||trim(to_char(vcoverfrom,'Month'))||' '||to_char(vcoverfrom,'RRRR'));
--        lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTO]',to_char(vcoverto,'ddth') ||' day of '||trim(to_char(vcoverto,'Month'))||' '||to_char(vcoverto,'RRRR'));
--        lv_temp_txt := REPLACE(lv_temp_txt,'[EFFDATE]',to_char(veffdate,'ddth') ||' day of '||trim(to_char(vcoverfrom,'Month'))||' '||to_char(vcoverfrom,'RRRR'));
--        lv_temp_txt := REPLACE(lv_temp_txt,'[RENEWALDATE]',TO_CHAR(vrendate,'DD/MM/YYYY'));
--        lv_temp_txt := REPLACE(lv_temp_txt,'[INTPARTIES]',v_int_parties);
--        --MESSAGE(v_pol_no||lv_temp_txt);PAUSE;
--       lv_temp_txt := REPLACE(lv_temp_txt,'[SA]',TO_CHAR(NVL(v_sa,0),'FM999,999,999.00'));
--        -- lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
--        lv_temp_txt := REPLACE(lv_temp_txt,'[PREMIUM]',NVL(v_prem,0)); 
--        lv_temp_txt := REPLACE(lv_temp_txt,'[AMTINWORDS]',FMS_CHEQUES_PKG.num_to_words(NVL(v_sa,0),v_cur_code));
--        SELECT MONTHS_BETWEEN(vcoverto,vcoverfrom) INTO v_period FROM DUAL;
--        lv_temp_txt := REPLACE(lv_temp_txt,'[PERIOD]',ROUND(v_period,0));
--         SELECT IPU_ITEM_DESC INTO v_ipu_desc
--         FROM TQ_GIS.GIN_INSURED_PROPERTY_UNDS
--         WHERE IPU_POL_BATCH_NO=v_pol_batch_no
--         AND ROWNUM=1;
--         lv_temp_txt := REPLACE(lv_temp_txt,'[ITEMDESC]',v_ipu_desc);
--     
--         SELECT ORG_NAME,ORG_PHY_ADDRS INTO v_org_name ,v_org_add
--         FROM TQC_ORGANIZATIONS
--         WHERE ORG_CODE=2;
--          lv_temp_txt := REPLACE(lv_temp_txt,'[ORGNAME]',v_org_name);
--          lv_temp_txt := REPLACE(lv_temp_txt,'[ORGADDR]',v_org_add);
--     
--         SELECT AGN_NAME,AGN_PHYSICAL_ADDRESS
--         INTO v_agn_name,v_agn_add
--         FROM TQC_AGENCIES
--         WHERE AGN_CODE = v_agn_code;
--     
--          lv_temp_txt := REPLACE(lv_temp_txt,'[AGNNAME]',v_agn_name);
--          lv_temp_txt := REPLACE(lv_temp_txt,'[AGNADDR]',v_agn_add);
--         BEGIN
--             SELECT GIS_UTILITIES.CLNT_NAME(CLNT_NAME,CLNT_OTHER_NAMES) CLNT_NAME ,CLNT_PHYSICAL_ADDRS
--             INTO v_insured_name,v_insured_add
--             FROM TQ_GIS.GIN_POLICY_INSUREDS,TQC_CLIENTS
--             WHERE POLIN_PRP_CODE=CLNT_CODE
--             AND POLIN_POL_BATCH_NO=v_pol_batch_no
--             AND ROWNUM=1;
--          EXCEPTION
--             WHEN NO_DATA_FOUND THEN
--               NULL;
--          END;
--             
--         lv_temp_txt := REPLACE(lv_temp_txt,'[INSNAME]',v_insured_name);
--         lv_temp_txt := REPLACE(lv_temp_txt,'[INSADDR]',v_insured_add);
--         
--         BEGIN
--             SELECT  BON_OBLIGOR,OBLIGOR_ADDRS,BON_OBLIGOR_TOWN,BON_OBLIGOR_COUNTRY,BON_EMPLOYER,BON_CONTRACT_DATE
--             INTO  v_obligor,v_ob_address,v_ob_town,v_ob_country,v_ob_emp,v_date
--             FROM GIN_BONDS,GIN_INSURED_PROPERTY_UNDS
--             WHERE BON_IPU_CODE=IPU_CODE
--             AND IPU_POL_BATCH_NO=v_pol_batch_no;
--         EXCEPTION
--         WHEN NO_DATA_FOUND THEN
--           NULL;
--         END;
--     
--          lv_temp_txt := REPLACE(lv_temp_txt,'[BANK]',v_obligor);
--          lv_temp_txt := REPLACE(lv_temp_txt,'[BANKADD]',v_ob_address);
--          lv_temp_txt := REPLACE(lv_temp_txt,'[BANKTWN]',v_ob_town);
--          lv_temp_txt := REPLACE(lv_temp_txt,'[BANKSTATE]',v_ob_country);
--          lv_temp_txt := REPLACE(lv_temp_txt,'[EMPLOYER]',v_ob_emp);
--          lv_temp_txt := REPLACE(lv_temp_txt,'[CONTRACTDATE]',to_char(v_date,'ddth') ||' day of '||trim(to_char(vcoverfrom,'Month'))||' '||to_char(vcoverfrom,'RRRR'));
--    ELSIF v_app_lvl = 'C' THEN

--        BEGIN
--              SELECT DISTINCT IPU_CODE,POL_POLICY_NO,CMB_CLAIM_NO,POL_TOTAL_SUM_INSURED,DECODE(REG_THIRD_PARTY,'S',GIS_UTILITIES.CLNT_NAME(CLNT_NAME,CLNT_SURNAME),CLD_OTHER_NAMES||' '||CLD_SURNAME) CLAIMANT_NAME
--              ,POL_TOT_ENDOS_DIFF_AMT,CMB_CLAIM_DATE, CMB_LOSS_DATE_TIME, CMB_IPU_PROPERTY_ID RISK_ID, CMB_LOCATION,CMB_LOSS_DESC,
--              nvl(CMB_OTHER_COVER_DETAILS,'Repairable loss') LOSS_TYPE,
--             IPU_ITEM_DESC RISK_DESCRIPTION,
--             DECODE(REG_THIRD_PARTY,'S','SELF','THIRD PARTY') CLAIMANT_TYPE,GET_OS_RESERVE(v_claim_no),CLNT_PHYSICAL_ADDRS
--             INTO v_ipu_code,v_pol_no,v_clm_no,v_sa,v_client,v_prem, v_date, v_time, v_reg, v_loc,v_desc,v_loss_type,v_item_desc,v_claimant_type,v_res_amt,v_address
--             FROM GIN_CLAIM_MASTER_BOOKINGS,GIN_POLICIES ,TQC_CLIENTS,TQ_GIS.GIN_INSURED_PROPERTY_UNDS,GIN_RGSTD_CLAIMANTS,GIN_CLAIMANTS
--             WHERE CMB_POL_BATCH_NO = POL_BATCH_NO
--             AND REG_CLD_CODE=CLNT_CODE
--             AND IPU_POL_BATCH_NO=POL_BATCH_NO
--             AND REG_CLD_CODE = CLD_CODE(+)
--             AND POL_PRP_CODE = CLNT_CODE
--             AND CMB_CLAIM_NO = v_claim_no
--             AND ROWNUM<2;
--             
--             BEGIN
--                SELECT NVL(CLMREV_AMT,0) INTO v_original_estimate
--                FROM GIN_CLAIM_REVISIONS
--                WHERE CLMREV_CMB_CLAIM_NO =v_claim_no
--                AND CLMREV_GGT_TRAN_TYPE = 'LOP';
--             EXCEPTION
--             WHEN NO_DATA_FOUND THEN
--              v_original_estimate:=0;
--             WHEN OTHERS THEN
--              RAISE_ERROR('Unable to search Original Estimate'||SQLERRM(SQLCODE));
--             END;
-- 
--            BEGIN
----                SELECT  NVL(CLMP_EXCESS_AMT,0),NVL((NVL(CLMP_DEPRPRD_RATE,0)/100)*v_sa,0),NVL(CLMP_DEPRPRD_RATE,0)
----                INTO  v_excess_amt,v_dep_amt ,v_dep_rate
----                FROM GIN_CLAIM_PERILS
----                WHERE CLMP_CMB_CLAIM_NO = v_claim_no;
--             NULL;
--             EXCEPTION
--             WHEN NO_DATA_FOUND THEN
--              v_dep_amt:=0;
--              v_excess_amt:=0;
--             WHEN OTHERS THEN
--              RAISE_ERROR('Unable to search Depreciation and Excess Amounts'||SQLERRM(SQLCODE));
--             END;
--              
--             BEGIN
--                  SELECT   NVL(PRSPR_EXCESS,0) INTO v_excess_rate
--                  FROM GIN_POL_RISK_SECTION_PERILS
--                  WHERE PRSPR_IPU_CODE=v_ipu_code
--                  AND NVL(PRSPR_EXCESS_TYPE,'A')='P';
--             EXCEPTION
--             WHEN NO_DATA_FOUND THEN
--              v_excess_rate:=0;
--             WHEN OTHERS THEN
--              RAISE_ERROR('Unable to select excess rate'||SQLERRM(SQLCODE));
--             END;
--            
--             
--             IF v_gcc_code IS NOT NULL THEN
--                    SELECT GCC_COURT_NAME, GCC_COURT_DATE, GCC_CASE_NO
--                    INTO v_courtname, v_courtdate, v_caseno
--                     FROM GIN_COURT_CASES
--                     WHERE GCC_CMB_CLAIM_NO = v_claim_no
--                     AND GCC_CODE = v_gcc_code;
--             END IF;

--             lv_temp_txt := REPLACE(lv_temp_txt,'[POLICYNO]',v_pol_no);
--             --rAISE_ERROR(v_client||';'||v_client);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[CLAIMNO]',v_clm_no);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[SA]',v_sa);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[20SA]',(v_sa*20/100));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[30SA]',(v_sa*30/100));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[50SA]',(v_sa*50/100));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[70SA]',(v_sa*70/100));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[CLAIMANTNAME]',v_client);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[INSUREDADDRESS]',v_address);---copy this
--             lv_temp_txt := REPLACE(lv_temp_txt,'[PREMIUM]',v_prem); 
--             lv_temp_txt := REPLACE(lv_temp_txt,'[DATE]',to_char(TRUNC(v_date),'ddth') ||' day of '||trim(to_char(TRUNC(v_date),'Month'))||' '||to_char(TRUNC(v_date),'RRRR'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[DATEADV]',to_char(TRUNC(v_date),'ddth') ||' day of '||trim(to_char(TRUNC(v_date),'Month'))||' '||to_char(TRUNC(v_date),'RRRR'));
--             
--              lv_temp_txt := REPLACE(lv_temp_txt,'[LOSSDATE]',to_char(TRUNC(v_time),'ddth') ||' day of '||trim(to_char(TRUNC(v_time),'Month'))||' '||to_char(TRUNC(v_time),'RRRR'));
--              lv_temp_txt := REPLACE(lv_temp_txt,'[REGISTRATION]',v_reg);
--              lv_temp_txt := REPLACE(lv_temp_txt,'[LOCATION]',v_loc);
--              lv_temp_txt := REPLACE(lv_temp_txt,'[CASENO]',v_caseno);
--              lv_temp_txt := REPLACE(lv_temp_txt,'[CASEDATE]',v_courtdate);
--              lv_temp_txt := REPLACE(lv_temp_txt,'[COURT]',v_courtname);
--              lv_temp_txt := REPLACE(lv_temp_txt,'[LOSSDESC]',v_desc );
--              lv_temp_txt := REPLACE(lv_temp_txt,'[LOSSTYPE]',v_loss_type);
--              lv_temp_txt := REPLACE(lv_temp_txt,'[RISKDESC]',v_item_desc);
--              lv_temp_txt := REPLACE(lv_temp_txt,'[CLAIMANTTYPE]',v_claimant_type); 
--              lv_temp_txt := REPLACE(lv_temp_txt,'[RESERVEAMT]',v_res_amt); 
--               --lv_temp_txt := REPLACE(lv_temp_txt,'[DATEADV]',v_date); 
--             lv_temp_txt := REPLACE(lv_temp_txt,'[TODAY]',to_char(TRUNC(SYSDATE),'ddth') ||' day of '||trim(to_char(TRUNC(SYSDATE),'Month'))||' '||to_char(TRUNC(SYSDATE),'RRRR'));
--             
--             
--             lv_temp_txt := REPLACE(lv_temp_txt,'[ORIGINALESTIMATE]',TO_CHAR(NVL(v_original_estimate,0),'FM999,999,999.00')); 
--             lv_temp_txt := REPLACE(lv_temp_txt,'[EXCESS]',TO_CHAR(NVL(v_excess_amt,0),'FM999,999,999.00')); 
--             lv_temp_txt := REPLACE(lv_temp_txt,'[EXCESSRATE]',v_excess_rate);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[DEPAMT]',TO_CHAR(NVL(v_dep_amt,0),'FM999,999,999.00'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[DEPRATE]',v_dep_rate);
--             lv_temp_txt := REPLACE(lv_temp_txt,'[PAYMENTDUE]',TO_CHAR(NVL(v_res_amt,0),'FM999,999,999.00'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[PAYMENTDUETOT]', to_char((NVL(v_sa,0)-(v_sa*50/100)-(v_excess_amt)),'FM999,999,999.00'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[REVISEDAMT]',TO_CHAR(NVL(v_res_amt+NVL(v_dep_amt,0)+NVL(v_excess_amt,0),0),'FM999,999,999.00'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[REVISEDAMTAVG]',TO_CHAR(NVL(2*(v_res_amt+NVL(v_dep_amt,0)+NVL(v_excess_amt,0))-NVL(v_original_estimate,0),0),'FM999,999,999.00'));
--             lv_temp_txt := REPLACE(lv_temp_txt,'[AMTINWORDS]',FMS_CHEQUES_PKG.num_to_words(NVL(v_res_amt,0),NVL(v_cur_code,17)));
--             
--        EXCEPTION
--             WHEN NO_DATA_FOUND THEN NULL;
--             WHEN OTHERS THEN
--                RAISE_APPLICATION_ERROR(-20001,'ERROR: '||SQLERRM(SQLCODE));
--        END;

--    ELSIF v_app_lvl = 'L' THEN
--        NULL;
--    
--    ELSIF v_app_lvl = 'Q' THEN
--        IF v_pol_batch_no IS NOT NULL THEN
--            BEGIN
--              
--                SELECT QUOT_NO,QUOT_TOT_PROPERTY_VAL,GIS_UTILITIES.CLNT_NAME(CLNT_NAME,CLNT_OTHER_NAMES) CLNT_NAME,QUOT_PREMIUM,
--                QUOT_COVER_FROM,QUOT_COVER_TO,QUOT_DATE,AGN_NAME,
--                BRN_NAME,CLNT_POSTAL_ADDRS,TWN_NAME,QR_PROPERTY_ID,QR_ITEM_DESC,QR_WEF,QR_WET,QR_COVT_SHT_DESC,QR_PREMIUM,QR_VALUE, QUOT_CUR_SYMBOL             
--                INTO v_pol_no,v_sa,v_client,v_prem,vcoverfrom, vcoverto,veffdate,v_agent,
--                v_branch,v_address,v_twn,v_risk_id,v_risk_desc,v_risk_wef,v_risk_wet,v_cover,v_risk_prem,v_risk_value, v_currency
--                FROM GIN_QUOTATIONS ,TQC_CLIENTS,TQC_AGENCIES,TQC_BRANCHES,TQC_TOWNS, TQ_GIS.GIN_QUOT_RISKS
--                WHERE QUOT_PRP_CODE = CLNT_CODE
--                AND QR_QUOT_CODE = QUOT_CODE
--                AND QUOT_AGNT_AGENT_CODE = AGN_CODE
--                AND TWN_CODE(+) = CLNT_TWN_CODE
--                AND BRN_CODE = QUOT_BRN_CODE
--                AND QUOT_CODE = v_pol_batch_no
--                AND ROWNUM <2;
--             
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[QUOTNO]',v_pol_no);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[COVERFROM]',TO_CHAR(vcoverfrom,'DD/Mon/YYYY'));
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTO]',TO_CHAR(vcoverto,'DD/Mon/YYYY'));
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[EFFDATE]',TO_CHAR(veffdate,'DD/Mon/YYYY'));
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[SA]',v_sa);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[PREMIUM]',v_prem);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[CURRENCY]',v_currency);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[AGENT]',v_agent);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[BRANCH]',v_branch);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[ADDRESS]',v_address);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[TOWN]',v_twn);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKID]',v_risk_id);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKDESC]',v_risk_desc);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWEF]',v_risk_wef);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKWET]',v_risk_wet);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[COVERTYPE]',v_cover);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKPREM]',v_risk_prem);
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[RISKVALUE]',v_risk_value);
--             
--            EXCEPTION
--                 WHEN NO_DATA_FOUND THEN NULL;
--                 WHEN OTHERS THEN
--                    RAISE_APPLICATION_ERROR(-20001,'ERROR: '||SQLERRM(SQLCODE));
--            END;
--        ELSE
--            IF v_pol_no IS NOT NULL THEN
--                 lv_temp_txt := REPLACE(lv_temp_txt,'[QUOTNO]',v_pol_no);
--            END IF;
--        END IF;
--    ELSE
--         RAISE_APPLICATION_ERROR(-20001,'Application level '||NVL(v_app_lvl,'NONE')||' not recognised..');
--    END IF;
--    RETURN(lv_temp_txt);
--END PROCESS_GIS_POL_MEMO;
FUNCTION PROCESS_GIS_POL_MEMOBK130514(v_pol_batch_no IN NUMBER,
                                v_claim_no     IN VARCHAR2,
                                v_gcc_code     IN NUMBER,
                                v_raw_txt      IN VARCHAR2,
                                v_app_lvl      IN VARCHAR2) RETURN VARCHAR2 IS
  
    lv_temp_txt     VARCHAR2(10000);
    v_pol_no        GIN_POLICIES.POL_POLICY_NO%TYPE;
    v_clm_no        GIN_CLAIM_MASTER_BOOKINGS.CMB_CLAIM_NO%TYPE;
    v_cmb_ins_claim_no        GIN_CLAIM_MASTER_BOOKINGS.CMB_INS_CLAIM_NO%TYPE;
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
    v_agn_code      NUMBER;
    v_agn_name      TQC_AGENCIES.AGN_NAME%TYPE;
    v_agn_add       TQC_AGENCIES.AGN_PHYSICAL_ADDRESS%TYPE;
    v_client_code   NUMBER;
    v_client_add    VARCHAR2(100);
    v_insured_name  VARCHAR2(300);
    v_insured_add   VARCHAR2(100);
    v_org_name      VARCHAR2(100);
    v_org_add       VARCHAR2(100);
    v_ipu_desc      GIN_INSURED_PROPERTY_UNDS.IPU_ITEM_DESC%TYPE;
    v_amt_words     VARCHAR2(50);
    v_cur_code      NUMBER;
    v_period        NUMBER;
  
    v_obligor           GIN_BONDS.BON_OBLIGOR%TYPE;
    v_ob_address        GIN_BONDS.OBLIGOR_ADDRS%TYPE;
    v_ob_town           GIN_BONDS.BON_OBLIGOR_TOWN%TYPE;
    v_ob_country        GIN_BONDS.BON_OBLIGOR_COUNTRY%TYPE;
    v_ob_emp            GIN_BONDS.BON_EMPLOYER%TYPE;
    v_res_amt           GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
    v_original_estimate GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
    v_dep_amt           GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
    v_excess_amt        GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
    v_ipu_code          GIN_INSURED_PROPERTY_UNDS.IPU_CODE%TYPE;
    v_clnt_tel          TQC_CLIENTS.CLNT_TEL%TYPE;
    v_peril             GIN_CLAIM_PERILS.CLMP_PER_PT_SHT_DESC%TYPE;
    v_excess_rate       NUMBER;
    v_dep_rate          NUMBER;
    v_agn_tel           TQC_AGENCIES.AGN_TEL1%TYPE;
    v_ipu_terr_desc       GIN_INSURED_PROPERTY_UNDS.IPU_TERR_DESC%TYPE;
    v_pip_code   NUMBER;
  BEGIN
    lv_temp_txt := v_raw_txt;
 --RAISE_ERROR(v_app_lvl);
  
    IF v_app_lvl = 'R' THEN
    
      IF v_pol_batch_no IS NOT NULL THEN
        BEGIN
        
          SELECT POL_POLICY_NO,
                 POL_TOTAL_SUM_INSURED,
                 GIS_UTILITIES.CLNT_NAME(CLNT_NAME, CLNT_OTHER_NAMES) CLNT_NAME,
                 POL_TOTAL_FAP,
                 POL_POLICY_COVER_FROM,
                 POL_POLICY_COVER_TO,
                 POL_WEF_DT,
                 POL_RENEWAL_DT,
                 AGN_NAME,
                  GIS_UTILITIES.other_int_parties(POL_OTH_INT_PARTIES) ,
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
                 IPU_VALUE,
                 ipu_terr_desc
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
                 v_risk_value,
                 v_ipu_terr_desc
            FROM GIN_REN_POLICIES,
                 TQC_CLIENTS,
                 TQC_AGENCIES,
                 TQC_BRANCHES,
                 TQC_TOWNS,
                 GIN_REN_INSURED_PROPERTY_UNDS
           WHERE POL_PRP_CODE = CLNT_CODE
             AND POL_AGNT_AGENT_CODE = AGN_CODE
             AND TWN_CODE(+) = CLNT_TWN_CODE
             AND BRN_CODE = POL_BRN_CODE
             AND IPU_POL_BATCH_NO = POL_BATCH_NO
             AND pol_batch_no = v_pol_batch_no
             AND ROWNUM < 2;
             
             
         
        
          lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[COVERFROM]',
                                 TO_CHAR(vcoverfrom, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[COVERTO]',
                                 TO_CHAR(vcoverto, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[EFFDATE]',
                                 TO_CHAR(veffdate, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[RENEWALDATE]',
                                 TO_CHAR(vrendate, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt, '[SA]', v_sa);
          lv_temp_txt := REPLACE(lv_temp_txt, '[CLIENT]', v_client);
          lv_temp_txt := REPLACE(lv_temp_txt, '[PREMIUM]', v_prem);
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
          lv_temp_txt := REPLACE(lv_temp_txt, '[TERRITORY]', v_ipu_terr_desc);
        
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            NULL;
          WHEN OTHERS THEN
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: ' || SQLERRM(SQLCODE));
        END;
      ELSE
        IF v_pol_no IS NOT NULL THEN
          lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
        END IF;
      END IF;
    
    ELSIF v_app_lvl = 'U' THEN
      IF v_pol_batch_no IS NOT NULL THEN
        BEGIN
          --RAISE_ERROR('BATCH NO '||v_pol_batch_no); 
          SELECT POL_POLICY_NO,
                 POL_SI_DIFF,
                 GIS_UTILITIES.CLNT_NAME(CLNT_NAME, CLNT_OTHER_NAMES) CLNT_NAME,
                 POL_TOT_ENDOS_DIFF_AMT,
                 POL_POLICY_COVER_FROM,
                 POL_POLICY_COVER_TO,
                 POL_WEF_DT,
                 POL_RENEWAL_DT,
                 AGN_NAME,
                 GIS_UTILITIES.other_int_parties(POL_OTH_INT_PARTIES),
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
                 IPU_VALUE,
                 ipu_terr_desc
            INTO v_pol_no,
                 v_sa,
                 v_client,
                 v_prem,
                 vcoverfrom,
                 vcoverto,
                 veffdate,
                 vrendate,
                 v_agent,
                 v_pip_code,
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
                 v_risk_value,
                 v_ipu_terr_desc
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
          
                    SELECT pip_name 
             INTO v_int_parties
             FROM gin_pol_interest_parties
             where pip_code=v_pip_code;
        
        
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
           lv_temp_txt := REPLACE(lv_temp_txt, '[TERRITORY]', v_ipu_terr_desc);
          --RAISE_ERROR('HERE');
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
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: ' || SQLERRM(SQLCODE));
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
                    GIS_UTILITIES.other_int_parties(POL_OTH_INT_PARTIES)
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
                             trim(to_char(vcoverfrom, 'Month')) || ' ' ||
                             to_char(vcoverfrom, 'RRRR'));
    ELSIF v_app_lvl = 'C' THEN
    
      BEGIN
        SELECT DISTINCT IPU_CODE,
                        POL_POLICY_NO,
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
                        DECODE(REG_THIRD_PARTY, 'S', 'SELF', 'THIRD PARTY') CLAIMANT_TYPE,
                        GET_OS_RESERVE(v_claim_no),CMB_INS_CLAIM_NO
          INTO v_ipu_code,
               v_pol_no,
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
               v_claimant_type,
               v_res_amt,
               v_cmb_ins_claim_no
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
           AND CMB_CLAIM_NO = v_claim_no
           AND ROWNUM < 2;
      
        BEGIN
          SELECT NVL(CLMREV_AMT, 0)
            INTO v_original_estimate
            FROM GIN_CLAIM_REVISIONS
           WHERE CLMREV_CMB_CLAIM_NO = v_claim_no
             AND CLMREV_GGT_TRAN_TYPE = 'LOP';
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_original_estimate := 0;
          WHEN OTHERS THEN
            RAISE_ERROR('Unable to search Original Estimate' ||
                        SQLERRM(SQLCODE));
        END;
      
        BEGIN
          --                SELECT  NVL(CLMP_EXCESS_AMT,0),NVL((NVL(CLMP_DEPRPRD_RATE,0)/100)*v_sa,0),NVL(CLMP_DEPRPRD_RATE,0)
          --                INTO  v_excess_amt,v_dep_amt ,v_dep_rate
          --                FROM GIN_CLAIM_PERILS
          --                WHERE CLMP_CMB_CLAIM_NO = v_claim_no;
          NULL;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_dep_amt    := 0;
            v_excess_amt := 0;
          WHEN OTHERS THEN
            RAISE_ERROR('Unable to search Depreciation and Excess Amounts' ||
                        SQLERRM(SQLCODE));
        END;
      
        BEGIN
          SELECT NVL(PRSPR_EXCESS, 0)
            INTO v_excess_rate
            FROM GIN_POL_RISK_SECTION_PERILS
           WHERE PRSPR_IPU_CODE = v_ipu_code
             AND NVL(PRSPR_EXCESS_TYPE, 'A') = 'P';
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_excess_rate := 0;
          WHEN OTHERS THEN
            RAISE_ERROR('Unable to select excess rate' || SQLERRM(SQLCODE));
        END;
      
        IF v_gcc_code IS NOT NULL THEN
          SELECT GCC_COURT_NAME, GCC_COURT_DATE, GCC_CASE_NO
            INTO v_courtname, v_courtdate, v_caseno
            FROM GIN_COURT_CASES
           WHERE GCC_CMB_CLAIM_NO = v_claim_no
             AND GCC_CODE = v_gcc_code;
        END IF;
      
        lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
        --MRAISE_ERROR(lv_temp_txt||';'||v_pol_no);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CLAIMNO]', v_clm_no);
        lv_temp_txt := REPLACE(lv_temp_txt, '[SA]', v_sa);
        lv_temp_txt := REPLACE(lv_temp_txt, '[20SA]', (v_sa * 20 / 100));
        lv_temp_txt := REPLACE(lv_temp_txt, '[30SA]', (v_sa * 30 / 100));
        lv_temp_txt := REPLACE(lv_temp_txt, '[50SA]', (v_sa * 50 / 100));
        lv_temp_txt := REPLACE(lv_temp_txt, '[70SA]', (v_sa * 70 / 100));
        lv_temp_txt := REPLACE(lv_temp_txt, '[CLAIMANTNAME]', v_client);
        lv_temp_txt := REPLACE(lv_temp_txt, '[PREMIUM]', v_prem);
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[DATE]',
                               to_char(TRUNC(v_date), 'ddth') || ' day of ' ||
                               trim(to_char(TRUNC(v_date), 'Month')) || ' ' ||
                               to_char(TRUNC(v_date), 'RRRR'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[DATEADV]',
                               to_char(TRUNC(v_date), 'ddth') || ' day of ' ||
                               trim(to_char(TRUNC(v_date), 'Month')) || ' ' ||
                               to_char(TRUNC(v_date), 'RRRR'));
      
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[LOSSDATE]',
                               to_char(TRUNC(v_time), 'ddth') || ' '||
                               trim(to_char(TRUNC(v_time), 'Month')) || ' ' ||
                               to_char(TRUNC(v_time), 'RRRR'));
        lv_temp_txt := REPLACE(lv_temp_txt, '[REGISTRATION]', v_reg);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOCATION]', v_loc);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CASENO]', v_caseno);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CASEDATE]', v_courtdate);
        lv_temp_txt := REPLACE(lv_temp_txt, '[COURT]', v_courtname);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOSSDESC]', v_desc);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOSSTYPE]', v_loss_type);
        lv_temp_txt := REPLACE(lv_temp_txt, '[RISKDESC]', v_item_desc);
         lv_temp_txt := REPLACE(lv_temp_txt, '[INSCLAIMNO]', v_cmb_ins_claim_no);
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[CLAIMANTTYPE]',
                               v_claimant_type);
        lv_temp_txt := REPLACE(lv_temp_txt, '[RESERVEAMT]', v_res_amt);
        --lv_temp_txt := REPLACE(lv_temp_txt,'[DATEADV]',v_date); 
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[TODAY]',
                               to_char(TRUNC(SYSDATE), 'ddth') || ' day of ' ||
                               trim(to_char(TRUNC(SYSDATE), 'Month')) || ' ' ||
                               to_char(TRUNC(SYSDATE), 'RRRR'));
      
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[ORIGINALESTIMATE]',
                               TO_CHAR(NVL(v_original_estimate, 0),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[EXCESS]',
                               TO_CHAR(NVL(v_excess_amt, 0),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt, '[EXCESSRATE]', v_excess_rate);
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[DEPAMT]',
                               TO_CHAR(NVL(v_dep_amt, 0), 'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt, '[DEPRATE]', v_dep_rate);
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[PAYMENTDUE]',
                               TO_CHAR(NVL(v_res_amt, 0), 'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[PAYMENTDUETOT]',
                               to_char((NVL(v_sa, 0) - (v_sa * 50 / 100) -
                                       (v_excess_amt)),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[REVISEDAMT]',
                               TO_CHAR(NVL(v_res_amt + NVL(v_dep_amt, 0) +
                                           NVL(v_excess_amt, 0),
                                           0),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[REVISEDAMTAVG]',
                               TO_CHAR(NVL(2 *
                                           (v_res_amt + NVL(v_dep_amt, 0) +
                                           NVL(v_excess_amt, 0)) -
                                           NVL(v_original_estimate, 0),
                                           0),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[AMTINWORDS]',
                               FMS_CHEQUES_PKG.num_to_words(NVL(v_res_amt, 0),
                                                            NVL(v_cur_code,
                                                                17)));
      
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
        WHEN OTHERS THEN
          RAISE_APPLICATION_ERROR(-20001, 'ERROR: ' || SQLERRM(SQLCODE));
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
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: ' || SQLERRM(SQLCODE));
        END;
      ELSE
        IF v_pol_no IS NOT NULL THEN
          lv_temp_txt := REPLACE(lv_temp_txt, '[QUOTNO]', v_pol_no);
        END IF;
      END IF;
    ELSE
      RAISE_APPLICATION_ERROR(-20001,
                              'Application level ' ||
                              NVL(v_app_lvl, 'NONE') || ' not recognised..');
    END IF;
    RETURN(lv_temp_txt);
  END PROCESS_GIS_POL_MEMOBK130514;
  FUNCTION PROCESS_GIS_POL_MEMO(v_pol_batch_no IN NUMBER,
                                v_claim_no     IN VARCHAR2,
                                v_gcc_code     IN NUMBER,
                                v_raw_txt      IN VARCHAR2,
                                v_app_lvl      IN VARCHAR2) RETURN VARCHAR2 IS
  
    lv_temp_txt     VARCHAR2(10000);
    v_pol_no        GIN_POLICIES.POL_POLICY_NO%TYPE;
    v_clm_no        GIN_CLAIM_MASTER_BOOKINGS.CMB_CLAIM_NO%TYPE;
    v_cmb_ins_claim_no        GIN_CLAIM_MASTER_BOOKINGS.CMB_INS_CLAIM_NO%TYPE;
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
    v_agn_code      NUMBER;
    v_agn_name      TQC_AGENCIES.AGN_NAME%TYPE;
    v_agn_add       TQC_AGENCIES.AGN_PHYSICAL_ADDRESS%TYPE;
    v_client_code   NUMBER;
    v_client_add    VARCHAR2(100);
    v_insured_name  VARCHAR2(300);
    v_insured_add   VARCHAR2(100);
    v_org_name      VARCHAR2(100);
    v_org_add       VARCHAR2(100);
    v_ipu_desc      GIN_INSURED_PROPERTY_UNDS.IPU_ITEM_DESC%TYPE;
    v_amt_words     VARCHAR2(50);
    v_cur_code      NUMBER;
    v_period        NUMBER;
  
    v_obligor           GIN_BONDS.BON_OBLIGOR%TYPE;
    v_ob_address        GIN_BONDS.OBLIGOR_ADDRS%TYPE;
    v_ob_town           GIN_BONDS.BON_OBLIGOR_TOWN%TYPE;
    v_ob_country        GIN_BONDS.BON_OBLIGOR_COUNTRY%TYPE;
    v_ob_emp            GIN_BONDS.BON_EMPLOYER%TYPE;
    v_res_amt           GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
    v_original_estimate GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
    v_dep_amt           GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
    v_excess_amt        GIN_CLAIM_REVISIONS.CLMREV_AMT%TYPE;
    v_ipu_code          GIN_INSURED_PROPERTY_UNDS.IPU_CODE%TYPE;
    v_clnt_tel          TQC_CLIENTS.CLNT_TEL%TYPE;
    v_peril             GIN_CLAIM_PERILS.CLMP_PER_PT_SHT_DESC%TYPE;
    v_excess_rate       NUMBER;
    v_dep_rate          NUMBER;
    v_agn_tel           TQC_AGENCIES.AGN_TEL1%TYPE;
    v_ipu_terr_desc       GIN_INSURED_PROPERTY_UNDS.IPU_TERR_DESC%TYPE;
    v_pip_code   NUMBER;
    v_reasons_rejected   gin_claim_master_bookings.CMB_REASONS_REJECTED%type;
    v_property_id GIN_INSured_property_unds.IPU_PROPERTY_ID%type;
    v_moto_verfy   gin_products.pro_moto_verfy%type;
   
    V_MAKE  gin_motor_private_sch.MPS_MAKE%TYPE;
    v_ipu_terr_descm VARCHAR2(50);
    V_spr_name  TQC_SERVICE_PROVIDERS.SPR_NAME%TYPE;
    v_cpv_tran_type VARCHAR2(5);
    v_rrc_amount NUMBER;
  BEGIN
    lv_temp_txt := v_raw_txt;
 --RAISE_ERROR(v_app_lvl);
  
    IF v_app_lvl = 'R' THEN
    
      IF v_pol_batch_no IS NOT NULL THEN
        BEGIN
        
          SELECT POL_POLICY_NO,
                 POL_TOTAL_SUM_INSURED,
                 GIS_UTILITIES.CLNT_NAME(CLNT_NAME, CLNT_OTHER_NAMES) CLNT_NAME,
                 POL_TOTAL_FAP,
                 POL_POLICY_COVER_FROM,
                 POL_POLICY_COVER_TO,
                 POL_WEF_DT,
                 POL_RENEWAL_DT,
                 AGN_NAME,
                  GIS_UTILITIES.other_int_parties(POL_OTH_INT_PARTIES) ,
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
                 IPU_VALUE,
                 ipu_terr_desc
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
                 v_risk_value,
                 v_ipu_terr_desc
            FROM GIN_REN_POLICIES,
                 TQC_CLIENTS,
                 TQC_AGENCIES,
                 TQC_BRANCHES,
                 TQC_TOWNS,
                 GIN_REN_INSURED_PROPERTY_UNDS
           WHERE POL_PRP_CODE = CLNT_CODE
             AND POL_AGNT_AGENT_CODE = AGN_CODE
             AND TWN_CODE(+) = CLNT_TWN_CODE
             AND BRN_CODE = POL_BRN_CODE
             AND IPU_POL_BATCH_NO = POL_BATCH_NO
             AND pol_batch_no = v_pol_batch_no
             AND ROWNUM < 2;
             
             
         
        
          lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[COVERFROM]',
                                 TO_CHAR(vcoverfrom, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[COVERTO]',
                                 TO_CHAR(vcoverto, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[EFFDATE]',
                                 TO_CHAR(veffdate, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[RENEWALDATE]',
                                 TO_CHAR(vrendate, 'DD/Mon/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt, '[SA]', v_sa);
          lv_temp_txt := REPLACE(lv_temp_txt, '[CLIENT]', v_client);
          lv_temp_txt := REPLACE(lv_temp_txt, '[PREMIUM]', v_prem);
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
          lv_temp_txt := REPLACE(lv_temp_txt, '[TERRITORY]', v_ipu_terr_desc);
        
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            NULL;
          WHEN OTHERS THEN
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: ' || SQLERRM(SQLCODE));
        END;
      ELSE
        IF v_pol_no IS NOT NULL THEN
          lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
        END IF;
      END IF;
    
    ELSIF v_app_lvl = 'U' THEN
      IF v_pol_batch_no IS NOT NULL THEN
        BEGIN
          --RAISE_ERROR('BATCH NO '||v_pol_batch_no); 
          SELECT POL_POLICY_NO,
                 POL_SI_DIFF,
                 GIS_UTILITIES.CLNT_NAME(CLNT_NAME, CLNT_OTHER_NAMES) CLNT_NAME,
                 POL_TOT_ENDOS_DIFF_AMT,
                 POL_POLICY_COVER_FROM,
                 POL_POLICY_COVER_TO,
                 POL_WEF_DT,
                 POL_RENEWAL_DT,
                 AGN_NAME,
                 GIS_UTILITIES.other_int_parties(POL_OTH_INT_PARTIES),
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
                 IPU_VALUE,
                 ipu_terr_desc,
                 PRO_MOTO_VERFY,
                 IPU_CODE
            INTO v_pol_no,
                 v_sa,
                 v_client,
                 v_prem,
                 vcoverfrom,
                 vcoverto,
                 veffdate,
                 vrendate,
                 v_agent,
                 v_pip_code,
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
                 v_risk_value,
                 v_ipu_terr_descm,
                 v_moto_verfy,
                 v_ipu_code
            FROM GIN_POLICIES,
            gin_products,
                 TQC_CLIENTS,
                 TQC_AGENCIES,
                 TQC_BRANCHES,
                 TQC_TOWNS,
                 TQ_GIS.GIN_INSURED_PROPERTY_UNDS
           WHERE POL_PRP_CODE = CLNT_CODE
             AND POL_AGNT_AGENT_CODE = AGN_CODE
             AND POL_PRO_CODE=PRO_CODE
             AND TWN_CODE(+) = CLNT_TWN_CODE
             AND BRN_CODE = POL_BRN_CODE
             AND IPU_POL_BATCH_NO = POL_BATCH_NO
             AND pol_batch_no = v_pol_batch_no
             AND ROWNUM < 2;
          
          
          
             SELECT pip_name 
             INTO v_int_parties
             FROM gin_pol_interest_parties
             where pip_code=v_pip_code;
             
          IF NVL(v_moto_verfy,'N')='Y'  THEN
          SELECT mps_make
          INTO v_make
          FROM gin_motor_private_sch
          WHERE mps_ipu_code=v_ipu_code;
            IF v_make IS NULL THEN
              SELECT mcoms_make
              INTO v_make
              FROM  gin_motor_commercial_sch
              WHERE mcoms_ipu_code=v_ipu_code;
           END IF;          
          END IF;   
        
        
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
           lv_temp_txt := REPLACE(lv_temp_txt, '[TERRITORY]', v_ipu_terr_desc);
          --RAISE_ERROR('HERE');
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[WEF]',
                                 TO_CHAR(veffdate, 'DD/MM/YYYY'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                                 '[WET]',
                                 TO_CHAR(vcoverto, 'DD/MM/YYYY'));
               lv_temp_txt := REPLACE(lv_temp_txt, '[MAKE]', v_make);                        
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            NULL;
          WHEN OTHERS THEN
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: ' || SQLERRM(SQLCODE));
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
                    GIS_UTILITIES.other_int_parties(POL_OTH_INT_PARTIES)
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
                             trim(to_char(vcoverfrom, 'Month')) || ' ' ||
                             to_char(vcoverfrom, 'RRRR'));
    ELSIF v_app_lvl = 'C' THEN
    
      BEGIN
        SELECT DISTINCT IPU_CODE,
                        POL_POLICY_NO,
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
                        DECODE(REG_THIRD_PARTY, 'S', 'SELF', 'THIRD PARTY') CLAIMANT_TYPE,
                        GET_OS_RESERVE(v_claim_no),CMB_INS_CLAIM_NO,CMB_REASONS_REJECTED,
                        CMB_IPU_PROPERTY_ID
          INTO v_ipu_code,
               v_pol_no,
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
               v_claimant_type,
               v_res_amt,
               v_cmb_ins_claim_no,
               v_reasons_rejected,
               v_property_id
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
           AND CMB_CLAIM_NO = v_claim_no
           AND ROWNUM < 2;
      
        BEGIN
          SELECT NVL(CLMREV_AMT, 0)
            INTO v_original_estimate
            FROM GIN_CLAIM_REVISIONS
           WHERE CLMREV_CMB_CLAIM_NO = v_claim_no
             AND CLMREV_GGT_TRAN_TYPE = 'LOP';
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_original_estimate := 0;
          WHEN OTHERS THEN
            RAISE_ERROR('Unable to search Original Estimate' ||
                        SQLERRM(SQLCODE));
        END;
      
        BEGIN
          --                SELECT  NVL(CLMP_EXCESS_AMT,0),NVL((NVL(CLMP_DEPRPRD_RATE,0)/100)*v_sa,0),NVL(CLMP_DEPRPRD_RATE,0)
          --                INTO  v_excess_amt,v_dep_amt ,v_dep_rate
          --                FROM GIN_CLAIM_PERILS
          --                WHERE CLMP_CMB_CLAIM_NO = v_claim_no;
          NULL;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_dep_amt    := 0;
            v_excess_amt := 0;
          WHEN OTHERS THEN
            RAISE_ERROR('Unable to search Depreciation and Excess Amounts' ||
                        SQLERRM(SQLCODE));
        END;
        
         BEGIN
          SELECT cpv_tran_type
            INTO v_cpv_tran_type
            FROM gin_clm_payment_vouchers
           WHERE cpv_cmb_claim_no=v_claim_no;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_cpv_tran_type:= 'CFO';
          WHEN OTHERS THEN
            RAISE_ERROR('Unable to select excess rate' || SQLERRM(SQLCODE));
        END;
        
        BEGIN
           SELECT rrc_amount
            INTO v_rrc_amount
            FROM gin_recovery_rcts
           WHERE rrc_claim_no=v_claim_no;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_rrc_amount:= 0;
          WHEN OTHERS THEN
            RAISE_ERROR('Unable to Recovery' || SQLERRM(SQLCODE));
        END;
        
        IF v_cpv_tran_type ='CFO'  THEN
            SELECT spr_name
            INTO V_spr_name
            FROM gin_appnt_correspondents,tqc_service_providers,tqc_service_provider_types
            WHERE apco_cor_code=spr_code
            AND spr_spt_code=spt_code;
        END IF;
      
        BEGIN
          SELECT NVL(PRSPR_EXCESS, 0)
            INTO v_excess_rate
            FROM GIN_POL_RISK_SECTION_PERILS
           WHERE PRSPR_IPU_CODE = v_ipu_code
             AND NVL(PRSPR_EXCESS_TYPE, 'A') = 'P';
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_excess_rate := 0;
          WHEN OTHERS THEN
            RAISE_ERROR('Unable to select excess rate' || SQLERRM(SQLCODE));
        END;
      
        IF v_gcc_code IS NOT NULL THEN
          SELECT GCC_COURT_NAME, GCC_COURT_DATE, GCC_CASE_NO
            INTO v_courtname, v_courtdate, v_caseno
            FROM GIN_COURT_CASES
           WHERE GCC_CMB_CLAIM_NO = v_claim_no
             AND GCC_CODE = v_gcc_code;
        END IF;
      
        lv_temp_txt := REPLACE(lv_temp_txt, '[POLICYNO]', v_pol_no);
        --MRAISE_ERROR(lv_temp_txt||';'||v_pol_no);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CLAIMNO]', v_clm_no);
        lv_temp_txt := REPLACE(lv_temp_txt, '[SA]', v_sa);
        lv_temp_txt := REPLACE(lv_temp_txt, '[20SA]', (v_sa * 20 / 100));
        lv_temp_txt := REPLACE(lv_temp_txt, '[30SA]', (v_sa * 30 / 100));
        lv_temp_txt := REPLACE(lv_temp_txt, '[50SA]', (v_sa * 50 / 100));
        lv_temp_txt := REPLACE(lv_temp_txt, '[70SA]', (v_sa * 70 / 100));
        lv_temp_txt := REPLACE(lv_temp_txt, '[CLAIMANTNAME]', v_client);
        lv_temp_txt := REPLACE(lv_temp_txt, '[PREMIUM]', v_prem);
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[DATE]',
                               to_char(TRUNC(v_date), 'ddth') || ' day of ' ||
                               trim(to_char(TRUNC(v_date), 'Month')) || ' ' ||
                               to_char(TRUNC(v_date), 'RRRR'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[DATEADV]',
                               to_char(TRUNC(v_date), 'ddth') || ' day of ' ||
                               trim(to_char(TRUNC(v_date), 'Month')) || ' ' ||
                               to_char(TRUNC(v_date), 'RRRR'));
      
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[LOSSDATE]',
                               to_char(TRUNC(v_time), 'ddth') || ' '||
                               trim(to_char(TRUNC(v_time), 'Month')) || ' ' ||
                               to_char(TRUNC(v_time), 'RRRR'));
        lv_temp_txt := REPLACE(lv_temp_txt, '[REGISTRATION]', v_reg);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOCATION]', v_loc);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CASENO]', v_caseno);
        lv_temp_txt := REPLACE(lv_temp_txt, '[CASEDATE]', v_courtdate);
        lv_temp_txt := REPLACE(lv_temp_txt, '[COURT]', v_courtname);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOSSDESC]', v_desc);
        lv_temp_txt := REPLACE(lv_temp_txt, '[LOSSTYPE]', v_loss_type);
        lv_temp_txt := REPLACE(lv_temp_txt, '[RISKDESC]', v_item_desc);
         lv_temp_txt := REPLACE(lv_temp_txt, '[INSCLAIMNO]', v_cmb_ins_claim_no);
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[CLAIMANTTYPE]',
                               v_claimant_type);
        lv_temp_txt := REPLACE(lv_temp_txt, '[RESERVEAMT]', v_res_amt);
        --lv_temp_txt := REPLACE(lv_temp_txt,'[DATEADV]',v_date); 
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[TODAY]',
                               to_char(TRUNC(SYSDATE), 'ddth') || ' day of ' ||
                               trim(to_char(TRUNC(SYSDATE), 'Month')) || ' ' ||
                               to_char(TRUNC(SYSDATE), 'RRRR'));
      
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[ORIGINALESTIMATE]',
                               TO_CHAR(NVL(v_original_estimate, 0),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[EXCESS]',
                               TO_CHAR(NVL(v_excess_amt, 0),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt, '[EXCESSRATE]', v_excess_rate);
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[DEPAMT]',
                               TO_CHAR(NVL(v_dep_amt, 0), 'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt, '[DEPRATE]', v_dep_rate);
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[PAYMENTDUE]',
                               TO_CHAR(NVL(v_res_amt, 0), 'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[PAYMENTDUETOT]',
                               to_char((NVL(v_sa, 0) - (v_sa * 50 / 100) -
                                       (v_excess_amt)),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[REVISEDAMT]',
                               TO_CHAR(NVL(v_res_amt + NVL(v_dep_amt, 0) +
                                           NVL(v_excess_amt, 0),
                                           0),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[REVISEDAMTAVG]',
                               TO_CHAR(NVL(2 *
                                           (v_res_amt + NVL(v_dep_amt, 0) +
                                           NVL(v_excess_amt, 0)) -
                                           NVL(v_original_estimate, 0),
                                           0),
                                       'FM999,999,999.00'));
        lv_temp_txt := REPLACE(lv_temp_txt,
                               '[AMTINWORDS]',
                               FMS_CHEQUES_PKG.num_to_words(NVL(v_res_amt, 0),
                                                            NVL(v_cur_code,
                                                                17)));
        lv_temp_txt := REPLACE(lv_temp_txt, '[REASON FOR REPUDIATION]', v_reasons_rejected);
         lv_temp_txt := REPLACE(lv_temp_txt, '[DOCTOR]', v_spr_name);
        lv_temp_txt := REPLACE(lv_temp_txt, '[VEHICLE REG-NO]', v_property_id); 
             lv_temp_txt := REPLACE(lv_temp_txt,
                               '[DATE OF ACCIDENT]',
                               to_char(TRUNC(v_date), 'ddth') || ' day of ' ||
                               trim(to_char(TRUNC(v_date), 'Month')) || ' ' ||
                               to_char(TRUNC(v_date), 'RRRR'));
         lv_temp_txt := REPLACE(lv_temp_txt,
                               '[STOLEN DATE]',
                               to_char(TRUNC(v_date), 'ddth') || ' day of ' ||
                               trim(to_char(TRUNC(v_date), 'Month')) || ' ' ||
                               to_char(TRUNC(v_date), 'RRRR'));
          lv_temp_txt := REPLACE(lv_temp_txt,
                               '[TIME OF ACCIDENT]',
                               to_char(TRUNC(v_time), 'ddth') || ' '||
                               trim(to_char(TRUNC(v_time), 'Month')) || ' ' ||
                               to_char(TRUNC(v_time), 'RRRR'));  
            lv_temp_txt := REPLACE(lv_temp_txt, '[VALUE-SH.]', v_rrc_amount);                                       
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
        WHEN OTHERS THEN
          RAISE_APPLICATION_ERROR(-20001, 'ERROR: ' || SQLERRM(SQLCODE));
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
            RAISE_APPLICATION_ERROR(-20001, 'ERROR: ' || SQLERRM(SQLCODE));
        END;
      ELSE
        IF v_pol_no IS NOT NULL THEN
          lv_temp_txt := REPLACE(lv_temp_txt, '[QUOTNO]', v_pol_no);
        END IF;
      END IF;
    ELSE
      RAISE_APPLICATION_ERROR(-20001,
                              'Application level ' ||
                              NVL(v_app_lvl, 'NONE') || ' not recognised..');
    END IF;
    RETURN(lv_temp_txt);
  END PROCESS_GIS_POL_MEMO;
  
  FUNCTION MERGE_CLAIMS_TEXT(v_claim IN VARCHAR2, v_raw_txt IN VARCHAR2)
    RETURN VARCHAR2 IS
    v_text VARCHAR2(4000);
  
  BEGIN
    BEGIN
      v_text := Tqc_Memos_DBPkg.PROCESS_GIS_POL_MEMO(NULL,
                                                     v_claim,
                                                     NULL,
                                                     v_raw_txt,
                                                     'C');
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Failed to merge claim wordings ...');
    end;
  
    RETURN(v_text);
  END;

  FUNCTION MERGE_POLICIES_TEXT(v_pol_batch_no IN NUMBER,
                               v_raw_txt      IN VARCHAR2) RETURN VARCHAR2 IS
    v_text VARCHAR2(4000);
  BEGIN
    v_text := Tqc_Memos_DBPkg.PROCESS_GIS_POL_MEMO(v_pol_batch_no,
                                                   NULL,
                                                   NULL,
                                                   v_raw_txt,
                                                   'U');
    RETURN(v_text);
  END;

  PROCEDURE process_memo_header(v_appl_lvl     IN VARCHAR2,
                                v_claim_no     IN VARCHAR2,
                                v_pol_no       IN VARCHAR2,
                                v_memo_subject OUT VARCHAR2)
  
   IS
    v_text     VARCHAR2(4000);
    al_id      number;
    v_batch_no NUMBER;
  BEGIN
  
    IF UPPER(v_appl_lvl) = UPPER('C') THEN
      v_text := MERGE_CLAIMS_TEXT(v_claim_no, v_text);
    
      -- message('v_text is  = 2   '||   v_text);PAUSE;                                          
    ELSIF v_appl_lvl = 'U' THEN
      BEGIN
        SELECT POL_BATCH_NO
          into v_batch_no
          FROM GIN_POLICIES
         WHERE POL_POLICY_NO = v_pol_no
           AND ROWNUM < 2;
      EXCEPTION
        WHEN OTHERS THEN
          NULL;
      END;
      v_text := MERGE_POLICIES_TEXT(v_batch_no, v_text);
    
    END IF;
    v_memo_subject := v_text;
  END;
  FUNCTION create_memo(v_sys_code        IN NUMBER,
                       v_appl_lvl        IN VARCHAR2,
                       v_mtyp_code       IN NUMBER,
                       v_addressee_type  IN NUMBER,
                       v_date            IN DATE,
                       v_in_quot_code    IN NUMBER,
                       v_in_pol_code     IN NUMBER,
                       v_in_clnt_code    IN NUMBER,
                       v_in_agn_code     IN NUMBER,
                       v_in_insured_code IN NUMBER,
                       v_in_claim_no     IN VARCHAR2,
                       v_in_cld_code     IN NUMBER,
                       v_in_spr_code     IN NUMBER,
                       v_in_gcc_code     IN NUMBER,
                       v_in_case_no      IN VARCHAR2,
                       v_memo_cc_tab     IN TQC_COMPANY_MEMO_CC_TAB,
                       v_user            IN VARCHAR2) RETURN NUMBER IS
  
    v_memo_subj     TQC_MEMOS.MEMO_SUBJECT%TYPE;
    v_add_name      VARCHAR2(300);
    v_add_sht_desc  VARCHAR2(100);
    v_add_address   VARCHAR2(300);
    v_code_town     VARCHAR2(300);
    v_batch_no      GIN_POLICIES.POL_BATCH_NO%TYPE;
    v_pol_no        GIN_POLICIES.POL_POLICY_NO%TYPE;
    v_endr_no       GIN_POLICIES.POL_REN_ENDOS_NO%TYPE;
    v_brn_code      GIN_POLICIES.POL_BRN_CODE%TYPE;
    v_agn_code      GIN_POLICIES.POL_AGNT_AGENT_CODE%TYPE;
    v_clnt_code     GIN_POLICIES.POL_PRP_CODE%TYPE;
    v_pro_desc      GIN_PRODUCTS.PRO_DESC%TYPE;
    v_pip_code      GIN_POLICIES.POL_PIP_CODE%TYPE;
    v_pip_pf_code   GIN_POLICIES.POL_PIP_PF_CODE%TYPE;
    v_insured_code  NUMBER;
    V_REG_CLMT_CODE GIN_RGSTD_CLAIMANTS.REG_CLMT_CODE%TYPE;
    v_quot_no       GIN_QUOTATIONS.QUOT_NO%TYPE;
    v_comem_code    TQC_COMPANY_MEMOS.COMEM_CODE%TYPE;
    v_cc_name       TQC_COMPANY_MEMO_CC.CC_NAME%TYPE;
    v_cc_address    TQC_COMPANY_MEMO_CC.CC_ADDRESS%TYPE;
    v_cc_codetown   TQC_COMPANY_MEMO_CC.CC_CODETOWN%TYPE;
    v_cc_email_addr TQC_COMPANY_MEMO_CC.CC_EMAIL_ADDR%TYPE;
    v_memh_code     TQC_MEMO_HEADER.MEMH_CODE%TYPE;
    v_memo          VARCHAR2(4000);
    v_insured       VARCHAR2(100);
    v_memo_code     TQC_MEMOS.MEMO_CODE%TYPE;
    v_raw_txt       TQC_MEMOS.MEMO_SUBJECT%TYPE;
    v_memdet_code   TQC_MEMO_DETAILS.MEMDET_CODE%TYPE;
    v_dtl_raw_txt   TQC_MEMO_DETAILS.MEMDET_CONTENT%TYPE;
    v_dtl_memo      TQC_MEMO_DETAILS.MEMDET_CONTENT%TYPE;
    v_memdtls_code  TQC_MEMO_HEADER_DTLS.MEMDTLS_CODE%TYPE;
    v_cnt           NUMBER;
    v_email_addr    VARCHAR2(75);
    v_cc_list       TQC_COMPANY_MEMO_CC_TAB := TQC_COMPANY_MEMO_CC_TAB();
    v_dummy         VARCHAR2(100);
    X               NUMBER;
  
    CURSOR clnts_cursor(vclntcode IN NUMBER) IS
      SELECT CLNT_NAME || ' ' || CLNT_OTHER_NAMES INSURED,
             CLNT_SHT_DESC,
             CLNT_POSTAL_ADDRS || ', ' || TWN_NAME address,
             nvl2(CLNT_ZIP_CODE, CLNT_ZIP_CODE || '-', '') || TWN_NAME,
             CLNT_EMAIL_ADDRS
        FROM TQC_CLIENTS, TQC_TOWNS
       WHERE CLNT_TWN_CODE = TWN_CODE(+)
         AND CLNT_CODE = vclntcode;
    CURSOR agn_cursor(vagncode IN NUMBER) IS
      SELECT AGN_NAME,
             AGN_SHT_DESC,
             AGN_POSTAL_ADDRESS,
             nvl2(AGN_ZIP, AGN_ZIP || '-', '') || TWN_NAME CODE_TOWN,
             AGN_EMAIL_ADDRESS
        FROM TQC_AGENCIES, TQC_TOWNS
       WHERE AGN_TWN_CODE = TWN_CODE(+)
         AND AGN_CODE = vagncode;
  
    CURSOR spr_cursor(vsprcode IN NUMBER) IS
      SELECT SPR_NAME COR_NAME,
             SPR_SHT_DESC,
             SPR_POSTAL_ADDRESS,
             nvl2(SPR_ZIP, SPR_ZIP || '-', '') || ' ' || TWN_NAME CODE_TOWN,
             SPR_EMAIL
        FROM TQC_SERVICE_PROVIDERS, TQC_TOWNS
       WHERE SPR_TWN_CODE = TWN_CODE(+)
         AND SPR_CODE = vsprcode;
  
    CURSOR claimant_cursor(vclaimno IN VARCHAR2, vcldcode IN NUMBER) IS
      SELECT CLAIMANT,
             CLAIMNAT_SHT_DESC,
             CLD_ADDRESS,
             CODE_TOWN,
             REG_CLMT_CODE,
             CLD_EMAIL
        FROM (SELECT CLD_SURNAME || ' ' || CLD_OTHER_NAMES CLAIMANT,
                     NULL CLAIMNAT_SHT_DESC,
                     CLD_ADDRESS,
                     nvl2(CLD_ZIP, CLD_ZIP || '-', '') || CLD_MAIL_TWN CODE_TOWN,
                     REG_CLMT_CODE,
                     CLD_EMAIL
                FROM GIN_CLAIM_MASTER_BOOKINGS,
                     GIN_RGSTD_CLAIMANTS,
                     gin_claimants
               WHERE REG_CMB_CLAIM_NO = CMB_CLAIM_NO
                 AND REG_CMB_CLAIM_NO = vclaimno
                 AND REG_CLD_CODE = CLD_CODE(+)
                 AND REG_CLD_CODE = vcldcode
                 AND REG_THIRD_PARTY != 'T'
              UNION
              SELECT CLNT_NAME || ' ' || CLNT_OTHER_NAMES CLAIMANT,
                     CLNT_SHT_DESC CLAIMNAT_SHT_DESC,
                     CLNT_POSTAL_ADDRS CLD_ADDRESS,
                     nvl2(CLNT_ZIP_CODE, CLNT_ZIP_CODE || '-', '') ||
                     TWN_NAME CODE_TOWN,
                     REG_CLMT_CODE,
                     CLNT_EMAIL_ADDRS
                FROM GIN_CLAIM_MASTER_BOOKINGS,
                     GIN_RGSTD_CLAIMANTS,
                     TQC_CLIENTS,
                     TQC_TOWNS
               WHERE REG_CMB_CLAIM_NO = CMB_CLAIM_NO
                 AND REG_CMB_CLAIM_NO = vclaimno
                 AND REG_CLD_CODE = CLNT_CODE(+)
                 AND CLNT_TWN_CODE = TWN_CODE(+)
                 AND REG_CLD_CODE = vcldcode
                 AND REG_THIRD_PARTY != 'T');
  
    CURSOR intr_parties_cursor(vpipcode IN NUMBER) IS
      SELECT PIP_NAME,
             NULL,
             PIP_POSTAL_ADDR,
             PIP_POSTAL_CODE,
             PIP_EMAIL_ADDR
        FROM GIN_POL_INTEREST_PARTIES
       WHERE PIP_CODE = vpipcode;
  
  BEGIN
    IF v_mtyp_code IS NULL THEN
      RAISE_ERROR('Select memo type to create..');
    END IF;
    IF v_appl_lvl NOT IN ('Q', 'U', 'C', 'A', 'L', 'V', 'I') THEN
      RAISE_ERROR('Memo type ' || v_appl_lvl || ' cannot be defined..');
    END IF;
  
    v_add_name     := NULL;
    v_add_sht_desc := NULL;
    v_add_address  := NULL;
    v_code_town    := NULL;
    IF v_appl_lvl = 'U' AND NVL(v_in_pol_code, 0) = 0 THEN
      RAISE_ERROR('Policy code not provided for a quotation level memo.');
    ELSIF v_appl_lvl = 'U' THEN
      SELECT POL_BATCH_NO,
             POL_POLICY_NO,
             POL_REN_ENDOS_NO,
             POL_BRN_CODE,
             POL_AGNT_AGENT_CODE,
             POL_PRP_CODE,
             PRO_DESC,
             POL_PIP_CODE,
             POL_PIP_PF_CODE
        INTO v_batch_no,
             v_pol_no,
             v_endr_no,
             v_brn_code,
             v_agn_code,
             v_clnt_code,
             v_pro_desc,
             v_pip_code,
             v_pip_pf_code
        FROM GIN_POLICIES, GIN_PRODUCTS
       WHERE POL_PRO_CODE = PRO_CODE
         AND POL_BATCH_NO = v_in_pol_code;
      IF v_addressee_type = 1 THEN
        --AGENT
        OPEN agn_cursor(v_agn_code);
        FETCH agn_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE agn_cursor;
      ELSIF v_addressee_type = 2 THEN
        --CLAIMANT
        NULL;
      ELSIF v_addressee_type = 3 THEN
        --CLIENT
        OPEN clnts_cursor(v_clnt_code);
        FETCH clnts_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE clnts_cursor;
      ELSIF v_addressee_type = 4 THEN
        --INSURED
        OPEN clnts_cursor(v_in_insured_code);
        FETCH clnts_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE clnts_cursor;
      ELSIF v_addressee_type = 6 THEN
        -- SERVICE PROVIDERS
        NULL;
      ELSIF V_ADDRESSEE_TYPE = 7 THEN
        -- Agents/Insurers
        OPEN agn_cursor(v_agn_code);
        FETCH agn_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE agn_cursor;
      ELSIF V_ADDRESSEE_TYPE = 8 THEN
        -- Interested Parties
        OPEN intr_parties_cursor(v_pip_code);
        FETCH intr_parties_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE intr_parties_cursor;
      ELSIF V_ADDRESSEE_TYPE = 9 THEN
        --  Premium Financier
        OPEN intr_parties_cursor(v_pip_pf_code);
        FETCH intr_parties_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE intr_parties_cursor;
      END IF;
      DBMS_OUTPUT.PUT_LINE('COUNT=' || v_memo_cc_tab.COUNT);
      IF NVL(v_memo_cc_tab.COUNT, 0) > 0 THEN
        X := 0;
        FOR I IN 1 .. v_memo_cc_tab.COUNT LOOP
          DBMS_OUTPUT.PUT_LINE('I=' || I ||
                               ' v_memo_cc_tab(I).CC_ADDRESSEE_TYPE=' ||
                               v_memo_cc_tab(I).CC_ADDRESSEE_TYPE);
          v_cc_address    := NULL;
          v_cc_codetown   := NULL;
          v_cc_email_addr := NULL;
          IF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 1 THEN
            --AGENT
            OPEN agn_cursor(v_agn_code);
            FETCH agn_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 2 THEN
            --CLAIMANT
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 3 THEN
            --CLIENT
            DBMS_OUTPUT.PUT_LINE('3=v_clnt_code' || v_clnt_code);
            OPEN clnts_cursor(v_clnt_code);
            FETCH clnts_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE clnts_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 4 THEN
            --INSURED
            DBMS_OUTPUT.PUT_LINE('4=v_clnt_code' || v_memo_cc_tab(I)
                                 .CC_ADDRESSEE_CODE);
            OPEN clnts_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH clnts_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE clnts_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 6 THEN
            -- SERVICE PROVIDERS
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 7 THEN
            -- Agents/Insurers
            OPEN agn_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH agn_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 8 THEN
            -- Interested Parties
            OPEN intr_parties_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH intr_parties_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE intr_parties_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 9 THEN
            --  Premium Financier
            OPEN intr_parties_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH intr_parties_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE intr_parties_cursor;
          END IF;
          DBMS_OUTPUT.PUT_LINE('v_cc_name=' || v_cc_name);
          IF v_cc_name IS NOT NULL THEN
            v_cc_list.EXTEND;
            X := NVL(X, 0) + 1;
            v_cc_list(X) := TQC_COMPANY_MEMO_CC_OBJ(NULL,
                                                    NULL,
                                                    v_cc_name,
                                                    v_cc_address,
                                                    v_cc_email_addr,
                                                    v_memo_cc_tab(I)
                                                    .CC_REMARKS,
                                                    v_memo_cc_tab(I).CC_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_CODE,
                                                    v_cc_codetown);
          END IF;
        END LOOP;
      END IF;
    
    END IF;
  
    IF v_appl_lvl = 'C' AND v_in_claim_no IS NULL THEN
      RAISE_ERROR('claim no not provided for a quotation level memo.');
    ELSIF v_appl_lvl = 'C' THEN
      SELECT CMB_POL_BATCH_NO,
             CMB_POL_POLICY_NO,
             CMB_POL_REN_ENDOS_NO,
             CMB_BRN_CODE,
             CMB_AGNT_AGENT_CODE,
             CMB_PRP_CODE,
             CMB_CLIENT_PRP_CODE,
             PRO_DESC
        INTO v_batch_no,
             v_pol_no,
             v_endr_no,
             v_brn_code,
             v_agn_code,
             v_insured_code,
             v_clnt_code,
             v_pro_desc
        FROM GIN_POLICIES, GIN_CLAIM_MASTER_BOOKINGS, GIN_PRODUCTS
       WHERE CMB_POL_BATCH_NO = POL_BATCH_NO
         AND CMB_PRO_CODE = PRO_CODE
         AND CMB_CLAIM_NO = v_in_claim_no;
    
      IF v_addressee_type = 1 THEN
        --AGENT
        OPEN agn_cursor(v_agn_code);
        FETCH agn_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE agn_cursor;
      ELSIF v_addressee_type = 2 THEN
        --CLAIMANT
        OPEN claimant_cursor(v_in_claim_no, v_in_cld_code);
        FETCH claimant_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_reg_clmt_code, v_email_addr;
        CLOSE claimant_cursor;
      ELSIF v_addressee_type = 3 THEN
        --CLIENT
        OPEN clnts_cursor(v_clnt_code);
        FETCH clnts_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE clnts_cursor;
      ELSIF v_addressee_type = 4 THEN
        --INSURED
        OPEN clnts_cursor(v_insured_code);
        FETCH clnts_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE clnts_cursor;
      ELSIF v_addressee_type = 6 THEN
        -- SERVICE PROVIDERS
        OPEN spr_cursor(v_in_spr_code);
        FETCH spr_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE spr_cursor;
      ELSIF V_ADDRESSEE_TYPE = 7 THEN
        -- Agents/Insurers
        OPEN agn_cursor(v_in_agn_code);
        FETCH agn_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE agn_cursor;
      ELSIF V_ADDRESSEE_TYPE = 8 THEN
        -- Interested Parties
        OPEN intr_parties_cursor(v_pip_code);
        FETCH intr_parties_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE intr_parties_cursor;
      ELSIF V_ADDRESSEE_TYPE = 9 THEN
        --  Premium Financier
        OPEN intr_parties_cursor(v_pip_pf_code);
        FETCH intr_parties_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE intr_parties_cursor;
      END IF;
      IF NVL(v_memo_cc_tab.COUNT, 0) > 0 THEN
        X := 0;
        FOR I IN 1 .. v_memo_cc_tab.COUNT LOOP
          v_cc_address    := NULL;
          v_cc_codetown   := NULL;
          v_cc_email_addr := NULL;
          IF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 1 THEN
            --AGENT
            OPEN agn_cursor(v_agn_code);
            FETCH agn_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 2 THEN
            --CLAIMANT
            OPEN claimant_cursor(v_in_claim_no,
                                 v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH claimant_cursor
              INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_reg_clmt_code, v_email_addr;
            CLOSE claimant_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 3 THEN
            --CLIENT
            OPEN clnts_cursor(v_clnt_code);
            FETCH clnts_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE clnts_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 4 THEN
            --INSURED
            OPEN clnts_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH clnts_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE clnts_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 6 THEN
            -- SERVICE PROVIDERS
            OPEN agn_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH agn_cursor
              INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 7 THEN
            -- Agents/Insurers
            OPEN agn_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH agn_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 8 THEN
            -- Interested Parties
            OPEN intr_parties_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH intr_parties_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE intr_parties_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 9 THEN
            --  Premium Financier
            OPEN intr_parties_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH intr_parties_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE intr_parties_cursor;
          END IF;
          IF v_cc_name IS NOT NULL THEN
            v_cc_list.EXTEND;
            X := NVL(X, 0) + 1;
            v_cc_list(X) := TQC_COMPANY_MEMO_CC_OBJ(NULL,
                                                    NULL,
                                                    v_cc_name,
                                                    v_cc_address,
                                                    v_cc_email_addr,
                                                    v_memo_cc_tab(I)
                                                    .CC_REMARKS,
                                                    v_memo_cc_tab(I).CC_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_CODE,
                                                    v_cc_codetown);
          END IF;
        END LOOP;
      END IF;
    END IF;
    IF v_appl_lvl = 'Q' AND NVL(v_in_quot_code, 0) = 0 THEN
      RAISE_ERROR('Client code not provided for a quotation level memo.');
    ELSIF v_appl_lvl = 'Q' THEN
      SELECT QUOT_NO, QUOT_BRN_CODE, QUOT_AGNT_AGENT_CODE, QUOT_PRP_CODE
        INTO v_quot_no, v_brn_code, v_agn_code, v_clnt_code
        FROM GIN_QUOTATIONS
       WHERE QUOT_CODE = v_in_quot_code;
      IF v_addressee_type = 1 THEN
        --AGENT
        OPEN agn_cursor(v_agn_code);
        FETCH agn_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE agn_cursor;
      ELSIF v_addressee_type = 2 THEN
        --CLAIMANT
        NULL; --claimant_cursor(v_claim_no,v_cld_code) INTO v_add_name,v_add_sht_desc,v_add_address,v_code_town;
      ELSIF v_addressee_type = 3 THEN
        --CLIENT
        OPEN clnts_cursor(v_clnt_code);
        FETCH clnts_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE clnts_cursor;
      ELSIF v_addressee_type = 4 THEN
        --INSURED
        OPEN clnts_cursor(v_in_insured_code);
        FETCH clnts_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE clnts_cursor;
      ELSIF v_addressee_type = 6 THEN
        -- SERVICE PROVIDERS
        NULL; --OPEN spr_cursor(v_claim_no,vsprcode) INTO v_add_name,v_add_sht_desc,v_add_address,v_code_town;
      ELSIF V_ADDRESSEE_TYPE = 7 THEN
        -- Agents/Insurers
        OPEN agn_cursor(v_in_agn_code);
        FETCH agn_cursor
          INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
        CLOSE agn_cursor;
      ELSIF V_ADDRESSEE_TYPE = 8 THEN
        -- Interested Parties
        NULL;
      ELSIF V_ADDRESSEE_TYPE = 9 THEN
        --  Premium Financier
        NULL;
      END IF;
      IF NVL(v_memo_cc_tab.COUNT, 0) > 0 THEN
        X := 0;
        FOR I IN 1 .. v_memo_cc_tab.COUNT LOOP
          v_cc_address    := NULL;
          v_cc_codetown   := NULL;
          v_cc_email_addr := NULL;
          IF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 1 THEN
            --AGENT
            OPEN agn_cursor(v_agn_code);
            FETCH agn_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 2 THEN
            --CLAIMANT
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 3 THEN
            --CLIENT
            OPEN clnts_cursor(v_clnt_code);
            FETCH clnts_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE clnts_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 4 THEN
            --INSURED
            OPEN clnts_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH clnts_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE clnts_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 6 THEN
            -- SERVICE PROVIDERS
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 7 THEN
            -- Agents/Insurers
            OPEN agn_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH agn_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 8 THEN
            -- Interested Parties
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 9 THEN
            --  Premium Financier
            NULL;
          END IF;
          IF v_cc_name IS NOT NULL THEN
            v_cc_list.EXTEND;
            X := NVL(X, 0) + 1;
            v_cc_list(X) := TQC_COMPANY_MEMO_CC_OBJ(NULL,
                                                    NULL,
                                                    v_cc_name,
                                                    v_cc_address,
                                                    v_cc_email_addr,
                                                    v_memo_cc_tab(I)
                                                    .CC_REMARKS,
                                                    v_memo_cc_tab(I).CC_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_CODE,
                                                    v_cc_codetown);
          END IF;
        END LOOP;
      END IF;
    END IF;
    IF v_appl_lvl = 'L' AND NVL(v_in_clnt_code, 0) = 0 THEN
      RAISE_ERROR('Client/Insured code not provided for a Client level memo.');
    ELSIF v_appl_lvl = 'L' THEN
      OPEN clnts_cursor(v_in_clnt_code);
      FETCH clnts_cursor
        INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
      CLOSE clnts_cursor;
      v_clnt_code := v_in_clnt_code;
      IF NVL(v_memo_cc_tab.COUNT, 0) > 0 THEN
        X := 0;
        FOR I IN 1 .. v_memo_cc_tab.COUNT LOOP
          v_cc_address    := NULL;
          v_cc_codetown   := NULL;
          v_cc_email_addr := NULL;
          IF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE IN (1, 7) THEN
            --AGENT
            OPEN agn_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH agn_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 2 THEN
            --CLAIMANT
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE IN (3, 4) THEN
            --CLIENT
            OPEN clnts_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH clnts_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE clnts_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 6 THEN
            -- SERVICE PROVIDERS
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 8 THEN
            -- Interested Parties
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 9 THEN
            --  Premium Financier
            NULL;
          END IF;
          IF v_cc_name IS NOT NULL THEN
            v_cc_list.EXTEND;
            X := NVL(X, 0) + 1;
            v_cc_list(X) := TQC_COMPANY_MEMO_CC_OBJ(NULL,
                                                    NULL,
                                                    v_cc_name,
                                                    v_cc_address,
                                                    v_cc_email_addr,
                                                    v_memo_cc_tab(I)
                                                    .CC_REMARKS,
                                                    v_memo_cc_tab(I).CC_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_CODE,
                                                    v_cc_codetown);
          END IF;
        END LOOP;
      END IF;
    END IF;
    IF v_appl_lvl = 'A' AND NVL(v_in_agn_code, 0) = 0 THEN
      RAISE_ERROR('Agent?Broker code not provided for a Agent/Broker level memo.');
    ELSIF v_appl_lvl = 'A' THEN
      OPEN agn_cursor(v_in_agn_code);
      FETCH agn_cursor
        INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
      CLOSE agn_cursor;
      IF NVL(v_memo_cc_tab.COUNT, 0) > 0 THEN
        X := 0;
        FOR I IN 1 .. v_memo_cc_tab.COUNT LOOP
          v_cc_address    := NULL;
          v_cc_codetown   := NULL;
          v_cc_email_addr := NULL;
          IF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE IN (1, 7) THEN
            --AGENT
            OPEN agn_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH agn_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 2 THEN
            --CLAIMANT
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE IN (3, 4) THEN
            --CLIENT
            OPEN clnts_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH clnts_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE clnts_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 6 THEN
            -- SERVICE PROVIDERS
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 8 THEN
            -- Interested Parties
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 9 THEN
            --  Premium Financier
            NULL;
          END IF;
          IF v_cc_name IS NOT NULL THEN
            v_cc_list.EXTEND;
            X := NVL(X, 0) + 1;
            v_cc_list(X) := TQC_COMPANY_MEMO_CC_OBJ(NULL,
                                                    NULL,
                                                    v_cc_name,
                                                    v_cc_address,
                                                    v_cc_email_addr,
                                                    v_memo_cc_tab(I)
                                                    .CC_REMARKS,
                                                    v_memo_cc_tab(I).CC_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_CODE,
                                                    v_cc_codetown);
          END IF;
        END LOOP;
      END IF;
    END IF;
    IF v_appl_lvl = 'V' AND NVL(v_in_spr_code, 0) = 0 THEN
      RAISE_ERROR('Vendor/Service Provider code not provided for a Vendor level memo.');
    ELSIF v_appl_lvl = 'V' THEN
      OPEN spr_cursor(v_in_spr_code);
      FETCH spr_cursor
        INTO v_add_name, v_add_sht_desc, v_add_address, v_code_town, v_email_addr;
      CLOSE spr_cursor;
      IF NVL(v_memo_cc_tab.COUNT, 0) > 0 THEN
        v_cc_list.DELETE;
        X := 0;
        FOR I IN 1 .. v_memo_cc_tab.COUNT LOOP
          v_cc_address    := NULL;
          v_cc_codetown   := NULL;
          v_cc_email_addr := NULL;
          IF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE IN (1, 7) THEN
            --AGENT
            OPEN agn_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH agn_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE agn_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 2 THEN
            --CLAIMANT
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE IN (3, 4) THEN
            --CLIENT
            OPEN clnts_cursor(v_memo_cc_tab(I).CC_ADDRESSEE_CODE);
            FETCH clnts_cursor
              INTO v_cc_name, v_dummy, v_cc_address, v_cc_codetown, v_cc_email_addr;
            CLOSE clnts_cursor;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 6 THEN
            -- SERVICE PROVIDERS
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 8 THEN
            -- Interested Parties
            NULL;
          ELSIF v_memo_cc_tab(I).CC_ADDRESSEE_TYPE = 9 THEN
            --  Premium Financier
            NULL;
          END IF;
          IF v_cc_name IS NOT NULL THEN
            v_cc_list.EXTEND;
            X := NVL(X, 0) + 1;
            v_cc_list(X) := TQC_COMPANY_MEMO_CC_OBJ(NULL,
                                                    NULL,
                                                    v_cc_name,
                                                    v_cc_address,
                                                    v_cc_email_addr,
                                                    v_memo_cc_tab(I)
                                                    .CC_REMARKS,
                                                    v_memo_cc_tab(I).CC_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_TYPE,
                                                    v_memo_cc_tab(I)
                                                    .CC_ADDRESSEE_CODE,
                                                    v_cc_codetown);
          END IF;
        END LOOP;
      END IF;
    END IF;
    /*IF v_appl_lvl = 'I' AND NVL(v_usr_code,0) = 0 THEN 
        RAISE_ERROR('Vendor/Service Provider code not provided for a Vendor level memo.');
    ELSE
    
    END IF;*/
    --  RAISE_ERROR(v_brn_code);
  
    select TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ||
           TQC_COMEM_CODE_SEQ.NEXTVAL
      INTO v_comem_code
      FROM DUAL;
    INSERT INTO TQC_COMPANY_MEMOS
      (COMEM_CODE,
       COMEM_SYS_CODE,
       COMEM_DESC,
       COMEM_DATE,
       COMEM_CMB_CLAIM_NO,
       COMEM_SUBJECT,
       COMEM_REG_CLMT_CODE,
       COMEM_ADDRESS,
       COMEM_COMMENTS,
       COMEM_POL_POLICY_NO,
       COMEM_APPL_LVL,
       COMEM_DONE_BY,
       COMEM_AUTHORIZED,
       COMEM_YOUR_REF,
       COMEM_ADDRESSEE,
       COMEM_SIGNATORY,
       COMEM_CC_LIST,
       COMEM_MTYP_CODE,
       COMEM_POL_REN_ENDOS_NO,
       COMEM_BRN_CODE,
       COMEM_PREPARED_BY,
       COMEM_CORR_CODE,
       COMEM_REF,
       COMEM_POL_BATCH_NO,
       COMEM_AUTHORIZED_DATE,
       COMEM_FILE_NO,
       COMEM_AUTHORIZED_BY,
       COMEM_SIGN_RANK,
       COMEM_INSURED,
       COMEM_RPT_TYPE,
       COMEM_ADDRESSEE_TYPE,
       COMEM_EDITS,
       COMEM_REMINDERS,
       COMEM_PRP_CODE,
       COMEM_STATUS,
       COMEM_CANCEL_DATE,
       COMEM_CANCEL_BY,
       COMEM_CODETOWN,
       COMEM_PRO_DESC,
       COMEM_COURT_CASE_NO,
       COMEM_GCC_CODE,
       COMEM_QUOT_CODE,
       COMEM_QUOT_NO,
       COMEM_EMAIL_ADDR)
    VALUES
      (v_comem_code,
       v_sys_code,
       NULL,
       v_date,
       v_in_claim_no,
       v_memo_subj,
       v_reg_clmt_code,
       v_add_address,
       NULL,
       v_pol_no,
       v_appl_lvl,
       v_user,
       'N',
       NULL,
       v_add_name,
       NULL,
       NULL,
       v_mtyp_code,
       v_endr_no,
       v_brn_code,
       v_user,
       v_in_spr_code,
       NULL,
       v_batch_no,
       NULL,
       NULL,
       NULL,
       NULL,
       v_insured,
       'L',
       v_addressee_type,
       NULL,
       NULL,
       v_clnt_code,
       'L',
       NULL,
       NULL,
       v_code_town,
       v_pro_desc,
       v_in_case_no,
       v_in_gcc_code,
       v_in_quot_code,
       v_quot_no,
       v_email_addr);
  
    BEGIN
      select TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ||
             TQC_MEMH_CODE_SEQ.NEXTVAL,
             MEMO_CODE,
             MEMO_SUBJECT
        INTO v_memh_code, v_memo_code, v_raw_txt
        from TQC_MEMOS
       WHERE TQC_MEMOS.MEMO_MTYP_CODE = v_mtyp_code;
      BEGIN
        v_memo := Tqc_Memos_Dbpkg.PROCESS_GIS_POL_MEMO(v_batch_no,
                                                       v_in_claim_no,
                                                       v_in_gcc_code,
                                                       v_raw_txt,
                                                       v_appl_lvl);
      EXCEPTION
        WHEN OTHERS THEN
          v_memo := v_raw_txt;
      END;
      BEGIN
        INSERT INTO TQC_MEMO_HEADER
          (MEMH_CODE, MEMH_COMEM_CODE, MEMH_MEMO_CODE, MEMH_MEMO_SUBJECT)
        VALUES
          (v_memh_code, v_comem_code, v_memo_code, v_memo);
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('Error inserting memo subject ...');
      END;
      select MEMDET_CODE,
             MEMDET_CONTENT,
             TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY') ||
                       TQC_MEMDTLS_CODE_SEQ.NEXTVAL) --,
      --MEMDET_FOOTER 
        INTO v_memdet_code, v_dtl_raw_txt, v_memdtls_code --,v_memdtls_footer
        from TQC_MEMO_DETAILS
       where MEMDET_MEMO_CODE = v_memo_code;
    
      BEGIN
        v_dtl_memo := Tqc_Memos_Dbpkg.PROCESS_GIS_POL_MEMO(v_batch_no,
                                                           v_in_claim_no,
                                                           v_in_gcc_code,
                                                           v_dtl_raw_txt,
                                                           v_appl_lvl);
      EXCEPTION
        WHEN OTHERS THEN
          v_dtl_memo := v_dtl_raw_txt;
      END;
      BEGIN
        SELECT COUNT(*)
          INTO v_cnt
          FROM TQC_MEMO_HEADER_DTLS
         WHERE MEMDTLS_MEMH_CODE = v_memh_code;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
      END;
      IF NVL(v_cnt, 0) = 0 THEN
        BEGIN
          INSERT INTO TQC_MEMO_HEADER_DTLS
            (MEMDTLS_MEMH_CODE,
             MEMDTLS_MEMDET_CODE,
             MEMDTLS_DETAIL,
             MEMDTLS_CODE --,
             --MEMDTLS_FOOTER
             )
          values
            (v_memh_code,
             v_memdet_code,
             v_dtl_memo,
             v_memdtls_code --,
             --v_memdtls_footer
             );
        EXCEPTION
          WHEN OTHERS THEN
            RAISE_ERROR('Error inserting memo subject and details..');
        END;
      ELSE
        Begin
          UPDATE TQC_MEMO_HEADER_DTLS
             SET MEMDTLS_MEMDET_CODE = v_memdet_code,
                 MEMDTLS_DETAIL      = v_dtl_memo,
                 MEMDTLS_CODE        = v_memdtls_code --,
          -- MEMDTLS_FOOTER=v_memdtls_footer
           WHERE MEMDTLS_MEMH_CODE = v_memh_code;
        EXCEPTION
          WHEN OTHERS THEN
            RAISE_ERROR('Error updating memo subject and details..');
        END;
      END IF;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Error creating memo subject and details..');
    END;
    IF NVL(v_cc_list.COUNT, 0) > 0 THEN
      FOR I IN 1 .. v_cc_list.COUNT LOOP
        INSERT INTO TQC_COMPANY_MEMO_CC
          (CC_CODE,
           CC_COMEM_CODE,
           CC_NAME,
           CC_ADDRESS,
           CC_REMARKS,
           CC_TYPE,
           CC_EMAIL_ADDR,
           CC_ADDRESSEE_TYPE,
           CC_ADDRESSEE_CODE,
           CC_CODETOWN)
        VALUES
          (TQC_CC_CODE_SEQ.NEXTVAL,
           v_comem_code,
           v_cc_list(I).CC_NAME,
           v_cc_list(I).CC_ADDRESS,
           v_cc_list(I).CC_REMARKS,
           v_cc_list(I).CC_TYPE,
           v_cc_list(I).CC_EMAIL_ADDR,
           v_cc_list(I).CC_ADDRESSEE_TYPE,
           v_cc_list(I).CC_ADDRESSEE_CODE,
           v_cc_list(I).CC_CODETOWN);
      END LOOP;
    END IF;
    RETURN(v_comem_code);
  
  END;
  
  PROCEDURE update_reminder_status(v_comem_code NUMBER,
                                   v_user VARCHAR2) IS
	reminderNum number;
	v_usr VARCHAR2(40);
    V_currReminder NUMBER;
	
BEGIN

       SELECT comem_reminders INTO V_currReminder
       FROM TQC_COMPANY_MEMOS
       WHERE comem_code = v_comem_code;
		
	 
	    reminderNum :=	nvl(V_currReminder,0)+1;
	  

          
		insert into TQC_COMPANY_MEMO_REMINDERS(REMNDR_CODE, REMNDR_DATE, REMNDR_COMEM_CODE, REMNDR_USER, REMNDR_NUM) 
		values(REMNDR_CODE_seq.nextval,TRUNC(sysdate),v_comem_code,v_user,reminderNum);
		
        UPDATE TQC_COMPANY_MEMOS
        SET  comem_reminders = reminderNum
        WHERE comem_code = v_comem_code;
exception 
when others then
raise_error('error updating reminder status');
			
	  	
END;
  
  
END TQC_MEMO_WEB_PKG180914; 
/