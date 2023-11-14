package TurnQuest.view.client;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ClientTitleBacking {
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichInputText txtTitleCode;
    private RichInputText txtTitleShortDesc;
    private RichInputText txtTitleDesc;
    private RichCommandButton btnSaveTitle;
    private RichTable tblClientTitles;
    private RichPopup confirmationDialog;
    private RichOutputLabel olConfirmMsgValue;
    private RichCommandButton btnEditTitle;
    private RichCommandButton btnDeleteTitle;
    private RichInputText txtFailedRemarks;
    private RichInputText txtApplicationLevel;
    private RichCommandButton btnSaveRemark;
    private RichCommandButton btnEditRemark;
    private RichCommandButton btnDeleteRemark;
    private RichTable ddFailedRemarksTbl;

    public ClientTitleBacking() {
    }

    public void setTxtTitleCode(RichInputText txtTitleCode) {
        this.txtTitleCode = txtTitleCode;
    }

    public RichInputText getTxtTitleCode() {
        return txtTitleCode;
    }

    public void setTxtTitleShortDesc(RichInputText txtTitleShortDesc) {
        this.txtTitleShortDesc = txtTitleShortDesc;
    }

    public RichInputText getTxtTitleShortDesc() {
        return txtTitleShortDesc;
    }

    public void setTxtTitleDesc(RichInputText txtTitleDesc) {
        this.txtTitleDesc = txtTitleDesc;
    }

    public RichInputText getTxtTitleDesc() {
        return txtTitleDesc;
    }

    public void setBtnSaveTitle(RichCommandButton btnSaveTitle) {
        this.btnSaveTitle = btnSaveTitle;
    }

    public RichCommandButton getBtnSaveTitle() {
        return btnSaveTitle;
    }

    public void setTblClientTitles(RichTable tblClientTitles) {
        this.tblClientTitles = tblClientTitles;
    }

    public RichTable getTblClientTitles() {
        return tblClientTitles;
    }

    public void setConfirmationDialog(RichPopup confirmationDialog) {
        this.confirmationDialog = confirmationDialog;
    }

    public RichPopup getConfirmationDialog() {
        return confirmationDialog;
    }

    public void setOlConfirmMsgValue(RichOutputLabel olConfirmMsgValue) {
        this.olConfirmMsgValue = olConfirmMsgValue;
    }

    public RichOutputLabel getOlConfirmMsgValue() {
        return olConfirmMsgValue;
    }

    public void refreshFields() {
        txtTitleCode.setValue(null);
        txtTitleShortDesc.setValue(null);
        txtTitleDesc.setValue(null);
    }

    public String actionSaveTitle() {
        // Check if the user wishes to SAVE or UPDATE
        if (btnSaveTitle.getText().equals("Edit")) {
            actionEditTitle();
        } else {

            // Proceed to save
            String titleCode =
                GlobalCC.checkNullValues(txtTitleCode.getValue());
            String titleShortDesc =
                GlobalCC.checkNullValues(txtTitleShortDesc.getValue());
            String titleDesc =
                GlobalCC.checkNullValues(txtTitleDesc.getValue());


            if (titleShortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
                return null;

            } else if (titleDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is empty");
                return null;

            } else {
                String Query =
                    "begin TQC_SETUPS_PKG.client_titles_prc(?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "A");
                    cst.setBigDecimal(2, null);
                    cst.setString(3, titleShortDesc);
                    cst.setString(4, titleDesc);

                    cst.execute();

                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:clientTitlePop" +
                                         "').hide(hints);");

                    ADFUtils.findIterator("fetchAllClientTitlesIterator").executeQuery();
                    GlobalCC.refreshUI(tblClientTitles);

                    GlobalCC.INFORMATIONREPORTING("New Record SAVED Successfully!");

                    cst.close();
                    conn.commit();
                    conn.close();
                    btnEditTitle.setDisabled(true);
                    btnDeleteTitle.setDisabled(true);
                    GlobalCC.refreshUI(btnEditTitle);
                    GlobalCC.refreshUI(btnDeleteTitle);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionEditTitle() {
        // Proceed to save
        String titleCode = GlobalCC.checkNullValues(txtTitleCode.getValue());
        String titleShortDesc =
            GlobalCC.checkNullValues(txtTitleShortDesc.getValue());
        String titleDesc = GlobalCC.checkNullValues(txtTitleDesc.getValue());


        if (titleCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Title Code is Empty");
            return null;

        } else if (titleShortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short description is empty");
            return null;

        } else if (titleDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is empty");
            return null;

        } else {
            String Query =
                "begin TQC_SETUPS_PKG.client_titles_prc(?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "E");
                cst.setBigDecimal(2, new BigDecimal(titleCode));
                cst.setString(3, titleShortDesc);
                cst.setString(4, titleDesc);

                cst.execute();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:clientTitlePop" + "').hide(hints);");

                ADFUtils.findIterator("fetchAllClientTitlesIterator").executeQuery();
                GlobalCC.refreshUI(tblClientTitles);
                btnEditTitle.setDisabled(true);
                btnDeleteTitle.setDisabled(true);
                GlobalCC.refreshUI(btnEditTitle);
                GlobalCC.refreshUI(btnDeleteTitle);

                GlobalCC.INFORMATIONREPORTING("Record UPDATED Successfully!");

                cst.close();
                conn.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionNewCientTitle() {
        refreshFields();
        btnSaveTitle.setText("Save");
        // Open the popup dialog for payment modes
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:clientTitlePop" + "').show(hints);");
        return null;
    }

    public String actionEditClientTitle() {
        Object key2 = tblClientTitles.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtTitleCode.setValue(nodeBinding.getAttribute("code"));
            txtTitleShortDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            txtTitleDesc.setValue(nodeBinding.getAttribute("description"));

            btnSaveTitle.setText("Edit");

            // Open the popup dialog for payment modes
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:clientTitlePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String confirmDeleteAction() {
        Object key2 = tblClientTitles.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmationDialog" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String actionDeleteClientTitle() {
        Object key2 = tblClientTitles.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String titleCode = null;
            String titleShortDesc = null;
            String titleDesc = null;

            titleCode = nodeBinding.getAttribute("code").toString();

            if (titleCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Title Code is Empty");
                return null;

            } else {
                String Query =
                    "begin TQC_SETUPS_PKG.client_titles_prc(?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "D");
                    cst.setBigDecimal(2, new BigDecimal(titleCode));
                    cst.setString(3, null);
                    cst.setString(4, null);

                    cst.execute();

                    cst.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchAllClientTitlesIterator").executeQuery();
                    GlobalCC.refreshUI(tblClientTitles);
                    btnEditTitle.setDisabled(true);
                    btnDeleteTitle.setDisabled(true);
                    GlobalCC.refreshUI(btnEditTitle);
                    GlobalCC.refreshUI(btnDeleteTitle);

                    GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully!");
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public void confirmationDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteClientTitle();
        }
    }

    public void tblClientTitle(SelectionEvent selectionEvent) {
        btnEditTitle.setDisabled(false);
        btnDeleteTitle.setDisabled(false);
        GlobalCC.refreshUI(btnEditTitle);
        GlobalCC.refreshUI(btnDeleteTitle);
    }

    public void setBtnEditTitle(RichCommandButton btnEditTitle) {
        this.btnEditTitle = btnEditTitle;
    }

    public RichCommandButton getBtnEditTitle() {
        return btnEditTitle;
    }

    public void setBtnDeleteTitle(RichCommandButton btnDeleteTitle) {
        this.btnDeleteTitle = btnDeleteTitle;
    }

    public RichCommandButton getBtnDeleteTitle() {
        return btnDeleteTitle;
    }

    public String newFailedRemarks() {
        session.setAttribute("action", "A");
        txtFailedRemarks.setValue(null);
        txtApplicationLevel.setValue(null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:failedRemarksPop" + "').show(hints);");
        return null;
    }

    public String editFailedRemarks() {
        session.setAttribute("action", "E");
        Object key = ddFailedRemarksTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        session.setAttribute("dfrCode", r.getAttribute("dfrCode"));
        txtFailedRemarks.setValue(r.getAttribute("dfrFailedRemarks"));
        txtApplicationLevel.setValue(r.getAttribute("dfrApplicationLevel"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:failedRemarksPop" + "').show(hints);");

        return null;
    }

    public String deleteFailedRemarks() {
        Object key2 = ddFailedRemarksTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("dfrCode",
                                 nodeBinding.getAttribute("dfrCode"));
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmationDialog" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public void setTxtFailedRemarks(RichInputText txtFailedRemarks) {
        this.txtFailedRemarks = txtFailedRemarks;
    }

    public RichInputText getTxtFailedRemarks() {
        return txtFailedRemarks;
    }

    public void setTxtApplicationLevel(RichInputText txtApplicationLevel) {
        this.txtApplicationLevel = txtApplicationLevel;
    }

    public RichInputText getTxtApplicationLevel() {
        return txtApplicationLevel;
    }

    public void setBtnSaveRemark(RichCommandButton btnSaveRemark) {
        this.btnSaveRemark = btnSaveRemark;
    }

    public RichCommandButton getBtnSaveRemark() {
        return btnSaveRemark;
    }

    public void setBtnEditRemark(RichCommandButton btnEditRemark) {
        this.btnEditRemark = btnEditRemark;
    }

    public RichCommandButton getBtnEditRemark() {
        return btnEditRemark;
    }

    public void setBtnDeleteRemark(RichCommandButton btnDeleteRemark) {
        this.btnDeleteRemark = btnDeleteRemark;
    }

    public RichCommandButton getBtnDeleteRemark() {
        return btnDeleteRemark;
    }

    public void setDdFailedRemarksTbl(RichTable ddFailedRemarksTbl) {
        this.ddFailedRemarksTbl = ddFailedRemarksTbl;
    }

    public RichTable getDdFailedRemarksTbl() {
        return ddFailedRemarksTbl;
    }

    public void comfirmDeleteAction(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            deleteRmks();
        }
    }

    private String deleteRmks() {
        Object key2 = ddFailedRemarksTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            String Query =
                "begin TQC_SETUPS_PKG.updateddfailedrmks(?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setObject(2,
                              new BigDecimal(nodeBinding.getAttribute("dfrCode").toString()));
                cst.setString(3, null);
                cst.setString(4, null);

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchDDFailedRemarksIterator").executeQuery();
                GlobalCC.refreshUI(ddFailedRemarksTbl);
                GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully!");
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String actionSaveFailRmks() {
        Object key2 = ddFailedRemarksTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        String Query =
            "begin TQC_SETUPS_PKG.updateddfailedrmks(?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {
            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setObject(1, session.getAttribute("action"));
            if (session.getAttribute("action").equals("A")) {
                cst.setObject(2, null);
            } else {
                cst.setObject(2,
                              new BigDecimal(nodeBinding.getAttribute("dfrCode").toString()));
            }
            cst.setObject(3, txtFailedRemarks.getValue());
            cst.setObject(4, txtApplicationLevel.getValue());
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchDDFailedRemarksIterator").executeQuery();
            GlobalCC.refreshUI(ddFailedRemarksTbl);
            GlobalCC.INFORMATIONREPORTING("Record Inserted Successfully!");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:failedRemarksPop" + "').hide(hints);");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }
}
