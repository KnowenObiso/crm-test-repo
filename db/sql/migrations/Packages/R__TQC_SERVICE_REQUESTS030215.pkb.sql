--
-- TQC_SERVICE_REQUESTS030215  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_SERVICE_REQUESTS030215 AS
/******************************************************************************
   NAME:       TQC_SERVICE_REQUESTS
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        22/Jun/2011             1. Created this package body.
******************************************************************************/

 PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         RAISE_APPLICATION_ERROR (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         RAISE_APPLICATION_ERROR (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

FUNCTION getServReqCategories(v_tsrc_srt_code IN NUMBER) RETURN serv_requests_cat_ref IS
    v_cursor serv_requests_cat_ref;
BEGIN
--RAISE_ERROR(v_tsrc_srt_code);
    OPEN v_cursor FOR
    SELECT TSRC_CODE, TSRC_NAME, TSRC_VALIDITY,TSRC_USR_CODE,USR_USERNAME,BRN_NAME,BRN_CODE,
     TSRC_DEFAULT,TSRC_SRT_CODE
    FROM TQC_SERV_REQ_CAT,TQC_USERS,tqc_branches
    WHERE TSRC_USR_CODE = USR_CODE(+)
    and TSRC_BRN_CODE=BRN_CODE(+)
    AND TSRC_SRT_CODE = NVL(v_tsrc_srt_code, TSRC_SRT_CODE);
   
    RETURN v_cursor;
END;

PROCEDURE categoryProc (v_addEdit   VARCHAR2,
                        v_code      NUMBER,
                        v_name      VARCHAR2,
                        v_validity  NUMBER,
                        v_usrCode   NUMBER,
                        v_tsrc_brn_code in NUMBER default null,
                        v_tsrc_srt_code VARCHAR2 default null,
                        v_tsrc_default in varchar2 default null ) IS
    v_catCode   NUMBER;
    v_cnt NUMBER;
BEGIN
--RAISE_ERROR(v_tsrc_srt_code);
    IF v_addEdit = 'A' THEN
        BEGIN
        SELECT NVL(MAX(TSRC_CODE),0) INTO v_catCode
        FROM TQC_SERV_REQ_CAT;
        EXCEPTION WHEN NO_DATA_FOUND THEN
        NULL;
        END;
        v_cnt:=0;
       -- RAISE_ERROR('v_tsrc_srt_code'||v_tsrc_srt_code);
       SELECT COUNT(*)
       INTO v_cnt
       FROM TQC_SERV_REQ_CAT
       WHERE TSRC_SRT_CODE=v_tsrc_srt_code
       AND TSRC_DEFAULT='Y';
      
    IF NVL(v_cnt,0)>=1 AND NVL(v_tsrc_default,'N')='Y'
    THEN 
    RAISE_ERROR('Failed to Update.You cannot have the two Default Values for the same Category');
    END IF;
        
        v_catCode := NVL(v_catCode,0) + 1;
       
        INSERT INTO TQC_SERV_REQ_CAT(TSRC_CODE, TSRC_NAME, TSRC_VALIDITY,TSRC_USR_CODE,TSRC_BRN_CODE,
        TSRC_SRT_CODE,tsrc_default)
        VALUES(v_catCode,v_name,v_validity,v_usrCode,v_tsrc_brn_code,
         v_tsrc_srt_code,v_tsrc_default);
        
    ELSIF v_addEdit = 'E' THEN
        UPDATE TQC_SERV_REQ_CAT
        SET TSRC_NAME = v_name, 
        TSRC_USR_CODE = v_usrCode,
        TSRC_VALIDITY = v_validity,
        TSRC_BRN_CODE=v_tsrc_brn_code,
      
        tsrc_srt_code=v_tsrc_srt_code,
        tsrc_default=v_tsrc_default
        WHERE TSRC_CODE = v_code;
    ELSIF v_addEdit = 'D' THEN
        DELETE FROM TQC_SERV_REQ_CAT
        WHERE TSRC_CODE = v_code AND TSRC_CODE != 1;
    END IF;
END categoryProc;

PROCEDURE OrdClientPropPol(v_PropPol OUT client_prop_pols_ref,
                            v_clntCode  NUMBER) IS
BEGIN
        OPEN v_PropPol FOR
            SELECT PROPOSAL_NO, POLICY_NO, TOT_SA,POL_CODE,SYS_PATH||'propPol.jspx' ,PROD_DESC,null,null
            FROM LMS_CRM_EXISTING_POLICIES_VW,TQC_SYSTEMS
            WHERE CLIENT_CODE = v_clntCode
            AND SYS_CODE = 27
            AND LIFE_CLASS = 'O';
END OrdClientPropPol;

PROCEDURE GrpClientPropPol(v_PropPol OUT client_prop_pols_ref,
                            v_clntCode  NUMBER) IS
BEGIN
        OPEN v_PropPol FOR
            SELECT PROPOSAL_NO, POLICY_NO, TOT_SA,POL_CODE,NULL,PROD_DESC,null,null FROM LMS_CRM_EXISTING_POLICIES_VW
            WHERE CLIENT_CODE = v_clntCode
            AND LIFE_CLASS = 'G';
END GrpClientPropPol;


PROCEDURE GisClientPropPol(v_PropPol OUT client_prop_pols_ref,
                            v_clntCode  NUMBER,v_type IN VARCHAR2) IS
BEGIN
IF NVL(v_type,'C')='C'
THEN
        OPEN v_PropPol FOR
           SELECT DISTINCT NULL, POL_POLICY_NO, POL_TOTAL_SUM_INSURED, POL_BATCH_NO,
                SYS_PATH || 'viewpolicy.jspx', NULL,'Policy',POL_CHECK_DATE
           FROM TQC_CLIENTS, GIN_POLICIES, TQC_SYSTEMS
          WHERE POL_PRP_CODE = CLNT_CODE AND SYS_CODE = 37
          AND CLNT_CODE = v_clntCode
         UNION
          SELECT DISTINCT NULL, TO_CHAR(QUOT_NO), NULL, QUOT_CODE,
                SYS_PATH || 'viewquotation.jspx', NULL,'Quotation',
                QUOT_AUTHORISED_DT
           FROM TQC_CLIENTS, GIN_QUOTATIONS, TQC_SYSTEMS
          WHERE QUOT_PRP_CODE = CLNT_CODE AND SYS_CODE = 37
          AND CLNT_CODE = v_clntCode
          union
          SELECT DISTINCT NULL, CMB_CLAIM_NO, NULL, NULL,
                SYS_PATH || 'viewClaims.jspx', NULL,'Claims',CMB_LOSS_DATE_TIME
           FROM TQC_CLIENTS, GIN_CLAIM_MASTER_BOOKINGS, TQC_SYSTEMS
          WHERE CMB_PRP_CODE = CLNT_CODE AND SYS_CODE = 37
          AND CLNT_CODE = v_clntCode 
          ORDER BY 8 DESC;
          ELSE
           OPEN v_PropPol FOR
          SELECT  NULL, NULL, NULL, NULL,null,
                NULL, NULL,NULl
           FROM DUAL;
END IF;
END GisClientPropPol;

PROCEDURE servicerequestsproc (
   v_addedit                 VARCHAR2,
   v_tsrcode        IN OUT   NUMBER,
   v_tsrccode                NUMBER,
   v_tsrtype                 VARCHAR2,
   v_tsracctype              VARCHAR2,
   v_tsracccode              NUMBER,
   v_tsrdate                 DATE,
   v_tsrasignee              NUMBER,
   v_tsrowntype              VARCHAR2,
   v_tsrowner                NUMBER,
   v_tsrstatus               VARCHAR2,
   v_duedate                 DATE,
   v_resoldate               DATE,
   v_tsrsummary              VARCHAR2,
   v_tsrdesc                 VARCHAR2,
   v_tsrsolution             VARCHAR2,
   v_tsrtcbcode              NUMBER DEFAULT NULL,
   v_tsrmode                 VARCHAR2 DEFAULT NULL,
   v_tsr_comments            VARCHAR2 DEFAULT NULL,
   v_tsr_srt_code NUMBER DEFAULT NULL
)
IS
   v_nextval      NUMBER;
   v_ref_number   VARCHAR2 (200);
   v_srt_code NUMBER;
   v_tsrcode_val NUMBER;
   v_tsr_assignee VARCHAR2 (200);
   v_user       VARCHAR2 (35) := pkg_global_vars.get_pvarchar2 ('PKG_GLOBAL_VARS.pvg_username');
            V_CODE NUMBER;
--   v_tsr_srt_code NUMBER;
--   v_tsr_comments VARCHAR2(200);
BEGIN
--RAISE_ERROR('v_tsrstatus'||v_tsrstatus);
   v_ref_number := generate_serv_req_ref (v_tsrtype);
 
  
   IF v_addedit = 'A'
   THEN
      BEGIN
         SELECT MAX (tsr_code)
           INTO v_nextval
           FROM tqc_serv_requests;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;

      v_nextval := NVL (v_nextval, 0) + 1;
      v_tsrcode := v_nextval;

    
      INSERT INTO tqc_serv_requests
                  (tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                   tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                   tsr_owner_code, tsr_status, tsr_due_date,
                   tsr_resolution_date, tsr_summary, tsr_description,
                   tsr_solution, tsr_tcb_code, tsr_comm_mode, tsr_ref_number,
                   tsr_comments,tsr_srt_code
                  )
           VALUES (v_tsrcode, v_tsrccode, v_tsrtype, v_tsracctype,
                   v_tsracccode, SYSDATE, v_tsrasignee, v_tsrowntype,
                   v_tsrowner, v_tsrstatus, SYSDATE,
                   SYSDATE, v_tsrsummary, v_tsrdesc,
                   v_tsrsolution, v_tsrtcbcode, v_tsrmode, v_ref_number,
                   v_tsr_comments,v_tsr_srt_code
                  );

      INSERT INTO tqc_serv_requests_hist
                  (tsr_code, tsr_tsrc_code, tsr_type,
                   tsr_acc_type, tsr_acc_code, tsr_date, tsr_assignee,
                   tsr_owner_type, tsr_owner_code, tsr_status, tsr_due_date,
                   tsr_resolution_date, tsr_summary, tsr_description,
                   tsr_solution, tsr_tcb_code, tsr_comm_mode, tsr_ref_number,
                   tsr_comments,tsr_srt_code
                  )
           VALUES (tqc_tsr_code_seq.NEXTVAL, v_tsrccode, v_tsrtype,
                   v_tsracctype, v_tsracccode, SYSDATE, v_tsrasignee,
                   v_tsrowntype, v_tsrowner, v_tsrstatus, SYSDATE,
                   SYSDATE, v_tsrsummary, v_tsrdesc,
                   v_tsrsolution, v_tsrtcbcode, v_tsrmode, v_ref_number,
                   v_tsr_comments,v_tsr_srt_code
                  );
   ELSIF v_addedit = 'E'
   THEN
        BEGIN
           SELECT tsr_srt_code,tsr_assignee
             INTO v_srt_code,v_tsr_assignee
             FROM tqc_serv_requests
            WHERE tsr_code = v_tsrcode;
        EXCEPTION
           WHEN OTHERS
           THEN
              raise_error ('Failed to obtain the request category');
        END;
    IF v_srt_code!=v_tsr_srt_code
    THEN
    BEGIN
         SELECT MAX (tsr_code)
           INTO v_nextval
           FROM tqc_serv_requests;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
      END;


     IF NVL(v_tsracctype,'C')='IN'
     THEN
       v_code:=2;
       ELSE
       v_code:=v_tsrccode;
     END IF;
     
      v_nextval := NVL (v_nextval, 0) + 1;
      v_tsrcode_val := v_nextval;
--RAISE_ERROR('TEST'||v_tsrcode);
      UPDATE tqc_serv_requests
         SET --tsr_tsrc_code = v_tsrccode,
             tsr_type = v_tsrtype,
             tsr_acc_type = v_tsracctype,
             tsr_acc_code = v_tsracccode,
             tsr_date = v_tsrdate,
             tsr_assignee = v_tsrasignee,
             tsr_owner_type = v_tsrowntype,
             tsr_owner_code = v_tsrowner,
             tsr_status = 'Closed',
             tsr_due_date = v_duedate,
             tsr_resolution_date = v_resoldate,
             tsr_summary = v_tsrsummary,
             tsr_description = v_tsrdesc,
             tsr_solution = v_tsrsolution,
             tsr_comm_mode = v_tsrmode,
             tsr_ref_number = v_ref_number,
             tsr_comments = v_tsr_comments
          --   tsr_srt_code=v_tsr_srt_code
       WHERE tsr_code = v_tsrcode;
     --  RAISE_ERROR('v_tsracctype'||v_tsracctype);
       INSERT INTO tqc_serv_requests
                  (tsr_code, tsr_tsrc_code, tsr_type, tsr_acc_type,
                   tsr_acc_code, tsr_date, tsr_assignee, tsr_owner_type,
                   tsr_owner_code, tsr_status, tsr_due_date,
                   tsr_resolution_date, tsr_summary, tsr_description,
                   tsr_solution, tsr_tcb_code, tsr_comm_mode, tsr_ref_number,
                   tsr_comments,tsr_srt_code
                  )
           VALUES (v_tsrcode_val,decode(v_tsracctype,'IN',2,v_tsrccode) , 'CONVERSION', v_tsracctype,
                   v_tsracccode, SYSDATE, v_tsrasignee, v_tsrowntype,
                   v_tsrowner, 'Open', SYSDATE,
                   SYSDATE, v_tsrsummary, v_tsrdesc,
                   v_tsrsolution, v_tsrtcbcode, v_tsrmode, v_ref_number,
                   v_tsr_comments,v_tsr_srt_code
                  );
       
  
     ELSE
   --  RAISE_ERROR('v_tsrstatus'||v_tsrstatus||'v_tsrcode'||v_tsrcode);
      UPDATE tqc_serv_requests
         SET tsr_tsrc_code =v_tsrccode ,
             tsr_type = v_tsrtype,
             tsr_acc_type = v_tsracctype,
             tsr_acc_code = v_tsracccode,
             tsr_date = v_tsrdate,
             tsr_assignee = v_tsrasignee,
             tsr_owner_type = v_tsrowntype,
             tsr_owner_code = v_tsrowner,
             tsr_status = v_tsrstatus,
             tsr_due_date = v_duedate,
             tsr_resolution_date = v_resoldate,
             tsr_summary = v_tsrsummary,
             tsr_description = v_tsrdesc,
             tsr_solution = v_tsrsolution,
             tsr_comm_mode = v_tsrmode,
             tsr_ref_number = v_ref_number,
             tsr_comments = v_tsr_comments,
             tsr_srt_code=v_tsr_srt_code
       WHERE tsr_code = v_tsrcode;
    END IF;

      INSERT INTO tqc_serv_requests_hist
                  (tsr_code, tsr_tsrc_code, tsr_type,
                   tsr_acc_type, tsr_acc_code, tsr_date, tsr_assignee,
                   tsr_owner_type, tsr_owner_code, tsr_status, tsr_due_date,
                   tsr_resolution_date, tsr_summary, tsr_description,
                   tsr_solution, tsr_tcb_code, tsr_comm_mode, tsr_ref_number,
                   tsr_comments,tsr_srt_code
                  )
           VALUES (tqc_tsr_code_seq.NEXTVAL, v_tsrccode, v_tsrtype,
                   v_tsracctype, v_tsracccode, SYSDATE, v_tsrasignee,
                   v_tsrowntype, v_tsrowner, v_tsrstatus, SYSDATE,
                   SYSDATE, v_tsrsummary, v_tsrdesc,
                   v_tsrsolution, v_tsrtcbcode, v_tsrmode, v_ref_number,
                   v_tsr_comments,v_tsr_srt_code
                  );
--     IF v_tsr_assignee=v_tsrasignee THEN
--         gin_manage_alerts.service_req_alert (v_user,v_tsrcode);
--     END IF;
                     
   END IF;
--EXCEPTION
--   WHEN OTHERS
--   THEN
--   raise_error ('Error Occured ' || SQLERRM);
END;
FUNCTION getClientOpenRequests(v_clientCode     NUMBER,v_tsr_acc_type in varchar2) RETURN serv_requests_ref IS
    v_cursor    serv_requests_ref;
BEGIN
--RAISE_ERROR('v_clientCode'||v_clientCode||'v_tsr_acc_type'||v_tsr_acc_type);
    OPEN v_cursor FOR
  SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,CLNT_NAME||' '|| CLNT_OTHER_NAMES ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    CLNT_SHT_DESC, CLNT_PHYSICAL_ADDRS, CLNT_EMAIL_ADDRS, CLNT_SMS_TEL,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_CLIENTS,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = CLNT_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'C'
    AND TSR_ACC_CODE = v_clientCode
    AND TSR_STATUS = 'Open'
    AND TSR_TYPE != 'Enquiry'
    and SRT_CODE(+)=TSR_SRT_CODE
    AND tsr_acc_type=v_tsr_acc_type
    UNION
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,AGN_NAME ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    AGN_SHT_DESC, AGN_PHYSICAL_ADDRESS, AGN_EMAIL_ADDRESS, AGN_SMS_TEL,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_AGENCIES,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = AGN_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'A'
    AND TSR_ACC_CODE = v_clientCode
    AND TSR_STATUS = 'Open'
    AND TSR_TYPE != 'Enquiry'
    and SRT_CODE(+)=TSR_SRT_CODE
    AND tsr_acc_type=v_tsr_acc_type
    UNION
     SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,SPR_NAME ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    SPR_SHT_DESC, SPR_PHYSICAL_ADDRESS, SPR_EMAIL, SPR_SMS_NUMBER,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_SERVICE_PROVIDERS,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = SPR_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'SP'
    AND TSR_ACC_CODE = v_clientCode
    AND TSR_STATUS = 'Open'
    AND TSR_TYPE != 'Enquiry'
    AND tsr_acc_type=v_tsr_acc_type
    and SRT_CODE(+)=TSR_SRT_CODE
     UNION
     SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,SRID_NAME ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    SRID_NAME, SRID_PHYSICAL_ADDRESS, SRID_EMAIL_ADDRESS, SRID_TELEPHONE,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,tqc_serv_req_ind_dtls,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = SRID_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'O'
    AND TSR_ACC_CODE = v_clientCode
    AND TSR_STATUS = 'Open'
    AND TSR_TYPE != 'Enquiry'
    AND tsr_acc_type=v_tsr_acc_type
    and SRT_CODE(+)=TSR_SRT_CODE
     UNION
      SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,DLT_DESC  ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    ORG_SHT_DESC,  ORG_PHY_ADDRS,ORG_EMAIL_ADDRS,ORG_TEL1,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,tqc_org_division_levels_type,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES,TQC_ORGANIZATIONS
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = DLT_CODE_VAL
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'IN'
   AND TSR_ACC_CODE = v_clientCode
    AND TSR_STATUS = 'Open'
    AND ORG_CODE=TSR_TSRC_CODE
    AND TSR_TYPE != 'Enquiry'
   AND tsr_acc_type=v_tsr_acc_type
    and SRT_CODE(+)=TSR_SRT_CODE;
    RETURN v_cursor;
END;



FUNCTION getClientAllOpenRequests(v_clientCode     NUMBER) RETURN serv_requests_ref IS
    v_cursor    serv_requests_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,CLNT_NAME||' '|| CLNT_OTHER_NAMES ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    CLNT_SHT_DESC, CLNT_PHYSICAL_ADDRS, CLNT_EMAIL_ADDRS, CLNT_SMS_TEL,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_CLIENTS,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = CLNT_CODE(+)
    AND TSR_OWNER_CODE = UOWN.USR_CODE(+)
    AND TSR_ACC_TYPE = 'C'
    and SRT_CODE(+)=TSR_SRT_CODE
    AND NVL(TSR_ACC_CODE,v_clientCode) = v_clientCode
    AND TSR_STATUS = 'Open';
    RETURN v_cursor;
END;


FUNCTION getAllClientRequests(v_clientCode     NUMBER,V_TSR_ACC_TYPE IN VARCHAR2) RETURN serv_requests_ref IS
    v_cursor    serv_requests_ref;
BEGIN
--RAISE_ERROR('v_clientCode'||v_clientCode);
    OPEN v_cursor FOR
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,CLNT_NAME||' '|| CLNT_OTHER_NAMES ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER ,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    CLNT_SHT_DESC, CLNT_PHYSICAL_ADDRS, CLNT_EMAIL_ADDRS, CLNT_SMS_TEL,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_CLIENTS,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = CLNT_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'C'
    and TSR_STATUS='Closed'
    and SRT_CODE(+)=TSR_SRT_CODE
    AND TSR_ACC_CODE = v_clientCode
    AND TSR_ACC_TYPE=V_TSR_ACC_TYPE
    union
     SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,AGN_NAME ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER ,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    AGN_SHT_DESC, AGN_PHYSICAL_ADDRESS, AGN_EMAIL_ADDRESS, AGN_SMS_TEL,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,tqc_agencies,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = AGN_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'A'
    and TSR_STATUS='Closed'
    and SRT_CODE(+)=TSR_SRT_CODE
    AND TSR_ACC_CODE = v_clientCode
    AND TSR_ACC_TYPE=V_TSR_ACC_TYPE
    union
     SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,SPR_NAME ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER ,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    SPR_SHT_DESC, SPR_PHYSICAL_ADDRESS, SPR_EMAIL, SPR_MOBILE_NO,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,tqc_service_providers,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = SPR_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'SP'
    and TSR_STATUS='Closed'
    and SRT_CODE(+)=TSR_SRT_CODE
    AND TSR_ACC_CODE = v_clientCode
    AND TSR_ACC_TYPE=V_TSR_ACC_TYPE
    UNION
     SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,SRID_NAME ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER ,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    NULL SPR_SHT_DESC, SRID_PHYSICAL_ADDRESS, SRID_EMAIL_ADDRESS, SRID_TELEPHONE,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_SERV_REQ_IND_DTLS,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
   AND TSR_ACC_CODE = SRID_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'O'
    and TSR_STATUS='Closed'
    and SRT_CODE(+)=TSR_SRT_CODE
    AND TSR_ACC_CODE = v_clientCode
    AND TSR_ACC_TYPE=V_TSR_ACC_TYPE
    UNION
     SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,DLT_DESC  ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    ORG_SHT_DESC,  ORG_PHY_ADDRS,ORG_EMAIL_ADDRS,ORG_TEL1,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,tqc_org_division_levels_type,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES,TQC_ORGANIZATIONS
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = DLT_CODE_VAL
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'IN'
   AND TSR_ACC_CODE = v_clientCode
    AND TSR_STATUS = 'Closed'
    AND ORG_CODE=TSR_TSRC_CODE
    AND TSR_TYPE != 'Enquiry'
   AND tsr_acc_type=v_tsr_acc_type
    and SRT_CODE(+)=TSR_SRT_CODE;
    RETURN v_cursor;
END;

FUNCTION getUserOpenRequests(v_userCode     NUMBER) RETURN serv_requests_ref IS
    v_cursor    serv_requests_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,CLNT_NAME||' '|| CLNT_OTHER_NAMES ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    CLNT_SHT_DESC, CLNT_PHYSICAL_ADDRS, CLNT_EMAIL_ADDRS, CLNT_SMS_TEL,null,null
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_CLIENTS,TQC_USERS UOWN
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = CLNT_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND ASS.USR_CODE  = v_userCode    
    AND TSR_STATUS = 'Open'
    AND TSR_TYPE != 'Enquiry';
    RETURN v_cursor;
END;

FUNCTION getUserOverdueRequests(v_userCode     NUMBER) RETURN serv_requests_ref IS
    v_cursor    serv_requests_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,CLNT_NAME||' '|| CLNT_OTHER_NAMES ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    CLNT_SHT_DESC, CLNT_PHYSICAL_ADDRS, CLNT_EMAIL_ADDRS, CLNT_SMS_TEL,NULL,NULL
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_CLIENTS,TQC_USERS UOWN
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = CLNT_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND ASS.USR_CODE  = v_userCode    
    AND TSR_STATUS = 'Open'
    AND SYSDATE > TSR_DUE_DATE
    AND TSR_TYPE != 'Enquiry';
    RETURN v_cursor;
END;

FUNCTION getOverdueRequests(v_tsr_status in varchar2) RETURN serv_requests_ref IS
    v_cursor    serv_requests_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,CLNT_NAME||' '|| CLNT_OTHER_NAMES ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    CLNT_SHT_DESC,CLNT_PHYSICAL_ADDRS,CLNT_EMAIL_ADDRS,CLNT_SMS_TEL,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_CLIENTS,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = CLNT_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_STATUS = v_tsr_status
    and SRT_CODE(+)=TSR_SRT_CODE
    AND SYSDATE > TSR_DUE_DATE
    AND TSR_TYPE != 'Enquiry'
    and TSR_ACC_TYPE='C'
    union
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,AGN_NAME ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    AGN_SHT_DESC, AGN_PHYSICAL_ADDRESS, AGN_EMAIL_ADDRESS, AGN_SMS_TEL,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_AGENCIES,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = AGN_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_STATUS = v_tsr_status
    AND SYSDATE > TSR_DUE_DATE
    and SRT_CODE(+)=TSR_SRT_CODE
    AND TSR_TYPE != 'Enquiry'
    and TSR_ACC_TYPE='A'
    union
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,SPR_NAME ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    SPR_SHT_DESC, SPR_PHYSICAL_ADDRESS, SPR_EMAIL, SPR_MOBILE_NO,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_SERVICE_PROVIDERS,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = SPR_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_STATUS = v_tsr_status
    AND SYSDATE > TSR_DUE_DATE
    and SRT_CODE(+)=TSR_SRT_CODE
    and TSR_ACC_TYPE='SP'
    AND TSR_TYPE != 'Enquiry'
    UNION
     SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,SRID_NAME ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    SRID_NAME, SRID_PHYSICAL_ADDRESS, SRID_EMAIL_ADDRESS, SRID_TELEPHONE,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,tqc_serv_req_ind_dtls,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = SRID_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'O'
    --AND TSR_ACC_CODE = v_clientCode
    AND TSR_STATUS = 'Open'
    AND TSR_TYPE != 'Enquiry'
   -- AND tsr_acc_type=v_tsr_acc_type
    and SRT_CODE(+)=TSR_SRT_CODE
    UNION
     SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,DLT_DESC  ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    ORG_SHT_DESC,  ORG_PHY_ADDRS,ORG_EMAIL_ADDRS,ORG_TEL1,TSR_SRT_CODE,SRT_DESC
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,tqc_org_division_levels_type,TQC_USERS UOWN,
    TQC_SERV_REQ_TYPES,TQC_ORGANIZATIONS
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = DLT_CODE_VAL
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_ACC_TYPE = 'IN'
  -- AND TSR_ACC_CODE = v_clientCode
    AND TSR_STATUS = 'Open'
    AND ORG_CODE=TSR_TSRC_CODE
    AND TSR_TYPE != 'Enquiry'
  -- AND tsr_acc_type=v_tsr_acc_type
    and SRT_CODE(+)=TSR_SRT_CODE;
    RETURN v_cursor;
END;

FUNCTION getAllPendingRequests RETURN serv_requests_ref IS
    v_cursor    serv_requests_ref;
BEGIN
--RAISE_ERROR('TEST');
    OPEN v_cursor FOR
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','IN','INTERNAL','U','USER','O','OTHERS')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,CLNT_NAME||' '|| CLNT_OTHER_NAMES ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','IN','INTERNAL','U','USER','OTHERS')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    CLNT_SHT_DESC, CLNT_PHYSICAL_ADDRS, CLNT_EMAIL_ADDRS, CLNT_SMS_TEL,NULL,NULL
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_CLIENTS,TQC_USERS UOWN
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = CLNT_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_STATUS = 'Open'
    AND TSR_TYPE != 'Enquiry';
    RETURN v_cursor;
END;

FUNCTION getOverdueRequestsChat RETURN overdueCount_ref IS
    v_cursor    overdueCount_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT COUNT(*) USER_ISSUES,ASS.USR_USERNAME ASSIGNEE
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_CLIENTS,TQC_USERS UOWN
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = CLNT_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_STATUS = 'Open'
    AND SYSDATE > TSR_DUE_DATE
    AND TSR_TYPE != 'Enquiry'
    GROUP BY ASS.USR_USERNAME;
    RETURN v_cursor;
END;

FUNCTION getRootTopics RETURN kb_topics_ref IS
    v_cursor kb_topics_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TKBT_ID, TKBT_ORDER, TKBT_SHT_DESC, TKBT_DESC, TKBT_PARENT_ID
    FROM TQC_KB_TOPICS
    WHERE TKBT_PARENT_ID IS NULL;
    RETURN v_cursor;
END;

FUNCTION getChildTopics(v_tkbt_id   NUMBER) RETURN kb_topics_ref IS
    v_cursor kb_topics_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TKBT_ID, TKBT_ORDER, TKBT_SHT_DESC, TKBT_DESC, TKBT_PARENT_ID
    FROM TQC_KB_TOPICS
    WHERE TKBT_PARENT_ID  = v_tkbt_id;
    RETURN v_cursor;
END;

PROCEDURE kbTopicProc(v_addEdit         VARCHAR2,
                      v_tkbtId          NUMBER,
                      v_tkbtorder       NUMBER,
                      v_tkbtShtDesc     VARCHAR2,
                      v_tkbtDesc        VARCHAR2,
                      v_tkbtParent      NUMBER) IS
    v_code  NUMBER;
BEGIN
    IF v_addEdit = 'A' THEN
        BEGIN
        SELECT MAX(TKBT_ID) INTO v_code  FROM TQC_KB_TOPICS;
        EXCEPTION WHEN NO_DATA_FOUND THEN 
            NULL;
        END;
        v_code := NVL(v_code,0) + 1;
        INSERT INTO TQC_KB_TOPICS (TKBT_ID, TKBT_ORDER, TKBT_SHT_DESC, TKBT_DESC, TKBT_PARENT_ID)
        VALUES(v_code,v_tkbtorder,v_tkbtShtDesc,v_tkbtDesc,v_tkbtParent);
    ELSIF v_addEdit = 'E' THEN
        UPDATE TQC_KB_TOPICS SET 
        TKBT_ORDER = v_tkbtorder, 
        TKBT_SHT_DESC = v_tkbtShtDesc, 
        TKBT_DESC = v_tkbtDesc, 
        TKBT_PARENT_ID = v_tkbtParent
        WHERE TKBT_ID = v_tkbtId ;
    ELSIF v_addEdit = 'D' THEN
    DELETE FROM TQC_KB_TOPICS WHERE TKBT_ID = v_tkbtId ;
    END IF;
EXCEPTION WHEN OTHERS THEN
    RAISE_ERROR('Error Occured '||SQLERRM);
END kbTopicProc;


FUNCTION getKbContent(v_tkbt_id   NUMBER) RETURN kb_content_ref IS
    v_cursor kb_content_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TKBC_ID, TKBC_TKBT_ID, TKBC_ORDER, TKBC_CONTENT
    FROM TQC_KB_CONTENT
    WHERE TKBC_TKBT_ID  = v_tkbt_id;
    RETURN v_cursor;
END;

PROCEDURE kbContentProc(v_addEdit   VARCHAR2,
                        v_tkbcId    NUMBER,
                        v_tkbtId    NUMBER,
                        v_tkbcOrder NUMBER,
                        v_tkbcCont  VARCHAR2) IS
    v_code  NUMBER;
BEGIN
    IF v_addEdit = 'A' THEN
        BEGIN
        SELECT MAX(TKBC_ID) INTO v_code  FROM TQC_KB_CONTENT;
        EXCEPTION WHEN NO_DATA_FOUND THEN 
            NULL;
        END;
        v_code := NVL(v_code,0) + 1;
        INSERT INTO TQC_KB_CONTENT (TKBC_ID, TKBC_TKBT_ID, TKBC_ORDER, TKBC_CONTENT)
        VALUES(v_code,v_tkbtId,v_tkbcOrder,v_tkbcCont);
    ELSIF v_addEdit = 'E' THEN
        UPDATE TQC_KB_CONTENT SET 
        TKBC_TKBT_ID = v_tkbtId, 
        TKBC_ORDER = v_tkbcOrder, 
        TKBC_CONTENT = v_tkbcCont
        WHERE TKBC_ID = v_tkbcId;
    ELSIF v_addEdit = 'D' THEN
    DELETE FROM TQC_KB_CONTENT WHERE TKBC_ID = v_tkbcId;
    END IF;
EXCEPTION WHEN OTHERS THEN
    RAISE_ERROR('Error Occured '||SQLERRM);
END kbContentProc;
FUNCTION getCategoryBranches RETURN cat_Branches_ref IS
    v_cursor cat_Branches_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT BRN_CODE, BRN_SHT_DESC, BRN_REG_CODE, BRN_NAME
    FROM tqc_branches,tqc_regions
    WHERE BRN_REG_CODE  = REG_CODE
    and REG_ORG_CODE=1;
    RETURN v_cursor;
END;
function GENERATE_SERV_REQ_REF (v_tsrType IN VARCHAR) RETURN VARCHAR2
IS 
v_ref_number varchar2(200);
BEGIN
    v_ref_number:='SRQ/'||TQC_SERV_REQ_REF_SEQ.NEXTVAL||'/'||v_tsrType;
    RETURN v_ref_number;
 END;
 FUNCTION get_departments RETURN departments_ref IS
    v_cursor departments_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT DEP_CODE, DEP_SHT_DESC, DEP_NAME, DEP_WEF, DEP_WET
    FROM TQC_DEPARTMENTS;
   
    RETURN v_cursor;
END;
 FUNCTION get_serv_request_types RETURN serv_request_types_ref IS
    v_cursor serv_request_types_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT SRT_CODE, SRT_SHT_DESC, SRT_DESC
    FROM TQC_SERV_REQ_TYPES;
    RETURN v_cursor;
END;

PROCEDURE update_serv_request_types (
   v_addedit             VARCHAR2,
   v_srt_code       IN   NUMBER,
   v_srt_desc   IN   VARCHAR2,
   v_srt_sht_desc      IN   VARCHAR2
)
IS
BEGIN
   IF v_addedit = 'A'
   THEN
      INSERT INTO tqc_serv_req_types
                  (SRT_CODE, srt_sht_desc, srt_desc
                  )
           VALUES (TQC_SRT_CODE_SEQ.NEXTVAL, v_srt_sht_desc, v_srt_desc
                  );
   ELSIF v_addedit = 'E'
   THEN
      UPDATE tqc_serv_req_types
         SET srt_sht_desc = v_srt_sht_desc,
             srt_desc = v_srt_desc
       WHERE srt_code = v_srt_code;
   ELSIF v_addedit = 'D'
   THEN
      DELETE FROM tqc_serv_req_types
            WHERE srt_code = v_srt_code;
   END IF;
EXCEPTION
   WHEN OTHERS
   THEN
      raise_error ('Error Occured ' || SQLERRM);
END update_serv_request_types;
 FUNCTION get_serv_providers RETURN serv_providers_ref IS
    v_cursor serv_providers_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT SPR_CODE, SPR_SHT_DESC, SPR_NAME, SPR_PHYSICAL_ADDRESS, 
           SPR_POSTAL_ADDRESS, SPR_PHONE, 
           SPR_EMAIL, SPR_MOBILE_NO, SPR_SMS_NUMBER
    FROM tqc_service_providers;
    RETURN v_cursor;
END;
FUNCTION getAllPendingRequests(v_tsr_assignee in varchar2) RETURN serv_requests_ref IS
    v_cursor    serv_requests_ref;
BEGIN
    OPEN v_cursor FOR
    SELECT TSR_CODE, TSR_TSRC_CODE, TSR_TYPE, TSR_ACC_TYPE, TSR_ACC_CODE, TSR_DATE, 
    TSR_ASSIGNEE, TSR_OWNER_TYPE, TSR_OWNER_CODE, TSR_STATUS, TSR_DUE_DATE, 
    TSR_RESOLUTION_DATE, TSR_SUMMARY, TSR_DESCRIPTION, TSR_SOLUTION,TSRC_NAME,
    DECODE(TSR_ACC_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','IN','INTERNAL','U','USER','O','OTHERS')  ACC_TYPE,
    ASS.USR_USERNAME ASSIGNEE,CLNT_NAME||' '|| CLNT_OTHER_NAMES ACCOUNT_NAME,
    DECODE(TSR_OWNER_TYPE,'C','CLIENT','A','AGENT','SP','SERVICE PROVIDER','U','USER')  OWNER_TYPE,
    UOWN.USR_USERNAME OWNER,TSR_COMM_MODE,TSR_REF_NUMBER,TSR_COMMENTS,
    CLNT_SHT_DESC, CLNT_PHYSICAL_ADDRS, CLNT_EMAIL_ADDRS, CLNT_SMS_TEL,NULL,NULL
    FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_CAT,TQC_USERS ASS,TQC_CLIENTS,TQC_USERS UOWN
    WHERE TSR_TSRC_CODE = TSRC_CODE
    AND TSR_ASSIGNEE = ASS.USR_CODE
    AND TSR_ACC_CODE = CLNT_CODE
    AND TSR_OWNER_CODE = UOWN.USR_CODE
    AND TSR_STATUS = 'Open'
    AND TSR_TYPE != 'Enquiry'
    and ASS.USR_USERNAME=v_tsr_assignee;
    RETURN v_cursor;
END;
PROCEDURE update_serv_req_other_dtls (
   v_addedit                      VARCHAR2,
   v_srid_code               IN OUT   NUMBER,
   v_srid_name               IN   VARCHAR2,
   v_srid_telephone          IN   VARCHAR2,
   v_srid_email_address      IN   VARCHAR2,
   v_srid_physical_address   IN   VARCHAR2,
   v_srid_id_number          IN   VARCHAR2
)
IS
BEGIN
   IF v_addedit = 'A'
   THEN
   v_srid_code:=tqc_srid_code_seq.NEXTVAL;
      INSERT INTO tqc_serv_req_ind_dtls
                  (srid_code, srid_name, srid_telephone,
                   srid_email_address, srid_physical_address,
                   srid_id_number
                  )
           VALUES (v_srid_code, v_srid_name, v_srid_telephone,
                   v_srid_email_address, v_srid_physical_address,
                   v_srid_id_number
                  );
   ELSIF v_addedit = 'E'
   THEN
      UPDATE tqc_serv_req_ind_dtls
         SET srid_name = v_srid_name,
             srid_telephone = v_srid_telephone,
             srid_email_address = v_srid_email_address,
             srid_physical_address = v_srid_physical_address,
             srid_id_number = v_srid_id_number
       WHERE srid_code = v_srid_code;
   ELSIF v_addedit = 'D'
   THEN
   NULL;
      DELETE FROM tqc_serv_req_ind_dtls
            WHERE srid_code = v_srid_code;
   END IF;
EXCEPTION
   WHEN OTHERS
   THEN
      raise_error ('Error Occured ' || SQLERRM);
END update_serv_req_other_dtls;
FUNCTION get_other_service_requesters (
   v_srid_name        IN   VARCHAR2,
   v_srid_id_number   IN   VARCHAR2
)
   RETURN service_requesters_ref
IS
   v_cursor   service_requesters_ref;
BEGIN
   OPEN v_cursor FOR
      SELECT srid_code, srid_name, srid_telephone, srid_email_address,
             srid_physical_address, srid_id_number
        FROM tqc_serv_req_ind_dtls
        where UPPER (srid_name) LIKE '%'||UPPER (v_srid_name)||'%'
        and UPPER (SRID_ID_NUMBER) LIKE '%'||UPPER (v_SRID_ID_NUMBER)||'%';
        

   RETURN v_cursor;
END;
FUNCTION get_Agent_commission(v_agn_code in number)
   RETURN Agent_commission_ref
IS
   v_cursor   Agent_commission_ref;
BEGIN
--RAISE_ERROR('v_agn_code'||v_agn_code);
   OPEN v_cursor FOR
SELECT cop_agn_code, agn_name, cop_date, cop_cr_ref_no, cop_dr_ref_no,
       cop_comm_amt, cop_whdtax_amt, cop_net_comm, cop_cur_code,
       cop_authorized, cop_authorized_by, cop_paid, cop_paid_chq_date,
       cop_paid_chq_no
  FROM gin_commission_pymts, tqc_agencies
 WHERE cop_agn_code = agn_code
 and cop_agn_code=v_agn_code;

   RETURN v_cursor;
END;
END TQC_SERVICE_REQUESTS030215; 
/