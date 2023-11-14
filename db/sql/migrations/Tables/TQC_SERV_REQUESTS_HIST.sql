--
-- TQC_SERV_REQUESTS_HIST  (Table) 
--
CREATE TABLE TQ_CRM.TQC_SERV_REQUESTS_HIST
(
  TSR_CODE             NUMBER                   NOT NULL,
  TSR_TSRC_CODE        NUMBER                   NOT NULL,
  TSR_TYPE             VARCHAR2(50 BYTE)        NOT NULL,
  TSR_ACC_TYPE         VARCHAR2(2 BYTE)         NOT NULL,
  TSR_ACC_CODE         NUMBER,
  TSR_DATE             DATE                     NOT NULL,
  TSR_ASSIGNEE         NUMBER                   NOT NULL,
  TSR_OWNER_TYPE       VARCHAR2(2 BYTE)         NOT NULL,
  TSR_OWNER_CODE       NUMBER                   NOT NULL,
  TSR_STATUS           VARCHAR2(10 BYTE)        NOT NULL,
  TSR_DUE_DATE         DATE                     NOT NULL,
  TSR_RESOLUTION_DATE  DATE,
  TSR_SUMMARY          VARCHAR2(100 BYTE)       NOT NULL,
  TSR_DESCRIPTION      VARCHAR2(500 BYTE)       NOT NULL,
  TSR_SOLUTION         VARCHAR2(500 BYTE),
  TSR_TCB_CODE         NUMBER,
  TSR_COMM_MODE        VARCHAR2(10 BYTE),
  TSR_REF_NUMBER       VARCHAR2(200 BYTE),
  TSR_COMMENTS         VARCHAR2(200 BYTE),
  TSR_SRT_CODE         NUMBER
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