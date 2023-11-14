--
-- IDX_DEPLPROP_DEPL  (Index) 
--
CREATE INDEX TQ_CRM.IDX_DEPLPROP_DEPL ON TQ_CRM.JBPM4_DEPLOYPROP
(DEPLOYMENT_)
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