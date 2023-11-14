--
-- UAR_PK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.UAR_PK ON TQ_CRM.TQC_USER_SYSTEM_AUTH_ROLES
(USAR_USR_CODE, USAR_SAR_CODE, USAR_GRANT_DATE)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          192K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           );