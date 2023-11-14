--
-- TQC_STATUSES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_STATUSES
(
  STATUS_ID    NUMBER                           NOT NULL,
  STATUS_CODE  VARCHAR2(10 BYTE)                NOT NULL,
  STATUS_DESC  VARCHAR2(20 BYTE)                NOT NULL
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