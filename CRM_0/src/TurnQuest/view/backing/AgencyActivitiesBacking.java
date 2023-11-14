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
import TurnQuest.view.models.AgencyActivity;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectItem;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import oracle.sql.ArrayDescriptor;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class AgencyActivitiesBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private RichTable tblAgencies;
    private RichTable tblAgencyActivity;
    private RichInputText txtAacCode;
    private RichInputText txtAacActyCode;
    private RichInputText txtAacActyDesc;
    private RichInputDate txtAacWef;
    private RichInputDate txtAacEstimateWet;
    private RichInputDate txtAacActualWet;
    private RichInputText txtAacRemarks;
    private RichInputText txtAacAgnCode;
    private RichInputText txtAacClientCode;
    private RichInputText txtAacClientName;
    private RichInputText txtAacSprCode;
    private RichInputText txtAacServiceProviderName;
    private RichInputText txtAacSysCode;
    private RichInputText txtAacSystemName;
    private RichInputText txtAacMktrAgnCode;
    private RichInputText txtAacMarketerName;
    private RichCommandButton btnSaveUpdateAgencyActivity;
    private RichTable tblActivityTypesPop;
    private RichTable tblMarketerPop;
    private RichPanelBox panelActivityTypeDetail;
    private RichTable tblSystemsPop;
    private RichTable tblClientsPop;
    private RichTable tblServiceProvidersPop;
    private RichSelectOneChoice socActByType;
    private RichInputText txtAaActTypeCode;
    private RichInputText txtAacReasonsForAct;
    private RichInputText txtActivityByCode;
    private RichSelectOneChoice socSelectTypeParticip;
    private RichInputText txtPartCode;
    private RichInputText txtPartActivityTypeCode;
    private RichInputText txtParticipActivityCode;
    private RichInputText txtParticipByCode;
    private RichCommandButton btnSaveEditParticipants;
    private RichTable tblParticipants;
    private RichInputText txtParticipId;
    private RichSelectItem selectStatusInactive;
    private RichSelectItem selectStatusDraft;
    private RichSelectItem selectStatusActive;
    private RichTable tblAgenciesByShortDesc;
    private RichInputText txtParticipByName;
    private RichInputText txtActivityByName;
    private RichOutputLabel confirmDeleteActivity;
    private RichOutputLabel connfirmDeleteActivity;
    private RichDialog confirmDeleteParticipants;

    public AgencyActivitiesBacking() {
    }

    public void setTblAgencies(RichTable tblAgencies) {
        this.tblAgencies = tblAgencies;
    }

    public RichTable getTblAgencies() {
        return tblAgencies;
    }

    public void setTblAgencyActivity(RichTable tblAgencyActivity) {
        this.tblAgencyActivity = tblAgencyActivity;
    }

    public RichTable getTblAgencyActivity() {
        return tblAgencyActivity;
    }

    public void setTxtAacCode(RichInputText txtAacCode) {
        this.txtAacCode = txtAacCode;
    }

    public RichInputText getTxtAacCode() {
        return txtAacCode;
    }

    public void setTxtAacActyCode(RichInputText txtAacActyCode) {
        this.txtAacActyCode = txtAacActyCode;
    }

    public RichInputText getTxtAacActyCode() {
        return txtAacActyCode;
    }

    public void setTxtAacActyDesc(RichInputText txtAacActyDesc) {
        this.txtAacActyDesc = txtAacActyDesc;
    }

    public RichInputText getTxtAacActyDesc() {
        return txtAacActyDesc;
    }

    public void setTxtAacWef(RichInputDate txtAacWef) {
        this.txtAacWef = txtAacWef;
    }

    public RichInputDate getTxtAacWef() {
        return txtAacWef;
    }

    public void setTxtAacEstimateWet(RichInputDate txtAacEstimateWet) {
        this.txtAacEstimateWet = txtAacEstimateWet;
    }

    public RichInputDate getTxtAacEstimateWet() {
        return txtAacEstimateWet;
    }

    public void setTxtAacActualWet(RichInputDate txtAacActualWet) {
        this.txtAacActualWet = txtAacActualWet;
    }

    public RichInputDate getTxtAacActualWet() {
        return txtAacActualWet;
    }

    public void setTxtAacRemarks(RichInputText txtAacRemarks) {
        this.txtAacRemarks = txtAacRemarks;
    }

    public RichInputText getTxtAacRemarks() {
        return txtAacRemarks;
    }

    public void setTxtAacAgnCode(RichInputText txtAacAgnCode) {
        this.txtAacAgnCode = txtAacAgnCode;
    }

    public RichInputText getTxtAacAgnCode() {
        return txtAacAgnCode;
    }

    public void setTxtAacClientCode(RichInputText txtAacClientCode) {
        this.txtAacClientCode = txtAacClientCode;
    }

    public RichInputText getTxtAacClientCode() {
        return txtAacClientCode;
    }

    public void setTxtAacClientName(RichInputText txtAacClientName) {
        this.txtAacClientName = txtAacClientName;
    }

    public RichInputText getTxtAacClientName() {
        return txtAacClientName;
    }

    public void setTxtAacSprCode(RichInputText txtAacSprCode) {
        this.txtAacSprCode = txtAacSprCode;
    }

    public RichInputText getTxtAacSprCode() {
        return txtAacSprCode;
    }

    public void setTxtAacServiceProviderName(RichInputText txtAacServiceProviderName) {
        this.txtAacServiceProviderName = txtAacServiceProviderName;
    }

    public RichInputText getTxtAacServiceProviderName() {
        return txtAacServiceProviderName;
    }

    public void setTxtAacSysCode(RichInputText txtAacSysCode) {
        this.txtAacSysCode = txtAacSysCode;
    }

    public RichInputText getTxtAacSysCode() {
        return txtAacSysCode;
    }

    public void setTxtAacSystemName(RichInputText txtAacSystemName) {
        this.txtAacSystemName = txtAacSystemName;
    }

    public RichInputText getTxtAacSystemName() {
        return txtAacSystemName;
    }

    public void setTxtAacMktrAgnCode(RichInputText txtAacMktrAgnCode) {
        this.txtAacMktrAgnCode = txtAacMktrAgnCode;
    }

    public RichInputText getTxtAacMktrAgnCode() {
        return txtAacMktrAgnCode;
    }

    public void setTxtAacMarketerName(RichInputText txtAacMarketerName) {
        this.txtAacMarketerName = txtAacMarketerName;
    }

    public RichInputText getTxtAacMarketerName() {
        return txtAacMarketerName;
    }

    public void setBtnSaveUpdateAgencyActivity(RichCommandButton btnSaveUpdateAgencyActivity) {
        this.btnSaveUpdateAgencyActivity = btnSaveUpdateAgencyActivity;
    }

    public RichCommandButton getBtnSaveUpdateAgencyActivity() {
        return btnSaveUpdateAgencyActivity;
    }

    public void setTblActivityTypesPop(RichTable tblActivityTypesPop) {
        this.tblActivityTypesPop = tblActivityTypesPop;
    }

    public RichTable getTblActivityTypesPop() {
        return tblActivityTypesPop;
    }

    public void setTblMarketerPop(RichTable tblMarketerPop) {
        this.tblMarketerPop = tblMarketerPop;
    }

    public RichTable getTblMarketerPop() {
        return tblMarketerPop;
    }

    public void setPanelActivityTypeDetail(RichPanelBox panelActivityTypeDetail) {
        this.panelActivityTypeDetail = panelActivityTypeDetail;
    }

    public RichPanelBox getPanelActivityTypeDetail() {
        return panelActivityTypeDetail;
    }

    public void setTblSystemsPop(RichTable tblSystemsPop) {
        this.tblSystemsPop = tblSystemsPop;
    }

    public RichTable getTblSystemsPop() {
        return tblSystemsPop;
    }

    public void setTblClientsPop(RichTable tblClientsPop) {
        this.tblClientsPop = tblClientsPop;
    }

    public RichTable getTblClientsPop() {
        return tblClientsPop;
    }

    public void setTblServiceProvidersPop(RichTable tblServiceProvidersPop) {
        this.tblServiceProvidersPop = tblServiceProvidersPop;
    }

    public RichTable getTblServiceProvidersPop() {
        return tblServiceProvidersPop;
    }

    public void tblAgenciesListener(SelectionEvent selectionEvent) {
        Object key2 = tblAgencies.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("agnCode", nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("fetchAgencyActivitiesIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyActivity);
        }
    }

    public void clearAgencyActivityFields() {
        txtAacCode.setValue(null);
        txtAacActyCode.setValue(null);
        txtAacActyDesc.setValue(null);
        txtAacWef.setValue(null);
        txtAacEstimateWet.setValue(null);
        txtAacActualWet.setValue(null);
        txtAacRemarks.setValue(null);
        txtAacAgnCode.setValue(session.getAttribute("agnCode"));
        txtActivityByCode.setValue(null);
        txtAacReasonsForAct.setValue(null);

        //  txtAacClientCode.setValue( null );
        // txtAacClientName.setValue( null );
        //    txtAacSprCode.setValue( null );
        // txtAacServiceProviderName.setValue( null );
        txtAacSysCode.setValue(null);
        txtAacSystemName.setValue(null);
        // txtAacMktrAgnCode.setValue( null );
        //txtAacMarketerName.setValue( null );

        btnSaveUpdateAgencyActivity.setText("Save");
    }

    public String actionNewAgencyActivity() {
        if (session.getAttribute("agnCode") != null) {
            txtActivityByCode.setValue(null);
            txtParticipByCode.setValue(null);
            txtParticipByName.setValue(null);
            txtActivityByName.setValue(null);
            clearAgencyActivityFields();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:agencyActivityPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Agency Record to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditAgencyActivity() {
        Object key2 = tblAgencyActivity.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAacCode.setValue(nodeBinding.getAttribute("aacCode"));
            txtAacActyCode.setValue(nodeBinding.getAttribute("aacActyCode"));
            txtAacActyDesc.setValue(nodeBinding.getAttribute("activityDesc"));
            txtAacWef.setValue(nodeBinding.getAttribute("aacWef"));
            txtAacEstimateWet.setValue(nodeBinding.getAttribute("aacEstimateWet"));
            txtAacActualWet.setValue(nodeBinding.getAttribute("aacActualWet"));
            txtAacRemarks.setValue(nodeBinding.getAttribute("aacRemarks"));
            txtAacAgnCode.setValue(nodeBinding.getAttribute("aacAgnCode"));

            String act_type_code =
                GlobalCC.checkNullValues(txtAaActTypeCode.getValue());
            String reasons =
                GlobalCC.checkNullValues(txtAacReasonsForAct.getValue());
            String sysCode =
                GlobalCC.checkNullValues(txtAacSysCode.getValue());
            String act_by_code =
                GlobalCC.checkNullValues(txtActivityByCode.getValue());
            txtAacActualWet.setValue(nodeBinding.getAttribute("aacActualWet"));
            txtAacRemarks.setValue(nodeBinding.getAttribute("aacRemarks"));
            txtActivityByCode.setValue(nodeBinding.getAttribute("aacActivityByCode"));
            txtAacReasonsForAct.setValue(nodeBinding.getAttribute("aacReasnsforActivity"));


            /* private String aacType;
          private String aacActivityByType;  //code
          private String aacActivityTypeName; //name
          private BigDecimal aacActivityByCode; */
            socActByType.setValue(nodeBinding.getAttribute("aacType"));
            txtAaActTypeCode.setValue(nodeBinding.getAttribute("aacType"));
            txtAacSysCode.setValue(nodeBinding.getAttribute("aacSysCode"));
            txtAacSystemName.setValue(nodeBinding.getAttribute("systemName"));
            txtActivityByCode.setValue(nodeBinding.getAttribute("aacActivityByCode"));
            txtActivityByName.setValue(nodeBinding.getAttribute("agencyName"));
            btnSaveUpdateAgencyActivity.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:agencyActivityPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeleteAgencyActivity() {
        Object key2 = tblAgencyActivity.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String aacCode = nodeBinding.getAttribute("aacCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.agencyActivities_prc(?,?,?,?,?,?,?,?,?,?,?,?); end;";


                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                // statement.setArray(2, array);
                statement.setBigDecimal(2, new BigDecimal(aacCode.toString()));
                statement.setBigDecimal(3, null);

                statement.setBigDecimal(4, null);
                statement.setDate(5, null);
                statement.setDate(6, null);
                statement.setDate(7, null);
                statement.setString(8, null);
                statement.setBigDecimal(9, null);
                statement.setString(10, null);
                statement.setString(11, null);
                statement.setString(12, null);

                statement.execute();

                statement.close();
                conn.commit();
                conn.close();


                ADFUtils.findIterator("fetchAgencyActivitiesIterator").executeQuery();
                GlobalCC.refreshUI(tblAgencyActivity);

                clearAgencyActivityFields();

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

    public String actionSaveUpdateAgencyActivity() {
        if (btnSaveUpdateAgencyActivity.getText().equals("Edit")) {
            actionUpdateAgencyActivity();
        } else {
            String code = GlobalCC.checkNullValues(txtAacCode.getValue());
            String actyCode =
                GlobalCC.checkNullValues(txtAacActyCode.getValue());
            String wef = GlobalCC.checkNullValues(txtAacWef.getValue());
            String estimateWet =
                GlobalCC.checkNullValues(txtAacEstimateWet.getValue());
            String actualWet =
                GlobalCC.checkNullValues(txtAacActualWet.getValue());
            String remarks =
                GlobalCC.checkNullValues(txtAacRemarks.getValue());
            String agnCode =
                GlobalCC.checkNullValues(txtAacAgnCode.getValue());
            String act_type_code =
                GlobalCC.checkNullValues(txtAaActTypeCode.getValue());
            String reasons =
                GlobalCC.checkNullValues(txtAacReasonsForAct.getValue());
            String sysCode =
                GlobalCC.checkNullValues(txtAacSysCode.getValue());
            String act_by_code =
                GlobalCC.checkNullValues(txtActivityByCode.getValue());
            String acc_type =
                GlobalCC.checkNullValues(txtAaActTypeCode.getValue());


            if (actyCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Activity Type is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.agencyActivities_prc(?,?,?,?,?,?,?,?,?,?,?,?); end;";

                    // ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("TQC_AGENCY_ACTIVITIES_TAB", conn);
                    //  ArrayList activityList = new ArrayList();
                    // AgencyActivity agencyActivity = new AgencyActivity();
                    //agencyActivity.setSQLTypeName("TQC_AGENCY_ACTIVITIES_OBJ");

                    // Take care of all the date fields on the form.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date tmpWefDate = null; //new Date();
                    if (txtAacWef.getValue() != null &&
                        !(txtAacWef.getValue().equals(""))) {
                        String date1 = df.format(txtAacWef.getValue());
                        tmpWefDate = df.parse(date1);
                    }

                    Date tmpEstimateWet = null; //new Date();
                    if (txtAacEstimateWet.getValue() != null &&
                        !(txtAacEstimateWet.getValue().equals(""))) {
                        String date1 = df.format(txtAacEstimateWet.getValue());
                        tmpEstimateWet = df.parse(date1);
                    }

                    Date tmpActualWet = null; //new Date();
                    if (txtAacActualWet.getValue() != null &&
                        !(txtAacActualWet.getValue().equals(""))) {
                        String date1 = df.format(txtAacActualWet.getValue());
                        tmpActualWet = df.parse(date1);
                    }


                    //   agencyActivity.setAacCode(code == null ? null : new BigDecimal(code));
                    // agencyActivity.setAacActyCode(actyCode == null ? null : new BigDecimal(actyCode));
                    //agencyActivity.setAacWef(tmpWefDate == null ? null : new java.sql.Date(tmpWefDate.getTime()));
                    // agencyActivity.setAacEstimateWet(tmpEstimateWet == null ? null : new java.sql.Date(tmpEstimateWet.getTime()));
                    //  agencyActivity.setAacActualWet(tmpActualWet == null ? null : new java.sql.Date(tmpActualWet.getTime()));
                    //agencyActivity.setAacRemarks(remarks);
                    // agencyActivity.setAacAgnCode(agnCode == null ? null : new BigDecimal(agnCode));
                    //agencyActivity.setAacActivityByType(act_type_code==null ?  null : act_type_code);
                    //agencyActivity.setAacReasnsforActivity(reasons==null ?  null : reasons);
                    //agencyActivity.setAacClientCode(clientCode == null ? null : new BigDecimal(clientCode));
                    //  agencyActivity.setAacSprCode(sprCode == null ? null : new BigDecimal(sprCode));
                    //agencyActivity.setAacSysCode(sysCode == null ? null : new BigDecimal(sysCode));
                    //      agencyActivity.setAacMktrAgnCode(mktrAgnCode == null ? null : new BigDecimal(mktrAgnCode));

                    // activityList.add(agencyActivity);
                    //  ARRAY array = new ARRAY(descriptor, conn, activityList.toArray());

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "A");
                    // statement.setArray(2, array);
                    statement.setBigDecimal(2, null);
                    statement.setBigDecimal(3,
                                            sysCode == null ? null : new BigDecimal(sysCode));

                    statement.setBigDecimal(4,
                                            actyCode == null ? null : new BigDecimal(actyCode));
                    statement.setDate(5,
                                      tmpWefDate == null ? null : new java.sql.Date(tmpWefDate.getTime()));
                    statement.setDate(6,
                                      tmpEstimateWet == null ? null : new java.sql.Date(tmpEstimateWet.getTime()));
                    statement.setDate(7,
                                      tmpActualWet == null ? null : new java.sql.Date(tmpActualWet.getTime()));
                    statement.setString(8, remarks);
                    statement.setBigDecimal(9,
                                            agnCode == null ? null : new BigDecimal(agnCode));
                    statement.setString(10,
                                        act_type_code == null ? null : act_type_code);
                    statement.setString(11, reasons == null ? null : reasons);
                    statement.setString(12,
                                        act_by_code == null ? null : act_by_code);

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
                                         "crm:agencyActivityPop" +
                                         "').hide(hints);");

                    ADFUtils.findIterator("fetchAgencyActivitiesIterator").executeQuery();
                    GlobalCC.refreshUI(tblAgencyActivity);

                    clearAgencyActivityFields();

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdateAgencyActivity() {
        String code = GlobalCC.checkNullValues(txtAacCode.getValue());
        String actyCode = GlobalCC.checkNullValues(txtAacActyCode.getValue());
        String wef = GlobalCC.checkNullValues(txtAacWef.getValue());
        String estimateWet =
            GlobalCC.checkNullValues(txtAacEstimateWet.getValue());
        String actualWet =
            GlobalCC.checkNullValues(txtAacActualWet.getValue());
        String remarks = GlobalCC.checkNullValues(txtAacRemarks.getValue());
        String agnCode = GlobalCC.checkNullValues(txtAacAgnCode.getValue());

        String sysCode = GlobalCC.checkNullValues(txtAacSysCode.getValue());

        String act_type_code =
            GlobalCC.checkNullValues(txtAaActTypeCode.getValue());
        String reasons =
            GlobalCC.checkNullValues(txtAacReasonsForAct.getValue());

        String act_by_code =
            GlobalCC.checkNullValues(txtActivityByCode.getValue());

        if (actyCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Activity Type is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.agencyActivities_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_ACTIVITIES_TAB",
                                                     conn);
                ArrayList activityList = new ArrayList();
                AgencyActivity agencyActivity = new AgencyActivity();
                agencyActivity.setSQLTypeName("TQC_AGENCY_ACTIVITIES_OBJ");

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpWefDate = null; //new Date();
                if (txtAacWef.getValue() != null &&
                    !(txtAacWef.getValue().equals(""))) {
                    String date1 = df.format(txtAacWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                Date tmpEstimateWet = null; //new Date();
                if (txtAacEstimateWet.getValue() != null &&
                    !(txtAacEstimateWet.getValue().equals(""))) {
                    String date1 = df.format(txtAacEstimateWet.getValue());
                    tmpEstimateWet = df.parse(date1);
                }

                Date tmpActualWet = null; //new Date();
                if (txtAacActualWet.getValue() != null &&
                    !(txtAacActualWet.getValue().equals(""))) {
                    String date1 = df.format(txtAacActualWet.getValue());
                    tmpActualWet = df.parse(date1);
                }

                statement.setString(1, "E");
                // statement.setArray(2, array);
                statement.setBigDecimal(2,
                                        code == null ? null : new BigDecimal(code));
                statement.setBigDecimal(3,
                                        sysCode == null ? null : new BigDecimal(sysCode));

                statement.setBigDecimal(4,
                                        actyCode == null ? null : new BigDecimal(actyCode));
                statement.setDate(5,
                                  tmpWefDate == null ? null : new java.sql.Date(tmpWefDate.getTime()));
                statement.setDate(6,
                                  tmpEstimateWet == null ? null : new java.sql.Date(tmpEstimateWet.getTime()));
                statement.setDate(7,
                                  tmpActualWet == null ? null : new java.sql.Date(tmpActualWet.getTime()));
                statement.setString(8, remarks);
                statement.setBigDecimal(9,
                                        agnCode == null ? null : new BigDecimal(agnCode));
                statement.setString(10,
                                    act_type_code == null ? null : act_type_code);
                statement.setString(11, reasons == null ? null : reasons);
                statement.setString(12,
                                    act_by_code == null ? null : act_by_code);

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
                                     "crm:agencyActivityPop" +
                                     "').hide(hints);");

                ADFUtils.findIterator("fetchAgencyActivitiesIterator").executeQuery();
                GlobalCC.refreshUI(tblAgencyActivity);

                clearAgencyActivityFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionShowActivityTypesLov() {

        if (txtAacSysCode.getValue() != null) {
            session.setAttribute("SYSCODE", txtAacSysCode.getValue());
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:activityTypePop" + "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("First:Select System ");
            return null;
        }

    }

    public String actionAcceptActivityType() {
        Object key2 = tblActivityTypesPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAacActyCode.setValue(nodeBinding.getAttribute("actyCode"));
            txtAacActyDesc.setValue(nodeBinding.getAttribute("actyDesc"));
        }

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:activityTypePop" + "').hide(hints);");

        GlobalCC.refreshUI(panelActivityTypeDetail);
        return null;
    }

    public String actionShowMarketersPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:agentsListPop" + "').show(hints);");
        return null;
    }

    public String actionAcceptMarketer() {
        Object key2 = tblMarketerPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            // txtAacMktrAgnCode.setValue(nodeBinding.getAttribute("code"));
            //txtAacMarketerName.setValue(nodeBinding.getAttribute("name"));
            txtActivityByCode.setValue(nodeBinding.getAttribute("code"));
            txtParticipByCode.setValue(nodeBinding.getAttribute("code"));
        }
        GlobalCC.refreshUI(txtActivityByCode);
        GlobalCC.refreshUI(txtParticipByCode);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:agentsListPop" + "').hide(hints);");
        GlobalCC.refreshUI(txtActivityByCode);
        GlobalCC.refreshUI(panelActivityTypeDetail);
        return null;
    }

    public String actionShowSystemsPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:systemsPop" + "').show(hints);");
        return null;
    }

    public String actionAcceptSystem() {
        Object key2 = tblSystemsPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAacSysCode.setValue(nodeBinding.getAttribute("code"));
            txtAacSystemName.setValue(nodeBinding.getAttribute("name"));
        }
        session.setAttribute("sysCode", txtAacSysCode.getValue());
        ADFUtils.findIterator("fetchActivityTypeBySystemIterator").executeQuery();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:systemsPop" + "').hide(hints);");

        GlobalCC.refreshUI(panelActivityTypeDetail);
        return null;
    }

    public String actionShowClientsPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:clientsPop" + "').show(hints);");
        return null;
    }

    public String actionShowActivityByCode() {
        session.setAttribute("accShtDesc", null);
        if (txtAaActTypeCode.getValue() != null) {
            if (txtAaActTypeCode.getValue().toString().equalsIgnoreCase("CL")) {

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:clientsPop" + "').show(hints);");
                return null;
            } else if (txtAaActTypeCode.getValue().toString().equalsIgnoreCase("SP")) {


                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:serviceProvidersPop" +
                                     "').show(hints);");
                return null;
            } else {
                session.setAttribute("accShtDesc",
                                     txtAaActTypeCode.getValue());
                ADFUtils.findIterator("fetchAgencyActivityLovIterator").executeQuery();
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:agentsListPopByAccType" +
                                     "').show(hints);");
                return null;
            }


        } else {
            GlobalCC.INFORMATIONREPORTING("First Activity by type");
            return null;

        }


    }

    public String actionShowParticipActivityByCode() {
        session.setAttribute("accShtDesc", null);
        if (txtPartActivityTypeCode.getValue() != null) {
            if (txtPartActivityTypeCode.getValue().toString().equalsIgnoreCase("CL")) {

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:clientsPop" + "').show(hints);");
                return null;
            } else if (txtPartActivityTypeCode.getValue().toString().equalsIgnoreCase("SP")) {


                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:serviceProvidersPop" +
                                     "').show(hints);");
                return null;
            } else {
                session.setAttribute("accShtDesc",
                                     txtPartActivityTypeCode.getValue());
                ADFUtils.findIterator("fetchAgencyActivityLovIterator").executeQuery();
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:agentsListPopByAccType" +
                                     "').show(hints);");
                return null;
            }


        } else {
            GlobalCC.INFORMATIONREPORTING("First Activity by type");
            return null;

        }


    }

    public String actionAcceptClient() {
        Object key2 = tblClientsPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        txtActivityByCode.setValue(null);
        txtParticipByCode.setValue(null);
        txtParticipByName.setValue(null);
        txtActivityByName.setValue(null);

        if (nodeBinding != null) {
            //txtAacClientCode.setValue(nodeBinding.getAttribute("code"));
            //txtAacClientName.setValue(nodeBinding.getAttribute("name"));
            txtActivityByCode.setValue(nodeBinding.getAttribute("code"));
            txtParticipByCode.setValue(nodeBinding.getAttribute("code"));
            txtParticipByName.setValue(nodeBinding.getAttribute("name"));
            txtActivityByName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtParticipByName);
            GlobalCC.refreshUI(txtActivityByName);

        }

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:clientsPop" + "').hide(hints);");

        //  GlobalCC.refreshUI(panelActivityTypeDetail);
        return null;
    }

    public String actionShowServiceProvidersPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:serviceProvidersPop" + "').show(hints);");
        return null;
    }

    public String actionAcceptServiceProvider() {
        Object key2 = tblServiceProvidersPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        txtActivityByCode.setValue(null);
        txtParticipByCode.setValue(null);
        if (nodeBinding != null) {
            // txtAacSprCode.setValue(nodeBinding.getAttribute("code"));
            // txtAacServiceProviderName.setValue(nodeBinding.getAttribute("name"));

            txtActivityByCode.setValue(nodeBinding.getAttribute("code"));
            txtParticipByCode.setValue(nodeBinding.getAttribute("code"));
            txtParticipByName.setValue(nodeBinding.getAttribute("name"));
            txtActivityByName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtParticipByName);
            GlobalCC.refreshUI(txtActivityByName);
            GlobalCC.refreshUI(txtActivityByCode);
            GlobalCC.refreshUI(txtParticipByCode);
        }

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:serviceProvidersPop" + "').hide(hints);");

        GlobalCC.refreshUI(panelActivityTypeDetail);
        return null;
    }

    public void actionSubmitActivityType(ValueChangeEvent evt) {

        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {
            RichSelectOneChoice myComp =
                (RichSelectOneChoice)evt.getComponent();

            txtAaActTypeCode.setValue(myComp.getValue());

            session.setAttribute("accShtDesc", txtAaActTypeCode.getValue());
            ADFUtils.findIterator("fetchAgencyActivityLovIterator").executeQuery();

            GlobalCC.refreshUI(txtAaActTypeCode);

        }

    }

    public void actionSubmitParticipActivityType(ValueChangeEvent evt) {

        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {
            RichSelectOneChoice myComp =
                (RichSelectOneChoice)evt.getComponent();

            txtPartActivityTypeCode.setValue(myComp.getValue());


            session.setAttribute("accShtDesc", txtAaActTypeCode.getValue());
            ADFUtils.findIterator("fetchAgencyActivityLovIterator").executeQuery();
            GlobalCC.refreshUI(txtPartActivityTypeCode);

        }

    }

    public void setSocActByType(RichSelectOneChoice socActByType) {
        this.socActByType = socActByType;
    }

    public RichSelectOneChoice getSocActByType() {
        return socActByType;
    }

    public void setTxtAaActTypeCode(RichInputText txtAaActTypeCode) {
        this.txtAaActTypeCode = txtAaActTypeCode;
    }

    public RichInputText getTxtAaActTypeCode() {
        return txtAaActTypeCode;
    }

    public void actionSubmitActivityByType(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setTxtAacReasonsForAct(RichInputText txtAacReasonsForAct) {
        this.txtAacReasonsForAct = txtAacReasonsForAct;
    }

    public RichInputText getTxtAacReasonsForAct() {
        return txtAacReasonsForAct;
    }


    public void submitActivityType(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setTxtActivityByCode(RichInputText txtActivityByCode) {
        this.txtActivityByCode = txtActivityByCode;
    }

    public RichInputText getTxtActivityByCode() {
        return txtActivityByCode;
    }

    public void setSocSelectTypeParticip(RichSelectOneChoice socSelectTypeParticip) {
        this.socSelectTypeParticip = socSelectTypeParticip;
    }

    public RichSelectOneChoice getSocSelectTypeParticip() {
        return socSelectTypeParticip;
    }

    public void setTxtPartCode(RichInputText txtPartCode) {
        this.txtPartCode = txtPartCode;
    }

    public RichInputText getTxtPartCode() {
        return txtPartCode;
    }

    public void setTxtPartActivityTypeCode(RichInputText txtPartActivityTypeCode) {
        this.txtPartActivityTypeCode = txtPartActivityTypeCode;
    }

    public RichInputText getTxtPartActivityTypeCode() {
        return txtPartActivityTypeCode;
    }

    public String actionShowAddEditParticipants() {
        if (session.getAttribute("activity_code") != null) {
            txtParticipActivityCode.setValue(session.getAttribute("activity_code"));

            txtPartCode.setValue(null);
            txtPartActivityTypeCode.setValue(null);
            txtParticipByCode.setValue(null);
            ///;'  txtParticipActivityCode.setValue(null);
            txtParticipId.setValue(null);
            txtPartCode.setValue(null);
            txtParticipByName.setValue(null);
            txtParticipByCode.setValue(null);
            socSelectTypeParticip.setValue(null);


            GlobalCC.refreshUI(txtPartCode);
            GlobalCC.refreshUI(txtPartActivityTypeCode);
            GlobalCC.refreshUI(txtParticipByCode);
            GlobalCC.refreshUI(txtParticipByName);
            GlobalCC.refreshUI(socSelectTypeParticip);


            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:participantsPop" + "').show(hints);");

            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("First::Select Activity ");
            return null;
        }
    }

    public void setTxtParticipActivityCode(RichInputText txtParticipActivityCode) {
        this.txtParticipActivityCode = txtParticipActivityCode;
    }

    public RichInputText getTxtParticipActivityCode() {
        return txtParticipActivityCode;
    }

    public void tblAgencyActivityListener(SelectionEvent selectionEvent) {
        Object key2 = tblAgencyActivity.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("activity_code",
                                 nodeBinding.getAttribute("aacCode"));

            ADFUtils.findIterator("fetchActivityByParticipantsIterator").executeQuery();
            GlobalCC.refreshUI(tblParticipants);
        }
    }

    public String UpdateParticipants() {

        String aacCode =
            GlobalCC.checkNullValues(txtParticipActivityCode.getValue());
        String particip_type =
            GlobalCC.checkNullValues(txtPartActivityTypeCode.getValue());
        String participByCode =
            GlobalCC.checkNullValues(txtParticipByCode.getValue());
        String participCode =
            GlobalCC.checkNullValues(txtParticipActivityCode.getValue());
        String participId = GlobalCC.checkNullValues(txtParticipId.getValue());
        if (participId == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Participant ID  is Empty");
            return null;

        }
        if (aacCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Activity Type is Empty");
            return null;

        } else if (particip_type == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Activity  By Type is Empty");
            return null;

        } else if (participByCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Activity  By Code is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = (OracleConnection)dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_CURSOR.create_activity_particip(?,?,?,?,?,?); end;";


                // Take care of all the date fields on the form.

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "E");
                statement.setBigDecimal(2, new BigDecimal(participId));
                statement.setBigDecimal(3,
                                        aacCode == null ? null : new BigDecimal(aacCode));
                statement.setString(4,
                                    particip_type == null ? null : particip_type);
                statement.setString(5,
                                    participByCode == null ? null : participByCode);
                statement.setString(6,
                                    participCode == null ? null : participCode);


                statement.execute();

                statement.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("fetchActivityByParticipantsIterator").executeQuery();
                GlobalCC.refreshUI(tblParticipants);
                GlobalCC.INFORMATIONREPORTING("Record Updated Successfully");
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "crm:participantsPop" +
                                     "').hide(hints);");
                return null;
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }


        return null;
    }

    public String deleteParticipant() {
        Object key2 = tblParticipants.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        txtPartActivityTypeCode.setValue(null);
        txtParticipByCode.setValue(null);
        txtParticipActivityCode.setValue(null);
        txtParticipId.setValue(null);
        txtPartCode.setValue(null);
        if (nodeBinding != null) {
            txtPartActivityTypeCode.setValue(nodeBinding.getAttribute("participActType"));
            txtParticipByCode.setValue(nodeBinding.getAttribute("participByCode"));
            txtParticipActivityCode.setValue(nodeBinding.getAttribute("aacCode"));
            txtParticipId.setValue(nodeBinding.getAttribute("participId"));
            txtPartCode.setValue(nodeBinding.getAttribute("participCode"));
            String aacCode =
                GlobalCC.checkNullValues(txtParticipActivityCode.getValue());
            String particip_type =
                GlobalCC.checkNullValues(txtPartActivityTypeCode.getValue());
            String participByCode =
                GlobalCC.checkNullValues(txtParticipByCode.getValue());
            String participCode =
                GlobalCC.checkNullValues(txtParticipActivityCode.getValue());
            String participId =
                GlobalCC.checkNullValues(txtParticipId.getValue());
            if (aacCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Activity Type is Empty");
                return null;

            } else if (particip_type == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Activity  By Type is Empty");
                return null;

            } else if (participByCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Activity  By Code is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_CURSOR.create_activity_particip(?,?,?,?,?,?); end;";


                    // Take care of all the date fields on the form.

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "D");
                    statement.setBigDecimal(2, new BigDecimal(participId));
                    statement.setBigDecimal(3,
                                            aacCode == null ? null : new BigDecimal(aacCode));
                    statement.setString(4,
                                        particip_type == null ? null : particip_type);
                    statement.setString(5,
                                        participByCode == null ? null : participByCode);
                    statement.setString(6,
                                        participCode == null ? null : participCode);


                    statement.execute();

                    statement.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchActivityByParticipantsIterator").executeQuery();
                    GlobalCC.refreshUI(tblParticipants);
                    GlobalCC.INFORMATIONREPORTING("Record Deleted  Successfully");
                    return null;


                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);

                }
            }
        }
        return null;
    }

    public String saveEditParticipants() {

        if (btnSaveEditParticipants.getText().equalsIgnoreCase("update")) {
            UpdateParticipants();
        } else {

            String aacCode =
                GlobalCC.checkNullValues(txtParticipActivityCode.getValue());
            String particip_type =
                GlobalCC.checkNullValues(txtPartActivityTypeCode.getValue());
            String participByCode =
                GlobalCC.checkNullValues(txtParticipByCode.getValue());
            String participCode =
                GlobalCC.checkNullValues(txtParticipActivityCode.getValue());
            if (aacCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Activity Code is Required");
                return null;

            } else if (particip_type == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Activity  By Type is Empty");
                return null;

            } else if (participByCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Activity  By Code is Empty");
                return null;

            }


            else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn =
(OracleConnection)dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_CURSOR.create_activity_particip(?,?,?,?,?,?); end;";


                    // Take care of all the date fields on the form.

                    statement =
                            (OracleCallableStatement)conn.prepareCall(query);
                    statement.setString(1, "A");
                    statement.setBigDecimal(2, null);
                    statement.setBigDecimal(3,
                                            aacCode == null ? null : new BigDecimal(aacCode));
                    statement.setString(4,
                                        particip_type == null ? null : particip_type);
                    statement.setString(5,
                                        participByCode == null ? null : participByCode);
                    statement.setString(6,
                                        participCode == null ? null : participCode);


                    statement.execute();

                    statement.close();
                    conn.commit();
                    conn.close();
                    ADFUtils.findIterator("fetchActivityByParticipantsIterator").executeQuery();
                    GlobalCC.refreshUI(tblParticipants);
                    GlobalCC.INFORMATIONREPORTING("Record created  Successfully");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "crm:participantsPop" +
                                         "').hide(hints);");

                    return null;
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }

            }

        }
        return null;
    }

    public void setTxtParticipByCode(RichInputText txtParticipByCode) {
        this.txtParticipByCode = txtParticipByCode;
    }

    public RichInputText getTxtParticipByCode() {
        return txtParticipByCode;
    }

    public void setBtnSaveEditParticipants(RichCommandButton btnSaveEditParticipants) {
        this.btnSaveEditParticipants = btnSaveEditParticipants;
    }

    public RichCommandButton getBtnSaveEditParticipants() {
        return btnSaveEditParticipants;
    }

    public void setTblParticipants(RichTable tblParticipants) {
        this.tblParticipants = tblParticipants;
    }

    public RichTable getTblParticipants() {
        return tblParticipants;
    }

    public String actionOnEditParticipantAct() {
        Object key2 = tblParticipants.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        txtPartActivityTypeCode.setValue(null);
        txtParticipByCode.setValue(null);
        txtParticipActivityCode.setValue(null);
        txtParticipId.setValue(null);
        txtPartCode.setValue(null);
        if (nodeBinding != null) {
            btnSaveEditParticipants.setText("update");
            txtPartActivityTypeCode.setValue(nodeBinding.getAttribute("participActType"));
            txtParticipByCode.setValue(nodeBinding.getAttribute("participByCode"));
            txtParticipActivityCode.setValue(nodeBinding.getAttribute("aacCode"));
            txtParticipId.setValue(nodeBinding.getAttribute("participId"));
            txtPartCode.setValue(nodeBinding.getAttribute("participCode"));
            GlobalCC.refreshUI(txtPartCode);
            GlobalCC.refreshUI(txtPartActivityTypeCode);
            GlobalCC.refreshUI(txtParticipByCode);
            GlobalCC.refreshUI(txtParticipByName);
            GlobalCC.refreshUI(socSelectTypeParticip);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:participantsPop" + "').show(hints);");
            ;
            txtPartActivityTypeCode.setValue(null);
            txtParticipByCode.setValue(null);
            txtParticipActivityCode.setValue(null);
            txtParticipId.setValue(null);
            txtPartCode.setValue(null);
            btnSaveEditParticipants.setText("Save");

            return null;


        }


        return null;
    }

    public void setTxtParticipId(RichInputText txtParticipId) {
        this.txtParticipId = txtParticipId;
    }

    public RichInputText getTxtParticipId() {
        return txtParticipId;
    }

    public void setSelectStatusInactive(RichSelectItem selectStatusInactive) {
        this.selectStatusInactive = selectStatusInactive;
    }

    public RichSelectItem getSelectStatusInactive() {
        return selectStatusInactive;
    }

    public void setSelectStatusDraft(RichSelectItem selectStatusDraft) {
        this.selectStatusDraft = selectStatusDraft;
    }

    public RichSelectItem getSelectStatusDraft() {
        return selectStatusDraft;
    }

    public void setSelectStatusActive(RichSelectItem selectStatusActive) {
        this.selectStatusActive = selectStatusActive;
    }

    public RichSelectItem getSelectStatusActive() {
        return selectStatusActive;
    }

    public String actionAcceptAgency() {
        Object key2 = tblAgenciesByShortDesc.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        txtActivityByCode.setValue(null);
        txtParticipByCode.setValue(null);
        txtParticipByName.setValue(null);
        txtActivityByName.setValue(null);
        if (nodeBinding != null) {
            // txtAacMktrAgnCode.setValue(nodeBinding.getAttribute("code"));
            //txtAacMarketerName.setValue(nodeBinding.getAttribute("name"));
            txtActivityByCode.setValue(nodeBinding.getAttribute("code"));
            txtParticipByCode.setValue(nodeBinding.getAttribute("code"));
            txtParticipByName.setValue(nodeBinding.getAttribute("name"));
            txtActivityByName.setValue(nodeBinding.getAttribute("name"));
        }
        GlobalCC.refreshUI(txtActivityByCode);
        GlobalCC.refreshUI(txtParticipByCode);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:agentsListPopByAccType" + "').hide(hints);");
        GlobalCC.refreshUI(txtParticipByName);
        GlobalCC.refreshUI(txtActivityByName);
        GlobalCC.refreshUI(txtActivityByCode);
        GlobalCC.refreshUI(panelActivityTypeDetail);
        return null;

    }

    public String actionShowConfirmDeleteActivity() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:confirmedDeleteActivity" +
                             "').show(hints);");
        return null;


    }

    public String actionShowConfirmDeleteParticipant() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:confirmDeleteParticip" + "').show(hints);");
        return null;

    }

    public void setTblAgenciesByShortDesc(RichTable tblAgenciesByShortDesc) {
        this.tblAgenciesByShortDesc = tblAgenciesByShortDesc;
    }

    public RichTable getTblAgenciesByShortDesc() {
        return tblAgenciesByShortDesc;
    }

    public void setTxtParticipByName(RichInputText txtParticipByName) {
        this.txtParticipByName = txtParticipByName;
    }

    public RichInputText getTxtParticipByName() {
        return txtParticipByName;
    }

    public void setTxtActivityByName(RichInputText txtActivityByName) {
        this.txtActivityByName = txtActivityByName;
    }

    public RichInputText getTxtActivityByName() {
        return txtActivityByName;
    }

    public void setConfirmDeleteActivity(RichOutputLabel confirmDeleteActivity) {
        this.confirmDeleteActivity = confirmDeleteActivity;
    }

    public RichOutputLabel getConfirmDeleteActivity() {
        return confirmDeleteActivity;
    }

    public void confirmDeleteActivity(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            actionDeleteAgencyActivity();

        }
    }

    public void setConnfirmDeleteActivity(RichOutputLabel connfirmDeleteActivity) {
        this.connfirmDeleteActivity = connfirmDeleteActivity;
    }

    public RichOutputLabel getConnfirmDeleteActivity() {
        return connfirmDeleteActivity;
    }

    public void confirmDeleteParticipants(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            deleteParticipant();

        }
    }

    public void setConfirmDeleteParticipants(RichDialog confirmDeleteParticipants) {
        this.confirmDeleteParticipants = confirmDeleteParticipants;
    }

    public RichDialog getConfirmDeleteParticipants() {
        return confirmDeleteParticipants;
    }
}
