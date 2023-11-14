--
-- TQC_MEMO_HEADER  (Table) 
--
CREATE TABLE TQ_CRM.TQC_MEMO_HEADER
(
  MEMH_CODE          NUMBER(22)                 NOT NULL,
  MEMH_COMEM_CODE    NUMBER(15)                 NOT NULL,
  MEMH_MEMO_CODE     NUMBER(22),
  MEMH_MEMO_SUBJECT  VARCHAR2(3000 BYTE)
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