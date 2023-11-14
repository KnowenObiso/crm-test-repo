package TurnQuest.view.Proposals;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class DynaProposal {

    private RichInputText txtSysModule;
    private RichInputText txtSubClass;
    private RichInputText txtSchedId;
    private RichInputText txtSchedDesc;
    private RichInputNumberSpinbox txtSchedOrder;
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    private RichTree scheduleTree;
    private RichTable moduleLov;
    private RichTable subclassLov;
    private RichTable schedDetails;
    private RichInputText txtName;
    private RichInputText txtPrompt;
    private RichInputText txtOrder;
    private RichInputNumberSpinbox txtParent;
    private RichSelectOneChoice txtMoreDetApp;
    private RichInputText txtMoreDetails;
    private RichSelectOneChoice txtRoot;
    private RichSelectOneChoice txtMoreDetReq;
    private RichTable modUnitsTable;
    private RichTable modDetailsTable;
    private RichTable modDetailsOptionTable;
    private RichInputText txtOptionName;
    private RichInputText txtOptionDesc;
    private RichInputNumberSpinbox txtOptionOrder;
    private RichSelectOneChoice txtOptionType;
    private RichSelectOneChoice txtType;
    private RichTable products;
    private RichInputText mappingDesc;
    private RichTable mapTable;
    private RichPopup previewButtonPop;


    public void setTxtSysModule(RichInputText txtSysModule) {
        this.txtSysModule = txtSysModule;
    }

    public RichInputText getTxtSysModule() {
        return txtSysModule;
    }

    public void setTxtSubClass(RichInputText txtSubClass) {
        this.txtSubClass = txtSubClass;
    }

    public RichInputText getTxtSubClass() {
        return txtSubClass;
    }

    public void setTxtSchedId(RichInputText txtSchedId) {
        this.txtSchedId = txtSchedId;
    }

    public RichInputText getTxtSchedId() {
        return txtSchedId;
    }

    public void setTxtSchedDesc(RichInputText txtSchedDesc) {
        this.txtSchedDesc = txtSchedDesc;
    }

    public RichInputText getTxtSchedDesc() {
        return txtSchedDesc;
    }

    public void setTxtSchedOrder(RichInputNumberSpinbox txtSchedOrder) {
        this.txtSchedOrder = txtSchedOrder;
    }

    public RichInputNumberSpinbox getTxtSchedOrder() {
        return txtSchedOrder;
    }

    public String editScheduleModule() {
        Object key2 = modUnitsTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("Select Module");
            return null;
        }
        BigDecimal TsmCode = (BigDecimal)r.getAttribute("TSMS_CODE");
        session.setAttribute("pkKey", TsmCode);
        session.setAttribute("action", "E");
        txtSchedId.setValue(r.getAttribute("TSMS_SHT_DESC"));
        txtSchedDesc.setValue(r.getAttribute("TSMS_DESC"));
        txtSchedOrder.setValue(r.getAttribute("TSMS_ORDER"));
        //txtSubClass.setValue(r.getAttribute("SCL_DESC"));
        session.setAttribute("SysTsmCode", r.getAttribute("TSMS_TSM_CODE"));
        txtSysModule.setValue(r.getAttribute("TSM_DESC"));

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "demoTemplate:p3" + "').show(hints);");
        return null;
    }


    public String createScheduleModule() {
        if (session.getAttribute("prodCode") == null) {
            GlobalCC.errorValueNotEntered("Select Product");
            return null;
        }
        txtSysModule.setValue(null);
        //txtSubClass.setValue(null);
        txtSchedId.setValue(null);
        txtSchedDesc.setValue(null);
        txtSchedOrder.setValue(null);
        session.removeAttribute("pkKey");
        session.setAttribute("action", "A");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "demoTemplate:p3" + "').show(hints);");
        return null;
    }

    public String deleteScheduleModule() {
        try {
            Object key2 = modUnitsTable.getSelectedRowData();
            JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
            if (r == null) {
                GlobalCC.errorValueNotEntered("Select Module");
                return null;
            }
            BigDecimal TsmCode = (BigDecimal)r.getAttribute("TSMS_CODE");
            session.setAttribute("pkKey", TsmCode);
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement cst = null;
            String Query =
                "begin tqc_setups_pkg.tqc_sys_mod_subunits_prc(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            cst.setString(1, "D");
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("pkKey"));
            cst.setString(3, null);
            cst.setString(4, null);
            cst.setString(5, null);
            cst.setString(6, null);
            cst.setString(7, null);
            cst.execute();
            ADFUtils.findIterator("findPropModulesSubUnitsIterator").executeQuery();
            GlobalCC.refreshUI(modUnitsTable);
            createScheduleModule();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public void setScheduleTree(RichTree scheduleTree) {
        this.scheduleTree = scheduleTree;
    }

    public RichTree getScheduleTree() {
        return scheduleTree;
    }

    public String saveScheduleModule() {
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement cst = null;
            String Query =
                "begin tqc_setups_pkg.tqc_sys_mod_subunits_prc(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("pkKey"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("SysTsmCode"));
            cst.setString(4, (String)txtSchedId.getValue());
            cst.setString(5, (String)txtSchedDesc.getValue());
            cst.setBigDecimal(6,
                              GlobalCC.extractBigDecimal(txtSchedOrder.getValue()));
            cst.setBigDecimal(7, (BigDecimal)session.getAttribute("prodCode"));
            cst.execute();
            ADFUtils.findIterator("findPropModulesSubUnitsIterator").executeQuery();
            GlobalCC.refreshUI(modUnitsTable);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public void selectSchedules(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    scheduleTree.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)scheduleTree.getRowData();
                    BigDecimal SclCode =
                        new BigDecimal(nd.getRow().getAttribute("subcode").toString());
                    session.setAttribute("prodCode", SclCode);
                    ADFUtils.findIterator("findPropModulesSubUnitsIterator").executeQuery();
                    GlobalCC.refreshUI(modUnitsTable);
                    session.setAttribute("SysTsmCode", null);
                    ADFUtils.findIterator("findPropModulesSubUnitsDetIterator").executeQuery();
                    GlobalCC.refreshUI(modDetailsTable);
                    session.setAttribute("TsmsdCode", null);
                    ADFUtils.findIterator("findPropModulesSubUnitsDetOptionsIterator").executeQuery();
                    GlobalCC.refreshUI(modDetailsOptionTable);

                }
            }
        }
    }


    public void selectSchedListener(SelectionEvent selectionEvent) {
        Object key2 = modUnitsTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("Select Module");
            return;
        }
        BigDecimal TsmCode = (BigDecimal)r.getAttribute("TSMS_CODE");
        session.setAttribute("SysTsmCode", TsmCode);
        ADFUtils.findIterator("findPropModulesSubUnitsDetIterator").executeQuery();
        GlobalCC.refreshUI(modDetailsTable);

        session.setAttribute("TsmsdCode", null);
        ADFUtils.findIterator("findPropModulesSubUnitsDetOptionsIterator").executeQuery();
        GlobalCC.refreshUI(modDetailsOptionTable);

    }


    public void selectSchedOptionsListener(SelectionEvent selectionEvent) {
        Object key2 = modDetailsTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("Select Module Detail");
            return;
        }
        BigDecimal TsmCode = (BigDecimal)r.getAttribute("TSMSD_CODE");
        session.setAttribute("TsmsdCode", TsmCode);
        ADFUtils.findIterator("findPropModulesSubUnitsDetOptionsIterator").executeQuery();
        GlobalCC.refreshUI(modDetailsOptionTable);

    }

    public String addOptions() {
        if (session.getAttribute("TsmsdCode") == null) {
            GlobalCC.errorValueNotEntered("Select Detail to Continue..");
            return null;
        }
        txtOptionDesc.setValue(null);
        txtOptionName.setValue(null);
        txtOptionOrder.setValue(null);
        txtOptionType.setValue(null);
        session.removeAttribute("Pkey");
        session.setAttribute("action", "A");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "demoTemplate:p4" + "').show(hints);");
        return null;
    }

    public String editOptions() {
        Object key2 = modDetailsOptionTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("Select Options");
            return null;
        }
        txtOptionDesc.setValue(r.getAttribute("TSSO_OPTION_DESC"));
        txtOptionName.setValue(r.getAttribute("TSSO_OPTION_NAME"));
        txtOptionOrder.setValue(r.getAttribute("TSSO_ORDER"));
        txtOptionType.setValue(r.getAttribute("TSSO_TYPE"));
        session.setAttribute("Pkey", r.getAttribute("TSSO_CODE"));
        session.setAttribute("action", "E");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "demoTemplate:p4" + "').show(hints);");
        GlobalCC.refreshUI(txtOptionType);
        return null;
    }


    public String deleteOptions() {
        Object key2 = modDetailsOptionTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("Select Options");
            return null;
        }
        session.setAttribute("Pkey", r.getAttribute("TSSO_CODE"));
        session.setAttribute("action", "D");
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement cst = null;
            String Query =
                "begin tqc_setups_pkg.sys_subunits_options_prc(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            cst.setObject(1, session.getAttribute("action"));
            cst.setObject(2, session.getAttribute("Pkey"));
            cst.setObject(3, session.getAttribute("TsmsdCode"));
            cst.setString(4, null);
            cst.setString(5, null);
            cst.setBigDecimal(6,
                              null); //GlobalCC.extractBigDecimal(txtOptionOrder));
            cst.setString(7, null);
            cst.execute();
            ADFUtils.findIterator("findPropModulesSubUnitsDetOptionsIterator").executeQuery();
            GlobalCC.refreshUI(modDetailsOptionTable);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }


    public String saveOptions() {
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement cst = null;
            String Query =
                "begin tqc_setups_pkg.sys_subunits_options_prc(?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("Pkey"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("TsmsdCode"));
            cst.setString(4, (String)txtOptionName.getValue());
            cst.setString(5, (String)txtOptionDesc.getValue());
            cst.setBigDecimal(6,
                              GlobalCC.extractBigDecimal(txtOptionOrder.getValue()));
            cst.setString(7, (String)txtOptionType.getValue());
            cst.execute();
            ADFUtils.findIterator("findPropModulesSubUnitsDetOptionsIterator").executeQuery();
            GlobalCC.refreshUI(modDetailsOptionTable);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }


    public String selectModule(DialogEvent event) {
        Object key2 = moduleLov.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("Select Module");
            return null;
        }
        txtSysModule.setValue(r.getAttribute("TSMS_DESC"));
        session.setAttribute("SysTsmCode", r.getAttribute("TSMS_CODE"));
        GlobalCC.refreshUI(txtSysModule);
        return null;
    }


    public String selectSubclas(DialogEvent event) {
        Object key2 = subclassLov.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("Select Sub Class");
            return null;
        }
        txtSubClass.setValue(r.getAttribute("subdesc"));
        session.setAttribute("SclCode",
                             new BigDecimal(r.getAttribute("subcode").toString()));
        GlobalCC.refreshUI(txtSubClass);
        return null;
    }

    public void setModuleLov(RichTable moduleLov) {
        this.moduleLov = moduleLov;
    }

    public RichTable getModuleLov() {
        return moduleLov;
    }

    public void setSubclassLov(RichTable subclassLov) {
        this.subclassLov = subclassLov;
    }

    public RichTable getSubclassLov() {
        return subclassLov;
    }

    public void setSchedDetails(RichTable schedDetails) {
        this.schedDetails = schedDetails;
    }

    public RichTable getSchedDetails() {
        return schedDetails;
    }

    public void setTxtName(RichInputText txtName) {
        this.txtName = txtName;
    }

    public RichInputText getTxtName() {
        return txtName;
    }

    public void setTxtPrompt(RichInputText txtPrompt) {
        this.txtPrompt = txtPrompt;
    }

    public RichInputText getTxtPrompt() {
        return txtPrompt;
    }


    public void setTxtOrder(RichInputText txtOrder) {
        this.txtOrder = txtOrder;
    }

    public RichInputText getTxtOrder() {
        return txtOrder;
    }

    public void setTxtParent(RichInputNumberSpinbox txtParent) {
        this.txtParent = txtParent;
    }

    public RichInputNumberSpinbox getTxtParent() {
        return txtParent;
    }

    public void setTxtMoreDetApp(RichSelectOneChoice txtMoreDetApp) {
        this.txtMoreDetApp = txtMoreDetApp;
    }

    public RichSelectOneChoice getTxtMoreDetApp() {
        return txtMoreDetApp;
    }

    public void setTxtMoreDetails(RichInputText txtMoreDetails) {
        this.txtMoreDetails = txtMoreDetails;
    }

    public RichInputText getTxtMoreDetails() {
        return txtMoreDetails;
    }

    public void setTxtRoot(RichSelectOneChoice txtRoot) {
        this.txtRoot = txtRoot;
    }

    public RichSelectOneChoice getTxtRoot() {
        return txtRoot;
    }

    public void setTxtMoreDetReq(RichSelectOneChoice txtMoreDetReq) {
        this.txtMoreDetReq = txtMoreDetReq;
    }

    public RichSelectOneChoice getTxtMoreDetReq() {
        return txtMoreDetReq;
    }

    public String addScheduleDetails() {
        if (session.getAttribute("SysTsmCode") == null) {
            GlobalCC.errorValueNotEntered("Select Sub Unit");
            return null;
        }
        session.setAttribute("action", "A");
        txtName.setValue(null);
        txtPrompt.setValue(null);
        txtType.setValue(null);
        txtOrder.setValue(null);
        txtParent.setValue(null);
        txtMoreDetApp.setValue(null);
        txtMoreDetails.setValue(null);
        txtRoot.setValue(null);
        txtMoreDetReq.setValue(null);
        mappingDesc.setValue(null);
        mappingDesc.setLabel(null);
        session.removeAttribute("Pkey");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "demoTemplate:popup1" + "').show(hints);");
        return null;
    }

    public String deleteScheduleDetails() {
        Object key2 = modDetailsTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("Select Schedule Details");
            return null;
        }
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement cst = null;
            String Query =
                "begin tqc_setups_pkg.mod_subunit_det_prc(?,?,?,?,?,?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            cst.setString(1, "D");
            cst.setObject(2, r.getAttribute("TSMSD_CODE"));
            cst.setObject(3, null);
            cst.setString(4, null);
            cst.setString(5, null);
            cst.setString(6, null);
            cst.setString(7, null);
            cst.setBigDecimal(8,
                              null); //GlobalCC.extractBigDecimal(txtParent));
            cst.setString(9, null);
            cst.setString(10, null);
            cst.setString(11, null);
            cst.setString(12, null);
            cst.execute();
            ADFUtils.findIterator("findPropModulesSubUnitsDetIterator").executeQuery();
            GlobalCC.refreshUI(modDetailsTable);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }


    public String editScheduleDetails() {
        Object key2 = modDetailsTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("Select Schedule Details");
            return null;
        }
        session.setAttribute("action", "E");
        txtName.setValue(r.getAttribute("TSMSD_NAME"));
        txtPrompt.setValue(r.getAttribute("TSMSD_PROMPT"));
        txtType.setValue(r.getAttribute("TSMSD_TYPE"));
        txtOrder.setValue(r.getAttribute("TSMSD_ORDER"));
        txtParent.setValue(r.getAttribute("TSMSD_PARENT"));
        txtMoreDetApp.setValue(r.getAttribute("TSMSD_MORE_DTLS_APPL"));
        txtMoreDetails.setValue(r.getAttribute("TSMSD_MORE_DTLS"));
        txtRoot.setValue(r.getAttribute("TSMSD_ROOT"));
        txtMoreDetReq.setValue(r.getAttribute("TSMSD_MORE_DTLS_REQUIRED"));
        mappingDesc.setValue(r.getAttribute("tmsc_desc"));
        if (r.getAttribute("tmsc_code") != null) {
            mappingDesc.setLabel(r.getAttribute("tmsc_code").toString());
        } else {
            mappingDesc.setLabel(null);
        }
        session.setAttribute("Pkey", r.getAttribute("TSMSD_CODE"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "demoTemplate:popup1" + "').show(hints);");
        return null;
    }

    public String saveScheduleDetails() {
        try {
            DBConnector datahandler = new DBConnector();
            OracleConnection conn;
            conn = datahandler.getDatabaseConnection();
            OracleCallableStatement cst = null;
            String Query =
                "begin tqc_setups_pkg.mod_subunit_det_prc(?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("Pkey"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("SysTsmCode"));
            cst.setString(4, GlobalCC.checkNullValues(txtName.getValue()));
            cst.setString(5, GlobalCC.checkNullValues(txtPrompt.getValue()));
            cst.setString(6, GlobalCC.checkNullValues(txtType.getValue()));
            cst.setString(7, GlobalCC.checkNullValues(txtOrder.getValue()));
            if (GlobalCC.checkNullValues(txtParent.getValue()) != null) {
                cst.setBigDecimal(8,
                                  GlobalCC.extractBigDecimal(txtParent.getValue()));
            } else {
                cst.setObject(8, null);
            }
            cst.setString(9,
                          GlobalCC.checkNullValues(txtMoreDetApp.getValue()));
            cst.setString(10,
                          GlobalCC.checkNullValues(txtMoreDetails.getValue()));
            cst.setString(11, GlobalCC.checkNullValues(txtRoot.getValue()));
            cst.setString(12,
                          GlobalCC.checkNullValues(txtMoreDetReq.getValue()));
            if (GlobalCC.checkNullValues(mappingDesc.getValue()) != null) {
                cst.setString(13, mappingDesc.getLabel());
            } else {
                cst.setString(13, null);
            }
            cst.execute();
            ADFUtils.findIterator("findPropModulesSubUnitsDetIterator").executeQuery();
            GlobalCC.refreshUI(modDetailsTable);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "demoTemplate:popup1" + "').hide(hints);");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public void setModUnitsTable(RichTable modUnitsTable) {
        this.modUnitsTable = modUnitsTable;
    }

    public RichTable getModUnitsTable() {
        return modUnitsTable;
    }

    public void setModDetailsTable(RichTable modDetailsTable) {
        this.modDetailsTable = modDetailsTable;
    }

    public RichTable getModDetailsTable() {
        return modDetailsTable;
    }

    public void setModDetailsOptionTable(RichTable modDetailsOptionTable) {
        this.modDetailsOptionTable = modDetailsOptionTable;
    }

    public RichTable getModDetailsOptionTable() {
        return modDetailsOptionTable;
    }

    public void setTxtOptionName(RichInputText txtOptionName) {
        this.txtOptionName = txtOptionName;
    }

    public RichInputText getTxtOptionName() {
        return txtOptionName;
    }

    public void setTxtOptionDesc(RichInputText txtOptionDesc) {
        this.txtOptionDesc = txtOptionDesc;
    }

    public RichInputText getTxtOptionDesc() {
        return txtOptionDesc;
    }

    public void setTxtOptionOrder(RichInputNumberSpinbox txtOptionOrder) {
        this.txtOptionOrder = txtOptionOrder;
    }

    public RichInputNumberSpinbox getTxtOptionOrder() {
        return txtOptionOrder;
    }

    public void setTxtOptionType(RichSelectOneChoice txtOptionType) {
        this.txtOptionType = txtOptionType;
    }

    public RichSelectOneChoice getTxtOptionType() {
        return txtOptionType;
    }

    public void setTxtType(RichSelectOneChoice txtType) {
        this.txtType = txtType;
    }

    public RichSelectOneChoice getTxtType() {
        return txtType;
    }

    public void productSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getRemovedSet() != selectionEvent.getAddedSet()) {
            RowKeySet rowKeySet = selectionEvent.getAddedSet();

            Object key2 = rowKeySet.iterator().next();
            products.setRowKey(key2);
            JUCtrlValueBinding r = (JUCtrlValueBinding)products.getRowData();

            System.out.println("productCode" + r.getAttribute("productCode"));
            session.setAttribute("prodCode", r.getAttribute("productCode"));
            ADFUtils.findIterator("findPropModulesSubUnitsIterator").executeQuery();
            GlobalCC.refreshUI(modUnitsTable);
            session.setAttribute("SysTsmCode", null);
            ADFUtils.findIterator("findPropModulesSubUnitsDetIterator").executeQuery();
            GlobalCC.refreshUI(modDetailsTable);
            session.setAttribute("TsmsdCode", null);
            ADFUtils.findIterator("findPropModulesSubUnitsDetOptionsIterator").executeQuery();
            GlobalCC.refreshUI(modDetailsOptionTable);
        }

    }

    public void setProducts(RichTable products) {
        this.products = products;
    }

    public RichTable getProducts() {
        return products;
    }

    public void setMappingDesc(RichInputText mappingDesc) {
        this.mappingDesc = mappingDesc;
    }

    public RichInputText getMappingDesc() {
        return mappingDesc;
    }

    public String launchMapping() {
        GlobalCC.showPopup("demoTemplate:mapping");
        return null;
    }

    public void setMapTable(RichTable mapTable) {
        this.mapTable = mapTable;
    }

    public RichTable getMapTable() {
        return mapTable;
    }

    public String MappingSelected() {
        Object obj = mapTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)obj;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Mapping Selected");
            return null;
        }
        mappingDesc.setValue(r.getAttribute("tmsc_desc"));
        mappingDesc.setLabel(r.getAttribute("tmsc_code").toString());
        GlobalCC.hidePopup("demoTemplate:mapping");
        GlobalCC.refreshUI(mappingDesc);
        return null;
    }

    public String cancelScheduleDetails() {
        txtName.setValue(null);
        txtPrompt.setValue(null);
        txtType.setValue(null);
        txtOrder.setValue(null);
        txtParent.setValue(null);
        txtMoreDetApp.setValue(null);
        txtMoreDetails.setValue(null);
        txtRoot.setValue(null);
        txtMoreDetReq.setValue(null);
        mappingDesc.setValue(null);
        mappingDesc.setLabel(null);
        session.removeAttribute("Pkey");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "demoTemplate:popup1" + "').hide(hints);");
        return null;
    }

    public String previewPop() {
        // Add event code here...
        return null;
    }

    public void setPreviewButtonPop(RichPopup previewButtonPop) {
        this.previewButtonPop = previewButtonPop;
    }

    public RichPopup getPreviewButtonPop() {
        return previewButtonPop;
    }
}
