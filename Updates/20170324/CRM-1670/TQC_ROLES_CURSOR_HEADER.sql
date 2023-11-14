PROCEDURE get_usersnotingrouptype (
        v_users_ref OUT users_ref, v_grp_code IN NUMBER
   );
    
   TYPE accounts_rec IS RECORD (
        acc_code         tqc_accounts.acc_code%TYPE,
        acc_type         tqc_accounts.acc_type%TYPE,
        act_account_type tqc_account_types.act_account_type%TYPE,
        agn_code         tqc_agencies.agn_code%TYPE,
        agn_name         tqc_agencies.agn_name%TYPE
   );
   
   TYPE accounts_ref IS REF CURSOR
        RETURN accounts_rec;
        
   PROCEDURE get_accountsnotingrouptype(
            v_accounts_ref OUT accounts_ref,
            v_grp_code IN NUMBER,
            v_acc_type_id IN tqc_account_types.act_type_id%TYPE);
   FUNCTION getallbranches
      RETURN branches_ref;