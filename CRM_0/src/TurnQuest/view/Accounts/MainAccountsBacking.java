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

package TurnQuest.view.Accounts;


import TurnQuest.view.Alerts.JBPMEngine;
import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Base.Util;
import TurnQuest.view.Connect.Authorization;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Ui.FormUi;
import TurnQuest.view.bpm.TaskManipulation;
import TurnQuest.view.commons.UtilDAO;
import TurnQuest.view.models.AgencySystem;
import TurnQuest.view.utilities.CSVtoADFTableProcessor;
import TurnQuest.view.web.IprsSetupsApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.math.BigDecimal;

import java.net.FileNameMap;
import java.net.URLConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTree;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.input.RichInputNumberSpinbox;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanRadio;
import oracle.adf.view.rich.component.rich.input.RichSelectManyShuttle;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.component.rich.output.RichPanelCollection;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.binding.BindingContainer;

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

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


/**
 * The base backing bean for all the accounts related pages. Includes
 * properties and methods that map to given  UI components in the relevant
 * pages of the accounts.
 *
 * @author Frankline Ogongi
 */
public class MainAccountsBacking {
    //there are 4 references to this procedure! so if you change this make sure you change the argment
    public static final String SETUPS_AGENCIES_PRC =
        "begin TQC_SETUPS_PKG.agencies_prc(" + "?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
        "?,?,?,?,?,?,?,?,?); end;";
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private Rendering renderer = new Rendering();

    private IprsSetupsApi iprsApi = null;

    /** An array of selectItem that will fill the AccountsType selectOneChoice */
    private List<SelectItem> accountsTypeElements =
        new ArrayList<SelectItem>();

    /** Variable to hold the selected Service Provider Type Value */
    private String accountsTypeSelectedValue;

    private BindingContainer bindings;
    private RichPanelBox requiredDocsBox;
    private RichTree treeAgencies;
    private RichPanelBox panelAgencyDetails;
    private RichInputText txtAgencyCode;
    private RichInputText txtAccountTypeCode;
    private RichInputText txtShortDesc;
    private RichInputText txtAgencyName;
    private RichInputText txtAgencyPhysicalAddress;
    private RichInputText txtAgencyPostalAddress;
    private RichInputText txtAgencyCountryCode;
    private RichInputText txtAgencyCountryName;
    private RichInputText txtAgencyTownCode;
    private RichInputText txtAgencyTownName;
    private RichInputText txtAgencyEmail;
    private RichInputText txtAgencyWebAddress;
    private RichInputText txtAgencyPostalCode;
    private RichInputText txtContactPerson;
    private RichInputText txtContactTitle;
    private RichInputText txtAgencyPhone1;
    private RichInputText txtAgencyPhone2;
    private RichInputText txtAgencyFax;
    private RichInputText txtAgencyAccountNumber;
    private RichInputText txtAgencyPIN;
    private RichInputNumberSpinbox txtAgencyCommission;
    private RichInputNumberSpinbox txtCommLevyRate;
    private RichSelectOneChoice txtCreditAllowed;
    private RichInputNumberSpinbox txtAgencyWthTx;
    private RichSelectOneChoice txtAgencyPrintdebitNote;
    private RichSelectOneChoice txtAgencyStatus;
    private RichInputDate txtDateCreated;
    private RichInputText txtCreatedBy;
    private RichInputText txtUpdatedBy;
    private RichInputDate txtUpdatedOn;
    private RichInputText txtRegistartionCode;
    private RichInputText txtCommReserveRate;
    private RichInputText txtAnnualBudget;
    private RichInputDate txtStaEffectiveDate;
    private RichInputText txtCreditPeriod;
    private RichInputDate txtCommStatEffectiveDate;
    private RichInputDate txtCommStatusDate;
    private RichSelectOneChoice txtCommAllowed;
    private RichSelectOneChoice txtAgencyChecked;
    private RichInputText txtAgencyCheckedBy;
    private RichInputDate txtAgencyCheckDate;
    private RichSelectOneChoice txtCompCommArrears;
    private RichSelectOneChoice txtAgencyReinsurer;
    private RichInputText txtStatusDesc;
    private RichInputText txtAgencyIDNum;
    private RichInputText txtAgencyContractCode;
    private RichInputText txtAgencyAgentCode;
    private RichInputText txtAgencySms;
    private RichInputText txtAgencyHoldingCompanyCode;
    private RichInputText txtAgencySectorCode;
    private RichInputText txtAgencyClassCode;
    private RichInputDate txtAgencyExpiriyDate;
    private RichInputText txtAgencyLicenseNum;
    private RichSelectOneChoice txtAgencyRunoff;
    private RichSelectOneChoice txtAgencyLicensed;
    private RichSelectOneChoice txtCreditRting;
    private RichInputText txtLicenseGracePeriod;
    private RichInputText txtOldAccountNum;
    private RichInputText txtAgencyStatusRemarks;
    private RichCommandButton btnCreateUpdateAgency;
    private RichCommandButton btnCancelAgency;
    private RichInputText txtAgencyBranchCode;
    private RichInputText txtAgencySBUCode;
    private RichTable tblAgencyCountry;
    private RichTable tblAgencyTown;
    private RichTable tblAgencyHoldingCompany;
    private RichInputText txtAgencyHoldingCompanyName;
    private RichTable tblAgencyClasses;
    private RichTable tblAgencySector;
    private RichInputText txtAgencySectorName;
    private RichInputText txtAgencyClassName;
    private RichTable tblAgencyBranch;
    private RichTable tblAgencySBU;
    private RichInputText txtAgencyBranchName;
    private RichInputText txtAgencySBUName;
    private RichTable registrationTable;
    private RichCommandButton saveRegistrationDetails;
    private RichInputText regkey;
    private RichInputDate regyear;
    private RichInputText regno;
    private RichInputDate regWef;
    private RichInputDate regWet;
    private RichSelectOneChoice regAccepted;
    private RichTable agentDirectorsTable;
    private RichInputText agencyDirectorId;
    private RichInputDate directYr;
    private RichInputText directname;
    private RichInputText directQualifications;
    private RichInputNumberSpinbox directshare;
    private RichCommandButton saveAgDirectorButton;
    private RichTable agencyRefereeTable;
    private RichCommandButton saveRefereeAgButton;
    private RichInputText refereeId;
    private RichInputText refereeName;
    private RichInputText refereephAddress;
    private RichInputText refereePostAddress;
    private RichInputText refereeIDNo;
    private RichInputText refereeEmail;
    private RichInputText refereeTelNo;
    private List<SelectItem> selectValues = new ArrayList<SelectItem>();
    private List<String> displayValue = new ArrayList<String>();
    private RichSelectManyShuttle systemShuttle;
    private UISelectItems systemSelectItem;
    private RichTable webUsersTable;
    private RichCommandButton saveWebUsersButton;
    private RichInputText webUserId;
    private RichInputText webusername;
    private RichInputText webfullNames;
    private RichInputText webPassword;
    private RichInputText webEmail;
    private RichInputText webPersonalRank;
    private RichSelectOneRadio webAllowLogin;
    private RichSelectOneChoice webUserStatus;
    private RichSelectOneRadio webReset;
    private RichSelectOneChoice accountTypeSelector;
    private RichPanelBox panelAccountSystems;
    private RichCommandButton btnCreateUpdateCurrentAgency;
    private RichPanelBox panelDetailSystems;
    private RichInputText txtSelectedUserSystemCode;
    private RichCommandButton btnAddAccountSystem;
    private RichCommandButton btnRemoveAccountSystem;
    private RichTree treeUnassignedSystems;
    private RichTree treeAssignedSystems;
    private RichTable tblAgencyInfo;
    private RichOutputText textToShow;
    private RichInputText hiddenAccountCode;
    private RichTable tblAgencyAccounts;
    private RichInputText txtAccountCode;
    private RichInputText txtAccShortDesc;
    private RichInputText txtAccName;
    private RichInputText txtAccAgencyCode;
    private RichInputText txtAccCreatedBy;
    private RichInputDate txtAccDateCreated;
    private RichSelectOneChoice txtAgencyAccStatus;
    private RichInputText txtAccRemarks;
    private RichInputDate txtAccWef;
    private RichInputDate txtAccWet;
    private RichCommandButton btnSaveUpdateAgencyAccount;
    private RichTable tblGlAccounts;
    private RichInputText txtDivCode;
    private RichTable divisionLov;
    private RichPanelTabbed mainPanelTab;
    private RichPanelBox pbMainPanelDetail;
    private RichTable tblAccountTypesPop;
    private RichInputText txtMainAccountType;
    private RichInputText txtMainAccountTypeCode;
    private RichInputText txtDivName;
    private RichTable tblAssignedAgencySystems;
    private RichPanelBox panelAgencySystem;
    private RichInputText txtAsysSysCode;
    private RichInputText txtAsysAgnCode;
    private RichInputDate txtAsysWef;
    private RichInputDate txtAsysWet;
    private RichInputText txtAsysCode;
    private RichInputText txtAsysOsdCode;
    private RichInputText txtOsdName;
    private RichCommandButton btnSaveUpdateAgencySystem;
    private RichInputText txtAsysComments;
    private RichTable tblOrgSubDivisions;
    private RichSelectBooleanRadio genAcSelectYes;
    private RichSelectBooleanRadio genAccNoSelect;
    private RichPanelGroupLayout shortDescYesOrNoLayout;
    private RichTable tbListAgencies;
    private RichInputText txtStateCode;
    private RichInputText txtStateName;
    private RichTable tblStates;
    private RichTable tblTownPop;
    private RichCommandButton actionShowStates;
    private RichTable tbTownListing;
    private RichInputText txtAStateName;
    private RichInputText txtAStateCode;
    private RichCommandButton btnDeleteAgency;
    private RichOutputLabel genAccLabel;
    private RichPopup agentAccountPop;
    private RichTable tblAdminRegions;
    private RichPanelLabelAndMessage pnmsgAdminRegionName;
    private RichPanelLabelAndMessage pnmsgTownName;
    private RichPanelFormLayout frmLoadAdminTownDetails;
    private RichDialog dlgAdminRegionList;
    private RichInputText txtBankBranch;
    private RichInputText txtBankName;
    private RichInputText txtBankCode;
    private RichInputText txtBankBranchCode;
    private RichTable tblBanks;
    private RichTable tblBankBranches;
    private RichInputText txtAccountNo;
    private RichInputText txtPrefix;
    private RichTable tblAssignedClients;
    private RichTable tblUnAssignedClients;
    private RichSelectBooleanCheckbox chBoxUnAssigedClients;
    private RichSelectBooleanCheckbox chBoxAssigedClients;
    private RichTable tblPersonnel;
    private RichInputNumberSpinbox txtAsysOsdId;
    private RichTable tblAccountTypesPopEdit;
    private RichTable unAssignedServiceProviders;
    private RichSelectBooleanCheckbox chAssignServiceprovs;
    private RichTable selected;
    private RichTable asssignedServiceProviders;
    private RichSelectBooleanCheckbox cbBoxassignedServiceProviders;
    private RichTable agentTbl;
    private RichInputText txtAgent;
    private RichCommandButton txtAgntLov;
    private RichShowDetailItem subAgntDetailsTab;
    private RichTable agentsTbl;
    private RichInputText telNo;
    private RichPanelLabelAndMessage txtAgentsAccountsLabel;
    private RichInputText txtSaccoName;
    private RichInputText txtSacco;
    private RichTable txtSaccoTbl;
    private RichInputText txtMarketer;
    private RichTable marketerTbl;
    private RichInputText txtBranches;
    private RichTable txtSystems;
    private RichTable unitManagerTbl;
    private RichInputText txtUnitManager;
    private RichPanelBox pbPromManager;
    private RichSelectBooleanRadio txtPromote;
    private RichSelectOneRadio txtAgentChoices;
    private RichTable branchTbl;
    private RichInputText txtBranchName;
    private RichInputText txtReplaceMgr;
    private RichInputDate txtEffectiveDate;
    private RichCommandButton clbPromote;
    private RichCommandButton clbDemote;
    private RichCommandButton cmbReplaceMgr;
    private RichCommandButton cmbPromoteDemoteAgent;
    private RichSelectOneChoice sltTransType;
    private RichSelectOneChoice promotedemoteAction;
    private RichOutputLabel lblBranch;
    private RichCommandButton clbBranchDrop;
    private RichSelectOneChoice sltTransTypeDem;
    private RichOutputLabel lblAgencyBranch;
    private RichOutputLabel lblReplacementMgr;
    private RichCommandButton clbUnitManager;
    private RichCommandButton clbDemoteCmb;
    private RichCommandButton clbAuthDemotion;
    private RichOutputLabel txtTransactionType;


    private RichInputNumberSpinbox txtAgencyPrefix;
    private RichInputNumberSpinbox txtUnitPrefix;
    private RichInputNumberSpinbox txtAgencySeq;
    private RichOutputLabel lblEffectiveDate;
    private RichOutputLabel lblAgencyPrefix;
    private RichOutputLabel lblUnitPrefix;
    private RichOutputLabel lblAgencySeq;
    private RichOutputLabel precontractCode;
    private RichInputNumberSpinbox txtPrecontractCode;

    private RichInputNumberSpinbox txtCreditLimit;
    private RichInputText txtPhoneNumber;
    private RichInputText txtBranchUnits;
    private RichTable branchUnitsTbl;
    private RichSelectOneChoice txtLocalInt;
    private RichSelectOneChoice txtPrincipleDirector;
    private RichInputText txtIraRegNumber;
    private RichCommandButton btnAuthorizeAgent;
    private RichCommandButton btnMakeReadyAgent;
    private RichCommandButton btnSuspendAgent;
    private RichCommandButton btnDeactivateAgent;
    private RichTable countryTbl;
    private RichInputText txtNationality;
    private RichInputText txtRatingOrg;
    private RichInputText txtRating;
    private RichTable orgRatingTbl;
    private RichTable replaceMgrsTbl;
    private RichTable ratingTbl;
    private RichSelectOneChoice txtAgencySelected;
    private RichSelectOneChoice txtIssueCert;
    private RichTable branchTblDtls;
    private RichInputText txtBranch;
    private RichTable countryTblDtls;
    private RichInputText txtCountry;
    private RichTable prefixTbl;
    private RichInputText txtPrefixVals;
    private RichTable clientTitleTbl;
    private RichTable bankTbl;
    private RichTable bankBranchTbl;
    private RichTable sectorTbl;
    private RichInputText txtSector;
    private RichInputText txtShtDesc;
    private RichInputText txtPhysicalAddress;
    private RichInputText txtPostalAddress;
    private RichCommandButton txtSelectClientTitle;
    private RichInputText txtClientTitle;
    private RichTable clientTitleTblDtls;
    private RichInputText txtName;
    private RichInputText txtEmail;
    private RichInputText txtFax;
    private RichInputText txtBankAccountNumber;
    private RichInputText txtZip;
    private RichInputText txtOffice;
    private RichInputText txtIdNumber;
    private RichInputText txtOldAccount;
    private RichInputDate txtWef;
    private RichInputDate txtWet;
    private RichTable townPopTbl;
    private RichCommandButton txtNextBtn;
    private RichInputText txtSectors;
    private RichInputText txtTown;
    private RichInputText txtSms;
    private HtmlPanelGrid gridClientSearchDetails;
    private RichPanelGroupLayout searchFormHolder;
    private RichPanelFormLayout SEARCHHOLDER;
    private RichInputText txtSearchName;
    private RichInputText txtSearchShortDesc;
    private RichInputText txtSearchPhysical;
    private RichInputText txtSearchPostal;
    private RichInputText txtSearchSector;
    private RichPanelLabelAndMessage resetSearchContainer;
    private RichInputText txtSrchSectorName;
    private RichCommandButton btnSectorLov;
    private RichPanelLabelAndMessage statusHolder;
    private RichSelectOneChoice txtSearchStatus;
    private RichInputDate clntDateCreatedFrom;
    private RichInputDate clntDateCreatedTo;
    private RichInputText pinNumber;
    private RichSelectBooleanRadio rbtnSearchAccountNo;
    private RichSelectBooleanRadio rbtnPartOfAnyName;
    private RichSelectBooleanRadio rbtnExactName;
    private RichSelectBooleanRadio rbtnStatus;
    private RichSelectBooleanRadio rbtnShortDesc;
    private RichSelectBooleanRadio rbtnShortDescLeg;
    private RichSelectBooleanRadio rbtnPhySicalAddr;
    private RichSelectBooleanRadio rbtnPostalAddr;
    private RichSelectBooleanRadio rbtnCustomerId;
    private RichSelectBooleanRadio rbtnOldNames;
    private RichSelectBooleanRadio txtPinNumber;
    private RichPanelCollection panelCollSearch;
    private RichSelectBooleanRadio rbtnSector;
    private RichSelectBooleanRadio rbtnDateCreated;
    private RichSelectBooleanRadio rdoDateCreatedFrom;
    private RichTable holdingCompaniesTbl;
    private RichInputText txtPrefixManager;
    private RichCommandButton prefixPop;
    private RichInputText txtSmsPrefix;
    private RichSelectOneChoice txtBouncedCheque;
    private RichSelectOneChoice txtModeOfComm;
    private RichInputText txtBussinessPersons;
    private RichTable bussinessPersonTbl;
    private RichInputText txtAgentType;
    private RichInputText txtAgentGroup;
    private RichTable agentTypeTbl;
    private RichTable accountGroupsTbl;
    private RichSelectOneChoice txtWithTax;
    private RichSelectOneChoice txtVatApp;
    private RichSelectOneChoice txtCommLevyApp;
    private RichTable unAssignedProductsTbl;
    private RichTable assignedProductsTbl;
    private RichSelectBooleanCheckbox chkAssignedProducts;
    private RichSelectBooleanCheckbox chkUnAssignedProducts;
    private RichInputText txtTelPay;
    private RichInputText txtTelPayPrefix;
    private RichTable prefixTbl2;
    private RichSelectOneChoice txtFrequencyofPayment;
    private RichSelectOneChoice txtModeofPayment;

    private List<SelectItem> paymentModes = new ArrayList<SelectItem>();
    private RichSelectOneChoice txtPmntDtlsValidated;
    private RichSelectOneChoice txtrelatedpart;
    private RichSelectOneChoice txtAgnIDNODocUsed;
    private RichSelectOneChoice txtAgnMaritalStatus;
    private RichSelectOneChoice txtAgnQualification;
    private RichInputDate txtAgnDOB;
    private RichInputDate txtAgnBenStartDate;
    private RichInputText txtAgencyRegionName;
    private RichTable tblRegion;
    private RichInputText txtAgencyRegionCode;
    private RichPopup branchUnitPopUp;
    private RichInputText txtAgencyTaxAuthCode;
    private RichInputText txtAgencyTaxAuthName;
    private RichTable taxAuthorityTbl;

    private RichInputText txtBnkBrn;
    private RichInputText txtBnkAccOfficer;
    private RichInputText txtBnkAccNumber;
    private RichInputText txtBnkAccName;
    private RichInputText txtBnkTelNo;
    private RichInputText txtBnkCellNo;
    private RichInputText txtBnkCurrency;
    private RichSelectOneChoice txtBnkDefault;
    private RichCommandButton saveBankDetailsBtn;
    private RichPopup bankdetailsPopUp;
    private RichTable tblBnkDetails;
    private RichInputText bnkCurrencyDesc;
    private RichTable tblCurrencyPop;
    private RichTable tblBankBranchPop;
    private RichInputText txtBnkBrnCode;
    private RichTable tblAccountOfficer;
    private RichInputText txtBnkIBAN;
    private RichSelectOneChoice txtBnkStatus;
    private RichTable agentRecDocsLov;
    private RichTable tblReqDocsList;
    private RichInputText txtDocCodePop;
    private RichInputText txtReqDocCodePop;
    private RichInputText txtReqDocNamePop;
    private RichInputText txtDocAgnCodePop;
    private RichSelectOneChoice txtDocSubmittedPop;
    private RichInputDate txtDocDateSubmittedPop;
    private RichInputText txtDocRefNumPop;
    private RichInputText txtDocRemarkPop;
    private RichInputText txtDocUserReceivedPop;
    private RichCommandButton btnSaveRequiredDoc;
    private RichShowDetailItem agentRecDocstab;
    private RichInputFile upFile;
    private UploadedFile uploadedFile;
    private String filename;
    private long filesize;
    private static String fileContent;
    private String filetype;
    private RichInputText txtAgencyIRARegNo;
    private RichInputText txtAssignRemarks;
    private RichTable systemUsers;
    private RichDialog dlgIprsComp;
    private RichInputText txtTqIdNumber;
    private RichInputText txtTqPinNo;
    private RichInputText txtTqPassport;
    private RichInputText txtTqDob;
    private RichInputText txtTqFullNames;
    private RichInputText txtTqGender;
    private RichInputText txtIprsIdNumber;
    private RichInputText txtIprsPinNo;
    private RichInputText txtIprsPassport;
    private RichInputText txtIprsDob;
    private RichInputText txtIprsFullNames;
    private RichInputText txtIprsGender;
    private RichOutputLabel txtActivityName;
    private HtmlPanelGrid panelMainAccButtons;


    /**
     * Default Class Constructor
     */
    public MainAccountsBacking() {
    }


    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpSession getSession() {
        return session;
    }


    /**
     * Populate the selectOneChoice with the Account Type values
     *
     * @return
     */
    public List<SelectItem> getAccountsTypeElements() {

        if (accountsTypeElements.size() >= 0) {
            accountsTypeElements.clear();
        }

        BindingContainer bindings = getBindings();
        DCIteratorBinding iterator =
            (DCIteratorBinding)bindings.get("fetchAllAccountTypesIterator");
        int length =
            iterator.getRangeSize() > 0 ? iterator.getRangeSize() : new Long(iterator.getEstimatedRowCount()).intValue();
        accountsTypeElements = new ArrayList<SelectItem>();
        SelectItem item = null;

        for (int i = 0; i < length; i++) {
            Row row = iterator.getRowAtRangeIndex(i);

            if (row != null) {
                item = new SelectItem();
                item.setValue(row.getAttribute("code").toString());
                item.setLabel(row.getAttribute("accountType").toString());
            }
            accountsTypeElements.add(item);
        }

        return accountsTypeElements;
    }

    public void setAccountsTypeSelectedValue(String accountsTypeSelectedValue) {
        this.accountsTypeSelectedValue = accountsTypeSelectedValue;
    }

    public String getAccountsTypeSelectedValue() {
        return accountsTypeSelectedValue;
    }

    public void setBindings(BindingContainer bindings) {
        this.bindings = bindings;
    }

    /**
     * Get the page bindings
     *
     * @return
     */
    public BindingContainer getBindings() {
        if (this.bindings == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            this.bindings =
                    (BindingContainer)fc.getApplication().evaluateExpressionGet(fc,
                                                                                "#{bindings}",
                                                                                BindingContainer.class);
        }
        return this.bindings;
    }

    public void setTreeAgencies(RichTree treeAgencies) {
        this.treeAgencies = treeAgencies;
    }

    public RichTree getTreeAgencies() {
        return treeAgencies;
    }

    public void setPanelAgencyDetails(RichPanelBox panelAgencyDetails) {
        this.panelAgencyDetails = panelAgencyDetails;
    }

    public RichPanelBox getPanelAgencyDetails() {
        return panelAgencyDetails;
    }

    public void setTxtAgencyCode(RichInputText txtAgencyCode) {
        this.txtAgencyCode = txtAgencyCode;
    }

    public RichInputText getTxtAgencyCode() {
        return txtAgencyCode;
    }

    public void setTxtAccountTypeCode(RichInputText txtAccountTypeCode) {
        this.txtAccountTypeCode = txtAccountTypeCode;
    }

    public RichInputText getTxtAccountTypeCode() {
        return txtAccountTypeCode;
    }

    public void setTxtShortDesc(RichInputText txtShortDesc) {
        this.txtShortDesc = txtShortDesc;
    }

    public RichInputText getTxtShortDesc() {
        return txtShortDesc;
    }

    public void setTxtAgencyName(RichInputText txtAgencyName) {
        this.txtAgencyName = txtAgencyName;
    }

    public RichInputText getTxtAgencyName() {
        return txtAgencyName;
    }

    public void setTxtAgencyPhysicalAddress(RichInputText txtAgencyPhysicalAddress) {
        this.txtAgencyPhysicalAddress = txtAgencyPhysicalAddress;
    }

    public RichInputText getTxtAgencyPhysicalAddress() {
        return txtAgencyPhysicalAddress;
    }

    public void setTxtAgencyPostalAddress(RichInputText txtAgencyPostalAddress) {
        this.txtAgencyPostalAddress = txtAgencyPostalAddress;
    }

    public RichInputText getTxtAgencyPostalAddress() {
        return txtAgencyPostalAddress;
    }

    public void setTxtAgencyCountryCode(RichInputText txtAgencyCountryCode) {
        this.txtAgencyCountryCode = txtAgencyCountryCode;
    }

    public RichInputText getTxtAgencyCountryCode() {
        return txtAgencyCountryCode;
    }

    public void setTxtAgencyCountryName(RichInputText txtAgencyCountryName) {
        this.txtAgencyCountryName = txtAgencyCountryName;
    }

    public RichInputText getTxtAgencyCountryName() {
        return txtAgencyCountryName;
    }

    public void setTxtAgencyTownCode(RichInputText txtAgencyTownCode) {
        this.txtAgencyTownCode = txtAgencyTownCode;
    }

    public RichInputText getTxtAgencyTownCode() {
        return txtAgencyTownCode;
    }

    public void setTxtAgencyTownName(RichInputText txtAgencyTownName) {
        this.txtAgencyTownName = txtAgencyTownName;
    }

    public RichInputText getTxtAgencyTownName() {
        return txtAgencyTownName;
    }

    public void setTxtAgencyEmail(RichInputText txtAgencyEmail) {
        this.txtAgencyEmail = txtAgencyEmail;
    }

    public RichInputText getTxtAgencyEmail() {
        return txtAgencyEmail;
    }

    public void setTxtAgencyWebAddress(RichInputText txtAgencyWebAddress) {
        this.txtAgencyWebAddress = txtAgencyWebAddress;
    }

    public RichInputText getTxtAgencyWebAddress() {
        return txtAgencyWebAddress;
    }

    public void setTxtAgencyPostalCode(RichInputText txtAgencyPostalCode) {
        this.txtAgencyPostalCode = txtAgencyPostalCode;
    }

    public RichInputText getTxtAgencyPostalCode() {
        return txtAgencyPostalCode;
    }

    public void setTxtContactPerson(RichInputText txtContactPerson) {
        this.txtContactPerson = txtContactPerson;
    }

    public RichInputText getTxtContactPerson() {
        return txtContactPerson;
    }

    public void setTxtContactTitle(RichInputText txtContactTitle) {
        this.txtContactTitle = txtContactTitle;
    }

    public RichInputText getTxtContactTitle() {
        return txtContactTitle;
    }

    public void setTxtAgencyPhone1(RichInputText txtAgencyPhone1) {
        this.txtAgencyPhone1 = txtAgencyPhone1;
    }

    public RichInputText getTxtAgencyPhone1() {
        return txtAgencyPhone1;
    }

    public void setTxtAgencyPhone2(RichInputText txtAgencyPhone2) {
        this.txtAgencyPhone2 = txtAgencyPhone2;
    }

    public RichInputText getTxtAgencyPhone2() {
        return txtAgencyPhone2;
    }

    public void setTxtAgencyFax(RichInputText txtAgencyFax) {
        this.txtAgencyFax = txtAgencyFax;
    }

    public RichInputText getTxtAgencyFax() {
        return txtAgencyFax;
    }

    public void setTxtAgencyAccountNumber(RichInputText txtAgencyAccountNumber) {
        this.txtAgencyAccountNumber = txtAgencyAccountNumber;
    }

    public RichInputText getTxtAgencyAccountNumber() {
        return txtAgencyAccountNumber;
    }

    public void setTxtAgencyPIN(RichInputText txtAgencyPIN) {
        this.txtAgencyPIN = txtAgencyPIN;
    }

    public RichInputText getTxtAgencyPIN() {
        return txtAgencyPIN;
    }

    public void setTxtAgencyCommission(RichInputNumberSpinbox txtAgencyCommission) {
        this.txtAgencyCommission = txtAgencyCommission;
    }

    public RichInputNumberSpinbox getTxtAgencyCommission() {
        return txtAgencyCommission;
    }

    public void setTxtCreditAllowed(RichSelectOneChoice txtCreditAllowed) {
        this.txtCreditAllowed = txtCreditAllowed;
    }

    public RichSelectOneChoice getTxtCreditAllowed() {
        return txtCreditAllowed;
    }

    public void setTxtAgencyWthTx(RichInputNumberSpinbox txtAgencyWthTx) {
        this.txtAgencyWthTx = txtAgencyWthTx;
    }

    public RichInputNumberSpinbox getTxtAgencyWthTx() {
        return txtAgencyWthTx;
    }

    public void setTxtAgencyPrintdebitNote(RichSelectOneChoice txtAgencyPrintdebitNote) {
        this.txtAgencyPrintdebitNote = txtAgencyPrintdebitNote;
    }

    public RichSelectOneChoice getTxtAgencyPrintdebitNote() {
        return txtAgencyPrintdebitNote;
    }

    public void setTxtAgencyStatus(RichSelectOneChoice txtAgencyStatus) {
        this.txtAgencyStatus = txtAgencyStatus;
    }

    public RichSelectOneChoice getTxtAgencyStatus() {
        return txtAgencyStatus;
    }

    public void setTxtDateCreated(RichInputDate txtDateCreated) {
        this.txtDateCreated = txtDateCreated;
    }

    public RichInputDate getTxtDateCreated() {
        return txtDateCreated;
    }

    public void setTxtCreatedBy(RichInputText txtCreatedBy) {
        this.txtCreatedBy = txtCreatedBy;
    }

    public RichInputText getTxtCreatedBy() {
        return txtCreatedBy;
    }

    public void setTxtRegistartionCode(RichInputText txtRegistartionCode) {
        this.txtRegistartionCode = txtRegistartionCode;
    }

    public RichInputText getTxtRegistartionCode() {
        return txtRegistartionCode;
    }

    public void setTxtCommReserveRate(RichInputText txtCommReserveRate) {
        this.txtCommReserveRate = txtCommReserveRate;
    }

    public RichInputText getTxtCommReserveRate() {
        return txtCommReserveRate;
    }

    public void setTxtAnnualBudget(RichInputText txtAnnualBudget) {
        this.txtAnnualBudget = txtAnnualBudget;
    }

    public RichInputText getTxtAnnualBudget() {
        return txtAnnualBudget;
    }

    public void setTxtStaEffectiveDate(RichInputDate txtStaEffectiveDate) {
        this.txtStaEffectiveDate = txtStaEffectiveDate;
    }

    public RichInputDate getTxtStaEffectiveDate() {
        return txtStaEffectiveDate;
    }

    public void setTxtCreditPeriod(RichInputText txtCreditPeriod) {
        this.txtCreditPeriod = txtCreditPeriod;
    }

    public RichInputText getTxtCreditPeriod() {
        return txtCreditPeriod;
    }

    public void setTxtCommStatEffectiveDate(RichInputDate txtCommStatEffectiveDate) {
        this.txtCommStatEffectiveDate = txtCommStatEffectiveDate;
    }

    public RichInputDate getTxtCommStatEffectiveDate() {
        return txtCommStatEffectiveDate;
    }

    public void setTxtCommStatusDate(RichInputDate txtCommStatusDate) {
        this.txtCommStatusDate = txtCommStatusDate;
    }

    public RichInputDate getTxtCommStatusDate() {
        return txtCommStatusDate;
    }

    public void setTxtCommAllowed(RichSelectOneChoice txtCommAllowed) {
        this.txtCommAllowed = txtCommAllowed;
    }

    public RichSelectOneChoice getTxtCommAllowed() {
        return txtCommAllowed;
    }

    public void setTxtAgencyChecked(RichSelectOneChoice txtAgencyChecked) {
        this.txtAgencyChecked = txtAgencyChecked;
    }

    public RichSelectOneChoice getTxtAgencyChecked() {
        return txtAgencyChecked;
    }

    public void setTxtAgencyCheckedBy(RichInputText txtAgencyCheckedBy) {
        this.txtAgencyCheckedBy = txtAgencyCheckedBy;
    }

    public RichInputText getTxtAgencyCheckedBy() {
        return txtAgencyCheckedBy;
    }

    public void setTxtAgencyCheckDate(RichInputDate txtAgencyCheckDate) {
        this.txtAgencyCheckDate = txtAgencyCheckDate;
    }

    public RichInputDate getTxtAgencyCheckDate() {
        return txtAgencyCheckDate;
    }

    public void setTxtCompCommArrears(RichSelectOneChoice txtCompCommArrears) {
        this.txtCompCommArrears = txtCompCommArrears;
    }

    public RichSelectOneChoice getTxtCompCommArrears() {
        return txtCompCommArrears;
    }

    public void setTxtAgencyReinsurer(RichSelectOneChoice txtAgencyReinsurer) {
        this.txtAgencyReinsurer = txtAgencyReinsurer;
    }

    public RichSelectOneChoice getTxtAgencyReinsurer() {
        return txtAgencyReinsurer;
    }

    public void setTxtStatusDesc(RichInputText txtStatusDesc) {
        this.txtStatusDesc = txtStatusDesc;
    }

    public RichInputText getTxtStatusDesc() {
        return txtStatusDesc;
    }

    public void setTxtAgencyIDNum(RichInputText txtAgencyIDNum) {
        this.txtAgencyIDNum = txtAgencyIDNum;
    }

    public RichInputText getTxtAgencyIDNum() {
        return txtAgencyIDNum;
    }

    public void setTxtAgencyContractCode(RichInputText txtAgencyContractCode) {
        this.txtAgencyContractCode = txtAgencyContractCode;
    }

    public RichInputText getTxtAgencyContractCode() {
        return txtAgencyContractCode;
    }

    public void setTxtAgencyAgentCode(RichInputText txtAgencyAgentCode) {
        this.txtAgencyAgentCode = txtAgencyAgentCode;
    }

    public RichInputText getTxtAgencyAgentCode() {
        return txtAgencyAgentCode;
    }

    public void setTxtAgencySms(RichInputText txtAgencySms) {
        this.txtAgencySms = txtAgencySms;
    }

    public RichInputText getTxtAgencySms() {
        return txtAgencySms;
    }

    public void setTxtAgencyHoldingCompanyCode(RichInputText txtAgencyHoldingCompanyCode) {
        this.txtAgencyHoldingCompanyCode = txtAgencyHoldingCompanyCode;
    }

    public RichInputText getTxtAgencyHoldingCompanyCode() {
        return txtAgencyHoldingCompanyCode;
    }

    public void setTxtAgencySectorCode(RichInputText txtAgencySectorCode) {
        this.txtAgencySectorCode = txtAgencySectorCode;
    }

    public RichInputText getTxtAgencySectorCode() {
        return txtAgencySectorCode;
    }

    public void setTxtAgencyClassCode(RichInputText txtAgencyClassCode) {
        this.txtAgencyClassCode = txtAgencyClassCode;
    }

    public RichInputText getTxtAgencyClassCode() {
        return txtAgencyClassCode;
    }

    public void setTxtAgencyExpiriyDate(RichInputDate txtAgencyExpiriyDate) {
        this.txtAgencyExpiriyDate = txtAgencyExpiriyDate;
    }

    public RichInputDate getTxtAgencyExpiriyDate() {
        return txtAgencyExpiriyDate;
    }

    public void setTxtAgencyLicenseNum(RichInputText txtAgencyLicenseNum) {
        this.txtAgencyLicenseNum = txtAgencyLicenseNum;
    }

    public RichInputText getTxtAgencyLicenseNum() {
        return txtAgencyLicenseNum;
    }

    public void setTxtAgencyRunoff(RichSelectOneChoice txtAgencyRunoff) {
        this.txtAgencyRunoff = txtAgencyRunoff;
    }

    public RichSelectOneChoice getTxtAgencyRunoff() {
        return txtAgencyRunoff;
    }

    public void setTxtAgencyLicensed(RichSelectOneChoice txtAgencyLicensed) {
        this.txtAgencyLicensed = txtAgencyLicensed;
    }

    public RichSelectOneChoice getTxtAgencyLicensed() {
        return txtAgencyLicensed;
    }

    public void setTxtLicenseGracePeriod(RichInputText txtLicenseGracePeriod) {
        this.txtLicenseGracePeriod = txtLicenseGracePeriod;
    }

    public RichInputText getTxtLicenseGracePeriod() {
        return txtLicenseGracePeriod;
    }

    public void setTxtOldAccountNum(RichInputText txtOldAccountNum) {
        this.txtOldAccountNum = txtOldAccountNum;
    }

    public RichInputText getTxtOldAccountNum() {
        return txtOldAccountNum;
    }

    public void setTxtAgencyStatusRemarks(RichInputText txtAgencyStatusRemarks) {
        this.txtAgencyStatusRemarks = txtAgencyStatusRemarks;
    }

    public RichInputText getTxtAgencyStatusRemarks() {
        return txtAgencyStatusRemarks;
    }

    public void setBtnCreateUpdateAgency(RichCommandButton btnCreateUpdateAgency) {
        this.btnCreateUpdateAgency = btnCreateUpdateAgency;
    }

    public RichCommandButton getBtnCreateUpdateAgency() {
        return btnCreateUpdateAgency;
    }

    public void setBtnCancelAgency(RichCommandButton btnCancelAgency) {
        this.btnCancelAgency = btnCancelAgency;
    }

    public RichCommandButton getBtnCancelAgency() {
        return btnCancelAgency;
    }

    public void setTxtAgencyBranchCode(RichInputText txtAgencyBranchCode) {
        this.txtAgencyBranchCode = txtAgencyBranchCode;
    }

    public RichInputText getTxtAgencyBranchCode() {
        return txtAgencyBranchCode;
    }

    public void setDivisionLov(RichTable divisionLov) {
        this.divisionLov = divisionLov;
    }

    public RichTable getDivisionLov() {
        return divisionLov;
    }

    public void setMainPanelTab(RichPanelTabbed mainPanelTab) {
        this.mainPanelTab = mainPanelTab;
    }

    public RichPanelTabbed getMainPanelTab() {
        return mainPanelTab;
    }

    public void setPbMainPanelDetail(RichPanelBox pbMainPanelDetail) {
        this.pbMainPanelDetail = pbMainPanelDetail;
    }

    public RichPanelBox getPbMainPanelDetail() {
        return pbMainPanelDetail;
    }

    public void setTblAccountTypesPop(RichTable tblAccountTypesPop) {
        this.tblAccountTypesPop = tblAccountTypesPop;
    }

    public RichTable getTblAccountTypesPop() {
        return tblAccountTypesPop;
    }

    public void setTxtMainAccountType(RichInputText txtMainAccountType) {
        this.txtMainAccountType = txtMainAccountType;
    }

    public RichInputText getTxtMainAccountType() {
        return txtMainAccountType;
    }

    public void setTxtMainAccountTypeCode(RichInputText txtMainAccountTypeCode) {
        this.txtMainAccountTypeCode = txtMainAccountTypeCode;
    }

    public RichInputText getTxtMainAccountTypeCode() {
        return txtMainAccountTypeCode;
    }

    /**
     * This method is called whenever an Account Type is selected.
     * It populates the <code>treeAgencies</code> with all the agencies
     * that belong to that account type.
     *
     * @param valueChangeEvent object passed in by ADF Faces when configuring
     * this method to become the selection listener.
     */
    public void accountTypeChangeListener(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() !=
            session.getAttribute("accountTypeVal")) {
            String accountTypeCode = valueChangeEvent.getNewValue().toString();
            session.setAttribute("accountTypeCode", accountTypeCode);


            ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyInfo);

            refreshAgencyDetailSection();
            GlobalCC.refreshUI(panelAgencyDetails);
        }
        session.setAttribute("accountTypeVal", accountTypeSelector.getValue());
    }

    public void accountTypeChangeListener2(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() !=
            session.getAttribute("accountTypeVal")) {


            // The ValueChangeEvent has the old and new values, but these are not the actual Account types code.
            //  Instead they are an index into the list of possible values.
            Integer accTypeIndex = (Integer)valueChangeEvent.getNewValue();
            //  To get the actual Account Type Code, I need the binding to the iterator that got the list.
            DCIteratorBinding accTypeListIter =
                ADFUtils.findIterator("fetchAllAccountTypesIterator");
            //  Then I find the row of the list that is pointed to by the index.
            Row accTypeRow =
                accTypeListIter.getRowAtRangeIndex(accTypeIndex.intValue());

            if (accTypeRow == null) {
                // This must be a marketer : 90900909

                String accountTypeCode =
                    accountTypeSelector.getValue().toString();
                session.setAttribute("accountTypeCode", accountTypeCode);

                // the demo tree
                ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
                GlobalCC.refreshUI(tblAgencyInfo);

                refreshAgencyDetailSection();
                GlobalCC.refreshUI(panelAgencyDetails);
            } else {
                // And get the attribute for the account type code from that row.
                String currentAccType =
                    (String)accTypeRow.getAttribute("code");


                String accountTypeCode = currentAccType;
                session.setAttribute("accountTypeCode", accountTypeCode);

                //ADFUtils.findIterator("fetchAllAccountAgenciesIterator").executeQuery();
                //GlobalCC.refreshUI(treeAgencies);

                // the demo tree
                ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
                GlobalCC.refreshUI(tblAgencyInfo);


                refreshAgencyDetailSection();
                GlobalCC.refreshUI(panelAgencyDetails);
            }
        }
    }

    /**
     * This method is called whenever an Agency/Account is selected in the
     * <code>treeAgencies</code> UI component. It populates the
     * detail section with information regarding that Agency/Account.
     *
     * @param selectionEvent object passed in by ADF Faces when configuring
     * this method to become the selection listener of the
     * <code>treeAgency</code>
     */
    /*public void tblAgenciesInfoSelectionListener(SelectionEvent selectionEvent) {

        // Update the detail section with the Agency details
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {

                for (Object treeRowKey : keys) {
                    treeAgencies.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeAgencies.getRowData();

                    String agencyCode =
                        (String)nd.getRow().getAttribute("code");
                    session.setAttribute("agencyCode", agencyCode);

                    // to avoid the session problem
                    hiddenAccountCode.setValue(agencyCode);
                    GlobalCC.refreshUI(panelDetailSystems);

                    txtAgencyCode.setValue(nd.getRow().getAttribute("code"));
                    txtAccountTypeCode.setValue(nd.getRow().getAttribute("accountCode"));
                    txtShortDesc.setValue(nd.getRow().getAttribute("shortDesc"));
                    txtAgencyName.setValue(nd.getRow().getAttribute("name"));
                    txtAgencyPhysicalAddress.setValue(nd.getRow().getAttribute("physicalAddress"));
                    txtAgencyPostalAddress.setValue(nd.getRow().getAttribute("postalAddress"));
                    txtAgencyCountryCode.setValue(nd.getRow().getAttribute("countryCode"));
                    txtAgencyCountryName.setValue(nd.getRow().getAttribute("country"));
                    txtAgencyTownCode.setValue(nd.getRow().getAttribute("townCode"));
                    txtAgencyTownName.setValue(nd.getRow().getAttribute("town"));
                    txtAgencyEmail.setValue(nd.getRow().getAttribute("emailAddress"));
                    txtAgencyWebAddress.setValue(nd.getRow().getAttribute("webAddress"));
                    txtAgencyPostalCode.setValue(nd.getRow().getAttribute("zip"));
                    txtContactPerson.setValue(nd.getRow().getAttribute("contactPerson"));
                    txtContactTitle.setValue(nd.getRow().getAttribute("contactTitle"));
                    txtAgencyPhone1.setValue(nd.getRow().getAttribute("telephone1"));
                    txtAgencyPhone2.setValue(nd.getRow().getAttribute("telephone2"));
                    txtAgencyFax.setValue(nd.getRow().getAttribute("fax"));
                    txtAgencyAccountNumber.setValue(nd.getRow().getAttribute("accountNum"));
                    txtAgencyPIN.setValue(nd.getRow().getAttribute("PIN"));
                    txtAgencyCommission.setValue(nd.getRow().getAttribute("commission"));
                    txtCreditAllowed.setValue(nd.getRow().getAttribute("creditAllowed"));
                    txtAgencyWthTx.setValue(nd.getRow().getAttribute("witholdTx"));
                    txtAgencyPrintdebitNote.setValue(nd.getRow().getAttribute("printDbNote"));
                    txtAgencyStatus.setValue(nd.getRow().getAttribute("status"));
                    txtDateCreated.setValue(nd.getRow().getAttribute("dateCreated"));
                    txtCreatedBy.setValue(nd.getRow().getAttribute("createdBy"));
                    txtRegistartionCode.setValue(nd.getRow().getAttribute("registrationCode"));
                    txtCommReserveRate.setValue(nd.getRow().getAttribute("commReserverate"));
                    txtAnnualBudget.setValue(nd.getRow().getAttribute("annualBudget"));
                    txtStaEffectiveDate.setValue(nd.getRow().getAttribute("statusEffectiveDate"));
                    txtCreditPeriod.setValue(nd.getRow().getAttribute("creditPeriod"));
                    txtCommStatEffectiveDate.setValue(nd.getRow().getAttribute("commStatEffectiveDate"));
                    txtCommStatusDate.setValue(nd.getRow().getAttribute("commStatusDate"));
                    txtCommAllowed.setValue(nd.getRow().getAttribute("commAllowed"));
                    txtAgencyChecked.setValue(nd.getRow().getAttribute("checked"));
                    txtAgencyCheckedBy.setValue(nd.getRow().getAttribute("checkedBy"));
                    txtAgencyCheckDate.setValue(nd.getRow().getAttribute("checkDate"));
                    txtCompCommArrears.setValue(nd.getRow().getAttribute("compCommArrears"));
                    txtAgencyReinsurer.setValue(nd.getRow().getAttribute("reinsurer"));
                    txtAgencyBranchCode.setValue(nd.getRow().getAttribute("branchCode"));
                    txtAgencyBranchName.setValue(nd.getRow().getAttribute("branchName"));
                    txtStatusDesc.setValue(nd.getRow().getAttribute("statusDesc"));
                    txtAgencyIDNum.setValue(nd.getRow().getAttribute("IDNum"));
                    txtAgencyContractCode.setValue(nd.getRow().getAttribute("conCode"));
                    txtAgencyAgentCode.setValue(nd.getRow().getAttribute("agentCode"));
                    txtAgencySms.setValue(nd.getRow().getAttribute("sms"));
                    txtAgencyHoldingCompanyCode.setValue(nd.getRow().getAttribute("holdCompanyCode"));
                    txtAgencyHoldingCompanyName.setValue(nd.getRow().getAttribute("holdCompanyName"));
                    txtAgencySectorCode.setValue(nd.getRow().getAttribute("sectorCode"));
                    txtAgencySectorName.setValue(nd.getRow().getAttribute("sectorName"));
                    txtAgencyClassCode.setValue(nd.getRow().getAttribute("classCode"));
                    txtAgencyClassName.setValue(nd.getRow().getAttribute("className"));
                    txtAgencyExpiriyDate.setValue(nd.getRow().getAttribute("expiriyDate"));
                    txtAgencyLicenseNum.setValue(nd.getRow().getAttribute("licenseNum"));
                    txtAgencyRunoff.setValue(nd.getRow().getAttribute("runOff"));
                    txtAgencyLicensed.setValue(nd.getRow().getAttribute("licensed"));
                    txtLicenseGracePeriod.setValue(nd.getRow().getAttribute("licenseGracePeriod"));
                    txtOldAccountNum.setValue(nd.getRow().getAttribute("oldAccountNum"));
                    txtAgencyStatusRemarks.setValue(nd.getRow().getAttribute("statusRemarks"));

                    // Refresh the agency details panel
                    GlobalCC.refreshUI(panelAgencyDetails);

                    // Agency Accounts
                    ADFUtils.findIterator("fetchAgencyAccountsIterator").executeQuery();
                    GlobalCC.refreshUI(tblAgencyAccounts);

                    // Get the Agents registration details
                    ADFUtils.findIterator("findAgentsRegistrationIterator").executeQuery();
                    GlobalCC.refreshUI(registrationTable);

                    // Get the Agency Directors
                    ADFUtils.findIterator("findAgentsDirectorsIterator").executeQuery();
                    GlobalCC.refreshUI(agentDirectorsTable);

                    // Get the Agents referees
                    ADFUtils.findIterator("findAgentsRefereesIterator").executeQuery();
                    GlobalCC.refreshUI(agencyRefereeTable);

                    // Get the user systems
                    ADFUtils.findIterator("fetchUnassignedAccountSystemsIterator").executeQuery();
                    ADFUtils.findIterator("fetchAssignedAccountSystemsIterator").executeQuery();
                    GlobalCC.refreshUI(treeUnassignedSystems);
                    GlobalCC.refreshUI(treeAssignedSystems);

                    // Get the User web accounts
                    ADFUtils.findIterator("findwebUserAccountsIterator").executeQuery();
                    GlobalCC.refreshUI(webUsersTable);

                }
            }
        }

        // Just in case the buttons are not disabled
        btnCreateUpdateCurrentAgency.setText("Update");
        btnCreateUpdateAgency.setText("Update");
        btnCreateUpdateAgency.setDisabled(true);
        //btnCancelAgency.setDisabled(true);
    }*/

    public void tblAgenciesInfoSelectionListener(SelectionEvent selectionEvent) {
        try {

            Object key2 = tblAgencyInfo.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            if (nodeBinding != null) {
                BigDecimal agentCode =
                    GlobalCC.checkBDNullValues(nodeBinding.getAttribute("code"));
                session.setAttribute("AgentCode", agentCode);
                /*please don't delete these line! it is used to pull payee records*/
                session.setAttribute("ClientCode", agentCode);
                //change ticket
                session.removeAttribute("taskID");
                session.removeAttribute("activityName");
                session.setAttribute("ticketProcessShtDesc",
                                     GlobalCC.agentProcess);
                session.setAttribute("ticketEntityCode", agentCode);

                JBPMEngine bpm = JBPMEngine.getInstance();
                bpm.getCurrentEntityTask();

                actionLoadByAgentCode();

                GlobalCC.refreshUI(txtActivityName);
            }
            //btnCancelAgency.setDisabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the screen in the Agency/Account detail section
     */
    public void refreshAgencyDetailSection() {
        Util ut = new Util();
        session.removeAttribute("agentIprsValidated");
        System.out.println("Refreshing Agencies!");
        int accountCode;
        txtBussinessPersons.setValue(null);
        txtAgencyCode.setValue(null);
        txtAccountTypeCode.setValue(null);
        txtShortDesc.setValue(null);
        txtAgencyName.setValue(null);
        txtAgent.setValue(null);
        // txtAgencyPhysicalAddress.setValue(null);
        txtAgencyPostalAddress.setValue(null);
        txtAgencyCountryCode.setValue(session.getAttribute("COUNTRY_CODE"));
        txtAgencyCountryName.setValue(session.getAttribute("COUNTRY_NAME"));
        if (session.getAttribute("COUNTRY_NAME") != null) {
            if (session.getAttribute("COUNTRY_NAME").equals("KENYA")) {
                pnmsgAdminRegionName.setLabel("County Name");
            }
        }

        String adminRegionType =
            session.getAttribute("ADMIN_REG_TYPE") != null ?
            (String)session.getAttribute("ADMIN_REG_TYPE") : null;
        if (adminRegionType != null) {

            pnmsgAdminRegionName.setLabel(GlobalCC.formatAdminUnitSingular(adminRegionType) +
                                          ":");
            dlgAdminRegionList.setTitle((GlobalCC.formatAdminUnitPlural(adminRegionType) +
                                         " List"));
            txtAStateCode.setValue(null);
            txtAStateName.setValue(null);
            txtAgencyTownCode.setValue(null);
            txtAgencyTownName.setValue(null);
            pnmsgAdminRegionName.setVisible(true);
            pnmsgTownName.setVisible(true);

        }
        txtAgencyTownCode.setValue(null);
        txtAStateCode.setValue(null);
        txtAStateName.setValue(null);
        txtAgencyTownName.setValue(null);
        txtAgencyEmail.setValue(null);
        txtAgencyWebAddress.setValue(null);
        txtAgencyPostalCode.setValue(null);
        txtContactPerson.setValue(null);
        txtContactTitle.setValue(null);
        txtAgencyPhone1.setValue(null);
        txtAgencyPhone2.setValue(null);
        txtAgencyFax.setValue(null);
        txtAgencyAccountNumber.setValue(null);
        txtAgencyPIN.setValue(null);
        txtAgencyCommission.setValue(null);
        txtCreditAllowed.setValue(null);
        txtAgencyWthTx.setValue(null);
        txtAgencyPrintdebitNote.setValue(null);
        session.setAttribute("AgentStatus", "DRAFT");
        txtAgencyStatus.setValue("DRAFT");
        txtDateCreated.setValue(null);
        txtCreatedBy.setValue(null);
        txtRegistartionCode.setValue(null);
        txtCommReserveRate.setValue(null);
        txtAnnualBudget.setValue(null);
        txtStaEffectiveDate.setValue(null);
        txtCreditPeriod.setValue(null);
        txtCommStatEffectiveDate.setValue(null);
        txtCommStatusDate.setValue(null);
        txtCommAllowed.setValue(null);
        txtAgencyChecked.setValue(null);
        txtAgencyCheckedBy.setValue(null);
        txtAgencyCheckDate.setValue(null);
        txtCompCommArrears.setValue(null);
        txtAgencyReinsurer.setValue(null);
        if (ut.defaultUserBranch().equals("N")) {
            txtAgencyBranchCode.setValue(null);
            txtAgencyBranchName.setValue(null);
        } else {
            txtAgencyBranchCode.setValue(session.getAttribute("branchCode"));
            txtAgencyBranchName.setValue(session.getAttribute("branchName"));
        }
        txtStatusDesc.setValue(null);
        txtAgencyIDNum.setValue(null);
        txtAgencyContractCode.setValue(null);
        txtAgencyAgentCode.setValue(null);
        txtSmsPrefix.setValue(null);
        txtAgencySms.setValue(null);
        txtTelPayPrefix.setValue(null);
        txtTelPay.setValue(null);
        txtAgencyHoldingCompanyCode.setValue(null);
        txtAgencyHoldingCompanyName.setValue(null);
        txtAgencySectorCode.setValue(null);
        txtAgencySectorName.setValue(null);
        txtAgencyClassCode.setValue(null);
        txtAgencyClassName.setValue(null);
        txtAgencyExpiriyDate.setValue(null);
        txtAgencyLicenseNum.setValue(null);
        //txtAgencyRunoff.setValue(null);
        txtAgencyLicensed.setValue(null);
        txtLicenseGracePeriod.setValue(null);
        txtOldAccountNum.setValue(null);
        txtAgencyStatusRemarks.setValue(null);
        txtBankBranch.setValue(null);
        txtBankName.setValue(null);
        txtBankCode.setValue(null);
        txtBankBranchCode.setValue(null);
        txtAccountNo.setValue(null);
        txtPrefix.setValue(null);
        txtCreditRting.setValue(null);
        txtBranchUnits.setValue(null);
        txtRatingOrg.setValue(null);
        txtRating.setValue(null);
        txtIssueCert.setValue(null);
        txtAgentType.setValue(null);
        txtAgentGroup.setValue(null);
        txtBussinessPersons.setValue(null);
        // The buttons as well
        btnCreateUpdateAgency.setDisabled(true);
        accountCode =
                Integer.parseInt(session.getAttribute("accountTypeCode").toString());
        txtCreditLimit.setValue(null);
        if (accountCode == 6) {
            txtLocalInt.setVisible(true);
            txtLocalInt.setValue(null);
        } else {
            txtLocalInt.setVisible(false);
        }
        txtVatApp.setValue("Y");
        txtWithTax.setValue("Y");
        //btnCancelAgency.setDisabled(true);
    }

    /**
     * Preparing to create a new Agency by learing the text fields
     *
     * @return null
     */
    public String actionNewAgency() {
        session.setAttribute("PayeeType", "A");
        session.removeAttribute("bactAccCode");

        // txtAgencyPostalAddress.setValue("P.O. BOX");

        txtAgent.setValue("");
        txtMarketer.setValue(null);
        // Check if an Account type has been selected
        if (session.getAttribute("accountTypeCode") == null ||
            session.getAttribute("accountTypeCode") == ("")) {
            GlobalCC.INFORMATIONREPORTING("You need to select an Account Type first!");

        } else {
            // Clear all the text fileds and enable the buttons
            session.removeAttribute("otherNames");
            session.removeAttribute("count");
            session.removeAttribute("branchCode");
            session.removeAttribute("AgentCode");
            session.removeAttribute("taskID");
            session.removeAttribute("activityName");

            refreshAgencyDetailSection();
            txtAgencyPostalAddress.setValue("P.O. BOX");

            txtContactPerson.setDisabled(false);
            txtAccountTypeCode.setValue(session.getAttribute("accountTypeCode"));

            btnCreateUpdateCurrentAgency.setText("Save");
            btnCreateUpdateAgency.setText("Save");
            btnCreateUpdateAgency.setDisabled(false);
            //btnCancelAgency.setDisabled(false);

            mainPanelTab.setVisible(true);
            pnmsgAdminRegionName.setVisible(true);


            ADFUtils.findIterator("fetchAllAccountAgenciesBasedOnNamesIterator").executeQuery();
            GlobalCC.refreshUI(tbListAgencies);
            GlobalCC.refreshUI(txtAgent);
            GlobalCC.refreshUI(panelMainAccButtons);
            GlobalCC.refreshUI(pbMainPanelDetail);
        }

        return null;
    }

    public String checkIfAgencyExist() {
        session.setAttribute("PayeeType", "A");
        BigDecimal amount;
        String pmtFreq;
        String pmtMode;
        pmtFreq = GlobalCC.checkNullValues(txtFrequencyofPayment.getValue());
        pmtMode = GlobalCC.checkNullValues(txtModeofPayment.getValue());
        session.setAttribute("PAYMENT_FREQ",
                             GlobalCC.checkNullValues(pmtFreq));
        //session.setAttribute("PM_MODE", pmtMode.toString());
        String agencyCode = GlobalCC.checkNullValues(txtAgencyCode.getValue());
        String agencyStatus =
            GlobalCC.checkNullValues(txtAgencyStatus.getValue());
        String accountTypeCode =
            GlobalCC.checkNullValues(session.getAttribute("accountTypeCode"));
        String shortDesc = GlobalCC.checkNullValues(txtShortDesc.getValue());
        String agencyName = GlobalCC.checkNullValues(txtAgencyName.getValue());
        agencyName = (agencyName != null) ? agencyName.toUpperCase() : null;
        String agencyPin = GlobalCC.checkNullValues(txtAgencyPIN.getValue());
        BigDecimal branchCode =
            GlobalCC.checkBDNullValues(txtAgencyBranchCode.getValue());
        String runoff = GlobalCC.checkNullValues(txtAgencyRunoff.getValue());
        String physicalAddress =
            GlobalCC.checkNullValues(txtAgencyPhysicalAddress.getValue());
        String emailAddress =
            GlobalCC.checkNullValues(txtAgencyEmail.getValue());
        String telephone1 =
            GlobalCC.checkNullValues(txtAgencyPhone1.getValue());
        String regionTypeName =
            GlobalCC.checkNullValues(txtAStateName.getValue());
        String townName =
            GlobalCC.checkNullValues(txtAgencyTownName.getValue());
        String creditLimit =
            GlobalCC.checkNullValues(txtCreditLimit.getValue());
        String commLevyApp =
            GlobalCC.checkNullValues(txtCommLevyApp.getValue());
        BigDecimal commLevyRate =
            GlobalCC.checkBDNullValues(txtCommLevyRate.getValue());
        String agencySBU =
            GlobalCC.checkNullValues(txtAgencySBUCode.getValue());
        String authorityName =
            GlobalCC.checkNullValues(txtAgencyTaxAuthName.getValue());
        String countryName =
            GlobalCC.checkNullValues(txtAgencyCountryName.getValue());
        String dob = GlobalCC.checkNullValues(txtAgnDOB.getValue());
        String benStartDate =
            GlobalCC.checkNullValues(txtAgnBenStartDate.getValue());
        String qualification =
            GlobalCC.checkNullValues(txtAgnQualification.getValue());
        String maritalStatus =
            GlobalCC.checkNullValues(txtAgnMaritalStatus.getValue());

        // Start checking rights
        if (txtCreditAllowed.getValue() != null) {
            if (txtCreditAllowed.getValue().equals("Y")) {
                if (txtCreditLimit.getValue() != null) {
                    amount =
                            GlobalCC.checkBDNullValues(txtCreditLimit.getValue());

                } else {
                    amount = null;
                }
            } else {
                amount = null;
            }

            Authorization auth = new Authorization();
            String process = "AMA";
            String processArea = "CRLMT";
            String processSubArea = "CRDET";
            String AccessGranted =
                auth.checkUserRights(process, processArea, processSubArea,
                                     amount, null);


            if (AccessGranted.equalsIgnoreCase("Y")) {


            } else if (AccessGranted.equalsIgnoreCase("N") &&
                       txtCreditAllowed.getValue().equals("Y") && 
                       // check if both txtCreditLimit and txtCreditAllowed are disabled and pass
                       !(txtCreditLimit.isDisabled() && txtCreditAllowed.isDisabled()) ) {
                GlobalCC.INFORMATIONREPORTING("You do not have the right to allow credit limits");
                return null;
            } else {

            }
        }
        Authorization auth = new Authorization();
        String process = "AMA";
        String processArea = "AMA";
        String processSubArea = "AMAE";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (!"Y".equalsIgnoreCase(AccessGranted)) {
            GlobalCC.INFORMATIONREPORTING("You do not have the right to update agent");
            return null;
        }
        // end checking rights

        if ("ACTIVE".equalsIgnoreCase(agencyStatus) &&
            "N".equalsIgnoreCase(runoff)) {
            Rendering render = new Rendering();

            boolean relation =
                render.isAGENT_RELATIONSHIP_OFFICER_REQUIRED(); //isAgencyRelationShipManager();
            boolean account = render.isBankBranchAccountMand();
            boolean multipleAcc = render.isMultiBankAccPerAccTypeApp();
            boolean accountEmail = render.isAccountEmailMand();
            boolean couMand = render.isCountryMandatory();
            boolean countyMand = render.isAgencyCountyMand();
            boolean townMand = render.isAgencyTowmMand();
            if (render.isAGENT_REGION_REQUIRED() &&
                txtAgencyRegionCode.getValue() == null) {
                GlobalCC.errorValueNotEntered("Please Select Region!");
                return null;
            }
            if (couMand == true && countryName == null) {
                GlobalCC.errorValueNotEntered("Please select country");
                return null;
            }
            if (countyMand == true && regionTypeName == null) {
                GlobalCC.errorValueNotEntered("Please select County/State");
                return null;
            }
            if (townMand == true && townName == null) {
                GlobalCC.errorValueNotEntered("Please select Town");
                return null;
            }
            if (accountEmail == true && emailAddress == null &&
                render.isAGENT_EMAIL_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Please select email address");
                return null;
            }
            if (account == true && multipleAcc == false &&
                txtBankName.getValue() == null) {
                GlobalCC.errorValueNotEntered("Please Select Bank Branch ");
                return null;
            }
            if (account == true && multipleAcc == false &&
                txtBankBranch.getValue() == null) {
                GlobalCC.errorValueNotEntered("Please Select Bank Branch ");
                return null;
            }
            if (account == true && multipleAcc == false &&
                txtAccountNo.getValue() == null) {
                GlobalCC.errorValueNotEntered("Please Enter Account Number");
                return null;
            }
            if (relation == true && txtMarketer.getValue() == null) {
                GlobalCC.errorValueNotEntered("Please select a relationship manager");
                return null;
            }

            if (render.isPHYSICAL_ADDRESS_REQUIRED() &&
                physicalAddress == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Physical address");
                return null;
            }
            if (render.isAGENT_PIN_REQUIRED() == true && agencyPin == null) {
                GlobalCC.errorValueNotEntered("Please enter Pin Number");
                return null;
            }
            //System.out.println("agencyCode: "+agencyCode+"agencyPin:  "+agencyPin);
            if (render.isRegCodeMand() == true &&
                txtRegistartionCode.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please enter the Registration code");
                return null;
            }
            if (render.isAGENT_POSTAL_ADDRESS_REQUIRED()) {
                if (txtAgencyPostalAddress.getValue() == null ||
                    txtAgencyPostalAddress.getValue() == "") {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Enter Postal address");
                    return null;

                }
            }

            if (render.isAGENT_EMAIL_REQUIRED()) {
                if (emailAddress == null || emailAddress == "") {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Enter Email address");
                    return null;

                }
            }
            if (render.isTelephoneRequired()) {
                if (telephone1 == null || telephone1 == "") {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Enter Telephone 1");
                    return null;
                }
            }


            if (render.isSMSRequired()) {
                if (txtAgencySms.getValue() == null) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Enter the SMS No.");
                    return null;
                }
            }
            if (render.isAGENT_DATE_OF_BIRTH_REQUIRED() && dob == null) {
                GlobalCC.errorValueNotEntered("Please enter Date Of Birth!");
                return null;
            }
            if (render.isAgencyCommStartDateMandatory() == true &&
                render.getLmsActive() != null && benStartDate == null) {
                GlobalCC.errorValueNotEntered("Please enter Commision Start Date!");
                return null;
            }
            //            if (render.isAgencyQualificationMandatory() == true &&
            //                qualification == null) {
            //                GlobalCC.errorValueNotEntered("Please select Qualification!");
            //                return null;
            //            }
            if (render.isAgencyMaritalStatusMandatory() == true &&
                maritalStatus == null) {
                GlobalCC.errorValueNotEntered("Please Select Marital Status!");
                return null;
            }
            // System.out.println("agentSBURequired"+render.isAGENT_SBU_REQUIRED());
            if (render.isAGENT_SBU_REQUIRED() && agencySBU == null) {
                GlobalCC.errorValueNotEntered("Please Select SBU!");
                return null;
            }
            if (render.isAGENT_TAX_AUTHORITY_REQUIRED() &&
                authorityName == null) {
                GlobalCC.errorValueNotEntered("Please Select Tax Authority!");
                return null;
            }
            if (accountTypeCode == null || accountTypeCode == "") {
                // GlobalCC.errorValueNotEntered("Error Value Missing: You need to select an account type");
                // return null;
                /*} else if (shortDesc == null || shortDesc == "") {
        GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Agency ID");
        return null;*/
            } else if (agencyName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Agency Name");
                return null;

            } else if (branchCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a branch");
                return null;

            } else if (runoff == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select Runoff value");
                return null;

            }

            if (btnCreateUpdateCurrentAgency.getText().equals("Update")) {
                actionCreateNewAgency();
            } else {
                session.setAttribute("otherNames", agencyName);
                System.out.println("Email address" + emailAddress);
                session.setAttribute("emailAddress", emailAddress);
                if (txtAgencyPIN.getValue() != null) {
                    session.setAttribute("pinNumber", txtAgencyPIN.getValue());
                } else {
                    session.setAttribute("pinNumber", null);
                }
                session.setAttribute("telNumber", telephone1);
                session.setAttribute("physicalAddress", physicalAddress);
                session.setAttribute("agencySearch", "agencySearch");
                ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
                ADFUtils.findIterator("fetchAllAccountAgenciesBasedOnNamesIterator").executeQuery();
                GlobalCC.refreshUI(tbListAgencies);

                session.setAttribute("agencySearch", null);
                int counter = 0;
                //count is set on the MainClientDAO
                if (session.getAttribute("countAgents") != null) {

                    String noOfRecords =
                        session.getAttribute("countAgents").toString();

                    counter = Integer.parseInt(noOfRecords);

                    if (counter > 0) {
                        GlobalCC.showPopup("pt1:confirmSaveAgency");
                        return null;
                    } else {

                        actionCreateNewAgency();
                    }
                } else {

                    actionCreateNewAgency();
                }
            }
        } else {
            actionCreateNewAgency();
        }
        return null;
    }

    public void confirmSaveAgencyDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionCreateNewAgency();

        }
    }

    public String actionShowStates() {
        if (txtAgencyCountryCode.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("You need to select a Country first to proceed.");
            return null;
        } else {
            session.setAttribute("countryCode",
                                 txtAgencyCountryCode.getValue());
            ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
            GlobalCC.refreshUI(tblAdminRegions);
            GlobalCC.showPopup("pt1:statesPop");
        }
        return null;
    }

    public String actionAcceptState() {
        Object key2 = tblAdminRegions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            BigDecimal stateCode =
                GlobalCC.checkBDNullValues(nodeBinding.getAttribute("stateCode"));
            if (stateCode == null) {
                GlobalCC.INFORMATIONREPORTING("Please Select Item First!");
                return null;
            }
            txtAStateCode.setValue(stateCode);
            txtAStateName.setValue(nodeBinding.getAttribute("stateName"));

            session.setAttribute("stateCode", stateCode);

            txtAgencyTownCode.setValue(null);
            txtAgencyTownName.setValue(null);

            ADFUtils.findIterator("fetchTownsByStateIterator").executeQuery();
            GlobalCC.refreshUI(tbTownListing);
            GlobalCC.refreshUI(txtAStateName);
        }
        GlobalCC.dismissPopUp("pt1", "statesPop");
        return null;
    }

    public String actionAcceptAgencyTown() {
        Object key2 = tbTownListing.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAgencyTownName.setValue(nodeBinding.getAttribute("name"));
            txtAgencyTownCode.setValue(nodeBinding.getAttribute("code"));
            txtAgencyPostalCode.setValue(nodeBinding.getAttribute("postalZipCode"));
            System.out.println("Town = " + nodeBinding.getAttribute("code"));
            GlobalCC.refreshUI(txtAgencyTownName);
            GlobalCC.refreshUI(txtAgencyTownCode);
            GlobalCC.refreshUI(txtAgencyPostalCode);
        }
        GlobalCC.dismissPopUp("pt1", "townListing");
        return null;
    }

    public String btnEditAgency() {
        // Add event code here...
        return null;
    }

    public String actionEditAgency() {

        btnCreateUpdateAgency.setText("Update");
        btnCreateUpdateAgency.setDisabled(false);
        //btnCancelAgency.setDisabled(false);

        return null;
    }

    /**
     * Prompts the user to confirm if they really need to proceed with the
     * delete operation.
     *
     * @return null
     */
    public String actionDeleteAgency() {
        Object key2 = tblAgencyInfo.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String agencyCode = nodeBinding.getAttribute("code").toString();
            // Open the popup dialog to confirm delete action
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmAgencyDeletePop" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Recocrd Selected");
        }
        return null;
    }

    /**
     * Update an Agency Record
     *
     * @return null
     */
    public String actionCreateUpdateAgency() {
        BigDecimal agentCode;
        String SubAgentName;
        BigDecimal clnCode;
        int pref;
        String localReins;
        BigDecimal accountManagerCode;
        System.out.println("This is the relationship manager" +
                           session.getAttribute("accountManagerCode"));
        if (session.getAttribute("accountManagerCode") != null) {
            accountManagerCode =
                    new BigDecimal(session.getAttribute("accountManagerCode").toString());
        } else {
            accountManagerCode = null;
        }
        if (session.getAttribute("CLCode") == null) {
            clnCode = null;
        } else {
            clnCode =
                    new BigDecimal(session.getAttribute("CLCode").toString());
        }
        if (txtLocalInt.getValue() != null) {
            localReins = txtLocalInt.getValue().toString();
        } else {
            localReins = null;
        }
        // Proceed to do an update
        // Checking only the required fields
        BigDecimal agencyCode =
            GlobalCC.checkBDNullValues(txtAgencyCode.getValue());
        String accountTypeCode =
            GlobalCC.checkNullValues(session.getAttribute("accountTypeCode"));
        String shortDesc = GlobalCC.checkNullValues(txtShortDesc.getValue());
        String agencyName = GlobalCC.checkNullValues(txtAgencyName.getValue());
        String branchCode =
            GlobalCC.checkNullValues(txtAgencyBranchCode.getValue());
        String runoff = GlobalCC.checkNullValues(txtAgencyRunoff.getValue());
        String physicalAddress =
            GlobalCC.checkNullValues(txtAgencyPhysicalAddress.getValue());
        String emailAddress =
            GlobalCC.checkNullValues(txtAgencyEmail.getValue());
        String telephone1 =
            GlobalCC.checkNullValues(txtAgencyPhone1.getValue());
        String agencyWithTax =
            GlobalCC.checkNullValues(txtAgencyWthTx.getValue());
        String agencyComm =
            GlobalCC.checkNullValues(txtAgencyCommission.getValue());
        String creditLimit =
            GlobalCC.checkNullValues(txtCreditLimit.getValue());
        String agencyStatus = (String)txtAgencyStatus.getValue();
        String benefitStartDate =
            GlobalCC.checkNullValues(txtAgnBenStartDate.getValue());
        String dob = GlobalCC.checkNullValues(txtAgnDOB.getValue());
        String qualification =
            GlobalCC.checkNullValues(txtAgnQualification.getValue());
        String maritalStatus =
            GlobalCC.checkNullValues(txtAgnMaritalStatus.getValue());
        String IDNODocUsed =
            GlobalCC.checkNullValues(txtAgnIDNODocUsed.getValue());
        BigDecimal agencySBU =
            GlobalCC.checkBDNullValues(txtAgencySBUCode.getValue());
        String commLevyApp =
            GlobalCC.checkNullValues(txtCommLevyApp.getValue());
        BigDecimal commLevyRate =
            GlobalCC.checkBDNullValues(txtCommLevyRate.getValue());
        BigDecimal regionCode =
            GlobalCC.checkBDNullValues(txtAgencyRegionCode.getValue());
        String regionName =
            GlobalCC.checkNullValues(txtAgencyRegionName.getValue());
        String authorityName =
            GlobalCC.checkNullValues(txtAgencyTaxAuthName.getValue());
        String statusRemarks =
            GlobalCC.checkNullValues(txtAgencyStatusRemarks.getValue());
        if (runoff == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select Runoff value");
            return null;

        }
        if ("Y".equals(runoff)) {
            if (statusRemarks == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Status Remarks when Run-Off is Yes!");
                return null;

            }
        }
        if ("ACTIVE".equals(agencyStatus) && "N".equals(runoff)) {
            if (agencyCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: The Agent record code is missing");
                return null;
            }
            //        } else if (accountTypeCode == null || accountTypeCode == "") {
            //            GlobalCC.errorValueNotEntered("Error Value Missing: You need to select an account type");
            //            return null;
            //
            //        }
            else if (shortDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Agency ID");
                return null;

            } else if (agencyName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Agency Name");
                return null;

            } else if (branchCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a branch");
                return null;

            }
            Rendering renderer = new Rendering();
            if (renderer.isCreditRatingRequired()) {
                if (txtCreditRting.getValue() == null) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Enter Client Credit Rating");

                    return null;


                }
            }

            if (txtAgencyPIN.isShowRequired() &&
                txtAgencyPIN.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please select the agent Pin");
                return null;
            }
            if (txtAgencyIDNum.isShowRequired() &&
                txtAgencyIDNum.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please select the agent Id");
                return null;
            }
            if (renderer.isAgencyBranchUnitMandatory() &&
                GlobalCC.isEmptyStr(txtBranchUnits.getValue())) {
                GlobalCC.INFORMATIONREPORTING("Please select Branch Unit");
                return null;
            }
        }
        String Query = SETUPS_AGENCIES_PRC;
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {


            conn =
(OracleConnection)(OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            // Take care of all the date fields on the form. Make sure they are not null.
            // If the dates are null, then default to the current date.
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            Date staEffectiveDate = new Date();
            if (txtStaEffectiveDate.getValue() != null &&
                !(txtStaEffectiveDate.getValue().equals(""))) {
                String date1 = df.format(txtStaEffectiveDate.getValue());
                staEffectiveDate = df.parse(date1);
            }

            Date commStatEffectiveDate = new Date();
            if (txtCommStatEffectiveDate.getValue() != null &&
                !(txtCommStatEffectiveDate.getValue().equals(""))) {
                String date2 = df.format(txtCommStatEffectiveDate.getValue());
                commStatEffectiveDate = df.parse(date2);
            }

            Date commStatusDate = new Date();
            if (txtCommStatusDate.getValue() != null &&
                !(txtCommStatusDate.getValue().equals(""))) {
                String date3 = df.format(txtCommStatusDate.getValue());
                commStatusDate = df.parse(date3);
            }

            Date agencyCheckDate = new Date();
            if (txtAgencyCheckDate.getValue() != null &&
                !(txtAgencyCheckDate.getValue().equals(""))) {
                String date4 = df.format(txtAgencyCheckDate.getValue());
                agencyCheckDate = df.parse(date4);
            }

            cst.setString(1, "E");

            try {
                cst.setBigDecimal(2, agencyCode);
            } catch (NumberFormatException e) {
                GlobalCC.INFORMATIONREPORTING("Agency code  should be a Number");
                return null;
            }
            System.out.println("Town code 2 = " +
                               (String)txtAgencyTownCode.getValue());
            cst.setString(3, accountTypeCode);
            cst.setString(4, shortDesc);
            cst.setString(5,
                          (agencyName != null) ? agencyName.toUpperCase() : null);
            cst.setString(6, (String)txtAgencyPhysicalAddress.getValue());
            cst.setString(7, (String)txtAgencyPostalAddress.getValue());
            cst.setString(8, (String)txtAgencyTownCode.getValue());
            cst.setString(9, String.valueOf(txtAgencyCountryCode.getValue()));
            cst.setString(10, (String)txtAgencyEmail.getValue());
            cst.setString(11, (String)txtAgencyWebAddress.getValue());
            cst.setString(12, (String)txtAgencyPostalCode.getValue());
            cst.setString(13, (String)txtContactPerson.getValue());
            cst.setString(14, (String)txtContactTitle.getValue());
            cst.setString(15, (String)txtAgencyPhone1.getValue());
            cst.setString(16, (String)txtAgencyPhone2.getValue());
            cst.setString(17, (String)txtAgencyFax.getValue());
            cst.setString(18, (String)txtAgencyAccountNumber.getValue());
            cst.setString(19, (String)txtAgencyPIN.getValue());
            cst.setString(20, agencyComm);
            cst.setString(21, (String)txtCreditAllowed.getValue());
            if (agencyWithTax == null || agencyWithTax == "") {
                cst.setBigDecimal(22, null);
            } else {
                try {
                    cst.setBigDecimal(22,
                                      agencyWithTax == null ? null : new BigDecimal(agencyWithTax));
                } catch (NumberFormatException e) {
                    GlobalCC.INFORMATIONREPORTING("Witholding Tax  should be a Number");
                }
            }
            cst.setString(23, (String)txtAgencyPrintdebitNote.getValue());
            cst.setString(24, "DRAFT");
            cst.setDate(25, new java.sql.Date(new Date().getTime()));
            cst.setString(26, (String)session.getAttribute("Username"));
            cst.setString(27, (String)txtRegistartionCode.getValue());
            cst.setString(28, (String)txtCommReserveRate.getValue());
            cst.setString(29, (String)txtAnnualBudget.getValue());
            cst.setDate(30, new java.sql.Date(staEffectiveDate.getTime()));
            cst.setString(31, (String)txtCreditPeriod.getValue());
            cst.setDate(32,
                        new java.sql.Date(commStatEffectiveDate.getTime()));
            cst.setDate(33, new java.sql.Date(commStatusDate.getTime()));
            cst.setString(34, (String)txtCommAllowed.getValue());
            cst.setString(35, (String)txtAgencyChecked.getValue());
            cst.setString(36, (String)txtAgencyCheckedBy.getValue());
            cst.setDate(37, new java.sql.Date(agencyCheckDate.getTime()));
            cst.setString(38, (String)txtCompCommArrears.getValue());
            cst.setString(39, (String)txtAgencyReinsurer.getValue());
            cst.setObject(40, txtAgencyBranchCode.getValue());
            cst.setString(41, (String)txtAgencyTownName.getValue());
            cst.setString(42, (String)txtAgencyCountryName.getValue());
            cst.setString(43, (String)txtStatusDesc.getValue());
            cst.setString(44, (String)txtAgencyIDNum.getValue());
            cst.setString(45, (String)txtAgencyContractCode.getValue());
            cst.setString(46, (String)txtAgencyAgentCode.getValue());
            String sms = GlobalCC.checkNullValues(txtAgencySms.getValue());

            String prefix = GlobalCC.checkNullValues(txtSmsPrefix.getValue());
            if (txtSmsPrefix.isVisible()) {
                if (prefix != null) {
                    //                    Row row =
                    //                        ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").getRowAtRangeIndex(new Integer(prefix));
                    //                    if (row.getAttribute("prefix") != null) {
                    //                        prefix = row.getAttribute("prefix").toString();
                    if (prefix.startsWith("0")) {
                        prefix = prefix.replaceFirst("0", "");
                    }
                    if (session.getAttribute("zipCode") == null) {
                        sms = prefix + "" + sms;
                    } else {
                        sms =
"+" + session.getAttribute("zipCode").toString() + prefix + "" + sms;
                    }
                    // }
                }
            } else {
                if (sms != null) {
                    if (sms.startsWith("0")) {
                        sms = sms.substring(1);
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    } else {
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    }
                }
            }

            cst.setString(47, sms);
            cst.setString(48, (String)txtAgencyHoldingCompanyCode.getValue());
            cst.setString(49, (String)txtAgencySectorCode.getValue());
            cst.setString(50, (String)txtAgencyClassCode.getValue());
            if (txtAgencyExpiriyDate.getValue() != null) {
                if (txtAgencyExpiriyDate.getValue().toString().contains(":")) {
                    cst.setObject(51,
                                  GlobalCC.parseDate(txtAgencyExpiriyDate.getValue()));
                } else {
                    cst.setObject(51,
                                  GlobalCC.upDateParseDate(txtAgencyExpiriyDate.getValue().toString()));
                }
            } else {
                cst.setString(51, null);
            }

            cst.setString(52, (String)txtAgencyLicenseNum.getValue());
            cst.setString(53, (String)txtAgencyRunoff.getValue());
            cst.setString(54, (String)txtAgencyLicensed.getValue());
            cst.setString(55, (String)txtLicenseGracePeriod.getValue());
            cst.setString(56, (String)txtOldAccountNum.getValue());
            cst.setString(57, (String)txtAgencyStatusRemarks.getValue());
            cst.setObject(58, txtBankBranchCode.getValue());
            cst.setObject(59, txtAccountNo.getValue());
            cst.setObject(60, txtPrefix.getValue());
            cst.setObject(61, session.getAttribute("stateCode"));
            cst.setObject(62, txtCreditRting.getValue());
            if (session.getAttribute("agentCodeType") == null ||
                session.getAttribute("agentCodeType").equals("")) {
                agentCode = null;
            } else {
                agentCode =
                        new BigDecimal(session.getAttribute("agentCodeType").toString());
            }
            if (txtAgencyName.getValue() == null) {
                SubAgentName = null;
            } else {
                SubAgentName = txtAgencyName.getValue().toString();
            }
            cst.setString(63, SubAgentName);
            cst.setBigDecimal(64, agentCode);
            cst.setBigDecimal(65, clnCode);
            cst.setBigDecimal(66, accountManagerCode);
            cst.setString(67, creditLimit);
            cst.setObject(68, session.getAttribute("bruCode"));
            cst.setObject(69, localReins);
            cst.setObject(70, txtIraRegNumber.getValue());
            cst.setObject(71, session.getAttribute("rorgCode"));
            cst.setObject(72, session.getAttribute("orsCode"));
            cst.setObject(73, txtIssueCert.getValue());
            cst.setObject(74, txtBouncedCheque.getValue());
            cst.setObject(75, txtModeOfComm.getValue());
            cst.setObject(76, session.getAttribute("bpnCode"));

            cst.setObject(77, txtAgentType.getValue());
            cst.setObject(78, txtAgentGroup.getValue());
            cst.setObject(79, txtVatApp.getValue());
            cst.setObject(80, txtWithTax.getValue());
            sms = GlobalCC.checkNullValues(txtTelPay.getValue());
            prefix = GlobalCC.checkNullValues(txtTelPayPrefix.getValue());
            if (txtTelPayPrefix.isVisible()) {
                if (prefix != null) {
                    //                    Row row =
                    //                        ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").getRowAtRangeIndex(new Integer(prefix));
                    //                    if (row.getAttribute("prefix") != null) {
                    //                        prefix = row.getAttribute("prefix").toString();
                    if (prefix.startsWith("0")) {
                        prefix = prefix.replaceFirst("0", "");
                    }
                    // sms = prefix + "" + sms;
                    if (session.getAttribute("zipCode") == null) {
                        sms = prefix + "" + sms;
                    } else {
                        sms =
"+" + session.getAttribute("zipCode").toString() + prefix + "" + sms;
                    }
                    // }
                }
            } else {
                if (sms != null) {
                    if (sms.startsWith("0")) {
                        sms = sms.substring(1);
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    } else {
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    }
                }
            }
            cst.setString(81, sms);
            String resetCode = null;
            Random rnd = new Random();
            Integer n = 10000000 + rnd.nextInt(99999999);
            resetCode = n.toString();
            cst.setString(82, resetCode);
            cst.setString(83,
                          GlobalCC.checkNullValues(session.getAttribute("PAYMENT_FREQ")));
            cst.setString(84,
                          GlobalCC.checkNullValues(session.getAttribute("PM_MODE")));
            cst.setString(85, (String)txtPmntDtlsValidated.getValue());

            cst.setDate(86, GlobalCC.extractDate(txtAgnBenStartDate));
            cst.setDate(87, GlobalCC.extractDate(txtAgnDOB));
            cst.setString(88, qualification);
            cst.setString(89, maritalStatus);
            cst.setString(90, IDNODocUsed);
            cst.setBigDecimal(91, agencySBU);
            cst.setString(92, commLevyApp);
            cst.setBigDecimal(93, commLevyRate);
            cst.setBigDecimal(94, regionCode);
            cst.setString(95, regionName);
            cst.setString(96, authorityName);
            cst.setString(97,
                          GlobalCC.checkNullValues(txtAgnMaritalStatus.getValue()));

            String validated =
                GlobalCC.checkNullValues(session.getAttribute("agentIprsValidated"));
            cst.setString(98, validated);
            cst.setString(99,
                          GlobalCC.checkNullValues(txtrelatedpart.getValue()));
            cst.execute();

            // Refresh the tree
            BigDecimal accTypeCode = null;
            if (session.getAttribute("accountTypeCode") != null) {
                try {
                    accTypeCode =
                            new BigDecimal(session.getAttribute("accountTypeCode").toString());

                } catch (NumberFormatException e) {
                    GlobalCC.INFORMATIONREPORTING("Acount  Type   Code  should be a Number");
                }
            }
            session.setAttribute("accountTypeCode", null);
            session.removeAttribute("accountTypeCode");

            //ADFUtils.findIterator("fetchAllAccountTypesIterator").executeQuery();
            //GlobalCC.refreshUI(accountTypeSelector);


            session.setAttribute("accountTypeCode",
                                 accTypeCode == null ? null :
                                 accTypeCode.toString());
            ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyInfo);

            cst.close();
            conn.commit();
            conn.close();

            // Reset the form
            //refreshAgencyDetailSection();
            BigDecimal taskId =
                GlobalCC.checkBDNullValues(session.getAttribute("taskID"));
            String activityName =
                GlobalCC.checkNullValues(session.getAttribute("activityName"));

            session.setAttribute("AgentCode", agencyCode);

            System.out.println("taskId: " + taskId);
            /* For Every Draft process i.e When you Update/Change any information about this Agent, Create a ticket if none exist.
             * or if the current task is not a 'Agent Draft'., and the Agent Exists
             * */
            if ((taskId == null || !"Agent Draft".equals(activityName)) &&
                session.getAttribute("AgentCode") != null) {
                //start new agent work flow
                TaskManipulation task = new TaskManipulation();
                String TaskId = task.agentWorkFlow();
                if (!"success".equals(TaskId)) {
                    return null;
                }
            }
            String message = "Agent UPDATED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    /**
     * Create and save a new Agency record
     *
     * @return Success message
     */
    public String actionCreateNewAgency() {


        BigDecimal agentCode;
        BigDecimal accountCode = null;
        String SubAgentName;
        BigDecimal clnCode;
        BigDecimal accountManagerCode;
        String localReins;
        int pref;
        System.out.println("This is the relationship manager" +
                           session.getAttribute("accountManagerCode"));
        if (session.getAttribute("accountManagerCode") != null) {
            accountManagerCode =
                    new BigDecimal(session.getAttribute("accountManagerCode").toString());
        } else {
            accountManagerCode = null;
        }
        if (txtLocalInt.getValue() != null) {
            localReins = txtLocalInt.getValue().toString();
        } else {
            localReins = null;
        }
        if (session.getAttribute("CLCode") == null) {
            clnCode = null;
        } else {
            clnCode =
                    new BigDecimal(session.getAttribute("CLCode").toString());
        }

        if (session.getAttribute("accountTypeCode") == null) {
            session.setAttribute("accountTypeCode",
                                 session.getAttribute("accType"));
        }

        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateCurrentAgency.getText().equals("Update")) {

            // Proceed to save
            String status = actionCreateUpdateAgency();

        } else {

            // Checking only the required fields
            String accountTypeCode =
                GlobalCC.checkNullValues(session.getAttribute("accountTypeCode"));
            String shortDesc =
                GlobalCC.checkNullValues(txtShortDesc.getValue());
            String agencyName =
                GlobalCC.checkNullValues(txtAgencyName.getValue());
            agencyName =
                    (agencyName != null) ? agencyName.toUpperCase() : null;
            String branchCode =
                GlobalCC.checkNullValues(txtAgencyBranchCode.getValue());
            String runoff =
                GlobalCC.checkNullValues(txtAgencyRunoff.getValue());
            String physicalAddress =
                GlobalCC.checkNullValues(txtAgencyPhysicalAddress.getValue());
            String emailAddress =
                GlobalCC.checkNullValues(txtAgencyEmail.getValue());
            String telephone1 =
                GlobalCC.checkNullValues(txtAgencyPhone1.getValue());
            String agencyComm =
                GlobalCC.checkNullValues(txtAgencyCommission.getValue());
            String creditLimit =
                GlobalCC.checkNullValues(txtCreditLimit.getValue());

            String benefitStartDate =
                GlobalCC.checkNullValues(txtAgnBenStartDate.getValue());
            String dob = GlobalCC.checkNullValues(txtAgnDOB.getValue());
            String qualification =
                GlobalCC.checkNullValues(txtAgnQualification.getValue());
            String maritalStatus =
                GlobalCC.checkNullValues(txtAgnMaritalStatus.getValue());
            String IDNODocUsed =
                GlobalCC.checkNullValues(txtAgnIDNODocUsed.getValue());
            BigDecimal agencySBU =
                GlobalCC.checkBDNullValues(txtAgencySBUCode.getValue());
            String commLevyApp =
                GlobalCC.checkNullValues(txtCommLevyApp.getValue());
            BigDecimal commLevyRate =
                GlobalCC.checkBDNullValues(txtCommLevyRate.getValue());
            BigDecimal regionCode =
                GlobalCC.checkBDNullValues(txtAgencyRegionCode.getValue());
            String regionName =
                GlobalCC.checkNullValues(txtAgencyRegionName.getValue());
            String authorityName =
                GlobalCC.checkNullValues(txtAgencyTaxAuthName.getValue());
            String statusRemarks =
                GlobalCC.checkNullValues(txtAgencyStatusRemarks.getValue());
            System.out.println("dob: " + dob);
            System.out.println("benefitStartDate: " + benefitStartDate);
            System.out.println("qualification: " + qualification);
            System.out.println("maritalStatus: " + maritalStatus);
            System.out.println("IDNODocUsed: " + IDNODocUsed);
            System.out.println("agencySBU: " + agencySBU);

            //            if (accountTypeCode == null || accountTypeCode == "") {
            //                GlobalCC.errorValueNotEntered("Error Value Missing: You need to select an account type");
            //                return null;
            //
            //
            //
            //            }

            System.out.println("This is it" + session.getAttribute("accType"));
            if (session.getAttribute("accType") != null) {
                accountCode =
                        new BigDecimal(session.getAttribute("accType").toString());
            }
            String agencyStatus = (String)txtAgencyStatus.getValue();
            if (runoff == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select Runoff value");
                return null;
            }
            if ("Y".equals(runoff)) {
                if (statusRemarks == null) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Status Remarks when Run-Off is Yes!");
                    return null;

                }
            }
            if (agencyStatus.equals("ACTIVE") && "N".equals(runoff)) {
                if (agencyName == null || agencyName == "") {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Agency Name");
                    return null;
                } else if (branchCode == null || branchCode == "") {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Select a branch");
                    return null;

                }
                Rendering renderer = new Rendering();
                if (renderer.isCreditRatingRequired()) {
                    if (txtCreditRting.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Error Value Missing: Enter Client Credit Rating");

                        return null;

                    }
                }

                if (txtAgencyPIN.isShowRequired() &&
                    txtAgencyPIN.getValue() == null) {
                    GlobalCC.INFORMATIONREPORTING("Please select the agent Pin");
                    return null;
                }
                if (txtAgencyIDNum.isShowRequired() &&
                    txtAgencyIDNum.getValue() == null) {
                    GlobalCC.INFORMATIONREPORTING("Please select the agent Id");
                    return null;
                }
                if (txtBranchUnits.isShowRequired() &&
                    txtBranchUnits.getValue() == null) {
                    GlobalCC.INFORMATIONREPORTING("Please select Branch Unit");
                    return null;
                }

            }


            // CRM-1452 validate Accounts fields
            FormUi formUi = new FormUi();
            if (!formUi.validate("mainAccountsTab")) { //main validation engine
                return null;
            }
            // END. CRM-1452
            if ("Y".equalsIgnoreCase(session.getAttribute("FX_AGENT_COUNTRY_CODE.required").toString()) &&
                txtAgencyPostalAddress.getValue().toString().length() <= 9)
                GlobalCC.errorValueNotEntered("Error Missing Postal Address!!");

            String Query = SETUPS_AGENCIES_PRC;
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                // Take care of all the date fields on the form. Make sure they are not null.
                // If the dates are null, then default to the current date.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date staEffectiveDate = new Date();
                if (txtStaEffectiveDate.getValue() != null &&
                    !(txtStaEffectiveDate.getValue().equals(""))) {
                    String date1 = df.format(txtStaEffectiveDate.getValue());
                    staEffectiveDate = df.parse(date1);
                }

                Date commStatEffectiveDate = new Date();
                if (txtCommStatEffectiveDate.getValue() != null &&
                    !(txtCommStatEffectiveDate.getValue().equals(""))) {
                    String date2 =
                        df.format(txtCommStatEffectiveDate.getValue());
                    commStatEffectiveDate = df.parse(date2);
                }

                Date commStatusDate = new Date();
                if (txtCommStatusDate.getValue() != null &&
                    !(txtCommStatusDate.getValue().equals(""))) {
                    String date3 = df.format(txtCommStatusDate.getValue());
                    commStatusDate = df.parse(date3);
                }


                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setBigDecimal(3, accountCode);
                cst.registerOutParameter(4, OracleTypes.VARCHAR); //shortDesc);
                cst.setString(5,
                              (agencyName != null) ? agencyName.toUpperCase() :
                              null);
                cst.setString(6, (String)txtAgencyPhysicalAddress.getValue());
                cst.setString(7, (String)txtAgencyPostalAddress.getValue());
                cst.setString(8, (String)txtAgencyTownCode.getValue());
                cst.setString(9,
                              String.valueOf(txtAgencyCountryCode.getValue()));
                cst.setString(10, (String)txtAgencyEmail.getValue());
                cst.setString(11, (String)txtAgencyWebAddress.getValue());
                cst.setString(12, (String)txtAgencyPostalCode.getValue());
                cst.setString(13, (String)txtContactPerson.getValue());
                cst.setString(14, (String)txtContactTitle.getValue());
                cst.setString(15, (String)txtAgencyPhone1.getValue());
                cst.setString(16, (String)txtAgencyPhone2.getValue());
                cst.setString(17, (String)txtAgencyFax.getValue());
                cst.setString(18, (String)txtAgencyAccountNumber.getValue());
                cst.setString(19, (String)txtAgencyPIN.getValue());
                cst.setString(20, agencyComm);
                cst.setString(21, (String)txtCreditAllowed.getValue());

                if (txtAgencyWthTx.getValue() != null &&
                    !(txtAgencyWthTx.getValue() == "")) {
                    cst.setBigDecimal(22,
                                      (BigDecimal)txtAgencyWthTx.getValue());
                } else {
                    cst.setString(22, (String)txtAgencyWthTx.getValue());
                }

                cst.setString(23, (String)txtAgencyPrintdebitNote.getValue());
                cst.setString(24, "DRAFT");
                cst.setDate(25, new java.sql.Date(new Date().getTime()));
                cst.setString(26, (String)session.getAttribute("Username"));
                cst.setString(27, (String)txtRegistartionCode.getValue());
                cst.setString(28, (String)txtCommReserveRate.getValue());
                cst.setString(29, (String)txtAnnualBudget.getValue());
                cst.setDate(30, new java.sql.Date(staEffectiveDate.getTime()));
                cst.setString(31, (String)txtCreditPeriod.getValue());
                cst.setDate(32,
                            new java.sql.Date(commStatEffectiveDate.getTime()));
                cst.setDate(33, new java.sql.Date(commStatusDate.getTime()));
                cst.setString(34, (String)txtCommAllowed.getValue());
                cst.setString(35, (String)txtAgencyChecked.getValue());
                cst.setString(36, (String)txtAgencyCheckedBy.getValue());
                cst.setDate(37, GlobalCC.extractDate(txtAgencyCheckDate));
                cst.setString(38, (String)txtCompCommArrears.getValue());
                cst.setString(39, (String)txtAgencyReinsurer.getValue());
                cst.setObject(40, txtAgencyBranchCode.getValue());
                cst.setString(41, (String)txtAgencyTownName.getValue());
                cst.setString(42, (String)txtAgencyCountryName.getValue());
                cst.setString(43, (String)txtStatusDesc.getValue());
                cst.setString(44, (String)txtAgencyIDNum.getValue());
                cst.setString(45, (String)txtAgencyContractCode.getValue());
                cst.setString(46, (String)txtAgencyAgentCode.getValue());
                String sms = GlobalCC.checkNullValues(txtAgencySms.getValue());
                String prefix =
                    GlobalCC.checkNullValues(txtSmsPrefix.getValue());
                if (txtSmsPrefix.isVisible()) {
                    if (prefix != null) {
                        //                    Row row =
                        //                        ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").getRowAtRangeIndex(new Integer(prefix));
                        //                    if (row.getAttribute("prefix") != null) {
                        //                        prefix = row.getAttribute("prefix").toString();
                        if (prefix.startsWith("0")) {
                            prefix = prefix.replaceFirst("0", "");
                        }
                        //sms = prefix + "" + sms;
                        if (session.getAttribute("zipCode") == null) {
                            sms = prefix + "" + sms;
                        } else {
                            sms =
"+" + session.getAttribute("zipCode").toString() + prefix + "" + sms;
                        }
                        // }
                    }
                } 
                
                else {
                    if(sms != null){
                    if (sms.startsWith("0")) {
                        sms = sms.substring(1);
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    } else {
                        sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                    }
                    }
                }
                cst.setString(47, sms);
                cst.setString(48,
                              (String)txtAgencyHoldingCompanyCode.getValue());
                cst.setString(49, (String)txtAgencySectorCode.getValue());
                cst.setString(50, (String)txtAgencyClassCode.getValue());
                cst.setDate(51, GlobalCC.extractDate(txtAgencyExpiriyDate));
                cst.setString(52, (String)txtAgencyLicenseNum.getValue());
                cst.setString(53, (String)txtAgencyRunoff.getValue());
                cst.setString(54, (String)txtAgencyLicensed.getValue());
                cst.setString(55, (String)txtLicenseGracePeriod.getValue());
                cst.setString(56, (String)txtOldAccountNum.getValue());
                cst.setString(57, (String)statusRemarks);

                cst.setObject(58, txtBankBranchCode.getValue());
                cst.setObject(59, txtAccountNo.getValue());
                cst.setObject(60, txtPrefix.getValue());
                cst.setObject(61, session.getAttribute("stateCode"));
                cst.setObject(62, txtCreditRting.getValue());
                if (session.getAttribute("agentCodeType") == null ||
                    session.getAttribute("agentCodeType").equals("")) {
                    agentCode = null;
                } else {
                    agentCode =
                            new BigDecimal(session.getAttribute("agentCodeType").toString());
                }
                session.setAttribute("accountTypeCode", accountCode);
                if (txtAgencyName.getValue() == null) {
                    SubAgentName = null;
                } else {
                    SubAgentName = txtAgencyName.getValue().toString();
                }
                cst.setString(63, SubAgentName);
                cst.setBigDecimal(64, agentCode);
                cst.setBigDecimal(65, clnCode);
                cst.setBigDecimal(66, accountManagerCode);
                cst.setString(67, creditLimit);
                cst.setObject(68, session.getAttribute("bruCode"));
                cst.setObject(69, localReins);
                cst.setObject(70, txtIraRegNumber.getValue());
                cst.setObject(71, session.getAttribute("rorgCode"));
                cst.setObject(72, session.getAttribute("orsCode"));
                cst.setObject(73, txtIssueCert.getValue());
                cst.setObject(74, txtBouncedCheque.getValue());
                cst.setObject(75, txtModeOfComm.getValue());
                cst.setObject(76, session.getAttribute("bpnCode"));

                cst.setObject(77, txtAgentType.getValue());
                cst.setObject(78, txtAgentGroup.getValue());
                cst.setObject(79, txtVatApp.getValue());
                cst.setObject(80, txtWithTax.getValue());
                sms = GlobalCC.checkNullValues(txtTelPay.getValue());
                prefix = GlobalCC.checkNullValues(txtTelPayPrefix.getValue());
                if (txtTelPayPrefix.isVisible()) {
                    if (prefix != null) {
                        //                    Row row =
                        //                        ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").getRowAtRangeIndex(new Integer(prefix));
                        //                    if (row.getAttribute("prefix") != null) {
                        //                        prefix = row.getAttribute("prefix").toString();
                        if (prefix.startsWith("0")) {
                            prefix = prefix.replaceFirst("0", "");
                        }
                        //sms = prefix + "" + sms;
                        if (session.getAttribute("zipCode") == null) {
                            sms = prefix + "" + sms;
                        } else {
                            sms =
"+" + session.getAttribute("zipCode").toString() + prefix + "" + sms;
                        }
                        // }
                    }
                } else {
                    if (sms != null) {
                        if (sms.startsWith("0")) {
                            sms = sms.substring(1);
                            sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                        } else {
                            sms =
"+" + session.getAttribute("zipCode").toString() + sms;
                        }
                    }
                }
                cst.setString(81, sms);
                String resetCode = null;
                Random rnd = new Random();
                Integer n = 10000000 + rnd.nextInt(99999999);
                resetCode = n.toString();
                cst.setString(82, resetCode);
                cst.setString(83,
                              (String)session.getAttribute("PAYMENT_FREQ"));
                //cst.setString(84, (String)session.getAttribute("PM_MODE"));
                cst.setObject(84, session.getAttribute("PM_MODE"));
                cst.setString(85, (String)txtPmntDtlsValidated.getValue());

                cst.setDate(86, GlobalCC.extractDate(txtAgnBenStartDate));
                cst.setDate(87, GlobalCC.extractDate(txtAgnDOB));
                cst.setString(88, qualification);
                cst.setString(89, maritalStatus);
                cst.setString(90, IDNODocUsed);
                cst.setBigDecimal(91, agencySBU);
                cst.setString(92, commLevyApp);
                cst.setBigDecimal(93, commLevyRate);
                cst.setBigDecimal(94, regionCode);
                cst.setString(95, regionName);
                cst.setString(96, authorityName);
                cst.setString(97,
                              GlobalCC.checkNullValues(txtAgnMaritalStatus.getValue()));
                String validated =
                    GlobalCC.checkNullValues(session.getAttribute("agentIprsValidated"));
                cst.setString(98, validated);
                cst.setString(99,
                              GlobalCC.checkNullValues(txtrelatedpart.getValue()));
                cst.execute();
                if (accountTypeCode.equalsIgnoreCase("16") &&
                    emailAddress != null) {
                    GlobalCC.applicationEmail(emailAddress, null,
                                              "Agents Portal Password",
                                              "Your Agents Portal Account has been Created. You need to Reset Password on Initial Login. Username:" +
                                              cst.getString(4) +
                                              " Reset Code:" + resetCode);
                }
                // Refresh the tree
                BigDecimal accTypeCode = null;
                if (session.getAttribute("accountTypeCode") != null ||
                    session.getAttribute("accountTypeCode") != "") {
                    accTypeCode =
                            new BigDecimal(session.getAttribute("accountTypeCode").toString());
                }
                session.setAttribute("accountTypeCode", null);
                session.removeAttribute("accountTypeCode");
                //ADFUtils.findIterator("fetchAllAccountTypesIterator").executeQuery();
                //GlobalCC.refreshUI(accountTypeSelector);


                session.setAttribute("accountTypeCode",
                                     accTypeCode.toString());
                ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
                GlobalCC.refreshUI(tblAgencyInfo);

                cst.close();
                conn.close();

                // Reset the form to avoid accidental creation of a new record with
                // the same values

                BigDecimal taskId =
                    GlobalCC.checkBDNullValues(session.getAttribute("taskID"));
                String activityName =
                    GlobalCC.checkNullValues(session.getAttribute("activityName"));
                session.setAttribute("AgentCode", agentCode);

                System.out.println("taskId: " + taskId);
                /* For Every Draft process i.e When you Update/Change any information about this Agent, Create a ticket if none exist.
                 * and the Agent Exists
                 * */

                if (agentCode != null) {
                    if (taskId == null) { //start new client work flow
                        TaskManipulation task = new TaskManipulation();
                        String TaskId = task.agentWorkFlow();
                    }
                    transitionToDraft();
                }
                //refreshAgencyDetailSection();
                actionLoadByAgentCode();


                String message = "Agent CREATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
                e.printStackTrace();
            }

        }
        return "Success";
    }


    /**
     * Opens up a popup dialog for selecting the towns, but only if a country
     * has been selected.
     *
     * @return
     */
    public String actionShowTownPopup() {

        // Check if a country code exists
        if (txtAStateCode.getValue() == null ||
            txtAStateName.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("You need to select a State  first!");

        } else {

            // Open the popup dialog to display towns
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:townListing" + "').show(hints);");
        }
        return null;
    }

    /**
     * Assign the country selected from the popup to the input text fields.
     *
     * @return
     */
    public String actionSelectCountryPop() {

        Object key2 = tblAgencyCountry.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            session.setAttribute("countryCode",
                                 nodeBinding.getAttribute("code"));
            ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
            GlobalCC.refreshUI(txtSmsPrefix);
            txtAgencyCountryName.setValue(nodeBinding.getAttribute("name"));
            txtAgencyCountryCode.setValue(nodeBinding.getAttribute("code"));
            String adminRegionType =
                nodeBinding.getAttribute("administrativeType") != null ?
                (String)nodeBinding.getAttribute("administrativeType") : null;
            if (adminRegionType != null) {

                pnmsgAdminRegionName.setLabel(GlobalCC.formatAdminUnitSingular(adminRegionType) +
                                              ":");
                dlgAdminRegionList.setTitle((GlobalCC.formatAdminUnitPlural(adminRegionType) +
                                             " List"));
                txtAStateCode.setValue(null);
                txtAStateName.setValue(null);
                txtAgencyTownCode.setValue(null);
                txtAgencyTownName.setValue(null);
                pnmsgAdminRegionName.setVisible(true);
                pnmsgTownName.setVisible(true);

            }

        } else {
            pnmsgAdminRegionName.setVisible(false);
            pnmsgTownName.setVisible(false);
            //  pnmsgTownName.setVisible(true);
        }
        GlobalCC.refreshUI(frmLoadAdminTownDetails);
        GlobalCC.dismissPopUp("pt1", "countryPop");

        return null;

    }

    /**
     * Assign the town selected from the popup to the input text fields.
     *
     * @return
     */
    public String actionSelectTownPop() {

        Object key2 = tblAgencyTown.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAgencyTownName.setValue(nodeBinding.getAttribute("name"));
            txtAgencyTownCode.setValue(nodeBinding.getAttribute("code"));
        }
        return null;
    }

    public void setTblAgencyCountry(RichTable tblAgencyCountry) {
        this.tblAgencyCountry = tblAgencyCountry;
    }

    public RichTable getTblAgencyCountry() {
        return tblAgencyCountry;
    }

    public void setTblAgencyTown(RichTable tblAgencyTown) {
        this.tblAgencyTown = tblAgencyTown;
    }

    public RichTable getTblAgencyTown() {
        return tblAgencyTown;
    }

    public String actionAcceptHoldingCompany() {

        Object key2 = tblAgencyHoldingCompany.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {
            txtAgencyHoldingCompanyCode.setValue(n.getAttribute("code"));
            txtAgencyHoldingCompanyName.setValue(n.getAttribute("name"));
            System.out.println("code: " + n.getAttribute("code"));
            GlobalCC.refreshUI(txtAgencyHoldingCompanyCode);
            GlobalCC.refreshUI(txtAgencyHoldingCompanyName);
        }
        GlobalCC.dismissPopUp("pt1", "holdingCompany");
        return null;
    }

    public void setTblAgencyHoldingCompany(RichTable tblAgencyHoldingCompany) {
        this.tblAgencyHoldingCompany = tblAgencyHoldingCompany;
    }

    public RichTable getTblAgencyHoldingCompany() {
        return tblAgencyHoldingCompany;
    }

    public void setTxtAgencyHoldingCompanyName(RichInputText txtAgencyHoldingCompanyName) {
        this.txtAgencyHoldingCompanyName = txtAgencyHoldingCompanyName;
    }

    public RichInputText getTxtAgencyHoldingCompanyName() {
        return txtAgencyHoldingCompanyName;
    }

    public String actionAcceptAgencyClass() {

        Object key2 = tblAgencyClasses.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAgencyClassCode.setValue(nodeBinding.getAttribute("code"));
            txtAgencyClassName.setValue(nodeBinding.getAttribute("description"));

            GlobalCC.refreshUI(txtAgencyClassCode);
            GlobalCC.refreshUI(txtAgencyClassName);
        }
        GlobalCC.dismissPopUp("pt1", "AgencyClassPop");
        return null;
    }

    public String actionAcceptSector() {

        Object key2 = tblAgencySector.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAgencySectorCode.setValue(nodeBinding.getAttribute("code"));
            txtAgencySectorName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtAgencySectorCode);
            GlobalCC.refreshUI(txtAgencySectorName);
        }
        GlobalCC.dismissPopUp("pt1", "sectorPop");

        return null;

    }

    public void setTblAgencyClasses(RichTable tblAgencyClasses) {
        this.tblAgencyClasses = tblAgencyClasses;
    }

    public RichTable getTblAgencyClasses() {
        return tblAgencyClasses;
    }

    public void setTblAgencySector(RichTable tblAgencySector) {
        this.tblAgencySector = tblAgencySector;
    }

    public RichTable getTblAgencySector() {
        return tblAgencySector;
    }

    public void setTxtAgencySectorName(RichInputText txtAgencySectorName) {
        this.txtAgencySectorName = txtAgencySectorName;
    }

    public RichInputText getTxtAgencySectorName() {
        return txtAgencySectorName;
    }

    public void setTxtAgencyClassName(RichInputText txtAgencyClassName) {
        this.txtAgencyClassName = txtAgencyClassName;
    }

    public RichInputText getTxtAgencyClassName() {
        return txtAgencyClassName;
    }

    public String actionAcceptAgencyBranch() {

        Object key2 = tblAgencyBranch.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        session.removeAttribute("branchCode");

        if (n != null) {
            txtAgencyBranchCode.setValue(n.getAttribute("id"));
            txtAgencyBranchName.setValue(n.getAttribute("name"));
            session.setAttribute("branchCode", n.getAttribute("id"));
            GlobalCC.refreshUI(txtAgencyBranchName);
            GlobalCC.refreshUI(txtAgencyBranchCode);
            System.out.println("txtAgencyBranchCode " +
                               txtAgencyBranchCode.getValue());
        }

        System.out.println("branchCode: " +
                           session.getAttribute("branchCode"));

        GlobalCC.dismissPopUp("pt1", "agencyBranchPop");
        return null;
    }

    public String actionAcceptAgencyRegion() {

        Object key2 = tblRegion.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {
            txtAgencyRegionCode.setValue(n.getAttribute("regionCode"));
            session.setAttribute("regionCode", n.getAttribute("regionCode"));
            txtAgencyRegionName.setValue(n.getAttribute("regionName"));
            GlobalCC.refreshUI(txtAgencyRegionName);

            ADFUtils.findIterator("findAccountBranchesIterator").executeQuery();

            GlobalCC.refreshUI(tblAgencyBranch);
            GlobalCC.refreshUI(branchUnitsTbl);
        }
        GlobalCC.dismissPopUp("pt1", "agencyRegionPop");
        return null;
    }

    public String actionShowBranches() {

        BigDecimal regCode =
            GlobalCC.checkBDNullValues(session.getAttribute("regionCode"));
        Rendering render = new Rendering();
        if (regCode == null &&
            (render.isAGENT_REGION_VISIBLE() && render.isAGENT_REGION_REQUIRED())) {
            GlobalCC.errorValueNotEntered("Please select region!");
            return null;
        }
        GlobalCC.showPopup("pt1:agencyBranchPop");
        return null;
    }

    public String actionAcceptAgencySBU() {
        Object key2 = tblAgencySBU.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            txtAgencySBUCode.setValue(nodeBinding.getAttribute("SBU_CODE"));
            txtAgencySBUName.setValue(nodeBinding.getAttribute("SBU"));
            GlobalCC.refreshUI(txtAgencySBUCode);
            GlobalCC.refreshUI(txtAgencySBUName);
            System.out.println("txtAgencySBUCode " +
                               txtAgencySBUCode.getValue());
        }
        GlobalCC.dismissPopUp("pt1", "agencySBUPop");
        return null;
    }


    public void setTblAgencyBranch(RichTable tblAgencyBranch) {
        this.tblAgencyBranch = tblAgencyBranch;
    }

    public RichTable getTblAgencyBranch() {
        return tblAgencyBranch;
    }

    public void setTxtAgencyBranchName(RichInputText txtAgencyBranchName) {
        this.txtAgencyBranchName = txtAgencyBranchName;
    }

    public RichInputText getTxtAgencyBranchName() {
        return txtAgencyBranchName;
    }

    public void setRegistrationTable(RichTable registrationTable) {
        this.registrationTable = registrationTable;
    }

    public RichTable getRegistrationTable() {
        return registrationTable;
    }

    public String addRegDetails() {

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:regPop" +
                             "').show(hints);");
        regyear.setValue("");
        regno.setValue("");
        regWef.setValue("");
        regWet.setValue("");
        regAccepted.setValue("");
        saveRegistrationDetails.setText("Save");
        return null;
    }

    public String updateRegDetails() {
        Object key2 = registrationTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
        
            // CRM-1975
            try {
                final String year = "01/01/" + nodeBinding.getAttribute("year");
                regyear.setValue(new SimpleDateFormat("dd/MM/yyyy").parse(year));
                
            } catch(Exception ex){
                GlobalCC.EXCEPTIONREPORTING(ex);
                ex.printStackTrace();
            }
            // END. CRM-1975
        
            
            regno.setValue(nodeBinding.getAttribute("regno"));
            regWef.setValue(nodeBinding.getAttribute("wef"));
            regWet.setValue(nodeBinding.getAttribute("wet"));
            regAccepted.setValue(nodeBinding.getAttribute("accepted"));
            regkey.setValue(nodeBinding.getAttribute("id"));

            saveRegistrationDetails.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:regPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("no record selected.");
            return null;
        }
        return null;
    }

    public String deleteRegDetails() {

        Object key2 = registrationTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteAgencyRegPop" +
                                 "').show(hints);");

        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected::");
        }
        return null;
    }

    public String saveRegistrationDetails() {

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_AGENCY_REGISTRATION_PRC(?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        if (saveRegistrationDetails.getText().equals("Save")) {

            // Proceed to do an update
            String regAcceptedVal =
                GlobalCC.checkNullValues(regAccepted.getValue());
            String regNum = GlobalCC.checkNullValues(regno.getValue());
            if (regAcceptedVal == null || regAcceptedVal.equals("")) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select Accepted");
                return null;
            } else if (regNum == null || regNum.equals("")) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Reg Number");
                return null;
            } else if (regyear.getValue() == null ||
                       regyear.getValue().equals("")) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Year");
                return null;
            } else if (regWef.getValue() == null ||
                       regWef.getValue().equals("")) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the WEF date");
                return null;
            } else if (regWet.getValue() == null ||
                       regWet.getValue().equals("")) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the WET date");
                return null;
            } else {

                // Validate the dates
                Date dateYear = (java.util.Date)regyear.getValue();
                Date dateWef = (java.util.Date)regWef.getValue();
                Date dateWet = (java.util.Date)regWet.getValue();

                Calendar calYear = Calendar.getInstance();
                calYear.setTime(dateYear);
                Calendar calWef = Calendar.getInstance();
                calWef.setTime(dateWef);
                Calendar calWet = Calendar.getInstance();
                calWet.setTime(dateWet);

                int year1 = calWef.get(Calendar.YEAR);
                int month1 = calWef.get(Calendar.MONTH);

                int year2 = calWet.get(Calendar.YEAR);
                int month2 = calWet.get(Calendar.MONTH);

                // Calculate the difference in months : This is because registration
                // is only within a year i.e. 12 months
                int months = ((year2 * 12) + month2) - ((year1 * 12) + month1);


                int tempYear = calYear.get(Calendar.YEAR);


                // Make sure the WEF is within the year
                if (calWef.get(Calendar.YEAR) != tempYear) {
                    GlobalCC.errorValueNotEntered("The WEF date has to be within the year specified");
                    return null;
                } else if (months > 12 || months <= 0) {
                    GlobalCC.errorValueNotEntered("The WET date has to be within the first 12 months from the WEF date. Please select another WET date");
                    return null;
                } else {

                    String agencyCode =
                        GlobalCC.checkNullValues(session.getAttribute("ClientCode"));

                    LinkedList reglist = new LinkedList();
                    AgentRegistration regbean = new AgentRegistration();
                    Date year = (Date)regyear.getValue();
                    BigDecimal yr =
                        new BigDecimal(new SimpleDateFormat("yyyy").format(year));
                    Date wef = (Date)regWef.getValue();
                    Date wet = (Date)regWet.getValue();

                    regbean.setAREG_CODE(null);
                    regbean.setAREG_ACCEPTED((String)regAccepted.getValue());
                    regbean.setAREG_AGN_CODE(new BigDecimal(agencyCode));
                    regbean.setAREG_REG_NO((String)regno.getValue());
                    regbean.setAREG_WEF(new java.sql.Date(wef.getTime()));
                    regbean.setAREG_WET(new java.sql.Date(wet.getTime()));
                    regbean.setAREG_YEAR(yr);
                    reglist.add(regbean);

                    try {
                        conn =
(OracleConnection)connection.getDatabaseConnection();
                        stmt =
(OracleCallableStatement)conn.prepareCall(query);
                        ArrayDescriptor descriptor =
                            ArrayDescriptor.createDescriptor("TQC_AGENCY_REGISTRATION_TAB",
                                                             conn);
                        array =
                                new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                        stmt.setString(1, "A");
                        stmt.setArray(2, array);
                        stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                        stmt.execute();
                        String error = stmt.getString(3);


                        stmt.close();
                        conn.commit();
                        conn.close();

                        ExtendedRenderKitService erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                        erkService.addScript(FacesContext.getCurrentInstance(),
                                             "var hints = {autodismissNever:false}; " +
                                             "AdfPage.PAGE.findComponent('" +
                                             "pt1:regPop" + "').hide(hints);");

                        ADFUtils.findIterator("findAgentsRegistrationIterator").executeQuery();
                        GlobalCC.refreshUI(registrationTable);

                        String message = "New Record ADDED Successfully!";
                        GlobalCC.INFORMATIONREPORTING(message);

                    } catch (SQLException e) {
                        GlobalCC.EXCEPTIONREPORTING(conn, e);
                    }
                }
            }

        } else if (saveRegistrationDetails.getText().equals("Update")) {

            AgentRegistration regbean = new AgentRegistration();
            Date year = (Date)regyear.getValue();
            LinkedList reglist = new LinkedList();
            BigDecimal yr =
                new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            regbean.setAREG_CODE(new BigDecimal((String)regkey.getValue()));
            regbean.setAREG_ACCEPTED((String)regAccepted.getValue());
            regbean.setAREG_AGN_CODE(new BigDecimal((String)session.getAttribute("agencyCode")));
            regbean.setAREG_REG_NO((String)regno.getValue());
            Date wef = (Date)regWef.getValue();
            regbean.setAREG_WEF(new java.sql.Date(wef.getTime()));
            Date wet = (Date)regWet.getValue();
            regbean.setAREG_WET(new java.sql.Date(wet.getTime()));
            regbean.setAREG_YEAR(yr);
            reglist.add(regbean);

            try {
                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_REGISTRATION_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "E");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(3);


                stmt.close();
                conn.commit();
                conn.close();

                // Hide the popup
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:regPop" + "').hide(hints);");

                ADFUtils.findIterator("findAgentsRegistrationIterator").executeQuery();
                GlobalCC.refreshUI(registrationTable);

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        GlobalCC.dismissPopUp("pt1", "regPop");

        return null;
    }

    public void setSaveRegistrationDetails(RichCommandButton saveRegistrationDetails) {
        this.saveRegistrationDetails = saveRegistrationDetails;
    }

    public RichCommandButton getSaveRegistrationDetails() {
        return saveRegistrationDetails;
    }

    public void setRegkey(RichInputText regkey) {
        this.regkey = regkey;
    }

    public RichInputText getRegkey() {
        return regkey;
    }

    public void setRegyear(RichInputDate regyear) {
        this.regyear = regyear;
    }

    public RichInputDate getRegyear() {
        return regyear;
    }

    public void setRegno(RichInputText regno) {
        this.regno = regno;
    }

    public RichInputText getRegno() {
        return regno;
    }

    public void setRegWef(RichInputDate regWef) {
        this.regWef = regWef;
    }

    public RichInputDate getRegWef() {
        return regWef;
    }

    public void setRegWet(RichInputDate regWet) {
        this.regWet = regWet;
    }

    public RichInputDate getRegWet() {
        return regWet;
    }

    public void setRegAccepted(RichSelectOneChoice regAccepted) {
        this.regAccepted = regAccepted;
    }

    public RichSelectOneChoice getRegAccepted() {
        return regAccepted;
    }

    public void setAgentDirectorsTable(RichTable agentDirectorsTable) {
        this.agentDirectorsTable = agentDirectorsTable;
    }

    public RichTable getAgentDirectorsTable() {
        return agentDirectorsTable;
    }

    public String addAgencyDirectors() {

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:agentDirectorsPop" + "').show(hints);");
        directYr.setValue(null);
        directname.setValue(null);
        directQualifications.setValue(null);
        directshare.setValue(null);
        txtPhoneNumber.setValue(null);
        txtNationality.setValue(null);
        saveAgDirectorButton.setText("Save");
        return null;
    }

    public String editAgencyDirectors() {

        Object key2 = agentDirectorsTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            agencyDirectorId.setValue(nodeBinding.getAttribute("id"));
            directYr.setValue(nodeBinding.getAttribute("year"));
            directname.setValue(nodeBinding.getAttribute("name"));
            directQualifications.setValue(nodeBinding.getAttribute("qualification"));
            directshare.setValue(nodeBinding.getAttribute("shareholding"));
            txtPhoneNumber.setValue(nodeBinding.getAttribute("phoneNumber"));
            txtPrincipleDirector.setValue(nodeBinding.getAttribute("principleDirecor"));
            txtNationality.setValue(nodeBinding.getAttribute("country"));
            saveAgDirectorButton.setText("Update");

            GlobalCC.showPopup("pt1:agentDirectorsPop");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String deleteAgencyDirectors() {

        Object key2 = agentDirectorsTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteAgencyDirectorPop" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String saveAgDirectorsAction() {

        if (directYr.getValue() == null) {
            GlobalCC.errorValueNotEntered("Year Required:");
            return null;
        }


        String share = GlobalCC.checkNullValues(directshare.getValue());
        String agncode =
            GlobalCC.checkNullValues(session.getAttribute("agencyCode"));
        String name = GlobalCC.checkNullValues(directname.getValue());
        String qual =
            GlobalCC.checkNullValues(directQualifications.getValue());
        String dirId = GlobalCC.checkNullValues(agencyDirectorId.getValue());
        String dirNumber = GlobalCC.checkNullValues(txtPhoneNumber.getValue());
        String principleDire =
            GlobalCC.checkNullValues(txtPrincipleDirector.getValue());
        BigDecimal nationality =
            GlobalCC.checkBDNullValues(session.getAttribute("countryCode"));
        try {
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection =
                        (OracleConnection)connector.getDatabaseConnection();
                String query =
                    "begin TQC_AGENCIES_PKG.getAgencyPrincipleDir(?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        new BigDecimal((String)session.getAttribute("agencyCode")));
                statement.setObject(2, principleDire);
                statement.execute();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_AGENCY_DIRECTORS_PRC(?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        Date year = null;
        BigDecimal yr = null;
        if (directYr.getValue() != null) {
            year = (Date)directYr.getValue();
            yr = new BigDecimal(new SimpleDateFormat("yyyy").format(year));
        }
        if (saveAgDirectorButton.getText().equals("Save")) {
            AgDirector regbean = new AgDirector();

            LinkedList reglist = new LinkedList();


            regbean.setADIR_CODE(null);
            regbean.setADIR_AGN_CODE(new BigDecimal(agncode));
            regbean.setADIR_DESIGNATION("");
            regbean.setADIR_NAME(name);
            regbean.setADIR_PCT_HOLDG(share == null ? null :
                                      new BigDecimal(share));
            regbean.setADIR_YEAR(yr);
            regbean.setADIR_QUALIFICATIONS(qual);
            regbean.setADIR_PHONE_NUMBER(dirNumber);
            regbean.setADIR_PRINCIPLE_DIR(principleDire);
            regbean.setADIR_NATIONALITY(nationality);
            reglist.add(regbean);
            try {
                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_DIRECTORS_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "A");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(3);


                stmt.close();
                conn.close();
                if (error != null) {
                    GlobalCC.INFORMATIONREPORTING("Error occured while saving ::" +
                                                  error);
                } else {
                    GlobalCC.INFORMATIONREPORTING("Record savedSuccessfully");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:agentDirectorsPop" +
                                         "').hide(hints);");
                }
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        } else if (saveAgDirectorButton.getText().equals("Update")) {
            AgDirector regbean = new AgDirector();
            LinkedList reglist = new LinkedList();

            yr = new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            regbean.setADIR_CODE(dirId == null ? null : new BigDecimal(dirId));
            regbean.setADIR_AGN_CODE(new BigDecimal(agncode));
            regbean.setADIR_DESIGNATION("");
            regbean.setADIR_NAME(name);
            regbean.setADIR_PCT_HOLDG(share == null ? null :
                                      new BigDecimal(share));
            regbean.setADIR_YEAR(yr);
            regbean.setADIR_QUALIFICATIONS(qual);
            regbean.setADIR_PHONE_NUMBER(dirNumber);
            regbean.setADIR_PRINCIPLE_DIR(principleDire);
            regbean.setADIR_NATIONALITY(nationality);
            reglist.add(regbean);
            try {
                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_DIRECTORS_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "E");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(3);
                stmt.close();
                conn.close();

                if (error != null) {
                    GlobalCC.INFORMATIONREPORTING("Error occured while updating ::" +
                                                  error);
                } else {
                    GlobalCC.INFORMATIONREPORTING("Record Updated Successfully");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:agentDirectorsPop" +
                                         "').hide(hints);");
                }

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        ADFUtils.findIterator("findAgentsDirectorsIterator").executeQuery();
        GlobalCC.refreshUI(agentDirectorsTable);
        return null;
    }

    public void setAgencyDirectorId(RichInputText agencyDirectorId) {
        this.agencyDirectorId = agencyDirectorId;
    }

    public RichInputText getAgencyDirectorId() {
        return agencyDirectorId;
    }

    public void setDirectYr(RichInputDate directYr) {
        this.directYr = directYr;
    }

    public RichInputDate getDirectYr() {
        return directYr;
    }

    public void setDirectname(RichInputText directname) {
        this.directname = directname;
    }

    public RichInputText getDirectname() {
        return directname;
    }

    public void setDirectQualifications(RichInputText directQualifications) {
        this.directQualifications = directQualifications;
    }

    public RichInputText getDirectQualifications() {
        return directQualifications;
    }

    public void setDirectshare(RichInputNumberSpinbox directshare) {
        this.directshare = directshare;
    }

    public RichInputNumberSpinbox getDirectshare() {
        return directshare;
    }

    public void setSaveAgDirectorButton(RichCommandButton saveAgDirectorButton) {
        this.saveAgDirectorButton = saveAgDirectorButton;
    }

    public RichCommandButton getSaveAgDirectorButton() {
        return saveAgDirectorButton;
    }

    public void setAgencyRefereeTable(RichTable agencyRefereeTable) {
        this.agencyRefereeTable = agencyRefereeTable;
    }

    public RichTable getAgencyRefereeTable() {
        return agencyRefereeTable;
    }

    public String saveAgencyReferees() {

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:agencyRefereePop" + "').show(hints);");
        refereeName.setValue("");
        refereephAddress.setValue("");
        refereePostAddress.setValue("");
        refereeIDNo.setValue("");
        refereeEmail.setValue("");
        refereeTelNo.setValue("");
        saveRefereeAgButton.setText("Save");
        return null;
    }

    public String updateAgencyReferees() {

        Object key2 = agencyRefereeTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            refereeId.setValue(nodeBinding.getAttribute("id"));
            refereeName.setValue(nodeBinding.getAttribute("name"));
            refereephAddress.setValue(nodeBinding.getAttribute("phyaddress"));
            refereePostAddress.setValue(nodeBinding.getAttribute("postaddress"));
            refereeIDNo.setValue(nodeBinding.getAttribute("idno"));
            refereeEmail.setValue(nodeBinding.getAttribute("email"));
            refereeTelNo.setValue(nodeBinding.getAttribute("telno"));

            saveRefereeAgButton.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:agencyRefereePop" + "').show(hints);");

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String deleteAgencyReferee() {

        Object key2 = agencyRefereeTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteAgencyRefereePop" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }

        return null;

    }

    public String saveRefereeAgencies() {

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_AGENCIES_PKG.TQC_AGENCY_REFEREES_PRC(?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        if (saveRefereeAgButton.getText().equals("Save")) {
            AgencyReferee regbean = new AgencyReferee();
            LinkedList reglist = new LinkedList();
            regbean.setAREF_CODE(null);
            regbean.setAREF_AGN_CODE(new BigDecimal((String)session.getAttribute("agencyCode")));
            regbean.setAREF_NAME((String)refereeName.getValue());
            regbean.setAREF_PHYSICAL_ADDRESS((String)refereephAddress.getValue());
            regbean.setAREF_POSTAL_ADDRESS((String)refereePostAddress.getValue());
            regbean.setAREF_ID_NO((String)refereeIDNo.getValue());
            regbean.setAREF_EMAIL_ADDRESS((String)refereeEmail.getValue());
            regbean.setAREF_TEL((String)refereeTelNo.getValue());
            reglist.add(regbean);
            try {
                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_REFEREES_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "A");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();

                stmt.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("findAgentsRefereesIterator").executeQuery();
                GlobalCC.refreshUI(agencyRefereeTable);

                String message = "New Record ADDED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        } else if (saveRefereeAgButton.getText().equals("Update")) {
            AgencyReferee regbean = new AgencyReferee();
            LinkedList reglist = new LinkedList();
            regbean.setAREF_CODE(new BigDecimal((String)refereeId.getValue()));
            regbean.setAREF_AGN_CODE(new BigDecimal((String)session.getAttribute("agencyCode")));
            regbean.setAREF_NAME((String)refereeName.getValue());
            regbean.setAREF_PHYSICAL_ADDRESS((String)refereephAddress.getValue());
            regbean.setAREF_POSTAL_ADDRESS((String)refereePostAddress.getValue());
            regbean.setAREF_ID_NO((String)refereeIDNo.getValue());
            regbean.setAREF_EMAIL_ADDRESS((String)refereeEmail.getValue());
            regbean.setAREF_TEL((String)refereeTelNo.getValue());
            reglist.add(regbean);
            try {
                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);
                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_REFEREES_TAB",
                                                     conn);
                array =
                        new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                stmt.setString(1, "E");
                stmt.setArray(2, array);
                stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                stmt.execute();

                stmt.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("findAgentsRefereesIterator").executeQuery();
                GlobalCC.refreshUI(agencyRefereeTable);

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        GlobalCC.dismissPopUp("pt1", "agencyRefereePop");
        return null;
    }

    public void setSaveRefereeAgButton(RichCommandButton saveRefereeAgButton) {
        this.saveRefereeAgButton = saveRefereeAgButton;
    }

    public RichCommandButton getSaveRefereeAgButton() {
        return saveRefereeAgButton;
    }

    public void setRefereeId(RichInputText refereeId) {
        this.refereeId = refereeId;
    }

    public RichInputText getRefereeId() {
        return refereeId;
    }

    public void setRefereeName(RichInputText refereeName) {
        this.refereeName = refereeName;
    }

    public RichInputText getRefereeName() {
        return refereeName;
    }

    public void setRefereephAddress(RichInputText refereephAddress) {
        this.refereephAddress = refereephAddress;
    }

    public RichInputText getRefereephAddress() {
        return refereephAddress;
    }

    public void setRefereePostAddress(RichInputText refereePostAddress) {
        this.refereePostAddress = refereePostAddress;
    }

    public RichInputText getRefereePostAddress() {
        return refereePostAddress;
    }

    public void setRefereeIDNo(RichInputText refereeIDNo) {
        this.refereeIDNo = refereeIDNo;
    }

    public RichInputText getRefereeIDNo() {
        return refereeIDNo;
    }

    public void setRefereeEmail(RichInputText refereeEmail) {
        this.refereeEmail = refereeEmail;
    }

    public RichInputText getRefereeEmail() {
        return refereeEmail;
    }

    public void setRefereeTelNo(RichInputText refereeTelNo) {
        this.refereeTelNo = refereeTelNo;
    }

    public RichInputText getRefereeTelNo() {
        return refereeTelNo;
    }

    public void setSelectValues(List<SelectItem> selectValues) {
        this.selectValues = selectValues;
    }

    public List<SelectItem> getSelectValues() {
        return selectValues;
    }

    public void setDisplayValue(List<String> displayValue) {
        this.displayValue = displayValue;
    }

    public List<String> getDisplayValue() {
        return displayValue;
    }

    public void setSystemShuttle(RichSelectManyShuttle systemShuttle) {
        this.systemShuttle = systemShuttle;
    }

    public RichSelectManyShuttle getSystemShuttle() {
        return systemShuttle;
    }

    public void updateAgentSystems(ValueChangeEvent valueChangeEvent) {
        DBConnector datahandler = new DBConnector();
        OracleConnection conn = null;

        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            try {
                conn = (OracleConnection)datahandler.getDatabaseConnection();
                ArrayList<String> myVals =
                    (ArrayList<String>)systemShuttle.getValue();
                ArrayList<SelectItem> myVals2 =
                    (ArrayList<SelectItem>)systemSelectItem.getValue();
                int v = 0;
                String revokeQuery =
                    "begin tqc_clients_pkg.unalloc_clnt_system(?,?); end;";

                String agencyCode =
                    GlobalCC.checkNullValues(session.getAttribute("agencyCode"));

                while (v < myVals2.size()) {
                    SelectItem select = myVals2.get(v);
                    OracleCallableStatement cst = null;

                    String sampNum =
                        GlobalCC.checkNullValues(select.getValue());


                    cst =
(OracleCallableStatement)conn.prepareCall(revokeQuery);
                    cst.setBigDecimal(1, new BigDecimal(agencyCode));
                    cst.setBigDecimal(2, new BigDecimal(sampNum));
                    cst.execute();

                    v++;
                }
                String query =
                    "begin tqc_clients_pkg.alloc_clnt_system(?,?); end;";
                int k = 0;
                while (k < myVals.size()) {
                    OracleCallableStatement cst = null;


                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.setBigDecimal(1, new BigDecimal(agencyCode));
                    cst.setBigDecimal(2, new BigDecimal(myVals.get(k)));
                    cst.execute();


                    k++;
                }
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }
    }

    public void setSystemSelectItem(UISelectItems systemSelectItem) {
        this.systemSelectItem = systemSelectItem;
    }

    public UISelectItems getSystemSelectItem() {
        return systemSelectItem;
    }

    public void setWebUsersTable(RichTable webUsersTable) {
        this.webUsersTable = webUsersTable;
    }

    public RichTable getWebUsersTable() {
        return webUsersTable;
    }

    public String addWebUserAction() {

        if (txtAgencyCode.getValue() == null ||
            txtAgencyCode.getValue().equals("")) {
            GlobalCC.INFORMATIONREPORTING("You need to first select an Agent!");

        } else {

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
            saveWebUsersButton.setText("Save");
        }

        return null;
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

    public String updateWebUserAction() {

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

            saveWebUsersButton.setText("Update");

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


    public String deleteWebUserAction() {

        Object key2 = webUsersTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            GlobalCC.showPopup("pt1:confirmDeleteAgencyWebAccPop");
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
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteWebAccPop" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String saveWebUsersOperation() {
        BigDecimal sysCode = new BigDecimal("0");
        if (session.getAttribute("sysCode") != null) {
            sysCode =
                    new BigDecimal(session.getAttribute("sysCode").toString());
        }
        if (txtAgencyCode.getValue() == null ||
            txtAgencyCode.getValue().equals("")) {
            TurnQuest.view.Base.GlobalCC.INFORMATIONREPORTING("You need to first select an Agent!");

        } else {

            DBConnector connection = new DBConnector();
            String query =
                "begin TQC_AGENCIES_PKG.TQC_ACCOUNT_CONTACTS_PRC(?,?,?,?,?);end;";
            ARRAY array = null;
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            String currentUser = (String)session.getAttribute("Username");
            if (saveWebUsersButton.getText().equals("Save")) {
                WebUser regbean = new WebUser();
                LinkedList reglist = new LinkedList();
                regbean.setACCC_CODE(null);
                regbean.setACCC_AGN_CODE(new BigDecimal((String)txtAgencyCode.getValue()));
                regbean.setACCC_USERNAME((String)webusername.getValue());
                regbean.setACCC_NAME((String)webfullNames.getValue());
                regbean.setACCC_TEL((String)telNo.getValue());
                regbean.setACCC_EMAIL_ADDR((String)webEmail.getValue());
                regbean.setACCC_PERSONEL_RANK((String)webPersonalRank.getValue());
                regbean.setACCC_CREATED_BY(currentUser);
                regbean.setACCC_LOGIN_ALLOWED((String)webAllowLogin.getValue());
                regbean.setACCC_STATUS((String)webUserStatus.getValue());
                regbean.setACCC_PWD_RESET((String)webReset.getValue());
                regbean.setACCC_PWD((String)webPassword.getValue());
                reglist.add(regbean);
                try {
                    conn =
(OracleConnection)connection.getDatabaseConnection();
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ACCOUNT_CONTACTS_TAB",
                                                         conn);
                    array =
                            new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                    stmt.setString(1, "A");
                    stmt.setArray(2, array);
                    stmt.setString(3, currentUser);
                    stmt.setBigDecimal(5, sysCode);
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

            } else if (saveWebUsersButton.getText().equals("Update")) {
                WebUser regbean = new WebUser();
                LinkedList reglist = new LinkedList();
                regbean.setACCC_CODE(new BigDecimal((String)webUserId.getValue()));
                regbean.setACCC_AGN_CODE(new BigDecimal((String)session.getAttribute("agencyCode")));
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
                    conn =
(OracleConnection)connection.getDatabaseConnection();
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ACCOUNT_CONTACTS_TAB",
                                                         conn);
                    array =
                            new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                    stmt.setString(1, "E");
                    stmt.setArray(2, array);
                    stmt.setString(3, currentUser);
                    stmt.setBigDecimal(5, sysCode);
                    stmt.registerOutParameter(4, OracleTypes.VARCHAR);
                    stmt.execute();
                    stmt.close();
                    conn.commit();
                    conn.close();
                    GlobalCC.dismissPopUp("pt1", "webUserPop");
                    String message = "Record UPDATED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (SQLException e) {
                    TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
            ADFUtils.findIterator("findwebUserAccountsIterator").executeQuery();
            GlobalCC.refreshUI(webUsersTable);
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


    public void setSaveWebUsersButton(RichCommandButton saveWebUsersButton) {
        this.saveWebUsersButton = saveWebUsersButton;
    }

    public RichCommandButton getSaveWebUsersButton() {
        return saveWebUsersButton;
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

    public void setAccountTypeSelector(RichSelectOneChoice accountTypeSelector) {
        this.accountTypeSelector = accountTypeSelector;
    }

    public RichSelectOneChoice getAccountTypeSelector() {
        return accountTypeSelector;
    }

    public void setPanelAccountSystems(RichPanelBox panelAccountSystems) {
        this.panelAccountSystems = panelAccountSystems;
    }

    public RichPanelBox getPanelAccountSystems() {
        return panelAccountSystems;
    }

    public void setBtnCreateUpdateCurrentAgency(RichCommandButton btnCreateUpdateCurrentAgency) {
        this.btnCreateUpdateCurrentAgency = btnCreateUpdateCurrentAgency;
    }

    public RichCommandButton getBtnCreateUpdateCurrentAgency() {
        return btnCreateUpdateCurrentAgency;
    }

    public String processOpenAgencyReport() {
        // Add event code here...
        return null;
    }

    public void setPanelDetailSystems(RichPanelBox panelDetailSystems) {
        this.panelDetailSystems = panelDetailSystems;
    }

    public RichPanelBox getPanelDetailSystems() {
        return panelDetailSystems;
    }

    public void setTxtSelectedUserSystemCode(RichInputText txtSelectedUserSystemCode) {
        this.txtSelectedUserSystemCode = txtSelectedUserSystemCode;
    }

    public RichInputText getTxtSelectedUserSystemCode() {
        return txtSelectedUserSystemCode;
    }

    public void setBtnAddAccountSystem(RichCommandButton btnAddAccountSystem) {
        this.btnAddAccountSystem = btnAddAccountSystem;
    }

    public RichCommandButton getBtnAddAccountSystem() {
        return btnAddAccountSystem;
    }

    public void setBtnRemoveAccountSystem(RichCommandButton btnRemoveAccountSystem) {
        this.btnRemoveAccountSystem = btnRemoveAccountSystem;
    }

    public RichCommandButton getBtnRemoveAccountSystem() {
        return btnRemoveAccountSystem;
    }

    public String processAddAccountSystem() {
        boolean processStatusOK = true;
        if (session.getAttribute("agencyCode") == null ||
            (session.getAttribute("agencyCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select an Account first.");
        }

        if (txtSelectedUserSystemCode.getValue() == null ||
            txtSelectedUserSystemCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a System first.");
        }

        if (processStatusOK) {
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection =
                        (OracleConnection)connector.getDatabaseConnection();
                String query =
                    "begin TQC_AGENCIES_PKG.grant_agent_system(?,?,?,?,?,?); end;";
                OracleCallableStatement statement = null;

                BigDecimal agencyCode =
                    new BigDecimal(hiddenAccountCode.getValue().toString());
                BigDecimal userSystemCode =
                    new BigDecimal(txtSelectedUserSystemCode.getValue().toString());

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setString(1, "A");
                statement.setBigDecimal(2, agencyCode);
                statement.setBigDecimal(3, userSystemCode);
                statement.setDate(4, null);
                statement.setDate(5, null);
                statement.setString(6, null);
                statement.execute();
                connection.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }


        }
        // Get the user systems
        ADFUtils.findIterator("fetchUnassignedAccountSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedSystems);

        ADFUtils.findIterator("fetchAllAgencyAssignedSystemsIterator").executeQuery();
        GlobalCC.refreshUI(tblAssignedAgencySystems);

        txtSelectedUserSystemCode.setValue(null);

        return null;
    }

    public String processRemoveAccountSystem() {
        boolean processStatusOK = true;
        if (session.getAttribute("agencyCode") == null ||
            (session.getAttribute("agencyCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select an Account first.");
        }

        if (txtSelectedUserSystemCode.getValue() == null ||
            txtSelectedUserSystemCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a System first.");
        }

        if (processStatusOK) {
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection =
                        (OracleConnection)connector.getDatabaseConnection();
                String query =
                    "begin TQC_AGENCIES_PKG.revoke_agent_system(?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        new BigDecimal((String)session.getAttribute("agencyCode")));
                statement.setBigDecimal(2,
                                        new BigDecimal(txtSelectedUserSystemCode.getValue().toString()));
                statement.execute();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }

        txtSelectedUserSystemCode.setValue(null);

        // Get the user systems
        ADFUtils.findIterator("fetchUnassignedAccountSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedSystems);

        ADFUtils.findIterator("fetchAllAgencyAssignedSystemsIterator").executeQuery();
        GlobalCC.refreshUI(tblAssignedAgencySystems);

        return null;
    }


    public void setTreeUnassignedSystems(RichTree treeUnassignedSystems) {
        this.treeUnassignedSystems = treeUnassignedSystems;
    }

    public RichTree getTreeUnassignedSystems() {
        return treeUnassignedSystems;
    }

    public void setTreeAssignedSystems(RichTree treeAssignedSystems) {
        this.treeAssignedSystems = treeAssignedSystems;
    }

    public RichTree getTreeAssignedSystems() {
        return treeAssignedSystems;
    }

    public void unassignedAccountSystemSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeUnassignedSystems.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeUnassignedSystems.getRowData();

                    txtSelectedUserSystemCode.setValue(nd.getRow().getAttribute("sysCode"));
                }
            }
        }
        GlobalCC.refreshUI(panelDetailSystems);
    }

    public void assignedAccountSystemSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeAssignedSystems.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeAssignedSystems.getRowData();

                    txtSelectedUserSystemCode.setValue(nd.getRow().getAttribute("sysCode"));
                }
            }
        }
        GlobalCC.refreshUI(panelDetailSystems);
    }

    public void processCommissionChanged(ValueChangeEvent valueChangeEvent) {

        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            String commValue =
                GlobalCC.checkNullValues(txtCommAllowed.getValue());

            if (commValue.equals("Y")) {

            } else if (commValue.equals("N")) {

            }
        }
    }

    public void setTblAgencyInfo(RichTable tblAgencyInfo) {
        this.tblAgencyInfo = tblAgencyInfo;
    }

    public RichTable getTblAgencyInfo() {
        return tblAgencyInfo;
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
        UIComponent source = accountTypeSelector;
        String alignId = source.getClientId(context);
        String popupId = "pt1:ServerMessage";

        StringBuilder script = new StringBuilder();
        script.append("var popup = AdfPage.PAGE.findComponent('").append(popupId).append("'); ").append("if (!popup.isPopupVisible()) { ").append("var hints = {}; ").append("hints[AdfRichPopup.HINT_ALIGN_ID] = '").append(alignId).append("'; ").append("hints[AdfRichPopup.HINT_ALIGN] = AdfRichPopup.ALIGN_AFTER_START; ").append("popup.show(hints);}");
        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());
    }

    public void setHiddenAccountCode(RichInputText hiddenAccountCode) {
        this.hiddenAccountCode = hiddenAccountCode;
    }

    public RichInputText getHiddenAccountCode() {
        return hiddenAccountCode;
    }

    public void setTblAgencyAccounts(RichTable tblAgencyAccounts) {
        this.tblAgencyAccounts = tblAgencyAccounts;
    }

    public RichTable getTblAgencyAccounts() {
        return tblAgencyAccounts;
    }

    public void clearAgencyAccountFields() {
        txtAccountCode.setValue(null);
        txtAccShortDesc.setValue(null);
        txtAccName.setValue(null);

        txtAccAgencyCode.setValue(session.getAttribute("agencyCode").toString());
        txtAccCreatedBy.setValue(null);
        txtAccDateCreated.setValue(null);
        //txtClientAccStatus.setValue(null);
        txtAccRemarks.setValue(null);
        txtAccWef.setValue(null);
        txtAccWet.setValue(null);
        txtDivCode.setValue(null);
        txtDivName.setValue(null);
        txtAgencyTaxAuthName.setValue(null);
    }

    public String actionNewAgencyAccount() {


        if (session.getAttribute("agencyCode") == null) {

            GlobalCC.INFORMATIONREPORTING("FIRST SELECT AGENCY ");

            return null;
        }

        clearAgencyAccountFields();
        // txtAgencyPostalAddress.setValue("P.O. BOX");
        GlobalCC.refreshUI(txtAgencyPostalAddress);
        GlobalCC.refreshUI(agentAccountPop);
        btnSaveUpdateAgencyAccount.setText("Save");
        session.setAttribute("accountsAction", "A");

        if (session.getAttribute("agencyCode") == null) {

            GlobalCC.INFORMATIONREPORTING("SELECT AGENCY FIRST");
        }
        //hide  generate acc fields
        genAccLabel.setVisible(true);
        genAccNoSelect.setVisible(true);
        genAcSelectYes.setVisible(true);

        //refresh pop

        GlobalCC.showPopup("pt1:agencyAccountPopup");
        return null;
    }

    public String actionEditAgencyAccount() {
        Object key2 = tblAgencyAccounts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("accountsAction", "E");

            txtAccountCode.setValue(nodeBinding.getAttribute("code"));
            txtAccShortDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            txtAccName.setValue(nodeBinding.getAttribute("name"));
            txtAccAgencyCode.setValue(nodeBinding.getAttribute("agentCode"));
            txtAccCreatedBy.setValue(nodeBinding.getAttribute("createdBy"));
            txtAccDateCreated.setValue(nodeBinding.getAttribute("dateCreated"));
            txtAgencyAccStatus.setValue(nodeBinding.getAttribute("status"));
            txtAccRemarks.setValue(nodeBinding.getAttribute("remarks"));
            txtAccWef.setValue(nodeBinding.getAttribute("wef"));
            txtAccWet.setValue(nodeBinding.getAttribute("wet"));
            txtDivCode.setValue(nodeBinding.getAttribute("divCode"));
            txtDivName.setValue(nodeBinding.getAttribute("divName"));
            session.setAttribute("accountsDivCode",
                                 nodeBinding.getAttribute("divCode"));

            //hide  generate acc fields
            genAccLabel.setVisible(true);
            genAccNoSelect.setVisible(true);
            genAcSelectYes.setVisible(true);
            //refresh pop
            GlobalCC.refreshUI(agentAccountPop);

            btnSaveUpdateAgencyAccount.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:agencyAccountPopup" + "').show(hints);");

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }
        return null;
    }

    public String actionDeleteAgencyAccount() {


        Object key2 = tblAgencyAccounts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteAgencyAccountPop" +
                                 "').show(hints);");
        } else {

            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public void setTxtAccountCode(RichInputText txtAccountCode) {
        this.txtAccountCode = txtAccountCode;
    }

    public RichInputText getTxtAccountCode() {
        return txtAccountCode;
    }

    public void setTxtAccShortDesc(RichInputText txtAccShortDesc) {
        this.txtAccShortDesc = txtAccShortDesc;
    }

    public RichInputText getTxtAccShortDesc() {
        return txtAccShortDesc;
    }

    public void setTxtAccName(RichInputText txtAccName) {
        this.txtAccName = txtAccName;
    }

    public RichInputText getTxtAccName() {
        return txtAccName;
    }

    public void setTxtAccAgencyCode(RichInputText txtAccAgencyCode) {
        this.txtAccAgencyCode = txtAccAgencyCode;
    }

    public RichInputText getTxtAccAgencyCode() {
        return txtAccAgencyCode;
    }

    public void setTxtAccCreatedBy(RichInputText txtAccCreatedBy) {
        this.txtAccCreatedBy = txtAccCreatedBy;
    }

    public RichInputText getTxtAccCreatedBy() {
        return txtAccCreatedBy;
    }

    public void setTxtAccDateCreated(RichInputDate txtAccDateCreated) {
        this.txtAccDateCreated = txtAccDateCreated;
    }

    public RichInputDate getTxtAccDateCreated() {
        return txtAccDateCreated;
    }

    public void setTxtAgencyAccStatus(RichSelectOneChoice txtAgencyAccStatus) {
        this.txtAgencyAccStatus = txtAgencyAccStatus;
    }

    public RichSelectOneChoice getTxtAgencyAccStatus() {
        return txtAgencyAccStatus;
    }

    public void setTxtAccRemarks(RichInputText txtAccRemarks) {
        this.txtAccRemarks = txtAccRemarks;
    }

    public RichInputText getTxtAccRemarks() {
        return txtAccRemarks;
    }

    public void setTxtAccWef(RichInputDate txtAccWef) {
        this.txtAccWef = txtAccWef;
    }

    public RichInputDate getTxtAccWef() {
        return txtAccWef;
    }

    public void setTxtAccWet(RichInputDate txtAccWet) {
        this.txtAccWet = txtAccWet;
    }

    public RichInputDate getTxtAccWet() {
        return txtAccWet;
    }

    public void setBtnSaveUpdateAgencyAccount(RichCommandButton btnSaveUpdateAgencyAccount) {
        this.btnSaveUpdateAgencyAccount = btnSaveUpdateAgencyAccount;
    }

    public RichCommandButton getBtnSaveUpdateAgencyAccount() {
        return btnSaveUpdateAgencyAccount;
    }

    public String actionSaveUpdateAgencyAccount() {
        if (btnSaveUpdateAgencyAccount.getText().equals("Edit")) {
            actionUpdateAgencyAccount();
        } else {
            String accCode =
                GlobalCC.checkNullValues(txtAccountCode.getValue());

            String accShtDesc =
                GlobalCC.checkNullValues(txtAccShortDesc.getValue());
            String accName = GlobalCC.checkNullValues(txtAccName.getValue());
            String accAgencyCode =
                GlobalCC.checkNullValues(txtAccAgencyCode.getValue());
            String accCreatedBy =
                GlobalCC.checkNullValues(txtAccCreatedBy.getValue());
            String accDateCreated =
                GlobalCC.checkNullValues(txtAccDateCreated.getValue());
            String accStatus =
                GlobalCC.checkNullValues(txtAgencyAccStatus.getValue());
            String accRemarks =
                GlobalCC.checkNullValues(txtAccRemarks.getValue());
            String accWef = GlobalCC.checkNullValues(txtAccWef.getValue());
            String accWet = GlobalCC.checkNullValues(txtAccWet.getValue());
            String divCode = GlobalCC.checkNullValues(txtDivCode.getValue());
            if (txtAgencyPIN.isShowRequired() &&
                txtAgencyPIN.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please select the agent Pin");
                return null;
            }
            if (txtAgencyIDNum.isShowRequired() &&
                txtAgencyIDNum.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please select the agent Id");
                return null;
            }
            if (txtBranchUnits.isShowRequired() &&
                txtBranchUnits.getValue() == null) {
                GlobalCC.INFORMATIONREPORTING("Please select Branch Unit");
                return null;
            }
            if (divCode == null &&
                new Rendering().isAGENT_DIVISION_NAME_REQUIRED()) {
                GlobalCC.INFORMATIONREPORTING("Error Select A Division");
                return null;
            }


            if (accName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
                return null;

            } else if (accWef == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WEF date is missing");
                return null;

            } else if (accStatus == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a status");
                return null;

            } else {

                String Query =
                    "begin TQC_AGENCIES_PKG.create_agency_account(?,?,?,?,?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date newWefDate = new Date();
                    if (txtAccWef.getValue() != null &&
                        !(txtAccWef.getValue().equals(""))) {
                        String date1 = df.format(txtAccWef.getValue());
                        newWefDate = df.parse(date1);
                    }

                    Date newWetDate = null; //new Date();
                    if (txtAccWet.getValue() != null &&
                        !(txtAccWet.getValue().equals(""))) {
                        String date2 = df.format(txtAccWet.getValue());
                        newWetDate = df.parse(date2);
                    }

                    cst.setString(1, "A");
                    cst.setString(2, null);
                    cst.setString(3, accShtDesc);
                    cst.setString(4, accName);
                    cst.setBigDecimal(5,
                                      new BigDecimal(session.getAttribute("agencyCode").toString()));
                    cst.setString(6,
                                  session.getAttribute("Username").toString());
                    cst.setDate(7,
                                new java.sql.Date(new java.util.Date().getTime()));
                    cst.setString(8, accStatus);
                    cst.setString(9, accRemarks);
                    cst.setDate(10,
                                newWefDate == null ? null : new java.sql.Date(newWefDate.getTime()));
                    cst.setDate(11,
                                newWetDate == null ? null : new java.sql.Date(newWetDate.getTime()));
                    cst.setBigDecimal(12,
                                      divCode == null ? null : new BigDecimal(divCode));
                    cst.execute();

                    // Close the connections

                    conn.commit();
                    cst.close();
                    conn.close();
                    conn = null;
                    cst = null;

                    ADFUtils.findIterator("fetchAgencyAccountsIterator").executeQuery();
                    GlobalCC.refreshUI(tblAgencyAccounts);

                    GlobalCC.hidePopup("pt1:agencyAccountPopup");

                    String message = "Agency Account ADDED Successfully!";
                    //showMessagePopup(message);
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                } finally {
                    DbUtils.closeQuietly(conn, cst, null);
                }
            }
        }
        return null;
    }

    public String actionUpdateAgencyAccount() {
        String accCode = GlobalCC.checkNullValues(txtAccountCode.getValue());
        String accShtDesc =
            GlobalCC.checkNullValues(txtAccShortDesc.getValue());
        String accName = GlobalCC.checkNullValues(txtAccName.getValue());
        String accAgencyCode =
            GlobalCC.checkNullValues(txtAccAgencyCode.getValue());
        String accCreatedBy =
            GlobalCC.checkNullValues(txtAccCreatedBy.getValue());
        String accDateCreated =
            GlobalCC.checkNullValues(txtAccDateCreated.getValue());
        String accStatus =
            GlobalCC.checkNullValues(txtAgencyAccStatus.getValue());
        String accRemarks = GlobalCC.checkNullValues(txtAccRemarks.getValue());
        String accWef = GlobalCC.checkNullValues(txtAccWef.getValue());
        String accWet = GlobalCC.checkNullValues(txtAccWet.getValue());

        String divCode = GlobalCC.checkNullValues(txtDivCode.getValue());


        if (accCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (accShtDesc == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
            return null;

        } else if (accName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
            return null;

        } else if (accWef == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WEF date is missing");
            return null;

        } else if (accStatus == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select a status");
            return null;

        } else {

            String Query =
                "begin TQC_AGENCIES_PKG.create_agency_account(?,?,?,?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date newWefDate = new Date();
                if (txtAccWef.getValue() != null &&
                    !(txtAccWef.getValue().equals(""))) {
                    String date1 = df.format(txtAccWef.getValue());
                    newWefDate = df.parse(date1);
                }

                Date newWetDate = null; //new Date();
                if (txtAccWet.getValue() != null &&
                    !(txtAccWet.getValue().equals(""))) {
                    String date2 = df.format(txtAccWet.getValue());
                    newWetDate = df.parse(date2);
                }

                cst.setString(1, "E");
                cst.setString(2, accCode);
                cst.setString(3, accShtDesc);
                cst.setString(4, accName);
                cst.setBigDecimal(5,
                                  new BigDecimal(session.getAttribute("agencyCode").toString()));
                cst.setString(6, session.getAttribute("Username").toString());
                cst.setDate(7,
                            new java.sql.Date(new java.util.Date().getTime()));
                cst.setString(8, accStatus);
                cst.setString(9, accRemarks);
                cst.setDate(10,
                            newWefDate == null ? null : new java.sql.Date(newWefDate.getTime()));
                cst.setDate(11,
                            newWetDate == null ? null : new java.sql.Date(newWetDate.getTime()));
                cst.setBigDecimal(12,
                                  divCode == null ? null : new BigDecimal(divCode));
                cst.execute();


                // Close the connections
                cst.close();
                conn.commit();
                conn.close();

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:agencyAccountPopup" +
                                     "').hide(hints);");

                String message = "Agency Account UPDATED Successfully!";
                //showMessagePopup(message);
                GlobalCC.INFORMATIONREPORTING(message);

                ADFUtils.findIterator("fetchAgencyAccountsIterator").executeQuery();
                GlobalCC.refreshUI(tblAgencyAccounts);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void setTblGlAccounts(RichTable tblGlAccounts) {
        this.tblGlAccounts = tblGlAccounts;
    }

    public RichTable getTblGlAccounts() {
        return tblGlAccounts;
    }

    public String actionAcceptGlAccount() {
        Object key2 = tblGlAccounts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAgencyAccountNumber.setValue(nodeBinding.getAttribute("accNumber"));
            GlobalCC.refreshUI(txtAgencyAccountNumber);
        }
        GlobalCC.dismissPopUp("pt1", "glAccountsPop");
        return null;
    }

    public String actionShowGlAccounts() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:glAccountsPop" + "').show(hints);");
        return null;
    }

    public void setTxtDivCode(RichInputText txtDivCode) {
        this.txtDivCode = txtDivCode;
    }

    public RichInputText getTxtDivCode() {
        return txtDivCode;
    }

    public void divisionSelected(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findDivisionsIterator");
            RowKeySet set = divisionLov.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));

                Row r = dciter.getCurrentRow();
                txtDivCode.setValue(r.getAttribute("DIV_CODE"));
                txtDivName.setValue(r.getAttribute("DIV_NAME"));
                GlobalCC.refreshUI(txtDivCode);
                GlobalCC.refreshUI(txtDivName);
                session.setAttribute("accountsDivCode",
                                     r.getAttribute("DIV_CODE"));
            }
        }
    }

    public String showAccountTypesPop() {
        GlobalCC.showPopup("pt1:accountTypesPop");
        return null;
    }

    public String actionAcceptAccountTypes() {
        // GlobalCC.refreshUI(txtAgent);
        Object key2 = tblAccountTypesPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String mainAccType = nodeBinding.getAttribute("code").toString();
            txtMainAccountTypeCode.setValue(mainAccType);
            session.setAttribute("docAccountType",
                                 nodeBinding.getAttribute("accountTypeShortDesc").toString());
            txtMainAccountType.setValue(nodeBinding.getAttribute("accountType"));
            GlobalCC.refreshUI(txtMainAccountType);
            session.setAttribute("accType", nodeBinding.getAttribute("code"));
            session.setAttribute("accountTypeCode", mainAccType);
            session.setAttribute("agencyCode", null);
            session.setAttribute("bactAccountType", "A");

            // the demo tree
            ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyInfo);

            refreshAgencyDetailSection();
            GlobalCC.refreshUI(panelAgencyDetails);
            if (session.getAttribute("accountTypeCode").toString() == "25" ||
                session.getAttribute("accountTypeCode").equals("25")) {
                txtAgent.setRendered(true);
                txtAgntLov.setRendered(true);
                txtAgentsAccountsLabel.setRendered(true);
                subAgntDetailsTab.setRendered(false);
                agentRecDocstab.setRendered(false);
            } else {
                txtAgent.setRendered(false);
                txtAgntLov.setRendered(false);
                txtAgentsAccountsLabel.setRendered(false);
            }
            if (session.getAttribute("accountTypeCode").toString() == "2" ||
                session.getAttribute("accountTypeCode").equals("2")) {
                subAgntDetailsTab.setText("Sub Agents Details");
                subAgntDetailsTab.setRendered(true);
                agentRecDocstab.setRendered(true);
            }
            if (session.getAttribute("accountTypeCode").toString() == "3" ||
                session.getAttribute("accountTypeCode").equals("3")) {
                subAgntDetailsTab.setText("Sub Agents Details");
                subAgntDetailsTab.setRendered(true);
                agentRecDocstab.setRendered(true);
            }
            if (session.getAttribute("accountTypeCode").toString() == "25" ||
                session.getAttribute("accountTypeCode").equals("25")) {
                subAgntDetailsTab.setText("Sub Agents Details");
                subAgntDetailsTab.setRendered(true);
                agentRecDocstab.setRendered(true);
            }
            if (session.getAttribute("accountTypeCode").equals("4") ||
                session.getAttribute("accountTypeCode").equals("7")) {
                txtAgent.setRendered(true);
                txtAgntLov.setRendered(true);
                txtAgentsAccountsLabel.setRendered(true);
                txtAgent.setLabel("Insurance Company");
                txtAgentsAccountsLabel.setLabel("Insurance Company");
                subAgntDetailsTab.setRendered(false);
                agentRecDocstab.setRendered(false);
            }
            if (session.getAttribute("accountTypeCode").equals("5") ||
                session.getAttribute("accountTypeCode").equals("3")) {
                txtAgent.setRendered(false);
                txtAgntLov.setRendered(false);
                txtAgentsAccountsLabel.setRendered(false);
                subAgntDetailsTab.setText("Facre Accounts Details");
                subAgntDetailsTab.setRendered(true);
                agentRecDocstab.setRendered(true);
            }
            if (new Rendering().getInhouseIdPinMand() == true) {
                if (session.getAttribute("accountTypeCode").equals("16")) {
                    txtAgencyPIN.setShowRequired(true);
                    txtAgencyIDNum.setShowRequired(true);
                    txtBranchUnits.setShowRequired(true);

                } else {
                    txtAgencyPIN.setShowRequired(false);
                    txtAgencyIDNum.setShowRequired(false);
                    txtBranchUnits.setShowRequired(false);
                }
            }
            System.out.println("This is the account Code" +
                               nodeBinding.getAttribute("code"));
            // Agencies Accounts
            ADFUtils.findIterator("fetchAgencyAccountsIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyAccounts);
        }
        GlobalCC.dismissPopUp("pt1", "accountTypesPop");
        return null;
    }

    public String actionAcceptAccountTypeEdit() {
        Object key2 = tblAccountTypesPopEdit.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String mainAccType = nodeBinding.getAttribute("code").toString();
            String Query =
                "begin TQC_SETUPS_PKG.agencies_editAccountType(?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);
                cst.setObject(1, session.getAttribute("agencyCode"));
                cst.setObject(2, nodeBinding.getAttribute("code"));
                cst.execute();
                conn.commit();
                cst.close();
                conn.close();
                ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
                GlobalCC.refreshUI(tblAgencyInfo);
                mainPanelTab.setVisible(false);
                GlobalCC.refreshUI(mainPanelTab);
                GlobalCC.INFORMATIONREPORTING("Agency Account Type Changed Successfully.");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        GlobalCC.dismissPopUp("pt1", "accountTypeEdit");
        return null;

    }

    public void setTxtDivName(RichInputText txtDivName) {
        this.txtDivName = txtDivName;
    }

    public RichInputText getTxtDivName() {
        return txtDivName;
    }

    public String actionUpdateAgencySystem() {
        Object key2 = tblAssignedAgencySystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAsysSysCode.setValue(nodeBinding.getAttribute("asysSysCode"));
            txtAsysAgnCode.setValue(nodeBinding.getAttribute("asysAgnCode"));
            txtAsysWef.setValue(nodeBinding.getAttribute("asysWef"));
            txtAsysWet.setValue(nodeBinding.getAttribute("asysWet"));
            txtAsysComments.setValue(nodeBinding.getAttribute("asysComment"));
            txtAsysOsdCode.setValue(nodeBinding.getAttribute("asysOsdCode"));
            txtAsysOsdId.setValue(nodeBinding.getAttribute("asysOsdId"));
            txtOsdName.setValue(nodeBinding.getAttribute("subdivisionName"));
            btnSaveUpdateAgencySystem.setText("Edit");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:agencySystemPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("You need to select a Record to proceed.");
            return null;
        }
        return null;
    }

    public void setTblAssignedAgencySystems(RichTable tblAssignedAgencySystems) {
        this.tblAssignedAgencySystems = tblAssignedAgencySystems;
    }

    public RichTable getTblAssignedAgencySystems() {
        return tblAssignedAgencySystems;
    }

    public void tblAssignedAgencySystemsListener(SelectionEvent selectionEvent) {
        Object key2 = tblAssignedAgencySystems.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("sysCode",
                                 nodeBinding.getAttribute("sysCode"));
            txtSelectedUserSystemCode.setValue(nodeBinding.getAttribute("sysCode"));

            ADFUtils.findIterator("fetchLowestOrgSubDivisionsBySystemIterator").executeQuery();
            GlobalCC.refreshUI(tblOrgSubDivisions);

            GlobalCC.refreshUI(panelDetailSystems);
        }
    }

    public void setPanelAgencySystem(RichPanelBox panelAgencySystem) {
        this.panelAgencySystem = panelAgencySystem;
    }

    public RichPanelBox getPanelAgencySystem() {
        return panelAgencySystem;
    }

    public void setTxtAsysSysCode(RichInputText txtAsysSysCode) {
        this.txtAsysSysCode = txtAsysSysCode;
    }

    public RichInputText getTxtAsysSysCode() {
        return txtAsysSysCode;
    }

    public void setTxtAsysAgnCode(RichInputText txtAsysAgnCode) {
        this.txtAsysAgnCode = txtAsysAgnCode;
    }

    public RichInputText getTxtAsysAgnCode() {
        return txtAsysAgnCode;
    }

    public void setTxtAsysWef(RichInputDate txtAsysWef) {
        this.txtAsysWef = txtAsysWef;
    }

    public RichInputDate getTxtAsysWef() {
        return txtAsysWef;
    }

    public void setTxtAsysWet(RichInputDate txtAsysWet) {
        this.txtAsysWet = txtAsysWet;
    }

    public RichInputDate getTxtAsysWet() {
        return txtAsysWet;
    }

    public void setTxtAsysCode(RichInputText txtAsysCode) {
        this.txtAsysCode = txtAsysCode;
    }

    public RichInputText getTxtAsysCode() {
        return txtAsysCode;
    }

    public void setTxtAsysOsdCode(RichInputText txtAsysOsdCode) {
        this.txtAsysOsdCode = txtAsysOsdCode;
    }

    public RichInputText getTxtAsysOsdCode() {
        return txtAsysOsdCode;
    }

    public void setTxtOsdName(RichInputText txtOsdName) {
        this.txtOsdName = txtOsdName;
    }

    public RichInputText getTxtOsdName() {
        return txtOsdName;
    }

    public void setBtnSaveUpdateAgencySystem(RichCommandButton btnSaveUpdateAgencySystem) {
        this.btnSaveUpdateAgencySystem = btnSaveUpdateAgencySystem;
    }

    public RichCommandButton getBtnSaveUpdateAgencySystem() {
        return btnSaveUpdateAgencySystem;
    }

    public String actionShowSubDivisionsLov() {
        ADFUtils.findIterator("fetchLowestOrgSubDivisionsBySystemIterator").executeQuery();
        GlobalCC.refreshUI(tblOrgSubDivisions);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:orgSubDivisionsPop" + "').show(hints);");
        return null;
    }

    public String actionSaveUpdateAgencySystem() {
        if (btnSaveUpdateAgencySystem.getText().equals("Edit")) {
            actionEditAgencySystem();
        } else {
            // Saving at this point is not allowed
        }
        return null;
    }

    public String actionEditAgencySystem() {
        String sysCode = GlobalCC.checkNullValues(txtAsysSysCode.getValue());
        String agnCode = GlobalCC.checkNullValues(txtAsysAgnCode.getValue());
        String wef = GlobalCC.checkNullValues(txtAsysWef.getValue());
        String wet = GlobalCC.checkNullValues(txtAsysWet.getValue());
        String comments = GlobalCC.checkNullValues(txtAsysComments.getValue());
        String osdCode = GlobalCC.checkNullValues(txtAsysOsdCode.getValue());
        String osdId = GlobalCC.checkNullValues(txtAsysOsdId.getValue());

        if (sysCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: System Code is Empty");
            return null;

        } else if (agnCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Agency Code is Empty");
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
                    "begin TQC_SETUPS_PKG.agencySystems_prc(?,?); end;";

                // Take care of all the date fields on the form.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date tmpWefDate = null;
                if (txtAsysWef.getValue() != null &&
                    !(txtAsysWef.getValue().equals(""))) {
                    String date1 = df.format(txtAsysWef.getValue());
                    tmpWefDate = df.parse(date1);
                }

                Date tmpWetDate = null;
                if (txtAsysWet.getValue() != null &&
                    !(txtAsysWet.getValue().equals(""))) {
                    String date1 = df.format(txtAsysWet.getValue());
                    tmpWetDate = df.parse(date1);
                }

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_AGENCY_SYSTEMS_TAB",
                                                     conn);
                ArrayList agencySystemsList = new ArrayList();
                AgencySystem agencySystem = new AgencySystem();
                agencySystem.setSQLTypeName("TQC_AGENCY_SYSTEMS_OBJ");

                agencySystem.setAsysSysCode(sysCode == null ? null :
                                            new BigDecimal(sysCode));
                agencySystem.setAsysAgnCode(agnCode == null ? null :
                                            new BigDecimal(agnCode));
                agencySystem.setAsysWef(tmpWefDate == null ? null :
                                        new java.sql.Date(tmpWefDate.getTime()));
                agencySystem.setAsysWet(tmpWetDate == null ? null :
                                        new java.sql.Date(tmpWetDate.getTime()));
                agencySystem.setAsysComment(comments);
                agencySystem.setAsysOsdCode(osdCode);
                agencySystem.setAsysOsdId(osdId == null ? null :
                                          new BigDecimal(osdId));
                agencySystemsList.add(agencySystem);
                ARRAY array =
                    new ARRAY(descriptor, conn, agencySystemsList.toArray());

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
                                     "pt1:agencySystemPop" +
                                     "').hide(hints);");

                ADFUtils.findIterator("fetchAllAgencyAssignedSystemsIterator").executeQuery();
                GlobalCC.refreshUI(tblAssignedAgencySystems);

                //clearDivLevelTypeFields();

                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void setTxtAsysComments(RichInputText txtAsysComments) {
        this.txtAsysComments = txtAsysComments;
    }

    public RichInputText getTxtAsysComments() {
        return txtAsysComments;
    }

    public void setTblOrgSubDivisions(RichTable tblOrgSubDivisions) {
        this.tblOrgSubDivisions = tblOrgSubDivisions;
    }

    public RichTable getTblOrgSubDivisions() {
        return tblOrgSubDivisions;
    }

    public String actionAcceptOrgSubDivision() {
        Object key2 = tblOrgSubDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAsysOsdCode.setValue(nodeBinding.getAttribute("osdCode"));
            txtAsysOsdId.setValue(nodeBinding.getAttribute("osdId"));
            txtOsdName.setValue(nodeBinding.getAttribute("osdName"));
        }

        GlobalCC.refreshUI(panelAgencySystem);

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:orgSubDivisionsPop" + "').hide(hints);");
        return null;
    }

    public void genAccYes(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void genAccNo(ValueChangeEvent evt) {


        if (evt.getNewValue() != evt.getOldValue() &&
            evt.getNewValue() != null) {


            if (genAcSelectYes.isSelected()) {
                txtAccShortDesc.setDisabled(true);
                txtAccShortDesc.setValue(null);
                GlobalCC.refreshUI(txtAccShortDesc);

            }

            else if (genAccNoSelect.isSelected()) {

                txtAccShortDesc.setDisabled(false);
                GlobalCC.refreshUI(txtAccShortDesc);
            }

        }
    }


    // Add event code here...


    public void setGenAcSelectYes(RichSelectBooleanRadio genAcSelectYes) {
        this.genAcSelectYes = genAcSelectYes;
    }

    public RichSelectBooleanRadio getGenAcSelectYes() {
        return genAcSelectYes;
    }

    public void setGenAccNoSelect(RichSelectBooleanRadio genAccNoSelect) {
        this.genAccNoSelect = genAccNoSelect;
    }

    public RichSelectBooleanRadio getGenAccNoSelect() {
        return genAccNoSelect;
    }

    public void setShortDescYesOrNoLayout(RichPanelGroupLayout shortDescYesOrNoLayout) {
        this.shortDescYesOrNoLayout = shortDescYesOrNoLayout;
    }

    public RichPanelGroupLayout getShortDescYesOrNoLayout() {
        return shortDescYesOrNoLayout;
    }

    public void setTbListAgencies(RichTable tbListAgencies) {
        this.tbListAgencies = tbListAgencies;
    }

    public RichTable getTbListAgencies() {
        return tbListAgencies;
    }


    public void setTxtStateCode(RichInputText txtStateCode) {
        this.txtStateCode = txtStateCode;
    }

    public RichInputText getTxtStateCode() {
        return txtStateCode;
    }

    public void setTxtStateName(RichInputText txtStateName) {
        this.txtStateName = txtStateName;
    }

    public RichInputText getTxtStateName() {
        return txtStateName;
    }

    public void setTblStates(RichTable tblStates) {
        this.tblStates = tblStates;
    }

    public RichTable getTblStates() {
        return tblStates;
    }

    public void setTblTownPop(RichTable tblTownPop) {
        this.tblTownPop = tblTownPop;
    }

    public RichTable getTblTownPop() {
        return tblTownPop;
    }

    public void setActionShowStates(RichCommandButton actionShowStates) {
        this.actionShowStates = actionShowStates;
    }

    public RichCommandButton getActionShowStates() {
        return actionShowStates;
    }

    public void setTbTownListing(RichTable tbTownListing) {
        this.tbTownListing = tbTownListing;
    }

    public RichTable getTbTownListing() {
        return tbTownListing;
    }

    public void setTxtAStateName(RichInputText txtAStateName) {
        this.txtAStateName = txtAStateName;
    }

    public RichInputText getTxtAStateName() {
        return txtAStateName;
    }

    public void setTxtAStateCode(RichInputText txtAStateCode) {
        this.txtAStateCode = txtAStateCode;
    }

    public RichInputText getTxtAStateCode() {
        return txtAStateCode;
    }

    public void actionConfirmDeleteAgentAccount(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = tblAgencyAccounts.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                String accCode = nodeBinding.getAttribute("code").toString();
                String accShtDesc =
                    nodeBinding.getAttribute("shortDesc").toString();
                String accName = nodeBinding.getAttribute("name").toString();
                String accAgencyCode =
                    nodeBinding.getAttribute("agentCode").toString();
                String accCreatedBy =
                    nodeBinding.getAttribute("createdBy").toString();
                String accDateCreated =
                    nodeBinding.getAttribute("dateCreated").toString();
                String accStatus =
                    nodeBinding.getAttribute("status").toString();
                String accRemarks = null;
                String accWef = null;
                String accWet = null;

                String Query =
                    "begin TQC_AGENCIES_PKG.create_agency_account(?,?,?,?,?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date newWefDate = new Date();
                    if (txtAccWef.getValue() != null &&
                        !(txtAccWef.getValue().equals(""))) {
                        String date1 = df.format(txtAccWef.getValue());
                        newWefDate = df.parse(date1);
                    }

                    Date newWetDate = null; //new Date();
                    if (txtAccWet.getValue() != null &&
                        !(txtAccWet.getValue().equals(""))) {
                        String date2 = df.format(txtAccWet.getValue());
                        newWetDate = df.parse(date2);
                    }

                    cst.setString(1, "D");
                    cst.setBigDecimal(2, new BigDecimal(accCode));
                    cst.setString(3, accShtDesc);
                    cst.setString(4, accName);
                    cst.setBigDecimal(5,
                                      new BigDecimal((String)session.getAttribute("agencyCode")));
                    cst.setString(6, (String)session.getAttribute("Username"));
                    cst.setDate(7,
                                new java.sql.Date(new java.util.Date().getTime()));
                    cst.setString(8, accStatus);
                    cst.setString(9, accRemarks);
                    cst.setDate(10, new java.sql.Date(newWefDate.getTime()));
                    cst.setDate(11,
                                newWetDate != null ? new java.sql.Date(newWetDate.getTime()) :
                                null);
                    cst.setBigDecimal(12, null);
                    cst.execute();

                    // Close the connections
                    cst.close();
                    conn.close();

                    String message = "Agency Account DELETED Successfully!";
                    //showMessagePopup(message);
                    GlobalCC.INFORMATIONREPORTING(message);

                    ADFUtils.findIterator("fetchAgencyAccountsIterator").executeQuery();
                    GlobalCC.refreshUI(tblAgencyAccounts);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                }
            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected.");

            }


        }
    }

    public void actionConfirmDeleteAgent(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = tblAgencyInfo.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                String agencyCode =
                    GlobalCC.checkNullValues(nodeBinding.getAttribute("code"));
                String accountTypeCode =
                    GlobalCC.checkNullValues(session.getAttribute("accountTypeCode"));

                if (agencyCode == null || agencyCode == "") {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Please select an agency to edit");
                } else {

                    String Query =
                        "begin tqc_setups_pkg.delete_agency_prc(?) ; end;";
                    DBConnector connector = new DBConnector();
                    OracleCallableStatement cst = null;
                    OracleConnection conn = null;

                    try {
                        conn =
(OracleConnection)connector.getDatabaseConnection();
                        cst = (OracleCallableStatement)conn.prepareCall(Query);

                        // Take care of all the date fields on the form. Make sure they are not null.
                        // If the dates are null, then default to the current date.

                        cst.setBigDecimal(1, new BigDecimal(agencyCode));

                        cst.execute();
                        cst.close();
                        conn.close();
                        ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
                        GlobalCC.refreshUI(tblAgencyInfo);

                        refreshAgencyDetailSection();
                        GlobalCC.refreshUI(mainPanelTab);
                        GlobalCC.INFORMATIONREPORTING("Agent DELETED Successfully!");


                    } catch (Exception e) {
                        GlobalCC.EXCEPTIONREPORTING(e);
                    }
                }

                // Reset the form

            } else {
                GlobalCC.INFORMATIONREPORTING("No Record Selected!");
            }
        }

    }

    public void setBtnDeleteAgency(RichCommandButton btnDeleteAgency) {
        this.btnDeleteAgency = btnDeleteAgency;
    }

    public RichCommandButton getBtnDeleteAgency() {
        return btnDeleteAgency;
    }

    public void setGenAccLabel(RichOutputLabel genAccLabel) {
        this.genAccLabel = genAccLabel;
    }

    public RichOutputLabel getGenAccLabel() {
        return genAccLabel;
    }

    public void setAgentAccountPop(RichPopup agentAccountPop) {
        this.agentAccountPop = agentAccountPop;
    }

    public RichPopup getAgentAccountPop() {
        return agentAccountPop;
    }

    /**
     * Deletes an Agency record from the database.
     *
     * @return null
     */
    public String actionConfirmDeleteAgency() {
        int pref;
        String agencyCode = GlobalCC.checkNullValues(txtAgencyCode.getValue());


        if (agencyCode == null || agencyCode == "") {
            GlobalCC.errorValueNotEntered("Error Value Missing: The record code is missing");
            return null;

        }
        String Query = "begin tqc_setups_pkg.delete_agency_prc(?) ; end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {
            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);
            cst.setBigDecimal(1, new BigDecimal(agencyCode));
            cst.execute();

            ADFUtils.findIterator("fetchAllAccountAgenciesIterator").executeQuery();
            GlobalCC.refreshUI(treeAgencies);

            cst.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }


        // Reset the form
        refreshAgencyDetailSection();

        String message = "Agent DELETED Successfully!";
        showMessagePopup(message);

        return null;
    }

    public void actionConfirmDeleteAgentReg(DialogEvent dialogEvent) {

        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            Object key2 = registrationTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                DBConnector connection = new DBConnector();
                String query =
                    "begin TQC_AGENCIES_PKG.TQC_AGENCY_REGISTRATION_PRC(?,?,?);end;";
                ARRAY array = null;
                OracleConnection conn = null;
                OracleCallableStatement stmt = null;

                AgentRegistration regbean = new AgentRegistration();
                LinkedList reglist = new LinkedList();
                regbean.setAREG_CODE(new BigDecimal((String)nodeBinding.getAttribute("id")));
                regbean.setAREG_ACCEPTED(null);
                regbean.setAREG_AGN_CODE(null);
                regbean.setAREG_REG_NO(null);
                regbean.setAREG_WEF(null);
                regbean.setAREG_WET(null);
                regbean.setAREG_YEAR(null);
                reglist.add(regbean);
                try {
                    conn =
(OracleConnection)connection.getDatabaseConnection();
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_AGENCY_REGISTRATION_TAB",
                                                         conn);
                    array =
                            new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                    stmt.setString(1, "D");
                    stmt.setArray(2, array);
                    stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                    stmt.execute();
                    String error = stmt.getString(3);


                    stmt.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("findAgentsRegistrationIterator").executeQuery();
                    GlobalCC.refreshUI(registrationTable);

                    String message = "Record DELETED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (SQLException e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }


            } else {
                GlobalCC.INFORMATIONREPORTING("No Record Selected");
            }
        }

    }

    public void actionConfirmDeleteReferee(DialogEvent dialogEvent) {

        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            Object key2 = agencyRefereeTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                DBConnector connection = new DBConnector();
                String query =
                    "begin TQC_AGENCIES_PKG.TQC_AGENCY_REFEREES_PRC(?,?,?);end;";
                ARRAY array = null;
                OracleConnection conn = null;
                OracleCallableStatement stmt = null;

                AgencyReferee regbean = new AgencyReferee();
                LinkedList reglist = new LinkedList();
                regbean.setAREF_CODE(new BigDecimal((String)nodeBinding.getAttribute("id")));
                reglist.add(regbean);
                try {
                    conn =
(OracleConnection)connection.getDatabaseConnection();
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_AGENCY_REFEREES_TAB",
                                                         conn);
                    array =
                            new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                    stmt.setString(1, "D");
                    stmt.setArray(2, array);
                    stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                    stmt.execute();


                    stmt.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("findAgentsRefereesIterator").executeQuery();
                    GlobalCC.refreshUI(agencyRefereeTable);

                    String message = "Record DELETED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
                }

            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected.");

            }
        }
    }

    public void actionConfirmDeleteAgentDirectors(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = agentDirectorsTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                DBConnector connection = new DBConnector();
                String query =
                    "begin TQC_AGENCIES_PKG.TQC_AGENCY_DIRECTORS_PRC(?,?,?);end;";
                ARRAY array = null;
                OracleConnection conn = null;
                OracleCallableStatement stmt = null;

                AgDirector regbean = new AgDirector();
                LinkedList reglist = new LinkedList();
                regbean.setADIR_CODE(new BigDecimal((String)nodeBinding.getAttribute("id")));
                regbean.setADIR_AGN_CODE(null);
                regbean.setADIR_DESIGNATION(null);
                regbean.setADIR_NAME(null);
                regbean.setADIR_PCT_HOLDG(null);
                regbean.setADIR_YEAR(null);
                regbean.setADIR_QUALIFICATIONS(null);
                regbean.setADIR_PHONE_NUMBER(null);
                reglist.add(regbean);
                try {
                    conn =
(OracleConnection)connection.getDatabaseConnection();
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_AGENCY_DIRECTORS_TAB",
                                                         conn);
                    array =
                            new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                    stmt.setString(1, "D");
                    stmt.setArray(2, array);
                    stmt.registerOutParameter(3, OracleTypes.VARCHAR);
                    stmt.execute();
                    String error = stmt.getString(3);


                    stmt.close();
                    conn.commit();
                    conn.close();

                    ADFUtils.findIterator("findAgentsDirectorsIterator").executeQuery();
                    GlobalCC.refreshUI(agentDirectorsTable);

                    String message = "Record DELETED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (SQLException e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            } else {
                GlobalCC.INFORMATIONREPORTING("No Record Selected.");

            }


        }
    }

    public void actionConfirmDeleteAgentWebAcc(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = webUsersTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {

                DBConnector connection = new DBConnector();
                String query =
                    "begin TQC_AGENCIES_PKG.TQC_ACCOUNT_CONTACTS_PRC(?,?,?,?,?);end;";
                ARRAY array = null;
                OracleConnection conn = null;
                OracleCallableStatement stmt = null;
                String currentUser = (String)session.getAttribute("Username");

                BigDecimal id =
                    GlobalCC.checkBDNullValues(nodeBinding.getAttribute("id"));
                WebUser regbean = new WebUser();
                LinkedList reglist = new LinkedList();
                regbean.setACCC_CODE(id);
                reglist.add(regbean);
                try {
                    conn =
(OracleConnection)connection.getDatabaseConnection();
                    stmt = (OracleCallableStatement)conn.prepareCall(query);
                    ArrayDescriptor descriptor =
                        ArrayDescriptor.createDescriptor("TQC_ACCOUNT_CONTACTS_TAB",
                                                         conn);
                    array =
                            new ARRAY(descriptor, conn, (Object[])reglist.toArray());
                    stmt.setString(1, "D");
                    stmt.setArray(2, array);
                    stmt.setString(3, currentUser);
                    stmt.registerOutParameter(4, OracleTypes.VARCHAR);
                    stmt.setString(5, null);
                    stmt.execute();
                    String error = stmt.getString(4);

                    conn.commit();
                    stmt.close();
                    conn.close();

                    ADFUtils.findIterator("findwebUserAccountsIterator").executeQuery();
                    GlobalCC.refreshUI(webUsersTable);

                    String message = "Account Successfully Deactivated!";
                    GlobalCC.INFORMATIONREPORTING(message);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }

            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected.");

            }
        }
    }


    public void setTblAdminRegions(RichTable tblAdminRegions) {
        this.tblAdminRegions = tblAdminRegions;
    }

    public RichTable getTblAdminRegions() {
        return tblAdminRegions;
    }

    public void setPnmsgAdminRegionName(RichPanelLabelAndMessage pnmsgAdminRegionName) {
        this.pnmsgAdminRegionName = pnmsgAdminRegionName;
    }

    public RichPanelLabelAndMessage getPnmsgAdminRegionName() {
        return pnmsgAdminRegionName;
    }

    public void setPnmsgTownName(RichPanelLabelAndMessage pnmsgTownName) {
        this.pnmsgTownName = pnmsgTownName;
    }

    public RichPanelLabelAndMessage getPnmsgTownName() {
        return pnmsgTownName;
    }

    public void setFrmLoadAdminTownDetails(RichPanelFormLayout frmLoadAdminTownDetails) {
        this.frmLoadAdminTownDetails = frmLoadAdminTownDetails;
    }

    public RichPanelFormLayout getFrmLoadAdminTownDetails() {
        return frmLoadAdminTownDetails;
    }

    public void setDlgAdminRegionList(RichDialog dlgAdminRegionList) {
        this.dlgAdminRegionList = dlgAdminRegionList;
    }

    public RichDialog getDlgAdminRegionList() {
        return dlgAdminRegionList;
    }

    public void setTxtBankBranch(RichInputText txtBankBranch) {
        this.txtBankBranch = txtBankBranch;
    }

    public RichInputText getTxtBankBranch() {
        return txtBankBranch;
    }

    public void setTxtBankName(RichInputText txtBankName) {
        this.txtBankName = txtBankName;
    }

    public RichInputText getTxtBankName() {
        return txtBankName;
    }

    public void setTxtBankCode(RichInputText txtBankCode) {
        this.txtBankCode = txtBankCode;
    }

    public RichInputText getTxtBankCode() {
        return txtBankCode;
    }

    public void setTxtBankBranchCode(RichInputText txtBankBranchCode) {
        this.txtBankBranchCode = txtBankBranchCode;
    }

    public RichInputText getTxtBankBranchCode() {
        return txtBankBranchCode;
    }

    public void setTblBanks(RichTable tblBanks) {
        this.tblBanks = tblBanks;
    }

    public RichTable getTblBanks() {
        return tblBanks;
    }

    public String actionAcceptBank() {
        Object key = tblBanks.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtBankCode.setValue(nodeBinding.getAttribute("bankCode"));
            txtBankName.setValue(nodeBinding.getAttribute("bankName"));
            txtBankBranchCode.setValue(null);
            txtBankBranch.setValue(null);
            GlobalCC.refreshUI(tblBankBranches);
            GlobalCC.refreshUI(txtBankName);
            GlobalCC.refreshUI(txtBankCode);
            GlobalCC.refreshUI(txtBankBranch);
        }

        GlobalCC.dismissPopUp("pt1", "BanksPop");
        return null;
    }

    public String actionAcceptBankBranch() {


        Object key = tblBankBranches.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtBankBranchCode.setValue(nodeBinding.getAttribute("branchCode"));
            txtBankBranch.setValue(nodeBinding.getAttribute("branchName"));
            GlobalCC.refreshUI(txtBankBranchCode);
            GlobalCC.refreshUI(txtBankBranch);
        }
        GlobalCC.dismissPopUp("pt1", "BankBranchesPOP");

        return null;
    }

    public void setTblBankBranches(RichTable tblBankBranches) {
        this.tblBankBranches = tblBankBranches;
    }

    public RichTable getTblBankBranches() {
        return tblBankBranches;
    }

    public void setTxtAccountNo(RichInputText txtAccountNo) {
        this.txtAccountNo = txtAccountNo;
    }

    public RichInputText getTxtAccountNo() {
        return txtAccountNo;
    }

    public String actionShowBankBranches() {
        if (txtBankCode.getValue() == null) {
            GlobalCC.EXCEPTIONREPORTING("You have not Selected a Bank");
            return null;
        }
        session.setAttribute("bankCode", txtBankCode.getValue());
        ADFUtils.findIterator("fetchBankBranchByBankCodeIterator").executeQuery();
        GlobalCC.showPopUp("pt1", "BankBranchesPOP");
        return null;
    }

    public void setTxtPrefix(RichInputText txtPrefix) {
        this.txtPrefix = txtPrefix;
    }

    public RichInputText getTxtPrefix() {
        return txtPrefix;
    }

    public void setTblAssignedClients(RichTable tblAssignedClients) {
        this.tblAssignedClients = tblAssignedClients;
    }

    public RichTable getTblAssignedClients() {
        return tblAssignedClients;
    }

    public void setTblUnAssignedClients(RichTable tblUnAssignedClients) {
        this.tblUnAssignedClients = tblUnAssignedClients;
    }

    public RichTable getTblUnAssignedClients() {
        return tblUnAssignedClients;
    }

    public void setChBoxUnAssigedClients(RichSelectBooleanCheckbox chBoxUnAssigedClients) {
        this.chBoxUnAssigedClients = chBoxUnAssigedClients;
    }

    public RichSelectBooleanCheckbox getChBoxUnAssigedClients() {
        return chBoxUnAssigedClients;
    }

    public void setChBoxAssigedClients(RichSelectBooleanCheckbox chBoxAssigedClients) {
        this.chBoxAssigedClients = chBoxAssigedClients;
    }

    public RichSelectBooleanCheckbox getChBoxAssigedClients() {
        return chBoxAssigedClients;
    }

    public String actionRemoveClient() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_AGENCIES_PKG.create_agency_client(?,?,?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);
            int rowcount = tblAssignedClients.getRowCount();
            Object ClientCode = new Object();

            for (int i = 0; i < rowcount; i++) {
                tblAssignedClients.setRowIndex(i);
                Object key = tblAssignedClients.getRowKey();
                tblAssignedClients.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)tblAssignedClients.getRowData();
                if (chBoxAssigedClients.isSelected()) {

                    ClientCode = nodeBinding.getAttribute("clientCode");


                    statement.setString(1, null);
                    statement.setObject(2, txtAgencyCode.getValue());
                    statement.setObject(3, ClientCode);
                    statement.setString(4, "D");
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchAgencyNoneClientsIterator").executeQuery();
            GlobalCC.refreshUI(tblUnAssignedClients);
            ADFUtils.findIterator("fetchAgencyClientsIterator").executeQuery();
            GlobalCC.refreshUI(tblAssignedClients);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String actionRemoveServiceProviders() {

        int rowcount = asssignedServiceProviders.getRowCount();
        Object ServiceproviderCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_AGENCIES_PKG.create_agency_serv_prov(?,?,?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 0; i < rowcount; i++) {
                asssignedServiceProviders.setRowIndex(i);
                Object key = asssignedServiceProviders.getRowKey();
                asssignedServiceProviders.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)asssignedServiceProviders.getRowData();
                if (cbBoxassignedServiceProviders.isSelected()) {

                    ServiceproviderCode = nodeBinding.getAttribute("SPR_CODE");


                    statement.setString(1, null);
                    statement.setObject(2, ServiceproviderCode);
                    statement.setObject(3, txtAgencyCode.getValue());
                    System.out.println("SPR_CODE" + ServiceproviderCode);
                    statement.setString(4, "D");
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findServiceprovidersIterator").executeQuery();
            GlobalCC.refreshUI(unAssignedServiceProviders);
            ADFUtils.findIterator("fetchAgencyServiceProvidersIterator").executeQuery();
            GlobalCC.refreshUI(asssignedServiceProviders);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String actionAddClient() {

        int rowcount = tblUnAssignedClients.getRowCount();
        Object ClientCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_AGENCIES_PKG.create_agency_client(?,?,?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 0; i < rowcount; i++) {
                tblUnAssignedClients.setRowIndex(i);
                Object key = tblUnAssignedClients.getRowKey();
                tblUnAssignedClients.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)tblUnAssignedClients.getRowData();
                if (chBoxUnAssigedClients.isSelected()) {

                    ClientCode = nodeBinding.getAttribute("clientCode");


                    statement.setString(1, null);
                    statement.setObject(2, txtAgencyCode.getValue());
                    statement.setObject(3, ClientCode);
                    System.out.println("ClientCode" + ClientCode);
                    statement.setString(4, "A");
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchAgencyNoneClientsIterator").executeQuery();
            GlobalCC.refreshUI(tblUnAssignedClients);
            ADFUtils.findIterator("fetchAgencyClientsIterator").executeQuery();
            GlobalCC.refreshUI(tblAssignedClients);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String actionAddAgentProduct() {

        int rowcount = unAssignedProductsTbl.getRowCount();
        Object productCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_AGENCIES_PKG.add_agency_product(?,?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 0; i < rowcount; i++) {
                unAssignedProductsTbl.setRowIndex(i);
                Object key = unAssignedProductsTbl.getRowKey();
                unAssignedProductsTbl.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)unAssignedProductsTbl.getRowData();
                if (chBoxAssigedClients.isSelected()) {

                    productCode = nodeBinding.getAttribute("prodCode");

                    statement.setObject(1, txtAgencyCode.getValue());
                    statement.setObject(2, productCode);
                    System.out.println("productCode" + productCode);
                    statement.setString(3, "A");
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchNoneAgencyProductsIterator").executeQuery();
            GlobalCC.refreshUI(unAssignedProductsTbl);
            ADFUtils.findIterator("fetchAgencyProductsIterator").executeQuery();
            GlobalCC.refreshUI(assignedProductsTbl);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String actionRemoveAgentProduct() {

        int rowcount = assignedProductsTbl.getRowCount();
        Object productCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_AGENCIES_PKG.add_agency_product(?,?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 0; i < rowcount; i++) {
                assignedProductsTbl.setRowIndex(i);
                Object key = assignedProductsTbl.getRowKey();
                assignedProductsTbl.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)assignedProductsTbl.getRowData();
                if (chkUnAssignedProducts.isSelected()) {

                    productCode = nodeBinding.getAttribute("prodCode");

                    statement.setObject(1, txtAgencyCode.getValue());
                    statement.setObject(2, productCode);
                    System.out.println("productCode" + productCode);
                    statement.setString(3, "D");
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("fetchNoneAgencyProductsIterator").executeQuery();
            GlobalCC.refreshUI(unAssignedProductsTbl);
            ADFUtils.findIterator("fetchAgencyProductsIterator").executeQuery();
            GlobalCC.refreshUI(assignedProductsTbl);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public String actionAddServiceProviders() {

        int rowcount = unAssignedServiceProviders.getRowCount();
        Object ServiceproviderCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_AGENCIES_PKG.create_agency_serv_prov(?,?,?,?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);

            for (int i = 0; i < rowcount; i++) {
                unAssignedServiceProviders.setRowIndex(i);
                Object key = unAssignedServiceProviders.getRowKey();
                unAssignedServiceProviders.setRowKey(key);
                JUCtrlValueBinding nodeBinding =
                    (JUCtrlValueBinding)unAssignedServiceProviders.getRowData();
                if (chAssignServiceprovs.isSelected()) {

                    ServiceproviderCode = nodeBinding.getAttribute("spr_code");


                    statement.setString(1, null);
                    statement.setObject(2, ServiceproviderCode);
                    statement.setObject(3, txtAgencyCode.getValue());
                    System.out.println("ServiceproviderCode" +
                                       ServiceproviderCode);
                    statement.setString(4, "A");
                    statement.execute();
                }
            }
            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findServiceprovidersIterator").executeQuery();
            GlobalCC.refreshUI(unAssignedServiceProviders);
            ADFUtils.findIterator("fetchAgencyServiceProvidersIterator").executeQuery();
            GlobalCC.refreshUI(asssignedServiceProviders);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String actionAddServiceProvidersDef() {

        int rowcount = unAssignedServiceProviders.getRowCount();
        Object ServiceproviderCode = new Object();

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_AGENCIES_PKG.create_agency_serv_prov_def(?); end;";
            statement = (OracleCallableStatement)conn.prepareCall(query);


            statement.setObject(1, txtAgencyCode.getValue());

            statement.execute();

            statement.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findServiceprovidersIterator").executeQuery();
            GlobalCC.refreshUI(unAssignedServiceProviders);
            ADFUtils.findIterator("fetchAgencyServiceProvidersIterator").executeQuery();
            GlobalCC.refreshUI(asssignedServiceProviders);
        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }


        return null;
    }

    public String actionAcceptPersonnel() {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("fetchAllPersonnelsIterator");
        RowKeySet set = tblPersonnel.getSelectedRowKeys();
        Iterator rowKeySetIter = set.iterator();

        while (rowKeySetIter.hasNext()) {
            List l = (List)rowKeySetIter.next();
            Key key = (Key)l.get(0);
            dciter.setCurrentRowWithKey(key.toStringFormat(true));

            Row r = dciter.getCurrentRow();
            txtAgencyContractCode.setValue(r.getAttribute("conCode"));
            GlobalCC.refreshUI(txtAgencyContractCode);


        }

        // Open the popup dialog
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:personnelPop').hide(hints);");
        return null;
    }

    public void setTblPersonnel(RichTable tblPersonnel) {
        this.tblPersonnel = tblPersonnel;
    }

    public RichTable getTblPersonnel() {
        return tblPersonnel;
    }

    public String showPersPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:personnelPop').show(hints);");
        return null;
    }

    public void setTxtAsysOsdId(RichInputNumberSpinbox txtAsysOsdId) {
        this.txtAsysOsdId = txtAsysOsdId;
    }

    public RichInputNumberSpinbox getTxtAsysOsdId() {
        return txtAsysOsdId;
    }

    public void setTblAccountTypesPopEdit(RichTable tblAccountTypesPopEdit) {
        this.tblAccountTypesPopEdit = tblAccountTypesPopEdit;
    }

    public RichTable getTblAccountTypesPopEdit() {
        return tblAccountTypesPopEdit;
    }

    public String actionLaunchEditActType() {
        if (session.getAttribute("agencyCode") == null) {
            GlobalCC.EXCEPTIONREPORTING("Select a agency first.");
            return null;
        }
        GlobalCC.showPopUp("pt1", "accountTypeEdit");
        return null;
    }

    public void setAccountsTypeElements(List<SelectItem> accountsTypeElements) {
        this.accountsTypeElements = accountsTypeElements;
    }

    public void setTxtCreditRting(RichSelectOneChoice txtCreditRting) {
        this.txtCreditRting = txtCreditRting;
    }

    public RichSelectOneChoice getTxtCreditRting() {
        return txtCreditRting;
    }

    public String addBranchesDetails() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:addBranchBankDetails" + "').show(hints);");
        directYr.setValue(null);
        directname.setValue(null);
        directQualifications.setValue(null);
        directshare.setValue(null);
        saveAgDirectorButton.setText("Save");
        return null;
    }

    public void setUnAssignedServiceProviders(RichTable unAssignedServiceProviders) {
        this.unAssignedServiceProviders = unAssignedServiceProviders;
    }

    public RichTable getUnAssignedServiceProviders() {
        return unAssignedServiceProviders;
    }

    public void setChAssignServiceprovs(RichSelectBooleanCheckbox chAssignServiceprovs) {
        this.chAssignServiceprovs = chAssignServiceprovs;
    }

    public RichSelectBooleanCheckbox getChAssignServiceprovs() {
        return chAssignServiceprovs;
    }

    public void setSelected(RichTable selected) {
        this.selected = selected;
    }

    public RichTable getSelected() {
        return selected;
    }

    public void setAsssignedServiceProviders(RichTable asssignedServiceProviders) {
        this.asssignedServiceProviders = asssignedServiceProviders;
    }

    public RichTable getAsssignedServiceProviders() {
        return asssignedServiceProviders;
    }

    public void setCbBoxassignedServiceProviders(RichSelectBooleanCheckbox cbBoxassignedServiceProviders) {
        this.cbBoxassignedServiceProviders = cbBoxassignedServiceProviders;
    }

    public RichSelectBooleanCheckbox getCbBoxassignedServiceProviders() {
        return cbBoxassignedServiceProviders;
    }

    public String selectAgent() {
        Object key = agentTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
        }
        txtAgent.setValue(r.getAttribute("agentname"));
        GlobalCC.refreshUI(txtAgent);
        session.setAttribute("agentCodeType", r.getAttribute("agentCode"));
        return null;
    }

    public void setAgentTbl(RichTable agentTbl) {
        this.agentTbl = agentTbl;
    }

    public RichTable getAgentTbl() {
        return agentTbl;
    }

    public void setTxtAgent(RichInputText txtAgent) {
        this.txtAgent = txtAgent;
    }

    public RichInputText getTxtAgent() {
        return txtAgent;
    }

    public void setTxtAgntLov(RichCommandButton txtAgntLov) {
        this.txtAgntLov = txtAgntLov;
    }

    public RichCommandButton getTxtAgntLov() {
        return txtAgntLov;
    }

    public void setSubAgntDetailsTab(RichShowDetailItem subAgntDetailsTab) {
        this.subAgntDetailsTab = subAgntDetailsTab;
    }

    public RichShowDetailItem getSubAgntDetailsTab() {
        return subAgntDetailsTab;
    }

    public void setAgentsTbl(RichTable agentsTbl) {
        this.agentsTbl = agentsTbl;
    }

    public RichTable getAgentsTbl() {
        return agentsTbl;
    }

    public void selectAgentTbl(DialogEvent dialogEvent) {
        Object key = agentTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
        }
        System.out.println("This is executed");
        txtAgent.setValue(r.getAttribute("agentShtDesc"));
        GlobalCC.refreshUI(txtAgent);
        session.setAttribute("agentCodeType", r.getAttribute("agentCode"));
        System.out.println("This is executed 1");
    }

    public void setTelNo(RichInputText telNo) {
        this.telNo = telNo;
    }

    public RichInputText getTelNo() {
        return telNo;
    }

    public void setTxtAgentsAccountsLabel(RichPanelLabelAndMessage txtAgentsAccountsLabel) {
        this.txtAgentsAccountsLabel = txtAgentsAccountsLabel;
    }

    public RichPanelLabelAndMessage getTxtAgentsAccountsLabel() {
        return txtAgentsAccountsLabel;
    }

    public void setTxtSaccoName(RichInputText txtSaccoName) {
        this.txtSaccoName = txtSaccoName;
    }

    public RichInputText getTxtSaccoName() {
        return txtSaccoName;
    }

    public String selectSacco() {
        Object key = txtSaccoTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
        }
        txtSacco.setValue(r.getAttribute("CLNT_NAME"));
        GlobalCC.refreshUI(txtSacco);
        session.setAttribute("CLCode", r.getAttribute("CLNT_CODE"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:saccoPop').hide(hints);");
        return null;
    }

    public void setTxtSacco(RichInputText txtSacco) {
        this.txtSacco = txtSacco;
    }

    public RichInputText getTxtSacco() {
        return txtSacco;
    }

    public void setTxtSaccoTbl(RichTable txtSaccoTbl) {
        this.txtSaccoTbl = txtSaccoTbl;
    }

    public RichTable getTxtSaccoTbl() {
        return txtSaccoTbl;
    }

    public void setTxtMarketer(RichInputText txtMarketer) {
        this.txtMarketer = txtMarketer;
    }

    public RichInputText getTxtMarketer() {
        return txtMarketer;
    }

    public String selectMarketer() {
        Object key = marketerTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a marketer");
            return null;
        }
        session.setAttribute("accountManagerCode",
                             r.getAttribute("agentCode"));
        txtMarketer.setValue(r.getAttribute("agentName"));
        GlobalCC.refreshUI(txtMarketer);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:MarketerPop').hide(hints);");
        return null;
    }

    public void setMarketerTbl(RichTable marketerTbl) {
        this.marketerTbl = marketerTbl;
    }

    public RichTable getMarketerTbl() {
        return marketerTbl;
    }

    public void setTxtBranches(RichInputText txtBranches) {
        this.txtBranches = txtBranches;
    }

    public RichInputText getTxtBranches() {
        return txtBranches;
    }

    public void setTxtSystems(RichTable txtSystems) {
        this.txtSystems = txtSystems;
    }

    public RichTable getTxtSystems() {
        return txtSystems;
    }

    public String saveSystem() {
        Object keys = txtSystems.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("sysCode", r.getAttribute("sysCode"));
        txtBranches.setValue(r.getAttribute("sysName"));
        GlobalCC.refreshUI(txtBranches);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:webUserPop" + "').show(hints);");
        return null;
    }

    public void setUnitManagerTbl(RichTable unitManagerTbl) {
        this.unitManagerTbl = unitManagerTbl;
    }

    public RichTable getUnitManagerTbl() {
        return unitManagerTbl;
    }

    public String selectBranch() {
        Object keys = unitManagerTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select A Branch");
            return null;
        }
        session.setAttribute("braCode", r.getAttribute("branchAgnCode"));
        session.setAttribute("brnCode", r.getAttribute("brnCode"));
        session.setAttribute("agentCode", r.getAttribute("agentCode"));
        txtUnitManager.setValue(r.getAttribute("branchName"));
        GlobalCC.refreshUI(txtUnitManager);
        return null;
    }

    public void setTxtUnitManager(RichInputText txtUnitManager) {
        this.txtUnitManager = txtUnitManager;
    }

    public RichInputText getTxtUnitManager() {
        return txtUnitManager;
    }

    public String PromoteManager() {
        // Add event code here...
        return null;
    }

    public void setPbPromManager(RichPanelBox pbPromManager) {
        this.pbPromManager = pbPromManager;
    }

    public RichPanelBox getPbPromManager() {
        return pbPromManager;
    }

    public void txtAgentPromotion(ValueChangeEvent valueChangeEvent) {
        sltTransType.setValue(null);
        sltTransTypeDem.setValue(null);
        txtBranchName.setValue(null);
        txtUnitManager.setValue(null);
        txtEffectiveDate.setValue(null);
        if (txtAgentChoices.getValue().equals("AGP")) {
            clbPromote.setVisible(true);
            // clbDemote.setVisible(true);
            txtEffectiveDate.setVisible(true);
            txtBranchName.setVisible(true);
            sltTransType.setVisible(true);
            lblBranch.setVisible(true);
            clbBranchDrop.setVisible(true);
            sltTransTypeDem.setVisible(false);
            lblAgencyBranch.setVisible(false);
            txtUnitManager.setVisible(false);
            clbUnitManager.setVisible(false);
            clbDemoteCmb.setVisible(false);
            // clbAuthDemotion.setVisible(false);
            txtTransactionType.setVisible(true);
            lblEffectiveDate.setVisible(true);

            lblAgencyPrefix.setVisible(true);

            txtAgencyPrefix.setVisible(true);
            lblUnitPrefix.setVisible(true);
            txtUnitPrefix.setVisible(true);
            lblAgencySeq.setVisible(true);
            txtAgencySeq.setVisible(true);
            txtPrecontractCode.setVisible(true);
            precontractCode.setVisible(true);
        } else if (txtAgentChoices.getValue().equals("AGD")) {
            System.out.println("Entered here");
            clbPromote.setVisible(false);
            //  clbDemote.setVisible(false);
            txtEffectiveDate.setVisible(true);
            txtBranchName.setVisible(false);
            sltTransType.setVisible(false);
            lblBranch.setVisible(false);
            clbBranchDrop.setVisible(false);

            sltTransTypeDem.setVisible(true);
            lblAgencyBranch.setVisible(true);
            txtUnitManager.setVisible(true);
            clbUnitManager.setVisible(true);
            clbDemoteCmb.setVisible(true);
            //  clbAuthDemotion.setVisible(true);
            txtTransactionType.setVisible(true);
            lblEffectiveDate.setVisible(true);

            lblAgencyPrefix.setVisible(false);
            txtAgencyPrefix.setVisible(false);
            lblUnitPrefix.setVisible(true);
            txtUnitPrefix.setVisible(true);
            lblAgencySeq.setVisible(false);
            txtAgencySeq.setVisible(false);
            txtPrecontractCode.setVisible(true);
            precontractCode.setVisible(true);
        }
        GlobalCC.refreshUI(clbPromote);
        GlobalCC.refreshUI(clbDemote);
        GlobalCC.refreshUI(txtEffectiveDate);
        GlobalCC.refreshUI(txtBranchName);
        GlobalCC.refreshUI(sltTransType);
        GlobalCC.refreshUI(lblBranch);
        GlobalCC.refreshUI(clbBranchDrop);
        GlobalCC.refreshUI(sltTransTypeDem);
        GlobalCC.refreshUI(lblAgencyBranch);
        GlobalCC.refreshUI(txtUnitManager);
        GlobalCC.refreshUI(clbUnitManager);
        GlobalCC.refreshUI(clbDemoteCmb);
        GlobalCC.refreshUI(clbAuthDemotion);
        GlobalCC.refreshUI(txtTransactionType);
        GlobalCC.refreshUI(lblEffectiveDate);

        GlobalCC.refreshUI(lblAgencyPrefix);
        GlobalCC.refreshUI(txtAgencySeq);
        GlobalCC.refreshUI(clbUnitManager);
        GlobalCC.refreshUI(lblUnitPrefix);
        GlobalCC.refreshUI(txtUnitPrefix);
        GlobalCC.refreshUI(lblAgencySeq);
        GlobalCC.refreshUI(txtAgencySeq);
        GlobalCC.refreshUI(txtAgencyPrefix);
        GlobalCC.refreshUI(txtPrecontractCode);
        GlobalCC.refreshUI(precontractCode);

    }

    public void setTxtPromote(RichSelectBooleanRadio txtPromote) {
        this.txtPromote = txtPromote;
    }

    public RichSelectBooleanRadio getTxtPromote() {
        return txtPromote;
    }

    public void setTxtAgentChoices(RichSelectOneRadio txtAgentChoices) {
        this.txtAgentChoices = txtAgentChoices;
    }

    public RichSelectOneRadio getTxtAgentChoices() {
        return txtAgentChoices;
    }

    public void setBranchTbl(RichTable branchTbl) {
        this.branchTbl = branchTbl;
    }

    public RichTable getBranchTbl() {
        return branchTbl;
    }

    public String selectBranchDetails() {
        Object key = branchTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a Branch");
            return null;
        }
        session.setAttribute("brnCode", r.getAttribute("brnCode"));
        txtBranchName.setValue(r.getAttribute("branchName"));
        GlobalCC.refreshUI(txtBranchName);
        return null;
    }

    public void setTxtBranchName(RichInputText txtBranchName) {
        this.txtBranchName = txtBranchName;
    }

    public RichInputText getTxtBranchName() {
        return txtBranchName;
    }

    public void setTxtEffectiveDate(RichInputDate txtEffectiveDate) {
        this.txtEffectiveDate = txtEffectiveDate;
    }

    public RichInputDate getTxtEffectiveDate() {
        return txtEffectiveDate;
    }

    public void setClbPromote(RichCommandButton clbPromote) {
        this.clbPromote = clbPromote;
    }

    public RichCommandButton getClbPromote() {
        return clbPromote;
    }

    public void setClbDemote(RichCommandButton clbDemote) {
        this.clbDemote = clbDemote;
    }

    public RichCommandButton getClbDemote() {
        return clbDemote;
    }

    public void setLblBranch(RichOutputLabel lblBranch) {
        this.lblBranch = lblBranch;
    }

    public RichOutputLabel getLblBranch() {
        return lblBranch;
    }

    public void setClbBranchDrop(RichCommandButton clbBranchDrop) {
        this.clbBranchDrop = clbBranchDrop;
    }

    public RichCommandButton getClbBranchDrop() {
        return clbBranchDrop;
    }

    public void setTxtOneChoiceDem(RichSelectOneChoice sltTransTypeDem) {
        this.sltTransTypeDem = sltTransTypeDem;
    }

    public RichSelectOneChoice getTxtOneChoiceDem() {
        return sltTransTypeDem;
    }

    public void setLblAgencyBranch(RichOutputLabel lblAgencyBranch) {
        this.lblAgencyBranch = lblAgencyBranch;
    }

    public RichOutputLabel getLblAgencyBranch() {
        return lblAgencyBranch;
    }

    public void setClbUnitManager(RichCommandButton clbUnitManager) {
        this.clbUnitManager = clbUnitManager;
    }

    public RichCommandButton getClbUnitManager() {
        return clbUnitManager;
    }

    public void setClbDemoteCmb(RichCommandButton clbDemoteCmb) {
        this.clbDemoteCmb = clbDemoteCmb;
    }

    public RichCommandButton getClbDemoteCmb() {
        return clbDemoteCmb;
    }

    public void setClbAuthDemotion(RichCommandButton clbAuthDemotion) {
        this.clbAuthDemotion = clbAuthDemotion;
    }

    public RichCommandButton getClbAuthDemotion() {
        return clbAuthDemotion;
    }

    public void setTxtTransactionType(RichOutputLabel txtTransactionType) {
        this.txtTransactionType = txtTransactionType;
    }

    public RichOutputLabel getTxtTransactionType() {
        return txtTransactionType;
    }


    public String promoteAgent() {
        BigDecimal agencyCode = null;
        String transType;
        BigDecimal brnCode;
        String promType;
        BigDecimal braCode;
        BigDecimal agencyPref;
        BigDecimal unitPref;
        BigDecimal agencySeq;
        BigDecimal precontractCode;
        if (txtAgencyPrefix.getValue() != null) {
            if (txtAgencyPrefix.getValue().toString().length() > 3) {
                GlobalCC.INFORMATIONREPORTING("The agency prefix cannot have more than 3 characters");
                return null;
            } else {
                agencyPref =
                        new BigDecimal(txtAgencyPrefix.getValue().toString());
            }

        } else {
            agencyPref = null;
        }
        if (txtUnitPrefix.getValue() != null) {
            if (txtUnitPrefix.getValue().toString().length() > 2) {
                GlobalCC.INFORMATIONREPORTING("The unit Prefix cannot have more than two characters");
                return null;
            } else {
                unitPref = new BigDecimal(txtUnitPrefix.getValue().toString());

            }

        } else {
            unitPref = null;

        }
        if (txtAgencySeq.getValue() != null) {
            agencySeq = new BigDecimal(txtAgencySeq.getValue().toString());
        } else {
            agencySeq = null;
        }
        if (session.getAttribute("brnCode") != null) {
            brnCode =
                    new BigDecimal(session.getAttribute("brnCode").toString());
        } else {
            brnCode = null;
        }
        if (session.getAttribute("agencyCode") != null) {
            agencyCode =
                    new BigDecimal(session.getAttribute("agencyCode").toString());
        } else {
            agencyCode = null;
        }
        if (txtAgentChoices.getValue() != null) {
            transType = txtAgentChoices.getValue().toString();
        } else {
            transType = null;
        }
        if (sltTransType.getValue() != null) {
            promType = sltTransType.getValue().toString();
        } else {
            promType = null;
        }
        if (session.getAttribute("braCode") != null) {
            braCode =
                    new BigDecimal(session.getAttribute("braCode").toString());
        } else {
            braCode = null;
        }
        if (txtPrecontractCode.getValue() != null) {
            precontractCode =
                    new BigDecimal(txtPrecontractCode.getValue().toString());
        } else {
            precontractCode = null;
        }
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = (OracleConnection)connector.getDatabaseConnection();
            String query =
                "begin LMS_WEB_PKG_UND.agentPromoDemotion(?,?,?,?,?,?,?,?,?,?,?); end;";
            OracleCallableStatement statement = null;
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1, agencyCode);
            statement.setBigDecimal(2, brnCode);
            statement.setString(3, transType);
            statement.setString(4, promType);
            statement.setDate(5, GlobalCC.extractDate(txtEffectiveDate));
            statement.setString(6,
                                session.getAttribute("Username").toString());
            statement.setBigDecimal(7, braCode);
            statement.setBigDecimal(8, agencyPref);
            statement.setBigDecimal(9, unitPref);
            statement.setBigDecimal(10, agencySeq);
            statement.setBigDecimal(11, precontractCode);
            statement.execute();
            connection.close();
            GlobalCC.INFORMATIONREPORTING("Promotion Successfull");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        clbDemote.setVisible(true);
        clbAuthDemotion.setVisible(false);
        GlobalCC.refreshUI(clbDemote);
        GlobalCC.refreshUI(clbAuthDemotion);
        return null;
    }

    public String demoteAgent() {
        BigDecimal agencyCode = null;
        String transType;
        BigDecimal brnCode;
        String promType;
        BigDecimal braCode;
        BigDecimal unitPref;
        BigDecimal precontractCode;
        if (txtPrecontractCode.getValue() != null) {
            precontractCode =
                    new BigDecimal(txtPrecontractCode.getValue().toString());
        } else {
            precontractCode = null;
        }
        if (txtUnitPrefix.getValue() != null) {
            if (txtUnitPrefix.getValue().toString().length() > 2) {
                GlobalCC.INFORMATIONREPORTING("The unit Prefix cannot have more than two characters");
                return null;
            } else {
                unitPref = new BigDecimal(txtUnitPrefix.getValue().toString());

            }

        } else {
            unitPref = null;

        }
        if (session.getAttribute("brnCode") != null) {
            brnCode =
                    new BigDecimal(session.getAttribute("brnCode").toString());
        } else {
            brnCode = null;
        }
        if (session.getAttribute("agencyCode") != null) {
            agencyCode =
                    new BigDecimal(session.getAttribute("agencyCode").toString());
        } else {
            agencyCode = null;
        }
        if (txtAgentChoices.getValue() != null) {
            transType = txtAgentChoices.getValue().toString();
        } else {
            transType = null;
        }
        if (sltTransTypeDem.getValue() != null) {
            promType = sltTransTypeDem.getValue().toString();
        } else {
            promType = null;
        }
        if (session.getAttribute("braCode") != null) {
            braCode =
                    new BigDecimal(session.getAttribute("braCode").toString());
        } else {
            braCode = null;
        }
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = (OracleConnection)connector.getDatabaseConnection();
            String query =
                "begin LMS_WEB_PKG_UND.agentPromoDemotion(?,?,?,?,?,?,?,?,?,?,?); end;";
            OracleCallableStatement statement = null;
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1, agencyCode);
            statement.setBigDecimal(2, brnCode);
            statement.setString(3, transType);
            statement.setString(4, promType);
            statement.setDate(5, GlobalCC.extractDate(txtEffectiveDate));
            statement.setString(6,
                                session.getAttribute("Username").toString());
            statement.setBigDecimal(7, braCode);
            statement.setBigDecimal(8, null);
            statement.setBigDecimal(9, unitPref);
            statement.setBigDecimal(10, null);
            statement.setBigDecimal(11, precontractCode);
            statement.execute();
            connection.close();
            GlobalCC.INFORMATIONREPORTING("Demotion Successfull");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        clbAuthDemotion.setVisible(true);
        clbDemote.setVisible(false);
        GlobalCC.refreshUI(clbAuthDemotion);
        GlobalCC.refreshUI(clbDemote);
        return null;
    }

    public String authorisePromotion() {
        BigDecimal agencyCode = null;
        String transType;
        BigDecimal brnCode;
        String promType;
        BigDecimal braCode;
        if (session.getAttribute("brnCode") != null) {
            brnCode =
                    new BigDecimal(session.getAttribute("brnCode").toString());
        } else {
            brnCode = null;
        }
        if (session.getAttribute("agencyCode") != null) {
            agencyCode =
                    new BigDecimal(session.getAttribute("agencyCode").toString());
        } else {
            agencyCode = null;
        }
        if (txtAgentChoices.getValue() != null) {
            transType = txtAgentChoices.getValue().toString();
        } else {
            transType = null;
        }
        if (sltTransType.getValue() != null) {
            promType = sltTransType.getValue().toString();
        } else {
            promType = null;
        }
        if (session.getAttribute("braCode") != null) {
            braCode =
                    new BigDecimal(session.getAttribute("braCode").toString());
        } else {
            braCode = null;
        }
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = (OracleConnection)connector.getDatabaseConnection();
            String query =
                "begin LMS_WEB_PKG_UND.authorizeAgentPromotion(?,?,?,?,?,?); end;";
            OracleCallableStatement statement = null;
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1, agencyCode);
            statement.setBigDecimal(3, brnCode);
            statement.setString(4, transType);
            statement.setString(5, promType);
            statement.setDate(2, GlobalCC.extractDate(txtEffectiveDate));
            statement.setString(6,
                                session.getAttribute("Username").toString());
            statement.execute();
            connection.close();
            GlobalCC.INFORMATIONREPORTING("Promotion Authorised Successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String authoriseDemotion() {
        BigDecimal agencyCode = null;
        String transType;
        BigDecimal brnCode;
        String promType;
        BigDecimal braCode;
        if (session.getAttribute("brnCode") != null) {
            brnCode =
                    new BigDecimal(session.getAttribute("brnCode").toString());
        } else {
            brnCode = null;
        }
        if (session.getAttribute("agencyCode") != null) {
            agencyCode =
                    new BigDecimal(session.getAttribute("agencyCode").toString());
        } else {
            agencyCode = null;
        }
        if (txtAgentChoices.getValue() != null) {
            transType = txtAgentChoices.getValue().toString();
        } else {
            transType = null;
        }
        if (sltTransTypeDem.getValue() != null) {
            promType = sltTransTypeDem.getValue().toString();
        } else {
            promType = null;
        }
        if (session.getAttribute("braCode") != null) {
            braCode =
                    new BigDecimal(session.getAttribute("braCode").toString());
        } else {
            braCode = null;
        }
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = (OracleConnection)connector.getDatabaseConnection();
            String query =
                "begin LMS_WEB_PKG_UND.authorizeAgentDemotion(?,?,?,?,?,?); end;";
            OracleCallableStatement statement = null;
            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1, agencyCode);
            statement.setBigDecimal(2, braCode);
            statement.setString(4, transType);
            statement.setString(5, promType);
            statement.setDate(3, GlobalCC.extractDate(txtEffectiveDate));
            statement.setString(6,
                                session.getAttribute("Username").toString());
            statement.execute();
            connection.close();
            GlobalCC.INFORMATIONREPORTING("Demotion Authorised Successfully");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }


    public void setTxtAgencyPrefix(RichInputNumberSpinbox txtAgencyPrefix) {
        this.txtAgencyPrefix = txtAgencyPrefix;
    }

    public RichInputNumberSpinbox getTxtAgencyPrefix() {
        return txtAgencyPrefix;
    }

    public void setTxtUnitPrefix(RichInputNumberSpinbox txtUnitPrefix) {
        this.txtUnitPrefix = txtUnitPrefix;
    }

    public RichInputNumberSpinbox getTxtUnitPrefix() {
        return txtUnitPrefix;
    }

    public void setTxtAgencySeq(RichInputNumberSpinbox txtAgencySeq) {
        this.txtAgencySeq = txtAgencySeq;
    }

    public RichInputNumberSpinbox getTxtAgencySeq() {
        return txtAgencySeq;
    }

    public void setLblEffectiveDate(RichOutputLabel lblEffectiveDate) {
        this.lblEffectiveDate = lblEffectiveDate;
    }

    public RichOutputLabel getLblEffectiveDate() {
        return lblEffectiveDate;
    }

    public void setLblAgencyPrefix(RichOutputLabel lblAgencyPrefix) {
        this.lblAgencyPrefix = lblAgencyPrefix;
    }

    public RichOutputLabel getLblAgencyPrefix() {
        return lblAgencyPrefix;
    }

    public void setLblUnitPrefix(RichOutputLabel lblUnitPrefix) {
        this.lblUnitPrefix = lblUnitPrefix;
    }

    public RichOutputLabel getLblUnitPrefix() {
        return lblUnitPrefix;
    }

    public void setLblAgencySeq(RichOutputLabel lblAgencySeq) {
        this.lblAgencySeq = lblAgencySeq;
    }

    public RichOutputLabel getLblAgencySeq() {
        return lblAgencySeq;
    }

    public void setPrecontractCode(RichOutputLabel precontractCode) {
        this.precontractCode = precontractCode;
    }

    public RichOutputLabel getPrecontractCode() {
        return precontractCode;
    }

    public void setTxtPrecontractCode(RichInputNumberSpinbox txtPrecontractCode) {
        this.txtPrecontractCode = txtPrecontractCode;
    }

    public RichInputNumberSpinbox getTxtPrecontractCode() {
        return txtPrecontractCode;
    }


    public void setTxtCreditLimit(RichInputNumberSpinbox txtCreditLimit) {
        this.txtCreditLimit = txtCreditLimit;
    }

    public RichInputNumberSpinbox getTxtCreditLimit() {
        return txtCreditLimit;
    }

    public void selectCreditAllowed(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void selectCredit(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getOldValue() != valueChangeEvent.getNewValue()) {
            if (txtCreditAllowed.getValue() != null) {
                if (txtCreditAllowed.getValue().equals("Y")) {
                    txtCreditLimit.setVisible(true);

                } else {
                    txtCreditLimit.setVisible(false);
                }

            } else {
                txtCreditLimit.setVisible(false);
            }

        }
        GlobalCC.refreshUI(txtCreditLimit);
    }

    public void setTxtPhoneNumber(RichInputText txtPhoneNumber) {
        this.txtPhoneNumber = txtPhoneNumber;
    }

    public RichInputText getTxtPhoneNumber() {
        return txtPhoneNumber;
    }

    public void setTxtBranchUnits(RichInputText txtBranchUnits) {
        this.txtBranchUnits = txtBranchUnits;
    }

    public RichInputText getTxtBranchUnits() {
        return txtBranchUnits;
    }

    public String selectBranchUnits() {
        Object key = branchUnitsTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("bruCode", r.getAttribute("bruCode"));
        txtBranchUnits.setValue(r.getAttribute("bruName"));
        GlobalCC.refreshUI(txtBranchUnits);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:BranchUnitPop" + "').hide(hints);");
        return null;
    }

    public void setBranchUnitsTbl(RichTable branchUnitsTbl) {
        this.branchUnitsTbl = branchUnitsTbl;
    }

    public RichTable getBranchUnitsTbl() {
        return branchUnitsTbl;
    }

    public void setTxtLocalInt(RichSelectOneChoice txtLocalInt) {
        this.txtLocalInt = txtLocalInt;
    }

    public RichSelectOneChoice getTxtLocalInt() {
        return txtLocalInt;
    }

    public String authorizeAccount() {
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = (OracleConnection)connector.getDatabaseConnection();
            String query = "begin TQC_SETUPS_PKG.AuthorizeAccount(?,?); end;";
            OracleCallableStatement statement = null;

            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setObject(1, session.getAttribute("Username"));
            statement.setObject(2, session.getAttribute("agencyCode"));
            statement.execute();
            connection.close();

            GlobalCC.refreshUI(panelAgencyDetails);

            GlobalCC.INFORMATIONREPORTING("Account Successfully Authorised!");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public void setTxtPrincipleDirector(RichSelectOneChoice txtPrincipleDirector) {
        this.txtPrincipleDirector = txtPrincipleDirector;
    }

    public RichSelectOneChoice getTxtPrincipleDirector() {
        return txtPrincipleDirector;
    }

    private String getPrincipleDirector(String principleDir) {
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            connection = (OracleConnection)connector.getDatabaseConnection();
            String query =
                "begin TQC_AGENCIES_PKG.getAgencyPrincipleDir(?,?); end;";
            OracleCallableStatement statement = null;

            statement = (OracleCallableStatement)connection.prepareCall(query);
            statement.setBigDecimal(1,
                                    new BigDecimal((String)session.getAttribute("agencyCode")));
            statement.setObject(2, principleDir);
            statement.execute();
            connection.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }


    public void setTxtIraRegNumber(RichInputText txtIraRegNumber) {
        this.txtIraRegNumber = txtIraRegNumber;
    }

    public RichInputText getTxtIraRegNumber() {
        return txtIraRegNumber;
    }

    public void setBtnAuthorizeAgent(RichCommandButton btnAuthorizeAgent) {
        this.btnAuthorizeAgent = btnAuthorizeAgent;
    }

    public RichCommandButton getBtnAuthorizeAgent() {
        return btnAuthorizeAgent;
    }

    public void setCountryTbl(RichTable countryTbl) {
        this.countryTbl = countryTbl;
    }

    public RichTable getCountryTbl() {
        return countryTbl;
    }

    public void selectCountry(ActionEvent actionEvent) {
        // Add event code here...
    }

    public String selectCountry() {
        Object key = countryTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        txtNationality.setValue(r.getAttribute("name"));
        session.setAttribute("countryCode", r.getAttribute("code"));
        GlobalCC.refreshUI(txtNationality);
        GlobalCC.dismissPopUp("pt1", "p4");
        return null;
    }

    public void setTxtNationality(RichInputText txtNationality) {
        this.txtNationality = txtNationality;
    }

    public RichInputText getTxtNationality() {
        return txtNationality;
    }

    public void setTxtRatingOrg(RichInputText txtRatingOrg) {
        this.txtRatingOrg = txtRatingOrg;
    }

    public RichInputText getTxtRatingOrg() {
        return txtRatingOrg;
    }

    public void setTxtRating(RichInputText txtRating) {
        this.txtRating = txtRating;
    }

    public RichInputText getTxtRating() {
        return txtRating;
    }

    public void setOrgRatingTbl(RichTable orgRatingTbl) {
        this.orgRatingTbl = orgRatingTbl;
    }

    public RichTable getOrgRatingTbl() {
        return orgRatingTbl;
    }

    public String selectRatingOrganizations() {
        Object key = orgRatingTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("rorgCode", r.getAttribute("rorgCode"));
        txtRatingOrg.setValue(r.getAttribute("rorgDesc"));
        GlobalCC.refreshUI(txtRatingOrg);
        ADFUtils.findIterator("findRatingStartndardsIterator").executeQuery();
        GlobalCC.refreshUI(ratingTbl);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:OrgRating" +
                             "').hide(hints);");

        return null;
    }

    public String selectRating() {
        // Add event code here...
        return null;
    }

    public String selectRatingDtls() {
        Object key = ratingTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("orsCode", r.getAttribute("orsCode"));
        txtRating.setValue(r.getAttribute("orsDesc"));
        GlobalCC.refreshUI(txtRating);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:RatingPop" +
                             "').hide(hints);");
        return null;
    }

    public void setRatingTbl(RichTable ratingTbl) {
        this.ratingTbl = ratingTbl;
    }

    public RichTable getRatingTbl() {
        return ratingTbl;
    }

    public String selectRatingPop() {
        if (txtRatingOrg.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Please select rating Organization");
            return null;
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:RatingPop" +
                             "').show(hints);");

        return null;
    }

    public void setTxtAgencySelected(RichSelectOneChoice txtAgencySelected) {
        this.txtAgencySelected = txtAgencySelected;
    }

    public RichSelectOneChoice getTxtAgencySelected() {
        return txtAgencySelected;
    }

    public void setTxtIssueCert(RichSelectOneChoice txtIssueCert) {
        this.txtIssueCert = txtIssueCert;
    }

    public RichSelectOneChoice getTxtIssueCert() {
        return txtIssueCert;
    }

    public String selectBranchDtls() {
        Object key = branchTblDtls.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        txtBranch.setValue(r.getAttribute("branchName"));
        GlobalCC.refreshUI(txtBranch);
        session.setAttribute("branchCode", r.getAttribute("branchCode"));
        System.out.println("pppbranchCode: " +
                           session.getAttribute("branchCode"));
        return null;
    }

    public void setBranchTblDtls(RichTable branchTblDtls) {
        this.branchTblDtls = branchTblDtls;
    }

    public RichTable getBranchTblDtls() {
        return branchTblDtls;
    }

    public void setTxtBranch(RichInputText txtBranch) {
        this.txtBranch = txtBranch;
    }

    public RichInputText getTxtBranch() {
        return txtBranch;
    }

    public String selectCountryDtls() {
        Object key = countryTblDtls.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("Countrycode", r.getAttribute("code"));
        txtCountry.setValue(r.getAttribute("name"));
        GlobalCC.refreshUI(txtCountry);
        session.setAttribute("countryPop", r.getAttribute("code"));

        // Add event code here...
        return null;
    }

    public void setCountryTblDtls(RichTable countryTblDtls) {
        this.countryTblDtls = countryTblDtls;
    }

    public RichTable getCountryTblDtls() {
        return countryTblDtls;
    }

    public void setTxtCountry(RichInputText txtCountry) {
        this.txtCountry = txtCountry;
    }

    public RichInputText getTxtCountry() {
        return txtCountry;
    }

    public void setPrefixTbl(RichTable prefixTbl) {
        this.prefixTbl = prefixTbl;
    }

    public RichTable getPrefixTbl() {
        return prefixTbl;
    }

    public String selectPrefixPop() {
        Object key = prefixTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("mptCode", r.getAttribute("mptCode"));
        txtPrefixVals.setValue(r.getAttribute("prefix"));
        GlobalCC.refreshUI(txtPrefixVals);
        return null;
    }

    public void setTxtPrefixVals(RichInputText txtPrefixVals) {
        this.txtPrefixVals = txtPrefixVals;
    }

    public RichInputText getTxtPrefixVals() {
        return txtPrefixVals;
    }

    public void setClientTitleTbl(RichTable clientTitleTbl) {
        this.clientTitleTbl = clientTitleTbl;
    }

    public RichTable getClientTitleTbl() {
        return clientTitleTbl;
    }

    public String selectClientTitle() {
        Object key = clientTitleTblDtls.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("ClientTitlecode", r.getAttribute("code"));
        txtClientTitle.setValue(r.getAttribute("description"));
        GlobalCC.refreshUI(txtClientTitle);
        return null;
    }

    public String selectBank() {
        Object key = bankTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("bankCode", r.getAttribute("bankCode"));
        txtBankName.setValue(r.getAttribute("bankName"));
        GlobalCC.refreshUI(txtBankName);
        return null;
    }

    public void setBankTbl(RichTable bankTbl) {
        this.bankTbl = bankTbl;
    }

    public RichTable getBankTbl() {
        return bankTbl;
    }

    public void setBankBranchTbl(RichTable bankBranchTbl) {
        this.bankBranchTbl = bankBranchTbl;
    }

    public RichTable getBankBranchTbl() {
        return bankBranchTbl;
    }

    public String selectBankBranch() {
        Object key = bankBranchTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("BankbranchCode", r.getAttribute("branchCode"));
        txtBankBranch.setValue(r.getAttribute("branchName"));
        GlobalCC.refreshUI(txtBankBranch);

        return null;
    }

    public String selectSectors() {
        Object key = sectorTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("sectorCode", r.getAttribute("code"));
        txtSector.setValue(r.getAttribute("name"));
        GlobalCC.refreshUI(txtSector);

        return null;
    }

    public void setSectorTbl(RichTable sectorTbl) {
        this.sectorTbl = sectorTbl;
    }

    public RichTable getSectorTbl() {
        return sectorTbl;
    }

    public void setTxtSector(RichInputText txtSector) {
        this.txtSector = txtSector;
    }

    public RichInputText getTxtSector() {
        return txtSector;
    }

    public void setTxtShtDesc(RichInputText txtShtDesc) {
        this.txtShtDesc = txtShtDesc;
    }

    public RichInputText getTxtShtDesc() {
        return txtShtDesc;
    }

    public void setTxtPhysicalAddress(RichInputText txtPhysicalAddress) {
        this.txtPhysicalAddress = txtPhysicalAddress;
    }

    public RichInputText getTxtPhysicalAddress() {
        return txtPhysicalAddress;
    }

    public void setTxtPostalAddress(RichInputText txtPostalAddress) {
        this.txtPostalAddress = txtPostalAddress;
    }

    public RichInputText getTxtPostalAddress() {
        return txtPostalAddress;
    }

    public void setTxtSelectClientTitle(RichCommandButton txtSelectClientTitle) {
        this.txtSelectClientTitle = txtSelectClientTitle;
    }

    public RichCommandButton getTxtSelectClientTitle() {
        return txtSelectClientTitle;
    }

    public void setTxtClientTitle(RichInputText txtClientTitle) {
        this.txtClientTitle = txtClientTitle;
    }

    public RichInputText getTxtClientTitle() {
        return txtClientTitle;
    }

    public void setClientTitleTblDtls(RichTable clientTitleTblDtls) {
        this.clientTitleTblDtls = clientTitleTblDtls;
    }

    public RichTable getClientTitleTblDtls() {
        return clientTitleTblDtls;
    }

    public String saveUpdateEntities() {

        String phoneNumber;
        String textSms;
        if (txtSms.getValue() != null) {
            textSms = txtSms.getValue().toString();
        } else {
            textSms = null;
        }
        if (txtPrefixVals.getValue() != null) {
            phoneNumber = txtPrefixVals.getValue().toString();
        } else {
            phoneNumber = null;
        }
        if (phoneNumber != null) {
            phoneNumber.concat(textSms);
        }
        session.removeAttribute("EntityCode");
        DBConnector connector = new DBConnector();
        OracleConnection connection = null;
        try {
            if (txtName.getValue() != null) {
                connection =
                        (OracleConnection)connector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.updateentities(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);

                statement.setObject(1, "A");
                statement.registerOutParameter(2,
                                               oracle.jdbc.OracleTypes.NUMBER);
                statement.setObject(3, txtShtDesc.getValue());
                statement.setObject(4, txtName.getValue());
                statement.setObject(5, txtPhysicalAddress.getValue());
                statement.setObject(6, txtPostalAddress.getValue());
                statement.setObject(7, session.getAttribute("townCode"));
                statement.setObject(8, session.getAttribute("Countrycode"));
                statement.setObject(9, txtEmail.getValue());
                statement.setObject(10, txtFax.getValue());
                statement.setObject(11, txtAgencyStatus.getValue());
                statement.setObject(12,
                                    session.getAttribute("BankbranchCode"));
                statement.setObject(13, txtBankAccountNumber.getValue());
                statement.setObject(14, txtZip.getValue());
                statement.setObject(15, phoneNumber);
                statement.setObject(16, session.getAttribute("Username"));
                statement.setObject(17, null);
                statement.setObject(18, txtAgencyPIN.getValue());
                statement.setObject(19, txtOffice.getValue());
                statement.setObject(20, txtAgencyPhone2.getValue());
                statement.setObject(21, txtAgencyStatusRemarks.getValue());
                statement.setObject(22, session.getAttribute("branchCode"));
                statement.setObject(23, txtIdNumber.getValue());
                statement.setObject(24, txtAgencyPhone2.getValue());
                statement.setObject(25, session.getAttribute("sectorCode"));
                statement.setObject(26, txtAgencyRunoff.getValue());
                statement.setObject(27, txtOldAccount.getValue());
                statement.setObject(28, txtClientTitle.getValue());
                statement.setObject(29, GlobalCC.extractDate(txtWef));
                statement.setObject(30, GlobalCC.extractDate(txtWet));
                statement.setObject(31, txtContactPerson.getValue());
                if (session.getAttribute("source").equals("FromClient")) {
                    statement.setObject(32, "C");
                }
                statement.execute();
                GlobalCC.INFORMATIONREPORTING("Record Created Successfully");
                BigDecimal returnedCode = statement.getBigDecimal(2);
                session.setAttribute("EntityCode", returnedCode);
                if (session.getAttribute("EntityCode") != null) {
                    txtNextBtn.setDisabled(true);
                }
                GlobalCC.refreshUI(txtNextBtn);
                connection.close();
                session.removeAttribute("source");
                FacesContext.getCurrentInstance().getExternalContext().redirect("clients.jspx");
            } else {
                if (session.getAttribute("source").equals("FromClient")) {
                    //  NavigationUtils navigation = new NavigationUtils();
                    // navigation.navigate("viewPolicy.jspx");
                    session.removeAttribute("source");
                    FacesContext.getCurrentInstance().getExternalContext().redirect("clients.jspx");

                }
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public void setTxtName(RichInputText txtName) {
        this.txtName = txtName;
    }

    public RichInputText getTxtName() {
        return txtName;
    }

    public void setTxtEmail(RichInputText txtEmail) {
        this.txtEmail = txtEmail;
    }

    public RichInputText getTxtEmail() {
        return txtEmail;
    }

    public void setTxtFax(RichInputText txtFax) {
        this.txtFax = txtFax;
    }

    public RichInputText getTxtFax() {
        return txtFax;
    }

    public void setTxtBankAccountNumber(RichInputText txtBankAccountNumber) {
        this.txtBankAccountNumber = txtBankAccountNumber;
    }

    public RichInputText getTxtBankAccountNumber() {
        return txtBankAccountNumber;
    }

    public void setTxtZip(RichInputText txtZip) {
        this.txtZip = txtZip;
    }

    public RichInputText getTxtZip() {
        return txtZip;
    }

    public void setTxtOffice(RichInputText txtOffice) {
        this.txtOffice = txtOffice;
    }

    public RichInputText getTxtOffice() {
        return txtOffice;
    }

    public void setTxtIdNumber(RichInputText txtIdNumber) {
        this.txtIdNumber = txtIdNumber;
    }

    public RichInputText getTxtIdNumber() {
        return txtIdNumber;
    }

    public void setTxtOldAccount(RichInputText txtOldAccount) {
        this.txtOldAccount = txtOldAccount;
    }

    public RichInputText getTxtOldAccount() {
        return txtOldAccount;
    }

    public void setTxtWef(RichInputDate txtWef) {
        this.txtWef = txtWef;
    }

    public RichInputDate getTxtWef() {
        return txtWef;
    }

    public void setTxtWet(RichInputDate txtWet) {
        this.txtWet = txtWet;
    }

    public RichInputDate getTxtWet() {
        return txtWet;
    }

    public void setTownPopTbl(RichTable townPopTbl) {
        this.townPopTbl = townPopTbl;
    }

    public RichTable getTownPopTbl() {
        return townPopTbl;
    }

    public String selectTownDtls() {
        Object key = townPopTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("townCode", r.getAttribute("code"));
        txtTown.setValue(r.getAttribute("name"));
        GlobalCC.refreshUI(txtTown);

        return null;
    }


    public void setTxtNextBtn(RichCommandButton txtNextBtn) {
        this.txtNextBtn = txtNextBtn;
    }

    public RichCommandButton getTxtNextBtn() {
        return txtNextBtn;
    }

    public void setTxtSectors(RichInputText txtSectors) {
        this.txtSectors = txtSectors;
    }

    public RichInputText getTxtSectors() {
        return txtSectors;
    }

    public void setTxtTown(RichInputText txtTown) {
        this.txtTown = txtTown;
    }

    public RichInputText getTxtTown() {
        return txtTown;
    }

    public void setTxtSms(RichInputText txtSms) {
        this.txtSms = txtSms;
    }

    public RichInputText getTxtSms() {
        return txtSms;
    }

    public String selectSearchPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:searchClientPop" + "').show(hints);");
        return null;
    }

    public String BranchUnitPop() {
        Rendering renderer = new Rendering();
        if (renderer.isREGION_UNIT_DEPEDENCY() &&
            (txtAgencyRegionCode.getValue() == null)) {
            GlobalCC.INFORMATIONREPORTING("Please Select Region Before Selecting Branch Unit!");
            return null;
        }
        ADFUtils.findIterator("findBranchUnitsIterator").executeQuery();
        GlobalCC.refreshUI(branchUnitPopUp);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:BranchUnitPop" + "').show(hints);");

        return null;
    }

    public void setGridClientSearchDetails(HtmlPanelGrid gridClientSearchDetails) {
        this.gridClientSearchDetails = gridClientSearchDetails;
    }

    public HtmlPanelGrid getGridClientSearchDetails() {
        return gridClientSearchDetails;
    }

    public void setSearchFormHolder(RichPanelGroupLayout searchFormHolder) {
        this.searchFormHolder = searchFormHolder;
    }

    public RichPanelGroupLayout getSearchFormHolder() {
        return searchFormHolder;
    }

    public void setSEARCHHOLDER(RichPanelFormLayout SEARCHHOLDER) {
        this.SEARCHHOLDER = SEARCHHOLDER;
    }

    public RichPanelFormLayout getSEARCHHOLDER() {
        return SEARCHHOLDER;
    }

    public void enterKeyPressed(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setTxtSearchName(RichInputText txtSearchName) {
        this.txtSearchName = txtSearchName;
    }

    public RichInputText getTxtSearchName() {
        return txtSearchName;
    }

    public void setTxtSearchShortDesc(RichInputText txtSearchShortDesc) {
        this.txtSearchShortDesc = txtSearchShortDesc;
    }

    public RichInputText getTxtSearchShortDesc() {
        return txtSearchShortDesc;
    }

    public void setTxtSearchPhysical(RichInputText txtSearchPhysical) {
        this.txtSearchPhysical = txtSearchPhysical;
    }

    public RichInputText getTxtSearchPhysical() {
        return txtSearchPhysical;
    }

    public void setTxtSearchPostal(RichInputText txtSearchPostal) {
        this.txtSearchPostal = txtSearchPostal;
    }

    public RichInputText getTxtSearchPostal() {
        return txtSearchPostal;
    }

    public void setTxtSearchSector(RichInputText txtSearchSector) {
        this.txtSearchSector = txtSearchSector;
    }

    public RichInputText getTxtSearchSector() {
        return txtSearchSector;
    }

    public void setResetSearchContainer(RichPanelLabelAndMessage resetSearchContainer) {
        this.resetSearchContainer = resetSearchContainer;
    }

    public RichPanelLabelAndMessage getResetSearchContainer() {
        return resetSearchContainer;
    }

    public void setTxtSrchSectorName(RichInputText txtSrchSectorName) {
        this.txtSrchSectorName = txtSrchSectorName;
    }

    public RichInputText getTxtSrchSectorName() {
        return txtSrchSectorName;
    }

    public void setBtnSectorLov(RichCommandButton btnSectorLov) {
        this.btnSectorLov = btnSectorLov;
    }

    public RichCommandButton getBtnSectorLov() {
        return btnSectorLov;
    }

    public void setStatusHolder(RichPanelLabelAndMessage statusHolder) {
        this.statusHolder = statusHolder;
    }

    public RichPanelLabelAndMessage getStatusHolder() {
        return statusHolder;
    }

    public void setTxtSearchStatus(RichSelectOneChoice txtSearchStatus) {
        this.txtSearchStatus = txtSearchStatus;
    }

    public RichSelectOneChoice getTxtSearchStatus() {
        return txtSearchStatus;
    }

    public void cboSearchStatusListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setClntDateCreatedFrom(RichInputDate clntDateCreatedFrom) {
        this.clntDateCreatedFrom = clntDateCreatedFrom;
    }

    public RichInputDate getClntDateCreatedFrom() {
        return clntDateCreatedFrom;
    }

    public void setClntDateCreatedTo(RichInputDate clntDateCreatedTo) {
        this.clntDateCreatedTo = clntDateCreatedTo;
    }

    public RichInputDate getClntDateCreatedTo() {
        return clntDateCreatedTo;
    }

    public void setPinNumber(RichInputText pinNumber) {
        this.pinNumber = pinNumber;
    }

    public RichInputText getPinNumber() {
        return pinNumber;
    }

    public void setRbtnSearchAccountNo(RichSelectBooleanRadio rbtnSearchAccountNo) {
        this.rbtnSearchAccountNo = rbtnSearchAccountNo;
    }

    public RichSelectBooleanRadio getRbtnSearchAccountNo() {
        return rbtnSearchAccountNo;
    }

    public void setRbtnPartOfAnyName(RichSelectBooleanRadio rbtnPartOfAnyName) {
        this.rbtnPartOfAnyName = rbtnPartOfAnyName;
    }

    public RichSelectBooleanRadio getRbtnPartOfAnyName() {
        return rbtnPartOfAnyName;
    }

    public void criteriaValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            System.out.println("This is the one");
            if (rbtnSearchAccountNo.isSelected()) {
                System.out.println("This is the one 5345");
                txtAccountNo.setDisabled(false);
                txtSearchShortDesc.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
            } else if (rbtnPartOfAnyName.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(false);
                //txtSearchOtherName.setDisabled(false);
            } else if (rbtnExactName.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(false);
                txtSearchPostal.setDisabled(true);
            } else if (rbtnSector.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(false);
                txtSearchPostal.setDisabled(true);
            } else if (rbtnSector.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(false);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPostal.setDisabled(true);
            } else if (rbtnStatus.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtSearchStatus.setDisabled(false);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPostal.setDisabled(true);
            } else if (rbtnShortDesc.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtSearchShortDesc.setDisabled(false);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPostal.setDisabled(true);
            } else if (rbtnDateCreated.isSelected()) {
                System.out.println("This is the one 5345566");
                //              txtAccountNo.setDisabled(true);
                //              txtSearchShortDesc.setDisabled(true);
                //              txtSearchName.setDisabled(true);
                //              txtSearchPhysical.setDisabled(true);
                //              txtSrchSectorName.setDisabled(true);
                //              btnSectorLov.setDisabled(true);
                //              txtSearchStatus.setDisabled(true);
                //              clntDateCreatedFrom.setDisabled(false);
                //              clntDateCreatedTo.setDisabled(false);
                //              pinNumber.setDisabled(true);
                //              txtSearchName.setDisabled(true);
                //              txtSearchPostal.setDisabled(true);
            } else if (rbtnPhySicalAddr.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(false);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPostal.setDisabled(true);
            } else if (rbtnPostalAddr.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(false);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPostal.setDisabled(true);
            } else if (txtPinNumber.isSelected()) {
                txtAccountNo.setDisabled(true);
                txtSearchShortDesc.setDisabled(true);
                txtSearchName.setDisabled(true);
                txtSearchPhysical.setDisabled(true);
                txtSrchSectorName.setDisabled(true);
                btnSectorLov.setDisabled(true);
                txtSearchStatus.setDisabled(true);
                clntDateCreatedFrom.setDisabled(true);
                clntDateCreatedTo.setDisabled(true);
                pinNumber.setDisabled(false);
                txtSearchName.setDisabled(true);
                txtSearchPostal.setDisabled(true);
            }
            GlobalCC.refreshUI(txtAccountNo);
            GlobalCC.refreshUI(txtSearchShortDesc);
            GlobalCC.refreshUI(txtSearchName);
            GlobalCC.refreshUI(txtSearchPhysical);
            GlobalCC.refreshUI(txtSrchSectorName);
            GlobalCC.refreshUI(btnSectorLov);
            GlobalCC.refreshUI(txtSearchStatus);
            GlobalCC.refreshUI(clntDateCreatedFrom);
            GlobalCC.refreshUI(clntDateCreatedTo);
            GlobalCC.refreshUI(pinNumber);
            GlobalCC.refreshUI(txtSearchName);
            GlobalCC.refreshUI(txtSearchPostal);
        }
    }

    public void setRbtnExactName(RichSelectBooleanRadio rbtnExactName) {
        this.rbtnExactName = rbtnExactName;
    }

    public RichSelectBooleanRadio getRbtnExactName() {
        return rbtnExactName;
    }

    public void setRbtnStatus(RichSelectBooleanRadio rbtnStatus) {
        this.rbtnStatus = rbtnStatus;
    }

    public RichSelectBooleanRadio getRbtnStatus() {
        return rbtnStatus;
    }

    public void setRbtnShortDesc(RichSelectBooleanRadio rbtnShortDesc) {
        this.rbtnShortDesc = rbtnShortDesc;
    }

    public RichSelectBooleanRadio getRbtnShortDesc() {
        return rbtnShortDesc;
    }

    public void setRbtnShortDescLeg(RichSelectBooleanRadio rbtnShortDescLeg) {
        this.rbtnShortDescLeg = rbtnShortDescLeg;
    }

    public RichSelectBooleanRadio getRbtnShortDescLeg() {
        return rbtnShortDescLeg;
    }

    public void setRbtnPhySicalAddr(RichSelectBooleanRadio rbtnPhySicalAddr) {
        this.rbtnPhySicalAddr = rbtnPhySicalAddr;
    }

    public RichSelectBooleanRadio getRbtnPhySicalAddr() {
        return rbtnPhySicalAddr;
    }

    public void setRbtnPostalAddr(RichSelectBooleanRadio rbtnPostalAddr) {
        this.rbtnPostalAddr = rbtnPostalAddr;
    }

    public RichSelectBooleanRadio getRbtnPostalAddr() {
        return rbtnPostalAddr;
    }

    public void setRbtnCustomerId(RichSelectBooleanRadio rbtnCustomerId) {
        this.rbtnCustomerId = rbtnCustomerId;
    }

    public RichSelectBooleanRadio getRbtnCustomerId() {
        return rbtnCustomerId;
    }

    public void setRbtnOldNames(RichSelectBooleanRadio rbtnOldNames) {
        this.rbtnOldNames = rbtnOldNames;
    }

    public RichSelectBooleanRadio getRbtnOldNames() {
        return rbtnOldNames;
    }

    public void setTxtPinNumber(RichSelectBooleanRadio txtPinNumber) {
        this.txtPinNumber = txtPinNumber;
    }

    public RichSelectBooleanRadio getTxtPinNumber() {
        return txtPinNumber;
    }

    public String actionAcceptSearchCriteria() {
        // Add event code here...
        return null;
    }

    public String actionResetSearch() {
        // Add event code here...
        return null;
    }

    public void setPanelCollSearch(RichPanelCollection panelCollSearch) {
        this.panelCollSearch = panelCollSearch;
    }

    public RichPanelCollection getPanelCollSearch() {
        return panelCollSearch;
    }

    public void setRbtnSector(RichSelectBooleanRadio rbtnSector) {
        this.rbtnSector = rbtnSector;
    }

    public RichSelectBooleanRadio getRbtnSector() {
        return rbtnSector;
    }


    public void setRbtnDateCreated(RichSelectBooleanRadio rbtnDateCreated) {
        this.rbtnDateCreated = rbtnDateCreated;
    }

    public RichSelectBooleanRadio getRbtnDateCreated() {
        return rbtnDateCreated;
    }

    public void setRdoDateCreatedFrom(RichSelectBooleanRadio rdoDateCreatedFrom) {
        this.rdoDateCreatedFrom = rdoDateCreatedFrom;
    }

    public RichSelectBooleanRadio getRdoDateCreatedFrom() {
        return rdoDateCreatedFrom;
    }

    public String selectHoldingCompanies() {

        return null;
    }

    public void setHoldingCompaniesTbl(RichTable holdingCompaniesTbl) {
        this.holdingCompaniesTbl = holdingCompaniesTbl;
    }

    public RichTable getHoldingCompaniesTbl() {
        return holdingCompaniesTbl;
    }

    public String selectPrefix() {
        Object key = prefixTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        session.setAttribute("mptpCode", r.getAttribute("mptpCode"));
        session.setAttribute("mptCode", r.getAttribute("mptCode"));
        txtSmsPrefix.setValue(r.getAttribute("prefix"));
        GlobalCC.refreshUI(txtSmsPrefix);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:prefixPOP" +
                             "').hide(hints);");
        return null;
    }

    public String selectPrefix2() {
        Object key = prefixTbl2.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        session.setAttribute("mptpCode", r.getAttribute("mptpCode"));
        session.setAttribute("mptCode", r.getAttribute("mptCode"));
        txtTelPayPrefix.setValue(r.getAttribute("prefix"));
        GlobalCC.refreshUI(txtTelPayPrefix);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:prefixPOP2" + "').hide(hints);");
        return null;
    }

    public void setTxtPrefixManager(RichInputText txtPrefixManager) {
        this.txtPrefixManager = txtPrefixManager;
    }

    public RichInputText getTxtPrefixManager() {
        return txtPrefixManager;
    }

    public void setPrefixPop(RichCommandButton prefixPop) {
        this.prefixPop = prefixPop;
    }

    public RichCommandButton getPrefixPop() {
        return prefixPop;
    }

    public void setTxtSmsPrefix(RichInputText txtSmsPrefix) {
        this.txtSmsPrefix = txtSmsPrefix;
    }

    public RichInputText getTxtSmsPrefix() {
        return txtSmsPrefix;
    }

    public void setTxtBouncedCheque(RichSelectOneChoice txtBouncedCheque) {
        this.txtBouncedCheque = txtBouncedCheque;
    }

    public RichSelectOneChoice getTxtBouncedCheque() {
        return txtBouncedCheque;
    }

    public void setTxtModeOfComm(RichSelectOneChoice txtModeOfComm) {
        this.txtModeOfComm = txtModeOfComm;
    }

    public RichSelectOneChoice getTxtModeOfComm() {
        return txtModeOfComm;
    }

    public void setTxtBussinessPersons(RichInputText txtBussinessPersons) {
        this.txtBussinessPersons = txtBussinessPersons;
    }

    public RichInputText getTxtBussinessPersons() {
        return txtBussinessPersons;
    }

    public void setBussinessPersonTbl(RichTable bussinessPersonTbl) {
        this.bussinessPersonTbl = bussinessPersonTbl;
    }

    public RichTable getBussinessPersonTbl() {
        return bussinessPersonTbl;
    }

    public String selectBussinessPerson() {
        Object key = bussinessPersonTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }

        txtBussinessPersons.setValue(r.getAttribute("bpnName"));
        GlobalCC.refreshUI(txtBussinessPersons);
        session.setAttribute("bpnCode", r.getAttribute("bpnCode"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:bussinessPersonPop" + "').hide(hints);");

        // Add event code here...
        return null;
    }

    public void setTxtAgentType(RichInputText txtAgentType) {
        this.txtAgentType = txtAgentType;
    }

    public RichInputText getTxtAgentType() {
        return txtAgentType;
    }

    public void setTxtAgentGroup(RichInputText txtAgentGroup) {
        this.txtAgentGroup = txtAgentGroup;
    }

    public RichInputText getTxtAgentGroup() {
        return txtAgentGroup;
    }

    public String selectAgentType() {
        Object key = agentTypeTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        txtAgentType.setValue(r.getAttribute("type"));
        GlobalCC.refreshUI(txtAgentType);
        if (r.getAttribute("type") != null) {
            if (r.getAttribute("type").toString().equalsIgnoreCase("INDEPENDENT AGENTS") ||
                r.getAttribute("type").toString().equalsIgnoreCase("BANCASSURANCE")) {
                txtIraRegNumber.setRequired(true);
                txtIraRegNumber.setVisible(true);
                txtRegistartionCode.setRequired(true);
                GlobalCC.refreshUI(txtIraRegNumber);
                GlobalCC.refreshUI(txtRegistartionCode);


            } else if (r.getAttribute("type").toString().equalsIgnoreCase("DIRECT SALES AGENTS")) {

                txtIraRegNumber.setVisible(false);
                txtRegistartionCode.setRequired(false);
                GlobalCC.refreshUI(txtIraRegNumber);
                GlobalCC.refreshUI(txtRegistartionCode);
            }
            //            if (r.getAttribute("type").toString().equalsIgnoreCase("DIRECT SALES AGENTS")){
            //                    txtRegistartionCode.setRequired(false);
            //                    GlobalCC.refreshUI(txtRegistartionCode);
            //            }else{
            //                    txtRegistartionCode.setRequired(true);
            //                    GlobalCC.refreshUI(txtRegistartionCode);
            //                }
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:agentTypePop" + "').hide(hints);");
        return null;
    }

    public void setAgentTypeTbl(RichTable agentTypeTbl) {
        this.agentTypeTbl = agentTypeTbl;
    }

    public RichTable getAgentTypeTbl() {
        return agentTypeTbl;
    }

    public void setAccountGroupsTbl(RichTable accountGroupsTbl) {
        this.accountGroupsTbl = accountGroupsTbl;
    }

    public RichTable getAccountGroupsTbl() {
        return accountGroupsTbl;
    }

    public String selectAccountGroup() {
        Object key = accountGroupsTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record selected");
            return null;
        }
        txtAgentGroup.setValue(r.getAttribute("accountGroups"));
        GlobalCC.refreshUI(txtAgentGroup);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:agentGroupPop" + "').hide(hints);");

        // Add event code here...
        return null;
    }

    public String actionShowAuthorityTbl() {
        //System.out.println("fuishfisfuisfuidsuhufisfs");
        GlobalCC.showPopup("pt1:taxAuthorityPop");
        return null;
    }

    public String selectTaxAuthority() {
        Object key = taxAuthorityTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        txtAgencyTaxAuthName.setValue(r.getAttribute("taxAuthName"));
        txtAgencyTaxAuthCode.setValue(r.getAttribute("taxAuthCode"));
        GlobalCC.refreshUI(txtAgencyTaxAuthName);

        GlobalCC.hidePopup("pt1:taxAuthorityPopUp");
        return null;
    }

    public void setTxtWithTax(RichSelectOneChoice txtWithTax) {
        this.txtWithTax = txtWithTax;
    }

    public RichSelectOneChoice getTxtWithTax() {
        return txtWithTax;
    }

    public void setTxtVatApp(RichSelectOneChoice txtVatApp) {
        this.txtVatApp = txtVatApp;
    }

    public RichSelectOneChoice getTxtVatApp() {
        return txtVatApp;
    }

    public void setUnAssignedProductsTbl(RichTable unAssignedProductsTbl) {
        this.unAssignedProductsTbl = unAssignedProductsTbl;
    }

    public RichTable getUnAssignedProductsTbl() {
        return unAssignedProductsTbl;
    }

    public void setAssignedProductsTbl(RichTable assignedProductsTbl) {
        this.assignedProductsTbl = assignedProductsTbl;
    }

    public RichTable getAssignedProductsTbl() {
        return assignedProductsTbl;
    }

    public void setChkAssignedProducts(RichSelectBooleanCheckbox chkAssignedProducts) {
        this.chkAssignedProducts = chkAssignedProducts;
    }

    public RichSelectBooleanCheckbox getChkAssignedProducts() {
        return chkAssignedProducts;
    }

    public void setChkUnAssignedProducts(RichSelectBooleanCheckbox chkUnAssignedProducts) {
        this.chkUnAssignedProducts = chkUnAssignedProducts;
    }

    public RichSelectBooleanCheckbox getChkUnAssignedProducts() {
        return chkUnAssignedProducts;
    }

    public void setTxtTelPay(RichInputText txtTelPay) {
        this.txtTelPay = txtTelPay;
    }

    public RichInputText getTxtTelPay() {
        return txtTelPay;
    }

    public void setTxtTelPayPrefix(RichInputText txtTelPayPrefix) {
        this.txtTelPayPrefix = txtTelPayPrefix;
    }

    public RichInputText getTxtTelPayPrefix() {
        return txtTelPayPrefix;
    }

    public void setPrefixTbl2(RichTable prefixTbl2) {
        this.prefixTbl2 = prefixTbl2;
    }

    public RichTable getPrefixTbl2() {
        return prefixTbl2;
    }

    public void setTxtFrequencyofPayment(RichSelectOneChoice txtFrequencyofPayment) {
        this.txtFrequencyofPayment = txtFrequencyofPayment;
    }

    public RichSelectOneChoice getTxtFrequencyofPayment() {
        return txtFrequencyofPayment;
    }

    public void setTxtModeofPayment(RichSelectOneChoice txtModeofPayment) {
        this.txtModeofPayment = txtModeofPayment;
    }

    public RichSelectOneChoice getTxtModeofPayment() {
        return txtModeofPayment;
    }

    public void pymtMode(ValueChangeEvent valueChangeEvent) {
        session.setAttribute("PM_MODE", valueChangeEvent.getNewValue());
        session.setAttribute("pymtMode", valueChangeEvent.getNewValue());
        System.out.println("PM_MODE = " + valueChangeEvent.getNewValue());
    }

    public void setPaymentModes(List<SelectItem> paymentModes) {
        this.paymentModes = paymentModes;
    }

    public List<SelectItem> getPaymentModes() {
        if (paymentModes != null) {
            paymentModes.clear();
        }
        DBConnector connection = new DBConnector();
        Connection conn = null;
        CallableStatement stmt = null;

        try {
            conn = connection.getDatabaseConnection();
            stmt =
conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.getclaimpaymodes(); end;");
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.execute();
            ResultSet rst = (ResultSet)stmt.getObject(1);
            //System.out.println("Payment modes: ");
            while (rst.next()) {
                //System.out.println("code: "+rst.getString(1)+" name: "+rst.getString(2));
                paymentModes.add(new SelectItem(rst.getString(1),
                                                rst.getString(2)));
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        }
        return paymentModes;
    }

    public void setTxtPmntDtlsValidated(RichSelectOneChoice txtPmntDtlsValidated) {
        this.txtPmntDtlsValidated = txtPmntDtlsValidated;
    }

    public RichSelectOneChoice getTxtPmntDtlsValidated() {
        return txtPmntDtlsValidated;
    }


    public void setTxtAgnDOB(RichInputDate txtAgnDOB) {
        this.txtAgnDOB = txtAgnDOB;
    }

    public RichInputDate getTxtAgnDOB() {
        return txtAgnDOB;
    }

    public void setTxtAgnMaritalStatus(RichSelectOneChoice txtAgnMaritalStatus) {
        this.txtAgnMaritalStatus = txtAgnMaritalStatus;
    }

    public RichSelectOneChoice getTxtAgnMaritalStatus() {
        return txtAgnMaritalStatus;
    }

    public void setTxtAgnIDNODocUsed(RichSelectOneChoice txtAgnIDNODocUsed) {
        this.txtAgnIDNODocUsed = txtAgnIDNODocUsed;
    }

    public RichSelectOneChoice getTxtAgnIDNODocUsed() {
        return txtAgnIDNODocUsed;
    }

    public void setTxtAgnQualification(RichSelectOneChoice txtAgnQualification) {
        this.txtAgnQualification = txtAgnQualification;
    }

    public RichSelectOneChoice getTxtAgnQualification() {
        return txtAgnQualification;
    }

    public void setTxtAgnBenStartDate(RichInputDate txtAgnBenStartDate) {
        this.txtAgnBenStartDate = txtAgnBenStartDate;
    }

    public RichInputDate getTxtAgnBenStartDate() {
        return txtAgnBenStartDate;
    }

    public void setTxtAgencySBUName(RichInputText txtAgencySBUName) {
        this.txtAgencySBUName = txtAgencySBUName;
    }

    public RichInputText getTxtAgencySBUName() {
        return txtAgencySBUName;
    }

    public void setTblAgencySBU(RichTable tblAgencySBU) {
        this.tblAgencySBU = tblAgencySBU;
    }

    public RichTable getTblAgencySBU() {
        return tblAgencySBU;
    }

    public void setTxtAgencySBUCode(RichInputText txtAgencySBUCode) {
        this.txtAgencySBUCode = txtAgencySBUCode;
    }

    public RichInputText getTxtAgencySBUCode() {
        return txtAgencySBUCode;
    }

    public void setTxtCommLevyApp(RichSelectOneChoice txtCommLevyApp) {
        this.txtCommLevyApp = txtCommLevyApp;
    }

    public RichSelectOneChoice getTxtCommLevyApp() {
        return txtCommLevyApp;
    }

    public void setTxtCommLevyRate(RichInputNumberSpinbox txtCommLevyRate) {
        this.txtCommLevyRate = txtCommLevyRate;
    }

    public RichInputNumberSpinbox getTxtCommLevyRate() {
        return txtCommLevyRate;
    }

    public void setTxtAgencyRegionName(RichInputText txtAgencyRegionName) {
        this.txtAgencyRegionName = txtAgencyRegionName;
    }

    public RichInputText getTxtAgencyRegionName() {
        return txtAgencyRegionName;
    }

    public void setTblRegion(RichTable tblRegion) {
        this.tblRegion = tblRegion;
    }

    public RichTable getTblRegion() {
        return tblRegion;
    }

    public void setTxtAgencyRegionCode(RichInputText txtAgencyRegionCode) {
        this.txtAgencyRegionCode = txtAgencyRegionCode;
    }

    public RichInputText getTxtAgencyRegionCode() {
        return txtAgencyRegionCode;
    }

    public void setBranchUnitPopUp(RichPopup branchUnitPopUp) {
        this.branchUnitPopUp = branchUnitPopUp;
    }

    public RichPopup getBranchUnitPopUp() {
        return branchUnitPopUp;
    }

    public void setTxtAgencyTaxAuthName(RichInputText txtAgencyTaxAuthName) {
        this.txtAgencyTaxAuthName = txtAgencyTaxAuthName;
    }

    public RichInputText getTxtAgencyTaxAuthName() {
        return txtAgencyTaxAuthName;
    }

    public void setTxtAgencyTaxAuthCode(RichInputText txtAgencyTaxAuthCode) {
        this.txtAgencyTaxAuthCode = txtAgencyTaxAuthCode;
    }

    public RichInputText getTxtAgencyTaxAuthCode() {
        return txtAgencyTaxAuthCode;
    }

    public void setTaxAuthorityTbl(RichTable taxAuthorityTbl) {
        this.taxAuthorityTbl = taxAuthorityTbl;
    }

    public RichTable getTaxAuthorityTbl() {
        return taxAuthorityTbl;
    }
    List<SelectItem> transferType = new ArrayList<SelectItem>();
    HashMap<String, PromoType> transferDesc = new HashMap();

    public List<SelectItem> getTransferTypes() {

        if (transferType != null) {
            transferType.clear();
        }
        if (transferDesc != null) {
            transferDesc.clear();
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;

        OracleCallableStatement statement = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;
        String status = null;
        Boolean value = true;
        try {
            conn = dbConnector.getDatabaseConnection();
            if (!GlobalCC.tableExists("tqc_promotion_types")) {
                return transferType;
            }
            String query1 =
                "SELECT prom_type,prom_desc,prom_prefix,prom_promote FROM tqc_promotion_types";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            rst = (OracleResultSet)stmt.executeQuery();
            while (rst.next()) {
                transferType.add(new SelectItem(rst.getString(1),
                                                rst.getString(2)));
                PromoType type = new PromoType();
                type.setType(rst.getString(1));
                type.setDesc(rst.getString(2));
                type.setPrefix(rst.getString(3));
                type.setPromote(rst.getString(4));
                transferDesc.put(rst.getString(1), type);
            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return transferType;
    }

    public void selectTransTypes(ValueChangeEvent valueChangeEvent) {
        String transType = GlobalCC.checkNullValues(sltTransType.getValue());
        session.setAttribute("prom_type", transType);
        //String label="Promote/Demote";
        PromoType type = (PromoType)transferDesc.get(transType);
        if (type != null) {
            session.setAttribute("prom_promote", type.getPromote());
            session.setAttribute("prom_prefix", type.getPrefix());
            // if("Y".equalsIgnoreCase(type.getPromote())) {
            //     label="Promote";
            //} else{
            //    label="Demote";
            //}
        }
        //cmbPromoteDemoteAgent.setText(label);

        //ADFUtils.findIterator("fetchReplacementMgrsIterator").executeQuery();
        // GlobalCC.refreshUI(cmbPromoteDemoteAgent);
        //GlobalCC.refreshUI(replaceMgrsTbl);

    }

    public String actionPromoteDemoteAgent() {


        Connection conn = null;
        CallableStatement stmt = null;
        String query = null;
        String demote = null;
        BigDecimal agnCode =
            GlobalCC.checkBDNullValues(session.getAttribute("agencyCode"));
        String prom_promote =
            GlobalCC.checkNullValues(session.getAttribute("prom_promote"));
        String type =
            GlobalCC.checkNullValues(session.getAttribute("prom_type"));
        String prom_prefix =
            GlobalCC.checkNullValues(session.getAttribute("prom_prefix"));
        String shortDesc =
            GlobalCC.checkNullValues(session.getAttribute("shortDesc"));
        String pdAction =
            "D"; //GlobalCC.checkNullValues(promotedemoteAction.getValue());

        if (pdAction == null) {
            GlobalCC.errorValueNotEntered("Select Promote/demote Action!");
            return null;
        }
        if (prom_prefix == null || shortDesc == null) {
            GlobalCC.errorValueNotEntered("Agent Short Description or Prom_type is empty!");
            return null;
        } else {

            shortDesc = shortDesc.substring(0, 1);


            if (GlobalCC.isNumeric(shortDesc)) {
                int promotionPrefix = Integer.parseInt(prom_prefix);
                int shortDescription = Integer.parseInt(shortDesc);


                if (pdAction.equalsIgnoreCase("P") &&
                    shortDescription <= promotionPrefix) {
                    //demotion
                    demote = "N";
                } else if (pdAction.equalsIgnoreCase("D") &&
                           shortDescription > promotionPrefix) {
                    //promotion
                    demote = "Y";
                } else {
                    String Action =
                        pdAction.equalsIgnoreCase("D") ? "Demoted" :
                        "Promoted";
                    GlobalCC.EXCEPTIONREPORTING("Agent is already " + Action);
                    return null;
                }
            } else {
                demote = "Y";
            }

        }


        if (agnCode == null) {
            GlobalCC.errorValueNotEntered("Please select Manager or Agent to demote/promote!");
            return null;
        }
        if (type == null) {
            GlobalCC.errorValueNotEntered("Please select Transfer Type!");
            return null;
        }
        /*if(newAgnCode==null){
                        GlobalCC.errorValueNotEntered("Please select Replacement manager!");
                        return null;
                } */
        try {
            DBConnector connection = new DBConnector();
            conn = connection.getDatabaseConnection();
            query =
                    "begin TQC_AGENCIES_PKG.promoteDemoteManager(?,?,?,?); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.setBigDecimal(1, agnCode);
            stmt.setString(2, null);
            stmt.setString(3, type);
            stmt.setString(4, "Y");
            stmt.execute();

            ADFUtils.findIterator("fetchAllAccountAgenciesInfoOnlyIterator").executeQuery();
            GlobalCC.refreshUI(tblAgencyInfo);
            stmt.close();
            conn.close();
            session.setAttribute("prom_type", null);
            session.setAttribute("prom_promote", null);
            ///txtReplaceMgr.setValue(null);
            sltTransType.setValue(null);
            //session.setAttribute("v_new_agn_code", null);


            GlobalCC.INFORMATIONREPORTING("Action Successfully " +
                                          cmbPromoteDemoteAgent.getText() +
                                          "d");

        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        }

        return null;
    }
    /*  public String selectReplaceMgrDetails() {

        Object key2 = replaceMgrsTbl.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;
        if (n != null) {
            session.setAttribute("v_new_agn_code", n.getAttribute("agnCode"));
            txtReplaceMgr.setValue(n.getAttribute("agnName"));
            GlobalCC.refreshUI(txtReplaceMgr);
        }
        GlobalCC.dismissPopUp("pt1", "ReplacementMgrPopUp");
        return null;
    }*/

    public void setLblReplacementMgr(RichOutputLabel lblReplacementMgr) {
        this.lblReplacementMgr = lblReplacementMgr;
    }

    public RichOutputLabel getLblReplacementMgr() {
        return lblReplacementMgr;
    }

    public void setTxtReplaceMgr(RichInputText txtReplaceMgr) {
        this.txtReplaceMgr = txtReplaceMgr;
    }

    public RichInputText getTxtReplaceMgr() {
        return txtReplaceMgr;
    }

    public void setCmbReplaceMgr(RichCommandButton cmbReplaceMgr) {
        this.cmbReplaceMgr = cmbReplaceMgr;
    }

    public RichCommandButton getCmbReplaceMgr() {
        return cmbReplaceMgr;
    }

    public void setCmbPromoteDemoteAgent(RichCommandButton cmbPromoteDemoteAgent) {
        this.cmbPromoteDemoteAgent = cmbPromoteDemoteAgent;
    }

    public RichCommandButton getCmbPromoteDemoteAgent() {
        return cmbPromoteDemoteAgent;
    }

    public void setReplaceMgrsTbl(RichTable replaceMgrsTbl) {
        this.replaceMgrsTbl = replaceMgrsTbl;
    }

    public RichTable getReplaceMgrsTbl() {
        return replaceMgrsTbl;
    }

    public void setSltTransType(RichSelectOneChoice sltTransType) {
        this.sltTransType = sltTransType;
    }

    public RichSelectOneChoice getSltTransType() {
        return sltTransType;
    }

    public void setPromotedemoteAction(RichSelectOneChoice promotedemoteAction) {
        this.promotedemoteAction = promotedemoteAction;
    }

    public RichSelectOneChoice getPromotedemoteAction() {
        return promotedemoteAction;
    }

    public void setTxtBnkBrn(RichInputText txtBnkBrn) {
        this.txtBnkBrn = txtBnkBrn;
    }

    public RichInputText getTxtBnkBrn() {
        return txtBnkBrn;
    }

    public void setTxtBnkAccOfficer(RichInputText txtBnkAccOfficer) {
        this.txtBnkAccOfficer = txtBnkAccOfficer;
    }

    public RichInputText getTxtBnkAccOfficer() {
        return txtBnkAccOfficer;
    }

    public void setTxtBnkAccNumber(RichInputText txtBnkAccNumber) {
        this.txtBnkAccNumber = txtBnkAccNumber;
    }

    public RichInputText getTxtBnkAccNumber() {
        return txtBnkAccNumber;
    }

    public void setTxtBnkAccName(RichInputText txtBnkAccName) {
        this.txtBnkAccName = txtBnkAccName;
    }

    public RichInputText getTxtBnkAccName() {
        return txtBnkAccName;
    }

    public void setTxtBnkTelNo(RichInputText txtBnkTelNo) {
        this.txtBnkTelNo = txtBnkTelNo;
    }

    public RichInputText getTxtBnkTelNo() {
        return txtBnkTelNo;
    }

    public void setTxtBnkCellNo(RichInputText txtBnkCellNo) {
        this.txtBnkCellNo = txtBnkCellNo;
    }

    public RichInputText getTxtBnkCellNo() {
        return txtBnkCellNo;
    }

    public void setSaveBankDetailsBtn(RichCommandButton saveBankDetailsBtn) {
        this.saveBankDetailsBtn = saveBankDetailsBtn;
    }

    public RichCommandButton getSaveBankDetailsBtn() {
        return saveBankDetailsBtn;
    }

    public void setTxtBnkCurrency(RichInputText txtBnkCurrency) {
        this.txtBnkCurrency = txtBnkCurrency;
    }

    public RichInputText getTxtBnkCurrency() {
        return txtBnkCurrency;
    }

    public void setTxtBnkDefault(RichSelectOneChoice txtBnkDefault) {
        this.txtBnkDefault = txtBnkDefault;
    }

    public RichSelectOneChoice getTxtBnkDefault() {
        return txtBnkDefault;
    }

    public String newBankDetailsAction() {
        if (session.getAttribute("bactAccCode") == null) {
            GlobalCC.INFORMATIONREPORTING("Please select An Agency First!");
            return null;
        } else {
            txtBnkAccOfficer.setValue(null);
            txtBnkBrn.setValue(null);
            txtBnkAccName.setValue(null);
            txtBnkAccNumber.setValue(null);
            txtBnkCellNo.setValue(null);
            txtBnkTelNo.setValue(null);
            txtBnkCurrency.setValue(null);
            bnkCurrencyDesc.setValue(null);
            txtBnkBrnCode.setValue(null);
            session.removeAttribute("bactUserCode");
            txtBnkDefault.setValue("N");
            txtBnkStatus.setValue("A");
            txtBnkIBAN.setValue(null);
            saveBankDetailsBtn.setText("Save");
            GlobalCC.showPop("pt1:bankdetailsPop");
            GlobalCC.refreshUI(bankdetailsPopUp);
        }
        return null;
    }

    public String editBankDetailsAction() {
        if (session.getAttribute("bactAccCode") == null) {
            GlobalCC.INFORMATIONREPORTING("Please select An Agency First!");
            return null;
        } else {
            Object key2 = tblBnkDetails.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                saveBankDetailsBtn.setText("Update");
                txtBnkAccOfficer.setValue(nodeBinding.getAttribute("BACT_ACCOUNT_OFFICER"));
                txtBnkBrn.setValue(nodeBinding.getAttribute("BACT_BANK_BRANCH"));
                txtBnkAccName.setValue(nodeBinding.getAttribute("BACT_NAME"));
                txtBnkAccNumber.setValue(nodeBinding.getAttribute("BACT_ACCOUNT_NO"));
                txtBnkCellNo.setValue(nodeBinding.getAttribute("BACT_CELL_NOS"));
                txtBnkTelNo.setValue(nodeBinding.getAttribute("BACT_TEL_NOS"));
                txtBnkDefault.setValue(nodeBinding.getAttribute("BACT_DEFAULT"));
                txtBnkCurrency.setValue(nodeBinding.getAttribute("BACT_CUR_CODE"));
                bnkCurrencyDesc.setValue(nodeBinding.getAttribute("BACT_CURRENCY"));
                txtBnkBrnCode.setValue(nodeBinding.getAttribute("BACT_BBR_CODE"));
                session.setAttribute("bactUserCode",
                                     nodeBinding.getAttribute("BACT_USER_CODE"));
                txtBnkIBAN.setValue(nodeBinding.getAttribute("BACT_IBAN"));
                txtBnkStatus.setValue(nodeBinding.getAttribute("BACT_STATUS"));

                GlobalCC.showPop("pt1:bankdetailsPop");
                GlobalCC.refreshUI(bankdetailsPopUp);

            }


        }
        return null;
    }

    public String deleteBankDetailsAction() {
        Connection conn = null;
        CallableStatement stmt = null;
        String query = null;

        if (session.getAttribute("bactAccCode") == null) {
            GlobalCC.INFORMATIONREPORTING("Please select An Agency First!");
            return null;
        }
        Object key2 = tblBnkDetails.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            String v_bact_code =
                GlobalCC.checkNullValues(nodeBinding.getAttribute("BACT_CODE"));
            DBConnector dbConnector = new DBConnector();
            conn = dbConnector.getDatabaseConnection();
            try {
                DBConnector connection = new DBConnector();
                conn = connection.getDatabaseConnection();
                query =
                        "begin tqc_setups_pkg.bank_details_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "D");
                stmt.setString(2, v_bact_code);
                stmt.setString(3, null);
                stmt.setString(4, null);
                stmt.setString(5, null);
                stmt.setString(6, null);
                stmt.setString(7, null);
                stmt.setString(8, null);
                stmt.setString(9, null);
                stmt.setString(10, null);
                stmt.setString(11, null);
                stmt.setString(12, null);
                stmt.setString(13, null);
                stmt.setString(14, null);
                stmt.execute();

                stmt.close();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Bank Details Successfully Deleted!");
                ADFUtils.findIterator("fetchBankAccountsIterator").executeQuery();
                GlobalCC.refreshUI(tblBnkDetails);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected!");
        }

        return null;
    }

    public String saveBankDetailsAction() {
        Connection conn = null;
        CallableStatement stmt = null;
        String query = null;
        String bnkBrn = GlobalCC.checkNullValues(txtBnkBrnCode.getValue());
        String bnkAccOfficer =
            GlobalCC.checkNullValues(session.getAttribute("bactUserCode"));
        String bnkAccNumber =
            GlobalCC.checkNullValues(txtBnkAccNumber.getValue());
        String bnkAccName = GlobalCC.checkNullValues(txtBnkAccName.getValue());
        String bnkTelNo = GlobalCC.checkNullValues(txtBnkTelNo.getValue());
        String bnkCellNo = GlobalCC.checkNullValues(txtBnkCellNo.getValue());
        String bnkCurrency =
            GlobalCC.checkNullValues(txtBnkCurrency.getValue());
        String bnkDefault = GlobalCC.checkNullValues(txtBnkDefault.getValue());
        String bactAccountType =
            GlobalCC.checkNullValues(session.getAttribute("bactAccountType"));
        String bactAccCode =
            GlobalCC.checkNullValues(session.getAttribute("bactAccCode"));
        String bnkIBAN = GlobalCC.checkNullValues(txtBnkIBAN.getValue());
        String bnkStatus = GlobalCC.checkNullValues(txtBnkStatus.getValue());
        String v_bact_code = null;
        String option = null;
        Rendering renderer = new Rendering();

        if (renderer.isAGENT_IBAN_REQUIRED() && bnkIBAN == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: IBAN is required!");
            return null;
        }


        if (saveBankDetailsBtn.getText().equalsIgnoreCase("update")) {
            option = "E";
            Object key2 = tblBnkDetails.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            v_bact_code =
                    GlobalCC.checkNullValues(nodeBinding.getAttribute("BACT_CODE"));
        } else if (saveBankDetailsBtn.getText().equalsIgnoreCase("Save")) {
            option = "A";
        }
        try {
            DBConnector connection = new DBConnector();
            conn = connection.getDatabaseConnection();
            query =
                    "begin tqc_setups_pkg.bank_details_prc(?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query);

            stmt.setString(1, option);
            stmt.setString(2, v_bact_code);
            stmt.setString(3, bnkAccName);
            stmt.setString(4, bnkAccNumber);
            stmt.setString(5, bnkBrn);
            stmt.setString(6, bnkAccOfficer);
            stmt.setString(7, bnkCellNo);
            stmt.setString(8, bnkTelNo);
            stmt.setString(9, bactAccountType);
            stmt.setString(10, bnkDefault);
            stmt.setString(11, bnkCurrency);
            stmt.setString(12, bactAccCode);
            stmt.setString(13, bnkIBAN);
            stmt.setString(14, bnkStatus);
            stmt.execute();

            stmt.close();
            conn.close();
            GlobalCC.INFORMATIONREPORTING("Bank Details Successfully Created!");
            GlobalCC.hidePopup("pt1:bankdetailsPop");
            ADFUtils.findIterator("fetchBankAccountsIterator").executeQuery();
            GlobalCC.refreshUI(tblBnkDetails);
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        }
        return null;
    }

    public String actionAcceptCurrency() {
        Object key2 = tblCurrencyPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBnkCurrency.setValue(nodeBinding.getAttribute("code"));
            bnkCurrencyDesc.setValue(nodeBinding.getAttribute("description"));
        }

        GlobalCC.refreshUI(txtBnkCurrency);
        GlobalCC.refreshUI(bnkCurrencyDesc);
        GlobalCC.hidePopup("pt1:currenciesPop");

        return null;
    }

    public String bankBranchSelected() {
        Object key2 = tblBankBranchPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtBnkBrn.setValue(nodeBinding.getAttribute("branchName"));
            txtBnkBrnCode.setValue(nodeBinding.getAttribute("bankBranchCode"));
            GlobalCC.refreshUI(txtBnkBrnCode);
            GlobalCC.refreshUI(txtBnkBrn);
        }
        GlobalCC.hidePopup("pt1:bankBranchPop");

        return null;
    }

    public String actionAcceptAccountOfficer() {


        Object key2 = tblAccountOfficer.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            txtBnkAccOfficer.setValue(nodeBinding.getAttribute("username"));
            session.setAttribute("bactUserCode",
                                 nodeBinding.getAttribute("userCode"));

            GlobalCC.refreshUI(txtBnkAccOfficer);
        }
        GlobalCC.hidePopup("pt1:accountOfficersPop");
        return null;
    }

    public void setBankdetailsPopUp(RichPopup bankdetailsPopUp) {
        this.bankdetailsPopUp = bankdetailsPopUp;
    }

    public RichPopup getBankdetailsPopUp() {
        return bankdetailsPopUp;
    }

    public void setTblBnkDetails(RichTable tblBnkDetails) {
        this.tblBnkDetails = tblBnkDetails;
    }

    public RichTable getTblBnkDetails() {
        return tblBnkDetails;
    }

    public void setBnkCurrencyDesc(RichInputText bnkCurrencyDesc) {
        this.bnkCurrencyDesc = bnkCurrencyDesc;
    }

    public RichInputText getBnkCurrencyDesc() {
        return bnkCurrencyDesc;
    }

    public void setTblCurrencyPop(RichTable tblCurrencyPop) {
        this.tblCurrencyPop = tblCurrencyPop;
    }

    public RichTable getTblCurrencyPop() {
        return tblCurrencyPop;
    }

    public void setTblBankBranchPop(RichTable tblBankBranchPop) {
        this.tblBankBranchPop = tblBankBranchPop;
    }

    public RichTable getTblBankBranchPop() {
        return tblBankBranchPop;
    }

    public void setTxtBnkBrnCode(RichInputText txtBnkBrnCode) {
        this.txtBnkBrnCode = txtBnkBrnCode;
    }

    public RichInputText getTxtBnkBrnCode() {
        return txtBnkBrnCode;
    }

    public void setTblAccountOfficer(RichTable tblAccountOfficer) {
        this.tblAccountOfficer = tblAccountOfficer;
    }

    public RichTable getTblAccountOfficer() {
        return tblAccountOfficer;
    }

    public void setTxtBnkIBAN(RichInputText txtBnkIBAN) {
        this.txtBnkIBAN = txtBnkIBAN;
    }

    public RichInputText getTxtBnkIBAN() {
        return txtBnkIBAN;
    }

    public void setTxtBnkStatus(RichSelectOneChoice txtBnkStatus) {
        this.txtBnkStatus = txtBnkStatus;
    }

    public RichSelectOneChoice getTxtBnkStatus() {
        return txtBnkStatus;
    }

    public void setTxtUpdatedBy(RichInputText txtUpdatedBy) {
        this.txtUpdatedBy = txtUpdatedBy;
    }

    public RichInputText getTxtUpdatedBy() {
        return txtUpdatedBy;
    }

    public void setTxtUpdatedOn(RichInputDate txtUpdatedOn) {
        this.txtUpdatedOn = txtUpdatedOn;
    }

    public RichInputDate getTxtUpdatedOn() {
        return txtUpdatedOn;
    }

    public void setAgentRecDocsLov(RichTable agentRecDocsLov) {
        this.agentRecDocsLov = agentRecDocsLov;
    }

    public RichTable getAgentRecDocsLov() {
        return agentRecDocsLov;
    }

    public String actionNewRequiredDoc() {
        if (session.getAttribute("agencyCode") != null) {
            txtDocCodePop.setValue(null);
            txtReqDocCodePop.setValue(null);
            txtReqDocNamePop.setValue(null);
            txtDocAgnCodePop.setValue(session.getAttribute("agentCode"));

            session.setAttribute("agentDocSubmitted", "N");
            session.setAttribute("agentDocId", null);

            txtDocDateSubmittedPop.setValue(null);
            txtDocRefNumPop.setValue(null);
            txtDocRemarkPop.setValue(null);
            txtDocUserReceivedPop.setValue(session.getAttribute("Username"));

            btnSaveRequiredDoc.setText("Save");

            // Open the popup dialog for required documents
            GlobalCC.showPopup("pt1:requiredDocsPop");

        } else {
            GlobalCC.INFORMATIONREPORTING("No Agent Selected:");
        }
        return null;
    }

    public String actionEditRequiredDoc() {
        Object key2 = agentRecDocsLov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDocCodePop.setValue(nodeBinding.getAttribute("CDOCR_CODE"));
            txtReqDocCodePop.setValue(nodeBinding.getAttribute("CDOCR_RDOC_CODE"));
            txtReqDocNamePop.setValue(nodeBinding.getAttribute("ROC_DESC"));
            txtDocAgnCodePop.setValue(nodeBinding.getAttribute("CDOCR_CLNT_CODE"));

            session.setAttribute("agentDocSubmitted",
                                 nodeBinding.getAttribute("CDOCR_SUBMITED"));
            session.setAttribute("agentDocId",
                                 nodeBinding.getAttribute("CDOCR_DOCID"));

            txtDocDateSubmittedPop.setValue(nodeBinding.getAttribute("CDOCR_DATE_S"));
            txtDocRefNumPop.setValue(nodeBinding.getAttribute("CDOCR_REF_NO"));
            txtDocRemarkPop.setValue(nodeBinding.getAttribute("CDOCR_RMRK"));
            txtDocUserReceivedPop.setValue(nodeBinding.getAttribute("CDOCR_USER_RECEIVD"));

            btnSaveRequiredDoc.setText("Edit");

            // Open the popup dialog for required documents
            GlobalCC.showPop("pt1:requiredDocsPop");

        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String actiondeleteRequiredDoc() {
        Object key2 = agentRecDocsLov.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            GlobalCC.showPopup("pt1:confirmDeleteClientRequiredDocs");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }

        return null;
    }

    public void setTblReqDocsList(RichTable tblReqDocsList) {
        this.tblReqDocsList = tblReqDocsList;
    }

    public RichTable getTblReqDocsList() {
        return tblReqDocsList;
    }

    public String actionAcceptRequireddDoc() {
        Object key2 = tblReqDocsList.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtReqDocCodePop.setValue(nodeBinding.getAttribute("code"));
            txtReqDocNamePop.setValue(nodeBinding.getAttribute("description"));
            GlobalCC.refreshUI(txtReqDocNamePop);
            // Open the popup dialog for required documents
            GlobalCC.showPopup("pt1:requiredDocsPop");
        }
        System.out.println("txtReqDocCodePop " + txtReqDocCodePop.getValue());
        GlobalCC.dismissPopUp("pt1", "requiredDocsListPop");
        return null;
    }

    public String actionRejectRequiredDoc() {
        // Open the popup dialog for required documents
        GlobalCC.showPopup("pt1:requiredDocsPop");
        return null;
    }

    public void setTxtDocCodePop(RichInputText txtDocCodePop) {
        this.txtDocCodePop = txtDocCodePop;
    }

    public RichInputText getTxtDocCodePop() {
        return txtDocCodePop;
    }

    public void setTxtReqDocCodePop(RichInputText txtReqDocCodePop) {
        this.txtReqDocCodePop = txtReqDocCodePop;
    }

    public RichInputText getTxtReqDocCodePop() {
        return txtReqDocCodePop;
    }

    public void setTxtReqDocNamePop(RichInputText txtReqDocNamePop) {
        this.txtReqDocNamePop = txtReqDocNamePop;
    }

    public RichInputText getTxtReqDocNamePop() {
        return txtReqDocNamePop;
    }

    public void setTxtDocAgnCodePop(RichInputText txtDocAgnCodePop) {
        this.txtDocAgnCodePop = txtDocAgnCodePop;
    }

    public RichInputText getTxtDocAgnCodePop() {
        return txtDocAgnCodePop;
    }

    public void setTxtDocSubmittedPop(RichSelectOneChoice txtDocSubmittedPop) {
        this.txtDocSubmittedPop = txtDocSubmittedPop;
    }

    public RichSelectOneChoice getTxtDocSubmittedPop() {
        return txtDocSubmittedPop;
    }

    public void setTxtDocDateSubmittedPop(RichInputDate txtDocDateSubmittedPop) {
        this.txtDocDateSubmittedPop = txtDocDateSubmittedPop;
    }

    public RichInputDate getTxtDocDateSubmittedPop() {
        return txtDocDateSubmittedPop;
    }

    public void setTxtDocRefNumPop(RichInputText txtDocRefNumPop) {
        this.txtDocRefNumPop = txtDocRefNumPop;
    }

    public RichInputText getTxtDocRefNumPop() {
        return txtDocRefNumPop;
    }

    public void setTxtDocRemarkPop(RichInputText txtDocRemarkPop) {
        this.txtDocRemarkPop = txtDocRemarkPop;
    }

    public RichInputText getTxtDocRemarkPop() {
        return txtDocRemarkPop;
    }

    public void setTxtDocUserReceivedPop(RichInputText txtDocUserReceivedPop) {
        this.txtDocUserReceivedPop = txtDocUserReceivedPop;
    }

    public RichInputText getTxtDocUserReceivedPop() {
        return txtDocUserReceivedPop;
    }

    public void setBtnSaveRequiredDoc(RichCommandButton btnSaveRequiredDoc) {
        this.btnSaveRequiredDoc = btnSaveRequiredDoc;
    }

    public RichCommandButton getBtnSaveRequiredDoc() {
        return btnSaveRequiredDoc;
    }

    public String actionEditAgentRequiredDoc() {
        return actionSaveRequiredDoc();
    }

    public String actionSaveRequiredDoc() {
        // Check if the user wishes to SAVE or UPDATE
        String action = "A";
        if (btnSaveRequiredDoc.getText().equals("Edit")) {
            // Proceed to edit
            action = "E";
        } else {
            action = "A";
        }
        BigDecimal docCode =
            GlobalCC.checkBDNullValues(txtDocCodePop.getValue());
        String docReqDocCode =
            GlobalCC.checkNullValues(txtReqDocCodePop.getValue());
        String docAgentCode =
            GlobalCC.checkNullValues(session.getAttribute("agentCode"));
        String docSubmitted =
            GlobalCC.checkNullValues(session.getAttribute("agentDocSubmitted"));
        String docDateSubmit =
            GlobalCC.checkNullValues(txtDocDateSubmittedPop.getValue());
        String docRefNum =
            GlobalCC.checkNullValues(txtDocRefNumPop.getValue());
        String docRemark =
            GlobalCC.checkNullValues(txtDocRemarkPop.getValue());

        if (docReqDocCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Required Document is Empty!");
            return null;

        }
        if (docAgentCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: You need to select a client!");
            return null;
        }
        if (docDateSubmit == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Date Submitted!");
            return null;
        }
        String Query =
            "begin TQC_WEB_PKG.save_agent_docs(?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {

            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, action);
            cst.setBigDecimal(2, docCode);
            cst.setString(3, docReqDocCode);
            cst.setString(4, docAgentCode);
            cst.setString(5, docSubmitted);
            cst.setDate(6, GlobalCC.extractDate(txtDocDateSubmittedPop));
            cst.setString(7, docRefNum);
            cst.setString(8, docRemark);
            cst.setString(9, (String)session.getAttribute("Username"));
            cst.setString(10, (String)session.getAttribute("agentDocId"));
            cst.registerOutParameter(11, OracleTypes.VARCHAR);
            cst.registerOutParameter(12, OracleTypes.VARCHAR);


            cst.execute();
            String status = GlobalCC.checkNullValues(cst.getString(11));
            String msg = GlobalCC.checkNullValues(cst.getString(12));
            cst.close();
            conn.close();
            if (!"S".equals(status)) {
                GlobalCC.errorValueNotEntered(msg);
                return null;
            }
            ADFUtils.findIterator("fetchAgentRequiredDocsIterator").executeQuery();
            GlobalCC.refreshUI(agentRecDocsLov);
            GlobalCC.dismissPopUp("pt1", "requiredDocsPop");
            GlobalCC.INFORMATIONREPORTING(msg);
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setAgentRecDocstab(RichShowDetailItem agentRecDocstab) {
        this.agentRecDocstab = agentRecDocstab;
    }

    public RichShowDetailItem getAgentRecDocstab() {
        return agentRecDocstab;
    }


    public void setUpFile(RichInputFile upFile) {
        this.upFile = upFile;
    }

    public RichInputFile getUpFile() {
        return upFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public long getFilesize() {
        return filesize;
    }

    public static void setFileContent(String fileContent) {
        MainAccountsBacking.fileContent = fileContent;
    }

    public static String getFileContent() {
        return fileContent;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public void uploadDoc(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {

            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            this.uploadedFile = _file;
            this.filename = _file.getFilename();
            this.filesize = _file.getLength();
            this.filetype = _file.getContentType();
            try {
                CSVtoADFTableProcessor CSVManip = new CSVtoADFTableProcessor();
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String mimeType =
                    fileNameMap.getContentTypeFor(_file.getFilename());
                CSVManip.uploadAgentDocuments(uploadedFile.getInputStream(),
                                              _file.getFilename(), mimeType);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
            GlobalCC.refreshUI(requiredDocsBox);
        }
    }

    public void runOffChanged(ValueChangeEvent valueChangeEvent) {

        String runOffVal =
            GlobalCC.checkNullValues(valueChangeEvent.getOldValue());

        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            runOffVal =
                    GlobalCC.checkNullValues(valueChangeEvent.getNewValue());
        }
        if ("Y".equals(runOffVal)) {
            txtAgencyStatusRemarks.setDisabled(false);
            txtAgencyStatusRemarks.setRequired(true);
        } else {
            txtAgencyStatusRemarks.setDisabled(true);
            txtAgencyStatusRemarks.setRequired(false);
        }

        GlobalCC.refreshUI(txtAgencyStatusRemarks);
    }

    public void setTxtAgencyIRARegNo(RichInputText txtAgencyIRARegNo) {
        this.txtAgencyIRARegNo = txtAgencyIRARegNo;
    }

    public RichInputText getTxtAgencyIRARegNo() {
        return txtAgencyIRARegNo;
    }

    public void actionLoadByAgentCode() {

        String agentCode =
            GlobalCC.checkNullValues(session.getAttribute("AgentCode"));
        HashMap<String, String> m = null;
        String client =
            GlobalCC.checkNullValues(session.getAttribute("CLIENT"));

        if (agentCode == null) {
            System.err.println("Agent Code Undefined!");
            return; //please define ClientCode
        }
        MainAccountsDAO accountsDao = new MainAccountsDAO();
        DBConnector dbConnector = DBConnector.getInstance();
        OracleCallableStatement stmt = null;
        Connection conn = null;
        OracleResultSet rs = null;

        try {

            String query =
                "begin ? := tqc_agencies_cursors.fetch_agent_details(?); end;";

            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.setString(2, agentCode);
            stmt.execute();
            
          
            
           TurnQuest.view.models.Agent agent =accountsDao.fetchAgentByAgentCode(new BigDecimal(agentCode));
            
            rs = (OracleResultSet)stmt.getObject(1);
            if (rs.next()) {

                //--TO COMPLETE
                // To solve the session problem
                session.setAttribute("PayeeType", "A");

                String shortDesc =
                    GlobalCC.checkNullValues(agent.getShtDesc());
                String name =
                    GlobalCC.checkNullValues(agent.getName());

                session.setAttribute("agencyCode", agentCode);
                session.setAttribute("AgentCode", agentCode);
                session.setAttribute("agentCode", agentCode);
                session.setAttribute("P_AGN_CODE", agentCode);
                session.setAttribute("bactAccCode", agentCode);

                System.out.println("agencyCode: " + agentCode);

                session.setAttribute("shortDesc", shortDesc);

                session.setAttribute("agencyName", name);
                session.setAttribute("agentName", name);

                btnDeleteAgency.setDisabled(false);
                GlobalCC.refreshUI(btnDeleteAgency);
                // to avoid the session problem
                hiddenAccountCode.setValue(agentCode);
                GlobalCC.refreshUI(panelDetailSystems);

                txtAgencyCode.setValue(agentCode);

                txtAccountTypeCode.setValue(agent.getActCode());

                session.setAttribute("accountTypeCode",agent.getActCode());

                txtMainAccountType.setValue(rs.getString("ACT_ACCOUNT_TYPE"));

                txtShortDesc.setValue(agent.getShtDesc());
                session.setAttribute("agentCodeType",agent.getMainAgnCode());
                
                txtAgent.setValue(UtilDAO.fetchAgentName(GlobalCC.checkNullValues(agent.getMainAgnCode())));
                txtAgencyName.setValue(agent.getName());
                txtAgencyName.setDisabled(false);
                txtAgencyPhysicalAddress.setValue(agent.getPhysicalAddress());
                txtAgencyPostalAddress.setValue(agent.getPostalAddress());
                txtAgencyCountryCode.setValue(agent.getCouCode());

                session.setAttribute("countryCode",
                                     txtAgencyCountryCode.getValue());
                session.setAttribute("bru_code",agent.getBruCode());
                session.setAttribute("accountManagerCode",
                                     rs.getString("agn_account_manager"));

                txtAgencyCountryName.setValue(UtilDAO.fetchCountryName(GlobalCC.checkNullValues(agent.getCouCode())));

                txtAgencyTownCode.setValue(agent.getTwnCode());
                txtAgencyTownName.setValue(UtilDAO.fetchTownName(GlobalCC.checkNullValues(agent.getTwnCode())));
                txtAgencyEmail.setValue(agent.getEmailAddress());
                txtAgencyWebAddress.setValue(agent.getWebAddress());
                txtAgencyPostalCode.setValue(agent.getZip());
                txtContactPerson.setValue(agent.getContactPerson());
                txtContactTitle.setValue(agent.getContactTitle());
                txtAgencyPhone1.setValue(agent.getTel1());
                txtAgencyPhone2.setValue(agent.getTel2());
                txtAgencyFax.setValue(agent.getFax());
                txtAgencyAccountNumber.setValue(agent.getAccNo());
                txtAgencyPIN.setValue(agent.getPin());
                txtAgencyCommission.setValue(agent.getAgentCommission());
                txtCreditAllowed.setValue(agent.getCreditAllowed());
                txtAgencyWthTx.setValue(agent.getAgentWhtTax());
                txtAgencyPrintdebitNote.setValue(agent.getPrintDbnote());

                if (rs.getString("AGN_STATUS") == null) {
                    txtAgencyStatus.setValue("ACTIVE");
                } else {
                    txtAgencyStatus.setValue(agent.getStatus());
                }
                String status =
                    GlobalCC.checkNullValues(agent.getStatus());
                session.setAttribute("AgentStatus", status);


                session.setAttribute("agentIprsValidated",
                                     rs.getString("agn_iprs_validated"));


                txtDateCreated.setValue(agent.getDateCreated());
                txtCreatedBy.setValue(agent.getCreatedBy());
                txtRegistartionCode.setValue(agent.getRegCode());
                txtCommReserveRate.setValue(agent.getCommReserveRate());
                txtAnnualBudget.setValue(agent.getAnnualBudget());
                txtStaEffectiveDate.setValue(agent.getStatusEffDate());
                txtCreditPeriod.setValue(agent.getCreditPeriod());
                txtCommStatEffectiveDate.setValue(agent.getCommStatEffDt());
                txtCommStatusDate.setValue(agent.getCommStatusDt());
                txtCommAllowed.setValue(agent.getCommAllowed());
                txtAgencyChecked.setValue(agent.getChecked());
                txtAgencyCheckedBy.setValue(agent.getCheckedBy());
                txtAgencyCheckDate.setValue(agent.getCheckDate());
                txtCompCommArrears.setValue(agent.getCompCommArrears());
                txtAgencyReinsurer.setValue(agent.getReinsurer());
                txtAgencyBranchCode.setValue(agent.getBrnCode());
                session.setAttribute("branchCode",
                                     agent.getBrnCode());
                System.out.println("aabranchCode: " +
                                   session.getAttribute("branchCode"));
                txtAgencyBranchName.setValue(UtilDAO.fetchBranchName(GlobalCC.checkNullValues(agent.getBrnCode())));
                txtStatusDesc.setValue(agent.getStatusDesc());
                txtAgencyIDNum.setValue(agent.getIdNo());
                txtAgnIDNODocUsed.setValue(agent.getIdNoDocUsed());
                txtAgencyContractCode.setValue(agent.getConCode());
                txtAgencyAgentCode.setValue(agent.getAgnCode());

                txtBranchUnits.setValue(UtilDAO.fetchUnitName(GlobalCC.checkNullValues(agent.getBruCode())));
                session.setAttribute("bruCode", agent.getBruCode());

                txtIraRegNumber.setValue(agent.getIraRegno());
                txtIssueCert.setValue(agent.getAllocateCert());
                txtBouncedCheque.setValue(agent.getBouncedChq());
                txtModeOfComm.setValue(agent.getDefaultCommMode());

                session.setAttribute("bpnCode",agent.getBpnCode()); 
                txtBussinessPersons.setValue(UtilDAO.fetchBussinesPerson(GlobalCC.checkNullValues(agent.getBpnCode())));

                txtCommLevyRate.setValue(agent.getCommLevyRate());
                txtCommLevyApp.setValue(agent.getCommLevyApp());

                txtAgencyRegionName.setValue(agent.getBrrName());
                txtAgencyRegionCode.setValue(agent.getBrrCode());

                txtAgencyTaxAuthName.setValue(agent.getAuthName());
                txtUpdatedBy.setValue(agent.getUpdatedBy());
                txtUpdatedOn.setValue(agent.getUpdatedOn());
                
                GlobalCC.refreshUI(txtCommLevyRate);
                GlobalCC.refreshUI(txtCommLevyApp);
                
                txtAgentType.setValue(agent.getAgentType());
                txtAgentGroup.setValue(agent.getGroup());

                session.setAttribute("zipCode", rs.getString("cou_zip_code"));
                if ("Y".equals(agent.getCreditAllowed())) {
                    txtCreditLimit.setVisible(true);
                    txtCreditLimit.setValue(agent.getCreditLimit());
                } else {
                    txtCreditLimit.setVisible(false);
                    txtCreditLimit.setValue(null);
                }

                /**
                                  * Get Prefix and ZipCode
                                  */
                String prefix = null;
                String smNo = null;
                int t = 0;
                if ( agent.getSmsTel() != null) {
                    if (rs.getString("cou_zip_code") != null) {
                        prefix =
                                agent.getSmsTel().replace(rs.getString("cou_zip_code"),
                                                                    "");
                        prefix = prefix.replace("+", "0");
                        if (txtSmsPrefix.isVisible()) {
                            smNo = prefix.substring(4);
                            prefix = prefix.substring(0, 4);
                        } else {
                            smNo = prefix;
                        }
                        // List prefixArray = new ArrayList();
                        //                        if (session.getAttribute("mobilePrefix") != null) {
                        //                            prefixArray =
                        //                                    (List)session.getAttribute("mobilePrefix");
                        //                            int k = 0;
                        //
                        //                            while (k < prefixArray.size()) {
                        //                                if (prefix.startsWith((String)prefixArray.get(k))) {
                        //                                    smNo =
                        //prefix.replace((CharSequence)prefixArray.get(k), "");
                        //                                    prefix = (String)prefixArray.get(k);
                        //                                    t = k;
                        //                                }
                        //                                k++;
                        //                            }
                        //                            prefixArray = null;
                        //                        }
                    }
                } else {
                    /*if (rs.getString("couZipCode") != null &&
                                                        rs.getString("sms") != null) {
                                                        prefix =
                                                                        rs.getString("sms").toString().replace(rs.getString("couZipCode").toString(),
                                                                                                                                                                           "");
                                                } else {
                                                        if (rs.getString("sms") != null) {
                                                                prefix =
                                                                                rs.getString("sms").toString();
                                                        } else {
                                                                prefix = "";
                                                        }
                                                }
                                                prefix = prefix.replace("+", "0");
                                                if (txtSmsPrefix.isVisible()) {
                                                        smNo = prefix.substring(4);
                                                        prefix = prefix.substring(0, 4);
                                                } else {
                                                        smNo = prefix;
                                                }*/


                }

                // if(rs.getString("couZipCode"))

                txtSmsPrefix.setValue(prefix);
                txtAgencySms.setValue(smNo);
                /* upDateQualificationsRecomms((BigDecimal)row.getAttribute("qualId"),
                                                                        comments, null);*/

                prefix = null;
                smNo = null;
                if (agent.getTelPay() != null) {
                    if (rs.getString("cou_zip_code") != null) {
                        prefix =
                                agent.getTelPay().replace(rs.getString("cou_zip_code"),
                                                                    "");
                        prefix = prefix.replace("+", "0");
                        if (txtTelPayPrefix.isVisible()) {
                            smNo = prefix.substring(4);
                            prefix = prefix.substring(0, 4);
                        } else {
                            smNo = prefix;
                        }
                    }
                }

                // if(rs.getString("couZipCode"))
                txtTelPay.setValue(smNo);
                txtTelPayPrefix.setValue(prefix);

                /* upDateQualificationsRecomms((BigDecimal)row.getAttribute("qualId"),
                                                                  comments, null);*/
 
                // txtAgencySms.setValue(rs.getString("sms"));

                txtAgencyHoldingCompanyCode.setValue(agent.getAgnCode());
                txtAgencyHoldingCompanyName.setValue(UtilDAO.fetchAgentName(GlobalCC.checkNullValues(agent.getAgnCode())));
                txtAgencySectorCode.setValue(agent.getSecCode());
                txtAgencySectorName.setValue(UtilDAO.fetchSectorName(GlobalCC.checkNullValues(agent.getSecCode())));
                txtAgencyClassCode.setValue(agent.getAgncClassCode());
                txtAgencyClassName.setValue(UtilDAO.fetchAgencyClassName(agent.getAgncClassCode()));
                session.setAttribute("brnCode", agent.getBrnCode());
                txtAgencyExpiriyDate.setValue(agent.getExpiryDate());
                txtAgencyLicenseNum.setValue(agent.getLicenseNo());
                txtAgencyRunoff.setValue(agent.getRunoff());
                txtAgencyLicensed.setValue(agent.getLicensed());
                txtLicenseGracePeriod.setValue(agent.getLicenseGracePr());
                txtOldAccountNum.setValue(agent.getOldAccNo());
                txtAgencySBUCode.setValue(agent.getSbuCode());
                txtAgencySBUName.setValue(UtilDAO.fetchSBUName(GlobalCC.checkNullValues(agent.getSbuCode())));
                txtAgencyStatusRemarks.setValue(agent.getStatusRemarks());
                txtBankCode.setValue(rs.getString("bnk_code"));
                txtBankName.setValue(rs.getString("bnk_bank_name"));
                txtBankBranchCode.setValue(agent.getBbrCode());
                txtBankBranch.setValue(UtilDAO.fetchBankBranchName(GlobalCC.checkNullValues(agent.getBbrCode())));
                txtAccountNo.setValue(agent.getBankAccNo());
                txtPrefix.setValue(agent.getUniquePrefix());
                txtAStateCode.setValue(agent.getStateCode());
                session.setAttribute("stateCode", txtAStateCode.getValue());
                txtAStateName.setValue(rs.getString("sts_name"));
                txtCreditRting.setValue(agent.getCrdtRting());
                txtSacco.setValue(rs.getString("clnt_name"));
                txtMarketer.setValue(UtilDAO.fetchAgentName(GlobalCC.checkNullValues(agent.getAccountManager())));
                txtAgencyHoldingCompanyCode.setValue(agent.getAhcCode());
                txtAgencyHoldingCompanyName.setValue(UtilDAO.fetchHoldingCompanyName(GlobalCC.checkNullValues(agent.getAhcCode())));
                txtVatApp.setValue(agent.getVatApplicable());
                txtWithTax.setValue(agent.getWhtaxApplicable());
                txtFrequencyofPayment.setValue(agent.getPaymentFreq());

                txtModeofPayment.setValue(agent.getDefaultCpmMode()); 

                session.setAttribute("pymtMode",agent.getDefaultCpmMode());
                session.setAttribute("PM_MODE",agent.getDefaultCpmMode());

                txtPmntDtlsValidated.setValue(agent.getPymtValidated());

                // System.out.println("cpmMode " +rs.getString("agn_default_cpm_mode"));
                session.setAttribute("cpmModeDesc",agent.getCpmModeDesc());
 
                //-------------------------------------------------TESTT


                //------------------------------------------------------
                txtRating.setValue(rs.getString("ors_desc"));
                txtRatingOrg.setValue(rs.getString("rorg_desc"));
                session.setAttribute("rorgCode",agent.getRorgCode());
                session.setAttribute("orsCode", agent.getOrsCode());

                if ("6".equals(agent.getActCode())) {
                    txtLocalInt.setVisible(true);
                    txtLocalInt.setValue(agent.getLocalInternational());
                } else {
                    txtLocalInt.setVisible(false);
                }

                if (rs.getString("prom_transaction_type") != null) {
                    /*
                                        if (rs.getString("transType").toString().equalsIgnoreCase("AGP")) {
                                                txtEffectiveDate.setVisible(true);
                                                txtEffectiveDate.setValue(rs.getString("promTransDate"));
                                                txtAgentChoices.setVisible(true);
                                                txtAgentChoices.setValue(rs.getString("transType"));
                                                txtTransactionType.setVisible(true);
                                                sltTransType.setValue(rs.getString("promDemType"));
                                                txtBranchName.setVisible(true);
                                                txtBranchName.setValue(rs.getString("brnName"));
                                                txtAgencyPrefix.setValue(rs.getString("agencyPrefix"));
                                                txtUnitPrefix.setValue(rs.getString("unitPrefix"));
                                                txtAgencySeq.setValue(rs.getString("agencySeqNumber"));
                                                txtPrecontractCode.setValue(rs.getString("precontractCode"));
                                                clbPromote.setVisible(true);
                                                // clbDemote.setVisible(true);
                                                sltTransType.setVisible(true);
                                                lblBranch.setVisible(true);
                                                clbBranchDrop.setVisible(true);
                                                sltTransTypeDem.setVisible(false);
                                                lblAgencyBranch.setVisible(false);
                                                txtUnitManager.setVisible(false);
                                                clbUnitManager.setVisible(false);
                                                clbDemoteCmb.setVisible(false);
                                                // clbAuthDemotion.setVisible(false);
                                                lblEffectiveDate.setVisible(true);
                                                txtTransactionType.setVisible(true);
                                                txtAgencyPrefix.setVisible(true);
                                                txtUnitPrefix.setVisible(true);
                                                txtAgencySeq.setVisible(true);
                                                lblAgencyPrefix.setVisible(true);
                                                lblUnitPrefix.setVisible(true);
                                                lblAgencySeq.setVisible(true);
                                                txtPrecontractCode.setVisible(true);
                                                precontractCode.setVisible(true);
                                                GlobalCC.refreshUI(txtAgencyPrefix);
                                                GlobalCC.refreshUI(txtUnitPrefix);
                                                GlobalCC.refreshUI(txtAgencySeq);
                                                GlobalCC.refreshUI(lblAgencyPrefix);
                                                GlobalCC.refreshUI(lblUnitPrefix);
                                                GlobalCC.refreshUI(lblAgencySeq);
                                                GlobalCC.refreshUI(txtPrecontractCode);
                                                GlobalCC.refreshUI(precontractCode);
                                        } else if (rs.getString("transType").toString().equalsIgnoreCase("AGD")) {
                                                txtAgentChoices.setVisible(true);
                                                txtAgentChoices.setValue(rs.getString("transType"));
                                                sltTransTypeDem.setVisible(true);
                                                sltTransTypeDem.setValue(rs.getString("promDemType"));
                                                txtUnitManager.setVisible(true);
                                                txtUnitManager.setValue(rs.getString("braName"));

                                                clbPromote.setVisible(false);
                                                //  clbDemote.setVisible(false);
                                                txtEffectiveDate.setVisible(true);
                                                txtBranchName.setVisible(false);
                                                sltTransType.setVisible(false);
                                                lblBranch.setVisible(false);
                                                clbBranchDrop.setVisible(false);

                                                sltTransTypeDem.setVisible(true);
                                                lblAgencyBranch.setVisible(true);
                                                txtUnitManager.setVisible(true);
                                                clbUnitManager.setVisible(true);
                                                clbDemoteCmb.setVisible(true);
                                                //  clbAuthDemotion.setVisible(true);
                                                txtTransactionType.setVisible(true);
                                                lblEffectiveDate.setVisible(true);
                                                txtAgencyPrefix.setVisible(false);
                                                txtUnitPrefix.setVisible(true);
                                                txtAgencySeq.setVisible(false);
                                                lblAgencyPrefix.setVisible(false);
                                                lblUnitPrefix.setVisible(true);
                                                lblAgencySeq.setVisible(false);
                                                txtPrecontractCode.setVisible(true);
                                                precontractCode.setVisible(true);
                                                GlobalCC.refreshUI(txtPrecontractCode);
                                                GlobalCC.refreshUI(precontractCode);
                                                GlobalCC.refreshUI(txtAgencyPrefix);
                                                GlobalCC.refreshUI(txtUnitPrefix);
                                                GlobalCC.refreshUI(txtAgencySeq);
                                                GlobalCC.refreshUI(lblAgencyPrefix);
                                                GlobalCC.refreshUI(lblUnitPrefix);
                                                GlobalCC.refreshUI(lblAgencySeq);
                                        }
                                */
                }
                
                
                // chcek credit limit 
                Authorization auth = new Authorization();
                String process = "AMA";
                String processArea = "CRLMT";
                String processSubArea = "CRDET";
                String AccessGranted =
                    auth.checkUserRights(process, processArea, processSubArea, null, null);
                if(AccessGranted.equalsIgnoreCase("Y")){
                    txtCreditAllowed.setDisabled(false);
                    txtCreditLimit.setDisabled(false);
                } else {
                    txtCreditAllowed.setDisabled(true);
                    txtCreditLimit.setDisabled(true);
                }
                // end check credit limit
                

                Object couAdminUnit = rs.getString("cou_admin_reg_type");
                if (couAdminUnit == null) {
                    pnmsgAdminRegionName.setVisible(false);
                    pnmsgTownName.setVisible(false);
                } else {
                    pnmsgAdminRegionName.setVisible(true);
                    pnmsgTownName.setVisible(true);
                    pnmsgAdminRegionName.setLabel(GlobalCC.formatAdminUnitSingular(couAdminUnit) +
                                                  ":");
                    dlgAdminRegionList.setTitle(GlobalCC.formatAdminUnitPlural(couAdminUnit) +
                                                " List");
                }
                GlobalCC.refreshUI(txtModeofPayment);
                ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
                GlobalCC.refreshUI(txtSmsPrefix);
                // Refresh the agency details panel
                GlobalCC.refreshUI(panelAgencyDetails);
                GlobalCC.refreshUI(txtMarketer);
                // Agencies Accounts
                ADFUtils.findIterator("fetchAgencyAccountsIterator").executeQuery();
                GlobalCC.refreshUI(tblAgencyAccounts);

                // Get the Agents registration details
                ADFUtils.findIterator("findAgentsRegistrationIterator").executeQuery();
                GlobalCC.refreshUI(registrationTable);

                // Get the Agency Directors
                ADFUtils.findIterator("findAgentsDirectorsIterator").executeQuery();
                GlobalCC.refreshUI(agentDirectorsTable);

                // Get the Agents referees
                ADFUtils.findIterator("findAgentsRefereesIterator").executeQuery();
                GlobalCC.refreshUI(agencyRefereeTable);

                // Get the user systems
                ADFUtils.findIterator("fetchUnassignedAccountSystemsIterator").executeQuery();
                GlobalCC.refreshUI(treeUnassignedSystems);

                ADFUtils.findIterator("fetchAllAgencyAssignedSystemsIterator").executeQuery();
                GlobalCC.refreshUI(tblAssignedAgencySystems);

                // Get the User web accounts
                ADFUtils.findIterator("findwebUserAccountsIterator").executeQuery();
                GlobalCC.refreshUI(webUsersTable);
                ADFUtils.findIterator("fetchAgentRequiredDocsIterator").executeQuery();
                GlobalCC.refreshUI(agentRecDocsLov);
                //Payee Accounts
                ADFUtils.findIterator("fetchAllBussinessPeopleIterator").executeQuery();

                GlobalCC.refreshUI(btnAuthorizeAgent);
                GlobalCC.refreshUI(btnMakeReadyAgent);
                GlobalCC.refreshUI(btnSuspendAgent);
                GlobalCC.refreshUI(btnDeactivateAgent);
                GlobalCC.refreshUI(btnCreateUpdateCurrentAgency);
                GlobalCC.refreshUI(panelMainAccButtons);
                mainPanelTab.setVisible(true);
                GlobalCC.refreshUI(pbMainPanelDetail);
                ADFUtils.findIterator("fetchSubAgentsDetailsIterator").executeQuery();
                GlobalCC.refreshUI(agencyRefereeTable);
                /* ADFUtils.findIterator("fetchAgencyNoneClientsIterator").executeQuery();
                                GlobalCC.refreshUI(tblUnAssignedClients);
                                ADFUtils.findIterator("fetchAgencyClientsIterator").executeQuery();
                                GlobalCC.refreshUI(agentsTbl);*/
                ADFUtils.findIterator("findServiceprovidersIterator").executeQuery();
                GlobalCC.refreshUI(unAssignedServiceProviders);
                ADFUtils.findIterator("fetchAgencyServiceProvidersIterator").executeQuery();
                GlobalCC.refreshUI(asssignedServiceProviders);
                // Just in case the buttons are not disabled
                btnCreateUpdateCurrentAgency.setText("Update");
                btnCreateUpdateAgency.setText("Update");
                btnCreateUpdateAgency.setDisabled(true);
                //btnCancelAgency.setDisabled(true);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }

    }

    public String actionAuthorizeAgent() {

        Connection conn = null;

        CallableStatement cst = null;
        try {
            DBConnector datahandler = DBConnector.getInstance();
            //confirm that there is someone to perform the next task before completing.
            String Rights = null;
            session.setAttribute("ProcessShtDesc", GlobalCC.agentProcess);
            session.setAttribute("ProcessAreaShtDesc", GlobalCC.Authorize);
            session.setAttribute("ProcessSubAShtDesc", GlobalCC.Authorize);
            JBPMEngine wf = JBPMEngine.getInstance();

            boolean ticketsApp = wf.TicketsApp();

            Rights = wf.CheckUserRights();

            if (Rights.equalsIgnoreCase("N")) {
                GlobalCC.INFORMATIONREPORTING("You do not have rights to Perform this Task.");
                return null;
            }

            //confirm that there is someone to perform the next task before completing
            //            String NextUser = null;
            //
            //            NextUser = GlobalCC.checkNullValues(session.getAttribute("NextTaskAssignee"));
            //            String TaskAss = GlobalCC.checkNullValues(session.getAttribute("TaskAssignee"));
            //            if (TaskAss == null && NextUser.equalsIgnoreCase("N")) {
            //                String Message =
            //                    "There is no User to Assign the Next Task. Consult the Administrator.";
            //                GlobalCC.INFORMATIONREPORTING(Message);
            //                return null;
            //
            //            }
            String Taske =
                GlobalCC.checkNullValues(session.getAttribute("taskID"));

            //Confirm that the Task being done is the correct one
            if (Taske != null && ticketsApp) {
                if (!wf.checkTaskCompletion(Taske, "AAGNT")) {
                    return null;
                }
            }

            conn = datahandler.getDatabaseConnection();
            String sql =
                "begin tqc_agencies_pkg.agent_status_prc(?,?,?,?); end;";
            cst = conn.prepareCall(sql);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setBigDecimal(3,
                              GlobalCC.checkBDNullValues(session.getAttribute("AgentCode")));
            cst.setString(4, "ACTIVE");
            cst.execute();
            conn.commit();
            conn.close();

            session.setAttribute("AgentStatus", "ACTIVE");

            String success = null;
            if (Taske != null && ticketsApp) {
                success = wf.CompleteTask();
                String CurrTask =
                    GlobalCC.checkNullValues(session.getAttribute("taskID"));
                if (CurrTask != null) {
                    wf.closeTicket("A", CurrTask);
                    session.removeAttribute("taskID");
                    session.removeAttribute("activityName");
                }
                wf.clearProcessSession();
            }
            actionLoadByAgentCode();
            GlobalCC.refreshUI(panelMainAccButtons);
            String Message = "Agent Successfully Authorised!";
            GlobalCC.INFORMATIONREPORTING(Message);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }


    public String actionMakeReadyAgent() {

        Connection conn = null;

        CallableStatement cst = null;
        try {
            DBConnector datahandler = DBConnector.getInstance();
            //confirm that there is someone to perform the next task before completing.
            String Rights = null;
            session.setAttribute("ProcessShtDesc", GlobalCC.agentProcess);
            session.setAttribute("ProcessAreaShtDesc", GlobalCC.MakeReady);
            session.setAttribute("ProcessSubAShtDesc", GlobalCC.MakeReady);

            JBPMEngine wf = JBPMEngine.getInstance();

            boolean ticketsApp = wf.TicketsApp();

            Rights = wf.CheckUserRights();

            if (Rights.equalsIgnoreCase("N")) {
                GlobalCC.INFORMATIONREPORTING("You do not have rights to Perform this Task.");
                return null;
            }

            //confirm that there is someone to perform the next task before completing
            //            String NextUser = null;
            //
            //            NextUser = GlobalCC.checkNullValues(session.getAttribute("NextTaskAssignee"));
            //            String TaskAss = GlobalCC.checkNullValues(session.getAttribute("TaskAssignee"));
            //            if (TaskAss == null && NextUser.equalsIgnoreCase("N")) {
            //                String Message =
            //                    "There is no User to Assign the Next Task. Consult the Administrator.";
            //                GlobalCC.INFORMATIONREPORTING(Message);
            //                return null;
            //
            //            }
            String Taske =
                GlobalCC.checkNullValues(session.getAttribute("taskID"));

            //Confirm that the Task being done is the correct one

            if (Taske != null && ticketsApp) {
                if (!wf.checkTaskCompletion(Taske, "MAGNT")) {
                    return null;
                }
            }


            conn = datahandler.getDatabaseConnection();
            String sql =
                "begin tqc_agencies_pkg.agent_status_prc(?,?,?,?); end;";
            cst = conn.prepareCall(sql);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setBigDecimal(3,
                              GlobalCC.checkBDNullValues(session.getAttribute("AgentCode")));
            cst.setString(4, "READY");
            cst.execute();
            conn.commit();
            conn.close();

            session.setAttribute("AgentStatus", "READY");

            String success = null;
            if (Taske != null && ticketsApp) {
                success = wf.CompleteTask();
            }

            actionLoadByAgentCode();

            if ("success".equals(success) || Taske == null || !ticketsApp) {
                String Message = "Agent Successfully Make Ready!";
                GlobalCC.INFORMATIONREPORTING(Message);
            }
            GlobalCC.refreshUI(panelMainAccButtons);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    public String actionDeactivateAgent() {

        Connection conn = null;

        CallableStatement cst = null;
        try {
            DBConnector datahandler = DBConnector.getInstance();
            //confirm that there is someone to perform the next task before completing.
            String Rights = null;
            session.setAttribute("ProcessShtDesc", GlobalCC.agentProcess);
            session.setAttribute("ProcessAreaShtDesc", GlobalCC.Deactivate);
            session.setAttribute("ProcessSubAShtDesc", GlobalCC.Deactivate);
            JBPMEngine wf = JBPMEngine.getInstance();

            boolean ticketsApp = wf.TicketsApp();

            Rights = wf.CheckUserRights();

            if (Rights.equalsIgnoreCase("N")) {
                GlobalCC.INFORMATIONREPORTING("You do not have rights to Perform this Task.");
                return null;
            }

            //confirm that there is someone to perform the next task before completing
            //            String NextUser = null;
            //
            //            NextUser = GlobalCC.checkNullValues(session.getAttribute("NextTaskAssignee"));
            //            String TaskAss = GlobalCC.checkNullValues(session.getAttribute("TaskAssignee"));
            //            if (TaskAss == null && NextUser.equalsIgnoreCase("N")) {
            //                String Message =
            //                    "There is no User to Assign the Next Task. Consult the Administrator.";
            //                GlobalCC.INFORMATIONREPORTING(Message);
            //                return null;
            //
            //            }
            String Taske =
                GlobalCC.checkNullValues(session.getAttribute("taskID"));

            //Confirm that the Task being done is the correct one
            if (Taske != null && ticketsApp) {
                if (!wf.checkTaskCompletion(Taske, "DAGNT")) {
                    return null;
                }
            }

            conn = datahandler.getDatabaseConnection();
            String sql =
                "begin tqc_agencies_pkg.agent_status_prc(?,?,?,?); end;";
            cst = conn.prepareCall(sql);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setBigDecimal(3,
                              GlobalCC.checkBDNullValues(session.getAttribute("AgentCode")));
            cst.setString(4, "INACTIVE");
            cst.execute();
            conn.commit();
            conn.close();

            session.setAttribute("AgentStatus", "INACTIVE");

            String success = null;
            if (Taske != null && ticketsApp) {
                success = wf.CompleteTask();
            }

            actionLoadByAgentCode();

            if ("success".equals(success) || Taske == null || !ticketsApp) {
                String Message = "Agent Successfully Deativated!";
                GlobalCC.INFORMATIONREPORTING(Message);
            }
            GlobalCC.refreshUI(panelMainAccButtons);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    public String actionSuspendAgent() {

        Connection conn = null;

        CallableStatement cst = null;
        try {
            DBConnector datahandler = DBConnector.getInstance();
            //confirm that there is someone to perform the next task before completing.
            String Rights = null;
            session.setAttribute("ProcessShtDesc", GlobalCC.agentProcess);
            session.setAttribute("ProcessAreaShtDesc", GlobalCC.Suspend);
            session.setAttribute("ProcessSubAShtDesc", GlobalCC.Suspend);
            JBPMEngine wf = JBPMEngine.getInstance();

            boolean ticketsApp = wf.TicketsApp();

            Rights = wf.CheckUserRights();

            if (Rights.equalsIgnoreCase("N")) {
                GlobalCC.INFORMATIONREPORTING("You do not have rights to Perform this Task.");
                return null;
            }

            //confirm that there is someone to perform the next task before completing
            //            String NextUser = null;
            //
            //            NextUser = GlobalCC.checkNullValues(session.getAttribute("NextTaskAssignee"));
            //            String TaskAss = GlobalCC.checkNullValues(session.getAttribute("TaskAssignee"));
            //            if (TaskAss == null && NextUser.equalsIgnoreCase("N")) {
            //                String Message =
            //                    "There is no User to Assign the Next Task. Consult the Administrator.";
            //                GlobalCC.INFORMATIONREPORTING(Message);
            //                return null;
            //
            //            }
            String Taske =
                GlobalCC.checkNullValues(session.getAttribute("taskID"));

            //Confirm that the Task being done is the correct one
            if (Taske != null && ticketsApp) {
                if (!wf.checkTaskCompletion(Taske, "AAGNT")) {
                    return null;
                }
            }

            conn = datahandler.getDatabaseConnection();
            String sql =
                "begin tqc_agencies_pkg.agent_status_prc(?,?,?,?); end;";
            cst = conn.prepareCall(sql);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setBigDecimal(3,
                              GlobalCC.checkBDNullValues(session.getAttribute("AgentCode")));
            cst.setString(4, "SUSPENDED");
            cst.execute();
            conn.commit();
            conn.close();

            session.setAttribute("AgentStatus", "SUSPENDED");

            String success = null;

            if (Taske != null && ticketsApp) {
                success = wf.CompleteTask();
                String CurrTask =
                    GlobalCC.checkNullValues(session.getAttribute("taskID"));
                if (CurrTask != null) {
                    wf.closeTicket("A", CurrTask);
                }
                wf.clearProcessSession();
            }
            actionLoadByAgentCode();
            if ("success".equals(success) || Taske == null || !ticketsApp) {
                String Message = "Agent Successfully Suspended!";
                GlobalCC.INFORMATIONREPORTING(Message);
            }
            GlobalCC.refreshUI(panelMainAccButtons);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }
    /*public String actionReassignAgent() {


        try {



            GlobalCC.showPopup("pt1:popReAssignTaskUsers");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }*/

    public String saveTaskAssignee() {
        String assignee = null;
        String taskId = null;
        String remarks = null;

        taskId = GlobalCC.checkNullValues(session.getAttribute("taskID"));
        remarks = GlobalCC.checkNullValues(txtAssignRemarks.getValue());

        if (taskId == null) {
            GlobalCC.errorValueNotEntered("Error: Please Select Task!");
            return null;
        }
        Object key2 = systemUsers.getSelectedRowData();

        if (key2 == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        JUCtrlValueBinding row = (JUCtrlValueBinding)key2;

        if (row == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        assignee = GlobalCC.checkNullValues(row.getAttribute("usrName"));


        if (assignee == null) {
            GlobalCC.errorValueNotEntered("Error: Please Select User!");
            return null;
        }


        JBPMEngine bpm = JBPMEngine.getInstance();
        bpm.saveTaskAssignee(assignee, taskId);

        DBConnector datahandler = null;
        datahandler = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;

        try {
            conn = datahandler.getDatabaseConnection();
            String mktProposalNext =
                "begin Tqc_Web_Pkg.Assign_Ticket ( ?, ?, ?, ?, ?, ? ); end;";
            cst = (OracleCallableStatement)conn.prepareCall(mktProposalNext);
            //bpm.saveTaskAssignee(assignee, taskId);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setString(3, taskId);
            cst.setString(4, assignee);
            cst.setString(5, remarks);
            cst.setString(6, session.getAttribute("Username").toString());
            cst.execute();
            conn.commit();
            String status = cst.getString(1);
            String msg = cst.getString(2);
            if ("S".equals(status)) {
                GlobalCC.hidePopup("pt1:popReAssignTaskUsers");
            }
            GlobalCC.INFORMATIONREPORTING(msg);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    public void onPageLoad(PhaseEvent phaseEvent) {
        String agentCode =
            GlobalCC.checkNullValues(session.getAttribute("AgentCode"));
        String viewMyticket =
            GlobalCC.checkNullValues(session.getAttribute("viewMyticket"));

        if ("Y".equals(viewMyticket) &&
            agentCode != null) { // //  from HOME TICKETS
            actionLoadByAgentCode();
            session.removeAttribute("viewMyticket");
        }
    }

    public void setTxtAssignRemarks(RichInputText txtAssignRemarks) {
        this.txtAssignRemarks = txtAssignRemarks;
    }

    public RichInputText getTxtAssignRemarks() {
        return txtAssignRemarks;
    }

    public void setSystemUsers(RichTable systemUsers) {
        this.systemUsers = systemUsers;
    }

    public RichTable getSystemUsers() {
        return systemUsers;
    }


    public boolean isbtnMakeReadyAgentDisabled() {
        String status =
            GlobalCC.checkNullValues(session.getAttribute("AgentStatus"));
        return ("INACTIVE".equalsIgnoreCase(status) ||
                "SUSPENDED".equalsIgnoreCase(status) ||
                "ACTIVE".equalsIgnoreCase(status) ||
                "READY".equalsIgnoreCase(status));
    }


    public boolean isbtnDeactivateAgentDisabled() {
        String status =
            GlobalCC.checkNullValues(session.getAttribute("AgentStatus"));
        return ("INACTIVE".equalsIgnoreCase(status) ||
                "SUSPENDED".equalsIgnoreCase(status) ||
                "READY".equalsIgnoreCase(status));
    }

    public boolean isbtnAuthorizeAgentDisabled() {
        String status =
            GlobalCC.checkNullValues(session.getAttribute("AgentStatus"));
        return ("ACTIVE".equalsIgnoreCase(status) ||
                "DRAFT".equalsIgnoreCase(status));
    }

    public boolean isbtnSaveAgentDisabled() { //DISABLE UPDATE BUTTON WHEN THE ACCOUNT IS SUSPENDED
        String status =
            GlobalCC.checkNullValues(session.getAttribute("AgentStatus"));
        return ("SUSPENDED".equalsIgnoreCase(status));
    }

    public boolean isbtnSuspendAgentDisabled() {
        String status =
            GlobalCC.checkNullValues(session.getAttribute("AgentStatus"));
        return ("INACTIVE".equalsIgnoreCase(status) ||
                "SUSPENDED".equalsIgnoreCase(status) ||
                "READY".equalsIgnoreCase(status));
    }

    public String viewDoc() {
        Object key2 = agentRecDocsLov.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        String id = (String)r.getAttribute("CDOCR_DOCID");
        String[] ids = id.split(";");
        String finalId = "";
        if (ids.length > 1) {
            finalId = ids[0];
        } else
            finalId = id;

        if (!new Rendering().isAnnotatedDoc()) {
            try {
                ServletContext servletContext =
                    (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();

                HttpServletRequest request =
                    (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String urlss =
                    request.getScheme() + "://" + request.getServerName() +
                    ":" + request.getServerPort() +
                    servletContext.getContextPath();
                System.out.println("Url " + urlss);
                String finalUrl = urlss + "/viewer.html?";

                CmisObject object =
                    new CSVtoADFTableProcessor().getCmisSession().getObject(finalId);
                Document document = (Document)object;
                String filename = document.getName();
                InputStream inputStream =
                    document.getContentStream().getStream();
                String file = "/Reports/" + filename;
                String filerpt = "/Reports/" + filename;

                FacesContext context = FacesContext.getCurrentInstance();
                ServletContext sc =
                    (ServletContext)context.getExternalContext().getContext();
                file = sc.getRealPath(file);
                File toUpload = new File(file);
                OutputStream out;

                out = new FileOutputStream(toUpload);
                int read = 0;
                byte[] bytes = new byte[inputStream.available()];

                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                inputStream.close();
                out.flush();
                out.close();
                finalUrl = finalUrl + "file=" + urlss + filerpt;
                session.setAttribute("toPrint", finalUrl);
                System.out.println(finalUrl);

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:p201" + "').show(hints);");

            } catch (Exception e) {

            }

        } else {

            Connection conn = null;
            String url = "";
            String user = "";
            String pass = "";
            DBConnector datahandler = DBConnector.getInstance();
            conn = datahandler.getDatabaseConnection();
            CallableStatement cstSections = null;
            ResultSet sectionsRS = null;
            try {
                cstSections =
                        conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_system_ecm_setups(?); end;");
                cstSections.registerOutParameter(1,
                                                 oracle.jdbc.OracleTypes.CURSOR);
                cstSections.setInt(2, 37);
                cstSections.execute();
                sectionsRS = (ResultSet)cstSections.getObject(1);
                if (sectionsRS.next()) {
                    url = sectionsRS.getString(2);
                    user = sectionsRS.getString(3);
                    pass = sectionsRS.getString(4);

                }
                String[] urls = url.split("/");
                String toPrint =
                    urls[0] + "//" + urls[2] + "/" + "/OpenAnnotate/login/unencrypted.htm?username=" +
                    user + "&password=" + pass +
                    "&docbase=Company Home&docId=" + finalId;
                System.out.println(toPrint);
                session.setAttribute("toPrint", toPrint);
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:p201" + "').show(hints);");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DbUtils.closeQuietly(conn, cstSections, sectionsRS);
            }
        }
        return null;

    }

    public void setRequiredDocsBox(RichPanelBox requiredDocsBox) {
        this.requiredDocsBox = requiredDocsBox;
    }

    public RichPanelBox getRequiredDocsBox() {
        return requiredDocsBox;
    }

    public void enterAgentId(ValueChangeEvent valueChangeEvent) {
        fetchWebDetails();
    }

    public String fetchWebDetails() {

        IprsSetupsApi iprsApi = new IprsSetupsApi(session);
        String id = GlobalCC.checkNullValues(txtAgencyIDNum.getValue());
        String type = GlobalCC.checkNullValues(txtAgnIDNODocUsed.getValue());
        String idno = null;
        String passport = null;
        if (id != null && type != null && renderer.isIprsApp()) {
            if ("I".equals(type)) {
                idno = id;
            }
            if ("P".equals(type)) {
                passport = id;
            }
            session.setAttribute("Iprs_Account_Type", "A");
            session.setAttribute("tq_passport", null);
            session.setAttribute("tq_idno", null);

            if (idno != null) {
                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcher = pattern.matcher(idno);
                if (idno.length() >= 7 && matcher.matches()) {
                    session.setAttribute("tq_idno", idno);
                }
            }
            if (passport != null) {
                passport = passport.toUpperCase();
                Pattern pattern =
                    Pattern.compile("[A-Z]{1}[0-9]{6,12}([A-Z]{0,1})");
                Matcher matcher = pattern.matcher(passport);
                if (passport.length() >= 7 && matcher.matches()) {
                    session.setAttribute("tq_passport", passport);
                }
            }
            iprsApi.clearIprsFields();
            iprsApi.popIprsFields(dlgIprsComp);

            String dob =
                GlobalCC.checkNullValues(session.getAttribute("iprs_dob"));
            if (dob != null) {
                java.util.Date date =
                    GlobalCC.getDate(dob, iprsApi.IPRS_JAVA_DATE_FMT);
                txtAgnDOB.setValue(date);
                txtAgnDOB.setDisabled(true);
            }
            List<String> fullNames = new ArrayList<String>();

            String surname =
                GlobalCC.checkNullValues(session.getAttribute("iprs_surname"));
            if (surname != null) {
                fullNames.add(surname);
            }
            String firstName =
                GlobalCC.checkNullValues(session.getAttribute("iprs_firstname"));
            if (firstName != null) {
                fullNames.add(firstName);
            }
            String otherNames =
                GlobalCC.checkNullValues(session.getAttribute("iprs_othernames"));
            if (otherNames != null) {
                fullNames.add(otherNames);
            }
            if (fullNames.size() > 0) {
                txtContactPerson.setValue(GlobalCC.join(fullNames, " "));
                txtContactPerson.setDisabled(true);
            }
            String gender =
                GlobalCC.checkNullValues(session.getAttribute("iprs_gender"));
            if (gender != null) {
                //  txtGender.setValue(gender);
                //  txtGender.setDisabled(true);
            }
            session.setAttribute("agentIprsValidated", "Y");
            GlobalCC.refreshUI(panelAgencyDetails);
        }
        return null;
    }

    public String actionLoadIprsAgentDtls() {

        BigDecimal agentCode = null;
        try {

            Authorization auth = new Authorization();
            String process = "AMA";
            String processArea = "AMAA";
            String processSubArea = "IVDT";
            String AccessGranted =
                auth.checkUserRights(process, processArea, processSubArea,
                                     null, null);
            if (!"Y".equalsIgnoreCase(AccessGranted)) {
                GlobalCC.accessDenied();
                return null;
            }

            agentCode =
                    GlobalCC.checkBDNullValues(session.getAttribute("AgentCode"));
            if (agentCode == null) {
                return null;
            }
            IprsSetupsApi iprsApi = new IprsSetupsApi(session);
            iprsApi.load("A", agentCode, dlgIprsComp);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String actionUpdateTQRecord() {
        IprsSetupsApi iprsApi = new IprsSetupsApi(session);
        return iprsApi.saveTQFields(dlgIprsComp);
    }

    public void setDlgIprsComp(RichDialog dlgIprsComp) {
        this.dlgIprsComp = dlgIprsComp;
    }

    public RichDialog getDlgIprsComp() {
        return dlgIprsComp;
    }

    public void setTxtTqIdNumber(RichInputText txtTqIdNumber) {
        this.txtTqIdNumber = txtTqIdNumber;
    }

    public RichInputText getTxtTqIdNumber() {
        return txtTqIdNumber;
    }

    public void setTxtTqPinNo(RichInputText txtTqPinNo) {
        this.txtTqPinNo = txtTqPinNo;
    }

    public RichInputText getTxtTqPinNo() {
        return txtTqPinNo;
    }

    public void setTxtTqPassport(RichInputText txtTqPassport) {
        this.txtTqPassport = txtTqPassport;
    }

    public RichInputText getTxtTqPassport() {
        return txtTqPassport;
    }

    public void setTxtTqDob(RichInputText txtTqDob) {
        this.txtTqDob = txtTqDob;
    }

    public RichInputText getTxtTqDob() {
        return txtTqDob;
    }

    public void setTxtTqFullNames(RichInputText txtTqFullNames) {
        this.txtTqFullNames = txtTqFullNames;
    }

    public RichInputText getTxtTqFullNames() {
        return txtTqFullNames;
    }

    public void setTxtTqGender(RichInputText txtTqGender) {
        this.txtTqGender = txtTqGender;
    }

    public RichInputText getTxtTqGender() {
        return txtTqGender;
    }

    public void setTxtIprsIdNumber(RichInputText txtIprsIdNumber) {
        this.txtIprsIdNumber = txtIprsIdNumber;
    }

    public RichInputText getTxtIprsIdNumber() {
        return txtIprsIdNumber;
    }

    public void setTxtIprsPinNo(RichInputText txtIprsPinNo) {
        this.txtIprsPinNo = txtIprsPinNo;
    }

    public RichInputText getTxtIprsPinNo() {
        return txtIprsPinNo;
    }

    public void setTxtIprsPassport(RichInputText txtIprsPassport) {
        this.txtIprsPassport = txtIprsPassport;
    }

    public RichInputText getTxtIprsPassport() {
        return txtIprsPassport;
    }

    public void setTxtIprsDob(RichInputText txtIprsDob) {
        this.txtIprsDob = txtIprsDob;
    }

    public RichInputText getTxtIprsDob() {
        return txtIprsDob;
    }

    public void setTxtIprsFullNames(RichInputText txtIprsFullNames) {
        this.txtIprsFullNames = txtIprsFullNames;
    }

    public RichInputText getTxtIprsFullNames() {
        return txtIprsFullNames;
    }

    public void setTxtIprsGender(RichInputText txtIprsGender) {
        this.txtIprsGender = txtIprsGender;
    }

    public RichInputText getTxtIprsGender() {
        return txtIprsGender;
    }

    public String transitionToDraft() {

        Connection conn = null;

        CallableStatement cst = null;
        try {

            BigDecimal taskId =
                GlobalCC.checkBDNullValues(session.getAttribute("taskID"));
            String activityName =
                GlobalCC.checkNullValues(session.getAttribute("activityName"));

            if (taskId == null) {
                return null;
            }
            if ("Agent Draft".equals(activityName)) { //already draft
                return null;
            }
            DBConnector datahandler = DBConnector.getInstance();
            //confirm that there is someone to perform the next task before completing.
            String Rights = null;
            session.setAttribute("ProcessShtDesc", GlobalCC.agentProcess);
            session.setAttribute("ProcessAreaShtDesc", GlobalCC.Edit);
            session.setAttribute("ProcessSubAShtDesc", GlobalCC.Edit);

            JBPMEngine wf = JBPMEngine.getInstance();

            boolean ticketsApp = wf.TicketsApp();

            String Taske =
                GlobalCC.checkNullValues(session.getAttribute("taskID"));


            conn = datahandler.getDatabaseConnection();
            String sql =
                "begin tqc_agencies_pkg.agent_status_prc(?,?,?,?); end;";
            cst = conn.prepareCall(sql);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setBigDecimal(3,
                              GlobalCC.checkBDNullValues(session.getAttribute("AgentCode")));
            cst.setString(4, "DRAFT");
            cst.execute();
            conn.commit();
            conn.close();

            session.setAttribute("AgentStatus", "DRAFT");

            String success = null;
            if (Taske != null && ticketsApp) {
                success = wf.CompleteTask();
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    public void setTxtActivityName(RichOutputLabel txtActivityName) {
        this.txtActivityName = txtActivityName;
    }

    public RichOutputLabel getTxtActivityName() {
        return txtActivityName;
    }

    public void setPanelMainAccButtons(HtmlPanelGrid panelMainAccButtons) {
        this.panelMainAccButtons = panelMainAccButtons;
    }

    public HtmlPanelGrid getPanelMainAccButtons() {
        return panelMainAccButtons;
    }

    public void setBtnMakeReadyAgent(RichCommandButton btnMakeReadyAgent) {
        this.btnMakeReadyAgent = btnMakeReadyAgent;
    }

    public RichCommandButton getBtnMakeReadyAgent() {
        return btnMakeReadyAgent;
    }

    public void setBtnSuspendAgent(RichCommandButton btnSuspendAgent) {
        this.btnSuspendAgent = btnSuspendAgent;
    }

    public RichCommandButton getBtnSuspendAgent() {
        return btnSuspendAgent;
    }

    public void setBtnDeactivateAgent(RichCommandButton btnDeactivateAgent) {
        this.btnDeactivateAgent = btnDeactivateAgent;
    }

    public RichCommandButton getBtnDeactivateAgent() {
        return btnDeactivateAgent;
    }

    public boolean istxtSurnameDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_AGENT_SURNAME.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("agentIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public boolean istxtOtherNamesDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_AGENT_OTHER_NAMES.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("agentIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public boolean istxtContactPersonDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_AGENT_CONTACT_PERSON.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("agentIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public boolean istxtFullNameDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_AGENT_FULL_NAMES.disabled"));
        return ("Y".equalsIgnoreCase(disabled));
    }

    public boolean istxtGenderDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_AGENT_GENDER.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("agentIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public boolean istxtDobDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_AGENT_DATE_OF_BIRTH.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("agentIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public void setTxtrelatedpart(RichSelectOneChoice txtrelatedpart) {
        this.txtrelatedpart = txtrelatedpart;
    }

    public RichSelectOneChoice getTxtrelatedpart() {
        return txtrelatedpart;
    }
}
