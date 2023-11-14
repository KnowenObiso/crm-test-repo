/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_PORTAL_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM."TQC_PORTAL_PKG"
AS
/******************************************************************************
   NAME:       TQ_CRM.TQC_PORTAL_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        26/May/2011             1. Created this package body.
******************************************************************************/
   PROCEDURE raise_error (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
   BEGIN
      IF v_err_no IS NULL
      THEN
         raise_application_error (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         raise_application_error (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

   FUNCTION getaccountcontacts
      RETURN account_contacts_ref
   IS
      v_cursor   account_contacts_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT accc_code, accc_username, accc_name,
                TO_CHAR (accc_last_login_date, 'RRRR-MM-DD'), accc_status,
                accc_pwd, accc_agn_code
           FROM tqc_account_contacts;

      RETURN v_cursor;
   END;

   FUNCTION getproducts
      RETURN products_ref
   IS
      v_cursor   products_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT prod_code, prod_sht_desc, prod_desc, prod_type,
                prod_yr_to_month_rate, prod_yr_to_quater_rate,
                prod_yr_to_s_annl_rate, prod_mnth_to_qtr_fctor,
                prod_mnth_to_s_annl_fctor, prod_mnth_to_annl_fctor
           FROM lms_products, lms_classes
          WHERE prod_cla_code = cla_code AND cla_type = 'O';

      RETURN v_cursor;
   END;

   FUNCTION getproductoptions
      RETURN prod_options_ref
   IS
      v_cursor   prod_options_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT pop_code, pop_prod_code, pop_sht_desc, pop_desc
           FROM lms_prod_options;

      RETURN v_cursor;
   END;

   FUNCTION getprodpremmasks
      RETURN prod_prem_masks_ref
   IS
      v_cursor   prod_prem_masks_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT pmas_code, pmas_prod_code, pmas_sht_desc, pmas_desc
           FROM lms_premium_masks;

      RETURN v_cursor;
   END;

   PROCEDURE create_clnt_proc (
      v_accountcode        IN       NUMBER,
      v_first_name         IN       VARCHAR2,
      v_middle_name        IN       VARCHAR2,
      v_surname            IN       VARCHAR2,
      v_syscode            IN       NUMBER,
      v_username           IN       VARCHAR2,
      v_clientcode         OUT      NUMBER,
      v_clnt_id_reg_no     IN       VARCHAR2,
      v_clnt_bank_acc_no   IN       VARCHAR2,
      v_clnt_twn_code      IN       NUMBER
   )
   IS
      v_use_names_in_code   VARCHAR2 (1)   := 'Y';
      v_direct_srl_fmt      VARCHAR2 (100);
      v_direct_clnt_seq     NUMBER;
      v_accnt_no            VARCHAR2 (35);
      v_clnt_param          VARCHAR2 (20);
      v_clnt_sht_desc       VARCHAR2 (30);
      v_brncode             NUMBER;
   BEGIN
      BEGIN
         SELECT tqc_parameters_pkg.get_param_varchar ('USE_NAMES_IN_CODE')
           INTO v_use_names_in_code
           FROM DUAL;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_use_names_in_code := 'Y';
      END;

      --RAISE_ERROR('v_sysCode'||v_sysCode);
      BEGIN
         SELECT brn_code
           INTO v_brncode
           FROM tqc_branches, tqc_regions, tqc_systems
          WHERE brn_reg_code = reg_code
            AND reg_org_code = sys_org_code
            AND sys_code = v_syscode
            AND brn_web_default = 'Y'
            AND ROWNUM <= 1;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error ('ERROR . No Default Branch Defined');
      END;

      --RAISE_ERROR('YESY');
      IF NVL (v_use_names_in_code, 'Y') = 'N'
      THEN
         BEGIN
            SELECT act_id_serial_format
              INTO v_direct_srl_fmt
              FROM tqc_account_types
             WHERE act_code = 1;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error getting direct clients format..');
         END;

         IF v_direct_srl_fmt IS NULL
         THEN
            raise_error ('Provide Id Format for the Direct Account type');
         END IF;

         BEGIN
            SELECT direct_clnt_seq.NEXTVAL
              INTO v_direct_clnt_seq
              FROM DUAL;

            -- v_accnt_no:= REPLACE(v_direct_srl_fmt,'[SERIALNO]',LPAD(v_direct_clnt_seq,6,0));
            v_accnt_no := LPAD (v_direct_clnt_seq, 6, 0);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error fetching the sequence...');
         END;

         BEGIN
            v_clnt_param :=
                    tqc_parameters_pkg.get_param_varchar ('CLIENT_ID_FORMAT');
         -- TQC_PARAMETERS
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error fetching client parameter...');
         END;

         IF NVL (v_clnt_param, 'DEFAULT') = 'DEFAULT'
         THEN
            --v_clnt_sht_desc := v_gen_acc_no;
            v_clnt_sht_desc :=
               tqc_sequences_pkg.get_number_format
                                                ('C',
                                                 v_brncode, --v_clnt_brn_code,
                                                 TO_CHAR (SYSDATE, 'RRRR'),
                                                 'I',
                                                 v_direct_srl_fmt,
                                                 v_surname,
                                                 v_first_name
                                                );
         END IF;
      END IF;

      IF v_clnt_sht_desc IS NULL
      THEN
         BEGIN
            v_clnt_sht_desc :=
               tqc_clients_pkg.get_client_sht_desc (v_first_name,
                                                    v_middle_name,
                                                    v_surname
                                                   );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error creating client Id...' || v_clnt_sht_desc);
         END;
      END IF;

      IF NVL (v_clnt_param, 'DEFAULT') = 'FMSURNAME'
      THEN
         v_accnt_no := v_clnt_sht_desc;
      END IF;

      IF v_accnt_no IS NULL
      THEN
         v_accnt_no := 'Y';
      END IF;

      BEGIN
         SELECT    TO_NUMBER (TO_CHAR (SYSDATE, 'YY'))
                || tqc_clnt_code_seq.NEXTVAL
           INTO v_clientcode
           FROM DUAL;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error Fetching proposer code..');
      END;

      BEGIN
         --RAISE_ERROR(v_surname);
         INSERT INTO tqc_clients
                     (clnt_code,
                      clnt_name,
                      clnt_sht_desc, clnt_wef, clnt_created_by,
                      clnt_date_created, clnt_direct_client, clnt_accnt_no,
                      clnt_email_addrs, clnt_id_reg_no, clnt_bank_acc_no,
                      clnt_twn_code
                     )
              VALUES (v_clientcode,
                      LTRIM (RTRIM (v_first_name || ' ' || v_middle_name)),
                      v_clnt_sht_desc, TRUNC (SYSDATE), 'CLNT_PORT_SYS',
                      TRUNC (SYSDATE), 'Y', v_accnt_no,
                      v_username, v_clnt_id_reg_no, v_clnt_bank_acc_no,
                      v_clnt_twn_code
                     );
--                UPDATE TQC_CLIENT_WEB_ACCOUNTS SET ACWA_CLNT_CODE = v_clientCode
--                WHERE ACWA_CODE = v_accountCode;
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('Error creating the client..');
      END;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Creating Client ' || SQLERRM (SQLCODE));
   END create_clnt_proc;

   PROCEDURE categories_proc (
      v_addedit    VARCHAR2,
      v_syscode    NUMBER,
      v_twpccode   NUMBER,
      v_twpcname   VARCHAR2,
      v_twpcdesc   VARCHAR2
   )
   IS
   BEGIN
      IF v_syscode IS NULL  AND v_addedit != 'D'
      THEN
        RAISE_ERROR('Select System First!'); 
      END IF;
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_web_product_categories
                     (twpc_code, twpc_sys_code, twpc_name,
                      twpc_description
                     )
              VALUES (twpc_code_seq.NEXTVAL, v_syscode, v_twpcname,
                      v_twpcdesc
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_web_product_categories
            SET twpc_sys_code = v_syscode,
                twpc_name = v_twpcname,
                twpc_description = v_twpcdesc
          WHERE twpc_code = v_twpccode;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_web_product_categories
               WHERE twpc_code = v_twpccode;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Category ' || SQLERRM (SQLCODE));
   END categories_proc;

   PROCEDURE products_proc (
      v_addedit              VARCHAR2,
      v_twpcode              NUMBER,
      v_twpccode             NUMBER,
      v_prodcode             NUMBER,
      v_proddesc             VARCHAR2,
      v_twp_binder      IN   VARCHAR2 DEFAULT NULL,
      v_twp_bind_code   IN   NUMBER DEFAULT NULL,
      v_twp_aga_code    IN   NUMBER DEFAULT NULL,
      v_twp_agn_code    IN   NUMBER DEFAULT NULL
   )
   IS
   BEGIN
      -- RAISE_ERROR('v_prodCode'||v_prodCode);
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_web_products
                     (twp_code, twp_twpc_code, twp_prod_code,
                      twp_prod_desc, twp_binder, twp_bind_code,
                      twp_aga_code, twp_agn_code
                     )
              VALUES (twp_code_seq.NEXTVAL, v_twpccode, v_prodcode,
                      v_proddesc, v_twp_binder, v_twp_bind_code,
                      v_twp_aga_code, v_twp_agn_code
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_web_products
            SET twp_prod_code = v_prodcode,
                twp_prod_desc = v_proddesc,
                twp_binder = v_twp_binder,
                twp_bind_code = v_twp_bind_code,
                twp_agn_code = v_twp_agn_code,
                twp_aga_code = v_twp_aga_code
          WHERE twp_code = v_twpcode;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_web_products
               WHERE twp_code = v_twpcode;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Product ' || SQLERRM (SQLCODE));
   END products_proc;

   PROCEDURE options_proc (
      v_addedit               VARCHAR2,
      v_twpocode              NUMBER,
      v_popcode               NUMBER,
      v_twpodesc              VARCHAR2,
      v_twpo_bind_code   IN   NUMBER,
      v_twpo_twp_code    IN   NUMBER
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_web_prod_options
                     (twpo_code, twpo_pop_code, twpo_desc,
                      twpo_bind_code, twpo_twp_code
                     )
              VALUES (twpo_code_seq.NEXTVAL, v_popcode, v_twpodesc,
                      v_twpo_bind_code, v_twpo_twp_code
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_web_prod_options
            SET twpo_pop_code = v_popcode,
                twpo_desc = v_twpodesc,
                twpo_bind_code = v_twpo_bind_code,
                twpo_twp_code = v_twpo_twp_code
          WHERE twpo_code = v_twpocode;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_web_prod_options
               WHERE twpo_code = v_twpocode;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Product Option' || SQLERRM (SQLCODE)
                     );
   END options_proc;

   PROCEDURE authenticateaccount (
      v_account_number   IN       VARCHAR2,
      v_exists           OUT      VARCHAR2
   )
   IS
      v_cnt   NUMBER;

      CURSOR prevrecords_ref
      IS
         SELECT *
           FROM tqc_sys_mod_subunits_inputs
          WHERE tsmsi_tsmsd_code = 161;
   BEGIN
      v_cnt := 0;
      v_exists := 'false';

      FOR prevrecords_rec IN prevrecords_ref
      LOOP
         IF v_account_number = prevrecords_rec.tsmsi_value
         THEN
            v_cnt := v_cnt + 1;
         END IF;
      END LOOP;

      IF v_cnt >= 2
      THEN
         v_exists := 'true';
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Failed To Determine if The Account Exists');
   END;

PROCEDURE checkIfUnitManager (
      v_agent_code   IN       NUMBER,
      v_exists           OUT      NUMBER
   )
   IS
      v_cnt   NUMBER;
    BEGIN
        SELECT     COUNT(1)
                            INTO v_cnt
        FROM     TQC_BRANCH_UNITS INNER JOIN
                        LMS_AGENCIES ON BRU_AGN_CODE = AGN_CODE
            WHERE  AGN_CODE = v_agent_code;
            
            IF v_cnt = 0 THEN
                v_exists := 0;
             ELSE
                v_exists := 1;
            END IF;
    END;
    
   PROCEDURE authenticateidnumber (
      v_idnumber   IN       VARCHAR2,
      v_exists     OUT      VARCHAR2
   )
   IS
      v_cnt   NUMBER;

      CURSOR prevrecords_ref
      IS
         SELECT *
           FROM tqc_sys_mod_subunits_inputs
          WHERE tsmsi_tsmsd_code = 141;
   BEGIN
      v_cnt := 0;
      v_exists := 'false';

      FOR prevrecords_rec IN prevrecords_ref
      LOOP
         IF v_idnumber = prevrecords_rec.tsmsi_value
         THEN
            v_cnt := v_cnt + 1;
         END IF;
      END LOOP;

      IF v_cnt >= 2
      THEN
         v_exists := 'true';
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Failed To Determine if the Id exists');
   END;

   FUNCTION getclientdtls (v_clnt_code NUMBER)
      RETURN client_dtls_ref
   IS
      v_cursor   client_dtls_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT clnt_code, clnt_postal_addrs, clnt_id_reg_no,
                clnt_physical_addrs, clnt_other_names, clnt_surname,
                clnt_title, clnt_dob, clnt_email_addrs, clnt_tel, 
                NULL,
                NULL,
                NULL,
                NVL(prp_idntf_doc_used, 'I') prp_idntf_doc_used
           FROM tqc_clients LEFT OUTER JOIN lms_proposers ON prp_clnt_code = clnt_code
          WHERE clnt_code = v_clnt_code;

      RETURN v_cursor;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Occured ' || SQLERRM (SQLCODE));
   END;

   PROCEDURE updateuserdetails (
      v_clnt_code        NUMBER,
      v_postal_addrs     VARCHAR2,
      v_prp_id_reg_no    VARCHAR2,
      v_physical_addrs   VARCHAR2,
      v_othernames       VARCHAR2,
      v_surname          VARCHAR2,
      v_title            VARCHAR2,
      v_dob              DATE,
      v_email_addrs      VARCHAR2,
      v_tel              VARCHAR2,
      v_doc_used        VARCHAR2
   )
   IS
   BEGIN
   raise_error(v_clnt_code || ', ' || v_prp_id_reg_no || ' , ' || v_doc_used);
      IF v_clnt_code IS NOT NULL
      THEN
         UPDATE tqc_clients
            SET clnt_postal_addrs = NVL (v_postal_addrs, clnt_postal_addrs),
                clnt_id_reg_no = NVL (v_prp_id_reg_no, clnt_id_reg_no),
                clnt_physical_addrs =
                                   NVL (v_physical_addrs, clnt_physical_addrs),
                clnt_other_names = NVL (v_othernames, clnt_other_names),
                clnt_surname = NVL (v_surname, clnt_surname),
                clnt_title = NVL (v_title, clnt_title),
                clnt_dob = NVL (v_dob, clnt_dob),
                clnt_email_addrs = NVL (v_email_addrs, clnt_email_addrs),
                clnt_tel = NVL (v_tel, clnt_tel)
          WHERE clnt_code = v_clnt_code;

         UPDATE lms_proposers
            SET prp_postal_address = NVL (v_postal_addrs, prp_postal_address),
                prp_id_reg_no = NVL (v_prp_id_reg_no, prp_id_reg_no),
                prp_physical_addr = NVL (v_physical_addrs, prp_physical_addr),
                prp_other_names = NVL (v_othernames, prp_other_names),
                prp_surname = NVL (v_surname, prp_surname),
                prp_title = NVL (v_title, prp_title),
                prp_dob = NVL (v_dob, prp_dob),
                prp_email = NVL (v_email_addrs, prp_email),
                prp_tel = NVL (v_tel, prp_tel),
                prp_idntf_doc_used = NVL(v_doc_used, prp_idntf_doc_used) 
          WHERE prp_clnt_code = v_clnt_code;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating Details');
   END;
END tqc_portal_pkg; 
/