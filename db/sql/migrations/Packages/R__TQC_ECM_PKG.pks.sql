--
-- TQC_ECM_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM."TQC_ECM_PKG"
AS
   PROCEDURE ecm_docs (
      v_quot_code   IN   NUMBER,
      v_pol_code    IN   NUMBER,
      v_pol_no      IN   VARCHAR2,
      v_clm_no      IN   VARCHAR2,
      v_sys_code    IN   NUMBER,
      v_sys_lvl     IN   VARCHAR2,
      v_files_tab   IN   files_tab,
      v_user             VARCHAR2
   );

   TYPE ecm_docs_rec IS RECORD (
      docr_id              tqc_documents_register.docr_id%TYPE,
      docr_doc_name        tqc_documents_register.docr_doc_name%TYPE,
      docr_doc_url         tqc_documents_register.docr_doc_url%TYPE,
      docr_doc_author      tqc_documents_register.docr_doc_author%TYPE,
      docr_doc_desc        tqc_documents_register.docr_doc_desc%TYPE,
      docr_clnt_code       tqc_documents_register.docr_clnt_code%TYPE,
      docr_pol_policy_no   tqc_documents_register.docr_pol_policy_no%TYPE,
      docr_claim_no        tqc_documents_register.docr_claim_no%TYPE,
      docr_quot_code       tqc_documents_register.docr_quot_code%TYPE,
      docr_level           tqc_documents_register.docr_level%TYPE,
      docr_sys_code        tqc_documents_register.docr_sys_code%TYPE,
      docr_pol_batch_no    tqc_documents_register.docr_pol_batch_no%TYPE,
      docr_date_created    tqc_documents_register.docr_date_created%TYPE,
      docr_agn_code        tqc_documents_register.docr_agn_code%TYPE
   );

   TYPE ecm_docs_ref IS REF CURSOR
      RETURN ecm_docs_rec;

   FUNCTION get_ecm_docs (
      v_clnt_code   IN   NUMBER DEFAULT -2000,
      v_agn_code    IN   NUMBER DEFAULT -2000,
      v_pol_code    IN   NUMBER DEFAULT -2000,
      v_quot_code   IN   NUMBER DEFAULT -2000,
      v_clm_no      IN   VARCHAR2 DEFAULT 'ALL',
      v_sys_code    IN   NUMBER DEFAULT -2000
   )
      RETURN ecm_docs_ref;

   PROCEDURE update_dispatch_docs (
      v_user           VARCHAR2,
      v_dt             DATE,
      v_dispatch_tab   files_tab
   );
END tqc_ecm_pkg; 
/