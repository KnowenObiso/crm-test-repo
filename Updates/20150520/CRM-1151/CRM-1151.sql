SET DEFINE OFF;
Insert into TQ_CRM.TQC_PARAMETERS
   (PARAM_CODE, PARAM_NAME, PARAM_VALUE, PARAM_STATUS, PARAM_DESC)
 Values
   (20150520, 'SYSDATEFORMAT', 'dd-MMM-yyyy hh:mm:ss', 'ACTIVE', 'THE SYSTEM DATE FORMAT');
COMMIT;

----------------------------------------------------------------------------

CREATE TABLE TQ_GIS.GIN_INSTALMENT_DAYS
(
  ID_INSTLMT_DAY  NUMBER(4)                     NOT NULL
);


CREATE OR REPLACE PUBLIC SYNONYM GIN_INSTALMENT_DAYS FOR TQ_GIS.GIN_INSTALMENT_DAYS;


ALTER TABLE TQ_GIS.GIN_INSTALMENT_DAYS ADD (
  CONSTRAINT GIN_INSTALMENT_DAYS_C01
  CHECK (ID_INSTLMT_DAY IN (0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31)));
  
  --------------------------------------------------------------------------------
  CREATE TABLE TQ_CRM.TQC_DD_FAILED_REMARKS
(
  DFR_CODE           NUMBER                     NOT NULL,
  DFR_FAILED_REMARK  VARCHAR2(200 BYTE)         NOT NULL,
  DFR_APPL_LEVEL     VARCHAR2(200 BYTE)
)
;


CREATE UNIQUE INDEX TQ_CRM.TQC_DD_FAILED_REMARKS_PK ON TQ_CRM.TQC_DD_FAILED_REMARKS
(DFR_CODE);


ALTER TABLE TQ_CRM.TQC_DD_FAILED_REMARKS ADD (
  CONSTRAINT TQC_DD_FAILED_REMARKS_PK
  PRIMARY KEY
  (DFR_CODE)
  USING INDEX TQ_CRM.TQC_DD_FAILED_REMARKS_PK);
  -----------------------------------------------------------------------------------------------
  --spec TQC_SETUPS_CURSOR 
  TYPE tqc_dd_failed_remarks_rec IS RECORD(
    dfr_code                        tqc_dd_failed_remarks.dfr_code%TYPE,
    dfr_failed_remark       tqc_dd_failed_remarks.dfr_failed_remark%TYPE,
    dfr_appl_level              tqc_dd_failed_remarks.dfr_appl_level%TYPE
   );
   
   TYPE tqc_dd_failed_remarks_ref IS REF CURSOR
        RETURN tqc_dd_failed_remarks_rec;
		
		
     FUNCTION  GETDDAPPLICATIONRMK RETURN tqc_dd_failed_remarks_ref;
	 
----------------------------------------------------------------------------------------------
--body TQC_SETUPS_CURSOR .getDDApplicationRmk
		 FUNCTION  getDDApplicationRmk RETURN tqc_dd_failed_remarks_ref IS
        v_cursor tqc_dd_failed_remarks_ref;
   BEGIN
        OPEN v_cursor FOR
                SELECT DFR_CODE,DFR_FAILED_REMARK,DFR_APPL_LEVEL
                FROM tqc_dd_failed_remarks;
        RETURN v_cursor;
   
   EXCEPTION 
        WHEN OTHERS THEN
            RAISE_ERROR('Fetching Cursor ..'||SQLERRM(SQLCODE));
    END;