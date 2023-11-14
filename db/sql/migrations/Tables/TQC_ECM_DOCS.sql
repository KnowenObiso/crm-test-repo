--
-- TQC_ECM_DOCS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ECM_DOCS
(
  ED_CODE      NUMBER,
  ED_SDT_CODE  NUMBER                           NOT NULL,
  ED_SHT_DESC  VARCHAR2(100 BYTE)               NOT NULL,
  ED_DESC      VARCHAR2(150 BYTE)               NOT NULL
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