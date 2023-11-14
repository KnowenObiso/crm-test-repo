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
import TurnQuest.view.Base.IQuery;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.models.SystemPost;
import TurnQuest.view.models.SystemPostLevel;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

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


public class PostLevelsBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblSystems;
    private RichTable tblSysPostLevels;
    private RichInputText txtSysplSysCode;
    private RichInputText txtSysplCode;
    private RichInputText txtSysplShtDesc;
    private RichInputText txtSysplDesc;
    private RichInputNumberSpinbox txtSysplRanking;
    private RichInputDate txtSysplWef;
    private RichCommandButton btnSaveUpdateSysPostLevel;
    private RichTree treeSysPosts;
    private RichInputText txtSpostSysCode;
    private RichInputText txtSpostSysplCode;
    private RichInputText txtSpostParentSpostCode;
    private RichInputText txtSpostCode;
    private RichInputText txtSpostShtDesc;
    private RichInputText txtSpostDesc;
    private RichInputText txtSpostRemarks;
    private RichInputDate txtSpostWef;
    private RichInputText txtSpostBrnCode;
    private RichInputText txtSpostSubdivOsdCode;
    private RichInputText txtSpostUsrCode;
    private RichCommandButton btnSaveUpdatePost;
    private RichPanelBox panelSysPosts;
    private RichInputText txtSpostSubdivOsdName;
    private RichTable tblSubdivisionsPop;
    private RichTable tblAgencyBranch;
    private RichTable tblbranches;
    private RichTable tblBranch;
    private RichTable branchtbl;
    private RichInputText brnCode;
    private RichInputText brnDesc;
    private RichInputText brnReg;
    private RichInputText brnname;
    private RichInputText brnAddress;
    private RichInputText brnBankCode;
    private RichInputText brnBankdesc;
    private RichCommandButton btnSetBnkCode;
    private RichTable tblBranches;
    private RichTable tblBrn;
    private RichTable branchtb;
    private RichTable tblBran;
    private RichTable tblbankBranches;
    private RichInputText brnchCode;
    private RichInputText txtBranchesCode;

    public PostLevelsBacking() {
    }

    public void setTblSystems(RichTable tblSystems) {
        this.tblSystems = tblSystems;
    }

    public RichTable getTblSystems() {
        return tblSystems;
    }

    public void setTblSysPostLevels(RichTable tblSysPostLevels) {
        this.tblSysPostLevels = tblSysPostLevels;
    }

    public RichTable getTblSysPostLevels() {
        return tblSysPostLevels;
    }

    public void setTxtSysplSysCode(RichInputText txtSysplSysCode) {
        this.txtSysplSysCode = txtSysplSysCode;
    }

    public RichInputText getTxtSysplSysCode() {
        return txtSysplSysCode;
    }

    public void setTxtSysplCode(RichInputText txtSysplCode) {
        this.txtSysplCode = txtSysplCode;
    }

    public RichInputText getTxtSysplCode() {
        return txtSysplCode;
    }

    public void setTxtSysplShtDesc(RichInputText txtSysplShtDesc) {
        this.txtSysplShtDesc = txtSysplShtDesc;
    }

    public RichInputText getTxtSysplShtDesc() {
        return txtSysplShtDesc;
    }

    public void setTxtSysplDesc(RichInputText txtSysplDesc) {
        this.txtSysplDesc = txtSysplDesc;
    }

    public RichInputText getTxtSysplDesc() {
        return txtSysplDesc;
    }

    public void setTxtSysplRanking(RichInputNumberSpinbox txtSysplRanking) {
        this.txtSysplRanking = txtSysplRanking;
    }

    public RichInputNumberSpinbox getTxtSysplRanking() {
        return txtSysplRanking;
    }

    public void setTxtSysplWef(RichInputDate txtSysplWef) {
        this.txtSysplWef = txtSysplWef;
    }

    public RichInputDate getTxtSysplWef() {
        return txtSysplWef;
    }

    public void setBtnSaveUpdateSysPostLevel(RichCommandButton btnSaveUpdateSysPostLevel) {
        this.btnSaveUpdateSysPostLevel = btnSaveUpdateSysPostLevel;
    }

    public RichCommandButton getBtnSaveUpdateSysPostLevel() {
        return btnSaveUpdateSysPostLevel;
    }

    public void setTreeSysPosts(RichTree treeSysPosts) {
        this.treeSysPosts = treeSysPosts;
    }

    public RichTree getTreeSysPosts() {
        return treeSysPosts;
    }

    public void setTxtSpostSysCode(RichInputText txtSpostSysCode) {
        this.txtSpostSysCode = txtSpostSysCode;
    }

    public RichInputText getTxtSpostSysCode() {
        return txtSpostSysCode;
    }

    public void setTxtSpostSysplCode(RichInputText txtSpostSysplCode) {
        this.txtSpostSysplCode = txtSpostSysplCode;
    }

    public RichInputText getTxtSpostSysplCode() {
        return txtSpostSysplCode;
    }

    public void setTxtSpostParentSpostCode(RichInputText txtSpostParentSpostCode) {
        this.txtSpostParentSpostCode = txtSpostParentSpostCode;
    }

    public RichInputText getTxtSpostParentSpostCode() {
        return txtSpostParentSpostCode;
    }

    public void setTxtSpostCode(RichInputText txtSpostCode) {
        this.txtSpostCode = txtSpostCode;
    }

    public RichInputText getTxtSpostCode() {
        return txtSpostCode;
    }

    public void setTxtSpostShtDesc(RichInputText txtSpostShtDesc) {
        this.txtSpostShtDesc = txtSpostShtDesc;
    }

    public RichInputText getTxtSpostShtDesc() {
        return txtSpostShtDesc;
    }

    public void setTxtSpostDesc(RichInputText txtSpostDesc) {
        this.txtSpostDesc = txtSpostDesc;
    }

    public RichInputText getTxtSpostDesc() {
        return txtSpostDesc;
    }

    public void setTxtSpostRemarks(RichInputText txtSpostRemarks) {
        this.txtSpostRemarks = txtSpostRemarks;
    }

    public RichInputText getTxtSpostRemarks() {
        return txtSpostRemarks;
    }

    public void setTxtSpostWef(RichInputDate txtSpostWef) {
        this.txtSpostWef = txtSpostWef;
    }

    public RichInputDate getTxtSpostWef() {
        return txtSpostWef;
    }

    public void setTxtSpostBrnCode(RichInputText txtSpostBrnCode) {
        this.txtSpostBrnCode = txtSpostBrnCode;
    }

    public RichInputText getTxtSpostBrnCode() {
        return txtSpostBrnCode;
    }

    public void setTxtSpostSubdivOsdCode(RichInputText txtSpostSubdivOsdCode) {
        this.txtSpostSubdivOsdCode = txtSpostSubdivOsdCode;
    }

    public RichInputText getTxtSpostSubdivOsdCode() {
        return txtSpostSubdivOsdCode;
    }

    public void setTxtSpostUsrCode(RichInputText txtSpostUsrCode) {
        this.txtSpostUsrCode = txtSpostUsrCode;
    }

    public RichInputText getTxtSpostUsrCode() {
        return txtSpostUsrCode;
    }

    public void setBtnSaveUpdatePost(RichCommandButton btnSaveUpdatePost) {
        this.btnSaveUpdatePost = btnSaveUpdatePost;
    }

    public RichCommandButton getBtnSaveUpdatePost() {
        return btnSaveUpdatePost;
    }

    public void setPanelSysPosts(RichPanelBox panelSysPosts) {
        this.panelSysPosts = panelSysPosts;
    }

    public RichPanelBox getPanelSysPosts() {
        return panelSysPosts;
    }

    public void tblSystemsListener(SelectionEvent selectionEvent) {
        Object key2 = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));
            txtSpostSysCode.setValue(nodeBinding.getAttribute("code"));
            session.removeAttribute("sysplCode");

            ADFUtils.findIterator("fetchSystemPostLevelsIterator").executeQuery();
            GlobalCC.refreshUI(tblSysPostLevels);
            ADFUtils.findIterator("fetchOrgSubDivisionsBySystemIterator").executeQuery();
            GlobalCC.refreshUI(tblSubdivisionsPop);
            GlobalCC.refreshUI(txtSpostSysCode);
        }
    }

    public void clearPostLevelFields() {
        txtSysplSysCode.setValue(session.getAttribute("sysCode"));
        txtSysplCode.setValue(null);
        txtSysplShtDesc.setValue(null);
        txtSysplDesc.setValue(null);
        txtSysplRanking.setValue(null);
        txtSysplWef.setValue(null);

        btnSaveUpdateSysPostLevel.setText("Save");
    }

    public void clearPostsFields() {
        txtSysplSysCode.setValue(session.getAttribute("sysCode"));
        txtSysplCode.setValue(null);
        txtSysplShtDesc.setValue(null);
        txtSysplDesc.setValue(null);
        txtSysplRanking.setValue(null);
        txtSysplWef.setValue(null);

        btnSaveUpdateSysPostLevel.setText("Save");
    }

    public String actionNewPostLevel() {
        if (session.getAttribute("sysCode") != null) {
            clearPostLevelFields();

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:postLevelPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing System to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditPostLevel() {
        Object key2 = tblSysPostLevels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSysplSysCode.setValue(nodeBinding.getAttribute("sysplSysCode"));
            txtSysplCode.setValue(nodeBinding.getAttribute("sysplCode"));
            txtSysplShtDesc.setValue(nodeBinding.getAttribute("sysplShtDesc"));
            txtSysplDesc.setValue(nodeBinding.getAttribute("sysplDesc"));
            txtSysplRanking.setValue(nodeBinding.getAttribute("sysplRanking"));
            txtSysplWef.setValue(nodeBinding.getAttribute("sysplWef"));

            btnSaveUpdateSysPostLevel.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:postLevelPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Record first.");
            return null;
        }
        return null;
    }

    public String actionDeletePostLevel() {
        Object key2 = tblSysPostLevels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String sysCode =
                nodeBinding.getAttribute("sysplSysCode").toString();
            String code = nodeBinding.getAttribute("sysplCode").toString();
            String shtDesc = null;
            String desc = null;
            String ranking = null;
            String wef = null;

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.sysPostLevels_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_SYS_POST_LEVELS_TAB",
                                                     conn);
                ArrayList levelList = new ArrayList();
                SystemPostLevel postLevel = new SystemPostLevel();
                postLevel.setSQLTypeName("TQC_SYS_POST_LEVELS_OBJ");

                postLevel.setSysplSysCode(sysCode == null ? null :
                                          new BigDecimal(sysCode));
                postLevel.setSysplCode(code == null ? null :
                                       new BigDecimal(code));

                levelList.add(postLevel);
                ARRAY array = new ARRAY(descriptor, conn, levelList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                session.removeAttribute("sysplCode");

                ADFUtils.findIterator("fetchSystemPostLevelsIterator").executeQuery();
                GlobalCC.refreshUI(tblSysPostLevels);

                ADFUtils.findIterator("fetchSystemPostsByLevelIterator").executeQuery();
                GlobalCC.refreshUI(treeSysPosts);

                clearPostLevelFields();

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateSysPostLevel() {
        if (btnSaveUpdateSysPostLevel.getText().equals("Edit")) {
            actionUpdateSysPostLevel();
        } else {
            String sysCode =
                GlobalCC.checkNullValues(txtSysplSysCode.getValue());
            String code = GlobalCC.checkNullValues(txtSysplCode.getValue());
            String shtDesc =
                GlobalCC.checkNullValues(txtSysplShtDesc.getValue());
            String desc = GlobalCC.checkNullValues(txtSysplDesc.getValue());
            String ranking =
                GlobalCC.checkNullValues(txtSysplRanking.getValue());
            String wef = GlobalCC.checkNullValues(txtSysplWef.getValue());

            /*if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;*/

            if (sysCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
                return null;

            } else if (desc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
                return null;

            } else if (shtDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
                return null;

            } else if (ranking == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Ranking is Empty");
                return null;

            } else if (wef == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.sysPostLevels_prc(?,?); end;";

                    // Take care of all the date fields on the form.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date tmpWefDate = new Date();
                    if (txtSysplWef.getValue() != null &&
                        !(txtSysplWef.getValue().equals(""))) {
                        String date1 = df.format(txtSysplWef.getValue());
                        tmpWefDate = df.parse(date1);
                    }

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_SYS_POST_LEVELS_TAB",
                                                         conn);
                    ArrayList levelList = new ArrayList();
                    SystemPostLevel postLevel = new SystemPostLevel();
                    postLevel.setSQLTypeName("TQC_SYS_POST_LEVELS_OBJ");

                    postLevel.setSysplSysCode(sysCode == null ? null :
                                              new BigDecimal(sysCode));
                    postLevel.setSysplCode(code == null ? null :
                                           new BigDecimal(code));
                    postLevel.setSysplShtDesc(shtDesc);
                    postLevel.setSysplDesc(desc);
                    postLevel.setSysplRanking(ranking == null ? null :
                                              new BigDecimal(ranking));
                    postLevel.setSysplWef(tmpWefDate == null ? null :
                                          new java.sql.Date(tmpWefDate.getTime()));

                    levelList.add(postLevel);
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
                                         "crm:postLevelPop" +
                                         "').hide(hints);");

                    ADFUtils.findIterator("fetchSystemPostLevelsIterator").executeQuery();
                    GlobalCC.refreshUI(tblSysPostLevels);

                    ADFUtils.findIterator("fetchSystemPostsByLevelIterator").executeQuery();
                    GlobalCC.refreshUI(treeSysPosts);

                    clearPostLevelFields();

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateSysPostLevel() {
        String sysCode = GlobalCC.checkNullValues(txtSysplSysCode.getValue());
        String code = GlobalCC.checkNullValues(txtSysplCode.getValue());
        String shtDesc = GlobalCC.checkNullValues(txtSysplShtDesc.getValue());
        String desc = GlobalCC.checkNullValues(txtSysplDesc.getValue());
        String ranking = GlobalCC.checkNullValues(txtSysplRanking.getValue());
        String wef = GlobalCC.checkNullValues(txtSysplWef.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (sysCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
            return null;

        } else if (desc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
            return null;

        } else if (shtDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
            return null;

        } else if (ranking == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Ranking is Empty");
            return null;

        } else if (wef == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.sysPostLevels_prc(?,?); end;";

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpWefDate = new Date();
                if (txtSysplWef.getValue() != null &&
                    !(txtSysplWef.getValue().equals(""))) {
                    String date1 = df.format(txtSysplWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_SYS_POST_LEVELS_TAB",
                                                     conn);
                ArrayList levelList = new ArrayList();
                SystemPostLevel postLevel = new SystemPostLevel();
                postLevel.setSQLTypeName("TQC_SYS_POST_LEVELS_OBJ");

                postLevel.setSysplSysCode(sysCode == null ? null :
                                          new BigDecimal(sysCode));
                postLevel.setSysplCode(code == null ? null :
                                       new BigDecimal(code));
                postLevel.setSysplShtDesc(shtDesc);
                postLevel.setSysplDesc(desc);
                postLevel.setSysplRanking(ranking == null ? null :
                                          new BigDecimal(ranking));
                postLevel.setSysplWef(tmpWefDate == null ? null :
                                      new java.sql.Date(tmpWefDate.getTime()));

                levelList.add(postLevel);
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
                                     "crm:postLevelPop" + "').hide(hints);");

                ADFUtils.findIterator("fetchSystemPostLevelsIterator").executeQuery();
                GlobalCC.refreshUI(tblSysPostLevels);

                ADFUtils.findIterator("fetchSystemPostsByLevelIterator").executeQuery();
                GlobalCC.refreshUI(treeSysPosts);

                clearPostLevelFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void tblSysPostLevelsListener(SelectionEvent selectionEvent) {
        Object key2 = tblSysPostLevels.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysplCode",
                                 nodeBinding.getAttribute("sysplCode"));
            txtSpostSysplCode.setValue(nodeBinding.getAttribute("sysplCode"));

            ADFUtils.findIterator("fetchSystemPostsByLevelIterator").executeQuery();
            GlobalCC.refreshUI(treeSysPosts);
            GlobalCC.refreshUI(txtSpostSysplCode);
        }
    }

    public void treeSysPostsListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeSysPosts.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)treeSysPosts.getRowData();

                    session.setAttribute("spostCode",
                                         nodeBinding.getRow().getAttribute("spostCode"));

                    txtSpostSysCode.setValue(nodeBinding.getRow().getAttribute("spostSysCode"));
                    txtSpostSysplCode.setValue(nodeBinding.getRow().getAttribute("spostSysplCode"));
                    txtSpostParentSpostCode.setValue(nodeBinding.getRow().getAttribute("spostParentSpostCode"));
                    txtSpostCode.setValue(nodeBinding.getRow().getAttribute("spostCode"));
                    txtSpostShtDesc.setValue(nodeBinding.getRow().getAttribute("spostShtDesc"));
                    txtSpostDesc.setValue(nodeBinding.getRow().getAttribute("spostDesc"));
                    txtSpostRemarks.setValue(nodeBinding.getRow().getAttribute("spostRemarks"));
                    txtSpostWef.setValue(nodeBinding.getRow().getAttribute("spostWef"));
                    txtSpostBrnCode.setValue(nodeBinding.getRow().getAttribute("spostBrnCode"));
                    txtSpostSubdivOsdCode.setValue(nodeBinding.getRow().getAttribute("spostSubdivOsdCode"));
                    txtSpostSubdivOsdName.setValue(nodeBinding.getRow().getAttribute("spostSubdivOsdCode"));
                    txtSpostUsrCode.setValue(nodeBinding.getRow().getAttribute("spostUsrCode"));

                    btnSaveUpdatePost.setText("Update");


                    GlobalCC.refreshUI(panelSysPosts);
                } // End for
            } // End if
        } // End if
    }

    public void clearSysPostFields() {
        txtSpostSysCode.setValue(session.getAttribute("sysCode"));
        txtSpostSysplCode.setValue(session.getAttribute("sysplCode"));
        txtSpostParentSpostCode.setValue(null);
        txtSpostCode.setValue(null);
        txtSpostShtDesc.setValue(null);
        txtSpostDesc.setValue(null);
        txtSpostRemarks.setValue(null);
        txtSpostWef.setValue(null);
        txtSpostBrnCode.setValue(null);
        txtSpostSubdivOsdCode.setValue(null);
        txtSpostSubdivOsdName.setValue(null);
        txtSpostUsrCode.setValue(session.getAttribute("UserCode"));

        btnSaveUpdatePost.setText("Save");
    }

    public String actionNewPost() {
        if (session.getAttribute("sysplCode") != null) {
            clearSysPostFields();
            GlobalCC.refreshUI(panelSysPosts);
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Post Level to proceed.");
            return null;
        }
        return null;
    }

    public String actionDeletePost() {
        if (session.getAttribute("spostCode") != null) {
            String sysCode =
                GlobalCC.checkNullValues(txtSpostSysCode.getValue());
            String sysplCode =
                GlobalCC.checkNullValues(txtSpostSysplCode.getValue());
            String parentCode =
                GlobalCC.checkNullValues(txtSpostParentSpostCode.getValue());
            String code = GlobalCC.checkNullValues(txtSpostCode.getValue());
            String shtDesc =
                GlobalCC.checkNullValues(txtSpostShtDesc.getValue());
            String desc = GlobalCC.checkNullValues(txtSpostDesc.getValue());
            String remarks =
                GlobalCC.checkNullValues(txtSpostRemarks.getValue());
            String wef = GlobalCC.checkNullValues(txtSpostWef.getValue());
            String brnCode =
                GlobalCC.checkNullValues(txtSpostBrnCode.getValue());
            String subdivOsdCode =
                GlobalCC.checkNullValues(txtSpostSubdivOsdCode.getValue());
            String usrCode =
                GlobalCC.checkNullValues(txtSpostUsrCode.getValue());

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query = "begin TQC_SETUPS_PKG.sysPosts_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_SYS_POSTS_TAB",
                                                     conn);
                ArrayList postsList = new ArrayList();
                SystemPost sysPost = new SystemPost();
                sysPost.setSQLTypeName("TQC_SYS_POSTS_OBJ");

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpWefDate = new Date();
                if (txtSpostWef.getValue() != null &&
                    !(txtSpostWef.getValue().equals(""))) {
                    String date1 = df.format(txtSpostWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                sysPost.setSpostSysCode(sysCode == null ? null :
                                        new BigDecimal(sysCode));
                sysPost.setSpostSysplCode(sysplCode == null ? null :
                                          new BigDecimal(sysplCode));
                sysPost.setSpostParentSpostCode(parentCode == null ? null :
                                                new BigDecimal(parentCode));
                sysPost.setSpostCode(code == null ? null :
                                     new BigDecimal(code));
                sysPost.setSpostShtDesc(shtDesc);
                sysPost.setSpostDesc(desc);
                sysPost.setSpostRemarks(remarks);
                sysPost.setSpostWef(tmpWefDate == null ? null :
                                    new java.sql.Date(tmpWefDate.getTime()));
                sysPost.setSpostBrnCode(brnCode == null ? null :
                                        new BigDecimal(brnCode));
                sysPost.setSpostSubdivOsdCode(subdivOsdCode);
                sysPost.setSpostUsrCode(usrCode == null ? null :
                                        new BigDecimal(usrCode));

                postsList.add(sysPost);
                ARRAY array = new ARRAY(descriptor, conn, postsList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchSystemPostsByLevelIterator").executeQuery();
                GlobalCC.refreshUI(treeSysPosts);

                session.setAttribute("spostCode", null);

                clearSysPostFields();

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

    public String actionSaveUpdatePost() {
        if (btnSaveUpdatePost.getText().equals("Update")) {
            actionUpdatePost();
        } else {
            String sysCode =
                GlobalCC.checkNullValues(txtSpostSysCode.getValue());
            String sysplCode =
                GlobalCC.checkNullValues(txtSpostSysplCode.getValue());
            String parentCode =
                GlobalCC.checkNullValues(txtSpostParentSpostCode.getValue());
            String code = GlobalCC.checkNullValues(txtSpostCode.getValue());
            String shtDesc =
                GlobalCC.checkNullValues(txtSpostShtDesc.getValue());
            String desc = GlobalCC.checkNullValues(txtSpostDesc.getValue());
            String remarks =
                GlobalCC.checkNullValues(txtSpostRemarks.getValue());
            String wef = GlobalCC.checkNullValues(txtSpostWef.getValue());
            String brnCode =
                GlobalCC.checkNullValues(txtSpostBrnCode.getValue());
            String subdivOsdCode =
                GlobalCC.checkNullValues(txtSpostSubdivOsdCode.getValue());
            String usrCode =
                GlobalCC.checkNullValues(txtSpostUsrCode.getValue());

            if (sysCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
                return null;

            } else if (sysplCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Post Level Code is Empty");
                return null;

                /*} else if (code == null) {
          GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
          return null;*/

            } else if (shtDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
                return null;

            } else if (desc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
                return null;

            } else if (remarks == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Remarks is Empty");
                return null;

            } else if (wef == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.sysPosts_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_SYS_POSTS_TAB",
                                                         conn);
                    ArrayList postsList = new ArrayList();
                    SystemPost sysPost = new SystemPost();
                    sysPost.setSQLTypeName("TQC_SYS_POSTS_OBJ");

                    // Take care of all the date fields on the form.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date tmpWefDate = new Date();
                    if (txtSpostWef.getValue() != null &&
                        !(txtSpostWef.getValue().equals(""))) {
                        String date1 = df.format(txtSpostWef.getValue());
                        tmpWefDate = df.parse(date1);
                    }

                    sysPost.setSpostSysCode(sysCode == null ? null :
                                            new BigDecimal(sysCode));
                    sysPost.setSpostSysplCode(sysplCode == null ? null :
                                              new BigDecimal(sysplCode));
                    sysPost.setSpostParentSpostCode(parentCode == null ? null :
                                                    new BigDecimal(parentCode));
                    sysPost.setSpostCode(code == null ? null :
                                         new BigDecimal(code));
                    sysPost.setSpostShtDesc(shtDesc);
                    sysPost.setSpostDesc(desc);
                    sysPost.setSpostRemarks(remarks);
                    sysPost.setSpostWef(tmpWefDate == null ? null :
                                        new java.sql.Date(tmpWefDate.getTime()));
                    sysPost.setSpostBrnCode(brnCode == null ? null :
                                            new BigDecimal(brnCode));
                    sysPost.setSpostSubdivOsdCode(subdivOsdCode);
                    sysPost.setSpostUsrCode(usrCode == null ? null :
                                            new BigDecimal(usrCode));

                    postsList.add(sysPost);
                    ARRAY array =
                        new ARRAY(descriptor, conn, postsList.toArray());

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "A");
                    statement.setArray(2, array);
                    statement.execute();

                    statement.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchSystemPostsByLevelIterator").executeQuery();
                    GlobalCC.refreshUI(treeSysPosts);

                    clearSysPostFields();

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String actionUpdatePost() {
        String sysCode = GlobalCC.checkNullValues(txtSpostSysCode.getValue());
        String sysplCode =
            GlobalCC.checkNullValues(txtSpostSysplCode.getValue());
        String parentCode =
            GlobalCC.checkNullValues(txtSpostParentSpostCode.getValue());
        String code = GlobalCC.checkNullValues(txtSpostCode.getValue());
        String shtDesc = GlobalCC.checkNullValues(txtSpostShtDesc.getValue());
        String desc = GlobalCC.checkNullValues(txtSpostDesc.getValue());
        String remarks = GlobalCC.checkNullValues(txtSpostRemarks.getValue());
        String wef = GlobalCC.checkNullValues(txtSpostWef.getValue());
        String brnCode = GlobalCC.checkNullValues(txtSpostBrnCode.getValue());
        String subdivOsdCode =
            GlobalCC.checkNullValues(txtSpostSubdivOsdCode.getValue());
        String usrCode = GlobalCC.checkNullValues(txtSpostUsrCode.getValue());

        if (sysCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
            return null;

        } else if (sysplCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Post Level Code is Empty");
            return null;

        } else if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (shtDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
            return null;

        } else if (desc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
            return null;

        } else if (remarks == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Remarks is Empty");
            return null;

        } else if (wef == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WEF Date is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query = "begin TQC_SETUPS_PKG.sysPosts_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_SYS_POSTS_TAB",
                                                     conn);
                ArrayList postsList = new ArrayList();
                SystemPost sysPost = new SystemPost();
                sysPost.setSQLTypeName("TQC_SYS_POSTS_OBJ");

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpWefDate = new Date();
                if (txtSpostWef.getValue() != null &&
                    !(txtSpostWef.getValue().equals(""))) {
                    String date1 = df.format(txtSpostWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                sysPost.setSpostSysCode(sysCode == null ? null :
                                        new BigDecimal(sysCode));
                sysPost.setSpostSysplCode(sysplCode == null ? null :
                                          new BigDecimal(sysplCode));
                sysPost.setSpostParentSpostCode(parentCode == null ? null :
                                                new BigDecimal(parentCode));
                sysPost.setSpostCode(code == null ? null :
                                     new BigDecimal(code));
                sysPost.setSpostShtDesc(shtDesc);
                sysPost.setSpostDesc(desc);
                sysPost.setSpostRemarks(remarks);
                sysPost.setSpostWef(tmpWefDate == null ? null :
                                    new java.sql.Date(tmpWefDate.getTime()));
                sysPost.setSpostBrnCode(brnCode == null ? null :
                                        new BigDecimal(brnCode));
                sysPost.setSpostSubdivOsdCode(subdivOsdCode);
                sysPost.setSpostUsrCode(usrCode == null ? null :
                                        new BigDecimal(usrCode));

                postsList.add(sysPost);
                ARRAY array = new ARRAY(descriptor, conn, postsList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchSystemPostsByLevelIterator").executeQuery();
                GlobalCC.refreshUI(treeSysPosts);

                clearSysPostFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void setTxtSpostSubdivOsdName(RichInputText txtSpostSubdivOsdName) {
        this.txtSpostSubdivOsdName = txtSpostSubdivOsdName;
    }

    public RichInputText getTxtSpostSubdivOsdName() {
        return txtSpostSubdivOsdName;
    }

    public String actionShowSubDivisionsLov() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:orgSubdivisionsPop" + "').show(hints);");
        return null;
    }

    public void setTblSubdivisionsPop(RichTable tblSubdivisionsPop) {
        this.tblSubdivisionsPop = tblSubdivisionsPop;
    }

    public RichTable getTblSubdivisionsPop() {
        return tblSubdivisionsPop;
    }

    public String actionAcceptSubDivision() {
        Object key2 = tblSubdivisionsPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSpostSubdivOsdCode.setValue(nodeBinding.getAttribute("osdCode"));
            txtSpostSubdivOsdName.setValue(nodeBinding.getAttribute("osdName"));
        }

        GlobalCC.refreshUI(panelSysPosts);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:orgSubdivisionsPop" + "').hide(hints);");
        return null;
    }

    public void setTblAgencyBranch(RichTable tblAgencyBranch) {
        this.tblAgencyBranch = tblAgencyBranch;
    }

    public RichTable getTblAgencyBranch() {
        return tblAgencyBranch;
    }

    public String actionAcceptAgencyBranch() {
        Object key2 = tblAgencyBranch.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSpostBrnCode.setValue(nodeBinding.getAttribute("id"));
            GlobalCC.refreshUI(txtSpostBrnCode);
            //txtAgencyBranchName.setValue(nodeBinding.getAttribute("name"));
        }
        GlobalCC.dismissPopUp("crm", "agencyBranchPop");
        return null;
    }

    public void populateBankBranches(SelectionEvent selectionEvent) {
        Object key2 = tblbranches.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("brn_name",
                                 nodeBinding.getAttribute("brn_name"));
            session.setAttribute("brn_reg_code",
                                 nodeBinding.getAttribute("brn_reg_code"));
            ADFUtils.findIterator("fetchBankBranchesIterator").executeQuery();
            GlobalCC.refreshUI(tblBranch);
        }
    }

    public void setTblbranches(RichTable tblbranches) {
        this.tblbranches = tblbranches;
    }

    public RichTable getTblbranches() {
        return tblbranches;
    }

    public void setTblBranch(RichTable tblBranch) {
        this.tblBranch = tblBranch;
    }

    public RichTable getTblBranch() {
        return tblBranch;
    }

    public void POPULATE(SelectionEvent selectionEvent) {
        // Add event code here...
    }

    public void populateBankBranchestbl(SelectionEvent selectionEvent) {
        Object key2 = tblBranch.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("BBR_CODE",
                                 nodeBinding.getAttribute("BBR_CODE"));
            session.setAttribute("bbr_bnk_code",
                                 nodeBinding.getAttribute("BBR_BNK_CODE"));
            ADFUtils.findIterator("fetchBankBranchDetailsIterator").executeQuery();
            GlobalCC.refreshUI(branchtb);
        }
    }

    public void setBranchtbl(RichTable branchtbl) {
        this.branchtbl = branchtbl;
    }

    public RichTable getBranchtbl() {
        return branchtbl;
    }

    public String addBranchesDetails() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:addBranchBankDetails" + "').show(hints);");
        session.setAttribute("Action", "A");
        return null;
    }

    public String editbrnDetails() {
        Object key2 = branchtb.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding == null) {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Record first.");
            return null;
        }
        GlobalCC.showPopup("crm:addBranchBankDetails");
        session.setAttribute("Action", "E");
        //
        //brnCode.setValue(null);
        //brnDesc.setValue(null);
        //brnDesc.setValue("");
       // brnBankCode.setValue("");
       // brnBankdesc.setValue("");
        
        brnBankCode.setValue(nodeBinding.getAttribute("BBB_BRN_NAME"));
        txtBranchesCode.setValue(nodeBinding.getAttribute("BBB_BBR_BRANCH_NAME"));
        
        GlobalCC.refreshUI(txtBranchesCode);
        GlobalCC.refreshUI(brnBankCode);
        return null;
    }

    public void setBrnCode(RichInputText brnCode) {
        this.brnCode = brnCode;
    }

    public RichInputText getBrnCode() {
        return brnCode;
    }

    public void setBrnDesc(RichInputText brnDesc) {
        this.brnDesc = brnDesc;
    }

    public RichInputText getBrnDesc() {
        return brnDesc;
    }

    public void setBrnReg(RichInputText brnReg) {
        this.brnReg = brnReg;
    }

    public RichInputText getBrnReg() {
        return brnReg;
    }

    public void setBrnname(RichInputText brnname) {
        this.brnname = brnname;
    }

    public RichInputText getBrnname() {
        return brnname;
    }

    public void setBrnAddress(RichInputText brnAddress) {
        this.brnAddress = brnAddress;
    }

    public RichInputText getBrnAddress() {
        return brnAddress;
    }

    public void setBrnBankCode(RichInputText brnBankCode) {
        this.brnBankCode = brnBankCode;
    }

    public RichInputText getBrnBankCode() {
        return brnBankCode;
    }

    public void setBrnBankdesc(RichInputText brnBankdesc) {
        this.brnBankdesc = brnBankdesc;
    }

    public RichInputText getBrnBankdesc() {
        return brnBankdesc;
    }

    public void setBtnSetBnkCode(RichCommandButton btnSetBnkCode) {
        this.btnSetBnkCode = btnSetBnkCode;
    }

    public RichCommandButton getBtnSetBnkCode() {
        return btnSetBnkCode;
    }

    public String actionsetCode() {
        GlobalCC globalCC = new GlobalCC();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:showAllBranches" + "').hide(hints);");
        System.out.println("This is the Branch Code" +
                          globalCC.checkNullValues(session.getAttribute("BranchName")) );
        String branchCode = globalCC.checkNullValues(session.getAttribute("BranchName"));
        txtBranchesCode.setValue(branchCode);
        GlobalCC.refreshUI(txtBranchesCode);
        return null;
    }

    public void selectBranch(SelectionEvent selectionEvent) {

        Object key2 = tblBrn.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if ((nodeBinding != null)) {
            session.setAttribute("BranchName",
                                 nodeBinding.getAttribute("brn_name"));
            session.setAttribute("BranchCode",
                                 nodeBinding.getAttribute("brn_code"));
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Record first.");
        }
    }

    public void setTblBranches(RichTable tblBranches) {
        this.tblBranches = tblBranches;
    }

    public RichTable getTblBranches() {
        return tblBranches;
    }

    public void setTblBrn(RichTable tblBrn) {
        this.tblBrn = tblBrn;
    }

    public RichTable getTblBrn() {
        return tblBrn;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setBranchtb(RichTable branchtb) {
        this.branchtb = branchtb;
    }

    public RichTable getBranchtb() {
        return branchtb;
    }

    public void setTblBran(RichTable tblBran) {
        this.tblBran = tblBran;
    }

    public RichTable getTblBran() {
        return tblBran;
    }

    public void setTblbankBranches(RichTable tblbankBranches) {
        this.tblbankBranches = tblbankBranches;
    }

    public RichTable getTblbankBranches() {
        return tblbankBranches;
    }

    public void selectbankBranch(SelectionEvent selectionEvent) {
        Object key2 = tblBran.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("BankBranchname",
                                 nodeBinding.getAttribute("v_bbr_branch_name"));
            session.setAttribute("BankBranchCode",
                                 nodeBinding.getAttribute("v_bbr_code"));
            session.setAttribute("BankCode",
                                 nodeBinding.getAttribute("v_bbr_bnk_code"));
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Record first.");
        }
        System.out.println("This is Ok" +
                           session.getAttribute("BankBranchCode"));
    }

    public String saveBranchDetails() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:showbankBranches" + "').hide(hints);");
        String bankBranchCode =
            session.getAttribute("BankBranchname").toString();
        brnBankCode.setValue(bankBranchCode);
        GlobalCC.refreshUI(brnBankCode);
        return null;
    }

    public void setBrnchCode(RichInputText brnchCode) {
        this.brnchCode = brnchCode;
    }

    public RichInputText getBrnchCode() {
        return brnchCode;
    }

    public void setTxtBranchesCode(RichInputText txtBranchesCode) {
        this.txtBranchesCode = txtBranchesCode;
    }

    public RichInputText getTxtBranchesCode() {
        return txtBranchesCode;
    }

    public String saveBankDetails() throws SQLException {
        GlobalCC globalCC = new GlobalCC();
        BigDecimal BranchCode =GlobalCC.checkBDNullValues(session.getAttribute("BranchCode").toString());
        BigDecimal bankBranchCode =GlobalCC.checkBDNullValues(session.getAttribute("BankBranchCode").toString());
        
        BigDecimal bbr_bnk_code =GlobalCC.checkBDNullValues(session.getAttribute("bbr_bnk_code"));
        String branchRegCode =( GlobalCC.checkNullValues(session.getAttribute("brn_reg_code")) );
           
        BigDecimal bbrCode =GlobalCC.checkBDNullValues(session.getAttribute("BBR_CODE"));
           
        String BranchName =GlobalCC.checkNullValues(session.getAttribute("BankBranchname"));
        if (bbrCode == null) {

        }
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        conn = datahandler.getDatabaseConnection();
        CallableStatement stmt = null;
        String query =
            "begin tqc_setups_pkg.bank_branch_details(?,?,?,?,?,?,?);end;";
        try {
            stmt = conn.prepareCall(query);
            stmt.setString(1, (String)session.getAttribute("Action"));
            System.out.println("Branches" + bbrCode);
            stmt.setBigDecimal(2, bankBranchCode);
            stmt.setBigDecimal(3, BranchCode);
            stmt.setString(4, branchRegCode);
            stmt.setString(5, BranchName);
            stmt.setBigDecimal(6, bbrCode);
            stmt.setBigDecimal(7, bbr_bnk_code);
            stmt.execute();
            ADFUtils.findIterator("fetchBankBranchDetailsIterator").executeQuery();
            GlobalCC.refreshUI(branchtb);
        } catch (Exception e) {
            GlobalCC.INFORMATIONREPORTING("Error" + e);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:addBranchBankDetails" + "').hide(hints);");
        return null;
    }

    public String deleteBrnDetails() {
        // Add event code here...
        Object key2 = branchtb.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n == null) {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Record first.");
            return null;
        }
        BigDecimal bbbCode=GlobalCC.checkBDNullValues(n.getAttribute("BBB_CODE"));
        if(bbbCode!=null) {
                String sql="delete from tqc_bank_branches_branches where bbb_code= "+bbbCode.toString()+" ";
              boolean success = IQuery.exec(sql);
            ADFUtils.findIterator("fetchBankBranchDetailsIterator").executeQuery();
              GlobalCC.refreshUI(branchtb);
               GlobalCC.INFORMATIONREPORTING("Successfully Deleted!"); 
            
        } 
        return null;
    }
}
