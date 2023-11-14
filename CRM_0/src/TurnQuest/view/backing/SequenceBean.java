package TurnQuest.view.backing;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class SequenceBean {
    private RichInputText txtCode;
    private RichInputText txtType;
    private RichInputText txtNoType;
    private RichInputText txtBrnCode;
    private RichInputNumberSpinbox txtUwyr;
    private RichInputNumberSpinbox txtNextNo;
    private RichCommandButton btnCreateUpdateSeq;
    private RichTable tblSequence;
    private RichCommandButton btnAcceptBranch;
    private RichTable tblBranches;
    private RichInputText txtBranchName;
    private RichCommandButton btnShowPop;
    private RichInputText txtOrgName;
    private RichInputText txtOrgCode;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichCommandButton btnCreateUpdateAlertType;
    private RichSelectOneChoice orgCode;
    private RichSelectOneChoice choiceClientType;

    public SequenceBean() {
    }

    public String actionShowSequence() {
        resetSequence();
        btnCreateUpdateSeq.setText("Save");
        session.setAttribute("MY_ORG_CODE", null);
        GlobalCC.showPopup("crm:sequencePop");
        return null;
    }

    public String actionDeleteSequence() {
        Object key2 = tblSequence.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        resetSequence();
        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmDeleteSeq" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        return null;
    }

    public String actionEditSequence() {
        btnCreateUpdateSeq.setText("Update");
        Object key2 = tblSequence.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        resetSequence();
        if (nodeBinding != null) {
            session.setAttribute("MY_ORG_CODE",
                                 nodeBinding.getAttribute("org_code"));
            txtCode.setValue(nodeBinding.getAttribute("tseq_code"));
            txtType.setValue(nodeBinding.getAttribute("tseq_type"));
            txtNoType.setValue(nodeBinding.getAttribute("tseq_no_type"));
            txtBrnCode.setValue(nodeBinding.getAttribute("tseq_brn"));
            txtUwyr.setValue(nodeBinding.getAttribute("tseq_uwyr"));
            txtNextNo.setValue(nodeBinding.getAttribute("tseq_next_no"));
            txtBranchName.setValue(nodeBinding.getAttribute("branch_name"));
            orgCode.setValue(nodeBinding.getAttribute("org_code"));
            choiceClientType.setValue(nodeBinding.getAttribute("clientType"));


            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:sequencePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        return null;
    }

    public void resetSequence() {
        txtCode.setValue(null);
        txtType.setValue(null);
        txtNoType.setValue(null);
        txtBrnCode.setValue(null);
        txtUwyr.setValue(null);
        txtNextNo.setValue(null);
        txtBranchName.setValue(null);
        choiceClientType.setValue(null);


    }

    public String actionCreateUpdateSequence() {
        String tseq_code = GlobalCC.checkNullValues(txtCode.getValue());
        String tseq_type = GlobalCC.checkNullValues(txtType.getValue());
        String tseq_no_type = GlobalCC.checkNullValues(txtNoType.getValue());
        String tseq_brn = GlobalCC.checkNullValues(txtBrnCode.getValue());
        String tseq_uwyr = GlobalCC.checkNullValues(txtUwyr.getValue());
        String tseq_next_no = GlobalCC.checkNullValues(txtNextNo.getValue());
        String org_code =
            GlobalCC.checkNullValues(session.getAttribute("MY_ORG_CODE"));
        String clientType =
            GlobalCC.checkNullValues(choiceClientType.getValue());
        if (org_code == null) {
            GlobalCC.errorValueNotEntered("ERROR: ORGANIZATION REQUIRED::");
            return null;
        }
        if (tseq_type == null) {
            GlobalCC.errorValueNotEntered("ERROR: TYPE REQUIRED::");
            return null;
        }
        if (tseq_no_type == null) {
            GlobalCC.errorValueNotEntered("ERROR: NO TYPE REQUIRED ::");
            return null;
        }
        if (tseq_brn == null) {
            GlobalCC.errorValueNotEntered("ERROR: BRANCH REQUIRED::");
            return null;
        }
        if (tseq_uwyr == null) {
            GlobalCC.errorValueNotEntered("ERROR: UNDERWRITING YEAR  REQUIRED::");
            return null;
        }

        if (tseq_next_no == null) {
            GlobalCC.errorValueNotEntered("ERROR: NEXT NO  REQUIRED::");
            return null;
        }

        OracleConnection conn = null;
        String Query =
            "BEGIN TQC_SEQUENCES_PKG.TQC_SEQUENCES_PRC(?,?,?,?,?,?,?,?,?,?);END;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        String option = null;

        if (btnCreateUpdateSeq.getText().equalsIgnoreCase("Update")) {
            option = "E";
            if (tseq_code == null) {
                GlobalCC.errorValueNotEntered("ERROR: CODE REQUIRED::");
            }

        } else {
            option = "A";
        }
        try {

            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);


            // Add or Edit
            cst.setString(1, option);
            cst.setBigDecimal(2,
                              tseq_code == null ? null : new BigDecimal(tseq_code));
            cst.setString(3, tseq_type);
            cst.setString(4, tseq_no_type);
            cst.setBigDecimal(5,
                              tseq_brn == null ? null : new BigDecimal(tseq_brn));
            cst.setBigDecimal(6,
                              tseq_uwyr == null ? null : new BigDecimal(tseq_uwyr));
            cst.setBigDecimal(7,
                              tseq_next_no == null ? null : new BigDecimal(tseq_next_no));
            cst.registerOutParameter(8, OracleTypes.VARCHAR);
            cst.setBigDecimal(9,
                              org_code == null ? null : new BigDecimal(org_code));
            cst.setString(10, clientType);
            cst.execute();
            String err = cst.getString(8);
            if (err != null) {
                GlobalCC.INFORMATIONREPORTING(err);

            } else {
                if (option.equalsIgnoreCase("E")) {

                    ADFUtils.findIterator("findSequencesIterator").executeQuery();
                    GlobalCC.refreshUI(tblSequence);
                    GlobalCC.INFORMATIONREPORTING("Record Updated Successfully:");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:sequencePop" +
                                         "').hide(hints);");
                } else {
                    GlobalCC.INFORMATIONREPORTING("Record Created Successfully:");
                    ADFUtils.findIterator("findSequencesIterator").executeQuery();
                    GlobalCC.refreshUI(tblSequence);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:sequencePop" +
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


    public void actionConfirmedDeleteSeq(DialogEvent dialogEvent) {
        OracleConnection conn = null;
        ;
        if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.no))) {

        } else if (dialogEvent.getOutcome().equals((DialogEvent.Outcome.yes))) {
            Object key2 = tblSequence.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                try {
                    DBConnector dbConnector = new DBConnector();
                    String Query =
                        "BEGIN TQC_SEQUENCES_PKG.TQC_SEQUENCES_PRC(?,?,?,?,?,?,?,?,?);END;";
                    DBConnector connector = new DBConnector();
                    OracleCallableStatement cst = null;
                    String option = null;
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    // Add or Edit
                    cst.setString(1, "D");
                    cst.setBigDecimal(2,
                                      (BigDecimal)nodeBinding.getAttribute("tseq_code"));
                    cst.setString(3, null);
                    cst.setString(4, null);
                    cst.setBigDecimal(5, null);
                    cst.setBigDecimal(6, null);
                    cst.setBigDecimal(7, null);
                    cst.registerOutParameter(8, OracleTypes.VARCHAR);
                    cst.setBigDecimal(9, null);
                    cst.execute();
                    String err = cst.getString(8);
                    if (err != null) {
                        GlobalCC.INFORMATIONREPORTING(err);

                    } else {

                        ADFUtils.findIterator("findSequencesIterator").executeQuery();
                        GlobalCC.refreshUI(tblSequence);
                        GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully:");


                    }
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
    }

    public void setTxtCode(RichInputText txtCode) {
        this.txtCode = txtCode;
    }

    public RichInputText getTxtCode() {
        return txtCode;
    }

    public void setTxtType(RichInputText txtType) {
        this.txtType = txtType;
    }

    public RichInputText getTxtType() {
        return txtType;
    }

    public void setTxtNoType(RichInputText txtNoType) {
        this.txtNoType = txtNoType;
    }

    public RichInputText getTxtNoType() {
        return txtNoType;
    }

    public void setTxtBrnCode(RichInputText txtBrnCode) {
        this.txtBrnCode = txtBrnCode;
    }

    public RichInputText getTxtBrnCode() {
        return txtBrnCode;
    }

    public void setTxtUwyr(RichInputNumberSpinbox txtUwyr) {
        this.txtUwyr = txtUwyr;
    }

    public RichInputNumberSpinbox getTxtUwyr() {
        return txtUwyr;
    }

    public void setTxtNextNo(RichInputNumberSpinbox txtNextNo) {
        this.txtNextNo = txtNextNo;
    }

    public RichInputNumberSpinbox getTxtNextNo() {
        return txtNextNo;
    }

    public void setBtnCreateUpdateSeq(RichCommandButton btnCreateUpdateSeq) {
        this.btnCreateUpdateSeq = btnCreateUpdateSeq;
    }

    public RichCommandButton getBtnCreateUpdateSeq() {
        return btnCreateUpdateSeq;
    }

    public void setTblSequence(RichTable tblSequence) {
        this.tblSequence = tblSequence;
    }

    public RichTable getTblSequence() {
        return tblSequence;
    }

    public void setBtnAcceptBranch(RichCommandButton btnAcceptBranch) {
        this.btnAcceptBranch = btnAcceptBranch;
    }

    public RichCommandButton getBtnAcceptBranch() {
        return btnAcceptBranch;
    }

    public String actionAcceptBranch() {
        Object key2 = tblBranches.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBranchName.setValue(nodeBinding.getAttribute("name"));
            txtBrnCode.setValue(nodeBinding.getAttribute("code"));
            GlobalCC.refreshUI(txtBranchName);
            GlobalCC.refreshUI(txtBrnCode);

        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:branchesPop" + "').hide(hints);");

        return null;
    }

    public String actionShowBranch() {
        if (session.getAttribute("MY_ORG_CODE") != null) {
            GlobalCC.refreshUI(tblBranches);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:branchesPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("Select organization first::");
        }
        return null;
    }

    public void setTblBranches(RichTable tblBranches) {
        this.tblBranches = tblBranches;
    }

    public RichTable getTblBranches() {
        return tblBranches;
    }

    public void setTxtBranchName(RichInputText txtBranchName) {
        this.txtBranchName = txtBranchName;
    }

    public RichInputText getTxtBranchName() {
        return txtBranchName;
    }

    public void setBtnShowPop(RichCommandButton btnShowPop) {
        this.btnShowPop = btnShowPop;
    }

    public RichCommandButton getBtnShowPop() {
        return btnShowPop;
    }

    public void setTxtOrgName(RichInputText txtOrgName) {
        this.txtOrgName = txtOrgName;
    }

    public RichInputText getTxtOrgName() {
        return txtOrgName;
    }

    public void setTxtOrgCode(RichInputText txtOrgCode) {
        this.txtOrgCode = txtOrgCode;
    }

    public RichInputText getTxtOrgCode() {
        return txtOrgCode;
    }

    public void actionOrglistener(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
            RichSelectOneChoice comp =
                (RichSelectOneChoice)valueChangeEvent.getComponent();
            Row row =
                ADFUtils.findIterator("findOrganizationIterator").getRowAtRangeIndex(new Integer(comp.getValue().toString()));
            session.setAttribute("MY_ORG_CODE", null);
            session.setAttribute("MY_ORG_CODE", row.getAttribute("orgCode"));
            txtBrnCode.setValue(null);
            txtBranchName.setValue(null);

            ADFUtils.findIterator("findAllBranchesIterator").executeQuery();
            GlobalCC.refreshUI(tblBranches);
            GlobalCC.refreshUI(txtBranchName);
            GlobalCC.refreshUI(txtBrnCode);

        }
    }


    public String actionCreateUpdateAlertType() {
        // Add event code here...
        return null;
    }

    public void setBtnCreateUpdateAlertType(RichCommandButton btnCreateUpdateAlertType) {
        this.btnCreateUpdateAlertType = btnCreateUpdateAlertType;
    }

    public RichCommandButton getBtnCreateUpdateAlertType() {
        return btnCreateUpdateAlertType;
    }

    public void actionConfirmedDeleteAlertType(DialogEvent dialogEvent) {
        // Add event code here...
    }

    public void setOrgCode(RichSelectOneChoice orgCode) {
        this.orgCode = orgCode;
    }

    public RichSelectOneChoice getOrgCode() {
        return orgCode;
    }

    public void setChoiceClientType(RichSelectOneChoice choiceClientType) {
        this.choiceClientType = choiceClientType;
    }

    public RichSelectOneChoice getChoiceClientType() {
        return choiceClientType;
    }

    public String userSelected() {
        // Add event code here...
        return null;
    }
}
