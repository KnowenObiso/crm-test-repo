--
-- TQC_HOUSEHOLD_DTLS  (Table) 
--
CREATE TABLE TQ_CRM.TQC_HOUSEHOLD_DTLS
(
  HHD_ID         NUMBER                         NOT NULL,
  HHD_HH_ID      NUMBER                         NOT NULL,
  HHD_CLNT_CODE  NUMBER                         NOT NULL
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