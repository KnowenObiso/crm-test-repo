--
-- TQC_ORG_DIVISION_LEVELS_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_ORG_DIVISION_LEVELS_OBJ"                                          as object (
  ODL_CODE              VARCHAR2(10 BYTE)             ,
  ODL_DLT_CODE          VARCHAR2(5 BYTE),
  ODL_DESC              VARCHAR2(50 BYTE)     ,
  ODL_RANKING           NUMBER(2),
  ODL_TYPE              VARCHAR2(1 BYTE)); 
/