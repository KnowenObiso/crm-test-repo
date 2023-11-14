--
-- TQC_WEB_DOCUMENTS_REGISTER  (Table) 
--
CREATE TABLE TQ_CRM.TQC_WEB_DOCUMENTS_REGISTER
(
  DOCR_ID             INTEGER,
  DOCR_DOC_NAME       VARCHAR2(100 BYTE),
  DOCR_DOC_URL        VARCHAR2(300 BYTE),
  DOCR_DOC_AUTHOR     VARCHAR2(50 BYTE),
  DOCR_DOC_DESC       VARCHAR2(50 BYTE),
  DOCR_CLNT_CODE      NUMBER(22),
  DOCR_POL_POLICY_NO  VARCHAR2(30 BYTE),
  DOCR_CLAIM_NO       VARCHAR2(30 BYTE),
  DOCR_QUOT_CODE      NUMBER(22),
  DOCR_LEVEL          VARCHAR2(3 BYTE)          DEFAULT 'A',
  DOCR_SYS_CODE       NUMBER,
  DOCR_POL_BATCH_NO   NUMBER(22),
  DOCR_DATE_CREATED   DATE,
  DOCR_AGN_CODE       NUMBER(22),
  DOCR_DOC            BLOB
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