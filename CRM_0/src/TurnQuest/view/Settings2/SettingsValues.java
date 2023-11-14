package TurnQuest.view.Settings2;

import java.math.BigDecimal;

import java.util.Date;


public class SettingsValues {
    private BigDecimal CountryCode;
    private String CountryShtDesc;
    private String CountryName;
    private BigDecimal TownCode;
    private String postalDesc;
    private String TownShtDesc;
    private String TownName;
    private String PostalZIPCode;
    private String SectorShtDesc;
    private String SectorName;
    private BigDecimal SectorCode;
    private String BankName;
    private String BranchName;
    private BigDecimal BankBranchCode;

    //Required Documents
    private BigDecimal RDOC_CODE;
    private String RDOC_SHT_DESC;
    private String ROC_DESC;
    private String ROC_MANDATORY;
    private Date ROC_DATE_ADDED;

    //Clients Documents
    private BigDecimal CDOCR_CODE;
    private BigDecimal CDOCR_RDOC_CODE;
    private String CDOCR_RDOC_NAME;
    private BigDecimal CDOCR_CLNT_CODE;
    private String CDOCR_SUBMITED;
    private Date CDOCR_DATE_S;
    private String CDOCR_REF_NO;
    private String CDOCR_RMRK;
    private String CDOCR_USER_RECEIVD;
    private String CDOCR_DOCID;

    //Account Managers
    private String USR_USERNAME;
    private String USR_NAME;
    private BigDecimal USR_CODE;


    public void setCountryCode(BigDecimal CountryCode) {
        this.CountryCode = CountryCode;
    }

    public BigDecimal getCountryCode() {
        return CountryCode;
    }

    public void setCountryShtDesc(String CountryShtDesc) {
        this.CountryShtDesc = CountryShtDesc;
    }

    public String getCountryShtDesc() {
        return CountryShtDesc;
    }

    public void setCountryName(String CountryName) {
        this.CountryName = CountryName;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setTownCode(BigDecimal TownCode) {
        this.TownCode = TownCode;
    }

    public BigDecimal getTownCode() {
        return TownCode;
    }

    public void setPostalDesc(String postalDesc) {
        this.postalDesc = postalDesc;
    }

    public String getPostalDesc() {
        return postalDesc;
    }

    public void setTownShtDesc(String TownShtDesc) {
        this.TownShtDesc = TownShtDesc;
    }

    public String getTownShtDesc() {
        return TownShtDesc;
    }

    public void setTownName(String TownName) {
        this.TownName = TownName;
    }

    public String getTownName() {
        return TownName;
    }

    public void setPostalZIPCode(String PostalZIPCode) {
        this.PostalZIPCode = PostalZIPCode;
    }

    public String getPostalZIPCode() {
        return PostalZIPCode;
    }

    public void setSectorShtDesc(String SectorShtDesc) {
        this.SectorShtDesc = SectorShtDesc;
    }

    public String getSectorShtDesc() {
        return SectorShtDesc;
    }

    public void setSectorName(String SectorName) {
        this.SectorName = SectorName;
    }

    public String getSectorName() {
        return SectorName;
    }

    public void setSectorCode(BigDecimal SectorCode) {
        this.SectorCode = SectorCode;
    }

    public BigDecimal getSectorCode() {
        return SectorCode;
    }

    public void setBankName(String BankName) {
        this.BankName = BankName;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBranchName(String BranchName) {
        this.BranchName = BranchName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBankBranchCode(BigDecimal BankBranchCode) {
        this.BankBranchCode = BankBranchCode;
    }

    public BigDecimal getBankBranchCode() {
        return BankBranchCode;
    }


    public void setRDOC_CODE(BigDecimal RDOC_CODE) {
        this.RDOC_CODE = RDOC_CODE;
    }

    public BigDecimal getRDOC_CODE() {
        return RDOC_CODE;
    }

    public void setRDOC_SHT_DESC(String RDOC_SHT_DESC) {
        this.RDOC_SHT_DESC = RDOC_SHT_DESC;
    }

    public String getRDOC_SHT_DESC() {
        return RDOC_SHT_DESC;
    }

    public void setROC_DESC(String ROC_DESC) {
        this.ROC_DESC = ROC_DESC;
    }

    public String getROC_DESC() {
        return ROC_DESC;
    }

    public void setROC_MANDATORY(String ROC_MANDATORY) {
        this.ROC_MANDATORY = ROC_MANDATORY;
    }

    public String getROC_MANDATORY() {
        return ROC_MANDATORY;
    }

    public void setROC_DATE_ADDED(Date ROC_DATE_ADDED) {
        this.ROC_DATE_ADDED = ROC_DATE_ADDED;
    }

    public Date getROC_DATE_ADDED() {
        return ROC_DATE_ADDED;
    }

    public void setCDOCR_CODE(BigDecimal CDOCR_CODE) {
        this.CDOCR_CODE = CDOCR_CODE;
    }

    public BigDecimal getCDOCR_CODE() {
        return CDOCR_CODE;
    }

    public void setCDOCR_RDOC_CODE(BigDecimal CDOCR_RDOC_CODE) {
        this.CDOCR_RDOC_CODE = CDOCR_RDOC_CODE;
    }

    public BigDecimal getCDOCR_RDOC_CODE() {
        return CDOCR_RDOC_CODE;
    }

    public void setCDOCR_CLNT_CODE(BigDecimal CDOCR_CLNT_CODE) {
        this.CDOCR_CLNT_CODE = CDOCR_CLNT_CODE;
    }

    public BigDecimal getCDOCR_CLNT_CODE() {
        return CDOCR_CLNT_CODE;
    }

    public void setCDOCR_SUBMITED(String CDOCR_SUBMITED) {
        this.CDOCR_SUBMITED = CDOCR_SUBMITED;
    }

    public String getCDOCR_SUBMITED() {
        return CDOCR_SUBMITED;
    }

    public void setCDOCR_DATE_S(Date CDOCR_DATE_S) {
        this.CDOCR_DATE_S = CDOCR_DATE_S;
    }

    public Date getCDOCR_DATE_S() {
        return CDOCR_DATE_S;
    }

    public void setCDOCR_REF_NO(String CDOCR_REF_NO) {
        this.CDOCR_REF_NO = CDOCR_REF_NO;
    }

    public String getCDOCR_REF_NO() {
        return CDOCR_REF_NO;
    }

    public void setCDOCR_RMRK(String CDOCR_RMRK) {
        this.CDOCR_RMRK = CDOCR_RMRK;
    }

    public String getCDOCR_RMRK() {
        return CDOCR_RMRK;
    }

    public void setCDOCR_USER_RECEIVD(String CDOCR_USER_RECEIVD) {
        this.CDOCR_USER_RECEIVD = CDOCR_USER_RECEIVD;
    }

    public String getCDOCR_USER_RECEIVD() {
        return CDOCR_USER_RECEIVD;
    }

    public void setUSR_USERNAME(String USR_USERNAME) {
        this.USR_USERNAME = USR_USERNAME;
    }

    public String getUSR_USERNAME() {
        return USR_USERNAME;
    }

    public void setUSR_NAME(String USR_NAME) {
        this.USR_NAME = USR_NAME;
    }

    public String getUSR_NAME() {
        return USR_NAME;
    }

    public void setUSR_CODE(BigDecimal USR_CODE) {
        this.USR_CODE = USR_CODE;
    }

    public BigDecimal getUSR_CODE() {
        return USR_CODE;
    }

    public void setCDOCR_RDOC_NAME(String CDOCR_RDOC_NAME) {
        this.CDOCR_RDOC_NAME = CDOCR_RDOC_NAME;
    }

    public String getCDOCR_RDOC_NAME() {
        return CDOCR_RDOC_NAME;
    }

    public void setCDOCR_DOCID(String CDOCR_DOCID) {
        this.CDOCR_DOCID = CDOCR_DOCID;
    }

    public String getCDOCR_DOCID() {
        return CDOCR_DOCID;
    }
}
