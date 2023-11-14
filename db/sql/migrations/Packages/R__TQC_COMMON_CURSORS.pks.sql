--
-- TQC_COMMON_CURSORS  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_COMMON_CURSORS AS
  /******************************************************************************
     NAME:       TQC_COMMON_CURSORS
     PURPOSE:
  
     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        3/22/2010             1. Created this package.
  ******************************************************************************/

  TYPE countries_rec IS RECORD(
    COU_CODE        TQC_COUNTRIES.COU_CODE%TYPE,
    COU_SHT_DESC    TQC_COUNTRIES.COU_SHT_DESC%TYPE,
    COU_NAME        TQC_COUNTRIES.COU_NAME%TYPE,
    COU_BASE_CURR   TQC_COUNTRIES.COU_BASE_CURR%TYPE,
    COU_NATIONALITY TQC_COUNTRIES.COU_NATIONALITY%TYPE,
    COU_ZIP_CODE    TQC_COUNTRIES.COU_ZIP_CODE%TYPE);

  TYPE countries_ref IS REF CURSOR RETURN countries_rec;
  PROCEDURE getcountries(v_refcur OUT countries_ref);

  TYPE towns_rec IS RECORD(
    TWN_CODE     TQC_TOWNS.TWN_CODE%TYPE,
    TWN_COU_CODE TQC_TOWNS.TWN_COU_CODE%TYPE,
    TWN_SHT_DESC TQC_TOWNS.TWN_SHT_DESC%TYPE,
    TWN_NAME     TQC_TOWNS.TWN_NAME%TYPE);

  TYPE towns_ref IS REF CURSOR RETURN towns_rec;
  PROCEDURE gettowns(v_cou_code IN NUMBER, v_refcur OUT towns_ref);

  TYPE branches_rec IS RECORD(
    BRN_CODE        TQC_BRANCHES.BRN_CODE%TYPE,
    BRN_SHT_DESC    TQC_BRANCHES.BRN_SHT_DESC%TYPE,
    BRN_REG_CODE    TQC_BRANCHES.BRN_REG_CODE%TYPE,
    BRN_NAME        TQC_BRANCHES.BRN_NAME%TYPE,
    BRN_PHY_ADDRS   TQC_BRANCHES.BRN_PHY_ADDRS%TYPE,
    BRN_EMAIL_ADDRS TQC_BRANCHES.BRN_EMAIL_ADDRS%TYPE,
    BRN_POST_ADDRS  TQC_BRANCHES.BRN_POST_ADDRS%TYPE,
    BRN_TWN_CODE    TQC_BRANCHES.BRN_TWN_CODE%TYPE,
    BRN_COU_CODE    TQC_BRANCHES.BRN_COU_CODE%TYPE,
    BRN_CONTACT     TQC_BRANCHES.BRN_CONTACT%TYPE,
    BRN_MANAGER     TQC_BRANCHES.BRN_MANAGER%TYPE,
    BRN_TEL         TQC_BRANCHES.BRN_TEL%TYPE,
    BRN_FAX         TQC_BRANCHES.BRN_FAX%TYPE,
    BRN_GEN_POL_CLM TQC_BRANCHES.BRN_GEN_POL_CLM%TYPE,
    BRN_BNS_CODE    TQC_BRANCHES.BRN_BNS_CODE%TYPE,
    BRN_AGN_CODE    TQC_BRANCHES.BRN_AGN_CODE%TYPE,
    BRN_POST_LEVEL  TQC_BRANCHES.BRN_POST_LEVEL%TYPE,
    BRN_MNGR_ALLOWED  TQC_BRANCHES.BRN_MNGR_ALLOWED%TYPE,
    BRN_OVERIDE_COMM_EARNED  TQC_BRANCHES.BRN_OVERIDE_COMM_EARNED%TYPE,
    BRN_BRA_MNGR_SEQ_NO  TQC_BRANCHES.BRN_BRA_MNGR_SEQ_NO%TYPE,
    BRN_AGN_SEQ_NO  TQC_BRANCHES.BRN_AGN_SEQ_NO%TYPE,
    BRN_POL_PREFIX  TQC_BRANCHES.BRN_POL_PREFIX%TYPE,
     BRN_POL_SEQ  TQC_BRANCHES.BRN_POL_SEQ%TYPE,
    BRN_PROP_SEQ  TQC_BRANCHES.BRN_PROP_SEQ%TYPE);

  TYPE branches_ref IS REF CURSOR RETURN branches_rec;
  PROCEDURE getorgbranches(v_org_code IN NUMBER, v_refcur OUT branches_ref);

  TYPE tqc_sectors_rec IS RECORD(
    SEC_CODE     tqc_sectors.SEC_CODE%TYPE,
    SEC_SHT_DESC tqc_sectors.SEC_SHT_DESC%TYPE,
    SEC_NAME     tqc_sectors.SEC_NAME%TYPE);

  TYPE tqc_sectors_ref IS REF CURSOR RETURN tqc_sectors_rec;
  PROCEDURE get_sectors(v_refcur OUT tqc_sectors_ref);

  TYPE cal_activites_rec IS RECORD(
    cala_location    tqc_calendar_activities.cala_location%TYPE,
    cala_title       tqc_calendar_activities.cala_title%TYPE,
    startadddays     NUMBER,
    cala_starthour   tqc_calendar_activities.cala_starthour%TYPE,
    cala_startmin    tqc_calendar_activities.cala_startmin%TYPE,
    cala_endadddays  NUMBER,
    cala_endaddhours tqc_calendar_activities.cala_endaddhours%TYPE,
    cala_endaddmin   tqc_calendar_activities.cala_endaddmin%TYPE);

  TYPE cal_activites_ref IS REF CURSOR RETURN cal_activites_rec;
  PROCEDURE get_cal_activities(v_refcur   OUT cal_activites_ref,
                               v_username VARCHAR2);
  PROCEDURE get_current_date(v_date  OUT DATE,
                             v_day   OUT VARCHAR2,
                             v_month OUT VARCHAR2,
                             v_year  OUT VARCHAR2);
  TYPE insurers_rec IS RECORD(
    AGN_CODE tqc_agencies.AGN_CODE%TYPE,
    AGN_NAME tqc_agencies.AGN_NAME%TYPE);

  TYPE insurers_ref IS REF CURSOR RETURN insurers_rec;
  PROCEDURE get_insurers(v_refcur OUT insurers_ref);

  /*TYPE currency_rec IS RECORD(
    v_CUR_CODE   tqc_currencies.CUR_CODE%TYPE,
    v_CUR_SYMBOL tqc_currencies.CUR_SYMBOL%TYPE,
    v_CUR_DESC   tqc_currencies.CUR_DESC%TYPE);*/
    
    TYPE currency_rec IS RECORD(
   v_CUR_CODE   tqc_currencies.CUR_CODE%TYPE,
   v_CUR_SYMBOL tqc_currencies.CUR_SYMBOL%TYPE,
   v_CUR_DESC   tqc_currencies.CUR_DESC%TYPE,
   v_CUR_RATE   number
   );


  TYPE currency_cursor IS REF CURSOR RETURN currency_rec;

  PROCEDURE get_treaty_currencies(v_cursor OUT currency_cursor);

  PROCEDURE get_treaty_currency_values(v_code   IN NUMBER,
                                       v_cursor OUT currency_cursor);

  TYPE tqc_doc_required_rec IS RECORD(
    v_TQDOCR_CODE         TQC_DOC_REQRD_SUBMTD.TQDOCR_CODE%TYPE,
    v_TQDOCR_DOCR_CODE    TQC_DOC_REQRD_SUBMTD.TQDOCR_DOCR_CODE%TYPE,
    v_TQDOCR_CLNT_CODE    TQC_DOC_REQRD_SUBMTD.TQDOCR_CLNT_CODE%TYPE,
    v_TQDOCR_SUBMITED     TQC_DOC_REQRD_SUBMTD.TQDOCR_SUBMITED%TYPE,
    v_TQDOCR_DATE_S       TQC_DOC_REQRD_SUBMTD.TQDOCR_DATE_S%TYPE,
    v_TQDOCR_REF_NO       TQC_DOC_REQRD_SUBMTD.TQDOCR_REF_NO%TYPE,
    v_TQDOCR_RMRK         TQC_DOC_REQRD_SUBMTD.TQDOCR_RMRK%TYPE,
    v_TQDOCR_USER_RECEIVD TQC_DOC_REQRD_SUBMTD.TQDOCR_USER_RECEIVD%TYPE);

  TYPE tqc_doc_required_cursor IS REF CURSOR RETURN tqc_doc_required_rec;

  PROCEDURE get_default_currency_val(v_cursor OUT currency_cursor);

  PROCEDURE get_default_branches(v_username IN VARCHAR2,
                                 v_refcur   OUT branches_ref);

  PROCEDURE get_account_branches_lov(v_refcur OUT branches_ref);

  TYPE personel_rec IS RECORD(
    PER_ID    HCO_PERSONNELS.PER_ID%TYPE,
    PER_NAMES VARCHAR2(300),
    CON_CODE  HCO_CONTRACTS.CON_CODE%TYPE);

  TYPE personel_ref IS REF CURSOR RETURN personel_rec;
  FUNCTION get_personnel RETURN personel_ref;

  FUNCTION get_personnel_name(v_per_id NUMBER) RETURN VARCHAR2;

  TYPE memo_types_rec IS RECORD(
    MTYP_CODE      TQC_MEMO_TYPES.MTYP_CODE%TYPE,
    MTYP_SYS_CODE  TQC_MEMO_TYPES.MTYP_SYS_CODE%TYPE,
    MTYP_MEMO_TYPE TQC_MEMO_TYPES.MTYP_MEMO_TYPE%TYPE,
    MTYP_APPL_LVL  TQC_MEMO_TYPES.MTYP_APPL_LVL%TYPE,
    MTYP_STATUS    TQC_MEMO_TYPES.MTYP_STATUS%TYPE,
    MTYP_SUB_CODE  TQC_MEMO_TYPES.MTYP_SUB_CODE%TYPE);

  TYPE memo_types_ref IS REF CURSOR RETURN memo_types_rec;

  FUNCTION getMemoType(v_sys_code TQC_MEMO_TYPES.MTYP_SYS_CODE%TYPE,
                       v_appl_lvl VARCHAR2) RETURN memo_types_ref;

  TYPE memos_rec IS RECORD(
    MEMO_CODE      TQC_MEMOS.MEMO_CODE%TYPE,
    MEMO_SUBJECT   TQC_MEMOS.MEMO_SUBJECT%TYPE,
    MEMO_MTYP_CODE TQC_MEMOS.MEMO_MTYP_CODE%TYPE);
  TYPE memos_ref IS REF CURSOR RETURN memos_rec;
  FUNCTION get_memos_subjects(v_mtyp_code NUMBER) RETURN memos_ref;

  TYPE memo_details_rec IS RECORD(
    MEMDET_CODE      TQC_MEMO_DETAILS.MEMDET_CODE%TYPE,
    MEMDET_CONTENT   TQC_MEMO_DETAILS.MEMDET_CONTENT%TYPE,
    MEMDET_MEMO_CODE TQC_MEMO_DETAILS.MEMDET_MEMO_CODE%TYPE
    
    );
  TYPE memo_details_ref IS REF CURSOR RETURN memo_details_rec;

  TYPE memoDetails_rec IS RECORD(
    MEMO_CODE        TQC_MEMOS.MEMO_CODE%TYPE,
    MEMO_SUBJECT     TQC_MEMOS.MEMO_SUBJECT%TYPE,
    MEMO_MTYP_CODE   TQC_MEMOS.MEMO_MTYP_CODE%TYPE,
    MEMDET_CODE      TQC_MEMO_DETAILS.MEMDET_CODE%TYPE,
    MEMDET_CONTENT   TQC_MEMO_DETAILS.MEMDET_CONTENT%TYPE,
    MEMDET_MEMO_CODE TQC_MEMO_DETAILS.MEMDET_MEMO_CODE%TYPE);
  TYPE memoDetails_ref IS REF CURSOR RETURN memoDetails_rec;
  FUNCTION get_memo_details(v_memo_code NUMBER) RETURN memo_details_ref;

  TYPE memoDtls_rec IS RECORD(
    MEMO_CODE        TQC_MEMOS.MEMO_CODE%TYPE,
    MEMO_SUBJECT     TQC_MEMOS.MEMO_SUBJECT%TYPE,
    MEMO_MTYP_CODE   TQC_MEMOS.MEMO_MTYP_CODE%TYPE,
    MEMDET_CODE      TQC_MEMO_DETAILS.MEMDET_CODE%TYPE,
    MEMDET_CONTENT   TQC_MEMO_DETAILS.MEMDET_CONTENT%TYPE,
    MEMDET_MEMO_CODE TQC_MEMO_DETAILS.MEMDET_MEMO_CODE%TYPE,
    SCL_CODE         GIN_SUB_CLASSES.SCL_CODE%TYPE,
    SCL_DESC         GIN_SUB_CLASSES.SCL_DESC%TYPE
    
    );
  TYPE memoDtls_ref IS REF CURSOR RETURN memoDtls_rec;
  FUNCTION getMemoDtls(v_mtyp_code NUMBER) RETURN memoDtls_ref;
  FUNCTION getMemoDetails(v_mtyp_code NUMBER) RETURN memoDetails_ref;

  TYPE sub_classes_rec IS RECORD(
    SCL_CODE GIN_SUB_CLASSES.SCL_CODE%TYPE,
    SCL_DESC GIN_SUB_CLASSES.SCL_DESC%TYPE);

  TYPE sub_classes_ref IS REF CURSOR RETURN sub_classes_rec;
  PROCEDURE find_sub_classes(v_refcur OUT sub_classes_ref);

  PROCEDURE getAllTowns(v_refcur OUT towns_ref);

  TYPE memo_typesDtls_rec IS RECORD(
    MTYP_CODE       TQC_MEMO_TYPES.MTYP_CODE%TYPE,
    MTYP_SYS_CODE   TQC_MEMO_TYPES.MTYP_SYS_CODE%TYPE,
    MTYP_MEMO_TYPE  TQC_MEMO_TYPES.MTYP_MEMO_TYPE%TYPE,
    MTYP_SAA_CODE   TQC_MEMO_TYPES.MTYP_SAA_CODE%TYPE,
    SAA_DESCRIPTION TQC_SYS_APPLICABLE_AREAS.SAA_DESCRIPTION%TYPE,
    MTYP_STATUS     TQC_MEMO_TYPES.MTYP_STATUS%TYPE,
    MTYP_SUB_CODE   TQC_MEMO_TYPES.MTYP_SUB_CODE%TYPE,
    SCL_DESC        GIN_SUB_CLASSES.SCL_DESC%TYPE,
    MTYP_APPL_LVL   TQC_MEMO_TYPES.MTYP_APPL_LVL%TYPE);

  TYPE memo_typesDtls_ref IS REF CURSOR RETURN memo_typesDtls_rec;

  FUNCTION getMemoTypeInDtls(v_sys_code TQC_MEMO_TYPES.MTYP_SYS_CODE%TYPE,
                             v_appl_lvl VARCHAR2) RETURN memo_typesDtls_ref;
  PROCEDURE get_all_branches_lov(v_refcur OUT branches_ref);
END TQC_COMMON_CURSORS; 
/