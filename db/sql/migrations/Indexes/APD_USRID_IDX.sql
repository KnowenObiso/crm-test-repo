--
-- APD_USRID_IDX  (Index) 
--
CREATE INDEX TQ_CRM.APD_USRID_IDX ON TQ_CRM.TQC_APPROVAL_DETAILS
(APD_USRID)
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