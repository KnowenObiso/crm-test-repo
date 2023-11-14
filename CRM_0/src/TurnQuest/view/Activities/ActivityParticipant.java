package TurnQuest.view.Activities;

import java.math.BigDecimal;

public class ActivityParticipant {
    private BigDecimal participantId;
    private BigDecimal participantActCode;
    private BigDecimal participantAccountCode;
    private String accountName;
    private String accountEmail;
    private Boolean selected;

    public void setParticipantId(BigDecimal participantId) {
        this.participantId = participantId;
    }

    public BigDecimal getParticipantId() {
        return participantId;
    }

    public void setParticipantActCode(BigDecimal participantActCode) {
        this.participantActCode = participantActCode;
    }

    public BigDecimal getParticipantActCode() {
        return participantActCode;
    }

    public void setParticipantAccountCode(BigDecimal participantAccountCode) {
        this.participantAccountCode = participantAccountCode;
    }

    public BigDecimal getParticipantAccountCode() {
        return participantAccountCode;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }
}
