--
-- TQC_MENU  (Table) 
--
CREATE TABLE TQ_CRM.TQC_MENU
(
  MENU_CODE           NUMBER(8)                 NOT NULL,
  MENU_SYS_CODE       NUMBER(8)                 NOT NULL,
  MENU_NAME           VARCHAR2(50 BYTE)         NOT NULL,
  MENU_LABEL          VARCHAR2(50 BYTE),
  MENU_TYPE           VARCHAR2(1 BYTE)          NOT NULL,
  MENU_OBJ_CODE       NUMBER(8),
  MENU_MENU_CODE      NUMBER(8),
  MENU_ORG_TYPE_SHOW  VARCHAR2(3 BYTE)          DEFAULT 'INS',
  MENU_BRK_LABEL      VARCHAR2(50 BYTE)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          256K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;

COMMENT ON COLUMN TQ_CRM.TQC_MENU.MENU_ORG_TYPE_SHOW IS '''INS'' for Insurance, ''BRK'' for Broker, ''BNK'' for bancassurance, ''ALL'' for All ';