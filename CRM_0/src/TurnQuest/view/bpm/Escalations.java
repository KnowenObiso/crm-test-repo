package TurnQuest.view.bpm;

import java.math.BigDecimal;

import java.util.List;


public class Escalations {
    //private List<Escalations> escalations;
    private List<Escalations> levels;

    public Escalations() {
        super();
    }

    /**
     * Systems
     */
    private BigDecimal sysCode;
    private String sysShortDesc;
    private String sysName;

    private String type;
    private String name;
    private List<SysProcess> sysProcess;
    private String activityType;
    private String activityName;

    private BigDecimal code;
    private String jpdlName;
    private BigDecimal level;
    private BigDecimal userCode;
    private String username;
    private BigDecimal toUserCode;
    private String toUsername;
    private BigDecimal duration;
    private boolean select;
    private BigDecimal ccUser;
    private String ccUsername;
    
    
    private BigDecimal sreCode;
    private BigDecimal sreSrtCode;
    private BigDecimal sreLvlDuration;
    private BigDecimal sreLvlOneAssignee;
    private BigDecimal sreLvlTwoDuration;
    private BigDecimal sreLvlTwoAssignee;
    private String sreSrtName;
    private String sreLvlOneAssigneeName;
    private String sreLvlTwoAssigneeName;


    public void setSysCode(BigDecimal sysCode) {
        this.sysCode = sysCode;
    }

    public BigDecimal getSysCode() {
        return sysCode;
    }

    public void setSysShortDesc(String sysShortDesc) {
        this.sysShortDesc = sysShortDesc;
    }

    public String getSysShortDesc() {
        return sysShortDesc;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSysProcess(List<SysProcess> sysProcess) {
        this.sysProcess = sysProcess;
    }

    public List<SysProcess> getSysProcess() {
        return sysProcess;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setCode(BigDecimal code) {
        this.code = code;
    }

    public BigDecimal getCode() {
        return code;
    }

    public void setJpdlName(String jpdlName) {
        this.jpdlName = jpdlName;
    }

    public String getJpdlName() {
        return jpdlName;
    }

    public void setLevel(BigDecimal level) {
        this.level = level;
    }

    public BigDecimal getLevel() {
        return level;
    }

    public void setUserCode(BigDecimal userCode) {
        this.userCode = userCode;
    }

    public BigDecimal getUserCode() {
        return userCode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean getSelect() {
        return select;
    }

    public void setCcUser(BigDecimal ccUser) {
        this.ccUser = ccUser;
    }

    public BigDecimal getCcUser() {
        return ccUser;
    }

    public void setCcUsername(String ccUsername) {
        this.ccUsername = ccUsername;
    }

    public String getCcUsername() {
        return ccUsername;
    }

    public void setSreCode(BigDecimal sreCode) {
        this.sreCode = sreCode;
    }

    public BigDecimal getSreCode() {
        return sreCode;
    }

 

    public void setSreLvlDuration(BigDecimal sreLvlDuration) {
        this.sreLvlDuration = sreLvlDuration;
    }

    public BigDecimal getSreLvlDuration() {
        return sreLvlDuration;
    }

    public void setSreLvlOneAssignee(BigDecimal sreLvlOneAssignee) {
        this.sreLvlOneAssignee = sreLvlOneAssignee;
    }

    public BigDecimal getSreLvlOneAssignee() {
        return sreLvlOneAssignee;
    }

    public void setSreLvlTwoDuration(BigDecimal sreLvlTwoDuration) {
        this.sreLvlTwoDuration = sreLvlTwoDuration;
    }

    public BigDecimal getSreLvlTwoDuration() {
        return sreLvlTwoDuration;
    }

    public void setSreLvlTwoAssignee(BigDecimal sreLvlTwoAssignee) {
        this.sreLvlTwoAssignee = sreLvlTwoAssignee;
    }

    public BigDecimal getSreLvlTwoAssignee() {
        return sreLvlTwoAssignee;
    }


    public void setSreSrtCode(BigDecimal sreSrtCode) {
        this.sreSrtCode = sreSrtCode;
    }

    public BigDecimal getSreSrtCode() {
        return sreSrtCode;
    }

    public void setSreSrtName(String sreSrtName) {
        this.sreSrtName = sreSrtName;
    }

    public String getSreSrtName() {
        return sreSrtName;
    }

    public void setSreLvlOneAssigneeName(String sreLvlOneAssigneeName) {
        this.sreLvlOneAssigneeName = sreLvlOneAssigneeName;
    }

    public String getSreLvlOneAssigneeName() {
        return sreLvlOneAssigneeName;
    }

    public void setSreLvlTwoAssigneeName(String sreLvlTwoAssigneeName) {
        this.sreLvlTwoAssigneeName = sreLvlTwoAssigneeName;
    }

    public String getSreLvlTwoAssigneeName() {
        return sreLvlTwoAssigneeName;
    }

    public void setToUserCode(BigDecimal toUserCode) {
        this.toUserCode = toUserCode;
    }

    public BigDecimal getToUserCode() {
        return toUserCode;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    void setLevels(List<Escalations> levels) {
        this.levels=levels;
    }

    public List<Escalations> getLevels() {
        return levels;
    }
}
