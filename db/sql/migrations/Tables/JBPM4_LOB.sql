--
-- JBPM4_LOB  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_LOB
(
  DBID_        NUMBER(19)                       NOT NULL,
  DBVERSION_   NUMBER(10)                       NOT NULL,
  BLOB_VALUE_  BLOB,
  DEPLOYMENT_  NUMBER(19),
  NAME_        CLOB
)
TABLESPACE CRMDATA
PCTUSED    40
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          192K
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