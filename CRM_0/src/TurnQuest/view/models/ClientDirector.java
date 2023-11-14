//-----------start entity's definition ClientDirector --------------------//
package TurnQuest.view.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name = "tqc_client_directors", schema = "tq_crm") 
public class ClientDirector implements Serializable {
	  @Id  
	  @Column(name = "clntdir_code", nullable = false)   
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ClientDirectorNoGen")
    @GenericGenerator(name = "ClientDirectorNoGen", strategy = "TurnQuest.view.models.ClientDirectorNoGen") 
    private BigDecimal code;
	  @Column(name = "clntdir_clnt_code", nullable = true) 
    private BigDecimal    clientCode;

	  @Column(name = "clntdir_title", nullable = true) 
    private String    title;

	  @Column(name = "clntdir_name", nullable = true) 
    private String    name;

	  @Column(name = "clntdir_source_of_income", nullable = true) 
    private String    sourceOfIncome;

	  @Column(name = "clntdir_occupation", nullable = true) 
    private String    occupation;

	  @Column(name = "clntdir_gender", nullable = true) 
    private String    gender;

	  @Column(name = "clntdir_nationality", nullable = true) 
    private String    nationality;

	  @Column(name = "clntdir_date_of_birth", nullable = true) 
    private Date    dateOfBirth;

	  @Column(name = "clntdir_place_of_birth", nullable = true) 
    private String    placeOfBirth;
          
    @Column(name = "clntdir_pobCountry", nullable = true) 
    private String    pobCountry;
    
    @Column(name = "clntdir_pobState", nullable = true) 
    private String    pobState;
    
    @Column(name = "clntdir_pobTown", nullable = true) 
    private String    pobTown;

	  @Column(name = "clntdir_phone_no", nullable = true) 
    private String    phoneNo;

    @Column(name = "clntdir_means_of_id", nullable = true) 
    private String    meansOfId;

    @Column(name = "clntdir_means_of_id_val", nullable = true) 
       private String    meansOfIdVal;

	  @Column(name = "clntdir_tax_no", nullable = true) 
    private String    taxNo;

	  @Column(name = "clntdir_email", nullable = true) 
    private String    email;

	  @Column(name = "clntdir_address", nullable = true) 
    private String    address;

	  @Column(name = "clntdir_year", nullable = true) 
    private String    year;

	  @Column(name = "clntdir_qualifications", nullable = true) 
    private String    qualifications;

	  @Column(name = "clntdir_pct_holdg", nullable = true) 
    private String    pctHoldg;

	  @Column(name = "clntdir_designation", nullable = true) 
    private String    designation;



    public BigDecimal getCode() {
        return code;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getClientCode() {
        return clientCode;
    }

    public void setClientCode(BigDecimal clientCode) {
        this.clientCode = clientCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }

    public void setSourceOfIncome(String sourceOfIncome) {
        this.sourceOfIncome = sourceOfIncome;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getPctHoldg() {
        return pctHoldg;
    }

    public void setPctHoldg(String pctHoldg) {
        this.pctHoldg = pctHoldg;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    @Override
    public String toString() {
       return "ClientDirector [ code=" + code + ",clientCode=" + clientCode + ",title=" + title + ",name=" + name + ",sourceOfIncome=" + sourceOfIncome + ",occupation=" + occupation + ",gender=" + gender + ",nationality=" + nationality + ",dateOfBirth=" + dateOfBirth +
              ",placeOfBirth=" + placeOfBirth + ",phoneNo=" + phoneNo + 
        ",pobCountry=" + pobCountry +
        ",pobState=" + pobState +
        ",pobTown=" + pobTown +
              ",meansOfId=" + meansOfId + ",meansOfIdVal=" + meansOfIdVal +
              ",taxNo=" + taxNo + ",email=" + email + ",address=" + address + ",year=" + year + ",qualifications=" + qualifications + ",pctHoldg=" + pctHoldg + ",designation=" + designation + " ]";
    }

    
}
//-----------end entity's definition ClientDirector --------------------//