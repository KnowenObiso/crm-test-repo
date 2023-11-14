--
-- TQC_SYS_SUBUNITS_OPTIONS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_SUBUNITS_OPTIONS
(
  TSSO_CODE         NUMBER                      NOT NULL,
  TSSO_TSMSD_CODE   NUMBER                      NOT NULL,
  TSSO_OPTION_NAME  VARCHAR2(50 BYTE)           NOT NULL,
  TSSO_OPTION_DESC  VARCHAR2(50 BYTE)           NOT NULL,
  TSSO_ORDER        NUMBER                      NOT NULL,
  TSSO_TYPE         VARCHAR2(10 BYTE)
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