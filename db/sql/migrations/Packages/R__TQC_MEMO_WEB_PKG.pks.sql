--
-- TQC_MEMO_WEB_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_MEMO_WEB_PKG AS
  PROCEDURE EditableCopy(v_mem_code  NUMBER,
                         v_memh_code NUMBER,
                         v_ref OUT  varchar2,
                         v_user IN   VARCHAR2,
                         v_new_mem_code OUT NUMBER);
  PROCEDURE memoSubject(v_comm_mtyp_code   NUMBER,
                        v_user             VARCHAR2,
                        v_comem_code       NUMBER,
                        v_cmb_pol_batch_no NUMBER,
                        v_cmb_claim_no     VARCHAR2,
                        v_gcc_code         NUMBER,
                        v_appl_lvl         VARCHAR2);
  PROCEDURE processMemorandumHeader(v_memo_subject     VARCHAR2,
                                    v_memh_code        NUMBER DEFAULT NULL,
                                    v_comem_code       NUMBER,
                                    v_cmb_pol_batch_no NUMBER,
                                    v_cmb_claim_no     VARCHAR2,
                                    v_gcc_code         NUMBER,
                                    v_appl_lvl         VARCHAR2,
                                    v_mtyp_code        NUMBER);
  PROCEDURE processMemorandumBody(v_mem_dtls_code    NUMBER,
                                  v_msg_dtls         VARCHAR2,
                                  v_comem_code       NUMBER,
                                  v_cmb_pol_batch_no NUMBER,
                                  v_cmb_claim_no     VARCHAR2,
                                  v_gcc_code         NUMBER,
                                  v_appl_lvl         VARCHAR2);
  PROCEDURE saveCCList(v_mem_code   NUMBER,
                       v_cc_name    VARCHAR2,
                       v_cc_address VARCHAR2,
                       v_cc_remarks VARCHAR2,
                       v_cc_type    VARCHAR2,
                       v_cc_code    NUMBER default null);
  PROCEDURE deleteCCList(v_cc_code NUMBER);
  PROCEDURE updateMemo(v_user           VARCHAR2,
                       v_appl_lvl       VARCHAR2,
                       v_memo_date      DATE,
                       v_policy_number  VARCHAR2,
                       v_quot_no        VARCHAR2,
                       v_quot_code      NUMBER,
                       v_endos_no       VARCHAR2,
                       v_clm_no         VARCHAR2 DEFAULT NULL,
                       v_gcc_code       NUMBER DEFAULT NULL,
                       v_court_case_no  VARCHAR2 DEFAULT NULL,
                       v_insured        VARCHAR2,
                       v_subject        VARCHAR2,
                       v_addressee      VARCHAR2,
                       v_address        VARCHAR2,
                       v_signatory      VARCHAR2,
                       v_authorised     VARCHAR2,
                       v_mtyp_code      NUMBER,
                       v_file_no        VARCHAR2,
                       v_comem_brn_code NUMBER,
                       v_mem_desc       VARCHAR2,
                       v_corr_code      NUMBER DEFAULT NULL,
                       v_branch         VARCHAR2,
                       v_addresse_type  VARCHAR2,
                       v_your_ref       VARCHAR2,
                       v_batch_no       NUMBER,
                       v_ref            VARCHar2,
                       v_rpt_type       VARCHAR2,
                       v_code_town      VARCHAR2,
                       v_mem_code       OUT NUMBER,
                       v_tran_type      VARCHAR2,
                       v_memo_subject   IN VARCHAR2,
                       v_mem_detail     IN VARCHAR2,
                       v_add_edit       IN VARCHAR2,
                       v_comem_issued IN VARCHAR2 DEFAULT NULL,
                       v_refno          OUT  VARCHAR2,
                       v_mem_pk         IN  NUMBER,
                       v_prp_code       IN   NUMBER    /*
  *  INTRODUCED v_prp_code AS ONE OF THE VARIABLES
  * THIS IS TO TAKE CARE OF DISPLAY OF CLIENT/AGENT OR SERVICE PROVIDERS IN LETTERS AND MEMOS
  * BUG : GIS-12254
  *
  *
  */
                       );
 PROCEDURE authorise_memo(v_user      VARCHAR2,
                           v_memo_code NUMBER,
                           v_status    OUT VARCHAR2,
                           v_auth_dt   OUT DATE,
                           v_sign      OUT VARCHAR2,
                           /**introduced v_seq_type as a variable 
                           *This is to get the Sequence  Type 
                           *bug GIS-12356
                           */
                           v_seq_type   VARCHAR2,
                           v_ref_no OUT VARCHAR2);                      
                       
                       PROCEDURE authorise_memo030915(v_user      VARCHAR2,
                           v_memo_code NUMBER,
                           v_status    OUT VARCHAR2,
                           v_auth_dt   OUT DATE,
                           v_sign      OUT VARCHAR2);
--  PROCEDURE authorise_memo(v_user      VARCHAR2,
--                           v_memo_code NUMBER,
--                           v_status    OUT VARCHAR2,
--                           v_auth_dt   OUT DATE,
--                           v_sign      OUT VARCHAR2);
  PROCEDURE cancel_authorise_memo(v_user VARCHAR2, v_memo_code NUMBER);
  PROCEDURE delete_memo(v_memo_code NUMBER);
  FUNCTION PROCESS_GIS_POL_MEMOBK130514(v_pol_batch_no IN NUMBER,
                                v_claim_no     IN VARCHAR2,
                                v_gcc_code     IN NUMBER,
                                v_raw_txt      IN VARCHAR2,
                                v_app_lvl      IN VARCHAR2) RETURN VARCHAR2;
                                
  FUNCTION PROCESS_GIS_POL_MEMO(v_pol_batch_no IN NUMBER,
                                v_claim_no     IN VARCHAR2,
                                v_gcc_code     IN NUMBER,
                                v_raw_txt      IN VARCHAR2,
                                v_app_lvl      IN VARCHAR2) RETURN VARCHAR2;

  PROCEDURE process_memo_header(v_appl_lvl     IN VARCHAR2,
                                v_claim_no     IN VARCHAR2,
                                v_pol_no       IN VARCHAR2,
                                v_memo_subject OUT VARCHAR2);
  FUNCTION create_memo(v_sys_code        IN NUMBER,
                       v_appl_lvl        IN VARCHAR2,
                       v_mtyp_code       IN NUMBER,
                       v_addressee_type  IN NUMBER,
                       v_date            IN DATE,
                       v_in_quot_code    IN NUMBER,
                       v_in_pol_code     IN NUMBER,
                       v_in_clnt_code    IN NUMBER,
                       v_in_agn_code     IN NUMBER,
                       v_in_insured_code IN NUMBER,
                       v_in_claim_no     IN VARCHAR2,
                       v_in_cld_code     IN NUMBER,
                       v_in_spr_code     IN NUMBER,
                       v_in_gcc_code     IN NUMBER,
                       v_in_case_no      IN VARCHAR2,
                       v_memo_cc_tab     IN TQC_COMPANY_MEMO_CC_TAB,
                       v_user            IN VARCHAR2) RETURN NUMBER;
                      
   PROCEDURE update_reminder_status(v_comem_code NUMBER,
                                   v_user VARCHAR2);
                                   FUNCTION MERGE_DOCS_RQRD(v_claim_no      IN VARCHAR2,
                         v_pol_batch_no  IN NUMBER,
                         v_doc_submited  IN VARCHAR2) RETURN VARCHAR2;
END TQC_MEMO_WEB_PKG; 
/