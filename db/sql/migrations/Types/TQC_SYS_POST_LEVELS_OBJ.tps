--
-- TQC_SYS_POST_LEVELS_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_SYS_POST_LEVELS_OBJ"                                          as object (
  SYSPL_SYS_CODE  NUMBER(8),
  SYSPL_CODE      NUMBER(8),
  SYSPL_SHT_DESC  VARCHAR2(10 BYTE),
  SYSPL_DESC      VARCHAR2(50 BYTE),
  SYSPL_RANKING   NUMBER(3),
  SYSPL_WEF       DATE
  ); 
/