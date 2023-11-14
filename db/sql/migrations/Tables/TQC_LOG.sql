--
-- TQC_LOG  (Table) 
--
CREATE TABLE TQ_CRM.TQC_LOG
(
  LOG_DATE  DATE                                NOT NULL,
  LOG_TEXT  CLOB,
  LOG_SYS   NUMBER                              DEFAULT 0                     NOT NULL,
  LOG_TYPE  VARCHAR2(20 BYTE)                   DEFAULT 'SYS'                 NOT NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          192K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;