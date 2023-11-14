--
-- TQC_SUBDIVISIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SUBDIVISIONS
(
  SDIV_CODE      NUMBER(22)                     NOT NULL,
  SDIV_NAME      VARCHAR2(50 BYTE)              NOT NULL,
  SDIV_SHT_DESC  VARCHAR2(30 BYTE),
  SDIV_DIV_CODE  NUMBER(23)
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