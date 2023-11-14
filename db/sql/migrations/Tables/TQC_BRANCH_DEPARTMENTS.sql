--
-- TQC_BRANCH_DEPARTMENTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BRANCH_DEPARTMENTS
(
  BDEP_CODE       NUMBER(8)                     NOT NULL,
  BDEP_BRN_CODE   NUMBER(8)                     NOT NULL,
  BDEP_DEP_CODE   NUMBER(8)                     NOT NULL,
  BDEP_DEPT_HEAD  VARCHAR2(60 BYTE),
  BDEP_WEF        DATE                          NOT NULL,
  BDEP_WET        DATE
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