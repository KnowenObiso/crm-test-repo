package TurnQuest.view.Activities;

import java.math.BigDecimal;

import java.sql.Date;


public class Activity {

    private BigDecimal activityCode;
    private BigDecimal activityTypeCode;
    private Date activityWEF;
    private Date activityWET;
    private BigDecimal activityDuration;
    private String activitysubject;
    private String activityLocation;
    private BigDecimal activityAssignedTo;
    private BigDecimal activityRelatedTo;
    private String activityRelatedAccount;
    private BigDecimal activityStatusId;
    private String activityDescription;
    private String activityReminder;
    private BigDecimal activityTeam;
    private String activityType;
    private String activityAssignedUser;
    private String activityStatus;
    private String activityTeamName;
    private Date activityReminderTime;
    private String msgTemplateCode;
    private String msgTemplateDesc;
    private Date actWef;
    private Date actWet;

    public void setActivityCode(BigDecimal activityCode) {
        this.activityCode = activityCode;
    }

    public BigDecimal getActivityCode() {
        return activityCode;
    }

    public void setActivityTypeCode(BigDecimal activityTypeCode) {
        this.activityTypeCode = activityTypeCode;
    }

    public BigDecimal getActivityTypeCode() {
        return activityTypeCode;
    }

    public void setActivityWEF(Date activityWEF) {
        this.activityWEF = activityWEF;
    }

    public Date getActivityWEF() {
        return activityWEF;
    }

    public void setActivityWET(Date activityWET) {
        this.activityWET = activityWET;
    }

    public Date getActivityWET() {
        return activityWET;
    }

    public void setActivityDuration(BigDecimal activityDuration) {
        this.activityDuration = activityDuration;
    }

    public BigDecimal getActivityDuration() {
        return activityDuration;
    }

    public void setActivitysubject(String activitysubject) {
        this.activitysubject = activitysubject;
    }

    public String getActivitysubject() {
        return activitysubject;
    }

    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }

    public String getActivityLocation() {
        return activityLocation;
    }

    public void setActivityAssignedTo(BigDecimal activityAssignedTo) {
        this.activityAssignedTo = activityAssignedTo;
    }

    public BigDecimal getActivityAssignedTo() {
        return activityAssignedTo;
    }


    public void setActivityStatusId(BigDecimal activityStatusId) {
        this.activityStatusId = activityStatusId;
    }

    public BigDecimal getActivityStatusId() {
        return activityStatusId;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityReminder(String activityReminder) {
        this.activityReminder = activityReminder;
    }

    public String getActivityReminder() {
        return activityReminder;
    }

    public void setActivityTeam(BigDecimal activityTeam) {
        this.activityTeam = activityTeam;
    }

    public BigDecimal getActivityTeam() {
        return activityTeam;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityType() {
        return activityType;
    }


    public void setActivityAssignedUser(String activityAssignedUser) {
        this.activityAssignedUser = activityAssignedUser;
    }

    public String getActivityAssignedUser() {
        return activityAssignedUser;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityTeamName(String activityTeamName) {
        this.activityTeamName = activityTeamName;
    }

    public String getActivityTeamName() {
        return activityTeamName;
    }

    public void setActivityRelatedTo(BigDecimal activityRelatedTo) {
        this.activityRelatedTo = activityRelatedTo;
    }

    public BigDecimal getActivityRelatedTo() {
        return activityRelatedTo;
    }

    public void setActivityRelatedAccount(String activityRelatedAccount) {
        this.activityRelatedAccount = activityRelatedAccount;
    }

    public String getActivityRelatedAccount() {
        return activityRelatedAccount;
    }

    public void setActivityReminderTime(Date activityReminderTime) {
        this.activityReminderTime = activityReminderTime;
    }

    public Date getActivityReminderTime() {
        return activityReminderTime;
    }

    public void setMsgTemplateCode(String msgTemplateCode) {
        this.msgTemplateCode = msgTemplateCode;
    }

    public String getMsgTemplateCode() {
        return msgTemplateCode;
    }

    public void setMsgTemplateDesc(String msgTemplateDesc) {
        this.msgTemplateDesc = msgTemplateDesc;
    }

    public String getMsgTemplateDesc() {
        return msgTemplateDesc;
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
