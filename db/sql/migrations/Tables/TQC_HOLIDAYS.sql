--
-- TQC_HOLIDAYS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_HOLIDAYS
(
  HLD_DATE  DATE                                NOT NULL,
  HLD_DESC  VARCHAR2(100 BYTE)                  NOT NULL
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