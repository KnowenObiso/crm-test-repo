--
-- TQC_REGIONS_BAK  (Table) 
--
CREATE TABLE TQ_CRM.TQC_REGIONS_BAK
(
  REG_CODE                 NUMBER(8)            NOT NULL,
  REG_ORG_CODE             NUMBER(8)            NOT NULL,
  REG_SHT_DESC             VARCHAR2(15 BYTE)    NOT NULL,
  REG_NAME                 VARCHAR2(100 BYTE)   NOT NULL,
  REG_WEF                  DATE                 NOT NULL,
  REG_WET                  DATE,
  REG_AGN_CODE             NUMBER(15),
  REG_POST_LEVEL           VARCHAR2(5 BYTE),
  REG_MNGR_ALLOWED         VARCHAR2(3 BYTE),
  REG_OVERIDE_COMM_EARNED  VARCHAR2(3 BYTE),
  REG_BRN_MNGR_SEQ_NO      VARCHAR2(15 BYTE),
  REG_AGN_SEQ_NO           VARCHAR2(15 BYTE)
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