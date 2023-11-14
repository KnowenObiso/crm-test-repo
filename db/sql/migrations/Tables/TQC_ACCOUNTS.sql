--
-- TQC_ACCOUNTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ACCOUNTS
(
  ACC_CODE       NUMBER,
  ACC_TYPE       VARCHAR2(5 BYTE)               NOT NULL,
  ACC_TYPE_CODE  NUMBER                         NOT NULL
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