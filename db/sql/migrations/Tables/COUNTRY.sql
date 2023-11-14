--
-- COUNTRY  (Table) 
--
CREATE TABLE TQ_CRM.COUNTRY
(
  ID         NUMBER                             NOT NULL,
  ISO        VARCHAR2(2 BYTE)                   NOT NULL,
  NAME       VARCHAR2(200 BYTE)                 NOT NULL,
  NICENAME   VARCHAR2(200 BYTE)                 NOT NULL,
  ISO3       VARCHAR2(3 BYTE)                   DEFAULT NULL,
  NUMCODE    NUMBER                             DEFAULT NULL,
  PHONECODE  NUMBER                             NOT NULL
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