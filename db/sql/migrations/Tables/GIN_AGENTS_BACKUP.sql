--
-- GIN_AGENTS_BACKUP  (Table) 
--
CREATE TABLE TQ_CRM.GIN_AGENTS_BACKUP
(
  AGNT_AGENT_CODE  NUMBER(22)                   NOT NULL,
  AGNT_AGENT_TYPE  VARCHAR2(20 BYTE)
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