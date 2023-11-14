--
-- TQC_SYS_REPORTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_REPORTS
(
  RPT_CODE           NUMBER,
  RPT_SYS_CODE       NUMBER                     NOT NULL,
  RPT_NAME           VARCHAR2(50 BYTE)          NOT NULL,
  RPT_DESCRIPTION    VARCHAR2(250 BYTE),
  RPT_APPLCTN_LEVEL  VARCHAR2(5 BYTE),
  RPT_ACTIVE         VARCHAR2(1 BYTE),
  RPT_SYS_REPORT     VARCHAR2(1 BYTE)           DEFAULT 'Y'
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