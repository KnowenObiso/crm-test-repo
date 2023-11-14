--
-- TQC_BANKS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BANKS
(
  BNK_CODE                   NUMBER(8)          NOT NULL,
  BNK_BANK_NAME              VARCHAR2(100 BYTE),
  BNK_REMARKS                VARCHAR2(250 BYTE),
  BNK_SHT_DESC               VARCHAR2(15 BYTE),
  BNK_DDR_CODE               NUMBER(3),
  DD_FORMAT_DESC             VARCHAR2(250 BYTE),
  BNK_FORWARDING_BNK_CODE    NUMBER(8),
  BNK_KBA_CODE               NUMBER(2),
  BNK_DD_FORMAT              NUMBER(1),
  BNK_EFT_SUPPORTED          VARCHAR2(1 BYTE)   DEFAULT 'N',
  BNK_LOGO                   BLOB,
  BNK_CLASS_TYPE             VARCHAR2(1 BYTE),
  BNK_ACCNT_DIGIT_NO         NUMBER,
  BNK_NEGOTIATED_BANK        VARCHAR2(1 BYTE),
  BNK_ADMINISTRATION_CHARGE  NUMBER,
  BNK_DD_SUPPORTED           VARCHAR2(1 BYTE)   DEFAULT 'N',
  BNK_WEF                    DATE,
  BNK_WET                    DATE,
  BNK_ACCNT_DIGIT_NO_2       NUMBER(25),
  BNK_ACCNT_DIGIT_NO_3       NUMBER(25),
  BNK_STATUS                 VARCHAR2(1 BYTE),
  BNK_ACC_MAX_NO             NUMBER(10),
  BNK_ACC_MIN_NO             NUMBER(10),
  BNK_COU_CODE               NUMBER(8)
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