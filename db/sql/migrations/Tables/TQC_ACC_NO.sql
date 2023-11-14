--
-- TQC_ACC_NO  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ACC_NO
(
  TAC_NO        VARCHAR2(8 BYTE)                NOT NULL,
  TAC_TWN_CODE  NUMBER(8)                       NOT NULL,
  TAC_ACC_TYPE  VARCHAR2(2 BYTE)                NOT NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          128K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;