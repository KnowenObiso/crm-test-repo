--
-- JBPM4_DEPLOYMENT  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_DEPLOYMENT
(
  DBID_       NUMBER(19)                        NOT NULL,
  NAME_       CLOB,
  TIMESTAMP_  NUMBER(19),
  STATE_      VARCHAR2(255 BYTE)
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