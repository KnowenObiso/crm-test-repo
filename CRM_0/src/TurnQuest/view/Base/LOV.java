package TurnQuest.view.Base;

import java.math.BigDecimal;

import java.util.Date;


public class LOV {
    public LOV() {
    }
    private String BtrTransCode;
    private String BtrTransType;

    private String othernames;
    private String surname;
    private String town;

    private String postalCode;
    private String postalAddress;
    private String country;
    private String telphoneOne;
    private String telphoneTwo;
    private String fax;
    private String clientID;
    private int agentCode;
    private Date previousLastLoginOn = new Date();
    private String fullname = "";

    private BigDecimal clientCode;

    private String shortDescription;

    private String idRegNumber;

    private String physicalAddress;

    private String email;
    private String smsTel;

    private String policyNumber;
    private String PINNumber;
    private String ZIPCode;

    private String valueCode;
    private String valueDesc;
    private String code;
    private String value;

    public void setBtrTransCode(String BtrTransCode) {
        this.BtrTransCode = BtrTransCode;
    }

    public String getBtrTransCode() {
        return BtrTransCode;
    }

    public void setBtrTransType(String BtrTransType) {
        this.BtrTransType = BtrTransType;
    }

    public String getBtrTransType() {
        return BtrTransType;
    }

    public void setOthernames(String othernames) {
        this.othernames = othernames;
    }

    public String getOthernames() {
        return othernames;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getTown() {
        return town;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setTelphoneOne(String telphoneOne) {
        this.telphoneOne = telphoneOne;
    }

    public String getTelphoneOne() {
        return telphoneOne;
    }

    public void setTelphoneTwo(String telphoneTwo) {
        this.telphoneTwo = telphoneTwo;
    }

    public String getTelphoneTwo() {
        return telphoneTwo;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setAgentCode(int agentCode) {
        this.agentCode = agentCode;
    }

    public int getAgentCode() {
        return agentCode;
    }

    public void setPreviousLastLoginOn(Date previousLastLoginOn) {
        this.previousLastLoginOn = previousLastLoginOn;
    }

    public Date getPreviousLastLoginOn() {
        return previousLastLoginOn;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setClientCode(BigDecimal clientCode) {
        this.clientCode = clientCode;
    }

    public BigDecimal getClientCode() {
        return clientCode;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setIdRegNumber(String idRegNumber) {
        this.idRegNumber = idRegNumber;
    }

    public String getIdRegNumber() {
        return idRegNumber;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setSmsTel(String smsTel) {
        this.smsTel = smsTel;
    }

    public String getSmsTel() {
        return smsTel;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPINNumber(String PINNumber) {
        this.PINNumber = PINNumber;
    }

    public String getPINNumber() {
        return PINNumber;
    }

    public void setZIPCode(String ZIPCode) {
        this.ZIPCode = ZIPCode;
    }

    public String getZIPCode() {
        return ZIPCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueDesc(String valueDesc) {
        this.valueDesc = valueDesc;
    }

    public String getValueDesc() {
        return valueDesc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
