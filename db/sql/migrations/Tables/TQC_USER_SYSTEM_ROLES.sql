--
-- TQC_USER_SYSTEM_ROLES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USER_SYSTEM_ROLES
(
  USRL_USR_CODE            NUMBER(8)            NOT NULL,
  USRL_SRL_CODE            NUMBER(8)            NOT NULL,
  USRL_GRANT_DATE          DATE                 NOT NULL,
  USRL_REVOKE_DATE         DATE,
  USRL_GRANT_BY_USR_CODE   NUMBER(22),
  USRL_REVOKE_BY_USR_CODE  NUMBER(22)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          128K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;