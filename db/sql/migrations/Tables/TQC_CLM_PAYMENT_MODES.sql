--
-- TQC_CLM_PAYMENT_MODES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLM_PAYMENT_MODES
(
  CPM_CODE         NUMBER(23)                   NOT NULL,
  CPM_SHT_DESC     VARCHAR2(10 BYTE)            NOT NULL,
  CPM_DESC         VARCHAR2(30 BYTE),
  CPM_REMARKS      VARCHAR2(100 BYTE),
  CPM_MIN_AMT      NUMBER(23,5),
  CPM_MAX_AMT      NUMBER(23,5),
  CPM_DEFAULT      VARCHAR2(1 BYTE)             DEFAULT 'N',
  CPM_COM_MIN_AMT  NUMBER(23,5)
)
TABLESPACE CRMDATA
PCTUSED    0
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