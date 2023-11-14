--
-- GRPD_UNIQUE  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.GRPD_UNIQUE ON TQ_CRM.TQC_GROUP_CLNT_DTLS
(GRPD_CLNT_CODE, GRPD_GRP_CODE)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
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
           );