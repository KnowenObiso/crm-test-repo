/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_PORTAL_PKG030215  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM."TQC_PORTAL_PKG030215" AS
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
         RAISE_APPLICATION_ERROR (-20015,
                                  v_msg || ' - ' || SQLERRM (SQLCODE));
      ELSE
         RAISE_APPLICATION_ERROR (v_err_no,
                                  v_msg || ' - ' || SQLERRM (SQLCODE)
                                 );
      END IF;
   END raise_error;

  FUNCTION getAccountContacts RETURN account_contacts_ref IS
        v_cursor account_contacts_ref;
  BEGIN
        OPEN v_cursor FOR
        SELECT ACCC_CODE,ACCC_USERNAME,ACCC_NAME,TO_CHAR(ACCC_LAST_LOGIN_DATE,'RRRR-MM-DD'),
        ACCC_STATUS, ACCC_PWD,ACCC_AGN_CODE 
        FROM TQC_ACCOUNT_CONTACTS;
        RETURN v_cursor;
  END;
  
  FUNCTION getProducts RETURN products_ref IS
        v_cursor products_ref;
  BEGIN
        OPEN v_cursor FOR
        SELECT PROD_CODE,PROD_SHT_DESC,PROD_DESC,PROD_TYPE,
        PROD_YR_TO_MONTH_RATE,PROD_YR_TO_QUATER_RATE,PROD_YR_TO_S_ANNL_RATE,
        PROD_MNTH_TO_QTR_FCTOR,PROD_MNTH_TO_S_ANNL_FCTOR,PROD_MNTH_TO_ANNL_FCTOR
        FROM LMS_PRODUCTS,LMS_CLASSES
        WHERE PROD_CLA_CODE = CLA_CODE
        AND CLA_TYPE = 'O';
        RETURN v_cursor;
  END;
  
  FUNCTION getProductOptions RETURN prod_options_ref IS
        v_cursor prod_options_ref;
  BEGIN
        OPEN v_cursor FOR
        SELECT POP_CODE,POP_PROD_CODE,POP_SHT_DESC,POP_DESC
        FROM LMS_PROD_OPTIONS; 
        RETURN v_cursor;
  END;
  
  FUNCTION getProdPremMasks RETURN prod_prem_masks_ref IS
        v_cursor prod_prem_masks_ref;
  BEGIN
        OPEN v_cursor FOR
        SELECT PMAS_CODE,PMAS_PROD_CODE,PMAS_SHT_DESC,PMAS_DESC
        FROM LMS_PREMIUM_MASKS;
        RETURN v_cursor;
  END;
  
  PROCEDURE create_clnt_proc(v_accountCode      IN  NUMBER,
                            v_first_name        IN  VARCHAR2,
                            v_middle_name       IN  VARCHAR2,
                            v_surname           IN  VARCHAR2,
                            v_sysCode           IN  NUMBER,
                            v_username          IN  VARCHAR2,
                            v_clientCode        OUT NUMBER,
                            v_clnt_id_reg_no IN VARCHAR2,
                            v_clnt_bank_acc_no IN VARCHAR2,
                            v_clnt_twn_code IN NUMBER) IS
                                
         v_use_names_in_code    VARCHAR2(1) :='Y';
         v_direct_srl_fmt    varchar2(100);
         v_direct_clnt_seq   NUMBER;
         v_accnt_no          VARCHAR2(35);
         v_clnt_param        varchar2(20);   
         v_clnt_sht_desc     varchar2(30);  
         v_brnCode           NUMBER;    
  BEGIN
 
       
        BEGIN
            SELECT TQC_PARAMETERS_PKG.get_param_varchar('USE_NAMES_IN_CODE')
            INTO v_use_names_in_code
            FROM DUAL;
        EXCEPTION
        WHEN OTHERS THEN
         v_use_names_in_code :='Y';
        END;
        --RAISE_ERROR('v_sysCode'||v_sysCode);
        BEGIN
        
            SELECT BRN_CODE INTO v_brnCode
            FROM TQC_BRANCHES,TQC_REGIONS,TQC_SYSTEMS
            WHERE BRN_REG_CODE = REG_CODE
            AND REG_ORG_CODE = SYS_ORG_CODE
            AND SYS_CODE = v_sysCode 
            AND BRN_WEB_DEFAULT = 'Y'
            AND ROWNUM <= 1;
        EXCEPTION
        WHEN NO_DATA_FOUND THEN
             RAISE_ERROR('ERROR . No Default Branch Defined');
        END;
        --RAISE_ERROR('YESY');    
            
        IF NVL(v_use_names_in_code,'Y')='N' THEN
            BEGIN
                SELECT ACT_ID_SERIAL_FORMAT
                INTO v_direct_srl_fmt
                FROM TQC_ACCOUNT_TYPES
                WHERE ACT_CODE = 1;
            EXCEPTION
                WHEN OTHERS THEN
                    RAISE_ERROR('Error getting direct clients format..');
            END;
                
            IF v_direct_srl_fmt IS NULL THEN
                        RAISE_ERROR('Provide Id Format for the Direct Account type');
            END IF;
                
            BEGIN
                SELECT DIRECT_CLNT_SEQ.NEXTVAL INTO v_direct_clnt_seq FROM DUAL;
               -- v_accnt_no:= REPLACE(v_direct_srl_fmt,'[SERIALNO]',LPAD(v_direct_clnt_seq,6,0));
                v_accnt_no:=  LPAD(v_direct_clnt_seq,6,0);
            EXCEPTION
                WHEN OTHERS THEN
                    RAISE_ERROR('Error fetching the sequence...');
            END;
                
            BEGIN
                v_clnt_param := TQC_PARAMETERS_PKG.get_param_varchar('CLIENT_ID_FORMAT');
               -- TQC_PARAMETERS
            EXCEPTION
                WHEN OTHERS THEN
                    RAISE_ERROR('Error fetching client parameter...');
            END;
            
                
            IF NVL(v_clnt_param,'DEFAULT') ='DEFAULT' THEN
                --v_clnt_sht_desc := v_gen_acc_no;
                   v_clnt_sht_desc := Tqc_Sequences_Pkg.get_number_format
                                                                       (
                                                                       'C',
                                                                        v_brnCode,--v_clnt_brn_code,
                                                                        TO_CHAR(SYSDATE,'RRRR'),
                                                                        'I',
                                                                        v_direct_srl_fmt,
                                                                        v_surname,
                                                                        v_first_name
                                                                       );
            END IF;
                
        END IF;
          
            IF  v_clnt_sht_desc IS NULL THEN
                BEGIN
                    v_clnt_sht_desc := TQC_CLIENTS_PKG.GET_CLIENT_SHT_DESC(v_first_name,
                                                        v_middle_name, 
                                                          v_surname);
                EXCEPTION
                    WHEN OTHERS THEN 
                        RAISE_ERROR('Error creating client Id...'||v_clnt_sht_desc);
                END;
            END IF;
        
            IF NVL(v_clnt_param,'DEFAULT')    = 'FMSURNAME' THEN 
                v_accnt_no := v_clnt_sht_desc;
            END IF;
        
            IF  v_accnt_no IS NULL THEN
                v_accnt_no := 'Y';
            END IF;
        
            BEGIN
                SELECT TO_NUMBER(TO_CHAR(SYSDATE,'YY'))||TQC_CLNT_CODE_SEQ.nextval INTO v_clientCode FROM DUAL;
            EXCEPTION
                WHEN OTHERS THEN
                    RAISE_ERROR('Error Fetching proposer code..');
            END;
        
            BEGIN
            --RAISE_ERROR(v_surname);
                INSERT INTO TQC_CLIENTS
                (CLNT_CODE,CLNT_NAME,CLNT_SHT_DESC,CLNT_WEF,CLNT_CREATED_BY,
                CLNT_DATE_CREATED,CLNT_DIRECT_CLIENT,CLNT_ACCNT_NO,CLNT_EMAIL_ADDRS,CLNT_ID_REG_NO,CLNT_BANK_ACC_NO,CLNT_TWN_CODE
                )
                VALUES(v_clientCode,ltrim(rtrim(v_first_name||' '||v_middle_name)),v_clnt_sht_desc,TRUNC(SYSDATE),'CLNT_PORT_SYS',
                TRUNC(SYSDATE),'Y',v_accnt_no,v_username,v_clnt_id_reg_no,v_clnt_bank_acc_no,v_clnt_twn_code);
                
--                UPDATE TQC_CLIENT_WEB_ACCOUNTS SET ACWA_CLNT_CODE = v_clientCode
--                WHERE ACWA_CODE = v_accountCode;
            EXCEPTION
                WHEN OTHERS THEN
                RAISE_ERROR('Error creating the client..');
            END;    
  EXCEPTION WHEN OTHERS THEN
        RAISE_ERROR('Error Creating Client '|| SQLERRM (SQLCODE)); 
  END create_clnt_proc;
  
  PROCEDURE categories_proc(v_addEdit       VARCHAR2,
                            v_sysCode       NUMBER,
                            v_twpcCode      NUMBER,
                            v_twpcName      VARCHAR2,
                            v_twpcDesc      VARCHAR2) IS
  BEGIN
        IF v_addEdit = 'A' THEN
            INSERT INTO TQC_WEB_PRODUCT_CATEGORIES
            (TWPC_CODE, TWPC_SYS_CODE, TWPC_NAME, TWPC_DESCRIPTION)
            VALUES(TWPC_CODE_SEQ.NEXTVAL,v_sysCode,v_twpcName,v_twpcDesc);
        ELSIF v_addEdit = 'E' THEN
            UPDATE TQC_WEB_PRODUCT_CATEGORIES
            SET TWPC_SYS_CODE = v_sysCode, 
            TWPC_NAME = v_twpcName, 
            TWPC_DESCRIPTION = v_twpcDesc
            WHERE TWPC_CODE = v_twpcCode;
        ELSIF v_addEdit = 'D' THEN
            DELETE FROM TQC_WEB_PRODUCT_CATEGORIES
            WHERE TWPC_CODE = v_twpcCode;
        END IF;
  EXCEPTION WHEN OTHERS THEN
        RAISE_ERROR('Error Manipulating Category '|| SQLERRM (SQLCODE)); 
  END categories_proc;
  
 PROCEDURE products_proc(v_addEdit         VARCHAR2,
                          v_twpCode         NUMBER,
                          v_twpcCode        NUMBER,
                          v_prodCode        NUMBER,
                          v_prodDesc        VARCHAR2,
                          v_twp_binder IN VARCHAR2 DEFAULT NULL,
                          v_twp_bind_code IN NUMBER DEFAULT NULL,
                          v_twp_aga_code IN NUMBER DEFAULT NULL,
                          v_twp_agn_code IN NUMBER DEFAULT NULL) IS
  BEGIN
 -- RAISE_ERROR('v_prodCode'||v_prodCode);
    IF v_addEdit = 'A' THEN
        INSERT INTO TQC_WEB_PRODUCTS 
        (TWP_CODE, TWP_TWPC_CODE, TWP_PROD_CODE, TWP_PROD_DESC,TWP_BINDER,
             TWP_BIND_CODE,TWP_AGA_CODE,TWP_AGN_CODE)
        VALUES(TWP_CODE_SEQ.NEXTVAL,v_twpcCode,v_prodCode,v_prodDesc,
        v_twp_binder,v_twp_bind_code,v_twp_aga_code,v_twp_agn_code);
    ELSIF v_addEdit = 'E' THEN
        UPDATE TQC_WEB_PRODUCTS SET 
        TWP_PROD_CODE = v_prodCode, 
        TWP_PROD_DESC = v_prodDesc,
        TWP_BINDER=v_twp_binder,
        TWP_BIND_CODE=v_twp_bind_code,
        TWP_AGN_CODE=v_twp_agn_code,
        TWP_AGA_CODE=v_twp_aga_code
        WHERE TWP_CODE = v_twpCode;
    ELSIF v_addEdit = 'D' THEN
        DELETE FROM TQC_WEB_PRODUCTS 
        WHERE TWP_CODE = v_twpCode;
    END IF;
  EXCEPTION WHEN OTHERS THEN
        RAISE_ERROR('Error Manipulating Product '|| SQLERRM (SQLCODE)); 
  END products_proc;
  
  PROCEDURE options_proc(v_addEdit          VARCHAR2,
                         v_twpoCode         NUMBER,
                         v_popCode          NUMBER,
                         v_twpoDesc         VARCHAR2,
                         v_twpo_bind_code IN NUMBER,
                         v_twpo_twp_code IN NUMBER) IS
  BEGIN
    IF v_addEdit = 'A' THEN
        INSERT INTO TQC_WEB_PROD_OPTIONS
        (TWPO_CODE, TWPO_POP_CODE, TWPO_DESC,TWPO_BIND_CODE,TWPO_TWP_CODE)
        VALUES(TWPO_CODE_SEQ.NEXTVAL,v_popCode,v_twpoDesc,v_twpo_bind_code,v_twpo_twp_code );
    ELSIF v_addEdit = 'E' THEN
        UPDATE TQC_WEB_PROD_OPTIONS SET 
        TWPO_POP_CODE = v_popCode , 
        TWPO_DESC = v_twpoDesc,
        TWPO_BIND_CODE=v_twpo_bind_code,
        TWPO_TWP_CODE=v_twpo_twp_code
        WHERE TWPO_CODE = v_twpoCode;
    ELSIF v_addEdit = 'D' THEN
        DELETE FROM TQC_WEB_PROD_OPTIONS
        WHERE TWPO_CODE = v_twpoCode;
    END IF;
  EXCEPTION WHEN OTHERS THEN
        RAISE_ERROR('Error Manipulating Product Option'|| SQLERRM (SQLCODE)); 
  END options_proc;
PROCEDURE authenticateAccount( v_account_number IN VARCHAR2,v_exists OUT VARCHAR2)
IS
v_cnt NUMBER;
CURSOR PrevRecords_ref IS 
select *
from TQC_SYS_MOD_SUBUNITS_INPUTS
where TSMSI_TSMSD_CODE=161;
BEGIN
v_cnt:=0;
v_exists:='false';
FOR PrevRecords_rec IN PrevRecords_ref
LOOP
IF v_account_number=PrevRecords_rec.tsmsi_value
THEN
v_cnt:=v_cnt+1;
END IF;
END LOOP;
IF v_cnt>=2
THEN
v_exists:='true';
END if;
EXCEPTION
WHEN OTHERS
THEN 
RAISE_ERROR('Failed To Determine if The Account Exists');
END;
PROCEDURE authenticateIdNumber( v_idNumber IN VARCHAR2,v_exists OUT VARCHAR2)
IS
v_cnt NUMBER;
CURSOR PrevRecords_ref IS 
select *
from TQC_SYS_MOD_SUBUNITS_INPUTS
where TSMSI_TSMSD_CODE=141;
BEGIN
v_cnt:=0;
v_exists:='false';
FOR PrevRecords_rec IN PrevRecords_ref
LOOP
IF v_idNumber=PrevRecords_rec.tsmsi_value
THEN
v_cnt:=v_cnt+1;
END IF;
END LOOP;
IF v_cnt>=2
THEN
v_exists:='true';
END if;
EXCEPTION
WHEN OTHERS
THEN 
RAISE_ERROR('Failed To Determine if the Id exists');
END;

 FUNCTION getClientDtls(v_clnt_code  NUMBER)RETURN client_dtls_ref IS
    v_cursor client_dtls_ref;
  BEGIN
    
    OPEN v_cursor FOR
     SELECT CLNT_CODE, CLNT_POSTAL_ADDRS, CLNT_ID_REG_NO, CLNT_PHYSICAL_ADDRS,  
         CLNT_OTHER_NAMES, CLNT_NAME, CLNT_TITLE, CLNT_DOB,
         CLNT_EMAIL_ADDRS, CLNT_TEL, CLNT_PASSPORT_NO, CLNT_CNTCT_NAME_1, CLNT_CNTCT_PHONE_1
         FROM TQC_CLIENTS
         WHERE CLNT_CODE = v_clnt_code;
    RETURN v_cursor;
  EXCEPTION WHEN OTHERS THEN
    RAISE_ERROR('Error Occured '||SQLERRM(SQLCODE));
  END;   
  
  PROCEDURE updateUserDetails (
                            v_clnt_code         NUMBER,
                            v_postal_addrs      VARCHAR2,
                            v_prp_id_reg_no     VARCHAR2,
                            v_physical_addrs    VARCHAR2,
                            v_othernames        VARCHAR2,
                            v_name           VARCHAR2,
                            v_title             VARCHAR2,
                            v_dob               DATE,
                            v_email_addrs       VARCHAR2,
                            v_tel               VARCHAR2
                            ) IS
   
BEGIN
    IF v_clnt_code IS NOT NULL THEN
          
            UPDATE TQC_CLIENTS SET   
                CLNT_POSTAL_ADDRS = NVL(v_postal_addrs,CLNT_POSTAL_ADDRS), 
                CLNT_ID_REG_NO = NVL(v_prp_id_reg_no,CLNT_ID_REG_NO),
                CLNT_PHYSICAL_ADDRS = NVL(v_physical_addrs,CLNT_PHYSICAL_ADDRS), 
                CLNT_OTHER_NAMES = NVL(v_othernames,CLNT_OTHER_NAMES), 
                CLNT_SURNAME = NVL(v_name,CLNT_SURNAME), 
                CLNT_TITLE = NVL(v_title,CLNT_TITLE),
                CLNT_DOB = NVL(v_dob,CLNT_DOB),
                CLNT_EMAIL_ADDRS = NVL(v_email_addrs,CLNT_EMAIL_ADDRS),
                CLNT_TEL = NVL(v_tel,CLNT_TEL)
                WHERE CLNT_CODE = v_clnt_code;
     
--            UPDATE LMS_PROPOSERS SET     
--                PRP_POSTAL_ADDRESS = NVL(v_postal_addrs, PRP_POSTAL_ADDRESS),
--                PRP_ID_REG_NO = NVL(v_prp_id_reg_no, PRP_ID_REG_NO),
--                PRP_PHYSICAL_ADDR =  NVL(v_physical_addrs, PRP_PHYSICAL_ADDR),
--                PRP_OTHER_NAMES =  NVL(v_othernames, PRP_OTHER_NAMES),
--                PRP_SURNAME = NVL(v_surname, PRP_SURNAME),
--                PRP_TITLE = NVL(v_title,PRP_TITLE),
--                PRP_DOB = NVL(v_dob,PRP_DOB),
--                PRP_EMAIL = NVL(v_email_addrs,PRP_EMAIL),
--                PRP_TEL = NVL(v_tel,PRP_TEL) 
--                WHERE   PRP_CLNT_CODE = v_clnt_code;

    END IF;
    
    EXCEPTION
       WHEN OTHERS THEN
         Raise_error('Error Updating Details');
                  
END;



END TQC_PORTAL_PKG030215; 
/