--
-- TAC_NO_PK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.TAC_NO_PK ON TQ_CRM.TQC_ACC_NO
(TAC_NO)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          128K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           );