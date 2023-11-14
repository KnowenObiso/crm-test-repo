--
-- TQC_SYS_APPLICABLE_AREAS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_APPLICABLE_AREAS
(
  SAA_CODE         NUMBER(8)                    NOT NULL,
  SAA_SYS_CODE     NUMBER(8)                    NOT NULL,
  SAA_DESCRIPTION  VARCHAR2(100 BYTE)           NOT NULL
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