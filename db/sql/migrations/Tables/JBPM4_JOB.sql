--
-- JBPM4_JOB  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_JOB
(
  DBID_             NUMBER(19)                  NOT NULL,
  CLASS_            VARCHAR2(255 BYTE)          NOT NULL,
  DBVERSION_        NUMBER(10)                  NOT NULL,
  DUEDATE_          DATE,
  STATE_            VARCHAR2(255 BYTE),
  ISEXCLUSIVE_      NUMBER(1),
  LOCKOWNER_        VARCHAR2(255 BYTE),
  LOCKEXPTIME_      DATE,
  EXCEPTION_        CLOB,
  RETRIES_          NUMBER(10),
  PROCESSINSTANCE_  NUMBER(19),
  EXECUTION_        NUMBER(19),
  CFG_              NUMBER(19),
  SIGNAL_           VARCHAR2(255 BYTE),
  EVENT_            VARCHAR2(255 BYTE),
  REPEAT_           VARCHAR2(255 BYTE)
)
TABLESPACE CRMDATA
PCTUSED    40
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
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;