
DECLARE
V_MAX NUMBER;
V_SPRC_CODE_NEW NUMBER;
V_SPRCA_CODE_NEW NUMBER;
V_SPRSA_CODE_NEW NUMBER;
V_COUNT NUMBER;

-- INSERTING TQC_SYS_PROCESSES
BEGIN
        COMMIT;
        SELECT COUNT(*) INTO V_COUNT FROM TQC_SYS_PROCESSES WHERE SPRC_SHT_DESC = 'SRT';
        IF V_COUNT > 0
        THEN            
            TQC_SETUPS_PKG.raise_error('Record already exists');
        ELSE
        
            SELECT MAX(SPRC_CODE) INTO V_MAX FROM TQC_SYS_PROCESSES;
            V_SPRC_CODE_NEW:=V_MAX+1;
            
            
            INSERT INTO TQC_SYS_PROCESSES
               (SPRC_CODE, SPRC_SYS_CODE, SPRC_PROCESS, SPRC_BPM_ID, SPRC_SHT_DESC, 
                SPRC_JPDL_DESC, SPRC_VISIBLE)
             Values
               (V_SPRC_CODE_NEW, 0, 'SERVICE REQUESTS', NULL, 'SRT', 
                NULL, 'Y');

        -- INSERTING TQC_SYS_PROCESS_AREAS

            SELECT MAX(SPRCA_CODE) INTO V_MAX FROM TQC_SYS_PROCESS_AREAS;
            V_SPRCA_CODE_NEW := V_MAX + 1;
            
            SELECT MAX(SPRSA_CODE) INTO V_MAX FROM TQC_SYS_PROCESS_SUB_AREAS;
            V_SPRSA_CODE_NEW := V_MAX +1;

            -- RECORD ONE
            Insert into TQC_SYS_PROCESS_AREAS
               (SPRCA_CODE, SPRCA_SPRC_CODE, SPRCA_AREA, SPRCA_SHT_DESC, SPRCA_VISIBLE)
                Values
               (V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'SERVICE DESK', 'SRD', 'Y');
            Insert into TQC_SYS_PROCESS_SUB_AREAS
               (SPRSA_CODE, SPRSA_SPRCA_CODE, SPRSA_SPRC_CODE, SPRSA_SUB_AREA, SPRSA_TYPE, 
                SPRSA_SHT_DESC, SPRSA_DEFAULT_USR_CODE, SPRSA_VISIBLE)
             Values
               (V_SPRSA_CODE_NEW, V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'ACCESS', 'C', 
                'SRDA', NULL, 'Y');
                
             V_SPRCA_CODE_NEW := V_SPRCA_CODE_NEW +1;
             V_SPRSA_CODE_NEW := V_SPRSA_CODE_NEW + 1;
            -- END ONE
            
            -- RECORD TWO
            Insert into TQC_SYS_PROCESS_AREAS
               (SPRCA_CODE, SPRCA_SPRC_CODE, SPRCA_AREA, SPRCA_SHT_DESC, SPRCA_VISIBLE)
             Values
               (V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'REQUESTS TRACKING', 'RQT', 'Y');
            Insert into TQC_SYS_PROCESS_SUB_AREAS
               (SPRSA_CODE, SPRSA_SPRCA_CODE, SPRSA_SPRC_CODE, SPRSA_SUB_AREA, SPRSA_TYPE, 
                SPRSA_SHT_DESC, SPRSA_DEFAULT_USR_CODE, SPRSA_VISIBLE)
             Values
               (V_SPRSA_CODE_NEW, V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'ACCESS', 'C', 
                'RQTA', NULL, 'Y');
                
             V_SPRCA_CODE_NEW := V_SPRCA_CODE_NEW +1;
             V_SPRSA_CODE_NEW := V_SPRSA_CODE_NEW + 1;
            -- END TWO
            
            -- RECORD THREE
            Insert into TQC_SYS_PROCESS_AREAS
               (SPRCA_CODE, SPRCA_SPRC_CODE, SPRCA_AREA, SPRCA_SHT_DESC, SPRCA_VISIBLE)
             Values
               (V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'REQUESTS HISTORY', 'RQH', 'Y');
            Insert into TQC_SYS_PROCESS_SUB_AREAS
               (SPRSA_CODE, SPRSA_SPRCA_CODE, SPRSA_SPRC_CODE, SPRSA_SUB_AREA, SPRSA_TYPE, 
                SPRSA_SHT_DESC, SPRSA_DEFAULT_USR_CODE, SPRSA_VISIBLE)
             Values
               (V_SPRSA_CODE_NEW, V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'ACCESS', 'C', 
                'RQHA', NULL, 'Y');
            
            V_SPRCA_CODE_NEW := V_SPRCA_CODE_NEW +1;
            V_SPRSA_CODE_NEW := V_SPRSA_CODE_NEW + 1;
            -- END THREE
            
            -- BEGIN FOUR
            Insert into TQC_SYS_PROCESS_AREAS
               (SPRCA_CODE, SPRCA_SPRC_CODE, SPRCA_AREA, SPRCA_SHT_DESC, SPRCA_VISIBLE)
             Values
               (V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'KNOWLEDGE BASE', 'KNB', 'Y');
            Insert into TQC_SYS_PROCESS_SUB_AREAS
               (SPRSA_CODE, SPRSA_SPRCA_CODE, SPRSA_SPRC_CODE, SPRSA_SUB_AREA, SPRSA_TYPE, 
                SPRSA_SHT_DESC, SPRSA_DEFAULT_USR_CODE, SPRSA_VISIBLE)
             Values
               (V_SPRSA_CODE_NEW, V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'ACCESS', 'C', 
                'KNBA', NULL, 'Y');
                
            V_SPRCA_CODE_NEW := V_SPRCA_CODE_NEW +1;
            V_SPRSA_CODE_NEW := V_SPRSA_CODE_NEW + 1;
            -- END FOUR
            
            -- BEGIN FIVE
            Insert into TQC_SYS_PROCESS_AREAS
               (SPRCA_CODE, SPRCA_SPRC_CODE, SPRCA_AREA, SPRCA_SHT_DESC, SPRCA_VISIBLE)
             Values
               (V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'KEY WORDS', 'KWD', 'Y');
            Insert into TQC_SYS_PROCESS_SUB_AREAS
               (SPRSA_CODE, SPRSA_SPRCA_CODE, SPRSA_SPRC_CODE, SPRSA_SUB_AREA, SPRSA_TYPE, 
                SPRSA_SHT_DESC, SPRSA_DEFAULT_USR_CODE, SPRSA_VISIBLE)
             Values
               (V_SPRSA_CODE_NEW, V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'ACCESS', 'C', 
                'KWDA', NULL, 'Y');
            
            V_SPRCA_CODE_NEW := V_SPRCA_CODE_NEW +1;
            V_SPRSA_CODE_NEW := V_SPRSA_CODE_NEW + 1;
            -- END FIVE
            
            -- BEGIN SIX
            Insert into TQC_SYS_PROCESS_AREAS
               (SPRCA_CODE, SPRCA_SPRC_CODE, SPRCA_AREA, SPRCA_SHT_DESC, SPRCA_VISIBLE)
             Values
               (V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'ESCALATIONS', 'ESC', 'Y');
                   Insert into TQC_SYS_PROCESS_SUB_AREAS
               (SPRSA_CODE, SPRSA_SPRCA_CODE, SPRSA_SPRC_CODE, SPRSA_SUB_AREA, SPRSA_TYPE, 
                SPRSA_SHT_DESC, SPRSA_DEFAULT_USR_CODE, SPRSA_VISIBLE)
             Values
               (V_SPRSA_CODE_NEW, V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'ACCESS', 'C', 
                'ESCA', NULL, 'Y');
               
            
            V_SPRCA_CODE_NEW := V_SPRCA_CODE_NEW +1;
            V_SPRSA_CODE_NEW := V_SPRSA_CODE_NEW + 1;
            --END SIX
            
            --- BEGIN SEVENTH
            Insert into TQC_SYS_PROCESS_AREAS
               (SPRCA_CODE, SPRCA_SPRC_CODE, SPRCA_AREA, SPRCA_SHT_DESC, SPRCA_VISIBLE)
             Values
               (V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'REQUEST CATEGORIES', 'RCT', 'Y');
            Insert into TQC_SYS_PROCESS_SUB_AREAS
               (SPRSA_CODE, SPRSA_SPRCA_CODE, SPRSA_SPRC_CODE, SPRSA_SUB_AREA, SPRSA_TYPE, 
                SPRSA_SHT_DESC, SPRSA_DEFAULT_USR_CODE, SPRSA_VISIBLE)
             Values
               (V_SPRSA_CODE_NEW, V_SPRCA_CODE_NEW, V_SPRC_CODE_NEW, 'ACCESS', 'C', 
                'RCTA', NULL, 'Y');
        END IF;
    COMMIT;       
END;