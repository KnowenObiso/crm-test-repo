--
-- TQC_COMMON_CURSORS  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_COMMON_CURSORS AS
  /******************************************************************************
     NAME:       TQC_COMMON_CURSORS
     PURPOSE:
  
     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        3/22/2010             1. Created this package body.
  ******************************************************************************/

  PROCEDURE getcountries(v_refcur OUT countries_ref) IS
  
  BEGIN
    OPEN v_refcur FOR
      SELECT COU_CODE,
             COU_SHT_DESC,
             COU_NAME,
             COU_BASE_CURR,
             COU_NATIONALITY,
             COU_ZIP_CODE
        FROM TQC_COUNTRIES;
  END;

  PROCEDURE getTowns(v_cou_code IN NUMBER, v_refcur OUT towns_ref) IS
  
  BEGIN
    OPEN v_refcur FOR
      select TWN_CODE, TWN_COU_CODE, TWN_SHT_DESC, TWN_NAME
        from TQC_TOWNS
       where TWN_COU_CODE = v_cou_code
       order by TWN_NAME;
  END;
  PROCEDURE getAllTowns(v_refcur OUT towns_ref) IS
  
  BEGIN
    OPEN v_refcur FOR
      select TWN_CODE, COU_CODE, COU_NAME, TWN_NAME
        from TQC_TOWNS, TQC_COUNTRIES
       WHERE twn_cou_code = cou_code
       order by TWN_NAME;
  END;
  PROCEDURE getorgbranches(v_org_code IN NUMBER, v_refcur OUT branches_ref) IS
  
  BEGIN
    OPEN v_refcur FOR
      SELECT BRN_CODE,
             BRN_SHT_DESC,
             BRN_REG_CODE,
             BRN_NAME,
             BRN_PHY_ADDRS,
             BRN_EMAIL_ADDRS,
             BRN_POST_ADDRS,
             BRN_TWN_CODE,
             BRN_COU_CODE,
             BRN_CONTACT,
             BRN_MANAGER,
             BRN_TEL,
             BRN_FAX,
             BRN_GEN_POL_CLM,
             BRN_BNS_CODE,
             BRN_AGN_CODE,
             BRN_POST_LEVEL,
             BRN_MNGR_ALLOWED,
              BRN_OVERIDE_COMM_EARNED,
              BRN_BRA_MNGR_SEQ_NO,
              BRN_AGN_SEQ_NO,
              BRN_POL_PREFIX,BRN_POL_SEQ, BRN_PROP_SEQ
        FROM TQC_BRANCHES, TQC_REGIONS, TQC_ORGANIZATIONS
       WHERE ORG_CODE = REG_ORG_CODE
         AND REG_CODE = BRN_REG_CODE
         AND ORG_CODE = NVL(v_org_code, ORG_CODE)
       ORDER BY BRN_NAME;
  END;
  PROCEDURE get_sectors(v_refcur OUT tqc_sectors_ref) IS
  
  BEGIN
    OPEN v_refcur FOR
      SELECT SEC_CODE, SEC_SHT_DESC, SEC_NAME FROM TQC_SECTORS;
  END;
  PROCEDURE get_current_date(v_date  OUT DATE,
                             v_day   OUT VARCHAR2,
                             v_month OUT VARCHAR2,
                             v_year  OUT VARCHAR2) IS
  
  BEGIN
    SELECT TO_DATE(TO_CHAR(sysdate, 'DD.MM.YYYY HH24:MI:SS'),
                   'DD.MM.YYYY HH24:MI:SS') CurrentDate,
           TO_CHAR(sysdate, 'DD') CurrentDay,
           TO_CHAR(sysdate, 'MM') CurrentMonth,
           TO_CHAR(sysdate, 'RRRR') CurrentYear
      INTO v_date, v_day, v_month, v_year
      FROM DUAL;
  END;
  PROCEDURE get_cal_activities(v_refcur   OUT cal_activites_ref,
                               v_username VARCHAR2) IS
  BEGIN
    OPEN v_refcur FOR
    /*SELECT cala_location, cala_title, 
                            CASE WHEN (CALA_CREATE_DATE + cala_startadddays) > TO_DATE(SYSDATE, 'DD/MM/RRRR')
                            THEN  (TO_DATE(SYSDATE, 'DD/MM/RRRR') - (CALA_CREATE_DATE + cala_startadddays)) ELSE
                            ((CALA_CREATE_DATE + cala_startadddays) - TO_DATE(SYSDATE, 'DD/MM/RRRR')) END As STARTADDDAYS,
                            cala_starthour, cala_startmin,   
                            CASE WHEN (CALA_CREATE_DATE + cala_endadddays) > TO_DATE(SYSDATE, 'DD/MM/RRRR')
                            THEN  (TO_DATE(SYSDATE, 'DD/MM/RRRR') - (CALA_CREATE_DATE + cala_endadddays)) ELSE
                            ((CALA_CREATE_DATE + cala_endadddays) - TO_DATE(SYSDATE, 'DD/MM/RRRR')) END As cala_endadddays,
                            cala_endaddhours, cala_endaddmin
                       FROM tqc_calendar_activities
                       WHERE cala_user = v_username;*/
      SELECT cala_location,
             cala_title,
             ((CALA_CREATE_DATE + cala_startadddays) -
             TO_DATE(SYSDATE, 'DD/MM/RRRR')) As STARTADDDAYS,
             cala_starthour,
             cala_startmin,
             ((CALA_CREATE_DATE + cala_endadddays) -
             TO_DATE(SYSDATE, 'DD/MM/RRRR')) As cala_endadddays,
             cala_endaddhours,
             cala_endaddmin
        FROM tqc_calendar_activities
       WHERE cala_user = v_username;
  END;
  PROCEDURE get_insurers(v_refcur OUT insurers_ref) IS
  
  BEGIN
    OPEN v_refcur FOR
      SELECT AGN_CODE, AGN_NAME FROM TQC_AGENCIES WHERE AGN_ACT_CODE = 5;
  END;

  PROCEDURE get_treaty_currencies(v_cursor OUT currency_cursor)
 
  IS
 
 BEGIN
 
   OPEN v_cursor FOR
     SELECT CUR_CODE, CUR_SYMBOL, CUR_DESC,get_exch_rate (cur_code) CUR_RATE
       FROM tqc_currencies
      ORDER BY CUR_SYMBOL;
 
 END; 


  PROCEDURE get_treaty_currency_values(v_code   IN NUMBER,
                                       v_cursor OUT currency_cursor)
  
   IS
  
  BEGIN
  
    OPEN v_cursor FOR
      SELECT CUR_CODE, CUR_SYMBOL, CUR_DESC,get_exch_rate (cur_code) CUR_RATE
        FROM tqc_currencies
       WHERE CUR_CODE = v_code
       ORDER BY CUR_SYMBOL;
  
  END; 


  PROCEDURE get_tqc_doc_required(v_code   IN NUMBER,
                                 v_cursor OUT tqc_doc_required_cursor)
  
   IS
  
  BEGIN
  
    OPEN v_cursor FOR
      SELECT TQDOCR_CODE,
             TQDOCR_DOCR_CODE,
             TQDOCR_CLNT_CODE,
             TQDOCR_SUBMITED,
             TQDOCR_DATE_S,
             TQDOCR_REF_NO,
             TQDOCR_RMRK,
             TQDOCR_USER_RECEIVD
        FROM TQC_DOC_REQRD_SUBMTD
       WHERE TQDOCR_CLNT_CODE = v_code;
  
  END;

  PROCEDURE get_default_currency_val(v_cursor OUT currency_cursor) IS
  BEGIN
  
    OPEN v_cursor FOR
      select CUR_CODE, CUR_SYMBOL, CUR_DESC,get_exch_rate (cur_code) CUR_RATE
        from tqc_organizations, tqc_currencies
       where org_cur_code = cur_code
         and org_code = 2;
  
  END;


  PROCEDURE get_default_branches(v_username IN VARCHAR2,
                                 v_refcur   OUT branches_ref) IS
  
  BEGIN
    OPEN v_refcur FOR
      SELECT BRN_CODE,
             BRN_SHT_DESC,
             BRN_REG_CODE,
             BRN_NAME,
             BRN_PHY_ADDRS,
             BRN_EMAIL_ADDRS,
             BRN_POST_ADDRS,
             BRN_TWN_CODE,
             BRN_COU_CODE,
             BRN_CONTACT,
             BRN_MANAGER,
             BRN_TEL,
             BRN_FAX,
             BRN_GEN_POL_CLM,
             BRN_BNS_CODE,
             BRN_AGN_CODE,
             BRN_POST_LEVEL,
              BRN_MNGR_ALLOWED,
              BRN_OVERIDE_COMM_EARNED,
              BRN_BRA_MNGR_SEQ_NO,
              BRN_AGN_SEQ_NO,
              BRN_POL_PREFIX,
              BRN_POL_SEQ,
               BRN_PROP_SEQ
        FROM tqc_branches, tqc_regions, tqc_user_branches, TQC_USERS
       WHERE USB_BRN_CODE = BRN_CODE
         AND USB_USR_CODE = USR_CODE
         AND USR_USERNAME = v_username
         AND reg_org_code = 2
         AND brn_reg_code = reg_code
         and USB_DFLT_BRN = 'Y';
  END;

  PROCEDURE get_account_branches_lov(v_refcur OUT branches_ref) IS
  
  BEGIN
    OPEN v_refcur FOR
    
      SELECT -2000,
             'ALL BRANCHES',
             NULL,
             'ALL BRANCHES',
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
             NULL,
              NULL
           
            
        FROM DUAL
      UNION
      SELECT BRN_CODE,
             BRN_SHT_DESC,
             BRN_REG_CODE,
             BRN_NAME,
             BRN_PHY_ADDRS,
             BRN_EMAIL_ADDRS,
             BRN_POST_ADDRS,
             BRN_TWN_CODE,
             BRN_COU_CODE,
             BRN_CONTACT,
             BRN_MANAGER,
             BRN_TEL,
             BRN_FAX,
             BRN_GEN_POL_CLM,
             BRN_BNS_CODE,
             BRN_AGN_CODE,
             BRN_POST_LEVEL,
              BRN_MNGR_ALLOWED,
              BRN_OVERIDE_COMM_EARNED,
              BRN_BRA_MNGR_SEQ_NO,
              BRN_AGN_SEQ_NO,
              BRN_POL_PREFIX,
              BRN_POL_SEQ,
               BRN_PROP_SEQ
        FROM tqc_branches, tqc_regions
       WHERE reg_org_code = 2
         AND brn_reg_code = reg_code;
  END;

  FUNCTION get_personnel RETURN personel_ref IS
    v_cursor personel_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT PER_ID,
             PER_NP_CODE || ' ' || PER_FIRST_NAMES || ' ' ||
             PER_MIDDLE_NAME || ' ' || PER_LAST_NAME PER_NAMES,
             CON_CODE
        FROM HCO_PERSONNELS, HCO_CONTRACTS
       WHERE PER_ID = CON_PER_ID;
    RETURN v_cursor;
  END;

  FUNCTION get_personnel_name(v_per_id NUMBER) RETURN VARCHAR2 IS
    v_rtval VARCHAR2(200);
  BEGIN
    IF v_per_id IS NULL THEN
      RETURN NULL;
    ELSE
      SELECT PER_NP_CODE || ' ' || PER_FIRST_NAMES || ' ' ||
             PER_MIDDLE_NAME || ' ' || PER_LAST_NAME PER_NAMES
        INTO v_rtval
        FROM HCO_PERSONNELS
       WHERE PER_ID = v_per_id;
    END IF;
    RETURN v_rtval;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              '..Error retrieving Personnel name..');
  END get_personnel_name;

  FUNCTION getMemoType(v_sys_code TQC_MEMO_TYPES.MTYP_SYS_CODE%TYPE,
                       v_appl_lvl VARCHAR2) RETURN memo_types_ref IS
    v_cursor memo_types_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT MTYP_CODE,
             MTYP_SYS_CODE,
             MTYP_MEMO_TYPE,
             MTYP_APPL_LVL,
             MTYP_STATUS,
             MTYP_SUB_CODE
        FROM TQC_MEMO_TYPES
       WHERE MTYP_STATUS NOT IN ('I')
         AND MTYP_SYS_CODE = v_sys_code
      --AND MTYP_APPL_LVL = DECODE(v_appl_lvl,'L',MTYP_APPL_LVL,'V',MTYP_APPL_LVL,v_appl_lvl) 
       ORDER BY 1;
    RETURN v_cursor;
  END;

  FUNCTION get_memos_subjects(v_mtyp_code NUMBER) RETURN memos_ref IS
    v_cursor memos_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT MEMO_CODE, MEMO_SUBJECT, MEMO_MTYP_CODE
        FROM TQC_MEMOS
       WHERE MEMO_MTYP_CODE = v_mtyp_code;
  
    RETURN v_cursor;
  END;

  FUNCTION get_memo_details(v_memo_code NUMBER) RETURN memo_details_ref IS
    v_cursor memo_details_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT MEMDET_CODE, MEMDET_CONTENT, MEMDET_MEMO_CODE
        FROM TQC_MEMO_DETAILS
       WHERE MEMDET_MEMO_CODE = v_memo_code;
  
    RETURN v_cursor;
  END;

  FUNCTION getMemoDetails(v_mtyp_code NUMBER) RETURN memoDetails_ref IS
    v_cursor memoDetails_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT MEMO_CODE,
             MEMO_SUBJECT,
             MEMO_MTYP_CODE,
             MEMDET_CODE,
             MEMDET_CONTENT,
             MEMDET_MEMO_CODE
        FROM TQC_MEMOS, TQC_MEMO_DETAILS
       WHERE MEMDET_MEMO_CODE = MEMO_CODE
         AND MEMO_MTYP_CODE = v_mtyp_code;
    RETURN v_cursor;
  END;

  FUNCTION getMemoDtls(v_mtyp_code NUMBER) RETURN memoDtls_ref IS
    v_cursor memoDtls_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT MEMO_CODE,
             MEMO_SUBJECT,
             MEMO_MTYP_CODE,
             MEMDET_CODE,
             MEMDET_CONTENT,
             MEMDET_MEMO_CODE,
             SCL_CODE,
             SCL_DESC
        FROM TQC_MEMOS, TQC_MEMO_DETAILS, tqc_memo_types, GIN_SUB_CLASSES
       WHERE MEMDET_MEMO_CODE = MEMO_CODE
         AND MEMO_MTYP_CODE = v_mtyp_code
         AND MTYP_CODE = MEMO_MTYP_CODE
         AND SCL_CODE(+) = MTYP_SUB_CODE;
    RETURN v_cursor;
  END;

  PROCEDURE find_sub_classes(v_refcur OUT sub_classes_ref) IS
  BEGIN
    OPEN v_refcur FOR
      SELECT SCL_CODE, SCL_DESC
        FROM GIN_SUB_CLASSES
      UNION
      SELECT NULL, 'NONE' FROM DUAL ORDER BY 1 DESC NULLS FIRST;
  END;

  FUNCTION getMemoTypeInDtls(v_sys_code TQC_MEMO_TYPES.MTYP_SYS_CODE%TYPE,
                             v_appl_lvl VARCHAR2) RETURN memo_typesDtls_ref IS
    v_cursor memo_typesDtls_ref;
  BEGIN
    OPEN v_cursor FOR
      SELECT MTYP_CODE,
             MTYP_SYS_CODE,
             MTYP_MEMO_TYPE,
             MTYP_SAA_CODE,
             SAA_DESCRIPTION,
             MTYP_STATUS,
             MTYP_SUB_CODE,
             SCL_DESC,
             MTYP_APPL_LVL
        FROM TQC_MEMO_TYPES, GIN_SUB_CLASSES, TQC_SYS_APPLICABLE_AREAS
       WHERE MTYP_STATUS NOT IN ('I')
         AND MTYP_SYS_CODE = v_sys_code
         AND SCL_CODE(+) = MTYP_SUB_CODE
         AND MTYP_SAA_CODE = SAA_CODE(+)
      
       ORDER BY 1;
    RETURN v_cursor;
  END;
  PROCEDURE get_all_branches_lov(v_refcur OUT branches_ref) IS
  
  BEGIN
    OPEN v_refcur FOR
    
      SELECT BRN_CODE,
             BRN_SHT_DESC,
             BRN_REG_CODE,
             BRN_NAME,
             BRN_PHY_ADDRS,
             BRN_EMAIL_ADDRS,
             BRN_POST_ADDRS,
             BRN_TWN_CODE,
             BRN_COU_CODE,
             BRN_CONTACT,
             BRN_MANAGER,
             BRN_TEL,
             BRN_FAX,
             BRN_GEN_POL_CLM,
             BRN_BNS_CODE,
             BRN_AGN_CODE,
             BRN_POST_LEVEL,
              BRN_MNGR_ALLOWED,
              BRN_OVERIDE_COMM_EARNED,
              BRN_BRA_MNGR_SEQ_NO,
              BRN_AGN_SEQ_NO,
              BRN_POL_PREFIX,
              BRN_POL_SEQ,
               BRN_PROP_SEQ
        FROM tqc_branches, tqc_regions
       WHERE reg_org_code = 2
         AND brn_reg_code = reg_code;
  END;
END TQC_COMMON_CURSORS; 
/