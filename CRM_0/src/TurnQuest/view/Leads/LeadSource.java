package TurnQuest.view.Leads;

import java.math.BigDecimal;

public class LeadSource {
    private BigDecimal leadSourceCode;
    private String leadSourceDesc;

    public void setLeadSourceCode(BigDecimal leadSourceCode) {
        this.leadSourceCode = leadSourceCode;
    }

    public BigDecimal getLeadSourceCode() {
        return leadSourceCode;
    }

    public void setLeadSourceDesc(String leadSourceDesc) {
        this.leadSourceDesc = leadSourceDesc;
    }

    public String getLeadSourceDesc() {
        return leadSourceDesc;
    }
}
