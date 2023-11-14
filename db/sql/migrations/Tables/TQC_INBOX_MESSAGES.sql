--
-- TQC_INBOX_MESSAGES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_INBOX_MESSAGES
(
  IBX_CODE         NUMBER(15)                   NOT NULL,
  IBX_SYS_CODE     NUMBER(15)                   DEFAULT 0                     NOT NULL,
  IBX_SYS_MODULE   VARCHAR2(5 BYTE)             DEFAULT 'G'                   NOT NULL,
  IBX_TEL_NO       VARCHAR2(35 BYTE),
  IBX_MSG          VARCHAR2(500 BYTE),
  IBX_STATUS       VARCHAR2(2 BYTE)             DEFAULT 'D'                   NOT NULL,
  IBX_DATE         DATE,
  IBX_ASGND_DATE   DATE,
  IBX_MSG_REPLIED  VARCHAR2(500 BYTE),
  IBX_USR_INFORMD  VARCHAR2(3 BYTE),
  IBX_ASGND_TO     VARCHAR2(50 BYTE)
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