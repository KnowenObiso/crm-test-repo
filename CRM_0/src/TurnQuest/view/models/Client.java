//-----------start entity's definition Client --------------------//
package TurnQuest.view.models;

import java.math.BigDecimal;

import java.sql.Date;
import java.sql.Blob;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tqc_clients", schema = "tq_crm")
public class Client implements Serializable {
    @Id
    @Column(name = "clnt_code", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "ClientNoGen")
    @GenericGenerator(name = "ClientNoGen",
                      strategy = "TurnQuest.view.models.ClientNoGen")
    private BigDecimal code;
    @Column(name = "clnt_sht_desc", length = 50, nullable = false)
    private String shtDesc;

    @Column(name = "clnt_name", length = 300, nullable = false)
    private String name;

    @Column(name = "clnt_other_names", length = 300, nullable = true)
    private String otherNames;

    @Column(name = "clnt_id_reg_no", length = 15, nullable = true)
    private String idRegNo;

    @Column(name = "clnt_dob", nullable = true)
    private Date dob;

    @Column(name = "clnt_pin", length = 20, nullable = true)
    private String pin;

    @Column(name = "clnt_physical_addrs", length = 300, nullable = true)
    private String physicalAddrs;

    @Column(name = "clnt_postal_addrs", length = 300, nullable = true)
    private String postalAddrs;

    @Column(name = "clnt_twn_code", length = 8, nullable = true)
    private BigDecimal twnCode;

    @Column(name = "clnt_cou_code", length = 8, nullable = true)
    private BigDecimal couCode;

    @Column(name = "clnt_email_addrs", length = 50, nullable = true)
    private String emailAddrs;

    @Column(name = "clnt_tel", length = 200, nullable = true)
    private String tel;

    @Column(name = "clnt_tel2", length = 30, nullable = true)
    private String tel2;

    @Column(name = "clnt_status", length = 15, nullable = true)
    private String status;

    @Column(name = "clnt_fax", length = 30, nullable = true)
    private String fax;

    @Column(name = "clnt_remarks", length = 100, nullable = true)
    private String remarks;

    @Column(name = "clnt_spcl_terms", length = 1, nullable = true)
    private String spclTerms;

    @Column(name = "clnt_declined_prop", length = 1, nullable = true)
    private String declinedProp;

    @Column(name = "clnt_increased_premium", length = 1, nullable = true)
    private String increasedPremium;

    @Column(name = "clnt_policy_cancelled", length = 1, nullable = true)
    private String policyCancelled;

    @Column(name = "clnt_proposer", length = 1, nullable = true)
    private String proposer;

    @Column(name = "clnt_accnt_no", length = 50, nullable = true)
    private String accntNo;

    @Column(name = "clnt_domicile_countries", length = 8, nullable = true)
    private BigDecimal domicileCountries;

    @Column(name = "clnt_wef", nullable = false)
    private Date wef;

    @Column(name = "clnt_wet", nullable = true)
    private Date wet;

    @Column(name = "clnt_withdrawal_reason", length = 50, nullable = true)
    private String withdrawalReason;

    @Column(name = "clnt_sec_code", length = 8, nullable = true)
    private BigDecimal secCode;

    @Column(name = "clnt_surname", length = 100, nullable = true)
    private String surname;

    @Column(name = "clnt_type", length = 15, nullable = true)
    private String type;

    @Column(name = "clnt_title", length = 10, nullable = true)
    private String title;

    @Column(name = "clnt_business", length = 200, nullable = true)
    private String business;

    @Column(name = "clnt_zip_code", length = 15, nullable = true)
    private String zipCode;

    @Column(name = "clnt_bbr_code", length = 15, nullable = true)
    private BigDecimal bbrCode;

    @Column(name = "clnt_bank_acc_no", length = 25, nullable = true)
    private String bankAccNo;

    @Column(name = "clnt_clnt_code", nullable = true)
    private BigDecimal clntCode;

    @Column(name = "cannon_life_pol_no", length = 50, nullable = true)
    private String cannonLifePolNo;

    @Column(name = "clnt_non_direct", length = 2, nullable = true)
    private String nonDirect;

    @Column(name = "clnt_created_by", length = 30, nullable = false)
    private String createdBy;

    @Column(name = "clnt_sms_tel", length = 35, nullable = true)
    private String smsTel;

    @Column(name = "clnt_agnt_status", length = 1, nullable = false)
    private String agntStatus;

    @Column(name = "clnt_date_created", nullable = false)
    private Date dateCreated;

    @Column(name = "clnt_runoff", length = 25, nullable = true)
    private String runoff;

    @Column(name = "clnt_loaded_by", length = 15, nullable = true)
    private String loadedBy;

    @Column(name = "clnt_direct_client", length = 1, nullable = false)
    private String directClient;

    @Column(name = "clnt_old_accnt_no", length = 20, nullable = true)
    private String oldAccntNo;

    @Column(name = "clnt_agnt_client_id", length = 25, nullable = true)
    private String agntClientId;

    @Column(name = "clnt_gender", length = 1, nullable = true)
    private String gender;

    @Column(name = "clnt_usr_code", nullable = true)
    private BigDecimal usrCode;

    @Column(name = "clnt_crdt_allwd", length = 1, nullable = true)
    private String crdtAllwd;

    @Column(name = "clnt_crdt_max_amt", length = 222, nullable = true)
    private BigDecimal crdtMaxAmt;

    @Column(name = "clnt_loc_code", nullable = true)
    private BigDecimal locCode;

    @Column(name = "clnt_update_dt", nullable = true)
    private Date updateDt;

    @Column(name = "clnt_updated_by", length = 20, nullable = true)
    private String updatedBy;

    @Column(name = "clnt_updated", length = 2, nullable = true)
    private String updated;

    @Column(name = "clnt_crm_id", length = 30, nullable = true)
    private String crmId;

    @Column(name = "clnt_image", nullable = true)
    private Blob image;

    @Column(name = "clnt_cntct_phone_1", length = 21, nullable = true)
    private String cntctPhone1;

    @Column(name = "clnt_cntct_email_1", length = 50, nullable = true)
    private String cntctEmail1;

    @Column(name = "clnt_cntct_phone_2", length = 21, nullable = true)
    private String cntctPhone2;

    @Column(name = "clnt_cntct_email_2", length = 50, nullable = true)
    private String cntctEmail2;

    @Column(name = "clnt_passport_no", length = 30, nullable = true)
    private String passportNo;

    @Column(name = "clnt_cntct_name_1", length = 50, nullable = true)
    private String cntctName1;

    @Column(name = "clnt_cntct_name_2", length = 50, nullable = true)
    private String cntctName2;

    @Column(name = "clnt_sts_code", length = 15, nullable = true)
    private BigDecimal stsCode;

    @Column(name = "clnt_bdiv_code", length = 22, nullable = true)
    private BigDecimal bdivCode;

    @Column(name = "clnt_brn_code", nullable = true)
    private BigDecimal brnCode;

    @Column(name = "clnt_website", length = 150, nullable = true)
    private String website;

    @Column(name = "clnt_auditors", length = 150, nullable = true)
    private String auditors;

    @Column(name = "clnt_parent_company", nullable = true)
    private BigDecimal parentCompany;

    @Column(name = "clnt_current_insurer", length = 150, nullable = true)
    private String currentInsurer;

    @Column(name = "clnt_projected_business", nullable = true)
    private BigDecimal projectedBusiness;

    @Column(name = "clnt_date_of_empl", nullable = true)
    private Date dateOfEmpl;

    @Column(name = "clnt_driving_licence", length = 150, nullable = true)
    private String drivingLicence;

    @Column(name = "clnt_signature", nullable = true)
    private Blob signature;

    @Column(name = "clnt_acc_officer", nullable = true)
    private BigDecimal accOfficer;

    @Column(name = "clnt_commons_id", length = 30, nullable = true)
    private String commonsId;

    @Column(name = "clnt_commons_code", length = 50, nullable = true)
    private String commonsCode;

    @Column(name = "clt_cell_no", length = 200, nullable = true)
    private String cltCellNo;

    @Column(name = "clnt_employer_cell_no", length = 200, nullable = true)
    private String employerCellNo;

    @Column(name = "clnt_employer_phone_no", length = 200, nullable = true)
    private String employerPhoneNo;

    @Column(name = "clnt_bank_cell_no", length = 200, nullable = true)
    private String bankCellNo;

    @Column(name = "clnt_bank_phone_no", length = 200, nullable = true)
    private String bankPhoneNo;

    @Column(name = "clnt_occupation", length = 100, nullable = true)
    private String occupation;

    @Column(name = "clnt_old_sht_desc", length = 50, nullable = true)
    private String oldShtDesc;

    @Column(name = "clnt_anniversary", nullable = true)
    private Date anniversary;

    @Column(name = "clnt_crdt_rating", length = 8, nullable = true)
    private String crdtRating;

    @Column(name = "cltn_client_types", length = 100, nullable = true)
    private String cltnClientTypes;

    @Column(name = "clnt_sacco", length = 20, nullable = true)
    private String sacco;

    @Column(name = "clnt_drv_experience", nullable = true)
    private BigDecimal drvExperience;

    @Column(name = "clnt_reason_updated", length = 200, nullable = true)
    private String reasonUpdated;

    @Column(name = "clnt_reg_date", nullable = true)
    private Date regDate;

    @Column(name = "clnt_payroll_no", length = 50, nullable = true)
    private String payrollNo;

    @Column(name = "clnt_digit_code", length = 5, nullable = true)
    private String digitCode;

    @Column(name = "clnt_credit_lim_allowed", length = 1, nullable = true)
    private String creditLimAllowed;

    @Column(name = "clnt_credit_limit", nullable = true)
    private BigDecimal creditLimit;

    @Column(name = "clnt_sal_max_range", nullable = true)
    private BigDecimal salMaxRange;

    @Column(name = "clnt_sal_min_range", nullable = true)
    private BigDecimal salMinRange;

    @Column(name = "clnt_bounced_chq", length = 1, nullable = true)
    private String bouncedChq;

    @Column(name = "clnt_marital_status", length = 2, nullable = true)
    private String maritalStatus;

    @Column(name = "clnt_bpn_code", nullable = true)
    private BigDecimal bpnCode;

    @Column(name = "clnt_occ_code", nullable = true)
    private BigDecimal occCode;

    @Column(name = "clnt_ent_code", nullable = true)
    private BigDecimal entCode;

    @Column(name = "clnt_default_comm_mode", length = 20, nullable = true)
    private String defaultCommMode;

    @Column(name = "clnt_work_permit", length = 200, nullable = true)
    private String workPermit;

    @Column(name = "clnt_dl_issue_date", nullable = true)
    private Date dlIssueDate;

    @Column(name = "clnt_dd_bbr_code", nullable = true)
    private BigDecimal ddBbrCode;

    @Column(name = "clnt_dd_account_name", length = 255, nullable = true)
    private String ddAccountName;

    @Column(name = "clnt_dd_account_no", length = 255, nullable = true)
    private String ddAccountNo;

    @Column(name = "clnt_tel_pay", length = 50, nullable = true)
    private String telPay;

    @Column(name = "clnt_email2", length = 200, nullable = true)
    private String email2;

    @Column(name = "clnt_client_level", length = 40, nullable = true)
    private String clientLevel;

    @Column(name = "clnt_int_tel", length = 50, nullable = true)
    private String intTel;

    @Column(name = "clnt_level", length = 20, nullable = true)
    private String level;

    @Column(name = "clnt_compliance", length = 1, nullable = true)
    private String compliance;

    @Column(name = "clnt_drv_dl_issue_dt", nullable = true)
    private Date drvDlIssueDt;

    @Column(name = "clnt_comm_mode", length = 100, nullable = true)
    private String commMode;

    @Column(name = "clnt_sms_prefix", length = 100, nullable = true)
    private String smsPrefix;

    @Column(name = "clnt_unlocked_by", length = 100, nullable = true)
    private String unlockedBy;

    @Column(name = "clnt_unlocked_on", nullable = true)
    private Date unlockedOn;

    @Column(name = "clnt_locked", length = 100, nullable = true)
    private String locked;

    @Column(name = "clnt_spz_code", nullable = true)
    private BigDecimal spzCode;

    @Column(name = "clnt_country", length = 100, nullable = true)
    private String country;

    @Column(name = "clnt_area_code", length = 15, nullable = true)
    private String areaCode;

    @Column(name = "clnt_rsa_pin", length = 15, nullable = true)
    private String rsaPin;

    @Column(name = "clnt_gsm_zip_code", nullable = true)
    private BigDecimal gsmZipCode;

    @Column(name = "clnt_int_zip_code", nullable = true)
    private BigDecimal intZipCode;

    @Column(name = "clnt_clnty_code", nullable = true)
    private BigDecimal clntyCode;

    @Column(name = "clnt_preffered_paymode", length = 20, nullable = true)
    private String prefferedPaymode;

    @Column(name = "clnt_staff_no", length = 40, nullable = true)
    private String staffNo;

    @Column(name = "clnt_sms_prefix2", length = 40, nullable = true)
    private String smsPrefix2;

    @Column(name = "clnt_div_code", nullable = true)
    private BigDecimal divCode;

    @Column(name = "clnt_division", length = 100, nullable = true)
    private String division;

    @Column(name = "clnt_sms_tel2", length = 35, nullable = true)
    private String smsTel2;

    @Column(name = "clnt_tel_pay2", length = 35, nullable = true)
    private String telPay2;

    @Column(name = "clnt_acc_officer_name", length = 35, nullable = true)
    private String accOfficerName;

    @Column(name = "clnt_benefit_payment_mode", nullable = true)
    private BigDecimal benefitPaymentMode;

    @Column(name = "clnt_premium_payment_mode", nullable = true)
    private BigDecimal premiumPaymentMode;

    @Column(name = "clnt_premium_paymode", length = 50, nullable = true)
    private String premiumPaymode;

    @Column(name = "clnt_education_level", length = 100, nullable = true)
    private String educationLevel;

    @Column(name = "clnt_monthly_income", nullable = true)
    private BigDecimal monthlyIncome;

    @Column(name = "clnt_sms_notifications", length = 1, nullable = true)
    private String smsNotifications;

    @Column(name = "clnt_email_notifications", length = 1, nullable = true)
    private String emailNotifications;

    @Column(name = "clnt_first_name", length = 80, nullable = true)
    private String firstName;

    @Column(name = "clnt_validation_source", length = 80, nullable = true)
    private String validationSource;

    @Column(name = "clnt_iprs_validated", length = 1, nullable = true)
    private String iprsValidated;

    @Column(name = "clnt_pymt_detail", nullable = true)
    private BigDecimal pymtDetail;

    @Column(name = "clnt_crdt_limit", nullable = true)
    private BigDecimal crdtLimit;

    @Column(name = "clnt_street", length = 50, nullable = true)
    private String street;

    @Column(name = "clnt_source_of_income", length = 200, nullable = true)
    private String sourceOfIncome;

    @Column(name = "clnt_type_of_source_of_income", length = 200,
            nullable = true)
    private String sourceOfIncomeType;

    @Column(name = "clnt_place_of_birth", length = 200, nullable = true)
    private String placeOfBirth;

    @Column(name = "clnt_mod_type", length = 80, nullable = true)
    private String meansOfId;

    @Column(name = "clnt_mod_val", length = 200, nullable = true)
    private String meansOfIdVal;

    @Column(name = "clnt_mod_date_issued", nullable = true)
    private Date meansOfIdDateIssued;

    @Column(name = "clnt_mod_date_expired", nullable = true)
    private Date meansOfIdDateExpired;

    @Column(name = "clnt_mod_issued_by", length = 200, nullable = true)
    private String meansOfIdIssuedBy;

    @Column(name = "clnt_mod_issuing_country", length = 20, nullable = true)
    private String meansOfIdIssuingCountry;

    @Column(name = "clnt_utility_bill", length = 200, nullable = true)
    private String utilityBill;

    @Column(name = "clnt_nationality_code", nullable = true)
    private BigDecimal nationalityCode;

    @Column(name = "clnt_pob_country", nullable = true)
    private String pobCountry;
    @Column(name = "clnt_pob_state", nullable = true)
    private String pobState;
    @Column(name = "clnt_pob_town", nullable = true)
    private String pobTown;


    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
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

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getIdRegNo() {
        return idRegNo;
    }

    public void setIdRegNo(String idRegNo) {
        this.idRegNo = idRegNo;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPhysicalAddrs() {
        return physicalAddrs;
    }

    public void setPhysicalAddrs(String physicalAddrs) {
        this.physicalAddrs = physicalAddrs;
    }

    public String getPostalAddrs() {
        return postalAddrs;
    }

    public void setPostalAddrs(String postalAddrs) {
        this.postalAddrs = postalAddrs;
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

    public String getEmailAddrs() {
        return emailAddrs;
    }

    public void setEmailAddrs(String emailAddrs) {
        this.emailAddrs = emailAddrs;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSpclTerms() {
        return spclTerms;
    }

    public void setSpclTerms(String spclTerms) {
        this.spclTerms = spclTerms;
    }

    public String getDeclinedProp() {
        return declinedProp;
    }

    public void setDeclinedProp(String declinedProp) {
        this.declinedProp = declinedProp;
    }

    public String getIncreasedPremium() {
        return increasedPremium;
    }

    public void setIncreasedPremium(String increasedPremium) {
        this.increasedPremium = increasedPremium;
    }

    public String getPolicyCancelled() {
        return policyCancelled;
    }

    public void setPolicyCancelled(String policyCancelled) {
        this.policyCancelled = policyCancelled;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getAccntNo() {
        return accntNo;
    }

    public void setAccntNo(String accntNo) {
        this.accntNo = accntNo;
    }

    public BigDecimal getDomicileCountries() {
        return domicileCountries;
    }

    public void setDomicileCountries(BigDecimal domicileCountries) {
        this.domicileCountries = domicileCountries;
    }

    public Date getWef() {
        return wef;
    }

    public void setWef(Date wef) {
        this.wef = wef;
    }

    public Date getWet() {
        return wet;
    }

    public void setWet(Date wet) {
        this.wet = wet;
    }

    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    public void setWithdrawalReason(String withdrawalReason) {
        this.withdrawalReason = withdrawalReason;
    }

    public BigDecimal getSecCode() {
        return secCode;
    }

    public void setSecCode(BigDecimal secCode) {
        this.secCode = secCode;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public BigDecimal getClntCode() {
        return clntCode;
    }

    public void setClntCode(BigDecimal clntCode) {
        this.clntCode = clntCode;
    }

    public String getCannonLifePolNo() {
        return cannonLifePolNo;
    }

    public void setCannonLifePolNo(String cannonLifePolNo) {
        this.cannonLifePolNo = cannonLifePolNo;
    }

    public String getNonDirect() {
        return nonDirect;
    }

    public void setNonDirect(String nonDirect) {
        this.nonDirect = nonDirect;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getSmsTel() {
        return smsTel;
    }

    public void setSmsTel(String smsTel) {
        this.smsTel = smsTel;
    }

    public String getAgntStatus() {
        return agntStatus;
    }

    public void setAgntStatus(String agntStatus) {
        this.agntStatus = agntStatus;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getRunoff() {
        return runoff;
    }

    public void setRunoff(String runoff) {
        this.runoff = runoff;
    }

    public String getLoadedBy() {
        return loadedBy;
    }

    public void setLoadedBy(String loadedBy) {
        this.loadedBy = loadedBy;
    }

    public String getDirectClient() {
        return directClient;
    }

    public void setDirectClient(String directClient) {
        this.directClient = directClient;
    }

    public String getOldAccntNo() {
        return oldAccntNo;
    }

    public void setOldAccntNo(String oldAccntNo) {
        this.oldAccntNo = oldAccntNo;
    }

    public String getAgntClientId() {
        return agntClientId;
    }

    public void setAgntClientId(String agntClientId) {
        this.agntClientId = agntClientId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getUsrCode() {
        return usrCode;
    }

    public void setUsrCode(BigDecimal usrCode) {
        this.usrCode = usrCode;
    }

    public String getCrdtAllwd() {
        return crdtAllwd;
    }

    public void setCrdtAllwd(String crdtAllwd) {
        this.crdtAllwd = crdtAllwd;
    }

    public BigDecimal getCrdtMaxAmt() {
        return crdtMaxAmt;
    }

    public void setCrdtMaxAmt(BigDecimal crdtMaxAmt) {
        this.crdtMaxAmt = crdtMaxAmt;
    }

    public BigDecimal getLocCode() {
        return locCode;
    }

    public void setLocCode(BigDecimal locCode) {
        this.locCode = locCode;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCrmId() {
        return crmId;
    }

    public void setCrmId(String crmId) {
        this.crmId = crmId;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getCntctPhone1() {
        return cntctPhone1;
    }

    public void setCntctPhone1(String cntctPhone1) {
        this.cntctPhone1 = cntctPhone1;
    }

    public String getCntctEmail1() {
        return cntctEmail1;
    }

    public void setCntctEmail1(String cntctEmail1) {
        this.cntctEmail1 = cntctEmail1;
    }

    public String getCntctPhone2() {
        return cntctPhone2;
    }

    public void setCntctPhone2(String cntctPhone2) {
        this.cntctPhone2 = cntctPhone2;
    }

    public String getCntctEmail2() {
        return cntctEmail2;
    }

    public void setCntctEmail2(String cntctEmail2) {
        this.cntctEmail2 = cntctEmail2;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getCntctName1() {
        return cntctName1;
    }

    public void setCntctName1(String cntctName1) {
        this.cntctName1 = cntctName1;
    }

    public String getCntctName2() {
        return cntctName2;
    }

    public void setCntctName2(String cntctName2) {
        this.cntctName2 = cntctName2;
    }

    public BigDecimal getStsCode() {
        return stsCode;
    }

    public void setStsCode(BigDecimal stsCode) {
        this.stsCode = stsCode;
    }

    public BigDecimal getBdivCode() {
        return bdivCode;
    }

    public void setBdivCode(BigDecimal bdivCode) {
        this.bdivCode = bdivCode;
    }

    public BigDecimal getBrnCode() {
        return brnCode;
    }

    public void setBrnCode(BigDecimal brnCode) {
        this.brnCode = brnCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAuditors() {
        return auditors;
    }

    public void setAuditors(String auditors) {
        this.auditors = auditors;
    }

    public BigDecimal getParentCompany() {
        return parentCompany;
    }

    public void setParentCompany(BigDecimal parentCompany) {
        this.parentCompany = parentCompany;
    }

    public String getCurrentInsurer() {
        return currentInsurer;
    }

    public void setCurrentInsurer(String currentInsurer) {
        this.currentInsurer = currentInsurer;
    }

    public BigDecimal getProjectedBusiness() {
        return projectedBusiness;
    }

    public void setProjectedBusiness(BigDecimal projectedBusiness) {
        this.projectedBusiness = projectedBusiness;
    }

    public Date getDateOfEmpl() {
        return dateOfEmpl;
    }

    public void setDateOfEmpl(Date dateOfEmpl) {
        this.dateOfEmpl = dateOfEmpl;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(String drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    public Blob getSignature() {
        return signature;
    }

    public void setSignature(Blob signature) {
        this.signature = signature;
    }

    public BigDecimal getAccOfficer() {
        return accOfficer;
    }

    public void setAccOfficer(BigDecimal accOfficer) {
        this.accOfficer = accOfficer;
    }

    public String getCommonsId() {
        return commonsId;
    }

    public void setCommonsId(String commonsId) {
        this.commonsId = commonsId;
    }

    public String getCommonsCode() {
        return commonsCode;
    }

    public void setCommonsCode(String commonsCode) {
        this.commonsCode = commonsCode;
    }

    public String getCltCellNo() {
        return cltCellNo;
    }

    public void setCltCellNo(String cltCellNo) {
        this.cltCellNo = cltCellNo;
    }

    public String getEmployerCellNo() {
        return employerCellNo;
    }

    public void setEmployerCellNo(String employerCellNo) {
        this.employerCellNo = employerCellNo;
    }

    public String getEmployerPhoneNo() {
        return employerPhoneNo;
    }

    public void setEmployerPhoneNo(String employerPhoneNo) {
        this.employerPhoneNo = employerPhoneNo;
    }

    public String getBankCellNo() {
        return bankCellNo;
    }

    public void setBankCellNo(String bankCellNo) {
        this.bankCellNo = bankCellNo;
    }

    public String getBankPhoneNo() {
        return bankPhoneNo;
    }

    public void setBankPhoneNo(String bankPhoneNo) {
        this.bankPhoneNo = bankPhoneNo;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOldShtDesc() {
        return oldShtDesc;
    }

    public void setOldShtDesc(String oldShtDesc) {
        this.oldShtDesc = oldShtDesc;
    }

    public Date getAnniversary() {
        return anniversary;
    }

    public void setAnniversary(Date anniversary) {
        this.anniversary = anniversary;
    }

    public String getCrdtRating() {
        return crdtRating;
    }

    public void setCrdtRating(String crdtRating) {
        this.crdtRating = crdtRating;
    }

    public String getCltnClientTypes() {
        return cltnClientTypes;
    }

    public void setCltnClientTypes(String cltnClientTypes) {
        this.cltnClientTypes = cltnClientTypes;
    }

    public String getSacco() {
        return sacco;
    }

    public void setSacco(String sacco) {
        this.sacco = sacco;
    }

    public BigDecimal getDrvExperience() {
        return drvExperience;
    }

    public void setDrvExperience(BigDecimal drvExperience) {
        this.drvExperience = drvExperience;
    }

    public String getReasonUpdated() {
        return reasonUpdated;
    }

    public void setReasonUpdated(String reasonUpdated) {
        this.reasonUpdated = reasonUpdated;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getPayrollNo() {
        return payrollNo;
    }

    public void setPayrollNo(String payrollNo) {
        this.payrollNo = payrollNo;
    }

    public String getDigitCode() {
        return digitCode;
    }

    public void setDigitCode(String digitCode) {
        this.digitCode = digitCode;
    }

    public String getCreditLimAllowed() {
        return creditLimAllowed;
    }

    public void setCreditLimAllowed(String creditLimAllowed) {
        this.creditLimAllowed = creditLimAllowed;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getSalMaxRange() {
        return salMaxRange;
    }

    public void setSalMaxRange(BigDecimal salMaxRange) {
        this.salMaxRange = salMaxRange;
    }

    public BigDecimal getSalMinRange() {
        return salMinRange;
    }

    public void setSalMinRange(BigDecimal salMinRange) {
        this.salMinRange = salMinRange;
    }

    public String getBouncedChq() {
        return bouncedChq;
    }

    public void setBouncedChq(String bouncedChq) {
        this.bouncedChq = bouncedChq;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public BigDecimal getBpnCode() {
        return bpnCode;
    }

    public void setBpnCode(BigDecimal bpnCode) {
        this.bpnCode = bpnCode;
    }

    public BigDecimal getOccCode() {
        return occCode;
    }

    public void setOccCode(BigDecimal occCode) {
        this.occCode = occCode;
    }

    public BigDecimal getEntCode() {
        return entCode;
    }

    public void setEntCode(BigDecimal entCode) {
        this.entCode = entCode;
    }

    public String getDefaultCommMode() {
        return defaultCommMode;
    }

    public void setDefaultCommMode(String defaultCommMode) {
        this.defaultCommMode = defaultCommMode;
    }

    public String getWorkPermit() {
        return workPermit;
    }

    public void setWorkPermit(String workPermit) {
        this.workPermit = workPermit;
    }

    public Date getDlIssueDate() {
        return dlIssueDate;
    }

    public void setDlIssueDate(Date dlIssueDate) {
        this.dlIssueDate = dlIssueDate;
    }

    public BigDecimal getDdBbrCode() {
        return ddBbrCode;
    }

    public void setDdBbrCode(BigDecimal ddBbrCode) {
        this.ddBbrCode = ddBbrCode;
    }

    public String getDdAccountName() {
        return ddAccountName;
    }

    public void setDdAccountName(String ddAccountName) {
        this.ddAccountName = ddAccountName;
    }

    public String getDdAccountNo() {
        return ddAccountNo;
    }

    public void setDdAccountNo(String ddAccountNo) {
        this.ddAccountNo = ddAccountNo;
    }

    public String getTelPay() {
        return telPay;
    }

    public void setTelPay(String telPay) {
        this.telPay = telPay;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getClientLevel() {
        return clientLevel;
    }

    public void setClientLevel(String clientLevel) {
        this.clientLevel = clientLevel;
    }

    public String getIntTel() {
        return intTel;
    }

    public void setIntTel(String intTel) {
        this.intTel = intTel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCompliance() {
        return compliance;
    }

    public void setCompliance(String compliance) {
        this.compliance = compliance;
    }

    public Date getDrvDlIssueDt() {
        return drvDlIssueDt;
    }

    public void setDrvDlIssueDt(Date drvDlIssueDt) {
        this.drvDlIssueDt = drvDlIssueDt;
    }

    public String getCommMode() {
        return commMode;
    }

    public void setCommMode(String commMode) {
        this.commMode = commMode;
    }

    public String getSmsPrefix() {
        return smsPrefix;
    }

    public void setSmsPrefix(String smsPrefix) {
        this.smsPrefix = smsPrefix;
    }

    public String getUnlockedBy() {
        return unlockedBy;
    }

    public void setUnlockedBy(String unlockedBy) {
        this.unlockedBy = unlockedBy;
    }

    public Date getUnlockedOn() {
        return unlockedOn;
    }

    public void setUnlockedOn(Date unlockedOn) {
        this.unlockedOn = unlockedOn;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public BigDecimal getSpzCode() {
        return spzCode;
    }

    public void setSpzCode(BigDecimal spzCode) {
        this.spzCode = spzCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getRsaPin() {
        return rsaPin;
    }

    public void setRsaPin(String rsaPin) {
        this.rsaPin = rsaPin;
    }

    public BigDecimal getGsmZipCode() {
        return gsmZipCode;
    }

    public void setGsmZipCode(BigDecimal gsmZipCode) {
        this.gsmZipCode = gsmZipCode;
    }

    public BigDecimal getIntZipCode() {
        return intZipCode;
    }

    public void setIntZipCode(BigDecimal intZipCode) {
        this.intZipCode = intZipCode;
    }

    public BigDecimal getClntyCode() {
        return clntyCode;
    }

    public void setClntyCode(BigDecimal clntyCode) {
        this.clntyCode = clntyCode;
    }

    public String getPrefferedPaymode() {
        return prefferedPaymode;
    }

    public void setPrefferedPaymode(String prefferedPaymode) {
        this.prefferedPaymode = prefferedPaymode;
    }

    public String getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public String getSmsPrefix2() {
        return smsPrefix2;
    }

    public void setSmsPrefix2(String smsPrefix2) {
        this.smsPrefix2 = smsPrefix2;
    }

    public BigDecimal getDivCode() {
        return divCode;
    }

    public void setDivCode(BigDecimal divCode) {
        this.divCode = divCode;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getSmsTel2() {
        return smsTel2;
    }

    public void setSmsTel2(String smsTel2) {
        this.smsTel2 = smsTel2;
    }

    public String getTelPay2() {
        return telPay2;
    }

    public void setTelPay2(String telPay2) {
        this.telPay2 = telPay2;
    }

    public String getAccOfficerName() {
        return accOfficerName;
    }

    public void setAccOfficerName(String accOfficerName) {
        this.accOfficerName = accOfficerName;
    }

    public BigDecimal getBenefitPaymentMode() {
        return benefitPaymentMode;
    }

    public void setBenefitPaymentMode(BigDecimal benefitPaymentMode) {
        this.benefitPaymentMode = benefitPaymentMode;
    }

    public BigDecimal getPremiumPaymentMode() {
        return premiumPaymentMode;
    }

    public void setPremiumPaymentMode(BigDecimal premiumPaymentMode) {
        this.premiumPaymentMode = premiumPaymentMode;
    }

    public String getPremiumPaymode() {
        return premiumPaymode;
    }

    public void setPremiumPaymode(String premiumPaymode) {
        this.premiumPaymode = premiumPaymode;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(String smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public String getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(String emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getMeansOfId() {
        return meansOfId;
    }

    public void setMeansOfId(String meansOfId) {
        this.meansOfId = meansOfId;
    }

    public String getMeansOfIdVal() {
        return meansOfIdVal;
    }

    public void setMeansOfIdVal(String meansOfIdVal) {
        this.meansOfIdVal = meansOfIdVal;
    }

    public Date getMeansOfIdDateIssued() {
        return meansOfIdDateIssued;
    }

    public void setMeansOfIdDateIssued(Date meansOfIdDateIssued) {
        this.meansOfIdDateIssued = meansOfIdDateIssued;
    }

    public Date getMeansOfIdDateExpired() {
        return meansOfIdDateExpired;
    }

    public void setMeansOfIdDateExpired(Date meansOfIdDateExpired) {
        this.meansOfIdDateExpired = meansOfIdDateExpired;
    }

    public String getMeansOfIdIssuedBy() {
        return meansOfIdIssuedBy;
    }

    public void setMeansOfIdIssuedBy(String meansOfIdIssuedBy) {
        this.meansOfIdIssuedBy = meansOfIdIssuedBy;
    }

    public String getMeansOfIdIssuingCountry() {
        return meansOfIdIssuingCountry;
    }

    public void setMeansOfIdIssuingCountry(String meansOfIdIssuingCountry) {
        this.meansOfIdIssuingCountry = meansOfIdIssuingCountry;
    }

    public String getUtilityBill() {
        return utilityBill;
    }

    public void setUtilityBill(String utilityBill) {
        this.utilityBill = utilityBill;
    }

    public BigDecimal getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(BigDecimal nationalityCode) {
        this.nationalityCode = nationalityCode;
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

    public void setSourceOfIncomeType(String sourceOfIncomeType) {
        this.sourceOfIncomeType = sourceOfIncomeType;
    }

    public String getSourceOfIncomeType() {
        return sourceOfIncomeType;
    }

    @Override
    public String toString() {
        return "Client [ code=" + code + ",shtDesc=" + shtDesc + ",name=" +
            name + ",otherNames=" + otherNames + ",idRegNo=" + idRegNo +
            ",dob=" + dob + ",pin=" + pin + ",physicalAddrs=" + physicalAddrs +
            ",postalAddrs=" + postalAddrs + ",twnCode=" + twnCode +
            ",couCode=" + couCode + ",emailAddrs=" + emailAddrs + ",tel=" +
            tel + ",tel2=" + tel2 + ",status=" + status + ",fax=" + fax +
            ",remarks=" + remarks + ",spclTerms=" + spclTerms +
            ",declinedProp=" + declinedProp + ",increasedPremium=" +
            increasedPremium + ",policyCancelled=" + policyCancelled +
            ",proposer=" + proposer + ",accntNo=" + accntNo +
            ",domicileCountries=" + domicileCountries + ",wef=" + wef +
            ",wet=" + wet + ",withdrawalReason=" + withdrawalReason +
            ",secCode=" + secCode + ",surname=" + surname + ",type=" + type +
            ",title=" + title + ",business=" + business + ",zipCode=" +
            zipCode + ",bbrCode=" + bbrCode + ",bankAccNo=" + bankAccNo +
            ",clntCode=" + clntCode + ",cannonLifePolNo=" + cannonLifePolNo +
            ",nonDirect=" + nonDirect + ",createdBy=" + createdBy +
            ",smsTel=" + smsTel + ",agntStatus=" + agntStatus +
            ",dateCreated=" + dateCreated + ",runoff=" + runoff +
            ",loadedBy=" + loadedBy + ",directClient=" + directClient +
            ",oldAccntNo=" + oldAccntNo + ",agntClientId=" + agntClientId +
            ",gender=" + gender + ",usrCode=" + usrCode + ",crdtAllwd=" +
            crdtAllwd + ",crdtMaxAmt=" + crdtMaxAmt + ",locCode=" + locCode +
            ",updateDt=" + updateDt + ",updatedBy=" + updatedBy + ",updated=" +
            updated + ",crmId=" + crmId + ",image=" + image + ",cntctPhone1=" +
            cntctPhone1 + ",cntctEmail1=" + cntctEmail1 + ",cntctPhone2=" +
            cntctPhone2 + ",cntctEmail2=" + cntctEmail2 + ",passportNo=" +
            passportNo + ",cntctName1=" + cntctName1 + ",cntctName2=" +
            cntctName2 + ",stsCode=" + stsCode + ",bdivCode=" + bdivCode +
            ",brnCode=" + brnCode + ",website=" + website + ",auditors=" +
            auditors + ",parentCompany=" + parentCompany + ",currentInsurer=" +
            currentInsurer + ",projectedBusiness=" + projectedBusiness +
            ",dateOfEmpl=" + dateOfEmpl + ",drivingLicence=" + drivingLicence +
            ",signature=" + signature + ",accOfficer=" + accOfficer +
            ",commonsId=" + commonsId + ",commonsCode=" + commonsCode +
            ",cltCellNo=" + cltCellNo + ",employerCellNo=" + employerCellNo +
            ",employerPhoneNo=" + employerPhoneNo + ",bankCellNo=" +
            bankCellNo + ",bankPhoneNo=" + bankPhoneNo + ",occupation=" +
            occupation + ",oldShtDesc=" + oldShtDesc + ",anniversary=" +
            anniversary + ",crdtRating=" + crdtRating + ",cltnClientTypes=" +
            cltnClientTypes + ",sacco=" + sacco + ",drvExperience=" +
            drvExperience + ",reasonUpdated=" + reasonUpdated + ",regDate=" +
            regDate + ",payrollNo=" + payrollNo + ",digitCode=" + digitCode +
            ",creditLimAllowed=" + creditLimAllowed + ",creditLimit=" +
            creditLimit + ",salMaxRange=" + salMaxRange + ",salMinRange=" +
            salMinRange + ",bouncedChq=" + bouncedChq + ",maritalStatus=" +
            maritalStatus + ",bpnCode=" + bpnCode + ",occCode=" + occCode +
            ",entCode=" + entCode + ",defaultCommMode=" + defaultCommMode +
            ",workPermit=" + workPermit + ",dlIssueDate=" + dlIssueDate +
            ",ddBbrCode=" + ddBbrCode + ",ddAccountName=" + ddAccountName +
            ",ddAccountNo=" + ddAccountNo + ",telPay=" + telPay + ",email2=" +
            email2 + ",clientLevel=" + clientLevel + ",intTel=" + intTel +
            ",level=" + level + ",compliance=" + compliance +
            ",drvDlIssueDt=" + drvDlIssueDt + ",commMode=" + commMode +
            ",smsPrefix=" + smsPrefix + ",unlockedBy=" + unlockedBy +
            ",unlockedOn=" + unlockedOn + ",locked=" + locked + ",spzCode=" +
            spzCode + ",country=" + country + ",areaCode=" + areaCode +
            ",rsaPin=" + rsaPin + ",gsmZipCode=" + gsmZipCode +
            ",intZipCode=" + intZipCode + ",clntyCode=" + clntyCode +
            ",prefferedPaymode=" + prefferedPaymode + ",staffNo=" + staffNo +
            ",smsPrefix2=" + smsPrefix2 + ",divCode=" + divCode +
            ",division=" + division + ",smsTel2=" + smsTel2 + ",telPay2=" +
            telPay2 + ",accOfficerName=" + accOfficerName +
            ",benefitPaymentMode=" + benefitPaymentMode +
            ",premiumPaymentMode=" + premiumPaymentMode + ",premiumPaymode=" +
            premiumPaymode + ",educationLevel=" + educationLevel +
            ",monthlyIncome=" + monthlyIncome + ",smsNotifications=" +
            smsNotifications + ",emailNotifications=" + emailNotifications +
            ",firstName=" + firstName + ",validationSource=" +
            validationSource + ",iprsValidated=" + iprsValidated +
            ",pymtDetail=" + pymtDetail + ",crdtLimit=" + crdtLimit +
            ",street=" + street + ",sourceOfIncome=" + sourceOfIncome +
            ",sourceOfIncomeType=" + sourceOfIncomeType + ",placeOfBirth=" +
            placeOfBirth + ",meansOfId=" + meansOfId + ",meansOfIdVal=" +
            meansOfIdVal + ",meansOfIdDateIssued=" + meansOfIdDateIssued +
            ",meansOfIdDateExpired=" + meansOfIdDateExpired +
            ",meansOfIdIssuedBy=" + meansOfIdIssuedBy +
            ",meansOfIdIssuingCountry=" + meansOfIdIssuingCountry +
            ",utilityBill=" + utilityBill + ",nationalityCode=" +
            nationalityCode + ",pobCountry=" + pobCountry + ",pobState=" +
            pobState + ",pobTown=" + pobTown + " ]";
    }


}
//-----------end entity's definition Client --------------------//