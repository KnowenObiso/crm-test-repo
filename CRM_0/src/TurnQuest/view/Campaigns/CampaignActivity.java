package TurnQuest.view.Campaigns;

import java.math.BigDecimal;

import java.sql.Date;


public class CampaignActivity {
    private BigDecimal campActCode;
    private BigDecimal campCode;
    private BigDecimal ActCode;
    private String actSubject;
    private String ActName;
    private Date actWef;
    private Date actWet;

    public void setActName(String ActName) {
        this.ActName = ActName;
    }

    public String getActName() {
        return ActName;
    }

    public void setCampActCode(BigDecimal campActCode) {
        this.campActCode = campActCode;
    }

    public BigDecimal getCampActCode() {
        return campActCode;
    }

    public void setCampCode(BigDecimal campCode) {
        this.campCode = campCode;
    }

    public BigDecimal getCampCode() {
        return campCode;
    }


    public void setActCode(BigDecimal ActCode) {
        this.ActCode = ActCode;
    }

    public BigDecimal getActCode() {
        return ActCode;
    }

    public void setActSubject(String actSubject) {
        this.actSubject = actSubject;
    }

    public String getActSubject() {
        return actSubject;
    }

    public void setActWef(Date actWef) {
        this.actWef = actWef;
    }

    public Date getActWef() {
        return actWef;
    }

    public void setActWet(Date actWet) {
        this.actWet = actWet;
    }

    public Date getActWet() {
        return actWet;
    }
}
