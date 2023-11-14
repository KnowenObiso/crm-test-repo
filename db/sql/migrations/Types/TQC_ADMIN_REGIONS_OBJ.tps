--
-- TQC_ADMIN_REGIONS_OBJ  (Type) 
--
CREATE OR REPLACE TYPE TQ_CRM."TQC_ADMIN_REGIONS_OBJ"                                          AS OBJECT
(
v_adm_reg_code number(8),
v_adm_reg_type varchar(15), 
adm_reg_sht_desc varchar(10), 
v_adm_reg_name varchar(100), 
v_adm_reg_cou_code number(8)
); 
/