--
-- TQC_LEADS_ACTIVITIES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_LEADS_ACTIVITIES
(
  LACTS_CODE      NUMBER(8)                     NOT NULL,
  LACTS_LDS_CODE  NUMBER(8),
  LACTS_ACT_CODE  NUMBER(8)                     NOT NULL
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