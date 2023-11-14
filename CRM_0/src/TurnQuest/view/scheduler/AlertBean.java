package TurnQuest.view.scheduler;


import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;


public class AlertBean {
    private String qtJobName;
    private String qtDescription;
    private Date qtNextFireTime;
    private Date qtPrevFireTime;
    private Timestamp qtStartTime;
    private Date qtEndTime;
    private BigDecimal qtSysCode;
    private String qtRecurrence;
    private String qtRecurrenceType;
    private BigDecimal qtJobAssignee;
    private BigDecimal qtNotifiedFailUser;
    private BigDecimal qtNotifiedSuccUser;
    private String qtReccurenceInterval;
    private String qtJobType;
    private String qtJobTemplate;
    private String qtFailNotifyTemplate;
    private String qtSuccNotifyTemplate;
    private BigDecimal qtCode;
    private String assignee;
    private String failUser;
    private String succUser;
    private String jobTemplate;
    private String failTemplate;
    private String succTemplate;
    private String assigneeEmail;
    private String failEmail;
    private String succEmail;
    private String qtStatus;
    private String qtCronExpresion;

    private String qtThreshType;
    private BigDecimal qtThreshValue;

    private String objType;
    private BigDecimal objCode;
    private String objDesc;
    private String objLocation;

    public void setQtJobName(String qtJobName) {
        this.qtJobName = qtJobName;
    }

    public String getQtJobName() {
        return qtJobName;
    }

    public void setQtDescription(String qtDescription) {
        this.qtDescription = qtDescription;
    }

    public String getQtDescription() {
        return qtDescription;
    }

    public void setQtNextFireTime(Date qtNextFireTime) {
        this.qtNextFireTime = qtNextFireTime;
    }

    public Date getQtNextFireTime() {
        return qtNextFireTime;
    }

    public void setQtPrevFireTime(Date qtPrevFireTime) {
        this.qtPrevFireTime = qtPrevFireTime;
    }

    public Date getQtPrevFireTime() {
        return qtPrevFireTime;
    }

    public void setQtStartTime(Timestamp qtStartTime) {
        this.qtStartTime = qtStartTime;
    }

    public Timestamp getQtStartTime() {
        return qtStartTime;
    }

    public void setQtEndTime(Date qtEndTime) {
        this.qtEndTime = qtEndTime;
    }

    public Date getQtEndTime() {
        return qtEndTime;
    }

    public void setQtSysCode(BigDecimal qtSysCode) {
        this.qtSysCode = qtSysCode;
    }

    public BigDecimal getQtSysCode() {
        return qtSysCode;
    }

    public void setQtRecurrence(String qtRecurrence) {
        this.qtRecurrence = qtRecurrence;
    }

    public String getQtRecurrence() {
        return qtRecurrence;
    }

    public void setQtRecurrenceType(String qtRecurrenceType) {
        this.qtRecurrenceType = qtRecurrenceType;
    }

    public String getQtRecurrenceType() {
        return qtRecurrenceType;
    }

    public void setQtJobAssignee(BigDecimal qtJobAssignee) {
        this.qtJobAssignee = qtJobAssignee;
    }

    public BigDecimal getQtJobAssignee() {
        return qtJobAssignee;
    }

    public void setQtNotifiedFailUser(BigDecimal qtNotifiedFailUser) {
        this.qtNotifiedFailUser = qtNotifiedFailUser;
    }

    public BigDecimal getQtNotifiedFailUser() {
        return qtNotifiedFailUser;
    }

    public void setQtNotifiedSuccUser(BigDecimal qtNotifiedSuccUser) {
        this.qtNotifiedSuccUser = qtNotifiedSuccUser;
    }

    public BigDecimal getQtNotifiedSuccUser() {
        return qtNotifiedSuccUser;
    }

    public void setQtReccurenceInterval(String qtReccurenceInterval) {
        this.qtReccurenceInterval = qtReccurenceInterval;
    }

    public String getQtReccurenceInterval() {
        return qtReccurenceInterval;
    }

    public void setQtJobType(String qtJobType) {
        this.qtJobType = qtJobType;
    }

    public String getQtJobType() {
        return qtJobType;
    }

    public void setQtJobTemplate(String qtJobTemplate) {
        this.qtJobTemplate = qtJobTemplate;
    }

    public String getQtJobTemplate() {
        return qtJobTemplate;
    }

    public void setQtFailNotifyTemplate(String qtFailNotifyTemplate) {
        this.qtFailNotifyTemplate = qtFailNotifyTemplate;
    }

    public String getQtFailNotifyTemplate() {
        return qtFailNotifyTemplate;
    }

    public void setQtSuccNotifyTemplate(String qtSuccNotifyTemplate) {
        this.qtSuccNotifyTemplate = qtSuccNotifyTemplate;
    }

    public String getQtSuccNotifyTemplate() {
        return qtSuccNotifyTemplate;
    }

    public void setQtCode(BigDecimal qtCode) {
        this.qtCode = qtCode;
    }

    public BigDecimal getQtCode() {
        return qtCode;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setFailUser(String failUser) {
        this.failUser = failUser;
    }

    public String getFailUser() {
        return failUser;
    }

    public void setSuccUser(String succUser) {
        this.succUser = succUser;
    }

    public String getSuccUser() {
        return succUser;
    }

    public void setJobTemplate(String jobTemplate) {
        this.jobTemplate = jobTemplate;
    }

    public String getJobTemplate() {
        return jobTemplate;
    }

    public void setFailTemplate(String failTemplate) {
        this.failTemplate = failTemplate;
    }

    public String getFailTemplate() {
        return failTemplate;
    }

    public void setSuccTemplate(String succTemplate) {
        this.succTemplate = succTemplate;
    }

    public String getSuccTemplate() {
        return succTemplate;
    }

    public void setAssigneeEmail(String assigneeEmail) {
        this.assigneeEmail = assigneeEmail;
    }

    public String getAssigneeEmail() {
        return assigneeEmail;
    }

    public void setFailEmail(String failEmail) {
        this.failEmail = failEmail;
    }

    public String getFailEmail() {
        return failEmail;
    }

    public void setSuccEmail(String succEmail) {
        this.succEmail = succEmail;
    }

    public String getSuccEmail() {
        return succEmail;
    }

    public void setQtStatus(String qtStatus) {
        this.qtStatus = qtStatus;
    }

    public String getQtStatus() {
        return qtStatus;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjCode(BigDecimal objCode) {
        this.objCode = objCode;
    }

    public BigDecimal getObjCode() {
        return objCode;
    }

    public void setObjDesc(String objDesc) {
        this.objDesc = objDesc;
    }

    public String getObjDesc() {
        return objDesc;
    }

    public void setObjLocation(String objLocation) {
        this.objLocation = objLocation;
    }

    public String getObjLocation() {
        return objLocation;
    }

    public void setQtThreshType(String qtThreshType) {
        this.qtThreshType = qtThreshType;
    }

    public String getQtThreshType() {
        return qtThreshType;
    }

    public void setQtThreshValue(BigDecimal qtThreshValue) {
        this.qtThreshValue = qtThreshValue;
    }

    public BigDecimal getQtThreshValue() {
        return qtThreshValue;
    }

    public void setQtCronExpresion(String qtCronExpresion) {
        this.qtCronExpresion = qtCronExpresion;
    }

    public String getQtCronExpresion() {
        return qtCronExpresion;
    }
}
