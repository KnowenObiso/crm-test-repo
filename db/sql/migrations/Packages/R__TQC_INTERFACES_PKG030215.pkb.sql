--
-- TQC_INTERFACES_PKG030215  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.Tqc_Interfaces_Pkg030215 AS
  PROCEDURE raise_error(v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL) IS
  BEGIN
    IF v_err_no IS NULL THEN
      RAISE_APPLICATION_ERROR(-20015, v_msg || ' - ' || SQLERRM(SQLCODE));
    ELSE
      RAISE_APPLICATION_ERROR(v_err_no, v_msg || ' - ' || SQLERRM(SQLCODE));
    END IF;
  END raise_error;
  PROCEDURE organizations_query(corgrefcur IN OUT orgrefcur,
                                vorgcode   IN NUMBER DEFAULT NULL,
                                vbrhcode   IN NUMBER DEFAULT NULL) IS
    v_usr_code NUMBER := vbrhcode; --note this is user not branch
  BEGIN
    OPEN corgrefcur FOR
      SELECT org_code, org_sht_desc, org_name, org_cur_code
        FROM TQC_ORGANIZATIONS
       WHERE (((TQC_ORGANIZATIONS.org_code = vorgcode) OR
             (vorgcode IS NULL)) AND ((v_usr_code IS NULL) OR (Tqc_Interfaces_Pkg.userinorganization(v_usr_code,
                                                                                                      org_code) =
             'TRUE')));
  END organizations_query;
  FUNCTION taxes_applicable RETURN VARCHAR2 IS
    v_tax VARCHAR2(1);
  BEGIN
    SELECT PARAM_VALUE
      INTO v_tax
      FROM TQC_PARAMETERS
     WHERE PARAM_NAME = 'TAXES_APPLICABLE';
    RETURN v_tax;
  EXCEPTION
    WHEN OTHERS THEN
      RETURN 'Y';
      NULL;
  END;
  FUNCTION GetObjectFileName(vObjectName VARCHAR2,
                             vSysCode    NUMBER DEFAULT NULL) RETURN VARCHAR2 IS
    vRetVal VARCHAR2(50);
  BEGIN
    SELECT NVL(OBJ_FILE_NAME, OBJ_NAME)
      INTO vRetVal
      FROM TQC_SYSTEM_OBJECTS
     WHERE ((OBJ_NAME = vObjectName) AND
           ((OBJ_SYS_CODE = vSysCode) OR (vSysCode IS NULL)));
  
    RETURN vRetVal;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN vObjectName;
  END GetObjectFileName;
  FUNCTION Module_enabled(vmodule IN VARCHAR2) RETURN BOOLEAN IS
    vrtn BOOLEAN := FALSE; --   VARCHAR2 (100);
  BEGIN
    IF UPPER(vmodule) = 'SMS' THEN
      vrtn := TRUE;
    ELSIF UPPER(vmodule) = 'EMAIL' THEN
      vrtn := TRUE;
    ELSIF UPPER(vmodule) = 'DMS' THEN
      vrtn := TRUE;
    END IF;
  
    RETURN vrtn;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END Module_enabled;

  PROCEDURE getorganizations(corgrefcur IN OUT orgrefcur,
                             vusername  IN VARCHAR2) IS
    v_usr_code NUMBER := usercode(vusername);
    --note this is user not branch
  BEGIN
    -- Organizations_Query(cOrgRefCur, null, v_usr_code);
    OPEN corgrefcur FOR
      SELECT org_code, org_sht_desc, org_name, org_cur_code
        FROM TQC_ORGANIZATIONS;
    --            WHERE ((   (v_usr_code IS NULL)
    --                    OR (
    --                    Tqc_Interfaces_Pkg.userinorganization (v_usr_code,
    --                                                               org_code
    --                                                              ) = 'TRUE'
    --                      )
    --                   )
    --                 );
  END getorganizations;
  PROCEDURE getorganizationdetails(corgrefcur IN OUT orgdtlscur,
                                   v_org_code IN NUMBER) IS
  
  BEGIN
    -- Organizations_Query(cOrgRefCur, null, v_usr_code);
    OPEN corgrefcur FOR
      SELECT org_code,
             org_sht_desc,
             org_name,
             org_cur_code,
             org_type,
             lang_code,
             lang_desc,
             lang_bundle
        FROM TQC_ORGANIZATIONS, TQC_LANGUAGES, TQC_SYSTEMS
       WHERE LANG_CODE = ORG_LANG_CODE
         AND SYS_ORG_CODE = ORG_CODE
         AND SYS_CODE = v_org_code;
    --            
  END getorganizationdetails;

  PROCEDURE branches_query(cbrhrefcur IN OUT branchrefcur,
                           vorgcode   IN NUMBER DEFAULT NULL,
                           vbrhcode   IN NUMBER DEFAULT NULL,
                           vusercode  IN NUMBER DEFAULT NULL) IS
  BEGIN
    IF vorgcode IS NULL THEN
      OPEN cbrhrefcur FOR
        SELECT bns_code,
               bns_sht_desc,
               bns_name,
               NULL,
               NULL,
               NULL,
               NULL,
               bns_phy_addrs,
               bns_email_addrs,
               bns_post_addrs,
               bns_twn_code,
               bns_cou_code,
               bns_contact,
               bns_manager,
               bns_tel,
               bns_fax,
               bns_code
          FROM TQC_BRANCH_NAMES
         WHERE ((TQC_BRANCH_NAMES.bns_code = vbrhcode) OR
               (vbrhcode IS NULL));
    ELSE
      OPEN cbrhrefcur FOR
        SELECT brn_code,
               brn_sht_desc,
               brn_name,
               reg_org_code,
               reg_code,
               reg_sht_desc,
               reg_name,
               brn_phy_addrs,
               brn_email_addrs,
               brn_post_addrs,
               brn_twn_code,
               brn_cou_code,
               brn_contact,
               brn_manager,
               brn_tel,
               brn_fax,
               brn_bns_code
          FROM TQC_BRANCHES, TQC_REGIONS
         WHERE ((TQC_REGIONS.reg_code = TQC_BRANCHES.brn_reg_code) AND
               (reg_org_code = vorgcode) AND
               ((TQC_BRANCHES.brn_code = vbrhcode) OR (vbrhcode IS NULL)));
    END IF;
  END branches_query;

  FUNCTION menuname(vmenucode NUMBER) RETURN VARCHAR2 IS
    vmenuname VARCHAR2(100);
  BEGIN
    SELECT menu_name
      INTO vmenuname
      FROM TQC_MENU
     WHERE menu_code = vmenucode;
  
    RETURN vmenuname;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END menuname;

  FUNCTION formsystemcode(vformname VARCHAR2) RETURN NUMBER IS
    vsystemcode NUMBER;
  BEGIN
    SELECT sys_code
      INTO vsystemcode
      FROM TQC_SYSTEM_OBJECTS, TQC_SYSTEMS
     WHERE sys_code = obj_sys_code
       AND obj_name = vformname;
  
    RETURN vsystemcode;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END formsystemcode;

  FUNCTION formsystemcode2(vformname VARCHAR2, v_org_type OUT VARCHAR2)
    RETURN NUMBER IS
    vsystemcode NUMBER;
  BEGIN
    SELECT sys_code, NVL(ORG_TYPE, 'INS')
      INTO vsystemcode, v_org_type
      FROM TQC_SYSTEM_OBJECTS, TQC_SYSTEMS, TQC_ORGANIZATIONS
     WHERE sys_code = obj_sys_code
       AND SYS_ORG_CODE = ORG_CODE
       AND obj_name = vformname;
  
    RETURN vsystemcode;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END formsystemcode2;

  PROCEDURE formheader(vformname IN VARCHAR2,
                       vtoolbar  IN OUT VARCHAR2,
                       vsysname  IN OUT VARCHAR2) IS
    vsystemcode NUMBER;
  BEGIN
    SELECT obj_label, sys_name
      INTO vtoolbar, vsysname
      FROM TQC_SYSTEM_OBJECTS, TQC_SYSTEMS
     WHERE sys_code = obj_sys_code
       AND obj_name = vformname;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'FORM NOT REGISTERED. CONTACT ADMINISTRATOR..');
  END formheader;

  FUNCTION menuitems_query(vsyscode IN NUMBER, vusername IN VARCHAR2)
    RETURN menurefcur IS
    cmenusrefcur menurefcur;
  BEGIN
    OPEN cmenusrefcur FOR
      SELECT menu_name, menu_type, menu_menu_code
        FROM TQC_MENU,
             TQC_SYSTEM_ROLE_MENU,
             TQC_SYSTEM_ROLES,
             TQC_USER_SYSTEM_ROLES
       WHERE menu_code = srm_menu_code
         AND srl_code = srm_srl_code
         AND srl_code = usrl_srl_code
         AND srl_sys_code = vsyscode
         AND usrl_revoke_date IS NULL
         AND usrl_usr_code =
             (SELECT usr_code FROM TQC_USERS WHERE usr_username = vusername);
  
    RETURN cmenusrefcur;
  END menuitems_query;

  FUNCTION menuitems_query2(vsyscode IN NUMBER, vusername IN VARCHAR2)
    RETURN menurefcur2 IS
    cmenusrefcur menurefcur2;
  BEGIN
    OPEN cmenusrefcur FOR
      SELECT menu_name,
             menu_type,
             menu_menu_code,
             MENU_ORG_TYPE_SHOW,
             MENU_BRK_LABEL
        FROM TQC_MENU,
             TQC_SYSTEM_ROLE_MENU,
             TQC_SYSTEM_ROLES,
             TQC_USER_SYSTEM_ROLES
       WHERE menu_code = srm_menu_code
         AND srl_code = srm_srl_code
         AND srl_code = usrl_srl_code
         AND srl_sys_code = vsyscode
         AND usrl_revoke_date IS NULL
         AND usrl_usr_code =
             (SELECT usr_code FROM TQC_USERS WHERE usr_username = vusername)
       ORDER BY menu_code;
  
    RETURN cmenusrefcur;
  END menuitems_query2;

  FUNCTION getdrlimit(v_user_code NUMBER,
                      v_sys_code  NUMBER,
                      v_auth_area VARCHAR2) RETURN NUMBER IS
    v_dr_limit NUMBER := 0;
  BEGIN
   --RETURN 9999999999;
  
    IF ((v_user_code IS NULL) OR (v_sys_code IS NULL) OR
       (v_auth_area IS NULL)) THEN
      v_dr_limit := 0;
    ELSE
      SELECT NVL(MAX(sar_debit_limit), 0) sar_debit_limit
        INTO v_dr_limit
        FROM TQC_SYSTEM_AUTH_AREAS,
             TQC_SYSTEM_AUTH_ROLES,
             TQC_USER_SYSTEM_AUTH_ROLES,
             TQC_USERS
       WHERE saa_code = sar_saa_code
         AND usar_usr_code = usr_code
         AND usar_sar_code = sar_code
         AND saa_sys_code = v_sys_code
         AND usar_revoke_date IS NULL
         AND saa_sht_desc = v_auth_area
         AND usar_usr_code = v_user_code
       ORDER BY 1;
    END IF;
  
    RETURN v_dr_limit;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'ERROR GETTING DEBIT LIMIT. V_SYS_CODE' ||
                              v_sys_code || ' V_AUTH_AREA' || v_auth_area ||
                              ' V_USER_CODE' || v_user_code ||
                              SQLERRM(SQLCODE));
      RETURN 0;
  END getdrlimit;

  FUNCTION accounttypeid(v_act_code NUMBER DEFAULT NULL,
                         vtype_id   CHAR DEFAULT 'T') RETURN VARCHAR2 IS
    v_type    VARCHAR2(50);
    v_type_id VARCHAR2(5);
  BEGIN
    IF v_act_code IS NULL THEN
      RETURN NULL;
    ELSE
      SELECT act_account_type, act_type_id
        INTO v_type, v_type_id
        FROM TQC_ACCOUNT_TYPES
       WHERE act_code = v_act_code;
    END IF;
  
    IF vtype_id = 'I' THEN
      RETURN v_type_id;
    ELSE
      RETURN v_type;
    END IF;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001, 'ERROR RETRIEVING ACCOUNT TYPE..');
      RETURN NULL;
  END accounttypeid;

  FUNCTION accountcode(v_act_type_id VARCHAR) RETURN NUMBER IS
    v_ret_val NUMBER;
  BEGIN
    SELECT act_code
      INTO v_ret_val
      FROM TQC_ACCOUNT_TYPES
     WHERE act_type_id = v_act_type_id;
  
    RETURN v_ret_val;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'RETRIEVING ACCOUNT TYPE ID..' ||
                              SQLERRM(SQLCODE));
      RETURN NULL;
  END accountcode;

  FUNCTION agency_query(v_search_val VARCHAR2)
    RETURN fms_interfaces_pkg.type_client_types_ref_cursor IS
    v_data fms_interfaces_pkg.type_client_types_ref_cursor;
  BEGIN
    OPEN v_data FOR
      SELECT agn_code code, agn_name NAME, act_type_id
        FROM TQC_AGENCIES, TQC_ACCOUNT_TYPES
       WHERE agn_act_code = act_code
         AND agn_name LIKE '%' || v_search_val || '%';
  
    RETURN v_data;
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001, '..ERROR RETRIEVING AGENT NAME ..');
  END agency_query;

  FUNCTION agencyname(v_act_code NUMBER) RETURN VARCHAR2 IS
    v_rtval VARCHAR2(200) DEFAULT NULL;
  BEGIN
    SELECT TQC_AGENCIES.agn_name
      INTO v_rtval
      FROM TQC_AGENCIES
     WHERE TQC_AGENCIES.agn_code = v_act_code;
  
    RETURN v_rtval;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001, '..ERROR RETRIEVING AGENT NAME ..');
      RETURN NULL;
  END agencyname;
  FUNCTION get_org_type(v_sys_code NUMBER) RETURN VARCHAR2 IS
    v_rtval VARCHAR2(200) DEFAULT NULL;
  BEGIN
    --DBMS_OUTPUT.PUT_LINE(sys_code);
    SELECT ORG_TYPE
      INTO v_rtval
      FROM TQC_ORGANIZATIONS, TQC_SYSTEMS
     WHERE SYS_ORG_CODE = ORG_CODE
       AND SYS_CODE = v_sys_code;
  
    --v_rtval := 'DFD';
    RETURN v_rtval;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              '..ERROR RETRIEVING ORGANIZATION TYPE ..' ||
                              v_sys_code || SQLERRM);
      RETURN NULL;
  END get_org_type;

  FUNCTION get_org_type(v_sys_name VARCHAR2) RETURN VARCHAR2 IS
    v_rtval VARCHAR2(200) DEFAULT NULL;
  BEGIN
    SELECT ORG_TYPE
      INTO v_rtval
      FROM TQC_ORGANIZATIONS, TQC_SYSTEMS
     WHERE SYS_ORG_CODE = ORG_CODE
       AND SYS_SHT_DESC = v_sys_name;
  
    RETURN v_rtval;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              '..ERROR RETRIEVING ORGANIZATION TYPE ..');
      RETURN NULL;
  END get_org_type;
  FUNCTION username(v_usr_code NUMBER, v_full VARCHAR2 DEFAULT 'N')
    RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    IF v_usr_code IS NULL THEN
      v_ret_val := NULL;
    ELSE
      SELECT DECODE(v_full, 'N', usr_username, 'Y', usr_name)
        INTO v_ret_val
        FROM TQC_USERS
       WHERE usr_code = v_usr_code;
    END IF;
  
    RETURN v_ret_val;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'ERROR GETTING USERNAME FOR USER CODE ' ||
                              TO_CHAR(v_usr_code) || '.' ||
                              SQLERRM(SQLCODE));
      RETURN NULL;
  END;

  ------------------returns logged on usercode if the username is not provided.------------------
  FUNCTION usercode(v_usr_name VARCHAR2) RETURN NUMBER IS
    v_usr_code NUMBER := TO_NUMBER(NULL);
  BEGIN
    IF v_usr_name IS NOT NULL THEN
      SELECT usr_code
        INTO v_usr_code
        FROM TQC_USERS
       WHERE usr_username = v_usr_name;
    END IF;
  
    RETURN v_usr_code;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN v_usr_code;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'ERROR GETTING USERCODE FOR USER NAME ' ||
                              v_usr_name || '.');
      RETURN NULL;
  END;
  FUNCTION orgcurrency(v_sys_code IN NUMBER, v_cur_symbol OUT VARCHAR2)
    RETURN NUMBER IS
    v_return NUMBER;
  BEGIN
    SELECT ORG_CUR_CODE, CUR_SYMBOL
      INTO v_return, v_cur_symbol
      FROM TQC_ORGANIZATIONS, TQC_SYSTEMS, TQC_CURRENCIES
     WHERE ORG_CODE = SYS_ORG_CODE
       AND CUR_CODE = ORG_CUR_CODE
       AND SYS_CODE = v_sys_code;
  
    RETURN v_return;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      NULL;
    
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'ERROR GETTING DEFAULT CURRENCY  ' ||
                              v_sys_code || '.');
  END;
 FUNCTION orgcurrencyDefault(v_cur_symbol OUT VARCHAR2)
    RETURN VARCHAR2 IS
  BEGIN
  SELECT   CUR_SYMBOL
      INTO v_cur_symbol
      FROM TQC_ORGANIZATIONS, TQC_SYSTEMS, TQC_CURRENCIES
     WHERE ORG_CODE = SYS_ORG_CODE
       AND CUR_CODE = ORG_CUR_CODE
       AND SYS_CODE = 37;
  RETURN v_cur_symbol;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      NULL;
    
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'ERROR GETTING DEFAULT CURRENCY  ' ||
                                '.');
  END;
  FUNCTION currencyname(v_curr_code NUMBER, v_full VARCHAR2 DEFAULT 'S')
    RETURN VARCHAR2 IS
    v_trn_cur_symbol      VARCHAR2(15);
    v_trn_cur_name        VARCHAR2(20);
    v_trn_cur_symbol_name VARCHAR2(36);
  BEGIN
    IF v_curr_code IS NULL THEN
      RETURN NULL;
    ELSIF v_full = 'S' THEN
      SELECT TQC_CURRENCIES.cur_symbol
        INTO v_trn_cur_symbol
        FROM TQC_CURRENCIES
       WHERE TQC_CURRENCIES.cur_code = v_curr_code;
    
      RETURN v_trn_cur_symbol;
    ELSIF v_full = 'L' THEN
      SELECT TQC_CURRENCIES.cur_desc
        INTO v_trn_cur_name
        FROM TQC_CURRENCIES
       WHERE TQC_CURRENCIES.cur_code = v_curr_code;
    
      RETURN v_trn_cur_name;
    ELSE
      SELECT TQC_CURRENCIES.cur_desc || '-' || TQC_CURRENCIES.cur_symbol
        INTO v_trn_cur_symbol_name
        FROM TQC_CURRENCIES
       WHERE TQC_CURRENCIES.cur_code = v_curr_code;
    
      RETURN v_trn_cur_symbol_name;
    END IF;
  END currencyname;

  FUNCTION currencyrate(v_fcur NUMBER, v_bcur NUMBER, v_date DATE)
    RETURN NUMBER IS
    v_cur_excs NUMBER;
    rtnval     NUMBER := NULL;
  BEGIN
    IF v_fcur IS NULL OR v_bcur IS NULL THEN
      RETURN NULL;
    ELSIF v_fcur = v_bcur THEN
      RETURN 1;
    ELSIF v_date IS NULL THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'GETTING CURRENCY RATE: THE CURRENCY EXCHANGE RATE CANNOT BE DETERMINED FOR ' ||
                              currencyname(v_fcur) || ' AGAINST ' ||
                              currencyname(v_bcur) ||
                              '. NO DATE WAS SUPPLIED.');
      RETURN NULL;
    ELSE
    
      SELECT COUNT('X')
        INTO v_cur_excs
        FROM TQC_CURRENCY_RATES
       WHERE crt_cur_code = v_fcur
         AND crt_date = TO_DATE(v_date, 'DD/MM/RRRR');
    
      IF v_cur_excs <> 0 THEN
      
        SELECT crt_rate
          INTO rtnval
          FROM TQC_CURRENCY_RATES
         WHERE crt_cur_code = v_fcur
           AND crt_date = TO_DATE(v_date, 'DD/MM/RRRR');
      
        RETURN rtnval;
      ELSE
        RAISE_APPLICATION_ERROR(-20001,
                                'THERE IS NO EXCHANGE RATE OF ' ||
                                currencyname(v_fcur) || ' AGAINST ' ||
                                currencyname(v_bcur) ||
                                ' IN THE SYSTEM FOR ' ||
                                TO_CHAR(v_date, 'DD/MM/YYYY') ||
                                '. PLEASE DEFINE THE RATE IN THE CORE SYSTEM ?');
        RETURN NULL;
      END IF;
    END IF;
  END currencyrate;

  FUNCTION branchname(v_brn_code NUMBER, v_full VARCHAR2 DEFAULT 'Y')
    RETURN VARCHAR2 IS
    v_rtval VARCHAR2(50);
  BEGIN
    IF v_brn_code IS NULL THEN
      RETURN NULL;
    ELSE
      IF v_full = 'Y' THEN
        SELECT brn_name
          INTO v_rtval
          FROM TQC_BRANCHES
         WHERE brn_code = v_brn_code;
      ELSIF v_full = 'S' THEN
        SELECT brn_sht_desc
          INTO v_rtval
          FROM TQC_BRANCHES
         WHERE brn_code = v_brn_code;
      ELSE
        SELECT brn_sht_desc || '-' || brn_name
          INTO v_rtval
          FROM TQC_BRANCHES
         WHERE brn_code = v_brn_code;
      END IF;
    
      RETURN v_rtval;
    END IF;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001, '..ERROR RETRIEVING BRANCH NAME..');
  END branchname;

  FUNCTION branchname(v_org_code NUMBER,
                      v_bns_code NUMBER,
                      v_full     VARCHAR2 DEFAULT 'Y') RETURN VARCHAR2 IS
    v_rtval VARCHAR2(50);
  BEGIN
    IF v_org_code IS NULL THEN
      IF v_bns_code IS NULL THEN
        RETURN NULL;
      ELSE
        IF v_full = 'Y' THEN
          SELECT bns_name
            INTO v_rtval
            FROM TQC_BRANCH_NAMES
           WHERE bns_code = v_bns_code;
        ELSIF v_full = 'S' THEN
          SELECT bns_sht_desc
            INTO v_rtval
            FROM TQC_BRANCH_NAMES
           WHERE bns_code = v_bns_code;
        ELSE
          SELECT bns_sht_desc || '-' || bns_name
            INTO v_rtval
            FROM TQC_BRANCH_NAMES
           WHERE bns_code = v_bns_code;
        END IF;
      
        RETURN v_rtval;
      END IF;
    ELSE
      RETURN branchname(branchcode(v_org_code, v_bns_code), v_full);
    END IF;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              '..ERROR RETRIEVING BRANCH NAME NAME..');
  END branchname;

  FUNCTION branchcode(v_org_code NUMBER, v_bns_code NUMBER) RETURN NUMBER IS
    v_rtval NUMBER;
  BEGIN
    SELECT TQC_BRANCHES.brn_code
      INTO v_rtval
      FROM TQC_REGIONS, TQC_BRANCHES
     WHERE ((TQC_REGIONS.reg_code = TQC_BRANCHES.brn_reg_code) AND
           (TQC_BRANCHES.brn_bns_code = v_bns_code) AND
           (TQC_REGIONS.reg_org_code = v_org_code));
  
    RETURN v_rtval;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              '..ERROR RETRIEVING BRANCH CODE..' ||
                              v_bns_code || ';' || v_org_code);
  END branchcode;

  FUNCTION branchcode(v_brh_code NUMBER) RETURN NUMBER IS
    v_bns_code NUMBER;
  BEGIN
    SELECT TQC_BRANCHES.brn_bns_code
      INTO v_bns_code
      FROM TQC_BRANCHES
     WHERE ((TQC_BRANCHES.brn_code = v_brh_code));
  
    RETURN v_bns_code;
  END;

  FUNCTION bankbranch(v_bbr_code NUMBER, v_full VARCHAR2 DEFAULT 'S')
    RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    IF v_bbr_code IS NULL THEN
      RETURN NULL;
    ELSIF v_full = 'S' THEN
      SELECT bbr_sht_desc
        INTO v_ret_val
        FROM TQC_BANK_BRANCHES
       WHERE TQC_BANK_BRANCHES.bbr_code = v_bbr_code;
    
      RETURN v_ret_val;
    ELSIF v_full = 'L' THEN
      SELECT bbr_branch_name
        INTO v_ret_val
        FROM TQC_BANK_BRANCHES
       WHERE TQC_BANK_BRANCHES.bbr_code = v_bbr_code;
    
      RETURN v_ret_val;
    END IF;
  END bankbranch;

  FUNCTION bank_ref(v_bbr_code NUMBER) RETURN VARCHAR2 IS
    v_ret_val VARCHAR2(100);
  BEGIN
    IF v_bbr_code IS NULL THEN
      RETURN NULL;
    ELSE
      SELECT bbr_ref_code
        INTO v_ret_val
        FROM TQC_BANK_BRANCHES
       WHERE TQC_BANK_BRANCHES.bbr_code = v_bbr_code;
    
      RETURN v_ret_val;
    END IF;
  END bank_ref;
  -------------------------GET_SYS_NAME-------------------------------------------

  FUNCTION systemname(v_sys_code NUMBER, v_full VARCHAR2 DEFAULT 'Y')
    RETURN VARCHAR2 IS
    v_rtval VARCHAR2(100);
  BEGIN
    IF v_sys_code IS NULL THEN
      RETURN NULL;
    ELSE
      IF v_full = 'Y' THEN
        SELECT TQC_SYSTEMS.sys_name
          INTO v_rtval
          FROM TQC_SYSTEMS
         WHERE TQC_SYSTEMS.sys_code = v_sys_code;
      ELSIF v_full = 'S' THEN
        SELECT TQC_SYSTEMS.sys_sht_desc
          INTO v_rtval
          FROM TQC_SYSTEMS
         WHERE TQC_SYSTEMS.sys_code = v_sys_code;
      ELSE
        SELECT TQC_SYSTEMS.sys_sht_desc || '-' || TQC_SYSTEMS.sys_name
          INTO v_rtval
          FROM TQC_SYSTEMS
         WHERE TQC_SYSTEMS.sys_code = v_sys_code;
      END IF;
    
      RETURN v_rtval;
    END IF;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001, '..ERROR RETRIEVING SYSTEM NAME..');
  END systemname;

  FUNCTION branchorganization(v_brh_code NUMBER) RETURN NUMBER IS
    v_org_code NUMBER;
  BEGIN
    SELECT TQC_REGIONS.reg_org_code
      INTO v_org_code
      FROM TQC_BRANCHES, TQC_REGIONS
     WHERE ((TQC_REGIONS.reg_code = TQC_BRANCHES.brn_reg_code) AND
           (TQC_BRANCHES.brn_code = v_brh_code));
  
    RETURN v_org_code;
  END;

  FUNCTION branchinorganization(v_org_code NUMBER, v_brh_code NUMBER)
    RETURN NUMBER IS
    v_bns_code NUMBER;
  BEGIN
    v_bns_code := branchcode(v_brh_code);
    RETURN branchcode(v_org_code, v_bns_code);
  END;

  FUNCTION clientname(v_clnt_code NUMBER) RETURN VARCHAR2 IS
    v_rtval VARCHAR2(200) DEFAULT NULL;
  BEGIN
    SELECT clnt_name
      INTO v_rtval
      FROM TQC_CLIENTS
     WHERE clnt_code = v_clnt_code;
  
    RETURN v_rtval;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001, '..ERROR RETRIEVING CLIENT NAME ..');
      RETURN NULL;
  END clientname;

  FUNCTION branchregion(v_brh_code NUMBER) RETURN NUMBER IS
    v_reg_code NUMBER;
  BEGIN
    SELECT TQC_REGIONS.reg_code
      INTO v_reg_code
      FROM TQC_BRANCHES, TQC_REGIONS
     WHERE ((TQC_REGIONS.reg_code = TQC_BRANCHES.brn_reg_code) AND
           (TQC_BRANCHES.brn_code = v_brh_code));
  
    RETURN v_reg_code;
  END;

  FUNCTION branchregion(v_org_code NUMBER, v_bns_code NUMBER) RETURN NUMBER IS
  BEGIN
    RETURN branchregion(branchcode(v_org_code, v_bns_code));
  END;

  FUNCTION currencysymbol(vcurcode NUMBER) RETURN VARCHAR2 IS
    vsymbol VARCHAR2(15);
  BEGIN
    SELECT TQC_CURRENCIES.cur_symbol --,    TQC_CURRENCIES.CUR_DESC
      INTO vsymbol
      FROM TQC_CURRENCIES
     WHERE ((TQC_CURRENCIES.cur_code = vcurcode));
  
    RETURN vsymbol;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END currencysymbol;

  FUNCTION systemorganization(vsyscode VARCHAR2) RETURN NUMBER IS
    vorg NUMBER;
  BEGIN
    SELECT TQC_SYSTEMS.sys_org_code
      INTO vorg
      FROM TQC_SYSTEMS
     WHERE ((TQC_SYSTEMS.sys_sht_desc = vsyscode));
  
    RETURN vorg;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END;

  FUNCTION organizationname(vorgcode NUMBER, vfull VARCHAR2 DEFAULT 'Y')
    RETURN VARCHAR2 IS
    vorg VARCHAR2(50);
  BEGIN
    SELECT DECODE(vfull,
                  'Y',
                  TQC_ORGANIZATIONS.org_name,
                  TQC_ORGANIZATIONS.org_sht_desc)
      INTO vorg
      FROM TQC_ORGANIZATIONS
     WHERE ((TQC_ORGANIZATIONS.org_code = vorgcode));
  
    RETURN vorg;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END;

  FUNCTION organizationname(vfull VARCHAR2 DEFAULT 'Y') RETURN VARCHAR2 IS
    vorg VARCHAR2(50);
  BEGIN
    SELECT DECODE(vfull, 'Y', NULL, NULL) INTO vorg FROM DUAL;
  
    RETURN vorg;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END;

  PROCEDURE costappropriation_query(corgcostrefcur IN OUT orgcostrefcur,
                                    vccscode       IN NUMBER,
                                    vexccode       IN NUMBER) IS
  BEGIN
    OPEN corgcostrefcur FOR
      SELECT org_code,
             vexccode,
             vccscode,
             org_name,
             fms_interfaces_pkg.org_cost_apportionment(org_code,
                                                       vccscode,
                                                       vexccode)
        FROM TQC_ORGANIZATIONS;
  END costappropriation_query;

  PROCEDURE systemsunionfms(systems_data_rec IN OUT type_systems_ref_cur,
                            v_org_code       IN NUMBER) IS
    v_fms_sys_code NUMBER(8);
    v_fms_sys_name VARCHAR2(100);
  BEGIN
    SELECT TQC_SYSTEMS.sys_code, TQC_SYSTEMS.sys_name
      INTO v_fms_sys_code, v_fms_sys_name
      FROM TQC_SYSTEMS
     WHERE ((TQC_SYSTEMS.sys_sht_desc = 'FMS'));
  
    OPEN systems_data_rec FOR
      SELECT sys_code, sys_name, sys_sht_desc, sys_org_code
        FROM TQC_SYSTEMS
       WHERE sys_org_code = v_org_code
      UNION
      SELECT v_fms_sys_code, v_fms_sys_name, 'FMS', v_org_code FROM DUAL;
  END;

  FUNCTION systemcode(v_sys_name IN VARCHAR2) RETURN NUMBER IS
    v_sys_code NUMBER(8);
  BEGIN
    SELECT TQC_SYSTEMS.sys_code
      INTO v_sys_code
      FROM TQC_SYSTEMS
     WHERE ((TQC_SYSTEMS.sys_sht_desc = v_sys_name));
  
    RETURN v_sys_code;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END;
  FUNCTION userinbranch(v_usr_name VARCHAR2, vbrncode NUMBER) RETURN VARCHAR2 IS
    vcnt NUMBER;
  BEGIN
    RETURN(userinbranch(usercode(v_usr_name), vbrncode));
  END;
  FUNCTION userinbranch(vusrcode NUMBER, vbrncode NUMBER) RETURN VARCHAR2 IS
    vcnt NUMBER;
  BEGIN
    SELECT COUNT('X')
      INTO vcnt
      FROM TQC_USER_BRANCHES
     WHERE ((TQC_USER_BRANCHES.usb_brn_code = vbrncode) AND
           (usb_usr_code = vusrcode));
  
    IF vcnt = 0 THEN
      RETURN 'FALSE';
    ELSE
      RETURN 'TRUE';
    END IF;
  END;

  PROCEDURE logaudit(vsessionid NUMBER) IS
  BEGIN
    UPDATE TQC_USER_LOGON
       SET usl_logout_dt = SYSDATE
     WHERE usl_session_code = vsessionid;
  END;

  FUNCTION userinorganization(vusrcode NUMBER, vorgcode NUMBER)
    RETURN VARCHAR2 IS
    vcnt NUMBER;
  BEGIN
    SELECT COUNT('X')
      INTO vcnt
      FROM TQC_USER_BRANCHES
     WHERE ((TQC_USER_BRANCHES.usb_usr_code = vusrcode) AND
           Tqc_Interfaces_Pkg.branchorganization(usb_brn_code) = vorgcode);
  
    IF vcnt = 0 THEN
      RETURN 'FALSE';
    ELSE
      RETURN 'TRUE';
    END IF;
  END;
  FUNCTION check_user_rights(v_usr_name      IN VARCHAR2,
                             v_sys_code      IN NUMBER,
                             v_process       IN VARCHAR2,
                             v_prcs_area     IN VARCHAR2,
                             v_prsc_sub_area IN VARCHAR2,
                             v_amount        IN NUMBER DEFAULT NULL,
                             v_dc            IN VARCHAR2 DEFAULT NULL)
    RETURN VARCHAR2 IS
  
    v_user_code  NUMBER;
    v_rights     BOOLEAN;
    v_rights_val VARCHAR2(50);
   
  BEGIN
--    RAISE_ERROR('v_process '||v_process||' v_prcs_area '||v_prcs_area||' v_prsc_sub_area '||v_prsc_sub_area);
    v_user_code := usercode(v_usr_name);
    v_rights    := check_user_rights2(v_user_code,
                                      v_sys_code,
                                      v_process,
                                      v_prcs_area,
                                      v_prsc_sub_area,
                                      v_amount,
                                      v_dc);
  
    IF v_rights = FALSE THEN
      v_rights_val := 'N';
    ELSE
      v_rights_val := 'Y';
 END IF;
  
    RETURN(v_rights_val);
  END check_user_rights;

  FUNCTION check_user_rights2(v_user_code     IN NUMBER,
                              v_sys_code      IN NUMBER,
                              v_process       IN VARCHAR2,
                              v_prcs_area     IN VARCHAR2,
                              v_prsc_sub_area IN VARCHAR2,
                              v_amount        IN NUMBER DEFAULT NULL,
                              v_dc            IN VARCHAR2 DEFAULT NULL)
    RETURN BOOLEAN IS
  
    v_cnt        NUMBER := 1;
    v_dr_limit   NUMBER;
    v_cr_limit   NUMBER;
    v_right_type VARCHAR2(15);
  BEGIN
  
   IF v_process = 'AMA' AND v_prcs_area = 'CRLMT' AND v_prsc_sub_area = 'CRDET'
    THEN
     RETURN(TRUE);
    END IF; 
     
     IF v_user_code = 195 THEN 
        RETURN(TRUE);
--        raise_error('sys='||v_sys_code||'v_process='||v_process||'v_prcs_area='||v_prcs_area||'v_prsc_sub_area='||v_prsc_sub_area||'v_amount='||v_amount||'v_dc='||v_dc);
    END IF;
 
    IF ((v_user_code IS NULL) OR (v_sys_code IS NULL) OR
       (v_process IS NULL) OR (v_prcs_area IS NULL) OR
       (v_prsc_sub_area IS NULL)) THEN
      RETURN(FALSE);
    END IF;
--  RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prsc_sub_area); 
--v_process GRPT v_prcs_area SPR_PYMNT_ v_prsc_sub_area RSPR_PYMNT_ 
    SELECT SPRSA_TYPE,
           COUNT(1),
           MAX(SRPSA_DEBIT_LIMIT),
           MAX(SRPSA_CREDIT_LIMIT)
      INTO v_right_type, v_cnt, v_dr_limit, v_cr_limit
      FROM TQC_SYS_USER_ROLES,
           TQC_SYS_ROLES_PROCESSES,
           TQC_SYS_PROCESSES,
           TQC_SYS_ROLES_PRCS_AREA,
           TQC_SYS_PROCESS_AREAS,
           TQC_SYS_ROLES_PRCS_S_AREA,
           TQC_SYS_PROCESS_SUB_AREAS,
           TQC_SYS_ROLES
     WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE
       AND SRPRC_CODE = SRPRA_SRPRC_CODE
       AND SRPRC_SPRC_CODE = SPRC_CODE
       AND SRPRA_CODE = SRPSA_SRPRA_CODE
       AND SRPRA_SPRCA_CODE = SPRCA_CODE
       AND SRPSA_SPRSA_CODE = SPRSA_CODE
       AND USRR_USR_CODE = v_user_code
       AND SPRC_SHT_DESC = v_process
       AND SPRCA_SHT_DESC = v_prcs_area
       AND SPRSA_SHT_DESC = v_prsc_sub_area
       AND SPRC_SYS_CODE = v_sys_code
       AND SRLS_CODE=USRR_SRLS_CODE
       AND SRLS_STATUS NOT IN('N','I')
       AND USRR_REVOKE_DATE IS NULL
      -- AND ROWNUM = 1
     GROUP BY SPRSA_TYPE;
  
    --RAISE_APPLICATION_ERROR(-20001, 'SA' ||v_amount ||'DC' || v_dc);
    --RAISE_APPLICATION_ERROR(-20001,v_right_type);
  
    IF v_right_type != 'A' THEN
      IF NVL(v_cnt, 0) = 0 THEN
        RETURN(FALSE);
      ELSE
        RETURN(TRUE);
      END IF;
    ELSE
      
      
      
      
      IF v_dc = 'C' THEN
        IF ABS(NVL(v_amount, 0)) > ABS(NVL(v_cr_limit, 0)) THEN
          RETURN(FALSE);
        ELSE
          RETURN(TRUE);
        END IF;
      ELSE
          
        IF ABS(NVL(v_amount, 0)) > ABS(NVL(v_dr_limit, 0)) THEN
          RETURN(FALSE);
        ELSE
          RETURN(TRUE);
        END IF;
      END IF;
    END IF;
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      --RAISE_APPLICATION_ERROR(-20001,'v_user_code=='||v_user_code||'v_process=='||v_process||'v_prcs_area=='||v_prcs_area||'v_prsc_sub_area =='||v_prsc_sub_area||'v_sys_code== '||v_sys_code);
      RETURN(FALSE);
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'UNABLE TO RETRIEVE AUTHORIZATION LIMITS ' ||
                              v_sys_code || ' PROCESS' || v_process ||
                              ' AREA' || v_prcs_area || ' SUB AREA' ||
                              v_prsc_sub_area || ' V_USER_CODE' ||
                              v_user_code || SQLERRM(SQLCODE));
  END check_user_rights2;

  FUNCTION check_userAuth_rights(v_usr_name      IN VARCHAR2,
                                 v_sys_code      IN NUMBER,
                                 v_process       IN VARCHAR2,
                                 v_prcs_area     IN VARCHAR2,
                                 v_prsc_sub_area IN VARCHAR2,
                                 v_transId       IN NUMBER) RETURN VARCHAR2 IS
    v_cnt        NUMBER := 1;
    v_dr_limit   NUMBER;
    v_cr_limit   NUMBER;
    v_right_type VARCHAR2(15);
    v_usrCode    NUMBER;
  BEGIN
    --RETURN(TRUE);
  
    IF ((v_sys_code IS NULL) OR (v_process IS NULL) OR
       (v_prcs_area IS NULL) OR (v_prsc_sub_area IS NULL) OR
       (v_transId IS NULL)) THEN
      RETURN('N');
    END IF;
    SELECT COUNT(1)
      INTO v_cnt
      FROM TQC_APPROVAL_DETAILS, TQC_SYS_PROCESS_SUB_AREAS
     WHERE APD_TRANS_ID = v_transId
       AND APD_SYS_CODE = v_sys_code
       AND APD_SPRSA_CODE = SPRSA_CODE
       AND SPRSA_SHT_DESC = v_prsc_sub_area;
  
    SELECT USR_CODE
      INTO v_usrCode
      FROM TQC_USERS
     WHERE USR_USERNAME = v_usr_name;
    BEGIN
      IF v_cnt = 0 THEN
        INSERT INTO TQC_APPROVAL_DETAILS
          (APD_ID,
           APD_SYS_CODE,
           APD_SPRSA_CODE,
           APD_DATE_CREATED,
           APD_DATE_APPROVED,
           APD_ACTION,
           APD_COMMENTS,
           APD_USRID,
           APD_LEVEL,
           APD_TRANS_ID,
           APD_DESCRIPTION)
          SELECT TQ_CRM.APD_ID_SEQ.NEXTVAL,
                 v_sys_code,
                 SPRSA_CODE,
                 SYSDATE,
                 NULL,
                 NULL,
                 NULL,
                 NULL,
                 TQAL_LEVEL_ID,
                 v_transId,
                 SRLS_NAME
            FROM TQC_SYS_PROCESS_SUB_AREAS,
                 TQC_SYS_PROCESS_AREAS,
                 TQC_SYS_PROCESSES,
                 TQC_AUTHORIZATION_LEVELS,
                 TQC_SYS_ROLES
           WHERE SPRSA_SPRCA_CODE = SPRCA_CODE
             AND SPRCA_SPRC_CODE = SPRC_CODE
             AND TQAL_SPRSA_CODE = SPRSA_CODE
             AND TQAL_SRLS_CODE = SRLS_CODE
             AND SPRC_SHT_DESC = v_process
             AND SPRCA_SHT_DESC = v_prcs_area
             AND SPRSA_SHT_DESC = v_prsc_sub_area;
      END IF;
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_ERROR('Error Inserting Records');
    END;
  
    SELECT COUNT(*)
      INTO v_cnt
      FROM TQC_APPROVAL_DETAILS,
           TQC_AUTHORIZATION_LEVELS,
           TQC_SYS_PROCESS_SUB_AREAS
     WHERE APD_SPRSA_CODE = TQAL_SPRSA_CODE
       AND APD_LEVEL = TQAL_LEVEL_ID
       AND APD_SPRSA_CODE = SPRSA_CODE
       AND APD_SYS_CODE = v_sys_code
       AND SPRSA_SHT_DESC = v_prsc_sub_area
       AND APD_DATE_APPROVED IS NULL;
  
    IF v_cnt = 0 THEN
      --RETURN('X');
      RETURN('Y');
    END IF;
  
    SELECT COUNT(*)
      INTO v_cnt
      FROM TQC_APPROVAL_DETAILS,
           TQC_AUTHORIZATION_LEVELS,
           TQC_SYS_USER_ROLES,
           TQC_SYS_PROCESS_SUB_AREAS
     WHERE APD_SPRSA_CODE = TQAL_SPRSA_CODE
       AND APD_LEVEL = TQAL_LEVEL_ID
       AND TQAL_SRLS_CODE = USRR_SRLS_CODE
       AND APD_SPRSA_CODE = SPRSA_CODE
       AND APD_SYS_CODE = v_sys_code
       AND SPRSA_SHT_DESC = v_prsc_sub_area
       AND USRR_USR_CODE = v_usrCode
       AND (USRR_REVOKE_DATE IS NULL OR USRR_REVOKE_DATE >= SYSDATE)
       AND APD_SYS_CODE = v_sys_code
       AND APD_DATE_APPROVED IS NULL;
  
    --RAISE_ERROR('Count Two '||v_cnt);
  
    IF v_cnt = 0 THEN
      --RETURN('N');
      RETURN('Y');
    ELSE
    
      RETURN('Y');
    END IF;
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN('N');
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'UNABLE TO RETRIEVE AUTHORIZATION LEVELS ' ||
                              v_sys_code || ' PROCESS' || v_process ||
                              ' AREA' || v_prcs_area || ' SUB AREA' ||
                              v_prsc_sub_area || SQLERRM(SQLCODE));
  END check_userAuth_rights;
  
  FUNCTION get_task_assignee(v_sys_code      IN NUMBER,
                             v_process       IN VARCHAR2,
                             v_prcs_area     IN VARCHAR2,
                             v_prsc_sub_area IN VARCHAR2,
                             v_amount        IN NUMBER DEFAULT NULL,
                             v_dc            IN VARCHAR2 DEFAULT NULL,
                             v_dflt_usr_code OUT NUMBER,
                             v_dflt_usrname  OUT VARCHAR2) RETURN usrs_ref IS
    v_refcur     usrs_ref;
    v_right_type VARCHAR2(15);
  BEGIN
 
    IF ((v_sys_code IS NULL) OR (v_process IS NULL) OR
       (v_prcs_area IS NULL) OR (v_prsc_sub_area IS NULL)) THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'Not all parameter provided to determine assignees..');
    END IF;
    --RAISE_APPLICATION_ERROR(-20001, 'SA' ||v_amount ||'DC' || v_dc);
    --RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prsc_sub_area||' SYS CODE '||v_sys_code||'dc '||v_dc||' Amount '||v_amount);
 
    BEGIN
      SELECT SPRSA_TYPE, SPRSA_DEFAULT_USR_CODE, USR_USERNAME
        INTO v_right_type, v_dflt_usr_code, v_dflt_usrname
        FROM TQC_SYS_PROCESSES,
             TQC_SYS_PROCESS_AREAS,
             TQC_SYS_PROCESS_SUB_AREAS,
             TQC_USERS
       WHERE SPRC_CODE = SPRCA_SPRC_CODE
         AND SPRCA_CODE = SPRSA_SPRCA_CODE
         AND SPRC_SHT_DESC = v_process
         AND SPRCA_SHT_DESC = v_prcs_area
         AND SPRSA_SHT_DESC = v_prsc_sub_area
         AND SPRC_SYS_CODE = v_sys_code
         AND SPRSA_DEFAULT_USR_CODE = USR_CODE(+);
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001,
                                'Error determining process type..Process ' ||
                                v_process || ' Process Area ' ||
                                v_prcs_area || ' Process Sub Area ' ||
                                v_prsc_sub_area);
    END;
    -- RAISE_APPLICATION_ERROR(-20001,v_dflt_usr_code);
    IF v_right_type != 'A' THEN
      BEGIN
        OPEN v_refcur FOR
          SELECT USR_CODE, USR_USERNAME, USR_NAME
            FROM TQC_USERS, TQC_USER_SYSTEMS
           WHERE USR_CODE = USYS_USR_CODE
             AND USYS_SYS_CODE = v_sys_code
             AND TRUNC(SYSDATE) BETWEEN TRUNC(USYS_WEF) AND
                 NVL(USYS_WET, TRUNC(SYSDATE))
             AND USR_CODE IN (SELECT USRR_USR_CODE
                                FROM TQC_SYS_USER_ROLES,
                                     TQC_SYS_ROLES_PROCESSES,
                                     TQC_SYS_PROCESSES,
                                     TQC_SYS_ROLES_PRCS_AREA,
                                     TQC_SYS_PROCESS_AREAS,
                                     TQC_SYS_ROLES_PRCS_S_AREA,
                                     TQC_SYS_PROCESS_SUB_AREAS
                               WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE
                                 AND SRPRC_CODE = SRPRA_SRPRC_CODE
                                 AND SRPRC_SPRC_CODE = SPRC_CODE
                                 AND SRPRA_CODE = SRPSA_SRPRA_CODE
                                 AND SRPRA_SPRCA_CODE = SPRCA_CODE
                                 AND SRPSA_SPRSA_CODE = SPRSA_CODE
                                 AND SPRC_SHT_DESC = v_process
                                 AND SPRCA_SHT_DESC = v_prcs_area
                                 AND SPRSA_SHT_DESC = v_prsc_sub_area
                                 AND SPRC_SYS_CODE = v_sys_code
                                 AND USRR_REVOKE_DATE IS NULL);
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_APPLICATION_ERROR(-20001, 'Error getting task assignees..');
      END;
    ELSE
      BEGIN
        --  RAISE_APPLICATION_ERROR (-20001,'Error determining process type..Process '||v_process||' Process Area '||v_prcs_area||' Process Sub Area '||v_prsc_sub_area||' amount '||v_amount);
     
        OPEN v_refcur FOR
          SELECT USR_CODE, USR_USERNAME, USR_NAME
            FROM TQC_USERS, TQC_USER_SYSTEMS
           WHERE USR_CODE = USYS_USR_CODE
             AND USYS_SYS_CODE = v_sys_code
             AND TRUNC(SYSDATE) BETWEEN TRUNC(USYS_WEF) AND
                 NVL(USYS_WET, TRUNC(SYSDATE))
             AND USR_CODE IN (SELECT USRR_USR_CODE
                                FROM TQC_SYS_USER_ROLES,
                                     TQC_SYS_ROLES_PROCESSES,
                                     TQC_SYS_PROCESSES,
                                     TQC_SYS_ROLES_PRCS_AREA,
                                     TQC_SYS_PROCESS_AREAS,
                                     TQC_SYS_ROLES_PRCS_S_AREA,
                                     TQC_SYS_PROCESS_SUB_AREAS
                               WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE
                                 AND SRPRC_CODE = SRPRA_SRPRC_CODE
                                 AND SRPRC_SPRC_CODE = SPRC_CODE
                                 AND SRPRA_CODE = SRPSA_SRPRA_CODE
                                 AND SRPRA_SPRCA_CODE = SPRCA_CODE
                                 AND SRPSA_SPRSA_CODE = SPRSA_CODE
                                 AND SPRC_SHT_DESC = v_process
                                 AND SPRCA_SHT_DESC = v_prcs_area
                                 AND SPRSA_SHT_DESC = v_prsc_sub_area
                                 AND SPRC_SYS_CODE = v_sys_code
                                 AND v_amount <=
                                     ABS(DECODE(v_dc,
                                                'C',
                                                SRPSA_CREDIT_LIMIT,
                                                SRPSA_DEBIT_LIMIT))
                                 AND USRR_REVOKE_DATE IS NULL);
        --            EXCEPTION
        --                WHEN OTHERS THEN
        --                    RAISE_APPLICATION_ERROR (-20001,'Error getting task assignees..');
      END;
    END IF;
    RETURN(v_refcur);
  END;
  FUNCTION get_task_assignee280814(v_sys_code      IN NUMBER,
                             v_process       IN VARCHAR2,
                             v_prcs_area     IN VARCHAR2,
                             v_prsc_sub_area IN VARCHAR2,
                             v_amount        IN NUMBER DEFAULT NULL,
                             v_dc            IN VARCHAR2 DEFAULT NULL,
                             v_dflt_usr_code OUT NUMBER,
                             v_dflt_usrname  OUT VARCHAR2) RETURN usrs_ref IS
    v_refcur     usrs_ref;
    v_right_type VARCHAR2(15);
  BEGIN
  
    IF ((v_sys_code IS NULL) OR (v_process IS NULL) OR
       (v_prcs_area IS NULL) OR (v_prsc_sub_area IS NULL)) THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'Not all parameter provided to determine assignees..');
    END IF;
    --RAISE_APPLICATION_ERROR(-20001, 'SA' ||v_amount ||'DC' || v_dc);
    --RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prsc_sub_area||' SYS CODE '||v_sys_code||'dc '||v_dc||' Amount '||v_amount);
  
    BEGIN
      SELECT SPRSA_TYPE, SPRSA_DEFAULT_USR_CODE, USR_USERNAME
        INTO v_right_type, v_dflt_usr_code, v_dflt_usrname
        FROM TQC_SYS_PROCESSES,
             TQC_SYS_PROCESS_AREAS,
             TQC_SYS_PROCESS_SUB_AREAS,
             TQC_USERS
       WHERE SPRC_CODE = SPRCA_SPRC_CODE
         AND SPRCA_CODE = SPRSA_SPRCA_CODE
         AND SPRC_SHT_DESC = v_process
         AND SPRCA_SHT_DESC = v_prcs_area
         AND SPRSA_SHT_DESC = v_prsc_sub_area
         AND SPRC_SYS_CODE = v_sys_code
         AND SPRSA_DEFAULT_USR_CODE = USR_CODE(+);
    EXCEPTION
      WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001,
                                'Error determining process type..Process ' ||
                                v_process || ' Process Area ' ||
                                v_prcs_area || ' Process Sub Area ' ||
                                v_prsc_sub_area);
    END;
    -- RAISE_APPLICATION_ERROR(-20001,v_dflt_usr_code);
    IF v_right_type != 'A' THEN
      BEGIN
        OPEN v_refcur FOR
          SELECT USR_CODE, USR_USERNAME, USR_NAME
            FROM TQC_USERS, TQC_USER_SYSTEMS
           WHERE USR_CODE = USYS_USR_CODE
             AND USYS_SYS_CODE = v_sys_code
             AND TRUNC(SYSDATE) BETWEEN TRUNC(USYS_WEF) AND
                 NVL(USYS_WET, TRUNC(SYSDATE))
             AND USR_CODE IN (SELECT USRR_USR_CODE
                                FROM TQC_SYS_USER_ROLES,
                                     TQC_SYS_ROLES_PROCESSES,
                                     TQC_SYS_PROCESSES,
                                     TQC_SYS_ROLES_PRCS_AREA,
                                     TQC_SYS_PROCESS_AREAS,
                                     TQC_SYS_ROLES_PRCS_S_AREA,
                                     TQC_SYS_PROCESS_SUB_AREAS
                               WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE
                                 AND SRPRC_CODE = SRPRA_SRPRC_CODE
                                 AND SRPRC_SPRC_CODE = SPRC_CODE
                                 AND SRPRA_CODE = SRPSA_SRPRA_CODE
                                 AND SRPRA_SPRCA_CODE = SPRCA_CODE
                                 AND SRPSA_SPRSA_CODE = SPRSA_CODE
                                 --AND SPRC_SHT_DESC = v_process
                                 --AND SPRCA_SHT_DESC = v_prcs_area
                                 --AND SPRSA_SHT_DESC = v_prsc_sub_area
                                 AND SPRC_SYS_CODE = v_sys_code
                                 AND USRR_REVOKE_DATE IS NULL);
      EXCEPTION
        WHEN OTHERS THEN
          RAISE_APPLICATION_ERROR(-20001, 'Error getting task assignees..');
      END;
    ELSE
      BEGIN
        --  RAISE_APPLICATION_ERROR (-20001,'Error determining process type..Process '||v_process||' Process Area '||v_prcs_area||' Process Sub Area '||v_prsc_sub_area||' amount '||v_amount);
      
        OPEN v_refcur FOR
          SELECT USR_CODE, USR_USERNAME, USR_NAME
            FROM TQC_USERS, TQC_USER_SYSTEMS
           WHERE USR_CODE = USYS_USR_CODE
             AND USYS_SYS_CODE = v_sys_code
             AND TRUNC(SYSDATE) BETWEEN TRUNC(USYS_WEF) AND
                 NVL(USYS_WET, TRUNC(SYSDATE))
             AND USR_CODE IN (SELECT USRR_USR_CODE
                                FROM TQC_SYS_USER_ROLES,
                                     TQC_SYS_ROLES_PROCESSES,
                                     TQC_SYS_PROCESSES,
                                     TQC_SYS_ROLES_PRCS_AREA,
                                     TQC_SYS_PROCESS_AREAS,
                                     TQC_SYS_ROLES_PRCS_S_AREA,
                                     TQC_SYS_PROCESS_SUB_AREAS
                               WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE
                                 AND SRPRC_CODE = SRPRA_SRPRC_CODE
                                 AND SRPRC_SPRC_CODE = SPRC_CODE
                                 AND SRPRA_CODE = SRPSA_SRPRA_CODE
                                 AND SRPRA_SPRCA_CODE = SPRCA_CODE
                                 AND SRPSA_SPRSA_CODE = SPRSA_CODE
                                 --AND SPRC_SHT_DESC = v_process
                                 --AND SPRCA_SHT_DESC = v_prcs_area
                                 --AND SPRSA_SHT_DESC = v_prsc_sub_area
                                 AND SPRC_SYS_CODE = v_sys_code
                                 AND v_amount <=
                                     ABS(DECODE(v_dc,
                                                'C',
                                                SRPSA_CREDIT_LIMIT,
                                                SRPSA_DEBIT_LIMIT))
                                 AND USRR_REVOKE_DATE IS NULL);
        --            EXCEPTION 
        --                WHEN OTHERS THEN 
        --                    RAISE_APPLICATION_ERROR (-20001,'Error getting task assignees..');
      END;
    END IF;
    RETURN(v_refcur);
  END;
   FUNCTION check_authorization_roles (v_usr_name    IN   VARCHAR2,
                                          v_sys_code    IN   NUMBER,
                                          v_auth_area   IN   VARCHAR2,
                                          v_amount      IN   NUMBER,
                                          v_dc          IN   VARCHAR2,
                                          v_pro_code    IN   NUMBER ) RETURN BOOLEAN IS
                                      
      v_cnt            NUMBER       := 1;
      v_user_code      NUMBER;
      v_dr_limit       NUMBER;
      v_cr_limit       NUMBER;
      v_saa_sht_desc   VARCHAR2 (15);
      v_param    VARCHAR2 (1);
   BEGIN
  -- RAISE_ERROR(v_dc);
--RETURN TRUE;
      v_user_code := usercode (v_usr_name);
   --  RAISE_ERROR('v_user_code'||v_user_code||'v_usr_name'||v_usr_name);
--RAISE_APPLICATION_ERROR(-20001,'v_dc1111111 '||v_sys_code||'--'||v_auth_area||' v_user_code '||v_user_code||' v_dc== '||v_dc);

      IF (   (v_user_code IS NULL)
          OR (v_sys_code IS NULL)
          OR (v_auth_area IS NULL)
         )
      THEN
      
         RETURN (FALSE);
      ELSIF v_dc ='E' THEN
    -- RAISE_ERROR('TEST');
          SELECT COUNT(1)
             INTO v_cnt 
                  FROM TQC_SYS_USER_ROLES,
                       TQC_SYS_ROLES_PROCESSES,
                       TQC_SYS_PROCESSES,
                       TQC_SYS_ROLES_PRCS_AREA,
                       TQC_SYS_PROCESS_AREAS,
                       TQC_SYS_ROLES_PRCS_S_AREA,
                       TQC_SYS_PROCESS_SUB_AREAS
                 WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE
                   AND SRPRC_CODE = SRPRA_SRPRC_CODE
                   AND SRPRC_SPRC_CODE = SPRC_CODE
                   AND SRPRA_CODE = SRPSA_SRPRA_CODE
                   AND SRPRA_SPRCA_CODE = SPRCA_CODE
                   AND SRPSA_SPRSA_CODE = SPRSA_CODE
                 AND USRR_USR_CODE = v_user_code
                   --AND SPRC_SHT_DESC = v_process
                   --AND SPRCA_SHT_DESC = v_prcs_area
                   AND SPRSA_SHT_DESC = v_auth_area
                   AND SPRC_SYS_CODE = v_sys_code
                   AND USRR_REVOKE_DATE IS NULL;
       --RAISE_APPLICATION_ERROR(-20001,'v_dc1111111 '||v_sys_code||'--'||v_auth_area||' v_user_code '||v_user_code);
--        SELECT   COUNT(1)
--             INTO v_cnt 
--             FROM TQC_SYSTEM_AUTH_AREAS,
--                  TQC_SYSTEM_AUTH_ROLES,
--                  TQC_USER_SYSTEM_AUTH_ROLES,
--                  TQC_USERS
--            WHERE saa_code = sar_saa_code
--              AND usar_usr_code = usr_code
--              AND usar_sar_code = sar_code
--              AND saa_sys_code = v_sys_code
--              AND usar_revoke_date IS NULL
--              AND saa_sht_desc = v_auth_area
--              AND usar_usr_code = v_user_code
--         GROUP BY saa_sht_desc;
--RAISE_ERROR('v_cnt'||v_cnt);
         IF NVL(v_cnt,0) = 0 THEN
            RETURN (FALSE);
         ELSE
            RETURN (TRUE);
         END IF;
     ELSE
         v_user_code := usercode (v_usr_name);
           v_param:=gin_parameters_pkg.get_param_varchar('ZERO_DEBITS_AUTH_ROLE');
      -- RAISE_APPLICATION_ERROR(-20001,'v_amount'||v_amount);
--RAISE_APPLICATION_ERROR(-20001,'V_AUTH_AREA = '||v_auth_area||'& V_USER_CODE = '||v_user_code||'& V_SYS_CODE = '||v_sys_code);
 --RAISE_ERROR('v_param'||v_param||'v_user_code'||v_user_code);
        IF NVL(v_param,'N')='Y'  THEN
    --- RAISE_ERROR('HAPA===='||v_user_code||'  =====  '||v_auth_area||'=======   '||v_sys_code);
        --RAISE_ERROR('v_user_code'||v_user_code||'v_auth_area '||v_auth_area);
                  SELECT  MAX (NVL(SRPSA_DEBIT_LIMIT,0)) SRPSA_DEBIT_LIMIT, MAX (NVL(SRPSA_CREDIT_LIMIT,0)) SRPSA_CREDIT_LIMIT
                 INTO v_dr_limit, v_cr_limit
                  FROM TQC_SYS_USER_ROLES,
                       TQC_SYS_ROLES_PROCESSES,
                       TQC_SYS_PROCESSES,
                       TQC_SYS_ROLES_PRCS_AREA,
                       TQC_SYS_PROCESS_AREAS,
                       TQC_SYS_ROLES_PRCS_S_AREA,
                       TQC_SYS_PROCESS_SUB_AREAS
                 WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE
                   AND SRPRC_CODE = SRPRA_SRPRC_CODE
                   AND SRPRC_SPRC_CODE = SPRC_CODE
                   AND SRPRA_CODE = SRPSA_SRPRA_CODE
                   AND SRPRA_SPRCA_CODE = SPRCA_CODE
                   AND SRPSA_SPRSA_CODE = SPRSA_CODE
                   AND USRR_USR_CODE = v_user_code
                   --AND SPRC_SHT_DESC = v_process
                   --AND SPRCA_SHT_DESC = v_prcs_area
                   AND SPRSA_SHT_DESC = v_auth_area
                   AND SPRC_SYS_CODE = v_sys_code
                   AND USRR_REVOKE_DATE IS NULL;
        ELSE
                 SELECT   MAX (sar_debit_limit) sar_debit_limit,
                          MAX (sar_credit_limit), saa_sht_desc
                     INTO v_dr_limit,
                          v_cr_limit, v_saa_sht_desc
                     FROM TQC_SYSTEM_AUTH_AREAS,
                          TQC_SYSTEM_AUTH_ROLES,
                          TQC_USER_SYSTEM_AUTH_ROLES,
                          TQC_USERS
                    WHERE saa_code = sar_saa_code
                      AND usar_usr_code = usr_code
                      AND usar_sar_code = sar_code
                      AND saa_sys_code = v_sys_code
                      AND usar_revoke_date IS NULL
                      AND saa_sht_desc = v_auth_area
                      AND usar_usr_code = v_user_code
                 GROUP BY saa_sht_desc;
         END IF;

         /*
            AND NVL (sar_pro_code, 0) =
                   DECODE (NVL (v_pro_code, 0),
                           0, NVL (sar_pro_code, 0),
                           NVL (v_pro_code, 0)
                          );
         */

  -- RAISE_APPLICATION_ERROR(-20001,'V_DR_LIMIT = '||v_auth_area  ||v_dr_limit||' V_CR_LIMIT = '||v_cr_limit||'V_SAA_SHT_DESC = '||v_saa_sht_desc||'v_auth_area='||v_auth_area);
         --IF v_dr_limit IS NULL AND v_cr_limit IS NULL
         --RAISE_ERROR('v_amount'||v_amount||'v_dr_limit'||v_dr_limit);
         IF (NVL (v_dr_limit, 0) = 0 AND NVL (v_cr_limit, 0) = 0) AND  v_dc != 'A'
         THEN
            --IF v_saa_sht_desc = v_auth_area
            --THEN
             --  RETURN (TRUE);
            --ELSE
               RETURN (FALSE);
               --RAISE_APPLICATION_ERROR(-20001,'ZERO CR AND DR v_amount'||v_amount||' v_dr_limit'||v_dr_limit);
           -- END IF;
         ELSIF v_dc = 'A'
         THEN
        -- RAISE_ERROR('v_dc 22222222  '||v_dc);
            RETURN (TRUE);
         ELSIF v_dc = 'C'
         THEN
            IF ABS (NVL (v_amount, 0)) > ABS (NVL (v_cr_limit, 0))
            THEN
               RETURN (FALSE);
            ELSE
               RETURN (TRUE);
            END IF;
         ELSE--IF v_dc='D' THEN 
       -- RAISE_APPLICATION_ERROR(-20001,v_dc||'==NOT SURE v_amount'||v_amount||' v_dr_limit'||v_dr_limit);
           IF ABS (NVL (v_amount, 0)) > ABS (NVL (v_dr_limit, 0))
            THEN
               RETURN (FALSE);
            ELSE
               RETURN (TRUE);
           END IF;
         END IF;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN (FALSE);
      WHEN OTHERS
      THEN
         RAISE_APPLICATION_ERROR
                               (-20001,
                                   'UNABLE TO RETRIEVE AUTHORIZATION LIMITS '
                                || v_sys_code
                                || ' V_AUTH_AREA'
                                || v_auth_area
                                || ' V_USER_CODE'
                                || v_user_code
                                || SQLERRM (SQLCODE)
                               );
   END check_authorization_roles;
  FUNCTION check_authorization_roles_BK(v_usr_name  IN VARCHAR2,
                                        v_sys_code  IN NUMBER,
                                        v_auth_area IN VARCHAR2,
                                        v_amount    IN NUMBER,
                                        v_dc        IN VARCHAR2,
                                        v_pro_code  IN NUMBER) RETURN BOOLEAN IS
    v_cnt       NUMBER := 1;
    v_user_code NUMBER;
    v_dr_limit  NUMBER;
    v_cr_limit  NUMBER;
  BEGIN
    v_user_code := usercode(v_usr_name);
    RETURN(TRUE);
    /*
    IF (   (v_user_code IS NULL)
        OR (v_sys_code IS NULL)
        OR (v_auth_area IS NULL)
       )
    THEN
       RETURN (FALSE);
    ELSE
       --v_user_code := usercode(v_usr_name);
       SELECT MAX (sar_debit_limit) sar_debit_limit, MAX (sar_credit_limit)
         INTO v_dr_limit, v_cr_limit
         FROM TQC_SYSTEM_AUTH_AREAS,
              TQC_SYSTEM_AUTH_ROLES,
              TQC_USER_SYSTEM_AUTH_ROLES,
              TQC_USERS
        WHERE saa_code = sar_saa_code
          AND usar_usr_code = usr_code
          AND usar_sar_code = sar_code
          AND saa_sys_code = v_sys_code
          AND usar_revoke_date IS NULL
          AND saa_sht_desc = v_auth_area
          AND usar_usr_code = v_user_code
          AND NVL (sar_pro_code, 0) =
                 DECODE (NVL (v_pro_code, 0),
                         0, NVL (sar_pro_code, 0),
                         NVL (v_pro_code, 0)
                        );
    
       IF v_dr_limit IS NULL AND v_cr_limit IS NULL
       THEN
          RETURN (FALSE);
       ELSIF v_dc = 'C'
       THEN
          IF ABS (NVL (v_amount, 0)) > ABS (NVL (v_cr_limit, 0))
          THEN
             RETURN (FALSE);
          ELSE
             RETURN (TRUE);
          END IF;
       ELSE
          --RAISE_APPLICATION_ERROR(-20001,'v_amount'||v_amount||' v_dr_limit'||v_dr_limit);
          IF ABS (NVL (v_amount, 0)) > ABS (NVL (v_dr_limit, 0))
          THEN
             RETURN (FALSE);
          ELSE
             RETURN (TRUE);
          END IF;
       END IF;
    END IF;
    */
  
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN(FALSE);
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'UNABLE TO RETRIEVE AUTHORIZATION LIMITS ' ||
                              v_sys_code || ' V_AUTH_AREA' || v_auth_area ||
                              ' V_USER_CODE' || v_user_code ||
                              SQLERRM(SQLCODE));
  END check_authorization_roles_BK;

  FUNCTION eft_enabled(v_bbr_code NUMBER) RETURN VARCHAR2 IS
    v_ref_code    VARCHAR2(20);
    v_eft_support VARCHAR2(1) := 'N';
    v_branch_name VARCHAR2(100);
  BEGIN
    IF v_bbr_code IS NOT NULL THEN
      BEGIN
        SELECT bbr_ref_code, bbr_eft_supported, bbr_branch_name
          INTO v_ref_code, v_eft_support, v_branch_name
          FROM TQC_BANK_BRANCHES
         WHERE bbr_code = v_bbr_code;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
        WHEN OTHERS THEN
          RAISE_APPLICATION_ERROR(-20001,
                                  'CHECKING EFT STATUS ' || SQLERRM);
      END;
    
      IF (v_ref_code IS NULL) AND (v_eft_support = 'Y') THEN
        RAISE_APPLICATION_ERROR(-20001,
                                v_branch_name || 'HAS NO BANK REF CODE.');
      END IF;
    END IF;
  
    RETURN v_eft_support;
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001, 'EFT_ENABLED:' || SQLERRM);
  END;

  PROCEDURE appendclob(v_clob IN OUT CLOB,
                       v_char VARCHAR2,
                       v_temp VARCHAR2 DEFAULT 'N') IS
    len BINARY_INTEGER;
  BEGIN
    IF v_temp = 'Y' THEN
      DBMS_LOB.createtemporary(v_clob, TRUE);
      DBMS_LOB.OPEN(v_clob, DBMS_LOB.lob_readwrite);
      len := LENGTH(v_char);
      DBMS_LOB.writeappend(v_clob, len, v_char);
      DBMS_LOB.CLOSE(v_clob);
    ELSE
      len := LENGTH(v_char);
      DBMS_LOB.writeappend(v_clob, len, v_char);
    END IF;
  END;

  PROCEDURE LOG(v_sys NUMBER, v_char VARCHAR2) IS
    PRAGMA AUTONOMOUS_TRANSACTION;
    v_clob   CLOB;
    v_date   DATE := SYSDATE;
    v_format VARCHAR2(20) := 'YYYYMONDD';
  BEGIN
    BEGIN
      SELECT log_text
        INTO v_clob
        FROM TQC_LOG
       WHERE ((log_sys = v_sys) AND
             (TO_CHAR(log_date, v_format) = TO_CHAR(v_date, v_format)))
         FOR UPDATE;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        NULL;
      
        INSERT INTO TQC_LOG
          (log_date, log_text, log_sys)
        VALUES
          (v_date, EMPTY_CLOB(), v_sys);
      
        SELECT log_text
          INTO v_clob
          FROM TQC_LOG
         WHERE ((log_sys = v_sys) AND
               (TO_CHAR(log_date, v_format) = TO_CHAR(v_date, v_format)))
           FOR UPDATE;
    END;
  
    appendclob(v_clob, v_char || CHR(10));
    COMMIT;
  EXCEPTION
    WHEN OTHERS THEN
      NULL;
      ROLLBACK;
  END;
  FUNCTION orgcurrency(v_sys_code IN NUMBER) RETURN NUMBER IS
    v_return NUMBER;
  BEGIN
    SELECT ORG_CUR_CODE
      INTO v_return
      FROM TQC_ORGANIZATIONS, TQC_SYSTEMS, TQC_CURRENCIES
     WHERE ORG_CODE = SYS_ORG_CODE
       AND CUR_CODE = ORG_CUR_CODE
       AND SYS_CODE = v_sys_code;
  
    RETURN v_return;
  EXCEPTION
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'ERROR GETTING DEFAULT CURRENCY  ' ||
                              v_sys_code || '.');
  END;
  FUNCTION check_excp_auth_rights(v_usr_code      NUMBER,
                                  v_sys_code      NUMBER,
                                  v_process       VARCHAR2,
                                  v_prcs_area     VARCHAR2,
                                  v_prcs_sub_area VARCHAR2) RETURN BOOLEAN IS
    v_right_type TQC_SYS_PROCESS_SUB_AREAS.SPRSA_TYPE%TYPE;
  BEGIN
    -- RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prcs_sub_area); 
    IF ((v_usr_code IS NULL) OR (v_sys_code IS NULL) OR (v_process IS NULL) OR
       (v_prcs_area IS NULL) OR (v_prcs_sub_area IS NULL)) THEN
      RETURN(FALSE);
    END IF;
    BEGIN
    
      SELECT DISTINCT SPRSA_TYPE
        INTO v_right_type
        FROM TQC_SYS_USER_ROLES,
             TQC_SYS_ROLES_PROCESSES,
             TQC_SYS_PROCESSES,
             TQC_SYS_ROLES_PRCS_AREA,
             TQC_SYS_PROCESS_AREAS,
             TQC_SYS_ROLES_PRCS_S_AREA,
             TQC_SYS_PROCESS_SUB_AREAS
       WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE
         AND SRPRC_CODE = SRPRA_SRPRC_CODE
         AND SRPRC_SPRC_CODE = SPRC_CODE
         AND SRPRA_CODE = SRPSA_SRPRA_CODE
         AND SRPRA_SPRCA_CODE = SPRCA_CODE
         AND SRPSA_SPRSA_CODE = SPRSA_CODE
         AND USRR_USR_CODE = v_usr_code
         AND SPRC_SHT_DESC = v_process
         AND SPRCA_SHT_DESC = v_prcs_area
         AND SPRSA_SHT_DESC = v_prcs_sub_area
         AND SPRC_SYS_CODE = v_sys_code
         AND USRR_REVOKE_DATE IS NULL;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        RETURN FALSE;
      WHEN OTHERS THEN
        RETURN FALSE;
    END;
    IF v_right_type IN ('C', 'A') THEN
      RETURN TRUE;
    END IF;
  END;

  PROCEDURE get_gin_treaty_accounts(v_cursor OUT gin_treaty_accounts_cursor)
  
   IS
  BEGIN
    OPEN v_cursor FOR
      SELECT ACC_NAME, ACC_NUMBER
        FROM FMS_ACCOUNTS
       WHERE ACC_ORG_CODE = TQC_INTERFACES_PKG.SYSTEMORGANIZATION('GIS')
         AND ACC_TRN = 'Y'
       ORDER BY ACC_NUMBER;
  END;

  FUNCTION orgPaymentMode(v_sys_code IN NUMBER, v_pay_symbol OUT VARCHAR2)
    RETURN NUMBER IS
    v_return NUMBER;
  BEGIN
    SELECT PMOD_CODE, PMOD_DESC
      INTO v_return, v_pay_symbol
      FROM TQC_PAYMENT_MODES
     WHERE PMOD_DEFAULT = 'Y';
    RETURN v_return;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      NULL;
    
    WHEN OTHERS THEN
      RAISE_APPLICATION_ERROR(-20001,
                              'ERROR GETTING DEFAULT PAYMENT MODE  ' ||
                              v_sys_code || '.');
  END;

  FUNCTION check_approval_right(v_user_code     NUMBER,
                                v_area_sht_desc VARCHAR2,
                                v_process       VARCHAR2,
                                v_prcs_area     VARCHAR2,
                                v_sys_code      NUMBER) RETURN VARCHAR2 IS
    v_count NUMBER;
  BEGIN
    --RAISE_ERROR('v_user_code#'||v_user_code||'v_area_sht_desc'||v_area_sht_desc);
    SELECT COUNT(*)
      INTO v_count
      FROM TQC_SYS_USER_ROLES,
           TQC_SYS_ROLES_PROCESSES,
           TQC_SYS_PROCESSES,
           TQC_SYS_ROLES_PRCS_AREA,
           TQC_SYS_PROCESS_AREAS,
           TQC_SYS_ROLES_PRCS_S_AREA,
           TQC_SYS_PROCESS_SUB_AREAS
     WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE
       AND SRPRC_CODE = SRPRA_SRPRC_CODE
       AND SRPRC_SPRC_CODE = SPRC_CODE
       AND SRPRA_CODE = SRPSA_SRPRA_CODE
       AND SRPRA_SPRCA_CODE = SPRCA_CODE
       AND SRPSA_SPRSA_CODE = SPRSA_CODE
       AND USRR_USR_CODE = v_user_code
       AND SPRC_SHT_DESC = v_process
       AND SPRCA_SHT_DESC = v_prcs_area
       AND SPRSA_SHT_DESC = v_area_sht_desc
       AND SPRC_SYS_CODE = v_sys_code
       AND USRR_REVOKE_DATE IS NULL;
  
    IF v_count > 0 THEN
      RETURN 'Y';
    ELSE
      RETURN 'N';
    END IF;
  EXCEPTION
    WHEN OTHERS THEN
      RETURN 'Y';
  END;
  function getCouSchengen(v_cou_name TQC_COUNTRIES.COU_NAME%TYPE)
    RETURN VARCHAR2 AS
    v_schengen varchar2(1) := 'N';
  BEGIN
    SELECT COU_SCHEGEN
      INTO v_schengen
      FROM TQC_COUNTRIES
     WHERE COU_NAME = v_cou_name;
  
    RETURN v_schengen;
  EXCEPTION
    WHEN OTHERS THEN
      RETURN 'N';
  END;

  FUNCTION getStateName(v_state_code IN NUMBER) RETURN VARCHAR2 IS
    v_stateName TQC_STATES.STS_NAME%TYPE;
  BEGIN
    SELECT STS_NAME
      INTO v_stateName
      FROM TQC_STATES
     WHERE STS_CODE = v_state_code;
    RETURN v_stateName;
  EXCEPTION
    WHEN OTHERS THEN
      RETURN NULL;
  END;
  
  FUNCTION check_user_limit (v_user_code    IN   NUMBER,
                              v_sys_code    IN   NUMBER,
                              v_process     IN VARCHAR2,
                              v_prcs_area   IN VARCHAR2,
                              v_prsc_sub_area   IN   VARCHAR2,
                              v_amount      IN   NUMBER DEFAULT NULL,
                              v_dc          IN   VARCHAR2 DEFAULT NULL) RETURN NUMBER IS
                                      
      v_cnt            NUMBER       := 1;
      v_dr_limit       NUMBER;
      v_cr_limit       NUMBER;
      v_right_type   VARCHAR2 (15);
   BEGIN

      IF (   (v_user_code IS NULL)
          OR (v_sys_code IS NULL)
          OR (v_process IS NULL)
          OR (v_prcs_area IS NULL)
          OR (v_prsc_sub_area IS NULL)
         ) THEN
         RETURN 0;
      END IF;
      
        --RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prsc_sub_area); 
        SELECT SPRSA_TYPE,COUNT(1),MAX(NVL(SRPSA_DEBIT_LIMIT,0)), MAX(SRPSA_CREDIT_LIMIT)
        INTO v_right_type, v_cnt,v_dr_limit,v_cr_limit
        FROM TQC_SYS_USER_ROLES,
        TQC_SYS_ROLES_PROCESSES,TQC_SYS_PROCESSES,
        TQC_SYS_ROLES_PRCS_AREA,TQC_SYS_PROCESS_AREAS,
        TQC_SYS_ROLES_PRCS_S_AREA,TQC_SYS_PROCESS_SUB_AREAS
        WHERE USRR_SRLS_CODE = SRPRC_SRLS_CODE 
        AND SRPRC_CODE = SRPRA_SRPRC_CODE AND SRPRC_SPRC_CODE = SPRC_CODE
        AND SRPRA_CODE = SRPSA_SRPRA_CODE AND SRPRA_SPRCA_CODE = SPRCA_CODE
        AND SRPSA_SPRSA_CODE = SPRSA_CODE
        AND USRR_USR_CODE = v_user_code
        AND SPRC_SHT_DESC = v_process
        AND SPRCA_SHT_DESC = v_prcs_area
        AND SPRSA_SHT_DESC= v_prsc_sub_area
        AND SPRC_SYS_CODE = v_sys_code
        AND USRR_REVOKE_DATE  IS NULL
        --AND ROWNUM=1
        GROUP BY SPRSA_TYPE;
        
        
        RETURN v_dr_limit;
        
     
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
        -- RAISE_APPLICATION_ERROR(-20001,'v_user_code=='||v_user_code||'v_process=='||v_process||'v_prcs_area=='||v_prcs_area||'v_prsc_sub_area =='||v_prsc_sub_area||'v_sys_code== '||v_sys_code);
     RETURN 0;
      WHEN OTHERS
      THEN
         RAISE_APPLICATION_ERROR
                               (-20001,
                                   'UNABLE TO RETRIEVE AUTHORIZATION LIMITS '
                                || v_sys_code
                                || ' PROCESS'
                                || v_process
                                || ' AREA'
                                || v_prcs_area
                                || ' SUB AREA'
                                || v_prsc_sub_area
                                || ' V_USER_CODE'
                                || v_user_code
                                || SQLERRM (SQLCODE)
                               );
   END check_user_limit;
    FUNCTION orgcurrencysymbol (v_sys_code IN NUMBER )
   RETURN VARCHAR2
   IS
   v_return VARCHAR2(100);
   BEGIN
           SELECT CUR_SYMBOL
        INTO v_return
        FROM TQC_ORGANIZATIONS,TQC_SYSTEMS, TQC_CURRENCIES
        WHERE ORG_CODE = SYS_ORG_CODE
        AND CUR_CODE = ORG_CUR_CODE
        AND SYS_CODE =  v_sys_code;

        RETURN v_return;
    EXCEPTION
        WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR (-20001,
                                     'ERROR GETTING DEFAULT CURRENCY  '
                                  || v_sys_code
                                  || '.'
                                 );
   END;

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
                                            ) IS
 CURSOR alrt IS SELECT *  FROM TQC_ALERT_TYPES
  WHERE alrt_code =v_alrt_code;
  v_sms_code NUMBER;
  v_email_code  NUMBER;
  v_send_alert_email VARCHAR2(1) DEFAULT 'N';
  v_files_tab tqc_email_pkg.email_files_tab;
 BEGIN
 /*- Creates registers the alert in tqc_alerts
    - Based on the alert type, it sends the relevant alert based on the template
  */
      BEGIN
        SELECT param_value
          INTO v_send_alert_email
          FROM tqc_parameters
         WHERE param_name = 'SEND_ALERT_EMAIL';--send_alert_email or use bring up
      EXCEPTION
        WHEN OTHERS THEN
          v_send_alert_email:='N';
      END;
  FOR a IN alrt LOOP
      BEGIN
       INSERT INTO TQC_ALERTS (
                         ALRTS_CODE, ALRTS_ALRT_CODE,  ALRTS_SYS_CODE,
                         ALRTS_DESCRIPTION,ALRTS_DATE, ALRTS_PERIOD,
                         ALRTS_USER_CODE, ALRTS_STATUS,ALRTS_SCREEN,
                         ALRTS_AGN_CODE, ALRTS_CLNT_CODE,ALRTS_PREPARED_BY,
                         ALRTS_PREPARE_DATE,ALRTS_QUOT_CODE,ALRTS_QUOT_NO,
                         ALRTS_POL_BATCH_NO, ALRTS_POL_POLICY_NO,ALRTS_CLAIM_NO,ALRTS_TO_SEND_DATE)
                      VALUES (
                          TQC_ALRT_CODE_SEQ.NEXTVAL,v_alrt_code,a.alrt_sys_code,
                          NVL(v_msg_subj,'You have an alert to attend to'),SYSDATE,1,
                          NVL(v_user_code,a.alrt_grp_usr_code),'A' ,a.alrt_screen,
                          v_agn_code,v_clnt_code,v_user,
                          SYSDATE,v_quot_code,v_quot_no,
                          v_pol_code,v_pol_no,v_clm_no,v_alrts_to_send_date);
       EXCEPTION
        WHEN OTHERS THEN
            RAISE_ERROR('Error registering alert...');
       END;
     -- RAISE_eRROR('alrt_sms='||a.alrt_sms||'='||a.alrt_email);
       IF  NVL(a.alrt_sms,'N') = 'Y' THEN
                    TQC_SMS_PKG.CREATE_SMS_MSG (v_reciepient,--v_reciepient   IN     VARCHAR2,
                                                  v_clnt_code,--v_clnt_code    IN     NUMBER,
                                                  v_agn_code,--v_agn_code     IN     NUMBER,
                                                  v_quot_code,--v_quot_code    IN     NUMBER,
                                                  v_quot_no,--v_quot_no      IN     VARCHAR2,
                                                  v_pol_code,--v_pol_code     IN     NUMBER,
                                                  v_pol_no,--v_pol_no       IN     VARCHAR2,
                                                  v_clm_no,--v_clm_no       IN     VARCHAR2,
                                                  a.alrt_sms_msgt_code,--v_msgt_code    IN     NUMBER,
                                                  a.alrt_sys_code,--v_sys_code     IN     NUMBER,
                                                  'A',--v_sys_mod      IN     VARCHAR2,
                                                  v_user,--v_user         IN     VARCHAR2,
                                                  v_sms_code,--v_sms_code     IN OUT NUMBER,
                                                  v_app_lvl,--v_app_lvl      IN     VARCHAR2
                                                  NVL(v_user_code,a.alrt_grp_usr_code)
                                                                         );
       ELSIF NVL(a.alrt_email,'N')= 'Y' THEN
                    Tqc_Email_Pkg.create_email_msg (v_reciepient,--v_reciepient   IN     VARCHAR2,
                                                   v_clnt_code,--v_clnt_code    IN     NUMBER,
                                                   v_agn_code,--v_agn_code     IN     NUMBER,
                                                   v_quot_code,--v_quot_code    IN     NUMBER,
                                                   v_quot_no,--v_quot_no      IN     VARCHAR2,
                                                   v_pol_code,--v_pol_code     IN     NUMBER,
                                                   v_pol_no,--v_pol_no       IN     VARCHAR2,
                                                   v_clm_no,--v_clm_no       IN     VARCHAR2,
                                                   NVL(v_msg_subj,'TURNQUEST SYSTEM ALERT'),--v_msg_subj     IN     VARCHAR2,
                                                   a.alrt_email_msgt_code,--v_msgt_code    IN     NUMBER,
                                                   v_files_tab,--v_files_tab    IN     email_files_tab,
                                                   a.alrt_sys_code,--v_sys_code     IN     NUMBER,
                                                   'A',--v_sys_mod      IN     VARCHAR2,
                                                   v_user,--v_user         IN     VARCHAR2,
                                                   v_email_code,--v_email_code   IN OUT NUMBER,
                                                   v_app_lvl,--v_app_lvl      IN     VARCHAR2
                                                   NVL(v_user_code,a.alrt_grp_usr_code)
                                                                           );
           IF NVL(v_send_alert_email,'N')='Y' THEN  
                Tqc_Email_Pkg.send_mail(v_email_code);
           END IF;
        END IF;
                             
   END LOOP;
  END;
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
                                            ) IS
 CURSOR alrt IS SELECT *  FROM TQC_ALERT_TYPES
  WHERE ALRT_CODE =v_alrt_code;
  v_sms_code NUMBER;
  v_email_code  NUMBER;
  v_files_tab tqc_email_pkg.email_files_tab;
 BEGIN
 /*- Creates registers the alert in tqc_alerts
    - Based on the alert type, it sends the relevant alert based on the template
  */

  FOR a IN alrt LOOP
      BEGIN
       INSERT INTO TQC_ALERTS (
                         ALRTS_CODE, ALRTS_ALRT_CODE,  ALRTS_SYS_CODE,
                         ALRTS_DESCRIPTION,ALRTS_DATE, ALRTS_PERIOD,
                         ALRTS_USER_CODE, ALRTS_STATUS,ALRTS_SCREEN,
                         ALRTS_AGN_CODE, ALRTS_CLNT_CODE,ALRTS_PREPARED_BY,
                         ALRTS_PREPARE_DATE,ALRTS_QUOT_CODE,ALRTS_QUOT_NO,
                         ALRTS_POL_BATCH_NO, ALRTS_POL_POLICY_NO,ALRTS_CLAIM_NO)
                      VALUES (
                          TQC_ALRT_CODE_SEQ.NEXTVAL,v_alrt_code,a.alrt_sys_code,
                          NVL(v_msg_subj,'You have an alert to attend to'),SYSDATE,1,
                          NVL(v_user_code,a.alrt_grp_usr_code),'A' ,a.alrt_screen,
                          v_agn_code,v_clnt_code,v_user,
                          SYSDATE,v_quot_code,v_quot_no,
                          v_pol_code,v_pol_no,v_clm_no);
       EXCEPTION
        WHEN OTHERS THEN
            RAISE_ERROR('Error registering alert...');
       END;
     -- RAISE_eRROR('alrt_sms='||a.alrt_sms||'='||a.alrt_email);
       IF  NVL(a.alrt_sms,'N') = 'Y' THEN
                    TQC_SMS_PKG.CREATE_SMS_MSG (v_reciepient,--v_reciepient   IN     VARCHAR2,
                                                                          v_clnt_code,--v_clnt_code    IN     NUMBER,
                                                                          v_agn_code,--v_agn_code     IN     NUMBER,
                                                                          v_quot_code,--v_quot_code    IN     NUMBER,
                                                                          v_quot_no,--v_quot_no      IN     VARCHAR2,
                                                                          v_pol_code,--v_pol_code     IN     NUMBER,
                                                                          v_pol_no,--v_pol_no       IN     VARCHAR2,
                                                                          v_clm_no,--v_clm_no       IN     VARCHAR2,
                                                                          a.alrt_sms_msgt_code,--v_msgt_code    IN     NUMBER,
                                                                          a.alrt_sys_code,--v_sys_code     IN     NUMBER,
                                                                          'A',--v_sys_mod      IN     VARCHAR2,
                                                                          v_user,--v_user         IN     VARCHAR2,
                                                                          v_sms_code,--v_sms_code     IN OUT NUMBER,
                                                                          v_app_lvl,--v_app_lvl      IN     VARCHAR2
                                                                          NVL(v_user_code,a.alrt_grp_usr_code)
                                                                         );
--        ELSIF NVL(a.alrt_email,'N')= 'Y' THEN
----                    Tqc_Email_Pkg.CREATE_EMAIL_MSG (v_reciepient,--v_reciepient   IN     VARCHAR2,
----                                                                           v_clnt_code,--v_clnt_code    IN     NUMBER,
----                                                                           v_agn_code,--v_agn_code     IN     NUMBER,
----                                                                           v_quot_code,--v_quot_code    IN     NUMBER,
----                                                                           v_quot_no,--v_quot_no      IN     VARCHAR2,
----                                                                           v_pol_code,--v_pol_code     IN     NUMBER,
----                                                                           v_pol_no,--v_pol_no       IN     VARCHAR2,
----                                                                           v_clm_no,--v_clm_no       IN     VARCHAR2,
----                                                                           NVL(v_msg_subj,'TURNQUEST SYSTEM ALERT'),--v_msg_subj     IN     VARCHAR2,
----                                                                           a.alrt_email_msgt_code,--v_msgt_code    IN     NUMBER,
----                                                                           v_files_tab,--v_files_tab    IN     email_files_tab,
----                                                                           a.alrt_sys_code,--v_sys_code     IN     NUMBER,
----                                                                           'A',--v_sys_mod      IN     VARCHAR2,
----                                                                           v_user,--v_user         IN     VARCHAR2,
----                                                                           v_email_code,--v_email_code   IN OUT NUMBER,
----                                                                           v_app_lvl,--v_app_lvl      IN     VARCHAR2
----                                                                           NVL(v_user_code,a.alrt_grp_usr_code)
----                                                                           );
--               -- Tqc_Email_Pkg.send_mail(v_email_code);
        END IF;
                             
   END LOOP;
  END;
      FUNCTION orgcurrencycode (v_sys_code IN NUMBER )
   RETURN NUMBER
   IS
   v_return NUMBER;
   BEGIN
           SELECT CUR_code
        INTO v_return
        FROM TQC_ORGANIZATIONS,TQC_SYSTEMS, TQC_CURRENCIES
        WHERE ORG_CODE = SYS_ORG_CODE
        AND CUR_CODE = ORG_CUR_CODE
        AND SYS_CODE =  v_sys_code;

        RETURN v_return;
    EXCEPTION
        WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR (-20001,
                                     'ERROR GETTING DEFAULT CURRENCY  '
                                  || v_sys_code
                                  || '.'
                                 );
   END;
END Tqc_Interfaces_Pkg030215; 
/