--
-- TQC_BRANCH_AGENCIES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BRANCH_AGENCIES
(
  BRA_CODE                     NUMBER(8)        NOT NULL,
  BRA_BRN_CODE                 NUMBER(8)        NOT NULL,
  BRA_SHT_DESC                 VARCHAR2(15 BYTE) NOT NULL,
  BRA_NAME                     VARCHAR2(100 BYTE) NOT NULL,
  BRA_STATUS                   VARCHAR2(1 BYTE),
  BRA_MANAGER                  VARCHAR2(60 BYTE),
  BRA_AGN_CODE                 NUMBER(15)       DEFAULT NULL,
  BRA_POST_LEVEL               VARCHAR2(5 BYTE),
  BRA_MNGR_ALLOWED             VARCHAR2(5 BYTE),
  BRA_OVERIDE_COMM_EARNED      VARCHAR2(5 BYTE) DEFAULT NULL,
  BRA_BRU_MNGR_SEQ_NO          VARCHAR2(15 BYTE),
  BRA_AGN_SEQ_NO               VARCHAR2(15 BYTE),
  BRA_UNT_SHT_DESC_PREFIX      VARCHAR2(30 BYTE),
  BRA_POL_SEQ                  NUMBER(22),
  BRA_PROP_SEQ                 NUMBER(22),
  BRA_PRE_CONTRACT_AGN_SEQ_NO  NUMBER(23),
  BRA_COMPT_OV_ON_OWN_BUSS     VARCHAR2(3 BYTE)
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

COMMENT ON COLUMN TQ_CRM.TQC_BRANCH_AGENCIES.BRA_BRU_MNGR_SEQ_NO IS 'Agent sequnce no for Unit managers';

COMMENT ON COLUMN TQ_CRM.TQC_BRANCH_AGENCIES.BRA_AGN_SEQ_NO IS 'Agent sequnce no';