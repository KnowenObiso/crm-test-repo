--
-- TQC_CLIENT_CONTACTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_CONTACTS
(
  CLCO_CODE            NUMBER                   NOT NULL,
  CLCO_CLNT_CODE       NUMBER                   NOT NULL,
  CLCO_NAME            VARCHAR2(300 BYTE)       NOT NULL,
  CLCO_POSTAL_ADDRS    VARCHAR2(300 BYTE),
  CLCO_PHYSICAL_ADDRS  VARCHAR2(300 BYTE),
  CLCO_SEC_CODE        NUMBER
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