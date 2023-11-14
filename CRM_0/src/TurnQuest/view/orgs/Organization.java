package TurnQuest.view.orgs;


import TurnQuest.view.Branches.Region;
import TurnQuest.view.Divisions.Division;

import java.math.BigDecimal;

import java.sql.Blob;

import java.util.Date;
import java.util.List;


public class Organization {
    public Organization() {
        super();
    }

    private BigDecimal orgCode;
    private String orgShortDesc;
    private String orgName;
    private String orgAddrs;
    private BigDecimal OrgTwnCode;
    private BigDecimal orgCouCode;
    private String orgEmail;
    private String orgPhyAddrs;
    private BigDecimal orgCurCode;
    private String orgZip;
    private String orgFax;
    private String orgTel1;
    private String orgTel2;
    private String orgRptLogo;
    private String orgMotto;
    private String orgPinNo;
    private String orgEdCode;
    private String orgItemAccCode;
    private String orgOtherName;
    private String orgType;
    private BigDecimal orgWebBrnCode;
    private String orgWebAddrs;
    private BigDecimal orgAgnCode;
    private String orgDirectors;
    private BigDecimal orgLangCode;
    private Blob orgAvatar;
    private BigDecimal orgStsCode;
    private String stateName;
    private String orgGrpLogo;
    private String mobile1;
    private String mobile2;


    //towns
    private BigDecimal twnCode;
    private BigDecimal twnCouCode;
    private String twnShortDesc;
    private String twnName;
    private BigDecimal twnStsCode;
    private BigDecimal twnPostalCode;

    //countries
    private BigDecimal couCode;
    private String couShortDesc;
    private String couName;
    private String couBaseCurr;
    private String couNationality;
    private BigDecimal couZipCode;
    private String administrativeType;

    //currencies
    private BigDecimal curCode;
    private String curSymbol;
    private String curDesc;
    private String curRnd;
    //Regions
    private BigDecimal regCode;
    private BigDecimal regOrgCode;
    private String regShortDesc;
    private String regName;
    private Date regWef;
    private Date regWet;
    private BigDecimal regAgnCode;
    private String regPostLevel;
    private String regMngrAllowed;
    private String regOverideCommEarned;
    private String regManager;
    private String regBrnMngrSeq_no;
    private String regAgnSeqNo;


    //Branches
    private BigDecimal brnCode;
    private String brnShortDesc;
    private BigDecimal brnRegCode;
    private String brnName;
    private String brnPhyAddrs;
    private String brnEmail;
    private String brnPostAddrs;
    private BigDecimal brnTwnCode;
    private BigDecimal brnCouCode;
    private String brnContact;
    private String brnManager;
    private String brnTel;
    private String brnFax;
    private String brnGenPolClm;
    private BigDecimal brnBnsCode;
    private BigDecimal brnAgnCode;
    private String brnPostLevel;
    private String brnMngrAllowed;
    private String brnOverideCommEarned;
    private String brnBrnMngrSeq_no;
    private String brnAgnSeqNo;
    private String brnAgnPolPrefix;
    private BigDecimal brnAdeCode;
    private String brnAdeName;
    private String brnTownName;
    private String brnPostCode;

    //Branch Units
    private BigDecimal bruCode;
    private BigDecimal bruBrnCode;
    private String bruShortDesc;
    private String bruName;
    private String bruSupervisor;
    private String bruStatus;
    private BigDecimal bruAgnCode;
    private BigDecimal bruBraCode;
    private String bruManager;
    private String bruPostLevel;
    private String bruMngrAllowed;
    private String bruOverideCommEarned;
    private String bruBrnMngrSeq_no;
    private String bruAgnSeqNo;
    //Branch Names
    private BigDecimal bnsCode;
    private String bnsShortDesc;
    private String bnsName;
    private String bnsPhyAddrs;
    private String bnsEmail;
    private String bnsPostAddrs;
    private BigDecimal bnsTwnCode;
    private BigDecimal bnsCouCode;
    private String bnsContact;
    private String bnsManager;
    private String bnsTel;
    private String bnsFax;

    // Manager Lov - For Regions, Branches and Units
    private BigDecimal agnCode;
    private String agnShortDesc;
    private String agnName;
    private String agnTwnName;

    //Branch agencies
    private BigDecimal braCode;
    private BigDecimal braBrnCode;
    private String braShortDesc;
    private String braName;
    private String braStatus;
    private String braManager;
    private BigDecimal braAgnCode;
    private String braPostLevel;
    private String braMngrAllowed;
    private String braOverideCommEarned;
    private String braBrnMngrSeq_no;
    private String braAgnSeqNo;
//    private String BraAgnShtDesc;
//    private String BraAgnName;
    
    
    //Unit agents
    private BigDecimal unaCode;
    private BigDecimal unaBruCode;
    private String unaShortDesc;
    private String unaName; 

    private String nodeType;
    private List<Region> regionsList;
    private List<Division> divisionList;
    private String policySeq;
    private String propSeq;
    private String agencypolicySeq;
    private String agencypropSeq;
    private String unitspolicySeq;
    private String unitspropSeq;
    private String vatNumber;

    //Branch Contacts
    private BigDecimal tbcCode;
    private String tbcName;
    private String tbcDesignation;
    private String tbcMobile;
    private String tbcPhone;
    private String tbcIdNumber;
    private String tbcPhysicalAdd;
    private String tbcEmailAdd;
    
    //Bank Regions
    
    BigDecimal bnkrCode;
    BigDecimal   bnkrOrgCode;
    String bnkrShtDesc;
    String bnkrName ;
    Date bnkrWef;
    Date bnkrWet;
    BigDecimal   bnkrRegCode;
    BigDecimal  bnkrAgnCode;
    String bnkrManager;
    //
    private Blob certSign;      
    
    //Transfer
    private BigDecimal ttCode;
    private String ttTransType;
    private Date ttTransDate;
    private String ttTransTo;
    private String ttTransFrom ;
    private String ttDoneBy    ;
    private Date ttDoneDate;
    private String ttAuthorized    ;
    private String ttAuthorizedBy;
    private Date ttAuthorizedDate;
    //Transfer Items
    private BigDecimal ttiCode;
    private java.lang.Boolean ttiSelected = false;
    private BigDecimal ttiTtCode;
    private BigDecimal ttiItemCode;
    private String ttiItemName;
    private String ttiItemShtDesc;
    private String ttiItemType ;
    private BigDecimal ttiFromCode ;
    private String ttiFromName ;
    private String shtDescPrefix;
    private String agnTxtCombuss;
    //Dest Lov Items
    private BigDecimal destCode;
    private String  destName; 
    //Bank Details
    private BigDecimal bnkCode;
    private String  bnkName; 
    private BigDecimal bbrCode;
    private String  bbrName; 
    private String ORG_BANK_ACCOUNT_NAME;
    private String ORG_BANK_ACCOUNT_NO;  
    private String ORG_SWIFT_CODE; 
    private BigDecimal ORG_BNK_CODE;
    private BigDecimal ORG_BBR_CODE; 
    private String ORG_BANK_NAME;  
    private String ORG_BANK_BRANCH;  
    private String ref;  
    
    private String txtAgencyDevExc;
    private String txtEarnOveride;
    private Date txtWEF;
    private Date txtWET;
    private BigDecimal    txtRegionCode;
    
    private BigDecimal bnsPostalCode;
    
    
    
    private String  KpiTask;
    private String  KpiSubTask;
    private BigDecimal kpiTcktCode;
    private BigDecimal kpidbId;
    
    private String txtAgencyName;
    private BigDecimal txtTeamManagerSequenceNo;
    private BigDecimal     txtAgentsSequenceNo;
    
    //Agency Intermediaries
       private BigDecimal intCode;
        private String intType;
        private String intName;
        private String intDesc;
        private String intPhysicalAddr;
        private String intPostalAddr;
        private String intTelNo;
        private String intFax;
        private String intMobile;
        private String intBank;
        private BigDecimal intBbrCode;
        private String intBankAccNo;
        private String intBankAccName;
        private String  intbankAccType;
        private BigDecimal  intActCode;
        private String  intActType;
        private Date  intWef;
        private Date  intWet;
    
    public void setOrgCode(BigDecimal orgCode) {
        this.orgCode = orgCode;
    }

    public BigDecimal getOrgCode() {
        return orgCode;
    }

    public void setOrgShortDesc(String orgShortDesc) {
        this.orgShortDesc = orgShortDesc;
    }

    public String getOrgShortDesc() {
        return orgShortDesc;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgAddrs(String orgAddrs) {
        this.orgAddrs = orgAddrs;
    }

    public String getOrgAddrs() {
        return orgAddrs;
    }

    public void setOrgTwnCode(BigDecimal OrgTwnCode) {
        this.OrgTwnCode = OrgTwnCode;
    }

    public BigDecimal getOrgTwnCode() {
        return OrgTwnCode;
    }

    public void setOrgCouCode(BigDecimal orgCouCode) {
        this.orgCouCode = orgCouCode;
    }

    public BigDecimal getOrgCouCode() {
        return orgCouCode;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public void setOrgPhyAddrs(String orgPhyAddrs) {
        this.orgPhyAddrs = orgPhyAddrs;
    }

    public String getOrgPhyAddrs() {
        return orgPhyAddrs;
    }

    public void setOrgCurCode(BigDecimal orgCurCode) {
        this.orgCurCode = orgCurCode;
    }

    public BigDecimal getOrgCurCode() {
        return orgCurCode;
    }

    public void setOrgZip(String orgZip) {
        this.orgZip = orgZip;
    }

    public String getOrgZip() {
        return orgZip;
    }

    public void setOrgFax(String orgFax) {
        this.orgFax = orgFax;
    }

    public String getOrgFax() {
        return orgFax;
    }

    public void setOrgTel1(String orgTel1) {
        this.orgTel1 = orgTel1;
    }

    public String getOrgTel1() {
        return orgTel1;
    }

    public void setOrgTel2(String orgTel2) {
        this.orgTel2 = orgTel2;
    }

    public String getOrgTel2() {
        return orgTel2;
    }

    public void setOrgRptLogo(String orgRptLogo) {
        this.orgRptLogo = orgRptLogo;
    }

    public String getOrgRptLogo() {
        return orgRptLogo;
    }

    public void setOrgMotto(String orgMotto) {
        this.orgMotto = orgMotto;
    }

    public String getOrgMotto() {
        return orgMotto;
    }

    public void setOrgPinNo(String orgPinNo) {
        this.orgPinNo = orgPinNo;
    }

    public String getOrgPinNo() {
        return orgPinNo;
    }

    public void setOrgEdCode(String orgEdCode) {
        this.orgEdCode = orgEdCode;
    }

    public String getOrgEdCode() {
        return orgEdCode;
    }

    public void setOrgItemAccCode(String orgItemAccCode) {
        this.orgItemAccCode = orgItemAccCode;
    }

    public String getOrgItemAccCode() {
        return orgItemAccCode;
    }

    public void setOrgOtherName(String orgOtherName) {
        this.orgOtherName = orgOtherName;
    }

    public String getOrgOtherName() {
        return orgOtherName;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgWebBrnCode(BigDecimal orgWebBrnCode) {
        this.orgWebBrnCode = orgWebBrnCode;
    }

    public BigDecimal getOrgWebBrnCode() {
        return orgWebBrnCode;
    }

    public void setOrgWebAddrs(String orgWebAddrs) {
        this.orgWebAddrs = orgWebAddrs;
    }

    public String getOrgWebAddrs() {
        return orgWebAddrs;
    }

    public void setOrgAgnCode(BigDecimal orgAgnCode) {
        this.orgAgnCode = orgAgnCode;
    }

    public BigDecimal getOrgAgnCode() {
        return orgAgnCode;
    }

    public void setOrgDirectors(String orgDirectors) {
        this.orgDirectors = orgDirectors;
    }

    public String getOrgDirectors() {
        return orgDirectors;
    }

    public void setOrgLangCode(BigDecimal orgLangCode) {
        this.orgLangCode = orgLangCode;
    }

    public BigDecimal getOrgLangCode() {
        return orgLangCode;
    }

    public void setOrgAvatar(Blob orgAvatar) {
        this.orgAvatar = orgAvatar;
    }

    public Blob getOrgAvatar() {
        return orgAvatar;
    }

    public void setTwnCode(BigDecimal twnCode) {
        this.twnCode = twnCode;
    }

    public BigDecimal getTwnCode() {
        return twnCode;
    }

    public void setTwnCouCode(BigDecimal twnCouCode) {
        this.twnCouCode = twnCouCode;
    }

    public BigDecimal getTwnCouCode() {
        return twnCouCode;
    }

    public void setTwnShortDesc(String twnShortDesc) {
        this.twnShortDesc = twnShortDesc;
    }

    public String getTwnShortDesc() {
        return twnShortDesc;
    }

    public void setTwnName(String twnName) {
        this.twnName = twnName;
    }

    public String getTwnName() {
        return twnName;
    }

    public void setTwnStsCode(BigDecimal twnStsCode) {
        this.twnStsCode = twnStsCode;
    }

    public BigDecimal getTwnStsCode() {
        return twnStsCode;
    }

    public void setCouCode(BigDecimal couCode) {
        this.couCode = couCode;
    }

    public BigDecimal getCouCode() {
        return couCode;
    }

    public void setCouShortDesc(String couShortDesc) {
        this.couShortDesc = couShortDesc;
    }

    public String getCouShortDesc() {
        return couShortDesc;
    }

    public void setCouName(String couName) {
        this.couName = couName;
    }

    public String getCouName() {
        return couName;
    }

    public void setCouBaseCurr(String couBaseCurr) {
        this.couBaseCurr = couBaseCurr;
    }

    public String getCouBaseCurr() {
        return couBaseCurr;
    }

    public void setCouNationality(String couNationality) {
        this.couNationality = couNationality;
    }

    public String getCouNationality() {
        return couNationality;
    }

    public void setCouZipCode(BigDecimal couZipCode) {
        this.couZipCode = couZipCode;
    }

    public BigDecimal getCouZipCode() {
        return couZipCode;
    }

    public void setCurCode(BigDecimal curCode) {
        this.curCode = curCode;
    }

    public BigDecimal getCurCode() {
        return curCode;
    }

    public void setCurSymbol(String curSymbol) {
        this.curSymbol = curSymbol;
    }

    public String getCurSymbol() {
        return curSymbol;
    }

    public void setCurDesc(String curDesc) {
        this.curDesc = curDesc;
    }

    public String getCurDesc() {
        return curDesc;
    }

    public void setCurRnd(String curRnd) {
        this.curRnd = curRnd;
    }

    public String getCurRnd() {
        return curRnd;
    }

    public void setRegCode(BigDecimal regCode) {
        this.regCode = regCode;
    }

    public BigDecimal getRegCode() {
        return regCode;
    }

    public void setRegOrgCode(BigDecimal regOrgCode) {
        this.regOrgCode = regOrgCode;
    }

    public BigDecimal getRegOrgCode() {
        return regOrgCode;
    }

    public void setRegShortDesc(String regShortDesc) {
        this.regShortDesc = regShortDesc;
    }

    public String getRegShortDesc() {
        return regShortDesc;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public String getRegName() {
        return regName;
    }

    public void setRegWef(Date regWef) {
        this.regWef = regWef;
    }

    public Date getRegWef() {
        return regWef;
    }

    public void setRegWet(Date regWet) {
        this.regWet = regWet;
    }

    public Date getRegWet() {
        return regWet;
    }

    public void setRegAgnCode(BigDecimal regAgnCode) {
        this.regAgnCode = regAgnCode;
    }

    public BigDecimal getRegAgnCode() {
        return regAgnCode;
    }

    public void setRegPostLevel(String regPostLevel) {
        this.regPostLevel = regPostLevel;
    }

    public String getRegPostLevel() {
        return regPostLevel;
    }

    public void setRegMngrAllowed(String regMngrAllowed) {
        this.regMngrAllowed = regMngrAllowed;
    }

    public String getRegMngrAllowed() {
        return regMngrAllowed;
    }

    public void setRegOverideCommEarned(String regOverideCommEarned) {
        this.regOverideCommEarned = regOverideCommEarned;
    }

    public String getRegOverideCommEarned() {
        return regOverideCommEarned;
    }

    public void setBrnCode(BigDecimal brnCode) {
        this.brnCode = brnCode;
    }

    public BigDecimal getBrnCode() {
        return brnCode;
    }

    public void setBrnShortDesc(String brnShortDesc) {
        this.brnShortDesc = brnShortDesc;
    }

    public String getBrnShortDesc() {
        return brnShortDesc;
    }

    public void setBrnRegCode(BigDecimal brnRegCode) {
        this.brnRegCode = brnRegCode;
    }

    public BigDecimal getBrnRegCode() {
        return brnRegCode;
    }

    public void setBrnName(String brnName) {
        this.brnName = brnName;
    }

    public String getBrnName() {
        return brnName;
    }

    public void setBrnPhyAddrs(String brnPhyAddrs) {
        this.brnPhyAddrs = brnPhyAddrs;
    }

    public String getBrnPhyAddrs() {
        return brnPhyAddrs;
    }

    public void setBrnEmail(String brnEmail) {
        this.brnEmail = brnEmail;
    }

    public String getBrnEmail() {
        return brnEmail;
    }

    public void setBrnPostAddrs(String brnPostAddrs) {
        this.brnPostAddrs = brnPostAddrs;
    }

    public String getBrnPostAddrs() {
        return brnPostAddrs;
    }

    public void setBrnTwnCode(BigDecimal brnTwnCode) {
        this.brnTwnCode = brnTwnCode;
    }

    public BigDecimal getBrnTwnCode() {
        return brnTwnCode;
    }

    public void setBrnCouCode(BigDecimal brnCouCode) {
        this.brnCouCode = brnCouCode;
    }

    public BigDecimal getBrnCouCode() {
        return brnCouCode;
    }

    public void setBrnContact(String brnContact) {
        this.brnContact = brnContact;
    }

    public String getBrnContact() {
        return brnContact;
    }

    public void setBrnManager(String brnManager) {
        this.brnManager = brnManager;
    }

    public String getBrnManager() {
        return brnManager;
    }

    public void setBrnTel(String brnTel) {
        this.brnTel = brnTel;
    }

    public String getBrnTel() {
        return brnTel;
    }

    public void setBrnFax(String brnFax) {
        this.brnFax = brnFax;
    }

    public String getBrnFax() {
        return brnFax;
    }

    public void setBrnGenPolClm(String brnGenPolClm) {
        this.brnGenPolClm = brnGenPolClm;
    }

    public String getBrnGenPolClm() {
        return brnGenPolClm;
    }

    public void setBrnBnsCode(BigDecimal brnBnsCode) {
        this.brnBnsCode = brnBnsCode;
    }

    public BigDecimal getBrnBnsCode() {
        return brnBnsCode;
    }

    public void setBrnAgnCode(BigDecimal brnAgnCode) {
        this.brnAgnCode = brnAgnCode;
    }

    public BigDecimal getBrnAgnCode() {
        return brnAgnCode;
    }

    public void setBrnPostLevel(String brnPostLevel) {
        this.brnPostLevel = brnPostLevel;
    }

    public String getBrnPostLevel() {
        return brnPostLevel;
    }

    public void setBruCode(BigDecimal bruCode) {
        this.bruCode = bruCode;
    }

    public BigDecimal getBruCode() {
        return bruCode;
    }

    public void setBruBrnCode(BigDecimal bruBrnCode) {
        this.bruBrnCode = bruBrnCode;
    }

    public BigDecimal getBruBrnCode() {
        return bruBrnCode;
    }

    public void setBruShortDesc(String bruShortDesc) {
        this.bruShortDesc = bruShortDesc;
    }

    public String getBruShortDesc() {
        return bruShortDesc;
    }

    public void setBruName(String bruName) {
        this.bruName = bruName;
    }

    public String getBruName() {
        return bruName;
    }

    public void setBruSupervisor(String bruSupervisor) {
        this.bruSupervisor = bruSupervisor;
    }

    public String getBruSupervisor() {
        return bruSupervisor;
    }

    public void setBruStatus(String bruStatus) {
        this.bruStatus = bruStatus;
    }

    public String getBruStatus() {
        return bruStatus;
    }

    public void setBruAgnCode(BigDecimal bruAgnCode) {
        this.bruAgnCode = bruAgnCode;
    }

    public BigDecimal getBruAgnCode() {
        return bruAgnCode;
    }

    public void setBruBraCode(BigDecimal bruBraCode) {
        this.bruBraCode = bruBraCode;
    }

    public BigDecimal getBruBraCode() {
        return bruBraCode;
    }

    public void setBruManager(String bruManager) {
        this.bruManager = bruManager;
    }

    public String getBruManager() {
        return bruManager;
    }

    public void setBruPostLevel(String bruPostLevel) {
        this.bruPostLevel = bruPostLevel;
    }

    public String getBruPostLevel() {
        return bruPostLevel;
    }

    public void setBnsCode(BigDecimal bnsCode) {
        this.bnsCode = bnsCode;
    }

    public BigDecimal getBnsCode() {
        return bnsCode;
    }

    public void setBnsShortDesc(String bnsShortDesc) {
        this.bnsShortDesc = bnsShortDesc;
    }

    public String getBnsShortDesc() {
        return bnsShortDesc;
    }

    public void setBnsName(String bnsName) {
        this.bnsName = bnsName;
    }

    public String getBnsName() {
        return bnsName;
    }

    public void setBnsPhyAddrs(String bnsPhyAddrs) {
        this.bnsPhyAddrs = bnsPhyAddrs;
    }

    public String getBnsPhyAddrs() {
        return bnsPhyAddrs;
    }

    public void setBnsEmail(String bnsEmail) {
        this.bnsEmail = bnsEmail;
    }

    public String getBnsEmail() {
        return bnsEmail;
    }

    public void setBnsPostAddrs(String bnsPostAddrs) {
        this.bnsPostAddrs = bnsPostAddrs;
    }

    public String getBnsPostAddrs() {
        return bnsPostAddrs;
    }

    public void setBnsTwnCode(BigDecimal bnsTwnCode) {
        this.bnsTwnCode = bnsTwnCode;
    }

    public BigDecimal getBnsTwnCode() {
        return bnsTwnCode;
    }

    public void setBnsCouCode(BigDecimal bnsCouCode) {
        this.bnsCouCode = bnsCouCode;
    }

    public BigDecimal getBnsCouCode() {
        return bnsCouCode;
    }

    public void setBnsContact(String bnsContact) {
        this.bnsContact = bnsContact;
    }

    public String getBnsContact() {
        return bnsContact;
    }

    public void setBnsManager(String bnsManager) {
        this.bnsManager = bnsManager;
    }

    public String getBnsManager() {
        return bnsManager;
    }

    public void setBnsTel(String bnsTel) {
        this.bnsTel = bnsTel;
    }

    public String getBnsTel() {
        return bnsTel;
    }

    public void setBnsFax(String bnsFax) {
        this.bnsFax = bnsFax;
    }

    public String getBnsFax() {
        return bnsFax;
    }

    public void setAgnCode(BigDecimal agnCode) {
        this.agnCode = agnCode;
    }

    public BigDecimal getAgnCode() {
        return agnCode;
    }

    public void setAgnShortDesc(String agnShortDesc) {
        this.agnShortDesc = agnShortDesc;
    }

    public String getAgnShortDesc() {
        return agnShortDesc;
    }

    public void setAgnName(String agnName) {
        this.agnName = agnName;
    }

    public String getAgnName() {
        return agnName;
    }

    public void setAgnTwnName(String agnTwnName) {
        this.agnTwnName = agnTwnName;
    }

    public String getAgnTwnName() {
        return agnTwnName;
    }

    public void setRegManager(String regManager) {
        this.regManager = regManager;
    }

    public String getRegManager() {
        return regManager;
    }

    public void setBraCode(BigDecimal braCode) {
        this.braCode = braCode;
    }

    public BigDecimal getBraCode() {
        return braCode;
    }

    public void setBraBrnCode(BigDecimal braBrnCode) {
        this.braBrnCode = braBrnCode;
    }

    public BigDecimal getBraBrnCode() {
        return braBrnCode;
    }

    public void setBraShortDesc(String braShortDesc) {
        this.braShortDesc = braShortDesc;
    }

    public String getBraShortDesc() {
        return braShortDesc;
    }

    public void setBraName(String braName) {
        this.braName = braName;
    }

    public String getBraName() {
        return braName;
    }

    public void setBraStatus(String braStatus) {
        this.braStatus = braStatus;
    }

    public String getBraStatus() {
        return braStatus;
    }

    public void setBraManager(String braManager) {
        this.braManager = braManager;
    }

    public String getBraManager() {
        return braManager;
    }

    public void setBraAgnCode(BigDecimal braAgnCode) {
        this.braAgnCode = braAgnCode;
    }

    public BigDecimal getBraAgnCode() {
        return braAgnCode;
    }

    public void setBraPostLevel(String braPostLevel) {
        this.braPostLevel = braPostLevel;
    }

    public String getBraPostLevel() {
        return braPostLevel;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setRegionsList(List<Region> regionsList) {
        this.regionsList = regionsList;
    }

    public List<Region> getRegionsList() {
        return regionsList;
    }

    public void setDivisionList(List<Division> divisionList) {
        this.divisionList = divisionList;
    }

    public List<Division> getDivisionList() {
        return divisionList;
    }

    public void setOrgStsCode(BigDecimal orgStsCode) {
        this.orgStsCode = orgStsCode;
    }

    public BigDecimal getOrgStsCode() {
        return orgStsCode;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setBraMngrAllowed(String braMngrAllowed) {
        this.braMngrAllowed = braMngrAllowed;
    }

    public String getBraMngrAllowed() {
        return braMngrAllowed;
    }

    public void setBraOverideCommEarned(String braOverideCommEarned) {
        this.braOverideCommEarned = braOverideCommEarned;
    }

    public String getBraOverideCommEarned() {
        return braOverideCommEarned;
    }

    public void setBrnMngrAllowed(String brnMngrAllowed) {
        this.brnMngrAllowed = brnMngrAllowed;
    }

    public String getBrnMngrAllowed() {
        return brnMngrAllowed;
    }

    public void setBrnOverideCommEarned(String brnOverideCommEarned) {
        this.brnOverideCommEarned = brnOverideCommEarned;
    }

    public String getBrnOverideCommEarned() {
        return brnOverideCommEarned;
    }

    public void setBruMngrAllowed(String bruMngrAllowed) {
        this.bruMngrAllowed = bruMngrAllowed;
    }

    public String getBruMngrAllowed() {
        return bruMngrAllowed;
    }

    public void setBruOverideCommEarned(String bruOverideCommEarned) {
        this.bruOverideCommEarned = bruOverideCommEarned;
    }

    public String getBruOverideCommEarned() {
        return bruOverideCommEarned;
    }

    public void setOrgGrpLogo(String orgGrpLogo) {
        this.orgGrpLogo = orgGrpLogo;
    }

    public String getOrgGrpLogo() {
        return orgGrpLogo;
    }

    public void setRegBrnMngrSeq_no(String regBrnMngrSeq_no) {
        this.regBrnMngrSeq_no = regBrnMngrSeq_no;
    }

    public String getRegBrnMngrSeq_no() {
        return regBrnMngrSeq_no;
    }

    public void setRegAgnSeqNo(String regAgnSeqNo) {
        this.regAgnSeqNo = regAgnSeqNo;
    }

    public String getRegAgnSeqNo() {
        return regAgnSeqNo;
    }

    public void setBrnBrnMngrSeq_no(String brnBrnMngrSeq_no) {
        this.brnBrnMngrSeq_no = brnBrnMngrSeq_no;
    }

    public String getBrnBrnMngrSeq_no() {
        return brnBrnMngrSeq_no;
    }

    public void setBrnAgnSeqNo(String brnAgnSeqNo) {
        this.brnAgnSeqNo = brnAgnSeqNo;
    }

    public String getBrnAgnSeqNo() {
        return brnAgnSeqNo;
    }

    public void setBruBrnMngrSeq_no(String bruBrnMngrSeq_no) {
        this.bruBrnMngrSeq_no = bruBrnMngrSeq_no;
    }

    public String getBruBrnMngrSeq_no() {
        return bruBrnMngrSeq_no;
    }

    public void setBruAgnSeqNo(String bruAgnSeqNo) {
        this.bruAgnSeqNo = bruAgnSeqNo;
    }

    public String getBruAgnSeqNo() {
        return bruAgnSeqNo;
    }

    public void setBraBrnMngrSeq_no(String braBrnMngrSeq_no) {
        this.braBrnMngrSeq_no = braBrnMngrSeq_no;
    }

    public String getBraBrnMngrSeq_no() {
        return braBrnMngrSeq_no;
    }

    public void setBraAgnSeqNo(String braAgnSeqNo) {
        this.braAgnSeqNo = braAgnSeqNo;
    }

    public String getBraAgnSeqNo() {
        return braAgnSeqNo;
    }

    public void setAdministrativeType(String administrativeType) {
        this.administrativeType = administrativeType;
    }

    public String getAdministrativeType() {
        return administrativeType;
    }

    public void setBrnAgnPolPrefix(String brnAgnPolPrefix) {
        this.brnAgnPolPrefix = brnAgnPolPrefix;
    }

    public String getBrnAgnPolPrefix() {
        return brnAgnPolPrefix;
    }

    public void setPolicySeq(String policySeq) {
        this.policySeq = policySeq;
    }

    public String getPolicySeq() {
        return policySeq;
    }

    public void setPropSeq(String propSeq) {
        this.propSeq = propSeq;
    }

    public String getPropSeq() {
        return propSeq;
    }

    public void setAgencypolicySeq(String agencypolicySeq) {
        this.agencypolicySeq = agencypolicySeq;
    }

    public String getAgencypolicySeq() {
        return agencypolicySeq;
    }

    public void setAgencypropSeq(String agencypropSeq) {
        this.agencypropSeq = agencypropSeq;
    }

    public String getAgencypropSeq() {
        return agencypropSeq;
    }

    public void setUnitspolicySeq(String unitspolicySeq) {
        this.unitspolicySeq = unitspolicySeq;
    }

    public String getUnitspolicySeq() {
        return unitspolicySeq;
    }

    public void setUnitspropSeq(String unitspropSeq) {
        this.unitspropSeq = unitspropSeq;
    }

    public String getUnitspropSeq() {
        return unitspropSeq;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setTbcCode(BigDecimal tbcCode) {
        this.tbcCode = tbcCode;
    }

    public BigDecimal getTbcCode() {
        return tbcCode;
    }

    public void setTbcName(String tbcName) {
        this.tbcName = tbcName;
    }

    public String getTbcName() {
        return tbcName;
    }

    public void setTbcDesignation(String tbcDesignation) {
        this.tbcDesignation = tbcDesignation;
    }

    public String getTbcDesignation() {
        return tbcDesignation;
    }

    public void setTbcMobile(String tbcMobile) {
        this.tbcMobile = tbcMobile;
    }

    public String getTbcMobile() {
        return tbcMobile;
    }

    public void setTbcPhone(String tbcPhone) {
        this.tbcPhone = tbcPhone;
    }

    public String getTbcPhone() {
        return tbcPhone;
    }

    public void setTbcIdNumber(String tbcIdNumber) {
        this.tbcIdNumber = tbcIdNumber;
    }

    public String getTbcIdNumber() {
        return tbcIdNumber;
    }

    public void setTbcPhysicalAdd(String tbcPhysicalAdd) {
        this.tbcPhysicalAdd = tbcPhysicalAdd;
    }

    public String getTbcPhysicalAdd() {
        return tbcPhysicalAdd;
    }

    public void setTbcEmailAdd(String tbcEmailAdd) {
        this.tbcEmailAdd = tbcEmailAdd;
    }

    public String getTbcEmailAdd() {
        return tbcEmailAdd;
    }

    public void setBnkrCode(BigDecimal bnkrCode) {
        this.bnkrCode = bnkrCode;
    }

    public BigDecimal getBnkrCode() {
        return bnkrCode;
    }

    public void setBnkrOrgCode(BigDecimal bnkrOrgCode) {
        this.bnkrOrgCode = bnkrOrgCode;
    }

    public BigDecimal getBnkrOrgCode() {
        return bnkrOrgCode;
    }

    public void setBnkrShtDesc(String bnkrShtDesc) {
        this.bnkrShtDesc = bnkrShtDesc;
    }

    public String getBnkrShtDesc() {
        return bnkrShtDesc;
    }

    public void setBnkrName(String bnkrName) {
        this.bnkrName = bnkrName;
    }

    public String getBnkrName() {
        return bnkrName;
    }

  

    public void setBnkrRegCode(BigDecimal bnkrRegCode) {
        this.bnkrRegCode = bnkrRegCode;
    }

    public BigDecimal getBnkrRegCode() {
        return bnkrRegCode;
    }

    public void setBnkrAgnCode(BigDecimal bnkrAgnCode) {
        this.bnkrAgnCode = bnkrAgnCode;
    }

    public BigDecimal getBnkrAgnCode() {
        return bnkrAgnCode;
    }

    public void setBnkrManager(String bnkrManager) {
        this.bnkrManager = bnkrManager;
    }

    public String getBnkrManager() {
        return bnkrManager;
    }

    public void setBnkrWef(Date bnkrWef) {
        this.bnkrWef = bnkrWef;
    }

    public Date getBnkrWef() {
        return bnkrWef;
    }

    public void setBnkrWet(Date bnkrWet) {
        this.bnkrWet = bnkrWet;
    }

    public Date getBnkrWet() {
        return bnkrWet;
    }

    public void setCertSign(Blob certSign) {
        this.certSign = certSign;
    }

    public Blob getCertSign() {
        return certSign;
    }


    public void setTtCode(BigDecimal ttCode) {
        this.ttCode = ttCode;
    }

    public BigDecimal getTtCode() {
        return ttCode;
    }

    public void setTtTransType(String ttTransType) {
        this.ttTransType = ttTransType;
    }

    public String getTtTransType() {
        return ttTransType;
    }

    public void setTtTransDate(Date ttTransDate) {
        this.ttTransDate = ttTransDate;
    }

    public Date getTtTransDate() {
        return ttTransDate;
    }

    public void setTtTransTo(String ttTransTo) {
        this.ttTransTo = ttTransTo;
    }

    public String getTtTransTo() {
        return ttTransTo;
    }

    public void setTtTransFrom(String ttTransFrom) {
        this.ttTransFrom = ttTransFrom;
    }

    public String getTtTransFrom() {
        return ttTransFrom;
    }

    public void setTtDoneBy(String ttDoneBy) {
        this.ttDoneBy = ttDoneBy;
    }

    public String getTtDoneBy() {
        return ttDoneBy;
    }

    public void setTtDoneDate(Date ttDoneDate) {
        this.ttDoneDate = ttDoneDate;
    }

    public Date getTtDoneDate() {
        return ttDoneDate;
    }

    public void setTtAuthorized(String ttAuthorized) {
        this.ttAuthorized = ttAuthorized;
    }

    public String getTtAuthorized() {
        return ttAuthorized;
    }

    public void setTtAuthorizedBy(String ttAuthorizedBy) {
        this.ttAuthorizedBy = ttAuthorizedBy;
    }

    public String getTtAuthorizedBy() {
        return ttAuthorizedBy;
    }

    public void setTtAuthorizedDate(Date ttAuthorizedDate) {
        this.ttAuthorizedDate = ttAuthorizedDate;
    }

    public Date getTtAuthorizedDate() {
        return ttAuthorizedDate;
    }

    public void setTtiCode(BigDecimal ttiCode) {
        this.ttiCode = ttiCode;
    }

    public BigDecimal getTtiCode() {
        return ttiCode;
    }

    public void setTtiTtCode(BigDecimal ttiTtCode) {
        this.ttiTtCode = ttiTtCode;
    }

    public BigDecimal getTtiTtCode() {
        return ttiTtCode;
    }

    public void setTtiItemCode(BigDecimal ttiItemCode) {
        this.ttiItemCode = ttiItemCode;
    }

    public BigDecimal getTtiItemCode() {
        return ttiItemCode;
    }

    public void setTtiItemName(String ttiItemName) {
        this.ttiItemName = ttiItemName;
    }

    public String getTtiItemName() {
        return ttiItemName;
    }

    public void setTtiItemShtDesc(String ttiItemShtDesc) {
        this.ttiItemShtDesc = ttiItemShtDesc;
    }

    public String getTtiItemShtDesc() {
        return ttiItemShtDesc;
    }

    public void setTtiItemType(String ttiItemType) {
        this.ttiItemType = ttiItemType;
    }

    public String getTtiItemType() {
        return ttiItemType;
    }

    public void setShtDescPrefix(String shtDescPrefix) {
        this.shtDescPrefix = shtDescPrefix;
    }

    public String getShtDescPrefix() {
        return shtDescPrefix;
    }

    public void setAgnTxtCombuss(String agnTxtCombuss) {
        this.agnTxtCombuss = agnTxtCombuss;
    }

    public String getAgnTxtCombuss() {
        return agnTxtCombuss;
    }

    public void setUnaCode(BigDecimal unaCode) {
        this.unaCode = unaCode;
    }

    public BigDecimal getUnaCode() {
        return unaCode;
    }

    public void setUnaBruCode(BigDecimal unaBruCode) {
        this.unaBruCode = unaBruCode;
    }

    public BigDecimal getUnaBruCode() {
        return unaBruCode;
    }

    public void setUnaShortDesc(String unaShortDesc) {
        this.unaShortDesc = unaShortDesc;
    }

    public String getUnaShortDesc() {
        return unaShortDesc;
    }

    public void setUnaName(String unaName) {
        this.unaName = unaName;
    }

    public String getUnaName() {
        return unaName;
    }

    public void setDestCode(BigDecimal destCode) {
        this.destCode = destCode;
    }

    public BigDecimal getDestCode() {
        return destCode;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getDestName() {
        return destName;
    }

    public void setBnkCode(BigDecimal bnkCode) {
        this.bnkCode = bnkCode;
    }

    public BigDecimal getBnkCode() {
        return bnkCode;
    }

    public void setBnkName(String bnkName) {
        this.bnkName = bnkName;
    }

    public String getBnkName() {
        return bnkName;
    }

    public void setBbrCode(BigDecimal bbrCode) {
        this.bbrCode = bbrCode;
    }

    public BigDecimal getBbrCode() {
        return bbrCode;
    }

    public void setBbrName(String bbrName) {
        this.bbrName = bbrName;
    }

    public String getBbrName() {
        return bbrName;
    }

    public void setORG_BANK_ACCOUNT_NAME(String ORG_BANK_ACCOUNT_NAME) {
        this.ORG_BANK_ACCOUNT_NAME = ORG_BANK_ACCOUNT_NAME;
    }

    public String getORG_BANK_ACCOUNT_NAME() {
        return ORG_BANK_ACCOUNT_NAME;
    }

    public void setORG_BANK_ACCOUNT_NO(String ORG_BANK_ACCOUNT_NO) {
        this.ORG_BANK_ACCOUNT_NO = ORG_BANK_ACCOUNT_NO;
    }

    public String getORG_BANK_ACCOUNT_NO() {
        return ORG_BANK_ACCOUNT_NO;
    }

    public void setORG_SWIFT_CODE(String ORG_SWIFT_CODE) {
        this.ORG_SWIFT_CODE = ORG_SWIFT_CODE;
    }

    public String getORG_SWIFT_CODE() {
        return ORG_SWIFT_CODE;
    }

    public void setORG_BNK_CODE(BigDecimal ORG_BNK_CODE) {
        this.ORG_BNK_CODE = ORG_BNK_CODE;
    }

    public BigDecimal getORG_BNK_CODE() {
        return ORG_BNK_CODE;
    }

    public void setORG_BBR_CODE(BigDecimal ORG_BBR_CODE) {
        this.ORG_BBR_CODE = ORG_BBR_CODE;
    }

    public BigDecimal getORG_BBR_CODE() {
        return ORG_BBR_CODE;
    }

    public void setORG_BANK_NAME(String ORG_BANK_NAME) {
        this.ORG_BANK_NAME = ORG_BANK_NAME;
    }

    public String getORG_BANK_NAME() {
        return ORG_BANK_NAME;
    }

    public void setORG_BANK_BRANCH(String ORG_BANK_BRANCH) {
        this.ORG_BANK_BRANCH = ORG_BANK_BRANCH;
    }

    public String getORG_BANK_BRANCH() {
        return ORG_BANK_BRANCH;
    }

  public void setTtiFromCode(BigDecimal ttiFromCode)
  {
    this.ttiFromCode = ttiFromCode;
  }

  public BigDecimal getTtiFromCode()
  {
    return ttiFromCode;
  }

  public void setTtiFromName(String ttiFromName)
  {
    this.ttiFromName = ttiFromName;
  }

  public String getTtiFromName()
  {
    return ttiFromName;
  }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    public void setTwnPostalCode(BigDecimal twnPostalCode) {
        this.twnPostalCode = twnPostalCode;
    }

    public BigDecimal getTwnPostalCode() {
        return twnPostalCode;
    }

    public void setTxtAgencyDevExc(String txtAgencyDevExc) {
        this.txtAgencyDevExc = txtAgencyDevExc;
    }

    public String getTxtAgencyDevExc() {
        return txtAgencyDevExc;
    }

    public void setTxtEarnOveride(String txtEarnOveride) {
        this.txtEarnOveride = txtEarnOveride;
    }

    public String getTxtEarnOveride() {
        return txtEarnOveride;
    }
    public void setBrnAdeCode(BigDecimal brnAdeCode) {
        this.brnAdeCode = brnAdeCode;
    }

    public BigDecimal getBrnAdeCode() {
        return brnAdeCode;
    }

    public void setBrnAdeName(String brnAdeName) {
        this.brnAdeName = brnAdeName;
    }

    public String getBrnAdeName() {
        return brnAdeName;
    }

    public void setTxtWEF(Date txtWEF) {
        this.txtWEF = txtWEF;
    }

    public Date getTxtWEF() {
        return txtWEF;
    }

    public void setTxtWET(Date txtWET) {
        this.txtWET = txtWET;
    }

    public Date getTxtWET() {
        return txtWET;
    }

    public void setTxtRegionCode(BigDecimal txtRegionCode) {
        this.txtRegionCode = txtRegionCode;
    }

    public BigDecimal getTxtRegionCode() {
        return txtRegionCode;
    }

    public void setBnsPostalCode(BigDecimal bnsPostalCode) {
        this.bnsPostalCode = bnsPostalCode;
    }

    public BigDecimal getBnsPostalCode() {
        return bnsPostalCode;
    }

    public void setTxtAgencyName(String txtAgencyName) {
        this.txtAgencyName = txtAgencyName;
    }

    public String getTxtAgencyName() {
        return txtAgencyName;
    }

    public void setTxtTeamManagerSequenceNo(BigDecimal txtTeamManagerSequenceNo) {
        this.txtTeamManagerSequenceNo = txtTeamManagerSequenceNo;
    }

    public BigDecimal getTxtTeamManagerSequenceNo() {
        return txtTeamManagerSequenceNo;
    }

    public void setTxtAgentsSequenceNo(BigDecimal txtAgentsSequenceNo) {
        this.txtAgentsSequenceNo = txtAgentsSequenceNo;
    }

    public BigDecimal getTxtAgentsSequenceNo() {
        return txtAgentsSequenceNo;
    }

    public void setBrnTownName(String brnTownName) {
        this.brnTownName = brnTownName;
    }

    public String getBrnTownName() {
        return brnTownName;
    }

    public void setBrnPostCode(String brnPostCode) {
        this.brnPostCode = brnPostCode;
    }

    public String getBrnPostCode() {
        return brnPostCode;
    }

    public void setKpiTask(String KpiTask) {
        this.KpiTask = KpiTask;
    }

    public String getKpiTask() {
        return KpiTask;
    }

    public void setKpiSubTask(String KpiSubTask) {
        this.KpiSubTask = KpiSubTask;
    }

    public String getKpiSubTask() {
        return KpiSubTask;
    }

    public void setKpiTcktCode(BigDecimal kpiTcktCode) {
        this.kpiTcktCode = kpiTcktCode;
    }

    public BigDecimal getKpiTcktCode() {
        return kpiTcktCode;
    }

    public void setKpidbId(BigDecimal kpidbId) {
        this.kpidbId = kpidbId;
    }

    public BigDecimal getKpidbId() {
        return kpidbId;
    }

    public void setIntCode(BigDecimal intCode) {
        this.intCode = intCode;
    }

    public BigDecimal getIntCode() {
        return intCode;
    }

    public void setIntType(String intType) {
        this.intType = intType;
    }

    public String getIntType() {
        return intType;
    }

    public void setIntName(String intName) {
        this.intName = intName;
    }

    public String getIntName() {
        return intName;
    }

    public void setIntDesc(String intDesc) {
        this.intDesc = intDesc;
    }

    public String getIntDesc() {
        return intDesc;
    }

    public void setIntPhysicalAddr(String intPhysicalAddr) {
        this.intPhysicalAddr = intPhysicalAddr;
    }

    public String getIntPhysicalAddr() {
        return intPhysicalAddr;
    }

    public void setIntPostalAddr(String intPostalAddr) {
        this.intPostalAddr = intPostalAddr;
    }

    public String getIntPostalAddr() {
        return intPostalAddr;
    }

    public void setIntTelNo(String intTelNo) {
        this.intTelNo = intTelNo;
    }

    public String getIntTelNo() {
        return intTelNo;
    }

    public void setIntFax(String intFax) {
        this.intFax = intFax;
    }

    public String getIntFax() {
        return intFax;
    }

    public void setIntMobile(String intMobile) {
        this.intMobile = intMobile;
    }

    public String getIntMobile() {
        return intMobile;
    }

    public void setIntBank(String intBank) {
        this.intBank = intBank;
    }

    public String getIntBank() {
        return intBank;
    }

    public void setIntBbrCode(BigDecimal intBbrCode) {
        this.intBbrCode = intBbrCode;
    }

    public BigDecimal getIntBbrCode() {
        return intBbrCode;
    }

    public void setIntBankAccNo(String intBankAccNo) {
        this.intBankAccNo = intBankAccNo;
    }

    public String getIntBankAccNo() {
        return intBankAccNo;
    }

    public void setIntBankAccName(String intBankAccName) {
        this.intBankAccName = intBankAccName;
    }

    public String getIntBankAccName() {
        return intBankAccName;
    }

    public void setIntbankAccType(String intbankAccType) {
        this.intbankAccType = intbankAccType;
    }

    public String getIntbankAccType() {
        return intbankAccType;
    }

    public void setIntActCode(BigDecimal intActCode) {
        this.intActCode = intActCode;
    }

    public BigDecimal getIntActCode() {
        return intActCode;
    }

    public void setIntActType(String intActType) {
        this.intActType = intActType;
    }

    public String getIntActType() {
        return intActType;
    }

    public void setIntWef(Date intWef) {
        this.intWef = intWef;
    }

    public Date getIntWef() {
        return intWef;
    }

    public void setIntWet(Date intWet) {
        this.intWet = intWet;
    }

    public Date getIntWet() {
        return intWet;
    }

    public void setTtiSelected(Boolean ttiSelected) {
        this.ttiSelected = ttiSelected;
    }

    public Boolean getTtiSelected() {
        return ttiSelected;
    }


}
