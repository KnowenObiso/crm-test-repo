--
-- TQC_HOUSEHOLDS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_HOUSEHOLDS
(
  HH_ID            NUMBER                       NOT NULL,
  HH_NAME          VARCHAR2(100 BYTE)           NOT NULL,
  HH_HEAD          NUMBER,
  HH_CREATED_BY    NUMBER                       NOT NULL,
  HH_DATE_CREATED  DATE                         NOT NULL,
  HH_CATEGORY      VARCHAR2(10 BYTE)
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