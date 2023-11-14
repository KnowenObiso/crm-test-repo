--
-- TQC_USER_LOGON  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USER_LOGON
(
  USL_SESSION_CODE  NUMBER(15)                  NOT NULL,
  USL_USR_CODE      NUMBER(8)                   NOT NULL,
  USL_SYS_CODE      NUMBER(8)                   NOT NULL,
  USL_LOGON_DT      DATE                        NOT NULL,
  USL_LOGOUT_DT     DATE,
  USL_LOG_OUT_TYPE  VARCHAR2(50 BYTE)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          5M
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;