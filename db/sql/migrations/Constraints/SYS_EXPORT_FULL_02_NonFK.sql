-- 
-- Non Foreign Key Constraints for Table SYS_EXPORT_FULL_02 
-- 
ALTER TABLE TQ_CRM.SYS_EXPORT_FULL_02 ADD (
  UNIQUE (PROCESS_ORDER, DUPLICATE)
  USING INDEX
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
               )
  ENABLE VALIDATE);