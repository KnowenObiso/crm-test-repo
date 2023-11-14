--
-- TQC_CLIENT_TITLES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_TITLES
(
  CLT_CODE      NUMBER                          NOT NULL,
  CLT_SHT_DESC  VARCHAR2(30 BYTE),
  CLNT_DESC     VARCHAR2(100 BYTE),
  CLT_GENDER    VARCHAR2(6 BYTE)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          320K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;