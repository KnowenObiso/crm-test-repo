--
-- JBPM4_ID_USER  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_ID_USER
(
  DBID_           NUMBER(19)                    NOT NULL,
  DBVERSION_      NUMBER(10)                    NOT NULL,
  ID_             VARCHAR2(255 BYTE),
  PASSWORD_       VARCHAR2(255 BYTE),
  GIVENNAME_      VARCHAR2(255 BYTE),
  FAMILYNAME_     VARCHAR2(255 BYTE),
  BUSINESSEMAIL_  VARCHAR2(255 BYTE)
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