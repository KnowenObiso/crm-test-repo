--
-- TQC_BRNCH_DIVISIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BRNCH_DIVISIONS
(
  BDIV_CODE      NUMBER(22)                     NOT NULL,
  BDIV_BRN_CODE  NUMBER(8)                      NOT NULL,
  BDIV_DIV_CODE  NUMBER(22)                     NOT NULL,
  BDIV_WEF       DATE                           NOT NULL,
  BDIV_WET       DATE
)
TABLESPACE USERS
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
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;