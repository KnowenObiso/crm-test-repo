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

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;


/**
 * The Agency Bean. Includes properties and methods that map to a
 * given Agency.
 *
 * @author Frankline Ogongi
 */
public class Agency implements Serializable {

    /* Properties */
    private String code;
    private String accountCode;
    private String shortDesc;
    private String name;
    private String physicalAddress;
    private String postalAddress;
    private String townCode;
    private String countryCode;
    private String emailAddress;
    private String webAddress;
    private String zip;
    private String contactPerson;
    private String contactTitle;
    private String telephone1;
    private String telephone2;
    private String fax;
    private String accountNum;
    private String PIN;
    private String commission;
    private String creditAllowed;
    private String witholdTx;
    private String printDbNote;
    private String status;
    private Date dateCreated;
    private String createdBy;
    private String registrationCode;
    private String commReserverate;
    private String annualBudget;
    private Date statusEffectiveDate;
    private String creditPeriod;
    private Date commStatEffectiveDate;
    private Date commStatusDate;
    private String commAllowed;
    private String checked;
    private String checkedBy;
    private Date checkDate;
    private String compCommArrears;
    private String reinsurer;
    private String branchCode;
    private String branchName;
    private String town;
    private String country;
    private String couAdminType;
    private String statusDesc;
    private String IDNum;
    private String conCode;
    private String agentCode;
    private String sms;
    private String holdCompanyCode;
    private String holdCompanyName;
    private String sectorCode;
    private String sectorName;
    private String classCode;
    private String className;
    private Date expiriyDate;
    private String licenseNum;
    private String runOff;
    private String licensed;
    private String licenseGracePeriod;
    private String oldAccountNum;
    private String statusRemarks;
    private String bankCode;
    private String bankName;
    private String bankBranchCode;
    private String bankBranchName;
    private String accountNo;
    private String agentPrefix;
    private String agentStateCode;
    private String agentStateName;
    private String agentCrRating;
    private String clientName;
    private String accountManager;
    private BigDecimal bru_code;
    private BigDecimal brnCode;

    private Date promTransDate;
    private String transType;
    private String promDemType;
    private String brnName;
    private String braName;

    private BigDecimal prodCode;
    private String prodDesc;
    private boolean selected;

    private BigDecimal agencyPrefix;
    private BigDecimal unitPrefix;
    private BigDecimal agencySeqNumber;
    private BigDecimal precontractCode;

    private String couZipCode;
    private Boolean select;
    private BigDecimal accountManagerCode;
    private BigDecimal agencyLimit;
    private BigDecimal bruCode;
    private String bruName;
    private String localInternational;
    private String regulatorNumber;
    private String Authorised;

    private BigDecimal rorgCode;
    private BigDecimal orsCode;
    private String rorgDesc;
    private String orsDesc;
    private String allocateCerts;
    private String holdingCompany;
    private String bouncedCheque;
    private String defaultCommMode;
    private BigDecimal agnBpnCode;
    private String bpnName;

    private String agencyType;
    private String agencyGroup;
    private BigDecimal mainAgnCode;
    private String mainAgent;
    private String vatApplicable;
    private String whtaxApplicable;
    private String telPay;
    private String pmtFreq;
    private BigDecimal cpmMode;
    private String cpmModeDesc;
    private String pmtValidated;
    private String AGN_COMM_LEVY_APP;
    private BigDecimal AGN_COMM_LEVY_RATE;
    private String regionName;
    private String regionCode;
    private String taxAuthorityName;
    private BigDecimal agnCode;
    private String agnShtDesc;
    private String agnName;
    private String agnUpdatedBy;
    private String agnUpdatedOn;
    /**
     * Default Class Constructor
     */
    public Agency() {
        super();
    }

    /* Methods */

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getPIN() {
        return PIN;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getCommission() {
        return commission;
    }

    public void setCreditAllowed(String creditAllowed) {
        this.creditAllowed = creditAllowed;
    }

    public String getCreditAllowed() {
        return creditAllowed;
    }

    public void setWitholdTx(String witholdTx) {
        this.witholdTx = witholdTx;
    }

    public String getWitholdTx() {
        return witholdTx;
    }

    public void setPrintDbNote(String printDbNote) {
        this.printDbNote = printDbNote;
    }

    public String getPrintDbNote() {
        return printDbNote;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setCommReserverate(String commReserverate) {
        this.commReserverate = commReserverate;
    }

    public String getCommReserverate() {
        return commReserverate;
    }

    public void setAnnualBudget(String annualBudget) {
        this.annualBudget = annualBudget;
    }

    public String getAnnualBudget() {
        return annualBudget;
    }

    public void setStatusEffectiveDate(Date statusEffectiveDate) {
        this.statusEffectiveDate = statusEffectiveDate;
    }

    public Date getStatusEffectiveDate() {
        return statusEffectiveDate;
    }

    public void setCreditPeriod(String creditPeriod) {
        this.creditPeriod = creditPeriod;
    }

    public String getCreditPeriod() {
        return creditPeriod;
    }

    public void setCommStatEffectiveDate(Date commStatEffectiveDate) {
        this.commStatEffectiveDate = commStatEffectiveDate;
    }

    public Date getCommStatEffectiveDate() {
        return commStatEffectiveDate;
    }

    public void setCommStatusDate(Date commStatusDate) {
        this.commStatusDate = commStatusDate;
    }

    public Date getCommStatusDate() {
        return commStatusDate;
    }

    public void setCommAllowed(String commAllowed) {
        this.commAllowed = commAllowed;
    }

    public String getCommAllowed() {
        return commAllowed;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getChecked() {
        return checked;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCompCommArrears(String compCommArrears) {
        this.compCommArrears = compCommArrears;
    }

    public String getCompCommArrears() {
        return compCommArrears;
    }

    public void setReinsurer(String reinsurer) {
        this.reinsurer = reinsurer;
    }

    public String getReinsurer() {
        return reinsurer;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTown() {
        return town;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setIDNum(String IDNum) {
        this.IDNum = IDNum;
    }

    public String getIDNum() {
        return IDNum;
    }

    public void setConCode(String conCode) {
        this.conCode = conCode;
    }

    public String getConCode() {
        return conCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getSms() {
        return sms;
    }

    public void setHoldCompanyCode(String holdCompanyCode) {
        this.holdCompanyCode = holdCompanyCode;
    }

    public String getHoldCompanyCode() {
        return holdCompanyCode;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setExpiriyDate(Date expiriyDate) {
        this.expiriyDate = expiriyDate;
    }

    public Date getExpiriyDate() {
        return expiriyDate;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public void setRunOff(String runOff) {
        this.runOff = runOff;
    }

    public String getRunOff() {
        return runOff;
    }

    public void setLicensed(String licensed) {
        this.licensed = licensed;
    }

    public String getLicensed() {
        return licensed;
    }

    public void setLicenseGracePeriod(String licenseGracePeriod) {
        this.licenseGracePeriod = licenseGracePeriod;
    }

    public String getLicenseGracePeriod() {
        return licenseGracePeriod;
    }

    public void setOldAccountNum(String oldAccountNum) {
        this.oldAccountNum = oldAccountNum;
    }

    public String getOldAccountNum() {
        return oldAccountNum;
    }

    public void setStatusRemarks(String statusRemarks) {
        this.statusRemarks = statusRemarks;
    }

    public String getStatusRemarks() {
        return statusRemarks;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setHoldCompanyName(String holdCompanyName) {
        this.holdCompanyName = holdCompanyName;
    }

    public String getHoldCompanyName() {
        return holdCompanyName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankBranchCode(String bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public String getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAgentPrefix(String agentPrefix) {
        this.agentPrefix = agentPrefix;
    }

    public String getAgentPrefix() {
        return agentPrefix;
    }

    public void setCouAdminType(String couAdminType) {
        this.couAdminType = couAdminType;
    }

    public String getCouAdminType() {
        return couAdminType;
    }

    public void setAgentStateCode(String agentStateCode) {
        this.agentStateCode = agentStateCode;
    }

    public String getAgentStateCode() {
        return agentStateCode;
    }

    public void setAgentStateName(String agentStateName) {
        this.agentStateName = agentStateName;
    }

    public String getAgentStateName() {
        return agentStateName;
    }

    public void setAgentCrRating(String agentCrRating) {
        this.agentCrRating = agentCrRating;
    }

    public String getAgentCrRating() {
        return agentCrRating;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setAccountManager(String accountManager) {
        this.accountManager = accountManager;
    }

    public String getAccountManager() {
        return accountManager;
    }

    public void setBru_code(BigDecimal bru_code) {
        this.bru_code = bru_code;
    }

    public BigDecimal getBru_code() {
        return bru_code;
    }

    public void setPromTransDate(Date promTransDate) {
        this.promTransDate = promTransDate;
    }

    public Date getPromTransDate() {
        return promTransDate;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransType() {
        return transType;
    }

    public void setPromDemType(String promDemType) {
        this.promDemType = promDemType;
    }

    public String getPromDemType() {
        return promDemType;
    }

    public void setBrnName(String brnName) {
        this.brnName = brnName;
    }

    public String getBrnName() {
        return brnName;
    }

    public void setBraName(String braName) {
        this.braName = braName;
    }

    public String getBraName() {
        return braName;
    }

    public void setBrnCode(BigDecimal brnCode) {
        this.brnCode = brnCode;
    }

    public BigDecimal getBrnCode() {
        return brnCode;
    }

    public void setAgencyPrefix(BigDecimal agencyPrefix) {
        this.agencyPrefix = agencyPrefix;
    }

    public BigDecimal getAgencyPrefix() {
        return agencyPrefix;
    }

    public void setUnitPrefix(BigDecimal unitPrefix) {
        this.unitPrefix = unitPrefix;
    }

    public BigDecimal getUnitPrefix() {
        return unitPrefix;
    }

    public void setAgencySeqNumber(BigDecimal agencySeqNumber) {
        this.agencySeqNumber = agencySeqNumber;
    }

    public BigDecimal getAgencySeqNumber() {
        return agencySeqNumber;
    }

    public void setPrecontractCode(BigDecimal precontractCode) {
        this.precontractCode = precontractCode;
    }

    public BigDecimal getPrecontractCode() {
        return precontractCode;
    }

    public void setCouZipCode(String couZipCode) {
        this.couZipCode = couZipCode;
    }

    public String getCouZipCode() {
        return couZipCode;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public Boolean getSelect() {
        return select;
    }

    public void setAccountManagerCode(BigDecimal accountManagerCode) {
        this.accountManagerCode = accountManagerCode;
    }

    public BigDecimal getAccountManagerCode() {
        return accountManagerCode;
    }

    public void setAgencyLimit(BigDecimal agencyLimit) {
        this.agencyLimit = agencyLimit;
    }

    public BigDecimal getAgencyLimit() {
        return agencyLimit;
    }

    public void setBruCode(BigDecimal bruCode) {
        this.bruCode = bruCode;
    }

    public BigDecimal getBruCode() {
        return bruCode;
    }

    public void setBruName(String bruName) {
        this.bruName = bruName;
    }

    public String getBruName() {
        return bruName;
    }


    public void setLocalInternational(String localInternational) {
        this.localInternational = localInternational;
    }

    public String getLocalInternational() {
        return localInternational;
    }

    public void setRegulatorNumber(String regulatorNumber) {
        this.regulatorNumber = regulatorNumber;
    }

    public String getRegulatorNumber() {
        return regulatorNumber;
    }

    public void setAuthorised(String Authorised) {
        this.Authorised = Authorised;
    }

    public String getAuthorised() {
        return Authorised;
    }

    public void setRorgCode(BigDecimal rorgCode) {
        this.rorgCode = rorgCode;
    }

    public BigDecimal getRorgCode() {
        return rorgCode;
    }

    public void setOrsCode(BigDecimal orsCode) {
        this.orsCode = orsCode;
    }

    public BigDecimal getOrsCode() {
        return orsCode;
    }

    public void setRorgDesc(String rorgDesc) {
        this.rorgDesc = rorgDesc;
    }

    public String getRorgDesc() {
        return rorgDesc;
    }

    public void setOrsDesc(String orsDesc) {
        this.orsDesc = orsDesc;
    }

    public String getOrsDesc() {
        return orsDesc;
    }

    public void setAllocateCerts(String allocateCerts) {
        this.allocateCerts = allocateCerts;
    }

    public String getAllocateCerts() {
        return allocateCerts;
    }

    public void setHoldingCompany(String holdingCompany) {
        this.holdingCompany = holdingCompany;
    }

    public String getHoldingCompany() {
        return holdingCompany;
    }

    public void setBouncedCheque(String bouncedCheque) {
        this.bouncedCheque = bouncedCheque;
    }

    public String getBouncedCheque() {
        return bouncedCheque;
    }

    public void setDefaultCommMode(String defaultCommMode) {
        this.defaultCommMode = defaultCommMode;
    }

    public String getDefaultCommMode() {
        return defaultCommMode;
    }

    public void setAgnBpnCode(BigDecimal agnBpnCode) {
        this.agnBpnCode = agnBpnCode;
    }

    public BigDecimal getAgnBpnCode() {
        return agnBpnCode;
    }

    public void setBpnName(String bpnName) {
        this.bpnName = bpnName;
    }

    public String getBpnName() {
        return bpnName;
    }

    public void setAgencyType(String agencyType) {
        this.agencyType = agencyType;
    }

    public String getAgencyType() {
        return agencyType;
    }

    public void setAgencyGroup(String agencyGroup) {
        this.agencyGroup = agencyGroup;
    }

    public String getAgencyGroup() {
        return agencyGroup;
    }

    public void setMainAgnCode(BigDecimal mainAgnCode) {
        this.mainAgnCode = mainAgnCode;
    }

    public BigDecimal getMainAgnCode() {
        return mainAgnCode;
    }

    public void setMainAgent(String mainAgent) {
        this.mainAgent = mainAgent;
    }

    public String getMainAgent() {
        return mainAgent;
    }

    public void setVatApplicable(String vatApplicable) {
        this.vatApplicable = vatApplicable;
    }

    public String getVatApplicable() {
        return vatApplicable;
    }

    public void setWhtaxApplicable(String whtaxApplicable) {
        this.whtaxApplicable = whtaxApplicable;
    }

    public String getWhtaxApplicable() {
        return whtaxApplicable;
    }

    public void setProdCode(BigDecimal prodCode) {
        this.prodCode = prodCode;
    }

    public BigDecimal getProdCode() {
        return prodCode;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setTelPay(String telPay) {
        this.telPay = telPay;
    }

    public String getTelPay() {
        return telPay;
    }

    public void setPmtFreq(String pmtFreq) {
        this.pmtFreq = pmtFreq;
    }

    public String getPmtFreq() {
        return pmtFreq;
    }

    public void setCpmMode(BigDecimal cpmMode) {
        this.cpmMode = cpmMode;
    }

    public BigDecimal getCpmMode() {
        return cpmMode;
    }

    public void setCpmModeDesc(String cpmModeDesc) {
        this.cpmModeDesc = cpmModeDesc;
    }

    public String getCpmModeDesc() {
        return cpmModeDesc;
    }

    public void setPmtValidated(String pmtValidated) {
        this.pmtValidated = pmtValidated;
    }

    public String getPmtValidated() {
        return pmtValidated;
    }

    public void setAGN_COMM_LEVY_APP(String AGN_COMM_LEVY_APP) {
        this.AGN_COMM_LEVY_APP = AGN_COMM_LEVY_APP;
    }

    public String getAGN_COMM_LEVY_APP() {
        return AGN_COMM_LEVY_APP;
    }

    public void setAGN_COMM_LEVY_RATE(BigDecimal AGN_COMM_LEVY_RATE) {
        this.AGN_COMM_LEVY_RATE = AGN_COMM_LEVY_RATE;
    }

    public BigDecimal getAGN_COMM_LEVY_RATE() {
        return AGN_COMM_LEVY_RATE;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setTaxAuthorityName(String taxAuthorityName) {
        this.taxAuthorityName = taxAuthorityName;
    }

    public String getTaxAuthorityName() {
        return taxAuthorityName;
    }

    public void setAgnCode(BigDecimal agnCode) {
        this.agnCode = agnCode;
    }

    public BigDecimal getAgnCode() {
        return agnCode;
    }

    public void setAgnShtDesc(String agnShtDesc) {
        this.agnShtDesc = agnShtDesc;
    }

    public String getAgnShtDesc() {
        return agnShtDesc;
    }

    public void setAgnName(String agnName) {
        this.agnName = agnName;
    }

    public String getAgnName() {
        return agnName;
    }

    public void setAgnUpdatedBy(String agnUpdatedBy) {
        this.agnUpdatedBy = agnUpdatedBy;
    }

    public String getAgnUpdatedBy() {
        return agnUpdatedBy;
    }

    public void setAgnUpdatedOn(String agnUpdatedOn) {
        this.agnUpdatedOn = agnUpdatedOn;
    }

    public String getAgnUpdatedOn() {
        return agnUpdatedOn;
    }
}
