--
-- TQC_SYSTEM_MAILS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYSTEM_MAILS
(
  MAIL_SERVER_NAME  VARCHAR2(15 BYTE),
  MAIL_HOST         VARCHAR2(20 BYTE),
  MAIL_USERNAME     VARCHAR2(50 BYTE),
  MAIL_PASSWORD     VARCHAR2(50 BYTE),
  MAIL_PORT         NUMBER,
  MAIL_EMAIL        VARCHAR2(50 BYTE),
  MAIL_IN_OUT       VARCHAR2(1 BYTE),
  MAIL_SECURE       VARCHAR2(1 BYTE),
  MAIL_TYPE         VARCHAR2(50 BYTE)
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