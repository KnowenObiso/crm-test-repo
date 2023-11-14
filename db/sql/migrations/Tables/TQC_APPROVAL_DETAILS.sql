--
-- TQC_APPROVAL_DETAILS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_APPROVAL_DETAILS
(
  APD_ID             NUMBER(22)                 NOT NULL,
  APD_SYS_CODE       NUMBER(8)                  NOT NULL,
  APD_SPRSA_CODE     NUMBER(8)                  NOT NULL,
  APD_DATE_CREATED   DATE                       NOT NULL,
  APD_DATE_APPROVED  DATE,
  APD_ACTION         VARCHAR2(15 BYTE),
  APD_COMMENTS       VARCHAR2(250 BYTE),
  APD_USRID          NUMBER(8),
  APD_LEVEL          NUMBER                     NOT NULL,
  APD_TRANS_ID       NUMBER                     NOT NULL,
  APD_DESCRIPTION    VARCHAR2(250 BYTE)
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