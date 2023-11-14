--
-- TQC_SMS_MESSAGES_BACKUP  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SMS_MESSAGES_BACKUP
(
  SMS_CODE           NUMBER(15),
  SMS_SYS_CODE       NUMBER(15)                 NOT NULL,
  SMS_SYS_MODULE     VARCHAR2(5 BYTE)           NOT NULL,
  SMS_CLNT_CODE      NUMBER(15)                 NOT NULL,
  SMS_AGN_CODE       NUMBER(15)                 NOT NULL,
  SMS_POL_CODE       NUMBER(15),
  SMS_POL_NO         VARCHAR2(35 BYTE),
  SMS_CLM_NO         VARCHAR2(35 BYTE),
  SMS_TEL_NO         VARCHAR2(35 BYTE),
  SMS_MSG            VARCHAR2(500 BYTE),
  SMS_STATUS         VARCHAR2(2 BYTE)           NOT NULL,
  SMS_PREPARED_BY    VARCHAR2(35 BYTE),
  SMS_PREPARED_DATE  DATE,
  SMS_SEND_DATE      DATE,
  SMS_QUOT_CODE      NUMBER(15),
  SMS_QUOT_NO        VARCHAR2(30 BYTE),
  SMS_USR_CODE       NUMBER(22)
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