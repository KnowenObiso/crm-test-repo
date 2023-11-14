--
-- TQC_CLIENT_DOCUMENTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_DOCUMENTS
(
  CDOCR_CODE          NUMBER(22)                NOT NULL,
  CDOCR_RDOC_CODE     NUMBER(22)                NOT NULL,
  CDOCR_CLNT_CODE     NUMBER(22)                NOT NULL,
  CDOCR_SUBMITED      VARCHAR2(1 BYTE)          DEFAULT 'N'                   NOT NULL,
  CDOCR_DATE_S        DATE,
  CDOCR_REF_NO        VARCHAR2(50 BYTE),
  CDOCR_RMRK          VARCHAR2(50 BYTE),
  CDOCR_USER_RECEIVD  VARCHAR2(50 BYTE)         NOT NULL,
  CDOCR_DOCID         VARCHAR2(20 BYTE)
)
TABLESPACE USERS
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
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;