--
-- TQC_INC_DEPARTMENTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_INC_DEPARTMENTS
(
  IDEP_CODE          NUMBER(15)                 NOT NULL,
  IDEP_SHT_DESC      VARCHAR2(15 BYTE)          NOT NULL,
  IDEP_DESC          VARCHAR2(25 BYTE)          NOT NULL,
  IDEP_SYS_CODE      NUMBER(8)                  NOT NULL,
  IDEP_BRN_CODE      NUMBER(8),
  IDEP_BRN_SHT_DESC  VARCHAR2(15 BYTE)
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