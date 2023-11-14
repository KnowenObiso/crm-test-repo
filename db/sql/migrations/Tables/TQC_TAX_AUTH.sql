--
-- TQC_TAX_AUTH  (Table) 
--
CREATE TABLE TQ_CRM.TQC_TAX_AUTH
(
  AUTH_CODE      NUMBER,
  AUTH_SHT_CODE  VARCHAR2(100 BYTE),
  AUTH_NAME      VARCHAR2(100 BYTE)
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