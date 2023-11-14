--
-- TQC_USER_SYSTEMS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USER_SYSTEMS
(
  USYS_CODE        NUMBER(8)                    NOT NULL,
  USYS_USR_CODE    NUMBER(8)                    NOT NULL,
  USYS_SYS_CODE    NUMBER(8)                    NOT NULL,
  USYS_WEF         DATE                         NOT NULL,
  USYS_WET         DATE,
  USYS_SPOST_CODE  NUMBER(8)
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