package TurnQuest.view.Activities;

import java.math.BigDecimal;

public class PriorityLevel {
    private BigDecimal plCode;
    private String plDescription;
    private String prShortDescription;

    public void setPlCode(BigDecimal plCode) {
        this.plCode = plCode;
    }

    public BigDecimal getPlCode() {
        return plCode;
    }

    public void setPlDescription(String plDescription) {
        this.plDescription = plDescription;
    }

    public String getPlDescription() {
        return plDescription;
    }

    public void setPrShortDescription(String prShortDescription) {
        this.prShortDescription = prShortDescription;
    }

    public String getPrShortDescription() {
        return prShortDescription;
    }
}
