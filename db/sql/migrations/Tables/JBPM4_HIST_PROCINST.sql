--
-- JBPM4_HIST_PROCINST  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_HIST_PROCINST
(
  DBID_         NUMBER(19)                      NOT NULL,
  DBVERSION_    NUMBER(10)                      NOT NULL,
  ID_           VARCHAR2(255 BYTE),
  PROCDEFID_    VARCHAR2(255 BYTE),
  KEY_          VARCHAR2(255 BYTE),
  START_        DATE,
  END_          DATE,
  DURATION_     NUMBER(19),
  STATE_        VARCHAR2(255 BYTE),
  ENDACTIVITY_  VARCHAR2(255 BYTE),
  NEXTIDX_      NUMBER(10)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          576K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;