package TurnQuest.view.reports;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.sql.SQLException;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;


public class ReportGroupsManinp {
    private RichTable tblReportGroups;
    private RichInputText txtReportName;
    private RichInputText txtRptGroupCode;
    private RichTable tblRptGrpDivisions;
    private RichTable tblDivisions;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichInputText txtDivisionCode;
    private RichInputText txtRptGrpDivisionCode;
    private RichInputText txtOrgName;
    private RichInputText txtShtDesc;
    private RichTable organizationTbl;
    private RichInputText txtRating;
    private RichTable starndardsTbl;

    public ReportGroupsManinp() {
    }

    public void setTblReportGroups(RichTable tblReportGroups) {
        this.tblReportGroups = tblReportGroups;
    }

    public RichTable getTblReportGroups() {
        return tblReportGroups;
    }

    public void setTxtReportName(RichInputText txtReportName) {
        this.txtReportName = txtReportName;
    }

    public RichInputText getTxtReportName() {
        return txtReportName;
    }

    public String actionSaveReport() {
        Object rptGroupCode = txtRptGroupCode.getValue();
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();

        String query = "BEGIN TQC_SETUPS_PKG.report_groups_prc(?,?,?);END;";
        try {


            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            if (rptGroupCode == null)
                cst.setObject(1, "A");
            else
                cst.setObject(1, "E");
            cst.setObject(2, rptGroupCode);
            cst.setObject(3, txtReportName.getValue());
            cst.execute();
            conn.commit();
            conn.close();
            GlobalCC.dismissPopUp("crm", "ReportGroupsPop");
            ADFUtils.findIterator("fetchAllReportGroupsIterator").executeQuery();
            GlobalCC.refreshUI(tblReportGroups);
            GlobalCC.INFORMATIONREPORTING("Successfully Executed Operation");
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String actionNewReportGroup() {
        txtRptGroupCode.setValue(null);
        GlobalCC.showPopUp("crm", "ReportGroupsPop");
        return null;
    }

    public String actionEditReportGroup() {
        Object key = tblReportGroups.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;

        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a report Group to proceeed");
            return null;
        }
        txtRptGroupCode.setValue(nodeBinding.getAttribute("rptGroupCode"));
        txtReportName.setValue(nodeBinding.getAttribute("rptGroupName"));


        GlobalCC.showPopUp("crm", "ReportGroupsPop");
        return null;
    }


    public String actionDeleteReportGroup() {
        Object key = tblReportGroups.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;

        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a report Group to proceeed");
            return null;
        }
        txtRptGroupCode.setValue(nodeBinding.getAttribute("rptGroupCode"));
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();

        String query = "BEGIN TQC_SETUPS_PKG.report_groups_prc(?,?);END;";
        try {


            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, "D");
            cst.setObject(2, txtRptGroupCode.getValue());
            cst.execute();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchAllReportGroupsIterator").executeQuery();
            GlobalCC.refreshUI(tblReportGroups);
            GlobalCC.INFORMATIONREPORTING("Successfully Executed Operation");
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTxtRptGroupCode(RichInputText txtRptGroupCode) {
        this.txtRptGroupCode = txtRptGroupCode;
    }

    public RichInputText getTxtRptGroupCode() {
        return txtRptGroupCode;
    }

    public void setTblRptGrpDivisions(RichTable tblRptGrpDivisions) {
        this.tblRptGrpDivisions = tblRptGrpDivisions;
    }

    public RichTable getTblRptGrpDivisions() {
        return tblRptGrpDivisions;
    }

    public void setTblDivisions(RichTable tblDivisions) {
        this.tblDivisions = tblDivisions;
    }

    public RichTable getTblDivisions() {
        return tblDivisions;
    }

    public String actionNewRptGrpDivision() {
        if (txtRptGroupCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Report Group First");
            return null;
        }
        txtRptGrpDivisionCode.setValue(null);
        ADFUtils.findIterator("findUndefinedRptGroupDivisionsIterator").executeQuery();
        GlobalCC.showPopUp("crm", "rptGroupDivisionsPOP");
        return null;
    }

    public String actionEditRptGrpDivision() {
        if (txtRptGroupCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Report Group First");
            return null;
        }
        Object key = tblRptGrpDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a report Group Division to proceeed");
            return null;
        }
        txtRptGrpDivisionCode.setValue(nodeBinding.getAttribute("rptGroupDivCode"));
        ADFUtils.findIterator("findUndefinedRptGroupDivisionsIterator").executeQuery();
        GlobalCC.showPopUp("crm", "rptGroupDivisionsPOP");
        return null;
    }

    public String actionDeleteRptGrpDivision() {
        if (txtRptGroupCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Report Group First");
            return null;
        }

        Object key = tblRptGrpDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Report Group Division First");
            return null;
        }
        txtRptGrpDivisionCode.setValue(nodeBinding.getAttribute("rptGroupDivCode"));
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();

        String query = "BEGIN TQC_SETUPS_PKG.report_div_groups_prc(?,?);END;";
        try {


            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);

            cst.setObject(1, "D");
            cst.setObject(2, txtRptGrpDivisionCode.getValue());

            cst.execute();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchReportGroupDivisionIterator").executeQuery();
            GlobalCC.refreshUI(tblRptGrpDivisions);
            GlobalCC.INFORMATIONREPORTING("Successfully Executed Operation");

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public void actionTblRptGroupsSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getRemovedSet() != selectionEvent.getAddedSet()) {
            Object key = tblReportGroups.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
            txtRptGroupCode.setValue(nodeBinding.getAttribute("rptGroupCode"));
            session.setAttribute("REPORT_GROUP_CODE",
                                 txtRptGroupCode.getValue());

            ADFUtils.findIterator("fetchReportGroupDivisionIterator").executeQuery();
            GlobalCC.refreshUI(tblRptGrpDivisions);

        }
    }

    public void setTxtDivisionCode(RichInputText txtDivisionCode) {
        this.txtDivisionCode = txtDivisionCode;
    }

    public RichInputText getTxtDivisionCode() {
        return txtDivisionCode;
    }

    public void setTxtRptGrpDivisionCode(RichInputText txtRptGrpDivisionCode) {
        this.txtRptGrpDivisionCode = txtRptGrpDivisionCode;
    }

    public RichInputText getTxtRptGrpDivisionCode() {
        return txtRptGrpDivisionCode;
    }

    public String actionSaveRptGrpDivision() {

        Object key = tblDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("You have Not Selected any Division");
            return null;
        }
        txtDivisionCode.setValue(nodeBinding.getAttribute("DIV_CODE"));
        Object rptGrpDivisionCode = txtRptGrpDivisionCode.getValue();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();

        String query =
            "BEGIN TQC_SETUPS_PKG.report_div_groups_prc(?,?,?,?);END;";
        try {


            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            if (rptGrpDivisionCode == null)
                cst.setObject(1, "A");
            else
                cst.setObject(1, "E");
            cst.setObject(2, rptGrpDivisionCode);
            cst.setObject(3, txtRptGroupCode.getValue());
            cst.setObject(4, txtDivisionCode.getValue());
            cst.execute();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchReportGroupDivisionIterator").executeQuery();
            GlobalCC.refreshUI(tblRptGrpDivisions);
            GlobalCC.INFORMATIONREPORTING("Successfully Executed Operation");
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }


        return null;
    }

    public void setTxtOrgName(RichInputText txtOrgName) {
        this.txtOrgName = txtOrgName;
    }

    public RichInputText getTxtOrgName() {
        return txtOrgName;
    }

    public void setTxtShtDesc(RichInputText txtShtDesc) {
        this.txtShtDesc = txtShtDesc;
    }

    public RichInputText getTxtShtDesc() {
        return txtShtDesc;
    }

    public String saveOrganization() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();

        String query = "BEGIN TQC_SETUPS_PKG.AddUpdateRatingOrg(?,?,?,?);END;";
        try {


            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, session.getAttribute("action"));
            if (session.getAttribute("action").equals("A")) {
                cst.setObject(2, null);
            } else {
                cst.setObject(2, session.getAttribute("rorgCode"));
            }
            if (txtOrgName.getValue() != null) {
                cst.setObject(3, txtOrgName.getValue());
            } else {
                cst.setObject(3, null);
            }
            if (txtShtDesc.getValue() != null) {
                cst.setObject(4, txtShtDesc.getValue());
            } else {
                cst.setObject(4, null);

            }
            cst.execute();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GlobalCC.dismissPopUp("crm", "OrgPop");
        ADFUtils.findIterator("findRatingOrganizationsIterator").executeQuery();
        GlobalCC.refreshUI(organizationTbl);

        GlobalCC.INFORMATIONREPORTING("Successfully Executed Operation");
        return null;
    }

    public String newOrganizationDtls() {
        session.setAttribute("action", "A");
        txtOrgName.setValue(null);
        txtShtDesc.setValue(null);
        GlobalCC.showPopUp("crm", "OrgPop");
        return null;
    }

    public String editOrganizations() {
        session.setAttribute("action", "E");
        Object key = organizationTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        session.setAttribute("rorgCode", r.getAttribute("rorgCode"));
        txtOrgName.setValue(r.getAttribute("rorgDesc"));
        txtShtDesc.setValue(r.getAttribute("rorgShtDesc"));
        GlobalCC.refreshUI(txtOrgName);
        GlobalCC.refreshUI(txtShtDesc);
        GlobalCC.showPopUp("crm", "OrgPop");
        return null;
    }

    public String deleteOrganizations() {
        Object key = organizationTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();

        String query = "BEGIN TQC_SETUPS_PKG.AddUpdateRatingOrg(?,?,?,?);END;";
        try {


            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, "D");
            cst.setObject(2, r.getAttribute("rorgCode"));
            cst.setObject(3, null);
            cst.setObject(4, null);
            cst.execute();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        }
        ADFUtils.findIterator("findRatingOrganizationsIterator").executeQuery();
        GlobalCC.refreshUI(organizationTbl);
        GlobalCC.INFORMATIONREPORTING("Successfully Executed Operation");
        return null;
    }

    public void setOrganizationTbl(RichTable organizationTbl) {
        this.organizationTbl = organizationTbl;
    }

    public RichTable getOrganizationTbl() {
        return organizationTbl;
    }

    public String newOrgRatingStarndards() {
        session.setAttribute("action", "A");
        txtRating.setValue(null);
        session.setAttribute("orsCode", null);
        GlobalCC.showPopUp("crm", "ratingPop");

        return null;
    }

    public String editRatingStarndards() {
        session.setAttribute("action", "E");
        Object key = starndardsTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("orsCode", r.getAttribute("orsCode"));
        txtRating.setValue(r.getAttribute("orsDesc"));
        GlobalCC.refreshUI(txtRating);
        GlobalCC.showPopUp("crm", "ratingPop");
        return null;
    }

    public String deleteDeleteRating() {
        Object key = starndardsTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();

        String query = "BEGIN TQC_SETUPS_PKG.AddUpdateRating(?,?,?,?);END;";
        try {


            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, "D");
            cst.setObject(2, r.getAttribute("orsCode"));
            cst.setObject(3, null);
            cst.setObject(4, null);
            cst.execute();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findRatingStartndardsIterator").executeQuery();
            GlobalCC.refreshUI(starndardsTbl);
            GlobalCC.dismissPopUp("crm", "OrgPop");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        }
        return null;
    }

    public void setTxtRating(RichInputText txtRating) {
        this.txtRating = txtRating;
    }

    public RichInputText getTxtRating() {
        return txtRating;
    }

    public String saveRating() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();

        String query = "BEGIN TQC_SETUPS_PKG.AddUpdateRating(?,?,?,?);END;";
        try {


            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setObject(1, session.getAttribute("action"));
            if (session.getAttribute("action").equals("A")) {
                cst.setObject(2, null);
            } else {
                cst.setObject(2, session.getAttribute("orsCode"));
            }
            if (txtRating.getValue() != null) {
                cst.setObject(3, txtRating.getValue());
            } else {
                cst.setObject(3, null);
            }
            if (session.getAttribute("rorgCode") != null) {
                cst.setObject(4, session.getAttribute("rorgCode"));
            } else {
                cst.setObject(4, null);

            }
            cst.execute();
            conn.commit();
            conn.close();
            GlobalCC.dismissPopUp("crm", "ratingPop");
            ADFUtils.findIterator("findRatingStartndardsIterator").executeQuery();
            GlobalCC.refreshUI(starndardsTbl);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        }

        return null;
    }

    public void setStarndardsTbl(RichTable starndardsTbl) {
        this.starndardsTbl = starndardsTbl;
    }

    public RichTable getStarndardsTbl() {
        return starndardsTbl;
    }

    public void selectReportGrp(SelectionEvent selectionEvent) {
        Object key = organizationTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return;
        }
        session.setAttribute("rorgCode", r.getAttribute("rorgCode"));
        ADFUtils.findIterator("findRatingStartndardsIterator").executeQuery();
        GlobalCC.refreshUI(starndardsTbl);
    }
}
