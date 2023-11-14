/*
* Copyright (c) 2010 TurnKey Africa Ltd. All Rights Reserved.
*
* This software is the confidential and proprietary information of TurnKey
* Africa Ltd. ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the terms
* of the license agreement you entered into with TurnKey Africa Ltd.
*
* TURNKEY AFRICA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
* OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TURNKEY AFRICA SHALL NOT BE LIABLE
* FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
* DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/

package TurnQuest.view.dao;


import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.Alert;
import TurnQuest.view.models.AlertType;
import TurnQuest.view.models.InboxMessage;
import TurnQuest.view.models.MessageTemplate;
import TurnQuest.view.models.SmsMessage;
import TurnQuest.view.provider.System;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;


public class MessagingDAO {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public MessagingDAO() {
        super();
    }
    
    /**
     *Fetch list of all available products based on the current selected system.
     * 
     * @return available products
     */
    public List<MessageProduct> fetchMessageProducts(){
        String sysShortDesc = (String)session.getAttribute("sysShortDesc");
        String sysCode = (String)session.getAttribute("sysCode");
        
        String query = null;
        List<MessageProduct> messageProds = new ArrayList<MessageProduct>();
        
        if(sysShortDesc.equalsIgnoreCase("LMS") && sysCode.equals("27")){
            // query list of products from lms_products
            query = "SELECT prod_code, prod_desc from TQ_LMS.LMS_PRODUCTS";
        } else if (sysShortDesc.equalsIgnoreCase("GIS") && sysCode.equals("37")){
            query = "select pro_code, pro_desc from tq_gis.gin_products";
        } else {
            query = null;
        }
        
        if(query!=null){
            DBConnector dbHandler = new DBConnector();
            OracleCallableStatement cstmt = null;
            OracleConnection conn =null;
            try{
                conn = dbHandler.getDatabaseConnection();
                cstmt = (OracleCallableStatement)conn.prepareCall(query);
                
                OracleResultSet rs = (OracleResultSet)cstmt.executeQuery();
                
                while(rs.next()){
                    MessageProduct messageProd = new MessageProduct();
                    messageProd.setProdCode(rs.getBigDecimal(1));
                    messageProd.setProdDesc(rs.getString(2));
                    
                    messageProds.add(messageProd);
                }
            } catch(Exception e){
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        
        return messageProds;
    }

    /**
     * Fetch a list of all the <code>MessageTemplate</code> objects/records with a
     * given code.
     *
     * @return A list of all <code>MessageTemplate</code> objects/records
     */
    public List<MessageTemplate> fetchMessageTemplatesBySystem() {
        List<MessageTemplate> msgTemplateList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SETUPS_CURSOR.get_em_template(?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            msgTemplateList = new ArrayList<MessageTemplate>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            if (session.getAttribute("sysCode") != null) {
                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("sysCode").toString()));
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    MessageTemplate messageTemplate = new MessageTemplate();
                    messageTemplate.setMsgtCode(resultSet.getBigDecimal(1));
                    messageTemplate.setMsgtShtDesc(resultSet.getString(2));
                    messageTemplate.setMsgtMsg(resultSet.getString(3));
                    messageTemplate.setMsgtSysCode(resultSet.getBigDecimal(4));
                    messageTemplate.setMsgtSysModule(resultSet.getString(5));
                    messageTemplate.setMsgtType(resultSet.getString(6));
                    messageTemplate.setMsgtSysProd(resultSet.getBigDecimal(7));
                    messageTemplate.setMsgtSysProdName(resultSet.getString(8));
                    messageTemplate.setMsgtSubject(resultSet.getString(9));
                    
                    msgTemplateList.add(messageTemplate);
                }

                resultSet.close();
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return msgTemplateList;
    }
    public static String messageType(String msg) 
    {
        if (msg != null) 
                                                {
            /* if(msg.toLowerCase().contains("has been authori")){
              return "Policy Authorization");
            } else if(msg.toLowerCase().contains("has been received")){
              return "Policy Conversion");
            */

            if ( msg.toLowerCase().contains("loan") ||
                 msg.toLowerCase().contains("maturity") ||
                 msg.toLowerCase().contains("refund") ||
                 msg.toLowerCase().contains("surrender") ||
                 msg.toLowerCase().contains("withdrawal") 
              ) {
                return "Policy Benefits";
            }
            if (msg.toLowerCase().contains("not taken")) {
                return "Policy Not Taken Up";
            }
            if (msg.toLowerCase().contains("fail")) {
                return "Insufficient Funds";
            }
            if (msg.toLowerCase().contains("authoriz")) {
                return "Policy Authorization";
            }
            if (msg.toLowerCase().contains("cancel")) {
                return "Policy Cancellation";
            } 
           if (msg.toLowerCase().contains("laps")) {
                return "Policy Lapsation";
            } 
           if (msg.toLowerCase().contains("accept")) {
                return "Policy Conversion";
            } 
           if (msg.toLowerCase().contains("renew")) {
                return "Policy Renewal";
            } 
           if (msg.toLowerCase().contains("first premium")) {
                return "Policy Inception";
            }
            if (msg.toLowerCase().contains("accept")) {
                return "Policy Accepted";
            }
            if (msg.toLowerCase().contains("deducted and allocated")) {
                return "Premim allocation";
            } 
           if (msg.toLowerCase().contains("outstanding")) {
                return "Deduction Ceased";
            }
            if (msg.toLowerCase().contains("birthday")) {
                return "Birthday Message";
            } 
            if (msg.toLowerCase().contains("claim")
               || "CLAIMS".equalsIgnoreCase(msg)
            ){
                return "Claims";
            } 
            if (msg.toLowerCase().contains("debt")) {
                return "Debtor/Accounts";
            } 
        }
                            
        return "Others";
    }

    public List<System> fetchAllmessages() throws SQLException {
        List<System> systemsList = new ArrayList<System>();
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.selectallMessages(?,?,?,?);end;";
        CallableStatement statement = null;
        OracleConnection connection = null;
        
        try {
            String val=(String) GlobalCC.getSysParamValue("SMS_NO_LENGTH");
            int smsNoLength=Integer.valueOf(val);
            String site=(String)GlobalCC.getSysParamValue("DEFAULT_SITE");
            String message = session.getAttribute("msg").toString();
            
            connection = dbConnector.getDatabaseConnection();
            statement = connection.prepareCall(query);
            if (session.getAttribute("sysCode") != null) {

                BigDecimal sysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());
                String smsWEFVal = "";
                String smsWETVal = "";
                String smsTransTypeVal = "";

                if (session.getAttribute("smsWEF") != null) {
                    smsWEFVal = session.getAttribute("smsWEF").toString();
                }

                if (session.getAttribute("smsWET") != null) {
                    smsWETVal = session.getAttribute("smsWET").toString();
                }

                if (session.getAttribute("smsTransType") != null) {
                    smsTransTypeVal =
                            session.getAttribute("smsTransType").toString();
                }

                statement.registerOutParameter(1, OracleTypes.CURSOR);
                statement.setBigDecimal(2, sysCode);
                statement.setString(3, smsWEFVal == "" ? null : smsWEFVal);
                statement.setString(4, smsWETVal == "" ? null : smsWETVal);
                statement.setString(5,
                                    smsTransTypeVal == "" ? null : smsTransTypeVal);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(1);

                while (resultSet.next()) {
                    System system = new System();
                    system.setSms_code(resultSet.getString(1));
                    system.setSms_sys_code(resultSet.getString(2));
                    system.setSms_sys_module(resultSet.getString(3));
                    system.setSms_clnt_code(resultSet.getString(4));
                    system.setSms_agn_code(resultSet.getString(5));
                    system.setSms_pol_no(resultSet.getString(6));
                    system.setSms_pol_code(resultSet.getString(7));
                    system.setSms_clm_no(resultSet.getString(8));
                  
                    system.setSms_tel_no(resultSet.getString(9));
                    system.setSms_msg(resultSet.getString(10));
                    /*if (system.getSms_msg() != null) {
                        
                        if (system.getSms_msg().toLowerCase().contains("loan") ||
                            system.getSms_msg().toLowerCase().contains("maturity") ||
                            system.getSms_msg().toLowerCase().contains("refund") ||
                            system.getSms_msg().toLowerCase().contains("surrender") ||
                            system.getSms_msg().toLowerCase().contains("withdrawal") 
                                    ) {
                            system.setTransLevel("Policy Benefits");
                        }
                        else if (system.getSms_msg().toLowerCase().contains("not taken")) {
                            system.setTransLevel("Policy Not Taken Up");
                        }
                        else if (system.getSms_msg().toLowerCase().contains("fail")) {
                            system.setTransLevel("Insufficient Funds");
                        }
                        else if (system.getSms_msg().toLowerCase().contains("authoriz")) {
                            system.setTransLevel("Policy Authorization");
                        }
                        else if (system.getSms_msg().toLowerCase().contains("cancel")) {
                            system.setTransLevel("Policy Cancellation");
                        } else if (system.getSms_msg().toLowerCase().contains("laps")) {
                            system.setTransLevel("Policy Lapsation");
                        } else if (system.getSms_msg().toLowerCase().contains("accept")) {
                            system.setTransLevel("Policy Conversion");
                        } else if (system.getSms_msg().toLowerCase().contains("renew")) {
                            system.setTransLevel("Policy Renewal");
                        } else if (system.getSms_msg().toLowerCase().contains("first premium")) {
                            system.setTransLevel("Policy Inception");
                        } else if (system.getSms_msg().toLowerCase().contains("accept")) {
                            system.setTransLevel("Policy Accepted");
                        } else if (system.getSms_msg().toLowerCase().contains("deducted and allocated")) {
                            system.setTransLevel("Premim allocation");
                        } else if (system.getSms_msg().toLowerCase().contains("outstanding")) {
                            system.setTransLevel("Deduction Ceased");
                        }else if (system.getSms_msg().toLowerCase().contains("birthday")) {
                            system.setTransLevel("Birthday Message");
                        } else if (system.getSms_msg().toLowerCase().contains("claim")
                           || "CLAIMS".equalsIgnoreCase(system.getSms_sys_module())
                        ){
                            system.setTransLevel("Claims");
                        } else if (system.getSms_msg().toLowerCase().contains("debt")) {
                            system.setTransLevel("Debtor/Accounts");
                        } else {
                            system.setTransLevel("Others");
                        }
                    }*/
                    String type=messageType(system.getSms_msg()); 
                    system.setTransLevel(type);
                    system.setSms_status(resultSet.getString(11));
                    system.setSms_prepared_by(resultSet.getString(12));
                    system.setSms_send_date(resultSet.getString(13));
                    system.setPol_current_status(resultSet.getString(14));
                    system.setClientName(resultSet.getString(15));
                    system.setCouCode(resultSet.getString(16));
                    system.setCouZipCode(resultSet.getString(17));
                    system.setDivName(resultSet.getString(18));
                    system.setDatePrepared(resultSet.getDate(19));
                  
                    String phoneNumber = system.getSms_tel_no();
                    phoneNumber = phoneNumber!=null? phoneNumber.replaceAll("-","") : null;
                    system.setSms_tel_no(phoneNumber);
                    
                    String no_format = "^((\\+254)|0)\\d{9}$"; //kenya format
                    boolean correct_no=false;
                    if(!GlobalCC.isEmptyStr(phoneNumber)) 
                    {
                        if (phoneNumber.matches(no_format) ) 
                        {
                            correct_no=true;
                        }  
                    }
                    String sms=system.getSms_msg();
                    String client=system.getClientName() ;
                    if (message.equalsIgnoreCase("all")) {
                        systemsList.add(system);
                    } else if ("invalid".equalsIgnoreCase(message)) {
                        if (GlobalCC.isEmptyStr(sms) || GlobalCC.isEmptyStr(phoneNumber)|| (!correct_no) || GlobalCC.isEmptyStr(client) ) {
                            systemsList.add(system);
                        }
                        
                    } else if ("valid".equalsIgnoreCase(message)) {
                        if(!GlobalCC.isEmptyStr(phoneNumber) && correct_no && !GlobalCC.isEmptyStr(sms) && !GlobalCC.isEmptyStr(client)) {
                                systemsList.add(system);
                        }
                        
                    } else {
                        systemsList.add(system);
                    }
                    phoneNumber = null;
                    no_format = null;
                }
                java.lang.System.out.println("Message Validity: "+message+" Total: "+systemsList.size());
                statement.close();
                resultSet.close();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }

        return systemsList;
    }

    public List<MessageTemplate> fetchMessageTemplatesByType() {
        List<MessageTemplate> msgTemplateList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SETUPS_CURSOR.get_em_template_by_type(?,?); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            msgTemplateList = new ArrayList<MessageTemplate>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("sysCode"));
            statement.setObject(3, session.getAttribute("messageType"));
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                MessageTemplate messageTemplate = new MessageTemplate();
                messageTemplate.setMsgtCode(resultSet.getBigDecimal(1));
                messageTemplate.setMsgtShtDesc(resultSet.getString(2));
                messageTemplate.setMsgtMsg(resultSet.getString(3));
                messageTemplate.setMsgtSysCode(resultSet.getBigDecimal(4));
                messageTemplate.setMsgtSysModule(resultSet.getString(5));
                messageTemplate.setMsgtType(resultSet.getString(6));
                msgTemplateList.add(messageTemplate);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return msgTemplateList;
    }

    /**
     * Fetch a list of all the <code>SmsMessage</code> objects/records with a
     * given code.
     *
     * @return A list of all <code>SmsMessage</code> objects/records
     */
    public List<SmsMessage> fetchAllSmsMessages() {
        List<SmsMessage> smsMessageList = null;
        DBConnector dbConnector = new DBConnector();
        String query = "begin ? := TQC_SMS_WEB_CURSOR.getSmsMessages(); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            smsMessageList = new ArrayList<SmsMessage>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                SmsMessage smsMessage = new SmsMessage();
                smsMessage.setSmsCode(resultSet.getBigDecimal(1));
                smsMessage.setSmsSysCode(resultSet.getBigDecimal(2));
                smsMessage.setSmsSysModule(resultSet.getString(3));
                smsMessage.setSmsClientCode(resultSet.getBigDecimal(4));
                smsMessage.setSmsAgnCode(resultSet.getBigDecimal(5));
                smsMessage.setSmsPolCode(resultSet.getBigDecimal(6));
                smsMessage.setSmsPolNo(resultSet.getString(7));
                smsMessage.setSmsClmNo(resultSet.getString(8));
                smsMessage.setSmsTelNo(resultSet.getString(9));
                smsMessage.setSmsMsg(resultSet.getString(10));
                smsMessage.setSmsStatus(resultSet.getString(11));
                smsMessage.setSmsPreparedBy(resultSet.getString(12));
                smsMessage.setSmsPreparedDate(resultSet.getDate(13));
                smsMessage.setSmsSendDate(resultSet.getDate(14));
                smsMessage.setSmsQuotCode(resultSet.getBigDecimal(15));
                smsMessage.setSmsQuotNo(resultSet.getString(16));
                smsMessage.setSmsUsrCode(resultSet.getBigDecimal(17));
                smsMessage.setSystemName(resultSet.getString(18));
                smsMessage.setClientName(resultSet.getString(19));
                smsMessage.setAgencyName(resultSet.getString(20));
                smsMessageList.add(smsMessage);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return smsMessageList;
    }

    /**
     * Fetch a list of all the <code>InboxMessage</code> objects/records with a
     * given code.
     *
     * @return A list of all <code>InboxMessage</code> objects/records
     */
    public List<InboxMessage> fetchNewInboxMessages() {
        List<InboxMessage> inboxMessageList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SMS_WEB_CURSOR.getInboxMessagesDrafts(); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            inboxMessageList = new ArrayList<InboxMessage>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                InboxMessage inboxMessage = new InboxMessage();
                inboxMessage.setIbxCode(resultSet.getBigDecimal(1));
                inboxMessage.setIbxSysCode(resultSet.getBigDecimal(2));
                inboxMessage.setIbxSysModule(resultSet.getString(3));
                inboxMessage.setIbxTelNo(resultSet.getString(4));
                inboxMessage.setIbxMessage(resultSet.getString(5));
                inboxMessage.setIbxStatus(resultSet.getString(6));
                inboxMessage.setIbxDate(resultSet.getDate(7));
                inboxMessage.setIbxAssignedDate(resultSet.getDate(8));
                inboxMessage.setIbxMessageReplied(resultSet.getString(9));
                inboxMessage.setIbxUserInformed(resultSet.getString(10));
                inboxMessage.setIbxAssignedTo(resultSet.getString(11));
                inboxMessageList.add(inboxMessage);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return inboxMessageList;
    }

    /**
     * Fetch a list of all the <code>InboxMessage</code> objects/records with a
     * given code.
     *
     * @return A list of all <code>InboxMessage</code> objects/records
     */
    public List<InboxMessage> fetchAssignedInboxMessages() {
        List<InboxMessage> inboxMessageList = null;
        DBConnector dbConnector = new DBConnector();
        String query =
            "begin ? := TQC_SMS_WEB_CURSOR.getInboxMessagesAssigned(); end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            inboxMessageList = new ArrayList<InboxMessage>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                InboxMessage inboxMessage = new InboxMessage();
                inboxMessage.setIbxCode(resultSet.getBigDecimal(1));
                inboxMessage.setIbxSysCode(resultSet.getBigDecimal(2));
                inboxMessage.setIbxSysModule(resultSet.getString(3));
                inboxMessage.setIbxTelNo(resultSet.getString(4));
                inboxMessage.setIbxMessage(resultSet.getString(5));
                inboxMessage.setIbxStatus(resultSet.getString(6));
                inboxMessage.setIbxDate(resultSet.getDate(7));
                inboxMessage.setIbxAssignedDate(resultSet.getDate(8));
                inboxMessage.setIbxMessageReplied(resultSet.getString(9));
                inboxMessage.setIbxUserInformed(resultSet.getString(10));
                inboxMessage.setIbxAssignedTo(resultSet.getString(11));
                inboxMessageList.add(inboxMessage);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return inboxMessageList;
    }

    public List<AlertType> fetchAlertTypes() {
        List<AlertType> alertTypesList = null;
        DBConnector dbConnector = new DBConnector();

        String query = "begin  Tqc_Sms_Pkg.get_AlertType(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            alertTypesList = new ArrayList<AlertType>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("SystemCode"));
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);

            while (resultSet.next()) {
                AlertType alertType = new AlertType();
                alertType.setALRT_CODE(resultSet.getBigDecimal(1));
                alertType.setALRT_TYPE(resultSet.getString(2));
                alertType.setALRT_SYS_CODE(resultSet.getBigDecimal(3));
                alertType.setALRT_EMAIL(resultSet.getString((4)));
                alertType.setALRT_SMS(resultSet.getString((5)));
                alertType.setSysName(resultSet.getString((6)));
                alertType.setAlertScreen(resultSet.getString(7));
                alertType.setAlertEmailMsgCode(resultSet.getBigDecimal(8));
                alertType.setAlertSmsMsgtCode(resultSet.getBigDecimal((9)));
                alertType.setAlertGrpUsrCode(resultSet.getBigDecimal((10)));
                alertType.setAlertEmail(resultSet.getString((11)));
                alertType.setAlertSms(resultSet.getString((12)));
                alertType.setGrpUsers(resultSet.getString((13)));
                alertType.setAlertCheckAlert(resultSet.getString((14)));
                alertType.setAlrtshtDesc(resultSet.getString((15)));
                alertTypesList.add(alertType);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return alertTypesList;
    }

    public List<Alert> fetchAlerts() {
        List<Alert> alertList = null;
        DBConnector dbConnector = new DBConnector();

        String query = "begin  Tqc_Sms_Pkg.get_Alert(?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        alertList = new ArrayList<Alert>();
        if (session.getAttribute("alertType") != null) {
            try {

                connection = dbConnector.getDatabaseConnection();
                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                statement.setBigDecimal(1,
                                        new BigDecimal(session.getAttribute("alertType").toString()));
                statement.registerOutParameter(2, OracleTypes.CURSOR);
                statement.execute();
                OracleResultSet resultSet =
                    (OracleResultSet)statement.getObject(2);

                while (resultSet.next()) {
                    Alert alert = new Alert();
                    alert.setCode(resultSet.getBigDecimal(1));
                    alert.setAlrt_code(resultSet.getBigDecimal(2));
                    alert.setDescription(resultSet.getString(6));
                    alert.setDate(resultSet.getDate(7));
                    alert.setPeriod(resultSet.getBigDecimal(8));
                    alert.setDest_type(resultSet.getString(10));
                    alert.setDest_code(resultSet.getBigDecimal(11));
                    alert.setMsgt_code(resultSet.getBigDecimal(12));
                    alert.setAgency_name(resultSet.getString(13));
                    alert.setTemplate_sht_desc(resultSet.getString(14));
                    alert.setAcc_name(resultSet.getString(15));
                    alert.setStatus(resultSet.getString(16));
                    alert.setShort_desc(resultSet.getString(17));

                    alertList.add(alert);
                }

                resultSet.close();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }
        return alertList;
    }
    
    public List<AlertType> fetchDispatchDocuments() {
        List<AlertType> dispatchDocumentsList = null;
        DBConnector dbConnector = new DBConnector();

        String query = "begin  Tqc_Sms_Pkg.alert_dispatch_documents_prc(?,?,?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;

        try {
            dispatchDocumentsList = new ArrayList<AlertType>();
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);

            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.setObject(2, session.getAttribute("SystemCode"));
            
            if(session.getAttribute("alertCode")!=null){
                statement.setObject(3, session.getAttribute("alertCode"));//ensure you set this variable(session)
            }else{
                statement.setObject(3, null);
            }
            
            if( session.getAttribute("shtDesc")!=null){
                statement.setObject(4, session.getAttribute("shtDesc"));
            }else{
                statement.setObject(4, null);
            }
            statement.execute();
            OracleResultSet resultSet =
                (OracleResultSet)statement.getObject(1);
        
            while (resultSet.next()) {
                AlertType alertType = new AlertType();
                alertType.setDocCode(resultSet.getBigDecimal(1));
                alertType.setDaAlertCode(resultSet.getBigDecimal(2));
                alertType.setDispatchDocCode(resultSet.getBigDecimal(3));
                alertType.setDocShtDesc(resultSet.getString(4));
                alertType.setDocDescription(resultSet.getString(5));
                dispatchDocumentsList.add(alertType);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        return dispatchDocumentsList;
    }
    
    public List<AlertType> findDispatchDocuments() {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn = null;
            conn = datahandler.getDatabaseConnection();
            OracleResultSet rs = null;
            OracleCallableStatement cst = null;
            List<AlertType> docsList = new ArrayList<AlertType>();
            BigDecimal currCode = (BigDecimal)session.getAttribute("currCode");
            try {                
                String dispatchDocs =
                    "begin tqc_web_pkg.get_dispatch_docs(?); end;";

                cst = (OracleCallableStatement)conn.prepareCall(dispatchDocs);
                cst.registerOutParameter(1,OracleTypes.CURSOR);
                cst.execute();

                rs = (OracleResultSet)cst.getObject(1);
                while (rs.next()) {
                    AlertType newDispatchDoc = new AlertType();
                    newDispatchDoc.setDispatchDocCode(rs.getBigDecimal(1));
                    newDispatchDoc.setDispatchDocRptDesc(rs.getString(2));
                    docsList.add(newDispatchDoc);
                }
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
            return docsList;
        }
}
