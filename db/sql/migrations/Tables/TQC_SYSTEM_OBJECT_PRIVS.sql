--
-- TQC_SYSTEM_OBJECT_PRIVS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SYSTEM_OBJECT_PRIVS
(
  SOP_OBJ_CODE  NUMBER(8)                       NOT NULL,
  SOP_SRL_CODE  NUMBER(8)                       NOT NULL,
  SOP_INSERT    VARCHAR2(1 BYTE),
  SOP_DELETE    VARCHAR2(1 BYTE),
  SOP_SELECT    VARCHAR2(1 BYTE),
  SOP_UPDATE    VARCHAR2(1 BYTE),
  SOP_EXECUTE   VARCHAR2(1 BYTE)
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