--
-- TQC_ORG_SUBDIV_PREV_HEADS_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_ORG_SUBDIV_PREV_HEADS_OBJ"                                          as object (
  OSDPH_CODE               NUMBER(8)             ,
  OSDPH_OSD_CODE           VARCHAR2(10 BYTE),
  OSDPH_PREV_AGN_CODE           NUMBER(8)      ,
  OSDPH_WET               DATE,
  OSDPH_OSD_ID             NUMBER(8)); 
/