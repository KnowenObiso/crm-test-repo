package TurnQuest.view.models;

import java.math.BigDecimal;

public class ClaimPaymentModes {
    public ClaimPaymentModes() {
        super();
    }
    private BigDecimal code;
    private String shortdesc;
    private String description;
    private String maxAmt;
    private String minAmt;
    private String default_value;
    private String remarks;
    private BigDecimal couCode;

    //mobileType Prefix

    private String prefix;
    private BigDecimal mptCode;
    private BigDecimal mptpCode;


    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setMaxAmt(String maxAmt) {
        this.maxAmt = maxAmt;
    }

    public String getMaxAmt() {
        return maxAmt;
    }

    public void setMinAmt(String minAmt) {
        this.minAmt = minAmt;
    }

    public String getMinAmt() {
        return minAmt;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setCouCode(BigDecimal couCode) {
        this.couCode = couCode;
    }

    public BigDecimal getCouCode() {
        return couCode;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setMptCode(BigDecimal mptCode) {
        this.mptCode = mptCode;
    }

    public BigDecimal getMptCode() {
        return mptCode;
    }

    public void setMptpCode(BigDecimal mptpCode) {
        this.mptpCode = mptpCode;
    }

    public BigDecimal getMptpCode() {
        return mptpCode;
    }
}
