--
-- TQC_SYSTEM_OBJECTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYSTEM_OBJECTS
(
  OBJ_CODE       NUMBER(8)                      NOT NULL,
  OBJ_SYS_CODE   NUMBER(8)                      NOT NULL,
  OBJ_NAME       VARCHAR2(15 BYTE)              NOT NULL,
  OBJ_OT_CODE    NUMBER(8)                      NOT NULL,
  OBJ_HELP       VARCHAR2(2000 BYTE),
  OBJ_LABEL      VARCHAR2(100 BYTE),
  OBJ_FILE_NAME  VARCHAR2(15 BYTE)
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