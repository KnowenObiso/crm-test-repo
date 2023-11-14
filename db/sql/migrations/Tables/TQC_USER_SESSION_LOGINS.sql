--
-- TQC_USER_SESSION_LOGINS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USER_SESSION_LOGINS
(
  USS_CODE             NUMBER,
  USS_USER             VARCHAR2(30 BYTE),
  USS_HOST_IP          VARCHAR2(30 BYTE),
  USS_TIME_LOGGED_IN   DATE,
  USS_TIME_LOGGED_OUT  DATE,
  USS_STATUS           VARCHAR2(1 BYTE)         DEFAULT 'N',
  USS_SYS_CODE         NUMBER,
  USS_BROWSER          VARCHAR2(50 BYTE)
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

COMMENT ON TABLE TQ_CRM.TQC_USER_SESSION_LOGINS IS '/*
  Created table to manage user sessions
*/';

COMMENT ON COLUMN TQ_CRM.TQC_USER_SESSION_LOGINS.USS_SYS_CODE IS 'SYSTEM CODE';