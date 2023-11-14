--
-- TQC_ACTIVITY_TYPES_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_ACTIVITY_TYPES_OBJ"                                          as object (
  ACTY_CODE      NUMBER(8),
  ACTY_SYS_CODE  NUMBER(8),
  ACTY_DESC      VARCHAR2(25 BYTE)
  ); 
/