--
-- TQC_SYS_ROLES_PRCS_AREA  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_ROLES_PRCS_AREA
(
  SRPRA_CODE        NUMBER(15),
  SRPRA_SRPRC_CODE  NUMBER(15)                  NOT NULL,
  SRPRA_SPRCA_CODE  NUMBER(15)                  NOT NULL
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