package TurnQuest.view.setups;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import java.sql.SQLException;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class EcmBacking {
    private RichInputText txtSystemName;
    private RichInputText txtEcmUrl;
    private RichInputText txtSystemCode;
    private RichTable tblSystems;
    private RichInputText txtEcmUsername;
    private RichInputText txtEcmPassword;
    private RichInputText txtEcmCode;
    private RichInputText txtEcmRootFoldeer;
    private RichTable tblEcm;
    private RichTree systemTree;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTable processTable;
    private RichPanelTabbed mainTabPanel;
    private RichTable systemReports;
    private RichInputText txtRptName;
    private RichInputText txtRptDesc;
    private RichInputText txtDesc;
    private RichSelectOneChoice txtType;
    private RichTable processEcmTable;
    private RichTable docTypesTable;
    private RichInputText txtContentType;
    private RichInputText txtDescription;
    private RichInputText txtContentCode;
    private RichInputText txtMetaCode;
    private RichInputText txtMetadataName;
    private RichInputText txtMetaDescription;
    private RichTable metadataTable;
    private RichInputText txtContTypeSearch;
    private RichTable contTypeTbl;
    private RichTable metadataTbl;
    private RichTable ecmTable;
    private RichInputText txtEcmType;
    private RichInputText txtContType;

    public EcmBacking() {
        super();
    }

    public void selectSystem(DialogEvent dialogEvent) {
        Object key = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtSystemCode.setValue(nodeBinding.getAttribute("code"));
            txtSystemName.setValue(nodeBinding.getAttribute("name"));
        }
        GlobalCC.refreshUI(txtSystemName);
        GlobalCC.refreshUI(txtSystemCode);
        GlobalCC.dismissPopUp("crm", "SystemsLOVPop");
    }

    public void setTxtSystemName(RichInputText txtSystemName) {
        this.txtSystemName = txtSystemName;
    }

    public RichInputText getTxtSystemName() {
        return txtSystemName;
    }

    public void setTxtEcmUrl(RichInputText txtEcmUrl) {
        this.txtEcmUrl = txtEcmUrl;
    }

    public RichInputText getTxtEcmUrl() {
        return txtEcmUrl;
    }

    public void setTxtSystemCode(RichInputText txtSystemCode) {
        this.txtSystemCode = txtSystemCode;
    }

    public RichInputText getTxtSystemCode() {
        return txtSystemCode;
    }

    public String saveEcmDetails() {
        if (txtSystemCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a System to Proceed");
            return null;
        }
        if (txtEcmUrl.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter ECM Url");
            return null;
        }
        if (txtEcmUsername.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter ECM Username");
            return null;
        }
        if (txtEcmPassword.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter ECM Username");
            return null;
        }
        if (txtEcmRootFoldeer.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("Enter ECM Root Folder");
            return null;
        }
        Object ecmcode = txtEcmCode.getValue();
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query =
            "begin TQC_SETUPS_PKG.ecm_setups_prc(?,?,?,?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();


        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (ecmcode == null)
                stmt.setObject(7, "A");
            else
                stmt.setObject(7, "E");
            stmt.setObject(1, ecmcode);
            stmt.setObject(2, txtEcmUrl.getValue());
            stmt.setObject(3, txtEcmUsername.getValue());
            stmt.setObject(4, txtEcmPassword.getValue());
            stmt.setObject(5, txtSystemCode.getValue());
            stmt.setObject(6, txtEcmRootFoldeer.getValue());
            stmt.execute();
            stmt.close();
            conn.commit();
            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("findEcmSetupsIterator").executeQuery();
            GlobalCC.refreshUI(tblEcm);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        GlobalCC.dismissPopUp("crm", "ecmsetupPOP");
        return null;
    }

    public void setTblSystems(RichTable tblSystems) {
        this.tblSystems = tblSystems;
    }

    public RichTable getTblSystems() {
        return tblSystems;
    }

    public void setTxtEcmUsername(RichInputText txtEcmUsername) {
        this.txtEcmUsername = txtEcmUsername;
    }

    public RichInputText getTxtEcmUsername() {
        return txtEcmUsername;
    }

    public void setTxtEcmPassword(RichInputText txtEcmPassword) {
        this.txtEcmPassword = txtEcmPassword;
    }

    public RichInputText getTxtEcmPassword() {
        return txtEcmPassword;
    }

    public String addEcms() {
        txtSystemCode.setValue(null);
        txtSystemName.setValue(null);
        txtEcmPassword.setValue(null);
        txtEcmUsername.setValue(null);
        txtEcmUrl.setValue(null);
        txtEcmCode.setValue(null);
        txtEcmRootFoldeer.setValue(null);
        GlobalCC.showPopUp("crm", "ecmsetupPOP");
        return null;
    }

    public void setTxtEcmCode(RichInputText txtEcmCode) {
        this.txtEcmCode = txtEcmCode;
    }

    public RichInputText getTxtEcmCode() {
        return txtEcmCode;
    }

    public void setTxtEcmRootFoldeer(RichInputText txtEcmRootFoldeer) {
        this.txtEcmRootFoldeer = txtEcmRootFoldeer;
    }

    public RichInputText getTxtEcmRootFoldeer() {
        return txtEcmRootFoldeer;
    }

    public void setTblEcm(RichTable tblEcm) {
        this.tblEcm = tblEcm;
    }

    public RichTable getTblEcm() {
        return tblEcm;
    }

    public String editEcm() {
        Object key = tblEcm.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a System appicable area to Edit");
            return null;
        }
        txtSystemCode.setValue(nodeBinding.getAttribute("estsyscode"));
        txtSystemName.setValue(nodeBinding.getAttribute("sysname"));
        txtEcmPassword.setValue(nodeBinding.getAttribute("estecmpassword"));
        txtEcmUsername.setValue(nodeBinding.getAttribute("estecmusername"));
        txtEcmUrl.setValue(nodeBinding.getAttribute("estecmurl"));
        txtEcmCode.setValue(nodeBinding.getAttribute("estcode"));
        txtEcmRootFoldeer.setValue(nodeBinding.getAttribute("estrootfolder"));
        GlobalCC.showPopUp("crm", "ecmsetupPOP");
        return null;
    }

    public String deleteEcmSetups() {
        Object key = tblEcm.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a System appicable area to Delete");
            return null;
        }
        txtEcmCode.setValue(nodeBinding.getAttribute("estcode"));
        GlobalCC.refreshUI(txtEcmCode);
        Object ecmcode = txtEcmCode.getValue();
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query =
            "begin TQC_SETUPS_PKG.ecm_setups_prc(?,?,?,?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();


        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setObject(7, "D");
            stmt.setObject(1, ecmcode);
            stmt.setObject(2, null);
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.setObject(5, null);
            stmt.setObject(6, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("findEcmSetupsIterator").executeQuery();
            GlobalCC.refreshUI(tblEcm);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void selectSystem(SelectionEvent evt) {
        try {
            if (evt.getAddedSet() != evt.getRemovedSet()) {
                RowKeySet keys = evt.getAddedSet();
                if (keys != null && keys.getSize() > 0) {
                    for (Object treeRowKey : keys) {
                        systemTree.setRowKey(treeRowKey);
                        JUCtrlHierNodeBinding nodeBinding =
                            (JUCtrlHierNodeBinding)systemTree.getRowData();

                        session.setAttribute("EcmsysCode",
                                             nodeBinding.getRow().getAttribute("syscode"));

                        // Fetch the Processes
                        ADFUtils.findIterator("findEcmProcessesIterator").executeQuery();
                        GlobalCC.refreshUI(processTable);

                        processTable.setVisible(true);
                        GlobalCC.refreshUI(processTable);
                    }
                }
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
            e.printStackTrace();
        }
    }

    public void setSystemTree(RichTree systemTree) {
        this.systemTree = systemTree;
    }

    public RichTree getSystemTree() {
        return systemTree;
    }

    public void setProcessTable(RichTable processTable) {
        this.processTable = processTable;
    }

    public RichTable getProcessTable() {
        return processTable;
    }

    public void processSelected(SelectionEvent selectionEvent) {
        Object key2 = processTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return;
        }
        session.setAttribute("EcmPrcCode", r.getAttribute("processCode"));
        session.removeAttribute("EcmSprrCode");
        session.removeAttribute("EcmSdtCode");
        mainTabPanel.setVisible(true);
        GlobalCC.refreshUI(mainTabPanel);
        ADFUtils.findIterator("findEcmProcessReportsIterator").executeQuery();
        ADFUtils.findIterator("findEcmsystemReportsIterator").executeQuery();
        ADFUtils.findIterator("findEcmProcessDocTypesIterator").executeQuery();
        GlobalCC.refreshUI(docTypesTable);
        ADFUtils.findIterator("findEcmProcessContentTypesIterator").executeQuery();
        GlobalCC.refreshUI(metadataTable);
        ADFUtils.findIterator("findEcmProcessContentTypesIterator").executeQuery();
        GlobalCC.refreshUI(metadataTable);
    }

    public void setMainTabPanel(RichPanelTabbed mainTabPanel) {
        this.mainTabPanel = mainTabPanel;
    }

    public RichPanelTabbed getMainTabPanel() {
        return mainTabPanel;
    }

    public void reportsSelected(DialogEvent dialogEvent) {
        Object key2 = systemReports.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return;
        }
        session.setAttribute("ecmRptCode", r.getAttribute("rptcode"));
        txtRptName.setValue(r.getAttribute("rptname"));
        txtRptDesc.setValue(r.getAttribute("rptdesc"));
        GlobalCC.refreshUI(txtRptName);
        GlobalCC.refreshUI(txtRptDesc);

    }

    public void setSystemReports(RichTable systemReports) {
        this.systemReports = systemReports;
    }

    public RichTable getSystemReports() {
        return systemReports;
    }

    public void setTxtRptName(RichInputText txtRptName) {
        this.txtRptName = txtRptName;
    }

    public RichInputText getTxtRptName() {
        return txtRptName;
    }

    public void setTxtRptDesc(RichInputText txtRptDesc) {
        this.txtRptDesc = txtRptDesc;
    }

    public RichInputText getTxtRptDesc() {
        return txtRptDesc;
    }

    public void setTxtDesc(RichInputText txtDesc) {
        this.txtDesc = txtDesc;
    }

    public RichInputText getTxtDesc() {
        return txtDesc;
    }

    public void setTxtType(RichSelectOneChoice txtType) {
        this.txtType = txtType;
    }

    public RichSelectOneChoice getTxtType() {
        return txtType;
    }

    public String addEcmReport() {
        session.setAttribute("ecmRptCode", null);
        txtRptName.setValue(null);
        txtRptDesc.setValue(null);
        txtDesc.setValue(null);
        txtType.setValue(null);
        txtEcmCode.setValue(null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:ecmpop" +
                             "').show(hints);");
        return null;
    }

    public String saveEcmReports() {
        Object ecmcode = txtEcmCode.getValue();
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query =
            "begin TQC_SETUPS_PKG.attach_ecm_reports(?,?,?,?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();


        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            if (ecmcode == null)
                stmt.setObject(1, "A");
            else
                stmt.setObject(1, "E");
            stmt.setObject(2, ecmcode);
            stmt.setBigDecimal(3,
                               (BigDecimal)session.getAttribute("ecmRptCode"));
            stmt.setBigDecimal(4,
                               (BigDecimal)session.getAttribute("EcmPrcCode"));
            stmt.setObject(5, txtDesc.getValue());
            stmt.setObject(6, txtType.getValue());
            stmt.setBigDecimal(7,
                               (BigDecimal)session.getAttribute("contCode"));
            stmt.execute();
            stmt.close();
            conn.commit();
            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("findEcmProcessReportsIterator").executeQuery();
            GlobalCC.refreshUI(processEcmTable);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        GlobalCC.dismissPopUp("crm", "ecmpop");

        return null;
    }


    public void setProcessEcmTable(RichTable processEcmTable) {
        this.processEcmTable = processEcmTable;
    }

    public RichTable getProcessEcmTable() {
        return processEcmTable;
    }

    public String editEcmReport() {
        Object key = processEcmTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row to Edit");
            return null;
        }
        txtEcmCode.setValue(nodeBinding.getAttribute("sprrCode"));
        session.setAttribute("ecmRptCode",
                             nodeBinding.getAttribute("rptcode"));
        txtRptName.setValue(nodeBinding.getAttribute("rptname"));
        txtRptDesc.setValue(nodeBinding.getAttribute("rptdesc"));
        txtDesc.setValue(nodeBinding.getAttribute("sprrDesc"));
        txtType.setValue(nodeBinding.getAttribute("type"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:ecmpop" +
                             "').show(hints);");
        return null;
    }

    public String deleteEcmReports() {
        Object key = processEcmTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row to Delete");
            return null;
        }
        txtEcmCode.setValue(nodeBinding.getAttribute("sprrCode"));
        GlobalCC.refreshUI(txtEcmCode);
        Object ecmcode = txtEcmCode.getValue();
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query =
            "begin TQC_SETUPS_PKG.attach_ecm_reports(?,?,?,?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();


        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setObject(1, "D");
            stmt.setObject(2, ecmcode);
            stmt.setBigDecimal(3,
                               (BigDecimal)session.getAttribute("ecmRptCode"));
            stmt.setBigDecimal(4,
                               (BigDecimal)session.getAttribute("EcmPrcCode"));
            stmt.setObject(5, txtDesc.getValue());
            stmt.setObject(6, txtType.getValue());
            stmt.setObject(7, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("findEcmProcessReportsIterator").executeQuery();
            GlobalCC.refreshUI(processEcmTable);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        GlobalCC.dismissPopUp("crm", "ecmpop");
        return null;
    }

    public void setDocTypesTable(RichTable docTypesTable) {
        this.docTypesTable = docTypesTable;
    }

    public RichTable getDocTypesTable() {
        return docTypesTable;
    }

    public void selectProcessReports(SelectionEvent selectionEvent) {
        Object key = processEcmTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row to Delete");
            return;
        }
        session.setAttribute("EcmSprrCode", r.getAttribute("sprrCode"));
        ADFUtils.findIterator("findEcmProcessDocTypesIterator").executeQuery();
        GlobalCC.refreshUI(docTypesTable);


    }

    public String addContentTypes() {
        txtContentType.setValue(null);
        txtDescription.setValue(null);
        txtContentCode.setValue(null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:contentPop" + "').show(hints);");
        return null;
    }

    public void setTxtContentType(RichInputText txtContentType) {
        this.txtContentType = txtContentType;
    }

    public RichInputText getTxtContentType() {
        return txtContentType;
    }

    public void setTxtDescription(RichInputText txtDescription) {
        this.txtDescription = txtDescription;
    }

    public RichInputText getTxtDescription() {
        return txtDescription;
    }

    public void setTxtContentCode(RichInputText txtContentCode) {
        this.txtContentCode = txtContentCode;
    }

    public RichInputText getTxtContentCode() {
        return txtContentCode;
    }

    public String saveContentTypes() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query = "begin TQC_SETUPS_PKG.ecm_doc_types(?,?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();
        Object ecmcode = txtContentCode.getValue();

        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            if (ecmcode == null)
                stmt.setObject(5, "A");
            else
                stmt.setObject(5, "E");
            stmt.setObject(1, ecmcode);
            stmt.setBigDecimal(2,
                               (BigDecimal)session.getAttribute("EcmSprrCode"));
            stmt.setObject(3, txtContentType.getValue());
            stmt.setObject(4, txtDescription.getValue());
            stmt.execute();
            stmt.close();
            conn.commit();
            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("findEcmProcessDocTypesIterator").executeQuery();
            GlobalCC.refreshUI(docTypesTable);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String editContentTypes() {
        Object key = docTypesTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row to Edit");
            return null;
        }
        txtContentType.setValue(r.getAttribute("content_name"));
        txtDescription.setValue(r.getAttribute("content_desc"));
        txtContentCode.setValue(r.getAttribute("sdt_code"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:contentPop" + "').show(hints);");
        return null;
    }

    public String deleteContentTypes() {
        Object key = docTypesTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row to Delete");
            return null;
        }
        txtContentCode.setValue(r.getAttribute("sdt_code"));
        GlobalCC.refreshUI(txtContentCode);
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query = "begin TQC_SETUPS_PKG.ecm_doc_types(?,?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();
        Object ecmcode = txtContentCode.getValue();

        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);


            stmt.setObject(5, "D");
            stmt.setObject(1, ecmcode);
            stmt.setBigDecimal(2,
                               (BigDecimal)session.getAttribute("EcmSprrCode"));
            stmt.setObject(3, txtContentType.getValue());
            stmt.setObject(4, txtDescription.getValue());
            stmt.execute();
            stmt.close();
            conn.commit();
            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("findEcmProcessDocTypesIterator").executeQuery();
            GlobalCC.refreshUI(docTypesTable);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String addMetadata() {
        txtMetadataName.setValue(null);
        txtMetaDescription.setValue(null);
        txtMetaCode.setValue(null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:metaPop" +
                             "').show(hints);");
        return null;
    }

    public String editMetadata() {
        Object key = metadataTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row to Edit");
            return null;
        }
        txtMetadataName.setValue(r.getAttribute("content_name"));
        txtMetaDescription.setValue(r.getAttribute("content_desc"));
        txtMetaCode.setValue(r.getAttribute("sdt_code"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "crm:metaPop" +
                             "').show(hints);");
        return null;
    }

    public String deleteMetadata() {
        Object key = metadataTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row to Edit");
            return null;
        }
        txtMetaCode.setValue(r.getAttribute("sdt_code"));
        GlobalCC.refreshUI(txtMetaCode);
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query = "begin TQC_SETUPS_PKG.ecm_metadata(?,?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();
        BigDecimal ecmcode = (BigDecimal)r.getAttribute("sdt_code");
        System.out.println("This is Ok" + r.getAttribute("sdt_code"));

        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);


            stmt.setObject(5, "D");
            stmt.setBigDecimal(1, ecmcode);
            stmt.setBigDecimal(2, null);
            stmt.setObject(3, null);
            stmt.setObject(4, null);
            stmt.execute();
            stmt.close();
            conn.commit();
            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("findEcmProcessDocTypesIterator").executeQuery();
            GlobalCC.refreshUI(docTypesTable);
            ADFUtils.findIterator("findEcmProcessContentTypesIterator").executeQuery();
            GlobalCC.refreshUI(metadataTable);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTxtMetaCode(RichInputText txtMetaCode) {
        this.txtMetaCode = txtMetaCode;
    }

    public RichInputText getTxtMetaCode() {
        return txtMetaCode;
    }

    public void setTxtMetadataName(RichInputText txtMetadataName) {
        this.txtMetadataName = txtMetadataName;
    }

    public RichInputText getTxtMetadataName() {
        return txtMetadataName;
    }

    public void setTxtMetaDescription(RichInputText txtMetaDescription) {
        this.txtMetaDescription = txtMetaDescription;
    }

    public RichInputText getTxtMetaDescription() {
        return txtMetaDescription;
    }

    public String saveMetadata() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String query = "begin TQC_SETUPS_PKG.ecm_metadata(?,?,?,?,?);end;";
        conn = dbConnector.getDatabaseConnection();
        Object ecmcode = txtMetaCode.getValue();

        try {
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            if (ecmcode == null)
                stmt.setObject(5, "A");
            else
                stmt.setObject(5, "E");
            stmt.setObject(1, ecmcode);
            stmt.setBigDecimal(2,
                               (BigDecimal)session.getAttribute("EcmSdtCode"));
            stmt.setObject(3, txtMetadataName.getValue());
            stmt.setObject(4, txtMetaDescription.getValue());
            stmt.execute();
            stmt.close();
            conn.commit();
            GlobalCC.INFORMATIONREPORTING("Operation Successfully Executed");
            ADFUtils.findIterator("findEcmProcessContentTypesIterator").executeQuery();
            GlobalCC.refreshUI(metadataTable);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void selectContentTypes(SelectionEvent selectionEvent) {
        Object key = docTypesTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row to Delete");
            return;
        }
        session.setAttribute("EcmSdtCode", r.getAttribute("sdt_code"));
        ADFUtils.findIterator("findEcmProcessContentTypesIterator").executeQuery();
        GlobalCC.refreshUI(metadataTable);
    }

    public void setMetadataTable(RichTable metadataTable) {
        this.metadataTable = metadataTable;
    }

    public RichTable getMetadataTable() {
        return metadataTable;
    }

    public void setTxtContTypeSearch(RichInputText txtContTypeSearch) {
        this.txtContTypeSearch = txtContTypeSearch;
    }

    public RichInputText getTxtContTypeSearch() {
        return txtContTypeSearch;
    }

    public void contTypeSelected(DialogEvent dialogEvent) {
        Object key = contTypeTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row ");
            return;
        }
        txtContTypeSearch.setValue(r.getAttribute("content_name"));
        GlobalCC.refreshUI(txtContTypeSearch);
        session.setAttribute("EcmSdtCode", r.getAttribute("sdt_code"));
        ADFUtils.findIterator("findEcmProcessContentTypesIterator").executeQuery();
        GlobalCC.refreshUI(metadataTbl);
    }

    public void setContTypeTbl(RichTable contTypeTbl) {
        this.contTypeTbl = contTypeTbl;
    }

    public RichTable getContTypeTbl() {
        return contTypeTbl;
    }

    public void setMetadataTbl(RichTable metadataTbl) {
        this.metadataTbl = metadataTbl;
    }

    public RichTable getMetadataTbl() {
        return metadataTbl;
    }

    public void docTypeSelected(DialogEvent dialogEvent) {
        Object key = ecmTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a Row ");
            return;
        }
        txtEcmType.setValue(r.getAttribute("content_name"));
        GlobalCC.refreshUI(txtEcmType);
        session.setAttribute("contCode", r.getAttribute("sdt_code"));
    }

    public void setEcmTable(RichTable ecmTable) {
        this.ecmTable = ecmTable;
    }

    public RichTable getEcmTable() {
        return ecmTable;
    }

    public void setTxtEcmType(RichInputText txtEcmType) {
        this.txtEcmType = txtEcmType;
    }

    public RichInputText getTxtEcmType() {
        return txtEcmType;
    }

    public void setTxtContType(RichInputText txtContType) {
        this.txtContType = txtContType;
    }

    public RichInputText getTxtContType() {
        return txtContType;
    }
}
