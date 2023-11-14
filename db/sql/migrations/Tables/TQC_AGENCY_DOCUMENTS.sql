--
-- TQC_AGENCY_DOCUMENTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_DOCUMENTS
(
  ADOCR_CODE          NUMBER(22)                NOT NULL,
  ADOCR_RDOC_CODE     NUMBER(22)                NOT NULL,
  ADOCR_AGN_CODE      NUMBER(22)                NOT NULL,
  ADOCR_SUBMITED      VARCHAR2(1 BYTE)          DEFAULT 'N'                   NOT NULL,
  ADOCR_DATE_S        DATE,
  ADOCR_REF_NO        VARCHAR2(50 BYTE),
  ADOCR_RMRK          VARCHAR2(50 BYTE),
  ADOCR_USER_RECEIVD  VARCHAR2(50 BYTE)         NOT NULL,
  ADOCR_DOCID         VARCHAR2(100 BYTE)
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