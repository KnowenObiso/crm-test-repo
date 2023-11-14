--
-- TQC_USER_SYSTEM_AUTH_ROLES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USER_SYSTEM_AUTH_ROLES
(
  USAR_USR_CODE            NUMBER(8)            NOT NULL,
  USAR_SAR_CODE            NUMBER(8)            NOT NULL,
  USAR_GRANT_DATE          DATE                 NOT NULL,
  USAR_REVOKE_DATE         DATE,
  USAR_GRANT_BY_USR_CODE   NUMBER(22),
  USAR_REVOKE_BY_USR_CODE  NUMBER(22)
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          320K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;