--
-- TQC_CLIENT_BRANCHES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_BRANCHES
(
  TCB_CODE       NUMBER,
  TCB_CLNT_CODE  NUMBER                         NOT NULL,
  TCB_SHT_DESC   VARCHAR2(10 BYTE)              NOT NULL,
  TCB_NAME       VARCHAR2(50 BYTE)              NOT NULL
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