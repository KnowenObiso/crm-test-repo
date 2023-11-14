--
-- TQC_QRTZ_CALENDARS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_QRTZ_CALENDARS
(
  SCHED_NAME     VARCHAR2(120 BYTE)             NOT NULL,
  CALENDAR_NAME  VARCHAR2(200 BYTE)             NOT NULL,
  CALENDAR       BLOB                           NOT NULL
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