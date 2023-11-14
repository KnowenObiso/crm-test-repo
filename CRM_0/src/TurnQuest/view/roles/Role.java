package TurnQuest.view.roles;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


public class Role {
    public Role() {
        super();
    }

    private BigDecimal processCode;
    private BigDecimal roleProcessCode;

    private BigDecimal roleCode;
    private String roleName;
    private String processName;
    private Date roleCrtDate;
    private String roleStatus;
    private String roleShtDesc;
    private String roleAuthorized;

    private BigDecimal processAreaCode;
    private String processArea;
    private BigDecimal processRoleAreaCode;
    private boolean processAreaSelected;
    private String areaSubArea;
    private List<SubRole> subAreas;

    private String nodeType; // Custom field used to check whether this is a
    // primary or secondary field 'P' or 'S'

    private Boolean roleSelected;
    private Date wetDate;
    private Date wefDate;
    

    
    private BigDecimal mkcCode;
    private String sprsaSubArea;
    private boolean mkcStatus;
    private String mkcApplicableArea;


    public void setProcessCode(BigDecimal processCode) {
        this.processCode = processCode;
    }

    public BigDecimal getProcessCode() {
        return processCode;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setRoleCrtDate(Date roleCrtDate) {
        this.roleCrtDate = roleCrtDate;
    }

    public Date getRoleCrtDate() {
        return roleCrtDate;
    }

    public void setRoleStatus(String roleStatus) {
        this.roleStatus = roleStatus;
    }

    public String getRoleStatus() {
        return roleStatus;
    }

    public void setRoleProcessCode(BigDecimal roleProcessCode) {
        this.roleProcessCode = roleProcessCode;
    }

    public BigDecimal getRoleProcessCode() {
        return roleProcessCode;
    }

    public void setProcessAreaCode(BigDecimal processAreaCode) {
        this.processAreaCode = processAreaCode;
    }

    public BigDecimal getProcessAreaCode() {
        return processAreaCode;
    }

    public void setProcessArea(String processArea) {
        this.processArea = processArea;
    }

    public String getProcessArea() {
        return processArea;
    }

    public void setProcessRoleAreaCode(BigDecimal processRoleAreaCode) {
        this.processRoleAreaCode = processRoleAreaCode;
    }

    public BigDecimal getProcessRoleAreaCode() {
        return processRoleAreaCode;
    }


    public void setSubAreas(List<SubRole> subAreas) {
        this.subAreas = subAreas;
    }

    public List<SubRole> getSubAreas() {
        return subAreas;
    }

    public void setProcessAreaSelected(boolean processAreaSelected) {
        this.processAreaSelected = processAreaSelected;
    }

    public boolean getProcessAreaSelected() {
        return processAreaSelected;
    }

    public void setAreaSubArea(String areaSubArea) {
        this.areaSubArea = areaSubArea;
    }

    public String getAreaSubArea() {
        return areaSubArea;
    }

    public void setRoleShtDesc(String roleShtDesc) {
        this.roleShtDesc = roleShtDesc;
    }

    public String getRoleShtDesc() {
        return roleShtDesc;
    }

    public void setRoleCode(BigDecimal roleCode) {
        this.roleCode = roleCode;
    }

    public BigDecimal getRoleCode() {
        return roleCode;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setRoleSelected(Boolean roleSelected) {
        this.roleSelected = roleSelected;
    }

    public Boolean getRoleSelected() {
        return roleSelected;
    }

    public void setWetDate(Date wetDate) {
        this.wetDate = wetDate;
    }

    public Date getWetDate() {
        return wetDate;
    }
    public void setWefDate(Date wefDate) {
        this.wefDate = wefDate;
    }

    public Date getWefDate() {
        return wefDate;
    }

  public void setRoleAuthorized(String roleAuthorized)
  {
    this.roleAuthorized = roleAuthorized;
  }

  public String getRoleAuthorized()
  {
    return roleAuthorized;
  }

    public void setMkcCode(BigDecimal mkcCode) {
        this.mkcCode = mkcCode;
    }

    public BigDecimal getMkcCode() {
        return mkcCode;
    }

    public void setSprsaSubArea(String sprsaSubArea) {
        this.sprsaSubArea = sprsaSubArea;
    }

    public String getSprsaSubArea() {
        return sprsaSubArea;
    }


    public void setMkcApplicableArea(String mkcApplicableArea) {
        this.mkcApplicableArea = mkcApplicableArea;
    }

    public String getMkcApplicableArea() {
        return mkcApplicableArea;
    }


    public void setMkcStatus(boolean mkcStatus) {
        this.mkcStatus = mkcStatus;
    }

    public boolean isMkcStatus() {
        return mkcStatus;
    }
}
