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
import TurnQuest.view.models.ActivityType;

import java.math.BigDecimal;

import java.util.ArrayList;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ActivityTypeBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblSystems;
    private RichTable tblActivityType;
    private RichInputText txtActyCode;
    private RichInputText txtActySysCode;
    private RichInputText txtActyDesc;
    private RichCommandButton btnSaveUpdateActivityType;
    private RichInputText txtActTypeCode;
    private RichCommandButton btnSaveEditParticipants;

    public ActivityTypeBacking() {
    }

    public void setTblSystems(RichTable tblSystems) {
        this.tblSystems = tblSystems;
    }

    public RichTable getTblSystems() {
        return tblSystems;
    }

    public void setTblActivityType(RichTable tblActivityType) {
        this.tblActivityType = tblActivityType;
    }

    public RichTable getTblActivityType() {
        return tblActivityType;
    }

    public void setTxtActyCode(RichInputText txtActyCode) {
        this.txtActyCode = txtActyCode;
    }

    public RichInputText getTxtActyCode() {
        return txtActyCode;
    }

    public void setTxtActySysCode(RichInputText txtActySysCode) {
        this.txtActySysCode = txtActySysCode;
    }

    public RichInputText getTxtActySysCode() {
        return txtActySysCode;
    }

    public void setTxtActyDesc(RichInputText txtActyDesc) {
        this.txtActyDesc = txtActyDesc;
    }

    public RichInputText getTxtActyDesc() {
        return txtActyDesc;
    }

    public void setBtnSaveUpdateActivityType(RichCommandButton btnSaveUpdateActivityType) {
        this.btnSaveUpdateActivityType = btnSaveUpdateActivityType;
    }

    public RichCommandButton getBtnSaveUpdateActivityType() {
        return btnSaveUpdateActivityType;
    }

    public void tblSystemsListener(SelectionEvent selectionEvent) {
        Object key2 = tblSystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode", nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("fetchActivityTypeBySystemIterator").executeQuery();
            GlobalCC.refreshUI(tblActivityType);
        }
    }

    public void clearActivityTypeFields() {
        txtActyCode.setValue(null);
        txtActySysCode.setValue(session.getAttribute("sysCode"));
        txtActyDesc.setValue(null);

        btnSaveUpdateActivityType.setText("Save");
    }

    public String actionNewActivityType() {
        if (session.getAttribute("sysCode") != null) {
            clearActivityTypeFields();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:activityTypePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing System Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditActivityType() {
        Object key2 = tblActivityType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtActyCode.setValue(nodeBinding.getAttribute("actyCode"));
            txtActySysCode.setValue(nodeBinding.getAttribute("actySysCode"));
            txtActyDesc.setValue(nodeBinding.getAttribute("actyDesc"));

            btnSaveUpdateActivityType.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:activityTypePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteActivityType() {
        Object key2 = tblActivityType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("actyCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.activityTypes_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ACTIVITY_TYPES_TAB",
                                                     conn);
                ArrayList activityList = new ArrayList();
                ActivityType activityType = new ActivityType();
                activityType.setSQLTypeName("TQC_ACTIVITY_TYPES_OBJ");

                activityType.setActyCode(new BigDecimal(code));

                activityList.add(activityType);
                ARRAY array =
                    new ARRAY(descriptor, conn, activityList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchActivityTypeBySystemIterator").executeQuery();
                GlobalCC.refreshUI(tblActivityType);

                clearActivityTypeFields();

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionSaveUpdateActivityType() {
        if (btnSaveUpdateActivityType.getText().equals("Edit")) {
            actionUpdateActivityType();
        } else {
            String code = GlobalCC.checkNullValues(txtActyCode.getValue());
            String sysCode =
                GlobalCC.checkNullValues(txtActySysCode.getValue());
            String desc = GlobalCC.checkNullValues(txtActyDesc.getValue());
            int status = 0;
            if (sysCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
                return null;

            } else if (desc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleConnection connection = null;
                OracleCallableStatement statement = null;
                OracleCallableStatement stmt = null;
                OracleResultSet rs = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    connection =
                            (OracleConnection)dbConnector.getDatabaseConnection();
                    String query1 =
                        "begin ? := TQC_SETUPS_CURSOR.check_activity_exist(?); end;";
                    stmt =
(OracleCallableStatement)connection.prepareCall(query1);
                    stmt.registerOutParameter(1, OracleTypes.CURSOR);
                    stmt.setString(2, desc);

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
                        GlobalCC.INFORMATIONREPORTING("Duplicate Activity Types not allowed");
                        return null;

                    } else {
                        String query =
                            "begin TQC_SETUPS_PKG.activityTypes_prc(?,?); end;";

                        ArrayDescriptor descriptor =
                            ArrayDescriptor.createDescriptor("TQC_ACTIVITY_TYPES_TAB",
                                                             conn);
                        ArrayList activityList = new ArrayList();
                        ActivityType activityType = new ActivityType();
                        activityType.setSQLTypeName("TQC_ACTIVITY_TYPES_OBJ");

                        activityType.setActyCode(code == null ? null :
                                                 new BigDecimal(code));
                        activityType.setActySysCode(sysCode == null ? null :
                                                    new BigDecimal(sysCode));
                        activityType.setActyDesc(desc);

                        activityList.add(activityType);
                        ARRAY array =
                            new ARRAY(descriptor, conn, activityList.toArray());

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
                                             "crm:activityTypePop" +
                                             "').hide(hints);");

                        ADFUtils.findIterator("fetchActivityTypeBySystemIterator").executeQuery();
                        GlobalCC.refreshUI(tblActivityType);

                        clearActivityTypeFields();

                        String message = "New Record ADDED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                    }
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String actionUpdateActivityType() {
        String code = GlobalCC.checkNullValues(txtActyCode.getValue());
        String sysCode = GlobalCC.checkNullValues(txtActySysCode.getValue());
        String desc = GlobalCC.checkNullValues(txtActyDesc.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (sysCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
            return null;

        } else if (desc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.activityTypes_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_ACTIVITY_TYPES_TAB",
                                                     conn);
                ArrayList activityList = new ArrayList();
                ActivityType activityType = new ActivityType();
                activityType.setSQLTypeName("TQC_ACTIVITY_TYPES_OBJ");

                activityType.setActyCode(code == null ? null :
                                         new BigDecimal(code));
                activityType.setActySysCode(sysCode == null ? null :
                                            new BigDecimal(sysCode));
                activityType.setActyDesc(desc);

                activityList.add(activityType);
                ARRAY array =
                    new ARRAY(descriptor, conn, activityList.toArray());

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
                                     "crm:activityTypePop" +
                                     "').hide(hints);");

                ADFUtils.findIterator("fetchActivityTypeBySystemIterator").executeQuery();
                GlobalCC.refreshUI(tblActivityType);

                clearActivityTypeFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void setTxtActTypeCode(RichInputText txtActTypeCode) {
        this.txtActTypeCode = txtActTypeCode;
    }

    public RichInputText getTxtActTypeCode() {
        return txtActTypeCode;
    }

    public void setBtnSaveEditParticipants(RichCommandButton btnSaveEditParticipants) {
        this.btnSaveEditParticipants = btnSaveEditParticipants;
    }

    public RichCommandButton getBtnSaveEditParticipants() {
        return btnSaveEditParticipants;
    }
}
