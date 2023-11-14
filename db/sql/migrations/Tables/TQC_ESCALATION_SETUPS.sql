--
-- TQC_ESCALATION_SETUPS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_ESCALATION_SETUPS
(
  TQES_ID        NUMBER,
  TQES_SYS_CODE  NUMBER                         NOT NULL,
  TQES_DESC      VARCHAR2(200 BYTE)             NOT NULL
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