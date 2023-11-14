--
-- PCA_TCA_CODE_IDX  (Index) 
--
CREATE INDEX TQ_CRM.PCA_TCA_CODE_IDX ON TQ_CRM.TQC_PRODUCT_CLIENT_ATTRIBUTES
(PCA_TCA_CODE)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
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
           );