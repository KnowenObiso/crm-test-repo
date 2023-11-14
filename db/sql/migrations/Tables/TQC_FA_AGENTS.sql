--
-- TQC_FA_AGENTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_FA_AGENTS
(
  FA_AGN_CODE      NUMBER(31)                   NOT NULL,
  FA_AGN_ORG_CODE  NUMBER(23)                   NOT NULL,
  FA_AGN_SHT_DESC  VARCHAR2(100 BYTE)           NOT NULL,
  FA_TEAM_CODE     NUMBER(23)                   NOT NULL,
  FA_AGN_NAME      VARCHAR2(100 BYTE)           NOT NULL,
  FA_AGENT_CODE    NUMBER
)
TABLESPACE CRMDATA
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
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;