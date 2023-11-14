--
-- TQC_ACTIVITY_TASKS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ACTIVITY_TASKS
(
  AT_CODE           NUMBER(8)                   NOT NULL,
  AT_ACT_CODE       NUMBER(8)                   NOT NULL,
  AT_DATE_FROM      DATE,
  AT_DATE_TO        DATE,
  AT_SUBJECT        VARCHAR2(100 BYTE),
  AT_STATUS_ID      NUMBER(8),
  AT_PRIORITY_CODE  NUMBER(8),
  AT_ACC_CODE       NUMBER(8)
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