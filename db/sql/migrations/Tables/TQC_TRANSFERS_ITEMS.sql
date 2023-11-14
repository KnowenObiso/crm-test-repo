--
-- TQC_TRANSFERS_ITEMS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_TRANSFERS_ITEMS
(
  TTI_CODE             NUMBER(8)                NOT NULL,
  TTI_TT_CODE          NUMBER(32)               NOT NULL,
  TTI_ITEM_CODE        VARCHAR2(80 BYTE)        NOT NULL,
  TTI_ITEM_NAME        VARCHAR2(80 BYTE)        NOT NULL,
  TTI_ITEM_SHT_DESC    VARCHAR2(80 BYTE)        NOT NULL,
  TTI_ITEM_TYPE        VARCHAR2(3 BYTE)         NOT NULL,
  TTI_TRANS_DATE       DATE,
  TTI_TRANS_TO         VARCHAR2(80 BYTE),
  TTI_TRANS_FROM       VARCHAR2(80 BYTE),
  TTI_DONE_BY          VARCHAR2(80 BYTE),
  TTI_DONE_DATE        DATE,
  TTI_AUTHORIZED       VARCHAR2(80 BYTE),
  TTI_AUTHORIZED_BY    VARCHAR2(80 BYTE),
  TTI_AUTHORIZED_DATE  DATE
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