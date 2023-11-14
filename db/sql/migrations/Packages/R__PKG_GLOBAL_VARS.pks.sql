--
-- PKG_GLOBAL_VARS  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."PKG_GLOBAL_VARS" AS
--Purpose: this package can be used to process server side package
--global variables. Each global variable can be declared below or
--in any other PL/SQL package. Any variable in this section has a
--scope of  current session. The set_pvar Procedure is overloaded
--and can accept either a NUMBER, VARCHAR2, OR DATE as the second
--parameter. The first parameter should in the format
--package_name.variable_name. The get_pnumber, get_pdate, and
--get_pvarchar2     FUNCTIONS can be used to receive either a NUMBER,
--DATE, AND VARCHAR2, respectively.
   PVG_CURSOR_NUM PLS_INTEGER;
    PVG_TEMP_NUM   PLS_INTEGER;
    PVG_ORD_NUM        NUMBER;
    PVG_CUST_NUM    NUMBER;
    PVG_USERNAME    VARCHAR2(250);
    PVG_AUTOFIX_PKGS VARCHAR2(5);
    PVG_AUTOFIX        VARCHAR2(2);
    PVG_SESSION_ID    NUMBER;
    PVG_ORG_TYPE    VARCHAR2( 5);
    V_USR_CODE NUMBER;
    V_SYS_CODE NUMBER;
    P_USR_CODE NUMBER;
    P_SYS_CODE NUMBER;
    V_CLNT_STATUS VARCHAR2(10);
    V_CLNT_CODE NUMBER;
    P_CLNT_CODE NUMBER(15);
    V_CLNA_STATUS VARCHAR2(10);
    P_CLIENT VARCHAR2(100);
    V_CLIENT VARCHAR2(100);
    P_STATUS VARCHAR2(100);
    V_STATUS VARCHAR2(100);
    FROMDT DATE;
    TODT DATE;
    DATE_FROM date;
    DATE_TO  date;
    P_BBR NUMBER;
    P_REF_NO VARCHAR2(25);
    P_POLICY_NO VARCHAR2(25);
    P_PARAM_CODE NUMBER;
    P_AGN_CODE  NUMBER;
    PVG_CLIENTCODE NUMBER;
    PROCEDURE set_pvar(p_varname_txt VARCHAR2,p_val NUMBER);
    PROCEDURE set_pvar(p_varname_txt VARCHAR2,p_val VARCHAR2);
    PROCEDURE set_pvar(p_varname_txt VARCHAR2,p_val DATE);
    FUNCTION get_pnumber(p_varname_txt VARCHAR2) RETURN NUMBER;
    FUNCTION get_pvarchar2(p_varname_txt VARCHAR2) RETURN VARCHAR2;
    FUNCTION get_pdate(p_varname_txt VARCHAR2) RETURN DATE;
    FUNCTION get_param_number(p_varname_txt VARCHAR2) RETURN NUMBER;
    FUNCTION get_param_varchar2(p_varname_txt VARCHAR2) RETURN VARCHAR2;
    FUNCTION get_param_date(p_varname_txt VARCHAR2) RETURN DATE;
END Pkg_Global_Vars; 
/