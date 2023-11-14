--
-- TQC_DIVISIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_DIVISIONS
(
  DIV_CODE             NUMBER(22)               NOT NULL,
  DIV_NAME             VARCHAR2(50 BYTE)        NOT NULL,
  DIV_SHT_DESC         VARCHAR2(30 BYTE),
  DIV_DIVISION_STATUS  VARCHAR2(1 BYTE)         DEFAULT 'A',
  DIV_ORG_CODE         NUMBER(15)               DEFAULT null                  NOT NULL,
  DIV_ORDER            NUMBER(8),
  DIV_ASST_DIRECTOR    NUMBER(8),
  DIV_DIRECTOR         NUMBER(8)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;

COMMENT ON COLUMN TQ_CRM.TQC_DIVISIONS.DIV_ASST_DIRECTOR IS 'Division Assistant director or deputy Head';

COMMENT ON COLUMN TQ_CRM.TQC_DIVISIONS.DIV_DIRECTOR IS 'Division director or the head';