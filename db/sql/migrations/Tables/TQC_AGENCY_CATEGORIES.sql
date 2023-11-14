--
-- TQC_AGENCY_CATEGORIES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_CATEGORIES
(
  AGC_CODE      NUMBER(12)                      NOT NULL,
  AGC_SHT_DESC  VARCHAR2(15 BYTE),
  AGC_DESC      VARCHAR2(50 BYTE)
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