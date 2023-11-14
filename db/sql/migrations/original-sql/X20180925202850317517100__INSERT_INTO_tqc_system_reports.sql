DECLARE
    isInsertExisting    CHAR;
    v_insert1      VARCHAR2(700);
    v_insert2      VARCHAR2(700);
    
    pk_code1   NUMBER;
    pk_code2   NUMBER;
    
    BEGIN
        BEGIN
            SELECT MAX(RPT_CODE)+1
            INTO  pk_code1
            FROM TQC_SYSTEM_REPORTS;
        END;
        
          BEGIN
            SELECT MAX(RPT_TMPL_CODE)+1
            INTO  pk_code2
            FROM TQC_SYS_RPT_TEMPLATES;
        END;
        
        v_insert1 :=
                           'Insert into TQ_CRM.TQC_SYSTEM_REPORTS
                           (RPT_CODE, 
                            RPT_SYS_CODE, RPT_NAME, RPT_DESCRIPTION, RPT_DATA_FILE, RPT_APPLCTN_LEVEL, 
                            RPT_ACTIVE, RPT_RSM_CODE, RPT_ORDER)
                         Values
                           (' || pk_code1 ||', 37, ''CLMQUERY_RPT'', ''Claim Transaction Details'', ''clmquery.xml'', 
                            NULL, ''A'', 14, NULL)';
                            
                            
         v_insert2 :=
                            'Insert into TQ_CRM.TQC_SYS_RPT_TEMPLATES
                           (RPT_TMPL_CODE, 
                            RPT_TMPL_RPT_CODE, RPT_TMPL_FILE, RPT_TMPL_NAME, RPT_TMPL_DESCRIPTION, RPT_TMPL_STYLE_FILE, 
                            RPT_TMPL_ORG_CODE, RPT_TMPL_ACTIVE)
                         Values
                           (' || pk_code2 ||', ' || pk_code1 ||', ''clmquery_template.rtf'', ''CLMQUERY_RPT'', ''Claim Transaction Details'', 
                            ''clmquery_template.xsl'', NULL, ''A'')';                  
                            
                            
        
        
        BEGIN
            SELECT DECODE(COUNT(*),0,'N','Y')
            INTO     isInsertExisting
            FROM    TQ_CRM.TQC_SYSTEM_REPORTS
            WHERE  RPT_NAME = 'CLMQUERY_RPT';
            
            EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
                isInsertExisting := 'N';
        END;                 
        
        BEGIN
        
            IF isInsertExisting = 'N'
            THEN
                EXECUTE IMMEDIATE v_insert1;
            END IF;
            
        END;
        
        
        
        
                     
        
        BEGIN
            SELECT DECODE(COUNT(*),0,'N','Y')
            INTO     isInsertExisting
            FROM    TQ_CRM.TQC_SYS_RPT_TEMPLATES
            WHERE  RPT_TMPL_FILE = 'clmquery_template.rtf';
            
            EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
                isInsertExisting := 'N';
        END;
        
        BEGIN
        
            IF isInsertExisting = 'N'
            THEN
                EXECUTE IMMEDIATE v_insert2;
            END IF;
            
        END;
        
    END;