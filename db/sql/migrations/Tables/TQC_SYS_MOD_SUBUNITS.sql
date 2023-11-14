--
-- TQC_SYS_MOD_SUBUNITS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_MOD_SUBUNITS
(
  TSMS_CODE       NUMBER                        NOT NULL,
  TSMS_TSM_CODE   NUMBER                        NOT NULL,
  TSMS_SHT_DESC   VARCHAR2(20 BYTE)             NOT NULL,
  TSMS_DESC       VARCHAR2(50 BYTE)             NOT NULL,
  TSMS_ORDER      NUMBER                        NOT NULL,
  TSMS_PROD_CODE  NUMBER
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