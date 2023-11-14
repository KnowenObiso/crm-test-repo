/* Start Entity's definition Agent 
 @author dancan kavagi  */
package TurnQuest.view.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Blob;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name = "tqc_agencies", schema = "TQ_CRM") 
public class Agent implements Serializable {
	  @Id  
	  @Column(name = "agn_code", nullable = false)   
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AgentNoGen")
    @GenericGenerator(name = "AgentNoGen", strategy = "TurnQuest.view.models.AgentNoGen") 
    private BigDecimal code;
	  @Column(name = "agn_act_code", length = 22, nullable = true) 
    private BigDecimal	actCode;

	  @Column(name = "agn_sht_desc", length = 60, nullable = false) 
    private String	shtDesc;

	  @Column(name = "agn_name", length = 100, nullable = false) 
    private String	name;

	  @Column(name = "agn_physical_address", length = 250, nullable = true) 
    private String	physicalAddress;

	  @Column(name = "agn_postal_address", length = 50, nullable = true) 
    private String	postalAddress;

	  @Column(name = "agn_twn_code", length = 22, nullable = true) 
    private BigDecimal	twnCode;

	  @Column(name = "agn_cou_code", length = 22, nullable = true) 
    private BigDecimal	couCode;

	  @Column(name = "agn_email_address", length = 250, nullable = true) 
    private String	emailAddress;

	  @Column(name = "agn_web_address", length = 50, nullable = true) 
    private String	webAddress;

	  @Column(name = "agn_zip", length = 15, nullable = true) 
    private String	zip;

	  @Column(name = "agn_contact_person", length = 60, nullable = true) 
    private String	contactPerson;

	  @Column(name = "agn_contact_title", length = 60, nullable = true) 
    private String	contactTitle;

	  @Column(name = "agn_tel1", length = 100, nullable = true) 
    private String	tel1;

	  @Column(name = "agn_tel2", length = 100, nullable = true) 
    private String	tel2;

	  @Column(name = "agn_fax", length = 100, nullable = true) 
    private String	fax;

	  @Column(name = "agn_acc_no", length = 20, nullable = true) 
    private String	accNo;

	  @Column(name = "agn_pin", length = 25, nullable = true) 
    private String	pin;

	  @Column(name = "agn_agent_commission", length = 22, nullable = true) 
    private BigDecimal	agentCommission;

	  @Column(name = "agn_credit_allowed", length = 1, nullable = true) 
    private String	creditAllowed;

	  @Column(name = "agn_agent_wht_tax", length = 22, nullable = true) 
    private BigDecimal	agentWhtTax;

	  @Column(name = "agn_print_dbnote", length = 1, nullable = true) 
    private String	printDbnote;

	  @Column(name = "agn_status", length = 10, nullable = true) 
    private String	status;

	  @Column(name = "agn_date_created", length = 7, nullable = true) 
    private Timestamp	dateCreated;

	  @Column(name = "agn_created_by", length = 40, nullable = true) 
    private String	createdBy;

	  @Column(name = "agn_reg_code", length = 15, nullable = true) 
    private String	regCode;

	  @Column(name = "agn_comm_reserve_rate", length = 22, nullable = true) 
    private BigDecimal	commReserveRate;

	  @Column(name = "agn_annual_budget", length = 22, nullable = true) 
    private BigDecimal	annualBudget;

	  @Column(name = "agn_status_eff_date", length = 7, nullable = true) 
    private Timestamp	statusEffDate;

	  @Column(name = "agn_credit_period", length = 22, nullable = true) 
    private BigDecimal	creditPeriod;

	  @Column(name = "agn_comm_stat_eff_dt", length = 7, nullable = true) 
    private Timestamp	commStatEffDt;

	  @Column(name = "agn_comm_status_dt", length = 7, nullable = true) 
    private Timestamp	commStatusDt;

	  @Column(name = "agn_comm_allowed", length = 1, nullable = true) 
    private String	commAllowed;

	  @Column(name = "agn_checked", length = 1, nullable = true) 
    private String	checked;

	  @Column(name = "agn_checked_by", length = 30, nullable = true) 
    private String	checkedBy;

	  @Column(name = "agn_check_date", length = 7, nullable = true) 
    private Timestamp	checkDate;

	  @Column(name = "agn_comp_comm_arrears", length = 1, nullable = true) 
    private String	compCommArrears;

	  @Column(name = "agn_reinsurer", length = 1, nullable = true) 
    private String	reinsurer;

	  @Column(name = "agn_brn_code", length = 22, nullable = false) 
    private BigDecimal	brnCode;

	  @Column(name = "agn_town", length = 50, nullable = true) 
    private String	town;

	  @Column(name = "agn_country", length = 30, nullable = true) 
    private String	country;

	  @Column(name = "agn_status_desc", length = 30, nullable = true) 
    private String	statusDesc;

	  @Column(name = "agn_id_no", length = 20, nullable = true) 
    private String	idNo;

	  @Column(name = "agn_con_code", length = 10, nullable = true) 
    private String	conCode;

	  @Column(name = "agn_agn_code", length = 22, nullable = true) 
    private BigDecimal	agnCode;

	  @Column(name = "agn_sms_tel", length = 35, nullable = true) 
    private String	smsTel;

	  @Column(name = "agn_ahc_code", length = 22, nullable = true) 
    private BigDecimal	ahcCode;

	  @Column(name = "agn_sec_code", length = 22, nullable = true) 
    private BigDecimal	secCode;

	  @Column(name = "agn_agnc_class_code", length = 25, nullable = true) 
    private String	agncClassCode;

	  @Column(name = "agn_expiry_date", length = 7, nullable = true) 
    private Timestamp	expiryDate;

	  @Column(name = "agn_license_no", length = 25, nullable = true) 
    private String	licenseNo;

	  @Column(name = "agn_runoff", length = 1, nullable = false) 
    private String	runoff;

	  @Column(name = "agn_licensed", length = 1, nullable = true) 
    private String	licensed;

	  @Column(name = "agn_license_grace_pr", length = 22, nullable = true) 
    private BigDecimal	licenseGracePr;

	  @Column(name = "agn_old_acc_no", length = 20, nullable = true) 
    private String	oldAccNo;

	  @Column(name = "agn_status_remarks", length = 20, nullable = true) 
    private String	statusRemarks;

	  @Column(name = "agn_osd_code", length = 10, nullable = true) 
    private String	osdCode;

	  @Column(name = "agn_bbr_acc_code", length = 22, nullable = true) 
    private BigDecimal	bbrAccCode;

	  @Column(name = "agn_bbr_code", length = 22, nullable = true) 
    private BigDecimal	bbrCode;

	  @Column(name = "agn_bank_acc_no", length = 30, nullable = true) 
    private String	bankAccNo;

	  @Column(name = "agn_unique_prefix", length = 30, nullable = true) 
    private String	uniquePrefix;

	  @Column(name = "agn_state_code", length = 22, nullable = true) 
    private BigDecimal	stateCode;

	  @Column(name = "agn_crdt_rting", length = 10, nullable = true) 
    private String	crdtRting;

	  @Column(name = "agn_clnt_code", length = 22, nullable = true) 
    private BigDecimal	clntCode;

	  @Column(name = "agn_birth_date", length = 7, nullable = true) 
    private Timestamp	birthDate;

	  @Column(name = "agn_credit_limit", length = 22, nullable = true) 
    private BigDecimal	creditLimit;

	  @Column(name = "agn_bru_code", length = 22, nullable = true) 
    private BigDecimal	bruCode;

	  @Column(name = "agn_local_international", length = 1, nullable = true) 
    private String	localInternational;

	  @Column(name = "agn_regulator_number", length = 200, nullable = true) 
    private String	regulatorNumber;

	  @Column(name = "agn_authorised", length = 1, nullable = true) 
    private String	authorised;

	  @Column(name = "agn_authorised_by", length = 200, nullable = true) 
    private String	authorisedBy;

	  @Column(name = "agn_authorised_date", length = 7, nullable = true) 
    private Timestamp	authorisedDate;

	  @Column(name = "agn_rorg_code", length = 22, nullable = true) 
    private BigDecimal	rorgCode;

	  @Column(name = "agn_ors_code", length = 22, nullable = true) 
    private BigDecimal	orsCode;

	  @Column(name = "agn_allocate_cert", length = 1, nullable = true) 
    private String	allocateCert;

	  @Column(name = "agn_bounced_chq", length = 1, nullable = true) 
    private String	bouncedChq;

	  @Column(name = "agn_bpn_code", length = 22, nullable = true) 
    private BigDecimal	bpnCode;

	  @Column(name = "agn_agent_type", length = 20, nullable = true) 
    private String	agentType;

	  @Column(name = "agn_group", length = 22, nullable = true) 
    private String	group;

	  @Column(name = "agn_subagent", length = 200, nullable = true) 
    private String	subagent;

	  @Column(name = "agn_main_agn_code", length = 22, nullable = true) 
    private BigDecimal	mainAgnCode;

	  @Column(name = "agn_payee", length = 200, nullable = true) 
    private String	payee;

	  @Column(name = "agn_enable_web_edit", length = 1, nullable = false) 
    private String	enableWebEdit;

	  @Column(name = "agn_account_manager", length = 22, nullable = true) 
    private BigDecimal	accountManager;

	  @Column(name = "agn_default_comm_mode", length = 20, nullable = true) 
    private String	defaultCommMode;

	  @Column(name = "agn_vat_applicable", length = 1, nullable = true) 
    private String	vatApplicable;

	  @Column(name = "agn_whtax_applicable", length = 1, nullable = true) 
    private String	whtaxApplicable;

	  @Column(name = "agn_tel_pay", length = 35, nullable = true) 
    private String	telPay;

	  @Column(name = "agn_payment_freq", length = 50, nullable = true) 
    private String	paymentFreq;

	  @Column(name = "agn_default_cpm_mode", length = 20, nullable = true) 
    private String	defaultCpmMode;

	  @Column(name = "agn_pymt_validated", length = 1, nullable = true) 
    private String	pymtValidated;

	  @Column(name = "agn_qualification", length = 100, nullable = true) 
    private String	qualification;

	  @Column(name = "agn_marital_status", length = 5, nullable = true) 
    private String	maritalStatus;

	  @Column(name = "agn_benefit_start_date", length = 7, nullable = true) 
    private Timestamp	benefitStartDate;

	  @Column(name = "agn_id_no_doc_used", length = 20, nullable = true) 
    private String	idNoDocUsed;

	  @Column(name = "agn_agnty_code", length = 22, nullable = true) 
    private BigDecimal	agntyCode;

	  @Column(name = "agn_sbu_code", length = 22, nullable = true) 
    private BigDecimal	sbuCode;

	  @Column(name = "agn_comm_levy_app", length = 1, nullable = true) 
    private String	commLevyApp;

	  @Column(name = "agn_comm_levy_rate", length = 22, nullable = true) 
    private BigDecimal	commLevyRate;

	  @Column(name = "agn_brr_code", length = 22, nullable = true) 
    private BigDecimal	brrCode;

	  @Column(name = "agn_brr_name", length = 100, nullable = true) 
    private String	brrName;

	  @Column(name = "agn_auth_name", length = 100, nullable = true) 
    private String	authName;

	  @Column(name = "agn_updated_by", length = 100, nullable = true) 
    private String	updatedBy;

	  @Column(name = "agn_updated_on", length = 7, nullable = true) 
    private Timestamp	updatedOn;

	  @Column(name = "agn_ent_code", length = 22, nullable = true) 
    private BigDecimal	entCode;

	  @Column(name = "agn_gender", length = 1, nullable = true) 
    private String	gender;

	  @Column(name = "agn_ira_regno", length = 20, nullable = true) 
    private String	iraRegno;

	  @Column(name = "agn_principal_officer", length = 100, nullable = true) 
    private String	principalOfficer;

	  @Column(name = "agn_passport_no", length = 80, nullable = true) 
    private String	passportNo;

	  @Column(name = "agn_validation_source", length = 80, nullable = true) 
    private String	validationSource;

	  @Column(name = "agn_iprs_validated", length = 1, nullable = true) 
    private String	iprsValidated;

	  @Column(name = "agn_pymt_detail", length = 22, nullable = true) 
    private BigDecimal	pymtDetail;

	  @Column(name = "agn_crdt_limit", length = 22, nullable = true) 
    private BigDecimal	crdtLimit;

	  @Column(name = "agn_inst_comm_allwd", length = 1, nullable = true) 
    private String	instCommAllwd;

	  @Column(name = "agn_paydetail_validated", length = 1, nullable = true) 
    private String	paydetailValidated;

	  @Column(name = "agn_gl_rcvbl_acc_no", length = 40, nullable = true) 
    private String	glRcvblAccNo;

	  @Column(name = "agn_forgn", length = 20, nullable = true) 
    private String	forgn;

	  @Column(name = "agn_inique_prefix", length = 20, nullable = true) 
    private String	iniquePrefix;

	  @Column(name = "agn_cpm_mode_desc", length = 40, nullable = true) 
    private String	cpmModeDesc;

	  @Column(name = "agn_related_party", length = 10, nullable = true) 
    private String	relatedParty;



    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getActCode() {
        return actCode;
    }

    public void setActCode(BigDecimal actCode) {
        this.actCode = actCode;
    }

    public String getShtDesc() {
        return shtDesc;
    }

    public void setShtDesc(String shtDesc) {
        this.shtDesc = shtDesc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public BigDecimal getTwnCode() {
        return twnCode;
    }

    public void setTwnCode(BigDecimal twnCode) {
        this.twnCode = twnCode;
    }

    public BigDecimal getCouCode() {
        return couCode;
    }

    public void setCouCode(BigDecimal couCode) {
        this.couCode = couCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public BigDecimal getAgentCommission() {
        return agentCommission;
    }

    public void setAgentCommission(BigDecimal agentCommission) {
        this.agentCommission = agentCommission;
    }

    public String getCreditAllowed() {
        return creditAllowed;
    }

    public void setCreditAllowed(String creditAllowed) {
        this.creditAllowed = creditAllowed;
    }

    public BigDecimal getAgentWhtTax() {
        return agentWhtTax;
    }

    public void setAgentWhtTax(BigDecimal agentWhtTax) {
        this.agentWhtTax = agentWhtTax;
    }

    public String getPrintDbnote() {
        return printDbnote;
    }

    public void setPrintDbnote(String printDbnote) {
        this.printDbnote = printDbnote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public BigDecimal getCommReserveRate() {
        return commReserveRate;
    }

    public void setCommReserveRate(BigDecimal commReserveRate) {
        this.commReserveRate = commReserveRate;
    }

    public BigDecimal getAnnualBudget() {
        return annualBudget;
    }

    public void setAnnualBudget(BigDecimal annualBudget) {
        this.annualBudget = annualBudget;
    }

    public Timestamp getStatusEffDate() {
        return statusEffDate;
    }

    public void setStatusEffDate(Timestamp statusEffDate) {
        this.statusEffDate = statusEffDate;
    }

    public BigDecimal getCreditPeriod() {
        return creditPeriod;
    }

    public void setCreditPeriod(BigDecimal creditPeriod) {
        this.creditPeriod = creditPeriod;
    }

    public Timestamp getCommStatEffDt() {
        return commStatEffDt;
    }

    public void setCommStatEffDt(Timestamp commStatEffDt) {
        this.commStatEffDt = commStatEffDt;
    }

    public Timestamp getCommStatusDt() {
        return commStatusDt;
    }

    public void setCommStatusDt(Timestamp commStatusDt) {
        this.commStatusDt = commStatusDt;
    }

    public String getCommAllowed() {
        return commAllowed;
    }

    public void setCommAllowed(String commAllowed) {
        this.commAllowed = commAllowed;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public Timestamp getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Timestamp checkDate) {
        this.checkDate = checkDate;
    }

    public String getCompCommArrears() {
        return compCommArrears;
    }

    public void setCompCommArrears(String compCommArrears) {
        this.compCommArrears = compCommArrears;
    }

    public String getReinsurer() {
        return reinsurer;
    }

    public void setReinsurer(String reinsurer) {
        this.reinsurer = reinsurer;
    }

    public BigDecimal getBrnCode() {
        return brnCode;
    }

    public void setBrnCode(BigDecimal brnCode) {
        this.brnCode = brnCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getConCode() {
        return conCode;
    }

    public void setConCode(String conCode) {
        this.conCode = conCode;
    }

    public BigDecimal getAgnCode() {
        return agnCode;
    }

    public void setAgnCode(BigDecimal agnCode) {
        this.agnCode = agnCode;
    }

    public String getSmsTel() {
        return smsTel;
    }

    public void setSmsTel(String smsTel) {
        this.smsTel = smsTel;
    }

    public BigDecimal getAhcCode() {
        return ahcCode;
    }

    public void setAhcCode(BigDecimal ahcCode) {
        this.ahcCode = ahcCode;
    }

    public BigDecimal getSecCode() {
        return secCode;
    }

    public void setSecCode(BigDecimal secCode) {
        this.secCode = secCode;
    }

    public String getAgncClassCode() {
        return agncClassCode;
    }

    public void setAgncClassCode(String agncClassCode) {
        this.agncClassCode = agncClassCode;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getRunoff() {
        return runoff;
    }

    public void setRunoff(String runoff) {
        this.runoff = runoff;
    }

    public String getLicensed() {
        return licensed;
    }

    public void setLicensed(String licensed) {
        this.licensed = licensed;
    }

    public BigDecimal getLicenseGracePr() {
        return licenseGracePr;
    }

    public void setLicenseGracePr(BigDecimal licenseGracePr) {
        this.licenseGracePr = licenseGracePr;
    }

    public String getOldAccNo() {
        return oldAccNo;
    }

    public void setOldAccNo(String oldAccNo) {
        this.oldAccNo = oldAccNo;
    }

    public String getStatusRemarks() {
        return statusRemarks;
    }

    public void setStatusRemarks(String statusRemarks) {
        this.statusRemarks = statusRemarks;
    }

    public String getOsdCode() {
        return osdCode;
    }

    public void setOsdCode(String osdCode) {
        this.osdCode = osdCode;
    }

    public BigDecimal getBbrAccCode() {
        return bbrAccCode;
    }

    public void setBbrAccCode(BigDecimal bbrAccCode) {
        this.bbrAccCode = bbrAccCode;
    }

    public BigDecimal getBbrCode() {
        return bbrCode;
    }

    public void setBbrCode(BigDecimal bbrCode) {
        this.bbrCode = bbrCode;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getUniquePrefix() {
        return uniquePrefix;
    }

    public void setUniquePrefix(String uniquePrefix) {
        this.uniquePrefix = uniquePrefix;
    }

    public BigDecimal getStateCode() {
        return stateCode;
    }

    public void setStateCode(BigDecimal stateCode) {
        this.stateCode = stateCode;
    }

    public String getCrdtRting() {
        return crdtRting;
    }

    public void setCrdtRting(String crdtRting) {
        this.crdtRting = crdtRting;
    }

    public BigDecimal getClntCode() {
        return clntCode;
    }

    public void setClntCode(BigDecimal clntCode) {
        this.clntCode = clntCode;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getBruCode() {
        return bruCode;
    }

    public void setBruCode(BigDecimal bruCode) {
        this.bruCode = bruCode;
    }

    public String getLocalInternational() {
        return localInternational;
    }

    public void setLocalInternational(String localInternational) {
        this.localInternational = localInternational;
    }

    public String getRegulatorNumber() {
        return regulatorNumber;
    }

    public void setRegulatorNumber(String regulatorNumber) {
        this.regulatorNumber = regulatorNumber;
    }

    public String getAuthorised() {
        return authorised;
    }

    public void setAuthorised(String authorised) {
        this.authorised = authorised;
    }

    public String getAuthorisedBy() {
        return authorisedBy;
    }

    public void setAuthorisedBy(String authorisedBy) {
        this.authorisedBy = authorisedBy;
    }

    public Timestamp getAuthorisedDate() {
        return authorisedDate;
    }

    public void setAuthorisedDate(Timestamp authorisedDate) {
        this.authorisedDate = authorisedDate;
    }

    public BigDecimal getRorgCode() {
        return rorgCode;
    }

    public void setRorgCode(BigDecimal rorgCode) {
        this.rorgCode = rorgCode;
    }

    public BigDecimal getOrsCode() {
        return orsCode;
    }

    public void setOrsCode(BigDecimal orsCode) {
        this.orsCode = orsCode;
    }

    public String getAllocateCert() {
        return allocateCert;
    }

    public void setAllocateCert(String allocateCert) {
        this.allocateCert = allocateCert;
    }

    public String getBouncedChq() {
        return bouncedChq;
    }

    public void setBouncedChq(String bouncedChq) {
        this.bouncedChq = bouncedChq;
    }

    public BigDecimal getBpnCode() {
        return bpnCode;
    }

    public void setBpnCode(BigDecimal bpnCode) {
        this.bpnCode = bpnCode;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubagent() {
        return subagent;
    }

    public void setSubagent(String subagent) {
        this.subagent = subagent;
    }

    public BigDecimal getMainAgnCode() {
        return mainAgnCode;
    }

    public void setMainAgnCode(BigDecimal mainAgnCode) {
        this.mainAgnCode = mainAgnCode;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getEnableWebEdit() {
        return enableWebEdit;
    }

    public void setEnableWebEdit(String enableWebEdit) {
        this.enableWebEdit = enableWebEdit;
    }

    public BigDecimal getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(BigDecimal accountManager) {
        this.accountManager = accountManager;
    }

    public String getDefaultCommMode() {
        return defaultCommMode;
    }

    public void setDefaultCommMode(String defaultCommMode) {
        this.defaultCommMode = defaultCommMode;
    }

    public String getVatApplicable() {
        return vatApplicable;
    }

    public void setVatApplicable(String vatApplicable) {
        this.vatApplicable = vatApplicable;
    }

    public String getWhtaxApplicable() {
        return whtaxApplicable;
    }

    public void setWhtaxApplicable(String whtaxApplicable) {
        this.whtaxApplicable = whtaxApplicable;
    }

    public String getTelPay() {
        return telPay;
    }

    public void setTelPay(String telPay) {
        this.telPay = telPay;
    }

    public String getPaymentFreq() {
        return paymentFreq;
    }

    public void setPaymentFreq(String paymentFreq) {
        this.paymentFreq = paymentFreq;
    }

    public String getDefaultCpmMode() {
        return defaultCpmMode;
    }

    public void setDefaultCpmMode(String defaultCpmMode) {
        this.defaultCpmMode = defaultCpmMode;
    }

    public String getPymtValidated() {
        return pymtValidated;
    }

    public void setPymtValidated(String pymtValidated) {
        this.pymtValidated = pymtValidated;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Timestamp getBenefitStartDate() {
        return benefitStartDate;
    }

    public void setBenefitStartDate(Timestamp benefitStartDate) {
        this.benefitStartDate = benefitStartDate;
    }

    public String getIdNoDocUsed() {
        return idNoDocUsed;
    }

    public void setIdNoDocUsed(String idNoDocUsed) {
        this.idNoDocUsed = idNoDocUsed;
    }

    public BigDecimal getAgntyCode() {
        return agntyCode;
    }

    public void setAgntyCode(BigDecimal agntyCode) {
        this.agntyCode = agntyCode;
    }

    public BigDecimal getSbuCode() {
        return sbuCode;
    }

    public void setSbuCode(BigDecimal sbuCode) {
        this.sbuCode = sbuCode;
    }

    public String getCommLevyApp() {
        return commLevyApp;
    }

    public void setCommLevyApp(String commLevyApp) {
        this.commLevyApp = commLevyApp;
    }

    public BigDecimal getCommLevyRate() {
        return commLevyRate;
    }

    public void setCommLevyRate(BigDecimal commLevyRate) {
        this.commLevyRate = commLevyRate;
    }

    public BigDecimal getBrrCode() {
        return brrCode;
    }

    public void setBrrCode(BigDecimal brrCode) {
        this.brrCode = brrCode;
    }

    public String getBrrName() {
        return brrName;
    }

    public void setBrrName(String brrName) {
        this.brrName = brrName;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }

    public BigDecimal getEntCode() {
        return entCode;
    }

    public void setEntCode(BigDecimal entCode) {
        this.entCode = entCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIraRegno() {
        return iraRegno;
    }

    public void setIraRegno(String iraRegno) {
        this.iraRegno = iraRegno;
    }

    public String getPrincipalOfficer() {
        return principalOfficer;
    }

    public void setPrincipalOfficer(String principalOfficer) {
        this.principalOfficer = principalOfficer;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getValidationSource() {
        return validationSource;
    }

    public void setValidationSource(String validationSource) {
        this.validationSource = validationSource;
    }

    public String getIprsValidated() {
        return iprsValidated;
    }

    public void setIprsValidated(String iprsValidated) {
        this.iprsValidated = iprsValidated;
    }

    public BigDecimal getPymtDetail() {
        return pymtDetail;
    }

    public void setPymtDetail(BigDecimal pymtDetail) {
        this.pymtDetail = pymtDetail;
    }

    public BigDecimal getCrdtLimit() {
        return crdtLimit;
    }

    public void setCrdtLimit(BigDecimal crdtLimit) {
        this.crdtLimit = crdtLimit;
    }

    public String getInstCommAllwd() {
        return instCommAllwd;
    }

    public void setInstCommAllwd(String instCommAllwd) {
        this.instCommAllwd = instCommAllwd;
    }

    public String getPaydetailValidated() {
        return paydetailValidated;
    }

    public void setPaydetailValidated(String paydetailValidated) {
        this.paydetailValidated = paydetailValidated;
    }

    public String getGlRcvblAccNo() {
        return glRcvblAccNo;
    }

    public void setGlRcvblAccNo(String glRcvblAccNo) {
        this.glRcvblAccNo = glRcvblAccNo;
    }

    public String getForgn() {
        return forgn;
    }

    public void setForgn(String forgn) {
        this.forgn = forgn;
    }

    public String getIniquePrefix() {
        return iniquePrefix;
    }

    public void setIniquePrefix(String iniquePrefix) {
        this.iniquePrefix = iniquePrefix;
    }

    public String getCpmModeDesc() {
        return cpmModeDesc;
    }

    public void setCpmModeDesc(String cpmModeDesc) {
        this.cpmModeDesc = cpmModeDesc;
    }

    public String getRelatedParty() {
        return relatedParty;
    }

    public void setRelatedParty(String relatedParty) {
        this.relatedParty = relatedParty;
    }


    @Override
    public String toString() {
        return "Agent [ code=" + code + ",actCode=" + actCode + ",shtDesc=" + shtDesc + ",name=" + name + ",physicalAddress=" + physicalAddress + ",postalAddress=" + postalAddress + ",twnCode=" + twnCode + ",couCode=" + couCode + ",emailAddress=" + emailAddress + ",webAddress=" + webAddress + ",zip=" + zip + ",contactPerson=" + contactPerson + ",contactTitle=" + contactTitle + ",tel1=" + tel1 + ",tel2=" + tel2 + ",fax=" + fax + ",accNo=" + accNo + ",pin=" + pin + ",agentCommission=" + agentCommission + ",creditAllowed=" + creditAllowed + ",agentWhtTax=" + agentWhtTax + ",printDbnote=" + printDbnote + ",status=" + status + ",dateCreated=" + dateCreated + ",createdBy=" + createdBy + ",regCode=" + regCode + ",commReserveRate=" + commReserveRate + ",annualBudget=" + annualBudget + ",statusEffDate=" + statusEffDate + ",creditPeriod=" + creditPeriod + ",commStatEffDt=" + commStatEffDt + ",commStatusDt=" + commStatusDt + ",commAllowed=" + commAllowed + ",checked=" + checked + ",checkedBy=" + checkedBy + ",checkDate=" + checkDate + ",compCommArrears=" + compCommArrears + ",reinsurer=" + reinsurer + ",brnCode=" + brnCode + ",town=" + town + ",country=" + country + ",statusDesc=" + statusDesc + ",idNo=" + idNo + ",conCode=" + conCode + ",agnCode=" + agnCode + ",smsTel=" + smsTel + ",ahcCode=" + ahcCode + ",secCode=" + secCode + ",agncClassCode=" + agncClassCode + ",expiryDate=" + expiryDate + ",licenseNo=" + licenseNo + ",runoff=" + runoff + ",licensed=" + licensed + ",licenseGracePr=" + licenseGracePr + ",oldAccNo=" + oldAccNo + ",statusRemarks=" + statusRemarks + ",osdCode=" + osdCode + ",bbrAccCode=" + bbrAccCode + ",bbrCode=" + bbrCode + ",bankAccNo=" + bankAccNo + ",uniquePrefix=" + uniquePrefix + ",stateCode=" + stateCode + ",crdtRting=" + crdtRting + ",clntCode=" + clntCode + ",birthDate=" + birthDate + ",creditLimit=" + creditLimit + ",bruCode=" + bruCode + ",localInternational=" + localInternational + ",regulatorNumber=" + regulatorNumber + ",authorised=" + authorised + ",authorisedBy=" + authorisedBy + ",authorisedDate=" + authorisedDate + ",rorgCode=" + rorgCode + ",orsCode=" + orsCode + ",allocateCert=" + allocateCert + ",bouncedChq=" + bouncedChq + ",bpnCode=" + bpnCode + ",agentType=" + agentType + ",group=" + group + ",subagent=" + subagent + ",mainAgnCode=" + mainAgnCode + ",payee=" + payee + ",enableWebEdit=" + enableWebEdit + ",accountManager=" + accountManager + ",defaultCommMode=" + defaultCommMode + ",vatApplicable=" + vatApplicable + ",whtaxApplicable=" + whtaxApplicable + ",telPay=" + telPay + ",paymentFreq=" + paymentFreq + ",defaultCpmMode=" + defaultCpmMode + ",pymtValidated=" + pymtValidated + ",qualification=" + qualification + ",maritalStatus=" + maritalStatus + ",benefitStartDate=" + benefitStartDate + ",idNoDocUsed=" + idNoDocUsed + ",agntyCode=" + agntyCode + ",sbuCode=" + sbuCode + ",commLevyApp=" + commLevyApp + ",commLevyRate=" + commLevyRate + ",brrCode=" + brrCode + ",brrName=" + brrName + ",authName=" + authName + ",updatedBy=" + updatedBy + ",updatedOn=" + updatedOn + ",entCode=" + entCode + ",gender=" + gender + ",iraRegno=" + iraRegno + ",principalOfficer=" + principalOfficer + ",passportNo=" + passportNo + ",validationSource=" + validationSource + ",iprsValidated=" + iprsValidated + ",pymtDetail=" + pymtDetail + ",crdtLimit=" + crdtLimit + ",instCommAllwd=" + instCommAllwd + ",paydetailValidated=" + paydetailValidated + ",glRcvblAccNo=" + glRcvblAccNo + ",forgn=" + forgn + ",iniquePrefix=" + iniquePrefix + ",cpmModeDesc=" + cpmModeDesc + ",relatedParty=" + relatedParty + " ]";
    }
}
/* end entity's definition Agent */