DECLARE
   isinsertexisting   CHAR;
   v_insert1          VARCHAR2 (2000);
   v_insert2          VARCHAR2 (2000);
BEGIN
   v_insert1 :=
      'Insert into TQ_CRM.TQC_PARAMETERS
   (PARAM_CODE, PARAM_NAME, PARAM_VALUE, PARAM_STATUS, PARAM_DESC, 
    MAIL_AGENT_PWD)
 Values
   (1344, ''AGENT_LOADIND_TEMP_PATH'', ''D://CRM_PROJECT/turnquest-crmbk/CRM_0/public_html/Reports/template.csv'', ''ACTIVE'', NULL, 
    NULL)';
    
     v_insert2 :=
      'Insert into TQ_CRM.TQC_PARAMETERS
   (PARAM_CODE, PARAM_NAME, PARAM_VALUE, PARAM_STATUS, PARAM_DESC, 
    MAIL_AGENT_PWD)
 Values
   (34347006660, ''CLIENT_LOADING_TEMP_PATH'', ''D://CRM_PROJECT/turnquest-crmbk/CRM_0/public_html/Reports/clientLoadingTemplate.csv'', ''ACTIVE'', NULL, 
    NULL)';

   BEGIN
      SELECT DECODE (COUNT (*), 0, 'N', 'Y')
        INTO isinsertexisting
        FROM tq_crm.tqc_parameters
       WHERE param_name = 'AGENT_LOADIND_TEMP_PATH';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         isinsertexisting := 'N';
   END;

   BEGIN
      IF isinsertexisting = 'N'
      THEN
         EXECUTE IMMEDIATE v_insert1;
         commit;
      END IF;
   END;
   
    BEGIN
      SELECT DECODE (COUNT (*), 0, 'N', 'Y')
        INTO isinsertexisting
        FROM tq_crm.tqc_parameters
       WHERE param_name = 'CLIENT_LOADING_TEMP_PATH';
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         isinsertexisting := 'N';
   END;

   BEGIN
      IF isinsertexisting = 'N'
      THEN
         EXECUTE IMMEDIATE v_insert2;
         commit;
      END IF;
   END;
END;
