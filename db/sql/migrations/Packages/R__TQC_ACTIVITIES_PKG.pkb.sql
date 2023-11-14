--
-- TQC_ACTIVITIES_PKG  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_ACTIVITIES_PKG AS
/******************************************************************************
   NAME:        TQC_ACTIVITIES_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        3/17/2010             1. Created this package body.
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
   
    PROCEDURE TQC_ACTIVITIES_PRC (
  v_add_edit         VARCHAR2,
  V_ACT_CODE           TQC_ACTIVITIES.ACT_CODE%TYPE,
  V_ACT_ACTY_CODE      TQC_ACTIVITIES.ACT_ACTY_CODE%TYPE,
  V_ACT_WEF            TQC_ACTIVITIES.ACT_WEF%TYPE DEFAULT NULL,
  V_ACT_WET            TQC_ACTIVITIES.ACT_WET%TYPE DEFAULT NULL,
  V_ACT_DURATION       TQC_ACTIVITIES.ACT_DURATION%TYPE DEFAULT NULL,
  V_ACT_SUBJECT        TQC_ACTIVITIES.ACT_SUBJECT%TYPE DEFAULT NULL,
  V_ACT_LOCATION       TQC_ACTIVITIES.ACT_LOCATION%TYPE DEFAULT NULL,
  V_ACT_ASSIGNED_TO    TQC_ACTIVITIES.ACT_ASSIGNED_TO%TYPE DEFAULT NULL,
  V_ACT_RELATED_TO     TQC_ACTIVITIES.ACT_RELATED_TO%TYPE DEFAULT NULL,
  V_ACT_STATUS_ID      TQC_ACTIVITIES.ACT_STATUS_ID%TYPE DEFAULT NULL,                        
  V_ACT_DESCRIPTION    TQC_ACTIVITIES.ACT_DESCRIPTION%TYPE DEFAULT NULL,
  V_ACT_REMINDER       TQC_ACTIVITIES.ACT_REMINDER%TYPE DEFAULT NULL  ,
  V_ACT_TEAM           TQC_ACTIVITIES.ACT_TEAM%TYPE DEFAULT NULL,
  V_ACT_REMINDER_TIME  TQC_ACTIVITIES.ACT_REMINDER_TIME%TYPE DEFAULT NULL,
  V_ACT_MSGT_CODE  TQC_ACTIVITIES.ACT_MSGT_CODE%TYPE DEFAULT NULL
  
  )    

  IS
  
  
 
  
  BEGIN
   
      
    IF v_add_edit = 'A' THEN 
  
    BEGIN
        INSERT INTO  TQC_ACTIVITIES(
            ACT_CODE,
            ACT_ACTY_CODE,
            ACT_WEF,
            ACT_WET, 
            ACT_DURATION,
            ACT_SUBJECT,
            ACT_LOCATION,
            ACT_ASSIGNED_TO,
            ACT_RELATED_TO,
            ACT_STATUS_ID,
            ACT_DESCRIPTION,
            ACT_REMINDER ,
            ACT_TEAM,
            ACT_REMINDER_TIME,
            ACT_MSGT_CODE
            
            )
        
          VALUES(
           ACT_CODE_SEQ.NEXTVAL,
            V_ACT_ACTY_CODE,
            V_ACT_WEF, 
             V_ACT_WET,
             V_ACT_DURATION,
             V_ACT_SUBJECT, 
             V_ACT_LOCATION, 
            V_ACT_ASSIGNED_TO, 
            V_ACT_RELATED_TO, 
             V_ACT_STATUS_ID,
             V_ACT_DESCRIPTION,
             V_ACT_REMINDER, 
             V_ACT_TEAM,
             V_ACT_REMINDER_TIME,
             V_ACT_MSGT_CODE 
             
             );
             
             EXCEPTION
             WHEN OTHERS
             THEN raise_error('Error inerting record');
         
             END;
    
    
    ELSIF v_add_edit = 'E' THEN 
        UPDATE  TQC_ACTIVITIES SET 
            ACT_ACTY_CODE=V_ACT_ACTY_CODE,
            ACT_WEF=V_ACT_WEF,
            ACT_WET=V_ACT_WET, 
            ACT_DURATION=V_ACT_DURATION,
            ACT_SUBJECT=V_ACT_SUBJECT,
            ACT_LOCATION=V_ACT_LOCATION,
            ACT_ASSIGNED_TO=V_ACT_ASSIGNED_TO,
            ACT_RELATED_TO=V_ACT_RELATED_TO,
            ACT_STATUS_ID=V_ACT_STATUS_ID,
            ACT_DESCRIPTION=V_ACT_DESCRIPTION,
            ACT_REMINDER=V_ACT_REMINDER,
            ACT_TEAM=V_ACT_TEAM,
            ACT_REMINDER_TIME=V_ACT_REMINDER_TIME,
            ACT_MSGT_CODE=V_ACT_MSGT_CODE
        WHERE ACT_CODE = V_ACT_CODE;
        
        
    ELSIF v_add_edit = 'D' THEN 
        DELETE TQC_ACTIVITIES
        WHERE ACT_CODE = V_ACT_CODE;
       
    END IF;
    
  END TQC_ACTIVITIES_PRC ;
  
  
  PROCEDURE TQC_PRIORITY_LEVEL_PRC(
  v_add_edit    varchar2,
  V_PRL_CODE            TQC_PRIORITY_LEVELS.PRL_CODE%TYPE,
  V_PRL_DESCRIPTION     TQC_PRIORITY_LEVELS.PRL_DESCRIPTION%TYPE DEFAULT NULL,
  V_PRL_SHT_DESCRIPTION TQC_PRIORITY_LEVELS.PRL_SHT_DESCRIPTION%TYPE DEFAULT NULL
  )
  IS
  
   
  BEGIN
    IF v_add_edit = 'A' THEN 
      
    INSERT INTO  TQC_PRIORITY_LEVELS(
        PRL_CODE,
        PRL_DESCRIPTION,
        PRL_SHT_DESCRIPTION
        )
        
      VALUES(
         PRL_CODE_SEQ.NEXTVAL,V_PRL_DESCRIPTION,V_PRL_SHT_DESCRIPTION
        
         );
    
    
    ELSIF v_add_edit = 'E' THEN 
        UPDATE  TQC_PRIORITY_LEVELS SET 
             PRL_DESCRIPTION=V_PRL_DESCRIPTION,
             PRL_SHT_DESCRIPTION=V_PRL_SHT_DESCRIPTION
        WHERE PRL_CODE = V_PRL_CODE;
        
    ELSIF v_add_edit = 'D' THEN 
        DELETE TQC_PRIORITY_LEVELS
           WHERE PRL_CODE = V_PRL_CODE;
       
    END IF;
    
    END TQC_PRIORITY_LEVEL_PRC;
    
   PROCEDURE TQC_STATUSES_PRC(
    v_add_edit    varchar2,
    V_STATUS_ID     TQC_STATUSES.STATUS_ID%TYPE, 
    V_STATUS_CODE   TQC_STATUSES.STATUS_CODE%TYPE, 
    V_STATUS_DESC   TQC_STATUSES.STATUS_DESC%TYPE
    )
    IS
    
     
  BEGIN
    IF v_add_edit = 'A' THEN 
      
    INSERT INTO  TQC_STATUSES(
      STATUS_ID, STATUS_CODE, STATUS_DESC  
        )
        
      VALUES(
         STATUS_ID_SEQ.NEXTVAL,V_STATUS_CODE,V_STATUS_DESC
        
         );
    
    
    ELSIF v_add_edit = 'E' THEN 
        UPDATE  TQC_STATUSES SET 
         STATUS_CODE=V_STATUS_CODE,
         STATUS_DESC=V_STATUS_DESC
            
        WHERE STATUS_ID = V_STATUS_ID;
        
    ELSIF v_add_edit = 'D' THEN 
        DELETE TQC_STATUSES
           WHERE STATUS_ID = V_STATUS_ID;
       
    END IF;
    
    END TQC_STATUSES_PRC;
  
  
  PROCEDURE TQC_ACTIVITY_NOTES_PRC(
    v_add_edit    varchar2,
    V_ANT_CODE                TQC_ACTIVITY_NOTES.ANT_CODE%TYPE,  
    V_ANT_ACC_CODE            TQC_ACTIVITY_NOTES.ANT_ACC_CODE%TYPE, 
    V_ANT_CONTACT_CODE        TQC_ACTIVITY_NOTES.ANT_CONTACT_CODE%TYPE, 
    V_ANT_SUBJECT             TQC_ACTIVITY_NOTES.ANT_SUBJECT%TYPE, 
    V_ANT_NOTES               TQC_ACTIVITY_NOTES.ANT_NOTES%TYPE, 
    V_ANT_ATTACHMENT          TQC_ACTIVITY_NOTES.ANT_ATTACHMENT%TYPE DEFAULT NULL, 
    V_ANT_ACT_CODE            TQC_ACTIVITY_NOTES.ANT_CODE%TYPE,
    V_ANT_ATTACHMENT_TYPE     TQC_ACTIVITY_NOTES.ANT_ATTACHMENT_TYPE%TYPE,
    V_ANT_FILE_NAME         TQC_ACTIVITY_NOTES.ANT_FILE_NAME%TYPE DEFAULT NULL
    
    )
    IS
    
    
      
  BEGIN
    IF v_add_edit = 'A' THEN 
      
    INSERT INTO  TQC_ACTIVITY_NOTES(
      ANT_CODE,
        ANT_ACC_CODE,
         ANT_CONTACT_CODE, 
         ANT_SUBJECT, 
         ANT_NOTES, 
        ANT_ATTACHMENT,
          ANT_ACT_CODE,
          ANT_ATTACHMENT_TYPE,
          ANT_FILE_NAME
        )
        
      VALUES(
       ANT_CODE_SEQ.NEXTVAL,
        V_ANT_ACC_CODE,
         V_ANT_CONTACT_CODE, 
         V_ANT_SUBJECT, 
         V_ANT_NOTES, 
        V_ANT_ATTACHMENT,
          V_ANT_ACT_CODE,
          V_ANT_ATTACHMENT_TYPE,
          V_ANT_FILE_NAME
        
         );
    
    
    ELSIF v_add_edit = 'E' THEN 
        UPDATE  TQC_ACTIVITY_NOTES SET 
        
         ANT_ACC_CODE=V_ANT_ACC_CODE,
         ANT_CONTACT_CODE=V_ANT_CONTACT_CODE,
         ANT_SUBJECT=V_ANT_SUBJECT, 
         ANT_NOTES=V_ANT_NOTES, 
         ANT_ATTACHMENT=V_ANT_ATTACHMENT,
         ANT_ACT_CODE=V_ANT_ACT_CODE,
         ANT_ATTACHMENT_TYPE=V_ANT_ATTACHMENT_TYPE,
         ANT_FILE_NAME     =ANT_FILE_NAME
            
        WHERE ANT_CODE = V_ANT_CODE;
        
    ELSIF v_add_edit = 'D' THEN 
        DELETE TQC_ACTIVITY_NOTES
          WHERE ANT_CODE = V_ANT_CODE;
       
    END IF;
    END TQC_ACTIVITY_NOTES_PRC;
    
  PROCEDURE TQC_ACTIVITY_TASKS_PRC(
      v_add_edit    varchar2,  
     V_AT_CODE            TQC_ACTIVITY_TASKS.AT_CODE%TYPE, 
     V_AT_ACT_CODE        TQC_ACTIVITY_TASKS.AT_ACT_CODE%TYPE,
     V_AT_DATE_FROM       TQC_ACTIVITY_TASKS.AT_DATE_FROM%TYPE,
     V_AT_DATE_TO         TQC_ACTIVITY_TASKS.AT_DATE_TO%TYPE,
     V_AT_SUBJECT         TQC_ACTIVITY_TASKS.AT_SUBJECT%TYPE, 
     V_AT_STATUS_ID       TQC_ACTIVITY_TASKS.AT_STATUS_ID%TYPE,
     V_AT_PRIORITY_CODE   TQC_ACTIVITY_TASKS.AT_PRIORITY_CODE%TYPE,
     V_AT_ACC_CODE        TQC_ACTIVITY_TASKS.AT_ACC_CODE%TYPE
     )
     IS
     
       
      
  BEGIN
    IF v_add_edit = 'A' THEN 
      
    INSERT INTO  TQC_ACTIVITY_TASKS(
     AT_CODE,
      AT_ACT_CODE,
       AT_DATE_FROM, 
       AT_DATE_TO, 
       AT_SUBJECT,
        AT_STATUS_ID, 
        AT_PRIORITY_CODE,
         AT_ACC_CODE
        )
        
      VALUES(
        AT_CODE_SEQ.NEXTVAL,
       V_AT_ACT_CODE,
       V_AT_DATE_FROM, 
       V_AT_DATE_TO, 
       V_AT_SUBJECT,
        V_AT_STATUS_ID, 
        V_AT_PRIORITY_CODE,
         V_AT_ACC_CODE 
        
         );
    
    
    ELSIF v_add_edit = 'E' THEN 
        UPDATE  TQC_ACTIVITY_TASKS SET 
         AT_ACT_CODE=V_AT_ACT_CODE,
         AT_DATE_FROM=V_AT_DATE_FROM, 
         AT_DATE_TO= V_AT_DATE_TO,
         AT_SUBJECT=V_AT_SUBJECT,
         AT_STATUS_ID=V_AT_STATUS_ID, 
         AT_PRIORITY_CODE=V_AT_PRIORITY_CODE,
         AT_ACC_CODE=V_AT_ACC_CODE
            
        WHERE AT_CODE = V_AT_CODE;
        
    ELSIF v_add_edit = 'D' THEN 
        DELETE TQC_ACTIVITY_TASKS
          WHERE AT_CODE = V_AT_CODE;
       
    END IF;
    END TQC_ACTIVITY_TASKS_PRC;
      
 PROCEDURE  TQC_ACTIVITY_PARTICIPANTS_PRC(
     v_add_edit    varchar2, 
    V_PARTICIP_ID        TQC_ACTIVITY_PARTICIPANTS.PARTICIP_ID%TYPE, 
    V_PARTICIP_ACT_CODE  TQC_ACTIVITY_PARTICIPANTS.PARTICIP_ACT_CODE%TYPE,
    V_PARTICIP_AAC_CODE  TQC_ACTIVITY_PARTICIPANTS.PARTICIP_AAC_CODE%TYPE
    )
    IS
    BEGIN
    IF v_add_edit = 'A' THEN 
           
    INSERT INTO  TQC_ACTIVITY_PARTICIPANTS(
           PARTICIP_ID,
          PARTICIP_ACT_CODE,
          PARTICIP_AAC_CODE
        )
        
      VALUES(
        PARTICIP_ID_SEQ.NEXTVAL,
        V_PARTICIP_ACT_CODE,
        V_PARTICIP_AAC_CODE
         );
    
    
    ELSIF v_add_edit = 'E' THEN 
        UPDATE  TQC_ACTIVITY_PARTICIPANTS SET 
          PARTICIP_ACT_CODE=V_PARTICIP_ACT_CODE,
          PARTICIP_AAC_CODE=V_PARTICIP_AAC_CODE
            
        WHERE PARTICIP_ID = V_PARTICIP_ID;
        
    ELSIF v_add_edit = 'D' THEN 
        DELETE TQC_ACTIVITY_PARTICIPANTS
          WHERE PARTICIP_ID = V_PARTICIP_ID;
       
    END IF;
    END TQC_ACTIVITY_PARTICIPANTS_PRC;
    
  PROCEDURE send_activity_invites(
     v_ACT_CODE IN NUMBER,
     v_user IN number)
    
    
    IS
    CURSOR act_participants IS
          SELECT TQC_ACTIVITY_PARTICIPANTS .*, TQC_ACTIVITIES .*,  (SELECT  AGN_EMAIL_ADDRESS FROM  TQC_AGENCIES WHERE AGN_CODE IN (
                                    SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=PARTICIP_AAC_CODE)
                                    UNION
                                   SELECT  CLNT_EMAIL_ADDRS  FROM TQC_CLIENTS  WHERE CLNT_CODE  IN (
                                    SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=PARTICIP_AAC_CODE)
                                    UNION
                                    SELECT  SPR_EMAIL  FROM TQC_SERVICE_PROVIDERS  WHERE SPR_CODE  IN (
                                    SELECT ACC_TYPE_CODE FROM TQC_ACCOUNTS  WHERE ACC_CODE=PARTICIP_AAC_CODE)
                                    ) RELATED_EMAIL
              FROM TQC_ACTIVITY_PARTICIPANTS, TQC_ACTIVITIES
              WHERE PARTICIP_ACT_CODE  = ACT_CODE 
              AND PARTICIP_ACT_CODE  = v_ACT_CODE;
        
     n number;
      
              
    BEGIN
           
            FOR  act_participants_rec IN act_participants LOOP
                 
                INSERT INTO TQC_EMAIL_MESSAGES(EMAIL_CODE, EMAIL_SYS_CODE, 
                EMAIL_SYS_MODULE, EMAIL_CLNT_CODE, EMAIL_AGN_CODE,
                 EMAIL_POL_CODE, EMAIL_POL_NO, EMAIL_CLM_NO, EMAIL_QUOT_CODE,
                  EMAIL_ADDRESS, EMAIL_SUBJ, EMAIL_MSG, EMAIL_STATUS, EMAIL_PREPARED_BY,
                   EMAIL_PREPARED_DATE, EMAIL_SEND_DATE )
                   VALUES(TQC_EMAIL_CODE_SEQ.NEXTVAL,0,'U',act_participants_rec.PARTICIP_AAC_CODE,
                   act_participants_rec.PARTICIP_AAC_CODE,NULL,NULL,NULL,NULL,act_participants_rec.RELATED_EMAIL,
                   act_participants_rec.ACT_SUBJECT,act_participants_rec.ACT_DESCRIPTION,'S',
                   v_user,TRUNC(SYSDATE),TRUNC(SYSDATE) );
          END LOOP;
         
          EXCEPTION 
            WHEN OTHERS THEN
            raise_error('Error saving email');
           
    END;
    
    
END TQC_ACTIVITIES_PKG; 
/