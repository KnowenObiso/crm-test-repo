
CREATE TABLE TQ_CRM.TQC_SERV_REQ_COMMENTS
(
  SRC_CODE            NUMBER,
  SRC_TSR_CODE        NUMBER,
  SRC_CLIENT_COMMENT  VARCHAR2(500 BYTE),
  SRC_SOLUTION        VARCHAR2(500 BYTE)
);


CREATE UNIQUE INDEX TQ_CRM.TQC_SERV_REQ_COMMENTS_PK ON TQ_CRM.TQC_SERV_REQ_COMMENTS
(SRC_CODE);


ALTER TABLE TQ_CRM.TQC_SERV_REQ_COMMENTS ADD (
  CONSTRAINT TQC_SERV_REQ_COMMENTS_PK
  PRIMARY KEY
  (SRC_CODE)
  USING INDEX TQ_CRM.TQC_SERV_REQ_COMMENTS_PK);

ALTER TABLE TQ_CRM.TQC_SERV_REQ_COMMENTS ADD (
  FOREIGN KEY (SRC_TSR_CODE) 
  REFERENCES TQ_CRM.TQC_SERV_REQUESTS (TSR_CODE));
  
   CREATE SEQUENCE TQ_CRM.TQC_SRC_CODE_SEQ
  START WITH 1
  MAXVALUE 999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


SET DEFINE OFF;
Insert into TQ_CRM.TQC_MSG_TEMPLATES
   (MSGT_CODE, MSGT_SHT_DESC, MSGT_MSG, MSGT_SYS_CODE, MSGT_SYS_MODULE, MSGT_TYPE)
 Values
   (3003, 'SERV_REQ_ESC', 'Dear [ASSIGNEE],
Ticket no [TICKET] has been escalated to you for action. Please log in to the system for details.', 0, 'SV', 
    'EMAIL');
COMMIT;



SET DEFINE OFF;
Insert into TQ_CRM.TQC_ALERT_TYPES
   (ALRT_CODE, ALRT_TYPE, ALRT_SYS_CODE, ALRT_EMAIL, ALRT_SMS, ALRT_SCREEN, ALRT_CHECK_ALERT)
 Values
   (12, 'SERV_REQ_ESC', 0, 'Y', 'N', 
    'Y', 'Y');
COMMIT;

CREATE TABLE TQ_CRM.TQC_SERV_REQ_ECALATIONS
(
  SRE_CODE              NUMBER,
  SRE_SRT_CODE          NUMBER,
  SRE_LVL_DURATION      NUMBER,
  SRE_LVL_ONE_ASSIGNEE  NUMBER,
  SRE_LVL_TWO_DURATION  NUMBER,
  SRE_LVL_TWO_ASSIGNEE  NUMBER
);


CREATE UNIQUE INDEX TQ_CRM.TQC_SERV_REQ_ECALATIONS_PK ON TQ_CRM.TQC_SERV_REQ_ECALATIONS
(SRE_CODE);


CREATE UNIQUE INDEX TQ_CRM.UK_SRE_SRT_CODE ON TQ_CRM.TQC_SERV_REQ_ECALATIONS
(SRE_SRT_CODE);


ALTER TABLE TQ_CRM.TQC_SERV_REQ_ECALATIONS ADD (
  CONSTRAINT TQC_SERV_REQ_ECALATIONS_PK
  PRIMARY KEY
  (SRE_CODE)
  USING INDEX TQ_CRM.TQC_SERV_REQ_ECALATIONS_PK,
  CONSTRAINT UK_SRE_SRT_CODE
  UNIQUE (SRE_SRT_CODE)
  USING INDEX TQ_CRM.UK_SRE_SRT_CODE);

ALTER TABLE TQ_CRM.TQC_SERV_REQ_ECALATIONS ADD (
  CONSTRAINT SRE_SRT_FK 
  FOREIGN KEY (SRE_SRT_CODE) 
  REFERENCES TQ_CRM.TQC_SERV_REQ_TYPES (SRT_CODE));

  

--------------------------------------------------------------------------------------------------------------
FUNCTION getServReqClientComments (
      v_tsr_code   IN   NUMBER DEFAULT NULL
   )
      RETURN serv_req_client_comm_ref
   IS
      v_cursor   serv_req_client_comm_ref;
   BEGIN
      OPEN v_cursor FOR
        select SRC_CODE,SRC_CLIENT_COMMENT,SRC_SOLUTION
        from TQC_SERV_REQ_COMMENTS
        where SRC_TSR_CODE =v_tsr_code;

      RETURN v_cursor;
   END;
   
  procedure servReqClntCommentProc(
       v_addedit                          VARCHAR2,
      v_src_code               IN OUT   NUMBER,
      v_src_client_comment               IN       VARCHAR2,
      v_src_solution          IN       VARCHAR2,
      v_src_tsr_code         IN       NUMBER
  ) IS
  
  BEGIN
         IF v_addedit = 'A'
      THEN
         v_src_code := tqc_src_code_seq.NEXTVAL;

         INSERT INTO TQC_SERV_REQ_COMMENTS
                     (SRC_CODE,SRC_CLIENT_COMMENT,SRC_SOLUTION,SRC_TSR_CODE
                     )
              VALUES (v_src_code,
                            v_src_client_comment,
                            v_src_solution,
                            v_src_tsr_code                           
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE TQC_SERV_REQ_COMMENTS
            SET SRC_CLIENT_COMMENT=v_src_client_comment,
            SRC_SOLUTION=v_src_solution,
            SRC_TSR_CODE=v_src_tsr_code
          WHERE SRC_CODE = v_src_code;
      ELSIF v_addedit = 'D'
      THEN
         NULL;

         DELETE FROM TQC_SERV_REQ_COMMENTS
               WHERE SRC_CODE = v_src_code;
      END IF;
  
  EXCEPTION
        WHEN OTHERS THEN
                RAISE_ERROR('Client comment proc .......'||SQLERRM(SQLCODE));
  END;
  
  /**
  This function returns the service requests types ascalation assignees
  **/
  FUNCTION getservreqscalations
      RETURN serv_req_escalations_ref
   IS
      v_cursor   serv_req_escalations_ref;
   BEGIN
      OPEN v_cursor FOR        
        SELECT sre_code, sre_srt_code,sre_lvl_duration,
        sre_lvl_one_assignee,sre_lvl_two_duration,sre_lvl_two_assignee,
        (select SRT_DESC from tqc_serv_req_types where srt_code=sre_srt_code) sre_srt_code_name,
        (select  USR_NAME  from tqc_users where USR_CODE=sre_lvl_one_assignee )  sre_lvl_one_assignee_name ,
        (select  USR_NAME  from tqc_users where USR_CODE= sre_lvl_two_assignee ) sre_lvl_two_assignee_name
        FROM TQC_SERV_REQ_ECALATIONS;

      RETURN v_cursor;
   END;
   
   /**
  This procedure  inserts updates and deletes  service requests types ascalation assignees
  **/
  procedure servReqClntEscalationsProc(
       v_addedit                           IN VARCHAR2,
      v_sre_code                           IN OUT NUMBER ,
       v_sre_srt_code                    IN NUMBER ,
        v_sre_lvl_duration               IN NUMBER,
       v_sre_lvl_one_assignee        IN NUMBER,
       v_sre_lvl_two_duration        IN NUMBER,
       v_sre_lvl_two_assignee       IN NUMBER
  ) IS
  
  BEGIN
         IF v_addedit = 'A'
      THEN
         v_sre_code := tqc_src_code_seq.NEXTVAL;

         INSERT INTO tqc_serv_req_ecalations
                     (sre_code,sre_srt_code,sre_lvl_duration,
                       sre_lvl_one_assignee,sre_lvl_two_duration,sre_lvl_two_assignee
                     )
              VALUES (v_sre_code,
                            v_sre_srt_code,
                            v_sre_lvl_duration,
                            v_sre_lvl_one_assignee,
                            v_sre_lvl_two_duration,
                            v_sre_lvl_two_assignee                          
                     );
      ELSIF v_addedit = 'E'
      THEN
         UPDATE tqc_serv_req_ecalations
            SET sre_srt_code=v_sre_srt_code,
            sre_lvl_duration=v_sre_lvl_duration,
            sre_lvl_one_assignee=v_sre_lvl_one_assignee,
            sre_lvl_two_duration=v_sre_lvl_two_duration,
            sre_lvl_two_assignee=v_sre_lvl_two_assignee
          where sre_code = v_sre_code;
      ELSIF v_addedit = 'D'
      THEN
         NULL;

         DELETE FROM tqc_serv_req_ecalations
               WHERE sre_code = v_sre_code;
      END IF;
  
  EXCEPTION
        WHEN OTHERS THEN
                RAISE_ERROR('Client escalation proc .......'||SQLERRM(SQLCODE));
  END;
  
  PROCEDURE process_serv_requests_alert
   IS
      v_msgt_msg            VARCHAR2 (400);
      v_final_msgt_msg      VARCHAR2 (200);
      v_alrt_grp_usr_code   NUMBER;
      v_email           VARCHAR2 (200);
      sender                VARCHAR2 (200);
      v_out                 NUMBER;
      v_alrt_code           NUMBER;
      v_param_val           NUMBER;
      v_count               NUMBER;
      v_user_name       VARCHAR2 (200);
       v_name               VARCHAR2 (200);

      CURSOR REQUESTS_LVL_TWO 
      IS
          SELECT  TSR_CODE,SRE_LVL_TWO_ASSIGNEE,TSR_REF_NUMBER,TSR_ASSIGNEE
             FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_ECALATIONS
          WHERE  SYSDATE -TSR_DATE>    ( NVL(SRE_LVL_DURATION,0)+NVL(SRE_LVL_TWO_DURATION,0) )
          and  SRE_SRT_CODE=TSR_SRT_CODE
          AND TSR_STATUS='Open' ;
          
        CURSOR REQUESTS_LVL_ONE 
      IS
           SELECT  TSR_CODE,SRE_LVL_ONE_ASSIGNEE ,TSR_REF_NUMBER ,TSR_ASSIGNEE
            FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_ECALATIONS
          WHERE  SYSDATE -TSR_DATE>    ( NVL(SRE_LVL_DURATION,0) )
          and  SRE_SRT_CODE=TSR_SRT_CODE
          AND TSR_STATUS='Open'
          AND TSR_CODE NOT IN (
                      SELECT  TSR_CODE   FROM TQC_SERV_REQUESTS,TQC_SERV_REQ_ECALATIONS
                      WHERE  SYSDATE -TSR_DATE>    ( NVL(SRE_LVL_DURATION,0)+NVL(SRE_LVL_TWO_DURATION,0) )
                      and  SRE_SRT_CODE=TSR_SRT_CODE
                      AND TSR_STATUS='Open'
          );
   BEGIN
     
 BEGIN
         SELECT msgt_msg
           INTO v_msgt_msg
           FROM tqc_msg_templates
          WHERE msgt_sht_desc = 'SERV_REQ_ESC';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('SERV_REQ_ESC Message Template not Defined...');
      END;
       
  BEGIN
         SELECT alrt_grp_usr_code, alrt_code
           INTO v_alrt_grp_usr_code, v_alrt_code
           FROM tqc_alert_types
          WHERE alrt_sys_code = 0
            AND alrt_email = 'Y'
            AND alrt_type = 'SERV_REQ_ESC';
      EXCEPTION
         WHEN OTHERS
         THEN
            raise_error ('DUE_INVOICES ALERT TYPE NOT ATTACHED TO A USER...');
      END;
     
     




       FOR i in REQUESTS_LVL_TWO loop
       
       SELECT USR_USERNAME,USR_NAME
       INTO v_user_name, v_name
       FROM tqc_users
       where USR_CODE=  i.SRE_LVL_TWO_ASSIGNEE;
       
              BEGIN
       
       SELECT USR_EMAIL
       INTO v_email
       FROM tqc_users
       where USR_CODE=  i.SRE_LVL_TWO_ASSIGNEE;
       EXCEPTION
            WHEN NO_DATA_FOUND THEN
       raise_error ( 'User  '||v_user_name||' DOES NOT HAVE AN EMAIL ADDRESS...');
       END ;
       
        SELECT REPLACE(v_msgt_msg, '[ASSIGNEE]', v_name)
        INTO v_msgt_msg
        FROM DUAL;
        
        SELECT REPLACE(v_msgt_msg, '[TICKET]', I.TSR_REF_NUMBER)
        INTO v_msgt_msg
        FROM DUAL;
  IF I.TSR_ASSIGNEE <>  i.SRE_LVL_TWO_ASSIGNEE THEN
        UPDATE TQC_SERV_REQUESTS
        SET TSR_ASSIGNEE= i.SRE_LVL_TWO_ASSIGNEE
        WHERE TSR_CODE=I.TSR_CODE;
       
         BEGIN
            INSERT INTO tqc_email_messages
                        (email_code, email_sys_code, email_sys_module,
                         email_clnt_code, email_agn_code, email_pol_code,
                         email_pol_no, email_clm_no, email_quot_code,
                         email_address, email_subj, email_msg, email_status,
                         email_prepared_by, email_prepared_date,
                         email_send_date, email_to_send_date
                        )
                 VALUES (tqc_sms_code_seq.NEXTVAL, 1, 'REC',
                         v_alrt_code, v_alrt_code, v_alrt_code,
                         v_alrt_code, v_alrt_code, v_alrt_code,
                         v_email, NULL, v_msgt_msg, 'R',
                         /*v_user*/
                         NULL, TRUNC (SYSDATE),
                         TRUNC (SYSDATE), TRUNC (SYSDATE)
                        );
         END;

         BEGIN
            tqc_email_pkg.send_mail (tqc_sms_code_seq.CURRVAL);
         EXCEPTION
            WHEN OTHERS
            THEN
               raise_error ('Sending Mail');
         END;
         
   END IF;   
      
      END LOOP;
         
         
     FOR i in REQUESTS_LVL_ONE loop
       
       SELECT USR_USERNAME,USR_NAME
       INTO v_user_name, v_name
       FROM tqc_users
       where USR_CODE=  i.SRE_LVL_ONE_ASSIGNEE;
       
              BEGIN
       
       SELECT USR_EMAIL
       INTO v_email
       FROM tqc_users
       where USR_CODE=  i.SRE_LVL_ONE_ASSIGNEE;
       EXCEPTION
            WHEN NO_DATA_FOUND THEN
       raise_error ( 'User  '||v_user_name||' DOES NOT HAVE AN EMAIL ADDRESS...');
       END ;
       
        SELECT REPLACE(v_msgt_msg, '[ASSIGNEE]', v_name)
        INTO v_msgt_msg
        FROM DUAL;
        
        SELECT REPLACE(v_msgt_msg, '[TICKET]', I.TSR_REF_NUMBER)
        INTO v_msgt_msg
        FROM DUAL;
        
         IF I.TSR_ASSIGNEE <>  i.SRE_LVL_ONE_ASSIGNEE THEN
                        UPDATE TQC_SERV_REQUESTS
                        SET TSR_ASSIGNEE= i.SRE_LVL_ONE_ASSIGNEE
                        WHERE TSR_CODE=I.TSR_CODE;
                       
                         BEGIN
                            INSERT INTO tqc_email_messages
                                        (email_code, email_sys_code, email_sys_module,
                                         email_clnt_code, email_agn_code, email_pol_code,
                                         email_pol_no, email_clm_no, email_quot_code,
                                         email_address, email_subj, email_msg, email_status,
                                         email_prepared_by, email_prepared_date,
                                         email_send_date, email_to_send_date
                                        )
                                 VALUES (tqc_sms_code_seq.NEXTVAL, 1, 'SRV',
                                         v_alrt_code, v_alrt_code, v_alrt_code,
                                         v_alrt_code, v_alrt_code, v_alrt_code,
                                         v_email, NULL, v_msgt_msg, 'R',
                                         /*v_user*/
                                         NULL, TRUNC (SYSDATE),
                                         TRUNC (SYSDATE), TRUNC (SYSDATE)
                                        );
                         END;

                         BEGIN
                            tqc_email_pkg.send_mail (tqc_sms_code_seq.CURRVAL);
                         EXCEPTION
                            WHEN OTHERS
                            THEN
                               raise_error ('Sending Mail');
                         END;
         END IF;
      
      
      END LOOP;
         
         
    
   EXCEPTION
      WHEN OTHERS
      THEN
         raise_error ('ERROR SENDING SERVICE REQUEST ALERTS');
   END process_serv_requests_alert;