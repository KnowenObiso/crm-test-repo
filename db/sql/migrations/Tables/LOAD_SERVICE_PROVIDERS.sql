--
-- LOAD_SERVICE_PROVIDERS  (Table) 
--
CREATE TABLE TQ_CRM.LOAD_SERVICE_PROVIDERS
(
  CODE             VARCHAR2(5 BYTE),
  PROVIDERNAME     VARCHAR2(200 BYTE),
  IDNUMBER         VARCHAR2(15 BYTE),
  PHYSICALADDRESS  VARCHAR2(100 BYTE),
  POSTALADDRESS    VARCHAR2(100 BYTE),
  TOWNCODE         VARCHAR2(50 BYTE),
  SPRPHONE         VARCHAR2(50 BYTE),
  SPRFAXNUMBER     VARCHAR2(50 BYTE),
  SPREMAIL         VARCHAR2(100 BYTE)
)
TABLESPACE CRMDATA
PCTUSED    40
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
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;