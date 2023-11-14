--
-- TQC_SYSTEM_ROLES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYSTEM_ROLES
(
  SRL_CODE      NUMBER(8)                       NOT NULL,
  SRL_SYS_CODE  NUMBER(8)                       NOT NULL,
  SRL_NAME      VARCHAR2(100 BYTE),
  SRL_CRT_DATE  DATE,
  SRL_SHT_DESC  VARCHAR2(15 BYTE)               NOT NULL,
  SRL_STATUS    VARCHAR2(1 BYTE)                NOT NULL
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