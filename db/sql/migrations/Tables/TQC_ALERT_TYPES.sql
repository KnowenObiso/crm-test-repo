--
-- TQC_ALERT_TYPES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ALERT_TYPES
(
  ALRT_CODE             NUMBER(15)              NOT NULL,
  ALRT_TYPE             VARCHAR2(50 BYTE)       NOT NULL,
  ALRT_SYS_CODE         NUMBER(15)              NOT NULL,
  ALRT_EMAIL            VARCHAR2(1 BYTE)        DEFAULT 'N',
  ALRT_SMS              VARCHAR2(1 BYTE)        DEFAULT 'N',
  ALRT_SCREEN           VARCHAR2(1 BYTE)        DEFAULT 'N',
  ALRT_EMAIL_MSGT_CODE  NUMBER(5),
  ALRT_SMS_MSGT_CODE    NUMBER(5),
  ALRT_GRP_USR_CODE     NUMBER(8),
  ALRT_CHECK_ALERT      VARCHAR2(1 BYTE)        DEFAULT 'N',
  ALRT_NORM_AUTO        VARCHAR2(1 BYTE),
  ALRT_SHT_DESC         VARCHAR2(100 BYTE)
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