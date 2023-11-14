package TurnQuest.view.setups;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.sql.SQLException;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;


public class SysAppAreaBean {
    private RichInputText txtAppAreaDesc;
    private RichInputText txtSystemName;
    private RichTable tblSystems;
    private RichTable tblSystemAppAreas;
    private RichInputText txtSystemCode;
    private RichInputText txtSysAppAreaCode;

    public SysAppAreaBean() {
    }

    public String actionNewAppArea() {
        txtSystemCode.setValue(null);
        txtSystemName.setValue(null);
        txtAppAreaDesc.setValue(null);
        txtSysAppAreaCode.setValue(null);

        GlobalCC.showPopUp("crm", "SysAppAreaPOP");
        return null;
    }


    public String actionEditAppArea() {
        Object key = tblSystemAppAreas.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a System appicable area to Edit");
            return null;
        }
        txtSystemCode.setValue(nodeBinding.getAttribute("sysCode"));
        txtSystemName.setValue(nodeBinding.getAttribute("sysName"));
        txtAppAreaDesc.setValue(nodeBinding.getAttribute("sysAppAreaDesc"));
        txtSysAppAreaCode.setValue(nodeBinding.getAttribute("sysAppAreaCode"));

        GlobalCC.showPopUp("crm", "SysAppAreaPOP");
        return null;

    }

    public String actionDeleteAppArea() {
        Object key = tblSystemAppAreas.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a System appicable area to Edit");
            return null;
        }
        Object sysAppAreaCode = nodeBinding.getAttribute("sysAppAreaCode");
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query =
            "begin TQC_SETUPS_PKG.system_app_areas_prc(?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();


        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setObject(1, "D");
            stmt.setObject(2, sysAppAreaCode);
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.execute();
            stmt.close();
            conn.commit();

            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("fetchSystemApplicableAreasIterator").executeQuery();
            GlobalCC.refreshUI(tblSystemAppAreas);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTxtAppAreaDesc(RichInputText txtAppAreaDesc) {
        this.txtAppAreaDesc = txtAppAreaDesc;
    }

    public RichInputText getTxtAppAreaDesc() {
        return txtAppAreaDesc;
    }

    public void setTxtSystemName(RichInputText txtSystemName) {
        this.txtSystemName = txtSystemName;
    }

    public RichInputText getTxtSystemName() {
        return txtSystemName;
    }

    public String actionSaveSysAppArea() {
        if (txtSystemCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a System to Proceed");
            return null;
        }
        if (txtAppAreaDesc.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter Applicable Area Description to Proceed");
            return null;
        }
        Object sysAppAreaCode = txtSysAppAreaCode.getValue();
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query =
            "begin TQC_SETUPS_PKG.system_app_areas_prc(?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();


        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (sysAppAreaCode == null)
                stmt.setObject(1, "A");
            else
                stmt.setObject(1, "E");
            stmt.setObject(2, sysAppAreaCode);
            stmt.setObject(3, txtSystemCode.getValue());
            stmt.setObject(4, txtAppAreaDesc.getValue());
            stmt.execute();
            stmt.close();
            conn.commit();
            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("fetchSystemApplicableAreasIterator").executeQuery();
            GlobalCC.refreshUI(tblSystemAppAreas);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        GlobalCC.dismissPopUp("crm", "SysAppAreaPOP");
        return null;
    }

    public String actionAcceptSystem() {
        Object key = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtSystemCode.setValue(nodeBinding.getAttribute("code"));
            txtSystemName.setValue(nodeBinding.getAttribute("name"));
        }
        GlobalCC.refreshUI(txtSystemName);
        GlobalCC.dismissPopUp("crm", "SystemsLOVPop");
        return null;
    }

    public String actionCancelSystem() {
        GlobalCC.dismissPopUp("crm", "SystemsLOVPop");
        return null;
    }

    public void setTblSystems(RichTable tblSystems) {
        this.tblSystems = tblSystems;
    }

    public RichTable getTblSystems() {
        return tblSystems;
    }

    public void setTblSystemAppAreas(RichTable tblSystemAppAreas) {
        this.tblSystemAppAreas = tblSystemAppAreas;
    }

    public RichTable getTblSystemAppAreas() {
        return tblSystemAppAreas;
    }

    public void setTxtSystemCode(RichInputText txtSystemCode) {
        this.txtSystemCode = txtSystemCode;
    }

    public RichInputText getTxtSystemCode() {
        return txtSystemCode;
    }

    public void setTxtSysAppAreaCode(RichInputText txtSysAppAreaCode) {
        this.txtSysAppAreaCode = txtSysAppAreaCode;
    }

    public RichInputText getTxtSysAppAreaCode() {
        return txtSysAppAreaCode;
    }
}
