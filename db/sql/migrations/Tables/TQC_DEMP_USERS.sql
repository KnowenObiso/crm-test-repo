--
-- TQC_DEMP_USERS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_DEMP_USERS
(
  USR_CODE               NUMBER(8),
  USR_USERNAME           VARCHAR2(15 BYTE)      NOT NULL,
  USR_NAME               VARCHAR2(60 BYTE),
  USR_LAST_DATE          DATE,
  USR_DT_CREATED         DATE                   NOT NULL,
  USR_STATUS             VARCHAR2(1 BYTE),
  USR_PWD                VARCHAR2(50 BYTE),
  USR_CREATED_BY         VARCHAR2(15 BYTE),
  USR_EMAIL              VARCHAR2(35 BYTE),
  USR_PER_RANK_ID        NUMBER,
  USR_PER_RANK_SHT_DESC  VARCHAR2(30 BYTE),
  USR_PER_ID             NUMBER(8),
  USR_PERSONEL_RANK      VARCHAR2(75 BYTE),
  USR_PWD_CHANGED        DATE                   NOT NULL,
  USR_PWD_RESET          VARCHAR2(2 BYTE),
  USR_LOGIN_ATTEMPTS     NUMBER(8),
  USR_SIGN               VARCHAR2(2 BYTE)       DEFAULT 'N',
  USR_REF                VARCHAR2(15 BYTE),
  USR_TYPE               VARCHAR2(1 BYTE)       DEFAULT 'U',
  USR_SIGNATURE          LONG RAW,
  USR_ACCT_MGR           VARCHAR2(1 BYTE)
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