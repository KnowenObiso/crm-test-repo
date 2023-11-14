--
-- TQC_AGENT_CHKOFF_RATES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENT_CHKOFF_RATES
(
  ACR_CODE       NUMBER(15)                     NOT NULL,
  ACR_AGN_CODE   NUMBER(22)                     NOT NULL,
  ACR_RATE       NUMBER(15,5)                   NOT NULL,
  ACR_DATE_FROM  DATE                           NOT NULL,
  ACR_DATE_TO    DATE,
  ACR_RATE_TYPE  VARCHAR2(1 BYTE)               DEFAULT 'P'                   NOT NULL
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