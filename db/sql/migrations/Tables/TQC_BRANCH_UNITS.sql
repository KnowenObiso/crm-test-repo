--
-- TQC_BRANCH_UNITS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BRANCH_UNITS
(
  BRU_CODE                     NUMBER(8)        NOT NULL,
  BRU_BRN_CODE                 NUMBER(8)        NOT NULL,
  BRU_SHT_DESC                 VARCHAR2(15 BYTE) NOT NULL,
  BRU_NAME                     VARCHAR2(100 BYTE) NOT NULL,
  BRU_SUPERVISOR               VARCHAR2(60 BYTE),
  BRU_STATUS                   VARCHAR2(1 BYTE),
  BRU_AGN_CODE                 NUMBER(15),
  BRU_BRA_CODE                 NUMBER(15),
  BRU_MANAGER                  VARCHAR2(60 BYTE),
  BRU_POST_LEVEL               VARCHAR2(20 BYTE),
  BRU_MNGR_ALLOWED             VARCHAR2(5 BYTE),
  BRU_OVERIDE_COMM_EARNED      VARCHAR2(5 BYTE) DEFAULT NULL,
  BRU_AGN_SEQ_NO               VARCHAR2(15 BYTE),
  BRU_UNT_SHT_DESC_PREFIX      VARCHAR2(30 BYTE),
  BRU_COMPT_OV_ON_OWN_BUSS     VARCHAR2(200 BYTE) DEFAULT NULL,
  BRU_POL_SEQ                  NUMBER(22),
  BRU_PROP_SEQ                 NUMBER(22),
  BRU_PRE_CONTRACT_AGN_SEQ_NO  NUMBER(23)
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

COMMENT ON COLUMN TQ_CRM.TQC_BRANCH_UNITS.BRU_AGN_SEQ_NO IS 'Agent sequnce no';