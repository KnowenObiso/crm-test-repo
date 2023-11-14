--
-- TQC_AGENCY_SERVICE_PROVIDERS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_SERVICE_PROVIDERS
(
  AGNT_CODE      NUMBER,
  AGNT_SPR_CODE  NUMBER                         NOT NULL,
  AGNT_AGN_CODE  NUMBER                         NOT NULL
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