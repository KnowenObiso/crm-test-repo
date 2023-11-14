--
-- TQC_ORG_DIV_LEVELS_TYPE_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_ORG_DIV_LEVELS_TYPE_OBJ"                                          as object (
  DLT_CODE              VARCHAR2(5 BYTE),
  DLT_SYS_CODE          NUMBER(8),
  DLT_DESC              VARCHAR2(25 BYTE),
  DLT_ACT_CODE          NUMBER(8)); 
/