--
-- JBPM4_VARIABLE  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_VARIABLE
(
  DBID_          NUMBER(19)                     NOT NULL,
  CLASS_         VARCHAR2(255 BYTE)             NOT NULL,
  DBVERSION_     NUMBER(10)                     NOT NULL,
  KEY_           VARCHAR2(255 BYTE),
  CONVERTER_     VARCHAR2(255 BYTE),
  HIST_          NUMBER(1),
  EXECUTION_     NUMBER(19),
  TASK_          NUMBER(19),
  LOB_           NUMBER(19),
  DATE_VALUE_    DATE,
  DOUBLE_VALUE_  FLOAT(126),
  CLASSNAME_     VARCHAR2(255 BYTE),
  LONG_VALUE_    NUMBER(19),
  STRING_VALUE_  VARCHAR2(255 BYTE),
  TEXT_VALUE_    CLOB,
  EXESYS_        NUMBER(19)
)
TABLESPACE CRMDATA
PCTUSED    40
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          2M
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;