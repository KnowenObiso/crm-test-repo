/*
* Copyright (c) 2010 TurnKey Africa Ltd. All Rights Reserved.
*
* This software is the confidential and proprietary information of TurnKey
* Africa Ltd. ("Confidential Information"). You shall not disclose such
* Confidential Information and shall use it only in accordance with the terms
* of the license agreement you entered into with TurnKey Africa Ltd.
*
* TURNKEY AFRICA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
* OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
* TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
* PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TURNKEY AFRICA SHALL NOT BE LIABLE
* FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
* DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*/

package TurnQuest.view.backing;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.OrgSubDivision;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class OrgSubDivisionBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichCommandButton btnSaveUpdateOrgSubDivision;
    private RichPanelBox panelOrgSubDivDetail;
    private RichInputText txtOsdCode;
    private RichInputText txtOsdParentOsdCode;
    private RichInputText txtOsdDltCode;
    private RichInputText txtOsdDltDesc;
    private RichInputText txtOsdOdlCode;
    private RichInputText txtOsdOdlDesc;
    private RichInputText txtOsdName;
    private RichInputDate txtOsdWef;
    private RichInputText txtOsdDivHeadAgnCode;
    private RichInputText txtOsdDivHeadAgnName;
    private RichInputText txtOsdSysCode;
    private RichTable tblorgSubDivLevelTypePop;
    private RichTable tblorgSubDivLevelPop;
    private RichTable tblHeadAgentsPop;
    private RichTree treeSysSubDivisions;

    public OrgSubDivisionBacking() {
    }

    public void setBtnSaveUpdateOrgSubDivision(RichCommandButton btnSaveUpdateOrgSubDivision) {
        this.btnSaveUpdateOrgSubDivision = btnSaveUpdateOrgSubDivision;
    }

    public RichCommandButton getBtnSaveUpdateOrgSubDivision() {
        return btnSaveUpdateOrgSubDivision;
    }

    public void setPanelOrgSubDivDetail(RichPanelBox panelOrgSubDivDetail) {
        this.panelOrgSubDivDetail = panelOrgSubDivDetail;
    }

    public RichPanelBox getPanelOrgSubDivDetail() {
        return panelOrgSubDivDetail;
    }

    public void setTxtOsdCode(RichInputText txtOsdCode) {
        this.txtOsdCode = txtOsdCode;
    }

    public RichInputText getTxtOsdCode() {
        return txtOsdCode;
    }

    public void setTxtOsdParentOsdCode(RichInputText txtOsdParentOsdCode) {
        this.txtOsdParentOsdCode = txtOsdParentOsdCode;
    }

    public RichInputText getTxtOsdParentOsdCode() {
        return txtOsdParentOsdCode;
    }

    public void setTxtOsdDltCode(RichInputText txtOsdDltCode) {
        this.txtOsdDltCode = txtOsdDltCode;
    }

    public RichInputText getTxtOsdDltCode() {
        return txtOsdDltCode;
    }

    public void setTxtOsdDltDesc(RichInputText txtOsdDltDesc) {
        this.txtOsdDltDesc = txtOsdDltDesc;
    }

    public RichInputText getTxtOsdDltDesc() {
        return txtOsdDltDesc;
    }

    public void setTxtOsdOdlCode(RichInputText txtOsdOdlCode) {
        this.txtOsdOdlCode = txtOsdOdlCode;
    }

    public RichInputText getTxtOsdOdlCode() {
        return txtOsdOdlCode;
    }

    public void setTxtOsdOdlDesc(RichInputText txtOsdOdlDesc) {
        this.txtOsdOdlDesc = txtOsdOdlDesc;
    }

    public RichInputText getTxtOsdOdlDesc() {
        return txtOsdOdlDesc;
    }

    public void setTxtOsdName(RichInputText txtOsdName) {
        this.txtOsdName = txtOsdName;
    }

    public RichInputText getTxtOsdName() {
        return txtOsdName;
    }

    public void setTxtOsdWef(RichInputDate txtOsdWef) {
        this.txtOsdWef = txtOsdWef;
    }

    public RichInputDate getTxtOsdWef() {
        return txtOsdWef;
    }

    public void setTxtOsdDivHeadAgnCode(RichInputText txtOsdDivHeadAgnCode) {
        this.txtOsdDivHeadAgnCode = txtOsdDivHeadAgnCode;
    }

    public RichInputText getTxtOsdDivHeadAgnCode() {
        return txtOsdDivHeadAgnCode;
    }

    public void setTxtOsdDivHeadAgnName(RichInputText txtOsdDivHeadAgnName) {
        this.txtOsdDivHeadAgnName = txtOsdDivHeadAgnName;
    }

    public RichInputText getTxtOsdDivHeadAgnName() {
        return txtOsdDivHeadAgnName;
    }

    public void setTxtOsdSysCode(RichInputText txtOsdSysCode) {
        this.txtOsdSysCode = txtOsdSysCode;
    }

    public RichInputText getTxtOsdSysCode() {
        return txtOsdSysCode;
    }

    public void setTblorgSubDivLevelTypePop(RichTable tblorgSubDivLevelTypePop) {
        this.tblorgSubDivLevelTypePop = tblorgSubDivLevelTypePop;
    }

    public RichTable getTblorgSubDivLevelTypePop() {
        return tblorgSubDivLevelTypePop;
    }

    public void setTblorgSubDivLevelPop(RichTable tblorgSubDivLevelPop) {
        this.tblorgSubDivLevelPop = tblorgSubDivLevelPop;
    }

    public RichTable getTblorgSubDivLevelPop() {
        return tblorgSubDivLevelPop;
    }

    public void setTblHeadAgentsPop(RichTable tblHeadAgentsPop) {
        this.tblHeadAgentsPop = tblHeadAgentsPop;
    }

    public RichTable getTblHeadAgentsPop() {
        return tblHeadAgentsPop;
    }

    public void setTreeSysSubDivisions(RichTree treeSysSubDivisions) {
        this.treeSysSubDivisions = treeSysSubDivisions;
    }

    public RichTree getTreeSysSubDivisions() {
        return treeSysSubDivisions;
    }

    public void treeSysSubDivisionsListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeSysSubDivisions.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)treeSysSubDivisions.getRowData();

                    String nodeType =
                        nodeBinding.getRow().getAttribute("nodeType").toString();

                    if (nodeType.equals("P")) { // Primary : System has been selected
                        session.setAttribute("sysCode",
                                             nodeBinding.getRow().getAttribute("code"));
                        session.setAttribute("osdCode", null);

                        panelOrgSubDivDetail.setVisible(false);

                    } else { // Secondary : OrgSubDivision has been selected.
                        session.setAttribute("sysCode",
                                             nodeBinding.getRow().getAttribute("osdSysCode"));
                        session.setAttribute("osdCode",
                                             nodeBinding.getRow().getAttribute("osdCode"));

                        txtOsdCode.setValue(nodeBinding.getRow().getAttribute("osdCode"));
                        txtOsdParentOsdCode.setValue(nodeBinding.getRow().getAttribute("osdParentOsdCode"));
                        txtOsdDltCode.setValue(nodeBinding.getRow().getAttribute("osdDltCode"));
                        txtOsdDltDesc.setValue(nodeBinding.getRow().getAttribute("dltDesc"));
                        txtOsdOdlCode.setValue(nodeBinding.getRow().getAttribute("osdOdlCode"));
                        txtOsdOdlDesc.setValue(nodeBinding.getRow().getAttribute("odlDesc"));
                        txtOsdName.setValue(nodeBinding.getRow().getAttribute("osdName"));
                        txtOsdWef.setValue(nodeBinding.getRow().getAttribute("osdWef"));
                        txtOsdDivHeadAgnCode.setValue(nodeBinding.getRow().getAttribute("osdDivHeadAgnCode"));
                        txtOsdDivHeadAgnName.setValue(nodeBinding.getRow().getAttribute("agentName"));
                        txtOsdSysCode.setValue(nodeBinding.getRow().getAttribute("osdSysCode"));

                        txtOsdCode.setDisabled(true);
                        btnSaveUpdateOrgSubDivision.setText("Update");

                        panelOrgSubDivDetail.setVisible(true);

                        //ADFUtils.findIterator("fetchOrgDivisionLevelTypesIterator").executeQuery();
                        //GlobalCC.refreshUI(tblorgSubDivLevelTypePop);

                    }

                    GlobalCC.refreshUI(panelOrgSubDivDetail);
                } // End for
            } // End if
        } // End if
    }

    public void clearSubDivisionFields() {
        txtOsdCode.setValue(null);
        txtOsdParentOsdCode.setValue(session.getAttribute("osdCode"));
        txtOsdDltCode.setValue(null);
        txtOsdDltDesc.setValue(null);
        txtOsdOdlCode.setValue(null);
        txtOsdOdlDesc.setValue(null);
        txtOsdName.setValue(null);
        txtOsdWef.setValue(null);
        txtOsdDivHeadAgnCode.setValue(null);
        txtOsdDivHeadAgnName.setValue(null);
        txtOsdSysCode.setValue(session.getAttribute("sysCode"));

        txtOsdCode.setDisabled(false);
        btnSaveUpdateOrgSubDivision.setText("Save");

        ADFUtils.findIterator("fetchOrgDivisionLevelTypesIterator").executeQuery();
        GlobalCC.refreshUI(tblorgSubDivLevelTypePop);

        panelOrgSubDivDetail.setVisible(true);
    }

    public String actionNewOrgSubDiv() {
        if (session.getAttribute("sysCode") != null) {
            clearSubDivisionFields();
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing System to proceed.");
            return null;
        }
        return null;
    }

    public String actionDeleteOrgSubDiv() {
        if (session.getAttribute("osdCode") != null) {
            String code = GlobalCC.checkNullValues(txtOsdCode.getValue());
            String parentCode =
                GlobalCC.checkNullValues(txtOsdParentOsdCode.getValue());
            String dltCode =
                GlobalCC.checkNullValues(txtOsdDltCode.getValue());
            String odlCode =
                GlobalCC.checkNullValues(txtOsdOdlCode.getValue());
            String name = GlobalCC.checkNullValues(txtOsdName.getValue());
            String wef = GlobalCC.checkNullValues(txtOsdWef.getValue());
            String agnCode =
                GlobalCC.checkNullValues(txtOsdDivHeadAgnCode.getValue());
            String sysCode =
                GlobalCC.checkNullValues(txtOsdSysCode.getValue());

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.orgSubDivisions_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ORG_SUBDIVISIONS_TAB",
                                                     conn);
                ArrayList subDivisionList = new ArrayList();
                OrgSubDivision subDivision = new OrgSubDivision();
                subDivision.setSQLTypeName("TQC_ORG_SUBDIVISIONS_OBJ");

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpWefDate = new Date();
                if (txtOsdWef.getValue() != null &&
                    !(txtOsdWef.getValue().equals(""))) {
                    String date1 = df.format(txtOsdWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                subDivision.setOsdCode(code);
                subDivision.setOsdParentOsdCode(parentCode);
                subDivision.setOsdDltCode(dltCode);
                subDivision.setOsdOdlCode(odlCode);
                subDivision.setOsdName(name);
                subDivision.setOsdWef(tmpWefDate == null ? null :
                                      new java.sql.Date(tmpWefDate.getTime()));
                subDivision.setOsdDivHeadAgnCode(agnCode == null ? null :
                                                 new BigDecimal(agnCode));
                subDivision.setOsdSysCode(sysCode == null ? null :
                                          new BigDecimal(sysCode));

                subDivisionList.add(subDivision);
                ARRAY array =
                    new ARRAY(descriptor, conn, subDivisionList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchSystemsOrgSubDivisionsIterator").executeQuery();
                GlobalCC.refreshUI(treeSysSubDivisions);

                session.setAttribute("osdCode", null);

                clearSubDivisionFields();

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing SubDivision record to proceed.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateOrgSubDivision() {
        if (btnSaveUpdateOrgSubDivision.getText().equals("Update")) {
            actionUpdateOrgSubDivision();
        } else {
            String code = GlobalCC.checkNullValues(txtOsdCode.getValue());
            String parentCode =
                GlobalCC.checkNullValues(txtOsdParentOsdCode.getValue());
            String dltCode =
                GlobalCC.checkNullValues(txtOsdDltCode.getValue());
            String odlCode =
                GlobalCC.checkNullValues(txtOsdOdlCode.getValue());
            String name = GlobalCC.checkNullValues(txtOsdName.getValue());
            String wef = GlobalCC.checkNullValues(txtOsdWef.getValue());
            String agnCode =
                GlobalCC.checkNullValues(txtOsdDivHeadAgnCode.getValue());
            String sysCode =
                GlobalCC.checkNullValues(txtOsdSysCode.getValue());

            if (code == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
                return null;

            } else if (dltCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Division Level Type is Empty");
                return null;

            } else if (odlCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Division Level is Empty");
                return null;

            } else if (name == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
                return null;

            } else if (wef == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
                return null;

            } else if (sysCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.orgSubDivisions_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ORG_SUBDIVISIONS_TAB",
                                                         conn);
                    ArrayList subDivisionList = new ArrayList();
                    OrgSubDivision subDivision = new OrgSubDivision();
                    subDivision.setSQLTypeName("TQC_ORG_SUBDIVISIONS_OBJ");

                    // Take care of all the date fields on the form.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date tmpWefDate = new Date();
                    if (txtOsdWef.getValue() != null &&
                        !(txtOsdWef.getValue().equals(""))) {
                        String date1 = df.format(txtOsdWef.getValue());
                        tmpWefDate = df.parse(date1);
                    }

                    subDivision.setOsdCode(code);
                    subDivision.setOsdParentOsdCode(parentCode);
                    subDivision.setOsdDltCode(dltCode);
                    subDivision.setOsdOdlCode(odlCode);
                    subDivision.setOsdName(name);
                    subDivision.setOsdWef(tmpWefDate == null ? null :
                                          new java.sql.Date(tmpWefDate.getTime()));
                    subDivision.setOsdDivHeadAgnCode(agnCode == null ? null :
                                                     new BigDecimal(agnCode));
                    subDivision.setOsdSysCode(sysCode == null ? null :
                                              new BigDecimal(sysCode));

                    subDivisionList.add(subDivision);
                    ARRAY array =
                        new ARRAY(descriptor, conn, subDivisionList.toArray());

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "A");
                    statement.setArray(2, array);
                    statement.execute();

                    statement.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchSystemsOrgSubDivisionsIterator").executeQuery();
                    GlobalCC.refreshUI(treeSysSubDivisions);

                    clearSubDivisionFields();

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateOrgSubDivision() {
        String code = GlobalCC.checkNullValues(txtOsdCode.getValue());
        String parentCode =
            GlobalCC.checkNullValues(txtOsdParentOsdCode.getValue());
        String dltCode = GlobalCC.checkNullValues(txtOsdDltCode.getValue());
        String odlCode = GlobalCC.checkNullValues(txtOsdOdlCode.getValue());
        String name = GlobalCC.checkNullValues(txtOsdName.getValue());
        String wef = GlobalCC.checkNullValues(txtOsdWef.getValue());
        String agnCode =
            GlobalCC.checkNullValues(txtOsdDivHeadAgnCode.getValue());
        String sysCode = GlobalCC.checkNullValues(txtOsdSysCode.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (dltCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Division Level Type is Empty");
            return null;

        } else if (odlCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Division Level is Empty");
            return null;

        } else if (name == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
            return null;

        } else if (wef == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
            return null;

        } else if (sysCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.orgSubDivisions_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ORG_SUBDIVISIONS_TAB",
                                                     conn);
                ArrayList subDivisionList = new ArrayList();
                OrgSubDivision subDivision = new OrgSubDivision();
                subDivision.setSQLTypeName("TQC_ORG_SUBDIVISIONS_OBJ");

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpWefDate = new Date();
                if (txtOsdWef.getValue() != null &&
                    !(txtOsdWef.getValue().equals(""))) {
                    String date1 = df.format(txtOsdWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                subDivision.setOsdCode(code);
                subDivision.setOsdParentOsdCode(parentCode);
                subDivision.setOsdDltCode(dltCode);
                subDivision.setOsdOdlCode(odlCode);
                subDivision.setOsdName(name);
                subDivision.setOsdWef(tmpWefDate == null ? null :
                                      new java.sql.Date(tmpWefDate.getTime()));
                subDivision.setOsdDivHeadAgnCode(agnCode == null ? null :
                                                 new BigDecimal(agnCode));
                subDivision.setOsdSysCode(sysCode == null ? null :
                                          new BigDecimal(sysCode));

                subDivisionList.add(subDivision);
                ARRAY array =
                    new ARRAY(descriptor, conn, subDivisionList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchSystemsOrgSubDivisionsIterator").executeQuery();
                GlobalCC.refreshUI(treeSysSubDivisions);

                clearSubDivisionFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionShowOrgDivLevelTypes() {
        if (session.getAttribute("sysCode") != null) {

            ADFUtils.findIterator("fetchOrgDivisionLevelTypesIterator").executeQuery();
            GlobalCC.refreshUI(tblorgSubDivLevelTypePop);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:orgSubDivLevelTypePop" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a System first");
            return null;
        }
        return null;
    }

    public String actionAcceptOrgSubDivLevelType() {
        Object key2 = tblorgSubDivLevelTypePop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtOsdDltCode.setValue(nodeBinding.getAttribute("dltCode"));
            txtOsdDltDesc.setValue(nodeBinding.getAttribute("dltDesc"));

            session.setAttribute("dltCode",
                                 nodeBinding.getAttribute("dltCode"));
        }
        return null;
    }

    public String actionShowOrgDivLevel() {
        if (session.getAttribute("dltCode") != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:orgSubDivLevel" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Division Level Type first");
            return null;
        }
        return null;
    }

    public String actionAcceptOrgSubDivLevel() {
        Object key2 = tblorgSubDivLevelPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtOsdOdlCode.setValue(nodeBinding.getAttribute("odlcode"));
            txtOsdOdlDesc.setValue(nodeBinding.getAttribute("odlDesc"));
        }
        return null;
    }

    public String actionShowHeadAgents() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:agentsListPop" + "').show(hints);");
        return null;
    }

    public String actionAcceptHeadAgent() {
        Object key2 = tblHeadAgentsPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtOsdDivHeadAgnCode.setValue(nodeBinding.getAttribute("code"));
            txtOsdDivHeadAgnName.setValue(nodeBinding.getAttribute("name"));
        }
        return null;
    }
}
