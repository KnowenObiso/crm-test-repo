--
-- TQC_BRANCH_DEPARTMENT_UK  (Index) 
--
CREATE UNIQUE INDEX TQ_CRM.TQC_BRANCH_DEPARTMENT_UK ON TQ_CRM.TQC_BRANCH_DEPARTMENTS
(BDEP_BRN_CODE, BDEP_DEP_CODE, BDEP_WEF)
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