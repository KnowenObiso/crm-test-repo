--
-- TQC_BPM_SCREENS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BPM_SCREENS
(
  SCRN_CODE       NUMBER,
  SCRN_TASK_DESC  VARCHAR2(100 BYTE),
  SCRN_NAME       VARCHAR2(50 BYTE),
  SCRN_SYS_CODE   NUMBER(25)                    DEFAULT 27                    NOT NULL
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