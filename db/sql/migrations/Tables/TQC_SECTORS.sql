--
-- TQC_SECTORS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SECTORS
(
  SEC_CODE      NUMBER(8),
  SEC_SHT_DESC  VARCHAR2(15 BYTE)               NOT NULL,
  SEC_NAME      VARCHAR2(100 BYTE)              NOT NULL
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