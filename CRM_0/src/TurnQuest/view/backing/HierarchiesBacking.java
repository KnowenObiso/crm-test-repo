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
import TurnQuest.view.models.OrgDivisionLevel;
import TurnQuest.view.models.OrgDivisionLevelType;
import TurnQuest.view.models.OrgSubDivPreviousHead;
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
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.component.UIXTree;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.ModelUtils;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetTreeImpl;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class HierarchiesBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblSystems;
    private RichTable tblDivLevelTypes;
    private RichTable tblDivLevels;
    private RichInputText txtDltCode;
    private RichInputText txtDltSysCode;
    private RichInputText txtDltDesc;
    private RichCommandButton btnSaveUpdateDivLevelType;
    private RichInputText txtOdlCode;
    private RichInputText txtOdlDltCode;
    private RichInputText txtOdlDesc;
    private RichInputNumberSpinbox txtOdlRanking;
    private RichCommandButton btnSaveUpdateDivLevel;
    private RichTree treeDltSubDivisions;
    private RichCommandButton btnSaveUpdateOrgSubDivision;
    private RichInputText txtOsdCode;
    private RichInputText txtOsdParentOsdCode;
    private RichInputText txtOsdDltCode;
    private RichInputText txtOsdDltDesc;
    private RichInputText txtOsdOdlCode;
    private RichInputText txtOsdOdlDesc;
    private RichInputText txtOsdName;
    private RichInputDate txtOsdWef;
    private RichInputDate txtOsdWet;
    private RichSelectOneChoice txtOsdStatus;
    private RichInputText txtOsdDivHeadAgnCode;
    private RichInputText txtOsdDivHeadAgnName;
    private RichInputText txtOsdSysCode;
    private RichPanelBox panelOrgSubDivDetail;
    private RichInputText txtDltActCode;
    private RichInputText txtDltActName;
    private RichTable tblAccountTypesPop;
    private RichPanelBox panelDivLevelTypePop;
    private RichTable tblorgSubDivLevelPop;
    private RichTable tblHeadAgentsPop;
    private RichTable tblPrevHeads;
    private RichInputText txtOsdphCode;
    private RichInputText txtOsdphOsdCode;
    private RichInputText txtOsdphPrevAgnCode;
    private RichInputText txtAgencyName;
    private RichInputDate txtOsdphWet;
    private RichCommandButton btnSaveUpdatePrevHead;
    private RichInputText txtOsdphOsdName;
    private RichTable tblHeadAgentsPop2;
    private RichPanelBox panelPrevHeads;
    private RichInputText txtParentDivision;
    private RichSelectOneChoice txtOdlType;
    private RichInputText txtType;
    private RichCommandButton btnType;
    private RichTable regTab;
    private RichTable branchTab;
    private RichSelectOneChoice txtPostLevel;
    private RichSelectOneChoice txtManagerAllowed;
    private RichSelectOneChoice overrideCommAll;
    private RichInputNumberSpinbox txtOsdId;
    private RichInputNumberSpinbox txtOsdParentId;
    private RichInputNumberSpinbox txtOsdphOsdId;
    private boolean initDisclosure = false;
    private RowKeySet disclosedRowKeys = new RowKeySetTreeImpl();
    private RichSelectOneChoice selDltType;
    private RichInputText txtHeadDltActCode;
    private RichInputText txtHeadDltActName;
    private RichTable tblHeadAccountTypesPop;
    private RichInputText txtAgencyIntermediaries;
    private RichInputText txtIntermediaryCode;
    private RichTable tblAgencyIntermediaryPop;
    private RichSelectOneChoice selPayIntermediary;
    private RichInputText txtOsdLocation;
    private RichTable tblDivisionLocationPop;
    private RichInputText txtOsdLocationCode;
    private RichPanelBox panelOrgSubDivHeadHistory;
    private RichTable osDHeadHistTbl;
    private RichCommandButton btnSaveEditOrgSubDivHead;
    private RichPanelBox editDivHeadHistPopUp;
    private RichCommandButton btnEditOrgSubDivHead;
    private RichInputText txtODHAgnShtDsc;
    private RichInputText txtODHAgnNAME;
    private RichInputDate txtODHAgnWEF;
    private RichInputDate txtODHAgnWET;

    public HierarchiesBacking() {
    }


    public void setTblSystems(RichTable tblSystems) {
        this.tblSystems = tblSystems;
    }

    public RichTable getTblSystems() {
        return tblSystems;
    }

    public void setTblDivLevelTypes(RichTable tblDivLevelTypes) {
        this.tblDivLevelTypes = tblDivLevelTypes;
    }

    public RichTable getTblDivLevelTypes() {
        return tblDivLevelTypes;
    }

    public void setTblDivLevels(RichTable tblDivLevels) {
        this.tblDivLevels = tblDivLevels;
    }

    public RichTable getTblDivLevels() {
        return tblDivLevels;
    }

    public void setTxtDltCode(RichInputText txtDltCode) {
        this.txtDltCode = txtDltCode;
    }

    public RichInputText getTxtDltCode() {
        return txtDltCode;
    }

    public void setTxtDltSysCode(RichInputText txtDltSysCode) {
        this.txtDltSysCode = txtDltSysCode;
    }

    public RichInputText getTxtDltSysCode() {
        return txtDltSysCode;
    }

    public void setTxtDltDesc(RichInputText txtDltDesc) {
        this.txtDltDesc = txtDltDesc;
    }

    public RichInputText getTxtDltDesc() {
        return txtDltDesc;
    }

    public void setBtnSaveUpdateDivLevelType(RichCommandButton btnSaveUpdateDivLevelType) {
        this.btnSaveUpdateDivLevelType = btnSaveUpdateDivLevelType;
    }

    public RichCommandButton getBtnSaveUpdateDivLevelType() {
        return btnSaveUpdateDivLevelType;
    }

    public void setTxtOdlCode(RichInputText txtOdlCode) {
        this.txtOdlCode = txtOdlCode;
    }

    public RichInputText getTxtOdlCode() {
        return txtOdlCode;
    }

    public void setTxtOdlDltCode(RichInputText txtOdlDltCode) {
        this.txtOdlDltCode = txtOdlDltCode;
    }

    public RichInputText getTxtOdlDltCode() {
        return txtOdlDltCode;
    }

    public void setTxtOdlDesc(RichInputText txtOdlDesc) {
        this.txtOdlDesc = txtOdlDesc;
    }

    public RichInputText getTxtOdlDesc() {
        return txtOdlDesc;
    }

    public void setTxtOdlRanking(RichInputNumberSpinbox txtOdlRanking) {
        this.txtOdlRanking = txtOdlRanking;
    }

    public RichInputNumberSpinbox getTxtOdlRanking() {
        return txtOdlRanking;
    }

    public void setBtnSaveUpdateDivLevel(RichCommandButton btnSaveUpdateDivLevel) {
        this.btnSaveUpdateDivLevel = btnSaveUpdateDivLevel;
    }

    public RichCommandButton getBtnSaveUpdateDivLevel() {
        return btnSaveUpdateDivLevel;
    }

    public void setTreeDltSubDivisions(RichTree treeDltSubDivisions) {
        this.treeDltSubDivisions = treeDltSubDivisions;
    }

    public RichTree getTreeDltSubDivisions() {
        return treeDltSubDivisions;
    }

    public void setBtnSaveUpdateOrgSubDivision(RichCommandButton btnSaveUpdateOrgSubDivision) {
        this.btnSaveUpdateOrgSubDivision = btnSaveUpdateOrgSubDivision;
    }

    public RichCommandButton getBtnSaveUpdateOrgSubDivision() {
        return btnSaveUpdateOrgSubDivision;
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

    public void setPanelOrgSubDivDetail(RichPanelBox panelOrgSubDivDetail) {
        this.panelOrgSubDivDetail = panelOrgSubDivDetail;
    }

    public RichPanelBox getPanelOrgSubDivDetail() {
        return panelOrgSubDivDetail;
    }

    public void setTxtDltActCode(RichInputText txtDltActCode) {
        this.txtDltActCode = txtDltActCode;
    }

    public RichInputText getTxtDltActCode() {
        return txtDltActCode;
    }

    public void setTxtDltActName(RichInputText txtDltActName) {
        this.txtDltActName = txtDltActName;
    }

    public RichInputText getTxtDltActName() {
        return txtDltActName;
    }

    public void setTblAccountTypesPop(RichTable tblAccountTypesPop) {
        this.tblAccountTypesPop = tblAccountTypesPop;
    }

    public RichTable getTblAccountTypesPop() {
        return tblAccountTypesPop;
    }

    public void setPanelDivLevelTypePop(RichPanelBox panelDivLevelTypePop) {
        this.panelDivLevelTypePop = panelDivLevelTypePop;
    }

    public RichPanelBox getPanelDivLevelTypePop() {
        return panelDivLevelTypePop;
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

    public void setTblPrevHeads(RichTable tblPrevHeads) {
        this.tblPrevHeads = tblPrevHeads;
    }

    public RichTable getTblPrevHeads() {
        return tblPrevHeads;
    }

    public void setTxtOsdphCode(RichInputText txtOsdphCode) {
        this.txtOsdphCode = txtOsdphCode;
    }

    public RichInputText getTxtOsdphCode() {
        return txtOsdphCode;
    }

    public void setTxtOsdphOsdCode(RichInputText txtOsdphOsdCode) {
        this.txtOsdphOsdCode = txtOsdphOsdCode;
    }

    public RichInputText getTxtOsdphOsdCode() {
        return txtOsdphOsdCode;
    }

    public void setTxtOsdphPrevAgnCode(RichInputText txtOsdphPrevAgnCode) {
        this.txtOsdphPrevAgnCode = txtOsdphPrevAgnCode;
    }

    public RichInputText getTxtOsdphPrevAgnCode() {
        return txtOsdphPrevAgnCode;
    }

    public void setTxtAgencyName(RichInputText txtAgencyName) {
        this.txtAgencyName = txtAgencyName;
    }

    public RichInputText getTxtAgencyName() {
        return txtAgencyName;
    }

    public void setTxtOsdphWet(RichInputDate txtOsdphWet) {
        this.txtOsdphWet = txtOsdphWet;
    }

    public RichInputDate getTxtOsdphWet() {
        return txtOsdphWet;
    }

    public void setBtnSaveUpdatePrevHead(RichCommandButton btnSaveUpdatePrevHead) {
        this.btnSaveUpdatePrevHead = btnSaveUpdatePrevHead;
    }

    public RichCommandButton getBtnSaveUpdatePrevHead() {
        return btnSaveUpdatePrevHead;
    }

    public void tblSystemsListener(SelectionEvent selectionEvent) {
        Object key2 = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));
            session.removeAttribute("dltCode");
            session.setAttribute("ORGCode",
                                 nodeBinding.getAttribute("orgCode"));
            ADFUtils.findIterator("fetchOrgDivisionLevelTypesIterator").executeQuery();
            GlobalCC.refreshUI(tblDivLevelTypes);

            ADFUtils.findIterator("fetchOrgDivisionLevelsIterator").executeQuery();
            GlobalCC.refreshUI(tblDivLevels);

            ADFUtils.findIterator("fetchHierarchyAccountTypesLovIterator").executeQuery();

            ADFUtils.findIterator("findAgentIntermediariesIterator").executeQuery();

            GlobalCC.refreshUI(tblAccountTypesPop);
            GlobalCC.refreshUI(tblHeadAccountTypesPop);
            GlobalCC.refreshUI(tblAgencyIntermediaryPop);
        }
    }

    public void tblDivLevelTypesListener(SelectionEvent selectionEvent) {
        Object key2 = tblDivLevelTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("dltCode",
                                 nodeBinding.getAttribute("dltCode"));
            session.setAttribute("dltDesc",
                                 nodeBinding.getAttribute("dltDesc"));
            session.setAttribute("odlRanking", null);

            session.setAttribute("actCode",
                                 nodeBinding.getAttribute("dltActCode"));

            session.setAttribute("headActCode",
                                 nodeBinding.getAttribute("dltHeadActCode"));


            session.removeAttribute("osdName1");
            ADFUtils.findIterator("fetchOrgDivisionLevelsIterator").executeQuery();
            GlobalCC.refreshUI(tblDivLevels);

            ADFUtils.findIterator("fetchOrgSubDivisionsByDltIterator").executeQuery();
            GlobalCC.refreshUI(treeDltSubDivisions);
        }
    }

    public void clearDivLevelTypeFields() {
        txtDltCode.setValue(null);
        txtDltSysCode.setValue(session.getAttribute("sysCode"));
        txtDltDesc.setValue(null);
        txtDltActCode.setValue(null);
        txtHeadDltActCode.setValue(null);
        txtDltActName.setValue(null);
        txtHeadDltActName.setValue(null);
        selDltType.setValue("N");

        txtDltCode.setDisabled(false);
        btnSaveUpdateDivLevelType.setText("Save");
    }

    public String actionNewHierarchy() {
        if (session.getAttribute("sysCode") != null) {
            clearDivLevelTypeFields();

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:divLevelTypePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing System to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditHierarchy() {
        Object key2 = tblDivLevelTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDltCode.setValue(nodeBinding.getAttribute("dltCode"));
            txtDltSysCode.setValue(nodeBinding.getAttribute("dltSysCode"));
            txtDltDesc.setValue(nodeBinding.getAttribute("dltDesc"));
            txtDltActCode.setValue(nodeBinding.getAttribute("dltActCode"));
            txtDltActName.setValue(nodeBinding.getAttribute("accountTypeName"));
            txtHeadDltActName.setValue(nodeBinding.getAttribute("headAccountTypeName"));
            txtHeadDltActCode.setValue(nodeBinding.getAttribute("dltHeadActCode"));
            selDltType.setValue(nodeBinding.getAttribute("dltType"));
            txtIntermediaryCode.setValue(nodeBinding.getAttribute("dltIntCode"));
            txtAgencyIntermediaries.setValue(nodeBinding.getAttribute("agencyIntermediary"));
            selPayIntermediary.setValue(nodeBinding.getAttribute("dltPayIntermediary"));

            txtDltCode.setDisabled(true);
            btnSaveUpdateDivLevelType.setText("Edit");


            session.setAttribute("actCode",
                                 nodeBinding.getAttribute("dltActCode"));
            ADFUtils.findIterator("findAgentIntermediariesIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyIntermediaryPop);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:divLevelTypePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String confirmDeleteAction() {
        Object key2 = tblDivLevelTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:confirmationHierarchyTypeDialog" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public void hierarchyTypeConfirmationDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteHierarchy();
        }
    }

    public String actionDeleteHierarchy() {
        Object key2 = tblDivLevelTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("dltCode").toString();
            String sysCode = nodeBinding.getAttribute("dltSysCode").toString();
            String desc = null;
            String accType = null;

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn =
(OracleConnection)(OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.orgDivLevelsType_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ORG_DIV_LEVELS_TYPE_TAB",
                                                     conn);
                ArrayList levelList = new ArrayList();
                OrgDivisionLevelType levelType = new OrgDivisionLevelType();
                levelType.setSQLTypeName("TQC_ORG_DIV_LEVELS_TYPE_OBJ");

                levelType.setDltCode(code);
                levelType.setDltSysCode(sysCode == null ? null :
                                        new BigDecimal(sysCode));
                levelType.setDltDesc(desc);
                levelType.setDltActCode(accType == null ? null :
                                        new BigDecimal(accType));

                levelList.add(levelType);
                ARRAY array = new ARRAY(descriptor, conn, levelList.toArray());

                statement =
                        (OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                session.removeAttribute("dltCode");

                ADFUtils.findIterator("fetchOrgDivisionLevelTypesIterator").executeQuery();
                GlobalCC.refreshUI(tblDivLevelTypes);

                ADFUtils.findIterator("fetchOrgDivisionLevelsIterator").executeQuery();
                GlobalCC.refreshUI(tblDivLevels);

                clearDivLevelTypeFields();

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateDivLevelType() {
        if (btnSaveUpdateDivLevelType.getText().equals("Edit")) {
            actionUpdateDivLevelType();
        } else {
            String code = GlobalCC.checkNullValues(txtDltCode.getValue());
            String sysCode =
                GlobalCC.checkNullValues(txtDltSysCode.getValue());
            String desc = GlobalCC.checkNullValues(txtDltDesc.getValue());
            String accType =
                GlobalCC.checkNullValues(txtDltActCode.getValue());
            String type = GlobalCC.checkNullValues(selDltType.getValue());
            String headAccType =
                GlobalCC.checkNullValues(txtHeadDltActCode.getValue());
            String intCode =
                GlobalCC.checkNullValues(txtIntermediaryCode.getValue());
            String payIntermediary =
                GlobalCC.checkNullValues(selPayIntermediary.getValue());


            if (sysCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
                return null;

            } else if (desc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
                return null;
            } else if (type == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type is Empty");
                return null;
            } else if (accType == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Account Type is Empty");
                return null;
            }
            // else if (headAccType == null) {
            //                if (type.equalsIgnoreCase("C")){
            //                    headAccType = accType;
            //                } else {
            //                    GlobalCC.errorValueNotEntered("Error Value Missing: Head Account Type is Empty");
            //                    return null;
            //                }
            //            } else if (type.equalsIgnoreCase("C") && !headAccType.equalsIgnoreCase(accType)) {
            //                 GlobalCC.errorValueNotEntered("Account Type and Head Account Type should be the same for Type Commission");
            //                return null;
            //            }  else if (intCode == null) {
            //                GlobalCC.errorValueNotEntered("Error Value Missing: Intermediary is Empty");
            //                return null;
            //            } else if (payIntermediary == null) {
            //                GlobalCC.errorValueNotEntered("Error Value Missing: Pay Intermediary is Empty");
            //                return null;
            //            }

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.orgDivLevelsType_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ORG_DIV_LEVELS_TYPE_TAB",
                                                     conn);
                ArrayList levelList = new ArrayList();
                OrgDivisionLevelType levelType = new OrgDivisionLevelType();

                levelType.setSQLTypeName("TQC_ORG_DIV_LEVELS_TYPE_OBJ");

                levelType.setDltCode(code);
                levelType.setDltSysCode(sysCode == null ? null :
                                        new BigDecimal(sysCode));
                levelType.setDltDesc(desc);
                levelType.setDltActCode(accType == null ? null :
                                        new BigDecimal(accType));
                levelType.setDltHeadActCode(headAccType == null ? null :
                                            new BigDecimal(headAccType));
                levelType.setDltType(type);
                levelType.setDltIntCode(intCode == null ? null :
                                        new BigDecimal(intCode));
                levelType.setDltPayIntermediary(payIntermediary == null ?
                                                null : payIntermediary);

                levelList.add(levelType);
                ARRAY array = new ARRAY(descriptor, conn, levelList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "A");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:divLevelTypePop" +
                                     "').hide(hints);");

                ADFUtils.findIterator("fetchOrgDivisionLevelTypesIterator").executeQuery();
                GlobalCC.refreshUI(tblDivLevelTypes);

                ADFUtils.findIterator("fetchOrgDivisionLevelsIterator").executeQuery();
                GlobalCC.refreshUI(tblDivLevels);

                clearDivLevelTypeFields();

                String message = "New Record ADDED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionUpdateDivLevelType() {
        String code = GlobalCC.checkNullValues(txtDltCode.getValue());
        String sysCode = GlobalCC.checkNullValues(txtDltSysCode.getValue());
        String desc = GlobalCC.checkNullValues(txtDltDesc.getValue());
        String accType = GlobalCC.checkNullValues(txtDltActCode.getValue());
        String Type = GlobalCC.checkNullValues(selDltType.getValue());
        String headAccType =
            GlobalCC.checkNullValues(txtHeadDltActCode.getValue());
        String intCode =
            GlobalCC.checkNullValues(txtIntermediaryCode.getValue());
        String payIntermediary =
            GlobalCC.checkNullValues(selPayIntermediary.getValue());

        //        if (code == null) {
        //            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
        //            return null;
        //
        //            } else
        //
        if (desc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
            return null;
        } else if (Type == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type is Empty");
            return null;
        } else if (accType == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Account Type is Empty");
            return null;
        }
        //else if (headAccType == null) {
        //                if (Type.equalsIgnoreCase("C")){
        //                    headAccType = accType;
        //                } else {
        //                    GlobalCC.errorValueNotEntered("Error Value Missing: Head Account Type is Empty");
        //                    return null;
        //                }
        //             } else if (Type.equalsIgnoreCase("C") && !headAccType.equalsIgnoreCase(accType)) {
        //                 GlobalCC.errorValueNotEntered("Account Type and Head Account Type should be the same for Type Commission");
        //                return null;
        //            }  else if (intCode == null) {
        //                GlobalCC.errorValueNotEntered("Error Value Missing: Intermediary is Empty");
        //                return null;
        //            } else if (payIntermediary == null) {
        //                GlobalCC.errorValueNotEntered("Error Value Missing: Pay Intermediary is Empty");
        //                return null;
        //            }

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_SETUPS_PKG.orgDivLevelsType_prc(?,?); end;";

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_ORG_DIV_LEVELS_TYPE_TAB",
                                                 conn);
            ArrayList levelList = new ArrayList();
            OrgDivisionLevelType levelType = new OrgDivisionLevelType();
            levelType.setSQLTypeName("TQC_ORG_DIV_LEVELS_TYPE_OBJ");

            levelType.setDltCode(code);
            levelType.setDltSysCode(sysCode == null ? null :
                                    new BigDecimal(sysCode));
            levelType.setDltDesc(desc);
            levelType.setDltActCode(accType == null ? null :
                                    new BigDecimal(accType));
            levelType.setDltHeadActCode(headAccType == null ? null :
                                        new BigDecimal(headAccType));
            levelType.setDltType(Type);
            levelType.setDltIntCode(intCode == null ? null :
                                    new BigDecimal(intCode));
            levelType.setDltPayIntermediary(payIntermediary == null ? null :
                                            payIntermediary);


            levelList.add(levelType);
            ARRAY array = new ARRAY(descriptor, conn, levelList.toArray());

            statement = (OracleCallableStatement)conn.prepareCall(query);
            statement.setString(1, "E");
            statement.setArray(2, array);
            statement.execute();

            statement.close();
            conn.commit();
            conn.close();

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:divLevelTypePop" + "').hide(hints);");

            ADFUtils.findIterator("fetchOrgDivisionLevelTypesIterator").executeQuery();
            GlobalCC.refreshUI(tblDivLevelTypes);

            ADFUtils.findIterator("fetchOrgDivisionLevelsIterator").executeQuery();
            GlobalCC.refreshUI(tblDivLevels);

            clearDivLevelTypeFields();

            String message = "Record UPDATED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void clearDivLevelFields() {
        txtOdlCode.setValue(null);
        txtOdlDltCode.setValue(session.getAttribute("dltCode"));
        txtOdlDesc.setValue(null);
        txtOdlRanking.setValue(1);
        txtOdlType.setValue("O");
        txtOdlCode.setDisabled(false);
        btnSaveUpdateDivLevel.setText("Save");
    }

    public String actionNewHierarchyLevel() {
        if (session.getAttribute("dltCode") != null) {
            clearDivLevelFields();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:divLevelPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Hierarchy Type record to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditHierarchyLevel() {
        Object key2 = tblDivLevels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtOdlCode.setValue(nodeBinding.getAttribute("odlcode"));
            txtOdlDltCode.setValue(nodeBinding.getAttribute("odlDltCode"));
            txtOdlDesc.setValue(nodeBinding.getAttribute("odlDesc"));
            txtOdlRanking.setValue(nodeBinding.getAttribute("odlRanking"));
            txtOdlType.setValue(nodeBinding.getAttribute("odlType"));
            txtOdlCode.setDisabled(true);
            btnSaveUpdateDivLevel.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:divLevelPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteHierarchyLevel() {
        Object key2 = tblDivLevels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("odlcode").toString();
            String dltCode = nodeBinding.getAttribute("odlDltCode").toString();
            String desc = null;
            String ranking = null;

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.orgDivisionLevels_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ORG_DIVISION_LEVELS_TAB",
                                                     conn);
                ArrayList levelList = new ArrayList();
                OrgDivisionLevel divlevel = new OrgDivisionLevel();
                divlevel.setSQLTypeName("TQC_ORG_DIVISION_LEVELS_OBJ");

                divlevel.setOdlcode(code);
                divlevel.setOdlDltCode(dltCode);
                divlevel.setOdlDesc(desc);
                divlevel.setOdlRanking(ranking == null ? null :
                                       new BigDecimal(ranking));

                levelList.add(divlevel);
                ARRAY array = new ARRAY(descriptor, conn, levelList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchOrgDivisionLevelsIterator").executeQuery();
                GlobalCC.refreshUI(tblDivLevels);

                clearDivLevelTypeFields();

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateDivLevel() {
        if (btnSaveUpdateDivLevel.getText().equals("Edit")) {
            actionUpdateDivLevel();
        } else {
            String code = GlobalCC.checkNullValues(txtOdlCode.getValue());
            String dltCode =
                GlobalCC.checkNullValues(txtOdlDltCode.getValue());
            String desc = GlobalCC.checkNullValues(txtOdlDesc.getValue());
            String ranking =
                GlobalCC.checkNullValues(txtOdlRanking.getValue());
            String type = GlobalCC.checkNullValues(txtOdlType.getValue());

            if (dltCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: DLT Code is Empty");
                return null;

            } else if (desc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
                return null;

            } else if (ranking == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Ranking is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                OracleConnection connection = null;
                OracleCallableStatement stmt = null;
                OracleResultSet rs = null;
                int status = 0;
                int rankStatus = 0;
                try {


                    connection =
                            (OracleConnection)dbConnector.getDatabaseConnection();
                    String query1 =
                        "begin ? := TQC_SETUPS_CURSOR.check_hierarchy_exist(?); end;";
                    stmt =
(OracleCallableStatement)connection.prepareCall(query1);
                    stmt.registerOutParameter(1,
                                              oracle.jdbc.OracleTypes.CURSOR);
                    stmt.setString(2, code);

                    stmt.execute();
                    rs = (OracleResultSet)stmt.getObject(1);
                    while (rs.next()) {
                        status = rs.getInt(1);

                    }
                    rs.close();

                    stmt.close();
                    connection.commit();
                    connection.close();

                    if (status == 1) {
                        GlobalCC.INFORMATIONREPORTING("Duplicate Hierachy Types not allowed::Enter A diferent code");
                        return null;

                    }

                    // start check if rank exist

                    connection =
                            (OracleConnection)dbConnector.getDatabaseConnection();
                    String query2 =
                        "begin ? := TQC_SETUPS_CURSOR.check_hierarchy_ranking_exist(?,?); end;";
                    stmt =
(OracleCallableStatement)connection.prepareCall(query2);
                    stmt.registerOutParameter(1,
                                              oracle.jdbc.OracleTypes.CURSOR);

                    stmt.setString(2, dltCode);
                    stmt.setInt(3, Integer.parseInt(ranking));
                    stmt.execute();
                    rs = (OracleResultSet)stmt.getObject(1);
                    while (rs.next()) {
                        rankStatus = rs.getInt(1);

                    }
                    rs.close();

                    stmt.close();
                    connection.commit();
                    connection.close();

                    if (rankStatus == 1) {
                        GlobalCC.INFORMATIONREPORTING("Duplicate Hierachy Types not allowed:: Choose Different Rank ");
                        return null;
                    }
                    //end check  of rank


                    else {


                        conn =
(OracleConnection)dbConnector.getDatabaseConnection();

                        String query =
                            "begin TQC_SETUPS_PKG.orgDivisionLevels_prc(?,?); end;";


                        ArrayDescriptor descriptor =
                            ArrayDescriptor.createDescriptor("TQC_ORG_DIVISION_LEVELS_TAB",
                                                             conn);
                        ArrayList levelList = new ArrayList();
                        OrgDivisionLevel divlevel = new OrgDivisionLevel();
                        divlevel.setSQLTypeName("TQC_ORG_DIVISION_LEVELS_OBJ");

                        divlevel.setOdlcode(code);
                        divlevel.setOdlDltCode(dltCode);
                        divlevel.setOdlDesc(desc);
                        divlevel.setOdlType(type);
                        divlevel.setOdlRanking(ranking == null ? null :
                                               new BigDecimal(ranking));

                        levelList.add(divlevel);
                        ARRAY array =
                            new ARRAY(descriptor, conn, levelList.toArray());

                        statement =
                                (OracleCallableStatement)conn.prepareCall(query);
                        statement.setString(1, "A");
                        statement.setArray(2, array);
                        statement.execute();

                        statement.close();
                        conn.commit();
                        conn.close();

                        ExtendedRenderKitService erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                        erkService.addScript(FacesContext.getCurrentInstance(),
                                             "var hints = {autodismissNever:false}; " +
                                             "AdfPage.PAGE.findComponent('" +
                                             "crm:divLevelPop" +
                                             "').hide(hints);");

                        ADFUtils.findIterator("fetchOrgDivisionLevelsIterator").executeQuery();
                        GlobalCC.refreshUI(tblDivLevels);

                        clearDivLevelFields();

                        String message = "New Record ADDED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                    }
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateDivLevel() {
        String code = GlobalCC.checkNullValues(txtOdlCode.getValue());
        String dltCode = GlobalCC.checkNullValues(txtOdlDltCode.getValue());
        String desc = GlobalCC.checkNullValues(txtOdlDesc.getValue());
        String ranking = GlobalCC.checkNullValues(txtOdlRanking.getValue());
        String type = GlobalCC.checkNullValues(txtOdlType.getValue());
        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (dltCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: DLT Code is Empty");
            return null;

        } else if (desc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
            return null;

        } else if (ranking == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Ranking is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.orgDivisionLevels_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ORG_DIVISION_LEVELS_TAB",
                                                     conn);
                ArrayList levelList = new ArrayList();
                OrgDivisionLevel divlevel = new OrgDivisionLevel();
                divlevel.setSQLTypeName("TQC_ORG_DIVISION_LEVELS_OBJ");

                divlevel.setOdlcode(code);
                divlevel.setOdlDltCode(dltCode);
                divlevel.setOdlDesc(desc);
                divlevel.setOdlType(type);
                divlevel.setOdlRanking(ranking == null ? null :
                                       new BigDecimal(ranking));

                levelList.add(divlevel);
                ARRAY array = new ARRAY(descriptor, conn, levelList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:divLevelPop" + "').hide(hints);");

                ADFUtils.findIterator("fetchOrgDivisionLevelsIterator").executeQuery();
                GlobalCC.refreshUI(tblDivLevels);

                clearDivLevelFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    // ==========================================================================

    public void treeDltSubDivisionsListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeDltSubDivisions.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)treeDltSubDivisions.getRowData();

                    String nodeType =
                        nodeBinding.getRow().getAttribute("nodeType").toString();

                    /*if (nodeType.equals( "P" )) { // Primary : System has been selected
                  session.setAttribute( "sysCode", nodeBinding.getRow().getAttribute( "code" ) );
                  session.setAttribute( "osdCode", null );

                  panelOrgSubDivDetail.setVisible( false );

                } else { */ // Secondary : OrgSubDivision has been selected.
                    session.setAttribute("sysCode",
                                         nodeBinding.getRow().getAttribute("osdSysCode"));
                    session.setAttribute("osdCode",
                                         nodeBinding.getRow().getAttribute("osdCode"));
                    session.setAttribute("osdId",
                                         nodeBinding.getRow().getAttribute("osdId"));
                    session.setAttribute("osdDltCode",
                                         nodeBinding.getRow().getAttribute("osdDltCode"));
                    session.setAttribute("dltDesc",
                                         nodeBinding.getRow().getAttribute("dltDesc"));
                    session.setAttribute("osdOdlCode",
                                         nodeBinding.getRow().getAttribute("osdOdlCode"));
                    session.setAttribute("odlDesc",
                                         nodeBinding.getRow().getAttribute("odlDesc"));
                    session.setAttribute("agnCode",
                                         nodeBinding.getRow().getAttribute("osdDivHeadAgnCode"));
                    session.setAttribute("odlRanking",
                                         nodeBinding.getRow().getAttribute("odlRanking"));


                    txtOsdCode.setValue(nodeBinding.getRow().getAttribute("osdCode"));
                    txtOsdParentOsdCode.setValue(nodeBinding.getRow().getAttribute("osdParentOsdCode"));
                    txtOsdId.setValue(nodeBinding.getRow().getAttribute("osdId"));
                    txtOsdParentId.setValue(nodeBinding.getRow().getAttribute("osdParentOsdId"));
                    txtOsdDltCode.setValue(nodeBinding.getRow().getAttribute("osdDltCode"));
                    txtOsdDltDesc.setValue(nodeBinding.getRow().getAttribute("dltDesc"));
                    txtOsdOdlCode.setValue(nodeBinding.getRow().getAttribute("osdOdlCode"));
                    txtOsdOdlDesc.setValue(nodeBinding.getRow().getAttribute("odlDesc"));
                    txtOsdName.setValue(nodeBinding.getRow().getAttribute("osdName"));
                    txtOsdWef.setValue(nodeBinding.getRow().getAttribute("osdWef"));
                    txtOsdDivHeadAgnCode.setValue(nodeBinding.getRow().getAttribute("osdDivHeadAgnCode"));
                    txtOsdDivHeadAgnName.setValue(nodeBinding.getRow().getAttribute("agentName"));
                    txtOsdSysCode.setValue(nodeBinding.getRow().getAttribute("osdSysCode"));
                    txtPostLevel.setValue(nodeBinding.getRow().getAttribute("osdPostLevel"));
                    txtManagerAllowed.setValue(nodeBinding.getRow().getAttribute("osdManagerAllowed"));
                    overrideCommAll.setValue(nodeBinding.getRow().getAttribute("osdOverCommAllowed"));
                    txtOsdLocationCode.setValue(nodeBinding.getRow().getAttribute("osdLocationCode"));
                    txtOsdLocation.setValue(nodeBinding.getRow().getAttribute("osdLocation"));


                    session.setAttribute("osdName1",
                                         nodeBinding.getRow().getAttribute("osdName"));
                    session.setAttribute("odlType",
                                         nodeBinding.getRow().getAttribute("odlType"));
                    if (nodeBinding.getRow().getAttribute("odlType").toString().equalsIgnoreCase("O")) {
                        btnType.setVisible(false);
                        txtOsdName.setDisabled(false);
                    } else if (nodeBinding.getRow().getAttribute("odlType").toString().equalsIgnoreCase("R")) {
                        btnType.setVisible(true);
                        txtOsdName.setDisabled(true);
                        if (nodeBinding.getRow().getAttribute("regCode") !=
                            null) {
                            session.setAttribute("REGCode",
                                                 nodeBinding.getRow().getAttribute("regCode"));
                            txtOsdName.setLabel(nodeBinding.getRow().getAttribute("regCode").toString());
                        }
                    } else if (nodeBinding.getRow().getAttribute("odlType").toString().equalsIgnoreCase("B")) {
                        btnType.setVisible(true);
                        txtOsdName.setDisabled(true);
                        if (nodeBinding.getRow().getAttribute("brnCode") !=
                            null) {
                            txtOsdName.setLabel(nodeBinding.getRow().getAttribute("brnCode").toString());
                        }
                    }
                    GlobalCC.refreshUI(btnType);

                    txtOsdCode.setDisabled(true);
                    txtOsdId.setDisabled(true);
                    btnSaveUpdateOrgSubDivision.setText("Update");

                    panelOrgSubDivDetail.setVisible(true);

                    txtParentDivision.setVisible(false);

                    ADFUtils.findIterator("fetchOrgSubDivPrevHeadsByOrgSubDivisionIterator").executeQuery();
                    GlobalCC.refreshUI(tblPrevHeads);

                    ADFUtils.findIterator("fetchSpecificAgencyMarketersLovIterator").executeQuery();
                    GlobalCC.refreshUI(tblHeadAgentsPop2);

                    ADFUtils.findIterator("fetchOSDHeadsHistIterator").executeQuery();
                    GlobalCC.refreshUI(osDHeadHistTbl);


                    // }

                    GlobalCC.refreshUI(panelOrgSubDivDetail);
                } // End for
            } // End if
        } // End if
    }

    public void clearSubDivisionFields() {


        txtOsdCode.setValue(null);
        txtOsdParentOsdCode.setValue(session.getAttribute("osdCode"));
        txtOsdId.setValue(null);
        txtOsdParentId.setValue(session.getAttribute("osdId"));
        txtOsdDltCode.setValue(session.getAttribute("dltCode"));
        txtOsdDltDesc.setValue(session.getAttribute("dltDesc"));
        txtOsdOdlCode.setValue(null);
        txtOsdOdlDesc.setValue(null);
        txtOsdName.setValue(null);
        txtOsdWef.setValue(null);
        txtOsdDivHeadAgnCode.setValue(null);
        txtOsdDivHeadAgnName.setValue(null);
        txtPostLevel.setValue(null);
        txtManagerAllowed.setValue(null);
        overrideCommAll.setValue(null);
        txtOsdLocationCode.setValue(null);
        txtOsdLocation.setValue(null);
        txtOsdSysCode.setValue(session.getAttribute("sysCode"));

        txtParentDivision.setValue(session.getAttribute("osdName1"));
        txtParentDivision.setVisible(true);

        txtOsdCode.setDisabled(false);
        txtOsdId.setDisabled(false);
        btnSaveUpdateOrgSubDivision.setText("Save");

        panelOrgSubDivDetail.setVisible(true);
    }

    public String actionNewOrgSubDiv() {
        if (session.getAttribute("dltCode") != null) {


            if (session.getAttribute("odlRanking") == null) {
                // This is based on the assumption that the ranking will always start from 1.
                session.setAttribute("odlRanking", new BigDecimal("9999"));
            }
            session.getAttribute("odlRanking");
            BigDecimal fg = (BigDecimal)session.getAttribute("odlRanking");
            int currentRank = fg.intValue();

            // -1 means an error occured during check
            int maxRank = check_max_rank();

            if ((currentRank >= maxRank || maxRank == -1) &&
                (currentRank != 9999)) {
                GlobalCC.INFORMATIONREPORTING("End of Hierarchy::Max Level=" +
                                              maxRank +
                                              " :: To add another level first define Another Hierachy Level");
                return null;
            }

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
            String osdId = GlobalCC.checkNullValues(txtOsdId.getValue());
            String parentId =
                GlobalCC.checkNullValues(txtOsdParentId.getValue());
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
                DateFormat df = new SimpleDateFormat("dd-MMM-yy");

                Date tmpWefDate = new Date();
                if (txtOsdWef.getValue() != null &&
                    !(txtOsdWef.getValue().equals(""))) {
                    String date1 = df.format(txtOsdWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                subDivision.setOsdCode(code);
                subDivision.setOsdParentOsdCode(parentCode);
                subDivision.setOsdId(osdId == null ? null :
                                     new BigDecimal(osdId));
                subDivision.setOsdParentOsdId(parentId == null ? null :
                                              new BigDecimal(parentId));
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

                ADFUtils.findIterator("fetchOrgSubDivisionsByDltIterator").executeQuery();
                GlobalCC.refreshUI(treeDltSubDivisions);
                session.setAttribute("osdCode", null);
                session.setAttribute("osdId", null);
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
            String osdId = GlobalCC.checkNullValues(txtOsdId.getValue());
            String parentId =
                GlobalCC.checkNullValues(txtOsdParentId.getValue());
            String dltCode =
                GlobalCC.checkNullValues(txtOsdDltCode.getValue());
            String odlCode =
                GlobalCC.checkNullValues(txtOsdOdlCode.getValue());
            String name = GlobalCC.checkNullValues(txtOsdName.getValue());
            System.out.println("Name");
            System.out.println(name);
            String status = GlobalCC.checkNullValues(txtOsdStatus.getValue());
            String wef = GlobalCC.checkNullValues(txtOsdWef.getValue());
            String wet = GlobalCC.checkNullValues(txtOsdWet.getValue());
            String agnCode =
                GlobalCC.checkNullValues(txtOsdDivHeadAgnCode.getValue());
            String sysCode =
                GlobalCC.checkNullValues(txtOsdSysCode.getValue());
            String postLevel =
                GlobalCC.checkNullValues(txtPostLevel.getValue());
            String mngrAllowed =
                GlobalCC.checkNullValues(txtManagerAllowed.getValue());
            String overCommAll =
                GlobalCC.checkNullValues(overrideCommAll.getValue());
            String locationCode =
                GlobalCC.checkNullValues(txtOsdLocationCode.getValue());

            if (code == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
                return null;

            }
            if (dltCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Division Level Type is Empty");
                return null;

            }
            if (odlCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Division Level is Empty");
                return null;

            }
            if (name == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
                return null;

            }
            if (wef == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
                return null;

            }
            if (sysCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
                return null;
            }

            if (locationCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Location is Empty");
                return null;
            }

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
                DateFormat df = new SimpleDateFormat("dd-MMM-yy");

                Date tmpWefDate = new Date();
                if (txtOsdWef.getValue() != null &&
                    !(txtOsdWef.getValue().equals(""))) {
                    String date1 = df.format(txtOsdWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                Date tmpWetDate = null;
                if (txtOsdWet.getValue() != null &&
                    !(txtOsdWet.getValue().equals(""))) {
                    String date1 = df.format(txtOsdWet.getValue());
                    tmpWetDate = df.parse(date1);
                }


                subDivision.setOsdCode(code);
                subDivision.setOsdParentOsdCode(parentCode);
                subDivision.setOsdId(osdId == null ? null :
                                     new BigDecimal(osdId));
                subDivision.setOsdParentOsdId(parentId == null ? null :
                                              new BigDecimal(parentId));
                subDivision.setOsdDltCode(dltCode);
                subDivision.setOsdOdlCode(odlCode);
                subDivision.setOsdName(name);
                subDivision.setOsdStatus(status);
                subDivision.setOsdWef(tmpWefDate == null ? null :
                                      new java.sql.Date(tmpWefDate.getTime()));
                subDivision.setOsdWet(tmpWetDate == null ? null :
                                      new java.sql.Date(tmpWetDate.getTime()));
                subDivision.setOsdDivHeadAgnCode(agnCode == null ? null :
                                                 new BigDecimal(agnCode));
                subDivision.setOsdSysCode(sysCode == null ? null :
                                          new BigDecimal(sysCode));
                if (session.getAttribute("odlType").toString().equalsIgnoreCase("B")) {
                    if (txtOsdName.getLabel() != null) {
                        subDivision.setBrnCode(new BigDecimal(txtOsdName.getLabel()));
                    }
                }
                if (session.getAttribute("odlType").toString().equalsIgnoreCase("R")) {
                    if (txtOsdName.getLabel() != null) {
                        // subDivision.setRegCode(new BigDecimal(txtOsdName.getLabel()));
                        subDivision.setRegCode(new BigDecimal(locationCode));
                    }
                }
                subDivision.setOsdPostLevel(postLevel);
                subDivision.setOsdManagerAllowed(mngrAllowed);
                subDivision.setOsdOverCommAllowed(overCommAll);
                subDivision.setOsdLocationCode(locationCode == null ? null :
                                               new BigDecimal(locationCode));
                subDivisionList.add(subDivision);

                System.out.println(subDivisionList);
                ARRAY array =
                    new ARRAY(descriptor, conn, subDivisionList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "A");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchOrgSubDivisionsByDltIterator").executeQuery();
                GlobalCC.refreshUI(treeDltSubDivisions);

                ADFUtils.findIterator("fetchOSDHeadsHistIterator").executeQuery();
                GlobalCC.refreshUI(osDHeadHistTbl);

                clearSubDivisionFields();

                String message = "New Record ADDED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            } finally {
                DbUtils.closeQuietly(conn, statement, null);
            }
        }
        return null;
    }

    public String actionUpdateOrgSubDivision() {
        String code = GlobalCC.checkNullValues(txtOsdCode.getValue());
        String parentCode =
            GlobalCC.checkNullValues(txtOsdParentOsdCode.getValue());
        String osdId = GlobalCC.checkNullValues(txtOsdId.getValue());
        String parentId = GlobalCC.checkNullValues(txtOsdParentId.getValue());
        String dltCode = GlobalCC.checkNullValues(txtOsdDltCode.getValue());
        String odlCode = GlobalCC.checkNullValues(txtOsdOdlCode.getValue());
        String name = GlobalCC.checkNullValues(txtOsdName.getValue());
        String status = GlobalCC.checkNullValues(txtOsdStatus.getValue());
        String wef = GlobalCC.checkNullValues(txtOsdWef.getValue());
        String wet = GlobalCC.checkNullValues(txtOsdWet.getValue());
        String agnCode =
            GlobalCC.checkNullValues(txtOsdDivHeadAgnCode.getValue());
        String sysCode = GlobalCC.checkNullValues(txtOsdSysCode.getValue());
        String postLevel = GlobalCC.checkNullValues(txtPostLevel.getValue());
        String mngrAllowed =
            GlobalCC.checkNullValues(txtManagerAllowed.getValue());
        String overCommAll =
            GlobalCC.checkNullValues(overrideCommAll.getValue());
        String locationCode =
            GlobalCC.checkNullValues(txtOsdLocationCode.getValue());


        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        }
        if (dltCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Division Level Type is Empty");
            return null;

        }
        if (odlCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Division Level is Empty");
            return null;
        }
        if (name == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
            return null;

        }
        if (wef == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
            return null;
        }
        /*if (wet == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WET Date is Empty");
            return null;
        } */
        if (sysCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
            return null;

        }

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
            DateFormat df = new SimpleDateFormat("dd-MMM-yy");

            Date tmpWefDate = new Date();
            if (txtOsdWef.getValue() != null &&
                !(txtOsdWef.getValue().equals(""))) {
                String date1 = df.format(txtOsdWef.getValue());
                tmpWefDate = df.parse(date1);
            }
            Date tmpWetDate = null;
            if (txtOsdWet.getValue() != null &&
                !(txtOsdWet.getValue().equals(""))) {
                String date1 = df.format(txtOsdWet.getValue());
                tmpWetDate = df.parse(date1);
            }

            subDivision.setOsdCode(code);
            subDivision.setOsdParentOsdCode(parentCode);
            subDivision.setOsdParentOsdId(parentId == null ? null :
                                          new BigDecimal(parentId));
            subDivision.setOsdId(osdId == null ? null : new BigDecimal(osdId));
            subDivision.setOsdDltCode(dltCode);
            subDivision.setOsdOdlCode(odlCode);
            subDivision.setOsdName(name);
            subDivision.setOsdStatus(status);
            subDivision.setOsdWef(tmpWefDate == null ? null :
                                  new java.sql.Date(tmpWefDate.getTime()));
            subDivision.setOsdWet(tmpWetDate == null ? null :
                                  new java.sql.Date(tmpWetDate.getTime()));
            subDivision.setOsdDivHeadAgnCode(agnCode == null ? null :
                                             new BigDecimal(agnCode));
            subDivision.setOsdSysCode(sysCode == null ? null :
                                      new BigDecimal(sysCode));
            if (session.getAttribute("odlType").toString().equalsIgnoreCase("B")) {
                if (txtOsdName.getLabel() != null) {
                    subDivision.setBrnCode(new BigDecimal(txtOsdName.getLabel()));
                }
            }
            if (session.getAttribute("odlType").toString().equalsIgnoreCase("R")) {
                if (txtOsdName.getLabel() != null) {
                    subDivision.setRegCode(new BigDecimal(txtOsdName.getLabel()));
                }
            }
            subDivision.setOsdPostLevel(postLevel);
            subDivision.setOsdManagerAllowed(mngrAllowed);
            subDivision.setOsdOverCommAllowed(overCommAll);
            subDivision.setOsdLocationCode(locationCode == null ? null :
                                           new BigDecimal(locationCode));
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

            ADFUtils.findIterator("fetchOrgSubDivisionsByDltIterator").executeQuery();
            GlobalCC.refreshUI(treeDltSubDivisions);

            clearSubDivisionFields();

            String message = "Record UPDATED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, statement, null);
        }

        return null;
    }

    public String actionShowAccountTypes() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:accountTypesPop" + "').show(hints);");
        return null;
    }

    public String actionShowHeadAccountTypes() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:headAccountTypesPop" + "').show(hints);");
        return null;
    }


    public String actionShowAgencyIntermediaries() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:intermediary" + "').show(hints);");
        return null;
    }

    public String actionAcceptAccountType() {
        Object key2 = tblAccountTypesPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDltActCode.setValue(nodeBinding.getAttribute("code"));
            txtDltActName.setValue(nodeBinding.getAttribute("accountType"));

            session.setAttribute("actCode", nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("findAgentIntermediariesIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyIntermediaryPop);

            txtIntermediaryCode.setValue(null);
            txtAgencyIntermediaries.setValue(null);

            GlobalCC.refreshUI(panelDivLevelTypePop);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:accountTypesPop" + "').hide(hints);");
        }
        return null;
    }

    public String actionAcceptHeadAccountType() {
        Object key2 = tblHeadAccountTypesPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtHeadDltActCode.setValue(nodeBinding.getAttribute("code"));
            txtHeadDltActName.setValue(nodeBinding.getAttribute("accountType"));

            GlobalCC.refreshUI(panelDivLevelTypePop);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:headAccountTypesPop" +
                                 "').hide(hints);");
        }
        return null;
    }

    public String actionAcceptAgencyIntermediary() {
        Object key2 = tblAgencyIntermediaryPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtIntermediaryCode.setValue(nodeBinding.getAttribute("intCode"));
            txtAgencyIntermediaries.setValue(nodeBinding.getAttribute("intName"));

            GlobalCC.refreshUI(panelDivLevelTypePop);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:intermediary" + "').hide(hints);");
        }
        return null;
    }

    public String actionShowOrgDivLevel() {
        if (session.getAttribute("dltCode") != null) {
            ADFUtils.findIterator("fetchOrgDivisionLevelsByRankingIterator").executeQuery();


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
            session.setAttribute("odlType",
                                 nodeBinding.getAttribute("odlType"));
            btnType.setVisible(false);
            txtOsdName.setDisabled(false);
            //            if (nodeBinding.getAttribute("odlType").toString().equalsIgnoreCase("O")) {
            //                btnType.setVisible(false);
            //                txtOsdName.setDisabled(false);
            //            } else {
            //                btnType.setVisible(true);
            //                txtOsdName.setDisabled(true);
            //            }
            txtOsdOdlCode.setValue(nodeBinding.getAttribute("odlcode"));
            txtOsdOdlDesc.setValue(nodeBinding.getAttribute("odlDesc"));
            txtOsdLocation.setValue(null);
            txtOsdLocationCode.setValue(null);
            session.setAttribute("osdOdlCode",
                                 nodeBinding.getAttribute("odlcode"));
            GlobalCC.refreshUI(txtOsdOdlDesc);
            GlobalCC.refreshUI(txtOsdOdlCode);
            GlobalCC.refreshUI(btnType);
            GlobalCC.refreshUI(txtOsdName);
            GlobalCC.refreshUI(txtOsdLocationCode);
            GlobalCC.refreshUI(txtOsdLocation);
        }
        GlobalCC.dismissPopUp("crm", "orgSubDivLevel");
        return null;
    }

    public String actionShowHeadAgents() {
        ADFUtils.findIterator("fetchAllAgencyMarketersLovIterator").executeQuery();
        GlobalCC.refreshUI(tblHeadAgentsPop);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:agentsListPop" + "').show(hints);");
        return null;
    }

    public String actionShowDivisionLoaction() {
        ADFUtils.findIterator("fetchDivisionLocationIterator").executeQuery();
        GlobalCC.refreshUI(tblDivisionLocationPop);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:divisionLocationPop" + "').show(hints);");
        return null;
    }

    public String divisionLocationSelected() {
        Object key2 = tblDivisionLocationPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtOsdLocationCode.setValue(nodeBinding.getAttribute("divLocationCode"));
            txtOsdLocation.setValue(nodeBinding.getAttribute("divLocationName"));
            GlobalCC.refreshUI(txtOsdLocation);
            GlobalCC.refreshUI(txtOsdLocationCode);
        }
        GlobalCC.dismissPopUp("crm", "divisionLocationPop");
        return null;
    }

    public String actionAcceptHeadAgent() {
        Object key2 = tblHeadAgentsPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtOsdDivHeadAgnCode.setValue(nodeBinding.getAttribute("code"));
            txtOsdDivHeadAgnName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtOsdDivHeadAgnName);
            GlobalCC.refreshUI(txtOsdDivHeadAgnCode);
        }
        GlobalCC.dismissPopUp("crm", "agentsListPop");
        return null;
    }

    // ============== PREVIOUS HEADS =========================================

    public void clearPrevHeadsFields() {
        txtOsdphCode.setValue(null);
        txtOsdphOsdCode.setValue(session.getAttribute("osdCode"));
        txtOsdphOsdId.setValue(session.getAttribute("osdId"));
        txtOsdphOsdName.setValue(null);
        txtOsdphPrevAgnCode.setValue(null);
        txtAgencyName.setValue(null);
        txtOsdphWet.setValue(null);

        btnSaveUpdatePrevHead.setText("Save");
    }

    public String actionNewPrevHead() {
        if (session.getAttribute("osdCode") != null) {
            clearPrevHeadsFields();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:prevHeadPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Org SubDivision record to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditPrevHead() {
        Object key2 = tblPrevHeads.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtOsdphCode.setValue(nodeBinding.getAttribute("osdphCode"));
            txtOsdphOsdCode.setValue(nodeBinding.getAttribute("osdphOsdCode"));
            txtOsdphOsdId.setValue(nodeBinding.getAttribute("osdphOsdId"));
            txtOsdphOsdName.setValue(nodeBinding.getAttribute("osdName"));
            txtOsdphPrevAgnCode.setValue(nodeBinding.getAttribute("osdphPrevAgnCode"));
            txtAgencyName.setValue(nodeBinding.getAttribute("agencyName"));
            txtOsdphWet.setValue(nodeBinding.getAttribute("osdphWet"));

            btnSaveUpdatePrevHead.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:prevHeadPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing record to proceed.");
            return null;
        }
        return null;
    }

    public String actionDeletePrevHead() {
        Object key2 = tblPrevHeads.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("osdphCode").toString();
            String osdCode =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("osdphOsdCode"));
            String osdId =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("osdphOsdId"));
            String prevAgnCode = null;
            String wet = null;

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.orgSubDivPrevHeads_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ORG_SUBDIV_PREV_HEADS_TAB",
                                                     conn);
                ArrayList prevHeadList = new ArrayList();
                OrgSubDivPreviousHead prevHead = new OrgSubDivPreviousHead();
                prevHead.setSQLTypeName("TQC_ORG_SUBDIV_PREV_HEADS_OBJ");

                prevHead.setOsdphCode(code == null ? null :
                                      new BigDecimal(code));
                prevHead.setOsdphOsdCode(osdCode);
                prevHead.setOsdphPrevAgnCode(prevAgnCode == null ? null :
                                             new BigDecimal(prevAgnCode));
                prevHead.setOsdphWet(null);
                prevHead.setOsdphOsdId(osdId == null ? null :
                                       new BigDecimal(osdId));
                prevHeadList.add(prevHead);
                ARRAY array =
                    new ARRAY(descriptor, conn, prevHeadList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchOrgSubDivPrevHeadsByOrgSubDivisionIterator").executeQuery();
                GlobalCC.refreshUI(tblPrevHeads);

                clearPrevHeadsFields();

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing record to proceed.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdatePrevHead() {
        if (btnSaveUpdatePrevHead.getText().equals("Edit")) {
            actionUpdatePrevHead();
        } else {
            BigDecimal code =
                GlobalCC.checkBDNullValues(txtOsdphCode.getValue());
            String osdCode =
                GlobalCC.checkNullValues(txtOsdphOsdCode.getValue());
            BigDecimal osdId =
                GlobalCC.checkBDNullValues(txtOsdphOsdId.getValue());
            BigDecimal prevAgnCode =
                GlobalCC.checkBDNullValues(txtOsdphPrevAgnCode.getValue());
            java.sql.Date wet = GlobalCC.extractDate(txtOsdphWet);

            if (osdCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Org SubDivision is Empty");
                return null;

            } else if (prevAgnCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Previous Head Agent is Empty");
                return null;

            } else if (wet == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WET Date is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.orgSubDivPrevHeads_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ORG_SUBDIV_PREV_HEADS_TAB",
                                                         conn);
                    ArrayList prevHeadList = new ArrayList();
                    OrgSubDivPreviousHead prevHead =
                        new OrgSubDivPreviousHead();
                    prevHead.setSQLTypeName("TQC_ORG_SUBDIV_PREV_HEADS_OBJ");

                    // Take care of all the date fields on the form.
                    DateFormat df = new SimpleDateFormat("dd-MMM-yy");

                    prevHead.setOsdphCode(code);
                    prevHead.setOsdphOsdCode(osdCode);
                    prevHead.setOsdphPrevAgnCode(prevAgnCode);
                    prevHead.setOsdphWet(wet);
                    prevHead.setOsdphOsdId(osdId);
                    prevHeadList.add(prevHead);
                    ARRAY array =
                        new ARRAY(descriptor, conn, prevHeadList.toArray());

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "A");
                    statement.setArray(2, array);
                    statement.execute();

                    statement.close();
                    conn.commit();
                    conn.close();

                    GlobalCC.hidePopup("crm:prevHeadPop");

                    ADFUtils.findIterator("fetchOrgSubDivPrevHeadsByOrgSubDivisionIterator").executeQuery();
                    GlobalCC.refreshUI(tblPrevHeads);

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdatePrevHead() {
        String code = GlobalCC.checkNullValues(txtOsdphCode.getValue());
        String osdCode = GlobalCC.checkNullValues(txtOsdphOsdCode.getValue());
        String prevAgnCode =
            GlobalCC.checkNullValues(txtOsdphPrevAgnCode.getValue());
        String wet = GlobalCC.checkNullValues(txtOsdphWet.getValue());
        String osdId = GlobalCC.checkNullValues(txtOsdphOsdId.getValue());

        if (osdCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Org SubDivision is Empty");
            return null;

        } else if (osdCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Org SubDivision is Empty");
            return null;

        } else if (prevAgnCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Previous Head Agent is Empty");
            return null;

        } else if (wet == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WET Date is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.orgSubDivPrevHeads_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ORG_SUBDIV_PREV_HEADS_TAB",
                                                     conn);
                ArrayList prevHeadList = new ArrayList();
                OrgSubDivPreviousHead prevHead = new OrgSubDivPreviousHead();
                prevHead.setSQLTypeName("TQC_ORG_SUBDIV_PREV_HEADS_OBJ");

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("dd-MMM-yy");

                Date tmpWefDate = null;
                if (txtOsdphWet.getValue() != null &&
                    !(txtOsdphWet.getValue().equals(""))) {
                    String date1 = df.format(txtOsdphWet.getValue());
                    tmpWefDate = df.parse(date1);
                }

                prevHead.setOsdphCode(code == null ? null :
                                      new BigDecimal(code));
                prevHead.setOsdphOsdCode(osdCode);
                prevHead.setOsdphPrevAgnCode(prevAgnCode == null ? null :
                                             new BigDecimal(prevAgnCode));
                prevHead.setOsdphWet(tmpWefDate == null ? null :
                                     new java.sql.Date(tmpWefDate.getTime()));
                prevHead.setOsdphOsdId(osdId == null ? null :
                                       new BigDecimal(osdId));
                prevHeadList.add(prevHead);
                ARRAY array =
                    new ARRAY(descriptor, conn, prevHeadList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:prevHeadPop" + "').hide(hints);");

                ADFUtils.findIterator("fetchOrgSubDivPrevHeadsByOrgSubDivisionIterator").executeQuery();
                GlobalCC.refreshUI(tblPrevHeads);

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionShowHeadAgentPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:agentsListPop2" + "').show(hints);");
        return null;
    }

    public void setTxtOsdphOsdName(RichInputText txtOsdphOsdName) {
        this.txtOsdphOsdName = txtOsdphOsdName;
    }

    public RichInputText getTxtOsdphOsdName() {
        return txtOsdphOsdName;
    }

    public void setTblHeadAgentsPop2(RichTable tblHeadAgentsPop2) {
        this.tblHeadAgentsPop2 = tblHeadAgentsPop2;
    }

    public RichTable getTblHeadAgentsPop2() {
        return tblHeadAgentsPop2;
    }

    public String actionAcceptHeadAgent2() {
        Object key2 = tblHeadAgentsPop2.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtOsdphPrevAgnCode.setValue(nodeBinding.getAttribute("code"));
            txtAgencyName.setValue(nodeBinding.getAttribute("name"));
        }

        GlobalCC.refreshUI(panelPrevHeads);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:agentsListPop2" + "').hide(hints);");
        return null;
    }

    public int check_max_rank() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        int max = -1;
        int rankStatus = 0;

        try {

            if (session.getAttribute("dltCode") == null) {

                return -1;
            } else {

                connection =
                        (OracleConnection)dbConnector.getDatabaseConnection();
                String query1 =
                    "begin ? := TQC_SETUPS_CURSOR.check_max_rank(?); end;";
                stmt = (OracleCallableStatement)connection.prepareCall(query1);
                stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
                stmt.setString(2, (String)session.getAttribute("dltCode"));

                stmt.execute();
                rs = (OracleResultSet)stmt.getObject(1);
                while (rs.next()) {
                    max = rs.getInt(1);

                }
                rs.close();

                stmt.close();
                connection.commit();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }

        return max;

    }

    public void setPanelPrevHeads(RichPanelBox panelPrevHeads) {
        this.panelPrevHeads = panelPrevHeads;
    }

    public RichPanelBox getPanelPrevHeads() {
        return panelPrevHeads;
    }

    public void setTxtParentDivision(RichInputText txtParentDivision) {
        this.txtParentDivision = txtParentDivision;
    }

    public RichInputText getTxtParentDivision() {
        return txtParentDivision;
    }

    public void setTxtOdlType(RichSelectOneChoice txtOdlType) {
        this.txtOdlType = txtOdlType;
    }

    public RichSelectOneChoice getTxtOdlType() {
        return txtOdlType;
    }

    public String launchType() {
        System.out.println("odlType: " + session.getAttribute("odlType"));
        if (session.getAttribute("odlType").toString().equalsIgnoreCase("R")) {
            GlobalCC.showPopUp("crm", "reg");
        } else if (session.getAttribute("odlType").toString().equalsIgnoreCase("B")) {
            GlobalCC.showPopUp("crm", "branch");
        }
        return null;
    }

    public void setTxtType(RichInputText txtType) {
        this.txtType = txtType;
    }

    public RichInputText getTxtType() {
        return txtType;
    }

    public void setBtnType(RichCommandButton btnType) {
        this.btnType = btnType;
    }

    public RichCommandButton getBtnType() {
        return btnType;
    }

    public void setRegTab(RichTable regTab) {
        this.regTab = regTab;
    }

    public RichTable getRegTab() {
        return regTab;
    }

    public void setBranchTab(RichTable branchTab) {
        this.branchTab = branchTab;
    }

    public RichTable getBranchTab() {
        return branchTab;
    }

    public String branchSelected() {
        Object key2 = branchTab.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        System.out.println("MY_BRN_CODE: " +
                           nodeBinding.getAttribute("brnCode").toString());

        if (nodeBinding != null) {
            txtOsdName.setLabel(nodeBinding.getAttribute("brnCode").toString());
            txtOsdName.setValue(nodeBinding.getAttribute("brnName"));
        }

        GlobalCC.refreshUI(txtOsdName);
        return null;
    }

    public String regionSelected() {
        Object key2 = regTab.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtOsdName.setLabel(nodeBinding.getAttribute("regCode").toString());
            txtOsdName.setValue(nodeBinding.getAttribute("regName"));
        }

        GlobalCC.refreshUI(txtOsdName);
        return null;
    }

    public void setTxtPostLevel(RichSelectOneChoice txtPostLevel) {
        this.txtPostLevel = txtPostLevel;
    }

    public RichSelectOneChoice getTxtPostLevel() {
        return txtPostLevel;
    }

    public void setTxtManagerAllowed(RichSelectOneChoice txtManagerAllowed) {
        this.txtManagerAllowed = txtManagerAllowed;
    }

    public RichSelectOneChoice getTxtManagerAllowed() {
        return txtManagerAllowed;
    }

    public void setOverrideCommAll(RichSelectOneChoice overrideCommAll) {
        this.overrideCommAll = overrideCommAll;
    }

    public RichSelectOneChoice getOverrideCommAll() {
        return overrideCommAll;
    }

    public void setTxtOsdId(RichInputNumberSpinbox txtOsdId) {
        this.txtOsdId = txtOsdId;
    }

    public RichInputNumberSpinbox getTxtOsdId() {
        return txtOsdId;
    }

    public void setTxtOsdParentId(RichInputNumberSpinbox txtOsdParentId) {
        this.txtOsdParentId = txtOsdParentId;
    }

    public RichInputNumberSpinbox getTxtOsdParentId() {
        return txtOsdParentId;
    }

    public void setTxtOsdphOsdId(RichInputNumberSpinbox txtOsdphOsdId) {
        this.txtOsdphOsdId = txtOsdphOsdId;
    }

    public RichInputNumberSpinbox getTxtOsdphOsdId() {
        return txtOsdphOsdId;
    }

    /**
     * This method is used to keep the nodes with value=S exapnded when the
     * page renders for the first time
     * @return
     */
    public RowKeySet getDisclosedRowKeys() {
        if (!initDisclosure)
            initializeDisclosedRowKeys();
        return disclosedRowKeys;
    }

    private void initializeDisclosedRowKeys() {
        UIXTree tree = getTreeDltSubDivisions();
        if (tree != null && tree.getValue() != null) {
            initDisclosure = true;
            disclosedRowKeys.setCollectionModel(ModelUtils.toTreeModel(tree.getValue()));
            discloseNodes(tree, getNodeValueTobeExpanded());
        }
    }

    private String getNodeValueTobeExpanded() {
        /* DCBindingContainer dcb =
          (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
      DCIteratorBinding dci =
          dcb.findIteratorBinding("EmpListView1Iterator");
      String fstName = (String)dci.getCurrentRow().getAttribute("FirstName");
      return fstName;
*/
        return "mmmm";
    }


    /**
     * This  method to expands nodes matching the 'nodeValueToBeExapnded' parameter
     * @param tree
     * @param _disclosedRowKeys
     * @param nodeValueToBeExapnded
     * @return
     */
    private boolean discloseNodes(UIXTree tree, String nodeValueToBeExapnded) {

        boolean isFound = false;

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.setRowIndex(i);
            System.out.println("iS CONTAINER=" + tree.isContainer());
            if (tree.isContainer()) {
                tree.enterContainer();
                if (tree.getRowCount() > 0 &&
                    discloseNodes(tree, nodeValueToBeExapnded)) {
                    tree.exitContainer();
                    tree.getDisclosedRowKeys().add();
                    System.out.println("Adding node.");

                    isFound = true;
                } else {
                    tree.exitContainer();
                }
            } else {
                System.out.println("Row data=" + tree.getRowData().toString());
                if (tree.getRowData() != null &&
                    tree.getRowData().toString().contains(nodeValueToBeExapnded)) {
                    isFound = true;


                }

            }
        }

        return isFound;
    }

    public void setTxtOsdWet(RichInputDate txtOsdWet) {
        this.txtOsdWet = txtOsdWet;
    }

    public RichInputDate getTxtOsdWet() {
        return txtOsdWet;
    }

    public void setTxtOsdStatus(RichSelectOneChoice txtOsdStatus) {
        this.txtOsdStatus = txtOsdStatus;
    }

    public RichSelectOneChoice getTxtOsdStatus() {
        return txtOsdStatus;
    }

    public void setSelDltType(RichSelectOneChoice selDltType) {
        this.selDltType = selDltType;
    }

    public RichSelectOneChoice getSelDltType() {
        return selDltType;
    }

    public void setTxtHeadDltActCode(RichInputText txtHeadDltActCode) {
        this.txtHeadDltActCode = txtHeadDltActCode;
    }

    public RichInputText getTxtHeadDltActCode() {
        return txtHeadDltActCode;
    }

    public void setTxtHeadDltActName(RichInputText txtHeadDltActName) {
        this.txtHeadDltActName = txtHeadDltActName;
    }

    public RichInputText getTxtHeadDltActName() {
        return txtHeadDltActName;
    }

    public void setTblHeadAccountTypesPop(RichTable tblHeadAccountTypesPop) {
        this.tblHeadAccountTypesPop = tblHeadAccountTypesPop;
    }

    public RichTable getTblHeadAccountTypesPop() {
        return tblHeadAccountTypesPop;
    }

    public void setTxtAgencyIntermediaries(RichInputText txtAgencyIntermediaries) {
        this.txtAgencyIntermediaries = txtAgencyIntermediaries;
    }

    public RichInputText getTxtAgencyIntermediaries() {
        return txtAgencyIntermediaries;
    }

    public void setTxtIntermediaryCode(RichInputText txtIntermediaryCode) {
        this.txtIntermediaryCode = txtIntermediaryCode;
    }

    public RichInputText getTxtIntermediaryCode() {
        return txtIntermediaryCode;
    }

    public void setTblAgencyIntermediaryPop(RichTable tblAgencyIntermediaryPop) {
        this.tblAgencyIntermediaryPop = tblAgencyIntermediaryPop;
    }

    public RichTable getTblAgencyIntermediaryPop() {
        return tblAgencyIntermediaryPop;
    }

    public void setSelPayIntermediary(RichSelectOneChoice selPayIntermediary) {
        this.selPayIntermediary = selPayIntermediary;
    }

    public RichSelectOneChoice getSelPayIntermediary() {
        return selPayIntermediary;
    }

    public void setTxtOsdLocation(RichInputText txtOsdLocation) {
        this.txtOsdLocation = txtOsdLocation;
    }

    public RichInputText getTxtOsdLocation() {
        return txtOsdLocation;
    }

    public void setTblDivisionLocationPop(RichTable tblDivisionLocationPop) {
        this.tblDivisionLocationPop = tblDivisionLocationPop;
    }

    public RichTable getTblDivisionLocationPop() {
        return tblDivisionLocationPop;
    }

    public void setTxtOsdLocationCode(RichInputText txtOsdLocationCode) {
        this.txtOsdLocationCode = txtOsdLocationCode;
    }

    public RichInputText getTxtOsdLocationCode() {
        return txtOsdLocationCode;
    }

    public void setPanelOrgSubDivHeadHistory(RichPanelBox panelOrgSubDivHeadHistory) {
        this.panelOrgSubDivHeadHistory = panelOrgSubDivHeadHistory;
    }

    public RichPanelBox getPanelOrgSubDivHeadHistory() {
        return panelOrgSubDivHeadHistory;
    }

    public void setOsDHeadHistTbl(RichTable osDHeadHistTbl) {
        this.osDHeadHistTbl = osDHeadHistTbl;
    }

    public RichTable getOsDHeadHistTbl() {
        return osDHeadHistTbl;
    }

    public void setBtnSaveEditOrgSubDivHead(RichCommandButton btnSaveEditOrgSubDivHead) {
        this.btnSaveEditOrgSubDivHead = btnSaveEditOrgSubDivHead;
    }

    public RichCommandButton getBtnSaveEditOrgSubDivHead() {
        return btnSaveEditOrgSubDivHead;
    }

    public void setEditDivHeadHistPopUp(RichPanelBox editDivHeadHistPopUp) {
        this.editDivHeadHistPopUp = editDivHeadHistPopUp;
    }

    public RichPanelBox getEditDivHeadHistPopUp() {
        return editDivHeadHistPopUp;
    }

    public void setBtnEditOrgSubDivHead(RichCommandButton btnEditOrgSubDivHead) {
        this.btnEditOrgSubDivHead = btnEditOrgSubDivHead;
    }

    public RichCommandButton getBtnEditOrgSubDivHead() {
        return btnEditOrgSubDivHead;
    }

    public String actionUpdateOrgSubDivHistory() {

        BigDecimal odh_code = (BigDecimal)session.getAttribute("odh_code");
        BigDecimal odh_agn_code =
            (BigDecimal)session.getAttribute("odh_osd_div_head_agn_code");
        String wef = GlobalCC.checkNullValues(txtODHAgnWEF.getValue());
        String wet = GlobalCC.checkNullValues(txtODHAgnWET.getValue());

        if (wef == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
            return null;
        }

        //        if (wet == null) {
        //            GlobalCC.errorValueNotEntered("Error Value Missing: WET Date is Empty");
        //            return null;
        //        }

        DateFormat df = new SimpleDateFormat("dd-MMM-yy");

        Date tmpWefDate = null;
        Date tmpWetDate = null;
        if (txtODHAgnWEF.getValue() != null &&
            !(txtODHAgnWEF.getValue().equals(""))) {
            wef = df.format(txtODHAgnWEF.getValue());
          //  tmpWefDate = df.parse(wef);
        }

        if (txtODHAgnWET.getValue() != null &&
            !(txtODHAgnWET.getValue().equals(""))) {
            wet = df.format(txtODHAgnWET.getValue());
           // tmpWetDate = df.parse(wet);
        }

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;

        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin TQC_SETUPS_PKG.update_div_head_history(?,?,?); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.setBigDecimal(1, odh_code);
            stmt.setString(2, wef);
            stmt.setString(3, wet);
            stmt.execute();
            stmt.close();
            connection.commit();
            connection.close();

            String message = "Record updated successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

            GlobalCC.dismissPopUp("crm", "editDivHeadHist");

            ADFUtils.findIterator("fetchOSDHeadsHistIterator").executeQuery();
            GlobalCC.refreshUI(osDHeadHistTbl);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String actionCancelUpdateOrgSubDivHeadHist() {
        GlobalCC.dismissPopUp("crm", "editDivHeadHist");
        return null;
    }

    public String actionEditOrgSubDivHeadHistBtn() {

        Object key2 = osDHeadHistTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtODHAgnShtDsc.setValue(nodeBinding.getAttribute("odh_agn_sht_desc"));
            txtODHAgnNAME.setValue(nodeBinding.getAttribute("odh_agn_name"));
            txtODHAgnWEF.setValue(nodeBinding.getAttribute("odh_wef_date"));
            txtODHAgnWET.setValue(nodeBinding.getAttribute("odh_wet_date"));
            session.setAttribute("odh_code",
                                 nodeBinding.getAttribute("odh_code"));
            session.setAttribute("odh_osd_div_head_agn_code",
                                 nodeBinding.getAttribute("odh_osd_div_head_agn_code"));

            GlobalCC.refreshUI(txtODHAgnShtDsc);
            GlobalCC.refreshUI(txtODHAgnNAME);
            GlobalCC.refreshUI(txtODHAgnWEF);
            GlobalCC.refreshUI(txtODHAgnWET);
        }

        GlobalCC.showPopUp("crm", "editDivHeadHist");
        return null;
    }

    public void setTxtODHAgnShtDsc(RichInputText txtODHAgnShtDsc) {
        this.txtODHAgnShtDsc = txtODHAgnShtDsc;
    }

    public RichInputText getTxtODHAgnShtDsc() {
        return txtODHAgnShtDsc;
    }

    public void setTxtODHAgnNAME(RichInputText txtODHAgnNAME) {
        this.txtODHAgnNAME = txtODHAgnNAME;
    }

    public RichInputText getTxtODHAgnNAME() {
        return txtODHAgnNAME;
    }

    public void setTxtODHAgnWEF(RichInputDate txtODHAgnWEF) {
        this.txtODHAgnWEF = txtODHAgnWEF;
    }

    public RichInputDate getTxtODHAgnWEF() {
        return txtODHAgnWEF;
    }

    public void setTxtODHAgnWET(RichInputDate txtODHAgnWET) {
        this.txtODHAgnWET = txtODHAgnWET;
    }

    public RichInputDate getTxtODHAgnWET() {
        return txtODHAgnWET;
    }
}
