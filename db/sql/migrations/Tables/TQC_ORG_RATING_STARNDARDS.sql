--
-- TQC_ORG_RATING_STARNDARDS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ORG_RATING_STARNDARDS
(
  ORS_CODE       NUMBER                         NOT NULL,
  ORS_DESC       VARCHAR2(200 BYTE),
  ORS_RORG_CODE  NUMBER
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