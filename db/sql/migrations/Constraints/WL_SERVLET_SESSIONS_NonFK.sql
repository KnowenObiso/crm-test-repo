-- 
-- Non Foreign Key Constraints for Table WL_SERVLET_SESSIONS 
-- 
ALTER TABLE TQ_CRM.WL_SERVLET_SESSIONS ADD (
  PRIMARY KEY
  (WL_ID, WL_CONTEXT_PATH)
  USING INDEX
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
               )
  ENABLE VALIDATE);