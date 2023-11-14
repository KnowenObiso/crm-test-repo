--
-- TQC_DIVISIONS_AUDIT_TRL  (Table) 
--
CREATE TABLE TQ_CRM.TQC_DIVISIONS_AUDIT_TRL
(
  DAUDT_CODE         NUMBER(23)                 NOT NULL,
  DAUDT_DIV_CODE     NUMBER(23)                 NOT NULL,
  DAUDT_DATE         DATE                       NOT NULL,
  DAUDT_PREV_STATUS  VARCHAR2(1 BYTE)           NOT NULL,
  DAUDT_CURR_STATUS  VARCHAR2(1 BYTE)           NOT NULL,
  DAUDT_USER         VARCHAR2(50 BYTE)
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