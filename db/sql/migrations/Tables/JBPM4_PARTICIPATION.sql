--
-- JBPM4_PARTICIPATION  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_PARTICIPATION
(
  DBID_       NUMBER(19)                        NOT NULL,
  DBVERSION_  NUMBER(10)                        NOT NULL,
  GROUPID_    VARCHAR2(255 BYTE),
  USERID_     VARCHAR2(255 BYTE),
  TYPE_       VARCHAR2(255 BYTE),
  TASK_       NUMBER(19),
  SWIMLANE_   NUMBER(19)
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