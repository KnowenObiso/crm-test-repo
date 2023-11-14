/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_LOADING_UTILITIES  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_loading_utilities
AS
   PROCEDURE tqc_clntagnt_loading_prc (
      v_add_edit               IN   VARCHAR2,
      v_tqc_clnt_loading_tbl        tqc_clnt_loading_tbl,
      v_binder                      VARCHAR2
   )
   IS
   BEGIN
      IF v_tqc_clnt_loading_tbl.COUNT = 0
      THEN
         raise_error ('CLIENT DATA DOES NOT EXIST');
      END IF;

      FOR i IN 1 .. v_tqc_clnt_loading_tbl.COUNT
      LOOP
         IF NVL (v_add_edit, 'A') = 'A'
         THEN
            INSERT INTO tqc_client_temp
                        (cln_code,
                         cln_clnt_sht_desc,
                         cln_clnt_twn_code,
                         cln_clnt_name,
                         cln_clnt_other_names,
                         cln_clnt_postal_addrs,
                         cln_clnt_physical_addrs,
                         cln_clnt_tel,
                         cln_clnt_tel2,
                         cln_clnt_fax,
                         cln_clnt_cntct_email_1,
                         cln_clnt_id_reg_no,
                         cln_clnt_dob
                        )
                 VALUES (tqc_cln_code_seq.NEXTVAL,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_sht_desc,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_twn_code,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_name,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_other_names,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_postal_addrs,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_physical_addrs,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_tel,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_tel2,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_fax,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_cntct_email_1,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_id_reg_no,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_dob
                        );
         END IF;
      END LOOP;
   END;

   PROCEDURE tqc_clnt_loading_prc (
      v_add_edit               IN   VARCHAR2,
      v_tqc_clnt_loading_tbl        tqc_clnt_loading_tbl,
      v_binder                      VARCHAR2
   )
   IS
   BEGIN
      IF v_tqc_clnt_loading_tbl.COUNT = 0
      THEN
         raise_error ('AGENTS DATA DOES NOT EXIST');
      END IF;

      FOR i IN 1 .. v_tqc_clnt_loading_tbl.COUNT
      LOOP
         IF NVL (v_add_edit, 'A') = 'A'
         THEN
            INSERT INTO tqc_client_temp
                        (cln_code,
                         cln_clnt_sht_desc,
                         cln_clnt_twn_code,
                         cln_clnt_name,
                         cln_clnt_other_names,
                         cln_clnt_postal_addrs,
                         cln_clnt_physical_addrs,
                         cln_clnt_tel,
                         cln_clnt_tel2,
                         cln_clnt_fax,
                         cln_clnt_cntct_email_1,
                         cln_clnt_id_reg_no,
                         cln_clnt_dob
                        )
                 VALUES (tqc_cln_code_seq.NEXTVAL,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_sht_desc,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_twn_code,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_name,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_other_names,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_postal_addrs,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_physical_addrs,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_tel,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_tel2,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_fax,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_cntct_email_1,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_id_reg_no,
                         v_tqc_clnt_loading_tbl (i).cln_clnt_dob
                        );
         END IF;
      END LOOP;
   END;

   PROCEDURE "RAISE_ERROR" (v_msg IN VARCHAR2, v_err_no IN NUMBER := NULL)
   IS
      v_dbmsg   VARCHAR2 (200) := NULL;
   BEGIN
      IF SQLCODE != 0
      THEN
         v_dbmsg := ' - ' || SQLERRM (SQLCODE);
      END IF;

      IF v_err_no IS NULL
      THEN
         raise_application_error (-20015, v_msg || v_dbmsg);
      ELSE
         raise_application_error (v_err_no, v_msg || v_dbmsg);
      END IF;
   END raise_error;

   PROCEDURE load_clients_prc (
      v_cln_code   IN   NUMBER,
      v_user       IN   VARCHAR2,
      v_gis        IN   VARCHAR2,
      v_lms        IN   VARCHAR2
   )
   IS
      v_cln_clnt_sht_desc         VARCHAR2 (50);
      v_cln_clnt_twn_code         VARCHAR2 (200);
      v_cln_clnt_name             VARCHAR2 (50);
      v_cln_clnt_other_names      VARCHAR2 (50);
      v_cln_clnt_postal_addrs     VARCHAR2 (50);
      v_cln_clnt_physical_addrs   VARCHAR2 (50);
      v_cln_clnt_tel              VARCHAR2 (50);
      v_cln_clnt_tel2             VARCHAR2 (50);
      v_cln_clnt_fax              VARCHAR2 (50);
      v_cln_clnt_cntct_email_1    VARCHAR2 (50);
      v_cln_clnt_id_reg_no        VARCHAR2 (50);
      v_cln_clnt_dob              DATE;
      v_towncode                  NUMBER;
      v_clnt_old_sht_desc         VARCHAR2 (50);
      v_cln_brn_sht_desc          VARCHAR2 (50);
      v_clnt_param                VARCHAR2 (50);
      v_brn_code                  NUMBER;
      v_client_type               VARCHAR2 (50);
      v_errmsg                    VARCHAR2 (300);
      v_err                       BOOLEAN;

      CURSOR cur_clients
      IS
         SELECT *
           FROM tqc_client_temp
          WHERE NVL (cln_loaded, 'N') != 'Y';
   BEGIN
      SELECT cln_clnt_sht_desc, cln_clnt_twn_code, cln_clnt_name,
             cln_clnt_other_names, cln_clnt_postal_addrs,
             cln_clnt_physical_addrs, cln_clnt_tel, cln_clnt_tel2,
             cln_clnt_fax, cln_clnt_cntct_email_1, cln_clnt_id_reg_no,
             cln_clnt_dob, clnt_old_sht_desc
        INTO v_clnt_old_sht_desc, v_cln_brn_sht_desc, v_cln_clnt_name,
             v_cln_clnt_other_names, v_cln_clnt_postal_addrs,
             v_cln_clnt_physical_addrs, v_cln_clnt_tel, v_cln_clnt_tel2,
             v_cln_clnt_fax, v_cln_clnt_cntct_email_1, v_cln_clnt_id_reg_no,
             v_cln_clnt_dob, v_cln_clnt_sht_desc
        FROM tqc_client_temp
       WHERE cln_code = v_cln_code;

      BEGIN
         BEGIN
            v_clnt_param :=
                    tqc_parameters_pkg.get_param_varchar ('CLIENT_ID_FORMAT');
         EXCEPTION
            WHEN OTHERS
            THEN
               v_errmsg := 'Error fetching client parameter...';
               v_err := TRUE;
         END;

         BEGIN
            UPDATE tqc_client_temp
               SET clnt_old_sht_desc = v_cln_clnt_sht_desc
             WHERE cln_code = v_cln_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_errmsg := 'Error Updating Client Short Desc..';
               NULL;
         END;

         IF NVL (v_clnt_param, 'DEFAULT') = 'DEFAULT'
         THEN
            v_cln_clnt_sht_desc :=
               tqc_sequences_pkg.get_number_format ('C',
                                                    v_cln_brn_sht_desc,
                                                    TO_CHAR (SYSDATE, 'RRRR'),
                                                    'I',
                                                    NULL,
                                                    v_cln_clnt_name,
                                                    v_cln_clnt_other_names
                                                   );
         END IF;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_errmsg := 'Error generating  Client Short Desc..';
      END;

      -- raise_error(v_cln_clnt_sht_desc);
      IF v_cln_clnt_sht_desc IS NULL
      THEN
         BEGIN
            v_cln_clnt_sht_desc :=
               tqc_clients_pkg.get_client_sht_desc (v_cln_clnt_name,
                                                    NULL,
                                                    v_cln_clnt_other_names
                                                   );
         EXCEPTION
            WHEN OTHERS
            THEN
               v_errmsg :=
                         'Error creating client Id...' || v_clnt_old_sht_desc;
               v_err := TRUE;
         END;
      END IF;

      -- raise_error(v_cln_clnt_sht_desc);
      IF v_cln_clnt_twn_code IS NOT NULL
      THEN
         BEGIN
            SELECT twn_code
              INTO v_towncode
              FROM tqc_towns
             WHERE twn_name = v_cln_clnt_twn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               v_towncode := NULL;
               NULL;
         END;
      END IF;

      INSERT INTO tqc_clients
                  (clnt_code, clnt_sht_desc,
                   clnt_twn_code, clnt_name, clnt_other_names,
                   clnt_postal_addrs, clnt_physical_addrs,
                   clnt_tel, clnt_tel2, clnt_fax,
                   clnt_cntct_email_1, clnt_id_reg_no,
                   clnt_dob, clnt_date_created, clnt_created_by, clnt_wef,
                   clnt_status, clnt_old_sht_desc
                  )
           VALUES (tqc_clnt_code_seq.NEXTVAL, v_cln_clnt_sht_desc,
                   v_towncode, v_cln_clnt_name, v_cln_clnt_other_names,
                   v_cln_clnt_postal_addrs, v_cln_clnt_physical_addrs,
                   v_cln_clnt_tel, v_cln_clnt_tel2, v_cln_clnt_fax,
                   v_cln_clnt_cntct_email_1, v_cln_clnt_id_reg_no,
                   v_cln_clnt_dob, SYSDATE, 'DATA LOADING', SYSDATE,
                   'A', v_clnt_old_sht_desc
                  );

      IF v_gis IS NOT NULL
      THEN
         BEGIN
            INSERT INTO tqc_client_systems
                        (csys_code,
                         csys_clnt_code, csys_sys_code, csys_wef, csys_wet
                        )
                 VALUES (tqc_csys_code_seq.NEXTVAL,
                         tqc_clnt_code_seq.CURRVAL, 37, SYSDATE, NULL
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               v_errmsg := 'Error asigning clients to GIS..';
               v_err := TRUE;
         END;
      END IF;

      -- RAISE_ERROR('desc'||v_cln_clnt_sht_desc);
      IF v_lms IS NOT NULL
      THEN
         BEGIN
            INSERT INTO tqc_client_systems
                        (csys_code,
                         csys_clnt_code, csys_sys_code, csys_wef, csys_wet
                        )
                 VALUES (tqc_csys_code_seq.NEXTVAL,
                         tqc_clnt_code_seq.CURRVAL, 27, SYSDATE, NULL
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               v_errmsg := 'Error asigning clients to LMS...';
               v_err := TRUE;
         END;
      END IF;

      UPDATE tqc_client_temp
         SET cln_loaded = 'Y'
       WHERE cln_code = v_cln_code;
   EXCEPTION
      WHEN OTHERS
      THEN
         --ROLLBACK;
           --v_errmsg:='You cannot Load an already existing Client...';
         UPDATE tqc_client_temp
            SET cln_loaded = 'N',
                cln_remarks = v_errmsg
          WHERE cln_code = v_cln_code;
   END;

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
   )
   IS
   BEGIN
      --RAISE_ERROR('v_cln_clnt_twn_code'||v_cln_clnt_twn_code);
      IF v_add_edit = 'A'
      THEN
         BEGIN
            INSERT INTO tqc_client_temp
                        (cln_code, cln_clnt_sht_desc,
                         cln_clnt_twn_code, cln_clnt_name,
                         cln_clnt_other_names, cln_clnt_postal_addrs,
                         cln_clnt_physical_addrs, cln_clnt_tel,
                         cln_clnt_tel2, cln_clnt_fax,
                         cln_clnt_cntct_email_1, cln_clnt_id_reg_no,
                         cln_clnt_dob
                        )
                 VALUES (tqc_clnt_code_seq.NEXTVAL, v_cln_clnt_sht_desc,
                         v_cln_clnt_twn_code, v_cln_clnt_name,
                         v_cln_clnt_other_names, v_cln_clnt_postal_addrs,
                         v_cln_clnt_physical_addrs, v_cln_clnt_tel,
                         v_cln_clnt_tel2, v_cln_clnt_fax,
                         v_cln_clnt_cntct_email_1, v_cln_clnt_id_reg_no,
                         v_cln_clnt_dob
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Insertion of data failed');
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_client_temp
               SET cln_clnt_sht_desc = v_cln_clnt_sht_desc,
                   cln_clnt_twn_code = v_cln_clnt_twn_code,
                   cln_clnt_name = v_cln_clnt_name,
                   cln_clnt_other_names = v_cln_clnt_other_names,
                   cln_clnt_postal_addrs = v_cln_clnt_postal_addrs,
                   cln_clnt_physical_addrs = v_cln_clnt_physical_addrs,
                   cln_clnt_tel = v_cln_clnt_tel,
                   cln_clnt_tel2 = v_cln_clnt_tel2,
                   cln_clnt_fax = v_cln_clnt_fax,
                   cln_clnt_cntct_email_1 = v_cln_clnt_cntct_email_1,
                   cln_clnt_id_reg_no = v_cln_clnt_id_reg_no,
                   cln_clnt_dob = v_cln_clnt_dob
             WHERE cln_code = v_cln_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Update of data failed');
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_client_temp
                  WHERE cln_code = v_cln_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Deletion of data failed');
         END;
      END IF;
   END;

   PROCEDURE tqc_agnt_loading_prc (
      v_add_edit               IN   VARCHAR2,
      v_tqc_agnt_loading_tbl        tqc_agnt_loading_tbl,
      v_binder                      VARCHAR2
   )
   IS
   BEGIN
      --RAISE_ERROR('v_add_edit'||v_add_edit);
      IF v_tqc_agnt_loading_tbl.COUNT = 0
      THEN
         raise_error ('AGENTS DATA DOES NOT EXIST');
      END IF;

      FOR i IN 1 .. v_tqc_agnt_loading_tbl.COUNT
      LOOP
         IF NVL (v_add_edit, 'A') = 'A'
         THEN
            --RAISE_ERROR();
            INSERT INTO tqc_agencies_temp
                        (agnl_code,
                         agnl_agn_act_code,
                         agnl_account_type,
                         agnl_agn_acc_name,
                         agnl_agn_physical_address,
                         agnl_agn_postal_address,
                         agnl_agn_twn_name,
                         agnl_agn_reg_code,
                         agnl_agn_contact_person,
                         agnl_agn_tel1,
                         agnl_agn_fax,
                         agnl_agn_email_address,
                         agnl_agn_date_created,
                         agnl_agn_check_date,
                         agnl_agn_branch_name
                        )
                 VALUES (tqc_agnl_code_seq.NEXTVAL,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_act_code,
                         v_tqc_agnt_loading_tbl (i).agnl_account_type,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_acc_name,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_physical_address,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_postal_address,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_twn_code,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_reg_code,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_contact_person,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_tel1,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_fax,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_email_address,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_date_created,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_check_date,
                         v_tqc_agnt_loading_tbl (i).agnl_agn_branch_name
                        );
         END IF;
      END LOOP;
   END;

--    PROCEDURE saveAgentDetails (
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
--   )
--   IS
--   BEGIN
--
--  -- RAISE_ERROR('v_cln_code'||v_cln_code||'v_cln_clnt_twn_code'||v_cln_clnt_twn_code||'v_cln_clnt_cou_code'||v_cln_clnt_cou_code);
--     IF v_add_edit = 'E'
--      THEN
--         BEGIN
--            UPDATE TQC_AGENCIES_TEMP
--              SET     AGNL_CODE=AGNL_CODE,
--                        AGNL_AGN_ACT_CODE=v_agnl_agn_act_code,
--                        AGNL_ACCOUNT_TYPE=v_agnl_account_type,
--                        AGNL_AGN_ACC_NAME=v_agnl_agn_acc_name,
--                        AGNL_AGN_PHYSICAL_ADDRESS=v_agnl_agn_physical_address,
--                        AGNL_AGN_POSTAL_ADDRESS=v_agnl_agn_postal_address,
--                        AGNL_AGN_TWN_NAME=v_agnl_agn_twn_name,
--                        AGNL_AGN_REG_CODE=v_agnl_agn_reg_code,
--                        AGNL_AGN_CONTACT_PERSON=v_agnl_agn_contact_person,
--                        AGNL_AGN_TEL1=v_agnl_agn_tel1,
--                        AGNL_AGN_FAX=v_agnl_agn_fax,
--                        AGNL_AGN_EMAIL_ADDRESS=v_agnl_agn_email_address,
--                        AGNL_AGN_DATE_CREATED=v_agnl_agn_date_created,
--                        AGNL_AGN_CHECK_DATE=v_agnl_agn_check_date,
--                        AGNL_AGN_BRANCH_NAME=v_agnl_branch_name
--
--             WHERE AGNL_CODE = v_agnl_code;
--
--            EXCEPTION
--                  WHEN OTHERS
--                  THEN
--                    RAISE_ERROR('Update of data failed');
--            END;
--
--      ELSIF v_add_edit = 'D'
--      THEN
--         BEGIN
--            DELETE FROM TQC_AGENCIES_TEMP
--                  WHERE AGNL_CODE = v_agnl_code;
--
--         EXCEPTION
--                  WHEN OTHERS
--                  THEN
--                    RAISE_ERROR('Deletion of data failed');
--         END;
--      END IF;
--   END;
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
   )
   IS
   BEGIN
       -- RAISE_ERROR('v_agnl_code'||v_agnl_code);
      -- RAISE_ERROR('v_cln_code'||v_cln_code||'v_cln_clnt_twn_code'||v_cln_clnt_twn_code||'v_cln_clnt_cou_code'||v_cln_clnt_cou_code);
      IF v_add_edit = 'E'
      THEN
         BEGIN
            UPDATE tqc_agencies_temp
               SET agnl_code = agnl_code,
                   agnl_agn_act_code = v_agnl_agn_act_code,
                   agnl_account_type = v_agnl_account_type,
                   agnl_agn_acc_name = v_agnl_agn_acc_name,
                   agnl_agn_physical_address = v_agnl_agn_physical_address,
                   agnl_agn_postal_address = v_agnl_agn_postal_address,
                   agnl_agn_twn_name = v_agnl_agn_twn_name,
                   agnl_agn_reg_code = v_agnl_agn_reg_code,
                   agnl_agn_contact_person = v_agnl_agn_contact_person,
                   agnl_agn_tel1 = v_agnl_agn_tel1,
                   agnl_agn_fax = v_agnl_agn_fax,
                   agnl_agn_email_address = v_agnl_agn_email_address,
                   agnl_agn_date_created = v_agnl_agn_date_created,
                   agnl_agn_check_date = v_agnl_agn_check_date,
                   agnl_agn_branch_name = v_agnl_branch_name
             WHERE agnl_code = v_agnl_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Update of data failed');
         END;
      ELSIF v_add_edit = 'D'
      THEN
         BEGIN
            DELETE FROM tqc_agencies_temp
                  WHERE agnl_code = v_agnl_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Deletion of data failed');
         END;
      END IF;
   END;

   PROCEDURE load_agents_prc (
      v_agnl_code   IN   NUMBER,
      v_user        IN   VARCHAR2,
      v_gis         IN   VARCHAR2,
      v_lms         IN   VARCHAR2
   )
   IS
      v_agnl_agn_act_code           VARCHAR2 (200);
      v_agnl_account_type           VARCHAR2 (200);
      v_agnl_agn_acc_name           VARCHAR2 (200);
      v_agnl_agn_physical_address   VARCHAR2 (200);
      v_agnl_agn_postal_address     VARCHAR2 (200);
      v_agnl_agn_twn_name           VARCHAR2 (200);
      v_agnl_agn_reg_code           VARCHAR2 (200);
      v_agnl_agn_contact_person     VARCHAR2 (200);
      v_agnl_agn_tel1               VARCHAR2 (200);
      v_agnl_agn_fax                VARCHAR2 (200);
      v_agnl_agn_email_address      VARCHAR2 (200);
      v_agnl_agn_date_created       DATE;
      v_agnl_agn_check_date         DATE;
      v_towncode                    NUMBER;
      v_brn_code                    NUMBER;
      v_agnl_agn_branch_name        VARCHAR2 (200);
      v_agnl_loaded                 VARCHAR2 (200);
      v_brn_sht_desc                tqc_branches.brn_sht_desc%TYPE;
      v_direct_srl_fmt              tqc_account_types.act_id_serial_format%TYPE;
      v_agent_sht_desc              VARCHAR2 (200);

      CURSOR LOAD
      IS
         SELECT *
           FROM tqc_agencies_temp
          WHERE agnl_code = v_agnl_code;
   BEGIN
      --RAISE_ERROR('v_agnl_code'||v_agnl_code||'v_lms'||v_lms);
      BEGIN
         SELECT agnl_account_type
           INTO v_agnl_account_type
           FROM tqc_agencies_temp
          WHERE agnl_code = v_agnl_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            RAISE;
      END;

      SELECT agnl_loaded
        INTO v_agnl_loaded
        FROM tqc_agencies_temp
       WHERE agnl_code = v_agnl_code;

      IF v_agnl_loaded IS NULL OR v_agnl_loaded != 'L'
      THEN
         BEGIN
--RAISE_ERROR('v_agnl_account_type'||v_agnl_account_type||'v_lms'||v_lms);
--    FOR l IN LOAD
--         LOOP
            BEGIN
               SELECT RTRIM (LTRIM (act_id_serial_format))
                 INTO v_direct_srl_fmt
                 FROM tqc_account_types
                WHERE act_code = v_agnl_account_type;
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error getting Id format for account type ');
                                     --||v_agencies_tab(I).ACT_ACCOUNT_TYPE);
            END;

            SELECT agnl_agn_act_code, agnl_account_type,
                   agnl_agn_acc_name, agnl_agn_physical_address,
                   agnl_agn_postal_address, agnl_agn_twn_name,
                   agnl_agn_reg_code, agnl_agn_contact_person,
                   agnl_agn_tel1, agnl_agn_fax, agnl_agn_email_address,
                   agnl_agn_date_created, agnl_agn_check_date,
                   agnl_agn_branch_name
              INTO v_agnl_agn_act_code, v_agnl_account_type,
                   v_agnl_agn_acc_name, v_agnl_agn_physical_address,
                   v_agnl_agn_postal_address, v_agnl_agn_twn_name,
                   v_agnl_agn_reg_code, v_agnl_agn_contact_person,
                   v_agnl_agn_tel1, v_agnl_agn_fax, v_agnl_agn_email_address,
                   v_agnl_agn_date_created, v_agnl_agn_check_date,
                   v_agnl_agn_branch_name
              FROM tqc_agencies_temp
             WHERE agnl_code = v_agnl_code;

            IF v_agnl_agn_twn_name IS NOT NULL
            THEN
               BEGIN
                  SELECT twn_code
                    INTO v_towncode
                    FROM tqc_towns
                   WHERE twn_name = v_agnl_agn_twn_name;
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     v_towncode := NULL;
                     NULL;
               END;
            END IF;

            BEGIN
               SELECT brn_sht_desc, brn_code
                 INTO v_brn_sht_desc, v_brn_code
                 FROM tqc_branches
                WHERE brn_name = v_agnl_agn_branch_name;
            EXCEPTION
               WHEN OTHERS
               THEN
                  v_towncode := NULL;
                  NULL;
            END;

            BEGIN
               v_agent_sht_desc :=
                  tqc_agencies_pkg.getagentshtdesc (v_agnl_account_type,
                                                    v_brn_sht_desc,
                                                    v_agnl_agn_acc_name,
                                                    v_direct_srl_fmt
                                                   );
            EXCEPTION
               WHEN OTHERS
               THEN
                  raise_error ('Error getting Agent Account ');
            END;

            BEGIN
               INSERT INTO tqc_agencies
                           (agn_code, agn_acc_no,
                            agn_physical_address,
                            agn_postal_address, agn_twn_code,
                            agn_reg_code, agn_contact_person,
                            agn_tel1, agn_fax,
                            agn_email_address,
                            agn_date_created, agn_created_by,
                            agn_check_date, agn_sht_desc, agn_name,
                            agn_brn_code, agn_old_acc_no,
                            agn_act_code
                           )
                    VALUES (tqc_agn_code_seq.NEXTVAL, v_agnl_agn_act_code,
                            v_agnl_agn_physical_address,
                            v_agnl_agn_postal_address, v_towncode,
                            v_agnl_agn_reg_code, v_agnl_agn_contact_person,
                            v_agnl_agn_tel1, v_agnl_agn_fax,
                            v_agnl_agn_email_address,
                            v_agnl_agn_date_created, 'LOADED',
                            SYSDATE, v_agent_sht_desc, v_agnl_agn_acc_name,
                            v_brn_code, v_agnl_agn_act_code,
                            v_agnl_account_type
                           );
            END;

            UPDATE tqc_agencies_temp
               SET agnl_loaded = 'L'
             WHERE agnl_code = v_agnl_code;

            IF v_gis IS NOT NULL
            THEN
               BEGIN
                  INSERT INTO tqc_agency_systems
                              (asys_sys_code, asys_agn_code, asys_wef,
                               asys_wet
                              )
                       VALUES (37, tqc_agn_code_seq.CURRVAL, SYSDATE,
                               NULL
                              );
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('Error asigning AGENTS to GIS..1');
               END;
            END IF;

            IF v_lms IS NOT NULL
            THEN
               BEGIN
                  INSERT INTO tqc_agency_systems
                              (asys_sys_code, asys_agn_code, asys_wef,
                               asys_wet
                              )
                       VALUES (27, tqc_agn_code_seq.CURRVAL, SYSDATE,
                               NULL
                              );
               EXCEPTION
                  WHEN OTHERS
                  THEN
                     raise_error ('Error asigning AGENTS to LMS..1');
               END;
            END IF;
--      END LOOP;
         END;
      ELSIF v_agnl_loaded = 'L'
      THEN
         NULL;
      --RAISE_ERROR('You cannot load an Already loaded Transaction');
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
   END;
END tqc_loading_utilities; 
/