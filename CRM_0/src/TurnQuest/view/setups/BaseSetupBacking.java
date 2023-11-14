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

package TurnQuest.view.setups;


import TurnQuest.view.Accounts.WebUser;
import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Ui.FormUi;
import TurnQuest.view.models.PostalCode;

import java.math.BigDecimal;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.component.UIXGroup;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


/**
 * The base backing bean for all the setups related pages. Includes properties
 * and methods that map to given  UI components in the relevant pages within
 * the setups section/menu.
 *
 * @author Frankline Ogongi
 */
public class BaseSetupBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    /* Holding Company Properties */
    private RichTable tblHoldingCompanies;
    private RichCommandButton btnNewHoldingCompany;
    private RichCommandButton btnEditHoldingCompany;
    private RichCommandButton btnDeleteHoldingCompany;
    private RichInputText txtHoldCompCode;
    private RichInputText txtHoldCompName;
    private RichCommandButton btnCreateUpdateHoldComp;

    /* Banks Definitions Properties*/
    private RichTree treeBanks;
    private RichInputText txtBankName;
    private RichInputText txtBankShortDesc;
    private RichInputText txtBankRemarks;
    private RichInputText txtBankDDRCode;
    private RichInputText txtBankDDFormatDesc;
    private RichInputText txtBankForwadingCode;
    private RichInputText txtBankKBACode;
    private RichInputText txtBankCode;
    private RichPanelBox panelBankDetails;
    private RichTable tblBankBranches;
    private RichCommandButton btnNewBankBranch;
    private RichCommandButton btnEditBankBranch;
    private RichCommandButton btnDeleteBankBranch;
    private RichInputText txtBranchCodePop;
    private RichInputText txtBranchBankCodePop;
    private RichInputText txtBranchNamePop;
    private RichInputText txtBranchShtDescPop;
    private RichInputText txtBtanchRemarksPop;
    private RichInputText txtBrancRefCodePop;
    private RichSelectOneChoice txtBranchEFTSupportPop;
    private RichSelectOneChoice txtBranchDDSupportPop;
    private RichInputDate txtBranchDateCreatedPop;
    private RichInputText txtBranchCreatedByPop;
    private RichInputText txtBranchPhysicalAddressPop;
    private RichInputText txtPostalAddressPop;
    private RichCommandButton btnCreateUpdateBankBranch;
    private RichInputText txtBranchKBACode;
    private RichCommandButton btnUpdateMainBankDetails;
    private RichSelectOneChoice txtClientPerson;

    /* Agency Classification Properties */
    private RichCommandButton btnNewAgencyClass;
    private RichCommandButton btnEditAgencyClass;
    private RichCommandButton btnDeleteAgencyClass;
    private RichTable tblAgencyClass;
    private RichInputText txtAgencyClassCode;
    private RichInputText txtAgencyClassDesc;
    private RichCommandButton btnCreateUpdateAgencyClass;

    /* Currency Definitions Properties */
    private RichTree treeCurrency;
    private RichPanelBox panelCurrencyDetails;
    private RichInputText txtCurrencyCode;
    private RichInputText txtCurrencySymbol;
    private RichInputText txtCurrencyDesc;
    private RichInputText txtCurrencyRound;
    private RichCommandButton btnCreateUpdateCurrency;
    private RichCommandButton btnNewCurrency;
    private RichTable tblCurrencyDenominations;
    private RichInputText txtDenomCode;
    private RichInputText txtCurrDenomCode;
    private RichInputText txtDenomValue;
    private RichInputText txtDenomName;
    private RichInputDate txtDenomWEF;
    private RichCommandButton btnCreateUpdateCurrDenom;
    private RichCommandButton btnNewCurrencyDenom;
    private RichCommandButton btnEditCurrencyDenom;
    private RichCommandButton btnDeleteCurrencyDenom;

    /* Exchange Rates Properties*/
    private RichTree treeERCurrency;
    private RichTable tableERExchangeRates;
    private RichCommandButton btnNewExchangeRate;
    private RichCommandButton btnEditExchangeRate;
    private RichCommandButton btnDeleteExchangeRate;
    private RichInputText txtExchangeRateCode;
    private RichInputText txtCurrCodePop;
    private RichInputText txtBaseCurrPop;
    private RichInputText txtRatePop;
    private RichInputDate txtDatePop;
    private RichCommandButton btnCreateUpdateExchangeRate;
    private RichTable tableBaseCurrencyPopup;
    private RichCommandButton btnAcceptExchangeRate;
    private RichInputText txtBaseCurrCodePop;

    /* Sectors Properties */
    private RichTable tblSectors;
    private RichInputText txtSectorShtDesc;
    private RichInputText txtSectorName;
    private RichCommandButton btnCreateUpdateSector;
    private RichInputText txtSectorCode;

    /* Service Provider Properties */
    private RichTable tblServiceProviderType;
    private RichInputText txtProviderTypeCodePop;
    private RichInputText txtProviderTypeShtDescPop;
    private RichInputText txtProviderTypeNamePop;
    private RichSelectOneChoice txtProviderTypeStatus;
    private RichCommandButton btnCreateUpdateProviderType;
    private RichInputText txtProviderTypeWhtxRatePop;
    private RichInputText txtProviderVatRatePop;

    /* Countries Properties */
    private RichTree treeCountries;
    private RichInputText txtCountryCode;
    private RichInputText txtCountryShtDesc;
    private RichInputText txtCountryName;
    private RichInputText txtCountryBaseCurrency;
    private RichInputText txtCountryNationality;
    private RichInputText txtCountryZIPCode;
    private RichPanelBox panelCountryDetails;
    private RichCommandButton btnCreateUpdateCountry;
    private RichCommandButton btnCancelCountry;
    private RichTable tblCountryTowns;
    private RichTable tblTownLocations;
    private RichInputText txtTownCodePopup;
    private RichInputText txtTownCountryPopup;
    private RichInputText txtTownShtDescPopup;
    private RichInputText txtTownNamePopup;
    private RichInputText txtTownSTSCodePopup;
    private RichCommandButton btnCreateUpdateTown;
    private RichTable tblCountryDists;
    private RichInputText txtDistCodePopup;
    private RichInputText txtDistCountryPopup;
    private RichInputText txtDistShtDescPopup;
    private RichInputText txtDistNamePopup;
    private RichInputText txtDistSTSCodePopup;
    private RichCommandButton btnCreateUpdateDist;
    private RichInputText txtLocationCodePopup;
    private RichInputText txtLocationTownCodePopup;
    private RichInputText txtLocationShtDescPopup;
    private RichInputText txtLocationNamePopup;
    private RichCommandButton btnCreateUpdateLocation;
    private RichTable tblCountryBaseCurrencies;
    private RichInputText txtCountryBaseCurrencySymbol;

    /* User Parameter Properties */
    private RichInputText txtParameterCode;
    private RichInputText txtParameterName;
    private RichInputText txtParameterValue;
    private RichSelectOneChoice txtParameterStatus;
    private RichInputText txtParameterDesc;
    private RichCommandButton btnCreateUpdateParameter;
    private RichTable tblParameters;
    private RichInputNumberSpinbox txtProviderTypeWhtxRatePop2;
    private RichInputNumberSpinbox txtProviderVatRatePop2;
    private RichTable tblCountryStates;
    private RichInputText txtStateCodePopup;
    private RichInputText txtCountryCodePopup;
    private RichInputText txtStateShtDescPopup;
    private RichInputText txtStateNamePopup;
    private RichCommandButton btnCreateUpdateState;
    private RichCommandButton btnCancelState;
    private RichOutputText textToShow;
    private RichTable servProviderTypesAct;
    private RichInputText txtActivityId;
    private RichInputText txtActivityDesc;
    private RichTable tblPostalCodes;
    private RichInputText txtPstCode;
    private RichInputText txtPstTwnCode;
    private RichInputText txtPstDesc;
    private RichInputText txtPstZipCode;
    private RichCommandButton btnSaveUpdatePostalCode;
    private RichInputText txtCampProductName;
    private RichInputText txtCampUser;
    private RichInputText txtCampTeam;
    private RichTable tblCountries;
    private RichSelectOneChoice chAdminUnits;
    private UIXGroup groupCountryDivisions;
    private RichShowDetailItem shDetailAdminUnits;
    private RichOutputLabel lblAdminUnit;
    private RichDialog dlgNewEditAdminUnit;
    private RichSelectOneChoice chSchengen;
    private RichInputText txtEmbCode;
    private RichInputNumberSpinbox txtCurrentSerial;
    private RichInputText txtPostAdd;
    private RichInputText txtPhyAdd;
    private RichInputText txtTelNumber;
    private RichInputText txtMobNumber;
    private RichInputText txtContactPerson;

    private RichTable webUsersTable;
    private RichInputText webUserId;
    private RichInputText webusername;
    private RichInputText webfullNames;
    private RichInputText webPassword;
    private RichInputText webEmail;
    private RichInputText webPersonalRank;
    private RichSelectOneRadio webAllowLogin;
    private RichSelectOneChoice webUserStatus;
    private RichSelectOneRadio webReset;
    private RichInputText txtClientType;
    private RichSelectOneChoice txtClientCategory;
    private RichTable txtClientTypeTbl;
    
    private RichInputText txtAgentType;
    private RichSelectOneChoice txtAgentTypeShtDesc;
    private RichTable txtAgentTypeTbl;
    
    private RichInputText txtClientShtDesc;
    private RichInputText txtClientNames;
    private RichInputText txtClientOthernames;
    private RichInputText txtRegNumber;
    private RichInputText txtClientDateOfBirth;
    private RichInputText txtClientsPhyAddress;
    private RichInputNumberSpinbox txtTownCode;
    private RichInputNumberSpinbox countryCode;
    private RichInputText txtAccountNumber;
    private RichInputDate txtClientCoverFrom;
    private RichInputDate txtClientCoverTo;
    private RichSelectOneChoice txtClientTypes;
    private RichInputText txtCreatedBy;
    private RichInputText txtAgentStatus;
    private RichInputText txtDirectClient;
    private RichInputDate txtDateCreated;
    private RichInputNumberSpinbox txtClientCodes;
    private RichInputText txtSmsTemplates;
    private RichInputText txtEmailTemplates;
    private RichTable smsTbl;
    private RichTable emailTbl;
    private RichSelectBooleanCheckbox smsCheck;
    private RichSelectBooleanCheckbox emailCheck;
    private RichTree systemTree;
    private RichTable requiredDocTbl;
    private RichSelectBooleanCheckbox rowChecked;
    private RichInputText txtOccupShtDesc;
    private RichInputText txtOccup;
    private RichTable occupTbl;
    private RichShowDetailItem locationTab;
    private RichInputText txtIdNumber;
    private RichInputText txtAddress;
    private RichInputText txtTelNumberVal;
    private RichInputText txtMobileNumber;
    private RichInputText txtEmail;
    private RichInputText txtZipCode;
    private RichInputText txttown;
    private RichInputText txtName;
    private RichInputText txtPin;
    private RichInputText txtBankAccountNumber;
    private RichInputText txtSwiftCode;
    private RichTable countryTbl;
    private RichInputText txtCountry;
    private RichSelectOneChoice txtType;
    private RichInputText txtClaimant;
    private RichInputText txtBankBranch;
    private RichTable bankBranchTbl;
    private RichTable registedClaimantTbl;
    private RichTable bussinessPersonTbl;
    private RichTable bussPersonTbl;
    private RichInputText txtLandmark;
    private RichInputNumberSpinbox reportDays;
    private RichTable allOccupTbl;
    private RichInputText txtSearchCountry;
    private RichSelectBooleanCheckbox chkOccSelect;
    private RichSelectBooleanCheckbox chkOrmSelect;
    //Agent Classes
    private RichInputText txtAccountType;
    private RichTable tblAccountTypesPop;
    private RichInputNumberSpinbox txtSuffix;
    private RichInputText txtPrefix;


    /**
     * Default Constructor
     */
    public BaseSetupBacking() {
    }

    public static BigDecimal extractBigDecimal(RichInputNumberSpinbox component) {
        BigDecimal val = null;
        try {
            val = new BigDecimal(component.getValue().toString());
        } catch (Exception ex) {
            val = null;
        }
        return val;
    }

    public static java.sql.Date extractDate(RichInputDate component) {
        java.sql.Date val = null;
        try {
            val = new java.sql.Date(((Date)component.getValue()).getTime());
        } catch (Exception ex) {
            val = null;
        }
        return val;
    }

    /*====================== Holding Company Methods ======================*/

    public void setTblHoldingCompanies(RichTable tblHoldingCompanies) {
        this.tblHoldingCompanies = tblHoldingCompanies;
    }

    public RichTable getTblHoldingCompanies() {
        return tblHoldingCompanies;
    }

    public void setBtnNewHoldingCompany(RichCommandButton btnNewHoldingCompany) {
        this.btnNewHoldingCompany = btnNewHoldingCompany;
    }

    public RichCommandButton getBtnNewHoldingCompany() {
        return btnNewHoldingCompany;
    }

    public void setBtnEditHoldingCompany(RichCommandButton btnEditHoldingCompany) {
        this.btnEditHoldingCompany = btnEditHoldingCompany;
    }

    public RichCommandButton getBtnEditHoldingCompany() {
        return btnEditHoldingCompany;
    }

    public void setBtnDeleteHoldingCompany(RichCommandButton btnDeleteHoldingCompany) {
        this.btnDeleteHoldingCompany = btnDeleteHoldingCompany;
    }

    public RichCommandButton getBtnDeleteHoldingCompany() {
        return btnDeleteHoldingCompany;
    }

    public void setTxtHoldCompCode(RichInputText txtHoldCompCode) {
        this.txtHoldCompCode = txtHoldCompCode;
    }

    public RichInputText getTxtHoldCompCode() {
        return txtHoldCompCode;
    }

    public void setTxtHoldCompName(RichInputText txtHoldCompName) {
        this.txtHoldCompName = txtHoldCompName;
    }

    public RichInputText getTxtHoldCompName() {
        return txtHoldCompName;
    }

    public void setBtnCreateUpdateHoldComp(RichCommandButton btnCreateUpdateHoldComp) {
        this.btnCreateUpdateHoldComp = btnCreateUpdateHoldComp;
    }

    public RichCommandButton getBtnCreateUpdateHoldComp() {
        return btnCreateUpdateHoldComp;
    }

    /**
     * Prepares the popup window by clearing the input fields to allow a new
     * <code>AgencyHoldingCompany</code> object/record to be created.
     *
     * @return NULL
     */
    public String actionNewHoldingCompany() {

        // Clear the text fileds
        txtHoldCompCode.setValue(null);
        txtHoldCompName.setValue(null);
        btnCreateUpdateHoldComp.setText("Save");
        txtPostAdd.setValue(null);
        txtPhyAdd.setValue(null);
        txtTelNumber.setValue(null);
        txtMobNumber.setValue(null);
        txtContactPerson.setValue(null);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:holdingCompanyPopup').show(hints);");

        return null;
    }

    public String actionsSHOWAgencyClassDelete() {
        Object key2 = tblAgencyClass.getSelectedRowData();

        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {


            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmationDialog').show(hints);");
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected");

            return null;
        }
    }

    /**
     * Prepares the popup window by populating the input fields with the
     * selected record from the table.
     *
     * @return NULL
     */
    public String actionEditHoldingCompany() {

        Object key2 = tblHoldingCompanies.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            // Set the text field values
            txtHoldCompCode.setValue(nodeBinding.getAttribute("code"));
            txtHoldCompName.setValue(nodeBinding.getAttribute("name"));
            txtPostAdd.setValue(nodeBinding.getAttribute("postAdd"));
            txtPhyAdd.setValue(nodeBinding.getAttribute("phyAdd"));
            txtTelNumber.setValue(nodeBinding.getAttribute("telNumber"));
            txtMobNumber.setValue(nodeBinding.getAttribute("mobNumber"));
            txtContactPerson.setValue(nodeBinding.getAttribute("contactPerson"));
            btnCreateUpdateHoldComp.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:holdingCompanyPopup" +
                                 "').show(hints);");
            btnEditHoldingCompany.setDisabled(true);
            btnDeleteHoldingCompany.setDisabled(true);
            GlobalCC.refreshUI(btnEditHoldingCompany);
            GlobalCC.refreshUI(btnDeleteHoldingCompany);

        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String confirmDeleteAction() {
        Object key2 = tblHoldingCompanies.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmationDialog" + "').show(hints);");

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    /**
     * Deletes a <code>AgencyHoldingCompany</code> object/record from the
     * database.
     *
     * @return NULL
     */
    public String actionDeleteHoldingCompany() {
        Object key2 = tblHoldingCompanies.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String Query =
                "begin TQC_SETUPS_PKG.agency_holding_company_prc(?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)nodeBinding.getAttribute("code")));
                cst.setString(3, (String)nodeBinding.getAttribute("name"));
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setString(6, null);
                cst.setString(7, null);
                cst.setString(8, null);
                cst.registerOutParameter(9, OracleTypes.VARCHAR);
                cst.execute();
                String err = cst.getString(9);

                //GlobalCC.INFORMATIONREPORTING(err);

                ADFUtils.findIterator("fetchAllAgencyHoldingCompaniesIterator").executeQuery();
                GlobalCC.refreshUI(tblHoldingCompanies);

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

                btnEditHoldingCompany.setDisabled(true);
                btnDeleteHoldingCompany.setDisabled(true);
                GlobalCC.refreshUI(btnEditHoldingCompany);
                GlobalCC.refreshUI(btnDeleteHoldingCompany);
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    /**
     * Updates a <code>AgencyHoldingCompany</code> object/record to the database
     *
     * @return NULL
     */
    public String actionCreateUpdateHoldingCompany() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateHoldComp.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewHoldingCompany();

        } else {

            // Proceed to do an update
            if (txtHoldCompCode.getValue() == null ||
                txtHoldCompCode.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Holding Company Code!");

            } else if (txtHoldCompName.getValue() == null ||
                       txtHoldCompName.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Holding Company Name!");

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.agency_holding_company_prc(?,?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst =
(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2,
                                      new BigDecimal((String)txtHoldCompCode.getValue()));
                    cst.setString(3, (String)txtHoldCompName.getValue());
                    cst.setObject(4,
                                  GlobalCC.checkNullValues(txtPostAdd.getValue()));
                    cst.setObject(5,
                                  GlobalCC.checkNullValues(txtPhyAdd.getValue()));
                    cst.setObject(6,
                                  GlobalCC.checkNullValues(txtTelNumber.getValue()));
                    cst.setObject(7,
                                  GlobalCC.checkNullValues(txtMobNumber.getValue()));
                    cst.setObject(8,
                                  GlobalCC.checkNullValues(txtContactPerson.getValue()));
                    cst.registerOutParameter(9, OracleTypes.VARCHAR);
                    cst.execute();

                    String err = cst.getString(9);
                    //GlobalCC.INFORMATIONREPORTING(err);


                    cst.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchAllAgencyHoldingCompaniesIterator").executeQuery();
                    GlobalCC.refreshUI(tblHoldingCompanies);
                    btnEditHoldingCompany.setDisabled(true);
                    btnDeleteHoldingCompany.setDisabled(true);
                    GlobalCC.refreshUI(btnEditHoldingCompany);
                    GlobalCC.refreshUI(btnDeleteHoldingCompany);
                    if (err != null) {
                        GlobalCC.INFORMATIONREPORTING(err);
                        return null;
                    } else {
                        GlobalCC.dismissPopUp("pt1", "holdingCompanyPopup");
                        String message = "Record UPDATED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                        return null;
                    }

                } catch (SQLException e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }

        return null;
    }

    /**
     * Creates a new <code>AgencyHoldingCompany</code> in the database.
     *
     * @return Success or Error message string
     */
    public String actionCreateNewHoldingCompany() {

        if (txtHoldCompName.getValue() == null ||
            txtHoldCompName.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Holding Company Name!");

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.agency_holding_company_prc(?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, (String)txtHoldCompName.getValue());
                cst.setObject(4,
                              GlobalCC.checkNullValues(txtPostAdd.getValue()));
                cst.setObject(5,
                              GlobalCC.checkNullValues(txtPhyAdd.getValue()));
                cst.setObject(6,
                              GlobalCC.checkNullValues(txtTelNumber.getValue()));
                cst.setObject(7,
                              GlobalCC.checkNullValues(txtMobNumber.getValue()));
                cst.setObject(8,
                              GlobalCC.checkNullValues(txtContactPerson.getValue()));
                cst.registerOutParameter(9, OracleTypes.VARCHAR);
                cst.execute();

                String err = cst.getString(9);


                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllAgencyHoldingCompaniesIterator").executeQuery();
                GlobalCC.refreshUI(tblHoldingCompanies);

                String message = "New Record ADDED Successfully!";
                if (err != null) {
                    GlobalCC.INFORMATIONREPORTING(err);
                } else {
                    GlobalCC.dismissPopUp("pt1", "holdingCompanyPopup");
                    GlobalCC.INFORMATIONREPORTING(message);
                }

            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return "Success";
    }

    /* ================== Bank Definition methods ======================*/

    public void setTreeBanks(RichTree treeBanks) {
        this.treeBanks = treeBanks;
    }

    public RichTree getTreeBanks() {
        return treeBanks;
    }

    public void setTxtBankName(RichInputText txtBankName) {
        this.txtBankName = txtBankName;
    }

    public RichInputText getTxtBankName() {
        return txtBankName;
    }

    public void setTxtBankShortDesc(RichInputText txtBankShortDesc) {
        this.txtBankShortDesc = txtBankShortDesc;
    }

    public RichInputText getTxtBankShortDesc() {
        return txtBankShortDesc;
    }

    public void setTxtBankRemarks(RichInputText txtBankRemarks) {
        this.txtBankRemarks = txtBankRemarks;
    }

    public RichInputText getTxtBankRemarks() {
        return txtBankRemarks;
    }

    public void setTxtBankDDRCode(RichInputText txtBankDDRCode) {
        this.txtBankDDRCode = txtBankDDRCode;
    }

    public RichInputText getTxtBankDDRCode() {
        return txtBankDDRCode;
    }

    public void setTxtBankDDFormatDesc(RichInputText txtBankDDFormatDesc) {
        this.txtBankDDFormatDesc = txtBankDDFormatDesc;
    }

    public RichInputText getTxtBankDDFormatDesc() {
        return txtBankDDFormatDesc;
    }

    public void setTxtBankForwadingCode(RichInputText txtBankForwadingCode) {
        this.txtBankForwadingCode = txtBankForwadingCode;
    }

    public RichInputText getTxtBankForwadingCode() {
        return txtBankForwadingCode;
    }

    public void setTxtBankKBACode(RichInputText txtBankKBACode) {
        this.txtBankKBACode = txtBankKBACode;
    }

    public RichInputText getTxtBankKBACode() {
        return txtBankKBACode;
    }

    public void setTxtBankCode(RichInputText txtBankCode) {
        this.txtBankCode = txtBankCode;
    }

    public RichInputText getTxtBankCode() {
        return txtBankCode;
    }

    public void setPanelBankDetails(RichPanelBox panelBankDetails) {
        this.panelBankDetails = panelBankDetails;
    }

    public RichPanelBox getPanelBankDetails() {
        return panelBankDetails;
    }

    public void setTblBankBranches(RichTable tblBankBranches) {
        this.tblBankBranches = tblBankBranches;
    }

    public RichTable getTblBankBranches() {
        return tblBankBranches;
    }

    public void setBtnNewBankBranch(RichCommandButton btnNewBankBranch) {
        this.btnNewBankBranch = btnNewBankBranch;
    }

    public RichCommandButton getBtnNewBankBranch() {
        return btnNewBankBranch;
    }

    public void setBtnEditBankBranch(RichCommandButton btnEditBankBranch) {
        this.btnEditBankBranch = btnEditBankBranch;
    }

    public RichCommandButton getBtnEditBankBranch() {
        return btnEditBankBranch;
    }

    public void setBtnDeleteBankBranch(RichCommandButton btnDeleteBankBranch) {
        this.btnDeleteBankBranch = btnDeleteBankBranch;
    }

    public RichCommandButton getBtnDeleteBankBranch() {
        return btnDeleteBankBranch;
    }

    public void setTxtBranchCodePop(RichInputText txtBranchCodePop) {
        this.txtBranchCodePop = txtBranchCodePop;
    }

    public RichInputText getTxtBranchCodePop() {
        return txtBranchCodePop;
    }

    public void setTxtBranchBankCodePop(RichInputText txtBranchBankCodePop) {
        this.txtBranchBankCodePop = txtBranchBankCodePop;
    }

    public RichInputText getTxtBranchBankCodePop() {
        return txtBranchBankCodePop;
    }

    public void setTxtBranchNamePop(RichInputText txtBranchNamePop) {
        this.txtBranchNamePop = txtBranchNamePop;
    }

    public RichInputText getTxtBranchNamePop() {
        return txtBranchNamePop;
    }

    public void setTxtBranchShtDescPop(RichInputText txtBranchShtDescPop) {
        this.txtBranchShtDescPop = txtBranchShtDescPop;
    }

    public RichInputText getTxtBranchShtDescPop() {
        return txtBranchShtDescPop;
    }

    public void setTxtBtanchRemarksPop(RichInputText txtBtanchRemarksPop) {
        this.txtBtanchRemarksPop = txtBtanchRemarksPop;
    }

    public RichInputText getTxtBtanchRemarksPop() {
        return txtBtanchRemarksPop;
    }

    public void setTxtBrancRefCodePop(RichInputText txtBrancRefCodePop) {
        this.txtBrancRefCodePop = txtBrancRefCodePop;
    }

    public RichInputText getTxtBrancRefCodePop() {
        return txtBrancRefCodePop;
    }

    public void setTxtBranchEFTSupportPop(RichSelectOneChoice txtBranchEFTSupportPop) {
        this.txtBranchEFTSupportPop = txtBranchEFTSupportPop;
    }

    public RichSelectOneChoice getTxtBranchEFTSupportPop() {
        return txtBranchEFTSupportPop;
    }

    public void setTxtBranchDDSupportPop(RichSelectOneChoice txtBranchDDSupportPop) {
        this.txtBranchDDSupportPop = txtBranchDDSupportPop;
    }

    public RichSelectOneChoice getTxtBranchDDSupportPop() {
        return txtBranchDDSupportPop;
    }

    public void setTxtBranchDateCreatedPop(RichInputDate txtBranchDateCreatedPop) {
        this.txtBranchDateCreatedPop = txtBranchDateCreatedPop;
    }

    public RichInputDate getTxtBranchDateCreatedPop() {
        return txtBranchDateCreatedPop;
    }

    public void setTxtBranchCreatedByPop(RichInputText txtBranchCreatedByPop) {
        this.txtBranchCreatedByPop = txtBranchCreatedByPop;
    }

    public RichInputText getTxtBranchCreatedByPop() {
        return txtBranchCreatedByPop;
    }

    public void setTxtBranchPhysicalAddressPop(RichInputText txtBranchPhysicalAddressPop) {
        this.txtBranchPhysicalAddressPop = txtBranchPhysicalAddressPop;
    }

    public RichInputText getTxtBranchPhysicalAddressPop() {
        return txtBranchPhysicalAddressPop;
    }

    public void setTxtPostalAddressPop(RichInputText txtPostalAddressPop) {
        this.txtPostalAddressPop = txtPostalAddressPop;
    }

    public RichInputText getTxtPostalAddressPop() {
        return txtPostalAddressPop;
    }

    public void setBtnCreateUpdateBankBranch(RichCommandButton btnCreateUpdateBankBranch) {
        this.btnCreateUpdateBankBranch = btnCreateUpdateBankBranch;
    }

    public RichCommandButton getBtnCreateUpdateBankBranch() {
        return btnCreateUpdateBankBranch;
    }

    public void setTxtBranchKBACode(RichInputText txtBranchKBACode) {
        this.txtBranchKBACode = txtBranchKBACode;
    }

    public RichInputText getTxtBranchKBACode() {
        return txtBranchKBACode;
    }

    public void setBtnUpdateMainBankDetails(RichCommandButton btnUpdateMainBankDetails) {
        this.btnUpdateMainBankDetails = btnUpdateMainBankDetails;
    }

    public RichCommandButton getBtnUpdateMainBankDetails() {
        return btnUpdateMainBankDetails;
    }

    /**
     * @param selectionEvent
     */
    public void treeBanksSelectionListener(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {

                for (Object treeRowKey : keys) {
                    treeBanks.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeBanks.getRowData();

                    txtBankCode.setValue(nd.getRow().getAttribute("bankCode"));
                    txtBankName.setValue(nd.getRow().getAttribute("bankName"));
                    txtBankShortDesc.setValue(nd.getRow().getAttribute("shortDesc"));
                    txtBankRemarks.setValue(nd.getRow().getAttribute("remarks"));
                    txtBankDDRCode.setValue(nd.getRow().getAttribute("DDRCode"));
                    txtBankDDFormatDesc.setValue(nd.getRow().getAttribute("DDFormatDesc"));
                    txtBankForwadingCode.setValue(nd.getRow().getAttribute("forwardingBankCode"));
                    txtBankKBACode.setValue(nd.getRow().getAttribute("KBACode"));

                    session.setAttribute("bankCode",
                                         nd.getRow().getAttribute("bankCode"));

                    ADFUtils.findIterator("fetchBankBranchByBankCodeIterator").executeQuery();
                    GlobalCC.refreshUI(tblBankBranches);
                    GlobalCC.refreshUI(panelBankDetails);
                }
            }
        }

        btnUpdateMainBankDetails.setText("Update");
    }

    /**
     * @return
     */
    public String actionNewBankBranch() {

        if (txtBankCode.getValue() == null ||
            txtBankCode.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("You need to first select a bank!");
        } else {

            // Clear the text fileds
            txtBranchCodePop.setValue("");
            txtBranchBankCodePop.setValue(txtBankCode.getValue());
            txtBranchNamePop.setValue("");
            txtBranchShtDescPop.setValue("");
            txtBtanchRemarksPop.setValue("");
            txtBrancRefCodePop.setValue("");
            txtBranchDateCreatedPop.setValue("");
            txtBranchCreatedByPop.setValue("");
            txtBranchPhysicalAddressPop.setValue("");
            txtPostalAddressPop.setValue("");
            btnCreateUpdateBankBranch.setText("Save");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:bankBranchPopup').show(hints);");
        }
        return null;
    }

    /**
     * @return
     */
    public String actionEditBankBranch() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchBankBranchByBankCodeIterator");
        RowKeySet set = tblBankBranches.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            // Set the text field values
            txtBranchCodePop.setValue(rows.getAttribute("branchCode"));
            txtBranchBankCodePop.setValue(rows.getAttribute("branchBankCode"));
            txtBranchNamePop.setValue(rows.getAttribute("branchName"));
            txtBranchShtDescPop.setValue(rows.getAttribute("shortDesc"));
            txtBtanchRemarksPop.setValue(rows.getAttribute("remarks"));
            txtBrancRefCodePop.setValue(rows.getAttribute("refCode"));
            txtBranchEFTSupportPop.setValue(rows.getAttribute("EFTSupported"));
            txtBranchDDSupportPop.setValue(rows.getAttribute("DDSupported"));
            txtBranchDateCreatedPop.setValue(rows.getAttribute("dateCreated"));
            txtBranchCreatedByPop.setValue(rows.getAttribute("createdBy"));
            txtBranchPhysicalAddressPop.setValue(rows.getAttribute("physicalAddress"));
            txtPostalAddressPop.setValue(rows.getAttribute("postalAddress"));
            txtBranchKBACode.setValue(rows.getAttribute("KBACode"));
            btnCreateUpdateBankBranch.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:bankBranchPopup" + "').show(hints);");
        }

        return null;
    }

    /**
     * @return
     */
    public String actionDeleteBankBranch() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchBankBranchByBankCodeIterator");
        RowKeySet set = tblBankBranches.getSelectedRowKeys();
        Iterator row = set.iterator();

        String Query =
            "begin TQC_SETUPS_PKG.bank_branches_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)rows.getAttribute("branchCode")));
                cst.setBigDecimal(3,
                                  new BigDecimal((String)rows.getAttribute("branchBankCode")));
                cst.setString(4, (String)rows.getAttribute("branchName"));
                cst.setString(5, (String)rows.getAttribute("remarks"));
                cst.setString(6, (String)rows.getAttribute("shortDesc"));
                cst.setString(7, (String)rows.getAttribute("refCode"));
                cst.setString(8, (String)rows.getAttribute("EFTSupported"));
                cst.setString(9, (String)rows.getAttribute("DDSupported"));
                cst.setDate(10,
                            new java.sql.Date(((java.util.Date)rows.getAttribute("dateCreated")).getTime()));
                cst.setString(11, (String)rows.getAttribute("createdBy"));
                cst.setString(12,
                              (String)rows.getAttribute("physicalAddress"));
                cst.setString(13, (String)rows.getAttribute("postalAddress"));
                cst.setString(14, (String)rows.getAttribute("KBACode"));

                cst.registerOutParameter(15, OracleTypes.VARCHAR);
                cst.setBigDecimal(16,null);
                cst.execute();
                String err = cst.getString(15);
                GlobalCC.INFORMATIONREPORTING(err);

                cst.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        ADFUtils.findIterator("fetchBankBranchByBankCodeIterator").executeQuery();
        GlobalCC.refreshUI(tblBankBranches);

        return null;
    }

    /**
     * @return
     */
    public String actionCreateUpdateBankBranch() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateBankBranch.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewBankBranch();

        } else {
            BigDecimal territoryCode =
                GlobalCC.checkBDNullValues(session.getAttribute("territoryCode"));

            if (territoryCode.compareTo(BigDecimal.ZERO)==0) {
                GlobalCC.INFORMATIONREPORTING("Select Bank Territory!");
               return null;
            } 
            // Proceed to do an update
            if (txtBranchCodePop.getValue() == null ||
                txtBranchCodePop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Bank Branch Code!");

            } else if (txtBranchBankCodePop.getValue() == null ||
                       txtBranchBankCodePop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Main Bank Branch Code!");

            } else if (txtBranchNamePop.getValue() == null ||
                       txtBranchNamePop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Bank Branch Name!");

            } else if (txtBranchShtDescPop.getValue() == null ||
                       txtBranchShtDescPop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Bank Branch Short Description!");

                /*} else if (txtBranchDateCreatedPop.getValue() == null ||
                       txtBranchDateCreatedPop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Date 'Created value'!");*/

            } else if (txtBranchCreatedByPop.getValue() == null ||
                       txtBranchCreatedByPop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the 'Created by' value!");

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.bank_branches_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2,
                                      new BigDecimal((String)txtBranchCodePop.getValue()));
                    cst.setBigDecimal(3,
                                      new BigDecimal((String)txtBranchBankCodePop.getValue()));
                    cst.setString(4, (String)txtBranchNamePop.getValue());
                    cst.setString(5, (String)txtBtanchRemarksPop.getValue());
                    cst.setString(6, (String)txtBranchShtDescPop.getValue());
                    cst.setString(7, (String)txtBrancRefCodePop.getValue());
                    cst.setString(8,
                                  (String)txtBranchEFTSupportPop.getValue());
                    cst.setString(9, (String)txtBranchDDSupportPop.getValue());
                    cst.setDate(10,
                                null); // No need to change the date created
                    cst.setString(11,
                                  (String)txtBranchCreatedByPop.getValue());
                    cst.setString(12,
                                  (String)txtBranchPhysicalAddressPop.getValue());
                    cst.setString(13, (String)txtPostalAddressPop.getValue());
                    cst.setString(14, (String)txtBranchKBACode.getValue());

                    cst.registerOutParameter(15, OracleTypes.VARCHAR);
                    
                    cst.setBigDecimal(16, territoryCode);
                    cst.execute();
                    String err = cst.getString(15);
                    GlobalCC.INFORMATIONREPORTING(err);

                    ADFUtils.findIterator("fetchBankBranchByBankCodeIterator").executeQuery();
                    GlobalCC.refreshUI(tblBankBranches);

                    cst.close();
                    conn.close();
                } catch (SQLException e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }

        return null;
    }

    /**
     * @return
     */
    public String actionCreateNewBankBranch() {

        if (txtBranchNamePop.getValue() == null ||
            txtBranchNamePop.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Bank Branch Name!");

        } else if (txtBranchShtDescPop.getValue() == null ||
                   txtBranchShtDescPop.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Bank Branch Short Description!");

            /*} else if (txtBranchDateCreatedPop.getValue() == null ||
                   txtBranchDateCreatedPop.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the date created!"); */

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.bank_branches_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                BigDecimal territoryCode =
                    GlobalCC.checkBDNullValues(session.getAttribute("territoryCode"));

                if (territoryCode.compareTo(BigDecimal.ZERO)==0) {
                    GlobalCC.INFORMATIONREPORTING("Select Bank Territory!");
                   return null;
                } 
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setBigDecimal(3,
                                  new BigDecimal((String)txtBranchBankCodePop.getValue()));
                cst.setString(4, (String)txtBranchNamePop.getValue());
                cst.setString(5, (String)txtBtanchRemarksPop.getValue());
                cst.setString(6, (String)txtBranchShtDescPop.getValue());
                cst.setString(7, (String)txtBrancRefCodePop.getValue());
                cst.setString(8, (String)txtBranchEFTSupportPop.getValue());
                cst.setString(9, (String)txtBranchDDSupportPop.getValue());
                cst.setDate(10,
                            new java.sql.Date(new java.util.Date().getTime()));
                cst.setString(11, (String)session.getAttribute("Username"));
                cst.setString(12,
                              (String)txtBranchPhysicalAddressPop.getValue());
                cst.setString(13, (String)txtPostalAddressPop.getValue());
                cst.setString(14, (String)txtBranchKBACode.getValue());

                cst.registerOutParameter(15, OracleTypes.VARCHAR);
                cst.setBigDecimal(16, territoryCode);
                cst.execute();
                String err = cst.getString(15);
                GlobalCC.INFORMATIONREPORTING(err);

                ADFUtils.findIterator("fetchBankBranchByBankCodeIterator").executeQuery();
                GlobalCC.refreshUI(tblBankBranches);

                cst.close();
                conn.close();

            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return "Success";
    }

    public String actionUpdateMainBankDetails() {

        if (btnUpdateMainBankDetails.getText().equals("Save")) {

            String status = actionsaveMainBankDetails();

        } else {

            // Check only for the necessary fields
            if (txtBankCode.getValue() == null ||
                txtBankCode.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Bank Code!");

            } else if (txtBankName.getValue() == null ||
                       txtBankName.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Bank Name!");

            } else if (txtBankShortDesc.getValue() == null ||
                       txtBankShortDesc.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Bank short description!");

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.banks_prc(?,?,?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2,
                                      new BigDecimal((String)txtBankCode.getValue()));
                    cst.setString(3, (String)txtBankName.getValue());
                    cst.setString(4, (String)txtBankRemarks.getValue());
                    cst.setString(5, (String)txtBankShortDesc.getValue());
                    cst.setString(6, (String)txtBankDDRCode.getValue());
                    cst.setString(7, (String)txtBankDDFormatDesc.getValue());
                    cst.setString(8, (String)txtBankForwadingCode.getValue());
                    cst.setString(9, (String)txtBankKBACode.getValue());

                    cst.registerOutParameter(10, OracleTypes.VARCHAR);
                    cst.execute();
                    String err = cst.getString(10);
                    GlobalCC.INFORMATIONREPORTING(err);

                    ADFUtils.findIterator("fetchAllBanksIterator").executeQuery();
                    GlobalCC.refreshUI(panelBankDetails);

                    cst.close();
                    conn.close();
                } catch (SQLException e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }

        return null;
    }

    public String deleteBankDetailSection() {
        // Check only for the necessary fields
        if (txtBankCode.getValue() == null ||
            txtBankCode.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Bank Code!");

        } else if (txtBankName.getValue() == null ||
                   txtBankName.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Bank Name!");

        } else if (txtBankShortDesc.getValue() == null ||
                   txtBankShortDesc.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Bank short description!");

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.banks_prc(?,?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)txtBankCode.getValue()));
                cst.setString(3, (String)txtBankName.getValue());
                cst.setString(4, (String)txtBankRemarks.getValue());
                cst.setString(5, (String)txtBankShortDesc.getValue());
                cst.setString(6, (String)txtBankDDRCode.getValue());
                cst.setString(7, (String)txtBankDDFormatDesc.getValue());
                cst.setString(8, (String)txtBankForwadingCode.getValue());
                cst.setString(9, (String)txtBankKBACode.getValue());

                cst.registerOutParameter(10, OracleTypes.VARCHAR);
                cst.execute();
                String err = cst.getString(10);
                GlobalCC.INFORMATIONREPORTING(err);

                ADFUtils.findIterator("fetchAllBanksIterator").executeQuery();
                GlobalCC.refreshUI(panelBankDetails);

                cst.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionsaveMainBankDetails() {

        if (txtBankName.getValue() == null ||
            txtBankName.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Bank Name!");

        } else if (txtBankShortDesc.getValue() == null ||
                   txtBankShortDesc.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Bank short description!");

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.banks_prc(?,?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, (String)txtBankName.getValue());
                cst.setString(4, (String)txtBankRemarks.getValue());
                cst.setString(5, (String)txtBankShortDesc.getValue());
                cst.setString(6, (String)txtBankDDRCode.getValue());
                cst.setString(7, (String)txtBankDDFormatDesc.getValue());
                cst.setString(8, (String)txtBankForwadingCode.getValue());
                cst.setString(9, (String)txtBankKBACode.getValue());

                cst.registerOutParameter(10, OracleTypes.VARCHAR);
                cst.execute();
                String err = cst.getString(10);
                GlobalCC.INFORMATIONREPORTING(err);

                ADFUtils.findIterator("fetchAllBanksIterator").executeQuery();
                GlobalCC.refreshUI(panelBankDetails);

                cst.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        refreshBankDetailSection();
        return "Success";
    }

    public String refreshBankDetailSection() {
        txtBankCode.setValue(null);
        txtBankName.setValue(null);
        txtBankRemarks.setValue(null);
        txtBankShortDesc.setValue(null);
        txtBankDDRCode.setValue(null);
        txtBankDDFormatDesc.setValue(null);
        txtBankForwadingCode.setValue(null);
        txtBankKBACode.setValue(null);

        btnUpdateMainBankDetails.setText("Save");

        return null;
    }

    /* ============= Agency Classification Methods ==================== */

    public void setBtnNewAgencyClass(RichCommandButton btnNewAgencyClass) {
        this.btnNewAgencyClass = btnNewAgencyClass;
    }

    public RichCommandButton getBtnNewAgencyClass() {
        return btnNewAgencyClass;
    }

    public void setBtnEditAgencyClass(RichCommandButton btnEditAgencyClass) {
        this.btnEditAgencyClass = btnEditAgencyClass;
    }

    public RichCommandButton getBtnEditAgencyClass() {
        return btnEditAgencyClass;
    }

    public void setBtnDeleteAgencyClass(RichCommandButton btnDeleteAgencyClass) {
        this.btnDeleteAgencyClass = btnDeleteAgencyClass;
    }

    public RichCommandButton getBtnDeleteAgencyClass() {
        return btnDeleteAgencyClass;
    }

    public void setTblAgencyClass(RichTable tblAgencyClass) {
        this.tblAgencyClass = tblAgencyClass;
    }

    public RichTable getTblAgencyClass() {
        return tblAgencyClass;
    }

    public void setTxtAgencyClassCode(RichInputText txtAgencyClassCode) {
        this.txtAgencyClassCode = txtAgencyClassCode;
    }

    public RichInputText getTxtAgencyClassCode() {
        return txtAgencyClassCode;
    }

    public void setTxtAgencyClassDesc(RichInputText txtAgencyClassDesc) {
        this.txtAgencyClassDesc = txtAgencyClassDesc;
    }

    public RichInputText getTxtAgencyClassDesc() {
        return txtAgencyClassDesc;
    }

    public void setBtnCreateUpdateAgencyClass(RichCommandButton btnCreateUpdateAgencyClass) {
        this.btnCreateUpdateAgencyClass = btnCreateUpdateAgencyClass;
    }

    public RichCommandButton getBtnCreateUpdateAgencyClass() {
        return btnCreateUpdateAgencyClass;
    }

    /**
     * @param selectionEvent
     */
    public void tblAgencyClassSelectionListener(SelectionEvent selectionEvent) {
        btnEditAgencyClass.setDisabled(false);
        btnDeleteAgencyClass.setDisabled(false);
        GlobalCC.refreshUI(btnEditAgencyClass);
        GlobalCC.refreshUI(btnDeleteAgencyClass);
    }

    /**
     * @return
     */
    public String actionNewAgencyClass() {

        // Clear the text fileds
        txtAgencyClassCode.setValue(null);
        txtAgencyClassDesc.setValue(null);
        btnCreateUpdateAgencyClass.setText("Save");

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:agencyClassPopup').show(hints);");

        return null;
    }

    /**
     * @return
     */
    public String actionEditAgencyClass() {

        Object key2 = tblAgencyClass.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAgencyClassCode.setValue(nodeBinding.getAttribute("code"));
            txtAgencyClassDesc.setValue(nodeBinding.getAttribute("description"));
            btnCreateUpdateAgencyClass.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:agencyClassPopup" + "').show(hints);");
            btnEditAgencyClass.setDisabled(true);
            btnDeleteAgencyClass.setDisabled(true);
            GlobalCC.refreshUI(btnEditAgencyClass);
            GlobalCC.refreshUI(btnDeleteAgencyClass);
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    /**
     * @return
     */
    public String actionDeleteAgencyClass() {

        Object key2 = tblAgencyClass.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String Query =
                "begin TQC_SETUPS_PKG.agency_classes_prc(?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal(nodeBinding.getAttribute("code").toString()));
                cst.setString(3,
                              nodeBinding.getAttribute("description").toString());

                cst.registerOutParameter(4, OracleTypes.VARCHAR);
                cst.execute();
                String err = cst.getString(4);
                //GlobalCC.INFORMATIONREPORTING(err);

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllAgencyClassesIterator").executeQuery();
                GlobalCC.refreshUI(tblAgencyClass);
                if (err != null) {
                    GlobalCC.INFORMATIONREPORTING(err);
                } else {
                    String message = "Record DELETED Successfully!";

                    GlobalCC.INFORMATIONREPORTING(message);
                }
                btnEditAgencyClass.setDisabled(true);
                btnDeleteAgencyClass.setDisabled(true);
                GlobalCC.refreshUI(btnEditAgencyClass);
                GlobalCC.refreshUI(btnDeleteAgencyClass);
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    /**
     * @return
     */
    public String actionCreateUpdateAgencyClass() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateAgencyClass.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewAgencyClass();

        } else {

            // Proceed to do an update
            if (txtAgencyClassCode.getValue() == null ||
                txtAgencyClassCode.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Agency Class Code!");

            } else if (txtAgencyClassDesc.getValue() == null ||
                       txtAgencyClassDesc.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Agency Class description!");

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.agency_classes_prc(?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2,
                                      new BigDecimal((String)txtAgencyClassCode.getValue()));
                    cst.setString(3, (String)txtAgencyClassDesc.getValue());

                    cst.registerOutParameter(4, OracleTypes.VARCHAR);
                    cst.execute();
                    String err = cst.getString(4);
                    //GlobalCC.INFORMATIONREPORTING(err);

                    cst.close();
                    conn.commit();
                    conn.close();


                    if (err != null) {
                        GlobalCC.INFORMATIONREPORTING(err);
                        return null;
                    } else {
                        String message = "Record UPDATED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);
                        ADFUtils.findIterator("fetchAllAgencyClassesIterator").executeQuery();
                        GlobalCC.refreshUI(tblAgencyClass);

                        btnEditAgencyClass.setDisabled(true);
                        btnDeleteAgencyClass.setDisabled(true);
                        GlobalCC.refreshUI(btnEditAgencyClass);
                        GlobalCC.refreshUI(btnDeleteAgencyClass);
                        ExtendedRenderKitService erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                        erkService.addScript(FacesContext.getCurrentInstance(),
                                             "var hints = {autodismissNever:false}; " +
                                             "AdfPage.PAGE.findComponent('" +
                                             "pt1:agencyClassPopup" +
                                             "').hide(hints);");
                    }

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    /**
     * @return
     */
    public String actionCreateNewAgencyClass() {

        if (txtAgencyClassDesc.getValue() == null ||
            txtAgencyClassDesc.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Agency Class description!");

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.agency_classes_prc(?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, txtAgencyClassDesc.getValue().toString());

                cst.registerOutParameter(4, OracleTypes.VARCHAR);
                cst.execute();
                String err = cst.getString(4);
                //GlobalCC.INFORMATIONREPORTING(err);

                cst.close();
                conn.commit();
                conn.close();


                if (err != null) {
                    GlobalCC.INFORMATIONREPORTING(err);
                    return null;
                } else {

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                    ADFUtils.findIterator("fetchAllAgencyClassesIterator").executeQuery();
                    GlobalCC.refreshUI(tblAgencyClass);
                    btnEditAgencyClass.setDisabled(true);
                    btnDeleteAgencyClass.setDisabled(true);
                    GlobalCC.refreshUI(btnEditAgencyClass);
                    GlobalCC.refreshUI(btnDeleteAgencyClass);
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:agencyClassPopup" +
                                         "').hide(hints);");
                }


            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return "Success";
    }

    /* =================== Currency Definitions Methods ===================*/

    public void setTreeCurrency(RichTree treeCurrency) {
        this.treeCurrency = treeCurrency;
    }

    public RichTree getTreeCurrency() {
        return treeCurrency;
    }

    public void setPanelCurrencyDetails(RichPanelBox panelCurrencyDetails) {
        this.panelCurrencyDetails = panelCurrencyDetails;
    }

    public RichPanelBox getPanelCurrencyDetails() {
        return panelCurrencyDetails;
    }

    public void setTxtCurrencyCode(RichInputText txtCurrencyCode) {
        this.txtCurrencyCode = txtCurrencyCode;
    }

    public RichInputText getTxtCurrencyCode() {
        return txtCurrencyCode;
    }

    public void setTxtCurrencySymbol(RichInputText txtCurrencySymbol) {
        this.txtCurrencySymbol = txtCurrencySymbol;
    }

    public RichInputText getTxtCurrencySymbol() {
        return txtCurrencySymbol;
    }

    public void setTxtCurrencyDesc(RichInputText txtCurrencyDesc) {
        this.txtCurrencyDesc = txtCurrencyDesc;
    }

    public RichInputText getTxtCurrencyDesc() {
        return txtCurrencyDesc;
    }

    public void setTxtCurrencyRound(RichInputText txtCurrencyRound) {
        this.txtCurrencyRound = txtCurrencyRound;
    }

    public RichInputText getTxtCurrencyRound() {
        return txtCurrencyRound;
    }

    public void setBtnCreateUpdateCurrency(RichCommandButton btnCreateUpdateCurrency) {
        this.btnCreateUpdateCurrency = btnCreateUpdateCurrency;
    }

    public RichCommandButton getBtnCreateUpdateCurrency() {
        return btnCreateUpdateCurrency;
    }

    public void setBtnNewCurrency(RichCommandButton btnNewCurrency) {
        this.btnNewCurrency = btnNewCurrency;
    }

    public RichCommandButton getBtnNewCurrency() {
        return btnNewCurrency;
    }

    /**
     * @param selectionEvent
     */
    public void treeCurrencySelectionListener(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {

                for (Object treeRowKey : keys) {
                    treeCurrency.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeCurrency.getRowData();

                    txtCurrencyCode.setValue(nd.getRow().getAttribute("code"));
                    txtCurrencySymbol.setValue(nd.getRow().getAttribute("symbol"));
                    txtCurrencyDesc.setValue(nd.getRow().getAttribute("description"));
                    txtCurrencyRound.setValue(nd.getRow().getAttribute("round"));
                    btnCreateUpdateCurrency.setText("Update");

                    // Set the Currency code on the popup dialog box as well,
                    // to be used when creating or updating a denomination
                    txtCurrDenomCode.setValue(nd.getRow().getAttribute("code"));

                    session.setAttribute("currencyCode",
                                         nd.getRow().getAttribute("code"));

                    // Refresh the tree
                    ADFUtils.findIterator("fetchAllCurrenciesIterator").executeQuery();
                    GlobalCC.refreshUI(treeCurrency);
                    GlobalCC.refreshUI(panelCurrencyDetails);

                    // Refresh the table
                    ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
                    GlobalCC.refreshUI(tblCurrencyDenominations);
                }
            }
        }
    }

    public String actionDeleteCurrency() {
        BaseSetupDAO baseSetupDAO = new BaseSetupDAO();
        if (baseSetupDAO.fetchCurrencyDenominationsByCurrency().size() > 0) {
            GlobalCC.errorValueNotEntered("The selected Currency has Currency Denominations defined for it. You need to delete them first.");
        } else {
            // Proceed to do an update
            if (txtCurrencyCode.getValue() == null ||
                txtCurrencyCode.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Code!");

            } else if (txtCurrencySymbol.getValue() == null ||
                       txtCurrencySymbol.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Symbol!");

            } else if (txtCurrencyDesc.getValue() == null ||
                       txtCurrencyDesc.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Description!");

            } else if (txtCurrencyRound.getValue() == null ||
                       txtCurrencyRound.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Round!");

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.currencies_prc(?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "D");
                    cst.setBigDecimal(2,
                                      new BigDecimal((String)txtCurrencyCode.getValue()));
                    cst.setString(3, (String)txtCurrencySymbol.getValue());
                    cst.setString(4, (String)txtCurrencyDesc.getValue());
                    cst.setString(5, (String)txtCurrencyRound.getValue());

                    cst.registerOutParameter(6, OracleTypes.VARCHAR);
                    cst.execute();
                    String err = cst.getString(6);
                    GlobalCC.INFORMATIONREPORTING(err);

                    ADFUtils.findIterator("fetchAllCurrenciesIterator").executeQuery();
                    GlobalCC.refreshUI(treeCurrency);

                    cst.close();
                    conn.close();
                } catch (SQLException e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    /**
     * @return
     */
    public String actionCreateUpdateCurrency() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateCurrency.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewCurrency();

        } else {

            // Proceed to do an update
            if (txtCurrencyCode.getValue() == null ||
                txtCurrencyCode.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Code!");

            } else if (txtCurrencySymbol.getValue() == null ||
                       txtCurrencySymbol.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Symbol!");

            } else if (txtCurrencyDesc.getValue() == null ||
                       txtCurrencyDesc.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Description!");

                /*} else if (txtCurrencyRound.getValue() == null ||
                       txtCurrencyRound.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Round!");*/

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.currencies_prc(?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2,
                                      new BigDecimal((String)txtCurrencyCode.getValue()));
                    cst.setString(3, (String)txtCurrencySymbol.getValue());
                    cst.setString(4, (String)txtCurrencyDesc.getValue());
                    cst.setString(5, (String)txtCurrencyRound.getValue());

                    cst.registerOutParameter(6, OracleTypes.VARCHAR);
                    cst.execute();
                    String err = cst.getString(6);
                    GlobalCC.INFORMATIONREPORTING(err);

                    ADFUtils.findIterator("fetchAllCurrenciesIterator").executeQuery();
                    GlobalCC.refreshUI(treeCurrency);

                    cst.close();
                    conn.close();
                } catch (SQLException e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    /**
     * @return
     */
    public String actionCreateNewCurrency() {

        if (txtCurrencySymbol.getValue() == null ||
            txtCurrencySymbol.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Currency Symbol!");

        } else if (txtCurrencyDesc.getValue() == null ||
                   txtCurrencyDesc.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Currency Description!");

            /*} else if (txtCurrencyRound.getValue() == null ||
                   txtCurrencyRound.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Currency Round!");*/

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.currencies_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, (String)txtCurrencySymbol.getValue());
                cst.setString(4, (String)txtCurrencyDesc.getValue());
                cst.setString(5, (String)txtCurrencyRound.getValue());

                cst.registerOutParameter(6, OracleTypes.VARCHAR);
                cst.execute();
                String err = cst.getString(6);
                GlobalCC.INFORMATIONREPORTING(err);

                ADFUtils.findIterator("fetchAllCurrenciesIterator").executeQuery();
                GlobalCC.refreshUI(treeCurrency);

                cst.close();
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    /**
     * @return
     */
    public String actionNewCurrency() {

        // Clear the text fileds
        txtCurrencyCode.setValue("");
        txtCurrencySymbol.setValue("");
        txtCurrencyDesc.setValue("");
        txtCurrencyRound.setValue("");
        btnCreateUpdateCurrency.setText("Save");
        return null;
    }

    public void tblCurrencyDenominationSelectionListener(SelectionEvent selectionEvent) {
        // Add event code here...
    }

    /**
     * @return
     */
    public String btnCreateUpdateCurrDenom() {
        // Add event code here...
        return null;
    }

    /**
     * @return
     */
    public String actionCreateUpdateCurrDenom() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateCurrDenom.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewCurrencyDenomination();

        } else {

            // Proceed to do an update
            if (txtDenomCode.getValue() == null ||
                txtDenomCode.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("The Denomination Code is empty!");

            } else if (txtCurrDenomCode.getValue() == null ||
                       txtCurrDenomCode.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Code!");

            } else if (txtDenomValue.getValue() == null ||
                       txtDenomValue.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Denomination Value!");

            } else if (txtDenomName.getValue() == null ||
                       txtDenomName.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Denomination Name!");

            } else if (txtDenomWEF.getValue() == null ||
                       txtDenomWEF.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Denomination WEF Date!");

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.currencies_denominations_prc(?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2,
                                      new BigDecimal((String)txtDenomCode.getValue()));
                    cst.setBigDecimal(3,
                                      new BigDecimal((String)txtCurrDenomCode.getValue()));
                    cst.setString(4, (String)txtDenomValue.getValue());
                    cst.setString(5, (String)txtDenomName.getValue());
                    cst.setDate(6,
                                new java.sql.Date(((java.util.Date)txtDenomWEF.getValue()).getTime()));

                    cst.registerOutParameter(7, OracleTypes.VARCHAR);
                    cst.execute();
                    String err = cst.getString(7);
                    GlobalCC.INFORMATIONREPORTING(err);

                    ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
                    GlobalCC.refreshUI(tblCurrencyDenominations);

                    cst.close();
                    conn.close();
                } catch (SQLException e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    /**
     * @return
     */
    public String actionCreateNewCurrencyDenomination() {

        if (txtCurrDenomCode.getValue() == null ||
            txtCurrDenomCode.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Currency Code!");

        } else if (txtDenomValue.getValue() == null ||
                   txtDenomValue.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Denomination Value!");

        } else if (txtDenomName.getValue() == null ||
                   txtDenomName.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Denomination Name!");

        } else if (txtDenomWEF.getValue() == null ||
                   txtDenomWEF.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Denomination WEF Date!");

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.currencies_denominations_prc(?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, (String)txtCurrDenomCode.getValue());
                cst.setString(4, (String)txtDenomValue.getValue());
                cst.setString(5, (String)txtDenomName.getValue());
                cst.setDate(6,
                            new java.sql.Date(((java.util.Date)txtDenomWEF.getValue()).getTime()));

                cst.registerOutParameter(7, OracleTypes.VARCHAR);
                cst.execute();
                String err = cst.getString(7);
                GlobalCC.INFORMATIONREPORTING(err);

                ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
                GlobalCC.refreshUI(tblCurrencyDenominations);

                cst.close();
                conn.close();
            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return "Success";
    }

    public String actionNewCurrencyDenom() {

        if (txtCurrencyCode.getValue() == null ||
            txtCurrencyCode.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("You first need to select a currency!");

        } else {

            // Clear the text fileds
            txtDenomCode.setValue("");
            txtCurrDenomCode.setValue(txtCurrencyCode.getValue());
            txtDenomValue.setValue("");
            txtDenomName.setValue("");
            txtDenomWEF.setValue("");
            btnCreateUpdateCurrDenom.setText("Save");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:currencyDenomPopup').show(hints);");
        }
        return null;
    }

    /**
     * @return
     */
    public String actionEditCurrencyDenom() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator");
        RowKeySet set = tblCurrencyDenominations.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            // Set the text field values
            txtDenomCode.setValue(rows.getAttribute("code"));
            txtCurrDenomCode.setValue(rows.getAttribute("currencyCode"));
            txtDenomValue.setValue(rows.getAttribute("value"));
            txtDenomName.setValue(rows.getAttribute("name"));
            txtDenomWEF.setValue(rows.getAttribute("wef"));
            btnCreateUpdateCurrDenom.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:currencyDenomPopup" + "').show(hints);");
        }
        return null;
    }

    /**
     * @return
     */
    public String actionDeleteCurrencyDenom() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator");
        RowKeySet set = tblCurrencyDenominations.getSelectedRowKeys();
        Iterator row = set.iterator();

        String Query =
            "begin TQC_SETUPS_PKG.currencies_denominations_prc(?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)rows.getAttribute("code")));
                cst.setBigDecimal(3,
                                  new BigDecimal((String)rows.getAttribute("currencyCode")));
                cst.setString(4, (String)rows.getAttribute("value"));
                cst.setString(5, (String)rows.getAttribute("name"));
                cst.setDate(6,
                            new java.sql.Date(((java.util.Date)rows.getAttribute("wef")).getTime()));

                cst.registerOutParameter(7, OracleTypes.VARCHAR);
                cst.execute();
                String err = cst.getString(7);
                GlobalCC.INFORMATIONREPORTING(err);

                cst.close();
                conn.close();

            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        ADFUtils.findIterator("fetchCurrencyDenominationsByCurrencyIterator").executeQuery();
        GlobalCC.refreshUI(tblCurrencyDenominations);
        return null;
    }

    /* ============== Exchange Rates Methods ========================= */

    public void setTreeERCurrency(RichTree treeERCurrency) {
        this.treeERCurrency = treeERCurrency;
    }

    public RichTree getTreeERCurrency() {
        return treeERCurrency;
    }

    public void setTableERExchangeRates(RichTable tableERExchangeRates) {
        this.tableERExchangeRates = tableERExchangeRates;
    }

    public RichTable getTableERExchangeRates() {
        return tableERExchangeRates;
    }

    /**
     * This method is called whenever a Currency is selected in the
     * <code>treeERCurrency</code> UI component. It populates the
     * <code>tableERExchangeRates</code> with a list of all the exchange
     * rates for that currency.
     *
     * @param selectionEvent object passed in by ADF Faces when configuring
     * this method to become the selection listener of the
     * <code>treeERCurrency</code>
     */
    public void treeERCurrencySelectionListener(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {

                for (Object treeRowKey : keys) {
                    treeERCurrency.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeERCurrency.getRowData();

                    txtBaseCurrCodePop.setValue(nd.getRow().getAttribute("code"));
                    session.setAttribute("currencyCode",
                                         nd.getRow().getAttribute("code"));
                    System.out.println("Currency Code : " +
                                       session.getAttribute("currencyCode").toString());

                    ADFUtils.findIterator("fetchCurrencyRatesByCurrencyIterator").executeQuery();
                    GlobalCC.refreshUI(tableERExchangeRates);

                    ADFUtils.findIterator("fetchCurrenciesExcludeCurrencyIterator").executeQuery();
                    GlobalCC.refreshUI(tableBaseCurrencyPopup);
                }
            }
        }
    }

    public void setBtnNewExchangeRate(RichCommandButton btnNewExchangeRate) {
        this.btnNewExchangeRate = btnNewExchangeRate;
    }

    public RichCommandButton getBtnNewExchangeRate() {
        return btnNewExchangeRate;
    }

    public void setBtnEditExchangeRate(RichCommandButton btnEditExchangeRate) {
        this.btnEditExchangeRate = btnEditExchangeRate;
    }

    public RichCommandButton getBtnEditExchangeRate() {
        return btnEditExchangeRate;
    }

    public void setBtnDeleteExchangeRate(RichCommandButton btnDeleteExchangeRate) {
        this.btnDeleteExchangeRate = btnDeleteExchangeRate;
    }

    public RichCommandButton getBtnDeleteExchangeRate() {
        return btnDeleteExchangeRate;
    }

    public String actionNewExchangeRate() {

        if (txtBaseCurrCodePop.getValue() == null ||
            txtBaseCurrCodePop.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("You first need to select a currency!");

        } else {

            // Clear the text fileds
            txtExchangeRateCode.setValue("");
            txtBaseCurrPop.setValue("");
            txtRatePop.setValue("");
            txtDatePop.setValue("");
            btnCreateUpdateExchangeRate.setText("Save");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:exchangeRatePopup').show(hints);");
        }
        return null;
    }

    public String actionEditExchangeRate() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchCurrencyRatesByCurrencyIterator");
        RowKeySet set = tableERExchangeRates.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            txtBaseCurrCodePop.setValue(rows.getAttribute("code"));
            txtBaseCurrPop.setValue(rows.getAttribute("baseCurrencyCode"));

            // Clear the text fileds
            txtExchangeRateCode.setValue(rows.getAttribute("code"));
            txtCurrCodePop.setValue(rows.getAttribute("currencyCode"));
            txtBaseCurrCodePop.setValue(rows.getAttribute("baseCurrencyCode"));
            txtBaseCurrPop.setValue(rows.getAttribute("baseCurrencyName"));
            txtRatePop.setValue(rows.getAttribute("currRate"));
            txtDatePop.setValue(rows.getAttribute("currDate"));
            btnCreateUpdateExchangeRate.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:exchangeRatePopup').show(hints);");
        }

        return null;
    }

    public String actionDeleteExchangeRate() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchCurrencyRatesByCurrencyIterator");
        RowKeySet set = tableERExchangeRates.getSelectedRowKeys();
        Iterator row = set.iterator();

        String Query =
            "begin TQC_SETUPS_PKG.currency_rates_prc(?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)rows.getAttribute("code")));
                cst.setBigDecimal(3,
                                  new BigDecimal((String)rows.getAttribute("currencyCode")));
                cst.setString(4, (String)rows.getAttribute("currRate"));
                cst.setDate(5,
                            new java.sql.Date(((java.util.Date)rows.getAttribute("currDate")).getTime()));
                cst.setString(6,
                              (String)rows.getAttribute("baseCurrencyCode"));

                cst.execute();

                cst.close();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        ADFUtils.findIterator("fetchCurrencyRatesByCurrencyIterator").executeQuery();
        GlobalCC.refreshUI(tableERExchangeRates);

        return null;
    }

    public void setTxtExchangeRateCode(RichInputText txtExchangeRateCode) {
        this.txtExchangeRateCode = txtExchangeRateCode;
    }

    public RichInputText getTxtExchangeRateCode() {
        return txtExchangeRateCode;
    }

    public void setTxtCurrCodePop(RichInputText txtCurrCodePop) {
        this.txtCurrCodePop = txtCurrCodePop;
    }

    public RichInputText getTxtCurrCodePop() {
        return txtCurrCodePop;
    }

    public void setTxtBaseCurrPop(RichInputText txtBaseCurrPop) {
        this.txtBaseCurrPop = txtBaseCurrPop;
    }

    public RichInputText getTxtBaseCurrPop() {
        return txtBaseCurrPop;
    }

    public void setTxtRatePop(RichInputText txtRatePop) {
        this.txtRatePop = txtRatePop;
    }

    public RichInputText getTxtRatePop() {
        return txtRatePop;
    }

    public void setTxtDatePop(RichInputDate txtDatePop) {
        this.txtDatePop = txtDatePop;
    }

    public RichInputDate getTxtDatePop() {
        return txtDatePop;
    }

    public void setBtnCreateUpdateExchangeRate(RichCommandButton btnCreateUpdateExchangeRate) {
        this.btnCreateUpdateExchangeRate = btnCreateUpdateExchangeRate;
    }

    public RichCommandButton getBtnCreateUpdateExchangeRate() {
        return btnCreateUpdateExchangeRate;
    }

    public void setTableBaseCurrencyPopup(RichTable tableBaseCurrencyPopup) {
        this.tableBaseCurrencyPopup = tableBaseCurrencyPopup;
    }

    public RichTable getTableBaseCurrencyPopup() {
        return tableBaseCurrencyPopup;
    }

    public void tableBaseCurrencyPopupSelectionListener(SelectionEvent selectionEvent) {
        // Add event code here...
    }

    public void setBtnAcceptExchangeRate(RichCommandButton btnAcceptExchangeRate) {
        this.btnAcceptExchangeRate = btnAcceptExchangeRate;
    }

    public RichCommandButton getBtnAcceptExchangeRate() {
        return btnAcceptExchangeRate;
    }

    public void setTxtBaseCurrCodePop(RichInputText txtBaseCurrCodePop) {
        this.txtBaseCurrCodePop = txtBaseCurrCodePop;
    }

    public RichInputText getTxtBaseCurrCodePop() {
        return txtBaseCurrCodePop;
    }

    /**
     * @return null
     */
    public String actionAcceptExchangeRate() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchCurrenciesExcludeCurrencyIterator");
        RowKeySet set = tableBaseCurrencyPopup.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {
            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            txtCurrCodePop.setValue(rows.getAttribute("code"));
            txtBaseCurrPop.setValue(rows.getAttribute("description"));

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:exchangeRatePopup" + "').show(hints);");
        }

        return null;
    }

    /**
     * @return null
     */
    public String actionCreateUpdateExchangeRate() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateExchangeRate.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewExchangeRate();

        } else {

            // Proceed to do an update
            if (txtExchangeRateCode.getValue() == null ||
                txtExchangeRateCode.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Exchange Code!");

            } else if (txtCurrCodePop.getValue() == null ||
                       txtCurrCodePop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Currency Code!");

            } else if (txtBaseCurrCodePop.getValue() == null ||
                       txtBaseCurrCodePop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Base Currency Code!");

            } else if (txtRatePop.getValue() == null ||
                       txtRatePop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Exchange Rate!");

            } else if (txtDatePop.getValue() == null ||
                       txtDatePop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Date!");

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.currency_rates_prc(?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2,
                                      new BigDecimal(txtExchangeRateCode.getValue().toString()));
                    cst.setBigDecimal(3,
                                      new BigDecimal((String)txtCurrCodePop.getValue()));
                    cst.setString(4, txtRatePop.getValue().toString());
                    cst.setDate(5,
                                new java.sql.Date(((java.util.Date)txtDatePop.getValue()).getTime()));
                    cst.setString(6, txtBaseCurrCodePop.getValue().toString());

                    cst.execute();

                    ADFUtils.findIterator("fetchCurrencyRatesByCurrencyIterator").executeQuery();
                    GlobalCC.refreshUI(tableERExchangeRates);

                    cst.close();
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionCreateNewExchangeRate() {

        if (txtCurrCodePop.getValue() == null ||
            txtCurrCodePop.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Currency Code!");

        } else if (txtBaseCurrCodePop.getValue() == null ||
                   txtBaseCurrCodePop.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Base Currency Code!");

        } else if (txtRatePop.getValue() == null ||
                   txtRatePop.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Exchange Rate!");

        } else if (txtDatePop.getValue() == null ||
                   txtDatePop.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Date!");

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.currency_rates_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setBigDecimal(3,
                                  new BigDecimal(txtCurrCodePop.getValue().toString()));
                cst.setString(4, txtRatePop.getValue().toString());
                cst.setDate(5,
                            new java.sql.Date(((java.util.Date)txtDatePop.getValue()).getTime()));
                cst.setString(6, (String)txtBaseCurrCodePop.getValue());

                cst.execute();

                ADFUtils.findIterator("fetchCurrencyRatesByCurrencyIterator").executeQuery();
                GlobalCC.refreshUI(tableERExchangeRates);

                cst.close();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return "Success";
    }

    /* ========================= Sectors Methods ============================*/

    public void setTblSectors(RichTable tblSectors) {
        this.tblSectors = tblSectors;
    }

    public RichTable getTblSectors() {
        return tblSectors;
    }

    public void setTxtSectorShtDesc(RichInputText txtSectorShtDesc) {
        this.txtSectorShtDesc = txtSectorShtDesc;
    }

    public RichInputText getTxtSectorShtDesc() {
        return txtSectorShtDesc;
    }

    public void setTxtSectorName(RichInputText txtSectorName) {
        this.txtSectorName = txtSectorName;
    }

    public RichInputText getTxtSectorName() {
        return txtSectorName;
    }

    public void setBtnCreateUpdateSector(RichCommandButton btnCreateUpdateSector) {
        this.btnCreateUpdateSector = btnCreateUpdateSector;
    }

    public RichCommandButton getBtnCreateUpdateSector() {
        return btnCreateUpdateSector;
    }

    public void setTxtSectorCode(RichInputText txtSectorCode) {
        this.txtSectorCode = txtSectorCode;
    }

    public RichInputText getTxtSectorCode() {
        return txtSectorCode;
    }

    /**
     * Prepare the fields in the popup to add a new Sector
     *
     * @return NULL
     */
    public String actionNewSector() {

        // Clear the text fileds
        txtSectorCode.setValue("");
        txtSectorShtDesc.setValue("");
        txtSectorName.setValue("");
        btnCreateUpdateSector.setText("CREATE");

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:sectorPopup').show(hints);");
        return null;
    }

    public String actionNewClientType() {
        session.setAttribute("action", "A");
        // Clear the text fileds
        txtClientCategory.setValue(null);
        txtClientType.setValue(null);
        txtClientPerson.setValue(null);
        GlobalCC.showPopup("pt1:ClientTypePop");
        return null;
    }
    public String actionNewAgentType() {
        session.setAttribute("action", "A");
        // Clear the text fileds
        txtAgentTypeShtDesc.setValue("");
        txtAgentType.setValue("");
        ADFUtils.findIterator("fetchAccountTypesIterator").executeQuery();
        GlobalCC.refreshUI(tblAccountTypesPop);
        GlobalCC.showPopup("pt1:AgentTypePop");
        return null;
    }

    /**
     * Set the popup fields with the values selected from the table row, then
     * open the popup to allow for editing.
     *
     * @return NULL
     */
    public String actionEditSector() {

        Object key2 = tblSectors.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSectorCode.setValue(nodeBinding.getAttribute("code"));
            txtSectorShtDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            txtSectorName.setValue(nodeBinding.getAttribute("name"));
            btnCreateUpdateSector.setText("UPDATE");

            GlobalCC.showPop("pt1:sectorPopup");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String actionEditClientType() {
        session.setAttribute("action", "E");
        Object key2 = txtClientTypeTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtClientType.setValue(nodeBinding.getAttribute("CLNTY_CLNT_TYPE"));
            txtClientCategory.setValue(nodeBinding.getAttribute("CLNTY_CATEGORY"));
            session.setAttribute("CLNTY_CODE",  nodeBinding.getAttribute("CLNTY_CODE"));
            txtClientPerson.setValue(nodeBinding.getAttribute("CLNTY_PERSON"));
            GlobalCC.showPopup("pt1:ClientTypePop");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }
    
    public String actionEditAgentType() {
        session.setAttribute("action", "E");
        Object key2 = txtAgentTypeTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;

        if (r != null) {
            txtAgentType.setValue(r.getAttribute("AGNTY_TYPE"));
            txtAgentTypeShtDesc.setValue(r.getAttribute("AGNTY_TYPE_SHT_DESC"));
            txtAccountType.setValue(r.getAttribute("ACT_ACCOUNT_TYPE"));
            session.setAttribute("AGNTY_CODE", r.getAttribute("AGNTY_CODE"));
            session.setAttribute("AGNTY_ACT_CODE", r.getAttribute("AGNTY_ACT_CODE"));
            ADFUtils.findIterator("fetchAccountTypesIterator").executeQuery();
            GlobalCC.refreshUI(tblAccountTypesPop);
            GlobalCC.showPopup("pt1:AgentTypePop");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }
    public String actionDeleteAgentType() {
        session.setAttribute("action", "D");
        Object key2 = txtAgentTypeTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;

        if (r != null) {
            session.setAttribute("AGNTY_CODE", r.getAttribute("AGNTY_CODE"));
            actionCreateUpdateDeleteAgentType();
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    /**
     * Deletes the selected sector from the database
     *
     * @return NULL
     */
    public String actionDeleteClientType() {
        session.setAttribute("action", "D");
        Object key2 = txtClientTypeTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
        

            session.setAttribute("CLNTY_CODE",
                                 nodeBinding.getAttribute("CLNTY_CODE"));
            String Query =
                "begin TQC_CLIENTS_PKG.client_type_proc(?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  (BigDecimal)nodeBinding.getAttribute("CLNTY_CODE"));
                cst.setString(3, null);
                cst.setString(4, null);
                cst.setString(5, null);
                cst.execute();
                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("findClientTypesIterator").executeQuery();
                GlobalCC.refreshUI(txtClientTypeTbl);
                String message = "Record Deleted Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }
    
    /*
     * public String actionDeleteAgentType() {
        session.setAttribute("action", "D");
        Object key2 = txtAgentTypeTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;

        if (r != null) {
            session.setAttribute("AGNTY_CODE",r.getAttribute("AGNTY_CODE"));
                                 
            String Query =
                "begin TQC_SETUPS_PKG.agent_type_proc(?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,(BigDecimal)r.getAttribute("AGNTY_CODE"));
                cst.setString(3, null);
                cst.setString(4, null);
                cst.execute();
                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllAgentTypesIterator").executeQuery();
                GlobalCC.refreshUI(txtAgentTypeTbl);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }
*/
    public String actionDeleteSector() {
        Object key2 = tblSectors.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String Query = "begin TQC_SETUPS_PKG.sectors_prc(?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)nodeBinding.getAttribute("code")));
                cst.setString(3,
                              (String)nodeBinding.getAttribute("shortDesc"));
                cst.setString(4, (String)nodeBinding.getAttribute("name"));

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllSectorsIterator").executeQuery();
                GlobalCC.refreshUI(tblSectors);

                String message = "Record Deleted Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    /**
     * Updates a given sector with the new values and persists it in the
     * database
     *
     * @return NULL
     */
    public String actionCreateUpdateSector() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateSector.getText().equals("CREATE")) {
            // Proceed to save
            String status = actionCreateNewSector();

        } else {

           if (txtSectorShtDesc.getValue() == null ||
                       txtSectorShtDesc.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Sector Short Description!");

            } else if (txtSectorName.getValue() == null ||
                       txtSectorName.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Sector Name!");

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.sectors_prc(?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2, GlobalCC.checkBDNullValues(txtSectorCode.getValue()));
                    cst.setString(3, (String)txtSectorShtDesc.getValue());
                    cst.setString(4, (String)txtSectorName.getValue());
                    cst.execute();

                    cst.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchAllSectorsIterator").executeQuery();
                    GlobalCC.refreshUI(tblSectors);
                    GlobalCC.dismissPopUp("pt1", "sectorPopup");
                    String message = "Record UPDATED successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionCreateUpdateClientType() {
        if (txtClientCategory.getValue() == null ||
            txtClientCategory.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Category!");
            return null;

        } 
        if (txtClientType.getValue() == null || txtClientType.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Client Type!");
            return null;
          }
       
        String clientPerson = GlobalCC.checkNullValues(txtClientPerson.getValue());

        if (clientPerson == null  ) {
            GlobalCC.INFORMATIONREPORTING("Enter the Client Person!");
            return null;
          }
            String Query =
                "begin TQC_CLIENTS_PKG.client_type_proc(?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, (String)session.getAttribute("action"));
                if (session.getAttribute("action") == "A") {
                    cst.setBigDecimal(2, null);
                } else {
                    cst.setBigDecimal(2,
                                      (BigDecimal)session.getAttribute("CLNTY_CODE"));
                }
                cst.setString(3, (String)txtClientType.getValue());
                cst.setString(4, (String)txtClientCategory.getValue());
                cst.setString(5, clientPerson);
                
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("findClientTypesIterator").executeQuery();
                GlobalCC.refreshUI(txtClientTypeTbl);
                GlobalCC.dismissPopUp("pt1", "ClientTypePop");
                String message = "Record UPDATED successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        return null;
    }
    
    public String actionCreateUpdateDeleteAgentType() {
 
            if (((String)session.getAttribute("action")) != "D") 
            {
                if (GlobalCC.checkNullValues(txtAgentType.getValue()) == null ) {
                    GlobalCC.INFORMATIONREPORTING("Enter the Agent Type!");
                    return null;
                } 
                if (GlobalCC.checkNullValues(txtAgentTypeShtDesc.getValue()) == null ) {
                    GlobalCC.INFORMATIONREPORTING("Enter the Short Desc!");
                    return null;
                }
                if (GlobalCC.checkNullValues(txtAccountType.getValue()) == null ) {
                    GlobalCC.INFORMATIONREPORTING("Enter the Account Type!");
                    return null;
                }
           }
        
        String Query = "begin TQC_SETUPS_PKG.agent_type_proc(?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, (String)session.getAttribute("action"));
            if (session.getAttribute("action") == "A") {
                cst.setBigDecimal(2, null);
            } else {
                cst.setBigDecimal(2,(BigDecimal)session.getAttribute("AGNTY_CODE"));             
            }
            cst.setString(3, (String)txtAgentType.getValue());
            cst.setString(4, (String)txtAgentTypeShtDesc.getValue());
            cst.setBigDecimal(5, (BigDecimal)session.getAttribute("AGNTY_ACT_CODE")); 
            cst.execute();

            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchAllAgentTypesIterator").executeQuery();
            GlobalCC.refreshUI(txtAgentTypeTbl);
            GlobalCC.dismissPopUp("pt1", "AgentTypePop");
            
            String message="Record UPDATED successfully!";
            
            if(session.getAttribute("action") == "A")
            {
              message = "Record ADDED successfully!";
            }
            if(session.getAttribute("action") == "E")
            {
              message = "Record UPDATED successfully!";
            }
            if(session.getAttribute("action") == "D") 
            {
              message = "Record DELETED Successfully!";
            }
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
       
        return null;
    }

    /**
     * Creates a new sector and stores it in the database.
     *
     * @return Success message
     */
    public String actionCreateNewSector() {

        if (txtSectorShtDesc.getValue() == null ||
            txtSectorShtDesc.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Short Description!");

        } else if (txtSectorName.getValue() == null ||
                   txtSectorName.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("Enter the Name!");

        } else {

            String Query = "begin TQC_SETUPS_PKG.sectors_prc(?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, (String)txtSectorShtDesc.getValue());
                cst.setString(4, (String)txtSectorName.getValue());
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllSectorsIterator").executeQuery();
                GlobalCC.refreshUI(tblSectors);
                GlobalCC.dismissPopUp("pt1", "sectorPopup");
                String message = "New Record CREATED successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return "Success";
    }

    /* ============== Service Provider Type methods =========================*/

    public void setTblServiceProviderType(RichTable tblServiceProviderType) {
        this.tblServiceProviderType = tblServiceProviderType;
    }

    public RichTable getTblServiceProviderType() {
        return tblServiceProviderType;
    }

    public void setTxtProviderTypeCodePop(RichInputText txtProviderTypeCodePop) {
        this.txtProviderTypeCodePop = txtProviderTypeCodePop;
    }

    public RichInputText getTxtProviderTypeCodePop() {
        return txtProviderTypeCodePop;
    }

    public void setTxtProviderTypeShtDescPop(RichInputText txtProviderTypeShtDescPop) {
        this.txtProviderTypeShtDescPop = txtProviderTypeShtDescPop;
    }

    public RichInputText getTxtProviderTypeShtDescPop() {
        return txtProviderTypeShtDescPop;
    }

    public void setTxtProviderTypeNamePop(RichInputText txtProviderTypeNamePop) {
        this.txtProviderTypeNamePop = txtProviderTypeNamePop;
    }

    public RichInputText getTxtProviderTypeNamePop() {
        return txtProviderTypeNamePop;
    }

    public void setTxtProviderTypeStatus(RichSelectOneChoice txtProviderTypeStatus) {
        this.txtProviderTypeStatus = txtProviderTypeStatus;
    }

    public RichSelectOneChoice getTxtProviderTypeStatus() {
        return txtProviderTypeStatus;
    }

    public void setBtnCreateUpdateProviderType(RichCommandButton btnCreateUpdateProviderType) {
        this.btnCreateUpdateProviderType = btnCreateUpdateProviderType;
    }

    public RichCommandButton getBtnCreateUpdateProviderType() {
        return btnCreateUpdateProviderType;
    }

    public void setTxtProviderTypeWhtxRatePop(RichInputText txtProviderTypeWhtxRatePop) {
        this.txtProviderTypeWhtxRatePop = txtProviderTypeWhtxRatePop;
    }

    public RichInputText getTxtProviderTypeWhtxRatePop() {
        return txtProviderTypeWhtxRatePop;
    }

    public void setTxtProviderVatRatePop(RichInputText txtProviderVatRatePop) {
        this.txtProviderVatRatePop = txtProviderVatRatePop;
    }

    public RichInputText getTxtProviderVatRatePop() {
        return txtProviderVatRatePop;
    }

    public String actionNewServiceProviderType() {

        // Clear the text fileds
        txtProviderTypeCodePop.setValue(null);
        txtProviderTypeShtDescPop.setValue(null);
        txtProviderTypeNamePop.setValue(null);
        txtProviderTypeStatus.setValue(null);
        txtProviderTypeWhtxRatePop.setValue(null);
        txtProviderVatRatePop.setValue(null);
        txtSuffix.setValue(null);
        
        txtProviderTypeWhtxRatePop2.setValue(null);
        txtProviderVatRatePop2.setValue(null);
        txtProviderTypeWhtxRatePop.setValue(null);
        txtProviderVatRatePop.setValue(null);
        
        
        
        btnCreateUpdateProviderType.setText("Save");
        GlobalCC.showPop("pt1:serviceProviderTypePopup");
        return null;
    }

    public String actionEditServiceProviderType() {

        Object key2 = tblServiceProviderType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtProviderTypeCodePop.setValue(nodeBinding.getAttribute("sptCode"));
            txtProviderTypeShtDescPop.setValue(nodeBinding.getAttribute("shortDesc"));
            txtProviderTypeNamePop.setValue(nodeBinding.getAttribute("name"));
            txtProviderTypeStatus.setValue(nodeBinding.getAttribute("status"));
            txtProviderTypeWhtxRatePop2.setValue(nodeBinding.getAttribute("whtxRate"));
            txtProviderVatRatePop2.setValue(nodeBinding.getAttribute("vatRate"));
            txtSuffix.setValue(nodeBinding.getAttribute("suffix"));

            btnCreateUpdateProviderType.setText("Update");

            GlobalCC.showPop("pt1:serviceProviderTypePopup");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    /**
     * Deletes a Service Provider Type from the database
     *
     * @return NULL
     */
    public String actionDeleteServiceProviderType() {

        Object key2 = tblServiceProviderType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String Query =
                "begin TQC_SETUPS_PKG.service_provider_types_prc(?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setObject(2, session.getAttribute("vSptCode"));
                                 // new BigDecimal(nodeBinding.getAttribute("sptCode").toString()));
                cst.setString(3,
                              nodeBinding.getAttribute("shortDesc").toString());
                cst.setString(4, nodeBinding.getAttribute("name").toString());
                cst.setString(5,
                              nodeBinding.getAttribute("status").toString());
                cst.setBigDecimal(6, null);
                cst.setBigDecimal(7, null);
                cst.setBigDecimal(8, null);

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllServiceProviderTypesIterator").executeQuery();
                AdfFacesContext.getCurrentInstance().addPartialTarget(tblServiceProviderType);
                
                
                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }


    /**
     * Updates a Provider Type with the new values from the popup
     *
     * @return NULL
     */
    public String actionCreateUpdateProviderType() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateProviderType.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewProviderType();

        } else {

            // Proceed to do an update
            if (txtProviderTypeCodePop.getValue() == null ||
                txtProviderTypeCodePop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("The Provider Type Code is empty!");

            } else if (txtProviderTypeShtDescPop.getValue() == null ||
                       txtProviderTypeShtDescPop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Short Description!");

            } else if (txtProviderTypeNamePop.getValue() == null ||
                       txtProviderTypeNamePop.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Name!");

            } else if (txtProviderTypeStatus.getValue() == null ||
                       txtProviderTypeStatus.getValue().equals("")) {
                GlobalCC.INFORMATIONREPORTING("Enter the Status!");
            }else if(txtSuffix.getValue() == null ||
                    txtSuffix.getValue().equals("")){
                    GlobalCC.INFORMATIONREPORTING("Enter the Suffix!");
            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.service_provider_types_prc(?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setObject(2,   session.getAttribute("vSptCode"));//GlobalCC.checkNullValues(txtProviderTypeCodePop.getValue()));
                    cst.setString(3,
                                  (String)txtProviderTypeShtDescPop.getValue());
                    cst.setString(4,
                                  (String)txtProviderTypeNamePop.getValue());
                    cst.setString(5, (String)txtProviderTypeStatus.getValue());

                    if (txtProviderTypeWhtxRatePop2.getValue() != null &&
                        !(txtProviderTypeWhtxRatePop2.getValue().equals(""))) {
                        cst.setBigDecimal(6,
                                          new BigDecimal(txtProviderTypeWhtxRatePop2.getValue().toString()));
                    } else {
                        cst.setBigDecimal(6, null);
                    }

                    if (txtProviderVatRatePop2.getValue() != null &&
                        !(txtProviderVatRatePop2.getValue().equals(""))) {
                        cst.setBigDecimal(7,
                                          new BigDecimal(txtProviderVatRatePop2.getValue().toString()));
                    } else {
                        cst.setBigDecimal(7, null);
                    }
                    
                    cst.setObject(8, txtSuffix.getValue().toString());

                    cst.execute();
                    cst.close();
                    conn.commit();
                    conn.close();

                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:serviceProviderTypePopup').hide(hints);");

                    ADFUtils.findIterator("fetchAllServiceProviderTypesIterator").executeQuery();
                    AdfFacesContext.getCurrentInstance().addPartialTarget(tblServiceProviderType);

                    String message = "Record UPDATED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                  

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    /**
     * Creates a new Service Provider Type and stores it in the database
     *
     * @return Success message
     */
    public String actionCreateNewProviderType() {

        String shortDesc =
            GlobalCC.checkNullValues(txtProviderTypeShtDescPop.getValue());
        String name =
            GlobalCC.checkNullValues(txtProviderTypeNamePop.getValue());
        String status =
            GlobalCC.checkNullValues(txtProviderTypeStatus.getValue());
        String suffix = GlobalCC.checkNullValues(txtSuffix.getValue());                                                             
      
        if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
            return null;

        } else if (name == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type Name");
            return null;

        } else if (status == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Status");
            return null;
        }else if(suffix == null){
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Suffix");
        } else {

            String Query =
                "begin TQC_SETUPS_PKG.service_provider_types_prc(?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, shortDesc);
                cst.setString(4, name);
                cst.setString(5, status);

                if (txtProviderTypeWhtxRatePop2.getValue() != null &&
                    !(txtProviderTypeWhtxRatePop2.getValue().equals(""))) {
                    cst.setBigDecimal(6,
                                      new BigDecimal(txtProviderTypeWhtxRatePop2.getValue().toString()));
                } else {
                    cst.setBigDecimal(6, null);
                }

                if (txtProviderVatRatePop2.getValue() != null &&
                    !(txtProviderVatRatePop2.getValue().equals(""))) {
                    cst.setBigDecimal(7,
                                      new BigDecimal(txtProviderVatRatePop2.getValue().toString()));
                } else {
                    cst.setBigDecimal(7, null);
                }
                
                cst.setObject(8, suffix);

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:serviceProviderTypePopup').hide(hints);");

                ADFUtils.findIterator("fetchAllServiceProviderTypesIterator").executeQuery();
               AdfFacesContext.getCurrentInstance().addPartialTarget(tblServiceProviderType);

                String message = "Record CREATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);


            

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return "Success";
    }

    /* ===================== Countries methods =============================*/

    public void setTreeCountries(RichTree treeCountries) {
        this.treeCountries = treeCountries;
    }

    public RichTree getTreeCountries() {
        return treeCountries;
    }

    public void setTxtCountryCode(RichInputText txtCountryCode) {
        this.txtCountryCode = txtCountryCode;
    }

    public RichInputText getTxtCountryCode() {
        return txtCountryCode;
    }

    public void setTxtCountryShtDesc(RichInputText txtCountryShtDesc) {
        this.txtCountryShtDesc = txtCountryShtDesc;
    }

    public RichInputText getTxtCountryShtDesc() {
        return txtCountryShtDesc;
    }

    public void setTxtCountryName(RichInputText txtCountryName) {
        this.txtCountryName = txtCountryName;
    }

    public RichInputText getTxtCountryName() {
        return txtCountryName;
    }

    public void setTxtCountryBaseCurrency(RichInputText txtCountryBaseCurrency) {
        this.txtCountryBaseCurrency = txtCountryBaseCurrency;
    }

    public RichInputText getTxtCountryBaseCurrency() {
        return txtCountryBaseCurrency;
    }

    public void setTxtCountryNationality(RichInputText txtCountryNationality) {
        this.txtCountryNationality = txtCountryNationality;
    }

    public RichInputText getTxtCountryNationality() {
        return txtCountryNationality;
    }


    public void setTxtCountryZIPCode(RichInputText txtCountryZIPCode) {
        this.txtCountryZIPCode = txtCountryZIPCode;
    }

    public RichInputText getTxtCountryZIPCode() {
        return txtCountryZIPCode;
    }

    public void setPanelCountryDetails(RichPanelBox panelCountryDetails) {
        this.panelCountryDetails = panelCountryDetails;
    }

    public RichPanelBox getPanelCountryDetails() {
        return panelCountryDetails;
    }

    public void setBtnCreateUpdateCountry(RichCommandButton btnCreateUpdateCountry) {
        this.btnCreateUpdateCountry = btnCreateUpdateCountry;
    }

    public RichCommandButton getBtnCreateUpdateCountry() {
        return btnCreateUpdateCountry;
    }

    public void setBtnCancelCountry(RichCommandButton btnCancelCountry) {
        this.btnCancelCountry = btnCancelCountry;
    }

    public RichCommandButton getBtnCancelCountry() {
        return btnCancelCountry;
    }

    public void setTblCountryTowns(RichTable tblCountryTowns) {
        this.tblCountryTowns = tblCountryTowns;
    }

    public RichTable getTblCountryTowns() {
        return tblCountryTowns;
    }

    public void setTblTownLocations(RichTable tblTownLocations) {
        this.tblTownLocations = tblTownLocations;
    }

    public RichTable getTblTownLocations() {
        return tblTownLocations;
    }

    public void setTxtTownCodePopup(RichInputText txtTownCodePopup) {
        this.txtTownCodePopup = txtTownCodePopup;
    }

    public RichInputText getTxtTownCodePopup() {
        return txtTownCodePopup;
    }

    public void setTxtTownCountryPopup(RichInputText txtTownCountryPopup) {
        this.txtTownCountryPopup = txtTownCountryPopup;
    }

    public RichInputText getTxtTownCountryPopup() {
        return txtTownCountryPopup;
    }

    public void setTxtTownShtDescPopup(RichInputText txtTownShtDescPopup) {
        this.txtTownShtDescPopup = txtTownShtDescPopup;
    }

    public RichInputText getTxtTownShtDescPopup() {
        return txtTownShtDescPopup;
    }

    public void setTxtTownNamePopup(RichInputText txtTownNamePopup) {
        this.txtTownNamePopup = txtTownNamePopup;
    }

    public RichInputText getTxtTownNamePopup() {
        return txtTownNamePopup;
    }

    public void setTxtTownSTSCodePopup(RichInputText txtTownSTSCodePopup) {
        this.txtTownSTSCodePopup = txtTownSTSCodePopup;
    }

    public RichInputText getTxtTownSTSCodePopup() {
        return txtTownSTSCodePopup;
    }

    public void setBtnCreateUpdateTown(RichCommandButton btnCreateUpdateTown) {
        this.btnCreateUpdateTown = btnCreateUpdateTown;
    }

    public RichCommandButton getBtnCreateUpdateTown() {
        return btnCreateUpdateTown;
    }

    public void setTxtLocationCodePopup(RichInputText txtLocationCodePopup) {
        this.txtLocationCodePopup = txtLocationCodePopup;
    }

    public RichInputText getTxtLocationCodePopup() {
        return txtLocationCodePopup;
    }

    public void setTxtLocationTownCodePopup(RichInputText txtLocationTownCodePopup) {
        this.txtLocationTownCodePopup = txtLocationTownCodePopup;
    }

    public RichInputText getTxtLocationTownCodePopup() {
        return txtLocationTownCodePopup;
    }

    public void setTxtLocationShtDescPopup(RichInputText txtLocationShtDescPopup) {
        this.txtLocationShtDescPopup = txtLocationShtDescPopup;
    }

    public RichInputText getTxtLocationShtDescPopup() {
        return txtLocationShtDescPopup;
    }

    public void setTxtLocationNamePopup(RichInputText txtLocationNamePopup) {
        this.txtLocationNamePopup = txtLocationNamePopup;
    }

    public RichInputText getTxtLocationNamePopup() {
        return txtLocationNamePopup;
    }

    public void setBtnCreateUpdateLocation(RichCommandButton btnCreateUpdateLocation) {
        this.btnCreateUpdateLocation = btnCreateUpdateLocation;
    }

    public RichCommandButton getBtnCreateUpdateLocation() {
        return btnCreateUpdateLocation;
    }

    public void setTblCountryBaseCurrencies(RichTable tblCountryBaseCurrencies) {
        this.tblCountryBaseCurrencies = tblCountryBaseCurrencies;
    }

    public RichTable getTblCountryBaseCurrencies() {
        return tblCountryBaseCurrencies;
    }

    public void setTxtCountryBaseCurrencySymbol(RichInputText txtCountryBaseCurrencySymbol) {
        this.txtCountryBaseCurrencySymbol = txtCountryBaseCurrencySymbol;
    }

    public RichInputText getTxtCountryBaseCurrencySymbol() {
        return txtCountryBaseCurrencySymbol;
    }

    /**
     * This method is called whenever a Country is selected in the
     * <code>treeCountries</code> UI component. It populates the
     * other fileds and tables with the values of the selected row.
     *
     * @param selectionEvent object passed in by ADF Faces when configuring
     * this method to become the selection listener of the
     * <code>treeCountries</code>
     */
    public void tblCountriesSelectionListener(SelectionEvent selectionEvent) {

        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            if (!groupCountryDivisions.isRendered()) {
                groupCountryDivisions.setRendered(true);
                GlobalCC.refreshUI(groupCountryDivisions);
            }
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {

                for (Object treeRowKey : keys) {
                    tblCountries.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)tblCountries.getRowData();

                    txtCountryCode.setValue(nd.getRow().getAttribute("code"));
                    txtCountryShtDesc.setValue(nd.getRow().getAttribute("shortDesc"));
                    txtCountryName.setValue(nd.getRow().getAttribute("name"));
                    txtCountryBaseCurrency.setValue(nd.getRow().getAttribute("baseCurrency"));
                    txtCountryBaseCurrencySymbol.setValue(nd.getRow().getAttribute("currencyDesc"));
                    txtCountryNationality.setValue(nd.getRow().getAttribute("nationality"));
                    txtCountryZIPCode.setValue(nd.getRow().getAttribute("zipCode"));
                    chAdminUnits.setValue(nd.getRow().getAttribute("administrativeType"));
                    chSchengen.setValue(nd.getRow().getAttribute("couShengen"));
                    txtEmbCode.setValue(nd.getRow().getAttribute("couEmbCode"));
                    txtCurrentSerial.setValue(nd.getRow().getAttribute("couCurrSerial"));
                    shDetailAdminUnits.setText(GlobalCC.formatAdminUnitPlural(chAdminUnits.getValue()));
                    lblAdminUnit.setValue(GlobalCC.formatAdminUnitPlural(chAdminUnits.getValue()));
                    dlgNewEditAdminUnit.setTitle(GlobalCC.formatAdminUnitSingular(chAdminUnits.getValue()));
                    session.setAttribute("countryCode",
                                         nd.getRow().getAttribute("code"));


                    session.setAttribute("stateCode", null);
                    session.setAttribute("townCode", null);

                    // Disable the buttons
                    btnCreateUpdateCountry.setText("Update");
                    //btnCreateUpdateCountry.setDisabled(true);
                    //btnCancelCountry.setDisabled(true);

                    // Set the popup values
                    txtTownCountryPopup.setValue(nd.getRow().getAttribute("code"));
                    txtCountryCodePopup.setValue(nd.getRow().getAttribute("code"));

                    if (nd.getRow().getAttribute("shortDesc").equals("GH")) {
                        shDetailAdminUnits.setText("Region");
                        locationTab.setText("Surburbs");

                    }
                    GlobalCC.refreshUI(panelCountryDetails);
                    GlobalCC.refreshUI(shDetailAdminUnits);
                    GlobalCC.refreshUI(locationTab);
                    ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
                    GlobalCC.refreshUI(shDetailAdminUnits);

                    ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
                    GlobalCC.refreshUI(tblCountryTowns);
                    if (tblCountryDists != null) {
                        ADFUtils.findIterator("fetchDistrictsByStateIterator").executeQuery();
                        GlobalCC.refreshUI(tblCountryDists);
                    }
                    ADFUtils.findIterator("findEmbassywebUserAccountsIterator").executeQuery();
                    GlobalCC.refreshUI(webUsersTable);

                }
            }
        }
    }


    public String addEmbWebUserAction() {

        if (session.getAttribute("countryCode") == null) {
            GlobalCC.INFORMATIONREPORTING("You need to first select a country first!");
            return null;
        }

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:webUserPop" + "').show(hints);");
        webusername.setValue("");
        webfullNames.setValue("");
        webEmail.setValue("");
        webPersonalRank.setValue("");
        webAllowLogin.setValue("");
        webUserStatus.setValue("");
        webReset.setValue("");
        webPassword.setValue("");
        webUserId.setValue(null);
        return null;
    }

    public void actionConfirmDeleteEmbWebAcc(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = webUsersTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {

                DBConnector connection = new DBConnector();
                String query =
                    "begin TQC_AGENCIES_PKG.TQC_EMBASSY_CONTACTS_PRC(?,?,?,?);end;";
                ARRAY array = null;
                OracleConnection conn = null;
                OracleCallableStatement stmt = null;
                String currentUser = (String)session.getAttribute("Username");

                BigDecimal id =
                    new BigDecimal((String)nodeBinding.getAttribute("id"));
                WebUser regbean = new WebUser();
                LinkedList reglist = new LinkedList();
                regbean.setACCC_CODE(id);
                reglist.add(regbean);
                try {
                    conn = connection.getDatabaseConnection();
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ACCOUNT_CONTACTS_TAB",
                                                         conn);
                    array =
                            new ARRAY(descriptor, conn, reglist.toArray());
                    stmt.setString(1, "D");
                    stmt.setArray(2, array);
                    stmt.setString(3, currentUser);
                    stmt.registerOutParameter(4, OracleTypes.VARCHAR);
                    stmt.execute();
                    String error = stmt.getString(4);


                    stmt.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("findEmbassywebUserAccountsIterator").executeQuery();
                    GlobalCC.refreshUI(webUsersTable);

                    String message = "Record DELETED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }

            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected.");

            }
        }
    }

    public String updateEmbWebUserAction() {
        if (session.getAttribute("countryCode") == null) {
            GlobalCC.INFORMATIONREPORTING("You need to first select a country first!");
            return null;
        }
        Object key2 = webUsersTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            webUserId.setValue(nodeBinding.getAttribute("id"));
            webusername.setValue(nodeBinding.getAttribute("username"));
            webfullNames.setValue(nodeBinding.getAttribute("name"));
            webEmail.setValue(nodeBinding.getAttribute("email"));
            webPersonalRank.setValue(nodeBinding.getAttribute("personalrank"));
            webAllowLogin.setValue(nodeBinding.getAttribute("allowlogin"));
            webUserStatus.setValue(nodeBinding.getAttribute("status"));
            webReset.setValue(nodeBinding.getAttribute("reset"));
            webPassword.setValue("");


            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:webUserPop" + "').show(hints);");

        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }


    public String deleteEmbWebUserAction() {
        if (session.getAttribute("countryCode") == null) {
            GlobalCC.INFORMATIONREPORTING("You need to first select a country first!");
            return null;
        }
        Object key2 = webUsersTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            GlobalCC.showPopup( "pt1:confirmDeleteWebAccPop");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }


    public String saveEmbWebUsersOperation() {
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_EMBASSY_CONTACTS_PRC(?,?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String currentUser = (String)session.getAttribute("Username");
        WebUser regbean = new WebUser();
        LinkedList reglist = new LinkedList();
        regbean.setACCC_CODE(webUserId.getValue() != null ?
                             new BigDecimal(webUserId.getValue().toString()) :
                             null);
        regbean.setACCC_AGN_CODE(new BigDecimal(session.getAttribute("countryCode").toString()));
        regbean.setACCC_USERNAME((String)webusername.getValue());
        regbean.setACCC_NAME((String)webfullNames.getValue());
        regbean.setACCC_EMAIL_ADDR((String)webEmail.getValue());
        regbean.setACCC_PERSONEL_RANK((String)webPersonalRank.getValue());
        regbean.setACCC_CREATED_BY(currentUser);
        regbean.setACCC_LOGIN_ALLOWED((String)webAllowLogin.getValue());
        regbean.setACCC_STATUS((String)webUserStatus.getValue());
        regbean.setACCC_PWD_RESET((String)webReset.getValue());
        regbean.setACCC_PWD((String)webPassword.getValue());
        reglist.add(regbean);
        try {
            conn = connection.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_ACCOUNT_CONTACTS_TAB",
                                                 conn);
            array = new ARRAY(descriptor, conn, reglist.toArray());
            stmt.setString(1, webUserId.getValue() != null ? "E" : "A");
            stmt.setArray(2, array);
            stmt.setString(3, currentUser);
            stmt.registerOutParameter(4, OracleTypes.VARCHAR);
            stmt.execute();


            stmt.close();
            conn.commit();
            conn.close();
            GlobalCC.dismissPopUp("pt1", "webUserPop");
            String message = "Record ADDED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        ADFUtils.findIterator("findEmbassywebUserAccountsIterator").executeQuery();
        GlobalCC.refreshUI(webUsersTable);


        return null;
    }


    /**
     * Prepare the text fields to create a new country
     *
     * @return
     */
    public String actionNewCountry() {

        txtCountryCode.setValue(null);
        txtCountryShtDesc.setValue(null);
        txtCountryName.setValue(null);
        txtCountryBaseCurrency.setValue(null);
        txtCountryBaseCurrencySymbol.setValue(null);
        txtCountryNationality.setValue(null);
        txtCountryZIPCode.setValue(null);
        chSchengen.setValue("N");
        chAdminUnits.setValue("S");
        txtCurrentSerial.setValue(null);
        txtEmbCode.setValue(null);
        btnCreateUpdateCountry.setText("Save");
        //btnCreateUpdateCountry.setDisabled(false);
        //btnCancelCountry.setDisabled(false);

        return null;
    }

    public String actionEditCountry() {

        btnCreateUpdateCountry.setText("Update");
        //btnCreateUpdateCountry.setDisabled(false);
        //btnCancelCountry.setDisabled(false);
        return null;
    }

    /**
     * Deletes a country from the database
     *
     * @return null
     */
    public String actionDeleteCountry() {

        String Query =
            "begin TQC_SETUPS_PKG.countries_prc(?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, "D");
            cst.setBigDecimal(2,
                              new BigDecimal((String)txtCountryCode.getValue()));
            cst.setString(3, (String)txtCountryShtDesc.getValue());
            cst.setString(4, (String)txtCountryName.getValue());
            cst.setString(5, (String)txtCountryBaseCurrency.getValue());
            cst.setString(6, (String)txtCountryNationality.getValue());
            cst.setString(7, (String)txtCountryZIPCode.getValue());

            cst.execute();

            cst.close();
            conn.commit();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        // Disable the buttons
        txtCountryCode.setValue(null);
        txtCountryShtDesc.setValue(null);
        txtCountryName.setValue(null);
        txtCountryBaseCurrency.setValue(null);
        txtCountryNationality.setValue(null);
        txtCountryZIPCode.setValue(null);
        chSchengen.setValue("N");
        chAdminUnits.setValue("S");
        txtCurrentSerial.setValue(null);
        txtEmbCode.setValue(null);
        //btnCreateUpdateCountry.setDisabled(true);
        //btnCancelCountry.setDisabled(true);


        ADFUtils.findIterator("fetchAllCountriesInfoIterator").executeQuery();
        GlobalCC.refreshUI(tblCountries);

        ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
        GlobalCC.refreshUI(tblCountryStates);

        ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
        GlobalCC.refreshUI(tblCountryTowns);
        if (tblCountryDists != null) {
            ADFUtils.findIterator("fetchDistrictsByStateIterator").executeQuery();
            GlobalCC.refreshUI(tblCountryDists);
        }

        GlobalCC.refreshUI(panelCountryDetails);

        return null;
    }

    public String actionCreateUpdateCountry() {
        
        FormUi formUi = new FormUi();
        if(!formUi.validate("CountryMainTab")){
            return null;
        }

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateCountry.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewCountry();

        } else {

            // Proceed to do an update
            String code = GlobalCC.checkNullValues(txtCountryCode.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtCountryShtDesc.getValue());
            String name = GlobalCC.checkNullValues(txtCountryName.getValue());
            String baseCurr =
                GlobalCC.checkNullValues(txtCountryBaseCurrency.getValue());
            String nationality =
                GlobalCC.checkNullValues(txtCountryNationality.getValue());
            String zipCode =
                GlobalCC.checkNullValues(txtCountryZIPCode.getValue());

            String adminUnits =
                GlobalCC.checkNullValues(chAdminUnits.getValue());

            if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
                return null;

            } else if (name == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type Name");
                return null;

                /*} else if (baseCurr == null) {
              GlobalCC.errorValueNotEntered("Error Value Missing: Enter Base Currency");
              return null;*/

            } else if (nationality == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type Nationality");
                return null;

            } else if (zipCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter ZIP Code");
                return null;

            } else if (adminUnits == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Country Administrative Units Type ");
                return null;
            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.countries_prc(?,?,?,?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2, new BigDecimal(code));
                    cst.setString(3, shortDesc);
                    cst.setString(4, name);
                    cst.setString(5, baseCurr);
                    cst.setString(6, nationality);
                    cst.setString(7, zipCode);
                    cst.setString(8, adminUnits);
                    cst.setObject(9, chSchengen.getValue());
                    cst.setObject(10, txtEmbCode.getValue());
                    cst.setObject(11, txtCurrentSerial.getValue());
                    cst.execute();

                    cst.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("fetchAllCountriesInfoIterator").executeQuery();
                    GlobalCC.refreshUI(tblCountries);

                    ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
                    GlobalCC.refreshUI(tblCountryStates);

                    ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
                    GlobalCC.refreshUI(tblCountryTowns);
                    if (tblCountryDists != null) {
                        ADFUtils.findIterator("fetchDistrictsByStateIterator").executeQuery();
                        GlobalCC.refreshUI(tblCountryDists);
                    }

                    GlobalCC.refreshUI(panelCountryDetails);

                    btnCancelCountry();

                    GlobalCC.INFORMATIONREPORTING("Record UPDATED Successfully!");

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionCreateNewCountry() {

        String shortDesc =
            GlobalCC.checkNullValues(txtCountryShtDesc.getValue());
        String name = GlobalCC.checkNullValues(txtCountryName.getValue());
        String baseCurr =
            GlobalCC.checkNullValues(txtCountryBaseCurrency.getValue());
        String nationality =
            GlobalCC.checkNullValues(txtCountryNationality.getValue());
        String zipCode =
            GlobalCC.checkNullValues(txtCountryZIPCode.getValue());
        String adminUnits = GlobalCC.checkNullValues(chAdminUnits.getValue());

        if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
            return null;

        } else if (name == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type Name");
            return null;

            /*} else if (baseCurr == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Base Currency");
            return null;*/

        } else if (nationality == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type Nationality");
            return null;

        } else if (zipCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter ZIP Code");
            return null;

        } else if (adminUnits == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Country Administrative Units Type ");
            return null;
        } else {

            String Query =
                "begin TQC_SETUPS_PKG.countries_prc(?,?,?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, shortDesc);
                cst.setString(4, name);
                cst.setString(5, baseCurr);
                cst.setString(6, nationality);
                cst.setString(7, zipCode);
                cst.setString(8, adminUnits);
                cst.setObject(9, chSchengen.getValue());
                cst.setObject(10, txtEmbCode.getValue());
                cst.setObject(11, txtCurrentSerial.getValue());

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllCountriesInfoIterator").executeQuery();
                GlobalCC.refreshUI(tblCountries);

                ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
                GlobalCC.refreshUI(tblCountryStates);

                ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
                GlobalCC.refreshUI(tblCountryTowns);
                if (tblCountryDists != null) {
                    ADFUtils.findIterator("fetchDistrictsByStateIterator").executeQuery();
                    GlobalCC.refreshUI(tblCountryDists);
                }

                GlobalCC.refreshUI(panelCountryDetails);

                btnCancelCountry();

                GlobalCC.INFORMATIONREPORTING("New Record ADDED Successfully!");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return "Success";
    }


    public String btnCancelCountry() {

        txtCountryCode.setValue(null);
        txtCountryShtDesc.setValue(null);
        txtCountryName.setValue(null);
        txtCountryBaseCurrency.setValue(null);
        txtCountryBaseCurrencySymbol.setValue(null);
        txtCountryNationality.setValue(null);
        txtCountryZIPCode.setValue(null);
        txtCurrentSerial.setValue(null);
        txtEmbCode.setValue(null);
        chAdminUnits.setValue("S");
        chSchengen.setValue("N");


        btnCreateUpdateCountry.setText("Save");

        return null;
    }

    public void tblCountryBaseCurrenciesSelectionListener(SelectionEvent selectionEvent) {

    }

    public String actionAcceptBaseCurrency() {

        DCIteratorBinding binder =
            ADFUtils.findIterator("fetchAllCurrenciesIterator");
        RowKeySet set = tblCountryBaseCurrencies.getSelectedRowKeys();
        Iterator row = set.iterator();

        while (row.hasNext()) {

            List data = (List)row.next();
            Key key = (Key)data.get(0);
            binder.setCurrentRowWithKey(key.toStringFormat(true));
            Row rows = binder.getCurrentRow();

            txtCountryBaseCurrency.setValue(rows.getAttribute("code"));
            txtCountryBaseCurrencySymbol.setValue(rows.getAttribute("description"));
            GlobalCC.refreshUI(txtCountryBaseCurrencySymbol);
        }
        GlobalCC.dismissPopUp("pt1", "currencyListPopup");
        return null;
    }


    public void tblCountryTownsSelectionListener(SelectionEvent selectionEvent) {
        Object key2 = tblCountryTowns.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("townCode", nodeBinding.getAttribute("code"));

            txtLocationTownCodePopup.setValue(nodeBinding.getAttribute("code"));

            ADFUtils.findIterator("fetchLocationsByTownIterator").executeQuery();
            GlobalCC.refreshUI(tblTownLocations);

            ADFUtils.findIterator("fetchPostalCodesByTownIterator").executeQuery();
            GlobalCC.refreshUI(tblPostalCodes);
        }
    }

    public String actionNewState() {
        if (session.getAttribute("countryCode") == null) {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Country to proceed.");
            return null;
        } else {
            // Clear the text fileds
            txtStateCodePopup.setValue(null);
            txtStateShtDescPopup.setValue(null);
            txtStateNamePopup.setValue(null);
            txtCountryCodePopup.setValue(session.getAttribute("countryCode"));
            dlgNewEditAdminUnit.setTitle("New " +
                                         dlgNewEditAdminUnit.getTitle());
            btnCreateUpdateState.setText("Save");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:countryStatePopup').show(hints);");
        }

        return null;
    }

    public String actionEditState() {
        if (session.getAttribute("countryCode") == null) {
            GlobalCC.errorValueNotEntered("You need to select a Country first");
        } else {

            Object key2 = tblCountryStates.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                txtStateCodePopup.setValue(nodeBinding.getAttribute("stateCode"));
                txtCountryCodePopup.setValue(nodeBinding.getAttribute("stateCountryCode"));
                txtStateShtDescPopup.setValue(nodeBinding.getAttribute("stateShortDesc"));
                txtStateNamePopup.setValue(nodeBinding.getAttribute("stateName"));
                dlgNewEditAdminUnit.setTitle("Edit " +
                                             dlgNewEditAdminUnit.getTitle());
                btnCreateUpdateState.setText("Edit");

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:countryStatePopup').show(hints);");
            } else {
                GlobalCC.INFORMATIONREPORTING("No Record Selected");
                return null;
            }
        }
        return null;
    }

    public String actionCreateNewState() {
        // Proceed to do an update
        String stateCode =
            GlobalCC.checkNullValues(txtStateCodePopup.getValue());
        String stateCountryCode =
            GlobalCC.checkNullValues(txtCountryCodePopup.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtStateShtDescPopup.getValue());
        String stateName =
            GlobalCC.checkNullValues(txtStateNamePopup.getValue());

        if (stateCountryCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select a Country");
            return null;

        } else if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
            return null;

        } else if (stateName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter State Name");
            return null;

        } else {

            String Query = "begin TQC_SETUPS_PKG.states_prc(?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, stateCountryCode);
                cst.setString(4, shortDesc);
                cst.setString(5, stateName);

                cst.execute();

                ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
                GlobalCC.refreshUI(tblCountryStates);

                cst.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:countryStatePopup').hide(hints);");

                String message = "Record CREATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return "Success";
    }

    public String actionCreateUpdateState() {
        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateState.getText().equals("Save")) {
            // Proceed to save
            String status = actionCreateNewState();
        } else {

            // Proceed to do an update
            String stateCode =
                GlobalCC.checkNullValues(txtStateCodePopup.getValue());
            String stateCountryCode =
                GlobalCC.checkNullValues(txtCountryCodePopup.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtStateShtDescPopup.getValue());
            String stateName =
                GlobalCC.checkNullValues(txtStateNamePopup.getValue());

            if (stateCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter State Code");
                return null;

            } else if (stateCountryCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a Country");
                return null;

            } else if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
                return null;

            } else if (stateName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter State Name");
                return null;

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.states_prc(?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2, new BigDecimal(stateCode));
                    cst.setString(3, stateCountryCode);
                    cst.setString(4, shortDesc);
                    cst.setString(5, stateName);

                    cst.execute();

                    ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
                    GlobalCC.refreshUI(tblCountryStates);

                    cst.close();
                    conn.commit();
                    conn.close();

                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:countryStatePopup').hide(hints);");

                    String message = "Record UPDATED  Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionDeleteState() {
        Object key2 = tblCountryStates.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String stateCode =
                nodeBinding.getAttribute("stateCode").toString();
            String stateCountryCode =
                nodeBinding.getAttribute("stateCountryCode").toString();
            String shortDesc =
                nodeBinding.getAttribute("stateShortDesc").toString();
            String stateName =
                nodeBinding.getAttribute("stateName").toString();


            String Query = "begin TQC_SETUPS_PKG.states_prc(?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2, new BigDecimal(stateCode));
                cst.setString(3, stateCountryCode);
                cst.setString(4, shortDesc);
                cst.setString(5, stateName);

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
                GlobalCC.refreshUI(tblCountryStates);

                String message = "Record DELETED  Successfully!";
                showMessagePopup(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String actionNewTown() {
        if (session.getAttribute("stateCode") == null) {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing State to proceed.");
            return null;
        } else {

            // Clear the text fileds
            txtTownCodePopup.setValue(null);
            txtTownShtDescPopup.setValue(null);
            txtTownNamePopup.setValue(null);
            txtTownCountryPopup.setValue(session.getAttribute("countryCode"));
            txtTownSTSCodePopup.setValue(session.getAttribute("stateCode"));

            btnCreateUpdateTown.setText("Save");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:countryTownPopup').show(hints);");
        }

        return null;
    }

    public String actionEditTown() {
        Object key2 = tblCountryTowns.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtTownCodePopup.setValue(nodeBinding.getAttribute("code"));
            txtTownCountryPopup.setValue(nodeBinding.getAttribute("countryCode"));
            txtTownShtDescPopup.setValue(nodeBinding.getAttribute("shortDesc"));
            txtTownNamePopup.setValue(nodeBinding.getAttribute("name"));
            txtTownSTSCodePopup.setValue(nodeBinding.getAttribute("STSCode"));

            btnCreateUpdateTown.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:countryTownPopup').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        return null;
    }

    public String actionDeleteTown() {
        Object key2 = tblCountryTowns.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String Query = "begin TQC_SETUPS_PKG.towns_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)nodeBinding.getAttribute("code")));
                cst.setString(3,
                              (String)nodeBinding.getAttribute("countryCode"));
                cst.setString(4,
                              (String)nodeBinding.getAttribute("shortDesc"));
                cst.setString(5, (String)nodeBinding.getAttribute("name"));
                cst.setString(6, (String)nodeBinding.getAttribute("STSCode"));

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
                GlobalCC.refreshUI(tblCountryTowns);

                String message = "Record DELETED Successfully!";
                showMessagePopup(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String actionNewLocation() {

        // Clear the text fileds
        // No need to clear the towncode field, Because it has been set to
        // allow a new Location to be created to that town
        txtLocationCodePopup.setValue("");
        txtLocationShtDescPopup.setValue("");
        txtLocationNamePopup.setValue("");
        txtLandmark.setValue(null);
        btnCreateUpdateTown.setText("Save");

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:townLocationPopup').show(hints);");
        return null;
    }

    /**
     * @return
     */
    public String actionEditLocation() {
        Object key2 = tblTownLocations.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtLocationCodePopup.setValue(nodeBinding.getAttribute("code"));
            txtLocationTownCodePopup.setValue(nodeBinding.getAttribute("townCode"));
            txtLocationShtDescPopup.setValue(nodeBinding.getAttribute("shortDesc"));
            txtLocationNamePopup.setValue(nodeBinding.getAttribute("name"));
            txtLandmark.setValue(nodeBinding.getAttribute("landmark"));
            btnCreateUpdateLocation.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:townLocationPopup').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    /**
     * @return
     */
    public String actionDeleteLocation() {
        Object key2 = tblTownLocations.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String Query =
                "begin TQC_SETUPS_PKG.locations_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)nodeBinding.getAttribute("code")));
                cst.setBigDecimal(3,
                                  new BigDecimal((String)nodeBinding.getAttribute("townCode")));
                cst.setString(4,
                              (String)nodeBinding.getAttribute("shortDesc"));
                cst.setString(5, (String)nodeBinding.getAttribute("name"));
                cst.setString(6, null);
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchLocationsByTownIterator").executeQuery();
                GlobalCC.refreshUI(tblTownLocations);

                GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully!");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String actionCreateUpdateTown() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateTown.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewTown();

        } else {

            // Proceed to do an update
            String townCode =
                GlobalCC.checkNullValues(txtTownCodePopup.getValue());
            String townCountryCode =
                GlobalCC.checkNullValues(txtTownCountryPopup.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtTownShtDescPopup.getValue());
            String townName =
                GlobalCC.checkNullValues(txtTownNamePopup.getValue());
            String stsCode =
                GlobalCC.checkNullValues(txtTownSTSCodePopup.getValue());

            if (townCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Town Code");
                return null;

            } else if (townCountryCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a Country");
                return null;

            } else if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
                return null;

            } else if (townName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Town Name");
                return null;

            } else if (stsCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type STS Code");
                return null;

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.towns_prc(?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2, new BigDecimal(townCode));
                    cst.setString(3, townCountryCode);
                    cst.setString(4, shortDesc);
                    cst.setString(5, townName);
                    cst.setString(6, stsCode);

                    cst.execute();

                    ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
                    GlobalCC.refreshUI(tblCountryTowns);

                    cst.close();
                    conn.commit();
                    conn.close();
                    GlobalCC.dismissPopUp("pt1", "countryTownPopup");
                    GlobalCC.INFORMATIONREPORTING("Record Updated Successfully!");

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionCreateNewTown() {

        String townCountryCode =
            GlobalCC.checkNullValues(txtTownCountryPopup.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtTownShtDescPopup.getValue());
        String townName =
            GlobalCC.checkNullValues(txtTownNamePopup.getValue());
        String stsCode =
            GlobalCC.checkNullValues(txtTownSTSCodePopup.getValue());

        if (townCountryCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select a Country");
            return null;

        } else if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
            return null;

        } else if (townName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Town Name");
            return null;

        } else if (stsCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type STS Code");
            return null;

        } else {

            String Query = "begin TQC_SETUPS_PKG.towns_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, townCountryCode);
                cst.setString(4, shortDesc);
                cst.setString(5, townName);
                cst.setString(6, stsCode);

                cst.execute();

                ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
                GlobalCC.refreshUI(tblCountryTowns);

                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.dismissPopUp("pt1", "countryTownPopup");
                GlobalCC.INFORMATIONREPORTING("New Record Created Successfully!");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return "Success";
    }

    public String actionCreateUpdateLocation() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateLocation.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewLocation();

        } else {

            // Proceed to do an update
            String locationCode =
                GlobalCC.checkNullValues(txtLocationCodePopup.getValue());
            String townCode =
                GlobalCC.checkNullValues(txtLocationTownCodePopup.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtLocationShtDescPopup.getValue());
            String locationName =
                GlobalCC.checkNullValues(txtLocationNamePopup.getValue());

            if (locationCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Location Code is empty");
                return null;

            } else if (townCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a Town");
                return null;

            } else if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
                return null;

            } else if (locationName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Location Name");
                return null;

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.locations_prc(?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2, new BigDecimal(locationCode));
                    cst.setBigDecimal(3, new BigDecimal(townCode));
                    cst.setString(4, shortDesc);
                    cst.setString(5, locationName);
                    cst.setString(6,
                                  GlobalCC.checkNullValues(txtLandmark.getValue()));

                    cst.execute();

                    ADFUtils.findIterator("fetchLocationsByTownIterator").executeQuery();
                    GlobalCC.refreshUI(tblTownLocations);

                    cst.close();
                    conn.commit();
                    conn.close();
                    GlobalCC.dismissPopUp("pt1", "townLocationPopup");
                    GlobalCC.INFORMATIONREPORTING("Record Updated Successfully!");

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionCreateNewLocation() {

        String townCode =
            GlobalCC.checkNullValues(txtLocationTownCodePopup.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtLocationShtDescPopup.getValue());
        String locationName =
            GlobalCC.checkNullValues(txtLocationNamePopup.getValue());

        if (townCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select a Town");
            return null;

        } else if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
            return null;

        } else if (locationName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Location Name");
            return null;

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.locations_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, townCode);
                cst.setString(4, shortDesc);
                cst.setString(5, locationName);
                cst.setString(6,
                              GlobalCC.checkNullValues(txtLandmark.getValue()));
                cst.execute();

                ADFUtils.findIterator("fetchLocationsByTownIterator").executeQuery();
                GlobalCC.refreshUI(tblTownLocations);

                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.dismissPopUp("pt1", "townLocationPopup");
                GlobalCC.INFORMATIONREPORTING("New Record CREATED Successfully!");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return "Success";
    }

    /* ======================== User Parameters Methods ==================== */

    public void setTxtParameterCode(RichInputText txtParameterCode) {
        this.txtParameterCode = txtParameterCode;
    }

    public RichInputText getTxtParameterCode() {
        return txtParameterCode;
    }

    public void setTxtParameterName(RichInputText txtParameterName) {
        this.txtParameterName = txtParameterName;
    }

    public RichInputText getTxtParameterName() {
        return txtParameterName;
    }

    public void setTxtParameterValue(RichInputText txtParameterValue) {
        this.txtParameterValue = txtParameterValue;
    }

    public RichInputText getTxtParameterValue() {
        return txtParameterValue;
    }

    public void setTxtParameterStatus(RichSelectOneChoice txtParameterStatus) {
        this.txtParameterStatus = txtParameterStatus;
    }

    public RichSelectOneChoice getTxtParameterStatus() {
        return txtParameterStatus;
    }

    public void setTxtParameterDesc(RichInputText txtParameterDesc) {
        this.txtParameterDesc = txtParameterDesc;
    }

    public RichInputText getTxtParameterDesc() {
        return txtParameterDesc;
    }

    public String actionNewParameter() {

        // Clear the text fileds
        txtParameterCode.setValue("");
        txtParameterName.setValue("");
        txtParameterValue.setValue("");
        txtParameterStatus.setValue("");
        txtParameterDesc.setValue("");
        btnCreateUpdateParameter.setText("Save");

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "crm:userParameterPopup').show(hints);");
        return null;
    }

    public String actionEditParameter() {

        Object key2 = tblParameters.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtParameterCode.setValue(nodeBinding.getAttribute("code"));
            txtParameterName.setValue(nodeBinding.getAttribute("name"));
            txtParameterValue.setValue(nodeBinding.getAttribute("value"));
            txtParameterStatus.setValue(nodeBinding.getAttribute("status"));
            txtParameterDesc.setValue(nodeBinding.getAttribute("description"));

            btnCreateUpdateParameter.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:userParameterPopup').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String actionDeleteParameter() {

        Object key2 = tblParameters.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String Query =
                "begin TQC_SETUPS_PKG.parameters_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)nodeBinding.getAttribute("code")));
                cst.setString(3, (String)nodeBinding.getAttribute("name"));
                cst.setString(4, (String)nodeBinding.getAttribute("value"));
                cst.setString(5, (String)nodeBinding.getAttribute("status"));
                cst.setString(6,
                              (String)nodeBinding.getAttribute("description"));

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();


                ADFUtils.findIterator("fetchAllParametersIterator").executeQuery();
                GlobalCC.refreshUI(tblParameters);

                String message = "Record Deleted Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public void setBtnCreateUpdateParameter(RichCommandButton btnCreateUpdateParameter) {
        this.btnCreateUpdateParameter = btnCreateUpdateParameter;
    }

    public RichCommandButton getBtnCreateUpdateParameter() {
        return btnCreateUpdateParameter;
    }

    public String actionCreateUpdateParameter() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateParameter.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewParameter();

        } else {

            // Proceed to do an update
            String paramCode =
                GlobalCC.checkNullValues(txtParameterCode.getValue());
            String paramName =
                GlobalCC.checkNullValues(txtParameterName.getValue());
            String paramValue =
                GlobalCC.checkNullValues(txtParameterValue.getValue());
            String paramStatus =
                GlobalCC.checkNullValues(txtParameterStatus.getValue());
            String paramDesc =
                GlobalCC.checkNullValues(txtParameterDesc.getValue());

            if (paramCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Parameter Code is Empty");
                return null;

            } else if (paramName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Parameter Name");
                return null;

            } else if (paramValue == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Parameter Value");
                return null;

                /*} else if (paramStatus == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select Parameter Status");
                return null;
                */
            } else if (paramDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Parameter Description");
                return null;

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.parameters_prc(?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2, new BigDecimal(paramCode));
                    cst.setString(3, paramName);
                    cst.setString(4, paramValue);
                    cst.setString(5, paramStatus);
                    cst.setString(6, paramDesc);
                    cst.execute();
                    cst.close();
                    conn.commit();
                    conn.close();
                    ADFUtils.findIterator("fetchAllParametersIterator").executeQuery();
                    GlobalCC.refreshUI(tblParameters);
                    GlobalCC.dismissPopUp("crm", "userParameterPopup");

                    String message = "Record UPDATED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String saveLabel() {


        // Proceed to do an update
        String paramCode =
            GlobalCC.checkNullValues(txtParameterCode.getValue());
        String paramName =
            GlobalCC.checkNullValues(txtParameterName.getValue());
        String paramValue =
            GlobalCC.checkNullValues(txtParameterValue.getValue());
        String paramStatus =
            GlobalCC.checkNullValues(txtParameterStatus.getValue());
        String paramDesc =
            GlobalCC.checkNullValues(txtParameterDesc.getValue());

        if (paramCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Parameter Code is Empty");
            return null;

        } else if (paramName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Parameter Name");
            return null;

        } else if (paramValue == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Parameter Value");
            return null;

            /*} else if (paramStatus == null) {
              GlobalCC.errorValueNotEntered("Error Value Missing: Select Parameter Status");
              return null;
              */
        } else if (paramDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Parameter Description");
            return null;

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.labels_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "E");
                cst.setBigDecimal(2, new BigDecimal(paramCode));
                cst.setString(3, paramName);
                cst.setString(4, paramValue);
                cst.setString(5, paramStatus);
                cst.setString(6, paramDesc);
                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("fetchAllLabelsIterator").executeQuery();
                GlobalCC.refreshUI(tblParameters);
                GlobalCC.dismissPopUp("crm", "userParameterPopup");

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String actionCreateNewParameter() {

        String paramName =
            GlobalCC.checkNullValues(txtParameterName.getValue());
        String paramValue =
            GlobalCC.checkNullValues(txtParameterValue.getValue());
        String paramStatus =
            GlobalCC.checkNullValues(txtParameterStatus.getValue());
        String paramDesc =
            GlobalCC.checkNullValues(txtParameterDesc.getValue());

        if (paramName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Parameter Name");
            return null;

        } else if (paramValue == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Parameter Value");
            return null;

        } else if (paramStatus == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select Parameter Status");
            return null;

        } else if (paramDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Parameter Description");
            return null;

        } else {

            String Query =
                "begin TQC_SETUPS_PKG.parameters_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, paramName);
                cst.setString(4, paramValue);
                cst.setString(5, paramStatus);
                cst.setString(6, paramDesc);

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllParametersIterator").executeQuery();
                GlobalCC.refreshUI(tblParameters);
                GlobalCC.dismissPopUp("crm", "userParameterPopup");
                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return "Success";
    }

    public void setTblParameters(RichTable tblParameters) {
        this.tblParameters = tblParameters;
    }

    public RichTable getTblParameters() {
        return tblParameters;
    }

    public void setTblCurrencyDenominations(RichTable tblCurrencyDenominations) {
        this.tblCurrencyDenominations = tblCurrencyDenominations;
    }

    public RichTable getTblCurrencyDenominations() {
        return tblCurrencyDenominations;
    }

    public void setTxtDenomCode(RichInputText txtDenomCode) {
        this.txtDenomCode = txtDenomCode;
    }

    public RichInputText getTxtDenomCode() {
        return txtDenomCode;
    }

    public void setTxtCurrDenomCode(RichInputText txtCurrDenomCode) {
        this.txtCurrDenomCode = txtCurrDenomCode;
    }

    public RichInputText getTxtCurrDenomCode() {
        return txtCurrDenomCode;
    }

    public void setTxtDenomValue(RichInputText txtDenomValue) {
        this.txtDenomValue = txtDenomValue;
    }

    public RichInputText getTxtDenomValue() {
        return txtDenomValue;
    }

    public void setTxtDenomName(RichInputText txtDenomName) {
        this.txtDenomName = txtDenomName;
    }

    public RichInputText getTxtDenomName() {
        return txtDenomName;
    }

    public void setTxtDenomWEF(RichInputDate txtDenomWEF) {
        this.txtDenomWEF = txtDenomWEF;
    }

    public RichInputDate getTxtDenomWEF() {
        return txtDenomWEF;
    }

    public void setBtnCreateUpdateCurrDenom(RichCommandButton btnCreateUpdateCurrDenom) {
        this.btnCreateUpdateCurrDenom = btnCreateUpdateCurrDenom;
    }

    public RichCommandButton getBtnCreateUpdateCurrDenom() {
        return btnCreateUpdateCurrDenom;
    }

    public void setBtnNewCurrencyDenom(RichCommandButton btnNewCurrencyDenom) {
        this.btnNewCurrencyDenom = btnNewCurrencyDenom;
    }

    public RichCommandButton getBtnNewCurrencyDenom() {
        return btnNewCurrencyDenom;
    }

    public void setBtnEditCurrencyDenom(RichCommandButton btnEditCurrencyDenom) {
        this.btnEditCurrencyDenom = btnEditCurrencyDenom;
    }

    public RichCommandButton getBtnEditCurrencyDenom() {
        return btnEditCurrencyDenom;
    }

    public void setBtnDeleteCurrencyDenom(RichCommandButton btnDeleteCurrencyDenom) {
        this.btnDeleteCurrencyDenom = btnDeleteCurrencyDenom;
    }

    public RichCommandButton getBtnDeleteCurrencyDenom() {
        return btnDeleteCurrencyDenom;
    }


    public void setTxtProviderTypeWhtxRatePop2(RichInputNumberSpinbox txtProviderTypeWhtxRatePop2) {
        this.txtProviderTypeWhtxRatePop2 = txtProviderTypeWhtxRatePop2;
    }

    public RichInputNumberSpinbox getTxtProviderTypeWhtxRatePop2() {
        return txtProviderTypeWhtxRatePop2;
    }

    public void setTxtProviderVatRatePop2(RichInputNumberSpinbox txtProviderVatRatePop2) {
        this.txtProviderVatRatePop2 = txtProviderVatRatePop2;
    }

    public RichInputNumberSpinbox getTxtProviderVatRatePop2() {
        return txtProviderVatRatePop2;
    }

    public void setTblCountryStates(RichTable tblCountryStates) {
        this.tblCountryStates = tblCountryStates;
    }

    public RichTable getTblCountryStates() {
        return tblCountryStates;
    }


    public void setTxtStateCodePopup(RichInputText txtStateCodePopup) {
        this.txtStateCodePopup = txtStateCodePopup;
    }

    public RichInputText getTxtStateCodePopup() {
        return txtStateCodePopup;
    }

    public void setTxtCountryCodePopup(RichInputText txtCountryCodePopup) {
        this.txtCountryCodePopup = txtCountryCodePopup;
    }

    public RichInputText getTxtCountryCodePopup() {
        return txtCountryCodePopup;
    }

    public void setTxtStateShtDescPopup(RichInputText txtStateShtDescPopup) {
        this.txtStateShtDescPopup = txtStateShtDescPopup;
    }

    public RichInputText getTxtStateShtDescPopup() {
        return txtStateShtDescPopup;
    }

    public void setTxtStateNamePopup(RichInputText txtStateNamePopup) {
        this.txtStateNamePopup = txtStateNamePopup;
    }

    public RichInputText getTxtStateNamePopup() {
        return txtStateNamePopup;
    }

    public void setBtnCreateUpdateState(RichCommandButton btnCreateUpdateState) {
        this.btnCreateUpdateState = btnCreateUpdateState;
    }

    public RichCommandButton getBtnCreateUpdateState() {
        return btnCreateUpdateState;
    }

    public void setBtnCancelState(RichCommandButton btnCancelState) {
        this.btnCancelState = btnCancelState;
    }

    public RichCommandButton getBtnCancelState() {
        return btnCancelState;
    }

    public void setTextToShow(RichOutputText textToShow) {
        this.textToShow = textToShow;
    }

    public RichOutputText getTextToShow() {
        return textToShow;
    }

    /**
     * Called upon from time to time to display various messages from the
     * server i.e. Completing of successful edit, delete or edit.
     */
    public void showMessagePopup(String msgToDisplay) {
        textToShow.setValue(null);
        textToShow.setValue(msgToDisplay);

        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent source = panelCountryDetails;
        String alignId = source.getClientId(context);
        String popupId = "pt1:ServerMessage";

        StringBuilder script = new StringBuilder();
        script.append("var popup = AdfPage.PAGE.findComponent('").append(popupId).append("'); ").append("if (!popup.isPopupVisible()) { ").append("var hints = {}; ").append("hints[AdfRichPopup.HINT_ALIGN_ID] = '").append(alignId).append("'; ").append("hints[AdfRichPopup.HINT_ALIGN] = AdfRichPopup.ALIGN_AFTER_START; ").append("popup.show(hints);}");
        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());
    }

    public void setServProviderTypesAct(RichTable servProviderTypesAct) {
        this.servProviderTypesAct = servProviderTypesAct;
    }

    public RichTable getServProviderTypesAct() {
        return servProviderTypesAct;
    }

    public void servProviderTypesSelected(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            DCIteratorBinding binder =
                ADFUtils.findIterator("fetchAllServiceProviderTypesIterator");
            RowKeySet set = selectionEvent.getAddedSet();
            Iterator row = set.iterator();
            while (row.hasNext()) {
                List data = (List)row.next();
                Key key = (Key)data.get(0);
                binder.setCurrentRowWithKey(key.toStringFormat(true));
                Row r = binder.getCurrentRow();
                session.setAttribute("vSptCode",
                                     new BigDecimal(r.getAttribute("code").toString()));
                ADFUtils.findIterator("fetchAllServiceProviderTypesActivitiesIterator").executeQuery();
                GlobalCC.refreshUI(servProviderTypesAct);
            }
        }
    }

    public void setTxtActivityId(RichInputText txtActivityId) {
        this.txtActivityId = txtActivityId;
    }

    public RichInputText getTxtActivityId() {
        return txtActivityId;
    }

    public void setTxtActivityDesc(RichInputText txtActivityDesc) {
        this.txtActivityDesc = txtActivityDesc;
    }

    public RichInputText getTxtActivityDesc() {
        return txtActivityDesc;
    }

    public String saveServActivities() {
        BigDecimal messageCode;
        BigDecimal emailCode;
        String smsSelected;
        String emailSelected;
        if (smsCheck.isSelected()) {
            smsSelected = "Y";
        } else {
            smsSelected = "N";
        }
        if (emailCheck.isSelected()) {
            emailSelected = "Y";
        } else {
            emailSelected = "N";
        }
        if (session.getAttribute("smsMessageCode") != null) {
            messageCode =
                    new BigDecimal(session.getAttribute("smsMessageCode").toString());
        } else {
            messageCode = null;
        }
        if (session.getAttribute("emailMessageCode") != null) {
            emailCode =
                    new BigDecimal(session.getAttribute("emailMessageCode").toString());
        } else {
            emailCode = null;
        }
        if (txtActivityId.getValue() == null) {
            GlobalCC.errorValueNotEntered("Error...Enter Activity Id");
        } else if (txtActivityDesc.getValue() == null) {
            GlobalCC.errorValueNotEntered("Error...Enter Activity Desc");
        } else {
            String Query =
                "begin TQC_SETUPS_PKG.service_provider_type_act_prc(?,?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1,
                              (String)session.getAttribute("servTypesAction"));
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("vSPtaCode"));
                cst.setBigDecimal(3,
                                  (BigDecimal)session.getAttribute("vSptCode"));
                if (txtActivityId.getValue() == null) {
                    cst.setString(4, null);
                } else
                    cst.setString(4, txtActivityId.getValue().toString());
                if (txtActivityDesc.getValue() == null) {
                    cst.setString(5, null);
                } else
                    cst.setString(5, txtActivityDesc.getValue().toString());
                cst.setBigDecimal(6, messageCode);
                cst.setBigDecimal(7, emailCode);
                cst.setString(8, smsSelected);
                cst.setString(9, emailSelected);
                cst.setString(10,
                              GlobalCC.checkNullValues(reportDays.getValue()));
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllServiceProviderTypesActivitiesIterator").executeQuery();
                GlobalCC.refreshUI(servProviderTypesAct);

                GlobalCC.INFORMATIONREPORTING("Record Processed Successfully!");

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:p1').hide();");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String addSErvActivities() {
        if (session.getAttribute("vSptCode") == null) {
            GlobalCC.errorValueNotEntered("Select Service Provider Type to Add Activity");
            return null;
        }
        session.setAttribute("servTypesAction", "A");
        session.setAttribute("vSPtaCode", null);
        txtActivityDesc.setValue(null);
        txtActivityId.setValue(null);
        txtSmsTemplates.setValue(null);
        txtEmailTemplates.setValue(null);
        smsCheck.setValue(null);
        emailCheck.setValue(null);
        reportDays.setValue(null);
        session.setAttribute("emailMessageCode", null);
        session.setAttribute("smsMessageCode", null);
        session.setAttribute("vSPtaCode", null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:p1').show(hints);");
        return null;
    }

    public String editServActivities() {

        Object key2 = servProviderTypesAct.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("servTypesAction", "E");

            txtActivityDesc.setValue(nodeBinding.getAttribute("name"));
            txtActivityId.setValue(nodeBinding.getAttribute("shortDesc"));
            session.setAttribute("vSPtaCode",
                                 new BigDecimal(nodeBinding.getAttribute("code").toString()));

            txtSmsTemplates.setValue(nodeBinding.getAttribute("smsmessage"));
            txtEmailTemplates.setValue(nodeBinding.getAttribute("emailmessage"));
            reportDays.setValue(nodeBinding.getAttribute("reportDays"));

            session.setAttribute("emailMessageCode",
                                 nodeBinding.getAttribute("emailCode"));
            session.setAttribute("smsMessageCode",
                                 nodeBinding.getAttribute("smsCode"));
            if (nodeBinding.getAttribute("smsDefault") != null) {
                if (nodeBinding.getAttribute("smsDefault").equals("Y")) {
                    smsCheck.setSelected(true);
                    session.setAttribute("smsSelected", "true");
                } else {
                    smsCheck.setSelected(false);
                    session.setAttribute("smsSelected", "false");
                }
            }
            if (nodeBinding.getAttribute("smsDefault") != null) {
                if (nodeBinding.getAttribute("smsDefault").equals("Y")) {
                    smsCheck.setSelected(true);
                } else {
                    smsCheck.setSelected(false);
                }

            } else {
                smsCheck.setSelected(false);
                session.setAttribute("smsSelected", null);
            }

            if (nodeBinding.getAttribute("emailDefault") != null) {
                if (nodeBinding.getAttribute("emailDefault").equals("Y")) {
                    emailCheck.setSelected(true);
                    session.setAttribute("emailSelected", "true");

                } else {
                    emailCheck.setSelected(false);
                    session.setAttribute("emailSelected", "false");
                }
            }
            if (nodeBinding.getAttribute("emailDefault") != null) {
                if (nodeBinding.getAttribute("emailDefault").equals("Y")) {
                    emailCheck.setSelected(true);
                } else {
                    emailCheck.setSelected(false);

                }
            } else {
                emailCheck.setSelected(false);
                session.setAttribute("emailSelected", null);
            }
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:p1').show(hints);");
        GlobalCC.refreshUI(smsCheck);
        GlobalCC.refreshUI(emailCheck);
        GlobalCC.refreshUI(txtActivityDesc);
        GlobalCC.refreshUI(txtEmailTemplates);
        GlobalCC.refreshUI(txtActivityId);
        GlobalCC.refreshUI(txtSmsTemplates);

        return null;
    }


    public String deleteServActivities() {

        Object key2 = servProviderTypesAct.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("servTypesAction", "D");
            session.setAttribute("vSPtaCode",
                                 new BigDecimal(nodeBinding.getAttribute("code").toString()));

            String Query =
                "begin TQC_SETUPS_PKG.service_provider_type_act_prc(?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1,
                              (String)session.getAttribute("servTypesAction"));
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("vSPtaCode"));
                cst.setBigDecimal(3,
                                  (BigDecimal)session.getAttribute("vSptCode"));
                cst.setString(4, null);
                cst.setString(5, null);
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllServiceProviderTypesActivitiesIterator").executeQuery();
                GlobalCC.refreshUI(servProviderTypesAct);

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public void clearPostalCodesFields() {
        txtPstCode.setValue(null);
        txtPstTwnCode.setValue(session.getAttribute("townCode"));
        txtPstDesc.setValue(null);
        txtPstZipCode.setValue(null);

        btnSaveUpdatePostalCode.setText("Save");
    }

    public String actionNewPostalCode() {
        if (session.getAttribute("townCode") != null) {
            clearPostalCodesFields();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:postalCodesPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select an existing Town to proceed.");
            return null;
        }
        return null;
    }

    public String actionEditPostalCode() {
        Object key2 = tblPostalCodes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtPstCode.setValue(nodeBinding.getAttribute("pstCode"));
            txtPstTwnCode.setValue(nodeBinding.getAttribute("pstTownCode"));
            txtPstDesc.setValue(nodeBinding.getAttribute("pstDesc"));
            txtPstZipCode.setValue(nodeBinding.getAttribute("pstZipCode"));

            btnSaveUpdatePostalCode.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:postalCodesPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record first.");
            return null;
        }
        return null;
    }

    public String actionDeletePostalCode() {
        Object key2 = tblPostalCodes.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("pstCode").toString();
            String townCode =
                nodeBinding.getAttribute("pstTownCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;

            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.postalCodes_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_POSTAL_CODES_TAB",
                                                     conn);
                ArrayList postalCodeList = new ArrayList();
                PostalCode postalCode = new PostalCode();
                postalCode.setSQLTypeName("TQC_POSTAL_CODES_OBJ");

                postalCode.setPstCode(new BigDecimal(code));
                postalCode.setPstTownCode(new BigDecimal(townCode));
                postalCode.setPstDesc(null);
                postalCode.setPstZipCode(null);

                postalCodeList.add(postalCode);
                ARRAY array =
                    new ARRAY(descriptor, conn, postalCodeList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchPostalCodesByTownIterator").executeQuery();
                GlobalCC.refreshUI(tblPostalCodes);

                clearPostalCodesFields();

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

    public String actionSaveUpdatePostalCode() {
        if (btnSaveUpdatePostalCode.getText().equals("Edit")) {
            actionUpdatePostalCode();
        } else {
            String code = GlobalCC.checkNullValues(txtPstCode.getValue());
            String twnCode =
                GlobalCC.checkNullValues(txtPstTwnCode.getValue());
            String desc = GlobalCC.checkNullValues(txtPstDesc.getValue());
            String zipCode =
                GlobalCC.checkNullValues(txtPstZipCode.getValue());

            if (twnCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Town Code is Empty");
                return null;

            } else if (desc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
                return null;

            } else {
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn = null;
                OracleCallableStatement statement = null;
                try {
                    conn = dbConnector.getDatabaseConnection();
                    String query =
                        "begin TQC_SETUPS_PKG.postalCodes_prc(?,?); end;";

                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_POSTAL_CODES_TAB",
                                                         conn);
                    ArrayList postalCodeList = new ArrayList();
                    PostalCode postalCode = new PostalCode();
                    postalCode.setSQLTypeName("TQC_POSTAL_CODES_OBJ");

                    postalCode.setPstCode(code == null ? null :
                                          new BigDecimal(code));
                    postalCode.setPstTownCode(twnCode == null ? null :
                                              new BigDecimal(twnCode));
                    postalCode.setPstDesc(desc);
                    postalCode.setPstZipCode(zipCode);

                    postalCodeList.add(postalCode);
                    ARRAY array =
                        new ARRAY(descriptor, conn, postalCodeList.toArray());

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
                                         "pt1:postalCodesPop" +
                                         "').hide(hints);");

                    ADFUtils.findIterator("fetchPostalCodesByTownIterator").executeQuery();
                    GlobalCC.refreshUI(tblPostalCodes);

                    clearPostalCodesFields();

                    String message = "New Record ADDED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionUpdatePostalCode() {
        String code = GlobalCC.checkNullValues(txtPstCode.getValue());
        String twnCode = GlobalCC.checkNullValues(txtPstTwnCode.getValue());
        String desc = GlobalCC.checkNullValues(txtPstDesc.getValue());
        String zipCode = GlobalCC.checkNullValues(txtPstZipCode.getValue());

        if (code == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (twnCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Town Code is Empty");
            return null;

        } else if (desc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Description is Empty");
            return null;

        } else {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.postalCodes_prc(?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_POSTAL_CODES_TAB",
                                                     conn);
                ArrayList postalCodeList = new ArrayList();
                PostalCode postalCode = new PostalCode();
                postalCode.setSQLTypeName("TQC_POSTAL_CODES_OBJ");

                postalCode.setPstCode(code == null ? null :
                                      new BigDecimal(code));
                postalCode.setPstTownCode(twnCode == null ? null :
                                          new BigDecimal(twnCode));
                postalCode.setPstDesc(desc);
                postalCode.setPstZipCode(zipCode);

                postalCodeList.add(postalCode);
                ARRAY array =
                    new ARRAY(descriptor, conn, postalCodeList.toArray());

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
                                     "pt1:postalCodesPop" + "').hide(hints);");

                ADFUtils.findIterator("fetchPostalCodesByTownIterator").executeQuery();
                GlobalCC.refreshUI(tblPostalCodes);

                clearPostalCodesFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void setTblPostalCodes(RichTable tblPostalCodes) {
        this.tblPostalCodes = tblPostalCodes;
    }

    public RichTable getTblPostalCodes() {
        return tblPostalCodes;
    }

    public void setTxtPstCode(RichInputText txtPstCode) {
        this.txtPstCode = txtPstCode;
    }

    public RichInputText getTxtPstCode() {
        return txtPstCode;
    }

    public void setTxtPstTwnCode(RichInputText txtPstTwnCode) {
        this.txtPstTwnCode = txtPstTwnCode;
    }

    public RichInputText getTxtPstTwnCode() {
        return txtPstTwnCode;
    }

    public void setTxtPstDesc(RichInputText txtPstDesc) {
        this.txtPstDesc = txtPstDesc;
    }

    public RichInputText getTxtPstDesc() {
        return txtPstDesc;
    }

    public void setTxtPstZipCode(RichInputText txtPstZipCode) {
        this.txtPstZipCode = txtPstZipCode;
    }

    public RichInputText getTxtPstZipCode() {
        return txtPstZipCode;
    }

    public void setBtnSaveUpdatePostalCode(RichCommandButton btnSaveUpdatePostalCode) {
        this.btnSaveUpdatePostalCode = btnSaveUpdatePostalCode;
    }

    public RichCommandButton getBtnSaveUpdatePostalCode() {
        return btnSaveUpdatePostalCode;
    }


    public void holdingCompanyConfirmationDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteHoldingCompany();
        }
    }

    public void agentClassConfirmationDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteAgencyClass();
        }
    }

    public void countryConfirmationDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteCountry();
        }
    }

    public String confirmCountryDeleteAction() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:countryConfirmationDialog" +
                             "').show(hints);");
        return null;
    }

    public void tblCountryStatesSelectionListener(SelectionEvent selectionEvent) {
        Object key2 = tblCountryStates.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("stateCode",
                                 nodeBinding.getAttribute("stateCode"));

            ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
            GlobalCC.refreshUI(tblCountryTowns);
            if (tblCountryDists != null) {
                ADFUtils.findIterator("fetchDistrictsByStateIterator").executeQuery();
                GlobalCC.refreshUI(tblCountryDists);
            }
        }
    }

    public void actionTblHoldingCompanies(SelectionEvent selectionEvent) {
        // Add event code here...
        btnEditHoldingCompany.setDisabled(false);
        btnDeleteHoldingCompany.setDisabled(false);
        GlobalCC.refreshUI(btnEditHoldingCompany);
        GlobalCC.refreshUI(btnDeleteHoldingCompany);

    }

    public void actionConfirmDeleteDenominitions(DialogEvent dialogEvent) {
        // Add event code here...
    }

    public void actionConfirmDeleteExchangeRate(DialogEvent dialogEvent) {
        // Add event code here...
    }

    public void setTxtCampProductName(RichInputText txtCampProductName) {
        this.txtCampProductName = txtCampProductName;
    }

    public RichInputText getTxtCampProductName() {
        return txtCampProductName;
    }

    public void setTxtCampUser(RichInputText txtCampUser) {
        this.txtCampUser = txtCampUser;
    }

    public RichInputText getTxtCampUser() {
        return txtCampUser;
    }

    public void setTxtCampTeam(RichInputText txtCampTeam) {
        this.txtCampTeam = txtCampTeam;
    }

    public RichInputText getTxtCampTeam() {
        return txtCampTeam;
    }

    public void setTblCountries(RichTable tblCountries) {
        this.tblCountries = tblCountries;
    }

    public RichTable getTblCountries() {
        return tblCountries;
    }

    public void setChAdminUnits(RichSelectOneChoice chAdminUnits) {
        this.chAdminUnits = chAdminUnits;
    }

    public RichSelectOneChoice getChAdminUnits() {
        return chAdminUnits;
    }

    public void setGroupCountryDivisions(UIXGroup groupCountryDivisions) {
        this.groupCountryDivisions = groupCountryDivisions;
    }

    public UIXGroup getGroupCountryDivisions() {
        return groupCountryDivisions;
    }

    public void setShDetailAdminUnits(RichShowDetailItem shDetailAdminUnits) {
        this.shDetailAdminUnits = shDetailAdminUnits;
    }

    public RichShowDetailItem getShDetailAdminUnits() {
        return shDetailAdminUnits;
    }

    public void setLblAdminUnit(RichOutputLabel lblAdminUnit) {
        this.lblAdminUnit = lblAdminUnit;
    }

    public RichOutputLabel getLblAdminUnit() {
        return lblAdminUnit;
    }

    public void setDlgNewEditAdminUnit(RichDialog dlgNewEditAdminUnit) {
        this.dlgNewEditAdminUnit = dlgNewEditAdminUnit;
    }

    public RichDialog getDlgNewEditAdminUnit() {
        return dlgNewEditAdminUnit;
    }

    public void setChSchengen(RichSelectOneChoice chSchengen) {
        this.chSchengen = chSchengen;
    }

    public RichSelectOneChoice getChSchengen() {
        return chSchengen;
    }

    public void setTxtEmbCode(RichInputText txtEmbCode) {
        this.txtEmbCode = txtEmbCode;
    }

    public RichInputText getTxtEmbCode() {
        return txtEmbCode;
    }

    public void setTxtCurrentSerial(RichInputNumberSpinbox txtCurrentSerial) {
        this.txtCurrentSerial = txtCurrentSerial;
    }

    public RichInputNumberSpinbox getTxtCurrentSerial() {
        return txtCurrentSerial;
    }

    public void setTxtPostAdd(RichInputText txtPostAdd) {
        this.txtPostAdd = txtPostAdd;
    }

    public RichInputText getTxtPostAdd() {
        return txtPostAdd;
    }

    public void setTxtPhyAdd(RichInputText txtPhyAdd) {
        this.txtPhyAdd = txtPhyAdd;
    }

    public RichInputText getTxtPhyAdd() {
        return txtPhyAdd;
    }

    public void setTxtTelNumber(RichInputText txtTelNumber) {
        this.txtTelNumber = txtTelNumber;
    }

    public RichInputText getTxtTelNumber() {
        return txtTelNumber;
    }

    public void setTxtMobNumber(RichInputText txtMobNumber) {
        this.txtMobNumber = txtMobNumber;
    }

    public RichInputText getTxtMobNumber() {
        return txtMobNumber;
    }

    public void setTxtContactPerson(RichInputText txtContactPerson) {
        this.txtContactPerson = txtContactPerson;
    }

    public RichInputText getTxtContactPerson() {
        return txtContactPerson;
    }

    public void setWebUsersTable(RichTable webUsersTable) {
        this.webUsersTable = webUsersTable;
    }

    public RichTable getWebUsersTable() {
        return webUsersTable;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setWebUserId(RichInputText webUserId) {
        this.webUserId = webUserId;
    }

    public RichInputText getWebUserId() {
        return webUserId;
    }

    public void setWebusername(RichInputText webusername) {
        this.webusername = webusername;
    }

    public RichInputText getWebusername() {
        return webusername;
    }

    public void setWebfullNames(RichInputText webfullNames) {
        this.webfullNames = webfullNames;
    }

    public RichInputText getWebfullNames() {
        return webfullNames;
    }

    public void setWebPassword(RichInputText webPassword) {
        this.webPassword = webPassword;
    }

    public RichInputText getWebPassword() {
        return webPassword;
    }

    public void setWebEmail(RichInputText webEmail) {
        this.webEmail = webEmail;
    }

    public RichInputText getWebEmail() {
        return webEmail;
    }

    public void setWebPersonalRank(RichInputText webPersonalRank) {
        this.webPersonalRank = webPersonalRank;
    }

    public RichInputText getWebPersonalRank() {
        return webPersonalRank;
    }

    public void setWebAllowLogin(RichSelectOneRadio webAllowLogin) {
        this.webAllowLogin = webAllowLogin;
    }

    public RichSelectOneRadio getWebAllowLogin() {
        return webAllowLogin;
    }

    public void setWebUserStatus(RichSelectOneChoice webUserStatus) {
        this.webUserStatus = webUserStatus;
    }

    public RichSelectOneChoice getWebUserStatus() {
        return webUserStatus;
    }

    public void setWebReset(RichSelectOneRadio webReset) {
        this.webReset = webReset;
    }

    public RichSelectOneRadio getWebReset() {
        return webReset;
    }

    public void setTxtClientType(RichInputText txtClientType) {
        this.txtClientType = txtClientType;
    }

    public RichInputText getTxtClientType() {
        return txtClientType;
    }

    public void setTxtClientCategory(RichSelectOneChoice txtClientCategory) {
        this.txtClientCategory = txtClientCategory;
    }

    public RichSelectOneChoice getTxtClientCategory() {
        return txtClientCategory;
    }

    public void setTxtClientTypeTbl(RichTable txtClientTypeTbl) {
        this.txtClientTypeTbl = txtClientTypeTbl;
    }

    public RichTable getTxtClientTypeTbl() {
        return txtClientTypeTbl;
    }


    public void setTxtClientShtDesc(RichInputText txtClientShtDesc) {
        this.txtClientShtDesc = txtClientShtDesc;
    }

    public RichInputText getTxtClientShtDesc() {
        return txtClientShtDesc;
    }

    public void setTxtClientNames(RichInputText txtClientNames) {
        this.txtClientNames = txtClientNames;
    }

    public RichInputText getTxtClientNames() {
        return txtClientNames;
    }

    public void setTxtClientOthernames(RichInputText txtClientOthernames) {
        this.txtClientOthernames = txtClientOthernames;
    }

    public RichInputText getTxtClientOthernames() {
        return txtClientOthernames;
    }

    public void setTxtRegNumber(RichInputText txtRegNumber) {
        this.txtRegNumber = txtRegNumber;
    }

    public RichInputText getTxtRegNumber() {
        return txtRegNumber;
    }

    public void setTxtClientDateOfBirth(RichInputText txtClientDateOfBirth) {
        this.txtClientDateOfBirth = txtClientDateOfBirth;
    }

    public RichInputText getTxtClientDateOfBirth() {
        return txtClientDateOfBirth;
    }

    public void setTxtClientsPhyAddress(RichInputText txtClientsPhyAddress) {
        this.txtClientsPhyAddress = txtClientsPhyAddress;
    }

    public RichInputText getTxtClientsPhyAddress() {
        return txtClientsPhyAddress;
    }

    public void setTxtTownCode(RichInputNumberSpinbox txtTownCode) {
        this.txtTownCode = txtTownCode;
    }

    public RichInputNumberSpinbox getTxtTownCode() {
        return txtTownCode;
    }

    public void setCountryCode(RichInputNumberSpinbox countryCode) {
        this.countryCode = countryCode;
    }

    public RichInputNumberSpinbox getCountryCode() {
        return countryCode;
    }

    public void setTxtAccountNumber(RichInputText txtAccountNumber) {
        this.txtAccountNumber = txtAccountNumber;
    }

    public RichInputText getTxtAccountNumber() {
        return txtAccountNumber;
    }

    public void setTxtClientCoverFrom(RichInputDate txtClientCoverFrom) {
        this.txtClientCoverFrom = txtClientCoverFrom;
    }

    public RichInputDate getTxtClientCoverFrom() {
        return txtClientCoverFrom;
    }

    public void setTxtClientCoverTo(RichInputDate txtClientCoverTo) {
        this.txtClientCoverTo = txtClientCoverTo;
    }

    public RichInputDate getTxtClientCoverTo() {
        return txtClientCoverTo;
    }

    public void setTxtClientTypes(RichSelectOneChoice txtClientTypes) {
        this.txtClientTypes = txtClientTypes;
    }

    public RichSelectOneChoice getTxtClientTypes() {
        return txtClientTypes;
    }

    public void setTxtCreatedBy(RichInputText txtCreatedBy) {
        this.txtCreatedBy = txtCreatedBy;
    }

    public RichInputText getTxtCreatedBy() {
        return txtCreatedBy;
    }

    public void setTxtAgentStatus(RichInputText txtAgentStatus) {
        this.txtAgentStatus = txtAgentStatus;
    }

    public RichInputText getTxtAgentStatus() {
        return txtAgentStatus;
    }

    public void setTxtDirectClient(RichInputText txtDirectClient) {
        this.txtDirectClient = txtDirectClient;
    }

    public RichInputText getTxtDirectClient() {
        return txtDirectClient;
    }

    public void setTxtDateCreated(RichInputDate txtDateCreated) {
        this.txtDateCreated = txtDateCreated;
    }

    public RichInputDate getTxtDateCreated() {
        return txtDateCreated;
    }

    public void setTxtClientCodes(RichInputNumberSpinbox txtClientCodes) {
        this.txtClientCodes = txtClientCodes;
    }

    public RichInputNumberSpinbox getTxtClientCodes() {
        return txtClientCodes;
    }

    public String editDescription() {
        // Add event code here...
        return null;
    }

    public String deleteDescription() {
        // Add event code here...
        return null;
    }

    public String saveDescription() {
        // Add event code here...
        return null;
    }

    public void setTxtSmsTemplates(RichInputText txtSmsTemplates) {
        this.txtSmsTemplates = txtSmsTemplates;
    }

    public RichInputText getTxtSmsTemplates() {
        return txtSmsTemplates;
    }

    public void setTxtEmailTemplates(RichInputText txtEmailTemplates) {
        this.txtEmailTemplates = txtEmailTemplates;
    }

    public RichInputText getTxtEmailTemplates() {
        return txtEmailTemplates;
    }

    public void setSmsTbl(RichTable smsTbl) {
        this.smsTbl = smsTbl;
    }

    public RichTable getSmsTbl() {
        return smsTbl;
    }

    public String showSmsTemplates() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:smsTempPop" + "').show(hints);");
        return null;
    }

    public String showEmailTempates() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:emailTemPop" + "').show(hints);");
        return null;
    }

    public void setEmailTbl(RichTable emailTbl) {
        this.emailTbl = emailTbl;
    }

    public RichTable getEmailTbl() {
        return emailTbl;
    }

    public String selectSmsTemplate() {
        Object key = smsTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a template");
            return null;
        }
        txtSmsTemplates.setValue(r.getAttribute("message"));
        session.setAttribute("smsMessageCode", r.getAttribute("msgCode"));
        if (session.getAttribute("smsSelected") != null) {
            if (session.getAttribute("smsSelected").equals("true")) {
                smsCheck.setSelected(true);
            } else {
                smsCheck.setSelected(false);
            }
        } else {
            smsCheck.setSelected(false);
        }
        if (session.getAttribute("emailSelected") != null) {
            if (session.getAttribute("emailSelected").equals("true")) {
                emailCheck.setSelected(true);
            } else {
                emailCheck.setSelected(false);
            }
        } else {
            emailCheck.setSelected(false);
        }
        GlobalCC.refreshUI(txtSmsTemplates);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:p1" +
                             "').show(hints);");


        txtSmsTemplates.setValue(r.getAttribute("message"));
        session.setAttribute("smsMessageCode", r.getAttribute("msgCode"));
        GlobalCC.refreshUI(txtSmsTemplates);

        GlobalCC.refreshUI(smsCheck);
        GlobalCC.refreshUI(emailCheck);
        return null;
    }

    public String selectEmailTemplate() {
        Object key = emailTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a template");
            return null;
        }
        txtEmailTemplates.setValue(r.getAttribute("message"));
        session.setAttribute("emailMessageCode", r.getAttribute("msgCode"));
        GlobalCC.refreshUI(txtEmailTemplates);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:p1" +
                             "').show(hints);");
        return null;
    }

    public void setSmsCheck(RichSelectBooleanCheckbox smsCheck) {
        this.smsCheck = smsCheck;
    }

    public RichSelectBooleanCheckbox getSmsCheck() {
        return smsCheck;
    }

    public void setEmailCheck(RichSelectBooleanCheckbox emailCheck) {
        this.emailCheck = emailCheck;
    }

    public RichSelectBooleanCheckbox getEmailCheck() {
        return emailCheck;
    }

    public void selectSystems(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();
            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    systemTree.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nodeBinding =
                        (JUCtrlHierNodeBinding)systemTree.getRowData();
                    session.setAttribute("SystemCode",
                                         nodeBinding.getRow().getAttribute("systemCode"));
                }
            }
            ADFUtils.findIterator("findRequiredDocumentsIterator").executeQuery();
            GlobalCC.refreshUI(requiredDocTbl);
        }
    }

    public void setSystemTree(RichTree systemTree) {
        this.systemTree = systemTree;
    }

    public RichTree getSystemTree() {
        return systemTree;
    }

    public void setRequiredDocTbl(RichTable requiredDocTbl) {
        this.requiredDocTbl = requiredDocTbl;
    }

    public RichTable getRequiredDocTbl() {
        return requiredDocTbl;
    }

    public void setRowChecked(RichSelectBooleanCheckbox rowChecked) {
        this.rowChecked = rowChecked;
    }

    public RichSelectBooleanCheckbox getRowChecked() {
        return rowChecked;
    }

    public void saveRequiredDocs(ActionEvent actionEvent) {
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        if (session.getAttribute("vSptCode") == null) {
            GlobalCC.errorValueNotEntered("Please select the service provider type");
            return;

        } else if (session.getAttribute("serviceProvTypeCode") == null) {
            GlobalCC.errorValueNotEntered("Please Select the service provider Activity");
            return;

        } else if (session.getAttribute("SystemCode") == null) {
            GlobalCC.errorValueNotEntered("Please select the system code");
            return;

        } else {

            int rowCount = requiredDocTbl.getRowCount();
            if (rowCount < 1) {
                GlobalCC.INFORMATIONREPORTING("No Required Docs!");
                return;
            }
            conn = connector.getDatabaseConnection();
            try {
                for (int i = 0; i < rowCount; i++) {
                    Boolean Accept = false;
                    JUCtrlValueBinding r =
                        (JUCtrlValueBinding)requiredDocTbl.getRowData(i);
                    Accept = (Boolean)r.getAttribute("checked");
                    if (Accept) {
                        String Query =
                            "begin TQC_SETUPS_PKG.getRequiredDocs(?,?,?,?,?,?); end;";
                        cst = (OracleCallableStatement)conn.prepareCall(Query);

                        cst.setString(1, "A");
                        cst.setBigDecimal(3,
                                          (BigDecimal)session.getAttribute("vSptCode"));
                        if (r.getAttribute("rqcCode") != null) {
                            cst.setBigDecimal(2,
                                              (BigDecimal)r.getAttribute("rqcCode"));
                        } else {
                            cst.setBigDecimal(2, null);
                        }
                        System.out.println("serviceProvTypeCode" +
                                           session.getAttribute("serviceProvTypeCode"));
                        cst.setBigDecimal(4,
                                          new BigDecimal(session.getAttribute("serviceProvTypeCode").toString()));
                        cst.setBigDecimal(5,
                                          (BigDecimal)session.getAttribute("SystemCode"));
                        if (r.getAttribute("docId") != null) {
                            cst.setBigDecimal(6,
                                              (BigDecimal)r.getAttribute("docId"));
                        } else {
                            cst.setBigDecimal(6, null);
                        }
                        cst.execute();
                    }

                }
                cst.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("findRequiredDocumentsIterator").executeQuery();
                GlobalCC.refreshUI(requiredDocTbl);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
    }

    public void deleteRequiredDocs(ActionEvent actionEvent) {
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        int rowCount = requiredDocTbl.getRowCount();
        if (rowCount < 1) {
            GlobalCC.INFORMATIONREPORTING("No Required Docs!");
            return;
        }
        conn = connector.getDatabaseConnection();
        try {
            for (int i = 0; i < rowCount; i++) {
                Boolean Accept = false;
                JUCtrlValueBinding r =
                    (JUCtrlValueBinding)requiredDocTbl.getRowData(i);
                Accept = (Boolean)r.getAttribute("checked");
                if (!Accept) {
                    String Query =
                        "begin TQC_SETUPS_PKG.getRequiredDocs(?,?,?,?,?,?); end;";
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "D");
                    cst.setBigDecimal(3, null);

                    cst.setBigDecimal(2,
                                      (BigDecimal)r.getAttribute("rqcCode"));
                    cst.setBigDecimal(4, null);
                    cst.setBigDecimal(5, null);
                    cst.setBigDecimal(6, null);
                    cst.execute();
                }

            }
            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findRequiredDocumentsIterator").executeQuery();
            GlobalCC.refreshUI(requiredDocTbl);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

    }

    public void selectServiceProviderType(SelectionEvent selectionEvent) {
        Object key = servProviderTypesAct.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return;
        }
        session.setAttribute("serviceProvTypeCode", r.getAttribute("code"));

    }

    public void selectActivity(SelectionEvent selectionEvent) {
        Object key = requiredDocTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return;
        }
        session.setAttribute("rqcCode", r.getAttribute("rqcCode"));
        session.setAttribute("docId", r.getAttribute("docId"));
    }

    public String saveSubModuleDtls() {
        // Add event code here...
        return null;
    }

    public void setTxtOccupShtDesc(RichInputText txtOccupShtDesc) {
        this.txtOccupShtDesc = txtOccupShtDesc;
    }

    public RichInputText getTxtOccupShtDesc() {
        return txtOccupShtDesc;
    }

    public void setTxtOccup(RichInputText txtOccup) {
        this.txtOccup = txtOccup;
    }

    public RichInputText getTxtOccup() {
        return txtOccup;
    }

    public String saveOccupation() {
        String Query = "begin TQC_SETUPS_PKG.occupations_prc(?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("pkey"));
            cst.setString(3, (String)txtOccupShtDesc.getValue());
            cst.setString(4, (String)txtOccup.getValue());
            cst.setBigDecimal(5, null);
            cst.execute();

            cst.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("findAllOccupationsIterator").executeQuery();
            GlobalCC.refreshUI(allOccupTbl);
            GlobalCC.dismissPopUp("pt1", "p1");
            String message = "Record UPDATED successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setOccupTbl(RichTable occupTbl) {
        this.occupTbl = occupTbl;
    }

    public RichTable getOccupTbl() {
        return occupTbl;
    }

    public void selectSector(SelectionEvent selectionEvent) {
        Object key = tblSectors.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return;
        }
        //System.out.println("SectCode: "+r.getAttribute("code"));
        session.setAttribute("SectCode",r.getAttribute("code"));
       
        ADFUtils.findIterator("findAllOccupationsIterator").executeQuery();
        GlobalCC.refreshUI(allOccupTbl);
        ADFUtils.findIterator("fetchSelectedOccupationsIterator").executeQuery();
        GlobalCC.refreshUI(occupTbl);
    }

    public String addOccup() {
        

        txtOccupShtDesc.setValue(null);
        txtOccup.setValue(null);
        session.setAttribute("action", "A");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:p1" +
                             "').show(hints);");
        return null;
    }

    public String editOccup() {
        Object key = allOccupTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }

        txtOccupShtDesc.setValue(r.getAttribute("shortDesc"));
        txtOccup.setValue(r.getAttribute("name"));
        session.setAttribute("action", "E");
        session.setAttribute("pkey",
                             new BigDecimal(r.getAttribute("code").toString()));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:p1" +
                             "').show(hints);");
        return null;
    }

    public String deleteOccup() {
        Object key = allOccupTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("pkey",
                             new BigDecimal(r.getAttribute("code").toString()));
        String Query = "begin TQC_SETUPS_PKG.occupations_prc(?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, "D");
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("pkey"));
            cst.setString(3, null);
            cst.setString(4, null);
            //cst.setBigDecimal(5, (BigDecimal)session.getAttribute("SectCode"));
            cst.setObject(5, session.getAttribute("SectCode"));
            cst.execute();

            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findAllOccupationsIterator").executeQuery();
            GlobalCC.refreshUI(allOccupTbl);
            String message = "Record DELETED successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setLocationTab(RichShowDetailItem locationTab) {
        this.locationTab = locationTab;
    }

    public RichShowDetailItem getLocationTab() {
        return locationTab;
    }

    public void setTxtIdNumber(RichInputText txtIdNumber) {
        this.txtIdNumber = txtIdNumber;
    }

    public RichInputText getTxtIdNumber() {
        return txtIdNumber;
    }

    public void setTxtAddress(RichInputText txtAddress) {
        this.txtAddress = txtAddress;
    }

    public RichInputText getTxtAddress() {
        return txtAddress;
    }

    public void setTxtTelNumberVal(RichInputText txtTelNumberVal) {
        this.txtTelNumberVal = txtTelNumberVal;
    }

    public RichInputText getTxtTelNumberVal() {
        return txtTelNumberVal;
    }

    public void setTxtMobileNumber(RichInputText txtMobileNumber) {
        this.txtMobileNumber = txtMobileNumber;
    }

    public RichInputText getTxtMobileNumber() {
        return txtMobileNumber;
    }

    public void setTxtEmail(RichInputText txtEmail) {
        this.txtEmail = txtEmail;
    }

    public RichInputText getTxtEmail() {
        return txtEmail;
    }

    public void setTxtZipCode(RichInputText txtZipCode) {
        this.txtZipCode = txtZipCode;
    }

    public RichInputText getTxtZipCode() {
        return txtZipCode;
    }

    public void setTxttown(RichInputText txttown) {
        this.txttown = txttown;
    }

    public RichInputText getTxttown() {
        return txttown;
    }

    public String selectCountry() {
        Object key = countryTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        txtCountry.setValue(r.getAttribute("name"));
        session.setAttribute("countrycode", r.getAttribute("code"));
        GlobalCC.refreshUI(txtCountry);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:countryPopup" + "').hide(hints);");
        return null;
    }
    public void setTxtName(RichInputText txtName) {
        this.txtName = txtName;
    }

    public RichInputText getTxtName() {
        return txtName;
    }

    public void setTxtPin(RichInputText txtPin) {
        this.txtPin = txtPin;
    }

    public RichInputText getTxtPin() {
        return txtPin;
    }

    public void setTxtBankAccountNumber(RichInputText txtBankAccountNumber) {
        this.txtBankAccountNumber = txtBankAccountNumber;
    }

    public RichInputText getTxtBankAccountNumber() {
        return txtBankAccountNumber;
    }

    public void setTxtSwiftCode(RichInputText txtSwiftCode) {
        this.txtSwiftCode = txtSwiftCode;
    }

    public RichInputText getTxtSwiftCode() {
        return txtSwiftCode;
    }

    public void setCountryTbl(RichTable countryTbl) {
        this.countryTbl = countryTbl;
    }

    public RichTable getCountryTbl() {
        return countryTbl;
    }

    public void setTxtCountry(RichInputText txtCountry) {
        this.txtCountry = txtCountry;
    }

    public RichInputText getTxtCountry() {
        return txtCountry;
    }

    public String saveBussinessPerson() {
        String Query =
            "begin TQC_SETUPS_PKG.bussiness_person_proc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setObject(1, session.getAttribute("action"));
            cst.setObject(2, session.getAttribute("bpnCode"));
            cst.setObject(3, txtIdNumber.getValue());
            cst.setObject(4, txtAddress.getValue());
            cst.setObject(5, txtTelNumberVal.getValue());
            cst.setObject(6, txtMobileNumber.getValue());
            cst.setObject(7, txtEmail.getValue());
            cst.setObject(8, txtType.getValue());
            cst.setObject(9, txtZipCode.getValue());
            cst.setObject(10, txttown.getValue());
            cst.setObject(11, session.getAttribute("countryCode"));

            cst.setObject(12, txtName.getValue());
            cst.setObject(13, txtPin.getValue());
            cst.setObject(14, session.getAttribute("bbrCode"));

            cst.setObject(15, txtBankAccountNumber.getValue());
            cst.setObject(16, txtSwiftCode.getValue());
            cst.setObject(17, session.getAttribute("ClientCode"));
            cst.setObject(18, "C");
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("fetchAllBussinessPeopleIterator").executeQuery();
            GlobalCC.refreshUI(bussPersonTbl);
            //         FacesContext fc = FacesContext.getCurrentInstance();
            //          fc.getExternalContext().redirect("payeeDef.jspx");
            String message = "Record Inserted successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }
    

    public String newBussinessperson() {
        session.setAttribute("action", "A");
        // Clear the text fileds
        txtIdNumber.setValue(null);
        txtAddress.setValue(null);
        txtTelNumberVal.setValue(null);
        txtMobileNumber.setValue(null);
        txtEmail.setValue(null);
        txtType.setValue(null);
        txtZipCode.setValue(null);
        txttown.setValue(null);
        txtCountry.setValue(null);
        txtName.setValue(null);
        txtPin.setValue(null);
        txtBankAccountNumber.setValue(null);
        txtSwiftCode.setValue(null);
        //   txtClaimant.setValue("");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:ClientTypePop').show(hints);");
        return null;
    }

    public void setTxtType(RichSelectOneChoice txtType) {
        this.txtType = txtType;
    }

    public RichSelectOneChoice getTxtType() {
        return txtType;
    }

    public void setTxtClaimant(RichInputText txtClaimant) {
        this.txtClaimant = txtClaimant;
    }

    public RichInputText getTxtClaimant() {
        return txtClaimant;
    }

    public void setTxtBankBranch(RichInputText txtBankBranch) {
        this.txtBankBranch = txtBankBranch;
    }

    public RichInputText getTxtBankBranch() {
        return txtBankBranch;
    }

    public void setBankBranchTbl(RichTable bankBranchTbl) {
        this.bankBranchTbl = bankBranchTbl;
    }

    public RichTable getBankBranchTbl() {
        return bankBranchTbl;
    }

    public String selectBankbranch() {
        Object key = bankBranchTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        txtBankBranch.setValue(r.getAttribute("bbrBranchName"));
        session.setAttribute("bbrCode", r.getAttribute("bbrCode"));
        GlobalCC.refreshUI(txtBankBranch);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:bankBranchPopup" + "').hide(hints);");

        return null;
    }

    public void setRegistedClaimantTbl(RichTable registedClaimantTbl) {
        this.registedClaimantTbl = registedClaimantTbl;
    }

    public RichTable getRegistedClaimantTbl() {
        return registedClaimantTbl;
    }

    public String selectRegistedClaimants() {
        Object key = registedClaimantTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        txtClaimant.setValue(r.getAttribute("name"));
        session.setAttribute("regClaimantCode",
                             r.getAttribute("regClaimantCode"));
        GlobalCC.refreshUI(txtClaimant);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:claimantPop" + "').hide(hints);");
        return null;
    }

    public void setBussinessPersonTbl(RichTable bussinessPersonTbl) {
        this.bussinessPersonTbl = bussinessPersonTbl;
    }

    public RichTable getBussinessPersonTbl() {
        return bussinessPersonTbl;
    }

    public String editBussinessPerson() {
        session.setAttribute("action", "E");
        Object key = bussPersonTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        txtIdNumber.setValue(r.getAttribute("bpnIdNumber"));
        txtAddress.setValue(r.getAttribute("bpnAddress"));
        txtTelNumberVal.setValue(r.getAttribute("bpnTelNumber"));
        txtMobileNumber.setValue(r.getAttribute("bpnMobileNumber"));
        txtEmail.setValue(r.getAttribute("bpnEmail"));
        txtType.setValue(r.getAttribute("bpnType"));
        txtZipCode.setValue(r.getAttribute("bpnZip"));
        txttown.setValue(r.getAttribute("bpnTown"));
        txtCountry.setValue(r.getAttribute("bpnCountryCode"));
        txtName.setValue(r.getAttribute("bpnName"));
        txtPin.setValue(r.getAttribute("bpnPin"));
        txtBankBranch.setValue(r.getAttribute("bpnBranchName"));
        txtBankAccountNumber.setValue(r.getAttribute("bpnBankAccount"));
        txtSwiftCode.setValue(r.getAttribute("bpnBbrSwiftCode"));
        //      txtClaimant.setValue(r.getAttribute("bpnRegClaimant"));
        session.setAttribute("bpnCode", r.getAttribute("bpnCode"));
        session.setAttribute("countrycode", r.getAttribute("bpnCountryCode"));
        session.setAttribute("bbrCode", r.getAttribute("bpnBbrCode"));
        // session.setAttribute("regClaimantCode", r.getAttribute("bpnRegClmtCode"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:ClientTypePop').show(hints);");
        return null;
    }

    public String deleteBussinessPerson() {
        Object key = bussPersonTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        String Query =
            "begin TQC_SETUPS_PKG.bussiness_person_proc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setObject(1, "D");
            cst.setObject(2, r.getAttribute("bpnCode"));
            cst.setObject(3, null);
            cst.setObject(4, null);
            cst.setObject(5, null);
            cst.setObject(6, null);
            cst.setObject(7, null);
            cst.setObject(8, null);
            cst.setObject(9, null);
            cst.setObject(10, null);
            cst.setObject(11, null);

            cst.setObject(12, null);
            cst.setObject(13, null);
            cst.setObject(14, null);

            cst.setObject(15, null);
            cst.setObject(16, null);
            cst.setObject(17, null);
            cst.setObject(18, null);
            cst.execute();

            cst.close();
            conn.commit();
            conn.close();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "crm:ClientTypePop" + "').hide(hints);");
            ADFUtils.findIterator("fetchAllBussinessPeopleIterator").executeQuery();
            GlobalCC.refreshUI(bussPersonTbl);
            //        FacesContext fc = FacesContext.getCurrentInstance();
            //        fc.getExternalContext().redirect("payeeDef.jspx");
            String message = "Record DELETED successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setBussPersonTbl(RichTable bussPersonTbl) {
        this.bussPersonTbl = bussPersonTbl;
    }

    public RichTable getBussPersonTbl() {
        return bussPersonTbl;
    }

    public String savePayee() {

        String Query =
            "begin TQC_SETUPS_PKG.bussiness_person_proc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setObject(1, session.getAttribute("action"));
            cst.setObject(2, session.getAttribute("bpnCode"));
            cst.setObject(3, txtIdNumber.getValue());
            cst.setObject(4, txtAddress.getValue());
            cst.setObject(5, txtTelNumberVal.getValue());
            cst.setObject(6, txtMobileNumber.getValue());
            cst.setObject(7, txtEmail.getValue());
            cst.setObject(8, txtType.getValue());
            cst.setObject(9, txtZipCode.getValue());
            cst.setObject(10, txttown.getValue());
            cst.setObject(11, session.getAttribute("countryCode"));

            cst.setObject(12, txtName.getValue());
            cst.setObject(13, txtPin.getValue());
            cst.setObject(14, session.getAttribute("bbrCode"));

            cst.setObject(15, txtBankAccountNumber.getValue());
            cst.setObject(16, txtSwiftCode.getValue());
            cst.setObject(17, session.getAttribute("ClientCode"));
            cst.setObject(18, "A");
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("fetchAllBussinessPeopleIterator").executeQuery();
            GlobalCC.refreshUI(bussPersonTbl);
            //         FacesContext fc = FacesContext.getCurrentInstance();
            //          fc.getExternalContext().redirect("payeeDef.jspx");
            String message = "Record Saved successfully!"; 
            GlobalCC.hidePopup("pt1:ClientTypePop");
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    //  public String saveServProvPayee() {
    //
    //        String Query =
    //            "begin TQC_SETUPS_PKG.bussiness_person_proc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
    //        DBConnector connector = new DBConnector();
    //        OracleCallableStatement cst = null;
    //        OracleConnection conn = null;
    //
    //        try {
    //            conn = connector.getDatabaseConnection();
    //            cst = (OracleCallableStatement)conn.prepareCall(Query);
    //
    //            cst.setObject(1, session.getAttribute("action"));
    //            cst.setObject(2, session.getAttribute("bpnCode"));
    //            cst.setObject(3, txtIdNumber.getValue());
    //            cst.setObject(4, txtAddress.getValue());
    //            cst.setObject(5, txtTelNumberVal.getValue());
    //            cst.setObject(6, txtMobileNumber.getValue());
    //            cst.setObject(7, txtEmail.getValue());
    //            cst.setObject(8, txtType.getValue());
    //            cst.setObject(9, txtZipCode.getValue());
    //            cst.setObject(10, txttown.getValue());
    //            cst.setObject(11, session.getAttribute("countryCode"));
    //
    //            cst.setObject(12, txtName.getValue());
    //            cst.setObject(13, txtPin.getValue());
    //            cst.setObject(14, session.getAttribute("bbrCode"));
    //
    //            cst.setObject(15, txtBankAccountNumber.getValue());
    //            cst.setObject(16, txtSwiftCode.getValue());
    //            cst.setObject(17, session.getAttribute("ClientCode"));
    //          cst.setObject(18, "A");
    //            cst.execute();
    //             cst.close();
    //            conn.commit();
    //            conn.close();
    //
    //            ADFUtils.findIterator("fetchAllBussinessPeopleIterator").executeQuery();
    //            GlobalCC.refreshUI(bussPersonTbl);
    //    //         FacesContext fc = FacesContext.getCurrentInstance();
    //    //          fc.getExternalContext().redirect("payeeDef.jspx");
    //            String message = "Record Inserted successfully!";
    //            GlobalCC.INFORMATIONREPORTING(message);
    //
    //        } catch (Exception e) {
    //            GlobalCC.EXCEPTIONREPORTING(conn, e);
    //        }
    //        return null;
    //    }

    public String saveServProvPayee() {
        String Query =
            "begin TQC_SETUPS_PKG.bussiness_person_proc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setObject(1, session.getAttribute("action"));
            cst.setObject(2, session.getAttribute("bpnCode"));
            cst.setObject(3, txtIdNumber.getValue());
            cst.setObject(4, txtAddress.getValue());
            cst.setObject(5, txtTelNumberVal.getValue());
            cst.setObject(6, txtMobileNumber.getValue());
            cst.setObject(7, txtEmail.getValue());
            cst.setObject(8, txtType.getValue());
            cst.setObject(9, txtZipCode.getValue());
            cst.setObject(10, txttown.getValue());
            cst.setObject(11, session.getAttribute("countryCode"));

            cst.setObject(12, txtName.getValue());
            cst.setObject(13, txtPin.getValue());
            cst.setObject(14, session.getAttribute("bbrCode"));

            cst.setObject(15, txtBankAccountNumber.getValue());
            cst.setObject(16, txtSwiftCode.getValue());
            cst.setObject(17, session.getAttribute("ClientCode"));
            cst.setObject(18, "S");
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("fetchAllBussinessPeopleIterator").executeQuery();
            GlobalCC.refreshUI(bussPersonTbl);

            String message = "Record Inserted successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTxtLandmark(RichInputText txtLandmark) {
        this.txtLandmark = txtLandmark;
    }

    public RichInputText getTxtLandmark() {
        return txtLandmark;
    }

    public String actionNewDist() {
        if (session.getAttribute("stateCode") == null) {
            if (session.getAttribute("ADMIN_REG_TYPE") == null)
                GlobalCC.INFORMATIONREPORTING("You need to select an existing Administrative Region to proceed.");
            GlobalCC.INFORMATIONREPORTING("You need to select an existing " +
                                          GlobalCC.formatAdminUnitPlural(session.getAttribute("ADMIN_REG_TYPE")));

            return null;
        } else {

            // Clear the text fileds
            txtDistCodePopup.setValue(null);
            txtDistShtDescPopup.setValue(null);
            txtDistNamePopup.setValue(null);
            txtDistCountryPopup.setValue(session.getAttribute("countryCode"));
            txtDistSTSCodePopup.setValue(session.getAttribute("stateCode"));

            btnCreateUpdateDist.setText("Save");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:countryDistPopup').show(hints);");
        }

        return null;
    }

    public String actionEditDist() {
        Object key2 = tblCountryDists.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDistCodePopup.setValue(nodeBinding.getAttribute("code"));
            txtDistCountryPopup.setValue(nodeBinding.getAttribute("countryCode"));
            txtDistShtDescPopup.setValue(nodeBinding.getAttribute("shortDesc"));
            txtDistNamePopup.setValue(nodeBinding.getAttribute("name"));
            txtDistSTSCodePopup.setValue(nodeBinding.getAttribute("STSCode"));

            btnCreateUpdateDist.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:countryDistPopup').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        return null;
    }

    public String actionDeleteDist() {
        Object key2 = tblCountryDists.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String Query = "begin TQC_SETUPS_PKG.dists_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal((String)nodeBinding.getAttribute("code")));
                cst.setString(3,
                              (String)nodeBinding.getAttribute("countryCode"));
                cst.setString(4,
                              (String)nodeBinding.getAttribute("shortDesc"));
                cst.setString(5, (String)nodeBinding.getAttribute("name"));
                cst.setString(6, (String)nodeBinding.getAttribute("STSCode"));

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchDistrictsByStateIterator").executeQuery();
                GlobalCC.refreshUI(tblCountryDists);

                String message = "Record DELETED Successfully!";
                showMessagePopup(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String actionCreateUpdateDist() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateDist.getText().equals("Save")) {

            // Proceed to save
            String status = actionCreateNewDist();

        } else {

            // Proceed to do an update
            String distCode =
                GlobalCC.checkNullValues(txtDistCodePopup.getValue());
            String distCountryCode =
                GlobalCC.checkNullValues(txtDistCountryPopup.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtDistShtDescPopup.getValue());
            String distName =
                GlobalCC.checkNullValues(txtDistNamePopup.getValue());
            String stsCode =
                GlobalCC.checkNullValues(txtDistSTSCodePopup.getValue());

            if (distCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter District Code");
                return null;

            } else if (distCountryCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a Country");
                return null;

            } else if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
                return null;

            } else if (distName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter District Name");
                return null;

            } else if (stsCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Type STS Code");
                return null;

            } else {

                String Query =
                    "begin TQC_SETUPS_PKG.dists_prc(?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, "E");
                    cst.setBigDecimal(2, new BigDecimal(distCode));
                    cst.setString(3, distCountryCode);
                    cst.setString(4, shortDesc);
                    cst.setString(5, distName);
                    cst.setString(6, stsCode);

                    cst.execute();

                    ADFUtils.findIterator("fetchDistrictsByStateIterator").executeQuery();
                    GlobalCC.refreshUI(tblCountryDists);

                    cst.close();
                    conn.commit();
                    conn.close();
                    GlobalCC.dismissPopUp("pt1", "countryDistPopup");
                    GlobalCC.INFORMATIONREPORTING("Record Updated Successfully!");

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionCreateNewDist() {

        String distCountryCode =
            GlobalCC.checkNullValues(txtDistCountryPopup.getValue());
        String shortDesc =
            GlobalCC.checkNullValues(txtDistShtDescPopup.getValue());
        String distName =
            GlobalCC.checkNullValues(txtDistNamePopup.getValue());
        String stsCode =
            GlobalCC.checkNullValues(txtDistSTSCodePopup.getValue());

        if (distCountryCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select a Country");
            return null;

        } else if (shortDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type Short Description");
            return null;

        } else if (distName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter District Name");
            return null;

        } else if (stsCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Type STS Code");
            return null;

        } else {

            String Query = "begin TQC_SETUPS_PKG.dists_prc(?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, distCountryCode);
                cst.setString(4, shortDesc);
                cst.setString(5, distName);
                cst.setString(6, stsCode);

                cst.execute();

                ADFUtils.findIterator("fetchDistrictsByStateIterator").executeQuery();
                GlobalCC.refreshUI(tblCountryDists);

                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.dismissPopUp("pt1", "countryDistPopup");
                GlobalCC.INFORMATIONREPORTING("New Record Created Successfully!");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

        return "Success";
    }

    public void setTblCountryDists(RichTable tblCountryDists) {
        this.tblCountryDists = tblCountryDists;
    }

    public RichTable getTblCountryDists() {
        return tblCountryDists;
    }

    public void setTxtDistCodePopup(RichInputText txtDistCodePopup) {
        this.txtDistCodePopup = txtDistCodePopup;
    }

    public RichInputText getTxtDistCodePopup() {
        return txtDistCodePopup;
    }

    public void setTxtDistCountryPopup(RichInputText txtDistCountryPopup) {
        this.txtDistCountryPopup = txtDistCountryPopup;
    }

    public RichInputText getTxtDistCountryPopup() {
        return txtDistCountryPopup;
    }

    public void setTxtDistShtDescPopup(RichInputText txtDistShtDescPopup) {
        this.txtDistShtDescPopup = txtDistShtDescPopup;
    }

    public RichInputText getTxtDistShtDescPopup() {
        return txtDistShtDescPopup;
    }

    public void setTxtDistNamePopup(RichInputText txtDistNamePopup) {
        this.txtDistNamePopup = txtDistNamePopup;
    }

    public RichInputText getTxtDistNamePopup() {
        return txtDistNamePopup;
    }

    public void setTxtDistSTSCodePopup(RichInputText txtDistSTSCodePopup) {
        this.txtDistSTSCodePopup = txtDistSTSCodePopup;
    }

    public RichInputText getTxtDistSTSCodePopup() {
        return txtDistSTSCodePopup;
    }

    public void setBtnCreateUpdateDist(RichCommandButton btnCreateUpdateDist) {
        this.btnCreateUpdateDist = btnCreateUpdateDist;
    }

    public RichCommandButton getBtnCreateUpdateDist() {
        return btnCreateUpdateDist;
    }

    public void setReportDays(RichInputNumberSpinbox reportDays) {
        this.reportDays = reportDays;
    }

    public RichInputNumberSpinbox getReportDays() {
        return reportDays;
    }

    public void setTxtAgentType(RichInputText txtAgentType) {
        this.txtAgentType = txtAgentType;
    }

    public RichInputText getTxtAgentType() {
        return txtAgentType;
    }

    public void setTxtAgentTypeTbl(RichTable txtAgentTypeTbl) {
        this.txtAgentTypeTbl = txtAgentTypeTbl;
    }

    public RichTable getTxtAgentTypeTbl() {
        return txtAgentTypeTbl;
    }

    public void setAllOccupTbl(RichTable allOccupTbl) {
        this.allOccupTbl = allOccupTbl;
    }

    public RichTable getAllOccupTbl() {
        return allOccupTbl;
    }
    
    
    public String saveSectorOccupation() {
        
        if(session.getAttribute("SectCode")==null){
            GlobalCC.INFORMATIONREPORTING("Select Sector to continue");
            return null;
        }
        
        Object key2 = allOccupTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        
        if(nodeBinding==null){
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        int rowCnt = allOccupTbl.getRowCount();  
        
        String Query = "begin TQC_SETUPS_PKG.occupations_sections_prc(?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection(); 
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            if(rowCnt>0) {
              for(int i = 0; i < rowCnt; i++){
                               JUCtrlValueBinding r =
                                   (JUCtrlValueBinding)allOccupTbl.getRowData(i);
                             Boolean  Accept = (Boolean)r.getAttribute("selected"); 
                               if(Accept == true){  
                                       cst.setString(1, "A");
                                       cst.setObject(2, r.getAttribute("code"));
                                       cst.setObject(3, session.getAttribute("SectCode"));
                                       cst.addBatch();  
                                   } 
                 } 
                cst.executeBatch();
                cst.close();
                conn.commit();  
            } 
            conn.close();

            ADFUtils.findIterator("findAllOccupationsIterator").executeQuery();
            GlobalCC.refreshUI(allOccupTbl);
            ADFUtils.findIterator("fetchSelectedOccupationsIterator").executeQuery();
            GlobalCC.refreshUI(occupTbl);
        
            String message = "Record UPDATED successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }
    
    public String deleteSectorOccupation() {
        
        if(session.getAttribute("SectCode")==null){
            GlobalCC.INFORMATIONREPORTING("Select Sector to continue");
            return null;
        }
        
        Object key2 = occupTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        
        if(nodeBinding==null){
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        int rowCnt = occupTbl.getRowCount();  
        
        String Query = "begin TQC_SETUPS_PKG.occupations_sections_prc(?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = connector.getDatabaseConnection();
            conn = connector.getDatabaseConnection(); 
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            if(rowCnt>0) {
              for(int i = 0; i < rowCnt; i++){
                               JUCtrlValueBinding r =
                                   (JUCtrlValueBinding)occupTbl.getRowData(i);
                             Boolean  Accept = (Boolean)r.getAttribute("selected"); 
                               if(Accept == true){  
                                       cst.setString(1, "D");
                                       cst.setObject(2, r.getAttribute("code"));
                                       cst.setObject(3, session.getAttribute("SectCode"));
                                       cst.addBatch();  
                                       System.out.println("Selected: "+i);
                                   } 
                 } 
                cst.executeBatch();
                cst.close();
                conn.commit();  
            } 
            conn.close();
           
            ADFUtils.findIterator("findAllOccupationsIterator").executeQuery();
            GlobalCC.refreshUI(allOccupTbl);
            ADFUtils.findIterator("fetchSelectedOccupationsIterator").executeQuery();
            GlobalCC.refreshUI(occupTbl);
            
            String message = "Record UPDATED successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setTxtSearchCountry(RichInputText txtSearchCountry) {
        this.txtSearchCountry = txtSearchCountry;
    }

    public RichInputText getTxtSearchCountry() {
        return txtSearchCountry;
    }
   
    public List<SelectItem> getAgentClasses() {
         List<SelectItem> agentTypes = new ArrayList<SelectItem>();
        
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;

        OracleCallableStatement statement = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        String status = null;
        Boolean value = true;
        try {
            conn = dbConnector.getDatabaseConnection();
            if(!GlobalCC.tableExists("tqc_agent_classes")) 
            {
                return agentTypes;    
            }
            String query1 = "select agncl_class_sht_desc, agncl_class from tqc_agent_classes";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.execute();
            rst = (OracleResultSet)stmt.getObject(1);
            while (rst.next()) {
                    agentTypes.add(new SelectItem(rst.getString(1),rst.getString(2)));
            }
            rst.close();
            stmt.close();
            conn.close(); 
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return agentTypes;
    }

    public void setTxtAccountType(RichInputText txtAccountType) {
        this.txtAccountType = txtAccountType;
    }

    public RichInputText getTxtAccountType() {
        return txtAccountType;
    }

    public void setTblAccountTypesPop(RichTable tblAccountTypesPop) {
        this.tblAccountTypesPop = tblAccountTypesPop;
    }

    public RichTable getTblAccountTypesPop() {
        return tblAccountTypesPop;
    }
    /**
     * Assign the selected Account Type into the text fields
     *
     * @return null
     */
    public String actionSelectAccountTypePop() {
        Object key2 = tblAccountTypesPop.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;
        if (n != null) {
            session.setAttribute("AGNTY_ACT_CODE", n.getAttribute("ACT_CODE"));
            txtAccountType.setValue(n.getAttribute("ACT_ACCOUNT_TYPE"));
            GlobalCC.refreshUI(txtAccountType);
        }
        GlobalCC.hidePopup("pt1:accountTypesPop");
        return null;
    }

    public void setTxtAgentTypeShtDesc(RichSelectOneChoice txtAgentTypeShtDesc) {
        this.txtAgentTypeShtDesc = txtAgentTypeShtDesc;
    }

    public RichSelectOneChoice getTxtAgentTypeShtDesc() {
        return txtAgentTypeShtDesc;
    }

    public void setChkOccSelect(RichSelectBooleanCheckbox chkOccSelect) {
        this.chkOccSelect = chkOccSelect;
    }

    public RichSelectBooleanCheckbox getChkOccSelect() {
        return chkOccSelect;
    }

    public void setChkOrmSelect(RichSelectBooleanCheckbox chkOrmSelect) {
        this.chkOrmSelect = chkOrmSelect;
    }

    public RichSelectBooleanCheckbox getChkOrmSelect() {
        return chkOrmSelect;
    }
    public void setTxtClientPerson(RichSelectOneChoice txtClientPerson) {
        this.txtClientPerson = txtClientPerson;
    }

    public RichSelectOneChoice getTxtClientPerson() {
        return txtClientPerson;
    }

    public void setTxtSuffix(RichInputNumberSpinbox txtSuffix) {
        this.txtSuffix = txtSuffix;
    }

    public RichInputNumberSpinbox getTxtSuffix() {
        return txtSuffix;
    }

    public void setTxtPrefix(RichInputText txtPrefix) {
        this.txtPrefix = txtPrefix;
    }

    public RichInputText getTxtPrefix() {
        return txtPrefix;
    }

    public void actionSelectServiceProvider(SelectionEvent selectionEvent) {
       Object key=tblServiceProviderType.getSelectedRowData();
       JUCtrlValueBinding node=(JUCtrlValueBinding)key;
       
        session.setAttribute("vSptCode", node.getAttribute("sptCode"));
       try{       session.setAttribute("vSptCode", node.getAttribute("sptCode"));}
       catch(Exception e){}
       
        ADFUtils.findIterator("fetchAllServiceProviderTypesActivitiesIterator").executeQuery();
        GlobalCC.refreshUI(servProviderTypesAct);
       
    }

    public String actionDelServProvider() {
        
        Object key2 = tblServiceProviderType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding == null)
        
        
            {
            GlobalCC.errorValueNotEntered("No selected record");
            return null;
            }
        
        
            String Query ="begin TQC_SETUPS_PKG.service_provider_types_prc(?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setObject(2, session.getAttribute("vSptCode"));
                                 // new BigDecimal(nodeBinding.getAttribute("sptCode").toString()));
                cst.setString(3,
                              nodeBinding.getAttribute("shortDesc").toString());
                cst.setString(4, nodeBinding.getAttribute("name").toString());
                cst.setString(5,
                              nodeBinding.getAttribute("status").toString());
                cst.setBigDecimal(6, null);
                cst.setBigDecimal(7, null);
                cst.setBigDecimal(8, null);

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchAllServiceProviderTypesIterator").executeQuery();
                AdfFacesContext.getCurrentInstance().addPartialTarget(tblServiceProviderType);
                
                
                
                
                
                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);
                
                
              

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
       
       
       
        return null;
    }

    public String actionEditSerProv() {

        Object key2 = tblServiceProviderType.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding == null) {
            
                GlobalCC.errorValueNotEntered("No selected record");
            return null;
            }
        
        
            txtProviderTypeCodePop.setValue(nodeBinding.getAttribute("sptCode"));
            txtProviderTypeShtDescPop.setValue(nodeBinding.getAttribute("shortDesc"));
            txtProviderTypeNamePop.setValue(nodeBinding.getAttribute("name"));
            txtProviderTypeStatus.setValue(nodeBinding.getAttribute("status"));
            txtProviderTypeWhtxRatePop2.setValue(nodeBinding.getAttribute("whtxRate"));
            txtProviderVatRatePop2.setValue(nodeBinding.getAttribute("vatRate"));
            
            txtProviderTypeWhtxRatePop.setValue(nodeBinding.getAttribute("whtxRate"));
            txtProviderVatRatePop.setValue(nodeBinding.getAttribute("vatRate"));
        
            
            txtSuffix.setValue(nodeBinding.getAttribute("suffix"));

            btnCreateUpdateProviderType.setText("Update");

            GlobalCC.showPop("pt1:serviceProviderTypePopup");
      
      
        return null;
    }
    
    
}
