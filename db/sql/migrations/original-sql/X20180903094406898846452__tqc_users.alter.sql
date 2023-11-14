BEGIN
-- Start alter table's definition TQC_USERS --
-- Start alter's definition usr_authorized --
DECLARE
   v_sql  VARCHAR2(4000);
   column_exist CHAR (1);
BEGIN
   v_sql := 'ALTER TABLE TQ_CRM.TQC_USERS ADD (USR_AUTHORIZED VARCHAR2(1))';
  
   BEGIN
      SELECT DECODE (COUNT(*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_USERS'
         AND column_name = 'USR_AUTHORIZED'
         AND owner = 'TQ_CRM';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         column_exist := 'N';
      WHEN OTHERS
      THEN
         column_exist := 'Y';
   END;
   
    BEGIN
        IF column_exist =  'N' THEN
            EXECUTE IMMEDIATE v_sql;
            COMMIT;
        END IF;
    EXCEPTION WHEN OTHERS
    THEN
         NULL;
    END;
END;
-- end alter's definition usr_updated_by 
-- Start alter's definition usr_authorized_by --
DECLARE
   v_sql  VARCHAR2(4000);
   column_exist CHAR (1);
BEGIN
   v_sql := 'ALTER TABLE TQ_CRM.TQC_USERS ADD (USR_AUTHORIZED_BY VARCHAR2(50))';
  
   BEGIN
      SELECT DECODE (COUNT(*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_USERS'
         AND column_name = 'USR_AUTHORIZED_BY'
         AND owner = 'TQ_CRM';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         column_exist := 'N';
      WHEN OTHERS
      THEN
         column_exist := 'Y';
   END;
   
    BEGIN
        IF column_exist =  'N' THEN
            EXECUTE IMMEDIATE v_sql;
            COMMIT;
        END IF;
    EXCEPTION WHEN OTHERS
    THEN
         NULL;
    END;
END;
-- end alter's definition usr_authorized_by 
-- Start alter's definition usr_authorized_date --
DECLARE
   v_sql  VARCHAR2(4000);
   column_exist CHAR (1);
BEGIN
   v_sql := 'ALTER TABLE TQ_CRM.TQC_USERS ADD (USR_AUTHORIZED_DATE DATE)';
  
   BEGIN
      SELECT DECODE (COUNT(*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_USERS'
         AND column_name = 'USR_AUTHORIZED_DATE'
         AND owner = 'TQ_CRM';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         column_exist := 'N';
      WHEN OTHERS
      THEN
         column_exist := 'Y';
   END;
   
    BEGIN
        IF column_exist =  'N' THEN
            EXECUTE IMMEDIATE v_sql;
            COMMIT;
        END IF;
    EXCEPTION WHEN OTHERS
    THEN
         NULL;
    END;
END;
-- end alter's definition usr_authorized_date 
-- Start alter's definition usr_deactivated_by --
DECLARE
   v_sql  VARCHAR2(4000);
   column_exist CHAR (1);
BEGIN
   v_sql := 'ALTER TABLE TQ_CRM.TQC_USERS ADD (USR_DEACTIVATED_BY VARCHAR2(50))';
  
   BEGIN
      SELECT DECODE (COUNT(*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_USERS'
         AND column_name = 'USR_DEACTIVATED_BY'
         AND owner = 'TQ_CRM';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         column_exist := 'N';
      WHEN OTHERS
      THEN
         column_exist := 'Y';
   END;
   
    BEGIN
        IF column_exist =  'N' THEN
            EXECUTE IMMEDIATE v_sql;
            COMMIT;
        END IF;
    EXCEPTION WHEN OTHERS
    THEN
         NULL;
    END;
END;
-- end alter's definition usr_deactivated_by 
-- Start alter's definition usr_supervisor_code --
DECLARE
   v_sql  VARCHAR2(4000);
   column_exist CHAR (1);
BEGIN
   v_sql := 'ALTER TABLE TQ_CRM.TQC_USERS ADD (USR_SUPERVISOR_CODE NUMBER(22))';
  
   BEGIN
      SELECT DECODE (COUNT(*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_USERS'
         AND column_name = 'USR_SUPERVISOR_CODE'
         AND owner = 'TQ_CRM';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         column_exist := 'N';
      WHEN OTHERS
      THEN
         column_exist := 'Y';
   END;
   
    BEGIN
        IF column_exist =  'N' THEN
            EXECUTE IMMEDIATE v_sql;
            COMMIT;
        END IF;
    EXCEPTION WHEN OTHERS
    THEN
         NULL;
    END;
END;
-- end alter's definition usr_supervisor_code 
-- Start alter's definition usr_deleted --
DECLARE
   v_sql  VARCHAR2(4000);
   column_exist CHAR (1);
BEGIN
   v_sql := 'ALTER TABLE TQ_CRM.TQC_USERS ADD (USR_DELETED VARCHAR2(1))';
  
   BEGIN
      SELECT DECODE (COUNT(*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_USERS'
         AND column_name = 'USR_DELETED'
         AND owner = 'TQ_CRM';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         column_exist := 'N';
      WHEN OTHERS
      THEN
         column_exist := 'Y';
   END;
   
    BEGIN
        IF column_exist =  'N' THEN
            EXECUTE IMMEDIATE v_sql;
            COMMIT;
        END IF;
    EXCEPTION WHEN OTHERS
    THEN
         NULL;
    END;
END;
-- end alter's definition usr_deleted 
-- Start alter's definition usr_deactivated_on --
DECLARE
   v_sql  VARCHAR2(4000);
   column_exist CHAR (1);
BEGIN
   v_sql := 'ALTER TABLE TQ_CRM.TQC_USERS ADD (USR_DEACTIVATED_ON DATE)';
  
   BEGIN
      SELECT DECODE (COUNT(*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_USERS'
         AND column_name = 'USR_DEACTIVATED_ON'
         AND owner = 'TQ_CRM';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         column_exist := 'N';
      WHEN OTHERS
      THEN
         column_exist := 'Y';
   END;
   
    BEGIN
        IF column_exist =  'N' THEN
            EXECUTE IMMEDIATE v_sql;
            COMMIT;
        END IF;
    EXCEPTION WHEN OTHERS
    THEN
         NULL;
    END;
END;
-- end alter's definition usr_deactivated_on 
-- Start alter's definition usr_activated_on --
DECLARE
   v_sql  VARCHAR2(4000);
   column_exist CHAR (1);
BEGIN
   v_sql := 'ALTER TABLE TQ_CRM.TQC_USERS ADD (USR_ACTIVATED_ON DATE)';
  
   BEGIN
      SELECT DECODE (COUNT(*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_USERS'
         AND column_name = 'USR_ACTIVATED_ON'
         AND owner = 'TQ_CRM';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         column_exist := 'N';
      WHEN OTHERS
      THEN
         column_exist := 'Y';
   END;
   
    BEGIN
        IF column_exist =  'N' THEN
            EXECUTE IMMEDIATE v_sql;
            COMMIT;
        END IF;
    EXCEPTION WHEN OTHERS
    THEN
         NULL;
    END;
END;
-- end alter's definition usr_activated_on 
-- Start alter's definition usr_activated_by --
DECLARE
   v_sql  VARCHAR2(4000);
   column_exist CHAR (1);
BEGIN
   v_sql := 'ALTER TABLE TQ_CRM.TQC_USERS ADD (USR_ACTIVATED_BY VARCHAR2(100))';
  
   BEGIN
      SELECT DECODE (COUNT(*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_USERS'
         AND column_name = 'USR_ACTIVATED_BY'
         AND owner = 'TQ_CRM';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         column_exist := 'N';
      WHEN OTHERS
      THEN
         column_exist := 'Y';
   END;
   
    BEGIN
        IF column_exist =  'N' THEN
            EXECUTE IMMEDIATE v_sql;
            COMMIT;
        END IF;
    EXCEPTION WHEN OTHERS
    THEN
         NULL;
    END;
END;
-- end alter's definition usr_activated_by 
END;
