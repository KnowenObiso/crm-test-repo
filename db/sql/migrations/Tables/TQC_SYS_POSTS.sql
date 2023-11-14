--
-- TQC_SYS_POSTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_POSTS
(
  SPOST_SYS_CODE           NUMBER(8)            NOT NULL,
  SPOST_SYSPL_CODE         NUMBER(8)            NOT NULL,
  SPOST_PARENT_SPOST_CODE  NUMBER(8),
  SPOST_CODE               NUMBER(8)            NOT NULL,
  SPOST_SHT_DESC           VARCHAR2(10 BYTE)    NOT NULL,
  SPOST_DESC               VARCHAR2(50 BYTE)    NOT NULL,
  SPOST_REMARKS            VARCHAR2(300 BYTE)   NOT NULL,
  SPOST_WEF                DATE                 NOT NULL,
  SPOST_BRN_CODE           NUMBER,
  SPOST_SUBDIV_OSD_CODE    VARCHAR2(10 BYTE),
  SPOST_USR_CODE           NUMBER(8)
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

COMMENT ON TABLE TQ_CRM.TQC_SYS_POSTS IS 'For storing of System Posts';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_SYS_CODE IS 'Establishment Short Name FK';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_SYSPL_CODE IS 'Post Level FK';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_PARENT_SPOST_CODE IS 'Parent Post FK';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_CODE IS 'PK';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_SHT_DESC IS 'Short Description';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_DESC IS 'Description';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_REMARKS IS 'Reason for creation';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_WEF IS 'With Effect From Date';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_BRN_CODE IS 'Branch for the post';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_SUBDIV_OSD_CODE IS 'Functional subdivision of the post';

COMMENT ON COLUMN TQ_CRM.TQC_SYS_POSTS.SPOST_USR_CODE IS 'User in the specified post';