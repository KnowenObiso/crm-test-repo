package TurnQuest.view.Leads;

import java.math.BigDecimal;

public class LeadStatus {
    private BigDecimal leadStatusCode;
    private String leadStatusDesc;

    public void setLeadStatusCode(BigDecimal leadStatusCode) {
        this.leadStatusCode = leadStatusCode;
    }

    public BigDecimal getLeadStatusCode() {
        return leadStatusCode;
    }

    public void setLeadStatusDesc(String leadStatusDesc) {
        this.leadStatusDesc = leadStatusDesc;
    }

    public String getLeadStatusDesc() {
        return leadStatusDesc;
    }
}
