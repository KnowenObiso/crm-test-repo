--
-- TQC_AUTHORIZATION_LEVELS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AUTHORIZATION_LEVELS
(
  TQAL_CODE        NUMBER                       NOT NULL,
  TQAL_SPRSA_CODE  NUMBER                       NOT NULL,
  TQAL_LEVEL_ID    NUMBER                       NOT NULL,
  TQAL_SRLS_CODE   NUMBER                       NOT NULL
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