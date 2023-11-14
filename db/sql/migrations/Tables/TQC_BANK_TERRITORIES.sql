--
-- TQC_BANK_TERRITORIES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BANK_TERRITORIES
(
  BNKT_CODE            NUMBER(8)                NOT NULL,
  BNKT_TERRITORY_NAME  VARCHAR2(100 BYTE),
  BNKT_SHT_DESC        VARCHAR2(100 BYTE),
  BNKT_BNK_CODE        NUMBER(8)
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