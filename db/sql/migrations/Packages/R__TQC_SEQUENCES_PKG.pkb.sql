/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_SEQUENCES_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_SEQUENCES_PKG AS
  PROCEDURE RAISE_ERROR(v_msg IN VARCHAR2) IS
  BEGIN
    IF SQLCODE != 0 THEN
      RAISE_APPLICATION_ERROR(-20015, v_msg || ' - ' || SQLERRM(SQLCODE));
    ELSE
      RAISE_APPLICATION_ERROR(-20015, v_msg);
    END IF;
  END RAISE_ERROR;

  PROCEDURE Get_Seq(v_seq_type    IN VARCHAR2,
                    v_seq_no_type IN VARCHAR2,
                    v_brn_code    IN NUMBER,
                    v_uw_yr       IN NUMBER,
                    v_clnt_type   IN VARCHAR2,
                    v_number_seq  OUT NUMBER) IS
  
    vbrncode   NUMBER;
    vuwyr      NUMBER;
    vproprex   VARCHAR2(5);
    v_clnttype VARCHAR2(5);
    v_nxt_seq  NUMBER;
    v_seq_code NUMBER;
  BEGIN
    --RAISE_ERROR(v_seq_type);
    --taking care of branch 
    vbrncode := -2000;
    IF v_seq_no_type IN ('PBC', 'PBA', 'YBC', 'YBA') THEN
      vbrncode := v_brn_code;
    END IF;
    --taking care of the undewritting year
    vuwyr := -2000;
    IF v_seq_no_type IN ('YAC', 'YAA', 'YBC', 'YBA') THEN
      vuwyr := v_uw_yr;
    END IF;
    v_clnttype := 'ALL';
    IF v_seq_no_type IN ('YAC', 'PBC', 'YBC', 'PAC') THEN
      v_clnttype := v_clnt_type;
    END IF;
    --RAISE_ERROR('v_seq_no_type '||v_seq_no_type);
    --taking care of products
  
    BEGIN
      SELECT TSEQ_CODE, TSEQ_NEXT_NO
        INTO v_seq_code, v_nxt_seq
        FROM TQC_SEQUENCES
       WHERE TSEQ_TYPE = v_seq_type
         AND TSEQ_NO_TYPE = v_seq_no_type
         AND TSEQ_BRN = vbrncode
         AND TSEQ_CLNT_TYPE = v_clnttype
         AND TSEQ_UWYR = vuwyr;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        INSERT INTO TQC_SEQUENCES
          (TSEQ_CODE,
           TSEQ_TYPE,
           TSEQ_NO_TYPE,
           TSEQ_BRN,
           TSEQ_UWYR,
           TSEQ_NEXT_NO,
           TSEQ_CLNT_TYPE)
        VALUES
          (TQC_SEQ_CODE_SEQ.NEXTVAL,
           v_seq_type,
           v_seq_no_type,
           vbrncode,
           vuwyr,
           1,
           v_clnttype);
        v_nxt_seq  := 1;
        v_seq_code := NULL;
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR GETTING CURRENT SEQUENCE NO');
    END;
  
    v_number_seq := v_nxt_seq;
    BEGIN
      UPDATE TQC_SEQUENCES
         SET TSEQ_NEXT_NO = NVL(v_number_seq, 0) + 1
       WHERE TSEQ_TYPE = v_seq_type
         AND TSEQ_NO_TYPE = v_seq_no_type
         AND TSEQ_BRN = vbrncode
         AND TSEQ_CLNT_TYPE = v_clnttype;
         --AND TSEQ_UWYR = vuwyr;
    
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR UPDATING NEXT SEQUENCE..');
    END;
    COMMIT;
  END;

  FUNCTION get_number_format(v_number_type IN VARCHAR2,
                             v_brn_code    IN NUMBER,
                             v_uw_yr       IN NUMBER,
                             v_clnt_type   IN VARCHAR2,
                             v_format      IN VARCHAR2,
                             v_surname     IN VARCHAR2 DEFAULT NULL,
                             v_other_names IN VARCHAR2 DEFAULT NULL)
    RETURN VARCHAR2 IS
    v_no_format     VARCHAR2(75);
    cbrnshtdesc     VARCHAR2(75);
    v_reg_sht_desc  VARCHAR2(30);
    cpolprefix      VARCHAR2(75);
    v_sno_type      VARCHAR2(15);
    v_sno_length    NUMBER;
    v_pol_seq       NUMBER;
    v_pol_seq1      VARCHAR2(15);
    v_trans_pfx     VARCHAR2(10);
    v_prg_type      VARCHAR2(10);
    v_endos_count   NUMBER;
    v_snameinit_len NUMBER;
  BEGIN
  
    IF v_number_type = 'C' THEN
      IF v_format IS NULL THEN
        v_no_format := TQC_PARAMETERS_PKG.get_param_varchar('CLIENT_ID_FORMAT');
      ELSE
        v_no_format := v_format;
      END IF;
      v_sno_type      := TQC_PARAMETERS_PKG.get_param_varchar('CLNTSERIAL');
      v_sno_length    := TQC_PARAMETERS_PKG.get_param_varchar('CLIENT_SERIAL_LENGTH');
      v_snameinit_len := TQC_PARAMETERS_PKG.get_param_varchar('CLNT_SURNAME_CHAR_LEN');
    
    END IF;
  
    --RAISE_ERROR('v_no_format==== '||v_no_format||'v_surname=='||v_surname||' v_snameinit_len '||v_snameinit_len);  
    IF INSTR(v_no_format, '[SURNAMEINIT]') != 0 THEN
      v_no_format := REPLACE(v_no_format,
                             '[SURNAMEINIT]',
                             SUBSTR(v_surname, 1, v_snameinit_len));
    END IF;
    -- RAISE_ERROR('v_no_format==== '||v_no_format);
    IF INSTR(v_no_format, '[OTHERNAMEINIT]') != 0 THEN
      v_no_format := REPLACE(v_no_format,
                             '[OTHERNAMEINIT]',
                             SUBSTR(v_other_names, 1, v_snameinit_len));
    END IF;
  
    IF INSTR(v_no_format, '[BRN]') != 0 THEN
      BEGIN
        SELECT brn_sht_desc, reg_sht_desc
          INTO cbrnshtdesc, v_reg_sht_desc
          FROM tqc_branches, tqc_regions
         WHERE brn_reg_code = reg_code
           AND brn_code = v_brn_code;
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_ERROR('ERROR GETTING BRANCH AND REGION SHORT PREFIX==='||v_brn_code);
      END;
    
      v_no_format := REPLACE(v_no_format, '[BRN]', cbrnshtdesc);
      v_no_format := REPLACE(v_no_format, '[REGION]', v_reg_sht_desc);
    END IF;
    
--    RAISE_ERROR('v_clnt_type: '||v_clnt_type);
    
    IF ( INSTR(v_no_format,'[CLNTTYPE]') > 0 ) THEN
      v_no_format:= REPLACE(v_no_format,'[CLNTTYPE]',UPPER(v_clnt_type));
    END IF;
     
    BEGIN
      SELECT REPLACE(v_no_format, '[YYYY]', v_uw_yr)
        INTO v_no_format
        FROM DUAL;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR SUBSTITUTING THE SYSDATE 4 CHARACTER FORMAT..');
    END;
  
    BEGIN
      SELECT REPLACE(v_no_format, '[YY]', TO_CHAR(SYSDATE, 'YY'))
        INTO v_no_format
        FROM DUAL;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR SUBSTITUTING THE SYSDATE 2 CHARACTER FORMAT');
    END;
  
    BEGIN
      SELECT REPLACE(v_no_format, '[MM]', TO_CHAR(SYSDATE, 'MM'))
        INTO v_no_format
        FROM DUAL;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR SUBSTITUTING THE SYSDATE 4 CHARACTER FROMAT..');
    END;
  
    BEGIN
      SELECT REPLACE(v_no_format, '[MON]', TO_CHAR(SYSDATE, 'MON'))
        INTO v_no_format
        FROM DUAL;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR SUBSTITUTING THE SYSDATE 2 CHARACTER FROMAT');
    END;
  
    -- raise_error(v_sno_type||';'||v_number_type);
    BEGIN
      get_seq(v_number_type,
              v_sno_type,
              v_brn_code,
              v_uw_yr,
              v_clnt_type,
              v_pol_seq);
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('ERROR GETTING THE SERIAL NO..');
    END;
  
    v_pol_seq1  := LPAD(v_pol_seq, v_sno_length, '0');
    v_no_format := REPLACE(v_no_format, '[SERIALNO]', v_pol_seq1);
    RETURN(v_no_format);
  END;

  PROCEDURE TQC_SEQUENCES_PRC(v_add_edit       IN VARCHAR2,
                              v_code           TQC_SEQUENCES.TSEQ_CODE%TYPE,
                              v_type           TQC_SEQUENCES.TSEQ_TYPE%TYPE,
                              v_no_type        TQC_SEQUENCES.TSEQ_NO_TYPE%TYPE,
                              v_brn            TQC_SEQUENCES.TSEQ_BRN%TYPE,
                              v_uwyr           TQC_SEQUENCES.TSEQ_UWYR%TYPE,
                              v_next_no        TQC_SEQUENCES.TSEQ_NEXT_NO%TYPE,
                              v_err            out varchar2,
                              v_org_code       Number,
                              v_tseq_clnt_type TQC_SEQUENCES.TSEQ_CLNT_TYPE%TYPE DEFAULT NULL) IS
  
  BEGIN
  
    IF v_add_edit = 'A' THEN
    
      INSERT INTO TQC_SEQUENCES
        (TSEQ_CODE,
         TSEQ_TYPE,
         TSEQ_NO_TYPE,
         TSEQ_BRN,
         TSEQ_UWYR,
         TSEQ_NEXT_NO,
         TSEQ_ORG_CODE,
         TSEQ_CLNT_TYPE)
      VALUES
        (TQC_SEQ_CODE_SEQ.NEXTVAL,
         v_type,
         v_no_type,
         v_brn,
         v_uwyr,
         v_next_no,
         v_org_code,
         v_tseq_clnt_type);
    
    ELSIF v_add_edit = 'E' THEN
      UPDATE TQC_SEQUENCES
         SET TSEQ_TYPE      = v_type,
             TSEQ_NO_TYPE   = v_no_type,
             TSEQ_BRN       = v_brn,
             TSEQ_UWYR      = v_uwyr,
             TSEQ_NEXT_NO   = v_next_no,
             TSEQ_ORG_CODE  = v_org_code,
             TSEQ_CLNT_TYPE = v_tseq_clnt_type
       WHERE TSEQ_CODE = v_code;
    
    ELSIF v_add_edit = 'D' THEN
      DELETE FROM TQC_SEQUENCES WHERE TSEQ_CODE = v_code;
    END IF;
  
  END;

  PROCEDURE get_sequences(v_refcur OUT SEQUENCE_REF) IS
  BEGIN
    OPEN v_refcur FOR
      SELECT TSEQ_CODE,
             TSEQ_TYPE,
             TSEQ_NO_TYPE,
             TSEQ_BRN,
             TSEQ_UWYR,
             TSEQ_NEXT_NO,
             TQC_SETUPS_CURSOR.BRANCH_NAME(TSEQ_BRN) BRANCHNAME,
             TSEQ_ORG_CODE,
             TSEQ_CLNT_TYPE
        FROM TQC_SEQUENCES;
  
  END;

END Tqc_Sequences_Pkg; 
/