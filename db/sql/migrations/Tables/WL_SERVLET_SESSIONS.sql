--
-- WL_SERVLET_SESSIONS  (Table) 
--
CREATE TABLE TQ_CRM.WL_SERVLET_SESSIONS
(
  WL_ID                     VARCHAR2(100 BYTE)  NOT NULL,
  WL_CONTEXT_PATH           VARCHAR2(100 BYTE)  NOT NULL,
  WL_IS_NEW                 CHAR(1 BYTE),
  WL_CREATE_TIME            NUMBER(20),
  WL_IS_VALID               CHAR(1 BYTE),
  WL_SESSION_VALUES         LONG RAW,
  WL_ACCESS_TIME            NUMBER(20),
  WL_MAX_INACTIVE_INTERVAL  INTEGER
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