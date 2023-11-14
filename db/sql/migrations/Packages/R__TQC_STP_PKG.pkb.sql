--
-- TQC_STP_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_STP_PKG AS
PROCEDURE create_srvce_prvdr(v_spr_sht_desc VARCHAR2, 
                            v_spr_name VARCHAR2, 
                            v_phys_addr VARCHAR2, 
                            v_postal_addr VARCHAR2, 
                            v_twn_code NUMBER, 
                            v_cou_code NUMBER, 
                            v_spt_code NUMBER, 
                            v_phone VARCHAR2, 
                            v_fax VARCHAR2, 
                            v_email VARCHAR2, 
                            v_title VARCHAR2, 
                            v_zip VARCHAR2, 
                            v_wef DATE, 
                            v_wet DATE, 
                            v_contact VARCHAR2, 
                            v_bbr_code NUMBER, 
                            v_bank_acc_no VARCHAR2) IS
    v_spr_code NUMBER;
    v_user VARCHAR2(25) := pkg_global_vars.get_pvarchar2 ('pkg_global_vars.pvg_username');
BEGIN
    select TQC_SPR_CODE_SEQ.nextval into v_spr_code from DUAL;
    
    INSERT INTO TQC_SERVICE_PROVIDERS(
        SPR_CODE, SPR_SHT_DESC, SPR_NAME, SPR_PHYSICAL_ADDRESS, SPR_POSTAL_ADDRESS, 
        SPR_TWN_CODE, SPR_COU_CODE, SPR_SPT_CODE, SPR_PHONE, SPR_FAX, SPR_EMAIL, 
        SPR_TITLE, SPR_ZIP, SPR_WEF, SPR_WET, SPR_CONTACT, SPR_BBR_CODE, 
        SPR_BANK_ACC_NO, SPR_CREATED_BY, SPR_DATE_CREATED)
    VALUES(
        v_spr_code, v_spr_sht_desc, v_spr_name, v_phys_addr, v_postal_addr, 
        v_twn_code, v_cou_code, v_spt_code, v_phone, v_fax, v_email, 
        v_title, v_zip, v_wef, v_wet, v_contact, v_bbr_code, 
        v_bank_acc_no, v_user, TRUNC(SYSDATE));
END;

END TQC_STP_PKG; 
/