--
-- TQC_UTILITIES_CURSOR  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_utilities_cursor
AS
/******************************************************************************
   NAME:       TQC_UTILITIES_CURSOR
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        8/6/2012             1. Created this package.
******************************************************************************/
   TYPE loaded_clients_rec IS RECORD (
      cln_code                  tqc_client_temp.cln_code%TYPE,
      cln_clnt_sht_desc         tqc_client_temp.cln_clnt_sht_desc%TYPE,
      cln_clnt_twn_code         tqc_client_temp.cln_clnt_twn_code%TYPE,
      cln_clnt_name             tqc_client_temp.cln_clnt_name%TYPE,
      cln_clnt_other_names      tqc_client_temp.cln_clnt_other_names%TYPE,
      cln_clnt_postal_addrs     tqc_client_temp.cln_clnt_postal_addrs%TYPE,
      cln_clnt_physical_addrs   tqc_client_temp.cln_clnt_physical_addrs%TYPE,
      cln_clnt_tel              tqc_client_temp.cln_clnt_tel%TYPE,
      cln_clnt_tel2             tqc_client_temp.cln_clnt_tel2%TYPE,
      cln_clnt_fax              tqc_client_temp.cln_clnt_fax%TYPE,
      cln_clnt_cntct_email_1    tqc_client_temp.cln_clnt_cntct_email_1%TYPE,
      cln_clnt_id_reg_no        tqc_client_temp.cln_clnt_id_reg_no%TYPE,
      cln_clnt_dob              tqc_client_temp.cln_clnt_dob%TYPE,
      cln_remarks               tqc_client_temp.cln_remarks%TYPE
   );

   TYPE loaded_clients_ref IS REF CURSOR
      RETURN loaded_clients_rec;

   FUNCTION search_loaded_clients (cln_loaded IN VARCHAR)
      RETURN loaded_clients_ref;

   TYPE loaded_agents_rec IS RECORD (
      agnl_code                   tqc_agencies_temp.agnl_code%TYPE,
      agnl_agn_act_code           tqc_agencies_temp.agnl_agn_act_code%TYPE,
      agnl_account_type           tqc_agencies_temp.agnl_account_type%TYPE,
      agnl_agn_acc_name           tqc_agencies_temp.agnl_agn_acc_name%TYPE,
      agnl_agn_physical_address   tqc_agencies_temp.agnl_agn_physical_address%TYPE,
      agnl_agn_postal_address     tqc_agencies_temp.agnl_agn_postal_address%TYPE,
      agnl_agn_twn_name           tqc_agencies_temp.agnl_agn_twn_name%TYPE,
      agnl_agn_reg_code           tqc_agencies_temp.agnl_agn_reg_code%TYPE,
      agnl_agn_contact_person     tqc_agencies_temp.agnl_agn_contact_person%TYPE,
      agnl_agn_tel1               tqc_agencies_temp.agnl_agn_tel1%TYPE,
      agnl_agn_fax                tqc_agencies_temp.agnl_agn_fax%TYPE,
      agnl_agn_email_address      tqc_agencies_temp.agnl_agn_email_address%TYPE,
      agnl_agn_date_created       tqc_agencies_temp.agnl_agn_date_created%TYPE,
      agnl_agn_check_date         tqc_agencies_temp.agnl_agn_check_date%TYPE,
      agnl_agn_branch_name        tqc_agencies_temp.agnl_agn_branch_name%TYPE
   );

   TYPE loaded_agents_ref IS REF CURSOR
      RETURN loaded_agents_rec;

   FUNCTION search_loaded_agents (options IN VARCHAR2)
      RETURN loaded_agents_ref;

   TYPE tqc_branches_rec IS RECORD (
      brn_code       tqc_branches.brn_code%TYPE,
      brn_sht_desc   tqc_branches.brn_sht_desc%TYPE,
      brn_reg_code   tqc_branches.brn_reg_code%TYPE,
      brn_name       tqc_branches.brn_name%TYPE
   );

   TYPE tqc_branches_ref IS REF CURSOR
      RETURN tqc_branches_rec;

   FUNCTION selectallbranches
      RETURN tqc_branches_ref;
END tqc_utilities_cursor; 
/