--
-- TQC_SYSTEM_SMS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYSTEM_SMS
(
  TSS_DESC      VARCHAR2(50 BYTE)               NOT NULL,
  TSS_URL       VARCHAR2(250 BYTE)              NOT NULL,
  TSS_USERNAME  VARCHAR2(50 BYTE),
  TSS_PASSWORD  VARCHAR2(50 BYTE),
  TSS_SOURCE    VARCHAR2(50 BYTE)               NOT NULL,
  TSS_CODE      NUMBER,
  TSS_DEFAULT   VARCHAR2(1 BYTE)                DEFAULT 'N'
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