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

package TurnQuest.view.backing;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.MessageTemplate;

import java.math.BigDecimal;

import java.net.HttpURLConnection;
import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class MessageTemplateBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblSystems;
    private RichTable tblMessageTemplate;
    private RichInputText txtMsgtCode;
    private RichInputText txtMsgtShtDesc;
    private RichInputText txtMsgtMsg;
    private RichInputText txtMsgtSysCode;
    private RichSelectOneChoice txtMsgtSysModule;
    private RichSelectOneChoice txtMsgtType;
    private RichCommandButton btnSaveUpdateMsgTemplate;
    private RichTable tblMessages;
    private RichTable tblSys;
    private RichSelectBooleanCheckbox rowChecked;
    private RichSelectBooleanCheckbox checked;
    private RichInputText txtSmsCode;
    private RichInputText txtSmsClientCode;
    private RichInputText txtSmsClientName;
    private RichInputText txtSmsTelNo;
    private RichInputText txtSearchShortDesc;
    private RichInputText txtSearchName;
    private RichInputText txtSearchOtherName;
    private RichInputText txtSearchPostal;
    private RichInputText txtSearchPhysical;
    private RichSelectBooleanRadio rbtnShortDesc;
    private RichSelectBooleanRadio rbtnPhySicalAddr;
    private RichSelectBooleanRadio rbtnPostalAddr;
    private RichSelectBooleanRadio rbtnPartOfAnyName;
    private RichSelectBooleanRadio rbtnExactName;
    private RichTable tblClientPop;
    private RichInputText txtSmsMessageTemplate;
    private RichInputText txtSmsMsgTemplateCode;
    private RichTable tblmMsgTemplate;
    private RichInputText txtSmsMsg;
    private RichInputText txtSmsMsgDtls;
    private RichSelectOneChoice txtPrefix;
    private RichTable smsTable;
    private RichTable emailTbl;
    private RichInputDate msgWEF;
    private RichInputDate msgWET;
    private RichSelectOneChoice msgTransType;
    private RichInputText txtEmailCode;
    private RichInputText txtEmailAddress;
    private RichInputText txtSubject;
    private RichTable messageProductsTbl;
    private RichInputText txtMessageProduct;
    private RichInputText txtMsgSubject;


    public MessageTemplateBacking() {
    }

    public void setTblSystems(RichTable tblSystems) {
        this.tblSystems = tblSystems;
    }

    public RichTable getTblSystems() {
        return tblSystems;
    }

    public void setTblMessageTemplate(RichTable tblMessageTemplate) {
        this.tblMessageTemplate = tblMessageTemplate;
    }

    public RichTable getTblMessageTemplate() {
        return tblMessageTemplate;
    }

    public void setTxtMsgtCode(RichInputText txtMsgtCode) {
        this.txtMsgtCode = txtMsgtCode;
    }

    public RichInputText getTxtMsgtCode() {
        return txtMsgtCode;
    }

    public void setTxtMsgtShtDesc(RichInputText txtMsgtShtDesc) {
        this.txtMsgtShtDesc = txtMsgtShtDesc;
    }

    public RichInputText getTxtMsgtShtDesc() {
        return txtMsgtShtDesc;
    }

    public void setTxtMsgtMsg(RichInputText txtMsgtMsg) {
        this.txtMsgtMsg = txtMsgtMsg;
    }

    public RichInputText getTxtMsgtMsg() {
        return txtMsgtMsg;
    }

    public void setTxtMsgtSysCode(RichInputText txtMsgtSysCode) {
        this.txtMsgtSysCode = txtMsgtSysCode;
    }

    public RichInputText getTxtMsgtSysCode() {
        return txtMsgtSysCode;
    }

    public void setTxtMsgtSysModule(RichSelectOneChoice txtMsgtSysModule) {
        this.txtMsgtSysModule = txtMsgtSysModule;
    }

    public RichSelectOneChoice getTxtMsgtSysModule() {
        return txtMsgtSysModule;
    }

    public void setTxtMsgtType(RichSelectOneChoice txtMsgtType) {
        this.txtMsgtType = txtMsgtType;
    }

    public RichSelectOneChoice getTxtMsgtType() {
        return txtMsgtType;
    }

    public void setBtnSaveUpdateMsgTemplate(RichCommandButton btnSaveUpdateMsgTemplate) {
        this.btnSaveUpdateMsgTemplate = btnSaveUpdateMsgTemplate;
    }

    public RichCommandButton getBtnSaveUpdateMsgTemplate() {
        return btnSaveUpdateMsgTemplate;
    }

    public void tblSystemsListener(SelectionEvent selectionEvent) {
        Object key2 = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));

            // Hold Reference to the selected system message template
            session.setAttribute("sysShortDesc",
                                 nodeBinding.getAttribute("shortDesc"));

            ADFUtils.findIterator("fetchMessageTemplatesBySystemIterator").executeQuery();
            ADFUtils.findIterator("fetchMessageProductsIterator").executeQuery();
            GlobalCC.refreshUI(tblMessageTemplate);
            GlobalCC.refreshUI(messageProductsTbl);
            // ADFUtils.findIterator("fetchMessageTemplatesBySystemIterator").executeQuery();
            //GlobalCC.refreshUI(tblMessages);
        }
    }

    public void clearMsgtemplateFields() {
        txtMsgtCode.setValue(null);
        txtMsgtShtDesc.setValue(null);
        txtMsgtMsg.setValue(null);
        txtMsgtSysCode.setValue(session.getAttribute("sysCode"));
        //txtMsgtSysModule.setValue( 'U' );
        //txtMsgtType.setValue( 'SMS' );
        txtMsgSubject.setValue(null);
        
        session.setAttribute("sysProdCode", null);
        session.setAttribute("sysProdDesc", null);

        txtMessageProduct.setValue(session.getAttribute("sysProdDesc"));
        GlobalCC.refreshUI(txtMessageProduct);

        btnSaveUpdateMsgTemplate.setText("Save");
    }

    public String actionNewMsgTemplate() {
        if (session.getAttribute("sysCode") != null) {
            clearMsgtemplateFields();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:msgTemplatePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing System to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditMsgTemplate() {
        Object key2 = tblMessageTemplate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtMsgtCode.setValue(nodeBinding.getAttribute("msgtCode"));
            txtMsgtShtDesc.setValue(nodeBinding.getAttribute("msgtShtDesc"));
            txtMsgtMsg.setValue(nodeBinding.getAttribute("msgtMsg"));
            txtMsgtSysCode.setValue(nodeBinding.getAttribute("msgtSysCode"));
            txtMsgtSysModule.setValue(nodeBinding.getAttribute("msgtSysModule"));
            txtMsgtType.setValue(nodeBinding.getAttribute("msgtType"));
            txtMessageProduct.setValue(nodeBinding.getAttribute("msgtSysProdName"));
            txtMsgSubject.setValue(nodeBinding.getAttribute("msgtSubject"));
            session.setAttribute("sysProdDesc", nodeBinding.getAttribute("msgtSysProdName"));
            session.setAttribute("sysProdCode", nodeBinding.getAttribute("msgtSysProd"));

            //btnSaveUpdateMsgTemplate.setText("Edit");
            btnSaveUpdateMsgTemplate.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:msgTemplatePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionDeleteMsgTemplate() {
        Object key2 = tblMessageTemplate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("msgtCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.messageTemplates_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_MSG_TEMPLATES_TAB",
                                                     conn);
                ArrayList templateList = new ArrayList();
                MessageTemplate messageTemplate = new MessageTemplate();
                messageTemplate.setSQLTypeName("TQC_MSG_TEMPLATES_OBJ");

                messageTemplate.setMsgtCode(new BigDecimal(code));

                templateList.add(messageTemplate);
                ARRAY array =
                    new ARRAY(descriptor, conn, templateList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchMessageTemplatesBySystemIterator").executeQuery();
                GlobalCC.refreshUI(tblMessageTemplate);

                clearMsgtemplateFields();

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateMsgTemplate() {
        if (btnSaveUpdateMsgTemplate.getText().equals("Edit")) {
            actionUpdateMsgTemplate();
        } else {
            String code = GlobalCC.checkNullValues(txtMsgtCode.getValue());
            String shtDesc =
                GlobalCC.checkNullValues(txtMsgtShtDesc.getValue());
            String msg = GlobalCC.checkNullValues(txtMsgtMsg.getValue());
            String sysCode =
                GlobalCC.checkNullValues(txtMsgtSysCode.getValue());
            String sysModule =
                GlobalCC.checkNullValues(txtMsgtSysModule.getValue());
            String type = GlobalCC.checkNullValues(txtMsgtType.getValue());

            BigDecimal sysProdCode = (BigDecimal)session.getAttribute("sysProdCode");
            String msgtSubject = GlobalCC.checkNullValues(txtMsgSubject.getValue());

            if (shtDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Message Code is Empty");
                return null;

            } else if (sysCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
                return null;

            } else if (type == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Message Type is Empty");
                return null;
            }else if(msgtSubject == null){
                GlobalCC.errorValueNotEntered("Error Value Missing: Message Subject is Empty");
            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn = dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.messageTemplates_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_MSG_TEMPLATES_TAB",
                                                         conn);
                    ArrayList templateList = new ArrayList();
                    MessageTemplate messageTemplate = new MessageTemplate();
                    messageTemplate.setSQLTypeName("TQC_MSG_TEMPLATES_OBJ");

                    messageTemplate.setMsgtCode(code == null ? null :
                                                new BigDecimal(code));
                    messageTemplate.setMsgtShtDesc(shtDesc);
                    messageTemplate.setMsgtMsg(msg);
                    messageTemplate.setMsgtSysCode(sysCode == null ? null :
                                                   new BigDecimal(sysCode));
                    messageTemplate.setMsgtSysModule(sysModule);
                    messageTemplate.setMsgtType(type);
                    messageTemplate.setMsgtSysProd(sysProdCode);

                    String username =
                        GlobalCC.checkNullValues(session.getAttribute("Username"));

                    messageTemplate.setMsgtCreatedBy(username);
                    messageTemplate.setMsgtUpdatedBy(null);
                    messageTemplate.setMsgtSubject(msgtSubject);

                    templateList.add(messageTemplate);
                    ARRAY array =
                        new ARRAY(descriptor, conn, templateList.toArray());

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "A");
                    statement.setArray(2, array);
                    statement.execute();

                    statement.close();
                    conn.commit();
                    conn.close();

                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:msgTemplatePop" +
                                         "').hide(hints);");

                    ADFUtils.findIterator("fetchMessageTemplatesBySystemIterator").executeQuery();
                    GlobalCC.refreshUI(tblMessageTemplate);

                    clearMsgtemplateFields();

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateMsgTemplate() {
        String code = GlobalCC.checkNullValues(txtMsgtCode.getValue());
        String shtDesc = GlobalCC.checkNullValues(txtMsgtShtDesc.getValue());
        String msg = GlobalCC.checkNullValues(txtMsgtMsg.getValue());
        String sysCode = GlobalCC.checkNullValues(txtMsgtSysCode.getValue());
        String sysModule =
            GlobalCC.checkNullValues(txtMsgtSysModule.getValue());
        String type = GlobalCC.checkNullValues(txtMsgtType.getValue());
        
        BigDecimal sysProdCode = (BigDecimal)session.getAttribute("sysProdCode");
        String msgtSubject = GlobalCC.checkNullValues(txtMsgSubject.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Template Code is Empty");
            return null;

        } else if (shtDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Message Code is Empty");
            return null;

        } else if (sysCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
            return null;

        } else if (type == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Message Type is Empty");
            return null;
        }else if(msgtSubject == null){
            GlobalCC.errorValueNotEntered("Error Value Missing: Message Subject is Empty");
            return null;
        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.messageTemplates_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_MSG_TEMPLATES_TAB",
                                                     conn);
                ArrayList templateList = new ArrayList();
                MessageTemplate messageTemplate = new MessageTemplate();
                messageTemplate.setSQLTypeName("TQC_MSG_TEMPLATES_OBJ");

                messageTemplate.setMsgtCode(code == null ? null :
                                            new BigDecimal(code));
                messageTemplate.setMsgtShtDesc(shtDesc);
                messageTemplate.setMsgtMsg(msg);
                messageTemplate.setMsgtSysCode(sysCode == null ? null :
                                               new BigDecimal(sysCode));
                messageTemplate.setMsgtSysModule(sysModule);
                messageTemplate.setMsgtType(type);
                messageTemplate.setMsgtSysProd(sysProdCode);
                messageTemplate.setMsgtSubject(msgtSubject);
                
                String username =
                    GlobalCC.checkNullValues(session.getAttribute("Username"));

                messageTemplate.setMsgtCreatedBy(null);
                messageTemplate.setMsgtUpdatedBy(username);
                templateList.add(messageTemplate);
                ARRAY array =
                    new ARRAY(descriptor, conn, templateList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:msgTemplatePop" + "').hide(hints);");

                ADFUtils.findIterator("fetchMessageTemplatesBySystemIterator").executeQuery();
                GlobalCC.refreshUI(tblMessageTemplate);

                clearMsgtemplateFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String selectAllAction() {
        int i = 0;
        while (i < tblMessages.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)tblMessages.getRowData(i);
            r.setAttribute("selected", true);
            rowChecked.setSelected(true);
            GlobalCC.refreshUI(rowChecked);
            i++;
        }
        return null;
    }

    public String unselectAllAction() {
        int i = 0;
        while (i < tblMessages.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)tblMessages.getRowData(i);
            if (r == null)
                continue;
            r.setAttribute("selected", false);
            rowChecked.setSelected(false);
            GlobalCC.refreshUI(rowChecked);
            i++;
        }
        return null;
    }

    public void setTblMessages(RichTable tblMessages) {
        this.tblMessages = tblMessages;
    }

    public RichTable getTblMessages() {
        return tblMessages;
    }

    public void tblSystemsListn(SelectionEvent selectionEvent) {
        Object key2 = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
            GlobalCC.refreshUI(tblMessages);

            ADFUtils.findIterator("findMsgTemplateIterator").executeQuery();
            GlobalCC.refreshUI(tblmMsgTemplate);
        }
    }

    public void setTblSys(RichTable tblSys) {
        this.tblSys = tblSys;
    }

    public RichTable getTblSys() {
        return tblSys;
    }

    public void setRowChecked(RichSelectBooleanCheckbox rowChecked) {
        this.rowChecked = rowChecked;
    }

    public RichSelectBooleanCheckbox getRowChecked() {
        return rowChecked;
    }

    /*
    public String AuthoriseMessage() {
        boolean messageSent = false;
        String url = null;
        try {
            JUCtrlValueBinding r = null;
            int rowCount2 = tblMessages.getRowCount();
            int count = 0;
            for (int i = 0; i < rowCount2; i++) {
                Boolean Accept = false;
                r = (JUCtrlValueBinding)tblMessages.getRowData(i);
                Accept = (Boolean)r.getAttribute("selected");
                if (Accept) {
                    String phoneNumber = null;
                    if (r.getAttribute("sms_tel_no") != null) {
                        phoneNumber = r.getAttribute("sms_tel_no").toString();
                    }
                    String regex = "^[+0-9]{10,25}$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = null;
                    boolean matches = false;
                    if (phoneNumber != null) {
                        matcher = pattern.matcher(phoneNumber);
                        matches = matcher.matches();
                    }
                    System.out.println("sms_msg == "+r.getAttribute("sms_msg")+
                                       " matches == "+matches+
                                       " Client == "+r.getAttribute("clientName")+
                                       "  phoneNumber == "+phoneNumber);
                    if (r.getAttribute("sms_msg") == null || !matches ||
                        phoneNumber == null ||
                        r.getAttribute("clientName") == null) {
                        GlobalCC.errorValueNotEntered("Selected Message and/or Phone Number for Client: " +
                                                      r.getAttribute("clientName") +
                                                      " Policy Number: " +
                                                      r.getAttribute("sms_pol_no") +
                                                      "is Invalid.");
                        return null;
                    }

                    count++;
                }
            }
            if (count == 0) {
                GlobalCC.errorValueNotEntered("No Message Selected");
                return null;
            }
            BigDecimal ExceptionSel = null;
            for (int i = 0; i < rowCount2; i++) {
                url = null;
                Boolean Accept = false;
                r = (JUCtrlValueBinding)tblMessages.getRowData(i);
                System.out.println("" + r);
                if (r == null)
                    continue;
                Accept = (Boolean)r.getAttribute("selected");
                if (Accept) {
                    session.setAttribute("sms_status",
                                         r.getAttribute("sms_status"));

                    BigDecimal except =
                        new BigDecimal((String)r.getAttribute("sms_code"));

                    DBConnector datahandler = new DBConnector();
                    DBConnector dbConnector = new DBConnector();
                    OracleConnection connection = null;
                    OracleCallableStatement stmt = null;
                    OracleResultSet rs = null;
                    try {


                        connection = dbConnector.getDatabaseConnection();
                        String query1 =
                            "begin ? := tqc_setups_cursor.getSmsSettings(); end;";
                        stmt =
(OracleCallableStatement)connection.prepareCall(query1);
                        stmt.registerOutParameter(1, OracleTypes.CURSOR);
                        stmt.execute();
                        rs = (OracleResultSet)stmt.getObject(1);
                        int k = 0;
                        while (rs.next()) {
                            String message = (String)r.getAttribute("sms_msg");
                            if (!GlobalCC.isEmptyStr(message)) {

                             //url ="http://104.155.25.147/MInteract/smsapi?username=[USERNAME]&destination=[DESTINATION]&message=[MESSAGE]&password=[PASSWORD]&timestamp=[TIMESTAMP]";

                               message = java.net.URLEncoder.encode(message,"UTF-8");
                               url = rs.getString(2);
                               String username=rs.getString(3);
                               String password=rs.getString(4);
                               String src=rs.getString(5);
                               String dest= (String)r.getAttribute("sms_tel_no");
                               String timestamp=GlobalCC.timeStamp("yyyyMMddHHmmss");//"20150922153700";

                              //contains timestamp?
                               if(url.indexOf("[TIMESTAMP]")>=0)
                               {
                                   password=Crypto.MD5(username+password+timestamp);
                               }

                               url = url.replace("[USERNAME]", username);
                               url = url.replace("[PASSWORD]", password);
                               url = url.replace("[SOURCE]", src);
                               url = url.replace("[MESSAGE]", message);
                               url = url.replace("[TIMESTAMP]", timestamp);
                               url = url.replace("[DESTINATION]", dest);

                              System.out.println("This is url " + url);
                              k++;
                        }

                        rs.close();
                        stmt.close();
                        connection.close();
//                        URL urls = new URL(url);
//                        System.out.println(url);
//                        HttpsURLConnection conns =
//                            (HttpsURLConnection)urls.openConnection();
 //-------------------------------------------------------------

                        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                        };

                        // Install the all-trusting trust manager
                        SSLContext sc = SSLContext.getInstance("SSL");
                        sc.init(null, trustAllCerts, new java.security.SecureRandom());
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                        // Create all-trusting host name verifier
                        HostnameVerifier allHostsValid = new HostnameVerifier() {
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }


                        };


                               //String https_url = "https://41.72.211.83/Bimaserve_Interface/API/?ClientID=1&AUTHKEY=3EQL86ITI2&service=2&sender=MADISON&phone=254720357230&message=Test%20Message&type=NOTIFICATION";
                              String https_url =url;
                              URL url1;
                              try {
                                  if(url.toLowerCase().contains("https")){
                                     url1 = new URL(https_url);
                                     HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                                     HttpsURLConnection conns = (HttpsURLConnection)url1.openConnection();
                                      conns.setDoOutput(false);
                                      conns.setDoInput(true);
                                      String res = conns.getResponseMessage();
                                      System.out.println("This is response" + res);
                                      int rsCode = conns.getResponseCode();
                                      if (rsCode == HttpURLConnection.HTTP_OK) {
                                          conns.disconnect();
                                      }
                                  }else{
                                      URL url2 = new URL(url);
                                      //URLEncoder.encode(arg0, arg1)
                                      HttpURLConnection connection2 =
                                          (HttpURLConnection)url2.openConnection();
                                      connection2.setDoOutput(false);
                                      connection2.setDoInput(true);
                                      String res = connection2.getResponseMessage();
                                      System.out.println("This is response" + res);
                                      int rsCode1 = connection2.getResponseCode();
                                      if (rsCode1 == HttpURLConnection.HTTP_OK) {
                                          connection2.disconnect();
                                      }
                                  }



                                  OracleCallableStatement cst = null;
                                  String Binders =
                                      "begin tqc_setups_pkg.smsStatus(?,?); end;";
                                  OracleConnection conn;
                                  conn = datahandler.getDatabaseConnection();
                                  cst =
                                  (OracleCallableStatement)conn.prepareCall(Binders);
                                  cst.setString(1, "OK");
                                  cst.setBigDecimal(2, except);
                                  cst.execute();
                                  conn.close();


                              } catch (MalformedURLException e) {
                                     e.printStackTrace();
                              } catch (IOException e) {
                                     e.printStackTrace();
                              }


                        //--------------------------------------------------
//                        conns.setDoOutput(false);
//                        conns.setDoInput(true);
//                        String res = conns.getResponseMessage();
//                        System.out.println("This is response" + res);


                    } catch (Exception e) {
                        e.printStackTrace();
                        GlobalCC.EXCEPTIONREPORTING(connection, e);
                    }
                }
            }
            GlobalCC.INFORMATIONREPORTING("Message(s) has been sent...");
            ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
            GlobalCC.refreshUI(tblMessages);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;

    }*/

    public String AuthoriseMessage() {
        boolean messageSent = false;

        OracleResultSet rs = null;
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            JUCtrlValueBinding r = null;
            int rowCount2 = this.tblMessages.getRowCount();
            int count = 0;
            String sql = "begin TQC_SMS_PKG.send_instant_sms (?); end; ";
            cst = (OracleCallableStatement)conn.prepareCall(sql);

            for (int i = 0; i < rowCount2; i++) {
                Boolean Accept = Boolean.valueOf(false);
                r = (JUCtrlValueBinding)this.tblMessages.getRowData(i);
                System.out.println("Sending sms: i:" + i + " r: " + r);
                if (r != null) {
                    Accept = (Boolean)r.getAttribute("selected");
                    if (Accept.booleanValue()) {
                        session.setAttribute("sms_status",
                                             r.getAttribute("sms_status"));
                        BigDecimal smsCode =
                            GlobalCC.checkBDNullValues(r.getAttribute("sms_code"));
                        cst.setBigDecimal(1, smsCode);
                        cst.addBatch();
                        count++;
                    }
                }
            }
            ;
            if (count == 0) {
                GlobalCC.errorValueNotEntered("No Message Selected");
                return null;
            }
            cst.executeBatch();
            conn.commit();
            ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
            GlobalCC.refreshUI(this.tblMessages);
            GlobalCC.INFORMATIONREPORTING(count +
                                          " Message(s) has been sent...");
            System.out.println("Sms Count: " + rowCount2);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, rs);
        }
        return null;
    }

    //    public String AuthoriseMessage() {
    //        boolean messageSent = false;
    //
    //        OracleConnection connection = null;
    //        OracleCallableStatement stmt = null;
    //        OracleResultSet rs = null;
    //        try {
    //          JUCtrlValueBinding r = null;
    //          int rowCount2 = this.tblMessages.getRowCount();
    //          int count = 0;
    //          for (int i = 0; i < rowCount2; i++) {
    //            Boolean Accept = Boolean.valueOf(false);
    //            r = (JUCtrlValueBinding)this.tblMessages.getRowData(i);
    //            Accept = (Boolean)r.getAttribute("selected");
    //            if (Accept.booleanValue()) {
    //              String phoneNumber = null;
    //              if (r.getAttribute("sms_tel_no") != null) {
    //                phoneNumber = r.getAttribute("sms_tel_no").toString();
    //              }
    //              String regex = "^[+0-9]{10,25}$";
    //              Pattern pattern = Pattern.compile(regex);
    //              Matcher matcher = null;
    //              boolean matches = false;
    //              if (phoneNumber != null) {
    //                matcher = pattern.matcher(phoneNumber);
    //                matches = matcher.matches();
    //              }
    //
    //              if ((r.getAttribute("sms_msg") == null) || (!matches) || (phoneNumber == null) || (r.getAttribute("clientName") == null))
    //              {
    //                GlobalCC.errorValueNotEntered("Selected Message and/or Phone Number for Client: " + r.getAttribute("clientName") + " Policy Number: " + r.getAttribute("sms_pol_no") + "is Invalid.");
    //
    //                return null;
    //              }
    //
    //              count++;
    //            }
    //          }
    //          if (count == 0) {
    //            GlobalCC.errorValueNotEntered("No Message Selected");
    //            return null;
    //          }
    //
    //          DBConnector datahandler = new DBConnector();
    //          DBConnector dbConnector = new DBConnector();
    //          connection = dbConnector.getDatabaseConnection();
    //          String query1 = "begin ? := tqc_setups_cursor.getSmsSettings(); end;";
    //
    //          stmt = (OracleCallableStatement)connection.prepareCall(query1);
    //
    //          stmt.registerOutParameter(1, -10);
    //          stmt.execute();
    //          rs = (OracleResultSet)stmt.getObject(1);
    //
    //          if (rs.next())
    //          {
    //            BigDecimal ExceptionSel = null;
    //
    //            String vUrl = GlobalCC.checkNullValues(rs.getString(2));
    //            if (vUrl == null) {
    //              GlobalCC.errorValueNotEntered("No Sms Url Set!");
    //              return null;
    //            }
    //            String username = GlobalCC.checkNullValues(rs.getString(3));
    //            String password = GlobalCC.checkNullValues(rs.getString(4));
    //            String src = GlobalCC.checkNullValues(rs.getString(5));
    //
    //            System.out.println("Sms Count: " +rowCount2);
    //            int k = 0;
    //            for (int i = 0; i < rowCount2; i++) {
    //              Boolean Accept = Boolean.valueOf(false);
    //              r = (JUCtrlValueBinding)this.tblMessages.getRowData(i);
    //              System.out.println("Sending sms: i:" +i+" r: "+r);
    //
    //              if (r != null)
    //              {
    //                Accept = (Boolean)r.getAttribute("selected");
    //                if (Accept.booleanValue()) {
    //                  this.session.setAttribute("sms_status", r.getAttribute("sms_status"));
    //                  BigDecimal except = new BigDecimal((String)r.getAttribute("sms_code"));
    //
    //                  String message = (String)r.getAttribute("sms_msg");
    //                  if (!GlobalCC.isEmptyStr(message))
    //                  {
    //                       //url ="http://104.155.25.147/MInteract/smsapi?username=[USERNAME]&destination=[DESTINATION]&message=[MESSAGE]&password=[PASSWORD]&timestamp=[TIMESTAMP]";
    //
    //                      message = java.net.URLEncoder.encode(message,"UTF-8");
    //                      String dest= (String)r.getAttribute("sms_tel_no");
    //                      String timestamp=GlobalCC.timeStamp("yyyyMMddHHmmss");//"20150922153700";
    //
    //                       String url=GlobalCC.checkNullValues(vUrl);
    //                       url = url.replace("[USERNAME]", username);
    //                       url = url.replace("[PASSWORD]", vUrl.indexOf("[TIMESTAMP]")>=0 ?Crypto.MD5(username+password+timestamp) : password );//contains timestamp?
    //                       url = url.replace("[SOURCE]", src);
    //                       url = url.replace("[MESSAGE]", message);
    //                       url = url.replace("[TIMESTAMP]", timestamp);
    //                       url = url.replace("[DESTINATION]", dest);
    //
    //                      System.out.println("sms url: " + url);
    //
    //                      URL urls = new URL(url);
    //                      HttpURLConnection conns = (HttpURLConnection)urls.openConnection();
    //                      conns.setDoOutput(false);
    //                      conns.setDoInput(true);
    //                      String res = conns.getResponseMessage();
    //
    //                      System.out.println("Sms Response: "+res);
    //
    //                       int rsCode = conns.getResponseCode();
    //                       System.out.println("Response Code: "+rsCode);
    //                      if (rsCode == 200)
    //                      {
    //                          k++;
    //                          conns.disconnect();
    //                          OracleCallableStatement cst = null;
    //                          String sql = "UPDATE tqc_sms_messages \n" +
    //                          "            SET sms_status = 'OK', \n" +
    //                          "                sms_send_date = SYSDATE, \n" +
    //                          "                sms_sent_response = ':v_sms_response' \n" +
    //                          "       WHERE sms_code = :v_sms_code ";
    //                          sql=sql.replace(":v_sms_response",String.valueOf(rsCode));
    //                          sql=sql.replace(":v_sms_code",except!=null? except.toString():"-1");
    //                          OracleConnection conn = datahandler.getDatabaseConnection();
    //                          cst = (OracleCallableStatement)conn.prepareCall(sql);
    //                          cst.execute();
    //                          conn.commit();
    //                          conn.close();
    //                      }
    //                  }
    //                }
    //              }
    //            }
    //            GlobalCC.INFORMATIONREPORTING(k+" Message(s) has been sent...");
    //            ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
    //            GlobalCC.refreshUI(this.tblMessages);
    //          }
    //        }
    //        catch (Exception e) {
    //          GlobalCC.EXCEPTIONREPORTING(e);
    //        }finally{
    //          DbUtils.closeQuietly(connection, stmt, rs);
    //        }
    //        return null;
    //      }

    public String DeleteMessage() {
        boolean messageSent = false;
        String url = null;
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;
        String Binders = "begin tqc_setups_pkg.smsStatus(?,?); end;";

        try {
            conn = datahandler.getDatabaseConnection();
            JUCtrlValueBinding r = null;
            int rowCount2 = tblMessages.getRowCount();
            int count = 0;

            for (int i = 0; i < rowCount2; i++) {
                url = null;
                Boolean Accept = false;
                r = (JUCtrlValueBinding)tblMessages.getRowData(i);
                if (r == null)
                    continue;
                if ((Boolean)r.getAttribute("selected")) {
                    String status = null;
                    if (r.getAttribute("sms_status") != null) {
                        status = r.getAttribute("sms_status").toString();
                    }
                    if (status.equalsIgnoreCase("Sent")) {
                        GlobalCC.errorValueNotEntered("Only Messages that have not been sent can be deleted.");
                        return null;
                    }

                    BigDecimal smsCode =
                        new BigDecimal((String)r.getAttribute("sms_code"));
                    System.out.println("Deleteing: " + smsCode + " Status: " +
                                       status);
                    OracleCallableStatement cst = null;
                    cst = (OracleCallableStatement)conn.prepareCall(Binders);
                    cst.setString(1, "D");
                    cst.setBigDecimal(2, smsCode);
                    cst.execute();
                    cst.close();

                }
            }
            GlobalCC.INFORMATIONREPORTING("Message(s) has been Deleted...");
            ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
            GlobalCC.refreshUI(tblMessages);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, null, null);
        }
        return null;

    }

    public String getAuthorisedMessages() {
        try {
            DBConnector datahandler = new DBConnector();
            DBConnector dbConnector = new DBConnector();
            OracleConnection connection = null;
            OracleCallableStatement stmt = null;
            OracleResultSet rs = null;
            connection = dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.selectallMessages(?,?,?,?);end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, 37);
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.setObject(5, null);
            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            int k = 0;
            while (rs.next()) {
                String message = rs.getString(9);
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getScheduledMessages() {
        System.out.println("This is OK");
        boolean messageSent = false;
        String message = null;
        String smsStatus = null;
        String messageToSend = null;
        String telNumber = null;
        String url = null;
        int count = 0;
        int rowCount2 = 0;
        try {
            System.out.println("This is OK 2");
            DBConnector datahandler = new DBConnector();
            DBConnector dbConnector = new DBConnector();
            OracleConnection connection = null;
            OracleCallableStatement stmt = null;
            OracleResultSet rs = null;
            connection = dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.selectallMessages(?,?,?,?);end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, 37);
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.setObject(5, null);
            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            int k = 0;
            while (rs.next()) {
                System.out.println("This is OK 4535");
                message = rs.getString(9);
                String clientName = rs.getString(15);
                String phoneNumber = null;
                if (message != null) {
                    phoneNumber = message;
                }
                String regex = "^[+0-9]{10,25}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = null;
                boolean matches = false;
                if (phoneNumber != null) {
                    matcher = pattern.matcher(phoneNumber);
                    matches = matcher.matches();
                }
                System.out.println("This is OK 4535ghfghfghf");
                if (message == null || !matches || phoneNumber == null ||
                    clientName == null) {
                    GlobalCC.errorValueNotEntered("Select Message for Client: " +
                                                  clientName +
                                                  " Policy Number: " +
                                                  message + "is Invalid.");
                    return null;
                }

                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count == 0) {
            GlobalCC.errorValueNotEntered("No Message Selected");
            return null;
        }
        try {
            System.out.println("This is OK fgfgdfg");
            DBConnector datahandler = new DBConnector();
            DBConnector dbConnector = new DBConnector();
            OracleConnection connection = null;
            OracleCallableStatement stmt = null;
            OracleResultSet rs = null;
            connection = dbConnector.getDatabaseConnection();
            String query =
                "begin ? := TQC_SETUPS_CURSOR.selectallMessages(?,?,?,?);end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, 37);
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.setObject(5, null);

            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            int k = 0;
            while (rs.next()) {
                System.out.println("This is OK gerttt");
                smsStatus = rs.getString(11);
                messageToSend = rs.getString(10);
                telNumber = rs.getString(9);
                session.setAttribute("sms_status", smsStatus);

                BigDecimal except =
                    new BigDecimal((String)rs.getString(1).toString());


                connection = dbConnector.getDatabaseConnection();
                String query13 =
                    "begin ? := tqc_setups_cursor.getSmsSettings(); end;";
                stmt =
(OracleCallableStatement)connection.prepareCall(query13);
                stmt.registerOutParameter(1, OracleTypes.CURSOR);
                stmt.execute();
                rs = (OracleResultSet)stmt.getObject(1);
                int j = 0;
                while (rs.next()) {
                    String messages = messageToSend;
                    if (messages == null) {

                    } else
                        messages = java.net.URLEncoder.encode(message);
                    url = rs.getString(2);
                    url = url.replace("[USERNAME]", rs.getString(3));
                    url = url.replace("[PASSWORD]", rs.getString(4));
                    url = url.replace("[SOURCE]", rs.getString(5));
                    url = url.replace("[MESSAGE]", message);
                    url = url.replace("[DESTINATION]", telNumber);
                    System.out.println("This is url" + url);
                    j++;
                }

                rs.close();
                stmt.close();
                connection.close();
                URL urls = new URL(url);
                HttpURLConnection conns =
                    (HttpURLConnection)urls.openConnection();
                conns.setDoOutput(false);
                conns.setDoInput(true);
                String res = conns.getResponseMessage();
                OracleCallableStatement cst = null;
                String Binders = "begin tqc_setups_pkg.smsStatus(?,?); end;";
                OracleConnection conn;
                conn = datahandler.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Binders);
                cst.setString(1, "OK");
                cst.setBigDecimal(2, except);
                cst.execute();
                conn.close();
                int rsCode = conns.getResponseCode();
                if (rsCode == HttpURLConnection.HTTP_OK) {
                    conns.disconnect();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        GlobalCC.INFORMATIONREPORTING("Message(s) has been sent...");
        ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
        GlobalCC.refreshUI(tblMessages);


        return null;

    }

    public void setChecked(RichSelectBooleanCheckbox checked) {
        this.checked = checked;
    }

    public RichSelectBooleanCheckbox getChecked() {
        return checked;
    }

    public String allMessages() {
        session.setAttribute("msg", "all");
        ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
        GlobalCC.refreshUI(tblMessages);
        return null;
    }

    public String invalidMessages() {
        session.setAttribute("msg", "invalid");
        ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
        GlobalCC.refreshUI(tblMessages);
        return null;
    }

    public String validMessages() {
        session.setAttribute("msg", "valid");
        ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
        GlobalCC.refreshUI(tblMessages);
        return null;
    }

    public void setTxtSmsCode(RichInputText txtSmsCode) {
        this.txtSmsCode = txtSmsCode;
    }

    public RichInputText getTxtSmsCode() {
        return txtSmsCode;
    }

    public void setTxtSmsClientCode(RichInputText txtSmsClientCode) {
        this.txtSmsClientCode = txtSmsClientCode;
    }

    public RichInputText getTxtSmsClientCode() {
        return txtSmsClientCode;
    }

    public void setTxtSmsClientName(RichInputText txtSmsClientName) {
        this.txtSmsClientName = txtSmsClientName;
    }

    public RichInputText getTxtSmsClientName() {
        return txtSmsClientName;
    }

    public void criteriaValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (rbtnShortDesc.isSelected()) {
                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);

                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);

                if (txtSearchShortDesc.isDisabled()) {
                    txtSearchShortDesc.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchShortDesc);

                }

                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                GlobalCC.refreshUI(txtSearchName);

                GlobalCC.refreshUI(txtSearchPhysical);
                GlobalCC.refreshUI(txtSearchPostal);
                GlobalCC.refreshUI(txtSearchOtherName);


            }

            else if (rbtnPhySicalAddr.isSelected()) {

                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);

                txtSearchShortDesc.setValue(null);


                txtSearchPostal.setValue(null);

                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);

                txtSearchShortDesc.setDisabled(true);

                if (txtSearchPhysical.isDisabled()) {
                    txtSearchPhysical.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchPhysical);

                }

                txtSearchPostal.setDisabled(true);


                GlobalCC.refreshUI(txtSearchName);
                GlobalCC.refreshUI(txtSearchOtherName);

                GlobalCC.refreshUI(txtSearchShortDesc);

                GlobalCC.refreshUI(txtSearchPostal);
            } else if (rbtnPostalAddr.isSelected()) {

                txtSearchName.setValue(null);
                txtSearchOtherName.setValue(null);
                txtSearchShortDesc.setValue(null);
                txtSearchPhysical.setValue(null);
                txtSearchName.setDisabled(true);
                txtSearchOtherName.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                if (txtSearchPostal.isDisabled()) {
                    txtSearchPostal.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchPostal);

                }
                GlobalCC.refreshUI(txtSearchName);
                GlobalCC.refreshUI(txtSearchOtherName);

                GlobalCC.refreshUI(txtSearchShortDesc);

                GlobalCC.refreshUI(txtSearchPhysical);

            }

            else if (rbtnExactName.isSelected()) {

                txtSearchShortDesc.setValue(null);

                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);


                txtSearchShortDesc.setValue(null);

                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);

                if (txtSearchName.isDisabled()) {
                    txtSearchName.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchName);

                }
                if (txtSearchOtherName.isDisabled()) {
                    txtSearchOtherName.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchOtherName);

                }

                txtSearchShortDesc.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                GlobalCC.refreshUI(txtSearchShortDesc);

                GlobalCC.refreshUI(txtSearchPhysical);
                GlobalCC.refreshUI(txtSearchPostal);
            } else if (rbtnPartOfAnyName.isSelected()) {

                txtSearchShortDesc.setValue(null);

                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);


                txtSearchShortDesc.setValue(null);

                txtSearchPhysical.setValue(null);
                txtSearchPostal.setValue(null);

                if (txtSearchName.isDisabled()) {
                    txtSearchName.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchName);

                }
                if (txtSearchOtherName.isDisabled()) {
                    txtSearchOtherName.setDisabled(false);
                    GlobalCC.refreshUI(txtSearchOtherName);

                }

                txtSearchShortDesc.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSearchPostal.setDisabled(true);
                GlobalCC.refreshUI(txtSearchShortDesc);

                GlobalCC.refreshUI(txtSearchPhysical);
                GlobalCC.refreshUI(txtSearchPostal);
            }


        }
    }

    public String actionAcceptSearchCriteria() {

        String name = null;
        String oName = null;
        String shtDesc = null;
        String clntType = null;
        String status = null;
        String postalAddr = null;
        String physicalAddr = null;
        String oldId = null;
        String sector =
            GlobalCC.checkNullValues(session.getAttribute("sectorCode"));
        String criteria = null;
        String criteria2 = null;
        String searchName = null;
        String searchOName = null;
        String searchPostalAddr = null;
        String searchPhysicalAddr = null;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String dFrom = null;
        String dTo = null;
        String fromDate = null;
        String searchOldId = null;
        String searchOldNames = null;

        String searchShtDesc =
            GlobalCC.checkNullValues(txtSearchShortDesc.getValue());
        String searchClntType =
            GlobalCC.checkNullValues(session.getAttribute("searchClntType"));
        String searchStatus =
            GlobalCC.checkNullValues(session.getAttribute("searchClntStatus"));
        String searchSector =
            GlobalCC.checkNullValues(session.getAttribute("sectorCode"));
        searchPhysicalAddr =
                GlobalCC.checkNullValues(txtSearchPhysical.getValue());
        searchPostalAddr =
                GlobalCC.checkNullValues(txtSearchPostal.getValue());


        session.setAttribute("searchCriteria", null);
        session.setAttribute("searchCriteria2", null);


        searchName = GlobalCC.checkNullValues(txtSearchName.getValue());

        if (searchName == null || searchName == "") {

            searchName = null;

        } else if (searchName.trim().length() < 1) {
            searchName = null;
        }

        searchOName = GlobalCC.checkNullValues(txtSearchOtherName.getValue());
        if (searchOName == null || searchOName == "") {

            searchOName = null;

        } else if (searchOName.trim().length() < 1) {
            searchOName = null;
        }
        if (searchPhysicalAddr == null) {

        } else if (searchPhysicalAddr.trim().length() < 1) {
            searchPhysicalAddr = null;
        }
        if (searchPostalAddr == null) {
            searchPostalAddr = null;
        } else if (searchPostalAddr.trim().length() < 1) {
            searchPostalAddr = null;
        }

        if (searchName != null) {
            name = "'" + searchName + "'";

        }
        if (searchOName != null) {
            oName = "'" + searchOName + "'";
        }
        if (searchShtDesc != null) {
            shtDesc = "'" + searchShtDesc + "'";
        }

        if (searchPhysicalAddr != null) {
            physicalAddr = "'" + searchPhysicalAddr + "'";
        }
        if (searchPostalAddr != null) {
            postalAddr = "'" + searchPostalAddr + "'";
        }


        if (rbtnPartOfAnyName.isSelected()) {
            // criteria
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            }
            criteria =
                    "    WHERE ( UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" +
                    name + ",'HAKUNA)'))||'%'\n" +
                    "            OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" +
                    name + ",'HAKUNA'))||'%' OR " +
                    "UPPER(CLNT_OTHER_NAMES) LIKE '%'|| UPPER(NVL(" + oName +
                    ",'HAKUNA)'))||'%'" +
                    " OR UPPER(CLNT_NAME) LIKE '%'|| UPPER(NVL(" + oName +
                    ",'HAKUNA'))||'%')";


        } else if (rbtnExactName.isSelected()) {
            if (oName != null && name != null) {
                GlobalCC.INFORMATIONREPORTING("Specify only One name:");
                return null;
            } else if (oName == null && name == null) {
                GlobalCC.INFORMATIONREPORTING("No name specified:");
                return null;
            } else if (name != null) {


                criteria = "WHERE ( UPPER(CLNT_NAME) =UPPER( " + name + ")) ";


            } else if (oName != null) {
                if (status == null && clntType == null && sector == null &&
                    dFrom == null && dTo == null) {
                    criteria =
                            "WHERE ( UPPER(CLNT_OTHER_NAMES)=UPPER(" + oName +
                            ") )";


                }

                //beginning part  of first and other name


            }
        } else if (rbtnShortDesc.isSelected()) {
            if (shtDesc == null) {
                GlobalCC.INFORMATIONREPORTING("Short Desc Required");
                return null;
            }

            criteria =
                    " WHERE ( UPPER( CLNT_SHT_DESC ) LIKE '%'||NVL(UPPER(" + shtDesc +
                    "),'HAKUNA')||'%')";

        } else if (rbtnPhySicalAddr.isSelected()) {

            if (physicalAddr == null) {
                GlobalCC.INFORMATIONREPORTING("Physical Address  Required");
                return null;
            }

            criteria =
                    "where ( UPPER(CLNT_PHYSICAL_ADDRS) like '%'||UPPER(" + physicalAddr +
                    ")||'%')";


        } else if (rbtnPostalAddr.isSelected()) {

            if (postalAddr == null) {
                GlobalCC.INFORMATIONREPORTING("Postal Address Required");
                return null;
            }

            criteria =
                    "where( UPPER(CLNT_POSTAL_ADDRS) like '%'||UPPER(" + postalAddr +
                    ")||'%')";

        }

        else {
            GlobalCC.INFORMATIONREPORTING("Choose criteria::");
            return null;
        }

        session.setAttribute("searchCriteria", criteria);


        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        // List<UIComponent> children =tblClientHolder.getChildren();
        //  UIComponent component = children.get(0);
        //  RichTable  table=(RichTable) component;

        // GlobalCC.refreshUI(table);
        GlobalCC.refreshUI(tblClientPop);

        return null;


    }

    public String actionResetSearch() {

        txtSearchName.setValue(null);
        txtSearchOtherName.setValue(null);
        txtSearchShortDesc.setValue(null);
        txtSearchPhysical.setValue(null);
        txtSearchPostal.setValue(null);

        //activate components
        txtSearchName.setDisabled(true);
        txtSearchOtherName.setDisabled(true);
        txtSearchShortDesc.setDisabled(true);
        txtSearchPhysical.setDisabled(true);
        txtSearchPostal.setDisabled(true);

        //refresh radio buttons
        rbtnPartOfAnyName.setSelected(false);
        rbtnExactName.setSelected(false);
        rbtnShortDesc.setSelected(false);
        rbtnPhySicalAddr.setSelected(false);
        rbtnPostalAddr.setSelected(false);


        GlobalCC.refreshUI(rbtnPartOfAnyName);
        GlobalCC.refreshUI(rbtnExactName);
        GlobalCC.refreshUI(rbtnShortDesc);
        GlobalCC.refreshUI(rbtnPhySicalAddr);
        GlobalCC.refreshUI(rbtnPostalAddr);


        //refesh components
        GlobalCC.refreshUI(txtSearchName);
        GlobalCC.refreshUI(txtSearchOtherName);

        GlobalCC.refreshUI(txtSearchShortDesc);

        GlobalCC.refreshUI(txtSearchPhysical);
        GlobalCC.refreshUI(txtSearchPostal);
        session.setAttribute("searchCriteria", null);
        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
        GlobalCC.refreshUI(tblClientPop);

        return null;
    }

    public String actionShowSearch() {


        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:searchClientPop" + "').show(hints);");
        return null;

    }

    public String actionShowMeassageTemplate() {

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:msgTemplatePop" + "').show(hints);");

        return null;

    }

    public String actionAcceptMessageTemplate() {
        Object key2 = tblmMsgTemplate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSmsMsgTemplateCode.setValue(nodeBinding.getAttribute("msgtCode"));
            txtSmsMsg.setValue(nodeBinding.getAttribute("msgtMsg"));
            txtSmsMessageTemplate.setValue(nodeBinding.getAttribute("msgtShtDesc"));
            GlobalCC.refreshUI(txtSmsMessageTemplate);
            GlobalCC.refreshUI(txtSmsMsg);
            GlobalCC.dismissPopUp("crm", "msgTemplatePop");

        } else {
            GlobalCC.INFORMATIONREPORTING("First select System ::");
        }
        return null;

    }

    public String actionAcceptClient() {
        Object key2 = tblClientPop.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        int t = 0;

        if (r != null) {
            //System.out.println(nodeBinding.getAttribute("code"));
            txtSmsClientCode.setValue(r.getAttribute("code"));
            txtSmsClientName.setValue(r.getAttribute("name"));
            session.setAttribute("zipCode", r.getAttribute("zipCode"));
            //txtSmsTelNo.setValue(r.getAttribute("sms"));
            String prefix = null;
            String smNo = null;
            if (r.getAttribute("sms") != null) {
                if (r.getAttribute("zipCode") != null) {
                    String couZip = r.getAttribute("zipCode").toString();
                    if (r.getAttribute("sms").toString().contains("+")) {

                        prefix =
                                r.getAttribute("sms").toString().replace("+" + couZip,
                                                                         "0");
                    } else {
                        prefix =
                                r.getAttribute("sms").toString().replace(couZip,
                                                                         "0");
                    }

                    //prefix = prefix.replace("+", "0");
                    List prefixArray = new ArrayList();
                    System.out.println(session.getAttribute("mobilePrefix"));
                    if (session.getAttribute("mobilePrefix") != null) {
                        prefixArray =
                                (List)session.getAttribute("mobilePrefix");
                        int k = 0;
                        System.out.println(prefixArray.size());
                        while (k < prefixArray.size()) {
                            if (prefix.startsWith((String)prefixArray.get(k))) {
                                smNo =
prefix.replace((CharSequence)prefixArray.get(k), "");
                                prefix = (String)prefixArray.get(k);
                                System.out.println(prefix);
                                System.out.println((String)prefixArray.get(k));
                                t = k;
                            }
                            k++;
                        }
                        prefixArray = null;
                    }
                }
            }
            System.out.println(t);
            txtPrefix.setValue(t);
            txtSmsTelNo.setValue(smNo);
            GlobalCC.refreshUI(txtSmsClientCode);
            GlobalCC.refreshUI(txtSmsClientName);
            GlobalCC.refreshUI(txtSmsTelNo);
            GlobalCC.refreshUI(txtPrefix);
            GlobalCC.dismissPopUp("crm", "searchClientPop");
        } else {
            GlobalCC.INFORMATIONREPORTING("First Select System ::");
        }
        return null;

    }

    public void setTxtSmsTelNo(RichInputText txtSmsTelNo) {
        this.txtSmsTelNo = txtSmsTelNo;
    }

    public RichInputText getTxtSmsTelNo() {
        return txtSmsTelNo;
    }

    public void setTxtSearchShortDesc(RichInputText txtSearchShortDesc) {
        this.txtSearchShortDesc = txtSearchShortDesc;
    }

    public RichInputText getTxtSearchShortDesc() {
        return txtSearchShortDesc;
    }

    public void setTxtSearchName(RichInputText txtSearchName) {
        this.txtSearchName = txtSearchName;
    }

    public RichInputText getTxtSearchName() {
        return txtSearchName;
    }

    public void setTxtSearchOtherName(RichInputText txtSearchOtherName) {
        this.txtSearchOtherName = txtSearchOtherName;
    }

    public RichInputText getTxtSearchOtherName() {
        return txtSearchOtherName;
    }

    public void setTxtSearchPostal(RichInputText txtSearchPostal) {
        this.txtSearchPostal = txtSearchPostal;
    }

    public RichInputText getTxtSearchPostal() {
        return txtSearchPostal;
    }

    public void setTxtSearchPhysical(RichInputText txtSearchPhysical) {
        this.txtSearchPhysical = txtSearchPhysical;
    }

    public RichInputText getTxtSearchPhysical() {
        return txtSearchPhysical;
    }

    public void setRbtnShortDesc(RichSelectBooleanRadio rbtnShortDesc) {
        this.rbtnShortDesc = rbtnShortDesc;
    }

    public RichSelectBooleanRadio getRbtnShortDesc() {
        return rbtnShortDesc;
    }

    public void setRbtnPhySicalAddr(RichSelectBooleanRadio rbtnPhySicalAddr) {
        this.rbtnPhySicalAddr = rbtnPhySicalAddr;
    }

    public RichSelectBooleanRadio getRbtnPhySicalAddr() {
        return rbtnPhySicalAddr;
    }

    public void setRbtnPostalAddr(RichSelectBooleanRadio rbtnPostalAddr) {
        this.rbtnPostalAddr = rbtnPostalAddr;
    }

    public RichSelectBooleanRadio getRbtnPostalAddr() {
        return rbtnPostalAddr;
    }

    public void setRbtnPartOfAnyName(RichSelectBooleanRadio rbtnPartOfAnyName) {
        this.rbtnPartOfAnyName = rbtnPartOfAnyName;
    }

    public RichSelectBooleanRadio getRbtnPartOfAnyName() {
        return rbtnPartOfAnyName;
    }

    public void setRbtnExactName(RichSelectBooleanRadio rbtnExactName) {
        this.rbtnExactName = rbtnExactName;
    }

    public RichSelectBooleanRadio getRbtnExactName() {
        return rbtnExactName;
    }

    public void setTblClientPop(RichTable tblClientPop) {
        this.tblClientPop = tblClientPop;
    }

    public RichTable getTblClientPop() {
        return tblClientPop;
    }

    public void setTxtSmsMessageTemplate(RichInputText txtSmsMessageTemplate) {
        this.txtSmsMessageTemplate = txtSmsMessageTemplate;
    }

    public RichInputText getTxtSmsMessageTemplate() {
        return txtSmsMessageTemplate;
    }

    public void setTxtSmsMsgTemplateCode(RichInputText txtSmsMsgTemplateCode) {
        this.txtSmsMsgTemplateCode = txtSmsMsgTemplateCode;
    }

    public RichInputText getTxtSmsMsgTemplateCode() {
        return txtSmsMsgTemplateCode;
    }

    public void setTblmMsgTemplate(RichTable tblmMsgTemplate) {
        this.tblmMsgTemplate = tblmMsgTemplate;
    }

    public RichTable getTblmMsgTemplate() {
        return tblmMsgTemplate;
    }

    public void setTxtSmsMsg(RichInputText txtSmsMsg) {
        this.txtSmsMsg = txtSmsMsg;
    }

    public RichInputText getTxtSmsMsg() {
        return txtSmsMsg;
    }

    public String saveMessage() {
        int pref;
        String destination = GlobalCC.checkNullValues(txtSmsTelNo.getValue());
        String message = GlobalCC.checkNullValues(txtSmsMsg.getValue());
        String smsCode = GlobalCC.checkNullValues(txtSmsCode.getValue());
        String prefix = null;
        System.out.println("Destination: " + destination + " zipCode: " +
                           session.getAttribute("zipCode") + " smsCode: " +
                           smsCode);
        if (txtPrefix.isVisible()) {
            prefix = GlobalCC.checkNullValues(txtPrefix.getValue());
            if (prefix != null) {
                Row row =
                    ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").getRowAtRangeIndex(new Integer(prefix));
                if (row.getAttribute("prefix") != null) {
                    prefix = row.getAttribute("prefix").toString();
                    if (prefix.startsWith("0")) {
                        prefix = prefix.replaceFirst("0", "");
                    }
                    System.out.println("Prefix: " + prefix);
                    destination = prefix + "" + destination;
                    destination =
                            "+" + session.getAttribute("zipCode") + destination;
                }
            }
        } else {
            if (destination.startsWith("0")) {
                destination = destination.substring(1);
                System.out.println(destination);
                System.out.println(session.getAttribute("zipCode").toString());
                destination =
                        "+" + session.getAttribute("zipCode").toString() +
                        destination;
            } else {
                destination =
                        "+" + session.getAttribute("zipCode").toString() +
                        destination;
            }
        }
        System.out.println("Destination: " + destination);
        OracleConnection conn = null;
        try {

            if (destination == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Destination");
                return null;
            }
            if (message == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Message");
                return null;
            }

            DBConnector datahandler = null;
            datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            OracleCallableStatement cst = null;


            String medicalQuery =
                "begin TQC_SMS_PKG.UpdateSMSMessage(?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            cst.setObject(1, smsCode);
            cst.setObject(2,
                          GlobalCC.checkNullValues(txtSmsClientCode.getValue()));
            cst.setObject(3, destination);
            cst.setObject(4, message);
            cst.setObject(5, session.getAttribute("UserCode"));
            cst.execute();
            cst.close();
            conn.close();
            ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
            GlobalCC.refreshUI(tblMessages);
            GlobalCC.dismissPopUp("crm", "messages");
            GlobalCC.sysInformation("Message Successfully Saved");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String editMessage() {
        Object key2 = tblMessages.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        txtSmsClientCode.setValue(r.getAttribute("sms_clnt_code"));
        txtSmsClientName.setValue(r.getAttribute("clientName"));
        txtSmsTelNo.setValue(r.getAttribute("sms_tel_no"));
        txtSmsMsg.setValue(r.getAttribute("sms_msg"));
        txtSmsCode.setValue(r.getAttribute("sms_code"));
        session.setAttribute("countryCode", r.getAttribute("couCode"));
        session.setAttribute("zipCode", r.getAttribute("couZipCode"));
        if (txtPrefix.isVisible()) {
            ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
            GlobalCC.refreshUI(txtPrefix);
        }
        String prefix = null;
        String smNo = null;
        int t = 0;
        System.out.println(r.getAttribute("sms_tel_no"));
        System.out.println(r.getAttribute("couZipCode"));
        if (r.getAttribute("sms_tel_no") != null) {
            if (r.getAttribute("couZipCode") != null) {
                String couZip = r.getAttribute("couZipCode").toString();
                if (r.getAttribute("sms_tel_no").toString().contains("+")) {

                    prefix =
                            r.getAttribute("sms_tel_no").toString().replace("+" +
                                                                            couZip,
                                                                            "0");
                } else {
                    prefix =
                            r.getAttribute("sms_tel_no").toString().replace(couZip,
                                                                            "0");
                }

                //prefix = prefix.replace("+", "0");
                if (txtPrefix.isVisible()) {
                    List prefixArray = new ArrayList();
                    System.out.println(session.getAttribute("mobilePrefix"));
                    if (session.getAttribute("mobilePrefix") != null) {
                        prefixArray =
                                (List)session.getAttribute("mobilePrefix");
                        int k = 0;
                        System.out.println(prefixArray.size());
                        while (k < prefixArray.size()) {
                            if (prefix.startsWith((String)prefixArray.get(k))) {
                                smNo =
prefix.replace((CharSequence)prefixArray.get(k), "");
                                prefix = (String)prefixArray.get(k);
                                System.out.println(prefix);
                                System.out.println((String)prefixArray.get(k));
                                t = k;
                            }
                            k++;
                        }
                        prefixArray = null;
                    }
                } else {
                    smNo = prefix;
                }
            }
        }
        System.out.println(t);
        if (txtPrefix.isVisible()) {
            txtPrefix.setValue(t);
        }
        txtSmsTelNo.setValue(smNo);
        GlobalCC.showPopUp("crm", "messages");
        return null;
    }

    public String showMessageDetails() {
        Object key2 = tblMessages.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        txtSmsMsgDtls.setValue(r.getAttribute("sms_msg"));
        GlobalCC.showPopup("crm:messageDetails");
        return null;
    }

    public String cancelMessage() {
        GlobalCC.dismissPopUp("crm", "messages");
        return null;
    }

    public String hideMessageDetails() {
        GlobalCC.hidePopup("crm:messageDetails");
        return null;
    }

    public void setTxtPrefix(RichSelectOneChoice txtPrefix) {
        this.txtPrefix = txtPrefix;
    }

    public RichSelectOneChoice getTxtPrefix() {
        return txtPrefix;
    }

    public void setSmsTable(RichTable smsTable) {
        this.smsTable = smsTable;
    }

    public RichTable getSmsTable() {
        return smsTable;
    }

    public void getSystemSms(SelectionEvent selectionEvent) {
        Object key2 = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("findSmsDetailsIterator").executeQuery();
            GlobalCC.refreshUI(smsTable);
            ADFUtils.findIterator("findMsgTemplateIterator").executeQuery();
            GlobalCC.refreshUI(tblmMsgTemplate);
        }
    }

    public void setEmailTbl(RichTable emailTbl) {
        this.emailTbl = emailTbl;
    }

    public RichTable getEmailTbl() {
        return emailTbl;
    }

    public void getSystemEmail(SelectionEvent selectionEvent) {
        Object key2 = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
            GlobalCC.refreshUI(emailTbl);
        }
    }

    public String selectAllEmails() {
        int i = 0;
        while (i < emailTbl.getRowCount()) {
            JUCtrlValueBinding r = (JUCtrlValueBinding)emailTbl.getRowData(i);
            r.setAttribute("checked", true);
            rowChecked.setSelected(true);
            GlobalCC.refreshUI(rowChecked);
            i++;
        }
        return null;
    }

    public String unselectAllEmails() {
        int i = 0;
        while (i < emailTbl.getRowCount()) {
            JUCtrlValueBinding r = (JUCtrlValueBinding)emailTbl.getRowData(i);
            if (r == null)
                continue;
            r.setAttribute("checked", false);
            rowChecked.setSelected(false);
            GlobalCC.refreshUI(rowChecked);
            i++;
        }
        return null;
    }

    public String sendAllEmails() {
        boolean messageSent = false;
        String url = null;
        int count = 0;
        try {
            JUCtrlValueBinding r = null;
            int rowCount2 = emailTbl.getRowCount();

            for (int i = 0; i < rowCount2; i++) {
                Boolean Accept = false;
                r = (JUCtrlValueBinding)emailTbl.getRowData(i);
                Accept = (Boolean)r.getAttribute("checked");
                if (Accept) {
                    String success = null;
                    if (r.getAttribute("emailAddress") == null ||
                        r.getAttribute("emailSubject") == null ||
                        r.getAttribute("messages") == null) {
                        count++;
                        GlobalCC.errorValueNotEntered("Error: No Email address and/or Email subject and/or Message provided for selected record");
                    } else {
                        
                        BigDecimal msgCode = GlobalCC.checkBDNullValues(r.getAttribute("code"));
                        session.setAttribute("SEND_MSG_CODE", msgCode);
                        
                        success =
                                GlobalCC.applicationEmail((String)r.getAttribute("emailAddress"),
                                                          null,
                                                          (String)r.getAttribute("emailSubject"),
                                                          (String)r.getAttribute("messages"));
                        if (success != null) {
                            GlobalCC.updateEmail(r.getAttribute("code"), "S");
                        } else {
                            GlobalCC.updateEmail(r.getAttribute("code"), "R");
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (count == 0) {
            GlobalCC.sysInformation("Messages sent successfully");
        }

        session.setAttribute("EmailsentStatus", "R");
        ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
        GlobalCC.refreshUI(emailTbl);
        return null;
    }

    public String showAllEmails() {
        session.setAttribute("EmailsentStatus", null);
        ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
        GlobalCC.refreshUI(emailTbl);
        return null;
    }

    public String showUnsentEmails() {
        session.setAttribute("EmailsentStatus", "R");
        ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
        GlobalCC.refreshUI(emailTbl);
        return null;
    }

    public String showSentEmails() {
        session.setAttribute("EmailsentStatus", "S");
        ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
        GlobalCC.refreshUI(emailTbl);
        return null;
    }

    public void setMsgWEF(RichInputDate smsWEF) {
        this.msgWEF = smsWEF;
    }

    public RichInputDate getMsgWEF() {
        return msgWEF;
    }

    public void setMsgWET(RichInputDate smsWET) {
        this.msgWET = smsWET;
    }

    public RichInputDate getMsgWET() {
        return msgWET;
    }

    public void setMsgTransType(RichSelectOneChoice transType) {
        this.msgTransType = transType;
    }

    public RichSelectOneChoice getMsgTransType() {
        return msgTransType;
    }

    public void smsWEFChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (msgWEF.getValue() == null) {
                session.setAttribute("smsWEF", null);
                return;
            }

            String reqDateVal = GlobalCC.checkNullValues(msgWEF.getValue());

            if (reqDateVal.contains(":")) {
                reqDateVal = GlobalCC.parseDate(reqDateVal);
            } else if (reqDateVal.contains("/")) {
                reqDateVal = GlobalCC.upDateParseDateTwo(reqDateVal);
            } else {
                reqDateVal = GlobalCC.upDateParseDate(reqDateVal);
            }

            session.setAttribute("smsWEF", reqDateVal);
            ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
            GlobalCC.refreshUI(tblMessages);
        }
    }

    public void smsWETChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (msgWET.getValue() == null) {
                session.setAttribute("smsWET", null);
                return;
            }

            String reqDateVal = GlobalCC.checkNullValues(msgWET.getValue());

            if (reqDateVal.contains(":")) {
                reqDateVal = GlobalCC.parseDate(reqDateVal);
            } else if (reqDateVal.contains("/")) {
                reqDateVal = GlobalCC.upDateParseDateTwo(reqDateVal);
            } else {
                reqDateVal = GlobalCC.upDateParseDate(reqDateVal);
            }


            session.setAttribute("smsWET", reqDateVal);
            ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
            GlobalCC.refreshUI(tblMessages);
        }
    }

    public void transTypeChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (msgTransType.getValue() == null) {
                session.setAttribute("smsTransType", "U");
            } else {
                session.setAttribute("smsTransType",
                                     msgTransType.getValue().toString());
            }
            ADFUtils.findIterator("fetchAllmessages1Iterator").executeQuery();
            GlobalCC.refreshUI(tblMessages);
        }
    }

    public void smsHistoryWEFChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (msgWEF.getValue() == null) {
                session.setAttribute("smsWEF", null);
                return;
            }

            String reqDateVal = GlobalCC.checkNullValues(msgWEF.getValue());

            if (reqDateVal.contains(":")) {
                reqDateVal = GlobalCC.parseDate(reqDateVal);
            } else if (reqDateVal.contains("/")) {
                reqDateVal = GlobalCC.upDateParseDateTwo(reqDateVal);
            } else {
                reqDateVal = GlobalCC.upDateParseDate(reqDateVal);
            }

            session.setAttribute("smsWEF", reqDateVal);
            ADFUtils.findIterator("findSmsDetailsIterator").executeQuery();
            GlobalCC.refreshUI(smsTable);
        }
    }

    public void smsHistoryWETChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (msgWET.getValue() == null) {
                session.setAttribute("smsWET", null);
                return;
            }

            String reqDateVal = GlobalCC.checkNullValues(msgWET.getValue());

            if (reqDateVal.contains(":")) {
                reqDateVal = GlobalCC.parseDate(reqDateVal);
            } else if (reqDateVal.contains("/")) {
                reqDateVal = GlobalCC.upDateParseDateTwo(reqDateVal);
            } else {
                reqDateVal = GlobalCC.upDateParseDate(reqDateVal);
            }


            session.setAttribute("smsWET", reqDateVal);
            ADFUtils.findIterator("findSmsDetailsIterator").executeQuery();
            GlobalCC.refreshUI(smsTable);
        }
    }

    public void transTypeHistoryChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (msgTransType.getValue() == null) {
                session.setAttribute("smsTransType", "U");
            } else {
                session.setAttribute("smsTransType",
                                     msgTransType.getValue().toString());
            }
            ADFUtils.findIterator("findSmsDetailsIterator").executeQuery();
            GlobalCC.refreshUI(smsTable);
        }
    }

    public void emailWEFChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (msgWEF.getValue() == null) {
                session.setAttribute("emailWEF", null);
                return;
            }

            String reqDateVal = GlobalCC.checkNullValues(msgWEF.getValue());

            if (reqDateVal.contains(":")) {
                reqDateVal = GlobalCC.parseDate(reqDateVal);
            } else if (reqDateVal.contains("/")) {
                reqDateVal = GlobalCC.upDateParseDateTwo(reqDateVal);
            } else {
                reqDateVal = GlobalCC.upDateParseDate(reqDateVal);
            }

            session.setAttribute("emailWEF", reqDateVal);
            ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
            GlobalCC.refreshUI(emailTbl);
        }
    }

    public void emailWETChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (msgWET.getValue() == null) {
                session.setAttribute("emailWET", null);
                return;
            }

            String reqDateVal = GlobalCC.checkNullValues(msgWET.getValue());

            if (reqDateVal.contains(":")) {
                reqDateVal = GlobalCC.parseDate(reqDateVal);
            } else if (reqDateVal.contains("/")) {
                reqDateVal = GlobalCC.upDateParseDateTwo(reqDateVal);
            } else {
                reqDateVal = GlobalCC.upDateParseDate(reqDateVal);
            }


            session.setAttribute("emailWET", reqDateVal);
            ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
            GlobalCC.refreshUI(emailTbl);
        }
    }

    public void emailTransTypeChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            if (msgTransType.getValue() == null) {
                session.setAttribute("emailTransType", "U");
            } else {
                session.setAttribute("emailTransType",
                                     msgTransType.getValue().toString());
            }
            ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
            GlobalCC.refreshUI(emailTbl);
        }
    }

    public String editEmailMessage() {

        Object key2 = emailTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        if (r.getAttribute("status").toString().equalsIgnoreCase("S")) {
            GlobalCC.errorValueNotEntered("Email Already sent out. Cannot Edit");
            return null;
        }
        txtEmailCode.setValue(r.getAttribute("code"));
        txtEmailAddress.setValue(r.getAttribute("emailAddress"));
        txtSubject.setValue(r.getAttribute("emailSubject"));
        txtSmsMsg.setValue(r.getAttribute("messages"));
        GlobalCC.showPopUp("crm", "messages");
        return null;
    }

    public void setTxtEmailCode(RichInputText txtEmailCode) {
        this.txtEmailCode = txtEmailCode;
    }

    public RichInputText getTxtEmailCode() {
        return txtEmailCode;
    }

    public void setTxtEmailAddress(RichInputText txtEmailAddress) {
        this.txtEmailAddress = txtEmailAddress;
    }

    public RichInputText getTxtEmailAddress() {
        return txtEmailAddress;
    }

    public void setTxtSubject(RichInputText txtSubject) {
        this.txtSubject = txtSubject;
    }

    public RichInputText getTxtSubject() {
        return txtSubject;
    }

    public String saveEmailMessage() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        String destination =
            GlobalCC.checkNullValues(txtEmailAddress.getValue());
        String subject = GlobalCC.checkNullValues(txtSubject.getValue());
        String message = GlobalCC.checkNullValues(txtSmsMsg.getValue());
        String emailCode = GlobalCC.checkNullValues(txtEmailCode.getValue());

        try {

            if (destination == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Destination");
                return null;
            }
            if (subject == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Subject");
                return null;
            }
            if (message == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Message");
                return null;
            }


            connection = dbConnector.getDatabaseConnection();
            String query1 =
                "begin Tqc_setups_Pkg.update_email_msg(?,?,?,?,?); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.setObject(1, emailCode);
            stmt.setString(2, destination);
            stmt.setString(3, subject);
            stmt.setString(4, message);
            stmt.setString(5, "R");
            stmt.execute();
            stmt.close();
            connection.commit();
            connection.close();
            session.setAttribute("EmailsentStatus", null);
            ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
            GlobalCC.refreshUI(emailTbl);
            GlobalCC.dismissPopUp("crm", "messages");
            GlobalCC.sysInformation("Message Successfully Saved");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();

        }
        return null;
    }

    public String resendMessage() {
        Object key2 = smsTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Row Selected");
            return null;
        }
        txtSmsClientCode.setValue(r.getAttribute("sms_clnt_code"));
        txtSmsClientName.setValue(r.getAttribute("clientName"));
        txtSmsTelNo.setValue(r.getAttribute("sms_tel_no"));
        txtSmsMsg.setValue(r.getAttribute("sms_msg"));
        txtSmsCode.setValue(r.getAttribute("sms_code"));
        session.setAttribute("countryCode", r.getAttribute("couCode"));
        session.setAttribute("zipCode", r.getAttribute("couZipCode"));
        if (txtPrefix.isVisible()) {
            ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
            GlobalCC.refreshUI(txtPrefix);
        }
        String prefix = null;
        String smNo = null;
        int t = 0;
        System.out.println(r.getAttribute("sms_tel_no"));
        System.out.println(r.getAttribute("couZipCode"));
        if (r.getAttribute("sms_tel_no") != null) {
            if (r.getAttribute("couZipCode") != null) {
                String couZip = r.getAttribute("couZipCode").toString();
                if (r.getAttribute("sms_tel_no").toString().contains("+")) {

                    prefix =
                            r.getAttribute("sms_tel_no").toString().replace("+" +
                                                                            couZip,
                                                                            "0");
                } else {
                    prefix =
                            r.getAttribute("sms_tel_no").toString().replace(couZip,
                                                                            "0");
                }

                //prefix = prefix.replace("+", "0");
                if (txtPrefix.isVisible()) {
                    List prefixArray = new ArrayList();
                    System.out.println(session.getAttribute("mobilePrefix"));
                    if (session.getAttribute("mobilePrefix") != null) {
                        prefixArray =
                                (List)session.getAttribute("mobilePrefix");
                        int k = 0;
                        System.out.println(prefixArray.size());
                        while (k < prefixArray.size()) {
                            if (prefix.startsWith((String)prefixArray.get(k))) {
                                smNo =
prefix.replace((CharSequence)prefixArray.get(k), "");
                                prefix = (String)prefixArray.get(k);
                                System.out.println(prefix);
                                System.out.println((String)prefixArray.get(k));
                                t = k;
                            }
                            k++;
                        }
                        prefixArray = null;
                    }
                } else {
                    smNo = prefix;
                }
            }
        }
        System.out.println(t);
        if (txtPrefix.isVisible()) {
            txtPrefix.setValue(t);
        }
        txtSmsTelNo.setValue(smNo);
        GlobalCC.showPopUp("crm", "messages");
        return null;
    }

    public String resendSMSMessage() {
        BigDecimal smsCode = null;
        String destination = GlobalCC.checkNullValues(txtSmsTelNo.getValue());
        String smsMessage = GlobalCC.checkNullValues(txtSmsMsg.getValue());
        String prefix = null;
        if (txtPrefix.isVisible()) {
            prefix = GlobalCC.checkNullValues(txtPrefix.getValue());
            if (prefix != null) {
                Row row =
                    ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").getRowAtRangeIndex(new Integer(prefix));
                if (row.getAttribute("prefix") != null) {
                    prefix = row.getAttribute("prefix").toString();
                    if (prefix.startsWith("0")) {
                        prefix = prefix.replaceFirst("0", "");
                    }

                    destination =
                            "+" + session.getAttribute("zipCode").toString() +
                            prefix + "" + destination;
                }
            } else {
                if (destination.startsWith("0")) {
                    destination = destination.substring(1);
                    destination =
                            "+" + session.getAttribute("zipCode").toString() +
                            destination;
                } else {
                    destination =
                            "+" + session.getAttribute("zipCode").toString() +
                            destination;
                }
            }
        } else {
            if (destination.startsWith("0")) {
                destination = destination.substring(1);
                destination =
                        "+" + session.getAttribute("zipCode").toString() +
                        destination;
            } else {
                destination =
                        "+" + session.getAttribute("zipCode").toString() +
                        destination;
            }
        }
        OracleConnection conn = null;
        try {

            if (destination == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Destination");
                return null;
            }
            if (smsMessage == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Message");
                return null;
            }

            DBConnector datahandler = null;
            datahandler = new DBConnector();

            conn = datahandler.getDatabaseConnection();

            OracleCallableStatement cst = null;


            String medicalQuery =
                "begin TQC_SMS_PKG.sendProvSms(?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(medicalQuery);

            cst.setObject(1, session.getAttribute("sysCode"));
            cst.setObject(2, null);
            cst.setObject(3,
                          GlobalCC.checkNullValues(txtSmsClientCode.getValue()));
            cst.setObject(4, null);
            cst.setObject(5, null);
            cst.setObject(6, null);
            cst.setObject(7, null);
            cst.setObject(8, null);
            cst.setObject(9, destination);
            cst.setObject(10, smsMessage);
            cst.setObject(11, "D");
            cst.setObject(12, session.getAttribute("Username"));
            cst.setObject(13, null);
            cst.registerOutParameter(14, OracleTypes.NUMBER);
            cst.execute();
            smsCode = cst.getBigDecimal(14);
            cst.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        String url = null;
        try {

            url = null;
            DBConnector datahandler = new DBConnector();
            DBConnector dbConnector = new DBConnector();
            OracleConnection connection = null;
            OracleCallableStatement stmt = null;
            OracleResultSet rs = null;
            try {


                connection = dbConnector.getDatabaseConnection();
                String query1 =
                    "begin ? := tqc_setups_cursor.getSmsSettings(); end;";
                stmt = (OracleCallableStatement)connection.prepareCall(query1);
                stmt.registerOutParameter(1, OracleTypes.CURSOR);
                stmt.execute();
                rs = (OracleResultSet)stmt.getObject(1);
                int k = 0;
                while (rs.next()) {
                    String message = smsMessage;
                    if (message == null) {

                    } else
                        message = java.net.URLEncoder.encode(message);
                    url = rs.getString(2);
                    url = url.replace("[USERNAME]", rs.getString(3));
                    url = url.replace("[PASSWORD]", rs.getString(4));
                    url = url.replace("[SOURCE]", rs.getString(5));
                    url = url.replace("[MESSAGE]", message);
                    url = url.replace("[DESTINATION]", destination);
                    System.out.println("This is url" + url);
                }

                rs.close();
                stmt.close();
                connection.close();
                URL urls = new URL(url);
                HttpURLConnection conns =
                    (HttpURLConnection)urls.openConnection();
                conns.setDoOutput(false);
                conns.setDoInput(true);
                String res = conns.getResponseMessage();
                System.out.println("This is response" + res);
                OracleCallableStatement cst = null;
                String Binders = "begin tqc_setups_pkg.smsStatus(?,?); end;";
                conn = null;
                conn = datahandler.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Binders);
                cst.setString(1, "OK");
                cst.setObject(2, smsCode);
                cst.execute();
                conn.close();
                int rsCode = conns.getResponseCode();
                if (rsCode == HttpURLConnection.HTTP_OK) {
                    conns.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
            GlobalCC.INFORMATIONREPORTING("Message(s) has been sent...");
            ADFUtils.findIterator("findSmsDetailsIterator").executeQuery();
            GlobalCC.refreshUI(smsTable);
            GlobalCC.dismissPopUp("crm", "messages");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;

    }


    public String invalidEmailMessages() {
        session.setAttribute("EmailsentStatus", "invalid");
        ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
        GlobalCC.refreshUI(emailTbl);
        return null;
    }

    public String validEmailMessages() {
        session.setAttribute("EmailsentStatus", "valid");
        ADFUtils.findIterator("findEmailDetailsIterator").executeQuery();
        GlobalCC.refreshUI(emailTbl);
        return null;
    }

    public void setTxtSmsMsgDtls(RichInputText txtSmsMsgDtls) {
        this.txtSmsMsgDtls = txtSmsMsgDtls;
    }

    public RichInputText getTxtSmsMsgDtls() {
        return txtSmsMsgDtls;
    }

    public String actionSelectMessageProduct() {
        GlobalCC.showPop("crm:addMessageProductsPopup");
        return null;
    }

    public void setMessageProductsTbl(RichTable messageProductsTbl) {
        this.messageProductsTbl = messageProductsTbl;
    }

    public RichTable getMessageProductsTbl() {
        return messageProductsTbl;
    }

    public String actionAcceptMessageproduct() {
        Object selectedKey = messageProductsTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)selectedKey;
        if (nodeBinding != null) {
            session.setAttribute("sysProdCode",
                                 nodeBinding.getAttribute("prodCode"));
            session.setAttribute("sysProdDesc",
                                 nodeBinding.getAttribute("prodDesc"));

            txtMessageProduct.setValue(nodeBinding.getAttribute("prodDesc"));
            GlobalCC.refreshUI(txtMessageProduct);
            GlobalCC.hidePopup("crm:addMessageProductsPopup");
        } else {
            GlobalCC.INFORMATIONREPORTING("Select a Product first.");
        }
        return null;
    }

    public void setTxtMessageProduct(RichInputText txtMessageProduct) {
        this.txtMessageProduct = txtMessageProduct;
    }

    public RichInputText getTxtMessageProduct() {
        return txtMessageProduct;
    }

    public void setTxtMsgSubject(RichInputText txtMsgSubject) {
        this.txtMsgSubject = txtMsgSubject;
    }

    public RichInputText getTxtMsgSubject() {
        return txtMsgSubject;
    }
}
