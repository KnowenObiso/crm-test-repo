--
-- TQC_BPM_TICKETS_BAK  (Table) 
--
CREATE TABLE TQ_CRM.TQC_BPM_TICKETS_BAK
(
  TCKT_CODE         NUMBER(15),
  TCKT_SYS_CODE     NUMBER(8)                   NOT NULL,
  TCKT_SYS_MODULE   VARCHAR2(50 BYTE)           NOT NULL,
  TCKT_CLNT_CODE    NUMBER(15),
  TCKT_AGN_CODE     NUMBER(15),
  TCKT_POL_CODE     NUMBER(15),
  TCKT_POL_NO       VARCHAR2(35 BYTE),
  TCKT_CLM_NO       VARCHAR2(35 BYTE),
  TCKT_QUOT_CODE    NUMBER(15),
  TCKT_BY           VARCHAR2(35 BYTE),
  TCKT_DATE         DATE,
  TCKT_PROCESS_ID   VARCHAR2(50 BYTE),
  TCKT_QUO_NO       VARCHAR2(100 BYTE),
  TCKT_SPRSA_CODE   NUMBER(15),
  TCKT_ENDR_CODE    NUMBER(15),
  TCKT_PROD_TYPE    VARCHAR2(20 BYTE),
  TCKT_TO           VARCHAR2(100 BYTE),
  TCKT_REMARKS      VARCHAR2(300 BYTE),
  TCKT_ENDORSEMENT  VARCHAR2(25 BYTE),
  TCKT_TRANSNO      NUMBER(15),
  TCKT_ACTIVE       VARCHAR2(10 BYTE),
  TCKT_PRP_CODE     NUMBER
)
TABLESPACE CRMDATA
PCTUSED    40
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          384K
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