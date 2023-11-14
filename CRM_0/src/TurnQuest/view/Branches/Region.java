package TurnQuest.view.Branches;

import java.math.BigDecimal;

import java.util.List;


public class Region {

    private BigDecimal regionCode;
    private BigDecimal regOrgCode;
    private String regionName;
    private String nodeType;

    private List<Branch> branchesList;

    public Region() {
        super();
    }

    public void setRegionCode(BigDecimal regionCode) {
        this.regionCode = regionCode;
    }

    public BigDecimal getRegionCode() {
        return regionCode;
    }

    public void setRegOrgCode(BigDecimal regOrgCode) {
        this.regOrgCode = regOrgCode;
    }

    public BigDecimal getRegOrgCode() {
        return regOrgCode;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setBranchesList(List<Branch> branchesList) {
        this.branchesList = branchesList;
    }

    public List<Branch> getBranchesList() {
        return branchesList;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }
}
