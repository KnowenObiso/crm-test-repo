--
-- TQC_DEPARTMENTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_DEPARTMENTS
(
  DEP_CODE      NUMBER(8)                       NOT NULL,
  DEP_SHT_DESC  VARCHAR2(20 BYTE)               NOT NULL,
  DEP_NAME      VARCHAR2(50 BYTE)               NOT NULL,
  DEP_WEF       DATE                            NOT NULL,
  DEP_WET       DATE,
  DEP_USR_CODE  NUMBER
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