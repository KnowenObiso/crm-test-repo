--
-- TQC_USER_SUB_DIVISIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USER_SUB_DIVISIONS
(
  USSD_CODE       NUMBER(22,22)                 NOT NULL,
  USSD_SDIV_CODE  NUMBER(22,22)                 NOT NULL,
  USSD_USD_CODE   NUMBER(22,22)                 NOT NULL,
  USSD_DEFAULT    VARCHAR2(1 BYTE)              DEFAULT 'N'                   NOT NULL
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