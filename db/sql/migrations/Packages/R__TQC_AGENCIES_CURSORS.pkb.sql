--
-- TQC_AGENCIES_CURSORS  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_agencies_cursors
AS
/******************************************************************************
   NAME:       +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++   _CURSORS
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        3/18/2010             1. Created this package body.
******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20033,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   PROCEDURE get_agns_by_acttype (
      v_act_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agns_by_acttype_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT agn_code, agn_act_code, agn_sht_desc, agn_name,
                agn_comm_allowed, agn_main_agn_code
           FROM tqc_agencies
          WHERE agn_act_code = v_act_code;
   END;

   PROCEDURE get_credit_account_lov (
      v_acct             VARCHAR2,
      v_act_name         VARCHAR2,
      v_acctype          VARCHAR2,
      v_cursor     OUT   tqc_agns_by_acttype_ref
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT mtran_control_acc acct, mtran_client_code,
                         agn_name NAME,
                         DECODE (mtran_client_type,
                                 'A', 'AGENCY',
                                 'R', 'REINSURER',
                                 'B', 'BROKER',
                                 'FI', 'FACULTATIVE IN',
                                 'I', 'INSURANCE COMPANY',
                                 'IA', 'INHOUSE AGENTS',
                                 'FO', 'FACULTATIVE OUT',
                                 mtran_client_type
                                ) acct_type,
                         NULL agn_comm_allowed, agn_main_agn_code
                    FROM gin_master_transactions, tqc_agencies
                   WHERE mtran_client_code = agn_code
                     AND mtran_client_type != 'D'
                     AND mtran_control_acc LIKE '%' || v_acct || '%'
                     AND agn_name LIKE '%' || v_act_name || '%'
                     AND DECODE (mtran_client_type,
                                 'A', 'AGENCY',
                                 'R', 'REINSURER',
                                 'B', 'BROKER',
                                 'FI', 'FACULTATIVE IN',
                                 'I', 'INSURANCE COMPANY',
                                 'IA', 'INHOUSE AGENTS',
                                 'FO', 'FACULTATIVE OUT',
                                 mtran_client_type
                                ) LIKE '%' || v_acctype || '%'
         --AND ROWNUM <= 50
         UNION
         SELECT DISTINCT mtran_control_acc acct, mtran_client_code,
                         clnt_other_names || ' ' || clnt_name NAME,
                         'DIRECT' acct_type, NULL agn_comm_allowed,
                         NULL agn_main_agn_code
                    FROM gin_master_transactions, tqc_clients
                   WHERE mtran_client_code = clnt_code
                     AND mtran_client_type = 'D'
                     AND mtran_control_acc LIKE '%' || v_acct || '%'
                     AND clnt_other_names || ' ' || clnt_name LIKE
                                                      '%' || v_act_name || '%'
                     AND 'DIRECT' LIKE '%' || v_acctype || '%'
                     AND ROWNUM <= 50;
   END;

   PROCEDURE get_marketers (v_cursor OUT tqc_agns_by_acttype_ref)
   IS
   BEGIN
      OPEN v_cursor FOR
--    SELECT NULL AGN_CODE,NULL AGN_ACT_CODE,NULL AGN_SHT_DESC,NULL AGN_NAME
--    FROM DUAL
--    UNION
         SELECT   NULL agn_code, NULL agn_act_code, NULL agn_sht_desc,
                  NULL agn_name, NULL agn_comm_allowed,
                  NULL agn_main_agn_code
             FROM DUAL
         UNION ALL
         SELECT   agn_code, agn_act_code, agn_sht_desc, agn_name,
                  agn_comm_allowed, agn_main_agn_code
             FROM tqc_agencies
            WHERE agn_act_code = 10 AND agn_status = 'ACTIVE'
         ORDER BY agn_code DESC;
   END;

   PROCEDURE get_agns_schedules (
      v_act_code   IN       VARCHAR2,
      v_refcur     IN OUT   tqc_agns_by_acttype_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT agn_code, agn_act_code, agn_sht_desc, agn_name,
                agn_comm_allowed, agn_main_agn_code
           FROM tqc_agencies
          WHERE agn_code != 0
            AND agn_name LIKE
                   DECODE (v_act_code,
                           '-1000', agn_name,
                           NVL (v_act_code, '%')
                          );
   END;

   PROCEDURE get_agencies (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agencies_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT agn.agn_code, agn.agn_act_code, agn.agn_sht_desc,
                agn.agn_name, agn.agn_physical_address,
                agn.agn_postal_address, agn.agn_twn_code, twn_name,
                agn.agn_cou_code, cou_name, agn.agn_email_address,
                agn.agn_web_address, agn.agn_zip, agn.agn_contact_person,
                agn.agn_contact_title, agn.agn_tel1, agn.agn_tel2,
                agn.agn_fax, agn.agn_acc_no, agn.agn_pin,
                agn.agn_agent_commission, agn.agn_credit_allowed,
                agn.agn_agent_wht_tax, agn.agn_print_dbnote, agn.agn_status,
                agn.agn_date_created, agn.agn_created_by, agn.agn_reg_code,
                agn.agn_comm_reserve_rate, agn.agn_annual_budget,
                agn.agn_status_eff_date, agn.agn_credit_period,
                agn.agn_comm_stat_eff_dt, agn.agn_comm_status_dt,
                agn.agn_comm_allowed, agn.agn_checked, agn.agn_checked_by,
                agn.agn_check_date, agn.agn_comp_comm_arrears,
                agn.agn_reinsurer, agn.agn_brn_code, agn.agn_town,
                agn.agn_country, agn.agn_status_desc, agn.agn_id_no,
                agn.agn_con_code, agn.agn_agn_code, agn.agn_sms_tel,
                agn.agn_ahc_code, agn.agn_sec_code, agn.agn_agnc_class_code,
                agn.agn_expiry_date, agn.agn_license_no, agn.agn_runoff,
                agn.agn_licensed, agn.agn_license_grace_pr,
                agn.agn_old_acc_no, agn.agn_status_remarks,
                aff.agn_name di_affiliate, ahc_name, NULL di_per_no,
                NULL di_personnel_names, NULL di_status_code
           FROM tqc_agencies agn,
                tqc_countries,
                tqc_towns,
                tqc_agencies aff,
                tqc_agency_holding_company
          WHERE agn.agn_cou_code = cou_code(+)
            AND agn.agn_twn_code = twn_code(+)
            AND agn.agn_code = aff.agn_agn_code(+)
            AND ahc_code(+) = agn.agn_ahc_code
            AND agn.agn_code = v_agn_code;
   --RETURN(v_refcur);
   END;

   PROCEDURE get_agency_registration (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agency_registration_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT areg_code, areg_agn_code, areg_year, areg_reg_no, areg_wef,
                areg_wet, areg_accepted
           FROM tqc_agency_registration
          WHERE areg_agn_code = v_agn_code;
   END;

   PROCEDURE get_tqc_agency_directors (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agency_directors_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT *
           FROM tqc_agency_directors
          WHERE adir_agn_code = v_agn_code;
   END;

   PROCEDURE get_tqc_agency_referees (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agency_referees_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT *
           FROM tqc_agency_referees
          WHERE aref_agn_code = v_agn_code;
   END;

   PROCEDURE get_agn_unassigned_sys (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agn_unassigned_sys_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT sys_code, sys_sht_desc, sys_name
           FROM tqc_systems;
   END;

   PROCEDURE get_agn_assigned_sys (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agency_systems_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT csys_code, csys_clnt_code, csys_sys_code, csys_wef, csys_wet,
                sys_sht_desc
           FROM tqc_client_systems, tqc_systems
          WHERE csys_sys_code = sys_code AND csys_clnt_code = v_agn_code;
   END;

   PROCEDURE get_account_contacts_sys (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_account_contacts_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT accc_code, accc_acc_code, accc_name, accc_username,
                accc_other_names, accc_personel_rank, accc_created_by,
                accc_dt_created, accc_login_allowed, accc_status,
                accc_pwd_reset, accc_tel, accc_email_addr
           FROM tqc_account_contacts
          WHERE accc_acc_code = v_agn_code;
   END;

   PROCEDURE get_account_unassigned_systems (
      v_acc_code   IN       NUMBER,
      v_cursor     OUT      tqc_account_systems_ref
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_active
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND NOT EXISTS (
                   SELECT asys_sys_code, asys_agn_code, asys_wef, asys_wet,
                          asys_comment
                     FROM tqc_agency_systems
                    WHERE tqc_systems.sys_code =
                                              tqc_agency_systems.asys_sys_code
                      AND asys_agn_code = v_acc_code);
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error fetching unassigned systems ...');
   END;

   PROCEDURE get_account_assigned_systems (
      v_acc_code   IN       NUMBER,
      v_cursor     OUT      tqc_account_systems_ref
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT sys_code, sys_sht_desc, sys_name, sys_active
           FROM tqc_systems
          WHERE sys_active <> 'N'
            AND EXISTS (
                   SELECT asys_sys_code, asys_agn_code, asys_wef, asys_wet,
                          asys_comment
                     FROM tqc_agency_systems
                    WHERE tqc_systems.sys_code =
                                              tqc_agency_systems.asys_sys_code
                      AND asys_agn_code = v_acc_code);
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error fetching assigned systems ...');
   END;

   PROCEDURE get_agency_classes (v_refcur OUT tqc_agency_classes_ref)
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT agnc_class_code, agnc_class_desc
           FROM tqc_agencies_classes;
   END;

   PROCEDURE get_agency_holdco (v_refcur OUT tqc_agency_holdco_ref)
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT   ahc_code, ahc_name
             FROM tqc_agency_holding_company
         ORDER BY 2;
   END;

   PROCEDURE get_account_types (v_cursor OUT account_types_cursor)
   IS
      v_org_type   VARCHAR2 (5);
   BEGIN
      v_org_type := tqc_interfaces_pkg.get_org_type (37);

      IF v_org_type = 'INS'
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT act_code, act_account_type
                       FROM tqc_account_types, gin_commissions
                      WHERE comm_act_code = act_code
                        AND comm_act_code IN (2, 3, 16, 4, 7, 10);
      ELSIF v_org_type = 'BRK'
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT act_code, act_account_type
                       FROM tqc_account_types, gin_commissions
                      WHERE comm_act_code = act_code
                        AND comm_act_code IN (5, 16);
      END IF;
   END;

   PROCEDURE get_comm_account_types (v_cursor OUT account_types_cursor)
   IS
      v_org_type   VARCHAR2 (5);
   BEGIN
      v_org_type := tqc_interfaces_pkg.get_org_type (37);

      IF v_org_type = 'INS'
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT act_code, act_account_type
                       FROM tqc_account_types
                      WHERE act_code IN (2, 3, 16, 4, 7, 10);
      ELSIF v_org_type = 'BRK'
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT act_code, act_account_type
                       FROM tqc_account_types
                      WHERE act_code IN (5, 16);
      END IF;
   END;

   PROCEDURE get_account_types (
      v_scl_code   IN       NUMBER,
      v_cursor     OUT      account_types_cursor
   )
   IS
      v_org_type   VARCHAR2 (5);
   BEGIN
      v_org_type := tqc_interfaces_pkg.get_org_type (37);

      IF v_org_type = 'INS'
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT act_code, act_account_type
                       FROM tqc_account_types, gin_commissions
                      WHERE comm_act_code = act_code
                        AND comm_act_code IN (2, 3, 16, 4, 7, 10)
                        AND comm_scl_code = v_scl_code;
      ELSIF v_org_type = 'BRK'
      THEN
         OPEN v_cursor FOR
            SELECT DISTINCT act_code, act_account_type
                       FROM tqc_account_types, gin_commissions
                      WHERE comm_act_code = act_code
                        AND comm_act_code IN (5, 16);
      END IF;
   END;

   PROCEDURE get_agent_commissions (v_ref_cursor OUT agents_for_comm_cursor)
   IS
   BEGIN
      OPEN v_ref_cursor FOR
         SELECT agn_sht_desc, agn_name, agn_code, agn_acc_no, '',
                agn_act_code
           FROM tqc_agencies, tqc_agency_systems
          WHERE agn_code = asys_agn_code AND asys_sys_code = 37;
   END;

   PROCEDURE get_product_agents (
      v_bcode        IN       NUMBER,
      v_ref_cursor   OUT      agents_for_comm_cursor
   )
   IS
   BEGIN
      --RAISE_ERROR('v_bcode'||v_bcode);
      OPEN v_ref_cursor FOR
         SELECT agn_sht_desc, agn_name, agn_code, agn_acc_no, '',
                agn_act_code
           FROM tqc_agencies, tqc_agency_systems, tqc_account_types
          WHERE agn_code = asys_agn_code
            AND asys_sys_code = 37
            AND agn_act_code = act_code
            AND agn_act_code = v_bcode;
   END;

   PROCEDURE get_product_agents (v_ref_cursor OUT agents_for_comm_cursor)
   IS
   BEGIN
      OPEN v_ref_cursor FOR
         SELECT agn_sht_desc, agn_name, agn_code, agn_acc_no, '',
                agn_act_code
           FROM tqc_agencies, tqc_agency_systems, tqc_branches
          WHERE agn_code = asys_agn_code
            AND asys_sys_code = 37
            AND brn_code(+) = agn_brn_code
            AND agn_act_code IN (2, 3)
            AND agn_code IN (SELECT DISTINCT accc_acc_code
                                        FROM tqc_account_contacts);
   END;

   PROCEDURE get_treaty_agents (v_cursor OUT tqc_agns_by_acttype_ref)
   IS
   BEGIN
      OPEN v_cursor FOR                                  ---tqc_account_types
         SELECT   agn_code, agn_act_code, agn_sht_desc, agn_name,
                  agn_comm_allowed, agn_main_agn_code
             FROM tqc_agencies
            WHERE agn_act_code IN (6) AND agn_status = 'ACTIVE'
         ORDER BY agn_code;
   END;

   PROCEDURE get_certificate_agents (v_cursor OUT tqc_agns_by_acttype_ref)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT   agn_code, agn_act_code, agn_sht_desc, agn_name,
                  agn_comm_allowed, agn_main_agn_code
             FROM tqc_agencies
            WHERE agn_code NOT IN (SELECT agco_agn_code
                                     FROM gin_agent_cert_scrn_order)
         ORDER BY 2, 3;
   END;

   PROCEDURE get_fac_agencies (v_cursor OUT tqc_agns_by_acttype_ref)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT   agn_code, agn_act_code, agn_sht_desc, agn_name,
                  agn_comm_allowed, agn_main_agn_code
             FROM tqc_agencies
            WHERE agn_act_code IN (5, 6)
         ORDER BY 2, 3;
   END;

   PROCEDURE get_binder_agents (
      v_ref_cursor    OUT      agents_for_comm_cursor,
      v_binder_type   IN       VARCHAR2
   )
   IS
   BEGIN
      IF tqc_interfaces_pkg.get_org_type (37) IN ('INS')
      THEN
         OPEN v_ref_cursor FOR
            SELECT   agn_sht_desc, agn_name, agn_code, agn_acc_no, brn_name,
                     agn_act_code
                FROM tqc_agencies,
                     tqc_agency_systems,
                     tqc_branches,
                     gin_binders
               WHERE bind_agnt_agent_code = agn_code
                 AND agn_code = asys_agn_code
                 AND asys_sys_code = 37
                 AND brn_code(+) = agn_brn_code
                 --AND AGN_CODE != 0
                 AND agn_act_code IN (2, 3, 1)
            ORDER BY 3;
      ELSE
         IF v_binder_type NOT IN ('B', 'M')
         THEN
            raise_error ('Binder type ' || v_binder_type || ' not defined..');
         END IF;

         OPEN v_ref_cursor FOR
            SELECT   agn_sht_desc, agn_name, agn_code, agn_acc_no, brn_name,
                     agn_act_code
                FROM tqc_agencies,
                     tqc_agency_systems,
                     tqc_branches,
                     gin_binders
               WHERE bind_agnt_agent_code = agn_code
                 AND bind_type = v_binder_type
                 AND agn_code = asys_agn_code
                 AND asys_sys_code = 37
                 AND brn_code(+) = agn_brn_code
                 --AND AGN_CODE != 0
                 AND agn_act_code IN (5)
            ORDER BY 3;
      END IF;
   END;

   PROCEDURE get_prod_binder_agents (
      v_ref_cursor   OUT   agents_for_comm_cursor,
      v_agn_name           VARCHAR2
   )
   IS
   BEGIN
      IF tqc_interfaces_pkg.get_org_type (37) IN ('INS')
      THEN
         OPEN v_ref_cursor FOR
            SELECT   agn_sht_desc, agn_name, agn_code, agn_acc_no, brn_name,
                     agn_act_code
                FROM tqc_agencies, tqc_agency_systems, tqc_branches
               WHERE agn_code = asys_agn_code
                 AND asys_sys_code = 37
                 AND brn_code(+) = agn_brn_code
                 AND agn_code != 0
                 AND agn_act_code IN (2, 3)
                 AND agn_name LIKE '%' || v_agn_name || '%'
            ORDER BY 3;
      ELSE
         OPEN v_ref_cursor FOR
            SELECT   agn_sht_desc, agn_name, agn_code, agn_acc_no, brn_name,
                     agn_act_code
                FROM tqc_agencies, tqc_agency_systems, tqc_branches
               WHERE agn_code = asys_agn_code
                 AND asys_sys_code = 37
                 AND brn_code(+) = agn_brn_code
                 AND agn_name LIKE '%' || v_agn_name || '%'
                 AND agn_code != 0
                 AND agn_act_code IN (5)
            ORDER BY 3;
      END IF;
   END;

   PROCEDURE get_insurance_pymts_agents (
      v_refcur   IN OUT   tqc_agns_by_acttype_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT   agn_code, agn_act_code, agn_sht_desc, agn_name,
                  agn_comm_allowed, agn_main_agn_code
             FROM tqc_agencies
            WHERE agn_act_code = 5
         ORDER BY 2;
   END;

   PROCEDURE get_commission_account_types (v_cursor OUT account_types_cursor)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT DISTINCT act_code, act_account_type
                    FROM tqc_account_types, gin_commissions
                   WHERE act_code = comm_act_code;
   END;

   FUNCTION getallagencyassignedsystems (v_agn_code NUMBER)
      RETURN agencyassignedsystems_ref
   IS
      v_cursor   agencyassignedsystems_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   sys_code, sys_sht_desc, sys_name, sys_active,
                  asys_sys_code, asys_agn_code, asys_wef, asys_wet,
                  asys_comment, asys_osd_code,
                  tqc_agencies_cursors.subdivision_name
                                              (asys_osd_code)
                                                            subdivision_name,
                  asys_osd_id, asys_agn_sht_desc
             FROM tqc_agency_systems, tqc_systems
            WHERE tqc_agency_systems.asys_sys_code = tqc_systems.sys_code
              AND tqc_systems.sys_active <> 'N'
              AND tqc_agency_systems.asys_agn_code = v_agn_code
         ORDER BY sys_name;

      RETURN v_cursor;
   END;

   FUNCTION subdivision_name (v_osd_code VARCHAR2)
      RETURN VARCHAR2
   IS
      v_ret_val   VARCHAR2 (50);
   BEGIN
      SELECT osd_name
        INTO v_ret_val
        FROM tqc_org_subdivisions
       WHERE osd_code = v_osd_code;

      RETURN v_ret_val;
   END subdivision_name;

   FUNCTION getallagencies (
      v_agn_name       IN   VARCHAR2 DEFAULT '',
      v_agn_sht_desc   IN   VARCHAR2 DEFAULT ''
   )
      RETURN tqc_agns_by_acttype_ref
   IS
      v_gency   tqc_agns_by_acttype_ref;
   BEGIN
      IF (v_agn_name IS NOT NULL) OR (v_agn_sht_desc IS NOT NULL)
      THEN
         OPEN v_gency FOR
            SELECT agn_code, agn_act_code, agn_sht_desc, agn_name,
                   agn_comm_allowed, agn_main_agn_code
              FROM tqc_agencies
             WHERE UPPER (agn_name) LIKE '%' || UPPER (v_agn_name) || '%'
                OR UPPER (agn_sht_desc) LIKE
                                           '%' || UPPER (v_agn_sht_desc)
                                           || '%';
      ELSIF (v_agn_name IS NULL) AND (v_agn_sht_desc IS NULL)
      THEN
         OPEN v_gency FOR
            SELECT NULL, NULL, NULL, NULL, NULL, NULL
              FROM DUAL;
      END IF;

      RETURN v_gency;
   END getallagencies;

   PROCEDURE get_embassy_contacts (
      v_cou_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_account_contacts_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT accc_code, NULL, accc_name, accc_username, accc_other_names,
                accc_personel_rank, accc_created_by, accc_dt_created,
                accc_login_allowed, accc_status, accc_pwd_reset, accc_tel,
                accc_email_addr
           FROM tqc_embassy_contacts
          WHERE accc_cou_code = v_cou_code AND accc_status NOT IN ('I');
   END;

   PROCEDURE find_serv_providers (
      v_cursor      OUT      serv_providers_ref,
      v_agentcode   IN       NUMBER
   )
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT spr_code, spr_sht_desc, spr_name
           FROM tqc_service_providers
          WHERE spr_code NOT IN (SELECT DISTINCT agnt_spr_code
                                            FROM tqc_agency_service_providers
                                           WHERE agnt_agn_code = v_agentcode);
   END;

   FUNCTION getagentserviceproviders (v_agentcode IN NUMBER)
      RETURN agent_service_providers_ref
   IS
      v_cursor   agent_service_providers_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT spr_code, spr_name, agnt_code, spr_sht_desc
           FROM tqc_service_providers, tqc_agency_service_providers
          WHERE spr_code = agnt_spr_code AND agnt_agn_code = v_agentcode;

      RETURN v_cursor;
   END;

   FUNCTION fetchagencynoneclients (v_agn_code NUMBER)
      RETURN agencynoneclients_ref
   IS
      v_cursor   agencynoneclients_ref;
   BEGIN
--RAISE_ERROR('v_agn_code'||v_agn_code);
      OPEN v_cursor FOR
         SELECT clnt_code, clnt_sht_desc,
                clnt_name || ' ' || clnt_other_names client
           FROM tqc_clients
          WHERE clnt_code NOT IN (SELECT agnc_clnt_code
                                    FROM tqc_agency_clients
                                   WHERE agnc_agn_code = v_agn_code);

      RETURN v_cursor;
   END;

   PROCEDURE get_agn_by_agn_code (
      v_agn_code   IN       NUMBER,
      v_refcur     IN OUT   tqc_agns_by_acttype_ref
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT agn_code, agn_act_code, agn_sht_desc, agn_name,
                agn_comm_allowed, agn_main_agn_code
           FROM tqc_agencies
          WHERE agn_code = v_agn_code;
   END;

   FUNCTION fetchbranchagencies
      RETURN branchagencies_ref
   IS
      v_cursor   branchagencies_ref;
   BEGIN
--RAISE_ERROR('v_agn_code'||v_agn_code);
      OPEN v_cursor FOR
         SELECT bra_code, bra_brn_code, bra_name, bra_agn_code, bra_sht_desc,
                agn_sht_desc
           FROM tqc_branch_agencies, tqc_agencies
          WHERE bra_agn_code = agn_code(+) AND bra_status = 'A';

      RETURN v_cursor;
   END;

   FUNCTION fetchagencyproducts (v_agent_code IN NUMBER)
      RETURN agency_products_ref
   IS
      v_cursor   agency_products_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   pro_code, pro_desc, agnp_code
             FROM tqc_agency_products, tq_gis.gin_products
            WHERE agnp_prod_code = pro_code AND agnp_agn_code = v_agent_code
         ORDER BY pro_desc;

      RETURN (v_cursor);
   END;

   FUNCTION fetchnoneagencyproducts (v_agent_code IN NUMBER)
      RETURN agency_products_ref
   IS
      v_cursor   agency_products_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT   pro_code, pro_desc, NULL
             FROM tq_gis.gin_products
            WHERE pro_code NOT IN (SELECT agnp_prod_code
                                     FROM tqc_agency_products
                                    WHERE agnp_agn_code = v_agent_code)
              AND TRUNC (SYSDATE) BETWEEN TRUNC (pro_wef)
                                      AND NVL (TRUNC (pro_wet),
                                               TRUNC (SYSDATE)
                                              )
         ORDER BY pro_desc;

      RETURN (v_cursor);
   END;

   FUNCTION fetchallbranches (v_org_code IN NUMBER)
      RETURN allbranches_ref
   IS
      v_cursor   allbranches_ref;
   BEGIN
      raise_error ('v_org_code' || v_org_code);

      OPEN v_cursor FOR
         SELECT brn_code, brn_name, brn_sht_desc
           FROM tqc_branches, tqc_regions, tqc_organizations
          WHERE brn_reg_code = reg_code
            AND org_code = reg_org_code
            AND org_code = v_org_code;

      RETURN v_cursor;
   END;

   PROCEDURE add_agency_product (
      v_agent_code   IN   NUMBER,
      v_prod_code    IN   NUMBER,
      v_add_edit     IN   VARCHAR2
   )
   IS
      v_count   NUMBER;
   BEGIN
      IF v_add_edit = 'A'
      THEN
         SELECT COUNT (agnp_prod_code)
           INTO v_count
           FROM tqc_agency_products
          WHERE agnp_agn_code = v_agent_code AND agnp_prod_code = v_prod_code;

         IF v_count = 0
         THEN                                                --Add the product
            INSERT INTO tqc_agency_products
                        (agnp_code, agnp_agn_code,
                         agnp_prod_code
                        )
                 VALUES (tqc_agency_products_seq.NEXTVAL, v_agent_code,
                         v_prod_code
                        );
         END IF;
      ELSIF v_add_edit = 'D'
      THEN
         DELETE      tqc_agency_products
               WHERE agnp_agn_code = v_agent_code
                 AND agnp_prod_code = v_prod_code;
      END IF;
   END;

   PROCEDURE get_insurance_pymts_agents (
      v_refcur     IN OUT   tqc_agns_by_acttype_ref,
      v_act_code   IN       NUMBER
   )
   IS
   BEGIN
      OPEN v_refcur FOR
         SELECT   agn_code, agn_act_code, agn_sht_desc, agn_name, NULL, NULL
             FROM tqc_agencies
            WHERE agn_act_code = v_act_code
         ORDER BY agn_name;
   END;

   FUNCTION fetchmanagers (v_act_type IN VARCHAR)
      RETURN managers_ref
   IS
      v_cursor   managers_ref;
   BEGIN
      raise_error ('v_act_type' || v_act_type);

      OPEN v_cursor FOR
         SELECT agn_code, agn_sht_desc, agn_name
           FROM tqc_agencies, tqc_account_types
          WHERE agn_act_code = act_code
            AND act_type_id = v_act_type
            AND agn_status = 'ACTIVE';

      RETURN v_cursor;
   END;

--24112016
   PROCEDURE get_treaty_brokers (v_cursor OUT tqc_agns_by_acttype_ref)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT   agn_code, agn_act_code, agn_sht_desc, agn_name,
                  agn_comm_allowed, agn_main_agn_code
             FROM tqc_agencies
            WHERE agn_act_code IN (6) AND agn_status = 'ACTIVE'
         ORDER BY agn_code;
   END;

   FUNCTION fetch_agent_details (v_agn_code IN tqc_agencies.agn_code%TYPE)
      RETURN sys_refcursor
   IS
      v_rc   sys_refcursor;
   BEGIN
      OPEN v_rc FOR
         SELECT agn_code, agn_act_code, agn_sht_desc, agn_name,
                agn_physical_address, agn_postal_address, agn_twn_code,
                agn_cou_code, agn_email_address, agn_web_address, agn_zip,
                agn_contact_person, agn_contact_title, agn_tel1, agn_tel2,
                agn_fax, agn_acc_no, agn_pin, agn_agent_commission,
                agn_credit_allowed, agn_agent_wht_tax, agn_print_dbnote,
                agn_status, agn_date_created, agn_created_by, agn_reg_code,
                agn_comm_reserve_rate, agn_annual_budget,
                agn_status_eff_date, agn_credit_period, agn_comm_stat_eff_dt,
                agn_comm_status_dt, agn_comm_allowed, agn_checked,
                agn_checked_by, agn_check_date, agn_comp_comm_arrears,
                agn_reinsurer, agn_brn_code, agn_town, agn_country,
                agn_status_desc, agn_id_no, agn_con_code, agn_sms_tel,
                agn_ahc_code, agn_sec_code, agn_agnc_class_code,
                agn_expiry_date, agn_license_no, agn_runoff, agn_licensed,
                agn_license_grace_pr, agn_old_acc_no, agn_status_remarks,
                agn_osd_code, agn_bank_acc_no, agn_unique_prefix,
                agn_bbr_code, agn_inique_prefix, agn_state_code,
                agn_bbr_acc_code, agn_crdt_rting, agn_subagent,
                agn_main_agn_code, agn_agn_code, agn_clnt_code, agn_payee,
                agn_account_manager, agn_enable_web_edit, agn_birth_date,
                agn_credit_limit, agn_bru_code, agn_local_international,
                agn_regulator_number, agn_authorised, agn_authorised_by,
                agn_authorised_date, agn_ors_code, agn_rorg_code,
                agn_allocate_cert, agn_bounced_chq, agn_agent_type,
                agn_bpn_code, agn_group, agn_forgn, agn_whtax_applicable,
                agn_vat_applicable, agn_default_comm_mode, agn_tel_pay,
                agn_payment_freq, agn_default_cpm_mode, agn_gl_rcvbl_acc_no,
                agn_paydetail_validated, agn_inst_comm_allwd, agn_auth_name,
                agn_brr_name, agn_brr_code, agn_comm_levy_rate,
                agn_comm_levy_app, agn_sbu_code, agn_pymt_validated,
                agn_qualification, agn_marital_status,
                agn_benefit_start_date, agn_id_no_doc_used, agn_agnty_code,
                agn_validation_source, agn_iprs_validated, agn_gender,
                agn_passport_no, agn_updated_on, agn_updated_by,
                agn_ira_regno, agn_ent_code, cou_zip_code,
                cou_admin_reg_type, brn_name, sec_name, sts_name,
                bbr_branch_name, clnt_name, bnk_code, bnk_bank_name,
                rorg_desc, ors_desc, prom_transaction_date,
                prom_transaction_type, prom_demo_promo_type,
                cpm_desc agn_cpm_mode_desc,act_account_type
           FROM tqc_agencies,tqc_account_types,
                tqc_branches,
                tqc_countries,
                tqc_sectors,
                tqc_states,
                tqc_towns,
                tqc_clients,
                tqc_bank_branches,
                tqc_banks,
                lms_agent_promotion,
                tqc_rating_organizations,
                tqc_org_rating_starndards,
                tqc_clm_payment_modes
          WHERE agn_code = v_agn_code
            AND agn_clnt_code = clnt_code(+)
            AND bbr_bnk_code = bnk_code(+)
            AND agn_cou_code = cou_code(+)
            AND agn_sec_code = sec_code(+)
            AND agn_state_code = sts_code(+)
            AND agn_brn_code = brn_code(+)
            AND agn_rorg_code = rorg_code(+)
            AND prom_agn_code(+) = agn_code
            AND agn_ors_code = ors_code(+)
            AND agn_default_cpm_mode = cpm_code(+)
            AND agn_act_code = act_code(+)
            AND agn_bbr_code = bbr_code(+);

--              and agn_gsm_zip_code = n.clnt_code(+)
--              and agn_int_zip_code = c.cou_zip_code(+)
--              and agn_domicile_countries= d.cou_code(+)
      RETURN v_rc;
   END fetch_agent_details;

--
   FUNCTION fetch_transferable_items (
      v_parent_code   IN   NUMBER,
      v_trn_type           VARCHAR2
   )
      RETURN sys_refcursor
   IS
      v_cursor   sys_refcursor;
   BEGIN
      ---- RAISE_ERROR('v_parent_code'||v_parent_code||' v_trn_type: '||v_trn_type);
      OPEN v_cursor FOR
         SELECT                                                    -- REGIONS
                reg_code ttx_item_code, reg_sht_desc ttx_item_sht_desc,
                reg_name ttx_item_name, reg_org_code ttx_from_code,
                org_name ttx_from_name, 'REG' ttx_item_type
           FROM tqc_regions, tqc_transfers, tqc_organizations
          WHERE reg_org_code = tt_trans_from_code
            AND reg_org_code = org_code
            AND tt_code = v_parent_code
            AND NVL (tt_authorized, 'N') <> 'Y'
            AND NOT EXISTS (
                   SELECT tti_item_code
                     FROM tqc_transfers_items, tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code =
                                                          tqc_regions.reg_code
                      AND tti_tt_code = v_parent_code
                      AND tt_code = tti_tt_code
                      AND NVL (tt_authorized, 'N') <> 'Y'
                      AND tt_trans_type = 'ROT')
            AND 'ROT' = v_trn_type
         UNION                                                     -- BRANCHES
         SELECT brn_code ttx_item_code, brn_sht_desc ttx_item_sht_desc,
                brn_name ttx_item_name, brn_reg_code ttx_from_code,
                reg_name ttx_from_name, 'BRN' ttx_item_type
           FROM tqc_branches, tqc_transfers, tqc_regions
          WHERE brn_reg_code = tt_trans_from_code
            AND brn_reg_code = reg_code
            AND tt_code = v_parent_code
            AND NVL (tt_authorized, 'N') <> 'Y'
            AND NOT EXISTS (
                   SELECT tti_item_code
                     FROM tqc_transfers_items, tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code =
                                                         tqc_branches.brn_code
                      AND tti_tt_code = v_parent_code
                      AND tt_code = tti_tt_code
                      AND NVL (tt_authorized, 'N') <> 'Y'
                      AND tt_trans_type = 'BRT')
            AND 'BRT' = v_trn_type
         UNION                                                     -- AGENCIES
         SELECT bra_code ttx_item_code, bra_sht_desc ttx_item_sht_desc,
                bra_name ttx_item_name, bra_brn_code ttx_from_code,
                brn_name ttx_from_name, 'AGN' ttx_item_type
           FROM tqc_branch_agencies, tqc_transfers, tqc_branches
          WHERE 'Y' !=
                   tqc_parameters_pkg.get_param_varchar
                                                     ('TRX_BRANCHES_FETCH_ALL')
            AND bra_brn_code = brn_code
            AND bra_brn_code = tt_trans_from_code
            AND tt_code = v_parent_code
            AND NVL (tt_authorized, 'N') <> 'Y'
            AND NOT EXISTS (
                   SELECT tti_item_code
                     FROM tqc_transfers_items, tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code =
                                                  tqc_branch_agencies.bra_code
                      AND tti_tt_code = v_parent_code
                      AND tt_code = tti_tt_code
                      AND NVL (tt_authorized, 'N') <> 'Y'
                      AND tt_trans_type = 'ABT')
            AND 'ABT' = v_trn_type
         UNION                                                     -- AGENCIES
         SELECT bra_code ttx_item_code, bra_sht_desc ttx_item_sht_desc,
                bra_name ttx_item_name, bra_brn_code ttx_from_code,
                brn_name ttx_from_name, 'AGN' ttx_item_type
           FROM tqc_branch_agencies, tqc_transfers, tqc_branches
          WHERE 'Y' =
                   tqc_parameters_pkg.get_param_varchar
                                                     ('TRX_BRANCHES_FETCH_ALL')
            AND bra_brn_code = brn_code
            AND tt_code = v_parent_code
            AND NVL (tt_authorized, 'N') <> 'Y'
            AND NOT EXISTS (
                   SELECT tti_item_code
                     FROM tqc_transfers_items, tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code =
                                                  tqc_branch_agencies.bra_code
                      AND tti_tt_code = v_parent_code
                      AND tt_code = tti_tt_code
                      AND NVL (tt_authorized, 'N') <> 'Y'
                      AND tt_trans_type = 'ABT')
            AND 'ABT' = v_trn_type
         UNION                                                        -- UNITS
         SELECT bru_code ttx_item_code, bru_sht_desc ttx_item_sht_desc,
                bru_name ttx_item_name, bru_bra_code ttx_from_code,
                bra_name ttx_from_name, 'UNT' ttx_item_type
           FROM tqc_branch_units, tqc_transfers, tqc_branch_agencies
          WHERE bru_bra_code = tt_trans_from_code
            AND bru_bra_code = bra_code
            AND tt_code = v_parent_code
            AND NVL (tt_authorized, 'N') <> 'Y'
            AND NOT EXISTS (
                   SELECT tti_item_code
                     FROM tqc_transfers_items, tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code =
                                                     tqc_branch_units.bru_code
                      AND tt_code = tti_tt_code
                      AND NVL (tt_authorized, 'N') <> 'Y'
                      AND tt_trans_type = 'UAT'
                      AND tti_tt_code = v_parent_code)
            AND 'UAT' = v_trn_type
         UNION                                                       -- AGENTS
         SELECT ttx_item_code, ttx_item_sht_desc, ttx_item_name,
                ttx_from_code, ttx_from_name, ttx_item_type
           FROM (SELECT agn_code ttx_item_code,
                        agn_sht_desc ttx_item_sht_desc,
                        agn_name ttx_item_name, agn_bru_code ttx_from_code,
                        bru_name ttx_from_name, 'AGT' ttx_item_type
                   FROM lms_agencies, tqc_branch_units
                  WHERE agn_bru_code = bru_code
                 UNION
                 SELECT agn_code ttx_item_code,
                        agn_sht_desc ttx_item_sht_desc,
                        agn_name ttx_item_name, agn_bru_code ttx_from_code,
                        bru_name ttx_from_name, 'AGT' ttx_item_type
                   FROM tqc_agencies, tqc_branch_units
                  WHERE agn_bru_code = bru_code) a,
                tqc_account_types,
                tqc_transfers
          WHERE a.ttx_from_code = tt_trans_from_code
            AND tt_code = v_parent_code
            AND act_type_sht_desc = 'IA'
            AND NVL (tt_authorized, 'N') <> 'Y'
            AND NOT EXISTS (
                   SELECT tti_item_code
                     FROM tqc_transfers_items, tqc_transfers
                    WHERE tqc_transfers_items.tti_item_code = a.ttx_item_code
                      AND tti_tt_code = v_parent_code
                      AND tt_code = tti_tt_code
                      AND NVL (tt_authorized, 'N') <> 'Y'
                      AND tt_trans_type = 'AUT')
            AND 'AUT' = v_trn_type;

      RETURN v_cursor;
   END fetch_transferable_items;
END tqc_agencies_cursors; 
/