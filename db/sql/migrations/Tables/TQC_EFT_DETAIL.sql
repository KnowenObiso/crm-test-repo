--
-- TQC_EFT_DETAIL  (Table) 
--
CREATE TABLE TQ_CRM.TQC_EFT_DETAIL
(
  ED_CODE             NUMBER(8)                 NOT NULL,
  ED_EH_CODE          NUMBER(8)                 NOT NULL,
  ED_CUST_CODE        VARCHAR2(40 BYTE)         NOT NULL,
  ED_CUST_NAME        VARCHAR2(200 BYTE)        NOT NULL,
  ED_BANK_CODE        VARCHAR2(20 BYTE)         NOT NULL,
  ED_ACNT_NO          VARCHAR2(20 BYTE)         NOT NULL,
  ED_CURR_CODE        VARCHAR2(10 BYTE)         NOT NULL,
  ED_AMOUNT           NUMBER                    NOT NULL,
  ED_PAY_DT           DATE,
  ED_REMARK           VARCHAR2(200 BYTE),
  ED_PER_NO           VARCHAR2(15 BYTE),
  ED_PAYEE_ADDRESS    VARCHAR2(100 BYTE),
  ED_PAYEE_ADDRESS_2  VARCHAR2(100 BYTE)
)
TABLESPACE CRMDATA
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
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;