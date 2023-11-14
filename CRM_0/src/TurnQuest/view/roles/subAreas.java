package TurnQuest.view.roles;

import java.math.BigDecimal;

public class subAreas {
    private BigDecimal processRoleSubAreaCode;
    private BigDecimal processSubAreaCode;
    private String processSubArea;
    private String processSubAreaType;
    private BigDecimal processSubAreaDebitLimit;
    private BigDecimal processSubAredCreditLimit;
    private boolean processSubAreaSelected;
    private String areaSubArea;

    public void setProcessRoleSubAreaCode(BigDecimal processRoleSubAreaCode) {
        this.processRoleSubAreaCode = processRoleSubAreaCode;
    }

    public BigDecimal getProcessRoleSubAreaCode() {
        return processRoleSubAreaCode;
    }

    public void setProcessSubAreaCode(BigDecimal processSubAreaCode) {
        this.processSubAreaCode = processSubAreaCode;
    }

    public BigDecimal getProcessSubAreaCode() {
        return processSubAreaCode;
    }

    public void setProcessSubArea(String processSubArea) {
        this.processSubArea = processSubArea;
    }

    public String getProcessSubArea() {
        return processSubArea;
    }

    public void setProcessSubAreaType(String processSubAreaType) {
        this.processSubAreaType = processSubAreaType;
    }

    public String getProcessSubAreaType() {
        return processSubAreaType;
    }

    public void setProcessSubAreaDebitLimit(BigDecimal processSubAreaDebitLimit) {
        this.processSubAreaDebitLimit = processSubAreaDebitLimit;
    }

    public BigDecimal getProcessSubAreaDebitLimit() {
        return processSubAreaDebitLimit;
    }

    public void setProcessSubAredCreditLimit(BigDecimal processSubAredCreditLimit) {
        this.processSubAredCreditLimit = processSubAredCreditLimit;
    }

    public BigDecimal getProcessSubAredCreditLimit() {
        return processSubAredCreditLimit;
    }

    public void setProcessSubAreaSelected(boolean processSubAreaSelected) {
        this.processSubAreaSelected = processSubAreaSelected;
    }

    public boolean isProcessSubAreaSelected() {
        return processSubAreaSelected;
    }

    public void setAreaSubArea(String areaSubArea) {
        this.areaSubArea = areaSubArea;
    }

    public String getAreaSubArea() {
        return areaSubArea;
    }
}
