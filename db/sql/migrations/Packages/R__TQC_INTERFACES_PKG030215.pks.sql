--
-- TQC_INTERFACES_PKG030215  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.Tqc_Interfaces_Pkg030215 IS
  TYPE orgrec IS RECORD(
    org_code     NUMBER(8),
    org_sht_desc VARCHAR2(15),
    org_name     VARCHAR2(50),
    org_cur_code NUMBER(8));

  TYPE orgrefcur IS REF CURSOR RETURN orgrec;

  TYPE orgrec2 IS RECORD(
    org_code     NUMBER(8),
    org_sht_desc VARCHAR2(15),
    org_name     VARCHAR2(50),
    org_cur_code NUMBER(8),
    org_type     VARCHAR2(3));

  TYPE orgrefcur2 IS REF CURSOR RETURN orgrec2;

  TYPE orgdtls IS RECORD(
    org_code     NUMBER(8),
    org_sht_desc VARCHAR2(15),
    org_name     VARCHAR2(50),
    org_cur_code NUMBER(8),
    org_type     VARCHAR2(3),
    lang_code    NUMBER,
    lang_desc    VARCHAR2(50),
    lang_bundle  VARCHAR2(50));

  TYPE orgdtlscur IS REF CURSOR RETURN orgdtls;

  TYPE orgcostrec IS RECORD(
    org_code     NUMBER(8),
    org_exc_code NUMBER(8),
    org_ccs_code NUMBER(8),
    org_name     VARCHAR2(50),
    org_pct      NUMBER(8));

  TYPE orgcostrefcur IS REF CURSOR RETURN orgcostrec;

  TYPE orgcosttable IS TABLE OF orgcostrec INDEX BY BINARY_INTEGER;

  TYPE branchrec IS RECORD(
    brn_code        NUMBER(15),
    brn_sht_desc    VARCHAR2(15),
    brn_name        VARCHAR2(50),
    reg_org_code    NUMBER(8),
    reg_code        NUMBER(8),
    reg_sht_desc    VARCHAR2(15),
    reg_name        VARCHAR2(100),
    brn_phy_addrs   VARCHAR2(100),
    brn_email_addrs VARCHAR2(30),
    brn_post_addrs  VARCHAR2(10),
    brn_twn_code    NUMBER(8),
    brn_cou_code    NUMBER(8),
    brn_contact     VARCHAR2(30),
    brn_manager     VARCHAR2(30),
    brn_tel         VARCHAR2(50),
    brn_fax         VARCHAR2(15),
    brn_bns_code    NUMBER(15));

  TYPE branchrefcur IS REF CURSOR RETURN branchrec;

  TYPE menurec IS RECORD(
    menu_name      VARCHAR2(50),
    menu_type      VARCHAR2(1),
    menu_menu_code NUMBER(8));

  TYPE menurefcur IS REF CURSOR RETURN menurec;

  TYPE menurec2 IS RECORD(
    menu_name          VARCHAR2(50),
    menu_type          VARCHAR2(1),
    menu_menu_code     NUMBER(8),
    menu_org_type_show VARCHAR2(3),
    menu_brk_label     VARCHAR2(50));

  TYPE menurefcur2 IS REF CURSOR RETURN menurec2;

  TYPE type_systems_rec IS RECORD(
    sys_code     NUMBER(8),
    sys_name     VARCHAR2(100),
    sys_sht_desc VARCHAR2(15),
    sys_org_code NUMBER(8));

  TYPE type_systems_ref_cur IS REF CURSOR RETURN type_systems_rec;

  TYPE usrs_rec IS RECORD(
    USR_CODE     TQC_USERS.USR_CODE%TYPE,
    USR_USERNAME TQC_USERS.USR_USERNAME%TYPE,
    USR_NAME     TQC_USERS.USR_NAME%TYPE);

  TYPE usrs_ref IS REF CURSOR RETURN usrs_rec;

  PROCEDURE organizations_query(corgrefcur IN OUT orgrefcur,
                                vorgcode   IN NUMBER DEFAULT NULL,
                                vbrhcode   IN NUMBER DEFAULT NULL);

  PROCEDURE getorganizations(corgrefcur IN OUT orgrefcur,
                             vusername  IN VARCHAR2);

  PROCEDURE branches_query(cbrhrefcur IN OUT branchrefcur,
                           vorgcode   IN NUMBER DEFAULT NULL,
                           vbrhcode   IN NUMBER DEFAULT NULL,
                           vusercode  IN NUMBER DEFAULT NULL);

  FUNCTION menuitems_query(vsyscode IN NUMBER, vusername IN VARCHAR2)
    RETURN menurefcur;

  FUNCTION menuitems_query2(vsyscode IN NUMBER, vusername IN VARCHAR2)
    RETURN menurefcur2;

  FUNCTION menuname(vmenucode NUMBER) RETURN VARCHAR2;

  FUNCTION formsystemcode(vformname VARCHAR2) RETURN NUMBER;

  FUNCTION formsystemcode2(vformname VARCHAR2, v_org_type OUT VARCHAR2)
    RETURN NUMBER;
  FUNCTION Module_enabled(vmodule IN VARCHAR2) RETURN BOOLEAN;
  FUNCTION GetObjectFileName(vObjectName VARCHAR2,
                             vSysCode    NUMBER DEFAULT NULL) RETURN VARCHAR2;
  PROCEDURE formheader(vformname IN VARCHAR2,
                       vtoolbar  IN OUT VARCHAR2,
                       vsysname  IN OUT VARCHAR2);

  FUNCTION getdrlimit(v_user_code NUMBER,
                      v_sys_code  NUMBER,
                      v_auth_area VARCHAR2) RETURN NUMBER;
  FUNCTION taxes_applicable RETURN VARCHAR2;
  FUNCTION accounttypeid(v_act_code NUMBER DEFAULT NULL,
                         vtype_id   CHAR DEFAULT 'T') RETURN VARCHAR2;

  FUNCTION accountcode(v_act_type_id VARCHAR) RETURN NUMBER;

  FUNCTION agency_query(v_search_val VARCHAR2)
    RETURN fms_interfaces_pkg.type_client_types_ref_cursor;

  FUNCTION agencyname(v_act_code NUMBER) RETURN VARCHAR2;

  FUNCTION get_org_type(v_sys_code NUMBER) RETURN VARCHAR2;
  FUNCTION get_org_type(v_sys_name VARCHAR2) RETURN VARCHAR2;

  FUNCTION username(v_usr_code NUMBER, v_full VARCHAR2 DEFAULT 'N')
    RETURN VARCHAR2;

  FUNCTION usercode(v_usr_name VARCHAR2) RETURN NUMBER;

  FUNCTION orgcurrency(v_sys_code IN NUMBER, v_cur_symbol OUT VARCHAR2)
    RETURN NUMBER;

  FUNCTION currencyname(v_curr_code NUMBER, v_full VARCHAR2 DEFAULT 'S')
    RETURN VARCHAR2;

  FUNCTION currencyrate(v_fcur NUMBER, v_bcur NUMBER, v_date DATE)
    RETURN NUMBER;

  FUNCTION bankbranch(v_bbr_code NUMBER, v_full VARCHAR2 DEFAULT 'S')
    RETURN VARCHAR2;

  FUNCTION bank_ref(v_bbr_code NUMBER) RETURN VARCHAR2;

  FUNCTION branchname(v_brn_code NUMBER, v_full VARCHAR2 DEFAULT 'Y')
    RETURN VARCHAR2;

  FUNCTION branchname(v_org_code NUMBER,
                      v_bns_code NUMBER,
                      v_full     VARCHAR2 DEFAULT 'Y') RETURN VARCHAR2;

  FUNCTION systemname(v_sys_code NUMBER, v_full VARCHAR2 DEFAULT 'Y')
    RETURN VARCHAR2;

  FUNCTION branchorganization(v_brh_code NUMBER) RETURN NUMBER;

  FUNCTION branchregion(v_brh_code NUMBER) RETURN NUMBER;

  FUNCTION branchregion(v_org_code NUMBER, v_bns_code NUMBER) RETURN NUMBER;

  FUNCTION currencysymbol(vcurcode NUMBER) RETURN VARCHAR2;

  FUNCTION systemorganization(vsyscode VARCHAR2) RETURN NUMBER;

  FUNCTION organizationname(vorgcode NUMBER, vfull VARCHAR2 DEFAULT 'Y')
    RETURN VARCHAR2;

  FUNCTION organizationname(vfull VARCHAR2 DEFAULT 'Y') RETURN VARCHAR2;

  PROCEDURE costappropriation_query(corgcostrefcur IN OUT orgcostrefcur,
                                    vccscode       IN NUMBER,
                                    vexccode       IN NUMBER);

  FUNCTION clientname(v_clnt_code NUMBER) RETURN VARCHAR2;

  FUNCTION systemcode(v_sys_name IN VARCHAR2) RETURN NUMBER;

  PROCEDURE systemsunionfms(systems_data_rec IN OUT type_systems_ref_cur,
                            v_org_code       IN NUMBER);

  FUNCTION branchcode(v_org_code NUMBER, v_bns_code NUMBER) RETURN NUMBER;

  FUNCTION branchcode(v_brh_code NUMBER) RETURN NUMBER;

  FUNCTION branchinorganization(v_org_code NUMBER, v_brh_code NUMBER)
    RETURN NUMBER;

  FUNCTION userinbranch(v_usr_name VARCHAR2, vbrncode NUMBER) RETURN VARCHAR2;
  FUNCTION userinbranch(vusrcode NUMBER, vbrncode NUMBER) RETURN VARCHAR2;

  PROCEDURE logaudit(vsessionid NUMBER);

  FUNCTION userinorganization(vusrcode NUMBER, vorgcode NUMBER)
    RETURN VARCHAR2;

  FUNCTION check_authorization_roles(v_usr_name  IN VARCHAR2,
                                     v_sys_code  IN NUMBER,
                                     v_auth_area IN VARCHAR2,
                                     v_amount    IN NUMBER,
                                     v_dc        IN VARCHAR2,
                                     v_pro_code  IN NUMBER) RETURN BOOLEAN;
  FUNCTION check_user_rights(v_usr_name      IN VARCHAR2,
                             v_sys_code      IN NUMBER,
                             v_process       IN VARCHAR2,
                             v_prcs_area     IN VARCHAR2,
                             v_prsc_sub_area IN VARCHAR2,
                             v_amount        IN NUMBER DEFAULT NULL,
                             v_dc            IN VARCHAR2 DEFAULT NULL)
    RETURN VARCHAR2;

  FUNCTION check_user_rights2(v_user_code     IN NUMBER,
                              v_sys_code      IN NUMBER,
                              v_process       IN VARCHAR2,
                              v_prcs_area     IN VARCHAR2,
                              v_prsc_sub_area IN VARCHAR2,
                              v_amount        IN NUMBER DEFAULT NULL,
                              v_dc            IN VARCHAR2 DEFAULT NULL)
    RETURN BOOLEAN;

  FUNCTION check_userAuth_rights(v_usr_name      IN VARCHAR2,
                                 v_sys_code      IN NUMBER,
                                 v_process       IN VARCHAR2,
                                 v_prcs_area     IN VARCHAR2,
                                 v_prsc_sub_area IN VARCHAR2,
                                 v_transId       IN NUMBER) RETURN VARCHAR2;

  FUNCTION get_task_assignee(v_sys_code      IN NUMBER,
                             v_process       IN VARCHAR2,
                             v_prcs_area     IN VARCHAR2,
                             v_prsc_sub_area IN VARCHAR2,
                             v_amount        IN NUMBER DEFAULT NULL,
                             v_dc            IN VARCHAR2 DEFAULT NULL,
                             v_dflt_usr_code OUT NUMBER,
                             v_dflt_usrname  OUT VARCHAR2) RETURN usrs_ref;

  FUNCTION eft_enabled(v_bbr_code NUMBER) RETURN VARCHAR2;

  PROCEDURE appendclob(v_clob IN OUT CLOB,
                       v_char VARCHAR2,
                       v_temp VARCHAR2 DEFAULT 'N');

  PROCEDURE LOG(v_sys NUMBER, v_char VARCHAR2);
  PROCEDURE getorganizationdetails(corgrefcur IN OUT orgdtlscur,
                                   v_org_code IN NUMBER);
  /******************************************************************************
     NAME:       FMS_INTERFACES_PKG
     PURPOSE:    To proxy all references to external objects.
     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        3/26/2003             1. Created this package.
  */
  FUNCTION orgcurrency(v_sys_code IN NUMBER) RETURN NUMBER;
  FUNCTION check_excp_auth_rights(v_usr_code      NUMBER,
                                  v_sys_code      NUMBER,
                                  v_process       VARCHAR2,
                                  v_prcs_area     VARCHAR2,
                                  v_prcs_sub_area VARCHAR2) RETURN BOOLEAN;

  TYPE gin_treaty_accounts_rec IS RECORD(
    ACC_NAME   FMS_ACCOUNTS.ACC_NAME%TYPE,
    ACC_NUMBER FMS_ACCOUNTS.ACC_NUMBER%TYPE);

  TYPE gin_treaty_accounts_cursor IS REF CURSOR RETURN gin_treaty_accounts_rec;

  PROCEDURE get_gin_treaty_accounts(v_cursor OUT gin_treaty_accounts_cursor);

  FUNCTION orgPaymentMode(v_sys_code IN NUMBER, v_pay_symbol OUT VARCHAR2)
    RETURN NUMBER;
  FUNCTION check_approval_right(v_user_code     NUMBER,
                                v_area_sht_desc VARCHAR2,
                                v_process       VARCHAR2,
                                v_prcs_area     VARCHAR2,
                                v_sys_code      NUMBER) RETURN VARCHAR2;
  function getCouSchengen(v_cou_name TQC_COUNTRIES.COU_NAME%TYPE)
    RETURN VARCHAR2;
  FUNCTION getStateName(v_state_code IN NUMBER) RETURN VARCHAR2;
  FUNCTION orgcurrencyDefault(v_cur_symbol OUT VARCHAR2)
    RETURN VARCHAR2;
    FUNCTION check_user_limit (v_user_code    IN   NUMBER,
                              v_sys_code    IN   NUMBER,
                              v_process     IN VARCHAR2,
                              v_prcs_area   IN VARCHAR2,
                              v_prsc_sub_area   IN   VARCHAR2,
                              v_amount      IN   NUMBER DEFAULT NULL,
                              v_dc          IN   VARCHAR2 DEFAULT NULL) RETURN NUMBER;
 FUNCTION orgcurrencysymbol (v_sys_code IN NUMBER )
   RETURN VARCHAR2;
   
   PROCEDURE create_alert_prc(v_alrt_code IN NUMBER, -- --alert type
                            v_user_code IN NUMBER, -- user to whom the alert is being send to
                            v_user IN VARCHAR2,     -- user raising the alert
                            v_reciepient IN VARCHAR2, -- reciepient -c client,u user, a agencies
                            v_clnt_code IN NUMBER, 
                            v_agn_code IN NUMBER,
                            v_quot_code IN NUMBER,
                            v_quot_no IN VARCHAR2,
                            v_pol_code IN NUMBER,
                            v_pol_no IN VARCHAR2,
                            v_clm_no IN VARCHAR2,
                            v_msg_subj IN VARCHAR2,
                            v_app_lvl IN VARCHAR2,
                            v_email_sender_addr IN VARCHAR2  DEFAULT NULL,
                            v_alrts_to_send_date IN DATE
                                            );
PROCEDURE create_alert_prc080115(v_alrt_code IN NUMBER, -- --alert type
                                            v_user_code IN NUMBER, -- user to whom the alert is being send to
                                            v_user IN VARCHAR2,     -- user raising the alert
                                            v_reciepient IN VARCHAR2, -- reciepient -c client,u user, a agencies
                                            v_clnt_code IN NUMBER, 
                                            v_agn_code IN NUMBER,
                                            v_quot_code IN NUMBER,
                                            v_quot_no IN VARCHAR2,
                                            v_pol_code IN NUMBER,
                                            v_pol_no IN VARCHAR2,
                                            v_clm_no IN VARCHAR2,
                                            v_msg_subj IN VARCHAR2,
                                            v_app_lvl IN VARCHAR2,
                                            v_email_sender_addr IN VARCHAR2  DEFAULT NULL
                                            ) ;
FUNCTION orgcurrencycode (v_sys_code IN NUMBER )
   RETURN NUMBER;
END Tqc_Interfaces_Pkg030215; 
 
/