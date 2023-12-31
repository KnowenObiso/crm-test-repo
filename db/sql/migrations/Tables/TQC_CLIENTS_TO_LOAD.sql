--
-- TQC_CLIENTS_TO_LOAD  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENTS_TO_LOAD
(
  CLIENTCODE      VARCHAR2(30 BYTE),
  LASTNAME        VARCHAR2(300 BYTE),
  CLIENTADDRESS1  VARCHAR2(300 BYTE),
  CLIENTADDRESS2  VARCHAR2(300 BYTE),
  TOWNCODE        VARCHAR2(300 BYTE),
  PHONE1          VARCHAR2(300 BYTE),
  SEX             VARCHAR2(30 BYTE),
  FAXNUMBER       VARCHAR2(30 BYTE),
  EMAIL           VARCHAR2(100 BYTE),
  GIS_CLNT_CODE   NUMBER,
  NAMES           VARCHAR2(300 BYTE),
  FIRSTNAME       VARCHAR2(100 BYTE),
  MIDDLENAME      VARCHAR2(100 BYTE),
  TITLE           VARCHAR2(100 BYTE),
  IDNUMBER        VARCHAR2(100 BYTE),
  PHONE2          VARCHAR2(300 BYTE),
  LOADED          VARCHAR2(1 BYTE)              DEFAULT 'N'                   NOT NULL,
  CLIENTTYPE      VARCHAR2(1 BYTE)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          7M
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;