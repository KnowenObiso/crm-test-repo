--
-- TQC_WEB_SYSTEM_AUTH_AREAS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_WEB_SYSTEM_AUTH_AREAS
(
  SAA_CODE      NUMBER(8),
  SAA_SYS_CODE  NUMBER(8)                       NOT NULL,
  SAA_SHT_DESC  VARCHAR2(15 BYTE)               NOT NULL,
  SAA_NAME      VARCHAR2(50 BYTE)               NOT NULL
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