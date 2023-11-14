--
-- TQC_AGENCY_SYSTEMS_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_AGENCY_SYSTEMS_OBJ"                                          as object (
  ASYS_SYS_CODE         NUMBER(8),
  ASYS_AGN_CODE         NUMBER(8),
  ASYS_WEF              DATE,
  ASYS_WET              DATE,
  ASYS_COMMENT          VARCHAR2(100 BYTE),
  ASYS_OSD_CODE         VARCHAR2(50 BYTE),
  ASYS_OSD_ID           NUMBER(8),
  ASYS_AGN_SHT_DESC     VARCHAR2(100 BYTE)
); 
/