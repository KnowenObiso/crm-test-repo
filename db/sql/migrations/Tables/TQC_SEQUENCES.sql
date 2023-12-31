--
-- TQC_SEQUENCES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SEQUENCES
(
  TSEQ_CODE       NUMBER(15),
  TSEQ_TYPE       VARCHAR2(3 BYTE),
  TSEQ_NO_TYPE    VARCHAR2(3 BYTE),
  TSEQ_BRN        NUMBER(10),
  TSEQ_UWYR       NUMBER(10),
  TSEQ_NEXT_NO    NUMBER(20),
  TSEQ_CLNT_TYPE  VARCHAR2(3 BYTE)              DEFAULT 'I',
  TSEQ_ORG_CODE   NUMBER
)
TABLESPACE CRMDATA
PCTUSED    40
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
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;