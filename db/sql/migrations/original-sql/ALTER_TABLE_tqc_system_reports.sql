DECLARE

    v_sql_string   VARCHAR2(200);
    column_exist    CHAR(1);
    
BEGIN
    v_sql_string := 'ALTER TABLE TQ_CRM.TQC_SYSTEM_REPORTS
                     ADD (RPT_PRNT_SRV_APPL CHAR(1 BYTE) DEFAULT ''N'')';
    BEGIN
        SELECT   decode(COUNT(*),0,'N','Y') cnt
        INTO     column_exist
        FROM     ALL_TAB_COLS
        WHERE    TABLE_NAME = 'TQC_SYSTEM_REPORTS'
        AND      COLUMN_NAME = 'RPT_PRNT_SRV_APPL'
        AND      OWNER = 'TQ_CRM';
        EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            column_exist := 'N';
    END;
    
    BEGIN
        IF column_exist =  'N' THEN
        
             EXECUTE IMMEDIATE v_sql_string;
             
        END IF;
    END;
    
END;