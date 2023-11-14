--
-- TQC_QRTZ_FIRED_TRIGGERS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_QRTZ_FIRED_TRIGGERS
(
  SCHED_NAME         VARCHAR2(120 BYTE)         NOT NULL,
  ENTRY_ID           VARCHAR2(95 BYTE)          NOT NULL,
  TRIGGER_NAME       VARCHAR2(200 BYTE)         NOT NULL,
  TRIGGER_GROUP      VARCHAR2(200 BYTE)         NOT NULL,
  INSTANCE_NAME      VARCHAR2(200 BYTE)         NOT NULL,
  FIRED_TIME         NUMBER(13)                 NOT NULL,
  PRIORITY           NUMBER(13)                 NOT NULL,
  STATE              VARCHAR2(16 BYTE)          NOT NULL,
  JOB_NAME           VARCHAR2(200 BYTE),
  JOB_GROUP          VARCHAR2(200 BYTE),
  IS_NONCONCURRENT   VARCHAR2(1 BYTE),
  REQUESTS_RECOVERY  VARCHAR2(1 BYTE)
)
TABLESPACE CRMDATA
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
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;