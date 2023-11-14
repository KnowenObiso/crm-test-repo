--
-- TQC_MEMO_WEB_CURSOR  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_memo_web_cursor
AS
   TYPE app_level IS RECORD (
      level_id     VARCHAR2 (2),
      level_desc   VARCHAR2 (20)
   );

   TYPE clientsmemo IS RECORD (
      usr_personel_rank   tqc_users.usr_personel_rank%TYPE
   );

   TYPE vendorsmemo IS RECORD (
      comem_file_no    tqc_company_memos.comem_file_no%TYPE,
      comem_code       tqc_company_memos.comem_code%TYPE,
      comem_desc       tqc_company_memos.comem_desc%TYPE,
      comem_date       tqc_company_memos.comem_date%TYPE,
      comem_subject    tqc_company_memos.comem_subject%TYPE,
      comem_appl_lvl   tqc_company_memos.comem_appl_lvl%TYPE,
      comem_done_by    tqc_company_memos.comem_done_by%TYPE,
      cor_name         tqc_service_providers.spr_name%TYPE,
      comem_prp_code   tqc_company_memos.comem_prp_code%TYPE
   );

   TYPE internalmemo IS RECORD (
      comem_file_no    tqc_company_memos.comem_file_no%TYPE,
      comem_code       tqc_company_memos.comem_code%TYPE,
      comem_date       tqc_company_memos.comem_date%TYPE,
      comem_subject    tqc_company_memos.comem_subject%TYPE,
      comem_done_by    tqc_company_memos.comem_done_by%TYPE,
      comem_appl_lvl   tqc_company_memos.comem_appl_lvl%TYPE
   );

   TYPE quotationmemo IS RECORD (
      pol_policy_no         tq_gis.gin_quotations.quot_no%TYPE,
      pol_agnt_sht_desc     tq_gis.gin_quotations.quot_agnt_sht_desc%TYPE,
      quot_code             tq_gis.gin_quotations.quot_code%TYPE,
      pol_ren_endos_no      NUMBER,
      batc_no               tq_gis.gin_quotations.quot_code%TYPE,
      insured               VARCHAR2 (200),
      quot_brn_code         tq_gis.gin_quotations.quot_brn_code%TYPE,
      address               VARCHAR2 (200),
      code_town             VARCHAR2 (200),
      prp_code              tqc_clients.clnt_code%TYPE,
      pol_agnt_agent_code   tq_gis.gin_quotations.quot_agnt_agent_code%TYPE
   );

   TYPE policymemo IS RECORD (
      pol_policy_no         gin_policies.pol_policy_no%TYPE,
      pol_agnt_sht_desc     gin_policies.pol_agnt_sht_desc%TYPE,
      pol_batch_no          gin_policies.pol_batch_no%TYPE,
      pol_ren_endos_no      gin_policies.pol_ren_endos_no%TYPE,
      batch_no              gin_policies.pol_batch_no%TYPE,
      insured               VARCHAR2 (200),
      pol_brn_code          gin_policies.pol_brn_code%TYPE,
      address               VARCHAR2 (100),
      code_town             VARCHAR2 (100),
      prp_code              tqc_clients.clnt_code%TYPE,
      pol_agnt_agent_code   gin_policies.pol_agnt_agent_code%TYPE
   );

   TYPE claimsmemo IS RECORD (
      cmb_claim_no          gin_claim_master_bookings.cmb_claim_no%TYPE,
      pol_agnt_sht_desc     gin_policies.pol_agnt_sht_desc%TYPE,
      cmb_pol_policy_no     gin_claim_master_bookings.cmb_pol_policy_no%TYPE,
      cmb_pol_batch_no      gin_claim_master_bookings.cmb_pol_batch_no%TYPE,
      insured               VARCHAR2 (200),
      pol_brn_code          gin_policies.pol_brn_code%TYPE,
      pol_prp_code          gin_policies.pol_prp_code%TYPE,
      address               VARCHAR2 (100),
      code_town             VARCHAR2 (100),
      pol_agnt_agent_code   gin_policies.pol_agnt_agent_code%TYPE,
      prp_code              tqc_clients.clnt_code%TYPE
   );

   TYPE casesmemo IS RECORD (
      gcc_code         tq_gis.gin_court_cases.gcc_code%TYPE,
      gcc_case_no      tq_gis.gin_court_cases.gcc_case_no%TYPE,
      gcc_court_name   tq_gis.gin_court_cases.gcc_court_name%TYPE
   );

   TYPE memotype IS RECORD (
      mtyp_memo_type   tqc_memo_types.mtyp_memo_type%TYPE,
      mtyp_code        tqc_memo_types.mtyp_code%TYPE
   );

   TYPE agentscclist IS RECORD (
      agn_sht_desc         tqc_agencies.agn_sht_desc%TYPE,
      agnt_agent_name      tqc_agencies.agn_name%TYPE,
      agn_postal_address   tqc_agencies.agn_postal_address%TYPE,
      code_town            VARCHAR2 (100)
   );

   TYPE claimantscclist IS RECORD (
      cld_code      NUMBER,
      cld_id_no     VARCHAR2 (50),
      client        VARCHAR2 (200),
      cld_address   VARCHAR2 (100),
      code_town     VARCHAR2 (100)
   );

   TYPE recipientlist IS RECORD (
      NAME          VARCHAR2 (200),
      description   VARCHAR2 (100),
      address       VARCHAR2 (100),
      code_town     VARCHAR2 (100)
   );

   TYPE memobranches IS RECORD (
      brn_code   tqc_branches.brn_code%TYPE,
      brn_name   tqc_branches.brn_name%TYPE
   );

   TYPE memopolicies IS RECORD (
      pol_policy_no         VARCHAR2 (30),
      pol_agnt_sht_desc     VARCHAR2 (20),
      pol_batch_no          VARCHAR2 (20),
      pol_ren_endos_no      NUMBER,
      batch_no              NUMBER,
      insured               VARCHAR2 (200),
      pol_brn_code          NUMBER,
      address               VARCHAR2 (200),
      code_town             VARCHAR2 (200),
      prp_code              NUMBER,
      pol_agnt_agent_code   NUMBER
   );

   TYPE memotypes IS RECORD (
      mtyp_memo_type   VARCHAR2 (30),
      mtyp_code        VARCHAR2 (30)
   );

   TYPE cclist IS RECORD (
      cc_code         tqc_company_memo_cc.cc_code%TYPE,
      cc_comem_code   tqc_company_memo_cc.cc_comem_code%TYPE,
      cc_name         tqc_company_memo_cc.cc_name%TYPE,
      cc_address      tqc_company_memo_cc.cc_address%TYPE,
      cc_remarks      tqc_company_memo_cc.cc_remarks%TYPE,
      cc_type         tqc_company_memo_cc.cc_type%TYPE
   );

  TYPE memo_search_rec IS RECORD (
      comem_code               NUMBER,
      comem_sys_code           NUMBER,
      comem_desc               VARCHAR2 (100),
      comem_date               DATE,
      comem_cmb_claim_no       VARCHAR2 (100),
      comem_subject            VARCHAR2 (100),
      comem_address            VARCHAR2 (100),
      comem_pol_policy_no      VARCHAR2 (100),
      comem_appl_lvl           VARCHAR2 (100),
      comem_done_by            VARCHAR2 (100),
      comem_authorized         VARCHAR2 (100),
      comem_your_ref           VARCHAR2 (30),
      comem_addressee          VARCHAR2 (30),
      comem_signatory          VARCHAR2 (30),
      comem_mtyp_code          NUMBER,
      comem_pol_ren_endos_no   VARCHAR2 (20),
      comem_brn_code           NUMBER,
      comem_prepared_by        VARCHAR2 (100),
      comem_corr_code          NUMBER,
      comem_ref                VARCHAR2 (100),
      comem_pol_batch_no       NUMBER,
      comem_authorized_date    DATE,
      comem_file_no            VARCHAR2 (20),
      comem_authorized_by      VARCHAR2 (100),
      comem_sign_rank          VARCHAR2 (15),
      comem_insured            VARCHAR2 (100),
      comem_rpt_type           VARCHAR2 (4),
      comem_status             VARCHAR2 (20),
      comem_codetown           VARCHAR2 (20),
      comem_pro_desc           VARCHAR2 (100),
      comem_court_case_no      VARCHAR2 (20),
      comem_gcc_code           NUMBER,
      comem_quot_code          NUMBER,
      comem_quot_no            VARCHAR2 (20),
      brn_name                 VARCHAR2 (30),
      comem_addressee_type     NUMBER,
      memh_memo_subject        tqc_memo_header.memh_memo_subject%TYPE,
      memdtls_detail           tqc_memo_header_dtls.memdtls_detail%TYPE,
      memh_code                NUMBER,
      memdtls_code             NUMBER,
      COMEM_ISSUED      tqc_company_memos.COMEM_ISSUED%TYPE
   );

   TYPE memo_search_ref IS REF CURSOR
      RETURN memo_search_rec;

   TYPE memo IS RECORD (
      comem_code               NUMBER,
      comem_sys_code           NUMBER,
      comem_desc               VARCHAR2 (100),
      comem_date               DATE,
      comem_cmb_claim_no       VARCHAR2 (100),
      comem_subject            VARCHAR2 (100),
      comem_address            VARCHAR2 (100),
      comem_pol_policy_no      VARCHAR2 (100),
      comem_appl_lvl           VARCHAR2 (100),
      comem_done_by            VARCHAR2 (100),
      comem_authorized         VARCHAR2 (100),
      comem_your_ref           VARCHAR2 (30),
      comem_addressee          VARCHAR2 (30),
      comem_signatory          VARCHAR2 (30),
      comem_mtyp_code          NUMBER,
      comem_pol_ren_endos_no   VARCHAR2 (20),
      comem_brn_code           NUMBER,
      comem_prepared_by        VARCHAR2 (100),
      comem_corr_code          NUMBER,
      comem_ref                VARCHAR2 (100),
      comem_pol_batch_no       NUMBER,
      comem_authorized_date    DATE,
      comem_file_no            VARCHAR2 (20),
      comem_authorized_by      VARCHAR2 (100),
      comem_sign_rank          VARCHAR2 (15),
      comem_insured            VARCHAR2 (100),
      comem_rpt_type           VARCHAR2 (4),
      comem_status             VARCHAR2 (20),
      comem_codetown           VARCHAR2 (20),
      comem_pro_desc           VARCHAR2 (100),
      comem_court_case_no      VARCHAR2 (20),
      comem_gcc_code           NUMBER,
      comem_quot_code          NUMBER,
      comem_quot_no            VARCHAR2 (20),
      brn_name                 VARCHAR2 (30),
      scl_code                 gin_sub_classes.scl_code%TYPE,
      scl_desc                 gin_sub_classes.scl_desc%TYPE
   );

   TYPE memoheaderdtls IS RECORD (
      memdtls_memh_code   tqc_memo_header_dtls.memdtls_memh_code%TYPE,
      memdtls_detail      tqc_memo_header_dtls.memdtls_detail%TYPE,
      memdtls_code        tqc_memo_header_dtls.memdtls_code%TYPE
   );

   TYPE memoheaderdtlsrec IS REF CURSOR
      RETURN memoheaderdtls;

   TYPE memorec IS REF CURSOR
      RETURN memo;

   TYPE cclistrec IS REF CURSOR
      RETURN cclist;

   TYPE memotypesrec IS REF CURSOR
      RETURN memotypes;

   TYPE memopoliciesrec IS REF CURSOR
      RETURN memopolicies;

   TYPE memobranchesrec IS REF CURSOR
      RETURN memobranches;

   TYPE recipientlistrec IS REF CURSOR
      RETURN recipientlist;

   TYPE claimantscclistrec IS REF CURSOR
      RETURN claimantscclist;

   TYPE agentscclistrec IS REF CURSOR
      RETURN agentscclist;

   TYPE memotyperec IS REF CURSOR
      RETURN memotype;

   TYPE casesmemorec IS REF CURSOR
      RETURN casesmemo;

   TYPE claimsmemorec IS REF CURSOR
      RETURN claimsmemo;

   TYPE policymemorec IS REF CURSOR
      RETURN policymemo;

   TYPE quotationmemorec IS REF CURSOR
      RETURN quotationmemo;

   TYPE internalmemorec IS REF CURSOR
      RETURN internalmemo;

   TYPE vendorsmemorec IS REF CURSOR
      RETURN vendorsmemo;

   TYPE clientsmemorec IS REF CURSOR
      RETURN clientsmemo;

   TYPE app_level_rec IS REF CURSOR
      RETURN app_level;

   PROCEDURE getapplevel (v_app_level OUT app_level_rec);

   PROCEDURE getclientsmemo (v_clients_memo OUT clientsmemorec);

   PROCEDURE getvendorsmemo (
      v_vendors_memo   OUT   vendorsmemorec,
      v_appl_lvl             VARCHAR2,
      v_trans_type           VARCHAR2
   );

   PROCEDURE getinternalmemo (
      v_internal_memo   OUT   internalmemorec,
      v_appl_lvl              VARCHAR2,
      v_trans_type            VARCHAR2
   );

   PROCEDURE getquotationmemo (v_quotation_memo OUT quotationmemorec);

   PROCEDURE getpolicymemo (v_policy_memo OUT policymemorec);

   PROCEDURE getclaimsmemo (v_claim_memo OUT claimsmemorec);

   PROCEDURE getcasesmemo (
      v_cases_memo     OUT   casesmemorec,
      v_claim_number         VARCHAR2
   );

   PROCEDURE getmemotype (v_memo_type OUT memotyperec, v_appl_lvl VARCHAR2);

   PROCEDURE getagentscclist (
      v_agents_rec   OUT   agentscclistrec,
      v_agn_code           NUMBER
   );

   PROCEDURE getclaimantscclist (
      v_claimants_rec   OUT   claimantscclistrec,
      v_claim_no              VARCHAR2
   );

   PROCEDURE getrecipients (
      v_recipients      OUT   recipientlistrec,
      rec_type                VARCHAR2,
      claim_no                VARCHAR2 DEFAULT NULL,
      agent_code              NUMBER DEFAULT NULL,
      v_prp_code              NUMBER DEFAULT NULL,
      v_batch_no              NUMBER DEFAULT NULL,
      v_policy_number         VARCHAR2 DEFAULT NULL
   );

   PROCEDURE getmemobranches (v_branches OUT memobranchesrec);

   PROCEDURE getmemopolicies (v_policies OUT memopoliciesrec);

   PROCEDURE getmemotypes (v_memo_type OUT memotypesrec, v_appl_level VARCHAR2);

   PROCEDURE getcclist (v_cc_list OUT cclistrec, v_cc_code NUMBER);

   PROCEDURE getmemos (
      v_memo_rec   OUT   memorec,
      v_auth             VARCHAR2,
      v_mem_code         NUMBER DEFAULT NULL,
      v_appl_lvl         VARCHAR2
   );

   PROCEDURE getmemoheaderdtls (
      v_mheader_dtls   OUT   memoheaderdtlsrec,
      v_memo_code            NUMBER
   );

   PROCEDURE getmemotypes (
      v_memo_type       OUT      memotypesrec,
      v_subclass_code            NUMBER,
      v_appl_level               VARCHAR2,
      v_sys_code        IN       NUMBER
   );

   PROCEDURE getinsuredcclist (
      v_claimants_rec   OUT   claimantscclistrec,
      v_pol_no                VARCHAR2,
      v_claim_no              VARCHAR2
   );

   TYPE letterandmemos IS RECORD (
      comem_file_no        tqc_company_memos.comem_file_no%TYPE,
      comem_code           tqc_company_memos.comem_code%TYPE,
      comem_desc           tqc_company_memos.comem_desc%TYPE,
      comem_date           tqc_company_memos.comem_date%TYPE,
      clientname           VARCHAR2 (100),
      comem_subject        tqc_company_memos.comem_subject%TYPE,
      comem_done_by        tqc_company_memos.comem_done_by%TYPE,
      comem_appl_lvl       tqc_company_memos.comem_appl_lvl%TYPE,
      clnt_sht_desc        tqc_clients.clnt_sht_desc%TYPE,
      prp_sht_desc         VARCHAR2 (100),
      comem_prp_code       NUMBER,
      comem_cmb_claim_no   tqc_company_memos.comem_cmb_claim_no%TYPE
   );

   TYPE letterandmemosrec IS REF CURSOR
      RETURN letterandmemos;

   PROCEDURE getmemosandletters (
      v_letterandmemos   OUT      letterandmemosrec,
      v_appl_level                VARCHAR2,
      v_trans_type       IN       VARCHAR2,
      v_cmb_no           IN       VARCHAR2
   );

   TYPE memovals IS RECORD (
      memo_code      NUMBER,
      memo_subject   VARCHAR2 (4000)
   );

   TYPE memovalsrec IS REF CURSOR
      RETURN memovals;

   PROCEDURE find_memos (v_memo_type OUT memovalsrec, v_mytp_code NUMBER);

   PROCEDURE find_memo_details (v_cursor OUT memovalsrec, v_memo_code IN NUMBER);

   TYPE addressee_rec IS RECORD (
      addr_name        VARCHAR2 (200),
      addr_sht_desc    VARCHAR2 (35),
      addr_address     VARCHAR2 (200),
      addr_code_town   VARCHAR2 (150)
   );

   TYPE addressee_ref IS REF CURSOR
      RETURN addressee_rec;

   PROCEDURE getaddresselist (
      v_refcur           OUT      addressee_ref,
      v_addressee_type   IN       VARCHAR2,
      v_appl_lvl         IN       VARCHAR2,
      v_pol_batch_no     IN       NUMBER,
      v_claim_no         IN       VARCHAR2,
      v_agnt_code        IN       NUMBER,
      v_clnt_code        IN       NUMBER,
      v_quot_code        IN       NUMBER
   );

   /*TYPE memos_rec IS RECORD(
   COMEM_FILE_NO        TQC_COMPANY_MEMOS.COMEM_FILE_NO%TYPE,
   COMEM_CODE           TQC_COMPANY_MEMOS.COMEM_CODE%TYPE,
   COMEM_DESC           TQC_COMPANY_MEMOS.COMEM_DESC%TYPE,
   COMEM_DATE           TQC_COMPANY_MEMOS.COMEM_DATE%TYPE,
   COMEM_SUBJECT        TQC_COMPANY_MEMOS.COMEM_SUBJECT%TYPE,
   COMEM_DONE_BY        TQC_COMPANY_MEMOS.COMEM_DONE_BY%TYPE,
   COMEM_APPL_LVL       TQC_COMPANY_MEMOS.COMEM_APPL_LVL%TYPE,
   COMEM_PRP_CODE       TQC_COMPANY_MEMOS.COMEM_PRP_CODE%TYPE

   );*/
   TYPE memo_ref IS REF CURSOR;

   --RETURN memos_rec;
   PROCEDURE find_quotations_memo (v_cursor OUT memo_ref, v_quot_code IN NUMBER);

   PROCEDURE find_uw_memos (v_cursor OUT memo_ref, v_code IN NUMBER);

   PROCEDURE find_claim_memos (v_cursor OUT memo_ref, v_claim_no IN VARCHAR2);

   PROCEDURE search_memos (v_memo_rec OUT memo_search_ref, v_mem_code NUMBER);

   TYPE memo_details_rec IS RECORD (
      pol_prp_code   NUMBER,
      pol_agn_code   NUMBER
   );

   TYPE memo_details_ref IS REF CURSOR
      RETURN memo_details_rec;

   PROCEDURE findmemodetails (
      v_code     IN       VARCHAR2,
      v_level    IN       VARCHAR2,
      v_cursor   OUT      memo_details_ref
   );

   FUNCTION process_gis_pol_memo (
      v_pol_batch_no   IN   NUMBER,
      v_claim_no       IN   VARCHAR2,
      v_gcc_code       IN   NUMBER,
      v_raw_txt        IN   VARCHAR2,
      v_app_lvl        IN   VARCHAR2
   )
      RETURN VARCHAR2;

   FUNCTION process_gis_pol_memo_clob (
      v_pol_batch_no   IN   NUMBER,
      v_claim_no       IN   VARCHAR2,
      v_gcc_code       IN   NUMBER,
      v_raw_txt        IN   CLOB,
      v_app_lvl        IN   VARCHAR2
   )
      RETURN VARCHAR2;
END tqc_memo_web_cursor; 
/