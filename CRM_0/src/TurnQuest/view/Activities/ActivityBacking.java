package TurnQuest.view.Activities;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ActivityBacking {
    private RichDialog dlgNewEditActivity;
    private RichTable tblActivities;
    private RichInputText txtActSubject;
    private RichInputDate dtActWEF;
    private RichInputDate dtActWET;
    private RichInputNumberSpinbox noActDuration;
    private RichInputText txtActLocation;

    private RichInputText txtActTtype;
    private RichInputText txtActAssignedTo;
    private RichInputText txtActDescription;
    private RichSelectOneChoice chActReminder;
    private RichInputText txtActTypeCode;
    private RichInputText txtActUserCode;
    private RichInputText txtActTeamCode;
    private RichInputText txtRelatedAccount;
    private RichInputText txtRelatedAccountCode;
    private RichInputText txtActTeam;
    private RichInputText txtActCode;
    private RichTable tblAccountsList;
    private RichTable tblTeamList;
    private RichTable tblActivityTypesList;
    private RichTable tblUserList;
    private RichPanelBox pnBoxActivityDetails;
    private RichTable tblStatuses;
    private RichInputText txtStatus;
    private RichInputText txtStatusId;
    private RichTable tblNotes;
    private RichDialog dlgNewEditNote;
    private RichInputText txtNoteSubject;
    private RichInputText txtNoteRelatedAccountCode;
    private RichInputText txtNoteDescription;
    private RichInputText txtNoteRelatedAccountName;
    private RichCommandButton btnAccoutsListOK;
    private RichPanelBox pnBoxNoteDetails;
    private RichInputText txtNoteCode;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable tblActParticipants;
    private RichDialog dlgNewEditTask;
    private RichInputDate dtTaskFrom;
    private RichInputDate dtTaskTo;
    private RichInputText txtTaskAccountName;
    private RichInputText txtTaskCode;
    private RichInputText txtTaskStatusCode;
    private RichInputText txtTaskPriorityCode;
    private RichInputText txtTaskAccountCode;
    private RichInputText txtTaskPriority;
    private RichTable tblActTasks;
    private RichInputText txtTaskSubject;
    private RichTable tblPriorityLevels;
    private RichPanelBox pnBoxTaskDetails;
    private RichCommandButton btnSelectParticipants;
    private RichTable tblUnInvitedParticipants;
    private RichSelectBooleanCheckbox chBoxSelectedPart;
    private RichTable tblStatusDefinition;
    private RichInputText txtStatusDesc;
    private RichInputText txtStatusShortDesc;

    private RichInputDate dtActivityTimer;
    private RichOutputLabel lblActivityTimer;
    private RichInputText txtStatusCode;
    private RichTable tblPrioriyLevelsDefinition;
    private RichInputText txtPLShortDesc;
    private RichInputText txtPLDescription;
    private RichInputText txtPLCode;
    private RichDialog dlNewEditPL;
    private RichDialog dlNewEditStatus;
    private RichInputFile infileAttachment;
    private static String filename;
    private static long filesize;
    private static String fileContentType;

    private static InputStream fileStream;
    private RichTable tblEmailTemplates;
    private RichInputText txtMsgTempCode;
    private RichInputText txtEmailTemplate;
    private RichTable securityQuestionsTbl;


    public void setDlgNewEditActivity(RichDialog dlgNewEditActivity) {
        this.dlgNewEditActivity = dlgNewEditActivity;
    }

    public RichDialog getDlgNewEditActivity() {
        return dlgNewEditActivity;
    }

    public void setTblActivities(RichTable tblActivities) {
        this.tblActivities = tblActivities;
    }

    public RichTable getTblActivities() {
        return tblActivities;
    }

    public String actionNewActivity() {
        dlgNewEditActivity.setTitle("New Activity");
        txtStatus.setValue(null);
        txtStatusId.setValue(null);
        txtActSubject.setValue(null);
        dtActWEF.setValue(null);
        dtActWET.setValue(null);
        noActDuration.setValue(null);
        txtActLocation.setValue(null);
        txtActTtype.setValue(null);
        txtActAssignedTo.setValue(session.getAttribute("Username"));
        txtRelatedAccount.setValue(null);
        txtActDescription.setValue(null);
        chActReminder.setValue(null);
        txtActTypeCode.setValue(null);
        txtActUserCode.setValue(session.getAttribute("UserCode"));
        txtActTeamCode.setValue(null);
        txtRelatedAccount.setValue(null);
        txtRelatedAccountCode.setValue(null);
        txtActTeam.setValue(null);
        txtActCode.setValue(null);
        txtMsgTempCode.setValue(null);
        txtEmailTemplate.setValue(null);
        lblActivityTimer.setVisible(false);
        dtActivityTimer.setValue(null);
        dtActivityTimer.setVisible(false);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:newEditActivityPop" + "').show(hints);");

        return null;
    }

    public String actionEditActivity() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to edit"));
            return null;
        }

        Object key = tblActivities.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        dlgNewEditActivity.setTitle("Edit Activity");
        txtActSubject.setValue(nodeBinding.getAttribute("activitysubject"));
        dtActWEF.setValue(nodeBinding.getAttribute("activityWEF"));
        dtActWET.setValue(nodeBinding.getAttribute("activityWET"));
        noActDuration.setValue(nodeBinding.getAttribute("activityDuration"));
        txtActLocation.setValue(nodeBinding.getAttribute("activityLocation"));
        txtActTtype.setValue(nodeBinding.getAttribute("activityType"));
        txtActAssignedTo.setValue(nodeBinding.getAttribute("activityAssignedUser"));
        txtRelatedAccount.setValue(nodeBinding.getAttribute("activityRelatedAccount"));
        txtActDescription.setValue(nodeBinding.getAttribute("activityDescription"));
        chActReminder.setValue(nodeBinding.getAttribute("activityReminder"));
        txtActTypeCode.setValue(nodeBinding.getAttribute("activityTypeCode"));
        txtActUserCode.setValue(nodeBinding.getAttribute("activityAssignedTo"));
        txtActTeamCode.setValue(nodeBinding.getAttribute("activityTeam"));
        txtRelatedAccount.setValue(nodeBinding.getAttribute("activityRelatedAccount"));
        txtRelatedAccountCode.setValue(nodeBinding.getAttribute("activityRelatedTo"));
        txtActTeam.setValue(nodeBinding.getAttribute("activityTeamName"));
        txtStatus.setValue(nodeBinding.getAttribute("activityStatus"));
        txtStatusId.setValue(nodeBinding.getAttribute("activityStatusId"));
        txtMsgTempCode.setValue(nodeBinding.getAttribute("msgTemplateCode"));
        txtEmailTemplate.setValue(nodeBinding.getAttribute("msgTemplateDesc"));
        dtActivityTimer.setValue(nodeBinding.getAttribute("activityReminderTime"));
        boolean showTimer = chActReminder.getValue() != null;
        if (chActReminder.getValue() != null)
            showTimer =
                    chActReminder.getValue().toString().equalsIgnoreCase("Y");
        dtActivityTimer.setVisible(showTimer);
        lblActivityTimer.setVisible(showTimer);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:newEditActivityPop" + "').show(hints);");

        return null;

    }

    public String actionDeleteActivity() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to delete"));
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_ACTIVITIES_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, "D");
            stmt.setBigDecimal(2, (BigDecimal)txtActCode.getValue());
            stmt.setBigDecimal(3, null);
            stmt.setDate(4, null);
            stmt.setDate(5, null);
            stmt.setBigDecimal(6, null);
            stmt.setString(7, null);
            stmt.setString(8, null);
            stmt.setBigDecimal(9, null);
            stmt.setBigDecimal(10, null);
            stmt.setBigDecimal(11, null);
            stmt.setString(12, null);
            stmt.setString(13, null);
            stmt.setBigDecimal(14, null);
            stmt.setString(15, null);

            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findActivitiesIterator").executeQuery();
            GlobalCC.refreshUI(tblActivities);

            String message = "Record DELETED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }

        return null;
    }

    public String actionShowActivityTypes() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:ActivityTypesPop" + "').show(hints);");
        return null;
    }

    public String actionShowAccounts() {
        btnAccoutsListOK.setShortDesc("ACTIVITY");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:AccountsListPop" + "').show(hints);");
        return null;
    }

    public String actionShowUsers() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:ActivityUsersPop" + "').show(hints);");
        return null;
    }

    public String actionShowuserGroups() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:userTeamsPop" + "').show(hints);");
        return null;
    }

    public String actionSaveActivity() {
        String activityCode = GlobalCC.checkNullValues(txtActCode.getValue());
        String subject = GlobalCC.checkNullValues(txtActSubject.getValue());
        java.sql.Date wef = GlobalCC.extractDate(dtActWEF);
        java.sql.Date wet = GlobalCC.extractDate(dtActWET);
        //java.sql.Date timer = GlobalCC.extractDate(dtActivityTimer);
        String duration = GlobalCC.checkNullValues(noActDuration.getValue());
        String location = GlobalCC.checkNullValues(txtActLocation.getValue());
        String activityTypeCode =
            GlobalCC.checkNullValues(txtActTypeCode.getValue());
        String assignedTo =
            GlobalCC.checkNullValues(txtActUserCode.getValue());
        String relatedTo =
            GlobalCC.checkNullValues(txtRelatedAccountCode.getValue());
        String description =
            GlobalCC.checkNullValues(txtActDescription.getValue());
        String reminder = GlobalCC.checkNullValues(chActReminder.getValue());
        String statusId = GlobalCC.checkNullValues(txtStatusId.getValue());
        String teamCode = GlobalCC.checkNullValues(txtActTeamCode.getValue());
        String msgTempCode =
            GlobalCC.checkNullValues(txtMsgTempCode.getValue());


        SimpleDateFormat sdf1 =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/yyyy");

        SimpleDateFormat sdf3 =
            new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa");

        if (session.getAttribute("actyCode") == null) {
            GlobalCC.INFORMATIONREPORTING("Please select an Activity Type");
            return null;
        }
        if (subject == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter activity Subject"));
            return null;
        }
        if (wef == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter activity starting date/time"));
            return null;
        }
        if (location == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter activity venue"));
            return null;
        }
        if (activityTypeCode == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select activity type"));
            return null;
        }
        if (subject == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter activity Subject"));
            return null;
        }
        //      if (activityCode == null) {
        //          GlobalCC.EXCEPTIONREPORTING(new Exception("Enter activity Code"));
        //          return null;
        //      }

        if (!GlobalCC.validateWefWetValues(dtActWEF.getValue(),
                                           dtActWET.getValue())) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Ensure the Activity start  date is earlier than the activity ending date"));
            return null;
        }

        GlobalCC.dismissPopUp("crm", "newEditActivityPop");


        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_ACTIVITIES_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (activityCode == null) {
                stmt.setString(1, "A");
                stmt.setBigDecimal(2, null);
            } else {
                stmt.setString(1, "E");
                stmt.setBigDecimal(2, new BigDecimal(activityCode));
            }
            stmt.setBigDecimal(3, new BigDecimal(activityTypeCode));
            stmt.setDate(4, wef);
            stmt.setDate(5, wet);
            stmt.setBigDecimal(6,
                               duration != null ? new BigDecimal(duration) : null);
            stmt.setString(7, subject);
            stmt.setString(8, location);
            stmt.setBigDecimal(9,
                               assignedTo != null ? new BigDecimal(assignedTo) :
                               null);
            stmt.setBigDecimal(10,
                               relatedTo != null ? new BigDecimal(relatedTo) :
                               null);
            stmt.setBigDecimal(11,
                               statusId != null ? new BigDecimal(statusId) :
                               null);
            stmt.setString(12, description);
            stmt.setString(13, reminder);
            stmt.setBigDecimal(14,
                               teamCode != null ? new BigDecimal(teamCode) :
                               null);
            if (dtActivityTimer.getValue() == null) {
                stmt.setString(15, null);
            } else {
                if (dtActivityTimer.getValue().toString().contains(":")) {
                    Date MyDate =
                        sdf1.parse(dtActivityTimer.getValue().toString());
                    stmt.setString(15, sdf2.format(MyDate));
                } else {
                    stmt.setString(15,
                                   GlobalCC.upDateParseDate(dtActivityTimer.getValue().toString()));
                }
            }

            stmt.setString(16, msgTempCode);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            session.removeAttribute("actyCode");
            ADFUtils.findIterator("findActivitiesIterator").executeQuery();
            GlobalCC.refreshUI(tblActivities);
            GlobalCC.dismissPopUp("crm", "newEditActivityPop");
            String message = "Record Saved Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String actionCancelActivity() {
        // Add event code here...
        return null;
    }

    public String actionCancelAccounts() {

        return null;
    }

    public String actionAcceptAccount() {

        Object key = tblAccountsList.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            if (btnAccoutsListOK.getShortDesc().equalsIgnoreCase("ACTIVITY")) {
                txtRelatedAccountCode.setValue(nodeBinding.getAttribute("accountCode"));
                txtRelatedAccount.setValue(nodeBinding.getAttribute("accountName"));
                GlobalCC.refreshUI(pnBoxActivityDetails);
            } else if (btnAccoutsListOK.getShortDesc().equalsIgnoreCase("NOTE")) {
                txtNoteRelatedAccountCode.setValue(nodeBinding.getAttribute("accountCode"));
                txtNoteRelatedAccountName.setValue(nodeBinding.getAttribute("accountName"));
                GlobalCC.refreshUI(pnBoxNoteDetails);
            } else if (btnAccoutsListOK.getShortDesc().equalsIgnoreCase("TASK")) {
                txtTaskAccountCode.setValue(nodeBinding.getAttribute("accountCode"));
                txtTaskAccountName.setValue(nodeBinding.getAttribute("accountName"));
                GlobalCC.refreshUI(pnBoxTaskDetails);
            }
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:AccountsListPop" + "').hide(hints);");
            return null;
        }
        return null;

    }

    public void setTxtActSubject(RichInputText txtActSubject) {
        this.txtActSubject = txtActSubject;
    }

    public RichInputText getTxtActSubject() {
        return txtActSubject;
    }

    public void setDtActWEF(RichInputDate dtActWEF) {
        this.dtActWEF = dtActWEF;
    }

    public RichInputDate getDtActWEF() {
        return dtActWEF;
    }

    public void setDtActWET(RichInputDate dtActWET) {
        this.dtActWET = dtActWET;
    }

    public RichInputDate getDtActWET() {
        return dtActWET;
    }

    public void setNoActDuration(RichInputNumberSpinbox noActDuration) {
        this.noActDuration = noActDuration;
    }

    public RichInputNumberSpinbox getNoActDuration() {
        return noActDuration;
    }

    public void setTxtActLocation(RichInputText txtActLocation) {
        this.txtActLocation = txtActLocation;
    }

    public RichInputText getTxtActLocation() {
        return txtActLocation;
    }


    public void setTxtActTtype(RichInputText txtActTtype) {
        this.txtActTtype = txtActTtype;
    }

    public RichInputText getTxtActTtype() {
        return txtActTtype;
    }

    public void setTxtActAssignedTo(RichInputText txtActAssignedTo) {
        this.txtActAssignedTo = txtActAssignedTo;
    }

    public RichInputText getTxtActAssignedTo() {
        return txtActAssignedTo;
    }


    public void setTxtActDescription(RichInputText txtActDescription) {
        this.txtActDescription = txtActDescription;
    }

    public RichInputText getTxtActDescription() {
        return txtActDescription;
    }

    public void setChActReminder(RichSelectOneChoice chActReminder) {
        this.chActReminder = chActReminder;
    }

    public RichSelectOneChoice getChActReminder() {
        return chActReminder;
    }


    public void setTxtActTypeCode(RichInputText txtActTypeCode) {
        this.txtActTypeCode = txtActTypeCode;
    }

    public RichInputText getTxtActTypeCode() {
        return txtActTypeCode;
    }

    public void setTxtActUserCode(RichInputText txtActUserCode) {
        this.txtActUserCode = txtActUserCode;
    }

    public RichInputText getTxtActUserCode() {
        return txtActUserCode;
    }

    public void setTxtActTeamCode(RichInputText txtActTeamCode) {
        this.txtActTeamCode = txtActTeamCode;
    }

    public RichInputText getTxtActTeamCode() {
        return txtActTeamCode;
    }

    public String actionAcceptActivityType() {
        Object key = tblActivityTypesList.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtActTypeCode.setValue(nodeBinding.getAttribute("actyCode"));
            session.setAttribute("actyCode",
                                 nodeBinding.getAttribute("actyCode"));
            txtActTtype.setValue(nodeBinding.getAttribute("actyDesc"));
            GlobalCC.refreshUI(pnBoxActivityDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:ActivityTypesPop" + "').hide(hints);");

        return null;
    }

    public String actionCancelActivityType() {
        // Add event code here...
        return null;
    }

    public String actionAcceptUser() {
        Object key = tblUserList.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtActUserCode.setValue(nodeBinding.getAttribute("userCode"));
            txtActAssignedTo.setValue(nodeBinding.getAttribute("username"));
            GlobalCC.refreshUI(pnBoxActivityDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:ActivityUsersPop" + "').hide(hints);");
        return null;
    }

    public void setTxtRelatedAccount(RichInputText txtRelatedAccount) {
        this.txtRelatedAccount = txtRelatedAccount;
    }

    public RichInputText getTxtRelatedAccount() {
        return txtRelatedAccount;
    }

    public void setTxtRelatedAccountCode(RichInputText txtRelatedAccountCode) {
        this.txtRelatedAccountCode = txtRelatedAccountCode;
    }

    public RichInputText getTxtRelatedAccountCode() {
        return txtRelatedAccountCode;
    }

    public void setTxtActTeam(RichInputText txtActTeam) {
        this.txtActTeam = txtActTeam;
    }

    public RichInputText getTxtActTeam() {
        return txtActTeam;
    }


    public void setTxtActCode(RichInputText txtActCode) {
        this.txtActCode = txtActCode;
    }

    public RichInputText getTxtActCode() {
        return txtActCode;
    }

    public void actiontblActivitiesSelected(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            Object key = tblActivities.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            txtActCode.setValue(nodeBinding.getAttribute("activityCode"));
            dtActWEF.setValue(nodeBinding.getAttribute("activityWEF"));
            txtActAssignedTo.setValue(nodeBinding.getAttribute("activityAssignedUser"));
            txtActUserCode.setValue(nodeBinding.getAttribute("activityAssignedTo"));
            txtRelatedAccount.setValue(nodeBinding.getAttribute("activityRelatedAccount"));
            txtRelatedAccountCode.setValue(nodeBinding.getAttribute("activityRelatedTo"));
            txtMsgTempCode.setValue(nodeBinding.getAttribute("msgTemplateCode"));
            session.setAttribute("ACTIVITY_CODE", txtActCode.getValue());
            ADFUtils.findIterator("findActivityNotesIterator").executeQuery();
            GlobalCC.refreshUI(tblNotes);
            ADFUtils.findIterator("findActivityTasksIterator").executeQuery();
            GlobalCC.refreshUI(tblActTasks);
            ADFUtils.findIterator("findActivityParticipantsIterator").executeQuery();
            GlobalCC.refreshUI(tblActParticipants);

        }
    }


    public String actionAcceptTeam() {
        Object key = tblTeamList.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        {
            txtActTeamCode.setValue(nodeBinding.getAttribute("userCode"));
            txtActTeam.setValue(nodeBinding.getAttribute("username"));
            GlobalCC.refreshUI(pnBoxActivityDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:userTeamsPop" + "').hide(hints);");

        return null;
    }

    public void setTblAccountsList(RichTable tblAccountsList) {
        this.tblAccountsList = tblAccountsList;
    }

    public RichTable getTblAccountsList() {
        return tblAccountsList;
    }

    public void setTblTeamList(RichTable tblTeamList) {
        this.tblTeamList = tblTeamList;
    }

    public RichTable getTblTeamList() {
        return tblTeamList;
    }

    public void setTblActivityTypesList(RichTable tblActivityTypesList) {
        this.tblActivityTypesList = tblActivityTypesList;
    }

    public RichTable getTblActivityTypesList() {
        return tblActivityTypesList;
    }

    public void setTblUserList(RichTable tblUserList) {
        this.tblUserList = tblUserList;
    }

    public RichTable getTblUserList() {
        return tblUserList;
    }

    public void setPnBoxActivityDetails(RichPanelBox pnBoxActivityDetails) {
        this.pnBoxActivityDetails = pnBoxActivityDetails;
    }

    public RichPanelBox getPnBoxActivityDetails() {
        return pnBoxActivityDetails;
    }

    public void setTblStatuses(RichTable tblStatuses) {
        this.tblStatuses = tblStatuses;
    }

    public RichTable getTblStatuses() {
        return tblStatuses;
    }

    public String actionAcceptStatus() {
        Object key = tblStatuses.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtStatusId.setValue(nodeBinding.getAttribute("statusId"));
            txtStatus.setValue(nodeBinding.getAttribute("statusDecription"));
            GlobalCC.refreshUI(pnBoxActivityDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:ActivityStatusPop" + "').hide(hints);");
        return null;

    }

    public String actionShowStatuses() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:ActivityStatusPop" + "').show(hints);");
        return null;
    }

    public void setTxtStatus(RichInputText txtStatus) {
        this.txtStatus = txtStatus;
    }

    public RichInputText getTxtStatus() {
        return txtStatus;
    }

    public void setTxtStatusId(RichInputText txtStatusId) {
        this.txtStatusId = txtStatusId;
    }

    public RichInputText getTxtStatusId() {
        return txtStatusId;
    }

    public String actionNewNote() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to add a Notes"));
            return null;
        }
        dlgNewEditNote.setTitle("New Note");
        txtNoteCode.setValue(null);
        infileAttachment.setValue(null);
        txtNoteRelatedAccountName.setValue(txtRelatedAccount.getValue());
        txtNoteDescription.setValue(null);
        txtNoteRelatedAccountCode.setValue(txtRelatedAccountCode.getValue());
        txtNoteSubject.setValue(null);


        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:newEditNotePop" + "').show(hints);");
        return null;
    }

    public String actionEditNote() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to edit its Notes"));
            return null;
        }

        Object key = tblNotes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a note to edit "));
            return null;
        }
        dlgNewEditNote.setTitle("Edit Note");
        txtNoteSubject.setValue(nodeBinding.getAttribute("noteSubject"));
        txtNoteDescription.setValue(nodeBinding.getAttribute("noteDescription"));
        txtNoteRelatedAccountName.setValue(nodeBinding.getAttribute("relatedAccount"));
        txtNoteRelatedAccountCode.setValue(nodeBinding.getAttribute("noteAcccountCode"));
        txtNoteCode.setValue(nodeBinding.getAttribute("noteCode"));
        // infileAttachment.setValue(nodeBinding.getAttribute("noteAttachmentName"));

        GlobalCC.refreshUI(infileAttachment);
        GlobalCC.showPopUp("crm", "newEditNotePop");
        return null;
    }

    public String actionDeleteNote() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to delete its Notes"));
            return null;
        }

        Object key = tblNotes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a note to dlete "));
            return null;
        }
        BigDecimal noteCode = (BigDecimal)nodeBinding.getAttribute("noteCode");

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_ACTIVITY_NOTES_PRC(?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);


            stmt.setString(1, "D");

            stmt.setBigDecimal(2, noteCode);

            stmt.setString(3, null);
            stmt.setString(4, null); //contact code
            stmt.setString(5, null);
            stmt.setString(6, null);
            stmt.setBLOB(7, null); //attachment
            stmt.setString(8, null);
            stmt.setString(9, null); //attachment type
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            session.setAttribute("ACTIVITY_CODE", txtActCode.getValue());
            ADFUtils.findIterator("findActivityNotesIterator").executeQuery();
            GlobalCC.refreshUI(tblNotes);

            String message = "Record Deleted Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void setTblNotes(RichTable tblNotes) {
        this.tblNotes = tblNotes;
    }

    public RichTable getTblNotes() {
        return tblNotes;
    }

    public void setDlgNewEditNote(RichDialog dlgNewEditNote) {
        this.dlgNewEditNote = dlgNewEditNote;
    }

    public RichDialog getDlgNewEditNote() {
        return dlgNewEditNote;
    }

    public void setTxtNoteSubject(RichInputText txtNoteSubject) {
        this.txtNoteSubject = txtNoteSubject;
    }

    public RichInputText getTxtNoteSubject() {
        return txtNoteSubject;
    }

    public void setTxtNoteRelatedAccountCode(RichInputText txtNoteRelatedAccountCode) {
        this.txtNoteRelatedAccountCode = txtNoteRelatedAccountCode;
    }

    public RichInputText getTxtNoteRelatedAccountCode() {
        return txtNoteRelatedAccountCode;
    }

    public void setTxtNoteDescription(RichInputText txtNoteDescription) {
        this.txtNoteDescription = txtNoteDescription;
    }

    public RichInputText getTxtNoteDescription() {
        return txtNoteDescription;
    }

    public void setTxtNoteRelatedAccountName(RichInputText txtNoteRelatedAccountName) {
        this.txtNoteRelatedAccountName = txtNoteRelatedAccountName;
    }

    public RichInputText getTxtNoteRelatedAccountName() {
        return txtNoteRelatedAccountName;
    }

    public void setBtnAccoutsListOK(RichCommandButton btnAccoutsListOK) {
        this.btnAccoutsListOK = btnAccoutsListOK;
    }

    public RichCommandButton getBtnAccoutsListOK() {
        return btnAccoutsListOK;
    }

    public String showAccountsNotes() {
        btnAccoutsListOK.setShortDesc("NOTE");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:AccountsListPop" + "').show(hints);");

        return null;
    }

    public void setPnBoxNoteDetails(RichPanelBox pnBoxNoteDetails) {
        this.pnBoxNoteDetails = pnBoxNoteDetails;
    }

    public RichPanelBox getPnBoxNoteDetails() {
        return pnBoxNoteDetails;
    }

    public void setTxtNoteCode(RichInputText txtNoteCode) {
        this.txtNoteCode = txtNoteCode;
    }

    public RichInputText getTxtNoteCode() {
        return txtNoteCode;
    }

    public String actionSaveNote() {

        String subject = GlobalCC.checkNullValues(txtNoteSubject.getValue());
        String description =
            GlobalCC.checkNullValues(txtNoteDescription.getValue());
        String account =
            GlobalCC.checkNullValues(txtNoteRelatedAccountCode.getValue());
        String noteCode = GlobalCC.checkNullValues(txtNoteCode.getValue());
        String actCode = GlobalCC.checkNullValues(txtActCode.getValue());

        if (subject == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter note Subject"));
            return null;
        }
        if (description == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter note description"));
            return null;
        }

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_ACTIVITY_NOTES_PRC(?,?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (noteCode == null) {
                stmt.setString(1, "A");
                stmt.setString(2, null);
            } else {
                stmt.setString(1, "E");
                stmt.setString(2, noteCode);
            }
            stmt.setString(3, account);
            stmt.setString(4, null); //contact code
            stmt.setString(5, subject);
            stmt.setString(6, description);
            stmt.setBlob(7, fileStream); //attachment
            stmt.setString(8, actCode);
            stmt.setString(9, fileContentType); //attachment type
            stmt.setString(10, filename); //attachment name

            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            session.setAttribute("ACTIVITY_CODE", txtActCode.getValue());
            ADFUtils.findIterator("findActivityNotesIterator").executeQuery();
            GlobalCC.refreshUI(tblNotes);
            GlobalCC.dismissPopUp("crm", "newEditNotePop");

            String message = "Record Saved Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String actionCancelNote() {
        // Add event code here...
        return null;
    }

    public String actionNewTask() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to add a Task"));
            return null;
        }
        dlgNewEditTask.setTitle("New Task");
        txtTaskSubject.setValue(null);
        dtTaskFrom.setValue(null);
        dtTaskTo.setValue(null);
        txtTaskAccountName.setValue(txtRelatedAccount.getValue());
        txtTaskCode.setValue(null);

        txtTaskStatusCode.setValue(null);
        txtTaskPriorityCode.setValue(null);
        txtTaskAccountCode.setValue(txtRelatedAccountCode.getValue());
        txtTaskPriority.setValue(null);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:newEditTaskPop" + "').show(hints);");

        return null;
    }

    public String actionEditTask() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to add a Task"));
            return null;
        }
        Object key = tblActTasks.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a task to edit "));
            return null;
        }

        dlgNewEditTask.setTitle("Edit Task");
        txtTaskSubject.setValue(nodeBinding.getAttribute("taskSubject"));
        dtTaskFrom.setValue(nodeBinding.getAttribute("taskFrom"));
        dtTaskTo.setValue(nodeBinding.getAttribute("taskTo"));
        txtTaskAccountName.setValue(nodeBinding.getAttribute("relatedAccount"));
        txtTaskCode.setValue(nodeBinding.getAttribute("tastCode"));

        txtTaskStatusCode.setValue(nodeBinding.getAttribute("taskStatusCode"));
        txtTaskPriorityCode.setValue(nodeBinding.getAttribute("taskPriorityCode"));
        txtTaskAccountCode.setValue(nodeBinding.getAttribute("taskAccountCode"));
        txtTaskPriority.setValue(nodeBinding.getAttribute("priority"));

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:newEditTaskPop" + "').show(hints);");

        return null;
    }

    public String actionDeleteTask() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to delete its task"));
            return null;
        }

        Object key = tblActTasks.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a task to edit "));
            return null;
        }
        BigDecimal taskCode = (BigDecimal)nodeBinding.getAttribute("tastCode");

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_ACTIVITY_TASKS_PRC(?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);


            stmt.setString(1, "D");

            stmt.setBigDecimal(2, taskCode);

            stmt.setString(3, null);
            stmt.setString(4, null);
            stmt.setString(5, null);
            stmt.setString(6, null);
            stmt.setString(7, null);
            stmt.setString(8, null);
            stmt.setString(9, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            session.setAttribute("ACTIVITY_CODE", txtActCode.getValue());
            ADFUtils.findIterator("findActivityTasksIterator").executeQuery();
            GlobalCC.refreshUI(tblActTasks);

            String message = "Record Deleted Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String actionNewParticipant() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to add a participant"));
            return null;
        }
        ADFUtils.findIterator("findUnInvitedAccountsIterator").executeQuery();
        GlobalCC.refreshUI(tblUnInvitedParticipants);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:addParcipantPop" + "').show(hints);");
        return null;
    }

    public String actionEditParticipant() {

        return null;
    }

    public String actionDeleteParticipant() {
        if (txtActCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a activity to remove participant"));
            return null;
        }
        Object key = tblActParticipants.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a participant to remove "));
            return null;
        }
        BigDecimal participantCode =
            (BigDecimal)nodeBinding.getAttribute("participantId");
        String activityCode = GlobalCC.checkNullValues(txtActCode.getValue());

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_ACTIVITY_PARTICIPANTS_PRC(?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, "D");
            stmt.setBigDecimal(2, participantCode);
            stmt.setString(3, null);
            stmt.setString(4, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findActivityParticipantsIterator").executeQuery();
            GlobalCC.refreshUI(tblActParticipants);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public void setTblActParticipants(RichTable tblActParticipants) {
        this.tblActParticipants = tblActParticipants;
    }

    public RichTable getTblActParticipants() {
        return tblActParticipants;
    }

    public void setDlgNewEditTask(RichDialog dlgNewEditTask) {
        this.dlgNewEditTask = dlgNewEditTask;
    }

    public RichDialog getDlgNewEditTask() {
        return dlgNewEditTask;
    }

    public String actionSaveTask() {

        String subject = GlobalCC.checkNullValues(txtTaskSubject.getValue());
        java.sql.Date dateFrom = GlobalCC.extractDate(dtTaskFrom);
        java.sql.Date dateTo = GlobalCC.extractDate(dtTaskTo);
        String accountCode =
            GlobalCC.checkNullValues(txtTaskAccountCode.getValue());
        String taskCode = GlobalCC.checkNullValues(txtTaskCode.getValue());
        String statusCode =
            GlobalCC.checkNullValues(txtTaskStatusCode.getValue());
        String priorityCode =
            GlobalCC.checkNullValues(txtTaskPriorityCode.getValue());
        String actCode = GlobalCC.checkNullValues(txtActCode.getValue());

        if (subject == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter task Subject"));
            return null;
        }
        if (dateFrom == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Enter task beginning time"));
            return null;
        }
        if (!GlobalCC.validateWefWetValues(dtActWEF.getValue(),
                                           dtTaskFrom.getValue())) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Ensure the Activity start  date is earlier than the tast start  date"));
            return null;
        }
        if (!GlobalCC.validateWefWetValues(dtTaskFrom.getValue(),
                                           dtTaskTo.getValue())) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Ensure the Task start  date is earlier than the Task end  date"));
            return null;
        }


        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_ACTIVITY_TASKS_PRC(?,?,?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (taskCode == null) {
                stmt.setString(1, "A");
                stmt.setString(2, null);
            } else {
                stmt.setString(1, "E");
                stmt.setString(2, taskCode);
            }
            stmt.setString(3, actCode);
            stmt.setDate(4, dateFrom);
            stmt.setDate(5, dateTo);
            stmt.setString(6, subject);
            stmt.setString(7, statusCode);
            stmt.setString(8, priorityCode);
            stmt.setString(9, accountCode);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            GlobalCC.dismissPopUp("crm", "newEditTaskPop");
            session.setAttribute("ACTIVITY_CODE", txtActCode.getValue());
            ADFUtils.findIterator("findActivityTasksIterator").executeQuery();
            GlobalCC.refreshUI(tblActTasks);

            String message = "Record Saved Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String actionCancelTask() {
        // Add event code here...
        return null;
    }


    public void setDtTaskFrom(RichInputDate dtTaskFrom) {
        this.dtTaskFrom = dtTaskFrom;
    }

    public RichInputDate getDtTaskFrom() {
        return dtTaskFrom;
    }

    public void setDtTaskTo(RichInputDate dtTaskTo) {
        this.dtTaskTo = dtTaskTo;
    }

    public RichInputDate getDtTaskTo() {
        return dtTaskTo;
    }

    public void setTxtTaskAccountName(RichInputText txtTaskAccountName) {
        this.txtTaskAccountName = txtTaskAccountName;
    }

    public RichInputText getTxtTaskAccountName() {
        return txtTaskAccountName;
    }

    public String actionShowAccountsTask() {
        btnAccoutsListOK.setShortDesc("TASK");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:AccountsListPop" + "').show(hints);");
        return null;
    }

    public void setTxtTaskCode(RichInputText txtTaskCode) {
        this.txtTaskCode = txtTaskCode;
    }

    public RichInputText getTxtTaskCode() {
        return txtTaskCode;
    }

    public void setTxtTaskStatusCode(RichInputText txtTaskStatusCode) {
        this.txtTaskStatusCode = txtTaskStatusCode;
    }

    public RichInputText getTxtTaskStatusCode() {
        return txtTaskStatusCode;
    }

    public void setTxtTaskPriorityCode(RichInputText txtTaskPriorityCode) {
        this.txtTaskPriorityCode = txtTaskPriorityCode;
    }

    public RichInputText getTxtTaskPriorityCode() {
        return txtTaskPriorityCode;
    }

    public void setTxtTaskAccountCode(RichInputText txtTaskAccountCode) {
        this.txtTaskAccountCode = txtTaskAccountCode;
    }

    public RichInputText getTxtTaskAccountCode() {
        return txtTaskAccountCode;
    }

    public void setTxtTaskPriority(RichInputText txtTaskPriority) {
        this.txtTaskPriority = txtTaskPriority;
    }

    public RichInputText getTxtTaskPriority() {
        return txtTaskPriority;
    }

    public void setTblActTasks(RichTable tblActTasks) {
        this.tblActTasks = tblActTasks;
    }

    public RichTable getTblActTasks() {
        return tblActTasks;
    }

    public void actionShowPriorityLevels(ActionEvent actionEvent) {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:priorityLevelsPop" + "').show(hints);");

    }

    public void setTxtTaskSubject(RichInputText txtTaskSubject) {
        this.txtTaskSubject = txtTaskSubject;
    }

    public RichInputText getTxtTaskSubject() {
        return txtTaskSubject;
    }

    public String actionAcceptPriorityLevel() {
        Object key = tblPriorityLevels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtTaskPriorityCode.setValue(nodeBinding.getAttribute("plCode"));
            txtTaskPriority.setValue(nodeBinding.getAttribute("plDescription"));
            GlobalCC.refreshUI(pnBoxTaskDetails);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:priorityLevelsPop" + "').hide(hints);");
        return null;

    }

    public String actionCancelPriorityLevel() {
        // Add event code here...
        return null;
    }

    public void setTblPriorityLevels(RichTable tblPriorityLevels) {
        this.tblPriorityLevels = tblPriorityLevels;
    }

    public RichTable getTblPriorityLevels() {
        return tblPriorityLevels;
    }

    public void setPnBoxTaskDetails(RichPanelBox pnBoxTaskDetails) {
        this.pnBoxTaskDetails = pnBoxTaskDetails;
    }

    public RichPanelBox getPnBoxTaskDetails() {
        return pnBoxTaskDetails;
    }

    public void setBtnSelectParticipants(RichCommandButton btnSelectParticipants) {
        this.btnSelectParticipants = btnSelectParticipants;
    }

    public RichCommandButton getBtnSelectParticipants() {
        return btnSelectParticipants;
    }

    public void setTblUnInvitedParticipants(RichTable tblUnInvitedParticipants) {
        this.tblUnInvitedParticipants = tblUnInvitedParticipants;
    }

    public RichTable getTblUnInvitedParticipants() {
        return tblUnInvitedParticipants;
    }

    public String actionSelectAllParticipants() {
        if (btnSelectParticipants.getText().equalsIgnoreCase("Select All"))
            GlobalCC.selectAll(tblActParticipants, chBoxSelectedPart,
                               btnSelectParticipants);
        else if (btnSelectParticipants.getText().equalsIgnoreCase("Unselect All"))
            GlobalCC.deselectAll(tblActParticipants, chBoxSelectedPart,
                                 "selected", btnSelectParticipants);
        return null;
    }

    public String actionSendInvites() {


        Object msgTempCode = txtMsgTempCode.getValue();
        if (msgTempCode == null) {
            GlobalCC.EXCEPTIONREPORTING("This Activity has no defined Email Template." +
                                        "\nPlease Define an Email Template to continue");
            return null;
        }

        /*
        * Save Email Message
        */
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.send_activity_invites(?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setObject(1, txtActCode.getValue());
            stmt.setObject(2, session.getAttribute("UserCode"));
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            emailInvites(msgTempCode);
            GlobalCC.INFORMATIONREPORTING("Activity has been emailled successfully");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String actionSaveParcipant() {

        /*

      GlobalCC.setUserName(session.getAttribute("Username"));
        HashMap H=GlobalCC.getLoggedInUserDetails();


      System.out.println("H"+H);


        Object rowData=null;

     RowKeySet rks= tblUnInvitedParticipants.getSelectedRowKeys();
     System.out.println( "rks.getSize()"+rks.getSize());
     for(Object rowKey:rks)
     {
        tblUnInvitedParticipants.setRowKey(rowKey);
           rowData=tblUnInvitedParticipants.getRowData();
         JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)rowData;
         System.out.println("nodeBinding("+nodeBinding.getAttribute("accountName"));
         }

      */

        Object key = tblUnInvitedParticipants.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a participant to add "));
            return null;
        }
        BigDecimal participantAccount =
            (BigDecimal)nodeBinding.getAttribute("accountCode");
        String activityCode = GlobalCC.checkNullValues(txtActCode.getValue());

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_ACTIVITY_PARTICIPANTS_PRC(?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, "A");
            stmt.setString(2, null);
            stmt.setString(3, activityCode);
            stmt.setBigDecimal(4, participantAccount);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findActivityParticipantsIterator").executeQuery();
            GlobalCC.refreshUI(tblActParticipants);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }

        return null;

    }

    public String actionCancelParticipant() {

        return null;
    }

    public void setChBoxSelectedPart(RichSelectBooleanCheckbox chBoxSelectedPart) {
        this.chBoxSelectedPart = chBoxSelectedPart;
    }

    public RichSelectBooleanCheckbox getChBoxSelectedPart() {
        return chBoxSelectedPart;
    }

    public String actionNewStatus() {
        session.setAttribute("action", "A");
        dlNewEditStatus.setTitle("New Status");
        txtStatusCode.setValue(null);
        txtStatusDesc.setValue(null);
        txtStatusShortDesc.setValue(null);
        GlobalCC.showPopUp("crm", "p1");

        return null;
    }

    public String actionNewSecurity() {
        session.setAttribute("action", "A");
        dlNewEditStatus.setTitle("New Security Question");
        txtStatusCode.setValue(null);
        txtStatusDesc.setValue(null);
        txtStatusShortDesc.setValue(null);
        GlobalCC.showPopUp("crm", "p1");

        return null;
    }

    public String actionEditStatus() {
        if (txtStatusCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a status to add to edit"));
            return null;
        }
        Object key = tblStatusDefinition.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        txtStatusDesc.setValue(nodeBinding.getAttribute("statusDecription"));
        txtStatusShortDesc.setValue(nodeBinding.getAttribute("statusCode"));
        dlNewEditStatus.setTitle("Edit Status");
        GlobalCC.showPopUp("crm", "p1");
        return null;
    }

    public String actionDeleteStatus() {
        if (txtStatusCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a status to add to delete"));
            return null;
        }
        String code = GlobalCC.checkNullValues(txtStatusCode.getValue());
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_STATUSES_PRC(?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, "D");
            stmt.setString(2, code);


            stmt.setString(3, null);
            stmt.setString(4, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findStatusesIterator").executeQuery();
            GlobalCC.refreshUI(tblStatusDefinition);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public void setTblStatusDefinition(RichTable tblStatusDefinition) {
        this.tblStatusDefinition = tblStatusDefinition;
    }

    public RichTable getTblStatusDefinition() {
        return tblStatusDefinition;
    }

    public void setTxtStatusDesc(RichInputText txtStatusDesc) {
        this.txtStatusDesc = txtStatusDesc;
    }

    public RichInputText getTxtStatusDesc() {
        return txtStatusDesc;
    }

    public void setTxtStatusShortDesc(RichInputText txtStatusShortDesc) {
        this.txtStatusShortDesc = txtStatusShortDesc;
    }

    public RichInputText getTxtStatusShortDesc() {
        return txtStatusShortDesc;
    }

    public String actionSaveStatus() {
        String statusDesc = GlobalCC.checkNullValues(txtStatusDesc.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtStatusShortDesc.getValue());
        String code = GlobalCC.checkNullValues(txtStatusCode.getValue());


        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_STATUSES_PRC(?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (code == null) {
                stmt.setString(1, "A");
                stmt.setString(2, null);
            } else {
                stmt.setString(1, "E");
                stmt.setString(2, code);
            }
            stmt.setString(3, shortDesc);
            stmt.setString(4, statusDesc);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findStatusesIterator").executeQuery();
            GlobalCC.refreshUI(tblStatusDefinition);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public void setDtActivityTimer(RichInputDate dtActivityTimer) {
        this.dtActivityTimer = dtActivityTimer;
    }

    public RichInputDate getDtActivityTimer() {
        return dtActivityTimer;
    }

    public void setLblActivityTimer(RichOutputLabel lblActivityTimer) {
        this.lblActivityTimer = lblActivityTimer;
    }

    public RichOutputLabel getLblActivityTimer() {
        return lblActivityTimer;
    }

    public void actionReminderChanged(ValueChangeEvent attributeChangeEvent) {
        if (attributeChangeEvent.getNewValue() !=
            attributeChangeEvent.getOldValue()) {
            if (attributeChangeEvent.getNewValue().toString().equalsIgnoreCase("Y")) {
                lblActivityTimer.setVisible(true);
                dtActivityTimer.setVisible(true);
                GlobalCC.refreshUI(lblActivityTimer);
                GlobalCC.refreshUI(dtActivityTimer);
            } else {
                lblActivityTimer.setVisible(false);
                dtActivityTimer.setVisible(false);
                GlobalCC.refreshUI(lblActivityTimer);
                GlobalCC.refreshUI(dtActivityTimer);
            }
        }

    }

    public void setTxtStatusCode(RichInputText txtStatusCode) {
        this.txtStatusCode = txtStatusCode;
    }

    public RichInputText getTxtStatusCode() {
        return txtStatusCode;
    }

    public void tblStatusDefinitionSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            Object key = tblStatusDefinition.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            txtStatusCode.setValue(nodeBinding.getAttribute("statusId"));
        }
    }

    public void setTblPrioriyLevelsDefinition(RichTable tblPrioriyLevelsDefinition) {
        this.tblPrioriyLevelsDefinition = tblPrioriyLevelsDefinition;
    }

    public RichTable getTblPrioriyLevelsDefinition() {
        return tblPrioriyLevelsDefinition;
    }

    public void tblPriorityLevelsDefSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            Object key = tblPrioriyLevelsDefinition.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            txtPLCode.setValue(nodeBinding.getAttribute("plCode"));
        }
    }

    public void setTxtPLShortDesc(RichInputText txtPLShortDesc) {
        this.txtPLShortDesc = txtPLShortDesc;
    }

    public RichInputText getTxtPLShortDesc() {
        return txtPLShortDesc;
    }

    public void setTxtPLDescription(RichInputText txtPLDescription) {
        this.txtPLDescription = txtPLDescription;
    }

    public RichInputText getTxtPLDescription() {
        return txtPLDescription;
    }

    public void setTxtPLCode(RichInputText txtPLCode) {
        this.txtPLCode = txtPLCode;
    }

    public RichInputText getTxtPLCode() {
        return txtPLCode;
    }

    public String actionSavePriorityLevel() {
        String PLDesc = GlobalCC.checkNullValues(txtPLDescription.getValue());
        String PLshortDesc =
            GlobalCC.checkNullValues(txtPLShortDesc.getValue());
        String code = GlobalCC.checkNullValues(txtPLCode.getValue());


        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_PRIORITY_LEVEL_PRC(?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (code == null) {
                stmt.setString(1, "A");
                stmt.setString(2, null);
            } else {
                stmt.setString(1, "E");
                stmt.setString(2, code);
            }
            stmt.setString(3, PLDesc);
            stmt.setString(4, PLshortDesc);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findPriorityLevelsIterator").executeQuery();
            GlobalCC.refreshUI(tblPrioriyLevelsDefinition);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }


        return null;
    }

    public void setDlNewEditPL(RichDialog dlNewEditPL) {
        this.dlNewEditPL = dlNewEditPL;
    }

    public RichDialog getDlNewEditPL() {
        return dlNewEditPL;
    }

    public String actionNewPL()

    {
        dlNewEditPL.setTitle("New Priority Level");
        txtPLCode.setValue(null);
        txtPLDescription.setValue(null);
        txtPLShortDesc.setValue(null);
        GlobalCC.showPopUp("crm", "p1");


        return null;
    }

    public String ationEditPL() {
        if (txtPLCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a priority level to add to edit"));
            return null;
        }
        Object key = tblPrioriyLevelsDefinition.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        txtPLDescription.setValue(nodeBinding.getAttribute("plDescription"));
        txtPLShortDesc.setValue(nodeBinding.getAttribute("prShortDescription"));

        GlobalCC.showPopUp("crm", "p1");

        return null;
    }

    public String actionDeletePL() {
        if (txtPLCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("Select a priority level to add to delete"));
            return null;
        }
        String code = GlobalCC.checkNullValues(txtPLCode.getValue());
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_PKG.TQC_PRIORITY_LEVEL_PRC(?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setString(1, "D");

            stmt.setString(2, code);

            stmt.setString(3, null);
            stmt.setString(4, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findPriorityLevelsIterator").executeQuery();
            GlobalCC.refreshUI(tblPrioriyLevelsDefinition);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }
        return null;


    }

    public void setDlNewEditStatus(RichDialog dlNewEditStatus) {
        this.dlNewEditStatus = dlNewEditStatus;
    }

    public RichDialog getDlNewEditStatus() {
        return dlNewEditStatus;
    }

    public void setInfileAttachment(RichInputFile infileAttachment) {
        this.infileAttachment = infileAttachment;
    }

    public RichInputFile getInfileAttachment() {
        return infileAttachment;
    }


    public void fileUpload(ValueChangeEvent valueChangeEvent) {

        UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
        if (_file != null) {
            this.filename = _file.getFilename();
            this.filesize = _file.getLength();
            this.fileContentType = _file.getContentType();
            try {
                fileStream = _file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void actionAttachmentSelected(ActionEvent actionEvent) {
        RichCommandLink command = (RichCommandLink)actionEvent.getSource();
        DBConnector connection = new DBConnector();
        String filename = command.getText();
        filename = filename.replace(" ", "_");

        String query =
            "SELECT ANT_ATTACHMENT,ANT_ATTACHMENT_TYPE  FROM  TQC_ACTIVITY_NOTES " +
            "WHERE ANT_CODE=?   ";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, command.getShortDesc());

            OracleResultSet rs = (OracleResultSet)stmt.executeQuery();
            FacesContext fctx = FacesContext.getCurrentInstance();
            HttpServletResponse response =
                (HttpServletResponse)fctx.getExternalContext().getResponse();
            byte[] bytes = null;
            while (rs.next()) {
                bytes = rs.getBytes(1);
            }
            rs.close();
            stmt.close();
            conn.close();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition",
                               "attachment; filename=" + filename + "");

            OutputStream out;
            out = response.getOutputStream();
            out.write(bytes, 0, bytes.length);
            out.flush();
            out.close();
            fctx.responseComplete();
        }

        catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(new Exception("An error occured while trying to view the attachment"));
            e.printStackTrace();
        }


    }

    private void emailInvites(Object msgTemplateCode) {

        String MessageTemplate = null;
        String sender = GlobalCC.getEmailFrom();
        String receiver = null;
        String subject = null;
        String message = null;
        String senderName = null;
        String senderJobtitle = null;
        GlobalCC.setUserName((String)session.getAttribute("Username"));
        HashMap loggedInUserDetails = GlobalCC.getLoggedInUserDetails();
        senderName = (String)loggedInUserDetails.get("UserFullName");
        senderJobtitle = (String)loggedInUserDetails.get("UserPersonnelRank");
        /*
         * Get Email Template
         */
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ACTIVITIES_CURSOR.getActivity_email_tmplt(?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setObject(1, msgTemplateCode);
            stmt.registerOutParameter(2, OracleTypes.VARCHAR);
            stmt.execute();
            MessageTemplate = stmt.getString(2);
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }

        Object key = tblActivities.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        subject = nodeBinding.getAttribute("activitysubject").toString();

        if (sender == null) {
            GlobalCC.EXCEPTIONREPORTING("Error Determining mail sender address.Contact your Systems Administrator");
        } else {
            int rowcount = tblActParticipants.getRowCount();


            for (int i = 0; i < rowcount; i++) {
                tblActParticipants.setRowIndex(i);

                Object key2 = tblActParticipants.getRowData();
                JUCtrlValueBinding nodeBinding2 = (JUCtrlValueBinding)key2;
                if (nodeBinding2.getAttribute("selected").toString().equalsIgnoreCase("true")) {
                    if (nodeBinding2.getAttribute("accountEmail") != null)
                        receiver =
                                nodeBinding2.getAttribute("accountEmail").toString();
                    message =
                            MessageTemplate.replace("[SENDEE]", nodeBinding2.getAttribute("accountName").toString());
                    message =
                            message.replace("[DATE]", nodeBinding.getAttribute("activityWEF").toString());
                    message = message.replace(" [TIME]", "");
                    message =
                            message.replace("[VENUE]", nodeBinding.getAttribute("activityLocation").toString());
                    message = message.replace("[SENDER]", senderName);
                    message = message.replace("[JOB_TITLE]", senderJobtitle);

                    try{
                        GlobalCC.applicationEmail(receiver, null, subject,
                                              message);
                    }catch(Exception e){
                        GlobalCC.EXCEPTIONREPORTING(e);
                    }
                    

                }
            }

        }
    }

    public void setTblEmailTemplates(RichTable tblEmailTemplates) {
        this.tblEmailTemplates = tblEmailTemplates;
    }

    public RichTable getTblEmailTemplates() {
        return tblEmailTemplates;
    }

    public String actionAcceptEmailTemplate() {
        Object key = tblEmailTemplates.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtMsgTempCode.setValue(nodeBinding.getAttribute("msgtCode"));
            txtEmailTemplate.setValue(nodeBinding.getAttribute("msgtShtDesc"));

        }
        GlobalCC.refreshUI(txtEmailTemplate);

        GlobalCC.dismissPopUp("crm", "EmailTemplatesPOP");
        return null;
    }

    public String actionCancelEmailTempalet() {
        GlobalCC.dismissPopUp("crm", "EmailTemplatesPOP");
        return null;
    }

    public void setTxtMsgTempCode(RichInputText txtMsgTempCode) {
        this.txtMsgTempCode = txtMsgTempCode;
    }

    public RichInputText getTxtMsgTempCode() {
        return txtMsgTempCode;
    }

    public void setTxtEmailTemplate(RichInputText txtEmailTemplate) {
        this.txtEmailTemplate = txtEmailTemplate;
    }

    public RichInputText getTxtEmailTemplate() {
        return txtEmailTemplate;
    }

    public String actionShowEmailTemplates() {
        session.setAttribute("sysCode", "0");
        GlobalCC.showPopUp("crm", "EmailTemplatesPOP");
        return null;
    }

    public void setSecurityQuestionsTbl(RichTable securityQuestionsTbl) {
        this.securityQuestionsTbl = securityQuestionsTbl;
    }

    public RichTable getSecurityQuestionsTbl() {
        return securityQuestionsTbl;
    }

    public String actionSaveQuestion() {
        String action;
        String statusDesc = GlobalCC.checkNullValues(txtStatusDesc.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtStatusShortDesc.getValue());
        String code = GlobalCC.checkNullValues(txtStatusCode.getValue());

        action = session.getAttribute("action").toString();
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ROLES_CURSOR.update_security_questions(?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (action.equals("A")) {
                stmt.setString(1, "A");
                stmt.setString(2, null);
            } else {
                stmt.setString(1, "E");
                stmt.setString(2, code);
            }
            stmt.setString(3, shortDesc);
            stmt.setString(4, statusDesc);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findSecurityQuestionsIterator").executeQuery();
            GlobalCC.refreshUI(securityQuestionsTbl);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String actionEditSecurityQuestions() {
        session.setAttribute("action", "E");
        Object key = securityQuestionsTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        txtStatusCode.setValue(nodeBinding.getAttribute("statusId"));
        txtStatusDesc.setValue(nodeBinding.getAttribute("statusDecription"));
        txtStatusShortDesc.setValue(nodeBinding.getAttribute("statusCode"));
        dlNewEditStatus.setTitle("Edit Question");
        GlobalCC.showPopUp("crm", "p1");
        return null;
    }

    public String actionDeleteSecurityQuestion() {
        Object key = securityQuestionsTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        txtStatusShortDesc.setValue(nodeBinding.getAttribute("statusCode"));
        // String code = GlobalCC.checkNullValues(txtStatusCode.getValue());
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_ROLES_CURSOR.update_security_questions(?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, "D");
            stmt.setObject(2, nodeBinding.getAttribute("statusId"));


            stmt.setString(3, null);
            stmt.setString(4, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findSecurityQuestionsIterator").executeQuery();
            GlobalCC.refreshUI(securityQuestionsTbl);


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }

        return null;
    }

}
