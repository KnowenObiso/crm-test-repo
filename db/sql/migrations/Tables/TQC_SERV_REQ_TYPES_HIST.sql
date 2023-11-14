--
-- TQC_SERV_REQ_TYPES_HIST  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SERV_REQ_TYPES_HIST
(
  SRT_CODE               NUMBER                 NOT NULL,
  SRT_SHT_DESC           VARCHAR2(200 BYTE),
  SRT_DESC               VARCHAR2(200 BYTE),
  SRT_REASON_FOR_CHANGE  VARCHAR2(200 BYTE)
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