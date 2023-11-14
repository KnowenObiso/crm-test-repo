--
-- USERS_VIEW  (View) 
--
CREATE OR REPLACE FORCE VIEW TQ_CRM.USERS_VIEW
(USR_CODE, USR_USERNAME, USR_NAME, USR_LAST_DATE, USR_DT_CREATED, 
 USR_STATUS, USR_PWD, USR_CREATED_BY, USR_EMAIL, USR_PER_RANK_ID, 
 USR_PER_RANK_SHT_DESC, USR_PER_ID, USR_PERSONEL_RANK, USR_PWD_CHANGED, USR_PWD_RESET, 
 USR_LOGIN_ATTEMPTS, USR_SIGN, USR_REF, USR_TYPE, USR_ACCT_MGR, 
 USR_PWD_RESET_CODE, USR_COU_CODE)
AS 
SELECT usr_code, usr_username, usr_name, usr_last_date, usr_dt_created,
          usr_status, usr_pwd, usr_created_by, usr_email, usr_per_rank_id,
          usr_per_rank_sht_desc, usr_per_id, usr_personel_rank,
          usr_pwd_changed, usr_pwd_reset, usr_login_attempts, usr_sign,
          usr_ref, usr_type, usr_acct_mgr, usr_pwd_reset_code, usr_cou_code
     FROM tqc_users
    WHERE usr_status = 'A'
   UNION ALL
   SELECT usr_code, usr_username, usr_name, usr_last_date, usr_dt_created,
          usr_status, usr_pwd, usr_created_by, usr_email, usr_per_rank_id,
          usr_per_rank_sht_desc, usr_per_id, usr_personel_rank,
          usr_pwd_changed, usr_pwd_reset, usr_login_attempts, usr_sign,
          usr_ref, usr_type, usr_acct_mgr, usr_pwd_reset_code, usr_cou_code
     FROM tqc_users 
    WHERE usr_status = 'A';