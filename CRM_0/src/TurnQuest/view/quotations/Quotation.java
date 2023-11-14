package TurnQuestClient.view.quotations;

import java.math.BigDecimal;

import java.util.Date;


public class Quotation {
    public Quotation() {
        super();
    }

    private BigDecimal quotationCode;
    private String riskSectionName;
    private BigDecimal riskSectionCode;
    private BigDecimal riskSectionLimit;
    private BigDecimal riskPremiumAmount;
    private String quotationNumber;
    private BigDecimal clientCode;
    private String clientName;
    private Date fromDate;
    private Date toDate;
    private String comments;
    private Date expiryDate;
    private String status;

    private String riskID;
    private String riskDescription;
    private String SubClass;
    private String coverType;
    //motor
    private String motorMake;
    private String motorCC;
    private String motorYrManufature;
    private String motorCapacity;
    private String motorValue;
    //Sections
    private String section1;
    private String section2;
    private String section3;
    private String section4;
    private String section5;

    private String sectionlabel1;
    private String sectionlabel2;
    private String sectionlabel3;
    private String sectionlabel4;
    private String sectionlabel5;

    private String coverFromDate;
    private String coverToDate;
    private String currency;
    private String otherIntrestedParties;
    private String refNumber;
    private BigDecimal sumInsured;

    private Date quotePreparedDate;

    private String ipuPropertyId;
    private String ipuPropertyDesc;
    private BigDecimal riskIpuID;
    private String riskIpuPropertyID;
    private BigDecimal subClassID;
    private BigDecimal ipuCode;
    private Date WEF;
    private Date WET;
    private String subClass;
    private BigDecimal premium;

    public void setQuotationCode(BigDecimal quotationCode) {
        this.quotationCode = quotationCode;
    }

    public BigDecimal getQuotationCode() {
        return quotationCode;
    }

    public void setRiskSectionName(String riskSectionName) {
        this.riskSectionName = riskSectionName;
    }

    public String getRiskSectionName() {
        return riskSectionName;
    }

    public void setRiskSectionCode(BigDecimal riskSectionCode) {
        this.riskSectionCode = riskSectionCode;
    }

    public BigDecimal getRiskSectionCode() {
        return riskSectionCode;
    }

    public void setRiskSectionLimit(BigDecimal riskSectionLimit) {
        this.riskSectionLimit = riskSectionLimit;
    }

    public BigDecimal getRiskSectionLimit() {
        return riskSectionLimit;
    }

    public void setRiskPremiumAmount(BigDecimal riskPremiumAmount) {
        this.riskPremiumAmount = riskPremiumAmount;
    }

    public BigDecimal getRiskPremiumAmount() {
        return riskPremiumAmount;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public void setClientCode(BigDecimal clientCode) {
        this.clientCode = clientCode;
    }

    public BigDecimal getClientCode() {
        return clientCode;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setRiskID(String riskID) {
        this.riskID = riskID;
    }

    public String getRiskID() {
        return riskID;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    public String getRiskDescription() {
        return riskDescription;
    }

    public void setSubClass(String SubClass) {
        this.SubClass = SubClass;
    }

    public String getSubClass() {
        return SubClass;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getCoverType() {
        return coverType;
    }

    public void setMotorMake(String motorMake) {
        this.motorMake = motorMake;
    }

    public String getMotorMake() {
        return motorMake;
    }

    public void setMotorCC(String motorCC) {
        this.motorCC = motorCC;
    }

    public String getMotorCC() {
        return motorCC;
    }

    public void setMotorYrManufature(String motorYrManufature) {
        this.motorYrManufature = motorYrManufature;
    }

    public String getMotorYrManufature() {
        return motorYrManufature;
    }

    public void setMotorCapacity(String motorCapacity) {
        this.motorCapacity = motorCapacity;
    }

    public String getMotorCapacity() {
        return motorCapacity;
    }

    public void setMotorValue(String motorValue) {
        this.motorValue = motorValue;
    }

    public String getMotorValue() {
        return motorValue;
    }

    public void setSection1(String section1) {
        this.section1 = section1;
    }

    public String getSection1() {
        return section1;
    }

    public void setSection2(String section2) {
        this.section2 = section2;
    }

    public String getSection2() {
        return section2;
    }

    public void setSection3(String section3) {
        this.section3 = section3;
    }

    public String getSection3() {
        return section3;
    }

    public void setSection4(String section4) {
        this.section4 = section4;
    }

    public String getSection4() {
        return section4;
    }

    public void setSection5(String section5) {
        this.section5 = section5;
    }

    public String getSection5() {
        return section5;
    }

    public void setSectionlabel1(String sectionlabel1) {
        this.sectionlabel1 = sectionlabel1;
    }

    public String getSectionlabel1() {
        return sectionlabel1;
    }

    public void setSectionlabel2(String sectionlabel2) {
        this.sectionlabel2 = sectionlabel2;
    }

    public String getSectionlabel2() {
        return sectionlabel2;
    }

    public void setSectionlabel3(String sectionlabel3) {
        this.sectionlabel3 = sectionlabel3;
    }

    public String getSectionlabel3() {
        return sectionlabel3;
    }

    public void setSectionlabel4(String sectionlabel4) {
        this.sectionlabel4 = sectionlabel4;
    }

    public String getSectionlabel4() {
        return sectionlabel4;
    }

    public void setSectionlabel5(String sectionlabel5) {
        this.sectionlabel5 = sectionlabel5;
    }

    public String getSectionlabel5() {
        return sectionlabel5;
    }

    public void setCoverFromDate(String coverFromDate) {
        this.coverFromDate = coverFromDate;
    }

    public String getCoverFromDate() {
        return coverFromDate;
    }

    public void setCoverToDate(String coverToDate) {
        this.coverToDate = coverToDate;
    }

    public String getCoverToDate() {
        return coverToDate;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setOtherIntrestedParties(String otherIntrestedParties) {
        this.otherIntrestedParties = otherIntrestedParties;
    }

    public String getOtherIntrestedParties() {
        return otherIntrestedParties;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setSumInsured(BigDecimal sumInsured) {
        this.sumInsured = sumInsured;
    }

    public BigDecimal getSumInsured() {
        return sumInsured;
    }

    public void setQuotePreparedDate(Date quotePreparedDate) {
        this.quotePreparedDate = quotePreparedDate;
    }

    public Date getQuotePreparedDate() {
        return quotePreparedDate;
    }

    public void setIpuPropertyId(String ipuPropertyId) {
        this.ipuPropertyId = ipuPropertyId;
    }

    public String getIpuPropertyId() {
        return ipuPropertyId;
    }

    public void setIpuPropertyDesc(String ipuPropertyDesc) {
        this.ipuPropertyDesc = ipuPropertyDesc;
    }

    public String getIpuPropertyDesc() {
        return ipuPropertyDesc;
    }

    public void setRiskIpuID(BigDecimal riskIpuID) {
        this.riskIpuID = riskIpuID;
    }

    public BigDecimal getRiskIpuID() {
        return riskIpuID;
    }

    public void setRiskIpuPropertyID(String riskIpuPropertyID) {
        this.riskIpuPropertyID = riskIpuPropertyID;
    }

    public String getRiskIpuPropertyID() {
        return riskIpuPropertyID;
    }

    public void setSubClassID(BigDecimal subClassID) {
        this.subClassID = subClassID;
    }

    public BigDecimal getSubClassID() {
        return subClassID;
    }

    public void setIpuCode(BigDecimal ipuCode) {
        this.ipuCode = ipuCode;
    }

    public BigDecimal getIpuCode() {
        return ipuCode;
    }

    public void setWEF(Date WEF) {
        this.WEF = WEF;
    }

    public Date getWEF() {
        return WEF;
    }

    public void setWET(Date WET) {
        this.WET = WET;
    }

    public Date getWET() {
        return WET;
    }

    public void setSubClass1(String subClass) {
        this.subClass = subClass;
    }

    public String getSubClass1() {
        return subClass;
    }

    public void setPremium(BigDecimal premium) {
        this.premium = premium;
    }

    public BigDecimal getPremium() {
        return premium;
    }
}
