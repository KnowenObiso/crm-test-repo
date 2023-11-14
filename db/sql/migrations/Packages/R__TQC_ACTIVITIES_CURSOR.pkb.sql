--
-- TQC_ACTIVITIES_CURSOR  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_ACTIVITIES_CURSOR AS
/******************************************************************************
   NAME:       TQC_ACTIVITIES_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        3/17/2010             1. Created this package.
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

 FUNCTION   get_priority_levels RETURN priority_level_ref
 IS 
  v_priority_lev_cur priority_level_ref;
 BEGIN
   
        OPEN v_priority_lev_cur FOR
        SELECT PRL_CODE, PRL_DESCRIPTION, PRL_SHT_DESCRIPTION
        FROM TQC_PRIORITY_LEVELS;
        RETURN (v_priority_lev_cur);
   END get_priority_levels;
 FUNCTION get_statuses RETURN status_ref
    IS
    v_status_Cur status_ref;
    BEGIN
    OPEN v_status_Cur FOR
    SELECT STATUS_ID, STATUS_CODE, STATUS_DESC
    FROM TQC_STATUSES
    ORDER BY STATUS_DESC;
     RETURN(v_status_Cur);
     
    END get_statuses;
    
  FUNCTION get_all_accounts RETURN account_ref
  IS
  v_accounts_cursor account_ref;
  BEGIN
    OPEN  v_accounts_cursor FOR
    SELECT ACC_CODE,  'Agency'ACC_TYPE, ACC_TYPE_CODE,AGN_NAME ACC_NAME,AGN_EMAIL_ADDRESS ACC_EMAIL 
    FROM TQC_AGENCIES,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=AGN_CODE 
    
    UNION
     SELECT ACC_CODE, 'Client' ACC_TYPE, ACC_TYPE_CODE,CLNT_NAME  ACC_NAME,CLNT_EMAIL_ADDRS ACC_EMAIL
    FROM TQC_CLIENTS ,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=CLNT_CODE  
    UNION
     SELECT ACC_CODE,'Service Provider' ACC_TYPE, ACC_TYPE_CODE,SPR_NAME  ACC_NAME,SPR_EMAIL ACC_EMAIL
    FROM TQC_SERVICE_PROVIDERS ,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=SPR_CODE 
    ORDER BY ACC_TYPE;
     RETURN (v_accounts_cursor);
    
  END get_all_accounts;
   PROCEDURE get_uninvited_act_accounts(
    v_act_code  IN TQC_ACTIVITIES.ACT_CODE%TYPE,
    v_account_cursor OUT account_ref)
     IS
     
  BEGIN
    OPEN  v_account_cursor FOR
    SELECT ACC_CODE, 'Agency'ACC_TYPE, ACC_TYPE_CODE,AGN_NAME ACC_NAME,AGN_EMAIL_ADDRESS ACC_EMAIL 
    FROM TQC_AGENCIES,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=AGN_CODE 
    AND  ACC_CODE NOT IN(SELECT PARTICIP_AAC_CODE  FROM TQC_ACTIVITIES,TQC_ACTIVITY_PARTICIPANTS 
                    WHERE PARTICIP_ACT_CODE=ACT_CODE
                    AND   ACT_CODE=v_act_code)
    
    UNION
     SELECT ACC_CODE,'Client' ACC_TYPE, ACC_TYPE_CODE,CLNT_NAME  ACC_NAME,CLNT_EMAIL_ADDRS ACC_EMAIL
    FROM TQC_CLIENTS ,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=CLNT_CODE  
    AND  ACC_CODE NOT IN(SELECT PARTICIP_AAC_CODE  FROM TQC_ACTIVITIES,TQC_ACTIVITY_PARTICIPANTS 
                    WHERE PARTICIP_ACT_CODE=ACT_CODE 
                    AND   ACT_CODE=v_act_code)
    UNION
     SELECT ACC_CODE,'Service Provider' ACC_TYPE, ACC_TYPE_CODE,SPR_NAME  ACC_NAME,SPR_EMAIL ACC_EMAIL
    FROM TQC_SERVICE_PROVIDERS ,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=SPR_CODE 
    AND  ACC_CODE NOT IN(SELECT PARTICIP_AAC_CODE  FROM TQC_ACTIVITIES,TQC_ACTIVITY_PARTICIPANTS 
                    WHERE PARTICIP_ACT_CODE=ACT_CODE 
                    AND   ACT_CODE=v_act_code)
    ORDER BY ACC_TYPE;
          
     END get_uninvited_act_accounts;
  PROCEDURE get_accounts(
    v_type  VARCHAR2,
    v_account_cursor OUT account_ref)
     IS
     
  BEGIN
  
    IF v_type = 'A' THEN 
    OPEN  v_account_cursor FOR
    SELECT ACC_CODE, 'A' ACC_TYPE, ACC_TYPE_CODE,AGN_NAME ACC_NAME,AGN_EMAIL_ADDRESS ACC_EMAIL 
    FROM TQC_AGENCIES,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=AGN_CODE
    ORDER BY 4;    
    ELSIF v_type = 'C' THEN
    OPEN  v_account_cursor FOR
    SELECT ACC_CODE,'C' ACC_TYPE, ACC_TYPE_CODE,CLNT_NAME  ACC_NAME,CLNT_EMAIL_ADDRS ACC_EMAIL
    FROM TQC_CLIENTS ,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=CLNT_CODE
    ORDER BY 4;
    ELSIF v_type = 'SP' THEN 
    OPEN  v_account_cursor FOR
    SELECT ACC_CODE,'SP' ACC_TYPE, ACC_TYPE_CODE,SPR_NAME  ACC_NAME,SPR_EMAIL ACC_EMAIL
    FROM TQC_SERVICE_PROVIDERS ,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=SPR_CODE
    ORDER BY 4; 
    ELSE
    OPEN  v_account_cursor FOR
     SELECT USR_CODE,'DIRECT',0, USR_USERNAME, USR_EMAIL
        FROM TQC_USER_SYSTEMS, TQC_SYSTEMS,TQC_SYS_POSTS,TQC_USERS
        WHERE TQC_USER_SYSTEMS.USYS_SYS_CODE = TQC_SYSTEMS.SYS_CODE
        AND SYS_ACTIVE <> 'N'
       
        AND USYS_SPOST_CODE=SPOST_CODE(+)
        AND USR_CODE=USYS_USR_CODE
        ORDER BY SYS_NAME;
    END IF;
          
 END get_accounts;
   
 PROCEDURE    get_activities(
    v_act_code     IN  TQC_ACTIVITIES.ACT_CODE%TYPE,
    v_activity_cursor OUT activity_ref)
    IS
    BEGIN
    OPEN v_activity_cursor FOR
        SELECT
            ACT_CODE,
            ACT_ACTY_CODE,(SELECT ACTY_DESC  FROM TQC_ACTIVITY_TYPES
                        WHERE ACTY_CODE=ACT_ACTY_CODE )
            ACT_TYPE,
            ACT_WEF, 
           ACT_WET,
           ACT_DURATION,
           ACT_SUBJECT,
           ACT_LOCATION, 
           ACT_ASSIGNED_TO,(SELECT USR_NAME  FROM TQC_USERS  
                WHERE USR_CODE=ACT_ASSIGNED_TO) 
           ASSIGNED_USER, 
           ACT_RELATED_TO,(SELECT  AGN_NAME FROM  TQC_AGENCIES WHERE AGN_CODE IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=ACT_RELATED_TO)
                            UNION
                           SELECT  CLNT_NAME  FROM TQC_CLIENTS  WHERE CLNT_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=ACT_RELATED_TO)
                            UNION
                            SELECT  SPR_NAME  FROM TQC_SERVICE_PROVIDERS  WHERE SPR_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=ACT_RELATED_TO)
                            )  
           RELATED_ACCOUNT,       
           ACT_STATUS_ID,(SELECT STATUS_DESC FROM   TQC_STATUSES 
                 WHERE STATUS_ID=ACT_STATUS_ID)
           STATUS, 
           ACT_DESCRIPTION,
           ACT_REMINDER,
           ACT_TEAM,(SELECT USR_NAME  FROM TQC_USERS  
                WHERE USR_CODE=ACT_TEAM) 
           TEAM_NAME,
           ACT_REMINDER_TIME,
           ACT_MSGT_CODE,(SELECT MSGT_SHT_DESC FROM TQC_MSG_TEMPLATES WHERE  MSGT_CODE=ACT_MSGT_CODE)
           MSG_SHT_DSC
           
       FROM TQC_ACTIVITIES
       WHERE ACT_CODE=NVL(v_act_code,ACT_CODE);
       
  
  END  get_activities;
  
   PROCEDURE get_activity_notes(
  v_ant_act_code IN TQC_ACTIVITY_NOTES.ANT_ACT_CODE%TYPE,
  v_act_note_cursor OUT note_ref)
  IS
  BEGIN
  OPEN v_act_note_cursor FOR 
  SELECT
     ANT_CODE, 
     ANT_ACC_CODE,(SELECT  AGN_NAME FROM  TQC_AGENCIES WHERE AGN_CODE IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=ANT_ACC_CODE)
                            UNION
                           SELECT  CLNT_NAME  FROM TQC_CLIENTS  WHERE CLNT_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=ANT_ACC_CODE)
                            UNION
                            SELECT  SPR_NAME  FROM TQC_SERVICE_PROVIDERS  WHERE SPR_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=ANT_ACC_CODE)
                            )  
      RELATED_ACCOUNT, 
      ANT_CONTACT_CODE,
      ANT_SUBJECT,
      ANT_NOTES, 
      ANT_ACT_CODE,
      ANT_ATTACHMENT_TYPE,
      ANT_FILE_NAME,
      ANT_ATTACHMENT
      FROM TQC_ACTIVITY_NOTES 
      WHERE ANT_ACT_CODE=v_ant_act_code;
  END get_activity_notes; 
  
   PROCEDURE get_activity_tasks( 
   v_at_act_code    IN        TQC_ACTIVITY_TASKS.AT_ACT_CODE%TYPE, 
   v_act_task_cursor OUT  act_task_ref)
   IS
   BEGIN
   OPEN v_act_task_cursor FOR
    SELECT
           AT_CODE,
            AT_ACT_CODE,
            AT_DATE_FROM,
             AT_DATE_TO, 
             AT_SUBJECT,
              AT_STATUS_ID,(SELECT STATUS_DESC FROM   TQC_STATUSES 
                 WHERE STATUS_ID=AT_STATUS_ID)
                 STATUS,
               AT_PRIORITY_CODE,(SELECT PRL_DESCRIPTION FROM TQC_PRIORITY_LEVELS 
                                WHERE PRL_CODE=AT_PRIORITY_CODE)
                PRIORITY, 
               AT_ACC_CODE,(SELECT  AGN_NAME FROM  TQC_AGENCIES WHERE AGN_CODE IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=AT_ACC_CODE)
                            UNION
                           SELECT  CLNT_NAME  FROM TQC_CLIENTS  WHERE CLNT_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=AT_ACC_CODE)
                            UNION
                            SELECT  SPR_NAME  FROM TQC_SERVICE_PROVIDERS  WHERE SPR_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=AT_ACC_CODE)
                            )  
                 RELATED_ACCOUNT            
               FROM  TQC_ACTIVITY_TASKS 
               WHERE AT_ACT_CODE=v_at_act_code;
         
   END get_activity_tasks;
   
   PROCEDURE get_activity_participants( 
 v_particip_act_code   IN TQC_ACTIVITY_PARTICIPANTS.PARTICIP_ACT_CODE%TYPE, 
 v_act_part_cursor OUT act_part_ref)
 IS
 BEGIN
 OPEN v_act_part_cursor FOR
 
SELECT
PARTICIP_ID, PARTICIP_ACT_CODE, PARTICIP_AAC_CODE,
(SELECT  AGN_NAME FROM  TQC_AGENCIES WHERE AGN_CODE IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=PARTICIP_AAC_CODE)
                            UNION
                           SELECT  CLNT_NAME  FROM TQC_CLIENTS  WHERE CLNT_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=PARTICIP_AAC_CODE)
                            UNION
                            SELECT  SPR_NAME  FROM TQC_SERVICE_PROVIDERS  WHERE SPR_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=PARTICIP_AAC_CODE)
                            )  
  ACCOUNT_NAME,
  (SELECT  AGN_EMAIL_ADDRESS FROM  TQC_AGENCIES WHERE AGN_CODE IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=PARTICIP_AAC_CODE)
                            UNION
                           SELECT  CLNT_EMAIL_ADDRS  FROM TQC_CLIENTS  WHERE CLNT_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=PARTICIP_AAC_CODE)
                            UNION
                            SELECT SPR_EMAIL  FROM TQC_SERVICE_PROVIDERS  WHERE SPR_CODE  IN (
                            SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=PARTICIP_AAC_CODE)
                            )  
   EMAIL
 FROM TQC_ACTIVITY_PARTICIPANTS 
 WHERE PARTICIP_ACT_CODE=v_particip_act_code;
 END get_activity_participants;
 
PROCEDURE  getActivity_email_tmplt( 
        v_msgt_code IN TQC_MSG_TEMPLATES.MSGT_CODE%TYPE,
         v_emailTemp  OUT VARCHAR2)
         
          IS
          v_template varchar2(300);        
        
      BEGIN
            SELECT MSGT_MSG INTO v_template FROM  TQC_MSG_TEMPLATES WHERE MSGT_CODE=v_msgt_code; 
        
      v_emailTemp:=v_template;
      
     END getActivity_email_tmplt;
         
  PROCEDURE get_normal_accounts(
    v_type  VARCHAR2,
    v_account_cursor OUT account_ref)
     IS
     
  BEGIN
    IF v_type = 'A' THEN 
    OPEN  v_account_cursor FOR
    SELECT ACC_CODE, 'A' ACC_TYPE, ACC_TYPE_CODE,AGN_NAME ACC_NAME,AGN_EMAIL_ADDRESS ACC_EMAIL 
    FROM TQC_AGENCIES,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=AGN_CODE
    
    ORDER BY 4;    
    ELSIF v_type = 'C' THEN
    OPEN  v_account_cursor FOR
    SELECT ACC_CODE,'C' ACC_TYPE, ACC_TYPE_CODE,CLNT_NAME  ACC_NAME,CLNT_EMAIL_ADDRS ACC_EMAIL
    FROM TQC_CLIENTS ,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=CLNT_CODE
    ORDER BY 4;
    ELSIF v_type = 'SP' THEN 
    OPEN  v_account_cursor FOR
    SELECT ACC_CODE,'SP' ACC_TYPE, ACC_TYPE_CODE,SPR_NAME  ACC_NAME,SPR_EMAIL ACC_EMAIL
    FROM TQC_SERVICE_PROVIDERS ,TQC_ACCOUNTS 
    WHERE  ACC_TYPE_CODE=SPR_CODE
    ORDER BY 4; 
    ELSE
    OPEN  v_account_cursor FOR
    SELECT USR_CODE,'U' ACC_TYPE, USR_CODE,USR_USERNAME  ACC_NAME,USR_EMAIL ACC_EMAIL
    FROM TQC_USERS
    WHERE  USR_TYPE ='U'
    AND  USR_STATUS='A'
    ORDER BY 4;
    END IF;
          
 END get_normal_accounts;
 END TQC_ACTIVITIES_CURSOR; 
/