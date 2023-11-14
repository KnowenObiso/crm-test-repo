--
-- TQC_GROUP_CLNT_DTLS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_GROUP_CLNT_DTLS
(
  GRPD_CODE       NUMBER                        NOT NULL,
  GRPD_CLNT_CODE  NUMBER,
  GRPD_GRP_CODE   NUMBER
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