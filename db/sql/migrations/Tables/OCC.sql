--
-- OCC  (Table) 
--
CREATE TABLE TQ_CRM.OCC
(
  IDNO         NUMBER,
  OCCUPATIONS  VARCHAR2(200 BYTE),
  SECTOR       VARCHAR2(100 BYTE)
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