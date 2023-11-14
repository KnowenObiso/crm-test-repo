--
-- TQC_REPORT_GROUPS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_REPORT_GROUPS
(
  TRG_CODE  NUMBER(8),
  TRG_NAME  VARCHAR2(50 BYTE)
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