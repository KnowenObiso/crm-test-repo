--
-- TQC_ORG_SUBDIVISIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ORG_SUBDIVISIONS
(
  OSD_CODE               VARCHAR2(10 BYTE)      NOT NULL,
  OSD_PARENT_OSD_CODE    VARCHAR2(30 BYTE),
  OSD_DLT_CODE           VARCHAR2(5 BYTE)       NOT NULL,
  OSD_ODL_CODE           VARCHAR2(10 BYTE)      NOT NULL,
  OSD_NAME               VARCHAR2(50 BYTE)      NOT NULL,
  OSD_WEF                DATE                   NOT NULL,
  OSD_DIV_HEAD_AGN_CODE  NUMBER(25),
  OSD_SYS_CODE           NUMBER(8)              NOT NULL,
  OSD_REG_CODE           NUMBER,
  OSD_BRN_CODE           NUMBER,
  OSD_POST_LEVEL         VARCHAR2(1 BYTE),
  OSD_MANAGER_ALLWD      VARCHAR2(1 BYTE),
  OSD_OVER_COMM_EARN     VARCHAR2(1 BYTE),
  OSD_ID                 NUMBER,
  OSD_PARENT_ID          NUMBER,
  OSD_WET                DATE,
  OSD_STATUS             VARCHAR2(10 BYTE)      DEFAULT 'ACTIVE',
  OSD_LOCATION_CODE      NUMBER(8),
  OSD_LOCATION           NUMBER(8)
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

COMMENT ON TABLE TQ_CRM.TQC_ORG_SUBDIVISIONS IS 'FOR STORING OF ORGANIZATION SUBDIVISIONS';

COMMENT ON COLUMN TQ_CRM.TQC_ORG_SUBDIVISIONS.OSD_CODE IS 'DIVISION SHORT NAME PK';

COMMENT ON COLUMN TQ_CRM.TQC_ORG_SUBDIVISIONS.OSD_PARENT_OSD_CODE IS 'PARENT DIVISION SHORT NAME FK';

COMMENT ON COLUMN TQ_CRM.TQC_ORG_SUBDIVISIONS.OSD_DLT_CODE IS 'SHORT DESCRIPTION OF HIERARCHY TYPE  FK';

COMMENT ON COLUMN TQ_CRM.TQC_ORG_SUBDIVISIONS.OSD_ODL_CODE IS 'SHORT DESCRIPTION OF HIERARCHY LEVEL FK';

COMMENT ON COLUMN TQ_CRM.TQC_ORG_SUBDIVISIONS.OSD_NAME IS 'DIVISION NAME';

COMMENT ON COLUMN TQ_CRM.TQC_ORG_SUBDIVISIONS.OSD_WEF IS 'WITH EFFECT FROM DATE';

COMMENT ON COLUMN TQ_CRM.TQC_ORG_SUBDIVISIONS.OSD_DIV_HEAD_AGN_CODE IS 'THE HEAD OF DIVISION DEFINED IN TQC_AGENCIES FK';