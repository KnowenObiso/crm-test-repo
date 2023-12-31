--
-- TQC_PRODUCT_ATTRIBUTES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_PRODUCT_ATTRIBUTES
(
  TPA_CODE            NUMBER,
  TPA_SYSTEM          NUMBER,
  TPA_PROD_CODE       NUMBER,
  TPA_PROD_SHTDESC    VARCHAR2(100 BYTE),
  TPA_PROD_DESC       VARCHAR2(300 BYTE),
  TPA_PROD_NARRATION  VARCHAR2(500 BYTE)
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