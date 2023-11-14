
DROP TYPE TQ_CRM.TQC_MSG_TEMPLATES_TAB;

DROP TYPE TQ_CRM.TQC_MSG_TEMPLATES_OBJ;

CREATE OR REPLACE TYPE TQ_CRM."TQC_MSG_TEMPLATES_OBJ" as object (
  MSGT_CODE        NUMBER(5),
  MSGT_SHT_DESC    VARCHAR2(15 BYTE),
  MSGT_MSG         VARCHAR2(160 BYTE),
  MSGT_SYS_CODE    NUMBER(8),
  MSGT_SYS_MODULE  VARCHAR2(2 BYTE),
  MSGT_TYPE        VARCHAR2(5 BYTE),
  MSGT_UPDATED_BY  VARCHAR2(160 BYTE),
  MSGT_CREATED_BY  VARCHAR2(160 BYTE),
  MSGT_PROD_CODE    NUMBER(8),
  MSGT_SUBJECT  VARCHAR2(50 BYTE)--Introduced to handle message subject on message templates screen
  );
  CREATE OR REPLACE TYPE TQ_CRM."TQC_MSG_TEMPLATES_TAB" as table of TQC_MSG_TEMPLATES_OBJ;


