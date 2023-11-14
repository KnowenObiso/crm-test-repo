/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_SMS_WEB_CURSOR  (Package Body) 
--
CREATE OR REPLACE PACKAGE BODY TQ_CRM.TQC_SMS_WEB_CURSOR AS
/******************************************************************************
   NAME:       TQC_SMS_WEB_CURSOR
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        5/6/2010             1. Created this package.
******************************************************************************/

FUNCTION getSmsMessages RETURN smsMessages_ref IS
    v_cursor smsMessages_ref;
BEGIN
    OPEN v_cursor FOR
        SELECT SMS_CODE, SMS_SYS_CODE, SMS_SYS_MODULE, SMS_CLNT_CODE, SMS_AGN_CODE, 
        SMS_POL_CODE, SMS_POL_NO, SMS_CLM_NO, SMS_TEL_NO, SMS_MSG, SMS_STATUS, SMS_PREPARED_BY, 
        SMS_PREPARED_DATE, SMS_SEND_DATE, SMS_QUOT_CODE, SMS_QUOT_NO, SMS_USR_CODE,
        TQC_SETUPS_CURSOR.SYSTEM_NAME(SMS_SYS_CODE) SYSTEM_NAME,
        TQC_SETUPS_CURSOR.CLNT_NAME(SMS_CLNT_CODE) CLIENT_NAME,
        TQC_SETUPS_CURSOR.AGN_NAME(SMS_AGN_CODE) AGENCY_NAME
        FROM TQC_SMS_MESSAGES;
    RETURN v_cursor;
END;

FUNCTION getInboxMessagesDrafts RETURN inboxMessages_ref IS
    v_cursor inboxMessages_ref;
BEGIN
    OPEN v_cursor FOR
        SELECT IBX_CODE, IBX_SYS_CODE, IBX_SYS_MODULE, IBX_TEL_NO, IBX_MSG, IBX_STATUS, 
        IBX_DATE, IBX_ASGND_DATE, IBX_MSG_REPLIED, IBX_USR_INFORMD, IBX_ASGND_TO
        FROM TQC_INBOX_MESSAGES
        WHERE IBX_STATUS = 'D'
        ORDER BY IBX_DATE ASC;
    RETURN v_cursor;
END;

FUNCTION getInboxMessagesAssigned RETURN inboxMessages_ref IS
    v_cursor inboxMessages_ref;
BEGIN
    OPEN v_cursor FOR
        SELECT IBX_CODE, IBX_SYS_CODE, IBX_SYS_MODULE, IBX_TEL_NO, IBX_MSG, IBX_STATUS, 
        IBX_DATE, IBX_ASGND_DATE, IBX_MSG_REPLIED, IBX_USR_INFORMD, IBX_ASGND_TO
        FROM TQC_INBOX_MESSAGES
        WHERE IBX_STATUS = 'A'
        ORDER BY IBX_DATE ASC;
    RETURN v_cursor;
END;

FUNCTION getInboxMessagesReplied RETURN inboxMessages_ref IS
    v_cursor inboxMessages_ref;
BEGIN
    OPEN v_cursor FOR
        SELECT IBX_CODE, IBX_SYS_CODE, IBX_SYS_MODULE, IBX_TEL_NO, IBX_MSG, IBX_STATUS, 
        IBX_DATE, IBX_ASGND_DATE, IBX_MSG_REPLIED, IBX_USR_INFORMD, IBX_ASGND_TO
        FROM TQC_INBOX_MESSAGES
        WHERE IBX_STATUS = 'R'
        ORDER BY IBX_DATE ASC;
    RETURN v_cursor;
END;

END TQC_SMS_WEB_CURSOR; 
/