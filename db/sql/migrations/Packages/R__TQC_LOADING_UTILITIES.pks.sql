/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_LOADING_UTILITIES  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_loading_utilities
AS
   PROCEDURE tqc_clntagnt_loading_prc (
      v_add_edit               IN   VARCHAR2,
      v_tqc_clnt_loading_tbl        tqc_clnt_loading_tbl,
      v_binder                      VARCHAR2
   );

   PROCEDURE tqc_clnt_loading_prc (
      v_add_edit               IN   VARCHAR2,
      v_tqc_clnt_loading_tbl        tqc_clnt_loading_tbl,
      v_binder                      VARCHAR2
   );

   PROCEDURE "RAISE_ERROR" (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL);

   PROCEDURE load_clients_prc (
      v_cln_code   IN   NUMBER,
      v_user       IN   VARCHAR2,
      v_gis        IN   VARCHAR2,
      v_lms        IN   VARCHAR2
   );

   PROCEDURE saveclientloadingdetails (
      v_add_edit                  IN   VARCHAR2,
      v_cln_code                  IN   VARCHAR2,
      v_cln_clnt_sht_desc         IN   VARCHAR2,
      v_cln_clnt_twn_code         IN   VARCHAR2,
      v_cln_clnt_name             IN   VARCHAR2,
      v_cln_clnt_other_names      IN   VARCHAR2,
      v_cln_clnt_postal_addrs     IN   VARCHAR2,
      v_cln_clnt_physical_addrs   IN   VARCHAR2,
      v_cln_clnt_tel              IN   VARCHAR2,
      v_cln_clnt_tel2             IN   VARCHAR2,
      v_cln_clnt_fax              IN   VARCHAR2,
      v_cln_clnt_cntct_email_1    IN   VARCHAR2,
      v_cln_clnt_id_reg_no        IN   VARCHAR2,
      v_cln_clnt_dob              IN   DATE
   );

   PROCEDURE tqc_agnt_loading_prc (
      v_add_edit               IN   VARCHAR2,
      v_tqc_agnt_loading_tbl        tqc_agnt_loading_tbl,
      v_binder                      VARCHAR2
   );

--  PROCEDURE saveAgentDetails (
--   v_add_edit                      IN       VARCHAR2,
--   v_agnl_code                     IN       VARCHAR2,
--   v_agnl_agn_act_code             IN       VARCHAR2,
--   v_agnl_account_type             IN       VARCHAR2,
--   v_agnl_agn_acc_name             IN       VARCHAR2,
--   v_agnl_agn_physical_address     IN       VARCHAR2,
--   v_agnl_agn_postal_address       IN       VARCHAR2,
--   v_agnl_agn_twn_name             IN       VARCHAR2,
--   v_agnl_agn_reg_code             IN       VARCHAR2,
--   v_agnl_agn_contact_person       IN       VARCHAR2,
--   v_agnl_agn_tel1                 IN       VARCHAR2,
--   v_agnl_agn_fax                  IN       VARCHAR2,
--   v_agnl_agn_email_address        IN       VARCHAR2,
--   v_agnl_agn_date_created         IN       VARCHAR2,
--   v_agnl_agn_check_date           IN       VARCHAR2,
--   v_agnl_branch_name       IN       VARCHAR2
--
--   );
   PROCEDURE saveagentdetails (
      v_add_edit                    IN   VARCHAR2,
      v_agnl_code                   IN   VARCHAR2,
      v_agnl_agn_act_code           IN   VARCHAR2,
      v_agnl_account_type           IN   VARCHAR2,
      v_agnl_agn_acc_name           IN   VARCHAR2,
      v_agnl_agn_physical_address   IN   VARCHAR2,
      v_agnl_agn_postal_address     IN   VARCHAR2,
      v_agnl_agn_twn_name           IN   VARCHAR2,
      v_agnl_agn_reg_code           IN   VARCHAR2,
      v_agnl_agn_contact_person     IN   VARCHAR2,
      v_agnl_agn_tel1               IN   VARCHAR2,
      v_agnl_agn_fax                IN   VARCHAR2,
      v_agnl_agn_email_address      IN   VARCHAR2,
      v_agnl_agn_date_created       IN   VARCHAR2,
      v_agnl_agn_check_date         IN   VARCHAR2,
      v_agnl_branch_name            IN   VARCHAR2
   );

   PROCEDURE load_agents_prc (
      v_agnl_code   IN   NUMBER,
      v_user        IN   VARCHAR2,
      v_gis         IN   VARCHAR2,
      v_lms         IN   VARCHAR2
   );
END tqc_loading_utilities; 
/