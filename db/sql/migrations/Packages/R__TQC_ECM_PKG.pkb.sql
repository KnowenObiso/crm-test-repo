--
-- TQC_ECM_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM."TQC_ECM_PKG"
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

   PROCEDURE ecm_docs (
      v_quot_code   IN   NUMBER,
      v_pol_code    IN   NUMBER,
      v_pol_no      IN   VARCHAR2,
      v_clm_no      IN   VARCHAR2,
      v_sys_code    IN   NUMBER,
      v_sys_lvl     IN   VARCHAR2,
      v_files_tab   IN   files_tab,
      v_user             VARCHAR2
   )
   IS
      v_client_name       tqc_clients.clnt_name%TYPE;
      v_client_code       tqc_clients.clnt_code%TYPE;
      v_pol_batch_no      gin_policies.pol_batch_no%TYPE;
      v_pol_endos_no      gin_policies.pol_ren_endos_no%TYPE;
      v_agn_agnt_code     gin_policies.pol_agnt_agent_code%TYPE;
      v_pol_coinsurance   gin_policies.pol_coinsurance%TYPE;
      v_pol_coin_leader   gin_policies.pol_coinsure_leader%TYPE;
      v_pro_code          gin_policies.pol_pro_code%TYPE;
      v_email_addr        VARCHAR2 (75);
      v_pro_sht_desc      VARCHAR2 (75);
      v_doc_desc          VARCHAR2 (150);
   BEGIN
      IF NVL (v_sys_lvl, 'NB') = 'RN'
      THEN
         BEGIN
            SELECT DISTINCT clnt_email_addrs, clnt_code
                       INTO v_email_addr, v_client_code
                       FROM tqc_clients
                      WHERE clnt_code = (SELECT DISTINCT pol_prp_code
                                                    FROM gin_ren_policies
                                                   WHERE pol_batch_no =
                                                                    v_pol_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error client not defined..');
         END;

         BEGIN
            SELECT DISTINCT pro_sht_desc || ' Policy No: ' || pol_policy_no,
                            pol_pro_code, pro_sht_desc, pol_batch_no,
                            pol_ren_endos_no, pol_agnt_agent_code,
                            pol_coinsurance, pol_coinsure_leader
                       INTO v_doc_desc,
                            v_pro_code, v_pro_sht_desc, v_pol_batch_no,
                            v_pol_endos_no, v_agn_agnt_code,
                            v_pol_coinsurance, v_pol_coin_leader
                       FROM gin_ren_policies, gin_products
                      WHERE pol_batch_no IN (SELECT pol_batch_no
                                               FROM gin_ren_policies
                                              WHERE pol_batch_no = v_pol_code)
                        AND pol_pro_code = pro_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error getting product short name.');
         END;
      ELSE
         BEGIN
            SELECT DISTINCT clnt_email_addrs, clnt_code
                       INTO v_email_addr, v_client_code
                       FROM tqc_clients
                      WHERE clnt_code = (SELECT DISTINCT pol_prp_code
                                                    FROM gin_policies
                                                   WHERE pol_batch_no =
                                                                    v_pol_code);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error client not defined..');
         END;

         BEGIN
            SELECT DISTINCT pro_sht_desc || ' Policy No: ' || pol_policy_no,
                            pol_pro_code, pro_sht_desc, pol_batch_no,
                            pol_ren_endos_no, pol_agnt_agent_code,
                            pol_coinsurance, pol_coinsure_leader
                       INTO v_doc_desc,
                            v_pro_code, v_pro_sht_desc, v_pol_batch_no,
                            v_pol_endos_no, v_agn_agnt_code,
                            v_pol_coinsurance, v_pol_coin_leader
                       FROM gin_policies, gin_products
                      WHERE pol_batch_no IN (SELECT pol_batch_no
                                               FROM gin_policies
                                              WHERE pol_batch_no = v_pol_code)
                        AND pol_pro_code = pro_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error getting product short name.');
         END;
      END IF;

      FOR x IN 1 .. v_files_tab.COUNT
      LOOP
         BEGIN
            INSERT INTO tqc_documents_register
                        (docr_id, docr_doc_name,
                         docr_doc_url, docr_doc_author, docr_doc_desc,
                         docr_clnt_code, docr_pol_policy_no, docr_claim_no,
                         docr_quot_code, docr_level, docr_sys_code,
                         docr_pol_batch_no, docr_agn_code, docr_date_created
                        )
                 VALUES (tqc_docr_id_seq.NEXTVAL, v_files_tab (x).file_name,
                         v_files_tab (x).file_path, v_user, v_doc_desc,
                         v_client_code, v_pol_no, v_clm_no,
                         v_quot_code, v_sys_lvl, v_sys_code,
                         v_pol_code, v_agn_agnt_code, TRUNC (SYSDATE)
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error registering document..');
         END;
      END LOOP;
   END;

   FUNCTION get_ecm_docs (
      v_clnt_code   IN   NUMBER DEFAULT -2000,
      v_agn_code    IN   NUMBER DEFAULT -2000,
      v_pol_code    IN   NUMBER DEFAULT -2000,
      v_quot_code   IN   NUMBER DEFAULT -2000,
      v_clm_no      IN   VARCHAR2 DEFAULT 'ALL',
      v_sys_code    IN   NUMBER DEFAULT -2000
   )
      RETURN ecm_docs_ref
   IS
      v_retcur   ecm_docs_ref;
   BEGIN
      OPEN v_retcur FOR
         SELECT docr_id, docr_doc_name, docr_doc_url, docr_doc_author,
                docr_doc_desc, docr_clnt_code, docr_pol_policy_no,
                docr_claim_no, docr_quot_code, docr_level, docr_sys_code,
                docr_pol_batch_no, docr_date_created, docr_agn_code
           FROM tqc_documents_register,
                tqc_systems,
                tqc_clients,
                tqc_agencies
          WHERE sys_code(+) = docr_sys_code
            AND clnt_code(+) = docr_clnt_code
            AND agn_code(+) = docr_agn_code;

      /*AND DOCR_CLNT_CODE   = DECODE(NVL(v_clnt_code,-2000),-2000,DOCR_CLNT_CODE,v_clnt_code)
      AND DOCR_AGN_CODE    = DECODE(NVL(v_agn_code,-2000),-2000,DOCR_AGN_CODE,v_agn_code)
      AND DOCR_POL_BATCH_NO    = DECODE(NVL(v_pol_code,-2000),-2000,DOCR_POL_BATCH_NO,v_pol_code)
      AND DOCR_QUOT_CODE   = DECODE(NVL(v_quot_code,-2000),-2000,DOCR_QUOT_CODE,v_quot_code)
      AND DOCR_CLAIM_NO      = DECODE(NVL(v_clm_no,'ALL'),'ALL',DOCR_CLAIM_NO,v_clm_no)
      AND DOCR_SYS_CODE    = DECODE(NVL(v_sys_code,-2000),-2000,DOCR_SYS_CODE,v_sys_code);*/
      RETURN (v_retcur);
   END;

   PROCEDURE update_dispatch_docs (
      v_user           VARCHAR2,
      v_dt             DATE,
      v_dispatch_tab   files_tab
   )
   IS
   BEGIN
      IF v_dt IS NULL
      THEN
         raise_error ('You must Enter the Documents Dispatch Date....');
      ELSE
         FOR x IN 1 .. v_dispatch_tab.COUNT
         LOOP
            IF NVL (v_dispatch_tab (x).docr_dispatchable, 'P') != 'P'
            THEN
               IF     v_dispatch_tab (x).docr_dispatched = 'Y'
                  AND v_dispatch_tab (x).docr_dispatch_dt IS NULL
               THEN
                  BEGIN
                     UPDATE tqc_documents_register
                        SET docr_dispatch_dt = v_dt,
                            docr_dispatched = 'Y',
                            docr_dispatchable = 'Y'
                      WHERE docr_id = v_dispatch_tab (x).docr_id;

                     UPDATE gin_policies
                        SET pol_dispatch_dt = v_dt,
                            pol_dispatch_by = v_user
                      WHERE pol_dispatch_dt IS NULL
                        AND pol_batch_no =
                                          v_dispatch_tab (x).docr_pol_batch_no;
                  EXCEPTION
                     WHEN OTHERS
                     THEN
                        raise_error
                           ('Error:- 1. Cannot update the Document Register status. Contact Turnkey Africa for support..'
                           );
                  END;
               END IF;
            ELSE
               UPDATE tqc_documents_register
                  SET docr_dispatched = 'N'
                WHERE docr_id = v_dispatch_tab (x).docr_id;
            END IF;
         END LOOP;
      END IF;
   END;
END tqc_ecm_pkg; 
/