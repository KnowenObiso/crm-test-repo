--
-- JBPM4_EXECUTION  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_EXECUTION
(
  DBID_           NUMBER(19)                    NOT NULL,
  CLASS_          VARCHAR2(255 BYTE)            NOT NULL,
  DBVERSION_      NUMBER(10)                    NOT NULL,
  ACTIVITYNAME_   VARCHAR2(255 BYTE),
  PROCDEFID_      VARCHAR2(255 BYTE),
  HASVARS_        NUMBER(1),
  NAME_           VARCHAR2(255 BYTE),
  KEY_            VARCHAR2(255 BYTE),
  ID_             VARCHAR2(255 BYTE),
  STATE_          VARCHAR2(255 BYTE),
  SUSPHISTSTATE_  VARCHAR2(255 BYTE),
  PRIORITY_       NUMBER(10),
  HISACTINST_     NUMBER(19),
  PARENT_         NUMBER(19),
  INSTANCE_       NUMBER(19),
  SUPEREXEC_      NUMBER(19),
  SUBPROCINST_    NUMBER(19),
  PARENT_IDX_     NUMBER(10)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          768K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;