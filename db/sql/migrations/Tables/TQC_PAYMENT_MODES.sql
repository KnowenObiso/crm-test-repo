--
-- TQC_PAYMENT_MODES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_PAYMENT_MODES
(
  PMOD_CODE      NUMBER(15)                     NOT NULL,
  PMOD_SHT_DESC  VARCHAR2(5 BYTE)               NOT NULL,
  PMOD_DESC      VARCHAR2(50 BYTE)              NOT NULL,
  PMOD_NARATION  VARCHAR2(250 BYTE),
  PMOD_DEFAULT   VARCHAR2(2 BYTE)               DEFAULT 'N'
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