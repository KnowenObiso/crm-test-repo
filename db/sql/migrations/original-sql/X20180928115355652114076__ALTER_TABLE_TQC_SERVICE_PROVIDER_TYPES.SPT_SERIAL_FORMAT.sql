DECLARE
   v_sql_string    VARCHAR2 (200);
   column_exist     CHAR (1);
BEGIN
   v_sql_string := 'ALTER TABLE TQ_CRM.TQC_SERVICE_PROVIDER_TYPES MODIFY(SPT_SERIAL_FORMAT VARCHAR2(100 BYTE))';

   BEGIN
      SELECT DECODE (COUNT (*), 0, 'N', 'Y') cnt
        INTO column_exist
        FROM all_tab_cols
       WHERE table_name = 'TQC_SERVICE_PROVIDER_TYPES'
         AND column_name = 'SPT_SERIAL_FORMAT'
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
      IF column_exist = 'Y'
      THEN
         EXECUTE IMMEDIATE v_sql_string;
      END IF;
   END;
END;
