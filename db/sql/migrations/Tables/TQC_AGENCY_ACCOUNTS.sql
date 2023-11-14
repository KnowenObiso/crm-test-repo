--
-- TQC_AGENCY_ACCOUNTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_ACCOUNTS
(
  AGA_CODE          NUMBER(22)                  NOT NULL,
  AGA_SHT_DESC      VARCHAR2(15 BYTE)           NOT NULL,
  AGA_NAME          VARCHAR2(300 BYTE)          NOT NULL,
  AGA_AGN_CODE      NUMBER(12)                  NOT NULL,
  AGA_CREATED_BY    VARCHAR2(30 BYTE)           NOT NULL,
  AGA_DATE_CREATED  DATE                        NOT NULL,
  AGA_STATUS        VARCHAR2(10 BYTE)           NOT NULL,
  AGA_REMARKS       VARCHAR2(300 BYTE),
  AGA_WEF           DATE                        NOT NULL,
  AGA_WET           DATE,
  AGA_DIV_CODE      NUMBER(22)
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