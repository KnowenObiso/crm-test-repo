package TurnQuest.view.Branches;


import TurnQuest.view.Divisions.Division;

import java.math.BigDecimal;

import java.util.List;


public class Branch {
    public Branch() {
        super();
    }

    private BigDecimal orgCode;
    private String orgShtDesc;
    private String orgName;

    private BigDecimal regCode;
    private BigDecimal regOrgCode;
    private String regName;
    private String regShtDesc;

    private BigDecimal usrBranchCode;
    private BigDecimal branchCode;
    private String branchShtDesc;
    private BigDecimal usrCode;
    private String usrStatus;
    private String usrDftBranch;
    private String branchName;
    private Boolean branchSelected;
    private List<Region> regionsList;

    private List<Division> divisionsList;

    private String nodeType; // Custom field used to check whether this is a
    // primary or secondary field 'P' or 'S'


    public void setOrgCode(BigDecimal orgCode) {
        this.orgCode = orgCode;
    }

    public BigDecimal getOrgCode() {
        return orgCode;
    }

    public void setOrgShtDesc(String orgShtDesc) {
        this.orgShtDesc = orgShtDesc;
    }

    public String getOrgShtDesc() {
        return orgShtDesc;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setRegCode(BigDecimal regCode) {
        this.regCode = regCode;
    }

    public BigDecimal getRegCode() {
        return regCode;
    }

    public void setRegOrgCode(BigDecimal regOrgCode) {
        this.regOrgCode = regOrgCode;
    }

    public BigDecimal getRegOrgCode() {
        return regOrgCode;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public String getRegName() {
        return regName;
    }

    public void setUsrBranchCode(BigDecimal usrBranchCode) {
        this.usrBranchCode = usrBranchCode;
    }

    public BigDecimal getUsrBranchCode() {
        return usrBranchCode;
    }

    public void setBranchCode(BigDecimal branchCode) {
        this.branchCode = branchCode;
    }

    public BigDecimal getBranchCode() {
        return branchCode;
    }

    public void setUsrCode(BigDecimal usrCode) {
        this.usrCode = usrCode;
    }

    public BigDecimal getUsrCode() {
        return usrCode;
    }

    public void setUsrStatus(String usrStatus) {
        this.usrStatus = usrStatus;
    }

    public String getUsrStatus() {
        return usrStatus;
    }

    public void setUsrDftBranch(String usrDftBranch) {
        this.usrDftBranch = usrDftBranch;
    }

    public String getUsrDftBranch() {
        return usrDftBranch;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setRegionsList(List<Region> regionsList) {
        this.regionsList = regionsList;
    }

    public List<Region> getRegionsList() {
        return regionsList;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setDivisionsList(List<Division> divisionsList) {
        this.divisionsList = divisionsList;
    }

    public List<Division> getDivisionsList() {
        return divisionsList;
    }

    public void setBranchSelected(Boolean branchSelected) {
        this.branchSelected = branchSelected;
    }

    public Boolean getBranchSelected() {
        return branchSelected;
    }

    public void setRegShtDesc(String regShtDesc) {
        this.regShtDesc = regShtDesc;
    }

    public String getRegShtDesc() {
        return regShtDesc;
    }

    public void setBranchShtDesc(String branchShtDesc) {
        this.branchShtDesc = branchShtDesc;
    }

    public String getBranchShtDesc() {
        return branchShtDesc;
    }
}
