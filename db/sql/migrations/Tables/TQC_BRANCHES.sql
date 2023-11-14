--
-- TQC_BRANCHES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BRANCHES
(
  BRN_CODE                     NUMBER(8)        NOT NULL,
  BRN_SHT_DESC                 VARCHAR2(15 BYTE) NOT NULL,
  BRN_REG_CODE                 NUMBER(8)        NOT NULL,
  BRN_NAME                     VARCHAR2(50 BYTE) NOT NULL,
  BRN_PHY_ADDRS                VARCHAR2(100 BYTE),
  BRN_EMAIL_ADDRS              VARCHAR2(30 BYTE),
  BRN_POST_ADDRS               VARCHAR2(250 BYTE),
  BRN_TWN_CODE                 NUMBER(8),
  BRN_COU_CODE                 NUMBER(8),
  BRN_CONTACT                  VARCHAR2(30 BYTE),
  BRN_MANAGER                  VARCHAR2(30 BYTE),
  BRN_TEL                      VARCHAR2(100 BYTE),
  BRN_FAX                      VARCHAR2(15 BYTE),
  BRN_GEN_POL_CLM              VARCHAR2(1 BYTE),
  BRN_BNS_CODE                 NUMBER(8)        NOT NULL,
  BRN_AGN_CODE                 NUMBER(15),
  BRN_POST_LEVEL               VARCHAR2(5 BYTE),
  BRN_MNGR_ALLOWED             VARCHAR2(2 BYTE),
  BRN_OVERIDE_COMM_EARNED      VARCHAR2(3 BYTE) DEFAULT NULL,
  BRN_ZIP_CODE                 NUMBER(23),
  BRN_BRA_MNGR_SEQ_NO          VARCHAR2(15 BYTE),
  BRN_AGN_SEQ_NO               VARCHAR2(15 BYTE),
  BRN_POL_PREFIX               VARCHAR2(20 BYTE),
  BRN_POL_SEQ                  NUMBER(22),
  BRN_PROP_SEQ                 NUMBER(22),
  BRN_WEB_DEFAULT              VARCHAR2(1 BYTE) DEFAULT 'N',
  BRN_PRE_CONTRACT_AGN_SEQ_NO  NUMBER(23),
  BRN_COMPT_OV_ON_OWN_BUSS     VARCHAR2(3 BYTE),
  BRN_PRINT_ADDRS              VARCHAR2(1 BYTE),
  BRN_REF                      VARCHAR2(50 BYTE),
  BRN_TOWN_NAME                VARCHAR2(50 BYTE),
  BRN_ADE_CODE                 NUMBER(22),
  BRN_POST_CODE                VARCHAR2(30 BYTE)
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

COMMENT ON COLUMN TQ_CRM.TQC_BRANCHES.BRN_BRA_MNGR_SEQ_NO IS 'Agent sequnce no for Agency managers';

COMMENT ON COLUMN TQ_CRM.TQC_BRANCHES.BRN_AGN_SEQ_NO IS 'Agent sequnce no';