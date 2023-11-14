-- 
-- Non Foreign Key Constraints for Table JBPM4_ID_USER 
-- 
ALTER TABLE TQ_CRM.JBPM4_ID_USER ADD (
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