package TurnQuest.view.setups;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class PaymentModeBacking {
    private RichTable tblPaymentMode;
    private RichInputText txtModeCode;
    private RichInputText txtModeShtDesc;
    private RichInputText txtModeDesc;
    private RichInputText txtModeNaration;
    private RichSelectOneChoice txtModeDefault;
    private RichCommandButton btnSaveMode;

    public PaymentModeBacking() {
    }

    public void setTblPaymentMode(RichTable tblPaymentMode) {
        this.tblPaymentMode = tblPaymentMode;
    }

    public RichTable getTblPaymentMode() {
        return tblPaymentMode;
    }

    public void setTxtModeCode(RichInputText txtModeCode) {
        this.txtModeCode = txtModeCode;
    }

    public RichInputText getTxtModeCode() {
        return txtModeCode;
    }

    public void setTxtModeShtDesc(RichInputText txtModeShtDesc) {
        this.txtModeShtDesc = txtModeShtDesc;
    }

    public RichInputText getTxtModeShtDesc() {
        return txtModeShtDesc;
    }

    public void setTxtModeDesc(RichInputText txtModeDesc) {
        this.txtModeDesc = txtModeDesc;
    }

    public RichInputText getTxtModeDesc() {
        return txtModeDesc;
    }

    public void setTxtModeNaration(RichInputText txtModeNaration) {
        this.txtModeNaration = txtModeNaration;
    }

    public RichInputText getTxtModeNaration() {
        return txtModeNaration;
    }

    public void setTxtModeDefault(RichSelectOneChoice txtModeDefault) {
        this.txtModeDefault = txtModeDefault;
    }

    public RichSelectOneChoice getTxtModeDefault() {
        return txtModeDefault;
    }

    public void setBtnSaveMode(RichCommandButton btnSaveMode) {
        this.btnSaveMode = btnSaveMode;
    }

    public RichCommandButton getBtnSaveMode() {
        return btnSaveMode;
    }

    public void refreshFields() {
        txtModeCode.setValue(null);
        txtModeShtDesc.setValue(null);
        txtModeDesc.setValue(null);
        txtModeNaration.setValue(null);
    }

    public String actionSaveMode() {
        // Check if the user wishes to SAVE or UPDATE
        if (btnSaveMode.getText().equals("Edit")) {
            actionEditMode();
        } else {

            // Proceed to save
            String modeCode = GlobalCC.checkNullValues(txtModeCode.getValue());
            String modeShortDesc =
                GlobalCC.checkNullValues(txtModeShtDesc.getValue());
            String modeDesc = GlobalCC.checkNullValues(txtModeDesc.getValue());
            String modeNaration =
                GlobalCC.checkNullValues(txtModeNaration.getValue());
            String modeDefault =
                GlobalCC.checkNullValues(txtModeDefault.getValue());

            if (modeShortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
                return null;

            } else if (modeDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is empty");
                return null;

            } else if (modeNaration == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Naration is Empty");
                return null;

            } else if (modeDefault == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Default is not selected");
                return null;

            } else {
                String Query =
                    "begin TQC_SETUPS_PKG.payment_modes_prc(?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "A");
                    cst.setBigDecimal(2, null);
                    cst.setString(3, modeShortDesc);
                    cst.setString(4, modeDesc);
                    cst.setString(5, modeNaration);
                    cst.setString(6, modeDefault);

                    cst.execute();

                    cst.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchAllPaymentModesIterator").executeQuery();
                    GlobalCC.refreshUI(tblPaymentMode);

                    GlobalCC.INFORMATIONREPORTING("New Record CREATED Successfully!");
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionEditMode() {
        // Proceed to edit
        String modeCode = GlobalCC.checkNullValues(txtModeCode.getValue());
        String modeShortDesc =
            GlobalCC.checkNullValues(txtModeShtDesc.getValue());
        String modeDesc = GlobalCC.checkNullValues(txtModeDesc.getValue());
        String modeNaration =
            GlobalCC.checkNullValues(txtModeNaration.getValue());
        String modeDefault =
            GlobalCC.checkNullValues(txtModeDefault.getValue());

        if (modeCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Mode Code is Empty");
            return null;

        } else if (modeShortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
            return null;

        } else if (modeDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is empty");
            return null;

        } else if (modeNaration == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Naration is Empty");
            return null;

        } else if (modeDefault == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Default is not selected");
            return null;

        } else {
            String Query =
                "begin TQC_SETUPS_PKG.payment_modes_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "E");
                cst.setBigDecimal(2, new BigDecimal(modeCode));
                cst.setString(3, modeShortDesc);
                cst.setString(4, modeDesc);
                cst.setString(5, modeNaration);
                cst.setString(6, modeDefault);

                cst.execute();

                ADFUtils.findIterator("fetchAllPaymentModesIterator").executeQuery();
                GlobalCC.refreshUI(tblPaymentMode);

                GlobalCC.INFORMATIONREPORTING("Record UPDATED Successfully!");

                cst.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionNewPaymentMode() {
        refreshFields();
        btnSaveMode.setText("Save");
        // Open the popup dialog for payment modes
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:paymentModePop" + "').show(hints);");
        return null;
    }

    public String actionEditPaymentMode() {
        Object key2 = tblPaymentMode.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtModeCode.setValue(nodeBinding.getAttribute("code"));
            txtModeShtDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            txtModeDesc.setValue(nodeBinding.getAttribute("description"));
            txtModeNaration.setValue(nodeBinding.getAttribute("naration"));
            txtModeDefault.setValue(nodeBinding.getAttribute("defaultMode"));

            btnSaveMode.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:paymentModePop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String actionDeletePaymentMode() {
        Object key2 = tblPaymentMode.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            String modeCode = nodeBinding.getAttribute("code").toString();

            String Query =
                "begin TQC_SETUPS_PKG.payment_modes_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2, new BigDecimal(modeCode));
                cst.setString(3, null);
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setString(6, null);

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllPaymentModesIterator").executeQuery();
                GlobalCC.refreshUI(tblPaymentMode);

                GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully!");
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }
        return null;
    }
}
