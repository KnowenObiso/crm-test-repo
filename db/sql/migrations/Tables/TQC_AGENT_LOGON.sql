--
-- TQC_AGENT_LOGON  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENT_LOGON
(
  AGNL_SESSION_CODE  NUMBER(15)                 NOT NULL,
  AGNL_AGN_CODE      NUMBER(8)                  NOT NULL,
  AGNL_ACCC_CODE     NUMBER(8)                  NOT NULL,
  AGNL_SYS_CODE      NUMBER(8)                  NOT NULL,
  AGNL_LOGON_DT      DATE                       NOT NULL,
  AGNL_LOGOUT_DT     DATE
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