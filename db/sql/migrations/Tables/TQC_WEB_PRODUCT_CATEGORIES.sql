--
-- TQC_WEB_PRODUCT_CATEGORIES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_WEB_PRODUCT_CATEGORIES
(
  TWPC_CODE         NUMBER,
  TWPC_SYS_CODE     NUMBER                      NOT NULL,
  TWPC_NAME         VARCHAR2(50 BYTE)           NOT NULL,
  TWPC_DESCRIPTION  VARCHAR2(4000 BYTE)
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