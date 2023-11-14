--
-- TQC_ERROR_LOG  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ERROR_LOG
(
  ERRL_ID          INTEGER,
  ERRL_CONTEXT     VARCHAR2(500 BYTE),
  ERRL_TEXT        VARCHAR2(4000 BYTE),
  ERRL_CALL_STACK  VARCHAR2(4000 BYTE),
  ERRL_CREATED_ON  DATE,
  ERRL_CREATED_BY  VARCHAR2(100 BYTE),
  ERRL_CHANGED_ON  DATE,
  ERRL_CHANGED_BY  VARCHAR2(100 BYTE)
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