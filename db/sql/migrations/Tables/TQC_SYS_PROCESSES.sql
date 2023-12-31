--
-- TQC_SYS_PROCESSES  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYS_PROCESSES
(
  SPRC_CODE       NUMBER(15),
  SPRC_SYS_CODE   NUMBER(15),
  SPRC_PROCESS    VARCHAR2(75 BYTE),
  SPRC_BPM_ID     NUMBER(15),
  SPRC_SHT_DESC   VARCHAR2(5 BYTE),
  SPRC_JPDL_DESC  VARCHAR2(50 BYTE),
  SPRC_VISIBLE    VARCHAR2(1 BYTE)              DEFAULT 'Y'
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