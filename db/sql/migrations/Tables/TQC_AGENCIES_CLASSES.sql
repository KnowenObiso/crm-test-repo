--
-- TQC_AGENCIES_CLASSES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCIES_CLASSES
(
  AGNC_CLASS_CODE  NUMBER(8)                    NOT NULL,
  AGNC_CLASS_DESC  VARCHAR2(50 BYTE)
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