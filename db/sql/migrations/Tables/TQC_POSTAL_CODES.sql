--
-- TQC_POSTAL_CODES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_POSTAL_CODES
(
  PST_CODE      NUMBER(15)                      NOT NULL,
  PST_TWN_CODE  NUMBER(15),
  PST_DESC      VARCHAR2(30 BYTE),
  PST_ZIP_CODE  VARCHAR2(30 BYTE)
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