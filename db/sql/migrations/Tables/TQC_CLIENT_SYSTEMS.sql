--
-- TQC_CLIENT_SYSTEMS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CLIENT_SYSTEMS
(
  CSYS_CODE       NUMBER(8),
  CSYS_CLNT_CODE  NUMBER(15)                    NOT NULL,
  CSYS_SYS_CODE   NUMBER(8)                     NOT NULL,
  CSYS_WEF        DATE                          NOT NULL,
  CSYS_WET        DATE
)
TABLESPACE CRMDATA
PCTUSED    40
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          3M
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