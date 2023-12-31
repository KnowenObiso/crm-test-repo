--
-- TQC_QRTZ_SIMPROP_TRIGGERS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_QRTZ_SIMPROP_TRIGGERS
(
  SCHED_NAME     VARCHAR2(120 BYTE)             NOT NULL,
  TRIGGER_NAME   VARCHAR2(200 BYTE)             NOT NULL,
  TRIGGER_GROUP  VARCHAR2(200 BYTE)             NOT NULL,
  STR_PROP_1     VARCHAR2(512 BYTE),
  STR_PROP_2     VARCHAR2(512 BYTE),
  STR_PROP_3     VARCHAR2(512 BYTE),
  INT_PROP_1     NUMBER(10),
  INT_PROP_2     NUMBER(10),
  LONG_PROP_1    NUMBER(13),
  LONG_PROP_2    NUMBER(13),
  DEC_PROP_1     NUMBER(13,4),
  DEC_PROP_2     NUMBER(13,4),
  BOOL_PROP_1    VARCHAR2(1 BYTE),
  BOOL_PROP_2    VARCHAR2(1 BYTE)
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