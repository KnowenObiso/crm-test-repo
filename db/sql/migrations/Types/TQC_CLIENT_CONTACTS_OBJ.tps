--
-- TQC_CLIENT_CONTACTS_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_CLIENT_CONTACTS_OBJ"                                          AS OBJECT
(
CLCO_CODE                NUMBER,
CLCO_CLNT_CODE          NUMBER,
CLCO_NAME                VARCHAR2 (300),
CLCO_POSTAL_ADDRS        VARCHAR2 (300),
CLCO_PHYSICAL_ADDRS        VARCHAR2 (300),
CLCO_SEC_CODE            NUMBER

); 
/