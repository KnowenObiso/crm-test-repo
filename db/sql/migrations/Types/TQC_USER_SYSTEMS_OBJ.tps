--
-- TQC_USER_SYSTEMS_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_USER_SYSTEMS_OBJ"                                          as object (
  USYS_CODE        NUMBER(8),
  USYS_USR_CODE    NUMBER(8),
  USYS_SYS_CODE    NUMBER(8),
  USYS_WEF         DATE,
  USYS_WET         DATE,
  USYS_SPOST_CODE  NUMBER(8)
  ); 
/