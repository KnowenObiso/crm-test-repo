--
-- JBPM4_HIST_DETAIL  (Table) 
--
CREATE TABLE TQ_CRM.JBPM4_HIST_DETAIL
(
  DBID_        NUMBER(19)                       NOT NULL,
  CLASS_       VARCHAR2(255 BYTE)               NOT NULL,
  DBVERSION_   NUMBER(10)                       NOT NULL,
  USERID_      VARCHAR2(255 BYTE),
  TIME_        DATE,
  HPROCI_      NUMBER(19),
  HPROCIIDX_   NUMBER(10),
  HACTI_       NUMBER(19),
  HACTIIDX_    NUMBER(10),
  HTASK_       NUMBER(19),
  HTASKIDX_    NUMBER(10),
  HVAR_        NUMBER(19),
  HVARIDX_     NUMBER(10),
  MESSAGE_     CLOB,
  OLD_INT_     NUMBER(10),
  NEW_INT_     NUMBER(10),
  OLD_STR_     VARCHAR2(255 BYTE),
  NEW_STR_     VARCHAR2(255 BYTE),
  OLD_TIME_    DATE,
  NEW_TIME_    DATE,
  PARENT_      NUMBER(19),
  PARENT_IDX_  NUMBER(10)
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