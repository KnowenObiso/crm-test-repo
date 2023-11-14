--
-- TQC_GROUP_USERS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_GROUP_USERS
(
  GUSR_CODE          NUMBER(15),
  GUSR_GRP_USR_CODE  NUMBER(15)                 NOT NULL,
  GUSR_USR_CODE      NUMBER(15)                 NOT NULL
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