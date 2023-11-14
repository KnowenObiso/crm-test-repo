--
-- JBPM4_HIST_ACTINST  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_HIST_ACTINST
(
  DBID_           NUMBER(19)                    NOT NULL,
  CLASS_          VARCHAR2(255 BYTE)            NOT NULL,
  DBVERSION_      NUMBER(10)                    NOT NULL,
  HPROCI_         NUMBER(19),
  TYPE_           VARCHAR2(255 BYTE),
  EXECUTION_      VARCHAR2(255 BYTE),
  ACTIVITY_NAME_  VARCHAR2(255 BYTE),
  START_          DATE,
  END_            DATE,
  DURATION_       NUMBER(19),
  TRANSITION_     VARCHAR2(255 BYTE),
  NEXTIDX_        NUMBER(10),
  HTASK_          NUMBER(19)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          4M
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;