--
-- TQC_SYSTEM_AUTH_ROLES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYSTEM_AUTH_ROLES
(
  SAR_CODE          NUMBER(8)                   NOT NULL,
  SAR_NAME          VARCHAR2(100 BYTE)          NOT NULL,
  SAR_SYS_CODE      NUMBER(8)                   NOT NULL,
  SAR_DEBIT_LIMIT   NUMBER(29,5),
  SAR_CREDIT_LIMIT  NUMBER(29,5),
  SAR_SAA_CODE      NUMBER(8)                   NOT NULL,
  SAR_PRO_CODE      NUMBER(15),
  SAR_PRO_SHT_DESC  VARCHAR2(25 BYTE)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          576K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;