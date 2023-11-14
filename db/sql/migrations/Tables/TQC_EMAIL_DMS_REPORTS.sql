--
-- TQC_EMAIL_DMS_REPORTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_EMAIL_DMS_REPORTS
(
  EMRPT_CODE       NUMBER                       NOT NULL,
  EMRPT_SPRR_CODE  NUMBER                       NOT NULL,
  EMRPT_DESC       VARCHAR2(100 BYTE),
  EMRPT_TYPE       VARCHAR2(20 BYTE)            NOT NULL
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