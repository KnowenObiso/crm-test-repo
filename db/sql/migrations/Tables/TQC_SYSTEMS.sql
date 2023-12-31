--
-- TQC_SYSTEMS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYSTEMS
(
  SYS_CODE               NUMBER(8)              NOT NULL,
  SYS_SHT_DESC           VARCHAR2(15 BYTE)      NOT NULL,
  SYS_NAME               VARCHAR2(100 BYTE)     NOT NULL,
  SYS_MAIN_FORM_NAME     VARCHAR2(20 BYTE),
  SYS_ACTIVE             VARCHAR2(1 BYTE)       DEFAULT 'N',
  SYS_DBASE_USERNAME     VARCHAR2(30 BYTE),
  SYS_DBASE_PASSWORD     VARCHAR2(30 BYTE),
  SYS_DBASE_STRING       VARCHAR2(30 BYTE),
  SYS_PATH               VARCHAR2(100 BYTE),
  SYS_ORG_CODE           NUMBER(8),
  SYS_AGN_MAIN_FRM_NAME  VARCHAR2(20 BYTE),
  SYS_KBA_CODE           NUMBER(8),
  SYS_SIGNATURE_PATH     VARCHAR2(100 BYTE),
  SYS_TEMPLATE           VARCHAR2(200 BYTE),
  SYS_REPORTS_PATH       VARCHAR2(200 BYTE),
  SYS_COU_CODE           NUMBER
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