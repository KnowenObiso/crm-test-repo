--
-- LOAD_CLNT_ERR  (Table) 
--
CREATE TABLE TQ_CRM.LOAD_CLNT_ERR
(
  SHORTNAME  VARCHAR2(24 BYTE),
  OLDCODE    NUMBER(15),
  ERR        VARCHAR2(200 BYTE)
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