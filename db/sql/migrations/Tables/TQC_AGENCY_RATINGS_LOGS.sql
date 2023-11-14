--
-- TQC_AGENCY_RATINGS_LOGS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_RATINGS_LOGS
(
  ARL_CODE       NUMBER                         NOT NULL,
  ARL_RORG_CODE  NUMBER,
  ARL_ORS_CODE   NUMBER
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