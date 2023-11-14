--
-- TQC_MEMOS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_MEMOS
(
  MEMO_CODE       NUMBER(22)                    NOT NULL,
  MEMO_SUBJECT    VARCHAR2(200 BYTE)            NOT NULL,
  MEMO_MTYP_CODE  NUMBER(15)                    NOT NULL
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