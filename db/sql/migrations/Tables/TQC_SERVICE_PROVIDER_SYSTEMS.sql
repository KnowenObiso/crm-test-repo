--
-- TQC_SERVICE_PROVIDER_SYSTEMS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SERVICE_PROVIDER_SYSTEMS
(
  SPS_CODE        NUMBER(8)                     NOT NULL,
  SPS_SPR_CODE    NUMBER(8)                     NOT NULL,
  SPS_SYS_CODE    NUMBER(8)                     NOT NULL,
  SPS_WEF         DATE                          NOT NULL,
  SPS_WET         DATE,
  SPS_CREATED_BY  VARCHAR2(30 BYTE)             NOT NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          128K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;