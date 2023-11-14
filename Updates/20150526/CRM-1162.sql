--tqc_setups_pkg.updateddfailedrmks

PROCEDURE  updateddfailedrmks(
        v_addedit    IN   VARCHAR2,
        v_dfr_code           NUMBER    ,
        v_dfr_failed_remark  VARCHAR2,
        v_dfr_appl_level     VARCHAR2)
        IS
        BEGIN
        IF v_addedit = 'A'
      THEN
         BEGIN
            

            INSERT INTO tqc_dd_failed_remarks
                        (DFR_CODE, DFR_APPL_LEVEL, DFR_FAILED_REMARK
                        )
                 VALUES (TQC_DFR_CODE_SEQ.NEXTVAL, v_dfr_appl_level, v_dfr_failed_remark
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while creating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_addedit = 'E'
      THEN
         BEGIN
            UPDATE tqc_dd_failed_remarks
               SET DFR_APPL_LEVEL = v_dfr_failed_remark,
                   DFR_FAILED_REMARK = v_dfr_appl_level
             WHERE DFR_CODE = v_dfr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while updating record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      ELSIF v_addedit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_dd_failed_remarks
                  WHERE DFR_CODE = v_dfr_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error occured while deleting record '
                            || SQLERRM (SQLCODE)
                           );
         END;
      END IF;
        EXCEPTION 
             WHEN OTHERS THEN
                RAISE_ERROR('Remarks Proc '||SQLERRM(SQLCODE));
        END;
		----------------------------------------------------------------------------
		
		
		CREATE TABLE TQ_CRM.TQC_USER_TAB_COLUMNS
(
  TABLE_NAME            VARCHAR2(100 BYTE),
  COLUMN_NAME           VARCHAR2(100 BYTE),
  DATA_TYPE             VARCHAR2(100 BYTE),
  DATA_TYPE_MOD         VARCHAR2(100 BYTE),
  DATA_TYPE_OWNER       VARCHAR2(100 BYTE),
  DATA_LENGTH           NUMBER,
  DATA_PRECISION        NUMBER,
  DATA_SCALE            NUMBER,
  NULLABLE              VARCHAR2(100 BYTE),
  COLUMN_ID             VARCHAR2(100 BYTE),
  DEFAULT_LENGTH        VARCHAR2(100 BYTE),
  DATA_DEFAULT          VARCHAR2(100 BYTE),
  NUM_DISTINCT          VARCHAR2(100 BYTE),
  LOW_VALUE             VARCHAR2(100 BYTE),
  HIGH_VALUE            VARCHAR2(100 BYTE),
  DENSITY               VARCHAR2(100 BYTE),
  NUM_NULLS             VARCHAR2(100 BYTE),
  NUM_BUCKETS           VARCHAR2(100 BYTE),
  LAST_ANALYZED         VARCHAR2(100 BYTE),
  SAMPLE_SIZE           VARCHAR2(100 BYTE),
  CHARACTER_SET_NAME    VARCHAR2(100 BYTE),
  CHAR_COL_DECL_LENGTH  VARCHAR2(100 BYTE),
  GLOBAL_STATS          VARCHAR2(100 BYTE),
  USER_STATS            VARCHAR2(100 BYTE),
  AVG_COL_LEN           VARCHAR2(100 BYTE),
  CHAR_LENGTH           VARCHAR2(100 BYTE),
  CHAR_USED             VARCHAR2(100 BYTE),
  V80_FMT_IMAGE         VARCHAR2(100 BYTE),
  DATA_UPGRADED         VARCHAR2(100 BYTE),
  HISTOGRAM             VARCHAR2(100 BYTE)
);