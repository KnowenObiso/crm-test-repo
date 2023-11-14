--
-- LOAD_REJECTED_CLIENTS  (Table) 
--
CREATE TABLE TQ_CRM.LOAD_REJECTED_CLIENTS
(
  SHORTNAME      VARCHAR2(25 BYTE),
  CLIENTNAME     VARCHAR2(60 BYTE),
  OLDCODE        NUMBER(15),
  BOX            VARCHAR2(15 BYTE),
  TOWN           VARCHAR2(30 BYTE),
  REJECT_REMARK  VARCHAR2(200 BYTE)
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