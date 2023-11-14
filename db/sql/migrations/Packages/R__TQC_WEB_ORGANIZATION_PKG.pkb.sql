--
-- TQC_WEB_ORGANIZATION_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.tqc_web_organization_pkg
AS
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

--   PROCEDURE update_organization (
--      v_add_edit            IN       VARCHAR2,
--      v_org_code            IN       tqc_organizations.org_code%TYPE,
--      v_org_sht_desc        IN       tqc_organizations.org_sht_desc%TYPE,
--      v_org_name            IN       tqc_organizations.org_name%TYPE,
--      v_org_addrs           IN       tqc_organizations.org_addrs%TYPE,
--      v_org_twn_code        IN       tqc_organizations.org_twn_code%TYPE,
--      v_org_cou_code        IN       tqc_organizations.org_cou_code%TYPE,
--      v_org_email_addrs     IN       tqc_organizations.org_email_addrs%TYPE,
--      v_org_phy_addrs       IN       tqc_organizations.org_phy_addrs%TYPE,
--      v_org_cur_code        IN       tqc_organizations.org_cur_code%TYPE,
--      v_org_zip             IN       tqc_organizations.org_zip%TYPE,
--      v_org_fax             IN       tqc_organizations.org_fax%TYPE,
--      v_org_tel1            IN       tqc_organizations.org_tel1%TYPE,
--      v_org_tel2            IN       tqc_organizations.org_tel2%TYPE,
--      v_org_rpt_logo        IN       tqc_organizations.org_rpt_logo%TYPE,
--      v_org_motto           IN       tqc_organizations.org_motto%TYPE,
--      v_org_pin_no          IN       tqc_organizations.org_pin_no%TYPE,
--      v_org_ed_code         IN       tqc_organizations.org_ed_code%TYPE,
--      v_org_item_acc_code   IN       tqc_organizations.org_item_acc_code%TYPE,
--      v_org_other_name      IN       tqc_organizations.org_other_name%TYPE,
--      v_org_type            IN       tqc_organizations.org_type%TYPE,
--      v_org_web_brn_code    IN       tqc_organizations.org_web_brn_code%TYPE,
--      v_org_web_addrs       IN       tqc_organizations.org_web_addrs%TYPE,
--      v_org_agn_code        IN       tqc_organizations.org_agn_code%TYPE,
--      v_org_directors       IN       tqc_organizations.org_directors%TYPE,
--      v_org_lang_code       IN       tqc_organizations.org_lang_code%TYPE,
--      v_org_avatar          IN       VARCHAR DEFAULT NULL,
--      v_org_sts_code        IN       tqc_organizations.org_sts_code%TYPE,
--      v_org_grp_logo        IN       tqc_organizations.org_grp_logo%TYPE,
--      v_err                 OUT      VARCHAR2,
--      v_org_b64                      tqc_organizations.org_logo_b64%TYPE
--            DEFAULT NULL,
--      v_vatnumber           IN       tqc_organizations.org_vat_number%TYPE,
--      v_mobile1             IN       tqc_organizations.org_mobile1%TYPE,
--      v_mobile2             IN       tqc_organizations.org_mobile2%TYPE
--   )
--   IS
--      v_rec_count          NUMBER;
--      v_count              NUMBER;
--      v_new_org_rpt_logo   tqc_organizations.org_rpt_logo%TYPE;
--   BEGIN
--      BEGIN
--         SELECT COUNT (*)
--           INTO v_rec_count
--           FROM tqc_organizations
--          WHERE org_code = v_org_code;
--      EXCEPTION
--         WHEN NO_DATA_FOUND
--         THEN
--            NULL;
--         WHEN OTHERS
--         THEN
--            raise_error (   'The Organization code is not correct...'
--                         || SQLERRM (SQLCODE)
--                        );
--      END;

--      IF v_add_edit = 'A' AND v_rec_count = 0
--      THEN
--         BEGIN
--            SELECT COUNT (1)
--              INTO v_count
--              FROM tqc_organizations
--             WHERE org_name = v_org_name;

--            IF v_count > 0
--            THEN
--               raise_error ('Record with ID Exists!');
--               RETURN;
--            END IF;

--            INSERT INTO tqc_organizations
--                        (org_code, org_sht_desc,
--                         org_name, org_addrs, org_twn_code,
--                         org_cou_code, org_email_addrs, org_phy_addrs,
--                         org_cur_code, org_zip, org_fax, org_tel1,
--                         org_tel2, org_rpt_logo, org_motto,
--                         org_pin_no, org_ed_code, org_item_acc_code,
--                         org_other_name, org_type, org_web_brn_code,
--                         org_web_addrs, org_agn_code, org_directors,
--                         org_lang_code,                         --,ORG_AVATAR)
--                                       org_sts_code, org_grp_logo,
--                         org_logo_b64, org_vat_number, org_mobile1,
--                         org_mobile2
--                        )
--                 VALUES (tqc_org_code_seq.NEXTVAL, v_org_sht_desc,
--                         v_org_name, v_org_addrs, v_org_twn_code,
--                         v_org_cou_code, v_org_email_addrs, v_org_phy_addrs,
--                         v_org_cur_code, v_org_zip, v_org_fax, v_org_tel1,
--                         v_org_tel2, v_org_rpt_logo, v_org_motto,
--                         v_org_pin_no, v_org_ed_code, v_org_item_acc_code,
--                         v_org_other_name, v_org_type, v_org_web_brn_code,
--                         v_org_web_addrs, v_org_agn_code, v_org_directors,
--                         v_org_lang_code,                       --v_org_avatar
--                                         v_org_sts_code, v_org_grp_logo,
--                         v_org_b64, v_vatnumber, v_mobile1,
--                         v_mobile2
--                        );
--         EXCEPTION
--            WHEN OTHERS
--            THEN
--               raise_error (   'Error Inserting Organization...'
--                            || SQLERRM (SQLCODE)
--                           );
--               RETURN;
--         END;
--      ELSIF v_add_edit = 'E' AND v_rec_count != 0
--      THEN
--         /*IF v_org_rpt_logo IS NOT NULL THEN
--            v_new_org_rpt_logo := v_org_rpt_logo;
--         ELSE
--            SELECT ORG_RPT_LOGO
--            INTO v_new_org_rpt_logo
--            FROM TQC_ORGANIZATIONS
--            WHERE ORG_CODE=v_org_code;
--         END IF;*/
--         BEGIN
--            IF v_org_rpt_logo IS NULL
--            THEN
--               UPDATE tqc_organizations
--                  SET org_sht_desc = NVL (v_org_sht_desc, org_sht_desc),
--                      org_name = NVL (v_org_name, org_name),
--                      org_addrs = NVL (v_org_addrs, org_addrs),
--                      org_twn_code = NVL (v_org_twn_code, org_twn_code),
--                      org_cou_code = NVL (v_org_cou_code, org_cou_code),
--                      org_email_addrs =
--                                      NVL (v_org_email_addrs, org_email_addrs),
--                      org_phy_addrs = NVL (v_org_phy_addrs, org_phy_addrs),
--                      org_cur_code = NVL (v_org_cur_code, org_cur_code),
--                      org_zip = NVL (v_org_zip, org_zip),
--                      org_fax = NVL (v_org_fax, org_fax),
--                      org_tel1 = NVL (v_org_tel1, org_tel1),
--                      org_tel2 = NVL (v_org_tel2, org_tel2),
--                      org_motto = NVL (v_org_motto, org_motto),
--                      org_pin_no = NVL (v_org_pin_no, org_pin_no),
--                      org_ed_code = NVL (v_org_ed_code, org_ed_code),
--                      org_item_acc_code =
--                                  NVL (v_org_item_acc_code, org_item_acc_code),
--                      org_other_name = NVL (v_org_other_name, org_other_name),
--                      org_type = NVL (v_org_type, org_type),
--                      org_web_brn_code =
--                                    NVL (v_org_web_brn_code, org_web_brn_code),
--                      org_web_addrs = NVL (v_org_web_addrs, org_web_addrs),
--                      org_agn_code = NVL (v_org_agn_code, org_agn_code),
--                      org_directors = NVL (v_org_directors, org_directors),
--                      org_lang_code = NVL (v_org_lang_code, org_lang_code),
--                                                                           --,
--                      --ORG_AVATAR        = NVL(v_org_avatar,ORG_AVATAR)
--                      org_sts_code = NVL (v_org_sts_code, org_sts_code),
--                      org_grp_logo = NVL (v_org_grp_logo, org_grp_logo),
--                      org_logo_b64 = NVL (v_org_b64, org_logo_b64),
--                      org_vat_number = NVL (v_vatnumber, org_vat_number),
--                      org_mobile1 = NVL (v_mobile1, org_mobile1),
--                      org_mobile2 = NVL (v_mobile2, org_mobile2)
--                WHERE org_code = v_org_code;
--            ELSE
--               UPDATE tqc_organizations
--                  SET org_sht_desc = NVL (v_org_sht_desc, org_sht_desc),
--                      org_name = NVL (v_org_name, org_name),
--                      org_addrs = NVL (v_org_addrs, org_addrs),
--                      org_twn_code = NVL (v_org_twn_code, org_twn_code),
--                      org_cou_code = NVL (v_org_cou_code, org_cou_code),
--                      org_email_addrs =
--                                      NVL (v_org_email_addrs, org_email_addrs),
--                      org_phy_addrs = NVL (v_org_phy_addrs, org_phy_addrs),
--                      org_cur_code = NVL (v_org_cur_code, org_cur_code),
--                      org_zip = NVL (v_org_zip, org_zip),
--                      org_fax = NVL (v_org_fax, org_fax),
--                      org_tel1 = NVL (v_org_tel1, org_tel1),
--                      org_tel2 = NVL (v_org_tel2, org_tel2),
--                      org_rpt_logo = v_org_rpt_logo,
--                      org_motto = NVL (v_org_motto, org_motto),
--                      org_pin_no = NVL (v_org_pin_no, org_pin_no),
--                      org_ed_code = NVL (v_org_ed_code, org_ed_code),
--                      org_item_acc_code =
--                                  NVL (v_org_item_acc_code, org_item_acc_code),
--                      org_other_name = NVL (v_org_other_name, org_other_name),
--                      org_type = NVL (v_org_type, org_type),
--                      org_web_brn_code =
--                                    NVL (v_org_web_brn_code, org_web_brn_code),
--                      org_web_addrs = NVL (v_org_web_addrs, org_web_addrs),
--                      org_agn_code = NVL (v_org_agn_code, org_agn_code),
--                      org_directors = NVL (v_org_directors, org_directors),
--                      org_lang_code = NVL (v_org_lang_code, org_lang_code),
--                                                                           --,
--                      --ORG_AVATAR        = NVL(v_org_avatar,ORG_AVATAR)
--                      org_sts_code = NVL (v_org_sts_code, org_sts_code),
--                      org_grp_logo = NVL (v_org_grp_logo, org_grp_logo),
--                      org_logo_b64 = NVL (v_org_b64, org_logo_b64)
--                WHERE org_code = v_org_code;
--            END IF;
--         EXCEPTION
--            WHEN OTHERS
--            THEN
--               raise_error (   'Error Updating Organizations...'
--                            || SQLERRM (SQLCODE)
--                           );
--               RETURN;
--         END;
--      ELSIF v_add_edit = 'D' AND v_rec_count != 0
--      THEN
--         BEGIN
--            DELETE FROM tqc_organizations
--                  WHERE org_code = v_org_code;
--         EXCEPTION
--            WHEN OTHERS
--            THEN
--               raise_error (   'Error Deleting Organization...'
--                            || SQLERRM (SQLCODE)
--                           );
--               RETURN;
--         END;
--      ELSE
--         raise_error ('System Unable To Resolve Organization ID...');
--         RETURN;
--      END IF;
--   EXCEPTION
--      WHEN OTHERS
--      THEN
--         raise_error ('Error Updating Organizations...' || SQLERRM (SQLCODE));
--         RETURN;
--   END update_organization;

 PROCEDURE update_organization (
      v_add_edit            IN       VARCHAR2,
      v_org_code            IN       tqc_organizations.org_code%TYPE,
      v_org_sht_desc        IN       tqc_organizations.org_sht_desc%TYPE,
      v_org_name            IN       tqc_organizations.org_name%TYPE,
      v_org_addrs           IN       tqc_organizations.org_addrs%TYPE,
      v_org_twn_code        IN       tqc_organizations.org_twn_code%TYPE,
      v_org_cou_code        IN       tqc_organizations.org_cou_code%TYPE,
      v_org_email_addrs     IN       tqc_organizations.org_email_addrs%TYPE,
      v_org_phy_addrs       IN       tqc_organizations.org_phy_addrs%TYPE,
      v_org_cur_code        IN       tqc_organizations.org_cur_code%TYPE,
      v_org_zip             IN       tqc_organizations.org_zip%TYPE,
      v_org_fax             IN       tqc_organizations.org_fax%TYPE,
      v_org_tel1            IN       tqc_organizations.org_tel1%TYPE,
      v_org_tel2            IN       tqc_organizations.org_tel2%TYPE,
      v_org_rpt_logo        IN       tqc_organizations.org_rpt_logo%TYPE,
      v_org_motto           IN       tqc_organizations.org_motto%TYPE,
      v_org_pin_no          IN       tqc_organizations.org_pin_no%TYPE,
      v_org_ed_code         IN       tqc_organizations.org_ed_code%TYPE,
      v_org_item_acc_code   IN       tqc_organizations.org_item_acc_code%TYPE,
      v_org_other_name      IN       tqc_organizations.org_other_name%TYPE,
      v_org_type            IN       tqc_organizations.org_type%TYPE,
      v_org_web_brn_code    IN       tqc_organizations.org_web_brn_code%TYPE,
      v_org_web_addrs       IN       tqc_organizations.org_web_addrs%TYPE,
      v_org_agn_code        IN       tqc_organizations.org_agn_code%TYPE,
      v_org_directors       IN       tqc_organizations.org_directors%TYPE,
      v_org_lang_code       IN       tqc_organizations.org_lang_code%TYPE,
      v_org_avatar          IN       VARCHAR DEFAULT NULL,
      v_org_sts_code        IN       tqc_organizations.org_sts_code%TYPE,
      v_org_grp_logo        IN       tqc_organizations.org_grp_logo%TYPE,
      v_err                 OUT      VARCHAR2,
      v_org_b64                      tqc_organizations.org_logo_b64%TYPE  DEFAULT NULL,
      v_vatnumber           IN       tqc_organizations.org_vat_number%TYPE,
      v_mobile1             IN       tqc_organizations.org_mobile1%TYPE,
      v_mobile2             IN       tqc_organizations.org_mobile2%TYPE,
      v_cert_sign           IN       tqc_organizations.org_cert_sign%TYPE, 
      v_org_bank_account_name    IN       tqc_organizations.org_bank_account_name%TYPE,
      v_org_bank_account_no      IN       tqc_organizations.org_bank_account_no%TYPE,
      v_org_swift_code           IN       tqc_organizations.org_swift_code%TYPE,
      v_org_bnk_code             IN       tqc_organizations.org_bnk_code%TYPE,
      v_org_bbr_code             IN       tqc_organizations.org_bbr_code%TYPE
   )
   IS
      v_rec_count          NUMBER;
      v_count              NUMBER;
      v_new_org_rpt_logo   tqc_organizations.org_rpt_logo%TYPE;
   BEGIN
--     raise_error(
--        'v_org_bank_account_name: '||v_org_bank_account_name||
--        'v_org_bank_account_no: '||v_org_bank_account_no|| 
--        'v_org_swift_code: '||v_org_swift_code ||
--        'v_org_bnk_code:'||v_org_bnk_code||         
--        'v_org_bbr_code:'||v_org_bbr_code   
--     );
      BEGIN
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_organizations
          WHERE org_code = v_org_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error (   'The Organization code is not correct...'
                         || SQLERRM (SQLCODE)
                        );
      END;

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_organizations
             WHERE org_name = v_org_name;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;

            INSERT INTO tqc_organizations
                        (org_code, org_sht_desc,
                        org_name, org_addrs, org_twn_code,
                        org_cou_code, org_email_addrs, org_phy_addrs,
                        org_cur_code, org_zip, org_fax, org_tel1,
                        org_tel2, org_rpt_logo, org_motto,
                        org_pin_no, org_ed_code, org_item_acc_code,
                        org_other_name, org_type, org_web_brn_code,
                        org_web_addrs, org_agn_code, org_directors,
                        org_lang_code,                         --,ORG_AVATAR)
                        org_sts_code, org_grp_logo,
                        org_logo_b64, org_vat_number, org_mobile1,
                        org_mobile2,org_cert_sign,
                        org_bank_account_name ,
                        org_bank_account_no ,    
                        org_swift_code   ,       
                        org_bnk_code ,           
                        org_bbr_code   
                        )
                 VALUES (tqc_org_code_seq.NEXTVAL, v_org_sht_desc,
                         v_org_name, v_org_addrs, v_org_twn_code,
                         v_org_cou_code, v_org_email_addrs, v_org_phy_addrs,
                         v_org_cur_code, v_org_zip, v_org_fax, v_org_tel1,
                         v_org_tel2, v_org_rpt_logo, v_org_motto,
                         v_org_pin_no, v_org_ed_code, v_org_item_acc_code,
                         v_org_other_name, v_org_type, v_org_web_brn_code,
                         v_org_web_addrs, v_org_agn_code, v_org_directors,
                         v_org_lang_code,                       --v_org_avatar
                                         v_org_sts_code, v_org_grp_logo,
                         v_org_b64, v_vatnumber, v_mobile1,
                         v_mobile2,v_cert_sign,
                         v_org_bank_account_name ,
                        v_org_bank_account_no ,    
                        v_org_swift_code   ,       
                        v_org_bnk_code ,           
                        v_org_bbr_code         
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Inserting Organization...'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_rec_count != 0
      THEN
         /*IF v_org_rpt_logo IS NOT NULL THEN
            v_new_org_rpt_logo := v_org_rpt_logo;
         ELSE
            SELECT ORG_RPT_LOGO
            INTO v_new_org_rpt_logo
            FROM TQC_ORGANIZATIONS
            WHERE ORG_CODE=v_org_code;
         END IF;*/
         BEGIN
            IF v_org_rpt_logo IS NULL
            THEN
               UPDATE tqc_organizations
                  SET org_sht_desc = NVL (v_org_sht_desc, org_sht_desc),
                      org_name = NVL (v_org_name, org_name),
                      org_addrs = NVL (v_org_addrs, org_addrs),
                      org_twn_code = NVL (v_org_twn_code, org_twn_code),
                      org_cou_code = NVL (v_org_cou_code, org_cou_code),
                      org_email_addrs =
                                      NVL (v_org_email_addrs, org_email_addrs),
                      org_phy_addrs = NVL (v_org_phy_addrs, org_phy_addrs),
                      org_cur_code = NVL (v_org_cur_code, org_cur_code),
                      org_zip = NVL (v_org_zip, org_zip),
                      org_fax = NVL (v_org_fax, org_fax),
                      org_tel1 = NVL (v_org_tel1, org_tel1),
                      org_tel2 = NVL (v_org_tel2, org_tel2),
                      org_motto = NVL (v_org_motto, org_motto),
                      org_pin_no = NVL (v_org_pin_no, org_pin_no),
                      org_ed_code = NVL (v_org_ed_code, org_ed_code),
                      org_item_acc_code =
                                  NVL (v_org_item_acc_code, org_item_acc_code),
                      org_other_name = NVL (v_org_other_name, org_other_name),
                      org_type = NVL (v_org_type, org_type),
                      org_web_brn_code =
                                    NVL (v_org_web_brn_code, org_web_brn_code),
                      org_web_addrs = NVL (v_org_web_addrs, org_web_addrs),
                      org_agn_code = NVL (v_org_agn_code, org_agn_code),
                      org_directors = NVL (v_org_directors, org_directors),
                      org_lang_code = NVL (v_org_lang_code, org_lang_code),
                                                                           --,
                      --ORG_AVATAR        = NVL(v_org_avatar,ORG_AVATAR)
                      org_sts_code = NVL (v_org_sts_code, org_sts_code),
                      org_grp_logo = NVL (v_org_grp_logo, org_grp_logo),
                      org_logo_b64 = NVL (v_org_b64, org_logo_b64),
                      org_vat_number = NVL (v_vatnumber, org_vat_number),
                      org_mobile1 = NVL (v_mobile1, org_mobile1),
                      org_mobile2 = NVL (v_mobile2, org_mobile2),
                      org_cert_sign=NVL (v_cert_sign, org_cert_sign),
                      org_bank_account_name = NVL (v_org_bank_account_name, v_org_bank_account_name),
                      org_bank_account_no = NVL (v_org_bank_account_no, v_org_bank_account_no),  
                      org_swift_code = NVL (v_org_swift_code, v_org_swift_code),
                      org_bnk_code = NVL (v_org_bnk_code, v_org_bnk_code),   
                      org_bbr_code = NVL (v_org_bbr_code, v_org_bbr_code)         
                WHERE org_code = v_org_code;
            ELSE
               UPDATE tqc_organizations
                  SET org_sht_desc = NVL (v_org_sht_desc, org_sht_desc),
                      org_name = NVL (v_org_name, org_name),
                      org_addrs = NVL (v_org_addrs, org_addrs),
                      org_twn_code = NVL (v_org_twn_code, org_twn_code),
                      org_cou_code = NVL (v_org_cou_code, org_cou_code),
                      org_email_addrs =
                                      NVL (v_org_email_addrs, org_email_addrs),
                      org_phy_addrs = NVL (v_org_phy_addrs, org_phy_addrs),
                      org_cur_code = NVL (v_org_cur_code, org_cur_code),
                      org_zip = NVL (v_org_zip, org_zip),
                      org_fax = NVL (v_org_fax, org_fax),
                      org_tel1 = NVL (v_org_tel1, org_tel1),
                      org_tel2 = NVL (v_org_tel2, org_tel2),
                      org_rpt_logo = v_org_rpt_logo,
                      org_motto = NVL (v_org_motto, org_motto),
                      org_pin_no = NVL (v_org_pin_no, org_pin_no),
                      org_ed_code = NVL (v_org_ed_code, org_ed_code),
                      org_item_acc_code =
                                  NVL (v_org_item_acc_code, org_item_acc_code),
                      org_other_name = NVL (v_org_other_name, org_other_name),
                      org_type = NVL (v_org_type, org_type),
                      org_web_brn_code =
                                    NVL (v_org_web_brn_code, org_web_brn_code),
                      org_web_addrs = NVL (v_org_web_addrs, org_web_addrs),
                      org_agn_code = NVL (v_org_agn_code, org_agn_code),
                      org_directors = NVL (v_org_directors, org_directors),
                      org_lang_code = NVL (v_org_lang_code, org_lang_code),
                                                                           --,
                      --ORG_AVATAR        = NVL(v_org_avatar,ORG_AVATAR)
                      org_sts_code = NVL (v_org_sts_code, org_sts_code),
                      org_grp_logo = NVL (v_org_grp_logo, org_grp_logo),
                      org_logo_b64 = NVL (v_org_b64, org_logo_b64),
                      org_cert_sign=v_cert_sign
                WHERE org_code = v_org_code;
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Updating Organizations...'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'D' AND v_rec_count != 0
      THEN
         BEGIN
            DELETE FROM tqc_organizations
                  WHERE org_code = v_org_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Deleting Organization...'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSE
         raise_error ('System Unable To Resolve Organization ID...');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating Organizations...' || SQLERRM (SQLCODE));
         RETURN;
   END update_organization;
   
   PROCEDURE update_regions (
      v_add_edit                  IN       VARCHAR2,
      v_reg_code                  IN       tqc_regions.reg_code%TYPE,
      v_reg_org_code              IN       tqc_regions.reg_org_code%TYPE,
      v_reg_sht_desc              IN       tqc_regions.reg_sht_desc%TYPE,
      v_reg_name                  IN       tqc_regions.reg_name%TYPE,
      v_reg_wef                   IN       tqc_regions.reg_wef%TYPE,
      v_reg_wet                   IN       tqc_regions.reg_wet%TYPE,
      v_reg_agn_code              IN       tqc_regions.reg_agn_code%TYPE,
      v_reg_post_level            IN       tqc_regions.reg_post_level%TYPE,
      v_reg_mngr_allowed          IN       tqc_regions.reg_mngr_allowed%TYPE,
      v_reg_overide_comm_earned   IN       tqc_regions.reg_overide_comm_earned%TYPE,
      v_reg_brn_mngr_seq_no       IN       tqc_regions.reg_brn_mngr_seq_no%TYPE,
      v_reg_agn_seq_no            IN       tqc_regions.reg_agn_seq_no%TYPE,
      v_err                       OUT      VARCHAR2
   )
   IS
      v_rec_count   NUMBER;
      v_count       NUMBER;
      v_regshtdesc    VARCHAR2(20) :=  v_reg_sht_desc;
   BEGIN
--RAISE_ERROR('v_reg_code '||v_reg_code||'v_reg_wet '|| v_reg_wet );
      BEGIN
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_regions
          WHERE reg_code = v_reg_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            raise_error (   'The Region Code is not Correct..'
                         || SQLERRM (SQLCODE)
                        );
            RETURN;
      END;

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_regions
             WHERE reg_sht_desc = v_reg_sht_desc;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;

            INSERT INTO tqc_regions
                        (reg_code, reg_org_code,
                         reg_sht_desc, reg_name, reg_wef, reg_wet,
                         reg_agn_code, reg_post_level,
                         reg_mngr_allowed, reg_overide_comm_earned,
                         reg_brn_mngr_seq_no, reg_agn_seq_no
                        )
                 VALUES (tqc_reg_code_seq.NEXTVAL, v_reg_org_code,
                         v_reg_sht_desc, v_reg_name, v_reg_wef, v_reg_wet,
                         v_reg_agn_code, v_reg_post_level,
                         v_reg_mngr_allowed, v_reg_overide_comm_earned,
                         v_reg_brn_mngr_seq_no, v_reg_agn_seq_no
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Inserting  '
                            || v_reg_name
                            || '  Region Details'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_rec_count != 0
      THEN
         BEGIN
         
        IF v_reg_agn_code IS NULL THEN
                v_regshtdesc := v_reg_sht_desc;
        ELSE
            SELECT AGN_SHT_DESC INTO v_regshtdesc FROM TQC_AGENCIES
            WHERE AGN_CODE = v_reg_agn_code;
        END IF;
        
            UPDATE tqc_regions
               SET reg_org_code = NVL (v_reg_org_code, reg_org_code),
                   reg_sht_desc = NVL (v_regshtdesc, reg_sht_desc),
                   reg_name = NVL (v_reg_name, reg_name),
                   reg_wef = NVL (v_reg_wef, reg_wef),
                   reg_wet = NVL (v_reg_wet, reg_wet),
                   reg_agn_code =v_reg_agn_code,
                   reg_post_level = NVL (v_reg_post_level, reg_post_level),
                   reg_mngr_allowed =
                                    NVL (v_reg_mngr_allowed, reg_mngr_allowed),
                   reg_overide_comm_earned =
                      NVL (v_reg_overide_comm_earned, reg_overide_comm_earned),
                   reg_brn_mngr_seq_no = v_reg_brn_mngr_seq_no,
                   reg_agn_seq_no = v_reg_agn_seq_no
             WHERE reg_code = v_reg_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Updating '
                            || v_reg_name
                            || ' Region'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSE
         raise_error ('System Unable to Resolve Region ID ');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating Regions...' || SQLERRM (SQLCODE));
   END update_regions;
   
   
   PROCEDURE bank_region_prc (
      v_add_edit                     VARCHAR2,
      v_bnkr_code                     tqc_bank_regions.bnkr_code%type,
      v_bnkr_org_code                 tqc_bank_regions.bnkr_org_code%type,
      v_bnkr_sht_desc                 tqc_bank_regions.bnkr_sht_desc%type,
      v_bnkr_name                     tqc_bank_regions.bnkr_name%type,
      v_bnkr_wef                      tqc_bank_regions.bnkr_wef%type,
      v_bnkr_wet                      tqc_bank_regions.bnkr_wet%type,
      v_bnkr_reg_code                 tqc_bank_regions.bnkr_reg_code%type,
      v_bnkr_agn_code                 tqc_bank_regions.bnkr_agn_code%type,
      v_err                       OUT      VARCHAR2
   )
   IS
      v_cnt      NUMBER;
   BEGIN
--    RAISE_ERROR(
--      'v_add_edit: '||v_add_edit||                    
--      'v_bnkr_code: '||v_bnkr_code||                    
--      'v_bnkr_org_code: '||v_bnkr_org_code||                
--      'v_bnkr_sht_desc: '||v_bnkr_sht_desc ||             
--      'v_bnkr_name: '||v_bnkr_name||                 
--      'v_bnkr_wef: '||v_bnkr_wef||                 
--      'v_bnkr_wet: '||v_bnkr_wet||                    
--      'v_bnkr_reg_code: '||v_bnkr_reg_code||                
--      'v_bnkr_agn_code: '||v_bnkr_agn_code                
--    );
      BEGIN
         SELECT COUNT (*)
           INTO v_cnt
           FROM tqc_bank_regions
          WHERE  bnkr_code = v_bnkr_code;
      EXCEPTION
         WHEN OTHERS
         THEN
            v_cnt:=0;         
      END;

      IF v_add_edit = 'A' AND v_cnt = 0
      THEN
         BEGIN
           BEGIN
            SELECT COUNT (1)
              INTO v_cnt
              FROM tqc_bank_regions
             WHERE bnkr_sht_desc = v_bnkr_sht_desc;
            EXCEPTION WHEN OTHERS THEN
                v_cnt:=0;         
            END;

            IF v_cnt > 0
            THEN
               v_err:= 'Bank Region Record with this short desc Exists!';
               RETURN;
            END IF;

            INSERT INTO tqc_bank_regions
                        (
                         bnkr_code,bnkr_org_code,bnkr_sht_desc,bnkr_name,bnkr_wef,bnkr_wet, bnkr_reg_code,bnkr_agn_code
                        )
              VALUES (
						 tqc_bnkr_code_seq.nextval,v_bnkr_org_code,v_bnkr_sht_desc,v_bnkr_name,v_bnkr_wef,v_bnkr_wet, v_bnkr_reg_code,v_bnkr_agn_code
                        );
         EXCEPTION WHEN OTHERS THEN
               v_err:=   'Error Inserting Bank  Region Details' || SQLERRM (SQLCODE);            
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_cnt > 0
      THEN
         BEGIN
            UPDATE tqc_bank_regions
               SET 
               bnkr_org_code=nvl(v_bnkr_org_code,bnkr_org_code),
               bnkr_sht_desc=nvl(v_bnkr_sht_desc,bnkr_sht_desc),
               bnkr_name=nvl(v_bnkr_name,bnkr_name),
               bnkr_wef=nvl(v_bnkr_wef,bnkr_wef),
               bnkr_wet=nvl(v_bnkr_wet,bnkr_wet), 
               bnkr_reg_code=nvl(v_bnkr_reg_code,bnkr_reg_code),
               bnkr_agn_code=nvl(v_bnkr_agn_code,bnkr_agn_code)
             WHERE bnkr_code = v_bnkr_code;
         EXCEPTION WHEN OTHERS THEN
            v_err:= 'Error Updating '|| v_bnkr_name|| ' Bank Region'|| SQLERRM (SQLCODE);
            RETURN;
         END;
      ELSIF v_add_edit = 'D' AND v_cnt > 0
      THEN
         BEGIN
            DELETE tqc_bank_regions
               WHERE bnkr_code = v_bnkr_code;
         EXCEPTION WHEN OTHERS THEN
            v_err:= 'Error Deleting '|| v_bnkr_name|| ' Bank Region'|| SQLERRM (SQLCODE);
            RETURN;
         END;
      ELSE
         raise_error ('System Unable to Resolve Bank Region Parameters!');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating Bank Regions...' || SQLERRM (SQLCODE));
   END ;
   

   PROCEDURE delete_region (
      v_reg_code         tqc_regions.reg_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   )
   IS
   BEGIN
      IF v_reg_code IS NULL
      THEN
         raise_error ('Pass the primary key for organization region record.');
         RETURN;
      ELSE
         BEGIN
            DELETE      tqc_regions
                  WHERE reg_code = v_reg_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error deleting organization region record.');
               RETURN;
         END;
      END IF;
   END delete_region;

   PROCEDURE update_branches (
      v_add_edit                  IN       VARCHAR2,
      v_brn_code                  IN       tqc_branches.brn_code%TYPE,
      v_brn_sht_desc              IN       tqc_branches.brn_sht_desc%TYPE,
      v_brn_reg_code              IN       tqc_branches.brn_reg_code%TYPE,
      v_brn_name                  IN       tqc_branches.brn_name%TYPE,
      v_brn_phy_addrs             IN       tqc_branches.brn_phy_addrs%TYPE,
      v_brn_email_addrs           IN       tqc_branches.brn_email_addrs%TYPE,
      v_brn_post_addrs            IN       tqc_branches.brn_post_addrs%TYPE,
      v_brn_twn_code              IN       tqc_branches.brn_twn_code%TYPE,
      v_brn_cou_code              IN       tqc_branches.brn_cou_code%TYPE,
      v_brn_contact               IN       tqc_branches.brn_contact%TYPE,
      v_brn_manager               IN       tqc_branches.brn_manager%TYPE,
      v_brn_tel                   IN       tqc_branches.brn_tel%TYPE,
      v_brn_fax                   IN       tqc_branches.brn_fax%TYPE,
      v_brn_agn_code              IN       tqc_branches.brn_agn_code%TYPE,
      v_brn_post_level            IN       tqc_branches.brn_post_level%TYPE,
      v_err                       OUT      VARCHAR2,
      v_brn_bra_mngr_seq_no       IN       tqc_branches.brn_bra_mngr_seq_no%TYPE,
      v_brn_agn_seq_no            IN       tqc_branches.brn_agn_seq_no%TYPE,
      v_brn_bns_code                       NUMBER,
      v_brn_mngr_allowed          IN       tqc_branches.brn_mngr_allowed%TYPE,
      v_brn_overide_comm_earned   IN       tqc_branches.brn_overide_comm_earned%TYPE,
      v_brn_pol_prefix            IN       tqc_branches.brn_pol_prefix%TYPE
            DEFAULT NULL,
      v_brn_pol_seq               IN       tqc_branches.brn_pol_seq%TYPE
            DEFAULT NULL,
      v_brn_prop_seq              IN       tqc_branches.brn_prop_seq%TYPE
            DEFAULT NULL,
      v_brn_ref                   IN       tqc_branches.brn_ref%TYPE
            DEFAULT NULL
   )
   IS
      v_rec_count   NUMBER;
      v_count       NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_branches
          WHERE brn_code = v_brn_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error ('The Branch Code Is Wrong...' || SQLERRM (SQLCODE));
            RETURN;
      END;

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_branches
             WHERE brn_sht_desc = v_brn_sht_desc
               AND brn_reg_code = v_brn_reg_code;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;

            /*SELECT NVL(MAX(BNS_CODE) + 1,1) INTO
                               v_brn_bns_code
                               FROM TQC_BRANCH_NAMES; */
            INSERT INTO tqc_branches
                        (brn_code, brn_sht_desc,
                         brn_reg_code, brn_name, brn_phy_addrs,
                         brn_email_addrs, brn_post_addrs, brn_twn_code,
                         brn_cou_code, brn_contact, brn_manager,
                         brn_tel, brn_fax, brn_agn_code,
                         brn_post_level, brn_bns_code,
                         brn_bra_mngr_seq_no, brn_agn_seq_no,
                         brn_pol_prefix,
                         brn_pol_seq, brn_prop_seq,brn_ref
                        )
                 VALUES (tqc_brn_code_seq.NEXTVAL, v_brn_sht_desc,
                         v_brn_reg_code, v_brn_name, v_brn_phy_addrs,
                         v_brn_email_addrs, v_brn_post_addrs, v_brn_twn_code,
                         v_brn_cou_code, v_brn_contact, v_brn_manager,
                         v_brn_tel, v_brn_fax, v_brn_agn_code,
                         v_brn_post_level, v_brn_bns_code,
                         v_brn_bra_mngr_seq_no, v_brn_agn_seq_no,
                         NVL (v_brn_pol_prefix, v_brn_sht_desc),
                         v_brn_pol_seq, v_brn_prop_seq,v_brn_ref
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Inserting '
                            || v_brn_name
                            || ' Branch Details..'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_rec_count != 0
      THEN
         BEGIN
            UPDATE tqc_branches
               SET brn_sht_desc = NVL (v_brn_sht_desc, brn_sht_desc),
                   brn_reg_code = NVL (v_brn_reg_code, brn_reg_code),
                   brn_name = NVL (v_brn_name, brn_name),
                   brn_phy_addrs = NVL (v_brn_phy_addrs, brn_phy_addrs),
                   brn_email_addrs = NVL (v_brn_email_addrs, brn_email_addrs),
                   brn_post_addrs = NVL (v_brn_post_addrs, brn_post_addrs),
                   brn_twn_code = NVL (v_brn_twn_code, brn_twn_code),
                   brn_cou_code = NVL (v_brn_cou_code, brn_cou_code),
                   brn_contact = NVL (v_brn_contact, brn_contact),
                   brn_manager = NVL (v_brn_manager, brn_manager),
                   brn_tel = NVL (v_brn_tel, brn_tel),
                   brn_fax = NVL (v_brn_fax, brn_fax),
                   brn_agn_code = NVL (v_brn_agn_code, brn_agn_code),
                   brn_post_level = NVL (v_brn_post_level, brn_post_level),
                   brn_bra_mngr_seq_no = v_brn_bra_mngr_seq_no,
                   brn_agn_seq_no = v_brn_agn_seq_no,
                   brn_pol_prefix = NVL (v_brn_pol_prefix, v_brn_sht_desc),
                   brn_pol_seq = v_brn_pol_seq,
                   brn_prop_seq = v_brn_prop_seq,
                   brn_ref = v_brn_ref
             WHERE brn_code = v_brn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Updating '
                            || v_brn_name
                            || ' Branch Details..'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSE
         raise_error ('System Unable to Resolve Branch ID...');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating_Branches...' || SQLERRM (SQLCODE));
         RETURN;
   END update_branches;

   PROCEDURE delete_branch (
      v_brn_code         tqc_branches.brn_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   )
   IS
   BEGIN
      IF v_brn_code IS NULL
      THEN
         raise_error ('Pass the primary key for branch record.');
         RETURN;
      ELSE
         BEGIN
            DELETE      tqc_branches
                  WHERE brn_code = v_brn_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error deleting branch record.');
               RETURN;
         END;
      END IF;
   END delete_branch;

   PROCEDURE update_branch_agencies (
      v_add_edit                  IN       VARCHAR2,
      v_bra_code                  IN OUT   tqc_branch_agencies.bra_code%TYPE,
      v_bra_brn_code              IN       tqc_branch_agencies.bra_brn_code%TYPE,
      v_bra_sht_desc              IN       tqc_branch_agencies.bra_sht_desc%TYPE,
      v_bra_name                  IN       tqc_branch_agencies.bra_name%TYPE,
      v_bra_status                IN       tqc_branch_agencies.bra_status%TYPE,
      v_bra_manager               IN       tqc_branch_agencies.bra_manager%TYPE,
      v_bra_agn_code              IN       tqc_branch_agencies.bra_agn_code%TYPE,
      v_bra_post_level            IN       tqc_branch_agencies.bra_post_level%TYPE,
      v_bra_bru_mngr_seq_no       IN       tqc_branch_agencies.bra_bru_mngr_seq_no%TYPE,
      v_bra_agn_seq_no            IN       tqc_branch_agencies.bra_agn_seq_no%TYPE,
      v_bra_mngr_allowed          IN       tqc_branch_agencies.bra_mngr_allowed%TYPE,
      v_bra_overide_comm_earned   IN       tqc_branch_agencies.bra_overide_comm_earned%TYPE,
      v_bra_sht_descpref          IN       VARCHAR2,
      v_brncombuss                IN       VARCHAR2,
      v_bra_pol_seq               IN       tqc_branch_agencies.bra_pol_seq%TYPE,
      v_bra_prop_seq              IN       tqc_branch_agencies.bra_prop_seq%TYPE,
      v_err                       OUT      VARCHAR2
   )
   IS
      v_rec_count   NUMBER;
      v_count       NUMBER;
      v_brashtdesc    VARCHAR2(20) := v_bra_sht_desc;
   BEGIN
      BEGIN
      
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_branch_agencies
          WHERE bra_code = v_bra_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error (   'The Branch_Agency Code Is Wrong...'
                         || SQLERRM (SQLCODE)
                        );
            RETURN;
      END;

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            SELECT tqc_bra_code_seq.NEXTVAL
              INTO v_bra_code
              FROM DUAL;

            BEGIN
               SELECT COUNT (*)
                 INTO v_count
                 FROM tqc_branch_agencies
                WHERE bra_sht_desc = TO_CHAR (v_bra_code);
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
            END;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;

            /* SELECT COUNT(1) INTO v_count
             FROM TQC_BRANCH_AGENCIES
             WHERE BRA_SHT_DESC = v_BRA_SHT_DESC;

             IF v_count > 0 THEN
                 RAISE_ERROR('Record with ID Exists!');
                 RETURN;
             END IF;
             SELECT tqc_bra_code_seq.nextval INTO v_bra_code FROM DUAL;*/
            INSERT INTO tqc_branch_agencies
                        (bra_code, bra_brn_code, bra_sht_desc, bra_name,
                         bra_status, bra_manager, bra_agn_code,
                         bra_post_level, bra_bru_mngr_seq_no,
                         bra_agn_seq_no, bra_pol_seq, bra_prop_seq,
                         bra_mngr_allowed,bra_unt_sht_desc_prefix,bra_compt_ov_on_own_buss
                        )
                 VALUES (v_bra_code, v_bra_brn_code, v_bra_code, v_bra_name,
                         v_bra_status, v_bra_manager, v_bra_agn_code,
                         v_bra_post_level, v_bra_bru_mngr_seq_no,
                         v_bra_agn_seq_no, v_bra_pol_seq, v_bra_prop_seq,v_bra_mngr_allowed,
                         v_bra_sht_descpref,v_brncombuss
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Inserting '
                            || v_bra_name
                            || ' Branch_Agency Details..'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      END IF;
      IF v_add_edit = 'E' AND v_rec_count != 0
      THEN
         BEGIN
            --RAISE_ERROR('ERROR '||v_bra_brn_code||' v_bra_code '||v_bra_code||' v_bra_agn_code '||v_bra_agn_code);
            IF v_bra_agn_code IS NULL THEN
                v_brashtdesc := v_bra_code;
            ELSE
                SELECT AGN_SHT_DESC INTO v_brashtdesc FROM TQC_AGENCIES
                WHERE AGN_CODE = v_bra_agn_code;
            END IF;
            UPDATE tqc_branch_agencies
               SET bra_brn_code = NVL (v_bra_brn_code, bra_brn_code),
                   bra_sht_desc = NVL (v_brashtdesc, bra_sht_desc),
                   bra_name = NVL (v_bra_name, bra_name),
                   bra_status = NVL (v_bra_status, bra_status),
                   bra_manager = NVL (v_bra_manager, bra_manager),
                   bra_agn_code = v_bra_agn_code,
                   bra_post_level = NVL (v_bra_post_level, bra_post_level),
                   bra_bru_mngr_seq_no = v_bra_bru_mngr_seq_no,
                   bra_agn_seq_no = v_bra_agn_seq_no,
                   bra_pol_seq = v_bra_pol_seq,
                   bra_prop_seq = v_bra_prop_seq,
                   bra_mngr_allowed = v_bra_mngr_allowed,
                   bra_unt_sht_desc_prefix=v_bra_sht_descpref,
                   bra_compt_ov_on_own_buss=v_brncombuss
             WHERE bra_code = v_bra_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Updating '
                            || v_bra_name
                            || ' Branch_Agency Details..'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error Updating '
                      || v_bra_name
                      || ' Branch_Agency Details..'
                      || SQLERRM (SQLCODE)
                     );
         RETURN;
   END;

   PROCEDURE delete_branch_agency (
      v_bra_code         tqc_branch_agencies.bra_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   )
   IS
   BEGIN
      IF v_bra_code IS NULL
      THEN
         raise_error ('Pass the primary key for branch agency record.');
         RETURN;
      ELSE
         BEGIN
            DELETE      tqc_branch_agencies
                  WHERE bra_code = v_bra_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error deleting branch agency record.');
               RETURN;
         END;
      END IF;
   END delete_branch_agency;

   PROCEDURE update_units (
      v_add_edit                   IN       VARCHAR2,
      v_bru_code                   IN OUT   tqc_branch_units.bru_code%TYPE,
      v_bru_brn_code               IN       tqc_branch_units.bru_brn_code%TYPE,
      v_bru_sht_desc               IN       tqc_branch_units.bru_sht_desc%TYPE,
      v_bru_name                   IN       tqc_branch_units.bru_name%TYPE,
      v_bru_supervisor             IN       tqc_branch_units.bru_supervisor%TYPE,
      v_bru_status                 IN       tqc_branch_units.bru_status%TYPE,
      v_bru_agn_code               IN       tqc_branch_units.bru_agn_code%TYPE,
      v_bru_bra_code               IN       tqc_branch_units.bru_bra_code%TYPE,
      v_bru_manager                IN       tqc_branch_units.bru_manager%TYPE,
      v_bru_post_level             IN       tqc_branch_units.bru_post_level%TYPE,
      v_bru_agn_seq_no             IN       tqc_branch_units.bru_agn_seq_no%TYPE,
      v_bru_mngr_allowed           IN       tqc_branch_units.bru_mngr_allowed%TYPE
            DEFAULT NULL,
      v_bru_overide_comm_earned    IN       tqc_branch_units.bru_overide_comm_earned%TYPE
            DEFAULT NULL,
      v_bru_unt_sht_desc_prefix    IN       tqc_branch_units.bru_unt_sht_desc_prefix%TYPE,
      v_bru_compt_ov_on_own_buss   IN       tqc_branch_units.bru_compt_ov_on_own_buss%TYPE,
      v_bru_pol_seq                IN       tqc_branch_units.bru_pol_seq%TYPE,
      v_bru_prop_seq               IN       tqc_branch_units.bru_prop_seq%TYPE,
      v_err                        OUT      VARCHAR2
   )
   IS
      v_rec_count   NUMBER;
      v_count       NUMBER;
      v_unitShtDesc VARCHAR2(20);
      v_brushtdesc    VARCHAR2(20) := v_bru_sht_desc;
   BEGIN
   --RAISE_ERROR('dsdsd '||v_bru_bra_code);
      BEGIN
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_branch_units
          WHERE bru_code = v_bru_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error ('The Unit Code Is Wrong...' || SQLERRM (SQLCODE));
            RETURN;
      END;
      
      
      IF v_bru_sht_desc IS NULL THEN
         
         BEGIN
            SELECT SUBSTR(brn_sht_desc,0,9)||v_bru_code
            INTO v_unitShtDesc
            FROM tqc_branches
            WHERE  brn_code = v_bru_brn_code;
         EXCEPTION WHEN OTHERS THEN
            NULL;
         END;
            
         ELSE
            v_unitShtDesc := v_bru_sht_desc;
       END IF;
      

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            SELECT tqc_bru_code_seq.NEXTVAL
              INTO v_bru_code
              FROM DUAL;

            BEGIN
               SELECT COUNT (1)
                 INTO v_count
                 FROM tqc_branch_units
                WHERE bru_sht_desc = TO_CHAR (v_bru_code);
            EXCEPTION
               WHEN NO_DATA_FOUND
               THEN
                  NULL;
            END;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;
            

            /*
            SELECT COUNT(1) INTO v_count
            FROM TQC_BRANCH_UNITS
            WHERE BRU_SHT_DESC = v_bru_sht_desc;

            IF v_count > 0 THEN
                RAISE_ERROR('Record with ID Exists!');
                RETURN;
            END IF;
            SELECT tqc_bru_code_seq.nextval INTO v_bru_code FROM DUAL;*/
            INSERT INTO tqc_branch_units
                        (bru_code, bru_brn_code, bru_sht_desc, bru_name,
                         bru_supervisor, bru_status, bru_agn_code,
                         bru_bra_code, bru_manager, bru_post_level,
                         bru_agn_seq_no, bru_mngr_allowed,
                         bru_overide_comm_earned,
                         bru_unt_sht_desc_prefix,
                         bru_compt_ov_on_own_buss, bru_pol_seq,
                         bru_prop_seq
                        )
                 VALUES (v_bru_code, v_bru_brn_code, v_unitShtDesc, v_bru_name,
                         v_bru_supervisor, v_bru_status, v_bru_agn_code,
                         v_bru_bra_code, v_bru_manager, v_bru_post_level,
                         v_bru_agn_seq_no, v_bru_mngr_allowed,
                         v_bru_overide_comm_earned,
                         v_bru_unt_sht_desc_prefix,
                         v_bru_compt_ov_on_own_buss, v_bru_pol_seq,
                         v_bru_prop_seq
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Inserting '
                            || v_bru_name
                            || ' Branch_Unit Details'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_rec_count != 0
      THEN
      
         
            
         BEGIN
            IF v_bru_agn_code IS NULL THEN
                v_brushtdesc := v_unitShtDesc;
            ELSE
                SELECT AGN_SHT_DESC INTO v_brushtdesc FROM TQC_AGENCIES
                WHERE AGN_CODE = v_bru_agn_code;
            END IF;
            
            UPDATE tqc_branch_units
               SET bru_brn_code = NVL (v_bru_brn_code, bru_brn_code),
                   bru_sht_desc = v_brushtdesc,
                   bru_name = NVL (v_bru_name, bru_name),
                   bru_supervisor = v_bru_supervisor,
                   bru_status = v_bru_status,
                   bru_agn_code = v_bru_agn_code,
                   bru_bra_code = v_bru_bra_code,
                   bru_manager = v_bru_manager,
                   bru_post_level = v_bru_post_level,
                   bru_agn_seq_no = v_bru_agn_seq_no,
                   bru_mngr_allowed = v_bru_mngr_allowed,
                   bru_overide_comm_earned = v_bru_overide_comm_earned,
                   bru_unt_sht_desc_prefix = v_bru_unt_sht_desc_prefix,
                   bru_compt_ov_on_own_buss = v_bru_compt_ov_on_own_buss,
                   bru_pol_seq = v_bru_pol_seq,
                   bru_prop_seq = v_bru_prop_seq
             WHERE bru_code = v_bru_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Updating Units  ' || SQLERRM (SQLCODE));
               RETURN;
         END;
      ELSE
         raise_error ('System Unable To Resolve Branch_Unit ID');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating Branch_Units  ' || SQLERRM (SQLCODE));
         RETURN;
   END update_units;

   PROCEDURE delete_branch_unit (
      v_bru_code         tqc_branch_units.bru_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   )
   IS
   BEGIN
      IF v_bru_code IS NULL
      THEN
         raise_error ('Pass the primary key for branch unit record.');
         RETURN;
      ELSE
         BEGIN
            DELETE      tqc_branch_units
                  WHERE bru_code = v_bru_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error deleting branch unit record.');
               RETURN;
         END;
      END IF;
   END delete_branch_unit;

   PROCEDURE update_divisions (
      v_add_edit            IN       VARCHAR2,
      v_div_code            IN       tqc_divisions.div_code%TYPE,
      v_div_name            IN       tqc_divisions.div_name%TYPE,
      v_div_sht_desc        IN       tqc_divisions.div_sht_desc%TYPE,
      v_div_status          IN       tqc_divisions.div_division_status%TYPE,
      v_div_org_code        IN       tqc_divisions.div_org_code%TYPE,
      v_user                IN       VARCHAR2,
      v_div_order           IN       tqc_divisions.div_order%TYPE DEFAULT NULL,
      v_div_director        IN       tqc_divisions.div_director%TYPE
            DEFAULT NULL,
      v_div_asst_director   IN       tqc_divisions.div_asst_director%TYPE
            DEFAULT NULL,
      v_err                 OUT      VARCHAR2
   )
   IS
      v_rec_count         NUMBER;
      v_count             NUMBER;
      v_div_curr_status   VARCHAR2 (1);
      v_order             NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_divisions
          WHERE div_code = v_div_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error (   'The Division Code is not correct...'
                         || SQLERRM (SQLCODE)
                        );
            RETURN;
      END;

      BEGIN
         SELECT COUNT (*)
           INTO v_order
           FROM tqc_divisions
          WHERE div_order = v_div_order;
      END;

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            IF v_order > 0
            THEN
               raise_error (   'The division order of '
                            || v_div_order
                            || ' has been defined'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
            END IF;

            SELECT COUNT (1)
              INTO v_count
              FROM tqc_divisions
             WHERE div_sht_desc = v_div_sht_desc;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;

            INSERT INTO tqc_divisions
                        (div_code, div_name,
                         div_sht_desc, div_org_code, div_order,
                         div_director, div_asst_director
                        )
                 VALUES (tqcrm_div_code_seq.NEXTVAL, v_div_name,
                         v_div_sht_desc, v_div_org_code, v_div_order,
                         v_div_director, v_div_asst_director
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (SQLERRM (SQLCODE));
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_rec_count != 0
      THEN
         BEGIN
            SELECT div_division_status
              INTO v_div_curr_status
              FROM tqc_divisions
             WHERE div_code = v_div_code;

            IF v_div_curr_status != v_div_status
            THEN
               INSERT INTO tqc_divisions_audit_trl
                           (daudt_code, daudt_div_code,
                            daudt_date, daudt_prev_status,
                            daudt_curr_status, daudt_user
                           )
                    VALUES (tqcrm_daudt_code_seq.NEXTVAL, v_div_code,
                            SYSDATE, v_div_curr_status,
                            v_div_status, v_user
                           );
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error creating division status Audit Trail'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;

         UPDATE tqc_divisions
            SET div_name = NVL (v_div_name, div_name),
                div_sht_desc = NVL (v_div_sht_desc, div_sht_desc),
                div_division_status = NVL (v_div_status, div_division_status),
                div_org_code = NVL (v_div_org_code, div_org_code),
                div_order = NVL (v_div_order, div_order),
                div_director = NVL (v_div_director, div_director),
                div_asst_director =
                                  NVL (v_div_asst_director, div_asst_director)
          WHERE div_code = v_div_code;
      ELSIF v_add_edit = 'D' AND v_rec_count != 0
      THEN
         BEGIN
            SELECT div_division_status
              INTO v_div_curr_status
              FROM tqc_divisions
             WHERE div_code = v_div_code;

            IF v_div_curr_status != v_div_status
            THEN
               INSERT INTO tqc_divisions_audit_trl
                           (daudt_code, daudt_div_code,
                            daudt_date, daudt_prev_status,
                            daudt_curr_status, daudt_user
                           )
                    VALUES (tqcrm_daudt_code_seq.NEXTVAL, v_div_code,
                            SYSDATE, v_div_curr_status,
                            v_div_status, v_user
                           );
            END IF;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error creating division status Audit Trail'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;

         BEGIN
            UPDATE tqc_divisions
               SET div_division_status = 'I'
             WHERE div_code = v_div_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting Division ' || SQLERRM (SQLCODE));
               RETURN;
         END;
      ELSE
         raise_error ('System Unable to Resolve Division ID..');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error updating Divisions...' || SQLERRM (SQLCODE));
         RETURN;
   END update_divisions;

   PROCEDURE update_subdivisions (
      v_add_edit        IN       VARCHAR2,
      v_sdiv_code       IN       tqc_subdivisions.sdiv_code%TYPE,
      v_sdiv_name       IN       tqc_subdivisions.sdiv_name%TYPE,
      v_sdiv_sht_desc   IN       tqc_subdivisions.sdiv_sht_desc%TYPE,
      v_sdiv_div_code   IN       tqc_subdivisions.sdiv_div_code%TYPE,
      v_err             OUT      VARCHAR2
   )
   IS
      v_rec_count   NUMBER;
      v_count       NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_subdivisions
          WHERE sdiv_code = v_sdiv_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error (   'The Sub_Division Code Is wrong...'
                         || SQLERRM (SQLCODE)
                        );
            RETURN;
      END;

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_subdivisions
             WHERE sdiv_sht_desc = v_sdiv_sht_desc;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;

            INSERT INTO tqc_subdivisions
                        (sdiv_code, sdiv_name,
                         sdiv_sht_desc, sdiv_div_code
                        )
                 VALUES (tqcrm_sdiv_code_seq.NEXTVAL, v_sdiv_name,
                         v_sdiv_sht_desc, v_sdiv_div_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (SQLERRM (SQLCODE));
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_rec_count != 0
      THEN
         BEGIN
            UPDATE tqc_subdivisions
               SET sdiv_name = NVL (v_sdiv_name, sdiv_name),
                   sdiv_sht_desc = NVL (v_sdiv_sht_desc, sdiv_sht_desc),
                   sdiv_div_code = NVL (v_sdiv_div_code, sdiv_div_code)
             WHERE sdiv_code = v_sdiv_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Updating '
                            || v_sdiv_name
                            || ' Subdivision '
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'D' AND v_rec_count != 0
      THEN
         BEGIN
            DELETE FROM tqc_subdivisions
                  WHERE sdiv_code = v_sdiv_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Deleting Sub-Division');
               RETURN;
         END;
      ELSE
         raise_error ('System Unable to Resolve Sub_Division ID');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error Updating '
                      || v_sdiv_name
                      || ' Subdivision '
                      || SQLERRM (SQLCODE)
                     );
         RETURN;
   END update_subdivisions;

   PROCEDURE update_brnch_divisions (
      v_add_edit        IN       VARCHAR2,
      v_bdiv_code       IN       tqc_brnch_divisions.bdiv_code%TYPE,
      v_bdiv_brn_code   IN       tqc_brnch_divisions.bdiv_brn_code%TYPE,
      v_bdiv_div_code   IN       tqc_brnch_divisions.bdiv_div_code%TYPE,
      v_bdiv_wef        IN       tqc_brnch_divisions.bdiv_wef%TYPE,
      v_bdiv_wet        IN       tqc_brnch_divisions.bdiv_wet%TYPE,
      v_err             OUT      VARCHAR2
   )
   IS
      v_rec_count   NUMBER;
   BEGIN
      BEGIN
         SELECT COUNT (*)
           INTO v_rec_count
           FROM tqc_brnch_divisions
          WHERE bdiv_code = v_bdiv_code;
      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            NULL;
         WHEN OTHERS
         THEN
            raise_error (   'The Branch_Division Code Is wrong...'
                         || SQLERRM (SQLCODE)
                        );
            RETURN;
      END;

      IF v_add_edit = 'A' AND v_rec_count = 0
      THEN
         BEGIN
            INSERT INTO tqc_brnch_divisions
                        (bdiv_code, bdiv_brn_code,
                         bdiv_div_code, bdiv_wef, bdiv_wet
                        )
                 VALUES (tqcrm_bdiv_code_seq.NEXTVAL, v_bdiv_brn_code,
                         v_bdiv_div_code, v_bdiv_wef, v_bdiv_wet
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error Inserting ' || SQLERRM (SQLCODE));
               RETURN;
         END;
      ELSIF v_add_edit = 'E' AND v_rec_count != 0
      THEN
         BEGIN
            UPDATE tqc_brnch_divisions
               SET bdiv_brn_code = NVL (v_bdiv_brn_code, bdiv_brn_code),
                   bdiv_div_code = NVL (v_bdiv_div_code, bdiv_div_code),
                   bdiv_wef = NVL (v_bdiv_wef, bdiv_wef),
                   bdiv_wet = v_bdiv_wet
             WHERE bdiv_code = v_bdiv_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Updating '
                            || v_bdiv_div_code
                            || ' Branch_Divisions '
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'D' AND v_rec_count != 0
      THEN
         BEGIN
            DELETE FROM tqc_brnch_divisions
                  WHERE bdiv_code = v_bdiv_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Deleting Branch_Division'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSE
         raise_error ('System Unable to Resolve Branch_Division ID');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error (   'Error Updating '
                      || v_bdiv_div_code
                      || ' Branch_Divisions '
                      || SQLERRM (SQLCODE)
                     );
         RETURN;
   END update_brnch_divisions;

   PROCEDURE update_image (v_image BLOB, v_org_code NUMBER, v_col NUMBER)
   IS
   BEGIN
   raise_error('v_col=='||v_col);
      IF v_col = 1
      THEN
         --RAISE_ERROR(v_client_code));
         UPDATE tqc_organizations
            SET org_rpt_logo = v_image
          WHERE org_code = v_org_code;

         COMMIT;
      ELSE
         UPDATE tqc_organizations
            SET org_grp_logo = v_image
          WHERE org_code = v_org_code;

         COMMIT;
      END IF;
   END;

   PROCEDURE update_branch_names (
      v_add_edit      IN       VARCHAR2,
      v_code                   tqc_branch_names.bns_code%TYPE,
      v_sht_desc               tqc_branch_names.bns_sht_desc%TYPE,
      v_name                   tqc_branch_names.bns_name%TYPE,
      v_phy_addrs              tqc_branch_names.bns_phy_addrs%TYPE,
      v_email_addrs            tqc_branch_names.bns_email_addrs%TYPE,
      v_post_addrs             tqc_branch_names.bns_post_addrs%TYPE,
      v_twn_code               tqc_branch_names.bns_twn_code%TYPE,
      v_cou_code               tqc_branch_names.bns_cou_code%TYPE,
      v_contact                tqc_branch_names.bns_contact%TYPE,
      v_manager                tqc_branch_names.bns_manager%TYPE,
      v_tel                    tqc_branch_names.bns_tel%TYPE,
      v_fax                    tqc_branch_names.bns_fax%TYPE,
      v_state                  tqc_branch_names.bns_state_code%TYPE,
      v_acctype                tqc_branch_names.bns_acc_type%TYPE,
      v_reg                    tqc_branch_names.bns_region%TYPE,
      v_post_code              tqc_branch_names.bns_post_code%TYPE,
      v_err           OUT      VARCHAR2
   )
   IS
      v_bns_code        NUMBER;
      v_count           NUMBER;
      v_shtdesc         VARCHAR2 (50);
      v_auto_sht_desc   VARCHAR2 (50)
             := tqc_parameters_pkg.get_param_varchar ('BRANCH_SHT_DESC_AUTO');
   BEGIN
      --Raise_error('****'||v_add_edit));

      --  raise_error('v_acctype  ' || v_acctype || 'v_reg  '  ||v_reg || 'v_sht_desc  ' || v_sht_desc);
      IF v_add_edit = 'A'
      THEN
         BEGIN
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_branch_names
             WHERE bns_sht_desc = v_sht_desc;

            IF v_count > 0
            THEN
               raise_error ('Record with ID Exists!');
               RETURN;
            END IF;

            SELECT NVL (MAX (bns_code) + 1, 1)
              INTO v_bns_code
              FROM tqc_branch_names;

            IF NVL (v_auto_sht_desc, 'N') = 'Y'
            THEN
               IF v_acctype IS NOT NULL AND v_reg IS NOT NULL
               THEN
                  SELECT    'EX'
                         || '1A13'
                         || v_acctype
                         || v_reg
                         || v_bns_code
                         || '0000'
                    INTO v_shtdesc
                    FROM DUAL;
               END IF;
            ELSE
               v_shtdesc := v_sht_desc;
            END IF;

            INSERT INTO tqc_branch_names
                        (bns_code, bns_sht_desc, bns_name,
                         bns_phy_addrs, bns_email_addrs,
                         bns_post_addrs, bns_twn_code, bns_cou_code,
                         bns_contact, bns_manager, bns_tel,
                         bns_fax, bns_state_code, bns_acc_type, bns_region,
                         bns_post_code
                        )
                 VALUES (v_bns_code, UPPER (v_shtdesc), UPPER (v_name),
                         UPPER (v_phy_addrs), UPPER (v_email_addrs),
                         UPPER (v_post_addrs), v_twn_code, v_cou_code,
                         UPPER (v_contact), UPPER (v_manager), UPPER (v_tel),
                         UPPER (v_fax), UPPER (v_state), v_acctype, v_reg,
                         v_post_code
                        );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Inserting  Branch_Name Details'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
            IF NVL (v_auto_sht_desc, 'N') = 'Y'
            THEN
               IF     v_acctype IS NOT NULL
                  AND v_reg IS NOT NULL
                  AND v_sht_desc IS NULL
               THEN
                  SELECT 'EX' || '1A13' || v_acctype || v_reg || v_code
                         || '0000'
                    INTO v_shtdesc
                    FROM DUAL;
               END IF;
            ELSE
               v_shtdesc := v_sht_desc;
            END IF;

            UPDATE tqc_branch_names
               SET bns_sht_desc = v_shtdesc,
                   bns_name = v_name,
                   bns_phy_addrs = v_phy_addrs,
                   bns_email_addrs = v_email_addrs,
                   bns_post_addrs = v_post_addrs,
                   bns_twn_code = v_twn_code,
                   bns_cou_code = v_cou_code,
                   bns_contact = v_contact,
                   bns_manager = v_manager,
                   bns_tel = v_tel,
                   bns_fax = v_fax,
                   bns_state_code = v_state,
                   bns_acc_type = v_acctype,
                   bns_region = v_reg,
                   bns_post_code = v_post_code
             WHERE bns_code = v_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Updating Branch Names  '
                            || v_shtdesc
                            || 'fdfdf'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSE
         raise_error ('System Unable To Resolve Branch_Name  ID');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating Branch_Name  ' || SQLERRM (SQLCODE));
         RETURN;
   END update_branch_names;

   PROCEDURE delete_branch_name (
      v_bns_code         tqc_branch_names.bns_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   )
   IS
   BEGIN
    raise_error ('Branch Name Deletion not possible!');
--      IF v_bns_code IS NULL
--      THEN
--         raise_error ('Pass the primary key for branch name record.');
--         RETURN;
--      ELSE
--         BEGIN
--           DELETE TQC_BRANCH_NAMES WHERE bns_code = v_bns_code;
--         EXCEPTION
--            WHEN OTHERS
--            THEN
--               raise_error ('Error deleting branch name record.');
--               RETURN;
--         END;
--      END IF;
   END delete_branch_name;

   PROCEDURE branch_contacts_proc (
      v_addedit          VARCHAR2,
      v_tbccode          NUMBER,
      v_tbcname          VARCHAR2,
      v_tbcdesignation   VARCHAR2,
      v_mobilephone      VARCHAR2,
      v_telephone        VARCHAR2,
      v_idnumber         VARCHAR2,
      v_physicaladd      VARCHAR2,
      v_emailaddress     VARCHAR2,
      v_brncode          NUMBER
   )
   IS
   BEGIN
      IF v_addedit = 'A'
      THEN
         INSERT INTO tqc_branch_contacts
                     (tbc_code, tbc_name, tbc_designation,
                      tbc_mobile_number, tbc_telephone, tbc_id_number,
                      tbc_physical_address, tbc_email_address, tbc_brn_code
                     )
              VALUES (tbc_code_seq.NEXTVAL, v_tbcname, v_tbcdesignation,
                      v_mobilephone, v_telephone, v_idnumber,
                      v_physicaladd, v_emailaddress, v_brncode
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_branch_contacts
            SET tbc_name = v_tbcname,
                tbc_designation = v_tbcdesignation,
                tbc_mobile_number = v_mobilephone,
                tbc_telephone = v_telephone,
                tbc_id_number = v_idnumber,
                tbc_physical_address = v_physicaladd,
                tbc_email_address = v_emailaddress
          WHERE tbc_code = v_tbccode;
      ELSIF v_addedit = 'D'
      THEN
         DELETE FROM tqc_branch_contacts
               WHERE tbc_code = v_tbccode;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Manipulating Branch Contact.'
                      || SQLERRM (SQLCODE)
                     );
         RETURN;
   END;
PROCEDURE delete_fa_agent (
      v_fa_agent_code         tqc_fa_agents.fa_agent_code%TYPE,
      v_err_msg    OUT   VARCHAR2
   )
   IS
   BEGIN
--    raise_error ('Branch Name Deletion not possible!');
      IF v_fa_agent_code IS NULL
      THEN
         raise_error ('Pass the primary key for branch name record.');
         RETURN;
      ELSE
         BEGIN
            DELETE      tqc_fa_agents
                  WHERE fa_agent_code = v_fa_agent_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Error deleting branch name record.');
               RETURN;
         END;
      END IF;
   END delete_fa_agent;
PROCEDURE update_fa_agents (
      v_add_edit                  IN       VARCHAR2,      
      v_fa_agn_org_code           IN       tqc_fa_agents.fa_agn_org_code%TYPE,
      v_fa_agn_sht_desc           IN       tqc_fa_agents.fa_agn_sht_desc %TYPE,
      v_fa_team_code              IN       tqc_fa_agents.fa_team_code%TYPE, 
      v_fa_agn_name	              IN       tqc_fa_agents.fa_agn_name%TYPE,
      v_fa_agent_code	          IN       tqc_fa_agents.fa_agent_code%TYPE 
     
   )
   IS
      v_rec_count    NUMBER;
      v_count        NUMBER;
      v_regshtdesc   VARCHAR2 (20)                := v_fa_agn_sht_desc;
      v_mng_count    NUMBER;
      v_mng_name     tqc_agencies.agn_name%TYPE;
   BEGIN
--RAISE_ERROR('v_reg_code '||v_reg_code||'v_reg_wet '|| v_reg_wet );
   

 

      IF v_add_edit = 'A' 
      THEN
      
       
          
      
         BEGIN
         
           SELECT COUNT (1)
              INTO v_count
              FROM tqc_fa_agents
             WHERE fa_agent_code = v_fa_agent_code;

            IF v_count > 0
            THEN
               raise_error ('Record with' || v_fa_agent_code || 'alredy Exists!');
               RETURN;
            END IF;
         
            SELECT COUNT (1)
              INTO v_count
              FROM tqc_fa_agents
             WHERE fa_agn_sht_desc = v_fa_agn_sht_desc;

            IF v_count > 0
            THEN
               raise_error ('Record with alredy Exists!');
               RETURN;
            END IF;

            INSERT INTO tqc_fa_agents
                        (fa_agn_code,fa_agn_org_code,fa_agn_sht_desc,
                          fa_team_code,fa_agn_name,fa_agent_code
                        )
                 VALUES (tqc_fa_code_seq.NEXTVAL, v_fa_agn_org_code,
                         v_fa_agn_sht_desc, v_fa_team_code, v_fa_agn_name, v_fa_agent_code
                         );
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Inserting  '
                            || v_fa_agn_name
                            || '  Region Details'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSIF v_add_edit = 'E'
      THEN
         BEGIN
           
            UPDATE tqc_fa_agents
               SET 
                   fa_agn_name = NVL (v_fa_agn_name, fa_agn_name),
                   fa_agn_sht_desc =NVL(v_fa_agn_sht_desc, fa_agn_sht_desc)
                  
                
             WHERE  fa_agent_code = v_fa_agent_code;
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error (   'Error Updating '
                            || v_fa_agn_name
                            || ' Region'
                            || SQLERRM (SQLCODE)
                           );
               RETURN;
         END;
      ELSE
         raise_error ('System Unable to Resolve Region ID ');
         RETURN;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('Error Updating Regions...' || SQLERRM (SQLCODE));
   END update_fa_agents;      
END tqc_web_organization_pkg; 
/