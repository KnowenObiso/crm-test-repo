--
-- TQC_USER_BRANCHES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USER_BRANCHES
(
  USB_CODE      NUMBER(8),
  USB_BRN_CODE  NUMBER(8)                       NOT NULL,
  USB_USR_CODE  NUMBER(8)                       NOT NULL,
  USB_STATUS    VARCHAR2(1 BYTE)                DEFAULT 'A'                   NOT NULL,
  USB_DFLT_BRN  VARCHAR2(1 BYTE)                DEFAULT 'N'
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