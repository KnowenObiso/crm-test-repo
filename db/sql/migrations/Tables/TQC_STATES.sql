--
-- TQC_STATES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_STATES
(
  STS_CODE      NUMBER(8)                       NOT NULL,
  STS_COU_CODE  NUMBER(8)                       NOT NULL,
  STS_SHT_DESC  VARCHAR2(15 BYTE),
  STS_NAME      VARCHAR2(50 BYTE)               NOT NULL
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