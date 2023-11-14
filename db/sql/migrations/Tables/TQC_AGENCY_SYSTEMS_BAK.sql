--
-- TQC_AGENCY_SYSTEMS_BAK  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_SYSTEMS_BAK
(
  ASYS_SYS_CODE  NUMBER(22),
  ASYS_AGN_CODE  NUMBER(22),
  ASYS_WEF       DATE                           NOT NULL,
  ASYS_WET       DATE,
  ASYS_COMMENT   VARCHAR2(100 BYTE),
  ASYS_OSD_CODE  VARCHAR2(10 BYTE)
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