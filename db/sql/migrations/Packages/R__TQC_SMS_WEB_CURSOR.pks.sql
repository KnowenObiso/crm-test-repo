/* This object may not be sorted properly in the script due to cirular references. */
--
-- TQC_SMS_WEB_CURSOR  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.tqc_sms_web_cursor
AS
/******************************************************************************
   NAME:       TQC_SMS_WEB_CURSOR
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        5/6/2010             1. Created this package.
******************************************************************************/
   TYPE smsmessages_rec IS RECORD (
      sms_code            tqc_sms_messages.sms_code%TYPE,
      sms_sys_code        tqc_sms_messages.sms_sys_code%TYPE,
      sms_sys_module      tqc_sms_messages.sms_sys_module%TYPE,
      sms_clnt_code       tqc_sms_messages.sms_clnt_code%TYPE,
      sms_agn_code        tqc_sms_messages.sms_agn_code%TYPE,
      sms_pol_code        tqc_sms_messages.sms_pol_code%TYPE,
      sms_pol_no          tqc_sms_messages.sms_pol_no%TYPE,
      sms_clm_no          tqc_sms_messages.sms_clm_no%TYPE,
      sms_tel_no          tqc_sms_messages.sms_tel_no%TYPE,
      sms_msg             tqc_sms_messages.sms_msg%TYPE,
      sms_status          tqc_sms_messages.sms_status%TYPE,
      sms_prepared_by     tqc_sms_messages.sms_prepared_by%TYPE,
      sms_prepared_date   tqc_sms_messages.sms_prepared_date%TYPE,
      sms_send_date       tqc_sms_messages.sms_send_date%TYPE,
      sms_quot_code       tqc_sms_messages.sms_quot_code%TYPE,
      sms_quot_no         tqc_sms_messages.sms_quot_no%TYPE,
      sms_usr_code        tqc_sms_messages.sms_usr_code%TYPE,
      system_name         VARCHAR2 (100),
      client_name         VARCHAR2 (100),
      agency_name         VARCHAR2 (100)
   );

   TYPE smsmessages_ref IS REF CURSOR
      RETURN smsmessages_rec;

   FUNCTION getsmsmessages
      RETURN smsmessages_ref;

   TYPE inboxmessages_rec IS RECORD (
      ibx_code          tqc_inbox_messages.ibx_code%TYPE,
      ibx_sys_code      tqc_inbox_messages.ibx_sys_code%TYPE,
      ibx_sys_module    tqc_inbox_messages.ibx_sys_module%TYPE,
      ibx_tel_no        tqc_inbox_messages.ibx_tel_no%TYPE,
      ibx_msg           tqc_inbox_messages.ibx_msg%TYPE,
      ibx_status        tqc_inbox_messages.ibx_status%TYPE,
      ibx_date          tqc_inbox_messages.ibx_date%TYPE,
      ibx_asgnd_date    tqc_inbox_messages.ibx_asgnd_date%TYPE,
      ibx_msg_replied   tqc_inbox_messages.ibx_msg_replied%TYPE,
      ibx_usr_informd   tqc_inbox_messages.ibx_usr_informd%TYPE,
      ibx_asgnd_to      tqc_inbox_messages.ibx_asgnd_to%TYPE
   );

   TYPE inboxmessages_ref IS REF CURSOR
      RETURN inboxmessages_rec;

   FUNCTION getinboxmessagesdrafts
      RETURN inboxmessages_ref;

   FUNCTION getinboxmessagesassigned
      RETURN inboxmessages_ref;

   FUNCTION getinboxmessagesreplied
      RETURN inboxmessages_ref;
END tqc_sms_web_cursor; 
/