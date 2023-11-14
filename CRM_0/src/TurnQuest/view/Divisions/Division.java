package TurnQuest.view.Divisions;

import java.math.BigDecimal;

import java.util.Date;


public class Division {
    public Division() {
        super();
    }
    private BigDecimal DIV_CODE;
    private String DIV_NAME;
    private String DIV_SHT_DESC;
    private String DIV_DIVISION_STATUS;

    private BigDecimal SDIV_CODE;
    private String SDIV_NAME;
    private String SDIV_SHT_DESC;
    private BigDecimal SDIV_DIV_CODE;

    private BigDecimal BDIV_CODE;
    private String BRN_NAME;
    private Date BDIV_WEF;
    private Date BDIV_WET;

    private BigDecimal BRN_CODE;

    private BigDecimal userDiv_code;
    private BigDecimal userDiv_userCode;
    private BigDecimal userDiv_divCode;
    private String userDiv_default;
    private String nodeType;
    private String divOrder;
    private String divDirectorCode;
    private BigDecimal orgCode;
    private BigDecimal branchCode;

    private String divDirectorName;

    private String divAssDirectorCode;

    private String divAssDirectorName;
    private Boolean divisionSelected;
    private BigDecimal rorgCode;
    private String rorgDesc;
    private String rorgShtDesc;

    private BigDecimal orsCode;
    private String orsDesc;


    public void setDIV_CODE(BigDecimal DIV_CODE) {
        this.DIV_CODE = DIV_CODE;
    }

    public BigDecimal getDIV_CODE() {
        return DIV_CODE;
    }

    public void setDIV_NAME(String DIV_NAME) {
        this.DIV_NAME = DIV_NAME;
    }

    public String getDIV_NAME() {
        return DIV_NAME;
    }

    public void setDIV_SHT_DESC(String DIV_SHT_DESC) {
        this.DIV_SHT_DESC = DIV_SHT_DESC;
    }

    public String getDIV_SHT_DESC() {
        return DIV_SHT_DESC;
    }

    public void setDIV_DIVISION_STATUS(String DIV_DIVISION_STATUS) {
        this.DIV_DIVISION_STATUS = DIV_DIVISION_STATUS;
    }

    public String getDIV_DIVISION_STATUS() {
        return DIV_DIVISION_STATUS;
    }

    public void setSDIV_CODE(BigDecimal SDIV_CODE) {
        this.SDIV_CODE = SDIV_CODE;
    }

    public BigDecimal getSDIV_CODE() {
        return SDIV_CODE;
    }

    public void setSDIV_NAME(String SDIV_NAME) {
        this.SDIV_NAME = SDIV_NAME;
    }

    public String getSDIV_NAME() {
        return SDIV_NAME;
    }

    public void setSDIV_SHT_DESC(String SDIV_SHT_DESC) {
        this.SDIV_SHT_DESC = SDIV_SHT_DESC;
    }

    public String getSDIV_SHT_DESC() {
        return SDIV_SHT_DESC;
    }

    public void setSDIV_DIV_CODE(BigDecimal SDIV_DIV_CODE) {
        this.SDIV_DIV_CODE = SDIV_DIV_CODE;
    }

    public BigDecimal getSDIV_DIV_CODE() {
        return SDIV_DIV_CODE;
    }

    public void setBDIV_CODE(BigDecimal BDIV_CODE) {
        this.BDIV_CODE = BDIV_CODE;
    }

    public BigDecimal getBDIV_CODE() {
        return BDIV_CODE;
    }

    public void setBRN_NAME(String BRN_NAME) {
        this.BRN_NAME = BRN_NAME;
    }

    public String getBRN_NAME() {
        return BRN_NAME;
    }

    public void setBDIV_WEF(Date BDIV_WEF) {
        this.BDIV_WEF = BDIV_WEF;
    }

    public Date getBDIV_WEF() {
        return BDIV_WEF;
    }

    public void setBDIV_WET(Date BDIV_WET) {
        this.BDIV_WET = BDIV_WET;
    }

    public Date getBDIV_WET() {
        return BDIV_WET;
    }

    public void setBRN_CODE(BigDecimal BRN_CODE) {
        this.BRN_CODE = BRN_CODE;
    }

    public BigDecimal getBRN_CODE() {
        return BRN_CODE;
    }

    public void setUserDiv_code(BigDecimal userDiv_code) {
        this.userDiv_code = userDiv_code;
    }

    public BigDecimal getUserDiv_code() {
        return userDiv_code;
    }

    public void setUserDiv_userCode(BigDecimal userDiv_userCode) {
        this.userDiv_userCode = userDiv_userCode;
    }

    public BigDecimal getUserDiv_userCode() {
        return userDiv_userCode;
    }

    public void setUserDiv_divCode(BigDecimal userDiv_divCode) {
        this.userDiv_divCode = userDiv_divCode;
    }

    public BigDecimal getUserDiv_divCode() {
        return userDiv_divCode;
    }

    public void setUserDiv_default(String userDiv_default) {
        this.userDiv_default = userDiv_default;
    }

    public String getUserDiv_default() {
        return userDiv_default;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setDivOrder(String divOrder) {
        this.divOrder = divOrder;
    }

    public String getDivOrder() {
        return divOrder;
    }

    public void setDivDirectorCode(String divDirectorCode) {
        this.divDirectorCode = divDirectorCode;
    }

    public String getDivDirectorCode() {
        return divDirectorCode;
    }

    public void setDivDirectorName(String divDirectorName) {
        this.divDirectorName = divDirectorName;
    }

    public String getDivDirectorName() {
        return divDirectorName;
    }

    public void setDivAssDirectorCode(String divAssDirectorCode) {
        this.divAssDirectorCode = divAssDirectorCode;
    }

    public String getDivAssDirectorCode() {
        return divAssDirectorCode;
    }

    public void setDivAssDirectorName(String divAssDirectorName) {
        this.divAssDirectorName = divAssDirectorName;
    }

    public String getDivAssDirectorName() {
        return divAssDirectorName;
    }

    public void setDivisionSelected(Boolean divisionSelected) {
        this.divisionSelected = divisionSelected;
    }

    public Boolean getDivisionSelected() {
        return divisionSelected;
    }

    public void setOrgCode(BigDecimal orgCode) {
        this.orgCode = orgCode;
    }

    public BigDecimal getOrgCode() {
        return orgCode;
    }

    public void setBranchCode(BigDecimal branchCode) {
        this.branchCode = branchCode;
    }

    public BigDecimal getBranchCode() {
        return branchCode;
    }

    public void setRorgCode(BigDecimal rorgCode) {
        this.rorgCode = rorgCode;
    }

    public BigDecimal getRorgCode() {
        return rorgCode;
    }

    public void setRorgDesc(String rorgDesc) {
        this.rorgDesc = rorgDesc;
    }

    public String getRorgDesc() {
        return rorgDesc;
    }

    public void setRorgShtDesc(String rorgShtDesc) {
        this.rorgShtDesc = rorgShtDesc;
    }

    public String getRorgShtDesc() {
        return rorgShtDesc;
    }


    public void setOrsDesc(String orsDesc) {
        this.orsDesc = orsDesc;
    }

    public String getOrsDesc() {
        return orsDesc;
    }

    public void setOrsCode(BigDecimal orsCode) {
        this.orsCode = orsCode;
    }

    public BigDecimal getOrsCode() {
        return orsCode;
    }
}
