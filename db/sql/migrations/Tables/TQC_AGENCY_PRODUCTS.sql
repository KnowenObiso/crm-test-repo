--
-- TQC_AGENCY_PRODUCTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_PRODUCTS
(
  AGNP_CODE       NUMBER,
  AGNP_AGN_CODE   NUMBER,
  AGNP_PROD_CODE  NUMBER
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