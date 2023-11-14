--
-- TQC_CURRENCY_DENOMINATIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CURRENCY_DENOMINATIONS
(
  CUD_CODE      NUMBER(8)                       NOT NULL,
  CUD_CUR_CODE  NUMBER(8)                       NOT NULL,
  CUD_VALUE     NUMBER(12,2)                    NOT NULL,
  CUD_NAME      VARCHAR2(50 BYTE)               NOT NULL,
  CUD_WEF       DATE                            NOT NULL
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