DECLARE
    v_sql_string1     VARCHAR2(4000);
    v_sql_string2     VARCHAR2(2000); 
    table_exist                CHAR(1);  

BEGIN
	 v_sql_string1 := 'DROP TABLE TQ_CRM.CLIENT_DATALOAD CASCADE CONSTRAINTS';
     v_sql_string2 := 'CREATE TABLE TQ_CRM.CLIENT_DATALOAD
	(
	  CLIENT_CODE       VARCHAR2(100 BYTE),
	  CLIENT_NAME       VARCHAR2(250 BYTE),
	  CLIENT_DOB        VARCHAR2(30 BYTE),
	  PHYSICAL_ADDRESS  VARCHAR2(200 BYTE),
	  POSTAL_ADDRESS    VARCHAR2(200 BYTE),
	  COUNTRY           VARCHAR2(100 BYTE),
	  TELEPHONE_NO      VARCHAR2(100 BYTE),
	  FAX               VARCHAR2(100 BYTE),
	  GENDER            VARCHAR2(5 BYTE),
	  OCCUPATION        VARCHAR2(100 BYTE),
	  ID_NO             VARCHAR2(100 BYTE),
	  PIN_NO            VARCHAR2(100 BYTE),
	  CLIENT_BANK_NAME  VARCHAR2(100 BYTE),
	  BANK_BRANCH       VARCHAR2(100 BYTE),
	  BANK_ACCT_NO      VARCHAR2(100 BYTE),
	  EMAIL             VARCHAR2(100 BYTE),
	  TOWN              VARCHAR2(100 BYTE),
	  PASSPORT_NO       VARCHAR2(100 BYTE),
	  CREDIT_ALLOWED    VARCHAR2(1 BYTE),
	  CREDIT_LIMIT      NUMBER(18,3),
	  MARITAL_STATUS    VARCHAR2(20 BYTE),
	  COMM_MODE         VARCHAR2(20 BYTE),
	  PAYMENT_MODE      VARCHAR2(20 BYTE),
	  CLIENT_TYPE       VARCHAR2(80 BYTE),
	  CLIENT_TITLE      VARCHAR2(10 BYTE),
	  BRANCH_CODE       VARCHAR2(100 BYTE)
	)';
    
-- Check if TABLE Exist
    BEGIN
        SELECT decode(COUNT(*),0,'N','Y') cnt
        INTO  table_exist
        FROM  ALL_TABLES
        WHERE TABLE_NAME = 'CLIENT_DATALOAD'
        AND   OWNER = 'TQ_CRM';
        
        EXCEPTION
        WHEN NO_DATA_FOUND
        THEN
        table_exist := 'N';
    END;
    
--  CREATE OBJECT IF THEY DON'T EXIST (N)
   BEGIN
        IF  table_exist =  'N' THEN       
             EXECUTE IMMEDIATE v_sql_string2;
        ELSIF  table_exist =  'Y' THEN
             EXECUTE IMMEDIATE v_sql_string1;
             EXECUTE IMMEDIATE v_sql_string2;         
        END IF;   
   END;
END;


