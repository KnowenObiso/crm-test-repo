--
-- BBB_CODE_PK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.BBB_CODE_PK ON TQ_CRM.TQC_BANCASSURANCE_BRANCHES
(BBB_CODE)
TABLESPACE USERS
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
            BUFFER_POOL      DEFAULT
           );