package TurnQuest.view.Settings1;

import java.math.BigDecimal;

public class AppSettings {
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
    private String bankShortDesc;

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

    public void setBankShortDesc(String bankShortDesc) {
        this.bankShortDesc = bankShortDesc;
    }

    public String getBankShortDesc() {
        return bankShortDesc;
    }
}
