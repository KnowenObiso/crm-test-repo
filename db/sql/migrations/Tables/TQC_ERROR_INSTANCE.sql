--
-- TQC_ERROR_INSTANCE  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ERROR_INSTANCE
(
  ERRI_ID                    INTEGER,
  ERRI_ERR_ID                INTEGER,
  ERRI_ERROR_STACK           VARCHAR2(4000 BYTE),
  ERRI_CALL_STACK            VARCHAR2(4000 BYTE),
  ERRI_MESSAGE               VARCHAR2(4000 BYTE),
  ERRI_SYSTEM_ERROR_CODE     INTEGER,
  ERRI_SYSTEM_ERROR_MESSAGE  VARCHAR2(4000 BYTE),
  ERRI_ENVIRONMENT_INFO      VARCHAR2(4000 BYTE),
  ERRI_CREATED_ON            DATE,
  ERRI_CREATED_BY            VARCHAR2(100 BYTE),
  ERRI_CHANGED_ON            DATE,
  ERRI_CHANGED_BY            VARCHAR2(100 BYTE)
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

COMMENT ON TABLE TQ_CRM.TQC_ERROR_INSTANCE IS 'A particular instance of an error that occurs in the application';