--
-- TQC_SYS_PRCSS_SUBAREA_LVLS_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_SYS_PRCSS_SUBAREA_LVLS_OBJ"                                          as object (
  SPSAL_CODE           NUMBER(8),
  SPSAL_SPRSA_CODE     NUMBER(8),
  SPSAL_SPSAT_CODE     NUMBER(8),
  SPSAL_LEVEL          NUMBER(5),
  SPSAL_APPROVER_TYPE  VARCHAR2(5 BYTE),
  SPSAL_APPROVER_ID    NUMBER(8)
  ); 
/