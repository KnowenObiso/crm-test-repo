--
-- TQC_CAMP_ACTIVITIES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CAMP_ACTIVITIES
(
  CMA_CODE      NUMBER(15)                      NOT NULL,
  CMA_CMP_CODE  NUMBER(15)                      NOT NULL,
  CMA_ACT_CODE  NUMBER(8)
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