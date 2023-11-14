--
-- TQC_ERROR  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ERROR
(
  ERR_ID                   INTEGER,
  ERR_ERROR_CATEGORY_NAME  VARCHAR2(500 BYTE),
  ERR_CODE                 INTEGER,
  ERR_NAME                 VARCHAR2(500 BYTE),
  ERR_DESCRIPTION          VARCHAR2(4000 BYTE),
  ERR_SUBSTITUTE_STRING    VARCHAR2(4000 BYTE),
  ERR_RECOMMENDATION       VARCHAR2(4000 BYTE),
  ERR_CREATED_ON           DATE,
  ERR_CREATED_BY           VARCHAR2(100 BYTE),
  ERR_CHANGED_ON           DATE,
  ERR_CHANGED_BY           VARCHAR2(100 BYTE),
  ERR_SYS_CODE             NUMBER(8),
  ERR_SERRM_CODE           NUMBER(8)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          128K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;

COMMENT ON TABLE TQ_CRM.TQC_ERROR IS 'The set of pre-defined errors copied from sa_error when app was deployed';