--
-- TQC_EMPLOYEES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_EMPLOYEES
(
  STAFF_NO              NUMBER(8)               NOT NULL,
  STAFF_NAME            VARCHAR2(40 BYTE)       NOT NULL,
  STAFF_GENDER          CHAR(1 BYTE)            NOT NULL,
  STAFF_NATIONAL_ID     VARCHAR2(15 BYTE),
  STAFF_DOB             DATE,
  STAFF_DATE_EMPLOYEED  DATE,
  STAFF_PIN             VARCHAR2(11 BYTE),
  STAFF_DEP_CODE        NUMBER(8),
  STAFF_EXIT_DATE       DATE
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