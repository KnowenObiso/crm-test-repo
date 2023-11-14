--
-- TQC_MEMO_WEB_CURSOR  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_memo_web_cursor
AS
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20037,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   PROCEDURE getapplevel (v_app_level OUT app_level_rec)
   IS
   BEGIN
      OPEN v_app_level FOR
         SELECT transid, transdesc
           FROM (SELECT 'P' transid, 'Policy' transdesc
                   FROM DUAL
                 UNION
                 SELECT 'C', 'Claims'
                   FROM DUAL
                 UNION
                 SELECT 'L', 'Clients'
                   FROM DUAL
                 UNION
                 SELECT 'V', 'Vendors/Third Parties'
                   FROM DUAL
                 UNION
                 SELECT 'I', 'Internal Memo'
                   FROM DUAL);
   END;

   PROCEDURE getclientsmemo (v_clients_memo OUT clientsmemorec)
   IS
   BEGIN
      OPEN v_clients_memo FOR
         SELECT DISTINCT usr_personel_rank
                    FROM tqc_users
                   WHERE usr_personel_rank IS NOT NULL
         UNION
         SELECT DISTINCT comem_done_by
                    FROM tqc_company_memos
                   WHERE comem_appl_lvl = 'I' AND comem_done_by IS NOT NULL;
   END;

   PROCEDURE getvendorsmemo (
      v_vendors_memo   OUT   vendorsmemorec,
      v_appl_lvl             VARCHAR2,
      v_trans_type           VARCHAR2
   )
   IS
   BEGIN
      OPEN v_vendors_memo FOR
         SELECT comem_file_no, comem_code, comem_desc, comem_date,
                comem_subject, comem_appl_lvl, comem_done_by,
                spr_name cor_name, comem_prp_code
           FROM tqc_company_memos, tqc_service_providers
          WHERE comem_appl_lvl = v_appl_lvl
            AND NVL (comem_authorized, 'N') =
                                          DECODE (v_trans_type,
                                                  'E', 'N',
                                                  'Y'
                                                 )
            AND comem_prp_code = spr_code
            AND comem_status = 'L';
   END;

   PROCEDURE getinternalmemo (
      v_internal_memo   OUT   internalmemorec,
      v_appl_lvl              VARCHAR2,
      v_trans_type            VARCHAR2
   )
   IS
   BEGIN
      OPEN v_internal_memo FOR
         SELECT comem_file_no, comem_code, comem_date, comem_subject,
                comem_done_by, comem_appl_lvl
           FROM tqc_company_memos;
   /*WHERE COMEM_APPL_LVL = v_appl_lvl
   AND NVL(COMEM_AUTHORIZED,'N') = DECODE(v_trans_type,'E','N','Y')
   AND NVL(COMEM_STATUS,'L')='L';*/
   END;

   PROCEDURE getquotationmemo (v_quotation_memo OUT quotationmemorec)
   IS
   BEGIN
      OPEN v_quotation_memo FOR
         SELECT DISTINCT quot_no pol_policy_no,
                         quot_agnt_sht_desc pol_agnt_sht_desc,
                         TO_CHAR (quot_code) quot_code,
                         NULL pol_ren_endos_no, quot_code batch_no,
                         clnt_name || ' ' || clnt_other_names insured,
                         quot_brn_code,
                         clnt_postal_addrs || ', ' || twn_name address,
                            NVL2 (clnt_zip_code,
                                  clnt_zip_code || '-',
                                  ''
                                 )
                         || twn_name code_town,
                         clnt_code prp_code,
                         quot_agnt_agent_code pol_agnt_agent_code
                    FROM gin_quotations, tqc_clients, tqc_towns
                   WHERE quot_prp_code = clnt_code AND clnt_twn_code = twn_code(+)
                ORDER BY quot_no DESC;
   END;

   PROCEDURE getpolicymemo (v_policy_memo OUT policymemorec)
   IS
   BEGIN
      OPEN v_policy_memo FOR
         SELECT DISTINCT pol_policy_no, pol_agnt_sht_desc,
                         TO_CHAR (pol_batch_no) pol_batch_no,
                         pol_ren_endos_no, pol_batch_no batch_no,
                         clnt_name || ' ' || clnt_other_names insured,
                         pol_brn_code,
                         clnt_postal_addrs || ', ' || twn_name address,
                            NVL2 (clnt_zip_code,
                                  clnt_zip_code || '-',
                                  ''
                                 )
                         || twn_name code_town,
                         clnt_code prp_code, pol_agnt_agent_code
                    FROM gin_policies, tqc_clients, tqc_towns
                   WHERE pol_prp_code = clnt_code AND clnt_twn_code = twn_code(+)
                ORDER BY pol_policy_no, pol_batch_no DESC;
   END;

   PROCEDURE getclaimsmemo (v_claim_memo OUT claimsmemorec)
   IS
   BEGIN
      OPEN v_claim_memo FOR
         SELECT DISTINCT cmb_claim_no, pol_agnt_sht_desc, cmb_pol_policy_no,
                         TO_CHAR (cmb_pol_batch_no) cmb_pol_batch_no,
                         clnt_name || ' ' || clnt_other_names insured,
                         pol_brn_code, pol_prp_code,
                         clnt_postal_addrs || ', ' || twn_name address,
                            NVL2 (clnt_zip_code,
                                  clnt_zip_code || '-',
                                  ''
                                 )
                         || twn_name code_town,
                         pol_agnt_agent_code, clnt_code prp_code
                    FROM gin_claim_master_bookings,
                         gin_policies,
                         tqc_clients,
                         tqc_towns
                   WHERE cmb_prp_code = clnt_code
                     AND clnt_twn_code = twn_code(+)
                     AND pol_batch_no = cmb_pol_batch_no;
   END;

   PROCEDURE getcasesmemo (
      v_cases_memo     OUT   casesmemorec,
      v_claim_number         VARCHAR2
   )
   IS
   BEGIN
      OPEN v_cases_memo FOR
         SELECT gcc_code, gcc_case_no, gcc_court_name
           FROM gin_court_cases
          WHERE gcc_cmb_claim_no = NVL (v_claim_number, gcc_cmb_claim_no);
   END;

   PROCEDURE getmemotype (v_memo_type OUT memotyperec, v_appl_lvl VARCHAR2)
   IS
   BEGIN
      OPEN v_memo_type FOR
         SELECT   UPPER (mtyp_memo_type) mtyp_memo_type,
                  TO_CHAR (mtyp_code) mtyp_code
             FROM tqc_memo_types
            WHERE mtyp_appl_lvl =
                     DECODE (v_appl_lvl,
                             'L', mtyp_appl_lvl,
                             'V', mtyp_appl_lvl,
                             v_appl_lvl
                            )
              AND mtyp_status NOT IN ('I')
         ORDER BY 1;
   END;

   PROCEDURE getagentscclist (
      v_agents_rec   OUT   agentscclistrec,
      v_agn_code           NUMBER
   )
   IS
   BEGIN
      OPEN v_agents_rec FOR
         SELECT agn_sht_desc, agn_name agnt_agent_name, agn_postal_address,
                NVL2 (agn_zip, agn_zip || '-', '') || twn_name code_town
           FROM tqc_agencies, tqc_towns
          WHERE agn_code = NVL (v_agn_code, agn_code) AND agn_twn_code = twn_code(+);
   END;

   PROCEDURE getclaimantscclist (
      v_claimants_rec   OUT   claimantscclistrec,
      v_claim_no              VARCHAR2
   )
   IS
   BEGIN
      OPEN v_claimants_rec FOR
         SELECT cld_surname || ' ' || cld_other_names client, cld_address,
                NVL2 (cld_zip, cld_zip || '-', '') || cld_mail_twn code_town,
                NULL, NULL
           FROM gin_claimants, gin_claim_master_bookings,
                gin_rgstd_claimants
          WHERE reg_cmb_claim_no = cmb_claim_no
            AND reg_cld_code = cld_code
            AND cmb_claim_no = v_claim_no;
   END;

   PROCEDURE getaddresselist (
      v_refcur           OUT      addressee_ref,
      v_addressee_type   IN       VARCHAR2,
      v_appl_lvl         IN       VARCHAR2,
      v_pol_batch_no     IN       NUMBER,
      v_claim_no         IN       VARCHAR2,
      v_agnt_code        IN       NUMBER,
      v_clnt_code        IN       NUMBER,
      v_quot_code        IN       NUMBER
   )
   IS
   BEGIN
      --RAISE_ERROR('v_appl_lvl'||v_appl_lvl||'v_addressee_type'||v_addressee_type);
      IF v_addressee_type = 1
      THEN
         OPEN v_refcur FOR
            SELECT agn_name agnt_agent_name, agn_sht_desc agnt_sht_desc,
                   agn_postal_address,
                   NVL2 (agn_zip, agn_zip || '-', '') || twn_name code_town
              FROM tqc_agencies, tqc_towns
             WHERE agn_code = NVL (v_agnt_code, agn_code)
               AND agn_twn_code = twn_code(+);
      ELSIF v_addressee_type = 2
      THEN
         OPEN v_refcur FOR
            SELECT cld_surname || ' ' || cld_other_names client, NULL,
                   cld_address,
                      NVL2 (cld_zip, cld_zip || '-', '')
                   || cld_mail_twn code_town
              FROM gin_claim_master_bookings,
                   gin_rgstd_claimants,
                   gin_claimants
             WHERE reg_cmb_claim_no = cmb_claim_no
               AND reg_cmb_claim_no = v_claim_no
               AND reg_cld_code = cld_code(+)
               AND reg_third_party != 'T'
            UNION
            SELECT clnt_name || ' ' || clnt_other_names client, clnt_sht_desc,
                   clnt_postal_addrs,
                      NVL2 (clnt_zip_code, clnt_zip_code || '-',
                            '')
                   || twn_name code_town
              FROM gin_claim_master_bookings,
                   gin_rgstd_claimants,
                   tqc_clients,
                   tqc_towns
             WHERE reg_cmb_claim_no = cmb_claim_no
               AND reg_cmb_claim_no = v_claim_no
               AND reg_cld_code = clnt_code(+)
               AND clnt_twn_code = twn_code(+)
               AND reg_third_party != 'T';
      ELSIF v_addressee_type = 3
      THEN
         IF v_appl_lvl IN ('U', 'C')
         THEN
            --RAISE_ERROR('v_appl_lvl'||v_appl_lvl);
            OPEN v_refcur FOR
               SELECT DISTINCT clnt_name || ' ' || clnt_other_names NAME,
                               clnt_sht_desc,
                               clnt_postal_addrs || ', ' || twn_name address,
                                  NVL2 (clnt_zip_code,
                                        clnt_zip_code || '-',
                                        ''
                                       )
                               || twn_name code_town
                          FROM tqc_clients, gin_policies, tqc_towns
                         WHERE pol_prp_code = NVL (v_clnt_code, pol_prp_code)
                           AND clnt_twn_code = twn_code(+)
                           AND pol_prp_code = clnt_code;
         ELSIF v_appl_lvl = 'Q'
         THEN
            OPEN v_refcur FOR
               SELECT DISTINCT clnt_name || ' ' || clnt_other_names NAME,
                               clnt_sht_desc,
                               clnt_postal_addrs || ', ' || twn_name address,
                                  NVL2 (clnt_zip_code,
                                        clnt_zip_code || '-',
                                        ''
                                       )
                               || twn_name code_town
                          FROM tqc_clients, gin_quotations, tqc_towns
                         WHERE quot_prp_code =
                                             NVL (v_clnt_code, quot_prp_code)
                           AND clnt_twn_code = twn_code(+)
                           AND quot_prp_code = clnt_code;
         END IF;
      ELSIF v_addressee_type = 4
      THEN
         IF v_appl_lvl = 'Q'
         THEN
            OPEN v_refcur FOR
               SELECT DISTINCT clnt_name || ' ' || clnt_other_names NAME,
                               clnt_sht_desc prp_sht_desc, clnt_postal_addrs,
                                  NVL2 (clnt_zip_code,
                                        clnt_zip_code || '-',
                                        ''
                                       )
                               || twn_name code_town
                          FROM gin_quot_risks, tqc_clients, tqc_towns
                         WHERE qr_prp_code = clnt_code
                           AND clnt_twn_code = twn_code
                           AND qr_quot_code = NVL (v_quot_code, qr_quot_code);
         ELSE
            OPEN v_refcur FOR
               SELECT DISTINCT clnt_name || ' ' || clnt_other_names NAME,
                               clnt_sht_desc prp_sht_desc, clnt_postal_addrs,
                                  NVL2 (clnt_zip_code,
                                        clnt_zip_code || '-',
                                        ''
                                       )
                               || twn_name code_town
                          FROM gin_quot_risks, tqc_clients, tqc_towns
                         WHERE qr_prp_code = clnt_code
                           AND clnt_twn_code = twn_code
                           AND qr_quot_code = NVL (v_quot_code, qr_quot_code);
         END IF;
      ELSIF v_addressee_type = 6
      THEN
         OPEN v_refcur FOR
            SELECT spr_name cor_name, spr_sht_desc,
                   spr_postal_address cor_post_add,
                      NVL2 (spr_zip, spr_zip || '-', '')
                   || ' '
                   || twn_name code_town
              FROM tqc_service_providers,
                   gin_claim_master_bookings,
                   tqc_service_provider_types,
                   gin_appnt_correspondents,
                   tqc_towns
             WHERE apco_cor_code = spr_code
               AND spr_twn_code = twn_code(+)
               AND spr_spt_code = spt_code
               AND apco_cmb_claim_no = cmb_claim_no
               AND cmb_claim_no = v_claim_no;
      ELSIF v_addressee_type = 11
      THEN
         OPEN v_refcur FOR
            SELECT usr_username, usr_name, NULL usr_email, NULL usr_status
              FROM tqc_users
             WHERE usr_type = 'G' AND usr_code IN (SELECT mg_usr_code
                                                     FROM gin_mail_group);
      ELSIF v_addressee_type = 7
      THEN
         OPEN v_refcur FOR
            SELECT   agn_name, agn_sht_desc, agn_postal_address,
                     NVL2 (agn_zip, agn_zip || '-', '') || twn_name
                                                                   code_town
                FROM tqc_account_types, tqc_agencies, tqc_towns
               WHERE agn_act_code = act_code
                 AND agn_twn_code = twn_code(+)
                 AND act_type_id != 'D'
                 AND act_code IN (4, 5, 6, 7)
                 AND agn_code != 0
                 AND agn_code IN (
                        SELECT fc_agnt_agent_code
                          FROM gin_facre_cessions
                         WHERE fc_pol_batch_no =
                                  NVL (TO_NUMBER (v_pol_batch_no),
                                       fc_pol_batch_no
                                      )
                        UNION
                        SELECT fid_agnt_agent_code
                          FROM gin_facre_in_dtls
                         WHERE fid_pol_batch_no =
                                  NVL (TO_NUMBER (v_pol_batch_no),
                                       fid_pol_batch_no
                                      )
                        UNION
                        SELECT trpa_agnt_agent_code
                          FROM gin_policy_rein_risk_details,
                               gin_treaty_participants
                         WHERE trpa_rei_code = ptotr_rei_code
                           AND ptotr_pol_batch_no =
                                  NVL (TO_NUMBER (v_pol_batch_no),
                                       ptotr_pol_batch_no
                                      ))
            ORDER BY 1;
      END IF;
   END;

   PROCEDURE getrecipients (
      v_recipients      OUT   recipientlistrec,
      rec_type                VARCHAR2,
      claim_no                VARCHAR2 DEFAULT NULL,
      agent_code              NUMBER DEFAULT NULL,
      v_prp_code              NUMBER DEFAULT NULL,
      v_batch_no              NUMBER DEFAULT NULL,
      v_policy_number         VARCHAR2 DEFAULT NULL
   )
   IS
   BEGIN
      IF rec_type = 'CL'
      THEN
         OPEN v_recipients FOR
            SELECT cld_surname || ' ' || cld_other_names names,
                   NULL description, cld_address address,
                      NVL2 (cld_zip, cld_zip || '-', '')
                   || cld_mail_twn code_town
              FROM gin_claimants,
                   gin_claim_master_bookings,
                   gin_rgstd_claimants
             WHERE reg_cmb_claim_no = cmb_claim_no
               AND reg_cld_code = cld_code
               AND cmb_claim_no = claim_no;
      ELSIF rec_type = 'A'
      THEN
         OPEN v_recipients FOR
            SELECT agn_name agnt_agent_name, agn_sht_desc description,
                   agn_postal_address address,
                   NVL2 (agn_zip, agn_zip || '-', '') || twn_name code_town
              FROM tqc_agencies, tqc_towns
             WHERE agn_code = NVL (agent_code, agn_code)
               AND agn_twn_code = twn_code(+);
      ELSIF rec_type = 'CO'
      THEN
         OPEN v_recipients FOR
            SELECT spr_name NAME, NULL description,
                   spr_postal_address address,
                      NVL2 (spr_zip, spr_zip || '-', '')
                   || ' '
                   || twn_name code_town
              FROM tqc_service_providers,
                   gin_claim_master_bookings,
                   tqc_service_provider_types,
                   gin_appnt_correspondents,
                   tqc_towns
             WHERE apco_cor_code = spr_code
               AND spr_twn_code = twn_code(+)
               AND spr_spt_code = spt_code
               AND apco_cmb_claim_no = cmb_claim_no
               AND cmb_claim_no = claim_no;
      ELSIF rec_type = 'RI'
      THEN
         OPEN v_recipients FOR
            SELECT   agn_name NAME, agn_sht_desc description,
                     agn_postal_address address,
                     NVL2 (agn_zip, agn_zip || '-', '') || twn_name
                                                                   code_town
                FROM tqc_account_types, tqc_agencies, tqc_towns
               WHERE agn_act_code = act_code
                 AND agn_twn_code = twn_code(+)
                 AND act_type_id != 'D'
                 AND act_code IN (4, 5, 6, 7)
                 AND agn_code != 0
                 AND agn_code IN (
                        SELECT fc_agnt_agent_code
                          FROM gin_facre_cessions
                         WHERE fc_pol_batch_no =
                                  NVL (TO_NUMBER (v_batch_no),
                                       fc_pol_batch_no)
                        UNION
                        SELECT fid_agnt_agent_code
                          FROM gin_facre_in_dtls
                         WHERE fid_pol_batch_no =
                                  NVL (TO_NUMBER (v_batch_no),
                                       fid_pol_batch_no
                                      )
                        UNION
                        SELECT trpa_agnt_agent_code
                          FROM gin_policy_rein_risk_details,
                               gin_treaty_participants
                         WHERE trpa_rei_code = ptotr_rei_code
                           AND ptotr_pol_batch_no =
                                  NVL (TO_NUMBER (v_batch_no),
                                       ptotr_pol_batch_no
                                      ))
            ORDER BY 1;
      ELSIF rec_type = 'I'
      THEN
         OPEN v_recipients FOR
            SELECT clnt_name || ' ' || clnt_other_names NAME,
                   clnt_sht_desc description, clnt_postal_addrs address,
                      NVL2 (clnt_zip_code, clnt_zip_code || '-',
                            '')
                   || twn_name code_town
              FROM tqc_clients,
                   gin_policy_insureds,
                   gin_insured_property_unds,
                   tqc_towns,
                   gin_claim_master_bookings
             WHERE polin_prp_code = clnt_code
               AND ipu_polin_code = polin_code
               AND ipu_code = cmb_ipu_code
               AND ipu_pol_policy_no =
                                      NVL (v_policy_number, ipu_pol_policy_no)
               AND ipu_code = DECODE (claim_no, NULL, ipu_code, cmb_ipu_code);
      ELSIF rec_type = 'CLI'
      THEN
         OPEN v_recipients FOR
            SELECT clnt_name || ' ' || clnt_other_names NAME,
                   clnt_sht_desc description, clnt_postal_addrs address,
                      NVL2 (clnt_zip_code, clnt_zip_code || '-',
                            '')
                   || twn_name code_town
              FROM tqc_clients, gin_policies, tqc_towns
             WHERE pol_prp_code = NVL (v_prp_code, pol_prp_code)
               AND clnt_twn_code = twn_code(+)
               AND pol_prp_code = clnt_code;

         NULL;
      ELSIF rec_type = 'C'
      THEN
         OPEN v_recipients FOR
            SELECT comem_done_by NAME, comem_file_no description,
                   comem_subject address, comem_cmb_claim_no code_town
              FROM tqc_company_memos
             WHERE comem_appl_lvl = 'C'
               AND NVL (comem_authorized, 'N') = 'N'
               AND NVL (comem_status, 'L') = 'L';
      END IF;
   END;

   PROCEDURE getmemobranches (v_branches OUT memobranchesrec)
   IS
   BEGIN
      OPEN v_branches FOR
         SELECT brn_code, brn_name
           FROM tqc_branches;
   END;

   PROCEDURE getmemopolicies (v_policies OUT memopoliciesrec)
   IS
   BEGIN
      OPEN v_policies FOR
         SELECT DISTINCT pol_policy_no, pol_agnt_sht_desc,
                         TO_CHAR (pol_batch_no) pol_batch_no,
                         pol_ren_endos_no, pol_batch_no batch_no,
                         clnt_name || ' ' || clnt_other_names insured,
                         pol_brn_code,
                         clnt_postal_addrs || ', ' || twn_name address,
                            NVL2 (clnt_zip_code,
                                  clnt_zip_code || '-',
                                  ''
                                 )
                         || twn_name code_town,
                         clnt_code prp_code, pol_agnt_agent_code
                    FROM gin_policies, tqc_clients, tqc_towns
                   WHERE pol_prp_code = clnt_code AND clnt_twn_code = twn_code(+)
                ORDER BY pol_policy_no, pol_batch_no DESC;
   END;

   PROCEDURE getmemotypes (v_memo_type OUT memotypesrec, v_appl_level VARCHAR2)
   IS
   BEGIN
      OPEN v_memo_type FOR
         SELECT   UPPER (mtyp_memo_type) mtyp_memo_type,
                  TO_CHAR (mtyp_code) mtyp_code
             FROM tqc_memo_types
            WHERE mtyp_appl_lvl =
                     DECODE (v_appl_level,
                             'L', mtyp_appl_lvl,
                             'V', mtyp_appl_lvl,
                             v_appl_level
                            )
              AND mtyp_status NOT IN ('I')
         ORDER BY 1;
   END;

   PROCEDURE getcclist (v_cc_list OUT cclistrec, v_cc_code NUMBER)
   IS
   BEGIN
      OPEN v_cc_list FOR
         SELECT cc_code, cc_comem_code, cc_name, cc_address, cc_remarks,
                cc_type
           FROM tqc_company_memo_cc
          WHERE cc_comem_code = v_cc_code;
   END;

  PROCEDURE search_memos(v_memo_rec OUT memo_search_ref, v_mem_code NUMBER) IS
  BEGIN
    OPEN v_memo_rec FOR
      SELECT comem_code,
             comem_sys_code,
             comem_desc,
             comem_date,
             comem_cmb_claim_no,
             mtyp_memo_type,
             comem_address,
             comem_pol_policy_no,
             comem_appl_lvl,
             comem_done_by,
             comem_authorized,
             comem_your_ref,
             comem_addressee,
             comem_signatory,
             comem_mtyp_code,
             comem_pol_ren_endos_no,
             comem_brn_code,
             comem_prepared_by,
             comem_corr_code,
             comem_ref,
             comem_pol_batch_no,
             comem_authorized_date,
             comem_file_no,
             comem_authorized_by,
             comem_sign_rank,
             comem_insured,
             comem_rpt_type,
             comem_status,
             comem_codetown,
             comem_pro_desc,
             comem_court_case_no,
             comem_gcc_code,
             comem_quot_code,
             comem_quot_no,
             brn_name,
             comem_addressee_type,
             memh_memo_subject,
             memdtls_detail,
             memh_code,
             memdtls_code,comem_issued
        FROM tqc_company_memos,
             tqc_branches,
             tqc_memo_header,
             tqc_memo_header_dtls,
             tqc_memo_types
      --AND CMB_SCL_CODE=SCL_CODE
       WHERE brn_code(+) = comem_brn_code
         AND comem_code = memh_comem_code(+)
         AND memdtls_memh_code(+) = memh_code
         AND comem_mtyp_code = mtyp_code(+)
         AND comem_code = NVL(v_mem_code, comem_code);
  END;
   PROCEDURE findmemodetails (
      v_code     IN       VARCHAR2,
      v_level    IN       VARCHAR2,
      v_cursor   OUT      memo_details_ref
   )
   IS
      v_batch_no   NUMBER;
   BEGIN
      IF v_level = 'C'
      THEN
         BEGIN
            SELECT cmb_pol_batch_no
              INTO v_batch_no
              FROM gin_claim_master_bookings
             WHERE cmb_claim_no = v_code;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               raise_error ('Data not found v_code====' || v_code);
            WHEN OTHERS
            THEN
               raise_error ('Others v_code====' || v_code);
         END;

         IF v_batch_no IS NOT NULL
         THEN
            OPEN v_cursor FOR
               SELECT pol_prp_code, pol_agnt_agent_code
                 FROM gin_policies
                WHERE pol_batch_no = v_batch_no;
         END IF;
      ELSIF v_level = 'P'
      THEN
         OPEN v_cursor FOR
            SELECT pol_prp_code, pol_agnt_agent_code
              FROM gin_policies
             WHERE pol_batch_no = TO_NUMBER (v_code);
      ELSIF v_level = 'U'
      THEN
         OPEN v_cursor FOR
            SELECT pol_prp_code, pol_agnt_agent_code
              FROM gin_policies
             WHERE pol_policy_no = v_code;
      ELSIF v_level = 'Q'
      THEN
         OPEN v_cursor FOR
            SELECT NULL pol_prp_code, NULL pol_agnt_agent_code
              FROM gin_quotations
             WHERE quot_no = v_code;
      END IF;
   END;

   PROCEDURE getmemos (
      v_memo_rec   OUT   memorec,
      v_auth             VARCHAR2,
      v_mem_code         NUMBER DEFAULT NULL,
      v_appl_lvl         VARCHAR2
   )
   IS
   BEGIN
      IF v_auth = 'N'
      THEN
         OPEN v_memo_rec FOR
            SELECT comem_code, comem_sys_code, comem_desc, comem_date,
                   comem_cmb_claim_no, comem_subject, comem_address,
                   comem_pol_policy_no, comem_appl_lvl, comem_done_by,
                   comem_authorized, comem_your_ref, comem_addressee,
                   comem_signatory, comem_mtyp_code, comem_pol_ren_endos_no,
                   comem_brn_code, comem_prepared_by, comem_corr_code,
                   comem_ref, comem_pol_batch_no, comem_authorized_date,
                   comem_file_no, comem_authorized_by, comem_sign_rank,
                   comem_insured, comem_rpt_type, comem_status,
                   comem_codetown, comem_pro_desc, comem_court_case_no,
                   comem_gcc_code, comem_quot_code, comem_quot_no, brn_name,
                   NULL scl_code, NULL scl_desc
              FROM tqc_company_memos, tqc_branches
             WHERE comem_authorized = NVL (v_auth, 'N')
               AND brn_code = comem_brn_code
               AND comem_appl_lvl = NVL (v_appl_lvl, comem_appl_lvl)
               AND comem_code = NVL (v_mem_code, comem_code);
      ELSE
         OPEN v_memo_rec FOR
            SELECT comem_code, comem_sys_code, comem_desc, comem_date,
                   comem_cmb_claim_no, comem_subject, comem_address,
                   comem_pol_policy_no, comem_appl_lvl, comem_done_by,
                   comem_authorized, comem_your_ref, comem_addressee,
                   comem_signatory, comem_mtyp_code, comem_pol_ren_endos_no,
                   comem_brn_code, comem_prepared_by, comem_corr_code,
                   comem_ref, comem_pol_batch_no, comem_authorized_date,
                   comem_file_no, comem_authorized_by, comem_sign_rank,
                   comem_insured, comem_rpt_type, comem_status,
                   comem_codetown, comem_pro_desc, comem_court_case_no,
                   comem_gcc_code, comem_quot_code, comem_quot_no, brn_name,
                   NULL scl_code, NULL scl_desc
              FROM tqc_company_memos, tqc_branches
             WHERE comem_authorized = NVL (v_auth, 'Y')
               AND brn_code = comem_brn_code
               AND comem_appl_lvl = NVL (v_appl_lvl, comem_appl_lvl)
               AND comem_code = NVL (v_mem_code, comem_code);
      END IF;
   END;

   PROCEDURE getmemoheaderdtls (
      v_mheader_dtls   OUT   memoheaderdtlsrec,
      v_memo_code            NUMBER
   )
   IS
   BEGIN
      OPEN v_mheader_dtls FOR
         SELECT memdtls_memh_code, memh_memo_subject, memdtls_code
           FROM tqc_memo_header_dtls, tqc_memo_header
          WHERE memdtls_memh_code = memh_code
            AND memh_comem_code = v_memo_code;
   END;

   PROCEDURE getmemotypes (
      v_memo_type       OUT      memotypesrec,
      v_subclass_code            NUMBER,
      v_appl_level               VARCHAR2,
      v_sys_code        IN       NUMBER
   )
   IS
      v_class_mem_lvl   VARCHAR2 (5);
   BEGIN
      IF v_sys_code = 37
      THEN
         BEGIN
            SELECT param_value
              INTO v_class_mem_lvl
              FROM gin_parameters
             WHERE param_name = 'MEMO_CLASS_LVL';
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               v_class_mem_lvl := 'N';
         END;
      ELSE
         v_class_mem_lvl := 'N';
      END IF;

      IF NVL (v_class_mem_lvl, 'Y') = 'Y'
      THEN
         OPEN v_memo_type FOR
            SELECT   UPPER (mtyp_memo_type) mtyp_memo_type, mtyp_code
                FROM tqc_memo_types
               WHERE mtyp_appl_lvl =
                        DECODE (v_appl_level,
                                'L', mtyp_appl_lvl,
                                'V', mtyp_appl_lvl,
                                v_appl_level
                               )
                 AND mtyp_status NOT IN ('I')
                 AND (   NVL (mtyp_sub_code, -2000) =
                            DECODE (NVL (v_subclass_code, -2000),
                                    NVL (mtyp_sub_code, -2000), NVL
                                                             (v_subclass_code,
                                                              -2000
                                                             )
                                   )
                      OR (mtyp_sub_code IS NULL)
                     )
            ORDER BY 1;
      ELSE
         OPEN v_memo_type FOR
            SELECT   UPPER (mtyp_memo_type) mtyp_memo_type, mtyp_code
                FROM tqc_memo_types
               WHERE mtyp_appl_lvl =
                        DECODE (v_appl_level,
                                'L', mtyp_appl_lvl,
                                'V', mtyp_appl_lvl,
                                v_appl_level
                               )
                 AND mtyp_status NOT IN ('I')
            ORDER BY 1;
      END IF;
   END;

   PROCEDURE getinsuredcclist (
      v_claimants_rec   OUT   claimantscclistrec,
      v_pol_no                VARCHAR2,
      v_claim_no              VARCHAR2
   )
   IS
   BEGIN
      OPEN v_claimants_rec FOR
         SELECT polin_code, clnt_sht_desc, clnt_name, twn_name address,
                   NVL2 (clnt_zip_code, clnt_zip_code || '-', '')
                || twn_name code_town
           FROM gin_claim_master_bookings,
                gin_policies,
                gin_policy_insureds,
                tqc_clients,
                tqc_towns
          WHERE cmb_pol_batch_no = pol_batch_no
            AND polin_pol_batch_no = pol_batch_no
            AND polin_prp_code = clnt_code
            AND clnt_twn_code = twn_code(+)
            AND cmb_claim_no = v_claim_no;
   END;

   PROCEDURE getmemosandletters (
      v_letterandmemos   OUT      letterandmemosrec,
      v_appl_level                VARCHAR2,
      v_trans_type       IN       VARCHAR2,
      v_cmb_no           IN       VARCHAR2
   )
   IS
   BEGIN
      --RAISE_ERROR('App Level '||v_appl_level||' Trans Type '||v_trans_type||' tran no '||v_cmb_no);
      IF v_appl_level = 'L'
      THEN
         OPEN v_letterandmemos FOR
            SELECT comem_file_no, comem_code, comem_desc, comem_date,
                   clnt_name || ' ' || clnt_other_names clientname,
                   comem_subject, comem_done_by, comem_appl_lvl,
                   clnt_sht_desc, clnt_sht_desc prp_sht_desc, comem_prp_code,
                   comem_cmb_claim_no
              FROM tqc_company_memos, tqc_clients
             WHERE comem_appl_lvl = v_appl_level
               AND NVL (comem_authorized, 'N') =
                                          DECODE (v_trans_type,
                                                  'E', 'N',
                                                  'Y'
                                                 )
               AND comem_prp_code = clnt_code
               AND comem_status = 'L';
      ELSIF v_appl_level = 'V'
      THEN
         OPEN v_letterandmemos FOR
            SELECT comem_file_no, comem_code, comem_desc, comem_date,
                   spr_name cor_name, comem_subject, comem_done_by,
                   comem_appl_lvl, spr_sht_desc, spr_sht_desc,
                   comem_prp_code, comem_cmb_claim_no
              FROM tqc_company_memos, tqc_service_providers
             WHERE comem_appl_lvl = v_appl_level
               AND NVL (comem_authorized, 'N') =
                                          DECODE (v_trans_type,
                                                  'E', 'N',
                                                  'Y'
                                                 )
               AND comem_prp_code = spr_code
               AND comem_status = 'L';
      ELSIF v_appl_level = 'I'
      THEN
         OPEN v_letterandmemos FOR
            SELECT comem_file_no, comem_code, comem_desc, comem_date, NULL,
                   comem_subject, comem_done_by, comem_appl_lvl, NULL, NULL,
                   comem_prp_code, comem_cmb_claim_no
              FROM tqc_company_memos
             WHERE comem_appl_lvl = v_appl_level
               AND NVL (comem_authorized, 'N') =
                                          DECODE (v_trans_type,
                                                  'E', 'N',
                                                  'Y'
                                                 )
               AND NVL (comem_status, 'L') = 'L';
      ELSIF v_appl_level = 'U'
      THEN
         --RAISE_ERROR('App Level '||v_appl_level||' Trans Type '||v_trans_type);
         OPEN v_letterandmemos FOR
            SELECT comem_file_no, comem_code, comem_desc, comem_date, NULL,
                   comem_subject, comem_done_by, comem_appl_lvl, NULL, NULL,
                   comem_prp_code, comem_pol_policy_no comem_cmb_claim_no
              FROM tqc_company_memos
             WHERE comem_appl_lvl = v_appl_level
               AND NVL (comem_authorized, 'N') =
                                          DECODE (v_trans_type,
                                                  'E', 'N',
                                                  'Y'
                                                 )
               AND NVL (comem_status, 'L') = 'L'
               AND NVL (comem_pol_policy_no, v_cmb_no) =
                                           NVL (v_cmb_no, comem_pol_policy_no);
      ELSIF v_appl_level = 'C'
      THEN
         --RAISE_ERROR('App Level '||v_appl_level||' Trans Type '||v_trans_type);
         OPEN v_letterandmemos FOR
            SELECT comem_file_no, comem_code, comem_desc, comem_date, NULL,
                   comem_subject, comem_done_by, comem_appl_lvl, NULL, NULL,
                   comem_prp_code, comem_cmb_claim_no
              FROM tqc_company_memos
             WHERE comem_appl_lvl = v_appl_level
               AND NVL (comem_authorized, 'N') =
                                          DECODE (v_trans_type,
                                                  'E', 'N',
                                                  'Y'
                                                 )
               AND NVL (comem_status, 'L') = 'L'
               AND NVL (comem_cmb_claim_no, v_cmb_no) =
                                            NVL (v_cmb_no, comem_cmb_claim_no);
      ELSIF v_appl_level = 'Q'
      THEN
         --RAISE_ERROR('App Level '||v_appl_level||' Trans Type '||v_trans_type);
         OPEN v_letterandmemos FOR
            SELECT comem_file_no, comem_code, comem_desc, comem_date, NULL,
                   comem_subject, comem_done_by, comem_appl_lvl, NULL, NULL,
                   comem_prp_code, comem_quot_no comem_cmb_claim_no
              FROM tqc_company_memos
             WHERE comem_appl_lvl = v_appl_level
               AND NVL (comem_authorized, 'N') =
                                          DECODE (v_trans_type,
                                                  'E', 'N',
                                                  'Y'
                                                 )
               AND NVL (comem_status, 'L') = 'L'
               AND NVL (comem_quot_no, v_cmb_no) =
                                                 NVL (v_cmb_no, comem_quot_no);
      END IF;
   END;

   PROCEDURE find_memos (v_memo_type OUT memovalsrec, v_mytp_code NUMBER)
   IS
   BEGIN
      OPEN v_memo_type FOR
         SELECT memo_code, memo_subject
           FROM tqc_memos
          WHERE memo_mtyp_code = v_mytp_code;
   /*BEGIN
      SELECT PARAM_VALUE
      INTO v_class_mem_lvl
      FROM GIN_PARAMETERS
      WHERE PARAM_NAME='MEMO_CLASS_LVL';
   EXCEPTION
      WHEN NO_DATA_FOUND THEN
           NULL;
   END;

   IF NVL(v_class_mem_lvl,'Y')='Y' THEN
        OPEN v_memo_type FOR
        SELECT UPPER( MTYP_MEMO_TYPE) MTYP_MEMO_TYPE, TO_CHAR(MTYP_CODE) MTYP_CODE
        FROM TQC_MEMO_TYPES
        WHERE MTYP_APPL_LVL=DECODE(v_appl_lvl,'L',MTYP_APPL_LVL,'V',MTYP_APPL_LVL,v_appl_lvl)
        AND MTYP_STATUS NOT IN ('I')
        AND NVL(MTYP_SUB_CODE,-2000)=DECODE(NVL(v_scl_code,-2000),NVL(MTYP_SUB_CODE,-2000),NVL(v_scl_code,-2000))
        ORDER BY 1
   ELSE
        OPEN v_memo_type FOR
      SELECT UPPER( MTYP_MEMO_TYPE) MTYP_MEMO_TYPE, TO_CHAR(MTYP_CODE) MTYP_CODE
        FROM TQC_MEMO_TYPES
        WHERE MTYP_APPL_LVL=DECODE(v_appl_lvl,'L',MTYP_APPL_LVL,'V',MTYP_APPL_LVL,v_appl_lvl)
        AND MTYP_STATUS NOT IN ('I')
        ORDER BY 1
   END IF; */
   END;

   PROCEDURE find_memo_details (v_cursor OUT memovalsrec, v_memo_code IN NUMBER)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT memdet_code, memdet_content
           FROM tqc_memo_details
          WHERE memdet_memo_code = v_memo_code;
   END;

   PROCEDURE find_quotations_memo (v_cursor OUT memo_ref, v_quot_code IN NUMBER)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT comem_file_no, comem_code, comem_desc, comem_date,
                clnt_name || ' ' || clnt_other_names, comem_subject,
                comem_done_by, comem_appl_lvl, clnt_sht_desc,
                clnt_sht_desc prp_sht_desc, comem_prp_code
           FROM tqc_company_memos, tqc_clients
          WHERE comem_quot_code = v_quot_code AND comem_prp_code = clnt_code(+);
   END;

   PROCEDURE find_uw_memos (v_cursor OUT memo_ref, v_code IN NUMBER)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT comem_file_no, comem_code, comem_desc, comem_date,
                comem_subject, comem_done_by, comem_appl_lvl, comem_prp_code
           FROM tqc_company_memos
          WHERE comem_pol_batch_no = v_code;
   END;

   PROCEDURE find_claim_memos (v_cursor OUT memo_ref, v_claim_no IN VARCHAR2)
   IS
   BEGIN
      OPEN v_cursor FOR
         SELECT comem_file_no, comem_code, comem_desc, comem_date,
                comem_subject, comem_done_by, comem_appl_lvl, comem_prp_code
           FROM tqc_company_memos
          WHERE comem_cmb_claim_no = v_claim_no;
   END;

   FUNCTION process_gis_pol_memo (
      v_pol_batch_no   IN   NUMBER,
      v_claim_no       IN   VARCHAR2,
      v_gcc_code       IN   NUMBER,
      v_raw_txt        IN   VARCHAR2,
      v_app_lvl        IN   VARCHAR2
   )
      RETURN VARCHAR2
   IS
      lv_temp_txt           VARCHAR2 (32767);
      v_pol_no              gin_policies.pol_policy_no%TYPE;
      v_clm_no              gin_claim_master_bookings.cmb_claim_no%TYPE;
      v_sa                  NUMBER;
      v_premium             NUMBER;
      v_client              tqc_clients.clnt_name%TYPE;
      v_prem                NUMBER;
      v_date                DATE;
      v_time                DATE;
      v_reg                 VARCHAR2 (100);
      v_loc                 VARCHAR2 (100);
      v_courtname           VARCHAR2 (100);
      v_courtdate           DATE;
      v_caseno              VARCHAR2 (100);
      vcoverfrom            DATE;
      vcoverto              DATE;
      veffdate              DATE;
      vrendate              DATE;
      v_desc                VARCHAR2 (100);
      v_loss_type           VARCHAR2 (100);
      v_item_desc           VARCHAR2 (100);
      v_claimant_type       VARCHAR2 (15);
      v_agent               tqc_agencies.agn_name%TYPE;
      v_int_parties         VARCHAR2 (100);
      v_uw_year             INT;
      v_branch              tqc_branches.brn_name%TYPE;
      v_address             VARCHAR2 (100);
      v_twn                 VARCHAR2 (100);
      v_risk_id             VARCHAR2 (100);
      v_surname             tqc_clients.clnt_name%TYPE;
      v_risk_desc           gin_insured_property_unds.ipu_item_desc%TYPE;
      v_risk_wef            DATE;
      v_risk_wet            DATE;
      v_cover               VARCHAR2 (50);
      v_risk_prem           DECIMAL (20, 2);
      v_risk_value          DECIMAL (20, 2);
      v_currency            VARCHAR2 (5);
      v_agn_code            NUMBER;
      v_agn_name            tqc_agencies.agn_name%TYPE;
      v_agn_add             tqc_agencies.agn_physical_address%TYPE;
      v_client_code         NUMBER;
      v_client_add          VARCHAR2 (100);
      v_insured_name        VARCHAR2 (300);
      v_insured_add         VARCHAR2 (100);
      v_org_name            VARCHAR2 (100);
      v_org_add             VARCHAR2 (100);
      v_ipu_desc            gin_insured_property_unds.ipu_item_desc%TYPE;
      v_amt_words           VARCHAR2 (50);
      v_cur_code            NUMBER;
      v_period              NUMBER;
      v_obligor             gin_bonds.bon_obligor%TYPE;
      v_ob_address          gin_bonds.obligor_addrs%TYPE;
      v_ob_town             gin_bonds.bon_obligor_town%TYPE;
      v_ob_country          gin_bonds.bon_obligor_country%TYPE;
      v_ob_emp              gin_bonds.bon_employer%TYPE;
      v_res_amt             gin_claim_revisions.clmrev_amt%TYPE;
      v_original_estimate   gin_claim_revisions.clmrev_amt%TYPE;
      v_dep_amt             gin_claim_revisions.clmrev_amt%TYPE;
      v_excess_amt          gin_claim_revisions.clmrev_amt%TYPE;
      v_ipu_code            gin_insured_property_unds.ipu_code%TYPE;
      v_clnt_tel            tqc_clients.clnt_tel%TYPE;
      v_peril               gin_claim_perils.clmp_per_pt_sht_desc%TYPE;
      v_excess_rate         NUMBER;
      v_dep_rate            NUMBER;
      v_agn_tel             tqc_agencies.agn_tel1%TYPE;
      v_cla_desc            gin_classes.cla_descn%TYPE;
      v_tot_amt_rsrv        NUMBER;
      v_comp_ret_rsrv       NUMBER;
      v_fee_amt             NUMBER;
      v_paid_amt            NUMBER;
      v_pol_coin_pct        NUMBER;
   BEGIN
      --RAISE_APPLICATION_ERROR(-20001,'ERROR: wonder where is the error');
      lv_temp_txt := v_raw_txt;

      IF v_app_lvl = 'U'
      THEN
         IF v_pol_batch_no IS NOT NULL
         THEN
            BEGIN
               SELECT pol_policy_no, pol_si_diff,
                      gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                      pol_tot_endos_diff_amt, pol_policy_cover_from,
                      pol_policy_cover_to, pol_wef_dt, pol_renewal_dt,
                      agn_name, pol_oth_int_parties, pol_uw_year, brn_name,
                      clnt_postal_addrs, twn_name, ipu_property_id,
                      ipu_item_desc, ipu_wef, ipu_wet, ipu_covt_sht_desc,
                      ipu_endos_diff_amt, ipu_value
                 INTO v_pol_no, v_sa,
                      v_client,
                      v_prem, vcoverfrom,
                      vcoverto, veffdate, vrendate,
                      v_agent, v_int_parties, v_uw_year, v_branch,
                      v_address, v_twn, v_risk_id,
                      v_risk_desc, v_risk_wef, v_risk_wet, v_cover,
                      v_risk_prem, v_risk_value
                 FROM gin_policies,
                      tqc_clients,
                      tqc_agencies,
                      tqc_branches,
                      tqc_towns,
                      tq_gis.gin_insured_property_unds
                WHERE pol_prp_code = clnt_code
                  AND pol_agnt_agent_code = agn_code
                  AND twn_code(+) = clnt_twn_code
                  AND brn_code = pol_brn_code
                  AND ipu_pol_batch_no = pol_batch_no
                  AND pol_batch_no = v_pol_batch_no
                  AND ROWNUM < 2;

               lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
               lv_temp_txt := REPLACE (lv_temp_txt, '[POLNO]', v_pol_no);
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERFROM]',
                           TO_CHAR (vcoverfrom, 'DD/MM/RRRR')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERTO]',
                           TO_CHAR (vcoverto, 'DD/MM/RRRR')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[EFFDATE]',
                           TO_CHAR (veffdate, 'DD/MM/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[RENEWALDATE]',
                           TO_CHAR (vrendate, 'DD/MM/RRRR')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[SA]',
                           TO_CHAR (NVL (v_sa, 0), 'FM999,999,999.00')
                          );
               lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[PREMIUM]',
                           TO_CHAR (NVL (v_prem, 0), 'FM999,999,999.00')
                          );
               lv_temp_txt := REPLACE (lv_temp_txt, '[AGENT]', v_agent);
               lv_temp_txt :=
                          REPLACE (lv_temp_txt, '[INTPARTIES]', v_int_parties);
               lv_temp_txt := REPLACE (lv_temp_txt, '[UWYEAR]', v_uw_year);
               lv_temp_txt := REPLACE (lv_temp_txt, '[BRANCH]', v_branch);
               lv_temp_txt := REPLACE (lv_temp_txt, '[ADDRESS]', v_address);
               lv_temp_txt := REPLACE (lv_temp_txt, '[TOWN]', v_twn);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKID]', v_risk_id);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKDESC]', v_risk_desc);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWEF]', v_risk_wef);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWET]', v_risk_wet);
               lv_temp_txt := REPLACE (lv_temp_txt, '[COVERTYPE]', v_cover);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKPREM]', v_risk_prem);
               lv_temp_txt :=
                            REPLACE (lv_temp_txt, '[RISKVALUE]', v_risk_value);
               --RAISE_ERROR('HERE');
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[WEF]',
                           TO_CHAR (veffdate, 'DD/MM/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[WET]',
                           TO_CHAR (vcoverto, 'DD/MM/YYYY')
                          );
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
               WHEN OTHERS
               THEN
                  raise_application_error (-20001,
                                           'ERROR: ' || SQLERRM (SQLCODE)
                                          );
            END;
         ELSE
            IF v_pol_no IS NOT NULL
            THEN
               lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
            END IF;
         END IF;
      ELSIF v_app_lvl = 'S'
      THEN
         SELECT pol_policy_no, pol_total_sum_insured,
                gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                clnt_name, pol_total_fap, pol_policy_cover_from,
                pol_policy_cover_to, pol_wef_dt, pol_renewal_dt,
                pol_agnt_agent_code, pol_cur_code, pol_oth_int_parties
           INTO v_pol_no, v_sa,
                v_client,
                v_surname, v_prem, vcoverfrom,
                vcoverto, veffdate, vrendate,
                v_agn_code, v_cur_code, v_int_parties
           FROM gin_policies, tqc_clients
          WHERE pol_prp_code = clnt_code AND pol_batch_no = v_pol_batch_no;

         --MESSAGE('[POLICYNO]'||lv_temp_txt);PAUSE;
         lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
         lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
         lv_temp_txt := REPLACE (lv_temp_txt, '[SURNAME]', v_surname);
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[COVERFROM]',
                        TO_CHAR (vcoverfrom, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[COVERTO]',
                        TO_CHAR (vcoverto, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[EFFDATE]',
                        TO_CHAR (veffdate, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[RENEWALDATE]',
                     TO_CHAR (vrendate, 'DD/MM/YYYY')
                    );
         lv_temp_txt := REPLACE (lv_temp_txt, '[INTPARTIES]', v_int_parties);
         --MESSAGE(v_pol_no||lv_temp_txt);PAUSE;
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[SA]',
                     TO_CHAR (NVL (v_sa, 0), 'FM999,999,999.00')
                    );
         -- lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
         lv_temp_txt := REPLACE (lv_temp_txt, '[PREMIUM]', NVL (v_prem, 0));
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[AMTINWORDS]',
                     fms_cheques_pkg.num_to_words (NVL (v_sa, 0), v_cur_code)
                    );

         SELECT MONTHS_BETWEEN (vcoverto, vcoverfrom)
           INTO v_period
           FROM DUAL;

         lv_temp_txt := REPLACE (lv_temp_txt, '[PERIOD]', ROUND (v_period, 0));

         SELECT ipu_item_desc
           INTO v_ipu_desc
           FROM tq_gis.gin_insured_property_unds
          WHERE ipu_pol_batch_no = v_pol_batch_no AND ROWNUM = 1;

         lv_temp_txt := REPLACE (lv_temp_txt, '[ITEMDESC]', v_ipu_desc);

         SELECT org_name, org_phy_addrs
           INTO v_org_name, v_org_add
           FROM tqc_organizations
          WHERE org_code = 2;

         lv_temp_txt := REPLACE (lv_temp_txt, '[ORGNAME]', v_org_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[ORGADDR]', v_org_add);

         SELECT agn_name, agn_physical_address
           INTO v_agn_name, v_agn_add
           FROM tqc_agencies
          WHERE agn_code = v_agn_code;

         lv_temp_txt := REPLACE (lv_temp_txt, '[AGNNAME]', v_agn_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[AGNADDR]', v_agn_add);

         BEGIN
            SELECT gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                   clnt_physical_addrs
              INTO v_insured_name,
                   v_insured_add
              FROM tq_gis.gin_policy_insureds, tqc_clients
             WHERE polin_prp_code = clnt_code
               AND polin_pol_batch_no = v_pol_batch_no
               AND ROWNUM = 1;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         lv_temp_txt := REPLACE (lv_temp_txt, '[INSNAME]', v_insured_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[INSADDR]', v_insured_add);

         BEGIN
            SELECT bon_obligor, obligor_addrs, bon_obligor_town,
                   bon_obligor_country, bon_employer, bon_contract_date
              INTO v_obligor, v_ob_address, v_ob_town,
                   v_ob_country, v_ob_emp, v_date
              FROM gin_bonds, gin_insured_property_unds
             WHERE bon_ipu_code = ipu_code
               AND ipu_pol_batch_no = v_pol_batch_no;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         lv_temp_txt := REPLACE (lv_temp_txt, '[BANK]', v_obligor);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKADD]', v_ob_address);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKTWN]', v_ob_town);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKSTATE]', v_ob_country);
         lv_temp_txt := REPLACE (lv_temp_txt, '[EMPLOYER]', v_ob_emp);
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[CONTRACTDATE]',
                        TO_CHAR (v_date, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
      ELSIF v_app_lvl = 'C'
      THEN
         --RAISE_APPLICATION_ERROR(-20001,'ERROR: wonder where is the error');
         BEGIN
            SELECT DISTINCT pol_coinsure_pct, pol_policy_no, cmb_claim_no,
                            pol_total_sum_insured,
                            DECODE (reg_third_party,
                                    'S', gis_utilities.clnt_name (clnt_name,
                                                                  clnt_surname
                                                                 ),
                                    cld_other_names || ' ' || cld_surname
                                   ) claimant_name,
                            pol_tot_endos_diff_amt, cmb_claim_date,
                            cmb_loss_date_time, cmb_ipu_property_id risk_id,
                            cmb_location, cmb_loss_desc,
                            NVL (cmb_other_cover_details, 'Repairable loss')
                                                                    loss_type,
                            ipu_item_desc risk_description,
                            DECODE (reg_third_party,
                                    'S', 'SELF',
                                    'THIRD PARTY'
                                   ) claimant_type,
                            cla_descn,
                            gis_utilities.clnt_name (clnt_name, clnt_surname)
                                                                      insured
                       INTO v_pol_coin_pct, v_pol_no, v_clm_no,
                            v_sa,
                            v_client,
                            v_prem, v_date,
                            v_time, v_reg,
                            v_loc, v_desc,
                            v_loss_type,
                            v_item_desc,
                            v_claimant_type,
                            v_cla_desc,
                            v_insured_name
                       FROM gin_claim_master_bookings,
                            gin_policies,
                            tqc_clients,
                            tq_gis.gin_insured_property_unds,
                            gin_rgstd_claimants,
                            gin_claimants,
                            gin_classes,
                            gin_sub_classes
                      WHERE cmb_pol_batch_no = pol_batch_no
                        AND reg_cld_code = clnt_code
                        AND ipu_pol_batch_no = pol_batch_no
                        AND cla_code = scl_cla_code
                        AND scl_code = cmb_scl_code
                        AND ipu_sec_scl_code = cmb_scl_code
                        AND reg_cld_code = cld_code(+)
                        AND pol_prp_code = clnt_code
                        AND cmb_claim_no = v_claim_no;

            SELECT   cmb_claim_no, SUM (tot_amt_rsrv) tot_amt_rsrv,
                     SUM (comp_ret_rsrv) comp_ret_rsrv, SUM (fee_amt) fee_amt,
                     SUM (paid_amt) paid_amt
                INTO v_clm_no, v_tot_amt_rsrv,
                     v_comp_ret_rsrv, v_fee_amt,
                     v_paid_amt
                FROM (SELECT   cmb_claim_no,
                               SUM (NVL (clmrev_amt, 0)) tot_amt_rsrv,
                               SUM (NVL (clmrev_comp_retention, 0)
                                   ) comp_ret_rsrv,
                               0 fee_amt, 0 paid_amt
                          FROM gin_claim_master_bookings, gin_claim_revisions
                         WHERE cmb_claim_no = clmrev_cmb_claim_no
                           AND cmb_claim_no = v_claim_no
                      GROUP BY cmb_claim_no
                      UNION
                      SELECT   cmb_claim_no, 0 tot_amt_rsrv, 0 comp_ret_rsrv,
                               SUM (DECODE (ggt_btr_trans_code,
                                            'CF', NVL (cpv_amount, 0),
                                            0
                                           )
                                   ) fee_amt,
                               SUM (NVL (cpv_amount, 0)) paid_amt
                          FROM gin_claim_master_bookings,
                               gin_clm_payment_vouchers,
                               gin_gis_transactions
                         WHERE cmb_claim_no = cpv_cmb_claim_no
                           AND ggt_trans_no = cpv_ggt_trans_no
                           AND cmb_claim_no = v_claim_no
                      GROUP BY cmb_claim_no)
            GROUP BY cmb_claim_no;

            IF v_gcc_code IS NOT NULL
            THEN
               SELECT gcc_court_name, gcc_court_date, gcc_case_no
                 INTO v_courtname, v_courtdate, v_caseno
                 FROM gin_court_cases
                WHERE gcc_cmb_claim_no = v_claim_no AND gcc_code = v_gcc_code;
            END IF;

            lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLAIMNO]', v_clm_no);
            lv_temp_txt := REPLACE (lv_temp_txt, '[SA]', v_sa);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLAIMANTNAME]', v_client);
            lv_temp_txt := REPLACE (lv_temp_txt, '[INSURED]', v_insured_name);
            lv_temp_txt := REPLACE (lv_temp_txt, '[PREMIUM]', v_prem);
            lv_temp_txt := REPLACE (lv_temp_txt, '[DATE]', v_date);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOSSTIME]', v_time);
            lv_temp_txt := REPLACE (lv_temp_txt, '[REGISTRATION]', v_reg);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOCATION]', v_loc);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CASENO]', v_caseno);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CASEDATE]', v_courtdate);
            lv_temp_txt := REPLACE (lv_temp_txt, '[COURT]', v_courtname);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOSSDESC]', v_desc);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOSSTYPE]', v_loss_type);
            lv_temp_txt := REPLACE (lv_temp_txt, '[RISKDESC]', v_item_desc);
            lv_temp_txt :=
                      REPLACE (lv_temp_txt, '[CLAIMANTTYPE]', v_claimant_type);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLASS]', v_cla_desc);
            lv_temp_txt :=
                       REPLACE (lv_temp_txt, '[GROSSRESERVE]', v_tot_amt_rsrv);
            lv_temp_txt :=
                  REPLACE (lv_temp_txt, '[OWNSHARE_RESERVE]', v_comp_ret_rsrv);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLAIMFEE_AMT]', v_fee_amt);
            lv_temp_txt :=
                    REPLACE (lv_temp_txt, '[TOTAL_CLAIM_PAYMENT]', v_paid_amt);
            lv_temp_txt :=
                         REPLACE (lv_temp_txt, '[COIN_SHARE]', v_pol_coin_pct);
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
            WHEN OTHERS
            THEN
               raise_application_error (-20001,
                                        'ERROR: ' || SQLERRM (SQLCODE)
                                       );
         END;
      ELSIF v_app_lvl = 'L'
      THEN
         NULL;
      ELSIF v_app_lvl = 'Q'
      THEN
         IF v_pol_batch_no IS NOT NULL
         THEN
            BEGIN
               SELECT quot_no, quot_tot_property_val,
                      gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                      quot_premium, quot_cover_from, quot_cover_to,
                      quot_date, agn_name, brn_name, clnt_postal_addrs,
                      twn_name, qr_property_id, qr_item_desc, qr_wef,
                      qr_wet, qr_covt_sht_desc, qr_premium, qr_value,
                      quot_cur_symbol
                 INTO v_pol_no, v_sa,
                      v_client,
                      v_prem, vcoverfrom, vcoverto,
                      veffdate, v_agent, v_branch, v_address,
                      v_twn, v_risk_id, v_risk_desc, v_risk_wef,
                      v_risk_wet, v_cover, v_risk_prem, v_risk_value,
                      v_currency
                 FROM gin_quotations,
                      tqc_clients,
                      tqc_agencies,
                      tqc_branches,
                      tqc_towns,
                      tq_gis.gin_quot_risks
                WHERE quot_prp_code = clnt_code
                  AND qr_quot_code = quot_code
                  AND quot_agnt_agent_code = agn_code
                  AND twn_code(+) = clnt_twn_code
                  AND brn_code = quot_brn_code
                  AND quot_code = v_pol_batch_no
                  AND ROWNUM < 2;

               lv_temp_txt := REPLACE (lv_temp_txt, '[QUOTNO]', v_pol_no);
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERFROM]',
                           TO_CHAR (vcoverfrom, 'DD/Mon/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERTO]',
                           TO_CHAR (vcoverto, 'DD/Mon/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[EFFDATE]',
                           TO_CHAR (veffdate, 'DD/Mon/YYYY')
                          );
               lv_temp_txt := REPLACE (lv_temp_txt, '[SA]', v_sa);
               lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
               lv_temp_txt := REPLACE (lv_temp_txt, '[PREMIUM]', v_prem);
               lv_temp_txt := REPLACE (lv_temp_txt, '[CURRENCY]', v_currency);
               lv_temp_txt := REPLACE (lv_temp_txt, '[AGENT]', v_agent);
               lv_temp_txt := REPLACE (lv_temp_txt, '[BRANCH]', v_branch);
               lv_temp_txt := REPLACE (lv_temp_txt, '[ADDRESS]', v_address);
               lv_temp_txt := REPLACE (lv_temp_txt, '[TOWN]', v_twn);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKID]', v_risk_id);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKDESC]', v_risk_desc);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWEF]', v_risk_wef);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWET]', v_risk_wet);
               lv_temp_txt := REPLACE (lv_temp_txt, '[COVERTYPE]', v_cover);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKPREM]', v_risk_prem);
               lv_temp_txt :=
                            REPLACE (lv_temp_txt, '[RISKVALUE]', v_risk_value);
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
               WHEN OTHERS
               THEN
                  raise_application_error (-20001,
                                           'ERROR: ' || SQLERRM (SQLCODE)
                                          );
            END;
         ELSE
            IF v_pol_no IS NOT NULL
            THEN
               lv_temp_txt := REPLACE (lv_temp_txt, '[QUOTNO]', v_pol_no);
            END IF;
         END IF;
      ELSE
         raise_application_error (-20001,
                                     'Application level '
                                  || NVL (v_app_lvl, 'NONE')
                                  || ' not recognised..'
                                 );
      END IF;

      RETURN (lv_temp_txt);
   END;

   FUNCTION convert_to_varchar2 (p_lob_value IN CLOB)
      RETURN VARCHAR2
   IS
      v_lob_length   NUMBER;
      v_varchar      VARCHAR2 (32727);
   BEGIN
      v_lob_length := NVL (DBMS_LOB.getlength (p_lob_value), 0);

      IF v_lob_length <> 0
      THEN
         DBMS_LOB.READ (p_lob_value, v_lob_length, 1, v_varchar);
      ELSE
         v_varchar := NULL;
      END IF;

      RETURN (v_varchar);
   END convert_to_varchar2;

   FUNCTION process_gis_pol_memo_clob (
      v_pol_batch_no   IN   NUMBER,
      v_claim_no       IN   VARCHAR2,
      v_gcc_code       IN   NUMBER,
      v_raw_txt        IN   CLOB,
      v_app_lvl        IN   VARCHAR2
   )
      RETURN VARCHAR2
   IS
      lv_temp_txt           VARCHAR2 (32767);
      v_pol_no              gin_policies.pol_policy_no%TYPE;
      v_clm_no              gin_claim_master_bookings.cmb_claim_no%TYPE;
      v_sa                  NUMBER;
      v_premium             NUMBER;
      v_client              tqc_clients.clnt_name%TYPE;
      v_prem                NUMBER;
      v_date                DATE;
      v_time                DATE;
      v_reg                 VARCHAR2 (100);
      v_loc                 VARCHAR2 (100);
      v_courtname           VARCHAR2 (100);
      v_courtdate           DATE;
      v_caseno              VARCHAR2 (100);
      vcoverfrom            DATE;
      vcoverto              DATE;
      veffdate              DATE;
      vrendate              DATE;
      v_desc                VARCHAR2 (100);
      v_loss_type           VARCHAR2 (100);
      v_item_desc           VARCHAR2 (100);
      v_claimant_type       VARCHAR2 (15);
      v_agent               tqc_agencies.agn_name%TYPE;
      v_int_parties         VARCHAR2 (100);
      v_uw_year             INT;
      v_branch              tqc_branches.brn_name%TYPE;
      v_address             VARCHAR2 (100);
      v_twn                 VARCHAR2 (100);
      v_risk_id             VARCHAR2 (100);
      v_surname             tqc_clients.clnt_name%TYPE;
      v_risk_desc           gin_insured_property_unds.ipu_item_desc%TYPE;
      v_risk_wef            DATE;
      v_risk_wet            DATE;
      v_cover               VARCHAR2 (50);
      v_risk_prem           DECIMAL (20, 2);
      v_risk_value          DECIMAL (20, 2);
      v_currency            VARCHAR2 (5);
      v_agn_code            NUMBER;
      v_agn_name            tqc_agencies.agn_name%TYPE;
      v_agn_add             tqc_agencies.agn_physical_address%TYPE;
      v_client_code         NUMBER;
      v_client_add          VARCHAR2 (100);
      v_insured_name        VARCHAR2 (300);
      v_insured_add         VARCHAR2 (100);
      v_org_name            VARCHAR2 (100);
      v_org_add             VARCHAR2 (100);
      v_ipu_desc            gin_insured_property_unds.ipu_item_desc%TYPE;
      v_amt_words           VARCHAR2 (50);
      v_cur_code            NUMBER;
      v_period              NUMBER;
      v_obligor             gin_bonds.bon_obligor%TYPE;
      v_ob_address          gin_bonds.obligor_addrs%TYPE;
      v_ob_town             gin_bonds.bon_obligor_town%TYPE;
      v_ob_country          gin_bonds.bon_obligor_country%TYPE;
      v_ob_emp              gin_bonds.bon_employer%TYPE;
      v_res_amt             gin_claim_revisions.clmrev_amt%TYPE;
      v_original_estimate   gin_claim_revisions.clmrev_amt%TYPE;
      v_dep_amt             gin_claim_revisions.clmrev_amt%TYPE;
      v_excess_amt          gin_claim_revisions.clmrev_amt%TYPE;
      v_ipu_code            gin_insured_property_unds.ipu_code%TYPE;
      v_clnt_tel            tqc_clients.clnt_tel%TYPE;
      v_peril               gin_claim_perils.clmp_per_pt_sht_desc%TYPE;
      v_excess_rate         NUMBER;
      v_dep_rate            NUMBER;
      v_agn_tel             tqc_agencies.agn_tel1%TYPE;
      v_cla_desc            gin_classes.cla_descn%TYPE;
      v_tot_amt_rsrv        NUMBER;
      v_comp_ret_rsrv       NUMBER;
      v_fee_amt             NUMBER;
      v_paid_amt            NUMBER;
      v_pol_coin_pct        NUMBER;
   BEGIN
      --RAISE_APPLICATION_ERROR(-20001,'ERROR: wonder where is the error');
      lv_temp_txt := DBMS_LOB.SUBSTR (v_raw_txt, 32765, 1);
                                           --CONVERT_TO_VARCHAR2 (v_raw_txt);

      IF v_app_lvl = 'U'
      THEN
         IF v_pol_batch_no IS NOT NULL
         THEN
            BEGIN
               SELECT pol_policy_no, pol_si_diff,
                      gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                      pol_tot_endos_diff_amt, pol_policy_cover_from,
                      pol_policy_cover_to, pol_wef_dt, pol_renewal_dt,
                      agn_name, pol_oth_int_parties, pol_uw_year, brn_name,
                      clnt_postal_addrs, twn_name, ipu_property_id,
                      ipu_item_desc, ipu_wef, ipu_wet, ipu_covt_sht_desc,
                      ipu_endos_diff_amt, ipu_value
                 INTO v_pol_no, v_sa,
                      v_client,
                      v_prem, vcoverfrom,
                      vcoverto, veffdate, vrendate,
                      v_agent, v_int_parties, v_uw_year, v_branch,
                      v_address, v_twn, v_risk_id,
                      v_risk_desc, v_risk_wef, v_risk_wet, v_cover,
                      v_risk_prem, v_risk_value
                 FROM gin_policies,
                      tqc_clients,
                      tqc_agencies,
                      tqc_branches,
                      tqc_towns,
                      tq_gis.gin_insured_property_unds
                WHERE pol_prp_code = clnt_code
                  AND pol_agnt_agent_code = agn_code
                  AND twn_code(+) = clnt_twn_code
                  AND brn_code = pol_brn_code
                  AND ipu_pol_batch_no = pol_batch_no
                  AND pol_batch_no = v_pol_batch_no
                  AND ROWNUM < 2;

               lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
               lv_temp_txt := REPLACE (lv_temp_txt, '[POLNO]', v_pol_no);
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERFROM]',
                           TO_CHAR (vcoverfrom, 'DD/MM/RRRR')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERTO]',
                           TO_CHAR (vcoverto, 'DD/MM/RRRR')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[EFFDATE]',
                           TO_CHAR (veffdate, 'DD/MM/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[RENEWALDATE]',
                           TO_CHAR (vrendate, 'DD/MM/RRRR')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[SA]',
                           TO_CHAR (NVL (v_sa, 0), 'FM999,999,999.00')
                          );
               lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[PREMIUM]',
                           TO_CHAR (NVL (v_prem, 0), 'FM999,999,999.00')
                          );
               lv_temp_txt := REPLACE (lv_temp_txt, '[AGENT]', v_agent);
               lv_temp_txt :=
                          REPLACE (lv_temp_txt, '[INTPARTIES]', v_int_parties);
               lv_temp_txt := REPLACE (lv_temp_txt, '[UWYEAR]', v_uw_year);
               lv_temp_txt := REPLACE (lv_temp_txt, '[BRANCH]', v_branch);
               lv_temp_txt := REPLACE (lv_temp_txt, '[ADDRESS]', v_address);
               lv_temp_txt := REPLACE (lv_temp_txt, '[TOWN]', v_twn);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKID]', v_risk_id);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKDESC]', v_risk_desc);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWEF]', v_risk_wef);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWET]', v_risk_wet);
               lv_temp_txt := REPLACE (lv_temp_txt, '[COVERTYPE]', v_cover);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKPREM]', v_risk_prem);
               lv_temp_txt :=
                            REPLACE (lv_temp_txt, '[RISKVALUE]', v_risk_value);
               --RAISE_ERROR('HERE');
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[WEF]',
                           TO_CHAR (veffdate, 'DD/MM/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[WET]',
                           TO_CHAR (vcoverto, 'DD/MM/YYYY')
                          );
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
               WHEN OTHERS
               THEN
                  raise_application_error (-20001,
                                           'ERROR: ' || SQLERRM (SQLCODE)
                                          );
            END;
         ELSE
            IF v_pol_no IS NOT NULL
            THEN
               lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
            END IF;
         END IF;
      ELSIF v_app_lvl = 'S'
      THEN
         SELECT pol_policy_no, pol_total_sum_insured,
                gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                clnt_name, pol_total_fap, pol_policy_cover_from,
                pol_policy_cover_to, pol_wef_dt, pol_renewal_dt,
                pol_agnt_agent_code, pol_cur_code, pol_oth_int_parties
           INTO v_pol_no, v_sa,
                v_client,
                v_surname, v_prem, vcoverfrom,
                vcoverto, veffdate, vrendate,
                v_agn_code, v_cur_code, v_int_parties
           FROM gin_policies, tqc_clients
          WHERE pol_prp_code = clnt_code AND pol_batch_no = v_pol_batch_no;

         --MESSAGE('[POLICYNO]'||lv_temp_txt);PAUSE;
         lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
         lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
         lv_temp_txt := REPLACE (lv_temp_txt, '[SURNAME]', v_surname);
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[COVERFROM]',
                        TO_CHAR (vcoverfrom, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[COVERTO]',
                        TO_CHAR (vcoverto, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[EFFDATE]',
                        TO_CHAR (veffdate, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[RENEWALDATE]',
                     TO_CHAR (vrendate, 'DD/MM/YYYY')
                    );
         lv_temp_txt := REPLACE (lv_temp_txt, '[INTPARTIES]', v_int_parties);
         --MESSAGE(v_pol_no||lv_temp_txt);PAUSE;
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[SA]',
                     TO_CHAR (NVL (v_sa, 0), 'FM999,999,999.00')
                    );
         -- lv_temp_txt := REPLACE(lv_temp_txt,'[CLIENT]',v_client);
         lv_temp_txt := REPLACE (lv_temp_txt, '[PREMIUM]', NVL (v_prem, 0));
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[AMTINWORDS]',
                     fms_cheques_pkg.num_to_words (NVL (v_sa, 0), v_cur_code)
                    );

         SELECT MONTHS_BETWEEN (vcoverto, vcoverfrom)
           INTO v_period
           FROM DUAL;

         lv_temp_txt := REPLACE (lv_temp_txt, '[PERIOD]', ROUND (v_period, 0));

         SELECT ipu_item_desc
           INTO v_ipu_desc
           FROM tq_gis.gin_insured_property_unds
          WHERE ipu_pol_batch_no = v_pol_batch_no AND ROWNUM = 1;

         lv_temp_txt := REPLACE (lv_temp_txt, '[ITEMDESC]', v_ipu_desc);

         SELECT org_name, org_phy_addrs
           INTO v_org_name, v_org_add
           FROM tqc_organizations
          WHERE org_code = 2;

         lv_temp_txt := REPLACE (lv_temp_txt, '[ORGNAME]', v_org_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[ORGADDR]', v_org_add);

         SELECT agn_name, agn_physical_address
           INTO v_agn_name, v_agn_add
           FROM tqc_agencies
          WHERE agn_code = v_agn_code;

         lv_temp_txt := REPLACE (lv_temp_txt, '[AGNNAME]', v_agn_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[AGNADDR]', v_agn_add);

         BEGIN
            SELECT gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                   clnt_physical_addrs
              INTO v_insured_name,
                   v_insured_add
              FROM tq_gis.gin_policy_insureds, tqc_clients
             WHERE polin_prp_code = clnt_code
               AND polin_pol_batch_no = v_pol_batch_no
               AND ROWNUM = 1;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         lv_temp_txt := REPLACE (lv_temp_txt, '[INSNAME]', v_insured_name);
         lv_temp_txt := REPLACE (lv_temp_txt, '[INSADDR]', v_insured_add);

         BEGIN
            SELECT bon_obligor, obligor_addrs, bon_obligor_town,
                   bon_obligor_country, bon_employer, bon_contract_date
              INTO v_obligor, v_ob_address, v_ob_town,
                   v_ob_country, v_ob_emp, v_date
              FROM gin_bonds, gin_insured_property_unds
             WHERE bon_ipu_code = ipu_code
               AND ipu_pol_batch_no = v_pol_batch_no;
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
         END;

         lv_temp_txt := REPLACE (lv_temp_txt, '[BANK]', v_obligor);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKADD]', v_ob_address);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKTWN]', v_ob_town);
         lv_temp_txt := REPLACE (lv_temp_txt, '[BANKSTATE]', v_ob_country);
         lv_temp_txt := REPLACE (lv_temp_txt, '[EMPLOYER]', v_ob_emp);
         lv_temp_txt :=
            REPLACE (lv_temp_txt,
                     '[CONTRACTDATE]',
                        TO_CHAR (v_date, 'ddth')
                     || ' day of '
                     || TRIM (TO_CHAR (vcoverfrom, 'Month'))
                     || ' '
                     || TO_CHAR (vcoverfrom, 'RRRR')
                    );
      ELSIF v_app_lvl = 'C'
      THEN
         --RAISE_APPLICATION_ERROR(-20001,'ERROR: wonder where is the error');
         BEGIN
            SELECT DISTINCT pol_coinsure_pct, pol_policy_no, cmb_claim_no,
                            pol_total_sum_insured,
                            DECODE (reg_third_party,
                                    'S', gis_utilities.clnt_name (clnt_name,
                                                                  clnt_surname
                                                                 ),
                                    cld_other_names || ' ' || cld_surname
                                   ) claimant_name,
                            pol_tot_endos_diff_amt, cmb_claim_date,
                            cmb_loss_date_time, cmb_ipu_property_id risk_id,
                            cmb_location, cmb_loss_desc,
                            NVL (cmb_other_cover_details, 'Repairable loss')
                                                                    loss_type,
                            ipu_item_desc risk_description,
                            DECODE (reg_third_party,
                                    'S', 'SELF',
                                    'THIRD PARTY'
                                   ) claimant_type,
                            cla_descn,
                            gis_utilities.clnt_name (clnt_name, clnt_surname)
                                                                      insured
                       INTO v_pol_coin_pct, v_pol_no, v_clm_no,
                            v_sa,
                            v_client,
                            v_prem, v_date,
                            v_time, v_reg,
                            v_loc, v_desc,
                            v_loss_type,
                            v_item_desc,
                            v_claimant_type,
                            v_cla_desc,
                            v_insured_name
                       FROM gin_claim_master_bookings,
                            gin_policies,
                            tqc_clients,
                            tq_gis.gin_insured_property_unds,
                            gin_rgstd_claimants,
                            gin_claimants,
                            gin_classes,
                            gin_sub_classes
                      WHERE cmb_pol_batch_no = pol_batch_no
                        AND reg_cld_code = clnt_code
                        AND ipu_pol_batch_no = pol_batch_no
                        AND cla_code = scl_cla_code
                        AND scl_code = cmb_scl_code
                        AND ipu_sec_scl_code = cmb_scl_code
                        AND reg_cld_code = cld_code(+)
                        AND pol_prp_code = clnt_code
                        AND cmb_claim_no = v_claim_no;

            SELECT   cmb_claim_no, SUM (tot_amt_rsrv) tot_amt_rsrv,
                     SUM (comp_ret_rsrv) comp_ret_rsrv, SUM (fee_amt) fee_amt,
                     SUM (paid_amt) paid_amt
                INTO v_clm_no, v_tot_amt_rsrv,
                     v_comp_ret_rsrv, v_fee_amt,
                     v_paid_amt
                FROM (SELECT   cmb_claim_no,
                               SUM (NVL (clmrev_amt, 0)) tot_amt_rsrv,
                               SUM (NVL (clmrev_comp_retention, 0)
                                   ) comp_ret_rsrv,
                               0 fee_amt, 0 paid_amt
                          FROM gin_claim_master_bookings, gin_claim_revisions
                         WHERE cmb_claim_no = clmrev_cmb_claim_no
                           AND cmb_claim_no = v_claim_no
                      GROUP BY cmb_claim_no
                      UNION
                      SELECT   cmb_claim_no, 0 tot_amt_rsrv, 0 comp_ret_rsrv,
                               SUM (DECODE (ggt_btr_trans_code,
                                            'CF', NVL (cpv_amount, 0),
                                            0
                                           )
                                   ) fee_amt,
                               SUM (NVL (cpv_amount, 0)) paid_amt
                          FROM gin_claim_master_bookings,
                               gin_clm_payment_vouchers,
                               gin_gis_transactions
                         WHERE cmb_claim_no = cpv_cmb_claim_no
                           AND ggt_trans_no = cpv_ggt_trans_no
                           AND cmb_claim_no = v_claim_no
                      GROUP BY cmb_claim_no)
            GROUP BY cmb_claim_no;

            IF v_gcc_code IS NOT NULL
            THEN
               SELECT gcc_court_name, gcc_court_date, gcc_case_no
                 INTO v_courtname, v_courtdate, v_caseno
                 FROM gin_court_cases
                WHERE gcc_cmb_claim_no = v_claim_no AND gcc_code = v_gcc_code;
            END IF;

            lv_temp_txt := REPLACE (lv_temp_txt, '[POLICYNO]', v_pol_no);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLAIMNO]', v_clm_no);
            lv_temp_txt := REPLACE (lv_temp_txt, '[SA]', v_sa);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLAIMANTNAME]', v_client);
            lv_temp_txt := REPLACE (lv_temp_txt, '[INSURED]', v_insured_name);
            lv_temp_txt := REPLACE (lv_temp_txt, '[PREMIUM]', v_prem);
            lv_temp_txt := REPLACE (lv_temp_txt, '[DATE]', v_date);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOSSTIME]', v_time);
            lv_temp_txt := REPLACE (lv_temp_txt, '[REGISTRATION]', v_reg);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOCATION]', v_loc);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CASENO]', v_caseno);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CASEDATE]', v_courtdate);
            lv_temp_txt := REPLACE (lv_temp_txt, '[COURT]', v_courtname);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOSSDESC]', v_desc);
            lv_temp_txt := REPLACE (lv_temp_txt, '[LOSSTYPE]', v_loss_type);
            lv_temp_txt := REPLACE (lv_temp_txt, '[RISKDESC]', v_item_desc);
            lv_temp_txt :=
                      REPLACE (lv_temp_txt, '[CLAIMANTTYPE]', v_claimant_type);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLASS]', v_cla_desc);
            lv_temp_txt :=
                       REPLACE (lv_temp_txt, '[GROSSRESERVE]', v_tot_amt_rsrv);
            lv_temp_txt :=
                  REPLACE (lv_temp_txt, '[OWNSHARE_RESERVE]', v_comp_ret_rsrv);
            lv_temp_txt := REPLACE (lv_temp_txt, '[CLAIMFEE_AMT]', v_fee_amt);
            lv_temp_txt :=
                    REPLACE (lv_temp_txt, '[TOTAL_CLAIM_PAYMENT]', v_paid_amt);
            lv_temp_txt :=
                         REPLACE (lv_temp_txt, '[COIN_SHARE]', v_pol_coin_pct);
         EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
               NULL;
            WHEN OTHERS
            THEN
               raise_application_error (-20001,
                                        'ERROR: ' || SQLERRM (SQLCODE)
                                       );
         END;
      ELSIF v_app_lvl = 'L'
      THEN
         NULL;
      ELSIF v_app_lvl = 'Q'
      THEN
         IF v_pol_batch_no IS NOT NULL
         THEN
            BEGIN
               SELECT quot_no, quot_tot_property_val,
                      gis_utilities.clnt_name (clnt_name, clnt_other_names)
                                                                    clnt_name,
                      quot_premium, quot_cover_from, quot_cover_to,
                      quot_date, agn_name, brn_name, clnt_postal_addrs,
                      twn_name, qr_property_id, qr_item_desc, qr_wef,
                      qr_wet, qr_covt_sht_desc, qr_premium, qr_value,
                      quot_cur_symbol
                 INTO v_pol_no, v_sa,
                      v_client,
                      v_prem, vcoverfrom, vcoverto,
                      veffdate, v_agent, v_branch, v_address,
                      v_twn, v_risk_id, v_risk_desc, v_risk_wef,
                      v_risk_wet, v_cover, v_risk_prem, v_risk_value,
                      v_currency
                 FROM gin_quotations,
                      tqc_clients,
                      tqc_agencies,
                      tqc_branches,
                      tqc_towns,
                      tq_gis.gin_quot_risks
                WHERE quot_prp_code = clnt_code
                  AND qr_quot_code = quot_code
                  AND quot_agnt_agent_code = agn_code
                  AND twn_code(+) = clnt_twn_code
                  AND brn_code = quot_brn_code
                  AND quot_code = v_pol_batch_no
                  AND ROWNUM < 2;

               lv_temp_txt := REPLACE (lv_temp_txt, '[QUOTNO]', v_pol_no);
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERFROM]',
                           TO_CHAR (vcoverfrom, 'DD/Mon/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[COVERTO]',
                           TO_CHAR (vcoverto, 'DD/Mon/YYYY')
                          );
               lv_temp_txt :=
                  REPLACE (lv_temp_txt,
                           '[EFFDATE]',
                           TO_CHAR (veffdate, 'DD/Mon/YYYY')
                          );
               lv_temp_txt := REPLACE (lv_temp_txt, '[SA]', v_sa);
               lv_temp_txt := REPLACE (lv_temp_txt, '[CLIENT]', v_client);
               lv_temp_txt := REPLACE (lv_temp_txt, '[PREMIUM]', v_prem);
               lv_temp_txt := REPLACE (lv_temp_txt, '[CURRENCY]', v_currency);
               lv_temp_txt := REPLACE (lv_temp_txt, '[AGENT]', v_agent);
               lv_temp_txt := REPLACE (lv_temp_txt, '[BRANCH]', v_branch);
               lv_temp_txt := REPLACE (lv_temp_txt, '[ADDRESS]', v_address);
               lv_temp_txt := REPLACE (lv_temp_txt, '[TOWN]', v_twn);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKID]', v_risk_id);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKDESC]', v_risk_desc);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWEF]', v_risk_wef);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKWET]', v_risk_wet);
               lv_temp_txt := REPLACE (lv_temp_txt, '[COVERTYPE]', v_cover);
               lv_temp_txt := REPLACE (lv_temp_txt, '[RISKPREM]', v_risk_prem);
               lv_temp_txt :=
                            REPLACE (lv_temp_txt, '[RISKVALUE]', v_risk_value);
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
               WHEN OTHERS
               THEN
                  raise_application_error (-20001,
                                           'ERROR: ' || SQLERRM (SQLCODE)
                                          );
            END;
         ELSE
            IF v_pol_no IS NOT NULL
            THEN
               lv_temp_txt := REPLACE (lv_temp_txt, '[QUOTNO]', v_pol_no);
            END IF;
         END IF;
      ELSE
         raise_application_error (-20001,
                                     'Application level '
                                  || NVL (v_app_lvl, 'NONE')
                                  || ' not recognised..'
                                 );
      END IF;

      RETURN (lv_temp_txt);
   END;
END; 
/