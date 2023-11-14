--
-- TQC_SERV_REQ_CAT  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SERV_REQ_CAT
(
  TSRC_CODE      NUMBER                         NOT NULL,
  TSRC_NAME      VARCHAR2(300 BYTE)             NOT NULL,
  TSRC_VALIDITY  NUMBER,
  TSRC_USR_CODE  NUMBER,
  TSRC_BRN_CODE  NUMBER,
  TSRC_DEFAULT   VARCHAR2(1 BYTE)               DEFAULT 'N',
  TSRC_SRT_CODE  NUMBER
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