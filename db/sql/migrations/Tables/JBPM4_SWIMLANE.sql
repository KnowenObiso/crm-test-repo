--
-- JBPM4_SWIMLANE  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_SWIMLANE
(
  DBID_       NUMBER(19)                        NOT NULL,
  DBVERSION_  NUMBER(10)                        NOT NULL,
  NAME_       VARCHAR2(255 BYTE),
  ASSIGNEE_   VARCHAR2(255 BYTE),
  EXECUTION_  NUMBER(19)
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