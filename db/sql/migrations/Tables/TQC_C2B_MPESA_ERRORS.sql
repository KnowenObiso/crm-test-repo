--
-- TQC_C2B_MPESA_ERRORS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_C2B_MPESA_ERRORS
(
  C2BE_CODE        NUMBER,
  C2BE_TRANS_ID    VARCHAR2(50 BYTE),
  C2BE_INVOICE_NO  VARCHAR2(50 BYTE),
  C2BE_ERRMSG      VARCHAR2(1000 BYTE),
  C2BE_MSISDN      VARCHAR2(50 BYTE),
  C2BE_DATE        DATE,
  C2BE_BILLREF_NO  VARCHAR2(50 BYTE)
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