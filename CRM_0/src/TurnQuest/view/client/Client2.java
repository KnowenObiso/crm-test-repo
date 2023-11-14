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

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;


/**
 * The Client Bean. Includes properties and methods that map to a
 * given Client.
 *
 * @author Frankline Ogongi
 */
public class Client2 implements Serializable {

    private String code;
    private String shortDesc;
    private String oldShtDesc;
    private String name;
    private String otherNames;
    private String idRegNo;
    private Date dob;
    private String pin;
    private String physicalAddress;
    private String postalAddress;
    private String countryCode;
    private String countryName;
    private String townCode;
    private String townName;
    private String email;
    private String phone1;
    private String phone2;
    private String status;
    private String fax;
    private String remarks;
    private String specialTerms;
    private String declinedProp;
    private String increasedPremium;
    private String policyCancelled;
    private String proposer;
    private String accountNum;
    private String domicileCountries;
    private Date wef;
    private Date wet;
    private String withdrawalReason;

    private BigDecimal sectorCode;
    private String sectorName;
    private String head;
    private String surname;
    private String type;
    private String title;
    private String business;
    private String zipCode;
    private String couZipCode;
    private String bankBranchCode;
    private String bankAccountNum;
    private String clientCode;
    private String nonDirect;
    private String createdBy;
    private String sms;
    private String agentStatus;
    private Date dateCreated;
    private String runOff;
    private String loadedBy;
    private String directClient;
    private String oldAccountNum;
    private String userCode;
    private String username;
    private String passportNumber;
    private String gender;
    private String contactName1;
    private String contactPhone1;
    private String contactEmail1;
    private String contactName2;
    private String contactPhone2;
    private String contactEmail2;
    private BigDecimal stsCode;
    private String stsName;
    private String clientCell;
    private String bankBranch;

    private String website;
    private String auditors;
    private String parent_company;
    private String parent_company_name;
    private String current_insurer;
    private BigDecimal projected_business;
    private Date date_of_empl;
    private String driving_licence;
    private BigDecimal brnCode;
    private String brnName;
    private BigDecimal aacOfficer;
    private String aacOfficerName;
    private String occupation;
    private Date anniversary;
    private String creditRating;

    private int count;

    private boolean selected;

    private BigDecimal hhCode;
    private String hhName;
    private String hhCategory;

    private boolean checked;
    private boolean select;

    /**
     * corporate DIRECTORS
     *
     */
    private BigDecimal clntdir_code;
    private BigDecimal clntdir_clnt_code;
    private BigDecimal clntdir_year;
    private String clntdir_name;
    private String clntdir_qualifications;
    private BigDecimal clntdir_pct_holdg;
    private String clntdir_designation;

    /**
     * corporate auditors
     *
     */
    private BigDecimal clntaud_code;
    private BigDecimal clntaud_clnt_code;
    private String clntaud_year;
    private String clntaud_name;
    private String clntaud_qualifications;
    private String clntaud_telephone;
    private String clntaud_designation;

    /**
     *Client  Groups
     *
     */

    private BigDecimal grp_Code;
    private String grp_Name;
    private BigDecimal grp_Minimum;
    private BigDecimal grp_Maximum;

    private BigDecimal grpd_Code;
    private BigDecimal grpd_Clnt_Code;
    private BigDecimal grpd_Grp_Code;
    private BigDecimal SPR_CODE;
    private String SPR_NAME;
    private BigDecimal AGNT_CODE;
    private String SPR_SHT_DESC;

    private BigDecimal CLNT_CODE;
    private String CLNT_SHT_DESC;
    private String CLNT_NAME;
    private BigDecimal CLNT_CLNT_CODE;


    private BigDecimal CLNTY_CODE;
    private String CLNTY_CLNT_TYPE;
    private String CLNTY_CATEGORY;
    private String CLTN_CLIENT_TYPES;


    private BigDecimal CLN_CODE;
    private String CLN_CLNT_SHT_DESC;
    private String CLN_CLNT_NAME;
    private String CLN_CLNT_OTHER_NAMES;
    private String CLN_CLNT_ID_REG_NO;
    private Date CLN_CLNT_DOB;
    private String CLN_CLNT_PIN;
    private String CLN_CLNT_PHYSICAL_ADDRS;
    private String CLN_CLNT_POSTAL_ADDRS;
    private BigDecimal CLN_CLNT_TWN_CODE;
    private BigDecimal CLN_CLNT_COU_CODE;
    private String CLN_CLNT_TEL;
    private String CLN_CLNT_TEL2;
    private String CLN_CLNT_ACCNT_NO;
    private Date CLN_CLNT_WEF;
    private Date CLN_CLNT_WET;
    private String CLN_CLNT_CREATED_BY;
    private String PRO_DESC;
    private String CLN_CLNT_WETfrom;
    private String CLN_CLNT_DOBlov;
    private Date CLN_CLNT_WEFto;
    private String CLN_CLNT_WEFtolov;
    private String CLN_CLNT_FAX;
    private String SystemName;
    private String CLN_CLNT_CNTCT_EMAIL_1;
    private String holdingCompany;
    private String Sacco;
    private BigDecimal clientBankCode;
    private BigDecimal ClnCode;
    private String clientShtDesc;
    private String clientBankName;
    private BigDecimal clientBranchCode;
    private String ClientShtDesc;

    private BigDecimal userBranchCode;
    private String Default;


    private BigDecimal webProductCode;
    private BigDecimal webproductDetailsCode;
    private BigDecimal productCode;
    private String webProductDesc;
    private String productDesc;


    private String userName;
    private String UserRealName;
    private String userStatus;
    private BigDecimal webUserCode;


    private String clientName;
    private String WebproductDesc;
    private BigDecimal UserCode;
    private String UserName;
    private BigDecimal DrLimitAmount;
    private BigDecimal CrLimitAmount;
    private String polUse;
    private String endosUse;
    private BigDecimal webclientCode;
    private BigDecimal ProductCode;
    private String clnRemarks;

    private BigDecimal clnaCode;
    private String clnaShtDesc;
    private String clnaName;
    private String clnaRamarks;

    private String creditLimitAllowed;
    private BigDecimal creditLimit;
    private String locationName;

    private BigDecimal clntCode;
    private String clntShtDesc;
    private String clntOtherNames;
    private String clntIdRegNumber;
    private Date clntDateOfBirth;
    private String clientPin;
    private String clientPhysicalAddress;
    private String clientPostalAddress;
    private String emailAddress;
    private String clientStatus;
    private String clientTypes;
    private String clientTitle;
    private String clientDirect;
    private String clientGender;
    private String clientPassport;
    private String clientOccupation;
    private String clntClientTypes;
    private String clientNameEnt;
    private String bouncedCheque;
    private String maritalStatus;
    private String defaultModeOfComm;
    private BigDecimal payrollNo;
    private BigDecimal salMin;
    private BigDecimal salMax;
    private BigDecimal clntBpnCode;
    private String bpnName;

    private BigDecimal occCode;
    private BigDecimal occSecCode;
    private String occShtDesc;
    private String occName;
    private Date dlIssueDate;
    private String workPermit;
    private String telPay;
    private String clntIntTel;
    private String clntEmail2;
    
    //specializations
    
    private BigDecimal SPZ_CODE ;
    private String SPZ_SHT_DESC;
    private String SPZ_NAME;
    private String SPZ_OCC_CODE;
    //
    private BigDecimal gsmZipCode; 
    private BigDecimal  intZipCode ;
    private String staffNo;
    private String clientLevel;
    private String CLNTY_PERSON;
    private String pobCountry;
    private String pobState;
    private String pobTown;
    private String sourceOfIncome;
    private String sourceOfIncomeType;

    /**
    /**
     * Default Class Constructor
     */
    public Client2() {
        super();
    }

    private BigDecimal clnt_code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
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

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setIdRegNo(String idRegNo) {
        this.idRegNo = idRegNo;
    }

    public String getIdRegNo() {
        return idRegNo;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDob() {
        return dob;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
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

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getTownName() {
        return townName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setSpecialTerms(String specialTerms) {
        this.specialTerms = specialTerms;
    }

    public String getSpecialTerms() {
        return specialTerms;
    }

    public void setDeclinedProp(String declinedProp) {
        this.declinedProp = declinedProp;
    }

    public String getDeclinedProp() {
        return declinedProp;
    }

    public void setIncreasedPremium(String increasedPremium) {
        this.increasedPremium = increasedPremium;
    }

    public String getIncreasedPremium() {
        return increasedPremium;
    }

    public void setPolicyCancelled(String policyCancelled) {
        this.policyCancelled = policyCancelled;
    }

    public String getPolicyCancelled() {
        return policyCancelled;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getProposer() {
        return proposer;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setDomicileCountries(String domicileCountries) {
        this.domicileCountries = domicileCountries;
    }

    public String getDomicileCountries() {
        return domicileCountries;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    public Date getWef() {
        return wef;
    }

    public void setWet(Date wet) {
        this.wet = wet;
    }

    public Date getWet() {
        return wet;
    }

    public void setWithdrawalReason(String withdrawalReason) {
        this.withdrawalReason = withdrawalReason;
    }

    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    public void setSectorCode(BigDecimal sectorCode) {
        this.sectorCode = sectorCode;
    }

    public BigDecimal getSectorCode() {
        return sectorCode;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getBusiness() {
        return business;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setBankBranchCode(String bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public String getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setNonDirect(String nonDirect) {
        this.nonDirect = nonDirect;
    }

    public String getNonDirect() {
        return nonDirect;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getSms() {
        return sms;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setRunOff(String runOff) {
        this.runOff = runOff;
    }

    public String getRunOff() {
        return runOff;
    }

    public void setLoadedBy(String loadedBy) {
        this.loadedBy = loadedBy;
    }

    public String getLoadedBy() {
        return loadedBy;
    }

    public void setDirectClient(String directClient) {
        this.directClient = directClient;
    }

    public String getDirectClient() {
        return directClient;
    }

    public void setOldAccountNum(String oldAccountNum) {
        this.oldAccountNum = oldAccountNum;
    }

    public String getOldAccountNum() {
        return oldAccountNum;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setContactName1(String contactName1) {
        this.contactName1 = contactName1;
    }

    public String getContactName1() {
        return contactName1;
    }

    public void setContactPhone1(String contactPhone1) {
        this.contactPhone1 = contactPhone1;
    }

    public String getContactPhone1() {
        return contactPhone1;
    }

    public void setContactEmail1(String contactEmail1) {
        this.contactEmail1 = contactEmail1;
    }

    public String getContactEmail1() {
        return contactEmail1;
    }

    public void setContactName2(String contactName2) {
        this.contactName2 = contactName2;
    }

    public String getContactName2() {
        return contactName2;
    }

    public void setContactPhone2(String contactPhone2) {
        this.contactPhone2 = contactPhone2;
    }

    public String getContactPhone2() {
        return contactPhone2;
    }

    public void setContactEmail2(String contactEmail2) {
        this.contactEmail2 = contactEmail2;
    }

    public String getContactEmail2() {
        return contactEmail2;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setStsCode(BigDecimal stsCode) {
        this.stsCode = stsCode;
    }

    public BigDecimal getStsCode() {
        return stsCode;
    }

    public void setStsName(String stsName) {
        this.stsName = stsName;
    }

    public String getStsName() {
        return stsName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public void setAuditors(String auditors) {
        this.auditors = auditors;
    }

    public String getAuditors() {
        return auditors;
    }

    public void setParent_company(String parent_company) {
        this.parent_company = parent_company;
    }

    public String getParent_company() {
        return parent_company;
    }

    public void setCurrent_insurer(String current_insurer) {
        this.current_insurer = current_insurer;
    }

    public String getCurrent_insurer() {
        return current_insurer;
    }

    public void setProjected_business(BigDecimal projected_business) {
        this.projected_business = projected_business;
    }

    public BigDecimal getProjected_business() {
        return projected_business;
    }

    public void setDate_of_empl(Date date_of_empl) {
        this.date_of_empl = date_of_empl;
    }

    public Date getDate_of_empl() {
        return date_of_empl;
    }

    public void setDriving_licence(String driving_licence) {
        this.driving_licence = driving_licence;
    }

    public String getDriving_licence() {
        return driving_licence;
    }

    public void setClnt_code(BigDecimal clnt_code) {
        this.clnt_code = clnt_code;
    }

    public BigDecimal getClnt_code() {
        return clnt_code;
    }

    public void setClntdir_code(BigDecimal clntdir_code) {
        this.clntdir_code = clntdir_code;
    }

    public BigDecimal getClntdir_code() {
        return clntdir_code;
    }

    public void setClntdir_clnt_code(BigDecimal clntdir_clnt_code) {
        this.clntdir_clnt_code = clntdir_clnt_code;
    }

    public BigDecimal getClntdir_clnt_code() {
        return clntdir_clnt_code;
    }

    public void setClntdir_year(BigDecimal clntdir_year) {
        this.clntdir_year = clntdir_year;
    }

    public BigDecimal getClntdir_year() {
        return clntdir_year;
    }

    public void setClntdir_name(String clntdir_name) {
        this.clntdir_name = clntdir_name;
    }

    public String getClntdir_name() {
        return clntdir_name;
    }

    public void setClntdir_qualifications(String clntdir_qualifications) {
        this.clntdir_qualifications = clntdir_qualifications;
    }

    public String getClntdir_qualifications() {
        return clntdir_qualifications;
    }

    public void setClntdir_pct_holdg(BigDecimal clntdir_pct_holdg) {
        this.clntdir_pct_holdg = clntdir_pct_holdg;
    }

    public BigDecimal getClntdir_pct_holdg() {
        return clntdir_pct_holdg;
    }

    public void setClntdir_designation(String clntdir_designation) {
        this.clntdir_designation = clntdir_designation;
    }

    public String getClntdir_designation() {
        return clntdir_designation;
    }

    public void setClntaud_code(BigDecimal clntaud_code) {
        this.clntaud_code = clntaud_code;
    }

    public BigDecimal getClntaud_code() {
        return clntaud_code;
    }

    public void setClntaud_clnt_code(BigDecimal clntaud_clnt_code) {
        this.clntaud_clnt_code = clntaud_clnt_code;
    }

    public BigDecimal getClntaud_clnt_code() {
        return clntaud_clnt_code;
    }

    public void setClntaud_year(String clntaud_year) {
        this.clntaud_year = clntaud_year;
    }

    public String getClntaud_year() {
        return clntaud_year;
    }

    public void setClntaud_name(String clntaud_name) {
        this.clntaud_name = clntaud_name;
    }

    public String getClntaud_name() {
        return clntaud_name;
    }

    public void setClntaud_qualifications(String clntaud_qualifications) {
        this.clntaud_qualifications = clntaud_qualifications;
    }

    public String getClntaud_qualifications() {
        return clntaud_qualifications;
    }

    public void setClntaud_telephone(String clntaud_telephone) {
        this.clntaud_telephone = clntaud_telephone;
    }

    public String getClntaud_telephone() {
        return clntaud_telephone;
    }

    public void setClntaud_designation(String clntaud_designation) {
        this.clntaud_designation = clntaud_designation;
    }

    public String getClntaud_designation() {
        return clntaud_designation;
    }

    public void setParent_company_name(String parent_company_name) {
        this.parent_company_name = parent_company_name;
    }

    public String getParent_company_name() {
        return parent_company_name;
    }

    public void setBrnCode(BigDecimal brnCode) {
        this.brnCode = brnCode;
    }

    public BigDecimal getBrnCode() {
        return brnCode;
    }

    public void setBrnName(String brnName) {
        this.brnName = brnName;
    }

    public String getBrnName() {
        return brnName;
    }

    public void setGrp_Code(BigDecimal grp_Code) {
        this.grp_Code = grp_Code;
    }

    public BigDecimal getGrp_Code() {
        return grp_Code;
    }

    public void setGrp_Name(String grp_Name) {
        this.grp_Name = grp_Name;
    }

    public String getGrp_Name() {
        return grp_Name;
    }

    public void setGrp_Minimum(BigDecimal grp_Minimum) {
        this.grp_Minimum = grp_Minimum;
    }

    public BigDecimal getGrp_Minimum() {
        return grp_Minimum;
    }

    public void setGrp_Maximum(BigDecimal grp_Maximum) {
        this.grp_Maximum = grp_Maximum;
    }

    public BigDecimal getGrp_Maximum() {
        return grp_Maximum;
    }

    public void setGrpd_Code(BigDecimal grpd_Code) {
        this.grpd_Code = grpd_Code;
    }

    public BigDecimal getGrpd_Code() {
        return grpd_Code;
    }

    public void setGrpd_Clnt_Code(BigDecimal grpd_Clnt_Code) {
        this.grpd_Clnt_Code = grpd_Clnt_Code;
    }

    public BigDecimal getGrpd_Clnt_Code() {
        return grpd_Clnt_Code;
    }

    public void setGrpd_Grp_Code(BigDecimal grpd_Grp_Code) {
        this.grpd_Grp_Code = grpd_Grp_Code;
    }

    public BigDecimal getGrpd_Grp_Code() {
        return grpd_Grp_Code;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setAacOfficer(BigDecimal aacOfficer) {
        this.aacOfficer = aacOfficer;
    }

    public BigDecimal getAacOfficer() {
        return aacOfficer;
    }

    public void setAacOfficerName(String aacOfficerName) {
        this.aacOfficerName = aacOfficerName;
    }

    public String getAacOfficerName() {
        return aacOfficerName;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setHhCode(BigDecimal hhCode) {
        this.hhCode = hhCode;
    }

    public BigDecimal getHhCode() {
        return hhCode;
    }

    public void setHhName(String hhName) {
        this.hhName = hhName;
    }

    public String getHhName() {
        return hhName;
    }

    public void setHhCategory(String hhCategory) {
        this.hhCategory = hhCategory;
    }

    public String getHhCategory() {
        return hhCategory;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getHead() {
        return head;
    }

    public void setOldShtDesc(String oldShtDesc) {
        this.oldShtDesc = oldShtDesc;
    }

    public String getOldShtDesc() {
        return oldShtDesc;
    }

    public void setClientCell(String clientCell) {
        this.clientCell = clientCell;
    }

    public String getClientCell() {
        return clientCell;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setAnniversary(Date anniversary) {
        this.anniversary = anniversary;
    }

    public Date getAnniversary() {
        return anniversary;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public void setSPR_CODE(BigDecimal SPR_CODE) {
        this.SPR_CODE = SPR_CODE;
    }

    public BigDecimal getSPR_CODE() {
        return SPR_CODE;
    }

    public void setSPR_NAME(String SPR_NAME) {
        this.SPR_NAME = SPR_NAME;
    }

    public String getSPR_NAME() {
        return SPR_NAME;
    }

    public void setAGNT_CODE(BigDecimal AGNT_CODE) {
        this.AGNT_CODE = AGNT_CODE;
    }

    public BigDecimal getAGNT_CODE() {
        return AGNT_CODE;
    }

    public void setSPR_SHT_DESC(String SPR_SHT_DESC) {
        this.SPR_SHT_DESC = SPR_SHT_DESC;
    }

    public String getSPR_SHT_DESC() {
        return SPR_SHT_DESC;
    }

    public void setCLNT_CODE(BigDecimal CLNT_CODE) {
        this.CLNT_CODE = CLNT_CODE;
    }

    public BigDecimal getCLNT_CODE() {
        return CLNT_CODE;
    }

    public void setCLNT_SHT_DESC(String CLNT_SHT_DESC) {
        this.CLNT_SHT_DESC = CLNT_SHT_DESC;
    }

    public String getCLNT_SHT_DESC() {
        return CLNT_SHT_DESC;
    }

    public void setCLNT_NAME(String CLNT_NAME) {
        this.CLNT_NAME = CLNT_NAME;
    }

    public String getCLNT_NAME() {
        return CLNT_NAME;
    }

    public void setCLNT_CLNT_CODE(BigDecimal CLNT_CLNT_CODE) {
        this.CLNT_CLNT_CODE = CLNT_CLNT_CODE;
    }

    public BigDecimal getCLNT_CLNT_CODE() {
        return CLNT_CLNT_CODE;
    }

    public void setCLNTY_CODE(BigDecimal CLNTY_CODE) {
        this.CLNTY_CODE = CLNTY_CODE;
    }

    public BigDecimal getCLNTY_CODE() {
        return CLNTY_CODE;
    }

    public void setCLNTY_CLNT_TYPE(String CLNTY_CLNT_TYPE) {
        this.CLNTY_CLNT_TYPE = CLNTY_CLNT_TYPE;
    }

    public String getCLNTY_CLNT_TYPE() {
        return CLNTY_CLNT_TYPE;
    }

    public void setCLNTY_CATEGORY(String CLNTY_CATEGORY) {
        this.CLNTY_CATEGORY = CLNTY_CATEGORY;
    }

    public String getCLNTY_CATEGORY() {
        return CLNTY_CATEGORY;
    }


    public void setCLTN_CLIENT_TYPES(String CLTN_CLIENT_TYPES) {
        this.CLTN_CLIENT_TYPES = CLTN_CLIENT_TYPES;
    }

    public String getCLTN_CLIENT_TYPES() {
        return CLTN_CLIENT_TYPES;
    }

    public void setCLN_CODE(BigDecimal CLN_CODE) {
        this.CLN_CODE = CLN_CODE;
    }

    public BigDecimal getCLN_CODE() {
        return CLN_CODE;
    }

    public void setCLN_CLNT_SHT_DESC(String CLN_CLNT_SHT_DESC) {
        this.CLN_CLNT_SHT_DESC = CLN_CLNT_SHT_DESC;
    }

    public String getCLN_CLNT_SHT_DESC() {
        return CLN_CLNT_SHT_DESC;
    }

    public void setCLN_CLNT_NAME(String CLN_CLNT_NAME) {
        this.CLN_CLNT_NAME = CLN_CLNT_NAME;
    }

    public String getCLN_CLNT_NAME() {
        return CLN_CLNT_NAME;
    }

    public void setCLN_CLNT_OTHER_NAMES(String CLN_CLNT_OTHER_NAMES) {
        this.CLN_CLNT_OTHER_NAMES = CLN_CLNT_OTHER_NAMES;
    }

    public String getCLN_CLNT_OTHER_NAMES() {
        return CLN_CLNT_OTHER_NAMES;
    }

    public void setCLN_CLNT_ID_REG_NO(String CLN_CLNT_ID_REG_NO) {
        this.CLN_CLNT_ID_REG_NO = CLN_CLNT_ID_REG_NO;
    }

    public String getCLN_CLNT_ID_REG_NO() {
        return CLN_CLNT_ID_REG_NO;
    }

    public void setCLN_CLNT_DOB(Date CLN_CLNT_DOB) {
        this.CLN_CLNT_DOB = CLN_CLNT_DOB;
    }

    public Date getCLN_CLNT_DOB() {
        return CLN_CLNT_DOB;
    }

    public void setCLN_CLNT_PIN(String CLN_CLNT_PIN) {
        this.CLN_CLNT_PIN = CLN_CLNT_PIN;
    }

    public String getCLN_CLNT_PIN() {
        return CLN_CLNT_PIN;
    }

    public void setCLN_CLNT_PHYSICAL_ADDRS(String CLN_CLNT_PHYSICAL_ADDRS) {
        this.CLN_CLNT_PHYSICAL_ADDRS = CLN_CLNT_PHYSICAL_ADDRS;
    }

    public String getCLN_CLNT_PHYSICAL_ADDRS() {
        return CLN_CLNT_PHYSICAL_ADDRS;
    }

    public void setCLN_CLNT_POSTAL_ADDRS(String CLN_CLNT_POSTAL_ADDRS) {
        this.CLN_CLNT_POSTAL_ADDRS = CLN_CLNT_POSTAL_ADDRS;
    }

    public String getCLN_CLNT_POSTAL_ADDRS() {
        return CLN_CLNT_POSTAL_ADDRS;
    }

    public void setCLN_CLNT_TWN_CODE(BigDecimal CLN_CLNT_TWN_CODE) {
        this.CLN_CLNT_TWN_CODE = CLN_CLNT_TWN_CODE;
    }

    public BigDecimal getCLN_CLNT_TWN_CODE() {
        return CLN_CLNT_TWN_CODE;
    }

    public void setCLN_CLNT_COU_CODE(BigDecimal CLN_CLNT_COU_CODE) {
        this.CLN_CLNT_COU_CODE = CLN_CLNT_COU_CODE;
    }

    public BigDecimal getCLN_CLNT_COU_CODE() {
        return CLN_CLNT_COU_CODE;
    }

    public void setCLN_CLNT_TEL(String CLN_CLNT_TEL) {
        this.CLN_CLNT_TEL = CLN_CLNT_TEL;
    }

    public String getCLN_CLNT_TEL() {
        return CLN_CLNT_TEL;
    }

    public void setCLN_CLNT_ACCNT_NO(String CLN_CLNT_ACCNT_NO) {
        this.CLN_CLNT_ACCNT_NO = CLN_CLNT_ACCNT_NO;
    }

    public String getCLN_CLNT_ACCNT_NO() {
        return CLN_CLNT_ACCNT_NO;
    }

    public void setCLN_CLNT_WEF(Date CLN_CLNT_WEF) {
        this.CLN_CLNT_WEF = CLN_CLNT_WEF;
    }

    public Date getCLN_CLNT_WEF() {
        return CLN_CLNT_WEF;
    }

    public void setCLN_CLNT_WET(Date CLN_CLNT_WET) {
        this.CLN_CLNT_WET = CLN_CLNT_WET;
    }

    public Date getCLN_CLNT_WET() {
        return CLN_CLNT_WET;
    }

    public void setCLN_CLNT_CREATED_BY(String CLN_CLNT_CREATED_BY) {
        this.CLN_CLNT_CREATED_BY = CLN_CLNT_CREATED_BY;
    }

    public String getCLN_CLNT_CREATED_BY() {
        return CLN_CLNT_CREATED_BY;
    }

    public void setPRO_DESC(String PRO_DESC) {
        this.PRO_DESC = PRO_DESC;
    }

    public String getPRO_DESC() {
        return PRO_DESC;
    }

    public void setCLN_CLNT_WETfrom(String CLN_CLNT_WETfrom) {
        this.CLN_CLNT_WETfrom = CLN_CLNT_WETfrom;
    }

    public String getCLN_CLNT_WETfrom() {
        return CLN_CLNT_WETfrom;
    }

    public void setCLN_CLNT_DOBlov(String CLN_CLNT_DOBlov) {
        this.CLN_CLNT_DOBlov = CLN_CLNT_DOBlov;
    }

    public String getCLN_CLNT_DOBlov() {
        return CLN_CLNT_DOBlov;
    }

    public void setCLN_CLNT_WEFto(Date CLN_CLNT_WEFto) {
        this.CLN_CLNT_WEFto = CLN_CLNT_WEFto;
    }

    public Date getCLN_CLNT_WEFto() {
        return CLN_CLNT_WEFto;
    }

    public void setCLN_CLNT_WEFtolov(String CLN_CLNT_WEFtolov) {
        this.CLN_CLNT_WEFtolov = CLN_CLNT_WEFtolov;
    }

    public String getCLN_CLNT_WEFtolov() {
        return CLN_CLNT_WEFtolov;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setSystemName(String SystemName) {
        this.SystemName = SystemName;
    }

    public String getSystemName() {
        return SystemName;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isSelect() {
        return select;
    }

    public void setCLN_CLNT_TEL2(String CLN_CLNT_TEL2) {
        this.CLN_CLNT_TEL2 = CLN_CLNT_TEL2;
    }

    public String getCLN_CLNT_TEL2() {
        return CLN_CLNT_TEL2;
    }

    public void setCLN_CLNT_FAX(String CLN_CLNT_FAX) {
        this.CLN_CLNT_FAX = CLN_CLNT_FAX;
    }

    public String getCLN_CLNT_FAX() {
        return CLN_CLNT_FAX;
    }

    public void setCLN_CLNT_CNTCT_EMAIL_1(String CLN_CLNT_CNTCT_EMAIL_1) {
        this.CLN_CLNT_CNTCT_EMAIL_1 = CLN_CLNT_CNTCT_EMAIL_1;
    }

    public String getCLN_CLNT_CNTCT_EMAIL_1() {
        return CLN_CLNT_CNTCT_EMAIL_1;
    }

    public void setHoldingCompany(String holdingCompany) {
        this.holdingCompany = holdingCompany;
    }

    public String getHoldingCompany() {
        return holdingCompany;
    }

    public void setSacco(String Sacco) {
        this.Sacco = Sacco;
    }

    public String getSacco() {
        return Sacco;
    }

    public void setClientBankCode(BigDecimal clientBankCode) {
        this.clientBankCode = clientBankCode;
    }

    public BigDecimal getClientBankCode() {
        return clientBankCode;
    }

    public void setClnCode(BigDecimal ClnCode) {
        this.ClnCode = ClnCode;
    }

    public BigDecimal getClnCode() {
        return ClnCode;
    }

    public void setClientShtDesc(String clientShtDesc) {
        this.clientShtDesc = clientShtDesc;
    }

    public String getClientShtDesc() {
        return clientShtDesc;
    }

    public void setClientBankName(String clientBankName) {
        this.clientBankName = clientBankName;
    }

    public String getClientBankName() {
        return clientBankName;
    }

    public void setClientBranchCode(BigDecimal clientBranchCode) {
        this.clientBranchCode = clientBranchCode;
    }

    public BigDecimal getClientBranchCode() {
        return clientBranchCode;
    }

    public void setClientShtDesc1(String ClientShtDesc) {
        this.ClientShtDesc = ClientShtDesc;
    }

    public String getClientShtDesc1() {
        return ClientShtDesc;
    }

    public void setUserBranchCode(BigDecimal userBranchCode) {
        this.userBranchCode = userBranchCode;
    }

    public BigDecimal getUserBranchCode() {
        return userBranchCode;
    }

    public void setDefault(String Default) {
        this.Default = Default;
    }

    public String getDefault() {
        return Default;
    }

    public void setWebProductCode(BigDecimal webProductCode) {
        this.webProductCode = webProductCode;
    }

    public BigDecimal getWebProductCode() {
        return webProductCode;
    }

    public void setWebproductDetailsCode(BigDecimal webproductDetailsCode) {
        this.webproductDetailsCode = webproductDetailsCode;
    }

    public BigDecimal getWebproductDetailsCode() {
        return webproductDetailsCode;
    }

    public void setProductCode(BigDecimal productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getProductCode() {
        return productCode;
    }

    public void setWebProductDesc(String webProductDesc) {
        this.webProductDesc = webProductDesc;
    }

    public String getWebProductDesc() {
        return webProductDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserRealName(String UserRealName) {
        this.UserRealName = UserRealName;
    }

    public String getUserRealName() {
        return UserRealName;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setWebUserCode(BigDecimal webUserCode) {
        this.webUserCode = webUserCode;
    }

    public BigDecimal getWebUserCode() {
        return webUserCode;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setWebproductDesc(String WebproductDesc) {
        this.WebproductDesc = WebproductDesc;
    }

    public String getWebproductDesc() {
        return WebproductDesc;
    }

    public void setUserCode(BigDecimal UserCode) {
        this.UserCode = UserCode;
    }

    public BigDecimal getUserCode1() {
        return UserCode;
    }

    public void setUserName1(String UserName) {
        this.UserName = UserName;
    }

    public String getUserName1() {
        return UserName;
    }

    public void setDrLimitAmount(BigDecimal DrLimitAmount) {
        this.DrLimitAmount = DrLimitAmount;
    }

    public BigDecimal getDrLimitAmount() {
        return DrLimitAmount;
    }

    public void setCrLimitAmount(BigDecimal CrLimitAmount) {
        this.CrLimitAmount = CrLimitAmount;
    }

    public BigDecimal getCrLimitAmount() {
        return CrLimitAmount;
    }

    public void setPolUse(String polUse) {
        this.polUse = polUse;
    }

    public String getPolUse() {
        return polUse;
    }

    public void setEndosUse(String endosUse) {
        this.endosUse = endosUse;
    }

    public String getEndosUse() {
        return endosUse;
    }

    public void setWebclientCode(BigDecimal webclientCode) {
        this.webclientCode = webclientCode;
    }

    public BigDecimal getWebclientCode() {
        return webclientCode;
    }

    public void setProductCode1(BigDecimal ProductCode) {
        this.ProductCode = ProductCode;
    }

    public BigDecimal getProductCode1() {
        return ProductCode;
    }


    public void setClnRemarks(String clnRemarks) {
        this.clnRemarks = clnRemarks;
    }

    public String getClnRemarks() {
        return clnRemarks;
    }

    public void setCouZipCode(String couZipCode) {
        this.couZipCode = couZipCode;
    }

    public String getCouZipCode() {
        return couZipCode;
    }

    public void setClnaCode(BigDecimal clnaCode) {
        this.clnaCode = clnaCode;
    }

    public BigDecimal getClnaCode() {
        return clnaCode;
    }

    public void setClnaShtDesc(String clnaShtDesc) {
        this.clnaShtDesc = clnaShtDesc;
    }

    public String getClnaShtDesc() {
        return clnaShtDesc;
    }

    public void setClnaName(String clnaName) {
        this.clnaName = clnaName;
    }

    public String getClnaName() {
        return clnaName;
    }


    public void setClnaRamarks(String clnaRamarks) {
        this.clnaRamarks = clnaRamarks;
    }

    public String getClnaRamarks() {
        return clnaRamarks;
    }

    public void setCreditLimitAllowed(String creditLimitAllowed) {
        this.creditLimitAllowed = creditLimitAllowed;
    }

    public String getCreditLimitAllowed() {
        return creditLimitAllowed;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setClntCode(BigDecimal clntCode) {
        this.clntCode = clntCode;
    }

    public BigDecimal getClntCode() {
        return clntCode;
    }

    public void setClntShtDesc(String clntShtDesc) {
        this.clntShtDesc = clntShtDesc;
    }

    public String getClntShtDesc() {
        return clntShtDesc;
    }

    public void setClntOtherNames(String clntOtherNames) {
        this.clntOtherNames = clntOtherNames;
    }

    public String getClntOtherNames() {
        return clntOtherNames;
    }

    public void setClntIdRegNumber(String clntIdRegNumber) {
        this.clntIdRegNumber = clntIdRegNumber;
    }

    public String getClntIdRegNumber() {
        return clntIdRegNumber;
    }

    public void setClntDateOfBirth(Date clntDateOfBirth) {
        this.clntDateOfBirth = clntDateOfBirth;
    }

    public Date getClntDateOfBirth() {
        return clntDateOfBirth;
    }

    public void setClientPin(String clientPin) {
        this.clientPin = clientPin;
    }

    public String getClientPin() {
        return clientPin;
    }

    public void setClientPhysicalAddress(String clientPhysicalAddress) {
        this.clientPhysicalAddress = clientPhysicalAddress;
    }

    public String getClientPhysicalAddress() {
        return clientPhysicalAddress;
    }

    public void setClientPostalAddress(String clientPostalAddress) {
        this.clientPostalAddress = clientPostalAddress;
    }

    public String getClientPostalAddress() {
        return clientPostalAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientTypes(String clientTypes) {
        this.clientTypes = clientTypes;
    }

    public String getClientTypes() {
        return clientTypes;
    }

    public void setClientTitle(String clientTitle) {
        this.clientTitle = clientTitle;
    }

    public String getClientTitle() {
        return clientTitle;
    }

    public void setClientDirect(String clientDirect) {
        this.clientDirect = clientDirect;
    }

    public String getClientDirect() {
        return clientDirect;
    }

    public void setClientGender(String clientGender) {
        this.clientGender = clientGender;
    }

    public String getClientGender() {
        return clientGender;
    }

    public void setClientPassport(String clientPassport) {
        this.clientPassport = clientPassport;
    }

    public String getClientPassport() {
        return clientPassport;
    }

    public void setClientOccupation(String clientOccupation) {
        this.clientOccupation = clientOccupation;
    }

    public String getClientOccupation() {
        return clientOccupation;
    }

    public void setClntClientTypes(String clntClientTypes) {
        this.clntClientTypes = clntClientTypes;
    }

    public String getClntClientTypes() {
        return clntClientTypes;
    }

    public void setClientNameEnt(String clientNameEnt) {
        this.clientNameEnt = clientNameEnt;
    }

    public String getClientNameEnt() {
        return clientNameEnt;
    }

    public void setBouncedCheque(String bouncedCheque) {
        this.bouncedCheque = bouncedCheque;
    }

    public String getBouncedCheque() {
        return bouncedCheque;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setDefaultModeOfComm(String defaultModeOfComm) {
        this.defaultModeOfComm = defaultModeOfComm;
    }

    public String getDefaultModeOfComm() {
        return defaultModeOfComm;
    }

   

    public void setSalMin(BigDecimal salMin) {
        this.salMin = salMin;
    }

    public BigDecimal getSalMin() {
        return salMin;
    }

    public void setSalMax(BigDecimal salMax) {
        this.salMax = salMax;
    }

    public BigDecimal getSalMax() {
        return salMax;
    }

    public void setClntBpnCode(BigDecimal clntBpnCode) {
        this.clntBpnCode = clntBpnCode;
    }

    public BigDecimal getClntBpnCode() {
        return clntBpnCode;
    }

    public void setBpnName(String bpnName) {
        this.bpnName = bpnName;
    }

    public String getBpnName() {
        return bpnName;
    }

    public void setOccCode(BigDecimal occCode) {
        this.occCode = occCode;
    }

    public BigDecimal getOccCode() {
        return occCode;
    }

    public void setOccSecCode(BigDecimal occSecCode) {
        this.occSecCode = occSecCode;
    }

    public BigDecimal getOccSecCode() {
        return occSecCode;
    }

    public void setOccShtDesc(String occShtDesc) {
        this.occShtDesc = occShtDesc;
    }

    public String getOccShtDesc() {
        return occShtDesc;
    }

    public void setOccName(String occName) {
        this.occName = occName;
    }

    public String getOccName() {
        return occName;
    }

    public void setDlIssueDate(Date dlIssueDate) {
        this.dlIssueDate = dlIssueDate;
    }

    public Date getDlIssueDate() {
        return dlIssueDate;
    }


    public void setWorkPermit(String workPermit) {
        this.workPermit = workPermit;
    }

    public String getWorkPermit() {
        return workPermit;
    }

    public void setTelPay(String telPay) {
        this.telPay = telPay;
    }

    public String getTelPay() {
        return telPay;
    }

    public void setPayrollNo(BigDecimal payrollNo) {
        this.payrollNo = payrollNo;
    }

    public BigDecimal getPayrollNo() {
        return payrollNo;
    }

    public void setClntIntTel(String clntIntTel) {
        this.clntIntTel = clntIntTel;
    }

    public String getClntIntTel() {
        return clntIntTel;
    }

    public void setClntEmail2(String clntEmail2) {
        this.clntEmail2 = clntEmail2;
    }

    public String getClntEmail2() {
        return clntEmail2;
    }

    public void setSPZ_CODE(BigDecimal SPZ_CODE) {
        this.SPZ_CODE = SPZ_CODE;
    }

    public BigDecimal getSPZ_CODE() {
        return SPZ_CODE;
    }

    public void setSPZ_SHT_DESC(String SPZ_SHT_DESC) {
        this.SPZ_SHT_DESC = SPZ_SHT_DESC;
    }

    public String getSPZ_SHT_DESC() {
        return SPZ_SHT_DESC;
    }

    public void setSPZ_NAME(String SPZ_NAME) {
        this.SPZ_NAME = SPZ_NAME;
    }

    public String getSPZ_NAME() {
        return SPZ_NAME;
    }

    public void setSPZ_OCC_CODE(String SPZ_OCC_CODE) {
        this.SPZ_OCC_CODE = SPZ_OCC_CODE;
    }

    public String getSPZ_OCC_CODE() {
        return SPZ_OCC_CODE;
    }

    public void setGsmZipCode(BigDecimal gsmZipCode) {
        this.gsmZipCode = gsmZipCode;
    }

    public BigDecimal getGsmZipCode() {
        return gsmZipCode;
    }

    public void setIntZipCode(BigDecimal intZipCode) {
        this.intZipCode = intZipCode;
    }

    public BigDecimal getIntZipCode() {
        return intZipCode;
    }

    void setStaffNo(String s) {
        this.staffNo=s;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setClientLevel(String clientLevel) {
        this.clientLevel = clientLevel;
    }

    public String getClientLevel() {
        return clientLevel;
    }

    void setCLNTY_PERSON(String string) {
        this.CLNTY_PERSON=string;
    }

    public String getCLNTY_PERSON() {
        return CLNTY_PERSON;
    }

    public void setPobCountry(String pobCountry) {
        this.pobCountry = pobCountry;
    }

    public String getPobCountry() {
        return pobCountry;
    }

    public void setPobState(String pobState) {
        this.pobState = pobState;
    }

    public String getPobState() {
        return pobState;
    }

    public void setPobTown(String pobTown) {
        this.pobTown = pobTown;
    }

    public String getPobTown() {
        return pobTown;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncomeType(String sourceOfIncomeType) {
        this.sourceOfIncomeType = sourceOfIncomeType;
    }

    public String getSourceOfIncomeType() {
        return sourceOfIncomeType;
    }
}
