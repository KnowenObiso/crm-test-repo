--
-- TQC_GROUP_CLIENT  (Table) 
--
CREATE TABLE TQ_CRM.TQC_GROUP_CLIENT
(
  GRP_CODE     NUMBER                           NOT NULL,
  GRP_NAME     VARCHAR2(100 BYTE),
  GRP_MINIMUM  NUMBER,
  GRP_MAXIMUM  NUMBER
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