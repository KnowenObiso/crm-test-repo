--
-- TQC_BANK_REGIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BANK_REGIONS
(
  BNKR_CODE      NUMBER(8)                      NOT NULL,
  BNKR_ORG_CODE  NUMBER(8)                      NOT NULL,
  BNKR_SHT_DESC  VARCHAR2(15 BYTE)              NOT NULL,
  BNKR_NAME      VARCHAR2(100 BYTE)             NOT NULL,
  BNKR_WEF       DATE,
  BNKR_WET       DATE,
  BNKR_REG_CODE  NUMBER(15)                     NOT NULL,
  BNKR_AGN_CODE  NUMBER(15)
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