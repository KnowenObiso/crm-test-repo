--
-- TQC_USER_ABSENTEEISM  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USER_ABSENTEEISM
(
  TQUA_ID            NUMBER                     NOT NULL,
  TQUA_USR_CODE      NUMBER                     NOT NULL,
  TQUA_WEF           DATE                       NOT NULL,
  TQUA_WET           DATE,
  TQUA_REASON        VARCHAR2(1000 BYTE)        NOT NULL,
  TQUA_STD_USR_CODE  NUMBER                     NOT NULL
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