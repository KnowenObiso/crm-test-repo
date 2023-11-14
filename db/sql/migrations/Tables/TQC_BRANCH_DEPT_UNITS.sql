--
-- TQC_BRANCH_DEPT_UNITS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BRANCH_DEPT_UNITS
(
  BDU_CODE        NUMBER(8)                     NOT NULL,
  BDU_BDEP_CODE   NUMBER(8)                     NOT NULL,
  BDU_SHT_DESC    VARCHAR2(15 BYTE)             NOT NULL,
  BDU_NAME        VARCHAR2(100 BYTE)            NOT NULL,
  BDU_SUPERVISOR  VARCHAR2(60 BYTE),
  BDU_STATUS      VARCHAR2(1 BYTE)
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