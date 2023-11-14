--
-- TQC_SYS_REPT_TEMPLATES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_REPT_TEMPLATES
(
  RPT_TMPL_CODE         NUMBER                  NOT NULL,
  RPT_TMPL_RPT_CODE     NUMBER                  NOT NULL,
  RPT_SYS_TMPL_FILE     VARCHAR2(50 BYTE)       NOT NULL,
  RPT_TMPL_NAME         VARCHAR2(50 BYTE)       NOT NULL,
  RPT_TMPL_DESCRIPTION  VARCHAR2(250 BYTE),
  RPT_TMPL_STYLE_FILE   VARCHAR2(50 BYTE)       NOT NULL,
  RPT_TMPL_ORG_CODE     NUMBER,
  RPT_TMPL_ACTIVE       VARCHAR2(1 BYTE),
  RPT_SYS_XML_FILE      VARCHAR2(50 BYTE),
  RPT_CLNT_XML_FILE     VARCHAR2(50 BYTE),
  RPT_CLNT_TMPL_FILE    VARCHAR2(50 BYTE)
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