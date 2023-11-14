--
-- TQC_INCI_ACTION_TYPES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_INCI_ACTION_TYPES
(
  ICTY_CODE      NUMBER(8)                      NOT NULL,
  ICTY_SHT_DESC  VARCHAR2(15 BYTE)              NOT NULL,
  ICTY_DESC      VARCHAR2(25 BYTE),
  ICTY_DATE      DATE
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