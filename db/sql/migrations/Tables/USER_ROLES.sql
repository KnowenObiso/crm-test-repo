--
-- USER_ROLES  (Table) 
--
CREATE TABLE TQ_CRM.USER_ROLES
(
  UR_USR_ID  VARCHAR2(15 BYTE),
  UR_ROL_ID  VARCHAR2(15 BYTE)
)
TABLESPACE CRMDATA
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
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;