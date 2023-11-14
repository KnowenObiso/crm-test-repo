--
-- TQC_BRANCH_PRINTERS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BRANCH_PRINTERS
(
  BRP_CODE          NUMBER                      NOT NULL,
  BRP_BRN_CODE      NUMBER,
  BRP_DIV_CODE      NUMBER,
  BRP_PRINTER_NAME  VARCHAR2(100 BYTE)          NOT NULL
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