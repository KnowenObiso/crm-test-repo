--
-- TQC_ESCALATION_LEVELS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ESCALATION_LEVELS
(
  TQEL_ID         NUMBER,
  TQEL_TQES_ID    NUMBER                        NOT NULL,
  TQEL_TYPE       VARCHAR2(5 BYTE)              NOT NULL,
  TQEL_LEVEL_ID   NUMBER                        NOT NULL,
  TQEL_USER_CODE  NUMBER(8)
)
TABLESPACE USERS
PCTUSED    0
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
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;