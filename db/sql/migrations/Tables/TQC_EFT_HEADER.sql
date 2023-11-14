--
-- TQC_EFT_HEADER  (Table) 
--
CREATE TABLE TQ_CRM.TQC_EFT_HEADER
(
  EH_CODE      NUMBER(8)                        NOT NULL,
  EH_BOOK_DT   DATE                             NOT NULL,
  EH_REF_NO    VARCHAR2(40 BYTE)                NOT NULL,
  EH_OUR_ACNT  VARCHAR2(40 BYTE)                NOT NULL,
  EH_DESC      VARCHAR2(200 BYTE)               NOT NULL,
  EH_SENT_YN   VARCHAR2(1 BYTE)                 NOT NULL,
  EH_FM_DT     DATE,
  EH_TO_DT     DATE
)
TABLESPACE CRMDATA
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          128K
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