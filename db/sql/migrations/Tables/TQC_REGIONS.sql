--
-- TQC_REGIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_REGIONS
(
  REG_CODE                     NUMBER(8)        NOT NULL,
  REG_ORG_CODE                 NUMBER(8)        NOT NULL,
  REG_SHT_DESC                 VARCHAR2(15 BYTE) NOT NULL,
  REG_NAME                     VARCHAR2(100 BYTE) NOT NULL,
  REG_WEF                      DATE             NOT NULL,
  REG_WET                      DATE,
  REG_AGN_CODE                 NUMBER(15),
  REG_POST_LEVEL               VARCHAR2(5 BYTE),
  REG_MNGR_ALLOWED             VARCHAR2(3 BYTE),
  REG_OVERIDE_COMM_EARNED      VARCHAR2(3 BYTE) DEFAULT NULL,
  REG_BRN_MNGR_SEQ_NO          VARCHAR2(15 BYTE),
  REG_AGN_SEQ_NO               VARCHAR2(15 BYTE),
  REG_POL_SEQ                  NUMBER(22),
  REG_PROP_SEQ                 NUMBER(22),
  REG_PRE_CONTRACT_AGN_SEQ_NO  NUMBER(23),
  REG_COMPT_OV_ON_OWN_BUSS     VARCHAR2(3 BYTE)
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

COMMENT ON COLUMN TQ_CRM.TQC_REGIONS.REG_BRN_MNGR_SEQ_NO IS 'Agent sequnce no for Branch managers';

COMMENT ON COLUMN TQ_CRM.TQC_REGIONS.REG_AGN_SEQ_NO IS 'Agent sequnce no';