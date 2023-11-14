--
-- TQC_DISTRICTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_DISTRICTS
(
  DIST_CODE      NUMBER(8)                      NOT NULL,
  DIST_COU_CODE  NUMBER(8)                      NOT NULL,
  DIST_SHT_DESC  VARCHAR2(15 BYTE)              NOT NULL,
  DIST_NAME      VARCHAR2(50 BYTE)              NOT NULL,
  DIST_STS_CODE  NUMBER(8)
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