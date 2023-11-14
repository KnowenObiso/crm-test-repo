--
-- TQC_SMS_PKG030215  (Package) 
--
CREATE OR REPLACE PACKAGE TQ_CRM.Tqc_Sms_Pkg030215 AS
PROCEDURE CREATE_SMS_MSG(v_reciepient IN VARCHAR2,
                        v_clnt_code IN NUMBER,
                        v_agn_code IN NUMBER,
                        v_quot_code IN NUMBER,
                        v_quot_no IN VARCHAR2,
                        v_pol_code IN NUMBER,
                        v_pol_no IN VARCHAR2,
                        v_clm_no IN VARCHAR2,
                        v_msgt_code IN NUMBER,
                        v_sys_code IN NUMBER,
                        v_sys_mod    IN VARCHAR2,
                        v_user IN VARCHAR2,
                        v_sms_code IN OUT NUMBER,
                        v_app_lvl IN VARCHAR2,
                        v_usr_code IN NUMBER);
PROCEDURE SEND_SMS_MSG(v_clnt_code IN NUMBER,
                        v_agn_code IN NUMBER,
                        v_quot_code IN NUMBER,
                        v_quot_no IN VARCHAR,
                        v_pol_code IN NUMBER,
                        v_pol_no IN VARCHAR2,
                        v_clm_no IN VARCHAR2,
                        v_tel_no IN VARCHAR2,
                        v_sms_text IN VARCHAR2,
                        v_sys_code IN NUMBER,
                        v_sys_mod    IN VARCHAR2,
                        v_user IN VARCHAR2,
                        v_sms_code OUT NUMBER);
PROCEDURE SEND_SMS_MSG(v_clnt_code IN NUMBER,
                        v_agn_code IN NUMBER,
                        v_quot_code IN NUMBER,
                        v_pol_code IN NUMBER,
                        v_pol_no IN VARCHAR2,
                        v_clm_no IN VARCHAR2,
                        v_tel_no IN VARCHAR2,
                        v_sms_text IN VARCHAR2,
                        v_sys_code IN NUMBER,
                        v_sys_mod    IN VARCHAR2,
                        v_user IN VARCHAR2,
                        v_sms_code OUT NUMBER);
PROCEDURE SEND_SMS_MSG(v_clnt_code IN NUMBER,
                        v_agn_code IN NUMBER,
                        v_quot_code IN NUMBER,
                        v_pol_code IN NUMBER,
                        v_pol_no IN VARCHAR2,
                        v_clm_no IN VARCHAR2,
                        v_tel_no IN VARCHAR2,
                        v_sms_text IN VARCHAR2,
                        v_sys_code IN NUMBER,
                        v_sys_mod    IN VARCHAR2,
                        v_user IN VARCHAR2 );                        
                                                
PROCEDURE Receive_Sms_Msg(v_tel_no IN VARCHAR2,
                        v_sms_text IN VARCHAR2);
PROCEDURE prompt_inbox_usr;
PROCEDURE prompt_usr_esc;
PROCEDURE create_sms_job;
PROCEDURE create_sms_esc_job;
PROCEDURE remove_sms_job;
PROCEDURE remove_sms_esc_job;
v_job NUMBER;
PROCEDURE create_sms_xml(v_sms_code IN NUMBER DEFAULT NULL);
PROCEDURE ftp_file;
TYPE sms_list_rec IS RECORD
        (CONTACT_NAME VARCHAR2(150),
        CONTACT_TEL VARCHAR2(50),
        CONTACT_CODE NUMBER,
        CONTACT_POLICY_NO VARCHAR2(30),
        MSG_TEXT VARCHAR2(500));

TYPE sms_list_tab IS TABLE OF sms_list_rec INDEX BY BINARY_INTEGER;
TYPE sms_list_ref IS REF CURSOR RETURN sms_list_rec;
PROCEDURE sms_contact_query (resultset IN OUT sms_list_ref,listtype VARCHAR2,
                                                             v_prod_code NUMBER,
                                                           v_pol_no VARCHAR2,
                                                           v_sys_code NUMBER,
                                                           sms_temp_text IN VARCHAR2);
TYPE account_type_rec IS RECORD
( AGNT_CODE  TQC_AGENCIES.AGN_CODE%TYPE,
  ANT_NAME TQC_AGENCIES.AGN_NAME%TYPE,
  ANT_EMAIL TQC_AGENCIES.AGN_EMAIL_ADDRESS%TYPE,
  ANT_TEL TQC_AGENCIES.AGN_SMS_TEL%TYPE
);
TYPE account_type_tab IS TABLE OF account_type_rec INDEX BY BINARY_INTEGER;
TYPE account_type_ref IS REF CURSOR RETURN account_type_rec;
PROCEDURE account_tpye (resultset IN OUT account_type_ref,
                                            v_act_code IN NUMBER);
                                     
TYPE sms_rec IS RECORD
        (SMS_CODE             TQC_SMS_MESSAGES.SMS_CODE%TYPE, 
        SMS_SYS_CODE             TQC_SMS_MESSAGES.SMS_SYS_CODE%TYPE, 
        SYS_NAME                TQC_SYSTEMS.SYS_NAME%TYPE,
        SMS_SYS_MODULE             TQC_SMS_MESSAGES.SMS_SYS_MODULE%TYPE, 
        SMS_CLNT_CODE             TQC_SMS_MESSAGES.SMS_CLNT_CODE%TYPE, 
        CLIENT_NAME             VARCHAR2(250),
        SMS_AGN_CODE             TQC_SMS_MESSAGES.SMS_AGN_CODE%TYPE, 
        AGN_NAME                    TQC_AGENCIES.AGN_NAME%TYPE,
        SMS_POL_CODE             TQC_SMS_MESSAGES.SMS_POL_CODE%TYPE, 
        SMS_POL_NO             TQC_SMS_MESSAGES.SMS_POL_NO%TYPE, 
        SMS_CLM_NO             TQC_SMS_MESSAGES.SMS_CLM_NO%TYPE, 
        SMS_TEL_NO             TQC_SMS_MESSAGES.SMS_TEL_NO%TYPE,
        SMS_MSG             TQC_SMS_MESSAGES.SMS_MSG%TYPE, 
        SMS_STATUS             TQC_SMS_MESSAGES.SMS_STATUS%TYPE, 
        SMS_PREPARED_BY             TQC_SMS_MESSAGES.SMS_PREPARED_BY%TYPE, 
        SMS_PREPARED_DATE             TQC_SMS_MESSAGES.SMS_PREPARED_DATE%TYPE, 
        SMS_SEND_DATE             TQC_SMS_MESSAGES.SMS_SEND_DATE%TYPE, 
        SMS_QUOT_CODE             TQC_SMS_MESSAGES.SMS_QUOT_CODE%TYPE, 
        SMS_QUOT_NO             TQC_SMS_MESSAGES.SMS_QUOT_NO%TYPE, 
        SMS_USR_CODE             TQC_SMS_MESSAGES.SMS_USR_CODE%TYPE);
        
     TYPE sms_ref IS REF CURSOR RETURN sms_rec;
     
     FUNCTION get_sms(v_sms_type IN VARCHAR2,
                        v_clnt_code IN NUMBER DEFAULT -2000,
                        v_agn_code IN NUMBER DEFAULT -2000,
                        v_pol_code  IN NUMBER DEFAULT -2000,
                        v_quot_code IN NUMBER DEFAULT -2000,
                        v_clm_no    IN VARCHAR2 DEFAULT 'ALL',
                        v_sys_code  IN NUMBER DEFAULT -2000) RETURN sms_ref;                                     
 TYPE smsQuotation IS RECORD(QUOT_CODE gin_quotations.QUOT_CODE%type,
                   QUOT_NO gin_quotations.QUOT_NO%type);
 TYPE smsQuotationRec IS REF CURSOR RETURN smsQuotation;
 PROCEDURE getSmsQuotation(v_sms_quotation OUT smsQuotationRec); 
 
 TYPE sms_template_rec IS RECORD (
    MSGT_CODE       TQC_MSG_TEMPLATES.MSGT_CODE%TYPE,
    MSGT_SHT_DESC   TQC_MSG_TEMPLATES.MSGT_SHT_DESC%TYPE,
    MSGT_MSG        TQC_MSG_TEMPLATES.MSGT_MSG%TYPE);
 TYPE sms_template_ref IS REF CURSOR RETURN sms_template_rec;
 FUNCTION get_sms_templates(v_sys_code IN NUMBER,
                           v_sys_module IN VARCHAR2,
                           v_msgt_type IN VARCHAR2) RETURN sms_template_ref;
PROCEDURE alertType_prc(v_addEdit  VARCHAR2, v_alertType_tab IN TQC_ALERT_TYPES_TAB, v_err OUT VARCHAR2);
 
 TYPE alert_type_rec IS RECORD (
 
        ALRT_CODE TQC_ALERT_TYPES.ALRT_CODE %TYPE, 
        ALRT_TYPE TQC_ALERT_TYPES.ALRT_TYPE%TYPE,
        ALRT_SYS_CODE TQC_ALERT_TYPES.ALRT_SYS_CODE%TYPE, 
        ALRT_EMAIL  TQC_ALERT_TYPES.ALRT_EMAIL%TYPE,
        ALRT_SMS TQC_ALERT_TYPES.ALRT_SMS%TYPE ,
        SYS_NAME VARCHAR2(200),
        ALRT_SCREEN TQC_ALERT_TYPES.ALRT_SCREEN%TYPE, 
        ALRT_EMAIL_MSGT_CODE TQC_ALERT_TYPES.ALRT_EMAIL_MSGT_CODE%TYPE, 
        ALRT_SMS_MSGT_CODE TQC_ALERT_TYPES.ALRT_SMS_MSGT_CODE%TYPE, 
        ALRT_GRP_USR_CODE TQC_ALERT_TYPES.ALRT_GRP_USR_CODE%TYPE,
        EMAIL  VARCHAR2(200),
        SMS  VARCHAR2(200),
        GRP_USER  VARCHAR2(200),
        ALRT_CHECK_ALERT TQC_ALERT_TYPES.ALRT_CHECK_ALERT%TYPE
   
  );
 TYPE alert_type_ref IS REF CURSOR RETURN alert_type_rec;
   PROCEDURE get_AlertType(    
                                v_refcur  OUT  alert_type_ref,
                                v_alrt_sys_code in NUMBER
                                );
 PROCEDURE alert_prc(v_addEdit  VARCHAR2, v_alert_tab IN TQC_ALERTS_TAB, v_err OUT VARCHAR2);
 TYPE alert_rec IS RECORD (
 
        ALRTS_CODE TQC_ALERTS.ALRTS_CODE%TYPE , 
        ALRTS_ALRT_CODE TQC_ALERTS.ALRTS_ALRT_CODE%TYPE ,
        ALRTS_SYS_CODE TQC_ALERTS.ALRTS_SYS_CODE%TYPE ,
        ALRTS_AGN_CODE TQC_ALERTS.ALRTS_AGN_CODE%TYPE ,
        ALRTS_CLNT_CODE TQC_ALERTS.ALRTS_CLNT_CODE%TYPE ,
        ALRTS_DESCRIPTION TQC_ALERTS.ALRTS_DESCRIPTION%TYPE ,
        ALRTS_DATE TQC_ALERTS.ALRTS_DATE%TYPE ,
        ALRTS_PERIOD TQC_ALERTS.ALRTS_PERIOD%TYPE ,
        ALRTS_USER_CODE TQC_ALERTS.ALRTS_USER_CODE%TYPE ,
        ALRTS_DEST_TYPE TQC_ALERTS.ALRTS_DEST_TYPE%TYPE , 
        ALRTS_DEST_CODE TQC_ALERTS.ALRTS_DEST_CODE%TYPE , 
        ALRTS_MSGT_CODE TQC_ALERTS.ALRTS_MSGT_CODE%TYPE ,
        AGENCY_NAME      VARCHAR2(200),
        MSGT_SHT_DESC    VARCHAR2(200),
       ACC_TYPE_NAME     VARCHAR2(200) ,
       ALRTS_STATUS     TQC_ALERTS.ALRTS_STATUS%TYPE,
       ALRTS_SHT_DESC   TQC_ALERTS.ALRTS_SHT_DESC%TYPE
   
  );
 TYPE alert_ref IS REF CURSOR RETURN alert_rec;
  PROCEDURE get_Alert(   v_alert_code IN Number , 
                                v_refcur  OUT  alert_ref
                                );
 FUNCTION GETAGENCY_NAME(v_agn_code NUMBER,v_acc_type VARCHAR2) RETURN VARCHAR2;
   FUNCTION ACCOUNT_TYPE_NAME(v_sht_desc VARCHAR2 ) RETURN VARCHAR2;
    TYPE alertMSG_rec IS RECORD (
    CLNT_SHT_DESC           TQC_CLIENTS.CLNT_SHT_DESC%TYPE,
    CLNT_NAME               TQC_CLIENTS.CLNT_NAME%TYPE, 
    CLNT_DOB                TQC_CLIENTS.CLNT_DOB%TYPE,
    CLNT_EMAIL_ADDRS        TQC_CLIENTS.CLNT_EMAIL_ADDRS%TYPE,
    MSGT_MSG                TQC_MSG_TEMPLATES.MSGT_MSG%TYPE ,
    ALRT_TYPE               TQC_ALERT_TYPES.ALRT_TYPE%TYPE,
    USR_NAME                TQC_USERS.USR_NAME%TYPE,
    USR_EMAIL               TQC_USERS.USR_EMAIL%TYPE,
    param                   TQC_PARAMETERS.PARAM_VALUE%TYPE
  
  );
 TYPE alertMSG_ref IS REF CURSOR RETURN alertMSG_rec;
 FUNCTION getBirthDayAlerts(v_sht_desc TQC_ALERTS.ALRTS_SHT_DESC%TYPE )RETURN alertMSG_ref;
 
    TYPE stockAlertMSG_rec IS RECORD (
    ITM_DESCRIPTION        varchar2(50),
    ITM_MAX_LEVEL           NUMBER,
    ITM_MIN_LEVEL           NUMBER,
    ITM_QUANTITY            NUMBER,
    UNT_DESCRIPTION        VARCHAR2(50),
    MSGT_MSG                TQC_MSG_TEMPLATES.MSGT_MSG%TYPE ,
    ALRT_TYPE               TQC_ALERT_TYPES.ALRT_TYPE%TYPE,
    USR_NAME                TQC_USERS.USR_NAME%TYPE,
    USR_EMAIL               TQC_USERS.USR_EMAIL%TYPE,
    param                   TQC_PARAMETERS.PARAM_VALUE%TYPE
  
  );

 TYPE stockAlertMSG_ref IS REF CURSOR RETURN stockAlertMSG_rec;
 FUNCTION getStockAlerts(v_sht_desc TQC_ALERTS.ALRTS_SHT_DESC%TYPE,v_item_code Number )RETURN stockAlertMSG_ref;
 
 PROCEDURE UpdateSMSMessage(v_smsCode       NUMBER,
                            v_clientCode    NUMBER,
                            v_telNo         VARCHAR2,
                            v_message       VARCHAR2,
                            v_userCode      NUMBER);
                             PROCEDURE sendServiceProvSms(v_sms_sys_code IN NUMBER,
                        v_sms_sys_module IN VARCHAR2,
                       v_sms_clnt_code IN NUMBER,
                       v_sms_agn_code IN NUMBER, 
                       v_sms_quot_code IN NUMBER,
                       v_sms_pol_code IN NUMBER, 
                       v_sms_pol_no IN VARCHAR2,
                       v_sms_clm_no IN VARCHAR2, 
                       v_sms_tel_no IN VARCHAR2, 
                       v_sms_msg IN VARCHAR2, 
                       v_sms_status IN VARCHAR2,
                       v_sms_prepared_by IN VARCHAR2, 
                       v_sms_quot_no IN VARCHAR2);
                       PROCEDURE sendserviceprov (
   v_add_edit               IN   VARCHAR2,
   v_alrt_code              IN   NUMBER,
   v_alrt_type              IN   VARCHAR,
   v_alrt_sys_code          IN   NUMBER,
   v_alrt_email             IN   VARCHAR,
   v_alrt_sms               IN   VARCHAR,
   v_alrt_screen            IN   VARCHAR,
   v_alrt_email_msgt_code   IN   NUMBER,
   v_alrt_sms_msgt_code     IN   NUMBER,
   v_alrt_grp_usr_code      IN   NUMBER
);

 TYPE Alerts_rec IS RECORD (
   ALRTS_DESCRIPTION                   tqc_alerts.ALRTS_DESCRIPTION%TYPE
  
  );
 TYPE Alerts_ref IS REF CURSOR RETURN Alerts_rec;
  FUNCTION GetNewAlerts(v_alrts_prepared_by IN VARCHAR2,v_cnt OUT NUMBER)
   RETURN Alerts_ref;
PROCEDURE updateAlerts(v_alrts_prepared_by IN VARCHAR2);
PROCEDURE updateViewedAlerts(v_alrts_prepared_by IN VARCHAR2,v_status IN VARCHAR2);

 TYPE AllUserAlerts_rec IS RECORD (
    ALRTS_CODE           tqc_alerts.ALRTS_CODE%TYPE,
    ALRTS_ALRT_CODE               tqc_alerts.ALRTS_ALRT_CODE%TYPE, 
    AGN_NAME                tqc_agencies.AGN_NAME%TYPE,
    CLNT_NAME        tqc_clients.CLNT_NAME%TYPE,
    ALRTS_DESCRIPTION                tqc_alerts.ALRTS_DESCRIPTION%TYPE ,
    ALRTS_DATE               tqc_alerts.ALRTS_DATE%TYPE,
    ALRTS_PERIOD                tqc_alerts.ALRTS_PERIOD%TYPE,
    ALRTS_STATUS               tqc_alerts.ALRTS_STATUS%TYPE,
    MSGT_MSG                   TQC_MSG_TEMPLATES.MSGT_MSG%TYPE,
    
    ALRTS_SCREEN               tqc_alerts.ALRTS_SCREEN%TYPE,
    ALRTS_PREPARED_BY                tqc_alerts.ALRTS_PREPARED_BY%TYPE,
    ALRTS_PREPARE_DATE               tqc_alerts.ALRTS_PREPARE_DATE%TYPE,
    ALRTS_QUOT_NO                   tqc_alerts.ALRTS_QUOT_NO%TYPE,
    ALRTS_POL_POLICY_NO               tqc_alerts.ALRTS_POL_POLICY_NO%TYPE,
    ALRTS_CLAIM_NO                   tqc_alerts.ALRTS_CLAIM_NO%TYPE
  
  );

 TYPE AllUserAlerts_ref IS REF CURSOR RETURN AllUserAlerts_rec;
 
FUNCTION GetAllUserAlerts(v_alrts_prepared_by IN VARCHAR2)
   RETURN AllUserAlerts_ref;
 PROCEDURE updateViewedAlerts(v_alrts_prepared_by IN VARCHAR2,v_status IN VARCHAR2,v_alrts_code in number);
END Tqc_Sms_Pkg030215; 
 
/