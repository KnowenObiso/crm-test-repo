--
-- TQC_SYS_PROCESS_REPORTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_PROCESS_REPORTS
(
  SPRR_CODE       NUMBER,
  SPRR_RPT_CODE   NUMBER,
  SPRR_SPRC_CODE  NUMBER,
  SPRR_DESC       VARCHAR2(100 BYTE),
  SPRR_TYPE       VARCHAR2(30 BYTE),
  SPRR_SDT_CODE   NUMBER
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