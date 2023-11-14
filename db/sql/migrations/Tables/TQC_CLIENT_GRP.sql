--
-- TQC_CLIENT_GRP  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_GRP
(
  CLNTG_CODE  NUMBER                            NOT NULL,
  CLNG_NAME   VARCHAR2(100 BYTE)                NOT NULL,
  CLNT_MIN    NUMBER                            NOT NULL,
  CLNT_MAX    NUMBER                            NOT NULL
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