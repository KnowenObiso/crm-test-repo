--
-- TQC_SP_DOCUMENTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SP_DOCUMENTS
(
  SDOCR_CODE          NUMBER(22)                NOT NULL,
  SDOCR_RDOC_CODE     NUMBER(22)                NOT NULL,
  SDOCR_CLNT_CODE     NUMBER(22)                NOT NULL,
  SDOCR_SUBMITED      VARCHAR2(1 BYTE)          DEFAULT 'N',
  SDOCR_DATE_S        DATE,
  SDOCR_REF_NO        VARCHAR2(50 BYTE),
  SDOCR_RMRK          VARCHAR2(50 BYTE),
  SDOCR_USER_RECEIVD  VARCHAR2(50 BYTE)         NOT NULL,
  SDOCR_DOCID         VARCHAR2(60 BYTE)
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