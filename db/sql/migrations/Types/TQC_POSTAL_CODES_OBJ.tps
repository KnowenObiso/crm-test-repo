--
-- TQC_POSTAL_CODES_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_POSTAL_CODES_OBJ"                                          as object (
  PST_CODE      NUMBER(15),
  PST_TWN_CODE  NUMBER(15),
  PST_DESC      VARCHAR2(30 BYTE),
  PST_ZIP_CODE  VARCHAR2(30 BYTE)); 
/