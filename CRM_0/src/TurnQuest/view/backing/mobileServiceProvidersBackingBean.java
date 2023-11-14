package TurnQuest.view.backing;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;

import java.math.BigDecimal;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class mobileServiceProvidersBackingBean {
    private RichTable tblCountryPop;
    private RichInputText txtCouName;
    private RichInputText txtCouShtDesc;
    private RichInputText txtCouCode;

    private RichInputText txtCode;
    private RichInputText txtShortDesc;
    private RichInputText txtDescription;
    private RichInputText txtMinimum;
    private RichInputText txtMaximum;
    private RichInputText txtRemarks;
    private RichCommandButton actionSaveMobileTypes;
    private RichCommandButton btnSaveClaimMobileTypes;
    private RichTable tblMobilePaymentTypes;
    private RichInputText txtMptCode;
    private RichInputText txtMptpCode;
    private RichInputText txtPrefix;
    private RichCommandButton btnSaveMobilePrefixTypes;
    private RichTable tblPrefixes;

    public mobileServiceProvidersBackingBean() {
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public String actionSelectCountryPop() {
        Object key2 = tblCountryPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            txtCouName.setValue(nodeBinding.getAttribute("couName"));
            txtCouCode.setValue(nodeBinding.getAttribute("couCode"));
            txtCouShtDesc.setValue(nodeBinding.getAttribute("couShortDesc"));

            // Set the country code to be used to fetch the states
            session.setAttribute("countryCode",
                                 nodeBinding.getAttribute("couCode"));


            GlobalCC.refreshUI(txtCouName);
            GlobalCC.refreshUI(txtCouShtDesc);
            ADFUtils.findIterator("fetchAlLMobileTypesIterator").executeQuery();
            GlobalCC.refreshUI(tblMobilePaymentTypes);

            ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
            GlobalCC.refreshUI(tblPrefixes);

        }
        GlobalCC.dismissPopUp("pt1", "countryPop");
        return null;
    }


    //------------------------------------------------------------------------------


    public String actionNewPaymode() {
        if (session.getAttribute("countryCode") != null) {
            txtCode.setValue(null);
            txtDescription.setValue(null);
            txtShortDesc.setValue(null);
            txtMaximum.setValue(null);
            txtMinimum.setValue(null);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:mobilePaymentTypes" + "').show(hints);");
            return null;

        } else {
            GlobalCC.INFORMATIONREPORTING(" First Select  Country ::");
            return null;
        }
    }

    public String actionNewPrefix() {
        if (session.getAttribute("mptCode") != null) {

            txtMptpCode.setValue(null);
            txtPrefix.setValue(null);
            txtMptCode.setValue(session.getAttribute("mptCode"));
            btnSaveMobilePrefixTypes.setText("Save");
            GlobalCC.refreshUI(txtPrefix);
            GlobalCC.refreshUI(btnSaveMobilePrefixTypes);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:mobileTypesPrefixes" +
                                 "').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("First Select Mobile Type:: ");
            return null;
        }
    }

    public String actionEditMobilePaymentType() {
        txtCode.setValue(null);
        txtDescription.setValue(null);
        txtShortDesc.setValue(null);
        txtMaximum.setValue(null);
        txtMinimum.setValue(null);
        Object key2 = tblMobilePaymentTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {


            txtCode.setValue(nodeBinding.getAttribute("code"));
            txtDescription.setValue(nodeBinding.getAttribute("description"));
            txtShortDesc.setValue(nodeBinding.getAttribute("shortdesc"));
            txtMaximum.setValue(nodeBinding.getAttribute("maxAmt"));
            txtMinimum.setValue(nodeBinding.getAttribute("minAmt"));


            btnSaveClaimMobileTypes.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:mobilePaymentTypes" + "').show(hints);");

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }
        return null;


    }

    public String actionEditMobileTypePrefix() {
        txtMptpCode.setValue(null);
        txtPrefix.setValue(null);
        txtMptCode.setValue(null);

        Object key2 = tblPrefixes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            txtMptpCode.setValue(nodeBinding.getAttribute("mptpCode"));
            txtPrefix.setValue(nodeBinding.getAttribute("prefix"));
            txtMptCode.setValue(nodeBinding.getAttribute("mptCode"));

            btnSaveMobilePrefixTypes.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:mobileTypesPrefixes" +
                                 "').show(hints);");

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }
        return null;


    }


    public String actionDeleteMobTypes() {
        Object key2 = tblMobilePaymentTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("code").toString();
            String Query =
                "begin TQC_SETUPS_PKG.mobilePaymentTypes_prc(?,?,?,?  ,?,?,?); end;";
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


                cst.execute();

                // Close the connections
                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAlLMobileTypesIterator").executeQuery();
                GlobalCC.refreshUI(tblMobilePaymentTypes);

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

    public String actionDeleteMobTypesPrefix() {
        Object key2 = tblPrefixes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("mptpCode").toString();
            String Query =
                "begin TQC_SETUPS_PKG.mobileTypesPrefix_prc(?,?,?,?); end;";
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

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();


                ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
                GlobalCC.refreshUI(tblPrefixes);

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

    public String actionConfirmDeleteMobTypes(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteMobTypes();
        }
        return null;
        // Add event code here...
    }

    public String actionConfirmDeleteMobTypesPrefix(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteMobTypesPrefix();
        }
        return null;
        // Add event code here...
    }
    //-----------------------------------------------------------------------


    public String actionConfirmDeleteMobTypes() {

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:confirmDeleteMobileType" +
                             "').show(hints);");
        return null;
    }

    public String actionConfirmDeleteMobPrefix() {

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:confirmDeleteMobileTypePrefix" +
                             "').show(hints);");
        return null;
    }

    public String actionShowCountryPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:countryPop" + "').show(hints);");
        return null;
    }


    public String actionSaveMobileTypes() {
        if (btnSaveClaimMobileTypes.getText().equalsIgnoreCase("Save")) {
            String code = GlobalCC.checkNullValues(txtCode.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtShortDesc.getValue());
            String description =
                GlobalCC.checkNullValues(txtDescription.getValue());
            String maxAmt = GlobalCC.checkNullValues(txtMaximum.getValue());
            String minAmt = GlobalCC.checkNullValues(txtMinimum.getValue());


            if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Short desc is required::");
            } else if (description == null) {
                GlobalCC.errorValueNotEntered("Description is required::");
            } else if (maxAmt == null) {
                GlobalCC.errorValueNotEntered("Maximum Amount is required::");
            } else if (minAmt == null) {
                GlobalCC.errorValueNotEntered("Minimum Amount  is required::");
            } else if (session.getAttribute("countryCode") == null) {
                GlobalCC.errorValueNotEntered("CountryCode   is required::");
            } else {


                String Query =
                    "begin TQC_SETUPS_PKG.mobilePaymentTypes_prc(?,?,?,?  ,?,?,?); end;";
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
                    cst.setString(5, minAmt);
                    cst.setString(6, maxAmt);
                    cst.setBigDecimal(7,
                                      new BigDecimal(session.getAttribute("countryCode").toString()));


                    cst.execute();

                    // Close the connections
                    cst.close();
                    conn.commit();
                    conn.close();


                    String message = "Record saved  Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                    ADFUtils.findIterator("fetchAlLMobileTypesIterator").executeQuery();
                    GlobalCC.refreshUI(tblMobilePaymentTypes);

                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:mobilePaymentTypes" +
                                         "').hide(hints);");

                } catch (Exception e) {
                    e.printStackTrace();
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
            } else if (session.getAttribute("countryCode") == null) {
                GlobalCC.errorValueNotEntered("CountryCode   is required::");
            } else {


                String Query =
                    "begin TQC_SETUPS_PKG.mobilePaymentTypes_prc(?,?,?,?  ,?,?,?); end;";
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
                    cst.setString(5, minAmt);
                    cst.setString(6, maxAmt);
                    cst.setBigDecimal(7,
                                      new BigDecimal(session.getAttribute("countryCode").toString()));


                    cst.execute();

                    // Close the connections
                    cst.close();
                    conn.commit();
                    conn.close();


                    String message = "Record Updated Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                    ADFUtils.findIterator("fetchAlLMobileTypesIterator").executeQuery();
                    GlobalCC.refreshUI(tblMobilePaymentTypes);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:mobilePaymentTypes" +
                                         "').hide(hints);");

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }


            }


        }
        return null;
    }

    public String actionSaveMobileTypesPrefix() {
        if (btnSaveMobilePrefixTypes.getText().equalsIgnoreCase("Save")) {
            String mptpcode = GlobalCC.checkNullValues(txtMptpCode.getValue());
            String mptcode = GlobalCC.checkNullValues(txtMptCode.getValue());
            String prefix = GlobalCC.checkNullValues(txtPrefix.getValue());


            if (prefix == null) {
                GlobalCC.errorValueNotEntered("Prefix is required::");
            } else if (mptcode == null) {
                GlobalCC.errorValueNotEntered("Select Mobile Type ::");
            }


            else {


                String Query =
                    "begin TQC_SETUPS_PKG.mobileTypesPrefix_prc(?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);
                    cst.setString(1, "A");
                    cst.setBigDecimal(2,
                                      mptpcode == null ? null : new BigDecimal(mptpcode));
                    cst.setString(3, prefix);
                    cst.setString(4, mptcode);

                    cst.execute();

                    // Close the connections
                    cst.close();
                    conn.commit();
                    conn.close();

                    GlobalCC.dismissPopUp("pt1", "mobileTypesPrefixes");
                    String message = "Record saved  Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                    ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
                    GlobalCC.refreshUI(tblPrefixes);

                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }


            }


        } else {
            String mptpcode = GlobalCC.checkNullValues(txtMptpCode.getValue());
            String mptcode = GlobalCC.checkNullValues(txtMptCode.getValue());
            String prefix = GlobalCC.checkNullValues(txtPrefix.getValue());


            if (mptpcode == null) {
                GlobalCC.errorValueNotEntered("Code  is required::");
            } else if (prefix == null) {
                GlobalCC.errorValueNotEntered("Prefix is required::");
            } else if (mptcode == null) {
                GlobalCC.errorValueNotEntered("Select Mobile Type ::");
            }


            else {


                String Query =
                    "begin TQC_SETUPS_PKG.mobileTypesPrefix_prc(?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);
                    cst.setString(1, "E");
                    cst.setBigDecimal(2,
                                      mptpcode == null ? null : new BigDecimal(mptpcode));
                    cst.setString(3, prefix);
                    cst.setString(4, mptcode);

                    cst.execute();
                    cst.close();
                    conn.commit();
                    conn.close();


                    String message = "Record Updated Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                    ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
                    GlobalCC.refreshUI(tblPrefixes);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:mobileTypesPrefixes" +
                                         "').hide(hints);");
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }


            }
        }
        return null;
    }

    public void actionListenerTblMobilePymntTypes(SelectionEvent selectionEvent) {
        Object key2 = tblMobilePaymentTypes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            session.setAttribute("mptCode", nodeBinding.getAttribute("code"));

            ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
            GlobalCC.refreshUI(tblPrefixes);
        }
    }

    public void setTblCountryPop(RichTable tblCountryPop) {
        this.tblCountryPop = tblCountryPop;
    }

    public RichTable getTblCountryPop() {
        return tblCountryPop;
    }


    public void setTxtCouName(RichInputText txtCouName) {
        this.txtCouName = txtCouName;
    }

    public RichInputText getTxtCouName() {
        return txtCouName;
    }

    public void setTxtShtDesc(RichInputText txtCouShtDesc) {
        this.txtCouShtDesc = txtCouShtDesc;
    }

    public RichInputText getTxtShtDesc() {
        return txtCouShtDesc;
    }

    public void setTxtCouCode(RichInputText txtCouCode) {
        this.txtCouCode = txtCouCode;
    }

    public RichInputText getTxtCouCode() {
        return txtCouCode;
    }

    public void setTxtCode(RichInputText txtCode) {
        this.txtCode = txtCode;
    }

    public RichInputText getTxtCode() {
        return txtCode;
    }

    public void setTxtShortDesc(RichInputText txtShortDesc) {
        this.txtShortDesc = txtShortDesc;
    }

    public RichInputText getTxtShortDesc() {
        return txtShortDesc;
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

    public void setTxtRemarks(RichInputText txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    public RichInputText getTxtRemarks() {
        return txtRemarks;
    }

    public void setActionSaveMobileTypes(RichCommandButton actionSaveMobileTypes) {
        this.actionSaveMobileTypes = actionSaveMobileTypes;
    }

    public RichCommandButton getActionSaveMobileTypes() {
        return actionSaveMobileTypes;
    }

    public void setBtnSaveClaimMobileTypes(RichCommandButton btnSaveClaimMobileTypes) {
        this.btnSaveClaimMobileTypes = btnSaveClaimMobileTypes;
    }

    public RichCommandButton getBtnSaveClaimMobileTypes() {
        return btnSaveClaimMobileTypes;
    }

    public void setTblMobilePaymentTypes(RichTable tblMobilePaymentTypes) {
        this.tblMobilePaymentTypes = tblMobilePaymentTypes;
    }

    public RichTable getTblMobilePaymentTypes() {
        return tblMobilePaymentTypes;
    }

    public void setTxtMptCode(RichInputText txtMptCode) {
        this.txtMptCode = txtMptCode;
    }

    public RichInputText getTxtMptCode() {
        return txtMptCode;
    }

    public void setTxtMptpCode(RichInputText txtMptpCode) {
        this.txtMptpCode = txtMptpCode;
    }

    public RichInputText getTxtMptpCode() {
        return txtMptpCode;
    }

    public void setTxtPrefix(RichInputText txtPrefix) {
        this.txtPrefix = txtPrefix;
    }

    public RichInputText getTxtPrefix() {
        return txtPrefix;
    }

    public void setBtnSaveMobilePrefixTypes(RichCommandButton btnSaveMobilePrefixTypes) {
        this.btnSaveMobilePrefixTypes = btnSaveMobilePrefixTypes;
    }

    public RichCommandButton getBtnSaveMobilePrefixTypes() {
        return btnSaveMobilePrefixTypes;
    }


    public void setTblPrefixes(RichTable tblPrefixes) {
        this.tblPrefixes = tblPrefixes;
    }

    public RichTable getTblPrefixes() {
        return tblPrefixes;
    }


}

