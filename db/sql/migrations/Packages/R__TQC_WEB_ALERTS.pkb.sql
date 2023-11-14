--
-- TQC_WEB_ALERTS  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_WEB_ALERTS  AS
/******************************************************************************
 NAME: TQ_HRMS.HRMS_WEB_ALERTS
 PURPOSE:

 REVISIONS:
 Ver Date Author Description
 --------- ---------- --------------- ------------------------------------
 1.0 17/Mar/2010 1. Created this package body.
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
   
   FUNCTION getoverduerequests (v_tsr_status IN VARCHAR2)
      RETURN serv_requests_ref
   IS
      v_cursor   serv_requests_ref;
   BEGIN
      OPEN v_cursor FOR
         SELECT tsr_code,tsr_type,tsr_date, tsr_assignee, tsr_status, tsr_due_date,
                tsr_summary, tsr_description, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                clnt_name || ' ' || clnt_other_names account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, NULL,NULL
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_clients,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = clnt_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_status = v_tsr_status
            AND srt_code(+) = tsr_srt_code
            AND SYSDATE > tsr_due_date
            AND tsr_type != 'Enquiry'
            AND tsr_acc_type = 'C'
         UNION
         SELECT tsr_code,tsr_type,tsr_date, tsr_assignee, tsr_status, tsr_due_date,
                tsr_summary, tsr_description, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                agn_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, NULL,NULL
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_agencies,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = agn_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_status = v_tsr_status
            AND SYSDATE > tsr_due_date
            AND srt_code(+) = tsr_srt_code
            AND tsr_type != 'Enquiry'
            AND tsr_acc_type = 'A'
         UNION
         SELECT tsr_code,tsr_type,tsr_date, tsr_assignee, tsr_status, tsr_due_date,
                tsr_summary, tsr_description, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                 spr_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, NULL,NULL
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_service_providers,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = spr_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_status = v_tsr_status
            AND SYSDATE > tsr_due_date
            AND srt_code(+) = tsr_srt_code
            AND tsr_acc_type = 'SP'
            AND tsr_type != 'Enquiry'
         UNION
         SELECT tsr_code,tsr_type,tsr_date, tsr_assignee, tsr_status, tsr_due_date,
                tsr_summary, tsr_description, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                 srid_name account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, NULL,NULL
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_serv_req_ind_dtls,
                tqc_users uown,
                tqc_serv_req_types
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = srid_code
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'O'
            AND SYSDATE > tsr_due_date
            --AND TSR_ACC_CODE = v_clientCode
            AND tsr_status = 'Open'
            AND tsr_type != 'Enquiry'
            -- AND tsr_acc_type=v_tsr_acc_type
            AND srt_code(+) = tsr_srt_code
         UNION
         SELECT tsr_code,tsr_type,tsr_date, tsr_assignee, tsr_status, tsr_due_date,
                tsr_summary, tsr_description, tsrc_name,
                DECODE (tsr_acc_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER'
                       ) acc_type,
                ass.usr_username assignee,
                 dlt_desc account_name,
                DECODE (tsr_owner_type,
                        'C', 'CLIENT',
                        'A', 'AGENT',
                        'SP', 'SERVICE PROVIDER',
                        'U', 'USER'
                       ) owner_type,
                uown.usr_username owner, NULL,NULL
           FROM tqc_serv_requests,
                tqc_serv_req_cat,
                tqc_users ass,
                tqc_org_division_levels_type,
                tqc_users uown,
                tqc_serv_req_types,
                tqc_organizations
          WHERE tsr_tsrc_code = tsrc_code
            AND tsr_assignee = ass.usr_code
            AND tsr_acc_code = dlt_code_val
            AND tsr_owner_code = uown.usr_code
            AND tsr_acc_type = 'IN'
            AND SYSDATE > tsr_due_date
            -- AND TSR_ACC_CODE = v_clientCode
            AND tsr_status = 'Open'
            AND org_code = tsr_tsrc_code
            AND tsr_type != 'Enquiry'
            -- AND tsr_acc_type=v_tsr_acc_type
            AND srt_code(+) = tsr_srt_code;

      RETURN v_cursor;
   END;
   
   FUNCTION getbirthdayalert
      RETURN alerts_ref
      IS
      v_cursor   alerts_ref;
   BEGIN
      OPEN v_cursor FOR
      SELECT 'HAPPY BIRTHDAY',CLNT_EMAIL_ADDRS,REPLACE(REPLACE(MSGT_MSG,'[CLIENT]',CLNT_NAME||' '||CLNT_OTHER_NAMES),'NULL','')
      FROM TQC_CLIENTS,TQC_MSG_TEMPLATES
      WHERE TO_CHAR(CLNT_DOB,'dd-MM') = TO_CHAR(SYSDATE,'dd-MM')
      AND MSGT_SHT_DESC = 'BIRTHDAY';
       RETURN v_cursor;
   END;

   FUNCTION getholidaysalert
      RETURN alerts_ref
      IS
      v_cursor   alerts_ref;
   BEGIN
      OPEN v_cursor FOR
      SELECT HLD_DESC,CLNT_EMAIL_ADDRS,REPLACE(REPLACE(MSGT_MSG,'[CLIENT]',CLNT_NAME||' '||CLNT_OTHER_NAMES),'NULL','')
      FROM TQC_CLIENTS,TQC_HOLIDAYS,TQC_MSG_TEMPLATES
      WHERE TO_CHAR(HLD_DATE,'dd-MM-RRRR') = TO_CHAR(SYSDATE,'dd-MM-RRRR')
      AND HLD_DESC LIKE '%'||MSGT_SHT_DESC||'%';
       RETURN v_cursor;
   END;
PROCEDURE payment_group_alrt(
     v_grp_usr_code IN NUMBER, 
	 v_account_no   IN VARCHAR2, 
	 v_account_type IN VARCHAR2, 
     v_currency     IN VARCHAR2, 
     v_amt          IN NUMBER,
	 v_paymode      IN   VARCHAR2, 
     v_3rdparty_transno   IN  VARCHAR2, 
	 v_timestamp          IN VARCHAR2,
	 v_receipt_no         IN VARCHAR2,
     v_policy_no          IN VARCHAR2,
     v_claim_no           IN VARCHAR2,
     v_quot_no            IN VARCHAR2,
	 v_pro_code           IN NUMBER, 
     v_si                 IN NUMBER, 
	 v_clnt_code   IN NUMBER, 
	 v_agn_code    IN NUMBER, 
	 v_posted_by   IN VARCHAR2
 )
   IS
      retval               NUMBER;
      v_cc                 VARCHAR2 (200);
      v_subject            VARCHAR2 (200);
      v_message            VARCHAR2 (2000);
      v_email              VARCHAR2 (200);
      v_org_name           VARCHAR2 (200) := NULL; 
	  v_pro_desc           VARCHAR2 (200) := NULL; 
	  v_client             VARCHAR2 (200) := NULL;
	  v_agent              VARCHAR2 (200) := NULL;
	  v_product              VARCHAR2 (200) := NULL;
      v_mail   VARCHAR2 (20) := null; 
      v_web_addrs          VARCHAR2 (200) := NULL;
      v_email_from  VARCHAR2 (200):= tqc_parameters_pkg.get_param_varchar('EMAILS_FROM'); 
      v_usr_code           NUMBER;
      v_cnt   NUMBER := 0;
      
      CURSOR cur_receipients is 
			SELECT usr_username,usr_email email
			   from tqc_users
			 where usr_code in (
				  select gusr_usr_code from tqc_group_users
				  where gusr_grp_usr_code = v_grp_usr_code
				);

   BEGIN
          BEGIN
              v_mail  := tqc_parameters_pkg.get_param_varchar('MAIL_GROUP_PAYMENT'); 
          END;

          BEGIN
             SELECT  agn_name 
               INTO v_agent 
               FROM tqc_agencies
              WHERE agn_code = v_agn_code;
          EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                v_agent:=NULL;
          END;
          
		  BEGIN
             SELECT  NVL(clnt_name,clnt_surname||' '||clnt_other_names) 
               INTO v_client 
               FROM tqc_clients
              WHERE clnt_code = v_clnt_code;
          EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                v_client:=NULL;
          END;		  
          
          BEGIN
             SELECT  pro_desc 
               INTO v_product
               FROM gin_products
              WHERE pro_code = v_pro_code;
          EXCEPTION
             WHEN NO_DATA_FOUND
             THEN
                v_product:=NULL;
          END;
           
          	
		  --RAISE_ERROR('v_product: '||v_product||'v_client: '||v_client); 
            FOR c IN cur_receipients LOOP
              BEGIN
                        SELECT LOWER(c.email) INTO v_email
                        FROM dual;
                        --RAISE_ERROR('v_email: '||v_email||'v_mail: '||v_mail);
                                    
                        IF v_email IS NOT NULL AND NVL(v_mail,'N')='Y'
                      THEN
                         BEGIN
						 
						    BEGIN
								SELECT msgt_msg,msgt_msg_subject 
									INTO v_message,v_subject
									FROM tqc_msg_templates 
									WHERE msgt_sht_desc='PORTAL_GROUPPAY';
								EXCEPTION WHEN NO_DATA_FOUND
								THEN
									raise_error ('Message template [PORTAL_GROUPPAY] not found!');
							END;
						 
						    v_cc := NULL; 
							v_subject := REPLACE (v_subject, '[ACCOUNTNO]', v_account_no);
							v_subject := REPLACE (v_subject, '[ACCOUNTTYPE]', v_account_type);
							v_subject := REPLACE (v_subject, '[RECIEIPTNO]', v_receipt_no); 
                            v_subject := REPLACE (v_subject, '[POLICYNO]', v_policy_no);
                            v_message := REPLACE (v_message, '[ACCOUNTNO]', v_account_no);
                            
                            v_message := REPLACE (v_message, '[SI]', v_si);
                            
							v_message := REPLACE (v_message, '[ACCOUNTTYPE]', v_account_type);
							v_message := REPLACE (v_message, '[RECIEIPTNO]', v_receipt_no);
                            v_message := REPLACE (v_message, '[POLICYNO]', v_policy_no);
                            v_message := REPLACE (v_message, '[CLAIMNO]', v_claim_no);	
                            v_message := REPLACE (v_message, '[QUOTNO]', v_quot_no);		
							v_message := REPLACE (v_message, '[CLIENT]', v_client);
                            v_message := REPLACE (v_message, '[CURRENCY]', v_currency);
							v_message := REPLACE (v_message, '[AGENT]', v_agent);
                            v_message := REPLACE (v_message, '[AMOUNT]', TO_CHAR(v_amt));
                            v_message := REPLACE (v_message, '[TRANSNO]', v_3rdparty_transno);
                            v_message := REPLACE (v_message, '[PAYMETHOD]', v_paymode);
                            v_message := REPLACE (v_message, '[PRODUCT]', LOWER(v_product));
							v_message := REPLACE (v_message, '[TIMESTAMP]', v_timestamp);
                          --RAISE_ERROR('v_email: '||v_email||'v_subject: '||v_subject||' v_message: '||v_message);
                            BEGIN
                            
                               INSERT INTO tqc_email_messages
                                          (email_code, email_sys_code, email_sys_module,
                                           email_clnt_code, email_agn_code, email_pol_code,
                                           email_pol_no, email_clm_no, email_quot_code,
                                           email_address, email_subj, email_msg, email_status,
                                           email_prepared_by, email_prepared_date, email_sender_addres
                                          )
                                   VALUES (tqc_sms_code_seq.NEXTVAL, 37, 'U',
                                           v_clnt_code,NVL(v_agn_code,0), null,
                                           null, null, null,
                                           v_email, v_subject, v_message, 'R',
                                           v_posted_by, SYSDATE, v_email_from
                               );
                            
                            tqc_email_pkg.send_mail(tqc_sms_code_seq.NEXTVAL);
                            EXCEPTION
                               WHEN OTHERS
                               THEN 
                                  raise_error ('Unable to send email' || SQLERRM (SQLCODE));
                            END;
                         END;
                      END IF;  
              END; 
            END LOOP;    
   END payment_group_alrt; 
PROCEDURE new_account_info_alert(v_agn_code IN number, v_new_pwd IN VARCHAR2,v_posted_by IN VARCHAR2)
   IS
      retval               NUMBER;
      v_cc                 VARCHAR2 (200);
      v_subject            VARCHAR2 (200);
      v_message            VARCHAR2 (200);
      v_email            VARCHAR2 (200);
      v_org_name           VARCHAR2 (200) := NULL; 
      v_mail_agent_pwd VARCHAR2 (20) := tqc_parameters_pkg.get_param_varchar('MAIL_AGENT_PWD');
      v_sms_agent_pwd VARCHAR2 (20) := tqc_parameters_pkg.get_param_varchar('SMS_AGENT_PWD');
      v_web_addrs   VARCHAR2 (200) := NULL;
      v_usr_code       NUMBER;
      v_cnt   NUMBER := 0;
      cursor cur_agent is 
      SELECT  *
               FROM tqc_agencies
              WHERE agn_code = v_agn_code;
   BEGIN
   
   
          BEGIN
             SELECT  usr_code 
               INTO v_usr_code 
               FROM tqc_USERS
              WHERE USR_USERNAME = v_posted_by;
          EXCEPTION
             WHEN OTHERS
             THEN
                raise_error ('Error getting posting user');
          END;
         
   
            BEGIN
               SELECT org_name,org_web_addrs
                INTO v_org_name,v_web_addrs
                 FROM tqc_organizations
                WHERE org_default = 'Y';
            END;
                    
            BEGIN
                SELECT msgt_msg INTO v_message
                FROM tqc_msg_templates 
                WHERE msgt_sht_desc='AGENT_ACC_INFO';
            EXCEPTION WHEN NO_DATA_FOUND
            THEN
                raise_error ('Message template [AGENT_ACC_INFO] not found!');
            end;
             
            for c in cur_agent loop
              begin
                        v_cc := NULL;
                                    
                        v_subject :=
                           'New agent account information: [WEBADDR]: Registration Successful!';
                        v_subject := REPLACE (v_subject, '[WEBADDR]', v_web_addrs);
                        v_message := REPLACE (v_message, '[FULLNAME]', c.agn_name);
                        v_message := REPLACE (v_message, '[ORG]', v_org_name);
                        v_message := REPLACE (v_message, '[PASSWORD]', V_New_Pwd);
                        v_message := REPLACE (v_message, '[USERNAME]', c.agn_sht_desc);
                        
                        select lower(c.agn_email_address) into v_email
                        from dual;

                                    
                        IF v_email IS NOT NULL AND NVL(v_mail_agent_pwd,'N')='Y'
                      THEN
                         BEGIN
                             --- raise_error('v_email: '||v_email||'v_subject: '||v_subject||' v_message: '||v_message);
                            BEGIN
                               retval :=
                                  tqc_email_pkg.send_email (NULL,
                                                            v_email,
                                                            v_cc,
                                                            v_subject,
                                                            v_message
                                                           );
                            EXCEPTION
                               WHEN OTHERS
                               THEN 
                                  raise_error ('Unable to send email' || SQLERRM (SQLCODE));
                            END;
                         END;
                      END IF;           
                                 
                       IF c.agn_sms_tel IS NOT NULL AND NVL(v_sms_agent_pwd,'N')='Y'
                      THEN
                         BEGIN
                             --- raise_error('v_email: '||v_email||'v_subject: '||v_subject||' v_message: '||v_message);
                            BEGIN
                                 
                                INSERT INTO Tqc_Sms_Messages
                                   (sms_code, sms_sys_code, sms_sys_module, sms_clnt_code, sms_agn_code, 
                                    sms_pol_code, sms_pol_no, sms_clm_no, sms_tel_no, sms_msg, 
                                    sms_status, sms_prepared_by, sms_prepared_date, sms_send_date, sms_quot_code, 
                                    sms_quot_no, sms_usr_code, sms_sent_response, sms_type)
                                 Values
                                   (tqc_sms_code_seq.NEXTVAL, 0, 'R', NULL, 0, 
                                    NULL, NULL, NULL, c.agn_sms_tel, v_message, 
                                    'D', v_posted_by, sysdate, sysdate, NULL, 
                                    NULL, v_usr_code, NULL, NULL);
                                            
                                    Tqc_Sms_Pkg.Send_Instant_Sms(tqc_sms_code_seq.CURRVAL);
                            EXCEPTION
                               WHEN OTHERS
                               THEN 
                                  raise_error ('Unable to send email' || SQLERRM (SQLCODE));
                            END;
                         END;
                      END IF;    
              end;
            
            end loop;    
   END new_account_info_alert;   
END TQC_WEB_ALERTS; 
/