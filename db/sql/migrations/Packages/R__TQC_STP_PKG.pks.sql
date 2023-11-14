--
-- TQC_STP_PKG  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_STP_PKG AS
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
                            v_bank_acc_no VARCHAR2);

END TQC_STP_PKG; 
/