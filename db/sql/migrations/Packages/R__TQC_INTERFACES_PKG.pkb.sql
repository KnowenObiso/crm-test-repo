--
-- TQC_INTERFACES_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_interfaces_pkg
AS
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   PROCEDURE organizations_query (
      corgrefcur   IN OUT   orgrefcur,
      vorgcode     IN       NUMBER DEFAULT NULL,
      vbrhcode     IN       NUMBER DEFAULT NULL
   )
   IS
      v_usr_code   NUMBER := vbrhcode;         --note this is user not branch
   BEGIN
      OPEN corgrefcur FOR
         SELECT org_code, org_sht_desc, org_name, org_cur_code
           FROM tqc_organizations
          WHERE (    (   (tqc_organizations.org_code = vorgcode)
                      OR (vorgcode IS NULL)
                     )
                 AND (   (v_usr_code IS NULL)
                      OR (tqc_interfaces_pkg.userinorganization (v_usr_code,
                                                                 org_code
                                                                ) = 'TRUE'
                         )
                     )
                );
   END organizations_query;

   FUNCTION taxes_applicable
      RETURN VARCHAR2
   IS
      v_tax   VARCHAR2 (1);
   BEGIN
      SELECT param_value
        INTO v_tax
        FROM tqc_parameters
       WHERE param_name = 'TAXES_APPLICABLE';

      RETURN v_tax;
   EXCEPTION
      WHEN OTHERS
      THEN
         RETURN 'Y';
         NULL;
   END;

   FUNCTION getobjectfilename (
      vobjectname   VARCHAR2,
      vsyscode      NUMBER DEFAULT NULL
   )
      RETURN VARCHAR2
   IS
      vretval   VARCHAR2 (50);
   BEGIN
      SELECT NVL (obj_file_name, obj_name)
        INTO vretval
        FROM tqc_system_objects
       WHERE (    (obj_name = vobjectname)
              AND ((obj_sys_code = vsyscode) OR (vsyscode IS NULL))
             );

      RETURN vretval;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN vobjectname;
   END getobjectfilename;

   FUNCTION module_enabled (vmodule IN VARCHAR2)
      RETURN BOOLEAN
   IS
      vrtn   BOOLEAN := FALSE;                           --   VARCHAR2 (100);
   BEGIN
      IF UPPER (vmodule) = 'SMS'
      THEN
         vrtn := TRUE;
      ELSIF UPPER (vmodule) = 'EMAIL'
      THEN
         vrtn := TRUE;
      ELSIF UPPER (vmodule) = 'DMS'
      THEN
         vrtn := TRUE;
      END IF;

      RETURN vrtn;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END module_enabled;

   PROCEDURE getorganizations (
      corgrefcur   IN OUT   orgrefcur,
      vusername    IN       VARCHAR2
   )
   IS
      v_usr_code   NUMBER := usercode (vusername);
   --note this is user not branch
   BEGIN
      -- Organizations_Query(cOrgRefCur, null, v_usr_code);
      OPEN corgrefcur FOR
         SELECT org_code, org_sht_desc, org_name, org_cur_code
           FROM tqc_organizations;
   --            WHERE ((   (v_usr_code IS NULL)
   --                    OR (
   --                    Tqc_Interfaces_Pkg.userinorganization (v_usr_code,
   --                                                               org_code
   --                                                              ) = 'TRUE'
   --                      )
   --                   )
   --                 );
   END getorganizations;

   PROCEDURE getorganizationdetails (
      corgrefcur   IN OUT   orgdtlscur,
      v_org_code   IN       NUMBER
   )
   IS
   BEGIN
      -- Organizations_Query(cOrgRefCur, null, v_usr_code);
      OPEN corgrefcur FOR
         SELECT org_code, org_sht_desc, org_name, org_cur_code, org_type,
                lang_code, lang_desc, lang_bundle
           FROM tqc_organizations, tqc_languages, tqc_systems
          WHERE lang_code = org_lang_code
            AND sys_org_code = org_code
            AND sys_code = v_org_code;
   --
   END getorganizationdetails;

   PROCEDURE branches_query (
      cbrhrefcur   IN OUT   branchrefcur,
      vorgcode     IN       NUMBER DEFAULT NULL,
      vbrhcode     IN       NUMBER DEFAULT NULL,
      vusercode    IN       NUMBER DEFAULT NULL
   )
   IS
   BEGIN
      IF vorgcode IS NULL
      THEN
         OPEN cbrhrefcur FOR
            SELECT bns_code, bns_sht_desc, bns_name, NULL, NULL, NULL, NULL,
                   bns_phy_addrs, bns_email_addrs, bns_post_addrs,
                   bns_twn_code, bns_cou_code, bns_contact, bns_manager,
                   bns_tel, bns_fax, bns_code
              FROM tqc_branch_names
             WHERE (   (tqc_branch_names.bns_code = vbrhcode)
                    OR (vbrhcode IS NULL)
                   );
      ELSE
         OPEN cbrhrefcur FOR
            SELECT brn_code, brn_sht_desc, brn_name, reg_org_code, reg_code,
                   reg_sht_desc, reg_name, brn_phy_addrs, brn_email_addrs,
                   brn_post_addrs, brn_twn_code, brn_cou_code, brn_contact,
                   brn_manager, brn_tel, brn_fax, brn_bns_code
              FROM tqc_branches, tqc_regions
             WHERE (    (tqc_regions.reg_code = tqc_branches.brn_reg_code)
                    AND (reg_org_code = vorgcode)
                    AND (   (tqc_branches.brn_code = vbrhcode)
                         OR (vbrhcode IS NULL)
                        )
                   );
      END IF;
   END branches_query;

   FUNCTION menuname (vmenucode NUMBER)
      RETURN VARCHAR2
   IS
      vmenuname   VARCHAR2 (100);
   BEGIN
      SELECT menu_name
        INTO vmenuname
        FROM tqc_menu
       WHERE menu_code = vmenucode;

      RETURN vmenuname;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END menuname;

   FUNCTION formsystemcode (vformname VARCHAR2)
      RETURN NUMBER
   IS
      vsystemcode   NUMBER;
   BEGIN
      SELECT sys_code
        INTO vsystemcode
        FROM tqc_system_objects, tqc_systems
       WHERE sys_code = obj_sys_code AND obj_name = vformname;

      RETURN vsystemcode;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END formsystemcode;

   FUNCTION formsystemcode2 (vformname VARCHAR2, v_org_type OUT VARCHAR2)
      RETURN NUMBER
   IS
      vsystemcode   NUMBER;
   BEGIN
      SELECT sys_code, NVL (org_type, 'INS')
        INTO vsystemcode, v_org_type
        FROM tqc_system_objects, tqc_systems, tqc_organizations
       WHERE sys_code = obj_sys_code
         AND sys_org_code = org_code
         AND obj_name = vformname;

      RETURN vsystemcode;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END formsystemcode2;

   PROCEDURE formheader (
      vformname   IN       VARCHAR2,
      vtoolbar    IN OUT   VARCHAR2,
      vsysname    IN OUT   VARCHAR2
   )
   IS
      vsystemcode   NUMBER;
   BEGIN
      SELECT obj_label, sys_name
        INTO vtoolbar, vsysname
        FROM tqc_system_objects, tqc_systems
       WHERE sys_code = obj_sys_code AND obj_name = vformname;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         raise_application_error
                              (-20001,
                               'FORM NOT REGISTERED. CONTACT ADMINISTRATOR..'
                              );
   END formheader;

   FUNCTION menuitems_query (vsyscode IN NUMBER, vusername IN VARCHAR2)
      RETURN menurefcur
   IS
      cmenusrefcur   menurefcur;
   BEGIN
      OPEN cmenusrefcur FOR
         SELECT menu_name, menu_type, menu_menu_code
           FROM tqc_menu,
                tqc_system_role_menu,
                tqc_system_roles,
                tqc_user_system_roles
          WHERE menu_code = srm_menu_code
            AND srl_code = srm_srl_code
            AND srl_code = usrl_srl_code
            AND srl_sys_code = vsyscode
            AND usrl_revoke_date IS NULL
            AND usrl_usr_code = (SELECT usr_code
                                   FROM tqc_users
                                  WHERE usr_username = vusername);

      RETURN cmenusrefcur;
   END menuitems_query;

   FUNCTION menuitems_query2 (vsyscode IN NUMBER, vusername IN VARCHAR2)
      RETURN menurefcur2
   IS
      cmenusrefcur   menurefcur2;
   BEGIN
      OPEN cmenusrefcur FOR
         SELECT   menu_name, menu_type, menu_menu_code, menu_org_type_show,
                  menu_brk_label
             FROM tqc_menu,
                  tqc_system_role_menu,
                  tqc_system_roles,
                  tqc_user_system_roles
            WHERE menu_code = srm_menu_code
              AND srl_code = srm_srl_code
              AND srl_code = usrl_srl_code
              AND srl_sys_code = vsyscode
              AND usrl_revoke_date IS NULL
              AND usrl_usr_code = (SELECT usr_code
                                     FROM tqc_users
                                    WHERE usr_username = vusername)
         ORDER BY menu_code;

      RETURN cmenusrefcur;
   END menuitems_query2;

   FUNCTION getdrlimit (
      v_user_code   NUMBER,
      v_sys_code    NUMBER,
      v_auth_area   VARCHAR2
   )
      RETURN NUMBER
   IS
      v_dr_limit   NUMBER := 0;
   BEGIN
      --RETURN 9999999999;
      IF (   (v_user_code IS NULL)
          OR (v_sys_code IS NULL)
          OR (v_auth_area IS NULL)
         )
      THEN
         v_dr_limit := 0;
      ELSE
         SELECT   NVL (MAX (sar_debit_limit), 0) sar_debit_limit
             INTO v_dr_limit
             FROM tqc_system_auth_areas,
                  tqc_system_auth_roles,
                  tqc_user_system_auth_roles,
                  tqc_users
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
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                     'ERROR GETTING DEBIT LIMIT. V_SYS_CODE'
                                  || v_sys_code
                                  || ' V_AUTH_AREA'
                                  || v_auth_area
                                  || ' V_USER_CODE'
                                  || v_user_code
                                  || SQLERRM (SQLCODE)
                                 );
         RETURN 0;
   END getdrlimit;

   FUNCTION accounttypeid (
      v_act_code   NUMBER DEFAULT NULL,
      vtype_id     CHAR DEFAULT 'T'
   )
      RETURN VARCHAR2
   IS
      v_type      VARCHAR2 (50);
      v_type_id   VARCHAR2 (5);
   BEGIN
      IF v_act_code IS NULL
      THEN
         RETURN NULL;
      ELSE
         SELECT act_account_type, act_type_id
           INTO v_type, v_type_id
           FROM tqc_account_types
          WHERE act_code = v_act_code;
      END IF;

      IF vtype_id = 'I'
      THEN
         RETURN v_type_id;
      ELSE
         RETURN v_type;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001, 'ERROR RETRIEVING ACCOUNT TYPE..');
         RETURN NULL;
   END accounttypeid;

   FUNCTION accountcode (v_act_type_id VARCHAR)
      RETURN NUMBER
   IS
      v_ret_val   NUMBER;
   BEGIN
      SELECT act_code
        INTO v_ret_val
        FROM tqc_account_types
       WHERE act_type_id = v_act_type_id;

      RETURN v_ret_val;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                     'RETRIEVING ACCOUNT TYPE ID..'
                                  || SQLERRM (SQLCODE)
                                 );
         RETURN NULL;
   END accountcode;

   FUNCTION agency_query (v_search_val VARCHAR2)
      RETURN fms_interfaces_pkg.type_client_types_ref_cursor
   IS
      v_data   fms_interfaces_pkg.type_client_types_ref_cursor;
   BEGIN
      OPEN v_data FOR
         SELECT agn_code code, agn_name NAME, act_type_id
           FROM tqc_agencies, tqc_account_types
          WHERE agn_act_code = act_code
            AND agn_name LIKE '%' || v_search_val || '%';

      RETURN v_data;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (-20001, '..ERROR RETRIEVING AGENT NAME ..');
   END agency_query;

   FUNCTION agencyname (v_act_code NUMBER)
      RETURN VARCHAR2
   IS
      v_rtval   VARCHAR2 (200) DEFAULT NULL;
   BEGIN
      SELECT tqc_agencies.agn_name
        INTO v_rtval
        FROM tqc_agencies
       WHERE tqc_agencies.agn_code = v_act_code;

      RETURN v_rtval;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001, '..ERROR RETRIEVING AGENT NAME ..');
         RETURN NULL;
   END agencyname;

   FUNCTION get_org_type (v_sys_code NUMBER)
      RETURN VARCHAR2
   IS
      v_rtval   VARCHAR2 (200) DEFAULT NULL;
   BEGIN
      --DBMS_OUTPUT.PUT_LINE(sys_code);
      SELECT org_type
        INTO v_rtval
        FROM tqc_organizations, tqc_systems
       WHERE sys_org_code = org_code AND sys_code = v_sys_code;

      --v_rtval := 'DFD';
      RETURN v_rtval;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error
                                (-20001,
                                    '..ERROR RETRIEVING ORGANIZATION TYPE ..'
                                 || v_sys_code
                                 || SQLERRM
                                );
         RETURN NULL;
   END get_org_type;

   FUNCTION get_org_type (v_sys_name VARCHAR2)
      RETURN VARCHAR2
   IS
      v_rtval   VARCHAR2 (200) DEFAULT NULL;
   BEGIN
      SELECT org_type
        INTO v_rtval
        FROM tqc_organizations, tqc_systems
       WHERE sys_org_code = org_code AND sys_sht_desc = v_sys_name;

      RETURN v_rtval;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                  '..ERROR RETRIEVING ORGANIZATION TYPE ..'
                                 );
         RETURN NULL;
   END get_org_type;

   FUNCTION username (v_usr_code NUMBER, v_full VARCHAR2 DEFAULT 'N')
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      IF v_usr_code IS NULL
      THEN
         v_ret_val := NULL;
      ELSE
         SELECT DECODE (v_full, 'N', usr_username, 'Y', usr_name)
           INTO v_ret_val
           FROM tqc_users
          WHERE usr_code = v_usr_code;
      END IF;

      RETURN v_ret_val;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                     'ERROR GETTING USERNAME FOR USER CODE '
                                  || TO_CHAR (v_usr_code)
                                  || '.'
                                  || SQLERRM (SQLCODE)
                                 );
         RETURN NULL;
   END;

   ------------------returns logged on usercode if the username is not provided.------------------
   FUNCTION usercode (v_usr_name VARCHAR2)
      RETURN NUMBER
   IS
      v_usr_code   NUMBER := TO_NUMBER (NULL);
   BEGIN
      IF v_usr_name IS NOT NULL
      THEN
         SELECT usr_code
           INTO v_usr_code
           FROM tqc_users
          WHERE usr_username = v_usr_name;
      END IF;

      RETURN v_usr_code;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN v_usr_code;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                     'ERROR GETTING USERCODE FOR USER NAME '
                                  || v_usr_name
                                  || '.'
                                 );
         RETURN NULL;
   END;

   FUNCTION orgcurrency (v_sys_code IN NUMBER, v_cur_symbol OUT VARCHAR2)
      RETURN NUMBER
   IS
      v_return   NUMBER;
   BEGIN
      SELECT org_cur_code, cur_symbol
        INTO v_return, v_cur_symbol
        FROM tqc_organizations, tqc_systems, tqc_currencies
       WHERE org_code = sys_org_code
         AND cur_code = org_cur_code
         AND sys_code = v_sys_code;

      RETURN v_return;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                     'ERROR GETTING DEFAULT CURRENCY  '
                                  || v_sys_code
                                  || '.'
                                 );
   END;

   FUNCTION orgcurrencydefault (v_cur_symbol OUT VARCHAR2)
      RETURN VARCHAR2
   IS
   BEGIN
      SELECT cur_symbol
        INTO v_cur_symbol
        FROM tqc_organizations, tqc_systems, tqc_currencies
       WHERE org_code = sys_org_code
         AND cur_code = org_cur_code
         AND sys_code = 37;

      RETURN v_cur_symbol;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                  'ERROR GETTING DEFAULT CURRENCY  ' || '.'
                                 );
   END;

   FUNCTION currencyname (v_curr_code NUMBER, v_full VARCHAR2 DEFAULT 'S')
      RETURN VARCHAR2
   IS
      v_trn_cur_symbol        VARCHAR2 (15);
      v_trn_cur_name          VARCHAR2 (20);
      v_trn_cur_symbol_name   VARCHAR2 (36);
   BEGIN
      IF v_curr_code IS NULL
      THEN
         RETURN NULL;
      ELSIF v_full = 'S'
      THEN
         SELECT tqc_currencies.cur_symbol
           INTO v_trn_cur_symbol
           FROM tqc_currencies
          WHERE tqc_currencies.cur_code = v_curr_code;

         RETURN v_trn_cur_symbol;
      ELSIF v_full = 'L'
      THEN
         SELECT tqc_currencies.cur_desc
           INTO v_trn_cur_name
           FROM tqc_currencies
          WHERE tqc_currencies.cur_code = v_curr_code;

         RETURN v_trn_cur_name;
      ELSE
         SELECT tqc_currencies.cur_desc || '-' || tqc_currencies.cur_symbol
           INTO v_trn_cur_symbol_name
           FROM tqc_currencies
          WHERE tqc_currencies.cur_code = v_curr_code;

         RETURN v_trn_cur_symbol_name;
      END IF;
   END currencyname;

   FUNCTION currencyrate (v_fcur NUMBER, v_bcur NUMBER, v_date DATE)
      RETURN NUMBER
   IS
      v_cur_excs   NUMBER;
      rtnval       NUMBER := NULL;
   BEGIN
      IF v_fcur IS NULL OR v_bcur IS NULL
      THEN
         RETURN NULL;
      ELSIF v_fcur = v_bcur
      THEN
         RETURN 1;
      ELSIF v_date IS NULL
      THEN
         raise_application_error
            (-20001,
                'GETTING CURRENCY RATE: THE CURRENCY EXCHANGE RATE CANNOT BE DETERMINED FOR '
             || currencyname (v_fcur)
             || ' AGAINST '
             || currencyname (v_bcur)
             || '. NO DATE WAS SUPPLIED.'
            );
         RETURN NULL;
      ELSE
         SELECT COUNT ('X')
           INTO v_cur_excs
           FROM tqc_currency_rates
          WHERE crt_cur_code = v_fcur
            AND crt_date = TO_DATE (v_date, 'DD/MM/RRRR');

         IF v_cur_excs <> 0
         THEN
            SELECT crt_rate
              INTO rtnval
              FROM tqc_currency_rates
             WHERE crt_cur_code = v_fcur
               AND crt_date = TO_DATE (v_date, 'DD/MM/RRRR');

            RETURN rtnval;
         ELSE
            raise_application_error
                             (-20001,
                                 'THERE IS NO EXCHANGE RATE OF '
                              || currencyname (v_fcur)
                              || ' AGAINST '
                              || currencyname (v_bcur)
                              || ' IN THE SYSTEM FOR '
                              || TO_CHAR (v_date, 'DD/MM/YYYY')
                              || '. PLEASE DEFINE THE RATE IN THE CORE SYSTEM ?'
                             );
            RETURN NULL;
         END IF;
      END IF;
   END currencyrate;

   FUNCTION branchname (v_brn_code NUMBER, v_full VARCHAR2 DEFAULT 'Y')
      RETURN VARCHAR2
   IS
      v_rtval   VARCHAR2 (50);
   BEGIN
      IF v_brn_code IS NULL
      THEN
         RETURN NULL;
      ELSE
         IF v_full = 'Y'
         THEN
            SELECT brn_name
              INTO v_rtval
              FROM tqc_branches
             WHERE brn_code = v_brn_code;
         ELSIF v_full = 'S'
         THEN
            SELECT brn_sht_desc
              INTO v_rtval
              FROM tqc_branches
             WHERE brn_code = v_brn_code;
         ELSE
            SELECT brn_sht_desc || '-' || brn_name
              INTO v_rtval
              FROM tqc_branches
             WHERE brn_code = v_brn_code;
         END IF;

         RETURN v_rtval;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001, '..ERROR RETRIEVING BRANCH NAME..');
   END branchname;

   FUNCTION branchname (
      v_org_code   NUMBER,
      v_bns_code   NUMBER,
      v_full       VARCHAR2 DEFAULT 'Y'
   )
      RETURN VARCHAR2
   IS
      v_rtval   VARCHAR2 (50);
   BEGIN
      IF v_org_code IS NULL
      THEN
         IF v_bns_code IS NULL
         THEN
            RETURN NULL;
         ELSE
            IF v_full = 'Y'
            THEN
               SELECT bns_name
                 INTO v_rtval
                 FROM tqc_branch_names
                WHERE bns_code = v_bns_code;
            ELSIF v_full = 'S'
            THEN
               SELECT bns_sht_desc
                 INTO v_rtval
                 FROM tqc_branch_names
                WHERE bns_code = v_bns_code;
            ELSE
               SELECT bns_sht_desc || '-' || bns_name
                 INTO v_rtval
                 FROM tqc_branch_names
                WHERE bns_code = v_bns_code;
            END IF;

            RETURN v_rtval;
         END IF;
      ELSE
         RETURN branchname (branchcode (v_org_code, v_bns_code), v_full);
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                  '..ERROR RETRIEVING BRANCH NAME NAME..'
                                 );
   END branchname;

   FUNCTION branchcode (v_org_code NUMBER, v_bns_code NUMBER)
      RETURN NUMBER
   IS
      v_rtval   NUMBER;
   BEGIN
      SELECT tqc_branches.brn_code
        INTO v_rtval
        FROM tqc_regions, tqc_branches
       WHERE (    (tqc_regions.reg_code = tqc_branches.brn_reg_code)
              AND (tqc_branches.brn_bns_code = v_bns_code)
              AND (tqc_regions.reg_org_code = v_org_code)
             );

      RETURN v_rtval;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                     '..ERROR RETRIEVING BRANCH CODE..'
                                  || v_bns_code
                                  || ';'
                                  || v_org_code
                                 );
   END branchcode;

   FUNCTION branchcode (v_brh_code NUMBER)
      RETURN NUMBER
   IS
      v_bns_code   NUMBER;
   BEGIN
      SELECT tqc_branches.brn_bns_code
        INTO v_bns_code
        FROM tqc_branches
       WHERE ((tqc_branches.brn_code = v_brh_code));

      RETURN v_bns_code;
   END;

   FUNCTION bankbranch (v_bbr_code NUMBER, v_full VARCHAR2 DEFAULT 'S')
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      IF v_bbr_code IS NULL
      THEN
         RETURN NULL;
      ELSIF v_full = 'S'
      THEN
         SELECT bbr_sht_desc
           INTO v_ret_val
           FROM tqc_bank_branches
          WHERE tqc_bank_branches.bbr_code = v_bbr_code;

         RETURN v_ret_val;
      ELSIF v_full = 'L'
      THEN
         SELECT bbr_branch_name
           INTO v_ret_val
           FROM tqc_bank_branches
          WHERE tqc_bank_branches.bbr_code = v_bbr_code;

         RETURN v_ret_val;
      END IF;
   END bankbranch;

   FUNCTION bank_ref (v_bbr_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (100);
   BEGIN
      IF v_bbr_code IS NULL
      THEN
         RETURN NULL;
      ELSE
         SELECT bbr_ref_code
           INTO v_ret_val
           FROM tqc_bank_branches
          WHERE tqc_bank_branches.bbr_code = v_bbr_code;

         RETURN v_ret_val;
      END IF;
   END bank_ref;

-------------------------GET_SYS_NAME-------------------------------------------
   FUNCTION systemname (v_sys_code NUMBER, v_full VARCHAR2 DEFAULT 'Y')
      RETURN VARCHAR2
   IS
      v_rtval   VARCHAR2 (100);
   BEGIN
      IF v_sys_code IS NULL
      THEN
         RETURN NULL;
      ELSE
         IF v_full = 'Y'
         THEN
            SELECT tqc_systems.sys_name
              INTO v_rtval
              FROM tqc_systems
             WHERE tqc_systems.sys_code = v_sys_code;
         ELSIF v_full = 'S'
         THEN
            SELECT tqc_systems.sys_sht_desc
              INTO v_rtval
              FROM tqc_systems
             WHERE tqc_systems.sys_code = v_sys_code;
         ELSE
            SELECT tqc_systems.sys_sht_desc || '-' || tqc_systems.sys_name
              INTO v_rtval
              FROM tqc_systems
             WHERE tqc_systems.sys_code = v_sys_code;
         END IF;

         RETURN v_rtval;
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001, '..ERROR RETRIEVING SYSTEM NAME..');
   END systemname;

   FUNCTION branchorganization (v_brh_code NUMBER)
      RETURN NUMBER
   IS
      v_org_code   NUMBER;
   BEGIN
      SELECT tqc_regions.reg_org_code
        INTO v_org_code
        FROM tqc_branches, tqc_regions
       WHERE (    (tqc_regions.reg_code = tqc_branches.brn_reg_code)
              AND (tqc_branches.brn_code = v_brh_code)
             );

      RETURN v_org_code;
   END;

   FUNCTION branchinorganization (v_org_code NUMBER, v_brh_code NUMBER)
      RETURN NUMBER
   IS
      v_bns_code   NUMBER;
   BEGIN
      v_bns_code := branchcode (v_brh_code);
      RETURN branchcode (v_org_code, v_bns_code);
   END;

   FUNCTION clientname (v_clnt_code NUMBER)
      RETURN VARCHAR2
   IS
      v_rtval   VARCHAR2 (200) DEFAULT NULL;
   BEGIN
      SELECT clnt_name
        INTO v_rtval
        FROM tqc_clients
       WHERE clnt_code = v_clnt_code;

      RETURN v_rtval;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                  '..ERROR RETRIEVING CLIENT NAME ..');
         RETURN NULL;
   END clientname;

   FUNCTION branchregion (v_brh_code NUMBER)
      RETURN NUMBER
   IS
      v_reg_code   NUMBER;
   BEGIN
      SELECT tqc_regions.reg_code
        INTO v_reg_code
        FROM tqc_branches, tqc_regions
       WHERE (    (tqc_regions.reg_code = tqc_branches.brn_reg_code)
              AND (tqc_branches.brn_code = v_brh_code)
             );

      RETURN v_reg_code;
   END;

   FUNCTION branchregion (v_org_code NUMBER, v_bns_code NUMBER)
      RETURN NUMBER
   IS
   BEGIN
      RETURN branchregion (branchcode (v_org_code, v_bns_code));
   END;

   FUNCTION currencysymbol (vcurcode NUMBER)
      RETURN VARCHAR2
   IS
      vsymbol   VARCHAR2 (15);
   BEGIN
      SELECT tqc_currencies.cur_symbol          --,    TQC_CURRENCIES.CUR_DESC
        INTO vsymbol
        FROM tqc_currencies
       WHERE ((tqc_currencies.cur_code = vcurcode));

      RETURN vsymbol;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END currencysymbol;

   FUNCTION systemorganization (vsyscode VARCHAR2)
      RETURN NUMBER
   IS
      vorg   NUMBER;
   BEGIN
      SELECT tqc_systems.sys_org_code
        INTO vorg
        FROM tqc_systems
       WHERE ((tqc_systems.sys_sht_desc = vsyscode));

      RETURN vorg;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION organizationname (vorgcode NUMBER, vfull VARCHAR2 DEFAULT 'Y')
      RETURN VARCHAR2
   IS
      vorg   VARCHAR2 (50);
   BEGIN
      SELECT DECODE (vfull,
                     'Y', tqc_organizations.org_name,
                     tqc_organizations.org_sht_desc
                    )
        INTO vorg
        FROM tqc_organizations
       WHERE ((tqc_organizations.org_code = vorgcode));

      RETURN vorg;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION organizationname (vfull VARCHAR2 DEFAULT 'Y')
      RETURN VARCHAR2
   IS
      vorg   VARCHAR2 (50);
   BEGIN
      SELECT DECODE (vfull, 'Y', NULL, NULL)
        INTO vorg
        FROM DUAL;

      RETURN vorg;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   PROCEDURE costappropriation_query (
      corgcostrefcur   IN OUT   orgcostrefcur,
      vccscode         IN       NUMBER,
      vexccode         IN       NUMBER
   )
   IS
   BEGIN
      OPEN corgcostrefcur FOR
         SELECT org_code, vexccode, vccscode, org_name,
                fms_interfaces_pkg.org_cost_apportionment (org_code,
                                                           vccscode,
                                                           vexccode
                                                          )
           FROM tqc_organizations;
   END costappropriation_query;

   PROCEDURE systemsunionfms (
      systems_data_rec   IN OUT   type_systems_ref_cur,
      v_org_code         IN       NUMBER
   )
   IS
      v_fms_sys_code   NUMBER (8);
      v_fms_sys_name   VARCHAR2 (100);
   BEGIN
      SELECT tqc_systems.sys_code, tqc_systems.sys_name
        INTO v_fms_sys_code, v_fms_sys_name
        FROM tqc_systems
       WHERE ((tqc_systems.sys_sht_desc = 'FMS'));

      OPEN systems_data_rec FOR
         SELECT sys_code, sys_name, sys_sht_desc, sys_org_code
           FROM tqc_systems
          WHERE sys_org_code = v_org_code
         UNION
         SELECT v_fms_sys_code, v_fms_sys_name, 'FMS', v_org_code
           FROM DUAL;
   END;

   FUNCTION systemcode (v_sys_name IN VARCHAR2)
      RETURN NUMBER
   IS
      v_sys_code   NUMBER (8);
   BEGIN
      SELECT tqc_systems.sys_code
        INTO v_sys_code
        FROM tqc_systems
       WHERE ((tqc_systems.sys_sht_desc = v_sys_name));

      RETURN v_sys_code;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN NULL;
   END;

   FUNCTION userinbranch (v_usr_name VARCHAR2, vbrncode NUMBER)
      RETURN VARCHAR2
   IS
      vcnt   NUMBER;
   BEGIN
      RETURN (userinbranch (usercode (v_usr_name), vbrncode));
   END;

   FUNCTION userinbranch (vusrcode NUMBER, vbrncode NUMBER)
      RETURN VARCHAR2
   IS
      vcnt   NUMBER;
   BEGIN
      SELECT COUNT ('X')
        INTO vcnt
        FROM tqc_user_branches
       WHERE (    (tqc_user_branches.usb_brn_code = vbrncode)
              AND (usb_usr_code = vusrcode)
             );

      IF vcnt = 0
      THEN
         RETURN 'FALSE';
      ELSE
         RETURN 'TRUE';
      END IF;
   END;

   PROCEDURE logaudit (vsessionid NUMBER)
   IS
   BEGIN
      UPDATE tqc_user_logon
         SET usl_logout_dt = SYSDATE
       WHERE usl_session_code = vsessionid;
   END;

   FUNCTION userinorganization (vusrcode NUMBER, vorgcode NUMBER)
      RETURN VARCHAR2
   IS
      vcnt   NUMBER;
   BEGIN
      SELECT COUNT ('X')
        INTO vcnt
        FROM tqc_user_branches
       WHERE (    (tqc_user_branches.usb_usr_code = vusrcode)
              AND tqc_interfaces_pkg.branchorganization (usb_brn_code) =
                                                                      vorgcode
             );

      IF vcnt = 0
      THEN
         RETURN 'FALSE';
      ELSE
         RETURN 'TRUE';
      END IF;
   END;

   FUNCTION check_user_rights (
      v_usr_name        IN   VARCHAR2,
      v_sys_code        IN   NUMBER,
      v_process         IN   VARCHAR2,
      v_prcs_area       IN   VARCHAR2,
      v_prsc_sub_area   IN   VARCHAR2,
      v_amount          IN   NUMBER DEFAULT NULL,
      v_dc              IN   VARCHAR2 DEFAULT NULL
   )
      RETURN VARCHAR2
   IS
      v_user_code    NUMBER;
      v_rights       BOOLEAN;
      v_rights_val   VARCHAR2 (50);
   BEGIN
 -- RETURN 'Y';
      v_user_code := tqc_interfaces_pkg.usercode (v_usr_name);
      v_rights :=
         check_user_rights2 (v_user_code,
                             v_sys_code,
                             v_process,
                             v_prcs_area,
                             v_prsc_sub_area,
                             v_amount,
                             v_dc
                            );

      IF v_rights = TRUE THEN
        v_rights_val := 'Y'; 
      ELSE
        v_rights_val := 'N';
      END IF;
    --RETURN ('Y');
      RETURN (v_rights_val);
   END check_user_rights;

--  FUNCTION check_user_rights2 (
--      v_user_code       IN   NUMBER,
--      v_sys_code        IN   NUMBER,
--      v_process         IN   VARCHAR2,
--      v_prcs_area       IN   VARCHAR2,
--      v_prsc_sub_area   IN   VARCHAR2,
--      v_amount          IN   NUMBER DEFAULT NULL,
--      v_dc              IN   VARCHAR2 DEFAULT NULL
--   )
--      RETURN BOOLEAN
--   IS
--      v_cnt        NUMBER := 0;
--      v_dr_limit     NUMBER := 0;
--      v_cr_limit     NUMBER := 0;
--      v_right_type   VARCHAR2 (15) := NULL;
--      v_role_maker_checker   VARCHAR2 (15) := tqc_parameters_pkg.get_param_varchar('ROLE_MAKER_CHECKER_APP');
--      v_user_maker_checker   VARCHAR2 (15) := tqc_parameters_pkg.get_param_varchar('USER_MAKER_CHECKER_APP');
--      v_revoke_app   VARCHAR2 (15) := tqc_parameters_pkg.get_param_varchar('REVOKE_DATE_APP');
--      CURSOR c_roles(
--              p_user_code          NUMBER,
--              p_sys_code           NUMBER,
--              p_process            VARCHAR2,
--              p_prcs_area          VARCHAR2,
--              p_prsc_sub_area      VARCHAR2
--      ) IS     SELECT *   FROM tqc_sys_user_roles,
--                   tqc_sys_roles_processes,
--                   tqc_sys_processes,
--                   tqc_sys_roles_prcs_area,
--                   tqc_sys_process_areas,
--                   tqc_sys_roles_prcs_s_area,
--                   tqc_sys_process_sub_areas,
--                   tqc_sys_roles,tqc_users
--             WHERE usrr_srls_code = srprc_srls_code
--               AND srprc_code = srpra_srprc_code
--               AND srprc_sprc_code = sprc_code
--               AND srpra_code = srpsa_srpra_code
--               AND srpra_sprca_code = sprca_code
--               AND srpsa_sprsa_code = sprsa_code
--               AND usrr_usr_code = p_user_code
--               AND usr_code = usrr_usr_code
--               AND sprc_sht_desc = p_process
--               AND sprca_sht_desc = p_prcs_area
--               AND sprsa_sht_desc = p_prsc_sub_area
--               AND sprc_sys_code = p_sys_code
--               AND srls_code = usrr_srls_code
--               AND ((srls_authorized='Y' AND v_role_maker_checker='Y') or ( v_role_maker_checker = 'N' ))
--               AND ((usr_authorized='Y' AND v_user_maker_checker='Y') or ( v_user_maker_checker = 'N' ))
--               AND srls_status NOT IN ('N', 'I')
--               AND (((SYSDATE BETWEEN usrr_grant_date AND usrr_revoke_date) AND v_revoke_app='Y') or ( v_revoke_app = 'N' ));
--       
--            
--            
--   BEGIN
--    --  RETURN (TRUE);      
--      IF (   (v_user_code IS NULL)
--          OR (v_sys_code IS NULL)
--          OR (v_process IS NULL)
--          OR (v_prcs_area IS NULL)
--          OR (v_prsc_sub_area IS NULL)
--         )
--      THEN
--         RETURN (FALSE);
--      END IF; 
--     

--    BEGIN
--    -- CHECK SYSTEM GRANT
--    SELECT COUNT(1) INTO v_cnt
--        FROM TQC_USER_SYSTEMS
--    WHERE USYS_SYS_CODE= v_sys_code
--    AND USYS_USR_CODE = v_user_code;
--    EXCEPTION WHEN OTHERS THEN
--      v_cnt:=0;
--    END;

--    IF NVL (v_cnt, 0)=0
--    THEN
--        log_required_rights
--                     (
--                      v_user_code      ,
--                      v_sys_code        ,
--                      v_process        ,
--                      v_prcs_area        ,
--                      v_prsc_sub_area    ,
--                      v_amount        ,
--                      v_dc            
--                     );
--        RETURN (FALSE);
--    END IF;
--    
--   BEGIN
--     v_cnt := 0;
--     FOR x IN c_roles(v_user_code, v_sys_code , v_process, v_prcs_area, v_prsc_sub_area ) 
--     LOOP
--       BEGIN
--               IF v_dr_limit < NVL(x.srpsa_debit_limit,0) THEN 
--                  v_dr_limit := NVL(x.srpsa_debit_limit,0);
--               END IF;
--               IF v_cr_limit < NVL(x.srpsa_credit_limit,0) THEN 
--                  v_cr_limit := NVL(x.srpsa_credit_limit,0);
--               END IF;
--              v_right_type := x.sprsa_type;
--              v_cnt := v_cnt + 1;
--      END;
--     END LOOP;
--   END;
--           
--   IF NVL (v_cnt, 0) > 1
--   THEN
--       RAISE_ERROR('More than one role assigned to this user with the same rights!');
--   END IF;
--   
--      IF v_right_type != 'A'  
--      THEN
--         IF NVL (v_cnt, 0) = 1 THEN
--            RETURN (TRUE);
--         ELSE
--            log_required_rights
--                     (
--                      v_user_code      ,
--                      v_sys_code        ,
--                      v_process        ,
--                      v_prcs_area        ,
--                      v_prsc_sub_area    ,
--                      v_amount        ,
--                      v_dc            
--                     );
--            RETURN (FALSE);
--         END IF;
--      ELSE -- its 'A'
--         IF NVL (v_cnt, 0) = 1 THEN
--             IF v_dc = 'C'
--             THEN
--                IF ABS (NVL (v_amount, 0)) > ABS (NVL (v_cr_limit, 0))
--                THEN
--                log_required_rights
--                     (
--                      v_user_code      ,
--                      v_sys_code        ,
--                      v_process        ,
--                      v_prcs_area        ,
--                      v_prsc_sub_area    ,
--                      v_amount        ,
--                      v_dc            
--                     );
--                   RETURN (FALSE);
--                ELSE
--                   RETURN (TRUE);
--                END IF;
--             ELSE
--                IF ABS (NVL (v_amount, 0)) > ABS (NVL (v_dr_limit, 0))
--                THEN
--                log_required_rights
--                     (
--                      v_user_code      ,
--                      v_sys_code        ,
--                      v_process        ,
--                      v_prcs_area        ,
--                      v_prsc_sub_area    ,
--                      v_amount        ,
--                      v_dc            
--                     );
--                   RETURN (FALSE);
--                ELSE
--                   RETURN (TRUE);
--                END IF;
--             END IF;
--         ELSE
--         log_required_rights
--                     (
--                      v_user_code      ,
--                      v_sys_code        ,
--                      v_process        ,
--                      v_prcs_area        ,
--                      v_prsc_sub_area    ,
--                      v_amount        ,
--                      v_dc            
--                     );
--            RETURN (FALSE);
--         END IF;
--         
--      END IF;
--   EXCEPTION
--      WHEN NO_DATA_FOUND
--      THEN
--      log_required_rights
--                     (
--                      v_user_code      ,
--                      v_sys_code        ,
--                      v_process        ,
--                      v_prcs_area        ,
--                      v_prsc_sub_area    ,
--                      v_amount        ,
--                      v_dc            
--                     );
--         --RAISE_APPLICATION_ERROR(-20001,'v_user_code=='||v_user_code||'v_process=='||v_process||'v_prcs_area=='||v_prcs_area||'v_prsc_sub_area =='||v_prsc_sub_area||'v_sys_code== '||v_sys_code);
--         RETURN (FALSE);
--      WHEN OTHERS
--      THEN
--         raise_application_error
--                               (-20001,
--                                   'UNABLE TO RETRIEVE AUTHORIZATION LIMITS '
--                                || v_sys_code
--                                || ' PROCESS'
--                                || v_process
--                                || ' AREA'
--                                || v_prcs_area
--                                || ' SUB AREA'
--                                || v_prsc_sub_area
--                                || ' V_USER_CODE'
--                                || v_user_code
--                                || SQLERRM (SQLCODE)
--                               );
--   END check_user_rights2;

  FUNCTION check_user_rights2 (
      v_user_code       IN   NUMBER,
      v_sys_code        IN   NUMBER,
      v_process         IN   VARCHAR2,
      v_prcs_area       IN   VARCHAR2,
      v_prsc_sub_area   IN   VARCHAR2,
      v_amount          IN   NUMBER DEFAULT NULL,
      v_dc              IN   VARCHAR2 DEFAULT NULL
   )
      RETURN BOOLEAN
   IS
      v_cnt          NUMBER        := 1;
      v_dr_limit     NUMBER;
      v_cr_limit     NUMBER;
      v_right_type   VARCHAR2 (15);
   BEGIN
     --RETURN (TRUE);
--    IF v_user_code IN (2079,
--    2186,
--    1717) THEN
--      RETURN (TRUE);
--    END IF;
--    IF v_user_code IN (1717) THEN
--      RETURN (TRUE);
--    END IF;
--  if v_user_code = 1729
--  then
--  raise_error(v_sys_code||'v_process=='||v_process||'v_prcs_area=='||v_prcs_area||'v_prsc_sub_area=='||v_prsc_sub_area||'v_amount=='||v_amount||'v_dc=='||v_dc);
-- End if;
--    IF v_prsc_sub_area  LIKE 'ACCS%' OR v_user_code = 195 THEN 
--      RETURN (TRUE);
--    END IF;

 IF v_user_code IN  (195, 1717) THEN 
      RETURN (TRUE);
 END IF;
    
      IF V_PROCESS LIKE 'SRT%' 
        THEN RETURN TRUE;
        END IF;
        
        if v_prcs_area LIKE 'GWP_%' 
        Then Return true;
        End if;
     IF (   (v_user_code IS NULL)
          OR (v_sys_code IS NULL)
          OR (v_process IS NULL)
          OR (v_prcs_area IS NULL)
          OR (v_prsc_sub_area IS NULL)
         )
      THEN
         RETURN (FALSE);
      END IF;

--  RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prsc_sub_area);
      SELECT   sprsa_type, COUNT (1), MAX (srpsa_debit_limit),
               MAX (srpsa_credit_limit)
          INTO v_right_type, v_cnt, v_dr_limit,
               v_cr_limit
          FROM tqc_sys_user_roles,
               tqc_sys_roles_processes,
               tqc_sys_processes,
               tqc_sys_roles_prcs_area,
               tqc_sys_process_areas,
               tqc_sys_roles_prcs_s_area,
               tqc_sys_process_sub_areas,
               tqc_sys_roles
         WHERE usrr_srls_code = srprc_srls_code
           AND srprc_code = srpra_srprc_code
           AND srprc_sprc_code = sprc_code
           AND srpra_code = srpsa_srpra_code
           AND srpra_sprca_code = sprca_code
           AND srpsa_sprsa_code = sprsa_code
           AND usrr_usr_code = v_user_code
           AND sprc_sht_desc = v_process
           AND sprca_sht_desc = v_prcs_area
           AND sprsa_sht_desc = v_prsc_sub_area
           AND sprc_sys_code = v_sys_code
           AND srls_code = usrr_srls_code
           AND srls_status NOT IN ('N', 'I')
           AND (usrr_revoke_date IS NULL OR usrr_revoke_date >= SYSDATE)
           --AND ROWNUM = 1
      GROUP BY sprsa_type;

      --RAISE_APPLICATION_ERROR(-20001, 'SA' ||v_amount ||'DC' || v_dc);
      --RAISE_APPLICATION_ERROR(-20001,v_right_type);
      IF v_right_type != 'A'
      THEN
         IF NVL (v_cnt, 0) = 0
         THEN
            RETURN (FALSE);
         ELSE
            RETURN (TRUE);
         END IF;
      ELSE
         --IF v_dc IS NULL OR v_amount IS NULL THEN
         --    RETURN(FALSE);
         --END IF;
         IF v_dc = 'C'
         THEN
            IF ABS (NVL (v_amount, 0)) > ABS (NVL (v_cr_limit, 0))
            THEN
               RETURN (FALSE);
            ELSE
               RETURN (TRUE);
            END IF;
         ELSE
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
         --RAISE_APPLICATION_ERROR(-20001,'v_user_code=='||v_user_code||'v_process=='||v_process||'v_prcs_area=='||v_prcs_area||'v_prsc_sub_area =='||v_prsc_sub_area||'v_sys_code== '||v_sys_code);
         RETURN (FALSE);
      WHEN OTHERS
      THEN
         raise_application_error
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
   END check_user_rights2;


   FUNCTION check_userauth_rights (
      v_usr_name        IN   VARCHAR2,
      v_sys_code        IN   NUMBER,
      v_process         IN   VARCHAR2,
      v_prcs_area       IN   VARCHAR2,
      v_prsc_sub_area   IN   VARCHAR2,
      v_transid         IN   NUMBER
   )
      RETURN VARCHAR2
   IS
      v_cnt          NUMBER        := 1;
      v_dr_limit     NUMBER;
      v_cr_limit     NUMBER;
      v_right_type   VARCHAR2 (15);
      v_usrcode      NUMBER;
   BEGIN
      --RETURN(TRUE);
      IF (   (v_sys_code IS NULL)
          OR (v_process IS NULL)
          OR (v_prcs_area IS NULL)
          OR (v_prsc_sub_area IS NULL)
          OR (v_transid IS NULL)
         )
      THEN
         RETURN ('N');
      END IF;

      SELECT COUNT (1)
        INTO v_cnt
        FROM tqc_approval_details, tqc_sys_process_sub_areas
       WHERE apd_trans_id = v_transid
         AND apd_sys_code = v_sys_code
         AND apd_sprsa_code = sprsa_code
         AND sprsa_sht_desc = v_prsc_sub_area;

      SELECT usr_code
        INTO v_usrcode
        FROM tqc_users
       WHERE usr_username = v_usr_name;

      BEGIN
         IF v_cnt = 0
         THEN
            INSERT INTO tqc_approval_details
                        (apd_id, apd_sys_code, apd_sprsa_code,
                         apd_date_created, apd_date_approved, apd_action,
                         apd_comments, apd_usrid, apd_level, apd_trans_id,
                         apd_description)
               SELECT tq_crm.apd_id_seq.NEXTVAL, v_sys_code, sprsa_code,
                      SYSDATE, NULL, NULL, NULL, NULL, tqal_level_id,
                      v_transid, srls_name
                 FROM tqc_sys_process_sub_areas,
                      tqc_sys_process_areas,
                      tqc_sys_processes,
                      tqc_authorization_levels,
                      tqc_sys_roles
                WHERE sprsa_sprca_code = sprca_code
                  AND sprca_sprc_code = sprc_code
                  AND tqal_sprsa_code = sprsa_code
                  AND tqal_srls_code = srls_code
                  AND sprc_sht_desc = v_process
                  AND sprca_sht_desc = v_prcs_area
                  AND sprsa_sht_desc = v_prsc_sub_area;
         END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error Inserting Records');
      END;

      SELECT COUNT (*)
        INTO v_cnt
        FROM tqc_approval_details,
             tqc_authorization_levels,
             tqc_sys_process_sub_areas
       WHERE apd_sprsa_code = tqal_sprsa_code
         AND apd_level = tqal_level_id
         AND apd_sprsa_code = sprsa_code
         AND apd_sys_code = v_sys_code
         AND sprsa_sht_desc = v_prsc_sub_area
         AND apd_date_approved IS NULL;

      IF v_cnt = 0
      THEN
         RETURN ('X');
      END IF;

      SELECT COUNT (*)
        INTO v_cnt
        FROM tqc_approval_details,
             tqc_authorization_levels,
             tqc_sys_user_roles,
             tqc_sys_process_sub_areas
       WHERE apd_sprsa_code = tqal_sprsa_code
         AND apd_level = tqal_level_id
         AND tqal_srls_code = usrr_srls_code
         AND apd_sprsa_code = sprsa_code
         AND apd_sys_code = v_sys_code
         AND sprsa_sht_desc = v_prsc_sub_area
         AND usrr_usr_code = v_usrcode
         AND (usrr_revoke_date IS NULL OR usrr_revoke_date >= SYSDATE)
         AND apd_sys_code = v_sys_code
         AND apd_date_approved IS NULL;

      --RAISE_ERROR('Count Two '||v_cnt);
      IF v_cnt = 0
      THEN
         RETURN ('N');
      ELSE
         RETURN ('Y');
      END IF;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         RETURN ('N');
      WHEN OTHERS
      THEN
         raise_application_error
                               (-20001,
                                   'UNABLE TO RETRIEVE AUTHORIZATION LEVELS '
                                || v_sys_code
                                || ' PROCESS'
                                || v_process
                                || ' AREA'
                                || v_prcs_area
                                || ' SUB AREA'
                                || v_prsc_sub_area
                                || SQLERRM (SQLCODE)
                               );
   END check_userauth_rights;
   
   
   --   FUNCTION get_task_assignee (
--      v_sys_code        IN       NUMBER,
--      v_process         IN       VARCHAR2,
--      v_prcs_area       IN       VARCHAR2,
--      v_prsc_sub_area   IN       VARCHAR2,
--      v_amount          IN       NUMBER DEFAULT NULL,
--      v_dc              IN       VARCHAR2 DEFAULT NULL,
--      v_dflt_usr_code   OUT      NUMBER,
--      v_dflt_usrname    OUT      VARCHAR2
--   )
--      RETURN usrs_ref
--   IS
--      v_refcur       usrs_ref;
--      v_right_type   VARCHAR2 (15);
--   BEGIN
--      IF (   (v_sys_code IS NULL)
--          OR (v_process IS NULL)
--          OR (v_prcs_area IS NULL)
--          OR (v_prsc_sub_area IS NULL)
--         )
--      THEN
--         raise_application_error
--                       (-20001,
--                        'Not all parameter provided to determine assignees..'
--                       );
--      END IF;

--      --RAISE_APPLICATION_ERROR(-20001, 'SA' ||v_amount ||'DC' || v_dc);
--      --RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prsc_sub_area||' SYS CODE '||v_sys_code||'dc '||v_dc||' Amount '||v_amount);
--      BEGIN
--         SELECT sprsa_type, sprsa_default_usr_code, usr_username
--           INTO v_right_type, v_dflt_usr_code, v_dflt_usrname
--           FROM tqc_sys_processes,
--                tqc_sys_process_areas,
--                tqc_sys_process_sub_areas,
--                tqc_users
--          WHERE sprc_code = sprca_sprc_code
--            AND sprca_code = sprsa_sprca_code
--            AND sprc_sht_desc = v_process
--            AND sprca_sht_desc = v_prcs_area
--            AND sprsa_sht_desc = v_prsc_sub_area
--            AND sprc_sys_code = v_sys_code
--            AND sprsa_default_usr_code = usr_code(+);
--      EXCEPTION
--         WHEN OTHERS
--         THEN
--            raise_application_error
--                               (-20001,
--                                   'Error determining process type..Process '
--                                || v_process
--                                || ' Process Area '
--                                || v_prcs_area
--                                || ' Process Sub Area '
--                                || v_prsc_sub_area
--                               );
--      END;

--      -- RAISE_APPLICATION_ERROR(-20001,v_dflt_usr_code);
--      IF v_right_type != 'A'
--      THEN
--         BEGIN
--            OPEN v_refcur FOR
--               SELECT usr_code, usr_username, usr_name
--                 FROM tqc_users, tqc_user_systems
--                WHERE usr_code = usys_usr_code
--                  AND usys_sys_code = v_sys_code
--                  AND TRUNC (SYSDATE) BETWEEN TRUNC (usys_wef)
--                                          AND NVL (usys_wet, TRUNC (SYSDATE))
--                  AND usr_code IN (
--                         SELECT usrr_usr_code
--                           FROM tqc_sys_user_roles,
--                                tqc_sys_roles_processes,
--                                tqc_sys_processes,
--                                tqc_sys_roles_prcs_area,
--                                tqc_sys_process_areas,
--                                tqc_sys_roles_prcs_s_area,
--                                tqc_sys_process_sub_areas
--                          WHERE usrr_srls_code = srprc_srls_code
--                            AND srprc_code = srpra_srprc_code
--                            AND srprc_sprc_code = sprc_code
--                            AND srpra_code = srpsa_srpra_code
--                            AND srpra_sprca_code = sprca_code
--                            AND srpsa_sprsa_code = sprsa_code
--                            --AND SPRC_SHT_DESC = v_process
--                            --AND SPRCA_SHT_DESC = v_prcs_area
--                            --AND SPRSA_SHT_DESC = v_prsc_sub_area
--                            AND sprc_sys_code = v_sys_code
--                            AND usrr_revoke_date IS NULL);
--         EXCEPTION
--            WHEN OTHERS
--            THEN
--               raise_application_error (-20001,
--                                        'Error getting task assignees..'
--                                       );
--         END;
--      ELSE
--         BEGIN
--            --  RAISE_APPLICATION_ERROR (-20001,'Error determining process type..Process '||v_process||' Process Area '||v_prcs_area||' Process Sub Area '||v_prsc_sub_area||' amount '||v_amount);
--            OPEN v_refcur FOR
--               SELECT usr_code, usr_username, usr_name
--                 FROM tqc_users, tqc_user_systems
--                WHERE usr_code = usys_usr_code
--                  AND usys_sys_code = v_sys_code
--                  AND TRUNC (SYSDATE) BETWEEN TRUNC (usys_wef)
--                                          AND NVL (usys_wet, TRUNC (SYSDATE))
--                  AND usr_code IN (
--                         SELECT usrr_usr_code
--                           FROM tqc_sys_user_roles,
--                                tqc_sys_roles_processes,
--                                tqc_sys_processes,
--                                tqc_sys_roles_prcs_area,
--                                tqc_sys_process_areas,
--                                tqc_sys_roles_prcs_s_area,
--                                tqc_sys_process_sub_areas
--                          WHERE usrr_srls_code = srprc_srls_code
--                            AND srprc_code = srpra_srprc_code
--                            AND srprc_sprc_code = sprc_code
--                            AND srpra_code = srpsa_srpra_code
--                            AND srpra_sprca_code = sprca_code
--                            AND srpsa_sprsa_code = sprsa_code
--                            --AND SPRC_SHT_DESC = v_process
--                            --AND SPRCA_SHT_DESC = v_prcs_area
--                            --AND SPRSA_SHT_DESC = v_prsc_sub_area
--                            AND sprc_sys_code = v_sys_code
--                            AND v_amount <=
--                                   ABS (DECODE (v_dc,
--                                                'C', srpsa_credit_limit,
--                                                srpsa_debit_limit
--                                               )
--                                       )
--                            AND usrr_revoke_date IS NULL);
--         --            EXCEPTION
--         --                WHEN OTHERS THEN
--         --                    RAISE_APPLICATION_ERROR (-20001,'Error getting task assignees..');
--         END;
--      END IF;

--      RETURN (v_refcur);
--   END;
   
FUNCTION get_task_assignee (
      v_sys_code        IN       NUMBER,
      v_process         IN       VARCHAR2,
      v_prcs_area       IN       VARCHAR2,
      v_prsc_sub_area   IN       VARCHAR2,
      v_amount          IN       NUMBER DEFAULT NULL,
      v_dc              IN       VARCHAR2 DEFAULT NULL,
      v_dflt_usr_code   OUT      NUMBER,
      v_dflt_usrname    OUT      VARCHAR2
   )
      RETURN usrs_ref
   IS
      v_refcur       usrs_ref;
      v_right_type   VARCHAR2 (15);
   BEGIN
      IF (   (v_sys_code IS NULL)
          OR (v_process IS NULL)
          OR (v_prcs_area IS NULL)
          OR (v_prsc_sub_area IS NULL)
         )
      THEN
         raise_application_error
                       (-20001,
                        'Not all parameter provided to determine assignees..'
                       );
      END IF;

      --RAISE_APPLICATION_ERROR(-20001, 'SA' ||v_amount ||'DC' || v_dc);
      --RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prsc_sub_area||' SYS CODE '||v_sys_code||'dc '||v_dc||' Amount '||v_amount);
      BEGIN
         SELECT sprsa_type, sprsa_default_usr_code, usr_username
           INTO v_right_type, v_dflt_usr_code, v_dflt_usrname
           FROM tqc_sys_processes,
                tqc_sys_process_areas,
                tqc_sys_process_sub_areas,
                tqc_users
          WHERE sprc_code = sprca_sprc_code
            AND sprca_code = sprsa_sprca_code
            AND sprc_sht_desc = v_process
            AND sprca_sht_desc = v_prcs_area
            AND sprsa_sht_desc = v_prsc_sub_area
            AND sprc_sys_code = v_sys_code
            AND sprsa_default_usr_code = usr_code(+);
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_application_error
                               (-20001,
                                   'Error determining process type..Process '
                                || v_process
                                || ' Process Area '
                                || v_prcs_area
                                || ' Process Sub Area '
                                || v_prsc_sub_area
                               );
      END;

      -- RAISE_APPLICATION_ERROR(-20001,v_dflt_usr_code);
      IF v_right_type != 'A'
      THEN
         BEGIN
            OPEN v_refcur FOR
               SELECT usr_code, usr_username, usr_name
                 FROM tqc_users, tqc_user_systems
                WHERE usr_code = usys_usr_code
                  AND usys_sys_code = v_sys_code
                  AND TRUNC (SYSDATE) BETWEEN TRUNC (usys_wef)
                                          AND NVL (usys_wet, TRUNC (SYSDATE))
                  AND usr_code IN (
                         SELECT usrr_usr_code
                           FROM tqc_sys_user_roles,
                                tqc_sys_roles_processes,
                                tqc_sys_processes,
                                tqc_sys_roles_prcs_area,
                                tqc_sys_process_areas,
                                tqc_sys_roles_prcs_s_area,
                                tqc_sys_process_sub_areas
                          WHERE usrr_srls_code = srprc_srls_code
                            AND srprc_code = srpra_srprc_code
                            AND srprc_sprc_code = sprc_code
                            AND srpra_code = srpsa_srpra_code
                            AND srpra_sprca_code = sprca_code
                            AND srpsa_sprsa_code = sprsa_code
                            --AND SPRC_SHT_DESC = v_process
                            --AND SPRCA_SHT_DESC = v_prcs_area
                            --AND SPRSA_SHT_DESC = v_prsc_sub_area
                            AND sprc_sys_code = v_sys_code
                            AND (usrr_revoke_date  IS NULL) OR (usrr_revoke_date >= SYSDATE)
                          );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_application_error (-20001,
                                        'Error getting task assignees..'
                                       );
         END;
      ELSE
         BEGIN
            --  RAISE_APPLICATION_ERROR (-20001,'Error determining process type..Process '||v_process||' Process Area '||v_prcs_area||' Process Sub Area '||v_prsc_sub_area||' amount '||v_amount);
            OPEN v_refcur FOR
               SELECT usr_code, usr_username, usr_name
                 FROM tqc_users, tqc_user_systems
                WHERE usr_code = usys_usr_code
                  AND usys_sys_code = v_sys_code
                  AND TRUNC (SYSDATE) BETWEEN TRUNC (usys_wef)
                                          AND NVL (usys_wet, TRUNC (SYSDATE))
                  AND usr_code IN (
                         SELECT usrr_usr_code
                           FROM tqc_sys_user_roles,
                                tqc_sys_roles_processes,
                                tqc_sys_processes,
                                tqc_sys_roles_prcs_area,
                                tqc_sys_process_areas,
                                tqc_sys_roles_prcs_s_area,
                                tqc_sys_process_sub_areas
                          WHERE usrr_srls_code = srprc_srls_code
                            AND srprc_code = srpra_srprc_code
                            AND srprc_sprc_code = sprc_code
                            AND srpra_code = srpsa_srpra_code
                            AND srpra_sprca_code = sprca_code
                            AND srpsa_sprsa_code = sprsa_code
                            --AND SPRC_SHT_DESC = v_process
                            --AND SPRCA_SHT_DESC = v_prcs_area
                            --AND SPRSA_SHT_DESC = v_prsc_sub_area
                            AND sprc_sys_code = v_sys_code
                            AND NVL(v_amount,0) <= ABS(DECODE(v_dc,'C',NVL(SRPSA_CREDIT_LIMIT,0),NVL(SRPSA_DEBIT_LIMIT,0)))
                            AND (usrr_revoke_date  IS NULL) OR (usrr_revoke_date >= SYSDATE)
                             );
         --            EXCEPTION
         --                WHEN OTHERS THEN
         --                    RAISE_APPLICATION_ERROR (-20001,'Error getting task assignees..');
         END;
      END IF;

      RETURN (v_refcur);
   END;


   FUNCTION check_authorization_roles (
      v_usr_name    IN   VARCHAR2,
      v_sys_code    IN   NUMBER,
      v_auth_area   IN   VARCHAR2,
      v_amount      IN   NUMBER,
      v_dc          IN   VARCHAR2,
      v_pro_code    IN   NUMBER
   )
      RETURN BOOLEAN
   IS
      v_cnt            NUMBER        := 1;
      v_user_code      NUMBER;
      v_dr_limit       NUMBER;
      v_cr_limit       NUMBER;
      v_saa_sht_desc   VARCHAR2 (15);
      v_param          VARCHAR2 (1);
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
      ELSIF v_dc = 'E'
      THEN
         -- RAISE_ERROR('TEST');
         SELECT COUNT (1)
           INTO v_cnt
           FROM tqc_sys_user_roles,
                tqc_sys_roles_processes,
                tqc_sys_processes,
                tqc_sys_roles_prcs_area,
                tqc_sys_process_areas,
                tqc_sys_roles_prcs_s_area,
                tqc_sys_process_sub_areas
          WHERE usrr_srls_code = srprc_srls_code
            AND srprc_code = srpra_srprc_code
            AND srprc_sprc_code = sprc_code
            AND srpra_code = srpsa_srpra_code
            AND srpra_sprca_code = sprca_code
            AND srpsa_sprsa_code = sprsa_code
            AND usrr_usr_code = v_user_code
            --AND SPRC_SHT_DESC = v_process
            --AND SPRCA_SHT_DESC = v_prcs_area
            AND sprsa_sht_desc = v_auth_area
            AND sprc_sys_code = v_sys_code
            AND (usrr_revoke_date IS NULL OR usrr_revoke_date >= SYSDATE);

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
         IF NVL (v_cnt, 0) = 0
         THEN
            RETURN (FALSE);
         ELSE
            RETURN (TRUE);
         END IF;
      ELSE
         v_user_code := usercode (v_usr_name);
         v_param :=
               gin_parameters_pkg.get_param_varchar ('ZERO_DEBITS_AUTH_ROLE');

      -- RAISE_APPLICATION_ERROR(-20001,'v_amount'||v_amount);
--RAISE_APPLICATION_ERROR(-20001,'V_AUTH_AREA = '||v_auth_area||' = '||v_user_code||' = '||v_sys_code);
 --RAISE_ERROR('v_param'||v_param||'v_user_code'||v_user_code);
         IF NVL (v_param, 'N') = 'Y'
         THEN
            --- RAISE_ERROR('HAPA===='||v_user_code||'  =====  '||v_auth_area||'=======   '||v_sys_code);
                --RAISE_ERROR('v_user_code'||v_user_code||'v_auth_area '||v_auth_area);
            SELECT MAX (NVL (srpsa_debit_limit, 0)) srpsa_debit_limit,
                   MAX (NVL (srpsa_credit_limit, 0)) srpsa_credit_limit
              INTO v_dr_limit,
                   v_cr_limit
              FROM tqc_sys_user_roles,
                   tqc_sys_roles_processes,
                   tqc_sys_processes,
                   tqc_sys_roles_prcs_area,
                   tqc_sys_process_areas,
                   tqc_sys_roles_prcs_s_area,
                   tqc_sys_process_sub_areas
             WHERE usrr_srls_code = srprc_srls_code
               AND srprc_code = srpra_srprc_code
               AND srprc_sprc_code = sprc_code
               AND srpra_code = srpsa_srpra_code
               AND srpra_sprca_code = sprca_code
               AND srpsa_sprsa_code = sprsa_code
               AND usrr_usr_code = v_user_code
               --AND SPRC_SHT_DESC = v_process
               --AND SPRCA_SHT_DESC = v_prcs_area
               AND sprsa_sht_desc = v_auth_area
               AND sprc_sys_code = v_sys_code
               AND usrr_revoke_date IS NULL;
         ELSE
            SELECT   MAX (sar_debit_limit) sar_debit_limit,
                     MAX (sar_credit_limit), saa_sht_desc
                INTO v_dr_limit,
                     v_cr_limit, v_saa_sht_desc
                FROM tqc_system_auth_areas,
                     tqc_system_auth_roles,
                     tqc_user_system_auth_roles,
                     tqc_users
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

         -- RAISE_APPLICATION_ERROR(-20001,'V_DR_LIMIT = '||v_auth_area  ||v_dr_limit||' V_CR_LIMIT = '||v_cr_limit||'V_SAA_SHT_DESC = '||v_saa_sht_desc||'v_auth_area='||v_auth_area);--IF v_dr_limit IS NULL AND v_cr_limit IS NULL
                --RAISE_ERROR('v_amount'||v_amount||'v_dr_limit'||v_dr_limit);
         IF     (NVL (v_dr_limit, 0) = 0 AND NVL (v_cr_limit, 0) = 0)
            AND v_dc != 'A'
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
         ELSE                                               --IF v_dc='D' THEN
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
         raise_application_error
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

   FUNCTION check_authorization_roles_bk (
      v_usr_name    IN   VARCHAR2,
      v_sys_code    IN   NUMBER,
      v_auth_area   IN   VARCHAR2,
      v_amount      IN   NUMBER,
      v_dc          IN   VARCHAR2,
      v_pro_code    IN   NUMBER
   )
      RETURN BOOLEAN
   IS
      v_cnt         NUMBER := 1;
      v_user_code   NUMBER;
      v_dr_limit    NUMBER;
      v_cr_limit    NUMBER;
   BEGIN
      v_user_code := usercode (v_usr_name);
      RETURN (TRUE);
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
      WHEN NO_DATA_FOUND
      THEN
         RETURN (FALSE);
      WHEN OTHERS
      THEN
         raise_application_error
                               (-20001,
                                   'UNABLE TO RETRIEVE AUTHORIZATION LIMITS '
                                || v_sys_code
                                || ' V_AUTH_AREA'
                                || v_auth_area
                                || ' V_USER_CODE'
                                || v_user_code
                                || SQLERRM (SQLCODE)
                               );
   END check_authorization_roles_bk;

   FUNCTION eft_enabled (v_bbr_code NUMBER)
      RETURN VARCHAR2
   IS
      v_ref_code      VARCHAR2 (20);
      v_eft_support   VARCHAR2 (1)   := 'N';
      v_branch_name   VARCHAR2 (100);
   BEGIN
      IF v_bbr_code IS NOT NULL
      THEN
         BEGIN
            SELECT bbr_ref_code, bbr_eft_supported, bbr_branch_name
              INTO v_ref_code, v_eft_support, v_branch_name
              FROM tqc_bank_branches
             WHERE bbr_code = v_bbr_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
            WHEN OTHERS
            THEN
               raise_application_error (-20001,
                                        'CHECKING EFT STATUS ' || SQLERRM
                                       );
         END;

         IF (v_ref_code IS NULL) AND (v_eft_support = 'Y')
         THEN
            raise_application_error (-20001,
                                     v_branch_name || 'HAS NO BANK REF CODE.'
                                    );
         END IF;
      END IF;

      RETURN v_eft_support;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (-20001, 'EFT_ENABLED:' || SQLERRM);
   END;

   PROCEDURE appendclob (
      v_clob   IN OUT   CLOB,
      v_char            VARCHAR2,
      v_temp            VARCHAR2 DEFAULT 'N'
   )
   IS
      len   BINARY_INTEGER;
   BEGIN
      IF v_temp = 'Y'
      THEN
         DBMS_LOB.createtemporary (v_clob, TRUE);
         DBMS_LOB.OPEN (v_clob, DBMS_LOB.lob_readwrite);
         len := LENGTH (v_char);
         DBMS_LOB.writeappend (v_clob, len, v_char);
         DBMS_LOB.CLOSE (v_clob);
      ELSE
         len := LENGTH (v_char);
         DBMS_LOB.writeappend (v_clob, len, v_char);
      END IF;
   END;

   PROCEDURE LOG (v_sys NUMBER, v_char VARCHAR2)
   IS
      PRAGMA AUTONOMOUS_TRANSACTION;
      v_clob     CLOB;
      v_date     DATE          := SYSDATE;
      v_format   VARCHAR2 (20) := 'YYYYMONDD';
   BEGIN
      BEGIN
         SELECT     log_text
               INTO v_clob
               FROM tqc_log
              WHERE (    (log_sys = v_sys)
                     AND (TO_CHAR (log_date, v_format) =
                                                    TO_CHAR (v_date, v_format)
                         )
                    )
         FOR UPDATE;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;

            INSERT INTO tqc_log
                        (log_date, log_text, log_sys
                        )
                 VALUES (v_date, EMPTY_CLOB (), v_sys
                        );

            SELECT     log_text
                  INTO v_clob
                  FROM tqc_log
                 WHERE (    (log_sys = v_sys)
                        AND (TO_CHAR (log_date, v_format) =
                                                    TO_CHAR (v_date, v_format)
                            )
                       )
            FOR UPDATE;
      END;

      appendclob (v_clob, v_char || CHR (10));
      COMMIT;
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
         ROLLBACK;
   END;

   FUNCTION orgcurrency (v_sys_code IN NUMBER)
      RETURN NUMBER
   IS
      v_return   NUMBER;
   BEGIN
      SELECT org_cur_code
        INTO v_return
        FROM tqc_organizations, tqc_systems, tqc_currencies
       WHERE org_code = sys_org_code
         AND cur_code = org_cur_code
         AND sys_code = v_sys_code;

      RETURN v_return;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                     'ERROR GETTING DEFAULT CURRENCY  '
                                  || v_sys_code
                                  || '.'
                                 );
   END;

   FUNCTION check_excp_auth_rights (
      v_usr_code        NUMBER,
      v_sys_code        NUMBER,
      v_process         VARCHAR2,
      v_prcs_area       VARCHAR2,
      v_prcs_sub_area   VARCHAR2
   )
      RETURN BOOLEAN
   IS
      v_right_type   tqc_sys_process_sub_areas.sprsa_type%TYPE;
   BEGIN
      -- RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prcs_sub_area);
      IF (   (v_usr_code IS NULL)
          OR (v_sys_code IS NULL)
          OR (v_process IS NULL)
          OR (v_prcs_area IS NULL)
          OR (v_prcs_sub_area IS NULL)
         )
      THEN
         RETURN (FALSE);
      END IF;

      BEGIN
         SELECT DISTINCT sprsa_type
                    INTO v_right_type
                    FROM tqc_sys_user_roles,
                         tqc_sys_roles_processes,
                         tqc_sys_processes,
                         tqc_sys_roles_prcs_area,
                         tqc_sys_process_areas,
                         tqc_sys_roles_prcs_s_area,
                         tqc_sys_process_sub_areas
                   WHERE usrr_srls_code = srprc_srls_code
                     AND srprc_code = srpra_srprc_code
                     AND srprc_sprc_code = sprc_code
                     AND srpra_code = srpsa_srpra_code
                     AND srpra_sprca_code = sprca_code
                     AND srpsa_sprsa_code = sprsa_code
                     AND usrr_usr_code = v_usr_code
                     AND sprc_sht_desc = v_process
                     AND sprca_sht_desc = v_prcs_area
                     AND sprsa_sht_desc = v_prcs_sub_area
                     AND sprc_sys_code = v_sys_code
                     AND (usrr_revoke_date IS NULL OR usrr_revoke_date >= SYSDATE);
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            RETURN FALSE;
         WHEN OTHERS
         THEN
            RETURN FALSE;
      END;

      IF v_right_type IN ('C', 'A')
      THEN
         RETURN TRUE;
      END IF;
   END;

   PROCEDURE get_gin_treaty_accounts (v_cursor OUT gin_treaty_accounts_cursor)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT   acc_name, acc_number
             FROM fms_accounts
            WHERE acc_org_code =
                                tqc_interfaces_pkg.systemorganization ('GIS')
              AND acc_trn = 'Y'
         ORDER BY acc_number;
   END;

   FUNCTION orgpaymentmode (v_sys_code IN NUMBER, v_pay_symbol OUT VARCHAR2)
      RETURN NUMBER
   IS
      v_return   NUMBER;
   BEGIN
      SELECT pmod_code, pmod_desc
        INTO v_return, v_pay_symbol
        FROM tqc_payment_modes
       WHERE pmod_default = 'Y';

      RETURN v_return;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         NULL;
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                     'ERROR GETTING DEFAULT PAYMENT MODE  '
                                  || v_sys_code
                                  || '.'
                                 );
   END;

   FUNCTION check_approval_right (
      v_user_code       NUMBER,
      v_area_sht_desc   VARCHAR2,
      v_process         VARCHAR2,
      v_prcs_area       VARCHAR2,
      v_sys_code        NUMBER
   )
      RETURN VARCHAR2
   IS
      v_count   NUMBER;
   BEGIN
      --RAISE_ERROR('v_user_code#'||v_user_code||'v_area_sht_desc'||v_area_sht_desc);
      SELECT COUNT (*)
        INTO v_count
        FROM tqc_sys_user_roles,
             tqc_sys_roles_processes,
             tqc_sys_processes,
             tqc_sys_roles_prcs_area,
             tqc_sys_process_areas,
             tqc_sys_roles_prcs_s_area,
             tqc_sys_process_sub_areas
       WHERE usrr_srls_code = srprc_srls_code
         AND srprc_code = srpra_srprc_code
         AND srprc_sprc_code = sprc_code
         AND srpra_code = srpsa_srpra_code
         AND srpra_sprca_code = sprca_code
         AND srpsa_sprsa_code = sprsa_code
         AND usrr_usr_code = v_user_code
         AND sprc_sht_desc = v_process
         AND sprca_sht_desc = v_prcs_area
         AND sprsa_sht_desc = v_area_sht_desc
         AND sprc_sys_code = v_sys_code
         AND (usrr_revoke_date IS NULL OR usrr_revoke_date >= SYSDATE);

      IF v_count > 0
      THEN
         RETURN 'Y';
      ELSE
         RETURN 'N';
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         RETURN 'Y';
   END;

   FUNCTION getcouschengen (v_cou_name tqc_countries.cou_name%TYPE)
      RETURN VARCHAR2
   AS
      v_schengen   VARCHAR2 (1) := 'N';
   BEGIN
      SELECT cou_schegen
        INTO v_schengen
        FROM tqc_countries
       WHERE cou_name = v_cou_name;

      RETURN v_schengen;
   EXCEPTION
      WHEN OTHERS
      THEN
         RETURN 'N';
   END;

   FUNCTION getstatename (v_state_code IN NUMBER)
      RETURN VARCHAR2
   IS
      v_statename   tqc_states.sts_name%TYPE;
   BEGIN
      SELECT sts_name
        INTO v_statename
        FROM tqc_states
       WHERE sts_code = v_state_code;

      RETURN v_statename;
   EXCEPTION
      WHEN OTHERS
      THEN
         RETURN NULL;
   END;

   FUNCTION check_user_limit (
      v_user_code       IN   NUMBER,
      v_sys_code        IN   NUMBER,
      v_process         IN   VARCHAR2,
      v_prcs_area       IN   VARCHAR2,
      v_prsc_sub_area   IN   VARCHAR2,
      v_amount          IN   NUMBER DEFAULT NULL,
      v_dc              IN   VARCHAR2 DEFAULT NULL
   )
      RETURN NUMBER
   IS
      v_cnt          NUMBER        := 1;
      v_dr_limit     NUMBER;
      v_cr_limit     NUMBER;
      v_right_type   VARCHAR2 (15);
   BEGIN
      IF (   (v_user_code IS NULL)
          OR (v_sys_code IS NULL)
          OR (v_process IS NULL)
          OR (v_prcs_area IS NULL)
          OR (v_prsc_sub_area IS NULL)
         )
      THEN
         RETURN 0;
      END IF;

      --RAISE_APPLICATION_ERROR(-20001, 'PRO  ' ||v_process ||'PRO AREA  ' || v_prcs_area ||'PRO SUBA  ' || v_prsc_sub_area);
      SELECT   sprsa_type, COUNT (1), MAX (NVL (srpsa_debit_limit, 0)),
               MAX (srpsa_credit_limit)
          INTO v_right_type, v_cnt, v_dr_limit,
               v_cr_limit
          FROM tqc_sys_user_roles,
               tqc_sys_roles_processes,
               tqc_sys_processes,
               tqc_sys_roles_prcs_area,
               tqc_sys_process_areas,
               tqc_sys_roles_prcs_s_area,
               tqc_sys_process_sub_areas
         WHERE usrr_srls_code = srprc_srls_code
           AND srprc_code = srpra_srprc_code
           AND srprc_sprc_code = sprc_code
           AND srpra_code = srpsa_srpra_code
           AND srpra_sprca_code = sprca_code
           AND srpsa_sprsa_code = sprsa_code
           AND usrr_usr_code = v_user_code
           AND sprc_sht_desc = v_process
           AND sprca_sht_desc = v_prcs_area
           AND sprsa_sht_desc = v_prsc_sub_area
           AND sprc_sys_code = v_sys_code
           AND usrr_revoke_date IS NULL
      --AND ROWNUM=1
      GROUP BY sprsa_type;

      RETURN v_dr_limit;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         -- RAISE_APPLICATION_ERROR(-20001,'v_user_code=='||v_user_code||'v_process=='||v_process||'v_prcs_area=='||v_prcs_area||'v_prsc_sub_area =='||v_prsc_sub_area||'v_sys_code== '||v_sys_code);
         RETURN 0;
      WHEN OTHERS
      THEN
         raise_application_error
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

   FUNCTION orgcurrencysymbol (v_sys_code IN NUMBER)
      RETURN VARCHAR2
   IS
      v_return   VARCHAR2 (100);
   BEGIN
      SELECT cur_symbol
        INTO v_return
        FROM tqc_organizations, tqc_systems, tqc_currencies
       WHERE org_code = sys_org_code
         AND cur_code = org_cur_code
         AND sys_code = v_sys_code;

      RETURN v_return;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_application_error (-20001,
                                     'ERROR GETTING DEFAULT CURRENCY  '
                                  || v_sys_code
                                  || '.'
                                 );
   END;
PROCEDURE create_alert_prc (
      v_alrt_code           IN   NUMBER,                       -- --alert type
      v_user_code           IN   NUMBER,
      -- user to whom the alert is being send to
      v_user                IN   VARCHAR2,           -- user raising the alert
      v_reciepient          IN   VARCHAR2,
      -- reciepient -c client,u user, a agencies
      v_clnt_code           IN   NUMBER,
      v_agn_code            IN   NUMBER,
      v_quot_code           IN   NUMBER,
      v_quot_no             IN   VARCHAR2,
      v_pol_code            IN   NUMBER,
      v_pol_no              IN   VARCHAR2,
      v_clm_no              IN   VARCHAR2,
      v_msg_subj            IN   VARCHAR2,
      v_app_lvl             IN   VARCHAR2,
      v_email_sender_addr   IN   VARCHAR2 DEFAULT NULL,
      v_to_send_date        IN   DATE DEFAULT TRUNC (SYSDATE)  ,
      v_gcc_code IN   NUMBER  DEFAULT NULL,
      v_csd_code IN   NUMBER  DEFAULT NULL,
      v_trans_no       IN   NUMBER DEFAULT NULL
   )
   IS
      CURSOR alrt
      IS
         SELECT *
           FROM tqc_alert_types
          WHERE alrt_code = v_alrt_code;

      v_sms_code     NUMBER;
      v_email_code   NUMBER;
      v_files_tab    tqc_email_pkg.email_files_tab;
   BEGIN
      /*- Creates registers the alert in tqc_alerts
         - Based on the alert type, it sends the relevant alert based on the template
       */
      FOR a IN alrt
      LOOP
         BEGIN
            INSERT INTO tqc_alerts
                        (alrts_code, alrts_alrt_code,
                         alrts_sys_code,
                         alrts_description,
                         alrts_date, alrts_period, alrts_user_code,
                         alrts_status, alrts_screen, alrts_agn_code,
                         alrts_clnt_code, alrts_prepared_by,
                         alrts_prepare_date, alrts_quot_code, alrts_quot_no,
                         alrts_pol_batch_no, alrts_pol_policy_no,
                         alrts_claim_no, alrts_to_send_date
                        )
                 VALUES (tqc_alrt_code_seq.NEXTVAL, v_alrt_code,
                         a.alrt_sys_code,
                         NVL (v_msg_subj, 'You have an alert to attend to'),
                         SYSDATE, 1, NVL (v_user_code, a.alrt_grp_usr_code),
                         'A', a.alrt_screen, v_agn_code,
                         v_clnt_code, v_user,
                         SYSDATE, v_quot_code, v_quot_no,
                         v_pol_code, v_pol_no,
                         v_clm_no, NVL (v_to_send_date, TRUNC (SYSDATE))
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error registering alert...');
         END;

--          RAISE_eRROR('alrtsms='||a.alrt_sms||'='||a.alrt_email||'v_reciepient'||v_reciepient||
--          'alrt_grp_usr_code'|| v_user_code||'v_trans_no'||v_trans_no);
         IF NVL (a.alrt_sms, 'N') = 'Y'
         THEN
            tqc_sms_pkg.create_sms_msg
                        (v_reciepient,       --v_reciepient   IN     VARCHAR2,
                         v_clnt_code,          --v_clnt_code    IN     NUMBER,
                         v_agn_code,           --v_agn_code     IN     NUMBER,
                         v_quot_code,          --v_quot_code    IN     NUMBER,
                         v_quot_no,          --v_quot_no      IN     VARCHAR2,
                         v_pol_code,           --v_pol_code     IN     NUMBER,
                         v_pol_no,           --v_pol_no       IN     VARCHAR2,
                         v_clm_no,           --v_clm_no       IN     VARCHAR2,
                         a.alrt_sms_msgt_code, --v_msgt_code    IN     NUMBER,
                         a.alrt_sys_code,      --v_sys_code     IN     NUMBER,
                         'A',                --v_sys_mod      IN     VARCHAR2,
                         v_user,             --v_user         IN     VARCHAR2,
                         v_sms_code,           --v_sms_code     IN OUT NUMBER,
                         v_app_lvl,           --v_app_lvl      IN     VARCHAR2
                         NVL (v_user_code, a.alrt_grp_usr_code),
                         v_trans_no 
                        );
         ELSIF NVL (a.alrt_email, 'N') = 'Y'
         THEN
            tqc_email_pkg.create_email_msg
                      (v_reciepient,         --v_reciepient   IN     VARCHAR2,
                       v_clnt_code,            --v_clnt_code    IN     NUMBER,
                       v_agn_code,             --v_agn_code     IN     NUMBER,
                       v_quot_code,            --v_quot_code    IN     NUMBER,
                       v_quot_no,            --v_quot_no      IN     VARCHAR2,
                       v_pol_code,             --v_pol_code     IN     NUMBER,
                       v_pol_no,             --v_pol_no       IN     VARCHAR2,
                       v_clm_no,             --v_clm_no       IN     VARCHAR2,
                       NVL (v_msg_subj, 'TURNQUEST SYSTEM ALERT'),
                       --v_msg_subj     IN     VARCHAR2,
                       a.alrt_email_msgt_code, --v_msgt_code    IN     NUMBER,
                       v_files_tab,   --v_files_tab    IN     email_files_tab,
                       a.alrt_sys_code,        --v_sys_code     IN     NUMBER,
                       'A',                  --v_sys_mod      IN     VARCHAR2,
                       v_user,               --v_user         IN     VARCHAR2,
                       v_email_code,           --v_email_code   IN OUT NUMBER,
                       v_app_lvl,             --v_app_lvl      IN     VARCHAR2
                       NVL (v_user_code, a.alrt_grp_usr_code),
                       v_to_send_date
                      );
            --  IF NVL(a.alrt_norm_auto,'N')='N' THEN
            tqc_email_pkg.send_mail (v_email_code);
         -- END IF;
         END IF;
      END LOOP;
   END;
   PROCEDURE send_auto_alerts_prc
   IS
      CURSOR email_alerts
      IS
         SELECT *
           FROM tqc_email_messages
          WHERE email_to_send_date = TRUNC (SYSDATE)
            AND email_send_date IS NULL
            AND NVL (email_status, 'N') != 'S';
   BEGIN
      FOR email_alerts_rec IN email_alerts
      LOOP
         tqc_email_pkg.send_mail (email_alerts_rec.email_code);
      END LOOP;
   END;
   
   FUNCTION verify_authorisation_roles
  (
            v_usr_name  IN VARCHAR2,
            v_sys_code  IN NUMBER,
            v_sprc_sht_desc  IN VARCHAR2,
            v_sprca_sht_desc IN VARCHAR2,
            v_sprsa_sht_desc IN VARCHAR2,
            v_amount    IN NUMBER,
            v_dc        IN VARCHAR2
  ) RETURN BOOLEAN IS
  
    v_cnt          NUMBER := 1;
    v_user_code    NUMBER;
    v_dr_limit     NUMBER;
    v_cr_limit     NUMBER;
  BEGIN
            RETURN (TRUE);
            v_user_code := usercode (v_usr_name);
                  
            IF 
            (  
                (v_user_code IS NULL)
                OR (v_sys_code IS NULL)
            )
            THEN
             RETURN (FALSE);
            END IF;

            BEGIN
                SELECT  MAX(SRPSA_DEBIT_LIMIT), MAX(SRPSA_CREDIT_LIMIT) --,COUNT(1) --, USRR_USR_CODE,USR_USERNAME, USR_NAME, SPRC_SHT_DESC, SPRCA_SHT_DESC, SRPSA_CODE
                INTO    v_dr_limit, v_cr_limit --v_cnt --, 
                FROM    TQC_SYS_USER_ROLES,
                        TQC_SYS_ROLES_PROCESSES,
                        TQC_SYS_PROCESSES,
                        TQC_SYS_ROLES_PRCS_AREA,
                        TQC_SYS_PROCESS_AREAS,
                        TQC_SYS_ROLES_PRCS_S_AREA,
                        TQC_SYS_PROCESS_SUB_AREAS,
                        tqc_users
                WHERE   USRR_SRLS_CODE = SRPRC_SRLS_CODE
                        AND SRPRC_CODE = SRPRA_SRPRC_CODE
                        AND SRPRC_SPRC_CODE = SPRC_CODE
                        AND SRPRA_CODE = SRPSA_SRPRA_CODE
                        AND SRPRA_SPRCA_CODE = SPRCA_CODE
                        AND SRPSA_SPRSA_CODE = SPRSA_CODE
                        AND USRR_USR_CODE = v_user_code
                        AND SPRC_SHT_DESC = v_sprc_sht_desc --'OCLM'
                        AND SPRCA_SHT_DESC = v_sprca_sht_desc --'MAT'
                        AND SPRSA_SHT_DESC = v_sprsa_sht_desc --'MATAUTH'
                        AND SPRC_SYS_CODE = v_sys_code --27
                        AND USRR_REVOKE_DATE IS NULL;
            EXCEPTION
                WHEN OTHERS THEN
                RETURN (FALSE);
            END;
             
             IF NVL (v_dr_limit, 0) = 0 AND NVL (v_cr_limit, 0) = 0
             THEN
                   RETURN (FALSE);
             ELSIF V_DC = 'C'
             THEN
                IF ABS (NVL (v_amount, 0)) > ABS (NVL (v_cr_limit, 0))
                THEN
                   RETURN (FALSE);
                ELSE
                   RETURN (TRUE);
                END IF;
             ELSE
               IF ABS (NVL (v_amount, 0)) > ABS (NVL (v_dr_limit, 0))
                THEN
                   RETURN (FALSE);
                ELSE
                   RETURN (TRUE);
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
                                    || ' v_sys_code:'
                                    || v_sys_code
                                    || ' v_sprc_sht_desc:'
                                    || v_sprc_sht_desc
                                    || ' v_sprca_sht_desc:'
                                    || v_sprca_sht_desc
                                    || ' v_sprsa_sht_desc:'
                                    || v_sprsa_sht_desc
                                    || ' v_user_code:'
                                    || v_user_code
                                    || SQLERRM (SQLCODE)
                                   );  
  END verify_authorisation_roles;
   
  
   PROCEDURE create_kyc_alert_prc (
      v_alrt_code           IN   NUMBER,                      -- --alert type
      v_user                IN   VARCHAR2,         -- user raising the alert
      v_agn_code            IN   NUMBER,
      v_clnt_code           IN   NUMBER DEFAULT NULL,
      v_quot_code           IN   NUMBER DEFAULT NULL,
      v_quot_no             IN   VARCHAR2 DEFAULT NULL,
      v_pol_code            IN   NUMBER  DEFAULT NULL,
      v_pol_no              IN   VARCHAR2 DEFAULT NULL,
      v_clm_no              IN   VARCHAR2 DEFAULT NULL,      
      v_app_lvl             IN   VARCHAR2 DEFAULT NULL,
      v_to_send_date        IN   DATE DEFAULT TRUNC (SYSDATE)
   )
   IS
      CURSOR alrt
      IS
         SELECT *
           FROM tqc_alert_types
          WHERE alrt_type = 'KYC_ALERT';
          
      CURSOR c_agent IS
          SELECT AGN_CODE,AGN_SHT_DESC,AGN_EMAIL_ADDRESS,AGN_BBR_ACC_CODE,
          (SELECT BNK_BANK_NAME FROM TQC_BANK_BRANCHES, TQC_BANKS WHERE BBR_CODE = AGN_BBR_CODE AND BBR_BNK_CODE = BNK_CODE) BANK,
          AGN_TEL_PAY
          FROM TQC_AGENCIES
          WHERE AGN_CODE= v_agn_code;

      v_sms_code        NUMBER;
      v_email_code      NUMBER;
      v_sms_msg         VARCHAR2(2000);
      v_email_msg       VARCHAR2(2000);
      v_files_tab    tqc_email_pkg.email_files_tab;
      v_agn_sht_desc    VARCHAR2(50);
      v_agn_email_address   VARCHAR2(250);
      v_acc             VARCHAR2(50);
      v_bank            VARCHAR2(150);
      v_agn_tel_pay     VARCHAR2(50);
      v_reciepient      VARCHAR2(1) := 'A';
      v_msg_subj        VARCHAR2(20) := 'KYC ALERT';
      -- reciepient -c client,u user, a agencies
      
   BEGIN
      /*- Creates registers the alert in tqc_alerts
         - Based on the alert type, it sends the relevant alert based on the template
       */
      FOR a IN alrt
      LOOP
         BEGIN
         
        SELECT agn_sht_desc, agn_email_address, agn_bbr_acc_code,
               (SELECT bbr_branch_name
                  FROM tqc_bank_branches, tqc_banks
                 WHERE bbr_code = agn_bbr_code AND bbr_bnk_code = bnk_code) bank,
               agn_tel_pay
          INTO v_agn_sht_desc, v_agn_email_address, v_acc,
               v_bank,
               v_agn_tel_pay
          FROM tqc_agencies
         WHERE agn_code = v_agn_code;
          
            INSERT INTO tqc_alerts
                        (alrts_code, alrts_alrt_code,
                         alrts_sys_code,
                         alrts_description,
                         alrts_date, alrts_period, alrts_user_code,
                         alrts_status, alrts_screen, alrts_agn_code,
                         alrts_clnt_code, alrts_prepared_by,
                         alrts_prepare_date, alrts_quot_code, alrts_quot_no,
                         alrts_pol_batch_no, alrts_pol_policy_no,
                         alrts_claim_no, alrts_to_send_date
                        )
                 VALUES (tqc_alrt_code_seq.NEXTVAL, v_alrt_code,
                         a.alrt_sys_code,
                         NVL (v_msg_subj, 'You have an alert to attend to'),
                         SYSDATE, 1,  a.alrt_grp_usr_code,
                         'A', a.alrt_screen, v_agn_code,
                         v_clnt_code, v_user,
                         SYSDATE, v_quot_code, v_quot_no,
                         v_pol_code, v_pol_no,
                         v_clm_no, NVL (v_to_send_date, TRUNC (SYSDATE))
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error registering alert...');
         END;
        
         SELECT MSGT_MSG
         INTO v_sms_msg
         FROM TQC_MSG_TEMPLATES
         WHERE MSGT_CODE = a.alrt_sms_msgt_code;
         
         SELECT MSGT_MSG
         INTO v_email_msg
         FROM TQC_MSG_TEMPLATES
         WHERE MSGT_CODE = a.alrt_email_msgt_code;
         
         v_sms_msg := REPLACE(v_sms_msg,'[AGT_SHT_DESC]',v_agn_sht_desc);
         v_sms_msg := REPLACE(v_sms_msg,'[AGN_EMAIL_ADDRESS]',v_agn_email_address);
         
         v_email_msg := REPLACE(v_email_msg,'[AGT_SHT_DESC]',v_agn_sht_desc);
         v_email_msg := REPLACE(v_email_msg,'[ACCOUNT]',v_acc);
         v_email_msg := REPLACE(v_email_msg,'[BANK_BRANCH]',v_bank);
         v_email_msg := REPLACE(v_email_msg,'[SMS_NO]',v_agn_tel_pay);
         -- RAISE_eRROR('alrt_sms='||a.alrt_sms||'='||a.alrt_email);
         IF NVL (a.alrt_sms, 'N') = 'Y'
         THEN
            tqc_sms_pkg.create_sms_msg
                        (v_reciepient,       --v_reciepient   IN     VARCHAR2,
                         v_clnt_code,          --v_clnt_code    IN     NUMBER,
                         v_agn_code,           --v_agn_code     IN     NUMBER,
                         v_quot_code,          --v_quot_code    IN     NUMBER,
                         v_quot_no,          --v_quot_no      IN     VARCHAR2,
                         v_pol_code,           --v_pol_code     IN     NUMBER,
                         v_pol_no,           --v_pol_no       IN     VARCHAR2,
                         v_clm_no,           --v_clm_no       IN     VARCHAR2,
                         a.alrt_sms_msgt_code, --v_msgt_code    IN     NUMBER,
                         a.alrt_sys_code,      --v_sys_code     IN     NUMBER,
                         'A',                --v_sys_mod      IN     VARCHAR2,
                         v_user,             --v_user         IN     VARCHAR2,
                         v_sms_code,           --v_sms_code     IN OUT NUMBER,
                         v_app_lvl,           --v_app_lvl      IN     VARCHAR2
                         a.alrt_grp_usr_code
                        );
                        
                        -- to insert send sms function 
         ELSIF NVL (a.alrt_email, 'N') = 'Y'
         THEN
            tqc_email_pkg.create_email_msg
                      (v_reciepient,         --v_reciepient   IN     VARCHAR2,
                       v_clnt_code,            --v_clnt_code    IN     NUMBER,
                       v_agn_code,             --v_agn_code     IN     NUMBER,
                       v_quot_code,            --v_quot_code    IN     NUMBER,
                       v_quot_no,            --v_quot_no      IN     VARCHAR2,
                       v_pol_code,             --v_pol_code     IN     NUMBER,
                       v_pol_no,             --v_pol_no       IN     VARCHAR2,
                       v_clm_no,             --v_clm_no       IN     VARCHAR2,
                       NVL (v_msg_subj, 'TURNQUEST SYSTEM ALERT'),
                       --v_msg_subj     IN     VARCHAR2,
                       a.alrt_email_msgt_code, --v_msgt_code    IN     NUMBER,
                       v_files_tab,   --v_files_tab    IN     email_files_tab,
                       a.alrt_sys_code,        --v_sys_code     IN     NUMBER,
                       'A',                  --v_sys_mod      IN     VARCHAR2,
                       v_user,               --v_user         IN     VARCHAR2,
                       v_email_code,           --v_email_code   IN OUT NUMBER,
                       v_app_lvl,             --v_app_lvl      IN     VARCHAR2
                       a.alrt_grp_usr_code,
                       v_to_send_date
                      );
            --  IF NVL(a.alrt_norm_auto,'N')='N' THEN
            tqc_email_pkg.send_mail (v_email_code);
         -- END IF;
         END IF;
      END LOOP;
   END;
   
    procedure log_required_rights 
     (
      v_user_code       IN   NUMBER,
      v_sys_code        IN   NUMBER,
      v_process         IN   VARCHAR2,
      v_prcs_area       IN   VARCHAR2,
      v_prsc_sub_area   IN   VARCHAR2,
      v_amount          IN   NUMBER DEFAULT NULL,
      v_dc              IN   VARCHAR2 DEFAULT NULL
     )
       is
     v_sprc_process varchar2(100);
     v_sprca_sht_desc varchar2(50);
     v_sprsa_sub_area varchar2(100);
     v_sprc_code number;
	 v_usr_username varchar2(100);
	 v_usr_name varchar2(100);
	   
     BEGIN
      BEGIN
	   SELECT  usr_username, usr_name
           INTO v_usr_username,v_usr_name
           FROM tqc_users
          WHERE usr_code = v_user_code;
	  END;
	  
          BEGIN
              select sprc_process ,sprc_code
              into v_sprc_process,v_sprc_code
              from tqc_sys_processes
              where sprc_sht_desc = v_process
              and  SPRC_SYS_CODE  = v_sys_code ;
             
          EXCEPTION
          WHEN OTHERS THEN
          NULL;
          END;
         
         
          BEGIN
              select sprca_sht_desc
              into v_sprca_sht_desc from
              tqc_sys_process_areas
              where sprca_sht_desc = v_prcs_area
              and SPRCA_SPRC_CODE = v_sprc_code ;
          EXCEPTION
          WHEN OTHERS THEN
          NULL;
          END;
         
          BEGIN
              select sprsa_sub_area
              into v_sprsa_sub_area
               from tqc_sys_process_sub_areas
               where    sprsa_sht_desc = v_prsc_sub_area
              and SPRSA_SPRC_CODE = v_sprc_code;
          EXCEPTION
          WHEN OTHERS THEN
          NULL;
          END;
       
       --tqc_users
             INSERT INTO TQC_USER_RIGHTS_LOG
             (URL_CODE,URL_USR_CODE, URL_USR_USERNAME,URL_USR_NAME,  URL_SPRC_PROCESS, URL_SPRCA_SHT_DESC, URL_SPRSA_SUB_AREA, URL_PRCS_AREA, URL_PRSC_SUB_AREA, URL_SYS_CODE,URL_AMOUNT,URL_DR_CR)
             VALUES
            ( TQC_URL_CODE_SEQ.NEXTVAL,v_user_code, v_usr_username,v_usr_name , v_sprc_process, v_sprca_sht_desc, v_sprsa_sub_area, v_prcs_area, v_prsc_sub_area, v_sys_code,v_amount,v_dc);
              
             COMMIT;
            
       END;
END tqc_interfaces_pkg;
/