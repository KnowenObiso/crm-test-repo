--
-- TQC_USER_DIVISIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USER_DIVISIONS
(
  USD_CODE      NUMBER(22)                      NOT NULL,
  USD_USR_CODE  NUMBER(8)                       NOT NULL,
  USD_DIV_CODE  NUMBER(22)                      NOT NULL,
  USD_DEFAULT   VARCHAR2(1 BYTE)                DEFAULT 'N'                   NOT NULL
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