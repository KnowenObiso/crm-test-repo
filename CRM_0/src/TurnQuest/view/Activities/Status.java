package TurnQuest.view.Activities;

import java.math.BigDecimal;

public class Status {
    private BigDecimal statusId;
    private String statusCode;
    private String statusDecription;

    public void setStatusId(BigDecimal statusId) {
        this.statusId = statusId;
    }

    public BigDecimal getStatusId() {
        return statusId;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusDecription(String statusDecription) {
        this.statusDecription = statusDecription;
    }

    public String getStatusDecription() {
        return statusDecription;
    }
}
