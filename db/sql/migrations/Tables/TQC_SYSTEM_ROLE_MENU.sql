--
-- TQC_SYSTEM_ROLE_MENU  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYSTEM_ROLE_MENU
(
  SRM_SRL_CODE    NUMBER(8)                     NOT NULL,
  SRM_MENU_CODE   NUMBER(8)                     NOT NULL,
  SRM_GRANT_DATE  DATE
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          512K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;