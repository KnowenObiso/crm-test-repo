--
-- TQC_INCIDENT_TYPES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_INCIDENT_TYPES
(
  INCT_CODE      NUMBER(8)                      NOT NULL,
  INCT_SHT_DESC  VARCHAR2(15 BYTE)              NOT NULL,
  INCT_DESC      VARCHAR2(100 BYTE)             NOT NULL,
  INCT_OWNER     VARCHAR2(1 BYTE)               DEFAULT 'U'                   NOT NULL
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

COMMENT ON COLUMN TQ_CRM.TQC_INCIDENT_TYPES.INCT_OWNER IS '(U)SER; (S)YSTEM;';