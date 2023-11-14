package TurnQuest.view.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name = "tqc_client_signatories", schema = "tq_crm") 
public class ClientSignatory implements Serializable {
	 @Id  
	 @Column(name = "clnsig_code", nullable = false)   
         @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SignatoryNumberGenerator")
         @GenericGenerator(name = "SignatoryNumberGenerator", strategy = "TurnQuest.view.models.SignatoryNumberGenerator") 
    private BigDecimal code;
	@Column(name = "clnsig_clnt_code", nullable = false) 
    private BigDecimal clientCode;
	@Column(name = "clnsig_title", nullable = true) 
    private String    title;
	@Column(name = "clnsig_name", nullable = true) 
    private String    name;
	@Column(name = "clnsig_source_of_income", nullable = true) 
    private String    sourceOfIncome;
	@Column(name = "clnsig_occupation", nullable = true) 
    private String    occupation;
	@Column(name = "clnsig_gender", nullable = true) 
    private String    gender;
	@Column(name = "clnsig_nationality", nullable = true) 
    private String    nationality; 
	@Column(name = "clnsig_date_of_birth", nullable = true) 
    private Date     dateOfbirth;
	@Column(name = "clnsig_place_of_birth", nullable = true) 
    private String    placeOfbirth;
    @Column(name = "clnsig_pob_country", nullable = true) 
    private String    pobCountry;
    @Column(name = "clnsig_pob_state", nullable = true) 
    private String    pobState;
    @Column(name = "clnsig_pob_town", nullable = true) 
    private String    pobTown;
	@Column(name = "clnsig_phone_no", nullable = true) 
    private String    phoneNo;
	@Column(name = "clnsig_means_of_id", nullable = true) 
    private String    meansOfId;
    @Column(name = "clnsig_means_of_id_val", nullable = true) 
    private String    meansOfIdVal;
	@Column(name = "clnsig_tax_no", nullable = true) 
    private String    taxNo;
	@Column(name = "clnsig_email", nullable = true) 
    private String    email;
	@Column(name = "clnsig_address", nullable = true) 
    private String    address;

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public void setDateOfbirth(Date dateOfbirth) {
        this.dateOfbirth = dateOfbirth;
    }

    public Date getDateOfbirth() {
        return dateOfbirth;
    }

    public void setPlaceOfbirth(String placeOfbirth) {
        this.placeOfbirth = placeOfbirth;
    }

    public String getPlaceOfbirth() {
        return placeOfbirth;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setMeansOfId(String meansOfId) {
        this.meansOfId = meansOfId;
    }

    public String getMeansOfId() {
        return meansOfId;
    }
    
     public String getMeansOfIdVal() {
           return meansOfIdVal;
       }

       public void setMeansOfIdVal(String meansOfIdVal) {
           this.meansOfIdVal = meansOfIdVal;
       }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setClientCode(BigDecimal clientCode) {
        this.clientCode = clientCode;
    }

    public BigDecimal getClientCode() {
        return clientCode;
    }
    @Override
    public String toString() {
        return "ClientSignatory [ code=" + code + ",clientCode=" + clientCode + ",title=" 
               + title + ",name=" + name + ",sourceOfIncome=" + sourceOfIncome +
               ",occupation=" + occupation + ",gender=" + gender + ",nationality="
               + nationality + ",dateOfBirth=" + dateOfbirth + ",placeOfBirth="
               +dateOfbirth + ",pobState="
               +pobState + ",pobTown="+ pobTown +",pobCountry=" + pobCountry+ ",phoneNo=" + phoneNo + ",meansOfId=" + meansOfId + ",meansOfIdVal=" + meansOfIdVal + ",taxNo=" + taxNo + ",email=" + email + ",address=" + address + " ]";
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
}
