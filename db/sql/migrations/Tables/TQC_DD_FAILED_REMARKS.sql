--
-- TQC_DD_FAILED_REMARKS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_DD_FAILED_REMARKS
(
  DFR_CODE           NUMBER                     NOT NULL,
  DFR_FAILED_REMARK  VARCHAR2(200 BYTE)         NOT NULL,
  DFR_APPL_LEVEL     VARCHAR2(200 BYTE)
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