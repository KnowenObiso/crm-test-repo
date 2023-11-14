package TurnQuest.view.Activities;

import java.math.BigDecimal;

import java.sql.Date;


public class ActivityTask {
    private BigDecimal tastCode;
    private BigDecimal activityCode;
    private Date taskFrom;
    private Date taskTo;
    private String taskSubject;
    private BigDecimal taskStatusCode;
    private BigDecimal taskPriorityCode;
    private BigDecimal taskAccountCode;
    private String Status;
    private String Priority;
    private String RelatedAccount;

    public void setTastCode(BigDecimal tastCode) {
        this.tastCode = tastCode;
    }

    public BigDecimal getTastCode() {
        return tastCode;
    }

    public void setActivityCode(BigDecimal activityCode) {
        this.activityCode = activityCode;
    }

    public BigDecimal getActivityCode() {
        return activityCode;
    }

    public void setTaskFrom(Date taskFrom) {
        this.taskFrom = taskFrom;
    }

    public Date getTaskFrom() {
        return taskFrom;
    }

    public void setTaskTo(Date taskTo) {
        this.taskTo = taskTo;
    }

    public Date getTaskTo() {
        return taskTo;
    }

    public void setTaskSubject(String taskSubject) {
        this.taskSubject = taskSubject;
    }

    public String getTaskSubject() {
        return taskSubject;
    }

    public void setTaskStatusCode(BigDecimal taskStatusCode) {
        this.taskStatusCode = taskStatusCode;
    }

    public BigDecimal getTaskStatusCode() {
        return taskStatusCode;
    }

    public void setTaskPriorityCode(BigDecimal taskPriorityCode) {
        this.taskPriorityCode = taskPriorityCode;
    }

    public BigDecimal getTaskPriorityCode() {
        return taskPriorityCode;
    }

    public void setTaskAccountCode(BigDecimal taskAccountCode) {
        this.taskAccountCode = taskAccountCode;
    }

    public BigDecimal getTaskAccountCode() {
        return taskAccountCode;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getStatus() {
        return Status;
    }

    public void setPriority(String Priority) {
        this.Priority = Priority;
    }

    public String getPriority() {
        return Priority;
    }

    public void setRelatedAccount(String RelatedAccount) {
        this.RelatedAccount = RelatedAccount;
    }

    public String getRelatedAccount() {
        return RelatedAccount;
    }
}
