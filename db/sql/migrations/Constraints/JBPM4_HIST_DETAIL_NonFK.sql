-- 
-- Non Foreign Key Constraints for Table JBPM4_HIST_DETAIL 
-- 
ALTER TABLE TQ_CRM.JBPM4_HIST_DETAIL ADD (
  PRIMARY KEY
  (DBID_)
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