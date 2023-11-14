CREATE OR REPLACE PACKAGE TQ_CRM."DATA_PORTING" IS  
 PROCEDURE call_create_client( v_load_lms varchar2);
 --Procedure Call_create_agent; 
 PROCEDURE create_agents (
 v_agn_sht_desc VARCHAR2,
 v_name VARCHAR2,
 v_brn_code NUMBER,
 v_act_code NUMBER,
 v_physical_address VARCHAR2,
 v_postal_address VARCHAR2,
 v_postal_code VARCHAR2,
 v_email VARCHAR2,
 v_contact_person VARCHAR2,
 v_tel1 VARCHAR2,
 v_tel2 VARCHAR2,
 v_fax VARCHAR2,
 v_acc_no VARCHAR2,
 v_pin VARCHAR2,
 v_agent_commission NUMBER,
 v_status VARCHAR2,
 v_region_code NUMBER,
 v_town VARCHAR2,
 v_country VARCHAR2,
 v_id_no VARCHAR2,
 v_date_terminated DATE,
 v_date_joined DATE,
 v_bank_acc_no VARCHAR2,
 v_bank VARCHAR2,
 v_reg_no VARCHAR2,
 v_bru_code NUMBER,
 v_balance NUMBER,
 v_agn_code OUT NUMBER
 );  
END Data_Porting;
/
