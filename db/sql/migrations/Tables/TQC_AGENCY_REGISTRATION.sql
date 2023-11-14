--
-- TQC_AGENCY_REGISTRATION  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_REGISTRATION
(
  AREG_CODE       NUMBER(8)                     NOT NULL,
  AREG_AGN_CODE   NUMBER(15)                    NOT NULL,
  AREG_YEAR       NUMBER(4)                     NOT NULL,
  AREG_REG_NO     VARCHAR2(35 BYTE)             NOT NULL,
  AREG_WEF        DATE                          NOT NULL,
  AREG_WET        DATE                          NOT NULL,
  AREG_ACCEPTED   VARCHAR2(1 BYTE)              NOT NULL,
  AREG_GRACE_PRD  NUMBER
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