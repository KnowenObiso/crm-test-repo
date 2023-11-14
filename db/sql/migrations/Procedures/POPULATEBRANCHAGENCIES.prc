--
-- POPULATEBRANCHAGENCIES  (Procedure) 
--
CREATE OR REPLACE PROCEDURE TQ_CRM."POPULATEBRANCHAGENCIES" (v_brn_code NUMBER) IS
    CURSOR BRN IS SELECT * FROM TQC_BRANCHES
    WHERE BRN_CODE = v_brn_code;
    CURSOR BRA(v_bra_code NUMBER) IS SELECT * FROM TQC_BRANCH_AGENCIES
    WHERE NVL(BRA_BRN_CODE,-1) = NVL(v_brn_code,NVL(BRA_BRN_CODE,-1));
v_bra_code NUMBER;
v_cnt1       NUMBER;
v_cnt2       NUMBER;
BEGIN
FOR B IN BRN LOOP
    SELECT COUNT(1) INTO v_cnt1
    FROM TQC_BRANCH_AGENCIES
    WHERE BRA_BRN_CODE = B.BRN_CODE;
    
    IF NVL(v_cnt1,0) = 0 THEN
        SELECT TQC_BRA_CODE_SEQ.NEXTVAL INTO v_bra_code FROM DUAL;
        INSERT INTO TQC_BRANCH_AGENCIES(BRA_CODE, BRA_BRN_CODE,BRA_SHT_DESC,BRA_NAME)
        VALUES(v_bra_code,B.BRN_CODE,B.BRN_SHT_DESC||TO_CHAR(B.BRN_CODE),'N/A');
    ELSIF NVL(v_cnt1,0) = 1 THEN
        BEGIN
            SELECT BRA_CODE INTO v_bra_code 
            FROM TQC_BRANCH_AGENCIES
            WHERE BRA_BRN_CODE = B.BRN_CODE;
        EXCEPTION
                 WHEN OTHERS THEN
                       RAISE_APPLICATION_ERROR(-20001,'Error creating record...');
        END;
    END IF;
    IF (NVL(v_cnt1,0) = 0) OR (NVL(v_cnt1,0) = 1) THEN
        UPDATE TQC_BRANCH_UNITS
        SET BRU_BRA_CODE = v_bra_code
        WHERE BRU_BRN_CODE = B.BRN_CODE
        AND BRU_BRA_CODE IS NULL;
    END IF;
END LOOP;
END Populatebranchagencies;

 
 
 
 
 
 
 
 
 
 
 
 
 
 
/