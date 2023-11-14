--
-- TQC_SPECIALIZATIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SPECIALIZATIONS
(
  SPZ_CODE      NUMBER,
  SPZ_SHT_DESC  VARCHAR2(15 BYTE)               NOT NULL,
  SPZ_NAME      VARCHAR2(100 BYTE)              NOT NULL,
  SPZ_OCC_CODE  NUMBER
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