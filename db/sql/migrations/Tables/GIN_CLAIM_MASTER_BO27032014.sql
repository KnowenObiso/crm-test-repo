--
-- GIN_CLAIM_MASTER_BO27032014  (Table) 
--
CREATE TABLE TQ_CRM.GIN_CLAIM_MASTER_BO27032014
(
  CMB_CLAIM_NO              VARCHAR2(40 BYTE)   NOT NULL,
  CMB_CLAIM_DATE            DATE                NOT NULL,
  CMB_LOCATION              VARCHAR2(50 BYTE),
  CMB_CAUSE_OF_LOSS         VARCHAR2(50 BYTE),
  CMB_LOSS_DATE_TIME        DATE,
  CMB_SCL_CODE              NUMBER(22)          NOT NULL,
  CMB_POL_POLICY_NO         VARCHAR2(30 BYTE)   NOT NULL,
  CMB_POL_REN_ENDOS_NO      VARCHAR2(30 BYTE)   NOT NULL,
  CMB_POL_BATCH_NO          NUMBER(22)          NOT NULL,
  CMB_PRP_CODE              NUMBER(22),
  CMB_EVE_CODE              NUMBER(22)          NOT NULL,
  CMB_IPU_CODE              NUMBER(22)          NOT NULL,
  CMB_IPU_PROPERTY_ID       VARCHAR2(60 BYTE),
  CMB_TOT_CLM_AMT           NUMBER(27,5),
  CMB_LOSS_DESC             VARCHAR2(1000 BYTE),
  CMB_POLICE_INFO_DT        DATE,
  CMB_POLICE_ADDR           VARCHAR2(30 BYTE),
  CMB_RECOV_STEP            VARCHAR2(30 BYTE),
  CMB_GUARDS                VARCHAR2(30 BYTE),
  CMB_SUSPECT               VARCHAR2(30 BYTE),
  CMB_REPAIR_RESPON         VARCHAR2(15 BYTE),
  CMB_DMG_PREVENT           VARCHAR2(20 BYTE),
  CMB_CLAIM_STATUS          VARCHAR2(1 BYTE),
  CMB_TIME                  VARCHAR2(8 BYTE),
  CMB_REJECTED              VARCHAR2(1 BYTE),
  CMB_REASONS_REJECTED      VARCHAR2(500 BYTE),
  CMB_AMT_OWED_DEFAULTER    NUMBER(22,5),
  CMB_DEFAULTER_DEFECTS     VARCHAR2(15 BYTE),
  CMB_DT_REPORTED           DATE,
  CMB_REPORTER              VARCHAR2(15 BYTE),
  CMB_OTHER_COVER_DETAILS   VARCHAR2(15 BYTE),
  CMB_BOOKED_BY             VARCHAR2(15 BYTE),
  CMB_BOOKED_DATE           DATE,
  CMB_MAX_RESERVE_AMT       NUMBER(22,5),
  CMB_OB_NO                 VARCHAR2(15 BYTE),
  CMB_SECT_CODE             NUMBER(22),
  CMB_SECT_SHT_DESC         VARCHAR2(15 BYTE),
  CMB_PER_CODE              NUMBER(22),
  CMB_PER_SHT_DESC          VARCHAR2(15 BYTE),
  CMB_POL_CLIENT_POLICY_NO  VARCHAR2(30 BYTE),
  CMB_IPU_ID                NUMBER(22),
  CMB_CLOSE_DATE            DATE,
  CMB_STATUS_DATE           DATE,
  CMB_GROSS_COM_RETENTION   NUMBER(22,5),
  CMB_COMP_NET_RETENTION    NUMBER(22,5),
  CMB_PRO_CODE              NUMBER(22)          NOT NULL,
  CMB_CLMC_CODE             NUMBER(22),
  CMB_LOP_AUTHORISED        VARCHAR2(1 BYTE),
  CMB_LOP_AUTHORISED_BY     VARCHAR2(30 BYTE),
  CMB_IPU_POLIN_CODE        NUMBER(22),
  CMB_CUR_CODE              NUMBER(22)          NOT NULL,
  CMB_CUR_SYMBOL            VARCHAR2(15 BYTE),
  CMB_TPI_CODE              NUMBER(22),
  CMB_CAT_CODE              NUMBER(22),
  CMB_CAT_SHT_DESC          VARCHAR2(15 BYTE),
  CMB_CLMC_CAS_SHT_DESC     VARCHAR2(15 BYTE),
  CMB_COMP_RETENTION_RATE   NUMBER(22,5),
  CMB_AGNT_AGENT_CODE       NUMBER(22),
  CMB_UW_YEAR               NUMBER(22)          NOT NULL,
  CMB_PRO_SHT_DESC          VARCHAR2(15 BYTE),
  CMB_CHANGE_EVE_CODE       NUMBER(22),
  CMB_CHANGE_CAT_CODE       NUMBER(22),
  CMB_POL_INCEPT_DATE       DATE,
  CMB_POL_INCEPT_UWYR       NUMBER(22),
  CMB_EVE_SHT_DESC          VARCHAR2(30 BYTE),
  CMB_COINSURANCE           VARCHAR2(1 BYTE),
  CMB_COINSURE_LEADER       VARCHAR2(1 BYTE),
  CMB_COINSURANCE_SHARE     NUMBER(22,5),
  CMB_COIN_PAY_TYPE         CHAR(1 BYTE),
  CMB_POL_POLICY_TYPE       CHAR(1 BYTE),
  CMB_BRN_CODE              NUMBER(22),
  CMB_PRP_SHT_DESC          VARCHAR2(30 BYTE),
  CMB_OLD_CLAIM_NO          VARCHAR2(40 BYTE),
  CMB_CLMC_DESC             VARCHAR2(60 BYTE),
  CMB_CLIENT_PRP_CODE       NUMBER(22),
  CMB_CLIENT_SHT_DESC       VARCHAR2(30 BYTE),
  CMB_COVT_SHT_DESC         VARCHAR2(20 BYTE),
  CMB_POL_LOADED            VARCHAR2(1 BYTE),
  CMB_PROCESS_RV            NUMBER(22),
  CMB_CURR_RESERVE          NUMBER(25,2),
  CMB_COVT_CODE             NUMBER(22),
  CMB_BRN_SHT_DESC          VARCHAR2(15 BYTE),
  CMB_OLD_POLICY_NO         VARCHAR2(20 BYTE),
  CMB_LOP_AUTHORISED_DATE   DATE,
  CMB_ADMIT_LIABILITY       VARCHAR2(2 BYTE)    NOT NULL,
  CMB_BOC_STATUS            VARCHAR2(1 BYTE),
  CMB_LOSS_TIME             DATE,
  CMB_OB_DATE               DATE,
  CMB_POLICE_INFORMED       VARCHAR2(1 BYTE),
  CMB_POLICE_STATION        VARCHAR2(60 BYTE),
  CMB_OB_NUMBER             VARCHAR2(60 BYTE),
  CMB_INSTG_LAST_NAME       VARCHAR2(100 BYTE),
  CMB_INSTG_OTHER_NAMES     VARCHAR2(100 BYTE),
  CMB_INSTG_TEL_NO          NUMBER(20),
  CMB_ABSTRACT_NO           VARCHAR2(60 BYTE),
  CMB_OLD_CLAIM_NUMBER      VARCHAR2(15 BYTE),
  CMB_DIV_CODE              NUMBER,
  CMB_TP_RECOVER            VARCHAR2(1 BYTE),
  CMB_EXS_RECOVER           VARCHAR2(1 BYTE),
  CMB_SLVG_RECOVER          VARCHAR2(1 BYTE),
  CMB_IPU_VALUE             NUMBER(20,5),
  CMB_IPU_WEF               DATE,
  CMB_IPU_WET               DATE,
  CMB_RAINING               VARCHAR2(1 BYTE),
  CMB_VISIBILITY            VARCHAR2(20 BYTE),
  CMB_ROAD_SURFACE          VARCHAR2(20 BYTE),
  CMB_INTENDED_PROSECUTION  VARCHAR2(1 BYTE),
  CMB_EX_GRATIA             VARCHAR2(1 BYTE),
  CMB_EXG_REASON            VARCHAR2(500 BYTE),
  CMB_REV_REASON            VARCHAR2(20 BYTE),
  CMB_PDS_SHT_DESC          VARCHAR2(50 BYTE),
  CMB_CHARGE_PENALTY        CHAR(1 BYTE),
  CMB_SALVAGE_RETAINED      VARCHAR2(1 BYTE),
  CMB_PRD_INCAPACITY        NUMBER(23),
  CMB_PDS_CODE              NUMBER(23),
  CMB_SALARY_BASED          VARCHAR2(1 BYTE),
  CMB_REIN_POOL_RATE        NUMBER(23,2),
  CMB_REIN_POOL_APPL        VARCHAR2(1 BYTE),
  CMB_SUBM_INSURER_DATE     DATE,
  CMB_NEXT_REVIEW_DT        DATE,
  CMB_INS_CONT_PERSN        VARCHAR2(100 BYTE),
  CMB_INS_CLAIM_NO          VARCHAR2(50 BYTE),
  CMB_AVRG_BASIC_SALARY     NUMBER(22,5),
  CMB_AVRG_EARNINGS         NUMBER(22,5),
  CMB_OFFDUTY_WEF_DT        DATE,
  CMB_OFFDUTY_WET_DT        DATE,
  CMB_PRIORITY_LVL          VARCHAR2(1 BYTE),
  CMB_COMM_MODE             NUMBER
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