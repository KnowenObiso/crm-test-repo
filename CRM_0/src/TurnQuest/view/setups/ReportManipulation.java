package TurnQuest.view.setups;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.sql.SQLException;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ReportManipulation {
    private RichTable reportsModuleTbl;
    private RichInputText moduleName;
    private RichInputText moduleDescription;
    private RichInputText systems;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable subModuleTbl;
    private RichInputText txtName;
    private RichInputText txtReportSubDesc;
    private RichSelectBooleanCheckbox chRptDtls;
    private RichSelectBooleanCheckbox chqAssignedRpt;
    private RichTable allReportsTbl;
    private RichTable assignedReports;
    private RichInputText txtReportDesc;
    private RichOutputLabel saveReportDetails;
    private RichInputText txtReportDescAss;

    public ReportManipulation() {
    }

    public void selectReportModule(SelectionEvent selectionEvent) {
        Object key = reportsModuleTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return;
        }
        moduleName.setValue(r.getAttribute("srmName"));
        moduleDescription.setValue(r.getAttribute("srmDesc"));
        systems.setValue(r.getAttribute("srmSysName"));
        session.setAttribute("srmCode", r.getAttribute("srmCode"));
        GlobalCC.refreshUI(moduleName);
        GlobalCC.refreshUI(moduleDescription);
        GlobalCC.refreshUI(systems);
        ADFUtils.findIterator("fetchSystemRptSubModulesIterator").executeQuery();
        GlobalCC.refreshUI(subModuleTbl);
    }

    public void setReportsModuleTbl(RichTable reportsModuleTbl) {
        this.reportsModuleTbl = reportsModuleTbl;
    }

    public RichTable getReportsModuleTbl() {
        return reportsModuleTbl;
    }

    public void setModuleName(RichInputText moduleName) {
        this.moduleName = moduleName;
    }

    public RichInputText getModuleName() {
        return moduleName;
    }

    public void setModuleDescription(RichInputText moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public RichInputText getModuleDescription() {
        return moduleDescription;
    }

    public void setSystems(RichInputText systems) {
        this.systems = systems;
    }

    public RichInputText getSystems() {
        return systems;
    }

    public void setSubModuleTbl(RichTable subModuleTbl) {
        this.subModuleTbl = subModuleTbl;
    }

    public RichTable getSubModuleTbl() {
        return subModuleTbl;
    }

    public String newReportSubMod() {
        session.setAttribute("action", "A");
        txtName.setValue(null);
        txtReportSubDesc.setValue(null);
        GlobalCC.showPop("pt1:subModulePop");
        return null;
    }

    public void setTxtName(RichInputText txtName) {
        this.txtName = txtName;
    }

    public RichInputText getTxtName() {
        return txtName;
    }

    public void setTxtReportSubDesc(RichInputText txtReportSubDesc) {
        this.txtReportSubDesc = txtReportSubDesc;
    }

    public RichInputText getTxtReportSubDesc() {
        return txtReportSubDesc;
    }

    public String editReportSubMod() {
        session.setAttribute("action", "E");
        Object key = subModuleTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        txtName.setValue(r.getAttribute("rsmName"));
        txtReportSubDesc.setValue(r.getAttribute("rsmDesc"));
        session.setAttribute("rsmCode", r.getAttribute("rsmCode"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:subModulePop" + "').show(hints);");
        return null;
    }

    public String saveSubModuleDtls() {
        String action;
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SETUPS_PKG.addReportSubModule(?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {
            action = session.getAttribute("action").toString();
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, (String)session.getAttribute("action"));
            if (action.equals("A")) {
                stmt.setObject(2, null);
            } else {
                stmt.setObject(2, session.getAttribute("rsmCode"));
            }
            stmt.setObject(3, txtName.getValue());
            stmt.setObject(4, txtReportSubDesc.getValue());
            stmt.setObject(5, session.getAttribute("srmCode"));
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchSystemRptSubModulesIterator").executeQuery();
            GlobalCC.refreshUI(subModuleTbl);
            GlobalCC.hidePopup("pt1:subModulePop");
            String message = "Successfull!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String assignReport() {
        int rowcount = allReportsTbl.getRowCount();
        Object reportsCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query = "begin TQC_SETUPS_PKG.AssignReport(?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 0; i < rowcount; i++) {
                allReportsTbl.setRowIndex(i);
                Object key = allReportsTbl.getRowKey();
                allReportsTbl.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)allReportsTbl.getRowData();
                if (chRptDtls.isSelected()) {

                    reportsCode = nodeBinding.getAttribute("rptCode");
                    statement.setObject(1, reportsCode);
                    statement.setObject(2, session.getAttribute("rsmCode"));
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchUnAssignedReportsIterator").executeQuery();
            GlobalCC.refreshUI(allReportsTbl);
            ADFUtils.findIterator("fetchAssignedReportsIterator").executeQuery();
            GlobalCC.refreshUI(assignedReports);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String UnAssignedReport() {
        int rowcount = assignedReports.getRowCount();
        Object reportsCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query = "begin TQC_SETUPS_PKG.UnAssignReport(?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 0; i < rowcount; i++) {
                assignedReports.setRowIndex(i);
                Object key = assignedReports.getRowKey();
                assignedReports.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)assignedReports.getRowData();
                if (chqAssignedRpt.isSelected()) {

                    reportsCode = nodeBinding.getAttribute("rptCode");
                    statement.setObject(1, reportsCode);
                    statement.setObject(2, session.getAttribute("rsmCode"));
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchUnAssignedReportsIterator").executeQuery();
            GlobalCC.refreshUI(allReportsTbl);
            ADFUtils.findIterator("fetchAssignedReportsIterator").executeQuery();
            GlobalCC.refreshUI(assignedReports);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setChRptDtls(RichSelectBooleanCheckbox chRptDtls) {
        this.chRptDtls = chRptDtls;
    }

    public RichSelectBooleanCheckbox getChRptDtls() {
        return chRptDtls;
    }

    public void setChqAssignedRpt(RichSelectBooleanCheckbox chqAssignedRpt) {
        this.chqAssignedRpt = chqAssignedRpt;
    }

    public RichSelectBooleanCheckbox getChqAssignedRpt() {
        return chqAssignedRpt;
    }

    public void setAllReportsTbl(RichTable allReportsTbl) {
        this.allReportsTbl = allReportsTbl;
    }

    public RichTable getAllReportsTbl() {
        return allReportsTbl;
    }

    public void setAssignedReports(RichTable assignedReports) {
        this.assignedReports = assignedReports;
    }

    public RichTable getAssignedReports() {
        return assignedReports;
    }

    public void selectReportSubModule(SelectionEvent selectionEvent) {
        Object key = subModuleTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return;
        }
        session.setAttribute("rsmCode", r.getAttribute("rsmCode"));
        ADFUtils.findIterator("fetchUnAssignedReportsIterator").executeQuery();
        GlobalCC.refreshUI(allReportsTbl);
        ADFUtils.findIterator("fetchAssignedReportsIterator").executeQuery();
        GlobalCC.refreshUI(assignedReports);
    }

    public String UpdateReport() {
        int rowcount = allReportsTbl.getRowCount();
        Object reportsCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_SETUPS_PKG.UpdateReportDetails(?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 0; i < rowcount; i++) {
                allReportsTbl.setRowIndex(i);
                Object key = allReportsTbl.getRowKey();
                allReportsTbl.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)allReportsTbl.getRowData();
                if (chRptDtls.isSelected()) {

                    reportsCode = nodeBinding.getAttribute("rptCode");
                    statement.setObject(1, reportsCode);
                    statement.setObject(2, txtReportDesc.getValue());
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchUnAssignedReportsIterator").executeQuery();
            GlobalCC.refreshUI(allReportsTbl);
            ADFUtils.findIterator("fetchAssignedReportsIterator").executeQuery();
            GlobalCC.refreshUI(assignedReports);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTxtReportDesc(RichInputText txtReportDesc) {
        this.txtReportDesc = txtReportDesc;
    }

    public RichInputText getTxtReportDesc() {
        return txtReportDesc;
    }

    public void setSaveReportDetails(RichOutputLabel saveReportDetails) {
        this.saveReportDetails = saveReportDetails;
    }

    public RichOutputLabel getSaveReportDetails() {
        return saveReportDetails;
    }

    public String UpdateUnAssignReport() {
        int rowcount = assignedReports.getRowCount();
        Object reportsCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query = "begin TQC_SETUPS_PKG.AssignReport(?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 0; i < rowcount; i++) {
                assignedReports.setRowIndex(i);
                Object key = assignedReports.getRowKey();
                assignedReports.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)assignedReports.getRowData();
                if (chqAssignedRpt.isSelected()) {

                    reportsCode = nodeBinding.getAttribute("rptCode");
                    statement.setObject(1, reportsCode);
                    statement.setObject(2, txtReportDescAss.getValue());
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchUnAssignedReportsIterator").executeQuery();
            GlobalCC.refreshUI(allReportsTbl);
            ADFUtils.findIterator("fetchAssignedReportsIterator").executeQuery();
            GlobalCC.refreshUI(assignedReports);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTxtReportDescAss(RichInputText txtReportDescAss) {
        this.txtReportDescAss = txtReportDescAss;
    }

    public RichInputText getTxtReportDescAss() {
        return txtReportDescAss;
    }
}
