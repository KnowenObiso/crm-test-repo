package TurnQuest.view.roles;

import java.math.BigDecimal;

import java.util.List;


public class Process {
    public Process() {
        super();
    }

    private BigDecimal processCode;
    private BigDecimal processRoleCode;
    private String processName;
    private String processAssigned;
    private boolean processSelected;
    private String areaSubArea;


    private List<Role> areas;

    public void setProcessCode(BigDecimal processCode) {
        this.processCode = processCode;
    }

    public BigDecimal getProcessCode() {
        return processCode;
    }

    public void setProcessRoleCode(BigDecimal processRoleCode) {
        this.processRoleCode = processRoleCode;
    }

    public BigDecimal getProcessRoleCode() {
        return processRoleCode;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessAssigned(String processAssigned) {
        this.processAssigned = processAssigned;
    }

    public String getProcessAssigned() {
        return processAssigned;
    }

    public void setAreas(List<Role> areas) {
        this.areas = areas;
    }

    public List<Role> getAreas() {
        return areas;
    }

    public void setProcessSelected(boolean processSelected) {
        this.processSelected = processSelected;
    }

    public boolean getProcessSelected() {
        return processSelected;
    }

    public void setAreaSubArea(String areaSubArea) {
        this.areaSubArea = areaSubArea;
    }

    public String getAreaSubArea() {
        return areaSubArea;
    }
}
