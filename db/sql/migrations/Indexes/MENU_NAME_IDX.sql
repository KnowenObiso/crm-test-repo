--
-- MENU_NAME_IDX  (Index) 
--
CREATE INDEX TQ_CRM.MENU_NAME_IDX ON TQ_CRM.TQC_MENU
(MENU_NAME)
TABLESPACE CRMDATA
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          192K
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           );