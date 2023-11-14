--
-- TQC_KB_TOPICS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_KB_TOPICS
(
  TKBT_ID         NUMBER,
  TKBT_ORDER      NUMBER,
  TKBT_SHT_DESC   VARCHAR2(50 BYTE),
  TKBT_DESC       VARCHAR2(250 BYTE),
  TKBT_PARENT_ID  NUMBER
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