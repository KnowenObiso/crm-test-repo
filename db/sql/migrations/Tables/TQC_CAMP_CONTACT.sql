--
-- TQC_CAMP_CONTACT  (Table) 
--
CREATE TABLE TQ_CRM.TQC_CAMP_CONTACT
(
  CMC_CODE      NUMBER(15)                      NOT NULL,
  CMC_AGN_CODE  NUMBER(15)                      NOT NULL,
  CMC_DATE      DATE                            NOT NULL,
  CMC_CMP_CODE  NUMBER(15)                      NOT NULL
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