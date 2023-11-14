package TurnQuest.view.orgs;


import TurnQuest.view.Base.ADFUtils;
import TurnQuest.view.Base.Base64;
import TurnQuest.view.Base.FileUpload;
import TurnQuest.view.Base.GlobalCC;
import TurnQuest.view.Base.HibernateUtil;
import TurnQuest.view.Base.Rendering;
import TurnQuest.view.Connect.Authorization;
import TurnQuest.view.Connect.DBConnector;
import TurnQuest.view.Ui.FormUi;
import TurnQuest.view.commons.UtilMethods;

import TurnQuest.view.models.TransferItem;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.imageio.ImageIO;

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
import oracle.adf.view.rich.component.rich.input.RichSelectItem;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.layout.RichPanelTabbed;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.output.RichImage;
import oracle.adf.view.rich.component.rich.output.RichOutputLabel;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.component.rich.output.RichPanelCollection;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.domain.BlobDomain;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;
import oracle.jbo.uicli.binding.JUCtrlValueBinding;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.commons.dbutils.DbUtils;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class OrganizationManip {

    private Rendering renderer = new Rendering();

    private RichInputNumberSpinbox orgCode;
    private RichInputText orgShortDesc;
    private RichInputText orgName;
    private RichInputText orgAddrs;
    private RichInputNumberSpinbox orgTwnCode;
    private RichInputNumberSpinbox orgCouCode;
    private RichInputText orgEmail;
    private RichInputText orgPhyAddrs;
    private RichInputNumberSpinbox orgCurCode;
    private RichInputText orgZip;
    private RichInputText orgFax;
    private RichInputText orgTel1;
    private RichInputText orgTel2;
    private RichInputText orgRptLogo;
    private RichInputText orgMotto;
    private RichInputText orgPinNo;
    private RichInputText orgEdCode;
    private RichInputText orgItemAccCode;
    private RichInputText orgOtherName;
    private RichSelectOneChoice orgType;
    private RichInputNumberSpinbox orgWebBrnCode;
    private RichInputText orgWebAddrs;
    private RichInputNumberSpinbox orgAgnCode;
    private RichInputText orgDirectors;
    private RichInputNumberSpinbox orgLangCode;
    private RichInputText orgAvatar;
    private RichTree orgTree;
    private RichPanelGroupLayout orgDetails;
    private RichTable countriesLovTab;
    private RichPopup countriesLovPopUp;
    private RichTable townsLovTab;
    private RichPopup townsLovPopUp;
    private RichInputText orgCountry;
    private RichInputText orgTown;
    private RichInputText orgBaseCurrency;
    private RichTable currenciesLovTab;
    private RichPopup currenciesLovPopUp;
    private RichTable regionsTab;
    private RichTable bankRegionsTab;
    private RichTable branchesTab;
    private RichTable unitsTab;
    private RichTable agentsTab;
    private RichPopup regionsPopUp;
    private RichPopup bankRegionsPopUp;
    private RichInputNumberSpinbox regCode;
    private RichInputNumberSpinbox regOrgCode;
    private RichInputText regShortDesc;
    private RichInputText regName;
    private RichInputDate regWef;
    private RichInputDate regWet;
    private RichInputNumberSpinbox regAgnCode;
    private RichSelectOneChoice regPostLevel;
    private RichSelectOneChoice regMngrAllowed;
    private RichSelectOneChoice regOverideCommEarned;
    private RichPopup branchesPopUp;
    private RichInputNumberSpinbox brnCode;
    private RichInputText brnShortDesc;
    private RichInputNumberSpinbox brnRegCode;
    private RichInputText brnName;
    private RichInputText brnPhyAddrs;
    private RichInputText brnEmail;
    private RichInputText brnPostAddrs;
    private RichInputNumberSpinbox brnTwnCode;
    private RichInputNumberSpinbox brnCouCode;
    private RichInputText brnContact;
    private RichInputText brnManager;
    private RichInputText brnTel;
    private RichInputText brnFax;
    private RichInputNumberSpinbox brnGenPolClm;
    private RichInputNumberSpinbox brnBnsCode;
    private RichInputNumberSpinbox brnAgnCode;
    private RichSelectOneChoice brnPostLevel;
    private RichTable branchNamesLovTab;
    private RichPopup branchNamesLovPopUp;
    private RichPopup branchUnitsPopUp;
    private RichInputNumberSpinbox bruCode;
    private RichInputNumberSpinbox bruBrnCode;
    private RichInputText bruShortDesc;
    private RichInputText bruName;
    private RichInputText bruSupervisor;
    private RichSelectOneChoice bruStatus;
    private RichInputNumberSpinbox bruAgnCode;
    private RichInputNumberSpinbox bruBraCode;
    private RichInputText bruManager;
    private RichSelectOneChoice bruPostLevel;
    private RichInputText regManager;
    private RichTable regManagerslovTab;
    private RichTable bankRegMgrsLovTab;
    private RichPopup regManagerslovPopUp;
    private RichPopup bankRegMgrsLovPopUp;
    private RichPopup brnManagersLovPopUp;
    private RichTable brnManagersLovTab;
    private RichPopup bruManagersLovPopUp;
    private RichTable bruManagersLovTab;
    private RichTable agenciesTab;
    private RichInputText txtBraCode;
    private RichInputNumberSpinbox braBrnCode;
    private RichInputText braShortDesc;
    private RichInputText braName;
    private RichSelectOneChoice braStatus;
    private RichInputText braManager;
    private RichInputNumberSpinbox braAgnCode;
    private RichSelectOneChoice braPostLevel;
    private RichPopup agenciesPopUp;
    private RichPopup braManagersLovPopUp;
    private RichTable braManagersLovTab;
    // private RichTable orgManagersLovPopUp;
    private RichTable orgManagersLovTab;
    private RichPopup orgManagersLovPopUp;
    private RichInputText orgManager;
    private RichTable tblOrgDivisions;
    private RichTable tblDivSubDivisions;
    private RichCommandButton btnSaveOrgDivisionPop;
    private RichInputText txtDivCodePop;
    private RichInputText txtDivShtDescPop;
    private RichInputText txtDivNamePop;
    private RichSelectOneChoice txtDivStatusPop;
    private RichInputText btnSubDivCode;
    private RichInputText btnSubDivShtDesc;
    private RichInputText btnSubDivName;
    private RichCommandButton btnSaveDivSubDivision;
    private RichPanelBox panelDetailDivisions;
    private RichInputText txtSelectedBranchDivisionCode;
    private RichTree treeUnassignedBranchDivs;
    private RichTable tblBranchDivs;
    private RichInputText txtSelectedBranchBDIVCode;
    private RichInputText bdCodePop;
    private RichInputText bdBranchCodePop;
    private RichInputText bdDivisionCodePop;
    private RichInputDate bdWEFPop;
    private RichInputDate bdWETPop;
    private RichCommandButton btnSaveBranchDivisionPop;
    private RichInputText txtBnsName;
    private RichTable tblStatesLOV;
    private RichInputText txtStateCode;
    private RichInputText txtStateName;
    private RichPanelTabbed mainTabPanel;
    private RichPanelBox mainOrgPanelBox;
    private RichShowDetailItem sdBranchTab;
    private RichShowDetailItem sdAgenciesTab;
    private RichShowDetailItem sdUnitsTab;
    private RichShowDetailItem sdAgentsTab;
    private RichImage orgLogoImg;
    private UploadedFile myFile;
    private RichImage orgGrpLogoImg;
    private BlobDomain domainFile;
    private BlobDomain orgGrpImageFile;
    private RichImage clientImage;
    private RichInputFile uploadComponent;
    private RichInputFile uploadOrgGrpImg;
    private RichInputText txtRegAgnSeqNo;
    private RichInputText txtRegBranchMgrSeqNo;

    private RichOutputLabel outputLbManager;
    private RichOutputLabel btnRegManagerpop;
    private RichCommandButton btnShowManagerLov;
    private RichCommandButton btnShowBankRegMgrsLov;
    private RichInputText txtBrnBranchMgrSeqNo;
    private RichInputText txtBrnAgnSeqNo;
    private RichOutputLabel outputLbBrnMgr;
    private RichCommandButton btnShowBranchMgrLov;
    private RichInputText txtBraBranchMgrSeqNo;
    private RichInputText txtBraAgnSeqNo;
    private RichOutputLabel outlBraMgr;
    private RichCommandButton btnShowBraMgrlov;
    private RichOutputLabel outputlbMgr;
    private RichCommandButton btnShowBruManager;
    private RichInputNumberSpinbox txtBruBranchMgrSeqNo;
    private RichInputText txtBruAgnSeqNo;
    private RichCommandButton btnDeleteBranchUnit;
    private RichCommandButton btnEditBranchUnit;
    private RichCommandButton btnDeleteRegions;
    private RichCommandButton btnEditRegions;
    private RichCommandButton btnTransferOrgRegion;

    private RichCommandButton btnDeleteBankRegion;
    private RichCommandButton btnEditBankRegion;
    private RichCommandButton btnNewBankRegion;

    private RichCommandButton btnDeleteBranch;
    private RichCommandButton btnEditBranch;
    private RichCommandButton btnDeleteBranchAgency;
    private RichCommandButton btnEditBranchAgency;
    private RichCommandButton btnTransferBranchAgency;
    private RichCommandButton btnTransferRegionBranch;
    private RichShowDetailItem orgTab;
    private RichShowDetailItem regManup;
    private RichShowDetailItem regTab;
    private InputStream orgImg;
    private InputStream orgGrpImg;
    private long orgImgByteLength;
    private long orgGrpImgByteLength;
    private RichInputFile inputFileOrgImgFile;
    private RichInputText bnsContact;
    private RichInputText bnsEmail;
    private RichInputText bnsFax;
    private RichInputText bnsTel;
    private RichInputText bnsTownName;
    private RichInputText bnsTownCode;
    private RichInputText bnsStateName;
    private RichInputText bnsStateCode;
    private RichInputText bnsCountryName;
    private RichInputText bnsCountryCode;
    private RichInputText bnsPostalAddr;
    private RichInputText bnsPhysicalAddr;
    private RichInputText bnsCode;
    private RichPanelCollection tblBranchNames;
    private RichTable tblBranchName;
    private RichInputText bnsName;
    private RichInputText bnsShortDesc;
    private RichCommandButton btnSaveBranchName;
    private RichCommandButton saveBranchName;
    private RichCommandButton btnSelectTown;
    private RichCommandButton btnSelectTwnfirst;
    private RichCommandButton btnStateLov2;
    private RichCommandButton btnStateLov1;
    private RichCommandButton btnSelectCountry2;
    private RichCommandButton btnSelectCountry1;
    private RichTable tblBranchNameListing;
    private RichCommandButton btnSaveBranch;
    private RichInputNumberSpinbox txtDivOrder;
    private RichTable tblPersonnelsLOV;
    private RichCommandButton btnPeronnelOK;
    private RichInputText txtDirectorName;
    private RichInputText txtDirectorCode;
    private RichInputText txtAssDirectorName;
    private RichInputText txtAssDirectorCode;
    private RichPanelLabelAndMessage lblState;
    private RichSelectOneChoice txtManagerAllowed;
    private RichSelectOneChoice txtOverideComm;
    private RichSelectOneChoice txtManagerAllowedBranch;
    private RichSelectOneChoice txtOverideCommBranch;
    private RichSelectOneChoice txtManagerAllowedAgency;
    private RichOutputLabel tmanager;
    private RichOutputLabel tade;
    private RichOutputLabel tmanallowed;
    private RichSelectOneChoice txtOverideCommAgency;
    private RichInputText txtBrnAgnPolPrefix;
    private RichInputText txtShtDescPref;
    private RichInputText txtBranchShtPref;
    private RichInputText txtPrefix;
    private RichInputText brnComOwnBuss;
    private RichSelectOneChoice txtcombuss;
    private RichInputText txtUnitPrefix;
    private RichSelectItem txtComtBuss;
    private RichInputNumberSpinbox proposalSeq;
    private RichInputNumberSpinbox policySeq;
    private RichInputText polSeqAgnt;
    private RichInputText propseqAgn;
    private RichInputText polSeqAgn;
    private RichInputNumberSpinbox polSeqNoUnits;
    private RichInputNumberSpinbox propSeqNoUnits;
    private RichOutputText branchLabel;
    private RichInputDate wef;
    private RichInputDate wet;
    private RichTable branchesLOV;
    private RichTable brnDivisionsLOV;
    private RichInputText txtVatNumber;
    private RichInputText txtMobile1;
    private RichInputText txtMobile2;
    private RichSelectOneChoice slMobile2;
    private RichSelectOneChoice slMobile1;
    private RichTable contactsTab;
    private RichInputText txtTbcCode;
    private RichInputText txtTbcName;
    private RichInputText txtTbcDesignation;
    private RichInputText txtTbcMobile;
    private RichInputText txtTbcTelephone;
    private RichInputText txtTbcIdNo;
    private RichInputText txtTbcPhysAdd;
    private RichInputText txtTbcEmail;
    private RichSelectOneChoice accType;
    private RichSelectOneChoice txtRegion;
    private RichCommandButton btnTransferUnitAgents;
    private RichCommandButton btnTransferBranchtAgents;
    private RichCommandButton btnTransferAgencyUnits;
    private RichCommandButton btnShowTransferTolov;
    private RichCommandButton btnShowTransferTolov2;

    //    private RichInputNumberSlider polSeqAgnt;
    //    private RichInputNumberSlider propseqAgn;
    //Bank Regions

    private RichInputNumberSpinbox bnkrCode;
    private RichInputNumberSpinbox bnkrOrgCode;
    private RichInputText bnkrShtDesc;
    private RichInputText bnkrName;
    private RichInputDate bnkrWef;
    private RichInputDate bnkrWet;
    private RichInputNumberSpinbox bnkrRegCode;
    private RichInputNumberSpinbox bnkrAgnCode;
    private RichInputText bnkrManager;
    private RichCommandButton btnSaveBankRegion;
    private RichImage orgAutoSignImg;
    private RichInputFile userSignature;
    private UploadedFile signatureFile;

    //Transfer Section
    private RichInputText txtTransferFromName;
    private RichInputText txtTransferFromName2;
    private RichInputText txtTransferToName;
    private RichInputText txtTransferToName2;

    private RichTable tblTransferToLov;
    private RichTable tblTransferableItems;
    private RichTable tblTransferedItems;

    private RichShowDetailItem swdTransferedItems;
    private RichShowDetailItem swdTransferedItems2;
    private RichShowDetailItem swdTransferableItems;
    private RichShowDetailItem swdTransferableItems2;
    private RichDialog dlgTransferOrgItems;
    private RichDialog dlgTransferOrgItems2;
    private RichSelectBooleanCheckbox chkTtiSelectA;
    private RichSelectBooleanCheckbox chkTtiSelectB;

    //-- Bank Details
    private RichInputText txtOrgBankAccountName;
    private RichInputText txtOrgBankAccountNo;
    private RichInputText txtOrgSwiftCode;
    private RichInputText txtOrgBankName;
    private RichInputText txtOrgBankBranch;
    //
    private RichTable orgBankLovTab;
    private RichTable orgBankBranchLovTab;
    private RichInputText brnRef;
    private RichInputText branchADE;
    private RichTable brnADEs;
    private RichInputNumberSpinbox adeAgnCode;


    private RichInputText txtAgencyName;
    private RichInputNumberSpinbox txtTeamManagerSequenceNo;
    private RichInputNumberSpinbox txtAgentsSequenceNo;

    private RichInputText bnsPostalCode;
    
    private RichInputText brn_Twn;
    private RichInputText brnPostCode;
    
    private RichPopup myPopup;
    private RichShowDetailItem branchName;
    private RichShowDetailItem faAgentsTab;
    
    private RichCommandButton btnEdifaAgentsTab;
    private RichCommandButton btndeletefaAgentsTab;
    private RichCommandButton btnSaveFaAgent;
    
    private RichInputText fateamCode;
    private RichInputText faAgentCode;
    private RichInputText faShortDesc;
    private RichInputText faAgentName;
    
    private RichTable FAagentsTab;
    private RichCommandButton btnTransferUnitBranch;
    
    

    public OrganizationManip() {
       
        super();
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    public void organizationSelected(SelectionEvent evt) {
        try {
            if (evt.getAddedSet() != evt.getRemovedSet()) {
                RowKeySet keys = evt.getAddedSet();
                if (keys != null && keys.getSize() > 0) {
                    for (Object treeRowKey : keys) {
                        orgTree.setRowKey(treeRowKey);
                        JUCtrlHierNodeBinding nodeBinding =
                            (JUCtrlHierNodeBinding)orgTree.getRowData();

                        session.setAttribute("ORGCode",
                                             nodeBinding.getRow().getAttribute("orgCode"));
                        session.setAttribute("ORGName",
                                             nodeBinding.getRow().getAttribute("orgName"));

                        orgCode.setValue(nodeBinding.getRow().getAttribute("orgCode"));
                        orgShortDesc.setValue(nodeBinding.getRow().getAttribute("orgShortDesc"));
                        orgName.setValue(nodeBinding.getRow().getAttribute("orgName"));
                        orgAddrs.setValue(nodeBinding.getRow().getAttribute("orgAddrs"));
                        orgTwnCode.setValue(nodeBinding.getRow().getAttribute("orgTwnCode"));
                        txtStateCode.setValue(nodeBinding.getRow().getAttribute("orgStsCode"));
                        txtStateName.setValue(nodeBinding.getRow().getAttribute("stateName"));
                        orgCouCode.setValue(nodeBinding.getRow().getAttribute("orgCouCode"));
                        orgEmail.setValue(nodeBinding.getRow().getAttribute("orgEmail"));
                        orgPhyAddrs.setValue(nodeBinding.getRow().getAttribute("orgPhyAddrs"));
                        orgCurCode.setValue(nodeBinding.getRow().getAttribute("orgCurCode"));
                        orgZip.setValue(nodeBinding.getRow().getAttribute("orgZip"));
                        orgFax.setValue(nodeBinding.getRow().getAttribute("orgFax"));
                        orgTel1.setValue(nodeBinding.getRow().getAttribute("orgTel1"));
                        orgTel2.setValue(nodeBinding.getRow().getAttribute("orgTel2"));
                        orgRptLogo.setValue(nodeBinding.getRow().getAttribute("orgRptLogo"));
                        orgMotto.setValue(nodeBinding.getRow().getAttribute("orgMotto"));
                        orgPinNo.setValue(nodeBinding.getRow().getAttribute("orgPinNo"));
                        orgEdCode.setValue(nodeBinding.getRow().getAttribute("orgEdCode"));
                        orgItemAccCode.setValue(nodeBinding.getRow().getAttribute("orgItemAccCode"));
                        orgOtherName.setValue(nodeBinding.getRow().getAttribute("orgOtherName"));
                        orgType.setValue(nodeBinding.getRow().getAttribute("orgType"));
                        orgWebBrnCode.setValue(nodeBinding.getRow().getAttribute("orgWebBrnCode"));
                        orgWebAddrs.setValue(nodeBinding.getRow().getAttribute("orgWebAddrs"));
                        orgAgnCode.setValue(nodeBinding.getRow().getAttribute("orgAgnCode"));
                        orgDirectors.setValue(nodeBinding.getRow().getAttribute("orgDirectors"));
                        orgLangCode.setValue(nodeBinding.getRow().getAttribute("orgLangCode"));
                        orgAvatar.setValue(nodeBinding.getRow().getAttribute("orgAvatar"));
                        txtVatNumber.setValue(nodeBinding.getRow().getAttribute("vatNumber"));
                        orgBaseCurrency.setValue(nodeBinding.getRow().getAttribute("curDesc"));
                        orgCountry.setValue(nodeBinding.getRow().getAttribute("couName"));
                        orgTown.setValue(nodeBinding.getRow().getAttribute("twnName"));
                        orgManager.setValue(nodeBinding.getRow().getAttribute("agnName"));
                        //Bank Details
                        txtOrgBankAccountName.setValue(nodeBinding.getRow().getAttribute("ORG_BANK_ACCOUNT_NAME"));
                        txtOrgBankAccountNo.setValue(nodeBinding.getRow().getAttribute("ORG_BANK_ACCOUNT_NO"));
                        txtOrgSwiftCode.setValue(nodeBinding.getRow().getAttribute("ORG_SWIFT_CODE"));
                        txtOrgBankBranch.setValue(nodeBinding.getRow().getAttribute("ORG_BANK_BRANCH"));
                        txtOrgBankName.setValue(nodeBinding.getRow().getAttribute("ORG_BANK_NAME"));
                        session.setAttribute("ORG_BNK_CODE",
                                             nodeBinding.getRow().getAttribute("ORG_BNK_CODE"));
                        session.setAttribute("ORG_BBR_CODE",
                                             nodeBinding.getRow().getAttribute("ORG_BBR_CODE"));

                        orgGrpLogoImg.setSource(null);
                        orgGrpLogoImg.setSource("/orggrpimageservlet?id=" +
                                                nodeBinding.getRow().getAttribute("orgCode"));

                        //organisation signature
                        orgAutoSignImg.setSource(null);
                        orgAutoSignImg.setSource("/certsignatureservelet?id=" +
                                                 nodeBinding.getRow().getAttribute("orgCode"));

                        // Org logo orgLogoImg
                        orgLogoImg.setSource("/orgimageservlet?id=" +
                                             nodeBinding.getRow().getAttribute("orgCode"));
                        session.setAttribute("orgLogo",
                                             nodeBinding.getRow().getAttribute("orgRptLogo"));
                        System.out.println("Image Logo path is : " +
                                           orgLogoImg.getSource());
                        System.out.println("Image Logo path is : " +
                                           orgGrpLogoImg.getSource());

                        session.setAttribute("countryCode",
                                             nodeBinding.getRow().getAttribute("orgCouCode"));
                        session.setAttribute("couZipCode",
                                             nodeBinding.getRow().getAttribute("couZipCode"));
                        if (slMobile1.isVisible()) {
                            ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
                            ADFUtils.findIterator("fetchAlLMobileTypePrefix2Iterator").executeQuery();
                        }
                        String prefix = null;
                        String smNo = null;
                        System.out.println(nodeBinding.getRow().getAttribute("mobile1"));
                        System.out.println(nodeBinding.getRow().getAttribute("couZipCode"));
                        int t = 0;
                        if (nodeBinding.getRow().getAttribute("mobile1") !=
                            null) {
                            if (nodeBinding.getRow().getAttribute("couZipCode") !=
                                null) {
                                prefix =
                                        nodeBinding.getRow().getAttribute("mobile1").toString().replace(nodeBinding.getRow().getAttribute("couZipCode").toString(),
                                                                                                        "");
                                prefix = prefix.replace("+", "0");
                                if (slMobile1.isVisible()) {
                                    List prefixArray = new ArrayList();
                                    if (session.getAttribute("mobilePrefix") !=
                                        null) {
                                        prefixArray =
                                                (List)session.getAttribute("mobilePrefix");
                                        int k = 0;
                                        System.out.println("Prefic");
                                        while (k < prefixArray.size()) {
                                            System.out.println(prefixArray.get(k));
                                            if (prefix.startsWith((String)prefixArray.get(k))) {
                                                smNo =
prefix.replace((CharSequence)prefixArray.get(k), "");
                                                prefix =
                                                        (String)prefixArray.get(k);
                                                t = k;
                                            }
                                            k++;
                                        }
                                        prefixArray = null;
                                    }
                                } else {
                                    smNo = prefix;
                                }
                            }
                        }
                        slMobile1.setValue(t);
                        txtMobile1.setValue(smNo);
                        System.out.println(t);
                        System.out.println(smNo);

                        prefix = null;
                        smNo = null;
                        t = 0;
                        if (nodeBinding.getRow().getAttribute("mobile2") !=
                            null) {
                            if (nodeBinding.getRow().getAttribute("couZipCode") !=
                                null) {
                                prefix =
                                        nodeBinding.getRow().getAttribute("mobile2").toString().replace(nodeBinding.getRow().getAttribute("couZipCode").toString(),
                                                                                                        "");
                                prefix = prefix.replace("+", "0");
                                if (slMobile2.isVisible()) {
                                    List prefixArray = new ArrayList();
                                    if (session.getAttribute("mobilePrefix") !=
                                        null) {
                                        prefixArray =
                                                (List)session.getAttribute("mobilePrefix");
                                        int k = 0;

                                        while (k < prefixArray.size()) {
                                            if (prefix.startsWith((String)prefixArray.get(k))) {
                                                smNo =
prefix.replace((CharSequence)prefixArray.get(k), "");
                                                prefix =
                                                        (String)prefixArray.get(k);
                                                t = k;
                                            }
                                            k++;
                                        }
                                        prefixArray = null;
                                    }
                                } else {
                                    smNo = prefix;
                                }
                            }
                        }
                        slMobile2.setValue(t);
                        txtMobile2.setValue(smNo);
                        AdfFacesContext adfFacesContext =
                            AdfFacesContext.getCurrentInstance();
                        adfFacesContext.addPartialTarget(slMobile1);
                        adfFacesContext.addPartialTarget(slMobile2);
                        adfFacesContext.addPartialTarget(txtMobile1);
                        adfFacesContext.addPartialTarget(txtMobile2);
                        String adminTYpe =
                            nodeBinding.getRow().getAttribute("administrativeType") !=
                            null ?
                            nodeBinding.getRow().getAttribute("administrativeType").toString() :
                            null;
                        if (adminTYpe == null)
                            adminTYpe = "S";
                        if (adminTYpe.equals("S"))
                            adminTYpe = "State";
                        else if (adminTYpe.equals("C"))
                            adminTYpe = "County";
                        else if (adminTYpe.equals("R"))
                            adminTYpe = "Region";
                        if (adminTYpe.equals("P"))
                            adminTYpe = "Province";
                        lblState.setLabel(adminTYpe);
                        GlobalCC.refreshUI(lblState);
                        ADFUtils.findIterator("fetchOrgDivisionsIterator").executeQuery();
                        GlobalCC.refreshUI(tblOrgDivisions);
                        GlobalCC.refreshUI(orgGrpLogoImg);
                        GlobalCC.refreshUI(orgAutoSignImg);
                        // Fetch the regions
                        ADFUtils.findIterator("findRegionsIterator").executeQuery();
                        regTab.setVisible(true);
                        GlobalCC.refreshUI(regionsTab);
                        GlobalCC.refreshUI(orgDetails);
                        mainTabPanel.setVisible(true);
                        GlobalCC.refreshUI(mainOrgPanelBox);
                    }
                }
            }
        } catch (Exception e) {
            String cEPTIONREPORTING = GlobalCC.EXCEPTIONREPORTING(e);
            e.printStackTrace();
        }
    }


    public String newOrganization() {

        orgCode.setValue(null);
        orgShortDesc.setValue(null);
        orgName.setValue(null);
        orgAddrs.setValue(null);
        orgTwnCode.setValue(null);
        orgCouCode.setValue(null);
        orgEmail.setValue(null);
        orgPhyAddrs.setValue(null);
        orgCurCode.setValue(null);
        orgZip.setValue(null);
        orgFax.setValue(null);
        orgTel1.setValue(null);
        orgTel2.setValue(null);
        orgRptLogo.setValue(null);
        orgMotto.setValue(null);
        orgPinNo.setValue(null);
        orgEdCode.setValue(null);
        orgItemAccCode.setValue(null);
        orgOtherName.setValue(null);
        orgType.setValue(null);
        orgWebBrnCode.setValue(null);
        orgWebAddrs.setValue(null);
        orgAgnCode.setValue(null);
        orgDirectors.setValue(null);
        orgLangCode.setValue(null);
        orgAvatar.setValue(null);
        orgManager.setValue(null);

        orgBaseCurrency.setValue(null);
        orgCountry.setValue(null);
        orgTown.setValue(null);

        txtStateCode.setValue(null);
        txtStateName.setValue(null);

        orgImg = null;
        orgGrpImg = null;
        orgImgByteLength = 0;
        orgGrpImgByteLength = 0;

        orgLogoImg.setSource(null);
        orgGrpLogoImg.setSource(null);
        orgTab.setDisclosed(true);
        regTab.setVisible(false);
        sdBranchTab.setVisible(false);
        sdAgenciesTab.setVisible(false);
        sdUnitsTab.setVisible(false);
        mainTabPanel.setVisible(true);
        session.setAttribute("ORGCode", null);

        ADFUtils.findIterator("findOrganizationIterator").executeQuery();
        GlobalCC.refreshUI(mainTabPanel);
        GlobalCC.refreshUI(orgTree);
        GlobalCC.refreshUI(orgTab);
        GlobalCC.refreshUI(mainOrgPanelBox);


        return null;
    }

    public String selectCountry() {
        Object key2 = countriesLovTab.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            //reiTrsShortDesc.setValue(r.getAttribute("trsName"));
            orgCouCode.setValue(nodeBinding.getAttribute("couCode"));
            orgCountry.setValue(nodeBinding.getAttribute("couName"));
            String adminTYpe =
                nodeBinding.getAttribute("administrativeType") != null ?
                nodeBinding.getAttribute("administrativeType").toString() :
                null;
            System.out.println("adminTYpe=" + adminTYpe);
            if (adminTYpe == null)
                adminTYpe = "S";
            if (adminTYpe.equals("S"))
                adminTYpe = "State";
            else if (adminTYpe.equals("C"))
                adminTYpe = "County";
            else if (adminTYpe.equals("R"))
                adminTYpe = "Region";
            if (adminTYpe.equals("P"))
                adminTYpe = "Province";
            session.setAttribute("COUCode",
                                 nodeBinding.getAttribute("couCode"));
            session.setAttribute("countryCode",
                                 nodeBinding.getAttribute("couCode"));
            session.setAttribute("couZipCode",
                                 nodeBinding.getAttribute("couZipCode"));
            ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").executeQuery();
            ADFUtils.findIterator("fetchAlLMobileTypePrefix2Iterator").executeQuery();
            AdfFacesContext adfFacesContext =
                AdfFacesContext.getCurrentInstance();
            adfFacesContext.addPartialTarget(orgCouCode);
            adfFacesContext.addPartialTarget(orgCountry);
            adfFacesContext.addPartialTarget(slMobile1);
            adfFacesContext.addPartialTarget(slMobile2);
            lblState.setLabel(adminTYpe);
            txtStateCode.setValue(null);
            txtStateName.setValue(null);
            orgTwnCode.setValue(null);
            orgTown.setValue(null);
            adfFacesContext.addPartialTarget(orgTown);
            adfFacesContext.addPartialTarget(txtStateName);
            adfFacesContext.addPartialTarget(orgBaseCurrency);
            adfFacesContext.addPartialTarget(lblState);


        }
        GlobalCC.dismissPopUp("fms", "countriesLovPopUp");
        return null;
    }

    public String newTown() {

        if (session.getAttribute("COUCode") == null) {
            GlobalCC.errorValueNotEntered("Error: You Have To Select Country First");
            return null;
        }


        return null;
    }

    public String selectTown() {

        RowKeySet rowKeySet = townsLovTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        townsLovTab.setRowKey(key2);

        JUCtrlValueBinding r = (JUCtrlValueBinding)townsLovTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        //reiTrsShortDesc.setValue(r.getAttribute("trsName"));
        orgTwnCode.setValue(r.getAttribute("twnCode"));
        orgTown.setValue(r.getAttribute("twnName"));

        orgZip.setValue(r.getAttribute("twnPostalCode"));
        // bnsPostalCode.setValue(r.getAttribute("twnPostalCode"));


        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
        adfFacesContext.addPartialTarget(orgTwnCode);
        adfFacesContext.addPartialTarget(orgTown);

        GlobalCC.dismissPopUp("fms", "townsLovPopUp");
        return null;
    }

    public String selectCurrency() {
        RowKeySet rowKeySet = currenciesLovTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        currenciesLovTab.setRowKey(key2);

        JUCtrlValueBinding r =
            (JUCtrlValueBinding)currenciesLovTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        //reiTrsShortDesc.setValue(r.getAttribute("trsName"));
        orgCurCode.setValue(r.getAttribute("curCode"));
        orgBaseCurrency.setValue(r.getAttribute("curDesc"));


        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
        adfFacesContext.addPartialTarget(orgCurCode);
        adfFacesContext.addPartialTarget(orgBaseCurrency);

        GlobalCC.dismissPopUp("fms", "currenciesLovPopUp");
        return null;
    }

    public String selectOrgManager() {

        RowKeySet rowKeySet = orgManagersLovTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        orgManagersLovTab.setRowKey(key2);

        JUCtrlValueBinding r =
            (JUCtrlValueBinding)orgManagersLovTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        orgAgnCode.setValue(r.getAttribute("agnCode"));
        orgManager.setValue(r.getAttribute("agnName"));


        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
        adfFacesContext.addPartialTarget(orgAgnCode);
        adfFacesContext.addPartialTarget(orgManager);
        GlobalCC.dismissPopUp("fms", "orgManagersLovPopUp");


        return null;
    }

    public String saveOrganization() {

        String orgCodeVal;
        String orgShortDescVal;
        String orgNameVal;
        String orgAddrsVal;
        String orgTwnCodeVal;
        String orgCouCodeVal;
        String orgEmailVal;
        String orgPhyAddrsVal;
        String orgCurCodeVal;
        String orgZipVal;
        String orgFaxVal;
        String orgTel1Val;
        String orgTel2Val;
        String orgRptLogoVal;
        String orgMottoVal;
        String orgPinNoVal;
        String orgEdCodeVal;
        String orgItemAccCodeVal;
        String orgOtherNameVal;
        String orgTypeVal;
        String orgWebBrnCodeVal;
        String orgWebAddrsVal;
        String orgAgnCodeVal;
        String orgDirectorsVal;
        String orgLangCodeVal;
        String orgAvatarVal;
        String orgStateCodeVal;
        String orgStateNameVal;

        orgCodeVal = GlobalCC.checkNullValues(orgCode.getValue());
        orgShortDescVal = GlobalCC.checkNullValues(orgShortDesc.getValue());
        orgNameVal = GlobalCC.checkNullValues(orgName.getValue());
        orgAddrsVal = GlobalCC.checkNullValues(orgAddrs.getValue());
        orgTwnCodeVal = GlobalCC.checkNullValues(orgTwnCode.getValue());
        orgCouCodeVal = GlobalCC.checkNullValues(orgCouCode.getValue());
        orgEmailVal = GlobalCC.checkNullValues(orgEmail.getValue());
        orgPhyAddrsVal = GlobalCC.checkNullValues(orgPhyAddrs.getValue());
        orgCurCodeVal = GlobalCC.checkNullValues(orgCurCode.getValue());
        orgZipVal = GlobalCC.checkNullValues(orgZip.getValue());
        orgFaxVal = GlobalCC.checkNullValues(orgFax.getValue());
        orgTel1Val = GlobalCC.checkNullValues(orgTel1.getValue());
        orgTel2Val = GlobalCC.checkNullValues(orgTel2.getValue());
        orgRptLogoVal = GlobalCC.checkNullValues(orgRptLogo.getValue());
        orgMottoVal = GlobalCC.checkNullValues(orgMotto.getValue());
        orgPinNoVal = GlobalCC.checkNullValues(orgPinNo.getValue());
        orgEdCodeVal = GlobalCC.checkNullValues(orgEdCode.getValue());
        orgItemAccCodeVal =
                GlobalCC.checkNullValues(orgItemAccCode.getValue());
        orgOtherNameVal = GlobalCC.checkNullValues(orgOtherName.getValue());
        orgTypeVal = GlobalCC.checkNullValues(orgType.getValue());
        orgWebBrnCodeVal = GlobalCC.checkNullValues(orgWebBrnCode.getValue());
        orgWebAddrsVal = GlobalCC.checkNullValues(orgWebAddrs.getValue());
        orgAgnCodeVal = GlobalCC.checkNullValues(orgAgnCode.getValue());
        orgDirectorsVal = GlobalCC.checkNullValues(orgDirectors.getValue());
        orgLangCodeVal = GlobalCC.checkNullValues(orgLangCode.getValue());
        orgAvatarVal = GlobalCC.checkNullValues(orgAvatar.getValue());
        orgStateCodeVal = GlobalCC.checkNullValues(txtStateCode.getValue());
        UploadedFile file = (UploadedFile)userSignature.getValue();


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;
        String query = null;
        try {
            if (orgShortDescVal == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description");
                return null;
            }

            if (orgNameVal == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Name");
                return null;
            }

            if (orgEmailVal != null) {
                if (!UtilMethods.isEmailValid(orgEmailVal)) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: The email entered is not a valid email address.");
                    return null;
                }
            }

            String mobile1 = GlobalCC.checkNullValues(txtMobile1.getValue());
            String mobile2 = GlobalCC.checkNullValues(txtMobile2.getValue());
            String prefix = GlobalCC.checkNullValues(slMobile1.getValue());
            String prefix2 = GlobalCC.checkNullValues(slMobile2.getValue());
            if (slMobile1.isVisible()) {
                if (prefix != null) {
                    Row row =
                        ADFUtils.findIterator("fetchAlLMobileTypePrefixIterator").getRowAtRangeIndex(new Integer(prefix));
                    if (row.getAttribute("prefix") != null) {
                        prefix = row.getAttribute("prefix").toString();
                        if (prefix.startsWith("0")) {
                            prefix = prefix.replaceFirst("0", "");
                        }
                        if (session.getAttribute("couZipCode") != null) {
                            mobile1 =
                                    "+" + session.getAttribute("couZipCode").toString() +
                                    prefix + "" + mobile1;
                        } else {
                            mobile1 = prefix + "" + mobile1;
                        }
                    }
                } else {
                    if (mobile1 != null && "".equals(mobile1) != true) {
                        if (mobile1.startsWith("0") &&
                            (mobile2 != null && "".equals(mobile2) != true)) {
                            mobile1 =
                                    mobile2.length() > 1 ? mobile2.substring(1) :
                                    "";
                            mobile1 =
                                    "+" + session.getAttribute("couZipCode").toString() +
                                    mobile1;
                        } else {
                            mobile1 =
                                    "+" + session.getAttribute("couZipCode").toString() +
                                    mobile1;
                        }
                    }
                }
            } else {
                if (mobile1 != null && "".equals(mobile1) != true) {
                    if (mobile1.startsWith("0") &&
                        (mobile2 != null && "".equals(mobile2) != true)) {
                        mobile1 =
                                mobile2.length() > 1 ? mobile2.substring(1) : "";
                        mobile1 =
                                "+" + session.getAttribute("couZipCode").toString() +
                                mobile1;
                    } else {
                        mobile1 =
                                "+" + session.getAttribute("couZipCode").toString() +
                                mobile1;
                    }
                }
            }
            if (slMobile2.isVisible()) {
                if (prefix2 != null) {
                    Row row =
                        ADFUtils.findIterator("fetchAlLMobileTypePrefix2Iterator").getRowAtRangeIndex(new Integer(prefix2));
                    if (row!=null && row.getAttribute("prefix") != null) {
                        prefix2 = row.getAttribute("prefix").toString();
                        if (prefix2.startsWith("0")) {
                            prefix2 = prefix2.replaceFirst("0", "");
                        }
                        if (session.getAttribute("couZipCode") != null) {
                            mobile2 =
                                    "+" + session.getAttribute("couZipCode").toString() +
                                    prefix2 + "" + mobile2;
                        } else {
                            mobile2 = prefix2 + "" + mobile2;
                        }
                    }
                } else {
                    if (session.getAttribute("couZipCode") != null &&
                        (mobile2 != null && "".equals(mobile2) != true)) {
                        if (mobile2.startsWith("0")) {
                            mobile2 =
                                    mobile2.length() > 1 ? mobile2.substring(1) :
                                    null;
                            mobile2 =
                                    "+" + session.getAttribute("couZipCode").toString() +
                                    mobile2;
                        } else {
                            mobile2 =
                                    "+" + session.getAttribute("couZipCode").toString() +
                                    mobile2;
                        }
                    }
                }
            } else {
                if (mobile2 != null && "".equals(mobile2) != true) {
                    if (mobile2.startsWith("0")) {
                        mobile2 =
                                mobile2.length() > 1 ? mobile2.substring(1) : null;
                        mobile2 =
                                "+" + session.getAttribute("couZipCode").toString() +
                                mobile2;
                    } else {
                        mobile2 =
                                "+" + session.getAttribute("couZipCode").toString() +
                                mobile2;
                    }
                }
            }
            System.out.println(mobile2);
            conn = dbConnector.getDatabaseConnection();
            query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_ORGANIZATION(" + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?" + "); end;";

            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables
            String option = null;
            if (orgCodeVal == null) {
                option = "A";
            } else {
                option = "E";
            }

            callStmt.setString(1, option);
            callStmt.setString(2, orgCodeVal);
            callStmt.setString(3, orgShortDescVal);
            callStmt.setString(4, orgNameVal);

            callStmt.setString(5, orgAddrsVal);
            callStmt.setString(6, orgTwnCodeVal);
            callStmt.setString(7, orgCouCodeVal);

            callStmt.setString(8, orgEmailVal);
            callStmt.setString(9, orgPhyAddrsVal);
            callStmt.setString(10, orgCurCodeVal);

            callStmt.setString(11, orgZipVal);
            callStmt.setString(12, orgFaxVal);
            callStmt.setString(13, orgTel1Val);
            callStmt.setString(14, orgTel2Val);


            UploadedFile _file = (UploadedFile)inputFileOrgImgFile.getValue();
            InputStream inp = null;

            if (_file != null) {
                inp = validateUploadedImg(_file, "Org logo");
                if (inp == null) {
                    inputFileOrgImgFile.setValue(null);
                    GlobalCC.refreshUI(inputFileOrgImgFile);
                    return null;

                } else {
                    inp = _file.getInputStream();
                }
            } else {
                inp = (InputStream)session.getAttribute("orglogo");
            }


            //System.out.println(String.valueOf(orgImgByteLength));
            callStmt.setBlob(15, inp); //orgRptLogoVal);
            callStmt.setString(16, orgMottoVal);
            callStmt.setString(17, orgPinNoVal);

            callStmt.setString(18, orgEdCodeVal);
            callStmt.setString(19, orgItemAccCodeVal);
            callStmt.setString(20, orgOtherNameVal);

            callStmt.setString(21, orgTypeVal);
            callStmt.setString(22, orgWebBrnCodeVal);
            callStmt.setString(23, orgWebAddrsVal);

            callStmt.setString(24, orgAgnCodeVal);
            callStmt.setString(25, orgDirectorsVal);
            callStmt.setString(26, orgLangCodeVal);
            callStmt.setString(27, orgAvatarVal);
            callStmt.setBigDecimal(28,
                                   orgStateCodeVal == null ? null : new BigDecimal(orgStateCodeVal));

            UploadedFile _file2 = (UploadedFile)uploadOrgGrpImg.getValue();
            InputStream inpGrpLogo = null;
            InputStream rptLogo = null;

            if (_file2 != null) {
                inpGrpLogo = validateUploadedImg(_file2, "Grp logo");
                if (inpGrpLogo == null) {
                    uploadOrgGrpImg.setValue(null);
                    GlobalCC.refreshUI(uploadOrgGrpImg);
                    return null;

                } else {
                    inpGrpLogo = _file2.getInputStream();
                }
            }

            callStmt.setBlob(29, inpGrpLogo); //orgGrpRptLogoVal);
            callStmt.registerOutParameter(30, OracleTypes.VARCHAR);
            if (_file2 != null)
                callStmt.setBytes(31,
                                  Base64.encodeBytesToBytes(GlobalCC.getBytes(_file2.getInputStream())));
            else
                callStmt.setBytes(31, null);
            callStmt.setObject(32, txtVatNumber.getValue());
            callStmt.setObject(33, mobile1);
            callStmt.setObject(34, mobile2);

            if (autoSignStream != null) {
                //checking the dimensions of the uploaded file.
                BufferedImage bi = ImageIO.read(autoSignStream);
                if (bi != null) {
                    int height = bi.getHeight();
                    int width = bi.getWidth();
                    System.out.println("TEST  height== " + height +
                                       " width==" + width);
                    if (height <= 36 && width <= 39) {
                        callStmt.setBlob(35, autoSignStream);
                    } else {
                        GlobalCC.INFORMATIONREPORTING("The Uploaded Certificate Signature File dimensions(" +
                                                      height + "px by " +
                                                      width +
                                                      "px) should be width-39px and height-36px .");
                        return null;
                    }
                } else {
                    callStmt.setBlob(35, autoSignStream);
                }
            } else {
                callStmt.setBlob(35, autoSignStream);
            }

            callStmt.setObject(36,
                               GlobalCC.checkNullValues(txtOrgBankAccountName.getValue()));
            callStmt.setObject(37,
                               GlobalCC.checkNullValues(txtOrgBankAccountNo.getValue()));
            callStmt.setObject(38,
                               GlobalCC.checkNullValues(txtOrgSwiftCode.getValue()));
            callStmt.setObject(39,
                               GlobalCC.checkBDNullValues(session.getAttribute("ORG_BNK_CODE")));
            callStmt.setObject(40,
                               GlobalCC.checkBDNullValues(session.getAttribute("ORG_BBR_CODE")));

            callStmt.execute();

            String errMessage = null;
            errMessage = callStmt.getString(30);

            callStmt.close();
            conn.commit();
            conn.close();

            if (errMessage != null) {
                GlobalCC.errorValueNotEntered(errMessage);
                return null;
            }
            GlobalCC.refreshUI(inputFileOrgImgFile);
            // Org logo orgLogoImg
            GlobalCC.refreshUI(userSignature);
            GlobalCC.refreshUI(orgGrpLogoImg);
            GlobalCC.refreshUI(orgLogoImg);
            GlobalCC.refreshUI(uploadOrgGrpImg);
            ADFUtils.findIterator("findOrganizationIterator").executeQuery();
            GlobalCC.refreshUI(orgTree);
            GlobalCC.refreshUI(orgAutoSignImg);
            GlobalCC.INFORMATIONREPORTING("Record Saved Successfully.");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public void regionSelected(SelectionEvent evt) {

        if (evt.getAddedSet() != evt.getRemovedSet()) {
            RowKeySet rowKeySet = regionsTab.getSelectedRowKeys();
            Object key2 = rowKeySet.iterator().next();
            regionsTab.setRowKey(key2);
            JUCtrlValueBinding n = (JUCtrlValueBinding)regionsTab.getRowData();

            session.setAttribute("REGCode", n.getAttribute("regCode"));
            session.setAttribute("REGName", n.getAttribute("regName"));

            ADFUtils.findIterator("findBranchesIterator").executeQuery();
            ADFUtils.findIterator("findBankRegionsIterator").executeQuery();

            GlobalCC.refreshUI(bankRegionsTab);
            GlobalCC.refreshUI(branchesTab);

            btnDeleteRegions.setDisabled(false);
            btnEditRegions.setDisabled(false);
            GlobalCC.refreshUI(btnDeleteRegions);
            GlobalCC.refreshUI(btnEditRegions);

            sdBranchTab.setVisible(true);
            GlobalCC.refreshUI(mainOrgPanelBox);


        }
    }

    public void bankRegionSelected(SelectionEvent evt) {

        RowKeySet rowKeySet = bankRegionsTab.getSelectedRowKeys();
        Object key2 = rowKeySet.iterator().next();
        bankRegionsTab.setRowKey(key2);
        JUCtrlValueBinding nodeBinding =
            (JUCtrlValueBinding)bankRegionsTab.getRowData();

        session.setAttribute("bnkrCode", nodeBinding.getAttribute("bnkrCode"));

        ADFUtils.findIterator("findBranchesIterator").executeQuery();
        GlobalCC.refreshUI(branchesTab);

        btnDeleteBankRegion.setDisabled(false);
        btnEditBankRegion.setDisabled(false);
        GlobalCC.refreshUI(btnDeleteBankRegion);
        GlobalCC.refreshUI(btnEditBankRegion);

        sdBranchTab.setVisible(true);
        GlobalCC.refreshUI(mainOrgPanelBox);
    }

    public void branchSelected(SelectionEvent evt) {

        if (evt.getAddedSet() != evt.getRemovedSet()) {
            RowKeySet rowKeySet = branchesTab.getSelectedRowKeys();
            Object key2 = rowKeySet.iterator().next();
            branchesTab.setRowKey(key2);
            JUCtrlValueBinding n =
                (JUCtrlValueBinding)branchesTab.getRowData();
            if (n != null) {
                session.setAttribute("BRNCode", n.getAttribute("brnCode"));
                session.setAttribute("BRNName", n.getAttribute("brnName"));           
              
                //session.setAttribute("braCode", nodeBinding.getAttribute("brnCode"));

            }


            ADFUtils.findIterator("findBranchUnitsIterator").executeQuery();
            GlobalCC.refreshUI(unitsTab);

            ADFUtils.findIterator("findBranchAgenciesIterator").executeQuery();
            GlobalCC.refreshUI(agenciesTab);

            // Fetch Branch Divisions
            ADFUtils.findIterator("findBranchUnassignedDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(treeUnassignedBranchDivs);
            ADFUtils.findIterator("fetchDivisionsByBranchIterator").executeQuery();
            GlobalCC.refreshUI(tblBranchDivs);
            ADFUtils.findIterator("findBranchContactsIterator").executeQuery();
            GlobalCC.refreshUI(contactsTab);

            btnDeleteBranch.setDisabled(false);
            btnEditBranch.setDisabled(false);
            GlobalCC.refreshUI(btnDeleteBranch);
            GlobalCC.refreshUI(btnEditBranch);

            sdAgenciesTab.setVisible(true);

            sdUnitsTab.setVisible(true);
            GlobalCC.refreshUI(mainOrgPanelBox);
        }
    }


    public String newOrgRegion() {
        if (session.getAttribute("ORGCode") == null) {
            GlobalCC.errorValueNotEntered("Error: Select Organization");
            return null;
        }

        GlobalCC.showPopup("fms:regionsPopUp");

        regCode.setValue(null);
        regOrgCode.setValue(session.getAttribute("ORGCode"));
        regShortDesc.setValue(null);
        regName.setValue(null);
        regWef.setValue(null);
        regWet.setValue(null);
        regAgnCode.setValue(null);
        regPostLevel.setValue(null);
        regMngrAllowed.setValue(null);
        regOverideCommEarned.setValue(null);
        regManager.setValue(null);

        //regManager.setVisible(false);
        //btnShowManagerLov.setVisible(false);
        //outputLbManager.setVisible(false);

        //GlobalCC.refreshUI(regManager);
        //GlobalCC.refreshUI(outputLbManager);
        //GlobalCC.refreshUI(btnShowManagerLov);

        return null;
    }

    public String newBankRegion() {
        if (session.getAttribute("ORGCode") == null) {
            GlobalCC.errorValueNotEntered("Error: Select Organization");
            return null;
        }
        clearBankRegion();
        btnSaveBankRegion.setText("Add");
        bnkrOrgCode.setValue(session.getAttribute("ORGCode"));
        bnkrRegCode.setValue(session.getAttribute("REGCode"));

        GlobalCC.refreshUI(btnSaveBankRegion);

        GlobalCC.showPopup("fms:bankRegionsPopUp");

        return null;
    }

    public String editBankRegion() {

        RowKeySet rowKeySet = bankRegionsTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        bankRegionsTab.setRowKey(key2);

        JUCtrlValueBinding r = (JUCtrlValueBinding)bankRegionsTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        clearBankRegion();
        btnSaveBankRegion.setText("Edit");
        bnkrCode.setValue(r.getAttribute("bnkrCode"));
        bnkrOrgCode.setValue(session.getAttribute("ORGCode"));
        bnkrRegCode.setValue(session.getAttribute("REGCode"));
        bnkrShtDesc.setValue(r.getAttribute("bnkrShtDesc"));
        bnkrName.setValue(r.getAttribute("bnkrName"));
        bnkrWef.setValue(r.getAttribute("bnkrWef"));
        bnkrWet.setValue(r.getAttribute("bnkrWet"));
        bnkrAgnCode.setValue(r.getAttribute("bnkrAgnCode"));
        bnkrManager.setValue(r.getAttribute("bnkrManager"));

        GlobalCC.refreshUI(btnSaveBankRegion);

        GlobalCC.showPopup("fms:bankRegionsPopUp");

        return null;
    }

    public String viewOrgRegion() {

        RowKeySet rowKeySet = regionsTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        regionsTab.setRowKey(key2);

        JUCtrlValueBinding r = (JUCtrlValueBinding)regionsTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        regManager.setVisible(true);
        btnShowManagerLov.setVisible(true);
        outputLbManager.setVisible(true);
        GlobalCC.refreshUI(regManager);
        GlobalCC.refreshUI(outputLbManager);
        GlobalCC.refreshUI(btnShowManagerLov);
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:regionsPopUp" + "').show(hints);");

        regCode.setValue(r.getAttribute("regCode"));
        regOrgCode.setValue(r.getAttribute("regOrgCode"));
        regShortDesc.setValue(r.getAttribute("regShortDesc"));
        regName.setValue(r.getAttribute("regName"));
        regWef.setValue(r.getAttribute("regWef"));
        regWet.setValue(r.getAttribute("regWet"));
        regAgnCode.setValue(r.getAttribute("regAgnCode"));
        regPostLevel.setValue(r.getAttribute("regPostLevel"));
        regMngrAllowed.setValue(r.getAttribute("regMngrAllowed"));
        regOverideCommEarned.setValue(r.getAttribute("regOverideCommEarned"));
        regManager.setValue(r.getAttribute("regManager"));
        txtRegBranchMgrSeqNo.setValue(r.getAttribute("regBrnMngrSeq_no"));
        txtRegAgnSeqNo.setValue(r.getAttribute("regAgnSeqNo"));

        GlobalCC.refreshUI(regCode);
        GlobalCC.refreshUI(regionsPopUp);

        return null;
    }


    public HashMap generateSeqNo(String act_type, BigDecimal regCode,
                                 BigDecimal brnCode, BigDecimal braCode,
                                 BigDecimal bruCode, BigDecimal agnCode,
                                 String braPostLevel) {
        HashMap map = new HashMap();
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        try {
            String query = null;
            OracleCallableStatement callStmt = null;
            if (GlobalCC.getSysParamValue("CLIENT").equalsIgnoreCase("CUSTODIAN")) {
                query =
                        "begin TQ_LMS.Lms_ord_misc.Generate_AgentNo(?,?,?,?,?,?,?,?,?,?); end;";
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                callStmt.setString(1, act_type);
                callStmt.registerOutParameter(2, 12);
                callStmt.setBigDecimal(3, regCode);
                callStmt.setBigDecimal(4, brnCode);
                callStmt.setBigDecimal(5, braCode);
                callStmt.setBigDecimal(6, bruCode);
                callStmt.setBigDecimal(7, agnCode);
                callStmt.registerOutParameter(8, 12);
                callStmt.setString(9, null);
                callStmt.setString(10, braPostLevel);
            }    else {
                query =
                        "begin TQ_LMS.Lms_ord_misc.Generate_AgentNo(?,?,?,?,?,?,?,?); end;";
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                callStmt.setString(1, act_type);
                callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
                callStmt.setBigDecimal(3, regCode);
                callStmt.setBigDecimal(4, brnCode);
                callStmt.setBigDecimal(5, braCode);
                callStmt.setBigDecimal(6, bruCode);
                callStmt.setBigDecimal(7, agnCode);
                callStmt.registerOutParameter(8, OracleTypes.VARCHAR);
            }

            callStmt.execute();
            String agnSeqNo = callStmt.getString(2);
            regShortDesc.setValue(agnSeqNo);
            System.out.println("agnSeqNo--" + agnSeqNo);
            map.put("seq", agnSeqNo);
            map.put("name", callStmt.getString(8));

            return map;
            //bind the variables

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }

    }

    public String selectRegionManager() {

        RowKeySet rowKeySet = regManagerslovTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        regManagerslovTab.setRowKey(key2);

        JUCtrlValueBinding r =
            (JUCtrlValueBinding)regManagerslovTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        BigDecimal orgCode = (BigDecimal)session.getAttribute("ORGCode");
        System.out.println("OrgCode" + orgCode);
        if (orgCode != null) {
            BigDecimal code = orgCode;
            if (orgCode.compareTo(BigDecimal.ONE) == 0) {
                HashMap map = new HashMap();
                String regcode = GlobalCC.checkNullValues(regCode.getValue());
                map =
generateSeqNo("RM", regcode == null ? null : new BigDecimal(regcode), null,
              null, null, new BigDecimal(r.getAttribute("agnCode").toString()),
              "N");
                System.out.println("regcode: " + regcode);
                if (map != null) {
                    braShortDesc.setValue(map.get("seq"));
                    braName.setValue(map.get("name"));
                }
            }
        }
        System.out.println("agnCode: " + r.getAttribute("agnCode") +
                           " agnName: " + r.getAttribute("agnName"));
        regAgnCode.setValue(r.getAttribute("agnCode"));
        regManager.setValue(r.getAttribute("agnName"));


        GlobalCC.hidePopup("fms:regManagerslovPopUp");

        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();

        adfFacesContext.addPartialTarget(regAgnCode);
        adfFacesContext.addPartialTarget(regManager);


        return null;
    }

    public String selectBankRegionManager() {

        RowKeySet rowKeySet = bankRegMgrsLovTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        bankRegMgrsLovTab.setRowKey(key2);

        JUCtrlValueBinding r =
            (JUCtrlValueBinding)bankRegMgrsLovTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        System.out.println("agnCode: " + r.getAttribute("agnCode") +
                           " agnName: " + r.getAttribute("agnName"));

        bnkrAgnCode.setValue(r.getAttribute("agnCode"));
        bnkrManager.setValue(r.getAttribute("agnName"));

        GlobalCC.hidePopup("fms:bankRegMgrsLovPopUp");

        GlobalCC.refreshUI(bnkrAgnCode);
        GlobalCC.refreshUI(bnkrManager);

        return null;
    }


    public String saveOrgRegion() {

        String regCodeVal;
        String regOrgCodeVal;
        String regShortDescVal;
        String regNameVal;
        String regWefVal;
        String regWetVal;
        String regAgnCodeVal;
        String regPostLevelVal;
        String regMngrAllowedVal;
        String regOverideCommEarnedVal;
        String regBraMgrseqNo;
        String regAgnSeqNo;
        regCodeVal = GlobalCC.checkNullValues(regCode.getValue());
        regOrgCodeVal = GlobalCC.checkNullValues(regOrgCode.getValue());
        regShortDescVal = GlobalCC.checkNullValues(regShortDesc.getValue());
        regNameVal = GlobalCC.checkNullValues(regName.getValue());
        regWefVal = GlobalCC.checkNullValues(regWef.getValue());
        regWetVal = GlobalCC.checkNullValues(regWet.getValue());
        regAgnCodeVal = GlobalCC.checkNullValues(regAgnCode.getValue());
        regPostLevelVal = GlobalCC.checkNullValues(regPostLevel.getValue());
        regMngrAllowedVal =
                GlobalCC.checkNullValues(regMngrAllowed.getValue());
        regOverideCommEarnedVal =
                GlobalCC.checkNullValues(regOverideCommEarned.getValue());
        regBraMgrseqNo =
                GlobalCC.checkNullValues(txtRegBranchMgrSeqNo.getValue());
        regAgnSeqNo = GlobalCC.checkNullValues(txtRegAgnSeqNo.getValue());

        FormUi formUi = new FormUi();
        if (!formUi.validate("OrgRegionAddTab")) { //main validation engine
            return null;
        }

        if (regBraMgrseqNo != null) {
            try {
                int x = Integer.parseInt(regBraMgrseqNo);
            } catch (Exception e) {
                GlobalCC.INFORMATIONREPORTING("Ensure BraMgr seqNo is Numeric");
                return null;
            }
        }
        if (regAgnSeqNo != null) {
            try {
                int x = Integer.parseInt(regAgnSeqNo);
            } catch (Exception e) {
                GlobalCC.INFORMATIONREPORTING("Ensure Agent  SeqNo is Numeric");
                return null;
            }
        }


        if (regWefVal == null) {

        } else {
            if (regWefVal.contains(":")) {
                regWefVal = GlobalCC.parseDate(regWefVal);
            } else {
                regWefVal = GlobalCC.upDateParseDate(regWefVal);
            }
        }

        if (regWetVal == null) {

        } else {
            if (regWetVal.contains(":")) {
                regWetVal = GlobalCC.parseDate(regWetVal);
            } else {
                regWetVal = GlobalCC.upDateParseDate(regWetVal);
            }
        }

        if (regOrgCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select Organization");
            return null;
        }

        if (regShortDescVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description");
            return null;
        }
        if (regNameVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Name");
            return null;
        }
        if (regWefVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: WEF");
            return null;
        }


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        try {
            String query = null;
            //modify the query for treaty groups procedure
            query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_REGIONS(?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";


            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables

            String option = null;
            if (regCodeVal == null) {
                option = "A";
            } else {
                option = "E";
            }

            callStmt.setString(1, option);
            callStmt.setString(2, regCodeVal);
            callStmt.setString(3, regOrgCodeVal);
            callStmt.setString(4, regShortDescVal);
            callStmt.setString(5, regNameVal);
            callStmt.setString(6, regWefVal);
            callStmt.setString(7, regWetVal);
            callStmt.setString(8, regAgnCodeVal);
            callStmt.setString(9, regPostLevelVal);
            callStmt.setString(10, regMngrAllowedVal);
            callStmt.setString(11, regOverideCommEarnedVal);
            callStmt.setString(12, regBraMgrseqNo);
            callStmt.setString(13, regAgnSeqNo);
            callStmt.registerOutParameter(14, OracleTypes.VARCHAR);


            callStmt.execute();
            String errMessage = null;
            errMessage = callStmt.getString(14);

            callStmt.close();
            conn.commit();
            conn.close();

            if (errMessage != null) {
                GlobalCC.INFORMATIONREPORTING(errMessage);
                return null;
            }

            regCode.setValue(null);
            regOrgCode.setValue(null);
            regShortDesc.setValue(null);
            regName.setValue(null);
            regWef.setValue(null);
            regWet.setValue(null);
            regAgnCode.setValue(null);
            regPostLevel.setValue(null);
            regMngrAllowed.setValue(null);
            regOverideCommEarned.setValue(null);
            regManager.setValue(null);
            txtRegBranchMgrSeqNo.setValue(null);

            txtRegAgnSeqNo.setValue(null);

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:regionsPopUp" + "').hide(hints);");
            ADFUtils.findIterator("findRegionsIterator").executeQuery();
            GlobalCC.refreshUI(regionsTab);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public String saveBankRegion() {


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;

        BigDecimal vbnkrCode = GlobalCC.checkBDNullValues(bnkrCode.getValue());
        BigDecimal vbnkrOrgCode =
            GlobalCC.checkBDNullValues(bnkrOrgCode.getValue());
        ;
        String vbnkrShtDesc = GlobalCC.checkNullValues(bnkrShtDesc.getValue());
        ;
        String vbnkrName = GlobalCC.checkNullValues(bnkrName.getValue());
        ;
        String vbnkrWef = GlobalCC.checkNullValues(bnkrWef.getValue());
        ;
        String vbnkrWet = GlobalCC.checkNullValues(bnkrWet.getValue());
        BigDecimal vbnkrRegCode =
            GlobalCC.checkBDNullValues(bnkrRegCode.getValue());
        BigDecimal vbnkrAgnCode =
            GlobalCC.checkBDNullValues(bnkrAgnCode.getValue());
        String save = GlobalCC.checkNullValues(btnSaveBankRegion.getText());
        ;

        String action = "";

        try {

            if ("Add".equals(save)) {
                action = "A";
            }
            if ("Edit".equals(save)) {
                action = "E";
            }

            if (vbnkrWef == null) {

            } else {
                if (vbnkrWef.contains(":")) {
                    vbnkrWef = GlobalCC.parseDate(vbnkrWef);
                } else {
                    vbnkrWef = GlobalCC.upDateParseDate(vbnkrWef);
                }
            }

            if (vbnkrWet == null) {

            } else {
                if (vbnkrWet.contains(":")) {
                    vbnkrWet = GlobalCC.parseDate(vbnkrWet);
                } else {
                    vbnkrWet = GlobalCC.upDateParseDate(vbnkrWet);
                }
            }

            if (GlobalCC.isEmptyBD(vbnkrOrgCode)) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select Organization");
                return null;
            }

            if (GlobalCC.isEmptyStr(vbnkrShtDesc)) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Short Description");
                return null;
            }

            if (GlobalCC.isEmptyStr(vbnkrName)) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Name");
                return null;
            }
            if (GlobalCC.isEmptyStr(vbnkrWef)) {
                GlobalCC.errorValueNotEntered("Error Value Missing: WEF");
                return null;
            }

            conn = dbConnector.getDatabaseConnection();

            String query = null;
            //modify the query for treaty groups procedure
            query =
                    "begin TQC_WEB_ORGANIZATION_PKG.bank_region_prc(?,?,?,?,?,?,?,?,?,?); end;";


            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables

            callStmt.setString(1, action);
            callStmt.setObject(2, vbnkrCode);
            callStmt.setObject(3, vbnkrOrgCode);
            callStmt.setString(4, vbnkrShtDesc);
            callStmt.setString(5, vbnkrName);
            callStmt.setString(6, vbnkrWef);
            callStmt.setString(7, vbnkrWet);
            callStmt.setObject(8, vbnkrRegCode);
            callStmt.setObject(9, vbnkrAgnCode);
            callStmt.registerOutParameter(10, OracleTypes.VARCHAR);


            callStmt.execute();
            String errMessage = null;
            errMessage = callStmt.getString(10);

            callStmt.close();
            conn.commit();
            conn.close();

            if (!GlobalCC.isEmptyStr(errMessage)) {
                GlobalCC.INFORMATIONREPORTING(errMessage);
                return null;
            }
            GlobalCC.hidePopup("fms:bankRegionsPopUp");
            ADFUtils.findIterator("findBankRegionsIterator").executeQuery();
            GlobalCC.refreshUI(bankRegionsTab);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String deleteOrgRegion() {
        RowKeySet rowKeySet = regionsTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        GlobalCC.showPopup("fms:confirmDeleteRegion");
        return null;
    }

    public String deleteBankRegion() {

        RowKeySet rowKeySet = bankRegionsTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        GlobalCC.showPopup("fms:confirmDeleteBankRegion");
        return null;
    }


    public String cancelOrgRegion() {

        regCode.setValue(null);
        regOrgCode.setValue(null);
        regShortDesc.setValue(null);
        regName.setValue(null);
        regWef.setValue(null);
        regWet.setValue(null);
        regAgnCode.setValue(null);
        regPostLevel.setValue(null);
        regMngrAllowed.setValue(null);
        regOverideCommEarned.setValue(null);
        regManager.setValue(null);

        return null;
    }

    public String clearBankRegion() {

        bnkrCode.setValue(null);
        bnkrOrgCode.setValue(null);
        bnkrShtDesc.setValue(null);
        bnkrName.setValue(null);
        bnkrWef.setValue(null);
        bnkrWet.setValue(null);
        bnkrRegCode.setValue(null);
        bnkrAgnCode.setValue(null);
        bnkrManager.setValue(null);

        AdfFacesContext context = AdfFacesContext.getCurrentInstance();

        context.addPartialTarget(bnkrCode);
        context.addPartialTarget(bnkrOrgCode);
        context.addPartialTarget(bnkrShtDesc);
        context.addPartialTarget(bnkrName);
        context.addPartialTarget(bnkrWef);
        context.addPartialTarget(bnkrWet);
        context.addPartialTarget(bnkrRegCode);
        context.addPartialTarget(bnkrAgnCode);
        context.addPartialTarget(bnkrManager);
        context.addPartialTarget(bankRegionsTab);

        return null;
    }


    private BigDecimal getAccountCode() {
        String query =
            "SELECT act_code FROM tq_crm.tqc_account_types WHERE act_type_id = 'AE'";

        DBConnector dbHandler = new DBConnector();
        OracleConnection conn = null;
        BigDecimal actCode = null;
        try {
            conn = dbHandler.getDatabaseConnection();

            Statement cstmt = conn.createStatement();
            ResultSet rs = cstmt.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    actCode = rs.getBigDecimal(1);
                    break;
                }
            }

            if (rs != null)
                rs.close();
            if (cstmt != null)
                cstmt.close();
            if (conn != null)
                conn.close();

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;
        }

        return actCode;
    }

    public String newBranch() {
        if (session.getAttribute("REGCode") == null) {
            GlobalCC.errorValueNotEntered("Error: Select Region");
            return null;
        }

        final BigDecimal actCode = getAccountCode();

        if (actCode == null) {
            GlobalCC.INFORMATIONREPORTING("Please add Agency Development Executive to list of account types.");
            return null;
        }

        session.setAttribute("accountTypeCode", actCode);

        btnSaveBranch.setText("Save");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:branchesPopUp" + "').show(hints);");


        brnCode.setValue(null);
        brnShortDesc.setValue(null);
        brnRegCode.setValue(session.getAttribute("REGCode"));
        brnName.setValue(null);
        brnPhyAddrs.setValue(null);
        brnEmail.setValue(null);
        brnPostAddrs.setValue(null);
        brnTwnCode.setValue(null);
        brnCouCode.setValue(null);
        brnContact.setValue(null);
        brnManager.setValue(null);
        brnTel.setValue(null);
        brnFax.setValue(null);
        brnGenPolClm.setValue(null);

        brnAgnCode.setValue(null);
        brnPostLevel.setValue(null);
        txtManagerAllowedBranch.setValue(null);
        txtOverideCommBranch.setValue(null);
        txtBrnAgnPolPrefix.setValue(null);
        brnManager.setVisible(true);
        btnShowBranchMgrLov.setVisible(true);
        outputLbBrnMgr.setVisible(true);
        brnRef.setValue(null);

        adeAgnCode.setValue(null);
        branchADE.setValue(null);

        GlobalCC.refreshUI(brnManager);
        GlobalCC.refreshUI(btnShowBranchMgrLov);
        GlobalCC.refreshUI(outputLbBrnMgr);

        return null;
    }


    public String viewBranch() {

        RowKeySet rowKeySet = branchesTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        branchesTab.setRowKey(key2);

        JUCtrlValueBinding r = (JUCtrlValueBinding)branchesTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        final BigDecimal actCode = getAccountCode();

        if (actCode == null) {
            GlobalCC.INFORMATIONREPORTING("Please add Agency Development Executive to list of account types.");
            return null;
        }

        session.setAttribute("accountTypeCode", actCode);

        btnSaveBranch.setText("Update");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:branchesPopUp" + "').show(hints);");


        brnCode.setValue(r.getAttribute("brnCode"));
        brnShortDesc.setValue(r.getAttribute("brnShortDesc"));
        brnRegCode.setValue(r.getAttribute("brnRegCode"));
        brnName.setValue(r.getAttribute("brnName"));
        brnPhyAddrs.setValue(r.getAttribute("brnPhyAddrs"));
        brnEmail.setValue(r.getAttribute("brnEmail"));
        brnPostAddrs.setValue(r.getAttribute("brnPostAddrs"));
        brnTwnCode.setValue(r.getAttribute("brnTwnCode"));
        brnCouCode.setValue(r.getAttribute("brnCouCode"));
        brnContact.setValue(r.getAttribute("brnContact"));
        brnManager.setValue(r.getAttribute("brnManager"));
        brnTel.setValue(r.getAttribute("brnTel"));
        brnFax.setValue(r.getAttribute("brnFax"));
        brnGenPolClm.setValue(r.getAttribute("brnGenPolClm"));

        brnAgnCode.setValue(r.getAttribute("brnAgnCode"));
        brnPostLevel.setValue(r.getAttribute("brnPostLevel"));
        brnBnsCode.setValue(r.getAttribute("brnBnsCode"));
        txtBrnBranchMgrSeqNo.setValue(r.getAttribute("brnBrnMngrSeq_no"));
        txtBrnAgnSeqNo.setValue(r.getAttribute("brnAgnSeqNo"));
        txtManagerAllowedBranch.setValue(r.getAttribute("brnMngrAllowed"));
        txtOverideCommBranch.setValue(r.getAttribute("brnOverideCommEarned"));
        txtBrnAgnPolPrefix.setValue(r.getAttribute("brnAgnPolPrefix"));
        policySeq.setValue(r.getAttribute("policySeq"));
        proposalSeq.setValue(r.getAttribute("propSeq"));
        brnRef.setValue(r.getAttribute("ref"));

        adeAgnCode.setValue(r.getAttribute("brnAdeCode"));
        branchADE.setValue(r.getAttribute("brnAdeName"));
        
        brn_Twn.setValue(r.getAttribute("brnTownName"));
        brnPostCode.setValue(r.getAttribute("brnPostCode"));

        brnManager.setVisible(true);
        btnShowBranchMgrLov.setVisible(true);
        outputLbBrnMgr.setVisible(true);
        GlobalCC.refreshUI(brnManager);
        GlobalCC.refreshUI(btnShowBranchMgrLov);
        GlobalCC.refreshUI(outputLbBrnMgr);

        return null;
    }

    public String selectBranchName() {

        RowKeySet rowKeySet = branchNamesLovTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        branchNamesLovTab.setRowKey(key2);

        JUCtrlValueBinding r =
            (JUCtrlValueBinding)branchNamesLovTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        brnShortDesc.setValue(r.getAttribute("bnsShortDesc"));
        brnName.setValue(r.getAttribute("bnsName"));
        brnPhyAddrs.setValue(r.getAttribute("bnsPhyAddrs"));
        brnEmail.setValue(r.getAttribute("bnsEmail"));
        brnPostAddrs.setValue(r.getAttribute("bnsPostAddrs"));
        brnTwnCode.setValue(r.getAttribute("bnsTwnCode"));
        brnCouCode.setValue(r.getAttribute("bnsCouCode"));
        brnContact.setValue(r.getAttribute("bnsContact"));
        // brnManager.setValue(r.getAttribute("bnsManager"));
        brnTel.setValue(r.getAttribute("bnsTel"));
        brnFax.setValue(r.getAttribute("bnsFax"));
        brnBnsCode.setValue(r.getAttribute("bnsCode"));

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:branchNamesLovPopUp" + "').hide(hints);");


        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
        adfFacesContext.addPartialTarget(brnShortDesc);
        adfFacesContext.addPartialTarget(brnName);
        adfFacesContext.addPartialTarget(brnPhyAddrs);
        adfFacesContext.addPartialTarget(brnEmail);
        adfFacesContext.addPartialTarget(brnPostAddrs);
        adfFacesContext.addPartialTarget(brnTwnCode);
        adfFacesContext.addPartialTarget(brnCouCode);
        adfFacesContext.addPartialTarget(brnContact);
        //adfFacesContext.addPartialTarget(brnManager);
        adfFacesContext.addPartialTarget(brnTel);
        adfFacesContext.addPartialTarget(brnFax);
        adfFacesContext.addPartialTarget(brnBnsCode);


        return null;
    }


    public String selectBranchManager() {


        RowKeySet rowKeySet = brnManagersLovTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        brnManagersLovTab.setRowKey(key2);

        JUCtrlValueBinding r =
            (JUCtrlValueBinding)brnManagersLovTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        String orgcode =
            GlobalCC.checkNullValues(session.getAttribute("ORGCode"));
        System.out.println("orgcode" + orgcode);
        if (orgcode != null) {
            BigDecimal code = new BigDecimal(orgcode);

            String val = Integer.toString(1);
            if (orgcode.equalsIgnoreCase(val)) {
                HashMap map = new HashMap();
                String brncode = GlobalCC.checkNullValues(brnCode.getValue());
                map =
generateSeqNo("BM", null, brncode == null ? null : new BigDecimal(brncode),
              null, null, new BigDecimal(r.getAttribute("agnCode").toString()),
              "N");
                if (map != null) {
                    brnShortDesc.setValue(map.get("seq"));
                    brnName.setValue(map.get("name"));
                }

            }
        }

        brnAgnCode.setValue(r.getAttribute("agnCode"));
        brnManager.setValue(r.getAttribute("agnName"));

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:brnManagersLovPopUp" + "').hide(hints);");


        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
        adfFacesContext.addPartialTarget(brnAgnCode);
        adfFacesContext.addPartialTarget(brnManager);


        return null;
    }

    public void actionConfirmSaveBranchesListener(DialogEvent dialogEvent) {
        
        
        
        
        
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            RowKeySet rowKeySet = branchesTab.getSelectedRowKeys();
           

        }

    }
    
    
 

    public String saveBranch() {
           

        String brnCodeVal;
        String brnShortDescVal;
        String brnRegCodeVal;
        String brnNameVal;
        String brnPhyAddrsVal;
        String brnEmailVal;
        String brnPostAddrsVal;
        String brnTwnCodeVal;
        String brnCouCodeVal;
        String brnContactVal;
        String brnManagerVal;
        String brnTelVal;
        String brnFaxVal;
        String brnGenPolClmVal;

        String brnAgnCodeVal;
        String brnPostLevelVal;

        String brnBraMgrseqNo;
        String brnAgnSeqNo;
        String brnBnsCodeVal;
        String policySequence;
        String propSeq;
      

        String adeAgnCodeVal;

        adeAgnCodeVal = GlobalCC.checkNullValues(adeAgnCode.getValue());

        BigDecimal propSeqNo = null;
        BigDecimal policySeqNo = null;
        brnCodeVal = GlobalCC.checkNullValues(brnCode.getValue());
        brnShortDescVal = GlobalCC.checkNullValues(brnShortDesc.getValue());
        brnRegCodeVal = GlobalCC.checkNullValues(brnRegCode.getValue());
        brnNameVal = GlobalCC.checkNullValues(brnName.getValue());
        brnPhyAddrsVal = GlobalCC.checkNullValues(brnPhyAddrs.getValue());
        brnEmailVal = GlobalCC.checkNullValues(brnEmail.getValue());
        brnPostAddrsVal = GlobalCC.checkNullValues(brnPostAddrs.getValue());
        brnTwnCodeVal = GlobalCC.checkNullValues(brnTwnCode.getValue());
        brnCouCodeVal = GlobalCC.checkNullValues(brnCouCode.getValue());
        brnContactVal = GlobalCC.checkNullValues(brnContact.getValue());
        brnManagerVal = GlobalCC.checkNullValues(brnManager.getValue());
        brnTelVal = GlobalCC.checkNullValues(brnTel.getValue());
        brnFaxVal = GlobalCC.checkNullValues(brnFax.getValue());
        brnGenPolClmVal = GlobalCC.checkNullValues(brnGenPolClm.getValue());

        brnAgnCodeVal = GlobalCC.checkNullValues(brnAgnCode.getValue());
        brnPostLevelVal = GlobalCC.checkNullValues(brnPostLevel.getValue());

        brnBraMgrseqNo =
                GlobalCC.checkNullValues(txtBrnBranchMgrSeqNo.getValue());
        brnAgnSeqNo = GlobalCC.checkNullValues(txtBrnAgnSeqNo.getValue());
        brnBnsCodeVal = GlobalCC.checkNullValues(brnBnsCode.getValue());

        String managerAllowed =
            GlobalCC.checkNullValues(txtManagerAllowedBranch.getValue());
        String overideComm =
            GlobalCC.checkNullValues(txtOverideCommBranch.getValue());
        String polPrefix =
            GlobalCC.checkNullValues(txtBrnAgnPolPrefix.getValue());
        
        String townName =
            GlobalCC.checkNullValues(brn_Twn.getValue());
        
        String postalCod =
            GlobalCC.checkNullValues(brnPostCode.getValue());
        
        policySequence = GlobalCC.checkNullValues(policySeq.getValue());
        propSeq = GlobalCC.checkNullValues(proposalSeq.getValue());
        String txtbrnRef = GlobalCC.checkNullValues(brnRef.getValue());
        if (policySeq.getValue() != null) {
            policySeqNo = new BigDecimal(policySeq.getValue().toString());
        } else {
            policySequence = null;
        }
        
      
        
        if (proposalSeq.getValue() != null) {
            propSeqNo = new BigDecimal(proposalSeq.getValue().toString());
        } else {
            propSeq = null;
        }
        
        if (brnRegCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select Region");
            return null;
        }

        if (brnShortDescVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Short Description");
            return null;
        }
        if (brnNameVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Name");
            return null;
        }
        
        
        if (postalCod == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Postal Code");
            return null;
        }


        if (brnBraMgrseqNo != null) {
            try {
                int x = Integer.parseInt(brnBraMgrseqNo);
            } catch (Exception e) {
                GlobalCC.INFORMATIONREPORTING("Ensure Agency Mgr seqNo is Numeric");
                return null;
            }
        }
        if (brnAgnSeqNo != null) {
            try {
                int x = Integer.parseInt(brnAgnSeqNo);
            } catch (Exception e) {
                GlobalCC.INFORMATIONREPORTING("Ensure Agency  SeqNo is Numeric");
                return null;
            }
        }
        if (txtbrnRef == null && renderer.isBRANCHES_REF_REQUIRED()) {
            GlobalCC.errorValueNotEntered("Branches " +
                                          renderer.getBRANCHES_REF_LABEL() +
                                          " is required!");
            return null;
        }
        
        
        if (townName == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Town");
            return null;
        }
        
        
        
        if (brnEmailVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Email");
            return null;
        }
        
        if (overideComm == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Overide Commission Earned");
            return null;
        }
        if (brnTelVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Telephone");
            return null;
        }
        
        if(brnManagerVal == null){
              tmanager.setValue("Manager");
        }else{tmanager.setValue(null);}
        
        if(managerAllowed == null){
              tmanallowed.setValue("Manager Allowed");
            }else{tmanallowed.setValue(null);}
        
        if(branchADE.getValue()==null ){
              tade.setValue("Agency Development Executive");
            }else{tade.setValue(null);}
        
        if( (managerAllowed == null || brnManagerVal == null ||branchADE.getValue()==null ) && ( btnSaveBranch.getText().equalsIgnoreCase("save"))){                       
            confirmToSaveBranches(); 
           
            return null;
        }
        
    
    
        


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        try {
            String query = null;
            //modify the query for treaty groups procedure
            query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_BRANCHES(" + "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?); end;";


            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables

            String option = null;
            if (btnSaveBranch.getText().equalsIgnoreCase("save")) {
                option = "A";
            } else if (btnSaveBranch.getText().equalsIgnoreCase("Update")) {
                option = "E";
            }

            callStmt.setString(1, option);
            callStmt.setString(2, brnCodeVal);
            callStmt.setString(3, brnShortDescVal);
            callStmt.setString(4, brnRegCodeVal);
            callStmt.setString(5, brnNameVal);
            callStmt.setString(6, brnPhyAddrsVal);
            callStmt.setString(7, brnEmailVal);
            callStmt.setString(8, brnPostAddrsVal);
            callStmt.setString(9, brnTwnCodeVal);
            callStmt.setString(10, brnCouCodeVal);
            callStmt.setString(11, brnContactVal);
            callStmt.setString(12, brnManagerVal);
            callStmt.setString(13, brnTelVal);
            callStmt.setString(14, brnFaxVal);
            callStmt.setString(15, brnAgnCodeVal);
            callStmt.setString(16, brnPostLevelVal);
            callStmt.registerOutParameter(17, OracleTypes.VARCHAR);
            // callStmt.setString(15, brnGenPolClmVal);
            //
            callStmt.setString(18, brnBraMgrseqNo);
            callStmt.setString(19, brnAgnSeqNo);
            callStmt.setBigDecimal(20,
                                   brnBnsCodeVal == null ? null : new BigDecimal(brnBnsCodeVal));
            callStmt.setString(21, managerAllowed);
            callStmt.setString(22, overideComm);
            callStmt.setString(23, polPrefix);
            callStmt.setBigDecimal(24, propSeqNo);
            callStmt.setBigDecimal(25, policySeqNo);
            callStmt.setString(26, txtbrnRef);
            callStmt.setObject(27, adeAgnCodeVal);
            
            callStmt.setObject(28, townName);
            callStmt.setObject(29, postalCod);
            
            callStmt.execute();
            String errMessage = null;
            errMessage = callStmt.getString(17);

            callStmt.close();
            conn.commit();
            conn.close();

            if (errMessage != null) {
                GlobalCC.errorValueNotEntered(errMessage);
                return null;
            }

            brnCode.setValue(null);
            brnShortDesc.setValue(null);
            brnRegCode.setValue(null);
            brnName.setValue(null);
            brnPhyAddrs.setValue(null);
            brnEmail.setValue(null);
            brnPostAddrs.setValue(null);
            brnTwnCode.setValue(null);
            brnCouCode.setValue(null);
            brnContact.setValue(null);
            brnManager.setValue(null);
            brnTel.setValue(null);
            brnFax.setValue(null);
            brnGenPolClm.setValue(null);

            brnAgnCode.setValue(null);
            brnPostLevel.setValue(null);
            txtBrnBranchMgrSeqNo.setValue(null);
            txtBrnAgnSeqNo.setValue(null);
            policySeq.setValue(null);
            proposalSeq.setValue(null);

            adeAgnCode.setValue(null);
            branchADE.setValue(null);
            brn_Twn.setValue(null);
            brnPostCode.setValue(null);

            ADFUtils.findIterator("findBranchesIterator").executeQuery();
            GlobalCC.refreshUI(branchesTab);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:branchesPopUp" + "').hide(hints);");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        return null;
        
    }
    
    
    public void actionConfirmSaveBranchListener(DialogEvent dialogEvent){
        
       
        
            if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
                    
                    
                   } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {                
                           
                                                   
                           
                           String brnCodeVal;
                           String brnShortDescVal;
                           String brnRegCodeVal;
                           String brnNameVal;
                           String brnPhyAddrsVal;
                           String brnEmailVal;
                           String brnPostAddrsVal;
                           String brnTwnCodeVal;
                           String brnCouCodeVal;
                           String brnContactVal;
                           String brnManagerVal;
                           String brnTelVal;
                           String brnFaxVal;
                           String brnGenPolClmVal;

                           String brnAgnCodeVal;
                           String brnPostLevelVal;

                           String brnBraMgrseqNo;
                           String brnAgnSeqNo;
                           String brnBnsCodeVal;
                           String policySequence;
                           String propSeq;
                           

                           String adeAgnCodeVal;

                           adeAgnCodeVal = GlobalCC.checkNullValues(adeAgnCode.getValue());

                           BigDecimal propSeqNo = null;
                           BigDecimal policySeqNo = null;
                           brnCodeVal = GlobalCC.checkNullValues(brnCode.getValue());
                           brnShortDescVal = GlobalCC.checkNullValues(brnShortDesc.getValue());
                           brnRegCodeVal = GlobalCC.checkNullValues(brnRegCode.getValue());
                           brnNameVal = GlobalCC.checkNullValues(brnName.getValue());
                           brnPhyAddrsVal = GlobalCC.checkNullValues(brnPhyAddrs.getValue());
                           brnEmailVal = GlobalCC.checkNullValues(brnEmail.getValue());
                           brnPostAddrsVal = GlobalCC.checkNullValues(brnPostAddrs.getValue());
                           brnTwnCodeVal = GlobalCC.checkNullValues(brnTwnCode.getValue());
                           brnCouCodeVal = GlobalCC.checkNullValues(brnCouCode.getValue());
                           brnContactVal = GlobalCC.checkNullValues(brnContact.getValue());
                           brnManagerVal = GlobalCC.checkNullValues(brnManager.getValue());
                           brnTelVal = GlobalCC.checkNullValues(brnTel.getValue());
                           brnFaxVal = GlobalCC.checkNullValues(brnFax.getValue());
                           brnGenPolClmVal = GlobalCC.checkNullValues(brnGenPolClm.getValue());

                           brnAgnCodeVal = GlobalCC.checkNullValues(brnAgnCode.getValue());
                           brnPostLevelVal = GlobalCC.checkNullValues(brnPostLevel.getValue());

                           brnBraMgrseqNo =
                                   GlobalCC.checkNullValues(txtBrnBranchMgrSeqNo.getValue());
                           brnAgnSeqNo = GlobalCC.checkNullValues(txtBrnAgnSeqNo.getValue());
                           brnBnsCodeVal = GlobalCC.checkNullValues(brnBnsCode.getValue());

                           String managerAllowed =
                               GlobalCC.checkNullValues(txtManagerAllowedBranch.getValue());
                           String overideComm =
                               GlobalCC.checkNullValues(txtOverideCommBranch.getValue());
                           String polPrefix =
                               GlobalCC.checkNullValues(txtBrnAgnPolPrefix.getValue());
                           
                           String townName =
                               GlobalCC.checkNullValues(brn_Twn.getValue());
                           
                           String postalCod =
                               GlobalCC.checkNullValues(brnPostCode.getValue());
                           
                           policySequence = GlobalCC.checkNullValues(policySeq.getValue());
                           propSeq = GlobalCC.checkNullValues(proposalSeq.getValue());
                           String txtbrnRef = GlobalCC.checkNullValues(brnRef.getValue());
                           if (policySeq.getValue() != null) {
                               policySeqNo = new BigDecimal(policySeq.getValue().toString());
                           } else {
                               policySequence = null;
                           }
                           
                           
                           
                           if (proposalSeq.getValue() != null) {
                               propSeqNo = new BigDecimal(proposalSeq.getValue().toString());
                           } else {
                               propSeq = null;
                           }
                           

                           DBConnector dbConnector = new DBConnector();
                           OracleConnection conn;
                           conn = dbConnector.getDatabaseConnection();
                           try {
                               String query = null;
                               //modify the query for treaty groups procedure
                               query =
                                       "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_BRANCHES(" + "?,?,?,?,?,?,?,?,?,?," +
                                       "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?); end;";


                               OracleCallableStatement callStmt = null;
                               callStmt = (OracleCallableStatement)conn.prepareCall(query);
                               //bind the variables

                               String option = null;
                               if (btnSaveBranch.getText().equalsIgnoreCase("save")) {
                                   option = "A";
                               } else if (btnSaveBranch.getText().equalsIgnoreCase("Update")) {
                                   option = "E";
                               }

                               callStmt.setString(1, option);
                               callStmt.setString(2, brnCodeVal);
                               callStmt.setString(3, brnShortDescVal);
                               callStmt.setString(4, brnRegCodeVal);
                               callStmt.setString(5, brnNameVal);
                               callStmt.setString(6, brnPhyAddrsVal);
                               callStmt.setString(7, brnEmailVal);
                               callStmt.setString(8, brnPostAddrsVal);
                               callStmt.setString(9, brnTwnCodeVal);
                               callStmt.setString(10, brnCouCodeVal);
                               callStmt.setString(11, brnContactVal);
                               callStmt.setString(12, brnManagerVal);
                               callStmt.setString(13, brnTelVal);
                               callStmt.setString(14, brnFaxVal);
                               callStmt.setString(15, brnAgnCodeVal);
                               callStmt.setString(16, brnPostLevelVal);
                               callStmt.registerOutParameter(17, OracleTypes.VARCHAR);
                               // callStmt.setString(15, brnGenPolClmVal);
                               //
                               callStmt.setString(18, brnBraMgrseqNo);
                               callStmt.setString(19, brnAgnSeqNo);
                               callStmt.setBigDecimal(20,
                                                      brnBnsCodeVal == null ? null : new BigDecimal(brnBnsCodeVal));
                               callStmt.setString(21, managerAllowed);
                               callStmt.setString(22, overideComm);
                               callStmt.setString(23, polPrefix);
                               callStmt.setBigDecimal(24, propSeqNo);
                               callStmt.setBigDecimal(25, policySeqNo);
                               callStmt.setString(26, txtbrnRef);
                               callStmt.setObject(27, adeAgnCodeVal);
                               
                               callStmt.setObject(28, townName);
                               callStmt.setObject(29, postalCod);
                               
                               callStmt.execute();
                               String errMessage = null;
                               errMessage = callStmt.getString(17);

                               callStmt.close();
                               conn.commit();
                               conn.close();

                               if (errMessage != null) {
                                   GlobalCC.errorValueNotEntered(errMessage);
                                   
                               }

                               brnCode.setValue(null);
                               brnShortDesc.setValue(null);
                               brnRegCode.setValue(null);
                               brnName.setValue(null);
                               brnPhyAddrs.setValue(null);
                               brnEmail.setValue(null);
                               brnPostAddrs.setValue(null);
                               brnTwnCode.setValue(null);
                               brnCouCode.setValue(null);
                               brnContact.setValue(null);
                               brnManager.setValue(null);
                               brnTel.setValue(null);
                               brnFax.setValue(null);
                               brnGenPolClm.setValue(null);

                               brnAgnCode.setValue(null);
                               brnPostLevel.setValue(null);
                               txtBrnBranchMgrSeqNo.setValue(null);
                               txtBrnAgnSeqNo.setValue(null);
                               policySeq.setValue(null);
                               proposalSeq.setValue(null);

                               adeAgnCode.setValue(null);
                               branchADE.setValue(null);
                               brn_Twn.setValue(null);
                               brnPostCode.setValue(null);

                               ADFUtils.findIterator("findBranchesIterator").executeQuery();
                               GlobalCC.refreshUI(branchesTab);
                               ExtendedRenderKitService erkService =
                                   Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                                      ExtendedRenderKitService.class);
                               erkService.addScript(FacesContext.getCurrentInstance(),
                                                    "var hints = {autodismissNever:false}; " +
                                                    "AdfPage.PAGE.findComponent('" +
                                                    "fms:branchesPopUp" + "').hide(hints);");
                           } catch (Exception e) {
                               e.printStackTrace();
                               GlobalCC.EXCEPTIONREPORTING(conn, e);

                           }
                           

                       }
        }
    
    public String confirmToSaveBranches() {
          
                
         ExtendedRenderKitService erkService =
              Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                 ExtendedRenderKitService.class);
          erkService.addScript(FacesContext.getCurrentInstance(),
                               "var hints = {autodismissNever:false}; " +
                               "AdfPage.PAGE.findComponent('" +
                               "fms:confirmSaveBranches" + "').show(hints);");

          return null;
   
       
      }

    public String deleteBranch() {
        RowKeySet rowKeySet = branchesTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:confirmDeleteBranches" + "').show(hints);");

        return null;
    }
    
    //added 
 


    public String cancelBranch() {

        brnCode.setValue(null);
        brnShortDesc.setValue(null);
        brnRegCode.setValue(null);
        brnName.setValue(null);
        brnPhyAddrs.setValue(null);
        brnEmail.setValue(null);
        brnPostAddrs.setValue(null);
        brnTwnCode.setValue(null);
        brnCouCode.setValue(null);
        brnContact.setValue(null);
        brnManager.setValue(null);
        brnTel.setValue(null);
        brnFax.setValue(null);
        brnGenPolClm.setValue(null);

        brnAgnCode.setValue(null);
        brnPostLevel.setValue(null);

        return null;
    }

    public String newBranchAgency() {

        if (session.getAttribute("BRNCode") == null) {
            GlobalCC.errorValueNotEntered("Error: Select Branch");
            return null;
        }
        txtBraCode.setValue(null);
        braBrnCode.setValue(session.getAttribute("BRNCode"));
        braShortDesc.setValue(null);
        braName.setValue(null);
        braStatus.setValue(null);
        braManager.setValue(null);
        braAgnCode.setValue(null);
        braPostLevel.setValue(null);
        txtManagerAllowedAgency.setValue(null);
        txtOverideCommAgency.setValue(null);
        polSeqAgnt.setValue(null);
        propseqAgn.setValue(null);
        txtBraBranchMgrSeqNo.setValue(null); //
        txtBraAgnSeqNo.setValue(null); //
        txtPrefix.setValue(null);
        txtcombuss.setValue(null);
        session.removeAttribute("manager");

        GlobalCC.refreshUI(agenciesPopUp);
        GlobalCC.showPop("fms:agenciesPopUp");
        return null;
    }

    public String viewBranchAgency() {

        RowKeySet rowKeySet = agenciesTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        agenciesTab.setRowKey(key2);

        JUCtrlValueBinding r = (JUCtrlValueBinding)agenciesTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        //txtBraCode.setValue("2044534");
        System.out.println("r.getAttribute(\"shtDescPrefix\")" +
                           r.getAttribute("shtDescPrefix"));
        System.out.println("r.getAttribute(\"agnTxtCombuss\")" +
                           r.getAttribute("agnTxtCombuss"));

        txtBraCode.setValue(r.getAttribute("braCode"));
        session.setAttribute("braCode", r.getAttribute("braCode"));
        braBrnCode.setValue(r.getAttribute("braBrnCode"));
        braShortDesc.setValue(r.getAttribute("braShortDesc"));
        session.setAttribute("manager", r.getAttribute("braAgnCode"));
        braName.setValue(r.getAttribute("braName"));
        braStatus.setValue(r.getAttribute("braStatus"));
        braManager.setValue(r.getAttribute("braManager"));
        braAgnCode.setValue(r.getAttribute("braAgnCode"));
        braPostLevel.setValue(r.getAttribute("braPostLevel"));
        txtBraBranchMgrSeqNo.setValue(r.getAttribute("braBrnMngrSeq_no")); //
        txtBraAgnSeqNo.setValue(r.getAttribute("braAgnSeqNo")); //
        txtPrefix.setValue(r.getAttribute("shtDescPrefix"));
        txtManagerAllowedAgency.setValue(r.getAttribute("braMngrAllowed"));
        txtOverideCommAgency.setValue(r.getAttribute("braOverideCommEarned"));
        polSeqAgnt.setValue(r.getAttribute("agencypolicySeq")); //
        propseqAgn.setValue(r.getAttribute("agencypropSeq")); //
        txtcombuss.setValue(r.getAttribute("agnTxtCombuss"));
        braManager.setVisible(true);
        btnShowBranchMgrLov.setVisible(true);
        outlBraMgr.setVisible(true);

        GlobalCC.refreshUI(brnManager);
        GlobalCC.refreshUI(btnShowBraMgrlov);
        GlobalCC.refreshUI(outlBraMgr);
        GlobalCC.refreshUI(btnShowBraMgrlov);
        GlobalCC.refreshUI(agenciesPopUp);
        GlobalCC.showPop("fms:agenciesPopUp");

        return null;
    }

    public String selectAgencyManager() {

        RowKeySet rowKeySet = braManagersLovTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        braManagersLovTab.setRowKey(key2);

        JUCtrlValueBinding r =
            (JUCtrlValueBinding)braManagersLovTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        /* String orgcode=GlobalCC.checkNullValues( session.getAttribute("ORGCode"));
        System.out.println("orgcode"+orgcode);
        if(orgcode!=null  ){
            BigDecimal code=new BigDecimal( orgcode) ;

            String val=Integer.toString(1);
            if(orgcode.equalsIgnoreCase(val) ){
              HashMap map=new HashMap();
         String bracode=GlobalCC.checkNullValues(braCode.getValue());
         map= generateSeqNo("BA",null ,null ,bracode==null ? null : new BigDecimal(bracode),null, new BigDecimal(r.getAttribute("agnCode").toString()));

            }
        }*/

        braAgnCode.setValue(r.getAttribute("agnCode"));
        braManager.setValue(r.getAttribute("agnName"));

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:braManagersLovPopUp" + "').hide(hints);");


        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
        adfFacesContext.addPartialTarget(braAgnCode);
        adfFacesContext.addPartialTarget(braManager);


        return null;
    }

    public String saveBranchAgency() {

        String braCodeVal;
        String braBrnCodeVal;
        String braShortDescVal;
        String braNameVal;
        String braStatusVal;
        String braManagerVal;
        String braAgnCodeVal;
        String braPostLevelVal;
        String braBraMgrseqNo;
        String braAgnSeqNo;
        String braShtDescPref;
        String brnComBuss;
        BigDecimal policySeqAgnt;
        BigDecimal proposalSeqAgnt;
        braCodeVal = GlobalCC.checkNullValues(txtBraCode.getValue());
        braBrnCodeVal =
                GlobalCC.checkNullValues(session.getAttribute("BRNCode")); //GlobalCC.checkNullValues(braBrnCode.getValue());
        braShortDescVal = GlobalCC.checkNullValues(braShortDesc.getValue());
        braNameVal = GlobalCC.checkNullValues(braName.getValue());
        braStatusVal = GlobalCC.checkNullValues(braStatus.getValue());
        braManagerVal = GlobalCC.checkNullValues(braManager.getValue());
        braAgnCodeVal = GlobalCC.checkNullValues(braAgnCode.getValue());
        braPostLevelVal = GlobalCC.checkNullValues(braPostLevel.getValue());
        braBraMgrseqNo =
                GlobalCC.checkNullValues(txtBraBranchMgrSeqNo.getValue());
        braAgnSeqNo = GlobalCC.checkNullValues(txtBraAgnSeqNo.getValue());
        String managerAllowed =
            GlobalCC.checkNullValues(txtManagerAllowedAgency.getValue());
        String overideComm =
            GlobalCC.checkNullValues(txtOverideCommAgency.getValue());
        braShtDescPref = GlobalCC.checkNullValues(txtPrefix.getValue());
        brnComBuss = GlobalCC.checkNullValues(txtcombuss.getValue());

        if (polSeqAgnt.getValue() != null) {
            try {
                policySeqAgnt =
                        new BigDecimal(polSeqAgnt.getValue().toString());
            } catch (Exception e) {
                GlobalCC.INFORMATIONREPORTING("Please enter a Number");
                return null;
            }
        } else {
            policySeqAgnt = null;
        }
        if (propseqAgn.getValue() != null) {
            try {
                proposalSeqAgnt =
                        new BigDecimal(propseqAgn.getValue().toString());
            } catch (Exception e) {
                GlobalCC.INFORMATIONREPORTING("Please enter a Number");
                return null;
            }
        } else {
            proposalSeqAgnt = null;
        }
        if (braBrnCodeVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select Branch");
            return null;
        }
        if (braAgnCodeVal == null &&
            (txtManagerAllowedAgency.getValue() == "Y" ||
             txtManagerAllowedAgency.getValue() == null)) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Select Manager");
            return null;
        }

        if (braNameVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Name");
            return null;
        }
        if (braBraMgrseqNo != null) {
            try {
                int x = Integer.parseInt(braBraMgrseqNo);
            } catch (Exception e) {
                GlobalCC.INFORMATIONREPORTING("Ensure Unit Mgr seqNo is Numeric");
                return null;
            }
        }
        if (braAgnSeqNo != null) {
            try {
                int x = Integer.parseInt(braAgnSeqNo);
            } catch (Exception e) {
                GlobalCC.INFORMATIONREPORTING("Ensure Unit SeqNo is Numeric");
                return null;
            }
        }


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        try {
            String query = null;
            //modify the query for treaty groups procedure
            query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_BRANCH_AGENCIES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";


            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables
            System.out.println("braCodeVal braCodeVal" + braCodeVal);
            String option = null;
            if (braCodeVal == null) {
                option = "A";
                callStmt.setString(1, option);

                callStmt.registerOutParameter(2, OracleTypes.NUMBER);
            } else {
                option = "E";
                callStmt.setString(1, option);
                callStmt.setString(2, braCodeVal);
            }


            callStmt.setString(3, braBrnCodeVal);
            if (session.getAttribute("manager") == null) {
                callStmt.setString(4, "XXX");
            } else {
                callStmt.setString(4, braShortDescVal);
            }
            callStmt.setString(5, braNameVal);
            callStmt.setString(6, braStatusVal);
            callStmt.setString(7, braManagerVal);
            callStmt.setString(8, braAgnCodeVal);
            callStmt.setString(9, braPostLevelVal);
            callStmt.setString(10, braBraMgrseqNo);
            callStmt.setString(11, braAgnSeqNo);
            callStmt.setString(12, managerAllowed);
            callStmt.setString(13, overideComm);
            callStmt.setString(14, braShtDescPref);
            callStmt.setString(15, brnComBuss);
            callStmt.setBigDecimal(16, policySeqAgnt);
            callStmt.setBigDecimal(17, proposalSeqAgnt);
            callStmt.registerOutParameter(18, OracleTypes.VARCHAR);
            callStmt.execute();
            String errMessage = null;
            errMessage = callStmt.getString(18);
            if (option.equalsIgnoreCase("A")) {
                braCodeVal = callStmt.getString(2);
            }
            System.out.println(option);
            System.out.println(braCodeVal);
            System.out.println(braAgnCodeVal);
            String orgcode =
                GlobalCC.checkNullValues(session.getAttribute("ORGCode"));
            System.out.println("orgcode" + orgcode);
            if (orgcode != null) {
                BigDecimal code = new BigDecimal(orgcode);

                String val = Integer.toString(1);
                if (orgcode.equalsIgnoreCase(val)) {
                    HashMap map = new HashMap();
                    String bracode = braCodeVal;
                    if (braAgnCodeVal != null) {
                        if (session.getAttribute("manager") == null ||
                            option.equalsIgnoreCase("E")) {
                            generateSeqNo("BA", null, null,
                                          new BigDecimal(bracode), null,
                                          new BigDecimal(braAgnCodeVal),
                                          braPostLevelVal);
                        } else if (!session.getAttribute("manager").toString().equalsIgnoreCase(braAgnCodeVal)) {
                            generateSeqNo("BA", null, null,
                                          new BigDecimal(bracode), null,
                                          new BigDecimal(braAgnCodeVal),
                                          braPostLevelVal);
                        }
                    }
                    //                    }else{
                    //                          generateSeqNo("BA", null, null,
                    //                                        new BigDecimal(bracode), null,
                    //                                        null);
                    //                        }
                }
            }
            callStmt.close();
            conn.commit();
            conn.close();

            txtBraCode.setValue(null);
            braBrnCode.setValue(null);
            braShortDesc.setValue(null);
            braName.setValue(null);
            braStatus.setValue(null);
            braManager.setValue(null);
            braAgnCode.setValue(null);
            braPostLevel.setValue(null);

            if (errMessage != null) {
                GlobalCC.errorValueNotEntered(errMessage);
                return null;
            }
            GlobalCC.hidePopup("fms:agenciesPopUp");
            ADFUtils.findIterator("findBranchAgenciesIterator").executeQuery();
            GlobalCC.refreshUI(agenciesTab);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }


    public String deleteBranchAgency() {

        RowKeySet rowKeySet = agenciesTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        GlobalCC.showPopup("fms:confirmDeleteBranchAgencies");
        return null;
    }

    public String authorizeOrgItemsTransfer() {

        OracleCallableStatement callStmt = null;
        OracleConnection conn = null;

        try {

            Authorization auth = new Authorization();
            String AccessGranted =
                auth.checkUserRights("OS", "OSO", "OSOTA", null, null);

            if (!AccessGranted.equalsIgnoreCase("Y")) {
                GlobalCC.accessDenied();
                return null;
            }
            if (GlobalCC.isEmptyBD((BigDecimal)session.getAttribute("tt_code"))) {
                GlobalCC.errorValueNotEntered("Error: Transfer not initialized!");
                return null;
            }

            if (GlobalCC.isEmptyBD(session.getAttribute("tt_trans_from_code"))) {
                GlobalCC.errorValueNotEntered("Error:  Source Not Provided!");
                return null;
            }
            BigDecimal fromCode =
                GlobalCC.checkBDNullValues(session.getAttribute("tt_trans_from_code"));

            if (GlobalCC.isEmptyBD(session.getAttribute("tt_trans_to_code"))) {
                GlobalCC.errorValueNotEntered("Error: Destination Not Provided!");
                return null;
            }
            BigDecimal toCode =
                GlobalCC.checkBDNullValues(session.getAttribute("tt_trans_to_code"));

            if (toCode.compareTo(fromCode) == 0) {
                GlobalCC.errorValueNotEntered("Error: Destination should not be same as Source!");
                return null;
            }
            String query =
                "begin tqc_setups_pkg.authorize_transfer(?,?); end;";

            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.setBigDecimal(1,
                                   (BigDecimal)session.getAttribute("tt_code"));
            callStmt.setString(2, (String)session.getAttribute("Username"));
            callStmt.execute();
            conn.commit();

            ADFUtils.findIterator("fetchTranferableItemsIterator").executeQuery();
            ADFUtils.findIterator("fetchTranferedItemsIterator").executeQuery();
            ADFUtils.findIterator("findRegionsIterator").executeQuery();
            ADFUtils.findIterator("findBranchesIterator").executeQuery();
            ADFUtils.findIterator("findBranchAgenciesIterator").executeQuery();
            ADFUtils.findIterator("findBranchUnitsIterator").executeQuery();
            ADFUtils.findIterator("fetchUnitAgentsIterator").executeQuery();

            GlobalCC.refreshUI(tblTransferableItems);
            GlobalCC.refreshUI(tblTransferedItems);
            GlobalCC.refreshUI(regionsTab);
            GlobalCC.refreshUI(bankRegionsTab);
            GlobalCC.refreshUI(branchesTab);
            GlobalCC.refreshUI(agenciesTab);
            GlobalCC.refreshUI(unitsTab);
            GlobalCC.refreshUI(agentsTab);

            GlobalCC.INFORMATIONREPORTING("Authorization Successfull.");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, callStmt, null);
        }

        return null;
    }

    public String transferOrgRegion() {

        if (GlobalCC.isEmptyBD(session.getAttribute("orgCode"))) {
            GlobalCC.errorValueNotEntered("Error: No Org Selected");
            return null;
        }

        session.setAttribute("tt_trans_type", "ROT");
        session.setAttribute("tt_trans_from_code",
                             GlobalCC.checkBDNullValues(session.getAttribute("ORGCode")));
        session.setAttribute("tt_trans_from_name",
                             GlobalCC.checkNullValues(session.getAttribute("ORGName")));
        session.setAttribute("tt_trans_to_code", null);
        session.setAttribute("tt_trans_to_name", null);

        fetchTranferDetails();

        return null;
    }

    public String transferRegionBranch() {

        if (GlobalCC.isEmptyBD(session.getAttribute("REGCode"))) {
            GlobalCC.errorValueNotEntered("Error: No Region Selected");
            return null;
        }
        session.setAttribute("tt_trans_type", "BRT");
        session.setAttribute("tt_trans_from_code",
                             GlobalCC.checkBDNullValues(session.getAttribute("REGCode")));
        session.setAttribute("tt_trans_from_name",
                             GlobalCC.checkNullValues(session.getAttribute("REGName")));
        session.setAttribute("tt_trans_to_code", null);
        session.setAttribute("tt_trans_to_name", null);

        fetchTranferDetails();

        return null;
    }

    public String transferBranchAgency() {

        if (GlobalCC.isEmptyBD(session.getAttribute("BRNCode"))) {
            GlobalCC.errorValueNotEntered("Error: No Branch Selected");
            return null;
        }

        session.setAttribute("tt_trans_type", "ABT");
        session.setAttribute("tt_trans_from_code",
                             GlobalCC.checkBDNullValues(session.getAttribute("BRNCode")));
        session.setAttribute("tt_trans_from_name",
                             GlobalCC.checkNullValues(session.getAttribute("BRNName")));
        session.setAttribute("tt_trans_to_code", null);
        session.setAttribute("tt_trans_to_name", null);

        fetchTranferDetails();

        return null;
    }

    public String transferAgencyUnits() {
        
        
//        String val = (String)GlobalCC.getSysParamValue("ORG_AGENCY_TAB_APP");
//                if (session.getAttribute("braCode") == null && "Y".equalsIgnoreCase(val) ) {
//                    GlobalCC.errorValueNotEntered("Error: Select Agency");
//                    return null;
//                }
                

        if (GlobalCC.isEmptyBD(session.getAttribute("braCode"))) {
            GlobalCC.errorValueNotEntered("Error: No Agency Selected");
            return null;
        }
        session.setAttribute("tt_trans_type", "UAT");
        session.setAttribute("tt_trans_from_code",
                             GlobalCC.checkBDNullValues(session.getAttribute("braCode")));
        session.setAttribute("tt_trans_from_name",
                             GlobalCC.checkNullValues(session.getAttribute("braName")));
        session.setAttribute("tt_trans_to_code", null);
        session.setAttribute("tt_trans_to_name", null);

        fetchTranferDetails();

        return null;
    }
    
    
    
    public String transferUnitBranch() {
        
        
//            String val = (String)GlobalCC.getSysParamValue("ORG_AGENCY_TAB_APP");
//                    if (session.getAttribute("braCode") == null && "Y".equalsIgnoreCase(val) ) {
//                        GlobalCC.errorValueNotEntered("Error: Select Agency");
//                        return null;
//                    }
//                

        if (GlobalCC.isEmptyBD(session.getAttribute("BRNCode"))) {
            GlobalCC.errorValueNotEntered("Error: No Branch Selected");
            return null;
        }
        session.setAttribute("tt_trans_type", "UBT");
        session.setAttribute("tt_trans_from_code",
                             GlobalCC.checkBDNullValues(session.getAttribute("BRNCode")));
        session.setAttribute("tt_trans_from_name",
                             GlobalCC.checkNullValues(session.getAttribute("BRNName")));
        session.setAttribute("tt_trans_to_code", null);
        session.setAttribute("tt_trans_to_name", null);

        fetchTranferDetails();

        return null;
    }

    public String transferUnitAgents() {

        if (GlobalCC.isEmptyBD(session.getAttribute("bruCode"))) {
            GlobalCC.errorValueNotEntered("Error: No Unit Selected");
            return null;
        }
        session.setAttribute("tt_trans_type", "AUT");
        session.setAttribute("tt_trans_from_code",
                             GlobalCC.checkBDNullValues(session.getAttribute("bruCode")));
        session.setAttribute("tt_trans_from_name",
                             GlobalCC.checkNullValues(session.getAttribute("bruName")));
        session.setAttribute("tt_trans_to_code", null);
        session.setAttribute("tt_trans_to_name", null);

        fetchTranferDetails();

        return null;
    }
    
    public String transferBranchAgents() {
        
            if (GlobalCC.isEmptyBD(session.getAttribute("BRNCode"))) {
                GlobalCC.errorValueNotEntered("Error: No Branch Selected");
                return null;
            }
            session.setAttribute("tt_trans_type", "ABT");
            session.setAttribute("tt_trans_from_code",
                                 GlobalCC.checkBDNullValues(session.getAttribute("BRNCode")));
            session.setAttribute("tt_trans_from_name",
                                 GlobalCC.checkNullValues(session.getAttribute("BRNName")));
            session.setAttribute("tt_trans_to_code", null);
            session.setAttribute("tt_trans_to_name", null);
            fetchTranferDetails();
         return null;
        }

    public void fetchTranferDetails() {                                     

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        OracleCallableStatement callStmt = null;
        OracleResultSet rs = null;
        String query =
            "begin TQC_SETUPS_CURSOR.get_transfer_details(?,?,?,?,?,?,?); end;";
        List<Organization> branchAgenciesData = new ArrayList<Organization>();
        try {

            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            callStmt.registerOutParameter(1, OracleTypes.CURSOR);
            callStmt.setString(2,
                               (String)session.getAttribute("tt_trans_type"));
            callStmt.setBigDecimal(3,
                                   (BigDecimal)session.getAttribute("tt_trans_from_code"));
            callStmt.setString(4,
                               (String)session.getAttribute("tt_trans_from_name"));
            callStmt.setBigDecimal(5,
                                   (BigDecimal)session.getAttribute("tt_trans_to_code"));
            callStmt.setString(6,
                               (String)session.getAttribute("tt_trans_to_name"));
            callStmt.setString(7, (String)session.getAttribute("Username"));
            callStmt.execute();

            session.setAttribute("tt_code", null);
            rs = (OracleResultSet)callStmt.getObject(1);

            if (rs.next()) {
                session.setAttribute("tt_code", rs.getBigDecimal("tt_code"));
                ;
                session.setAttribute("tt_trans_date",
                                     rs.getString("tt_trans_date"));
                session.setAttribute("tt_trans_to_code",
                                     rs.getBigDecimal("tt_trans_to_code"));
                session.setAttribute("tt_trans_to_name",
                                     rs.getString("tt_trans_to_name"));
                session.setAttribute("tt_trans_from_name",
                                     rs.getString("tt_trans_from_name"));
                session.setAttribute("tt_done_by", rs.getString("tt_done_by"));
                session.setAttribute("tt_done_date",
                                     rs.getString("tt_done_date"));
                session.setAttribute("tt_authorized",
                                     rs.getString("tt_authorized"));
                session.setAttribute("tt_authorized_by",
                                     rs.getString("tt_authorized_by"));
                session.setAttribute("tt_authorized_date",
                                     rs.getString("tt_authorized_date"));

                System.out.println("Transfer Code: " +
                                   session.getAttribute("tt_code"));
                txtTransferFromName.setValue(session.getAttribute("tt_trans_from_name"));
                txtTransferToName.setValue(session.getAttribute("tt_trans_to_name"));

                GlobalCC.refreshUI(txtTransferFromName);
                GlobalCC.refreshUI(txtTransferToName);
            }
            conn.commit();

            ADFUtils.findIterator("fetchTranferableItemsIterator").executeQuery();
            ADFUtils.findIterator("fetchTranferedItemsIterator").executeQuery();
            GlobalCC.refreshUI(tblTransferableItems);
            GlobalCC.refreshUI(tblTransferedItems);
            GlobalCC.refreshUI(swdTransferableItems);
            GlobalCC.refreshUI(swdTransferedItems);
            GlobalCC.refreshUI(dlgTransferOrgItems);
            GlobalCC.showPopup("fms:transferOrgItemsPopUp");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, callStmt, rs);
        }
    }

    public String showTransferToLov() {
        try {
            ADFUtils.findIterator("fetchDestLovItemsIterator").executeQuery();
            GlobalCC.refreshUI(tblTransferToLov);
            GlobalCC.showPopup("fms:transferToLovPop");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }
    
    public String showTransferToLov2() {
        try {
            ADFUtils.findIterator("fetchDestLovItemsIterator").executeQuery();
            GlobalCC.refreshUI(tblTransferToLov);
            GlobalCC.showPopup("fms:transferToLovPop");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
        return null;
    }

    /*public String addTransferableItems() {
        OracleCallableStatement stmt = null;
        OracleConnection conn = null;

        try {
            RowKeySet selection = tblTransferableItems.getSelectedRowKeys();
            if (!selection.iterator().hasNext()) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }
            if (GlobalCC.isEmptyBD((BigDecimal)session.getAttribute("tt_code"))) {
                GlobalCC.errorValueNotEntered("Error: Transfer not initialized!");
                return null;
            }

            if (GlobalCC.isEmptyBD(session.getAttribute("tt_trans_from_code"))) {
                GlobalCC.errorValueNotEntered("Error:  Source Not Provided!");
                return null;
            }

            BigDecimal fromCode =
                GlobalCC.checkBDNullValues(session.getAttribute("tt_trans_from_code"));

            if (GlobalCC.isEmptyBD(session.getAttribute("tt_trans_to_code"))) {
                GlobalCC.errorValueNotEntered("Error: Destination Not Provided!");
                return null;
            }
            BigDecimal toCode =
                GlobalCC.checkBDNullValues(session.getAttribute("tt_trans_to_code"));

            if (toCode.compareTo(fromCode) == 0) {
                GlobalCC.errorValueNotEntered("Error: Destination should not be same as Source!");
                return null;
            }

            String query =
                "begin INSERT INTO tqc_transfers_items (" + "                         tti_code,\n" +
                "                         tti_tt_code,\n" +
                "                         tti_item_code,\n" +
                "                         tti_item_name,\n" +
                "                         tti_item_sht_desc,\n" +
                "                         tti_item_type,\n" +
                "                         tti_trans_date,\n" +
                "                         tti_trans_to,\n" +
                "                         tti_trans_from,\n" +
                "                         tti_done_by,\n" +
                "                         tti_done_date,\n" +
                "                         tti_authorized,\n" +
                "                         tti_authorized_by,\n" +
                "                         tti_authorized_date)\n" +
                "                 VALUES (TQC_TTI_CODE_SEQ.NEXTVAL,\n" +
                "                         :v_tt_code,\n" +
                "                         :v_item_code,\n" +
                "                         :v_item_name,\n" +
                "                         :v_item_sht_desc,\n" +
                "                         :v_item_type,\n" +
                "                         SYSDATE,\n" +
                "                         :v_trans_to,\n" +
                "                         :v_trans_from,\n" +
                "                         :v_done_by,\n" +
                "                         SYSDATE,\n" +
                "                         NULL,\n" +
                "                         NULL,\n" +
                "                         NULL); end;";

           
            int rowCnt = tblTransferableItems.getRowCount();  
            if(rowCnt>0) {
                DBConnector dbCon = new DBConnector();
                conn = dbCon.getDatabaseConnection();
                stmt = (OracleCallableStatement)conn.prepareCall(query); 
              for(int i = 0; i < rowCnt; i++){
                JUCtrlValueBinding r =
                    (JUCtrlValueBinding)tblTransferableItems.getRowData(i);
                Boolean  Accept = (Boolean)r.getAttribute("ttiSelected"); 
                if (Accept == true) {
                    BigDecimal itemCode =
                        GlobalCC.checkBDNullValues((BigDecimal)r.getAttribute("ttiItemCode"));
                    BigDecimal ttCode =
                        GlobalCC.checkBDNullValues(session.getAttribute("tt_code"));
                    String itemShtDesc =
                        GlobalCC.checkNullValues((String)r.getAttribute("ttiItemShtDesc"));
                    String itemName =
                        GlobalCC.checkNullValues(r.getAttribute("ttiItemName"));
                    String itemType =
                        GlobalCC.checkNullValues(r.getAttribute("ttiItemType"));
                    BigDecimal itemTo =
                        GlobalCC.checkBDNullValues((BigDecimal)r.getAttribute("ttiItemCode"));
                    BigDecimal itemFrom =
                        GlobalCC.checkBDNullValues((BigDecimal)r.getAttribute("ttiFromCode"));
                    String user =
                        GlobalCC.checkNullValues(session.getAttribute("Username"));

                 
                    stmt.setBigDecimal("v_tt_code", ttCode);
                    stmt.setBigDecimal("v_item_code", itemCode);
                    stmt.setString("v_item_name", itemName);
                    stmt.setString("v_item_sht_desc", itemShtDesc);
                    stmt.setString("v_item_type", itemType);
                    stmt.setBigDecimal("v_trans_to", itemTo);
                    stmt.setBigDecimal("v_trans_from", itemFrom);
                    stmt.setString("v_done_by", user);

                    System.out.println("Adding itemCode: " + itemCode +
                                       " itemShtDesc: " + itemShtDesc +
                                       " itemName: " + itemName +
                                       " itemType: " + itemType);
                    System.out.println("item insert query: " + query);
                    stmt.addBatch();
                }  
              }
              stmt.executeBatch();
              conn.commit();
            } 
            ADFUtils.findIterator("fetchTranferableItemsIterator").executeQuery();
            ADFUtils.findIterator("fetchTranferedItemsIterator").executeQuery();
            GlobalCC.refreshUI(tblTransferableItems);
            GlobalCC.refreshUI(tblTransferedItems);
            GlobalCC.showPopup("fms:transferOrgItemsPopUp");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, null);
        }

        return null;
    }*/
    
    
    public String addTransferableItems2() { 
          return null;
        }
    
    public String removeTransferedItems2(){
          return null;
        }
    
    public String addTransferableItems() { 
        
                RowKeySet selection = tblTransferableItems.getSelectedRowKeys();
                if (!selection.iterator().hasNext()) {
                    GlobalCC.errorValueNotEntered("Error: No Record Selected");
                    return null;
                }
                if (GlobalCC.isEmptyBD((BigDecimal)session.getAttribute("tt_code"))) {
                    GlobalCC.errorValueNotEntered("Error: Transfer not initialized!");
                    return null;
                }

                if (GlobalCC.isEmptyBD(session.getAttribute("tt_trans_from_code"))) {
                    GlobalCC.errorValueNotEntered("Error:  Source Not Provided!");
                    return null;
                }

                BigDecimal fromCode =
                    GlobalCC.checkBDNullValues(session.getAttribute("tt_trans_from_code"));

                if (GlobalCC.isEmptyBD(session.getAttribute("tt_trans_to_code"))) {
                    GlobalCC.errorValueNotEntered("Error: Destination Not Provided!");
                    return null;
                }
                BigDecimal toCode =
                    GlobalCC.checkBDNullValues(session.getAttribute("tt_trans_to_code"));

                if (toCode.compareTo(fromCode) == 0) {
                    GlobalCC.errorValueNotEntered("Error: Destination should not be same as Source!");
                    return null;
                } 

               
                int rowCnt = tblTransferableItems.getRowCount();  
                if(rowCnt<=0) {
                    GlobalCC.errorValueNotEntered("Error: No Record Selected");
                    return null;
                } 
                 Session dbSess = HibernateUtil.getSession();
                 Transaction tx = null; 
                 try { 
                      tx = dbSess.beginTransaction();
                       
                    for(int i = 0; i < rowCnt; i++){
                      JUCtrlValueBinding r =
                          (JUCtrlValueBinding)tblTransferableItems.getRowData(i);
                      Boolean  Accept = (Boolean)r.getAttribute("ttiSelected"); 
                      if (Accept == true) {
                          BigDecimal itemCode =
                              GlobalCC.checkBDNullValues(r.getAttribute("ttiItemCode"));
                          BigDecimal ttCode =
                              GlobalCC.checkBDNullValues(session.getAttribute("tt_code"));
                          String itemShtDesc =
                              GlobalCC.checkNullValues((String)r.getAttribute("ttiItemShtDesc"));
                          String itemName =
                              GlobalCC.checkNullValues(r.getAttribute("ttiItemName"));
                          String itemType =
                              GlobalCC.checkNullValues(r.getAttribute("ttiItemType"));
                          BigDecimal itemTo =
                              GlobalCC.checkBDNullValues((BigDecimal)r.getAttribute("ttiItemCode"));
                          BigDecimal itemFrom =
                              GlobalCC.checkBDNullValues((BigDecimal)r.getAttribute("ttiFromCode"));
                          String user =
                              GlobalCC.checkNullValues(session.getAttribute("Username")); 
 
                          TransferItem item = new TransferItem(); 

                        //-----------set Client------------------//
                          item.setCode(null);
                          item.setItemCode(itemCode);
                          item.setItemName(itemName);
                          item.setItemShtDesc(itemShtDesc);
                          item.setItemType(itemType);
                          item.setTtCode(ttCode);
                          item.setTransTo(itemTo);
                          item.setTransFrom(itemFrom);
                          item.setDoneBy(user); 
                          java.sql.Date now = new java.sql.Date(new java.util.Date().getTime());
                          item.setDoneDate(now);
                          dbSess.persist(item); 
                      }  
                    }
                    tx.commit(); 

                    String message = "Transfer Saved Successfully!";
                    GlobalCC.INFORMATIONREPORTING(message);
                    ADFUtils.findIterator("fetchTranferableItemsIterator").executeQuery();
                    ADFUtils.findIterator("fetchTranferedItemsIterator").executeQuery();
                    GlobalCC.refreshUI(tblTransferableItems);
                    GlobalCC.refreshUI(tblTransferedItems);
                    GlobalCC.showPopup("fms:transferOrgItemsPopUp");
                                     
                 
                } catch (HibernateException e) {
                   if (tx!=null) tx.rollback();
                   GlobalCC.EXCEPTIONREPORTING(e); 
                } finally {
                   dbSess.close(); 
                }
                

            return null;
        }

    public String removeTransferedItems() {


        OracleCallableStatement callStmt = null;
        OracleConnection conn = null;

        try {
            RowKeySet selection = tblTransferedItems.getSelectedRowKeys();
            if (!selection.iterator().hasNext()) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
                return null;
            }

            String query =
                "begin tqc_setups_pkg.remove_transfered_items(?); end;";

            DBConnector dbCon = new DBConnector();
            conn = dbCon.getDatabaseConnection();

            callStmt = (OracleCallableStatement)conn.prepareCall(query);

            int rowCnt = tblTransferedItems.getRowCount();  
            if(rowCnt>0) {
                    for(int i = 0; i < rowCnt; i++){ 
                    JUCtrlValueBinding r =
                        (JUCtrlValueBinding)tblTransferedItems.getRowData(i);
                        Boolean  Accept = (Boolean)r.getAttribute("ttiSelected"); 
                   if(Accept == true){  
                        BigDecimal ttiCode = (BigDecimal)r.getAttribute("ttiCode");
                        System.out.println("Adding ttiCode: " + ttiCode);
                        if (!GlobalCC.isEmptyBD(ttiCode)) {
                            callStmt.setBigDecimal(1, ttiCode);
                            callStmt.addBatch();
                        }
                    }
                }
                callStmt.executeBatch();
                conn.commit();
            } 

            ADFUtils.findIterator("fetchTranferableItemsIterator").executeQuery();
            ADFUtils.findIterator("fetchTranferedItemsIterator").executeQuery();
            GlobalCC.refreshUI(tblTransferableItems);
            GlobalCC.refreshUI(tblTransferedItems);

            GlobalCC.showPopup("fms:transferOrgItemsPopUp");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        } finally {
            DbUtils.closeQuietly(conn, callStmt, null);
        }

        return null;
    }

    public String cancelBranchAgency() {

        txtBraCode.setValue(null);
        braBrnCode.setValue(null);
        braShortDesc.setValue(null);
        braName.setValue(null);
        braStatus.setValue(null);
        braManager.setValue(null);
        braAgnCode.setValue(null);
        braPostLevel.setValue(null);
        GlobalCC.hidePopup("fms:agenciesPopUp");
        return null;
    }

    public String closeOrgItemsTransferPopUp() {
        GlobalCC.hidePopup("fms:transferOrgItemsPopUp");
        return null;
    }

    public String resetBranchAgencyTransfer() {

        txtBraCode.setValue(null);
        braBrnCode.setValue(null);
        braShortDesc.setValue(null);
        braName.setValue(null);
        braStatus.setValue(null);
        braManager.setValue(null);
        braAgnCode.setValue(null);
        braPostLevel.setValue(null);
        GlobalCC.hidePopup("fms:agenciesPopUp");

        return null;
    }

    public String actionAcceptTransferTo() {

        DBConnector dbConnector = new DBConnector();
        OracleConnection conn = null;

        Object key2 = tblTransferToLov.getSelectedRowData();
        JUCtrlValueBinding n = (JUCtrlValueBinding)key2;

        List<Organization> branchAgenciesData = new ArrayList<Organization>();
        try {

            if (n != null) {
                txtTransferToName.setValue(n.getAttribute("destName"));
                session.setAttribute("tt_trans_to_code",
                                     n.getAttribute("destCode"));
                session.setAttribute("tt_trans_to_name",
                                     n.getAttribute("destName"));
                System.out.println("Selected tt_trans_to_code: " +
                                   session.getAttribute("tt_trans_to_code") +
                                   " tt_trans_to_name: " +
                                   session.getAttribute("tt_trans_to_name"));

                conn = dbConnector.getDatabaseConnection();
                String query =
                    "UPDATE tq_crm.tqc_transfers set tt_trans_to_code=?,tt_trans_to_name=?,tt_done_by=?,tt_done_date=SYSDATE WHERE tt_code =?";

                OracleCallableStatement callStmt = null;
                callStmt =
                        (OracleCallableStatement)(OracleCallableStatement)(OracleCallableStatement)conn.prepareCall(query);
                callStmt.setBigDecimal(1,
                                       (BigDecimal)session.getAttribute("tt_trans_to_code"));
                callStmt.setString(2,
                                   (String)session.getAttribute("tt_trans_to_name"));
                callStmt.setString(3,
                                   (String)session.getAttribute("Username"));
                callStmt.setBigDecimal(4,
                                       (BigDecimal)session.getAttribute("tt_code"));
                callStmt.execute();
                conn.commit();
                callStmt.close();
                conn.close();

                GlobalCC.refreshUI(txtTransferToName);

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }

        GlobalCC.hidePopup("fms:transferToLovPop");
        return null;
    }

    public String closeTransferToLov() {
        GlobalCC.hidePopup("fms:transferToLovPop");
        return null;
    }
    
    public String newFaAgent() {
        
            faAgentCode.setVisible(true);
            btnSaveFaAgent.setText("Save");
            if (session.getAttribute("unaCode") == null) {
                GlobalCC.errorValueNotEntered("Please Select Team First");
                return null;
            }
// clearBranchName();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:addFaAgent" + "').show(hints);");
    
        return null;
        }
    
    public String editFaAgent() {
                   
        
            Object key2 = FAagentsTab.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                faAgentCode.setValue(nodeBinding.getAttribute("fa_agent_code"));
                faShortDesc.setValue(nodeBinding.getAttribute("fa_agn_sht_desc"));
                faAgentName.setValue(nodeBinding.getAttribute("fa_agn_name"));            
                faAgentCode.setVisible(false);
                btnSaveFaAgent.setText("Update");
                GlobalCC.refreshUI(btnSaveFaAgent);

                // Open the popup dialog for Services
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "fms:addFaAgent" + "').show(hints);");
            } else {
                GlobalCC.INFORMATIONREPORTING("No record selected.");
                return null;
            }
            return null;
        
        }
    
    public String deleteFaAgent() {
        
            Object key2 = FAagentsTab.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "fms:confirmDeleteFaAgent" +
                                     "').show(hints);");
            } else {
                GlobalCC.INFORMATIONREPORTING("No record Selected");
            }
    
        return null;
        }
    
    public void actionConfirmDeleteFaAgent(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = FAagentsTab.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {

                String fa_agent_code =
                    GlobalCC.checkNullValues(nodeBinding.getAttribute("fa_agent_code"));
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn;
                conn = dbConnector.getDatabaseConnection();
                try {
                    String query = null;
                    //modify the query for treaty groups procedure
                    query =
                            "BEGIN TQC_WEB_ORGANIZATION_PKG.delete_fa_agent(?,?);END;";

                    OracleCallableStatement cst = null;
                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.setBigDecimal(1, fa_agent_code == null ? null : new BigDecimal(fa_agent_code));
                    cst.registerOutParameter(2, OracleTypes.VARCHAR);
                    cst.execute();
                    String errMessage = cst.getString(2);
                    if (errMessage != null) {
                        GlobalCC.INFORMATIONREPORTING(errMessage);
                    } else {
                        ADFUtils.findIterator("fetchFaAgentsIterator").executeQuery();
                        GlobalCC.refreshUI(FAagentsTab);
                        GlobalCC.INFORMATIONREPORTING("Record Deleted Successfully");
                    }
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);

                }
            } else {
                GlobalCC.INFORMATIONREPORTING("NO RECORD SELECTED");
            }
        }
    }
    
    public String actionSaveFaAgent() {
        
        
        String fateamCodeVal=null;
        String option = null;
        if(session.getAttribute("unaCode") != null){
                fateamCodeVal  = session.getAttribute("unaCode").toString();
               
            }
        
            // Check if the user wishes to SAVE or UPDATE
                    if (btnSaveFaAgent.getText().equals("Update")) {

                         option = "E";
                    } 
                    
            if (btnSaveFaAgent.getText().equals("Save")) {

                 option = "A";
            }

                        if (faAgentCode.getValue() == null) {
                            GlobalCC.errorValueNotEntered("Enter a Agent Code!");
                            return null;
                        }

                        if (faShortDesc.getValue() == null) {
                            GlobalCC.errorValueNotEntered("Enter Short Desc!");
                            return null;
                        }
                        if (faAgentName.getValue() == null) {
                            GlobalCC.errorValueNotEntered("Please enter Short Desc!");
                            return null;
                        }

                        DBConnector dbCon = new DBConnector();
                        OracleConnection conn = null;
                        conn = dbCon.getDatabaseConnection();
                        try {

                            String query =
                                "begin TQC_WEB_ORGANIZATION_PKG.update_fa_agents(?,?,?,?,?,?); end;";
                            OracleCallableStatement cst = null;
                            cst = (OracleCallableStatement)conn.prepareCall(query);
                            cst.setString(1, option);
                            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("ORGCode"));
                            cst.setString(3, faShortDesc.getValue().toString());
                            cst.setBigDecimal(4, (BigDecimal)session.getAttribute("unaCode"));
                            cst.setString(5, faAgentName.getValue().toString()); 
                            cst.setBigDecimal(6, new BigDecimal(faAgentCode.getValue().toString()));
//                            cst.registerOutParameter(6, OracleTypes.VARCHAR);
                            cst.execute();
//
//                            if (cst.getString(6) != null) {
//                                GlobalCC.errorValueNotEntered(cst.getString(6));
//                                return null;
//                            }
                              faShortDesc.setValue(null);
                              faAgentCode.setValue(null);                      
                              faAgentName.setValue(null);
                            ADFUtils.findIterator("fetchFaAgentsIterator").executeQuery();
                           GlobalCC.refreshUI(FAagentsTab);
                            
                            
                            GlobalCC.dismissPopUp("fms", "addFaAgent");
                            faShortDesc.setValue(null);
                            faAgentCode.setValue(null);                      
                            faAgentName.setValue(null);
                            btnSaveFaAgent.setText("Save");
                            GlobalCC.refreshUI(btnSaveFaAgent);
                            GlobalCC.INFORMATIONREPORTING("Process Successfully Completed!");
                            
                            cst.close();
                            conn.commit();
                            conn.close();
                        } catch (Exception e) {
                            GlobalCC.EXCEPTIONREPORTING(conn, e);

                        }
                    
                    return null;
        

        }
   
    
    
    public String newBranchUnit() {
        String val = (String)GlobalCC.getSysParamValue("ORG_AGENCY_TAB_APP");
      

        if (session.getAttribute("BRNCode") == null) {
            GlobalCC.errorValueNotEntered("Error: Select Branch");
            return null;
        }
        if (session.getAttribute("BRNCode") == null) {
            GlobalCC.errorValueNotEntered("Error: Select Branch");
            return null;
        }

        if (session.getAttribute("braCode") == null && "Y".equalsIgnoreCase(val) ) {
            GlobalCC.errorValueNotEntered("Error: Select Agency");
            return null;
        }

        txtAgencyName.setValue(session.getAttribute("braName"));
        GlobalCC.showPop("fms:branchUnitsPopUp");


        bruCode.setValue(null);
        bruBrnCode.setValue(session.getAttribute("BRNCode"));
        bruShortDesc.setValue(null);
        bruName.setValue(null);
        bruSupervisor.setValue(null);
        bruStatus.setValue(null);
        bruBraCode.setValue(session.getAttribute("braCode"));
        bruAgnCode.setValue(session.getAttribute("braAgnCode"));
        bruManager.setValue(null);
        bruPostLevel.setValue(null);
        txtManagerAllowed.setValue(null);
        txtOverideComm.setValue(null);
        session.removeAttribute("manager");
        /* bruManager.setVisible(false);
        btnShowBruManager.setVisible(false);
        outputlbMgr.setVisible(false);
        GlobalCC.refreshUI(bruManager);
        GlobalCC.refreshUI(btnShowBruManager);
        GlobalCC.refreshUI(outputlbMgr);*/
        return null;
    }

    public String viewBranchUnit() {
        /* if (session.getAttribute("agnCode") == null) {
            GlobalCC.errorValueNotEntered("Error: Select Agent/Specify Agency Manager");
            return null;
        }*/

        RowKeySet rowKeySet = unitsTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        unitsTab.setRowKey(key2);

        JUCtrlValueBinding r = (JUCtrlValueBinding)unitsTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        GlobalCC.showPop("fms:branchUnitsPopUp");

        bruAgnCode.setValue(r.getAttribute("bruAgnCode"));
        bruCode.setValue(r.getAttribute("bruCode"));
        bruBrnCode.setValue(r.getAttribute("bruBrnCode"));
        bruShortDesc.setValue(r.getAttribute("bruShortDesc"));
        bruName.setValue(r.getAttribute("bruName"));
        bruSupervisor.setValue(r.getAttribute("bruSupervisor"));
        bruStatus.setValue(r.getAttribute("bruStatus"));
        bruBraCode.setValue(r.getAttribute("bruBraCode"));
        bruManager.setValue(r.getAttribute("bruManager"));
        bruPostLevel.setValue(r.getAttribute("bruPostLevel"));
        txtBruAgnSeqNo.setValue(r.getAttribute("bruAgnSeqNo"));
        txtOverideComm.setValue(r.getAttribute("bruOverideCommEarned"));
        txtManagerAllowed.setValue(r.getAttribute("bruMngrAllowed"));
        polSeqNoUnits.setValue(r.getAttribute("unitspolicySeq"));
        propSeqNoUnits.setValue(r.getAttribute("unitspropSeq"));
        txtAgencyName.setValue(r.getAttribute("txtAgencyName"));
        session.setAttribute("manager", r.getAttribute("bruAgnCode"));
        bruManager.setVisible(true);
        btnShowBruManager.setVisible(true);
        outputlbMgr.setVisible(true);
        GlobalCC.refreshUI(bruManager);
        GlobalCC.refreshUI(txtAgencyName);
        GlobalCC.refreshUI(btnShowBruManager);
        GlobalCC.refreshUI(outputlbMgr);

        return null;
    }

    public String selectUnitManager() {

        RowKeySet rowKeySet = bruManagersLovTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        bruManagersLovTab.setRowKey(key2);

        JUCtrlValueBinding r =
            (JUCtrlValueBinding)bruManagersLovTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        /* String orgcode=GlobalCC.checkNullValues( session.getAttribute("ORGCode"));
        System.out.println("orgcode"+orgcode);
        if(orgcode!=null  ){
            BigDecimal code=new BigDecimal( orgcode) ;

            String val=Integer.toString(1);
            if(orgcode.equalsIgnoreCase(val) ){
             HashMap map=new HashMap();
         String brucode=GlobalCC.checkNullValues(bruCode.getValue());
         map=generateSeqNo("BE",null ,null ,null,brucode==null ? null : new BigDecimal(brucode), new BigDecimal(r.getAttribute("agnCode").toString()));
         if(map!=null){
             bruShortDesc.setValue(map.get("seq"));
             bruName.setValue(map.get("name"));
             }
           }
        }*/

        bruAgnCode.setValue(r.getAttribute("agnCode"));
        bruManager.setValue(r.getAttribute("agnName"));

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:bruManagersLovPopUp" + "').hide(hints);");


        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
        adfFacesContext.addPartialTarget(bruAgnCode);
        adfFacesContext.addPartialTarget(bruManager);


        return null;
    }


    public String saveBranchUnit() {
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        try {
            String bruCodeVal;
            String bruBrnCodeVal;
            String bruShortDescVal;
            String bruNameVal;
            String bruSupervisorVal;
            String bruStatusVal;
            String bruAgnCodeVal;
            String bruBraCodeVal;
            String bruManagerVal;
            String bruPostLevelVal;
            String bruAgnSeqNo;
            String bruUnitpref;
            String bruComtBuss;
            BigDecimal policyseqNoUnits;
            BigDecimal propseqNoUnits;
            String txtAgencyNameVal;
            BigDecimal txtTeamManagerSequenceNoVal;
            BigDecimal txtAgentsSequenceNoVal;
            bruCodeVal = GlobalCC.checkNullValues(bruCode.getValue());
            bruBrnCodeVal = GlobalCC.checkNullValues(bruBrnCode.getValue());
            bruShortDescVal =
                    GlobalCC.checkNullValues(bruShortDesc.getValue());
            bruNameVal = GlobalCC.checkNullValues(bruName.getValue());
            bruSupervisorVal =
                    GlobalCC.checkNullValues(bruSupervisor.getValue());
            bruStatusVal = GlobalCC.checkNullValues(bruStatus.getValue());
            bruAgnCodeVal = GlobalCC.checkNullValues(bruAgnCode.getValue());
            bruBraCodeVal = GlobalCC.checkNullValues(bruBraCode.getValue());
            bruManagerVal = GlobalCC.checkNullValues(bruManager.getValue());
            bruPostLevelVal =
                    GlobalCC.checkNullValues(bruPostLevel.getValue());
            bruAgnSeqNo = GlobalCC.checkNullValues(txtBruAgnSeqNo.getValue());
            String managerAllowed =
                GlobalCC.checkNullValues(txtManagerAllowed.getValue());
            String overideComm =
                GlobalCC.checkNullValues(txtOverideComm.getValue());
            bruUnitpref = GlobalCC.checkNullValues(txtUnitPrefix.getValue());
            bruComtBuss = GlobalCC.checkNullValues(txtComtBuss.getValue());
            txtAgencyNameVal =
                    GlobalCC.checkNullValues(txtAgencyName.getValue());

            if (txtTeamManagerSequenceNo.getValue() != null) {

                txtTeamManagerSequenceNoVal =
                        new BigDecimal(txtTeamManagerSequenceNo.getValue().toString());
            } else {
                txtTeamManagerSequenceNoVal = null;
            }

            if (txtAgentsSequenceNo.getValue() != null) {

                txtAgentsSequenceNoVal =
                        new BigDecimal(txtAgentsSequenceNo.getValue().toString());
            } else {
                txtAgentsSequenceNoVal = null;
            }

            if (polSeqNoUnits.getValue() != null) {
                try {
                    policyseqNoUnits =
                            new BigDecimal(polSeqNoUnits.getValue().toString());
                } catch (Exception e) {
                    GlobalCC.INFORMATIONREPORTING("Please enter a Number");
                    return null;
                }
            } else {
                policyseqNoUnits = null;
            }
            if (propSeqNoUnits.getValue() != null) {
                try {
                    propseqNoUnits =
                            new BigDecimal(propSeqNoUnits.getValue().toString());
                } catch (Exception e) {
                    GlobalCC.INFORMATIONREPORTING("Please enter a Number");
                    return null;
                }
            } else {
                propseqNoUnits = null;
            }
            if (bruBrnCodeVal == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Select Branch");
                return null;
            }


            /*if (bruAgnCodeVal == null) {
                    GlobalCC.errorValueNotEntered("Error Value Missing: Select Manager");
                    return null;
                }*/

            if (bruNameVal == null) {
                GlobalCC.errorValueNotEntered("Error Value Missing: Name");
                return null;
            }
            
            if (bruNameVal == null) {
                     GlobalCC.errorValueNotEntered("Error Value Missing: Name");
                     return null;
                 }
                 
                 if (bruStatusVal == null) {
                     GlobalCC.errorValueNotEntered("Error Value Missing: Status");
                     return null;
                 }
                 
                 if (bruManagerVal == null) {
                     GlobalCC.errorValueNotEntered("Error Value Missing:Supervisor ");
                     return null;
                 }
                 
                 
                 
                 if (overideComm == null) {
                     GlobalCC.errorValueNotEntered("Error Value Missing:Override Commission Earned");
                     return null;
                 }
            //check  if bruAgnSeqNo  is numeric ,but pass it as a string
            if (bruAgnSeqNo != null) {
                try {
                    int x = Integer.parseInt(bruAgnSeqNo);
                } catch (Exception e) {
                    GlobalCC.INFORMATIONREPORTING("Ensure Unit SeqNo is Numeric");
                    return null;
                }
            }
            Rendering rend = new Rendering();
            String auto = rend.getBranchUnitShtDesc();
            if (auto.equalsIgnoreCase("N") && bruShortDescVal == null) {
                GlobalCC.INFORMATIONREPORTING("Branch Unit Sht Desc Missing");
                return null;
            }
            rend = null;


            String query = null;
            //modify the query for treaty groups procedure
            query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_UNITS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";


            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables

            String option = null;
            if (bruCodeVal == null) {
                option = "A";
                callStmt.setString(1, option);
                callStmt.registerOutParameter(2, OracleTypes.NUMBER);
            } else {
                option = "E";
                callStmt.setString(1, option);
                callStmt.setBigDecimal(2,
                                       GlobalCC.checkBDNullValues(bruCodeVal));
            }

            callStmt.setBigDecimal(3,
                                   GlobalCC.checkBDNullValues(bruBrnCodeVal));
            callStmt.setString(4, bruShortDescVal);
            callStmt.setString(5, bruNameVal);
            callStmt.setString(6, bruSupervisorVal);
            callStmt.setString(7, bruStatusVal);
            callStmt.setBigDecimal(8,
                                   GlobalCC.checkBDNullValues(bruAgnCodeVal));
            callStmt.setBigDecimal(9,
                                   GlobalCC.checkBDNullValues(bruBraCodeVal));
            callStmt.setString(10, bruManagerVal);
            callStmt.setString(11, bruPostLevelVal);
            callStmt.setString(12, bruAgnSeqNo);
            callStmt.setString(13, managerAllowed);
            callStmt.setString(14, overideComm);
            callStmt.setObject(15, bruUnitpref);
            callStmt.setObject(16, bruComtBuss);
            callStmt.setString(17, GlobalCC.checkNullValues(policyseqNoUnits));
            callStmt.setString(18, GlobalCC.checkNullValues(propseqNoUnits));
            callStmt.setString(19, txtAgencyNameVal);
            callStmt.setBigDecimal(20, txtTeamManagerSequenceNoVal);
            callStmt.setBigDecimal(21, txtAgentsSequenceNoVal);

            callStmt.registerOutParameter(22, OracleTypes.VARCHAR);
            callStmt.execute();

            String errMessage = null;
            //  errMessage = callStmt.getString(17);
            if (option.equalsIgnoreCase("A")) {
                bruCodeVal = callStmt.getString(2);
            }

            String orgcode =
                GlobalCC.checkNullValues(session.getAttribute("ORGCode"));
            System.out.println("orgcode" + orgcode);
            if (orgcode != null) {
                BigDecimal code = new BigDecimal(orgcode);

                String val = Integer.toString(1);
                if (orgcode.equalsIgnoreCase(val)) {
                    HashMap map = new HashMap();
                    BigDecimal braCode = null;
                    if (session.getAttribute("braCode") != null) {
                        braCode =
                                new BigDecimal(session.getAttribute("braCode").toString());
                    }
                    map =
generateSeqNo("BE", null, (bruBrnCodeVal == null ? null :
                           new BigDecimal(bruBrnCodeVal)), braCode,
              (bruCodeVal == null ? null : new BigDecimal(bruCodeVal)),
              (bruAgnCodeVal == null ? null : new BigDecimal(bruAgnCodeVal)),
              "N");
                    if (map != null) {
                        bruShortDesc.setValue(map.get("seq"));
                        bruName.setValue(map.get("name"));
                    }
                }
            }

            callStmt.close();
            conn.commit();
            conn.close();

            bruCode.setValue(null);
            bruShortDesc.setValue(null);
            bruName.setValue(null);
            bruSupervisor.setValue(null);
            bruStatus.setValue(null);
            bruBraCode.setValue(null);
            bruManager.setValue(null);
            bruPostLevel.setValue(null);


            if (errMessage != null) {
                GlobalCC.INFORMATIONREPORTING(errMessage);
                return null;
            }
            GlobalCC.hidePopup("fms:branchUnitsPopUp");
            ADFUtils.findIterator("findBranchUnitsIterator").executeQuery();
            GlobalCC.refreshUI(unitsTab);

            GlobalCC.INFORMATIONREPORTING("Record Processed Successfully.");

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String deleteBranchUnit() {
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:confirmDeleteUnits" + "').show(hints);");
        RowKeySet rowKeySet = unitsTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        return null;
    }

    public String cancelBranchUnit() {

        bruCode.setValue(null);
        bruShortDesc.setValue(null);
        bruName.setValue(null);
        bruSupervisor.setValue(null);
        bruStatus.setValue(null);
        bruAgnCode.setValue(null);
        bruBraCode.setValue(null);
        bruManager.setValue(null);
        bruPostLevel.setValue(null);
        GlobalCC.hidePopup("fms:branchUnitsPopUp");

        return null;
    }

    public void setOrgCode(RichInputNumberSpinbox orgCode) {
        this.orgCode = orgCode;
    }

    public RichInputNumberSpinbox getOrgCode() {
        return orgCode;
    }

    public void setOrgShortDesc(RichInputText orgShortDesc) {
        this.orgShortDesc = orgShortDesc;
    }

    public RichInputText getOrgShortDesc() {
        return orgShortDesc;
    }

    public void setOrgName(RichInputText orgName) {
        this.orgName = orgName;
    }

    public RichInputText getOrgName() {
        return orgName;
    }

    public void setOrgAddrs(RichInputText orgAddrs) {
        this.orgAddrs = orgAddrs;
    }

    public RichInputText getOrgAddrs() {
        return orgAddrs;
    }

    public void setOrgTwnCode(RichInputNumberSpinbox orgTwnCode) {
        this.orgTwnCode = orgTwnCode;
    }

    public RichInputNumberSpinbox getOrgTwnCode() {
        return orgTwnCode;
    }

    public void setOrgCouCode(RichInputNumberSpinbox orgCouCode) {
        this.orgCouCode = orgCouCode;
    }

    public RichInputNumberSpinbox getOrgCouCode() {
        return orgCouCode;
    }

    public void setOrgEmail(RichInputText orgEmail) {
        this.orgEmail = orgEmail;
    }

    public RichInputText getOrgEmail() {
        return orgEmail;
    }

    public void setOrgPhyAddrs(RichInputText orgPhyAddrs) {
        this.orgPhyAddrs = orgPhyAddrs;
    }

    public RichInputText getOrgPhyAddrs() {
        return orgPhyAddrs;
    }

    public void setOrgCurCode(RichInputNumberSpinbox orgCurCode) {
        this.orgCurCode = orgCurCode;
    }

    public RichInputNumberSpinbox getOrgCurCode() {
        return orgCurCode;
    }

    public void setOrgZip(RichInputText orgZip) {
        this.orgZip = orgZip;
    }

    public RichInputText getOrgZip() {
        return orgZip;
    }

    public void setOrgFax(RichInputText orgFax) {
        this.orgFax = orgFax;
    }

    public RichInputText getOrgFax() {
        return orgFax;
    }

    public void setOrgTel1(RichInputText orgTel1) {
        this.orgTel1 = orgTel1;
    }

    public RichInputText getOrgTel1() {
        return orgTel1;
    }

    public void setOrgTel2(RichInputText orgTel2) {
        this.orgTel2 = orgTel2;
    }

    public RichInputText getOrgTel2() {
        return orgTel2;
    }

    public void setOrgRptLogo(RichInputText orgRptLogo) {
        this.orgRptLogo = orgRptLogo;
    }

    public RichInputText getOrgRptLogo() {
        return orgRptLogo;
    }

    public void setOrgMotto(RichInputText orgMotto) {
        this.orgMotto = orgMotto;
    }

    public RichInputText getOrgMotto() {
        return orgMotto;
    }

    public void setOrgPinNo(RichInputText orgPinNo) {
        this.orgPinNo = orgPinNo;
    }

    public RichInputText getOrgPinNo() {
        return orgPinNo;
    }

    public void setOrgEdCode(RichInputText orgEdCode) {
        this.orgEdCode = orgEdCode;
    }

    public RichInputText getOrgEdCode() {
        return orgEdCode;
    }

    public void setOrgItemAccCode(RichInputText orgItemAccCode) {
        this.orgItemAccCode = orgItemAccCode;
    }

    public RichInputText getOrgItemAccCode() {
        return orgItemAccCode;
    }

    public void setOrgOtherName(RichInputText orgOtherName) {
        this.orgOtherName = orgOtherName;
    }

    public RichInputText getOrgOtherName() {
        return orgOtherName;
    }

    public void setOrgType(RichSelectOneChoice orgType) {
        this.orgType = orgType;
    }

    public RichSelectOneChoice getOrgType() {
        return orgType;
    }

    public void setOrgWebBrnCode(RichInputNumberSpinbox orgWebBrnCode) {
        this.orgWebBrnCode = orgWebBrnCode;
    }

    public RichInputNumberSpinbox getOrgWebBrnCode() {
        return orgWebBrnCode;
    }

    public void setOrgWebAddrs(RichInputText orgWebAddrs) {
        this.orgWebAddrs = orgWebAddrs;
    }

    public RichInputText getOrgWebAddrs() {
        return orgWebAddrs;
    }

    public void setOrgAgnCode(RichInputNumberSpinbox orgAgnCode) {
        this.orgAgnCode = orgAgnCode;
    }

    public RichInputNumberSpinbox getOrgAgnCode() {
        return orgAgnCode;
    }

    public void setOrgDirectors(RichInputText orgDirectors) {
        this.orgDirectors = orgDirectors;
    }

    public RichInputText getOrgDirectors() {
        return orgDirectors;
    }

    public void setOrgLangCode(RichInputNumberSpinbox orgLangCode) {
        this.orgLangCode = orgLangCode;
    }

    public RichInputNumberSpinbox getOrgLangCode() {
        return orgLangCode;
    }

    public void setOrgAvatar(RichInputText orgAvatar) {
        this.orgAvatar = orgAvatar;
    }

    public RichInputText getOrgAvatar() {
        return orgAvatar;
    }

    public void setOrgTree(RichTree orgTree) {
        this.orgTree = orgTree;
    }

    public RichTree getOrgTree() {
        return orgTree;
    }

    public void setOrgDetails(RichPanelGroupLayout orgDetails) {
        this.orgDetails = orgDetails;
    }

    public RichPanelGroupLayout getOrgDetails() {
        return orgDetails;
    }

    public void setCountriesLovTab(RichTable countriesLovTab) {
        this.countriesLovTab = countriesLovTab;
    }

    public RichTable getCountriesLovTab() {
        return countriesLovTab;
    }

    public void setCountriesLovPopUp(RichPopup countriesLovPopUp) {
        this.countriesLovPopUp = countriesLovPopUp;
    }

    public RichPopup getCountriesLovPopUp() {
        return countriesLovPopUp;
    }

    public void setTownsLovTab(RichTable townsLovTab) {
        this.townsLovTab = townsLovTab;
    }

    public RichTable getTownsLovTab() {
        return townsLovTab;
    }

    public void setTownsLovPopUp(RichPopup townsLovPopUp) {
        this.townsLovPopUp = townsLovPopUp;
    }

    public RichPopup getTownsLovPopUp() {
        return townsLovPopUp;
    }

    public void setOrgCountry(RichInputText orgCountry) {
        this.orgCountry = orgCountry;
    }

    public RichInputText getOrgCountry() {
        return orgCountry;
    }

    public void setOrgTown(RichInputText orgTown) {
        this.orgTown = orgTown;
    }

    public RichInputText getOrgTown() {
        return orgTown;
    }

    public void setOrgBaseCurrency(RichInputText orgBaseCurrency) {
        this.orgBaseCurrency = orgBaseCurrency;
    }

    public RichInputText getOrgBaseCurrency() {
        return orgBaseCurrency;
    }

    public void setCurrenciesLovTab(RichTable currenciesLovTab) {
        this.currenciesLovTab = currenciesLovTab;
    }

    public RichTable getCurrenciesLovTab() {
        return currenciesLovTab;
    }

    public void setCurrenciesLovPopUp(RichPopup currenciesLovPopUp) {
        this.currenciesLovPopUp = currenciesLovPopUp;
    }

    public RichPopup getCurrenciesLovPopUp() {
        return currenciesLovPopUp;
    }

    public void setRegionsTab(RichTable regionsTab) {
        this.regionsTab = regionsTab;
    }

    public RichTable getRegionsTab() {
        return regionsTab;
    }

    public void setBranchesTab(RichTable branchesTab) {
        this.branchesTab = branchesTab;
    }

    public RichTable getBranchesTab() {
        return branchesTab;
    }

    public void setUnitsTab(RichTable unitsTab) {
        this.unitsTab = unitsTab;
    }

    public RichTable getUnitsTab() {
        return unitsTab;
    }

    public void setRegionsPopUp(RichPopup regionsPopUp) {
        this.regionsPopUp = regionsPopUp;
    }

    public RichPopup getRegionsPopUp() {
        return regionsPopUp;
    }

    public void setRegCode(RichInputNumberSpinbox regCode) {
        this.regCode = regCode;
    }

    public RichInputNumberSpinbox getRegCode() {
        return regCode;
    }

    public void setRegOrgCode(RichInputNumberSpinbox regOrgCode) {
        this.regOrgCode = regOrgCode;
    }

    public RichInputNumberSpinbox getRegOrgCode() {
        return regOrgCode;
    }

    public void setRegShortDesc(RichInputText regShortDesc) {
        this.regShortDesc = regShortDesc;
    }

    public RichInputText getRegShortDesc() {
        return regShortDesc;
    }

    public void setRegName(RichInputText regName) {
        this.regName = regName;
    }

    public RichInputText getRegName() {
        return regName;
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

    public void setRegAgnCode(RichInputNumberSpinbox regAgnCode) {
        this.regAgnCode = regAgnCode;
    }

    public RichInputNumberSpinbox getRegAgnCode() {
        return regAgnCode;
    }

    public void setRegPostLevel(RichSelectOneChoice regPostLevel) {
        this.regPostLevel = regPostLevel;
    }

    public RichSelectOneChoice getRegPostLevel() {
        return regPostLevel;
    }

    public void setRegMngrAllowed(RichSelectOneChoice regMngrAllowed) {
        this.regMngrAllowed = regMngrAllowed;
    }

    public RichSelectOneChoice getRegMngrAllowed() {
        return regMngrAllowed;
    }

    public void setRegOverideCommEarned(RichSelectOneChoice regOverideCommEarned) {
        this.regOverideCommEarned = regOverideCommEarned;
    }

    public RichSelectOneChoice getRegOverideCommEarned() {
        return regOverideCommEarned;
    }

    public void setBranchesPopUp(RichPopup branchesPopUp) {
        this.branchesPopUp = branchesPopUp;
    }

    public RichPopup getBranchesPopUp() {
        return branchesPopUp;
    }

    public void setBrnCode(RichInputNumberSpinbox brnCode) {
        this.brnCode = brnCode;
    }

    public RichInputNumberSpinbox getBrnCode() {
        return brnCode;
    }

    public void setBrnShortDesc(RichInputText brnShortDesc) {
        this.brnShortDesc = brnShortDesc;
    }

    public RichInputText getBrnShortDesc() {
        return brnShortDesc;
    }

    public void setBrnRegCode(RichInputNumberSpinbox brnRegCode) {
        this.brnRegCode = brnRegCode;
    }

    public RichInputNumberSpinbox getBrnRegCode() {
        return brnRegCode;
    }

    public void setBrnName(RichInputText brnName) {
        this.brnName = brnName;
    }

    public RichInputText getBrnName() {
        return brnName;
    }

    public void setBrnPhyAddrs(RichInputText brnPhyAddrs) {
        this.brnPhyAddrs = brnPhyAddrs;
    }

    public RichInputText getBrnPhyAddrs() {
        return brnPhyAddrs;
    }

    public void setBrnEmail(RichInputText brnEmail) {
        this.brnEmail = brnEmail;
    }

    public RichInputText getBrnEmail() {
        return brnEmail;
    }

    public void setBrnPostAddrs(RichInputText brnPostAddrs) {
        this.brnPostAddrs = brnPostAddrs;
    }

    public RichInputText getBrnPostAddrs() {
        return brnPostAddrs;
    }

    public void setBrnTwnCode(RichInputNumberSpinbox brnTwnCode) {
        this.brnTwnCode = brnTwnCode;
    }

    public RichInputNumberSpinbox getBrnTwnCode() {
        return brnTwnCode;
    }

    public void setBrnCouCode(RichInputNumberSpinbox brnCouCode) {
        this.brnCouCode = brnCouCode;
    }

    public RichInputNumberSpinbox getBrnCouCode() {
        return brnCouCode;
    }

    public void setBrnContact(RichInputText brnContact) {
        this.brnContact = brnContact;
    }

    public RichInputText getBrnContact() {
        return brnContact;
    }

    public void setBrnManager(RichInputText brnManager) {
        this.brnManager = brnManager;
    }

    public RichInputText getBrnManager() {
        return brnManager;
    }

    public void setBrnTel(RichInputText brnTel) {
        this.brnTel = brnTel;
    }

    public RichInputText getBrnTel() {
        return brnTel;
    }

    public void setBrnFax(RichInputText brnFax) {
        this.brnFax = brnFax;
    }

    public RichInputText getBrnFax() {
        return brnFax;
    }

    public void setBrnGenPolClm(RichInputNumberSpinbox brnGenPolClm) {
        this.brnGenPolClm = brnGenPolClm;
    }

    public RichInputNumberSpinbox getBrnGenPolClm() {
        return brnGenPolClm;
    }

    public void setBrnBnsCode(RichInputNumberSpinbox brnBnsCode) {
        this.brnBnsCode = brnBnsCode;
    }

    public RichInputNumberSpinbox getBrnBnsCode() {
        return brnBnsCode;
    }

    public void setBrnAgnCode(RichInputNumberSpinbox brnAgnCode) {
        this.brnAgnCode = brnAgnCode;
    }

    public RichInputNumberSpinbox getBrnAgnCode() {
        return brnAgnCode;
    }

    public void setBrnPostLevel(RichSelectOneChoice brnPostLevel) {
        this.brnPostLevel = brnPostLevel;
    }

    public RichSelectOneChoice getBrnPostLevel() {
        return brnPostLevel;
    }

    public void setBranchNamesLovTab(RichTable branchNamesLovTab) {
        this.branchNamesLovTab = branchNamesLovTab;
    }

    public RichTable getBranchNamesLovTab() {
        return branchNamesLovTab;
    }

    public void setBranchNamesLovPopUp(RichPopup branchNamesLovPopUp) {
        this.branchNamesLovPopUp = branchNamesLovPopUp;
    }

    public RichPopup getBranchNamesLovPopUp() {
        return branchNamesLovPopUp;
    }

    public void setBranchUnitsPopUp(RichPopup branchUnitsPopUp) {
        this.branchUnitsPopUp = branchUnitsPopUp;
    }

    public RichPopup getBranchUnitsPopUp() {
        return branchUnitsPopUp;
    }

    public void setBruCode(RichInputNumberSpinbox bruCode) {
        this.bruCode = bruCode;
    }

    public RichInputNumberSpinbox getBruCode() {
        return bruCode;
    }

    public void setBruBrnCode(RichInputNumberSpinbox bruBrnCode) {
        this.bruBrnCode = bruBrnCode;
    }

    public RichInputNumberSpinbox getBruBrnCode() {
        return bruBrnCode;
    }

    public void setBruShortDesc(RichInputText bruShortDesc) {
        this.bruShortDesc = bruShortDesc;
    }

    public RichInputText getBruShortDesc() {
        return bruShortDesc;
    }

    public void setBruName(RichInputText bruName) {
        this.bruName = bruName;
    }

    public RichInputText getBruName() {
        return bruName;
    }

    public void setBruSupervisor(RichInputText bruSupervisor) {
        this.bruSupervisor = bruSupervisor;
    }

    public RichInputText getBruSupervisor() {
        return bruSupervisor;
    }

    public void setBruStatus(RichSelectOneChoice bruStatus) {
        this.bruStatus = bruStatus;
    }

    public RichSelectOneChoice getBruStatus() {
        return bruStatus;
    }

    public void setBruAgnCode(RichInputNumberSpinbox bruAgnCode) {
        this.bruAgnCode = bruAgnCode;
    }

    public RichInputNumberSpinbox getBruAgnCode() {
        return bruAgnCode;
    }

    public void setBruBraCode(RichInputNumberSpinbox bruBraCode) {
        this.bruBraCode = bruBraCode;
    }

    public RichInputNumberSpinbox getBruBraCode() {
        return bruBraCode;
    }

    public void setBruManager(RichInputText bruManager) {
        this.bruManager = bruManager;
    }

    public RichInputText getBruManager() {
        return bruManager;
    }

    public void setBruPostLevel(RichSelectOneChoice bruPostLevel) {
        this.bruPostLevel = bruPostLevel;
    }

    public RichSelectOneChoice getBruPostLevel() {
        return bruPostLevel;
    }

    public void setRegManager(RichInputText regManager) {
        this.regManager = regManager;
    }

    public RichInputText getRegManager() {
        return regManager;
    }

    public void setRegManagerslovTab(RichTable regManagerslovTab) {
        this.regManagerslovTab = regManagerslovTab;
    }

    public RichTable getRegManagerslovTab() {
        return regManagerslovTab;
    }

    public void setRegManagerslovPopUp(RichPopup regManagerslovPopUp) {
        this.regManagerslovPopUp = regManagerslovPopUp;
    }

    public RichPopup getRegManagerslovPopUp() {
        return regManagerslovPopUp;
    }

    public void setBrnManagersLovPopUp(RichPopup brnManagersLovPopUp) {
        this.brnManagersLovPopUp = brnManagersLovPopUp;
    }

    public RichPopup getBrnManagersLovPopUp() {
        return brnManagersLovPopUp;
    }

    public void setBrnManagersLovTab(RichTable brnManagersLovTab) {
        this.brnManagersLovTab = brnManagersLovTab;
    }

    public RichTable getBrnManagersLovTab() {
        return brnManagersLovTab;
    }

    public void setBruManagersLovPopUp(RichPopup bruManagersLovPopUp) {
        this.bruManagersLovPopUp = bruManagersLovPopUp;
    }

    public RichPopup getBruManagersLovPopUp() {
        return bruManagersLovPopUp;
    }

    public void setBruManagersLovTab(RichTable bruManagersLovTab) {
        this.bruManagersLovTab = bruManagersLovTab;
    }

    public RichTable getBruManagersLovTab() {
        return bruManagersLovTab;
    }

    public void setAgenciesTab(RichTable agenciesTab) {
        this.agenciesTab = agenciesTab;
    }

    public RichTable getAgenciesTab() {
        return agenciesTab;
    }


    public void setBraBrnCode(RichInputNumberSpinbox braBrnCode) {
        this.braBrnCode = braBrnCode;
    }

    public RichInputNumberSpinbox getBraBrnCode() {
        return braBrnCode;
    }

    public void setBraShortDesc(RichInputText braShortDesc) {
        this.braShortDesc = braShortDesc;
    }

    public RichInputText getBraShortDesc() {
        return braShortDesc;
    }

    public void setBraName(RichInputText braName) {
        this.braName = braName;
    }

    public RichInputText getBraName() {
        return braName;
    }

    public void setBraStatus(RichSelectOneChoice braStatus) {
        this.braStatus = braStatus;
    }

    public RichSelectOneChoice getBraStatus() {
        return braStatus;
    }

    public void setBraManager(RichInputText braManager) {
        this.braManager = braManager;
    }

    public RichInputText getBraManager() {
        return braManager;
    }

    public void setBraAgnCode(RichInputNumberSpinbox braAgnCode) {
        this.braAgnCode = braAgnCode;
    }

    public RichInputNumberSpinbox getBraAgnCode() {
        return braAgnCode;
    }

    public void setBraPostLevel(RichSelectOneChoice braPostLevel) {
        this.braPostLevel = braPostLevel;
    }

    public RichSelectOneChoice getBraPostLevel() {
        return braPostLevel;
    }

    public void setAgenciesPopUp(RichPopup agenciesPopUp) {
        this.agenciesPopUp = agenciesPopUp;
    }

    public RichPopup getAgenciesPopUp() {
        return agenciesPopUp;
    }

    public void setBraManagersLovPopUp(RichPopup braManagersLovPopUp) {
        this.braManagersLovPopUp = braManagersLovPopUp;
    }

    public RichPopup getBraManagersLovPopUp() {
        return braManagersLovPopUp;
    }

    public void setBraManagersLovTab(RichTable braManagersLovTab) {
        this.braManagersLovTab = braManagersLovTab;
    }

    public RichTable getBraManagersLovTab() {
        return braManagersLovTab;
    }

    public void setOrgManagersLovTab(RichTable orgManagersLovTab) {
        this.orgManagersLovTab = orgManagersLovTab;
    }

    public RichTable getOrgManagersLovTab() {
        return orgManagersLovTab;
    }

    public void setOrgManagersLovPopUp(RichPopup orgManagersLovPopUp) {
        this.orgManagersLovPopUp = orgManagersLovPopUp;
    }

    public RichPopup getOrgManagersLovPopUp() {
        return orgManagersLovPopUp;
    }

    public void setOrgManager(RichInputText orgManager) {
        this.orgManager = orgManager;
    }

    public RichInputText getOrgManager() {
        return orgManager;
    }

    public void setTblOrgDivisions(RichTable tblOrgDivisions) {
        this.tblOrgDivisions = tblOrgDivisions;
    }

    public RichTable getTblOrgDivisions() {
        return tblOrgDivisions;
    }

    public void setTblDivSubDivisions(RichTable tblDivSubDivisions) {
        this.tblDivSubDivisions = tblDivSubDivisions;
    }

    public RichTable getTblDivSubDivisions() {
        return tblDivSubDivisions;
    }

    public void tblOrgDivisionsSelectionListener(SelectionEvent selectionEvent) {
        Object key2 = tblOrgDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            session.setAttribute("divisionCode",
                                 nodeBinding.getAttribute("DIV_CODE"));

            ADFUtils.findIterator("findSubDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(tblDivSubDivisions);
            ADFUtils.findIterator("findBranchDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(brnDivisionsLOV);
        }
    }

    public void refreshFields() {
        txtDivCodePop.setValue(null);
        txtDivShtDescPop.setValue(null);
        txtDivNamePop.setValue(null);
        txtDivOrder.setValue(null);

        txtDirectorName.setValue(null);
        txtDirectorCode.setValue(null);
        txtAssDirectorName.setValue(null);
        txtAssDirectorCode.setValue(null);


    }

    public String actionNewOrgDivision() {
        refreshFields();
        btnSaveOrgDivisionPop.setText("Save");

        // Open the popup dialog
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:orgDivisionPop" + "').show(hints);");
        return null;
    }

    public void setBtnSaveOrgDivisionPop(RichCommandButton btnSaveOrgDivisionPop) {
        this.btnSaveOrgDivisionPop = btnSaveOrgDivisionPop;
    }

    public RichCommandButton getBtnSaveOrgDivisionPop() {
        return btnSaveOrgDivisionPop;
    }

    public String actionSaveOrgDivisionPop() {

        // Check if the user wishes to SAVE or UPDATE
        if (btnSaveOrgDivisionPop.getText().equals("Edit")) {
            actionUpdateOrgDivisionPop();
        } else {

            if (txtDivNamePop.getValue() == null) {
                GlobalCC.errorValueNotEntered("Enter a Division Name");
                return null;
            }

            if (txtDivShtDescPop == null) {
                GlobalCC.errorValueNotEntered("Enter a Division Short Desc");
                return null;
            }
            if (session.getAttribute("ORGCode") == null) {
                GlobalCC.errorValueNotEntered("Please select an Organization!");
                return null;
            }

            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            conn = dbCon.getDatabaseConnection();
            try {

                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_DIVISIONS(?,?,?,?,?,?,?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, txtDivNamePop.getValue().toString());
                cst.setString(4, txtDivShtDescPop.getValue().toString());
                cst.setString(5, txtDivStatusPop.getValue().toString());
                cst.setBigDecimal(6,
                                  (BigDecimal)session.getAttribute("ORGCode"));
                cst.setBigDecimal(7,
                                  (BigDecimal)session.getAttribute("username"));
                cst.setBigDecimal(8, (BigDecimal)txtDivOrder.getValue());
                cst.setObject(9, txtDirectorCode.getValue());
                cst.setObject(10, txtAssDirectorCode.getValue());
                cst.registerOutParameter(11, OracleTypes.VARCHAR);
                cst.execute();

                if (cst.getString(11) != null) {
                    GlobalCC.errorValueNotEntered(cst.getString(11));
                    return null;
                }

                ADFUtils.findIterator("fetchOrgDivisionsIterator").executeQuery();
                GlobalCC.refreshUI(tblOrgDivisions);
                GlobalCC.dismissPopUp("fms", "orgDivisionPop");

                GlobalCC.INFORMATIONREPORTING("New Record CREATED Successfully!");
                cst.close();
                conn.commit();
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);

            }
        }
        return null;

    }

    public String actionUpdateOrgDivisionPop() {

        if (txtDivNamePop.getValue() == null) {
            GlobalCC.errorValueNotEntered("Enter a Division Name");
            return null;
        }

        if (txtDivNamePop.getValue() == null) {
            GlobalCC.errorValueNotEntered("Enter a Division Name");
            return null;
        }

        if (txtDivShtDescPop == null) {
            GlobalCC.errorValueNotEntered("Enter a Division Short Desc");
            return null;
        }

        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();
        try {
            String query =
                "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_DIVISIONS(?,?,?,?,?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, "E");
            cst.setBigDecimal(2,
                              new BigDecimal(txtDivCodePop.getValue().toString()));
            cst.setString(3, txtDivNamePop.getValue().toString());
            cst.setString(4, txtDivShtDescPop.getValue().toString());
            cst.setString(5, txtDivStatusPop.getValue().toString());
            cst.setBigDecimal(6, (BigDecimal)session.getAttribute("ORGCode"));
            cst.setBigDecimal(7, (BigDecimal)session.getAttribute("username"));
            cst.setBigDecimal(8, (BigDecimal)txtDivOrder.getValue());
            cst.setObject(9, txtDirectorCode.getValue());
            cst.setObject(10, txtAssDirectorCode.getValue());
            cst.registerOutParameter(11, OracleTypes.VARCHAR);
            cst.execute();

            if (cst.getString(11) != null) {
                GlobalCC.errorValueNotEntered(cst.getString(11));
                return null;
            }

            ADFUtils.findIterator("fetchOrgDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(tblOrgDivisions);

            GlobalCC.INFORMATIONREPORTING("Record UPDATED Successfully!");
            cst.close();
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        return null;

    }

    public void setTxtDivCodePop(RichInputText txtDivCodePop) {
        this.txtDivCodePop = txtDivCodePop;
    }

    public RichInputText getTxtDivCodePop() {
        return txtDivCodePop;
    }

    public void setTxtDivShtDescPop(RichInputText txtDivShtDescPop) {
        this.txtDivShtDescPop = txtDivShtDescPop;
    }

    public RichInputText getTxtDivShtDescPop() {
        return txtDivShtDescPop;
    }

    public void setTxtDivNamePop(RichInputText txtDivNamePop) {
        this.txtDivNamePop = txtDivNamePop;
    }

    public RichInputText getTxtDivNamePop() {
        return txtDivNamePop;
    }

    public void setTxtDivStatusPop(RichSelectOneChoice txtDivStatusPop) {
        this.txtDivStatusPop = txtDivStatusPop;
    }

    public RichSelectOneChoice getTxtDivStatusPop() {
        return txtDivStatusPop;
    }

    public String actionEditOrgDivision() {
        Object key2 = tblOrgDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            txtDivCodePop.setValue(nodeBinding.getAttribute("DIV_CODE"));
            txtDivShtDescPop.setValue(nodeBinding.getAttribute("DIV_SHT_DESC"));
            txtDivNamePop.setValue(nodeBinding.getAttribute("DIV_NAME"));
            txtDivStatusPop.setValue(nodeBinding.getAttribute("DIV_DIVISION_STATUS"));
            txtDivOrder.setValue(nodeBinding.getAttribute("divOrder"));
            txtDirectorName.setValue(nodeBinding.getAttribute("divDirectorName"));
            txtDirectorCode.setValue(nodeBinding.getAttribute("divDirectorCode"));
            txtAssDirectorName.setValue(nodeBinding.getAttribute("divAssDirectorName"));
            txtAssDirectorCode.setValue(nodeBinding.getAttribute("divAssDirectorCode"));

            btnSaveOrgDivisionPop.setText("Edit");

            // Open the popup dialog for Services
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:orgDivisionPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String actionDeleteOrgDivision() {
        Object key2 = tblOrgDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            conn = dbCon.getDatabaseConnection();
            try {
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_DIVISIONS(?,?,?,?,?,?,?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  (BigDecimal)nodeBinding.getAttribute("DIV_CODE"));
                cst.setString(3, (String)nodeBinding.getAttribute("DIV_NAME"));
                cst.setString(4,
                              (String)nodeBinding.getAttribute("DIV_SHT_DESC"));
                cst.setString(5,
                              (String)nodeBinding.getAttribute("DIV_DIVISION_STATUS"));
                cst.setBigDecimal(6,
                                  (BigDecimal)session.getAttribute("ORGCode"));
                cst.setString(7, (String)session.getAttribute("Username"));
                cst.setString(8, null);
                cst.setString(9, null);
                cst.setString(10, null);
                cst.registerOutParameter(11, OracleTypes.VARCHAR);
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("fetchOrgDivisionsIterator").executeQuery();
                GlobalCC.refreshUI(tblOrgDivisions);

                GlobalCC.INFORMATIONREPORTING("Record DELETED Successfully!");


            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No record selected.");
            return null;
        }
        return null;
    }

    public String btnSaveDivSubDivision() {
        // Check if the user wishes to SAVE or UPDATE
        if (btnSaveDivSubDivision.getText().equals("Edit")) {
            updateDivSubDivision();
        } else {
            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            conn = dbCon.getDatabaseConnection();

            try {
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_SUBDIVISIONS(?,?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setString(3, btnSubDivName.getValue().toString());
                cst.setString(4, btnSubDivShtDesc.getValue().toString());
                cst.setBigDecimal(5,
                                  (BigDecimal)session.getAttribute("divisionCode"));
                cst.registerOutParameter(6,
                                         oracle.jdbc.internal.OracleTypes.VARCHAR);
                cst.execute();

                cst.close();
                conn.commit();
                conn.close();

                /*if(cst.getString(6)!=null){
                GlobalCC.errorValueNotEntered(cst.getString(6));
                return null;
            }*/

                ADFUtils.findIterator("findSubDivisionsIterator").executeQuery();
                GlobalCC.refreshUI(tblDivSubDivisions);

                GlobalCC.INFORMATIONREPORTING("Record Processed Successfully!");


            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }
        return null;
    }

    public String updateDivSubDivision() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        try {
            String query =
                "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_SUBDIVISIONS(?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, "E");
            cst.setBigDecimal(2,
                              new BigDecimal((String)btnSubDivCode.getValue().toString()));
            cst.setString(3, btnSubDivName.getValue().toString());
            cst.setString(4, btnSubDivShtDesc.getValue().toString());
            cst.setBigDecimal(5,
                              (BigDecimal)session.getAttribute("divisionCode"));
            cst.registerOutParameter(6,
                                     oracle.jdbc.internal.OracleTypes.VARCHAR);
            cst.execute();

            cst.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("findSubDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(tblDivSubDivisions);

            GlobalCC.INFORMATIONREPORTING("Record Processed Successfully!");


        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setBtnSubDivCode(RichInputText btnSubDivCode) {
        this.btnSubDivCode = btnSubDivCode;
    }

    public RichInputText getBtnSubDivCode() {
        return btnSubDivCode;
    }

    public void setBtnSubDivShtDesc(RichInputText btnSubDivShtDesc) {
        this.btnSubDivShtDesc = btnSubDivShtDesc;
    }

    public RichInputText getBtnSubDivShtDesc() {
        return btnSubDivShtDesc;
    }

    public void setBtnSubDivName(RichInputText btnSubDivName) {
        this.btnSubDivName = btnSubDivName;
    }

    public RichInputText getBtnSubDivName() {
        return btnSubDivName;
    }

    public void refreshSubdivisionFields() {
        btnSubDivCode.setValue(null);
        btnSubDivShtDesc.setValue(null);
        btnSubDivName.setValue(null);
    }

    public String actionNewDivSubDivision() {
        refreshSubdivisionFields();
        btnSaveDivSubDivision.setText("Save");

        // Open the popup dialog
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:divSubDivisionPop" + "').show(hints);");
        return null;
    }

    public String actionEditDivsubDivision() {
        Object key2 = tblDivSubDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            btnSubDivCode.setValue(nodeBinding.getAttribute("SDIV_CODE"));
            btnSubDivShtDesc.setValue(nodeBinding.getAttribute("SDIV_SHT_DESC"));
            btnSubDivName.setValue(nodeBinding.getAttribute("SDIV_NAME"));
            btnSaveDivSubDivision.setText("Edit");

            // Open the popup dialog for Services
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:divSubDivisionPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public String actionDeleteDivSubDivision() {
        Object key2 = tblDivSubDivisions.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            conn = dbCon.getDatabaseConnection();

            try {
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_SUBDIVISIONS(?,?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  (BigDecimal)nodeBinding.getAttribute("SDIV_CODE"));
                cst.setString(3,
                              (String)nodeBinding.getAttribute("SDIV_NAME"));
                cst.setString(4,
                              (String)nodeBinding.getAttribute("SDIV_SHT_DESC"));
                cst.setBigDecimal(5,
                                  (BigDecimal)session.getAttribute("divisionCode"));
                cst.registerOutParameter(6,
                                         oracle.jdbc.internal.OracleTypes.VARCHAR);
                cst.execute();

                if (cst.getString(6) != null) {
                    GlobalCC.errorValueNotEntered(cst.getString(6));
                    return null;
                }
                cst.close();
                conn.close();
                ADFUtils.findIterator("findSubDivisionsIterator").executeQuery();
                GlobalCC.refreshUI(tblDivSubDivisions);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
            return null;
        }
        return null;
    }

    public void setBtnSaveDivSubDivision(RichCommandButton btnSaveDivSubDivision) {
        this.btnSaveDivSubDivision = btnSaveDivSubDivision;
    }

    public RichCommandButton getBtnSaveDivSubDivision() {
        return btnSaveDivSubDivision;
    }

    public String processAddBranchDivision() {
        boolean processStatusOK = true;

        if (txtSelectedBranchDivisionCode.getValue().equals(null) ||
            txtSelectedBranchDivisionCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a Division first.");
        }

        if (processStatusOK) {
            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            conn = dbCon.getDatabaseConnection();

            try {
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_BRNCH_DIVISIONS(?,?,?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "A");
                cst.setBigDecimal(2, null);
                cst.setBigDecimal(3,
                                  (BigDecimal)session.getAttribute("BRNCode"));
                cst.setBigDecimal(4,
                                  new BigDecimal(txtSelectedBranchDivisionCode.getValue().toString()));
                cst.setDate(5, new java.sql.Date(new Date().getTime()));
                cst.setDate(6, new java.sql.Date(new Date().getTime()));
                cst.registerOutParameter(7,
                                         oracle.jdbc.internal.OracleTypes.VARCHAR);
                cst.execute();

                if (cst.getString(7) != null) {
                    GlobalCC.errorValueNotEntered(cst.getString(7));
                    return null;
                }


                cst.close();
                conn.commit();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        }

        txtSelectedBranchDivisionCode.setValue(null);


        ADFUtils.findIterator("findBranchUnassignedDivisionsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedBranchDivs);
        ADFUtils.findIterator("fetchDivisionsByBranchIterator").executeQuery();
        GlobalCC.refreshUI(tblBranchDivs);
        return null;
    }

    public String processRemoveBranchDivision() {
        boolean processStatusOK = true;

        if (txtSelectedBranchDivisionCode.getValue().equals(null) ||
            txtSelectedBranchDivisionCode.getValue() == "") {
            processStatusOK = false;
            GlobalCC.errorValueNotEntered("You need to select a Division first.");
        }

        if (processStatusOK) {
            DBConnector dbCon = new DBConnector();
            OracleConnection conn = null;
            conn = dbCon.getDatabaseConnection();

            try {
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_BRNCH_DIVISIONS(?,?,?,?,?,?,?); end;";
                OracleCallableStatement cst = null;
                cst = (OracleCallableStatement)conn.prepareCall(query);
                cst.setString(1, "D");
                cst.setBigDecimal(2,
                                  new BigDecimal(txtSelectedBranchBDIVCode.getValue().toString()));
                cst.setBigDecimal(3,
                                  (BigDecimal)session.getAttribute("BRNCode"));
                cst.setBigDecimal(4,
                                  new BigDecimal(txtSelectedBranchDivisionCode.getValue().toString()));
                cst.setDate(5, new java.sql.Date(new Date().getTime()));
                cst.setDate(6, new java.sql.Date(new Date().getTime()));
                cst.registerOutParameter(7,
                                         oracle.jdbc.internal.OracleTypes.VARCHAR);
                cst.execute();

                if (cst.getString(7) != null) {
                    GlobalCC.errorValueNotEntered(cst.getString(7));
                    return null;
                }


                cst.close();
                conn.commit();
                conn.close();

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        }

        txtSelectedBranchDivisionCode.setValue(null);


        ADFUtils.findIterator("findBranchUnassignedDivisionsIterator").executeQuery();
        GlobalCC.refreshUI(treeUnassignedBranchDivs);
        ADFUtils.findIterator("fetchDivisionsByBranchIterator").executeQuery();
        GlobalCC.refreshUI(tblBranchDivs);
        return null;
    }

    public void setPanelDetailDivisions(RichPanelBox panelDetailDivisions) {
        this.panelDetailDivisions = panelDetailDivisions;
    }

    public RichPanelBox getPanelDetailDivisions() {
        return panelDetailDivisions;
    }

    public void setTxtSelectedBranchDivisionCode(RichInputText txtSelectedBranchDivisionCode) {
        this.txtSelectedBranchDivisionCode = txtSelectedBranchDivisionCode;
    }

    public RichInputText getTxtSelectedBranchDivisionCode() {
        return txtSelectedBranchDivisionCode;
    }

    public void setTreeUnassignedBranchDivs(RichTree treeUnassignedBranchDivs) {
        this.treeUnassignedBranchDivs = treeUnassignedBranchDivs;
    }

    public RichTree getTreeUnassignedBranchDivs() {
        return treeUnassignedBranchDivs;
    }

    public void setTblBranchDivs(RichTable tblBranchDivs) {
        this.tblBranchDivs = tblBranchDivs;
    }

    public RichTable getTblBranchDivs() {
        return tblBranchDivs;
    }

    public void treeUnassignedBranchDivsListener(SelectionEvent selectionEvent) {
        if (selectionEvent.getAddedSet() != selectionEvent.getRemovedSet()) {
            RowKeySet keys = selectionEvent.getAddedSet();

            if (keys != null && keys.getSize() > 0) {
                for (Object treeRowKey : keys) {
                    treeUnassignedBranchDivs.setRowKey(treeRowKey);
                    JUCtrlHierNodeBinding nd =
                        (JUCtrlHierNodeBinding)treeUnassignedBranchDivs.getRowData();

                    txtSelectedBranchDivisionCode.setValue(nd.getRow().getAttribute("DIV_CODE"));
                }
            }
        }
        GlobalCC.refreshUI(panelDetailDivisions);
    }

    public void tblBranchDivsListener(SelectionEvent selectionEvent) {
        Object key2 = tblBranchDivs.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            System.out.println("Setting the DIV CODE : " +
                               new BigDecimal(nodeBinding.getAttribute("DIV_CODE").toString()));
            txtSelectedBranchDivisionCode.setValue(new BigDecimal(nodeBinding.getAttribute("DIV_CODE").toString()));
            txtSelectedBranchBDIVCode.setValue(new BigDecimal(nodeBinding.getAttribute("BDIV_CODE").toString()));

            GlobalCC.refreshUI(panelDetailDivisions);
        }
    }

    public void setTxtSelectedBranchBDIVCode(RichInputText txtSelectedBranchBDIVCode) {
        this.txtSelectedBranchBDIVCode = txtSelectedBranchBDIVCode;
    }

    public RichInputText getTxtSelectedBranchBDIVCode() {
        return txtSelectedBranchBDIVCode;
    }

    public void setBdCodePop(RichInputText bdCodePop) {
        this.bdCodePop = bdCodePop;
    }

    public RichInputText getBdCodePop() {
        return bdCodePop;
    }

    public void setBdBranchCodePop(RichInputText bdBranchCodePop) {
        this.bdBranchCodePop = bdBranchCodePop;
    }

    public RichInputText getBdBranchCodePop() {
        return bdBranchCodePop;
    }

    public void setBdDivisionCodePop(RichInputText bdDivisionCodePop) {
        this.bdDivisionCodePop = bdDivisionCodePop;
    }

    public RichInputText getBdDivisionCodePop() {
        return bdDivisionCodePop;
    }

    public void setBdWEFPop(RichInputDate bdWEFPop) {
        this.bdWEFPop = bdWEFPop;
    }

    public RichInputDate getBdWEFPop() {
        return bdWEFPop;
    }

    public void setBdWETPop(RichInputDate bdWETPop) {
        this.bdWETPop = bdWETPop;
    }

    public RichInputDate getBdWETPop() {
        return bdWETPop;
    }

    public void setBtnSaveBranchDivisionPop(RichCommandButton btnSaveBranchDivisionPop) {
        this.btnSaveBranchDivisionPop = btnSaveBranchDivisionPop;
    }

    public RichCommandButton getBtnSaveBranchDivisionPop() {
        return btnSaveBranchDivisionPop;
    }

    public String actionSaveBranchDivisionPop() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        try {
            String query =
                "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_BRNCH_DIVISIONS(?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, "E");
            cst.setBigDecimal(2,
                              new BigDecimal(bdCodePop.getValue().toString()));
            cst.setBigDecimal(3,
                              new BigDecimal(bdBranchCodePop.getValue().toString()));
            cst.setBigDecimal(4,
                              new BigDecimal(bdDivisionCodePop.getValue().toString()));
            if (bdWEFPop.getValue() == null) {
                cst.setString(5, null);
            } else {
                String todayString = null;
                if (bdWEFPop.getValue().toString().contains(":")) {

                    todayString =
                            GlobalCC.parseDate(bdWEFPop.getValue().toString());
                } else {
                    todayString =
                            GlobalCC.upDateParseDate(bdWEFPop.getValue().toString());
                }
                cst.setString(5, todayString);
            }
            if (bdWETPop.getValue() == null) {
                cst.setString(6, null);
            } else {
                String todayString = null;
                if (bdWETPop.getValue().toString().contains(":")) {
                    todayString =
                            GlobalCC.parseDate(bdWETPop.getValue().toString());
                } else {
                    todayString =
                            GlobalCC.upDateParseDate(bdWETPop.getValue().toString());
                }
                cst.setString(6, todayString);
            }
            cst.registerOutParameter(7, OracleTypes.VARCHAR);
            cst.execute();

            if (cst.getString(7) != null) {
                GlobalCC.errorValueNotEntered(cst.getString(7));
                return null;
            }
            cst.close();
            conn.close();
            ADFUtils.findIterator("fetchDivisionsByBranchIterator").executeQuery();
            GlobalCC.refreshUI(tblBranchDivs);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public String actionEditBranchDivision() {
        Object key2 = tblBranchDivs.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            bdCodePop.setValue(nodeBinding.getAttribute("BDIV_CODE"));
            bdBranchCodePop.setValue(session.getAttribute("BRNCode").toString());
            bdDivisionCodePop.setValue(nodeBinding.getAttribute("DIV_CODE"));
            bdWEFPop.setValue(nodeBinding.getAttribute("BDIV_WEF"));
            bdWETPop.setValue(nodeBinding.getAttribute("BDIV_WET"));
            btnSaveBranchDivisionPop.setText("Edit");

            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:branchDivisionPop" + "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected.");
        }
        return null;
    }

    public void setTxtBnsName(RichInputText txtBnsName) {
        this.txtBnsName = txtBnsName;
    }

    public RichInputText getTxtBnsName() {
        return txtBnsName;
    }

    public void setTblStatesLOV(RichTable tblStatesLOV) {
        this.tblStatesLOV = tblStatesLOV;
    }

    public RichTable getTblStatesLOV() {
        return tblStatesLOV;
    }

    public String actionAcceptState() {
        RowKeySet rowKeySet = tblStatesLOV.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        tblStatesLOV.setRowKey(key2);

        JUCtrlValueBinding r = (JUCtrlValueBinding)tblStatesLOV.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        txtStateCode.setValue(r.getAttribute("stateCode"));
        txtStateName.setValue(r.getAttribute("stateName"));

        session.setAttribute("stateCode", r.getAttribute("stateCode"));
        GlobalCC.dismissPopUp("fms", "statesLovPop");

        //AdfFacesContext.getCurrentInstance().adfFacesContext.addPartialTarget(txtStateCode);
        //AdfFacesContext.getCurrentInstance().adfFacesContext.addPartialTarget(txtStateName);

        //ADFUtils.findIterator("findTownsIterator").executeQuery();
        //GlobalCC.refreshUI(townsLovTab);
        return null;

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

    public void setMainTabPanel(RichPanelTabbed mainTabPanel) {
        this.mainTabPanel = mainTabPanel;
    }

    public RichPanelTabbed getMainTabPanel() {
        return mainTabPanel;
    }

    public void setMainOrgPanelBox(RichPanelBox mainOrgPanelBox) {
        this.mainOrgPanelBox = mainOrgPanelBox;
    }

    public RichPanelBox getMainOrgPanelBox() {
        return mainOrgPanelBox;
    }

    public void setSdBranchTab(RichShowDetailItem sdBranchTab) {
        this.sdBranchTab = sdBranchTab;
    }

    public RichShowDetailItem getSdBranchTab() {
        return sdBranchTab;
    }

    public void setSdAgenciesTab(RichShowDetailItem sdAgenciesTab) {
        this.sdAgenciesTab = sdAgenciesTab;
    }

    public RichShowDetailItem getSdAgenciesTab() {
        return sdAgenciesTab;
    }

    public void setSdUnitsTab(RichShowDetailItem sdUnitsTab) {
        this.sdUnitsTab = sdUnitsTab;
    }

    public RichShowDetailItem getSdUnitsTab() {
        return sdUnitsTab;
    }

    public void setOrgLogoImg(RichImage orgLogoImg) {
        this.orgLogoImg = orgLogoImg;
    }

    public RichImage getOrgLogoImg() {
        return orgLogoImg;
    }


    private UploadedFile uploadedfile;
    private String filename;
    private long filesize;
    private String fileContent;
    private String filetype;
    private static InputStream fileStream;
    private static InputStream autoSignStream;

    public void setUploadedfile(UploadedFile uploadedfile) {
        fileStream = null;
        if (uploadedfile != null) {
            this.filename = uploadedfile.getFilename();
            this.filesize = uploadedfile.getLength();
            this.fileContent = uploadedfile.getContentType();
            try {
                fileStream = uploadedfile.getInputStream();
            } catch (IOException e) {
                System.out.println("Error while getting streams.");
            }

            this.uploadedfile = uploadedfile;
        }
    }

    public void fileUploadedListener(ValueChangeEvent valueChangeEvent) {
        //UploadedFile myfile = (UploadedFile)this.getFile();

        RichInputFile inputFileComponent =
            (RichInputFile)valueChangeEvent.getComponent();
        UploadedFile newFile = (UploadedFile)valueChangeEvent.getNewValue();

        try {
            fileStream = newFile.getInputStream();
        } catch (IOException e) {
            System.out.println("Error while getting streams.");
        }

        /*if (newFile.getFilename().endsWith("gif")) {

      FacesContext.getCurrentInstance().addMessage(
          inputFileComponent.getClientId(FacesContext.getCurrentInstance()) ,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "GIF files are not allowed",
                           "This file ("+ newFile.getFilename()+") is not allowed; the extension gif is not allowed."));
      inputFileComponent.resetValue();
      inputFileComponent.setValid(false);
    }*/
    }


    private BlobDomain createBlobDomain(UploadedFile file) {

        InputStream in = null;
        BlobDomain blobDomain = null;
        OutputStream out = null;

        try {
            in = file.getInputStream();

            blobDomain = new BlobDomain();
            out = blobDomain.getBinaryOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead = 0;

            while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        return blobDomain;
    }

    /*private UploadedFile _file;

  public UploadedFile getFile() {
      return _file;
  }

  public void setFile(UploadedFile file) {
      _file = file;
  }*/

    /*public void fileUploadedListener( ValueChangeEvent valueChangeEvent ) {
	  //UploadedFile myfile = (UploadedFile)this.getFile();

	  RichInputFile inputFileComponent = (RichInputFile)valueChangeEvent.getComponent();
    UploadedFile newFile = (UploadedFile)valueChangeEvent.getNewValue();

	  /*if (newFile.getFilename().endsWith("gif")) {

      FacesContext.getCurrentInstance().addMessage(
          inputFileComponent.getClientId(FacesContext.getCurrentInstance()) ,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "GIF files are not allowed",
                           "This file ("+ newFile.getFilename()+") is not allowed; the extension gif is not allowed."));
      inputFileComponent.resetValue();
      inputFileComponent.setValid(false);
    }*/


    /*orgLogoImg.setSource( createBlobDomain(newFile).toString() );
    System.out.println("Uploaded string : -" + orgLogoImg.getSource() + "-");

    // Refresh screen
	  GlobalCC.refreshUI(orgDetails);
	  GlobalCC.refreshUI(mainOrgPanelBox);
	}*/

    /*private BlobDomain createBlobDomain(UploadedFile file) {
System.out.println("Inside creating blob image ...");
      InputStream in = null;
      BlobDomain blobDomain = null;
      OutputStream out = null;

      try {
          if ( file != null){
          in = file.getInputStream();

          blobDomain = new BlobDomain();
          out = blobDomain.getBinaryOutputStream();
          byte[] buffer = new byte[8192];
          int bytesRead = 0;

          while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
              out.write(buffer, 0, bytesRead);
          }
            in.close();

          } else {
            System.out.println("File is null :-(");
          }
      } catch (IOException e) {
          e.printStackTrace();
      } catch (SQLException e) {
          e.fillInStackTrace();
      }

      System.out.println("Return blob domain ... -" + blobDomain.toString() + "-");

      return blobDomain;
  }*/

    public void uploadedfileListener(ValueChangeEvent valueChangeEvent) {

        try {
            UploadedFile file = (UploadedFile)valueChangeEvent.getNewValue();
            if (file != null) {
                orgLogoImg.setSource("/orgimageservlet?id=" + 2);
                file.getFilename();
                System.out.println(file.getFilename());
                setUploadedfile(file);

                System.out.println("Inside the file upload listener ...");


                GlobalCC.refreshUI(orgDetails);

                mainTabPanel.setVisible(true);
                GlobalCC.refreshUI(mainOrgPanelBox);


                /*FacesContext context = FacesContext.getCurrentInstance();
      FacesMessage message = new FacesMessage(
         "Successfully uploaded file " + file.getFilename() +
         " (" + file.getLength() + " bytes)");
      context.addMessage(event.getComponent().getClientId(context), message);*/
                // Here's where we could call file.getInputStream()
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void actionConfirmDeleteRegionListener(DialogEvent dialogEvent) {
        
        
      
            
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            RowKeySet rowKeySet = regionsTab.getSelectedRowKeys();
            if (!rowKeySet.iterator().hasNext()) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");

            }

            Object key2 = rowKeySet.iterator().next();
            regionsTab.setRowKey(key2);

            JUCtrlValueBinding r = (JUCtrlValueBinding)regionsTab.getRowData();

            if (r == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");

            }
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn;
            conn = dbConnector.getDatabaseConnection();
            try {
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.DELETE_REGION(?,?); end;";

                OracleCallableStatement callStmt = null;
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                //bind the variables
                callStmt.setBigDecimal(1,
                                       (BigDecimal)r.getAttribute("regCode"));
                callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
                callStmt.execute();

                String msg = callStmt.getString(2);
                if (msg != null) {
                    msg = msg + " ::First Delete The Child Records   ";
                } else {
                    msg = "Record DELETED Successfully.";
                }
                callStmt.close();
                conn.close();

                ADFUtils.findIterator("findRegionsIterator").executeQuery();
                GlobalCC.refreshUI(regionsTab);
                GlobalCC.INFORMATIONREPORTING(msg);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }

        }

    }

    public void actionConfirmDeleteBankRegionListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            RowKeySet rowKeySet = bankRegionsTab.getSelectedRowKeys();
            if (!rowKeySet.iterator().hasNext()) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");

            }

            Object key2 = rowKeySet.iterator().next();
            bankRegionsTab.setRowKey(key2);

            JUCtrlValueBinding r =
                (JUCtrlValueBinding)bankRegionsTab.getRowData();

            if (r == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");
            }
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn = null;
            OracleCallableStatement callStmt = null;

            try {
                conn = dbConnector.getDatabaseConnection();

                //modify the query for treaty groups procedure
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.bank_region_prc(?,?,?,?,?,?,?,?,?,?); end;";

                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                //bind the variables

                callStmt.setString(1, "D");
                callStmt.setObject(2, (BigDecimal)r.getAttribute("bnkrCode"));
                callStmt.setObject(3, null);
                callStmt.setString(4, null);
                callStmt.setString(5, null);
                callStmt.setString(6, null);
                callStmt.setString(7, null);
                callStmt.setObject(8, null);
                callStmt.setObject(9, null);
                callStmt.registerOutParameter(10, OracleTypes.VARCHAR);


                callStmt.execute();
                String msg = null;
                msg = callStmt.getString(10);

                callStmt.close();
                conn.commit();
                conn.close();

                ADFUtils.findIterator("findBankRegionsIterator").executeQuery();
                GlobalCC.refreshUI(bankRegionsTab);
                GlobalCC.INFORMATIONREPORTING("Record DELETED successfully!");

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            } finally {
                DbUtils.closeQuietly(conn, callStmt, null);

            }

        }

    }

    public void actionConfirmDeleteUnitsListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            RowKeySet rowKeySet = unitsTab.getSelectedRowKeys();
            if (!rowKeySet.iterator().hasNext()) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");

            }

            Object key2 = rowKeySet.iterator().next();
            unitsTab.setRowKey(key2);

            JUCtrlValueBinding r = (JUCtrlValueBinding)unitsTab.getRowData();

            if (r == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");

            }
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn;
            conn = dbConnector.getDatabaseConnection();
            try {
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.DELETE_BRANCH_UNIT(?,?); end;";

                OracleCallableStatement callStmt = null;
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                //bind the variables
                callStmt.setBigDecimal(1,
                                       (BigDecimal)r.getAttribute("bruCode"));
                callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
                callStmt.execute();


                String msg = callStmt.getString(2);
                if (msg != null) {
                    msg = msg + " ::First Delete The Child Records   ";
                } else {
                    msg = "Record DELETED Successfully.";
                }
                callStmt.close();
                conn.commit();
                conn.close();
                session.setAttribute("agnCode", bruAgnCode.getValue());
                ADFUtils.findIterator("findBranchUnitsIterator").executeQuery();
                GlobalCC.refreshUI(unitsTab);

                GlobalCC.INFORMATIONREPORTING(msg);
                btnDeleteBranchUnit.setDisabled(true);

            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        }

    }

    public void actionConfirmDeleteOrgListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn;
            conn = dbConnector.getDatabaseConnection();
            try {
                String query = null;
                query = "DELETE FROM tqc_organizations WHERE org_code = ? ";
                OracleCallableStatement callStmt = null;
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                //bind the variables
                String option = null;
                callStmt.setObject(1, session.getAttribute("ORGCode"));
                callStmt.execute();
                String errMessage = null;
                // errMessage = callStmt.getString(30);

                callStmt.close();
                conn.commit();
                conn.close();

                if (errMessage != null) {

                    GlobalCC.INFORMATIONREPORTING(errMessage);

                } else {
                    orgCode.setValue(null);
                    orgShortDesc.setValue(null);
                    orgName.setValue(null);
                    orgAddrs.setValue(null);
                    orgTwnCode.setValue(null);
                    orgCouCode.setValue(null);
                    orgEmail.setValue(null);
                    orgPhyAddrs.setValue(null);
                    orgCurCode.setValue(null);
                    orgZip.setValue(null);
                    orgFax.setValue(null);
                    orgTel1.setValue(null);
                    orgTel2.setValue(null);
                    orgRptLogo.setValue(null);
                    orgMotto.setValue(null);
                    orgPinNo.setValue(null);
                    orgEdCode.setValue(null);
                    orgItemAccCode.setValue(null);
                    orgOtherName.setValue(null);
                    orgType.setValue(null);
                    orgWebBrnCode.setValue(null);
                    orgWebAddrs.setValue(null);
                    orgAgnCode.setValue(null);
                    orgDirectors.setValue(null);
                    orgLangCode.setValue(null);
                    orgAvatar.setValue(null);

                    orgBaseCurrency.setValue(null);
                    orgCountry.setValue(null);
                    orgTown.setValue(null);

                    txtStateCode.setValue(null);
                    txtStateName.setValue(null);
                    GlobalCC.INFORMATIONREPORTING("Record Deleted  Successfuly");
                }
                GlobalCC.refreshUI(mainOrgPanelBox);
                inputFileOrgImgFile.setValue(null);
                GlobalCC.refreshUI(inputFileOrgImgFile);
                uploadOrgGrpImg.setValue(null);

                orgGrpLogoImg.setSource(null);
                // Org logo orgLogoImg
                orgLogoImg.setSource(null);
                mainTabPanel.setVisible(false);
                GlobalCC.refreshUI(mainTabPanel);
                GlobalCC.refreshUI(orgGrpLogoImg);
                GlobalCC.refreshUI(orgLogoImg);
                GlobalCC.refreshUI(uploadOrgGrpImg);
                ADFUtils.findIterator("findOrganizationIterator").executeQuery();
                GlobalCC.refreshUI(orgTree);


            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
                e.printStackTrace();
            }
        } // Add event code here...
    }

    public void actionConfirmDeleteBranchesListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            RowKeySet rowKeySet = branchesTab.getSelectedRowKeys();
            if (!rowKeySet.iterator().hasNext()) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");

            }

            Object key2 = rowKeySet.iterator().next();
            branchesTab.setRowKey(key2);

            JUCtrlValueBinding r =
                (JUCtrlValueBinding)branchesTab.getRowData();

            if (r == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");

            }
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn;
            conn = dbConnector.getDatabaseConnection();
            try {
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.DELETE_BRANCH(?,?); end;";

                OracleCallableStatement callStmt = null;
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                //bind the variables
                callStmt.setBigDecimal(1,
                                       (BigDecimal)r.getAttribute("brnCode"));
                callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
                callStmt.execute();


                String msg = callStmt.getString(2);
                if (msg != null) {
                    msg = msg + " ::First Delete The Child Records   ";
                } else {
                    msg = "Record DELETED Successfully.";
                }
                callStmt.close();
                conn.close();
                ADFUtils.findIterator("findBranchesIterator").executeQuery();
                GlobalCC.refreshUI(branchesTab);

                GlobalCC.INFORMATIONREPORTING(msg);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }


        }

    }

    public void actionConfirmDeleteBranchAgencyListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            RowKeySet rowKeySet = agenciesTab.getSelectedRowKeys();
            if (!rowKeySet.iterator().hasNext()) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");

            }

            Object key2 = rowKeySet.iterator().next();
            agenciesTab.setRowKey(key2);

            JUCtrlValueBinding r =
                (JUCtrlValueBinding)agenciesTab.getRowData();

            if (r == null) {
                GlobalCC.errorValueNotEntered("Error: No Record Selected");

            }
            DBConnector dbConnector = new DBConnector();
            OracleConnection conn;
            conn = dbConnector.getDatabaseConnection();
            try {
                String query =
                    "begin TQC_WEB_ORGANIZATION_PKG.DELETE_BRANCH_AGENCY(?,?); end;";

                OracleCallableStatement callStmt = null;
                callStmt = (OracleCallableStatement)conn.prepareCall(query);
                //bind the variables
                callStmt.setBigDecimal(1,
                                       (BigDecimal)r.getAttribute("braCode"));
                callStmt.registerOutParameter(2, OracleTypes.VARCHAR);
                callStmt.execute();


                String msg = callStmt.getString(2);
                if (msg != null) {
                    msg = msg + " ::First Delete The Child Records   ";
                } else {
                    msg = "Record DELETED Successfully.";
                }
                callStmt.close();
                conn.commit();
                conn.close();
                ADFUtils.findIterator("findBranchAgenciesIterator").executeQuery();
                GlobalCC.refreshUI(agenciesTab);

                GlobalCC.INFORMATIONREPORTING(msg);


            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }


        }

    }


    public void uploadedOrgGrpListener(ValueChangeEvent valueChangeEvent) {
        try {
            UploadedFile file = (UploadedFile)valueChangeEvent.getNewValue();
            if (file != null) {
                orgGrpLogoImg.setSource("/orgimageservlet?id=" +
                                        createBlobDomain(file));
                file.getFilename();
                System.out.println(file.getFilename());
                setMyFile(file);
                System.out.println("Inside the org GRP file upload listener ...");

                BlobDomain domainFile = null;
                domainFile = FileUpload.writeToBlobDomain(file);
                setDomainFile(domainFile);

                GlobalCC.refreshUI(orgDetails);

                mainTabPanel.setVisible(true);
                GlobalCC.refreshUI(mainOrgPanelBox);
                /*FacesContext context = FacesContext.getCurrentInstance();
      FacesMessage message = new FacesMessage(
         "Successfully uploaded file " + file.getFilename() +
         " (" + file.getLength() + " bytes)");
      context.addMessage(event.getComponent().getClientId(context), message);*/
                // Here's where we could call file.getInputStream()
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String actionDeleteOrg() {

        if (session.getAttribute("ORGCode") == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:confirmDeleteOrg" + "').show(hints);");

        return null;

    }

    public void setMyFile(UploadedFile myFile) {
        this.myFile = myFile;
    }

    public UploadedFile getMyFile() {
        return myFile;
    }

    public void setOrgGrpLogoImg(RichImage orgGrpLogoImg) {
        this.orgGrpLogoImg = orgGrpLogoImg;
    }

    public RichImage getOrgGrpLogoImg() {
        return orgGrpLogoImg;
    }

    public void setDomainFile(BlobDomain domainFile) {
        this.domainFile = domainFile;
    }

    public BlobDomain getDomainFile() {
        return domainFile;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public void setOrgGrpImageFile(BlobDomain orgGrpImageFile) {
        this.orgGrpImageFile = orgGrpImageFile;
    }

    public BlobDomain getOrgGrpImageFile() {
        return orgGrpImageFile;
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
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            if (_file.getContentType().equalsIgnoreCase("image/jpeg") ||
                _file.getContentType().equalsIgnoreCase("image/gif") ||
                _file.getContentType().equalsIgnoreCase("image/png")) {

                InputStream Reader;
                this.UploadedImageFile = _file;
                try {
                    long Val = UploadedImageFile.getLength();
                    Reader = UploadedImageFile.getInputStream();
                    byte[] ImageBytes = new byte[Reader.available()];
                    int BytesLength = ImageBytes.length;
                    String xy = String.valueOf(Val);
                    System.out.println("Image size *" + xy);
                    InsertClientImage(Reader, Val);
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                }
            } else { //GlobalCC.refreshUI(inputFileOrgImgFile);
                GlobalCC.INFORMATIONREPORTING("ERROR:File Not uploaded :: Ensure the file  type is  .jpg/.gif/.png ");

            }

        }
    }

    public static Dimension getJPEGDimension(InputStream fis) throws IOException {


        // check for SOI marker
        if (fis.read() != 255 || fis.read() != 216)
            throw new RuntimeException("SOI (Start Of Image) marker 0xff 0xd8 missing");

        Dimension d = null;

        while (fis.read() == 255) {
            int marker = fis.read();
            int len = fis.read() << 8 | fis.read();

            if (marker == 192) {
                fis.skip(1);

                int height = fis.read() << 8 | fis.read();
                int width = fis.read() << 8 | fis.read();

                d = new Dimension(width, height);
                break;
            }

            fis.skip(len - 2);
        }

        fis.close();

        return d;
    }


    public InputStream validateUploadedImg(UploadedFile file, String imgName) {
        // Add event code here...

        if (file != null) {
            System.out.println("contentType       " + file.getContentType());
            if (file.getContentType().equalsIgnoreCase("image/jpeg") ||
                file.getContentType().equalsIgnoreCase("image/gif") ||
                file.getContentType().equalsIgnoreCase("image/png")) {

                InputStream Reader;
                this.UploadedImageFile = file;
                try {
                    long Val = UploadedImageFile.getLength();
                    Reader = UploadedImageFile.getInputStream();
                    byte[] ImageBytes = new byte[Reader.available()];
                    //int BytesLength = ImageBytes.length;

                    Dimension d = getJPEGDimension(Reader);
                    double w = d.getWidth();
                    double h = d.getHeight();

                    System.out.println("width" + d.getWidth() + "height" +
                                       d.getHeight());

                    if (w <= 200.00 && h <= 200.00) {
                        return Reader;
                    } else {
                        GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                                      " cannot be Saved   :: Image size greater than '150 x 100 px' ");
                        inputFileOrgImgFile.setValue(null);
                        GlobalCC.refreshUI(inputFileOrgImgFile);
                        return null;
                    }
                    // InsertGroupImage(Reader, Val);
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                    return null;
                }

            } else {

                //GlobalCC.refreshUI(uploadOrgGrpImg);
                GlobalCC.INFORMATIONREPORTING("ERROR:" + imgName +
                                              " cannot be save  :: Ensure the file  type is  .jpg/.gif/.png ");

            }
        }
        return null;

    }


    public void setClientImage(RichImage clientImage) {
        this.clientImage = clientImage;
    }

    public RichImage getClientImage() {
        return clientImage;
    }

    public void InsertClientImage(InputStream Image, long BytesLength) {
        if (session.getAttribute("ORGCode") != null) {
            OracleConnection conn = null;

            try {

                DBConnector connector = new DBConnector();
                conn = connector.getDatabaseConnection();
                String systemsQuery =
                    "BEGIN TQC_WEB_ORGANIZATION_PKG.update_image(?,?,?);END;";
                CallableStatement cst = null;
                cst = conn.prepareCall(systemsQuery);
                cst.setBlob(1, Image, BytesLength);
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("ORGCode"));
                cst.setBigDecimal(3, new BigDecimal(1));
                cst.execute();

                //ADFUtils.findIterator("findAllQuotationDetailsIterator").executeQuery();
                GlobalCC.refreshUI(inputFileOrgImgFile);
                GlobalCC.refreshUI(orgLogoImg);
                GlobalCC.INFORMATIONREPORTING("Image succesfully Updated ");
                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            orgImg = Image;
            orgImgByteLength = BytesLength;

        }
    }

    public void InsertGroupImage(InputStream Image, long BytesLength) {
        if (session.getAttribute("ORGCode") != null) {
            OracleConnection conn = null;
            try {

                DBConnector connector = new DBConnector();
                conn = connector.getDatabaseConnection();
                String systemsQuery =
                    "BEGIN TQC_WEB_ORGANIZATION_PKG.update_image(?,?,?);END;";
                CallableStatement cst = null;
                cst = conn.prepareCall(systemsQuery);
                cst.setBlob(1, Image, BytesLength);
                cst.setBigDecimal(2,
                                  (BigDecimal)session.getAttribute("ORGCode"));
                cst.setBigDecimal(3, new BigDecimal(2));
                cst.execute();
                orgGrpLogoImg.setSource(null);
                orgGrpLogoImg.setSource("/orggrpimageservlet?id=" +
                                        (BigDecimal)session.getAttribute("orgCode"));
                GlobalCC.refreshUI(orgGrpLogoImg);

                GlobalCC.refreshUI(uploadOrgGrpImg);

                GlobalCC.refreshUI(orgDetails);
                mainTabPanel.setVisible(true);

                GlobalCC.refreshUI(mainOrgPanelBox);
                GlobalCC.INFORMATIONREPORTING("Image succesfully Updated ");


                conn.close();
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(conn, e);
            }
        } else {
            orgGrpImg = Image;
            orgGrpImgByteLength = BytesLength;

        }
    }

    public void setUploadComponent(RichInputFile uploadComponent) {
        this.uploadComponent = uploadComponent;
    }

    public RichInputFile getUploadComponent() {
        return uploadComponent;
    }

    public void setUploadOrgGrpImg(RichInputFile uploadOrgGrpImg) {
        this.uploadOrgGrpImg = uploadOrgGrpImg;
    }

    public RichInputFile getUploadOrgGrpImg() {
        return uploadOrgGrpImg;
    }

    public void setTxtRegAgnSeqNo(RichInputText txtRegAgnSeqNo) {
        this.txtRegAgnSeqNo = txtRegAgnSeqNo;
    }

    public RichInputText getTxtRegAgnSeqNo() {
        return txtRegAgnSeqNo;
    }

    public void setTxtRegBranchMgrSeqNo(RichInputText txtRegBranchMgrSeqNo) {
        this.txtRegBranchMgrSeqNo = txtRegBranchMgrSeqNo;
    }

    public RichInputText getTxtRegBranchMgrSeqNo() {
        return txtRegBranchMgrSeqNo;
    }

    public void setOutputLbManager(RichOutputLabel outputLbManager) {
        this.outputLbManager = outputLbManager;
    }

    public RichOutputLabel getOutputLbManager() {
        return outputLbManager;
    }

    public void setBtnRegManagerpop(RichOutputLabel btnRegManagerpop) {
        this.btnRegManagerpop = btnRegManagerpop;
    }

    public RichOutputLabel getBtnRegManagerpop() {
        return btnRegManagerpop;
    }

    public void setBtnShowManagerLov(RichCommandButton btnShowManagerLov) {
        this.btnShowManagerLov = btnShowManagerLov;
    }

    public RichCommandButton getBtnShowManagerLov() {
        return btnShowManagerLov;
    }

    public void setTxtBrnBranchMgrSeqNo(RichInputText txtBrnBranchMgrSeqNo) {
        this.txtBrnBranchMgrSeqNo = txtBrnBranchMgrSeqNo;
    }

    public RichInputText getTxtBrnBranchMgrSeqNo() {
        return txtBrnBranchMgrSeqNo;
    }

    public void setTxtBrnAgnSeqNo(RichInputText txtBrnAgnSeqNo) {
        this.txtBrnAgnSeqNo = txtBrnAgnSeqNo;
    }

    public RichInputText getTxtBrnAgnSeqNo() {
        return txtBrnAgnSeqNo;
    }

    public UploadedFile getUploadedfile() {
        return uploadedfile;
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


    public static void setFileStream(InputStream fileStream) {
        OrganizationManip.fileStream = fileStream;
    }

    public static InputStream getFileStream() {
        return fileStream;
    }

    public void setOutputLbBrnMgr(RichOutputLabel outputLbBrnMgr) {
        this.outputLbBrnMgr = outputLbBrnMgr;
    }

    public RichOutputLabel getOutputLbBrnMgr() {
        return outputLbBrnMgr;
    }

    public void setBtnShowBranchMgrLov(RichCommandButton btnShowBranchMgrLov) {
        this.btnShowBranchMgrLov = btnShowBranchMgrLov;
    }

    public RichCommandButton getBtnShowBranchMgrLov() {
        return btnShowBranchMgrLov;
    }

    public void setTxtBraBranchMgrSeqNo(RichInputText txtBraBranchMgrSeqNo) {
        this.txtBraBranchMgrSeqNo = txtBraBranchMgrSeqNo;
    }

    public RichInputText getTxtBraBranchMgrSeqNo() {
        return txtBraBranchMgrSeqNo;
    }

    public void setTxtBraAgnSeqNo(RichInputText txtBraAgnSeqNo) {
        this.txtBraAgnSeqNo = txtBraAgnSeqNo;
    }

    public RichInputText getTxtBraAgnSeqNo() {
        return txtBraAgnSeqNo;
    }

    public void setOutlBraMgr(RichOutputLabel outlBraMgr) {
        this.outlBraMgr = outlBraMgr;
    }

    public RichOutputLabel getOutlBraMgr() {
        return outlBraMgr;
    }

    public void setBtnShowBraMgrlov(RichCommandButton btnShowBraMgrlov) {
        this.btnShowBraMgrlov = btnShowBraMgrlov;
    }

    public RichCommandButton getBtnShowBraMgrlov() {
        return btnShowBraMgrlov;
    }

    public void setOutputlbMgr(RichOutputLabel outputlbMgr) {
        this.outputlbMgr = outputlbMgr;
    }

    public RichOutputLabel getOutputlbMgr() {
        return outputlbMgr;
    }

    public void setBtnShowBruManager(RichCommandButton btnShowBruManager) {
        this.btnShowBruManager = btnShowBruManager;
    }

    public RichCommandButton getBtnShowBruManager() {
        return btnShowBruManager;
    }

    public void setTxtBruBranchMgrSeqNo(RichInputNumberSpinbox txtBruBranchMgrSeqNo) {
        this.txtBruBranchMgrSeqNo = txtBruBranchMgrSeqNo;
    }

    public RichInputNumberSpinbox getTxtBruBranchMgrSeqNo() {
        return txtBruBranchMgrSeqNo;
    }

    public void setTxtBruAgnSeqNo(RichInputText txtBruAgnSeqNo) {
        this.txtBruAgnSeqNo = txtBruAgnSeqNo;
    }

    public RichInputText getTxtBruAgnSeqNo() {
        return txtBruAgnSeqNo;
    }

    public void branchAgencySelected(SelectionEvent evt) {

        if (evt.getAddedSet() != evt.getRemovedSet()) {
            RowKeySet rowKeySet = agenciesTab.getSelectedRowKeys();
            Object key2 = rowKeySet.iterator().next();
            agenciesTab.setRowKey(key2);
            JUCtrlValueBinding nodeBinding =
                (JUCtrlValueBinding)agenciesTab.getRowData();

            session.setAttribute("braName",
                                 nodeBinding.getAttribute("braName"));
            session.setAttribute("braCode",
                                 nodeBinding.getAttribute("braCode"));
            session.setAttribute("braAgnCode",
                                 nodeBinding.getAttribute("braAgnCode"));

            btnDeleteBranchAgency.setDisabled(false);
            btnEditBranchAgency.setDisabled(false);
            btnTransferBranchAgency.setDisabled(false);

            GlobalCC.refreshUI(btnDeleteBranchAgency);
            GlobalCC.refreshUI(btnEditBranchAgency);
            GlobalCC.refreshUI(btnTransferBranchAgency);

            ADFUtils.findIterator("findBranchUnitsIterator").executeQuery();
            GlobalCC.refreshUI(unitsTab);


        }
    }

    public void actionTblUnitsTabListener(SelectionEvent selectionEvent) {
        btnDeleteBranchUnit.setDisabled(false);
        btnEditBranchUnit.setDisabled(false);
        GlobalCC.refreshUI(btnDeleteBranchUnit);
        GlobalCC.refreshUI(btnEditBranchUnit);
        RowKeySet rowKeySet = unitsTab.getSelectedRowKeys();
        if (rowKeySet.iterator().hasNext()) {
            Object key2 = rowKeySet.iterator().next();
            unitsTab.setRowKey(key2);
            JUCtrlValueBinding r = (JUCtrlValueBinding)unitsTab.getRowData();
            if (r != null) {
                session.setAttribute("bruCode", r.getAttribute("bruCode"));
                session.setAttribute("bruName", r.getAttribute("bruName"));
            }
            sdAgentsTab.setVisible(true);
            btnTransferUnitAgents.setDisabled(false);
        }
        ADFUtils.findIterator("fetchUnitAgentsIterator").executeQuery();
        GlobalCC.refreshUI(sdAgentsTab);
        GlobalCC.refreshUI(btnTransferUnitAgents);
        GlobalCC.refreshUI(agentsTab);
    }

    public void unitAgentSelected(SelectionEvent selectionEvent) {
        RowKeySet rowKeySet = agentsTab.getSelectedRowKeys();
        if (rowKeySet.iterator().hasNext()) {
            Object key2 = rowKeySet.iterator().next();
            agentsTab.setRowKey(key2);
            JUCtrlValueBinding r = (JUCtrlValueBinding)agentsTab.getRowData();

            if (r != null) {
                session.setAttribute("unaCode", r.getAttribute("unaCode"));
                session.setAttribute("unaName", r.getAttribute("unaName"));
            }
        }
    }

    public void setBtnDeleteBranchUnit(RichCommandButton btnDeleteBranchUnit) {
        this.btnDeleteBranchUnit = btnDeleteBranchUnit;
    }

    public RichCommandButton getBtnDeleteBranchUnit() {
        return btnDeleteBranchUnit;
    }

    public void setBtnEditBranchUnit(RichCommandButton btnEditBranchUnit) {
        this.btnEditBranchUnit = btnEditBranchUnit;
    }

    public RichCommandButton getBtnEditBranchUnit() {
        return btnEditBranchUnit;
    }

    public void setBtnDeleteRegions(RichCommandButton btnDeleteRegions) {
        this.btnDeleteRegions = btnDeleteRegions;
    }

    public RichCommandButton getBtnDeleteRegions() {
        return btnDeleteRegions;
    }

    public void setBtnEditRegions(RichCommandButton btnEditRegions) {
        this.btnEditRegions = btnEditRegions;
    }

    public RichCommandButton getBtnEditRegions() {
        return btnEditRegions;
    }

    public void setBtnDeleteBranch(RichCommandButton btnDeleteBranch) {
        this.btnDeleteBranch = btnDeleteBranch;
    }

    public RichCommandButton getBtnDeleteBranch() {
        return btnDeleteBranch;
    }

    public void setBtnEditBranch(RichCommandButton btnEditBranch) {
        this.btnEditBranch = btnEditBranch;
    }

    public RichCommandButton getBtnEditBranch() {
        return btnEditBranch;
    }

    public void setBtnDeleteBranchAgency(RichCommandButton btnDeleteBranchAgency) {
        this.btnDeleteBranchAgency = btnDeleteBranchAgency;
    }

    public RichCommandButton getBtnDeleteBranchAgency() {
        return btnDeleteBranchAgency;
    }

    public void setBtnEditBranchAgency(RichCommandButton btnEditBranchAgency) {
        this.btnEditBranchAgency = btnEditBranchAgency;
    }

    public RichCommandButton getBtnEditBranchAgency() {
        return btnEditBranchAgency;
    }

    public void setOrgTab(RichShowDetailItem orgTab) {
        this.orgTab = orgTab;
    }

    public RichShowDetailItem getOrgTab() {
        return orgTab;
    }

    public void setRegManup(RichShowDetailItem regManup) {
        this.regManup = regManup;
    }

    public RichShowDetailItem getRegManup() {
        return regManup;
    }

    public void setRegTab(RichShowDetailItem regTab) {
        this.regTab = regTab;
    }

    public RichShowDetailItem getRegTab() {
        return regTab;
    }

    public void setInputFileOrgImgFile(RichInputFile inputFileOrgImgFile) {
        this.inputFileOrgImgFile = inputFileOrgImgFile;
    }

    public RichInputFile getInputFileOrgImgFile() {
        return inputFileOrgImgFile;
    }

    //


    public void setBnsContact(RichInputText bnsContact) {
        this.bnsContact = bnsContact;
    }

    public RichInputText getBnsContact() {
        return bnsContact;
    }

    public void setBnsEmail(RichInputText bnsEmail) {
        this.bnsEmail = bnsEmail;
    }

    public RichInputText getBnsEmail() {
        return bnsEmail;
    }

    public void setBnsFax(RichInputText bnsFax) {
        this.bnsFax = bnsFax;
    }

    public RichInputText getBnsFax() {
        return bnsFax;
    }

    public void setBnsTel(RichInputText bnsTel) {
        this.bnsTel = bnsTel;
    }

    public RichInputText getBnsTel() {
        return bnsTel;
    }

    public void setBnsTownName(RichInputText bnsTownName) {
        this.bnsTownName = bnsTownName;
    }

    public RichInputText getBnsTownName() {
        return bnsTownName;
    }

    public void setBnsTownCode(RichInputText bnsTownCode) {
        this.bnsTownCode = bnsTownCode;
    }

    public RichInputText getBnsTownCode() {
        return bnsTownCode;
    }

    public void setBnsStateName(RichInputText bnsStateName) {
        this.bnsStateName = bnsStateName;
    }

    public RichInputText getBnsStateName() {
        return bnsStateName;
    }

    public void setBnsStateCode(RichInputText bnsStateCode) {
        this.bnsStateCode = bnsStateCode;
    }

    public RichInputText getBnsStateCode() {
        return bnsStateCode;
    }

    public void setBnsCountryName(RichInputText bnsCountryName) {
        this.bnsCountryName = bnsCountryName;
    }

    public RichInputText getBnsCountryName() {
        return bnsCountryName;
    }

    public void setBnsCountryCode(RichInputText bnsCountryCode) {
        this.bnsCountryCode = bnsCountryCode;
    }

    public RichInputText getBnsCountryCode() {
        return bnsCountryCode;
    }

    public void setBnsPostalAddr(RichInputText bnsPostalAddr) {
        this.bnsPostalAddr = bnsPostalAddr;
    }

    public RichInputText getBnsPostalAddr() {
        return bnsPostalAddr;
    }

    public void setBnsPhysicalAddr(RichInputText bnsPhysicalAddr) {
        this.bnsPhysicalAddr = bnsPhysicalAddr;
    }

    public RichInputText getBnsPhysicalAddr() {
        return bnsPhysicalAddr;
    }

    public void setBnsCode(RichInputText bnsCode) {
        this.bnsCode = bnsCode;
    }

    public RichInputText getBnsCode() {
        return bnsCode;
    }

    public String actionAcceptBranchName() {
        Object key2 = tblBranchNameListing.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {

            brnBnsCode.setValue(nodeBinding.getAttribute("bns_code"));
            brnShortDesc.setValue(nodeBinding.getAttribute("bns_sht_desc"));
            brnName.setValue(nodeBinding.getAttribute("bns_name"));
            brnPhyAddrs.setValue(nodeBinding.getAttribute("bns_phy_addrs"));
            brnEmail.setValue(nodeBinding.getAttribute("bns_email_addrs"));
            brnPostAddrs.setValue(nodeBinding.getAttribute("bns_post_addrs"));
            brn_Twn.setValue(nodeBinding.getAttribute("townName"));
            brnPostCode.setValue(nodeBinding.getAttribute("bnsPostalCod"));
            
         

            //brnTownCode.setValue(nodeBinding.getAttribute("bns_twn_code"));

            /// brnCountryCode.setValue(nodeBinding.getAttribute("bns_cou_code"));
            brnContact.setValue(nodeBinding.getAttribute("bns_contact"));
            // bnsManag.setValue(nodeBinding.getAttribute("bns_manager"));
            brnTel.setValue(nodeBinding.getAttribute("bns_tel"));
            brnFax.setValue(nodeBinding.getAttribute("bns_fax"));
            //bnsStateCode.setValue(nodeBinding.getAttribute("state_code"));
            // bnsTownName.setValue(nodeBinding.getAttribute("townName"));
            //bnsCountryName.setValue(nodeBinding.getAttribute("countryName"));
            //bnsStateName.setValue(nodeBinding.getAttribute("stateName"));
            GlobalCC.refreshUI(branchesPopUp);
            ExtendedRenderKitService erkService1 =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService1.addScript(FacesContext.getCurrentInstance(),
                                  "var hints = {autodismissNever:false}; " +
                                  "AdfPage.PAGE.findComponent('" +
                                  "fms:branchesPopUp" + "').show(hints);");
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:showBranchNameLov" + "').hide(hints);");
            return null;
        }
        return null;
    }

    public String actionSaveBranchName() {
        BigDecimal proposalCode = null;
        BigDecimal policycode = null;
        String option = null;
        Rendering renderer = new Rendering();
        String bnCode = GlobalCC.checkNullValues(bnsCode.getValue());

        if (btnSaveBranchName.getText().equalsIgnoreCase("update")) {
            option = "E";
            if (bnCode == null) {
                GlobalCC.checkNullValues("BNS CODE REQUIRED");
                return null;
            }
        }


        else if (btnSaveBranchName.getText().equalsIgnoreCase("Save")) {
            option = "A";
        }
        String bnContact;
        String bnShtdesc = GlobalCC.checkNullValues(bnsShortDesc.getValue());
        String bnName = GlobalCC.checkNullValues(bnsName.getValue());
        String bnPhysical =
            GlobalCC.checkNullValues(bnsPhysicalAddr.getValue());
        String bnPostal = GlobalCC.checkNullValues(bnsPostalAddr.getValue());
        String bnEmail = GlobalCC.checkNullValues(bnsEmail.getValue());
        String bnTwnCode = GlobalCC.checkNullValues(bnsTownCode.getValue());
        String bnCouCode = GlobalCC.checkNullValues(bnsCountryCode.getValue());
        String bnsPostalCodeVal =
            GlobalCC.checkNullValues(bnsPostalCode.getValue());

        try {
            if (bnsContact.getValue() == null ||
                bnsContact.getValue().equals("")) {
                bnContact = null;
            } else {
                bnContact = bnsContact.getValue().toString();
            }
        } catch (Exception e) {
            bnContact = null;
        }
        String bnTel = GlobalCC.checkNullValues(bnsTel.getValue());
        String bnFax = GlobalCC.checkNullValues(bnsFax.getValue());
        String bnStateCode = GlobalCC.checkNullValues(bnsStateCode.getValue());
        Rendering rend = new Rendering();
        String auto = rend.getBranchShtDesc();
        if (bnShtdesc == null && auto.equalsIgnoreCase("N")) {
            GlobalCC.errorValueNotEntered("Short Desc Required:");
            return null;
        } else if (auto.equalsIgnoreCase("Y")) {
            if (GlobalCC.checkNullValues(accType.getValue()) == null) {
                GlobalCC.errorValueNotEntered("Account Type Required:");
                return null;
            }
            if (GlobalCC.checkNullValues(txtRegion.getValue()) == null) {
                GlobalCC.errorValueNotEntered("Region Required:");
                return null;
            }

        }
        if (bnName == null) {
            GlobalCC.errorValueNotEntered("Name Required:");
            return null;
        }


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        try {
            String query = null;
            //modify the query for treaty groups procedure
            query =
                    "BEGIN TQC_WEB_ORGANIZATION_PKG.UPDATE_BRANCH_NAMES(?,?,?,  ?,?,?,  ?,?,?,   ?,?,?,  ?,?,?,?,?,?);END;";


            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, option);
            cst.setBigDecimal(2,
                              bnCode == null ? null : new BigDecimal(bnCode));
            cst.setString(3, bnShtdesc);
            cst.setString(4, bnName);
            cst.setString(5, bnPhysical);
            cst.setString(6, bnPostal);
            cst.setString(7, bnEmail);
            cst.setBigDecimal(8,
                              bnTwnCode == null ? null : new BigDecimal(bnTwnCode));
            cst.setBigDecimal(9,
                              bnCouCode == null ? null : new BigDecimal(bnCouCode));
            cst.setString(10, bnContact);
            cst.setString(11, null);
            cst.setString(12, bnTel);
            cst.setString(13, bnFax);
            cst.setString(14, bnStateCode);
            cst.setString(15, GlobalCC.checkNullValues(accType.getValue()));
            cst.setString(16, GlobalCC.checkNullValues(txtRegion.getValue()));
            cst.setString(17, bnsPostalCodeVal);
            cst.registerOutParameter(18, OracleTypes.VARCHAR);
            cst.execute();
            String errMessage = cst.getString(18);
            if (errMessage != null) {
                GlobalCC.INFORMATIONREPORTING(errMessage);
            } else {
                GlobalCC.INFORMATIONREPORTING("Record Saved Successfully");
                ADFUtils.findIterator("fetchBranchNamesIterator").executeQuery();
                GlobalCC.dismissPopUp("fms", "addBranchName");
                GlobalCC.refreshUI(tblBranchName);
                bnsPostalCode.setValue(null);

                return null;
            }
            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
            return null;

        }


        return null;
    }

    public void setBtnSaveBranchName(RichCommandButton btnSaveBranchName) {
        this.btnSaveBranchName = btnSaveBranchName;
    }

    public RichCommandButton getBtnSaveBranchName() {
        return btnSaveBranchName;
    }


    public String actionNewBranchName() {
        clearBranchName();
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:addBranchName" + "').show(hints);");
        return null;
    }

    public String actionShowTown() {
        if (bnsCountryCode.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Select a country to proceed");
            return null;
        }
        btnSelectTwnfirst.setVisible(false);
        btnSelectTown.setRendered(true);
        ADFUtils.findIterator("findTownsIterator").executeQuery();
        GlobalCC.refreshUI(townsLovTab);
        GlobalCC.showPopUp("fms", "townsLovPopUp");
        return null;
    }

    public String actionAcceptTown() {
        btnSelectTwnfirst.setVisible(true);
        btnSelectTown.setRendered(false);

        Object key2 = townsLovTab.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            System.out.println("tOWN nAME" +
                               nodeBinding.getAttribute("twnCode"));
            bnsTownCode.setValue(nodeBinding.getAttribute("twnCode"));
            bnsTownName.setValue(nodeBinding.getAttribute("twnName"));
            bnsPostalCode.setValue(nodeBinding.getAttribute("twnPostalCode"));
            GlobalCC.refreshUI(bnsTownName);
            GlobalCC.refreshUI(bnsTownCode);
            GlobalCC.refreshUI(bnsPostalCode);


            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:townsLovPopUp" + "').hide(hints);");
        }
        return null;
    }

    public String actionEditBranchName() {
        Object key2 = tblBranchName.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            btnSaveBranchName.setText("Update");
            bnsCode.setValue(nodeBinding.getAttribute("bns_code"));
            bnsShortDesc.setValue(nodeBinding.getAttribute("bns_sht_desc"));
            bnsName.setValue(nodeBinding.getAttribute("bns_name"));
            bnsPhysicalAddr.setValue(nodeBinding.getAttribute("bns_phy_addrs"));
            bnsEmail.setValue(nodeBinding.getAttribute("bns_email_addrs"));
            bnsPostalAddr.setValue(nodeBinding.getAttribute("bns_post_addrs"));
            if (nodeBinding.getAttribute("bnsPostalCod") != null) {
                bnsPostalCode.setValue(nodeBinding.getAttribute("bnsPostalCod"));
            } else {
                bnsPostalCode.setValue(null);
            }
            if (nodeBinding.getAttribute("bns_twn_code") != null) {
                bnsTownCode.setValue(nodeBinding.getAttribute("bns_twn_code"));
            } else {
                bnsTownCode.setValue(null);
            }
            if (nodeBinding.getAttribute("bns_cou_code") != null) {
                bnsCountryCode.setValue(nodeBinding.getAttribute("bns_cou_code"));
            } else {
                bnsCountryCode.setValue(null);
            }
            if (nodeBinding.getAttribute("bns_contact") != null) {
                //bnsContact.setValue(nodeBinding.getAttribute("bns_contact"));
            } else {
                // bnsContact.setValue(null);
            }
            // bnsManag.setValue(nodeBinding.getAttribute("bns_manager"));
            if (nodeBinding.getAttribute("bns_tel") != null) {
                bnsTel.setValue(nodeBinding.getAttribute("bns_tel"));
            } else {
                bnsTel.setValue(null);
            }
            if (nodeBinding.getAttribute("bns_fax") != null) {
                bnsFax.setValue(nodeBinding.getAttribute("bns_fax"));
            } else {
                bnsFax.setValue(null);
            }
            if (nodeBinding.getAttribute("state_code") != null) {
                bnsStateCode.setValue(nodeBinding.getAttribute("state_code"));
            } else {
                bnsStateCode.setValue(null);
            }
            if (nodeBinding.getAttribute("townName") != null) {
                bnsTownName.setValue(nodeBinding.getAttribute("townName"));
            } else {
                bnsTownName.setValue(null);
            }
            if (nodeBinding.getAttribute("countryName") != null) {
                bnsCountryName.setValue(nodeBinding.getAttribute("countryName"));
            } else {
                bnsCountryName.setValue(null);
            }
            if (nodeBinding.getAttribute("stateName") != null) {
                bnsStateName.setValue(nodeBinding.getAttribute("stateName"));
            } else {
                bnsStateName.setValue(null);
            }
            accType.setValue(nodeBinding.getAttribute("accType"));
            txtRegion.setValue(nodeBinding.getAttribute("region"));
            
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:addBranchName" + "').show(hints);");
            
            return null;

        } else {
            GlobalCC.INFORMATIONREPORTING("No Record Selected");
            return null;
        }
    }

    public String actionDeleteBranchName() {
        // Add event code here...
        return null;
    }

    public void clearBranchName() {
        btnSaveBranchName.setText("Save");
        bnsCode.setValue(null);
        bnsShortDesc.setValue(null);
        bnsName.setValue(null);
        bnsPhysicalAddr.setValue(null);
        bnsEmail.setValue(null);
        bnsPostalAddr.setValue(null);

        bnsTownCode.setValue(null);
        accType.setValue(null);
        txtRegion.setValue(null);

        //  bnsCountryCode.setValue(null);
        //  bnsContact.setValue(null);
        // bnsManag.setValue(nodeBinding.getAttribute("bns_manager"));
        bnsTel.setValue(null);
        bnsFax.setValue(null);
        bnsStateCode.setValue(null);
        bnsTownName.setValue(null);
        bnsCountryName.setValue(null);
        bnsStateName.setValue(null);
    }

    public void setTblBranchNames(RichPanelCollection tblBranchNames) {
        this.tblBranchNames = tblBranchNames;
    }

    public RichPanelCollection getTblBranchNames() {
        return tblBranchNames;
    }

    public void setTblBranchName(RichTable tblBranchName) {
        this.tblBranchName = tblBranchName;
    }

    public RichTable getTblBranchName() {
        return tblBranchName;
    }

    public void setBnsName(RichInputText bnsName) {
        this.bnsName = bnsName;
    }

    public RichInputText getBnsName() {
        return bnsName;
    }

    public String actionShowState() {
        if (bnsCountryCode.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Select a country to proceed");
            return null;
        }

        btnStateLov1.setVisible(false);
        btnStateLov2.setRendered(true);
        ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
        GlobalCC.refreshUI(tblStatesLOV);
        GlobalCC.showPopUp("fms", "statesLovPop");
        return null;
    }

    public String actionAcceptState2() {

        btnStateLov1.setVisible(true);
        btnStateLov2.setRendered(false);
        Object key2 = tblStatesLOV.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            bnsStateCode.setValue(nodeBinding.getAttribute("stateCode"));
            bnsStateName.setValue(nodeBinding.getAttribute("stateName"));
            session.setAttribute("stateCode",
                                 nodeBinding.getAttribute("stateCode"));
            GlobalCC.refreshUI(bnsStateName);
            GlobalCC.refreshUI(bnsStateCode);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:statesLovPop" + "').hide(hints);");
        }
        return null;
    }


    public String actionShowCountry() {
        btnSelectCountry1.setVisible(false);
        btnSelectCountry2.setRendered(true);
        System.out.println("Bitton");
        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:countriesLovPopUp" + "').show(hints);");
        System.out.println("Bitton2");
        return null;
    }

    public String actionAcceptCountry2() {

        btnSelectCountry1.setVisible(true);
        btnSelectCountry2.setRendered(false);
        Object key2 = countriesLovTab.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            bnsCountryCode.setValue(nodeBinding.getAttribute("couCode"));
            bnsCountryName.setValue(nodeBinding.getAttribute("couName"));
            session.setAttribute("COUCode",
                                 nodeBinding.getAttribute("couCode"));
            session.setAttribute("countryCode",
                                 nodeBinding.getAttribute("couCode"));
            GlobalCC.refreshUI(bnsCountryName);
            GlobalCC.refreshUI(bnsCountryCode);
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:countriesLovPopUp" + "').hide(hints);");
        }
        bnsStateCode.setValue(null);
        bnsStateName.setValue(null);
        bnsTownName.setValue(null);
        bnsTownCode.setValue(null);
        GlobalCC.refreshUI(bnsStateName);
        GlobalCC.refreshUI(bnsTownName);
        GlobalCC.dismissPopUp("fms", "countriesLovPopUp");
        return null;
    }

    public void setBnsShortDesc(RichInputText bnsShortDesc) {
        this.bnsShortDesc = bnsShortDesc;
    }

    public RichInputText getBnsShortDesc() {
        return bnsShortDesc;
    }

    public void actionConfirmDeleteBranchName(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.no)) {
        } else if (dialogEvent.getOutcome().equals(DialogEvent.Outcome.yes)) {
            Object key2 = tblBranchName.getSelectedRowData();
            JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

            if (nodeBinding != null) {

                String bnCode =
                    GlobalCC.checkNullValues(nodeBinding.getAttribute("bns_code"));
                DBConnector dbConnector = new DBConnector();
                OracleConnection conn;
                conn = dbConnector.getDatabaseConnection();
                try {
                    String query = null;
                    //modify the query for treaty groups procedure
                    query =
                            "BEGIN TQC_WEB_ORGANIZATION_PKG.DELETE_BRANCH_NAME(?,?);END;";

                    OracleCallableStatement cst = null;
                    cst = (OracleCallableStatement)conn.prepareCall(query);
                    cst.setBigDecimal(1,
                                      bnCode == null ? null : new BigDecimal(bnCode));
                    cst.registerOutParameter(2, OracleTypes.VARCHAR);
                    cst.execute();
                    String errMessage = cst.getString(2);
                    if (errMessage != null) {
                        GlobalCC.INFORMATIONREPORTING(errMessage);
                    } else {
                        ADFUtils.findIterator("fetchBranchNamesIterator").executeQuery();
                        GlobalCC.refreshUI(tblBranchName);
                        GlobalCC.INFORMATIONREPORTING("Record Deleted Successfully");
                    }
                    conn.close();
                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(conn, e);

                }
            } else {
                GlobalCC.INFORMATIONREPORTING("NO RECORD SELECTED");
            }
        }
    }

    public String actionShowDeleteBranchName() {
        Object key2 = tblBranchName.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key2;

        if (nodeBinding != null) {
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:confirmDeleteBranchName" +
                                 "').show(hints);");
        } else {
            GlobalCC.INFORMATIONREPORTING("No record Selected");
        }
        return null;
    }

    public void setBtnSelectTown(RichCommandButton btnSelectTown) {
        this.btnSelectTown = btnSelectTown;
    }

    public RichCommandButton getBtnSelectTown() {
        return btnSelectTown;
    }

    public void setBtnSelectTwnfirst(RichCommandButton btnSelectTwnfirst) {
        this.btnSelectTwnfirst = btnSelectTwnfirst;
    }

    public RichCommandButton getBtnSelectTwnfirst() {
        return btnSelectTwnfirst;
    }


    public void setBtnStateLov2(RichCommandButton btnStateLov2) {
        this.btnStateLov2 = btnStateLov2;
    }

    public RichCommandButton getBtnStateLov2() {
        return btnStateLov2;
    }

    public void setBtnStateLov1(RichCommandButton btnStateLov1) {
        this.btnStateLov1 = btnStateLov1;
    }

    public RichCommandButton getBtnStateLov1() {
        return btnStateLov1;
    }

    public void setBtnSelectCountry2(RichCommandButton btnSelectCountry2) {
        this.btnSelectCountry2 = btnSelectCountry2;
    }

    public RichCommandButton getBtnSelectCountry2() {
        return btnSelectCountry2;
    }

    public void setBtnSelectCountry1(RichCommandButton btnSelectCountry1) {
        this.btnSelectCountry1 = btnSelectCountry1;
    }

    public RichCommandButton getBtnSelectCountry1() {
        return btnSelectCountry1;
    }

    public void setTblBranchNameListing(RichTable tblBranchNameListing) {
        this.tblBranchNameListing = tblBranchNameListing;
    }

    public RichTable getTblBranchNameListing() {
        return tblBranchNameListing;
    }

    public void setBtnSaveBranch(RichCommandButton btnSaveBranch) {
        this.btnSaveBranch = btnSaveBranch;
    }

    public RichCommandButton getBtnSaveBranch() {
        return btnSaveBranch;
    }

    public void setTxtDivOrder(RichInputNumberSpinbox txtDivOrder) {
        this.txtDivOrder = txtDivOrder;
    }

    public RichInputNumberSpinbox getTxtDivOrder() {
        return txtDivOrder;
    }

    public void setTblPersonnelsLOV(RichTable tblPersonnelsLOV) {
        this.tblPersonnelsLOV = tblPersonnelsLOV;
    }

    public RichTable getTblPersonnelsLOV() {
        return tblPersonnelsLOV;
    }

    public void setBtnPeronnelOK(RichCommandButton btnPeronnelOK) {
        this.btnPeronnelOK = btnPeronnelOK;
    }

    public RichCommandButton getBtnPeronnelOK() {
        return btnPeronnelOK;
    }

    public String actionShowDirectors() {
        btnPeronnelOK.setShortDesc("Director");
        GlobalCC.showPopUp("fms", "personnelPop");
        return null;
    }

    public String actionShowAssDirectors() {
        btnPeronnelOK.setShortDesc("Ass Director");
        GlobalCC.showPopUp("fms", "personnelPop");
        return null;
    }

    public void setTxtDirectorName(RichInputText txtDirectorName) {
        this.txtDirectorName = txtDirectorName;
    }

    public RichInputText getTxtDirectorName() {
        return txtDirectorName;
    }

    public void setTxtDirectorCode(RichInputText txtDirectorCode) {
        this.txtDirectorCode = txtDirectorCode;
    }

    public RichInputText getTxtDirectorCode() {
        return txtDirectorCode;
    }

    public void setTxtAssDirectorName(RichInputText txtAssDirectorName) {
        this.txtAssDirectorName = txtAssDirectorName;
    }

    public RichInputText getTxtAssDirectorName() {
        return txtAssDirectorName;
    }

    public void setTxtAssDirectorCode(RichInputText txtAssDirectorCode) {
        this.txtAssDirectorCode = txtAssDirectorCode;
    }

    public RichInputText getTxtAssDirectorCode() {
        return txtAssDirectorCode;
    }

    public String actionAcceptPersonnel() {
        Object key = tblPersonnelsLOV.getSelectedRowData();
        JUCtrlValueBinding nodeBinding = (JUCtrlValueBinding)key;
        if (nodeBinding != null) {


            if (btnPeronnelOK.getShortDesc().equalsIgnoreCase("Director")) {

                txtDirectorCode.setValue(nodeBinding.getAttribute("perId"));
                txtDirectorName.setValue(nodeBinding.getAttribute("perFullNames"));
                GlobalCC.refreshUI(txtDirectorName);

            } else if (btnPeronnelOK.getShortDesc().equalsIgnoreCase("Ass Director")) {
                txtAssDirectorCode.setValue(nodeBinding.getAttribute("perId"));
                txtAssDirectorName.setValue(nodeBinding.getAttribute("perFullNames"));
                GlobalCC.refreshUI(txtAssDirectorName);
            }
            GlobalCC.dismissPopUp("fms", "personnelPop");

        }
        return null;
    }

    public String actionCancelPersonnel() {
        GlobalCC.dismissPopUp("fms", "personnelPop");
        return null;
    }

    public String actionCancelDivision() {
        GlobalCC.dismissPopUp("fms", "orgDivisionPop");
        return null;
    }

    public String actionCancelCountry() {
        GlobalCC.dismissPopUp("fms", "countriesLovPopUp");


        return null;
    }

    public String actionCancelState() {
        GlobalCC.dismissPopUp("fms", "statesLovPop");


        return null;
    }

    public String actionCancelTown() {
        GlobalCC.dismissPopUp("fms", "townsLovPopUp");

        return null;
    }

    public String actionCancelCurrency() {
        GlobalCC.dismissPopUp("fms", "currenciesLovPopUp");

        return null;
    }

    public String actionCancelOrgManager() {
        GlobalCC.dismissPopUp("fms", "orgManagersLovPopUp");
        return null;
    }

    public String actionShowStates() {
        if (orgCouCode.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Select a country to proceed");
            return null;
        }
        ADFUtils.findIterator("fetchStatesByCountryIterator").executeQuery();
        GlobalCC.refreshUI(tblStatesLOV);
        GlobalCC.showPopUp("fms", "statesLovPop");
        return null;
    }

    public String actionShowTowns() {
        if (orgCouCode.getValue() == null) {
            GlobalCC.INFORMATIONREPORTING("Select a country to proceed");
            return null;
        }
        ADFUtils.findIterator("findTownsIterator").executeQuery();
        GlobalCC.refreshUI(townsLovTab);
        GlobalCC.showPopUp("fms", "townsLovPopUp");
        return null;
    }

    public String actionShowCurrencies() {
        ADFUtils.findIterator("findCurrenciesIterator").executeQuery();
        GlobalCC.refreshUI(currenciesLovTab);
        GlobalCC.showPopUp("fms", "currenciesLovPopUp");
        return null;
    }

    public String launchAgentManager() {
        ADFUtils.findIterator("findAgencyManagersIterator").executeQuery();
        GlobalCC.refreshUI(braManagersLovTab);
        GlobalCC.showPopUp("fms", "braManagersLovPopUp");
        return null;
    }

    public String launchUnitManager() {
        ADFUtils.findIterator("findUnitManagersIterator").executeQuery();
        GlobalCC.refreshUI(bruManagersLovTab);
        GlobalCC.showPopUp("fms", "bruManagersLovPopUp");
        return null;
    }

    public void setLblState(RichPanelLabelAndMessage lblState) {
        this.lblState = lblState;
    }

    public RichPanelLabelAndMessage getLblState() {
        return lblState;
    }

    public void setTxtManagerAllowed(RichSelectOneChoice txtManagerAllowed) {
        this.txtManagerAllowed = txtManagerAllowed;
    }

    public RichSelectOneChoice getTxtManagerAllowed() {
        return txtManagerAllowed;
    }

    public void setTxtOverideComm(RichSelectOneChoice txtOverideComm) {
        this.txtOverideComm = txtOverideComm;
    }

    public RichSelectOneChoice getTxtOverideComm() {
        return txtOverideComm;
    }

    public void setTxtManagerAllowedBranch(RichSelectOneChoice txtManagerAllowedBranch) {
        this.txtManagerAllowedBranch = txtManagerAllowedBranch;
    }

    public RichSelectOneChoice getTxtManagerAllowedBranch() {
        return txtManagerAllowedBranch;
    }

    public void setTxtOverideCommBranch(RichSelectOneChoice txtOverideCommBranch) {
        this.txtOverideCommBranch = txtOverideCommBranch;
    }

    public RichSelectOneChoice getTxtOverideCommBranch() {
        return txtOverideCommBranch;
    }

    public void setTxtManagerAllowedAgency(RichSelectOneChoice txtManagerAllowedAgency) {
        this.txtManagerAllowedAgency = txtManagerAllowedAgency;
    }

    public RichSelectOneChoice getTxtManagerAllowedAgency() {
        return txtManagerAllowedAgency;
    }

    public void setTxtOverideCommAgency(RichSelectOneChoice txtOverideCommAgency) {
        this.txtOverideCommAgency = txtOverideCommAgency;
    }

    public RichSelectOneChoice getTxtOverideCommAgency() {
        return txtOverideCommAgency;
    }

    public void setTxtBrnAgnPolPrefix(RichInputText txtBrnAgnPolPrefix) {
        this.txtBrnAgnPolPrefix = txtBrnAgnPolPrefix;
    }

    public RichInputText getTxtBrnAgnPolPrefix() {
        return txtBrnAgnPolPrefix;
    }

    public void setTxtShtDescPref(RichInputText txtShtDescPref) {
        this.txtShtDescPref = txtShtDescPref;
    }

    public RichInputText getTxtShtDescPref() {
        return txtShtDescPref;
    }

    public void setTxtBranchShtPref(RichInputText txtBranchShtPref) {
        this.txtBranchShtPref = txtBranchShtPref;
    }

    public RichInputText getTxtBranchShtPref() {
        return txtBranchShtPref;
    }

    public void setTxtPrefix(RichInputText txtPrefix) {
        this.txtPrefix = txtPrefix;
    }

    public RichInputText getTxtPrefix() {
        return txtPrefix;
    }

    public void setBrnComOwnBuss(RichInputText brnComOwnBuss) {
        this.brnComOwnBuss = brnComOwnBuss;
    }

    public RichInputText getBrnComOwnBuss() {
        return brnComOwnBuss;
    }

    public void setTxtcombuss(RichSelectOneChoice txtcombuss) {
        this.txtcombuss = txtcombuss;
    }

    public RichSelectOneChoice getTxtcombuss() {
        return txtcombuss;
    }

    public void setTxtUnitPrefix(RichInputText txtUnitPrefix) {
        this.txtUnitPrefix = txtUnitPrefix;
    }

    public RichInputText getTxtUnitPrefix() {
        return txtUnitPrefix;
    }

    public void setTxtComtBuss(RichSelectItem txtComtBuss) {
        this.txtComtBuss = txtComtBuss;
    }

    public RichSelectItem getTxtComtBuss() {
        return txtComtBuss;
    }


    public void setPolicySeq(RichInputNumberSpinbox policySeq) {
        this.policySeq = policySeq;
    }

    public RichInputNumberSpinbox getPolicySeq() {
        return policySeq;
    }

    public void setProposalSeq(RichInputNumberSpinbox proposalSeq) {
        this.proposalSeq = proposalSeq;
    }

    public RichInputNumberSpinbox getProposalSeq() {
        return proposalSeq;
    }


    public void setPolSeqAgnt(RichInputText polSeqAgnt) {
        this.polSeqAgnt = polSeqAgnt;
    }

    public RichInputText getPolSeqAgnt() {
        return polSeqAgnt;
    }

    public void setPropseqAgn(RichInputText propseqAgn) {
        this.propseqAgn = propseqAgn;
    }

    public RichInputText getPropseqAgn() {
        return propseqAgn;
    }

    public void setPolSeqAgn(RichInputText polSeqAgn) {
        this.polSeqAgn = polSeqAgn;
    }

    public RichInputText getPolSeqAgn() {
        return polSeqAgn;
    }

    public void setPolSeqNoUnits(RichInputNumberSpinbox polSeqNoUnits) {
        this.polSeqNoUnits = polSeqNoUnits;
    }

    public RichInputNumberSpinbox getPolSeqNoUnits() {
        return polSeqNoUnits;
    }

    public void setPropSeqNoUnits(RichInputNumberSpinbox propSeqNoUnits) {
        this.propSeqNoUnits = propSeqNoUnits;
    }

    public RichInputNumberSpinbox getPropSeqNoUnits() {
        return propSeqNoUnits;
    }

    public String AddBranchDivision() {
        try {
            session.setAttribute("action", "A");
            session.setAttribute("brnDivCode", null);

            wef.setValue(null);
            wet.setValue(null);
            branchLabel.setValue(null);

            // Render Popup
            ExtendedRenderKitService erkService =
                Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                   ExtendedRenderKitService.class);
            erkService.addScript(FacesContext.getCurrentInstance(),
                                 "var hints = {autodismissNever:false}; " +
                                 "AdfPage.PAGE.findComponent('" +
                                 "fms:branches" + "').show(hints);");
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String EditBranchDivision() {
        try {
            session.setAttribute("action", "E");
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findBranchDivisionsIterator");
            RowKeySet set = brnDivisionsLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("brnDivCode",
                                     r.getAttribute("BDIV_CODE"));
                branchLabel.setValue(r.getAttribute("BRN_NAME"));
                wef.setValue(r.getAttribute("BDIV_WEF"));
                wet.setValue(r.getAttribute("BDIV_WET"));

                // Render Popup
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "fms:brnDivisions" + "').show(hints);");
            }

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public String DeleteBranchDivision() {
        try {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findBranchDivisionsIterator");
            RowKeySet set = brnDivisionsLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("brnDivCode",
                                     r.getAttribute("BDIV_CODE"));
                SaveBranchDivision();
            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public void setBranchLabel(RichOutputText branchLabel) {
        this.branchLabel = branchLabel;
    }

    public RichOutputText getBranchLabel() {
        return branchLabel;
    }

    public void setWef(RichInputDate wef) {
        this.wef = wef;
    }

    public RichInputDate getWef() {
        return wef;
    }

    public void setWet(RichInputDate wet) {
        this.wet = wet;
    }

    public RichInputDate getWet() {
        return wet;
    }

    public String SaveBranchDivision() {
        DBConnector dbCon = new DBConnector();
        OracleConnection conn = null;
        conn = dbCon.getDatabaseConnection();

        try {
            String query =
                "begin TQC_WEB_ORGANIZATION_PKG.UPDATE_BRNCH_DIVISIONS(?,?,?,?,?,?,?); end;";
            OracleCallableStatement cst = null;
            cst = (OracleCallableStatement)conn.prepareCall(query);
            cst.setString(1, (String)session.getAttribute("action"));
            cst.setBigDecimal(2,
                              (BigDecimal)session.getAttribute("brnDivCode"));
            cst.setBigDecimal(3,
                              (BigDecimal)session.getAttribute("branchCode"));
            cst.setBigDecimal(4,
                              (BigDecimal)session.getAttribute("divisionCode"));
            if (wef.getValue() == null) {
                cst.setString(5, null);
            } else {
                String todayString = null;
                if (wef.getValue().toString().contains(":")) {

                    todayString =
                            GlobalCC.parseDate(wef.getValue().toString());
                } else {
                    todayString =
                            GlobalCC.upDateParseDate(wef.getValue().toString());
                }
                cst.setString(5, todayString);
            }
            if (wet.getValue() == null) {
                cst.setString(6, null);
            } else {
                String todayString = null;
                if (wet.getValue().toString().contains(":")) {
                    todayString =
                            GlobalCC.parseDate(wet.getValue().toString());
                } else {
                    todayString =
                            GlobalCC.upDateParseDate(wet.getValue().toString());
                }
                cst.setString(6, todayString);
            }
            cst.registerOutParameter(7,
                                     oracle.jdbc.internal.OracleTypes.VARCHAR);
            cst.execute();

            if (cst.getString(7) != null) {
                GlobalCC.errorValueNotEntered(cst.getString(7));
                return null;
            }
            cst.close();
            conn.close();
            ADFUtils.findIterator("findBranchDivisionsIterator").executeQuery();
            GlobalCC.refreshUI(brnDivisionsLOV);

        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(conn, e);
        }
        return null;
    }

    public void setBranchesLOV(RichTable branchesLOV) {
        this.branchesLOV = branchesLOV;
    }

    public RichTable getBranchesLOV() {
        return branchesLOV;
    }

    public String BranchSelected() {
        try {
            DCIteratorBinding dciter =
                ADFUtils.findIterator("findBranches1Iterator");
            RowKeySet set = branchesLOV.getSelectedRowKeys();
            Iterator rowKeySetIter = set.iterator();

            while (rowKeySetIter.hasNext()) {
                List l = (List)rowKeySetIter.next();
                Key key = (Key)l.get(0);
                dciter.setCurrentRowWithKey(key.toStringFormat(true));


                Row r = dciter.getCurrentRow();
                session.setAttribute("branchCode", r.getAttribute("BRN_CODE"));
                branchLabel.setValue(r.getAttribute("BRN_NAME"));

                // Render Popup
                ExtendedRenderKitService erkService =
                    Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                                       ExtendedRenderKitService.class);
                erkService.addScript(FacesContext.getCurrentInstance(),
                                     "var hints = {autodismissNever:false}; " +
                                     "AdfPage.PAGE.findComponent('" +
                                     "fms:brnDivisions" + "').show(hints);");

            }
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(null, e);
        }
        return null;
    }

    public void setBrnDivisionsLOV(RichTable brnDivisionsLOV) {
        this.brnDivisionsLOV = brnDivisionsLOV;
    }

    public RichTable getBrnDivisionsLOV() {
        return brnDivisionsLOV;
    }

    public void setTxtVatNumber(RichInputText txtVatNumber) {
        this.txtVatNumber = txtVatNumber;
    }

    public RichInputText getTxtVatNumber() {
        return txtVatNumber;
    }

    public void setTxtMobile1(RichInputText txtMobile1) {
        this.txtMobile1 = txtMobile1;
    }

    public RichInputText getTxtMobile1() {
        return txtMobile1;
    }

    public void setTxtMobile2(RichInputText txtMobile2) {
        this.txtMobile2 = txtMobile2;
    }

    public RichInputText getTxtMobile2() {
        return txtMobile2;
    }

    public void setSlMobile2(RichSelectOneChoice slMobile2) {
        this.slMobile2 = slMobile2;
    }

    public RichSelectOneChoice getSlMobile2() {
        return slMobile2;
    }

    public void setSlMobile1(RichSelectOneChoice slMobile1) {
        this.slMobile1 = slMobile1;
    }

    public RichSelectOneChoice getSlMobile1() {
        return slMobile1;
    }

    public void setContactsTab(RichTable contactsTab) {
        this.contactsTab = contactsTab;
    }

    public RichTable getContactsTab() {
        return contactsTab;
    }

    public void setTxtTbcCode(RichInputText txtTbcCode) {
        this.txtTbcCode = txtTbcCode;
    }

    public RichInputText getTxtTbcCode() {
        return txtTbcCode;
    }

    public void setTxtTbcName(RichInputText txtTbcName) {
        this.txtTbcName = txtTbcName;
    }

    public RichInputText getTxtTbcName() {
        return txtTbcName;
    }

    public void setTxtTbcDesignation(RichInputText txtTbcDesignation) {
        this.txtTbcDesignation = txtTbcDesignation;
    }

    public RichInputText getTxtTbcDesignation() {
        return txtTbcDesignation;
    }

    public void setTxtTbcMobile(RichInputText txtTbcMobile) {
        this.txtTbcMobile = txtTbcMobile;
    }

    public RichInputText getTxtTbcMobile() {
        return txtTbcMobile;
    }

    public void setTxtTbcTelephone(RichInputText txtTbcTelephone) {
        this.txtTbcTelephone = txtTbcTelephone;
    }

    public RichInputText getTxtTbcTelephone() {
        return txtTbcTelephone;
    }

    public void setTxtTbcIdNo(RichInputText txtTbcIdNo) {
        this.txtTbcIdNo = txtTbcIdNo;
    }

    public RichInputText getTxtTbcIdNo() {
        return txtTbcIdNo;
    }

    public void setTxtTbcPhysAdd(RichInputText txtTbcPhysAdd) {
        this.txtTbcPhysAdd = txtTbcPhysAdd;
    }

    public RichInputText getTxtTbcPhysAdd() {
        return txtTbcPhysAdd;
    }

    public void setTxtTbcEmail(RichInputText txtTbcEmail) {
        this.txtTbcEmail = txtTbcEmail;
    }

    public RichInputText getTxtTbcEmail() {
        return txtTbcEmail;
    }

    public String newContact() {
        txtTbcCode.setValue(null);
        txtTbcName.setValue(null);
        txtTbcDesignation.setValue(null);
        txtTbcMobile.setValue(null);
        txtTbcTelephone.setValue(null);
        txtTbcIdNo.setValue(null);
        txtTbcPhysAdd.setValue(null);
        txtTbcEmail.setValue(null);
        GlobalCC.refreshUI(txtTbcCode);
        GlobalCC.refreshUI(txtTbcName);
        GlobalCC.refreshUI(txtTbcDesignation);
        GlobalCC.refreshUI(txtTbcMobile);
        GlobalCC.refreshUI(txtTbcTelephone);
        GlobalCC.refreshUI(txtTbcIdNo);
        GlobalCC.refreshUI(txtTbcPhysAdd);
        GlobalCC.refreshUI(txtTbcEmail);
        GlobalCC.showPopUp("fms", "contacts");
        return null;
    }

    public String editContact() {
        RowKeySet rowKeySet = contactsTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        contactsTab.setRowKey(key2);

        JUCtrlValueBinding r = (JUCtrlValueBinding)contactsTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        txtTbcCode.setValue(r.getAttribute("tbcCode"));
        txtTbcName.setValue(r.getAttribute("tbcName"));
        txtTbcDesignation.setValue(r.getAttribute("tbcDesignation"));
        txtTbcMobile.setValue(r.getAttribute("tbcMobile"));
        txtTbcTelephone.setValue(r.getAttribute("tbcPhone"));
        txtTbcIdNo.setValue(r.getAttribute("tbcIdNumber"));
        txtTbcPhysAdd.setValue(r.getAttribute("tbcPhysicalAdd"));
        txtTbcEmail.setValue(r.getAttribute("tbcEmailAdd"));
        GlobalCC.refreshUI(txtTbcCode);
        GlobalCC.refreshUI(txtTbcName);
        GlobalCC.refreshUI(txtTbcDesignation);
        GlobalCC.refreshUI(txtTbcMobile);
        GlobalCC.refreshUI(txtTbcTelephone);
        GlobalCC.refreshUI(txtTbcIdNo);
        GlobalCC.refreshUI(txtTbcPhysAdd);
        GlobalCC.refreshUI(txtTbcEmail);
        GlobalCC.showPopUp("fms", "contacts");
        return null;
    }

    public String deleteContact() {
        RowKeySet rowKeySet = contactsTab.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        Object key2 = rowKeySet.iterator().next();
        contactsTab.setRowKey(key2);
        JUCtrlValueBinding r = (JUCtrlValueBinding)contactsTab.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }
        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        try {
            String query = null;
            //modify the query for treaty groups procedure
            query =
                    "begin TQC_WEB_ORGANIZATION_PKG.branch_contacts_proc(?,?,?,?,?,?,?,?,?,?); end;";


            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);

            callStmt.setString(1, "D");
            callStmt.setObject(2, r.getAttribute("tbcCode"));
            callStmt.setString(3, null);
            callStmt.setString(4, null);
            callStmt.setString(5, null);
            callStmt.setString(6, null);
            callStmt.setString(7, null);
            callStmt.setString(8, null);
            callStmt.setString(9, null);
            callStmt.setString(10, null);
            callStmt.execute();
            callStmt.close();
            conn.commit();
            conn.close();
            ADFUtils.findIterator("findBranchContactsIterator").executeQuery();
            GlobalCC.refreshUI(contactsTab);
            GlobalCC.dismissPopUp("fms", "contacts");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        return null;
    }

    public String saveContact() {
        String codeVal;
        String nameVal;
        String desigVal;
        String mobVal;
        String telVal;
        String idVal;
        String physVal;
        String emailVal;
        codeVal = GlobalCC.checkNullValues(txtTbcCode.getValue());
        nameVal = GlobalCC.checkNullValues(txtTbcName.getValue());
        desigVal = GlobalCC.checkNullValues(txtTbcDesignation.getValue());
        mobVal = GlobalCC.checkNullValues(txtTbcMobile.getValue());
        telVal = GlobalCC.checkNullValues(txtTbcTelephone.getValue());
        idVal = GlobalCC.checkNullValues(txtTbcIdNo.getValue());
        physVal = GlobalCC.checkNullValues(txtTbcPhysAdd.getValue());
        emailVal = GlobalCC.checkNullValues(txtTbcEmail.getValue());


        if (nameVal == null) {
            GlobalCC.errorValueNotEntered("Error Value Missing: Contact Name");
            return null;
        }


        DBConnector dbConnector = new DBConnector();
        OracleConnection conn;
        conn = dbConnector.getDatabaseConnection();
        try {
            String query = null;
            //modify the query for treaty groups procedure
            query =
                    "begin TQC_WEB_ORGANIZATION_PKG.branch_contacts_proc(?,?,?,?,?,?,?,?,?,?); end;";


            OracleCallableStatement callStmt = null;
            callStmt = (OracleCallableStatement)conn.prepareCall(query);
            //bind the variables

            String option = null;
            if (codeVal == null) {
                option = "A";
            } else {
                option = "E";
            }

            callStmt.setString(1, option);
            callStmt.setString(2, codeVal);
            callStmt.setString(3, nameVal);
            callStmt.setString(4, desigVal);
            callStmt.setString(5, mobVal);
            callStmt.setString(6, telVal);
            callStmt.setString(7, idVal);
            callStmt.setString(8, physVal);
            callStmt.setString(9, emailVal);
            callStmt.setObject(10, session.getAttribute("BRNCode"));
            callStmt.execute();

            callStmt.close();
            conn.commit();
            conn.close();

            ADFUtils.findIterator("findBranchContactsIterator").executeQuery();
            GlobalCC.refreshUI(contactsTab);
            GlobalCC.dismissPopUp("fms", "contacts");
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(conn, e);

        }
        return null;
    }

    public String cancelContact() {
        GlobalCC.dismissPopUp("fms", "contacts");
        return null;
    }

    public void setAccType(RichSelectOneChoice accType) {
        this.accType = accType;
    }

    public RichSelectOneChoice getAccType() {
        return accType;
    }

    public void setTxtRegion(RichSelectOneChoice txtRegion) {
        this.txtRegion = txtRegion;
    }

    public RichSelectOneChoice getTxtRegion() {
        return txtRegion;
    }


    public void uploadedImg(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            InputStream Reader;
            this.UploadedImageFile = _file;
            try {
                long Val = UploadedImageFile.getLength();
                Reader = UploadedImageFile.getInputStream();
                byte[] ImageBytes = new byte[Reader.available()];
                //int BytesLength = ImageBytes.length;
                Boolean isOk =
                    GlobalCC.validateUploadedImg(UploadedImageFile, "Client Image");
                System.out.println("isOk " + isOk);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(null, e);
            }
        }
    }

    public void setBankRegionsTab(RichTable bankRegionsTab) {
        this.bankRegionsTab = bankRegionsTab;
    }

    public RichTable getBankRegionsTab() {
        return bankRegionsTab;
    }

    public void setSaveBranchName(RichCommandButton saveBranchName) {
        this.saveBranchName = saveBranchName;
    }

    public RichCommandButton getSaveBranchName() {
        return saveBranchName;
    }

    public void setBnkrCode(RichInputNumberSpinbox bnkrCode) {
        this.bnkrCode = bnkrCode;
    }

    public RichInputNumberSpinbox getBnkrCode() {
        return bnkrCode;
    }

    public void setBnkrOrgCode(RichInputNumberSpinbox bnkrOrgCode) {
        this.bnkrOrgCode = bnkrOrgCode;
    }

    public RichInputNumberSpinbox getBnkrOrgCode() {
        return bnkrOrgCode;
    }

    public void setBnkrShtDesc(RichInputText bnkrShtDesc) {
        this.bnkrShtDesc = bnkrShtDesc;
    }

    public RichInputText getBnkrShtDesc() {
        return bnkrShtDesc;
    }

    public void setBnkrName(RichInputText bnkrName) {
        this.bnkrName = bnkrName;
    }

    public RichInputText getBnkrName() {
        return bnkrName;
    }

    public void setBnkrWef(RichInputDate bnkrWef) {
        this.bnkrWef = bnkrWef;
    }

    public RichInputDate getBnkrWef() {
        return bnkrWef;
    }

    public void setBnkrWet(RichInputDate bnkrWet) {
        this.bnkrWet = bnkrWet;
    }

    public RichInputDate getBnkrWet() {
        return bnkrWet;
    }

    public void setBnkrRegCode(RichInputNumberSpinbox bnkrRegCode) {
        this.bnkrRegCode = bnkrRegCode;
    }

    public RichInputNumberSpinbox getBnkrRegCode() {
        return bnkrRegCode;
    }

    public void setBnkrAgnCode(RichInputNumberSpinbox bnkrAgnCode) {
        this.bnkrAgnCode = bnkrAgnCode;
    }

    public RichInputNumberSpinbox getBnkrAgnCode() {
        return bnkrAgnCode;
    }

    public void setBnkrManager(RichInputText bnkrManager) {
        this.bnkrManager = bnkrManager;
    }

    public RichInputText getBnkrManager() {
        return bnkrManager;
    }

    public void setBtnSaveBankRegion(RichCommandButton btnSaveBankRegion) {
        this.btnSaveBankRegion = btnSaveBankRegion;
    }

    public RichCommandButton getBtnSaveBankRegion() {
        return btnSaveBankRegion;
    }

    public void setBankRegionsPopUp(RichPopup bankRegionsPopUp) {
        this.bankRegionsPopUp = bankRegionsPopUp;
    }

    public RichPopup getBankRegionsPopUp() {
        return bankRegionsPopUp;
    }

    public void setBankRegMgrsLovTab(RichTable bankRegMgrsLovTab) {
        this.bankRegMgrsLovTab = bankRegMgrsLovTab;
    }

    public RichTable getBankRegMgrsLovTab() {
        return bankRegMgrsLovTab;
    }

    public void setBankRegMgrsLovPopUp(RichPopup bankRegMgrsLovPopUp) {
        this.bankRegMgrsLovPopUp = bankRegMgrsLovPopUp;
    }

    public RichPopup getBankRegMgrsLovPopUp() {
        return bankRegMgrsLovPopUp;
    }

    public void setBtnShowBankRegMgrsLov(RichCommandButton btnShowBankRegMgrsLov) {
        this.btnShowBankRegMgrsLov = btnShowBankRegMgrsLov;
    }

    public RichCommandButton getBtnShowBankRegMgrsLov() {
        return btnShowBankRegMgrsLov;
    }

    public void setBtnDeleteBankRegion(RichCommandButton btnDeleteBankRegion) {
        this.btnDeleteBankRegion = btnDeleteBankRegion;
    }

    public RichCommandButton getBtnDeleteBankRegion() {
        return btnDeleteBankRegion;
    }

    public void setBtnEditBankRegion(RichCommandButton btnEditBankRegion) {
        this.btnEditBankRegion = btnEditBankRegion;
    }

    public RichCommandButton getBtnEditBankRegion() {
        return btnEditBankRegion;
    }

    public void setBtnNewBankRegion(RichCommandButton btnNewBankRegion) {
        this.btnNewBankRegion = btnNewBankRegion;
    }

    public RichCommandButton getBtnNewBankRegion() {
        return btnNewBankRegion;
    }

    public void setOrgAutoSignImg(RichImage orgAutoSignImg) {
        this.orgAutoSignImg = orgAutoSignImg;
    }

    public RichImage getOrgAutoSignImg() {
        return orgAutoSignImg;
    }

    public void setUserSignature(RichInputFile userSignature) {
        this.userSignature = userSignature;
    }

    public RichInputFile getUserSignature() {
        return userSignature;
    }

    public UploadedFile getSignatureFile() {
        return signatureFile;
    }

    public void setSignatureFile(UploadedFile file) {
        autoSignStream = null;
        if (file != null) {
            this.filename = file.getFilename();
            this.filesize = file.getLength();
            this.fileContent = file.getContentType();
            try {
                autoSignStream = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.signatureFile = file;
    }

    public void ImageSignatureUploadedListener(ValueChangeEvent valueChangeEvent) {
        if (session.getAttribute("sysUserCode") != null) {
            if (valueChangeEvent.getNewValue() !=
                valueChangeEvent.getOldValue()) {
                UploadedFile _file =
                    (UploadedFile)valueChangeEvent.getNewValue();
                InputStream Reader;
                this.UploadedImageFile = _file;
                try {
                    long Val = UploadedImageFile.getLength();
                    Reader = UploadedImageFile.getInputStream();
                    System.out.println(" UploadedImageFile " + _file);
                    byte[] ImageBytes = new byte[Reader.available()];
                    int BytesLength = ImageBytes.length;
                    Boolean isOk =
                        GlobalCC.validateUploadedSignImg(UploadedImageFile,
                                                         "Cert Signature");
                    System.out.println("********* " +
                                       GlobalCC.checkUploadedSignImg(UploadedImageFile,
                                                                     "User Signature"));
                    if (isOk == true) {

                        InsertCertSignature(Reader, Val);
                        orgAutoSignImg.setSource("/certsignatureservelet?id=" +
                                                 session.getAttribute("ORGCode"));
                        GlobalCC.refreshUI(orgAutoSignImg);
                        GlobalCC.refreshUI(userSignature);
                    } else {
                        userSignature.setValue(null);
                        GlobalCC.refreshUI(userSignature);
                    }

                } catch (Exception e) {
                    GlobalCC.EXCEPTIONREPORTING(e);
                }
            }
        }

    }

    public void InsertCertSignature(InputStream Image, long BytesLength) {
        try {

            DBConnector connector = new DBConnector();
            OracleConnection conn = connector.getDatabaseConnection();
            String systemsQuery =
                "UPDATE TQC_ORGANIZATIONS SET ORG_CERT_SIGN=? WHERE ORG_CODE=?";
            CallableStatement cst = null;
            cst = conn.prepareCall(systemsQuery);
            cst.setBlob(1, Image, BytesLength);
            cst.setObject(2, session.getAttribute("ORGCode"));

            cst.execute();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }

    public void setBtnTransferBranchAgency(RichCommandButton btnTransferBranchAgency) {
        this.btnTransferBranchAgency = btnTransferBranchAgency;
    }

    public RichCommandButton getBtnTransferBranchAgency() {
        return btnTransferBranchAgency;
    }


    public void setSwdTransferedItems(RichShowDetailItem swdTransferedItems) {
        this.swdTransferedItems = swdTransferedItems;
    }

    public RichShowDetailItem getSwdTransferedItems() {
        return swdTransferedItems;
    }

    public void setSwdTransferableItems(RichShowDetailItem swdTransferableItems) {
        this.swdTransferableItems = swdTransferableItems;
    }

    public RichShowDetailItem getSwdTransferableItems() {
        return swdTransferableItems;
    }

    public void setTblTransferableItems(RichTable tblTransferableItems) {
        this.tblTransferableItems = tblTransferableItems;
    }

    public RichTable getTblTransferableItems() {
        return tblTransferableItems;
    }

    public void setTblTransferedItems(RichTable tblTransferedItems) {
        this.tblTransferedItems = tblTransferedItems;
    }

    public RichTable getTblTransferedItems() {
        return tblTransferedItems;
    }

    public static void setAutoSignStream(InputStream autoSignStream) {
        OrganizationManip.autoSignStream = autoSignStream;
    }

    public static InputStream getAutoSignStream() {
        return autoSignStream;
    }

    public void setTxtBraCode(RichInputText txtBraCode) {
        this.txtBraCode = txtBraCode;
    }

    public RichInputText getTxtBraCode() {
        return txtBraCode;
    }

    public void setDlgTransferOrgItems(RichDialog dlgTransferOrgItems) {
        this.dlgTransferOrgItems = dlgTransferOrgItems;
    }

    public RichDialog getDlgTransferOrgItems() {
        return dlgTransferOrgItems;
    }

    public void setTxtTransferFromName(RichInputText txtTransferFromName) {
        this.txtTransferFromName = txtTransferFromName;
    }

    public RichInputText getTxtTransferFromName() {
        return txtTransferFromName;
    }

    public void setTxtTransferToName(RichInputText txtTransferToName) {
        this.txtTransferToName = txtTransferToName;
    }

    public RichInputText getTxtTransferToName() {
        return txtTransferToName;
    }

    public void setBtnTransferUnitAgents(RichCommandButton btnTransferUnitAgents) {
        this.btnTransferUnitAgents = btnTransferUnitAgents;
    }

    public RichCommandButton getBtnTransferUnitAgents() {
        return btnTransferUnitAgents;
    }

    public void setSdAgentsTab(RichShowDetailItem sdAgentsTab) {
        this.sdAgentsTab = sdAgentsTab;
    }

    public RichShowDetailItem getSdAgentsTab() {
        return sdAgentsTab;
    }

    public void setAgentsTab(RichTable agentsTab) {
        this.agentsTab = agentsTab;
    }

    public RichTable getAgentsTab() {
        return agentsTab;
    }

    public void setBtnTransferAgencyUnits(RichCommandButton btnTransferAgencyUnits) {
        this.btnTransferAgencyUnits = btnTransferAgencyUnits;
    }

    public RichCommandButton getBtnTransferAgencyUnits() {
        return btnTransferAgencyUnits;
    }

    public void setBtnShowTransferTolov(RichCommandButton btnShowTransferTolov) {
        this.btnShowTransferTolov = btnShowTransferTolov;
    }

    public RichCommandButton getBtnShowTransferTolov() {
        return btnShowTransferTolov;
    }

    public void setTblTransferToLov(RichTable tblTransferToLov) {
        this.tblTransferToLov = tblTransferToLov;
    }

    public RichTable getTblTransferToLov() {
        return tblTransferToLov;
    }

    public void setBtnTransferRegionBranch(RichCommandButton btnTransferRegionBranch) {
        this.btnTransferRegionBranch = btnTransferRegionBranch;
    }

    public RichCommandButton getBtnTransferRegionBranch() {
        return btnTransferRegionBranch;
    }

    public void setBtnTransferOrgRegion(RichCommandButton btnTransferOrgRegion) {
        this.btnTransferOrgRegion = btnTransferOrgRegion;
    }

    public RichCommandButton getBtnTransferOrgRegion() {
        return btnTransferOrgRegion;
    }

    public void setTxtOrgSwiftCode(RichInputText txtOrgSwiftCode) {
        this.txtOrgSwiftCode = txtOrgSwiftCode;
    }

    public RichInputText getTxtOrgSwiftCode() {
        return txtOrgSwiftCode;
    }

    public void setTxtOrgBankName(RichInputText txtOrgBankName) {
        this.txtOrgBankName = txtOrgBankName;
    }

    public RichInputText getTxtOrgBankName() {
        return txtOrgBankName;
    }

    public void setTxtOrgBankBranch(RichInputText txtOrgBankBranch) {
        this.txtOrgBankBranch = txtOrgBankBranch;
    }

    public RichInputText getTxtOrgBankBranch() {
        return txtOrgBankBranch;
    }

    public String actionAcceptOrgBankName() {

        JUCtrlValueBinding n =
            (JUCtrlValueBinding)orgBankLovTab.getSelectedRowData();

        if (n != null) {
            session.setAttribute("ORG_BNK_CODE", n.getAttribute("bnkCode"));
            txtOrgBankName.setValue(n.getAttribute("bnkName"));
            GlobalCC.refreshUI(txtOrgBankName);
        }
        GlobalCC.hidePopup("fms:popSelectOrgBankName");
        return null;
    }

    public String closeOrgBankNameLov() {
        GlobalCC.hidePopup("fms:popSelectOrgBankBranch");
        return null;
    }

    public String actionAcceptOrgBankBranch() {

        JUCtrlValueBinding n =
            (JUCtrlValueBinding)orgBankBranchLovTab.getSelectedRowData();

        if (n != null) {
            session.setAttribute("ORG_BBR_CODE", n.getAttribute("bbrCode"));
            txtOrgBankBranch.setValue(n.getAttribute("bbrName"));
            GlobalCC.refreshUI(txtOrgBankBranch);
        }
        GlobalCC.hidePopup("fms:popSelectOrgBankBranch");
        return null;
    }
    
    public boolean isShowAgencyTab(){
            String val = (String)GlobalCC.getSysParamValue("ORG_AGENCY_TAB_APP");
            return ("Y".equalsIgnoreCase(val));
        }


    public boolean isShowUnitTab(){
            String val = (String)GlobalCC.getSysParamValue("ORG_BRANCH_UNIT_TAB_APP");
            return ("Y".equalsIgnoreCase(val));
        }
    
    public boolean isShowBranchTransferButton(){
            String val = (String)GlobalCC.getSysParamValue("ORG_BRANCH_UNIT_TAB_APP");
            return ("N".equalsIgnoreCase(val));
        }
    
    
    public String closeOrgBankBranchLov() {
        GlobalCC.hidePopup("fms:popSelectOrgBankBranch");
        return null;
    }

    public void setOrgBankLovTab(RichTable orgBankLovTab) {
        this.orgBankLovTab = orgBankLovTab;
    }

    public RichTable getOrgBankLovTab() {
        return orgBankLovTab;
    }

    public void setOrgBankBranchLovTab(RichTable orgBankBranchLovTab) {
        this.orgBankBranchLovTab = orgBankBranchLovTab;
    }

    public RichTable getOrgBankBranchLovTab() {
        return orgBankBranchLovTab;
    }

    public void setTxtOrgBankAccountName(RichInputText txtOrgBankAccountName) {
        this.txtOrgBankAccountName = txtOrgBankAccountName;
    }

    public RichInputText getTxtOrgBankAccountName() {
        return txtOrgBankAccountName;
    }

    public void setTxtOrgBankAccountNo(RichInputText txtOrgBankAccountNo) {
        this.txtOrgBankAccountNo = txtOrgBankAccountNo;
    }

    public RichInputText getTxtOrgBankAccountNo() {
        return txtOrgBankAccountNo;
    }

    public void setBrnRef(RichInputText brnRef) {
        this.brnRef = brnRef;
    }

    public RichInputText getBrnRef() {
        return brnRef;
    }

    public void setBranchADE(RichInputText branchADE) {
        this.branchADE = branchADE;
    }

    public RichInputText getBranchADE() {
        return branchADE;
    }

    public String selectBrnADE() {
        RowKeySet rowKeySet = brnADEs.getSelectedRowKeys();
        if (!rowKeySet.iterator().hasNext()) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        Object key2 = rowKeySet.iterator().next();
        brnADEs.setRowKey(key2);

        JUCtrlValueBinding r = (JUCtrlValueBinding)brnADEs.getRowData();

        if (r == null) {
            GlobalCC.errorValueNotEntered("Error: No Record Selected");
            return null;
        }

        adeAgnCode.setValue(r.getAttribute("agnCode"));
        branchADE.setValue(r.getAttribute("agnName"));

        ExtendedRenderKitService erkService =
            Service.getService(FacesContext.getCurrentInstance().getRenderKit(),
                               ExtendedRenderKitService.class);
        erkService.addScript(FacesContext.getCurrentInstance(),
                             "var hints = {autodismissNever:false}; " +
                             "AdfPage.PAGE.findComponent('" +
                             "fms:brnADEsLovPopUp" + "').hide(hints);");


        AdfFacesContext adfFacesContext = AdfFacesContext.getCurrentInstance();
        adfFacesContext.addPartialTarget(adeAgnCode);
        adfFacesContext.addPartialTarget(branchADE);
        return null;
    }

    public void setBrnADEs(RichTable brnADEs) {
        this.brnADEs = brnADEs;
    }

    public RichTable getBrnADEs() {
        return brnADEs;
    }

    public void setAdeAgnCode(RichInputNumberSpinbox adeAgnCode) {
        this.adeAgnCode = adeAgnCode;
    }

    public RichInputNumberSpinbox getAdeAgnCode() {
        return adeAgnCode;
    }

    public void setBnsPostalCode(RichInputText bnsPostalCode) {
        this.bnsPostalCode = bnsPostalCode;
    }

    public RichInputText getBnsPostalCode() {
        return bnsPostalCode;
    }

    public void setTxtAgencyName(RichInputText txtAgencyName) {
        this.txtAgencyName = txtAgencyName;
    }

    public RichInputText getTxtAgencyName() {
        return txtAgencyName;
    }

    public void setTxtTeamManagerSequenceNo(RichInputNumberSpinbox txtTeamManagerSequenceNo) {
        this.txtTeamManagerSequenceNo = txtTeamManagerSequenceNo;
    }

    public RichInputNumberSpinbox getTxtTeamManagerSequenceNo() {
        return txtTeamManagerSequenceNo;
    }

    public void setTxtAgentsSequenceNo(RichInputNumberSpinbox txtAgentsSequenceNo) {
        this.txtAgentsSequenceNo = txtAgentsSequenceNo;
    }

    public RichInputNumberSpinbox getTxtAgentsSequenceNo() {
        return txtAgentsSequenceNo;
    }

    public void setBrn_Twn(RichInputText brn_Twn) {
        this.brn_Twn = brn_Twn;
    }

    public RichInputText getBrn_Twn() {
        return brn_Twn;
    }

    public void setBrnPostCode(RichInputText brnPostCode) {
        this.brnPostCode = brnPostCode;
    }

    public RichInputText getBrnPostCode() {
        return brnPostCode;
    }

    public void setMyPopup(RichPopup myPopup) {
        this.myPopup = myPopup;
    }

    public RichPopup getMyPopup() {
        return myPopup;
    }

    public void setTmanager(RichOutputLabel tmanager) {
        this.tmanager = tmanager;
    }

    public RichOutputLabel getTmanager() {
        return tmanager;
    }

    public void setTade(RichOutputLabel tade) {
        this.tade = tade;
    }

    public RichOutputLabel getTade() {
        return tade;
    }

    public void setTmanallowed(RichOutputLabel tmanallowed) {
        this.tmanallowed = tmanallowed;
    }

    public RichOutputLabel getTmanallowed() {
        return tmanallowed;
    }

    public void setBranchName(RichShowDetailItem branchName) {
        this.branchName = branchName;
    }

    public RichShowDetailItem getBranchName() {
        return branchName;
    }

    public void setFaAgentsTab(RichShowDetailItem faAgentsTab) {
        this.faAgentsTab = faAgentsTab;
    }

    public RichShowDetailItem getFaAgentsTab() {
        return faAgentsTab;
    }

    public void setBtnEdifaAgentsTab(RichCommandButton btnEdifaAgentsTab) {
        this.btnEdifaAgentsTab = btnEdifaAgentsTab;
    }

    public RichCommandButton getBtnEdifaAgentsTab() {
        return btnEdifaAgentsTab;
    }

    public void setBtndeletefaAgentsTab(RichCommandButton btndeletefaAgentsTab) {
        this.btndeletefaAgentsTab = btndeletefaAgentsTab;
    }

    public RichCommandButton getBtndeletefaAgentsTab() {
        return btndeletefaAgentsTab;
    }

    public void setBtnSaveFaAgent(RichCommandButton btnSaveFaAgent) {
        this.btnSaveFaAgent = btnSaveFaAgent;
    }

    public RichCommandButton getBtnSaveFaAgent() {
        return btnSaveFaAgent;
    }

    public void setFateamCode(RichInputText fateamCode) {
        this.fateamCode = fateamCode;
    }

    public RichInputText getFateamCode() {
        return fateamCode;
    }

    public void setFaAgentCode(RichInputText faAgentCode) {
        this.faAgentCode = faAgentCode;
    }

    public RichInputText getFaAgentCode() {
        return faAgentCode;
    }

    public void setFaShortDesc(RichInputText faShortDesc) {
        this.faShortDesc = faShortDesc;
    }

    public RichInputText getFaShortDesc() {
        return faShortDesc;
    }

    public void setFaAgentName(RichInputText faAgentName) {
        this.faAgentName = faAgentName;
    }

    public RichInputText getFaAgentName() {
        return faAgentName;
    }

    public void setFAagentsTab(RichTable FAagentsTab) {
        this.FAagentsTab = FAagentsTab;
    }

    public RichTable getFAagentsTab() {
        return FAagentsTab;
    }

    public void setChkTtiSelectA(RichSelectBooleanCheckbox chkTtiSelectA) {
        this.chkTtiSelectA = chkTtiSelectA;
    }

    public RichSelectBooleanCheckbox getChkTtiSelectA() {
        return chkTtiSelectA;
    }

    public void setChkTtiSelectB(RichSelectBooleanCheckbox chkTtiSelectB) {
        this.chkTtiSelectB = chkTtiSelectB;
    }

    public RichSelectBooleanCheckbox getChkTtiSelectB() {
        return chkTtiSelectB;
    }

    public void setBtnTransferBranchtAgents(RichCommandButton btnTransferBranchtAgents) {
        this.btnTransferBranchtAgents = btnTransferBranchtAgents;
    }

    public RichCommandButton getBtnTransferBranchtAgents() {
        return btnTransferBranchtAgents;
    }

    public void setDlgTransferOrgItems2(RichDialog dlgTransferOrgItems2) {
        this.dlgTransferOrgItems2 = dlgTransferOrgItems2;
    }

    public RichDialog getDlgTransferOrgItems2() {
        return dlgTransferOrgItems2;
    }

    public void setTxtTransferFromName2(RichInputText txtTransferFromName2) {
        this.txtTransferFromName2 = txtTransferFromName2;
    }

    public RichInputText getTxtTransferFromName2() {
        return txtTransferFromName2;
    }

    public void setTxtTransferToName2(RichInputText txtTransferToName2) {
        this.txtTransferToName2 = txtTransferToName2;
    }

    public RichInputText getTxtTransferToName2() {
        return txtTransferToName2;
    }

    public void setBtnShowTransferTolov2(RichCommandButton btnShowTransferTolov2) {
        this.btnShowTransferTolov2 = btnShowTransferTolov2;
    }

    public RichCommandButton getBtnShowTransferTolov2() {
        return btnShowTransferTolov2;
    }

    public void setSwdTransferedItems2(RichShowDetailItem swdTransferedItems2) {
        this.swdTransferedItems2 = swdTransferedItems2;
    }

    public RichShowDetailItem getSwdTransferedItems2() {
        return swdTransferedItems2;
    }

    public void setSwdTransferableItems2(RichShowDetailItem swdTransferableItems2) {
        this.swdTransferableItems2 = swdTransferableItems2;
    }

    public RichShowDetailItem getSwdTransferableItems2() {
        return swdTransferableItems2;
    }

    public void setBtnTransferUnitBranch(RichCommandButton btnTransferUnitBranch) {
        this.btnTransferUnitBranch = btnTransferUnitBranch;
    }

    public RichCommandButton getBtnTransferUnitBranch() {
        return btnTransferUnitBranch;
    }
}
