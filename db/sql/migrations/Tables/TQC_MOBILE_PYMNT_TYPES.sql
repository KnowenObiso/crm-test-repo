--
-- TQC_MOBILE_PYMNT_TYPES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_MOBILE_PYMNT_TYPES
(
  MPT_CODE             NUMBER(8)                NOT NULL,
  MPT_SHT_DESC         VARCHAR2(15 BYTE)        NOT NULL,
  MPT_DESC             VARCHAR2(50 BYTE)        NOT NULL,
  MPT_MIN_AMT_ALLOWED  NUMBER(23,5)             NOT NULL,
  MPT_MAX_AMT_ALLOWED  NUMBER(23,5),
  MPT_COU_CODE         NUMBER(8)                NOT NULL
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