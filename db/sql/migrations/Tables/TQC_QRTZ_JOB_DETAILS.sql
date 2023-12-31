--
-- TQC_QRTZ_JOB_DETAILS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_QRTZ_JOB_DETAILS
(
  SCHED_NAME         VARCHAR2(120 BYTE)         NOT NULL,
  JOB_NAME           VARCHAR2(200 BYTE)         NOT NULL,
  JOB_GROUP          VARCHAR2(200 BYTE)         NOT NULL,
  DESCRIPTION        VARCHAR2(250 BYTE),
  JOB_CLASS_NAME     VARCHAR2(250 BYTE)         NOT NULL,
  IS_DURABLE         VARCHAR2(1 BYTE)           NOT NULL,
  IS_NONCONCURRENT   VARCHAR2(1 BYTE)           NOT NULL,
  IS_UPDATE_DATA     VARCHAR2(1 BYTE)           NOT NULL,
  REQUESTS_RECOVERY  VARCHAR2(1 BYTE)           NOT NULL,
  JOB_DATA           BLOB
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