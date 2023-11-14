--
-- TQC_DMS_DOC_METADATA  (Table) 
--
CREATE TABLE TQ_CRM.TQC_DMS_DOC_METADATA
(
  DDM_CODE      NUMBER,
  DDM_SDT_CODE  NUMBER                          NOT NULL,
  DDM_NAME      VARCHAR2(30 BYTE)               NOT NULL,
  DDM_DESC      VARCHAR2(100 BYTE),
  DDM_MAPPED    VARCHAR2(30 BYTE)
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