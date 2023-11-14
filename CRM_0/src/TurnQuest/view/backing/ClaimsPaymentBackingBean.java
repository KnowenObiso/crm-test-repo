package TurnQuest.view.backing;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ClaimsPaymentBackingBean {
    private RichInputText txtDescription;
    private RichInputText txtMinimum;
    private RichInputText txtMaximum;
    private RichCommandButton btnSaveClaimPaymentMode;
    private RichSelectOneChoice txtDefault;
    private RichSelectOneChoice txtShortDesc;
    private RichInputText txtRemarks;
    private RichInputText txtCode;
    private RichTable tblClaimPaymentModes;

    public ClaimsPaymentBackingBean() {
    }

    public void actionAcceptId(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setTxtDescription(RichInputText txtDescription) {
        this.txtDescription = txtDescription;
    }

    public RichInputText getTxtDescription() {
        return txtDescription;
    }

    public void setTxtMinimum(RichInputText txtMinimum) {
        this.txtMinimum = txtMinimum;
    }

    public RichInputText getTxtMinimum() {
        return txtMinimum;
    }

    public void setTxtMaximum(RichInputText txtMaximum) {
        this.txtMaximum = txtMaximum;
    }

    public RichInputText getTxtMaximum() {
        return txtMaximum;
    }

    public void setBtnSaveClaimPaymentMode(RichCommandButton btnSaveClaimPaymentMode) {
        this.btnSaveClaimPaymentMode = btnSaveClaimPaymentMode;
    }

    public RichCommandButton getBtnSaveClaimPaymentMode() {
        return btnSaveClaimPaymentMode;
    }

    public String actionSavePaymentMode() {

        if (btnSaveClaimPaymentMode.getText().equalsIgnoreCase("Save")) {
            String code = GlobalCC.checkNullValues(txtCode.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtShortDesc.getValue());
            String description =
                GlobalCC.checkNullValues(txtDescription.getValue());
            String maxAmt = GlobalCC.checkNullValues(txtMaximum.getValue());
            String minAmt = GlobalCC.checkNullValues(txtMinimum.getValue());
            String default_value =
                GlobalCC.checkNullValues(txtDefault.getValue());
            String remarks = GlobalCC.checkNullValues(txtRemarks.getValue());

            if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Short desc is required::");
            } else if (description == null) {
                GlobalCC.errorValueNotEntered("Description is required::");
            } else if (maxAmt == null) {
                GlobalCC.errorValueNotEntered("Maximum Amount is required::");
            } else if (minAmt == null) {
                GlobalCC.errorValueNotEntered("Minimum Amount  is required::");
            } else if (default_value == null) {
                GlobalCC.errorValueNotEntered("Default Value is required::");


            } else {


                String Query =
                    "begin TQC_SETUPS_PKG.claim_paymentMode_prc(?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);
                    cst.setString(1, "A");
                    cst.setBigDecimal(2,
                                      code == null ? null : new BigDecimal(code));
                    cst.setString(3, shortDesc);
                    cst.setString(4, description);
                    cst.setString(5, remarks == null ? null : remarks);
                    cst.setString(6, maxAmt);
                    cst.setString(7, minAmt);
                    cst.setString(8, default_value);


                    cst.execute();

                    // Close the connections
                    cst.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchAlClaimsModesIterator").executeQuery();
                    GlobalCC.refreshUI(tblClaimPaymentModes);

                    String message = "Record saved  Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:claimPaymentFormPop" +
                                         "').hide(hints);");

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }


            }


        } else {


            String code = GlobalCC.checkNullValues(txtCode.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtShortDesc.getValue());
            String description =
                GlobalCC.checkNullValues(txtDescription.getValue());
            String maxAmt = GlobalCC.checkNullValues(txtMaximum.getValue());
            String minAmt = GlobalCC.checkNullValues(txtMinimum.getValue());
            String default_value =
                GlobalCC.checkNullValues(txtDefault.getValue());
            String remarks = GlobalCC.checkNullValues(txtRemarks.getValue());
            if (code == null) {
                GlobalCC.errorValueNotEntered("Code is required::");
            } else if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Short desc is required::");
            } else if (description == null) {
                GlobalCC.errorValueNotEntered("Description is required::");
            } else if (maxAmt == null) {
                GlobalCC.errorValueNotEntered("Maximum Amount is required::");
            } else if (minAmt == null) {
                GlobalCC.errorValueNotEntered("Minimum Amount  is required::");
            } else if (default_value == null) {
                GlobalCC.errorValueNotEntered("Default Value is required::");


            } else {


                String Query =
                    "begin TQC_SETUPS_PKG.claim_paymentMode_prc(?,?,?,?  ,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);
                    cst.setString(1, "E");
                    cst.setBigDecimal(2,
                                      code == null ? null : new BigDecimal(code));
                    cst.setString(3, shortDesc);
                    cst.setString(4, description);
                    cst.setString(5, remarks == null ? null : remarks);
                    cst.setString(6, maxAmt);
                    cst.setString(7, minAmt);
                    cst.setString(8, default_value);

                    cst.execute();

                    // Close the connections
                    cst.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchAlClaimsModesIterator").executeQuery();
                    GlobalCC.refreshUI(tblClaimPaymentModes);

                    String message = "Record Updated Successfully!";

                    GlobalCC.INFORMATIONREPORTING(message);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:claimPaymentFormPop" +
                                         "').hide(hints);");

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }


            }


        }

        // Add event code here...
        return null;
    }

    public String actionNewPaymode() {
        txtCode.setValue(null);
        txtDescription.setValue(null);
        txtShortDesc.setValue(null);
        txtMaximum.setValue(null);
        txtMinimum.setValue(null);
        btnSaveClaimPaymentMode.setText("Save");

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:claimPaymentFormPop" + "').show(hints);");
        return null;

    }

    public String actionEditPaymentMode() {
        txtCode.setValue(null);
        txtDescription.setValue(null);
        txtShortDesc.setValue(null);
        txtMaximum.setValue(null);
        txtMinimum.setValue(null);
        Object key2 = tblClaimPaymentModes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {


            txtCode.setValue(nodeBinding.getAttribute("code"));
            txtDescription.setValue(nodeBinding.getAttribute("description"));
            txtShortDesc.setValue(nodeBinding.getAttribute("shortdesc"));
            txtMaximum.setValue(nodeBinding.getAttribute("maxAmt"));
            txtMinimum.setValue(nodeBinding.getAttribute("minAmt"));
            txtRemarks.setValue(nodeBinding.getAttribute("remarks"));
            txtDefault.setValue(nodeBinding.getAttribute("default_value"));


            btnSaveClaimPaymentMode.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:claimPaymentFormPop" +
                                 "').show(hints);");

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }
        return null;


    }

    public String actionDeletePayMode() {
        Object key2 = tblClaimPaymentModes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("code").toString();
            String Query =
                "begin TQC_SETUPS_PKG.claim_paymentMode_prc(?,?,?,?  ,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);
                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  code == null ? null : new BigDecimal(code));
                cst.setString(3, null);
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setString(6, null);
                cst.setString(7, null);
                cst.setString(8, null);


                cst.execute();

                // Close the connections
                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAlClaimsModesIterator").executeQuery();
                GlobalCC.refreshUI(tblClaimPaymentModes);

                String message = "Record Deleted Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }


        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }
        return null;
    }

    public String actionConfirmDeletePayMode(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeletePayMode();
        }
        return null;
        // Add event code here...
    }

    public void setTxtDefault(RichSelectOneChoice txtDefault) {
        this.txtDefault = txtDefault;
    }

    public RichSelectOneChoice getTxtDefault() {
        return txtDefault;
    }

    public void settxtShortDesc(RichSelectOneChoice txtShortDesc) {
        this.txtShortDesc = txtShortDesc;
    }

    public RichSelectOneChoice gettxtShortDesc() {
        return txtShortDesc;
    }

    public void setTxtRemarks(RichInputText txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    public RichInputText getTxtRemarks() {
        return txtRemarks;
    }

    public void setTxtCode(RichInputText txtCode) {
        this.txtCode = txtCode;
    }

    public RichInputText getTxtCode() {
        return txtCode;
    }

    public void setTblClaimPaymentModes(RichTable tblClaimPaymentModes) {
        this.tblClaimPaymentModes = tblClaimPaymentModes;
    }

    public RichTable getTblClaimPaymentModes() {
        return tblClaimPaymentModes;
    }


    public String actionConfirmDeletePayMode() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:confirmDeletePaymode" + "').show(hints);");
        return null;
    }


    public void actionConfirmDeleteBankBranch(DialogEvent dialogEvent) {
        // Add event code here...
    }
}

