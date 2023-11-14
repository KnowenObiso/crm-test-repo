--
-- TQC_AGENCY_CLIENTS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_AGENCY_CLIENTS
(
  AGNC_CODE       NUMBER(15)                    NOT NULL,
  AGNC_AGN_CODE   NUMBER(15)                    NOT NULL,
  AGNC_CLNT_CODE  NUMBER(15)                    NOT NULL
)
TABLESPACE USERS
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          2M
            NEXT             1M
            MAXSIZE          UNLIMITED
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCOMPRESS ;