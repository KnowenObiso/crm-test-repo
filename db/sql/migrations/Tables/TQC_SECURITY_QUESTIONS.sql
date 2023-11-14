--
-- TQC_SECURITY_QUESTIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SECURITY_QUESTIONS
(
  SECQ_CODE      NUMBER                         NOT NULL,
  SECQ_SHT_DESC  VARCHAR2(200 BYTE),
  SECQ_DESC      VARCHAR2(200 BYTE)
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