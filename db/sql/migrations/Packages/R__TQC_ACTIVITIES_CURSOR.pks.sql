--
-- TQC_ACTIVITIES_CURSOR  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.TQC_ACTIVITIES_CURSOR AS
/******************************************************************************
   NAME:       TQC_ACTIVITIES_PKG
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        3/17/2010             1. Created this package.
******************************************************************************/

TYPE priority_level_rec IS RECORD
(
  PRL_CODE            TQC_PRIORITY_LEVELS.PRL_CODE%TYPE,
  PRL_DESCRIPTION     TQC_PRIORITY_LEVELS.PRL_DESCRIPTION%TYPE,
  PRL_SHT_DESCRIPTION TQC_PRIORITY_LEVELS.PRL_SHT_DESCRIPTION%TYPE
);

 TYPE priority_level_ref IS REF CURSOR RETURN priority_level_rec;
 
 FUNCTION   get_priority_levels RETURN priority_level_ref;
 TYPE status_rec IS RECORD
 (
    STATUS_ID         TQC_STATUSES.STATUS_ID%TYPE,
    STATUS_CODE      TQC_STATUSES.STATUS_CODE%TYPE,
    STATUS_DESC      TQC_STATUSES.STATUS_DESC%TYPE
    );
    
 TYPE status_ref IS REF CURSOR RETURN status_rec;
 
FUNCTION get_statuses RETURN status_ref; 
TYPE activity_rec IS RECORD
(
       ACT_CODE               TQC_ACTIVITIES.ACT_CODE%TYPE,
       ACT_ACTY_CODE          TQC_ACTIVITIES.ACT_ACTY_CODE%TYPE,
       ACT_TYPE               TQC_ACTIVITY_TYPES.ACTY_DESC%TYPE, 
       ACT_WEF                TQC_ACTIVITIES.ACT_WEF%TYPE, 
       ACT_WET                TQC_ACTIVITIES.ACT_WET%TYPE,
       ACT_DURATION           TQC_ACTIVITIES.ACT_DURATION%TYPE,
       ACT_SUBJECT            TQC_ACTIVITIES.ACT_SUBJECT%TYPE,
       ACT_LOCATION           TQC_ACTIVITIES.ACT_LOCATION%TYPE, 
       ACT_ASSIGNED_TO        TQC_ACTIVITIES.ACT_ASSIGNED_TO%TYPE,
       ASSIGNED_USER          TQC_USERS.USR_NAME%TYPE, 
       ACT_RELATED_TO         TQC_ACTIVITIES.ACT_RELATED_TO%TYPE,
       RELATED_ACCOUNT        TQC_AGENCIES.AGN_NAME%TYPE, 
       ACT_STATUS_ID          TQC_ACTIVITIES.ACT_STATUS_ID%TYPE,
       STATUS                 TQC_STATUSES.STATUS_DESC%TYPE,  
       ACT_DESCRIPTION        TQC_ACTIVITIES.ACT_DESCRIPTION%TYPE,
       ACT_REMINDER           TQC_ACTIVITIES.ACT_REMINDER%TYPE,
       ACT_TEAM               TQC_ACTIVITIES.ACT_TEAM%TYPE,
       TEAM_NAME              TQC_USERS.USR_NAME%TYPE,
       ACT_REMINDER_TIME      TQC_ACTIVITIES.ACT_REMINDER_TIME%TYPE,
       ACT_MSGT_CODE          TQC_ACTIVITIES.ACT_MSGT_CODE%TYPE,
       MSG_SHT_DSC            TQC_MSG_TEMPLATES.MSGT_SHT_DESC%TYPE
 );
 TYPE activity_ref IS REF CURSOR RETURN activity_rec;
 PROCEDURE    get_activities(
    v_act_code     IN  TQC_ACTIVITIES.ACT_CODE%TYPE,
    v_activity_cursor OUT activity_ref);
 TYPE account_rec IS RECORD
    (
    ACC_CODE         TQC_ACCOUNTS.ACC_CODE%TYPE,  
    ACC_TYPE         TQC_ACCOUNTS.ACC_TYPE%TYPE, 
    ACC_TYPE_CODE    TQC_ACCOUNTS.ACC_TYPE_CODE%TYPE,
    ACC_NAME         VARCHAR(100),
    ACC_EMAIL         TQC_AGENCIES.AGN_EMAIL_ADDRESS%TYPE  
    );
  TYPE account_ref IS REF CURSOR RETURN account_rec;
 FUNCTION get_all_accounts  RETURN account_ref;
 PROCEDURE get_uninvited_act_accounts(
    v_act_code  IN TQC_ACTIVITIES.ACT_CODE%TYPE,
    v_account_cursor OUT account_ref);
 PROCEDURE get_accounts(
    v_type VARCHAR2,
    v_account_cursor OUT account_ref);
 
 TYPE note_rec IS RECORD
 (    ANT_CODE         TQC_ACTIVITY_NOTES.ANT_CODE%TYPE, 
      ANT_ACC_CODE     TQC_ACTIVITY_NOTES.ANT_ACC_CODE%TYPE, 
      RELATED_ACCOUNT  TQC_AGENCIES.AGN_NAME%TYPE,  
      ANT_CONTACT_CODE TQC_AGENCIES.AGN_ACC_NO%TYPE, 
      ANT_SUBJECT      TQC_ACTIVITY_NOTES.ANT_SUBJECT%TYPE, 
      ANT_NOTES        TQC_ACTIVITY_NOTES.ANT_NOTES%TYPE, 
      ANT_ACT_CODE     TQC_ACTIVITY_NOTES.ANT_ACT_CODE%TYPE,
      ANT_ATTCHMENT_NAME TQC_ACTIVITY_NOTES.ANT_ATTACHMENT_TYPE%TYPE,
      ANT_ATTACHMENT_TYPE TQC_ACTIVITY_NOTES.ANT_ATTACHMENT_TYPE%TYPE,
      ANT_ATTACHMENT     TQC_ACTIVITY_NOTES.ANT_ATTACHMENT%TYPE
      );
      
   TYPE note_ref IS REF CURSOR RETURN note_rec;
 PROCEDURE get_activity_notes(
  v_ant_act_code IN TQC_ACTIVITY_NOTES.ANT_ACT_CODE%TYPE,
  v_act_note_cursor OUT note_ref);
  
 TYPE act_task_rec IS RECORD
 (
             AT_CODE                TQC_ACTIVITY_TASKS.AT_CODE%TYPE, 
             AT_ACT_CODE            TQC_ACTIVITY_TASKS.AT_ACT_CODE%TYPE,  
             AT_DATE_FROM           TQC_ACTIVITY_TASKS.AT_DATE_FROM%TYPE,
             AT_DATE_TO             TQC_ACTIVITY_TASKS.AT_DATE_TO%TYPE,
             AT_SUBJECT             TQC_ACTIVITY_TASKS.AT_SUBJECT%TYPE,
             AT_STATUS_ID           TQC_ACTIVITY_TASKS.AT_STATUS_ID%TYPE,
             STATUS                 TQC_STATUSES.STATUS_DESC%TYPE, 
             AT_PRIORITY_CODE       TQC_ACTIVITY_TASKS.AT_PRIORITY_CODE%TYPE,
             PRIORITY               TQC_PRIORITY_LEVELS.PRL_DESCRIPTION%TYPE, 
             AT_ACC_CODE            TQC_ACTIVITY_TASKS.AT_ACC_CODE%TYPE,
             RELATED_ACCOUNT        TQC_AGENCIES.AGN_NAME%TYPE
             );
             
   TYPE act_task_ref IS REF CURSOR RETURN act_task_rec;
   
   PROCEDURE get_activity_tasks( 
   v_at_act_code    IN        TQC_ACTIVITY_TASKS.AT_ACT_CODE%TYPE, 
   v_act_task_cursor OUT  act_task_ref);
TYPE act_part_rec IS RECORD
(
     PARTICIP_ID         TQC_ACTIVITY_PARTICIPANTS.PARTICIP_ID%TYPE, 
     PARTICIP_ACT_CODE   TQC_ACTIVITY_PARTICIPANTS.PARTICIP_ACT_CODE%TYPE,  
     PARTICIP_AAC_CODE   TQC_ACTIVITY_PARTICIPANTS.PARTICIP_AAC_CODE%TYPE, 
     ACCOUNT_NAME        TQC_AGENCIES.AGN_NAME%TYPE,  
     EMAIL               TQC_AGENCIES.AGN_EMAIL_ADDRESS%TYPE
     );
     
 TYPE act_part_ref IS REF CURSOR RETURN act_part_rec;
 
PROCEDURE get_activity_participants( 
 v_particip_act_code   IN TQC_ACTIVITY_PARTICIPANTS.PARTICIP_ACT_CODE%TYPE, 
 v_act_part_cursor OUT act_part_ref); 
 
 PROCEDURE  getActivity_email_tmplt( 
         v_msgt_code IN TQC_MSG_TEMPLATES.MSGT_CODE%TYPE,
         v_emailTemp  OUT VARCHAR2
         );
  
  PROCEDURE get_normal_accounts(
    v_type  VARCHAR2,
    v_account_cursor OUT account_ref);   
    
 END TQC_ACTIVITIES_CURSOR; 
/