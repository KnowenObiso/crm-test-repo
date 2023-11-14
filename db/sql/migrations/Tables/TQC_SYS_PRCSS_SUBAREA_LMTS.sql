--
-- TQC_SYS_PRCSS_SUBAREA_LMTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_PRCSS_SUBAREA_LMTS
(
  SPSAT_CODE         NUMBER(8)                  NOT NULL,
  SPSAT_SPRSA_CODE   NUMBER(8)                  NOT NULL,
  SPSAT_NO_OF_LEVEL  NUMBER(5)                  DEFAULT 1                     NOT NULL,
  SPSAT_MIN_LIMIT    NUMBER(22,5)               NOT NULL,
  SPSAT_MAX_LIMIT    NUMBER(22,5)               NOT NULL,
  SPSAT_PRO_CODE     NUMBER
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