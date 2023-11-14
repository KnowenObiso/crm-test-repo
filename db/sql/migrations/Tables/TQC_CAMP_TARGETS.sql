--
-- TQC_CAMP_TARGETS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CAMP_TARGETS
(
  CMT_CODE      NUMBER(15)                      NOT NULL,
  CMT_ACC_CODE  NUMBER(15)                      NOT NULL,
  CMT_DATE      DATE                            NOT NULL,
  CMT_CMP_CODE  NUMBER(15)                      NOT NULL
)
TABLESPACE CRMDATA
PCTUSED    40
PCTFREE    10
INITRANS   1
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
NOCOMPRESS ;