--
-- TQC_SERVICE_PROVIDER_REG  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SERVICE_PROVIDER_REG
(
  SREG_CODE        NUMBER(8),
  SREG_SPR_CODE    NUMBER(8)                    NOT NULL,
  SREG_YEAR        NUMBER(4)                    NOT NULL,
  SREG_LICENCE_NO  VARCHAR2(35 BYTE)            NOT NULL,
  SREG_WEF         DATE                         NOT NULL,
  SREG_WET         DATE                         NOT NULL,
  SREG_ACCEPTED    VARCHAR2(1 BYTE)             NOT NULL
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