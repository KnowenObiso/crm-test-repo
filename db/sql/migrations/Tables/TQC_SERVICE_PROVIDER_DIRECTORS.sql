--
-- TQC_SERVICE_PROVIDER_DIRECTORS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SERVICE_PROVIDER_DIRECTORS
(
  SPD_CODE            NUMBER(8),
  SPD_SPR_CODE        NUMBER(8)                 NOT NULL,
  SPD_YEAR            NUMBER(4)                 NOT NULL,
  SPD_NAME            VARCHAR2(100 BYTE)        NOT NULL,
  SPD_QUALIFICATIONS  VARCHAR2(300 BYTE)
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