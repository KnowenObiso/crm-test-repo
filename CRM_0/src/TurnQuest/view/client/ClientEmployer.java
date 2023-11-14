package TurnQuest.view.client;

import java.math.BigDecimal;

import java.sql.Date;


public class ClientEmployer {
    private BigDecimal code;
    
    private BigDecimal clntCode; // client reference
    
    private BigDecimal countryCode; // country reference
    private String countryName;
    
    private BigDecimal stateCode; // state reference
    private String stateName;
    
    private BigDecimal townCode; // town reference
    private String townName; 
    
    // private BigDecimal postalCode; // postal reference
    private String postalZipCode;
    
    private BigDecimal sectorCode; // sector code
    private String sectorName; 
    
    private String employerType; // e.g Govt, Private, Parastatal
    private String name;
    private String payrollNo;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private BigDecimal monthlyIncome;
    private Date employmentDate;
    private String employerNos;
    private String employerCells;
    private String fax;

    public ClientEmployer() {
        super();
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setClntCode(BigDecimal clntCode) {
        this.clntCode = clntCode;
    }

    public BigDecimal getClntCode() {
        return clntCode;
    }

    public void setTownCode(BigDecimal townCode) {
        this.townCode = townCode;
    }

    public BigDecimal getTownCode() {
        return townCode;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getTownName() {
        return townName;
    }

    public void setSectorCode(BigDecimal sectorCode) {
        this.sectorCode = sectorCode;
    }

    public BigDecimal getSectorCode() {
        return sectorCode;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setEmployerType(String employerType) {
        this.employerType = employerType;
    }

    public String getEmployerType() {
        return employerType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPayrollNo(String payrollNo) {
        this.payrollNo = payrollNo;
    }

    public String getPayrollNo() {
        return payrollNo;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setEmploymentDate(Date employmentDate) {
        this.employmentDate = employmentDate;
    }

    public Date getEmploymentDate() {
        return employmentDate;
    }

    public void setEmployerNos(String employerNos) {
        this.employerNos = employerNos;
    }

    public String getEmployerNos() {
        return employerNos;
    }

    public void setEmployerCells(String employerCells) {
        this.employerCells = employerCells;
    }

    public String getEmployerCells() {
        return employerCells;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setCountryCode(BigDecimal countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getCountryCode() {
        return countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    /*public void setPostalCode(BigDecimal postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getPostalCode() {
        return postalCode;
    }*/

    public void setPostalZipCode(String postalZipCode) {
        this.postalZipCode = postalZipCode;
    }

    public String getPostalZipCode() {
        return postalZipCode;
    }

    public void setStateCode(BigDecimal stateCode) {
        this.stateCode = stateCode;
    }

    public BigDecimal getStateCode() {
        return stateCode;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }
}
