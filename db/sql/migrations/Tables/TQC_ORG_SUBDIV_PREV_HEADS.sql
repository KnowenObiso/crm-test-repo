--
-- TQC_ORG_SUBDIV_PREV_HEADS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ORG_SUBDIV_PREV_HEADS
(
  OSDPH_CODE           NUMBER(8)                NOT NULL,
  OSDPH_OSD_CODE       VARCHAR2(10 BYTE)        NOT NULL,
  OSDPH_PREV_AGN_CODE  NUMBER(8)                NOT NULL,
  OSDPH_WET            DATE                     NOT NULL,
  OSDPH_OSD_ID         NUMBER
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