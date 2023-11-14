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

package TurnQuest.view.client;


import TurnQuest.view.Alerts.JBPMEngine;
import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.ChannelManager;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.HibernateUtil;
import TurnQuest.view.Base.IQuery;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Base.Util;
import TurnQuest.view.Connect.Authorization;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Ui.FormUi;
import TurnQuest.view.bpm.TaskManipulation;
import TurnQuest.view.commons.UtilDAO;
import TurnQuest.view.models.ClientDirector;
import TurnQuest.view.models.ClientSignatory;
import TurnQuest.view.models.ClientType;
import TurnQuest.view.models.IncomeSources;
import TurnQuest.view.models.UserSystem;
import TurnQuest.view.navigation.Links;
import TurnQuest.view.utilities.CSVtoADFTableProcessor;
import TurnQuest.view.web.CbaCustomerAccountInfo;
import TurnQuest.view.web.IprsSetupsApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.math.BigDecimal;

import java.net.FileNameMap;
import java.net.URLConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import oracle.adf.view.rich.component.rich.input.RichSelectItem;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandLink;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.component.rich.output.RichPanelCollection;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * The base backing bean for all the client related pages. Includes
 * properties and methods that map to given  UI components in the relevant
 * pages of the clients.
 *
 * @author Frankline Ogongi
 *
 * Understanding Analysisggggggggg
 */


public class BaseClientBacking {

    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    Rendering renderer = new Rendering();
    private IprsSetupsApi iprsApi = null;

    private RichSelectOneChoice txtClientLevel;
    private RichTable tblClients;
    private RichTable tbPinRefs;
    private RichPanelBox panelCreateClient;
    private RichSelectBooleanRadio sbrIndividual;
    private RichSelectBooleanRadio sbrCorporate;
    private RichInputDate txtDateCreated;
    private RichInputText txtCreatedBy;
    private RichSelectOneChoice txtDirectClient;
    private RichInputText txtAccountNum;
    private RichInputText txtId;
    private RichSelectOneChoice txtTitle;
    private RichInputText txtSurname;
    private RichInputText txtOtherNames;
    private RichInputText txtFullNames;
    private RichInputText txtPhysicalAddress;
    private RichInputText txtPostalAddress;
    private RichInputText txtCountryCode;
    private RichInputText txtCountryName;
    private RichInputText txtTownCode;
    private RichInputText txtTownName;
    private RichInputDate txtDOB;
    private RichInputText txtBankAccNum;
    private RichInputText txtPIN;
    private RichInputText txtIdRegNum;
    private RichInputText txtEmail;
    private RichInputText txtEmail2;
    private RichInputText txtPhone1;
    private RichInputText txtSms;
    private RichInputText txtFax;
    private RichSelectOneChoice txtProposer;
    private RichInputDate txtWef;
    private RichInputDate txtWet;
    private RichInputText txtRemarks;
    private RichInputText txtSignatoryTitleCode;
    private RichInputText txtWithdrawalReason;
    private RichSelectOneChoice txtStatus;
    private RichSelectOneChoice txtRunOff;
    private RichSelectOneChoice txtSpecialTerms;
    private RichSelectOneChoice txtCancelledPolicy;
    private RichSelectOneChoice txtIncreasePremium;
    private RichSelectOneChoice txtDeclinedProposal;
    private RichInputText txtBranchCode;
    private RichInputText txtBranchName;
    private RichInputText txtDivisionCode;
    private RichInputText txtDivisionName;
    private RichInputText txtAccountManager;
    private RichInputText txtSectorCode;
    private RichInputText txtSectorName;
    private RichInputText txtDomicileCountriesCode;
    private RichInputText txtDomicileCountriesName;
    private RichTable tblCountryPop;
    private RichTable tblTownPop;
    private RichInputText txtClientCode;
    private RichInputText contactName1;
    private RichInputText contactPhone1;
    private RichInputText contactEmail1;
    private RichInputText contactName2;
    private RichInputText contactPhone2;
    private RichInputText contactEmail2;
    private RichCommandButton btnShowContactPersons;
    private RichCommandLink cmdSelectPin;
    private RichInputText txtContactName1;
    private RichInputText txtContactPhone1;
    private RichInputText txtContactEmail1;
    private RichInputText txtContactName2;
    private RichInputText txtContactPhone2;
    private RichInputText txtContactEmail2;
    private RichTable tblSectorPop;
    private RichTable tblDomicileCountryPop;
    private RichTable tblCountryPop367;
    private RichTable tblAccountManagersPop;
    private RichInputText txtAccountManagerCode;
    private RichTable tblBankBranchPop;
    private RichTable tblBranchDivisionPop;
    private RichCommandButton btnCreateUpdateClient;
    private RichInputText txtAccUsername;
    private RichInputText txtAccFullNames;
    private RichInputText txtAccPassword;
    private RichInputText txtAccPersonelRank;
    private RichSelectOneChoice txtAccStatus;
    private RichSelectOneChoice txtAccAllowLogin;
    private RichInputText txtAccEmail;
    private RichInputText txtAccCode;
    private RichCommandButton btnSaveUpdateWebAccount;
    private RichCommandButton btnReassignClient;
    private RichTable tblwebAccounts;
    private RichPanelBox panelClientSystems;
    private RichTree treeAssignedClientSystems;
    private RichTree treeUnassignedClientSystems;
    private RichPanelBox panelDetailSystems;
    private RichInputText txtSelectedClientSystemCode;
    private RichCommandButton btnAddClientSystem;
    private RichCommandButton btnRemoveClientSystem;
    private RichTable tblRequiredDocs;
    private RichShowDetailItem tabDetailCreateClient;
    private RichShowDetailItem tabDetailSearchClients;
    private RichShowDetailItem tabClientInfo;
    private RichShowDetailItem tabClientSystems;
    private RichShowDetailItem tabClientWebAccounts;
    private RichShowDetailItem tabClientDocs;
    private RichPanelBox mainPanel;
    private RichCommandButton btnSaveRequiredDoc;
    private RichInputText txtDocCodePop;
    private RichInputText txtReqDocCodePop;
    private RichInputText txtReqDocNamePop;
    private RichInputText txtDocClientCodePop;
    private RichSelectOneChoice txtDocSubmittedPop;
    private RichInputDate txtDocDateSubmittedPop;
    private RichInputText txtDocRefNumPop;
    private RichInputText txtDocRemarkPop;
    private RichInputText txtDocUserReceivedPop;
    private RichTable tblReqDocsList;
    private RichPopup confirmationDialog;
    private RichPanelBox requiredDocsBox;
    private RichOutputLabel clientCodeValue;
    private RichOutputLabel olConfirmMsgValue;
    private RichOutputText textToShow;
    private RichInputText txtClientTitle;
    private RichTable tblClientTitles;
    private RichTable tblSignTitles;
    private RichInputText txtClientTitleCode;
    private RichInputText hiddenClientCode;
    private RichTable tblClientAccounts;
    private RichInputText txtAccountCode;
    private RichInputText txtAccShortDesc;
    private RichInputText txtAccName;
    private RichInputText txtAccClientCode;
    private RichInputText txtAccCreatedBy;
    private RichInputDate txtAccDateCreated;
    private RichSelectOneChoice txtClientAccStatus;
    private RichInputText txtAccRemarks;
    private RichInputDate txtAccWef;
    private RichInputDate txtAccWet;

    private RichInputText txtAccCntName;
    private RichInputText txtAccCntTitle;
    private RichInputText txtAccCntSmsNo;
    private RichInputText txtAccCntEmail;

    private RichCommandButton btnSaveUpdateClientAccount;
    private RichInputText txtPassportNo;
    private RichInputText txtBrnDivName;
    private RichTable brnDivLov;
    private RichInputText txtStateCode;
    private RichInputText txtStateName;
    private RichTable tblStates;
    private RichPanelLabelAndMessage clientTitlePan;
    private RichPanelLabelAndMessage signTitlePan;
    private RichPanelLabelAndMessage dirTitlePan;
    private RichPanelLabelAndMessage clientIdPan;
    private RichPanelLabelAndMessage clientPinPan;
    private RichPanelLabelAndMessage clientPassportPan;
    private RichSelectBooleanRadio genAcSelectYes;
    private RichSelectBooleanRadio genAccNoSelect;
    private RichOutputLabel confirmDeleteAccMsg;
    private RichTable tbClientListingBasedOnNames;
    private RichDialog confirmSaveDialog;
    private RichCommandButton btnAuthorizeClient;
    private RichCommandButton btnMakeReadyClient;
    private RichCommandButton btnDeactivateClient;

    private RichSelectItem selectStatusDraft;
    private RichSelectItem selectStatusInactive;
    private RichSelectItem selectStatusReady;
    private RichSelectItem selectStatusActive;
    private RichCommandButton btnRejectClient;
    private RichCommandButton btnBackClient;
    private RichImage clientImage;
    private RichInputFile uploadComponent;
    private RichInputFile uploadPhoto;
    private RichImage clientSignature;
    private RichImage clientPhoto;
    private RichInputFile uploadedPicture;
    private RichInputFile uploadSignature;
    private RichInputText txtWebsite;
    private RichInputText txtAuditors;
    private RichInputText txtDLNo;
    private RichInputDate txtDateOfEmployment;
    private RichInputText txtInsurer;
    private RichInputText txtParentCompany;
    private RichInputText txtProjectedBusiness;
    private BigDecimal clnt_code;
    private RichCommandButton addAgencyDirectors;
    private RichCommandButton editAgencyDirectors;
    private RichCommandButton deleteAgencyDirectors;
    private RichCommandButton btnRefreshSignatories;
    private RichInputText agencyDirectorId;
    private RichInputDate directYr;
    private RichInputText directQualifications;
    private RichInputNumberSpinbox directshare;
    private RichCommandButton saveAgDirectorsAction;
    private RichShowDetailItem tabDirectors;
    private RichShowDetailItem tabSignatories;
    private RichInputText directname;
    private RichCommandButton saveAgDirectorButton;
    private RichTable agentDirectorsTable;
    private RichTable postalCodeTable;
    private RichShowDetailItem tabAuditors;
    private RichCommandButton addAgencyAuditors;
    private RichCommandButton editAgencyAuditors;
    private RichCommandButton deleteAgencyAuditors;
    private RichInputText agencyAuditorId;
    private RichCommandButton saveAgAuditorButton;
    private RichInputText directAuditorQualification;
    private RichInputText auditName;
    private RichInputText auditYr;
    private RichInputText auditYear;
    private RichInputDate auditYER;
    private RichInputText auditorTelephone;
    private RichTable agentAuditorsTable;
    private RichTable signatoriesTable;
    private RichCommandButton addClientAuditors;
    private RichInputText txtParentCompanyCode;
    private RichPanelLabelAndMessage parentcompanyPan;
    private RichPopup tbParentCompanyPop;
    private RichTable tableParentComp;
    private RichInputNumberSpinbox txtProjectedBiz;
    private RichInputText txtRegBranchName;
    private RichInputText txtRegBranchCode;
    private RichCommandButton tblBranches;
    private RichTable tblRegBranches;
    private RichTable tblCurrencyPop;
    private RichPanelBox panelCurrencyRate;
    private RichCommandButton btnAcceptBranch;
    private RichPopup branchPop;
    private RichInputText txtGrpName;
    private RichInputNumberSpinbox txtGrpMinimum;
    private RichInputNumberSpinbox txtGrpMax;
    private RichCommandButton btnsaveGrp;
    private RichInputText txtGrpCode;
    private RichTable tblClientGroup;
    private RichTable tblClientGrpMembers;
    private RichTable tblClientPop;
    private RichCommandButton btnRemoveClientFromGroup;
    private RichCommandButton btnActionEditGroupClient;
    private RichCommandButton btnActionDeleteGroupClient;
    private RichSelectOneChoice searchCriteria;
    private RichInputText txtSearchSectorName;
    private RichSelectBooleanCheckbox columnSelect;
    private RichInputText txtSearchSector;
    private RichInputText txtSrchSectorName;
    private RichInputText txtAccountOfficerCode;
    private RichInputText txtAccountOfficerName;
    private RichTable tblAccountOfficer;
    private RichPanelGroupLayout laySignature;
    private RichPanelGroupLayout layClientImage;
    private InputStream inputstream;
    private RichCommandButton btnDeleteClient;
    private RichPanelGroupLayout searchFormHolder;
    private RichPanelFormLayout SEARCHHOLDER;
    private RichPanelLabelAndMessage statusHolder;
    private RichPanelLabelAndMessage searchClientType;
    private RichShowDetailItem tabClientAccounts;
    private RichPanelTabbed clientTab;
    private RichPanelCollection panelCollSearch;
    private RichSelectOneChoice txtGender;
    private RichInputText txtStaffNo;
    private UploadedFile pictureFile;
    private UploadedFile signatureFile;
    private static InputStream fileStream;
    private String filename;
    private long filesize;
    private static String fileContent;
    private String filetype;

    private static InputStream fileStream2;
    private String filename2;
    private long filesize2;
    private static String fileContent2;
    private String filetype2;
    private RichInputText txtClientCellNos;
    private RichInputText txtCltBankTelNo;
    private RichInputText txtCltBankCellNo;
    private RichInputText txtCltEmployerTelNo;
    private RichInputText txtCltEmployerCellNo;
    private RichPanelGroupLayout grpEmploymentDetails;
    private RichInputText txtCltOccupation;
    private RichInputText txtSpecialization;
    private HtmlPanelGrid pgridPersonnalDetails;
    private RichShowDetailItem detailEmploymentDetails;
    private RichTable tblAdminRegions;
    private RichPanelLabelAndMessage panMsgRegionTypeName;
    private RichPanelLabelAndMessage panMsgOccupationSign;
    private RichPanelLabelAndMessage panMsgOccupation;
    private RichPanelLabelAndMessage panMsgNationality;
    private RichPanelLabelAndMessage panDirgOccupation;
    private RichPanelLabelAndMessage panMsgSector;
    private RichPanelLabelAndMessage panMsgSpecialization;

    private RichDialog dlgAdminRegionTypes;
    private RichInputText txtAdminRegionType;
    private RichInputText txtAdminRegionName;
    private RichInputText txtAdminRegionCode;
    private RichPanelLabelAndMessage pnMsgTownName;
    private RichDialog dlgNewEditAdminUnit;
    private RichPanelLabelAndMessage lbMsgState;
    private RichInputText txtAgencyName;
    private RichInputText txtAgencyCode;
    private RichTable tblAgenciesLOV;

    private RichPanelLabelAndMessage pnLabelAgency;
    private RichTable clntContacts;
    private RichInputText conCode;
    private RichInputText conName;
    private RichInputText conAddress;
    private RichInputText conPhysi;
    private RichInputText conSect;
    private RichTable sector;
    private RichInputText legacyShtDesc;
    private RichInputText txtZipCode;
    private RichInputDate txtAnniversary;
    private RichSelectOneChoice txtCreditRting;
    private RichInputText txtHoldingCompany;
    private RichTable holdingCompanyTbl;
    private RichSelectOneChoice txtClientCategory;
    private UISelectItems clientSelected;
    private RichSelectItem selectClientSelected;
    private RichSelectOneChoice txtClientTypes;
    private RichInputText txtDrvExperience;
    private RichPanelLabelAndMessage txtHoldingCompanyCont;
    private RichCommandButton txtCommandBtn;
    private RichSelectOneChoice txtSacco;
    private RichCommandButton txtSelectClientTitle;
    private RichCommandButton txtSelectSignTitle;
    private RichCommandButton txtSelectDirTitle;
    private RichCommandButton txtSelectContactTitle;
    private RichDialog comfirmContinue;
    private RichInputText txtReasonForUpdate;
    private RichInputText webClientName;
    private RichInputText webClientShtDesc;
    private RichCommandButton saveShtDesc;
    private RichCommandButton addClientBranches;
    private RichCommandButton editClientBranches;
    private RichCommandButton deleteClientBranches;
    private RichTable clientBranch;
    private RichTable unAssignedBank;
    private RichSelectBooleanCheckbox unAssigned;
    private RichTable assignedBank;
    private RichTable tblTownPOBPopdir;
    private RichSelectBooleanCheckbox assignedCheck;
    private RichInputText txtDefaultBranch;
    private RichTable defaultBranch;
    private RichInputNumberSpinbox txtDebitLimit;
    private RichInputNumberSpinbox txtCrLimits;
    private RichInputText txtWebProduct;
    private RichInputText txtUserName;
    private RichTable webProductTbl;
    private RichTable tblUser;
    private RichSelectBooleanCheckbox txtPolicyCheck;
    private RichSelectBooleanCheckbox txtEndorsCheck;
    private RichTable webProductsDetails;
    private RichCommandButton txtAgencies;
    private RichTable smsTbl;
    private RichTable emailTbl;
    private RichSelectOneChoice txtPrefix;
    private RichInputText txtClientAccount;
    private RichTable clientAccountTbl;
    private RichTable marketerTbl;
    private RichPanelLabelAndMessage labelAndMessage;
    private RichCommandButton relationShipPop;
    private RichInputText txtPrefixManager;
    private RichCommandButton prefixPop;
    private RichTable prefixTbl;
    private RichPanelFormLayout panelBinding;
    private RichSelectOneChoice txtCreditAllowed;
    private RichSelectOneChoice txtCheckedByComplianceOfficer;
    private RichInputNumberSpinbox txtCreditLimit;
    private RichInputText txtNationality;
    private RichInputText txtSurburbs;
    private RichPanelLabelAndMessage txtSurburbsLbl;
    private RichTable locationTbl;
    private RichInputText txtSector;
    private RichInputText txtSmsPrefix;
    private RichSelectOneChoice txtBouncedCheque;
    private RichSelectOneChoice txtMaritalStatus;
    private RichSelectOneChoice txtModeOfComm;
    private RichInputText txtPayroll;
    private RichInputNumberSpinbox txtMinSalary;
    private RichInputNumberSpinbox txtMaxSalary;
    private RichSelectOneChoice txtType;
    private RichInputText txtBussinessPerson;
    private RichTable bussinesPersonTbl;
    private RichTable occupationTbl;
    private RichTable signaoccupationTbl;
    private RichTable specializationTbl;
    private RichDialog confirmPin;
    private RichInputDate txtDlIssueDate;
    private RichInputText txtWorkPermit;
    private RichInputFile upFile;
    private UploadedFile uploadedFile;
    private RichTable docTbl;
    private RichTable debtor;
    private RichTable prefixTbl2;
    private RichInputText txtTelPayPrefix;
    private RichInputText txtPayTel;
    private RichInputText postalCode;
    private RichTable tblCountryPop2;
    private RichInputText txtCountryName2;
    private RichInputText txtCountryCode2;
    private RichInputText txtCountryName3;
    private RichInputText txtCountryCode3;
    private RichInputText txtIntTel;
    private RichInputText txtCustomerCompany;
    private RichTable tblCountryPop3;
    private RichTable tblBankAccounts;
    private RichSelectBooleanCheckbox chkGISClient;
    private RichSelectBooleanCheckbox chkLMSClient;
    private RichInputText txtSearchCountry;
    private RichDialog dlgClientAccount;
    private RichInputText txtBACT_NAME;
    private RichInputText txtBACT_ACCOUNT_NO;
    private RichInputText txtBACT_BANK_BRANCH;
    private RichInputText txtBACT_CURRENCY;
    private RichInputText txtBACT_BANK_NAME;
    private RichInputText txtBACT_CELL_NOS;
    private RichInputText txtBACT_TEL_NOS;
    private RichInputText txtBACT_ACCOUNT_TYPE;
    private RichSelectOneChoice txtBACT_DEFAULT;
    private RichInputText txtBACT_ACC_OFFICER;
    private RichOutputLabel lblClientTabTitle;
    private RichCommandButton saveBankDetailsBtn;
    private RichShowDetailItem tabBankAccounts;
    private RichPopup bankDetailsPopUp;
    private RichInputText txtBnkIBAN;
    private RichSelectOneChoice txtBACT_STATUS;
    private RichTable systemUsers;
    private RichInputText txtAssignRemarks;
    private RichInputText txtOldAccNO;
    private RichInputText txtPreferedBenefitPaymentMode;
    private RichInputText txtPreferedBenefitPaymentModeCode;
    private RichTable tblPaymentMode;
    private RichInputText txtPreferedPremiumPaymentCode;
    private RichInputText txtPreferedPremiumPayment;
    private RichTable tblPremiumPaymode;
    private RichInputText txtEduLevel;
    private RichInputNumberSpinbox txtMonthlyIncome;
    private RichPopup employerDetailsPopup;
    private RichInputText clntEmplName;
    private RichInputText clntEmplCountryName;
    private RichInputText clntEmplTwnName;
    private RichInputText clntEmplTwnPostalZipCode;
    private RichInputText clntEmplSectorName;
    private RichSelectOneChoice clntEmplType;
    private RichInputText clntEmplPayrollNo;
    private RichInputNumberSpinbox clntEmplMonthlyIncome;
    private RichInputNumberSpinbox clntEmplMinSalary;
    private RichInputNumberSpinbox clntEmplMaxSalary;
    private RichInputDate clntEmplEmploymentDate;
    private RichInputText clntEmplTelNos;
    private RichInputText clntEmplCellNos;
    private RichInputText clntEmplFax;
    private RichInputText clntEmplCode;
    private RichInputText clntEmplCountryCode;
    private RichInputText clntEmplTwnCode;
    private RichInputText clntEmplSectorCode;
    // private RichInputText clntEmplTwnPostalCode;
    private RichTable tblEmployerDetails;
    private RichTable tblEmpCountryPop;
    private RichInputText txtSearchEmpCountry;
    private RichInputText clntEmplState;
    private RichInputText clntEmplStateCode;
    private RichPanelLabelAndMessage clntEmplStatePanel;
    private RichPanelLabelAndMessage clntEmplTownPanel;
    private RichPanelLabelAndMessage clntPostalCodePanel;
    private RichDialog dlgAdminEmpRegionTypes;
    private RichTable tblEmpStates;
    private RichTable tblEmpTownPop;
    private RichTable empPostalCodeTable;
    private RichTable tblEmpSectorPop;
    private RichCommandButton saveEmployerDetailsBtn;
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
    private RichCommandButton btnNewSignatory;
    private RichCommandButton btnEditSignatory;
    private RichCommandButton btnDeleteSignatory;
    private RichCommandButton btnSaveSignatory;
    private RichInputText txtSignatoryTitle;
    private RichInputText txtSignatoryName;
    private RichSelectOneChoice txtSignatorySourceofincome;
    private RichInputText txtSignatoryOccupation;
    private RichSelectOneChoice txtSignatoryGender;
    private RichInputText txtSignatoryNationality;
    private RichInputDate txtSignatoryDateofbirth;
    private RichInputText txtSignatoryPlaceofbirth;
    private RichInputText txtSignatoryPhoneno;
    private RichSelectOneChoice txtSignatoryMeansofid;
    private RichInputText txtSignatoryMeansOfIdVal;
    private RichInputText txtSignatoryTaxno;
    private RichInputText txtSignatoryEmail;
    private RichInputText txtSignatoryAddress;
    private RichTable directorsTable;
    //-----------start buttons--------------------//
    private RichCommandButton btnNewDirector;
    private RichCommandButton btnEditDirector;
    private RichCommandButton btnDeleteDirector;
    private RichCommandButton btnSaveDirector;
    //-----------end buttons--------------------//
    //-----------start __fields--------------------//
    private RichInputText txtDirectorTitle;
    private RichInputText txtDirectorName;
    private RichSelectOneChoice txtDirectorSourceOfIncome;
    private RichInputText txtDirectorOccupation;
    private RichSelectOneChoice txtDirectorGender;
    private RichInputText txtDirectorNationality;
    private RichInputDate txtDirectorDateOfBirth;
    // private RichInputText txtDirectorPlaceOfBirth;
    private RichInputText txtDirectorPhoneNo;
    private RichSelectOneChoice txtDirectorMeansOfId;
    private RichInputText txtDirectorMeansOfIdVal;
    private RichInputText txtDirectorTaxNo;
    private RichInputText txtDirectorEmail;
    private RichInputText txtDirectorAddress;
    private RichInputText txtDirectorYear;
    private RichInputText txtDirectorQualifications;
    private RichInputText txtDirectorPctHoldg;
    private RichInputText txtDirectorDesignation;
    private RichSelectOneChoice txtClientSourceOfIncome;
    private RichInputText txtClientPlaceOfBirth;
    private RichSelectOneChoice txtClientMeansOfId;
    private RichInputText txtClientMeansOfIdVal;
    private RichInputText txtClientUtilityBill;
    private RichInputDate txtClientMeansOfIdDateIssued;
    private RichInputDate txtClientMeansOfIdDateExpired;
    private RichInputText txtClientMeansOfIdIssuedBy;
    private RichSelectOneChoice txtClientMeansOfIdIssuingCountry;
    private RichSelectOneChoice txtClientNationalityCode;
    private RichPanelLabelAndMessage clientMeansOfIdPan;
    private RichInputText txtSignatoryNationalityCode;


    private RichInputText txtSignatoryCountry1;
    private RichCommandButton txtSelectCountry1;
    private RichInputText txtSignatoryState1;
    private RichCommandButton txtSelectState1;
    private RichInputText txtSignatoryTown;
    private RichCommandButton txtSelectSignTown;
    private RichTable tblBirthCountry;
    private RichCommandButton actionSelectBirthCountry;
    private RichCommandButton actionSelectBirthSate;
    private RichTable tblBirthSate;
    private RichTable tblTownPOBPop;
    private RichTable tblDirTitles;
    private RichInputText txtDirectorTitleCode;
    private RichTable tblDirOccup;
    private RichTable tblDirNationality;
    private RichInputText txtDirtoryCountry1;
    private RichCommandButton SelectCountryDir;
    private RichInputText txtDirtoryState;
    private RichCommandButton SelectStateDir;
    private RichInputText txtDirtoryTownDir;
    private RichCommandButton SelectTownDir;
    private RichTable tblBirthCountryDIR;
    private RichTable tblBirthSateDir;

    private RichInputText txtClntPOBCountry;
    private RichInputText txtClntPOBState;
    private RichInputText txtClntPOBTown;
    private RichCommandButton selectClntPOBCountry;
    private RichCommandButton selectClntPOBState;
    private RichCommandButton selectClntPOBTown;
    private RichTable tblClntPOBCountry;
    private RichTable tblClntPOBState;
    private RichTable tblClntPOBTown;
    private RichInputText txtClientSourceOfIncomeDet;

    private RichPanelLabelAndMessage pobClntHolder;
    private RichCommandButton btnSaveIncomeSource;
    private RichCommandButton btnNEWIncomeSource;
    private RichCommandButton btnEditSIncomeSource;
    private RichSelectOneChoice txtSourceofIncomeC;
    private RichInputText txtIncomeSourceType;
    private RichCommandButton btnDeleteIncomeSource;
    private RichShowDetailItem tabSourcesOfIncome;

    private RichCommandButton btnRefreshIncomeSource;
    private RichTable tblClntIncomeSource;


    // private RichTable docTbl;

    /**
     * Default Class Constructor
     */
    public BaseClientBacking() {
        inputstream = null;
    }

    public void setMainPanel(RichPanelBox mainPanel) {
        this.mainPanel = mainPanel;
    }

    public RichPanelBox getMainPanel() {
        return mainPanel;
    }

    public void setBtnSaveRequiredDoc(RichCommandButton btnSaveRequiredDoc) {
        this.btnSaveRequiredDoc = btnSaveRequiredDoc;
    }

    public RichCommandButton getBtnSaveRequiredDoc() {
        return btnSaveRequiredDoc;
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

    public void setTxtDocClientCodePop(RichInputText txtDocClientCodePop) {
        this.txtDocClientCodePop = txtDocClientCodePop;
    }

    public RichInputText getTxtDocClientCodePop() {
        return txtDocClientCodePop;
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

    public void setTblClients(RichTable tblClients) {
        this.tblClients = tblClients;
    }

    public RichTable getTblClients() {
        return tblClients;
    }

    public void setPanelCreateClient(RichPanelBox panelCreateClient) {
        this.panelCreateClient = panelCreateClient;
    }

    public RichPanelBox getPanelCreateClient() {
        return panelCreateClient;
    }

    public void setSbrIndividual(RichSelectBooleanRadio sbrIndividual) {
        this.sbrIndividual = sbrIndividual;
    }

    public RichSelectBooleanRadio getSbrIndividual() {
        return sbrIndividual;
    }

    public void setSbrCorporate(RichSelectBooleanRadio sbrCorporate) {
        this.sbrCorporate = sbrCorporate;
    }

    public RichSelectBooleanRadio getSbrCorporate() {
        return sbrCorporate;
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

    public void setTxtDirectClient(RichSelectOneChoice txtDirectClient) {
        this.txtDirectClient = txtDirectClient;
    }

    public RichSelectOneChoice getTxtDirectClient() {
        return txtDirectClient;
    }

    public void setTxtAccountNum(RichInputText txtAccountNum) {
        this.txtAccountNum = txtAccountNum;
    }

    public RichInputText getTxtAccountNum() {
        return txtAccountNum;
    }

    public void setTxtId(RichInputText txtId) {
        this.txtId = txtId;
    }

    public RichInputText getTxtId() {
        return txtId;
    }

    public void setTxtTitle(RichSelectOneChoice txtTitle) {
        this.txtTitle = txtTitle;
    }

    public RichSelectOneChoice getTxtTitle() {
        return txtTitle;
    }

    public void setTxtSurname(RichInputText txtSurname) {
        this.txtSurname = txtSurname;
    }

    public RichInputText getTxtSurname() {
        return txtSurname;
    }


    public void setTxtOtherNames(RichInputText txtOtherNames) {
        this.txtOtherNames = txtOtherNames;
    }

    public RichInputText getTxtOtherNames() {
        return txtOtherNames;
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

    public void setTxtCountryCode(RichInputText txtCountryCode) {
        this.txtCountryCode = txtCountryCode;
    }

    public RichInputText getTxtCountryCode() {
        return txtCountryCode;
    }

    public void setTxtCountryName(RichInputText txtCountryName) {
        this.txtCountryName = txtCountryName;
    }

    public RichInputText getTxtCountryName() {
        return txtCountryName;
    }

    public void setTxtTownCode(RichInputText txtTownCode) {
        this.txtTownCode = txtTownCode;
    }

    public RichInputText getTxtTownCode() {
        return txtTownCode;
    }

    public void setTxtTownName(RichInputText txtTownName) {
        this.txtTownName = txtTownName;
    }

    public RichInputText getTxtTownName() {
        return txtTownName;
    }

    public void setTxtDOB(RichInputDate txtDOB) {
        this.txtDOB = txtDOB;
    }

    public RichInputDate getTxtDOB() {
        return txtDOB;
    }

    public void setTxtBankAccNum(RichInputText txtBankAccNum) {
        this.txtBankAccNum = txtBankAccNum;
    }

    public RichInputText getTxtBankAccNum() {
        return txtBankAccNum;
    }

    public void setTxtPIN(RichInputText txtPIN) {
        this.txtPIN = txtPIN;
    }

    public RichInputText getTxtPIN() {
        return txtPIN;
    }

    public void setTxtIdRegNum(RichInputText txtIdRegNum) {
        this.txtIdRegNum = txtIdRegNum;
    }

    public RichInputText getTxtIdRegNum() {
        return txtIdRegNum;
    }

    public void setTxtEmail(RichInputText txtEmail) {
        this.txtEmail = txtEmail;
    }

    public RichInputText getTxtEmail() {
        return txtEmail;
    }

    public void setTxtPhone1(RichInputText txtPhone1) {
        this.txtPhone1 = txtPhone1;
    }

    public RichInputText getTxtPhone1() {
        return txtPhone1;
    }

    public void setTxtSms(RichInputText txtSms) {
        this.txtSms = txtSms;
    }

    public RichInputText getTxtSms() {
        return txtSms;
    }

    public void setTxtFax(RichInputText txtFax) {
        this.txtFax = txtFax;
    }

    public RichInputText getTxtFax() {
        return txtFax;
    }

    public void setTxtProposer(RichSelectOneChoice txtProposer) {
        this.txtProposer = txtProposer;
    }

    public RichSelectOneChoice getTxtProposer() {
        return txtProposer;
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

    public void setTxtRemarks(RichInputText txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    public RichInputText getTxtRemarks() {
        return txtRemarks;
    }

    public void setTxtWithdrawalReason(RichInputText txtWithdrawalReason) {
        this.txtWithdrawalReason = txtWithdrawalReason;
    }

    public RichInputText getTxtWithdrawalReason() {
        return txtWithdrawalReason;
    }

    public void setTxtStatus(RichSelectOneChoice txtStatus) {
        this.txtStatus = txtStatus;
    }

    public RichSelectOneChoice getTxtStatus() {
        return txtStatus;
    }

    public void setTxtRunOff(RichSelectOneChoice txtRunOff) {
        this.txtRunOff = txtRunOff;
    }

    public RichSelectOneChoice getTxtRunOff() {
        return txtRunOff;
    }

    public void setTxtSpecialTerms(RichSelectOneChoice txtSpecialTerms) {
        this.txtSpecialTerms = txtSpecialTerms;
    }

    public RichSelectOneChoice getTxtSpecialTerms() {
        return txtSpecialTerms;
    }

    public void setTxtCancelledPolicy(RichSelectOneChoice txtCancelledPolicy) {
        this.txtCancelledPolicy = txtCancelledPolicy;
    }

    public RichSelectOneChoice getTxtCancelledPolicy() {
        return txtCancelledPolicy;
    }

    public void setTxtIncreasePremium(RichSelectOneChoice txtIncreasePremium) {
        this.txtIncreasePremium = txtIncreasePremium;
    }

    public RichSelectOneChoice getTxtIncreasePremium() {
        return txtIncreasePremium;
    }

    public void setTxtDeclinedProposal(RichSelectOneChoice txtDeclinedProposal) {
        this.txtDeclinedProposal = txtDeclinedProposal;
    }

    public RichSelectOneChoice getTxtDeclinedProposal() {
        return txtDeclinedProposal;
    }

    public void setTxtBranchCode(RichInputText txtBranchCode) {
        this.txtBranchCode = txtBranchCode;
    }

    public RichInputText getTxtBranchCode() {
        return txtBranchCode;
    }

    public void setTxtBranchName(RichInputText txtBranchName) {
        this.txtBranchName = txtBranchName;
    }

    public RichInputText getTxtBranchName() {
        return txtBranchName;
    }

    public void setTxtDivisionCode(RichInputText txtDivisionCode) {
        this.txtDivisionCode = txtDivisionCode;
    }

    public RichInputText getTxtDivisionCode() {
        return txtDivisionCode;
    }

    public void setTxtDivisionName(RichInputText txtDivisionName) {
        this.txtDivisionName = txtDivisionName;
    }

    public RichInputText getTxtDivisionName() {
        return txtDivisionName;
    }

    public void setTxtAccountManager(RichInputText txtAccountManager) {
        this.txtAccountManager = txtAccountManager;
    }

    public RichInputText getTxtAccountManager() {
        return txtAccountManager;
    }

    public void setTxtSectorCode(RichInputText txtSectorCode) {
        this.txtSectorCode = txtSectorCode;
    }

    public RichInputText getTxtSectorCode() {
        return txtSectorCode;
    }

    public void setTxtSectorName(RichInputText txtSectorName) {
        this.txtSectorName = txtSectorName;
    }

    public RichInputText getTxtSectorName() {
        return txtSectorName;
    }

    public void setTxtDomicileCountriesCode(RichInputText txtDomicileCountriesCode) {
        this.txtDomicileCountriesCode = txtDomicileCountriesCode;
    }

    public RichInputText getTxtDomicileCountriesCode() {
        return txtDomicileCountriesCode;
    }

    public void setTxtDomicileCountriesName(RichInputText txtDomicileCountriesName) {
        this.txtDomicileCountriesName = txtDomicileCountriesName;
    }

    public RichInputText getTxtDomicileCountriesName() {
        return txtDomicileCountriesName;
    }

    public void setTblCountryPop(RichTable tblCountryPop) {
        this.tblCountryPop = tblCountryPop;
    }

    public RichTable getTblCountryPop() {
        return tblCountryPop;
    }

    public void setTblTownPop(RichTable tblTownPop) {
        this.tblTownPop = tblTownPop;
    }

    public RichTable getTblTownPop() {
        return tblTownPop;
    }

    public void setTxtClientCode(RichInputText txtClientCode) {
        this.txtClientCode = txtClientCode;
    }

    public RichInputText getTxtClientCode() {
        return txtClientCode;
    }

    public void setContactName1(RichInputText contactName1) {
        this.contactName1 = contactName1;
    }

    public RichInputText getContactName1() {
        return contactName1;
    }

    public void setContactPhone1(RichInputText contactPhone1) {
        this.contactPhone1 = contactPhone1;
    }

    public RichInputText getContactPhone1() {
        return contactPhone1;
    }

    public void setContactEmail1(RichInputText contactEmail1) {
        this.contactEmail1 = contactEmail1;
    }

    public RichInputText getContactEmail1() {
        return contactEmail1;
    }

    public void setContactName2(RichInputText contactName2) {
        this.contactName2 = contactName2;
    }

    public RichInputText getContactName2() {
        return contactName2;
    }

    public void setContactPhone2(RichInputText contactPhone2) {
        this.contactPhone2 = contactPhone2;
    }

    public RichInputText getContactPhone2() {
        return contactPhone2;
    }

    public void setContactEmail2(RichInputText contactEmail2) {
        this.contactEmail2 = contactEmail2;
    }

    public RichInputText getContactEmail2() {
        return contactEmail2;
    }

    public void setBtnShowContactPersons(RichCommandButton btnShowContactPersons) {
        this.btnShowContactPersons = btnShowContactPersons;
    }

    public RichCommandButton getBtnShowContactPersons() {
        return btnShowContactPersons;
    }

    public void setTxtContactName1(RichInputText txtContactName1) {
        this.txtContactName1 = txtContactName1;
    }

    public RichInputText getTxtContactName1() {
        return txtContactName1;
    }

    public void setTxtContactPhone1(RichInputText txtContactPhone1) {
        this.txtContactPhone1 = txtContactPhone1;
    }

    public RichInputText getTxtContactPhone1() {
        return txtContactPhone1;
    }

    public void setTxtContactEmail1(RichInputText txtContactEmail1) {
        this.txtContactEmail1 = txtContactEmail1;
    }

    public RichInputText getTxtContactEmail1() {
        return txtContactEmail1;
    }

    public void setTxtContactName2(RichInputText txtContactName2) {
        this.txtContactName2 = txtContactName2;
    }

    public RichInputText getTxtContactName2() {
        return txtContactName2;
    }

    public void setTxtContactPhone2(RichInputText txtContactPhone2) {
        this.txtContactPhone2 = txtContactPhone2;
    }

    public RichInputText getTxtContactPhone2() {
        return txtContactPhone2;
    }

    public void setTxtContactEmail2(RichInputText txtContactEmail2) {
        this.txtContactEmail2 = txtContactEmail2;
    }

    public RichInputText getTxtContactEmail2() {
        return txtContactEmail2;
    }

    public void setTblSectorPop(RichTable tblSectorPop) {
        this.tblSectorPop = tblSectorPop;
    }

    public RichTable getTblSectorPop() {
        return tblSectorPop;
    }

    public void setTblDomicileCountryPop(RichTable tblDomicileCountryPop) {
        this.tblDomicileCountryPop = tblDomicileCountryPop;
    }

    public RichTable getTblDomicileCountryPop() {
        return tblDomicileCountryPop;
    }

    public void setTblAccountManagersPop(RichTable tblAccountManagersPop) {
        this.tblAccountManagersPop = tblAccountManagersPop;
    }

    public RichTable getTblAccountManagersPop() {
        return tblAccountManagersPop;
    }

    public void setTxtAccountManagerCode(RichInputText txtAccountManagerCode) {
        this.txtAccountManagerCode = txtAccountManagerCode;
    }

    public RichInputText getTxtAccountManagerCode() {
        return txtAccountManagerCode;
    }

    public void setTblBankBranchPop(RichTable tblBankBranchPop) {
        this.tblBankBranchPop = tblBankBranchPop;
    }

    public RichTable getTblBankBranchPop() {
        return tblBankBranchPop;
    }

    public void setTblBranchDivisionPop(RichTable tblBranchDivisionPop) {
        this.tblBranchDivisionPop = tblBranchDivisionPop;
    }

    public RichTable getTblBranchDivisionPop() {
        return tblBranchDivisionPop;
    }

    public void setBtnCreateUpdateClient(RichCommandButton btnCreateUpdateClient) {
        this.btnCreateUpdateClient = btnCreateUpdateClient;
    }

    public RichCommandButton getBtnCreateUpdateClient() {
        return btnCreateUpdateClient;
    }

    public void setTxtAccUsername(RichInputText txtAccUsername) {
        this.txtAccUsername = txtAccUsername;
    }

    public RichInputText getTxtAccUsername() {
        return txtAccUsername;
    }

    public void setTxtAccFullNames(RichInputText txtAccFullNames) {
        this.txtAccFullNames = txtAccFullNames;
    }

    public RichInputText getTxtAccFullNames() {
        return txtAccFullNames;
    }

    public void setTxtAccPassword(RichInputText txtAccPassword) {
        this.txtAccPassword = txtAccPassword;
    }

    public RichInputText getTxtAccPassword() {
        return txtAccPassword;
    }

    public void setTxtAccPersonelRank(RichInputText txtAccPersonelRank) {
        this.txtAccPersonelRank = txtAccPersonelRank;
    }

    public RichInputText getTxtAccPersonelRank() {
        return txtAccPersonelRank;
    }

    public void setTxtAccStatus(RichSelectOneChoice txtAccStatus) {
        this.txtAccStatus = txtAccStatus;
    }

    public RichSelectOneChoice getTxtAccStatus() {
        return txtAccStatus;
    }

    public void setTxtAccAllowLogin(RichSelectOneChoice txtAccAllowLogin) {
        this.txtAccAllowLogin = txtAccAllowLogin;
    }

    public RichSelectOneChoice getTxtAccAllowLogin() {
        return txtAccAllowLogin;
    }

    public void setTxtAccEmail(RichInputText txtAccEmail) {
        this.txtAccEmail = txtAccEmail;
    }

    public RichInputText getTxtAccEmail() {
        return txtAccEmail;
    }

    public void setTxtAccCode(RichInputText txtAccCode) {
        this.txtAccCode = txtAccCode;
    }

    public RichInputText getTxtAccCode() {
        return txtAccCode;
    }

    public void setBtnSaveUpdateWebAccount(RichCommandButton btnSaveUpdateWebAccount) {
        this.btnSaveUpdateWebAccount = btnSaveUpdateWebAccount;
    }

    public RichCommandButton getBtnSaveUpdateWebAccount() {
        return btnSaveUpdateWebAccount;
    }

    public void setTxtClientTitleCode(RichInputText txtClientTitleCode) {
        this.txtClientTitleCode = txtClientTitleCode;
    }

    public RichInputText getTxtClientTitleCode() {
        return txtClientTitleCode;
    }

    public void setHiddenClientCode(RichInputText hiddenClientCode) {
        this.hiddenClientCode = hiddenClientCode;
    }

    public RichInputText getHiddenClientCode() {
        return hiddenClientCode;
    }

    public void setTblClientAccounts(RichTable tblClientAccounts) {
        this.tblClientAccounts = tblClientAccounts;
    }

    public RichTable getTblClientAccounts() {
        return tblClientAccounts;
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

    public void setTxtAccClientCode(RichInputText txtAccClientCode) {
        this.txtAccClientCode = txtAccClientCode;
    }

    public RichInputText getTxtAccClientCode() {
        return txtAccClientCode;
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

    public void setTxtClientAccStatus(RichSelectOneChoice txtClientAccStatus) {
        this.txtClientAccStatus = txtClientAccStatus;
    }

    public RichSelectOneChoice getTxtClientAccStatus() {
        return txtClientAccStatus;
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

    public void setBtnSaveUpdateClientAccount(RichCommandButton btnSaveUpdateClientAccount) {
        this.btnSaveUpdateClientAccount = btnSaveUpdateClientAccount;
    }

    public RichCommandButton getBtnSaveUpdateClientAccount() {
        return btnSaveUpdateClientAccount;
    }

    public void setPanelDetailSystems(RichPanelBox panelDetailSystems) {
        this.panelDetailSystems = panelDetailSystems;
    }

    public RichPanelBox getPanelDetailSystems() {
        return panelDetailSystems;
    }

    public void setTxtSelectedClientSystemCode(RichInputText txtSelectedClientSystemCode) {
        this.txtSelectedClientSystemCode = txtSelectedClientSystemCode;
    }

    public RichInputText getTxtSelectedClientSystemCode() {
        return txtSelectedClientSystemCode;
    }

    public void setBtnAddClientSystem(RichCommandButton btnAddClientSystem) {
        this.btnAddClientSystem = btnAddClientSystem;
    }

    public RichCommandButton getBtnAddClientSystem() {
        return btnAddClientSystem;
    }

    public void setBtnRemoveClientSystem(RichCommandButton btnRemoveClientSystem) {
        this.btnRemoveClientSystem = btnRemoveClientSystem;
    }

    public RichCommandButton getBtnRemoveClientSystem() {
        return btnRemoveClientSystem;
    }

    public void setTxtPassportNo(RichInputText txtPassportNo) {
        this.txtPassportNo = txtPassportNo;
    }

    public RichInputText getTxtPassportNo() {
        return txtPassportNo;
    }

    public void setTxtBrnDivName(RichInputText txtBrnDivName) {
        this.txtBrnDivName = txtBrnDivName;
    }

    public RichInputText getTxtBrnDivName() {
        return txtBrnDivName;
    }

    public void setBrnDivLov(RichTable brnDivLov) {
        this.brnDivLov = brnDivLov;
    }

    public RichTable getBrnDivLov() {
        return brnDivLov;
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

    public void tblClientsSelectionListener(SelectionEvent selectionEvent) {
        Object key2 = tblClients.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("ClientCode",
                                 nodeBinding.getAttribute("code"));
            btnDeleteClient.setDisabled(false);
            GlobalCC.refreshUI(btnDeleteClient);
        }
    }


    public String actionSelectEmpCountryPop() {
        Object key2 = tblEmpCountryPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            clntEmplCountryName.setValue(nodeBinding.getAttribute("name"));
            clntEmplCountryCode.setValue(nodeBinding.getAttribute("code"));
            Object adminUnit = nodeBinding.getAttribute("administrativeType");
            if (adminUnit == null) {
                clntEmplStatePanel.setVisible(false);
                clntEmplTownPanel.setVisible(false);
                clntPostalCodePanel.setVisible(false);
            } else {
                clntEmplStatePanel.setLabel(GlobalCC.formatAdminUnitSingular(adminUnit) +
                                            ":");
                clntEmplStatePanel.setVisible(true);
                clntEmplTownPanel.setVisible(true);
                clntPostalCodePanel.setVisible(true);
                dlgAdminEmpRegionTypes.setTitle(GlobalCC.formatAdminUnitPlural(adminUnit) +
                                                " List");
            }

            clntEmplState.setValue(null);
            clntEmplStateCode.setValue(null);
            clntEmplTwnName.setValue(null);
            clntEmplTwnCode.setValue(null);
            // clntEmplTwnPostalCode.setValue(null);
            clntEmplTwnPostalZipCode.setValue(null);

            GlobalCC.refreshUI(clntEmplState);
            GlobalCC.refreshUI(clntEmplStateCode);
            GlobalCC.refreshUI(clntEmplTwnName);
            GlobalCC.refreshUI(clntEmplTwnCode);
            // GlobalCC.refreshUI(clntEmplTwnPostalCode);
            GlobalCC.refreshUI(clntEmplTwnPostalZipCode);

            GlobalCC.refreshUI(clntEmplCountryName);
            GlobalCC.refreshUI(clntEmplCountryCode);
            GlobalCC.refreshUI(clntEmplStatePanel);
            GlobalCC.refreshUI(clntEmplTownPanel);
            GlobalCC.refreshUI(clntPostalCodePanel);

            // Set the country code to be used to fetch the states
            session.setAttribute("countryCode",
                                 nodeBinding.getAttribute("code"));

            // fetch states
            ADFUtils.findIterator("fetchStatesByCountry1Iterator").executeQuery();
        }

        GlobalCC.hidePopup("pt1:empCountryPop");
        return null;
    }

    /**
     * Assign the selected country into the text fields
     *
     * @return null
     */
    public String actionSelectCountryPop() {
        Object key2 = tblCountryPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String country = (String)nodeBinding.getAttribute("name");
            txtCountryName.setValue(nodeBinding.getAttribute("name"));
            txtCountryCode.setValue(nodeBinding.getAttribute("code"));
            Object adminUnit = nodeBinding.getAttribute("administrativeType");
            if (adminUnit == null) {
                lbMsgState.setVisible(false);
                pnMsgTownName.setVisible(false);

            } else {
                lbMsgState.setLabel(GlobalCC.formatAdminUnitSingular(adminUnit) +
                                    ":");
                lbMsgState.setVisible(true);
                pnMsgTownName.setVisible(true);
                dlgAdminRegionTypes.setTitle(GlobalCC.formatAdminUnitPlural(adminUnit) +
                                             " List");
            }
            txtAdminRegionCode.setValue(null);
            txtAdminRegionName.setValue(null);
            txtTownName.setValue(null);
            txtTownCode.setValue(null);
            txtZipCode.setValue(null);
            txtSurburbs.setValue(null);
            // Set the country code to be used to fetch the states
            session.setAttribute("countryCode",
                                 nodeBinding.getAttribute("code"));

            if (session.getAttribute("COUNTRY_NAME").toString().equalsIgnoreCase(country)) {
                txtWorkPermit.setVisible(false);
            } else {
                txtWorkPermit.setVisible(true);
            }

            ADFUtils.findIterator("fetchStatesByCountry1Iterator").executeQuery();
            ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();

            GlobalCC.refreshUI(txtWorkPermit);
            GlobalCC.refreshUI(tblStates);
            GlobalCC.refreshUI(txtPrefixManager);
            GlobalCC.refreshUI(lbMsgState);
            GlobalCC.refreshUI(txtSurburbs);
            GlobalCC.refreshUI(pnMsgTownName);
            GlobalCC.refreshUI(txtCountryName);

        }
        GlobalCC.dismissPopUp("pt1", "countryPop");
        return null;
    }

    public String actionSelectCountryPop2() {
        Object key2 = tblCountryPop2.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding == null) {
            GlobalCC.sysInformation("No Record selected");
            return null;
        }

        if (nodeBinding != null) {
            txtCountryName2.setValue(nodeBinding.getAttribute("name"));
            txtCountryCode2.setValue(nodeBinding.getAttribute("zipCode"));
            // Set the country code to be used to fetch the states
            session.setAttribute("GsmZipCode",
                                 nodeBinding.getAttribute("zipCode"));
            session.setAttribute("GsmCountryCode",
                                 nodeBinding.getAttribute("code"));
            GlobalCC.refreshUI(txtCountryName2);
            GlobalCC.refreshUI(txtCountryCode2);
            System.out.println(nodeBinding.getAttribute("shortDesc"));
            System.out.println("GsmCountryCode: " +
                               session.getAttribute("GsmCountryCode"));

        }
        GlobalCC.dismissPopUp("pt1", "countryPop2");
        return null;
    }

    public String actionSelectCountryPop3() {
        Object key2 = tblCountryPop3.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            txtCountryName3.setValue(nodeBinding.getAttribute("name"));
            txtCountryCode3.setValue(nodeBinding.getAttribute("zipCode"));
            GlobalCC.refreshUI(txtCountryName3);
            GlobalCC.refreshUI(txtCountryCode3);


        }
        GlobalCC.dismissPopUp("pt1", "countryPop3");
        return null;
    }


    public String actionSelectCountryPop344() {
        Object key2 = tblCountryPop367.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            txtSignatoryNationality.setValue(nodeBinding.getAttribute("nationality"));
            txtSignatoryNationalityCode.setValue(nodeBinding.getAttribute("zipCode"));
            GlobalCC.refreshUI(txtSignatoryNationality);
            GlobalCC.refreshUI(txtSignatoryNationalityCode);


        }
        GlobalCC.dismissPopUp("pt1", "countryPop344");
        return null;
    }

    public String actionSelectBirthCountry() {
        Object key2 = tblBirthCountry.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            txtSignatoryCountry1.setValue(nodeBinding.getAttribute("name"));
            session.setAttribute("countryCode1",
                                 nodeBinding.getAttribute("code"));
            //txtSignatoryNationalityCode.setValue(nodeBinding.getAttribute("zipCode"));
            GlobalCC.refreshUI(txtSignatoryCountry1);
            // GlobalCC.refreshUI(txtSignatoryNationalityCode);


        }
        GlobalCC.dismissPopUp("pt1", "CountryPop1");
        return null;
    }


    public String actionSelectBirthCountryDIR() {
        Object key2 = tblBirthCountryDIR.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            txtDirtoryCountry1.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtDirtoryCountry1);
            GlobalCC.refreshUI(txtDirtoryCountry1);
            session.setAttribute("countryCodeDir",
                                 nodeBinding.getAttribute("code"));
            //txtSignatoryNationalityCode.setValue(nodeBinding.getAttribute("zipCode"));

            // GlobalCC.refreshUI(txtSignatoryNationalityCode);


        }
        GlobalCC.dismissPopUp("pt1", "CountryPop1DIR");
        return null;
    }


    public String actionSelectClntPOBCountry() {
        Object key2 = tblClntPOBCountry.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            txtClntPOBCountry.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtClntPOBCountry);
            session.setAttribute("countryCodeClnt",
                                 nodeBinding.getAttribute("code"));
            //txtSignatoryNationalityCode.setValue(nodeBinding.getAttribute("zipCode"));

            // GlobalCC.refreshUI(txtSignatoryNationalityCode);


        }
        GlobalCC.dismissPopUp("pt1", "CountryClntPOBPop");
        return null;
    }

    public String actionSelectClntPOBSate() {
        Object key2 = tblClntPOBState.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtClntPOBState.setValue(nodeBinding.getAttribute("stateName"));
            GlobalCC.refreshUI(txtClntPOBState);
            session.setAttribute("stateCodeClnt",
                                 nodeBinding.getAttribute("stateCode"));
            //txtSignatoryNationalityCode.setValue(nodeBinding.getAttribute("zipCode"));

            // GlobalCC.refreshUI(txtSignatoryNationalityCode);


        }
        GlobalCC.dismissPopUp("pt1", "StateClntPOBPop");
        return null;
    }


    public String actionShowStateClnt() {
        if (txtClntPOBCountry.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Select place of birth country first");
            return null;
        }
        ADFUtils.findIterator("fetchStatesByCountryClntIterator").executeQuery();
        GlobalCC.refreshUI(tblClntPOBState);
        GlobalCC.showPopUp("pt1", "StateClntPOBPop");
        return null;
    }

    public String actionShowTownsClnt() {
        if (txtClntPOBState.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Select place of birth State first");
            return null;
        }
        ADFUtils.findIterator("fetchTownsWithZipCodesByStateClntIterator").executeQuery();
        GlobalCC.refreshUI(tblClntPOBTown);
        GlobalCC.showPopUp("pt1", "TownClntPOBPop");
        return null;
    }

    public String actionShowTownsDir() {
        if (txtDirtoryCountry1.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Select place of birth Country first");
            return null;
        }
        ADFUtils.findIterator("fetchStatesByCountryClntIterator").executeQuery();
        GlobalCC.refreshUI(tblBirthSateDir);
        GlobalCC.showPopUp("pt1", "StatePop1Dir");
        return null;
    }


    public String actionSelectClntPOBTown() {
        Object key2 = tblClntPOBTown.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            txtClntPOBTown.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtClntPOBTown);

        }
        GlobalCC.dismissPopUp("pt1", "TownClntPOBPop");
        return null;
    }


    public String actionSelectBirthSateDir() {
        Object key2 = tblBirthSateDir.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            txtDirtoryState.setValue(nodeBinding.getAttribute("stateName"));
            session.setAttribute("stateCodeDir",
                                 nodeBinding.getAttribute("stateCode"));
            //txtSignatoryNationalityCode.setValue(nodeBinding.getAttribute("zipCode"));
            GlobalCC.refreshUI(txtDirtoryState);
            // GlobalCC.refreshUI(txtSignatoryNationalityCode);


        }
        GlobalCC.dismissPopUp("pt1", "StatePop1Dir");
        return null;
    }

    public String actionSelectBirthSate() {
        Object key2 = tblBirthSate.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            txtSignatoryState1.setValue(nodeBinding.getAttribute("stateName"));
            session.setAttribute("stateCode1",
                                 nodeBinding.getAttribute("stateCode"));
            //txtSignatoryNationalityCode.setValue(nodeBinding.getAttribute("zipCode"));
            GlobalCC.refreshUI(txtSignatoryState1);
            // GlobalCC.refreshUI(txtSignatoryNationalityCode);


        }
        GlobalCC.dismissPopUp("pt1", "StatePop1");
        return null;
    }


    public String actionAcceptEmpTown() {
        Object key2 = tblEmpTownPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            session.setAttribute("townCode", nodeBinding.getAttribute("code"));

            clntEmplTwnName.setValue(nodeBinding.getAttribute("name"));
            clntEmplTwnCode.setValue(nodeBinding.getAttribute("code"));
            // clntEmplTwnPostalCode.setValue(null);
            clntEmplTwnPostalZipCode.setValue(nodeBinding.getAttribute("postalZipCode"));


            GlobalCC.refreshUI(clntEmplTwnName);
            GlobalCC.refreshUI(clntEmplTwnCode);
            // GlobalCC.refreshUI(clntEmplTwnPostalCode);
            GlobalCC.refreshUI(clntEmplTwnPostalZipCode);
        }

        GlobalCC.dismissPopUp("pt1", "townEmpPop");
        return null;
    }

    /**
     * Assign the town selected from the popup to the input text fields.
     *
     * @return null
     */
    public String actionAcceptAgencyTown() {
        Object key2 = tblTownPop.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {
            txtTownName.setValue(n.getAttribute("name"));
            txtTownCode.setValue(n.getAttribute("code"));
            session.setAttribute("townCode", n.getAttribute("code"));
            //txtSurburbsLbl.setValue(n.getAttribute("postalZipCode"));
            txtZipCode.setValue(n.getAttribute("postalZipCode"));
            txtSurburbs.setValue(n.getAttribute("postalZipCode"));

            GlobalCC.refreshUI(txtZipCode);
            GlobalCC.refreshUI(txtTownName);
            GlobalCC.refreshUI(txtSurburbs);
        }

        GlobalCC.dismissPopUp("pt1", "townPop");
        return null;
    }


    public String actionAcceptPOBTown() {
        Object key2 = tblTownPOBPop.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {
            txtSignatoryTown.setValue(n.getAttribute("name"));
            // txtTownCode.setValue(n.getAttribute("code"));
            session.setAttribute("townCode", n.getAttribute("code"));
            //txtSurburbsLbl.setValue(n.getAttribute("postalZipCode"));
            //            txtZipCode.setValue(n.getAttribute("postalZipCode"));
            //            txtSurburbs.setValue(n.getAttribute("postalZipCode"));


            GlobalCC.refreshUI(txtSignatoryTown);
            //GlobalCC.refreshUI(txtTownCode);
        }

        GlobalCC.dismissPopUp("pt1", "townPop13");
        return null;
    }

    public String actionAcceptPOBTownDir() {
        Object key2 = tblTownPOBPopdir.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {
            txtDirtoryTownDir.setValue(n.getAttribute("name"));
            // txtTownCode.setValue(n.getAttribute("code"));
            session.setAttribute("townCode", n.getAttribute("code"));
            //txtSurburbsLbl.setValue(n.getAttribute("postalZipCode"));
            //            txtZipCode.setValue(n.getAttribute("postalZipCode"));
            //            txtSurburbs.setValue(n.getAttribute("postalZipCode"));


            GlobalCC.refreshUI(txtDirtoryTownDir);
            //GlobalCC.refreshUI(txtTownCode);
        }

        GlobalCC.dismissPopUp("pt1", "townPop14dir");
        return null;
    }


    public String actionShowEmpTownPopup() {
        BigDecimal stateCode =
            GlobalCC.checkBDNullValues(clntEmplStateCode.getValue());
        session.setAttribute("stateCode", stateCode);
        if (stateCode == null) {
            GlobalCC.errorValueNotEntered("Select " +
                                          renderer.getDefaultAdminUnitSingular() +
                                          " first!");
            return null;
        }
        ADFUtils.findIterator("fetchTownsWithZipCodesByStateIterator").executeQuery();
        GlobalCC.showPopup("pt1:townEmpPop");
        return null;
    }

    /**
     * Opens up a popup dialog for selecting the towns, but only if a country
     * has been selected.
     *
     * @return null
     */

    public String actionShowTownPopup() {

        BigDecimal stateCode =
            GlobalCC.checkBDNullValues(txtAdminRegionCode.getValue());
        session.setAttribute("stateCode", stateCode);
        if (stateCode == null) {
            GlobalCC.errorValueNotEntered("Select " +
                                          renderer.getDefaultAdminUnitSingular() +
                                          " first!");
            return null;
        }
        ADFUtils.findIterator("fetchTownsWithZipCodesByStateIterator").executeQuery();
        GlobalCC.showPopup("pt1:townPop");
        return null;
    }

    public String actionShowParentCompany() {

        GlobalCC.showPopup("pt1:parentCompanyPop");
        return null;
    }

    public String actionShowBranches() {
        try {
            ADFUtils.findIterator("findBranchesIterator").executeQuery();
            GlobalCC.refreshUI(tblRegBranches);
            GlobalCC.showPop("pt1:branchPop");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    /**
     * Retrieve the client details from the table row and fill in the details
     * section panel.
     *
     * @return null
     */
    public String actionFindClientDetails() {

        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        BigDecimal clientCode = null;
        try {

            Authorization auth = new Authorization();
            String AccessGranted =
                auth.checkUserRights("CLCR", "CLAU", "CLAE", null, null);
            if (("Y".equalsIgnoreCase(AccessGranted)) != true) {
                GlobalCC.accessDenied();
                return null;
            }

            // chcek credit limit
            String process = "CLCR";
            String processArea = "CLAU";
            String processSubArea = "CLCRDET";
            AccessGranted =
                    auth.checkUserRights(process, processArea, processSubArea,
                                         null, null);
            if(txtCreditAllowed != null && txtCreditLimit!=null ){ 
				if(AccessGranted.equalsIgnoreCase("Y")){
					txtCreditAllowed.setDisabled(false);
					txtCreditLimit.setDisabled(false);
				} else {
					txtCreditAllowed.setDisabled(true);
					txtCreditLimit.setDisabled(true);
				}
				// end check credit limit
			}

            txtReasonForUpdate.setVisible(true);
            // tbClientListingBasedOnNames.setRendered(false);
            // GlobalCC.refreshUI(tbClientListingBasedOnNames);
            // txtAccountOfficerCode.setValue(null);
            // txtAccountOfficerName.setValue(null);

            List<UIComponent> children = null;

            if (panelCollSearch.getChildren() != null) {

                children = panelCollSearch.getChildren();

            }

            UIComponent component = children.get(0);

            RichTable rpt = null;
            for (int i = 0; i < children.size(); i++) {
                component = children.get(i);
                if (component.getId().equalsIgnoreCase("t1200")) {
                    rpt = (RichTable)component;
                }
            }

            Object key2 = rpt.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                clientCode =
                        GlobalCC.checkBDNullValues(nodeBinding.getAttribute("code"));
            }
            if (clientCode == null) {
                return null;
            }
            session.setAttribute("ClientCode", clientCode);

            //change ticket
            session.removeAttribute("taskID");
            session.removeAttribute("activityName");
            session.setAttribute("ticketProcessShtDesc",
                                 GlobalCC.clientProcess);
            session.setAttribute("ticketEntityCode", clientCode);

            JBPMEngine bpm = JBPMEngine.getInstance();
            bpm.getCurrentEntityTask();
            GlobalCC.refreshUI(lblClientTabTitle);


            ADFUtils.findIterator("fetchClientEmployersIterator").executeQuery();
            GlobalCC.refreshUI(tblEmployerDetails);

            actionLoadByClientCode();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(connection, e);
        } finally {
            DbUtils.closeQuietly(connection, stmt, rs);
        }

        return null;
    }


    /**
     * Assign the new contact persons to the respective fields in the detail
     * section.
     *
     * @return null
     */
    public String actionAcceptContactPersons() {
        txtContactName1.setValue(contactName1.getValue());
        txtContactPhone1.setValue(contactPhone1.getValue());
        txtContactEmail1.setValue(contactEmail1.getValue());
        txtContactName2.setValue(contactName2.getValue());
        txtContactPhone2.setValue(contactPhone2.getValue());
        txtContactEmail2.setValue(contactEmail2.getValue());
        GlobalCC.refreshUI(txtContactName1);
        GlobalCC.refreshUI(txtContactPhone1);

        GlobalCC.refreshUI(txtContactEmail1);
        GlobalCC.refreshUI(txtContactName2);
        GlobalCC.refreshUI(txtContactPhone2);
        GlobalCC.refreshUI(txtContactEmail2);
        GlobalCC.dismissPopUp("pt1", "contactPersonsPopup");
        return null;
    }

    /**
     * Clear the text fields in the popup then open the popup dialog.
     *
     * @return null
     */
    public String actionShowContactPersons() {
        /*contactName1.setValue("");
        contactPhone1.setValue("");
        contactEmail1.setValue("");
        contactName2.setValue("");
        contactPhone2.setValue("");
        contactEmail2.setValue("");*/

        // Open the popup dialog to add new contact Persons
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:contactPersonsPopup" + "').show(hints);");
        return null;
    }


    public String findEmpPostalCodeSelected() {
        Object key = empPostalCodeTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.dismissPopUp("pt1", "empPostalCodePOP");
            return null;
        }
        session.setAttribute("locCode",
                             nodeBinding.getAttribute("pstZipCode"));
        GlobalCC.dismissPopUp("pt1", "empPostalCodePOP");
        return null;
    }

    public String findPostalCodeSelected() {
        Object key = postalCodeTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding == null) {
            GlobalCC.dismissPopUp("pt1", "postalCodePOP");
            return null;
        }
        System.out.println("nodeBinding.getAttribute(\"pstZipCode\")==" +
                           nodeBinding.getAttribute("pstZipCode"));

        //postalCode.setValue(nodeBinding.getAttribute("pstZipCode"));
        session.setAttribute("locCode",
                             nodeBinding.getAttribute("pstZipCode"));
        txtSurburbs.setValue(nodeBinding.getAttribute("pstZipCode"));
        txtZipCode.setValue(nodeBinding.getAttribute("pstZipCode"));
        GlobalCC.refreshUI(txtSurburbs);
        GlobalCC.dismissPopUp("pt1", "postalCodePOP");


        return null;

    }

    public String actionEmpHidePostalCode() {
        GlobalCC.dismissPopUp("pt1", "empPostalCodePOP");
        return null;
    }

    public String actionHidePostalCode() {
        GlobalCC.dismissPopUp("pt1", "postalCodePOP");
        //GlobalCC.dismissPopUp("crm", "postalCodePOP");
        return null;
    }

    public String actionAcceptEmpSector() {
        Object key2 = tblEmpSectorPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            clntEmplSectorName.setValue(nodeBinding.getAttribute("name"));
            clntEmplSectorCode.setValue(nodeBinding.getAttribute("code"));
            session.setAttribute("sectorCode",
                                 nodeBinding.getAttribute("code"));
            session.setAttribute("sectorName",
                                 nodeBinding.getAttribute("name"));

            ADFUtils.findIterator("findOccupationIterator").executeQuery();
            ADFUtils.findIterator("findSpecializationsIterator").executeQuery();

            GlobalCC.refreshUI(clntEmplSectorName);
            GlobalCC.refreshUI(clntEmplSectorCode);
            GlobalCC.hidePopup("pt1:empSectorPop");
        }
        return null;
    }

    public String actionAcceptSector() {
        Object key2 = tblSectorPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSectorName.setValue(nodeBinding.getAttribute("name"));

            txtSectorCode.setValue(nodeBinding.getAttribute("code"));
            session.setAttribute("sectorCode",
                                 nodeBinding.getAttribute("code"));
            session.setAttribute("sectorName",
                                 nodeBinding.getAttribute("name"));
            txtSrchSectorName.setValue(session.getAttribute("sectorName"));

            ADFUtils.findIterator("findOccupationIterator").executeQuery();
            ADFUtils.findIterator("findSpecializationsIterator").executeQuery();

            GlobalCC.refreshUI(txtSrchSectorName);
            GlobalCC.refreshUI(txtSectorName);
            GlobalCC.refreshUI(txtSectorCode);
            GlobalCC.refreshUI(txtCltOccupation);
            GlobalCC.refreshUI(occupationTbl);
            GlobalCC.refreshUI(specializationTbl);
            GlobalCC.hidePopup("pt1:sectorPop");

        }
        return null;
    }

    public String actionAcceptDomicileCountry() {
        Object key2 = tblDomicileCountryPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDomicileCountriesName.setValue(nodeBinding.getAttribute("name"));
            txtDomicileCountriesCode.setValue(nodeBinding.getAttribute("code"));
            System.out.println("code: " + nodeBinding.getAttribute("code"));
            GlobalCC.refreshUI(txtDomicileCountriesName);
            GlobalCC.refreshUI(txtDomicileCountriesCode);
        }

        GlobalCC.dismissPopUp("pt1", "domicileCountryPop");
        return null;
    }

    /**
     * Assign the appropriate account manager from the popup to the details
     * section
     *
     * @return null
     */
    public String actionAcceptAccountManager() {
        Object key2 = tblAccountManagersPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAccountManagerCode.setValue(nodeBinding.getAttribute("userCode"));
            txtAccountManager.setValue(nodeBinding.getAttribute("userFullname"));
            GlobalCC.refreshUI(txtAccountManager);

        }
        GlobalCC.dismissPopUp("pt1", "accountManagersPop");
        return null;
    }

    public String bankBranchSelected() {
        Object key2 = tblBankBranchPop.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {

            session.setAttribute("branchCode",
                                 n.getAttribute("bankBranchCode"));

            session.setAttribute("BACT_BBR_CODE",
                                 n.getAttribute("bankBranchCode"));
            if (txtBACT_BANK_BRANCH != null) {
                txtBACT_BANK_BRANCH.setValue(n.getAttribute("branchName"));
            }

            GlobalCC.refreshUI(txtBACT_BANK_BRANCH);
            txtBranchName.setValue(n.getAttribute("branchName"));
            txtBranchCode.setValue(n.getAttribute("bankBranchCode"));
            GlobalCC.refreshUI(txtBranchName);
            GlobalCC.refreshUI(txtBranchCode);

        }
        GlobalCC.dismissPopUp("pt1", "bankBranchPop");

        return null;
    }


    public String acceptBranchDivision() {
        Object key2 = tblBranchDivisionPop.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;


        if (nodeBinding != null) {
            txtDivisionCode.setValue(nodeBinding.getAttribute("DIV_CODE"));
            txtDivisionName.setValue(nodeBinding.getAttribute("DIV_NAME"));
            GlobalCC.refreshUI(txtDivisionCode);
            GlobalCC.refreshUI(txtDivisionName);
        }
        GlobalCC.dismissPopUp("pt1", "branchDivisionPop");

        return null;
    }


    public String selectABranchDivision() {

        // Open the popup dialog to display towns
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:branchDivisionPop" + "').show(hints);");
        //}
        return null;
    }

    public void selectTypeOfClient(ValueChangeEvent valueChangeEvent) {

        String type =
            GlobalCC.checkNullValues(session.getAttribute("typeVAl"));
        System.out.println("CLIENT TYPE IS>>>>>>>>>>" + type);
        if (type == null) {

            // GlobalCC.errorValueNotEntered("Error Value Missing: Client Type");
            return;

        }

        if (sbrIndividual.isSelected() || type.contains("I")) {
            clientTitlePan.setVisible(true);
            txtTitle.setDisabled(false);
            txtTitle.setVisible(false);
            txtSurname.setLabel("Surname Name");
            String label = GlobalCC.getSysParamValue("CLIENT_DOB_LABEL");
            txtDOB.setLabel(label);

            txtOtherNames.setVisible(true);
            // middleName.setDisabled(false);
            //surname.setDisabled(false);
            //fullName.setDisabled(true);
            btnShowContactPersons.setVisible(false);
            btnShowContactPersons.setDisabled(true);
            txtWebsite.setVisible(false);
            txtAuditors.setVisible(false);
            txtParentCompany.setVisible(false);
            parentcompanyPan.setVisible(false);
            tabDirectors.setVisible(false);
            tabSignatories.setVisible(false);
            
            if(GlobalCC.getSysParamValue("CLIENT").equalsIgnoreCase("ENSURE")){
            clientMeansOfIdPan.setVisible(true);
            txtClientMeansOfIdDateIssued.setVisible(true);
            txtClientMeansOfIdIssuedBy.setVisible(true);
            txtClientMeansOfIdDateExpired.setVisible(true);
            txtClientMeansOfIdIssuingCountry.setVisible(true);           
            txtClientNationalityCode.setVisible(true);
            txtClientUtilityBill.setVisible(true);
            
            }
            
            tabAuditors.setVisible(false);
            txtDLNo.setVisible(true);
            txtDateOfEmployment.setVisible(true);
            layClientImage.setVisible(true);
            txtGender.setRendered(true);
            grpEmploymentDetails.setRendered(true);
            layClientImage.setVisible(true);


        }
        if (sbrCorporate.isSelected() || type.contains("C") ||
            type.contains("G")) {
            grpEmploymentDetails.setRendered(false);
            clientTitlePan.setVisible(false);
            txtTitle.setVisible(false);
            txtTitle.setDisabled(true);
            txtSurname.setLabel("Corporate Name");
            String label = GlobalCC.getSysParamValue("CLIENT_DOI_LABEL");
            txtDOB.setLabel(label);
            txtOtherNames.setVisible(false);

            layClientImage.setVisible(false);
            txtOtherNames.setValue(null);
            txtWebsite.setVisible(true);
            txtAuditors.setVisible(false);
            txtGender.setRendered(false);
            txtParentCompanyCode.setVisible(true);
            parentcompanyPan.setVisible(true);
            txtDLNo.setVisible(false);
            txtDateOfEmployment.setVisible(false);
            clientPassportPan.setRendered(false);
            tabAuditors.setVisible(true);
            tabDirectors.setVisible(true);
            tabSignatories.setVisible(true);
            clientMeansOfIdPan.setVisible(false);
            txtClientMeansOfIdDateIssued.setVisible(false);
            txtClientMeansOfIdIssuedBy.setVisible(false);
            txtClientMeansOfIdDateExpired.setVisible(false);
            txtClientMeansOfIdIssuingCountry.setVisible(false);
            txtClientNationalityCode.setVisible(false);
            txtClientUtilityBill.setVisible(false);


            btnShowContactPersons.setVisible(true);
            btnShowContactPersons.setDisabled(false);

        }

        GlobalCC.refreshUI(clientTab);
        GlobalCC.refreshUI(panelCreateClient);
        GlobalCC.refreshUI(mainPanel);
        panelCreateClient.setVisible(true);
        // }
    }
    private List<SelectItem> clientType = new ArrayList<SelectItem>();


    public List<SelectItem> getClientType() {
        if (clientType != null) {
            clientType.clear();
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;


        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;

        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 =
                "SELECT  clnty_clnt_type,clnty_code \n" + //,CLNTY_CODE
                "           FROM tqc_client_types";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            rst = (OracleResultSet)stmt.executeQuery();

            clientType.add(new SelectItem("-2000", "Select Client Type"));

            while (rst.next()) {
                clientType.add(new SelectItem(rst.getString(2),
                                              rst.getString(1)));

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return clientType;
    }

    public void actionSearchByPin(ValueChangeEvent e) {

        //tbPinRefs.getSelectedRowKeys().clear();
        String v = GlobalCC.checkNullValues(e.getNewValue());
        session.setAttribute("pinNumber", v);
        ADFUtils.findIterator("fetchAllClientsByPinIterator").executeQuery();
        tbPinRefs.setVisible(false);
        if (v != null) {
            if (v.length() >= 10) {
                int count = (Integer)session.getAttribute("count");
                System.out.println("count: " + count);
                if (count == 1) {
                    SelectionEvent p = null;
                    actionSelectClient(p);
                }
                if (count > 1) {
                    tbPinRefs.setVisible(true);
                }
            }
        }
        GlobalCC.refreshUI(tbPinRefs);
        // Add event code here...
    }


    public void actionSelectClient(SelectionEvent selectionEvent) {
        System.out.println("Select client!");
        tbPinRefs.setVisible(false);
        JUCtrlValueBinding n =
            (JUCtrlValueBinding)tbPinRefs.getSelectedRowData();
        if (n != null) {
            session.setAttribute("ClientCode", n.getAttribute("code"));
        }
        actionLoadByClientCode();
    }

    public void setClientType(List<SelectItem> clientType) {
        this.clientType = clientType;
    }

    public void selectClientTypes(ValueChangeEvent valueChangeEvent) {


        if (valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            session.setAttribute("clntyCode", valueChangeEvent.getNewValue());
            ClientTypeChanged();
        }
    }

    public void selectSourceOfIncome(ValueChangeEvent valueChangeEvent) {


        if (valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {

            if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Salary")) {
                txtClientSourceOfIncomeDet.setLabel("Name of Employer");
                GlobalCC.refreshUI(txtClientSourceOfIncomeDet);

            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Own Business")) {
                txtClientSourceOfIncomeDet.setLabel("Type/Name of Business");
                GlobalCC.refreshUI(txtClientSourceOfIncomeDet);


            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Investment")) {
                txtClientSourceOfIncomeDet.setLabel("Type of Investment");
                GlobalCC.refreshUI(txtClientSourceOfIncomeDet);


            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Inheritance")) {
                txtClientSourceOfIncomeDet.setLabel("Type of Inheritance");
                GlobalCC.refreshUI(txtClientSourceOfIncomeDet);


            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Others")) {
                txtClientSourceOfIncomeDet.setLabel("Specify");
                GlobalCC.refreshUI(txtClientSourceOfIncomeDet);


            } else {
                txtClientSourceOfIncomeDet.setLabel("Name of Employer");
                GlobalCC.refreshUI(txtClientSourceOfIncomeDet);
                System.out.println("huhuhuhyyyyyyyyyyy" +
                                   valueChangeEvent.getNewValue().toString());
            }

        }
    }


    public void selectSourceOfIncomeMult(ValueChangeEvent valueChangeEvent) {


        if (valueChangeEvent.getNewValue() != null &&
            valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {

            if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Salary")) {
                txtIncomeSourceType.setLabel("Name of Employer");
                GlobalCC.refreshUI(txtIncomeSourceType);

            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Own Business")) {
                txtIncomeSourceType.setLabel("Type/Name of Business");
                GlobalCC.refreshUI(txtIncomeSourceType);


            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Investment")) {
                txtIncomeSourceType.setLabel("Type of Investment");
                GlobalCC.refreshUI(txtIncomeSourceType);


            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Inheritance")) {
                txtIncomeSourceType.setLabel("Type of Inheritance");
                GlobalCC.refreshUI(txtIncomeSourceType);


            } else if (valueChangeEvent.getNewValue().toString().equalsIgnoreCase("Others")) {
                txtIncomeSourceType.setLabel("Specify");
                GlobalCC.refreshUI(txtIncomeSourceType);


            } else {
                txtIncomeSourceType.setLabel("Name of Employer");
                GlobalCC.refreshUI(txtIncomeSourceType);

            }

        }
    }

    /**
     * @return
     */
    public String actionCreateClient() {
        String entity;
        try {
            session.setAttribute("saveSaccoStatus", null);
            session.removeAttribute("bactAccCode");
            session.removeAttribute("otherNames");
            session.removeAttribute("surNames");
            session.removeAttribute("count");
            session.removeAttribute("saveStatus");
            session.removeAttribute("pinStatus");
            session.setAttribute("client_status", "D");
            System.out.println("COUNTRY_NAME=" +
                               session.getAttribute("COUNTRY_NAME"));
            System.out.println("COUNTRY_CODE=" +
                               session.getAttribute("COUNTRY_CODE"));
            System.out.println("CouZipCode=" +
                               session.getAttribute("CouZipCode"));

            session.setAttribute("date", null);
            // Change the save button text to update
            session.setAttribute("typeVAl", null);
            session.setAttribute("clntyCode", "-2000");
            txtClientTypes.setValue("-2000");


            session.setAttribute("client_action", "A");
            //ADFUtils.findIterator("fetchAllClientsByNamesIterator").executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String actionEditClient() {
        String entity;
        try {
            System.out.println("COUNTRY_NAME=" +
                               session.getAttribute("COUNTRY_NAME"));
            System.out.println("COUNTRY_CODE=" +
                               session.getAttribute("COUNTRY_CODE"));
            System.out.println("CouZipCode=" +
                               session.getAttribute("CouZipCode"));

            session.setAttribute("date", null);
            // Change the save button text to update
            //session.setAttribute("typeVAl", null);
            //txtClientTypes.setValue("-2000");
            session.setAttribute("client_action", "E");
            //ADFUtils.findIterator("fetchAllClientsByNamesIterator").executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     */
    public String actionNewClient() {
        String entity;
        try {
            Util util = new Util();
            entity = util.getEntityApplicable();
            if (entity.equals("Y")) {
                session.setAttribute("source", "FromClient");
                //NavigationUtils navigation = new NavigationUtils();
                // navigation.navigate("viewPolicy.jspx");

                FacesContext.getCurrentInstance().getExternalContext().redirect("entities.jspx");

                return null;
            }
            session.removeAttribute("saveSaccoStatus");
            session.removeAttribute("taskID");
            session.removeAttribute("clientIprsValidated");

            Authorization auth = new Authorization();
            String AccessGranted =
                auth.checkUserRights("CLCR", "CLAU", "CLAC", null, null);
            if (("Y".equalsIgnoreCase(AccessGranted)) != true) {
                GlobalCC.accessDenied();
                return null;
            }

            // chcek credit limit
            String process = "CLCR";
            String processArea = "CLAU";
            String processSubArea = "CLCRDET";
            AccessGranted =
                    auth.checkUserRights(process, processArea, processSubArea,
                                         null, null);
            if (AccessGranted.equalsIgnoreCase("Y")) {
                txtCreditAllowed.setDisabled(false);
                txtCreditLimit.setDisabled(false);
            } else {
                txtCreditAllowed.setDisabled(true);
                txtCreditLimit.setDisabled(true);
            }
            // end check credit limit

            session.setAttribute("ClientCode", null);

            session.removeAttribute("bactAccCode");
            session.removeAttribute("otherNames");
            session.removeAttribute("surNames");
            session.removeAttribute("count");
            session.removeAttribute("saveStatus");
            session.removeAttribute("pinStatus");

            session.setAttribute("client_status", "D");

            ADFUtils.findIterator("fetchAllClientsByNamesIterator").executeQuery();
            GlobalCC.refreshUI(tbClientListingBasedOnNames);
            ADFUtils.findIterator("fetchClientEmployersIterator").executeQuery();
            GlobalCC.refreshUI(tblEmployerDetails);


            clearClientFields();
            switchClientTabs(mode.NEW_MODE);
            tabClientSystems.setRendered(true);
            tabClientWebAccounts.setRendered(true);
            tabClientDocs.setRendered(true);
            tabDetailCreateClient.setDisclosed(true);
            tabDetailSearchClients.setDisclosed(false);
            tabDetailCreateClient.setVisible(true);
            tabClientAccounts.setVisible(true);
            tabDetailCreateClient.setVisible(true);
            txtReasonForUpdate.setVisible(false);
            GlobalCC.refreshUI(tabDetailCreateClient);
            GlobalCC.refreshUI(txtReasonForUpdate);
            GlobalCC.refreshUI(tabDetailCreateClient);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    /**
     * Clears all the fields for new Client creation
     */
    public void clearClientFields() {

        try {
            txtClientCode.setValue(null);
            txtId.setValue(null);
            txtSurname.setValue(null);
            txtOtherNames.setValue(null);

            if (txtFullNames != null) {
                if (txtFullNames.isVisible() || txtFullNames.isRendered()) {
                    txtFullNames.setValue(null);
                }
            }

            txtPhysicalAddress.setValue(null);
            //pinxx
            session.setAttribute("pinNumber", null);
            ADFUtils.findIterator("fetchAllClientsByPinIterator").executeQuery();
            tbPinRefs.setVisible(false);

            if (legacyShtDesc != null) {
                legacyShtDesc.setValue(null);
            }
            System.out.println("COUNTRY_NAME=" +
                               session.getAttribute("COUNTRY_NAME"));
            System.out.println("COUNTRY_CODE=" +
                               session.getAttribute("COUNTRY_CODE"));
            System.out.println("CouZipCode=" +
                               session.getAttribute("CouZipCode"));

            txtCountryCode.setValue(session.getAttribute("COUNTRY_CODE"));
            txtCountryName.setValue(session.getAttribute("COUNTRY_NAME"));

            txtCountryName2.setValue(session.getAttribute("COUNTRY_NAME"));
            txtCountryCode2.setValue(session.getAttribute("CouZipCode"));

            txtDomicileCountriesCode.setValue(session.getAttribute("COUNTRY_CODE"));
            txtDomicileCountriesName.setValue(session.getAttribute("COUNTRY_NAME"));

            if ("AIICO".equalsIgnoreCase(GlobalCC.getSysParamValue("CLIENT"))) {
                txtPostalAddress.setValue(null);
            } else {
                txtPostalAddress.setValue("P.O. BOX ");
            }

            txtBussinessPerson.setValue(null);
            txtTownCode.setValue(null);
            txtAdminRegionName.setValue(null);
            txtClientTitleCode.setValue(null);
            txtClientTitle.setValue(null);
            txtDateCreated.setValue(null);
            txtCreatedBy.setValue(null);
            txtDirectClient.setValue(null);
            txtAccountNum.setValue(null);
            txtStaffNo.setValue(null);
            // txtTitle.setValue(null);
            String label =
                GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_DOB.label"));

            txtDOB.setLabel(label);
            txtDOB.setValue(null);
            txtOldAccNO.setValue(null);
            // txtBankAccNum.setValue(null);
            txtPassportNo.setValue(null);
            txtPIN.setValue(null);
            txtIdRegNum.setValue(null);
            txtEmail.setValue(null);
            txtPhone1.setValue(null);
            txtPrefixManager.setValue(null);
            txtSms.setValue(null);
            txtTelPayPrefix.setValue(null);
            txtPayTel.setValue(null);
            txtFax.setValue(null);
            txtProposer.setValue(null);
            txtWef.setValue(new java.util.Date());
            txtWet.setValue(null);
            txtRemarks.setValue(null);
            txtWithdrawalReason.setValue(null);

            selectStatusActive.setDisabled(false);
            selectStatusDraft.setDisabled(false);
            selectStatusInactive.setDisabled(false);
            selectStatusReady.setDisabled(false);
            txtStatus.setValue("D");

            if ("FIDELITY".equalsIgnoreCase(GlobalCC.getSysParamValue("CLIENT")))
                txtDirectClient.setValue("B");
            else
                txtDirectClient.setValue("C");

            txtRunOff.setValue(null);
            txtSpecialTerms.setValue(null);
            txtCancelledPolicy.setValue(null);
            txtIncreasePremium.setValue(null);
            txtDeclinedProposal.setValue(null);
            txtAdminRegionCode.setValue(null);
            txtAdminRegionName.setValue(null);

            // txtBranchCode.setValue(null);
            // txtBranchName.setValue(null);

            contactName1.setValue(null);
            contactPhone1.setValue(null);
            contactEmail1.setValue(null);
            contactName2.setValue(null);
            contactPhone2.setValue(null);
            contactEmail2.setValue(null);
            txtGender.setValue(null);

            if (renderer.isCheckedByComplianceOfficerVisible()) {
                txtCheckedByComplianceOfficer.setValue(null);
            }

            txtAgencyName.setValue(null);
            txtAgencyCode.setValue(null);
            pnLabelAgency.setVisible(false);

            txtClientSourceOfIncome.setValue(null);
            txtClientSourceOfIncomeDet.setValue(null);
            txtClientPlaceOfBirth.setValue(null);
            txtClntPOBCountry.setValue(null);
            txtClntPOBState.setValue(null);
            txtClntPOBTown.setValue(null);
            txtClientMeansOfId.setValue(null);
            txtClientMeansOfIdVal.setValue(null);
            txtClientMeansOfIdDateIssued.setValue(null);
            txtClientMeansOfIdDateExpired.setValue(null);
            txtClientMeansOfIdIssuedBy.setValue(null);
            txtClientMeansOfIdIssuingCountry.setValue(null);
            txtClientUtilityBill.setValue(null);
            txtClientNationalityCode.setValue(null);


            sbrIndividual.setSelected(true);
            //txtAccountOfficerCode.setValue(null);
            // txtAccountOfficerName.setValue(null);

            String defaultUserBranch =
                (String)GlobalCC.getSysParamValue("DEFAULT_USER_BRANCH");
            if (defaultUserBranch == null ||
                "N".equalsIgnoreCase(defaultUserBranch)) {
                txtRegBranchCode.setValue(null);
                txtRegBranchName.setValue(null);
            } else if ("Y".equalsIgnoreCase(defaultUserBranch)) {
                txtRegBranchCode.setValue(session.getAttribute("DEFAULT_BRANCH_CODE"));
                txtRegBranchName.setValue(session.getAttribute("DEFAULT_BRANCH_NAME"));
            } else {
                txtRegBranchCode.setValue(null);
                txtRegBranchName.setValue(null);
            }

            session.setAttribute("date", null);
            // Change the save button text to update
            btnCreateUpdateClient.setText("Save");

            session.setAttribute("typeVAl", null);
            session.setAttribute("clntyCode", "-2000");
            txtClientTypes.setValue("-2000");

            session.setAttribute("client_action", "A");
            btnCreateUpdateClient.setDisabled(false);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }


    public boolean check_if_uppecaseRequired() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;

        OracleCallableStatement statement = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        String status = null;
        Boolean value = true;
        try {


            conn = dbConnector.getDatabaseConnection();

            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.checkif_UpperCase_required(); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                status = rs.getString(1);

            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        if (status == null) {
            value = true;
            return value;
        } else {

            if (status.equalsIgnoreCase("Y")) {
                value = true;
                return value;

            } else if (status.equalsIgnoreCase("N")) {
                value = false;
                return value;

            }
        }
        return value;
    }

    public boolean checkIfSaccoRequired() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;

        OracleCallableStatement statement = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        String status = null;
        Boolean value = true;
        try {


            conn = dbConnector.getDatabaseConnection();

            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.checkifSaccoRequired(); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                status = rs.getString(1);

            }
            rs.close();
            conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        if (status == null) {
            value = true;
            return value;
        } else if (status.equalsIgnoreCase("Y")) {
            value = true;
            return value;

        } else if (status.equalsIgnoreCase("N")) {
            value = false;
            return value;

        }
        return value;
    }

    public String check_ifclientActive() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        String status = null;
        String value = null;
        try {


            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_SETUPS_CURSOR.checkif_user_Active(?); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            stmt.setBigDecimal(2,
                               new BigDecimal(session.getAttribute("ClientCode").toString()));
            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            while (rs.next()) {
                status = rs.getString(1);

            }
            rs.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;

        }


        return status;
    }

    public String checkIfSSuchRecorDExist() {
        try {


            session.setAttribute("PayeeType", "C");
            BigDecimal amount = null;
            getAutoAuthirize();


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
                String process = "CLCR";
                String processArea = "CLAU";
                String processSubArea = "CLCRDET";
                String AccessGranted =
                    auth.checkUserRights(process, processArea, processSubArea,
                                         amount, null);
                if (AccessGranted.equalsIgnoreCase("Y")) {


                } else if (AccessGranted.equalsIgnoreCase("N") &&
                           txtCreditAllowed.getValue().equals("Y") &&
                    // check if both txtCreditLimit and txtCreditAllowed are disabled and pass
                    !(txtCreditLimit.isDisabled() &&
                      txtCreditAllowed.isDisabled())) {
                    GlobalCC.INFORMATIONREPORTING("You do not have the right to allow credit limits");
                    return null;
                } else {

                }
            }


            if (btnCreateUpdateClient.getText().equals("Update")) {
                // Proceed to update
                processCreateUpdateClient();
            } else {

                // Get the Names  simirar to the  one submitted
                tbClientListingBasedOnNames.setRendered(true);
                ADFUtils.findIterator("fetchAllClientsByNamesIterator").executeQuery();


                //   GlobalCC.refreshUI(tbClientListingBasedOnNames);
                if (txtSacco.getValue() != null) {
                    if (checkIfSaccoRequired() == true &&
                        txtSacco.getValue().equals("N") &&
                        txtHoldingCompany.getValue() == null) {
                        GlobalCC.showPopup("pt1:saccoReq");
                        return null;
                    } else {
                        processCreateUpdateClient();
                    }
                } else {
                    int counter = 0;
                    //count is set on the MainClientDAO
                    System.out.println("This is counting the number of records" +
                                       session.getAttribute("count"));
                    if (session.getAttribute("count") != null) {
                        System.out.println("This is counting the number of records" +
                                           session.getAttribute("count"));
                        String noOfRecords =
                            session.getAttribute("count").toString();
                        System.out.println("This is noOfRecords the number of records" +
                                           noOfRecords);
                        counter = Integer.parseInt(noOfRecords);

                        if (counter >= 1) {
                            GlobalCC.showPopup("pt1:p2");
                            return null;
                        }
                    }
                    processCreateUpdateClient();
                    return null;
                }
                if (checkIfSaccoRequired() == true &&
                    txtSacco.getValue() == null &&
                    txtProposer.getValue() == null &&
                    txtHoldingCompany.getValue() == null) {
                    GlobalCC.showPopup("pt1:saccoReq");
                    return null;
                }
                //---  the session are required to fetch data
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public void actionAlertDateListener(ValueChangeEvent valueChangeEvent) {
        RichInputDate comp = (RichInputDate)valueChangeEvent.getComponent();
        session.setAttribute("date", comp.getValue());
        ClientTypeChanged();
    }

    public String processCreateUpdateClient() {

        BigDecimal clntCode;
        BigDecimal systemCode;
        String DriverExperience;
        String sacco;
        String reasonforUpdates;
        String creditLimitAllowed = null;
        BigDecimal creditLimit = null;
        BigDecimal locationCode = null;
        String clientLevel = null;


        Authorization auth = new Authorization();
        String type =
            GlobalCC.checkNullValues(session.getAttribute("typeVAl"));
        String typeLabel =
            GlobalCC.checkNullValues(session.getAttribute("clientTypeLabel"));

        String checkedByComplianceOfficer =
            (String)session.getAttribute("checkedByComplianceOfficer");
        System.out.println("checkedByComplianceOfficer: " +
                           checkedByComplianceOfficer);

        if (txtCreditAllowed.getValue() != null) {
            if (txtCreditAllowed.getValue().equals("Y")) {
                creditLimitAllowed = "Y";
                if (txtCreditLimit.getValue() != null) {
                    creditLimit =
                            new BigDecimal(txtCreditLimit.getValue().toString());
                } else {
                    creditLimit = null;
                }
            } else {
                creditLimitAllowed = "N";
            }
        }
        reasonforUpdates =
                GlobalCC.checkNullValues(txtReasonForUpdate.getValue());
        DriverExperience =
                GlobalCC.checkNullValues(txtDrvExperience.getValue());
        systemCode =
                GlobalCC.checkBDNullValues(session.getAttribute("systemCode"));
        clntCode =
                GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
        locationCode =
                GlobalCC.checkBDNullValues(session.getAttribute("locCode"));
        clientLevel = GlobalCC.checkNullValues(txtClientLevel.getValue());

        System.out.println("clientLevel: " + clientLevel);
        if (txtSacco.getValue() != null) {
            sacco = txtSacco.getValue().toString();
        } else {
            sacco = "N";
        }


        String option = null;
        String code = null;
        // Check if the user wishes to SAVE or UPDATE
        if (btnCreateUpdateClient.getText().toString().equalsIgnoreCase("Update")) {
            option = "E";

            String AccessGranted =
                auth.checkUserRights("CLCR", "CLAU", "CLAE", null, null);
            if (("Y".equalsIgnoreCase(AccessGranted)) != true) {
                GlobalCC.accessDenied();
                return null;
            }

            String accountNumber =
                GlobalCC.checkNullValues(txtAccountNum.getValue());
            String id = GlobalCC.checkNullValues(txtId.getValue());
            if (id == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Client Id");
                return null;

            }
            code = GlobalCC.checkNullValues(txtClientCode.getValue());
            if (code == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing:Client code Required");
                return null;
            }
            session.setAttribute("ClientCode",
                                 GlobalCC.checkBDNullValues(txtClientCode.getValue()));

            // Proceed to update
            // String status = processUpdateClient();;

        } else {
            option = "A";
            String AccessGranted =
                auth.checkUserRights("CLCR", "CLAU", "CLAC", null, null);
            if (("Y".equalsIgnoreCase(AccessGranted)) != true) {
                GlobalCC.accessDenied();
                return null;
            }
        }


        if (option != null) {
            String upCaseOthNames, upCaseSurNames;
            upCaseOthNames = null;
            upCaseSurNames = null;
            UploadedFile _file = (UploadedFile)uploadedPicture.getValue();
            System.out.println("_file1 " + _file);

            String directClient =
                GlobalCC.checkNullValues(txtDirectClient.getValue());


            String fullNames =
                (String)GlobalCC.checkNullValues(txtFullNames.getValue());
            String surname = GlobalCC.checkNullValues(txtSurname.getValue());
            String otherNames =
                GlobalCC.checkNullValues(txtOtherNames.getValue());
            String gender = GlobalCC.checkNullValues(txtGender.getValue());

            String sms = GlobalCC.checkNullValues(txtSms.getValue());
            String sms2 = GlobalCC.checkNullValues(txtPayTel.getValue());


            String site =
                GlobalCC.checkNullValues(session.getAttribute("DEFAULT_SITE"));
            String idno =
                (String)GlobalCC.checkNullValues(txtIdRegNum.getValue());
            String passport =
                (String)GlobalCC.checkNullValues(txtPassportNo.getValue());
            String pin = GlobalCC.checkNullValues(txtPIN.getValue());
            String country =
                GlobalCC.checkNullValues(txtCountryName.getValue());
            String wef = GlobalCC.checkNullValues(txtWef.getValue());
            String status = GlobalCC.checkNullValues(txtStatus.getValue());

            System.out.println("status: " + status);

            String stateCode =
                GlobalCC.checkNullValues(txtAdminRegionCode.getValue());
            String email = GlobalCC.checkNullValues(txtEmail.getValue());
            String email2 = GlobalCC.checkNullValues(txtEmail2.getValue());
            String phone1 = GlobalCC.checkNullValues(txtPhone1.getValue());
            String physicalAddress =
                GlobalCC.checkNullValues(txtPhysicalAddress.getValue());
            String postalAddress =
                GlobalCC.checkNullValues(txtPostalAddress.getValue());

            BigDecimal divisionCode =
                GlobalCC.checkBDNullValues(txtDivisionCode.getValue());


            String id = GlobalCC.checkNullValues(txtId.getValue());
            String title =
                GlobalCC.checkNullValues(txtClientTitleCode.getValue());

            String countryCode =
                GlobalCC.checkNullValues(txtCountryCode.getValue());
            String townCode = GlobalCC.checkNullValues(txtTownCode.getValue());

            String idRegNum = GlobalCC.checkNullValues(txtIdRegNum.getValue());

            String prefix =
                GlobalCC.checkNullValues(txtPrefixManager.getValue());

            String prefix2 =
                GlobalCC.checkNullValues(txtTelPayPrefix.getValue());
            String fax = GlobalCC.checkNullValues(txtFax.getValue());
            String proposer = GlobalCC.checkNullValues(txtProposer.getValue());


            String remarks = GlobalCC.checkNullValues(txtRemarks.getValue());
            String withdrawalReason =
                GlobalCC.checkNullValues(txtWithdrawalReason.getValue());
            String runOff = GlobalCC.checkNullValues(txtRunOff.getValue());
            String specialTerms =
                GlobalCC.checkNullValues(txtSpecialTerms.getValue());
            String cancelledPolicy =
                GlobalCC.checkNullValues(txtCancelledPolicy.getValue());
            String increasePremium =
                GlobalCC.checkNullValues(txtIncreasePremium.getValue());
            String declinedProposal =
                GlobalCC.checkNullValues(txtDeclinedProposal.getValue());

            String accountManagerCode =
                GlobalCC.checkNullValues(txtAccountManagerCode.getValue());
            String sectorCode =
                GlobalCC.checkNullValues(txtSectorCode.getValue());
            String sectorName =
                GlobalCC.checkNullValues(txtSectorName.getValue());
            String domicileCountriesCode =
                GlobalCC.checkNullValues(txtDomicileCountriesCode.getValue());

            // Prefered Benefit Mode of Payment
            BigDecimal preferedBenefitPaymentModeCode =
                GlobalCC.checkBDNullValues(txtPreferedBenefitPaymentModeCode.getValue());
            String preferedBenefitPaymentModeName =
                GlobalCC.checkNullValues(txtPreferedBenefitPaymentMode.getValue());
            // END. Prefered Benefit Mode of Payment

            // Prefered Premium Mode of Payment
            BigDecimal preferedPremiumPaymentModeCode =
                GlobalCC.checkBDNullValues(txtPreferedPremiumPaymentCode.getValue());
            String preferedPremiumPaymentModeName =
                GlobalCC.checkNullValues(txtPreferedPremiumPayment.getValue());
            // END. Prefered Premium Mode of Payment

            // Education Level & Monthly Income
            // CRM-1465
            String clntEducationLevel =
                GlobalCC.checkNullValues(txtEduLevel.getValue());
            BigDecimal clntMonthlyIncome =
                GlobalCC.checkBDNullValues(txtMonthlyIncome.getValue());
            // END. Education Level & Monthly Income

            String contactName1 =
                GlobalCC.checkNullValues(txtContactName1.getValue());
            String contactPhone1 =
                GlobalCC.checkNullValues(txtContactPhone1.getValue());
            String contactEmail1 =
                GlobalCC.checkNullValues(txtContactEmail1.getValue());
            String contactName2 =
                GlobalCC.checkNullValues(txtContactName2.getValue());
            String contactPhone2 =
                GlobalCC.checkNullValues(txtContactPhone2.getValue());
            String contactEmail2 =
                GlobalCC.checkNullValues(txtContactEmail2.getValue());
            String auditors = GlobalCC.checkNullValues(txtAuditors.getValue());
            String website = GlobalCC.checkNullValues(txtWebsite.getValue());
            String parentcompany =
                GlobalCC.checkNullValues(txtParentCompany.getValue());
            String current_insurer =
                GlobalCC.checkNullValues(txtInsurer.getValue());
            String projectedBiz =
                GlobalCC.checkNullValues(txtProjectedBiz.getValue());

            String drivingLicence =
                GlobalCC.checkNullValues(txtDLNo.getValue());
            String regBranchCode =
                GlobalCC.checkNullValues(txtRegBranchCode.getValue());
            BigDecimal accMgr =
                GlobalCC.checkBDNullValues(txtAccountOfficerCode.getValue());


            String cltCellNos =
                GlobalCC.checkNullValues(txtClientCellNos.getValue());
            String cltBankTelNo =
                GlobalCC.checkNullValues(txtCltBankTelNo.getValue());
            String cltBankCellNo =
                GlobalCC.checkNullValues(txtCltBankCellNo.getValue());
            String cltEmployerTelNo =
                GlobalCC.checkNullValues(txtCltEmployerTelNo.getValue());
            String cltEmployerCellNo =
                GlobalCC.checkNullValues(txtCltEmployerCellNo.getValue());
            String staffNo = GlobalCC.checkNullValues(txtStaffNo.getValue());
            String sourceOfIncome =
                GlobalCC.checkNullValues(txtClientSourceOfIncome.getValue());
            String sourceOfIncomeDet =
                GlobalCC.checkNullValues(txtClientSourceOfIncomeDet.getValue());
            String placeOfBirth =
                GlobalCC.checkNullValues(txtClientPlaceOfBirth.getValue());

            String placeOfBirthCountry =
                GlobalCC.checkNullValues(txtClntPOBCountry.getValue());

            String placeOfBirthState =
                GlobalCC.checkNullValues(txtClntPOBState.getValue());

            String placeOfBirthTown =
                GlobalCC.checkNullValues(txtClntPOBTown.getValue());

            String meansOfId =
                GlobalCC.checkNullValues(txtClientMeansOfId.getValue());
            String meansOfIdVal =
                GlobalCC.checkNullValues(txtClientMeansOfIdVal.getValue());
            String utilityBill =
                GlobalCC.checkNullValues(txtClientUtilityBill.getValue());
            java.sql.Date meansOfIdDateIssued =
                GlobalCC.extractDate(txtClientMeansOfIdDateIssued);
            java.sql.Date meansOfIdDateExpired =
                GlobalCC.extractDate(txtClientMeansOfIdDateExpired);
            String meansOfIdIssuedBy =
                GlobalCC.checkNullValues(txtClientMeansOfIdIssuedBy.getValue());
            String meansOfIdIssuingCountry =
                GlobalCC.checkNullValues(txtClientMeansOfIdIssuingCountry.getValue());


            BigDecimal nationalityCode =
                GlobalCC.checkBDNullValues(txtClientNationalityCode.getValue());

            BigDecimal typeCode =
                GlobalCC.checkBDNullValues(txtClientTypes.getValue());

            sms = GlobalCC.checkNullValues(txtSms.getValue());
            System.out.println("SMS1=" + sms);

            //sms2
            sms2 = GlobalCC.checkNullValues(txtPayTel.getValue());
            System.out.println("SMS2=" + sms2);

            String intNo = GlobalCC.checkNullValues(txtIntTel.getValue());

            String countryInt = null;

            if (type == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select Client Type!");
                return null;
            }


            if ("AIICO".equalsIgnoreCase((String)session.getAttribute("CLIENT"))) {
                if (GlobalCC.checkNullValues(txtIntTel.getValue()) != null) {
                    intNo = GlobalCC.checkNullValues(txtIntTel.getValue());
                    countryInt =
                            GlobalCC.checkNullValues(txtCountryCode3.getValue());
                    if (intNo != null && countryInt == null) {
                        GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Country First When entering  International Telephone No! ");
                        return null;
                    }


                    if (countryInt != null) {
                        if (countryInt.startsWith("+")) {
                            countryInt = countryInt.replaceFirst("+", "");
                        }
                        intNo = countryInt + "" + intNo;
                    }
                }

            }
            String vPerson =
                GlobalCC.checkNullValues(session.getAttribute("typePerson"));
            boolean human = "Y".equals(vPerson);

            if ("Y".equalsIgnoreCase(GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_MOI.required"))) &&
                "N".equals(vPerson)) {
                session.setAttribute("FX_CLIENT_MOI.required", "N");

                txtCltOccupation.setRendered(false);
                panMsgOccupation.setRendered(false);
                txtCltOccupation.setRequired(false);

                txtClientMeansOfId.setRendered(false);
                txtClientMeansOfId.setRequired(false);
                txtClientMeansOfIdVal.setRequired(false);

                txtClientMeansOfId.setVisible(false);
                txtClientMeansOfIdVal.setVisible(false);
            }

            Util checkClientAgency = new Util();
            String clientAgencyTyingCheck =
                checkClientAgency.getClientAgencyTying();
            session.setAttribute("checkedByComplianceOfficer",
                                 checkedByComplianceOfficer != null ?
                                 checkedByComplianceOfficer : "N");


            boolean mand = renderer.isCLIENT_RELATIONSHIP_OFFICER_REQUIRED();

            boolean exp = renderer.isDriverExpNumber();


            session.setAttribute("ClientFullname", fullNames);
            session.setAttribute("ClientFullname", fullNames);
            session.setAttribute("insuredDesc", fullNames);
            session.setAttribute("clientName", fullNames);


            if ((!renderer.isClientForDisabled()) &&
                renderer.isClientForVisible()) {
                if (!(renderer.getClientForLMS().isSelected() ||
                      renderer.getClientForGIS().isSelected())) {
                    GlobalCC.errorValueNotEntered("Client for LMS or GIS field is mandatory");
                    return null;
                }
            }


            String Telo = GlobalCC.checkNullValues(txtIntTel.getValue());
            if ((Telo == null) && ("F".equals(type))) {
                GlobalCC.errorValueNotEntered("Error Missing Value : Enter International Tel No for foreign Client");
                return null;
            }


            System.out.println("STEP 111");
            if (!renderer.isCLIENT_FULLNAME_DISABLED() &&
                renderer.isCLIENT_FULLNAME_VISIBLE() &&
                renderer.isCLIENT_FULLNAME_REQUIRED()) {
                if (fullNames == null || "".equals(fullNames)) {
                    GlobalCC.INFORMATIONREPORTING("Please Enter Client Full Names!");
                    return null;
                }


                if (!fullNames.matches("[A-Za-z]+(\\s[A-Za-z]+){1,2}")) {
                    GlobalCC.INFORMATIONREPORTING("'" + fullNames +
                                                  "' is not a valid Full Name, Should be atleast 2,3 names starting with Surname!");
                    return null;
                }


                int s = fullNames.trim().indexOf(" ");
                if (s > 0) {
                    txtSurname.setValue((String)fullNames.substring(0, s));
                    txtOtherNames.setValue((String)fullNames.substring(s + 1,
                                                                       fullNames.length()));
                    GlobalCC.refreshUI(txtSurname);
                    GlobalCC.refreshUI(txtOtherNames);
                }
            }

            System.out.println("STEP 222");
            if (pin == null && renderer.isCLIENT_PIN_REQUIRED()) {
                if (!"F".equalsIgnoreCase(type)) {
                    if (!human) {
                        String val = GlobalCC.getSysParamValue("CLIENT");
                        if (val.equalsIgnoreCase("ENSURE")) {
                            GlobalCC.EXCEPTIONREPORTING("Ente TIN No.. ");
                        } else {
                            GlobalCC.EXCEPTIONREPORTING("Enter Pin No.. ");
                        }
                        return null;
                    }
                    java.util.Date dob = (java.util.Date)txtDOB.getValue();
                    int age = (dob != null) ? GlobalCC.getAge(dob) : 0;
                    if (human && age > 18) {
                        GlobalCC.EXCEPTIONREPORTING("Enter Pin No.. ");
                        return null;
                    }
                }

            }


            if ("I".equalsIgnoreCase(type) || "S".equalsIgnoreCase(type)) {
                if (renderer.isID_OR_PASSPORT_MAND()) {
                    if (idno == null && passport == null) {
                        GlobalCC.EXCEPTIONREPORTING("You must either Enter Passport No or ID No  Details.. ");
                        return null;
                    }
                }


                if (renderer.isPIN_OR_PASS_MAND()) {
                    if (pin == null && passport == null) {
                        GlobalCC.INFORMATIONREPORTING("You must either Enter Passport No or PIN No  Details..");
                        return null;
                    }
                }

                String pinReq =
                    GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_PIN.required"));
                String passReq =
                    GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_PASSPORT_NO.required"));

                boolean passMand = "Y".equalsIgnoreCase(passReq);

                if ("F".equals(type) &&
                    "Y".equals(GlobalCC.getSysParamValue("FOREIGN_CLIENT_PASSPORT_MAND"))) {
                    passMand = true;
                }

                txtPassportNo.setRequired(passMand);

                boolean pinMand = "Y".equalsIgnoreCase(pinReq);

                //negations
                String foreign =
                    GlobalCC.getSysParamValue("FOREIGN_CLIENT_PIN_NOT_MAND");

                String under18 =
                    GlobalCC.getSysParamValue("INDIVIDUAL_UNDER_18_CLIENT_PIN_NOT_MAND");

                if ("Y".equals(pinReq) && "F".equals(type) &&
                    "Y".equals(foreign)) {
                    pinMand = false;
                }
                if ("Y".equals(pinReq) && "I".equals(type) &&
                    "Y".equals(under18)) {
                    java.util.Date dob = (java.util.Date)txtDOB.getValue();
                    int age = (dob != null) ? GlobalCC.getAge(dob) : 0;
                    if ((age < 18) && (dob != null)) {
                        pinMand = false;
                    }
                }
                if (txtPIN.getValue() == null) {
                    if (pinMand) {
                        GlobalCC.EXCEPTIONREPORTING("Please Enter Pin! ");
                        return null;
                    }
                }


                System.out.println("STEP 333");
                if (idno != null) {
                    if (!idno.matches("^[0-9]{7,9}$") &&
                        "KENYA".equalsIgnoreCase(site)) {
                        GlobalCC.EXCEPTIONREPORTING("Invalid ID No should be a number with digits between 7 and 9! ");
                        return null;
                    }


                    if ("ZAMBIA".equalsIgnoreCase(site)) {
                        if (!Util.validatePassport(idno, "ID")) {

                            GlobalCC.EXCEPTIONREPORTING("Invalid " +
                                                        session.getAttribute("ID_NO_LABEL") +
                                                        " Format: You must Enter in the format " +
                                                        session.getAttribute("ID_NO_FORMAT"));
                            return null;
                        }
                    }
                }


                String clnt = "Y";


                /* if (GlobalCC.checkNullValues(session.getAttribute("CLIENT_TITLE_MAND")) !=
                    null) {
                    clnt = GlobalCC.checkNullValues(session.getAttribute("CLIENT_TITLE_MAND"));
                    if (clnt == "Y") {
                        if (txtClientTitleCode.getValue() == null) {
                            GlobalCC.errorValueNotEntered("Client Title  required!");
                            return null;
                        }
                    }
                } else {
                    if (txtClientTitleCode.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Client Title  required!");
                        return null;
                    }
                }*/


                if (renderer.isCLIENT_TITLE_REQUIRED()) {
                    if (txtClientTitleCode.getValue() == null) {
                        GlobalCC.errorValueNotEntered("Client Title  required!");
                        return null;
                    }
                }


            }


            System.out.println("STEP 444");
            String driverExp = null;
            if (exp == true && txtDrvExperience.getValue() != null) {
                driverExp = txtDrvExperience.getValue().toString();
                try {
                    new BigDecimal(driverExp);
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalCC.INFORMATIONREPORTING("The Driver Experience should be a number");
                    return null;
                }
            }

            String gsmZipCode =
                GlobalCC.checkNullValues(txtCountryCode2.getValue());

            if (gsmZipCode == null && (prefix != null || prefix2 != null)) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Country First When entering  Sms/Gsm Prefix! ");
                return null;
            }
            String sms_0 = GlobalCC.checkNullValues(txtSms.getValue());
            String sms_format = GlobalCC.getSysParamValue("SMS_NO_FORMAT");
            sms_format = !("[NONE]".equals(sms_format)) ? sms_format : null;

            if (sms_0 != null && sms_format != null) {
                if (!GlobalCC.validate(sms_0, sms_format)) {
                    GlobalCC.errorValueNotEntered("Error Invalid GSM suffix 1 No." +
                                                  sms_0 +
                                                  " format. Use format: " +
                                                  sms_format);
                    return null;
                }
            }

            if ("AIICO".equalsIgnoreCase((String)session.getAttribute("CLIENT"))) {

                //added
                if (GlobalCC.checkNullValues(txtIntTel.getValue()) == null &&
                    GlobalCC.checkNullValues(txtCountryCode3.getValue()) !=
                    null) {
                    GlobalCC.errorValueNotEntered("hapa::::Error Value Missing: Enter  International Telephone No! ");
                    return null;
                }


                if (sms_0 == null && human &&
                    renderer.isCLIENT_SMS_REQUIRED()) {
                    GlobalCC.errorValueNotEntered("Please enter SMS suffix 1 No!");
                    return null;
                }


            }

            String sms_1 = GlobalCC.checkNullValues(txtPayTel.getValue());

            if (gsmZipCode == null && (prefix != null || prefix2 != null)) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter the Country First When entering  Sms/Gsm Prefix! ");
                return null;
            }
            if (sms_1 != null && sms_format != null) {
                if (!GlobalCC.validate(sms_1, sms_format)) {
                    GlobalCC.errorValueNotEntered("Error Invalid GSM suffix 1 No." +
                                                  sms_1 +
                                                  " format. Use format: " +
                                                  sms_format);
                    return null;
                }
            }

            if ("AIICO".equalsIgnoreCase((String)session.getAttribute("CLIENT"))) {

                //added
                if (GlobalCC.checkNullValues(txtIntTel.getValue()) == null &&
                    GlobalCC.checkNullValues(txtCountryCode3.getValue()) !=
                    null) {
                    GlobalCC.errorValueNotEntered("hapa::::Error Value Missing: Enter  International Telephone No! ");
                    return null;
                }


                if (sms_1 == null && human &&
                    renderer.isCLIENT_SMS_2_REQUIRED()) {
                    GlobalCC.errorValueNotEntered("Please enter GSM suffix 2 No!");
                    return null;
                }
            }

            if (clientAgencyTyingCheck.equals("Y")) {
                if (txtDirectClient.getValue().equals("N") &&
                    txtAgencyName.getValue() == null && mand == true) {
                    GlobalCC.INFORMATIONREPORTING("Please select Agent");
                    return null;
                }

                if (txtDirectClient.getValue().equals("Y") &&
                    txtAccountManager.getValue() == null && mand == true) {
                    GlobalCC.INFORMATIONREPORTING("Please select A Relationship Manager");
                    return null;
                }
            }

            session.setAttribute("passportNumber", txtPassportNo.getValue());
            session.setAttribute("idRegNumber", txtIdRegNum.getValue());
            session.setAttribute("pinNumber", txtPIN.getValue());


            System.out.println("STEP 5555");


            if (txtClientLevel != null) {
                session.setAttribute("clientLevel", txtClientLevel.getValue());
            }


            System.out.println("email2: " + email2);
            session.setAttribute("EMAIL2", txtEmail2.getValue());

            if ((txtReasonForUpdate.isVisible() == true) &&
                txtReasonForUpdate.getValue() == null) {
                GlobalCC.errorValueNotEntered("Please enter the reason for updating client");
                return null;
            }

            if (email2 != null &&
                !GlobalCC.validate(email2, GlobalCC.EMAIL_PATTERN)) {
                GlobalCC.errorValueNotEntered("Invalid Alternative Email Address: Enter A valid Alternative Email address");
                return null;
            }
            if (email != null &&
                !GlobalCC.validate(email, GlobalCC.EMAIL_PATTERN)) {
                GlobalCC.errorValueNotEntered("Invalid Email Address: Enter A valid email address");
                return null;
            }


            if (check_if_uppecaseRequired() == true) {
                if (otherNames != null) {
                    upCaseOthNames = otherNames.toUpperCase();
                } else {
                    upCaseOthNames = otherNames;
                }
                if (surname != null) {
                    upCaseSurNames = surname.toUpperCase();
                }
            } else {
                upCaseSurNames = surname;
                upCaseOthNames = otherNames;

            }
            String cltOccupation =
                GlobalCC.checkNullValues(txtCltOccupation.getValue());
            System.out.println("STEP 666");
            // String status = GlobalCC.checkNullValues(rs.getString("CLNT_STATUS"));
            
            // CRM-2017 Incorporation/RC number should be mandatory for corporate clients
            final String mTypePerson =
                GlobalCC.checkNullValues(session.getAttribute("typePerson"));
            String regNoRequired = "N";
            String regNoLabelMsg = "";
            if("Y".equalsIgnoreCase(mTypePerson)){
                regNoRequired = GlobalCC.getSysParamValue("INDIVIDUAL_ID_NO_REQUIRED");
                regNoLabelMsg = GlobalCC.getSysParamValue("INDIVIDUAL_ID_NO_LABEL");
            } else  {
                regNoRequired = GlobalCC.getSysParamValue("CORPORATE_ID_NO_REQUIRED");
                regNoLabelMsg = GlobalCC.getSysParamValue("CORPORATE_ID_NO_LABEL");
            }
            if("Y".equalsIgnoreCase(regNoRequired) && 
                (this.txtIdRegNum.getValue() == null ||
                 ((String)this.txtIdRegNum.getValue()).trim().length() == 0)) {
                // IdRcNumberRequired
                final String idRcNumberRequiredMsg = regNoLabelMsg + " is requreid";
                GlobalCC.INFORMATIONREPORTING(idRcNumberRequiredMsg);
                return null;
            }
            // END. CRM-2017

            FormUi formUi = new FormUi();
            if (!formUi.validate("MainClientTab")) { //main validation engine
                return null;
            }
            
            // CRM-2011 Validation on means of id field
            String clientMoiRequired =
                GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_MOI.required"));
            if("Y".equalsIgnoreCase(clientMoiRequired) && 
               (this.txtClientMeansOfIdVal.getValue() == null || 
                ((String)this.txtClientMeansOfIdVal.getValue()).trim().length() == 0)){
                // means of Id val is required
                GlobalCC.INFORMATIONREPORTING("Please provide means of ID value.");
                return null;
            }
            // END. CRM-2011


            // Proceed to create a new Client
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                String Query =
                    "begin ? := tqc_clients_pkg.client_proc(" + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?" + "); end;";


                conn = connector.getDatabaseConnection();

                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.registerOutParameter(1, OracleTypes.NUMBER);
                cst.setString(2, directClient);
                cst.setString(3, id);
                cst.setString(4, null);
                cst.setString(5, upCaseOthNames);
                cst.setString(6, upCaseSurNames);
                cst.setString(7, pin);
                cst.setString(8, postalAddress);
                cst.setString(9, physicalAddress);
                cst.setString(10, idRegNum);
                cst.setString(11, (String)session.getAttribute("Username"));
                cst.setDate(12, GlobalCC.extractDate(txtWef));
                cst.setDate(13, GlobalCC.extractDate(txtWet));
                cst.setString(14, title);
                cst.setDate(15, GlobalCC.extractDate(txtDOB));
                cst.setString(16, countryCode);
                cst.setString(17, townCode);
                cst.setObject(18, txtZipCode.getValue()); // TODO : ZIP code
                cst.setString(19, email);
                cst.setString(20, phone1);
                cst.setString(21, sms);
                cst.setString(22, fax);
                cst.setString(23, sectorCode);
                cst.setString(24, sectorName);
                cst.setString(25, domicileCountriesCode);
                cst.setString(26, proposer); // Same as Holding company
                cst.setString(27,
                              status); // Use authorized buttons to change the client status field instead!
                cst.setString(28, runOff);
                cst.setString(29,
                              withdrawalReason); // Not sure. Need to confirm in sql
                cst.setString(30, remarks);
                cst.setString(31,
                              GlobalCC.checkNullValues(txtBankAccNum.getValue())); //bankacc
                cst.setString(32,
                              GlobalCC.checkNullValues(session.getAttribute("BACT_BBR_CODE"))); //branchCode
                cst.setString(33, specialTerms);
                cst.setString(34, cancelledPolicy);
                cst.setString(35, increasePremium);
                cst.setString(36, declinedProposal);
                cst.setString(37, type);
                cst.setString(38, option);
                // Client Code
                System.out.println("Client Code" + clntCode);

                cst.setBigDecimal(39, clntCode);

                cst.setString(40,
                              GlobalCC.checkNullValues(session.getAttribute("MarketeragentCode"))); /*accountManagerCode*/
                cst.setString(41, contactName1);
                cst.setString(42, contactPhone1);
                cst.setString(43, contactEmail1);
                cst.setString(44, contactName2);
                cst.setString(45, contactPhone2);
                cst.setString(46, contactEmail2);
                cst.setString(47, passport);
                cst.setBigDecimal(48,
                                  stateCode == null ? null : new BigDecimal(stateCode));
                cst.setString(49, website);
                cst.setString(50, auditors);
                cst.setBigDecimal(51,
                                  parentcompany == null ? null : new BigDecimal(parentcompany));
                cst.setString(52, current_insurer);
                cst.setBigDecimal(53,
                                  projectedBiz == null ? null : new BigDecimal(projectedBiz));
                cst.setDate(54, GlobalCC.extractDate(txtDateOfEmployment));
                cst.setString(55, drivingLicence);
                cst.setString(56, regBranchCode);

                System.out.println("STEP 77777");


                System.out.println("_file2 " + _file);
                InputStream inp = null;
                boolean isOk = false;
                //client image
                if (session.getAttribute("ClientCode") == null) {
                    if (_file != null) {
                        isOk =
GlobalCC.validateUploadedImgFile(fileStream, "Client Image", fileContent);
                        if (isOk == false) {
                            uploadedPicture.setValue(null);
                            GlobalCC.refreshUI(uploadedPicture);

                            return null;

                        } else {
                            inp = _file.getInputStream();
                        }
                    }

                    // signature


                    UploadedFile _fileSignature =
                        (UploadedFile)uploadSignature.getValue();
                    InputStream inpSign = null;
                    boolean isSignOk = false;
                    //client image
                    if (_fileSignature != null) {
                        isOk =
GlobalCC.validateUploadedImgFile(fileStream2, "signature", fileContent2);
                        if (isSignOk == false) {
                            uploadSignature.setValue(null);
                            GlobalCC.refreshUI(uploadSignature);
                            //  uploadedPicture.setValue(null);
                            // GlobalCC.refreshUI(uploadedPicture);
                            return null;

                        } else {
                            inpSign = _fileSignature.getInputStream();


                        }
                    }

                    cst.setBlob(57, fileStream);
                    cst.setBlob(58, fileStream2);
                } else {
                    fileStream = null;
                    fileStream2 = null;
                    cst.setBlob(57, fileStream);
                    cst.setBlob(58, fileStream2);
                }

                cst.setObject(59,
                              GlobalCC.checkNullValues(session.getAttribute("MarketeragentCode"))); /*accMgr*/ //
                cst.setString(60, gender);
                cst.setString(61, cltOccupation);
                cst.setString(62, cltBankTelNo); //
                cst.setString(63, cltBankCellNo); //
                cst.setString(64, cltEmployerTelNo);
                cst.setString(65, cltEmployerCellNo);
                cst.setString(66, cltCellNos);
                cst.setString(67, typeLabel);
                cst.registerOutParameter(68, OracleTypes.NUMBER);

                cst.registerOutParameter(69, OracleTypes.VARCHAR);
                String oldShtDesc =
                    GlobalCC.checkNullValues(legacyShtDesc.getValue());
                cst.setString(70, oldShtDesc);
                cst.setDate(71,
                            txtAnniversary.getValue() != null ? GlobalCC.extractDate(txtAnniversary) :
                            null);
                cst.setObject(72,
                              txtCreditRting.getValue() != null ? txtCreditRting.getValue() :
                              null);
                cst.setBigDecimal(73, systemCode);
                cst.setString(74, DriverExperience);
                cst.setString(75, sacco);
                cst.setBigDecimal(76, clntCode);
                cst.setString(77, reasonforUpdates);
                cst.setObject(78, txtCreditAllowed.getValue());
                cst.setObject(79, txtCreditLimit.getValue());
                cst.setObject(80, locationCode);
                cst.setObject(81, session.getAttribute("occupationCode"));
                cst.setObject(82, txtBouncedCheque.getValue());
                cst.setObject(83, txtMaritalStatus.getValue());
                cst.setObject(84, txtModeOfComm.getValue());
                cst.setObject(85, txtPayroll.getValue());
                cst.setObject(86, txtMinSalary.getValue());
                cst.setObject(87, txtMaxSalary.getValue());
                cst.setDate(88, GlobalCC.extractDate(txtDlIssueDate));
                cst.setObject(89, txtWorkPermit.getValue());
                cst.setString(90, null);
                cst.setString(91, clientLevel);
                cst.setString(92, sms2);
                cst.setObject(93,
                              GlobalCC.checkNullValues(session.getAttribute("EMAIL2")));
                cst.setString(94, intNo);
                cst.setString(95, checkedByComplianceOfficer);
                cst.setObject(96, txtDirectClient.getValue());
                cst.setObject(97, txtPrefixManager.getValue());
                cst.setObject(98, txtTelPayPrefix.getValue());
                cst.setObject(99, session.getAttribute("SPZ_CODE"));
                cst.setBigDecimal(100, divisionCode);
                cst.setBigDecimal(101,
                                  GlobalCC.checkBDNullValues(txtCountryCode2.getValue()));
                cst.setBigDecimal(102,
                                  GlobalCC.checkBDNullValues(txtCountryCode3.getValue()));
                cst.setString(103, staffNo);
                cst.setBigDecimal(104, typeCode);
                cst.registerOutParameter(105, OracleTypes.VARCHAR);
                cst.registerOutParameter(106, OracleTypes.VARCHAR);
                cst.setBigDecimal(107, preferedBenefitPaymentModeCode);
                cst.setString(108, preferedBenefitPaymentModeName);
                cst.setBigDecimal(109, preferedPremiumPaymentModeCode);
                cst.setString(110, preferedPremiumPaymentModeName);
                cst.setString(111, clntEducationLevel);
                cst.setBigDecimal(112, clntMonthlyIncome);
                String validated =
                    GlobalCC.checkNullValues(session.getAttribute("clientIprsValidated"));
                cst.setString(113, validated);
                cst.setString(114, sourceOfIncome);
                cst.setString(115, placeOfBirth);
                cst.setString(116, meansOfId);
                cst.setString(117, meansOfIdVal);
                cst.setString(118, utilityBill);
                cst.setDate(119, meansOfIdDateIssued);
                cst.setString(120, meansOfIdIssuedBy);
                cst.setBigDecimal(121, nationalityCode);
                cst.setDate(122, meansOfIdDateExpired);
                cst.setString(123, meansOfIdIssuingCountry);
                cst.setString(124, placeOfBirthCountry);
                cst.setString(125, placeOfBirthState);
                cst.setString(126, placeOfBirthTown);
                cst.setString(127, sourceOfIncomeDet);
                cst.execute();

                BigDecimal returnedClientCode =
                    GlobalCC.checkBDNullValues(cst.getBigDecimal(68));
                String msg = cst.getString(105);
                String result = cst.getString(106);
                String returnedShortDesc = cst.getString(69);
                // Close the connections
                cst.close();
                if ("S".equals(result) != true) {
                    conn.rollback();
                    conn.close();
                    GlobalCC.EXCEPTIONREPORTING(msg);
                    return null;
                }
                conn.commit();
                conn.close();

                System.out.println("STEP 888");

                session.setAttribute("bactAccCode", returnedClientCode);
                session.setAttribute("ClientCode", returnedClientCode);
                session.setAttribute("clientCode", returnedClientCode);
                session.setAttribute("uwclientCode", returnedClientCode);
                session.setAttribute("clientPRPCode", returnedClientCode);
                session.setAttribute("insuredCode", returnedClientCode);
                session.setAttribute("ticketEntityCode", returnedClientCode);
                session.setAttribute("clientDesc", returnedShortDesc);
                session.setAttribute("insuredShtDesc", returnedShortDesc);
                session.setAttribute("ClientExists", "Y");
                session.setAttribute("ClientStatus", "D");

                session.setAttribute("otherNames", upCaseOthNames);
                session.setAttribute("surNames", upCaseSurNames);
                session.setAttribute("idRegNumber", txtIdRegNum.getValue());


                BigDecimal taskId =
                    GlobalCC.checkBDNullValues(session.getAttribute("taskID"));
                String activityName =
                    GlobalCC.checkNullValues(session.getAttribute("activityName"));

                System.out.println("taskId: " + taskId);
                /* For Every Draft process i.e When you Update/Change any information about this Client, Create a ticket if none exist.
                 * and the Client Exists
                 * */
                if (returnedClientCode != null) {
                    if (taskId == null) { //start new client work flow
                        TaskManipulation task = new TaskManipulation();
                        String TaskId = task.clientWorkFlow();
                    }
                    transitionToDraft();
                }
                if (txtAgencyCode.getValue() != null &&
                    returnedClientCode != null) {
                    conn = connector.getDatabaseConnection();
                    Query = "begin TQC_CLIENTS_PKG.clientAgent(?,?,?); end; ";
                    cst = (OracleCallableStatement)conn.prepareCall(Query);
                    cst.setString(1, option);
                    cst.setBigDecimal(2, returnedClientCode);
                    cst.setObject(3, txtAgencyCode.getValue());

                    cst.execute();

                }
                ADFUtils.findIterator("fetchAllClientsIterator").executeQuery();
                System.out.println("STEP 999");
                // GlobalCC.refreshUI(tblClients);
                uploadSignature.setValue(null);
                GlobalCC.refreshUI(uploadSignature);
                uploadedPicture.setValue(null);
                GlobalCC.refreshUI(uploadedPicture);


                Date date = new Date();
                txtDateCreated.setValue(date);
                txtCreatedBy.setValue(session.getAttribute("Username"));
                GlobalCC.refreshUI(txtDateCreated);
                GlobalCC.refreshUI(txtCreatedBy);
                // Disable the save button
                btnCreateUpdateClient.setDisabled(false);

                if (returnedClientCode != null) {
                    // Give user feedback
                    if (option.equalsIgnoreCase("A")) {
                        GlobalCC.INFORMATIONREPORTING("You can now Submit Required Documents");


                        GlobalCC.hidePopup("pt1:p2");
                        // Get the newly created Client Id and save it in a session variable
                        session.setAttribute("ClientCode", returnedClientCode);
                        clientPhoto.setSource("/clientimagesservlet?id=" +
                                              session.getAttribute("ClientCode"));
                        clientSignature.setSource("/clientsignatureservlet?id=" +
                                                  session.getAttribute("ClientCode"));

                        txtId.setValue(returnedShortDesc);
                        txtClientCode.setValue(returnedClientCode);
                        session.setAttribute("clientShtDesc",
                                             returnedShortDesc);
                        session.setAttribute("clientName",
                                             upCaseOthNames + " " +
                                             upCaseSurNames);
                        GlobalCC.refreshUI(clientPhoto);
                        GlobalCC.refreshUI(clientSignature);

                        GlobalCC.refreshUI(txtClientCode);
                        // Display the other tabs
                        refreshTabsForNewClient();
                    } else {
                        ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
                        // GlobalCC.refreshUI(tblClients);
                        uploadSignature.setValue(null);
                        GlobalCC.refreshUI(uploadSignature);
                        uploadedPicture.setValue(null);
                        GlobalCC.refreshUI(uploadedPicture);
                        clientPhoto.setSource("/clientimagesservlet?id=" +
                                              session.getAttribute("ClientCode"));
                        clientSignature.setSource("/clientsignatureservlet?id=" +
                                                  session.getAttribute("ClientCode"));

                    }
                    fileStream = null;
                    fileContent = null;
                    fileStream2 = null;
                    fileContent2 = null;
                    actionLoadByClientCode();
                }
                //commented out by kavagi
                //clearFields();

                //txtClientTypes.setValue(null);
                //GlobalCC.refreshUI(txtClientTypes);
                GlobalCC.INFORMATIONREPORTING(msg);

            } catch (SQLException e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                e.printStackTrace();
            } catch (IOException e) {
                GlobalCC.EXCEPTIONREPORTING(null, e);
                e.printStackTrace();
            }

        }
        return null;
    }

    public void clearFields() {

        txtClntPOBCountry.setValue(null);
        txtClntPOBState.setValue(null);
        txtClntPOBTown.setValue(null);

        GlobalCC.refreshUI(txtClntPOBCountry);
        GlobalCC.refreshUI(txtClntPOBState);
        GlobalCC.refreshUI(txtClntPOBTown);

        txtCreditAllowed.setValue(null);
        GlobalCC.refreshUI(txtCreditAllowed);

        txtCreditAllowed.setValue(null);
        GlobalCC.refreshUI(txtCreditAllowed);
        txtSacco.setValue(null);
        GlobalCC.refreshUI(txtSacco);
        txtHoldingCompany.setValue(null);
        GlobalCC.refreshUI(txtHoldingCompany);
        txtProposer.setValue(null);
        GlobalCC.refreshUI(txtProposer);
        txtCreditLimit.setValue(null);
        GlobalCC.refreshUI(txtCreditLimit);
        txtReasonForUpdate.setValue(null);
        GlobalCC.refreshUI(txtReasonForUpdate);
        txtDrvExperience.setValue(null);
        GlobalCC.refreshUI(txtDrvExperience);
        txtSacco.setValue(null);
        GlobalCC.refreshUI(txtSacco);
        txtAccountNum.setValue(null);
        GlobalCC.refreshUI(txtAccountNum);
        uploadedPicture.setValue(null);
        GlobalCC.refreshUI(uploadedPicture);
        txtFullNames.setValue(null);
        GlobalCC.refreshUI(txtFullNames);
        txtSurname.setValue(null);
        GlobalCC.refreshUI(txtSurname);
        txtOtherNames.setValue(null);
        GlobalCC.refreshUI(txtOtherNames);
        txtGender.setValue(null);
        GlobalCC.refreshUI(txtGender);
        txtSms.setValue(null);
        GlobalCC.refreshUI(txtSms);
        txtPayTel.setValue(null);
        GlobalCC.refreshUI(txtPayTel);
        legacyShtDesc.setValue(null);
        GlobalCC.refreshUI(legacyShtDesc);
        txtPassportNo.setValue(null);
        GlobalCC.refreshUI(txtPassportNo);
        txtPIN.setValue(null);
        GlobalCC.refreshUI(txtPIN);
        txtCountryName.setValue(null);
        GlobalCC.refreshUI(txtCountryName);
        txtCreatedBy.setValue(null);
        GlobalCC.refreshUI(txtCreatedBy);
        txtWef.setValue(null);
        GlobalCC.refreshUI(txtWef);
        txtStatus.setValue(null);
        GlobalCC.refreshUI(txtStatus);
        txtAdminRegionCode.setValue(null);
        GlobalCC.refreshUI(txtAdminRegionCode);
        txtEmail.setValue(null);
        GlobalCC.refreshUI(txtEmail);
        txtEmail2.setValue(null);
        GlobalCC.refreshUI(txtEmail2);
        txtPhone1.setValue(null);
        GlobalCC.refreshUI(txtPhone1);
        txtPhysicalAddress.setValue(null);
        GlobalCC.refreshUI(txtPhysicalAddress);
        txtPostalAddress.setValue(null);
        GlobalCC.refreshUI(txtPostalAddress);
        txtDivisionCode.setValue(null);
        GlobalCC.refreshUI(txtDivisionCode);
        txtCountryCode.setValue(null);
        GlobalCC.refreshUI(txtCountryCode);
        txtTownCode.setValue(null);
        GlobalCC.refreshUI(txtTownCode);
        txtIdRegNum.setValue(null);
        GlobalCC.refreshUI(txtIdRegNum);
        txtDateCreated.setValue(null);
        GlobalCC.refreshUI(txtDateCreated);
        txtPrefixManager.setValue(null);
        GlobalCC.refreshUI(txtPrefixManager);
        txtTelPayPrefix.setValue(null);
        GlobalCC.refreshUI(txtTelPayPrefix);
        txtFax.setValue(null);
        GlobalCC.refreshUI(txtFax);
        txtProposer.setValue(null);
        GlobalCC.refreshUI(txtProposer);
        txtRemarks.setValue(null);
        GlobalCC.refreshUI(txtRemarks);
        txtWithdrawalReason.setValue(null);
        GlobalCC.refreshUI(txtWithdrawalReason);
        txtRunOff.setValue(null);
        GlobalCC.refreshUI(txtRunOff);
        txtSpecialTerms.setValue(null);
        GlobalCC.refreshUI(txtSpecialTerms);
        txtCancelledPolicy.setValue(null);
        GlobalCC.refreshUI(txtCancelledPolicy);
        txtIncreasePremium.setValue(null);
        GlobalCC.refreshUI(txtIncreasePremium);
        txtDeclinedProposal.setValue(null);
        GlobalCC.refreshUI(txtDeclinedProposal);
        txtAccountManagerCode.setValue(null);
        GlobalCC.refreshUI(txtAccountManagerCode);
        txtSectorCode.setValue(null);
        GlobalCC.refreshUI(txtSectorCode);
        txtSectorName.setValue(null);
        GlobalCC.refreshUI(txtSectorName);
        txtDomicileCountriesCode.setValue(null);
        GlobalCC.refreshUI(txtDomicileCountriesCode);
        txtPreferedBenefitPaymentModeCode.setValue(null);
        GlobalCC.refreshUI(txtPreferedBenefitPaymentModeCode);
        txtPreferedBenefitPaymentMode.setValue(null);
        GlobalCC.refreshUI(txtPreferedBenefitPaymentMode);
        txtPreferedPremiumPaymentCode.setValue(null);
        GlobalCC.refreshUI(txtPreferedPremiumPaymentCode);
        txtPreferedPremiumPayment.setValue(null);
        GlobalCC.refreshUI(txtPreferedPremiumPayment);
        txtEduLevel.setValue(null);
        GlobalCC.refreshUI(txtEduLevel);
        txtMonthlyIncome.setValue(null);
        GlobalCC.refreshUI(txtMonthlyIncome);
        txtContactName1.setValue(null);
        GlobalCC.refreshUI(txtContactName1);
        txtContactPhone1.setValue(null);
        GlobalCC.refreshUI(txtContactPhone1);
        txtContactEmail1.setValue(null);
        GlobalCC.refreshUI(txtContactEmail1);
        txtContactName2.setValue(null);
        GlobalCC.refreshUI(txtContactName2);
        txtContactPhone2.setValue(null);
        GlobalCC.refreshUI(txtContactPhone2);
        txtContactEmail2.setValue(null);
        GlobalCC.refreshUI(txtContactEmail2);
        txtAuditors.setValue(null);
        GlobalCC.refreshUI(txtAuditors);
        txtWebsite.setValue(null);
        GlobalCC.refreshUI(txtWebsite);
        txtParentCompany.setValue(null);
        GlobalCC.refreshUI(txtParentCompany);
        txtInsurer.setValue(null);
        GlobalCC.refreshUI(txtInsurer);
        txtProjectedBiz.setValue(null);
        GlobalCC.refreshUI(txtProjectedBiz);
        txtDLNo.setValue(null);
        GlobalCC.refreshUI(txtDLNo);
        txtRegBranchCode.setValue(null);
        GlobalCC.refreshUI(txtRegBranchCode);
        txtAccountOfficerCode.setValue(null);
        GlobalCC.refreshUI(txtAccountOfficerCode);
        txtCltOccupation.setValue(null);
        GlobalCC.refreshUI(txtCltOccupation);
        txtClientCellNos.setValue(null);
        GlobalCC.refreshUI(txtClientCellNos);
        txtCltBankTelNo.setValue(null);
        GlobalCC.refreshUI(txtCltBankTelNo);
        txtCltBankCellNo.setValue(null);
        GlobalCC.refreshUI(txtCltBankCellNo);
        txtCltEmployerTelNo.setValue(null);
        GlobalCC.refreshUI(txtCltEmployerTelNo);
        txtCltEmployerCellNo.setValue(null);
        GlobalCC.refreshUI(txtCltEmployerCellNo);
        txtStaffNo.setValue(null);
        GlobalCC.refreshUI(txtStaffNo);
        txtClientTypes.setValue(null);
        GlobalCC.refreshUI(txtClientTypes);
        txtSms.setValue(null);
        GlobalCC.refreshUI(txtSms);
        txtPayTel.setValue(null);
        GlobalCC.refreshUI(txtPayTel);
        txtIntTel.setValue(null);
        GlobalCC.refreshUI(txtIntTel);
        txtCountryCode3.setValue(null);
        GlobalCC.refreshUI(txtCountryCode3);
        txtDirectClient.setValue(null);
        GlobalCC.refreshUI(txtDirectClient);
        txtAgencyName.setValue(null);
        GlobalCC.refreshUI(txtAgencyName);
        txtAccountManager.setValue(null);
        GlobalCC.refreshUI(txtAccountManager);
        txtClientLevel.setValue(null);
        GlobalCC.refreshUI(txtClientLevel);
        txtAnniversary.setValue(null);
        GlobalCC.refreshUI(txtAnniversary);
        txtCreditRting.setValue(null);
        GlobalCC.refreshUI(txtCreditRting);
        txtBouncedCheque.setValue(null);
        GlobalCC.refreshUI(txtBouncedCheque);
        txtMaritalStatus.setValue(null);
        GlobalCC.refreshUI(txtMaritalStatus);
        txtModeOfComm.setValue(null);
        GlobalCC.refreshUI(txtModeOfComm);
        txtPayroll.setValue(null);
        GlobalCC.refreshUI(txtPayroll);
        txtMinSalary.setValue(null);
        GlobalCC.refreshUI(txtMinSalary);
        txtMaxSalary.setValue(null);
        GlobalCC.refreshUI(txtMaxSalary);
        txtWorkPermit.setValue(null);
        GlobalCC.refreshUI(txtWorkPermit);
        txtCountryCode2.setValue(null);
        GlobalCC.refreshUI(txtCountryCode2);
        txtDlIssueDate.setValue(null);
        GlobalCC.refreshUI(txtDlIssueDate);
    }

    public void refreshTabsForNewClient() {

        BigDecimal clientCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));

        // To solve the session problem
        hiddenClientCode.setValue(clientCode);
        GlobalCC.refreshUI(panelDetailSystems);

        // Change the save button text to update
        btnCreateUpdateClient.setText("Update");
        session.setAttribute("client_action", "E");
        btnCreateUpdateClient.setDisabled(false);

        // Client Accounts
        ADFUtils.findIterator("fetchClientAccountsIterator").executeQuery();
        GlobalCC.refreshUI(tblClientAccounts);

        // Get the Client web accounts
        ADFUtils.findIterator("fetchClientWebAccountsIterator").executeQuery();
        GlobalCC.refreshUI(tblwebAccounts);

        // Get the client systems
        ADFUtils.findIterator("fetchUnallocatedClientSystemsIterator").executeQuery();
        ADFUtils.findIterator("fetchAllocatedClientSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedClientSystems);
        GlobalCC.refreshUI(treeAssignedClientSystems);

        // Get the Client required documents
        ADFUtils.findIterator("fetchClientRequiredDocsIterator").executeQuery();
        GlobalCC.refreshUI(tblRequiredDocs);

        // Now we are ready to show the client tab with all the required details
        switchClientTabs(mode.EDIT_MODE);
    }

    /**
     * Delete a Client from the system
     *
     * @return
     */
    public String actionDeleteClient() {
        if (session.getAttribute("ClientCode") != null) {
            clientCodeValue.setValue(session.getAttribute("ClientCode").toString());
            String msg =
                "Are you sure you wish to DELETE Client number " + session.getAttribute("ClientCode").toString();
            olConfirmMsgValue.setValue(msg);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmationDialog" + "').show(hints);");
        } else {
            GlobalCC.errorValueNotEntered("You need to select a Client first!");
        }
        return null;
    }

    /**
     * Open up a popup dialog to enter new web account details
     *
     * @return null
     */
    public String actionNewWebAccount() {
        if (session.getAttribute("ClientCode") != null) {
            txtAccUsername.setValue(null);
            txtAccUsername.setDisabled(false);
            txtAccFullNames.setRendered(false);
            txtAccPassword.setValue(null);
            txtAccEmail.setValue(null);
            txtAccCode.setValue(null);
            txtAccPersonelRank.setValue(null);
            txtAccPassword.setRendered(true);
            btnSaveUpdateWebAccount.setText("Save");

            // Open the popup dialog for web accounts
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:webAccountPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No client Selected::");
        }
        return null;
    }


    /**
     * Create and save a new web account record
     *
     * @return null
     */
    public String actionSaveUpdateWebAccount() {
        if (btnSaveUpdateWebAccount.getText().equals("Update")) {
            actionUpdateWebAccount();
        } else {
            String accCode = null;
            String accUsername =
                GlobalCC.checkNullValues(txtAccUsername.getValue());
            /* String accFullNames =
                GlobalCC.checkNullValues(txtAccFullNames.getValue());*/
            String accPassword =
                GlobalCC.checkNullValues(txtAccPassword.getValue());
            String accPersonelRank =
                GlobalCC.checkNullValues(txtAccPersonelRank.getValue());
            String accStatus =
                GlobalCC.checkNullValues(txtAccStatus.getValue());
            String accAllowLogin =
                GlobalCC.checkNullValues(txtAccAllowLogin.getValue());
            String accEmail = GlobalCC.checkNullValues(txtAccEmail.getValue());

            if (accUsername == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Username is Empty");
                return null;

            } /*else if (accFullNames == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Full Names");
                return null;

            } */ else if (accPassword == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Password field is missing");
                return null;

            } else if (accPersonelRank == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Personel Rank");
                return null;

            } else if (accStatus == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a status");
                return null;

            } else {

                // Proceed to create a new web account
                String Query =
                    "begin tqc_clients_pkg.create_client_web_account(?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {
                    conn = connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    cst.setString(1, accUsername);
                    cst.setString(2, accPassword);
                    cst.setString(3, accAllowLogin);
                    cst.setString(4, null);
                    cst.setString(5, null);
                    cst.setString(6, accPersonelRank);
                    cst.setString(7, accStatus);
                    cst.setBigDecimal(8,
                                      new BigDecimal(session.getAttribute("ClientCode").toString()));
                    cst.setString(9, (String)session.getAttribute("Username"));
                    cst.setString(10, accEmail);
                    cst.setString(11, "A");
                    cst.setBigDecimal(12, null);
                    String clntType =
                        GlobalCC.checkNullValues(session.getAttribute("clientTypes"));
                    if ("C".equals(clntType)) {
                        cst.setString(13, "G");
                    } else {
                        cst.setString(13, "I");
                    }
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
                                         "pt1:webAccountPop" +
                                         "').hide(hints);");

                    String message = "Web Account Saved Successfully!";
                    FacesContext.getCurrentInstance().addMessage(null,
                                                                 new FacesMessage(message));

                    ADFUtils.findIterator("fetchClientWebAccountsIterator").executeQuery();
                    GlobalCC.refreshUI(tblwebAccounts);

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    /**
     * Create and save a new web account record
     *
     * @return null
     */
    public String actionUpdateWebAccount() {
        String accCode = GlobalCC.checkNullValues(txtAccCode.getValue());
        String accUsername =
            GlobalCC.checkNullValues(txtAccUsername.getValue());
        /* String accFullNames =
            GlobalCC.checkNullValues(txtAccFullNames.getValue());*/
        String accPassword =
            GlobalCC.checkNullValues(txtAccPassword.getValue());
        String accPersonelRank =
            GlobalCC.checkNullValues(txtAccPersonelRank.getValue());
        String accStatus = GlobalCC.checkNullValues(txtAccStatus.getValue());
        String accAllowLogin =
            GlobalCC.checkNullValues(txtAccAllowLogin.getValue());
        String accEmail = GlobalCC.checkNullValues(txtAccEmail.getValue());

        if (accCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Account Code is Empty");
            return null;

        } else if (accUsername == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Username is Empty");
            return null;

        } /* else if (accFullNames == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Full Names");
            return null;
*/
        /*} else if (accPassword == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Password field is missing");
            return null;

        } */ else if (accPersonelRank == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Personel Rank");
            return null;

        } else if (accStatus == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select a status");
            return null;

        } else {

            // Proceed to create a new web account
            String Query =
                "begin tqc_clients_pkg.create_client_web_account(?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, accUsername);
                cst.setString(2, accPassword);
                cst.setString(3, accAllowLogin);
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setString(6, accPersonelRank);
                cst.setString(7, accStatus);
                cst.setBigDecimal(8,
                                  new BigDecimal(session.getAttribute("ClientCode").toString()));
                cst.setString(9, (String)session.getAttribute("Username"));
                cst.setString(10, accEmail);
                cst.setString(11, "E");
                cst.setBigDecimal(12, new BigDecimal(accCode));

                // FIXING NullPointerException
                if (session.getAttribute("clientTypes") != null &&
                    session.getAttribute("clientTypes").equals("C")) {
                    cst.setString(13, "G");
                } else {
                    cst.setString(13, "I");
                }
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
                                     "pt1:webAccountPop" + "').hide(hints);");

                String message = "Web Account Edited Successfully!";
                FacesContext.getCurrentInstance().addMessage(null,
                                                             new FacesMessage(message));

                ADFUtils.findIterator("fetchClientWebAccountsIterator").executeQuery();
                GlobalCC.refreshUI(tblwebAccounts);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public void setTblwebAccounts(RichTable tblwebAccounts) {
        this.tblwebAccounts = tblwebAccounts;
    }

    public RichTable getTblwebAccounts() {
        return tblwebAccounts;
    }

    /**
     * Delete a web account record.
     * @return null
     */
    public String actionDeleteWebAccount() {

        Object key2 = tblwebAccounts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteClientWebAccount" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }

        return null;

    }

    public void actionConfirmDeleteWebAcc(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = tblwebAccounts.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;


            BigDecimal clientAccountCode = null;
            clientAccountCode =
                    (BigDecimal)nodeBinding.getAttribute("clientAccCode");

            // Proceed to delete the web account
            String Query =
                "begin tqc_clients_pkg.create_client_web_account(?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, null);
                cst.setString(2, null);
                cst.setString(3, null);
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setString(6, null);
                cst.setString(7, null);
                cst.setBigDecimal(8, null);
                cst.setString(9, null);
                cst.setString(10, null);
                cst.setString(11, "D");
                cst.setBigDecimal(12, clientAccountCode);

                // FIXING NullPointerException
                if (session.getAttribute("clientTypes") != null &&
                    session.getAttribute("clientTypes").equals("C")) {
                    cst.setString(13, "G");
                } else {
                    cst.setString(13, "I");
                }
                cst.execute();

                // Close the connections
                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchClientWebAccountsIterator").executeQuery();
                GlobalCC.refreshUI(tblwebAccounts);

                String message = "Web Account Deleted Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);

            }
        }
    }

    public void actionConfirmDeleteRequiredDocs(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            Object key2 = tblRequiredDocs.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            String Query =
                "begin TQC_WEB_PKG.saveClientDocuments(?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {

                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal(nodeBinding.getAttribute("CDOCR_CODE").toString()));
                cst.setBigDecimal(3,
                                  new BigDecimal(nodeBinding.getAttribute("CDOCR_RDOC_CODE").toString()));
                cst.setString(4, null);
                cst.setString(5, null);
                cst.setDate(6, null);
                cst.setString(7, null);
                cst.setString(8, null);
                cst.setString(9, session.getAttribute("Username").toString());

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

            ADFUtils.findIterator("fetchClientRequiredDocsIterator").executeQuery();
            GlobalCC.refreshUI(tblRequiredDocs);


        }

    }

    public String actionEditWebAccount() {
        Object key2 = tblwebAccounts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAccUsername.setValue(nodeBinding.getAttribute("clientAccUserName"));
            txtAccUsername.setDisabled(true);
            txtAccFullNames.setValue(nodeBinding.getAttribute("clientAccName"));
            txtAccFullNames.setRendered(false);
            txtAccPassword.setRendered(false); // nodeBinding.getAttribute("clientAccUserName") );
            txtAccEmail.setValue(nodeBinding.getAttribute("clientAccEmail"));
            txtAccCode.setValue(nodeBinding.getAttribute("clientAccCode"));
            txtAccPersonelRank.setValue(nodeBinding.getAttribute("clientAccPersonelRank"));
            txtAccStatus.setValue(nodeBinding.getAttribute("clientAccStatus"));
            txtAccAllowLogin.setValue(nodeBinding.getAttribute("clientAccLoginAllowed"));

            btnSaveUpdateWebAccount.setText("Update");

            // Open the popup dialog for web accounts
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:webAccountPop" + "').show(hints);");
        }
        return null;
    }

    public void setPanelClientSystems(RichPanelBox panelClientSystems) {
        this.panelClientSystems = panelClientSystems;
    }

    public RichPanelBox getPanelClientSystems() {
        return panelClientSystems;
    }

    public void setTreeAssignedClientSystems(RichTree treeAssignedClientSystems) {
        this.treeAssignedClientSystems = treeAssignedClientSystems;
    }

    public RichTree getTreeAssignedClientSystems() {
        return treeAssignedClientSystems;
    }

    public void setTreeUnassignedClientSystems(RichTree treeUnassignedClientSystems) {
        this.treeUnassignedClientSystems = treeUnassignedClientSystems;
    }

    public RichTree getTreeUnassignedClientSystems() {
        return treeUnassignedClientSystems;
    }

    public void unassignedClientSystemSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeUnassignedClientSystems.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeUnassignedClientSystems.getRowData();

                    txtSelectedClientSystemCode.setValue(nd.getRow().getAttribute("sysCode"));
                }
            }
        }
        GlobalCC.refreshUI(panelDetailSystems);
    }

    public void assignedClientSystemSelectionListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeAssignedClientSystems.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeAssignedClientSystems.getRowData();

                    txtSelectedClientSystemCode.setValue(nd.getRow().getAttribute("sysCode"));
                }
            }
        }
        GlobalCC.refreshUI(panelDetailSystems);
    }

    public void switchClientTabs(mode mode) {
        switch (mode) {

        case EDIT_MODE:

            tabClientSystems.setRendered(true);
            tabClientWebAccounts.setRendered(true);
            tabClientDocs.setRendered(true);
            tabDetailCreateClient.setDisclosed(true);
            tabDetailSearchClients.setDisclosed(false);
            tabDetailCreateClient.setVisible(true);
            tabClientAccounts.setVisible(true);
            GlobalCC.refreshUI(tabDetailCreateClient);
            GlobalCC.refreshUI(tabClientAccounts);
            if (sbrCorporate.isSelected()) {
                tabDirectors.setVisible(true);
                tabSignatories.setVisible(true);
                clientMeansOfIdPan.setVisible(false);
                txtClientMeansOfIdDateIssued.setVisible(false);
                txtClientMeansOfIdIssuedBy.setVisible(false);
                txtClientMeansOfIdDateExpired.setVisible(false);
                txtClientMeansOfIdIssuingCountry.setVisible(false);
                txtClientNationalityCode.setVisible(false);
                txtClientUtilityBill.setVisible(false);
                tabAuditors.setVisible(true);
                txtParentCompanyCode.setVisible(true);
                layClientImage.setVisible(false);
            }


            else {
                tabDirectors.setVisible(false);
                tabSignatories.setVisible(false);
                
                if(GlobalCC.getSysParamValue("CLIENT").equalsIgnoreCase("ENSURE")){
                clientMeansOfIdPan.setVisible(true);
                txtClientMeansOfIdDateIssued.setVisible(true);
                txtClientMeansOfIdIssuedBy.setVisible(true);
                txtClientMeansOfIdDateExpired.setVisible(true);
                txtClientMeansOfIdIssuingCountry.setVisible(true);
                    txtClientNationalityCode.setVisible(true);
                    txtClientUtilityBill.setVisible(true);
                }
               
                
                tabAuditors.setVisible(false);
                layClientImage.setVisible(true);
                //  txtGender.setRendered(true);
            }
            GlobalCC.refreshUI(mainPanel);
            break;

        case NEW_MODE:

            tabClientSystems.setRendered(false);
            tabClientWebAccounts.setRendered(false);
            tabClientDocs.setRendered(true);
            tabDetailCreateClient.setDisclosed(true);
            tabDetailSearchClients.setDisclosed(false);
            tabDetailCreateClient.setVisible(true);
            GlobalCC.refreshUI(tabDetailCreateClient);
            session.setAttribute("ClientCode", null);

            sbrIndividual.setSelected(true);
            layClientImage.setVisible(true);
            // txtGender.setRendered(true);


            clientSignature.setSource(null);
            clientPhoto.setSource(null);
            GlobalCC.refreshUI(mainPanel);
            break;

        case DEFAULT_MODE:
            tabDetailSearchClients.setDisclosed(true);
            GlobalCC.refreshUI(mainPanel);
            break;
        }
    }

    /**
     * Allocates a system to a client. It takes in the current client's Id and
     * the selected system code.
     *
     * @return null
     */
    public String processAddClientSystem() {
        boolean processStatusOK = true;
        if (session.getAttribute("ClientCode") == null ||
            (session.getAttribute("ClientCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a Client first.");
        }

        if (txtSelectedClientSystemCode.getValue() == null ||
            txtSelectedClientSystemCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a System first.");
        }
        String status = check_ifclientActive();
        if (!status.equals("A")) {

            GlobalCC.INFORMATIONREPORTING("The Client Status Should be Active::");
            return null;
        }

        if (processStatusOK) {
            DBConnector connector = new DBConnector();
            OracleConnection connection = null;
            try {
                connection =
                        (OracleConnection)connector.getDatabaseConnection();
                String query =
                    "begin tqc_clients_pkg.alloc_clnt_system(?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        new BigDecimal(hiddenClientCode.getValue().toString()));
                statement.setString(2,
                                    txtSelectedClientSystemCode.getValue().toString());
                statement.execute();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }

        txtSelectedClientSystemCode.setValue(null);

        // Get the client systems
        ADFUtils.findIterator("fetchUnallocatedClientSystemsIterator").executeQuery();
        ADFUtils.findIterator("fetchAllocatedClientSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedClientSystems);
        GlobalCC.refreshUI(treeAssignedClientSystems);

        return null;
    }

    /**
     * Unallocate a Sytem from a client. Accepts the current Client's Code and
     * the selected System code
     *
     * @return null
     */
    public String processRemoveClientSystem() {
        boolean processStatusOK = true;
        if (session.getAttribute("ClientCode") == null ||
            (session.getAttribute("ClientCode") == "")) {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a Client first.");
        }

        if (txtSelectedClientSystemCode.getValue() == null ||
            txtSelectedClientSystemCode.getValue() == "") {
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
                    "begin tqc_clients_pkg.unalloc_clnt_system(?,?); end;";
                OracleCallableStatement statement = null;

                statement =
                        (OracleCallableStatement)connection.prepareCall(query);
                statement.setBigDecimal(1,
                                        new BigDecimal(session.getAttribute("ClientCode").toString()));
                statement.setBigDecimal(2,
                                        new BigDecimal(txtSelectedClientSystemCode.getValue().toString()));
                statement.execute();

                statement.close();
                connection.commit();
                connection.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(connection, e);
            }
        }

        txtSelectedClientSystemCode.setValue(null);

        // Get the client systems
        ADFUtils.findIterator("fetchUnallocatedClientSystemsIterator").executeQuery();
        ADFUtils.findIterator("fetchAllocatedClientSystemsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedClientSystems);
        GlobalCC.refreshUI(treeAssignedClientSystems);

        return null;
    }

    public void setTblRequiredDocs(RichTable tblRequiredDocs) {
        this.tblRequiredDocs = tblRequiredDocs;
    }

    public RichTable getTblRequiredDocs() {
        return tblRequiredDocs;
    }

    public void setTabDetailCreateClient(RichShowDetailItem tabDetailCreateClient) {
        this.tabDetailCreateClient = tabDetailCreateClient;
    }

    public RichShowDetailItem getTabDetailCreateClient() {
        return tabDetailCreateClient;
    }

    public void setTabDetailSearchClients(RichShowDetailItem tabDetailSearchClients) {
        this.tabDetailSearchClients = tabDetailSearchClients;
    }

    public RichShowDetailItem getTabDetailSearchClients() {
        return tabDetailSearchClients;
    }

    public void setTabClientInfo(RichShowDetailItem tabClientInfo) {
        this.tabClientInfo = tabClientInfo;
    }

    public RichShowDetailItem getTabClientInfo() {
        return tabClientInfo;
    }

    public void setTabClientSystems(RichShowDetailItem tabClientSystems) {
        this.tabClientSystems = tabClientSystems;
    }

    public RichShowDetailItem getTabClientSystems() {
        return tabClientSystems;
    }

    public void setTabClientWebAccounts(RichShowDetailItem tabClientWebAccounts) {
        this.tabClientWebAccounts = tabClientWebAccounts;
    }

    public RichShowDetailItem getTabClientWebAccounts() {
        return tabClientWebAccounts;
    }

    public void setTabClientDocs(RichShowDetailItem tabClientDocs) {
        this.tabClientDocs = tabClientDocs;
    }

    public RichShowDetailItem getTabClientDocs() {
        return tabClientDocs;
    }

    public String actionEditRequiredDoc() {
        Object key2 = tblRequiredDocs.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDocCodePop.setValue(nodeBinding.getAttribute("CDOCR_CODE"));
            txtReqDocCodePop.setValue(nodeBinding.getAttribute("CDOCR_RDOC_CODE"));
            txtReqDocNamePop.setValue(nodeBinding.getAttribute("ROC_DESC"));
            txtDocClientCodePop.setValue(nodeBinding.getAttribute("CDOCR_CLNT_CODE"));


            session.setAttribute("clientDocSubmitted",
                                 nodeBinding.getAttribute("CDOCR_SUBMITED"));
            session.setAttribute("clientDocId",
                                 nodeBinding.getAttribute("CDOCR_DOCID"));

            txtDocDateSubmittedPop.setValue(nodeBinding.getAttribute("CDOCR_DATE_S"));
            txtDocRefNumPop.setValue(nodeBinding.getAttribute("CDOCR_REF_NO"));
            txtDocRemarkPop.setValue(nodeBinding.getAttribute("CDOCR_RMRK"));
            txtDocUserReceivedPop.setValue(nodeBinding.getAttribute("CDOCR_USER_RECEIVD"));

            btnSaveRequiredDoc.setText("Edit");

            // Open the popup dialog for required documents
            GlobalCC.showPopup("pt1:requiredDocsPop");

        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String actionNewRequiredDoc() {
        if (session.getAttribute("ClientCode") != null) {
            txtDocCodePop.setValue(null);
            txtReqDocCodePop.setValue(null);
            txtReqDocNamePop.setValue(null);
            txtDocClientCodePop.setValue(session.getAttribute("ClientCode"));

            session.setAttribute("clientDocSubmitted", "N");
            session.setAttribute("clientDocId", null);

            txtDocDateSubmittedPop.setValue(null);
            txtDocRefNumPop.setValue(null);
            txtDocRemarkPop.setValue(null);
            txtDocUserReceivedPop.setValue(session.getAttribute("Username"));

            btnSaveRequiredDoc.setText("Save");

            // Open the popup dialog for required documents
            GlobalCC.showPopup("pt1:requiredDocsPop");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Client Selected:");
        }
        return null;
    }

    public String actionDeleteRequiredDoc() {
        Object key2 = tblRequiredDocs.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            GlobalCC.showPopup("pt1:confirmDeleteClientRequiredDocs");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }

        return null;
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

    public void setTblReqDocsList(RichTable tblReqDocsList) {
        this.tblReqDocsList = tblReqDocsList;
    }

    public RichTable getTblReqDocsList() {
        return tblReqDocsList;
    }

    /*public String actionSaveRequiredDoc() {
        // Check if the user wishes to SAVE or UPDATE
        if (btnSaveRequiredDoc.getText().equals("Edit")) {
            // Proceed to edit
            String status = actionEditClientRequiredDoc();

        } else {
            String docCode = null;
            String docReqDocCode =
                GlobalCC.checkNullValues(txtReqDocCodePop.getValue());
            String docClientCode =
                GlobalCC.checkNullValues(txtDocClientCodePop.getValue());
            String docSubmitted =
                GlobalCC.checkNullValues(txtDocSubmittedPop.getValue());
            String docDateSubmit =
                GlobalCC.checkNullValues(txtDocDateSubmittedPop.getValue());
            String docRefNum =
                GlobalCC.checkNullValues(txtDocRefNumPop.getValue());
            String docRemark =
                GlobalCC.checkNullValues(txtDocRemarkPop.getValue());

            if (docReqDocCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Required Document is Empty");
                return null;

            } else if (docClientCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: You need to select a client");
                return null;

            } else if (docSubmitted == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Specify if Submitted");
                return null;

            } else if (docDateSubmit == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Enter Personel Rank");
                return null;

            } else {

                String Query =
                    "begin TQC_WEB_PKG.saveClientDocuments(?,?,?,?,?,?,?,?,?); end;";
                DBConnector connector = new DBConnector();
                OracleCallableStatement cst = null;
                OracleConnection conn = null;

                try {

                    conn = (OracleConnection)connector.getDatabaseConnection();
                    cst = (OracleCallableStatement)conn.prepareCall(Query);

                    // Take care of all the date fields on the form. Make sure they are not null.
                    // If the dates are null, then default to the current date.
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                    Date docSubmitDate = new Date();
                    if (txtDocDateSubmittedPop.getValue() != null &&
                        !(txtDocDateSubmittedPop.getValue().equals(""))) {
                        String date1 =
                            df.format(txtDocDateSubmittedPop.getValue());
                        docSubmitDate = df.parse(date1);
                    }

                    cst.setString(1, "A");
                    cst.setBigDecimal(2, null);
                    cst.setString(3, docReqDocCode);
                    cst.setString(4, docClientCode);
                    cst.setString(5, docSubmitted);
                    cst.setDate(6, new java.sql.Date(docSubmitDate.getTime()));
                    cst.setString(7, docRefNum);
                    cst.setString(8, docRemark);
                    cst.setString(9, (String)session.getAttribute("Username"));

                    cst.execute();
                    cst.close();
                    conn.close();
                    ADFUtils.findIterator("fetchClientRequiredDocsIterator").executeQuery();
                    GlobalCC.refreshUI(tblRequiredDocs);

                    GlobalCC.dismissPopUp("pt1", "requiredDocsPop");


                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);
                }
            }
        }
        return null;
    }

    public String actionEditClientRequiredDoc() {
        String docCode = GlobalCC.checkNullValues(txtDocCodePop.getValue());
        ;
        String docReqDocCode =
            GlobalCC.checkNullValues(txtReqDocCodePop.getValue());
        String docClientCode =
            GlobalCC.checkNullValues(txtDocClientCodePop.getValue());
        String docSubmitted =
            GlobalCC.checkNullValues(txtDocSubmittedPop.getValue());
        String docDateSubmit =
            GlobalCC.checkNullValues(txtDocDateSubmittedPop.getValue());
        String docRefNum =
            GlobalCC.checkNullValues(txtDocRefNumPop.getValue());
        String docRemark =
            GlobalCC.checkNullValues(txtDocRemarkPop.getValue());

        if (docReqDocCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
            return null;

        } else if (docReqDocCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Required Document is Empty");
            return null;

        } else if (docClientCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: You need to select a client");
            return null;

        } else if (docSubmitted == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Specify if Submitted");
            return null;

        } else if (docDateSubmit == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Personel Rank");
            return null;

        } else {

            String Query =
                "begin TQC_WEB_PKG.saveClientDocuments(?,?,?,?,?,?,?,?,?); end;";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {

                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                // Take care of all the date fields on the form. Make sure they are not null.
                // If the dates are null, then default to the current date.
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Date docSubmitDate = new Date();
                if (txtDocDateSubmittedPop.getValue() != null &&
                    !(txtDocDateSubmittedPop.getValue().equals(""))) {
                    String date1 =
                        df.format(txtDocDateSubmittedPop.getValue());
                    docSubmitDate = df.parse(date1);
                }

                cst.setString(1, "E");
                cst.setBigDecimal(2, new BigDecimal(docCode));
                cst.setString(3, docReqDocCode);
                cst.setString(4, docClientCode);
                cst.setString(5, docSubmitted);
                cst.setDate(6, new java.sql.Date(docSubmitDate.getTime()));
                cst.setString(7, docRefNum);
                cst.setString(8, docRemark);
                cst.setString(9, (String)session.getAttribute("Username"));

                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchClientRequiredDocsIterator").executeQuery();
                GlobalCC.refreshUI(tblRequiredDocs);
                GlobalCC.dismissPopUp("pt1", "requiredDocsPop");
                String message = "Record UPDATED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }*/

    public String actionSaveRequiredDoc() {
        // Check if the user wishes to SAVE or UPDATE
        String action = "A";
        if (btnSaveRequiredDoc.getText().equals("Edit")) {
            action = "E";
        } else {
            action = "A";
        }
        BigDecimal docCode =
            GlobalCC.checkBDNullValues(txtDocCodePop.getValue());
        String docReqDocCode =
            GlobalCC.checkNullValues(txtReqDocCodePop.getValue());
        BigDecimal docClientCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
        String docSubmitted =
            GlobalCC.checkNullValues(session.getAttribute("clientDocSubmitted"));
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
        if (docClientCode == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: You need to select a client!");
            return null;
        }
        if (docDateSubmit == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Enter Date Submitted!");
            return null;
        }
        String Query =
            "begin TQC_WEB_PKG.save_client_docs(?,?,?,?,?,?,?,?,?,?,?,?); end;";
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;

        try {

            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);

            cst.setString(1, action);
            cst.setBigDecimal(2, docCode);
            cst.setString(3, docReqDocCode);
            cst.setBigDecimal(4, docClientCode);
            cst.setString(5, docSubmitted);
            cst.setDate(6, GlobalCC.extractDate(txtDocDateSubmittedPop));
            cst.setString(7, docRefNum);
            cst.setString(8, docRemark);
            cst.setString(9, (String)session.getAttribute("Username"));
            cst.setString(10, (String)session.getAttribute("clientDocId"));
            cst.registerOutParameter(11,
                                     oracle.jdbc.internal.OracleTypes.VARCHAR);
            cst.registerOutParameter(12,
                                     oracle.jdbc.internal.OracleTypes.VARCHAR);


            cst.execute();
            String status = GlobalCC.checkNullValues(cst.getString(11));
            String msg = GlobalCC.checkNullValues(cst.getString(12));
            cst.close();
            conn.close();
            if (!"S".equals(status)) {
                GlobalCC.errorValueNotEntered(msg);
                return null;
            }
            ADFUtils.findIterator("fetchClientRequiredDocsIterator").executeQuery();
            GlobalCC.refreshUI(tblRequiredDocs);
            GlobalCC.dismissPopUp("pt1", "requiredDocsPop");
            GlobalCC.INFORMATIONREPORTING(msg);
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setConfirmationDialog(RichPopup confirmationDialog) {
        this.confirmationDialog = confirmationDialog;
    }

    public RichPopup getConfirmationDialog() {
        return confirmationDialog;
    }

    public void confirmationDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            String Query = "begin ? := tqc_clients_pkg.delete_client(?,?";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;


            try {

                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.registerOutParameter(1, OracleTypes.VARCHAR);
                // Add or Edit
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("ClientCode").toString())); // Client Code

                cst.execute();
                String err = cst.getString(1);
                cst.close();
                conn.commit();
                conn.close();
                if (err != null) {
                    GlobalCC.INFORMATIONREPORTING(err);
                } else {
                    session.removeAttribute("ClientCode");
                    ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
                    GlobalCC.refreshUI(tblClients);

                    String message = "Client DELETED Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                } //showMessagePopup(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);

            }


        }

    }

    /**
     * Called upon from time to time to display various messages from the
     * server i.e. Completing of successful edit, delete or edit.
     */
    public void showMessagePopup(String msgToDisplay) {
        textToShow.setValue(null);
        textToShow.setValue(msgToDisplay);

        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent source = tblClients;
        String alignId = source.getClientId(context);
        String popupId = "pt1:ServerMessage";

        StringBuilder script = new StringBuilder();
        script.append("var popup = AdfPage.PAGE.findComponent('").append(popupId).append("'); ").append("if (!popup.isPopupVisible()) { ").append("var hints = {}; ").append("hints[AdfRichPopup.HINT_ALIGN_ID] = '").append(alignId).append("'; ").append("hints[AdfRichPopup.HINT_ALIGN] = AdfRichPopup.ALIGN_AFTER_START; ").append("popup.show(hints);}");
        ExtendedRenderKitService erks =
            Service.getService(context.getRenderKit(),
                               ExtendedRenderKitService.class);
        erks.addScript(context, script.toString());
    }

    public void setClientCodeValue(RichOutputLabel clientCodeValue) {
        this.clientCodeValue = clientCodeValue;
    }

    public RichOutputLabel getClientCodeValue() {
        return clientCodeValue;
    }

    public void setOlConfirmMsgValue(RichOutputLabel olConfirmMsgValue) {
        this.olConfirmMsgValue = olConfirmMsgValue;
    }

    public RichOutputLabel getOlConfirmMsgValue() {
        return olConfirmMsgValue;
    }

    public void setTextToShow(RichOutputText textToShow) {
        this.textToShow = textToShow;
    }

    public RichOutputText getTextToShow() {
        return textToShow;
    }

    public void setTxtClientTitle(RichInputText txtClientTitle) {
        this.txtClientTitle = txtClientTitle;
    }

    public RichInputText getTxtClientTitle() {
        return txtClientTitle;
    }

    public void setTblClientTitles(RichTable tblClientTitles) {
        this.tblClientTitles = tblClientTitles;
    }

    public RichTable getTblClientTitles() {
        return tblClientTitles;
    }

    public String actionAcceptClientTitle() {
        Object key2 = tblClientTitles.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtClientTitleCode.setValue(nodeBinding.getAttribute("shortDesc"));
            txtClientTitle.setValue(nodeBinding.getAttribute("description"));
            GlobalCC.refreshUI(txtClientTitle);
        }
        GlobalCC.dismissPopUp("pt1", "clientTitlePop");
        return null;
    }


    public String actionAcceptSignTitle() {
        Object key2 = tblSignTitles.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtSignatoryTitleCode.setValue(nodeBinding.getAttribute("shortDesc"));
            txtSignatoryTitle.setValue(nodeBinding.getAttribute("description"));
            GlobalCC.refreshUI(txtSignatoryTitle);
            GlobalCC.refreshUI(txtSignatoryTitleCode);
        }
        GlobalCC.dismissPopUp("pt1", "SignAtoryTitlePop");
        return null;
    }


    public String actionAcceptDirTitle() {
        Object key2 = tblDirTitles.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDirectorTitleCode.setValue(nodeBinding.getAttribute("shortDesc"));
            txtDirectorTitle.setValue(nodeBinding.getAttribute("description"));
            GlobalCC.refreshUI(txtDirectorTitle);
            GlobalCC.refreshUI(txtDirectorTitleCode);
        }
        GlobalCC.dismissPopUp("pt1", "DirectorTitlePop");
        return null;
    }


    public String actionAcceptDirNationality() {
        Object key2 = tblDirNationality.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            //txtDirectorTitleCode.setValue(nodeBinding.getAttribute("shortDesc"));
            txtDirectorNationality.setValue(nodeBinding.getAttribute("nationality"));
            GlobalCC.refreshUI(txtDirectorNationality);
            // GlobalCC.refreshUI(txtDirectorTitleCode);
        }
        GlobalCC.dismissPopUp("pt1", "diroNationPop");
        return null;
    }

    public String actionAcceptDirOccup() {
        Object key2 = tblDirOccup.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDirectorOccupation.setValue(nodeBinding.getAttribute("occName"));
            //txtDirectorTitle.setValue(nodeBinding.getAttribute("description"));
            GlobalCC.refreshUI(txtDirectorOccupation);
            //GlobalCC.refreshUI(txtDirectorTitleCode);
        }
        GlobalCC.dismissPopUp("pt1", "diroccupationPop");
        return null;
    }

    public String actionUpdateClientAccount() throws Exception {

        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {

            String accCode =
                GlobalCC.checkNullValues(txtAccountCode.getValue());
            String accShtDesc =
                GlobalCC.checkNullValues(txtAccShortDesc.getValue());
            String accName = GlobalCC.checkNullValues(txtAccName.getValue());
            String accClientCode =
                GlobalCC.checkNullValues(txtAccClientCode.getValue());
            String accCreatedBy =
                GlobalCC.checkNullValues(txtAccCreatedBy.getValue());
            String accDateCreated =
                GlobalCC.checkNullValues(txtAccDateCreated.getValue());
            String accStatus =
                GlobalCC.checkNullValues(txtClientAccStatus.getValue());
            String accRemarks =
                GlobalCC.checkNullValues(txtAccRemarks.getValue());
            String accWef = GlobalCC.checkNullValues(txtAccWef.getValue());
            String accWet = GlobalCC.checkNullValues(txtAccWet.getValue());
            String accCntName =
                GlobalCC.checkNullValues(txtAccCntName.getValue());
            String accCntTitle =
                GlobalCC.checkNullValues(txtAccCntTitle.getValue());
            String accCntSmsNo =
                GlobalCC.checkNullValues(txtAccCntSmsNo.getValue());
            String accCntEmail =
                GlobalCC.checkNullValues(txtAccCntEmail.getValue());

            if (accCode == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Code is Empty");
                return null;

            }
            if (accShtDesc == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description is Empty");
                return null;

            }
            if (accName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
                return null;

            }
            if (accWef == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WEF date is missing");
                return null;

            }
            if (accStatus == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a status");
                return null;

            }
            if (accCntName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Name!");
                return null;
            }
            if (accCntTitle == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Title!");
                return null;
            }
            if (accCntSmsNo == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact SmsNo!");
                return null;
            }
            if (accCntEmail == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Email!");
                return null;
            }


            GlobalCC.hidePopup("pt1:clientAccountPopup");

            String message = "Client Account UPDATED Successfully!";
            showMessagePopup(message);

            ADFUtils.findIterator("fetchClientAccountsIterator").executeQuery();
            GlobalCC.refreshUI(tblClientAccounts);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return null;
    }

    public String actionSaveUpdateClientAccount() {
        DBConnector connector = new DBConnector();
        OracleCallableStatement cst = null;
        OracleConnection conn = null;
        try {
            BigDecimal accCode =
                GlobalCC.checkBDNullValues(txtAccountCode.getValue());
            String accName = GlobalCC.checkNullValues(txtAccName.getValue());
            String accClientCode =
                GlobalCC.checkNullValues(txtAccClientCode.getValue());
            String shortDesc =
                GlobalCC.checkNullValues(txtAccShortDesc.getValue());
            String accCreatedBy =
                GlobalCC.checkNullValues(txtAccCreatedBy.getValue());
            String accDateCreated =
                GlobalCC.checkNullValues(txtAccDateCreated.getValue());
            String accStatus =
                GlobalCC.checkNullValues(txtClientAccStatus.getValue());
            String accRemarks =
                GlobalCC.checkNullValues(txtAccRemarks.getValue());
            String accWef = GlobalCC.checkNullValues(txtAccWef.getValue());
            String accWet = GlobalCC.checkNullValues(txtAccWet.getValue());
            String cltCellNos =
                GlobalCC.checkNullValues(txtClientCellNos.getValue());

            String accCntName =
                GlobalCC.checkNullValues(txtAccCntName.getValue());
            String accCntTitle =
                GlobalCC.checkNullValues(txtAccCntTitle.getValue());
            String accCntSmsNo =
                GlobalCC.checkNullValues(txtAccCntSmsNo.getValue());
            String accCntEmail =
                GlobalCC.checkNullValues(txtAccCntEmail.getValue());


            if (txtBrnDivName.getValue() == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing Select Branch Division");
                return null;
            }


            if (accName == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Name is Empty");
                return null;

            }
            if (accWef == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WEF date is missing");
                return null;

            }
            if (accStatus == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select a status");
                return null;
            }

            if (accCntName == null &&
                renderer.isCLIENT_CONTACT_NAME_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Name!");
                return null;
            }
            if (accCntTitle == null &&
                renderer.isCLIENT_CONTACT_TITLE_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Title!");
                return null;
            }
            if (accCntSmsNo == null &&
                renderer.isCLIENT_CONTACT_SMSNO_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact SmsNo!");
                return null;
            }
            if (accCntEmail == null &&
                renderer.isCLIENT_CONTACT_EMAIL_REQUIRED()) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Contact Email!");
                return null;
            }

            String Query =
                "begin tqc_clients_pkg.create_client_account(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";

            conn = (OracleConnection)connector.getDatabaseConnection();
            cst = (OracleCallableStatement)conn.prepareCall(Query);


            String checkNullValues =
                GlobalCC.checkNullValues(session.getAttribute("ClientCode"));
            session.getAttribute("ClientCode");


            cst.setString(1,
                          (String)session.getAttribute("clientAccountAction"));
            cst.setBigDecimal(2, accCode);
            cst.setString(3, shortDesc);
            cst.setString(4, accName);
            cst.setObject(5, session.getAttribute("ClientCode"));
            cst.setString(6, (String)session.getAttribute("Username"));
            cst.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
            cst.setString(8, accStatus);
            cst.setString(9, accRemarks);
            cst.setDate(10, GlobalCC.extractDate(txtAccWef));
            cst.setDate(11, GlobalCC.extractDate(txtAccWet));
            cst.setBigDecimal(12,
                              (BigDecimal)session.getAttribute("BrnDivCode"));
            cst.setBigDecimal(13,
                              (BigDecimal)session.getAttribute("branchCode"));
            cst.setString(14, accCntName);
            cst.setString(15, accCntTitle);
            cst.setString(16, accCntSmsNo);
            cst.setString(17, accCntEmail);
            cst.execute();

            // Close the connections
            cst.close();
            conn.commit();
            conn.close();

            GlobalCC.hidePopup("pt1:clientAccountPopup");
            String message = "Client Account ADDED Successfully!";
            showMessagePopup(message);

            ADFUtils.findIterator("fetchClientAccountsIterator").executeQuery();
            GlobalCC.refreshUI(tblClientAccounts);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            e.printStackTrace();
        }
        return null;
    }

    public void clearClientAccountFields() {
        txtAccountCode.setValue(null);
        txtAccShortDesc.setValue(null);
        if (txtSurname.getValue() != null) {
            txtAccName.setValue(txtSurname.getValue());
        } else {
            txtAccName.setValue(null);
        }
        if (txtWef.getValue() != null) {
            txtAccWef.setValue(txtWef.getValue());
        } else {
            txtAccWef.setValue(null);
        }
        if (session.getAttribute("ClientCode") != null) {
            txtAccClientCode.setValue(session.getAttribute("ClientCode").toString());
        } else {
            txtAccClientCode.setValue(null);
        }
        txtAccCreatedBy.setValue(null);
        txtAccDateCreated.setValue(null);
        //txtClientAccStatus.setValue(null);
        txtAccRemarks.setValue(null);

        txtAccWet.setValue(null);
        txtClientAccStatus.setDisabled(false);
    }

    public String actionNewClientAccount() {

        try {
            if (session.getAttribute("ClientCode") != null) {

                clearClientAccountFields();
                btnSaveUpdateClientAccount.setText("Save");
                session.setAttribute("clientAccountAction", "A");
                txtAccCntName.setValue(null);
                txtAccCntTitle.setValue(null);
                txtAccCntSmsNo.setValue(null);
                txtAccCntEmail.setValue(null);
                session.setAttribute("client_account_status", "ACTIVE");

                GlobalCC.showPopup("pt1:clientAccountPopup");
            } else {
                GlobalCC.INFORMATIONREPORTING("No client Selected::");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public String actionEditClientAccount() {

        Object key2 = tblClientAccounts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("clientAccountAction", "E");
            txtAccountCode.setValue(nodeBinding.getAttribute("code"));
            txtAccShortDesc.setValue(nodeBinding.getAttribute("shortDesc"));
            txtAccName.setValue(nodeBinding.getAttribute("name"));
            txtAccClientCode.setValue(nodeBinding.getAttribute("clientCode"));
            txtAccCreatedBy.setValue(nodeBinding.getAttribute("createdBy"));
            txtAccDateCreated.setValue(nodeBinding.getAttribute("dateCreated"));
            txtClientAccStatus.setValue(nodeBinding.getAttribute("status"));
            session.setAttribute("client_account_status",
                                 nodeBinding.getAttribute("status"));
            txtAccRemarks.setValue(nodeBinding.getAttribute("remarks"));
            txtAccWef.setValue(nodeBinding.getAttribute("wef"));
            txtAccWet.setValue(nodeBinding.getAttribute("wet"));
            txtBrnDivName.setValue(nodeBinding.getAttribute("divName"));
            txtAccCntName.setValue(nodeBinding.getAttribute("contactName"));
            txtAccCntTitle.setValue(nodeBinding.getAttribute("contactTitle"));
            txtAccCntSmsNo.setValue(nodeBinding.getAttribute("contactSmsNo"));
            txtAccCntEmail.setValue(nodeBinding.getAttribute("contactEmail"));

            btnSaveUpdateClientAccount.setText("Update");
            session.setAttribute("BrnDivCode",
                                 nodeBinding.getAttribute("bdivCode"));

            Authorization auth = new Authorization();
            String process = "AMA";
            String processArea = "AMAC";
            String processSubArea = "AMACDA";
            String AccessGranted =
                auth.checkUserRightsNoError(process, processArea,
                                            processSubArea, null, null);
            if (AccessGranted.equalsIgnoreCase("Y"))
                txtClientAccStatus.setDisabled(false);
            else
                txtClientAccStatus.setDisabled(true);

            GlobalCC.showPopup("pt1:clientAccountPopup");

        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String actionDeleteClientAccount() {
        Object key2 = tblClientAccounts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String accCode = nodeBinding.getAttribute("code").toString();
            session.setAttribute("ClientAccountCode", accCode);

            String msg =
                "Are you sure you wish to DELETE Client Account No " + session.getAttribute("ClientAccountCode").toString();
            confirmDeleteAccMsg.setValue(msg);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteClientAcc" +
                                 "').show(hints);");


        }


        else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;
        }
        return null;
    }

    public void branchDivSelected(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            Object key2 = brnDivLov.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                txtBrnDivName.setValue(nodeBinding.getAttribute("DIV_NAME"));
                GlobalCC.refreshUI(txtBrnDivName);
                session.setAttribute("BrnDivCode",
                                     nodeBinding.getAttribute("DIV_CODE"));
            }
        }
    }

    public String actionShowadminRegions() {
        if (txtCountryCode.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("You need to select a Country first to proceed.");
            return null;
        } else {

            // Set the country code to be used to fetch the states
            session.setAttribute("countryCode", txtCountryCode.getValue());

            ADFUtils.findIterator("fetchStatesByCountry1Iterator").executeQuery();
            GlobalCC.refreshUI(tblStates);
            ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
            GlobalCC.refreshUI(prefixTbl);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:adminRegionsPop" + "').show(hints);");
        }
        return null;
    }


    public String actionAcceptEmpAdminRegion() {
        Object key2 = tblEmpStates.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            clntEmplState.setValue(nodeBinding.getAttribute("stateName"));
            clntEmplStateCode.setValue(nodeBinding.getAttribute("stateCode"));
            clntEmplTwnName.setValue(null);
            clntEmplTwnCode.setValue(null);
            // clntEmplTwnPostalCode.setValue(null);
            clntEmplTwnPostalZipCode.setValue(null);

            GlobalCC.refreshUI(clntEmplState);
            GlobalCC.refreshUI(clntEmplStateCode);
            GlobalCC.refreshUI(clntEmplTwnName);
            GlobalCC.refreshUI(clntEmplTwnCode);
            // GlobalCC.refreshUI(clntEmplTwnPostalCode);
            GlobalCC.refreshUI(clntEmplTwnPostalZipCode);
        }

        GlobalCC.hidePopup("pt1:adminEmpRegionsPop");
        return null;
    }

    public String actionAcceptAdminRegion() {
        Object key2 = tblStates.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtAdminRegionCode.setValue(nodeBinding.getAttribute("stateCode"));
            txtAdminRegionName.setValue(nodeBinding.getAttribute("stateName"));
            txtTownName.setValue(null);
            txtTownCode.setValue(null);
            txtZipCode.setValue(null);

            GlobalCC.refreshUI(panelCreateClient);
        }
        GlobalCC.dismissPopUp("pt1", "adminRegionsPop");
        return null;
    }

    public void setClientTitlePan(RichPanelLabelAndMessage clientTitlePan) {
        this.clientTitlePan = clientTitlePan;
    }

    public RichPanelLabelAndMessage getClientTitlePan() {
        return clientTitlePan;
    }

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

    public void deleteClientAccountsListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {

            String accCode =
                session.getAttribute("ClientAccountCode").toString();

            String Query =
                "DELETE FROM tqc_client_accounts WHERE clna_code = ? ";
            DBConnector connector = new DBConnector();
            OracleCallableStatement cst = null;
            OracleConnection conn = null;

            try {
                conn = (OracleConnection)connector.getDatabaseConnection();
                cst = (OracleCallableStatement)conn.prepareCall(Query);

                cst.setBigDecimal(1, new BigDecimal(accCode));
                cst.execute();

                // Close the connections
                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchClientAccountsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientAccounts);

                String message = "Client Account DELETED Successfully!";
                showMessagePopup(message);

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
    }
    // Add event code here...


    public void setConfirmDeleteAccMsg(RichOutputLabel confirmDeleteAccMsg) {
        this.confirmDeleteAccMsg = confirmDeleteAccMsg;
    }

    public RichOutputLabel getConfirmDeleteAccMsg() {
        return confirmDeleteAccMsg;
    }

    public void setTbClientListingBasedOnNames(RichTable tbClientListingBasedOnNames) {
        this.tbClientListingBasedOnNames = tbClientListingBasedOnNames;
    }

    public RichTable getTbClientListingBasedOnNames() {
        return tbClientListingBasedOnNames;
    }

    public void confirmSavingOfTheAcc(DialogEvent dialogEvent) {
        // Add event code here...
        session.setAttribute("saveStatus", null);
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
            session.setAttribute("saveStatus", "NO");

        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            session.setAttribute("saveStatus", "YES");
            processCreateUpdateClient();
        }

    }

    public void confirmSaccoContinue(DialogEvent dialogEvent) {
        // Add event code here...
        session.setAttribute("saveSaccoStatus", null);
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:saccoReq" + "').hide(hints);");
            session.setAttribute("saveSaccoStatus", "NO");
            //  processCreateUpdateClient();
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            session.setAttribute("saveSaccoStatus", "YES");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:saccoReq" + "').hide(hints);");
            int counter = 0;
            //count is set on the MainClientDAO
            if (session.getAttribute("count") != null) {

                String noOfRecords = session.getAttribute("count").toString();

                counter = Integer.parseInt(noOfRecords);

                if (counter >= 1) {

                    erkService =
                            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                               ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:p2" + "').show(hints);");


                } else {
                    processCreateUpdateClient();

                }
            } else {
                processCreateUpdateClient();
            }

        }

    }

    public void setConfirmSaveDialog(RichDialog confirmSaveDialog) {
        this.confirmSaveDialog = confirmSaveDialog;
    }

    public RichDialog getConfirmSaveDialog() {
        return confirmSaveDialog;
    }

    public void confirmSaveAgencyDialogListener(DialogEvent dialogEvent) {
        // Add event code here...
    }

    /*
    public String actionRejectClient() {

        Authorization auth = new Authorization();
        String process = "CLAU";
        String processArea = "CLAU";
        String processSubArea = "CLAU";
        String AccessGranted =
            auth.checkUserRights(process, processArea, processSubArea, null,
                                 null);
        if (!AccessGranted.equalsIgnoreCase("Y")) {
            GlobalCC.INFORMATIONREPORTING("Sorry. You Do not Have The Rights to reject a client.");
            return null;

        }
        if (session.getAttribute("ClientCode") != null) {

            if (checkIfUserAlowedToAuthorize()) {
                String status = check_ifclientActive();
                if (status != null) {
                    if (status.equalsIgnoreCase("D")) {


                        String Query =
                            "BEGIN TQC_CLIENTS_PKG.authorise_client(?,?,?);END;";
                        DBConnector connector = new DBConnector();
                        OracleCallableStatement cst = null;
                        OracleConnection conn = null;


                        try {

                            conn =
(OracleConnection)connector.getDatabaseConnection();
                            cst =
 (OracleCallableStatement)conn.prepareCall(Query);

                            cst.registerOutParameter(1, OracleTypes.VARCHAR);
                            // Add or Edit
                            cst.setBigDecimal(2,
                                              new BigDecimal(session.getAttribute("ClientCode").toString())); // Client Code
                            cst.setString(3, "R");
                            cst.execute();
                            String err = cst.getString(1);
                            if (err != null) {
                                GlobalCC.INFORMATIONREPORTING(err);
                            } else {

                                String message =
                                    "Client Rejected Successfully!";
                                txtStatus.setValue("R");
                                selectStatusReady.setDisabled(false);
                                selectStatusDraft.setDisabled(true);
                                GlobalCC.refreshUI(txtStatus);
                                GlobalCC.INFORMATIONREPORTING(message);
                                //showMessagePopup(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            GlobalCC.EXCEPTIONREPORTING(conn, e);

                        }
                    } else {
                        {
                            if (status.equalsIgnoreCase("A")) {
                                GlobalCC.INFORMATIONREPORTING("The Client Status is Already Authorised:: You can only set it to InActive ");
                                return null;
                            } else if (status.equalsIgnoreCase("I")) {
                                GlobalCC.INFORMATIONREPORTING("The Client Status is Inactive::You can only set it to Active");
                                return null;
                            }


                            else if (status.equalsIgnoreCase("R")) {
                                GlobalCC.INFORMATIONREPORTING("The Client status is Already Rejected::");
                                return null;
                            }


                        }
                    }


                }

                else {

                    return null;
                }


            } else {
                GlobalCC.INFORMATIONREPORTING("You don't have permission to Authorise");
                return null;
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("Client Code Required::");
            return null;
        }

        return null;
    }*/

    public String actionBackClient() {
        try {
            String back_url =
                GlobalCC.checkNullValues(session.getAttribute("back_url"));
            if (back_url != null) {
                if (!back_url.contains("sn=")) {
                    back_url = back_url + "&sn=" + renderer.getSessionUrl();
                }
                // System.out.println("back_url: " + back_url);
                Links.navigate(back_url);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }
    //end if

    public BigDecimal getClientCode() {

        return GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));


    }


    public void setBtnAuthorizeClient(RichCommandButton btnAuthorizeClient) {
        this.btnAuthorizeClient = btnAuthorizeClient;
    }

    public RichCommandButton getBtnAuthorizeClient() {
        return btnAuthorizeClient;
    }

    public void setSelectStatusDraft(RichSelectItem selectStatusDraft) {
        this.selectStatusDraft = selectStatusDraft;
    }

    public RichSelectItem getSelectStatusDraft() {
        return selectStatusDraft;
    }

    public void setSelectStatusInactive(RichSelectItem selectStatusInactive) {
        this.selectStatusInactive = selectStatusInactive;
    }

    public RichSelectItem getSelectStatusInactive() {
        return selectStatusInactive;
    }

    public void setSelectStatusReady(RichSelectItem selectStatusReady) {
        this.selectStatusReady = selectStatusReady;
    }

    public RichSelectItem getSelectStatusReady() {
        return selectStatusReady;
    }

    public void setSelectStatusActive(RichSelectItem selectStatusActive) {
        this.selectStatusActive = selectStatusActive;
    }

    public RichSelectItem getSelectStatusActive() {
        return selectStatusActive;
    }

    public boolean checkIfUserAlowedToAuthorize() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        OracleConnection connection = null;
        OracleCallableStatement stmt = null;
        OracleResultSet rs = null;
        String status = null;

        try {
            connection = (OracleConnection)dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_INTERFACES_PKG.check_user_rights(?,?,?,?,?,?,?); end;";
            stmt = (OracleCallableStatement)connection.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.VARCHAR);
            stmt.setString(2, session.getAttribute("Username").toString());
            stmt.setBigDecimal(3, new BigDecimal(0));
            stmt.setString(4, "CLCR");
            stmt.setString(5, "CLAU");
            stmt.setString(6, "CLAU");
            stmt.setBigDecimal(7, null);
            stmt.setString(8, null);

            stmt.execute();

            status = stmt.getString(1);


            if (status.equalsIgnoreCase("Y")) {
                return true;
            } else if (status.equalsIgnoreCase("N")) {
                return false;
            }
            rs.close();

            stmt.close();
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);


        }
        return false;
    }

    public String actionSearchClient() {
        try {
            if (session.getAttribute("grpCode") != null) {
                session.setAttribute("grpCode", null);

                session.setAttribute("_search", "false");
                List<UIComponent> child = panelCollSearch.getChildren();
                UIComponent comp = child.get(0);
                for (int i = 0; i < child.size(); i++) {
                    comp = child.get(i);

                    if (comp.getId().equalsIgnoreCase("t11")) {

                    }
                }

                List<UIComponent> children = SEARCHHOLDER.getChildren();
                UIComponent component = children.get(0);

                for (int i = 0; i < children.size(); i++) {
                    component = children.get(i);

                    if (component.getId().equalsIgnoreCase("it19") ||
                        component.getId().equalsIgnoreCase("it20") ||
                        component.getId().equalsIgnoreCase("it21") ||
                        component.getId().equalsIgnoreCase("it18") ||
                        component.getId().equalsIgnoreCase("it25") ||
                        component.getId().equalsIgnoreCase("it26")) {
                        RichInputText rpt = (RichInputText)component;
                        rpt.setValue(null);

                        GlobalCC.refreshUI(rpt);
                    }
                    if (component.getId().equalsIgnoreCase("id4") ||
                        component.getId().equalsIgnoreCase("id5")) {


                        RichInputDate rsc = (RichInputDate)component;

                        rsc.setValue(null);
                        GlobalCC.refreshUI(rsc);
                    }


                }
                children = statusHolder.getChildren();
                component = children.get(0);

                for (int i = 0; i < children.size(); i++) {
                    component = children.get(i);

                    if (component.getId().equalsIgnoreCase("soc2")) {


                        RichSelectOneChoice rsc =
                            (RichSelectOneChoice)component;

                        rsc.setValue(null);
                        GlobalCC.refreshUI(rsc);
                    }


                }
                children = searchClientType.getChildren();
                component = children.get(0);

                for (int i = 0; i < children.size(); i++) {
                    component = children.get(i);

                    if (component.getId().equalsIgnoreCase("soc1")) {


                        RichSelectOneChoice rsc =
                            (RichSelectOneChoice)component;

                        rsc.setValue(null);
                        GlobalCC.refreshUI(rsc);
                    }


                }
                session.setAttribute("searchCriteria", null);
            }

            ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:searchClientPop" + "').show(hints);");

            // session.setAttribute("myTrigger", "myTrigger");
            //ADFUtils.findIterator("fetchAllClientsByTriggerIterator").executeQuery();
            //tblClients.setRendered(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setBtnRejectClient(RichCommandButton btnRejectClient) {
        this.btnRejectClient = btnRejectClient;
    }

    public RichCommandButton getBtnRejectClient() {
        return btnRejectClient;
    }

    private UploadedFile UploadedImageFile;

    public void setUploadedImageFile(UploadedFile UploadedImageFile) {
        this.UploadedImageFile = UploadedImageFile;

        InputStream Reader;
        try {
            long Val = UploadedImageFile.getLength();
            Reader = UploadedImageFile.getInputStream();
            byte[] ImageBytes = new byte[Reader.available()];
            //int BytesLength = ImageBytes.length;

            InsertClientImage(Reader, Val);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UploadedFile getUploadedImageFile() {
        return UploadedImageFile;
    }

    public void ImageUploadedListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...

        if (session.getAttribute("ClientCode") != null) {

            System.out.println(" CLient code == " +
                               session.getAttribute("ClientCode"));
            if (valueChangeEvent.getNewValue() !=
                valueChangeEvent.getOldValue()) {
                UploadedFile _file =
                    (UploadedFile)valueChangeEvent.getNewValue();
                InputStream Reader;
                this.UploadedImageFile = _file;
                try {
                    long Val = UploadedImageFile.getLength();
                    Reader = UploadedImageFile.getInputStream();
                    byte[] ImageBytes = new byte[Reader.available()];
                    //int BytesLength = ImageBytes.length;
                    Boolean isOk =
                        GlobalCC.validateUploadedImg(UploadedImageFile,
                                                     "Client Image");
                    System.out.println("isOk " + isOk);
                    if (isOk == true) {
                        InsertClientImage(Reader, Val);
                        clientPhoto.setSource("/clientimagesservlet?id=" +
                                              session.getAttribute("ClientCode"));


                        GlobalCC.refreshUI(clientPhoto);

                    } else {
                        uploadedPicture.setValue(null);

                        GlobalCC.refreshUI(uploadedPicture);


                    }


                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(null, e);
                }
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("Client code is required to upload image ");
        }
    }

    public void ImageSignatureUploadedListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            if (session.getAttribute("ClientCode") != null) {

                InputStream Reader;
                this.UploadedImageFile = _file;
                try {
                    long Val = UploadedImageFile.getLength();
                    Reader = UploadedImageFile.getInputStream();
                    byte[] ImageBytes = new byte[Reader.available()];
                    //int BytesLength = ImageBytes.length;
                    Boolean isOk =
                        GlobalCC.validateUploadedImg(UploadedImageFile,
                                                     "Client Image");
                    if (isOk == true) {
                        InsertSignatureImage(Reader, Val);

                        clientSignature.setSource("/clientsignatureservlet?id=" +
                                                  session.getAttribute("ClientCode"));


                        GlobalCC.refreshUI(clientSignature);
                    } else {
                        uploadSignature.setValue(null);
                        GlobalCC.refreshUI(uploadSignature);
                    }

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(null, e);
                }
            } else {
                GlobalCC.INFORMATIONREPORTING("Client code is required to upload image ");
            }
        }

    }

    public void setClientImage(RichImage clientImage) {
        this.clientImage = clientImage;
    }

    public RichImage getClientImage() {
        return clientImage;
    }

    public void InsertClientImage(InputStream Image, long BytesLength) {
        OracleConnection conn = null;
        try {

            DBConnector connector = new DBConnector();
            conn = connector.getDatabaseConnection();
            String systemsQuery =
                "BEGIN TQC_CLIENTS_PKG.update_image(?,?);END;";
            CallableStatement cst = null;
            cst = conn.prepareCall(systemsQuery);
            cst.setBlob(1, Image, BytesLength);
            cst.setBigDecimal(2,
                              new BigDecimal(session.getAttribute("ClientCode").toString()));

            cst.execute();


            GlobalCC.refreshUI(uploadedPicture);

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
    }

    public void InsertSignatureImage(InputStream Image, long BytesLength) {
        DBConnector connector = new DBConnector();
        OracleConnection conn = connector.getDatabaseConnection();
        try {

            String systemsQuery =
                "BEGIN TQC_CLIENTS_PKG.update_signature(?,?);END;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(systemsQuery);
            cst.setBlob(1, Image, BytesLength);
            cst.setBigDecimal(2,
                              new BigDecimal(session.getAttribute("ClientCode").toString()));

            cst.execute();

            //ADFUtils.findIterator("findAllQuotationDetailsIterator").executeQuery();
            // GlobalCC.refreshUI(clientImage);

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
    }

    public void setUploadComponent(RichInputFile uploadComponent) {
        this.uploadComponent = uploadComponent;
    }

    public RichInputFile getUploadComponent() {
        return uploadComponent;
    }

    public void setUploadPhoto(RichInputFile uploadPhoto) {
        this.uploadPhoto = uploadPhoto;
    }

    public RichInputFile getUploadPhoto() {
        return uploadPhoto;
    }

    public void setClientSignature(RichImage clientSignature) {
        this.clientSignature = clientSignature;
    }

    public RichImage getClientSignature() {
        return clientSignature;
    }

    public void setClientPhoto(RichImage clientPhoto) {
        this.clientPhoto = clientPhoto;
    }

    public RichImage getClientPhoto() {
        return clientPhoto;
    }

    public void setUploadedPicture(RichInputFile uploadedPicture) {
        this.uploadedPicture = uploadedPicture;
    }

    public RichInputFile getUploadedPicture() {
        return uploadedPicture;
    }

    public void setUploadSignature(RichInputFile uploadSignature) {
        this.uploadSignature = uploadSignature;
    }

    public RichInputFile getUploadSignature() {
        return uploadSignature;
    }

    public void setTxtWebsite(RichInputText txtWebsite) {
        this.txtWebsite = txtWebsite;
    }

    public RichInputText getTxtWebsite() {
        return txtWebsite;
    }

    public void setTxtAuditors(RichInputText txtAuditors) {
        this.txtAuditors = txtAuditors;
    }

    public RichInputText getTxtAuditors() {
        return txtAuditors;
    }

    public void setTxtDLNo(RichInputText txtDLNo) {
        this.txtDLNo = txtDLNo;
    }

    public RichInputText getTxtDLNo() {
        return txtDLNo;
    }

    public void setTxtDateOfEmployment(RichInputDate txtDateOfEmployment) {
        this.txtDateOfEmployment = txtDateOfEmployment;
    }

    public RichInputDate getTxtDateOfEmployment() {
        return txtDateOfEmployment;
    }

    public void setTxtInsurer(RichInputText txtInsurer) {
        this.txtInsurer = txtInsurer;
    }

    public RichInputText getTxtInsurer() {
        return txtInsurer;
    }

    public void setTxtParentCompany(RichInputText txtParentCompany) {
        this.txtParentCompany = txtParentCompany;
    }

    public RichInputText getTxtParentCompany() {
        return txtParentCompany;
    }

    public void setTxtProjectedBusiness(RichInputText txtProjectedBusiness) {
        this.txtProjectedBusiness = txtProjectedBusiness;
    }

    public RichInputText getTxtProjectedBusiness() {
        return txtProjectedBusiness;
    }

    public void setAddAgencyDirectors(RichCommandButton addAgencyDirectors) {
        this.addAgencyDirectors = addAgencyDirectors;
    }

    public RichCommandButton getAddAgencyDirectors() {
        return addAgencyDirectors;
    }

    public void setEditAgencyDirectors(RichCommandButton editAgencyDirectors) {
        this.editAgencyDirectors = editAgencyDirectors;
    }

    public RichCommandButton getEditAgencyDirectors() {
        return editAgencyDirectors;
    }

    public void setDeleteAgencyDirectors(RichCommandButton deleteAgencyDirectors) {
        this.deleteAgencyDirectors = deleteAgencyDirectors;
    }

    public RichCommandButton getDeleteAgencyDirectors() {
        return deleteAgencyDirectors;
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

    public void setSaveAgDirectorsAction(RichCommandButton saveAgDirectorsAction) {
        this.saveAgDirectorsAction = saveAgDirectorsAction;
    }

    public RichCommandButton getSaveAgDirectorsAction() {
        return saveAgDirectorsAction;
    }


    public void setTabDirectors(RichShowDetailItem tabDirectors) {
        this.tabDirectors = tabDirectors;
    }

    public RichShowDetailItem getTabDirectors() {
        return tabDirectors;
    }

    public String addAgencyDirectors() {
        if (session.getAttribute("ClientCode") != null) {
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
            saveAgDirectorButton.setText("Save");
        } else {
            GlobalCC.INFORMATIONREPORTING("CLIENT CODE REQUIRED");
            return null;
        }
        return null;
    }

    public String saveAgDirectorsAction() {

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_CLIENTS_PKG.TQC_CLIENT_DIRECTORS_PRC(?,?,?,?,?,?,?,?,?);end;";
        ARRAY array = null;
        OracleConnection conn = null;
        OracleCallableStatement stmt = null;
        String drYear = null;
        String drName = null;
        String qual = null;
        String share = null;
        drYear = GlobalCC.checkNullValues(directYr.getValue());
        drName = GlobalCC.checkNullValues(directname.getValue());
        qual = GlobalCC.checkNullValues(directQualifications.getValue());
        share = GlobalCC.checkNullValues(directshare.getValue());


        if (drYear == null) {
            GlobalCC.errorValueNotEntered("YEAR Required::");
            return null;
        } else if (drName == null) {
            GlobalCC.errorValueNotEntered("Auditor  Name Required::");
            return null;
        }
        if (saveAgDirectorButton.getText().equals("Save")) {

            BigDecimal yr = null;
            Date year = (Date)directYr.getValue();
            if (year != null && directYr.getValue() != "") {

                yr = new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            }
            try {


                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "A");
                stmt.setBigDecimal(2, null);
                stmt.setBigDecimal(3,
                                   new BigDecimal(session.getAttribute("ClientCode").toString()));
                stmt.setBigDecimal(4, yr);
                stmt.setString(5, (String)drName);
                stmt.setString(6, (String)qual);
                stmt.setBigDecimal(7,
                                   share == null ? null : new BigDecimal(share));
                stmt.setString(8, "");


                stmt.registerOutParameter(9,
                                          oracle.jdbc.internal.OracleTypes.VARCHAR);
                stmt.execute();

                String error = stmt.getString(9);
                if (error == null) {
                    GlobalCC.INFORMATIONREPORTING("RECORD SAVED SUCCESSFULLY");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:agentDirectorsPop" +
                                         "').hide(hints);");


                    stmt.close();
                    conn.close();
                    ADFUtils.findIterator("findClientDirectorsIterator").executeQuery();
                    GlobalCC.refreshUI(agentDirectorsTable);
                    return null;

                } else {
                    GlobalCC.INFORMATIONREPORTING(error);
                    return null;
                }

            } catch (Exception e) {
                TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }

        } else if (saveAgDirectorButton.getText().equals("Update")) {
            BigDecimal yr = null;
            Date year = null;
            if (directYr.getValue() != null) {
                year = (Date)directYr.getValue();
            }

            if (year != null && directYr.getValue() != "") {

                yr = new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            }


            try {
                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "E");

                stmt.setBigDecimal(2,
                                   new BigDecimal(agencyDirectorId.getValue().toString()));
                stmt.setBigDecimal(3,
                                   new BigDecimal(session.getAttribute("ClientCode").toString()));
                stmt.setBigDecimal(4, yr);
                stmt.setString(5, (String)drName);
                stmt.setString(6, (String)qual);
                stmt.setBigDecimal(7,
                                   share == null ? null : new BigDecimal(share));
                stmt.setString(8, (String)qual);


                stmt.registerOutParameter(9,
                                          oracle.jdbc.internal.OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(9);

                if (error == null) {
                    GlobalCC.INFORMATIONREPORTING("RECORD UPDATED SUCCESSFULLY");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:agentDirectorsPop" +
                                         "').hide(hints);");


                    stmt.close();
                    conn.close();
                    ADFUtils.findIterator("findClientDirectorsIterator").executeQuery();
                    GlobalCC.refreshUI(agentDirectorsTable);
                    return null;

                } else {
                    GlobalCC.INFORMATIONREPORTING(error);
                    return null;
                }


            } catch (Exception e) {
                TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }
        }

        return null;
    }

    public String editAgencyDirectors() {

        Object key2 = agentDirectorsTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            agencyDirectorId.setValue(nodeBinding.getAttribute("clntdir_code"));
            //directYr.setValue(nodeBinding.getAttribute("clntdir_year"));
            directname.setValue(nodeBinding.getAttribute("clntdir_name"));
            directQualifications.setValue(nodeBinding.getAttribute("clntdir_qualifications"));
            directshare.setValue(nodeBinding.getAttribute("clntdir_pct_holdg"));

            saveAgDirectorButton.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:agentDirectorsPop" + "').show(hints);");
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
                                 "pt1:confirmDeleteClientDirectors" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public void setDirectname(RichInputText directname) {
        this.directname = directname;
    }

    public RichInputText getDirectname() {
        return directname;
    }

    public void setSaveAgDirectorButton(RichCommandButton saveAgDirectorButton) {
        this.saveAgDirectorButton = saveAgDirectorButton;
    }

    public RichCommandButton getSaveAgDirectorButton() {
        return saveAgDirectorButton;
    }

    public void setAgentDirectorsTable(RichTable agentDirectorsTable) {
        this.agentDirectorsTable = agentDirectorsTable;
    }

    public RichTable getAgentDirectorsTable() {
        return agentDirectorsTable;
    }

    public void setTabAuditors(RichShowDetailItem tabAuditors) {
        this.tabAuditors = tabAuditors;
    }

    public RichShowDetailItem getTabAuditors() {
        return tabAuditors;
    }

    public void setAddAgencyAuditors(RichCommandButton addAgencyAuditors) {
        this.addAgencyAuditors = addAgencyAuditors;
    }

    public RichCommandButton getAddAgencyAuditors() {
        return addAgencyAuditors;
    }

    public String deleteAgencyAuditors() {
        Object key2 = agentAuditorsTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteClientAuditors" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String editAgencyAuditors() {
        Object key2 = agentAuditorsTable.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            agencyAuditorId.setValue(nodeBinding.getAttribute("clntaud_code"));
            // auditYER.setValue(nodeBinding.getAttribute("clntaud_year"));
            auditName.setValue(nodeBinding.getAttribute("clntaud_name"));
            directAuditorQualification.setValue(nodeBinding.getAttribute("clntaud_qualifications"));
            auditorTelephone.setValue(nodeBinding.getAttribute("clntaud_telephone"));

            saveAgAuditorButton.setText("Update");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:agentAuditorsPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;

    }

    public void setEditAgencyAuditors(RichCommandButton editAgencyAuditors) {
        this.editAgencyAuditors = editAgencyAuditors;
    }

    public RichCommandButton getEditAgencyAuditors() {
        return editAgencyAuditors;
    }


    public void setDeleteAgencyAuditors(RichCommandButton deleteAgencyAuditors) {
        this.deleteAgencyAuditors = deleteAgencyAuditors;
    }

    public RichCommandButton getDeleteAgencyAuditors() {
        return deleteAgencyAuditors;
    }

    public void setAgencyAuditorId(RichInputText agencyAuditorId) {
        this.agencyAuditorId = agencyAuditorId;
    }

    public RichInputText getAgencyAuditorId() {
        return agencyAuditorId;
    }

    public String saveAgAuditorAction() {

        DBConnector connection = new DBConnector();
        String query =
            "begin TQC_CLIENTS_PKG.TQC_CLIENT_AUDITORS_PRC(?,?,?,?,?,?,?,?,?);end;";

        OracleConnection conn = null;
        OracleCallableStatement stmt = null;

        String auYear = GlobalCC.checkNullValues(auditYER.getValue());
        String auName = GlobalCC.checkNullValues(auditName.getValue());
        String qual =
            GlobalCC.checkNullValues(directAuditorQualification.getValue());
        String telephone =
            GlobalCC.checkNullValues(auditorTelephone.getValue());


        if (auYear == null) {
            GlobalCC.errorValueNotEntered("YEAR Required::");
            return null;
        } else if (auName == null) {
            GlobalCC.errorValueNotEntered("Auditor  Name Required::");
            return null;
        }
        if (saveAgAuditorButton.getText().equals("Save")) {

            BigDecimal yr = null;
            Date year = null;
            if (auYear != null && auYear != "") {
                year = (Date)auditYER.getValue();
            }

            if (year != null && auditYER.getValue() != "") {

                yr = new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            }


            try {


                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "A");
                stmt.setBigDecimal(2, null);
                stmt.setBigDecimal(3,
                                   new BigDecimal(session.getAttribute("ClientCode").toString()));
                stmt.setBigDecimal(4, yr);
                stmt.setString(5, auName);
                stmt.setString(6, qual);
                stmt.setString(7, telephone);
                stmt.setString(8, "");
                stmt.registerOutParameter(9,
                                          oracle.jdbc.internal.OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(9);

                if (error == null) {
                    GlobalCC.INFORMATIONREPORTING("RECORD SAVED SUCCESSFULLY");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:agentAuditorsPop" +
                                         "').hide(hints);");


                    stmt.close();
                    conn.close();
                    ADFUtils.findIterator("findClientsAuditorsIterator").executeQuery();
                    GlobalCC.refreshUI(agentAuditorsTable);
                    return null;

                } else {
                    GlobalCC.INFORMATIONREPORTING(error);
                    return null;
                }

            } catch (Exception e) {
                TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }

        } else if (saveAgAuditorButton.getText().equals("Update")) {
            BigDecimal yr = null;
            Date year = (Date)auditYER.getValue();
            if (year != null && auditYER.getValue() != "") {

                yr = new BigDecimal(new SimpleDateFormat("yyyy").format(year));
            }
            try {
                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "E");

                stmt.setBigDecimal(2,
                                   new BigDecimal(agencyAuditorId.getValue().toString()));
                stmt.setBigDecimal(3,
                                   new BigDecimal(session.getAttribute("ClientCode").toString()));
                stmt.setBigDecimal(4, yr);
                stmt.setString(5, (String)auditName.getValue());
                stmt.setString(6,
                               (String)directAuditorQualification.getValue());
                stmt.setString(7, (String)auditorTelephone.getValue());
                stmt.setString(8,
                               (String)directAuditorQualification.getValue());


                stmt.registerOutParameter(9,
                                          oracle.jdbc.internal.OracleTypes.VARCHAR);
                stmt.execute();

                String error = stmt.getString(9);

                if (error == null) {
                    GlobalCC.INFORMATIONREPORTING("RECORD UPDTED SUCCESSFULLY");
                    ExtendedRenderKitService erkService =
                        Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                           ExtendedRenderKitService.class);
                    erkService.addScript(FacesContext.getCurrentInstance(),
                                         "var hints = {autodismissNever:false}; " +
                                         "AdfPage.PAGE.findComponent('" +
                                         "pt1:agentAuditorsPop" +
                                         "').hide(hints);");

                    stmt.close();
                    conn.close();
                    ADFUtils.findIterator("findClientsAuditorsIterator").executeQuery();
                    GlobalCC.refreshUI(agentAuditorsTable);

                    return null;

                } else {
                    GlobalCC.INFORMATIONREPORTING(error);
                    return null;
                }

            } catch (Exception e) {
                TurnQuest.view.Base.GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }
        }

        return null;

    }

    public void setSaveAgAuditorButton(RichCommandButton saveAgAuditorButton) {
        this.saveAgAuditorButton = saveAgAuditorButton;
    }

    public RichCommandButton getSaveAgAuditorButton() {
        return saveAgAuditorButton;
    }

    public void setDirectAuditorQualification(RichInputText directAuditorQualification) {
        this.directAuditorQualification = directAuditorQualification;
    }

    public RichInputText getDirectAuditorQualification() {
        return directAuditorQualification;
    }

    public void setAuditName(RichInputText auditName) {
        this.auditName = auditName;
    }

    public RichInputText getAuditName() {
        return auditName;
    }

    public void setAuditYr(RichInputText auditYr) {
        this.auditYr = auditYr;
    }

    public RichInputText getAuditYr() {
        return auditYr;
    }

    public void setAuditYear(RichInputText auditYear) {
        this.auditYear = auditYear;
    }

    public RichInputText getAuditYear() {
        return auditYear;
    }

    public void setAuditYER(RichInputDate auditYER) {
        this.auditYER = auditYER;
    }

    public RichInputDate getAuditYER() {
        return auditYER;
    }

    public void setAuditorTelephone(RichInputText auditorTelephone) {
        this.auditorTelephone = auditorTelephone;
    }

    public RichInputText getAuditorTelephone() {
        return auditorTelephone;
    }

    public void setAgentAuditorsTable(RichTable agentAuditorsTable) {
        this.agentAuditorsTable = agentAuditorsTable;
    }

    public RichTable getAgentAuditorsTable() {
        return agentAuditorsTable;
    }

    public void setAddClientAuditors(RichCommandButton addClientAuditors) {
        this.addClientAuditors = addClientAuditors;
    }

    public RichCommandButton getAddClientAuditors() {
        return addClientAuditors;
    }

    public String addClientAuditors() {

        if (session.getAttribute("ClientCode") != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:agentAuditorsPop" + "').show(hints);");
            auditYER.setValue(null);
            auditName.setValue(null);
            directAuditorQualification.setValue(null);
            auditorTelephone.setValue(null);
            saveAgAuditorButton.setText("Save");
        } else {
            GlobalCC.INFORMATIONREPORTING("CLIENT CODE REQUIRED");
            return null;
        }
        return null;
    }

    public String actionAcceptParentCompany() {
        Object key2 = tableParentComp.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtParentCompany.setValue(nodeBinding.getAttribute("code"));
            txtParentCompanyCode.setValue(nodeBinding.getAttribute("name"));
        }

        GlobalCC.refreshUI(txtParentCompanyCode);
        GlobalCC.dismissPopUp("pt1", "parentCompanyPop");
        return null;

    }

    public String actionAcceptBranch() {
        Object key2 = tblRegBranches.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("branchCode",
                                 nodeBinding.getAttribute("branchCode"));
            txtRegBranchCode.setValue(nodeBinding.getAttribute("branchCode"));
            txtRegBranchName.setValue(nodeBinding.getAttribute("branchName"));


        }

        GlobalCC.refreshUI(txtRegBranchName);
        //        GlobalCC.INFORMATIONREPORTING(nodeBinding.getAttribute("branchCode").toString());

        return null;

    }

    public void tbParentCompanyListener(SelectionEvent selectionEvent) {
        // Add event code here...
    }

    public void setTxtParentCompanyCode(RichInputText txtParentCompanyCode) {
        this.txtParentCompanyCode = txtParentCompanyCode;
    }

    public RichInputText getTxtParentCompanyCode() {
        return txtParentCompanyCode;
    }

    public void setParentcompanyPan(RichPanelLabelAndMessage parentcompanyPan) {
        this.parentcompanyPan = parentcompanyPan;
    }

    public RichPanelLabelAndMessage getParentcompanyPan() {
        return parentcompanyPan;
    }

    public void setTbParentCompanyPop(RichPopup tbParentCompanyPop) {
        this.tbParentCompanyPop = tbParentCompanyPop;
    }

    public RichPopup getTbParentCompanyPop() {
        return tbParentCompanyPop;
    }

    public void setTableParentComp(RichTable tableParentComp) {
        this.tableParentComp = tableParentComp;
    }

    public RichTable getTableParentComp() {
        return tableParentComp;
    }

    public void setTxtProjectedBiz(RichInputNumberSpinbox txtProjectedBiz) {
        this.txtProjectedBiz = txtProjectedBiz;
    }

    public RichInputNumberSpinbox getTxtProjectedBiz() {
        return txtProjectedBiz;
    }

    public void setTxtRegBranchName(RichInputText txtRegBranchName) {
        this.txtRegBranchName = txtRegBranchName;
    }

    public RichInputText getTxtRegBranchName() {
        return txtRegBranchName;
    }

    public void setTxtRegBranchCode(RichInputText txtRegBranchCode) {
        this.txtRegBranchCode = txtRegBranchCode;
    }

    public RichInputText getTxtRegBranchCode() {
        return txtRegBranchCode;
    }

    public void setTblBranches(RichCommandButton tblBranches) {
        this.tblBranches = tblBranches;
    }

    public RichCommandButton getTblBranches() {
        return tblBranches;
    }

    public void setTblRegBranches(RichTable tblRegBranches) {
        this.tblRegBranches = tblRegBranches;
    }

    public RichTable getTblRegBranches() {
        return tblRegBranches;
    }

    public void setBtnAcceptBranch(RichCommandButton btnAcceptBranch) {
        this.btnAcceptBranch = btnAcceptBranch;
    }

    public RichCommandButton getBtnAcceptBranch() {
        return btnAcceptBranch;
    }

    public void setBranchPop(RichPopup branchPop) {
        this.branchPop = branchPop;
    }

    public RichPopup getBranchPop() {
        return branchPop;
    }


    public String actionNewGroup() {
        txtGrpCode.setValue(null);
        txtGrpName.setValue(null);
        txtGrpMinimum.setValue(null);
        txtGrpMax.setValue(null);
        btnsaveGrp.setText("Save");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:addGrpClient" + "').show(hints);");
        return null;
    }

    public String actionEditGroupClient() {

        if (session.getAttribute("grpCode") != null) {

            Object key2 = tblClientGroup.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            txtGrpCode.setValue(null);
            txtGrpName.setValue(null);
            txtGrpMinimum.setValue(null);
            txtGrpMax.setValue(null);

            if (nodeBinding != null) {
                txtGrpCode.setValue(nodeBinding.getAttribute("grp_Code"));
                txtGrpName.setValue(nodeBinding.getAttribute("grp_Name"));
                txtGrpMinimum.setValue(nodeBinding.getAttribute("grp_Minimum"));
                txtGrpMax.setValue(nodeBinding.getAttribute("grp_Maximum"));
                btnsaveGrp.setText("update");

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:addGrpClient" + "').show(hints);");

            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected::");
                return null;
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected::");
            return null;
        }
        return null;
    }

    public void actionConfirmDeleteClntGrp(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            actionDeleteGroupClient();
        }
    }

    public void actionConfirmDeleteClntGrpMember(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_SETUPS_PKG.clientGroupMembers_prc(?,?,?,?); end;";
                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setBigDecimal(2,
                                        new BigDecimal(session.getAttribute("grpdCode").toString()));
                statement.setBigDecimal(3, null);
                statement.setBigDecimal(4, null);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();
                btnRemoveClientFromGroup.setDisabled(true);
                GlobalCC.refreshUI(btnRemoveClientFromGroup);
                GlobalCC.INFORMATIONREPORTING("Client Deleted Successfully");
                // ADFUtils.findIterator("fetchAllClientsIterator").executeQuery();
                // GlobalCC.refreshUI(tblClientPop);
                ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGrpMembers);


            } catch (Exception e) {

                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        }
    }

    public String actionDeleteGroupClient() {
        Object key2 = tblClientGroup.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;


        if (nodeBinding != null) {


            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_SETUPS_PKG.clientGroup_prc(?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal(session.getAttribute("grpCode").toString()));
                cst.setString(3, null);
                cst.setBigDecimal(4, null);
                cst.setBigDecimal(5, null);

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Record Successfully Deleted");
                btnActionDeleteGroupClient.setDisabled(true);
                GlobalCC.refreshUI(btnActionDeleteGroupClient);
                ADFUtils.findIterator("findClientGroupsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGroup);
                ADFUtils.findIterator("findClientGroupMembersIteratorr").executeQuery();
                GlobalCC.refreshUI(tblClientGrpMembers);

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }

        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected::");
            return null;
        }
    }

    public String actionHideUserGroupLov() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:addClientGrpPopt" + "').hide(hints);");
        return null;
    }

    public String actionAddClientToGroup() {
        if (session.getAttribute("grpCode") != null) {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:searchClientPop" + "').show(hints);");
            return null;
        } else {

            GlobalCC.INFORMATIONREPORTING("First select  The group::");

            return null;
        }

    }

    public String actionShowConfirmDelete() {
        if (session.getAttribute("grpCode") != null) {

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:confirmDeleteClientGrp" +
                                 "').show(hints);");
            return null;
        } else {

            GlobalCC.INFORMATIONREPORTING("First select  The group::");

            return null;
        }

    }

    public String actionSaveGroupClient() {

        if (btnsaveGrp.getText().equalsIgnoreCase("save")) {

            String name = GlobalCC.checkNullValues(txtGrpName.getValue());
            String min = GlobalCC.checkNullValues(txtGrpMinimum.getValue());
            String max = GlobalCC.checkNullValues(txtGrpMax.getValue());

            if (name == null) {
                GlobalCC.errorValueNotEntered("Group name required::");
                return null;
            } else if (min == null)

            {
                GlobalCC.errorValueNotEntered("Min number  required::");
                return null;
            } else if (max == null)

            {
                GlobalCC.errorValueNotEntered("Max number required::");
                return null;

            }


            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_SETUPS_PKG.clientGroup_prc(?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, name);
                cst.setBigDecimal(4, new BigDecimal(min));
                cst.setBigDecimal(5, new BigDecimal(max));

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Record Successfully saved");
                ADFUtils.findIterator("findClientGroupsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGroup);
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:addGrpClient" + "').hide(hints);");
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }
        } else {

            String name = GlobalCC.checkNullValues(txtGrpName.getValue());
            String min = GlobalCC.checkNullValues(txtGrpMinimum.getValue());
            String max = GlobalCC.checkNullValues(txtGrpMax.getValue());
            String code = GlobalCC.checkNullValues(txtGrpCode.getValue());
            if (name == null) {
                GlobalCC.errorValueNotEntered("Group name required::");
                return null;
            } else if (min == null)

            {
                GlobalCC.errorValueNotEntered("Min number  required::");
                return null;
            } else if (max == null)

            {
                GlobalCC.errorValueNotEntered("Max number required::");
                return null;

            } else if (code == null)

            {
                GlobalCC.errorValueNotEntered("Grp Code required::");
                return null;

            }


            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            try {
                conn = dbCon.getDatabaseConnection();

                String query =
                    "begin TQC_SETUPS_PKG.clientGroup_prc(?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "E");
                cst.setBigDecimal(2, new BigDecimal(code));
                cst.setString(3, name);
                cst.setBigDecimal(4, new BigDecimal(min));
                cst.setBigDecimal(5, new BigDecimal(max));

                cst.execute();
                cst.close();
                conn.commit();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("Record Successfully Updated");
                btnActionEditGroupClient.setDisabled(false);


                GlobalCC.refreshUI(btnActionEditGroupClient);
                ADFUtils.findIterator("findClientGroupsIterator").executeQuery();
                GlobalCC.refreshUI(tblClientGroup);
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:addGrpClient" + "').hide(hints);");
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                return null;
            }


        }


    }

    public boolean checkIfAnyRowselected() {

        RowKeySet rks = new RowKeySetImpl();


        int rowcount = tblClientPop.getRowCount();

        int count = 0;
        for (int i = 0; i < rowcount; i++) {
            tblClientPop.setRowIndex(i);
            Object key = tblClientPop.getRowKey();
            tblClientPop.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)tblClientPop.getRowData();

            if (nodeBinding.getAttribute("selected").toString().equalsIgnoreCase("true")) {

                count = count + 1;
            }

        }

        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    public String selectAll() {

        RowKeySet rks = new RowKeySetImpl();

        int rowcount = tblClientPop.getRowCount();


        for (int i = 0; i < rowcount; i++) {
            tblClientPop.setRowIndex(i);
            Object key = tblClientPop.getRowKey();
            tblClientPop.setRowKey(key);
            JUCtrlHierNodeBinding nodeBinding =
                (JUCtrlHierNodeBinding)tblClientPop.getRowData();
            /// nodeBinding.setAttribute("selected",true);
            columnSelect.setSelected(true);
            GlobalCC.refreshUI(columnSelect);
        }
        return null;

    }

    public String actionAcceptClientLov() {
        if (checkIfAnyRowselected() == true) {
            Object xy = null;
            RowKeySet rks = new RowKeySetImpl();

            int rowcount = tblClientPop.getRowCount();

            for (int i = 0; i < rowcount; i++) {
                tblClientPop.setRowIndex(i);
                Object key = tblClientPop.getRowKey();
                tblClientPop.setRowKey(key);
                JUCtrlHierNodeBinding nodeBinding =
                    (JUCtrlHierNodeBinding)tblClientPop.getRowData();


                // Object key2 = tblClientPop.getSelectedRowData();
                // JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
                Object selected = nodeBinding.getAttribute("selected");

                if (selected.toString().equalsIgnoreCase("true")) {
                    if (session.getAttribute("grpCode") != null) {

                        DBConnector dbConnector = new DBConnector();
                        OracleConnection conn = null;
                        OracleCallableStatement statement = null;
                        try {
                            conn = dbConnector.getDatabaseConnection();
                            String query =
                                "begin TQC_SETUPS_PKG.clientGroupMembers_prc(?,?,?,?); end;";

                            statement =
                                    (OracleCallableStatement)conn.prepareCall(query);
                            statement.setString(1, "A");
                            statement.setBigDecimal(2, null);
                            statement.setBigDecimal(3,
                                                    new BigDecimal(nodeBinding.getAttribute("code").toString()));
                            statement.setBigDecimal(4,
                                                    new BigDecimal(session.getAttribute("grpCode").toString()));
                            statement.execute();

                            statement.close();
                            conn.commit();
                            conn.close();


                        } catch (Exception e) {
                            e.printStackTrace();
                            GlobalCC.EXCEPTIONREPORTING(conn, e);
                            return null;
                        }


                    } else {
                        GlobalCC.INFORMATIONREPORTING("First Select Client Group ::");
                        return null;
                    }


                }

            }

            ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
            GlobalCC.refreshUI(tblClientGrpMembers);
            ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
            GlobalCC.refreshUI(tblClientPop);
            GlobalCC.dismissPopUp("pt1", "addClientGrpPop");

            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;

        }
    }

    public void actionTblClientGroupListener(SelectionEvent selectionEvent) {
        Object key2 = tblClientGroup.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            session.setAttribute("grpCode",
                                 nodeBinding.getAttribute("grp_Code"));
            btnActionEditGroupClient.setDisabled(false);
            btnActionDeleteGroupClient.setDisabled(false);
            //ADFUtils.findIterator("fetchClientsByCriteriaIterator").executeQuery();
            GlobalCC.refreshUI(btnActionEditGroupClient);
            GlobalCC.refreshUI(btnActionDeleteGroupClient);


            ADFUtils.findIterator("findClientGroupMembersIterator").executeQuery();
            GlobalCC.refreshUI(tblClientGrpMembers);


        }
    }

    public String actionDeleteClntFromGroup() {
        Object key2 = tblClientGrpMembers.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            if (nodeBinding.getAttribute("grpd_Code") != null) {
                session.setAttribute("grpdCode",
                                     nodeBinding.getAttribute("grpd_Code"));

                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "pt1:confirmDeleteClientGrpMember" +
                                     "').show(hints);");


                return null;


            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected::");
                return null;
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected::");
            return null;
        }

    }


    public void actiontblGrpMemberListener(SelectionEvent selectionEvent) {

        Object key2 = tblClientGrpMembers.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {
            session.setAttribute("grpdCode",
                                 nodeBinding.getAttribute("grpd_Code"));
            btnRemoveClientFromGroup.setDisabled(false);

            GlobalCC.refreshUI(btnRemoveClientFromGroup);

        }
    }

    public void setTxtGrpName(RichInputText txtGrpName) {
        this.txtGrpName = txtGrpName;
    }

    public RichInputText getTxtGrpName() {
        return txtGrpName;
    }

    public void setTxtGrpMinimum(RichInputNumberSpinbox txtGrpMinimum) {
        this.txtGrpMinimum = txtGrpMinimum;
    }

    public RichInputNumberSpinbox getTxtGrpMinimum() {
        return txtGrpMinimum;
    }

    public void setTxtGrpMax(RichInputNumberSpinbox txtGrpMax) {
        this.txtGrpMax = txtGrpMax;
    }

    public RichInputNumberSpinbox getTxtGrpMax() {
        return txtGrpMax;
    }


    public void setBtnsaveGrp(RichCommandButton btnsaveGrp) {
        this.btnsaveGrp = btnsaveGrp;
    }

    public RichCommandButton getBtnsaveGrp() {
        return btnsaveGrp;
    }

    public void setTxtGrpCode(RichInputText txtGrpCode) {
        this.txtGrpCode = txtGrpCode;
    }

    public RichInputText getTxtGrpCode() {
        return txtGrpCode;
    }

    public void setTblClientGroup(RichTable tblClientGroup) {
        this.tblClientGroup = tblClientGroup;
    }

    public RichTable getTblClientGroup() {
        return tblClientGroup;
    }

    public void setTblClientGrpMembers(RichTable tblClientGrpMembers) {
        this.tblClientGrpMembers = tblClientGrpMembers;
    }

    public RichTable getTblClientGrpMembers() {
        return tblClientGrpMembers;
    }

    public void setTblClientPop(RichTable tblClientPop) {
        this.tblClientPop = tblClientPop;
    }

    public RichTable getTblClientPop() {
        return tblClientPop;
    }

    public void setBtnRemoveClientFromGroup(RichCommandButton btnRemoveClientFromGroup) {
        this.btnRemoveClientFromGroup = btnRemoveClientFromGroup;
    }

    public RichCommandButton getBtnRemoveClientFromGroup() {
        return btnRemoveClientFromGroup;
    }

    public void setBtnActionEditGroupClient(RichCommandButton btnActionEditGroupClient) {
        this.btnActionEditGroupClient = btnActionEditGroupClient;
    }

    public RichCommandButton getBtnActionEditGroupClient() {
        return btnActionEditGroupClient;
    }

    public void setBtnActionDeleteGroupClient(RichCommandButton btnActionDeleteGroupClient) {
        this.btnActionDeleteGroupClient = btnActionDeleteGroupClient;
    }

    public RichCommandButton getBtnActionDeleteGroupClient() {
        return btnActionDeleteGroupClient;
    }

    public void setSearchCriteria(RichSelectOneChoice searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public RichSelectOneChoice getSearchCriteria() {
        return searchCriteria;
    }

    public void setXtSearchSectorName(RichInputText txtSearchSectorName) {
        this.txtSearchSectorName = txtSearchSectorName;
    }

    public RichInputText getXtSearchSectorName() {
        return txtSearchSectorName;
    }

    public void setColumnSelect(RichSelectBooleanCheckbox columnSelect) {
        this.columnSelect = columnSelect;
    }

    public RichSelectBooleanCheckbox getColumnSelect() {
        return columnSelect;
    }

    public void setTxtSearchSector(RichInputText txtSearchSector) {
        this.txtSearchSector = txtSearchSector;
    }

    public RichInputText getTxtSearchSector() {
        return txtSearchSector;
    }

    public void setTxtSrchSectorName(RichInputText txtSrchSectorName) {
        this.txtSrchSectorName = txtSrchSectorName;
    }

    public RichInputText getTxtSrchSectorName() {
        return txtSrchSectorName;
    }

    public void setTxtAccountOfficerCode(RichInputText txtAccountOfficerCode) {
        this.txtAccountOfficerCode = txtAccountOfficerCode;
    }

    public RichInputText getTxtAccountOfficerCode() {
        return txtAccountOfficerCode;
    }

    public void setTxtAccountOfficerName(RichInputText txtAccountOfficerName) {
        this.txtAccountOfficerName = txtAccountOfficerName;
    }

    public RichInputText getTxtAccountOfficerName() {
        return txtAccountOfficerName;
    }

    public String actionAcceptAccountOfficer() {


        Object key2 = tblAccountOfficer.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {

            session.setAttribute("bactUserCode",
                                 nodeBinding.getAttribute("userCode"));
            txtBACT_ACC_OFFICER.setValue(nodeBinding.getAttribute("username"));
            txtAccountOfficerName.setValue(nodeBinding.getAttribute("username"));
            GlobalCC.refreshUI(txtBACT_ACC_OFFICER);
            GlobalCC.refreshUI(txtAccountOfficerName);
        }
        GlobalCC.dismissPopUp("pt1", "accountOfficersPop");
        return null;
    }


    // Add event code here...


    public void setTblAccountOfficer(RichTable tblAccountOfficer) {
        this.tblAccountOfficer = tblAccountOfficer;
    }

    public RichTable getTblAccountOfficer() {
        return tblAccountOfficer;
    }

    public void setLaySignature(RichPanelGroupLayout laySignature) {
        this.laySignature = laySignature;
    }

    public RichPanelGroupLayout getLaySignature() {
        return laySignature;
    }

    public void setLayClientImage(RichPanelGroupLayout layClientImage) {
        this.layClientImage = layClientImage;
    }

    public RichPanelGroupLayout getLayClientImage() {
        return layClientImage;
    }

    public void setBtnDeleteClient(RichCommandButton btnDeleteClient) {
        this.btnDeleteClient = btnDeleteClient;
    }

    public RichCommandButton getBtnDeleteClient() {
        return btnDeleteClient;
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

    public void setStatusHolder(RichPanelLabelAndMessage statusHolder) {
        this.statusHolder = statusHolder;
    }

    public RichPanelLabelAndMessage getStatusHolder() {
        return statusHolder;
    }

    public void setSearchClientType(RichPanelLabelAndMessage searchClientType) {
        this.searchClientType = searchClientType;
    }

    public RichPanelLabelAndMessage getSearchClientType() {
        return searchClientType;
    }

    public void setTabClientAccounts(RichShowDetailItem tabClientAccounts) {
        this.tabClientAccounts = tabClientAccounts;
    }

    public RichShowDetailItem getTabClientAccounts() {
        return tabClientAccounts;
    }

    public void setClientTab(RichPanelTabbed clientTab) {
        this.clientTab = clientTab;
    }

    public RichPanelTabbed getClientTab() {
        return clientTab;
    }

    public void actionConfirmDeleteDirectors(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = agentDirectorsTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            DBConnector connection = new DBConnector();
            String query =
                "begin TQC_CLIENTS_PKG.TQC_CLIENT_DIRECTORS_PRC(?,?,?,?,?,?,?,?,?);end;";

            OracleConnection conn = null;
            OracleCallableStatement stmt = null;

            try {
                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "D");
                stmt.setBigDecimal(2,
                                   new BigDecimal((String)nodeBinding.getAttribute("clntdir_code").toString()));
                stmt.setBigDecimal(3, null);
                stmt.setBigDecimal(4, null);
                stmt.setString(5, null);
                stmt.setString(6, null);
                stmt.setBigDecimal(7, null);
                stmt.setString(8, null);


                stmt.registerOutParameter(9,
                                          oracle.jdbc.internal.OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(9);


                stmt.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("findClientDirectorsIterator").executeQuery();
                GlobalCC.refreshUI(agentDirectorsTable);

                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);

            }
        }
    }

    public void actionConfirmDeleteAuditor(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
            // Nothing to do.
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = agentAuditorsTable.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
            DBConnector connection = new DBConnector();
            String query =
                "begin TQC_CLIENTS_PKG.TQC_CLIENT_AUDITORS_PRC(?,?,?,?,?,?,?,?,?);end;";
            OracleConnection conn = null;
            OracleCallableStatement stmt = null;
            try {
                conn = (OracleConnection)connection.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query);

                stmt.setString(1, "D");
                stmt.setBigDecimal(2,
                                   new BigDecimal(nodeBinding.getAttribute("clntaud_code").toString()));
                stmt.setBigDecimal(3, null);
                stmt.setBigDecimal(4, null);
                stmt.setString(5, null);
                stmt.setString(6, null);
                stmt.setBigDecimal(7, null);
                stmt.setString(8, null);
                stmt.registerOutParameter(9,
                                          oracle.jdbc.internal.OracleTypes.VARCHAR);
                stmt.execute();
                String error = stmt.getString(9);
                stmt.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("findClientsAuditorsIterator").executeQuery();
                GlobalCC.refreshUI(agentAuditorsTable);
                String message = "Record DELETED Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);

            }
        }
    }

    public void setPanelCollSearch(RichPanelCollection panelCollSearch) {
        this.panelCollSearch = panelCollSearch;
    }

    public RichPanelCollection getPanelCollSearch() {
        return panelCollSearch;
    }

    public void setTxtGender(RichSelectOneChoice txtGender) {
        this.txtGender = txtGender;
    }

    public RichSelectOneChoice getTxtGender() {
        return txtGender;
    }

    public void setPictureFile(UploadedFile pictureFile) {
        if (pictureFile != null) {
            this.filename = pictureFile.getFilename();
            this.filesize = pictureFile.getLength();
            this.fileContent = pictureFile.getContentType();
            try {
                fileStream = pictureFile.getInputStream();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.pictureFile = pictureFile;
    }

    public UploadedFile getPictureFile() {
        return pictureFile;
    }

    public String saveYes() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:p2" +
                             "').hide(hints);");
        processCreateUpdateClient();
        return null;
    }

    public String saveSaccoYes() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:saccoReq" +
                             "').hide(hints);");
        processCreateUpdateClient();
        return null;
    }

    public String saveNo() {
        ADFUtils.findIterator("fetchAllClientsByNamesIterator").executeQuery();
        GlobalCC.refreshUI(tbClientListingBasedOnNames);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:p2" +
                             "').hide(hints);");

        return null;
    }

    public String saveSaccoNo() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:saccoReq" +
                             "').hide(hints);");
        return null;
    }

    public void setInputstream(InputStream inputstream) {
        this.inputstream = inputstream;
    }

    public InputStream getInputstream() {
        return inputstream;
    }

    public void setFileStream(InputStream fileStream) {
        BaseClientBacking.fileStream = fileStream;
    }

    public InputStream getFileStream() {
        return fileStream;
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

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFileStream2(InputStream fileStream2) {
        BaseClientBacking.fileStream2 = fileStream2;
    }

    public InputStream getFileStream2() {
        return fileStream2;
    }

    public void setFilename2(String filename2) {
        this.filename2 = filename2;
    }

    public String getFilename2() {
        return filename2;
    }

    public void setFilesize2(long filesize2) {
        this.filesize2 = filesize2;
    }

    public long getFilesize2() {
        return filesize2;
    }

    public void setFileContent2(String fileContent2) {
        this.fileContent2 = fileContent2;
    }

    public String getFileContent2() {
        return fileContent2;
    }

    public void setFiletype2(String filetype2) {
        this.filetype2 = filetype2;
    }

    public String getFiletype2() {
        return filetype2;
    }

    public void setSignatureFile(UploadedFile signatureFile) {
        if (signatureFile != null) {
            this.filename2 = signatureFile.getFilename();
            this.filesize2 = signatureFile.getLength();
            this.fileContent2 = signatureFile.getContentType();
            try {
                fileStream2 = signatureFile.getInputStream();

            } catch (IOException e) {
            }
        }

        this.signatureFile = signatureFile;
    }

    public UploadedFile getSignatureFile() {
        // signatureFile.
        return signatureFile;
    }

    public void setTxtClientCellNos(RichInputText txtClientCellNos) {
        this.txtClientCellNos = txtClientCellNos;
    }

    public RichInputText getTxtClientCellNos() {
        return txtClientCellNos;
    }

    public void setTxtCltBankTelNo(RichInputText txtCltBankTelNo) {
        this.txtCltBankTelNo = txtCltBankTelNo;
    }

    public RichInputText getTxtCltBankTelNo() {
        return txtCltBankTelNo;
    }

    public void setTxtCltBankCellNo(RichInputText txtCltBankCellNo) {
        this.txtCltBankCellNo = txtCltBankCellNo;
    }

    public RichInputText getTxtCltBankCellNo() {
        return txtCltBankCellNo;
    }

    public void setTxtCltEmployerTelNo(RichInputText txtCltEmployerTelNo) {
        this.txtCltEmployerTelNo = txtCltEmployerTelNo;
    }

    public RichInputText getTxtCltEmployerTelNo() {
        return txtCltEmployerTelNo;
    }

    public void setTxtCltEmployerCellNo(RichInputText txtCltEmployerCellNo) {
        this.txtCltEmployerCellNo = txtCltEmployerCellNo;
    }

    public RichInputText getTxtCltEmployerCellNo() {
        return txtCltEmployerCellNo;
    }

    public void setGrpEmploymentDetails(RichPanelGroupLayout grpEmploymentDetails) {
        this.grpEmploymentDetails = grpEmploymentDetails;
    }

    public RichPanelGroupLayout getGrpEmploymentDetails() {
        return grpEmploymentDetails;
    }


    public void setPgridPersonnalDetails(HtmlPanelGrid pgridPersonnalDetails) {
        this.pgridPersonnalDetails = pgridPersonnalDetails;
    }

    public HtmlPanelGrid getPgridPersonnalDetails() {
        return pgridPersonnalDetails;
    }

    public void setDetailEmploymentDetails(RichShowDetailItem detailEmploymentDetails) {
        this.detailEmploymentDetails = detailEmploymentDetails;
    }

    public RichShowDetailItem getDetailEmploymentDetails() {
        return detailEmploymentDetails;
    }

    public void setTblAdminRegions(RichTable tblAdminRegions) {
        this.tblAdminRegions = tblAdminRegions;
    }

    public RichTable getTblAdminRegions() {
        return tblAdminRegions;
    }

    public void setPanMsgRegionTypeName(RichPanelLabelAndMessage panMsgRegionTypeName) {
        this.panMsgRegionTypeName = panMsgRegionTypeName;
    }

    public RichPanelLabelAndMessage getPanMsgRegionTypeName() {
        return panMsgRegionTypeName;
    }

    public void setDlgAdminRegionTypes(RichDialog dlgAdminRegionTypes) {
        this.dlgAdminRegionTypes = dlgAdminRegionTypes;
    }

    public RichDialog getDlgAdminRegionTypes() {
        return dlgAdminRegionTypes;
    }

    public void setTxtAdminRegionType(RichInputText txtAdminRegionType) {
        this.txtAdminRegionType = txtAdminRegionType;
    }

    public RichInputText getTxtAdminRegionType() {
        return txtAdminRegionType;
    }

    public void setTxtAdminRegionName(RichInputText txtAdminRegionName) {
        this.txtAdminRegionName = txtAdminRegionName;
    }

    public RichInputText getTxtAdminRegionName() {
        return txtAdminRegionName;
    }

    public void setTxtAdminRegionCode(RichInputText txtAdminRegionCode) {
        this.txtAdminRegionCode = txtAdminRegionCode;
    }

    public RichInputText getTxtAdminRegionCode() {
        return txtAdminRegionCode;
    }

    public void setPnMsgTownName(RichPanelLabelAndMessage pnMsgTownName) {
        this.pnMsgTownName = pnMsgTownName;
    }

    public RichPanelLabelAndMessage getPnMsgTownName() {
        return pnMsgTownName;
    }

    public String actionAcceptSystem() {
        // Add event code here...
        return null;
    }

    public String actionCancelSytem() {
        // Add event code here...
        return null;
    }

    public void setDlgNewEditAdminUnit(RichDialog dlgNewEditAdminUnit) {
        this.dlgNewEditAdminUnit = dlgNewEditAdminUnit;
    }

    public RichDialog getDlgNewEditAdminUnit() {
        return dlgNewEditAdminUnit;
    }

    public void setLbMsgState(RichPanelLabelAndMessage lbMsgState) {
        this.lbMsgState = lbMsgState;
    }

    public RichPanelLabelAndMessage getLbMsgState() {
        return lbMsgState;
    }

    public void setTxtAgencyName(RichInputText txtAgencyName) {
        this.txtAgencyName = txtAgencyName;
    }

    public RichInputText getTxtAgencyName() {
        return txtAgencyName;
    }

    public void setTxtAgencyCode(RichInputText txtAgencyCode) {
        this.txtAgencyCode = txtAgencyCode;
    }

    public RichInputText getTxtAgencyCode() {
        return txtAgencyCode;
    }

    public String actionAcceptAgency() {

        Object key = tblAgenciesLOV.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {
            txtAgencyCode.setValue(nodeBinding.getAttribute("agentCode"));
            txtAgencyName.setValue(nodeBinding.getAttribute("name"));
            GlobalCC.refreshUI(txtAgencyName);
        }
        GlobalCC.dismissPopUp("pt1", "AgenciesLOVPOP");
        return null;
    }

    public void setTblAgenciesLOV(RichTable tblAgenciesLOV) {
        this.tblAgenciesLOV = tblAgenciesLOV;
    }

    public RichTable getTblAgenciesLOV() {
        return tblAgenciesLOV;
    }

    private boolean clientIsHuman() {

        DBConnector dbConnector = null;
        OracleCallableStatement stmt = null;
        Connection conn = null;
        OracleResultSet rs = null;
        boolean human = false;
        String vPerson = null;
        try {
            dbConnector = DBConnector.getInstance();
            conn = dbConnector.getDatabaseConnection();
            String query1 =
                "begin ? := TQC_CLIENTS_PKG.get_client_types(?); end;";
            String vLabel =
                GlobalCC.checkNullValues(session.getAttribute("clientTypeLabel"));
            System.out.println("vLabel: " + vLabel);
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setObject(2, session.getAttribute("clientTypeLabel"));
            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);

            if (rs.next()) {
                vPerson =
                        GlobalCC.checkNullValues(rs.getString("CLNTY_PERSON"));
                session.setAttribute("clientPerson", vPerson);


            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }


        return human;

    }

    private void getClientTypeDetails() {
                TurnQuest.view.models.ClientType clienttype = null; 
                Session dbSess = HibernateUtil.getSession();
                Transaction tx = null; 
                 try { 
                         BigDecimal clntyCode =
                         GlobalCC.checkBDNullValues(session.getAttribute("clntyCode"));
                         System.out.println("clntyCode: " + clntyCode);
                         if(clntyCode == null){
                             throw new Exception("Client Type Code Not Defined! Cleanup Needed!");
                         } 
                        tx = dbSess.beginTransaction(); 
                        clienttype = (TurnQuest.view.models.ClientType)dbSess.get(TurnQuest.view.models.ClientType.class, clntyCode);
                        if(clienttype != null){
                            System.out.println("clienttype: "+clienttype);
                            session.setAttribute("clientTypeLabel", clienttype.getClntType());
                            session.setAttribute("typeCat", clienttype.getCategory()); 
                            session.setAttribute("typePerson", clienttype.getPerson());
                            session.setAttribute("typeVAl", clienttype.getType());
                        }  
                        tx.commit();
                 } catch (Exception e) { 
                    GlobalCC.EXCEPTIONREPORTING(e);
                 } finally {
                    dbSess.close(); 
                 }
    }

    private void ClientTypeChanged() {

        Util checkClientAgency = new Util();
        String clientAgencyTyingCheck =
            checkClientAgency.getClientAgencyTying();

        getClientTypeDetails();
        String type =
            GlobalCC.checkNullValues(session.getAttribute("typeVAl"));
        System.out.println("hu type: " + type +
                           session.getAttribute("typePerson"));
        System.out.println("clientTypeLabel: " +
                           session.getAttribute("clientTypeLabel"));


        if (type == null) {
            return;
        }

        //pull label from clientType LOV	

        //        for (SelectItem item : getClientType()) {
        //            if (item != null) {
        //                String v = GlobalCC.checkNullValues(item.getValue());
        //                if (v != null) {
        //                    if (v.equalsIgnoreCase((String)type)) {
        //                        session.setAttribute("clientTypeLabel",
        //                                             item.getLabel());
        //
        //                    }
        //                }
        //            }
        //        }


        try {

            String vPerson =
                GlobalCC.checkNullValues(session.getAttribute("typePerson"));


            boolean human = "Y".equals(vPerson);

            if (human == true) {

                panMsgOccupation.setVisible(true);

                panMsgOccupation.setRendered(true);
                
                if(GlobalCC.getSysParamValue("CLIENT").equalsIgnoreCase("ENSURE")){
                    txtClientMeansOfIdVal.setVisible(true);
                    txtClientMeansOfId.setVisible(true);
                    txtClientMeansOfId.setRendered(true);
                    txtCltOccupation.setVisible(true);
                    txtSpecialization.setVisible(true);
                    txtCltOccupation.setRendered(true);
                    txtClientMeansOfIdVal.setRendered(true);
                
                    txtClientSourceOfIncome.setVisible(true);
                    txtClientSourceOfIncomeDet.setVisible(true);
                    
                    
                    // CRM-2015 Validation on domicile country field
                    session.setAttribute("FX_CLNT_DOMICILE_COUNTRIES.required", "N");
                    // END. CRM-2015
                    
                    
                    // CRM-2016 Validation on Type of business field
                    final String sectorLbl = GlobalCC.getSysParamValue("UNION_INDIV_SECTOR_LABEL");
                    session.setAttribute("FX_CLIENT_SECTOR.label", sectorLbl);
                    // CRM-2016
                }
               

                panMsgSpecialization.setVisible(true);
                sbrIndividual.setSelected(true);
                sbrCorporate.setSelected(false);
                clientTitlePan.setVisible(true);
                GlobalCC.refreshUI(txtCltOccupation);
                GlobalCC.refreshUI(panMsgOccupation);

                boolean visibleLevel =
                    "Y".equals(GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_LEVEL.visible")));
                if (visibleLevel) {
                    txtClientLevel.setVisible(true);
                }
                txtSurname.setLabel("Surname Name: ");

                String label = GlobalCC.getSysParamValue("CLIENT_DOB_LABEL");
                txtDOB.setLabel(label);

                txtOtherNames.setVisible(renderer.isCLIENT_OTHER_NAMES_VISIBLE());
                btnShowContactPersons.setVisible(false);
                txtPhone1.setRendered(false);
                btnShowContactPersons.setDisabled(true);
                txtWebsite.setVisible(false);
                txtAuditors.setVisible(false);
                txtParentCompany.setVisible(false);
                parentcompanyPan.setVisible(false);
                tabDirectors.setVisible(false);
                tabSignatories.setVisible(false);
                
                if(GlobalCC.getSysParamValue("CLIENT").equalsIgnoreCase("ENSURE")){
                clientMeansOfIdPan.setVisible(true);
                txtClientMeansOfIdDateIssued.setVisible(true);
                txtClientMeansOfIdIssuedBy.setVisible(true);
                txtClientMeansOfIdDateExpired.setVisible(true);
                txtClientMeansOfIdIssuingCountry.setVisible(true);
                    txtClientNationalityCode.setVisible(true);
                    txtClientUtilityBill.setVisible(true);
                }
                
                tabAuditors.setVisible(false);
                txtDLNo.setVisible(true);
                txtDrvExperience.setVisible(true);
                txtDlIssueDate.setVisible(true);
                txtDateOfEmployment.setVisible(true);
                layClientImage.setVisible(true);
                txtGender.setVisible(true);
                grpEmploymentDetails.setVisible(true);


                layClientImage.setVisible(true);
                clientTitlePan.setVisible(true);
                txtClientTitle.setVisible(true);
                txtSelectClientTitle.setVisible(true);

                txtCltOccupation.setVisible(true);
                panMsgOccupation.setVisible(true);
                pobClntHolder.setVisible(true);


                txtMaritalStatus.setVisible(true);
                if (clientAgencyTyingCheck.equals("Y")) {


                    pnLabelAgency.setVisible(true);

                    txtAgencyName.setVisible(true);
                    txtAgencies.setVisible(true);
                } else {
                    pnLabelAgency.setVisible(false);

                    txtAgencyName.setVisible(false);
                    txtAgencies.setVisible(false);
                }

                if (renderer.isClientId() == true) {


                    txtId.setDisabled(false);
                    txtId.setRequired(true);
                }
                clientPassportPan.setRendered(true);
                
                // CRM-2012 Place of birth field label to toggle to place of incorporation for corporate clients
                this.txtClientPlaceOfBirth.setVisible(false);
                // END. CRM-2012
                
                // CRM-2017 Incorporation/RC number should be mandatory for corporate clients
                final String regNoRequired = GlobalCC.getSysParamValue("INDIVIDUAL_ID_NO_REQUIRED");
                session.setAttribute("FX_CLIENT_ID_REG_NO.required", regNoRequired);
                // END. CRM-2017

            } else {


                txtSpecialization.setVisible(false);
                panMsgSpecialization.setVisible(false);
                txtClientSourceOfIncomeDet.setVisible(false);

                txtCltOccupation.setVisible(false);
                panMsgOccupation.setVisible(false);
                txtSpecialization.setVisible(false);
                panMsgSpecialization.setVisible(false);

                sbrCorporate.setSelected(true);
                sbrIndividual.setSelected(false);
                grpEmploymentDetails.setVisible(false);
                clientTitlePan.setVisible(false);


                txtClientLevel.setVisible(false);
                txtSurname.setLabel("Corporate Name");
                String label = GlobalCC.getSysParamValue("CLIENT_DOI_LABEL");

                txtDOB.setLabel(label);
                txtOtherNames.setVisible(false);
                layClientImage.setVisible(false);
                txtOtherNames.setValue(null);
                txtWebsite.setVisible(true);
                txtAuditors.setVisible(false);
                txtGender.setVisible(false);
                txtParentCompanyCode.setVisible(true);
                parentcompanyPan.setVisible(true);
                txtDLNo.setVisible(false);
                txtDrvExperience.setVisible(false);
                txtDlIssueDate.setVisible(false);
                txtDateOfEmployment.setVisible(false);
                clientPassportPan.setRendered(false);
                txtPassportNo.setValid(false);
                txtPassportNo.setRequired(false);

                tabAuditors.setVisible(true);
                tabDirectors.setVisible(true);
                tabSignatories.setVisible(true);
                clientMeansOfIdPan.setVisible(false);
                txtClientMeansOfIdDateIssued.setVisible(false);
                txtClientMeansOfIdIssuedBy.setVisible(false);
                txtClientMeansOfIdDateExpired.setVisible(false);
                txtClientMeansOfIdIssuingCountry.setVisible(false);
                txtClientNationalityCode.setVisible(false);
                txtClientUtilityBill.setVisible(false);
                txtPhone1.setRendered(true);

                btnShowContactPersons.setVisible(true);
                btnShowContactPersons.setDisabled(false);
                txtSelectClientTitle.setVisible(false);
                clientTitlePan.setVisible(false);
                txtClientTitle.setVisible(false);
                pnLabelAgency.setVisible(false);
                txtAgencyName.setVisible(false);
                txtAgencies.setVisible(false);
                txtMaritalStatus.setVisible(false);
                txtClientNationalityCode.setRequired(false);
                txtClientNationalityCode.setVisible(false);
                pobClntHolder.setVisible(false);

                // kindldly dont touch this
                // txtIdRegNum.setRequired(true);


                if ("Y".equalsIgnoreCase(GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_MOI.required"))) &&
                    type.contains("C")) {
                    //session.setAttribute("FX_CLIENT_MOI.required","N");
                    //session.setAttribute("FX_CLIENT_MOI.visible","N");

                    txtCltOccupation.setVisible(false);
                    txtClientMeansOfId.setVisible(false);
                    txtCltOccupation.setRendered(false);
                    panMsgOccupation.setRendered(false);
                    txtCltOccupation.setRequired(false);
                    txtClientMeansOfId.setRendered(false);
                    txtClientNationalityCode.setRequired(false);
                    txtClientMeansOfId.setRequired(false);
                    txtClientMeansOfIdVal.setRequired(false);
                }
                
                // CRM-2012 Place of birth field label to toggle to place of incorporation for corporate clients
                this.txtClientPlaceOfBirth.setVisible(true);
                // END. CRM-2012
                
                // CRM-2017 Incorporation/RC number should be mandatory for corporate clients
                final String regNoRequired = GlobalCC.getSysParamValue("CORPORATE_ID_NO_REQUIRED");
                session.setAttribute("FX_CLIENT_ID_REG_NO.required", regNoRequired);
                // END. CRM-2017
                
                if(GlobalCC.getSysParamValue("CLIENT").equalsIgnoreCase("ENSURE")){
                    // CRM-2016 Validation on Type of business field
                    final String sectorLbl = GlobalCC.getSysParamValue("UNION_CORP_SECTOR_LABEL");
                    session.setAttribute("FX_CLIENT_SECTOR.label", sectorLbl);
                    // CRM-2016
                }

            }

            txtStaffNo.setVisible(false);
            txtId.setVisible(true);


            if (type.contains("I")) { //Individual
                txtSurname.setLabel("Surname: ");
            } else if (type.contains("S")) { //Staff
                txtSurname.setLabel("Staff Name: ");
                txtStaffNo.setVisible(true);
            } else if (type.contains("G")) { //Government
                txtSurname.setLabel("Government: ");
            } else if (type.contains("C")) { //Corporate
                txtSurname.setLabel("Corporate Name: ");


            } else if (type.contains("P")) { //Parastatal
                txtSurname.setLabel("Parastatal Name: ");
            } else if (type.contains("B")) { //Small bussiness
                txtSurname.setLabel("Bussiness Name: ");
            } else if (type.contains("T")) { //Institution
                txtSurname.setLabel("Institution Name: ");
            } else if (type.contains("O")) { //SME
                txtSurname.setLabel("Organization Name: ");
            } else if (type.contains("F")) { //SME
                txtId.setVisible(false);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        if (!(type.contains("I"))) {
            session.setAttribute("SMS_REQUIRED", "N");
        }

        GlobalCC.refreshUI(txtSelectClientTitle);
        GlobalCC.refreshUI(sbrIndividual);
        GlobalCC.refreshUI(sbrCorporate);
        GlobalCC.refreshUI(clientTab);
        GlobalCC.refreshUI(clientPassportPan);
        GlobalCC.refreshUI(panelCreateClient);
        GlobalCC.refreshUI(txtClientTypes);
        GlobalCC.refreshUI(txtSms);
        GlobalCC.refreshUI(mainPanel);
        panelCreateClient.setVisible(true);


    }

    public void clientCategoryChanged(ValueChangeEvent valueChangeEvent) {
        String tying;

        Util mand = new Util();
        tying = mand.getClientAgencyTying();
        if (!tying.equals("Y")) {
            return;
        }
        String directClient =
            GlobalCC.checkNullValues(txtDirectClient.getValue());
        if (directClient != null) {
            if (valueChangeEvent.getNewValue() !=
                valueChangeEvent.getOldValue()) {

                if (directClient == null) {
                    pnLabelAgency.setVisible(false);
                    txtAgencyName.setVisible(false);
                    txtAgencies.setVisible(false);
                    pnLabelAgency.setShowRequired(false);
                    labelAndMessage.setShowRequired(true);
                } else {
                    if (directClient.equals("I")) {
                        pnLabelAgency.setVisible(false);
                        txtAgencyName.setVisible(false);
                        txtAgencies.setVisible(false);
                        pnLabelAgency.setShowRequired(false);
                        labelAndMessage.setShowRequired(true);
                    }
                    if (directClient.equals("C")) {
                        pnLabelAgency.setVisible(true);
                        txtAgencyName.setVisible(true);
                        txtAgencies.setVisible(true);
                        pnLabelAgency.setShowRequired(true);
                        labelAndMessage.setShowRequired(false);
                    }
                }
            }
        }
        GlobalCC.refreshUI(pnLabelAgency);
        GlobalCC.refreshUI(txtAgencyName);
        GlobalCC.refreshUI(txtAgencies);
        GlobalCC.refreshUI(labelAndMessage);
    }

    public void setPnLabelAgency(RichPanelLabelAndMessage pnLabelAgency) {
        this.pnLabelAgency = pnLabelAgency;
    }


    public RichPanelLabelAndMessage getPnLabelAgency() {
        return pnLabelAgency;
    }

    public void setClntContacts(RichTable clntContacts) {
        this.clntContacts = clntContacts;
    }

    public RichTable getClntContacts() {
        return clntContacts;
    }

    public String newContact() {
        conCode.setValue(null);
        conName.setValue(null);
        conAddress.setValue(null);
        conPhysi.setValue(null);
        conSect.setValue(null);
        conSect.setLabel(null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:clntCont" +
                             "').show(hints);");
        return null;
    }

    public String editContact() {
        Object key2 = clntContacts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            conCode.setValue(nodeBinding.getAttribute("clcoCode"));
            conName.setValue(nodeBinding.getAttribute("clcoName"));
            conAddress.setValue(nodeBinding.getAttribute("clcoPostAdd"));
            conPhysi.setValue(nodeBinding.getAttribute("clcoPhysAdd"));
            conSect.setValue(nodeBinding.getAttribute("secName"));
            conSect.setLabel(nodeBinding.getAttribute("secCode").toString());
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:clntCont" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String deleteContact() {
        Object key2 = clntContacts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            String code = nodeBinding.getAttribute("clcoCode").toString();

            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement statement = null;
            try {
                conn = dbConnector.getDatabaseConnection();
                String query =
                    "begin TQC_CLIENTS_PKG.client_contacts_proc(?,?,?); end;";

                ArrayDescriptor descriptor =
                    ArrayDescriptor.createDescriptor("TQC_CLIENT_CONTACTS_TAB",
                                                     conn);
                ArrayList activityList = new ArrayList();
                UserSystem conType = new UserSystem();
                conType.setSQLTypeName("TQC_CLIENT_CONTACTS_OBJ");

                conType.setClcoCode(new BigDecimal(code));

                activityList.add(conType);
                ARRAY array =
                    new ARRAY(descriptor, conn, activityList.toArray());

                statement = (OracleCallableStatement)conn.prepareCall(query);
                statement.setString(1, "D");
                statement.setArray(2, array);
                statement.setObject(3, null);
                statement.execute();

                statement.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchClientContactsIterator").executeQuery();
                GlobalCC.refreshUI(clntContacts);


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

    public void setConCode(RichInputText conCode) {
        this.conCode = conCode;
    }

    public RichInputText getConCode() {
        return conCode;
    }

    public void setConName(RichInputText conName) {
        this.conName = conName;
    }

    public RichInputText getConName() {
        return conName;
    }

    public void setConAddress(RichInputText conAddress) {
        this.conAddress = conAddress;
    }

    public RichInputText getConAddress() {
        return conAddress;
    }

    public void setConPhysi(RichInputText conPhysi) {
        this.conPhysi = conPhysi;
    }

    public RichInputText getConPhysi() {
        return conPhysi;
    }

    public void setConSect(RichInputText conSect) {
        this.conSect = conSect;
    }

    public RichInputText getConSect() {
        return conSect;
    }

    public String launchSector() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:conSector" +
                             "').show(hints);");
        return null;
    }

    public void setSector(RichTable sector) {
        this.sector = sector;
    }

    public RichTable getSector() {
        return sector;
    }

    public String sectorSelected() {
        Object key2 = sector.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            conSect.setValue(nodeBinding.getAttribute("name"));
            conSect.setLabel(nodeBinding.getAttribute("code").toString());
            GlobalCC.refreshUI(conSect);
            GlobalCC.hidePopup("pt1:conSector");
        }
        return null;
    }

    public String HoldingCompanySelected() {
        Object key2 = holdingCompanyTbl.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtHoldingCompany.setValue(nodeBinding.getAttribute("CLNT_NAME"));
            session.setAttribute("ClnCode",
                                 nodeBinding.getAttribute("CLNT_CODE"));
            GlobalCC.refreshUI(txtHoldingCompany);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:holdingCompanyPop" + "').hide(hints);");

        }
        return null;
    }

    public String saveContact() throws SQLException {

        String conCodeVal = GlobalCC.checkNullValues(conCode.getValue());
        String conNameVal = GlobalCC.checkNullValues(conName.getValue());
        String address = GlobalCC.checkNullValues(conAddress.getValue());
        String phys = GlobalCC.checkNullValues(conPhysi.getValue());
        String sect = GlobalCC.checkNullValues(conSect.getValue());
        if (conNameVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Name");
            return null;
        }
        if (sect != null) {
            sect = conSect.getLabel();
        }

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement statement = null;
        try {
            conn = dbConnector.getDatabaseConnection();
            String query =
                "begin TQC_CLIENTS_PKG.client_contacts_proc(?,?,?); end;";

            ArrayDescriptor descriptor =
                ArrayDescriptor.createDescriptor("TQC_CLIENT_CONTACTS_TAB",
                                                 conn);
            ArrayList activityList = new ArrayList();
            UserSystem conType = new UserSystem();
            conType.setSQLTypeName("TQC_CLIENT_CONTACTS_OBJ");
            if (conCodeVal != null) {
                conType.setClcoCode(new BigDecimal(conCodeVal));

            } else {
                conType.setClcoCode(null);
            }
            conType.setClntCode(new BigDecimal(session.getAttribute("ClientCode").toString()));
            conType.setClcoName(conNameVal);
            conType.setClcoPhysAddress(phys);
            conType.setClcoPostAddress(address);
            if (sect != null) {
                conType.setClcoSecCode(new BigDecimal(sect));
            }
            activityList.add(conType);
            ARRAY array = new ARRAY(descriptor, conn, activityList.toArray());

            statement = (OracleCallableStatement)conn.prepareCall(query);
            if (conCodeVal != null) {
                statement.setString(1, "E");
            } else {
                statement.setString(1, "A");
            }
            BigDecimal contactCode = null;
            statement.setArray(2, array);
            statement.setObject(3, oracle.jdbc.internal.OracleTypes.INTEGER);
            statement.execute();

            statement.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("fetchClientContactsIterator").executeQuery();
            GlobalCC.refreshUI(clntContacts);

            GlobalCC.dismissPopUp("pt1", "clntCont");
            String message = "Record saved Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);

        } catch (SQLException e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }

    public void setLegacyShtDesc(RichInputText legacyShtDesc) {
        this.legacyShtDesc = legacyShtDesc;
    }

    public RichInputText getLegacyShtDesc() {
        return legacyShtDesc;
    }

    public void setTxtZipCode(RichInputText txtZipCode) {
        this.txtZipCode = txtZipCode;
    }

    public RichInputText getTxtZipCode() {
        return txtZipCode;
    }

    public void setTxtAnniversary(RichInputDate txtAnniversary) {
        this.txtAnniversary = txtAnniversary;
    }

    public RichInputDate getTxtAnniversary() {
        return txtAnniversary;
    }

    public void setTxtCreditRting(RichSelectOneChoice txtCreditRting) {
        this.txtCreditRting = txtCreditRting;
    }

    public RichSelectOneChoice getTxtCreditRting() {
        return txtCreditRting;
    }

    public void setTxtHoldingCompany(RichInputText txtHoldingCompany) {
        this.txtHoldingCompany = txtHoldingCompany;
    }

    public RichInputText getTxtHoldingCompany() {
        return txtHoldingCompany;
    }

    public void setHoldingCompanyTbl(RichTable holdingCompanyTbl) {
        this.holdingCompanyTbl = holdingCompanyTbl;
    }

    public RichTable getHoldingCompanyTbl() {
        return holdingCompanyTbl;
    }


    public void setTxtClientCategory(RichSelectOneChoice txtClientCategory) {
        this.txtClientCategory = txtClientCategory;
    }

    public RichSelectOneChoice getTxtClientCategory() {
        return txtClientCategory;
    }

    public void setClientSelected(UISelectItems clientSelected) {
        this.clientSelected = clientSelected;
    }

    public UISelectItems getClientSelected() {
        return clientSelected;
    }

    public void setSelectClientSelected(RichSelectItem selectClientSelected) {
        this.selectClientSelected = selectClientSelected;
    }

    public RichSelectItem getSelectClientSelected() {
        return selectClientSelected;
    }

    public void setClnt_code(BigDecimal clnt_code) {
        this.clnt_code = clnt_code;
    }

    public BigDecimal getClnt_code() {
        return clnt_code;
    }

    public void setTxtClientTypes(RichSelectOneChoice txtClientTypes) {
        this.txtClientTypes = txtClientTypes;
    }

    public RichSelectOneChoice getTxtClientTypes() {
        return txtClientTypes;
    }

    public void setTxtDrvExperience(RichInputText txtDrvExperience) {
        this.txtDrvExperience = txtDrvExperience;
    }

    public RichInputText getTxtDrvExperience() {
        return txtDrvExperience;
    }

    public void selectHoldingCompany(ValueChangeEvent valueChangeEvent) {
        if (txtProposer.getValue().toString().equalsIgnoreCase("N")) {
            session.setAttribute("HoldingCompany", "Y");
            // txtHoldingCompanyCont.setVisible(true);
            txtHoldingCompany.setDisabled(true);
            txtCommandBtn.setDisabled(false);
            GlobalCC.refreshUI(txtHoldingCompany);
            GlobalCC.refreshUI(txtCommandBtn);
        } else if (txtProposer.getValue().toString().equalsIgnoreCase("Y")) {
            session.setAttribute("HoldingCompany", "N");
            //txtHoldingCompanyCont.setVisible(false);
            txtHoldingCompany.setDisabled(true);
            txtCommandBtn.setDisabled(true);
            GlobalCC.refreshUI(txtHoldingCompany);
            GlobalCC.refreshUI(txtCommandBtn);
        }

    }

    public void setTxtHoldingCompanyCont(RichPanelLabelAndMessage txtHoldingCompanyCont) {
        this.txtHoldingCompanyCont = txtHoldingCompanyCont;
    }

    public RichPanelLabelAndMessage getTxtHoldingCompanyCont() {
        return txtHoldingCompanyCont;
    }

    public void setTxtCommandBtn(RichCommandButton txtCommandBtn) {
        this.txtCommandBtn = txtCommandBtn;
    }

    public RichCommandButton getTxtCommandBtn() {
        return txtCommandBtn;
    }

    public void setTxtSacco(RichSelectOneChoice txtSacco) {
        this.txtSacco = txtSacco;
    }

    public RichSelectOneChoice getTxtSacco() {
        return txtSacco;
    }

    public void saccoSelected(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setTxtSelectClientTitle(RichCommandButton txtSelectClientTitle) {
        this.txtSelectClientTitle = txtSelectClientTitle;
    }

    public RichCommandButton getTxtSelectClientTitle() {
        return txtSelectClientTitle;
    }

    public void setComfirmContinue(RichDialog comfirmContinue) {
        this.comfirmContinue = comfirmContinue;
    }

    public RichDialog getComfirmContinue() {
        return comfirmContinue;
    }

    public void setTxtReasonForUpdate(RichInputText txtReasonForUpdate) {
        this.txtReasonForUpdate = txtReasonForUpdate;
    }

    public RichInputText getTxtReasonForUpdate() {
        return txtReasonForUpdate;
    }

    public void setWebClientName(RichInputText webClientName) {
        this.webClientName = webClientName;
    }

    public RichInputText getWebClientName() {
        return webClientName;
    }

    public void setWebClientShtDesc(RichInputText webClientShtDesc) {
        this.webClientShtDesc = webClientShtDesc;
    }

    public RichInputText getWebClientShtDesc() {
        return webClientShtDesc;
    }

    public void setSaveShtDesc(RichCommandButton saveShtDesc) {
        this.saveShtDesc = saveShtDesc;
    }

    public RichCommandButton getSaveShtDesc() {
        return saveShtDesc;
    }

    public void setAddClientBranches(RichCommandButton addClientBranches) {
        this.addClientBranches = addClientBranches;
    }

    public RichCommandButton getAddClientBranches() {
        return addClientBranches;
    }

    public void setEditClientBranches(RichCommandButton editClientBranches) {
        this.editClientBranches = editClientBranches;
    }

    public RichCommandButton getEditClientBranches() {
        return editClientBranches;
    }

    public void setDeleteClientBranches(RichCommandButton deleteClientBranches) {
        this.deleteClientBranches = deleteClientBranches;
    }

    public RichCommandButton getDeleteClientBranches() {
        return deleteClientBranches;
    }

    public String addClientBrches() {
        session.setAttribute("action", "A");
        webClientName.setValue(null);
        webClientShtDesc.setValue(null);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:webClientPop" + "').show(hints);");
        return null;
    }

    public String editClntBranches() {
        session.setAttribute("action", "E");
        Object key2 = clientBranch.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please Select A Branch to edit");
        }
        session.setAttribute("clientBankCode",
                             r.getAttribute("clientBankCode"));
        webClientName.setValue(r.getAttribute("clientBankName"));
        webClientShtDesc.setValue(r.getAttribute("clientShtDesc"));
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:webClientPop" + "').show(hints);");
        return null;
    }

    public String deleteClntBranches() {
        BigDecimal Code;
        if (session.getAttribute("clientBankCode") != null) {
            Code =
new BigDecimal(session.getAttribute("clientBankCode").toString());
        } else {
            Code = null;
        }

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_SETUPS_PKG.createBankBranches(?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, "D");
            cst.setBigDecimal(2, Code);
            cst.setBigDecimal(3, null);
            cst.setString(4, null);
            cst.setString(5, null);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.INFORMATIONREPORTING("Record Successfully Deleted");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        ADFUtils.findIterator("findWebClientBranchesIterator").executeQuery();
        GlobalCC.refreshUI(clientBranch);
        return null;
    }

    public void setClientBranch(RichTable clientBranch) {
        this.clientBranch = clientBranch;
    }

    public RichTable getClientBranch() {
        return clientBranch;
    }

    public String saveBranchDetails() {
        BigDecimal clientCode = null;
        BigDecimal Code = null;
        String Branchname = null;
        String BranchshtDesc = null;

        clientCode =
                GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));

        if (clientCode == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a client or Create one");
            return null;
        }
        if (session.getAttribute("clientBankCode") != null) {
            Code =
new BigDecimal(session.getAttribute("clientBankCode").toString());
        } else {
            Code = null;
        }
        if (webClientName.getValue() != null) {
            Branchname = webClientName.getValue().toString();
        } else {
            Branchname = null;
        }
        if (webClientShtDesc.getValue() != null) {
            BranchshtDesc = webClientShtDesc.getValue().toString();
        } else {
            BranchshtDesc = null;
        }
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        try {
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_SETUPS_PKG.createBankBranches(?,?,?,?,?); end;";

            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, session.getAttribute("action").toString());
            cst.setBigDecimal(2, Code);
            cst.setBigDecimal(3, clientCode);
            cst.setString(4, BranchshtDesc);
            cst.setString(5, Branchname);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            GlobalCC.INFORMATIONREPORTING("Record Successfully Updated");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:webClientPop" + "').hide(hints);");
        ADFUtils.findIterator("findWebClientBranchesIterator").executeQuery();
        GlobalCC.refreshUI(clientBranch);

        return null;
    }

    public String assignBank() {
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        CallableStatement cst = null;
        int rowCount = unAssignedBank.getRowCount();
        if (rowCount < 1) {
            GlobalCC.INFORMATIONREPORTING("No Bank To Assign");
            return null;
        }

        conn = datahandler.getDatabaseConnection();
        try {
            for (int i = 0; i < rowCount; i++) {
                Boolean Accept = false;
                JUCtrlValueBinding r =
                    (JUCtrlValueBinding)unAssignedBank.getRowData(i);
                Accept = (Boolean)r.getAttribute("select");
                if (r == null) {
                    GlobalCC.INFORMATIONREPORTING("Please select a Bank To Assign");
                    return null;
                }
                if (Accept) {
                    String Query =
                        "begin TQC_SETUPS_PKG.assignBankToClient(?,?,?); end;";

                    datahandler = new DBConnector();
                    conn = datahandler.getDatabaseConnection();
                    cst = conn.prepareCall(Query);
                    cst.setString(1, "A");
                    cst.setBigDecimal(2,
                                      (BigDecimal)session.getAttribute("clientAccCode"));
                    cst.setBigDecimal(3,
                                      (BigDecimal)r.getAttribute("CLNT_CODE"));
                    cst.execute();

                }

            }
            ADFUtils.findIterator("findUnassignedCompaniesIterator").executeQuery();
            ADFUtils.findIterator("findassignedCompaniesIterator").executeQuery();
            GlobalCC.refreshUI(unAssignedBank);
            GlobalCC.refreshUI(assignedBank);
            ADFUtils.findIterator("findDefaultBranchLovIterator").executeQuery();
            GlobalCC.refreshUI(defaultBranch);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public void setUnAssignedBank(RichTable unAssignedBank) {
        this.unAssignedBank = unAssignedBank;
    }

    public RichTable getUnAssignedBank() {
        return unAssignedBank;
    }

    public void setUnAssigned(RichSelectBooleanCheckbox unAssigned) {
        this.unAssigned = unAssigned;
    }

    public RichSelectBooleanCheckbox getUnAssigned() {
        return unAssigned;
    }

    public void setAssignedBank(RichTable assignedBank) {
        this.assignedBank = assignedBank;
    }

    public RichTable getAssignedBank() {
        return assignedBank;
    }

    public void selectWebAccount(SelectionEvent selectionEvent) {
        Object keys = tblwebAccounts.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select A web Account");
            return;
        }
        session.setAttribute("clientAccCode", r.getAttribute("clientAccCode"));
        ADFUtils.findIterator("findUnassignedCompaniesIterator").executeQuery();
        GlobalCC.refreshUI(unAssignedBank);
        ADFUtils.findIterator("findassignedCompaniesIterator").executeQuery();
        GlobalCC.refreshUI(assignedBank);
        ADFUtils.findIterator("findDefaultBranchLovIterator").executeQuery();
        GlobalCC.refreshUI(defaultBranch);
    }

    public String unassignBank() {
        DBConnector datahandler = new DBConnector();
        Connection conn = null;
        CallableStatement cst = null;
        int rowCount = assignedBank.getRowCount();
        if (rowCount < 1) {
            GlobalCC.INFORMATIONREPORTING("No Bank To Re-Assign");
            return null;
        }
        conn = datahandler.getDatabaseConnection();
        try {
            for (int i = 0; i < rowCount; i++) {
                Boolean Accept = false;
                JUCtrlValueBinding r =
                    (JUCtrlValueBinding)assignedBank.getRowData(i);
                Accept = (Boolean)r.getAttribute("select");
                if (r == null) {
                    GlobalCC.INFORMATIONREPORTING("Please select a Bank To Assign");
                    return null;
                }
                if (Accept) {
                    String Query =
                        "begin TQC_SETUPS_PKG.assignBankToClient(?,?,?); end;";

                    datahandler = new DBConnector();
                    conn = datahandler.getDatabaseConnection();
                    cst = conn.prepareCall(Query);
                    cst.setString(1, "D");
                    cst.setBigDecimal(2, null);
                    cst.setBigDecimal(3,
                                      (BigDecimal)r.getAttribute("CLNT_CODE"));
                    cst.execute();

                }
            }

            ADFUtils.findIterator("findUnassignedCompaniesIterator").executeQuery();
            ADFUtils.findIterator("findassignedCompaniesIterator").executeQuery();
            GlobalCC.refreshUI(unAssignedBank);
            GlobalCC.refreshUI(assignedBank);
            ADFUtils.findIterator("findDefaultBranchLovIterator").executeQuery();
            GlobalCC.refreshUI(defaultBranch);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String assingAllBanks() {
        int j = 0;
        while (j < unAssignedBank.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)unAssignedBank.getRowData(j);
            r.setAttribute("select", true);
            unAssigned.setSelected(true);
            GlobalCC.refreshUI(unAssigned);
            j++;
        }

        assignBank();
        return null;
    }

    public String unAssignAllBanks() {
        int j = 0;
        while (j < assignedBank.getRowCount()) {
            JUCtrlValueBinding r =
                (JUCtrlValueBinding)assignedBank.getRowData(j);
            r.setAttribute("select", false);
            assignedCheck.setSelected(false);
            GlobalCC.refreshUI(assignedCheck);
            j++;
        }

        unassignBank();
        return null;
    }

    public void setAssignedCheck(RichSelectBooleanCheckbox assignedCheck) {
        this.assignedCheck = assignedCheck;
    }

    public RichSelectBooleanCheckbox getAssignedCheck() {
        return assignedCheck;
    }

    public void setTxtDefaultBranch(RichInputText txtDefaultBranch) {
        this.txtDefaultBranch = txtDefaultBranch;
    }

    public RichInputText getTxtDefaultBranch() {
        return txtDefaultBranch;
    }

    public String addSaveDefaultBranch() {
        BigDecimal branchCode;
        if (session.getAttribute("clientBranchCode") != null) {
            branchCode =
                    new BigDecimal(session.getAttribute("clientBranchCode").toString());
        } else {

            branchCode = null;
        }
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        try {
            conn = dbCon.getDatabaseConnection();

            String query = "begin TQC_SETUPS_PKG.assignDefaultBranch(?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setBigDecimal(1, branchCode);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    public String addClientDetails() {
        Object keys = defaultBranch.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select A Default Branch");
            return null;
        }
        session.setAttribute("clientBranchCode",
                             r.getAttribute("clientBankCode"));
        txtDefaultBranch.setValue(r.getAttribute("clientBankName"));
        GlobalCC.refreshUI(txtDefaultBranch);
        addSaveDefaultBranch();
        GlobalCC.INFORMATIONREPORTING("Default Branch Set Successfully");
        return null;
    }

    public void setDefaultBranch(RichTable defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public RichTable getDefaultBranch() {
        return defaultBranch;
    }

    public String addProductAttribute() {
        session.setAttribute("action", "A");
        txtWebProduct.setValue(null);
        txtUserName.setValue(null);
        txtDebitLimit.setValue(null);
        txtCrLimits.setValue(null);
        txtClientAccount.setValue(null);
        txtPolicyCheck.setSelected(false);
        txtEndorsCheck.setSelected(false);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:productAttributePop" + "').show(hints);");

        return null;
    }

    public String editProductAttribute() {
        session.setAttribute("action", "E");
        Object keys = webProductsDetails.getSelectedRowData();

        JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select A Web product");
            return null;
        }
        session.setAttribute("productCode1", r.getAttribute("productCode1"));
        session.setAttribute("webProductCode", r.getAttribute("productCode1"));
        session.setAttribute("WebuserCode", r.getAttribute("userCode1"));
        session.setAttribute("AccCode", r.getAttribute("clnaCode"));
        txtWebProduct.setValue(r.getAttribute("PRO_DESC"));
        txtUserName.setValue(r.getAttribute("userName1"));
        txtDebitLimit.setValue(r.getAttribute("drLimitAmount"));
        txtCrLimits.setValue(r.getAttribute("crLimitAmount"));
        if (r.getAttribute("polUse") != null) {
            if (r.getAttribute("polUse").equals("Y")) {
                txtPolicyCheck.setSelected(true);
            } else {
                txtPolicyCheck.setSelected(false);
            }
        } else {
            txtPolicyCheck.setSelected(false);
        }
        if (r.getAttribute("endosUse") != null) {
            if (r.getAttribute("endosUse").equals("Y")) {
                txtEndorsCheck.setSelected(true);
            } else {
                txtEndorsCheck.setSelected(false);
            }
        } else {
            txtEndorsCheck.setSelected(false);
        }
        if (r.getAttribute("clnaShtDesc") != null) {
            txtClientAccount.setValue(r.getAttribute("clnaShtDesc"));
        } else {
            txtClientAccount.setValue(null);
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:productAttributePop" + "').show(hints);");

        return null;
    }

    public String deleteProductAttribute() {
        BigDecimal clientCode;
        BigDecimal webProductCode;
        if (session.getAttribute("ClientCode") != null) {
            clientCode =
                    new BigDecimal(session.getAttribute("ClientCode").toString());
        } else {
            clientCode = null;
        }
        if (session.getAttribute("wbProductCode") != null) {
            webProductCode =
                    new BigDecimal(session.getAttribute("wbProductCode").toString());
        } else {
            webProductCode = null;
        }

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_SETUPS_PKG.createWebProductDetails(?,?,?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, "D");
            cst.setBigDecimal(2, clientCode);
            cst.setBigDecimal(3, webProductCode);
            cst.setBigDecimal(4, null);
            cst.setString(5, null);
            cst.setBigDecimal(6, null);
            cst.setBigDecimal(7, null);
            cst.setString(8, null);
            cst.setString(9, null);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findWebProductsDetailsIterator").executeQuery();
            GlobalCC.refreshUI(webProductsDetails);
            GlobalCC.INFORMATIONREPORTING("Record Successfully Deleted");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:productAttributePop" +
                                 "').hide(hints);");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }


    public void setTxtDebitLimit(RichInputNumberSpinbox txtDebitLimit) {
        this.txtDebitLimit = txtDebitLimit;
    }

    public RichInputNumberSpinbox getTxtDebitLimit() {
        return txtDebitLimit;
    }

    public void setTxtCrLimits(RichInputNumberSpinbox txtCrLimits) {
        this.txtCrLimits = txtCrLimits;
    }

    public RichInputNumberSpinbox getTxtCrLimits() {
        return txtCrLimits;
    }

    public void setTxtWebProduct(RichInputText txtWebProduct) {
        this.txtWebProduct = txtWebProduct;
    }

    public RichInputText getTxtWebProduct() {
        return txtWebProduct;
    }

    public void setTxtUserName(RichInputText txtUserName) {
        this.txtUserName = txtUserName;
    }

    public RichInputText getTxtUserName() {
        return txtUserName;
    }

    public String saveProductAttribute() {
        BigDecimal clientCode;
        BigDecimal webProductCode;
        BigDecimal userCode;
        String userName;
        BigDecimal DrLimits;
        BigDecimal CrLimits;
        String PolicyUse;
        String endosUse;
        BigDecimal accountCode;
        if (session.getAttribute("AccCode") != null) {
            accountCode =
                    new BigDecimal(session.getAttribute("AccCode").toString());
        } else {
            accountCode = null;
        }
        if (session.getAttribute("ClientCode") != null) {
            clientCode =
                    new BigDecimal(session.getAttribute("ClientCode").toString());
        } else {
            clientCode = null;
        }
        if (session.getAttribute("webProductCode") != null) {
            webProductCode =
                    new BigDecimal(session.getAttribute("webProductCode").toString());
        } else {
            webProductCode = null;
        }
        if (session.getAttribute("WebuserCode") != null) {
            userCode =
                    new BigDecimal(session.getAttribute("WebuserCode").toString());
        } else {
            userCode = null;
        }
        if (session.getAttribute("WebuserName") != null) {
            userName = session.getAttribute("WebuserName").toString();
        } else {
            userName = null;
        }

        if (txtDebitLimit.getValue() != null) {
            DrLimits = new BigDecimal(txtDebitLimit.getValue().toString());
        } else {
            DrLimits = null;
        }
        if (txtCrLimits.getValue() != null) {
            CrLimits = new BigDecimal(txtCrLimits.getValue().toString());
        } else {
            CrLimits = null;
        }
        if (txtPolicyCheck.isSelected()) {
            PolicyUse = "Y";
        } else {
            PolicyUse = "N";
        }
        if (txtEndorsCheck.isSelected()) {
            endosUse = "Y";
        } else {
            endosUse = "N";
        }
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        try {
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_SETUPS_PKG.createWebProductDetails(?,?,?,?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2, clientCode);
            cst.setBigDecimal(3, webProductCode);
            cst.setBigDecimal(4, userCode);
            cst.setString(5, userName);
            cst.setBigDecimal(6, DrLimits);
            cst.setBigDecimal(7, CrLimits);
            cst.setString(8, PolicyUse);
            cst.setString(9, endosUse);
            cst.setBigDecimal(10, accountCode);
            cst.execute();
            cst.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findWebProductsDetailsIterator").executeQuery();
            GlobalCC.refreshUI(webProductsDetails);
            GlobalCC.INFORMATIONREPORTING("Record Successfully inserted");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:productAttributePop" +
                                 "').hide(hints);");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }
        return null;
    }

    public void setWebProductTbl(RichTable webProductTbl) {
        this.webProductTbl = webProductTbl;
    }

    public RichTable getWebProductTbl() {
        return webProductTbl;
    }

    public String addWebproductDetails() {
        Object keys = webProductTbl.getSelectedRowData();
        JUCtrlValueBinding rows = (JUCtrlValueBinding)keys;
        if (rows == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("webProductCode",
                             rows.getAttribute("webProductCode"));
        txtWebProduct.setValue(rows.getAttribute("productDesc"));
        GlobalCC.refreshUI(txtWebProduct);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:productAttributePop" + "').show(hints);");
        return null;
    }

    public String addWebUser() {
        Object keys = tblUser.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a user");
            return null;
        }
        session.setAttribute("WebuserCode", r.getAttribute("webUserCode"));
        session.setAttribute("WebuserName", r.getAttribute("userName"));
        txtUserName.setValue(r.getAttribute("userName"));
        GlobalCC.refreshUI(txtUserName);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:productAttributePop" + "').show(hints);");
        return null;
    }

    public void setTblUser(RichTable tblUser) {
        this.tblUser = tblUser;
    }

    public RichTable getTblUser() {
        return tblUser;
    }

    public void setTxtPolicyCheck(RichSelectBooleanCheckbox txtPolicyCheck) {
        this.txtPolicyCheck = txtPolicyCheck;
    }

    public RichSelectBooleanCheckbox getTxtPolicyCheck() {
        return txtPolicyCheck;
    }

    public void setTxtEndorsCheck(RichSelectBooleanCheckbox txtEndorsCheck) {
        this.txtEndorsCheck = txtEndorsCheck;
    }

    public RichSelectBooleanCheckbox getTxtEndorsCheck() {
        return txtEndorsCheck;
    }

    public void setWebProductsDetails(RichTable webProductsDetails) {
        this.webProductsDetails = webProductsDetails;
    }

    public RichTable getWebProductsDetails() {
        return webProductsDetails;
    }

    public String selectProductPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:WebproductPop" + "').show(hints);");


        return null;
    }

    public void selectWebProducts(SelectionEvent selectionEvent) {
        Object keys = webProductsDetails.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a user");
            return;
        }
        session.setAttribute("wbProductCode", r.getAttribute("productCode1"));

    }

    public void selectEndorsment(ValueChangeEvent valueChangeEvent) {
        if (txtEndorsCheck.isSelected()) {
            txtPolicyCheck.setDisabled(true);
        } else {
            txtPolicyCheck.setDisabled(false);
        }
        GlobalCC.refreshUI(txtPolicyCheck);
    }

    public void selectPolicyCheck(ValueChangeEvent valueChangeEvent) {
        if (txtPolicyCheck.isSelected()) {
            txtEndorsCheck.setDisabled(true);
        } else {
            txtEndorsCheck.setDisabled(false);
        }
        GlobalCC.refreshUI(txtEndorsCheck);
    }

    public void setTxtAgencies(RichCommandButton txtAgencies) {
        this.txtAgencies = txtAgencies;
    }

    public RichCommandButton getTxtAgencies() {
        return txtAgencies;
    }

    public String checkIfIdIsUnique(String idRegNumber) {
        String message = null;
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        OracleCallableStatement cst = null;
        try {
            conn = dbCon.getDatabaseConnection();

            String query =
                "begin TQC_CLIENTS_PKG.check_if_id_unique(?,?); end;";
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, idRegNumber);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.execute();
            message = cst.getString(2);
            cst.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return message;
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

    public String selectSmsTemplate() {

        return null;
    }

    public String selectEmailTemplate() {
        // Add event code here...
        return null;
    }

    public void setSmsTbl(RichTable smsTbl) {
        this.smsTbl = smsTbl;
    }

    public RichTable getSmsTbl() {
        return smsTbl;
    }

    public void setEmailTbl(RichTable emailTbl) {
        this.emailTbl = emailTbl;
    }

    public RichTable getEmailTbl() {
        return emailTbl;
    }

    public void setTxtPrefix(RichSelectOneChoice txtPrefix) {
        this.txtPrefix = txtPrefix;
    }

    public RichSelectOneChoice getTxtPrefix() {
        return txtPrefix;
    }

    public void setTxtClientAccount(RichInputText txtClientAccount) {
        this.txtClientAccount = txtClientAccount;
    }

    public RichInputText getTxtClientAccount() {
        return txtClientAccount;
    }

    public String addWebClientAccount() {
        // Add event code here...
        return null;
    }

    public void setClientAccountTbl(RichTable clientAccountTbl) {
        this.clientAccountTbl = clientAccountTbl;
    }

    public RichTable getClientAccountTbl() {
        return clientAccountTbl;
    }

    public String addClientAccount() {
        Object key = clientAccountTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        txtClientAccount.setValue(r.getAttribute("clnaShtDesc"));
        session.setAttribute("AccCode", r.getAttribute("clnaCode"));
        GlobalCC.refreshUI(txtClientAccount);
        GlobalCC.refreshUI(txtUserName);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:productAttributePop" + "').show(hints);");
        return null;
    }

    public void setMarketerTbl(RichTable marketerTbl) {
        this.marketerTbl = marketerTbl;
    }

    public RichTable getMarketerTbl() {
        return marketerTbl;
    }

    public String selectMarketer() {
        Object key = marketerTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("MarketeragentCode", r.getAttribute("agentCode"));
        txtAccountManager.setValue(r.getAttribute("agentName"));
        txtAccountManagerCode.setValue(r.getAttribute("agentCode"));
        GlobalCC.refreshUI(txtAccountManager);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:MarketerPop" + "').hide(hints);");

        return null;
    }

    public void setLabelAndMessage(RichPanelLabelAndMessage labelAndMessage) {
        this.labelAndMessage = labelAndMessage;
    }

    public RichPanelLabelAndMessage getLabelAndMessage() {
        return labelAndMessage;
    }

    public void setRelationShipPop(RichCommandButton relationShipPop) {
        this.relationShipPop = relationShipPop;
    }

    public RichCommandButton getRelationShipPop() {
        return relationShipPop;
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

    public String selectPrefix() {
        Object key = prefixTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
            return null;
        }
        session.setAttribute("mptpCode", r.getAttribute("mptpCode"));
        session.setAttribute("mptCode", r.getAttribute("mptCode"));
        txtPrefixManager.setValue(r.getAttribute("prefix"));
        GlobalCC.refreshUI(txtPrefixManager);
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

    public void setPrefixTbl(RichTable prefixTbl) {
        this.prefixTbl = prefixTbl;
    }

    public RichTable getPrefixTbl() {
        return prefixTbl;
    }

    public void selectPrefixFirst(FacesContext facesContext,
                                  UIComponent uIComponent, Object object) {


        String prefix = GlobalCC.checkNullValues(txtPrefixManager.getValue());
        String suffix = GlobalCC.checkNullValues(object);
        if (suffix != null && prefix == null) {
            GlobalCC.errorValueNotEntered("Select Sms 1 Prefix First");
            return;
        }
        String sms_format = GlobalCC.getSysParamValue("SMS_NO_FORMAT");
        if (suffix != null && sms_format != null &&
            !"[NONE]".equals(sms_format)) {
            if (!GlobalCC.validate(suffix, sms_format)) {
                String allowedDigitsCount =
                    getSmsNoCountFromFormat(sms_format);
                String msg =
                    "Sms Suffix is not in Proper Format. Only (" + allowedDigitsCount +
                    ") digits accepted after prefix";
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              msg, null));
            }
        }
    }

    public void selectPrefixFirst2(FacesContext facesContext,
                                   UIComponent uIComponent, Object object) {

        String prefix = GlobalCC.checkNullValues(txtTelPayPrefix.getValue());
        String suffix = GlobalCC.checkNullValues(object);

        if (suffix != null && prefix == null) {
            GlobalCC.errorValueNotEntered("Select Prefix First");
            return;
        }

        String sms_format = GlobalCC.getSysParamValue("SMS_NO_FORMAT");
        if (suffix != null && sms_format != null &&
            !"[NONE]".equals(sms_format)) {
            if (!GlobalCC.validate(suffix, sms_format)) {
                String allowedDigitsCount =
                    getSmsNoCountFromFormat(sms_format);
                String msg =
                    "Sms Suffix is not in Proper Format. Only (" + allowedDigitsCount +
                    ") digits accepted after prefix";
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              msg, null));
            }
        }
    }

    public void validateSms(FacesContext facesContext, UIComponent uIComponent,
                            Object object) {

        if (object != null) {
            String name = object.toString();
            String expression =
                "^0[1-9]{1}[0-9]{8}$"; //[A-Za-z]+[A-Za-z0-9.-]+[A-Za-z]{1,9,1}";
            CharSequence inputStr = name;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            String msg =
                "Please check format.SMS No. Must be at least 10 Numbers and should start with 0(zero)";
            if (!matcher.matches()) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              msg, null));
            }
        }
    }


    public void validateClientCellNos(FacesContext facesContext,
                                      UIComponent uIComponent, Object object) {

        if (object != null) {
            String cellNo = object.toString();
            String expression = getCellNoFormat();

            CharSequence inputStr = cellNo;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            String msg =
                "Please check format.Cell No. Must be at least 10 Numbers and should start with 0(zero)";
            if (!matcher.matches()) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              msg, null));
            }
        }
    }

    public void setPanelBinding(RichPanelFormLayout panelBinding) {
        this.panelBinding = panelBinding;
    }

    public RichPanelFormLayout getPanelBinding() {
        return panelBinding;
    }

    public void selectAnniversary(ActionEvent actionEvent) {
        // Add event code here...
    }

    public void setTxtCreditAllowed(RichSelectOneChoice txtCreditAllowed) {
        this.txtCreditAllowed = txtCreditAllowed;
    }

    public RichSelectOneChoice getTxtCreditAllowed() {
        return txtCreditAllowed;
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

    public void setTxtCreditLimit(RichInputNumberSpinbox txtCreditLimit) {
        this.txtCreditLimit = txtCreditLimit;
    }

    public RichInputNumberSpinbox getTxtCreditLimit() {
        return txtCreditLimit;
    }

    public void setTxtNationality(RichInputText txtNationality) {
        this.txtNationality = txtNationality;
    }

    public RichInputText getTxtNationality() {
        return txtNationality;
    }

    public void setTxtSurburbs(RichInputText txtSurburbs) {
        this.txtSurburbs = txtSurburbs;
    }

    public RichInputText getTxtSurburbs() {
        return txtSurburbs;
    }

    public String actionSelectEmpLocation() {
        ADFUtils.findIterator("fetchPostalCodesByTownIterator").executeQuery();
        GlobalCC.refreshUI(empPostalCodeTable);
        GlobalCC.showPopup("pt1:empPostalCodePOP");
        return null;
    }

    public String actionSelectLocation() {
        ADFUtils.findIterator("fetchPostalCodesByTownIterator").executeQuery();
        GlobalCC.refreshUI(postalCodeTable);
        GlobalCC.showPopup("pt1:postalCodePOP");
        return null;
    }

    public void setTxtSurburbsLbl(RichPanelLabelAndMessage txtSurburbsLbl) {
        this.txtSurburbsLbl = txtSurburbsLbl;
    }

    public RichPanelLabelAndMessage getTxtSurburbsLbl() {
        return txtSurburbsLbl;
    }

    public void setLocationTbl(RichTable locationTbl) {
        this.locationTbl = locationTbl;
    }

    public RichTable getLocationTbl() {
        return locationTbl;
    }

    public String selectLocationDtls() {
        Object key = locationTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        session.setAttribute("locCode", r.getAttribute("locCode"));
        txtSurburbs.setValue(r.getAttribute("locName"));
        GlobalCC.refreshUI(txtSurburbs);
        GlobalCC.hidePopup("pt1:locationPop");
        return null;
    }

    public void validatePin(FacesContext facesContext, UIComponent uIComponent,
                            Object object) {
        if (object != null) {
            //          String pin=txtPIN.getValue().toString();
            //         int val=pin.length();
            //         System.out.print("val"+val);
            //          if(val!=11)
            //          {
            //                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Number does not comform to format",null));
            //              }
            String name = object.toString();
            String expression = "[A-Za-z]+[A-Za-z0-9.-]+[A-Za-z]{1,9,1}";
            CharSequence inputStr = name;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            String msg = "Email is not in Proper Format";
            if (matcher.matches()) {

            } else {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              msg, null));
            }

        }
    }

    public void selectPin(FacesContext facesContext, UIComponent uIComponent,
                          Object object) {
        //                String pin=txtPIN.getValue().toString();
        //               int val=pin.length();
        System.out.print("val 5345345534534");
        //                if(val!=11)
        //                {
        //                      GlobalCC.INFORMATIONREPORTING("Format not ok 23");
        //                      return;
        //                    }
        String name = object.toString();
        String expression = "[A-Za-z]+[A-Za-z0-9.-]+[A-Za-z]{1,9,1}";
        CharSequence inputStr = name;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        String msg = "Email is not in Proper Format";
        if (matcher.matches()) {
            System.out.print("val 5345345534535354");
        } else {
            System.out.print("val 5345345553534535354");
            GlobalCC.INFORMATIONREPORTING("Format not ok");
            return;
        }
    }

    public void setTxtSector(RichInputText txtSector) {
        this.txtSector = txtSector;
    }

    public RichInputText getTxtSector() {
        return txtSector;
    }

    public String getAutoAuthirize() {
        DBConnector myConn = new DBConnector();
        Connection conn = myConn.getDatabaseConnection();
        String delink = "NONE";
        CallableStatement cst = null;
        try {
            cst =
conn.prepareCall("begin ? := TQC_PARAMETERS_PKG.GET_PARAM_VARCHAR(?); end;");
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.setString(2, "AUTHORISE_CLIENT_AUTO");
            cst.execute();
            delink = cst.getString(1);
            if (delink.equals("Y")) {
                txtStatus.setValue("A");
            }

            GlobalCC.refreshUI(txtStatus);
        } catch (SQLException e) {
            //System.out.println("Error.." + e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }

        return delink;
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

    public void enterClientId(ValueChangeEvent valueChangeEvent) {
        fetchWebDetailsById();
    }

    public void enterClientPassport(ValueChangeEvent valueChangeEvent) {
        fetchWebDetailsByPassport();
    }

    public String fetchWebDetailsById() {
        /*if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
            valueChangeEvent.getNewValue().toString() != null) {
        }*/
        session.setAttribute("idRegNumber", txtIdRegNum.getValue());
        IprsSetupsApi iprsApi = new IprsSetupsApi(session);
        String idno = GlobalCC.checkNullValues(txtIdRegNum.getValue());
        if (idno != null && renderer.isIprsApp()) {
            Pattern pattern = Pattern.compile("[0-9]+");
            Matcher matcher = pattern.matcher(idno);
            if (idno.length() >= 7 && matcher.matches()) {

                session.setAttribute("tq_passport", null);
                session.setAttribute("Iprs_Account_Type", "C");
                session.setAttribute("tq_idno", idno);
                iprsApi.clearIprsFields();
                iprsApi.popIprsFields(dlgIprsComp);

                String dob =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_dob"));
                if (dob != null) {
                    java.util.Date date =
                        GlobalCC.getDate(dob, iprsApi.IPRS_JAVA_DATE_FMT);
                    txtDOB.setValue(date);
                    txtDOB.setDisabled(true);
                }

                String surname =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_surname"));
                if (surname != null) {
                    txtSurname.setValue(surname);
                    txtSurname.setDisabled(true);
                }
                String otherNames =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_othernames"));
                String firstName =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_firstname"));

                if (firstName != null) {
                    otherNames =
                            GlobalCC.checkNullValues(firstName + " " + (otherNames !=
                                                                        null ?
                                                                        otherNames :
                                                                        ""));
                }
                if (otherNames != null) {
                    txtOtherNames.setValue(otherNames);
                    txtOtherNames.setDisabled(true);
                }

                String gender =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_gender"));
                if (gender != null) {
                    txtGender.setValue(gender);
                    txtGender.setDisabled(true);
                }
                session.setAttribute("clientIprsValidated", "Y");
                GlobalCC.refreshUI(tabDetailCreateClient);
            }
        }
        return null;
    }

    public String fetchWebDetailsByPassport() {
        IprsSetupsApi iprsApi = new IprsSetupsApi(session);
        String passport = GlobalCC.checkNullValues(txtPassportNo.getValue());
        if (passport != null && renderer.isIprsApp()) {
            passport = passport.toUpperCase();
            Pattern pattern =
                Pattern.compile("[A-Z]{1}[0-9]{6,12}([A-Z]{0,1})");
            Matcher matcher = pattern.matcher(passport);
            if (passport.length() >= 7 && matcher.matches()) {
                session.setAttribute("tq_idno", null);
                session.setAttribute("Iprs_Account_Type", "C");
                session.setAttribute("tq_passport", passport);
                iprsApi.clearIprsFields();
                iprsApi.popIprsFields(dlgIprsComp);
                String dob =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_dob"));
                if (dob != null) {
                    java.util.Date date =
                        GlobalCC.getDate(dob, iprsApi.IPRS_JAVA_DATE_FMT);
                    txtDOB.setValue(date);
                    txtDOB.setDisabled(true);
                }

                String surname =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_surname"));
                if (surname != null) {
                    txtSurname.setValue(surname);
                    txtSurname.setDisabled(true);
                }
                String otherNames =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_othernames"));
                String firstName =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_firstname"));
                if (firstName != null) {
                    otherNames =
                            GlobalCC.checkNullValues(firstName + " " + (otherNames !=
                                                                        null ?
                                                                        otherNames :
                                                                        ""));
                }
                if (otherNames != null) {
                    txtOtherNames.setValue(otherNames);
                    txtOtherNames.setDisabled(true);
                }
                String gender =
                    GlobalCC.checkNullValues(session.getAttribute("iprs_gender"));
                if (gender != null) {
                    txtGender.setValue(gender);
                    txtGender.setDisabled(true);
                }
                GlobalCC.refreshUI(tabDetailCreateClient);
            }
        }
        return null;
    }

    public void enterClientFullName(ValueChangeEvent valueChangeEvent) {
        //if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&
        //valueChangeEvent.getNewValue().toString() != null) {
        try {
            if (!Rendering.isDisabledField("CLIENT_FULLNAME") &&
                Rendering.isVisibleField("CLIENT_FULLNAME")) {
                String fullnames =
                    GlobalCC.checkNullValues(txtFullNames.getValue());
                String surname = null;
                String othernames = null;
                if (fullnames != null && "".equals(fullnames) != true) {
                    int s = fullnames.trim().indexOf(" ");
                    if (s > 0) {
                        surname = fullnames.substring(0, s);
                        othernames =
                                fullnames.substring(s + 1, fullnames.length());
                    }
                }
                System.out.println("surname: " + surname + " othernames: " +
                                   othernames);
                txtSurname.setValue(surname);
                txtOtherNames.setValue(othernames);
                GlobalCC.refreshUI(txtSurname);
                GlobalCC.refreshUI(txtOtherNames);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        // }
    }

    public void enterCustomerCompany(ValueChangeEvent valueChangeEvent) {

        session.setAttribute("CustomerCompany",
                             valueChangeEvent.getNewValue());

    }

    public void enterCMId(ValueChangeEvent valueChangeEvent) {

        String client = GlobalCC.getSysParamValue("CLIENT");

        //if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue() &&  valueChangeEvent.getNewValue().toString() != null)
        {
            if (GlobalCC.getSysParamValue("CHANNEL_MANAGER") != null) {
                if (GlobalCC.getSysParamValue("CHANNEL_MANAGER").equals("Y")) {
                    Map clntDtls = new HashMap();
                    if ("CBA".equalsIgnoreCase(client)) {
                        String customerNo =
                            GlobalCC.checkNullValues(valueChangeEvent.getNewValue());
                        String customerCompany =
                            GlobalCC.checkNullValues(session.getAttribute("CustomerCompany"));
                        if (customerNo != null) {
                            if (customerNo.length() == 6 &&
                                !customerNo.matches("[0-9]{6}")) {
                                GlobalCC.errorValueNotEntered("Invalid Customer Number! Enter a six digit number!");
                                return;
                            }
                            if (customerCompany == null) {
                                GlobalCC.errorValueNotEntered("Enter Customer Company!");
                                return;
                            }
                            if (customerNo.matches("[0-9]{6}")) {
                                CbaCustomerAccountInfo info =
                                    new CbaCustomerAccountInfo();
                                HashMap m =
                                    info.customerInfo(customerCompany, customerNo);
                                if (m.size() > 0) {
                                    try {
                                        legacyShtDesc.setValue(m.get("CLNT_OLD_SHT_DESC"));
                                        String fullnames =
                                            GlobalCC.checkNullValues(m.get("CLNT_FULLNAME"));
                                        String surname = null;
                                        String othernames = null;
                                        if (fullnames != null) {
                                            int s =
                                                fullnames.trim().indexOf(" ");
                                            if (s > 0) {
                                                surname =
                                                        fullnames.substring(0,
                                                                            s);
                                                othernames =
                                                        fullnames.substring(s +
                                                                            1,
                                                                            fullnames.length());


                                            }
                                        }

                                        txtSurname.setValue(surname);
                                        txtOtherNames.setValue(othernames);
                                        txtFullNames.setValue(fullnames);
                                        txtIdRegNum.setValue(m.get("CLNT_ID_REG_NO"));
                                        txtEmail.setValue(m.get("CLNT_EMAIL_ADDRS"));
                                        txtClientTitle.setValue(m.get("CLNT_TITLE"));
                                        txtPhone1.setValue(m.get("CLNT_TEL"));
                                        txtPIN.setValue(m.get("CLNT_PIN"));
                                        txtPassportNo.setValue(m.get("CLNT_PASSPORT_NO"));
                                        txtClientCellNos.setValue(m.get("CLNT_SMS_TEL"));
                                        txtGender.setValue(m.get("CLNT_GENDER"));

                                        String couZipCode =
                                            GlobalCC.checkNullValues(m.get("CLNT_COU_ZIP_CODE"));

                                        String no_format =
                                            "^((\\+" + couZipCode +
                                            ")|0){0,1}\\d{9}$"; //country sms no format
                                        String prefix = null;
                                        String suffix = null;
                                        String sms =
                                            GlobalCC.checkNullValues(m.get("CLNT_SMS_TEL"));
                                        System.out.println("sms: " + sms);

                                        //sms ="+254700-456-789";sms ="0700-456-789";
                                        if (sms != null) {
                                            sms =
sms.replaceAll("\\-", ""); //remove (-)
                                            System.out.println("sms: " + sms);
                                            if (sms.length() >= 9 &&
                                                (sms.matches(no_format) ||
                                                 couZipCode == null)) {
                                                sms =
sms.substring(sms.length() - 9, sms.length());
                                                prefix =
                                                        "0" + sms.substring(0, 3);
                                                suffix = sms.substring(3, 9);
                                            } else {
                                                prefix = null;
                                                suffix = null;
                                            }
                                        }
                                        System.out.println("prefix: " +
                                                           prefix);
                                        System.out.println("suffix: " +
                                                           suffix);

                                        txtPrefixManager.setValue(prefix);
                                        txtSms.setValue(suffix);

                                        txtPostalAddress.setValue(m.get("CLNT_POSTAL_ADDRS"));
                                        txtGender.setValue(m.get("CLNT_GENDER"));
                                        txtDOB.setValue(m.get("CLNT_DOB"));
                                        txtRegBranchCode.setValue(m.get("CLNT_BRN_CODE"));
                                        txtRegBranchName.setValue(m.get("CLNT_BRN_NAME"));
                                        //txtCountryName.setValue(m.get("CLNT_COU_NAME"));
                                        //txtCountryCode.setValue(m.get("CLNT_COU_CODE"));
                                        txtZipCode.setValue(m.get("CLNT_POSTAL_CODE"));
                                        txtSurburbs.setValue(m.get("CLNT_POSTAL_CODE"));
                                        String clientType =
                                            GlobalCC.checkNullValues(m.get("CLNT_TYPE"));
                                        //txtClientTypes.setValue(clientType);
                                        session.setAttribute("typeVAl",
                                                             clientType);
                                        ClientTypeChanged();
                                    } catch (Exception e) {
                                        GlobalCC.EXCEPTIONREPORTING(e);
                                    }
                                    GlobalCC.refreshUI(clientTab);
                                } else {
                                    GlobalCC.errorValueNotEntered("Customer not found!");
                                }
                            }

                        }

                    }
                    if ("KCB".equalsIgnoreCase(client)) {
                        ChannelManager cm = new ChannelManager();
                        clntDtls =
                                cm.getCustomerInfo(valueChangeEvent.getNewValue().toString());
                        System.out.println("fetched_customer_details:" +
                                           clntDtls);
                        if (clntDtls.size() > 0) {
                            try {
                                legacyShtDesc.setValue(clntDtls.get("customerId"));

                                String fullnames =
                                    (String)clntDtls.get("customerName");
                                String surname = null;
                                String othernames = null;
                                if (fullnames != null &&
                                    "".equals(fullnames) != true) {
                                    int s = fullnames.trim().indexOf(" ");
                                    if (s > 0) {
                                        surname = fullnames.substring(0, s);
                                        othernames =
                                                fullnames.substring(s + 1, fullnames.length());
                                    }
                                }

                                txtSurname.setValue(surname);
                                txtOtherNames.setValue(othernames);
                                txtFullNames.setValue(fullnames);

                                txtIdRegNum.setValue(clntDtls.get("customerIdentifDoc"));
                                txtEmail.setValue(clntDtls.get("emailAddress"));
                                txtPhone1.setValue(clntDtls.get("offphone"));
                                txtClientCellNos.setValue(clntDtls.get("phoneNumber"));
                                txtPostalAddress.setValue(clntDtls.get("postAdd"));
                                txtGender.setValue(clntDtls.get("gender"));
                                txtDOB.setValue(clntDtls.get("dateofbirth"));
                                txtRegBranchCode.setValue(clntDtls.get("accountofficer"));
                                txtRegBranchName.setValue(clntDtls.get("branchName"));
                                txtMaritalStatus.setValue(clntDtls.get("maritalstatus"));
                                txtCltOccupation.setValue(clntDtls.get("occupation"));
                                txtZipCode.setValue(clntDtls.get("postcode"));
                                txtSurburbs.setValue(clntDtls.get("postcode"));
                                txtTownName.setValue(clntDtls.get("towncountry"));
                                //txtClientTypes.setValue((String)clntDtls.get("localref"));
                                session.setAttribute("typeVAl",
                                                     clntDtls.get("localref"));
                                ClientTypeChanged();
                            } catch (Exception e) {
                                GlobalCC.EXCEPTIONREPORTING(e);
                            }
                            GlobalCC.refreshUI(clientTab);
                        }
                    }


                    if ("I&M".equalsIgnoreCase(client)) {
                        String ClientNumber =
                            GlobalCC.checkNullValues(valueChangeEvent.getNewValue());


                        DBConnector dbConnector = new DBConnector();
                        // String query ="begin ?:= tqc_clients_pkg.get_cif_clients(?);end;";

                        String query =
                            "SELECT * FROM VIEW_CUSTOMER_DETAILS@FINACLE.INM.CORP WHERE CIF = ?";

                        OracleCallableStatement statement = null;
                        OracleConnection connection = null;
                        ResultSet rs = null;

                        try {

                            connection =
                                    (OracleConnection)dbConnector.getDatabaseConnection();
                            statement =
                                    (OracleCallableStatement)connection.prepareCall(query);


                            statement.setObject(1, ClientNumber);
                            rs = statement.executeQuery();

                            while (rs.next()) {

                                session.setAttribute("CIF",
                                                     rs.getString("CIF"));
                                session.setAttribute("NAME",
                                                     rs.getString("NAME"));
                                session.setAttribute("PIN_NO",
                                                     rs.getString("PIN_NO"));
                                session.setAttribute("PHONE",
                                                     rs.getString("PHONE"));
                                session.setAttribute("POSTAL_CODE",
                                                     rs.getString("POSTAL_CODE"));
                                session.setAttribute("GENDER",
                                                     rs.getString("GENDER"));


                            }

                            rs.close();
                            statement.close();
                            connection.close();

                        } catch (Exception e) {
                            GlobalCC.EXCEPTIONREPORTING(connection, e);
                        }


                        legacyShtDesc.setValue(session.getAttribute("CIF"));
                        txtPIN.setValue(session.getAttribute("PIN_NO"));
                        txtSurname.setValue(session.getAttribute("NAME"));
                        txtGender.setValue(session.getAttribute("GENDER"));
                        txtPostalAddress.setValue(session.getAttribute("POSTAL_CODE"));
                        txtPhone1.setValue(session.getAttribute("PHONE"));
                        txtClientCellNos.setValue(session.getAttribute("PHONE"));


                        GlobalCC.refreshUI(clientTab);


                    }


                }
            }
        }
    }


    public void clientVaraibleInitialization(Connection conn) {

        CallableStatement callStmt = null;
        try {
            HttpSession session =
                (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            String globalUserVar = "begin pkg_global_vars.set_pvar(?,?); end;";

            callStmt =
                    (OracleCallableStatement)conn.prepareCall("begin pkg_global_vars.set_pvar(?,?); end;");

            callStmt.setString(1, "pkg_global_vars.pvg_username");
            callStmt.setString(2, (String)session.getAttribute("Username"));
            callStmt.execute();
            callStmt = conn.prepareCall(globalUserVar);

            callStmt.setString(1, "Pkg_Global_Vars.Pvg_ClientCode");
            callStmt.setBigDecimal(2,
                                   GlobalCC.checkBDNullValues(session.getAttribute("ClientCode")));
            callStmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }

    public void setTxtMaritalStatus(RichSelectOneChoice txtMaritalStatus) {
        this.txtMaritalStatus = txtMaritalStatus;
    }

    public RichSelectOneChoice getTxtMaritalStatus() {
        return txtMaritalStatus;
    }

    public void setTxtModeOfComm(RichSelectOneChoice txtModeOfComm) {
        this.txtModeOfComm = txtModeOfComm;
    }

    public RichSelectOneChoice getTxtModeOfComm() {
        return txtModeOfComm;
    }

    public void setTxtPayroll(RichInputText txtPayroll) {
        this.txtPayroll = txtPayroll;
    }

    public RichInputText getTxtPayroll() {
        return txtPayroll;
    }

    public void setTxtMinSalary(RichInputNumberSpinbox txtMinSalary) {
        this.txtMinSalary = txtMinSalary;
    }

    public RichInputNumberSpinbox getTxtMinSalary() {
        return txtMinSalary;
    }

    public void setTxtMaxSalary(RichInputNumberSpinbox txtMaxSalary) {
        this.txtMaxSalary = txtMaxSalary;
    }

    public void setTxtType(RichSelectOneChoice txtType) {
        this.txtType = txtType;
    }

    public RichSelectOneChoice getTxtType() {
        return txtType;
    }

    public void setTxtBussinessPerson(RichInputText txtBussinessPerson) {
        this.txtBussinessPerson = txtBussinessPerson;
    }

    public RichInputText getTxtBussinessPerson() {
        return txtBussinessPerson;
    }

    public void setBussinesPersonTbl(RichTable bussinesPersonTbl) {
        this.bussinesPersonTbl = bussinesPersonTbl;
    }

    public RichTable getBussinesPersonTbl() {
        return bussinesPersonTbl;
    }

    public String selectBussinessPerson() {
        Object key = bussinesPersonTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
        System.out.println("This is bpnCode" + r.getAttribute("bpnCode"));
        session.setAttribute("bpnCodeVal", r.getAttribute("bpnCode"));
        txtBussinessPerson.setValue(r.getAttribute("bpnName"));
        GlobalCC.refreshUI(txtBussinessPerson);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:BussinessPop" + "').hide(hints);");

        // Add event code here...
        return null;
    }

    public String selectBussinessPersonPop() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "pt1:BussinessPop" + "').show(hints);");

        return null;
    }

    public void setOccupationTbl(RichTable occupationTbl) {
        this.occupationTbl = occupationTbl;
    }

    public RichTable getOccupationTbl() {
        return occupationTbl;
    }

    public String selectOccupation() {
        Object key = occupationTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        txtCltOccupation.setValue(r.getAttribute("occName"));
        session.setAttribute("occCode", r.getAttribute("occCode"));
        GlobalCC.refreshUI(txtCltOccupation);
        // Add event code here...
        return null;
    }

    public String selectOccupationDetails() {
        try {
            Object key = occupationTbl.getSelectedRowData();
            JUCtrlValueBinding r = (JUCtrlValueBinding)key;
            if (r == null) {
                GlobalCC.INFORMATIONREPORTING("No Record selected");
                return null;
            }

            session.setAttribute("occupationCode", r.getAttribute("occCode"));
            txtCltOccupation.setValue(r.getAttribute("occName"));
            session.setAttribute("occCode", r.getAttribute("occCode"));

            GlobalCC.refreshUI(txtCltOccupation);
            ADFUtils.findIterator("findSpecializationsIterator").executeQuery();
            GlobalCC.refreshUI(specializationTbl);

            GlobalCC.hidePopup("pt1:occupationPop");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }


    public String selectSignatoryOccupationDetails() {
        try {
            Object key = signaoccupationTbl.getSelectedRowData();
            JUCtrlValueBinding r = (JUCtrlValueBinding)key;
            if (r == null) {
                GlobalCC.INFORMATIONREPORTING("No Record selected");
                return null;
            }

            session.setAttribute("signoccupationCode",
                                 r.getAttribute("occCode"));
            txtSignatoryOccupation.setValue(r.getAttribute("occName"));

            session.setAttribute("signoccCode", r.getAttribute("occCode"));

            GlobalCC.refreshUI(txtSignatoryOccupation);
            ADFUtils.findIterator("findSpecializationsIterator").executeQuery();
            GlobalCC.refreshUI(specializationTbl);

            GlobalCC.hidePopup("pt1:signoccupationPop");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String selectSpecializationDetails() {
        Object key = specializationTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("No Record selected");
            return null;
        }
        txtSpecialization.setValue(r.getAttribute("SPZ_NAME"));
        session.setAttribute("SPZ_CODE", r.getAttribute("SPZ_CODE"));
        GlobalCC.refreshUI(txtSpecialization);
        GlobalCC.hidePopup("pt1:SpecializationPop");
        return null;
    }

    public void setConfirmPin(RichDialog confirmPin) {
        this.confirmPin = confirmPin;
    }

    public RichInputNumberSpinbox getTxtMaxSalary() {
        return txtMaxSalary;
    }

    public RichDialog getConfirmPin() {
        return confirmPin;
    }

    public void confirmPinContinue(DialogEvent dialogEvent) {
        session.setAttribute("pinStatus", null);
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {

            session.setAttribute("pinStatus", "NO");

        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            session.setAttribute("pinStatus", "YES");
            checkIfSSuchRecorDExist();
        }
    }

    public void setTxtDlIssueDate(RichInputDate txtDlIssueDate) {
        this.txtDlIssueDate = txtDlIssueDate;
    }

    public RichInputDate getTxtDlIssueDate() {
        return txtDlIssueDate;
    }

    public void setTxtWorkPermit(RichInputText txtWorkPermit) {
        this.txtWorkPermit = txtWorkPermit;
    }

    public RichInputText getTxtWorkPermit() {
        return txtWorkPermit;
    }

    public boolean isForegn() {
        String defaultCountry = "NONE";
        defaultCountry =
                (String)GlobalCC.checkNullValues(session.getAttribute("COUNTRY_NAME"));
        if (defaultCountry != null) {
            if (defaultCountry.equalsIgnoreCase((String)txtCountryName.getValue())) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public void setUpFile(RichInputFile upFile) {
        this.upFile = upFile;
    }

    public RichInputFile getUpFile() {
        return upFile;
    }


    public void setDocTbl(RichTable docTbl) {
        this.docTbl = docTbl;
    }

    public RichTable getDocTbl() {
        return docTbl;
    }


    public String launchDebtor() {
        ADFUtils.findIterator("fetchClientAccountsIterator").executeQuery();
        GlobalCC.refreshUI(debtor);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" + "pt1:conDebtor" +
                             "').show(hints);");
        return null;
    }

    public void setDebtor(RichTable debtor) {
        this.debtor = debtor;
    }

    public RichTable getDebtor() {
        return debtor;
    }

    public String debtorSelected() {
        Object key2 = debtor.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            conSect.setValue(nodeBinding.getAttribute("name"));

            conSect.setLabel(nodeBinding.getAttribute("code").toString());
            GlobalCC.refreshUI(conSect);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "pt1:conDebtor" + "').hide(hints);");


        }
        return null;
    }

    public void setPrefixTbl2(RichTable prefixTbl2) {
        this.prefixTbl2 = prefixTbl2;
    }

    public RichTable getPrefixTbl2() {
        return prefixTbl2;
    }

    public void setTxtTelPayPrefix(RichInputText txtTelPayPrefix) {
        this.txtTelPayPrefix = txtTelPayPrefix;
    }

    public RichInputText getTxtTelPayPrefix() {
        return txtTelPayPrefix;
    }

    public void setTxtPayTel(RichInputText txtPayTel) {
        this.txtPayTel = txtPayTel;
    }

    public RichInputText getTxtPayTel() {
        return txtPayTel;
    }

    public void setPostalCodeTable(RichTable postalCodeTable) {
        this.postalCodeTable = postalCodeTable;
    }

    public RichTable getPostalCodeTable() {
        return postalCodeTable;
    }

    public void setPostalCode(RichInputText postalCode) {
        this.postalCode = postalCode;
    }

    public RichInputText getPostalCode() {
        return postalCode;
    }

    public void setTxtEmail2(RichInputText txtEmail2) {
        this.txtEmail2 = txtEmail2;
    }

    public RichInputText getTxtEmail2() {
        return txtEmail2;
    }

    public void setTblCountryPop2(RichTable tblCountryPop2) {
        this.tblCountryPop2 = tblCountryPop2;
    }

    public RichTable getTblCountryPop2() {
        return tblCountryPop2;
    }

    public void setTxtCountryName2(RichInputText txtCountryName2) {
        this.txtCountryName2 = txtCountryName2;
    }

    public RichInputText getTxtCountryName2() {
        return txtCountryName2;
    }

    public void setTxtCountryCode2(RichInputText txtCountryCode2) {
        this.txtCountryCode2 = txtCountryCode2;
    }

    public RichInputText getTxtCountryCode2() {
        return txtCountryCode2;
    }

    public void setTxtCountryName3(RichInputText txtCountryName3) {
        this.txtCountryName3 = txtCountryName3;
    }

    public RichInputText getTxtCountryName3() {
        return txtCountryName3;
    }

    public void setTxtCountryCode3(RichInputText txtCountryCode3) {
        this.txtCountryCode3 = txtCountryCode3;
    }

    public RichInputText getTxtCountryCode3() {
        return txtCountryCode3;
    }

    public void setTxtIntTel(RichInputText txtIntTel) {
        this.txtIntTel = txtIntTel;
    }

    public RichInputText getTxtIntTel() {
        return txtIntTel;
    }

    public void setTblCountryPop3(RichTable tblCountryPop3) {
        this.tblCountryPop3 = tblCountryPop3;
    }

    public RichTable getTblCountryPop3() {
        return tblCountryPop3;
    }

    public void GSMCountriesListener(SelectionEvent selectionEvent) {
        // Add event code here...
        Object key2 = tblCountryPop2.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            System.out.println("Country" + nodeBinding.getAttribute("name"));
        }
    }

    public void setTxtClientLevel(RichSelectOneChoice txtClientLevel) {
        this.txtClientLevel = txtClientLevel;
    }

    public RichSelectOneChoice getTxtClientLevel() {
        return txtClientLevel;
    }

    public void setTxtCheckedByComplianceOfficer(RichSelectOneChoice txtCheckedByComplianceOfficer) {
        this.txtCheckedByComplianceOfficer = txtCheckedByComplianceOfficer;
    }

    public RichSelectOneChoice getTxtCheckedByComplianceOfficer() {
        return txtCheckedByComplianceOfficer;
    }

    public void setChkGISClient(RichSelectBooleanCheckbox chkGISClient) {
        this.chkGISClient = chkGISClient;
    }

    public RichSelectBooleanCheckbox getChkGISClient() {
        return chkGISClient;
    }

    public void setChkLMSClient(RichSelectBooleanCheckbox chkLMSClient) {
        this.chkLMSClient = chkLMSClient;
    }

    public RichSelectBooleanCheckbox getChkLMSClient() {
        return chkLMSClient;
    }

    public void setTxtAccCntName(RichInputText txtAccCntName) {
        this.txtAccCntName = txtAccCntName;
    }

    public RichInputText getTxtAccCntName() {
        return txtAccCntName;
    }

    public void setTxtAccCntTitle(RichInputText txtAccCntTitle) {
        this.txtAccCntTitle = txtAccCntTitle;
    }

    public RichInputText getTxtAccCntTitle() {
        return txtAccCntTitle;
    }

    public void setTxtAccCntSmsNo(RichInputText txtAccCntSmsNo) {
        this.txtAccCntSmsNo = txtAccCntSmsNo;
    }

    public RichInputText getTxtAccCntSmsNo() {
        return txtAccCntSmsNo;
    }

    public void setTxtAccCntEmail(RichInputText txtAccCntEmail) {
        this.txtAccCntEmail = txtAccCntEmail;
    }

    public RichInputText getTxtAccCntEmail() {
        return txtAccCntEmail;
    }

    public void setTxtSelectContactTitle(RichCommandButton txtSelectContactTitle) {
        this.txtSelectContactTitle = txtSelectContactTitle;
    }

    public RichCommandButton getTxtSelectContactTitle() {
        return txtSelectContactTitle;
    }

    public void setTxtFullNames(RichInputText txtFullNames) {
        this.txtFullNames = txtFullNames;
    }

    public RichInputText getTxtFullNames() {
        return txtFullNames;
    }

    public void setTxtSearchCountry(RichInputText txtSearchCountry) {
        this.txtSearchCountry = txtSearchCountry;
    }

    public RichInputText getTxtSearchCountry() {
        return txtSearchCountry;
    }

    public void setBtnBackClient(RichCommandButton btnBackClient) {
        this.btnBackClient = btnBackClient;
    }

    public RichCommandButton getBtnBackClient() {
        return btnBackClient;
    }

    public void setSpecializationTbl(RichTable specializationTbl) {
        this.specializationTbl = specializationTbl;
    }

    public RichTable getSpecializationTbl() {
        return specializationTbl;
    }

    public void setTxtSpecialization(RichInputText txtSpecialization) {
        this.txtSpecialization = txtSpecialization;
    }

    public RichInputText getTxtSpecialization() {
        return txtSpecialization;
    }

    public void setDlgClientAccount(RichDialog dlgClientAccount) {
        this.dlgClientAccount = dlgClientAccount;
    }

    public RichDialog getDlgClientAccount() {
        return dlgClientAccount;
    }

    public void setTxtCustomerCompany(RichInputText txtCustomerCompany) {
        this.txtCustomerCompany = txtCustomerCompany;
    }

    public RichInputText getTxtCustomerCompany() {
        return txtCustomerCompany;
    }

    public void setTblBankAccounts(RichTable tblBankAccounts) {
        this.tblBankAccounts = tblBankAccounts;
    }

    public RichTable getTblBankAccounts() {
        return tblBankAccounts;
    }

    public void setTxtBACT_NAME(RichInputText txtBACT_NAME) {
        this.txtBACT_NAME = txtBACT_NAME;
    }

    public RichInputText getTxtBACT_NAME() {
        return txtBACT_NAME;
    }

    public void setTxtBACT_ACCOUNT_NO(RichInputText txtBACT_ACCOUNT_NO) {
        this.txtBACT_ACCOUNT_NO = txtBACT_ACCOUNT_NO;
    }

    public RichInputText getTxtBACT_ACCOUNT_NO() {
        return txtBACT_ACCOUNT_NO;
    }

    public void setTxtBACT_BANK_BRANCH(RichInputText txtBACT_BANK_BRANCH) {
        this.txtBACT_BANK_BRANCH = txtBACT_BANK_BRANCH;
    }

    public RichInputText getTxtBACT_BANK_BRANCH() {
        return txtBACT_BANK_BRANCH;
    }

    public void setTxtBACT_CURRENCY(RichInputText txtBACT_CURRENCY) {
        this.txtBACT_CURRENCY = txtBACT_CURRENCY;
    }

    public RichInputText getTxtBACT_CURRENCY() {
        return txtBACT_CURRENCY;
    }

    public void setTxtBACT_BANK_NAME(RichInputText txtBACT_BANK_NAME) {
        this.txtBACT_BANK_NAME = txtBACT_BANK_NAME;
    }

    public RichInputText getTxtBACT_BANK_NAME() {
        return txtBACT_BANK_NAME;
    }

    public void setTxtBACT_CELL_NOS(RichInputText txtBACT_CELL_NOS) {
        this.txtBACT_CELL_NOS = txtBACT_CELL_NOS;
    }

    public RichInputText getTxtBACT_CELL_NOS() {
        return txtBACT_CELL_NOS;
    }

    public void setTxtBACT_TEL_NOS(RichInputText txtBACT_TEL_NOS) {
        this.txtBACT_TEL_NOS = txtBACT_TEL_NOS;
    }

    public RichInputText getTxtBACT_TEL_NOS() {
        return txtBACT_TEL_NOS;
    }

    public void setTxtBACT_ACCOUNT_TYPE(RichInputText txtBACT_ACCOUNT_TYPE) {
        this.txtBACT_ACCOUNT_TYPE = txtBACT_ACCOUNT_TYPE;
    }

    public RichInputText getTxtBACT_ACCOUNT_TYPE() {
        return txtBACT_ACCOUNT_TYPE;
    }

    public void setTxtBACT_DEFAULT(RichSelectOneChoice txtBACT_DEFAULT) {
        this.txtBACT_DEFAULT = txtBACT_DEFAULT;
    }

    public RichSelectOneChoice getTxtBACT_DEFAULT() {
        return txtBACT_DEFAULT;
    }

    public void setTblCurrencyPop(RichTable tblCurrencyPop) {
        this.tblCurrencyPop = tblCurrencyPop;
    }

    public RichTable getTblCurrencyPop() {
        return tblCurrencyPop;
    }

    public void setPanelCurrencyRate(RichPanelBox panelCurrencyRate) {
        this.panelCurrencyRate = panelCurrencyRate;
    }

    public RichPanelBox getPanelCurrencyRate() {
        return panelCurrencyRate;
    }

    public void setSaveBankDetailsBtn(RichCommandButton saveBankDetailsBtn) {
        this.saveBankDetailsBtn = saveBankDetailsBtn;
    }

    public RichCommandButton getSaveBankDetailsBtn() {
        return saveBankDetailsBtn;
    }

    public void setTxtBACT_ACC_OFFICER(RichInputText txtBACT_ACC_OFFICER) {
        this.txtBACT_ACC_OFFICER = txtBACT_ACC_OFFICER;
    }

    public RichInputText getTxtBACT_ACC_OFFICER() {
        return txtBACT_ACC_OFFICER;
    }

    public void setTabBankAccounts(RichShowDetailItem tabBankAccounts) {
        this.tabBankAccounts = tabBankAccounts;
    }

    public RichShowDetailItem getTabBankAccounts() {
        return tabBankAccounts;
    }

    public void setBankDetailsPopUp(RichPopup bankDetailsPopUp) {
        this.bankDetailsPopUp = bankDetailsPopUp;
    }

    public RichPopup getBankDetailsPopUp() {
        return bankDetailsPopUp;
    }

    public void setTxtBnkIBAN(RichInputText txtBnkIBAN) {
        this.txtBnkIBAN = txtBnkIBAN;
    }

    public RichInputText getTxtBnkIBAN() {
        return txtBnkIBAN;
    }

    public void setTxtBACT_STATUS(RichSelectOneChoice txtBACT_STATUS) {
        this.txtBACT_STATUS = txtBACT_STATUS;
    }

    public RichSelectOneChoice getTxtBACT_STATUS() {
        return txtBACT_STATUS;
    }

    public String clientsRefs() {
        // Add event code here...
        return null;
    }

    public void setCmdSelectPin(RichCommandLink cmdSelectPin) {
        this.cmdSelectPin = cmdSelectPin;
    }

    public RichCommandLink getCmdSelectPin() {
        return cmdSelectPin;
    }

    public void setTbPinRefs(RichTable tbPinRefs) {
        this.tbPinRefs = tbPinRefs;
    }

    public RichTable getTbPinRefs() {
        return tbPinRefs;
    }

    public void setTxtStaffNo(RichInputText txtStaffNo) {
        this.txtStaffNo = txtStaffNo;
    }

    public RichInputText getTxtStaffNo() {
        return txtStaffNo;
    }

    public void emailValidator(FacesContext facesContext,
                               UIComponent uIComponent, Object object) {
        if (object != null) {
            if (!GlobalCC.validate(object, GlobalCC.EMAIL_PATTERN)) {
                String msg =
                    "Email is not in Proper Format: Expected format is example@example.com";
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              msg, null));
            }
        }

    }

    public void setLblClientTabTitle(RichOutputLabel lblClientTabTitle) {
        this.lblClientTabTitle = lblClientTabTitle;
    }

    public RichOutputLabel getLblClientTabTitle() {
        return lblClientTabTitle;
    }

    public void setPanMsgOccupation(RichPanelLabelAndMessage panMsgOccupation) {
        this.panMsgOccupation = panMsgOccupation;
    }

    public RichPanelLabelAndMessage getPanMsgOccupation() {
        return panMsgOccupation;
    }

    public void setBtnMakeReadyClient(RichCommandButton btnMakeReadyClient) {
        this.btnMakeReadyClient = btnMakeReadyClient;
    }

    public RichCommandButton getBtnMakeReadyClient() {
        return btnMakeReadyClient;
    }

    public void setBtnDeactivateClient(RichCommandButton btnDeactivateClient) {
        this.btnDeactivateClient = btnDeactivateClient;
    }

    public RichCommandButton getBtnDeactivateClient() {
        return btnDeactivateClient;
    }

    public void setBtnReassignClient(RichCommandButton btnReassignClient) {
        this.btnReassignClient = btnReassignClient;
    }

    public RichCommandButton getBtnReassignClient() {
        return btnReassignClient;
    }

    public void setPanMsgSector(RichPanelLabelAndMessage panMsgSector) {
        this.panMsgSector = panMsgSector;
    }

    public RichPanelLabelAndMessage getPanMsgSector() {
        return panMsgSector;
    }

    public void setPanMsgSpecialization(RichPanelLabelAndMessage panMsgSpecialization) {
        this.panMsgSpecialization = panMsgSpecialization;
    }

    public RichPanelLabelAndMessage getPanMsgSpecialization() {
        return panMsgSpecialization;
    }

    public void setSystemUsers(RichTable systemUsers) {
        this.systemUsers = systemUsers;
    }

    public RichTable getSystemUsers() {
        return systemUsers;
    }

    public void setTxtAssignRemarks(RichInputText txtAssignRemarks) {
        this.txtAssignRemarks = txtAssignRemarks;
    }

    public RichInputText getTxtAssignRemarks() {
        return txtAssignRemarks;
    }

    public void setTxtOldAccNO(RichInputText txtOldAccNO) {
        this.txtOldAccNO = txtOldAccNO;
    }

    public RichInputText getTxtOldAccNO() {
        return txtOldAccNO;
    }


    public void setRequiredDocsBox(RichPanelBox requiredDocsBox) {
        this.requiredDocsBox = requiredDocsBox;
    }

    public RichPanelBox getRequiredDocsBox() {
        return requiredDocsBox;
    }

    public void setTxtPreferedBenefitPaymentMode(RichInputText txtPreferedBenefitPaymentMode) {
        this.txtPreferedBenefitPaymentMode = txtPreferedBenefitPaymentMode;
    }

    public RichInputText getTxtPreferedBenefitPaymentMode() {
        return txtPreferedBenefitPaymentMode;
    }

    public void setTxtPreferedBenefitPaymentModeCode(RichInputText txtPreferedBenefitPaymentModeCode) {
        this.txtPreferedBenefitPaymentModeCode =
                txtPreferedBenefitPaymentModeCode;
    }

    public RichInputText getTxtPreferedBenefitPaymentModeCode() {
        return txtPreferedBenefitPaymentModeCode;
    }

    public String actionAcceptPreferedBenefitPayment() {
        Object key2 = tblPaymentMode.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtPreferedBenefitPaymentMode.setValue(nodeBinding.getAttribute("description"));
            txtPreferedBenefitPaymentModeCode.setValue(nodeBinding.getAttribute("code"));
            System.out.println("code: " + nodeBinding.getAttribute("code"));
            GlobalCC.refreshUI(txtPreferedBenefitPaymentMode);
            GlobalCC.refreshUI(txtPreferedBenefitPaymentModeCode);
        }

        GlobalCC.dismissPopUp("pt1", "preferedBenefitPaymentPopup");
        return null;
    }

    public void setTblPaymentMode(RichTable tblPaymentMode) {
        this.tblPaymentMode = tblPaymentMode;
    }

    public RichTable getTblPaymentMode() {
        return tblPaymentMode;
    }

    public void setTxtPreferedPremiumPaymentCode(RichInputText txtPreferedPremiumPaymentCode) {
        this.txtPreferedPremiumPaymentCode = txtPreferedPremiumPaymentCode;
    }

    public RichInputText getTxtPreferedPremiumPaymentCode() {
        return txtPreferedPremiumPaymentCode;
    }

    public void setTxtPreferedPremiumPayment(RichInputText txtPreferedPremiumPayment) {
        this.txtPreferedPremiumPayment = txtPreferedPremiumPayment;
    }

    public RichInputText getTxtPreferedPremiumPayment() {
        return txtPreferedPremiumPayment;
    }

    public String actionAcceptPreferedPremiumPayment() {
        Object key2 = tblPremiumPaymode.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtPreferedPremiumPayment.setValue(nodeBinding.getAttribute("description"));
            txtPreferedPremiumPaymentCode.setValue(nodeBinding.getAttribute("code"));
            System.out.println("code: " + nodeBinding.getAttribute("code"));
            GlobalCC.refreshUI(txtPreferedPremiumPayment);
            GlobalCC.refreshUI(txtPreferedPremiumPaymentCode);
        }

        GlobalCC.dismissPopUp("pt1", "preferedPremiumPaymentPopup");
        return null;
    }

    public void setTblPremiumPaymode(RichTable tblPremiumPaymode) {
        this.tblPremiumPaymode = tblPremiumPaymode;
    }

    public RichTable getTblPremiumPaymode() {
        return tblPremiumPaymode;
    }

    public void setTxtEduLevel(RichInputText txtEduLevel) {
        this.txtEduLevel = txtEduLevel;
    }

    public RichInputText getTxtEduLevel() {
        return txtEduLevel;
    }

    public void setTxtMonthlyIncome(RichInputNumberSpinbox txtMonthlyIncome) {
        this.txtMonthlyIncome = txtMonthlyIncome;
    }

    public RichInputNumberSpinbox getTxtMonthlyIncome() {
        return txtMonthlyIncome;
    }

    public void setEmployerDetailsPopup(RichPopup employerDetailsPopup) {
        this.employerDetailsPopup = employerDetailsPopup;
    }

    public RichPopup getEmployerDetailsPopup() {
        return employerDetailsPopup;
    }

    public void setClntEmplName(RichInputText clntEmplName) {
        this.clntEmplName = clntEmplName;
    }

    public RichInputText getClntEmplName() {
        return clntEmplName;
    }

    public void setClntEmplCountryName(RichInputText clntEmplCountryName) {
        this.clntEmplCountryName = clntEmplCountryName;
    }

    public RichInputText getClntEmplCountryName() {
        return clntEmplCountryName;
    }

    public void setClntEmplTwnName(RichInputText clntEmplTwnName) {
        this.clntEmplTwnName = clntEmplTwnName;
    }

    public RichInputText getClntEmplTwnName() {
        return clntEmplTwnName;
    }

    public void setClntEmplTwnPostalZipCode(RichInputText clntEmplTwnPostalZipCode) {
        this.clntEmplTwnPostalZipCode = clntEmplTwnPostalZipCode;
    }

    public RichInputText getClntEmplTwnPostalZipCode() {
        return clntEmplTwnPostalZipCode;
    }

    public void setClntEmplSectorName(RichInputText clntEmplSectorName) {
        this.clntEmplSectorName = clntEmplSectorName;
    }

    public RichInputText getClntEmplSectorName() {
        return clntEmplSectorName;
    }

    public void setClntEmplType(RichSelectOneChoice clntEmplType) {
        this.clntEmplType = clntEmplType;
    }

    public RichSelectOneChoice getClntEmplType() {
        return clntEmplType;
    }

    public void setClntEmplPayrollNo(RichInputText clntEmplPayrollNo) {
        this.clntEmplPayrollNo = clntEmplPayrollNo;
    }

    public RichInputText getClntEmplPayrollNo() {
        return clntEmplPayrollNo;
    }

    public void setClntEmplMonthlyIncome(RichInputNumberSpinbox clntEmplMonthlyIncome) {
        this.clntEmplMonthlyIncome = clntEmplMonthlyIncome;
    }

    public RichInputNumberSpinbox getClntEmplMonthlyIncome() {
        return clntEmplMonthlyIncome;
    }

    public void setClntEmplMinSalary(RichInputNumberSpinbox clntEmplMinSalary) {
        this.clntEmplMinSalary = clntEmplMinSalary;
    }

    public RichInputNumberSpinbox getClntEmplMinSalary() {
        return clntEmplMinSalary;
    }

    public void setClntEmplMaxSalary(RichInputNumberSpinbox clntEmplMaxSalary) {
        this.clntEmplMaxSalary = clntEmplMaxSalary;
    }

    public RichInputNumberSpinbox getClntEmplMaxSalary() {
        return clntEmplMaxSalary;
    }

    public void setClntEmplEmploymentDate(RichInputDate clntEmplEmploymentDate) {
        this.clntEmplEmploymentDate = clntEmplEmploymentDate;
    }

    public RichInputDate getClntEmplEmploymentDate() {
        return clntEmplEmploymentDate;
    }

    public void setClntEmplTelNos(RichInputText clntEmplTelNos) {
        this.clntEmplTelNos = clntEmplTelNos;
    }

    public RichInputText getClntEmplTelNos() {
        return clntEmplTelNos;
    }

    public void setClntEmplCellNos(RichInputText clntEmplCellNos) {
        this.clntEmplCellNos = clntEmplCellNos;
    }

    public RichInputText getClntEmplCellNos() {
        return clntEmplCellNos;
    }

    public void setClntEmplFax(RichInputText clntEmplFax) {
        this.clntEmplFax = clntEmplFax;
    }

    public RichInputText getClntEmplFax() {
        return clntEmplFax;
    }

    public void setClntEmplCode(RichInputText clntEmplCode) {
        this.clntEmplCode = clntEmplCode;
    }

    public RichInputText getClntEmplCode() {
        return clntEmplCode;
    }

    public void setClntEmplCountryCode(RichInputText clntEmplCountryCode) {
        this.clntEmplCountryCode = clntEmplCountryCode;
    }

    public RichInputText getClntEmplCountryCode() {
        return clntEmplCountryCode;
    }

    public void setClntEmplTwnCode(RichInputText clntEmplTwnCode) {
        this.clntEmplTwnCode = clntEmplTwnCode;
    }

    public RichInputText getClntEmplTwnCode() {
        return clntEmplTwnCode;
    }

    public void setClntEmplSectorCode(RichInputText clntEmplSectorCode) {
        this.clntEmplSectorCode = clntEmplSectorCode;
    }

    public RichInputText getClntEmplSectorCode() {
        return clntEmplSectorCode;
    }

    /*public void setClntEmplTwnPostalCode(RichInputText clntEmplTwnPostalCode) {
        this.clntEmplTwnPostalCode = clntEmplTwnPostalCode;
    }

    public RichInputText getClntEmplTwnPostalCode() {
        return clntEmplTwnPostalCode;
    }*/

    public void setTblEmployerDetails(RichTable tblEmployerDetails) {
        this.tblEmployerDetails = tblEmployerDetails;
    }

    public RichTable getTblEmployerDetails() {
        return tblEmployerDetails;
    }

    public void actionDiscloseEmployerDetailsTab(DisclosureEvent event) {
        if (event.isExpanded()) {
            ADFUtils.findIterator("fetchClientEmployersIterator").executeQuery();
            GlobalCC.refreshUI(tblEmployerDetails);
        }
    }

    public void setTblEmpCountryPop(RichTable tblEmpCountryPop) {
        this.tblEmpCountryPop = tblEmpCountryPop;
    }

    public RichTable getTblEmpCountryPop() {
        return tblEmpCountryPop;
    }

    public void setTxtSearchEmpCountry(RichInputText txtSearchEmpCountry) {
        this.txtSearchEmpCountry = txtSearchEmpCountry;
    }

    public RichInputText getTxtSearchEmpCountry() {
        return txtSearchEmpCountry;
    }

    public void setClntEmplState(RichInputText clntEmplState) {
        this.clntEmplState = clntEmplState;
    }

    public RichInputText getClntEmplState() {
        return clntEmplState;
    }

    public void setClntEmplStateCode(RichInputText clntEmplStateCode) {
        this.clntEmplStateCode = clntEmplStateCode;
    }

    public RichInputText getClntEmplStateCode() {
        return clntEmplStateCode;
    }

    public void setClntEmplStatePanel(RichPanelLabelAndMessage clntEmplStatePanel) {
        this.clntEmplStatePanel = clntEmplStatePanel;
    }

    public RichPanelLabelAndMessage getClntEmplStatePanel() {
        return clntEmplStatePanel;
    }

    public void setClntEmplTownPanel(RichPanelLabelAndMessage clntEmplTownPanel) {
        this.clntEmplTownPanel = clntEmplTownPanel;
    }

    public RichPanelLabelAndMessage getClntEmplTownPanel() {
        return clntEmplTownPanel;
    }

    public void setClntPostalCodePanel(RichPanelLabelAndMessage clntPostalCodePanel) {
        this.clntPostalCodePanel = clntPostalCodePanel;
    }

    public RichPanelLabelAndMessage getClntPostalCodePanel() {
        return clntPostalCodePanel;
    }

    public void setDlgAdminEmpRegionTypes(RichDialog dlgAdminEmpRegionTypes) {
        this.dlgAdminEmpRegionTypes = dlgAdminEmpRegionTypes;
    }

    public RichDialog getDlgAdminEmpRegionTypes() {
        return dlgAdminEmpRegionTypes;
    }

    public void setTblEmpStates(RichTable tblEmpStates) {
        this.tblEmpStates = tblEmpStates;
    }

    public RichTable getTblEmpStates() {
        return tblEmpStates;
    }

    public void setTblEmpTownPop(RichTable tblEmpTownPop) {
        this.tblEmpTownPop = tblEmpTownPop;
    }

    public RichTable getTblEmpTownPop() {
        return tblEmpTownPop;
    }

    public void setEmpPostalCodeTable(RichTable empPostalCodeTable) {
        this.empPostalCodeTable = empPostalCodeTable;
    }

    public RichTable getEmpPostalCodeTable() {
        return empPostalCodeTable;
    }

    public void setTblEmpSectorPop(RichTable tblEmpSectorPop) {
        this.tblEmpSectorPop = tblEmpSectorPop;
    }

    public RichTable getTblEmpSectorPop() {
        return tblEmpSectorPop;
    }

    public void setSaveEmployerDetailsBtn(RichCommandButton saveEmployerDetailsBtn) {
        this.saveEmployerDetailsBtn = saveEmployerDetailsBtn;
    }

    public RichCommandButton getSaveEmployerDetailsBtn() {
        return saveEmployerDetailsBtn;
    }

    public void setDlgIprsComp(RichDialog dlgIprsComp) {
        this.dlgIprsComp = dlgIprsComp;
    }

    public RichDialog getDlgIprsComp() {
        return dlgIprsComp;
    }

    public void setClientIdPan(RichPanelLabelAndMessage clientIdPan) {
        this.clientIdPan = clientIdPan;
    }

    public RichPanelLabelAndMessage getClientIdPan() {
        return clientIdPan;
    }

    public void setClientPassportPan(RichPanelLabelAndMessage clientPassportPan) {
        this.clientPassportPan = clientPassportPan;
    }

    public RichPanelLabelAndMessage getClientPassportPan() {
        return clientPassportPan;
    }

    public void setTxtIprsIdNumber(RichInputText txtIprsIdNumber) {
        this.txtIprsIdNumber = txtIprsIdNumber;
    }

    public RichInputText getTxtIprsIdNumber() {
        return txtIprsIdNumber;
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

    public void setClientPinPan(RichPanelLabelAndMessage clientPinPan) {
        this.clientPinPan = clientPinPan;
    }

    public RichPanelLabelAndMessage getClientPinPan() {
        return clientPinPan;
    }

    public void setTabSignatories(RichShowDetailItem tabSignatories) {
        this.tabSignatories = tabSignatories;
    }

    public RichShowDetailItem getTabSignatories() {
        return tabSignatories;
    }

    public void setBtnRefreshSignatories(RichCommandButton btnRefreshSignatories) {
        this.btnRefreshSignatories = btnRefreshSignatories;
    }

    public RichCommandButton getBtnRefreshSignatories() {
        return btnRefreshSignatories;
    }

    public void setSignatoriesTable(RichTable signatoriesTable) {
        this.signatoriesTable = signatoriesTable;
    }

    public RichTable getSignatoriesTable() {
        return signatoriesTable;
    }

    public void setBtnNewSignatory(RichCommandButton btnNewSignatory) {
        this.btnNewSignatory = btnNewSignatory;
    }

    public RichCommandButton getBtnNewSignatory() {
        return btnNewSignatory;
    }

    public void setBtnEditSignatory(RichCommandButton btnEditSignatory) {
        this.btnEditSignatory = btnEditSignatory;
    }

    public RichCommandButton getBtnEditSignatory() {
        return btnEditSignatory;
    }

    public void setBtnDeleteSignatory(RichCommandButton btnDeleteSignatory) {
        this.btnDeleteSignatory = btnDeleteSignatory;
    }

    public RichCommandButton getBtnDeleteSignatory() {
        return btnDeleteSignatory;
    }

    public void setTxtSignatoryTitle(RichInputText txtSignatoryTitle) {
        this.txtSignatoryTitle = txtSignatoryTitle;
    }

    public RichInputText getTxtSignatoryTitle() {
        return txtSignatoryTitle;
    }

    public void setTxtSignatoryName(RichInputText txtSignatoryName) {
        this.txtSignatoryName = txtSignatoryName;
    }

    public RichInputText getTxtSignatoryName() {
        return txtSignatoryName;
    }


    public void setTxtSignatoryOccupation(RichInputText txtSignatoryOccupation) {
        this.txtSignatoryOccupation = txtSignatoryOccupation;
    }

    public RichInputText getTxtSignatoryOccupation() {
        return txtSignatoryOccupation;
    }

    public void setTxtSignatoryGender(RichSelectOneChoice txtSignatoryGender) {
        this.txtSignatoryGender = txtSignatoryGender;
    }

    public RichSelectOneChoice getTxtSignatoryGender() {
        return txtSignatoryGender;
    }

    public void setTxtSignatoryNationality(RichInputText txtSignatoryNationality) {
        this.txtSignatoryNationality = txtSignatoryNationality;
    }

    public RichInputText getTxtSignatoryNationality() {
        return txtSignatoryNationality;
    }

    public void setTxtSignatoryDateofbirth(RichInputDate txtSignatoryDateofbirth) {
        this.txtSignatoryDateofbirth = txtSignatoryDateofbirth;
    }

    public RichInputDate getTxtSignatoryDateofbirth() {
        return txtSignatoryDateofbirth;
    }

    public void setTxtSignatoryPlaceofbirth(RichInputText txtSignatoryPlaceofbirth) {
        this.txtSignatoryPlaceofbirth = txtSignatoryPlaceofbirth;
    }

    public RichInputText getTxtSignatoryPlaceofbirth() {
        return txtSignatoryPlaceofbirth;
    }

    public void setTxtSignatoryPhoneno(RichInputText txtSignatoryPhoneno) {
        this.txtSignatoryPhoneno = txtSignatoryPhoneno;
    }

    public RichInputText getTxtSignatoryPhoneno() {
        return txtSignatoryPhoneno;
    }

    public void setTxtSignatoryMeansofid(RichSelectOneChoice txtSignatoryMeansofid) {
        this.txtSignatoryMeansofid = txtSignatoryMeansofid;
    }

    public RichSelectOneChoice getTxtSignatoryMeansofid() {
        return txtSignatoryMeansofid;
    }

    public void setTxtSignatoryTaxno(RichInputText txtSignatoryTaxno) {
        this.txtSignatoryTaxno = txtSignatoryTaxno;
    }

    public RichInputText getTxtSignatoryTaxno() {
        return txtSignatoryTaxno;
    }

    public void setTxtSignatoryEmail(RichInputText txtSignatoryEmail) {
        this.txtSignatoryEmail = txtSignatoryEmail;
    }

    public RichInputText getTxtSignatoryEmail() {
        return txtSignatoryEmail;
    }

    public void setTxtSignatoryAddress(RichInputText txtSignatoryAddress) {
        this.txtSignatoryAddress = txtSignatoryAddress;
    }

    public RichInputText getTxtSignatoryAddress() {
        return txtSignatoryAddress;
    }

    public void setBtnSaveSignatory(RichCommandButton btnSaveSignatory) {
        this.btnSaveSignatory = btnSaveSignatory;
    }

    public RichCommandButton getBtnSaveSignatory() {
        return btnSaveSignatory;
    }

    public void setBtnEditDirector(RichCommandButton btnEditDirector) {
        this.btnEditDirector = btnEditDirector;
    }

    public RichCommandButton getBtnEditDirector() {
        return btnEditDirector;
    }

    public void setBtnDeleteDirector(RichCommandButton btnDeleteDirector) {
        this.btnDeleteDirector = btnDeleteDirector;
    }

    public RichCommandButton getBtnDeleteDirector() {
        return btnDeleteDirector;
    }

    public void setBtnSaveDirector(RichCommandButton btnSaveDirector) {
        this.btnSaveDirector = btnSaveDirector;
    }

    public RichCommandButton getBtnSaveDirector() {
        return btnSaveDirector;
    }

    public void setTxtDirectorTitle(RichInputText txtDirectorTitle) {
        this.txtDirectorTitle = txtDirectorTitle;
    }

    public RichInputText getTxtDirectorTitle() {
        return txtDirectorTitle;
    }

    public void setTxtDirectorName(RichInputText txtDirectorName) {
        this.txtDirectorName = txtDirectorName;
    }

    public RichInputText getTxtDirectorName() {
        return txtDirectorName;
    }


    public void setTxtDirectorGender(RichSelectOneChoice txtDirectorGender) {
        this.txtDirectorGender = txtDirectorGender;
    }

    public RichSelectOneChoice getTxtDirectorGender() {
        return txtDirectorGender;
    }

    public void setTxtDirectorNationality(RichInputText txtDirectorNationality) {
        this.txtDirectorNationality = txtDirectorNationality;
    }

    public RichInputText getTxtDirectorNationality() {
        return txtDirectorNationality;
    }


    public void setTxtDirectorEmail(RichInputText txtDirectorEmail) {
        this.txtDirectorEmail = txtDirectorEmail;
    }

    public RichInputText getTxtDirectorEmail() {
        return txtDirectorEmail;
    }

    public void setTxtDirectorAddress(RichInputText txtDirectorAddress) {
        this.txtDirectorAddress = txtDirectorAddress;
    }

    public RichInputText getTxtDirectorAddress() {
        return txtDirectorAddress;
    }

    public void setTxtDirectorYear(RichInputText txtDirectorYear) {
        this.txtDirectorYear = txtDirectorYear;
    }

    public RichInputText getTxtDirectorYear() {
        return txtDirectorYear;
    }

    public void setTxtDirectorQualifications(RichInputText txtDirectorQualifications) {
        this.txtDirectorQualifications = txtDirectorQualifications;
    }

    public RichInputText getTxtDirectorQualifications() {
        return txtDirectorQualifications;
    }


    public void setTxtDirectorDesignation(RichInputText txtDirectorDesignation) {
        this.txtDirectorDesignation = txtDirectorDesignation;
    }

    public RichInputText getTxtDirectorDesignation() {
        return txtDirectorDesignation;
    }

    public void setDirectorsTable(RichTable directorsTable) {
        this.directorsTable = directorsTable;
    }

    public RichTable getDirectorsTable() {
        return directorsTable;
    }

    public void setBtnNewDirector(RichCommandButton btnNewDirector) {
        this.btnNewDirector = btnNewDirector;
    }

    public RichCommandButton getBtnNewDirector() {
        return btnNewDirector;
    }


    public RichInputDate getTxtDirectorDateOfBirth() {
        return txtDirectorDateOfBirth;
    }


    public void setTxtDirectorPhoneNo(RichInputText txtDirectorPhoneNo) {
        this.txtDirectorPhoneNo = txtDirectorPhoneNo;
    }

    public RichInputText getTxtDirectorPhoneNo() {
        return txtDirectorPhoneNo;
    }

    public void setTxtDirectorMeansOfId(RichSelectOneChoice txtDirectorMeansOfId) {
        this.txtDirectorMeansOfId = txtDirectorMeansOfId;
    }

    public RichSelectOneChoice getTxtDirectorMeansOfId() {
        return txtDirectorMeansOfId;
    }

    public void setTxtDirectorTaxNo(RichInputText txtDirectorTaxNo) {
        this.txtDirectorTaxNo = txtDirectorTaxNo;
    }

    public RichInputText getTxtDirectorTaxNo() {
        return txtDirectorTaxNo;
    }

    public void setTxtDirectorPctHoldg(RichInputText txtDirectorPctHoldg) {
        this.txtDirectorPctHoldg = txtDirectorPctHoldg;
    }

    public RichInputText getTxtDirectorPctHoldg() {
        return txtDirectorPctHoldg;
    }

    public void setTxtClientSourceOfIncome(RichSelectOneChoice txtClientSourceOfIncome) {
        this.txtClientSourceOfIncome = txtClientSourceOfIncome;
    }

    public RichSelectOneChoice getTxtClientSourceOfIncome() {
        return txtClientSourceOfIncome;
    }

    public void setTxtClientPlaceOfBirth(RichInputText txtClientPlaceOfBirth) {
        this.txtClientPlaceOfBirth = txtClientPlaceOfBirth;
    }

    public RichInputText getTxtClientPlaceOfBirth() {
        return txtClientPlaceOfBirth;
    }

    public void setTxtClientMeansOfId(RichSelectOneChoice txtClientMeansOfId) {
        this.txtClientMeansOfId = txtClientMeansOfId;
    }

    public RichSelectOneChoice getTxtClientMeansOfId() {
        return txtClientMeansOfId;
    }

    public void setTxtClientUtilityBill(RichInputText txtClientUtilityBill) {
        this.txtClientUtilityBill = txtClientUtilityBill;
    }

    public RichInputText getTxtClientUtilityBill() {
        return txtClientUtilityBill;
    }

    public void setTxtClientMeansOfIdVal(RichInputText txtClientMeansOfIdVal) {
        this.txtClientMeansOfIdVal = txtClientMeansOfIdVal;
    }

    public RichInputText getTxtClientMeansOfIdVal() {
        return txtClientMeansOfIdVal;
    }

    public void setTxtDirectorMeansOfIdVal(RichInputText txtDirectorMeansOfIdVal) {
        this.txtDirectorMeansOfIdVal = txtDirectorMeansOfIdVal;
    }

    public RichInputText getTxtDirectorMeansOfIdVal() {
        return txtDirectorMeansOfIdVal;
    }

    public void setTxtSignatoryMeansOfIdVal(RichInputText txtSignatoryMeansOfIdVal) {
        this.txtSignatoryMeansOfIdVal = txtSignatoryMeansOfIdVal;
    }

    public RichInputText getTxtSignatoryMeansOfIdVal() {
        return txtSignatoryMeansOfIdVal;
    }

    public void setTxtClientMeansOfIdDateIssued(RichInputDate txtClientMeansOfIdDateIssued) {
        this.txtClientMeansOfIdDateIssued = txtClientMeansOfIdDateIssued;
    }

    public RichInputDate getTxtClientMeansOfIdDateIssued() {
        return txtClientMeansOfIdDateIssued;
    }

    public void setTxtClientMeansOfIdIssuedBy(RichInputText txtClientMeansOfIdIssuedBy) {
        this.txtClientMeansOfIdIssuedBy = txtClientMeansOfIdIssuedBy;
    }

    public RichInputText getTxtClientMeansOfIdIssuedBy() {
        return txtClientMeansOfIdIssuedBy;
    }

    public void setTxtClientNationalityCode(RichSelectOneChoice txtClientNationalityCode) {
        this.txtClientNationalityCode = txtClientNationalityCode;
    }

    public RichSelectOneChoice getTxtClientNationalityCode() {
        return txtClientNationalityCode;
    }


    public void setRenderer(Rendering renderer) {
        this.renderer = renderer;
    }

    public Rendering getRenderer() {
        return renderer;
    }

    public void setIprsApi(IprsSetupsApi iprsApi) {
        this.iprsApi = iprsApi;
    }

    public IprsSetupsApi getIprsApi() {
        return iprsApi;
    }

    public void setClientMeansOfIdPan(RichPanelLabelAndMessage clientMeansOfIdPan) {
        this.clientMeansOfIdPan = clientMeansOfIdPan;
    }

    public RichPanelLabelAndMessage getClientMeansOfIdPan() {
        return clientMeansOfIdPan;
    }

    public void setNationalities(List<SelectItem> nationalities) {
        this.nationalities = nationalities;
    }

    public void setTxtClientMeansOfIdDateExpired(RichInputDate txtClientMeansOfIdDateExpired) {
        this.txtClientMeansOfIdDateExpired = txtClientMeansOfIdDateExpired;
    }

    public RichInputDate getTxtClientMeansOfIdDateExpired() {
        return txtClientMeansOfIdDateExpired;
    }

    public void setTxtClientMeansOfIdIssuingCountry(RichSelectOneChoice txtClientMeansOfIdIssuingCountry) {
        this.txtClientMeansOfIdIssuingCountry =
                txtClientMeansOfIdIssuingCountry;
    }

    public RichSelectOneChoice getTxtClientMeansOfIdIssuingCountry() {
        return txtClientMeansOfIdIssuingCountry;
    }

    public void setIncomes(List<SelectItem> incomes) {
        this.incomes = incomes;
    }

    public void setTxtSignatorySourceofincome(RichSelectOneChoice txtSignatorySourceofincome) {
        this.txtSignatorySourceofincome = txtSignatorySourceofincome;
    }

    public RichSelectOneChoice getTxtSignatorySourceofincome() {
        return txtSignatorySourceofincome;
    }

    public void setIncomes1(List<SelectItem> incomes1) {
        this.incomes1 = incomes1;
    }

    public void setSignaoccupationTbl(RichTable signaoccupationTbl) {
        this.signaoccupationTbl = signaoccupationTbl;
    }

    public RichTable getSignaoccupationTbl() {
        return signaoccupationTbl;
    }

    public void setTblSignTitles(RichTable tblSignTitles) {
        this.tblSignTitles = tblSignTitles;
    }

    public RichTable getTblSignTitles() {
        return tblSignTitles;
    }

    public void setTxtSelectSignTitle(RichCommandButton txtSelectSignTitle) {
        this.txtSelectSignTitle = txtSelectSignTitle;
    }

    public RichCommandButton getTxtSelectSignTitle() {
        return txtSelectSignTitle;
    }

    public void setSignTitlePan(RichPanelLabelAndMessage signTitlePan) {
        this.signTitlePan = signTitlePan;
    }

    public RichPanelLabelAndMessage getSignTitlePan() {
        return signTitlePan;
    }

    public void setTxtSignatoryTitleCode(RichInputText txtSignatoryTitleCode) {
        this.txtSignatoryTitleCode = txtSignatoryTitleCode;
    }

    public RichInputText getTxtSignatoryTitleCode() {
        return txtSignatoryTitleCode;
    }

    public void setTblCountryPop367(RichTable tblCountryPop367) {
        this.tblCountryPop367 = tblCountryPop367;
    }

    public RichTable getTblCountryPop367() {
        return tblCountryPop367;
    }

    public void setTxtSignatoryNationalityCode(RichInputText txtSignatoryNationalityCode) {
        this.txtSignatoryNationalityCode = txtSignatoryNationalityCode;
    }

    public RichInputText getTxtSignatoryNationalityCode() {
        return txtSignatoryNationalityCode;
    }

    public void setTxtSignatoryCountry1(RichInputText txtSignatoryCountry1) {
        this.txtSignatoryCountry1 = txtSignatoryCountry1;
    }

    public RichInputText getTxtSignatoryCountry1() {
        return txtSignatoryCountry1;
    }

    public void setTxtSelectCountry1(RichCommandButton txtSelectCountry1) {
        this.txtSelectCountry1 = txtSelectCountry1;
    }

    public RichCommandButton getTxtSelectCountry1() {
        return txtSelectCountry1;
    }

    public void setTxtSignatoryState1(RichInputText txtSignatoryState1) {
        this.txtSignatoryState1 = txtSignatoryState1;
    }

    public RichInputText getTxtSignatoryState1() {
        return txtSignatoryState1;
    }

    public void setTxtSelectState1(RichCommandButton txtSelectState1) {
        this.txtSelectState1 = txtSelectState1;
    }

    public RichCommandButton getTxtSelectState1() {
        return txtSelectState1;
    }

    public void setTxtSignatoryTown(RichInputText txtSignatoryTown) {
        this.txtSignatoryTown = txtSignatoryTown;
    }

    public RichInputText getTxtSignatoryTown() {
        return txtSignatoryTown;
    }

    public void setTxtSelectSignTown(RichCommandButton txtSelectSignTown) {
        this.txtSelectSignTown = txtSelectSignTown;
    }

    public RichCommandButton getTxtSelectSignTown() {
        return txtSelectSignTown;
    }

    public void setTblBirthCountry(RichTable tblBirthCountry) {
        this.tblBirthCountry = tblBirthCountry;
    }

    public RichTable getTblBirthCountry() {
        return tblBirthCountry;
    }

    public void setActionSelectBirthCountry(RichCommandButton actionSelectBirthCountry) {
        this.actionSelectBirthCountry = actionSelectBirthCountry;
    }

    public RichCommandButton getActionSelectBirthCountry() {
        return actionSelectBirthCountry;
    }

    public void setActionSelectBirthSate(RichCommandButton actionSelectBirthSate) {
        this.actionSelectBirthSate = actionSelectBirthSate;
    }

    public RichCommandButton getActionSelectBirthSate() {
        return actionSelectBirthSate;
    }

    public void setTblBirthSate(RichTable tblBirthSate) {
        this.tblBirthSate = tblBirthSate;
    }

    public RichTable getTblBirthSate() {
        return tblBirthSate;
    }

    public void setTblTownPOBPop(RichTable tblTownPOBPop) {
        this.tblTownPOBPop = tblTownPOBPop;
    }

    public RichTable getTblTownPOBPop() {
        return tblTownPOBPop;
    }

    public void setDirTitlePan(RichPanelLabelAndMessage dirTitlePan) {
        this.dirTitlePan = dirTitlePan;
    }

    public RichPanelLabelAndMessage getDirTitlePan() {
        return dirTitlePan;
    }

    public void setTxtSelectDirTitle(RichCommandButton txtSelectDirTitle) {
        this.txtSelectDirTitle = txtSelectDirTitle;
    }

    public RichCommandButton getTxtSelectDirTitle() {
        return txtSelectDirTitle;
    }

    public void setTblDirTitles(RichTable tblDirTitles) {
        this.tblDirTitles = tblDirTitles;
    }

    public RichTable getTblDirTitles() {
        return tblDirTitles;
    }

    public void setTxtDirectorTitleCode(RichInputText txtDirectorTitleCode) {
        this.txtDirectorTitleCode = txtDirectorTitleCode;
    }

    public RichInputText getTxtDirectorTitleCode() {
        return txtDirectorTitleCode;
    }


    public void setTxtDirectorDateOfBirth(RichInputDate txtDirectorDateOfBirth) {
        this.txtDirectorDateOfBirth = txtDirectorDateOfBirth;
    }

    public void setTxtDirectorSourceOfIncome(RichSelectOneChoice txtDirectorSourceOfIncome) {
        this.txtDirectorSourceOfIncome = txtDirectorSourceOfIncome;
    }

    public RichSelectOneChoice getTxtDirectorSourceOfIncome() {
        return txtDirectorSourceOfIncome;
    }


    public void setIncomesDir(List<SelectItem> incomesDir) {
        this.incomesDir = incomesDir;
    }

    public void setTblDirOccup(RichTable tblDirOccup) {
        this.tblDirOccup = tblDirOccup;
    }

    public RichTable getTblDirOccup() {
        return tblDirOccup;
    }

    public void setTxtDirectorOccupation(RichInputText txtDirectorOccupation) {
        this.txtDirectorOccupation = txtDirectorOccupation;
    }

    public RichInputText getTxtDirectorOccupation() {
        return txtDirectorOccupation;
    }

    public void setPanDirgOccupation(RichPanelLabelAndMessage panDirgOccupation) {
        this.panDirgOccupation = panDirgOccupation;
    }

    public RichPanelLabelAndMessage getPanDirgOccupation() {
        return panDirgOccupation;
    }

    public void setPanMsgNationality(RichPanelLabelAndMessage panMsgNationality) {
        this.panMsgNationality = panMsgNationality;
    }

    public RichPanelLabelAndMessage getPanMsgNationality() {
        return panMsgNationality;
    }

    public void setTblDirNationality(RichTable tblDirNationality) {
        this.tblDirNationality = tblDirNationality;
    }

    public RichTable getTblDirNationality() {
        return tblDirNationality;
    }

    public void setTxtDirtoryCountry1(RichInputText txtDirtoryCountry1) {
        this.txtDirtoryCountry1 = txtDirtoryCountry1;
    }

    public RichInputText getTxtDirtoryCountry1() {
        return txtDirtoryCountry1;
    }

    public void setSelectCountryDir(RichCommandButton SelectCountryDir) {
        this.SelectCountryDir = SelectCountryDir;
    }

    public RichCommandButton getSelectCountryDir() {
        return SelectCountryDir;
    }

    public void setTxtDirtoryState(RichInputText txtDirtoryState) {
        this.txtDirtoryState = txtDirtoryState;
    }

    public RichInputText getTxtDirtoryState() {
        return txtDirtoryState;
    }

    public void setSelectStateDir(RichCommandButton SelectStateDir) {
        this.SelectStateDir = SelectStateDir;
    }

    public RichCommandButton getSelectStateDir() {
        return SelectStateDir;
    }

    public void setTxtDirtoryTownDir(RichInputText txtDirtoryTownDir) {
        this.txtDirtoryTownDir = txtDirtoryTownDir;
    }

    public RichInputText getTxtDirtoryTownDir() {
        return txtDirtoryTownDir;
    }

    public void setSelectTownDir(RichCommandButton SelectTownDir) {
        this.SelectTownDir = SelectTownDir;
    }

    public RichCommandButton getSelectTownDir() {
        return SelectTownDir;
    }

    public void setTblBirthCountryDIR(RichTable tblBirthCountryDIR) {
        this.tblBirthCountryDIR = tblBirthCountryDIR;
    }

    public RichTable getTblBirthCountryDIR() {
        return tblBirthCountryDIR;
    }

    public void setTblBirthSateDir(RichTable tblBirthSateDir) {
        this.tblBirthSateDir = tblBirthSateDir;
    }

    public RichTable getTblBirthSateDir() {
        return tblBirthSateDir;
    }

    public void setTxtCltOccupation(RichInputText txtCltOccupation) {
        this.txtCltOccupation = txtCltOccupation;
    }

    public RichInputText getTxtCltOccupation() {
        return txtCltOccupation;
    }

    public void setPanMsgOccupationSign(RichPanelLabelAndMessage panMsgOccupationSign) {
        this.panMsgOccupationSign = panMsgOccupationSign;
    }

    public RichPanelLabelAndMessage getPanMsgOccupationSign() {
        return panMsgOccupationSign;
    }

    public void setTblTownPOBPopdir(RichTable tblTownPOBPopdir) {
        this.tblTownPOBPopdir = tblTownPOBPopdir;
    }

    public RichTable getTblTownPOBPopdir() {
        return tblTownPOBPopdir;
    }

    public void setTxtClntPOBCountry(RichInputText txtClntPOBCountry) {
        this.txtClntPOBCountry = txtClntPOBCountry;
    }

    public RichInputText getTxtClntPOBCountry() {
        return txtClntPOBCountry;
    }

    public void setTxtClntPOBState(RichInputText txtClntPOBState) {
        this.txtClntPOBState = txtClntPOBState;
    }

    public RichInputText getTxtClntPOBState() {
        return txtClntPOBState;
    }

    public void setTxtClntPOBTown(RichInputText txtClntPOBTown) {
        this.txtClntPOBTown = txtClntPOBTown;
    }

    public RichInputText getTxtClntPOBTown() {
        return txtClntPOBTown;
    }

    public void setSelectClntPOBCountry(RichCommandButton selectClntPOBCountry) {
        this.selectClntPOBCountry = selectClntPOBCountry;
    }

    public RichCommandButton getSelectClntPOBCountry() {
        return selectClntPOBCountry;
    }

    public void setSelectClntPOBState(RichCommandButton selectClntPOBState) {
        this.selectClntPOBState = selectClntPOBState;
    }

    public RichCommandButton getSelectClntPOBState() {
        return selectClntPOBState;
    }

    public void setSelectClntPOBTown(RichCommandButton selectClntPOBTown) {
        this.selectClntPOBTown = selectClntPOBTown;
    }

    public RichCommandButton getSelectClntPOBTown() {
        return selectClntPOBTown;
    }

    public void setTblClntPOBCountry(RichTable tblClntPOBCountry) {
        this.tblClntPOBCountry = tblClntPOBCountry;
    }

    public RichTable getTblClntPOBCountry() {
        return tblClntPOBCountry;
    }

    public void setTblClntPOBState(RichTable tblClntPOBState) {
        this.tblClntPOBState = tblClntPOBState;
    }

    public RichTable getTblClntPOBState() {
        return tblClntPOBState;
    }

    public void setTblClntPOBTown(RichTable tblClntPOBTown) {
        this.tblClntPOBTown = tblClntPOBTown;
    }

    public RichTable getTblClntPOBTown() {
        return tblClntPOBTown;
    }

    public void setPobClntHolder(RichPanelLabelAndMessage pobClntHolder) {
        this.pobClntHolder = pobClntHolder;
    }

    public RichPanelLabelAndMessage getPobClntHolder() {
        return pobClntHolder;
    }

    public void setTxtClientSourceOfIncomeDet(RichInputText txtClientSourceOfIncomeDet) {
        this.txtClientSourceOfIncomeDet = txtClientSourceOfIncomeDet;
    }

    public RichInputText getTxtClientSourceOfIncomeDet() {
        return txtClientSourceOfIncomeDet;
    }

    public void setBtnSaveIncomeSource(RichCommandButton btnSaveIncomeSource) {
        this.btnSaveIncomeSource = btnSaveIncomeSource;
    }

    public RichCommandButton getBtnSaveIncomeSource() {
        return btnSaveIncomeSource;
    }

    public void setBtnEditSIncomeSource(RichCommandButton btnEditSIncomeSource) {
        this.btnEditSIncomeSource = btnEditSIncomeSource;
    }

    public RichCommandButton getBtnEditSIncomeSource() {
        return btnEditSIncomeSource;
    }

    public void setIncomesIS(List<SelectItem> incomesIS) {
        this.incomesIS = incomesIS;
    }

    public List<SelectItem> getIncomesIS() {
        return incomesIS;
    }

    public void setTxtSourceofIncomeC(RichSelectOneChoice txtSourceofIncomeC) {
        this.txtSourceofIncomeC = txtSourceofIncomeC;
    }

    public RichSelectOneChoice getTxtSourceofIncomeC() {
        return txtSourceofIncomeC;
    }

    public void setTxtIncomeSourceType(RichInputText txtIncomeSourceType) {
        this.txtIncomeSourceType = txtIncomeSourceType;
    }

    public RichInputText getTxtIncomeSourceType() {
        return txtIncomeSourceType;
    }

    public void setBtnNEWIncomeSource(RichCommandButton btnNEWIncomeSource) {
        this.btnNEWIncomeSource = btnNEWIncomeSource;
    }

    public RichCommandButton getBtnNEWIncomeSource() {
        return btnNEWIncomeSource;
    }

    public void setBtnDeleteIncomeSource(RichCommandButton btnDeleteIncomeSource) {
        this.btnDeleteIncomeSource = btnDeleteIncomeSource;
    }

    public RichCommandButton getBtnDeleteIncomeSource() {
        return btnDeleteIncomeSource;
    }

    public void setTabSourcesOfIncome(RichShowDetailItem tabSourcesOfIncome) {
        this.tabSourcesOfIncome = tabSourcesOfIncome;
    }

    public RichShowDetailItem getTabSourcesOfIncome() {
        return tabSourcesOfIncome;
    }

    public void setBtnRefreshIncomeSource(RichCommandButton btnRefreshIncomeSource) {
        this.btnRefreshIncomeSource = btnRefreshIncomeSource;
    }

    public RichCommandButton getBtnRefreshIncomeSource() {
        return btnRefreshIncomeSource;
    }

    public void setTblClntIncomeSource(RichTable tblClntIncomeSource) {
        this.tblClntIncomeSource = tblClntIncomeSource;
    }

    public RichTable getTblClntIncomeSource() {
        return tblClntIncomeSource;
    }


    /**
     * Controls which tabs get shown necessary to perform the task at hand.
     */
    public enum mode {
        /**
         * Shows only the tabs with the relevant information to allow for editing.
         */
        EDIT_MODE,

        /**
         * Shows only the bare minimum tabs to allow a new client to be created
         */
        NEW_MODE,

        /**
         * Shows the Search Client tab as the default tab.
         */
        DEFAULT_MODE;
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
                CSVManip.uploadClientDocuments(uploadedFile.getInputStream(),
                                               _file.getFilename(), mimeType);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
            GlobalCC.refreshUI(requiredDocsBox);
        }

    }

    public String generateDoc() {
        Object key2 = docTbl.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)key2;
        if (r == null) {
            GlobalCC.errorValueNotEntered("No Record Selected");
            return null;
        }
        String id = (String)r.getAttribute("id");
        String[] ids = id.split(";");
        String finalId = "";
        if (ids.length > 1) {
            finalId = ids[0];
        } else
            finalId = id;

        Connection conn = null;
        String url = "";
        String user = "";
        String pass = "";
        DBConnector datahandler = new DBConnector();
        conn = datahandler.getDatabaseConnection();
        CallableStatement cstSections = null;
        ResultSet sectionsRS = null;
        try {
            cstSections =
                    conn.prepareCall("begin ? := TQC_SETUPS_CURSOR.get_system_ecm_setups(?); end;");
            cstSections.registerOutParameter(1,
                                             oracle.jdbc.OracleTypes.CURSOR);
            cstSections.setInt(2, 0);
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
                user + "&password=" + pass + "&docbase=Company Home&docId=" +
                finalId;
            System.out.println(toPrint);
            session.setAttribute("toPrint", toPrint);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" + "pt1:p201" +
                                 "').show(hints);");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, cstSections, sectionsRS);
        }
        return null;

    }

    public String viewDoc() {
        Object key2 = tblRequiredDocs.getSelectedRowData();
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

    public String refreshDocs() {
        ADFUtils.findIterator("enquireDocsIterator").executeQuery();
        GlobalCC.refreshUI(docTbl);
        return null;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
        System.out.println(this.uploadedFile);
        this.filename = uploadedFile.getFilename();
        System.out.println(this.filename);
        this.filesize = uploadedFile.getLength();
        System.out.println(this.filesize);
        this.filetype = uploadedFile.getContentType();
        System.out.println(this.filetype);

    }

    public String searchCountry() {
        try {
            session.setAttribute("txtSearchCountry",
                                 txtSearchCountry.getValue());
            ADFUtils.findIterator("fetchAllCountriesInfoIterator").executeQuery();
            GlobalCC.refreshUI(tblCountryPop);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public String searchEmpCountry() {
        try {
            session.setAttribute("txtSearchCountry",
                                 txtSearchEmpCountry.getValue());
            ADFUtils.findIterator("fetchAllCountriesInfoIterator").executeQuery();
            GlobalCC.refreshUI(tblEmpCountryPop);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public void refreshTabClientInfo(ValueChangeEvent valueChangeEvent) {
        //System.out.println("tabClientInfo ok!");
        GlobalCC.refreshUI(tabClientInfo);
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void onPageLoad(PhaseEvent phaseEvent) {
        String source =
            GlobalCC.checkNullValues(session.getAttribute("source"));
        String clientCode =
            GlobalCC.checkNullValues(session.getAttribute("ClientCode"));
        String viewMyticket =
            GlobalCC.checkNullValues(session.getAttribute("viewMyticket"));

        session.removeAttribute("viewMyticket");
        session.removeAttribute("source");

        if ("Y".equals(viewMyticket) &&
            clientCode != null) { // //  from HOME TICKETS
            actionLoadByClientCode();
        } else {
            if ("GisNewClient".equals(source)) { // "clntCode" from crm session
                actionNewClient();
            }
        }

        String leftWidth = "15.0%";
        if (session.getAttribute("back_url") != null) {
            leftWidth = "1.0%";
        }
        //ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExtendedRenderKitService service =
            Service.getRenderKitService(facesContext,
                                        ExtendedRenderKitService.class);

        service.addScript(facesContext,
                          "{var lftPanel = document.getElementById('doyeenLeftPanel');if(lftPanel!=undefined)lftPanel.width='" +
                          leftWidth + "';}");

    }

    public String redirectToClientsJSPX() {
        try {
            session.setAttribute("source", "GisNewClient");
            String url =
                renderer.getCrmUrl() + "clients.jspx?sn=" + renderer.getSessionUrl();
            System.out.println("url: " + url);
            ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(url);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    public void changeClientAccountStatus(ValueChangeEvent valueChangeEvent) {
        session.setAttribute("client_account_status",
                             txtClientAccStatus.getValue());
        GlobalCC.refreshUI(dlgClientAccount);
    }

    public String actionAcceptCurrency() {
        Object key2 = tblCurrencyPop.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {
            session.setAttribute("BACT_CUR_CODE", n.getAttribute("code"));
            txtBACT_CURRENCY.setValue(n.getAttribute("description"));
        }

        GlobalCC.refreshUI(txtBACT_CURRENCY);
        GlobalCC.hidePopup("pt1:currenciesPop");
        return null;
    }

    public String actionNewBankAccount() {

        try {

            if (session.getAttribute("bactAccCode") != null) {


                session.setAttribute("BACT_ACCOUNT_TYPE", "C");
                session.setAttribute("bankAccountAction", "A");
                session.setAttribute("BACT_CODE", null);
                session.setAttribute("BACT_BBR_CODE", null);
                session.setAttribute("BACT_CUR_CODE", null);
                session.setAttribute("bactUserCode", null);
                txtBACT_ACCOUNT_NO.setValue(null);

                if (txtSurname.getValue() != null) {
                    txtBACT_NAME.setValue(txtSurname.getValue());
                } else {
                    txtBACT_NAME.setValue(null);
                }
                txtBACT_CURRENCY.setValue(null);
                txtBACT_ACCOUNT_NO.setValue(null);
                txtBACT_ACC_OFFICER.setValue(null);
                txtBACT_STATUS.setValue("A");
                txtBACT_DEFAULT.setValue("N");
                txtBACT_CELL_NOS.setValue(null);
                txtBACT_TEL_NOS.setValue(null);
                txtBACT_BANK_BRANCH.setValue(null);
                txtBnkIBAN.setValue(null);
                txtBACT_NAME.setValue(null);

                saveBankDetailsBtn.setText("Save");

                GlobalCC.showPopup("pt1:BankDetailsPop");
            } else {
                GlobalCC.INFORMATIONREPORTING("No client Selected::");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String actionEditBankAccount() {

        Object key2 = tblBankAccounts.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;
        if (n != null) {

            session.setAttribute("bankAccountAction", "E");
            session.setAttribute("BACT_CODE", n.getAttribute("BACT_CODE"));
            BigDecimal accCode =
                GlobalCC.checkBDNullValues(session.getAttribute("bactAccCode"));
            session.setAttribute("BACT_ACC_CODE", accCode);
            session.setAttribute("BACT_BBR_CODE",
                                 n.getAttribute("BACT_BBR_CODE"));
            session.setAttribute("BACT_CUR_CODE",
                                 n.getAttribute("BACT_CUR_CODE"));
            session.setAttribute("bactUserCode",
                                 n.getAttribute("BACT_USER_CODE"));
            session.setAttribute("BACT_ACCOUNT_TYPE", "C");

            txtBACT_ACC_OFFICER.setValue(n.getAttribute("BACT_ACCOUNT_OFFICER"));
            txtBACT_ACCOUNT_NO.setValue(n.getAttribute("BACT_ACCOUNT_NO"));
            txtBACT_NAME.setValue(n.getAttribute("BACT_NAME"));
            txtBACT_DEFAULT.setValue(n.getAttribute("BACT_DEFAULT"));
            txtBACT_CELL_NOS.setValue(n.getAttribute("BACT_CELL_NOS"));
            txtBACT_TEL_NOS.setValue(n.getAttribute("BACT_TEL_NOS"));
            txtBACT_CURRENCY.setValue(n.getAttribute("BACT_CURRENCY"));
            txtBACT_BANK_BRANCH.setValue(n.getAttribute("BACT_BANK_BRANCH"));
            txtBnkIBAN.setValue(n.getAttribute("BACT_IBAN"));
            txtBACT_STATUS.setValue(n.getAttribute("BACT_STATUS"));


            saveBankDetailsBtn.setText("Update");

            GlobalCC.showPopup("pt1:BankDetailsPop");
            GlobalCC.refreshUI(bankDetailsPopUp);

        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String actionDeleteBankAccount() {
        Object key2 = tblBankAccounts.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            BigDecimal accCode =
                GlobalCC.checkBDNullValues(nodeBinding.getAttribute("BACT_CODE"));
            if (accCode == null) {
                GlobalCC.INFORMATIONREPORTING("No Record selected.");
                return null;
            }
            String query =
                "DELETE FROM TQC_BANK_ACCOUNTS WHERE BACT_CODE = " + accCode.toString() +
                " ";
            if (IQuery.exec(query)) {
                GlobalCC.INFORMATIONREPORTING("pt1:confirmDeleteClientAcc");
            }
            ;
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record selected.");
            return null;

        }
        ADFUtils.findIterator("fetchBankAccountsIterator").executeQuery();
        GlobalCC.refreshUI(tblBankAccounts);
        return null;
    }


    public String saveEmployerDetailsAction() {
        Connection conn = null;
        DBConnector dbHandler = new DBConnector();
        String query =
            "begin ? := tqc_clients_pkg.create_client_employer(" + "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?); end;";

        String action =
            GlobalCC.checkNullValues(session.getAttribute("clientEmployerAction"));
        BigDecimal code = GlobalCC.checkBDNullValues(clntEmplCode.getValue());
        BigDecimal clientCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
        BigDecimal stateCode =
            GlobalCC.checkBDNullValues(clntEmplStateCode.getValue());
        BigDecimal couCode =
            GlobalCC.checkBDNullValues(clntEmplCountryCode.getValue());
        BigDecimal townCode =
            GlobalCC.checkBDNullValues(clntEmplTwnCode.getValue());
        String postalZipCode =
            GlobalCC.checkNullValues(clntEmplTwnPostalZipCode.getValue());
        BigDecimal sectorCode =
            GlobalCC.checkBDNullValues(clntEmplSectorCode.getValue());
        String empType = GlobalCC.checkNullValues(clntEmplType.getValue());
        String empName = GlobalCC.checkNullValues(clntEmplName.getValue());
        String payrollNo =
            GlobalCC.checkNullValues(clntEmplPayrollNo.getValue());
        BigDecimal minSalary =
            GlobalCC.checkBDNullValues(clntEmplMinSalary.getValue());
        BigDecimal maxSalary =
            GlobalCC.checkBDNullValues(clntEmplMaxSalary.getValue());
        BigDecimal monthlySalary =
            GlobalCC.checkBDNullValues(clntEmplMonthlyIncome.getValue());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
        String strDate =
            GlobalCC.checkNullValues(clntEmplEmploymentDate.getValue());
        java.sql.Date empDate = null;
        try {
            empDate =
                    strDate == null ? null : new java.sql.Date(sdf.parse(strDate).getTime());
        } catch (Exception e) {
            empDate = null;
        }

        String empTelNos = GlobalCC.checkNullValues(clntEmplTelNos.getValue());
        String empCellNos =
            GlobalCC.checkNullValues(clntEmplCellNos.getValue());
        String empFax = GlobalCC.checkNullValues(clntEmplFax.getValue());


        FormUi formUi = new FormUi();
        if (!formUi.validate("employerDetailsTab")) {
            return null;
        }

        try {
            conn = dbHandler.getDatabaseConnection();
            OracleCallableStatement cstmt =
                (OracleCallableStatement)conn.prepareCall(query);

            cstmt.registerOutParameter(1, OracleTypes.VARCHAR);
            cstmt.setString(2, action);
            cstmt.setBigDecimal(3, code);
            cstmt.setBigDecimal(4, clientCode);
            cstmt.setBigDecimal(5, couCode);
            cstmt.setBigDecimal(6, stateCode);
            cstmt.setBigDecimal(7, townCode);
            cstmt.setString(8, postalZipCode);
            cstmt.setBigDecimal(9, sectorCode);
            cstmt.setString(10, empType);
            cstmt.setString(11, empName);
            cstmt.setString(12, payrollNo);
            cstmt.setBigDecimal(13, minSalary);
            cstmt.setBigDecimal(14, maxSalary);
            cstmt.setBigDecimal(15, monthlySalary);
            cstmt.setObject(16, empDate);
            cstmt.setString(17, empTelNos);
            cstmt.setString(18, empCellNos);
            cstmt.setString(19, empFax);
            cstmt.execute();


            String msg = cstmt.getString(1);


            if (msg != null && msg.toLowerCase().contains("successfully")) {
                conn.commit();
                cstmt.close();
                conn.close();

                ADFUtils.findIterator("fetchClientEmployersIterator").executeQuery();
                GlobalCC.refreshUI(tblEmployerDetails);

                GlobalCC.hidePopup("pt1:EmployerDetailsPop");
                GlobalCC.INFORMATIONREPORTING(msg);
            } else if (msg != null) {
                cstmt.close();
                conn.close();
                GlobalCC.INFORMATIONREPORTING(msg);
            } else {
                cstmt.close();
                conn.close();
                GlobalCC.INFORMATIONREPORTING("An Error has Occured.");
            }


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        return null;
    }


    public String saveBankDetailsAction() {
        Connection conn = null;
        CallableStatement stmt = null;
        String query = null;


        String bnkBrn =
            GlobalCC.checkNullValues(session.getAttribute("BACT_BBR_CODE"));
        String bnkAccOfficer =
            GlobalCC.checkNullValues(session.getAttribute("bactUserCode"));
        String bnkAccNumber =
            GlobalCC.checkNullValues(txtBACT_ACCOUNT_NO.getValue());
        String bnkAccName = GlobalCC.checkNullValues(txtBACT_NAME.getValue());
        String bnkTelNo = GlobalCC.checkNullValues(txtBACT_TEL_NOS.getValue());
        String bnkCellNo =
            GlobalCC.checkNullValues(txtBACT_CELL_NOS.getValue());
        String bnkCurrency =
            GlobalCC.checkNullValues(session.getAttribute("BACT_CUR_CODE"));
        String bnkDefault =
            GlobalCC.checkNullValues(txtBACT_DEFAULT.getValue());
        String bactAccountType =
            GlobalCC.checkNullValues(session.getAttribute("bactAccountType"));
        String bactAccCode =
            GlobalCC.checkNullValues(session.getAttribute("bactAccCode"));
        String bnkIBAN = GlobalCC.checkNullValues(txtBnkIBAN.getValue());
        String bnkStatus = GlobalCC.checkNullValues(txtBACT_STATUS.getValue());
        String v_bact_code = null;
        String option = null;
        Rendering renderer = new Rendering();

        if (renderer.isCLIENT_IBAN_REQUIRED() && bnkIBAN == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: IBAN is required!");
            return null;
        }

        if (saveBankDetailsBtn.getText().equalsIgnoreCase("Update")) {
            option = "E";
            Object key2 = tblBankAccounts.getSelectedRowData();
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


            BigDecimal ClientCode =
                GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
            BigDecimal taskId =
                GlobalCC.checkBDNullValues(session.getAttribute("taskID"));
            String activityName =
                GlobalCC.checkNullValues(session.getAttribute("activityName"));

            System.out.println("taskId: " + taskId);
            /* For Every Draft process i.e When you Update/Change any information about this Client, Create a ticket if none exist.
             * and the Client Exists
             * */
            if (ClientCode != null) {
                if (taskId == null) { //start new client work flow
                    TaskManipulation task = new TaskManipulation();
                    String TaskId = task.clientWorkFlow();
                }
                transitionToDraft();
            }
            actionLoadByClientCode();

            GlobalCC.INFORMATIONREPORTING("Bank Details Successfully Saved!");
            ADFUtils.findIterator("fetchBankAccountsIterator").executeQuery();

            GlobalCC.hidePopup("pt1:BankDetailsPop");

            GlobalCC.refreshUI(tblBankAccounts);
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        }
        return null;
    }


    public String getSmsNoFormat() {
        String val = (String)GlobalCC.getSysParamValue("SMS_NO_FORMAT");
        if (val == null) {
            val = "[0-9]";
        }
        return val;
    }


    public void validateSmsNos(FacesContext facesContext,
                               UIComponent uIComponent, Object object) {
        String smsFormat = getSmsNoFormat();

        if (object != null) {
            String name = object.toString();
            CharSequence inputStr = name;
            Pattern pattern = Pattern.compile(smsFormat);
            Matcher matcher = pattern.matcher(inputStr);
            String allowedDigitsCount = getSmsNoCountFromFormat(smsFormat);
            String msg =
                "Please check format.SMS No. Only " + allowedDigitsCount +
                " allowed";
            if (!matcher.matches()) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                              msg, null));
            }
        }
    }

    public String getSmsNoCountFromFormat(String format) {
        Scanner sc = new Scanner(format);
        String s = sc.findWithinHorizon("(?<=\\{).*?(?=\\})", 0);
        return s;
    }

    public String getTelNoFormat() {
        String val = (String)GlobalCC.getSysParamValue("TEL_NO_FORMAT");
        if (val == null) {
            val = "((\\\\+[1-9]{3,4}|0[1-9]{4}|00[1-9]{3})\\\\-?)?\\\\d{8,20}";
        }
        return val;
    }

    public String getTelNoErrMsg() {
        String val = (String)GlobalCC.getSysParamValue("TEL_NO_ERRMSG");
        if (val == null) {
            val = "Please check format.Tel No. Accepted formats : e.g \n" +
                    "    025712345678\n" +
                    "    0299999999\n";
            ;
        }
        return val;
    }

    public String getCellNoFormat() {
        String val = (String)GlobalCC.getSysParamValue("CELL_NO_FORMAT");
        if (val == null) {
            val = "((\\\\+[1-9]{3,4}|0[1-9]{4}|00[1-9]{3})\\\\-?)?\\\\d{8,20}";
        }
        return val;
    }

    public int getTelNoDigits() {
        String val = (String)GlobalCC.getSysParamValue("TEL_NO_DIGITS");
        if (val == null) {
            val = "10";
        }
        return Integer.parseInt(val);
    }

    public void validateTelNos(FacesContext facesContext,
                               UIComponent uIComponent, Object object) {
        if (object != null) {
            if ("AIICO".equalsIgnoreCase((String)session.getAttribute("CLIENT"))) {
                int telDigits = getTelNoDigits();
                String name = object.toString();
                String msg =
                    "Telphone No is not in Proper Format. Only (" + telDigits +
                    ") digits accepted";
                if (name.length() != telDigits) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  msg, null));
                }
            } else {
                String telFormat = getTelNoFormat();
                String msg = getTelNoErrMsg();
                String name = object.toString();
                CharSequence inputStr = name;
                Pattern pattern = Pattern.compile(telFormat);
                Matcher matcher = pattern.matcher(inputStr);

                if (!matcher.matches()) {
                    throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                  msg, null));
                }
            }
        }
    }

    public void actionLoadByClientCode() {
        MainClientDAO clientDao = new MainClientDAO();
        BigDecimal clientCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
        HashMap<String, String> m = null;
        Util checkClientAgency = new Util();
        String clientCheck = checkClientAgency.getClientAgencyTying();

        String site = GlobalCC.checkNullValues(session.getAttribute("CLIENT"));

        if (clientCode == null) {
            System.err.println("Client Code Undefined!");
            return; //please define ClientCode
        }
        DBConnector dbConnector = null;
        OracleCallableStatement stmt = null;
        Connection conn = null;
        OracleResultSet rs = null;
        OracleResultSet as = null;

        try {
            dbConnector = DBConnector.getInstance();
            conn = dbConnector.getDatabaseConnection();
            clientVaraibleInitialization(conn);
            String query1 =
                "begin ? := TQC_CLIENTS_PKG.getClientsAgent(?); end;";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, clientCode);

            stmt.execute();
            as = (OracleResultSet)stmt.getObject(1);
            int k = 0;
            while (as.next()) {
                txtAgencyCode.setValue(as.getBigDecimal(1));
                txtAgencyName.setValue(as.getString(3));
                k++;
            }
            if (k == 0) {
                txtAgencyCode.setValue(null);
                txtAgencyName.setValue(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, as);
        }
        rs = null;
        try {
            //please use the below line when pulling clients
            TurnQuest.view.models.Client client =
                clientDao.fetchClientByClientCode(clientCode);
            System.out.println("actionLoadByClientCode:" + client);
            //the below
            String query =
                "begin ? := tqc_clients_pkg.fetch_client_details(?); end;";

            conn = (OracleConnection)dbConnector.getDatabaseConnection();
            stmt = (OracleCallableStatement)conn.prepareCall(query);
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setBigDecimal(2, clientCode);
            stmt.execute();
            rs = (OracleResultSet)stmt.getObject(1);
            if (rs.next()) {
                txtReasonForUpdate.setValue(null);
                //--TO COMPLETE
                // To solve the session problem
                session.setAttribute("PayeeType", "C");
                hiddenClientCode.setValue(clientCode);
                txtClientCode.setValue(clientCode);
                session.setAttribute("clientCode", clientCode);
                session.setAttribute("uwclientCode", clientCode);
                session.setAttribute("clientPRPCode", clientCode);
                session.setAttribute("insuredCode", clientCode);
                session.setAttribute("bactAccCode", clientCode);
                session.setAttribute("CLNT_CODE", clientCode);

                session.setAttribute("bactAccountType", "C");

                legacyShtDesc.setValue(client.getOldShtDesc());

                String clientShtDesc =
                    GlobalCC.checkNullValues(client.getShtDesc());
                txtId.setValue(clientShtDesc);

                session.setAttribute("clientDesc", clientShtDesc);
                session.setAttribute("clientShtDesc", clientShtDesc);
                session.setAttribute("insuredShtDesc", clientShtDesc);

                String name =
                    client.getSurname() + " " + client.getOtherNames();


                name = GlobalCC.checkNullValues(name);
                if (name != null) {
                    name = GlobalCC.checkNullValues(client.getName());

                    name = name.replaceAll("null", "");
                }

                // FIXING Execption thrown while saving and updating
                String clntAgntClientId =
                    rs.getString("CLNT_AGNT_CLIENT_ID") == null ? "" :
                    rs.getString("CLNT_AGNT_CLIENT_ID").toString();
                String holdingCompany =
                    UtilDAO.fetchHoldingCompanyName(clntAgntClientId);
                txtHoldingCompany.setValue(holdingCompany);
                //end. FIXING Execption thrown while saving and updating

                //txtHoldingCompanyCode.setValue(rs.getString("holdCompanyCode"));
                txtFullNames.setValue((name + " " +
                                       client.getOtherNames()).replaceAll("null",
                                                                          ""));
                txtSurname.setValue(name);

                session.setAttribute("ClientFullname", name);
                session.setAttribute("insuredDesc", name);
                session.setAttribute("clientName", name);


                txtOtherNames.setValue(client.getOtherNames());

                session.setAttribute("clientIprsValidated",
                                     client.getIprsValidated());

                if (!renderer.isCLIENT_FULLNAME_DISABLED() &&
                    renderer.isCLIENT_FULLNAME_VISIBLE() && name != null) {
                    if (name.matches("[A-Za-z]+(\\s[A-Za-z]+){1,2}")) {
                        int s = name.trim().indexOf(" ");
                        if (s > 0) {
                            txtSurname.setValue((String)name.substring(0, s));
                            txtOtherNames.setValue((String)name.substring(s +
                                                                          1,
                                                                          name.length()));
                        }
                    }
                }

                session.setAttribute("bpnCode", client.getBpnCode());
                txtBussinessPerson.setValue(rs.getString("CLNT_BUSINESS"));


                String direct =
                    GlobalCC.checkNullValues(client.getDirectClient());
                System.out.println("directClient: " + direct);
                direct = direct != null ? direct.toUpperCase() : "C";
                direct =
                        ("B".equals(direct) || "C".equals(direct) || "I".equals(direct)) ?
                        direct : "C";
                System.out.println("directClient: " + direct);
                txtDirectClient.setValue(direct);


                if (direct != null) {
                    if (direct.equalsIgnoreCase("Y")) {
                        pnLabelAgency.setVisible(false);
                    } else {
                        if (clientCheck.equals("N") || clientCheck != null) {
                            pnLabelAgency.setVisible(false);
                        } else {
                            pnLabelAgency.setVisible(true);
                        }
                    }
                }

                txtStaffNo.setValue(client.getStaffNo());
                txtIdRegNum.setValue(client.getIdRegNo());
                txtEmail.setValue(client.getEmailAddrs());
                txtEmail2.setValue(client.getEmail2());
                txtClientTitleCode.setValue(client.getTitle());
                txtClientTitle.setValue(client.getTitle());
                txtPhone1.setValue(client.getTel());
                txtPIN.setValue(client.getPin());
                txtPassportNo.setValue(client.getPassportNo());
                txtClientCellNos.setValue(client.getCltCellNo());
                txtGender.setValue(client.getGender());

                ///////------------------CONTACTS-----------------------//////////
                txtContactName1.setValue(client.getCntctName1());
                contactName1.setValue(client.getCntctName1());
                txtContactPhone1.setValue(client.getCntctPhone1());
                contactPhone1.setValue(client.getCntctPhone1());
                txtContactEmail1.setValue(client.getCntctEmail1());
                contactEmail1.setValue(client.getCntctEmail1());
                txtContactName2.setValue(client.getCntctName2());
                contactName2.setValue(client.getCntctName2());
                txtContactPhone2.setValue(client.getCntctPhone2());
                contactPhone2.setValue(client.getCntctPhone2());
                txtContactEmail2.setValue(client.getCntctEmail2());
                contactEmail2.setValue(client.getCntctEmail2());
                //--TO COMPLETE
                txtPhysicalAddress.setValue(client.getPhysicalAddrs()); //Client Level

                String countryCode =
                    GlobalCC.checkNullValues(client.getCouCode());
                String country = UtilDAO.fetchCountryName(countryCode);
                txtCountryName.setValue(country);

                txtCountryCode.setValue(client.getCouCode());
                session.setAttribute("countryCode", countryCode);

                txtAdminRegionCode.setValue(client.getStsCode()); //County or State
                txtAdminRegionName.setValue(rs.getString("STS_NAME")); //County or State
                txtTownName.setValue(UtilDAO.fetchTownName(GlobalCC.checkNullValues(client.getTwnCode()))); //County or State
                txtTownCode.setValue(client.getTwnCode()); //County or State

                session.setAttribute("townCode", client.getTwnCode());

                txtCountryName2.setValue(rs.getString("SMS_COUNTRY")); //aLTERNATE EMAIL
                txtCountryCode2.setValue(client.getGsmZipCode()); //aLTERNATE EMAIL
                txtCountryName3.setValue(rs.getString("INT_COUNTRY")); //aLTERNATE EMAIL
                txtCountryCode3.setValue(client.getIntZipCode()); //aLTERNATE EMAIL

                String prefix = null;
                String suffix = null;
                String sms1 = GlobalCC.checkNullValues(client.getSmsTel());
                System.out.println("sms1: " + sms1);

                String szSuffix =
                    GlobalCC.getSysParamValue("SMS_NO_LOWER_LIMIT");
                int nSuffix = Integer.valueOf(szSuffix);
                System.out.println("nSuffiz: " + nSuffix);

                //sms1 ="+254700-456-789";sms1 ="0700-456-789";
                //sms ="+254700-456-789";sms ="0700-456-789";
                if (sms1 != null) {
                    sms1 = sms1.replaceAll("\\-", ""); //remove (-)
                    System.out.println("sms: " + sms1);
                    if (sms1.length() >= (nSuffix + 3)) {
                        int startOfSuffix = sms1.length() - nSuffix;
                        suffix = sms1.substring(startOfSuffix, sms1.length());
                        prefix =
                                "0" + sms1.substring(startOfSuffix - 3, startOfSuffix);
                    } else {
                        prefix = null;
                        suffix = null;
                    }
                }
                System.out.println("prefix1: " + prefix);
                System.out.println("suffix1: " + suffix);

                txtPrefixManager.setValue(prefix);
                txtSms.setValue(suffix);
                prefix = null;
                suffix = null;
                String sms2 = GlobalCC.checkNullValues(client.getTelPay());

                if (sms2 != null) {
                    sms2 = sms2.replaceAll("\\-", ""); //remove (-)
                    System.out.println("sms: " + sms2);
                    if (sms2.length() >= (nSuffix + 3)) {
                        int startOfSuffix = sms2.length() - nSuffix;
                        suffix = sms2.substring(startOfSuffix, sms2.length());
                        prefix =
                                "0" + sms2.substring(startOfSuffix - 3, startOfSuffix);
                    } else {
                        prefix = null;
                        suffix = null;
                    }
                }
                System.out.println("prefix2: " + prefix);
                System.out.println("suffix2: " + suffix);

                txtTelPayPrefix.setValue(prefix);
                txtPayTel.setValue(suffix);
                prefix = null;
                suffix = null;

                if (client != null) {
                    if ("AIICO".equalsIgnoreCase(site)) {
                        String intTel =
                            GlobalCC.checkNullValues(client.getIntTel());
                        String couZip =
                            GlobalCC.checkNullValues(client.getIntZipCode());
                        if (intTel != null) {
                            intTel = intTel.replaceFirst("\\+", "");
                            if (couZip != null && intTel.startsWith(couZip)) {
                                intTel = intTel.replaceFirst(couZip, "");
                            }
                            System.out.println("intTel: " + intTel);
                            System.out.println("couZip: " + couZip);
                        }
                        String intCountry = null;
                        if (couZip != null) {
                            intCountry =
                                    IQuery.fetchString("select cou_name from tqc_countries where cou_zip_code=" +
                                                       couZip);
                        }
                        txtIntTel.setValue(intTel);
                        txtCountryCode3.setValue(couZip);
                        txtCountryName3.setValue(intCountry);

                    }
                }


                txtSms.setValue(client.getSmsTel2());
                txtPayTel.setValue(client.getTelPay2());


                txtPostalAddress.setValue(client.getPostalAddrs());
                txtGender.setValue(client.getGender());
                txtDOB.setValue(client.getDob());


                txtZipCode.setValue(client.getZipCode());
                txtSurburbs.setValue(client.getZipCode());


                //--TO COMPLETE
                txtRemarks.setValue(client.getRemarks()); //Account No.
                txtDLNo.setValue(client.getDrivingLicence()); //Account No.
                txtDlIssueDate.setValue(client.getDlIssueDate()); //Account No.
                txtDrvExperience.setValue(client.getDrvExperience()); //Account No.

                txtAccountNum.setValue(client.getAccntNo()); //Account No.
                String clientLevel =
                    GlobalCC.checkNullValues(client.getClientLevel());
                txtClientLevel.setValue(clientLevel); //Client Level
                GlobalCC.refreshUI(txtClientLevel);
                System.out.println("CLNT_CLIENT_LEVEL" + clientLevel);
                txtBouncedCheque.setValue(client.getBouncedChq());

                txtWebsite.setValue(client.getWebsite()); //County or State
                txtWorkPermit.setValue(client.getWorkPermit()); //County or State
                txtDateCreated.setValue(client.getDateCreated()); //County or State
                txtEmail2.setValue(client.getEmail2()); //aLTERNATE EMAIL
                txtWef.setValue(client.getWef()); //aLTERNATE EMAIL
                txtWet.setValue(client.getWet()); //aLTERNATE EMAIL
                txtDivisionCode.setValue(client.getDivCode()); //aLTERNATE EMAIL
                txtDivisionName.setValue(client.getDivision()); //aLTERNATE EMAIL
                txtDomicileCountriesCode.setValue(client.getDomicileCountries()); //aLTERNATE EMAIL
                txtDomicileCountriesName.setValue(rs.getString("DOMICILE_COUNTRY")); //aLTERNATE EMAIL

                txtPreferedBenefitPaymentModeCode.setValue(client.getBenefitPaymentMode());
                txtPreferedBenefitPaymentMode.setValue(client.getPrefferedPaymode());
                txtPreferedPremiumPaymentCode.setValue(client.getPremiumPaymentMode());
                txtPreferedPremiumPayment.setValue(client.getPremiumPaymode());
                txtEduLevel.setValue(client.getEducationLevel());
                txtMonthlyIncome.setValue(client.getMonthlyIncome());

                ////////------txtStatus-----////////
                String status = GlobalCC.checkNullValues(client.getStatus());

                txtStatus.setValue(status);
                session.setAttribute("client_status", status);
                session.setAttribute("ClientStatus", status);


                if (status != null) {
                    if (status.equalsIgnoreCase("A")) {
                        selectStatusActive.setDisabled(false);
                        selectStatusInactive.setDisabled(false);
                    } else if (status.equalsIgnoreCase("I")) {
                        selectStatusInactive.setDisabled(false);
                    } else if (status.equalsIgnoreCase("R")) {
                        // CRM-1628 REquest (Karuga & Richard Mboya)
                        txtStatus.setValue("D");
                        selectStatusDraft.setDisabled(false);

                        // selectStatusReady.setDisabled(false);
                    } else if (status.equalsIgnoreCase("D")) {
                        selectStatusDraft.setDisabled(false);
                    }
                }

                txtMaritalStatus.setValue(client.getMaritalStatus()); //aLTERNATE EMAIL
                txtModeOfComm.setValue(client.getDefaultCommMode()); //aLTERNATE EMAIL
                txtAgencyCode.setValue(client.getAccOfficer()); //aLTERNATE EMAIL
                txtSectorName.setValue(UtilDAO.fetchSectorName((String)rs.getString("CLNT_SEC_CODE"))); //aLTERNATE EMAIL
                txtSectorCode.setValue(client.getSecCode()); //aLTERNATE EMAIL

                txtCltOccupation.setValue(client.getOccupation()); //aLTERNATE EMAIL
                session.setAttribute("occupationCode", client.getOccCode());
                session.setAttribute("occCode", client.getOccCode());

                txtSpecialization.setValue(client.getSpzCode()); //aLTERNATE EMAIL
                txtAnniversary.setValue(client.getAnniversary()); //aLTERNATE EMAIL
                txtFax.setValue(client.getFax()); //aLTERNATE EMAIL

                txtCreditAllowed.setValue(client.getCreditLimAllowed()); //Account No.
                txtCreditLimit.setValue(client.getCreditLimit()); //aLTERNATE EMAIL
                txtCreditRting.setValue(client.getCrdtLimit()); //aLTERNATE EMAIL
                txtSacco.setValue(client.getSacco());
                txtAccountManagerCode.setValue(client.getUsrCode()); //aLTERNATE EMAIL
                txtAccountManager.setValue(rs.getString("USR_NAME")); //aLTERNATE EMAIL

                txtPayroll.setValue(client.getPayrollNo());
                txtMinSalary.setValue(client.getSalMinRange());
                txtMaxSalary.setValue(client.getSalMaxRange());

                txtBankAccNum.setValue(client.getBankAccNo());
                txtCltBankCellNo.setValue(client.getBankCellNo());
                txtCltBankTelNo.setValue(client.getBankPhoneNo());

                txtBranchCode.setValue(client.getBbrCode());

                session.setAttribute("BACT_BBR_CODE",
                                     GlobalCC.checkNullValues(client.getBbrCode()));

                txtBranchName.setValue(rs.getString("BBR_BRANCH_NAME"));
                txtRegBranchCode.setValue(client.getBrnCode());
                txtRegBranchName.setValue(rs.getString("BRN_NAME"));

                txtProposer.setValue(client.getProposer());

                if ("Y".equalsIgnoreCase(client.getProposer())) {
                    txtCommandBtn.setDisabled(true);
                } else {
                    txtCommandBtn.setDisabled(false);
                }


                txtCltEmployerTelNo.setValue(client.getEmployerPhoneNo());
                txtCltEmployerCellNo.setValue(client.getEmployerCellNo());

                txtAccountOfficerCode.setValue(client.getUsrCode());
                txtClientSourceOfIncome.setValue(client.getSourceOfIncome());
                txtClientSourceOfIncomeDet.setValue(client.getSourceOfIncomeType());
                if (client.getSourceOfIncome() != null) {
                    if (client.getSourceOfIncome().equalsIgnoreCase("Inheritance")) {
                        txtClientSourceOfIncomeDet.setLabel("Type of Inheritance");
                    } else if (client.getSourceOfIncome().equalsIgnoreCase("Investment")) {
                        txtClientSourceOfIncomeDet.setLabel("Type of Investment");
                    } else if (client.getSourceOfIncome().equalsIgnoreCase("Own Business")) {
                        txtClientSourceOfIncomeDet.setLabel("Type/Name of Business");
                    } else if (client.getSourceOfIncome().equalsIgnoreCase("Salary")) {
                        txtClientSourceOfIncomeDet.setLabel("Name of Employer");
                    } else if (client.getSourceOfIncome().equalsIgnoreCase("Others")) {
                        txtClientSourceOfIncomeDet.setLabel("Specify");
                    }
                }
                txtClntPOBCountry.setValue(client.getPobCountry());
                txtClntPOBState.setValue(client.getPobState());
                txtClntPOBTown.setValue(client.getPobTown());
                txtAccountOfficerName.setValue(client.getAccOfficer());
                txtWithdrawalReason.setValue(client.getWithdrawalReason());
                txtSpecialTerms.setValue(client.getSpclTerms());
                txtAuditors.setValue(client.getAuditors());
                txtParentCompany.setValue(client.getParentCompany());
                txtInsurer.setValue(client.getCurrentInsurer());
                txtProjectedBiz.setValue(client.getProjectedBusiness());
                txtDateOfEmployment.setValue(client.getDateOfEmpl());
                txtParentCompanyCode.setValue(client.getParentCompany());
                txtCancelledPolicy.setValue(client.getPolicyCancelled());
                txtIncreasePremium.setValue(client.getIncreasedPremium());
                txtDeclinedProposal.setValue(client.getDeclinedProp());

                txtCreatedBy.setValue(client.getCreatedBy());


                txtPrefixManager.setValue(client.getSmsPrefix());
                txtTelPayPrefix.setValue(client.getSmsPrefix2());


                txtOldAccNO.setValue(client.getOldAccntNo());

                txtClientSourceOfIncome.setValue(client.getSourceOfIncome());
                txtClientPlaceOfBirth.setValue(client.getPlaceOfBirth());
                txtClientMeansOfId.setValue(client.getMeansOfId());
                txtClientMeansOfIdVal.setValue(client.getMeansOfIdVal());
                txtClientMeansOfIdDateIssued.setValue(client.getMeansOfIdDateIssued());
                txtClientMeansOfIdIssuedBy.setValue(client.getMeansOfIdIssuedBy());
                txtClientMeansOfIdDateExpired.setValue(client.getMeansOfIdDateExpired());
                txtClientMeansOfIdIssuingCountry.setValue(client.getMeansOfIdIssuingCountry());
                txtClientUtilityBill.setValue(client.getUtilityBill());

                String nationalityCode =
                    GlobalCC.checkNullValues(client.getNationalityCode());

                txtClientNationalityCode.setValue(nationalityCode);

                /* CLNT_BBR_CODE
                        CLNT_BANK_ACC_NO*/

                session.setAttribute("MarketeragentCode",
                                     GlobalCC.checkNullValues(client.getAccOfficer()));
                txtAccountManager.setValue(GlobalCC.checkNullValues(client.getAccOfficerName()));
                txtAccountManagerCode.setValue(GlobalCC.checkNullValues(client.getAccOfficer()));

                session.setAttribute("wbProductCode", null);
                session.setAttribute("WebuserCode", null);
                session.setAttribute("WebuserName", null);
                session.setAttribute("webProductCode", null);
                session.setAttribute("clientBankCode", null);

                clientSignature.setSource(null);
                clientPhoto.setSource(null);
                clientPhoto.setSource("/clientimagesservlet?id=" + clientCode);
                clientSignature.setSource("/clientsignatureservlet?id=" +
                                          clientCode);

                txtRunOff.setValue(client.getRunoff());

                ///////-----------------------------------------//////////

                String type = GlobalCC.checkNullValues(client.getType());
                System.out.println("typeVAl: " + type);
                session.setAttribute("typeVAl", type);

                // FIXING ERROR While searching client
                String clntyCode =
                    GlobalCC.checkNullValues(client.getClntyCode());
                // End. FIXING ERROR While searching client
                session.setAttribute("clntyCode", clntyCode);
                System.out.println("clntyCode" + clntyCode);
                txtClientTypes.setValue(clntyCode);

                ClientTypeChanged();


                // Change the save button text to update
                btnCreateUpdateClient.setDisabled(false);
                btnCreateUpdateClient.setText("Update");
                session.setAttribute("client_action", "E");

                ADFUtils.findIterator("fetchClientAccountsIterator").executeQuery();
                ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
                ADFUtils.findIterator("fetchClientWebAccountsIterator").executeQuery();
                ADFUtils.findIterator("fetchUnallocatedClientSystemsIterator").executeQuery();
                ADFUtils.findIterator("fetchAllocatedClientSystemsIterator").executeQuery();
                ADFUtils.findIterator("fetchClientRequiredDocsIterator").executeQuery();
                ADFUtils.findIterator("fetchClientWebAccountsIterator").executeQuery();
                ADFUtils.findIterator("findUnassignedCompaniesIterator").executeQuery();
                ADFUtils.findIterator("findassignedCompaniesIterator").executeQuery();
                ADFUtils.findIterator("findWebClientBranchesIterator").executeQuery();
                ADFUtils.findIterator("findWebProductsDetailsIterator").executeQuery();
                ADFUtils.findIterator("fetchAllBussinessPeopleIterator").executeQuery();

                tabClientSystems.setRendered(true);
                tabClientWebAccounts.setRendered(true);
                tabClientDocs.setRendered(true);
                tabDetailCreateClient.setDisclosed(true);
                tabDetailSearchClients.setDisclosed(false);
                tabDetailCreateClient.setVisible(true);
                tabClientAccounts.setVisible(true);

                // Now we are ready to show the client tab with all the required details
                switchClientTabs(mode.EDIT_MODE);

                GlobalCC.refreshUI(btnMakeReadyClient);
                GlobalCC.refreshUI(btnAuthorizeClient);
                GlobalCC.refreshUI(btnDeactivateClient);
                GlobalCC.refreshUI(btnCreateUpdateClient);

                GlobalCC.refreshUI(lblClientTabTitle);
                GlobalCC.refreshUI(tblRequiredDocs);
                GlobalCC.refreshUI(tblClientAccounts);
                GlobalCC.refreshUI(txtAgencyCode);
                GlobalCC.refreshUI(txtAgencyName);
                GlobalCC.refreshUI(tabDetailCreateClient);
                GlobalCC.refreshUI(treeUnassignedClientSystems);
                GlobalCC.refreshUI(treeAssignedClientSystems);
                GlobalCC.refreshUI(clientBranch);
                GlobalCC.refreshUI(unAssignedBank);
                GlobalCC.refreshUI(assignedBank);
                GlobalCC.refreshUI(webProductsDetails);
                GlobalCC.refreshUI(tblwebAccounts);
                GlobalCC.refreshUI(panelDetailSystems);
                GlobalCC.refreshUI(tabDetailCreateClient);
                GlobalCC.refreshUI(tabClientAccounts);
                GlobalCC.refreshUI(mainPanel);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }

    }
    /*   public String actionAuthorizeClient() {
         Authorization auth = new Authorization();
         String process = "CLCR";
         String processArea = "CLAU";
         String processSubArea = "CLAU";
         String AccessGranted =
             auth.checkUserRights(process, processArea, processSubArea, null,
                                  null);
         if (!AccessGranted.equalsIgnoreCase("Y")) {
             GlobalCC.INFORMATIONREPORTING("Sorry. You Do not Have The Rights to authorize a client.");
             return null;

         }
         if (session.getAttribute("ClientCode") != null) {

             //if (checkIfUserAlowedToAuthorize()) {
             String status = check_ifclientActive();
             System.out.println("status: " + status);
             if (status != null) {

                 if (status.equalsIgnoreCase("D") ||
                     (status.equalsIgnoreCase("R"))) {
                     DBConnector dbConnector = new DBConnector();
                     OracleConnection conn = null;


                     String Query =
                         "BEGIN TQC_CLIENTS_PKG.authorise_client(?,?,?);END;";
                     DBConnector connector = new DBConnector();
                     OracleCallableStatement cst = null;
                     try {
                         conn = connector.getDatabaseConnection();
                         cst = (OracleCallableStatement)conn.prepareCall(Query);

                         cst.registerOutParameter(1, OracleTypes.VARCHAR);
                         // Add or Edit
                         cst.setBigDecimal(2,
                                           new BigDecimal(session.getAttribute("ClientCode").toString())); // Client Code
                         cst.setString(3, "A");
                         cst.execute();
                         String err = cst.getString(1);
                         if (err != null) {
                             GlobalCC.INFORMATIONREPORTING(err);
                         } else {
                             selectStatusActive.setDisabled(false);
                             selectStatusDraft.setDisabled(false);
                             selectStatusInactive.setDisabled(false);
                             selectStatusRejected.setDisabled(false);
                             txtStatus.setValue("D");

                             String message =
                                 "Client   Authorised  Successfully!";
                             txtStatus.setValue("A");
                             GlobalCC.refreshUI(txtStatus);

                             GlobalCC.INFORMATIONREPORTING(message);
                         } //showMessagePopup(message);

                     } catch (Exception e) {
                         e.printStackTrace();
                         GlobalCC.EXCEPTIONREPORTING(conn, e);
                     }
                 } else {

                     if (status.equalsIgnoreCase("A")) {
                         GlobalCC.INFORMATIONREPORTING("The Client Status is Already Authorised::");
                         return null;
                     } else if (status.equalsIgnoreCase("I")) {
                         GlobalCC.INFORMATIONREPORTING("The Client Status is Inactive::You can only set it to Active");
                         return null;
                     }


                     else if (status.equalsIgnoreCase("R")) {
                         GlobalCC.INFORMATIONREPORTING("The Client status is  Rejected::");
                         return null;
                     }
                 }
             } else {
                 GlobalCC.INFORMATIONREPORTING("The Client Status is Not set::");
                 return null;
             }

         } else {
             GlobalCC.INFORMATIONREPORTING("Client Code Required::");
             return null;
         }

         return null;
     }*/

    public String actionAuthorizeClient() {

        Connection conn = null;

        CallableStatement cst = null;
        try {
            DBConnector datahandler = DBConnector.getInstance();
            //confirm that there is someone to perform the next task before completing.
            String Rights = null;
            session.setAttribute("ProcessShtDesc", GlobalCC.clientProcess);
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
                if (!wf.checkTaskCompletion(Taske, "ACLNT")) {
                    return null;
                }
            }

            conn = datahandler.getDatabaseConnection();
            String sql =
                "begin tqc_clients_pkg.client_status_prc(?,?,?,?); end;";
            cst = conn.prepareCall(sql);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("clientCode"));
            cst.setString(4, "A");
            cst.execute();
            conn.commit();
            conn.close();

            session.setAttribute("ClientStatus", "A");

            String success = null;
            if (Taske != null && ticketsApp) {
                success = wf.CompleteTask();
                String CurrTask =
                    GlobalCC.checkNullValues(session.getAttribute("taskID"));
                if (CurrTask != null) {
                    wf.closeTicket("C", CurrTask);
                }
                wf.clearProcessSession();
            }

            if ("success".equals(success) || !ticketsApp) {
                actionLoadByClientCode();
            }
            String Message = "Client Successfully Authorised!";
            GlobalCC.INFORMATIONREPORTING(Message);
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }


    public String actionMakeReadyClient() {

        Connection conn = null;

        CallableStatement cst = null;
        try {
            DBConnector datahandler = DBConnector.getInstance();
            //confirm that there is someone to perform the next task before completing.
            String Rights = null;
            session.setAttribute("ProcessShtDesc", GlobalCC.clientProcess);
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
                if (!wf.checkTaskCompletion(Taske, "MCLNT")) {
                    return null;
                }
            }


            conn = datahandler.getDatabaseConnection();
            String sql =
                "begin tqc_clients_pkg.client_status_prc(?,?,?,?); end;";
            cst = conn.prepareCall(sql);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("clientCode"));
            cst.setString(4, "R");
            cst.execute();
            conn.commit();
            conn.close();

            session.setAttribute("ClientStatus", "R");
            String success = null;
            if (Taske != null && ticketsApp) {
                success = wf.CompleteTask();
            }

            if ("success".equals(success) || !ticketsApp) {
                actionLoadByClientCode();
                String Message = "Client Successfully Make Ready!";
                GlobalCC.INFORMATIONREPORTING(Message);
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    public String actionDeactivateClient() {

        Connection conn = null;

        CallableStatement cst = null;
        try {
            DBConnector datahandler = DBConnector.getInstance();
            //confirm that there is someone to perform the next task before completing.
            String Rights = null;
            session.setAttribute("ProcessShtDesc", GlobalCC.clientProcess);
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
                if (!wf.checkTaskCompletion(Taske, "DCLNT")) {
                    return null;
                }
            }

            conn = datahandler.getDatabaseConnection();
            String sql =
                "begin tqc_clients_pkg.client_status_prc(?,?,?,?); end;";
            cst = conn.prepareCall(sql);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("clientCode"));
            cst.setString(4, "I");
            cst.execute();
            conn.commit();
            conn.close();

            session.setAttribute("ClientStatus", "I");

            String success = null;
            if (Taske != null && ticketsApp) {
                success = wf.CompleteTask();
            }

            if ("success".equals(success) || !ticketsApp) {
                actionLoadByClientCode();
                String Message = "Client Successfully Deativated!";
                GlobalCC.INFORMATIONREPORTING(Message);
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            DbUtils.closeQuietly(conn, cst, null);
        }
        return null;
    }

    /*public String actionReassignClient() {


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


    public boolean isShowStatusPanel() {

        String tickets = GlobalCC.getSysParamValue("TICKETS_APP");
        boolean show =
            session.getAttribute("ClientCode") != null && "Y".equals(tickets);
        return show;

    }

    public String actionNewClientEmployerDetails() {
        try {
            if (session.getAttribute("ClientCode") != null) {
                clearEmployerDetailsFields();
                session.setAttribute("clientEmployerAction", "A");

                saveEmployerDetailsBtn.setText("Save");
                GlobalCC.showPopup("pt1:EmployerDetailsPop");
                GlobalCC.refreshUI(employerDetailsPopup);
            } else {
                GlobalCC.INFORMATIONREPORTING("No client Selected::");
            }
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.INFORMATIONREPORTING(e.getMessage());
        }

        return null;
    }

    public String actionEditClientEmployerDetails() {
        Object key2 = tblEmployerDetails.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {

            session.setAttribute("clientEmployerAction", "E");

            txtSearchEmpCountry.setValue(null);
            session.setAttribute("txtSearchCountry", null);
            session.setAttribute("countryCode",
                                 nodeBinding.getAttribute("countryCode"));
            session.setAttribute("stateCode",
                                 nodeBinding.getAttribute("stateCode"));
            session.setAttribute("townCode",
                                 nodeBinding.getAttribute("townCode"));

            clntEmplCode.setValue(nodeBinding.getAttribute("code"));

            clntEmplCountryCode.setValue(nodeBinding.getAttribute("countryCode"));
            clntEmplCountryName.setValue(nodeBinding.getAttribute("countryName"));
            clntEmplTwnCode.setValue(nodeBinding.getAttribute("townCode"));
            clntEmplTwnName.setValue(nodeBinding.getAttribute("townName"));
            // clntEmplTwnPostalCode.setValue(nodeBinding.getAttribute("postalCode"));
            clntEmplTwnPostalZipCode.setValue(nodeBinding.getAttribute("postalZipCode"));
            clntEmplStateCode.setValue(nodeBinding.getAttribute("stateCode"));
            clntEmplState.setValue(nodeBinding.getAttribute("stateName"));

            clntEmplSectorCode.setValue(nodeBinding.getAttribute("sectorCode"));
            clntEmplSectorName.setValue(nodeBinding.getAttribute("sectorName"));

            clntEmplType.setValue(nodeBinding.getAttribute("employerType"));
            clntEmplName.setValue(nodeBinding.getAttribute("name"));
            clntEmplPayrollNo.setValue(nodeBinding.getAttribute("payrollNo"));
            clntEmplMinSalary.setValue(nodeBinding.getAttribute("minSalary"));
            clntEmplMaxSalary.setValue(nodeBinding.getAttribute("maxSalary"));
            clntEmplMonthlyIncome.setValue(nodeBinding.getAttribute("monthlyIncome"));
            clntEmplEmploymentDate.setValue(nodeBinding.getAttribute("employmentDate"));
            clntEmplTelNos.setValue(nodeBinding.getAttribute("employerNos"));
            clntEmplCellNos.setValue(nodeBinding.getAttribute("employerCells"));
            clntEmplFax.setValue(nodeBinding.getAttribute("fax"));

            saveEmployerDetailsBtn.setText("Update");
            GlobalCC.showPopup("pt1:EmployerDetailsPop");
            GlobalCC.refreshUI(employerDetailsPopup);
            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
    }

    public String actionDeleteClientEmployerDetails() {
        Object key2 = tblEmployerDetails.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;
        if (nodeBinding != null) {

            session.setAttribute("clientEmployerAction", "D");
            BigDecimal clientCode =
                GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
            BigDecimal code =
                GlobalCC.checkBDNullValues(nodeBinding.getAttribute("code"));

            String query =
                "begin ? := tqc_clients_pkg.create_client_employer(" +
                "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?" + "); end;";
            Connection conn = null;
            DBConnector dbHandler = new DBConnector();
            try {
                conn = dbHandler.getDatabaseConnection();
                OracleCallableStatement cstmt =
                    (OracleCallableStatement)conn.prepareCall(query);

                cstmt.registerOutParameter(1, OracleTypes.VARCHAR);
                cstmt.setString(2,
                                GlobalCC.checkNullValues(session.getAttribute("clientEmployerAction")));
                cstmt.setBigDecimal(3, code);
                cstmt.setBigDecimal(4, clientCode);
                cstmt.setBigDecimal(5, null);
                cstmt.setBigDecimal(6, null);
                cstmt.setBigDecimal(7, null);
                cstmt.setString(8, null);
                cstmt.setBigDecimal(9, null);
                cstmt.setString(10, null);
                cstmt.setString(11, null);
                cstmt.setString(12, null);
                cstmt.setBigDecimal(13, null);
                cstmt.setBigDecimal(14, null);
                cstmt.setBigDecimal(15, null);
                cstmt.setObject(16, null);
                cstmt.setString(17, null);
                cstmt.setString(18, null);
                cstmt.setString(19, null);
                cstmt.execute();


                String msg = cstmt.getString(1);


                if (msg != null &&
                    msg.toLowerCase().contains("successfully")) {
                    conn.commit();
                    cstmt.close();
                    conn.close();

                    ADFUtils.findIterator("fetchClientEmployersIterator").executeQuery();
                    GlobalCC.refreshUI(tblEmployerDetails);

                    GlobalCC.hidePopup("pt1:EmployerDetailsPop");
                    GlobalCC.INFORMATIONREPORTING(msg);
                } else if (msg != null) {
                    cstmt.close();
                    conn.close();
                    GlobalCC.INFORMATIONREPORTING(msg);
                } else {
                    cstmt.close();
                    conn.close();
                    GlobalCC.INFORMATIONREPORTING("An Error has Occured.");
                }

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

            return null;
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
    }

    private void clearEmployerDetailsFields() {

        txtSearchEmpCountry.setValue(null);
        session.setAttribute("txtSearchCountry", null);
        session.setAttribute("countryCode", null);
        session.setAttribute("stateCode", null);
        session.setAttribute("townCode", null);

        clntEmplCode.setValue(null);

        clntEmplCountryCode.setValue(null);
        clntEmplCountryName.setValue(null);
        clntEmplTwnCode.setValue(null);
        clntEmplTwnName.setValue(null);
        // clntEmplTwnPostalCode.setValue(null);
        clntEmplTwnPostalZipCode.setValue(null);
        clntEmplStateCode.setValue(null);
        clntEmplState.setValue(null);

        clntEmplSectorCode.setValue(null);
        clntEmplSectorName.setValue(null);

        clntEmplType.setValue(null);
        clntEmplName.setValue(null);
        clntEmplPayrollNo.setValue(null);
        clntEmplMinSalary.setValue(null);
        clntEmplMaxSalary.setValue(null);
        clntEmplMonthlyIncome.setValue(null);
        clntEmplEmploymentDate.setValue(null);
        clntEmplTelNos.setValue(null);
        clntEmplCellNos.setValue(null);
        clntEmplFax.setValue(null);
    }

    public String actionLoadIprsClientDtls() {

        BigDecimal clientCode = null;
        try {
            Authorization auth = new Authorization();
            String process = "AMA";
            String processArea = "AMAC";
            String processSubArea = "IVDT";
            String AccessGranted =
                auth.checkUserRights(process, processArea, processSubArea,
                                     null, null);
            if (!"Y".equalsIgnoreCase(AccessGranted)) {
                GlobalCC.accessDenied();
                return null;
            }


            List<UIComponent> children = null;
            if (panelCollSearch.getChildren() != null) {
                children = panelCollSearch.getChildren();
            }

            UIComponent component = children.get(0);

            RichTable rpt = null;
            for (int i = 0; i < children.size(); i++) {
                component = children.get(i);
                if (component.getId().equalsIgnoreCase("t1200")) {
                    rpt = (RichTable)component;
                }
            }

            Object key2 = rpt.getSelectedRowData();
            JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

            if (n != null) {
                clientCode =
                        GlobalCC.checkBDNullValues(n.getAttribute("code"));
            }
            if (clientCode == null) {
                return null;
            }
            IprsSetupsApi iprsApi = new IprsSetupsApi(session);
            iprsApi.load("C", clientCode, dlgIprsComp);

            GlobalCC.refreshUI(txtTqIdNumber);
            GlobalCC.refreshUI(txtTqPinNo);
            GlobalCC.refreshUI(txtTqPassport);
            GlobalCC.refreshUI(txtTqDob);
            GlobalCC.refreshUI(txtTqFullNames);
            GlobalCC.refreshUI(txtTqGender);
            GlobalCC.refreshUI(txtIprsIdNumber);
            GlobalCC.refreshUI(txtIprsPinNo);
            GlobalCC.refreshUI(txtIprsPassport);
            GlobalCC.refreshUI(txtIprsDob);
            GlobalCC.refreshUI(txtIprsFullNames);
            GlobalCC.refreshUI(txtIprsGender);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return null;
    }

    public String actionUpdateTQRecord() {

        IprsSetupsApi iprsApi = new IprsSetupsApi(session);

        iprsApi.saveTQFields(dlgIprsComp);

        GlobalCC.refreshUI(txtTqIdNumber);
        GlobalCC.refreshUI(txtTqPinNo);
        GlobalCC.refreshUI(txtTqPassport);
        GlobalCC.refreshUI(txtTqDob);
        GlobalCC.refreshUI(txtTqFullNames);
        GlobalCC.refreshUI(txtTqGender);

        return null;
    }

    public String actionCloseIprsPopup() {
        GlobalCC.hidePopup("pt1:iprsCompPop");
        return null;
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
            session.setAttribute("ProcessShtDesc", GlobalCC.clientProcess);
            session.setAttribute("ProcessAreaShtDesc", GlobalCC.Edit);
            session.setAttribute("ProcessSubAShtDesc", GlobalCC.Edit);

            JBPMEngine wf = JBPMEngine.getInstance();

            boolean ticketsApp = wf.TicketsApp();

            String Taske =
                GlobalCC.checkNullValues(session.getAttribute("taskID"));


            conn = datahandler.getDatabaseConnection();
            String sql =
                "begin tqc_clients_pkg.client_status_prc(?,?,?,?); end;";
            cst = conn.prepareCall(sql);
            cst.registerOutParameter(1, OracleTypes.VARCHAR);
            cst.registerOutParameter(2, OracleTypes.VARCHAR);
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("clientCode"));
            cst.setString(4, "D");
            cst.execute();
            conn.commit();
            conn.close();

            session.setAttribute("ClientStatus", "D");

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

    public boolean isbtnMakeReadyClientDisabled() {
        String status =
            GlobalCC.checkNullValues(session.getAttribute("ClientStatus"));
        return ("I".equalsIgnoreCase(status) || "A".equalsIgnoreCase(status) ||
                "R".equalsIgnoreCase(status));
    }


    public boolean isbtnDeactivateClientDisabled() {
        String status =
            GlobalCC.checkNullValues(session.getAttribute("ClientStatus"));
        return ("I".equalsIgnoreCase(status) || "R".equalsIgnoreCase(status));
    }

    public boolean isbtnAuthorizeClientDisabled() {
        String status =
            GlobalCC.checkNullValues(session.getAttribute("ClientStatus"));
        return ("A".equalsIgnoreCase(status) || "D".equalsIgnoreCase(status));
    }

    public boolean isbtnSaveClientDisabled() { //DISABLE UPDATE BUTTON WHEN THE ACCOUNT IS SUSPENDED
        String status =
            GlobalCC.checkNullValues(session.getAttribute("ClientStatus"));
        return ("I".equalsIgnoreCase(status));
    }

    public boolean istxtSurnameDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_SURNAME.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("clientIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public boolean istxtOtherNamesDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_OTHER_NAMES.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("clientIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public boolean istxtFullNameDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_FULLNAME.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("clientIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public boolean istxtGenderDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_GENDER.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("clientIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public boolean istxtDobDisabled() {
        String disabled =
            GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_DOB.disabled"));
        String iprs_validated =
            GlobalCC.checkNullValues(session.getAttribute("clientIprsValidated"));
        return ("Y".equalsIgnoreCase(disabled) ||
                "Y".equalsIgnoreCase(iprs_validated));
    }

    public String actionRefreshSignatories() {
        ADFUtils.findIterator("findClientsSignatoriesIterator").executeQuery();
        GlobalCC.refreshUI(signatoriesTable);
        return null;
    }

    public String actionRefreshIncomeSource() {
        ADFUtils.findIterator("fetchIncomeSourcesIterator").executeQuery();
        GlobalCC.refreshUI(tblClntIncomeSource);
        return null;
    }

    public String actionNewSignatory() {
        session.removeAttribute("SignatoryCode");

        txtSignatoryTitle.setValue(null);
        txtSignatoryName.setValue(null);
        txtSignatorySourceofincome.setValue(null);
        txtSignatoryOccupation.setValue(null);
        txtSignatoryGender.setValue(null);
        txtSignatoryNationality.setValue(null);
        txtSignatoryDateofbirth.setValue(null);
        txtSignatoryPlaceofbirth.setValue(null);
        txtSignatoryPhoneno.setValue(null);
        txtSignatoryMeansofid.setValue(null);
        txtSignatoryMeansOfIdVal.setValue(null);
        txtSignatoryTaxno.setValue(null);
        txtSignatoryEmail.setValue(null);
        txtSignatoryAddress.setValue(null);

        btnSaveSignatory.setText("Save");
        // Open the popup dialog for required documents
        GlobalCC.showPopup("pt1:SignatoryPop");
        return null;
    }


    public String actionNewIncomeSource() {
        // session.removeAttribute("SignatoryCode");
        BigDecimal clientCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));

        if (clientCode == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a client");
            return null;
        }

        btnSaveIncomeSource.setText("Save");
        // Open the popup dialog for required documents
        GlobalCC.showPopup("pt1:IncomeSourcePop");
        return null;
    }

    public String actionEditSignatory() {
        if (session.getAttribute("ClientCode") != null) {
            Object key2 = signatoriesTable.getSelectedRowData();
            JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

            if (n != null) {
                session.setAttribute("SignatoryCode", n.getAttribute("code"));
                txtSignatoryTitle.setValue(n.getAttribute("title"));
                txtSignatoryName.setValue(n.getAttribute("name"));
                txtSignatorySourceofincome.setValue(n.getAttribute("sourceOfIncome"));
                txtSignatoryOccupation.setValue(n.getAttribute("occupation"));
                txtSignatoryGender.setValue(n.getAttribute("gender"));
                txtSignatoryNationality.setValue(n.getAttribute("nationality"));
                txtSignatoryDateofbirth.setValue(n.getAttribute("dateOfbirth"));
                txtSignatoryPlaceofbirth.setValue(n.getAttribute("placeOfbirth"));
                txtSignatoryPhoneno.setValue(n.getAttribute("phoneNo"));
                txtSignatoryMeansofid.setValue(n.getAttribute("meansOfId"));
                txtSignatoryMeansOfIdVal.setValue(n.getAttribute("meansOfIdVal"));
                txtSignatoryTaxno.setValue(n.getAttribute("taxNo"));
                txtSignatoryEmail.setValue(n.getAttribute("email"));
                txtSignatoryAddress.setValue(n.getAttribute("address"));
                btnSaveSignatory.setText("Edit");
                // Open the popup dialog
                GlobalCC.showPopup("pt1:SignatoryPop");

            } else {
                GlobalCC.INFORMATIONREPORTING("No Signatory Selected:");
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Client Selected:");
        }
        return null;
    }

    public String actionEditIncomeSource() {

        if (session.getAttribute("ClientCode") != null) {
            Object key2 = tblClntIncomeSource.getSelectedRowData();
            JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

            if (n != null) {
                session.setAttribute("IncomeSourceCode",
                                     n.getAttribute("code"));
                txtSourceofIncomeC.setValue(n.getAttribute("incomeName"));
                txtIncomeSourceType.setValue(n.getAttribute("incomeType"));

                btnSaveIncomeSource.setText("Edit");
                // Open the popup dialog
                GlobalCC.showPopup("pt1:IncomeSourcePop");

            } else {
                GlobalCC.INFORMATIONREPORTING("Please Select Record to Edit:");

            }

        } else {
            GlobalCC.INFORMATIONREPORTING("Please Select a Client:");
        }

        return null;

    }

    public String actionDeleteIncomeSource() {
        Object key2 = tblClntIncomeSource.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;
        if (n == null) {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        //SessionFactory factory = GlobalCC.getSessionFactory();
        //Session dbSess = factory.openSession();
        Session dbSess = HibernateUtil.getSession();
        Transaction tx = null;

        try {
            BigDecimal code =
                GlobalCC.checkBDNullValues(n.getAttribute("code"));
            tx = dbSess.beginTransaction();
            IncomeSources incomeSource =
                (IncomeSources)dbSess.get(IncomeSources.class, code);
            dbSess.delete(incomeSource);
            tx.commit();
            session.removeAttribute("IncomeSourceCode");
            ADFUtils.findIterator("fetchIncomeSourcesIterator").executeQuery();
            GlobalCC.refreshUI(tblClntIncomeSource);

            String message = "Record DELETED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            dbSess.close();
        }
        return null;
    }

    public String actionDeleteSignatory() {
        Object key2 = agentDirectorsTable.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;
        if (n == null) {
            GlobalCC.INFORMATIONREPORTING("No Signatory Selected.");
            return null;
        }
        //SessionFactory factory = GlobalCC.getSessionFactory();
        //Session dbSess = factory.openSession();
        Session dbSess = HibernateUtil.getSession();
        Transaction tx = null;

        try {
            BigDecimal code =
                GlobalCC.checkBDNullValues(n.getAttribute("code"));
            tx = dbSess.beginTransaction();
            ClientSignatory signatory =
                (ClientSignatory)dbSess.get(ClientSignatory.class, code);
            dbSess.delete(signatory);
            tx.commit();
            session.removeAttribute("SignatoryCode");
            ADFUtils.findIterator("findClientsSignatoriesIterator").executeQuery();
            GlobalCC.refreshUI(signatoriesTable);

            String message = "Signatory DELETED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            dbSess.close();
        }
        return null;
    }

    public String actionSaveSignatory() {
        //SessionFactory factory = GlobalCC.getSessionFactory();
        //Session dbSess = factory.openSession();
        Session dbSess = HibernateUtil.getSession();
        Transaction tx = null;
        Integer itemID = null;
        BigDecimal clientCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));

        try {
            tx = dbSess.beginTransaction();
            ClientSignatory item = new ClientSignatory();
            String action = btnSaveSignatory.getText();
            if ("Edit".equals(action)) {
                BigDecimal code =
                    GlobalCC.checkBDNullValues(session.getAttribute("SignatoryCode"));
                item =
(ClientSignatory)dbSess.get(ClientSignatory.class, code);
            }

            item.setClientCode(clientCode);
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryTitle.getValue());
                item.setTitle(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryName.getValue());
                item.setName(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatorySourceofincome.getValue());
                item.setSourceOfIncome(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryOccupation.getValue());
                item.setOccupation(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryGender.getValue());
                item.setGender(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryNationality.getValue());
                item.setNationality(val);
            }
            {
                java.sql.Date val =
                    GlobalCC.extractDate(txtSignatoryDateofbirth);
                item.setDateOfbirth(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryCountry1.getValue());
                item.setPobCountry(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryState1.getValue());
                item.setPobState(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryTown.getValue());
                item.setPobTown(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryPhoneno.getValue());
                item.setPhoneNo(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryMeansofid.getValue());
                item.setMeansOfId(val);
            }
            String valMeansOfIdVal =
                GlobalCC.checkNullValues(txtSignatoryMeansOfIdVal.getValue());
            item.setMeansOfIdVal(valMeansOfIdVal);
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryTaxno.getValue());
                item.setTaxNo(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryEmail.getValue());
                item.setEmail(val);
            }
            {
                String val =
                    GlobalCC.checkNullValues(txtSignatoryAddress.getValue());
                item.setAddress(val);
            }
            if ("Edit".equals(action)) {
                dbSess.update(item);
            } else {
                dbSess.save(item);
            }
            tx.commit();
            ADFUtils.findIterator("findClientsSignatoriesIterator").executeQuery();
            GlobalCC.refreshUI(signatoriesTable);

            String message = "Signatory Saved Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);
            GlobalCC.hidePopup("pt1:SignatoryPop");
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            dbSess.close();
        }
        return null;
    }

    public String actionSaveIncomeSource() {
        Session dbSess = HibernateUtil.getSession();
        Transaction tx = null;
        Integer itemID = null;

        BigDecimal clientCode =
            GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));
        try {
            tx = dbSess.beginTransaction();
            IncomeSources item = new IncomeSources();
            String action = btnSaveIncomeSource.getText();
            if ("Edit".equals(action)) {
                BigDecimal code =
                    GlobalCC.checkBDNullValues(session.getAttribute("IncomeSourceCode"));
                item = (IncomeSources)dbSess.get(IncomeSources.class, code);
            }

            item.setClientCode(clientCode);
            {
                String val =
                    GlobalCC.checkNullValues(txtSourceofIncomeC.getValue());
                item.setIncomeName(val);
            }

            {
                String val =
                    GlobalCC.checkNullValues(txtIncomeSourceType.getValue());
                item.setIncomeType(val);
            }
            if ("Edit".equals(action)) {
                dbSess.update(item);
            } else {
                dbSess.save(item);
            }
            tx.commit();
            ADFUtils.findIterator("fetchIncomeSourcesIterator").executeQuery();
            GlobalCC.refreshUI(tblClntIncomeSource);

            String message = "Income source Saved Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);
            GlobalCC.hidePopup("pt1:IncomeSourcePop");
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            dbSess.close();
        }


        return null;
    }


    public void selectSignatory(SelectionEvent selectionEvent) {
        Object keys = signatoriesTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a signatory!");
            return;
        }
        session.setAttribute("SignatoryCode", r.getAttribute("code"));
    }


    //-----------start selectDirector--------------------//

    public void selectDirector(SelectionEvent selectionEvent) {
        Object keys = directorsTable.getSelectedRowData();
        JUCtrlValueBinding r = (JUCtrlValueBinding)keys;
        if (r == null) {
            GlobalCC.INFORMATIONREPORTING("Please select a Director!");
            return;
        }
        session.setAttribute("DirectorCode", r.getAttribute("code"));
    }
    //-----------end selectDirector--------------------//
    //-----------start actionNewDirector--------------------//

    public String actionNewDirector() {
        session.removeAttribute("DirectorCode");
        txtDirectorTitle.setValue(null);
        txtDirectorName.setValue(null);
        txtDirectorSourceOfIncome.setValue(null);
        txtDirectorOccupation.setValue(null);
        txtDirectorGender.setValue(null);
        txtDirectorNationality.setValue(null);
        txtDirectorDateOfBirth.setValue(null);
        //txtDirectorPlaceOfBirth.setValue(null);
        txtDirectorPhoneNo.setValue(null);
        txtDirectorMeansOfId.setValue(null);
        txtDirectorMeansOfIdVal.setValue(null);
        txtDirectorTaxNo.setValue(null);
        txtDirectorEmail.setValue(null);
        txtDirectorAddress.setValue(null);
        txtDirectorYear.setValue(null);
        txtDirectorQualifications.setValue(null);
        txtDirectorPctHoldg.setValue(null);
        txtDirectorDesignation.setValue(null);
        btnSaveDirector.setText("Save");
        GlobalCC.showPopup("pt1:DirectorPop");
        return null;
    }
    //-----------end actionNewDirector--------------------//
    //-----------start actionEditDirector--------------------//

    public String actionEditDirector() {
        Object key2 = directorsTable.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        if (n != null) {
            session.setAttribute("DirectorCode", n.getAttribute("code"));
            txtDirectorTitle.setValue(n.getAttribute("title"));
            txtDirectorName.setValue(n.getAttribute("name"));
            txtDirectorSourceOfIncome.setValue(n.getAttribute("sourceOfIncome"));
            txtDirectorOccupation.setValue(n.getAttribute("occupation"));
            txtDirectorGender.setValue(n.getAttribute("gender"));
            txtDirectorNationality.setValue(n.getAttribute("nationality"));
            txtDirectorDateOfBirth.setValue(n.getAttribute("dateOfBirth"));
            //txtDirectorPlaceOfBirth.setValue(n.getAttribute("placeOfBirth"));
            txtDirectorPhoneNo.setValue(n.getAttribute("phoneNo"));
            txtDirectorMeansOfId.setValue(n.getAttribute("meansOfId"));
            txtDirectorMeansOfIdVal.setValue(n.getAttribute("meansOfIdVal"));
            txtDirectorTaxNo.setValue(n.getAttribute("taxNo"));
            txtDirectorEmail.setValue(n.getAttribute("email"));
            txtDirectorAddress.setValue(n.getAttribute("address"));
            txtDirectorYear.setValue(n.getAttribute("year"));
            txtDirectorQualifications.setValue(n.getAttribute("qualifications"));
            txtDirectorPctHoldg.setValue(n.getAttribute("pctHoldg"));
            txtDirectorDesignation.setValue(n.getAttribute("designation"));
            btnSaveDirector.setText("Edit");
            // Open the popup dialog
            GlobalCC.showPopup("pt1:DirectorPop");

        } else {
            GlobalCC.INFORMATIONREPORTING("No Director Selected:");
        }
        return null;
    }
    //-----------end actionEditDirector--------------------//
    //-----------start actionDeleteDirector--------------------//

    public String actionDeleteDirector() {
        Object key2 = directorsTable.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;
        if (n == null) {
            GlobalCC.INFORMATIONREPORTING("No Director Selected.");
            return null;
        }
        Session dbSess = HibernateUtil.getSession();
        Transaction tx = null;

        try {
            BigDecimal code =
                GlobalCC.checkBDNullValues(n.getAttribute("code"));
            System.out.println("DirectorCode: " + code);
            tx = dbSess.beginTransaction();
            ClientDirector item =
                (ClientDirector)dbSess.get(ClientDirector.class, code);
            dbSess.delete(item);
            tx.commit();
            session.removeAttribute("DirectorCode");
            ADFUtils.findIterator("findClientDirectorsIterator").executeQuery();
            GlobalCC.refreshUI(directorsTable);

            String message = "Director DELETED Successfully!";
            GlobalCC.INFORMATIONREPORTING(message);
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            GlobalCC.EXCEPTIONREPORTING(e);
        } finally {
            dbSess.close();
        }
        return null;
    }
    //-----------end actionDeleteDirector--------------------//
    //-----------start actionSaveDirector--------------------//

    public String actionSaveDirector() {

        String action = btnSaveDirector.getText();
        try {
            MainClientDAO clientDao = new MainClientDAO();
            ClientDirector item = new ClientDirector();

            BigDecimal clientCode =
                GlobalCC.checkBDNullValues(session.getAttribute("ClientCode"));


            // System.out.println("clientCode  555555 "+clientCode.toString());

            BigDecimal valCode =
                GlobalCC.checkBDNullValues(session.getAttribute("DirectorCode"));

            //-----------retrieve inputs Director-----------------//
            String valTitle =
                GlobalCC.checkNullValues(txtDirectorTitle.getValue());
            String valName =
                GlobalCC.checkNullValues(txtDirectorName.getValue());
            String valSourceOfIncome =
                GlobalCC.checkNullValues(txtDirectorSourceOfIncome.getValue());
            String valOccupation =
                GlobalCC.checkNullValues(txtDirectorOccupation.getValue());
            String valGender =
                GlobalCC.checkNullValues(txtDirectorGender.getValue());
            String valNationality =
                GlobalCC.checkNullValues(txtDirectorNationality.getValue());
            java.sql.Date valDateOfBirth =
                GlobalCC.extractDate(txtDirectorDateOfBirth);
            //  String valPlaceOfBirth =
            //                GlobalCC.checkNullValues(txtDirectorPlaceOfBirth.getValue());
            String valPhoneNo =
                GlobalCC.checkNullValues(txtDirectorPhoneNo.getValue());
            String valMeansOfId =
                GlobalCC.checkNullValues(txtDirectorMeansOfId.getValue());
            String valMeansOfIdVal =
                GlobalCC.checkNullValues(txtDirectorMeansOfIdVal.getValue());
            String valTaxNo =
                GlobalCC.checkNullValues(txtDirectorTaxNo.getValue());
            String valEmail =
                GlobalCC.checkNullValues(txtDirectorEmail.getValue());
            String valAddress =
                GlobalCC.checkNullValues(txtDirectorAddress.getValue());
            String valYear =
                GlobalCC.checkNullValues(txtDirectorYear.getValue());
            String valQualifications =
                GlobalCC.checkNullValues(txtDirectorQualifications.getValue());
            String valPctHoldg =
                GlobalCC.checkNullValues(txtDirectorPctHoldg.getValue());
            String valDesignation =
                GlobalCC.checkNullValues(txtDirectorDesignation.getValue());

            String pState =
                GlobalCC.checkNullValues(txtDirtoryState.getValue());

            String pCountry =

                GlobalCC.checkNullValues(txtDirtoryCountry1.getValue());


            String pTown =
                GlobalCC.checkNullValues(txtDirtoryTownDir.getValue());
            //-----------set ClientDirector------------------//

            item.setCode(valCode);
            item.setTitle(valTitle);
            item.setName(valName);
            item.setSourceOfIncome(valSourceOfIncome);
            item.setOccupation(valOccupation);
            item.setGender(valGender);
            item.setNationality(valNationality);
            item.setDateOfBirth(valDateOfBirth);
            //item.setPlaceOfBirth(valPlaceOfBirth);
            item.setPhoneNo(valPhoneNo);
            item.setMeansOfId(valMeansOfId);
            item.setMeansOfIdVal(valMeansOfIdVal);
            item.setTaxNo(valTaxNo);
            item.setEmail(valEmail);
            item.setAddress(valAddress);
            item.setYear(valYear);
            item.setQualifications(valQualifications);
            item.setPctHoldg(valPctHoldg);
            item.setDesignation(valDesignation);
            item.setClientCode(clientCode);
            item.setPobCountry(pCountry);
            item.setPobState(pState);
            item.setPobTown(pTown);

            Boolean sucess =
                clientDao.actionSaveClientDirector(action, clientCode, item,
                                                   valCode);
            if (sucess) {

                ADFUtils.findIterator("findClientDirectorsIterator").executeQuery();
                GlobalCC.refreshUI(directorsTable);

                String message = "Director Saved Successfully!";
                GlobalCC.INFORMATIONREPORTING(message);
                GlobalCC.hidePopup("pt1:DirectorPop");

            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }
    private List<SelectItem> nationalities = new ArrayList<SelectItem>();


    public List<SelectItem> getNationalities() {
        if (nationalities != null) {
            nationalities.clear();
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;


        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;

        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 =
                "SELECT  cou_nationality,cou_code  \n" + //,CLNTY_CODE
                "           FROM tqc_countries";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            rst = (OracleResultSet)stmt.executeQuery();

            nationalities.add(new SelectItem("-2000", "Select Nationality"));

            while (rst.next()) {
                nationalities.add(new SelectItem(rst.getString(2),
                                                 rst.getString(1)));

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return nationalities;
    }

    public String getClientIdRegNoLabel() {

        String idLabel =
            GlobalCC.checkNullValues(session.getAttribute("FX_CLIENT_ID_REG_NO.label"));
        try {
            String typePerson =
                GlobalCC.checkNullValues(session.getAttribute("typePerson"));
            if ("Y".equals(typePerson)) {
                idLabel = GlobalCC.getSysParamValue("INDIVIDUAL_ID_NO_LABEL");
            }
            if ("N".equals(typePerson)) {
                idLabel = GlobalCC.getSysParamValue("CORPORATE_ID_NO_LABEL");
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }

        return idLabel;
    }

    List<SelectItem> incomes = new ArrayList<SelectItem>();

    public List<SelectItem> getSourcesOfIncome() {
        if (incomes != null) {
            incomes.clear();
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;


        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;

        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 = "SELECT  INCOME_NAME \n" + //,CLNTY_CODE
                "           FROM tqc_income_sources";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            rst = (OracleResultSet)stmt.executeQuery();

            incomes.add(new SelectItem("-2000", "Select Source Of Income"));

            while (rst.next()) {
                incomes.add(new SelectItem(rst.getString(1)));

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return incomes;
    }

    List<SelectItem> incomes1 = new ArrayList<SelectItem>();

    public List<SelectItem> getSignaSourcesOfIncome() {
        if (incomes1 != null) {
            incomes1.clear();
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;


        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;

        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 = "SELECT  INCOME_NAME \n" + //,CLNTY_CODE
                "           FROM tqc_income_sources";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            rst = (OracleResultSet)stmt.executeQuery();

            incomes1.add(new SelectItem("-2000", "Select Source Of Income"));

            while (rst.next()) {
                incomes1.add(new SelectItem(rst.getString(1)));

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return incomes1;
    }


    List<SelectItem> incomesIS = new ArrayList<SelectItem>();

    public List<SelectItem> getSourcesOfIncomeIS() {
        if (incomesIS != null) {
            incomesIS.clear();
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;


        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;

        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 = "SELECT  INCOME_NAME \n" + //,CLNTY_CODE
                "           FROM tqc_income_sources";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            rst = (OracleResultSet)stmt.executeQuery();

            incomesIS.add(new SelectItem("-2000", "Select Source Of Income"));

            while (rst.next()) {
                incomesIS.add(new SelectItem(rst.getString(1)));

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return incomesIS;
    }


    List<SelectItem> incomesDir = new ArrayList<SelectItem>();

    public List<SelectItem> getDirectorSourcesOfIncome() {
        if (incomesDir != null) {
            incomesDir.clear();
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;


        OracleCallableStatement stmt = null;
        OracleResultSet rst = null;

        try {
            conn = dbConnector.getDatabaseConnection();
            String query1 = "SELECT  INCOME_NAME  \n" + //,CLNTY_CODE
                "           FROM tqc_income_sources";
            stmt = (OracleCallableStatement)conn.prepareCall(query1);
            rst = (OracleResultSet)stmt.executeQuery();

            incomesDir.add(new SelectItem("-2000", "Select Source Of Income"));

            while (rst.next()) {
                incomesDir.add(new SelectItem(rst.getString(1)));

            }
            rst.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            GlobalCC.EXCEPTIONREPORTING(ex);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rst);
        }
        return incomesDir;
    }

    //show income source tab for multi client
    public boolean isMULTI_INCOME_SOURCE() {
        String val = GlobalCC.getSysParamValue("CLIENT_MULT_INCOME_SOURCES");
        if (val.equalsIgnoreCase("Y")) {
            return true;

        }
        return false;


    }


}
