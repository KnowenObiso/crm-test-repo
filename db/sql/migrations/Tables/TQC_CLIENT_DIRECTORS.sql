--
-- TQC_CLIENT_DIRECTORS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_DIRECTORS
(
  CLNTDIR_CODE            NUMBER(8),
  CLNTDIR_CLNT_CODE       NUMBER(15)            NOT NULL,
  CLNTDIR_YEAR            NUMBER(4)             NOT NULL,
  CLNTDIR_NAME            VARCHAR2(100 BYTE)    NOT NULL,
  CLNTDIR_QUALIFICATIONS  VARCHAR2(300 BYTE),
  CLNTDIR_PCT_HOLDG       NUMBER(8,4),
  CLNTDIR_DESIGNATION     VARCHAR2(20 BYTE)
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