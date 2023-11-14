--
-- TQC_CLNT_LOADING_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_CLNT_LOADING_OBJ"                                          AS OBJECT
( 
   CLN_CLNT_SHT_DESC             VARCHAR2(200),  
   CLN_CLNT_TWN_CODE             VARCHAR2(200),  
   CLN_CLNT_NAME                 VARCHAR2(200),
   CLN_CLNT_OTHER_NAMES          VARCHAR2(200),
   CLN_CLNT_POSTAL_ADDRS         VARCHAR2(200),
   CLN_CLNT_PHYSICAL_ADDRS       VARCHAR2(200),
   CLN_CLNT_TEL                  VARCHAR2(200),
   CLN_CLNT_TEL2                 VARCHAR2(200),
   CLN_CLNT_FAX                  VARCHAR2(200),
   CLN_CLNT_CNTCT_EMAIL_1        VARCHAR2(200),
   CLN_CLNT_ID_REG_NO            VARCHAR2(200),
   CLN_CLNT_DOB                  DATE
); 
/