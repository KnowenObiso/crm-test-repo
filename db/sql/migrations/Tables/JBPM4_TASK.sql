--
-- JBPM4_TASK  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_TASK
(
  DBID_           NUMBER(19)                    NOT NULL,
  CLASS_          CHAR(1 BYTE)                  NOT NULL,
  DBVERSION_      NUMBER(10)                    NOT NULL,
  NAME_           VARCHAR2(255 BYTE),
  DESCR_          CLOB,
  STATE_          VARCHAR2(255 BYTE),
  SUSPHISTSTATE_  VARCHAR2(255 BYTE),
  ASSIGNEE_       VARCHAR2(255 BYTE),
  FORM_           VARCHAR2(255 BYTE),
  PRIORITY_       NUMBER(10),
  CREATE_         DATE,
  DUEDATE_        DATE,
  PROGRESS_       NUMBER(10),
  SIGNALLING_     NUMBER(1),
  EXECUTION_ID_   VARCHAR2(255 BYTE),
  ACTIVITY_NAME_  VARCHAR2(255 BYTE),
  HASVARS_        NUMBER(1),
  SUPERTASK_      NUMBER(19),
  EXECUTION_      NUMBER(19),
  PROCINST_       NUMBER(19),
  SWIMLANE_       NUMBER(19),
  TASKDEFNAME_    VARCHAR2(255 BYTE)
)
TABLESPACE CRMDATA
PCTUSED    40
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          384K
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