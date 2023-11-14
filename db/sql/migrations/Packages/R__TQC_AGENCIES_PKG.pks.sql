--
-- TQC_AGENCIES_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_agencies_pkg
AS
/******************************************************************************
   NAME:       TQC_AGENCIES_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        3/17/2010             1. Created this package.
******************************************************************************/
   PROCEDURE tqc_agencies_prc (
      v_add_edit       IN       VARCHAR2,
      v_agencies_tab   IN       tqc_agencies_tab,
      v_user           IN       VARCHAR2,
      v_err            OUT      VARCHAR2
   );

   PROCEDURE tqc_agency_directors_prc (
      v_add_edit               IN       VARCHAR2,
      v_agency_directors_tab   IN       tqc_agency_directors_tab,
      v_err                    OUT      VARCHAR2
   );

   PROCEDURE tqc_agency_registration_prc (
      v_add_edit                  IN       VARCHAR2,
      v_agency_registration_tab   IN       tqc_agency_registration_tab,
      v_err                       OUT      VARCHAR2
   );

   PROCEDURE tqc_agency_referees_prc (
      v_add_edit              IN       VARCHAR2,
      v_agency_referees_tab   IN       tqc_agency_referees_tab,
      v_err                   OUT      VARCHAR2
   );

   PROCEDURE grant_agent_system (
      v_add_edit   IN   VARCHAR2,
      v_agn_code   IN   NUMBER,
      v_sys_code   IN   NUMBER,
      v_wef        IN   DATE,
      v_wet        IN   DATE,
      v_comment    IN   VARCHAR2
   );

   PROCEDURE revoke_agent_system (v_agn_code IN NUMBER, v_sys_code IN NUMBER);

   PROCEDURE tqc_account_contacts_prc (
      v_add_edit               IN       VARCHAR2,
      v_account_contacts_tab   IN       tqc_account_contacts_tab,
      v_user                   IN       VARCHAR2,
      v_err                    OUT      VARCHAR2,
      v_accc_sys_code          IN       NUMBER
   );

   PROCEDURE change_agn_acc_password (
      v_acc_code   IN   NUMBER,
      v_new_pwd    IN   NUMBER
   );

   PROCEDURE change_agn_sht_desc (
      v_agn_code       IN   NUMBER,
      v_agn_sht_desc   IN   VARCHAR2,
      v_agn_act_code   IN   NUMBER,
      v_new_sht_desc   IN   VARCHAR2,
      v_user           IN   VARCHAR2
   );

   TYPE agency_accounts_rec IS RECORD (
      aga_code           tqc_agency_accounts.aga_code%TYPE,
      aga_sht_desc       tqc_agency_accounts.aga_sht_desc%TYPE,
      aga_name           tqc_agency_accounts.aga_name%TYPE,
      aga_agn_code       tqc_agency_accounts.aga_agn_code%TYPE,
      aga_created_by     tqc_agency_accounts.aga_created_by%TYPE,
      aga_date_created   tqc_agency_accounts.aga_date_created%TYPE,
      aga_status         tqc_agency_accounts.aga_status%TYPE,
      aga_remarks        tqc_agency_accounts.aga_remarks%TYPE,
      aga_wef            tqc_agency_accounts.aga_wef%TYPE,
      aga_wet            tqc_agency_accounts.aga_wet%TYPE,
      div_name           tqc_divisions.div_name%TYPE,
      div_code           NUMBER
   );

   TYPE agency_accounts_ref IS REF CURSOR
      RETURN agency_accounts_rec;

   FUNCTION get_agency_accounts (v_agency_code IN NUMBER)
      RETURN agency_accounts_ref;

   PROCEDURE create_agency_account (
      v_add_edit       VARCHAR2,
      v_acc_code       NUMBER,
      v_sht_desc       VARCHAR2,
      v_name           VARCHAR2,
      v_agn_code       NUMBER,
      v_created_by     VARCHAR2,
      v_date_created   DATE,
      v_status         VARCHAR2,
      v_remarks        VARCHAR2,
      v_wef            DATE,
      v_wet            DATE,
      v_div_code       NUMBER
   );

   PROCEDURE create_agency_client (
      v_agnc_code        tqc_agency_clients.agnc_code%TYPE,
      v_agnc_agn_code    tqc_agency_clients.agnc_agn_code%TYPE DEFAULT NULL,
      v_agnc_clnt_code   tqc_agency_clients.agnc_clnt_code%TYPE DEFAULT NULL,
      v_add_edit         VARCHAR2 DEFAULT NULL
   );

   PROCEDURE tqc_embassy_contacts_prc (
      v_add_edit               IN       VARCHAR2,
      v_account_contacts_tab   IN       tqc_account_contacts_tab,
      v_user                   IN       VARCHAR2,
      v_err                    OUT      VARCHAR2
   );

   PROCEDURE create_agency_serv_prov (
      v_agnt_code       tqc_agency_service_providers.agnt_code%TYPE,
      v_agnt_spr_code   tqc_agency_service_providers.agnt_spr_code%TYPE
            DEFAULT NULL,
      v_agnt_agn_code   tqc_agency_service_providers.agnt_agn_code%TYPE
            DEFAULT NULL,
      v_add_edit        VARCHAR2 DEFAULT NULL
   );

   FUNCTION getagentshtdesc (
      v_act_type         IN   VARCHAR2,
      v_brn_sht_desc     IN   VARCHAR2,
      v_agn_name         IN   VARCHAR2,
      v_direct_srl_fmt   IN   VARCHAR2
   )
      RETURN VARCHAR2;

   PROCEDURE getagencyprincipledir (
      v_agn_code    IN   NUMBER,
      v_principle   IN   VARCHAR2
   );
   PROCEDURE add_agency_product(v_agent_code IN NUMBER, v_prod_code IN NUMBER, v_add_edit IN VARCHAR2);
   PROCEDURE promoteDemoteManager(
            v_agn_code IN NUMBER, 
            v_promo_promote IN VARCHAR2, 
            v_trans_type IN VARCHAR2,
            v_demote IN VARCHAR);
PROCEDURE agent_status_prc (
      v_result        OUT      VARCHAR2,
      v_err           OUT      VARCHAR2,
      v_agent_code   IN       NUMBER,
      v_status        IN       VARCHAR2
   );
PROCEDURE Agent_Field_Log_Prc (
	 v_AGN_CODE in number,
	 v_no in number,
	 v_field IN VARCHAR2,
	 v_action IN VARCHAR2,
	 v_old_val IN VARCHAR2,
	 v_new_val IN VARCHAR2,
	 v_date date,
	 v_posted_by IN VARCHAR2
 );               
END tqc_agencies_pkg; 
/