--
-- TQC_RATING_ORGANIZATIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_RATING_ORGANIZATIONS
(
  RORG_CODE      NUMBER                         NOT NULL,
  RORG_DESC      VARCHAR2(200 BYTE),
  RORG_SHT_DESC  VARCHAR2(200 BYTE)
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