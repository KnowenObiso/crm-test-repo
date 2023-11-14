--
-- TQC_SYS_MOD_SUBUNITS_INPUTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_MOD_SUBUNITS_INPUTS
(
  TSMSI_CODE        NUMBER                      NOT NULL,
  TSMSI_TSMSD_CODE  NUMBER                      NOT NULL,
  TSMSI_VALUE       VARCHAR2(1000 BYTE),
  TSMSI_MODE_CODE   NUMBER,
  TSMSI_ROW         NUMBER,
  TSMSI_COLUMN      NUMBER
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