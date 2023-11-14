--
-- TQC_AGENCY_ACTIVITIES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_ACTIVITIES
(
  AAC_CODE           NUMBER(15)                 NOT NULL,
  AAC_ACTY_CODE      NUMBER(8)                  NOT NULL,
  AAC_WEF            DATE,
  AAC_ESTIMATE_WET   DATE,
  AAC_ACTUAL_WET     DATE,
  AAC_REMARKS        VARCHAR2(500 BYTE),
  AAC_AGN_CODE       NUMBER(8),
  AAC_CLNT_CODE      NUMBER(8),
  AAC_SPR_CODE       NUMBER(8),
  AAC_SYS_CODE       NUMBER(8),
  AAC_MKTR_AGN_CODE  NUMBER(8),
  AAC_BY_CODE        NUMBER,
  AAC_BY_TYPE        VARCHAR2(100 BYTE),
  AAC_REASONS        VARCHAR2(300 BYTE)
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