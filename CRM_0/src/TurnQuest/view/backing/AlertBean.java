package TurnQuest.view.backing;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.dao.MessagingDAO;
import TurnQuest.view.models.Alert2;
import TurnQuest.view.models.AlertType;
import TurnQuest.view.models.AlertType2;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.faces.model.SelectItem;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

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


public class AlertBean {
    private RichCommandButton actionNewAlertType;
    private RichCommandButton actionEditAlertType;
    private RichInputText txtAlertType;
    private RichInputText txtAltTCode;
    private RichSelectOneChoice txtAltT_Type;
    private RichInputText txtAltT_sysCode;
    private RichInputText txtAltT_email;
    private RichInputText txtAltT_sysName;
    private RichInputText txtAltT_sms;
    private RichCommandButton btnCreateUpdateAlertType;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable tblAlertType;
    private RichTable tblSytems;
    private RichCommandButton actionNewAlert;
    private RichCommandButton actionEditAlert;
    private RichInputText txtAltsCode;
    private RichInputText txtAlertTypeCode;
    private RichInputText txtAlert_Description;
    private RichInputDate txtAlert_date;
    private RichInputNumberSpinbox txtAlertPeriod;
    private RichSelectOneChoice socActByType;
    private RichInputText txtAlertDestCode;
    private RichInputText txtAlertDestName;
    private RichInputText txtAlertTemplate;
    private RichInputText txtAlertTemplateCode;
    private RichCommandButton btnCreateUpdateAlert;
    private RichCommandButton actionAcceptAgent;
    private RichTable tblAlert;
    private RichSelectBooleanRadio rbtnPartOfAnyName;
    private RichSelectBooleanRadio rbtnExactName;
    private RichSelectBooleanRadio rbtnShortDesc;
    private RichSelectBooleanRadio rbtnPhySicalAddr;
    private RichSelectBooleanRadio rbtnPostalAddr;
    private RichInputText txtSearchShortDesc;
    private RichInputText txtSearchName;
    private RichInputText txtSearchOtherName;
    private RichInputText txtSearchPhysical;
    private RichInputText txtSearchPostal;
    private RichTable tblClientPop;
    private RichTable tblAgencies;
    private RichTable tblmMsgTemplate;
    private RichTable tbUsers;
    private RichSelectOneChoice soc1Status;
    private RichInputDate actionAlertDateListener;
    private RichInputText txtAlert_shtDesc;
    private RichInputNumberSpinbox txtCharacterNo;
    private RichTable systemTbl;
    private RichSelectOneChoice txtEmail;
    private RichSelectOneChoice txtSms;
    private RichInputText txtEmailTemplate;
    private RichTable smsPopTbl;
    private RichTable emailPopTbl;
    private RichInputText txtSmsTemplate;
    private RichSelectOneChoice txtScreen;
    private RichInputText txtAlertTypes;
    private RichCommandButton emailPopBtn;
    private RichCommandButton smsPopBtn;
    private RichSelectOneChoice txtCheckAlerts;
    private RichInputText txtShtDesc;
    private RichSelectOneChoice soDispatchDocument;
    private RichInputText txtDescription;
    private RichInputText txtShtDescription;
    private RichCommandButton btnSaveDispatchDoc;
    private RichCommandButton btnDeleteDispatchDoc;
    private RichTable tblDispatchDocs;

    public AlertBean() {
    }


    public String actionNewAlertType() {
        resetAlertType();
        btnCreateUpdateAlertType.setText("Save");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:alertTypesPop" + "').show(hints);");
        return null;
    }

    public String actionNewAlert() {
        resetAlert();
        if (session.getAttribute("alertType") != null) {
            btnCreateUpdateAlert.setText("Save");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:alerts" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("First:Select  Alert Type");
        }
        return null;
    }


    public String actionShowSystems() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:systemsPop" + "').show(hints);");
        return null;
    }

    public void actiontblAlertType(SelectionEvent selectionEvent) {
        Object key2 = tblAlertType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        resetAlertType();
        if (nodeBinding != null) {
            session.setAttribute("alertType",
                                 nodeBinding.getAttribute("ALRT_CODE"));
            session.setAttribute("sysCode",
                                 nodeBinding.getAttribute("ALRT_SYS_CODE"));
            ADFUtils.findIterator("findMsgTemplateIterator").executeQuery();
            ADFUtils.findIterator("fetchAlertsIterator").executeQuery();
            GlobalCC.refreshUI(tblAlert);


        }
    }

    public String actionDeleteAlertType() {
        Object key2 = tblAlertType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        resetAlertType();
        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteAlertType" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }

        return null;
    }

    public String actionDeleteAlert() {
        Object key2 = tblAlert.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        resetAlertType();
        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteAlert" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }

        return null;
    }

    public String actionEditAlertType() {
        btnCreateUpdateAlertType.setText("Update");
        Object key2 = tblAlertType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        resetAlertType();

        if (nodeBinding != null) {

            txtAltTCode.setValue(nodeBinding.getAttribute("ALRT_CODE"));
            txtAltT_Type.setValue(nodeBinding.getAttribute("ALRT_TYPE"));
            txtAltT_sysCode.setValue(nodeBinding.getAttribute("ALRT_SYS_CODE"));
            txtAltT_sysName.setValue(nodeBinding.getAttribute("sysName"));
            //  txtAltT_email.setValue(nodeBinding.getAttribute("ALRT_SMS"));
            // txtAltT_sms.setValue(nodeBinding.getAttribute());


            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:alertTypesPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        return null;
    }

    public String actionEditAlert() {
        btnCreateUpdateAlert.setText("Update");
        Object key2 = tblAlert.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        resetAlert();
        if (nodeBinding != null) {
            txtAltsCode.setValue(nodeBinding.getAttribute("code"));
            txtAlertTypeCode.setValue(nodeBinding.getAttribute("alrt_code"));
            txtAlert_Description.setValue(nodeBinding.getAttribute("description"));
            txtAlert_date.setValue(nodeBinding.getAttribute("date"));
            txtAlertPeriod.setValue(nodeBinding.getAttribute("period"));
            socActByType.setValue(nodeBinding.getAttribute("dest_type"));
            txtAlertDestCode.setValue(nodeBinding.getAttribute("dest_code"));
            txtAlertDestName.setValue(nodeBinding.getAttribute("agency_name"));
            txtAlertTemplate.setValue(nodeBinding.getAttribute("template_sht_desc"));
            txtAlertTemplateCode.setValue(nodeBinding.getAttribute("msgt_code"));
            soc1Status.setValue(nodeBinding.getAttribute("status"));
            txtAlert_shtDesc.setValue(nodeBinding.getAttribute("short_desc"));
            session.setAttribute("accShtDesc",
                                 nodeBinding.getAttribute("dest_type"));
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:alerts" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        return null;
    }

    public void resetAlertType() {
        txtAltT_Type.setValue(null);
        txtAltTCode.setValue(null);
        txtAltT_sysCode.setValue(null);
        txtAltT_sysName.setValue(null);
        session.removeAttribute("sysCode");

    }

    public void resetAlert() {
        txtAltsCode.setValue(null);
        txtAlertTypeCode.setValue(null);
        txtAlert_Description.setValue(null);
        txtAlert_date.setValue(null);
        txtAlertPeriod.setValue(null);
        socActByType.setValue(null);
        txtAlertDestCode.setValue(null);
        txtAlertDestName.setValue(null);
        txtAlertTemplate.setValue(null);
        txtAlertTemplateCode.setValue(null);
        txtAlert_shtDesc.setValue(null);
        soc1Status.setValue("A");
        session.setAttribute("date", null);
        session.setAttribute("accShtDesc", null);

    }

    public String actionCreateUpdateAlertType() {
        String code = GlobalCC.checkNullValues(txtAltTCode.getValue());
        String type = GlobalCC.checkNullValues(txtAltT_Type.getValue());
        String syscode = GlobalCC.checkNullValues(txtAltT_sysCode.getValue());
        //  String email=GlobalCC.checkNullValues(txtAltT_email.getValue());
        //   String sms=GlobalCC.checkNullValues( txtAltT_sms.getValue());

        if (type == null) {
            GlobalCC.errorValueNotEntered("ERROR:TYPE REQUIRED::");
            return null;
        }
        if (syscode == null) {
            GlobalCC.errorValueNotEntered("ERROR: SYSTEM REQUIRED ::");
            return null;
        }


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        String Query = "BEGIN Tqc_Sms_Pkg.alertType_prc(?,?,?);END;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        String option = null;
        if (btnCreateUpdateAlertType.getText().equalsIgnoreCase("Update")) {
            option = "E";
            if (code == null) {
                GlobalCC.errorValueNotEntered("ERROR: CODE REQUIRED::");
            }

        } else {
            option = "A";
        }
        try {

            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_ALERT_TYPES_TAB", conn);
            ArrayList templateList = new ArrayList();
            AlertType2 alertType = new AlertType2();
            alertType.setSQLTypeName("TQC_ALERT_TYPES_OBJ");

            alertType.setALRT_CODE(code == null ? null : new BigDecimal(code));
            alertType.setALRT_TYPE(type);
            alertType.setALRT_SYS_CODE(syscode == null ? null :
                                       new BigDecimal(syscode));

            templateList.add(alertType);
            ARRAY array = new ARRAY(descriptor, conn, templateList.toArray());

            // Add or Edit
            cst.setString(1, option);
            cst.setArray(2, array);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();
            String err = cst.getString(3);
            if (err != null) {
                GlobalCC.INFORMATIONREPORTING(err);

            } else {
                if (option.equalsIgnoreCase("E")) {

                    ADFUtils.findIterator("fetchAlertTypesIterator").executeQuery();
                    GlobalCC.refreshUI(tblAlertType);
                    GlobalCC.INFORMATIONREPORTING("Record Updated Successfully:");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:alertTypesPop" +
                                         "').hide(hints);");
                } else {
                    GlobalCC.INFORMATIONREPORTING("Record Created Successfully:");

                    ADFUtils.findIterator("fetchAlertTypesIterator").executeQuery();
                    GlobalCC.refreshUI(tblAlertType);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:alertTypesPop" +
                                         "').hide(hints);");
                }

            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public void actionSubmitType(ValueChangeEvent evt) {

        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {
            RichSelectOneChoice myComp =
                (RichSelectOneChoice)evt.getComponent();
            session.setAttribute("accShtDesc", myComp.getValue());
            txtAlertDestCode.setValue(null);
            txtAlertDestName.setValue(null);
            GlobalCC.refreshUI(txtAlertDestName);
            ADFUtils.findIterator("fetchAgencyActivityLovIterator").executeQuery();


        }

    }

    public String actionAcceptClient() {
        Object key2 = tblClientPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAlertDestCode.setValue(nodeBinding.getAttribute("code"));
            txtAlertDestName.setValue(nodeBinding.getAttribute("name"));

            GlobalCC.refreshUI(txtAlertDestCode);
            GlobalCC.refreshUI(txtAlertDestName);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:searchClientPop" + "').hide(hints);");

        return null;

    }

    public String actionAcceptMessageTemplate() {
        Object key2 = tblmMsgTemplate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAlertTemplateCode.setValue(nodeBinding.getAttribute("msgtCode"));
            // txtAlertTemplate.setValue(nodeBinding.getAttribute("msgtMsg"));
            txtAlertTemplate.setValue(nodeBinding.getAttribute("msgtShtDesc"));
            GlobalCC.refreshUI(txtAlertTemplate);
            // GlobalCC.refreshUI(txtSmsMsg);


        }

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:msgTemplatePop" + "').hide(hints);");

        return null;

    }

    public String actionShowMessageTemplate() {

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:msgTemplatePop" + "').show(hints);");
        return null;
    }

    public String actionShowDestCodeLov() {

        if (session.getAttribute("accShtDesc") != null) {
            if (session.getAttribute("accShtDesc").toString().equalsIgnoreCase("CL")) {

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:searchClientPop" +
                                     "').show(hints);");
                return null;
            } else if (session.getAttribute("accShtDesc").toString().equalsIgnoreCase("SP")) {


                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:serviceProvidersPop" +
                                     "').show(hints);");
                return null;
            } else if (session.getAttribute("accShtDesc").toString().equalsIgnoreCase("U")) {


                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:usersPop" + "').show(hints);");
                return null;
            } else if (session.getAttribute("accShtDesc").toString().equalsIgnoreCase("AC")) {
                GlobalCC.INFORMATIONREPORTING("System will Resolve who is the 'Account Officer' Automaticallly:  ");
                return null;
            } else {

                ADFUtils.findIterator("fetchAgencyActivityLovIterator").executeQuery();
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:agentsListPopByAccType" +
                                     "').show(hints);");
                return null;
            }


        } else {
            GlobalCC.INFORMATIONREPORTING("First Activity by type");
            return null;

        }


    }

    public String actionCreateUpdateAlert() {
        String code = GlobalCC.checkNullValues(txtAltsCode.getValue());
        String type =
            GlobalCC.checkNullValues(session.getAttribute("alertType"));

        String description =
            GlobalCC.checkNullValues(txtAlert_Description.getValue());
        String date = GlobalCC.checkNullValues(txtAlert_date.getValue());
        String period = GlobalCC.checkNullValues(txtAlertPeriod.getValue());
        String dest_type = GlobalCC.checkNullValues(socActByType.getValue());
        String dest_code =
            GlobalCC.checkNullValues(txtAlertDestCode.getValue());
        String tempCode =
            GlobalCC.checkNullValues(txtAlertTemplateCode.getValue());
        String shtDesc = GlobalCC.checkNullValues(txtAlert_shtDesc.getValue());
        String status = null;


        //  String email=GlobalCC.checkNullValues(txtAltT_email.getValue());
        //   String sms=GlobalCC.checkNullValues( txtAltT_sms.getValue());

        if (period == null) {
            GlobalCC.errorValueNotEntered("ERROR:PERIOD REQUIRED::");
            return null;
        }
        if (description == null) {
            GlobalCC.errorValueNotEntered("ERROR: DESCRIPTION  REQUIRED ::");
            return null;
        }
        if (session.getAttribute("status") != null) {
            status = GlobalCC.checkNullValues(session.getAttribute("status"));
        } else {
            status = GlobalCC.checkNullValues(soc1Status.getValue());
        }
        if (status == null) {
            GlobalCC.errorValueNotEntered("ERROR: STATUS  REQUIRED ::");
            return null;
        }
        if (dest_type == null) {
            GlobalCC.errorValueNotEntered("ERROR: ACC TYPE REQUIRED::");
            return null;
        }
        if (!dest_type.equalsIgnoreCase("AC")) {
            if (dest_code == null) {
                GlobalCC.errorValueNotEntered("ERROR: RECIPIENT REQUIRED ::");
                return null;
            }
        }
        if (tempCode == null) {
            GlobalCC.errorValueNotEntered("ERROR: TEMPLATE REQUIRED ::");
            return null;
        }


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        String Query = "BEGIN Tqc_Sms_Pkg.alert_prc(?,?,?);END;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        String option = null;
        String theDate = null;
        if (btnCreateUpdateAlert.getText().equalsIgnoreCase("Update")) {
            option = "E";
            if (code == null) {
                GlobalCC.errorValueNotEntered("ERROR: CODE REQUIRED::");
            }

        } else {
            option = "A";
        }
        try {

            if (date != null && date.trim().length() > 1) {
                if (session.getAttribute("date") != null) {
                    theDate =
                            GlobalCC.parseDate(session.getAttribute("date").toString());
                } else {
                    theDate = GlobalCC.upDateParseDate(date);

                }
            }
            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_ALERTS_TAB", conn);
            ArrayList templateList = new ArrayList();
            Alert2 alert2 = new Alert2();
            alert2.setSQLTypeName("TQC_ALERTS_OBJ");

            alert2.setALRTS_CODE(code == null ? null : new BigDecimal(code));
            alert2.setALRTS_ALRT_CODE(type == null ? null :
                                      new BigDecimal(type));
            alert2.setALRTS_SYS_CODE(session.getAttribute("sysCode") == null ?
                                     null :
                                     new BigDecimal(session.getAttribute("sysCode").toString()));
            alert2.setALRTS_AGN_CODE(null);
            alert2.setALRTS_CLNT_CODE(null);
            alert2.setALRTS_DESCRIPTION(description);
            alert2.setALRTS_DATE(theDate);
            alert2.setALRTS_PERIOD(period == null ? null :
                                   new BigDecimal(period));
            alert2.setALRTS_USER_CODE(null);
            alert2.setALRTS_DEST_TYPE(dest_type == null ? null : dest_type);
            alert2.setALRTS_DEST_CODE(dest_code == null ? null :
                                      new BigDecimal(dest_code));
            alert2.setALRTS_MSGT_CODE(tempCode == null ? null :
                                      new BigDecimal(tempCode));
            alert2.setALRTS_STATUS(status);
            alert2.setALRTS_SHT_DESC(shtDesc);

            templateList.add(alert2);
            ARRAY array = new ARRAY(descriptor, conn, templateList.toArray());

            // Add or Edit
            cst.setString(1, option);
            cst.setArray(2, array);
            cst.registerOutParameter(3, OracleTypes.VARCHAR);
            cst.execute();
            String err = cst.getString(3);
            if (err != null) {
                GlobalCC.INFORMATIONREPORTING(err);

            } else {
                if (option.equalsIgnoreCase("E")) {

                    ADFUtils.findIterator("fetchAlertsIterator").executeQuery();
                    GlobalCC.refreshUI(tblAlert);
                    GlobalCC.INFORMATIONREPORTING("Record Updated Successfully:");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:alerts" + "').hide(hints);");
                } else {
                    GlobalCC.INFORMATIONREPORTING("Record Created Successfully:");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:alerts" + "').hide(hints);");
                    ADFUtils.findIterator("fetchAlertsIterator").executeQuery();
                    GlobalCC.refreshUI(tblAlert);
                }

            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public void actionConfirmedDeleteAlertType(DialogEvent dialogEvent) {
        OracleConnection conn = null;
        if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.no))) {

        } else if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.yes))) {


            Object key2 = tblAlertType.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            resetAlertType();
            if (nodeBinding != null) {
                String code =
                    GlobalCC.checkNullValues(nodeBinding.getAttribute("ALRT_CODE"));
                String Query = "BEGIN Tqc_Sms_Pkg.alertType_prc(?,?,?);END;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                String option = null;

                try {

                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ALERT_TYPES_TAB",
                                                         conn);
                    ArrayList templateList = new ArrayList();
                    AlertType2 alertType = new AlertType2();
                    alertType.setSQLTypeName("TQC_ALERT_TYPES_OBJ");

                    alertType.setALRT_CODE(code == null ? null :
                                           new BigDecimal(code));
                    alertType.setALRT_TYPE(null);
                    alertType.setALRT_SYS_CODE(null);

                    templateList.add(alertType);
                    ARRAY array =
                        new ARRAY(descriptor, conn, templateList.toArray());

                    // Add or Edit
                    cst.setString(1, "D");
                    cst.setArray(2, array);
                    cst.registerOutParameter(3, OracleTypes.VARCHAR);
                    cst.execute();
                    String err = cst.getString(3);
                    if (err != null) {
                        GlobalCC.INFORMATIONREPORTING(err);

                    } else {

                        ADFUtils.findIterator("fetchAlertTypesIterator").executeQuery();
                        GlobalCC.refreshUI(tblAlertType);
                        GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully:");

                    }
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
    }


    public void actionConfirmedDeleteAlert(DialogEvent dialogEvent) {
        OracleConnection conn = null;
        if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.no))) {

        } else if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.yes))) {


            Object key2 = tblAlert.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            resetAlertType();
            if (nodeBinding != null) {
                String code =
                    GlobalCC.checkNullValues(nodeBinding.getAttribute("code"));
                String Query = "BEGIN Tqc_Sms_Pkg.alert_prc(?,?,?);END;";

                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                String option = null;
                try {
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);


                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ALERTS_TAB",
                                                         conn);
                    ArrayList templateList = new ArrayList();
                    Alert2 alert2 = new Alert2();
                    alert2.setSQLTypeName("TQC_ALERTS_OBJ");

                    alert2.setALRTS_CODE(code == null ? null :
                                         new BigDecimal(code));
                    alert2.setALRTS_ALRT_CODE(null);
                    alert2.setALRTS_SYS_CODE(session.getAttribute("sysCode") ==
                                             null ? null :
                                             new BigDecimal(session.getAttribute("sysCode").toString()));
                    alert2.setALRTS_AGN_CODE(null);
                    alert2.setALRTS_CLNT_CODE(null);
                    alert2.setALRTS_DESCRIPTION(null);
                    alert2.setALRTS_DATE(null);
                    alert2.setALRTS_PERIOD(null);
                    alert2.setALRTS_USER_CODE(null);
                    alert2.setALRTS_DEST_TYPE(null);
                    alert2.setALRTS_DEST_CODE(null);
                    alert2.setALRTS_MSGT_CODE(null);
                    alert2.setALRTS_STATUS(null);
                    alert2.setALRTS_SHT_DESC(null);

                    templateList.add(alert2);
                    ARRAY array =
                        new ARRAY(descriptor, conn, templateList.toArray());

                    // Add or Edit
                    cst.setString(1, "D");
                    cst.setArray(2, array);
                    cst.registerOutParameter(3, OracleTypes.VARCHAR);
                    cst.execute();
                    String err = cst.getString(3);
                    if (err != null) {
                        GlobalCC.INFORMATIONREPORTING(err);

                    } else {

                        ADFUtils.findIterator("fetchAlertsIterator").executeQuery();
                        GlobalCC.refreshUI(tblAlert);
                        GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully:");

                    }
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
    }


    public String actionAcceptSystems() {
        Object key2 = tblSytems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAltT_sysCode.setValue(nodeBinding.getAttribute("sysCode"));
            txtAltT_sysName.setValue(nodeBinding.getAttribute("sysName"));
            GlobalCC.refreshUI(txtAltT_sysCode);
            GlobalCC.refreshUI(txtAltT_sysName);


        } else {
            GlobalCC.INFORMATIONREPORTING("First select System ::");
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:systemsPop" + "').hide(hints);");
        return null;


    }

    public String actionAcceptUser() {
        Object key2 = tbUsers.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("userCode",
                                 nodeBinding.getAttribute("userCode"));
            txtAlertDestName.setValue(nodeBinding.getAttribute("userFullname"));
            GlobalCC.refreshUI(txtAlertDestName);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:usersPop" + "').hide(hints);");
        }
        return null;
    }

    public String actionAcceptAgent() {
        Object key2 = tblAgencies.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAlertDestCode.setValue(nodeBinding.getAttribute("code"));
            txtAlertDestName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtAlertDestName);

        }

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:agentsListPopByAccType" + "').hide(hints);");
        return null;
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

    public void setTxtSearchPhysical(RichInputText txtSearchPhysical) {
        this.txtSearchPhysical = txtSearchPhysical;
    }

    public RichInputText getTxtSearchPhysical() {
        return txtSearchPhysical;
    }

    public void setTxtSearchPostal(RichInputText txtSearchPostal) {
        this.txtSearchPostal = txtSearchPostal;
    }

    public RichInputText getTxtSearchPostal() {
        return txtSearchPostal;
    }

    public void setTblClientPop(RichTable tblClientPop) {
        this.tblClientPop = tblClientPop;
    }

    public RichTable getTblClientPop() {
        return tblClientPop;
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


    public void setActionNewAlertType(RichCommandButton actionNewAlertType) {
        this.actionNewAlertType = actionNewAlertType;
    }

    public RichCommandButton getActionNewAlertType() {
        return actionNewAlertType;
    }

    public void setActionEditAlertType(RichCommandButton actionEditAlertType) {
        this.actionEditAlertType = actionEditAlertType;
    }

    public RichCommandButton getActionEditAlertType() {
        return actionEditAlertType;
    }


    public void setTxtAlertType(RichInputText txtAlertType) {
        this.txtAlertType = txtAlertType;
    }

    public RichInputText getTxtAlertType() {
        return txtAlertType;
    }

    public void setTxtAltTCode(RichInputText txtAltTCode) {
        this.txtAltTCode = txtAltTCode;
    }

    public RichInputText getTxtAltTCode() {
        return txtAltTCode;
    }

    public void setTxtAltT_Type(RichSelectOneChoice txtAltT_Type) {
        this.txtAltT_Type = txtAltT_Type;
    }

    public RichSelectOneChoice getTxtAltT_Type() {
        return txtAltT_Type;
    }

    public void setTxtAltT_sysCode(RichInputText txtAltT_sysCode) {
        this.txtAltT_sysCode = txtAltT_sysCode;
    }

    public RichInputText getTxtAltT_sysCode() {
        return txtAltT_sysCode;
    }

    public void setTxtAltT_email(RichInputText txtAltT_email) {
        this.txtAltT_email = txtAltT_email;
    }

    public RichInputText getTxtAltT_email() {
        return txtAltT_email;
    }

    public void setTxtAltT_sysName(RichInputText txtAltT_sysName) {
        this.txtAltT_sysName = txtAltT_sysName;
    }

    public RichInputText getTxtAltT_sysName() {
        return txtAltT_sysName;
    }


    public void setTxtAltT_sms(RichInputText txtAltT_sms) {
        this.txtAltT_sms = txtAltT_sms;
    }

    public RichInputText getTxtAltT_sms() {
        return txtAltT_sms;
    }

    public void setBtnCreateUpdateAlertType(RichCommandButton btnCreateUpdateAlertType) {
        this.btnCreateUpdateAlertType = btnCreateUpdateAlertType;
    }

    public RichCommandButton getBtnCreateUpdateAlertType() {
        return btnCreateUpdateAlertType;
    }

    public void setTblAlertType(RichTable tblAlertType) {
        this.tblAlertType = tblAlertType;
    }

    public RichTable getTblAlertType() {
        return tblAlertType;
    }


    public void setTblSytems(RichTable tblSytems) {
        this.tblSytems = tblSytems;
    }

    public RichTable getTblSytems() {
        return tblSytems;
    }

    public void setActionNewAlert(RichCommandButton actionNewAlert) {
        this.actionNewAlert = actionNewAlert;
    }

    public RichCommandButton getActionNewAlert() {
        return actionNewAlert;
    }

    public void setActionEditAlert(RichCommandButton actionEditAlert) {
        this.actionEditAlert = actionEditAlert;
    }

    public RichCommandButton getActionEditAlert() {
        return actionEditAlert;
    }

    public void setTxtAltsCode(RichInputText txtAltsCode) {
        this.txtAltsCode = txtAltsCode;
    }

    public RichInputText getTxtAltsCode() {
        return txtAltsCode;
    }

    public void setTxtAlertTypeCode(RichInputText txtAlertTypeCode) {
        this.txtAlertTypeCode = txtAlertTypeCode;
    }

    public RichInputText getTxtAlertTypeCode() {
        return txtAlertTypeCode;
    }

    public void setTxtAlert_Description(RichInputText txtAlert_Description) {
        this.txtAlert_Description = txtAlert_Description;
    }

    public RichInputText getTxtAlert_Description() {
        return txtAlert_Description;
    }

    public void setTxtAlert_date(RichInputDate txtAlert_date) {
        this.txtAlert_date = txtAlert_date;
    }

    public RichInputDate getTxtAlert_date() {
        return txtAlert_date;
    }

    public void setTxtAlertPeriod(RichInputNumberSpinbox txtAlertPeriod) {
        this.txtAlertPeriod = txtAlertPeriod;
    }

    public RichInputNumberSpinbox getTxtAlertPeriod() {
        return txtAlertPeriod;
    }

    public void setSocActByType(RichSelectOneChoice socActByType) {
        this.socActByType = socActByType;
    }

    public RichSelectOneChoice getSocActByType() {
        return socActByType;
    }

    public void setTxtAlertDestCode(RichInputText txtAlertDestCode) {
        this.txtAlertDestCode = txtAlertDestCode;
    }

    public RichInputText getTxtAlertDestCode() {
        return txtAlertDestCode;
    }

    public void setTxtAlertDestName(RichInputText txtAlertDestName) {
        this.txtAlertDestName = txtAlertDestName;
    }

    public RichInputText getTxtAlertDestName() {
        return txtAlertDestName;
    }

    public void setTxtAlertTemplate(RichInputText txtAlertTemplate) {
        this.txtAlertTemplate = txtAlertTemplate;
    }

    public RichInputText getTxtAlertTemplate() {
        return txtAlertTemplate;
    }

    public void setTxtAlertTemplateCode(RichInputText txtAlertTemplateCode) {
        this.txtAlertTemplateCode = txtAlertTemplateCode;
    }

    public RichInputText getTxtAlertTemplateCode() {
        return txtAlertTemplateCode;
    }


    public void setBtnCreateUpdateAlert(RichCommandButton btnCreateUpdateAlert) {
        this.btnCreateUpdateAlert = btnCreateUpdateAlert;
    }

    public RichCommandButton getBtnCreateUpdateAlert() {
        return btnCreateUpdateAlert;
    }

    public void setActionAcceptAgent(RichCommandButton actionAcceptAgent) {
        this.actionAcceptAgent = actionAcceptAgent;
    }

    public RichCommandButton getActionAcceptAgent() {
        return actionAcceptAgent;
    }


    public void setTblAlert(RichTable tblAlert) {
        this.tblAlert = tblAlert;
    }

    public RichTable getTblAlert() {
        return tblAlert;
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

    public void setTblAgencies(RichTable tblAgencies) {
        this.tblAgencies = tblAgencies;
    }

    public RichTable getTblAgencies() {
        return tblAgencies;
    }

    public void setTblmMsgTemplate(RichTable tblmMsgTemplate) {
        this.tblmMsgTemplate = tblmMsgTemplate;
    }

    public RichTable getTblmMsgTemplate() {
        return tblmMsgTemplate;
    }

    public void setTbUsers(RichTable tbUsers) {
        this.tbUsers = tbUsers;
    }

    public RichTable getTbUsers() {
        return tbUsers;
    }


    public void setSoc1Status(RichSelectOneChoice soc1Status) {
        this.soc1Status = soc1Status;
    }

    public RichSelectOneChoice getSoc1Status() {
        return soc1Status;
    }

    public void actionStatuslistener(ValueChangeEvent valueChangeEvent) {
        RichSelectOneChoice roc =
            (RichSelectOneChoice)valueChangeEvent.getComponent();
        session.setAttribute("status", roc.getValue());
    }

    public void setActionAlertDateListener(RichInputDate actionAlertDateListener) {
        this.actionAlertDateListener = actionAlertDateListener;
    }

    public RichInputDate getActionAlertDateListener() {
        return actionAlertDateListener;
    }

    public void actionAlertDateListener(ValueChangeEvent valueChangeEvent) {
        RichInputDate comp = (RichInputDate)valueChangeEvent.getComponent();
        session.setAttribute("date", comp.getValue());
        // Add event code here...
    }

    public void setTxtAlert_shtDesc(RichInputText txtAlert_shtDesc) {
        this.txtAlert_shtDesc = txtAlert_shtDesc;
    }

    public RichInputText getTxtAlert_shtDesc() {
        return txtAlert_shtDesc;
    }

    public void setTxtCharacterNo(RichInputNumberSpinbox txtCharacterNo) {
        this.txtCharacterNo = txtCharacterNo;
    }

    public RichInputNumberSpinbox getTxtCharacterNo() {
        return txtCharacterNo;
    }

    public void selectSystem(SelectionEvent selectionEvent) {
        Object key = systemTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return;
        }
        session.setAttribute("SystemCode", r.getAttribute("code"));
        ADFUtils.findIterator("fetchAlertTypesIterator").executeQuery();
        GlobalCC.refreshUI(tblAlertType);
    }

    public void setSystemTbl(RichTable systemTbl) {
        this.systemTbl = systemTbl;
    }

    public RichTable getSystemTbl() {
        return systemTbl;
    }

    public void setTxtEmail(RichSelectOneChoice txtEmail) {
        this.txtEmail = txtEmail;
    }

    public RichSelectOneChoice getTxtEmail() {
        return txtEmail;
    }

    public void setTxtSms(RichSelectOneChoice txtSms) {
        this.txtSms = txtSms;
    }

    public RichSelectOneChoice getTxtSms() {
        return txtSms;
    }

    public String selectSmsTemplate() {
        Object key = smsPopTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("smsmsgtCode", r.getAttribute("msgtCode"));
        txtSmsTemplate.setValue(r.getAttribute("msgtMsg"));
        GlobalCC.refreshUI(txtSmsTemplate);
        ExtendedRenderKitService erkServiceser =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkServiceser.addScript(FacesContext.getCurrentInstance(),
                                "var hints = {autodismissNever:false}; " +
                                "AdfPage.PAGE.findComponent('" + "crm:smsPop" +
                                "').hide(hints);");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:alertTypesPop" + "').show(hints);");
        return null;
    }

    public String selectEmailTemplate() {
        Object key = emailPopTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("emailmsgtCode", r.getAttribute("msgtCode"));
        txtEmailTemplate.setValue(r.getAttribute("msgtMsg"));
        GlobalCC.refreshUI(txtEmailTemplate);
        ExtendedRenderKitService erkServiceser =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkServiceser.addScript(FacesContext.getCurrentInstance(),
                                "var hints = {autodismissNever:false}; " +
                                "AdfPage.PAGE.findComponent('" +
                                "crm:emailPop" + "').hide(hints);");
        return null;
    }

    public String selectSmsPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:smsPop" +
                             "').show(hints);");
        return null;
    }

    public void setTxtEmailTemplate(RichInputText txtEmailTemplate) {
        this.txtEmailTemplate = txtEmailTemplate;
    }

    public RichInputText getTxtEmailTemplate() {
        return txtEmailTemplate;
    }

    public void selectEmailPop(ActionEvent actionEvent) {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:emailPop" +
                             "').show(hints);");
    }

    public void setSmsPopTbl(RichTable smsPopTbl) {
        this.smsPopTbl = smsPopTbl;
    }

    public RichTable getSmsPopTbl() {
        return smsPopTbl;
    }

    public void setEmailPopTbl(RichTable emailPopTbl) {
        this.emailPopTbl = emailPopTbl;
    }

    public RichTable getEmailPopTbl() {
        return emailPopTbl;
    }

    public void setTxtSmsTemplate(RichInputText txtSmsTemplate) {
        this.txtSmsTemplate = txtSmsTemplate;
    }

    public RichInputText getTxtSmsTemplate() {
        return txtSmsTemplate;
    }

    public void setTxtScreen(RichSelectOneChoice txtScreen) {
        this.txtScreen = txtScreen;
    }

    public RichSelectOneChoice getTxtScreen() {
        return txtScreen;
    }

    public void setTxtAlertTypes(RichInputText txtAlertTypes) {
        this.txtAlertTypes = txtAlertTypes;
    }

    public RichInputText getTxtAlertTypes() {
        return txtAlertTypes;
    }

    public String saveAlertTypes() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 =
                "begin Tqc_Sms_Pkg.sendserviceprov(?,?,?,?,?,?,?,?,?,?,?,?);end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.setString(1, "E");
            stmt.setObject(2, session.getAttribute("ALRT_CODE"));
            stmt.setObject(3, txtAlertTypes.getValue());
            stmt.setObject(4, session.getAttribute("SystemCode"));
            stmt.setObject(5, txtEmail.getValue());
            stmt.setObject(6, txtSms.getValue());
            stmt.setObject(7, txtScreen.getValue());
            stmt.setObject(8, session.getAttribute("emailmsgtCode"));
            stmt.setObject(9, session.getAttribute("smsmsgtCode"));
            stmt.setObject(10, session.getAttribute("userCode"));
            stmt.setObject(11, txtCheckAlerts.getValue());
            stmt.setObject(12, txtShtDesc.getValue());
            stmt.execute();
            stmt.close();
            conn.close();
            ExtendedRenderKitService erkServiceser =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkServiceser.addScript(FacesContext.getCurrentInstance(),
                                    "var hints = {autodismissNever:false}; " +
                                    "AdfPage.PAGE.findComponent('" +
                                    "crm:alertTypesPop" + "').hide(hints);");
            ADFUtils.findIterator("fetchAlertTypesIterator").executeQuery();
            GlobalCC.refreshUI(tblAlertType);
        } catch (SQLException ex) {
            ex.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return null;
    }

    public String selectAlertUser() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:usersPop" +
                             "').show(hints);");
        return null;
    }

    public String editAlertType() {
        Object key = tblAlertType.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        txtAlertTypes.setValue(r.getAttribute("ALRT_TYPE"));
        txtEmail.setValue(r.getAttribute("ALRT_EMAIL"));
        txtEmailTemplate.setValue(r.getAttribute("alertEmail"));
        txtSms.setValue(r.getAttribute("ALRT_SMS"));
        txtSmsTemplate.setValue(r.getAttribute("alertSms"));
        txtScreen.setValue(r.getAttribute("alertScreen"));
        txtAlertDestName.setValue(r.getAttribute("grpUsers"));
        session.setAttribute("ALRT_CODE", r.getAttribute("ALRT_CODE"));
        session.setAttribute("emailmsgtCode",
                             r.getAttribute("alertEmailMsgCode"));
        session.setAttribute("smsmsgtCode",
                             r.getAttribute("alertSmsMsgtCode"));
        txtCheckAlerts.setValue(r.getAttribute("alertCheckAlert"));
        txtShtDesc.setValue(r.getAttribute("alrtshtDesc"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:alertTypesPop" + "').show(hints);");
        return null;
    }

    public void setEmailPopBtn(RichCommandButton emailPopBtn) {
        this.emailPopBtn = emailPopBtn;
    }

    public RichCommandButton getEmailPopBtn() {
        return emailPopBtn;
    }

    public void setSmsPopBtn(RichCommandButton smsPopBtn) {
        this.smsPopBtn = smsPopBtn;
    }

    public RichCommandButton getSmsPopBtn() {
        return smsPopBtn;
    }

    public void selectEmail(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
            if (txtEmail.getValue().equals("Y")) {
                emailPopBtn.setDisabled(false);
            } else {
                emailPopBtn.setDisabled(true);
            }

        }
        GlobalCC.refreshUI(emailPopBtn);

    }

    public void selectSmsVal(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
            if (txtSms.getValue().equals("Y")) {
                smsPopBtn.setDisabled(false);
            } else {
                smsPopBtn.setDisabled(true);
            }

        }
        GlobalCC.refreshUI(smsPopBtn);
    }

    public String selectEmailPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:emailPop" +
                             "').show(hints);");
        return null;
    }

    public void setTxtCheckAlerts(RichSelectOneChoice txtCheckAlerts) {
        this.txtCheckAlerts = txtCheckAlerts;
    }

    public RichSelectOneChoice getTxtCheckAlerts() {
        return txtCheckAlerts;
    }

    public void setTxtShtDesc(RichInputText txtShtDesc) {
        this.txtShtDesc = txtShtDesc;
    }

    public RichInputText getTxtShtDesc() {
        return txtShtDesc;
    }

    public void setSoDispatchDocument(RichSelectOneChoice soDispatchDocument) {
        this.soDispatchDocument = soDispatchDocument;
    }

    public RichSelectOneChoice getSoDispatchDocument() {
        return soDispatchDocument;
    }

    public void setTxtDescription(RichInputText txtDescription) {
        this.txtDescription = txtDescription;
    }

    public RichInputText getTxtDescription() {
        return txtDescription;
    }

    public void setTxtShtDescription(RichInputText txtShtDescription) {
        this.txtShtDescription = txtShtDescription;
    }

    public RichInputText getTxtShtDescription() {
        return txtShtDescription;
    }

    public void setBtnSaveDispatchDoc(RichCommandButton btnSaveDispatchDoc) {
        this.btnSaveDispatchDoc = btnSaveDispatchDoc;
    }

    public RichCommandButton getBtnSaveDispatchDoc() {
        return btnSaveDispatchDoc;
    }

    public void setBtnDeleteDispatchDoc(RichCommandButton btnDeleteDispatchDoc) {
        this.btnDeleteDispatchDoc = btnDeleteDispatchDoc;
    }

    public RichCommandButton getBtnDeleteDispatchDoc() {
        return btnDeleteDispatchDoc;
    }

    public void setTblDispatchDocs(RichTable tblDispatchDocs) {
        this.tblDispatchDocs = tblDispatchDocs;
    }

    public RichTable getTblDispatchDocs() {
        return tblDispatchDocs;
    }
    public String addDispatchDocPop() {
        Object key = tblAlertType.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:p1" + "').show(hints);");
        return null;
    }

    public String deleteDispatchDoc() {
        DBConnector dbConnector = new DBConnector();

        String query = "begin  Tqc_Sms_Pkg.dispatch_documents_prc(?,?,?,?,?);end;";
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        
        Object key = tblDispatchDocs.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        
        try {
            connection = dbConnector.getDatabaseConnection();
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setObject(1, "D");
            statement.setObject(2, null);
            statement.setObject(3, null);
            statement.setObject(4, null);
            statement.setObject(5, r.getAttribute("dispatchDocCode"));
            
            statement.execute();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        }
        
        ADFUtils.findIterator("fetchDispatchDocumentsIterator").executeQuery();
        GlobalCC.refreshUI(tblDispatchDocs);
        
        GlobalCC.INFORMATIONREPORTING("Record deleted succesfully");
        return null;
    }
    public List<SelectItem> getDocs() {
        List<SelectItem> docs = new ArrayList<SelectItem>();
        if (docs != null) {
            docs.clear();
        }
        List<AlertType> dispatchDocs = new MessagingDAO().findDispatchDocuments();
        for (AlertType doc : dispatchDocs) {
            docs.add(new SelectItem(doc.getDispatchDocCode(),
                                       doc.getDispatchDocRptDesc()));
        }

        return docs;
    }

    public String saveDispatchDoc() {
        DBConnector dbConnector = new DBConnector();
         OracleConnection conn = null;
         OracleCallableStatement stmt = null;
         OracleResultSet rst = null;
         try {
             conn = dbConnector.getDatabaseConnection();
             String query1 =
                 "begin Tqc_Sms_Pkg.dispatch_documents_prc(?,?,?,?,?);end;";
             if(txtShtDescription.getValue() == null){
                 GlobalCC.EXCEPTIONREPORTING("Enter short description..");
                 return null;
             }
             
             if(txtDescription.getValue() == null){
                 GlobalCC.EXCEPTIONREPORTING("Enter Description..");
                 return null;
             }
             
             if(soDispatchDocument.getValue() == null){
                 GlobalCC.EXCEPTIONREPORTING("Select Document..");
                 return null;
             }
             
             stmt = (OracleCallableStatement)conn.prepareCall(query1);
             stmt.setString(1, "A");//add/Edit
             stmt.setObject(2, session.getAttribute("alertCode"));
             stmt.setObject(3, txtShtDescription.getValue());
             stmt.setObject(4, txtDescription.getValue());
             stmt.setObject(5, soDispatchDocument.getValue());
             stmt.execute();
             stmt.close();
             conn.close();
//             if(session.getAttribute("alertCode")!=null){
//                 session.removeAttribute("alertCode");
//             }
             
             clearComponents();
             ExtendedRenderKitService erkServiceser =
                 Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                    ExtendedRenderKitService.class);
             erkServiceser.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:p1" + "').hide(hints);");
             ADFUtils.findIterator("fetchDispatchDocumentsIterator").executeQuery();
             GlobalCC.refreshUI(tblDispatchDocs);
         } catch (SQLException ex) {
             ex.printStackTrace();
             GlobalCC.EXCEPTIONREPORTING(ex);
         } finally {
             DbUtils.closeQuietly(conn, stmt, rst);
         }
         return null;

    }
    
    public void documentSelectedChoice(ValueChangeEvent valueChangeEvent) {
        if (soDispatchDocument.getValue() == null)
            return;
        session.setAttribute("dispatchDocCode",
                             new BigDecimal(soDispatchDocument.getValue().toString()));
        List<AlertType> docs = new MessagingDAO().findDispatchDocuments();
        for (AlertType doc : docs) {
            if (doc.getDispatchDocCode().compareTo(new BigDecimal(soDispatchDocument.getValue().toString())) ==
                0) {
                session.setAttribute("dispatchDocCode", doc.getDispatchDocCode());
                session.setAttribute("dispatchDocRptDesc", doc.getDispatchDocRptDesc().toString());
                return;
            }
        }

    }

    public void alrtSelectionListener(SelectionEvent selectionEvent) {
        if( session.getAttribute("alertCode")!=null){
            session.removeAttribute("alertCode");
        }
        Object key = tblAlertType.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            return;
        }
        session.setAttribute("alertCode", r.getAttribute("ALRT_CODE").toString());
        ADFUtils.findIterator("fetchDispatchDocumentsIterator").executeQuery();
        GlobalCC.refreshUI(tblDispatchDocs);
    }

    private void clearComponents() {
        txtShtDescription.setValue(null);
        txtDescription.setValue(null);
        soDispatchDocument.setValue(null);
        GlobalCC.refreshUI(txtShtDescription);
        GlobalCC.refreshUI(txtDescription);
        GlobalCC.refreshUI(soDispatchDocument);
    }
}
