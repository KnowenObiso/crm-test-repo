--
-- TQC_USERS_PREV_POSTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_USERS_PREV_POSTS
(
  USRPP_CODE        NUMBER(8)                   NOT NULL,
  USRPP_SYS_CODE    NUMBER(8)                   NOT NULL,
  USRPP_SPOST_CODE  NUMBER(8)                   NOT NULL,
  USRPP_WEF         DATE                        NOT NULL,
  USRPP_WET         DATE                        NOT NULL
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