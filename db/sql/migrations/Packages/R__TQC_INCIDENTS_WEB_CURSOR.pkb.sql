--
-- TQC_INCIDENTS_WEB_CURSOR  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_INCIDENTS_WEB_CURSOR AS

  PROCEDURE FindIncBranches(v_branches OUT branchesLOvRec,
                            v_sys_code NUMBER) IS
  BEGIN
    OPEN v_branches FOR
      SELECT BRN_CODE, BRN_SHT_DESC, BRN_NAME
        FROM TQC_SYSTEMS, TQC_ORGANIZATIONS, TQC_REGIONS, TQC_BRANCHES
       WHERE SYS_ORG_CODE = ORG_CODE
         AND REG_ORG_CODE = ORG_CODE
         AND BRN_REG_CODE = REG_CODE
         AND SYS_CODE = NVL(v_sys_code, SYS_CODE)
       ORDER BY 3;
  END;
  PROCEDURE FindIncDepartments(v_incidences OUT incidencesLovRec,
                               v_brn_code   NUMBER) IS
  BEGIN
    OPEN v_incidences FOR
      SELECT IDEP_CODE, IDEP_DESC, IDEP_SHT_DESC
        FROM TQC_INC_DEPARTMENTS
       WHERE IDEP_BRN_CODE = v_brn_code
          OR IDEP_BRN_CODE = -2000;
  END;
  PROCEDURE FindIncTypes(v_inc_types OUT incidentTypeLOVRec,
                         v_idep_code NUMBER) IS
  BEGIN
    OPEN v_inc_types FOR
      SELECT DEPICT_CODE,
             DEPICT_INCT_CODE,
             DEPICT_IDEP_CODE,
             INCT_SHT_DESC,
             DEPICT_SEND_EMAIL,
             DEPICT_PRIORITY_LVL,
             DEPICT_ESCALATE,
             DEPICT_ESC_DAYS,
             DEPICT_CLOSE_DAYS,
             INCT_DESC
        FROM TQC_DEP_INCIDENT_TYPES, TQC_INCIDENT_TYPES
       WHERE DEPICT_IDEP_CODE = v_idep_code
         AND INCT_CODE = DEPICT_INCT_CODE;
  END;
  PROCEDURE FindUsers(v_user OUT usersLOVRec) IS
  BEGIN
    OPEN v_user FOR
      SELECT 'NON' USR_USERNAME, 'NON' USR_NAME
        FROM dual
      UNION
      SELECT USR_USERNAME, USR_NAME FROM TQC_USERS;
  END;
  PROCEDURE FindPolicies(v_policy OUT policLOVRec, v_sys_param NUMBER) IS
  BEGIN
    IF v_sys_param = 37 THEN
      OPEN v_policy FOR
        SELECT DISTINCT pol_policy_no,
                        TO_CHAR(pol_batch_no) code,
                        clnt_name || ' ' || clnt_other_names NAME,
                        brn_name
          FROM GIN_POLICIES, tqc_clients, tqc_branches
         WHERE pol_prp_code = clnt_code
           AND pol_brn_code = brn_code;
      /*ELSIF v_sys_param=27 THEN
      OPEN v_policy FOR
      SELECT DISTINCT pol_policy_no,  TO_CHAR(pol_code)code,  PRP_SURNAME||' '||PRP_OTHER_NAMES NAME, brn_name
      FROM LMS_POLICIES, lms_proposers, tqc_branches
      WHERE pol_prp_code = PRP_code
      AND pol_bra_code = brn_code
      ORDER BY 1;*/
    END IF;
  
  END;
  PROCEDURE FindClaims(v_claims OUT claimsLOVRec) IS
  BEGIN
    OPEN v_claims FOR
      SELECT cmb_claim_no,
             cmb_pol_policy_no,
             pol_batch_no,
             clnt_name || ' ' || clnt_other_names NAME,
             brn_name
        FROM GIN_CLAIM_MASTER_BOOKINGS,
             GIN_POLICIES,
             tqc_clients,
             tqc_branches
       WHERE cmb_pol_batch_no = pol_batch_no
         AND pol_prp_code = clnt_code
         AND pol_brn_code = brn_code;
  END;
  PROCEDURE FindQuotation(v_quotation OUT quotationLOVRec) IS
  BEGIN
    OPEN v_quotation FOR
      SELECT QUOT_NO FROM GIN_QUOTATIONS;
  END;
  PROCEDURE FindIncidents(v_incidents_rec OUT incidentsRec) IS
    v_user VARCHAR2(100) := pkg_global_vars.get_pvarchar2('pkg_global_vars.pvg_username');
  BEGIN
    OPEN v_incidents_rec FOR
      SELECT INC_CODE,
             INC_BRN_CODE,
             BRN_NAME,
             INC_INCT_CODE,
             INC_INCT_SHT_DESC,
             INC_DATE,
             INC_ORIGIN,
             INC_ADDRESSED_TO,
             INC_RECEIVED_BY,
             INC_REMARKS,
             INC_POL_POLICY_NO,
             INC_CLM_NO,
             INC_IDEP_CODE,
             INC_QUOT_NO,
             INC_SHT_DESC,
             INC_IDEP_SHT_DESC,
             INCC_MAILED
        FROM TQC_INCIDENTS, TQC_BRANCHES
       WHERE INC_BRN_CODE = BRN_CODE
         AND INC_ADDRESSED_TO = NVL(v_user, INC_ADDRESSED_TO)
       ORDER BY INC_CODE DESC;
  END;
  PROCEDURE getIncidentStatus(v_inc_status_rec OUT incidentStatusRec,
                              v_inc_code       NUMBER,
                              v_view_mode      VARCHAR2) IS
    v_user VARCHAR2(100) := pkg_global_vars.get_pvarchar2('pkg_global_vars.pvg_username');
  BEGIN
    IF v_view_mode = 'U' THEN
    
      OPEN v_inc_status_rec FOR
        SELECT INCS_ALLOC_TO,
               INCS_PRIORITY,
               INCS_EXP_ACT_DATE,
               INCS_ESCALATION_DATE,
               INCS_COMMENTS,
               INCS_ICTY_DATE,
               INCS_ALLOC_DATE,
               INCS_STATUS,
               INCS_CODE,
               INCS_INC_CODE,
               INCS_ICTY_SHT_DESC,
               INCS_ICTY_BY,
               INCS_ACKNOWLEDGED
          FROM TQC_INCIDENT_STATUS
         WHERE INCS_INC_CODE = v_inc_code;
    ELSIF v_view_mode = 'V' THEN
      OPEN v_inc_status_rec FOR
        SELECT INCS_ALLOC_TO,
               INCS_PRIORITY,
               INCS_EXP_ACT_DATE,
               INCS_ESCALATION_DATE,
               INCS_COMMENTS,
               INCS_ICTY_DATE,
               INCS_ALLOC_DATE,
               INCS_STATUS,
               INCS_CODE,
               INCS_INC_CODE,
               INCS_ICTY_SHT_DESC,
               INCS_ICTY_BY,
               INCS_ACKNOWLEDGED
          FROM TQC_INCIDENT_STATUS
         WHERE INCS_ALLOC_TO = NVL(v_user, INCS_ALLOC_TO);
    END IF;
  END;
  PROCEDURE getIncActions(v_inc_actions OUT incActionsRec) IS
  BEGIN
    OPEN v_inc_actions FOR
      select ICTY_CODE, ICTY_SHT_DESC, ICTY_DESC, ICTY_DATE
        from TQC_INCI_ACTION_TYPES
       WHERE ICTY_SHT_DESC <> 'P';
  END;
  PROCEDURE find_incidence_types(v_inc_types OUT incidentTypesRec) IS
  BEGIN
    OPEN v_inc_types FOR
      SELECT INCT_CODE,
             INCT_SHT_DESC,
             INCT_DESC,
             INCT_OWNER,
             DECODE(INCT_OWNER, 'U', 'User', 'S', 'System') INCT_OWNER_DESC
        FROM TQC_INCIDENT_TYPES;
  
  END;

  PROCEDURE FindIncDepartmentsBySystem(v_incidences OUT incidentDepsRec,
                                       v_sys_code   NUMBER) IS
  BEGIN
    OPEN v_incidences FOR
      SELECT IDEP_CODE,
             IDEP_SHT_DESC,
             IDEP_DESC,
             IDEP_BRN_CODE,
             BRN_SHT_DESC,
             BRN_NAME
        FROM TQC_INC_DEPARTMENTS, TQC_BRANCHES
       WHERE IDEP_SYS_CODE = v_sys_code
         AND IDEP_BRN_CODE = BRN_CODE(+);
  END;

  PROCEDURE find_system_branches(v_incidences OUT branchesRec,
                                 v_sys_code   NUMBER) IS
  BEGIN
    OPEN v_incidences FOR
      SELECT BRN_CODE, BRN_SHT_DESC, BRN_NAME
        FROM TQC_BRANCHES, TQC_SYSTEMS, TQC_ORGANIZATIONS, TQC_REGIONS
       WHERE BRN_REG_CODE = reg_code
         AND SYS_ORG_CODE = ORG_CODE
         AND REG_ORG_CODE = ORG_CODE
         AND SYS_CODE = v_sys_code
       ORDER BY 1;
  END;

END TQC_INCIDENTS_WEB_CURSOR; 
/