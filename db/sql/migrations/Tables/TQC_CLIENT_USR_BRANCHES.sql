--
-- TQC_CLIENT_USR_BRANCHES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_USR_BRANCHES
(
  TCUB_CODE       NUMBER,
  TCUB_ACWA_CODE  NUMBER                        NOT NULL,
  TCUB_TCB_CODE   NUMBER                        NOT NULL,
  TCUB_DEFAULT    VARCHAR2(1 BYTE)
)
TABLESPACE CRMDATA
PCTUSED    40
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
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;