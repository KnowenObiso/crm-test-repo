package TurnQuest.view.Proposals;


import java.math.BigDecimal;

import java.util.Date;


public class Proposal {
    public Proposal() {
    }
    private String proposalNumber;
    private BigDecimal transactionNumber;
    private BigDecimal pprCode;
    private String Client;
    private String BranchName;
    private String AgentName;
    private String ProductDescription;
    private String pprPolTerm;
    private BigDecimal PremiumAmount;
    private String pprMippContr;
    private String paymentFrequencyCode;
    private String paymentFrequencyDesc;
    private String paymentModeCode;
    private String paymentModeDesc;
    private String status;
    private String preparedBy;
    private String pprBankAccountNo;
    private String bank;
    private Date EffectiveDate;
    private BigDecimal productCode;
    private String pprCoinsurance;
    private String pprCoinsureLeader;
    private String pprEdCode;
    private String pprItemAccCode;
    private Date pprProposalSignDate;

    private Date pprDate;
    private Date pprUwDate;
    private Date pprReturnToBrnDate;
    private Date pprReturnFromBrnDate;
    private BigDecimal pprAnnuityAmt;


    private BigDecimal prpInstCode;
    private String prpInstName;
    private Date deductMonth;
    private Date launchDate;
    private BigDecimal boDebitDay;
    private Date boStartDate;
    private Date boDebitDate;
    private BigDecimal bbrCode;

    private BigDecimal popCode;
    private String popDesc;
    private String payrollNo;

    private BigDecimal rcptamt;
    private BigDecimal medamt;
    private BigDecimal totcommclwbk;
    private BigDecimal admin_chrg;
    private BigDecimal totrefundamt;
    private BigDecimal childAge;

    private BigDecimal prpsCode;
    private BigDecimal prpsPolCode;
    private Date prpsDate;
    private String pendingReason;
    private String prpsStatus;
    private Date prpsDateSorted;

    private BigDecimal pgsCode;
    private String pgsDesc;
    private String lifeCoverFactor;

    private String retAgeDesc;
    private BigDecimal retAge;
    private String lifeRider;

    private BigDecimal lsfRate;
    private String cancelType;
    private BigDecimal pprCurrentSa;
    private String lsfApplyTo;
    private BigDecimal lsfCode;

    private BigDecimal lssCode;
    private BigDecimal lssUsrCode;
    private String lssSid;
    private Date lssDate;
    private String lssSourceJspx;
    private String lssTranstype;
    private String lssInitProp;
    private BigDecimal lssProdcode;
    private String lssProddesc;
    private BigDecimal lssBrncode;
    private String lssBrndesc;
    private BigDecimal lssPprcode;
    private String lssPprProposalNo;
    private BigDecimal lssPrpcode;
    private String lssPrpOtherNames;
    private String lssPrpSurname;
    private BigDecimal lssCoCode;
    private String lssPayfreq;
    private String lssPayfreqdesc;
    private String lssPaymode;
    private String lssPaymodedesc;
    private BigDecimal lssPremamt;
    private BigDecimal lssTerm;
    private BigDecimal lssAgncode;
    private String lssAgnname;
    private BigDecimal lssInstcode;
    private String lssInstdesc;
    private String lssBankaccnt;
    private BigDecimal lssBankbbrcode;
    private String lssBankbbrname;
    private String lssEffectiveDate;
    private String lssDeductmonth;
    private String lssDebitday;
    private Date lssStartdate;
    private BigDecimal lssPopCode;
    private String lssPopDesc;
    private String lssPayrollNo;
    private BigDecimal lssChildage;
    private BigDecimal lssSa;
    private BigDecimal lssClientPrpCode;
    private String lssClientOtherNames;
    private String lssClientSurname;
    private BigDecimal lssJointPrpCode;
    private String lssJointOtherNames;
    private String lssJointSurname;
    private BigDecimal lssMthlyIncome;
    private Date lssMaturityDate;
    private Date lssProposalDateSigned;
    private BigDecimal lssIntrRate;
    private String lssGrpLifeRider;
    private BigDecimal lssLifeCvrFact;
    private BigDecimal lssBcaCode;
    private String lssBcaDesc;

    //proposal Receipts
    private Date opprDate;
    private BigDecimal opprAmt;
    private String opprReceiptNo;
    private Date opprReceiptDate;
    private String opprDrCr;
    private String opprDoneBy;

    //proposal defects
    private BigDecimal prdCode;
    private BigDecimal prdDefCode;
    private BigDecimal prdPprCode;
    private String prdSorted;
    private Date prdSortDate;
    private String defDesc;
    private BigDecimal defCode;

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setTransactionNumber(BigDecimal transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public BigDecimal getTransactionNumber() {
        return transactionNumber;
    }

    public void setPprCode(BigDecimal pprCode) {
        this.pprCode = pprCode;
    }

    public BigDecimal getPprCode() {
        return pprCode;
    }

    public void setClient(String Client) {
        this.Client = Client;
    }

    public String getClient() {
        return Client;
    }

    public void setBranchName(String BranchName) {
        this.BranchName = BranchName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setAgentName(String AgentName) {
        this.AgentName = AgentName;
    }

    public String getAgentName() {
        return AgentName;
    }

    public void setProductDescription(String ProductDescription) {
        this.ProductDescription = ProductDescription;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setPprPolTerm(String pprPolTerm) {
        this.pprPolTerm = pprPolTerm;
    }

    public String getPprPolTerm() {
        return pprPolTerm;
    }

    public void setPremiumAmount(BigDecimal PremiumAmount) {
        this.PremiumAmount = PremiumAmount;
    }

    public BigDecimal getPremiumAmount() {
        return PremiumAmount;
    }

    public void setPprMippContr(String pprMippContr) {
        this.pprMippContr = pprMippContr;
    }

    public String getPprMippContr() {
        return pprMippContr;
    }

    public void setPaymentFrequencyCode(String paymentFrequencyCode) {
        this.paymentFrequencyCode = paymentFrequencyCode;
    }

    public String getPaymentFrequencyCode() {
        return paymentFrequencyCode;
    }

    public void setPaymentFrequencyDesc(String paymentFrequencyDesc) {
        this.paymentFrequencyDesc = paymentFrequencyDesc;
    }

    public String getPaymentFrequencyDesc() {
        return paymentFrequencyDesc;
    }

    public void setPaymentModeCode(String paymentModeCode) {
        this.paymentModeCode = paymentModeCode;
    }

    public String getPaymentModeCode() {
        return paymentModeCode;
    }

    public void setPaymentModeDesc(String paymentModeDesc) {
        this.paymentModeDesc = paymentModeDesc;
    }

    public String getPaymentModeDesc() {
        return paymentModeDesc;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPprBankAccountNo(String pprBankAccountNo) {
        this.pprBankAccountNo = pprBankAccountNo;
    }

    public String getPprBankAccountNo() {
        return pprBankAccountNo;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBank() {
        return bank;
    }

    public void setEffectiveDate(Date EffectiveDate) {
        this.EffectiveDate = EffectiveDate;
    }

    public Date getEffectiveDate() {
        return EffectiveDate;
    }

    public void setProductCode(BigDecimal productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getProductCode() {
        return productCode;
    }

    public void setPrpInstCode(BigDecimal prpInstCode) {
        this.prpInstCode = prpInstCode;
    }

    public BigDecimal getPrpInstCode() {
        return prpInstCode;
    }

    public void setPrpInstName(String prpInstName) {
        this.prpInstName = prpInstName;
    }

    public String getPrpInstName() {
        return prpInstName;
    }

    public void setDeductMonth(Date deductMonth) {
        this.deductMonth = deductMonth;
    }

    public Date getDeductMonth() {
        return deductMonth;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setBoDebitDay(BigDecimal boDebitDay) {
        this.boDebitDay = boDebitDay;
    }

    public BigDecimal getBoDebitDay() {
        return boDebitDay;
    }

    public void setBoStartDate(Date boStartDate) {
        this.boStartDate = boStartDate;
    }

    public Date getBoStartDate() {
        return boStartDate;
    }

    public void setBoDebitDate(Date boDebitDate) {
        this.boDebitDate = boDebitDate;
    }

    public Date getBoDebitDate() {
        return boDebitDate;
    }

    public void setBbrCode(BigDecimal bbrCode) {
        this.bbrCode = bbrCode;
    }

    public BigDecimal getBbrCode() {
        return bbrCode;
    }

    public void setPopCode(BigDecimal popCode) {
        this.popCode = popCode;
    }

    public BigDecimal getPopCode() {
        return popCode;
    }

    public void setPopDesc(String popDesc) {
        this.popDesc = popDesc;
    }

    public String getPopDesc() {
        return popDesc;
    }

    public void setPayrollNo(String payrollNo) {
        this.payrollNo = payrollNo;
    }

    public String getPayrollNo() {
        return payrollNo;
    }

    public void setRcptamt(BigDecimal rcptamt) {
        this.rcptamt = rcptamt;
    }

    public BigDecimal getRcptamt() {
        return rcptamt;
    }

    public void setMedamt(BigDecimal medamt) {
        this.medamt = medamt;
    }

    public BigDecimal getMedamt() {
        return medamt;
    }

    public void setTotcommclwbk(BigDecimal totcommclwbk) {
        this.totcommclwbk = totcommclwbk;
    }

    public BigDecimal getTotcommclwbk() {
        return totcommclwbk;
    }

    public void setAdmin_chrg(BigDecimal admin_chrg) {
        this.admin_chrg = admin_chrg;
    }

    public BigDecimal getAdmin_chrg() {
        return admin_chrg;
    }

    public void setTotrefundamt(BigDecimal totrefundamt) {
        this.totrefundamt = totrefundamt;
    }

    public BigDecimal getTotrefundamt() {
        return totrefundamt;
    }

    public void setChildAge(BigDecimal childAge) {
        this.childAge = childAge;
    }

    public BigDecimal getChildAge() {
        return childAge;
    }

    public void setPrpsCode(BigDecimal prpsCode) {
        this.prpsCode = prpsCode;
    }

    public BigDecimal getPrpsCode() {
        return prpsCode;
    }

    public void setPrpsPolCode(BigDecimal prpsPolCode) {
        this.prpsPolCode = prpsPolCode;
    }

    public BigDecimal getPrpsPolCode() {
        return prpsPolCode;
    }

    public void setPrpsDate(Date prpsDate) {
        this.prpsDate = prpsDate;
    }

    public Date getPrpsDate() {
        return prpsDate;
    }

    public void setPendingReason(String pendingReason) {
        this.pendingReason = pendingReason;
    }

    public String getPendingReason() {
        return pendingReason;
    }

    public void setPrpsStatus(String prpsStatus) {
        this.prpsStatus = prpsStatus;
    }

    public String getPrpsStatus() {
        return prpsStatus;
    }

    public void setPrpsDateSorted(Date prpsDateSorted) {
        this.prpsDateSorted = prpsDateSorted;
    }

    public Date getPrpsDateSorted() {
        return prpsDateSorted;
    }

    public void setPgsCode(BigDecimal pgsCode) {
        this.pgsCode = pgsCode;
    }

    public BigDecimal getPgsCode() {
        return pgsCode;
    }

    public void setPgsDesc(String pgsDesc) {
        this.pgsDesc = pgsDesc;
    }

    public String getPgsDesc() {
        return pgsDesc;
    }

    public void setLifeCoverFactor(String lifeCoverFactor) {
        this.lifeCoverFactor = lifeCoverFactor;
    }

    public String getLifeCoverFactor() {
        return lifeCoverFactor;
    }

    public void setRetAgeDesc(String retAgeDesc) {
        this.retAgeDesc = retAgeDesc;
    }

    public String getRetAgeDesc() {
        return retAgeDesc;
    }

    public void setRetAge(BigDecimal retAge) {
        this.retAge = retAge;
    }

    public BigDecimal getRetAge() {
        return retAge;
    }

    public void setLifeRider(String lifeRider) {
        this.lifeRider = lifeRider;
    }

    public String getLifeRider() {
        return lifeRider;
    }

    public void setLsfRate(BigDecimal lsfRate) {
        this.lsfRate = lsfRate;
    }

    public BigDecimal getLsfRate() {
        return lsfRate;
    }

    public void setCancelType(String cancelType) {
        this.cancelType = cancelType;
    }

    public String getCancelType() {
        return cancelType;
    }

    public void setPprCurrentSa(BigDecimal pprCurrentSa) {
        this.pprCurrentSa = pprCurrentSa;
    }

    public BigDecimal getPprCurrentSa() {
        return pprCurrentSa;
    }

    public void setLssCode(BigDecimal lssCode) {
        this.lssCode = lssCode;
    }

    public BigDecimal getLssCode() {
        return lssCode;
    }

    public void setLssUsrCode(BigDecimal lssUsrCode) {
        this.lssUsrCode = lssUsrCode;
    }

    public BigDecimal getLssUsrCode() {
        return lssUsrCode;
    }

    public void setLssSid(String lssSid) {
        this.lssSid = lssSid;
    }

    public String getLssSid() {
        return lssSid;
    }

    public void setLssDate(Date lssDate) {
        this.lssDate = lssDate;
    }

    public Date getLssDate() {
        return lssDate;
    }

    public void setLssSourceJspx(String lssSourceJspx) {
        this.lssSourceJspx = lssSourceJspx;
    }

    public String getLssSourceJspx() {
        return lssSourceJspx;
    }

    public void setLssTranstype(String lssTranstype) {
        this.lssTranstype = lssTranstype;
    }

    public String getLssTranstype() {
        return lssTranstype;
    }

    public void setLssInitProp(String lssInitProp) {
        this.lssInitProp = lssInitProp;
    }

    public String getLssInitProp() {
        return lssInitProp;
    }

    public void setLssProdcode(BigDecimal lssProdcode) {
        this.lssProdcode = lssProdcode;
    }

    public BigDecimal getLssProdcode() {
        return lssProdcode;
    }

    public void setLssProddesc(String lssProddesc) {
        this.lssProddesc = lssProddesc;
    }

    public String getLssProddesc() {
        return lssProddesc;
    }

    public void setLssBrncode(BigDecimal lssBrncode) {
        this.lssBrncode = lssBrncode;
    }

    public BigDecimal getLssBrncode() {
        return lssBrncode;
    }

    public void setLssBrndesc(String lssBrndesc) {
        this.lssBrndesc = lssBrndesc;
    }

    public String getLssBrndesc() {
        return lssBrndesc;
    }

    public void setLssPprcode(BigDecimal lssPprcode) {
        this.lssPprcode = lssPprcode;
    }

    public BigDecimal getLssPprcode() {
        return lssPprcode;
    }

    public void setLssPprProposalNo(String lssPprProposalNo) {
        this.lssPprProposalNo = lssPprProposalNo;
    }

    public String getLssPprProposalNo() {
        return lssPprProposalNo;
    }

    public void setLssPrpcode(BigDecimal lssPrpcode) {
        this.lssPrpcode = lssPrpcode;
    }

    public BigDecimal getLssPrpcode() {
        return lssPrpcode;
    }

    public void setLssPrpOtherNames(String lssPrpOtherNames) {
        this.lssPrpOtherNames = lssPrpOtherNames;
    }

    public String getLssPrpOtherNames() {
        return lssPrpOtherNames;
    }

    public void setLssPrpSurname(String lssPrpSurname) {
        this.lssPrpSurname = lssPrpSurname;
    }

    public String getLssPrpSurname() {
        return lssPrpSurname;
    }

    public void setLssCoCode(BigDecimal lssCoCode) {
        this.lssCoCode = lssCoCode;
    }

    public BigDecimal getLssCoCode() {
        return lssCoCode;
    }

    public void setLssPayfreq(String lssPayfreq) {
        this.lssPayfreq = lssPayfreq;
    }

    public String getLssPayfreq() {
        return lssPayfreq;
    }

    public void setLssPayfreqdesc(String lssPayfreqdesc) {
        this.lssPayfreqdesc = lssPayfreqdesc;
    }

    public String getLssPayfreqdesc() {
        return lssPayfreqdesc;
    }

    public void setLssPaymode(String lssPaymode) {
        this.lssPaymode = lssPaymode;
    }

    public String getLssPaymode() {
        return lssPaymode;
    }

    public void setLssPaymodedesc(String lssPaymodedesc) {
        this.lssPaymodedesc = lssPaymodedesc;
    }

    public String getLssPaymodedesc() {
        return lssPaymodedesc;
    }

    public void setLssPremamt(BigDecimal lssPremamt) {
        this.lssPremamt = lssPremamt;
    }

    public BigDecimal getLssPremamt() {
        return lssPremamt;
    }

    public void setLssTerm(BigDecimal lssTerm) {
        this.lssTerm = lssTerm;
    }

    public BigDecimal getLssTerm() {
        return lssTerm;
    }

    public void setLssAgncode(BigDecimal lssAgncode) {
        this.lssAgncode = lssAgncode;
    }

    public BigDecimal getLssAgncode() {
        return lssAgncode;
    }

    public void setLssAgnname(String lssAgnname) {
        this.lssAgnname = lssAgnname;
    }

    public String getLssAgnname() {
        return lssAgnname;
    }

    public void setLssInstcode(BigDecimal lssInstcode) {
        this.lssInstcode = lssInstcode;
    }

    public BigDecimal getLssInstcode() {
        return lssInstcode;
    }

    public void setLssInstdesc(String lssInstdesc) {
        this.lssInstdesc = lssInstdesc;
    }

    public String getLssInstdesc() {
        return lssInstdesc;
    }

    public void setLssBankaccnt(String lssBankaccnt) {
        this.lssBankaccnt = lssBankaccnt;
    }

    public String getLssBankaccnt() {
        return lssBankaccnt;
    }

    public void setLssBankbbrcode(BigDecimal lssBankbbrcode) {
        this.lssBankbbrcode = lssBankbbrcode;
    }

    public BigDecimal getLssBankbbrcode() {
        return lssBankbbrcode;
    }

    public void setLssBankbbrname(String lssBankbbrname) {
        this.lssBankbbrname = lssBankbbrname;
    }

    public String getLssBankbbrname() {
        return lssBankbbrname;
    }

    public void setLssEffectiveDate(String lssEffectiveDate) {
        this.lssEffectiveDate = lssEffectiveDate;
    }

    public String getLssEffectiveDate() {
        return lssEffectiveDate;
    }

    public void setLssDeductmonth(String lssDeductmonth) {
        this.lssDeductmonth = lssDeductmonth;
    }

    public String getLssDeductmonth() {
        return lssDeductmonth;
    }

    public void setLssDebitday(String lssDebitday) {
        this.lssDebitday = lssDebitday;
    }

    public String getLssDebitday() {
        return lssDebitday;
    }

    public void setLssStartdate(Date lssStartdate) {
        this.lssStartdate = lssStartdate;
    }

    public Date getLssStartdate() {
        return lssStartdate;
    }

    public void setLssPopCode(BigDecimal lssPopCode) {
        this.lssPopCode = lssPopCode;
    }

    public BigDecimal getLssPopCode() {
        return lssPopCode;
    }

    public void setLssPopDesc(String lssPopDesc) {
        this.lssPopDesc = lssPopDesc;
    }

    public String getLssPopDesc() {
        return lssPopDesc;
    }

    public void setLssPayrollNo(String lssPayrollNo) {
        this.lssPayrollNo = lssPayrollNo;
    }

    public String getLssPayrollNo() {
        return lssPayrollNo;
    }

    public void setLssChildage(BigDecimal lssChildage) {
        this.lssChildage = lssChildage;
    }

    public BigDecimal getLssChildage() {
        return lssChildage;
    }

    public void setLssSa(BigDecimal lssSa) {
        this.lssSa = lssSa;
    }

    public BigDecimal getLssSa() {
        return lssSa;
    }

    public void setLssClientPrpCode(BigDecimal lssClientPrpCode) {
        this.lssClientPrpCode = lssClientPrpCode;
    }

    public BigDecimal getLssClientPrpCode() {
        return lssClientPrpCode;
    }

    public void setLssClientOtherNames(String lssClientOtherNames) {
        this.lssClientOtherNames = lssClientOtherNames;
    }

    public String getLssClientOtherNames() {
        return lssClientOtherNames;
    }

    public void setLssClientSurname(String lssClientSurname) {
        this.lssClientSurname = lssClientSurname;
    }

    public String getLssClientSurname() {
        return lssClientSurname;
    }

    public void setLssJointPrpCode(BigDecimal lssJointPrpCode) {
        this.lssJointPrpCode = lssJointPrpCode;
    }

    public BigDecimal getLssJointPrpCode() {
        return lssJointPrpCode;
    }

    public void setLssJointOtherNames(String lssJointOtherNames) {
        this.lssJointOtherNames = lssJointOtherNames;
    }

    public String getLssJointOtherNames() {
        return lssJointOtherNames;
    }

    public void setLssJointSurname(String lssJointSurname) {
        this.lssJointSurname = lssJointSurname;
    }

    public String getLssJointSurname() {
        return lssJointSurname;
    }

    public void setLssMthlyIncome(BigDecimal lssMthlyIncome) {
        this.lssMthlyIncome = lssMthlyIncome;
    }

    public BigDecimal getLssMthlyIncome() {
        return lssMthlyIncome;
    }

    public void setLssMaturityDate(Date lssMaturityDate) {
        this.lssMaturityDate = lssMaturityDate;
    }

    public Date getLssMaturityDate() {
        return lssMaturityDate;
    }

    public void setLssProposalDateSigned(Date lssProposalDateSigned) {
        this.lssProposalDateSigned = lssProposalDateSigned;
    }

    public Date getLssProposalDateSigned() {
        return lssProposalDateSigned;
    }

    public void setLssIntrRate(BigDecimal lssIntrRate) {
        this.lssIntrRate = lssIntrRate;
    }

    public BigDecimal getLssIntrRate() {
        return lssIntrRate;
    }

    public void setLssGrpLifeRider(String lssGrpLifeRider) {
        this.lssGrpLifeRider = lssGrpLifeRider;
    }

    public String getLssGrpLifeRider() {
        return lssGrpLifeRider;
    }

    public void setLssLifeCvrFact(BigDecimal lssLifeCvrFact) {
        this.lssLifeCvrFact = lssLifeCvrFact;
    }

    public BigDecimal getLssLifeCvrFact() {
        return lssLifeCvrFact;
    }

    public void setLssBcaCode(BigDecimal lssBcaCode) {
        this.lssBcaCode = lssBcaCode;
    }

    public BigDecimal getLssBcaCode() {
        return lssBcaCode;
    }

    public void setLssBcaDesc(String lssBcaDesc) {
        this.lssBcaDesc = lssBcaDesc;
    }

    public String getLssBcaDesc() {
        return lssBcaDesc;
    }

    public void setOpprDate(Date opprDate) {
        this.opprDate = opprDate;
    }

    public Date getOpprDate() {
        return opprDate;
    }

    public void setOpprAmt(BigDecimal opprAmt) {
        this.opprAmt = opprAmt;
    }

    public BigDecimal getOpprAmt() {
        return opprAmt;
    }

    public void setOpprReceiptNo(String opprReceiptNo) {
        this.opprReceiptNo = opprReceiptNo;
    }

    public String getOpprReceiptNo() {
        return opprReceiptNo;
    }

    public void setOpprReceiptDate(Date opprReceiptDate) {
        this.opprReceiptDate = opprReceiptDate;
    }

    public Date getOpprReceiptDate() {
        return opprReceiptDate;
    }

    public void setOpprDrCr(String opprDrCr) {
        this.opprDrCr = opprDrCr;
    }

    public String getOpprDrCr() {
        return opprDrCr;
    }

    public void setOpprDoneBy(String opprDoneBy) {
        this.opprDoneBy = opprDoneBy;
    }

    public String getOpprDoneBy() {
        return opprDoneBy;
    }

    public void setDefDesc(String defDesc) {
        this.defDesc = defDesc;
    }

    public String getDefDesc() {
        return defDesc;
    }

    public void setPrdSorted(String prdSorted) {
        this.prdSorted = prdSorted;
    }

    public String getPrdSorted() {
        return prdSorted;
    }

    public void setPrdSortDate(Date prdSortDate) {
        this.prdSortDate = prdSortDate;
    }

    public Date getPrdSortDate() {
        return prdSortDate;
    }

    public void setDefCode(BigDecimal defCode) {
        this.defCode = defCode;
    }

    public BigDecimal getDefCode() {
        return defCode;
    }

    public void setPrdCode(BigDecimal prdCode) {
        this.prdCode = prdCode;
    }

    public BigDecimal getPrdCode() {
        return prdCode;
    }

    public void setPrdDefCode(BigDecimal prdDefCode) {
        this.prdDefCode = prdDefCode;
    }

    public BigDecimal getPrdDefCode() {
        return prdDefCode;
    }

    public void setPrdPprCode(BigDecimal prdPprCode) {
        this.prdPprCode = prdPprCode;
    }

    public BigDecimal getPrdPprCode() {
        return prdPprCode;
    }

    public void setPprCoinsurance(String pprCoinsurance) {
        this.pprCoinsurance = pprCoinsurance;
    }

    public String getPprCoinsurance() {
        return pprCoinsurance;
    }

    public void setPprCoinsureLeader(String pprCoinsureLeader) {
        this.pprCoinsureLeader = pprCoinsureLeader;
    }

    public String getPprCoinsureLeader() {
        return pprCoinsureLeader;
    }

    public void setPprEdCode(String pprEdCode) {
        this.pprEdCode = pprEdCode;
    }

    public String getPprEdCode() {
        return pprEdCode;
    }

    public void setPprItemAccCode(String pprItemAccCode) {
        this.pprItemAccCode = pprItemAccCode;
    }

    public String getPprItemAccCode() {
        return pprItemAccCode;
    }

    public void setPprProposalSignDate(Date pprProposalSignDate) {
        this.pprProposalSignDate = pprProposalSignDate;
    }

    public Date getPprProposalSignDate() {
        return pprProposalSignDate;
    }

    public void setPprDate(Date pprDate) {
        this.pprDate = pprDate;
    }

    public Date getPprDate() {
        return pprDate;
    }

    public void setPprUwDate(Date pprUwDate) {
        this.pprUwDate = pprUwDate;
    }

    public Date getPprUwDate() {
        return pprUwDate;
    }

    public void setPprReturnToBrnDate(Date pprReturnToBrnDate) {
        this.pprReturnToBrnDate = pprReturnToBrnDate;
    }

    public Date getPprReturnToBrnDate() {
        return pprReturnToBrnDate;
    }

    public void setPprReturnFromBrnDate(Date pprReturnFromBrnDate) {
        this.pprReturnFromBrnDate = pprReturnFromBrnDate;
    }

    public Date getPprReturnFromBrnDate() {
        return pprReturnFromBrnDate;
    }

    public void setLsfApplyTo(String lsfApplyTo) {
        this.lsfApplyTo = lsfApplyTo;
    }

    public String getLsfApplyTo() {
        return lsfApplyTo;
    }

    public void setLsfCode(BigDecimal lsfCode) {
        this.lsfCode = lsfCode;
    }

    public BigDecimal getLsfCode() {
        return lsfCode;
    }

    public void setPprAnnuityAmt(BigDecimal pprAnnuityAmt) {
        this.pprAnnuityAmt = pprAnnuityAmt;
    }

    public BigDecimal getPprAnnuityAmt() {
        return pprAnnuityAmt;
    }
}
