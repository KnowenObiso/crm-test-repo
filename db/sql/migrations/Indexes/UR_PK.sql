--
-- UR_PK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.UR_PK ON TQ_CRM.TQC_USER_SYSTEM_ROLES
(USRL_USR_CODE, USRL_SRL_CODE, USRL_GRANT_DATE)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          128K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           );