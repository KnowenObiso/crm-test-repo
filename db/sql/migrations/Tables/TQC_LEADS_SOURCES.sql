--
-- TQC_LEADS_SOURCES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_LEADS_SOURCES
(
  LDSRC_CODE  NUMBER(8),
  LDSRC_DESC  VARCHAR2(25 BYTE)
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