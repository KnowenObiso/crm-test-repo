--
-- TQC_AGENT_TYPES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENT_TYPES
(
  AGNTY_CODE           NUMBER                   NOT NULL,
  AGNTY_TYPE_SHT_DESC  VARCHAR2(200 BYTE),
  AGNTY_TYPE           VARCHAR2(200 BYTE),
  AGNTY_ACT_CODE       NUMBER
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