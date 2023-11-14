package TurnQuest.view.Usr;


import TurnQuest.view.roles.Role;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


public class User {
    private String pin;

    public User() {
    }
    private BigDecimal userCode;
    private String username;
    private String userFullname;
    private String userEmail;
    private String userPersonnelRank;
    private Date userDateCreated;
    private String userType;
    private String userStatus;
    private String userPassReset;
    private String userPerId;
    private String userCreatedBy;
    private String userAccManager;
    private BigDecimal sysCode;
    private String sysShtDesc;
    private String sysName;
    private String sysActive;
    private BigDecimal UsrCode;
    private String usrName;
    private String usrFullname;

    private Date sysWef;
    private Date sysWet;

    private BigDecimal userSysCode;
    private BigDecimal userSys_userCode;
    private BigDecimal userSys_sysCode;

    private BigDecimal gusr_Code;
    private BigDecimal grpUsrCode;
    private Boolean userSelected;

    private BigDecimal gtUsrcode;
    private BigDecimal gtUsrGrptUsrCode;

    private String nodeType; // Custom field used to check whether this is a
    // primary or secondary field 'P' or 'S'
    private String phoneNumber;
    private List<Role> rolesList;

    private String usrSecurityQuestion;
    private String usrSecurityAnswer;

    private BigDecimal SACT_CODE;
    private BigDecimal USR_SACT_CODE;
    private String SACT_SHT_DESC;
    private String SACT_DESCRIPTION;
    private String userAuthorized;
    private String userAuthorizedBy;
    private String updatedBy;
    private Date updatedDate;

    private String teamLeader;


    private BigDecimal kpiCode;
    private BigDecimal kpiUserGroupCode;
    private BigDecimal kpiTaskCode;
    private BigDecimal kpiSubTaskCode;
    private String kpiTask;
    private String kpiSubTask;
    private String kpiParameter;
    private String kpiUnit;
    private String kpiComment;

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

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserPersonnelRank(String userPersonnelRank) {
        this.userPersonnelRank = userPersonnelRank;
    }

    public String getUserPersonnelRank() {
        return userPersonnelRank;
    }

    public void setUserDateCreated(Date userDateCreated) {
        this.userDateCreated = userDateCreated;
    }

    public Date getUserDateCreated() {
        return userDateCreated;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserPassReset(String userPassReset) {
        this.userPassReset = userPassReset;
    }

    public String getUserPassReset() {
        return userPassReset;
    }

    public void setSysCode(BigDecimal sysCode) {
        this.sysCode = sysCode;
    }

    public BigDecimal getSysCode() {
        return sysCode;
    }

    public void setSysShtDesc(String sysShtDesc) {
        this.sysShtDesc = sysShtDesc;
    }

    public String getSysShtDesc() {
        return sysShtDesc;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysActive(String sysActive) {
        this.sysActive = sysActive;
    }

    public String getSysActive() {
        return sysActive;
    }

    public void setSysWef(Date sysWef) {
        this.sysWef = sysWef;
    }

    public Date getSysWef() {
        return sysWef;
    }

    public void setUserSysCode(BigDecimal userSysCode) {
        this.userSysCode = userSysCode;
    }

    public BigDecimal getUserSysCode() {
        return userSysCode;
    }

    public void setSysWet(Date sysWet) {
        this.sysWet = sysWet;
    }

    public Date getSysWet() {
        return sysWet;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setUserSys_userCode(BigDecimal userSys_userCode) {
        this.userSys_userCode = userSys_userCode;
    }

    public BigDecimal getUserSys_userCode() {
        return userSys_userCode;
    }

    public void setUserSys_sysCode(BigDecimal userSys_sysCode) {
        this.userSys_sysCode = userSys_sysCode;
    }

    public BigDecimal getUserSys_sysCode() {
        return userSys_sysCode;
    }

    public void setRolesList(List<Role> rolesList) {
        this.rolesList = rolesList;
    }

    public List<Role> getRolesList() {
        return rolesList;
    }

    public void setUserPerId(String userPerId) {
        this.userPerId = userPerId;
    }

    public String getUserPerId() {
        return userPerId;
    }

    public void setUserCreatedBy(String userCreatedBy) {
        this.userCreatedBy = userCreatedBy;
    }

    public String getUserCreatedBy() {
        return userCreatedBy;
    }

    public void setGusr_Code(BigDecimal gusr_Code) {
        this.gusr_Code = gusr_Code;
    }

    public BigDecimal getGusr_Code() {
        return gusr_Code;
    }

    public void setGrpUsrCode(BigDecimal grpUsrCode) {
        this.grpUsrCode = grpUsrCode;
    }

    public BigDecimal getGrpUsrCode() {
        return grpUsrCode;
    }

    public void setUserSelected(Boolean userSelected) {
        this.userSelected = userSelected;
    }

    public Boolean getUserSelected() {
        return userSelected;
    }

    public void setUserAccManager(String userAccManager) {
        this.userAccManager = userAccManager;
    }

    public String getUserAccManager() {
        return userAccManager;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setUsrSecurityQuestion(String usrSecurityQuestion) {
        this.usrSecurityQuestion = usrSecurityQuestion;
    }

    public String getUsrSecurityQuestion() {
        return usrSecurityQuestion;
    }

    public void setUsrSecurityAnswer(String usrSecurityAnswer) {
        this.usrSecurityAnswer = usrSecurityAnswer;
    }

    public String getUsrSecurityAnswer() {
        return usrSecurityAnswer;
    }

    public void setSACT_CODE(BigDecimal SACT_CODE) {
        this.SACT_CODE = SACT_CODE;
    }

    public BigDecimal getSACT_CODE() {
        return SACT_CODE;
    }

    public void setSACT_SHT_DESC(String SACT_SHT_DESC) {
        this.SACT_SHT_DESC = SACT_SHT_DESC;
    }

    public String getSACT_SHT_DESC() {
        return SACT_SHT_DESC;
    }

    public void setSACT_DESCRIPTION(String SACT_DESCRIPTION) {
        this.SACT_DESCRIPTION = SACT_DESCRIPTION;
    }

    public String getSACT_DESCRIPTION() {
        return SACT_DESCRIPTION;
    }

    public void setUSR_SACT_CODE(BigDecimal USR_SACT_CODE) {
        this.USR_SACT_CODE = USR_SACT_CODE;
    }

    public BigDecimal getUSR_SACT_CODE() {
        return USR_SACT_CODE;
    }

    public void setUserAuthorized(String userAuthorized) {
        this.userAuthorized = userAuthorized;
    }

    public String getUserAuthorized() {
        return userAuthorized;
    }

    public void setUserAuthorizedBy(String userAuthorizedBy) {
        this.userAuthorizedBy = userAuthorizedBy;
    }

    public String getUserAuthorizedBy() {
        return userAuthorizedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setPin(String s) {
        this.pin = s;
    }

    public String getPin() {
        return pin;
    }

    public void setUsrCode(BigDecimal UsrCode) {
        this.UsrCode = UsrCode;
    }

    public BigDecimal getUsrCode() {
        return UsrCode;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrFullname(String usrFullname) {
        this.usrFullname = usrFullname;
    }

    public String getUsrFullname() {
        return usrFullname;
    }

    public void setGtUsrGrptUsrCode(BigDecimal gtUsrGrptUsrCode) {
        this.gtUsrGrptUsrCode = gtUsrGrptUsrCode;
    }

    public BigDecimal getGtUsrGrptUsrCode() {
        return gtUsrGrptUsrCode;
    }

    public void setGtUsrcode(BigDecimal gtUsrcode) {
        this.gtUsrcode = gtUsrcode;
    }

    public BigDecimal getGtUsrcode() {
        return gtUsrcode;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setKpiCode(BigDecimal kpiCode) {
        this.kpiCode = kpiCode;
    }

    public BigDecimal getKpiCode() {
        return kpiCode;
    }

    public void setKpiUserGroupCode(BigDecimal kpiUserGroupCode) {
        this.kpiUserGroupCode = kpiUserGroupCode;
    }

    public BigDecimal getKpiUserGroupCode() {
        return kpiUserGroupCode;
    }

    public void setKpiTaskCode(BigDecimal kpiTaskCode) {
        this.kpiTaskCode = kpiTaskCode;
    }

    public BigDecimal getKpiTaskCode() {
        return kpiTaskCode;
    }

    public void setKpiSubTaskCode(BigDecimal kpiSubTaskCode) {
        this.kpiSubTaskCode = kpiSubTaskCode;
    }

    public BigDecimal getKpiSubTaskCode() {
        return kpiSubTaskCode;
    }

    public void setKpiTask(String kpiTask) {
        this.kpiTask = kpiTask;
    }

    public String getKpiTask() {
        return kpiTask;
    }

    public void setKpiSubTask(String kpiSubTask) {
        this.kpiSubTask = kpiSubTask;
    }

    public String getKpiSubTask() {
        return kpiSubTask;
    }

    public void setKpiParameter(String kpiParameter) {
        this.kpiParameter = kpiParameter;
    }

    public String getKpiParameter() {
        return kpiParameter;
    }

    public void setKpiUnit(String kpiUnit) {
        this.kpiUnit = kpiUnit;
    }

    public String getKpiUnit() {
        return kpiUnit;
    }

    public void setKpiComment(String kpiComment) {
        this.kpiComment = kpiComment;
    }

    public String getKpiComment() {
        return kpiComment;
    }
}
