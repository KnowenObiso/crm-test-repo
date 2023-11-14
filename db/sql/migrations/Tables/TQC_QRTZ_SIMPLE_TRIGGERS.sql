--
-- TQC_QRTZ_SIMPLE_TRIGGERS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_QRTZ_SIMPLE_TRIGGERS
(
  SCHED_NAME       VARCHAR2(120 BYTE)           NOT NULL,
  TRIGGER_NAME     VARCHAR2(200 BYTE)           NOT NULL,
  TRIGGER_GROUP    VARCHAR2(200 BYTE)           NOT NULL,
  REPEAT_COUNT     NUMBER(7)                    NOT NULL,
  REPEAT_INTERVAL  NUMBER(12)                   NOT NULL,
  TIMES_TRIGGERED  NUMBER(10)                   NOT NULL
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