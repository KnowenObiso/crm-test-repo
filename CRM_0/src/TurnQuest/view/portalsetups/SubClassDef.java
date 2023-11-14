package TurnQuest.view.portalsetups;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class SubClassDef {
    private RichInputText sclDescription;
    private RichTable sclTable;

    public SubClassDef() {
    }

    public String newDescription() {
        sclDescription.setValue(null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:description').show(hints);");
        return null;
    }

    public String editDescription() {
        Object key2 = sclTable.getSelectedRowData();
        JUCtrlValueBinding rows = (JUCtrlValueBinding)key2;
        if (rows == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a subClass");
        }
        sclDescription.setValue(rows.getAttribute("sclWebSclDescription"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:description').show(hints);");
        return null;
    }

    public String updateDescription() {
        String Description;
        Object key2 = sclTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a subclass");
            return null;
        }
        if (sclDescription.getValue() != null) {
            Description = sclDescription.getValue().toString();
        } else {
            Description = null;
        }
        String Query = "begin Gis_Web_Pkg.deleteScldescDetails(?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {
            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setBigDecimal(1,
                              (BigDecimal)nodeBinding.getAttribute("sclCode"));
            cst.setString(2, Description);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findSubClassDescIterator").executeQuery();
            GlobalCC.refreshUI(sclTable);
            String message = "Record Updated Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String saveDescription() {
        // Add event code here...
        return null;
    }

    public void setSclDescription(RichInputText sclDescription) {
        this.sclDescription = sclDescription;
    }

    public RichInputText getSclDescription() {
        return sclDescription;
    }

    public void setSclTable(RichTable sclTable) {
        this.sclTable = sclTable;
    }

    public RichTable getSclTable() {
        return sclTable;
    }
}
