--
-- TQC_SYS_POSTS_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_SYS_POSTS_OBJ"                                          as object (
  SPOST_SYS_CODE           NUMBER(8),
  SPOST_SYSPL_CODE         NUMBER(8),
  SPOST_PARENT_SPOST_CODE  NUMBER(8),
  SPOST_CODE               NUMBER(8),
  SPOST_SHT_DESC           VARCHAR2(10 BYTE),
  SPOST_DESC               VARCHAR2(50 BYTE),
  SPOST_REMARKS            VARCHAR2(300 BYTE) ,
  SPOST_WEF                DATE ,
  SPOST_BRN_CODE           NUMBER,
  SPOST_SUBDIV_OSD_CODE    VARCHAR2(10 BYTE),
  SPOST_USR_CODE           NUMBER(8)
  ); 
/