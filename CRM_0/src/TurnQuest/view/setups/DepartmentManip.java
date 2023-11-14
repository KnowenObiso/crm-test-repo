package TurnQuest.view.setups;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class DepartmentManip {
    private RichTable departMentTbl;
    private RichInputText txtName;
    private RichInputDate txtWef;
    private RichInputDate txtWet;
    private RichTable userTbl;
    private RichInputText txtUser;
    private RichCommandButton saveDepartments;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public DepartmentManip() {
    }

    public String createNewDepartment() {
        session.setAttribute("action", "A");
        txtName.setValue(null);
        txtWef.setValue(null);
        txtWet.setValue(null);
        txtUser.setValue(null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:departmentPop" + "').show(hints);");
        return null;
    }

    public String editDepartment() {
        Object key = departMentTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a record to edit");
            return null;
        }
        session.setAttribute("action", "E");
        session.setAttribute("depCode", r.getAttribute("depCode"));
        txtName.setValue(r.getAttribute("depName"));
        txtWef.setValue(r.getAttribute("depWef"));
        txtWet.setValue(r.getAttribute("depWet"));
        txtUser.setValue(r.getAttribute("userName"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:departmentPop" + "').show(hints);");
        return null;
    }

    public String deleteDepartment() {
        Object key = departMentTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SETUPS_PKG.update_departments(?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {

            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setString(1, "D");
            stmt.setObject(2, session.getAttribute("depCode"));
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.setObject(5, null);
            stmt.setObject(6, null);
            stmt.setObject(7, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchDepartmentDtlsIterator").executeQuery();
            GlobalCC.refreshUI(departMentTbl);
            String message = "Successfull!";
            TurnQuest.view.Base.GlobalCC.INFORMATIONREPORTING(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDepartMentTbl(RichTable departMentTbl) {
        this.departMentTbl = departMentTbl;
    }

    public RichTable getDepartMentTbl() {
        return departMentTbl;
    }

    public void setTxtName(RichInputText txtName) {
        this.txtName = txtName;
    }

    public RichInputText getTxtName() {
        return txtName;
    }


    public void setTxtWef(RichInputDate txtWef) {
        this.txtWef = txtWef;
    }

    public RichInputDate getTxtWef() {
        return txtWef;
    }

    public void setTxtWet(RichInputDate txtWet) {
        this.txtWet = txtWet;
    }

    public RichInputDate getTxtWet() {
        return txtWet;
    }

    public void setUserTbl(RichTable userTbl) {
        this.userTbl = userTbl;
    }

    public RichTable getUserTbl() {
        return userTbl;
    }

    public String selectUser() {
        Object key = userTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("usercode", r.getAttribute("depUsrCode"));
        txtUser.setValue(r.getAttribute("userName"));
        GlobalCC.refreshUI(txtUser);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:UserPop" +
                             "').hide(hints);");
        return null;
    }

    public String cancelUser() {
        // Add event code here...
        return null;
    }

    public void setTxtUser(RichInputText txtUser) {
        this.txtUser = txtUser;
    }

    public RichInputText getTxtUser() {
        return txtUser;
    }

    public void setSaveDepartments(RichCommandButton saveDepartments) {
        this.saveDepartments = saveDepartments;
    }

    public RichCommandButton getSaveDepartments() {
        return saveDepartments;
    }

    public String cancelDetails() {
        // Add event code here...
        return null;
    }

    public String saveDepartmentDtls() {
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_SETUPS_PKG.update_departments(?,?,?,?,?,?,?);end;";
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        try {

            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setString(1, (String)session.getAttribute("action"));
            if (session.getAttribute("action").equals("A")) {
                stmt.setObject(2, null);
            } else {
                stmt.setObject(2, session.getAttribute("depCode"));
            }
            stmt.setObject(3, null);
            stmt.setObject(4, txtName.getValue());
            stmt.setObject(5, GlobalCC.extractDate(txtWef));
            stmt.setObject(6, GlobalCC.extractDate(txtWet));
            stmt.setObject(7, session.getAttribute("usercode"));
            stmt.execute();
            stmt.close();
            conn.commit();
            conn.close();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:departmentPop" + "').hide(hints);");
            ADFUtils.findIterator("fetchDepartmentDtlsIterator").executeQuery();
            GlobalCC.refreshUI(departMentTbl);
            String message = "Successfull!";
            TurnQuest.view.Base.GlobalCC.INFORMATIONREPORTING(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
