--
-- TQC_SYS_ROLES_PROCESSES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_ROLES_PROCESSES
(
  SRPRC_CODE       NUMBER(15),
  SRPRC_SRLS_CODE  NUMBER(15)                   NOT NULL,
  SRPRC_SPRC_CODE  NUMBER(15)                   NOT NULL
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