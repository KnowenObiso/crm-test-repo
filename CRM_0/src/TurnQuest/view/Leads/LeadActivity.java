package TurnQuest.view.Leads;

import java.math.BigDecimal;


public class LeadActivity {
    private BigDecimal leadActivityCode;
    private BigDecimal activityCode;
    private String activitySubject;
    private String activityLocation;

    public void setLeadActivityCode(BigDecimal leadActivityCode) {
        this.leadActivityCode = leadActivityCode;
    }

    public BigDecimal getLeadActivityCode() {
        return leadActivityCode;
    }

    public void setActivityCode(BigDecimal activityCode) {
        this.activityCode = activityCode;
    }

    public BigDecimal getActivityCode() {
        return activityCode;
    }

    public void setActivitySubject(String activitySubject) {
        this.activitySubject = activitySubject;
    }

    public String getActivitySubject() {
        return activitySubject;
    }

    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }

    public String getActivityLocation() {
        return activityLocation;
    }
}
